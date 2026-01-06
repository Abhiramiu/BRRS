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
    BRRS_GL_SCH_Manual_Summary_Repo GL_SCH_Manual_summary_repo;

    @Autowired
    BRRS_GL_SCH_Manual_Archival_Summary_Repo GL_SCH_Manual_Archival_Summary_Repo;

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

    public byte[] getGL_SCHExcel(String filename, String reportId, String fromdate, String todate, String currency,
            String dtltype, String type, String version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");

        // ARCHIVAL check
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
            logger.info("Service: Generating ARCHIVAL report for version {}", version);
            return getExcelGL_SCHARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
        }

        // Fetch data

        List<GL_SCH_Summary_Entity1> dataList = GL_SCH_summary_repo1
                .getdatabydateList(dateformat.parse(todate));
        List<GL_SCH_Summary_Entity2> dataList1 = GL_SCH_summary_repo2
                .getdatabydateList(dateformat.parse(todate));
        List<GL_SCH_Summary_Entity3> dataList2 = GL_SCH_summary_repo3
                .getdatabydateList(dateformat.parse(todate));
        List<GL_SCH_Manual_Summary_Entity> dataList3 = GL_SCH_Manual_summary_repo
                .getdatabydateList(dateformat.parse(todate));

        if (dataList.isEmpty() && dataList1.isEmpty() && dataList2.isEmpty() && dataList3.isEmpty()) {
            logger.warn("Service: No data found for GL_SCHS report. Returning empty result.");
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

            if (!dataList.isEmpty()) {
                populateEntity1Data(sheet, dataList.get(0), textStyle, numberStyle);
            }

            if (!dataList1.isEmpty()) {
                populateEntity2Data(sheet, dataList1.get(0), textStyle, numberStyle);
            }

            if (!dataList2.isEmpty()) {
                populateEntity3Data(sheet, dataList2.get(0), textStyle, numberStyle);
            }

            if (!dataList3.isEmpty()) {
                populateEntity4Data(sheet, dataList3.get(0), textStyle, numberStyle);
            }

			/* workbook.getCreationHelper().createFormulaEvaluator().evaluateAll(); */
            workbook.write(out);
            logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
            return out.toByteArray();
        }
    }

    private void populateEntity1Data(Sheet sheet, GL_SCH_Summary_Entity1 record, CellStyle textStyle,
            CellStyle numberStyle) {

        Row row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);
        Cell R11Cell1 = row.createCell(3);
        if (record.getR11_FIG_BAL_BWP1() != null) {
            R11Cell1.setCellValue(record.getR11_FIG_BAL_BWP1().doubleValue());
            R11Cell1.setCellStyle(numberStyle);
        } else {
            R11Cell1.setCellValue("");
            R11Cell1.setCellStyle(textStyle);
        }

        // R11 Col E
        Cell R11Cell2 = row.createCell(4);
        if (record.getR11_FIG_BAL_BWP2() != null) {
            R11Cell2.setCellValue(record.getR11_FIG_BAL_BWP2().doubleValue());
            R11Cell2.setCellStyle(numberStyle);
        } else {
            R11Cell2.setCellValue("");
            R11Cell2.setCellStyle(textStyle);
        }

        // R11 Col F
        Cell R11Cell3 = row.createCell(5);
        if (record.getR11_AMT_ADJ_BWP1() != null) {
            R11Cell3.setCellValue(record.getR11_AMT_ADJ_BWP1().doubleValue());
            R11Cell3.setCellStyle(numberStyle);
        } else {
            R11Cell3.setCellValue("");
            R11Cell3.setCellStyle(textStyle);
        }
        // R11 Col G
        Cell R11Cell4 = row.createCell(6);
        if (record.getR11_AMT_ADJ_BWP2() != null) {
            R11Cell4.setCellValue(record.getR11_AMT_ADJ_BWP2().doubleValue());
            R11Cell4.setCellStyle(numberStyle);
        } else {
            R11Cell4.setCellValue("");
            R11Cell4.setCellStyle(textStyle);
        }
        // R11 Col H
        Cell R11Cell5 = row.createCell(7);
        if (record.getR11_NET_AMT_BWP1() != null) {
            R11Cell5.setCellValue(record.getR11_NET_AMT_BWP1().doubleValue());
            R11Cell5.setCellStyle(numberStyle);
        } else {
            R11Cell5.setCellValue("");
            R11Cell5.setCellStyle(textStyle);
        }
        // R11 Col I
        Cell R11Cell6 = row.createCell(8);
        if (record.getR11_NET_AMT_BWP2() != null) {
            R11Cell6.setCellValue(record.getR11_NET_AMT_BWP2().doubleValue());
            R11Cell6.setCellStyle(numberStyle);
        } else {
            R11Cell6.setCellValue("");
            R11Cell6.setCellStyle(textStyle);
        }
        // R11 Col J
        Cell R11Cell7 = row.createCell(9);
        if (record.getR11_BAL_SUB_BWP1() != null) {
            R11Cell7.setCellValue(record.getR11_BAL_SUB_BWP1().doubleValue());
            R11Cell7.setCellStyle(numberStyle);
        } else {
            R11Cell7.setCellValue("");
            R11Cell7.setCellStyle(textStyle);
        }
        // R11 Col K
        Cell R11Cell8 = row.createCell(10);
        if (record.getR11_BAL_SUB_BWP2() != null) {
            R11Cell8.setCellValue(record.getR11_BAL_SUB_BWP2().doubleValue());
            R11Cell8.setCellStyle(numberStyle);
        } else {
            R11Cell8.setCellValue("");
            R11Cell8.setCellStyle(textStyle);
        }
        // R11 Col L
        Cell R11Cell9 = row.createCell(11);
        if (record.getR11_BAL_ACT_SUB_BWP1() != null) {
            R11Cell9.setCellValue(record.getR11_BAL_ACT_SUB_BWP1().doubleValue());
            R11Cell9.setCellStyle(numberStyle);
        } else {
            R11Cell9.setCellValue("");
            R11Cell9.setCellStyle(textStyle);
        }
        // R11 Col M
        Cell R11Cell10 = row.createCell(12);
        if (record.getR11_BAL_ACT_SUB_BWP2() != null) {
            R11Cell10.setCellValue(record.getR11_BAL_ACT_SUB_BWP2().doubleValue());
            R11Cell10.setCellStyle(numberStyle);
        } else {
            R11Cell10.setCellValue("");
            R11Cell10.setCellStyle(textStyle);
        }
        // R12 Col D
        row = sheet.getRow(11);
        Cell R12Cell1 = row.createCell(3);
        if (record.getR12_FIG_BAL_BWP1() != null) {
            R12Cell1.setCellValue(record.getR12_FIG_BAL_BWP1().doubleValue());
            R12Cell1.setCellStyle(numberStyle);
        } else {
            R12Cell1.setCellValue("");
            R12Cell1.setCellStyle(textStyle);
        }

        // R12 Col E
        Cell R12Cell2 = row.createCell(4);
        if (record.getR12_FIG_BAL_BWP2() != null) {
            R12Cell2.setCellValue(record.getR12_FIG_BAL_BWP2().doubleValue());
            R12Cell2.setCellStyle(numberStyle);
        } else {
            R12Cell2.setCellValue("");
            R12Cell2.setCellStyle(textStyle);
        }

        // R12 Col F
        Cell R12Cell3 = row.createCell(5);
        if (record.getR12_AMT_ADJ_BWP1() != null) {
            R12Cell3.setCellValue(record.getR12_AMT_ADJ_BWP1().doubleValue());
            R12Cell3.setCellStyle(numberStyle);
        } else {
            R12Cell3.setCellValue("");
            R12Cell3.setCellStyle(textStyle);
        }

        // R12 Col G
        Cell R12Cell4 = row.createCell(6);
        if (record.getR12_AMT_ADJ_BWP2() != null) {
            R12Cell4.setCellValue(record.getR12_AMT_ADJ_BWP2().doubleValue());
            R12Cell4.setCellStyle(numberStyle);
        } else {
            R12Cell4.setCellValue("");
            R12Cell4.setCellStyle(textStyle);
        }

        // R12 Col H
        Cell R12Cell5 = row.createCell(7);
        if (record.getR12_NET_AMT_BWP1() != null) {
            R12Cell5.setCellValue(record.getR12_NET_AMT_BWP1().doubleValue());
            R12Cell5.setCellStyle(numberStyle);
        } else {
            R12Cell5.setCellValue("");
            R12Cell5.setCellStyle(textStyle);
        }

        // R12 Col I
        Cell R12Cell6 = row.createCell(8);
        if (record.getR12_NET_AMT_BWP2() != null) {
            R12Cell6.setCellValue(record.getR12_NET_AMT_BWP2().doubleValue());
            R12Cell6.setCellStyle(numberStyle);
        } else {
            R12Cell6.setCellValue("");
            R12Cell6.setCellStyle(textStyle);
        }

        // R12 Col J
        Cell R12Cell7 = row.createCell(9);
        if (record.getR12_BAL_SUB_BWP1() != null) {
            R12Cell7.setCellValue(record.getR12_BAL_SUB_BWP1().doubleValue());
            R12Cell7.setCellStyle(numberStyle);
        } else {
            R12Cell7.setCellValue("");
            R12Cell7.setCellStyle(textStyle);
        }

        // R12 Col K
        Cell R12Cell8 = row.createCell(10);
        if (record.getR12_BAL_SUB_BWP2() != null) {
            R12Cell8.setCellValue(record.getR12_BAL_SUB_BWP2().doubleValue());
            R12Cell8.setCellStyle(numberStyle);
        } else {
            R12Cell8.setCellValue("");
            R12Cell8.setCellStyle(textStyle);
        }

        // R12 Col L
        Cell R12Cell9 = row.createCell(11);
        if (record.getR12_BAL_ACT_SUB_BWP1() != null) {
            R12Cell9.setCellValue(record.getR12_BAL_ACT_SUB_BWP1().doubleValue());
            R12Cell9.setCellStyle(numberStyle);
        } else {
            R12Cell9.setCellValue("");
            R12Cell9.setCellStyle(textStyle);
        }

        // R12 Col M
        Cell R12Cell10 = row.createCell(12);
        if (record.getR12_BAL_ACT_SUB_BWP2() != null) {
            R12Cell10.setCellValue(record.getR12_BAL_ACT_SUB_BWP2().doubleValue());
            R12Cell10.setCellStyle(numberStyle);
        } else {
            R12Cell10.setCellValue("");
            R12Cell10.setCellStyle(textStyle);
        }

        // R13 Col D
        row = sheet.getRow(12);
        Cell R13Cell1 = row.createCell(3);
        if (record.getR13_FIG_BAL_BWP1() != null) {
            R13Cell1.setCellValue(record.getR13_FIG_BAL_BWP1().doubleValue());
            R13Cell1.setCellStyle(numberStyle);
        } else {
            R13Cell1.setCellValue("");
            R13Cell1.setCellStyle(textStyle);
        }

        // R13 Col E
        Cell R13Cell2 = row.createCell(4);
        if (record.getR13_FIG_BAL_BWP2() != null) {
            R13Cell2.setCellValue(record.getR13_FIG_BAL_BWP2().doubleValue());
            R13Cell2.setCellStyle(numberStyle);
        } else {
            R13Cell2.setCellValue("");
            R13Cell2.setCellStyle(textStyle);
        }

        // R13 Col F
        Cell R13Cell3 = row.createCell(5);
        if (record.getR13_AMT_ADJ_BWP1() != null) {
            R13Cell3.setCellValue(record.getR13_AMT_ADJ_BWP1().doubleValue());
            R13Cell3.setCellStyle(numberStyle);
        } else {
            R13Cell3.setCellValue("");
            R13Cell3.setCellStyle(textStyle);
        }

        // R13 Col G
        Cell R13Cell4 = row.createCell(6);
        if (record.getR13_AMT_ADJ_BWP2() != null) {
            R13Cell4.setCellValue(record.getR13_AMT_ADJ_BWP2().doubleValue());
            R13Cell4.setCellStyle(numberStyle);
        } else {
            R13Cell4.setCellValue("");
            R13Cell4.setCellStyle(textStyle);
        }

        // R13 Col H
        Cell R13Cell5 = row.createCell(7);
        if (record.getR13_NET_AMT_BWP1() != null) {
            R13Cell5.setCellValue(record.getR13_NET_AMT_BWP1().doubleValue());
            R13Cell5.setCellStyle(numberStyle);
        } else {
            R13Cell5.setCellValue("");
            R13Cell5.setCellStyle(textStyle);
        }

        // R13 Col I
        Cell R13Cell6 = row.createCell(8);
        if (record.getR13_NET_AMT_BWP2() != null) {
            R13Cell6.setCellValue(record.getR13_NET_AMT_BWP2().doubleValue());
            R13Cell6.setCellStyle(numberStyle);
        } else {
            R13Cell6.setCellValue("");
            R13Cell6.setCellStyle(textStyle);
        }

        // R13 Col J
        Cell R13Cell7 = row.createCell(9);
        if (record.getR13_BAL_SUB_BWP1() != null) {
            R13Cell7.setCellValue(record.getR13_BAL_SUB_BWP1().doubleValue());
            R13Cell7.setCellStyle(numberStyle);
        } else {
            R13Cell7.setCellValue("");
            R13Cell7.setCellStyle(textStyle);
        }

        // R13 Col K
        Cell R13Cell8 = row.createCell(10);
        if (record.getR13_BAL_SUB_BWP2() != null) {
            R13Cell8.setCellValue(record.getR13_BAL_SUB_BWP2().doubleValue());
            R13Cell8.setCellStyle(numberStyle);
        } else {
            R13Cell8.setCellValue("");
            R13Cell8.setCellStyle(textStyle);
        }

        // R13 Col L
        Cell R13Cell9 = row.createCell(11);
        if (record.getR13_BAL_ACT_SUB_BWP1() != null) {
            R13Cell9.setCellValue(record.getR13_BAL_ACT_SUB_BWP1().doubleValue());
            R13Cell9.setCellStyle(numberStyle);
        } else {
            R13Cell9.setCellValue("");
            R13Cell9.setCellStyle(textStyle);
        }

        // R13 Col M
        Cell R13Cell10 = row.createCell(12);
        if (record.getR13_BAL_ACT_SUB_BWP2() != null) {
            R13Cell10.setCellValue(record.getR13_BAL_ACT_SUB_BWP2().doubleValue());
            R13Cell10.setCellStyle(numberStyle);
        } else {
            R13Cell10.setCellValue("");
            R13Cell10.setCellStyle(textStyle);
        }

        // R14 Col D
        row = sheet.getRow(13);
        Cell R14Cell1 = row.createCell(3);
        if (record.getR14_FIG_BAL_BWP1() != null) {
            R14Cell1.setCellValue(record.getR14_FIG_BAL_BWP1().doubleValue());
            R14Cell1.setCellStyle(numberStyle);
        } else {
            R14Cell1.setCellValue("");
            R14Cell1.setCellStyle(textStyle);
        }

        // R14 Col E
        Cell R14Cell2 = row.createCell(4);
        if (record.getR14_FIG_BAL_BWP2() != null) {
            R14Cell2.setCellValue(record.getR14_FIG_BAL_BWP2().doubleValue());
            R14Cell2.setCellStyle(numberStyle);
        } else {
            R14Cell2.setCellValue("");
            R14Cell2.setCellStyle(textStyle);
        }

        // R14 Col F
        Cell R14Cell3 = row.createCell(5);
        if (record.getR14_AMT_ADJ_BWP1() != null) {
            R14Cell3.setCellValue(record.getR14_AMT_ADJ_BWP1().doubleValue());
            R14Cell3.setCellStyle(numberStyle);
        } else {
            R14Cell3.setCellValue("");
            R14Cell3.setCellStyle(textStyle);
        }

        // R14 Col G
        Cell R14Cell4 = row.createCell(6);
        if (record.getR14_AMT_ADJ_BWP2() != null) {
            R14Cell4.setCellValue(record.getR14_AMT_ADJ_BWP2().doubleValue());
            R14Cell4.setCellStyle(numberStyle);
        } else {
            R14Cell4.setCellValue("");
            R14Cell4.setCellStyle(textStyle);
        }

        // R14 Col H
        Cell R14Cell5 = row.createCell(7);
        if (record.getR14_NET_AMT_BWP1() != null) {
            R14Cell5.setCellValue(record.getR14_NET_AMT_BWP1().doubleValue());
            R14Cell5.setCellStyle(numberStyle);
        } else {
            R14Cell5.setCellValue("");
            R14Cell5.setCellStyle(textStyle);
        }

        // R14 Col I
        Cell R14Cell6 = row.createCell(8);
        if (record.getR14_NET_AMT_BWP2() != null) {
            R14Cell6.setCellValue(record.getR14_NET_AMT_BWP2().doubleValue());
            R14Cell6.setCellStyle(numberStyle);
        } else {
            R14Cell6.setCellValue("");
            R14Cell6.setCellStyle(textStyle);
        }

        // R14 Col J
        Cell R14Cell7 = row.createCell(9);
        if (record.getR14_BAL_SUB_BWP1() != null) {
            R14Cell7.setCellValue(record.getR14_BAL_SUB_BWP1().doubleValue());
            R14Cell7.setCellStyle(numberStyle);
        } else {
            R14Cell7.setCellValue("");
            R14Cell7.setCellStyle(textStyle);
        }

        // R14 Col K
        Cell R14Cell8 = row.createCell(10);
        if (record.getR14_BAL_SUB_BWP2() != null) {
            R14Cell8.setCellValue(record.getR14_BAL_SUB_BWP2().doubleValue());
            R14Cell8.setCellStyle(numberStyle);
        } else {
            R14Cell8.setCellValue("");
            R14Cell8.setCellStyle(textStyle);
        }

        // R14 Col L
        Cell R14Cell9 = row.createCell(11);
        if (record.getR14_BAL_ACT_SUB_BWP1() != null) {
            R14Cell9.setCellValue(record.getR14_BAL_ACT_SUB_BWP1().doubleValue());
            R14Cell9.setCellStyle(numberStyle);
        } else {
            R14Cell9.setCellValue("");
            R14Cell9.setCellStyle(textStyle);
        }

        // R14 Col M
        Cell R14Cell10 = row.createCell(12);
        if (record.getR14_BAL_ACT_SUB_BWP2() != null) {
            R14Cell10.setCellValue(record.getR14_BAL_ACT_SUB_BWP2().doubleValue());
            R14Cell10.setCellStyle(numberStyle);
        } else {
            R14Cell10.setCellValue("");
            R14Cell10.setCellStyle(textStyle);
        }
        /* ================= R16 ================= */
        row = sheet.getRow(15);
        Cell R16Cell1 = row.createCell(3);
        if (record.getR16_FIG_BAL_BWP1() != null) {
            R16Cell1.setCellValue(record.getR16_FIG_BAL_BWP1().doubleValue());
            R16Cell1.setCellStyle(numberStyle);
        } else {
            R16Cell1.setCellValue("");
            R16Cell1.setCellStyle(textStyle);
        }
        Cell R16Cell2 = row.createCell(4);
        if (record.getR16_FIG_BAL_BWP2() != null) {
            R16Cell2.setCellValue(record.getR16_FIG_BAL_BWP2().doubleValue());
            R16Cell2.setCellStyle(numberStyle);
        } else {
            R16Cell2.setCellValue("");
            R16Cell2.setCellStyle(textStyle);
        }
        Cell R16Cell3 = row.createCell(5);
        if (record.getR16_AMT_ADJ_BWP1() != null) {
            R16Cell3.setCellValue(record.getR16_AMT_ADJ_BWP1().doubleValue());
            R16Cell3.setCellStyle(numberStyle);
        } else {
            R16Cell3.setCellValue("");
            R16Cell3.setCellStyle(textStyle);
        }
        Cell R16Cell4 = row.createCell(6);
        if (record.getR16_AMT_ADJ_BWP2() != null) {
            R16Cell4.setCellValue(record.getR16_AMT_ADJ_BWP2().doubleValue());
            R16Cell4.setCellStyle(numberStyle);
        } else {
            R16Cell4.setCellValue("");
            R16Cell4.setCellStyle(textStyle);
        }
        Cell R16Cell5 = row.createCell(7);
        if (record.getR16_NET_AMT_BWP1() != null) {
            R16Cell5.setCellValue(record.getR16_NET_AMT_BWP1().doubleValue());
            R16Cell5.setCellStyle(numberStyle);
        } else {
            R16Cell5.setCellValue("");
            R16Cell5.setCellStyle(textStyle);
        }
        Cell R16Cell6 = row.createCell(8);
        if (record.getR16_NET_AMT_BWP2() != null) {
            R16Cell6.setCellValue(record.getR16_NET_AMT_BWP2().doubleValue());
            R16Cell6.setCellStyle(numberStyle);
        } else {
            R16Cell6.setCellValue("");
            R16Cell6.setCellStyle(textStyle);
        }
        Cell R16Cell7 = row.createCell(9);
        if (record.getR16_BAL_SUB_BWP1() != null) {
            R16Cell7.setCellValue(record.getR16_BAL_SUB_BWP1().doubleValue());
            R16Cell7.setCellStyle(numberStyle);
        } else {
            R16Cell7.setCellValue("");
            R16Cell7.setCellStyle(textStyle);
        }
        Cell R16Cell8 = row.createCell(10);
        if (record.getR16_BAL_SUB_BWP2() != null) {
            R16Cell8.setCellValue(record.getR16_BAL_SUB_BWP2().doubleValue());
            R16Cell8.setCellStyle(numberStyle);
        } else {
            R16Cell8.setCellValue("");
            R16Cell8.setCellStyle(textStyle);
        }
        Cell R16Cell9 = row.createCell(11);
        if (record.getR16_BAL_ACT_SUB_BWP1() != null) {
            R16Cell9.setCellValue(record.getR16_BAL_ACT_SUB_BWP1().doubleValue());
            R16Cell9.setCellStyle(numberStyle);
        } else {
            R16Cell9.setCellValue("");
            R16Cell9.setCellStyle(textStyle);
        }
        Cell R16Cell10 = row.createCell(12);
        if (record.getR16_BAL_ACT_SUB_BWP2() != null) {
            R16Cell10.setCellValue(record.getR16_BAL_ACT_SUB_BWP2().doubleValue());
            R16Cell10.setCellStyle(numberStyle);
        } else {
            R16Cell10.setCellValue("");
            R16Cell10.setCellStyle(textStyle);
        }

        /* ================= R17 ================= */
        row = sheet.getRow(16);
        Cell R17Cell1 = row.createCell(3);
        if (record.getR17_FIG_BAL_BWP1() != null) {
            R17Cell1.setCellValue(record.getR17_FIG_BAL_BWP1().doubleValue());
            R17Cell1.setCellStyle(numberStyle);
        } else {
            R17Cell1.setCellValue("");
            R17Cell1.setCellStyle(textStyle);
        }
        Cell R17Cell2 = row.createCell(4);
        if (record.getR17_FIG_BAL_BWP2() != null) {
            R17Cell2.setCellValue(record.getR17_FIG_BAL_BWP2().doubleValue());
            R17Cell2.setCellStyle(numberStyle);
        } else {
            R17Cell2.setCellValue("");
            R17Cell2.setCellStyle(textStyle);
        }
        Cell R17Cell3 = row.createCell(5);
        if (record.getR17_AMT_ADJ_BWP1() != null) {
            R17Cell3.setCellValue(record.getR17_AMT_ADJ_BWP1().doubleValue());
            R17Cell3.setCellStyle(numberStyle);
        } else {
            R17Cell3.setCellValue("");
            R17Cell3.setCellStyle(textStyle);
        }
        Cell R17Cell4 = row.createCell(6);
        if (record.getR17_AMT_ADJ_BWP2() != null) {
            R17Cell4.setCellValue(record.getR17_AMT_ADJ_BWP2().doubleValue());
            R17Cell4.setCellStyle(numberStyle);
        } else {
            R17Cell4.setCellValue("");
            R17Cell4.setCellStyle(textStyle);
        }
        Cell R17Cell5 = row.createCell(7);
        if (record.getR17_NET_AMT_BWP1() != null) {
            R17Cell5.setCellValue(record.getR17_NET_AMT_BWP1().doubleValue());
            R17Cell5.setCellStyle(numberStyle);
        } else {
            R17Cell5.setCellValue("");
            R17Cell5.setCellStyle(textStyle);
        }
        Cell R17Cell6 = row.createCell(8);
        if (record.getR17_NET_AMT_BWP2() != null) {
            R17Cell6.setCellValue(record.getR17_NET_AMT_BWP2().doubleValue());
            R17Cell6.setCellStyle(numberStyle);
        } else {
            R17Cell6.setCellValue("");
            R17Cell6.setCellStyle(textStyle);
        }
        Cell R17Cell7 = row.createCell(9);
        if (record.getR17_BAL_SUB_BWP1() != null) {
            R17Cell7.setCellValue(record.getR17_BAL_SUB_BWP1().doubleValue());
            R17Cell7.setCellStyle(numberStyle);
        } else {
            R17Cell7.setCellValue("");
            R17Cell7.setCellStyle(textStyle);
        }
        Cell R17Cell8 = row.createCell(10);
        if (record.getR17_BAL_SUB_BWP2() != null) {
            R17Cell8.setCellValue(record.getR17_BAL_SUB_BWP2().doubleValue());
            R17Cell8.setCellStyle(numberStyle);
        } else {
            R17Cell8.setCellValue("");
            R17Cell8.setCellStyle(textStyle);
        }
        Cell R17Cell9 = row.createCell(11);
        if (record.getR17_BAL_ACT_SUB_BWP1() != null) {
            R17Cell9.setCellValue(record.getR17_BAL_ACT_SUB_BWP1().doubleValue());
            R17Cell9.setCellStyle(numberStyle);
        } else {
            R17Cell9.setCellValue("");
            R17Cell9.setCellStyle(textStyle);
        }
        Cell R17Cell10 = row.createCell(12);
        if (record.getR17_BAL_ACT_SUB_BWP2() != null) {
            R17Cell10.setCellValue(record.getR17_BAL_ACT_SUB_BWP2().doubleValue());
            R17Cell10.setCellStyle(numberStyle);
        } else {
            R17Cell10.setCellValue("");
            R17Cell10.setCellStyle(textStyle);
        }

        /* ================= R18 ================= */
        row = sheet.getRow(17);
        Cell R18Cell1 = row.createCell(3);
        if (record.getR18_FIG_BAL_BWP1() != null) {
            R18Cell1.setCellValue(record.getR18_FIG_BAL_BWP1().doubleValue());
            R18Cell1.setCellStyle(numberStyle);
        } else {
            R18Cell1.setCellValue("");
            R18Cell1.setCellStyle(textStyle);
        }
        Cell R18Cell2 = row.createCell(4);
        if (record.getR18_FIG_BAL_BWP2() != null) {
            R18Cell2.setCellValue(record.getR18_FIG_BAL_BWP2().doubleValue());
            R18Cell2.setCellStyle(numberStyle);
        } else {
            R18Cell2.setCellValue("");
            R18Cell2.setCellStyle(textStyle);
        }
        Cell R18Cell3 = row.createCell(5);
        if (record.getR18_AMT_ADJ_BWP1() != null) {
            R18Cell3.setCellValue(record.getR18_AMT_ADJ_BWP1().doubleValue());
            R18Cell3.setCellStyle(numberStyle);
        } else {
            R18Cell3.setCellValue("");
            R18Cell3.setCellStyle(textStyle);
        }
        Cell R18Cell4 = row.createCell(6);
        if (record.getR18_AMT_ADJ_BWP2() != null) {
            R18Cell4.setCellValue(record.getR18_AMT_ADJ_BWP2().doubleValue());
            R18Cell4.setCellStyle(numberStyle);
        } else {
            R18Cell4.setCellValue("");
            R18Cell4.setCellStyle(textStyle);
        }
        Cell R18Cell5 = row.createCell(7);
        if (record.getR18_NET_AMT_BWP1() != null) {
            R18Cell5.setCellValue(record.getR18_NET_AMT_BWP1().doubleValue());
            R18Cell5.setCellStyle(numberStyle);
        } else {
            R18Cell5.setCellValue("");
            R18Cell5.setCellStyle(textStyle);
        }
        Cell R18Cell6 = row.createCell(8);
        if (record.getR18_NET_AMT_BWP2() != null) {
            R18Cell6.setCellValue(record.getR18_NET_AMT_BWP2().doubleValue());
            R18Cell6.setCellStyle(numberStyle);
        } else {
            R18Cell6.setCellValue("");
            R18Cell6.setCellStyle(textStyle);
        }
        Cell R18Cell7 = row.createCell(9);
        if (record.getR18_BAL_SUB_BWP1() != null) {
            R18Cell7.setCellValue(record.getR18_BAL_SUB_BWP1().doubleValue());
            R18Cell7.setCellStyle(numberStyle);
        } else {
            R18Cell7.setCellValue("");
            R18Cell7.setCellStyle(textStyle);
        }
        Cell R18Cell8 = row.createCell(10);
        if (record.getR18_BAL_SUB_BWP2() != null) {
            R18Cell8.setCellValue(record.getR18_BAL_SUB_BWP2().doubleValue());
            R18Cell8.setCellStyle(numberStyle);
        } else {
            R18Cell8.setCellValue("");
            R18Cell8.setCellStyle(textStyle);
        }
        Cell R18Cell9 = row.createCell(11);
        if (record.getR18_BAL_ACT_SUB_BWP1() != null) {
            R18Cell9.setCellValue(record.getR18_BAL_ACT_SUB_BWP1().doubleValue());
            R18Cell9.setCellStyle(numberStyle);
        } else {
            R18Cell9.setCellValue("");
            R18Cell9.setCellStyle(textStyle);
        }
        Cell R18Cell10 = row.createCell(12);
        if (record.getR18_BAL_ACT_SUB_BWP2() != null) {
            R18Cell10.setCellValue(record.getR18_BAL_ACT_SUB_BWP2().doubleValue());
            R18Cell10.setCellStyle(numberStyle);
        } else {
            R18Cell10.setCellValue("");
            R18Cell10.setCellStyle(textStyle);
        }

        /* ================= R19 ================= */
        row = sheet.getRow(18);
        Cell R19Cell1 = row.createCell(3);
        if (record.getR19_FIG_BAL_BWP1() != null) {
            R19Cell1.setCellValue(record.getR19_FIG_BAL_BWP1().doubleValue());
            R19Cell1.setCellStyle(numberStyle);
        } else {
            R19Cell1.setCellValue("");
            R19Cell1.setCellStyle(textStyle);
        }
        Cell R19Cell2 = row.createCell(4);
        if (record.getR19_FIG_BAL_BWP2() != null) {
            R19Cell2.setCellValue(record.getR19_FIG_BAL_BWP2().doubleValue());
            R19Cell2.setCellStyle(numberStyle);
        } else {
            R19Cell2.setCellValue("");
            R19Cell2.setCellStyle(textStyle);
        }
        Cell R19Cell3 = row.createCell(5);
        if (record.getR19_AMT_ADJ_BWP1() != null) {
            R19Cell3.setCellValue(record.getR19_AMT_ADJ_BWP1().doubleValue());
            R19Cell3.setCellStyle(numberStyle);
        } else {
            R19Cell3.setCellValue("");
            R19Cell3.setCellStyle(textStyle);
        }
        Cell R19Cell4 = row.createCell(6);
        if (record.getR19_AMT_ADJ_BWP2() != null) {
            R19Cell4.setCellValue(record.getR19_AMT_ADJ_BWP2().doubleValue());
            R19Cell4.setCellStyle(numberStyle);
        } else {
            R19Cell4.setCellValue("");
            R19Cell4.setCellStyle(textStyle);
        }
        Cell R19Cell5 = row.createCell(7);
        if (record.getR19_NET_AMT_BWP1() != null) {
            R19Cell5.setCellValue(record.getR19_NET_AMT_BWP1().doubleValue());
            R19Cell5.setCellStyle(numberStyle);
        } else {
            R19Cell5.setCellValue("");
            R19Cell5.setCellStyle(textStyle);
        }
        Cell R19Cell6 = row.createCell(8);
        if (record.getR19_NET_AMT_BWP2() != null) {
            R19Cell6.setCellValue(record.getR19_NET_AMT_BWP2().doubleValue());
            R19Cell6.setCellStyle(numberStyle);
        } else {
            R19Cell6.setCellValue("");
            R19Cell6.setCellStyle(textStyle);
        }
        Cell R19Cell7 = row.createCell(9);
        if (record.getR19_BAL_SUB_BWP1() != null) {
            R19Cell7.setCellValue(record.getR19_BAL_SUB_BWP1().doubleValue());
            R19Cell7.setCellStyle(numberStyle);
        } else {
            R19Cell7.setCellValue("");
            R19Cell7.setCellStyle(textStyle);
        }
        Cell R19Cell8 = row.createCell(10);
        if (record.getR19_BAL_SUB_BWP2() != null) {
            R19Cell8.setCellValue(record.getR19_BAL_SUB_BWP2().doubleValue());
            R19Cell8.setCellStyle(numberStyle);
        } else {
            R19Cell8.setCellValue("");
            R19Cell8.setCellStyle(textStyle);
        }
        Cell R19Cell9 = row.createCell(11);
        if (record.getR19_BAL_ACT_SUB_BWP1() != null) {
            R19Cell9.setCellValue(record.getR19_BAL_ACT_SUB_BWP1().doubleValue());
            R19Cell9.setCellStyle(numberStyle);
        } else {
            R19Cell9.setCellValue("");
            R19Cell9.setCellStyle(textStyle);
        }
        Cell R19Cell10 = row.createCell(12);
        if (record.getR19_BAL_ACT_SUB_BWP2() != null) {
            R19Cell10.setCellValue(record.getR19_BAL_ACT_SUB_BWP2().doubleValue());
            R19Cell10.setCellStyle(numberStyle);
        } else {
            R19Cell10.setCellValue("");
            R19Cell10.setCellStyle(textStyle);
        }

        /* ================= R20 ================= */
        row = sheet.getRow(19);
        Cell R20Cell1 = row.createCell(3);
        if (record.getR20_FIG_BAL_BWP1() != null) {
            R20Cell1.setCellValue(record.getR20_FIG_BAL_BWP1().doubleValue());
            R20Cell1.setCellStyle(numberStyle);
        } else {
            R20Cell1.setCellValue("");
            R20Cell1.setCellStyle(textStyle);
        }
        Cell R20Cell2 = row.createCell(4);
        if (record.getR20_FIG_BAL_BWP2() != null) {
            R20Cell2.setCellValue(record.getR20_FIG_BAL_BWP2().doubleValue());
            R20Cell2.setCellStyle(numberStyle);
        } else {
            R20Cell2.setCellValue("");
            R20Cell2.setCellStyle(textStyle);
        }
        Cell R20Cell3 = row.createCell(5);
        if (record.getR20_AMT_ADJ_BWP1() != null) {
            R20Cell3.setCellValue(record.getR20_AMT_ADJ_BWP1().doubleValue());
            R20Cell3.setCellStyle(numberStyle);
        } else {
            R20Cell3.setCellValue("");
            R20Cell3.setCellStyle(textStyle);
        }
        Cell R20Cell4 = row.createCell(6);
        if (record.getR20_AMT_ADJ_BWP2() != null) {
            R20Cell4.setCellValue(record.getR20_AMT_ADJ_BWP2().doubleValue());
            R20Cell4.setCellStyle(numberStyle);
        } else {
            R20Cell4.setCellValue("");
            R20Cell4.setCellStyle(textStyle);
        }
        Cell R20Cell5 = row.createCell(7);
        if (record.getR20_NET_AMT_BWP1() != null) {
            R20Cell5.setCellValue(record.getR20_NET_AMT_BWP1().doubleValue());
            R20Cell5.setCellStyle(numberStyle);
        } else {
            R20Cell5.setCellValue("");
            R20Cell5.setCellStyle(textStyle);
        }
        Cell R20Cell6 = row.createCell(8);
        if (record.getR20_NET_AMT_BWP2() != null) {
            R20Cell6.setCellValue(record.getR20_NET_AMT_BWP2().doubleValue());
            R20Cell6.setCellStyle(numberStyle);
        } else {
            R20Cell6.setCellValue("");
            R20Cell6.setCellStyle(textStyle);
        }
        Cell R20Cell7 = row.createCell(9);
        if (record.getR20_BAL_SUB_BWP1() != null) {
            R20Cell7.setCellValue(record.getR20_BAL_SUB_BWP1().doubleValue());
            R20Cell7.setCellStyle(numberStyle);
        } else {
            R20Cell7.setCellValue("");
            R20Cell7.setCellStyle(textStyle);
        }
        Cell R20Cell8 = row.createCell(10);
        if (record.getR20_BAL_SUB_BWP2() != null) {
            R20Cell8.setCellValue(record.getR20_BAL_SUB_BWP2().doubleValue());
            R20Cell8.setCellStyle(numberStyle);
        } else {
            R20Cell8.setCellValue("");
            R20Cell8.setCellStyle(textStyle);
        }
        Cell R20Cell9 = row.createCell(11);
        if (record.getR20_BAL_ACT_SUB_BWP1() != null) {
            R20Cell9.setCellValue(record.getR20_BAL_ACT_SUB_BWP1().doubleValue());
            R20Cell9.setCellStyle(numberStyle);
        } else {
            R20Cell9.setCellValue("");
            R20Cell9.setCellStyle(textStyle);
        }
        Cell R20Cell10 = row.createCell(12);
        if (record.getR20_BAL_ACT_SUB_BWP2() != null) {
            R20Cell10.setCellValue(record.getR20_BAL_ACT_SUB_BWP2().doubleValue());
            R20Cell10.setCellStyle(numberStyle);
        } else {
            R20Cell10.setCellValue("");
            R20Cell10.setCellStyle(textStyle);
        }

        /* ================= R21 ================= */
        row = sheet.getRow(20);
        Cell R21Cell1 = row.createCell(3);
        if (record.getR21_FIG_BAL_BWP1() != null) {
            R21Cell1.setCellValue(record.getR21_FIG_BAL_BWP1().doubleValue());
            R21Cell1.setCellStyle(numberStyle);
        } else {
            R21Cell1.setCellValue("");
            R21Cell1.setCellStyle(textStyle);
        }
        Cell R21Cell2 = row.createCell(4);
        if (record.getR21_FIG_BAL_BWP2() != null) {
            R21Cell2.setCellValue(record.getR21_FIG_BAL_BWP2().doubleValue());
            R21Cell2.setCellStyle(numberStyle);
        } else {
            R21Cell2.setCellValue("");
            R21Cell2.setCellStyle(textStyle);
        }
        Cell R21Cell3 = row.createCell(5);
        if (record.getR21_AMT_ADJ_BWP1() != null) {
            R21Cell3.setCellValue(record.getR21_AMT_ADJ_BWP1().doubleValue());
            R21Cell3.setCellStyle(numberStyle);
        } else {
            R21Cell3.setCellValue("");
            R21Cell3.setCellStyle(textStyle);
        }
        Cell R21Cell4 = row.createCell(6);
        if (record.getR21_AMT_ADJ_BWP2() != null) {
            R21Cell4.setCellValue(record.getR21_AMT_ADJ_BWP2().doubleValue());
            R21Cell4.setCellStyle(numberStyle);
        } else {
            R21Cell4.setCellValue("");
            R21Cell4.setCellStyle(textStyle);
        }
        Cell R21Cell5 = row.createCell(7);
        if (record.getR21_NET_AMT_BWP1() != null) {
            R21Cell5.setCellValue(record.getR21_NET_AMT_BWP1().doubleValue());
            R21Cell5.setCellStyle(numberStyle);
        } else {
            R21Cell5.setCellValue("");
            R21Cell5.setCellStyle(textStyle);
        }
        Cell R21Cell6 = row.createCell(8);
        if (record.getR21_NET_AMT_BWP2() != null) {
            R21Cell6.setCellValue(record.getR21_NET_AMT_BWP2().doubleValue());
            R21Cell6.setCellStyle(numberStyle);
        } else {
            R21Cell6.setCellValue("");
            R21Cell6.setCellStyle(textStyle);
        }
        Cell R21Cell7 = row.createCell(9);
        if (record.getR21_BAL_SUB_BWP1() != null) {
            R21Cell7.setCellValue(record.getR21_BAL_SUB_BWP1().doubleValue());
            R21Cell7.setCellStyle(numberStyle);
        } else {
            R21Cell7.setCellValue("");
            R21Cell7.setCellStyle(textStyle);
        }
        Cell R21Cell8 = row.createCell(10);
        if (record.getR21_BAL_SUB_BWP2() != null) {
            R21Cell8.setCellValue(record.getR21_BAL_SUB_BWP2().doubleValue());
            R21Cell8.setCellStyle(numberStyle);
        } else {
            R21Cell8.setCellValue("");
            R21Cell8.setCellStyle(textStyle);
        }
        Cell R21Cell9 = row.createCell(11);
        if (record.getR21_BAL_ACT_SUB_BWP1() != null) {
            R21Cell9.setCellValue(record.getR21_BAL_ACT_SUB_BWP1().doubleValue());
            R21Cell9.setCellStyle(numberStyle);
        } else {
            R21Cell9.setCellValue("");
            R21Cell9.setCellStyle(textStyle);
        }
        Cell R21Cell10 = row.createCell(12);
        if (record.getR21_BAL_ACT_SUB_BWP2() != null) {
            R21Cell10.setCellValue(record.getR21_BAL_ACT_SUB_BWP2().doubleValue());
            R21Cell10.setCellStyle(numberStyle);
        } else {
            R21Cell10.setCellValue("");
            R21Cell10.setCellStyle(textStyle);
        }

        /* ================= R22 ================= */
        row = sheet.getRow(21);
        Cell R22Cell1 = row.createCell(3);
        if (record.getR22_FIG_BAL_BWP1() != null) {
            R22Cell1.setCellValue(record.getR22_FIG_BAL_BWP1().doubleValue());
            R22Cell1.setCellStyle(numberStyle);
        } else {
            R22Cell1.setCellValue("");
            R22Cell1.setCellStyle(textStyle);
        }
        Cell R22Cell2 = row.createCell(4);
        if (record.getR22_FIG_BAL_BWP2() != null) {
            R22Cell2.setCellValue(record.getR22_FIG_BAL_BWP2().doubleValue());
            R22Cell2.setCellStyle(numberStyle);
        } else {
            R22Cell2.setCellValue("");
            R22Cell2.setCellStyle(textStyle);
        }
        Cell R22Cell3 = row.createCell(5);
        if (record.getR22_AMT_ADJ_BWP1() != null) {
            R22Cell3.setCellValue(record.getR22_AMT_ADJ_BWP1().doubleValue());
            R22Cell3.setCellStyle(numberStyle);
        } else {
            R22Cell3.setCellValue("");
            R22Cell3.setCellStyle(textStyle);
        }
        Cell R22Cell4 = row.createCell(6);
        if (record.getR22_AMT_ADJ_BWP2() != null) {
            R22Cell4.setCellValue(record.getR22_AMT_ADJ_BWP2().doubleValue());
            R22Cell4.setCellStyle(numberStyle);
        } else {
            R22Cell4.setCellValue("");
            R22Cell4.setCellStyle(textStyle);
        }
        Cell R22Cell5 = row.createCell(7);
        if (record.getR22_NET_AMT_BWP1() != null) {
            R22Cell5.setCellValue(record.getR22_NET_AMT_BWP1().doubleValue());
            R22Cell5.setCellStyle(numberStyle);
        } else {
            R22Cell5.setCellValue("");
            R22Cell5.setCellStyle(textStyle);
        }
        Cell R22Cell6 = row.createCell(8);
        if (record.getR22_NET_AMT_BWP2() != null) {
            R22Cell6.setCellValue(record.getR22_NET_AMT_BWP2().doubleValue());
            R22Cell6.setCellStyle(numberStyle);
        } else {
            R22Cell6.setCellValue("");
            R22Cell6.setCellStyle(textStyle);
        }
        Cell R22Cell7 = row.createCell(9);
        if (record.getR22_BAL_SUB_BWP1() != null) {
            R22Cell7.setCellValue(record.getR22_BAL_SUB_BWP1().doubleValue());
            R22Cell7.setCellStyle(numberStyle);
        } else {
            R22Cell7.setCellValue("");
            R22Cell7.setCellStyle(textStyle);
        }
        Cell R22Cell8 = row.createCell(10);
        if (record.getR22_BAL_SUB_BWP2() != null) {
            R22Cell8.setCellValue(record.getR22_BAL_SUB_BWP2().doubleValue());
            R22Cell8.setCellStyle(numberStyle);
        } else {
            R22Cell8.setCellValue("");
            R22Cell8.setCellStyle(textStyle);
        }
        Cell R22Cell9 = row.createCell(11);
        if (record.getR22_BAL_ACT_SUB_BWP1() != null) {
            R22Cell9.setCellValue(record.getR22_BAL_ACT_SUB_BWP1().doubleValue());
            R22Cell9.setCellStyle(numberStyle);
        } else {
            R22Cell9.setCellValue("");
            R22Cell9.setCellStyle(textStyle);
        }
        Cell R22Cell10 = row.createCell(12);
        if (record.getR22_BAL_ACT_SUB_BWP2() != null) {
            R22Cell10.setCellValue(record.getR22_BAL_ACT_SUB_BWP2().doubleValue());
            R22Cell10.setCellStyle(numberStyle);
        } else {
            R22Cell10.setCellValue("");
            R22Cell10.setCellStyle(textStyle);
        }

        /* ================= R23 ================= */
        row = sheet.getRow(22);
        Cell R23Cell1 = row.createCell(3);
        if (record.getR23_FIG_BAL_BWP1() != null) {
            R23Cell1.setCellValue(record.getR23_FIG_BAL_BWP1().doubleValue());
            R23Cell1.setCellStyle(numberStyle);
        } else {
            R23Cell1.setCellValue("");
            R23Cell1.setCellStyle(textStyle);
        }
        Cell R23Cell2 = row.createCell(4);
        if (record.getR23_FIG_BAL_BWP2() != null) {
            R23Cell2.setCellValue(record.getR23_FIG_BAL_BWP2().doubleValue());
            R23Cell2.setCellStyle(numberStyle);
        } else {
            R23Cell2.setCellValue("");
            R23Cell2.setCellStyle(textStyle);
        }
        Cell R23Cell3 = row.createCell(5);
        if (record.getR23_AMT_ADJ_BWP1() != null) {
            R23Cell3.setCellValue(record.getR23_AMT_ADJ_BWP1().doubleValue());
            R23Cell3.setCellStyle(numberStyle);
        } else {
            R23Cell3.setCellValue("");
            R23Cell3.setCellStyle(textStyle);
        }
        Cell R23Cell4 = row.createCell(6);
        if (record.getR23_AMT_ADJ_BWP2() != null) {
            R23Cell4.setCellValue(record.getR23_AMT_ADJ_BWP2().doubleValue());
            R23Cell4.setCellStyle(numberStyle);
        } else {
            R23Cell4.setCellValue("");
            R23Cell4.setCellStyle(textStyle);
        }
        Cell R23Cell5 = row.createCell(7);
        if (record.getR23_NET_AMT_BWP1() != null) {
            R23Cell5.setCellValue(record.getR23_NET_AMT_BWP1().doubleValue());
            R23Cell5.setCellStyle(numberStyle);
        } else {
            R23Cell5.setCellValue("");
            R23Cell5.setCellStyle(textStyle);
        }
        Cell R23Cell6 = row.createCell(8);
        if (record.getR23_NET_AMT_BWP2() != null) {
            R23Cell6.setCellValue(record.getR23_NET_AMT_BWP2().doubleValue());
            R23Cell6.setCellStyle(numberStyle);
        } else {
            R23Cell6.setCellValue("");
            R23Cell6.setCellStyle(textStyle);
        }
        Cell R23Cell7 = row.createCell(9);
        if (record.getR23_BAL_SUB_BWP1() != null) {
            R23Cell7.setCellValue(record.getR23_BAL_SUB_BWP1().doubleValue());
            R23Cell7.setCellStyle(numberStyle);
        } else {
            R23Cell7.setCellValue("");
            R23Cell7.setCellStyle(textStyle);
        }
        Cell R23Cell8 = row.createCell(10);
        if (record.getR23_BAL_SUB_BWP2() != null) {
            R23Cell8.setCellValue(record.getR23_BAL_SUB_BWP2().doubleValue());
            R23Cell8.setCellStyle(numberStyle);
        } else {
            R23Cell8.setCellValue("");
            R23Cell8.setCellStyle(textStyle);
        }
        Cell R23Cell9 = row.createCell(11);
        if (record.getR23_BAL_ACT_SUB_BWP1() != null) {
            R23Cell9.setCellValue(record.getR23_BAL_ACT_SUB_BWP1().doubleValue());
            R23Cell9.setCellStyle(numberStyle);
        } else {
            R23Cell9.setCellValue("");
            R23Cell9.setCellStyle(textStyle);
        }
        Cell R23Cell10 = row.createCell(12);
        if (record.getR23_BAL_ACT_SUB_BWP2() != null) {
            R23Cell10.setCellValue(record.getR23_BAL_ACT_SUB_BWP2().doubleValue());
            R23Cell10.setCellStyle(numberStyle);
        } else {
            R23Cell10.setCellValue("");
            R23Cell10.setCellStyle(textStyle);
        }

        /* ================= R24 ================= */
        row = sheet.getRow(23);
        Cell R24Cell1 = row.createCell(3);
        if (record.getR24_FIG_BAL_BWP1() != null) {
            R24Cell1.setCellValue(record.getR24_FIG_BAL_BWP1().doubleValue());
            R24Cell1.setCellStyle(numberStyle);
        } else {
            R24Cell1.setCellValue("");
            R24Cell1.setCellStyle(textStyle);
        }
        Cell R24Cell2 = row.createCell(4);
        if (record.getR24_FIG_BAL_BWP2() != null) {
            R24Cell2.setCellValue(record.getR24_FIG_BAL_BWP2().doubleValue());
            R24Cell2.setCellStyle(numberStyle);
        } else {
            R24Cell2.setCellValue("");
            R24Cell2.setCellStyle(textStyle);
        }
        Cell R24Cell3 = row.createCell(5);
        if (record.getR24_AMT_ADJ_BWP1() != null) {
            R24Cell3.setCellValue(record.getR24_AMT_ADJ_BWP1().doubleValue());
            R24Cell3.setCellStyle(numberStyle);
        } else {
            R24Cell3.setCellValue("");
            R24Cell3.setCellStyle(textStyle);
        }
        Cell R24Cell4 = row.createCell(6);
        if (record.getR24_AMT_ADJ_BWP2() != null) {
            R24Cell4.setCellValue(record.getR24_AMT_ADJ_BWP2().doubleValue());
            R24Cell4.setCellStyle(numberStyle);
        } else {
            R24Cell4.setCellValue("");
            R24Cell4.setCellStyle(textStyle);
        }
        Cell R24Cell5 = row.createCell(7);
        if (record.getR24_NET_AMT_BWP1() != null) {
            R24Cell5.setCellValue(record.getR24_NET_AMT_BWP1().doubleValue());
            R24Cell5.setCellStyle(numberStyle);
        } else {
            R24Cell5.setCellValue("");
            R24Cell5.setCellStyle(textStyle);
        }
        Cell R24Cell6 = row.createCell(8);
        if (record.getR24_NET_AMT_BWP2() != null) {
            R24Cell6.setCellValue(record.getR24_NET_AMT_BWP2().doubleValue());
            R24Cell6.setCellStyle(numberStyle);
        } else {
            R24Cell6.setCellValue("");
            R24Cell6.setCellStyle(textStyle);
        }
        Cell R24Cell7 = row.createCell(9);
        if (record.getR24_BAL_SUB_BWP1() != null) {
            R24Cell7.setCellValue(record.getR24_BAL_SUB_BWP1().doubleValue());
            R24Cell7.setCellStyle(numberStyle);
        } else {
            R24Cell7.setCellValue("");
            R24Cell7.setCellStyle(textStyle);
        }
        Cell R24Cell8 = row.createCell(10);
        if (record.getR24_BAL_SUB_BWP2() != null) {
            R24Cell8.setCellValue(record.getR24_BAL_SUB_BWP2().doubleValue());
            R24Cell8.setCellStyle(numberStyle);
        } else {
            R24Cell8.setCellValue("");
            R24Cell8.setCellStyle(textStyle);
        }
        Cell R24Cell9 = row.createCell(11);
        if (record.getR24_BAL_ACT_SUB_BWP1() != null) {
            R24Cell9.setCellValue(record.getR24_BAL_ACT_SUB_BWP1().doubleValue());
            R24Cell9.setCellStyle(numberStyle);
        } else {
            R24Cell9.setCellValue("");
            R24Cell9.setCellStyle(textStyle);
        }
        Cell R24Cell10 = row.createCell(12);
        if (record.getR24_BAL_ACT_SUB_BWP2() != null) {
            R24Cell10.setCellValue(record.getR24_BAL_ACT_SUB_BWP2().doubleValue());
            R24Cell10.setCellStyle(numberStyle);
        } else {
            R24Cell10.setCellValue("");
            R24Cell10.setCellStyle(textStyle);
        }

        // R27 Col D
        row = sheet.getRow(26);
        Cell R27Cell1 = row.createCell(3);
        if (record.getR27_FIG_BAL_BWP1() != null) {
            R27Cell1.setCellValue(record.getR27_FIG_BAL_BWP1().doubleValue());
            R27Cell1.setCellStyle(numberStyle);
        } else {
            R27Cell1.setCellValue("");
            R27Cell1.setCellStyle(textStyle);
        }

        // R27 Col E
        Cell R27Cell2 = row.createCell(4);
        if (record.getR27_FIG_BAL_BWP2() != null) {
            R27Cell2.setCellValue(record.getR27_FIG_BAL_BWP2().doubleValue());
            R27Cell2.setCellStyle(numberStyle);
        } else {
            R27Cell2.setCellValue("");
            R27Cell2.setCellStyle(textStyle);
        }

        // R27 Col F
        Cell R27Cell3 = row.createCell(5);
        if (record.getR27_AMT_ADJ_BWP1() != null) {
            R27Cell3.setCellValue(record.getR27_AMT_ADJ_BWP1().doubleValue());
            R27Cell3.setCellStyle(numberStyle);
        } else {
            R27Cell3.setCellValue("");
            R27Cell3.setCellStyle(textStyle);
        }

        // R27 Col G
        Cell R27Cell4 = row.createCell(6);
        if (record.getR27_AMT_ADJ_BWP2() != null) {
            R27Cell4.setCellValue(record.getR27_AMT_ADJ_BWP2().doubleValue());
            R27Cell4.setCellStyle(numberStyle);
        } else {
            R27Cell4.setCellValue("");
            R27Cell4.setCellStyle(textStyle);
        }

        // R27 Col H
        Cell R27Cell5 = row.createCell(7);
        if (record.getR27_NET_AMT_BWP1() != null) {
            R27Cell5.setCellValue(record.getR27_NET_AMT_BWP1().doubleValue());
            R27Cell5.setCellStyle(numberStyle);
        } else {
            R27Cell5.setCellValue("");
            R27Cell5.setCellStyle(textStyle);
        }

        // R27 Col I
        Cell R27Cell6 = row.createCell(8);
        if (record.getR27_NET_AMT_BWP2() != null) {
            R27Cell6.setCellValue(record.getR27_NET_AMT_BWP2().doubleValue());
            R27Cell6.setCellStyle(numberStyle);
        } else {
            R27Cell6.setCellValue("");
            R27Cell6.setCellStyle(textStyle);
        }

        // R27 Col J
        Cell R27Cell7 = row.createCell(9);
        if (record.getR27_BAL_SUB_BWP1() != null) {
            R27Cell7.setCellValue(record.getR27_BAL_SUB_BWP1().doubleValue());
            R27Cell7.setCellStyle(numberStyle);
        } else {
            R27Cell7.setCellValue("");
            R27Cell7.setCellStyle(textStyle);
        }

        // R27 Col K
        Cell R27Cell8 = row.createCell(10);
        if (record.getR27_BAL_SUB_BWP2() != null) {
            R27Cell8.setCellValue(record.getR27_BAL_SUB_BWP2().doubleValue());
            R27Cell8.setCellStyle(numberStyle);
        } else {
            R27Cell8.setCellValue("");
            R27Cell8.setCellStyle(textStyle);
        }

        // R27 Col L
        Cell R27Cell9 = row.createCell(11);
        if (record.getR27_BAL_ACT_SUB_BWP1() != null) {
            R27Cell9.setCellValue(record.getR27_BAL_ACT_SUB_BWP1().doubleValue());
            R27Cell9.setCellStyle(numberStyle);
        } else {
            R27Cell9.setCellValue("");
            R27Cell9.setCellStyle(textStyle);
        }

        // R27 Col M
        Cell R27Cell10 = row.createCell(12);
        if (record.getR27_BAL_ACT_SUB_BWP2() != null) {
            R27Cell10.setCellValue(record.getR27_BAL_ACT_SUB_BWP2().doubleValue());
            R27Cell10.setCellStyle(numberStyle);
        } else {
            R27Cell10.setCellValue("");
            R27Cell10.setCellStyle(textStyle);
        }

        /* ================= R34 ================= */
        row = sheet.getRow(33);
        Cell R34Cell1 = row.createCell(3);
        if (record.getR34_FIG_BAL_BWP1() != null) {
            R34Cell1.setCellValue(record.getR34_FIG_BAL_BWP1().doubleValue());
            R34Cell1.setCellStyle(numberStyle);
        } else {
            R34Cell1.setCellValue("");
            R34Cell1.setCellStyle(textStyle);
        }
        Cell R34Cell2 = row.createCell(4);
        if (record.getR34_FIG_BAL_BWP2() != null) {
            R34Cell2.setCellValue(record.getR34_FIG_BAL_BWP2().doubleValue());
            R34Cell2.setCellStyle(numberStyle);
        } else {
            R34Cell2.setCellValue("");
            R34Cell2.setCellStyle(textStyle);
        }
        Cell R34Cell3 = row.createCell(5);
        if (record.getR34_AMT_ADJ_BWP1() != null) {
            R34Cell3.setCellValue(record.getR34_AMT_ADJ_BWP1().doubleValue());
            R34Cell3.setCellStyle(numberStyle);
        } else {
            R34Cell3.setCellValue("");
            R34Cell3.setCellStyle(textStyle);
        }
        Cell R34Cell4 = row.createCell(6);
        if (record.getR34_AMT_ADJ_BWP2() != null) {
            R34Cell4.setCellValue(record.getR34_AMT_ADJ_BWP2().doubleValue());
            R34Cell4.setCellStyle(numberStyle);
        } else {
            R34Cell4.setCellValue("");
            R34Cell4.setCellStyle(textStyle);
        }
        Cell R34Cell5 = row.createCell(7);
        if (record.getR34_NET_AMT_BWP1() != null) {
            R34Cell5.setCellValue(record.getR34_NET_AMT_BWP1().doubleValue());
            R34Cell5.setCellStyle(numberStyle);
        } else {
            R34Cell5.setCellValue("");
            R34Cell5.setCellStyle(textStyle);
        }
        Cell R34Cell6 = row.createCell(8);
        if (record.getR34_NET_AMT_BWP2() != null) {
            R34Cell6.setCellValue(record.getR34_NET_AMT_BWP2().doubleValue());
            R34Cell6.setCellStyle(numberStyle);
        } else {
            R34Cell6.setCellValue("");
            R34Cell6.setCellStyle(textStyle);
        }
        Cell R34Cell7 = row.createCell(9);
        if (record.getR34_BAL_SUB_BWP1() != null) {
            R34Cell7.setCellValue(record.getR34_BAL_SUB_BWP1().doubleValue());
            R34Cell7.setCellStyle(numberStyle);
        } else {
            R34Cell7.setCellValue("");
            R34Cell7.setCellStyle(textStyle);
        }
        Cell R34Cell8 = row.createCell(10);
        if (record.getR34_BAL_SUB_BWP2() != null) {
            R34Cell8.setCellValue(record.getR34_BAL_SUB_BWP2().doubleValue());
            R34Cell8.setCellStyle(numberStyle);
        } else {
            R34Cell8.setCellValue("");
            R34Cell8.setCellStyle(textStyle);
        }
        Cell R34Cell9 = row.createCell(11);
        if (record.getR34_BAL_ACT_SUB_BWP1() != null) {
            R34Cell9.setCellValue(record.getR34_BAL_ACT_SUB_BWP1().doubleValue());
            R34Cell9.setCellStyle(numberStyle);
        } else {
            R34Cell9.setCellValue("");
            R34Cell9.setCellStyle(textStyle);
        }
        Cell R34Cell10 = row.createCell(12);
        if (record.getR34_BAL_ACT_SUB_BWP2() != null) {
            R34Cell10.setCellValue(record.getR34_BAL_ACT_SUB_BWP2().doubleValue());
            R34Cell10.setCellStyle(numberStyle);
        } else {
            R34Cell10.setCellValue("");
            R34Cell10.setCellStyle(textStyle);
        }

        /* ================= R35 ================= */
        row = sheet.getRow(34);
        Cell R35Cell1 = row.createCell(3);
        if (record.getR35_FIG_BAL_BWP1() != null) {
            R35Cell1.setCellValue(record.getR35_FIG_BAL_BWP1().doubleValue());
            R35Cell1.setCellStyle(numberStyle);
        } else {
            R35Cell1.setCellValue("");
            R35Cell1.setCellStyle(textStyle);
        }
        Cell R35Cell2 = row.createCell(4);
        if (record.getR35_FIG_BAL_BWP2() != null) {
            R35Cell2.setCellValue(record.getR35_FIG_BAL_BWP2().doubleValue());
            R35Cell2.setCellStyle(numberStyle);
        } else {
            R35Cell2.setCellValue("");
            R35Cell2.setCellStyle(textStyle);
        }
        Cell R35Cell3 = row.createCell(5);
        if (record.getR35_AMT_ADJ_BWP1() != null) {
            R35Cell3.setCellValue(record.getR35_AMT_ADJ_BWP1().doubleValue());
            R35Cell3.setCellStyle(numberStyle);
        } else {
            R35Cell3.setCellValue("");
            R35Cell3.setCellStyle(textStyle);
        }
        Cell R35Cell4 = row.createCell(6);
        if (record.getR35_AMT_ADJ_BWP2() != null) {
            R35Cell4.setCellValue(record.getR35_AMT_ADJ_BWP2().doubleValue());
            R35Cell4.setCellStyle(numberStyle);
        } else {
            R35Cell4.setCellValue("");
            R35Cell4.setCellStyle(textStyle);
        }
        Cell R35Cell5 = row.createCell(7);
        if (record.getR35_NET_AMT_BWP1() != null) {
            R35Cell5.setCellValue(record.getR35_NET_AMT_BWP1().doubleValue());
            R35Cell5.setCellStyle(numberStyle);
        } else {
            R35Cell5.setCellValue("");
            R35Cell5.setCellStyle(textStyle);
        }
        Cell R35Cell6 = row.createCell(8);
        if (record.getR35_NET_AMT_BWP2() != null) {
            R35Cell6.setCellValue(record.getR35_NET_AMT_BWP2().doubleValue());
            R35Cell6.setCellStyle(numberStyle);
        } else {
            R35Cell6.setCellValue("");
            R35Cell6.setCellStyle(textStyle);
        }
        Cell R35Cell7 = row.createCell(9);
        if (record.getR35_BAL_SUB_BWP1() != null) {
            R35Cell7.setCellValue(record.getR35_BAL_SUB_BWP1().doubleValue());
            R35Cell7.setCellStyle(numberStyle);
        } else {
            R35Cell7.setCellValue("");
            R35Cell7.setCellStyle(textStyle);
        }
        Cell R35Cell8 = row.createCell(10);
        if (record.getR35_BAL_SUB_BWP2() != null) {
            R35Cell8.setCellValue(record.getR35_BAL_SUB_BWP2().doubleValue());
            R35Cell8.setCellStyle(numberStyle);
        } else {
            R35Cell8.setCellValue("");
            R35Cell8.setCellStyle(textStyle);
        }
        Cell R35Cell9 = row.createCell(11);
        if (record.getR35_BAL_ACT_SUB_BWP1() != null) {
            R35Cell9.setCellValue(record.getR35_BAL_ACT_SUB_BWP1().doubleValue());
            R35Cell9.setCellStyle(numberStyle);
        } else {
            R35Cell9.setCellValue("");
            R35Cell9.setCellStyle(textStyle);
        }
        Cell R35Cell10 = row.createCell(12);
        if (record.getR35_BAL_ACT_SUB_BWP2() != null) {
            R35Cell10.setCellValue(record.getR35_BAL_ACT_SUB_BWP2().doubleValue());
            R35Cell10.setCellStyle(numberStyle);
        } else {
            R35Cell10.setCellValue("");
            R35Cell10.setCellStyle(textStyle);
        }

        /* ================= R36 ================= */
        row = sheet.getRow(35);
        Cell R36Cell1 = row.createCell(3);
        if (record.getR36_FIG_BAL_BWP1() != null) {
            R36Cell1.setCellValue(record.getR36_FIG_BAL_BWP1().doubleValue());
            R36Cell1.setCellStyle(numberStyle);
        } else {
            R36Cell1.setCellValue("");
            R36Cell1.setCellStyle(textStyle);
        }
        Cell R36Cell2 = row.createCell(4);
        if (record.getR36_FIG_BAL_BWP2() != null) {
            R36Cell2.setCellValue(record.getR36_FIG_BAL_BWP2().doubleValue());
            R36Cell2.setCellStyle(numberStyle);
        } else {
            R36Cell2.setCellValue("");
            R36Cell2.setCellStyle(textStyle);
        }
        Cell R36Cell3 = row.createCell(5);
        if (record.getR36_AMT_ADJ_BWP1() != null) {
            R36Cell3.setCellValue(record.getR36_AMT_ADJ_BWP1().doubleValue());
            R36Cell3.setCellStyle(numberStyle);
        } else {
            R36Cell3.setCellValue("");
            R36Cell3.setCellStyle(textStyle);
        }
        Cell R36Cell4 = row.createCell(6);
        if (record.getR36_AMT_ADJ_BWP2() != null) {
            R36Cell4.setCellValue(record.getR36_AMT_ADJ_BWP2().doubleValue());
            R36Cell4.setCellStyle(numberStyle);
        } else {
            R36Cell4.setCellValue("");
            R36Cell4.setCellStyle(textStyle);
        }
        Cell R36Cell5 = row.createCell(7);
        if (record.getR36_NET_AMT_BWP1() != null) {
            R36Cell5.setCellValue(record.getR36_NET_AMT_BWP1().doubleValue());
            R36Cell5.setCellStyle(numberStyle);
        } else {
            R36Cell5.setCellValue("");
            R36Cell5.setCellStyle(textStyle);
        }
        Cell R36Cell6 = row.createCell(8);
        if (record.getR36_NET_AMT_BWP2() != null) {
            R36Cell6.setCellValue(record.getR36_NET_AMT_BWP2().doubleValue());
            R36Cell6.setCellStyle(numberStyle);
        } else {
            R36Cell6.setCellValue("");
            R36Cell6.setCellStyle(textStyle);
        }
        Cell R36Cell7 = row.createCell(9);
        if (record.getR36_BAL_SUB_BWP1() != null) {
            R36Cell7.setCellValue(record.getR36_BAL_SUB_BWP1().doubleValue());
            R36Cell7.setCellStyle(numberStyle);
        } else {
            R36Cell7.setCellValue("");
            R36Cell7.setCellStyle(textStyle);
        }
        Cell R36Cell8 = row.createCell(10);
        if (record.getR36_BAL_SUB_BWP2() != null) {
            R36Cell8.setCellValue(record.getR36_BAL_SUB_BWP2().doubleValue());
            R36Cell8.setCellStyle(numberStyle);
        } else {
            R36Cell8.setCellValue("");
            R36Cell8.setCellStyle(textStyle);
        }
        Cell R36Cell9 = row.createCell(11);
        if (record.getR36_BAL_ACT_SUB_BWP1() != null) {
            R36Cell9.setCellValue(record.getR36_BAL_ACT_SUB_BWP1().doubleValue());
            R36Cell9.setCellStyle(numberStyle);
        } else {
            R36Cell9.setCellValue("");
            R36Cell9.setCellStyle(textStyle);
        }
        Cell R36Cell10 = row.createCell(12);
        if (record.getR36_BAL_ACT_SUB_BWP2() != null) {
            R36Cell10.setCellValue(record.getR36_BAL_ACT_SUB_BWP2().doubleValue());
            R36Cell10.setCellStyle(numberStyle);
        } else {
            R36Cell10.setCellValue("");
            R36Cell10.setCellStyle(textStyle);
        }

        /* ================= R37 - R65 ================= */

        // R37
        row = sheet.getRow(36);
        Cell R37Cell1 = row.createCell(3);
        if (record.getR37_FIG_BAL_BWP1() != null) {
            R37Cell1.setCellValue(record.getR37_FIG_BAL_BWP1().doubleValue());
            R37Cell1.setCellStyle(numberStyle);
        } else {
            R37Cell1.setCellValue("");
            R37Cell1.setCellStyle(textStyle);
        }
        Cell R37Cell2 = row.createCell(4);
        if (record.getR37_FIG_BAL_BWP2() != null) {
            R37Cell2.setCellValue(record.getR37_FIG_BAL_BWP2().doubleValue());
            R37Cell2.setCellStyle(numberStyle);
        } else {
            R37Cell2.setCellValue("");
            R37Cell2.setCellStyle(textStyle);
        }
        Cell R37Cell3 = row.createCell(5);
        if (record.getR37_AMT_ADJ_BWP1() != null) {
            R37Cell3.setCellValue(record.getR37_AMT_ADJ_BWP1().doubleValue());
            R37Cell3.setCellStyle(numberStyle);
        } else {
            R37Cell3.setCellValue("");
            R37Cell3.setCellStyle(textStyle);
        }
        Cell R37Cell4 = row.createCell(6);
        if (record.getR37_AMT_ADJ_BWP2() != null) {
            R37Cell4.setCellValue(record.getR37_AMT_ADJ_BWP2().doubleValue());
            R37Cell4.setCellStyle(numberStyle);
        } else {
            R37Cell4.setCellValue("");
            R37Cell4.setCellStyle(textStyle);
        }
        Cell R37Cell5 = row.createCell(7);
        if (record.getR37_NET_AMT_BWP1() != null) {
            R37Cell5.setCellValue(record.getR37_NET_AMT_BWP1().doubleValue());
            R37Cell5.setCellStyle(numberStyle);
        } else {
            R37Cell5.setCellValue("");
            R37Cell5.setCellStyle(textStyle);
        }
        Cell R37Cell6 = row.createCell(8);
        if (record.getR37_NET_AMT_BWP2() != null) {
            R37Cell6.setCellValue(record.getR37_NET_AMT_BWP2().doubleValue());
            R37Cell6.setCellStyle(numberStyle);
        } else {
            R37Cell6.setCellValue("");
            R37Cell6.setCellStyle(textStyle);
        }
        Cell R37Cell7 = row.createCell(9);
        if (record.getR37_BAL_SUB_BWP1() != null) {
            R37Cell7.setCellValue(record.getR37_BAL_SUB_BWP1().doubleValue());
            R37Cell7.setCellStyle(numberStyle);
        } else {
            R37Cell7.setCellValue("");
            R37Cell7.setCellStyle(textStyle);
        }
        Cell R37Cell8 = row.createCell(10);
        if (record.getR37_BAL_SUB_BWP2() != null) {
            R37Cell8.setCellValue(record.getR37_BAL_SUB_BWP2().doubleValue());
            R37Cell8.setCellStyle(numberStyle);
        } else {
            R37Cell8.setCellValue("");
            R37Cell8.setCellStyle(textStyle);
        }
        Cell R37Cell9 = row.createCell(11);
        if (record.getR37_BAL_ACT_SUB_BWP1() != null) {
            R37Cell9.setCellValue(record.getR37_BAL_ACT_SUB_BWP1().doubleValue());
            R37Cell9.setCellStyle(numberStyle);
        } else {
            R37Cell9.setCellValue("");
            R37Cell9.setCellStyle(textStyle);
        }
        Cell R37Cell10 = row.createCell(12);
        if (record.getR37_BAL_ACT_SUB_BWP2() != null) {
            R37Cell10.setCellValue(record.getR37_BAL_ACT_SUB_BWP2().doubleValue());
            R37Cell10.setCellStyle(numberStyle);
        } else {
            R37Cell10.setCellValue("");
            R37Cell10.setCellStyle(textStyle);
        }

        // R38
        row = sheet.getRow(37);
        Cell R38Cell1 = row.createCell(3);
        if (record.getR38_FIG_BAL_BWP1() != null) {
            R38Cell1.setCellValue(record.getR38_FIG_BAL_BWP1().doubleValue());
            R38Cell1.setCellStyle(numberStyle);
        } else {
            R38Cell1.setCellValue("");
            R38Cell1.setCellStyle(textStyle);
        }
        Cell R38Cell2 = row.createCell(4);
        if (record.getR38_FIG_BAL_BWP2() != null) {
            R38Cell2.setCellValue(record.getR38_FIG_BAL_BWP2().doubleValue());
            R38Cell2.setCellStyle(numberStyle);
        } else {
            R38Cell2.setCellValue("");
            R38Cell2.setCellStyle(textStyle);
        }
        Cell R38Cell3 = row.createCell(5);
        if (record.getR38_AMT_ADJ_BWP1() != null) {
            R38Cell3.setCellValue(record.getR38_AMT_ADJ_BWP1().doubleValue());
            R38Cell3.setCellStyle(numberStyle);
        } else {
            R38Cell3.setCellValue("");
            R38Cell3.setCellStyle(textStyle);
        }
        Cell R38Cell4 = row.createCell(6);
        if (record.getR38_AMT_ADJ_BWP2() != null) {
            R38Cell4.setCellValue(record.getR38_AMT_ADJ_BWP2().doubleValue());
            R38Cell4.setCellStyle(numberStyle);
        } else {
            R38Cell4.setCellValue("");
            R38Cell4.setCellStyle(textStyle);
        }
        Cell R38Cell5 = row.createCell(7);
        if (record.getR38_NET_AMT_BWP1() != null) {
            R38Cell5.setCellValue(record.getR38_NET_AMT_BWP1().doubleValue());
            R38Cell5.setCellStyle(numberStyle);
        } else {
            R38Cell5.setCellValue("");
            R38Cell5.setCellStyle(textStyle);
        }
        Cell R38Cell6 = row.createCell(8);
        if (record.getR38_NET_AMT_BWP2() != null) {
            R38Cell6.setCellValue(record.getR38_NET_AMT_BWP2().doubleValue());
            R38Cell6.setCellStyle(numberStyle);
        } else {
            R38Cell6.setCellValue("");
            R38Cell6.setCellStyle(textStyle);
        }
        Cell R38Cell7 = row.createCell(9);
        if (record.getR38_BAL_SUB_BWP1() != null) {
            R38Cell7.setCellValue(record.getR38_BAL_SUB_BWP1().doubleValue());
            R38Cell7.setCellStyle(numberStyle);
        } else {
            R38Cell7.setCellValue("");
            R38Cell7.setCellStyle(textStyle);
        }
        Cell R38Cell8 = row.createCell(10);
        if (record.getR38_BAL_SUB_BWP2() != null) {
            R38Cell8.setCellValue(record.getR38_BAL_SUB_BWP2().doubleValue());
            R38Cell8.setCellStyle(numberStyle);
        } else {
            R38Cell8.setCellValue("");
            R38Cell8.setCellStyle(textStyle);
        }
        Cell R38Cell9 = row.createCell(11);
        if (record.getR38_BAL_ACT_SUB_BWP1() != null) {
            R38Cell9.setCellValue(record.getR38_BAL_ACT_SUB_BWP1().doubleValue());
            R38Cell9.setCellStyle(numberStyle);
        } else {
            R38Cell9.setCellValue("");
            R38Cell9.setCellStyle(textStyle);
        }
        Cell R38Cell10 = row.createCell(12);
        if (record.getR38_BAL_ACT_SUB_BWP2() != null) {
            R38Cell10.setCellValue(record.getR38_BAL_ACT_SUB_BWP2().doubleValue());
            R38Cell10.setCellStyle(numberStyle);
        } else {
            R38Cell10.setCellValue("");
            R38Cell10.setCellStyle(textStyle);
        }

        // R39
        row = sheet.getRow(38);
        Cell R39Cell1 = row.createCell(3);
        if (record.getR39_FIG_BAL_BWP1() != null) {
            R39Cell1.setCellValue(record.getR39_FIG_BAL_BWP1().doubleValue());
            R39Cell1.setCellStyle(numberStyle);
        } else {
            R39Cell1.setCellValue("");
            R39Cell1.setCellStyle(textStyle);
        }
        Cell R39Cell2 = row.createCell(4);
        if (record.getR39_FIG_BAL_BWP2() != null) {
            R39Cell2.setCellValue(record.getR39_FIG_BAL_BWP2().doubleValue());
            R39Cell2.setCellStyle(numberStyle);
        } else {
            R39Cell2.setCellValue("");
            R39Cell2.setCellStyle(textStyle);
        }
        Cell R39Cell3 = row.createCell(5);
        if (record.getR39_AMT_ADJ_BWP1() != null) {
            R39Cell3.setCellValue(record.getR39_AMT_ADJ_BWP1().doubleValue());
            R39Cell3.setCellStyle(numberStyle);
        } else {
            R39Cell3.setCellValue("");
            R39Cell3.setCellStyle(textStyle);
        }
        Cell R39Cell4 = row.createCell(6);
        if (record.getR39_AMT_ADJ_BWP2() != null) {
            R39Cell4.setCellValue(record.getR39_AMT_ADJ_BWP2().doubleValue());
            R39Cell4.setCellStyle(numberStyle);
        } else {
            R39Cell4.setCellValue("");
            R39Cell4.setCellStyle(textStyle);
        }
        Cell R39Cell5 = row.createCell(7);
        if (record.getR39_NET_AMT_BWP1() != null) {
            R39Cell5.setCellValue(record.getR39_NET_AMT_BWP1().doubleValue());
            R39Cell5.setCellStyle(numberStyle);
        } else {
            R39Cell5.setCellValue("");
            R39Cell5.setCellStyle(textStyle);
        }
        Cell R39Cell6 = row.createCell(8);
        if (record.getR39_NET_AMT_BWP2() != null) {
            R39Cell6.setCellValue(record.getR39_NET_AMT_BWP2().doubleValue());
            R39Cell6.setCellStyle(numberStyle);
        } else {
            R39Cell6.setCellValue("");
            R39Cell6.setCellStyle(textStyle);
        }
        Cell R39Cell7 = row.createCell(9);
        if (record.getR39_BAL_SUB_BWP1() != null) {
            R39Cell7.setCellValue(record.getR39_BAL_SUB_BWP1().doubleValue());
            R39Cell7.setCellStyle(numberStyle);
        } else {
            R39Cell7.setCellValue("");
            R39Cell7.setCellStyle(textStyle);
        }
        Cell R39Cell8 = row.createCell(10);
        if (record.getR39_BAL_SUB_BWP2() != null) {
            R39Cell8.setCellValue(record.getR39_BAL_SUB_BWP2().doubleValue());
            R39Cell8.setCellStyle(numberStyle);
        } else {
            R39Cell8.setCellValue("");
            R39Cell8.setCellStyle(textStyle);
        }
        Cell R39Cell9 = row.createCell(11);
        if (record.getR39_BAL_ACT_SUB_BWP1() != null) {
            R39Cell9.setCellValue(record.getR39_BAL_ACT_SUB_BWP1().doubleValue());
            R39Cell9.setCellStyle(numberStyle);
        } else {
            R39Cell9.setCellValue("");
            R39Cell9.setCellStyle(textStyle);
        }
        Cell R39Cell10 = row.createCell(12);
        if (record.getR39_BAL_ACT_SUB_BWP2() != null) {
            R39Cell10.setCellValue(record.getR39_BAL_ACT_SUB_BWP2().doubleValue());
            R39Cell10.setCellStyle(numberStyle);
        } else {
            R39Cell10.setCellValue("");
            R39Cell10.setCellStyle(textStyle);
        }

        // R40
        row = sheet.getRow(39);
        Cell R40Cell1 = row.createCell(3);
        if (record.getR40_FIG_BAL_BWP1() != null) {
            R40Cell1.setCellValue(record.getR40_FIG_BAL_BWP1().doubleValue());
            R40Cell1.setCellStyle(numberStyle);
        } else {
            R40Cell1.setCellValue("");
            R40Cell1.setCellStyle(textStyle);
        }
        Cell R40Cell2 = row.createCell(4);
        if (record.getR40_FIG_BAL_BWP2() != null) {
            R40Cell2.setCellValue(record.getR40_FIG_BAL_BWP2().doubleValue());
            R40Cell2.setCellStyle(numberStyle);
        } else {
            R40Cell2.setCellValue("");
            R40Cell2.setCellStyle(textStyle);
        }
        Cell R40Cell3 = row.createCell(5);
        if (record.getR40_AMT_ADJ_BWP1() != null) {
            R40Cell3.setCellValue(record.getR40_AMT_ADJ_BWP1().doubleValue());
            R40Cell3.setCellStyle(numberStyle);
        } else {
            R40Cell3.setCellValue("");
            R40Cell3.setCellStyle(textStyle);
        }
        Cell R40Cell4 = row.createCell(6);
        if (record.getR40_AMT_ADJ_BWP2() != null) {
            R40Cell4.setCellValue(record.getR40_AMT_ADJ_BWP2().doubleValue());
            R40Cell4.setCellStyle(numberStyle);
        } else {
            R40Cell4.setCellValue("");
            R40Cell4.setCellStyle(textStyle);
        }
        Cell R40Cell5 = row.createCell(7);
        if (record.getR40_NET_AMT_BWP1() != null) {
            R40Cell5.setCellValue(record.getR40_NET_AMT_BWP1().doubleValue());
            R40Cell5.setCellStyle(numberStyle);
        } else {
            R40Cell5.setCellValue("");
            R40Cell5.setCellStyle(textStyle);
        }
        Cell R40Cell6 = row.createCell(8);
        if (record.getR40_NET_AMT_BWP2() != null) {
            R40Cell6.setCellValue(record.getR40_NET_AMT_BWP2().doubleValue());
            R40Cell6.setCellStyle(numberStyle);
        } else {
            R40Cell6.setCellValue("");
            R40Cell6.setCellStyle(textStyle);
        }
        Cell R40Cell7 = row.createCell(9);
        if (record.getR40_BAL_SUB_BWP1() != null) {
            R40Cell7.setCellValue(record.getR40_BAL_SUB_BWP1().doubleValue());
            R40Cell7.setCellStyle(numberStyle);
        } else {
            R40Cell7.setCellValue("");
            R40Cell7.setCellStyle(textStyle);
        }
        Cell R40Cell8 = row.createCell(10);
        if (record.getR40_BAL_SUB_BWP2() != null) {
            R40Cell8.setCellValue(record.getR40_BAL_SUB_BWP2().doubleValue());
            R40Cell8.setCellStyle(numberStyle);
        } else {
            R40Cell8.setCellValue("");
            R40Cell8.setCellStyle(textStyle);
        }
        Cell R40Cell9 = row.createCell(11);
        if (record.getR40_BAL_ACT_SUB_BWP1() != null) {
            R40Cell9.setCellValue(record.getR40_BAL_ACT_SUB_BWP1().doubleValue());
            R40Cell9.setCellStyle(numberStyle);
        } else {
            R40Cell9.setCellValue("");
            R40Cell9.setCellStyle(textStyle);
        }
        Cell R40Cell10 = row.createCell(12);
        if (record.getR40_BAL_ACT_SUB_BWP2() != null) {
            R40Cell10.setCellValue(record.getR40_BAL_ACT_SUB_BWP2().doubleValue());
            R40Cell10.setCellStyle(numberStyle);
        } else {
            R40Cell10.setCellValue("");
            R40Cell10.setCellStyle(textStyle);
        }

        // R41
        row = sheet.getRow(40);
        Cell R41Cell1 = row.createCell(3);
        if (record.getR41_FIG_BAL_BWP1() != null) {
            R41Cell1.setCellValue(record.getR41_FIG_BAL_BWP1().doubleValue());
            R41Cell1.setCellStyle(numberStyle);
        } else {
            R41Cell1.setCellValue("");
            R41Cell1.setCellStyle(textStyle);
        }
        Cell R41Cell2 = row.createCell(4);
        if (record.getR41_FIG_BAL_BWP2() != null) {
            R41Cell2.setCellValue(record.getR41_FIG_BAL_BWP2().doubleValue());
            R41Cell2.setCellStyle(numberStyle);
        } else {
            R41Cell2.setCellValue("");
            R41Cell2.setCellStyle(textStyle);
        }
        Cell R41Cell3 = row.createCell(5);
        if (record.getR41_AMT_ADJ_BWP1() != null) {
            R41Cell3.setCellValue(record.getR41_AMT_ADJ_BWP1().doubleValue());
            R41Cell3.setCellStyle(numberStyle);
        } else {
            R41Cell3.setCellValue("");
            R41Cell3.setCellStyle(textStyle);
        }
        Cell R41Cell4 = row.createCell(6);
        if (record.getR41_AMT_ADJ_BWP2() != null) {
            R41Cell4.setCellValue(record.getR41_AMT_ADJ_BWP2().doubleValue());
            R41Cell4.setCellStyle(numberStyle);
        } else {
            R41Cell4.setCellValue("");
            R41Cell4.setCellStyle(textStyle);
        }
        Cell R41Cell5 = row.createCell(7);
        if (record.getR41_NET_AMT_BWP1() != null) {
            R41Cell5.setCellValue(record.getR41_NET_AMT_BWP1().doubleValue());
            R41Cell5.setCellStyle(numberStyle);
        } else {
            R41Cell5.setCellValue("");
            R41Cell5.setCellStyle(textStyle);
        }
        Cell R41Cell6 = row.createCell(8);
        if (record.getR41_NET_AMT_BWP2() != null) {
            R41Cell6.setCellValue(record.getR41_NET_AMT_BWP2().doubleValue());
            R41Cell6.setCellStyle(numberStyle);
        } else {
            R41Cell6.setCellValue("");
            R41Cell6.setCellStyle(textStyle);
        }
        Cell R41Cell7 = row.createCell(9);
        if (record.getR41_BAL_SUB_BWP1() != null) {
            R41Cell7.setCellValue(record.getR41_BAL_SUB_BWP1().doubleValue());
            R41Cell7.setCellStyle(numberStyle);
        } else {
            R41Cell7.setCellValue("");
            R41Cell7.setCellStyle(textStyle);
        }
        Cell R41Cell8 = row.createCell(10);
        if (record.getR41_BAL_SUB_BWP2() != null) {
            R41Cell8.setCellValue(record.getR41_BAL_SUB_BWP2().doubleValue());
            R41Cell8.setCellStyle(numberStyle);
        } else {
            R41Cell8.setCellValue("");
            R41Cell8.setCellStyle(textStyle);
        }
        Cell R41Cell9 = row.createCell(11);
        if (record.getR41_BAL_ACT_SUB_BWP1() != null) {
            R41Cell9.setCellValue(record.getR41_BAL_ACT_SUB_BWP1().doubleValue());
            R41Cell9.setCellStyle(numberStyle);
        } else {
            R41Cell9.setCellValue("");
            R41Cell9.setCellStyle(textStyle);
        }
        Cell R41Cell10 = row.createCell(12);
        if (record.getR41_BAL_ACT_SUB_BWP2() != null) {
            R41Cell10.setCellValue(record.getR41_BAL_ACT_SUB_BWP2().doubleValue());
            R41Cell10.setCellStyle(numberStyle);
        } else {
            R41Cell10.setCellValue("");
            R41Cell10.setCellStyle(textStyle);
        }

        // R42
        row = sheet.getRow(41);
        Cell R42Cell1 = row.createCell(3);
        if (record.getR42_FIG_BAL_BWP1() != null) {
            R42Cell1.setCellValue(record.getR42_FIG_BAL_BWP1().doubleValue());
            R42Cell1.setCellStyle(numberStyle);
        } else {
            R42Cell1.setCellValue("");
            R42Cell1.setCellStyle(textStyle);
        }
        Cell R42Cell2 = row.createCell(4);
        if (record.getR42_FIG_BAL_BWP2() != null) {
            R42Cell2.setCellValue(record.getR42_FIG_BAL_BWP2().doubleValue());
            R42Cell2.setCellStyle(numberStyle);
        } else {
            R42Cell2.setCellValue("");
            R42Cell2.setCellStyle(textStyle);
        }
        Cell R42Cell3 = row.createCell(5);
        if (record.getR42_AMT_ADJ_BWP1() != null) {
            R42Cell3.setCellValue(record.getR42_AMT_ADJ_BWP1().doubleValue());
            R42Cell3.setCellStyle(numberStyle);
        } else {
            R42Cell3.setCellValue("");
            R42Cell3.setCellStyle(textStyle);
        }
        Cell R42Cell4 = row.createCell(6);
        if (record.getR42_AMT_ADJ_BWP2() != null) {
            R42Cell4.setCellValue(record.getR42_AMT_ADJ_BWP2().doubleValue());
            R42Cell4.setCellStyle(numberStyle);
        } else {
            R42Cell4.setCellValue("");
            R42Cell4.setCellStyle(textStyle);
        }
        Cell R42Cell5 = row.createCell(7);
        if (record.getR42_NET_AMT_BWP1() != null) {
            R42Cell5.setCellValue(record.getR42_NET_AMT_BWP1().doubleValue());
            R42Cell5.setCellStyle(numberStyle);
        } else {
            R42Cell5.setCellValue("");
            R42Cell5.setCellStyle(textStyle);
        }
        Cell R42Cell6 = row.createCell(8);
        if (record.getR42_NET_AMT_BWP2() != null) {
            R42Cell6.setCellValue(record.getR42_NET_AMT_BWP2().doubleValue());
            R42Cell6.setCellStyle(numberStyle);
        } else {
            R42Cell6.setCellValue("");
            R42Cell6.setCellStyle(textStyle);
        }
        Cell R42Cell7 = row.createCell(9);
        if (record.getR42_BAL_SUB_BWP1() != null) {
            R42Cell7.setCellValue(record.getR42_BAL_SUB_BWP1().doubleValue());
            R42Cell7.setCellStyle(numberStyle);
        } else {
            R42Cell7.setCellValue("");
            R42Cell7.setCellStyle(textStyle);
        }
        Cell R42Cell8 = row.createCell(10);
        if (record.getR42_BAL_SUB_BWP2() != null) {
            R42Cell8.setCellValue(record.getR42_BAL_SUB_BWP2().doubleValue());
            R42Cell8.setCellStyle(numberStyle);
        } else {
            R42Cell8.setCellValue("");
            R42Cell8.setCellStyle(textStyle);
        }
        Cell R42Cell9 = row.createCell(11);
        if (record.getR42_BAL_ACT_SUB_BWP1() != null) {
            R42Cell9.setCellValue(record.getR42_BAL_ACT_SUB_BWP1().doubleValue());
            R42Cell9.setCellStyle(numberStyle);
        } else {
            R42Cell9.setCellValue("");
            R42Cell9.setCellStyle(textStyle);
        }
        Cell R42Cell10 = row.createCell(12);
        if (record.getR42_BAL_ACT_SUB_BWP2() != null) {
            R42Cell10.setCellValue(record.getR42_BAL_ACT_SUB_BWP2().doubleValue());
            R42Cell10.setCellStyle(numberStyle);
        } else {
            R42Cell10.setCellValue("");
            R42Cell10.setCellStyle(textStyle);
        }

        // R43
        row = sheet.getRow(42);
        Cell R43Cell1 = row.createCell(3);
        if (record.getR43_FIG_BAL_BWP1() != null) {
            R43Cell1.setCellValue(record.getR43_FIG_BAL_BWP1().doubleValue());
            R43Cell1.setCellStyle(numberStyle);
        } else {
            R43Cell1.setCellValue("");
            R43Cell1.setCellStyle(textStyle);
        }
        Cell R43Cell2 = row.createCell(4);
        if (record.getR43_FIG_BAL_BWP2() != null) {
            R43Cell2.setCellValue(record.getR43_FIG_BAL_BWP2().doubleValue());
            R43Cell2.setCellStyle(numberStyle);
        } else {
            R43Cell2.setCellValue("");
            R43Cell2.setCellStyle(textStyle);
        }
        Cell R43Cell3 = row.createCell(5);
        if (record.getR43_AMT_ADJ_BWP1() != null) {
            R43Cell3.setCellValue(record.getR43_AMT_ADJ_BWP1().doubleValue());
            R43Cell3.setCellStyle(numberStyle);
        } else {
            R43Cell3.setCellValue("");
            R43Cell3.setCellStyle(textStyle);
        }
        Cell R43Cell4 = row.createCell(6);
        if (record.getR43_AMT_ADJ_BWP2() != null) {
            R43Cell4.setCellValue(record.getR43_AMT_ADJ_BWP2().doubleValue());
            R43Cell4.setCellStyle(numberStyle);
        } else {
            R43Cell4.setCellValue("");
            R43Cell4.setCellStyle(textStyle);
        }
        Cell R43Cell5 = row.createCell(7);
        if (record.getR43_NET_AMT_BWP1() != null) {
            R43Cell5.setCellValue(record.getR43_NET_AMT_BWP1().doubleValue());
            R43Cell5.setCellStyle(numberStyle);
        } else {
            R43Cell5.setCellValue("");
            R43Cell5.setCellStyle(textStyle);
        }
        Cell R43Cell6 = row.createCell(8);
        if (record.getR43_NET_AMT_BWP2() != null) {
            R43Cell6.setCellValue(record.getR43_NET_AMT_BWP2().doubleValue());
            R43Cell6.setCellStyle(numberStyle);
        } else {
            R43Cell6.setCellValue("");
            R43Cell6.setCellStyle(textStyle);
        }
        Cell R43Cell7 = row.createCell(9);
        if (record.getR43_BAL_SUB_BWP1() != null) {
            R43Cell7.setCellValue(record.getR43_BAL_SUB_BWP1().doubleValue());
            R43Cell7.setCellStyle(numberStyle);
        } else {
            R43Cell7.setCellValue("");
            R43Cell7.setCellStyle(textStyle);
        }
        Cell R43Cell8 = row.createCell(10);
        if (record.getR43_BAL_SUB_BWP2() != null) {
            R43Cell8.setCellValue(record.getR43_BAL_SUB_BWP2().doubleValue());
            R43Cell8.setCellStyle(numberStyle);
        } else {
            R43Cell8.setCellValue("");
            R43Cell8.setCellStyle(textStyle);
        }
        Cell R43Cell9 = row.createCell(11);
        if (record.getR43_BAL_ACT_SUB_BWP1() != null) {
            R43Cell9.setCellValue(record.getR43_BAL_ACT_SUB_BWP1().doubleValue());
            R43Cell9.setCellStyle(numberStyle);
        } else {
            R43Cell9.setCellValue("");
            R43Cell9.setCellStyle(textStyle);
        }
        Cell R43Cell10 = row.createCell(12);
        if (record.getR43_BAL_ACT_SUB_BWP2() != null) {
            R43Cell10.setCellValue(record.getR43_BAL_ACT_SUB_BWP2().doubleValue());
            R43Cell10.setCellStyle(numberStyle);
        } else {
            R43Cell10.setCellValue("");
            R43Cell10.setCellStyle(textStyle);
        }

        // R44
        row = sheet.getRow(43);
        Cell R44Cell1 = row.createCell(3);
        if (record.getR44_FIG_BAL_BWP1() != null) {
            R44Cell1.setCellValue(record.getR44_FIG_BAL_BWP1().doubleValue());
            R44Cell1.setCellStyle(numberStyle);
        } else {
            R44Cell1.setCellValue("");
            R44Cell1.setCellStyle(textStyle);
        }
        Cell R44Cell2 = row.createCell(4);
        if (record.getR44_FIG_BAL_BWP2() != null) {
            R44Cell2.setCellValue(record.getR44_FIG_BAL_BWP2().doubleValue());
            R44Cell2.setCellStyle(numberStyle);
        } else {
            R44Cell2.setCellValue("");
            R44Cell2.setCellStyle(textStyle);
        }
        Cell R44Cell3 = row.createCell(5);
        if (record.getR44_AMT_ADJ_BWP1() != null) {
            R44Cell3.setCellValue(record.getR44_AMT_ADJ_BWP1().doubleValue());
            R44Cell3.setCellStyle(numberStyle);
        } else {
            R44Cell3.setCellValue("");
            R44Cell3.setCellStyle(textStyle);
        }
        Cell R44Cell4 = row.createCell(6);
        if (record.getR44_AMT_ADJ_BWP2() != null) {
            R44Cell4.setCellValue(record.getR44_AMT_ADJ_BWP2().doubleValue());
            R44Cell4.setCellStyle(numberStyle);
        } else {
            R44Cell4.setCellValue("");
            R44Cell4.setCellStyle(textStyle);
        }
        Cell R44Cell5 = row.createCell(7);
        if (record.getR44_NET_AMT_BWP1() != null) {
            R44Cell5.setCellValue(record.getR44_NET_AMT_BWP1().doubleValue());
            R44Cell5.setCellStyle(numberStyle);
        } else {
            R44Cell5.setCellValue("");
            R44Cell5.setCellStyle(textStyle);
        }
        Cell R44Cell6 = row.createCell(8);
        if (record.getR44_NET_AMT_BWP2() != null) {
            R44Cell6.setCellValue(record.getR44_NET_AMT_BWP2().doubleValue());
            R44Cell6.setCellStyle(numberStyle);
        } else {
            R44Cell6.setCellValue("");
            R44Cell6.setCellStyle(textStyle);
        }
        Cell R44Cell7 = row.createCell(9);
        if (record.getR44_BAL_SUB_BWP1() != null) {
            R44Cell7.setCellValue(record.getR44_BAL_SUB_BWP1().doubleValue());
            R44Cell7.setCellStyle(numberStyle);
        } else {
            R44Cell7.setCellValue("");
            R44Cell7.setCellStyle(textStyle);
        }
        Cell R44Cell8 = row.createCell(10);
        if (record.getR44_BAL_SUB_BWP2() != null) {
            R44Cell8.setCellValue(record.getR44_BAL_SUB_BWP2().doubleValue());
            R44Cell8.setCellStyle(numberStyle);
        } else {
            R44Cell8.setCellValue("");
            R44Cell8.setCellStyle(textStyle);
        }
        Cell R44Cell9 = row.createCell(11);
        if (record.getR44_BAL_ACT_SUB_BWP1() != null) {
            R44Cell9.setCellValue(record.getR44_BAL_ACT_SUB_BWP1().doubleValue());
            R44Cell9.setCellStyle(numberStyle);
        } else {
            R44Cell9.setCellValue("");
            R44Cell9.setCellStyle(textStyle);
        }
        Cell R44Cell10 = row.createCell(12);
        if (record.getR44_BAL_ACT_SUB_BWP2() != null) {
            R44Cell10.setCellValue(record.getR44_BAL_ACT_SUB_BWP2().doubleValue());
            R44Cell10.setCellStyle(numberStyle);
        } else {
            R44Cell10.setCellValue("");
            R44Cell10.setCellStyle(textStyle);
        }

        // R45
        row = sheet.getRow(44);
        Cell R45Cell1 = row.createCell(3);
        if (record.getR45_FIG_BAL_BWP1() != null) {
            R45Cell1.setCellValue(record.getR45_FIG_BAL_BWP1().doubleValue());
            R45Cell1.setCellStyle(numberStyle);
        } else {
            R45Cell1.setCellValue("");
            R45Cell1.setCellStyle(textStyle);
        }
        Cell R45Cell2 = row.createCell(4);
        if (record.getR45_FIG_BAL_BWP2() != null) {
            R45Cell2.setCellValue(record.getR45_FIG_BAL_BWP2().doubleValue());
            R45Cell2.setCellStyle(numberStyle);
        } else {
            R45Cell2.setCellValue("");
            R45Cell2.setCellStyle(textStyle);
        }
        Cell R45Cell3 = row.createCell(5);
        if (record.getR45_AMT_ADJ_BWP1() != null) {
            R45Cell3.setCellValue(record.getR45_AMT_ADJ_BWP1().doubleValue());
            R45Cell3.setCellStyle(numberStyle);
        } else {
            R45Cell3.setCellValue("");
            R45Cell3.setCellStyle(textStyle);
        }
        Cell R45Cell4 = row.createCell(6);
        if (record.getR45_AMT_ADJ_BWP2() != null) {
            R45Cell4.setCellValue(record.getR45_AMT_ADJ_BWP2().doubleValue());
            R45Cell4.setCellStyle(numberStyle);
        } else {
            R45Cell4.setCellValue("");
            R45Cell4.setCellStyle(textStyle);
        }
        Cell R45Cell5 = row.createCell(7);
        if (record.getR45_NET_AMT_BWP1() != null) {
            R45Cell5.setCellValue(record.getR45_NET_AMT_BWP1().doubleValue());
            R45Cell5.setCellStyle(numberStyle);
        } else {
            R45Cell5.setCellValue("");
            R45Cell5.setCellStyle(textStyle);
        }
        Cell R45Cell6 = row.createCell(8);
        if (record.getR45_NET_AMT_BWP2() != null) {
            R45Cell6.setCellValue(record.getR45_NET_AMT_BWP2().doubleValue());
            R45Cell6.setCellStyle(numberStyle);
        } else {
            R45Cell6.setCellValue("");
            R45Cell6.setCellStyle(textStyle);
        }
        Cell R45Cell7 = row.createCell(9);
        if (record.getR45_BAL_SUB_BWP1() != null) {
            R45Cell7.setCellValue(record.getR45_BAL_SUB_BWP1().doubleValue());
            R45Cell7.setCellStyle(numberStyle);
        } else {
            R45Cell7.setCellValue("");
            R45Cell7.setCellStyle(textStyle);
        }
        Cell R45Cell8 = row.createCell(10);
        if (record.getR45_BAL_SUB_BWP2() != null) {
            R45Cell8.setCellValue(record.getR45_BAL_SUB_BWP2().doubleValue());
            R45Cell8.setCellStyle(numberStyle);
        } else {
            R45Cell8.setCellValue("");
            R45Cell8.setCellStyle(textStyle);
        }
        Cell R45Cell9 = row.createCell(11);
        if (record.getR45_BAL_ACT_SUB_BWP1() != null) {
            R45Cell9.setCellValue(record.getR45_BAL_ACT_SUB_BWP1().doubleValue());
            R45Cell9.setCellStyle(numberStyle);
        } else {
            R45Cell9.setCellValue("");
            R45Cell9.setCellStyle(textStyle);
        }
        Cell R45Cell10 = row.createCell(12);
        if (record.getR45_BAL_ACT_SUB_BWP2() != null) {
            R45Cell10.setCellValue(record.getR45_BAL_ACT_SUB_BWP2().doubleValue());
            R45Cell10.setCellStyle(numberStyle);
        } else {
            R45Cell10.setCellValue("");
            R45Cell10.setCellStyle(textStyle);
        }

        // R46
        row = sheet.getRow(45);
        Cell R46Cell1 = row.createCell(3);
        if (record.getR46_FIG_BAL_BWP1() != null) {
            R46Cell1.setCellValue(record.getR46_FIG_BAL_BWP1().doubleValue());
            R46Cell1.setCellStyle(numberStyle);
        } else {
            R46Cell1.setCellValue("");
            R46Cell1.setCellStyle(textStyle);
        }
        Cell R46Cell2 = row.createCell(4);
        if (record.getR46_FIG_BAL_BWP2() != null) {
            R46Cell2.setCellValue(record.getR46_FIG_BAL_BWP2().doubleValue());
            R46Cell2.setCellStyle(numberStyle);
        } else {
            R46Cell2.setCellValue("");
            R46Cell2.setCellStyle(textStyle);
        }
        Cell R46Cell3 = row.createCell(5);
        if (record.getR46_AMT_ADJ_BWP1() != null) {
            R46Cell3.setCellValue(record.getR46_AMT_ADJ_BWP1().doubleValue());
            R46Cell3.setCellStyle(numberStyle);
        } else {
            R46Cell3.setCellValue("");
            R46Cell3.setCellStyle(textStyle);
        }
        Cell R46Cell4 = row.createCell(6);
        if (record.getR46_AMT_ADJ_BWP2() != null) {
            R46Cell4.setCellValue(record.getR46_AMT_ADJ_BWP2().doubleValue());
            R46Cell4.setCellStyle(numberStyle);
        } else {
            R46Cell4.setCellValue("");
            R46Cell4.setCellStyle(textStyle);
        }
        Cell R46Cell5 = row.createCell(7);
        if (record.getR46_NET_AMT_BWP1() != null) {
            R46Cell5.setCellValue(record.getR46_NET_AMT_BWP1().doubleValue());
            R46Cell5.setCellStyle(numberStyle);
        } else {
            R46Cell5.setCellValue("");
            R46Cell5.setCellStyle(textStyle);
        }
        Cell R46Cell6 = row.createCell(8);
        if (record.getR46_NET_AMT_BWP2() != null) {
            R46Cell6.setCellValue(record.getR46_NET_AMT_BWP2().doubleValue());
            R46Cell6.setCellStyle(numberStyle);
        } else {
            R46Cell6.setCellValue("");
            R46Cell6.setCellStyle(textStyle);
        }
        Cell R46Cell7 = row.createCell(9);
        if (record.getR46_BAL_SUB_BWP1() != null) {
            R46Cell7.setCellValue(record.getR46_BAL_SUB_BWP1().doubleValue());
            R46Cell7.setCellStyle(numberStyle);
        } else {
            R46Cell7.setCellValue("");
            R46Cell7.setCellStyle(textStyle);
        }
        Cell R46Cell8 = row.createCell(10);
        if (record.getR46_BAL_SUB_BWP2() != null) {
            R46Cell8.setCellValue(record.getR46_BAL_SUB_BWP2().doubleValue());
            R46Cell8.setCellStyle(numberStyle);
        } else {
            R46Cell8.setCellValue("");
            R46Cell8.setCellStyle(textStyle);
        }
        Cell R46Cell9 = row.createCell(11);
        if (record.getR46_BAL_ACT_SUB_BWP1() != null) {
            R46Cell9.setCellValue(record.getR46_BAL_ACT_SUB_BWP1().doubleValue());
            R46Cell9.setCellStyle(numberStyle);
        } else {
            R46Cell9.setCellValue("");
            R46Cell9.setCellStyle(textStyle);
        }
        Cell R46Cell10 = row.createCell(12);
        if (record.getR46_BAL_ACT_SUB_BWP2() != null) {
            R46Cell10.setCellValue(record.getR46_BAL_ACT_SUB_BWP2().doubleValue());
            R46Cell10.setCellStyle(numberStyle);
        } else {
            R46Cell10.setCellValue("");
            R46Cell10.setCellStyle(textStyle);
        }

        // R47
        row = sheet.getRow(46);
        Cell R47Cell1 = row.createCell(3);
        if (record.getR47_FIG_BAL_BWP1() != null) {
            R47Cell1.setCellValue(record.getR47_FIG_BAL_BWP1().doubleValue());
            R47Cell1.setCellStyle(numberStyle);
        } else {
            R47Cell1.setCellValue("");
            R47Cell1.setCellStyle(textStyle);
        }
        Cell R47Cell2 = row.createCell(4);
        if (record.getR47_FIG_BAL_BWP2() != null) {
            R47Cell2.setCellValue(record.getR47_FIG_BAL_BWP2().doubleValue());
            R47Cell2.setCellStyle(numberStyle);
        } else {
            R47Cell2.setCellValue("");
            R47Cell2.setCellStyle(textStyle);
        }
        Cell R47Cell3 = row.createCell(5);
        if (record.getR47_AMT_ADJ_BWP1() != null) {
            R47Cell3.setCellValue(record.getR47_AMT_ADJ_BWP1().doubleValue());
            R47Cell3.setCellStyle(numberStyle);
        } else {
            R47Cell3.setCellValue("");
            R47Cell3.setCellStyle(textStyle);
        }
        Cell R47Cell4 = row.createCell(6);
        if (record.getR47_AMT_ADJ_BWP2() != null) {
            R47Cell4.setCellValue(record.getR47_AMT_ADJ_BWP2().doubleValue());
            R47Cell4.setCellStyle(numberStyle);
        } else {
            R47Cell4.setCellValue("");
            R47Cell4.setCellStyle(textStyle);
        }
        Cell R47Cell5 = row.createCell(7);
        if (record.getR47_NET_AMT_BWP1() != null) {
            R47Cell5.setCellValue(record.getR47_NET_AMT_BWP1().doubleValue());
            R47Cell5.setCellStyle(numberStyle);
        } else {
            R47Cell5.setCellValue("");
            R47Cell5.setCellStyle(textStyle);
        }
        Cell R47Cell6 = row.createCell(8);
        if (record.getR47_NET_AMT_BWP2() != null) {
            R47Cell6.setCellValue(record.getR47_NET_AMT_BWP2().doubleValue());
            R47Cell6.setCellStyle(numberStyle);
        } else {
            R47Cell6.setCellValue("");
            R47Cell6.setCellStyle(textStyle);
        }
        Cell R47Cell7 = row.createCell(9);
        if (record.getR47_BAL_SUB_BWP1() != null) {
            R47Cell7.setCellValue(record.getR47_BAL_SUB_BWP1().doubleValue());
            R47Cell7.setCellStyle(numberStyle);
        } else {
            R47Cell7.setCellValue("");
            R47Cell7.setCellStyle(textStyle);
        }
        Cell R47Cell8 = row.createCell(10);
        if (record.getR47_BAL_SUB_BWP2() != null) {
            R47Cell8.setCellValue(record.getR47_BAL_SUB_BWP2().doubleValue());
            R47Cell8.setCellStyle(numberStyle);
        } else {
            R47Cell8.setCellValue("");
            R47Cell8.setCellStyle(textStyle);
        }
        Cell R47Cell9 = row.createCell(11);
        if (record.getR47_BAL_ACT_SUB_BWP1() != null) {
            R47Cell9.setCellValue(record.getR47_BAL_ACT_SUB_BWP1().doubleValue());
            R47Cell9.setCellStyle(numberStyle);
        } else {
            R47Cell9.setCellValue("");
            R47Cell9.setCellStyle(textStyle);
        }
        Cell R47Cell10 = row.createCell(12);
        if (record.getR47_BAL_ACT_SUB_BWP2() != null) {
            R47Cell10.setCellValue(record.getR47_BAL_ACT_SUB_BWP2().doubleValue());
            R47Cell10.setCellStyle(numberStyle);
        } else {
            R47Cell10.setCellValue("");
            R47Cell10.setCellStyle(textStyle);
        }

        // R48
        row = sheet.getRow(47);
        Cell R48Cell1 = row.createCell(3);
        if (record.getR48_FIG_BAL_BWP1() != null) {
            R48Cell1.setCellValue(record.getR48_FIG_BAL_BWP1().doubleValue());
            R48Cell1.setCellStyle(numberStyle);
        } else {
            R48Cell1.setCellValue("");
            R48Cell1.setCellStyle(textStyle);
        }
        Cell R48Cell2 = row.createCell(4);
        if (record.getR48_FIG_BAL_BWP2() != null) {
            R48Cell2.setCellValue(record.getR48_FIG_BAL_BWP2().doubleValue());
            R48Cell2.setCellStyle(numberStyle);
        } else {
            R48Cell2.setCellValue("");
            R48Cell2.setCellStyle(textStyle);
        }
        Cell R48Cell3 = row.createCell(5);
        if (record.getR48_AMT_ADJ_BWP1() != null) {
            R48Cell3.setCellValue(record.getR48_AMT_ADJ_BWP1().doubleValue());
            R48Cell3.setCellStyle(numberStyle);
        } else {
            R48Cell3.setCellValue("");
            R48Cell3.setCellStyle(textStyle);
        }
        Cell R48Cell4 = row.createCell(6);
        if (record.getR48_AMT_ADJ_BWP2() != null) {
            R48Cell4.setCellValue(record.getR48_AMT_ADJ_BWP2().doubleValue());
            R48Cell4.setCellStyle(numberStyle);
        } else {
            R48Cell4.setCellValue("");
            R48Cell4.setCellStyle(textStyle);
        }
        Cell R48Cell5 = row.createCell(7);
        if (record.getR48_NET_AMT_BWP1() != null) {
            R48Cell5.setCellValue(record.getR48_NET_AMT_BWP1().doubleValue());
            R48Cell5.setCellStyle(numberStyle);
        } else {
            R48Cell5.setCellValue("");
            R48Cell5.setCellStyle(textStyle);
        }
        Cell R48Cell6 = row.createCell(8);
        if (record.getR48_NET_AMT_BWP2() != null) {
            R48Cell6.setCellValue(record.getR48_NET_AMT_BWP2().doubleValue());
            R48Cell6.setCellStyle(numberStyle);
        } else {
            R48Cell6.setCellValue("");
            R48Cell6.setCellStyle(textStyle);
        }
        Cell R48Cell7 = row.createCell(9);
        if (record.getR48_BAL_SUB_BWP1() != null) {
            R48Cell7.setCellValue(record.getR48_BAL_SUB_BWP1().doubleValue());
            R48Cell7.setCellStyle(numberStyle);
        } else {
            R48Cell7.setCellValue("");
            R48Cell7.setCellStyle(textStyle);
        }
        Cell R48Cell8 = row.createCell(10);
        if (record.getR48_BAL_SUB_BWP2() != null) {
            R48Cell8.setCellValue(record.getR48_BAL_SUB_BWP2().doubleValue());
            R48Cell8.setCellStyle(numberStyle);
        } else {
            R48Cell8.setCellValue("");
            R48Cell8.setCellStyle(textStyle);
        }
        Cell R48Cell9 = row.createCell(11);
        if (record.getR48_BAL_ACT_SUB_BWP1() != null) {
            R48Cell9.setCellValue(record.getR48_BAL_ACT_SUB_BWP1().doubleValue());
            R48Cell9.setCellStyle(numberStyle);
        } else {
            R48Cell9.setCellValue("");
            R48Cell9.setCellStyle(textStyle);
        }
        Cell R48Cell10 = row.createCell(12);
        if (record.getR48_BAL_ACT_SUB_BWP2() != null) {
            R48Cell10.setCellValue(record.getR48_BAL_ACT_SUB_BWP2().doubleValue());
            R48Cell10.setCellStyle(numberStyle);
        } else {
            R48Cell10.setCellValue("");
            R48Cell10.setCellStyle(textStyle);
        }

        // R49
        row = sheet.getRow(48);
        Cell R49Cell1 = row.createCell(3);
        if (record.getR49_FIG_BAL_BWP1() != null) {
            R49Cell1.setCellValue(record.getR49_FIG_BAL_BWP1().doubleValue());
            R49Cell1.setCellStyle(numberStyle);
        } else {
            R49Cell1.setCellValue("");
            R49Cell1.setCellStyle(textStyle);
        }
        Cell R49Cell2 = row.createCell(4);
        if (record.getR49_FIG_BAL_BWP2() != null) {
            R49Cell2.setCellValue(record.getR49_FIG_BAL_BWP2().doubleValue());
            R49Cell2.setCellStyle(numberStyle);
        } else {
            R49Cell2.setCellValue("");
            R49Cell2.setCellStyle(textStyle);
        }
        Cell R49Cell3 = row.createCell(5);
        if (record.getR49_AMT_ADJ_BWP1() != null) {
            R49Cell3.setCellValue(record.getR49_AMT_ADJ_BWP1().doubleValue());
            R49Cell3.setCellStyle(numberStyle);
        } else {
            R49Cell3.setCellValue("");
            R49Cell3.setCellStyle(textStyle);
        }
        Cell R49Cell4 = row.createCell(6);
        if (record.getR49_AMT_ADJ_BWP2() != null) {
            R49Cell4.setCellValue(record.getR49_AMT_ADJ_BWP2().doubleValue());
            R49Cell4.setCellStyle(numberStyle);
        } else {
            R49Cell4.setCellValue("");
            R49Cell4.setCellStyle(textStyle);
        }
        Cell R49Cell5 = row.createCell(7);
        if (record.getR49_NET_AMT_BWP1() != null) {
            R49Cell5.setCellValue(record.getR49_NET_AMT_BWP1().doubleValue());
            R49Cell5.setCellStyle(numberStyle);
        } else {
            R49Cell5.setCellValue("");
            R49Cell5.setCellStyle(textStyle);
        }
        Cell R49Cell6 = row.createCell(8);
        if (record.getR49_NET_AMT_BWP2() != null) {
            R49Cell6.setCellValue(record.getR49_NET_AMT_BWP2().doubleValue());
            R49Cell6.setCellStyle(numberStyle);
        } else {
            R49Cell6.setCellValue("");
            R49Cell6.setCellStyle(textStyle);
        }
        Cell R49Cell7 = row.createCell(9);
        if (record.getR49_BAL_SUB_BWP1() != null) {
            R49Cell7.setCellValue(record.getR49_BAL_SUB_BWP1().doubleValue());
            R49Cell7.setCellStyle(numberStyle);
        } else {
            R49Cell7.setCellValue("");
            R49Cell7.setCellStyle(textStyle);
        }
        Cell R49Cell8 = row.createCell(10);
        if (record.getR49_BAL_SUB_BWP2() != null) {
            R49Cell8.setCellValue(record.getR49_BAL_SUB_BWP2().doubleValue());
            R49Cell8.setCellStyle(numberStyle);
        } else {
            R49Cell8.setCellValue("");
            R49Cell8.setCellStyle(textStyle);
        }
        Cell R49Cell9 = row.createCell(11);
        if (record.getR49_BAL_ACT_SUB_BWP1() != null) {
            R49Cell9.setCellValue(record.getR49_BAL_ACT_SUB_BWP1().doubleValue());
            R49Cell9.setCellStyle(numberStyle);
        } else {
            R49Cell9.setCellValue("");
            R49Cell9.setCellStyle(textStyle);
        }
        Cell R49Cell10 = row.createCell(12);
        if (record.getR49_BAL_ACT_SUB_BWP2() != null) {
            R49Cell10.setCellValue(record.getR49_BAL_ACT_SUB_BWP2().doubleValue());
            R49Cell10.setCellStyle(numberStyle);
        } else {
            R49Cell10.setCellValue("");
            R49Cell10.setCellStyle(textStyle);
        }

        // R50
        row = sheet.getRow(49);
        Cell R50Cell1 = row.createCell(3);
        if (record.getR50_FIG_BAL_BWP1() != null) {
            R50Cell1.setCellValue(record.getR50_FIG_BAL_BWP1().doubleValue());
            R50Cell1.setCellStyle(numberStyle);
        } else {
            R50Cell1.setCellValue("");
            R50Cell1.setCellStyle(textStyle);
        }
        Cell R50Cell2 = row.createCell(4);
        if (record.getR50_FIG_BAL_BWP2() != null) {
            R50Cell2.setCellValue(record.getR50_FIG_BAL_BWP2().doubleValue());
            R50Cell2.setCellStyle(numberStyle);
        } else {
            R50Cell2.setCellValue("");
            R50Cell2.setCellStyle(textStyle);
        }
        Cell R50Cell3 = row.createCell(5);
        if (record.getR50_AMT_ADJ_BWP1() != null) {
            R50Cell3.setCellValue(record.getR50_AMT_ADJ_BWP1().doubleValue());
            R50Cell3.setCellStyle(numberStyle);
        } else {
            R50Cell3.setCellValue("");
            R50Cell3.setCellStyle(textStyle);
        }
        Cell R50Cell4 = row.createCell(6);
        if (record.getR50_AMT_ADJ_BWP2() != null) {
            R50Cell4.setCellValue(record.getR50_AMT_ADJ_BWP2().doubleValue());
            R50Cell4.setCellStyle(numberStyle);
        } else {
            R50Cell4.setCellValue("");
            R50Cell4.setCellStyle(textStyle);
        }
        Cell R50Cell5 = row.createCell(7);
        if (record.getR50_NET_AMT_BWP1() != null) {
            R50Cell5.setCellValue(record.getR50_NET_AMT_BWP1().doubleValue());
            R50Cell5.setCellStyle(numberStyle);
        } else {
            R50Cell5.setCellValue("");
            R50Cell5.setCellStyle(textStyle);
        }
        Cell R50Cell6 = row.createCell(8);
        if (record.getR50_NET_AMT_BWP2() != null) {
            R50Cell6.setCellValue(record.getR50_NET_AMT_BWP2().doubleValue());
            R50Cell6.setCellStyle(numberStyle);
        } else {
            R50Cell6.setCellValue("");
            R50Cell6.setCellStyle(textStyle);
        }
        Cell R50Cell7 = row.createCell(9);
        if (record.getR50_BAL_SUB_BWP1() != null) {
            R50Cell7.setCellValue(record.getR50_BAL_SUB_BWP1().doubleValue());
            R50Cell7.setCellStyle(numberStyle);
        } else {
            R50Cell7.setCellValue("");
            R50Cell7.setCellStyle(textStyle);
        }
        Cell R50Cell8 = row.createCell(10);
        if (record.getR50_BAL_SUB_BWP2() != null) {
            R50Cell8.setCellValue(record.getR50_BAL_SUB_BWP2().doubleValue());
            R50Cell8.setCellStyle(numberStyle);
        } else {
            R50Cell8.setCellValue("");
            R50Cell8.setCellStyle(textStyle);
        }
        Cell R50Cell9 = row.createCell(11);
        if (record.getR50_BAL_ACT_SUB_BWP1() != null) {
            R50Cell9.setCellValue(record.getR50_BAL_ACT_SUB_BWP1().doubleValue());
            R50Cell9.setCellStyle(numberStyle);
        } else {
            R50Cell9.setCellValue("");
            R50Cell9.setCellStyle(textStyle);
        }
        Cell R50Cell10 = row.createCell(12);
        if (record.getR50_BAL_ACT_SUB_BWP2() != null) {
            R50Cell10.setCellValue(record.getR50_BAL_ACT_SUB_BWP2().doubleValue());
            R50Cell10.setCellStyle(numberStyle);
        } else {
            R50Cell10.setCellValue("");
            R50Cell10.setCellStyle(textStyle);
        }

        // R51
        row = sheet.getRow(50);
        Cell R51Cell1 = row.createCell(3);
        if (record.getR51_FIG_BAL_BWP1() != null) {
            R51Cell1.setCellValue(record.getR51_FIG_BAL_BWP1().doubleValue());
            R51Cell1.setCellStyle(numberStyle);
        } else {
            R51Cell1.setCellValue("");
            R51Cell1.setCellStyle(textStyle);
        }
        Cell R51Cell2 = row.createCell(4);
        if (record.getR51_FIG_BAL_BWP2() != null) {
            R51Cell2.setCellValue(record.getR51_FIG_BAL_BWP2().doubleValue());
            R51Cell2.setCellStyle(numberStyle);
        } else {
            R51Cell2.setCellValue("");
            R51Cell2.setCellStyle(textStyle);
        }
        Cell R51Cell3 = row.createCell(5);
        if (record.getR51_AMT_ADJ_BWP1() != null) {
            R51Cell3.setCellValue(record.getR51_AMT_ADJ_BWP1().doubleValue());
            R51Cell3.setCellStyle(numberStyle);
        } else {
            R51Cell3.setCellValue("");
            R51Cell3.setCellStyle(textStyle);
        }
        Cell R51Cell4 = row.createCell(6);
        if (record.getR51_AMT_ADJ_BWP2() != null) {
            R51Cell4.setCellValue(record.getR51_AMT_ADJ_BWP2().doubleValue());
            R51Cell4.setCellStyle(numberStyle);
        } else {
            R51Cell4.setCellValue("");
            R51Cell4.setCellStyle(textStyle);
        }
        Cell R51Cell5 = row.createCell(7);
        if (record.getR51_NET_AMT_BWP1() != null) {
            R51Cell5.setCellValue(record.getR51_NET_AMT_BWP1().doubleValue());
            R51Cell5.setCellStyle(numberStyle);
        } else {
            R51Cell5.setCellValue("");
            R51Cell5.setCellStyle(textStyle);
        }
        Cell R51Cell6 = row.createCell(8);
        if (record.getR51_NET_AMT_BWP2() != null) {
            R51Cell6.setCellValue(record.getR51_NET_AMT_BWP2().doubleValue());
            R51Cell6.setCellStyle(numberStyle);
        } else {
            R51Cell6.setCellValue("");
            R51Cell6.setCellStyle(textStyle);
        }
        Cell R51Cell7 = row.createCell(9);
        if (record.getR51_BAL_SUB_BWP1() != null) {
            R51Cell7.setCellValue(record.getR51_BAL_SUB_BWP1().doubleValue());
            R51Cell7.setCellStyle(numberStyle);
        } else {
            R51Cell7.setCellValue("");
            R51Cell7.setCellStyle(textStyle);
        }
        Cell R51Cell8 = row.createCell(10);
        if (record.getR51_BAL_SUB_BWP2() != null) {
            R51Cell8.setCellValue(record.getR51_BAL_SUB_BWP2().doubleValue());
            R51Cell8.setCellStyle(numberStyle);
        } else {
            R51Cell8.setCellValue("");
            R51Cell8.setCellStyle(textStyle);
        }
        Cell R51Cell9 = row.createCell(11);
        if (record.getR51_BAL_ACT_SUB_BWP1() != null) {
            R51Cell9.setCellValue(record.getR51_BAL_ACT_SUB_BWP1().doubleValue());
            R51Cell9.setCellStyle(numberStyle);
        } else {
            R51Cell9.setCellValue("");
            R51Cell9.setCellStyle(textStyle);
        }
        Cell R51Cell10 = row.createCell(12);
        if (record.getR51_BAL_ACT_SUB_BWP2() != null) {
            R51Cell10.setCellValue(record.getR51_BAL_ACT_SUB_BWP2().doubleValue());
            R51Cell10.setCellStyle(numberStyle);
        } else {
            R51Cell10.setCellValue("");
            R51Cell10.setCellStyle(textStyle);
        }

        // R52
        row = sheet.getRow(51);
        Cell R52Cell1 = row.createCell(3);
        if (record.getR52_FIG_BAL_BWP1() != null) {
            R52Cell1.setCellValue(record.getR52_FIG_BAL_BWP1().doubleValue());
            R52Cell1.setCellStyle(numberStyle);
        } else {
            R52Cell1.setCellValue("");
            R52Cell1.setCellStyle(textStyle);
        }
        Cell R52Cell2 = row.createCell(4);
        if (record.getR52_FIG_BAL_BWP2() != null) {
            R52Cell2.setCellValue(record.getR52_FIG_BAL_BWP2().doubleValue());
            R52Cell2.setCellStyle(numberStyle);
        } else {
            R52Cell2.setCellValue("");
            R52Cell2.setCellStyle(textStyle);
        }
        Cell R52Cell3 = row.createCell(5);
        if (record.getR52_AMT_ADJ_BWP1() != null) {
            R52Cell3.setCellValue(record.getR52_AMT_ADJ_BWP1().doubleValue());
            R52Cell3.setCellStyle(numberStyle);
        } else {
            R52Cell3.setCellValue("");
            R52Cell3.setCellStyle(textStyle);
        }
        Cell R52Cell4 = row.createCell(6);
        if (record.getR52_AMT_ADJ_BWP2() != null) {
            R52Cell4.setCellValue(record.getR52_AMT_ADJ_BWP2().doubleValue());
            R52Cell4.setCellStyle(numberStyle);
        } else {
            R52Cell4.setCellValue("");
            R52Cell4.setCellStyle(textStyle);
        }
        Cell R52Cell5 = row.createCell(7);
        if (record.getR52_NET_AMT_BWP1() != null) {
            R52Cell5.setCellValue(record.getR52_NET_AMT_BWP1().doubleValue());
            R52Cell5.setCellStyle(numberStyle);
        } else {
            R52Cell5.setCellValue("");
            R52Cell5.setCellStyle(textStyle);
        }
        Cell R52Cell6 = row.createCell(8);
        if (record.getR52_NET_AMT_BWP2() != null) {
            R52Cell6.setCellValue(record.getR52_NET_AMT_BWP2().doubleValue());
            R52Cell6.setCellStyle(numberStyle);
        } else {
            R52Cell6.setCellValue("");
            R52Cell6.setCellStyle(textStyle);
        }
        Cell R52Cell7 = row.createCell(9);
        if (record.getR52_BAL_SUB_BWP1() != null) {
            R52Cell7.setCellValue(record.getR52_BAL_SUB_BWP1().doubleValue());
            R52Cell7.setCellStyle(numberStyle);
        } else {
            R52Cell7.setCellValue("");
            R52Cell7.setCellStyle(textStyle);
        }
        Cell R52Cell8 = row.createCell(10);
        if (record.getR52_BAL_SUB_BWP2() != null) {
            R52Cell8.setCellValue(record.getR52_BAL_SUB_BWP2().doubleValue());
            R52Cell8.setCellStyle(numberStyle);
        } else {
            R52Cell8.setCellValue("");
            R52Cell8.setCellStyle(textStyle);
        }
        Cell R52Cell9 = row.createCell(11);
        if (record.getR52_BAL_ACT_SUB_BWP1() != null) {
            R52Cell9.setCellValue(record.getR52_BAL_ACT_SUB_BWP1().doubleValue());
            R52Cell9.setCellStyle(numberStyle);
        } else {
            R52Cell9.setCellValue("");
            R52Cell9.setCellStyle(textStyle);
        }
        Cell R52Cell10 = row.createCell(12);
        if (record.getR52_BAL_ACT_SUB_BWP2() != null) {
            R52Cell10.setCellValue(record.getR52_BAL_ACT_SUB_BWP2().doubleValue());
            R52Cell10.setCellStyle(numberStyle);
        } else {
            R52Cell10.setCellValue("");
            R52Cell10.setCellStyle(textStyle);
        }

        // R53
        row = sheet.getRow(52);
        Cell R53Cell1 = row.createCell(3);
        if (record.getR53_FIG_BAL_BWP1() != null) {
            R53Cell1.setCellValue(record.getR53_FIG_BAL_BWP1().doubleValue());
            R53Cell1.setCellStyle(numberStyle);
        } else {
            R53Cell1.setCellValue("");
            R53Cell1.setCellStyle(textStyle);
        }
        Cell R53Cell2 = row.createCell(4);
        if (record.getR53_FIG_BAL_BWP2() != null) {
            R53Cell2.setCellValue(record.getR53_FIG_BAL_BWP2().doubleValue());
            R53Cell2.setCellStyle(numberStyle);
        } else {
            R53Cell2.setCellValue("");
            R53Cell2.setCellStyle(textStyle);
        }
        Cell R53Cell3 = row.createCell(5);
        if (record.getR53_AMT_ADJ_BWP1() != null) {
            R53Cell3.setCellValue(record.getR53_AMT_ADJ_BWP1().doubleValue());
            R53Cell3.setCellStyle(numberStyle);
        } else {
            R53Cell3.setCellValue("");
            R53Cell3.setCellStyle(textStyle);
        }
        Cell R53Cell4 = row.createCell(6);
        if (record.getR53_AMT_ADJ_BWP2() != null) {
            R53Cell4.setCellValue(record.getR53_AMT_ADJ_BWP2().doubleValue());
            R53Cell4.setCellStyle(numberStyle);
        } else {
            R53Cell4.setCellValue("");
            R53Cell4.setCellStyle(textStyle);
        }
        Cell R53Cell5 = row.createCell(7);
        if (record.getR53_NET_AMT_BWP1() != null) {
            R53Cell5.setCellValue(record.getR53_NET_AMT_BWP1().doubleValue());
            R53Cell5.setCellStyle(numberStyle);
        } else {
            R53Cell5.setCellValue("");
            R53Cell5.setCellStyle(textStyle);
        }
        Cell R53Cell6 = row.createCell(8);
        if (record.getR53_NET_AMT_BWP2() != null) {
            R53Cell6.setCellValue(record.getR53_NET_AMT_BWP2().doubleValue());
            R53Cell6.setCellStyle(numberStyle);
        } else {
            R53Cell6.setCellValue("");
            R53Cell6.setCellStyle(textStyle);
        }
        Cell R53Cell7 = row.createCell(9);
        if (record.getR53_BAL_SUB_BWP1() != null) {
            R53Cell7.setCellValue(record.getR53_BAL_SUB_BWP1().doubleValue());
            R53Cell7.setCellStyle(numberStyle);
        } else {
            R53Cell7.setCellValue("");
            R53Cell7.setCellStyle(textStyle);
        }
        Cell R53Cell8 = row.createCell(10);
        if (record.getR53_BAL_SUB_BWP2() != null) {
            R53Cell8.setCellValue(record.getR53_BAL_SUB_BWP2().doubleValue());
            R53Cell8.setCellStyle(numberStyle);
        } else {
            R53Cell8.setCellValue("");
            R53Cell8.setCellStyle(textStyle);
        }
        Cell R53Cell9 = row.createCell(11);
        if (record.getR53_BAL_ACT_SUB_BWP1() != null) {
            R53Cell9.setCellValue(record.getR53_BAL_ACT_SUB_BWP1().doubleValue());
            R53Cell9.setCellStyle(numberStyle);
        } else {
            R53Cell9.setCellValue("");
            R53Cell9.setCellStyle(textStyle);
        }
        Cell R53Cell10 = row.createCell(12);
        if (record.getR53_BAL_ACT_SUB_BWP2() != null) {
            R53Cell10.setCellValue(record.getR53_BAL_ACT_SUB_BWP2().doubleValue());
            R53Cell10.setCellStyle(numberStyle);
        } else {
            R53Cell10.setCellValue("");
            R53Cell10.setCellStyle(textStyle);
        }

        // R54
        row = sheet.getRow(53);
        Cell R54Cell1 = row.createCell(3);
        if (record.getR54_FIG_BAL_BWP1() != null) {
            R54Cell1.setCellValue(record.getR54_FIG_BAL_BWP1().doubleValue());
            R54Cell1.setCellStyle(numberStyle);
        } else {
            R54Cell1.setCellValue("");
            R54Cell1.setCellStyle(textStyle);
        }
        Cell R54Cell2 = row.createCell(4);
        if (record.getR54_FIG_BAL_BWP2() != null) {
            R54Cell2.setCellValue(record.getR54_FIG_BAL_BWP2().doubleValue());
            R54Cell2.setCellStyle(numberStyle);
        } else {
            R54Cell2.setCellValue("");
            R54Cell2.setCellStyle(textStyle);
        }
        Cell R54Cell3 = row.createCell(5);
        if (record.getR54_AMT_ADJ_BWP1() != null) {
            R54Cell3.setCellValue(record.getR54_AMT_ADJ_BWP1().doubleValue());
            R54Cell3.setCellStyle(numberStyle);
        } else {
            R54Cell3.setCellValue("");
            R54Cell3.setCellStyle(textStyle);
        }
        Cell R54Cell4 = row.createCell(6);
        if (record.getR54_AMT_ADJ_BWP2() != null) {
            R54Cell4.setCellValue(record.getR54_AMT_ADJ_BWP2().doubleValue());
            R54Cell4.setCellStyle(numberStyle);
        } else {
            R54Cell4.setCellValue("");
            R54Cell4.setCellStyle(textStyle);
        }
        Cell R54Cell5 = row.createCell(7);
        if (record.getR54_NET_AMT_BWP1() != null) {
            R54Cell5.setCellValue(record.getR54_NET_AMT_BWP1().doubleValue());
            R54Cell5.setCellStyle(numberStyle);
        } else {
            R54Cell5.setCellValue("");
            R54Cell5.setCellStyle(textStyle);
        }
        Cell R54Cell6 = row.createCell(8);
        if (record.getR54_NET_AMT_BWP2() != null) {
            R54Cell6.setCellValue(record.getR54_NET_AMT_BWP2().doubleValue());
            R54Cell6.setCellStyle(numberStyle);
        } else {
            R54Cell6.setCellValue("");
            R54Cell6.setCellStyle(textStyle);
        }
        Cell R54Cell7 = row.createCell(9);
        if (record.getR54_BAL_SUB_BWP1() != null) {
            R54Cell7.setCellValue(record.getR54_BAL_SUB_BWP1().doubleValue());
            R54Cell7.setCellStyle(numberStyle);
        } else {
            R54Cell7.setCellValue("");
            R54Cell7.setCellStyle(textStyle);
        }
        Cell R54Cell8 = row.createCell(10);
        if (record.getR54_BAL_SUB_BWP2() != null) {
            R54Cell8.setCellValue(record.getR54_BAL_SUB_BWP2().doubleValue());
            R54Cell8.setCellStyle(numberStyle);
        } else {
            R54Cell8.setCellValue("");
            R54Cell8.setCellStyle(textStyle);
        }
        Cell R54Cell9 = row.createCell(11);
        if (record.getR54_BAL_ACT_SUB_BWP1() != null) {
            R54Cell9.setCellValue(record.getR54_BAL_ACT_SUB_BWP1().doubleValue());
            R54Cell9.setCellStyle(numberStyle);
        } else {
            R54Cell9.setCellValue("");
            R54Cell9.setCellStyle(textStyle);
        }
        Cell R54Cell10 = row.createCell(12);
        if (record.getR54_BAL_ACT_SUB_BWP2() != null) {
            R54Cell10.setCellValue(record.getR54_BAL_ACT_SUB_BWP2().doubleValue());
            R54Cell10.setCellStyle(numberStyle);
        } else {
            R54Cell10.setCellValue("");
            R54Cell10.setCellStyle(textStyle);
        }

        // R55
        row = sheet.getRow(54);
        Cell R55Cell1 = row.createCell(3);
        if (record.getR55_FIG_BAL_BWP1() != null) {
            R55Cell1.setCellValue(record.getR55_FIG_BAL_BWP1().doubleValue());
            R55Cell1.setCellStyle(numberStyle);
        } else {
            R55Cell1.setCellValue("");
            R55Cell1.setCellStyle(textStyle);
        }
        Cell R55Cell2 = row.createCell(4);
        if (record.getR55_FIG_BAL_BWP2() != null) {
            R55Cell2.setCellValue(record.getR55_FIG_BAL_BWP2().doubleValue());
            R55Cell2.setCellStyle(numberStyle);
        } else {
            R55Cell2.setCellValue("");
            R55Cell2.setCellStyle(textStyle);
        }
        Cell R55Cell3 = row.createCell(5);
        if (record.getR55_AMT_ADJ_BWP1() != null) {
            R55Cell3.setCellValue(record.getR55_AMT_ADJ_BWP1().doubleValue());
            R55Cell3.setCellStyle(numberStyle);
        } else {
            R55Cell3.setCellValue("");
            R55Cell3.setCellStyle(textStyle);
        }
        Cell R55Cell4 = row.createCell(6);
        if (record.getR55_AMT_ADJ_BWP2() != null) {
            R55Cell4.setCellValue(record.getR55_AMT_ADJ_BWP2().doubleValue());
            R55Cell4.setCellStyle(numberStyle);
        } else {
            R55Cell4.setCellValue("");
            R55Cell4.setCellStyle(textStyle);
        }
        Cell R55Cell5 = row.createCell(7);
        if (record.getR55_NET_AMT_BWP1() != null) {
            R55Cell5.setCellValue(record.getR55_NET_AMT_BWP1().doubleValue());
            R55Cell5.setCellStyle(numberStyle);
        } else {
            R55Cell5.setCellValue("");
            R55Cell5.setCellStyle(textStyle);
        }
        Cell R55Cell6 = row.createCell(8);
        if (record.getR55_NET_AMT_BWP2() != null) {
            R55Cell6.setCellValue(record.getR55_NET_AMT_BWP2().doubleValue());
            R55Cell6.setCellStyle(numberStyle);
        } else {
            R55Cell6.setCellValue("");
            R55Cell6.setCellStyle(textStyle);
        }
        Cell R55Cell7 = row.createCell(9);
        if (record.getR55_BAL_SUB_BWP1() != null) {
            R55Cell7.setCellValue(record.getR55_BAL_SUB_BWP1().doubleValue());
            R55Cell7.setCellStyle(numberStyle);
        } else {
            R55Cell7.setCellValue("");
            R55Cell7.setCellStyle(textStyle);
        }
        Cell R55Cell8 = row.createCell(10);
        if (record.getR55_BAL_SUB_BWP2() != null) {
            R55Cell8.setCellValue(record.getR55_BAL_SUB_BWP2().doubleValue());
            R55Cell8.setCellStyle(numberStyle);
        } else {
            R55Cell8.setCellValue("");
            R55Cell8.setCellStyle(textStyle);
        }
        Cell R55Cell9 = row.createCell(11);
        if (record.getR55_BAL_ACT_SUB_BWP1() != null) {
            R55Cell9.setCellValue(record.getR55_BAL_ACT_SUB_BWP1().doubleValue());
            R55Cell9.setCellStyle(numberStyle);
        } else {
            R55Cell9.setCellValue("");
            R55Cell9.setCellStyle(textStyle);
        }
        Cell R55Cell10 = row.createCell(12);
        if (record.getR55_BAL_ACT_SUB_BWP2() != null) {
            R55Cell10.setCellValue(record.getR55_BAL_ACT_SUB_BWP2().doubleValue());
            R55Cell10.setCellStyle(numberStyle);
        } else {
            R55Cell10.setCellValue("");
            R55Cell10.setCellStyle(textStyle);
        }

        // R56
        row = sheet.getRow(55);
        Cell R56Cell1 = row.createCell(3);
        if (record.getR56_FIG_BAL_BWP1() != null) {
            R56Cell1.setCellValue(record.getR56_FIG_BAL_BWP1().doubleValue());
            R56Cell1.setCellStyle(numberStyle);
        } else {
            R56Cell1.setCellValue("");
            R56Cell1.setCellStyle(textStyle);
        }
        Cell R56Cell2 = row.createCell(4);
        if (record.getR56_FIG_BAL_BWP2() != null) {
            R56Cell2.setCellValue(record.getR56_FIG_BAL_BWP2().doubleValue());
            R56Cell2.setCellStyle(numberStyle);
        } else {
            R56Cell2.setCellValue("");
            R56Cell2.setCellStyle(textStyle);
        }
        Cell R56Cell3 = row.createCell(5);
        if (record.getR56_AMT_ADJ_BWP1() != null) {
            R56Cell3.setCellValue(record.getR56_AMT_ADJ_BWP1().doubleValue());
            R56Cell3.setCellStyle(numberStyle);
        } else {
            R56Cell3.setCellValue("");
            R56Cell3.setCellStyle(textStyle);
        }
        Cell R56Cell4 = row.createCell(6);
        if (record.getR56_AMT_ADJ_BWP2() != null) {
            R56Cell4.setCellValue(record.getR56_AMT_ADJ_BWP2().doubleValue());
            R56Cell4.setCellStyle(numberStyle);
        } else {
            R56Cell4.setCellValue("");
            R56Cell4.setCellStyle(textStyle);
        }
        Cell R56Cell5 = row.createCell(7);
        if (record.getR56_NET_AMT_BWP1() != null) {
            R56Cell5.setCellValue(record.getR56_NET_AMT_BWP1().doubleValue());
            R56Cell5.setCellStyle(numberStyle);
        } else {
            R56Cell5.setCellValue("");
            R56Cell5.setCellStyle(textStyle);
        }
        Cell R56Cell6 = row.createCell(8);
        if (record.getR56_NET_AMT_BWP2() != null) {
            R56Cell6.setCellValue(record.getR56_NET_AMT_BWP2().doubleValue());
            R56Cell6.setCellStyle(numberStyle);
        } else {
            R56Cell6.setCellValue("");
            R56Cell6.setCellStyle(textStyle);
        }
        Cell R56Cell7 = row.createCell(9);
        if (record.getR56_BAL_SUB_BWP1() != null) {
            R56Cell7.setCellValue(record.getR56_BAL_SUB_BWP1().doubleValue());
            R56Cell7.setCellStyle(numberStyle);
        } else {
            R56Cell7.setCellValue("");
            R56Cell7.setCellStyle(textStyle);
        }
        Cell R56Cell8 = row.createCell(10);
        if (record.getR56_BAL_SUB_BWP2() != null) {
            R56Cell8.setCellValue(record.getR56_BAL_SUB_BWP2().doubleValue());
            R56Cell8.setCellStyle(numberStyle);
        } else {
            R56Cell8.setCellValue("");
            R56Cell8.setCellStyle(textStyle);
        }
        Cell R56Cell9 = row.createCell(11);
        if (record.getR56_BAL_ACT_SUB_BWP1() != null) {
            R56Cell9.setCellValue(record.getR56_BAL_ACT_SUB_BWP1().doubleValue());
            R56Cell9.setCellStyle(numberStyle);
        } else {
            R56Cell9.setCellValue("");
            R56Cell9.setCellStyle(textStyle);
        }
        Cell R56Cell10 = row.createCell(12);
        if (record.getR56_BAL_ACT_SUB_BWP2() != null) {
            R56Cell10.setCellValue(record.getR56_BAL_ACT_SUB_BWP2().doubleValue());
            R56Cell10.setCellStyle(numberStyle);
        } else {
            R56Cell10.setCellValue("");
            R56Cell10.setCellStyle(textStyle);
        }

        // R57
        row = sheet.getRow(56);
        Cell R57Cell1 = row.createCell(3);
        if (record.getR57_FIG_BAL_BWP1() != null) {
            R57Cell1.setCellValue(record.getR57_FIG_BAL_BWP1().doubleValue());
            R57Cell1.setCellStyle(numberStyle);
        } else {
            R57Cell1.setCellValue("");
            R57Cell1.setCellStyle(textStyle);
        }
        Cell R57Cell2 = row.createCell(4);
        if (record.getR57_FIG_BAL_BWP2() != null) {
            R57Cell2.setCellValue(record.getR57_FIG_BAL_BWP2().doubleValue());
            R57Cell2.setCellStyle(numberStyle);
        } else {
            R57Cell2.setCellValue("");
            R57Cell2.setCellStyle(textStyle);
        }
        Cell R57Cell3 = row.createCell(5);
        if (record.getR57_AMT_ADJ_BWP1() != null) {
            R57Cell3.setCellValue(record.getR57_AMT_ADJ_BWP1().doubleValue());
            R57Cell3.setCellStyle(numberStyle);
        } else {
            R57Cell3.setCellValue("");
            R57Cell3.setCellStyle(textStyle);
        }
        Cell R57Cell4 = row.createCell(6);
        if (record.getR57_AMT_ADJ_BWP2() != null) {
            R57Cell4.setCellValue(record.getR57_AMT_ADJ_BWP2().doubleValue());
            R57Cell4.setCellStyle(numberStyle);
        } else {
            R57Cell4.setCellValue("");
            R57Cell4.setCellStyle(textStyle);
        }
        Cell R57Cell5 = row.createCell(7);
        if (record.getR57_NET_AMT_BWP1() != null) {
            R57Cell5.setCellValue(record.getR57_NET_AMT_BWP1().doubleValue());
            R57Cell5.setCellStyle(numberStyle);
        } else {
            R57Cell5.setCellValue("");
            R57Cell5.setCellStyle(textStyle);
        }
        Cell R57Cell6 = row.createCell(8);
        if (record.getR57_NET_AMT_BWP2() != null) {
            R57Cell6.setCellValue(record.getR57_NET_AMT_BWP2().doubleValue());
            R57Cell6.setCellStyle(numberStyle);
        } else {
            R57Cell6.setCellValue("");
            R57Cell6.setCellStyle(textStyle);
        }
        Cell R57Cell7 = row.createCell(9);
        if (record.getR57_BAL_SUB_BWP1() != null) {
            R57Cell7.setCellValue(record.getR57_BAL_SUB_BWP1().doubleValue());
            R57Cell7.setCellStyle(numberStyle);
        } else {
            R57Cell7.setCellValue("");
            R57Cell7.setCellStyle(textStyle);
        }
        Cell R57Cell8 = row.createCell(10);
        if (record.getR57_BAL_SUB_BWP2() != null) {
            R57Cell8.setCellValue(record.getR57_BAL_SUB_BWP2().doubleValue());
            R57Cell8.setCellStyle(numberStyle);
        } else {
            R57Cell8.setCellValue("");
            R57Cell8.setCellStyle(textStyle);
        }
        Cell R57Cell9 = row.createCell(11);
        if (record.getR57_BAL_ACT_SUB_BWP1() != null) {
            R57Cell9.setCellValue(record.getR57_BAL_ACT_SUB_BWP1().doubleValue());
            R57Cell9.setCellStyle(numberStyle);
        } else {
            R57Cell9.setCellValue("");
            R57Cell9.setCellStyle(textStyle);
        }
        Cell R57Cell10 = row.createCell(12);
        if (record.getR57_BAL_ACT_SUB_BWP2() != null) {
            R57Cell10.setCellValue(record.getR57_BAL_ACT_SUB_BWP2().doubleValue());
            R57Cell10.setCellStyle(numberStyle);
        } else {
            R57Cell10.setCellValue("");
            R57Cell10.setCellStyle(textStyle);
        }

        // R58
        row = sheet.getRow(57);
        Cell R58Cell1 = row.createCell(3);
        if (record.getR58_FIG_BAL_BWP1() != null) {
            R58Cell1.setCellValue(record.getR58_FIG_BAL_BWP1().doubleValue());
            R58Cell1.setCellStyle(numberStyle);
        } else {
            R58Cell1.setCellValue("");
            R58Cell1.setCellStyle(textStyle);
        }
        Cell R58Cell2 = row.createCell(4);
        if (record.getR58_FIG_BAL_BWP2() != null) {
            R58Cell2.setCellValue(record.getR58_FIG_BAL_BWP2().doubleValue());
            R58Cell2.setCellStyle(numberStyle);
        } else {
            R58Cell2.setCellValue("");
            R58Cell2.setCellStyle(textStyle);
        }
        Cell R58Cell3 = row.createCell(5);
        if (record.getR58_AMT_ADJ_BWP1() != null) {
            R58Cell3.setCellValue(record.getR58_AMT_ADJ_BWP1().doubleValue());
            R58Cell3.setCellStyle(numberStyle);
        } else {
            R58Cell3.setCellValue("");
            R58Cell3.setCellStyle(textStyle);
        }
        Cell R58Cell4 = row.createCell(6);
        if (record.getR58_AMT_ADJ_BWP2() != null) {
            R58Cell4.setCellValue(record.getR58_AMT_ADJ_BWP2().doubleValue());
            R58Cell4.setCellStyle(numberStyle);
        } else {
            R58Cell4.setCellValue("");
            R58Cell4.setCellStyle(textStyle);
        }
        Cell R58Cell5 = row.createCell(7);
        if (record.getR58_NET_AMT_BWP1() != null) {
            R58Cell5.setCellValue(record.getR58_NET_AMT_BWP1().doubleValue());
            R58Cell5.setCellStyle(numberStyle);
        } else {
            R58Cell5.setCellValue("");
            R58Cell5.setCellStyle(textStyle);
        }
        Cell R58Cell6 = row.createCell(8);
        if (record.getR58_NET_AMT_BWP2() != null) {
            R58Cell6.setCellValue(record.getR58_NET_AMT_BWP2().doubleValue());
            R58Cell6.setCellStyle(numberStyle);
        } else {
            R58Cell6.setCellValue("");
            R58Cell6.setCellStyle(textStyle);
        }
        Cell R58Cell7 = row.createCell(9);
        if (record.getR58_BAL_SUB_BWP1() != null) {
            R58Cell7.setCellValue(record.getR58_BAL_SUB_BWP1().doubleValue());
            R58Cell7.setCellStyle(numberStyle);
        } else {
            R58Cell7.setCellValue("");
            R58Cell7.setCellStyle(textStyle);
        }
        Cell R58Cell8 = row.createCell(10);
        if (record.getR58_BAL_SUB_BWP2() != null) {
            R58Cell8.setCellValue(record.getR58_BAL_SUB_BWP2().doubleValue());
            R58Cell8.setCellStyle(numberStyle);
        } else {
            R58Cell8.setCellValue("");
            R58Cell8.setCellStyle(textStyle);
        }
        Cell R58Cell9 = row.createCell(11);
        if (record.getR58_BAL_ACT_SUB_BWP1() != null) {
            R58Cell9.setCellValue(record.getR58_BAL_ACT_SUB_BWP1().doubleValue());
            R58Cell9.setCellStyle(numberStyle);
        } else {
            R58Cell9.setCellValue("");
            R58Cell9.setCellStyle(textStyle);
        }
        Cell R58Cell10 = row.createCell(12);
        if (record.getR58_BAL_ACT_SUB_BWP2() != null) {
            R58Cell10.setCellValue(record.getR58_BAL_ACT_SUB_BWP2().doubleValue());
            R58Cell10.setCellStyle(numberStyle);
        } else {
            R58Cell10.setCellValue("");
            R58Cell10.setCellStyle(textStyle);
        }

        // R59
        row = sheet.getRow(58);
        Cell R59Cell1 = row.createCell(3);
        if (record.getR59_FIG_BAL_BWP1() != null) {
            R59Cell1.setCellValue(record.getR59_FIG_BAL_BWP1().doubleValue());
            R59Cell1.setCellStyle(numberStyle);
        } else {
            R59Cell1.setCellValue("");
            R59Cell1.setCellStyle(textStyle);
        }
        Cell R59Cell2 = row.createCell(4);
        if (record.getR59_FIG_BAL_BWP2() != null) {
            R59Cell2.setCellValue(record.getR59_FIG_BAL_BWP2().doubleValue());
            R59Cell2.setCellStyle(numberStyle);
        } else {
            R59Cell2.setCellValue("");
            R59Cell2.setCellStyle(textStyle);
        }
        Cell R59Cell3 = row.createCell(5);
        if (record.getR59_AMT_ADJ_BWP1() != null) {
            R59Cell3.setCellValue(record.getR59_AMT_ADJ_BWP1().doubleValue());
            R59Cell3.setCellStyle(numberStyle);
        } else {
            R59Cell3.setCellValue("");
            R59Cell3.setCellStyle(textStyle);
        }
        Cell R59Cell4 = row.createCell(6);
        if (record.getR59_AMT_ADJ_BWP2() != null) {
            R59Cell4.setCellValue(record.getR59_AMT_ADJ_BWP2().doubleValue());
            R59Cell4.setCellStyle(numberStyle);
        } else {
            R59Cell4.setCellValue("");
            R59Cell4.setCellStyle(textStyle);
        }
        Cell R59Cell5 = row.createCell(7);
        if (record.getR59_NET_AMT_BWP1() != null) {
            R59Cell5.setCellValue(record.getR59_NET_AMT_BWP1().doubleValue());
            R59Cell5.setCellStyle(numberStyle);
        } else {
            R59Cell5.setCellValue("");
            R59Cell5.setCellStyle(textStyle);
        }
        Cell R59Cell6 = row.createCell(8);
        if (record.getR59_NET_AMT_BWP2() != null) {
            R59Cell6.setCellValue(record.getR59_NET_AMT_BWP2().doubleValue());
            R59Cell6.setCellStyle(numberStyle);
        } else {
            R59Cell6.setCellValue("");
            R59Cell6.setCellStyle(textStyle);
        }
        Cell R59Cell7 = row.createCell(9);
        if (record.getR59_BAL_SUB_BWP1() != null) {
            R59Cell7.setCellValue(record.getR59_BAL_SUB_BWP1().doubleValue());
            R59Cell7.setCellStyle(numberStyle);
        } else {
            R59Cell7.setCellValue("");
            R59Cell7.setCellStyle(textStyle);
        }
        Cell R59Cell8 = row.createCell(10);
        if (record.getR59_BAL_SUB_BWP2() != null) {
            R59Cell8.setCellValue(record.getR59_BAL_SUB_BWP2().doubleValue());
            R59Cell8.setCellStyle(numberStyle);
        } else {
            R59Cell8.setCellValue("");
            R59Cell8.setCellStyle(textStyle);
        }
        Cell R59Cell9 = row.createCell(11);
        if (record.getR59_BAL_ACT_SUB_BWP1() != null) {
            R59Cell9.setCellValue(record.getR59_BAL_ACT_SUB_BWP1().doubleValue());
            R59Cell9.setCellStyle(numberStyle);
        } else {
            R59Cell9.setCellValue("");
            R59Cell9.setCellStyle(textStyle);
        }
        Cell R59Cell10 = row.createCell(12);
        if (record.getR59_BAL_ACT_SUB_BWP2() != null) {
            R59Cell10.setCellValue(record.getR59_BAL_ACT_SUB_BWP2().doubleValue());
            R59Cell10.setCellStyle(numberStyle);
        } else {
            R59Cell10.setCellValue("");
            R59Cell10.setCellStyle(textStyle);
        }

        // R60
        row = sheet.getRow(59);
        Cell R60Cell1 = row.createCell(3);
        if (record.getR60_FIG_BAL_BWP1() != null) {
            R60Cell1.setCellValue(record.getR60_FIG_BAL_BWP1().doubleValue());
            R60Cell1.setCellStyle(numberStyle);
        } else {
            R60Cell1.setCellValue("");
            R60Cell1.setCellStyle(textStyle);
        }
        Cell R60Cell2 = row.createCell(4);
        if (record.getR60_FIG_BAL_BWP2() != null) {
            R60Cell2.setCellValue(record.getR60_FIG_BAL_BWP2().doubleValue());
            R60Cell2.setCellStyle(numberStyle);
        } else {
            R60Cell2.setCellValue("");
            R60Cell2.setCellStyle(textStyle);
        }
        Cell R60Cell3 = row.createCell(5);
        if (record.getR60_AMT_ADJ_BWP1() != null) {
            R60Cell3.setCellValue(record.getR60_AMT_ADJ_BWP1().doubleValue());
            R60Cell3.setCellStyle(numberStyle);
        } else {
            R60Cell3.setCellValue("");
            R60Cell3.setCellStyle(textStyle);
        }
        Cell R60Cell4 = row.createCell(6);
        if (record.getR60_AMT_ADJ_BWP2() != null) {
            R60Cell4.setCellValue(record.getR60_AMT_ADJ_BWP2().doubleValue());
            R60Cell4.setCellStyle(numberStyle);
        } else {
            R60Cell4.setCellValue("");
            R60Cell4.setCellStyle(textStyle);
        }
        Cell R60Cell5 = row.createCell(7);
        if (record.getR60_NET_AMT_BWP1() != null) {
            R60Cell5.setCellValue(record.getR60_NET_AMT_BWP1().doubleValue());
            R60Cell5.setCellStyle(numberStyle);
        } else {
            R60Cell5.setCellValue("");
            R60Cell5.setCellStyle(textStyle);
        }
        Cell R60Cell6 = row.createCell(8);
        if (record.getR60_NET_AMT_BWP2() != null) {
            R60Cell6.setCellValue(record.getR60_NET_AMT_BWP2().doubleValue());
            R60Cell6.setCellStyle(numberStyle);
        } else {
            R60Cell6.setCellValue("");
            R60Cell6.setCellStyle(textStyle);
        }
        Cell R60Cell7 = row.createCell(9);
        if (record.getR60_BAL_SUB_BWP1() != null) {
            R60Cell7.setCellValue(record.getR60_BAL_SUB_BWP1().doubleValue());
            R60Cell7.setCellStyle(numberStyle);
        } else {
            R60Cell7.setCellValue("");
            R60Cell7.setCellStyle(textStyle);
        }
        Cell R60Cell8 = row.createCell(10);
        if (record.getR60_BAL_SUB_BWP2() != null) {
            R60Cell8.setCellValue(record.getR60_BAL_SUB_BWP2().doubleValue());
            R60Cell8.setCellStyle(numberStyle);
        } else {
            R60Cell8.setCellValue("");
            R60Cell8.setCellStyle(textStyle);
        }
        Cell R60Cell9 = row.createCell(11);
        if (record.getR60_BAL_ACT_SUB_BWP1() != null) {
            R60Cell9.setCellValue(record.getR60_BAL_ACT_SUB_BWP1().doubleValue());
            R60Cell9.setCellStyle(numberStyle);
        } else {
            R60Cell9.setCellValue("");
            R60Cell9.setCellStyle(textStyle);
        }
        Cell R60Cell10 = row.createCell(12);
        if (record.getR60_BAL_ACT_SUB_BWP2() != null) {
            R60Cell10.setCellValue(record.getR60_BAL_ACT_SUB_BWP2().doubleValue());
            R60Cell10.setCellStyle(numberStyle);
        } else {
            R60Cell10.setCellValue("");
            R60Cell10.setCellStyle(textStyle);
        }

        // R62
        row = sheet.getRow(61);
        Cell R62Cell1 = row.createCell(3);
        if (record.getR62_FIG_BAL_BWP1() != null) {
            R62Cell1.setCellValue(record.getR62_FIG_BAL_BWP1().doubleValue());
            R62Cell1.setCellStyle(numberStyle);
        } else {
            R62Cell1.setCellValue("");
            R62Cell1.setCellStyle(textStyle);
        }
        Cell R62Cell2 = row.createCell(4);
        if (record.getR62_FIG_BAL_BWP2() != null) {
            R62Cell2.setCellValue(record.getR62_FIG_BAL_BWP2().doubleValue());
            R62Cell2.setCellStyle(numberStyle);
        } else {
            R62Cell2.setCellValue("");
            R62Cell2.setCellStyle(textStyle);
        }
        Cell R62Cell3 = row.createCell(5);
        if (record.getR62_AMT_ADJ_BWP1() != null) {
            R62Cell3.setCellValue(record.getR62_AMT_ADJ_BWP1().doubleValue());
            R62Cell3.setCellStyle(numberStyle);
        } else {
            R62Cell3.setCellValue("");
            R62Cell3.setCellStyle(textStyle);
        }
        Cell R62Cell4 = row.createCell(6);
        if (record.getR62_AMT_ADJ_BWP2() != null) {
            R62Cell4.setCellValue(record.getR62_AMT_ADJ_BWP2().doubleValue());
            R62Cell4.setCellStyle(numberStyle);
        } else {
            R62Cell4.setCellValue("");
            R62Cell4.setCellStyle(textStyle);
        }
        Cell R62Cell5 = row.createCell(7);
        if (record.getR62_NET_AMT_BWP1() != null) {
            R62Cell5.setCellValue(record.getR62_NET_AMT_BWP1().doubleValue());
            R62Cell5.setCellStyle(numberStyle);
        } else {
            R62Cell5.setCellValue("");
            R62Cell5.setCellStyle(textStyle);
        }
        Cell R62Cell6 = row.createCell(8);
        if (record.getR62_NET_AMT_BWP2() != null) {
            R62Cell6.setCellValue(record.getR62_NET_AMT_BWP2().doubleValue());
            R62Cell6.setCellStyle(numberStyle);
        } else {
            R62Cell6.setCellValue("");
            R62Cell6.setCellStyle(textStyle);
        }
        Cell R62Cell7 = row.createCell(9);
        if (record.getR62_BAL_SUB_BWP1() != null) {
            R62Cell7.setCellValue(record.getR62_BAL_SUB_BWP1().doubleValue());
            R62Cell7.setCellStyle(numberStyle);
        } else {
            R62Cell7.setCellValue("");
            R62Cell7.setCellStyle(textStyle);
        }
        Cell R62Cell8 = row.createCell(10);
        if (record.getR62_BAL_SUB_BWP2() != null) {
            R62Cell8.setCellValue(record.getR62_BAL_SUB_BWP2().doubleValue());
            R62Cell8.setCellStyle(numberStyle);
        } else {
            R62Cell8.setCellValue("");
            R62Cell8.setCellStyle(textStyle);
        }
        Cell R62Cell9 = row.createCell(11);
        if (record.getR62_BAL_ACT_SUB_BWP1() != null) {
            R62Cell9.setCellValue(record.getR62_BAL_ACT_SUB_BWP1().doubleValue());
            R62Cell9.setCellStyle(numberStyle);
        } else {
            R62Cell9.setCellValue("");
            R62Cell9.setCellStyle(textStyle);
        }
        Cell R62Cell10 = row.createCell(12);
        if (record.getR62_BAL_ACT_SUB_BWP2() != null) {
            R62Cell10.setCellValue(record.getR62_BAL_ACT_SUB_BWP2().doubleValue());
            R62Cell10.setCellStyle(numberStyle);
        } else {
            R62Cell10.setCellValue("");
            R62Cell10.setCellStyle(textStyle);
        }

        // R63
        row = sheet.getRow(62);
        Cell R63Cell1 = row.createCell(3);
        if (record.getR63_FIG_BAL_BWP1() != null) {
            R63Cell1.setCellValue(record.getR63_FIG_BAL_BWP1().doubleValue());
            R63Cell1.setCellStyle(numberStyle);
        } else {
            R63Cell1.setCellValue("");
            R63Cell1.setCellStyle(textStyle);
        }
        Cell R63Cell2 = row.createCell(4);
        if (record.getR63_FIG_BAL_BWP2() != null) {
            R63Cell2.setCellValue(record.getR63_FIG_BAL_BWP2().doubleValue());
            R63Cell2.setCellStyle(numberStyle);
        } else {
            R63Cell2.setCellValue("");
            R63Cell2.setCellStyle(textStyle);
        }
        Cell R63Cell3 = row.createCell(5);
        if (record.getR63_AMT_ADJ_BWP1() != null) {
            R63Cell3.setCellValue(record.getR63_AMT_ADJ_BWP1().doubleValue());
            R63Cell3.setCellStyle(numberStyle);
        } else {
            R63Cell3.setCellValue("");
            R63Cell3.setCellStyle(textStyle);
        }
        Cell R63Cell4 = row.createCell(6);
        if (record.getR63_AMT_ADJ_BWP2() != null) {
            R63Cell4.setCellValue(record.getR63_AMT_ADJ_BWP2().doubleValue());
            R63Cell4.setCellStyle(numberStyle);
        } else {
            R63Cell4.setCellValue("");
            R63Cell4.setCellStyle(textStyle);
        }
        Cell R63Cell5 = row.createCell(7);
        if (record.getR63_NET_AMT_BWP1() != null) {
            R63Cell5.setCellValue(record.getR63_NET_AMT_BWP1().doubleValue());
            R63Cell5.setCellStyle(numberStyle);
        } else {
            R63Cell5.setCellValue("");
            R63Cell5.setCellStyle(textStyle);
        }
        Cell R63Cell6 = row.createCell(8);
        if (record.getR63_NET_AMT_BWP2() != null) {
            R63Cell6.setCellValue(record.getR63_NET_AMT_BWP2().doubleValue());
            R63Cell6.setCellStyle(numberStyle);
        } else {
            R63Cell6.setCellValue("");
            R63Cell6.setCellStyle(textStyle);
        }
        Cell R63Cell7 = row.createCell(9);
        if (record.getR63_BAL_SUB_BWP1() != null) {
            R63Cell7.setCellValue(record.getR63_BAL_SUB_BWP1().doubleValue());
            R63Cell7.setCellStyle(numberStyle);
        } else {
            R63Cell7.setCellValue("");
            R63Cell7.setCellStyle(textStyle);
        }
        Cell R63Cell8 = row.createCell(10);
        if (record.getR63_BAL_SUB_BWP2() != null) {
            R63Cell8.setCellValue(record.getR63_BAL_SUB_BWP2().doubleValue());
            R63Cell8.setCellStyle(numberStyle);
        } else {
            R63Cell8.setCellValue("");
            R63Cell8.setCellStyle(textStyle);
        }
        Cell R63Cell9 = row.createCell(11);
        if (record.getR63_BAL_ACT_SUB_BWP1() != null) {
            R63Cell9.setCellValue(record.getR63_BAL_ACT_SUB_BWP1().doubleValue());
            R63Cell9.setCellStyle(numberStyle);
        } else {
            R63Cell9.setCellValue("");
            R63Cell9.setCellStyle(textStyle);
        }
        Cell R63Cell10 = row.createCell(12);
        if (record.getR63_BAL_ACT_SUB_BWP2() != null) {
            R63Cell10.setCellValue(record.getR63_BAL_ACT_SUB_BWP2().doubleValue());
            R63Cell10.setCellStyle(numberStyle);
        } else {
            R63Cell10.setCellValue("");
            R63Cell10.setCellStyle(textStyle);
        }

        // R64
        row = sheet.getRow(63);
        Cell R64Cell1 = row.createCell(3);
        if (record.getR64_FIG_BAL_BWP1() != null) {
            R64Cell1.setCellValue(record.getR64_FIG_BAL_BWP1().doubleValue());
            R64Cell1.setCellStyle(numberStyle);
        } else {
            R64Cell1.setCellValue("");
            R64Cell1.setCellStyle(textStyle);
        }
        Cell R64Cell2 = row.createCell(4);
        if (record.getR64_FIG_BAL_BWP2() != null) {
            R64Cell2.setCellValue(record.getR64_FIG_BAL_BWP2().doubleValue());
            R64Cell2.setCellStyle(numberStyle);
        } else {
            R64Cell2.setCellValue("");
            R64Cell2.setCellStyle(textStyle);
        }
        Cell R64Cell3 = row.createCell(5);
        if (record.getR64_AMT_ADJ_BWP1() != null) {
            R64Cell3.setCellValue(record.getR64_AMT_ADJ_BWP1().doubleValue());
            R64Cell3.setCellStyle(numberStyle);
        } else {
            R64Cell3.setCellValue("");
            R64Cell3.setCellStyle(textStyle);
        }
        Cell R64Cell4 = row.createCell(6);
        if (record.getR64_AMT_ADJ_BWP2() != null) {
            R64Cell4.setCellValue(record.getR64_AMT_ADJ_BWP2().doubleValue());
            R64Cell4.setCellStyle(numberStyle);
        } else {
            R64Cell4.setCellValue("");
            R64Cell4.setCellStyle(textStyle);
        }
        Cell R64Cell5 = row.createCell(7);
        if (record.getR64_NET_AMT_BWP1() != null) {
            R64Cell5.setCellValue(record.getR64_NET_AMT_BWP1().doubleValue());
            R64Cell5.setCellStyle(numberStyle);
        } else {
            R64Cell5.setCellValue("");
            R64Cell5.setCellStyle(textStyle);
        }
        Cell R64Cell6 = row.createCell(8);
        if (record.getR64_NET_AMT_BWP2() != null) {
            R64Cell6.setCellValue(record.getR64_NET_AMT_BWP2().doubleValue());
            R64Cell6.setCellStyle(numberStyle);
        } else {
            R64Cell6.setCellValue("");
            R64Cell6.setCellStyle(textStyle);
        }
        Cell R64Cell7 = row.createCell(9);
        if (record.getR64_BAL_SUB_BWP1() != null) {
            R64Cell7.setCellValue(record.getR64_BAL_SUB_BWP1().doubleValue());
            R64Cell7.setCellStyle(numberStyle);
        } else {
            R64Cell7.setCellValue("");
            R64Cell7.setCellStyle(textStyle);
        }
        Cell R64Cell8 = row.createCell(10);
        if (record.getR64_BAL_SUB_BWP2() != null) {
            R64Cell8.setCellValue(record.getR64_BAL_SUB_BWP2().doubleValue());
            R64Cell8.setCellStyle(numberStyle);
        } else {
            R64Cell8.setCellValue("");
            R64Cell8.setCellStyle(textStyle);
        }
        Cell R64Cell9 = row.createCell(11);
        if (record.getR64_BAL_ACT_SUB_BWP1() != null) {
            R64Cell9.setCellValue(record.getR64_BAL_ACT_SUB_BWP1().doubleValue());
            R64Cell9.setCellStyle(numberStyle);
        } else {
            R64Cell9.setCellValue("");
            R64Cell9.setCellStyle(textStyle);
        }
        Cell R64Cell10 = row.createCell(12);
        if (record.getR64_BAL_ACT_SUB_BWP2() != null) {
            R64Cell10.setCellValue(record.getR64_BAL_ACT_SUB_BWP2().doubleValue());
            R64Cell10.setCellStyle(numberStyle);
        } else {
            R64Cell10.setCellValue("");
            R64Cell10.setCellStyle(textStyle);
        }

        // R65
        row = sheet.getRow(64);
        Cell R65Cell1 = row.createCell(3);
        if (record.getR65_FIG_BAL_BWP1() != null) {
            R65Cell1.setCellValue(record.getR65_FIG_BAL_BWP1().doubleValue());
            R65Cell1.setCellStyle(numberStyle);
        } else {
            R65Cell1.setCellValue("");
            R65Cell1.setCellStyle(textStyle);
        }
        Cell R65Cell2 = row.createCell(4);
        if (record.getR65_FIG_BAL_BWP2() != null) {
            R65Cell2.setCellValue(record.getR65_FIG_BAL_BWP2().doubleValue());
            R65Cell2.setCellStyle(numberStyle);
        } else {
            R65Cell2.setCellValue("");
            R65Cell2.setCellStyle(textStyle);
        }
        Cell R65Cell3 = row.createCell(5);
        if (record.getR65_AMT_ADJ_BWP1() != null) {
            R65Cell3.setCellValue(record.getR65_AMT_ADJ_BWP1().doubleValue());
            R65Cell3.setCellStyle(numberStyle);
        } else {
            R65Cell3.setCellValue("");
            R65Cell3.setCellStyle(textStyle);
        }
        Cell R65Cell4 = row.createCell(6);
        if (record.getR65_AMT_ADJ_BWP2() != null) {
            R65Cell4.setCellValue(record.getR65_AMT_ADJ_BWP2().doubleValue());
            R65Cell4.setCellStyle(numberStyle);
        } else {
            R65Cell4.setCellValue("");
            R65Cell4.setCellStyle(textStyle);
        }
        Cell R65Cell5 = row.createCell(7);
        if (record.getR65_NET_AMT_BWP1() != null) {
            R65Cell5.setCellValue(record.getR65_NET_AMT_BWP1().doubleValue());
            R65Cell5.setCellStyle(numberStyle);
        } else {
            R65Cell5.setCellValue("");
            R65Cell5.setCellStyle(textStyle);
        }
        Cell R65Cell6 = row.createCell(8);
        if (record.getR65_NET_AMT_BWP2() != null) {
            R65Cell6.setCellValue(record.getR65_NET_AMT_BWP2().doubleValue());
            R65Cell6.setCellStyle(numberStyle);
        } else {
            R65Cell6.setCellValue("");
            R65Cell6.setCellStyle(textStyle);
        }
        Cell R65Cell7 = row.createCell(9);
        if (record.getR65_BAL_SUB_BWP1() != null) {
            R65Cell7.setCellValue(record.getR65_BAL_SUB_BWP1().doubleValue());
            R65Cell7.setCellStyle(numberStyle);
        } else {
            R65Cell7.setCellValue("");
            R65Cell7.setCellStyle(textStyle);
        }
        Cell R65Cell8 = row.createCell(10);
        if (record.getR65_BAL_SUB_BWP2() != null) {
            R65Cell8.setCellValue(record.getR65_BAL_SUB_BWP2().doubleValue());
            R65Cell8.setCellStyle(numberStyle);
        } else {
            R65Cell8.setCellValue("");
            R65Cell8.setCellStyle(textStyle);
        }
        Cell R65Cell9 = row.createCell(11);
        if (record.getR65_BAL_ACT_SUB_BWP1() != null) {
            R65Cell9.setCellValue(record.getR65_BAL_ACT_SUB_BWP1().doubleValue());
            R65Cell9.setCellStyle(numberStyle);
        } else {
            R65Cell9.setCellValue("");
            R65Cell9.setCellStyle(textStyle);
        }
        Cell R65Cell10 = row.createCell(12);
        if (record.getR65_BAL_ACT_SUB_BWP2() != null) {
            R65Cell10.setCellValue(record.getR65_BAL_ACT_SUB_BWP2().doubleValue());
            R65Cell10.setCellStyle(numberStyle);
        } else {
            R65Cell10.setCellValue("");
            R65Cell10.setCellStyle(textStyle);
        }
        /* ================= R66 ================= */
        row = sheet.getRow(65);
        Cell R66Cell1 = row.createCell(3);
        if (record.getR66_FIG_BAL_BWP1() != null) {
            R66Cell1.setCellValue(record.getR66_FIG_BAL_BWP1().doubleValue());
            R66Cell1.setCellStyle(numberStyle);
        } else {
            R66Cell1.setCellValue("");
            R66Cell1.setCellStyle(textStyle);
        }
        Cell R66Cell2 = row.createCell(4);
        if (record.getR66_FIG_BAL_BWP2() != null) {
            R66Cell2.setCellValue(record.getR66_FIG_BAL_BWP2().doubleValue());
            R66Cell2.setCellStyle(numberStyle);
        } else {
            R66Cell2.setCellValue("");
            R66Cell2.setCellStyle(textStyle);
        }
        Cell R66Cell3 = row.createCell(5);
        if (record.getR66_AMT_ADJ_BWP1() != null) {
            R66Cell3.setCellValue(record.getR66_AMT_ADJ_BWP1().doubleValue());
            R66Cell3.setCellStyle(numberStyle);
        } else {
            R66Cell3.setCellValue("");
            R66Cell3.setCellStyle(textStyle);
        }
        Cell R66Cell4 = row.createCell(6);
        if (record.getR66_AMT_ADJ_BWP2() != null) {
            R66Cell4.setCellValue(record.getR66_AMT_ADJ_BWP2().doubleValue());
            R66Cell4.setCellStyle(numberStyle);
        } else {
            R66Cell4.setCellValue("");
            R66Cell4.setCellStyle(textStyle);
        }
        Cell R66Cell5 = row.createCell(7);
        if (record.getR66_NET_AMT_BWP1() != null) {
            R66Cell5.setCellValue(record.getR66_NET_AMT_BWP1().doubleValue());
            R66Cell5.setCellStyle(numberStyle);
        } else {
            R66Cell5.setCellValue("");
            R66Cell5.setCellStyle(textStyle);
        }
        Cell R66Cell6 = row.createCell(8);
        if (record.getR66_NET_AMT_BWP2() != null) {
            R66Cell6.setCellValue(record.getR66_NET_AMT_BWP2().doubleValue());
            R66Cell6.setCellStyle(numberStyle);
        } else {
            R66Cell6.setCellValue("");
            R66Cell6.setCellStyle(textStyle);
        }
        Cell R66Cell7 = row.createCell(9);
        if (record.getR66_BAL_SUB_BWP1() != null) {
            R66Cell7.setCellValue(record.getR66_BAL_SUB_BWP1().doubleValue());
            R66Cell7.setCellStyle(numberStyle);
        } else {
            R66Cell7.setCellValue("");
            R66Cell7.setCellStyle(textStyle);
        }
        Cell R66Cell8 = row.createCell(10);
        if (record.getR66_BAL_SUB_BWP2() != null) {
            R66Cell8.setCellValue(record.getR66_BAL_SUB_BWP2().doubleValue());
            R66Cell8.setCellStyle(numberStyle);
        } else {
            R66Cell8.setCellValue("");
            R66Cell8.setCellStyle(textStyle);
        }
        Cell R66Cell9 = row.createCell(11);
        if (record.getR66_BAL_ACT_SUB_BWP1() != null) {
            R66Cell9.setCellValue(record.getR66_BAL_ACT_SUB_BWP1().doubleValue());
            R66Cell9.setCellStyle(numberStyle);
        } else {
            R66Cell9.setCellValue("");
            R66Cell9.setCellStyle(textStyle);
        }
        Cell R66Cell10 = row.createCell(12);
        if (record.getR66_BAL_ACT_SUB_BWP2() != null) {
            R66Cell10.setCellValue(record.getR66_BAL_ACT_SUB_BWP2().doubleValue());
            R66Cell10.setCellStyle(numberStyle);
        } else {
            R66Cell10.setCellValue("");
            R66Cell10.setCellStyle(textStyle);
        }

        /* ================= R71 ================= */
        row = sheet.getRow(70);
        Cell R71Cell1 = row.createCell(3);
        if (record.getR71_FIG_BAL_BWP1() != null) {
            R71Cell1.setCellValue(record.getR71_FIG_BAL_BWP1().doubleValue());
            R71Cell1.setCellStyle(numberStyle);
        } else {
            R71Cell1.setCellValue("");
            R71Cell1.setCellStyle(textStyle);
        }
        Cell R71Cell2 = row.createCell(4);
        if (record.getR71_FIG_BAL_BWP2() != null) {
            R71Cell2.setCellValue(record.getR71_FIG_BAL_BWP2().doubleValue());
            R71Cell2.setCellStyle(numberStyle);
        } else {
            R71Cell2.setCellValue("");
            R71Cell2.setCellStyle(textStyle);
        }
        Cell R71Cell3 = row.createCell(5);
        if (record.getR71_AMT_ADJ_BWP1() != null) {
            R71Cell3.setCellValue(record.getR71_AMT_ADJ_BWP1().doubleValue());
            R71Cell3.setCellStyle(numberStyle);
        } else {
            R71Cell3.setCellValue("");
            R71Cell3.setCellStyle(textStyle);
        }
        Cell R71Cell4 = row.createCell(6);
        if (record.getR71_AMT_ADJ_BWP2() != null) {
            R71Cell4.setCellValue(record.getR71_AMT_ADJ_BWP2().doubleValue());
            R71Cell4.setCellStyle(numberStyle);
        } else {
            R71Cell4.setCellValue("");
            R71Cell4.setCellStyle(textStyle);
        }
        Cell R71Cell5 = row.createCell(7);
        if (record.getR71_NET_AMT_BWP1() != null) {
            R71Cell5.setCellValue(record.getR71_NET_AMT_BWP1().doubleValue());
            R71Cell5.setCellStyle(numberStyle);
        } else {
            R71Cell5.setCellValue("");
            R71Cell5.setCellStyle(textStyle);
        }
        Cell R71Cell6 = row.createCell(8);
        if (record.getR71_NET_AMT_BWP2() != null) {
            R71Cell6.setCellValue(record.getR71_NET_AMT_BWP2().doubleValue());
            R71Cell6.setCellStyle(numberStyle);
        } else {
            R71Cell6.setCellValue("");
            R71Cell6.setCellStyle(textStyle);
        }
        Cell R71Cell7 = row.createCell(9);
        if (record.getR71_BAL_SUB_BWP1() != null) {
            R71Cell7.setCellValue(record.getR71_BAL_SUB_BWP1().doubleValue());
            R71Cell7.setCellStyle(numberStyle);
        } else {
            R71Cell7.setCellValue("");
            R71Cell7.setCellStyle(textStyle);
        }
        Cell R71Cell8 = row.createCell(10);
        if (record.getR71_BAL_SUB_BWP2() != null) {
            R71Cell8.setCellValue(record.getR71_BAL_SUB_BWP2().doubleValue());
            R71Cell8.setCellStyle(numberStyle);
        } else {
            R71Cell8.setCellValue("");
            R71Cell8.setCellStyle(textStyle);
        }
        Cell R71Cell9 = row.createCell(11);
        if (record.getR71_BAL_ACT_SUB_BWP1() != null) {
            R71Cell9.setCellValue(record.getR71_BAL_ACT_SUB_BWP1().doubleValue());
            R71Cell9.setCellStyle(numberStyle);
        } else {
            R71Cell9.setCellValue("");
            R71Cell9.setCellStyle(textStyle);
        }
        Cell R71Cell10 = row.createCell(12);
        if (record.getR71_BAL_ACT_SUB_BWP2() != null) {
            R71Cell10.setCellValue(record.getR71_BAL_ACT_SUB_BWP2().doubleValue());
            R71Cell10.setCellStyle(numberStyle);
        } else {
            R71Cell10.setCellValue("");
            R71Cell10.setCellStyle(textStyle);
        }

        /* ================= R72 ================= */
        row = sheet.getRow(71);
        Cell R72Cell1 = row.createCell(3);
        if (record.getR72_FIG_BAL_BWP1() != null) {
            R72Cell1.setCellValue(record.getR72_FIG_BAL_BWP1().doubleValue());
            R72Cell1.setCellStyle(numberStyle);
        } else {
            R72Cell1.setCellValue("");
            R72Cell1.setCellStyle(textStyle);
        }
        Cell R72Cell2 = row.createCell(4);
        if (record.getR72_FIG_BAL_BWP2() != null) {
            R72Cell2.setCellValue(record.getR72_FIG_BAL_BWP2().doubleValue());
            R72Cell2.setCellStyle(numberStyle);
        } else {
            R72Cell2.setCellValue("");
            R72Cell2.setCellStyle(textStyle);
        }
        Cell R72Cell3 = row.createCell(5);
        if (record.getR72_AMT_ADJ_BWP1() != null) {
            R72Cell3.setCellValue(record.getR72_AMT_ADJ_BWP1().doubleValue());
            R72Cell3.setCellStyle(numberStyle);
        } else {
            R72Cell3.setCellValue("");
            R72Cell3.setCellStyle(textStyle);
        }
        Cell R72Cell4 = row.createCell(6);
        if (record.getR72_AMT_ADJ_BWP2() != null) {
            R72Cell4.setCellValue(record.getR72_AMT_ADJ_BWP2().doubleValue());
            R72Cell4.setCellStyle(numberStyle);
        } else {
            R72Cell4.setCellValue("");
            R72Cell4.setCellStyle(textStyle);
        }
        Cell R72Cell5 = row.createCell(7);
        if (record.getR72_NET_AMT_BWP1() != null) {
            R72Cell5.setCellValue(record.getR72_NET_AMT_BWP1().doubleValue());
            R72Cell5.setCellStyle(numberStyle);
        } else {
            R72Cell5.setCellValue("");
            R72Cell5.setCellStyle(textStyle);
        }
        Cell R72Cell6 = row.createCell(8);
        if (record.getR72_NET_AMT_BWP2() != null) {
            R72Cell6.setCellValue(record.getR72_NET_AMT_BWP2().doubleValue());
            R72Cell6.setCellStyle(numberStyle);
        } else {
            R72Cell6.setCellValue("");
            R72Cell6.setCellStyle(textStyle);
        }
        Cell R72Cell7 = row.createCell(9);
        if (record.getR72_BAL_SUB_BWP1() != null) {
            R72Cell7.setCellValue(record.getR72_BAL_SUB_BWP1().doubleValue());
            R72Cell7.setCellStyle(numberStyle);
        } else {
            R72Cell7.setCellValue("");
            R72Cell7.setCellStyle(textStyle);
        }
        Cell R72Cell8 = row.createCell(10);
        if (record.getR72_BAL_SUB_BWP2() != null) {
            R72Cell8.setCellValue(record.getR72_BAL_SUB_BWP2().doubleValue());
            R72Cell8.setCellStyle(numberStyle);
        } else {
            R72Cell8.setCellValue("");
            R72Cell8.setCellStyle(textStyle);
        }
        Cell R72Cell9 = row.createCell(11);
        if (record.getR72_BAL_ACT_SUB_BWP1() != null) {
            R72Cell9.setCellValue(record.getR72_BAL_ACT_SUB_BWP1().doubleValue());
            R72Cell9.setCellStyle(numberStyle);
        } else {
            R72Cell9.setCellValue("");
            R72Cell9.setCellStyle(textStyle);
        }
        Cell R72Cell10 = row.createCell(12);
        if (record.getR72_BAL_ACT_SUB_BWP2() != null) {
            R72Cell10.setCellValue(record.getR72_BAL_ACT_SUB_BWP2().doubleValue());
            R72Cell10.setCellStyle(numberStyle);
        } else {
            R72Cell10.setCellValue("");
            R72Cell10.setCellStyle(textStyle);
        }

        /* ================= R73 ================= */
        row = sheet.getRow(72);
        Cell R73Cell1 = row.createCell(3);
        if (record.getR73_FIG_BAL_BWP1() != null) {
            R73Cell1.setCellValue(record.getR73_FIG_BAL_BWP1().doubleValue());
            R73Cell1.setCellStyle(numberStyle);
        } else {
            R73Cell1.setCellValue("");
            R73Cell1.setCellStyle(textStyle);
        }
        Cell R73Cell2 = row.createCell(4);
        if (record.getR73_FIG_BAL_BWP2() != null) {
            R73Cell2.setCellValue(record.getR73_FIG_BAL_BWP2().doubleValue());
            R73Cell2.setCellStyle(numberStyle);
        } else {
            R73Cell2.setCellValue("");
            R73Cell2.setCellStyle(textStyle);
        }
        Cell R73Cell3 = row.createCell(5);
        if (record.getR73_AMT_ADJ_BWP1() != null) {
            R73Cell3.setCellValue(record.getR73_AMT_ADJ_BWP1().doubleValue());
            R73Cell3.setCellStyle(numberStyle);
        } else {
            R73Cell3.setCellValue("");
            R73Cell3.setCellStyle(textStyle);
        }
        Cell R73Cell4 = row.createCell(6);
        if (record.getR73_AMT_ADJ_BWP2() != null) {
            R73Cell4.setCellValue(record.getR73_AMT_ADJ_BWP2().doubleValue());
            R73Cell4.setCellStyle(numberStyle);
        } else {
            R73Cell4.setCellValue("");
            R73Cell4.setCellStyle(textStyle);
        }
        Cell R73Cell5 = row.createCell(7);
        if (record.getR73_NET_AMT_BWP1() != null) {
            R73Cell5.setCellValue(record.getR73_NET_AMT_BWP1().doubleValue());
            R73Cell5.setCellStyle(numberStyle);
        } else {
            R73Cell5.setCellValue("");
            R73Cell5.setCellStyle(textStyle);
        }
        Cell R73Cell6 = row.createCell(8);
        if (record.getR73_NET_AMT_BWP2() != null) {
            R73Cell6.setCellValue(record.getR73_NET_AMT_BWP2().doubleValue());
            R73Cell6.setCellStyle(numberStyle);
        } else {
            R73Cell6.setCellValue("");
            R73Cell6.setCellStyle(textStyle);
        }
        Cell R73Cell7 = row.createCell(9);
        if (record.getR73_BAL_SUB_BWP1() != null) {
            R73Cell7.setCellValue(record.getR73_BAL_SUB_BWP1().doubleValue());
            R73Cell7.setCellStyle(numberStyle);
        } else {
            R73Cell7.setCellValue("");
            R73Cell7.setCellStyle(textStyle);
        }
        Cell R73Cell8 = row.createCell(10);
        if (record.getR73_BAL_SUB_BWP2() != null) {
            R73Cell8.setCellValue(record.getR73_BAL_SUB_BWP2().doubleValue());
            R73Cell8.setCellStyle(numberStyle);
        } else {
            R73Cell8.setCellValue("");
            R73Cell8.setCellStyle(textStyle);
        }
        Cell R73Cell9 = row.createCell(11);
        if (record.getR73_BAL_ACT_SUB_BWP1() != null) {
            R73Cell9.setCellValue(record.getR73_BAL_ACT_SUB_BWP1().doubleValue());
            R73Cell9.setCellStyle(numberStyle);
        } else {
            R73Cell9.setCellValue("");
            R73Cell9.setCellStyle(textStyle);
        }
        Cell R73Cell10 = row.createCell(12);
        if (record.getR73_BAL_ACT_SUB_BWP2() != null) {
            R73Cell10.setCellValue(record.getR73_BAL_ACT_SUB_BWP2().doubleValue());
            R73Cell10.setCellStyle(numberStyle);
        } else {
            R73Cell10.setCellValue("");
            R73Cell10.setCellStyle(textStyle);
        }

        /* ================= R74 ================= */
        row = sheet.getRow(73);
        Cell R74Cell1 = row.createCell(3);
        if (record.getR74_FIG_BAL_BWP1() != null) {
            R74Cell1.setCellValue(record.getR74_FIG_BAL_BWP1().doubleValue());
            R74Cell1.setCellStyle(numberStyle);
        } else {
            R74Cell1.setCellValue("");
            R74Cell1.setCellStyle(textStyle);
        }
        Cell R74Cell2 = row.createCell(4);
        if (record.getR74_FIG_BAL_BWP2() != null) {
            R74Cell2.setCellValue(record.getR74_FIG_BAL_BWP2().doubleValue());
            R74Cell2.setCellStyle(numberStyle);
        } else {
            R74Cell2.setCellValue("");
            R74Cell2.setCellStyle(textStyle);
        }
        Cell R74Cell3 = row.createCell(5);
        if (record.getR74_AMT_ADJ_BWP1() != null) {
            R74Cell3.setCellValue(record.getR74_AMT_ADJ_BWP1().doubleValue());
            R74Cell3.setCellStyle(numberStyle);
        } else {
            R74Cell3.setCellValue("");
            R74Cell3.setCellStyle(textStyle);
        }
        Cell R74Cell4 = row.createCell(6);
        if (record.getR74_AMT_ADJ_BWP2() != null) {
            R74Cell4.setCellValue(record.getR74_AMT_ADJ_BWP2().doubleValue());
            R74Cell4.setCellStyle(numberStyle);
        } else {
            R74Cell4.setCellValue("");
            R74Cell4.setCellStyle(textStyle);
        }
        Cell R74Cell5 = row.createCell(7);
        if (record.getR74_NET_AMT_BWP1() != null) {
            R74Cell5.setCellValue(record.getR74_NET_AMT_BWP1().doubleValue());
            R74Cell5.setCellStyle(numberStyle);
        } else {
            R74Cell5.setCellValue("");
            R74Cell5.setCellStyle(textStyle);
        }
        Cell R74Cell6 = row.createCell(8);
        if (record.getR74_NET_AMT_BWP2() != null) {
            R74Cell6.setCellValue(record.getR74_NET_AMT_BWP2().doubleValue());
            R74Cell6.setCellStyle(numberStyle);
        } else {
            R74Cell6.setCellValue("");
            R74Cell6.setCellStyle(textStyle);
        }
        Cell R74Cell7 = row.createCell(9);
        if (record.getR74_BAL_SUB_BWP1() != null) {
            R74Cell7.setCellValue(record.getR74_BAL_SUB_BWP1().doubleValue());
            R74Cell7.setCellStyle(numberStyle);
        } else {
            R74Cell7.setCellValue("");
            R74Cell7.setCellStyle(textStyle);
        }
        Cell R74Cell8 = row.createCell(10);
        if (record.getR74_BAL_SUB_BWP2() != null) {
            R74Cell8.setCellValue(record.getR74_BAL_SUB_BWP2().doubleValue());
            R74Cell8.setCellStyle(numberStyle);
        } else {
            R74Cell8.setCellValue("");
            R74Cell8.setCellStyle(textStyle);
        }
        Cell R74Cell9 = row.createCell(11);
        if (record.getR74_BAL_ACT_SUB_BWP1() != null) {
            R74Cell9.setCellValue(record.getR74_BAL_ACT_SUB_BWP1().doubleValue());
            R74Cell9.setCellStyle(numberStyle);
        } else {
            R74Cell9.setCellValue("");
            R74Cell9.setCellStyle(textStyle);
        }
        Cell R74Cell10 = row.createCell(12);
        if (record.getR74_BAL_ACT_SUB_BWP2() != null) {
            R74Cell10.setCellValue(record.getR74_BAL_ACT_SUB_BWP2().doubleValue());
            R74Cell10.setCellStyle(numberStyle);
        } else {
            R74Cell10.setCellValue("");
            R74Cell10.setCellStyle(textStyle);
        }

        /* ================= R75 ================= */
        row = sheet.getRow(74);
        Cell R75Cell1 = row.createCell(3);
        if (record.getR75_FIG_BAL_BWP1() != null) {
            R75Cell1.setCellValue(record.getR75_FIG_BAL_BWP1().doubleValue());
            R75Cell1.setCellStyle(numberStyle);
        } else {
            R75Cell1.setCellValue("");
            R75Cell1.setCellStyle(textStyle);
        }
        Cell R75Cell2 = row.createCell(4);
        if (record.getR75_FIG_BAL_BWP2() != null) {
            R75Cell2.setCellValue(record.getR75_FIG_BAL_BWP2().doubleValue());
            R75Cell2.setCellStyle(numberStyle);
        } else {
            R75Cell2.setCellValue("");
            R75Cell2.setCellStyle(textStyle);
        }
        Cell R75Cell3 = row.createCell(5);
        if (record.getR75_AMT_ADJ_BWP1() != null) {
            R75Cell3.setCellValue(record.getR75_AMT_ADJ_BWP1().doubleValue());
            R75Cell3.setCellStyle(numberStyle);
        } else {
            R75Cell3.setCellValue("");
            R75Cell3.setCellStyle(textStyle);
        }
        Cell R75Cell4 = row.createCell(6);
        if (record.getR75_AMT_ADJ_BWP2() != null) {
            R75Cell4.setCellValue(record.getR75_AMT_ADJ_BWP2().doubleValue());
            R75Cell4.setCellStyle(numberStyle);
        } else {
            R75Cell4.setCellValue("");
            R75Cell4.setCellStyle(textStyle);
        }
        Cell R75Cell5 = row.createCell(7);
        if (record.getR75_NET_AMT_BWP1() != null) {
            R75Cell5.setCellValue(record.getR75_NET_AMT_BWP1().doubleValue());
            R75Cell5.setCellStyle(numberStyle);
        } else {
            R75Cell5.setCellValue("");
            R75Cell5.setCellStyle(textStyle);
        }
        Cell R75Cell6 = row.createCell(8);
        if (record.getR75_NET_AMT_BWP2() != null) {
            R75Cell6.setCellValue(record.getR75_NET_AMT_BWP2().doubleValue());
            R75Cell6.setCellStyle(numberStyle);
        } else {
            R75Cell6.setCellValue("");
            R75Cell6.setCellStyle(textStyle);
        }
        Cell R75Cell7 = row.createCell(9);
        if (record.getR75_BAL_SUB_BWP1() != null) {
            R75Cell7.setCellValue(record.getR75_BAL_SUB_BWP1().doubleValue());
            R75Cell7.setCellStyle(numberStyle);
        } else {
            R75Cell7.setCellValue("");
            R75Cell7.setCellStyle(textStyle);
        }
        Cell R75Cell8 = row.createCell(10);
        if (record.getR75_BAL_SUB_BWP2() != null) {
            R75Cell8.setCellValue(record.getR75_BAL_SUB_BWP2().doubleValue());
            R75Cell8.setCellStyle(numberStyle);
        } else {
            R75Cell8.setCellValue("");
            R75Cell8.setCellStyle(textStyle);
        }
        Cell R75Cell9 = row.createCell(11);
        if (record.getR75_BAL_ACT_SUB_BWP1() != null) {
            R75Cell9.setCellValue(record.getR75_BAL_ACT_SUB_BWP1().doubleValue());
            R75Cell9.setCellStyle(numberStyle);
        } else {
            R75Cell9.setCellValue("");
            R75Cell9.setCellStyle(textStyle);
        }
        Cell R75Cell10 = row.createCell(12);
        if (record.getR75_BAL_ACT_SUB_BWP2() != null) {
            R75Cell10.setCellValue(record.getR75_BAL_ACT_SUB_BWP2().doubleValue());
            R75Cell10.setCellStyle(numberStyle);
        } else {
            R75Cell10.setCellValue("");
            R75Cell10.setCellStyle(textStyle);
        }

        /* ================= R76 ================= */
        row = sheet.getRow(75);
        Cell R76Cell1 = row.createCell(3);
        if (record.getR76_FIG_BAL_BWP1() != null) {
            R76Cell1.setCellValue(record.getR76_FIG_BAL_BWP1().doubleValue());
            R76Cell1.setCellStyle(numberStyle);
        } else {
            R76Cell1.setCellValue("");
            R76Cell1.setCellStyle(textStyle);
        }
        Cell R76Cell2 = row.createCell(4);
        if (record.getR76_FIG_BAL_BWP2() != null) {
            R76Cell2.setCellValue(record.getR76_FIG_BAL_BWP2().doubleValue());
            R76Cell2.setCellStyle(numberStyle);
        } else {
            R76Cell2.setCellValue("");
            R76Cell2.setCellStyle(textStyle);
        }
        Cell R76Cell3 = row.createCell(5);
        if (record.getR76_AMT_ADJ_BWP1() != null) {
            R76Cell3.setCellValue(record.getR76_AMT_ADJ_BWP1().doubleValue());
            R76Cell3.setCellStyle(numberStyle);
        } else {
            R76Cell3.setCellValue("");
            R76Cell3.setCellStyle(textStyle);
        }
        Cell R76Cell4 = row.createCell(6);
        if (record.getR76_AMT_ADJ_BWP2() != null) {
            R76Cell4.setCellValue(record.getR76_AMT_ADJ_BWP2().doubleValue());
            R76Cell4.setCellStyle(numberStyle);
        } else {
            R76Cell4.setCellValue("");
            R76Cell4.setCellStyle(textStyle);
        }
        Cell R76Cell5 = row.createCell(7);
        if (record.getR76_NET_AMT_BWP1() != null) {
            R76Cell5.setCellValue(record.getR76_NET_AMT_BWP1().doubleValue());
            R76Cell5.setCellStyle(numberStyle);
        } else {
            R76Cell5.setCellValue("");
            R76Cell5.setCellStyle(textStyle);
        }
        Cell R76Cell6 = row.createCell(8);
        if (record.getR76_NET_AMT_BWP2() != null) {
            R76Cell6.setCellValue(record.getR76_NET_AMT_BWP2().doubleValue());
            R76Cell6.setCellStyle(numberStyle);
        } else {
            R76Cell6.setCellValue("");
            R76Cell6.setCellStyle(textStyle);
        }
        Cell R76Cell7 = row.createCell(9);
        if (record.getR76_BAL_SUB_BWP1() != null) {
            R76Cell7.setCellValue(record.getR76_BAL_SUB_BWP1().doubleValue());
            R76Cell7.setCellStyle(numberStyle);
        } else {
            R76Cell7.setCellValue("");
            R76Cell7.setCellStyle(textStyle);
        }
        Cell R76Cell8 = row.createCell(10);
        if (record.getR76_BAL_SUB_BWP2() != null) {
            R76Cell8.setCellValue(record.getR76_BAL_SUB_BWP2().doubleValue());
            R76Cell8.setCellStyle(numberStyle);
        } else {
            R76Cell8.setCellValue("");
            R76Cell8.setCellStyle(textStyle);
        }
        Cell R76Cell9 = row.createCell(11);
        if (record.getR76_BAL_ACT_SUB_BWP1() != null) {
            R76Cell9.setCellValue(record.getR76_BAL_ACT_SUB_BWP1().doubleValue());
            R76Cell9.setCellStyle(numberStyle);
        } else {
            R76Cell9.setCellValue("");
            R76Cell9.setCellStyle(textStyle);
        }
        Cell R76Cell10 = row.createCell(12);
        if (record.getR76_BAL_ACT_SUB_BWP2() != null) {
            R76Cell10.setCellValue(record.getR76_BAL_ACT_SUB_BWP2().doubleValue());
            R76Cell10.setCellStyle(numberStyle);
        } else {
            R76Cell10.setCellValue("");
            R76Cell10.setCellStyle(textStyle);
        }

        /* ================= R77 ================= */
        row = sheet.getRow(76);
        Cell R77Cell1 = row.createCell(3);
        if (record.getR77_FIG_BAL_BWP1() != null) {
            R77Cell1.setCellValue(record.getR77_FIG_BAL_BWP1().doubleValue());
            R77Cell1.setCellStyle(numberStyle);
        } else {
            R77Cell1.setCellValue("");
            R77Cell1.setCellStyle(textStyle);
        }
        Cell R77Cell2 = row.createCell(4);
        if (record.getR77_FIG_BAL_BWP2() != null) {
            R77Cell2.setCellValue(record.getR77_FIG_BAL_BWP2().doubleValue());
            R77Cell2.setCellStyle(numberStyle);
        } else {
            R77Cell2.setCellValue("");
            R77Cell2.setCellStyle(textStyle);
        }
        Cell R77Cell3 = row.createCell(5);
        if (record.getR77_AMT_ADJ_BWP1() != null) {
            R77Cell3.setCellValue(record.getR77_AMT_ADJ_BWP1().doubleValue());
            R77Cell3.setCellStyle(numberStyle);
        } else {
            R77Cell3.setCellValue("");
            R77Cell3.setCellStyle(textStyle);
        }
        Cell R77Cell4 = row.createCell(6);
        if (record.getR77_AMT_ADJ_BWP2() != null) {
            R77Cell4.setCellValue(record.getR77_AMT_ADJ_BWP2().doubleValue());
            R77Cell4.setCellStyle(numberStyle);
        } else {
            R77Cell4.setCellValue("");
            R77Cell4.setCellStyle(textStyle);
        }
        Cell R77Cell5 = row.createCell(7);
        if (record.getR77_NET_AMT_BWP1() != null) {
            R77Cell5.setCellValue(record.getR77_NET_AMT_BWP1().doubleValue());
            R77Cell5.setCellStyle(numberStyle);
        } else {
            R77Cell5.setCellValue("");
            R77Cell5.setCellStyle(textStyle);
        }
        Cell R77Cell6 = row.createCell(8);
        if (record.getR77_NET_AMT_BWP2() != null) {
            R77Cell6.setCellValue(record.getR77_NET_AMT_BWP2().doubleValue());
            R77Cell6.setCellStyle(numberStyle);
        } else {
            R77Cell6.setCellValue("");
            R77Cell6.setCellStyle(textStyle);
        }
        Cell R77Cell7 = row.createCell(9);
        if (record.getR77_BAL_SUB_BWP1() != null) {
            R77Cell7.setCellValue(record.getR77_BAL_SUB_BWP1().doubleValue());
            R77Cell7.setCellStyle(numberStyle);
        } else {
            R77Cell7.setCellValue("");
            R77Cell7.setCellStyle(textStyle);
        }
        Cell R77Cell8 = row.createCell(10);
        if (record.getR77_BAL_SUB_BWP2() != null) {
            R77Cell8.setCellValue(record.getR77_BAL_SUB_BWP2().doubleValue());
            R77Cell8.setCellStyle(numberStyle);
        } else {
            R77Cell8.setCellValue("");
            R77Cell8.setCellStyle(textStyle);
        }
        Cell R77Cell9 = row.createCell(11);
        if (record.getR77_BAL_ACT_SUB_BWP1() != null) {
            R77Cell9.setCellValue(record.getR77_BAL_ACT_SUB_BWP1().doubleValue());
            R77Cell9.setCellStyle(numberStyle);
        } else {
            R77Cell9.setCellValue("");
            R77Cell9.setCellStyle(textStyle);
        }
        Cell R77Cell10 = row.createCell(12);
        if (record.getR77_BAL_ACT_SUB_BWP2() != null) {
            R77Cell10.setCellValue(record.getR77_BAL_ACT_SUB_BWP2().doubleValue());
            R77Cell10.setCellStyle(numberStyle);
        } else {
            R77Cell10.setCellValue("");
            R77Cell10.setCellStyle(textStyle);
        }

        /* ================= R78 ================= */
        row = sheet.getRow(77);
        Cell R78Cell1 = row.createCell(3);
        if (record.getR78_FIG_BAL_BWP1() != null) {
            R78Cell1.setCellValue(record.getR78_FIG_BAL_BWP1().doubleValue());
            R78Cell1.setCellStyle(numberStyle);
        } else {
            R78Cell1.setCellValue("");
            R78Cell1.setCellStyle(textStyle);
        }
        Cell R78Cell2 = row.createCell(4);
        if (record.getR78_FIG_BAL_BWP2() != null) {
            R78Cell2.setCellValue(record.getR78_FIG_BAL_BWP2().doubleValue());
            R78Cell2.setCellStyle(numberStyle);
        } else {
            R78Cell2.setCellValue("");
            R78Cell2.setCellStyle(textStyle);
        }
        Cell R78Cell3 = row.createCell(5);
        if (record.getR78_AMT_ADJ_BWP1() != null) {
            R78Cell3.setCellValue(record.getR78_AMT_ADJ_BWP1().doubleValue());
            R78Cell3.setCellStyle(numberStyle);
        } else {
            R78Cell3.setCellValue("");
            R78Cell3.setCellStyle(textStyle);
        }
        Cell R78Cell4 = row.createCell(6);
        if (record.getR78_AMT_ADJ_BWP2() != null) {
            R78Cell4.setCellValue(record.getR78_AMT_ADJ_BWP2().doubleValue());
            R78Cell4.setCellStyle(numberStyle);
        } else {
            R78Cell4.setCellValue("");
            R78Cell4.setCellStyle(textStyle);
        }
        Cell R78Cell5 = row.createCell(7);
        if (record.getR78_NET_AMT_BWP1() != null) {
            R78Cell5.setCellValue(record.getR78_NET_AMT_BWP1().doubleValue());
            R78Cell5.setCellStyle(numberStyle);
        } else {
            R78Cell5.setCellValue("");
            R78Cell5.setCellStyle(textStyle);
        }
        Cell R78Cell6 = row.createCell(8);
        if (record.getR78_NET_AMT_BWP2() != null) {
            R78Cell6.setCellValue(record.getR78_NET_AMT_BWP2().doubleValue());
            R78Cell6.setCellStyle(numberStyle);
        } else {
            R78Cell6.setCellValue("");
            R78Cell6.setCellStyle(textStyle);
        }
        Cell R78Cell7 = row.createCell(9);
        if (record.getR78_BAL_SUB_BWP1() != null) {
            R78Cell7.setCellValue(record.getR78_BAL_SUB_BWP1().doubleValue());
            R78Cell7.setCellStyle(numberStyle);
        } else {
            R78Cell7.setCellValue("");
            R78Cell7.setCellStyle(textStyle);
        }
        Cell R78Cell8 = row.createCell(10);
        if (record.getR78_BAL_SUB_BWP2() != null) {
            R78Cell8.setCellValue(record.getR78_BAL_SUB_BWP2().doubleValue());
            R78Cell8.setCellStyle(numberStyle);
        } else {
            R78Cell8.setCellValue("");
            R78Cell8.setCellStyle(textStyle);
        }
        Cell R78Cell9 = row.createCell(11);
        if (record.getR78_BAL_ACT_SUB_BWP1() != null) {
            R78Cell9.setCellValue(record.getR78_BAL_ACT_SUB_BWP1().doubleValue());
            R78Cell9.setCellStyle(numberStyle);
        } else {
            R78Cell9.setCellValue("");
            R78Cell9.setCellStyle(textStyle);
        }
        Cell R78Cell10 = row.createCell(12);
        if (record.getR78_BAL_ACT_SUB_BWP2() != null) {
            R78Cell10.setCellValue(record.getR78_BAL_ACT_SUB_BWP2().doubleValue());
            R78Cell10.setCellStyle(numberStyle);
        } else {
            R78Cell10.setCellValue("");
            R78Cell10.setCellStyle(textStyle);
        }

        /* ================= R79 ================= */
        row = sheet.getRow(78);
        Cell R79Cell1 = row.createCell(3);
        if (record.getR79_FIG_BAL_BWP1() != null) {
            R79Cell1.setCellValue(record.getR79_FIG_BAL_BWP1().doubleValue());
            R79Cell1.setCellStyle(numberStyle);
        } else {
            R79Cell1.setCellValue("");
            R79Cell1.setCellStyle(textStyle);
        }
        Cell R79Cell2 = row.createCell(4);
        if (record.getR79_FIG_BAL_BWP2() != null) {
            R79Cell2.setCellValue(record.getR79_FIG_BAL_BWP2().doubleValue());
            R79Cell2.setCellStyle(numberStyle);
        } else {
            R79Cell2.setCellValue("");
            R79Cell2.setCellStyle(textStyle);
        }
        Cell R79Cell3 = row.createCell(5);
        if (record.getR79_AMT_ADJ_BWP1() != null) {
            R79Cell3.setCellValue(record.getR79_AMT_ADJ_BWP1().doubleValue());
            R79Cell3.setCellStyle(numberStyle);
        } else {
            R79Cell3.setCellValue("");
            R79Cell3.setCellStyle(textStyle);
        }
        Cell R79Cell4 = row.createCell(6);
        if (record.getR79_AMT_ADJ_BWP2() != null) {
            R79Cell4.setCellValue(record.getR79_AMT_ADJ_BWP2().doubleValue());
            R79Cell4.setCellStyle(numberStyle);
        } else {
            R79Cell4.setCellValue("");
            R79Cell4.setCellStyle(textStyle);
        }
        Cell R79Cell5 = row.createCell(7);
        if (record.getR79_NET_AMT_BWP1() != null) {
            R79Cell5.setCellValue(record.getR79_NET_AMT_BWP1().doubleValue());
            R79Cell5.setCellStyle(numberStyle);
        } else {
            R79Cell5.setCellValue("");
            R79Cell5.setCellStyle(textStyle);
        }
        Cell R79Cell6 = row.createCell(8);
        if (record.getR79_NET_AMT_BWP2() != null) {
            R79Cell6.setCellValue(record.getR79_NET_AMT_BWP2().doubleValue());
            R79Cell6.setCellStyle(numberStyle);
        } else {
            R79Cell6.setCellValue("");
            R79Cell6.setCellStyle(textStyle);
        }
        Cell R79Cell7 = row.createCell(9);
        if (record.getR79_BAL_SUB_BWP1() != null) {
            R79Cell7.setCellValue(record.getR79_BAL_SUB_BWP1().doubleValue());
            R79Cell7.setCellStyle(numberStyle);
        } else {
            R79Cell7.setCellValue("");
            R79Cell7.setCellStyle(textStyle);
        }
        Cell R79Cell8 = row.createCell(10);
        if (record.getR79_BAL_SUB_BWP2() != null) {
            R79Cell8.setCellValue(record.getR79_BAL_SUB_BWP2().doubleValue());
            R79Cell8.setCellStyle(numberStyle);
        } else {
            R79Cell8.setCellValue("");
            R79Cell8.setCellStyle(textStyle);
        }
        Cell R79Cell9 = row.createCell(11);
        if (record.getR79_BAL_ACT_SUB_BWP1() != null) {
            R79Cell9.setCellValue(record.getR79_BAL_ACT_SUB_BWP1().doubleValue());
            R79Cell9.setCellStyle(numberStyle);
        } else {
            R79Cell9.setCellValue("");
            R79Cell9.setCellStyle(textStyle);
        }
        Cell R79Cell10 = row.createCell(12);
        if (record.getR79_BAL_ACT_SUB_BWP2() != null) {
            R79Cell10.setCellValue(record.getR79_BAL_ACT_SUB_BWP2().doubleValue());
            R79Cell10.setCellStyle(numberStyle);
        } else {
            R79Cell10.setCellValue("");
            R79Cell10.setCellStyle(textStyle);
        }

        /* ================= R80 ================= */
        row = sheet.getRow(79);
        Cell R80Cell1 = row.createCell(3);
        if (record.getR80_FIG_BAL_BWP1() != null) {
            R80Cell1.setCellValue(record.getR80_FIG_BAL_BWP1().doubleValue());
            R80Cell1.setCellStyle(numberStyle);
        } else {
            R80Cell1.setCellValue("");
            R80Cell1.setCellStyle(textStyle);
        }
        Cell R80Cell2 = row.createCell(4);
        if (record.getR80_FIG_BAL_BWP2() != null) {
            R80Cell2.setCellValue(record.getR80_FIG_BAL_BWP2().doubleValue());
            R80Cell2.setCellStyle(numberStyle);
        } else {
            R80Cell2.setCellValue("");
            R80Cell2.setCellStyle(textStyle);
        }
        Cell R80Cell3 = row.createCell(5);
        if (record.getR80_AMT_ADJ_BWP1() != null) {
            R80Cell3.setCellValue(record.getR80_AMT_ADJ_BWP1().doubleValue());
            R80Cell3.setCellStyle(numberStyle);
        } else {
            R80Cell3.setCellValue("");
            R80Cell3.setCellStyle(textStyle);
        }
        Cell R80Cell4 = row.createCell(6);
        if (record.getR80_AMT_ADJ_BWP2() != null) {
            R80Cell4.setCellValue(record.getR80_AMT_ADJ_BWP2().doubleValue());
            R80Cell4.setCellStyle(numberStyle);
        } else {
            R80Cell4.setCellValue("");
            R80Cell4.setCellStyle(textStyle);
        }
        Cell R80Cell5 = row.createCell(7);
        if (record.getR80_NET_AMT_BWP1() != null) {
            R80Cell5.setCellValue(record.getR80_NET_AMT_BWP1().doubleValue());
            R80Cell5.setCellStyle(numberStyle);
        } else {
            R80Cell5.setCellValue("");
            R80Cell5.setCellStyle(textStyle);
        }
        Cell R80Cell6 = row.createCell(8);
        if (record.getR80_NET_AMT_BWP2() != null) {
            R80Cell6.setCellValue(record.getR80_NET_AMT_BWP2().doubleValue());
            R80Cell6.setCellStyle(numberStyle);
        } else {
            R80Cell6.setCellValue("");
            R80Cell6.setCellStyle(textStyle);
        }
        Cell R80Cell7 = row.createCell(9);
        if (record.getR80_BAL_SUB_BWP1() != null) {
            R80Cell7.setCellValue(record.getR80_BAL_SUB_BWP1().doubleValue());
            R80Cell7.setCellStyle(numberStyle);
        } else {
            R80Cell7.setCellValue("");
            R80Cell7.setCellStyle(textStyle);
        }
        Cell R80Cell8 = row.createCell(10);
        if (record.getR80_BAL_SUB_BWP2() != null) {
            R80Cell8.setCellValue(record.getR80_BAL_SUB_BWP2().doubleValue());
            R80Cell8.setCellStyle(numberStyle);
        } else {
            R80Cell8.setCellValue("");
            R80Cell8.setCellStyle(textStyle);
        }
        Cell R80Cell9 = row.createCell(11);
        if (record.getR80_BAL_ACT_SUB_BWP1() != null) {
            R80Cell9.setCellValue(record.getR80_BAL_ACT_SUB_BWP1().doubleValue());
            R80Cell9.setCellStyle(numberStyle);
        } else {
            R80Cell9.setCellValue("");
            R80Cell9.setCellStyle(textStyle);
        }
        Cell R80Cell10 = row.createCell(12);
        if (record.getR80_BAL_ACT_SUB_BWP2() != null) {
            R80Cell10.setCellValue(record.getR80_BAL_ACT_SUB_BWP2().doubleValue());
            R80Cell10.setCellStyle(numberStyle);
        } else {
            R80Cell10.setCellValue("");
            R80Cell10.setCellStyle(textStyle);
        }

        /* ================= R81 ================= */
        row = sheet.getRow(80);
        Cell R81Cell1 = row.createCell(3);
        if (record.getR81_FIG_BAL_BWP1() != null) {
            R81Cell1.setCellValue(record.getR81_FIG_BAL_BWP1().doubleValue());
            R81Cell1.setCellStyle(numberStyle);
        } else {
            R81Cell1.setCellValue("");
            R81Cell1.setCellStyle(textStyle);
        }
        Cell R81Cell2 = row.createCell(4);
        if (record.getR81_FIG_BAL_BWP2() != null) {
            R81Cell2.setCellValue(record.getR81_FIG_BAL_BWP2().doubleValue());
            R81Cell2.setCellStyle(numberStyle);
        } else {
            R81Cell2.setCellValue("");
            R81Cell2.setCellStyle(textStyle);
        }
        Cell R81Cell3 = row.createCell(5);
        if (record.getR81_AMT_ADJ_BWP1() != null) {
            R81Cell3.setCellValue(record.getR81_AMT_ADJ_BWP1().doubleValue());
            R81Cell3.setCellStyle(numberStyle);
        } else {
            R81Cell3.setCellValue("");
            R81Cell3.setCellStyle(textStyle);
        }
        Cell R81Cell4 = row.createCell(6);
        if (record.getR81_AMT_ADJ_BWP2() != null) {
            R81Cell4.setCellValue(record.getR81_AMT_ADJ_BWP2().doubleValue());
            R81Cell4.setCellStyle(numberStyle);
        } else {
            R81Cell4.setCellValue("");
            R81Cell4.setCellStyle(textStyle);
        }
        Cell R81Cell5 = row.createCell(7);
        if (record.getR81_NET_AMT_BWP1() != null) {
            R81Cell5.setCellValue(record.getR81_NET_AMT_BWP1().doubleValue());
            R81Cell5.setCellStyle(numberStyle);
        } else {
            R81Cell5.setCellValue("");
            R81Cell5.setCellStyle(textStyle);
        }
        Cell R81Cell6 = row.createCell(8);
        if (record.getR81_NET_AMT_BWP2() != null) {
            R81Cell6.setCellValue(record.getR81_NET_AMT_BWP2().doubleValue());
            R81Cell6.setCellStyle(numberStyle);
        } else {
            R81Cell6.setCellValue("");
            R81Cell6.setCellStyle(textStyle);
        }
        Cell R81Cell7 = row.createCell(9);
        if (record.getR81_BAL_SUB_BWP1() != null) {
            R81Cell7.setCellValue(record.getR81_BAL_SUB_BWP1().doubleValue());
            R81Cell7.setCellStyle(numberStyle);
        } else {
            R81Cell7.setCellValue("");
            R81Cell7.setCellStyle(textStyle);
        }
        Cell R81Cell8 = row.createCell(10);
        if (record.getR81_BAL_SUB_BWP2() != null) {
            R81Cell8.setCellValue(record.getR81_BAL_SUB_BWP2().doubleValue());
            R81Cell8.setCellStyle(numberStyle);
        } else {
            R81Cell8.setCellValue("");
            R81Cell8.setCellStyle(textStyle);
        }
        Cell R81Cell9 = row.createCell(11);
        if (record.getR81_BAL_ACT_SUB_BWP1() != null) {
            R81Cell9.setCellValue(record.getR81_BAL_ACT_SUB_BWP1().doubleValue());
            R81Cell9.setCellStyle(numberStyle);
        } else {
            R81Cell9.setCellValue("");
            R81Cell9.setCellStyle(textStyle);
        }
        Cell R81Cell10 = row.createCell(12);
        if (record.getR81_BAL_ACT_SUB_BWP2() != null) {
            R81Cell10.setCellValue(record.getR81_BAL_ACT_SUB_BWP2().doubleValue());
            R81Cell10.setCellStyle(numberStyle);
        } else {
            R81Cell10.setCellValue("");
            R81Cell10.setCellStyle(textStyle);
        }

        /* ================= R82 ================= */
        row = sheet.getRow(81);
        Cell R82Cell1 = row.createCell(3);
        if (record.getR82_FIG_BAL_BWP1() != null) {
            R82Cell1.setCellValue(record.getR82_FIG_BAL_BWP1().doubleValue());
            R82Cell1.setCellStyle(numberStyle);
        } else {
            R82Cell1.setCellValue("");
            R82Cell1.setCellStyle(textStyle);
        }
        Cell R82Cell2 = row.createCell(4);
        if (record.getR82_FIG_BAL_BWP2() != null) {
            R82Cell2.setCellValue(record.getR82_FIG_BAL_BWP2().doubleValue());
            R82Cell2.setCellStyle(numberStyle);
        } else {
            R82Cell2.setCellValue("");
            R82Cell2.setCellStyle(textStyle);
        }
        Cell R82Cell3 = row.createCell(5);
        if (record.getR82_AMT_ADJ_BWP1() != null) {
            R82Cell3.setCellValue(record.getR82_AMT_ADJ_BWP1().doubleValue());
            R82Cell3.setCellStyle(numberStyle);
        } else {
            R82Cell3.setCellValue("");
            R82Cell3.setCellStyle(textStyle);
        }
        Cell R82Cell4 = row.createCell(6);
        if (record.getR82_AMT_ADJ_BWP2() != null) {
            R82Cell4.setCellValue(record.getR82_AMT_ADJ_BWP2().doubleValue());
            R82Cell4.setCellStyle(numberStyle);
        } else {
            R82Cell4.setCellValue("");
            R82Cell4.setCellStyle(textStyle);
        }
        Cell R82Cell5 = row.createCell(7);
        if (record.getR82_NET_AMT_BWP1() != null) {
            R82Cell5.setCellValue(record.getR82_NET_AMT_BWP1().doubleValue());
            R82Cell5.setCellStyle(numberStyle);
        } else {
            R82Cell5.setCellValue("");
            R82Cell5.setCellStyle(textStyle);
        }
        Cell R82Cell6 = row.createCell(8);
        if (record.getR82_NET_AMT_BWP2() != null) {
            R82Cell6.setCellValue(record.getR82_NET_AMT_BWP2().doubleValue());
            R82Cell6.setCellStyle(numberStyle);
        } else {
            R82Cell6.setCellValue("");
            R82Cell6.setCellStyle(textStyle);
        }
        Cell R82Cell7 = row.createCell(9);
        if (record.getR82_BAL_SUB_BWP1() != null) {
            R82Cell7.setCellValue(record.getR82_BAL_SUB_BWP1().doubleValue());
            R82Cell7.setCellStyle(numberStyle);
        } else {
            R82Cell7.setCellValue("");
            R82Cell7.setCellStyle(textStyle);
        }
        Cell R82Cell8 = row.createCell(10);
        if (record.getR82_BAL_SUB_BWP2() != null) {
            R82Cell8.setCellValue(record.getR82_BAL_SUB_BWP2().doubleValue());
            R82Cell8.setCellStyle(numberStyle);
        } else {
            R82Cell8.setCellValue("");
            R82Cell8.setCellStyle(textStyle);
        }
        Cell R82Cell9 = row.createCell(11);
        if (record.getR82_BAL_ACT_SUB_BWP1() != null) {
            R82Cell9.setCellValue(record.getR82_BAL_ACT_SUB_BWP1().doubleValue());
            R82Cell9.setCellStyle(numberStyle);
        } else {
            R82Cell9.setCellValue("");
            R82Cell9.setCellStyle(textStyle);
        }
        Cell R82Cell10 = row.createCell(12);
        if (record.getR82_BAL_ACT_SUB_BWP2() != null) {
            R82Cell10.setCellValue(record.getR82_BAL_ACT_SUB_BWP2().doubleValue());
            R82Cell10.setCellStyle(numberStyle);
        } else {
            R82Cell10.setCellValue("");
            R82Cell10.setCellStyle(textStyle);
        }

        /* ================= R83 ================= */
        row = sheet.getRow(82);
        Cell R83Cell1 = row.createCell(3);
        if (record.getR83_FIG_BAL_BWP1() != null) {
            R83Cell1.setCellValue(record.getR83_FIG_BAL_BWP1().doubleValue());
            R83Cell1.setCellStyle(numberStyle);
        } else {
            R83Cell1.setCellValue("");
            R83Cell1.setCellStyle(textStyle);
        }
        Cell R83Cell2 = row.createCell(4);
        if (record.getR83_FIG_BAL_BWP2() != null) {
            R83Cell2.setCellValue(record.getR83_FIG_BAL_BWP2().doubleValue());
            R83Cell2.setCellStyle(numberStyle);
        } else {
            R83Cell2.setCellValue("");
            R83Cell2.setCellStyle(textStyle);
        }
        Cell R83Cell3 = row.createCell(5);
        if (record.getR83_AMT_ADJ_BWP1() != null) {
            R83Cell3.setCellValue(record.getR83_AMT_ADJ_BWP1().doubleValue());
            R83Cell3.setCellStyle(numberStyle);
        } else {
            R83Cell3.setCellValue("");
            R83Cell3.setCellStyle(textStyle);
        }
        Cell R83Cell4 = row.createCell(6);
        if (record.getR83_AMT_ADJ_BWP2() != null) {
            R83Cell4.setCellValue(record.getR83_AMT_ADJ_BWP2().doubleValue());
            R83Cell4.setCellStyle(numberStyle);
        } else {
            R83Cell4.setCellValue("");
            R83Cell4.setCellStyle(textStyle);
        }
        Cell R83Cell5 = row.createCell(7);
        if (record.getR83_NET_AMT_BWP1() != null) {
            R83Cell5.setCellValue(record.getR83_NET_AMT_BWP1().doubleValue());
            R83Cell5.setCellStyle(numberStyle);
        } else {
            R83Cell5.setCellValue("");
            R83Cell5.setCellStyle(textStyle);
        }
        Cell R83Cell6 = row.createCell(8);
        if (record.getR83_NET_AMT_BWP2() != null) {
            R83Cell6.setCellValue(record.getR83_NET_AMT_BWP2().doubleValue());
            R83Cell6.setCellStyle(numberStyle);
        } else {
            R83Cell6.setCellValue("");
            R83Cell6.setCellStyle(textStyle);
        }
        Cell R83Cell7 = row.createCell(9);
        if (record.getR83_BAL_SUB_BWP1() != null) {
            R83Cell7.setCellValue(record.getR83_BAL_SUB_BWP1().doubleValue());
            R83Cell7.setCellStyle(numberStyle);
        } else {
            R83Cell7.setCellValue("");
            R83Cell7.setCellStyle(textStyle);
        }
        Cell R83Cell8 = row.createCell(10);
        if (record.getR83_BAL_SUB_BWP2() != null) {
            R83Cell8.setCellValue(record.getR83_BAL_SUB_BWP2().doubleValue());
            R83Cell8.setCellStyle(numberStyle);
        } else {
            R83Cell8.setCellValue("");
            R83Cell8.setCellStyle(textStyle);
        }
        Cell R83Cell9 = row.createCell(11);
        if (record.getR83_BAL_ACT_SUB_BWP1() != null) {
            R83Cell9.setCellValue(record.getR83_BAL_ACT_SUB_BWP1().doubleValue());
            R83Cell9.setCellStyle(numberStyle);
        } else {
            R83Cell9.setCellValue("");
            R83Cell9.setCellStyle(textStyle);
        }
        Cell R83Cell10 = row.createCell(12);
        if (record.getR83_BAL_ACT_SUB_BWP2() != null) {
            R83Cell10.setCellValue(record.getR83_BAL_ACT_SUB_BWP2().doubleValue());
            R83Cell10.setCellStyle(numberStyle);
        } else {
            R83Cell10.setCellValue("");
            R83Cell10.setCellStyle(textStyle);
        }

        /* ================= R84 ================= */
        row = sheet.getRow(83);
        Cell R84Cell1 = row.createCell(3);
        if (record.getR84_FIG_BAL_BWP1() != null) {
            R84Cell1.setCellValue(record.getR84_FIG_BAL_BWP1().doubleValue());
            R84Cell1.setCellStyle(numberStyle);
        } else {
            R84Cell1.setCellValue("");
            R84Cell1.setCellStyle(textStyle);
        }
        Cell R84Cell2 = row.createCell(4);
        if (record.getR84_FIG_BAL_BWP2() != null) {
            R84Cell2.setCellValue(record.getR84_FIG_BAL_BWP2().doubleValue());
            R84Cell2.setCellStyle(numberStyle);
        } else {
            R84Cell2.setCellValue("");
            R84Cell2.setCellStyle(textStyle);
        }
        Cell R84Cell3 = row.createCell(5);
        if (record.getR84_AMT_ADJ_BWP1() != null) {
            R84Cell3.setCellValue(record.getR84_AMT_ADJ_BWP1().doubleValue());
            R84Cell3.setCellStyle(numberStyle);
        } else {
            R84Cell3.setCellValue("");
            R84Cell3.setCellStyle(textStyle);
        }
        Cell R84Cell4 = row.createCell(6);
        if (record.getR84_AMT_ADJ_BWP2() != null) {
            R84Cell4.setCellValue(record.getR84_AMT_ADJ_BWP2().doubleValue());
            R84Cell4.setCellStyle(numberStyle);
        } else {
            R84Cell4.setCellValue("");
            R84Cell4.setCellStyle(textStyle);
        }
        Cell R84Cell5 = row.createCell(7);
        if (record.getR84_NET_AMT_BWP1() != null) {
            R84Cell5.setCellValue(record.getR84_NET_AMT_BWP1().doubleValue());
            R84Cell5.setCellStyle(numberStyle);
        } else {
            R84Cell5.setCellValue("");
            R84Cell5.setCellStyle(textStyle);
        }
        Cell R84Cell6 = row.createCell(8);
        if (record.getR84_NET_AMT_BWP2() != null) {
            R84Cell6.setCellValue(record.getR84_NET_AMT_BWP2().doubleValue());
            R84Cell6.setCellStyle(numberStyle);
        } else {
            R84Cell6.setCellValue("");
            R84Cell6.setCellStyle(textStyle);
        }
        Cell R84Cell7 = row.createCell(9);
        if (record.getR84_BAL_SUB_BWP1() != null) {
            R84Cell7.setCellValue(record.getR84_BAL_SUB_BWP1().doubleValue());
            R84Cell7.setCellStyle(numberStyle);
        } else {
            R84Cell7.setCellValue("");
            R84Cell7.setCellStyle(textStyle);
        }
        Cell R84Cell8 = row.createCell(10);
        if (record.getR84_BAL_SUB_BWP2() != null) {
            R84Cell8.setCellValue(record.getR84_BAL_SUB_BWP2().doubleValue());
            R84Cell8.setCellStyle(numberStyle);
        } else {
            R84Cell8.setCellValue("");
            R84Cell8.setCellStyle(textStyle);
        }
        Cell R84Cell9 = row.createCell(11);
        if (record.getR84_BAL_ACT_SUB_BWP1() != null) {
            R84Cell9.setCellValue(record.getR84_BAL_ACT_SUB_BWP1().doubleValue());
            R84Cell9.setCellStyle(numberStyle);
        } else {
            R84Cell9.setCellValue("");
            R84Cell9.setCellStyle(textStyle);
        }
        Cell R84Cell10 = row.createCell(12);
        if (record.getR84_BAL_ACT_SUB_BWP2() != null) {
            R84Cell10.setCellValue(record.getR84_BAL_ACT_SUB_BWP2().doubleValue());
            R84Cell10.setCellStyle(numberStyle);
        } else {
            R84Cell10.setCellValue("");
            R84Cell10.setCellStyle(textStyle);
        }

        /* ================= R87 - R94 ================= */

        /* ================= R87 ================= */
        row = sheet.getRow(86);
        Cell R87Cell1 = row.createCell(33);
        if (record.getR87_FIG_BAL_BWP1() != null) {
            R87Cell1.setCellValue(record.getR87_FIG_BAL_BWP1().doubleValue());
            R87Cell1.setCellStyle(numberStyle);
        } else {
            R87Cell1.setCellValue("");
            R87Cell1.setCellStyle(textStyle);
        }
        Cell R87Cell2 = row.createCell(34);
        if (record.getR87_FIG_BAL_BWP2() != null) {
            R87Cell2.setCellValue(record.getR87_FIG_BAL_BWP2().doubleValue());
            R87Cell2.setCellStyle(numberStyle);
        } else {
            R87Cell2.setCellValue("");
            R87Cell2.setCellStyle(textStyle);
        }
        Cell R87Cell3 = row.createCell(35);
        if (record.getR87_AMT_ADJ_BWP1() != null) {
            R87Cell3.setCellValue(record.getR87_AMT_ADJ_BWP1().doubleValue());
            R87Cell3.setCellStyle(numberStyle);
        } else {
            R87Cell3.setCellValue("");
            R87Cell3.setCellStyle(textStyle);
        }
        Cell R87Cell4 = row.createCell(36);
        if (record.getR87_AMT_ADJ_BWP2() != null) {
            R87Cell4.setCellValue(record.getR87_AMT_ADJ_BWP2().doubleValue());
            R87Cell4.setCellStyle(numberStyle);
        } else {
            R87Cell4.setCellValue("");
            R87Cell4.setCellStyle(textStyle);
        }
        Cell R87Cell5 = row.createCell(37);
        if (record.getR87_NET_AMT_BWP1() != null) {
            R87Cell5.setCellValue(record.getR87_NET_AMT_BWP1().doubleValue());
            R87Cell5.setCellStyle(numberStyle);
        } else {
            R87Cell5.setCellValue("");
            R87Cell5.setCellStyle(textStyle);
        }
        Cell R87Cell6 = row.createCell(38);
        if (record.getR87_NET_AMT_BWP2() != null) {
            R87Cell6.setCellValue(record.getR87_NET_AMT_BWP2().doubleValue());
            R87Cell6.setCellStyle(numberStyle);
        } else {
            R87Cell6.setCellValue("");
            R87Cell6.setCellStyle(textStyle);
        }
        Cell R87Cell7 = row.createCell(39);
        if (record.getR87_BAL_SUB_BWP1() != null) {
            R87Cell7.setCellValue(record.getR87_BAL_SUB_BWP1().doubleValue());
            R87Cell7.setCellStyle(numberStyle);
        } else {
            R87Cell7.setCellValue("");
            R87Cell7.setCellStyle(textStyle);
        }
        Cell R87Cell8 = row.createCell(40);
        if (record.getR87_BAL_SUB_BWP2() != null) {
            R87Cell8.setCellValue(record.getR87_BAL_SUB_BWP2().doubleValue());
            R87Cell8.setCellStyle(numberStyle);
        } else {
            R87Cell8.setCellValue("");
            R87Cell8.setCellStyle(textStyle);
        }
        Cell R87Cell9 = row.createCell(41);
        if (record.getR87_BAL_ACT_SUB_BWP1() != null) {
            R87Cell9.setCellValue(record.getR87_BAL_ACT_SUB_BWP1().doubleValue());
            R87Cell9.setCellStyle(numberStyle);
        } else {
            R87Cell9.setCellValue("");
            R87Cell9.setCellStyle(textStyle);
        }
        Cell R87Cell10 = row.createCell(42);
        if (record.getR87_BAL_ACT_SUB_BWP2() != null) {
            R87Cell10.setCellValue(record.getR87_BAL_ACT_SUB_BWP2().doubleValue());
            R87Cell10.setCellStyle(numberStyle);
        } else {
            R87Cell10.setCellValue("");
            R87Cell10.setCellStyle(textStyle);
        }

        /* ================= R88 ================= */
        row = sheet.getRow(87);
        Cell R88Cell1 = row.createCell(3);
        if (record.getR88_FIG_BAL_BWP1() != null) {
            R88Cell1.setCellValue(record.getR88_FIG_BAL_BWP1().doubleValue());
            R88Cell1.setCellStyle(numberStyle);
        } else {
            R88Cell1.setCellValue("");
            R88Cell1.setCellStyle(textStyle);
        }
        Cell R88Cell2 = row.createCell(4);
        if (record.getR88_FIG_BAL_BWP2() != null) {
            R88Cell2.setCellValue(record.getR88_FIG_BAL_BWP2().doubleValue());
            R88Cell2.setCellStyle(numberStyle);
        } else {
            R88Cell2.setCellValue("");
            R88Cell2.setCellStyle(textStyle);
        }
        Cell R88Cell3 = row.createCell(5);
        if (record.getR88_AMT_ADJ_BWP1() != null) {
            R88Cell3.setCellValue(record.getR88_AMT_ADJ_BWP1().doubleValue());
            R88Cell3.setCellStyle(numberStyle);
        } else {
            R88Cell3.setCellValue("");
            R88Cell3.setCellStyle(textStyle);
        }
        Cell R88Cell4 = row.createCell(6);
        if (record.getR88_AMT_ADJ_BWP2() != null) {
            R88Cell4.setCellValue(record.getR88_AMT_ADJ_BWP2().doubleValue());
            R88Cell4.setCellStyle(numberStyle);
        } else {
            R88Cell4.setCellValue("");
            R88Cell4.setCellStyle(textStyle);
        }
        Cell R88Cell5 = row.createCell(7);
        if (record.getR88_NET_AMT_BWP1() != null) {
            R88Cell5.setCellValue(record.getR88_NET_AMT_BWP1().doubleValue());
            R88Cell5.setCellStyle(numberStyle);
        } else {
            R88Cell5.setCellValue("");
            R88Cell5.setCellStyle(textStyle);
        }
        Cell R88Cell6 = row.createCell(8);
        if (record.getR88_NET_AMT_BWP2() != null) {
            R88Cell6.setCellValue(record.getR88_NET_AMT_BWP2().doubleValue());
            R88Cell6.setCellStyle(numberStyle);
        } else {
            R88Cell6.setCellValue("");
            R88Cell6.setCellStyle(textStyle);
        }
        Cell R88Cell7 = row.createCell(9);
        if (record.getR88_BAL_SUB_BWP1() != null) {
            R88Cell7.setCellValue(record.getR88_BAL_SUB_BWP1().doubleValue());
            R88Cell7.setCellStyle(numberStyle);
        } else {
            R88Cell7.setCellValue("");
            R88Cell7.setCellStyle(textStyle);
        }
        Cell R88Cell8 = row.createCell(10);
        if (record.getR88_BAL_SUB_BWP2() != null) {
            R88Cell8.setCellValue(record.getR88_BAL_SUB_BWP2().doubleValue());
            R88Cell8.setCellStyle(numberStyle);
        } else {
            R88Cell8.setCellValue("");
            R88Cell8.setCellStyle(textStyle);
        }
        Cell R88Cell9 = row.createCell(11);
        if (record.getR88_BAL_ACT_SUB_BWP1() != null) {
            R88Cell9.setCellValue(record.getR88_BAL_ACT_SUB_BWP1().doubleValue());
            R88Cell9.setCellStyle(numberStyle);
        } else {
            R88Cell9.setCellValue("");
            R88Cell9.setCellStyle(textStyle);
        }
        Cell R88Cell10 = row.createCell(12);
        if (record.getR88_BAL_ACT_SUB_BWP2() != null) {
            R88Cell10.setCellValue(record.getR88_BAL_ACT_SUB_BWP2().doubleValue());
            R88Cell10.setCellStyle(numberStyle);
        } else {
            R88Cell10.setCellValue("");
            R88Cell10.setCellStyle(textStyle);
        }

        /* ================= R89 ================= */
        row = sheet.getRow(88);
        Cell R89Cell1 = row.createCell(3);
        if (record.getR89_FIG_BAL_BWP1() != null) {
            R89Cell1.setCellValue(record.getR89_FIG_BAL_BWP1().doubleValue());
            R89Cell1.setCellStyle(numberStyle);
        } else {
            R89Cell1.setCellValue("");
            R89Cell1.setCellStyle(textStyle);
        }
        Cell R89Cell2 = row.createCell(4);
        if (record.getR89_FIG_BAL_BWP2() != null) {
            R89Cell2.setCellValue(record.getR89_FIG_BAL_BWP2().doubleValue());
            R89Cell2.setCellStyle(numberStyle);
        } else {
            R89Cell2.setCellValue("");
            R89Cell2.setCellStyle(textStyle);
        }
        Cell R89Cell3 = row.createCell(5);
        if (record.getR89_AMT_ADJ_BWP1() != null) {
            R89Cell3.setCellValue(record.getR89_AMT_ADJ_BWP1().doubleValue());
            R89Cell3.setCellStyle(numberStyle);
        } else {
            R89Cell3.setCellValue("");
            R89Cell3.setCellStyle(textStyle);
        }
        Cell R89Cell4 = row.createCell(6);
        if (record.getR89_AMT_ADJ_BWP2() != null) {
            R89Cell4.setCellValue(record.getR89_AMT_ADJ_BWP2().doubleValue());
            R89Cell4.setCellStyle(numberStyle);
        } else {
            R89Cell4.setCellValue("");
            R89Cell4.setCellStyle(textStyle);
        }
        Cell R89Cell5 = row.createCell(7);
        if (record.getR89_NET_AMT_BWP1() != null) {
            R89Cell5.setCellValue(record.getR89_NET_AMT_BWP1().doubleValue());
            R89Cell5.setCellStyle(numberStyle);
        } else {
            R89Cell5.setCellValue("");
            R89Cell5.setCellStyle(textStyle);
        }
        Cell R89Cell6 = row.createCell(8);
        if (record.getR89_NET_AMT_BWP2() != null) {
            R89Cell6.setCellValue(record.getR89_NET_AMT_BWP2().doubleValue());
            R89Cell6.setCellStyle(numberStyle);
        } else {
            R89Cell6.setCellValue("");
            R89Cell6.setCellStyle(textStyle);
        }
        Cell R89Cell7 = row.createCell(9);
        if (record.getR89_BAL_SUB_BWP1() != null) {
            R89Cell7.setCellValue(record.getR89_BAL_SUB_BWP1().doubleValue());
            R89Cell7.setCellStyle(numberStyle);
        } else {
            R89Cell7.setCellValue("");
            R89Cell7.setCellStyle(textStyle);
        }
        Cell R89Cell8 = row.createCell(10);
        if (record.getR89_BAL_SUB_BWP2() != null) {
            R89Cell8.setCellValue(record.getR89_BAL_SUB_BWP2().doubleValue());
            R89Cell8.setCellStyle(numberStyle);
        } else {
            R89Cell8.setCellValue("");
            R89Cell8.setCellStyle(textStyle);
        }
        Cell R89Cell9 = row.createCell(11);
        if (record.getR89_BAL_ACT_SUB_BWP1() != null) {
            R89Cell9.setCellValue(record.getR89_BAL_ACT_SUB_BWP1().doubleValue());
            R89Cell9.setCellStyle(numberStyle);
        } else {
            R89Cell9.setCellValue("");
            R89Cell9.setCellStyle(textStyle);
        }
        Cell R89Cell10 = row.createCell(12);
        if (record.getR89_BAL_ACT_SUB_BWP2() != null) {
            R89Cell10.setCellValue(record.getR89_BAL_ACT_SUB_BWP2().doubleValue());
            R89Cell10.setCellStyle(numberStyle);
        } else {
            R89Cell10.setCellValue("");
            R89Cell10.setCellStyle(textStyle);
        }

        /* ================= R90 ================= */
        row = sheet.getRow(89);
        Cell R90Cell1 = row.createCell(3);
        if (record.getR90_FIG_BAL_BWP1() != null) {
            R90Cell1.setCellValue(record.getR90_FIG_BAL_BWP1().doubleValue());
            R90Cell1.setCellStyle(numberStyle);
        } else {
            R90Cell1.setCellValue("");
            R90Cell1.setCellStyle(textStyle);
        }
        Cell R90Cell2 = row.createCell(4);
        if (record.getR90_FIG_BAL_BWP2() != null) {
            R90Cell2.setCellValue(record.getR90_FIG_BAL_BWP2().doubleValue());
            R90Cell2.setCellStyle(numberStyle);
        } else {
            R90Cell2.setCellValue("");
            R90Cell2.setCellStyle(textStyle);
        }
        Cell R90Cell3 = row.createCell(5);
        if (record.getR90_AMT_ADJ_BWP1() != null) {
            R90Cell3.setCellValue(record.getR90_AMT_ADJ_BWP1().doubleValue());
            R90Cell3.setCellStyle(numberStyle);
        } else {
            R90Cell3.setCellValue("");
            R90Cell3.setCellStyle(textStyle);
        }
        Cell R90Cell4 = row.createCell(6);
        if (record.getR90_AMT_ADJ_BWP2() != null) {
            R90Cell4.setCellValue(record.getR90_AMT_ADJ_BWP2().doubleValue());
            R90Cell4.setCellStyle(numberStyle);
        } else {
            R90Cell4.setCellValue("");
            R90Cell4.setCellStyle(textStyle);
        }
        Cell R90Cell5 = row.createCell(7);
        if (record.getR90_NET_AMT_BWP1() != null) {
            R90Cell5.setCellValue(record.getR90_NET_AMT_BWP1().doubleValue());
            R90Cell5.setCellStyle(numberStyle);
        } else {
            R90Cell5.setCellValue("");
            R90Cell5.setCellStyle(textStyle);
        }
        Cell R90Cell6 = row.createCell(8);
        if (record.getR90_NET_AMT_BWP2() != null) {
            R90Cell6.setCellValue(record.getR90_NET_AMT_BWP2().doubleValue());
            R90Cell6.setCellStyle(numberStyle);
        } else {
            R90Cell6.setCellValue("");
            R90Cell6.setCellStyle(textStyle);
        }
        Cell R90Cell7 = row.createCell(9);
        if (record.getR90_BAL_SUB_BWP1() != null) {
            R90Cell7.setCellValue(record.getR90_BAL_SUB_BWP1().doubleValue());
            R90Cell7.setCellStyle(numberStyle);
        } else {
            R90Cell7.setCellValue("");
            R90Cell7.setCellStyle(textStyle);
        }
        Cell R90Cell8 = row.createCell(10);
        if (record.getR90_BAL_SUB_BWP2() != null) {
            R90Cell8.setCellValue(record.getR90_BAL_SUB_BWP2().doubleValue());
            R90Cell8.setCellStyle(numberStyle);
        } else {
            R90Cell8.setCellValue("");
            R90Cell8.setCellStyle(textStyle);
        }
        Cell R90Cell9 = row.createCell(11);
        if (record.getR90_BAL_ACT_SUB_BWP1() != null) {
            R90Cell9.setCellValue(record.getR90_BAL_ACT_SUB_BWP1().doubleValue());
            R90Cell9.setCellStyle(numberStyle);
        } else {
            R90Cell9.setCellValue("");
            R90Cell9.setCellStyle(textStyle);
        }
        Cell R90Cell10 = row.createCell(12);
        if (record.getR90_BAL_ACT_SUB_BWP2() != null) {
            R90Cell10.setCellValue(record.getR90_BAL_ACT_SUB_BWP2().doubleValue());
            R90Cell10.setCellStyle(numberStyle);
        } else {
            R90Cell10.setCellValue("");
            R90Cell10.setCellStyle(textStyle);
        }

        /* ================= R91 ================= */
        row = sheet.getRow(90);
        Cell R91Cell1 = row.createCell(3);
        if (record.getR91_FIG_BAL_BWP1() != null) {
            R91Cell1.setCellValue(record.getR91_FIG_BAL_BWP1().doubleValue());
            R91Cell1.setCellStyle(numberStyle);
        } else {
            R91Cell1.setCellValue("");
            R91Cell1.setCellStyle(textStyle);
        }
        Cell R91Cell2 = row.createCell(4);
        if (record.getR91_FIG_BAL_BWP2() != null) {
            R91Cell2.setCellValue(record.getR91_FIG_BAL_BWP2().doubleValue());
            R91Cell2.setCellStyle(numberStyle);
        } else {
            R91Cell2.setCellValue("");
            R91Cell2.setCellStyle(textStyle);
        }
        Cell R91Cell3 = row.createCell(5);
        if (record.getR91_AMT_ADJ_BWP1() != null) {
            R91Cell3.setCellValue(record.getR91_AMT_ADJ_BWP1().doubleValue());
            R91Cell3.setCellStyle(numberStyle);
        } else {
            R91Cell3.setCellValue("");
            R91Cell3.setCellStyle(textStyle);
        }
        Cell R91Cell4 = row.createCell(6);
        if (record.getR91_AMT_ADJ_BWP2() != null) {
            R91Cell4.setCellValue(record.getR91_AMT_ADJ_BWP2().doubleValue());
            R91Cell4.setCellStyle(numberStyle);
        } else {
            R91Cell4.setCellValue("");
            R91Cell4.setCellStyle(textStyle);
        }
        Cell R91Cell5 = row.createCell(7);
        if (record.getR91_NET_AMT_BWP1() != null) {
            R91Cell5.setCellValue(record.getR91_NET_AMT_BWP1().doubleValue());
            R91Cell5.setCellStyle(numberStyle);
        } else {
            R91Cell5.setCellValue("");
            R91Cell5.setCellStyle(textStyle);
        }
        Cell R91Cell6 = row.createCell(8);
        if (record.getR91_NET_AMT_BWP2() != null) {
            R91Cell6.setCellValue(record.getR91_NET_AMT_BWP2().doubleValue());
            R91Cell6.setCellStyle(numberStyle);
        } else {
            R91Cell6.setCellValue("");
            R91Cell6.setCellStyle(textStyle);
        }
        Cell R91Cell7 = row.createCell(9);
        if (record.getR91_BAL_SUB_BWP1() != null) {
            R91Cell7.setCellValue(record.getR91_BAL_SUB_BWP1().doubleValue());
            R91Cell7.setCellStyle(numberStyle);
        } else {
            R91Cell7.setCellValue("");
            R91Cell7.setCellStyle(textStyle);
        }
        Cell R91Cell8 = row.createCell(10);
        if (record.getR91_BAL_SUB_BWP2() != null) {
            R91Cell8.setCellValue(record.getR91_BAL_SUB_BWP2().doubleValue());
            R91Cell8.setCellStyle(numberStyle);
        } else {
            R91Cell8.setCellValue("");
            R91Cell8.setCellStyle(textStyle);
        }
        Cell R91Cell9 = row.createCell(11);
        if (record.getR91_BAL_ACT_SUB_BWP1() != null) {
            R91Cell9.setCellValue(record.getR91_BAL_ACT_SUB_BWP1().doubleValue());
            R91Cell9.setCellStyle(numberStyle);
        } else {
            R91Cell9.setCellValue("");
            R91Cell9.setCellStyle(textStyle);
        }
        Cell R91Cell10 = row.createCell(12);
        if (record.getR91_BAL_ACT_SUB_BWP2() != null) {
            R91Cell10.setCellValue(record.getR91_BAL_ACT_SUB_BWP2().doubleValue());
            R91Cell10.setCellStyle(numberStyle);
        } else {
            R91Cell10.setCellValue("");
            R91Cell10.setCellStyle(textStyle);
        }

        /* ================= R92 ================= */
        row = sheet.getRow(91);
        Cell R92Cell1 = row.createCell(3);
        if (record.getR92_FIG_BAL_BWP1() != null) {
            R92Cell1.setCellValue(record.getR92_FIG_BAL_BWP1().doubleValue());
            R92Cell1.setCellStyle(numberStyle);
        } else {
            R92Cell1.setCellValue("");
            R92Cell1.setCellStyle(textStyle);
        }
        Cell R92Cell2 = row.createCell(4);
        if (record.getR92_FIG_BAL_BWP2() != null) {
            R92Cell2.setCellValue(record.getR92_FIG_BAL_BWP2().doubleValue());
            R92Cell2.setCellStyle(numberStyle);
        } else {
            R92Cell2.setCellValue("");
            R92Cell2.setCellStyle(textStyle);
        }
        Cell R92Cell3 = row.createCell(5);
        if (record.getR92_AMT_ADJ_BWP1() != null) {
            R92Cell3.setCellValue(record.getR92_AMT_ADJ_BWP1().doubleValue());
            R92Cell3.setCellStyle(numberStyle);
        } else {
            R92Cell3.setCellValue("");
            R92Cell3.setCellStyle(textStyle);
        }
        Cell R92Cell4 = row.createCell(6);
        if (record.getR92_AMT_ADJ_BWP2() != null) {
            R92Cell4.setCellValue(record.getR92_AMT_ADJ_BWP2().doubleValue());
            R92Cell4.setCellStyle(numberStyle);
        } else {
            R92Cell4.setCellValue("");
            R92Cell4.setCellStyle(textStyle);
        }
        Cell R92Cell5 = row.createCell(7);
        if (record.getR92_NET_AMT_BWP1() != null) {
            R92Cell5.setCellValue(record.getR92_NET_AMT_BWP1().doubleValue());
            R92Cell5.setCellStyle(numberStyle);
        } else {
            R92Cell5.setCellValue("");
            R92Cell5.setCellStyle(textStyle);
        }
        Cell R92Cell6 = row.createCell(8);
        if (record.getR92_NET_AMT_BWP2() != null) {
            R92Cell6.setCellValue(record.getR92_NET_AMT_BWP2().doubleValue());
            R92Cell6.setCellStyle(numberStyle);
        } else {
            R92Cell6.setCellValue("");
            R92Cell6.setCellStyle(textStyle);
        }
        Cell R92Cell7 = row.createCell(9);
        if (record.getR92_BAL_SUB_BWP1() != null) {
            R92Cell7.setCellValue(record.getR92_BAL_SUB_BWP1().doubleValue());
            R92Cell7.setCellStyle(numberStyle);
        } else {
            R92Cell7.setCellValue("");
            R92Cell7.setCellStyle(textStyle);
        }
        Cell R92Cell8 = row.createCell(10);
        if (record.getR92_BAL_SUB_BWP2() != null) {
            R92Cell8.setCellValue(record.getR92_BAL_SUB_BWP2().doubleValue());
            R92Cell8.setCellStyle(numberStyle);
        } else {
            R92Cell8.setCellValue("");
            R92Cell8.setCellStyle(textStyle);
        }
        Cell R92Cell9 = row.createCell(11);
        if (record.getR92_BAL_ACT_SUB_BWP1() != null) {
            R92Cell9.setCellValue(record.getR92_BAL_ACT_SUB_BWP1().doubleValue());
            R92Cell9.setCellStyle(numberStyle);
        } else {
            R92Cell9.setCellValue("");
            R92Cell9.setCellStyle(textStyle);
        }
        Cell R92Cell10 = row.createCell(12);
        if (record.getR92_BAL_ACT_SUB_BWP2() != null) {
            R92Cell10.setCellValue(record.getR92_BAL_ACT_SUB_BWP2().doubleValue());
            R92Cell10.setCellStyle(numberStyle);
        } else {
            R92Cell10.setCellValue("");
            R92Cell10.setCellStyle(textStyle);
        }

        /* ================= R93 ================= */
        row = sheet.getRow(92);
        Cell R93Cell1 = row.createCell(3);
        if (record.getR93_FIG_BAL_BWP1() != null) {
            R93Cell1.setCellValue(record.getR93_FIG_BAL_BWP1().doubleValue());
            R93Cell1.setCellStyle(numberStyle);
        } else {
            R93Cell1.setCellValue("");
            R93Cell1.setCellStyle(textStyle);
        }
        Cell R93Cell2 = row.createCell(4);
        if (record.getR93_FIG_BAL_BWP2() != null) {
            R93Cell2.setCellValue(record.getR93_FIG_BAL_BWP2().doubleValue());
            R93Cell2.setCellStyle(numberStyle);
        } else {
            R93Cell2.setCellValue("");
            R93Cell2.setCellStyle(textStyle);
        }
        Cell R93Cell3 = row.createCell(5);
        if (record.getR93_AMT_ADJ_BWP1() != null) {
            R93Cell3.setCellValue(record.getR93_AMT_ADJ_BWP1().doubleValue());
            R93Cell3.setCellStyle(numberStyle);
        } else {
            R93Cell3.setCellValue("");
            R93Cell3.setCellStyle(textStyle);
        }
        Cell R93Cell4 = row.createCell(6);
        if (record.getR93_AMT_ADJ_BWP2() != null) {
            R93Cell4.setCellValue(record.getR93_AMT_ADJ_BWP2().doubleValue());
            R93Cell4.setCellStyle(numberStyle);
        } else {
            R93Cell4.setCellValue("");
            R93Cell4.setCellStyle(textStyle);
        }
        Cell R93Cell5 = row.createCell(7);
        if (record.getR93_NET_AMT_BWP1() != null) {
            R93Cell5.setCellValue(record.getR93_NET_AMT_BWP1().doubleValue());
            R93Cell5.setCellStyle(numberStyle);
        } else {
            R93Cell5.setCellValue("");
            R93Cell5.setCellStyle(textStyle);
        }
        Cell R93Cell6 = row.createCell(8);
        if (record.getR93_NET_AMT_BWP2() != null) {
            R93Cell6.setCellValue(record.getR93_NET_AMT_BWP2().doubleValue());
            R93Cell6.setCellStyle(numberStyle);
        } else {
            R93Cell6.setCellValue("");
            R93Cell6.setCellStyle(textStyle);
        }
        Cell R93Cell7 = row.createCell(9);
        if (record.getR93_BAL_SUB_BWP1() != null) {
            R93Cell7.setCellValue(record.getR93_BAL_SUB_BWP1().doubleValue());
            R93Cell7.setCellStyle(numberStyle);
        } else {
            R93Cell7.setCellValue("");
            R93Cell7.setCellStyle(textStyle);
        }
        Cell R93Cell8 = row.createCell(10);
        if (record.getR93_BAL_SUB_BWP2() != null) {
            R93Cell8.setCellValue(record.getR93_BAL_SUB_BWP2().doubleValue());
            R93Cell8.setCellStyle(numberStyle);
        } else {
            R93Cell8.setCellValue("");
            R93Cell8.setCellStyle(textStyle);
        }
        Cell R93Cell9 = row.createCell(11);
        if (record.getR93_BAL_ACT_SUB_BWP1() != null) {
            R93Cell9.setCellValue(record.getR93_BAL_ACT_SUB_BWP1().doubleValue());
            R93Cell9.setCellStyle(numberStyle);
        } else {
            R93Cell9.setCellValue("");
            R93Cell9.setCellStyle(textStyle);
        }
        Cell R93Cell10 = row.createCell(12);
        if (record.getR93_BAL_ACT_SUB_BWP2() != null) {
            R93Cell10.setCellValue(record.getR93_BAL_ACT_SUB_BWP2().doubleValue());
            R93Cell10.setCellStyle(numberStyle);
        } else {
            R93Cell10.setCellValue("");
            R93Cell10.setCellStyle(textStyle);
        }

        /* ================= R94 ================= */
        row = sheet.getRow(93);
        Cell R94Cell1 = row.createCell(3);
        if (record.getR94_FIG_BAL_BWP1() != null) {
            R94Cell1.setCellValue(record.getR94_FIG_BAL_BWP1().doubleValue());
            R94Cell1.setCellStyle(numberStyle);
        } else {
            R94Cell1.setCellValue("");
            R94Cell1.setCellStyle(textStyle);
        }
        Cell R94Cell2 = row.createCell(4);
        if (record.getR94_FIG_BAL_BWP2() != null) {
            R94Cell2.setCellValue(record.getR94_FIG_BAL_BWP2().doubleValue());
            R94Cell2.setCellStyle(numberStyle);
        } else {
            R94Cell2.setCellValue("");
            R94Cell2.setCellStyle(textStyle);
        }
        Cell R94Cell3 = row.createCell(5);
        if (record.getR94_AMT_ADJ_BWP1() != null) {
            R94Cell3.setCellValue(record.getR94_AMT_ADJ_BWP1().doubleValue());
            R94Cell3.setCellStyle(numberStyle);
        } else {
            R94Cell3.setCellValue("");
            R94Cell3.setCellStyle(textStyle);
        }
        Cell R94Cell4 = row.createCell(6);
        if (record.getR94_AMT_ADJ_BWP2() != null) {
            R94Cell4.setCellValue(record.getR94_AMT_ADJ_BWP2().doubleValue());
            R94Cell4.setCellStyle(numberStyle);
        } else {
            R94Cell4.setCellValue("");
            R94Cell4.setCellStyle(textStyle);
        }
        Cell R94Cell5 = row.createCell(7);
        if (record.getR94_NET_AMT_BWP1() != null) {
            R94Cell5.setCellValue(record.getR94_NET_AMT_BWP1().doubleValue());
            R94Cell5.setCellStyle(numberStyle);
        } else {
            R94Cell5.setCellValue("");
            R94Cell5.setCellStyle(textStyle);
        }
        Cell R94Cell6 = row.createCell(8);
        if (record.getR94_NET_AMT_BWP2() != null) {
            R94Cell6.setCellValue(record.getR94_NET_AMT_BWP2().doubleValue());
            R94Cell6.setCellStyle(numberStyle);
        } else {
            R94Cell6.setCellValue("");
            R94Cell6.setCellStyle(textStyle);
        }
        Cell R94Cell7 = row.createCell(9);
        if (record.getR94_BAL_SUB_BWP1() != null) {
            R94Cell7.setCellValue(record.getR94_BAL_SUB_BWP1().doubleValue());
            R94Cell7.setCellStyle(numberStyle);
        } else {
            R94Cell7.setCellValue("");
            R94Cell7.setCellStyle(textStyle);
        }
        Cell R94Cell8 = row.createCell(10);
        if (record.getR94_BAL_SUB_BWP2() != null) {
            R94Cell8.setCellValue(record.getR94_BAL_SUB_BWP2().doubleValue());
            R94Cell8.setCellStyle(numberStyle);
        } else {
            R94Cell8.setCellValue("");
            R94Cell8.setCellStyle(textStyle);
        }
        Cell R94Cell9 = row.createCell(11);
        if (record.getR94_BAL_ACT_SUB_BWP1() != null) {
            R94Cell9.setCellValue(record.getR94_BAL_ACT_SUB_BWP1().doubleValue());
            R94Cell9.setCellStyle(numberStyle);
        } else {
            R94Cell9.setCellValue("");
            R94Cell9.setCellStyle(textStyle);
        }
        Cell R94Cell10 = row.createCell(12);
        if (record.getR94_BAL_ACT_SUB_BWP2() != null) {
            R94Cell10.setCellValue(record.getR94_BAL_ACT_SUB_BWP2().doubleValue());
            R94Cell10.setCellStyle(numberStyle);
        } else {
            R94Cell10.setCellValue("");
            R94Cell10.setCellStyle(textStyle);
        }
        /* ================= R102 - R108 ================= */

        /* ================= R102 ================= */
        row = sheet.getRow(101);
        Cell R102Cell1 = row.createCell(3);
        if (record.getR102_FIG_BAL_BWP1() != null) {
            R102Cell1.setCellValue(record.getR102_FIG_BAL_BWP1().doubleValue());
            R102Cell1.setCellStyle(numberStyle);
        } else {
            R102Cell1.setCellValue("");
            R102Cell1.setCellStyle(textStyle);
        }
        Cell R102Cell2 = row.createCell(4);
        if (record.getR102_FIG_BAL_BWP2() != null) {
            R102Cell2.setCellValue(record.getR102_FIG_BAL_BWP2().doubleValue());
            R102Cell2.setCellStyle(numberStyle);
        } else {
            R102Cell2.setCellValue("");
            R102Cell2.setCellStyle(textStyle);
        }
        Cell R102Cell3 = row.createCell(5);
        if (record.getR102_AMT_ADJ_BWP1() != null) {
            R102Cell3.setCellValue(record.getR102_AMT_ADJ_BWP1().doubleValue());
            R102Cell3.setCellStyle(numberStyle);
        } else {
            R102Cell3.setCellValue("");
            R102Cell3.setCellStyle(textStyle);
        }
        Cell R102Cell4 = row.createCell(6);
        if (record.getR102_AMT_ADJ_BWP2() != null) {
            R102Cell4.setCellValue(record.getR102_AMT_ADJ_BWP2().doubleValue());
            R102Cell4.setCellStyle(numberStyle);
        } else {
            R102Cell4.setCellValue("");
            R102Cell4.setCellStyle(textStyle);
        }
        Cell R102Cell5 = row.createCell(7);
        if (record.getR102_NET_AMT_BWP1() != null) {
            R102Cell5.setCellValue(record.getR102_NET_AMT_BWP1().doubleValue());
            R102Cell5.setCellStyle(numberStyle);
        } else {
            R102Cell5.setCellValue("");
            R102Cell5.setCellStyle(textStyle);
        }
        Cell R102Cell6 = row.createCell(8);
        if (record.getR102_NET_AMT_BWP2() != null) {
            R102Cell6.setCellValue(record.getR102_NET_AMT_BWP2().doubleValue());
            R102Cell6.setCellStyle(numberStyle);
        } else {
            R102Cell6.setCellValue("");
            R102Cell6.setCellStyle(textStyle);
        }
        Cell R102Cell7 = row.createCell(9);
        if (record.getR102_BAL_SUB_BWP1() != null) {
            R102Cell7.setCellValue(record.getR102_BAL_SUB_BWP1().doubleValue());
            R102Cell7.setCellStyle(numberStyle);
        } else {
            R102Cell7.setCellValue("");
            R102Cell7.setCellStyle(textStyle);
        }
        Cell R102Cell8 = row.createCell(10);
        if (record.getR102_BAL_SUB_BWP2() != null) {
            R102Cell8.setCellValue(record.getR102_BAL_SUB_BWP2().doubleValue());
            R102Cell8.setCellStyle(numberStyle);
        } else {
            R102Cell8.setCellValue("");
            R102Cell8.setCellStyle(textStyle);
        }
        Cell R102Cell9 = row.createCell(11);
        if (record.getR102_BAL_ACT_SUB_BWP1() != null) {
            R102Cell9.setCellValue(record.getR102_BAL_ACT_SUB_BWP1().doubleValue());
            R102Cell9.setCellStyle(numberStyle);
        } else {
            R102Cell9.setCellValue("");
            R102Cell9.setCellStyle(textStyle);
        }
        Cell R102Cell10 = row.createCell(12);
        if (record.getR102_BAL_ACT_SUB_BWP2() != null) {
            R102Cell10.setCellValue(record.getR102_BAL_ACT_SUB_BWP2().doubleValue());
            R102Cell10.setCellStyle(numberStyle);
        } else {
            R102Cell10.setCellValue("");
            R102Cell10.setCellStyle(textStyle);
        }

        /* ================= R103 ================= */

        /* ================= R104 ================= */
        row = sheet.getRow(103);
        Cell R104Cell1 = row.createCell(3);
        if (record.getR104_FIG_BAL_BWP1() != null) {
            R104Cell1.setCellValue(record.getR104_FIG_BAL_BWP1().doubleValue());
            R104Cell1.setCellStyle(numberStyle);
        } else {
            R104Cell1.setCellValue("");
            R104Cell1.setCellStyle(textStyle);
        }
        Cell R104Cell2 = row.createCell(4);
        if (record.getR104_FIG_BAL_BWP2() != null) {
            R104Cell2.setCellValue(record.getR104_FIG_BAL_BWP2().doubleValue());
            R104Cell2.setCellStyle(numberStyle);
        } else {
            R104Cell2.setCellValue("");
            R104Cell2.setCellStyle(textStyle);
        }
        Cell R104Cell3 = row.createCell(5);
        if (record.getR104_AMT_ADJ_BWP1() != null) {
            R104Cell3.setCellValue(record.getR104_AMT_ADJ_BWP1().doubleValue());
            R104Cell3.setCellStyle(numberStyle);
        } else {
            R104Cell3.setCellValue("");
            R104Cell3.setCellStyle(textStyle);
        }
        Cell R104Cell4 = row.createCell(6);
        if (record.getR104_AMT_ADJ_BWP2() != null) {
            R104Cell4.setCellValue(record.getR104_AMT_ADJ_BWP2().doubleValue());
            R104Cell4.setCellStyle(numberStyle);
        } else {
            R104Cell4.setCellValue("");
            R104Cell4.setCellStyle(textStyle);
        }
        Cell R104Cell5 = row.createCell(7);
        if (record.getR104_NET_AMT_BWP1() != null) {
            R104Cell5.setCellValue(record.getR104_NET_AMT_BWP1().doubleValue());
            R104Cell5.setCellStyle(numberStyle);
        } else {
            R104Cell5.setCellValue("");
            R104Cell5.setCellStyle(textStyle);
        }
        Cell R104Cell6 = row.createCell(8);
        if (record.getR104_NET_AMT_BWP2() != null) {
            R104Cell6.setCellValue(record.getR104_NET_AMT_BWP2().doubleValue());
            R104Cell6.setCellStyle(numberStyle);
        } else {
            R104Cell6.setCellValue("");
            R104Cell6.setCellStyle(textStyle);
        }
        Cell R104Cell7 = row.createCell(9);
        if (record.getR104_BAL_SUB_BWP1() != null) {
            R104Cell7.setCellValue(record.getR104_BAL_SUB_BWP1().doubleValue());
            R104Cell7.setCellStyle(numberStyle);
        } else {
            R104Cell7.setCellValue("");
            R104Cell7.setCellStyle(textStyle);
        }
        Cell R104Cell8 = row.createCell(10);
        if (record.getR104_BAL_SUB_BWP2() != null) {
            R104Cell8.setCellValue(record.getR104_BAL_SUB_BWP2().doubleValue());
            R104Cell8.setCellStyle(numberStyle);
        } else {
            R104Cell8.setCellValue("");
            R104Cell8.setCellStyle(textStyle);
        }
        Cell R104Cell9 = row.createCell(11);
        if (record.getR104_BAL_ACT_SUB_BWP1() != null) {
            R104Cell9.setCellValue(record.getR104_BAL_ACT_SUB_BWP1().doubleValue());
            R104Cell9.setCellStyle(numberStyle);
        } else {
            R104Cell9.setCellValue("");
            R104Cell9.setCellStyle(textStyle);
        }
        Cell R104Cell10 = row.createCell(12);
        if (record.getR104_BAL_ACT_SUB_BWP2() != null) {
            R104Cell10.setCellValue(record.getR104_BAL_ACT_SUB_BWP2().doubleValue());
            R104Cell10.setCellStyle(numberStyle);
        } else {
            R104Cell10.setCellValue("");
            R104Cell10.setCellStyle(textStyle);
        }

        /* ================= R105 ================= */
        row = sheet.getRow(104);
        Cell R105Cell1 = row.createCell(3);
        if (record.getR105_FIG_BAL_BWP1() != null) {
            R105Cell1.setCellValue(record.getR105_FIG_BAL_BWP1().doubleValue());
            R105Cell1.setCellStyle(numberStyle);
        } else {
            R105Cell1.setCellValue("");
            R105Cell1.setCellStyle(textStyle);
        }
        Cell R105Cell2 = row.createCell(4);
        if (record.getR105_FIG_BAL_BWP2() != null) {
            R105Cell2.setCellValue(record.getR105_FIG_BAL_BWP2().doubleValue());
            R105Cell2.setCellStyle(numberStyle);
        } else {
            R105Cell2.setCellValue("");
            R105Cell2.setCellStyle(textStyle);
        }
        Cell R105Cell3 = row.createCell(5);
        if (record.getR105_AMT_ADJ_BWP1() != null) {
            R105Cell3.setCellValue(record.getR105_AMT_ADJ_BWP1().doubleValue());
            R105Cell3.setCellStyle(numberStyle);
        } else {
            R105Cell3.setCellValue("");
            R105Cell3.setCellStyle(textStyle);
        }
        Cell R105Cell4 = row.createCell(6);
        if (record.getR105_AMT_ADJ_BWP2() != null) {
            R105Cell4.setCellValue(record.getR105_AMT_ADJ_BWP2().doubleValue());
            R105Cell4.setCellStyle(numberStyle);
        } else {
            R105Cell4.setCellValue("");
            R105Cell4.setCellStyle(textStyle);
        }
        Cell R105Cell5 = row.createCell(7);
        if (record.getR105_NET_AMT_BWP1() != null) {
            R105Cell5.setCellValue(record.getR105_NET_AMT_BWP1().doubleValue());
            R105Cell5.setCellStyle(numberStyle);
        } else {
            R105Cell5.setCellValue("");
            R105Cell5.setCellStyle(textStyle);
        }
        Cell R105Cell6 = row.createCell(8);
        if (record.getR105_NET_AMT_BWP2() != null) {
            R105Cell6.setCellValue(record.getR105_NET_AMT_BWP2().doubleValue());
            R105Cell6.setCellStyle(numberStyle);
        } else {
            R105Cell6.setCellValue("");
            R105Cell6.setCellStyle(textStyle);
        }
        Cell R105Cell7 = row.createCell(9);
        if (record.getR105_BAL_SUB_BWP1() != null) {
            R105Cell7.setCellValue(record.getR105_BAL_SUB_BWP1().doubleValue());
            R105Cell7.setCellStyle(numberStyle);
        } else {
            R105Cell7.setCellValue("");
            R105Cell7.setCellStyle(textStyle);
        }
        Cell R105Cell8 = row.createCell(10);
        if (record.getR105_BAL_SUB_BWP2() != null) {
            R105Cell8.setCellValue(record.getR105_BAL_SUB_BWP2().doubleValue());
            R105Cell8.setCellStyle(numberStyle);
        } else {
            R105Cell8.setCellValue("");
            R105Cell8.setCellStyle(textStyle);
        }
        Cell R105Cell9 = row.createCell(11);
        if (record.getR105_BAL_ACT_SUB_BWP1() != null) {
            R105Cell9.setCellValue(record.getR105_BAL_ACT_SUB_BWP1().doubleValue());
            R105Cell9.setCellStyle(numberStyle);
        } else {
            R105Cell9.setCellValue("");
            R105Cell9.setCellStyle(textStyle);
        }
        Cell R105Cell10 = row.createCell(12);
        if (record.getR105_BAL_ACT_SUB_BWP2() != null) {
            R105Cell10.setCellValue(record.getR105_BAL_ACT_SUB_BWP2().doubleValue());
            R105Cell10.setCellStyle(numberStyle);
        } else {
            R105Cell10.setCellValue("");
            R105Cell10.setCellStyle(textStyle);
        }

        /* ================= R106 ================= */
        row = sheet.getRow(105);
        Cell R106Cell1 = row.createCell(3);
        if (record.getR106_FIG_BAL_BWP1() != null) {
            R106Cell1.setCellValue(record.getR106_FIG_BAL_BWP1().doubleValue());
            R106Cell1.setCellStyle(numberStyle);
        } else {
            R106Cell1.setCellValue("");
            R106Cell1.setCellStyle(textStyle);
        }
        Cell R106Cell2 = row.createCell(4);
        if (record.getR106_FIG_BAL_BWP2() != null) {
            R106Cell2.setCellValue(record.getR106_FIG_BAL_BWP2().doubleValue());
            R106Cell2.setCellStyle(numberStyle);
        } else {
            R106Cell2.setCellValue("");
            R106Cell2.setCellStyle(textStyle);
        }
        Cell R106Cell3 = row.createCell(5);
        if (record.getR106_AMT_ADJ_BWP1() != null) {
            R106Cell3.setCellValue(record.getR106_AMT_ADJ_BWP1().doubleValue());
            R106Cell3.setCellStyle(numberStyle);
        } else {
            R106Cell3.setCellValue("");
            R106Cell3.setCellStyle(textStyle);
        }
        Cell R106Cell4 = row.createCell(6);
        if (record.getR106_AMT_ADJ_BWP2() != null) {
            R106Cell4.setCellValue(record.getR106_AMT_ADJ_BWP2().doubleValue());
            R106Cell4.setCellStyle(numberStyle);
        } else {
            R106Cell4.setCellValue("");
            R106Cell4.setCellStyle(textStyle);
        }
        Cell R106Cell5 = row.createCell(7);
        if (record.getR106_NET_AMT_BWP1() != null) {
            R106Cell5.setCellValue(record.getR106_NET_AMT_BWP1().doubleValue());
            R106Cell5.setCellStyle(numberStyle);
        } else {
            R106Cell5.setCellValue("");
            R106Cell5.setCellStyle(textStyle);
        }
        Cell R106Cell6 = row.createCell(8);
        if (record.getR106_NET_AMT_BWP2() != null) {
            R106Cell6.setCellValue(record.getR106_NET_AMT_BWP2().doubleValue());
            R106Cell6.setCellStyle(numberStyle);
        } else {
            R106Cell6.setCellValue("");
            R106Cell6.setCellStyle(textStyle);
        }
        Cell R106Cell7 = row.createCell(9);
        if (record.getR106_BAL_SUB_BWP1() != null) {
            R106Cell7.setCellValue(record.getR106_BAL_SUB_BWP1().doubleValue());
            R106Cell7.setCellStyle(numberStyle);
        } else {
            R106Cell7.setCellValue("");
            R106Cell7.setCellStyle(textStyle);
        }
        Cell R106Cell8 = row.createCell(10);
        if (record.getR106_BAL_SUB_BWP2() != null) {
            R106Cell8.setCellValue(record.getR106_BAL_SUB_BWP2().doubleValue());
            R106Cell8.setCellStyle(numberStyle);
        } else {
            R106Cell8.setCellValue("");
            R106Cell8.setCellStyle(textStyle);
        }
        Cell R106Cell9 = row.createCell(11);
        if (record.getR106_BAL_ACT_SUB_BWP1() != null) {
            R106Cell9.setCellValue(record.getR106_BAL_ACT_SUB_BWP1().doubleValue());
            R106Cell9.setCellStyle(numberStyle);
        } else {
            R106Cell9.setCellValue("");
            R106Cell9.setCellStyle(textStyle);
        }
        Cell R106Cell10 = row.createCell(12);
        if (record.getR106_BAL_ACT_SUB_BWP2() != null) {
            R106Cell10.setCellValue(record.getR106_BAL_ACT_SUB_BWP2().doubleValue());
            R106Cell10.setCellStyle(numberStyle);
        } else {
            R106Cell10.setCellValue("");
            R106Cell10.setCellStyle(textStyle);
        }

        /* ================= R107 ================= */
        row = sheet.getRow(106);
        Cell R107Cell1 = row.createCell(3);
        if (record.getR107_FIG_BAL_BWP1() != null) {
            R107Cell1.setCellValue(record.getR107_FIG_BAL_BWP1().doubleValue());
            R107Cell1.setCellStyle(numberStyle);
        } else {
            R107Cell1.setCellValue("");
            R107Cell1.setCellStyle(textStyle);
        }
        Cell R107Cell2 = row.createCell(4);
        if (record.getR107_FIG_BAL_BWP2() != null) {
            R107Cell2.setCellValue(record.getR107_FIG_BAL_BWP2().doubleValue());
            R107Cell2.setCellStyle(numberStyle);
        } else {
            R107Cell2.setCellValue("");
            R107Cell2.setCellStyle(textStyle);
        }
        Cell R107Cell3 = row.createCell(5);
        if (record.getR107_AMT_ADJ_BWP1() != null) {
            R107Cell3.setCellValue(record.getR107_AMT_ADJ_BWP1().doubleValue());
            R107Cell3.setCellStyle(numberStyle);
        } else {
            R107Cell3.setCellValue("");
            R107Cell3.setCellStyle(textStyle);
        }
        Cell R107Cell4 = row.createCell(6);
        if (record.getR107_AMT_ADJ_BWP2() != null) {
            R107Cell4.setCellValue(record.getR107_AMT_ADJ_BWP2().doubleValue());
            R107Cell4.setCellStyle(numberStyle);
        } else {
            R107Cell4.setCellValue("");
            R107Cell4.setCellStyle(textStyle);
        }
        Cell R107Cell5 = row.createCell(7);
        if (record.getR107_NET_AMT_BWP1() != null) {
            R107Cell5.setCellValue(record.getR107_NET_AMT_BWP1().doubleValue());
            R107Cell5.setCellStyle(numberStyle);
        } else {
            R107Cell5.setCellValue("");
            R107Cell5.setCellStyle(textStyle);
        }
        Cell R107Cell6 = row.createCell(8);
        if (record.getR107_NET_AMT_BWP2() != null) {
            R107Cell6.setCellValue(record.getR107_NET_AMT_BWP2().doubleValue());
            R107Cell6.setCellStyle(numberStyle);
        } else {
            R107Cell6.setCellValue("");
            R107Cell6.setCellStyle(textStyle);
        }
        Cell R107Cell7 = row.createCell(9);
        if (record.getR107_BAL_SUB_BWP1() != null) {
            R107Cell7.setCellValue(record.getR107_BAL_SUB_BWP1().doubleValue());
            R107Cell7.setCellStyle(numberStyle);
        } else {
            R107Cell7.setCellValue("");
            R107Cell7.setCellStyle(textStyle);
        }
        Cell R107Cell8 = row.createCell(10);
        if (record.getR107_BAL_SUB_BWP2() != null) {
            R107Cell8.setCellValue(record.getR107_BAL_SUB_BWP2().doubleValue());
            R107Cell8.setCellStyle(numberStyle);
        } else {
            R107Cell8.setCellValue("");
            R107Cell8.setCellStyle(textStyle);
        }
        Cell R107Cell9 = row.createCell(11);
        if (record.getR107_BAL_ACT_SUB_BWP1() != null) {
            R107Cell9.setCellValue(record.getR107_BAL_ACT_SUB_BWP1().doubleValue());
            R107Cell9.setCellStyle(numberStyle);
        } else {
            R107Cell9.setCellValue("");
            R107Cell9.setCellStyle(textStyle);
        }
        Cell R107Cell10 = row.createCell(12);
        if (record.getR107_BAL_ACT_SUB_BWP2() != null) {
            R107Cell10.setCellValue(record.getR107_BAL_ACT_SUB_BWP2().doubleValue());
            R107Cell10.setCellStyle(numberStyle);
        } else {
            R107Cell10.setCellValue("");
            R107Cell10.setCellStyle(textStyle);
        }

        /* ================= R108 ================= */
        row = sheet.getRow(107);
        Cell R108Cell1 = row.createCell(3);
        if (record.getR108_FIG_BAL_BWP1() != null) {
            R108Cell1.setCellValue(record.getR108_FIG_BAL_BWP1().doubleValue());
            R108Cell1.setCellStyle(numberStyle);
        } else {
            R108Cell1.setCellValue("");
            R108Cell1.setCellStyle(textStyle);
        }
        Cell R108Cell2 = row.createCell(4);
        if (record.getR108_FIG_BAL_BWP2() != null) {
            R108Cell2.setCellValue(record.getR108_FIG_BAL_BWP2().doubleValue());
            R108Cell2.setCellStyle(numberStyle);
        } else {
            R108Cell2.setCellValue("");
            R108Cell2.setCellStyle(textStyle);
        }
        Cell R108Cell3 = row.createCell(5);
        if (record.getR108_AMT_ADJ_BWP1() != null) {
            R108Cell3.setCellValue(record.getR108_AMT_ADJ_BWP1().doubleValue());
            R108Cell3.setCellStyle(numberStyle);
        } else {
            R108Cell3.setCellValue("");
            R108Cell3.setCellStyle(textStyle);
        }
        Cell R108Cell4 = row.createCell(6);
        if (record.getR108_AMT_ADJ_BWP2() != null) {
            R108Cell4.setCellValue(record.getR108_AMT_ADJ_BWP2().doubleValue());
            R108Cell4.setCellStyle(numberStyle);
        } else {
            R108Cell4.setCellValue("");
            R108Cell4.setCellStyle(textStyle);
        }
        Cell R108Cell5 = row.createCell(7);
        if (record.getR108_NET_AMT_BWP1() != null) {
            R108Cell5.setCellValue(record.getR108_NET_AMT_BWP1().doubleValue());
            R108Cell5.setCellStyle(numberStyle);
        } else {
            R108Cell5.setCellValue("");
            R108Cell5.setCellStyle(textStyle);
        }
        Cell R108Cell6 = row.createCell(8);
        if (record.getR108_NET_AMT_BWP2() != null) {
            R108Cell6.setCellValue(record.getR108_NET_AMT_BWP2().doubleValue());
            R108Cell6.setCellStyle(numberStyle);
        } else {
            R108Cell6.setCellValue("");
            R108Cell6.setCellStyle(textStyle);
        }
        Cell R108Cell7 = row.createCell(9);
        if (record.getR108_BAL_SUB_BWP1() != null) {
            R108Cell7.setCellValue(record.getR108_BAL_SUB_BWP1().doubleValue());
            R108Cell7.setCellStyle(numberStyle);
        } else {
            R108Cell7.setCellValue("");
            R108Cell7.setCellStyle(textStyle);
        }
        Cell R108Cell8 = row.createCell(10);
        if (record.getR108_BAL_SUB_BWP2() != null) {
            R108Cell8.setCellValue(record.getR108_BAL_SUB_BWP2().doubleValue());
            R108Cell8.setCellStyle(numberStyle);
        } else {
            R108Cell8.setCellValue("");
            R108Cell8.setCellStyle(textStyle);
        }
        Cell R108Cell9 = row.createCell(11);
        if (record.getR108_BAL_ACT_SUB_BWP1() != null) {
            R108Cell9.setCellValue(record.getR108_BAL_ACT_SUB_BWP1().doubleValue());
            R108Cell9.setCellStyle(numberStyle);
        } else {
            R108Cell9.setCellValue("");
            R108Cell9.setCellStyle(textStyle);
        }
        Cell R108Cell10 = row.createCell(12);
        if (record.getR108_BAL_ACT_SUB_BWP2() != null) {
            R108Cell10.setCellValue(record.getR108_BAL_ACT_SUB_BWP2().doubleValue());
            R108Cell10.setCellStyle(numberStyle);
        } else {
            R108Cell10.setCellValue("");
            R108Cell10.setCellStyle(textStyle);
        }
    }

    private void populateEntity2Data(Sheet sheet, GL_SCH_Summary_Entity2 record1, CellStyle textStyle,
            CellStyle numberStyle) {

        Row row = sheet.getRow(112) != null ? sheet.getRow(112) : sheet.createRow(112);
        /* ================= R113 ================= */
        row = sheet.getRow(112);
        Cell R113Cell1 = row.createCell(3);
        if (record1.getR113_FIG_BAL_BWP1() != null) {
            R113Cell1.setCellValue(record1.getR113_FIG_BAL_BWP1().doubleValue());
            R113Cell1.setCellStyle(numberStyle);
        } else {
            R113Cell1.setCellValue("");
            R113Cell1.setCellStyle(textStyle);
        }
        Cell R113Cell2 = row.createCell(4);
        if (record1.getR113_FIG_BAL_BWP2() != null) {
            R113Cell2.setCellValue(record1.getR113_FIG_BAL_BWP2().doubleValue());
            R113Cell2.setCellStyle(numberStyle);
        } else {
            R113Cell2.setCellValue("");
            R113Cell2.setCellStyle(textStyle);
        }
        Cell R113Cell3 = row.createCell(5);
        if (record1.getR113_AMT_ADJ_BWP1() != null) {
            R113Cell3.setCellValue(record1.getR113_AMT_ADJ_BWP1().doubleValue());
            R113Cell3.setCellStyle(numberStyle);
        } else {
            R113Cell3.setCellValue("");
            R113Cell3.setCellStyle(textStyle);
        }
        Cell R113Cell4 = row.createCell(6);
        if (record1.getR113_AMT_ADJ_BWP2() != null) {
            R113Cell4.setCellValue(record1.getR113_AMT_ADJ_BWP2().doubleValue());
            R113Cell4.setCellStyle(numberStyle);
        } else {
            R113Cell4.setCellValue("");
            R113Cell4.setCellStyle(textStyle);
        }
        Cell R113Cell5 = row.createCell(7);
        if (record1.getR113_NET_AMT_BWP1() != null) {
            R113Cell5.setCellValue(record1.getR113_NET_AMT_BWP1().doubleValue());
            R113Cell5.setCellStyle(numberStyle);
        } else {
            R113Cell5.setCellValue("");
            R113Cell5.setCellStyle(textStyle);
        }
        Cell R113Cell6 = row.createCell(8);
        if (record1.getR113_NET_AMT_BWP2() != null) {
            R113Cell6.setCellValue(record1.getR113_NET_AMT_BWP2().doubleValue());
            R113Cell6.setCellStyle(numberStyle);
        } else {
            R113Cell6.setCellValue("");
            R113Cell6.setCellStyle(textStyle);
        }
        Cell R113Cell7 = row.createCell(9);
        if (record1.getR113_BAL_SUB_BWP1() != null) {
            R113Cell7.setCellValue(record1.getR113_BAL_SUB_BWP1().doubleValue());
            R113Cell7.setCellStyle(numberStyle);
        } else {
            R113Cell7.setCellValue("");
            R113Cell7.setCellStyle(textStyle);
        }
        Cell R113Cell8 = row.createCell(10);
        if (record1.getR113_BAL_SUB_BWP2() != null) {
            R113Cell8.setCellValue(record1.getR113_BAL_SUB_BWP2().doubleValue());
            R113Cell8.setCellStyle(numberStyle);
        } else {
            R113Cell8.setCellValue("");
            R113Cell8.setCellStyle(textStyle);
        }
        Cell R113Cell9 = row.createCell(11);
        if (record1.getR113_BAL_ACT_SUB_BWP1() != null) {
            R113Cell9.setCellValue(record1.getR113_BAL_ACT_SUB_BWP1().doubleValue());
            R113Cell9.setCellStyle(numberStyle);
        } else {
            R113Cell9.setCellValue("");
            R113Cell9.setCellStyle(textStyle);
        }
        Cell R113Cell10 = row.createCell(12);
        if (record1.getR113_BAL_ACT_SUB_BWP2() != null) {
            R113Cell10.setCellValue(record1.getR113_BAL_ACT_SUB_BWP2().doubleValue());
            R113Cell10.setCellStyle(numberStyle);
        } else {
            R113Cell10.setCellValue("");
            R113Cell10.setCellStyle(textStyle);
        }

        /* ================= R114 ================= */
        row = sheet.getRow(113);
        Cell R114Cell1 = row.createCell(3);
        if (record1.getR114_FIG_BAL_BWP1() != null) {
            R114Cell1.setCellValue(record1.getR114_FIG_BAL_BWP1().doubleValue());
            R114Cell1.setCellStyle(numberStyle);
        } else {
            R114Cell1.setCellValue("");
            R114Cell1.setCellStyle(textStyle);
        }
        Cell R114Cell2 = row.createCell(4);
        if (record1.getR114_FIG_BAL_BWP2() != null) {
            R114Cell2.setCellValue(record1.getR114_FIG_BAL_BWP2().doubleValue());
            R114Cell2.setCellStyle(numberStyle);
        } else {
            R114Cell2.setCellValue("");
            R114Cell2.setCellStyle(textStyle);
        }
        Cell R114Cell3 = row.createCell(5);
        if (record1.getR114_AMT_ADJ_BWP1() != null) {
            R114Cell3.setCellValue(record1.getR114_AMT_ADJ_BWP1().doubleValue());
            R114Cell3.setCellStyle(numberStyle);
        } else {
            R114Cell3.setCellValue("");
            R114Cell3.setCellStyle(textStyle);
        }
        Cell R114Cell4 = row.createCell(6);
        if (record1.getR114_AMT_ADJ_BWP2() != null) {
            R114Cell4.setCellValue(record1.getR114_AMT_ADJ_BWP2().doubleValue());
            R114Cell4.setCellStyle(numberStyle);
        } else {
            R114Cell4.setCellValue("");
            R114Cell4.setCellStyle(textStyle);
        }
        Cell R114Cell5 = row.createCell(7);
        if (record1.getR114_NET_AMT_BWP1() != null) {
            R114Cell5.setCellValue(record1.getR114_NET_AMT_BWP1().doubleValue());
            R114Cell5.setCellStyle(numberStyle);
        } else {
            R114Cell5.setCellValue("");
            R114Cell5.setCellStyle(textStyle);
        }
        Cell R114Cell6 = row.createCell(8);
        if (record1.getR114_NET_AMT_BWP2() != null) {
            R114Cell6.setCellValue(record1.getR114_NET_AMT_BWP2().doubleValue());
            R114Cell6.setCellStyle(numberStyle);
        } else {
            R114Cell6.setCellValue("");
            R114Cell6.setCellStyle(textStyle);
        }
        Cell R114Cell7 = row.createCell(9);
        if (record1.getR114_BAL_SUB_BWP1() != null) {
            R114Cell7.setCellValue(record1.getR114_BAL_SUB_BWP1().doubleValue());
            R114Cell7.setCellStyle(numberStyle);
        } else {
            R114Cell7.setCellValue("");
            R114Cell7.setCellStyle(textStyle);
        }
        Cell R114Cell8 = row.createCell(10);
        if (record1.getR114_BAL_SUB_BWP2() != null) {
            R114Cell8.setCellValue(record1.getR114_BAL_SUB_BWP2().doubleValue());
            R114Cell8.setCellStyle(numberStyle);
        } else {
            R114Cell8.setCellValue("");
            R114Cell8.setCellStyle(textStyle);
        }
        Cell R114Cell9 = row.createCell(11);
        if (record1.getR114_BAL_ACT_SUB_BWP1() != null) {
            R114Cell9.setCellValue(record1.getR114_BAL_ACT_SUB_BWP1().doubleValue());
            R114Cell9.setCellStyle(numberStyle);
        } else {
            R114Cell9.setCellValue("");
            R114Cell9.setCellStyle(textStyle);
        }
        Cell R114Cell10 = row.createCell(12);
        if (record1.getR114_BAL_ACT_SUB_BWP2() != null) {
            R114Cell10.setCellValue(record1.getR114_BAL_ACT_SUB_BWP2().doubleValue());
            R114Cell10.setCellStyle(numberStyle);
        } else {
            R114Cell10.setCellValue("");
            R114Cell10.setCellStyle(textStyle);
        }

        /* ================= R115 ================= */
        row = sheet.getRow(114);
        Cell R115Cell1 = row.createCell(3);
        if (record1.getR115_FIG_BAL_BWP1() != null) {
            R115Cell1.setCellValue(record1.getR115_FIG_BAL_BWP1().doubleValue());
            R115Cell1.setCellStyle(numberStyle);
        } else {
            R115Cell1.setCellValue("");
            R115Cell1.setCellStyle(textStyle);
        }
        Cell R115Cell2 = row.createCell(4);
        if (record1.getR115_FIG_BAL_BWP2() != null) {
            R115Cell2.setCellValue(record1.getR115_FIG_BAL_BWP2().doubleValue());
            R115Cell2.setCellStyle(numberStyle);
        } else {
            R115Cell2.setCellValue("");
            R115Cell2.setCellStyle(textStyle);
        }
        Cell R115Cell3 = row.createCell(5);
        if (record1.getR115_AMT_ADJ_BWP1() != null) {
            R115Cell3.setCellValue(record1.getR115_AMT_ADJ_BWP1().doubleValue());
            R115Cell3.setCellStyle(numberStyle);
        } else {
            R115Cell3.setCellValue("");
            R115Cell3.setCellStyle(textStyle);
        }
        Cell R115Cell4 = row.createCell(6);
        if (record1.getR115_AMT_ADJ_BWP2() != null) {
            R115Cell4.setCellValue(record1.getR115_AMT_ADJ_BWP2().doubleValue());
            R115Cell4.setCellStyle(numberStyle);
        } else {
            R115Cell4.setCellValue("");
            R115Cell4.setCellStyle(textStyle);
        }
        Cell R115Cell5 = row.createCell(7);
        if (record1.getR115_NET_AMT_BWP1() != null) {
            R115Cell5.setCellValue(record1.getR115_NET_AMT_BWP1().doubleValue());
            R115Cell5.setCellStyle(numberStyle);
        } else {
            R115Cell5.setCellValue("");
            R115Cell5.setCellStyle(textStyle);
        }
        Cell R115Cell6 = row.createCell(8);
        if (record1.getR115_NET_AMT_BWP2() != null) {
            R115Cell6.setCellValue(record1.getR115_NET_AMT_BWP2().doubleValue());
            R115Cell6.setCellStyle(numberStyle);
        } else {
            R115Cell6.setCellValue("");
            R115Cell6.setCellStyle(textStyle);
        }
        Cell R115Cell7 = row.createCell(9);
        if (record1.getR115_BAL_SUB_BWP1() != null) {
            R115Cell7.setCellValue(record1.getR115_BAL_SUB_BWP1().doubleValue());
            R115Cell7.setCellStyle(numberStyle);
        } else {
            R115Cell7.setCellValue("");
            R115Cell7.setCellStyle(textStyle);
        }
        Cell R115Cell8 = row.createCell(10);
        if (record1.getR115_BAL_SUB_BWP2() != null) {
            R115Cell8.setCellValue(record1.getR115_BAL_SUB_BWP2().doubleValue());
            R115Cell8.setCellStyle(numberStyle);
        } else {
            R115Cell8.setCellValue("");
            R115Cell8.setCellStyle(textStyle);
        }
        Cell R115Cell9 = row.createCell(11);
        if (record1.getR115_BAL_ACT_SUB_BWP1() != null) {
            R115Cell9.setCellValue(record1.getR115_BAL_ACT_SUB_BWP1().doubleValue());
            R115Cell9.setCellStyle(numberStyle);
        } else {
            R115Cell9.setCellValue("");
            R115Cell9.setCellStyle(textStyle);
        }
        Cell R115Cell10 = row.createCell(12);
        if (record1.getR115_BAL_ACT_SUB_BWP2() != null) {
            R115Cell10.setCellValue(record1.getR115_BAL_ACT_SUB_BWP2().doubleValue());
            R115Cell10.setCellStyle(numberStyle);
        } else {
            R115Cell10.setCellValue("");
            R115Cell10.setCellStyle(textStyle);
        }

        /* ================= R116 ================= */
        row = sheet.getRow(115);
        Cell R116Cell1 = row.createCell(3);
        if (record1.getR116_FIG_BAL_BWP1() != null) {
            R116Cell1.setCellValue(record1.getR116_FIG_BAL_BWP1().doubleValue());
            R116Cell1.setCellStyle(numberStyle);
        } else {
            R116Cell1.setCellValue("");
            R116Cell1.setCellStyle(textStyle);
        }
        Cell R116Cell2 = row.createCell(4);
        if (record1.getR116_FIG_BAL_BWP2() != null) {
            R116Cell2.setCellValue(record1.getR116_FIG_BAL_BWP2().doubleValue());
            R116Cell2.setCellStyle(numberStyle);
        } else {
            R116Cell2.setCellValue("");
            R116Cell2.setCellStyle(textStyle);
        }
        Cell R116Cell3 = row.createCell(5);
        if (record1.getR116_AMT_ADJ_BWP1() != null) {
            R116Cell3.setCellValue(record1.getR116_AMT_ADJ_BWP1().doubleValue());
            R116Cell3.setCellStyle(numberStyle);
        } else {
            R116Cell3.setCellValue("");
            R116Cell3.setCellStyle(textStyle);
        }
        Cell R116Cell4 = row.createCell(6);
        if (record1.getR116_AMT_ADJ_BWP2() != null) {
            R116Cell4.setCellValue(record1.getR116_AMT_ADJ_BWP2().doubleValue());
            R116Cell4.setCellStyle(numberStyle);
        } else {
            R116Cell4.setCellValue("");
            R116Cell4.setCellStyle(textStyle);
        }
        Cell R116Cell5 = row.createCell(7);
        if (record1.getR116_NET_AMT_BWP1() != null) {
            R116Cell5.setCellValue(record1.getR116_NET_AMT_BWP1().doubleValue());
            R116Cell5.setCellStyle(numberStyle);
        } else {
            R116Cell5.setCellValue("");
            R116Cell5.setCellStyle(textStyle);
        }
        Cell R116Cell6 = row.createCell(8);
        if (record1.getR116_NET_AMT_BWP2() != null) {
            R116Cell6.setCellValue(record1.getR116_NET_AMT_BWP2().doubleValue());
            R116Cell6.setCellStyle(numberStyle);
        } else {
            R116Cell6.setCellValue("");
            R116Cell6.setCellStyle(textStyle);
        }
        Cell R116Cell7 = row.createCell(9);
        if (record1.getR116_BAL_SUB_BWP1() != null) {
            R116Cell7.setCellValue(record1.getR116_BAL_SUB_BWP1().doubleValue());
            R116Cell7.setCellStyle(numberStyle);
        } else {
            R116Cell7.setCellValue("");
            R116Cell7.setCellStyle(textStyle);
        }
        Cell R116Cell8 = row.createCell(10);
        if (record1.getR116_BAL_SUB_BWP2() != null) {
            R116Cell8.setCellValue(record1.getR116_BAL_SUB_BWP2().doubleValue());
            R116Cell8.setCellStyle(numberStyle);
        } else {
            R116Cell8.setCellValue("");
            R116Cell8.setCellStyle(textStyle);
        }
        Cell R116Cell9 = row.createCell(11);
        if (record1.getR116_BAL_ACT_SUB_BWP1() != null) {
            R116Cell9.setCellValue(record1.getR116_BAL_ACT_SUB_BWP1().doubleValue());
            R116Cell9.setCellStyle(numberStyle);
        } else {
            R116Cell9.setCellValue("");
            R116Cell9.setCellStyle(textStyle);
        }
        Cell R116Cell10 = row.createCell(12);
        if (record1.getR116_BAL_ACT_SUB_BWP2() != null) {
            R116Cell10.setCellValue(record1.getR116_BAL_ACT_SUB_BWP2().doubleValue());
            R116Cell10.setCellStyle(numberStyle);
        } else {
            R116Cell10.setCellValue("");
            R116Cell10.setCellStyle(textStyle);
        }

        /* ================= R117 ================= */
        row = sheet.getRow(116);
        Cell R117Cell1 = row.createCell(3);
        if (record1.getR117_FIG_BAL_BWP1() != null) {
            R117Cell1.setCellValue(record1.getR117_FIG_BAL_BWP1().doubleValue());
            R117Cell1.setCellStyle(numberStyle);
        } else {
            R117Cell1.setCellValue("");
            R117Cell1.setCellStyle(textStyle);
        }
        Cell R117Cell2 = row.createCell(4);
        if (record1.getR117_FIG_BAL_BWP2() != null) {
            R117Cell2.setCellValue(record1.getR117_FIG_BAL_BWP2().doubleValue());
            R117Cell2.setCellStyle(numberStyle);
        } else {
            R117Cell2.setCellValue("");
            R117Cell2.setCellStyle(textStyle);
        }
        Cell R117Cell3 = row.createCell(5);
        if (record1.getR117_AMT_ADJ_BWP1() != null) {
            R117Cell3.setCellValue(record1.getR117_AMT_ADJ_BWP1().doubleValue());
            R117Cell3.setCellStyle(numberStyle);
        } else {
            R117Cell3.setCellValue("");
            R117Cell3.setCellStyle(textStyle);
        }
        Cell R117Cell4 = row.createCell(6);
        if (record1.getR117_AMT_ADJ_BWP2() != null) {
            R117Cell4.setCellValue(record1.getR117_AMT_ADJ_BWP2().doubleValue());
            R117Cell4.setCellStyle(numberStyle);
        } else {
            R117Cell4.setCellValue("");
            R117Cell4.setCellStyle(textStyle);
        }
        Cell R117Cell5 = row.createCell(7);
        if (record1.getR117_NET_AMT_BWP1() != null) {
            R117Cell5.setCellValue(record1.getR117_NET_AMT_BWP1().doubleValue());
            R117Cell5.setCellStyle(numberStyle);
        } else {
            R117Cell5.setCellValue("");
            R117Cell5.setCellStyle(textStyle);
        }
        Cell R117Cell6 = row.createCell(8);
        if (record1.getR117_NET_AMT_BWP2() != null) {
            R117Cell6.setCellValue(record1.getR117_NET_AMT_BWP2().doubleValue());
            R117Cell6.setCellStyle(numberStyle);
        } else {
            R117Cell6.setCellValue("");
            R117Cell6.setCellStyle(textStyle);
        }
        Cell R117Cell7 = row.createCell(9);
        if (record1.getR117_BAL_SUB_BWP1() != null) {
            R117Cell7.setCellValue(record1.getR117_BAL_SUB_BWP1().doubleValue());
            R117Cell7.setCellStyle(numberStyle);
        } else {
            R117Cell7.setCellValue("");
            R117Cell7.setCellStyle(textStyle);
        }
        Cell R117Cell8 = row.createCell(10);
        if (record1.getR117_BAL_SUB_BWP2() != null) {
            R117Cell8.setCellValue(record1.getR117_BAL_SUB_BWP2().doubleValue());
            R117Cell8.setCellStyle(numberStyle);
        } else {
            R117Cell8.setCellValue("");
            R117Cell8.setCellStyle(textStyle);
        }
        Cell R117Cell9 = row.createCell(11);
        if (record1.getR117_BAL_ACT_SUB_BWP1() != null) {
            R117Cell9.setCellValue(record1.getR117_BAL_ACT_SUB_BWP1().doubleValue());
            R117Cell9.setCellStyle(numberStyle);
        } else {
            R117Cell9.setCellValue("");
            R117Cell9.setCellStyle(textStyle);
        }
        Cell R117Cell10 = row.createCell(12);
        if (record1.getR117_BAL_ACT_SUB_BWP2() != null) {
            R117Cell10.setCellValue(record1.getR117_BAL_ACT_SUB_BWP2().doubleValue());
            R117Cell10.setCellStyle(numberStyle);
        } else {
            R117Cell10.setCellValue("");
            R117Cell10.setCellStyle(textStyle);
        }

        /* ================= R127 ================= */
        row = sheet.getRow(126);
        Cell R127Cell1 = row.createCell(3);
        if (record1.getR127_FIG_BAL_BWP1() != null) {
            R127Cell1.setCellValue(record1.getR127_FIG_BAL_BWP1().doubleValue());
            R127Cell1.setCellStyle(numberStyle);
        } else {
            R127Cell1.setCellValue("");
            R127Cell1.setCellStyle(textStyle);
        }
        Cell R127Cell2 = row.createCell(4);
        if (record1.getR127_FIG_BAL_BWP2() != null) {
            R127Cell2.setCellValue(record1.getR127_FIG_BAL_BWP2().doubleValue());
            R127Cell2.setCellStyle(numberStyle);
        } else {
            R127Cell2.setCellValue("");
            R127Cell2.setCellStyle(textStyle);
        }
        Cell R127Cell3 = row.createCell(5);
        if (record1.getR127_AMT_ADJ_BWP1() != null) {
            R127Cell3.setCellValue(record1.getR127_AMT_ADJ_BWP1().doubleValue());
            R127Cell3.setCellStyle(numberStyle);
        } else {
            R127Cell3.setCellValue("");
            R127Cell3.setCellStyle(textStyle);
        }
        Cell R127Cell4 = row.createCell(6);
        if (record1.getR127_AMT_ADJ_BWP2() != null) {
            R127Cell4.setCellValue(record1.getR127_AMT_ADJ_BWP2().doubleValue());
            R127Cell4.setCellStyle(numberStyle);
        } else {
            R127Cell4.setCellValue("");
            R127Cell4.setCellStyle(textStyle);
        }
        Cell R127Cell5 = row.createCell(7);
        if (record1.getR127_NET_AMT_BWP1() != null) {
            R127Cell5.setCellValue(record1.getR127_NET_AMT_BWP1().doubleValue());
            R127Cell5.setCellStyle(numberStyle);
        } else {
            R127Cell5.setCellValue("");
            R127Cell5.setCellStyle(textStyle);
        }
        Cell R127Cell6 = row.createCell(8);
        if (record1.getR127_NET_AMT_BWP2() != null) {
            R127Cell6.setCellValue(record1.getR127_NET_AMT_BWP2().doubleValue());
            R127Cell6.setCellStyle(numberStyle);
        } else {
            R127Cell6.setCellValue("");
            R127Cell6.setCellStyle(textStyle);
        }
        Cell R127Cell7 = row.createCell(9);
        if (record1.getR127_BAL_SUB_BWP1() != null) {
            R127Cell7.setCellValue(record1.getR127_BAL_SUB_BWP1().doubleValue());
            R127Cell7.setCellStyle(numberStyle);
        } else {
            R127Cell7.setCellValue("");
            R127Cell7.setCellStyle(textStyle);
        }
        Cell R127Cell8 = row.createCell(10);
        if (record1.getR127_BAL_SUB_BWP2() != null) {
            R127Cell8.setCellValue(record1.getR127_BAL_SUB_BWP2().doubleValue());
            R127Cell8.setCellStyle(numberStyle);
        } else {
            R127Cell8.setCellValue("");
            R127Cell8.setCellStyle(textStyle);
        }
        Cell R127Cell9 = row.createCell(11);
        if (record1.getR127_BAL_ACT_SUB_BWP1() != null) {
            R127Cell9.setCellValue(record1.getR127_BAL_ACT_SUB_BWP1().doubleValue());
            R127Cell9.setCellStyle(numberStyle);
        } else {
            R127Cell9.setCellValue("");
            R127Cell9.setCellStyle(textStyle);
        }
        Cell R127Cell10 = row.createCell(12);
        if (record1.getR127_BAL_ACT_SUB_BWP2() != null) {
            R127Cell10.setCellValue(record1.getR127_BAL_ACT_SUB_BWP2().doubleValue());
            R127Cell10.setCellStyle(numberStyle);
        } else {
            R127Cell10.setCellValue("");
            R127Cell10.setCellStyle(textStyle);
        }

        /* ================= R128 ================= */
        row = sheet.getRow(127);
        Cell R128Cell1 = row.createCell(3);
        if (record1.getR128_FIG_BAL_BWP1() != null) {
            R128Cell1.setCellValue(record1.getR128_FIG_BAL_BWP1().doubleValue());
            R128Cell1.setCellStyle(numberStyle);
        } else {
            R128Cell1.setCellValue("");
            R128Cell1.setCellStyle(textStyle);
        }
        Cell R128Cell2 = row.createCell(4);
        if (record1.getR128_FIG_BAL_BWP2() != null) {
            R128Cell2.setCellValue(record1.getR128_FIG_BAL_BWP2().doubleValue());
            R128Cell2.setCellStyle(numberStyle);
        } else {
            R128Cell2.setCellValue("");
            R128Cell2.setCellStyle(textStyle);
        }
        Cell R128Cell3 = row.createCell(5);
        if (record1.getR128_AMT_ADJ_BWP1() != null) {
            R128Cell3.setCellValue(record1.getR128_AMT_ADJ_BWP1().doubleValue());
            R128Cell3.setCellStyle(numberStyle);
        } else {
            R128Cell3.setCellValue("");
            R128Cell3.setCellStyle(textStyle);
        }
        Cell R128Cell4 = row.createCell(6);
        if (record1.getR128_AMT_ADJ_BWP2() != null) {
            R128Cell4.setCellValue(record1.getR128_AMT_ADJ_BWP2().doubleValue());
            R128Cell4.setCellStyle(numberStyle);
        } else {
            R128Cell4.setCellValue("");
            R128Cell4.setCellStyle(textStyle);
        }
        Cell R128Cell5 = row.createCell(7);
        if (record1.getR128_NET_AMT_BWP1() != null) {
            R128Cell5.setCellValue(record1.getR128_NET_AMT_BWP1().doubleValue());
            R128Cell5.setCellStyle(numberStyle);
        } else {
            R128Cell5.setCellValue("");
            R128Cell5.setCellStyle(textStyle);
        }
        Cell R128Cell6 = row.createCell(8);
        if (record1.getR128_NET_AMT_BWP2() != null) {
            R128Cell6.setCellValue(record1.getR128_NET_AMT_BWP2().doubleValue());
            R128Cell6.setCellStyle(numberStyle);
        } else {
            R128Cell6.setCellValue("");
            R128Cell6.setCellStyle(textStyle);
        }
        Cell R128Cell7 = row.createCell(9);
        if (record1.getR128_BAL_SUB_BWP1() != null) {
            R128Cell7.setCellValue(record1.getR128_BAL_SUB_BWP1().doubleValue());
            R128Cell7.setCellStyle(numberStyle);
        } else {
            R128Cell7.setCellValue("");
            R128Cell7.setCellStyle(textStyle);
        }
        Cell R128Cell8 = row.createCell(10);
        if (record1.getR128_BAL_SUB_BWP2() != null) {
            R128Cell8.setCellValue(record1.getR128_BAL_SUB_BWP2().doubleValue());
            R128Cell8.setCellStyle(numberStyle);
        } else {
            R128Cell8.setCellValue("");
            R128Cell8.setCellStyle(textStyle);
        }
        Cell R128Cell9 = row.createCell(11);
        if (record1.getR128_BAL_ACT_SUB_BWP1() != null) {
            R128Cell9.setCellValue(record1.getR128_BAL_ACT_SUB_BWP1().doubleValue());
            R128Cell9.setCellStyle(numberStyle);
        } else {
            R128Cell9.setCellValue("");
            R128Cell9.setCellStyle(textStyle);
        }
        Cell R128Cell10 = row.createCell(12);
        if (record1.getR128_BAL_ACT_SUB_BWP2() != null) {
            R128Cell10.setCellValue(record1.getR128_BAL_ACT_SUB_BWP2().doubleValue());
            R128Cell10.setCellStyle(numberStyle);
        } else {
            R128Cell10.setCellValue("");
            R128Cell10.setCellStyle(textStyle);
        }

        /* ================= R129 ================= */
        row = sheet.getRow(128);
        Cell R129Cell1 = row.createCell(3);
        if (record1.getR129_FIG_BAL_BWP1() != null) {
            R129Cell1.setCellValue(record1.getR129_FIG_BAL_BWP1().doubleValue());
            R129Cell1.setCellStyle(numberStyle);
        } else {
            R129Cell1.setCellValue("");
            R129Cell1.setCellStyle(textStyle);
        }
        Cell R129Cell2 = row.createCell(4);
        if (record1.getR129_FIG_BAL_BWP2() != null) {
            R129Cell2.setCellValue(record1.getR129_FIG_BAL_BWP2().doubleValue());
            R129Cell2.setCellStyle(numberStyle);
        } else {
            R129Cell2.setCellValue("");
            R129Cell2.setCellStyle(textStyle);
        }
        Cell R129Cell3 = row.createCell(5);
        if (record1.getR129_AMT_ADJ_BWP1() != null) {
            R129Cell3.setCellValue(record1.getR129_AMT_ADJ_BWP1().doubleValue());
            R129Cell3.setCellStyle(numberStyle);
        } else {
            R129Cell3.setCellValue("");
            R129Cell3.setCellStyle(textStyle);
        }
        Cell R129Cell4 = row.createCell(6);
        if (record1.getR129_AMT_ADJ_BWP2() != null) {
            R129Cell4.setCellValue(record1.getR129_AMT_ADJ_BWP2().doubleValue());
            R129Cell4.setCellStyle(numberStyle);
        } else {
            R129Cell4.setCellValue("");
            R129Cell4.setCellStyle(textStyle);
        }
        Cell R129Cell5 = row.createCell(7);
        if (record1.getR129_NET_AMT_BWP1() != null) {
            R129Cell5.setCellValue(record1.getR129_NET_AMT_BWP1().doubleValue());
            R129Cell5.setCellStyle(numberStyle);
        } else {
            R129Cell5.setCellValue("");
            R129Cell5.setCellStyle(textStyle);
        }
        Cell R129Cell6 = row.createCell(8);
        if (record1.getR129_NET_AMT_BWP2() != null) {
            R129Cell6.setCellValue(record1.getR129_NET_AMT_BWP2().doubleValue());
            R129Cell6.setCellStyle(numberStyle);
        } else {
            R129Cell6.setCellValue("");
            R129Cell6.setCellStyle(textStyle);
        }
        Cell R129Cell7 = row.createCell(9);
        if (record1.getR129_BAL_SUB_BWP1() != null) {
            R129Cell7.setCellValue(record1.getR129_BAL_SUB_BWP1().doubleValue());
            R129Cell7.setCellStyle(numberStyle);
        } else {
            R129Cell7.setCellValue("");
            R129Cell7.setCellStyle(textStyle);
        }
        Cell R129Cell8 = row.createCell(10);
        if (record1.getR129_BAL_SUB_BWP2() != null) {
            R129Cell8.setCellValue(record1.getR129_BAL_SUB_BWP2().doubleValue());
            R129Cell8.setCellStyle(numberStyle);
        } else {
            R129Cell8.setCellValue("");
            R129Cell8.setCellStyle(textStyle);
        }
        Cell R129Cell9 = row.createCell(11);
        if (record1.getR129_BAL_ACT_SUB_BWP1() != null) {
            R129Cell9.setCellValue(record1.getR129_BAL_ACT_SUB_BWP1().doubleValue());
            R129Cell9.setCellStyle(numberStyle);
        } else {
            R129Cell9.setCellValue("");
            R129Cell9.setCellStyle(textStyle);
        }
        Cell R129Cell10 = row.createCell(12);
        if (record1.getR129_BAL_ACT_SUB_BWP2() != null) {
            R129Cell10.setCellValue(record1.getR129_BAL_ACT_SUB_BWP2().doubleValue());
            R129Cell10.setCellStyle(numberStyle);
        } else {
            R129Cell10.setCellValue("");
            R129Cell10.setCellStyle(textStyle);
        }

        /* ================= R130 ================= */

        /* ================= R131 ================= */
        row = sheet.getRow(130);
        Cell R131Cell1 = row.createCell(3);
        if (record1.getR131_FIG_BAL_BWP1() != null) {
            R131Cell1.setCellValue(record1.getR131_FIG_BAL_BWP1().doubleValue());
            R131Cell1.setCellStyle(numberStyle);
        } else {
            R131Cell1.setCellValue("");
            R131Cell1.setCellStyle(textStyle);
        }
        Cell R131Cell2 = row.createCell(4);
        if (record1.getR131_FIG_BAL_BWP2() != null) {
            R131Cell2.setCellValue(record1.getR131_FIG_BAL_BWP2().doubleValue());
            R131Cell2.setCellStyle(numberStyle);
        } else {
            R131Cell2.setCellValue("");
            R131Cell2.setCellStyle(textStyle);
        }
        Cell R131Cell3 = row.createCell(5);
        if (record1.getR131_AMT_ADJ_BWP1() != null) {
            R131Cell3.setCellValue(record1.getR131_AMT_ADJ_BWP1().doubleValue());
            R131Cell3.setCellStyle(numberStyle);
        } else {
            R131Cell3.setCellValue("");
            R131Cell3.setCellStyle(textStyle);
        }
        Cell R131Cell4 = row.createCell(6);
        if (record1.getR131_AMT_ADJ_BWP2() != null) {
            R131Cell4.setCellValue(record1.getR131_AMT_ADJ_BWP2().doubleValue());
            R131Cell4.setCellStyle(numberStyle);
        } else {
            R131Cell4.setCellValue("");
            R131Cell4.setCellStyle(textStyle);
        }
        Cell R131Cell5 = row.createCell(7);
        if (record1.getR131_NET_AMT_BWP1() != null) {
            R131Cell5.setCellValue(record1.getR131_NET_AMT_BWP1().doubleValue());
            R131Cell5.setCellStyle(numberStyle);
        } else {
            R131Cell5.setCellValue("");
            R131Cell5.setCellStyle(textStyle);
        }
        Cell R131Cell6 = row.createCell(8);
        if (record1.getR131_NET_AMT_BWP2() != null) {
            R131Cell6.setCellValue(record1.getR131_NET_AMT_BWP2().doubleValue());
            R131Cell6.setCellStyle(numberStyle);
        } else {
            R131Cell6.setCellValue("");
            R131Cell6.setCellStyle(textStyle);
        }
        Cell R131Cell7 = row.createCell(9);
        if (record1.getR131_BAL_SUB_BWP1() != null) {
            R131Cell7.setCellValue(record1.getR131_BAL_SUB_BWP1().doubleValue());
            R131Cell7.setCellStyle(numberStyle);
        } else {
            R131Cell7.setCellValue("");
            R131Cell7.setCellStyle(textStyle);
        }
        Cell R131Cell8 = row.createCell(10);
        if (record1.getR131_BAL_SUB_BWP2() != null) {
            R131Cell8.setCellValue(record1.getR131_BAL_SUB_BWP2().doubleValue());
            R131Cell8.setCellStyle(numberStyle);
        } else {
            R131Cell8.setCellValue("");
            R131Cell8.setCellStyle(textStyle);
        }
        Cell R131Cell9 = row.createCell(11);
        if (record1.getR131_BAL_ACT_SUB_BWP1() != null) {
            R131Cell9.setCellValue(record1.getR131_BAL_ACT_SUB_BWP1().doubleValue());
            R131Cell9.setCellStyle(numberStyle);
        } else {
            R131Cell9.setCellValue("");
            R131Cell9.setCellStyle(textStyle);
        }
        Cell R131Cell10 = row.createCell(12);
        if (record1.getR131_BAL_ACT_SUB_BWP2() != null) {
            R131Cell10.setCellValue(record1.getR131_BAL_ACT_SUB_BWP2().doubleValue());
            R131Cell10.setCellStyle(numberStyle);
        } else {
            R131Cell10.setCellValue("");
            R131Cell10.setCellStyle(textStyle);
        }

        /* ================= R132 ================= */
        row = sheet.getRow(131);
        Cell R132Cell1 = row.createCell(3);
        if (record1.getR132_FIG_BAL_BWP1() != null) {
            R132Cell1.setCellValue(record1.getR132_FIG_BAL_BWP1().doubleValue());
            R132Cell1.setCellStyle(numberStyle);
        } else {
            R132Cell1.setCellValue("");
            R132Cell1.setCellStyle(textStyle);
        }
        Cell R132Cell2 = row.createCell(4);
        if (record1.getR132_FIG_BAL_BWP2() != null) {
            R132Cell2.setCellValue(record1.getR132_FIG_BAL_BWP2().doubleValue());
            R132Cell2.setCellStyle(numberStyle);
        } else {
            R132Cell2.setCellValue("");
            R132Cell2.setCellStyle(textStyle);
        }
        Cell R132Cell3 = row.createCell(5);
        if (record1.getR132_AMT_ADJ_BWP1() != null) {
            R132Cell3.setCellValue(record1.getR132_AMT_ADJ_BWP1().doubleValue());
            R132Cell3.setCellStyle(numberStyle);
        } else {
            R132Cell3.setCellValue("");
            R132Cell3.setCellStyle(textStyle);
        }
        Cell R132Cell4 = row.createCell(6);
        if (record1.getR132_AMT_ADJ_BWP2() != null) {
            R132Cell4.setCellValue(record1.getR132_AMT_ADJ_BWP2().doubleValue());
            R132Cell4.setCellStyle(numberStyle);
        } else {
            R132Cell4.setCellValue("");
            R132Cell4.setCellStyle(textStyle);
        }
        Cell R132Cell5 = row.createCell(7);
        if (record1.getR132_NET_AMT_BWP1() != null) {
            R132Cell5.setCellValue(record1.getR132_NET_AMT_BWP1().doubleValue());
            R132Cell5.setCellStyle(numberStyle);
        } else {
            R132Cell5.setCellValue("");
            R132Cell5.setCellStyle(textStyle);
        }
        Cell R132Cell6 = row.createCell(8);
        if (record1.getR132_NET_AMT_BWP2() != null) {
            R132Cell6.setCellValue(record1.getR132_NET_AMT_BWP2().doubleValue());
            R132Cell6.setCellStyle(numberStyle);
        } else {
            R132Cell6.setCellValue("");
            R132Cell6.setCellStyle(textStyle);
        }
        Cell R132Cell7 = row.createCell(9);
        if (record1.getR132_BAL_SUB_BWP1() != null) {
            R132Cell7.setCellValue(record1.getR132_BAL_SUB_BWP1().doubleValue());
            R132Cell7.setCellStyle(numberStyle);
        } else {
            R132Cell7.setCellValue("");
            R132Cell7.setCellStyle(textStyle);
        }
        Cell R132Cell8 = row.createCell(10);
        if (record1.getR132_BAL_SUB_BWP2() != null) {
            R132Cell8.setCellValue(record1.getR132_BAL_SUB_BWP2().doubleValue());
            R132Cell8.setCellStyle(numberStyle);
        } else {
            R132Cell8.setCellValue("");
            R132Cell8.setCellStyle(textStyle);
        }
        Cell R132Cell9 = row.createCell(11);
        if (record1.getR132_BAL_ACT_SUB_BWP1() != null) {
            R132Cell9.setCellValue(record1.getR132_BAL_ACT_SUB_BWP1().doubleValue());
            R132Cell9.setCellStyle(numberStyle);
        } else {
            R132Cell9.setCellValue("");
            R132Cell9.setCellStyle(textStyle);
        }
        Cell R132Cell10 = row.createCell(12);
        if (record1.getR132_BAL_ACT_SUB_BWP2() != null) {
            R132Cell10.setCellValue(record1.getR132_BAL_ACT_SUB_BWP2().doubleValue());
            R132Cell10.setCellStyle(numberStyle);
        } else {
            R132Cell10.setCellValue("");
            R132Cell10.setCellStyle(textStyle);
        }

        /* ================= R133 ================= */
        row = sheet.getRow(132);
        Cell R133Cell1 = row.createCell(3);
        if (record1.getR133_FIG_BAL_BWP1() != null) {
            R133Cell1.setCellValue(record1.getR133_FIG_BAL_BWP1().doubleValue());
            R133Cell1.setCellStyle(numberStyle);
        } else {
            R133Cell1.setCellValue("");
            R133Cell1.setCellStyle(textStyle);
        }
        Cell R133Cell2 = row.createCell(4);
        if (record1.getR133_FIG_BAL_BWP2() != null) {
            R133Cell2.setCellValue(record1.getR133_FIG_BAL_BWP2().doubleValue());
            R133Cell2.setCellStyle(numberStyle);
        } else {
            R133Cell2.setCellValue("");
            R133Cell2.setCellStyle(textStyle);
        }
        Cell R133Cell3 = row.createCell(5);
        if (record1.getR133_AMT_ADJ_BWP1() != null) {
            R133Cell3.setCellValue(record1.getR133_AMT_ADJ_BWP1().doubleValue());
            R133Cell3.setCellStyle(numberStyle);
        } else {
            R133Cell3.setCellValue("");
            R133Cell3.setCellStyle(textStyle);
        }
        Cell R133Cell4 = row.createCell(6);
        if (record1.getR133_AMT_ADJ_BWP2() != null) {
            R133Cell4.setCellValue(record1.getR133_AMT_ADJ_BWP2().doubleValue());
            R133Cell4.setCellStyle(numberStyle);
        } else {
            R133Cell4.setCellValue("");
            R133Cell4.setCellStyle(textStyle);
        }
        Cell R133Cell5 = row.createCell(7);
        if (record1.getR133_NET_AMT_BWP1() != null) {
            R133Cell5.setCellValue(record1.getR133_NET_AMT_BWP1().doubleValue());
            R133Cell5.setCellStyle(numberStyle);
        } else {
            R133Cell5.setCellValue("");
            R133Cell5.setCellStyle(textStyle);
        }
        Cell R133Cell6 = row.createCell(8);
        if (record1.getR133_NET_AMT_BWP2() != null) {
            R133Cell6.setCellValue(record1.getR133_NET_AMT_BWP2().doubleValue());
            R133Cell6.setCellStyle(numberStyle);
        } else {
            R133Cell6.setCellValue("");
            R133Cell6.setCellStyle(textStyle);
        }
        Cell R133Cell7 = row.createCell(9);
        if (record1.getR133_BAL_SUB_BWP1() != null) {
            R133Cell7.setCellValue(record1.getR133_BAL_SUB_BWP1().doubleValue());
            R133Cell7.setCellStyle(numberStyle);
        } else {
            R133Cell7.setCellValue("");
            R133Cell7.setCellStyle(textStyle);
        }
        Cell R133Cell8 = row.createCell(10);
        if (record1.getR133_BAL_SUB_BWP2() != null) {
            R133Cell8.setCellValue(record1.getR133_BAL_SUB_BWP2().doubleValue());
            R133Cell8.setCellStyle(numberStyle);
        } else {
            R133Cell8.setCellValue("");
            R133Cell8.setCellStyle(textStyle);
        }
        Cell R133Cell9 = row.createCell(11);
        if (record1.getR133_BAL_ACT_SUB_BWP1() != null) {
            R133Cell9.setCellValue(record1.getR133_BAL_ACT_SUB_BWP1().doubleValue());
            R133Cell9.setCellStyle(numberStyle);
        } else {
            R133Cell9.setCellValue("");
            R133Cell9.setCellStyle(textStyle);
        }
        Cell R133Cell10 = row.createCell(12);
        if (record1.getR133_BAL_ACT_SUB_BWP2() != null) {
            R133Cell10.setCellValue(record1.getR133_BAL_ACT_SUB_BWP2().doubleValue());
            R133Cell10.setCellStyle(numberStyle);
        } else {
            R133Cell10.setCellValue("");
            R133Cell10.setCellStyle(textStyle);
        }

        /* ================= R134 ================= */
        row = sheet.getRow(133);
        Cell R134Cell1 = row.createCell(3);
        if (record1.getR134_FIG_BAL_BWP1() != null) {
            R134Cell1.setCellValue(record1.getR134_FIG_BAL_BWP1().doubleValue());
            R134Cell1.setCellStyle(numberStyle);
        } else {
            R134Cell1.setCellValue("");
            R134Cell1.setCellStyle(textStyle);
        }
        Cell R134Cell2 = row.createCell(4);
        if (record1.getR134_FIG_BAL_BWP2() != null) {
            R134Cell2.setCellValue(record1.getR134_FIG_BAL_BWP2().doubleValue());
            R134Cell2.setCellStyle(numberStyle);
        } else {
            R134Cell2.setCellValue("");
            R134Cell2.setCellStyle(textStyle);
        }
        Cell R134Cell3 = row.createCell(5);
        if (record1.getR134_AMT_ADJ_BWP1() != null) {
            R134Cell3.setCellValue(record1.getR134_AMT_ADJ_BWP1().doubleValue());
            R134Cell3.setCellStyle(numberStyle);
        } else {
            R134Cell3.setCellValue("");
            R134Cell3.setCellStyle(textStyle);
        }
        Cell R134Cell4 = row.createCell(6);
        if (record1.getR134_AMT_ADJ_BWP2() != null) {
            R134Cell4.setCellValue(record1.getR134_AMT_ADJ_BWP2().doubleValue());
            R134Cell4.setCellStyle(numberStyle);
        } else {
            R134Cell4.setCellValue("");
            R134Cell4.setCellStyle(textStyle);
        }
        Cell R134Cell5 = row.createCell(7);
        if (record1.getR134_NET_AMT_BWP1() != null) {
            R134Cell5.setCellValue(record1.getR134_NET_AMT_BWP1().doubleValue());
            R134Cell5.setCellStyle(numberStyle);
        } else {
            R134Cell5.setCellValue("");
            R134Cell5.setCellStyle(textStyle);
        }
        Cell R134Cell6 = row.createCell(8);
        if (record1.getR134_NET_AMT_BWP2() != null) {
            R134Cell6.setCellValue(record1.getR134_NET_AMT_BWP2().doubleValue());
            R134Cell6.setCellStyle(numberStyle);
        } else {
            R134Cell6.setCellValue("");
            R134Cell6.setCellStyle(textStyle);
        }
        Cell R134Cell7 = row.createCell(9);
        if (record1.getR134_BAL_SUB_BWP1() != null) {
            R134Cell7.setCellValue(record1.getR134_BAL_SUB_BWP1().doubleValue());
            R134Cell7.setCellStyle(numberStyle);
        } else {
            R134Cell7.setCellValue("");
            R134Cell7.setCellStyle(textStyle);
        }
        Cell R134Cell8 = row.createCell(10);
        if (record1.getR134_BAL_SUB_BWP2() != null) {
            R134Cell8.setCellValue(record1.getR134_BAL_SUB_BWP2().doubleValue());
            R134Cell8.setCellStyle(numberStyle);
        } else {
            R134Cell8.setCellValue("");
            R134Cell8.setCellStyle(textStyle);
        }
        Cell R134Cell9 = row.createCell(11);
        if (record1.getR134_BAL_ACT_SUB_BWP1() != null) {
            R134Cell9.setCellValue(record1.getR134_BAL_ACT_SUB_BWP1().doubleValue());
            R134Cell9.setCellStyle(numberStyle);
        } else {
            R134Cell9.setCellValue("");
            R134Cell9.setCellStyle(textStyle);
        }
        Cell R134Cell10 = row.createCell(12);
        if (record1.getR134_BAL_ACT_SUB_BWP2() != null) {
            R134Cell10.setCellValue(record1.getR134_BAL_ACT_SUB_BWP2().doubleValue());
            R134Cell10.setCellStyle(numberStyle);
        } else {
            R134Cell10.setCellValue("");
            R134Cell10.setCellStyle(textStyle);
        }

        /* ================= R135 ================= */
        row = sheet.getRow(134);
        Cell R135Cell1 = row.createCell(3);
        if (record1.getR135_FIG_BAL_BWP1() != null) {
            R135Cell1.setCellValue(record1.getR135_FIG_BAL_BWP1().doubleValue());
            R135Cell1.setCellStyle(numberStyle);
        } else {
            R135Cell1.setCellValue("");
            R135Cell1.setCellStyle(textStyle);
        }
        Cell R135Cell2 = row.createCell(4);
        if (record1.getR135_FIG_BAL_BWP2() != null) {
            R135Cell2.setCellValue(record1.getR135_FIG_BAL_BWP2().doubleValue());
            R135Cell2.setCellStyle(numberStyle);
        } else {
            R135Cell2.setCellValue("");
            R135Cell2.setCellStyle(textStyle);
        }
        Cell R135Cell3 = row.createCell(5);
        if (record1.getR135_AMT_ADJ_BWP1() != null) {
            R135Cell3.setCellValue(record1.getR135_AMT_ADJ_BWP1().doubleValue());
            R135Cell3.setCellStyle(numberStyle);
        } else {
            R135Cell3.setCellValue("");
            R135Cell3.setCellStyle(textStyle);
        }
        Cell R135Cell4 = row.createCell(6);
        if (record1.getR135_AMT_ADJ_BWP2() != null) {
            R135Cell4.setCellValue(record1.getR135_AMT_ADJ_BWP2().doubleValue());
            R135Cell4.setCellStyle(numberStyle);
        } else {
            R135Cell4.setCellValue("");
            R135Cell4.setCellStyle(textStyle);
        }
        Cell R135Cell5 = row.createCell(7);
        if (record1.getR135_NET_AMT_BWP1() != null) {
            R135Cell5.setCellValue(record1.getR135_NET_AMT_BWP1().doubleValue());
            R135Cell5.setCellStyle(numberStyle);
        } else {
            R135Cell5.setCellValue("");
            R135Cell5.setCellStyle(textStyle);
        }
        Cell R135Cell6 = row.createCell(8);
        if (record1.getR135_NET_AMT_BWP2() != null) {
            R135Cell6.setCellValue(record1.getR135_NET_AMT_BWP2().doubleValue());
            R135Cell6.setCellStyle(numberStyle);
        } else {
            R135Cell6.setCellValue("");
            R135Cell6.setCellStyle(textStyle);
        }
        Cell R135Cell7 = row.createCell(9);
        if (record1.getR135_BAL_SUB_BWP1() != null) {
            R135Cell7.setCellValue(record1.getR135_BAL_SUB_BWP1().doubleValue());
            R135Cell7.setCellStyle(numberStyle);
        } else {
            R135Cell7.setCellValue("");
            R135Cell7.setCellStyle(textStyle);
        }
        Cell R135Cell8 = row.createCell(10);
        if (record1.getR135_BAL_SUB_BWP2() != null) {
            R135Cell8.setCellValue(record1.getR135_BAL_SUB_BWP2().doubleValue());
            R135Cell8.setCellStyle(numberStyle);
        } else {
            R135Cell8.setCellValue("");
            R135Cell8.setCellStyle(textStyle);
        }
        Cell R135Cell9 = row.createCell(11);
        if (record1.getR135_BAL_ACT_SUB_BWP1() != null) {
            R135Cell9.setCellValue(record1.getR135_BAL_ACT_SUB_BWP1().doubleValue());
            R135Cell9.setCellStyle(numberStyle);
        } else {
            R135Cell9.setCellValue("");
            R135Cell9.setCellStyle(textStyle);
        }
        Cell R135Cell10 = row.createCell(12);
        if (record1.getR135_BAL_ACT_SUB_BWP2() != null) {
            R135Cell10.setCellValue(record1.getR135_BAL_ACT_SUB_BWP2().doubleValue());
            R135Cell10.setCellStyle(numberStyle);
        } else {
            R135Cell10.setCellValue("");
            R135Cell10.setCellStyle(textStyle);
        }

        /* ================= R136 ================= */
        row = sheet.getRow(135);
        Cell R136Cell1 = row.createCell(3);
        if (record1.getR136_FIG_BAL_BWP1() != null) {
            R136Cell1.setCellValue(record1.getR136_FIG_BAL_BWP1().doubleValue());
            R136Cell1.setCellStyle(numberStyle);
        } else {
            R136Cell1.setCellValue("");
            R136Cell1.setCellStyle(textStyle);
        }
        Cell R136Cell2 = row.createCell(4);
        if (record1.getR136_FIG_BAL_BWP2() != null) {
            R136Cell2.setCellValue(record1.getR136_FIG_BAL_BWP2().doubleValue());
            R136Cell2.setCellStyle(numberStyle);
        } else {
            R136Cell2.setCellValue("");
            R136Cell2.setCellStyle(textStyle);
        }
        Cell R136Cell3 = row.createCell(5);
        if (record1.getR136_AMT_ADJ_BWP1() != null) {
            R136Cell3.setCellValue(record1.getR136_AMT_ADJ_BWP1().doubleValue());
            R136Cell3.setCellStyle(numberStyle);
        } else {
            R136Cell3.setCellValue("");
            R136Cell3.setCellStyle(textStyle);
        }
        Cell R136Cell4 = row.createCell(6);
        if (record1.getR136_AMT_ADJ_BWP2() != null) {
            R136Cell4.setCellValue(record1.getR136_AMT_ADJ_BWP2().doubleValue());
            R136Cell4.setCellStyle(numberStyle);
        } else {
            R136Cell4.setCellValue("");
            R136Cell4.setCellStyle(textStyle);
        }
        Cell R136Cell5 = row.createCell(7);
        if (record1.getR136_NET_AMT_BWP1() != null) {
            R136Cell5.setCellValue(record1.getR136_NET_AMT_BWP1().doubleValue());
            R136Cell5.setCellStyle(numberStyle);
        } else {
            R136Cell5.setCellValue("");
            R136Cell5.setCellStyle(textStyle);
        }
        Cell R136Cell6 = row.createCell(8);
        if (record1.getR136_NET_AMT_BWP2() != null) {
            R136Cell6.setCellValue(record1.getR136_NET_AMT_BWP2().doubleValue());
            R136Cell6.setCellStyle(numberStyle);
        } else {
            R136Cell6.setCellValue("");
            R136Cell6.setCellStyle(textStyle);
        }
        Cell R136Cell7 = row.createCell(9);
        if (record1.getR136_BAL_SUB_BWP1() != null) {
            R136Cell7.setCellValue(record1.getR136_BAL_SUB_BWP1().doubleValue());
            R136Cell7.setCellStyle(numberStyle);
        } else {
            R136Cell7.setCellValue("");
            R136Cell7.setCellStyle(textStyle);
        }
        Cell R136Cell8 = row.createCell(10);
        if (record1.getR136_BAL_SUB_BWP2() != null) {
            R136Cell8.setCellValue(record1.getR136_BAL_SUB_BWP2().doubleValue());
            R136Cell8.setCellStyle(numberStyle);
        } else {
            R136Cell8.setCellValue("");
            R136Cell8.setCellStyle(textStyle);
        }
        Cell R136Cell9 = row.createCell(11);
        if (record1.getR136_BAL_ACT_SUB_BWP1() != null) {
            R136Cell9.setCellValue(record1.getR136_BAL_ACT_SUB_BWP1().doubleValue());
            R136Cell9.setCellStyle(numberStyle);
        } else {
            R136Cell9.setCellValue("");
            R136Cell9.setCellStyle(textStyle);
        }
        Cell R136Cell10 = row.createCell(12);
        if (record1.getR136_BAL_ACT_SUB_BWP2() != null) {
            R136Cell10.setCellValue(record1.getR136_BAL_ACT_SUB_BWP2().doubleValue());
            R136Cell10.setCellStyle(numberStyle);
        } else {
            R136Cell10.setCellValue("");
            R136Cell10.setCellStyle(textStyle);
        }

        /* ================= R137 ================= */
        row = sheet.getRow(136);
        Cell R137Cell1 = row.createCell(3);
        if (record1.getR137_FIG_BAL_BWP1() != null) {
            R137Cell1.setCellValue(record1.getR137_FIG_BAL_BWP1().doubleValue());
            R137Cell1.setCellStyle(numberStyle);
        } else {
            R137Cell1.setCellValue("");
            R137Cell1.setCellStyle(textStyle);
        }
        Cell R137Cell2 = row.createCell(4);
        if (record1.getR137_FIG_BAL_BWP2() != null) {
            R137Cell2.setCellValue(record1.getR137_FIG_BAL_BWP2().doubleValue());
            R137Cell2.setCellStyle(numberStyle);
        } else {
            R137Cell2.setCellValue("");
            R137Cell2.setCellStyle(textStyle);
        }
        Cell R137Cell3 = row.createCell(5);
        if (record1.getR137_AMT_ADJ_BWP1() != null) {
            R137Cell3.setCellValue(record1.getR137_AMT_ADJ_BWP1().doubleValue());
            R137Cell3.setCellStyle(numberStyle);
        } else {
            R137Cell3.setCellValue("");
            R137Cell3.setCellStyle(textStyle);
        }
        Cell R137Cell4 = row.createCell(6);
        if (record1.getR137_AMT_ADJ_BWP2() != null) {
            R137Cell4.setCellValue(record1.getR137_AMT_ADJ_BWP2().doubleValue());
            R137Cell4.setCellStyle(numberStyle);
        } else {
            R137Cell4.setCellValue("");
            R137Cell4.setCellStyle(textStyle);
        }
        Cell R137Cell5 = row.createCell(7);
        if (record1.getR137_NET_AMT_BWP1() != null) {
            R137Cell5.setCellValue(record1.getR137_NET_AMT_BWP1().doubleValue());
            R137Cell5.setCellStyle(numberStyle);
        } else {
            R137Cell5.setCellValue("");
            R137Cell5.setCellStyle(textStyle);
        }
        Cell R137Cell6 = row.createCell(8);
        if (record1.getR137_NET_AMT_BWP2() != null) {
            R137Cell6.setCellValue(record1.getR137_NET_AMT_BWP2().doubleValue());
            R137Cell6.setCellStyle(numberStyle);
        } else {
            R137Cell6.setCellValue("");
            R137Cell6.setCellStyle(textStyle);
        }
        Cell R137Cell7 = row.createCell(9);
        if (record1.getR137_BAL_SUB_BWP1() != null) {
            R137Cell7.setCellValue(record1.getR137_BAL_SUB_BWP1().doubleValue());
            R137Cell7.setCellStyle(numberStyle);
        } else {
            R137Cell7.setCellValue("");
            R137Cell7.setCellStyle(textStyle);
        }
        Cell R137Cell8 = row.createCell(10);
        if (record1.getR137_BAL_SUB_BWP2() != null) {
            R137Cell8.setCellValue(record1.getR137_BAL_SUB_BWP2().doubleValue());
            R137Cell8.setCellStyle(numberStyle);
        } else {
            R137Cell8.setCellValue("");
            R137Cell8.setCellStyle(textStyle);
        }
        Cell R137Cell9 = row.createCell(11);
        if (record1.getR137_BAL_ACT_SUB_BWP1() != null) {
            R137Cell9.setCellValue(record1.getR137_BAL_ACT_SUB_BWP1().doubleValue());
            R137Cell9.setCellStyle(numberStyle);
        } else {
            R137Cell9.setCellValue("");
            R137Cell9.setCellStyle(textStyle);
        }
        Cell R137Cell10 = row.createCell(12);
        if (record1.getR137_BAL_ACT_SUB_BWP2() != null) {
            R137Cell10.setCellValue(record1.getR137_BAL_ACT_SUB_BWP2().doubleValue());
            R137Cell10.setCellStyle(numberStyle);
        } else {
            R137Cell10.setCellValue("");
            R137Cell10.setCellStyle(textStyle);
        }

        /* ================= R138 ================= */
        row = sheet.getRow(137);
        Cell R138Cell1 = row.createCell(3);
        if (record1.getR138_FIG_BAL_BWP1() != null) {
            R138Cell1.setCellValue(record1.getR138_FIG_BAL_BWP1().doubleValue());
            R138Cell1.setCellStyle(numberStyle);
        } else {
            R138Cell1.setCellValue("");
            R138Cell1.setCellStyle(textStyle);
        }
        Cell R138Cell2 = row.createCell(4);
        if (record1.getR138_FIG_BAL_BWP2() != null) {
            R138Cell2.setCellValue(record1.getR138_FIG_BAL_BWP2().doubleValue());
            R138Cell2.setCellStyle(numberStyle);
        } else {
            R138Cell2.setCellValue("");
            R138Cell2.setCellStyle(textStyle);
        }
        Cell R138Cell3 = row.createCell(5);
        if (record1.getR138_AMT_ADJ_BWP1() != null) {
            R138Cell3.setCellValue(record1.getR138_AMT_ADJ_BWP1().doubleValue());
            R138Cell3.setCellStyle(numberStyle);
        } else {
            R138Cell3.setCellValue("");
            R138Cell3.setCellStyle(textStyle);
        }
        Cell R138Cell4 = row.createCell(6);
        if (record1.getR138_AMT_ADJ_BWP2() != null) {
            R138Cell4.setCellValue(record1.getR138_AMT_ADJ_BWP2().doubleValue());
            R138Cell4.setCellStyle(numberStyle);
        } else {
            R138Cell4.setCellValue("");
            R138Cell4.setCellStyle(textStyle);
        }
        Cell R138Cell5 = row.createCell(7);
        if (record1.getR138_NET_AMT_BWP1() != null) {
            R138Cell5.setCellValue(record1.getR138_NET_AMT_BWP1().doubleValue());
            R138Cell5.setCellStyle(numberStyle);
        } else {
            R138Cell5.setCellValue("");
            R138Cell5.setCellStyle(textStyle);
        }
        Cell R138Cell6 = row.createCell(8);
        if (record1.getR138_NET_AMT_BWP2() != null) {
            R138Cell6.setCellValue(record1.getR138_NET_AMT_BWP2().doubleValue());
            R138Cell6.setCellStyle(numberStyle);
        } else {
            R138Cell6.setCellValue("");
            R138Cell6.setCellStyle(textStyle);
        }
        Cell R138Cell7 = row.createCell(9);
        if (record1.getR138_BAL_SUB_BWP1() != null) {
            R138Cell7.setCellValue(record1.getR138_BAL_SUB_BWP1().doubleValue());
            R138Cell7.setCellStyle(numberStyle);
        } else {
            R138Cell7.setCellValue("");
            R138Cell7.setCellStyle(textStyle);
        }
        Cell R138Cell8 = row.createCell(10);
        if (record1.getR138_BAL_SUB_BWP2() != null) {
            R138Cell8.setCellValue(record1.getR138_BAL_SUB_BWP2().doubleValue());
            R138Cell8.setCellStyle(numberStyle);
        } else {
            R138Cell8.setCellValue("");
            R138Cell8.setCellStyle(textStyle);
        }
        Cell R138Cell9 = row.createCell(11);
        if (record1.getR138_BAL_ACT_SUB_BWP1() != null) {
            R138Cell9.setCellValue(record1.getR138_BAL_ACT_SUB_BWP1().doubleValue());
            R138Cell9.setCellStyle(numberStyle);
        } else {
            R138Cell9.setCellValue("");
            R138Cell9.setCellStyle(textStyle);
        }
        Cell R138Cell10 = row.createCell(12);
        if (record1.getR138_BAL_ACT_SUB_BWP2() != null) {
            R138Cell10.setCellValue(record1.getR138_BAL_ACT_SUB_BWP2().doubleValue());
            R138Cell10.setCellStyle(numberStyle);
        } else {
            R138Cell10.setCellValue("");
            R138Cell10.setCellStyle(textStyle);
        }

        /* ================= R140 ================= */
        row = sheet.getRow(139);
        Cell R140Cell1 = row.createCell(3);
        if (record1.getR140_FIG_BAL_BWP1() != null) {
            R140Cell1.setCellValue(record1.getR140_FIG_BAL_BWP1().doubleValue());
            R140Cell1.setCellStyle(numberStyle);
        } else {
            R140Cell1.setCellValue("");
            R140Cell1.setCellStyle(textStyle);
        }
        Cell R140Cell2 = row.createCell(4);
        if (record1.getR140_FIG_BAL_BWP2() != null) {
            R140Cell2.setCellValue(record1.getR140_FIG_BAL_BWP2().doubleValue());
            R140Cell2.setCellStyle(numberStyle);
        } else {
            R140Cell2.setCellValue("");
            R140Cell2.setCellStyle(textStyle);
        }
        Cell R140Cell3 = row.createCell(5);
        if (record1.getR140_AMT_ADJ_BWP1() != null) {
            R140Cell3.setCellValue(record1.getR140_AMT_ADJ_BWP1().doubleValue());
            R140Cell3.setCellStyle(numberStyle);
        } else {
            R140Cell3.setCellValue("");
            R140Cell3.setCellStyle(textStyle);
        }
        Cell R140Cell4 = row.createCell(6);
        if (record1.getR140_AMT_ADJ_BWP2() != null) {
            R140Cell4.setCellValue(record1.getR140_AMT_ADJ_BWP2().doubleValue());
            R140Cell4.setCellStyle(numberStyle);
        } else {
            R140Cell4.setCellValue("");
            R140Cell4.setCellStyle(textStyle);
        }
        Cell R140Cell5 = row.createCell(7);
        if (record1.getR140_NET_AMT_BWP1() != null) {
            R140Cell5.setCellValue(record1.getR140_NET_AMT_BWP1().doubleValue());
            R140Cell5.setCellStyle(numberStyle);
        } else {
            R140Cell5.setCellValue("");
            R140Cell5.setCellStyle(textStyle);
        }
        Cell R140Cell6 = row.createCell(8);
        if (record1.getR140_NET_AMT_BWP2() != null) {
            R140Cell6.setCellValue(record1.getR140_NET_AMT_BWP2().doubleValue());
            R140Cell6.setCellStyle(numberStyle);
        } else {
            R140Cell6.setCellValue("");
            R140Cell6.setCellStyle(textStyle);
        }
        Cell R140Cell7 = row.createCell(9);
        if (record1.getR140_BAL_SUB_BWP1() != null) {
            R140Cell7.setCellValue(record1.getR140_BAL_SUB_BWP1().doubleValue());
            R140Cell7.setCellStyle(numberStyle);
        } else {
            R140Cell7.setCellValue("");
            R140Cell7.setCellStyle(textStyle);
        }
        Cell R140Cell8 = row.createCell(10);
        if (record1.getR140_BAL_SUB_BWP2() != null) {
            R140Cell8.setCellValue(record1.getR140_BAL_SUB_BWP2().doubleValue());
            R140Cell8.setCellStyle(numberStyle);
        } else {
            R140Cell8.setCellValue("");
            R140Cell8.setCellStyle(textStyle);
        }
        Cell R140Cell9 = row.createCell(11);
        if (record1.getR140_BAL_ACT_SUB_BWP1() != null) {
            R140Cell9.setCellValue(record1.getR140_BAL_ACT_SUB_BWP1().doubleValue());
            R140Cell9.setCellStyle(numberStyle);
        } else {
            R140Cell9.setCellValue("");
            R140Cell9.setCellStyle(textStyle);
        }
        Cell R140Cell10 = row.createCell(12);
        if (record1.getR140_BAL_ACT_SUB_BWP2() != null) {
            R140Cell10.setCellValue(record1.getR140_BAL_ACT_SUB_BWP2().doubleValue());
            R140Cell10.setCellStyle(numberStyle);
        } else {
            R140Cell10.setCellValue("");
            R140Cell10.setCellStyle(textStyle);
        }

        /* ================= R141 ================= */
        row = sheet.getRow(140);
        Cell R141Cell1 = row.createCell(3);
        if (record1.getR141_FIG_BAL_BWP1() != null) {
            R141Cell1.setCellValue(record1.getR141_FIG_BAL_BWP1().doubleValue());
            R141Cell1.setCellStyle(numberStyle);
        } else {
            R141Cell1.setCellValue("");
            R141Cell1.setCellStyle(textStyle);
        }
        Cell R141Cell2 = row.createCell(4);
        if (record1.getR141_FIG_BAL_BWP2() != null) {
            R141Cell2.setCellValue(record1.getR141_FIG_BAL_BWP2().doubleValue());
            R141Cell2.setCellStyle(numberStyle);
        } else {
            R141Cell2.setCellValue("");
            R141Cell2.setCellStyle(textStyle);
        }
        Cell R141Cell3 = row.createCell(5);
        if (record1.getR141_AMT_ADJ_BWP1() != null) {
            R141Cell3.setCellValue(record1.getR141_AMT_ADJ_BWP1().doubleValue());
            R141Cell3.setCellStyle(numberStyle);
        } else {
            R141Cell3.setCellValue("");
            R141Cell3.setCellStyle(textStyle);
        }
        Cell R141Cell4 = row.createCell(6);
        if (record1.getR141_AMT_ADJ_BWP2() != null) {
            R141Cell4.setCellValue(record1.getR141_AMT_ADJ_BWP2().doubleValue());
            R141Cell4.setCellStyle(numberStyle);
        } else {
            R141Cell4.setCellValue("");
            R141Cell4.setCellStyle(textStyle);
        }
        Cell R141Cell5 = row.createCell(7);
        if (record1.getR141_NET_AMT_BWP1() != null) {
            R141Cell5.setCellValue(record1.getR141_NET_AMT_BWP1().doubleValue());
            R141Cell5.setCellStyle(numberStyle);
        } else {
            R141Cell5.setCellValue("");
            R141Cell5.setCellStyle(textStyle);
        }
        Cell R141Cell6 = row.createCell(8);
        if (record1.getR141_NET_AMT_BWP2() != null) {
            R141Cell6.setCellValue(record1.getR141_NET_AMT_BWP2().doubleValue());
            R141Cell6.setCellStyle(numberStyle);
        } else {
            R141Cell6.setCellValue("");
            R141Cell6.setCellStyle(textStyle);
        }
        Cell R141Cell7 = row.createCell(9);
        if (record1.getR141_BAL_SUB_BWP1() != null) {
            R141Cell7.setCellValue(record1.getR141_BAL_SUB_BWP1().doubleValue());
            R141Cell7.setCellStyle(numberStyle);
        } else {
            R141Cell7.setCellValue("");
            R141Cell7.setCellStyle(textStyle);
        }
        Cell R141Cell8 = row.createCell(10);
        if (record1.getR141_BAL_SUB_BWP2() != null) {
            R141Cell8.setCellValue(record1.getR141_BAL_SUB_BWP2().doubleValue());
            R141Cell8.setCellStyle(numberStyle);
        } else {
            R141Cell8.setCellValue("");
            R141Cell8.setCellStyle(textStyle);
        }
        Cell R141Cell9 = row.createCell(11);
        if (record1.getR141_BAL_ACT_SUB_BWP1() != null) {
            R141Cell9.setCellValue(record1.getR141_BAL_ACT_SUB_BWP1().doubleValue());
            R141Cell9.setCellStyle(numberStyle);
        } else {
            R141Cell9.setCellValue("");
            R141Cell9.setCellStyle(textStyle);
        }
        Cell R141Cell10 = row.createCell(12);
        if (record1.getR141_BAL_ACT_SUB_BWP2() != null) {
            R141Cell10.setCellValue(record1.getR141_BAL_ACT_SUB_BWP2().doubleValue());
            R141Cell10.setCellStyle(numberStyle);
        } else {
            R141Cell10.setCellValue("");
            R141Cell10.setCellStyle(textStyle);
        }

        /* ================= R142 ================= */
        row = sheet.getRow(141);
        Cell R142Cell1 = row.createCell(3);
        if (record1.getR142_FIG_BAL_BWP1() != null) {
            R142Cell1.setCellValue(record1.getR142_FIG_BAL_BWP1().doubleValue());
            R142Cell1.setCellStyle(numberStyle);
        } else {
            R142Cell1.setCellValue("");
            R142Cell1.setCellStyle(textStyle);
        }
        Cell R142Cell2 = row.createCell(4);
        if (record1.getR142_FIG_BAL_BWP2() != null) {
            R142Cell2.setCellValue(record1.getR142_FIG_BAL_BWP2().doubleValue());
            R142Cell2.setCellStyle(numberStyle);
        } else {
            R142Cell2.setCellValue("");
            R142Cell2.setCellStyle(textStyle);
        }
        Cell R142Cell3 = row.createCell(5);
        if (record1.getR142_AMT_ADJ_BWP1() != null) {
            R142Cell3.setCellValue(record1.getR142_AMT_ADJ_BWP1().doubleValue());
            R142Cell3.setCellStyle(numberStyle);
        } else {
            R142Cell3.setCellValue("");
            R142Cell3.setCellStyle(textStyle);
        }
        Cell R142Cell4 = row.createCell(6);
        if (record1.getR142_AMT_ADJ_BWP2() != null) {
            R142Cell4.setCellValue(record1.getR142_AMT_ADJ_BWP2().doubleValue());
            R142Cell4.setCellStyle(numberStyle);
        } else {
            R142Cell4.setCellValue("");
            R142Cell4.setCellStyle(textStyle);
        }
        Cell R142Cell5 = row.createCell(7);
        if (record1.getR142_NET_AMT_BWP1() != null) {
            R142Cell5.setCellValue(record1.getR142_NET_AMT_BWP1().doubleValue());
            R142Cell5.setCellStyle(numberStyle);
        } else {
            R142Cell5.setCellValue("");
            R142Cell5.setCellStyle(textStyle);
        }
        Cell R142Cell6 = row.createCell(8);
        if (record1.getR142_NET_AMT_BWP2() != null) {
            R142Cell6.setCellValue(record1.getR142_NET_AMT_BWP2().doubleValue());
            R142Cell6.setCellStyle(numberStyle);
        } else {
            R142Cell6.setCellValue("");
            R142Cell6.setCellStyle(textStyle);
        }
        Cell R142Cell7 = row.createCell(9);
        if (record1.getR142_BAL_SUB_BWP1() != null) {
            R142Cell7.setCellValue(record1.getR142_BAL_SUB_BWP1().doubleValue());
            R142Cell7.setCellStyle(numberStyle);
        } else {
            R142Cell7.setCellValue("");
            R142Cell7.setCellStyle(textStyle);
        }
        Cell R142Cell8 = row.createCell(10);
        if (record1.getR142_BAL_SUB_BWP2() != null) {
            R142Cell8.setCellValue(record1.getR142_BAL_SUB_BWP2().doubleValue());
            R142Cell8.setCellStyle(numberStyle);
        } else {
            R142Cell8.setCellValue("");
            R142Cell8.setCellStyle(textStyle);
        }
        Cell R142Cell9 = row.createCell(11);
        if (record1.getR142_BAL_ACT_SUB_BWP1() != null) {
            R142Cell9.setCellValue(record1.getR142_BAL_ACT_SUB_BWP1().doubleValue());
            R142Cell9.setCellStyle(numberStyle);
        } else {
            R142Cell9.setCellValue("");
            R142Cell9.setCellStyle(textStyle);
        }
        Cell R142Cell10 = row.createCell(12);
        if (record1.getR142_BAL_ACT_SUB_BWP2() != null) {
            R142Cell10.setCellValue(record1.getR142_BAL_ACT_SUB_BWP2().doubleValue());
            R142Cell10.setCellStyle(numberStyle);
        } else {
            R142Cell10.setCellValue("");
            R142Cell10.setCellStyle(textStyle);
        }

        /* ================= R149 ================= */
        row = sheet.getRow(148);
        Cell R149Cell1 = row.createCell(3);
        if (record1.getR149_FIG_BAL_BWP1() != null) {
            R149Cell1.setCellValue(record1.getR149_FIG_BAL_BWP1().doubleValue());
            R149Cell1.setCellStyle(numberStyle);
        } else {
            R149Cell1.setCellValue("");
            R149Cell1.setCellStyle(textStyle);
        }
        Cell R149Cell2 = row.createCell(4);
        if (record1.getR149_FIG_BAL_BWP2() != null) {
            R149Cell2.setCellValue(record1.getR149_FIG_BAL_BWP2().doubleValue());
            R149Cell2.setCellStyle(numberStyle);
        } else {
            R149Cell2.setCellValue("");
            R149Cell2.setCellStyle(textStyle);
        }
        Cell R149Cell3 = row.createCell(5);
        if (record1.getR149_AMT_ADJ_BWP1() != null) {
            R149Cell3.setCellValue(record1.getR149_AMT_ADJ_BWP1().doubleValue());
            R149Cell3.setCellStyle(numberStyle);
        } else {
            R149Cell3.setCellValue("");
            R149Cell3.setCellStyle(textStyle);
        }
        Cell R149Cell4 = row.createCell(6);
        if (record1.getR149_AMT_ADJ_BWP2() != null) {
            R149Cell4.setCellValue(record1.getR149_AMT_ADJ_BWP2().doubleValue());
            R149Cell4.setCellStyle(numberStyle);
        } else {
            R149Cell4.setCellValue("");
            R149Cell4.setCellStyle(textStyle);
        }
        Cell R149Cell5 = row.createCell(7);
        if (record1.getR149_NET_AMT_BWP1() != null) {
            R149Cell5.setCellValue(record1.getR149_NET_AMT_BWP1().doubleValue());
            R149Cell5.setCellStyle(numberStyle);
        } else {
            R149Cell5.setCellValue("");
            R149Cell5.setCellStyle(textStyle);
        }
        Cell R149Cell6 = row.createCell(8);
        if (record1.getR149_NET_AMT_BWP2() != null) {
            R149Cell6.setCellValue(record1.getR149_NET_AMT_BWP2().doubleValue());
            R149Cell6.setCellStyle(numberStyle);
        } else {
            R149Cell6.setCellValue("");
            R149Cell6.setCellStyle(textStyle);
        }
        Cell R149Cell7 = row.createCell(9);
        if (record1.getR149_BAL_SUB_BWP1() != null) {
            R149Cell7.setCellValue(record1.getR149_BAL_SUB_BWP1().doubleValue());
            R149Cell7.setCellStyle(numberStyle);
        } else {
            R149Cell7.setCellValue("");
            R149Cell7.setCellStyle(textStyle);
        }
        Cell R149Cell8 = row.createCell(10);
        if (record1.getR149_BAL_SUB_BWP2() != null) {
            R149Cell8.setCellValue(record1.getR149_BAL_SUB_BWP2().doubleValue());
            R149Cell8.setCellStyle(numberStyle);
        } else {
            R149Cell8.setCellValue("");
            R149Cell8.setCellStyle(textStyle);
        }
        Cell R149Cell9 = row.createCell(11);
        if (record1.getR149_BAL_ACT_SUB_BWP1() != null) {
            R149Cell9.setCellValue(record1.getR149_BAL_ACT_SUB_BWP1().doubleValue());
            R149Cell9.setCellStyle(numberStyle);
        } else {
            R149Cell9.setCellValue("");
            R149Cell9.setCellStyle(textStyle);
        }
        Cell R149Cell10 = row.createCell(12);
        if (record1.getR149_BAL_ACT_SUB_BWP2() != null) {
            R149Cell10.setCellValue(record1.getR149_BAL_ACT_SUB_BWP2().doubleValue());
            R149Cell10.setCellStyle(numberStyle);
        } else {
            R149Cell10.setCellValue("");
            R149Cell10.setCellStyle(textStyle);
        }

        /* ================= R150 ================= */
        row = sheet.getRow(149);
        Cell R150Cell1 = row.createCell(3);
        if (record1.getR150_FIG_BAL_BWP1() != null) {
            R150Cell1.setCellValue(record1.getR150_FIG_BAL_BWP1().doubleValue());
            R150Cell1.setCellStyle(numberStyle);
        } else {
            R150Cell1.setCellValue("");
            R150Cell1.setCellStyle(textStyle);
        }
        Cell R150Cell2 = row.createCell(4);
        if (record1.getR150_FIG_BAL_BWP2() != null) {
            R150Cell2.setCellValue(record1.getR150_FIG_BAL_BWP2().doubleValue());
            R150Cell2.setCellStyle(numberStyle);
        } else {
            R150Cell2.setCellValue("");
            R150Cell2.setCellStyle(textStyle);
        }
        Cell R150Cell3 = row.createCell(5);
        if (record1.getR150_AMT_ADJ_BWP1() != null) {
            R150Cell3.setCellValue(record1.getR150_AMT_ADJ_BWP1().doubleValue());
            R150Cell3.setCellStyle(numberStyle);
        } else {
            R150Cell3.setCellValue("");
            R150Cell3.setCellStyle(textStyle);
        }
        Cell R150Cell4 = row.createCell(6);
        if (record1.getR150_AMT_ADJ_BWP2() != null) {
            R150Cell4.setCellValue(record1.getR150_AMT_ADJ_BWP2().doubleValue());
            R150Cell4.setCellStyle(numberStyle);
        } else {
            R150Cell4.setCellValue("");
            R150Cell4.setCellStyle(textStyle);
        }
        Cell R150Cell5 = row.createCell(7);
        if (record1.getR150_NET_AMT_BWP1() != null) {
            R150Cell5.setCellValue(record1.getR150_NET_AMT_BWP1().doubleValue());
            R150Cell5.setCellStyle(numberStyle);
        } else {
            R150Cell5.setCellValue("");
            R150Cell5.setCellStyle(textStyle);
        }
        Cell R150Cell6 = row.createCell(8);
        if (record1.getR150_NET_AMT_BWP2() != null) {
            R150Cell6.setCellValue(record1.getR150_NET_AMT_BWP2().doubleValue());
            R150Cell6.setCellStyle(numberStyle);
        } else {
            R150Cell6.setCellValue("");
            R150Cell6.setCellStyle(textStyle);
        }
        Cell R150Cell7 = row.createCell(9);
        if (record1.getR150_BAL_SUB_BWP1() != null) {
            R150Cell7.setCellValue(record1.getR150_BAL_SUB_BWP1().doubleValue());
            R150Cell7.setCellStyle(numberStyle);
        } else {
            R150Cell7.setCellValue("");
            R150Cell7.setCellStyle(textStyle);
        }
        Cell R150Cell8 = row.createCell(10);
        if (record1.getR150_BAL_SUB_BWP2() != null) {
            R150Cell8.setCellValue(record1.getR150_BAL_SUB_BWP2().doubleValue());
            R150Cell8.setCellStyle(numberStyle);
        } else {
            R150Cell8.setCellValue("");
            R150Cell8.setCellStyle(textStyle);
        }
        Cell R150Cell9 = row.createCell(11);
        if (record1.getR150_BAL_ACT_SUB_BWP1() != null) {
            R150Cell9.setCellValue(record1.getR150_BAL_ACT_SUB_BWP1().doubleValue());
            R150Cell9.setCellStyle(numberStyle);
        } else {
            R150Cell9.setCellValue("");
            R150Cell9.setCellStyle(textStyle);
        }
        Cell R150Cell10 = row.createCell(12);
        if (record1.getR150_BAL_ACT_SUB_BWP2() != null) {
            R150Cell10.setCellValue(record1.getR150_BAL_ACT_SUB_BWP2().doubleValue());
            R150Cell10.setCellStyle(numberStyle);
        } else {
            R150Cell10.setCellValue("");
            R150Cell10.setCellStyle(textStyle);
        }

        /* ================= R151 ================= */
        row = sheet.getRow(150);
        Cell R151Cell1 = row.createCell(3);
        if (record1.getR151_FIG_BAL_BWP1() != null) {
            R151Cell1.setCellValue(record1.getR151_FIG_BAL_BWP1().doubleValue());
            R151Cell1.setCellStyle(numberStyle);
        } else {
            R151Cell1.setCellValue("");
            R151Cell1.setCellStyle(textStyle);
        }
        Cell R151Cell2 = row.createCell(4);
        if (record1.getR151_FIG_BAL_BWP2() != null) {
            R151Cell2.setCellValue(record1.getR151_FIG_BAL_BWP2().doubleValue());
            R151Cell2.setCellStyle(numberStyle);
        } else {
            R151Cell2.setCellValue("");
            R151Cell2.setCellStyle(textStyle);
        }
        Cell R151Cell3 = row.createCell(5);
        if (record1.getR151_AMT_ADJ_BWP1() != null) {
            R151Cell3.setCellValue(record1.getR151_AMT_ADJ_BWP1().doubleValue());
            R151Cell3.setCellStyle(numberStyle);
        } else {
            R151Cell3.setCellValue("");
            R151Cell3.setCellStyle(textStyle);
        }
        Cell R151Cell4 = row.createCell(6);
        if (record1.getR151_AMT_ADJ_BWP2() != null) {
            R151Cell4.setCellValue(record1.getR151_AMT_ADJ_BWP2().doubleValue());
            R151Cell4.setCellStyle(numberStyle);
        } else {
            R151Cell4.setCellValue("");
            R151Cell4.setCellStyle(textStyle);
        }
        Cell R151Cell5 = row.createCell(7);
        if (record1.getR151_NET_AMT_BWP1() != null) {
            R151Cell5.setCellValue(record1.getR151_NET_AMT_BWP1().doubleValue());
            R151Cell5.setCellStyle(numberStyle);
        } else {
            R151Cell5.setCellValue("");
            R151Cell5.setCellStyle(textStyle);
        }
        Cell R151Cell6 = row.createCell(8);
        if (record1.getR151_NET_AMT_BWP2() != null) {
            R151Cell6.setCellValue(record1.getR151_NET_AMT_BWP2().doubleValue());
            R151Cell6.setCellStyle(numberStyle);
        } else {
            R151Cell6.setCellValue("");
            R151Cell6.setCellStyle(textStyle);
        }
        Cell R151Cell7 = row.createCell(9);
        if (record1.getR151_BAL_SUB_BWP1() != null) {
            R151Cell7.setCellValue(record1.getR151_BAL_SUB_BWP1().doubleValue());
            R151Cell7.setCellStyle(numberStyle);
        } else {
            R151Cell7.setCellValue("");
            R151Cell7.setCellStyle(textStyle);
        }
        Cell R151Cell8 = row.createCell(10);
        if (record1.getR151_BAL_SUB_BWP2() != null) {
            R151Cell8.setCellValue(record1.getR151_BAL_SUB_BWP2().doubleValue());
            R151Cell8.setCellStyle(numberStyle);
        } else {
            R151Cell8.setCellValue("");
            R151Cell8.setCellStyle(textStyle);
        }
        Cell R151Cell9 = row.createCell(11);
        if (record1.getR151_BAL_ACT_SUB_BWP1() != null) {
            R151Cell9.setCellValue(record1.getR151_BAL_ACT_SUB_BWP1().doubleValue());
            R151Cell9.setCellStyle(numberStyle);
        } else {
            R151Cell9.setCellValue("");
            R151Cell9.setCellStyle(textStyle);
        }
        Cell R151Cell10 = row.createCell(12);
        if (record1.getR151_BAL_ACT_SUB_BWP2() != null) {
            R151Cell10.setCellValue(record1.getR151_BAL_ACT_SUB_BWP2().doubleValue());
            R151Cell10.setCellStyle(numberStyle);
        } else {
            R151Cell10.setCellValue("");
            R151Cell10.setCellStyle(textStyle);
        }

        /* ================= R152 ================= */
        row = sheet.getRow(151);
        Cell R152Cell1 = row.createCell(3);
        if (record1.getR152_FIG_BAL_BWP1() != null) {
            R152Cell1.setCellValue(record1.getR152_FIG_BAL_BWP1().doubleValue());
            R152Cell1.setCellStyle(numberStyle);
        } else {
            R152Cell1.setCellValue("");
            R152Cell1.setCellStyle(textStyle);
        }
        Cell R152Cell2 = row.createCell(4);
        if (record1.getR152_FIG_BAL_BWP2() != null) {
            R152Cell2.setCellValue(record1.getR152_FIG_BAL_BWP2().doubleValue());
            R152Cell2.setCellStyle(numberStyle);
        } else {
            R152Cell2.setCellValue("");
            R152Cell2.setCellStyle(textStyle);
        }
        Cell R152Cell3 = row.createCell(5);
        if (record1.getR152_AMT_ADJ_BWP1() != null) {
            R152Cell3.setCellValue(record1.getR152_AMT_ADJ_BWP1().doubleValue());
            R152Cell3.setCellStyle(numberStyle);
        } else {
            R152Cell3.setCellValue("");
            R152Cell3.setCellStyle(textStyle);
        }
        Cell R152Cell4 = row.createCell(6);
        if (record1.getR152_AMT_ADJ_BWP2() != null) {
            R152Cell4.setCellValue(record1.getR152_AMT_ADJ_BWP2().doubleValue());
            R152Cell4.setCellStyle(numberStyle);
        } else {
            R152Cell4.setCellValue("");
            R152Cell4.setCellStyle(textStyle);
        }
        Cell R152Cell5 = row.createCell(7);
        if (record1.getR152_NET_AMT_BWP1() != null) {
            R152Cell5.setCellValue(record1.getR152_NET_AMT_BWP1().doubleValue());
            R152Cell5.setCellStyle(numberStyle);
        } else {
            R152Cell5.setCellValue("");
            R152Cell5.setCellStyle(textStyle);
        }
        Cell R152Cell6 = row.createCell(8);
        if (record1.getR152_NET_AMT_BWP2() != null) {
            R152Cell6.setCellValue(record1.getR152_NET_AMT_BWP2().doubleValue());
            R152Cell6.setCellStyle(numberStyle);
        } else {
            R152Cell6.setCellValue("");
            R152Cell6.setCellStyle(textStyle);
        }
        Cell R152Cell7 = row.createCell(9);
        if (record1.getR152_BAL_SUB_BWP1() != null) {
            R152Cell7.setCellValue(record1.getR152_BAL_SUB_BWP1().doubleValue());
            R152Cell7.setCellStyle(numberStyle);
        } else {
            R152Cell7.setCellValue("");
            R152Cell7.setCellStyle(textStyle);
        }
        Cell R152Cell8 = row.createCell(10);
        if (record1.getR152_BAL_SUB_BWP2() != null) {
            R152Cell8.setCellValue(record1.getR152_BAL_SUB_BWP2().doubleValue());
            R152Cell8.setCellStyle(numberStyle);
        } else {
            R152Cell8.setCellValue("");
            R152Cell8.setCellStyle(textStyle);
        }
        Cell R152Cell9 = row.createCell(11);
        if (record1.getR152_BAL_ACT_SUB_BWP1() != null) {
            R152Cell9.setCellValue(record1.getR152_BAL_ACT_SUB_BWP1().doubleValue());
            R152Cell9.setCellStyle(numberStyle);
        } else {
            R152Cell9.setCellValue("");
            R152Cell9.setCellStyle(textStyle);
        }
        Cell R152Cell10 = row.createCell(12);
        if (record1.getR152_BAL_ACT_SUB_BWP2() != null) {
            R152Cell10.setCellValue(record1.getR152_BAL_ACT_SUB_BWP2().doubleValue());
            R152Cell10.setCellStyle(numberStyle);
        } else {
            R152Cell10.setCellValue("");
            R152Cell10.setCellStyle(textStyle);
        }

        /* ================= R153 ================= */
        row = sheet.getRow(152);
        Cell R153Cell1 = row.createCell(3);
        if (record1.getR153_FIG_BAL_BWP1() != null) {
            R153Cell1.setCellValue(record1.getR153_FIG_BAL_BWP1().doubleValue());
            R153Cell1.setCellStyle(numberStyle);
        } else {
            R153Cell1.setCellValue("");
            R153Cell1.setCellStyle(textStyle);
        }
        Cell R153Cell2 = row.createCell(4);
        if (record1.getR153_FIG_BAL_BWP2() != null) {
            R153Cell2.setCellValue(record1.getR153_FIG_BAL_BWP2().doubleValue());
            R153Cell2.setCellStyle(numberStyle);
        } else {
            R153Cell2.setCellValue("");
            R153Cell2.setCellStyle(textStyle);
        }
        Cell R153Cell3 = row.createCell(5);
        if (record1.getR153_AMT_ADJ_BWP1() != null) {
            R153Cell3.setCellValue(record1.getR153_AMT_ADJ_BWP1().doubleValue());
            R153Cell3.setCellStyle(numberStyle);
        } else {
            R153Cell3.setCellValue("");
            R153Cell3.setCellStyle(textStyle);
        }
        Cell R153Cell4 = row.createCell(6);
        if (record1.getR153_AMT_ADJ_BWP2() != null) {
            R153Cell4.setCellValue(record1.getR153_AMT_ADJ_BWP2().doubleValue());
            R153Cell4.setCellStyle(numberStyle);
        } else {
            R153Cell4.setCellValue("");
            R153Cell4.setCellStyle(textStyle);
        }
        Cell R153Cell5 = row.createCell(7);
        if (record1.getR153_NET_AMT_BWP1() != null) {
            R153Cell5.setCellValue(record1.getR153_NET_AMT_BWP1().doubleValue());
            R153Cell5.setCellStyle(numberStyle);
        } else {
            R153Cell5.setCellValue("");
            R153Cell5.setCellStyle(textStyle);
        }
        Cell R153Cell6 = row.createCell(8);
        if (record1.getR153_NET_AMT_BWP2() != null) {
            R153Cell6.setCellValue(record1.getR153_NET_AMT_BWP2().doubleValue());
            R153Cell6.setCellStyle(numberStyle);
        } else {
            R153Cell6.setCellValue("");
            R153Cell6.setCellStyle(textStyle);
        }
        Cell R153Cell7 = row.createCell(9);
        if (record1.getR153_BAL_SUB_BWP1() != null) {
            R153Cell7.setCellValue(record1.getR153_BAL_SUB_BWP1().doubleValue());
            R153Cell7.setCellStyle(numberStyle);
        } else {
            R153Cell7.setCellValue("");
            R153Cell7.setCellStyle(textStyle);
        }
        Cell R153Cell8 = row.createCell(10);
        if (record1.getR153_BAL_SUB_BWP2() != null) {
            R153Cell8.setCellValue(record1.getR153_BAL_SUB_BWP2().doubleValue());
            R153Cell8.setCellStyle(numberStyle);
        } else {
            R153Cell8.setCellValue("");
            R153Cell8.setCellStyle(textStyle);
        }
        Cell R153Cell9 = row.createCell(11);
        if (record1.getR153_BAL_ACT_SUB_BWP1() != null) {
            R153Cell9.setCellValue(record1.getR153_BAL_ACT_SUB_BWP1().doubleValue());
            R153Cell9.setCellStyle(numberStyle);
        } else {
            R153Cell9.setCellValue("");
            R153Cell9.setCellStyle(textStyle);
        }
        Cell R153Cell10 = row.createCell(12);
        if (record1.getR153_BAL_ACT_SUB_BWP2() != null) {
            R153Cell10.setCellValue(record1.getR153_BAL_ACT_SUB_BWP2().doubleValue());
            R153Cell10.setCellStyle(numberStyle);
        } else {
            R153Cell10.setCellValue("");
            R153Cell10.setCellStyle(textStyle);
        }

        /* ================= R154 ================= */
        row = sheet.getRow(153);
        Cell R154Cell1 = row.createCell(3);
        if (record1.getR154_FIG_BAL_BWP1() != null) {
            R154Cell1.setCellValue(record1.getR154_FIG_BAL_BWP1().doubleValue());
            R154Cell1.setCellStyle(numberStyle);
        } else {
            R154Cell1.setCellValue("");
            R154Cell1.setCellStyle(textStyle);
        }
        Cell R154Cell2 = row.createCell(4);
        if (record1.getR154_FIG_BAL_BWP2() != null) {
            R154Cell2.setCellValue(record1.getR154_FIG_BAL_BWP2().doubleValue());
            R154Cell2.setCellStyle(numberStyle);
        } else {
            R154Cell2.setCellValue("");
            R154Cell2.setCellStyle(textStyle);
        }
        Cell R154Cell3 = row.createCell(5);
        if (record1.getR154_AMT_ADJ_BWP1() != null) {
            R154Cell3.setCellValue(record1.getR154_AMT_ADJ_BWP1().doubleValue());
            R154Cell3.setCellStyle(numberStyle);
        } else {
            R154Cell3.setCellValue("");
            R154Cell3.setCellStyle(textStyle);
        }
        Cell R154Cell4 = row.createCell(6);
        if (record1.getR154_AMT_ADJ_BWP2() != null) {
            R154Cell4.setCellValue(record1.getR154_AMT_ADJ_BWP2().doubleValue());
            R154Cell4.setCellStyle(numberStyle);
        } else {
            R154Cell4.setCellValue("");
            R154Cell4.setCellStyle(textStyle);
        }
        Cell R154Cell5 = row.createCell(7);
        if (record1.getR154_NET_AMT_BWP1() != null) {
            R154Cell5.setCellValue(record1.getR154_NET_AMT_BWP1().doubleValue());
            R154Cell5.setCellStyle(numberStyle);
        } else {
            R154Cell5.setCellValue("");
            R154Cell5.setCellStyle(textStyle);
        }
        Cell R154Cell6 = row.createCell(8);
        if (record1.getR154_NET_AMT_BWP2() != null) {
            R154Cell6.setCellValue(record1.getR154_NET_AMT_BWP2().doubleValue());
            R154Cell6.setCellStyle(numberStyle);
        } else {
            R154Cell6.setCellValue("");
            R154Cell6.setCellStyle(textStyle);
        }
        Cell R154Cell7 = row.createCell(9);
        if (record1.getR154_BAL_SUB_BWP1() != null) {
            R154Cell7.setCellValue(record1.getR154_BAL_SUB_BWP1().doubleValue());
            R154Cell7.setCellStyle(numberStyle);
        } else {
            R154Cell7.setCellValue("");
            R154Cell7.setCellStyle(textStyle);
        }
        Cell R154Cell8 = row.createCell(10);
        if (record1.getR154_BAL_SUB_BWP2() != null) {
            R154Cell8.setCellValue(record1.getR154_BAL_SUB_BWP2().doubleValue());
            R154Cell8.setCellStyle(numberStyle);
        } else {
            R154Cell8.setCellValue("");
            R154Cell8.setCellStyle(textStyle);
        }
        Cell R154Cell9 = row.createCell(11);
        if (record1.getR154_BAL_ACT_SUB_BWP1() != null) {
            R154Cell9.setCellValue(record1.getR154_BAL_ACT_SUB_BWP1().doubleValue());
            R154Cell9.setCellStyle(numberStyle);
        } else {
            R154Cell9.setCellValue("");
            R154Cell9.setCellStyle(textStyle);
        }
        Cell R154Cell10 = row.createCell(12);
        if (record1.getR154_BAL_ACT_SUB_BWP2() != null) {
            R154Cell10.setCellValue(record1.getR154_BAL_ACT_SUB_BWP2().doubleValue());
            R154Cell10.setCellStyle(numberStyle);
        } else {
            R154Cell10.setCellValue("");
            R154Cell10.setCellStyle(textStyle);
        }

        /* ================= R155 ================= */
        row = sheet.getRow(154);
        Cell R155Cell1 = row.createCell(3);
        if (record1.getR155_FIG_BAL_BWP1() != null) {
            R155Cell1.setCellValue(record1.getR155_FIG_BAL_BWP1().doubleValue());
            R155Cell1.setCellStyle(numberStyle);
        } else {
            R155Cell1.setCellValue("");
            R155Cell1.setCellStyle(textStyle);
        }
        Cell R155Cell2 = row.createCell(4);
        if (record1.getR155_FIG_BAL_BWP2() != null) {
            R155Cell2.setCellValue(record1.getR155_FIG_BAL_BWP2().doubleValue());
            R155Cell2.setCellStyle(numberStyle);
        } else {
            R155Cell2.setCellValue("");
            R155Cell2.setCellStyle(textStyle);
        }
        Cell R155Cell3 = row.createCell(5);
        if (record1.getR155_AMT_ADJ_BWP1() != null) {
            R155Cell3.setCellValue(record1.getR155_AMT_ADJ_BWP1().doubleValue());
            R155Cell3.setCellStyle(numberStyle);
        } else {
            R155Cell3.setCellValue("");
            R155Cell3.setCellStyle(textStyle);
        }
        Cell R155Cell4 = row.createCell(6);
        if (record1.getR155_AMT_ADJ_BWP2() != null) {
            R155Cell4.setCellValue(record1.getR155_AMT_ADJ_BWP2().doubleValue());
            R155Cell4.setCellStyle(numberStyle);
        } else {
            R155Cell4.setCellValue("");
            R155Cell4.setCellStyle(textStyle);
        }
        Cell R155Cell5 = row.createCell(7);
        if (record1.getR155_NET_AMT_BWP1() != null) {
            R155Cell5.setCellValue(record1.getR155_NET_AMT_BWP1().doubleValue());
            R155Cell5.setCellStyle(numberStyle);
        } else {
            R155Cell5.setCellValue("");
            R155Cell5.setCellStyle(textStyle);
        }
        Cell R155Cell6 = row.createCell(8);
        if (record1.getR155_NET_AMT_BWP2() != null) {
            R155Cell6.setCellValue(record1.getR155_NET_AMT_BWP2().doubleValue());
            R155Cell6.setCellStyle(numberStyle);
        } else {
            R155Cell6.setCellValue("");
            R155Cell6.setCellStyle(textStyle);
        }
        Cell R155Cell7 = row.createCell(9);
        if (record1.getR155_BAL_SUB_BWP1() != null) {
            R155Cell7.setCellValue(record1.getR155_BAL_SUB_BWP1().doubleValue());
            R155Cell7.setCellStyle(numberStyle);
        } else {
            R155Cell7.setCellValue("");
            R155Cell7.setCellStyle(textStyle);
        }
        Cell R155Cell8 = row.createCell(10);
        if (record1.getR155_BAL_SUB_BWP2() != null) {
            R155Cell8.setCellValue(record1.getR155_BAL_SUB_BWP2().doubleValue());
            R155Cell8.setCellStyle(numberStyle);
        } else {
            R155Cell8.setCellValue("");
            R155Cell8.setCellStyle(textStyle);
        }
        Cell R155Cell9 = row.createCell(11);
        if (record1.getR155_BAL_ACT_SUB_BWP1() != null) {
            R155Cell9.setCellValue(record1.getR155_BAL_ACT_SUB_BWP1().doubleValue());
            R155Cell9.setCellStyle(numberStyle);
        } else {
            R155Cell9.setCellValue("");
            R155Cell9.setCellStyle(textStyle);
        }
        Cell R155Cell10 = row.createCell(12);
        if (record1.getR155_BAL_ACT_SUB_BWP2() != null) {
            R155Cell10.setCellValue(record1.getR155_BAL_ACT_SUB_BWP2().doubleValue());
            R155Cell10.setCellStyle(numberStyle);
        } else {
            R155Cell10.setCellValue("");
            R155Cell10.setCellStyle(textStyle);
        }

        /* ================= R156 ================= */
        row = sheet.getRow(155);
        Cell R156Cell1 = row.createCell(3);
        if (record1.getR156_FIG_BAL_BWP1() != null) {
            R156Cell1.setCellValue(record1.getR156_FIG_BAL_BWP1().doubleValue());
            R156Cell1.setCellStyle(numberStyle);
        } else {
            R156Cell1.setCellValue("");
            R156Cell1.setCellStyle(textStyle);
        }
        Cell R156Cell2 = row.createCell(4);
        if (record1.getR156_FIG_BAL_BWP2() != null) {
            R156Cell2.setCellValue(record1.getR156_FIG_BAL_BWP2().doubleValue());
            R156Cell2.setCellStyle(numberStyle);
        } else {
            R156Cell2.setCellValue("");
            R156Cell2.setCellStyle(textStyle);
        }
        Cell R156Cell3 = row.createCell(5);
        if (record1.getR156_AMT_ADJ_BWP1() != null) {
            R156Cell3.setCellValue(record1.getR156_AMT_ADJ_BWP1().doubleValue());
            R156Cell3.setCellStyle(numberStyle);
        } else {
            R156Cell3.setCellValue("");
            R156Cell3.setCellStyle(textStyle);
        }
        Cell R156Cell4 = row.createCell(6);
        if (record1.getR156_AMT_ADJ_BWP2() != null) {
            R156Cell4.setCellValue(record1.getR156_AMT_ADJ_BWP2().doubleValue());
            R156Cell4.setCellStyle(numberStyle);
        } else {
            R156Cell4.setCellValue("");
            R156Cell4.setCellStyle(textStyle);
        }
        Cell R156Cell5 = row.createCell(7);
        if (record1.getR156_NET_AMT_BWP1() != null) {
            R156Cell5.setCellValue(record1.getR156_NET_AMT_BWP1().doubleValue());
            R156Cell5.setCellStyle(numberStyle);
        } else {
            R156Cell5.setCellValue("");
            R156Cell5.setCellStyle(textStyle);
        }
        Cell R156Cell6 = row.createCell(8);
        if (record1.getR156_NET_AMT_BWP2() != null) {
            R156Cell6.setCellValue(record1.getR156_NET_AMT_BWP2().doubleValue());
            R156Cell6.setCellStyle(numberStyle);
        } else {
            R156Cell6.setCellValue("");
            R156Cell6.setCellStyle(textStyle);
        }
        Cell R156Cell7 = row.createCell(9);
        if (record1.getR156_BAL_SUB_BWP1() != null) {
            R156Cell7.setCellValue(record1.getR156_BAL_SUB_BWP1().doubleValue());
            R156Cell7.setCellStyle(numberStyle);
        } else {
            R156Cell7.setCellValue("");
            R156Cell7.setCellStyle(textStyle);
        }
        Cell R156Cell8 = row.createCell(10);
        if (record1.getR156_BAL_SUB_BWP2() != null) {
            R156Cell8.setCellValue(record1.getR156_BAL_SUB_BWP2().doubleValue());
            R156Cell8.setCellStyle(numberStyle);
        } else {
            R156Cell8.setCellValue("");
            R156Cell8.setCellStyle(textStyle);
        }
        Cell R156Cell9 = row.createCell(11);
        if (record1.getR156_BAL_ACT_SUB_BWP1() != null) {
            R156Cell9.setCellValue(record1.getR156_BAL_ACT_SUB_BWP1().doubleValue());
            R156Cell9.setCellStyle(numberStyle);
        } else {
            R156Cell9.setCellValue("");
            R156Cell9.setCellStyle(textStyle);
        }
        Cell R156Cell10 = row.createCell(12);
        if (record1.getR156_BAL_ACT_SUB_BWP2() != null) {
            R156Cell10.setCellValue(record1.getR156_BAL_ACT_SUB_BWP2().doubleValue());
            R156Cell10.setCellStyle(numberStyle);
        } else {
            R156Cell10.setCellValue("");
            R156Cell10.setCellStyle(textStyle);
        }

        /* ================= R157 ================= */
        row = sheet.getRow(156);
        Cell R157Cell1 = row.createCell(3);
        if (record1.getR157_FIG_BAL_BWP1() != null) {
            R157Cell1.setCellValue(record1.getR157_FIG_BAL_BWP1().doubleValue());
            R157Cell1.setCellStyle(numberStyle);
        } else {
            R157Cell1.setCellValue("");
            R157Cell1.setCellStyle(textStyle);
        }
        Cell R157Cell2 = row.createCell(4);
        if (record1.getR157_FIG_BAL_BWP2() != null) {
            R157Cell2.setCellValue(record1.getR157_FIG_BAL_BWP2().doubleValue());
            R157Cell2.setCellStyle(numberStyle);
        } else {
            R157Cell2.setCellValue("");
            R157Cell2.setCellStyle(textStyle);
        }
        Cell R157Cell3 = row.createCell(5);
        if (record1.getR157_AMT_ADJ_BWP1() != null) {
            R157Cell3.setCellValue(record1.getR157_AMT_ADJ_BWP1().doubleValue());
            R157Cell3.setCellStyle(numberStyle);
        } else {
            R157Cell3.setCellValue("");
            R157Cell3.setCellStyle(textStyle);
        }
        Cell R157Cell4 = row.createCell(6);
        if (record1.getR157_AMT_ADJ_BWP2() != null) {
            R157Cell4.setCellValue(record1.getR157_AMT_ADJ_BWP2().doubleValue());
            R157Cell4.setCellStyle(numberStyle);
        } else {
            R157Cell4.setCellValue("");
            R157Cell4.setCellStyle(textStyle);
        }
        Cell R157Cell5 = row.createCell(7);
        if (record1.getR157_NET_AMT_BWP1() != null) {
            R157Cell5.setCellValue(record1.getR157_NET_AMT_BWP1().doubleValue());
            R157Cell5.setCellStyle(numberStyle);
        } else {
            R157Cell5.setCellValue("");
            R157Cell5.setCellStyle(textStyle);
        }
        Cell R157Cell6 = row.createCell(8);
        if (record1.getR157_NET_AMT_BWP2() != null) {
            R157Cell6.setCellValue(record1.getR157_NET_AMT_BWP2().doubleValue());
            R157Cell6.setCellStyle(numberStyle);
        } else {
            R157Cell6.setCellValue("");
            R157Cell6.setCellStyle(textStyle);
        }
        Cell R157Cell7 = row.createCell(9);
        if (record1.getR157_BAL_SUB_BWP1() != null) {
            R157Cell7.setCellValue(record1.getR157_BAL_SUB_BWP1().doubleValue());
            R157Cell7.setCellStyle(numberStyle);
        } else {
            R157Cell7.setCellValue("");
            R157Cell7.setCellStyle(textStyle);
        }
        Cell R157Cell8 = row.createCell(10);
        if (record1.getR157_BAL_SUB_BWP2() != null) {
            R157Cell8.setCellValue(record1.getR157_BAL_SUB_BWP2().doubleValue());
            R157Cell8.setCellStyle(numberStyle);
        } else {
            R157Cell8.setCellValue("");
            R157Cell8.setCellStyle(textStyle);
        }
        Cell R157Cell9 = row.createCell(11);
        if (record1.getR157_BAL_ACT_SUB_BWP1() != null) {
            R157Cell9.setCellValue(record1.getR157_BAL_ACT_SUB_BWP1().doubleValue());
            R157Cell9.setCellStyle(numberStyle);
        } else {
            R157Cell9.setCellValue("");
            R157Cell9.setCellStyle(textStyle);
        }
        Cell R157Cell10 = row.createCell(12);
        if (record1.getR157_BAL_ACT_SUB_BWP2() != null) {
            R157Cell10.setCellValue(record1.getR157_BAL_ACT_SUB_BWP2().doubleValue());
            R157Cell10.setCellStyle(numberStyle);
        } else {
            R157Cell10.setCellValue("");
            R157Cell10.setCellStyle(textStyle);
        }

        /* ================= R158 ================= */
        row = sheet.getRow(157);
        Cell R158Cell1 = row.createCell(3);
        if (record1.getR158_FIG_BAL_BWP1() != null) {
            R158Cell1.setCellValue(record1.getR158_FIG_BAL_BWP1().doubleValue());
            R158Cell1.setCellStyle(numberStyle);
        } else {
            R158Cell1.setCellValue("");
            R158Cell1.setCellStyle(textStyle);
        }
        Cell R158Cell2 = row.createCell(4);
        if (record1.getR158_FIG_BAL_BWP2() != null) {
            R158Cell2.setCellValue(record1.getR158_FIG_BAL_BWP2().doubleValue());
            R158Cell2.setCellStyle(numberStyle);
        } else {
            R158Cell2.setCellValue("");
            R158Cell2.setCellStyle(textStyle);
        }
        Cell R158Cell3 = row.createCell(5);
        if (record1.getR158_AMT_ADJ_BWP1() != null) {
            R158Cell3.setCellValue(record1.getR158_AMT_ADJ_BWP1().doubleValue());
            R158Cell3.setCellStyle(numberStyle);
        } else {
            R158Cell3.setCellValue("");
            R158Cell3.setCellStyle(textStyle);
        }
        Cell R158Cell4 = row.createCell(6);
        if (record1.getR158_AMT_ADJ_BWP2() != null) {
            R158Cell4.setCellValue(record1.getR158_AMT_ADJ_BWP2().doubleValue());
            R158Cell4.setCellStyle(numberStyle);
        } else {
            R158Cell4.setCellValue("");
            R158Cell4.setCellStyle(textStyle);
        }
        Cell R158Cell5 = row.createCell(7);
        if (record1.getR158_NET_AMT_BWP1() != null) {
            R158Cell5.setCellValue(record1.getR158_NET_AMT_BWP1().doubleValue());
            R158Cell5.setCellStyle(numberStyle);
        } else {
            R158Cell5.setCellValue("");
            R158Cell5.setCellStyle(textStyle);
        }
        Cell R158Cell6 = row.createCell(8);
        if (record1.getR158_NET_AMT_BWP2() != null) {
            R158Cell6.setCellValue(record1.getR158_NET_AMT_BWP2().doubleValue());
            R158Cell6.setCellStyle(numberStyle);
        } else {
            R158Cell6.setCellValue("");
            R158Cell6.setCellStyle(textStyle);
        }
        Cell R158Cell7 = row.createCell(9);
        if (record1.getR158_BAL_SUB_BWP1() != null) {
            R158Cell7.setCellValue(record1.getR158_BAL_SUB_BWP1().doubleValue());
            R158Cell7.setCellStyle(numberStyle);
        } else {
            R158Cell7.setCellValue("");
            R158Cell7.setCellStyle(textStyle);
        }
        Cell R158Cell8 = row.createCell(10);
        if (record1.getR158_BAL_SUB_BWP2() != null) {
            R158Cell8.setCellValue(record1.getR158_BAL_SUB_BWP2().doubleValue());
            R158Cell8.setCellStyle(numberStyle);
        } else {
            R158Cell8.setCellValue("");
            R158Cell8.setCellStyle(textStyle);
        }
        Cell R158Cell9 = row.createCell(11);
        if (record1.getR158_BAL_ACT_SUB_BWP1() != null) {
            R158Cell9.setCellValue(record1.getR158_BAL_ACT_SUB_BWP1().doubleValue());
            R158Cell9.setCellStyle(numberStyle);
        } else {
            R158Cell9.setCellValue("");
            R158Cell9.setCellStyle(textStyle);
        }
        Cell R158Cell10 = row.createCell(12);
        if (record1.getR158_BAL_ACT_SUB_BWP2() != null) {
            R158Cell10.setCellValue(record1.getR158_BAL_ACT_SUB_BWP2().doubleValue());
            R158Cell10.setCellStyle(numberStyle);
        } else {
            R158Cell10.setCellValue("");
            R158Cell10.setCellStyle(textStyle);
        }

        /* ================= R159 ================= */
        row = sheet.getRow(158);
        Cell R159Cell1 = row.createCell(3);
        if (record1.getR159_FIG_BAL_BWP1() != null) {
            R159Cell1.setCellValue(record1.getR159_FIG_BAL_BWP1().doubleValue());
            R159Cell1.setCellStyle(numberStyle);
        } else {
            R159Cell1.setCellValue("");
            R159Cell1.setCellStyle(textStyle);
        }
        Cell R159Cell2 = row.createCell(4);
        if (record1.getR159_FIG_BAL_BWP2() != null) {
            R159Cell2.setCellValue(record1.getR159_FIG_BAL_BWP2().doubleValue());
            R159Cell2.setCellStyle(numberStyle);
        } else {
            R159Cell2.setCellValue("");
            R159Cell2.setCellStyle(textStyle);
        }
        Cell R159Cell3 = row.createCell(5);
        if (record1.getR159_AMT_ADJ_BWP1() != null) {
            R159Cell3.setCellValue(record1.getR159_AMT_ADJ_BWP1().doubleValue());
            R159Cell3.setCellStyle(numberStyle);
        } else {
            R159Cell3.setCellValue("");
            R159Cell3.setCellStyle(textStyle);
        }
        Cell R159Cell4 = row.createCell(6);
        if (record1.getR159_AMT_ADJ_BWP2() != null) {
            R159Cell4.setCellValue(record1.getR159_AMT_ADJ_BWP2().doubleValue());
            R159Cell4.setCellStyle(numberStyle);
        } else {
            R159Cell4.setCellValue("");
            R159Cell4.setCellStyle(textStyle);
        }
        Cell R159Cell5 = row.createCell(7);
        if (record1.getR159_NET_AMT_BWP1() != null) {
            R159Cell5.setCellValue(record1.getR159_NET_AMT_BWP1().doubleValue());
            R159Cell5.setCellStyle(numberStyle);
        } else {
            R159Cell5.setCellValue("");
            R159Cell5.setCellStyle(textStyle);
        }
        Cell R159Cell6 = row.createCell(8);
        if (record1.getR159_NET_AMT_BWP2() != null) {
            R159Cell6.setCellValue(record1.getR159_NET_AMT_BWP2().doubleValue());
            R159Cell6.setCellStyle(numberStyle);
        } else {
            R159Cell6.setCellValue("");
            R159Cell6.setCellStyle(textStyle);
        }
        Cell R159Cell7 = row.createCell(9);
        if (record1.getR159_BAL_SUB_BWP1() != null) {
            R159Cell7.setCellValue(record1.getR159_BAL_SUB_BWP1().doubleValue());
            R159Cell7.setCellStyle(numberStyle);
        } else {
            R159Cell7.setCellValue("");
            R159Cell7.setCellStyle(textStyle);
        }
        Cell R159Cell8 = row.createCell(10);
        if (record1.getR159_BAL_SUB_BWP2() != null) {
            R159Cell8.setCellValue(record1.getR159_BAL_SUB_BWP2().doubleValue());
            R159Cell8.setCellStyle(numberStyle);
        } else {
            R159Cell8.setCellValue("");
            R159Cell8.setCellStyle(textStyle);
        }
        Cell R159Cell9 = row.createCell(11);
        if (record1.getR159_BAL_ACT_SUB_BWP1() != null) {
            R159Cell9.setCellValue(record1.getR159_BAL_ACT_SUB_BWP1().doubleValue());
            R159Cell9.setCellStyle(numberStyle);
        } else {
            R159Cell9.setCellValue("");
            R159Cell9.setCellStyle(textStyle);
        }
        Cell R159Cell10 = row.createCell(12);
        if (record1.getR159_BAL_ACT_SUB_BWP2() != null) {
            R159Cell10.setCellValue(record1.getR159_BAL_ACT_SUB_BWP2().doubleValue());
            R159Cell10.setCellStyle(numberStyle);
        } else {
            R159Cell10.setCellValue("");
            R159Cell10.setCellStyle(textStyle);
        }

        /* ================= R160 ================= */
        row = sheet.getRow(159);
        Cell R160Cell1 = row.createCell(3);
        if (record1.getR160_FIG_BAL_BWP1() != null) {
            R160Cell1.setCellValue(record1.getR160_FIG_BAL_BWP1().doubleValue());
            R160Cell1.setCellStyle(numberStyle);
        } else {
            R160Cell1.setCellValue("");
            R160Cell1.setCellStyle(textStyle);
        }
        Cell R160Cell2 = row.createCell(4);
        if (record1.getR160_FIG_BAL_BWP2() != null) {
            R160Cell2.setCellValue(record1.getR160_FIG_BAL_BWP2().doubleValue());
            R160Cell2.setCellStyle(numberStyle);
        } else {
            R160Cell2.setCellValue("");
            R160Cell2.setCellStyle(textStyle);
        }
        Cell R160Cell3 = row.createCell(5);
        if (record1.getR160_AMT_ADJ_BWP1() != null) {
            R160Cell3.setCellValue(record1.getR160_AMT_ADJ_BWP1().doubleValue());
            R160Cell3.setCellStyle(numberStyle);
        } else {
            R160Cell3.setCellValue("");
            R160Cell3.setCellStyle(textStyle);
        }
        Cell R160Cell4 = row.createCell(6);
        if (record1.getR160_AMT_ADJ_BWP2() != null) {
            R160Cell4.setCellValue(record1.getR160_AMT_ADJ_BWP2().doubleValue());
            R160Cell4.setCellStyle(numberStyle);
        } else {
            R160Cell4.setCellValue("");
            R160Cell4.setCellStyle(textStyle);
        }
        Cell R160Cell5 = row.createCell(7);
        if (record1.getR160_NET_AMT_BWP1() != null) {
            R160Cell5.setCellValue(record1.getR160_NET_AMT_BWP1().doubleValue());
            R160Cell5.setCellStyle(numberStyle);
        } else {
            R160Cell5.setCellValue("");
            R160Cell5.setCellStyle(textStyle);
        }
        Cell R160Cell6 = row.createCell(8);
        if (record1.getR160_NET_AMT_BWP2() != null) {
            R160Cell6.setCellValue(record1.getR160_NET_AMT_BWP2().doubleValue());
            R160Cell6.setCellStyle(numberStyle);
        } else {
            R160Cell6.setCellValue("");
            R160Cell6.setCellStyle(textStyle);
        }
        Cell R160Cell7 = row.createCell(9);
        if (record1.getR160_BAL_SUB_BWP1() != null) {
            R160Cell7.setCellValue(record1.getR160_BAL_SUB_BWP1().doubleValue());
            R160Cell7.setCellStyle(numberStyle);
        } else {
            R160Cell7.setCellValue("");
            R160Cell7.setCellStyle(textStyle);
        }
        Cell R160Cell8 = row.createCell(10);
        if (record1.getR160_BAL_SUB_BWP2() != null) {
            R160Cell8.setCellValue(record1.getR160_BAL_SUB_BWP2().doubleValue());
            R160Cell8.setCellStyle(numberStyle);
        } else {
            R160Cell8.setCellValue("");
            R160Cell8.setCellStyle(textStyle);
        }
        Cell R160Cell9 = row.createCell(11);
        if (record1.getR160_BAL_ACT_SUB_BWP1() != null) {
            R160Cell9.setCellValue(record1.getR160_BAL_ACT_SUB_BWP1().doubleValue());
            R160Cell9.setCellStyle(numberStyle);
        } else {
            R160Cell9.setCellValue("");
            R160Cell9.setCellStyle(textStyle);
        }
        Cell R160Cell10 = row.createCell(12);
        if (record1.getR160_BAL_ACT_SUB_BWP2() != null) {
            R160Cell10.setCellValue(record1.getR160_BAL_ACT_SUB_BWP2().doubleValue());
            R160Cell10.setCellStyle(numberStyle);
        } else {
            R160Cell10.setCellValue("");
            R160Cell10.setCellStyle(textStyle);
        }

        /* ================= R161 ================= */
        row = sheet.getRow(160);
        Cell R161Cell1 = row.createCell(3);
        if (record1.getR161_FIG_BAL_BWP1() != null) {
            R161Cell1.setCellValue(record1.getR161_FIG_BAL_BWP1().doubleValue());
            R161Cell1.setCellStyle(numberStyle);
        } else {
            R161Cell1.setCellValue("");
            R161Cell1.setCellStyle(textStyle);
        }
        Cell R161Cell2 = row.createCell(4);
        if (record1.getR161_FIG_BAL_BWP2() != null) {
            R161Cell2.setCellValue(record1.getR161_FIG_BAL_BWP2().doubleValue());
            R161Cell2.setCellStyle(numberStyle);
        } else {
            R161Cell2.setCellValue("");
            R161Cell2.setCellStyle(textStyle);
        }
        Cell R161Cell3 = row.createCell(5);
        if (record1.getR161_AMT_ADJ_BWP1() != null) {
            R161Cell3.setCellValue(record1.getR161_AMT_ADJ_BWP1().doubleValue());
            R161Cell3.setCellStyle(numberStyle);
        } else {
            R161Cell3.setCellValue("");
            R161Cell3.setCellStyle(textStyle);
        }
        Cell R161Cell4 = row.createCell(6);
        if (record1.getR161_AMT_ADJ_BWP2() != null) {
            R161Cell4.setCellValue(record1.getR161_AMT_ADJ_BWP2().doubleValue());
            R161Cell4.setCellStyle(numberStyle);
        } else {
            R161Cell4.setCellValue("");
            R161Cell4.setCellStyle(textStyle);
        }
        Cell R161Cell5 = row.createCell(7);
        if (record1.getR161_NET_AMT_BWP1() != null) {
            R161Cell5.setCellValue(record1.getR161_NET_AMT_BWP1().doubleValue());
            R161Cell5.setCellStyle(numberStyle);
        } else {
            R161Cell5.setCellValue("");
            R161Cell5.setCellStyle(textStyle);
        }
        Cell R161Cell6 = row.createCell(8);
        if (record1.getR161_NET_AMT_BWP2() != null) {
            R161Cell6.setCellValue(record1.getR161_NET_AMT_BWP2().doubleValue());
            R161Cell6.setCellStyle(numberStyle);
        } else {
            R161Cell6.setCellValue("");
            R161Cell6.setCellStyle(textStyle);
        }
        Cell R161Cell7 = row.createCell(9);
        if (record1.getR161_BAL_SUB_BWP1() != null) {
            R161Cell7.setCellValue(record1.getR161_BAL_SUB_BWP1().doubleValue());
            R161Cell7.setCellStyle(numberStyle);
        } else {
            R161Cell7.setCellValue("");
            R161Cell7.setCellStyle(textStyle);
        }
        Cell R161Cell8 = row.createCell(10);
        if (record1.getR161_BAL_SUB_BWP2() != null) {
            R161Cell8.setCellValue(record1.getR161_BAL_SUB_BWP2().doubleValue());
            R161Cell8.setCellStyle(numberStyle);
        } else {
            R161Cell8.setCellValue("");
            R161Cell8.setCellStyle(textStyle);
        }
        Cell R161Cell9 = row.createCell(11);
        if (record1.getR161_BAL_ACT_SUB_BWP1() != null) {
            R161Cell9.setCellValue(record1.getR161_BAL_ACT_SUB_BWP1().doubleValue());
            R161Cell9.setCellStyle(numberStyle);
        } else {
            R161Cell9.setCellValue("");
            R161Cell9.setCellStyle(textStyle);
        }
        Cell R161Cell10 = row.createCell(12);
        if (record1.getR161_BAL_ACT_SUB_BWP2() != null) {
            R161Cell10.setCellValue(record1.getR161_BAL_ACT_SUB_BWP2().doubleValue());
            R161Cell10.setCellStyle(numberStyle);
        } else {
            R161Cell10.setCellValue("");
            R161Cell10.setCellStyle(textStyle);
        }

        /* ================= R162 ================= */
        row = sheet.getRow(161);
        Cell R162Cell1 = row.createCell(3);
        if (record1.getR162_FIG_BAL_BWP1() != null) {
            R162Cell1.setCellValue(record1.getR162_FIG_BAL_BWP1().doubleValue());
            R162Cell1.setCellStyle(numberStyle);
        } else {
            R162Cell1.setCellValue("");
            R162Cell1.setCellStyle(textStyle);
        }
        Cell R162Cell2 = row.createCell(4);
        if (record1.getR162_FIG_BAL_BWP2() != null) {
            R162Cell2.setCellValue(record1.getR162_FIG_BAL_BWP2().doubleValue());
            R162Cell2.setCellStyle(numberStyle);
        } else {
            R162Cell2.setCellValue("");
            R162Cell2.setCellStyle(textStyle);
        }
        Cell R162Cell3 = row.createCell(5);
        if (record1.getR162_AMT_ADJ_BWP1() != null) {
            R162Cell3.setCellValue(record1.getR162_AMT_ADJ_BWP1().doubleValue());
            R162Cell3.setCellStyle(numberStyle);
        } else {
            R162Cell3.setCellValue("");
            R162Cell3.setCellStyle(textStyle);
        }
        Cell R162Cell4 = row.createCell(6);
        if (record1.getR162_AMT_ADJ_BWP2() != null) {
            R162Cell4.setCellValue(record1.getR162_AMT_ADJ_BWP2().doubleValue());
            R162Cell4.setCellStyle(numberStyle);
        } else {
            R162Cell4.setCellValue("");
            R162Cell4.setCellStyle(textStyle);
        }
        Cell R162Cell5 = row.createCell(7);
        if (record1.getR162_NET_AMT_BWP1() != null) {
            R162Cell5.setCellValue(record1.getR162_NET_AMT_BWP1().doubleValue());
            R162Cell5.setCellStyle(numberStyle);
        } else {
            R162Cell5.setCellValue("");
            R162Cell5.setCellStyle(textStyle);
        }
        Cell R162Cell6 = row.createCell(8);
        if (record1.getR162_NET_AMT_BWP2() != null) {
            R162Cell6.setCellValue(record1.getR162_NET_AMT_BWP2().doubleValue());
            R162Cell6.setCellStyle(numberStyle);
        } else {
            R162Cell6.setCellValue("");
            R162Cell6.setCellStyle(textStyle);
        }
        Cell R162Cell7 = row.createCell(9);
        if (record1.getR162_BAL_SUB_BWP1() != null) {
            R162Cell7.setCellValue(record1.getR162_BAL_SUB_BWP1().doubleValue());
            R162Cell7.setCellStyle(numberStyle);
        } else {
            R162Cell7.setCellValue("");
            R162Cell7.setCellStyle(textStyle);
        }
        Cell R162Cell8 = row.createCell(10);
        if (record1.getR162_BAL_SUB_BWP2() != null) {
            R162Cell8.setCellValue(record1.getR162_BAL_SUB_BWP2().doubleValue());
            R162Cell8.setCellStyle(numberStyle);
        } else {
            R162Cell8.setCellValue("");
            R162Cell8.setCellStyle(textStyle);
        }
        Cell R162Cell9 = row.createCell(11);
        if (record1.getR162_BAL_ACT_SUB_BWP1() != null) {
            R162Cell9.setCellValue(record1.getR162_BAL_ACT_SUB_BWP1().doubleValue());
            R162Cell9.setCellStyle(numberStyle);
        } else {
            R162Cell9.setCellValue("");
            R162Cell9.setCellStyle(textStyle);
        }
        Cell R162Cell10 = row.createCell(12);
        if (record1.getR162_BAL_ACT_SUB_BWP2() != null) {
            R162Cell10.setCellValue(record1.getR162_BAL_ACT_SUB_BWP2().doubleValue());
            R162Cell10.setCellStyle(numberStyle);
        } else {
            R162Cell10.setCellValue("");
            R162Cell10.setCellStyle(textStyle);
        }

        /* ================= R163 ================= */
        row = sheet.getRow(162);
        Cell R163Cell1 = row.createCell(3);
        if (record1.getR163_FIG_BAL_BWP1() != null) {
            R163Cell1.setCellValue(record1.getR163_FIG_BAL_BWP1().doubleValue());
            R163Cell1.setCellStyle(numberStyle);
        } else {
            R163Cell1.setCellValue("");
            R163Cell1.setCellStyle(textStyle);
        }
        Cell R163Cell2 = row.createCell(4);
        if (record1.getR163_FIG_BAL_BWP2() != null) {
            R163Cell2.setCellValue(record1.getR163_FIG_BAL_BWP2().doubleValue());
            R163Cell2.setCellStyle(numberStyle);
        } else {
            R163Cell2.setCellValue("");
            R163Cell2.setCellStyle(textStyle);
        }
        Cell R163Cell3 = row.createCell(5);
        if (record1.getR163_AMT_ADJ_BWP1() != null) {
            R163Cell3.setCellValue(record1.getR163_AMT_ADJ_BWP1().doubleValue());
            R163Cell3.setCellStyle(numberStyle);
        } else {
            R163Cell3.setCellValue("");
            R163Cell3.setCellStyle(textStyle);
        }
        Cell R163Cell4 = row.createCell(6);
        if (record1.getR163_AMT_ADJ_BWP2() != null) {
            R163Cell4.setCellValue(record1.getR163_AMT_ADJ_BWP2().doubleValue());
            R163Cell4.setCellStyle(numberStyle);
        } else {
            R163Cell4.setCellValue("");
            R163Cell4.setCellStyle(textStyle);
        }
        Cell R163Cell5 = row.createCell(7);
        if (record1.getR163_NET_AMT_BWP1() != null) {
            R163Cell5.setCellValue(record1.getR163_NET_AMT_BWP1().doubleValue());
            R163Cell5.setCellStyle(numberStyle);
        } else {
            R163Cell5.setCellValue("");
            R163Cell5.setCellStyle(textStyle);
        }
        Cell R163Cell6 = row.createCell(8);
        if (record1.getR163_NET_AMT_BWP2() != null) {
            R163Cell6.setCellValue(record1.getR163_NET_AMT_BWP2().doubleValue());
            R163Cell6.setCellStyle(numberStyle);
        } else {
            R163Cell6.setCellValue("");
            R163Cell6.setCellStyle(textStyle);
        }
        Cell R163Cell7 = row.createCell(9);
        if (record1.getR163_BAL_SUB_BWP1() != null) {
            R163Cell7.setCellValue(record1.getR163_BAL_SUB_BWP1().doubleValue());
            R163Cell7.setCellStyle(numberStyle);
        } else {
            R163Cell7.setCellValue("");
            R163Cell7.setCellStyle(textStyle);
        }
        Cell R163Cell8 = row.createCell(10);
        if (record1.getR163_BAL_SUB_BWP2() != null) {
            R163Cell8.setCellValue(record1.getR163_BAL_SUB_BWP2().doubleValue());
            R163Cell8.setCellStyle(numberStyle);
        } else {
            R163Cell8.setCellValue("");
            R163Cell8.setCellStyle(textStyle);
        }
        Cell R163Cell9 = row.createCell(11);
        if (record1.getR163_BAL_ACT_SUB_BWP1() != null) {
            R163Cell9.setCellValue(record1.getR163_BAL_ACT_SUB_BWP1().doubleValue());
            R163Cell9.setCellStyle(numberStyle);
        } else {
            R163Cell9.setCellValue("");
            R163Cell9.setCellStyle(textStyle);
        }
        Cell R163Cell10 = row.createCell(12);
        if (record1.getR163_BAL_ACT_SUB_BWP2() != null) {
            R163Cell10.setCellValue(record1.getR163_BAL_ACT_SUB_BWP2().doubleValue());
            R163Cell10.setCellStyle(numberStyle);
        } else {
            R163Cell10.setCellValue("");
            R163Cell10.setCellStyle(textStyle);
        }

        /* ================= R164 ================= */
        row = sheet.getRow(163);
        Cell R164Cell1 = row.createCell(3);
        if (record1.getR164_FIG_BAL_BWP1() != null) {
            R164Cell1.setCellValue(record1.getR164_FIG_BAL_BWP1().doubleValue());
            R164Cell1.setCellStyle(numberStyle);
        } else {
            R164Cell1.setCellValue("");
            R164Cell1.setCellStyle(textStyle);
        }
        Cell R164Cell2 = row.createCell(4);
        if (record1.getR164_FIG_BAL_BWP2() != null) {
            R164Cell2.setCellValue(record1.getR164_FIG_BAL_BWP2().doubleValue());
            R164Cell2.setCellStyle(numberStyle);
        } else {
            R164Cell2.setCellValue("");
            R164Cell2.setCellStyle(textStyle);
        }
        Cell R164Cell3 = row.createCell(5);
        if (record1.getR164_AMT_ADJ_BWP1() != null) {
            R164Cell3.setCellValue(record1.getR164_AMT_ADJ_BWP1().doubleValue());
            R164Cell3.setCellStyle(numberStyle);
        } else {
            R164Cell3.setCellValue("");
            R164Cell3.setCellStyle(textStyle);
        }
        Cell R164Cell4 = row.createCell(6);
        if (record1.getR164_AMT_ADJ_BWP2() != null) {
            R164Cell4.setCellValue(record1.getR164_AMT_ADJ_BWP2().doubleValue());
            R164Cell4.setCellStyle(numberStyle);
        } else {
            R164Cell4.setCellValue("");
            R164Cell4.setCellStyle(textStyle);
        }
        Cell R164Cell5 = row.createCell(7);
        if (record1.getR164_NET_AMT_BWP1() != null) {
            R164Cell5.setCellValue(record1.getR164_NET_AMT_BWP1().doubleValue());
            R164Cell5.setCellStyle(numberStyle);
        } else {
            R164Cell5.setCellValue("");
            R164Cell5.setCellStyle(textStyle);
        }
        Cell R164Cell6 = row.createCell(8);
        if (record1.getR164_NET_AMT_BWP2() != null) {
            R164Cell6.setCellValue(record1.getR164_NET_AMT_BWP2().doubleValue());
            R164Cell6.setCellStyle(numberStyle);
        } else {
            R164Cell6.setCellValue("");
            R164Cell6.setCellStyle(textStyle);
        }
        Cell R164Cell7 = row.createCell(9);
        if (record1.getR164_BAL_SUB_BWP1() != null) {
            R164Cell7.setCellValue(record1.getR164_BAL_SUB_BWP1().doubleValue());
            R164Cell7.setCellStyle(numberStyle);
        } else {
            R164Cell7.setCellValue("");
            R164Cell7.setCellStyle(textStyle);
        }
        Cell R164Cell8 = row.createCell(10);
        if (record1.getR164_BAL_SUB_BWP2() != null) {
            R164Cell8.setCellValue(record1.getR164_BAL_SUB_BWP2().doubleValue());
            R164Cell8.setCellStyle(numberStyle);
        } else {
            R164Cell8.setCellValue("");
            R164Cell8.setCellStyle(textStyle);
        }
        Cell R164Cell9 = row.createCell(11);
        if (record1.getR164_BAL_ACT_SUB_BWP1() != null) {
            R164Cell9.setCellValue(record1.getR164_BAL_ACT_SUB_BWP1().doubleValue());
            R164Cell9.setCellStyle(numberStyle);
        } else {
            R164Cell9.setCellValue("");
            R164Cell9.setCellStyle(textStyle);
        }
        Cell R164Cell10 = row.createCell(12);
        if (record1.getR164_BAL_ACT_SUB_BWP2() != null) {
            R164Cell10.setCellValue(record1.getR164_BAL_ACT_SUB_BWP2().doubleValue());
            R164Cell10.setCellStyle(numberStyle);
        } else {
            R164Cell10.setCellValue("");
            R164Cell10.setCellStyle(textStyle);
        }

        /* ================= R165 ================= */
        row = sheet.getRow(164);
        Cell R165Cell1 = row.createCell(3);
        if (record1.getR165_FIG_BAL_BWP1() != null) {
            R165Cell1.setCellValue(record1.getR165_FIG_BAL_BWP1().doubleValue());
            R165Cell1.setCellStyle(numberStyle);
        } else {
            R165Cell1.setCellValue("");
            R165Cell1.setCellStyle(textStyle);
        }
        Cell R165Cell2 = row.createCell(4);
        if (record1.getR165_FIG_BAL_BWP2() != null) {
            R165Cell2.setCellValue(record1.getR165_FIG_BAL_BWP2().doubleValue());
            R165Cell2.setCellStyle(numberStyle);
        } else {
            R165Cell2.setCellValue("");
            R165Cell2.setCellStyle(textStyle);
        }
        Cell R165Cell3 = row.createCell(5);
        if (record1.getR165_AMT_ADJ_BWP1() != null) {
            R165Cell3.setCellValue(record1.getR165_AMT_ADJ_BWP1().doubleValue());
            R165Cell3.setCellStyle(numberStyle);
        } else {
            R165Cell3.setCellValue("");
            R165Cell3.setCellStyle(textStyle);
        }
        Cell R165Cell4 = row.createCell(6);
        if (record1.getR165_AMT_ADJ_BWP2() != null) {
            R165Cell4.setCellValue(record1.getR165_AMT_ADJ_BWP2().doubleValue());
            R165Cell4.setCellStyle(numberStyle);
        } else {
            R165Cell4.setCellValue("");
            R165Cell4.setCellStyle(textStyle);
        }
        Cell R165Cell5 = row.createCell(7);
        if (record1.getR165_NET_AMT_BWP1() != null) {
            R165Cell5.setCellValue(record1.getR165_NET_AMT_BWP1().doubleValue());
            R165Cell5.setCellStyle(numberStyle);
        } else {
            R165Cell5.setCellValue("");
            R165Cell5.setCellStyle(textStyle);
        }
        Cell R165Cell6 = row.createCell(8);
        if (record1.getR165_NET_AMT_BWP2() != null) {
            R165Cell6.setCellValue(record1.getR165_NET_AMT_BWP2().doubleValue());
            R165Cell6.setCellStyle(numberStyle);
        } else {
            R165Cell6.setCellValue("");
            R165Cell6.setCellStyle(textStyle);
        }
        Cell R165Cell7 = row.createCell(9);
        if (record1.getR165_BAL_SUB_BWP1() != null) {
            R165Cell7.setCellValue(record1.getR165_BAL_SUB_BWP1().doubleValue());
            R165Cell7.setCellStyle(numberStyle);
        } else {
            R165Cell7.setCellValue("");
            R165Cell7.setCellStyle(textStyle);
        }
        Cell R165Cell8 = row.createCell(10);
        if (record1.getR165_BAL_SUB_BWP2() != null) {
            R165Cell8.setCellValue(record1.getR165_BAL_SUB_BWP2().doubleValue());
            R165Cell8.setCellStyle(numberStyle);
        } else {
            R165Cell8.setCellValue("");
            R165Cell8.setCellStyle(textStyle);
        }
        Cell R165Cell9 = row.createCell(11);
        if (record1.getR165_BAL_ACT_SUB_BWP1() != null) {
            R165Cell9.setCellValue(record1.getR165_BAL_ACT_SUB_BWP1().doubleValue());
            R165Cell9.setCellStyle(numberStyle);
        } else {
            R165Cell9.setCellValue("");
            R165Cell9.setCellStyle(textStyle);
        }
        Cell R165Cell10 = row.createCell(12);
        if (record1.getR165_BAL_ACT_SUB_BWP2() != null) {
            R165Cell10.setCellValue(record1.getR165_BAL_ACT_SUB_BWP2().doubleValue());
            R165Cell10.setCellStyle(numberStyle);
        } else {
            R165Cell10.setCellValue("");
            R165Cell10.setCellStyle(textStyle);
        }

        /* ================= R166 ================= */
        row = sheet.getRow(165);
        Cell R166Cell1 = row.createCell(3);
        if (record1.getR166_FIG_BAL_BWP1() != null) {
            R166Cell1.setCellValue(record1.getR166_FIG_BAL_BWP1().doubleValue());
            R166Cell1.setCellStyle(numberStyle);
        } else {
            R166Cell1.setCellValue("");
            R166Cell1.setCellStyle(textStyle);
        }
        Cell R166Cell2 = row.createCell(4);
        if (record1.getR166_FIG_BAL_BWP2() != null) {
            R166Cell2.setCellValue(record1.getR166_FIG_BAL_BWP2().doubleValue());
            R166Cell2.setCellStyle(numberStyle);
        } else {
            R166Cell2.setCellValue("");
            R166Cell2.setCellStyle(textStyle);
        }
        Cell R166Cell3 = row.createCell(5);
        if (record1.getR166_AMT_ADJ_BWP1() != null) {
            R166Cell3.setCellValue(record1.getR166_AMT_ADJ_BWP1().doubleValue());
            R166Cell3.setCellStyle(numberStyle);
        } else {
            R166Cell3.setCellValue("");
            R166Cell3.setCellStyle(textStyle);
        }
        Cell R166Cell4 = row.createCell(6);
        if (record1.getR166_AMT_ADJ_BWP2() != null) {
            R166Cell4.setCellValue(record1.getR166_AMT_ADJ_BWP2().doubleValue());
            R166Cell4.setCellStyle(numberStyle);
        } else {
            R166Cell4.setCellValue("");
            R166Cell4.setCellStyle(textStyle);
        }
        Cell R166Cell5 = row.createCell(7);
        if (record1.getR166_NET_AMT_BWP1() != null) {
            R166Cell5.setCellValue(record1.getR166_NET_AMT_BWP1().doubleValue());
            R166Cell5.setCellStyle(numberStyle);
        } else {
            R166Cell5.setCellValue("");
            R166Cell5.setCellStyle(textStyle);
        }
        Cell R166Cell6 = row.createCell(8);
        if (record1.getR166_NET_AMT_BWP2() != null) {
            R166Cell6.setCellValue(record1.getR166_NET_AMT_BWP2().doubleValue());
            R166Cell6.setCellStyle(numberStyle);
        } else {
            R166Cell6.setCellValue("");
            R166Cell6.setCellStyle(textStyle);
        }
        Cell R166Cell7 = row.createCell(9);
        if (record1.getR166_BAL_SUB_BWP1() != null) {
            R166Cell7.setCellValue(record1.getR166_BAL_SUB_BWP1().doubleValue());
            R166Cell7.setCellStyle(numberStyle);
        } else {
            R166Cell7.setCellValue("");
            R166Cell7.setCellStyle(textStyle);
        }
        Cell R166Cell8 = row.createCell(10);
        if (record1.getR166_BAL_SUB_BWP2() != null) {
            R166Cell8.setCellValue(record1.getR166_BAL_SUB_BWP2().doubleValue());
            R166Cell8.setCellStyle(numberStyle);
        } else {
            R166Cell8.setCellValue("");
            R166Cell8.setCellStyle(textStyle);
        }
        Cell R166Cell9 = row.createCell(11);
        if (record1.getR166_BAL_ACT_SUB_BWP1() != null) {
            R166Cell9.setCellValue(record1.getR166_BAL_ACT_SUB_BWP1().doubleValue());
            R166Cell9.setCellStyle(numberStyle);
        } else {
            R166Cell9.setCellValue("");
            R166Cell9.setCellStyle(textStyle);
        }
        Cell R166Cell10 = row.createCell(12);
        if (record1.getR166_BAL_ACT_SUB_BWP2() != null) {
            R166Cell10.setCellValue(record1.getR166_BAL_ACT_SUB_BWP2().doubleValue());
            R166Cell10.setCellStyle(numberStyle);
        } else {
            R166Cell10.setCellValue("");
            R166Cell10.setCellStyle(textStyle);
        }

        /* ================= R167 ================= */
        row = sheet.getRow(166);
        Cell R167Cell1 = row.createCell(3);
        if (record1.getR167_FIG_BAL_BWP1() != null) {
            R167Cell1.setCellValue(record1.getR167_FIG_BAL_BWP1().doubleValue());
            R167Cell1.setCellStyle(numberStyle);
        } else {
            R167Cell1.setCellValue("");
            R167Cell1.setCellStyle(textStyle);
        }
        Cell R167Cell2 = row.createCell(4);
        if (record1.getR167_FIG_BAL_BWP2() != null) {
            R167Cell2.setCellValue(record1.getR167_FIG_BAL_BWP2().doubleValue());
            R167Cell2.setCellStyle(numberStyle);
        } else {
            R167Cell2.setCellValue("");
            R167Cell2.setCellStyle(textStyle);
        }
        Cell R167Cell3 = row.createCell(5);
        if (record1.getR167_AMT_ADJ_BWP1() != null) {
            R167Cell3.setCellValue(record1.getR167_AMT_ADJ_BWP1().doubleValue());
            R167Cell3.setCellStyle(numberStyle);
        } else {
            R167Cell3.setCellValue("");
            R167Cell3.setCellStyle(textStyle);
        }
        Cell R167Cell4 = row.createCell(6);
        if (record1.getR167_AMT_ADJ_BWP2() != null) {
            R167Cell4.setCellValue(record1.getR167_AMT_ADJ_BWP2().doubleValue());
            R167Cell4.setCellStyle(numberStyle);
        } else {
            R167Cell4.setCellValue("");
            R167Cell4.setCellStyle(textStyle);
        }
        Cell R167Cell5 = row.createCell(7);
        if (record1.getR167_NET_AMT_BWP1() != null) {
            R167Cell5.setCellValue(record1.getR167_NET_AMT_BWP1().doubleValue());
            R167Cell5.setCellStyle(numberStyle);
        } else {
            R167Cell5.setCellValue("");
            R167Cell5.setCellStyle(textStyle);
        }
        Cell R167Cell6 = row.createCell(8);
        if (record1.getR167_NET_AMT_BWP2() != null) {
            R167Cell6.setCellValue(record1.getR167_NET_AMT_BWP2().doubleValue());
            R167Cell6.setCellStyle(numberStyle);
        } else {
            R167Cell6.setCellValue("");
            R167Cell6.setCellStyle(textStyle);
        }
        Cell R167Cell7 = row.createCell(9);
        if (record1.getR167_BAL_SUB_BWP1() != null) {
            R167Cell7.setCellValue(record1.getR167_BAL_SUB_BWP1().doubleValue());
            R167Cell7.setCellStyle(numberStyle);
        } else {
            R167Cell7.setCellValue("");
            R167Cell7.setCellStyle(textStyle);
        }
        Cell R167Cell8 = row.createCell(10);
        if (record1.getR167_BAL_SUB_BWP2() != null) {
            R167Cell8.setCellValue(record1.getR167_BAL_SUB_BWP2().doubleValue());
            R167Cell8.setCellStyle(numberStyle);
        } else {
            R167Cell8.setCellValue("");
            R167Cell8.setCellStyle(textStyle);
        }
        Cell R167Cell9 = row.createCell(11);
        if (record1.getR167_BAL_ACT_SUB_BWP1() != null) {
            R167Cell9.setCellValue(record1.getR167_BAL_ACT_SUB_BWP1().doubleValue());
            R167Cell9.setCellStyle(numberStyle);
        } else {
            R167Cell9.setCellValue("");
            R167Cell9.setCellStyle(textStyle);
        }
        Cell R167Cell10 = row.createCell(12);
        if (record1.getR167_BAL_ACT_SUB_BWP2() != null) {
            R167Cell10.setCellValue(record1.getR167_BAL_ACT_SUB_BWP2().doubleValue());
            R167Cell10.setCellStyle(numberStyle);
        } else {
            R167Cell10.setCellValue("");
            R167Cell10.setCellStyle(textStyle);
        }

        /* ================= R168 ================= */
        row = sheet.getRow(167);
        Cell R168Cell1 = row.createCell(3);
        if (record1.getR168_FIG_BAL_BWP1() != null) {
            R168Cell1.setCellValue(record1.getR168_FIG_BAL_BWP1().doubleValue());
            R168Cell1.setCellStyle(numberStyle);
        } else {
            R168Cell1.setCellValue("");
            R168Cell1.setCellStyle(textStyle);
        }
        Cell R168Cell2 = row.createCell(4);
        if (record1.getR168_FIG_BAL_BWP2() != null) {
            R168Cell2.setCellValue(record1.getR168_FIG_BAL_BWP2().doubleValue());
            R168Cell2.setCellStyle(numberStyle);
        } else {
            R168Cell2.setCellValue("");
            R168Cell2.setCellStyle(textStyle);
        }
        Cell R168Cell3 = row.createCell(5);
        if (record1.getR168_AMT_ADJ_BWP1() != null) {
            R168Cell3.setCellValue(record1.getR168_AMT_ADJ_BWP1().doubleValue());
            R168Cell3.setCellStyle(numberStyle);
        } else {
            R168Cell3.setCellValue("");
            R168Cell3.setCellStyle(textStyle);
        }
        Cell R168Cell4 = row.createCell(6);
        if (record1.getR168_AMT_ADJ_BWP2() != null) {
            R168Cell4.setCellValue(record1.getR168_AMT_ADJ_BWP2().doubleValue());
            R168Cell4.setCellStyle(numberStyle);
        } else {
            R168Cell4.setCellValue("");
            R168Cell4.setCellStyle(textStyle);
        }
        Cell R168Cell5 = row.createCell(7);
        if (record1.getR168_NET_AMT_BWP1() != null) {
            R168Cell5.setCellValue(record1.getR168_NET_AMT_BWP1().doubleValue());
            R168Cell5.setCellStyle(numberStyle);
        } else {
            R168Cell5.setCellValue("");
            R168Cell5.setCellStyle(textStyle);
        }
        Cell R168Cell6 = row.createCell(8);
        if (record1.getR168_NET_AMT_BWP2() != null) {
            R168Cell6.setCellValue(record1.getR168_NET_AMT_BWP2().doubleValue());
            R168Cell6.setCellStyle(numberStyle);
        } else {
            R168Cell6.setCellValue("");
            R168Cell6.setCellStyle(textStyle);
        }
        Cell R168Cell7 = row.createCell(9);
        if (record1.getR168_BAL_SUB_BWP1() != null) {
            R168Cell7.setCellValue(record1.getR168_BAL_SUB_BWP1().doubleValue());
            R168Cell7.setCellStyle(numberStyle);
        } else {
            R168Cell7.setCellValue("");
            R168Cell7.setCellStyle(textStyle);
        }
        Cell R168Cell8 = row.createCell(10);
        if (record1.getR168_BAL_SUB_BWP2() != null) {
            R168Cell8.setCellValue(record1.getR168_BAL_SUB_BWP2().doubleValue());
            R168Cell8.setCellStyle(numberStyle);
        } else {
            R168Cell8.setCellValue("");
            R168Cell8.setCellStyle(textStyle);
        }
        Cell R168Cell9 = row.createCell(11);
        if (record1.getR168_BAL_ACT_SUB_BWP1() != null) {
            R168Cell9.setCellValue(record1.getR168_BAL_ACT_SUB_BWP1().doubleValue());
            R168Cell9.setCellStyle(numberStyle);
        } else {
            R168Cell9.setCellValue("");
            R168Cell9.setCellStyle(textStyle);
        }
        Cell R168Cell10 = row.createCell(12);
        if (record1.getR168_BAL_ACT_SUB_BWP2() != null) {
            R168Cell10.setCellValue(record1.getR168_BAL_ACT_SUB_BWP2().doubleValue());
            R168Cell10.setCellStyle(numberStyle);
        } else {
            R168Cell10.setCellValue("");
            R168Cell10.setCellStyle(textStyle);
        }

        /* ================= R169 ================= */
        row = sheet.getRow(168);
        Cell R169Cell1 = row.createCell(3);
        if (record1.getR169_FIG_BAL_BWP1() != null) {
            R169Cell1.setCellValue(record1.getR169_FIG_BAL_BWP1().doubleValue());
            R169Cell1.setCellStyle(numberStyle);
        } else {
            R169Cell1.setCellValue("");
            R169Cell1.setCellStyle(textStyle);
        }
        Cell R169Cell2 = row.createCell(4);
        if (record1.getR169_FIG_BAL_BWP2() != null) {
            R169Cell2.setCellValue(record1.getR169_FIG_BAL_BWP2().doubleValue());
            R169Cell2.setCellStyle(numberStyle);
        } else {
            R169Cell2.setCellValue("");
            R169Cell2.setCellStyle(textStyle);
        }
        Cell R169Cell3 = row.createCell(5);
        if (record1.getR169_AMT_ADJ_BWP1() != null) {
            R169Cell3.setCellValue(record1.getR169_AMT_ADJ_BWP1().doubleValue());
            R169Cell3.setCellStyle(numberStyle);
        } else {
            R169Cell3.setCellValue("");
            R169Cell3.setCellStyle(textStyle);
        }
        Cell R169Cell4 = row.createCell(6);
        if (record1.getR169_AMT_ADJ_BWP2() != null) {
            R169Cell4.setCellValue(record1.getR169_AMT_ADJ_BWP2().doubleValue());
            R169Cell4.setCellStyle(numberStyle);
        } else {
            R169Cell4.setCellValue("");
            R169Cell4.setCellStyle(textStyle);
        }
        Cell R169Cell5 = row.createCell(7);
        if (record1.getR169_NET_AMT_BWP1() != null) {
            R169Cell5.setCellValue(record1.getR169_NET_AMT_BWP1().doubleValue());
            R169Cell5.setCellStyle(numberStyle);
        } else {
            R169Cell5.setCellValue("");
            R169Cell5.setCellStyle(textStyle);
        }
        Cell R169Cell6 = row.createCell(8);
        if (record1.getR169_NET_AMT_BWP2() != null) {
            R169Cell6.setCellValue(record1.getR169_NET_AMT_BWP2().doubleValue());
            R169Cell6.setCellStyle(numberStyle);
        } else {
            R169Cell6.setCellValue("");
            R169Cell6.setCellStyle(textStyle);
        }
        Cell R169Cell7 = row.createCell(9);
        if (record1.getR169_BAL_SUB_BWP1() != null) {
            R169Cell7.setCellValue(record1.getR169_BAL_SUB_BWP1().doubleValue());
            R169Cell7.setCellStyle(numberStyle);
        } else {
            R169Cell7.setCellValue("");
            R169Cell7.setCellStyle(textStyle);
        }
        Cell R169Cell8 = row.createCell(10);
        if (record1.getR169_BAL_SUB_BWP2() != null) {
            R169Cell8.setCellValue(record1.getR169_BAL_SUB_BWP2().doubleValue());
            R169Cell8.setCellStyle(numberStyle);
        } else {
            R169Cell8.setCellValue("");
            R169Cell8.setCellStyle(textStyle);
        }
        Cell R169Cell9 = row.createCell(11);
        if (record1.getR169_BAL_ACT_SUB_BWP1() != null) {
            R169Cell9.setCellValue(record1.getR169_BAL_ACT_SUB_BWP1().doubleValue());
            R169Cell9.setCellStyle(numberStyle);
        } else {
            R169Cell9.setCellValue("");
            R169Cell9.setCellStyle(textStyle);
        }
        Cell R169Cell10 = row.createCell(12);
        if (record1.getR169_BAL_ACT_SUB_BWP2() != null) {
            R169Cell10.setCellValue(record1.getR169_BAL_ACT_SUB_BWP2().doubleValue());
            R169Cell10.setCellStyle(numberStyle);
        } else {
            R169Cell10.setCellValue("");
            R169Cell10.setCellStyle(textStyle);
        }

        /* ================= R170 ================= */
        row = sheet.getRow(169);
        Cell R170Cell1 = row.createCell(3);
        if (record1.getR170_FIG_BAL_BWP1() != null) {
            R170Cell1.setCellValue(record1.getR170_FIG_BAL_BWP1().doubleValue());
            R170Cell1.setCellStyle(numberStyle);
        } else {
            R170Cell1.setCellValue("");
            R170Cell1.setCellStyle(textStyle);
        }
        Cell R170Cell2 = row.createCell(4);
        if (record1.getR170_FIG_BAL_BWP2() != null) {
            R170Cell2.setCellValue(record1.getR170_FIG_BAL_BWP2().doubleValue());
            R170Cell2.setCellStyle(numberStyle);
        } else {
            R170Cell2.setCellValue("");
            R170Cell2.setCellStyle(textStyle);
        }
        Cell R170Cell3 = row.createCell(5);
        if (record1.getR170_AMT_ADJ_BWP1() != null) {
            R170Cell3.setCellValue(record1.getR170_AMT_ADJ_BWP1().doubleValue());
            R170Cell3.setCellStyle(numberStyle);
        } else {
            R170Cell3.setCellValue("");
            R170Cell3.setCellStyle(textStyle);
        }
        Cell R170Cell4 = row.createCell(6);
        if (record1.getR170_AMT_ADJ_BWP2() != null) {
            R170Cell4.setCellValue(record1.getR170_AMT_ADJ_BWP2().doubleValue());
            R170Cell4.setCellStyle(numberStyle);
        } else {
            R170Cell4.setCellValue("");
            R170Cell4.setCellStyle(textStyle);
        }
        Cell R170Cell5 = row.createCell(7);
        if (record1.getR170_NET_AMT_BWP1() != null) {
            R170Cell5.setCellValue(record1.getR170_NET_AMT_BWP1().doubleValue());
            R170Cell5.setCellStyle(numberStyle);
        } else {
            R170Cell5.setCellValue("");
            R170Cell5.setCellStyle(textStyle);
        }
        Cell R170Cell6 = row.createCell(8);
        if (record1.getR170_NET_AMT_BWP2() != null) {
            R170Cell6.setCellValue(record1.getR170_NET_AMT_BWP2().doubleValue());
            R170Cell6.setCellStyle(numberStyle);
        } else {
            R170Cell6.setCellValue("");
            R170Cell6.setCellStyle(textStyle);
        }
        Cell R170Cell7 = row.createCell(9);
        if (record1.getR170_BAL_SUB_BWP1() != null) {
            R170Cell7.setCellValue(record1.getR170_BAL_SUB_BWP1().doubleValue());
            R170Cell7.setCellStyle(numberStyle);
        } else {
            R170Cell7.setCellValue("");
            R170Cell7.setCellStyle(textStyle);
        }
        Cell R170Cell8 = row.createCell(10);
        if (record1.getR170_BAL_SUB_BWP2() != null) {
            R170Cell8.setCellValue(record1.getR170_BAL_SUB_BWP2().doubleValue());
            R170Cell8.setCellStyle(numberStyle);
        } else {
            R170Cell8.setCellValue("");
            R170Cell8.setCellStyle(textStyle);
        }
        Cell R170Cell9 = row.createCell(11);
        if (record1.getR170_BAL_ACT_SUB_BWP1() != null) {
            R170Cell9.setCellValue(record1.getR170_BAL_ACT_SUB_BWP1().doubleValue());
            R170Cell9.setCellStyle(numberStyle);
        } else {
            R170Cell9.setCellValue("");
            R170Cell9.setCellStyle(textStyle);
        }
        Cell R170Cell10 = row.createCell(12);
        if (record1.getR170_BAL_ACT_SUB_BWP2() != null) {
            R170Cell10.setCellValue(record1.getR170_BAL_ACT_SUB_BWP2().doubleValue());
            R170Cell10.setCellStyle(numberStyle);
        } else {
            R170Cell10.setCellValue("");
            R170Cell10.setCellStyle(textStyle);
        }

        /* ================= R171 ================= */
        row = sheet.getRow(170);
        Cell R171Cell1 = row.createCell(3);
        if (record1.getR171_FIG_BAL_BWP1() != null) {
            R171Cell1.setCellValue(record1.getR171_FIG_BAL_BWP1().doubleValue());
            R171Cell1.setCellStyle(numberStyle);
        } else {
            R171Cell1.setCellValue("");
            R171Cell1.setCellStyle(textStyle);
        }
        Cell R171Cell2 = row.createCell(4);
        if (record1.getR171_FIG_BAL_BWP2() != null) {
            R171Cell2.setCellValue(record1.getR171_FIG_BAL_BWP2().doubleValue());
            R171Cell2.setCellStyle(numberStyle);
        } else {
            R171Cell2.setCellValue("");
            R171Cell2.setCellStyle(textStyle);
        }
        Cell R171Cell3 = row.createCell(5);
        if (record1.getR171_AMT_ADJ_BWP1() != null) {
            R171Cell3.setCellValue(record1.getR171_AMT_ADJ_BWP1().doubleValue());
            R171Cell3.setCellStyle(numberStyle);
        } else {
            R171Cell3.setCellValue("");
            R171Cell3.setCellStyle(textStyle);
        }
        Cell R171Cell4 = row.createCell(6);
        if (record1.getR171_AMT_ADJ_BWP2() != null) {
            R171Cell4.setCellValue(record1.getR171_AMT_ADJ_BWP2().doubleValue());
            R171Cell4.setCellStyle(numberStyle);
        } else {
            R171Cell4.setCellValue("");
            R171Cell4.setCellStyle(textStyle);
        }
        Cell R171Cell5 = row.createCell(7);
        if (record1.getR171_NET_AMT_BWP1() != null) {
            R171Cell5.setCellValue(record1.getR171_NET_AMT_BWP1().doubleValue());
            R171Cell5.setCellStyle(numberStyle);
        } else {
            R171Cell5.setCellValue("");
            R171Cell5.setCellStyle(textStyle);
        }
        Cell R171Cell6 = row.createCell(8);
        if (record1.getR171_NET_AMT_BWP2() != null) {
            R171Cell6.setCellValue(record1.getR171_NET_AMT_BWP2().doubleValue());
            R171Cell6.setCellStyle(numberStyle);
        } else {
            R171Cell6.setCellValue("");
            R171Cell6.setCellStyle(textStyle);
        }
        Cell R171Cell7 = row.createCell(9);
        if (record1.getR171_BAL_SUB_BWP1() != null) {
            R171Cell7.setCellValue(record1.getR171_BAL_SUB_BWP1().doubleValue());
            R171Cell7.setCellStyle(numberStyle);
        } else {
            R171Cell7.setCellValue("");
            R171Cell7.setCellStyle(textStyle);
        }
        Cell R171Cell8 = row.createCell(10);
        if (record1.getR171_BAL_SUB_BWP2() != null) {
            R171Cell8.setCellValue(record1.getR171_BAL_SUB_BWP2().doubleValue());
            R171Cell8.setCellStyle(numberStyle);
        } else {
            R171Cell8.setCellValue("");
            R171Cell8.setCellStyle(textStyle);
        }
        Cell R171Cell9 = row.createCell(11);
        if (record1.getR171_BAL_ACT_SUB_BWP1() != null) {
            R171Cell9.setCellValue(record1.getR171_BAL_ACT_SUB_BWP1().doubleValue());
            R171Cell9.setCellStyle(numberStyle);
        } else {
            R171Cell9.setCellValue("");
            R171Cell9.setCellStyle(textStyle);
        }
        Cell R171Cell10 = row.createCell(12);
        if (record1.getR171_BAL_ACT_SUB_BWP2() != null) {
            R171Cell10.setCellValue(record1.getR171_BAL_ACT_SUB_BWP2().doubleValue());
            R171Cell10.setCellStyle(numberStyle);
        } else {
            R171Cell10.setCellValue("");
            R171Cell10.setCellStyle(textStyle);
        }

        /* ================= R172 ================= */
        row = sheet.getRow(171);
        Cell R172Cell1 = row.createCell(3);
        if (record1.getR172_FIG_BAL_BWP1() != null) {
            R172Cell1.setCellValue(record1.getR172_FIG_BAL_BWP1().doubleValue());
            R172Cell1.setCellStyle(numberStyle);
        } else {
            R172Cell1.setCellValue("");
            R172Cell1.setCellStyle(textStyle);
        }
        Cell R172Cell2 = row.createCell(4);
        if (record1.getR172_FIG_BAL_BWP2() != null) {
            R172Cell2.setCellValue(record1.getR172_FIG_BAL_BWP2().doubleValue());
            R172Cell2.setCellStyle(numberStyle);
        } else {
            R172Cell2.setCellValue("");
            R172Cell2.setCellStyle(textStyle);
        }
        Cell R172Cell3 = row.createCell(5);
        if (record1.getR172_AMT_ADJ_BWP1() != null) {
            R172Cell3.setCellValue(record1.getR172_AMT_ADJ_BWP1().doubleValue());
            R172Cell3.setCellStyle(numberStyle);
        } else {
            R172Cell3.setCellValue("");
            R172Cell3.setCellStyle(textStyle);
        }
        Cell R172Cell4 = row.createCell(6);
        if (record1.getR172_AMT_ADJ_BWP2() != null) {
            R172Cell4.setCellValue(record1.getR172_AMT_ADJ_BWP2().doubleValue());
            R172Cell4.setCellStyle(numberStyle);
        } else {
            R172Cell4.setCellValue("");
            R172Cell4.setCellStyle(textStyle);
        }
        Cell R172Cell5 = row.createCell(7);
        if (record1.getR172_NET_AMT_BWP1() != null) {
            R172Cell5.setCellValue(record1.getR172_NET_AMT_BWP1().doubleValue());
            R172Cell5.setCellStyle(numberStyle);
        } else {
            R172Cell5.setCellValue("");
            R172Cell5.setCellStyle(textStyle);
        }
        Cell R172Cell6 = row.createCell(8);
        if (record1.getR172_NET_AMT_BWP2() != null) {
            R172Cell6.setCellValue(record1.getR172_NET_AMT_BWP2().doubleValue());
            R172Cell6.setCellStyle(numberStyle);
        } else {
            R172Cell6.setCellValue("");
            R172Cell6.setCellStyle(textStyle);
        }
        Cell R172Cell7 = row.createCell(9);
        if (record1.getR172_BAL_SUB_BWP1() != null) {
            R172Cell7.setCellValue(record1.getR172_BAL_SUB_BWP1().doubleValue());
            R172Cell7.setCellStyle(numberStyle);
        } else {
            R172Cell7.setCellValue("");
            R172Cell7.setCellStyle(textStyle);
        }
        Cell R172Cell8 = row.createCell(10);
        if (record1.getR172_BAL_SUB_BWP2() != null) {
            R172Cell8.setCellValue(record1.getR172_BAL_SUB_BWP2().doubleValue());
            R172Cell8.setCellStyle(numberStyle);
        } else {
            R172Cell8.setCellValue("");
            R172Cell8.setCellStyle(textStyle);
        }
        Cell R172Cell9 = row.createCell(11);
        if (record1.getR172_BAL_ACT_SUB_BWP1() != null) {
            R172Cell9.setCellValue(record1.getR172_BAL_ACT_SUB_BWP1().doubleValue());
            R172Cell9.setCellStyle(numberStyle);
        } else {
            R172Cell9.setCellValue("");
            R172Cell9.setCellStyle(textStyle);
        }
        Cell R172Cell10 = row.createCell(12);
        if (record1.getR172_BAL_ACT_SUB_BWP2() != null) {
            R172Cell10.setCellValue(record1.getR172_BAL_ACT_SUB_BWP2().doubleValue());
            R172Cell10.setCellStyle(numberStyle);
        } else {
            R172Cell10.setCellValue("");
            R172Cell10.setCellStyle(textStyle);
        }

        /* ================= R173 ================= */
        row = sheet.getRow(172);
        Cell R173Cell1 = row.createCell(3);
        if (record1.getR173_FIG_BAL_BWP1() != null) {
            R173Cell1.setCellValue(record1.getR173_FIG_BAL_BWP1().doubleValue());
            R173Cell1.setCellStyle(numberStyle);
        } else {
            R173Cell1.setCellValue("");
            R173Cell1.setCellStyle(textStyle);
        }
        Cell R173Cell2 = row.createCell(4);
        if (record1.getR173_FIG_BAL_BWP2() != null) {
            R173Cell2.setCellValue(record1.getR173_FIG_BAL_BWP2().doubleValue());
            R173Cell2.setCellStyle(numberStyle);
        } else {
            R173Cell2.setCellValue("");
            R173Cell2.setCellStyle(textStyle);
        }
        Cell R173Cell3 = row.createCell(5);
        if (record1.getR173_AMT_ADJ_BWP1() != null) {
            R173Cell3.setCellValue(record1.getR173_AMT_ADJ_BWP1().doubleValue());
            R173Cell3.setCellStyle(numberStyle);
        } else {
            R173Cell3.setCellValue("");
            R173Cell3.setCellStyle(textStyle);
        }
        Cell R173Cell4 = row.createCell(6);
        if (record1.getR173_AMT_ADJ_BWP2() != null) {
            R173Cell4.setCellValue(record1.getR173_AMT_ADJ_BWP2().doubleValue());
            R173Cell4.setCellStyle(numberStyle);
        } else {
            R173Cell4.setCellValue("");
            R173Cell4.setCellStyle(textStyle);
        }
        Cell R173Cell5 = row.createCell(7);
        if (record1.getR173_NET_AMT_BWP1() != null) {
            R173Cell5.setCellValue(record1.getR173_NET_AMT_BWP1().doubleValue());
            R173Cell5.setCellStyle(numberStyle);
        } else {
            R173Cell5.setCellValue("");
            R173Cell5.setCellStyle(textStyle);
        }
        Cell R173Cell6 = row.createCell(8);
        if (record1.getR173_NET_AMT_BWP2() != null) {
            R173Cell6.setCellValue(record1.getR173_NET_AMT_BWP2().doubleValue());
            R173Cell6.setCellStyle(numberStyle);
        } else {
            R173Cell6.setCellValue("");
            R173Cell6.setCellStyle(textStyle);
        }
        Cell R173Cell7 = row.createCell(9);
        if (record1.getR173_BAL_SUB_BWP1() != null) {
            R173Cell7.setCellValue(record1.getR173_BAL_SUB_BWP1().doubleValue());
            R173Cell7.setCellStyle(numberStyle);
        } else {
            R173Cell7.setCellValue("");
            R173Cell7.setCellStyle(textStyle);
        }
        Cell R173Cell8 = row.createCell(10);
        if (record1.getR173_BAL_SUB_BWP2() != null) {
            R173Cell8.setCellValue(record1.getR173_BAL_SUB_BWP2().doubleValue());
            R173Cell8.setCellStyle(numberStyle);
        } else {
            R173Cell8.setCellValue("");
            R173Cell8.setCellStyle(textStyle);
        }
        Cell R173Cell9 = row.createCell(11);
        if (record1.getR173_BAL_ACT_SUB_BWP1() != null) {
            R173Cell9.setCellValue(record1.getR173_BAL_ACT_SUB_BWP1().doubleValue());
            R173Cell9.setCellStyle(numberStyle);
        } else {
            R173Cell9.setCellValue("");
            R173Cell9.setCellStyle(textStyle);
        }
        Cell R173Cell10 = row.createCell(12);
        if (record1.getR173_BAL_ACT_SUB_BWP2() != null) {
            R173Cell10.setCellValue(record1.getR173_BAL_ACT_SUB_BWP2().doubleValue());
            R173Cell10.setCellStyle(numberStyle);
        } else {
            R173Cell10.setCellValue("");
            R173Cell10.setCellStyle(textStyle);
        }

        /* ================= R174 ================= */
        row = sheet.getRow(173);
        Cell R174Cell1 = row.createCell(3);
        if (record1.getR174_FIG_BAL_BWP1() != null) {
            R174Cell1.setCellValue(record1.getR174_FIG_BAL_BWP1().doubleValue());
            R174Cell1.setCellStyle(numberStyle);
        } else {
            R174Cell1.setCellValue("");
            R174Cell1.setCellStyle(textStyle);
        }
        Cell R174Cell2 = row.createCell(4);
        if (record1.getR174_FIG_BAL_BWP2() != null) {
            R174Cell2.setCellValue(record1.getR174_FIG_BAL_BWP2().doubleValue());
            R174Cell2.setCellStyle(numberStyle);
        } else {
            R174Cell2.setCellValue("");
            R174Cell2.setCellStyle(textStyle);
        }
        Cell R174Cell3 = row.createCell(5);
        if (record1.getR174_AMT_ADJ_BWP1() != null) {
            R174Cell3.setCellValue(record1.getR174_AMT_ADJ_BWP1().doubleValue());
            R174Cell3.setCellStyle(numberStyle);
        } else {
            R174Cell3.setCellValue("");
            R174Cell3.setCellStyle(textStyle);
        }
        Cell R174Cell4 = row.createCell(6);
        if (record1.getR174_AMT_ADJ_BWP2() != null) {
            R174Cell4.setCellValue(record1.getR174_AMT_ADJ_BWP2().doubleValue());
            R174Cell4.setCellStyle(numberStyle);
        } else {
            R174Cell4.setCellValue("");
            R174Cell4.setCellStyle(textStyle);
        }
        Cell R174Cell5 = row.createCell(7);
        if (record1.getR174_NET_AMT_BWP1() != null) {
            R174Cell5.setCellValue(record1.getR174_NET_AMT_BWP1().doubleValue());
            R174Cell5.setCellStyle(numberStyle);
        } else {
            R174Cell5.setCellValue("");
            R174Cell5.setCellStyle(textStyle);
        }
        Cell R174Cell6 = row.createCell(8);
        if (record1.getR174_NET_AMT_BWP2() != null) {
            R174Cell6.setCellValue(record1.getR174_NET_AMT_BWP2().doubleValue());
            R174Cell6.setCellStyle(numberStyle);
        } else {
            R174Cell6.setCellValue("");
            R174Cell6.setCellStyle(textStyle);
        }
        Cell R174Cell7 = row.createCell(9);
        if (record1.getR174_BAL_SUB_BWP1() != null) {
            R174Cell7.setCellValue(record1.getR174_BAL_SUB_BWP1().doubleValue());
            R174Cell7.setCellStyle(numberStyle);
        } else {
            R174Cell7.setCellValue("");
            R174Cell7.setCellStyle(textStyle);
        }
        Cell R174Cell8 = row.createCell(10);
        if (record1.getR174_BAL_SUB_BWP2() != null) {
            R174Cell8.setCellValue(record1.getR174_BAL_SUB_BWP2().doubleValue());
            R174Cell8.setCellStyle(numberStyle);
        } else {
            R174Cell8.setCellValue("");
            R174Cell8.setCellStyle(textStyle);
        }
        Cell R174Cell9 = row.createCell(11);
        if (record1.getR174_BAL_ACT_SUB_BWP1() != null) {
            R174Cell9.setCellValue(record1.getR174_BAL_ACT_SUB_BWP1().doubleValue());
            R174Cell9.setCellStyle(numberStyle);
        } else {
            R174Cell9.setCellValue("");
            R174Cell9.setCellStyle(textStyle);
        }
        Cell R174Cell10 = row.createCell(12);
        if (record1.getR174_BAL_ACT_SUB_BWP2() != null) {
            R174Cell10.setCellValue(record1.getR174_BAL_ACT_SUB_BWP2().doubleValue());
            R174Cell10.setCellStyle(numberStyle);
        } else {
            R174Cell10.setCellValue("");
            R174Cell10.setCellStyle(textStyle);
        }

        /* ================= R180 ================= */
        row = sheet.getRow(179);
        Cell R180Cell1 = row.createCell(3);
        if (record1.getR180_FIG_BAL_BWP1() != null) {
            R180Cell1.setCellValue(record1.getR180_FIG_BAL_BWP1().doubleValue());
            R180Cell1.setCellStyle(numberStyle);
        } else {
            R180Cell1.setCellValue("");
            R180Cell1.setCellStyle(textStyle);
        }

        Cell R180Cell2 = row.createCell(4);
        if (record1.getR180_FIG_BAL_BWP2() != null) {
            R180Cell2.setCellValue(record1.getR180_FIG_BAL_BWP2().doubleValue());
            R180Cell2.setCellStyle(numberStyle);
        } else {
            R180Cell2.setCellValue("");
            R180Cell2.setCellStyle(textStyle);
        }

        Cell R180Cell3 = row.createCell(5);
        if (record1.getR180_AMT_ADJ_BWP1() != null) {
            R180Cell3.setCellValue(record1.getR180_AMT_ADJ_BWP1().doubleValue());
            R180Cell3.setCellStyle(numberStyle);
        } else {
            R180Cell3.setCellValue("");
            R180Cell3.setCellStyle(textStyle);
        }

        Cell R180Cell4 = row.createCell(6);
        if (record1.getR180_AMT_ADJ_BWP2() != null) {
            R180Cell4.setCellValue(record1.getR180_AMT_ADJ_BWP2().doubleValue());
            R180Cell4.setCellStyle(numberStyle);
        } else {
            R180Cell4.setCellValue("");
            R180Cell4.setCellStyle(textStyle);
        }

        Cell R180Cell5 = row.createCell(7);
        if (record1.getR180_NET_AMT_BWP1() != null) {
            R180Cell5.setCellValue(record1.getR180_NET_AMT_BWP1().doubleValue());
            R180Cell5.setCellStyle(numberStyle);
        } else {
            R180Cell5.setCellValue("");
            R180Cell5.setCellStyle(textStyle);
        }

        Cell R180Cell6 = row.createCell(8);
        if (record1.getR180_NET_AMT_BWP2() != null) {
            R180Cell6.setCellValue(record1.getR180_NET_AMT_BWP2().doubleValue());
            R180Cell6.setCellStyle(numberStyle);
        } else {
            R180Cell6.setCellValue("");
            R180Cell6.setCellStyle(textStyle);
        }

        Cell R180Cell7 = row.createCell(9);
        if (record1.getR180_BAL_SUB_BWP1() != null) {
            R180Cell7.setCellValue(record1.getR180_BAL_SUB_BWP1().doubleValue());
            R180Cell7.setCellStyle(numberStyle);
        } else {
            R180Cell7.setCellValue("");
            R180Cell7.setCellStyle(textStyle);
        }

        Cell R180Cell8 = row.createCell(10);
        if (record1.getR180_BAL_SUB_BWP2() != null) {
            R180Cell8.setCellValue(record1.getR180_BAL_SUB_BWP2().doubleValue());
            R180Cell8.setCellStyle(numberStyle);
        } else {
            R180Cell8.setCellValue("");
            R180Cell8.setCellStyle(textStyle);
        }

        Cell R180Cell9 = row.createCell(11);
        if (record1.getR180_BAL_ACT_SUB_BWP1() != null) {
            R180Cell9.setCellValue(record1.getR180_BAL_ACT_SUB_BWP1().doubleValue());
            R180Cell9.setCellStyle(numberStyle);
        } else {
            R180Cell9.setCellValue("");
            R180Cell9.setCellStyle(textStyle);
        }

        Cell R180Cell10 = row.createCell(12);
        if (record1.getR180_BAL_ACT_SUB_BWP2() != null) {
            R180Cell10.setCellValue(record1.getR180_BAL_ACT_SUB_BWP2().doubleValue());
            R180Cell10.setCellStyle(numberStyle);
        } else {
            R180Cell10.setCellValue("");
            R180Cell10.setCellStyle(textStyle);
        }

        /* ================= R181 ================= */
        row = sheet.getRow(180);
        Cell R181Cell1 = row.createCell(3);
        if (record1.getR181_FIG_BAL_BWP1() != null) {
            R181Cell1.setCellValue(record1.getR181_FIG_BAL_BWP1().doubleValue());
            R181Cell1.setCellStyle(numberStyle);
        } else {
            R181Cell1.setCellValue("");
            R181Cell1.setCellStyle(textStyle);
        }
        Cell R181Cell2 = row.createCell(4);
        if (record1.getR181_FIG_BAL_BWP2() != null) {
            R181Cell2.setCellValue(record1.getR181_FIG_BAL_BWP2().doubleValue());
            R181Cell2.setCellStyle(numberStyle);
        } else {
            R181Cell2.setCellValue("");
            R181Cell2.setCellStyle(textStyle);
        }
        Cell R181Cell3 = row.createCell(5);
        if (record1.getR181_AMT_ADJ_BWP1() != null) {
            R181Cell3.setCellValue(record1.getR181_AMT_ADJ_BWP1().doubleValue());
            R181Cell3.setCellStyle(numberStyle);
        } else {
            R181Cell3.setCellValue("");
            R181Cell3.setCellStyle(textStyle);
        }
        Cell R181Cell4 = row.createCell(6);
        if (record1.getR181_AMT_ADJ_BWP2() != null) {
            R181Cell4.setCellValue(record1.getR181_AMT_ADJ_BWP2().doubleValue());
            R181Cell4.setCellStyle(numberStyle);
        } else {
            R181Cell4.setCellValue("");
            R181Cell4.setCellStyle(textStyle);
        }
        Cell R181Cell5 = row.createCell(7);
        if (record1.getR181_NET_AMT_BWP1() != null) {
            R181Cell5.setCellValue(record1.getR181_NET_AMT_BWP1().doubleValue());
            R181Cell5.setCellStyle(numberStyle);
        } else {
            R181Cell5.setCellValue("");
            R181Cell5.setCellStyle(textStyle);
        }
        Cell R181Cell6 = row.createCell(8);
        if (record1.getR181_NET_AMT_BWP2() != null) {
            R181Cell6.setCellValue(record1.getR181_NET_AMT_BWP2().doubleValue());
            R181Cell6.setCellStyle(numberStyle);
        } else {
            R181Cell6.setCellValue("");
            R181Cell6.setCellStyle(textStyle);
        }
        Cell R181Cell7 = row.createCell(9);
        if (record1.getR181_BAL_SUB_BWP1() != null) {
            R181Cell7.setCellValue(record1.getR181_BAL_SUB_BWP1().doubleValue());
            R181Cell7.setCellStyle(numberStyle);
        } else {
            R181Cell7.setCellValue("");
            R181Cell7.setCellStyle(textStyle);
        }
        Cell R181Cell8 = row.createCell(10);
        if (record1.getR181_BAL_SUB_BWP2() != null) {
            R181Cell8.setCellValue(record1.getR181_BAL_SUB_BWP2().doubleValue());
            R181Cell8.setCellStyle(numberStyle);
        } else {
            R181Cell8.setCellValue("");
            R181Cell8.setCellStyle(textStyle);
        }
        Cell R181Cell9 = row.createCell(11);
        if (record1.getR181_BAL_ACT_SUB_BWP1() != null) {
            R181Cell9.setCellValue(record1.getR181_BAL_ACT_SUB_BWP1().doubleValue());
            R181Cell9.setCellStyle(numberStyle);
        } else {
            R181Cell9.setCellValue("");
            R181Cell9.setCellStyle(textStyle);
        }
        Cell R181Cell10 = row.createCell(12);
        if (record1.getR181_BAL_ACT_SUB_BWP2() != null) {
            R181Cell10.setCellValue(record1.getR181_BAL_ACT_SUB_BWP2().doubleValue());
            R181Cell10.setCellStyle(numberStyle);
        } else {
            R181Cell10.setCellValue("");
            R181Cell10.setCellStyle(textStyle);
        }

        /* ================= R182 ================= */
        row = sheet.getRow(181);
        Cell R182Cell1 = row.createCell(3);
        if (record1.getR182_FIG_BAL_BWP1() != null) {
            R182Cell1.setCellValue(record1.getR182_FIG_BAL_BWP1().doubleValue());
            R182Cell1.setCellStyle(numberStyle);
        } else {
            R182Cell1.setCellValue("");
            R182Cell1.setCellStyle(textStyle);
        }
        Cell R182Cell2 = row.createCell(4);
        if (record1.getR182_FIG_BAL_BWP2() != null) {
            R182Cell2.setCellValue(record1.getR182_FIG_BAL_BWP2().doubleValue());
            R182Cell2.setCellStyle(numberStyle);
        } else {
            R182Cell2.setCellValue("");
            R182Cell2.setCellStyle(textStyle);
        }
        Cell R182Cell3 = row.createCell(5);
        if (record1.getR182_AMT_ADJ_BWP1() != null) {
            R182Cell3.setCellValue(record1.getR182_AMT_ADJ_BWP1().doubleValue());
            R182Cell3.setCellStyle(numberStyle);
        } else {
            R182Cell3.setCellValue("");
            R182Cell3.setCellStyle(textStyle);
        }
        Cell R182Cell4 = row.createCell(6);
        if (record1.getR182_AMT_ADJ_BWP2() != null) {
            R182Cell4.setCellValue(record1.getR182_AMT_ADJ_BWP2().doubleValue());
            R182Cell4.setCellStyle(numberStyle);
        } else {
            R182Cell4.setCellValue("");
            R182Cell4.setCellStyle(textStyle);
        }
        Cell R182Cell5 = row.createCell(7);
        if (record1.getR182_NET_AMT_BWP1() != null) {
            R182Cell5.setCellValue(record1.getR182_NET_AMT_BWP1().doubleValue());
            R182Cell5.setCellStyle(numberStyle);
        } else {
            R182Cell5.setCellValue("");
            R182Cell5.setCellStyle(textStyle);
        }
        Cell R182Cell6 = row.createCell(8);
        if (record1.getR182_NET_AMT_BWP2() != null) {
            R182Cell6.setCellValue(record1.getR182_NET_AMT_BWP2().doubleValue());
            R182Cell6.setCellStyle(numberStyle);
        } else {
            R182Cell6.setCellValue("");
            R182Cell6.setCellStyle(textStyle);
        }
        Cell R182Cell7 = row.createCell(9);
        if (record1.getR182_BAL_SUB_BWP1() != null) {
            R182Cell7.setCellValue(record1.getR182_BAL_SUB_BWP1().doubleValue());
            R182Cell7.setCellStyle(numberStyle);
        } else {
            R182Cell7.setCellValue("");
            R182Cell7.setCellStyle(textStyle);
        }
        Cell R182Cell8 = row.createCell(10);
        if (record1.getR182_BAL_SUB_BWP2() != null) {
            R182Cell8.setCellValue(record1.getR182_BAL_SUB_BWP2().doubleValue());
            R182Cell8.setCellStyle(numberStyle);
        } else {
            R182Cell8.setCellValue("");
            R182Cell8.setCellStyle(textStyle);
        }
        Cell R182Cell9 = row.createCell(11);
        if (record1.getR182_BAL_ACT_SUB_BWP1() != null) {
            R182Cell9.setCellValue(record1.getR182_BAL_ACT_SUB_BWP1().doubleValue());
            R182Cell9.setCellStyle(numberStyle);
        } else {
            R182Cell9.setCellValue("");
            R182Cell9.setCellStyle(textStyle);
        }
        Cell R182Cell10 = row.createCell(12);
        if (record1.getR182_BAL_ACT_SUB_BWP2() != null) {
            R182Cell10.setCellValue(record1.getR182_BAL_ACT_SUB_BWP2().doubleValue());
            R182Cell10.setCellStyle(numberStyle);
        } else {
            R182Cell10.setCellValue("");
            R182Cell10.setCellStyle(textStyle);
        }

        /* ================= R183 ================= */
        row = sheet.getRow(182);
        Cell R183Cell1 = row.createCell(3);
        if (record1.getR183_FIG_BAL_BWP1() != null) {
            R183Cell1.setCellValue(record1.getR183_FIG_BAL_BWP1().doubleValue());
            R183Cell1.setCellStyle(numberStyle);
        } else {
            R183Cell1.setCellValue("");
            R183Cell1.setCellStyle(textStyle);
        }
        Cell R183Cell2 = row.createCell(4);
        if (record1.getR183_FIG_BAL_BWP2() != null) {
            R183Cell2.setCellValue(record1.getR183_FIG_BAL_BWP2().doubleValue());
            R183Cell2.setCellStyle(numberStyle);
        } else {
            R183Cell2.setCellValue("");
            R183Cell2.setCellStyle(textStyle);
        }
        Cell R183Cell3 = row.createCell(5);
        if (record1.getR183_AMT_ADJ_BWP1() != null) {
            R183Cell3.setCellValue(record1.getR183_AMT_ADJ_BWP1().doubleValue());
            R183Cell3.setCellStyle(numberStyle);
        } else {
            R183Cell3.setCellValue("");
            R183Cell3.setCellStyle(textStyle);
        }
        Cell R183Cell4 = row.createCell(6);
        if (record1.getR183_AMT_ADJ_BWP2() != null) {
            R183Cell4.setCellValue(record1.getR183_AMT_ADJ_BWP2().doubleValue());
            R183Cell4.setCellStyle(numberStyle);
        } else {
            R183Cell4.setCellValue("");
            R183Cell4.setCellStyle(textStyle);
        }
        Cell R183Cell5 = row.createCell(7);
        if (record1.getR183_NET_AMT_BWP1() != null) {
            R183Cell5.setCellValue(record1.getR183_NET_AMT_BWP1().doubleValue());
            R183Cell5.setCellStyle(numberStyle);
        } else {
            R183Cell5.setCellValue("");
            R183Cell5.setCellStyle(textStyle);
        }
        Cell R183Cell6 = row.createCell(8);
        if (record1.getR183_NET_AMT_BWP2() != null) {
            R183Cell6.setCellValue(record1.getR183_NET_AMT_BWP2().doubleValue());
            R183Cell6.setCellStyle(numberStyle);
        } else {
            R183Cell6.setCellValue("");
            R183Cell6.setCellStyle(textStyle);
        }
        Cell R183Cell7 = row.createCell(9);
        if (record1.getR183_BAL_SUB_BWP1() != null) {
            R183Cell7.setCellValue(record1.getR183_BAL_SUB_BWP1().doubleValue());
            R183Cell7.setCellStyle(numberStyle);
        } else {
            R183Cell7.setCellValue("");
            R183Cell7.setCellStyle(textStyle);
        }
        Cell R183Cell8 = row.createCell(10);
        if (record1.getR183_BAL_SUB_BWP2() != null) {
            R183Cell8.setCellValue(record1.getR183_BAL_SUB_BWP2().doubleValue());
            R183Cell8.setCellStyle(numberStyle);
        } else {
            R183Cell8.setCellValue("");
            R183Cell8.setCellStyle(textStyle);
        }
        Cell R183Cell9 = row.createCell(11);
        if (record1.getR183_BAL_ACT_SUB_BWP1() != null) {
            R183Cell9.setCellValue(record1.getR183_BAL_ACT_SUB_BWP1().doubleValue());
            R183Cell9.setCellStyle(numberStyle);
        } else {
            R183Cell9.setCellValue("");
            R183Cell9.setCellStyle(textStyle);
        }
        Cell R183Cell10 = row.createCell(12);
        if (record1.getR183_BAL_ACT_SUB_BWP2() != null) {
            R183Cell10.setCellValue(record1.getR183_BAL_ACT_SUB_BWP2().doubleValue());
            R183Cell10.setCellStyle(numberStyle);
        } else {
            R183Cell10.setCellValue("");
            R183Cell10.setCellStyle(textStyle);
        }

        /* ================= R184 ================= */
        row = sheet.getRow(183);
        Cell R184Cell1 = row.createCell(3);
        if (record1.getR184_FIG_BAL_BWP1() != null) {
            R184Cell1.setCellValue(record1.getR184_FIG_BAL_BWP1().doubleValue());
            R184Cell1.setCellStyle(numberStyle);
        } else {
            R184Cell1.setCellValue("");
            R184Cell1.setCellStyle(textStyle);
        }
        Cell R184Cell2 = row.createCell(4);
        if (record1.getR184_FIG_BAL_BWP2() != null) {
            R184Cell2.setCellValue(record1.getR184_FIG_BAL_BWP2().doubleValue());
            R184Cell2.setCellStyle(numberStyle);
        } else {
            R184Cell2.setCellValue("");
            R184Cell2.setCellStyle(textStyle);
        }
        Cell R184Cell3 = row.createCell(5);
        if (record1.getR184_AMT_ADJ_BWP1() != null) {
            R184Cell3.setCellValue(record1.getR184_AMT_ADJ_BWP1().doubleValue());
            R184Cell3.setCellStyle(numberStyle);
        } else {
            R184Cell3.setCellValue("");
            R184Cell3.setCellStyle(textStyle);
        }
        Cell R184Cell4 = row.createCell(6);
        if (record1.getR184_AMT_ADJ_BWP2() != null) {
            R184Cell4.setCellValue(record1.getR184_AMT_ADJ_BWP2().doubleValue());
            R184Cell4.setCellStyle(numberStyle);
        } else {
            R184Cell4.setCellValue("");
            R184Cell4.setCellStyle(textStyle);
        }
        Cell R184Cell5 = row.createCell(7);
        if (record1.getR184_NET_AMT_BWP1() != null) {
            R184Cell5.setCellValue(record1.getR184_NET_AMT_BWP1().doubleValue());
            R184Cell5.setCellStyle(numberStyle);
        } else {
            R184Cell5.setCellValue("");
            R184Cell5.setCellStyle(textStyle);
        }
        Cell R184Cell6 = row.createCell(8);
        if (record1.getR184_NET_AMT_BWP2() != null) {
            R184Cell6.setCellValue(record1.getR184_NET_AMT_BWP2().doubleValue());
            R184Cell6.setCellStyle(numberStyle);
        } else {
            R184Cell6.setCellValue("");
            R184Cell6.setCellStyle(textStyle);
        }
        Cell R184Cell7 = row.createCell(9);
        if (record1.getR184_BAL_SUB_BWP1() != null) {
            R184Cell7.setCellValue(record1.getR184_BAL_SUB_BWP1().doubleValue());
            R184Cell7.setCellStyle(numberStyle);
        } else {
            R184Cell7.setCellValue("");
            R184Cell7.setCellStyle(textStyle);
        }
        Cell R184Cell8 = row.createCell(10);
        if (record1.getR184_BAL_SUB_BWP2() != null) {
            R184Cell8.setCellValue(record1.getR184_BAL_SUB_BWP2().doubleValue());
            R184Cell8.setCellStyle(numberStyle);
        } else {
            R184Cell8.setCellValue("");
            R184Cell8.setCellStyle(textStyle);
        }
        Cell R184Cell9 = row.createCell(11);
        if (record1.getR184_BAL_ACT_SUB_BWP1() != null) {
            R184Cell9.setCellValue(record1.getR184_BAL_ACT_SUB_BWP1().doubleValue());
            R184Cell9.setCellStyle(numberStyle);
        } else {
            R184Cell9.setCellValue("");
            R184Cell9.setCellStyle(textStyle);
        }
        Cell R184Cell10 = row.createCell(12);
        if (record1.getR184_BAL_ACT_SUB_BWP2() != null) {
            R184Cell10.setCellValue(record1.getR184_BAL_ACT_SUB_BWP2().doubleValue());
            R184Cell10.setCellStyle(numberStyle);
        } else {
            R184Cell10.setCellValue("");
            R184Cell10.setCellStyle(textStyle);
        }

        /* ================= R185 ================= */
        row = sheet.getRow(184);
        Cell R185Cell1 = row.createCell(3);
        if (record1.getR185_FIG_BAL_BWP1() != null) {
            R185Cell1.setCellValue(record1.getR185_FIG_BAL_BWP1().doubleValue());
            R185Cell1.setCellStyle(numberStyle);
        } else {
            R185Cell1.setCellValue("");
            R185Cell1.setCellStyle(textStyle);
        }
        Cell R185Cell2 = row.createCell(4);
        if (record1.getR185_FIG_BAL_BWP2() != null) {
            R185Cell2.setCellValue(record1.getR185_FIG_BAL_BWP2().doubleValue());
            R185Cell2.setCellStyle(numberStyle);
        } else {
            R185Cell2.setCellValue("");
            R185Cell2.setCellStyle(textStyle);
        }
        Cell R185Cell3 = row.createCell(5);
        if (record1.getR185_AMT_ADJ_BWP1() != null) {
            R185Cell3.setCellValue(record1.getR185_AMT_ADJ_BWP1().doubleValue());
            R185Cell3.setCellStyle(numberStyle);
        } else {
            R185Cell3.setCellValue("");
            R185Cell3.setCellStyle(textStyle);
        }
        Cell R185Cell4 = row.createCell(6);
        if (record1.getR185_AMT_ADJ_BWP2() != null) {
            R185Cell4.setCellValue(record1.getR185_AMT_ADJ_BWP2().doubleValue());
            R185Cell4.setCellStyle(numberStyle);
        } else {
            R185Cell4.setCellValue("");
            R185Cell4.setCellStyle(textStyle);
        }
        Cell R185Cell5 = row.createCell(7);
        if (record1.getR185_NET_AMT_BWP1() != null) {
            R185Cell5.setCellValue(record1.getR185_NET_AMT_BWP1().doubleValue());
            R185Cell5.setCellStyle(numberStyle);
        } else {
            R185Cell5.setCellValue("");
            R185Cell5.setCellStyle(textStyle);
        }
        Cell R185Cell6 = row.createCell(8);
        if (record1.getR185_NET_AMT_BWP2() != null) {
            R185Cell6.setCellValue(record1.getR185_NET_AMT_BWP2().doubleValue());
            R185Cell6.setCellStyle(numberStyle);
        } else {
            R185Cell6.setCellValue("");
            R185Cell6.setCellStyle(textStyle);
        }
        Cell R185Cell7 = row.createCell(9);
        if (record1.getR185_BAL_SUB_BWP1() != null) {
            R185Cell7.setCellValue(record1.getR185_BAL_SUB_BWP1().doubleValue());
            R185Cell7.setCellStyle(numberStyle);
        } else {
            R185Cell7.setCellValue("");
            R185Cell7.setCellStyle(textStyle);
        }
        Cell R185Cell8 = row.createCell(10);
        if (record1.getR185_BAL_SUB_BWP2() != null) {
            R185Cell8.setCellValue(record1.getR185_BAL_SUB_BWP2().doubleValue());
            R185Cell8.setCellStyle(numberStyle);
        } else {
            R185Cell8.setCellValue("");
            R185Cell8.setCellStyle(textStyle);
        }
        Cell R185Cell9 = row.createCell(11);
        if (record1.getR185_BAL_ACT_SUB_BWP1() != null) {
            R185Cell9.setCellValue(record1.getR185_BAL_ACT_SUB_BWP1().doubleValue());
            R185Cell9.setCellStyle(numberStyle);
        } else {
            R185Cell9.setCellValue("");
            R185Cell9.setCellStyle(textStyle);
        }
        Cell R185Cell10 = row.createCell(12);
        if (record1.getR185_BAL_ACT_SUB_BWP2() != null) {
            R185Cell10.setCellValue(record1.getR185_BAL_ACT_SUB_BWP2().doubleValue());
            R185Cell10.setCellStyle(numberStyle);
        } else {
            R185Cell10.setCellValue("");
            R185Cell10.setCellStyle(textStyle);
        }

        /* ================= R186 ================= */
        row = sheet.getRow(185);
        Cell R186Cell1 = row.createCell(3);
        if (record1.getR186_FIG_BAL_BWP1() != null) {
            R186Cell1.setCellValue(record1.getR186_FIG_BAL_BWP1().doubleValue());
            R186Cell1.setCellStyle(numberStyle);
        } else {
            R186Cell1.setCellValue("");
            R186Cell1.setCellStyle(textStyle);
        }
        Cell R186Cell2 = row.createCell(4);
        if (record1.getR186_FIG_BAL_BWP2() != null) {
            R186Cell2.setCellValue(record1.getR186_FIG_BAL_BWP2().doubleValue());
            R186Cell2.setCellStyle(numberStyle);
        } else {
            R186Cell2.setCellValue("");
            R186Cell2.setCellStyle(textStyle);
        }
        Cell R186Cell3 = row.createCell(5);
        if (record1.getR186_AMT_ADJ_BWP1() != null) {
            R186Cell3.setCellValue(record1.getR186_AMT_ADJ_BWP1().doubleValue());
            R186Cell3.setCellStyle(numberStyle);
        } else {
            R186Cell3.setCellValue("");
            R186Cell3.setCellStyle(textStyle);
        }
        Cell R186Cell4 = row.createCell(6);
        if (record1.getR186_AMT_ADJ_BWP2() != null) {
            R186Cell4.setCellValue(record1.getR186_AMT_ADJ_BWP2().doubleValue());
            R186Cell4.setCellStyle(numberStyle);
        } else {
            R186Cell4.setCellValue("");
            R186Cell4.setCellStyle(textStyle);
        }
        Cell R186Cell5 = row.createCell(7);
        if (record1.getR186_NET_AMT_BWP1() != null) {
            R186Cell5.setCellValue(record1.getR186_NET_AMT_BWP1().doubleValue());
            R186Cell5.setCellStyle(numberStyle);
        } else {
            R186Cell5.setCellValue("");
            R186Cell5.setCellStyle(textStyle);
        }
        Cell R186Cell6 = row.createCell(8);
        if (record1.getR186_NET_AMT_BWP2() != null) {
            R186Cell6.setCellValue(record1.getR186_NET_AMT_BWP2().doubleValue());
            R186Cell6.setCellStyle(numberStyle);
        } else {
            R186Cell6.setCellValue("");
            R186Cell6.setCellStyle(textStyle);
        }
        Cell R186Cell7 = row.createCell(9);
        if (record1.getR186_BAL_SUB_BWP1() != null) {
            R186Cell7.setCellValue(record1.getR186_BAL_SUB_BWP1().doubleValue());
            R186Cell7.setCellStyle(numberStyle);
        } else {
            R186Cell7.setCellValue("");
            R186Cell7.setCellStyle(textStyle);
        }
        Cell R186Cell8 = row.createCell(10);
        if (record1.getR186_BAL_SUB_BWP2() != null) {
            R186Cell8.setCellValue(record1.getR186_BAL_SUB_BWP2().doubleValue());
            R186Cell8.setCellStyle(numberStyle);
        } else {
            R186Cell8.setCellValue("");
            R186Cell8.setCellStyle(textStyle);
        }
        Cell R186Cell9 = row.createCell(11);
        if (record1.getR186_BAL_ACT_SUB_BWP1() != null) {
            R186Cell9.setCellValue(record1.getR186_BAL_ACT_SUB_BWP1().doubleValue());
            R186Cell9.setCellStyle(numberStyle);
        } else {
            R186Cell9.setCellValue("");
            R186Cell9.setCellStyle(textStyle);
        }
        Cell R186Cell10 = row.createCell(12);
        if (record1.getR186_BAL_ACT_SUB_BWP2() != null) {
            R186Cell10.setCellValue(record1.getR186_BAL_ACT_SUB_BWP2().doubleValue());
            R186Cell10.setCellStyle(numberStyle);
        } else {
            R186Cell10.setCellValue("");
            R186Cell10.setCellStyle(textStyle);
        }

        /* ================= R187 ================= */
        row = sheet.getRow(186);
        Cell R187Cell1 = row.createCell(3);
        if (record1.getR187_FIG_BAL_BWP1() != null) {
            R187Cell1.setCellValue(record1.getR187_FIG_BAL_BWP1().doubleValue());
            R187Cell1.setCellStyle(numberStyle);
        } else {
            R187Cell1.setCellValue("");
            R187Cell1.setCellStyle(textStyle);
        }
        Cell R187Cell2 = row.createCell(4);
        if (record1.getR187_FIG_BAL_BWP2() != null) {
            R187Cell2.setCellValue(record1.getR187_FIG_BAL_BWP2().doubleValue());
            R187Cell2.setCellStyle(numberStyle);
        } else {
            R187Cell2.setCellValue("");
            R187Cell2.setCellStyle(textStyle);
        }
        Cell R187Cell3 = row.createCell(5);
        if (record1.getR187_AMT_ADJ_BWP1() != null) {
            R187Cell3.setCellValue(record1.getR187_AMT_ADJ_BWP1().doubleValue());
            R187Cell3.setCellStyle(numberStyle);
        } else {
            R187Cell3.setCellValue("");
            R187Cell3.setCellStyle(textStyle);
        }
        Cell R187Cell4 = row.createCell(6);
        if (record1.getR187_AMT_ADJ_BWP2() != null) {
            R187Cell4.setCellValue(record1.getR187_AMT_ADJ_BWP2().doubleValue());
            R187Cell4.setCellStyle(numberStyle);
        } else {
            R187Cell4.setCellValue("");
            R187Cell4.setCellStyle(textStyle);
        }
        Cell R187Cell5 = row.createCell(7);
        if (record1.getR187_NET_AMT_BWP1() != null) {
            R187Cell5.setCellValue(record1.getR187_NET_AMT_BWP1().doubleValue());
            R187Cell5.setCellStyle(numberStyle);
        } else {
            R187Cell5.setCellValue("");
            R187Cell5.setCellStyle(textStyle);
        }
        Cell R187Cell6 = row.createCell(8);
        if (record1.getR187_NET_AMT_BWP2() != null) {
            R187Cell6.setCellValue(record1.getR187_NET_AMT_BWP2().doubleValue());
            R187Cell6.setCellStyle(numberStyle);
        } else {
            R187Cell6.setCellValue("");
            R187Cell6.setCellStyle(textStyle);
        }
        Cell R187Cell7 = row.createCell(9);
        if (record1.getR187_BAL_SUB_BWP1() != null) {
            R187Cell7.setCellValue(record1.getR187_BAL_SUB_BWP1().doubleValue());
            R187Cell7.setCellStyle(numberStyle);
        } else {
            R187Cell7.setCellValue("");
            R187Cell7.setCellStyle(textStyle);
        }
        Cell R187Cell8 = row.createCell(10);
        if (record1.getR187_BAL_SUB_BWP2() != null) {
            R187Cell8.setCellValue(record1.getR187_BAL_SUB_BWP2().doubleValue());
            R187Cell8.setCellStyle(numberStyle);
        } else {
            R187Cell8.setCellValue("");
            R187Cell8.setCellStyle(textStyle);
        }
        Cell R187Cell9 = row.createCell(11);
        if (record1.getR187_BAL_ACT_SUB_BWP1() != null) {
            R187Cell9.setCellValue(record1.getR187_BAL_ACT_SUB_BWP1().doubleValue());
            R187Cell9.setCellStyle(numberStyle);
        } else {
            R187Cell9.setCellValue("");
            R187Cell9.setCellStyle(textStyle);
        }
        Cell R187Cell10 = row.createCell(12);
        if (record1.getR187_BAL_ACT_SUB_BWP2() != null) {
            R187Cell10.setCellValue(record1.getR187_BAL_ACT_SUB_BWP2().doubleValue());
            R187Cell10.setCellStyle(numberStyle);
        } else {
            R187Cell10.setCellValue("");
            R187Cell10.setCellStyle(textStyle);
        }

        /* ================= R188 ================= */
        row = sheet.getRow(187);
        Cell R188Cell1 = row.createCell(3);
        if (record1.getR188_FIG_BAL_BWP1() != null) {
            R188Cell1.setCellValue(record1.getR188_FIG_BAL_BWP1().doubleValue());
            R188Cell1.setCellStyle(numberStyle);
        } else {
            R188Cell1.setCellValue("");
            R188Cell1.setCellStyle(textStyle);
        }
        Cell R188Cell2 = row.createCell(4);
        if (record1.getR188_FIG_BAL_BWP2() != null) {
            R188Cell2.setCellValue(record1.getR188_FIG_BAL_BWP2().doubleValue());
            R188Cell2.setCellStyle(numberStyle);
        } else {
            R188Cell2.setCellValue("");
            R188Cell2.setCellStyle(textStyle);
        }
        Cell R188Cell3 = row.createCell(5);
        if (record1.getR188_AMT_ADJ_BWP1() != null) {
            R188Cell3.setCellValue(record1.getR188_AMT_ADJ_BWP1().doubleValue());
            R188Cell3.setCellStyle(numberStyle);
        } else {
            R188Cell3.setCellValue("");
            R188Cell3.setCellStyle(textStyle);
        }
        Cell R188Cell4 = row.createCell(6);
        if (record1.getR188_AMT_ADJ_BWP2() != null) {
            R188Cell4.setCellValue(record1.getR188_AMT_ADJ_BWP2().doubleValue());
            R188Cell4.setCellStyle(numberStyle);
        } else {
            R188Cell4.setCellValue("");
            R188Cell4.setCellStyle(textStyle);
        }
        Cell R188Cell5 = row.createCell(7);
        if (record1.getR188_NET_AMT_BWP1() != null) {
            R188Cell5.setCellValue(record1.getR188_NET_AMT_BWP1().doubleValue());
            R188Cell5.setCellStyle(numberStyle);
        } else {
            R188Cell5.setCellValue("");
            R188Cell5.setCellStyle(textStyle);
        }
        Cell R188Cell6 = row.createCell(8);
        if (record1.getR188_NET_AMT_BWP2() != null) {
            R188Cell6.setCellValue(record1.getR188_NET_AMT_BWP2().doubleValue());
            R188Cell6.setCellStyle(numberStyle);
        } else {
            R188Cell6.setCellValue("");
            R188Cell6.setCellStyle(textStyle);
        }
        Cell R188Cell7 = row.createCell(9);
        if (record1.getR188_BAL_SUB_BWP1() != null) {
            R188Cell7.setCellValue(record1.getR188_BAL_SUB_BWP1().doubleValue());
            R188Cell7.setCellStyle(numberStyle);
        } else {
            R188Cell7.setCellValue("");
            R188Cell7.setCellStyle(textStyle);
        }
        Cell R188Cell8 = row.createCell(10);
        if (record1.getR188_BAL_SUB_BWP2() != null) {
            R188Cell8.setCellValue(record1.getR188_BAL_SUB_BWP2().doubleValue());
            R188Cell8.setCellStyle(numberStyle);
        } else {
            R188Cell8.setCellValue("");
            R188Cell8.setCellStyle(textStyle);
        }
        Cell R188Cell9 = row.createCell(11);
        if (record1.getR188_BAL_ACT_SUB_BWP1() != null) {
            R188Cell9.setCellValue(record1.getR188_BAL_ACT_SUB_BWP1().doubleValue());
            R188Cell9.setCellStyle(numberStyle);
        } else {
            R188Cell9.setCellValue("");
            R188Cell9.setCellStyle(textStyle);
        }
        Cell R188Cell10 = row.createCell(12);
        if (record1.getR188_BAL_ACT_SUB_BWP2() != null) {
            R188Cell10.setCellValue(record1.getR188_BAL_ACT_SUB_BWP2().doubleValue());
            R188Cell10.setCellStyle(numberStyle);
        } else {
            R188Cell10.setCellValue("");
            R188Cell10.setCellStyle(textStyle);
        }

        /* ================= R189 ================= */
        row = sheet.getRow(188);
        Cell R189Cell1 = row.createCell(3);
        if (record1.getR189_FIG_BAL_BWP1() != null) {
            R189Cell1.setCellValue(record1.getR189_FIG_BAL_BWP1().doubleValue());
            R189Cell1.setCellStyle(numberStyle);
        } else {
            R189Cell1.setCellValue("");
            R189Cell1.setCellStyle(textStyle);
        }
        Cell R189Cell2 = row.createCell(4);
        if (record1.getR189_FIG_BAL_BWP2() != null) {
            R189Cell2.setCellValue(record1.getR189_FIG_BAL_BWP2().doubleValue());
            R189Cell2.setCellStyle(numberStyle);
        } else {
            R189Cell2.setCellValue("");
            R189Cell2.setCellStyle(textStyle);
        }
        Cell R189Cell3 = row.createCell(5);
        if (record1.getR189_AMT_ADJ_BWP1() != null) {
            R189Cell3.setCellValue(record1.getR189_AMT_ADJ_BWP1().doubleValue());
            R189Cell3.setCellStyle(numberStyle);
        } else {
            R189Cell3.setCellValue("");
            R189Cell3.setCellStyle(textStyle);
        }
        Cell R189Cell4 = row.createCell(6);
        if (record1.getR189_AMT_ADJ_BWP2() != null) {
            R189Cell4.setCellValue(record1.getR189_AMT_ADJ_BWP2().doubleValue());
            R189Cell4.setCellStyle(numberStyle);
        } else {
            R189Cell4.setCellValue("");
            R189Cell4.setCellStyle(textStyle);
        }
        Cell R189Cell5 = row.createCell(7);
        if (record1.getR189_NET_AMT_BWP1() != null) {
            R189Cell5.setCellValue(record1.getR189_NET_AMT_BWP1().doubleValue());
            R189Cell5.setCellStyle(numberStyle);
        } else {
            R189Cell5.setCellValue("");
            R189Cell5.setCellStyle(textStyle);
        }
        Cell R189Cell6 = row.createCell(8);
        if (record1.getR189_NET_AMT_BWP2() != null) {
            R189Cell6.setCellValue(record1.getR189_NET_AMT_BWP2().doubleValue());
            R189Cell6.setCellStyle(numberStyle);
        } else {
            R189Cell6.setCellValue("");
            R189Cell6.setCellStyle(textStyle);
        }
        Cell R189Cell7 = row.createCell(9);
        if (record1.getR189_BAL_SUB_BWP1() != null) {
            R189Cell7.setCellValue(record1.getR189_BAL_SUB_BWP1().doubleValue());
            R189Cell7.setCellStyle(numberStyle);
        } else {
            R189Cell7.setCellValue("");
            R189Cell7.setCellStyle(textStyle);
        }
        Cell R189Cell8 = row.createCell(10);
        if (record1.getR189_BAL_SUB_BWP2() != null) {
            R189Cell8.setCellValue(record1.getR189_BAL_SUB_BWP2().doubleValue());
            R189Cell8.setCellStyle(numberStyle);
        } else {
            R189Cell8.setCellValue("");
            R189Cell8.setCellStyle(textStyle);
        }
        Cell R189Cell9 = row.createCell(11);
        if (record1.getR189_BAL_ACT_SUB_BWP1() != null) {
            R189Cell9.setCellValue(record1.getR189_BAL_ACT_SUB_BWP1().doubleValue());
            R189Cell9.setCellStyle(numberStyle);
        } else {
            R189Cell9.setCellValue("");
            R189Cell9.setCellStyle(textStyle);
        }
        Cell R189Cell10 = row.createCell(12);
        if (record1.getR189_BAL_ACT_SUB_BWP2() != null) {
            R189Cell10.setCellValue(record1.getR189_BAL_ACT_SUB_BWP2().doubleValue());
            R189Cell10.setCellStyle(numberStyle);
        } else {
            R189Cell10.setCellValue("");
            R189Cell10.setCellStyle(textStyle);
        }

        /* ================= R190 ================= */
        row = sheet.getRow(189);
        Cell R190Cell1 = row.createCell(3);
        if (record1.getR190_FIG_BAL_BWP1() != null) {
            R190Cell1.setCellValue(record1.getR190_FIG_BAL_BWP1().doubleValue());
            R190Cell1.setCellStyle(numberStyle);
        } else {
            R190Cell1.setCellValue("");
            R190Cell1.setCellStyle(textStyle);
        }
        Cell R190Cell2 = row.createCell(4);
        if (record1.getR190_FIG_BAL_BWP2() != null) {
            R190Cell2.setCellValue(record1.getR190_FIG_BAL_BWP2().doubleValue());
            R190Cell2.setCellStyle(numberStyle);
        } else {
            R190Cell2.setCellValue("");
            R190Cell2.setCellStyle(textStyle);
        }
        Cell R190Cell3 = row.createCell(5);
        if (record1.getR190_AMT_ADJ_BWP1() != null) {
            R190Cell3.setCellValue(record1.getR190_AMT_ADJ_BWP1().doubleValue());
            R190Cell3.setCellStyle(numberStyle);
        } else {
            R190Cell3.setCellValue("");
            R190Cell3.setCellStyle(textStyle);
        }
        Cell R190Cell4 = row.createCell(6);
        if (record1.getR190_AMT_ADJ_BWP2() != null) {
            R190Cell4.setCellValue(record1.getR190_AMT_ADJ_BWP2().doubleValue());
            R190Cell4.setCellStyle(numberStyle);
        } else {
            R190Cell4.setCellValue("");
            R190Cell4.setCellStyle(textStyle);
        }
        Cell R190Cell5 = row.createCell(7);
        if (record1.getR190_NET_AMT_BWP1() != null) {
            R190Cell5.setCellValue(record1.getR190_NET_AMT_BWP1().doubleValue());
            R190Cell5.setCellStyle(numberStyle);
        } else {
            R190Cell5.setCellValue("");
            R190Cell5.setCellStyle(textStyle);
        }
        Cell R190Cell6 = row.createCell(8);
        if (record1.getR190_NET_AMT_BWP2() != null) {
            R190Cell6.setCellValue(record1.getR190_NET_AMT_BWP2().doubleValue());
            R190Cell6.setCellStyle(numberStyle);
        } else {
            R190Cell6.setCellValue("");
            R190Cell6.setCellStyle(textStyle);
        }
        Cell R190Cell7 = row.createCell(9);
        if (record1.getR190_BAL_SUB_BWP1() != null) {
            R190Cell7.setCellValue(record1.getR190_BAL_SUB_BWP1().doubleValue());
            R190Cell7.setCellStyle(numberStyle);
        } else {
            R190Cell7.setCellValue("");
            R190Cell7.setCellStyle(textStyle);
        }
        Cell R190Cell8 = row.createCell(10);
        if (record1.getR190_BAL_SUB_BWP2() != null) {
            R190Cell8.setCellValue(record1.getR190_BAL_SUB_BWP2().doubleValue());
            R190Cell8.setCellStyle(numberStyle);
        } else {
            R190Cell8.setCellValue("");
            R190Cell8.setCellStyle(textStyle);
        }
        Cell R190Cell9 = row.createCell(11);
        if (record1.getR190_BAL_ACT_SUB_BWP1() != null) {
            R190Cell9.setCellValue(record1.getR190_BAL_ACT_SUB_BWP1().doubleValue());
            R190Cell9.setCellStyle(numberStyle);
        } else {
            R190Cell9.setCellValue("");
            R190Cell9.setCellStyle(textStyle);
        }
        Cell R190Cell10 = row.createCell(12);
        if (record1.getR190_BAL_ACT_SUB_BWP2() != null) {
            R190Cell10.setCellValue(record1.getR190_BAL_ACT_SUB_BWP2().doubleValue());
            R190Cell10.setCellStyle(numberStyle);
        } else {
            R190Cell10.setCellValue("");
            R190Cell10.setCellStyle(textStyle);
        }

        /* ================= R191 ================= */
        row = sheet.getRow(190);
        Cell R191Cell1 = row.createCell(3);
        if (record1.getR191_FIG_BAL_BWP1() != null) {
            R191Cell1.setCellValue(record1.getR191_FIG_BAL_BWP1().doubleValue());
            R191Cell1.setCellStyle(numberStyle);
        } else {
            R191Cell1.setCellValue("");
            R191Cell1.setCellStyle(textStyle);
        }
        Cell R191Cell2 = row.createCell(4);
        if (record1.getR191_FIG_BAL_BWP2() != null) {
            R191Cell2.setCellValue(record1.getR191_FIG_BAL_BWP2().doubleValue());
            R191Cell2.setCellStyle(numberStyle);
        } else {
            R191Cell2.setCellValue("");
            R191Cell2.setCellStyle(textStyle);
        }
        Cell R191Cell3 = row.createCell(5);
        if (record1.getR191_AMT_ADJ_BWP1() != null) {
            R191Cell3.setCellValue(record1.getR191_AMT_ADJ_BWP1().doubleValue());
            R191Cell3.setCellStyle(numberStyle);
        } else {
            R191Cell3.setCellValue("");
            R191Cell3.setCellStyle(textStyle);
        }
        Cell R191Cell4 = row.createCell(6);
        if (record1.getR191_AMT_ADJ_BWP2() != null) {
            R191Cell4.setCellValue(record1.getR191_AMT_ADJ_BWP2().doubleValue());
            R191Cell4.setCellStyle(numberStyle);
        } else {
            R191Cell4.setCellValue("");
            R191Cell4.setCellStyle(textStyle);
        }
        Cell R191Cell5 = row.createCell(7);
        if (record1.getR191_NET_AMT_BWP1() != null) {
            R191Cell5.setCellValue(record1.getR191_NET_AMT_BWP1().doubleValue());
            R191Cell5.setCellStyle(numberStyle);
        } else {
            R191Cell5.setCellValue("");
            R191Cell5.setCellStyle(textStyle);
        }
        Cell R191Cell6 = row.createCell(8);
        if (record1.getR191_NET_AMT_BWP2() != null) {
            R191Cell6.setCellValue(record1.getR191_NET_AMT_BWP2().doubleValue());
            R191Cell6.setCellStyle(numberStyle);
        } else {
            R191Cell6.setCellValue("");
            R191Cell6.setCellStyle(textStyle);
        }
        Cell R191Cell7 = row.createCell(9);
        if (record1.getR191_BAL_SUB_BWP1() != null) {
            R191Cell7.setCellValue(record1.getR191_BAL_SUB_BWP1().doubleValue());
            R191Cell7.setCellStyle(numberStyle);
        } else {
            R191Cell7.setCellValue("");
            R191Cell7.setCellStyle(textStyle);
        }
        Cell R191Cell8 = row.createCell(10);
        if (record1.getR191_BAL_SUB_BWP2() != null) {
            R191Cell8.setCellValue(record1.getR191_BAL_SUB_BWP2().doubleValue());
            R191Cell8.setCellStyle(numberStyle);
        } else {
            R191Cell8.setCellValue("");
            R191Cell8.setCellStyle(textStyle);
        }
        Cell R191Cell9 = row.createCell(11);
        if (record1.getR191_BAL_ACT_SUB_BWP1() != null) {
            R191Cell9.setCellValue(record1.getR191_BAL_ACT_SUB_BWP1().doubleValue());
            R191Cell9.setCellStyle(numberStyle);
        } else {
            R191Cell9.setCellValue("");
            R191Cell9.setCellStyle(textStyle);
        }
        Cell R191Cell10 = row.createCell(12);
        if (record1.getR191_BAL_ACT_SUB_BWP2() != null) {
            R191Cell10.setCellValue(record1.getR191_BAL_ACT_SUB_BWP2().doubleValue());
            R191Cell10.setCellStyle(numberStyle);
        } else {
            R191Cell10.setCellValue("");
            R191Cell10.setCellStyle(textStyle);
        }

        /* ================= R192 ================= */
        row = sheet.getRow(191);
        Cell R192Cell1 = row.createCell(3);
        if (record1.getR192_FIG_BAL_BWP1() != null) {
            R192Cell1.setCellValue(record1.getR192_FIG_BAL_BWP1().doubleValue());
            R192Cell1.setCellStyle(numberStyle);
        } else {
            R192Cell1.setCellValue("");
            R192Cell1.setCellStyle(textStyle);
        }
        Cell R192Cell2 = row.createCell(4);
        if (record1.getR192_FIG_BAL_BWP2() != null) {
            R192Cell2.setCellValue(record1.getR192_FIG_BAL_BWP2().doubleValue());
            R192Cell2.setCellStyle(numberStyle);
        } else {
            R192Cell2.setCellValue("");
            R192Cell2.setCellStyle(textStyle);
        }
        Cell R192Cell3 = row.createCell(5);
        if (record1.getR192_AMT_ADJ_BWP1() != null) {
            R192Cell3.setCellValue(record1.getR192_AMT_ADJ_BWP1().doubleValue());
            R192Cell3.setCellStyle(numberStyle);
        } else {
            R192Cell3.setCellValue("");
            R192Cell3.setCellStyle(textStyle);
        }
        Cell R192Cell4 = row.createCell(6);
        if (record1.getR192_AMT_ADJ_BWP2() != null) {
            R192Cell4.setCellValue(record1.getR192_AMT_ADJ_BWP2().doubleValue());
            R192Cell4.setCellStyle(numberStyle);
        } else {
            R192Cell4.setCellValue("");
            R192Cell4.setCellStyle(textStyle);
        }
        Cell R192Cell5 = row.createCell(7);
        if (record1.getR192_NET_AMT_BWP1() != null) {
            R192Cell5.setCellValue(record1.getR192_NET_AMT_BWP1().doubleValue());
            R192Cell5.setCellStyle(numberStyle);
        } else {
            R192Cell5.setCellValue("");
            R192Cell5.setCellStyle(textStyle);
        }
        Cell R192Cell6 = row.createCell(8);
        if (record1.getR192_NET_AMT_BWP2() != null) {
            R192Cell6.setCellValue(record1.getR192_NET_AMT_BWP2().doubleValue());
            R192Cell6.setCellStyle(numberStyle);
        } else {
            R192Cell6.setCellValue("");
            R192Cell6.setCellStyle(textStyle);
        }
        Cell R192Cell7 = row.createCell(9);
        if (record1.getR192_BAL_SUB_BWP1() != null) {
            R192Cell7.setCellValue(record1.getR192_BAL_SUB_BWP1().doubleValue());
            R192Cell7.setCellStyle(numberStyle);
        } else {
            R192Cell7.setCellValue("");
            R192Cell7.setCellStyle(textStyle);
        }
        Cell R192Cell8 = row.createCell(10);
        if (record1.getR192_BAL_SUB_BWP2() != null) {
            R192Cell8.setCellValue(record1.getR192_BAL_SUB_BWP2().doubleValue());
            R192Cell8.setCellStyle(numberStyle);
        } else {
            R192Cell8.setCellValue("");
            R192Cell8.setCellStyle(textStyle);
        }
        Cell R192Cell9 = row.createCell(11);
        if (record1.getR192_BAL_ACT_SUB_BWP1() != null) {
            R192Cell9.setCellValue(record1.getR192_BAL_ACT_SUB_BWP1().doubleValue());
            R192Cell9.setCellStyle(numberStyle);
        } else {
            R192Cell9.setCellValue("");
            R192Cell9.setCellStyle(textStyle);
        }
        Cell R192Cell10 = row.createCell(12);
        if (record1.getR192_BAL_ACT_SUB_BWP2() != null) {
            R192Cell10.setCellValue(record1.getR192_BAL_ACT_SUB_BWP2().doubleValue());
            R192Cell10.setCellStyle(numberStyle);
        } else {
            R192Cell10.setCellValue("");
            R192Cell10.setCellStyle(textStyle);
        }

        /* ================= R193 ================= */
        row = sheet.getRow(192);
        Cell R193Cell1 = row.createCell(3);
        if (record1.getR193_FIG_BAL_BWP1() != null) {
            R193Cell1.setCellValue(record1.getR193_FIG_BAL_BWP1().doubleValue());
            R193Cell1.setCellStyle(numberStyle);
        } else {
            R193Cell1.setCellValue("");
            R193Cell1.setCellStyle(textStyle);
        }
        Cell R193Cell2 = row.createCell(4);
        if (record1.getR193_FIG_BAL_BWP2() != null) {
            R193Cell2.setCellValue(record1.getR193_FIG_BAL_BWP2().doubleValue());
            R193Cell2.setCellStyle(numberStyle);
        } else {
            R193Cell2.setCellValue("");
            R193Cell2.setCellStyle(textStyle);
        }
        Cell R193Cell3 = row.createCell(5);
        if (record1.getR193_AMT_ADJ_BWP1() != null) {
            R193Cell3.setCellValue(record1.getR193_AMT_ADJ_BWP1().doubleValue());
            R193Cell3.setCellStyle(numberStyle);
        } else {
            R193Cell3.setCellValue("");
            R193Cell3.setCellStyle(textStyle);
        }
        Cell R193Cell4 = row.createCell(6);
        if (record1.getR193_AMT_ADJ_BWP2() != null) {
            R193Cell4.setCellValue(record1.getR193_AMT_ADJ_BWP2().doubleValue());
            R193Cell4.setCellStyle(numberStyle);
        } else {
            R193Cell4.setCellValue("");
            R193Cell4.setCellStyle(textStyle);
        }
        Cell R193Cell5 = row.createCell(7);
        if (record1.getR193_NET_AMT_BWP1() != null) {
            R193Cell5.setCellValue(record1.getR193_NET_AMT_BWP1().doubleValue());
            R193Cell5.setCellStyle(numberStyle);
        } else {
            R193Cell5.setCellValue("");
            R193Cell5.setCellStyle(textStyle);
        }
        Cell R193Cell6 = row.createCell(8);
        if (record1.getR193_NET_AMT_BWP2() != null) {
            R193Cell6.setCellValue(record1.getR193_NET_AMT_BWP2().doubleValue());
            R193Cell6.setCellStyle(numberStyle);
        } else {
            R193Cell6.setCellValue("");
            R193Cell6.setCellStyle(textStyle);
        }
        Cell R193Cell7 = row.createCell(9);
        if (record1.getR193_BAL_SUB_BWP1() != null) {
            R193Cell7.setCellValue(record1.getR193_BAL_SUB_BWP1().doubleValue());
            R193Cell7.setCellStyle(numberStyle);
        } else {
            R193Cell7.setCellValue("");
            R193Cell7.setCellStyle(textStyle);
        }
        Cell R193Cell8 = row.createCell(10);
        if (record1.getR193_BAL_SUB_BWP2() != null) {
            R193Cell8.setCellValue(record1.getR193_BAL_SUB_BWP2().doubleValue());
            R193Cell8.setCellStyle(numberStyle);
        } else {
            R193Cell8.setCellValue("");
            R193Cell8.setCellStyle(textStyle);
        }
        Cell R193Cell9 = row.createCell(11);
        if (record1.getR193_BAL_ACT_SUB_BWP1() != null) {
            R193Cell9.setCellValue(record1.getR193_BAL_ACT_SUB_BWP1().doubleValue());
            R193Cell9.setCellStyle(numberStyle);
        } else {
            R193Cell9.setCellValue("");
            R193Cell9.setCellStyle(textStyle);
        }
        Cell R193Cell10 = row.createCell(12);
        if (record1.getR193_BAL_ACT_SUB_BWP2() != null) {
            R193Cell10.setCellValue(record1.getR193_BAL_ACT_SUB_BWP2().doubleValue());
            R193Cell10.setCellStyle(numberStyle);
        } else {
            R193Cell10.setCellValue("");
            R193Cell10.setCellStyle(textStyle);
        }

        /* ================= R194 ================= */
        row = sheet.getRow(193);
        Cell R194Cell1 = row.createCell(3);
        if (record1.getR194_FIG_BAL_BWP1() != null) {
            R194Cell1.setCellValue(record1.getR194_FIG_BAL_BWP1().doubleValue());
            R194Cell1.setCellStyle(numberStyle);
        } else {
            R194Cell1.setCellValue("");
            R194Cell1.setCellStyle(textStyle);
        }
        Cell R194Cell2 = row.createCell(4);
        if (record1.getR194_FIG_BAL_BWP2() != null) {
            R194Cell2.setCellValue(record1.getR194_FIG_BAL_BWP2().doubleValue());
            R194Cell2.setCellStyle(numberStyle);
        } else {
            R194Cell2.setCellValue("");
            R194Cell2.setCellStyle(textStyle);
        }
        Cell R194Cell3 = row.createCell(5);
        if (record1.getR194_AMT_ADJ_BWP1() != null) {
            R194Cell3.setCellValue(record1.getR194_AMT_ADJ_BWP1().doubleValue());
            R194Cell3.setCellStyle(numberStyle);
        } else {
            R194Cell3.setCellValue("");
            R194Cell3.setCellStyle(textStyle);
        }
        Cell R194Cell4 = row.createCell(6);
        if (record1.getR194_AMT_ADJ_BWP2() != null) {
            R194Cell4.setCellValue(record1.getR194_AMT_ADJ_BWP2().doubleValue());
            R194Cell4.setCellStyle(numberStyle);
        } else {
            R194Cell4.setCellValue("");
            R194Cell4.setCellStyle(textStyle);
        }
        Cell R194Cell5 = row.createCell(7);
        if (record1.getR194_NET_AMT_BWP1() != null) {
            R194Cell5.setCellValue(record1.getR194_NET_AMT_BWP1().doubleValue());
            R194Cell5.setCellStyle(numberStyle);
        } else {
            R194Cell5.setCellValue("");
            R194Cell5.setCellStyle(textStyle);
        }
        Cell R194Cell6 = row.createCell(8);
        if (record1.getR194_NET_AMT_BWP2() != null) {
            R194Cell6.setCellValue(record1.getR194_NET_AMT_BWP2().doubleValue());
            R194Cell6.setCellStyle(numberStyle);
        } else {
            R194Cell6.setCellValue("");
            R194Cell6.setCellStyle(textStyle);
        }
        Cell R194Cell7 = row.createCell(9);
        if (record1.getR194_BAL_SUB_BWP1() != null) {
            R194Cell7.setCellValue(record1.getR194_BAL_SUB_BWP1().doubleValue());
            R194Cell7.setCellStyle(numberStyle);
        } else {
            R194Cell7.setCellValue("");
            R194Cell7.setCellStyle(textStyle);
        }
        Cell R194Cell8 = row.createCell(10);
        if (record1.getR194_BAL_SUB_BWP2() != null) {
            R194Cell8.setCellValue(record1.getR194_BAL_SUB_BWP2().doubleValue());
            R194Cell8.setCellStyle(numberStyle);
        } else {
            R194Cell8.setCellValue("");
            R194Cell8.setCellStyle(textStyle);
        }
        Cell R194Cell9 = row.createCell(11);
        if (record1.getR194_BAL_ACT_SUB_BWP1() != null) {
            R194Cell9.setCellValue(record1.getR194_BAL_ACT_SUB_BWP1().doubleValue());
            R194Cell9.setCellStyle(numberStyle);
        } else {
            R194Cell9.setCellValue("");
            R194Cell9.setCellStyle(textStyle);
        }
        Cell R194Cell10 = row.createCell(12);
        if (record1.getR194_BAL_ACT_SUB_BWP2() != null) {
            R194Cell10.setCellValue(record1.getR194_BAL_ACT_SUB_BWP2().doubleValue());
            R194Cell10.setCellStyle(numberStyle);
        } else {
            R194Cell10.setCellValue("");
            R194Cell10.setCellStyle(textStyle);
        }

        /* ================= R195 ================= */
        row = sheet.getRow(194);
        Cell R195Cell1 = row.createCell(3);
        if (record1.getR195_FIG_BAL_BWP1() != null) {
            R195Cell1.setCellValue(record1.getR195_FIG_BAL_BWP1().doubleValue());
            R195Cell1.setCellStyle(numberStyle);
        } else {
            R195Cell1.setCellValue("");
            R195Cell1.setCellStyle(textStyle);
        }
        Cell R195Cell2 = row.createCell(4);
        if (record1.getR195_FIG_BAL_BWP2() != null) {
            R195Cell2.setCellValue(record1.getR195_FIG_BAL_BWP2().doubleValue());
            R195Cell2.setCellStyle(numberStyle);
        } else {
            R195Cell2.setCellValue("");
            R195Cell2.setCellStyle(textStyle);
        }
        Cell R195Cell3 = row.createCell(5);
        if (record1.getR195_AMT_ADJ_BWP1() != null) {
            R195Cell3.setCellValue(record1.getR195_AMT_ADJ_BWP1().doubleValue());
            R195Cell3.setCellStyle(numberStyle);
        } else {
            R195Cell3.setCellValue("");
            R195Cell3.setCellStyle(textStyle);
        }
        Cell R195Cell4 = row.createCell(6);
        if (record1.getR195_AMT_ADJ_BWP2() != null) {
            R195Cell4.setCellValue(record1.getR195_AMT_ADJ_BWP2().doubleValue());
            R195Cell4.setCellStyle(numberStyle);
        } else {
            R195Cell4.setCellValue("");
            R195Cell4.setCellStyle(textStyle);
        }
        Cell R195Cell5 = row.createCell(7);
        if (record1.getR195_NET_AMT_BWP1() != null) {
            R195Cell5.setCellValue(record1.getR195_NET_AMT_BWP1().doubleValue());
            R195Cell5.setCellStyle(numberStyle);
        } else {
            R195Cell5.setCellValue("");
            R195Cell5.setCellStyle(textStyle);
        }
        Cell R195Cell6 = row.createCell(8);
        if (record1.getR195_NET_AMT_BWP2() != null) {
            R195Cell6.setCellValue(record1.getR195_NET_AMT_BWP2().doubleValue());
            R195Cell6.setCellStyle(numberStyle);
        } else {
            R195Cell6.setCellValue("");
            R195Cell6.setCellStyle(textStyle);
        }
        Cell R195Cell7 = row.createCell(9);
        if (record1.getR195_BAL_SUB_BWP1() != null) {
            R195Cell7.setCellValue(record1.getR195_BAL_SUB_BWP1().doubleValue());
            R195Cell7.setCellStyle(numberStyle);
        } else {
            R195Cell7.setCellValue("");
            R195Cell7.setCellStyle(textStyle);
        }
        Cell R195Cell8 = row.createCell(10);
        if (record1.getR195_BAL_SUB_BWP2() != null) {
            R195Cell8.setCellValue(record1.getR195_BAL_SUB_BWP2().doubleValue());
            R195Cell8.setCellStyle(numberStyle);
        } else {
            R195Cell8.setCellValue("");
            R195Cell8.setCellStyle(textStyle);
        }
        Cell R195Cell9 = row.createCell(11);
        if (record1.getR195_BAL_ACT_SUB_BWP1() != null) {
            R195Cell9.setCellValue(record1.getR195_BAL_ACT_SUB_BWP1().doubleValue());
            R195Cell9.setCellStyle(numberStyle);
        } else {
            R195Cell9.setCellValue("");
            R195Cell9.setCellStyle(textStyle);
        }
        Cell R195Cell10 = row.createCell(12);
        if (record1.getR195_BAL_ACT_SUB_BWP2() != null) {
            R195Cell10.setCellValue(record1.getR195_BAL_ACT_SUB_BWP2().doubleValue());
            R195Cell10.setCellStyle(numberStyle);
        } else {
            R195Cell10.setCellValue("");
            R195Cell10.setCellStyle(textStyle);
        }

        /* ================= R196 ================= */
        row = sheet.getRow(195);
        Cell R196Cell1 = row.createCell(3);
        if (record1.getR196_FIG_BAL_BWP1() != null) {
            R196Cell1.setCellValue(record1.getR196_FIG_BAL_BWP1().doubleValue());
            R196Cell1.setCellStyle(numberStyle);
        } else {
            R196Cell1.setCellValue("");
            R196Cell1.setCellStyle(textStyle);
        }
        Cell R196Cell2 = row.createCell(4);
        if (record1.getR196_FIG_BAL_BWP2() != null) {
            R196Cell2.setCellValue(record1.getR196_FIG_BAL_BWP2().doubleValue());
            R196Cell2.setCellStyle(numberStyle);
        } else {
            R196Cell2.setCellValue("");
            R196Cell2.setCellStyle(textStyle);
        }
        Cell R196Cell3 = row.createCell(5);
        if (record1.getR196_AMT_ADJ_BWP1() != null) {
            R196Cell3.setCellValue(record1.getR196_AMT_ADJ_BWP1().doubleValue());
            R196Cell3.setCellStyle(numberStyle);
        } else {
            R196Cell3.setCellValue("");
            R196Cell3.setCellStyle(textStyle);
        }
        Cell R196Cell4 = row.createCell(6);
        if (record1.getR196_AMT_ADJ_BWP2() != null) {
            R196Cell4.setCellValue(record1.getR196_AMT_ADJ_BWP2().doubleValue());
            R196Cell4.setCellStyle(numberStyle);
        } else {
            R196Cell4.setCellValue("");
            R196Cell4.setCellStyle(textStyle);
        }
        Cell R196Cell5 = row.createCell(7);
        if (record1.getR196_NET_AMT_BWP1() != null) {
            R196Cell5.setCellValue(record1.getR196_NET_AMT_BWP1().doubleValue());
            R196Cell5.setCellStyle(numberStyle);
        } else {
            R196Cell5.setCellValue("");
            R196Cell5.setCellStyle(textStyle);
        }
        Cell R196Cell6 = row.createCell(8);
        if (record1.getR196_NET_AMT_BWP2() != null) {
            R196Cell6.setCellValue(record1.getR196_NET_AMT_BWP2().doubleValue());
            R196Cell6.setCellStyle(numberStyle);
        } else {
            R196Cell6.setCellValue("");
            R196Cell6.setCellStyle(textStyle);
        }
        Cell R196Cell7 = row.createCell(9);
        if (record1.getR196_BAL_SUB_BWP1() != null) {
            R196Cell7.setCellValue(record1.getR196_BAL_SUB_BWP1().doubleValue());
            R196Cell7.setCellStyle(numberStyle);
        } else {
            R196Cell7.setCellValue("");
            R196Cell7.setCellStyle(textStyle);
        }
        Cell R196Cell8 = row.createCell(10);
        if (record1.getR196_BAL_SUB_BWP2() != null) {
            R196Cell8.setCellValue(record1.getR196_BAL_SUB_BWP2().doubleValue());
            R196Cell8.setCellStyle(numberStyle);
        } else {
            R196Cell8.setCellValue("");
            R196Cell8.setCellStyle(textStyle);
        }
        Cell R196Cell9 = row.createCell(11);
        if (record1.getR196_BAL_ACT_SUB_BWP1() != null) {
            R196Cell9.setCellValue(record1.getR196_BAL_ACT_SUB_BWP1().doubleValue());
            R196Cell9.setCellStyle(numberStyle);
        } else {
            R196Cell9.setCellValue("");
            R196Cell9.setCellStyle(textStyle);
        }
        Cell R196Cell10 = row.createCell(12);
        if (record1.getR196_BAL_ACT_SUB_BWP2() != null) {
            R196Cell10.setCellValue(record1.getR196_BAL_ACT_SUB_BWP2().doubleValue());
            R196Cell10.setCellStyle(numberStyle);
        } else {
            R196Cell10.setCellValue("");
            R196Cell10.setCellStyle(textStyle);
        }

        /* ================= R197 ================= */
        row = sheet.getRow(196);
        Cell R197Cell1 = row.createCell(3);
        if (record1.getR197_FIG_BAL_BWP1() != null) {
            R197Cell1.setCellValue(record1.getR197_FIG_BAL_BWP1().doubleValue());
            R197Cell1.setCellStyle(numberStyle);
        } else {
            R197Cell1.setCellValue("");
            R197Cell1.setCellStyle(textStyle);
        }
        Cell R197Cell2 = row.createCell(4);
        if (record1.getR197_FIG_BAL_BWP2() != null) {
            R197Cell2.setCellValue(record1.getR197_FIG_BAL_BWP2().doubleValue());
            R197Cell2.setCellStyle(numberStyle);
        } else {
            R197Cell2.setCellValue("");
            R197Cell2.setCellStyle(textStyle);
        }
        Cell R197Cell3 = row.createCell(5);
        if (record1.getR197_AMT_ADJ_BWP1() != null) {
            R197Cell3.setCellValue(record1.getR197_AMT_ADJ_BWP1().doubleValue());
            R197Cell3.setCellStyle(numberStyle);
        } else {
            R197Cell3.setCellValue("");
            R197Cell3.setCellStyle(textStyle);
        }
        Cell R197Cell4 = row.createCell(6);
        if (record1.getR197_AMT_ADJ_BWP2() != null) {
            R197Cell4.setCellValue(record1.getR197_AMT_ADJ_BWP2().doubleValue());
            R197Cell4.setCellStyle(numberStyle);
        } else {
            R197Cell4.setCellValue("");
            R197Cell4.setCellStyle(textStyle);
        }
        Cell R197Cell5 = row.createCell(7);
        if (record1.getR197_NET_AMT_BWP1() != null) {
            R197Cell5.setCellValue(record1.getR197_NET_AMT_BWP1().doubleValue());
            R197Cell5.setCellStyle(numberStyle);
        } else {
            R197Cell5.setCellValue("");
            R197Cell5.setCellStyle(textStyle);
        }
        Cell R197Cell6 = row.createCell(8);
        if (record1.getR197_NET_AMT_BWP2() != null) {
            R197Cell6.setCellValue(record1.getR197_NET_AMT_BWP2().doubleValue());
            R197Cell6.setCellStyle(numberStyle);
        } else {
            R197Cell6.setCellValue("");
            R197Cell6.setCellStyle(textStyle);
        }
        Cell R197Cell7 = row.createCell(9);
        if (record1.getR197_BAL_SUB_BWP1() != null) {
            R197Cell7.setCellValue(record1.getR197_BAL_SUB_BWP1().doubleValue());
            R197Cell7.setCellStyle(numberStyle);
        } else {
            R197Cell7.setCellValue("");
            R197Cell7.setCellStyle(textStyle);
        }
        Cell R197Cell8 = row.createCell(10);
        if (record1.getR197_BAL_SUB_BWP2() != null) {
            R197Cell8.setCellValue(record1.getR197_BAL_SUB_BWP2().doubleValue());
            R197Cell8.setCellStyle(numberStyle);
        } else {
            R197Cell8.setCellValue("");
            R197Cell8.setCellStyle(textStyle);
        }
        Cell R197Cell9 = row.createCell(11);
        if (record1.getR197_BAL_ACT_SUB_BWP1() != null) {
            R197Cell9.setCellValue(record1.getR197_BAL_ACT_SUB_BWP1().doubleValue());
            R197Cell9.setCellStyle(numberStyle);
        } else {
            R197Cell9.setCellValue("");
            R197Cell9.setCellStyle(textStyle);
        }
        Cell R197Cell10 = row.createCell(12);
        if (record1.getR197_BAL_ACT_SUB_BWP2() != null) {
            R197Cell10.setCellValue(record1.getR197_BAL_ACT_SUB_BWP2().doubleValue());
            R197Cell10.setCellStyle(numberStyle);
        } else {
            R197Cell10.setCellValue("");
            R197Cell10.setCellStyle(textStyle);
        }

        /* ================= R198 ================= */
        row = sheet.getRow(197);
        Cell R198Cell1 = row.createCell(3);
        if (record1.getR198_FIG_BAL_BWP1() != null) {
            R198Cell1.setCellValue(record1.getR198_FIG_BAL_BWP1().doubleValue());
            R198Cell1.setCellStyle(numberStyle);
        } else {
            R198Cell1.setCellValue("");
            R198Cell1.setCellStyle(textStyle);
        }
        Cell R198Cell2 = row.createCell(4);
        if (record1.getR198_FIG_BAL_BWP2() != null) {
            R198Cell2.setCellValue(record1.getR198_FIG_BAL_BWP2().doubleValue());
            R198Cell2.setCellStyle(numberStyle);
        } else {
            R198Cell2.setCellValue("");
            R198Cell2.setCellStyle(textStyle);
        }
        Cell R198Cell3 = row.createCell(5);
        if (record1.getR198_AMT_ADJ_BWP1() != null) {
            R198Cell3.setCellValue(record1.getR198_AMT_ADJ_BWP1().doubleValue());
            R198Cell3.setCellStyle(numberStyle);
        } else {
            R198Cell3.setCellValue("");
            R198Cell3.setCellStyle(textStyle);
        }
        Cell R198Cell4 = row.createCell(6);
        if (record1.getR198_AMT_ADJ_BWP2() != null) {
            R198Cell4.setCellValue(record1.getR198_AMT_ADJ_BWP2().doubleValue());
            R198Cell4.setCellStyle(numberStyle);
        } else {
            R198Cell4.setCellValue("");
            R198Cell4.setCellStyle(textStyle);
        }
        Cell R198Cell5 = row.createCell(7);
        if (record1.getR198_NET_AMT_BWP1() != null) {
            R198Cell5.setCellValue(record1.getR198_NET_AMT_BWP1().doubleValue());
            R198Cell5.setCellStyle(numberStyle);
        } else {
            R198Cell5.setCellValue("");
            R198Cell5.setCellStyle(textStyle);
        }
        Cell R198Cell6 = row.createCell(8);
        if (record1.getR198_NET_AMT_BWP2() != null) {
            R198Cell6.setCellValue(record1.getR198_NET_AMT_BWP2().doubleValue());
            R198Cell6.setCellStyle(numberStyle);
        } else {
            R198Cell6.setCellValue("");
            R198Cell6.setCellStyle(textStyle);
        }
        Cell R198Cell7 = row.createCell(9);
        if (record1.getR198_BAL_SUB_BWP1() != null) {
            R198Cell7.setCellValue(record1.getR198_BAL_SUB_BWP1().doubleValue());
            R198Cell7.setCellStyle(numberStyle);
        } else {
            R198Cell7.setCellValue("");
            R198Cell7.setCellStyle(textStyle);
        }
        Cell R198Cell8 = row.createCell(10);
        if (record1.getR198_BAL_SUB_BWP2() != null) {
            R198Cell8.setCellValue(record1.getR198_BAL_SUB_BWP2().doubleValue());
            R198Cell8.setCellStyle(numberStyle);
        } else {
            R198Cell8.setCellValue("");
            R198Cell8.setCellStyle(textStyle);
        }
        Cell R198Cell9 = row.createCell(11);
        if (record1.getR198_BAL_ACT_SUB_BWP1() != null) {
            R198Cell9.setCellValue(record1.getR198_BAL_ACT_SUB_BWP1().doubleValue());
            R198Cell9.setCellStyle(numberStyle);
        } else {
            R198Cell9.setCellValue("");
            R198Cell9.setCellStyle(textStyle);
        }
        Cell R198Cell10 = row.createCell(12);
        if (record1.getR198_BAL_ACT_SUB_BWP2() != null) {
            R198Cell10.setCellValue(record1.getR198_BAL_ACT_SUB_BWP2().doubleValue());
            R198Cell10.setCellStyle(numberStyle);
        } else {
            R198Cell10.setCellValue("");
            R198Cell10.setCellStyle(textStyle);
        }

        /* ================= R199 ================= */
        row = sheet.getRow(198);
        Cell R199Cell1 = row.createCell(3);
        if (record1.getR199_FIG_BAL_BWP1() != null) {
            R199Cell1.setCellValue(record1.getR199_FIG_BAL_BWP1().doubleValue());
            R199Cell1.setCellStyle(numberStyle);
        } else {
            R199Cell1.setCellValue("");
            R199Cell1.setCellStyle(textStyle);
        }
        Cell R199Cell2 = row.createCell(4);
        if (record1.getR199_FIG_BAL_BWP2() != null) {
            R199Cell2.setCellValue(record1.getR199_FIG_BAL_BWP2().doubleValue());
            R199Cell2.setCellStyle(numberStyle);
        } else {
            R199Cell2.setCellValue("");
            R199Cell2.setCellStyle(textStyle);
        }
        Cell R199Cell3 = row.createCell(5);
        if (record1.getR199_AMT_ADJ_BWP1() != null) {
            R199Cell3.setCellValue(record1.getR199_AMT_ADJ_BWP1().doubleValue());
            R199Cell3.setCellStyle(numberStyle);
        } else {
            R199Cell3.setCellValue("");
            R199Cell3.setCellStyle(textStyle);
        }
        Cell R199Cell4 = row.createCell(6);
        if (record1.getR199_AMT_ADJ_BWP2() != null) {
            R199Cell4.setCellValue(record1.getR199_AMT_ADJ_BWP2().doubleValue());
            R199Cell4.setCellStyle(numberStyle);
        } else {
            R199Cell4.setCellValue("");
            R199Cell4.setCellStyle(textStyle);
        }
        Cell R199Cell5 = row.createCell(7);
        if (record1.getR199_NET_AMT_BWP1() != null) {
            R199Cell5.setCellValue(record1.getR199_NET_AMT_BWP1().doubleValue());
            R199Cell5.setCellStyle(numberStyle);
        } else {
            R199Cell5.setCellValue("");
            R199Cell5.setCellStyle(textStyle);
        }
        Cell R199Cell6 = row.createCell(8);
        if (record1.getR199_NET_AMT_BWP2() != null) {
            R199Cell6.setCellValue(record1.getR199_NET_AMT_BWP2().doubleValue());
            R199Cell6.setCellStyle(numberStyle);
        } else {
            R199Cell6.setCellValue("");
            R199Cell6.setCellStyle(textStyle);
        }
        Cell R199Cell7 = row.createCell(9);
        if (record1.getR199_BAL_SUB_BWP1() != null) {
            R199Cell7.setCellValue(record1.getR199_BAL_SUB_BWP1().doubleValue());
            R199Cell7.setCellStyle(numberStyle);
        } else {
            R199Cell7.setCellValue("");
            R199Cell7.setCellStyle(textStyle);
        }
        Cell R199Cell8 = row.createCell(10);
        if (record1.getR199_BAL_SUB_BWP2() != null) {
            R199Cell8.setCellValue(record1.getR199_BAL_SUB_BWP2().doubleValue());
            R199Cell8.setCellStyle(numberStyle);
        } else {
            R199Cell8.setCellValue("");
            R199Cell8.setCellStyle(textStyle);
        }
        Cell R199Cell9 = row.createCell(11);
        if (record1.getR199_BAL_ACT_SUB_BWP1() != null) {
            R199Cell9.setCellValue(record1.getR199_BAL_ACT_SUB_BWP1().doubleValue());
            R199Cell9.setCellStyle(numberStyle);
        } else {
            R199Cell9.setCellValue("");
            R199Cell9.setCellStyle(textStyle);
        }
        Cell R199Cell10 = row.createCell(12);
        if (record1.getR199_BAL_ACT_SUB_BWP2() != null) {
            R199Cell10.setCellValue(record1.getR199_BAL_ACT_SUB_BWP2().doubleValue());
            R199Cell10.setCellStyle(numberStyle);
        } else {
            R199Cell10.setCellValue("");
            R199Cell10.setCellStyle(textStyle);
        }

        /* ================= R200 ================= */
        row = sheet.getRow(199);
        Cell R200Cell1 = row.createCell(3);
        if (record1.getR200_FIG_BAL_BWP1() != null) {
            R200Cell1.setCellValue(record1.getR200_FIG_BAL_BWP1().doubleValue());
            R200Cell1.setCellStyle(numberStyle);
        } else {
            R200Cell1.setCellValue("");
            R200Cell1.setCellStyle(textStyle);
        }
        Cell R200Cell2 = row.createCell(4);
        if (record1.getR200_FIG_BAL_BWP2() != null) {
            R200Cell2.setCellValue(record1.getR200_FIG_BAL_BWP2().doubleValue());
            R200Cell2.setCellStyle(numberStyle);
        } else {
            R200Cell2.setCellValue("");
            R200Cell2.setCellStyle(textStyle);
        }
        Cell R200Cell3 = row.createCell(5);
        if (record1.getR200_AMT_ADJ_BWP1() != null) {
            R200Cell3.setCellValue(record1.getR200_AMT_ADJ_BWP1().doubleValue());
            R200Cell3.setCellStyle(numberStyle);
        } else {
            R200Cell3.setCellValue("");
            R200Cell3.setCellStyle(textStyle);
        }
        Cell R200Cell4 = row.createCell(6);
        if (record1.getR200_AMT_ADJ_BWP2() != null) {
            R200Cell4.setCellValue(record1.getR200_AMT_ADJ_BWP2().doubleValue());
            R200Cell4.setCellStyle(numberStyle);
        } else {
            R200Cell4.setCellValue("");
            R200Cell4.setCellStyle(textStyle);
        }
        Cell R200Cell5 = row.createCell(7);
        if (record1.getR200_NET_AMT_BWP1() != null) {
            R200Cell5.setCellValue(record1.getR200_NET_AMT_BWP1().doubleValue());
            R200Cell5.setCellStyle(numberStyle);
        } else {
            R200Cell5.setCellValue("");
            R200Cell5.setCellStyle(textStyle);
        }
        Cell R200Cell6 = row.createCell(8);
        if (record1.getR200_NET_AMT_BWP2() != null) {
            R200Cell6.setCellValue(record1.getR200_NET_AMT_BWP2().doubleValue());
            R200Cell6.setCellStyle(numberStyle);
        } else {
            R200Cell6.setCellValue("");
            R200Cell6.setCellStyle(textStyle);
        }
        Cell R200Cell7 = row.createCell(9);
        if (record1.getR200_BAL_SUB_BWP1() != null) {
            R200Cell7.setCellValue(record1.getR200_BAL_SUB_BWP1().doubleValue());
            R200Cell7.setCellStyle(numberStyle);
        } else {
            R200Cell7.setCellValue("");
            R200Cell7.setCellStyle(textStyle);
        }
        Cell R200Cell8 = row.createCell(10);
        if (record1.getR200_BAL_SUB_BWP2() != null) {
            R200Cell8.setCellValue(record1.getR200_BAL_SUB_BWP2().doubleValue());
            R200Cell8.setCellStyle(numberStyle);
        } else {
            R200Cell8.setCellValue("");
            R200Cell8.setCellStyle(textStyle);
        }
        Cell R200Cell9 = row.createCell(11);
        if (record1.getR200_BAL_ACT_SUB_BWP1() != null) {
            R200Cell9.setCellValue(record1.getR200_BAL_ACT_SUB_BWP1().doubleValue());
            R200Cell9.setCellStyle(numberStyle);
        } else {
            R200Cell9.setCellValue("");
            R200Cell9.setCellStyle(textStyle);
        }
        Cell R200Cell10 = row.createCell(12);
        if (record1.getR200_BAL_ACT_SUB_BWP2() != null) {
            R200Cell10.setCellValue(record1.getR200_BAL_ACT_SUB_BWP2().doubleValue());
            R200Cell10.setCellStyle(numberStyle);
        } else {
            R200Cell10.setCellValue("");
            R200Cell10.setCellStyle(textStyle);
        }

        /* ================= R201 ================= */
        row = sheet.getRow(200);
        Cell R201Cell1 = row.createCell(3);
        if (record1.getR201_FIG_BAL_BWP1() != null) {
            R201Cell1.setCellValue(record1.getR201_FIG_BAL_BWP1().doubleValue());
            R201Cell1.setCellStyle(numberStyle);
        } else {
            R201Cell1.setCellValue("");
            R201Cell1.setCellStyle(textStyle);
        }
        Cell R201Cell2 = row.createCell(4);
        if (record1.getR201_FIG_BAL_BWP2() != null) {
            R201Cell2.setCellValue(record1.getR201_FIG_BAL_BWP2().doubleValue());
            R201Cell2.setCellStyle(numberStyle);
        } else {
            R201Cell2.setCellValue("");
            R201Cell2.setCellStyle(textStyle);
        }
        Cell R201Cell3 = row.createCell(5);
        if (record1.getR201_AMT_ADJ_BWP1() != null) {
            R201Cell3.setCellValue(record1.getR201_AMT_ADJ_BWP1().doubleValue());
            R201Cell3.setCellStyle(numberStyle);
        } else {
            R201Cell3.setCellValue("");
            R201Cell3.setCellStyle(textStyle);
        }
        Cell R201Cell4 = row.createCell(6);
        if (record1.getR201_AMT_ADJ_BWP2() != null) {
            R201Cell4.setCellValue(record1.getR201_AMT_ADJ_BWP2().doubleValue());
            R201Cell4.setCellStyle(numberStyle);
        } else {
            R201Cell4.setCellValue("");
            R201Cell4.setCellStyle(textStyle);
        }
        Cell R201Cell5 = row.createCell(7);
        if (record1.getR201_NET_AMT_BWP1() != null) {
            R201Cell5.setCellValue(record1.getR201_NET_AMT_BWP1().doubleValue());
            R201Cell5.setCellStyle(numberStyle);
        } else {
            R201Cell5.setCellValue("");
            R201Cell5.setCellStyle(textStyle);
        }
        Cell R201Cell6 = row.createCell(8);
        if (record1.getR201_NET_AMT_BWP2() != null) {
            R201Cell6.setCellValue(record1.getR201_NET_AMT_BWP2().doubleValue());
            R201Cell6.setCellStyle(numberStyle);
        } else {
            R201Cell6.setCellValue("");
            R201Cell6.setCellStyle(textStyle);
        }
        Cell R201Cell7 = row.createCell(9);
        if (record1.getR201_BAL_SUB_BWP1() != null) {
            R201Cell7.setCellValue(record1.getR201_BAL_SUB_BWP1().doubleValue());
            R201Cell7.setCellStyle(numberStyle);
        } else {
            R201Cell7.setCellValue("");
            R201Cell7.setCellStyle(textStyle);
        }
        Cell R201Cell8 = row.createCell(10);
        if (record1.getR201_BAL_SUB_BWP2() != null) {
            R201Cell8.setCellValue(record1.getR201_BAL_SUB_BWP2().doubleValue());
            R201Cell8.setCellStyle(numberStyle);
        } else {
            R201Cell8.setCellValue("");
            R201Cell8.setCellStyle(textStyle);
        }
        Cell R201Cell9 = row.createCell(11);
        if (record1.getR201_BAL_ACT_SUB_BWP1() != null) {
            R201Cell9.setCellValue(record1.getR201_BAL_ACT_SUB_BWP1().doubleValue());
            R201Cell9.setCellStyle(numberStyle);
        } else {
            R201Cell9.setCellValue("");
            R201Cell9.setCellStyle(textStyle);
        }
        Cell R201Cell10 = row.createCell(12);
        if (record1.getR201_BAL_ACT_SUB_BWP2() != null) {
            R201Cell10.setCellValue(record1.getR201_BAL_ACT_SUB_BWP2().doubleValue());
            R201Cell10.setCellStyle(numberStyle);
        } else {
            R201Cell10.setCellValue("");
            R201Cell10.setCellStyle(textStyle);
        }

        /* ================= R202 ================= */
        row = sheet.getRow(201);
        Cell R202Cell1 = row.createCell(3);
        if (record1.getR202_FIG_BAL_BWP1() != null) {
            R202Cell1.setCellValue(record1.getR202_FIG_BAL_BWP1().doubleValue());
            R202Cell1.setCellStyle(numberStyle);
        } else {
            R202Cell1.setCellValue("");
            R202Cell1.setCellStyle(textStyle);
        }
        Cell R202Cell2 = row.createCell(4);
        if (record1.getR202_FIG_BAL_BWP2() != null) {
            R202Cell2.setCellValue(record1.getR202_FIG_BAL_BWP2().doubleValue());
            R202Cell2.setCellStyle(numberStyle);
        } else {
            R202Cell2.setCellValue("");
            R202Cell2.setCellStyle(textStyle);
        }
        Cell R202Cell3 = row.createCell(5);
        if (record1.getR202_AMT_ADJ_BWP1() != null) {
            R202Cell3.setCellValue(record1.getR202_AMT_ADJ_BWP1().doubleValue());
            R202Cell3.setCellStyle(numberStyle);
        } else {
            R202Cell3.setCellValue("");
            R202Cell3.setCellStyle(textStyle);
        }
        Cell R202Cell4 = row.createCell(6);
        if (record1.getR202_AMT_ADJ_BWP2() != null) {
            R202Cell4.setCellValue(record1.getR202_AMT_ADJ_BWP2().doubleValue());
            R202Cell4.setCellStyle(numberStyle);
        } else {
            R202Cell4.setCellValue("");
            R202Cell4.setCellStyle(textStyle);
        }
        Cell R202Cell5 = row.createCell(7);
        if (record1.getR202_NET_AMT_BWP1() != null) {
            R202Cell5.setCellValue(record1.getR202_NET_AMT_BWP1().doubleValue());
            R202Cell5.setCellStyle(numberStyle);
        } else {
            R202Cell5.setCellValue("");
            R202Cell5.setCellStyle(textStyle);
        }
        Cell R202Cell6 = row.createCell(8);
        if (record1.getR202_NET_AMT_BWP2() != null) {
            R202Cell6.setCellValue(record1.getR202_NET_AMT_BWP2().doubleValue());
            R202Cell6.setCellStyle(numberStyle);
        } else {
            R202Cell6.setCellValue("");
            R202Cell6.setCellStyle(textStyle);
        }
        Cell R202Cell7 = row.createCell(9);
        if (record1.getR202_BAL_SUB_BWP1() != null) {
            R202Cell7.setCellValue(record1.getR202_BAL_SUB_BWP1().doubleValue());
            R202Cell7.setCellStyle(numberStyle);
        } else {
            R202Cell7.setCellValue("");
            R202Cell7.setCellStyle(textStyle);
        }
        Cell R202Cell8 = row.createCell(10);
        if (record1.getR202_BAL_SUB_BWP2() != null) {
            R202Cell8.setCellValue(record1.getR202_BAL_SUB_BWP2().doubleValue());
            R202Cell8.setCellStyle(numberStyle);
        } else {
            R202Cell8.setCellValue("");
            R202Cell8.setCellStyle(textStyle);
        }
        Cell R202Cell9 = row.createCell(11);
        if (record1.getR202_BAL_ACT_SUB_BWP1() != null) {
            R202Cell9.setCellValue(record1.getR202_BAL_ACT_SUB_BWP1().doubleValue());
            R202Cell9.setCellStyle(numberStyle);
        } else {
            R202Cell9.setCellValue("");
            R202Cell9.setCellStyle(textStyle);
        }
        Cell R202Cell10 = row.createCell(12);
        if (record1.getR202_BAL_ACT_SUB_BWP2() != null) {
            R202Cell10.setCellValue(record1.getR202_BAL_ACT_SUB_BWP2().doubleValue());
            R202Cell10.setCellStyle(numberStyle);
        } else {
            R202Cell10.setCellValue("");
            R202Cell10.setCellStyle(textStyle);
        }
    }

    private void populateEntity3Data(Sheet sheet, GL_SCH_Summary_Entity3 record2, CellStyle textStyle,
            CellStyle numberStyle) {

        Row row = sheet.getRow(208) != null ? sheet.getRow(208) : sheet.createRow(208);
        /* ================= R209 ================= */
        row = sheet.getRow(208);
        Cell R209Cell1 = row.createCell(3);
        if (record2.getR209_FIG_BAL_BWP1() != null) {
            R209Cell1.setCellValue(record2.getR209_FIG_BAL_BWP1().doubleValue());
            R209Cell1.setCellStyle(numberStyle);
        } else {
            R209Cell1.setCellValue("");
            R209Cell1.setCellStyle(textStyle);
        }
        Cell R209Cell2 = row.createCell(4);
        if (record2.getR209_FIG_BAL_BWP2() != null) {
            R209Cell2.setCellValue(record2.getR209_FIG_BAL_BWP2().doubleValue());
            R209Cell2.setCellStyle(numberStyle);
        } else {
            R209Cell2.setCellValue("");
            R209Cell2.setCellStyle(textStyle);
        }
        Cell R209Cell3 = row.createCell(5);
        if (record2.getR209_AMT_ADJ_BWP1() != null) {
            R209Cell3.setCellValue(record2.getR209_AMT_ADJ_BWP1().doubleValue());
            R209Cell3.setCellStyle(numberStyle);
        } else {
            R209Cell3.setCellValue("");
            R209Cell3.setCellStyle(textStyle);
        }
        Cell R209Cell4 = row.createCell(6);
        if (record2.getR209_AMT_ADJ_BWP2() != null) {
            R209Cell4.setCellValue(record2.getR209_AMT_ADJ_BWP2().doubleValue());
            R209Cell4.setCellStyle(numberStyle);
        } else {
            R209Cell4.setCellValue("");
            R209Cell4.setCellStyle(textStyle);
        }
        Cell R209Cell5 = row.createCell(7);
        if (record2.getR209_NET_AMT_BWP1() != null) {
            R209Cell5.setCellValue(record2.getR209_NET_AMT_BWP1().doubleValue());
            R209Cell5.setCellStyle(numberStyle);
        } else {
            R209Cell5.setCellValue("");
            R209Cell5.setCellStyle(textStyle);
        }
        Cell R209Cell6 = row.createCell(8);
        if (record2.getR209_NET_AMT_BWP2() != null) {
            R209Cell6.setCellValue(record2.getR209_NET_AMT_BWP2().doubleValue());
            R209Cell6.setCellStyle(numberStyle);
        } else {
            R209Cell6.setCellValue("");
            R209Cell6.setCellStyle(textStyle);
        }
        Cell R209Cell7 = row.createCell(9);
        if (record2.getR209_BAL_SUB_BWP1() != null) {
            R209Cell7.setCellValue(record2.getR209_BAL_SUB_BWP1().doubleValue());
            R209Cell7.setCellStyle(numberStyle);
        } else {
            R209Cell7.setCellValue("");
            R209Cell7.setCellStyle(textStyle);
        }
        Cell R209Cell8 = row.createCell(10);
        if (record2.getR209_BAL_SUB_BWP2() != null) {
            R209Cell8.setCellValue(record2.getR209_BAL_SUB_BWP2().doubleValue());
            R209Cell8.setCellStyle(numberStyle);
        } else {
            R209Cell8.setCellValue("");
            R209Cell8.setCellStyle(textStyle);
        }
        Cell R209Cell9 = row.createCell(11);
        if (record2.getR209_BAL_ACT_SUB_BWP1() != null) {
            R209Cell9.setCellValue(record2.getR209_BAL_ACT_SUB_BWP1().doubleValue());
            R209Cell9.setCellStyle(numberStyle);
        } else {
            R209Cell9.setCellValue("");
            R209Cell9.setCellStyle(textStyle);
        }
        Cell R209Cell10 = row.createCell(12);
        if (record2.getR209_BAL_ACT_SUB_BWP2() != null) {
            R209Cell10.setCellValue(record2.getR209_BAL_ACT_SUB_BWP2().doubleValue());
            R209Cell10.setCellStyle(numberStyle);
        } else {
            R209Cell10.setCellValue("");
            R209Cell10.setCellStyle(textStyle);
        }

        /* ================= R210 ================= */
        row = sheet.getRow(209);
        Cell R210Cell1 = row.createCell(3);
        if (record2.getR210_FIG_BAL_BWP1() != null) {
            R210Cell1.setCellValue(record2.getR210_FIG_BAL_BWP1().doubleValue());
            R210Cell1.setCellStyle(numberStyle);
        } else {
            R210Cell1.setCellValue("");
            R210Cell1.setCellStyle(textStyle);
        }
        Cell R210Cell2 = row.createCell(4);
        if (record2.getR210_FIG_BAL_BWP2() != null) {
            R210Cell2.setCellValue(record2.getR210_FIG_BAL_BWP2().doubleValue());
            R210Cell2.setCellStyle(numberStyle);
        } else {
            R210Cell2.setCellValue("");
            R210Cell2.setCellStyle(textStyle);
        }
        Cell R210Cell3 = row.createCell(5);
        if (record2.getR210_AMT_ADJ_BWP1() != null) {
            R210Cell3.setCellValue(record2.getR210_AMT_ADJ_BWP1().doubleValue());
            R210Cell3.setCellStyle(numberStyle);
        } else {
            R210Cell3.setCellValue("");
            R210Cell3.setCellStyle(textStyle);
        }
        Cell R210Cell4 = row.createCell(6);
        if (record2.getR210_AMT_ADJ_BWP2() != null) {
            R210Cell4.setCellValue(record2.getR210_AMT_ADJ_BWP2().doubleValue());
            R210Cell4.setCellStyle(numberStyle);
        } else {
            R210Cell4.setCellValue("");
            R210Cell4.setCellStyle(textStyle);
        }
        Cell R210Cell5 = row.createCell(7);
        if (record2.getR210_NET_AMT_BWP1() != null) {
            R210Cell5.setCellValue(record2.getR210_NET_AMT_BWP1().doubleValue());
            R210Cell5.setCellStyle(numberStyle);
        } else {
            R210Cell5.setCellValue("");
            R210Cell5.setCellStyle(textStyle);
        }
        Cell R210Cell6 = row.createCell(8);
        if (record2.getR210_NET_AMT_BWP2() != null) {
            R210Cell6.setCellValue(record2.getR210_NET_AMT_BWP2().doubleValue());
            R210Cell6.setCellStyle(numberStyle);
        } else {
            R210Cell6.setCellValue("");
            R210Cell6.setCellStyle(textStyle);
        }
        Cell R210Cell7 = row.createCell(9);
        if (record2.getR210_BAL_SUB_BWP1() != null) {
            R210Cell7.setCellValue(record2.getR210_BAL_SUB_BWP1().doubleValue());
            R210Cell7.setCellStyle(numberStyle);
        } else {
            R210Cell7.setCellValue("");
            R210Cell7.setCellStyle(textStyle);
        }
        Cell R210Cell8 = row.createCell(10);
        if (record2.getR210_BAL_SUB_BWP2() != null) {
            R210Cell8.setCellValue(record2.getR210_BAL_SUB_BWP2().doubleValue());
            R210Cell8.setCellStyle(numberStyle);
        } else {
            R210Cell8.setCellValue("");
            R210Cell8.setCellStyle(textStyle);
        }
        Cell R210Cell9 = row.createCell(11);
        if (record2.getR210_BAL_ACT_SUB_BWP1() != null) {
            R210Cell9.setCellValue(record2.getR210_BAL_ACT_SUB_BWP1().doubleValue());
            R210Cell9.setCellStyle(numberStyle);
        } else {
            R210Cell9.setCellValue("");
            R210Cell9.setCellStyle(textStyle);
        }
        Cell R210Cell10 = row.createCell(12);
        if (record2.getR210_BAL_ACT_SUB_BWP2() != null) {
            R210Cell10.setCellValue(record2.getR210_BAL_ACT_SUB_BWP2().doubleValue());
            R210Cell10.setCellStyle(numberStyle);
        } else {
            R210Cell10.setCellValue("");
            R210Cell10.setCellStyle(textStyle);
        }

        /* ================= R211 ================= */
        row = sheet.getRow(210);
        Cell R211Cell1 = row.createCell(3);
        if (record2.getR211_FIG_BAL_BWP1() != null) {
            R211Cell1.setCellValue(record2.getR211_FIG_BAL_BWP1().doubleValue());
            R211Cell1.setCellStyle(numberStyle);
        } else {
            R211Cell1.setCellValue("");
            R211Cell1.setCellStyle(textStyle);
        }
        Cell R211Cell2 = row.createCell(4);
        if (record2.getR211_FIG_BAL_BWP2() != null) {
            R211Cell2.setCellValue(record2.getR211_FIG_BAL_BWP2().doubleValue());
            R211Cell2.setCellStyle(numberStyle);
        } else {
            R211Cell2.setCellValue("");
            R211Cell2.setCellStyle(textStyle);
        }
        Cell R211Cell3 = row.createCell(5);
        if (record2.getR211_AMT_ADJ_BWP1() != null) {
            R211Cell3.setCellValue(record2.getR211_AMT_ADJ_BWP1().doubleValue());
            R211Cell3.setCellStyle(numberStyle);
        } else {
            R211Cell3.setCellValue("");
            R211Cell3.setCellStyle(textStyle);
        }
        Cell R211Cell4 = row.createCell(6);
        if (record2.getR211_AMT_ADJ_BWP2() != null) {
            R211Cell4.setCellValue(record2.getR211_AMT_ADJ_BWP2().doubleValue());
            R211Cell4.setCellStyle(numberStyle);
        } else {
            R211Cell4.setCellValue("");
            R211Cell4.setCellStyle(textStyle);
        }
        Cell R211Cell5 = row.createCell(7);
        if (record2.getR211_NET_AMT_BWP1() != null) {
            R211Cell5.setCellValue(record2.getR211_NET_AMT_BWP1().doubleValue());
            R211Cell5.setCellStyle(numberStyle);
        } else {
            R211Cell5.setCellValue("");
            R211Cell5.setCellStyle(textStyle);
        }
        Cell R211Cell6 = row.createCell(8);
        if (record2.getR211_NET_AMT_BWP2() != null) {
            R211Cell6.setCellValue(record2.getR211_NET_AMT_BWP2().doubleValue());
            R211Cell6.setCellStyle(numberStyle);
        } else {
            R211Cell6.setCellValue("");
            R211Cell6.setCellStyle(textStyle);
        }
        Cell R211Cell7 = row.createCell(9);
        if (record2.getR211_BAL_SUB_BWP1() != null) {
            R211Cell7.setCellValue(record2.getR211_BAL_SUB_BWP1().doubleValue());
            R211Cell7.setCellStyle(numberStyle);
        } else {
            R211Cell7.setCellValue("");
            R211Cell7.setCellStyle(textStyle);
        }
        Cell R211Cell8 = row.createCell(10);
        if (record2.getR211_BAL_SUB_BWP2() != null) {
            R211Cell8.setCellValue(record2.getR211_BAL_SUB_BWP2().doubleValue());
            R211Cell8.setCellStyle(numberStyle);
        } else {
            R211Cell8.setCellValue("");
            R211Cell8.setCellStyle(textStyle);
        }
        Cell R211Cell9 = row.createCell(11);
        if (record2.getR211_BAL_ACT_SUB_BWP1() != null) {
            R211Cell9.setCellValue(record2.getR211_BAL_ACT_SUB_BWP1().doubleValue());
            R211Cell9.setCellStyle(numberStyle);
        } else {
            R211Cell9.setCellValue("");
            R211Cell9.setCellStyle(textStyle);
        }
        Cell R211Cell10 = row.createCell(12);
        if (record2.getR211_BAL_ACT_SUB_BWP2() != null) {
            R211Cell10.setCellValue(record2.getR211_BAL_ACT_SUB_BWP2().doubleValue());
            R211Cell10.setCellStyle(numberStyle);
        } else {
            R211Cell10.setCellValue("");
            R211Cell10.setCellStyle(textStyle);
        }

        /* ================= R212 ================= */
        row = sheet.getRow(211);
        Cell R212Cell1 = row.createCell(3);
        if (record2.getR212_FIG_BAL_BWP1() != null) {
            R212Cell1.setCellValue(record2.getR212_FIG_BAL_BWP1().doubleValue());
            R212Cell1.setCellStyle(numberStyle);
        } else {
            R212Cell1.setCellValue("");
            R212Cell1.setCellStyle(textStyle);
        }
        Cell R212Cell2 = row.createCell(4);
        if (record2.getR212_FIG_BAL_BWP2() != null) {
            R212Cell2.setCellValue(record2.getR212_FIG_BAL_BWP2().doubleValue());
            R212Cell2.setCellStyle(numberStyle);
        } else {
            R212Cell2.setCellValue("");
            R212Cell2.setCellStyle(textStyle);
        }
        Cell R212Cell3 = row.createCell(5);
        if (record2.getR212_AMT_ADJ_BWP1() != null) {
            R212Cell3.setCellValue(record2.getR212_AMT_ADJ_BWP1().doubleValue());
            R212Cell3.setCellStyle(numberStyle);
        } else {
            R212Cell3.setCellValue("");
            R212Cell3.setCellStyle(textStyle);
        }
        Cell R212Cell4 = row.createCell(6);
        if (record2.getR212_AMT_ADJ_BWP2() != null) {
            R212Cell4.setCellValue(record2.getR212_AMT_ADJ_BWP2().doubleValue());
            R212Cell4.setCellStyle(numberStyle);
        } else {
            R212Cell4.setCellValue("");
            R212Cell4.setCellStyle(textStyle);
        }
        Cell R212Cell5 = row.createCell(7);
        if (record2.getR212_NET_AMT_BWP1() != null) {
            R212Cell5.setCellValue(record2.getR212_NET_AMT_BWP1().doubleValue());
            R212Cell5.setCellStyle(numberStyle);
        } else {
            R212Cell5.setCellValue("");
            R212Cell5.setCellStyle(textStyle);
        }
        Cell R212Cell6 = row.createCell(8);
        if (record2.getR212_NET_AMT_BWP2() != null) {
            R212Cell6.setCellValue(record2.getR212_NET_AMT_BWP2().doubleValue());
            R212Cell6.setCellStyle(numberStyle);
        } else {
            R212Cell6.setCellValue("");
            R212Cell6.setCellStyle(textStyle);
        }
        Cell R212Cell7 = row.createCell(9);
        if (record2.getR212_BAL_SUB_BWP1() != null) {
            R212Cell7.setCellValue(record2.getR212_BAL_SUB_BWP1().doubleValue());
            R212Cell7.setCellStyle(numberStyle);
        } else {
            R212Cell7.setCellValue("");
            R212Cell7.setCellStyle(textStyle);
        }
        Cell R212Cell8 = row.createCell(10);
        if (record2.getR212_BAL_SUB_BWP2() != null) {
            R212Cell8.setCellValue(record2.getR212_BAL_SUB_BWP2().doubleValue());
            R212Cell8.setCellStyle(numberStyle);
        } else {
            R212Cell8.setCellValue("");
            R212Cell8.setCellStyle(textStyle);
        }
        Cell R212Cell9 = row.createCell(11);
        if (record2.getR212_BAL_ACT_SUB_BWP1() != null) {
            R212Cell9.setCellValue(record2.getR212_BAL_ACT_SUB_BWP1().doubleValue());
            R212Cell9.setCellStyle(numberStyle);
        } else {
            R212Cell9.setCellValue("");
            R212Cell9.setCellStyle(textStyle);
        }
        Cell R212Cell10 = row.createCell(12);
        if (record2.getR212_BAL_ACT_SUB_BWP2() != null) {
            R212Cell10.setCellValue(record2.getR212_BAL_ACT_SUB_BWP2().doubleValue());
            R212Cell10.setCellStyle(numberStyle);
        } else {
            R212Cell10.setCellValue("");
            R212Cell10.setCellStyle(textStyle);
        }

        /* ================= R213 ================= */
        row = sheet.getRow(212);
        Cell R213Cell1 = row.createCell(3);
        if (record2.getR213_FIG_BAL_BWP1() != null) {
            R213Cell1.setCellValue(record2.getR213_FIG_BAL_BWP1().doubleValue());
            R213Cell1.setCellStyle(numberStyle);
        } else {
            R213Cell1.setCellValue("");
            R213Cell1.setCellStyle(textStyle);
        }
        Cell R213Cell2 = row.createCell(4);
        if (record2.getR213_FIG_BAL_BWP2() != null) {
            R213Cell2.setCellValue(record2.getR213_FIG_BAL_BWP2().doubleValue());
            R213Cell2.setCellStyle(numberStyle);
        } else {
            R213Cell2.setCellValue("");
            R213Cell2.setCellStyle(textStyle);
        }
        Cell R213Cell3 = row.createCell(5);
        if (record2.getR213_AMT_ADJ_BWP1() != null) {
            R213Cell3.setCellValue(record2.getR213_AMT_ADJ_BWP1().doubleValue());
            R213Cell3.setCellStyle(numberStyle);
        } else {
            R213Cell3.setCellValue("");
            R213Cell3.setCellStyle(textStyle);
        }
        Cell R213Cell4 = row.createCell(6);
        if (record2.getR213_AMT_ADJ_BWP2() != null) {
            R213Cell4.setCellValue(record2.getR213_AMT_ADJ_BWP2().doubleValue());
            R213Cell4.setCellStyle(numberStyle);
        } else {
            R213Cell4.setCellValue("");
            R213Cell4.setCellStyle(textStyle);
        }
        Cell R213Cell5 = row.createCell(7);
        if (record2.getR213_NET_AMT_BWP1() != null) {
            R213Cell5.setCellValue(record2.getR213_NET_AMT_BWP1().doubleValue());
            R213Cell5.setCellStyle(numberStyle);
        } else {
            R213Cell5.setCellValue("");
            R213Cell5.setCellStyle(textStyle);
        }
        Cell R213Cell6 = row.createCell(8);
        if (record2.getR213_NET_AMT_BWP2() != null) {
            R213Cell6.setCellValue(record2.getR213_NET_AMT_BWP2().doubleValue());
            R213Cell6.setCellStyle(numberStyle);
        } else {
            R213Cell6.setCellValue("");
            R213Cell6.setCellStyle(textStyle);
        }
        Cell R213Cell7 = row.createCell(9);
        if (record2.getR213_BAL_SUB_BWP1() != null) {
            R213Cell7.setCellValue(record2.getR213_BAL_SUB_BWP1().doubleValue());
            R213Cell7.setCellStyle(numberStyle);
        } else {
            R213Cell7.setCellValue("");
            R213Cell7.setCellStyle(textStyle);
        }
        Cell R213Cell8 = row.createCell(10);
        if (record2.getR213_BAL_SUB_BWP2() != null) {
            R213Cell8.setCellValue(record2.getR213_BAL_SUB_BWP2().doubleValue());
            R213Cell8.setCellStyle(numberStyle);
        } else {
            R213Cell8.setCellValue("");
            R213Cell8.setCellStyle(textStyle);
        }
        Cell R213Cell9 = row.createCell(11);
        if (record2.getR213_BAL_ACT_SUB_BWP1() != null) {
            R213Cell9.setCellValue(record2.getR213_BAL_ACT_SUB_BWP1().doubleValue());
            R213Cell9.setCellStyle(numberStyle);
        } else {
            R213Cell9.setCellValue("");
            R213Cell9.setCellStyle(textStyle);
        }
        Cell R213Cell10 = row.createCell(12);
        if (record2.getR213_BAL_ACT_SUB_BWP2() != null) {
            R213Cell10.setCellValue(record2.getR213_BAL_ACT_SUB_BWP2().doubleValue());
            R213Cell10.setCellStyle(numberStyle);
        } else {
            R213Cell10.setCellValue("");
            R213Cell10.setCellStyle(textStyle);
        }

        /* ================= R214 ================= */
        row = sheet.getRow(213);
        Cell R214Cell1 = row.createCell(3);
        if (record2.getR214_FIG_BAL_BWP1() != null) {
            R214Cell1.setCellValue(record2.getR214_FIG_BAL_BWP1().doubleValue());
            R214Cell1.setCellStyle(numberStyle);
        } else {
            R214Cell1.setCellValue("");
            R214Cell1.setCellStyle(textStyle);
        }
        Cell R214Cell2 = row.createCell(4);
        if (record2.getR214_FIG_BAL_BWP2() != null) {
            R214Cell2.setCellValue(record2.getR214_FIG_BAL_BWP2().doubleValue());
            R214Cell2.setCellStyle(numberStyle);
        } else {
            R214Cell2.setCellValue("");
            R214Cell2.setCellStyle(textStyle);
        }
        Cell R214Cell3 = row.createCell(5);
        if (record2.getR214_AMT_ADJ_BWP1() != null) {
            R214Cell3.setCellValue(record2.getR214_AMT_ADJ_BWP1().doubleValue());
            R214Cell3.setCellStyle(numberStyle);
        } else {
            R214Cell3.setCellValue("");
            R214Cell3.setCellStyle(textStyle);
        }
        Cell R214Cell4 = row.createCell(6);
        if (record2.getR214_AMT_ADJ_BWP2() != null) {
            R214Cell4.setCellValue(record2.getR214_AMT_ADJ_BWP2().doubleValue());
            R214Cell4.setCellStyle(numberStyle);
        } else {
            R214Cell4.setCellValue("");
            R214Cell4.setCellStyle(textStyle);
        }
        Cell R214Cell5 = row.createCell(7);
        if (record2.getR214_NET_AMT_BWP1() != null) {
            R214Cell5.setCellValue(record2.getR214_NET_AMT_BWP1().doubleValue());
            R214Cell5.setCellStyle(numberStyle);
        } else {
            R214Cell5.setCellValue("");
            R214Cell5.setCellStyle(textStyle);
        }
        Cell R214Cell6 = row.createCell(8);
        if (record2.getR214_NET_AMT_BWP2() != null) {
            R214Cell6.setCellValue(record2.getR214_NET_AMT_BWP2().doubleValue());
            R214Cell6.setCellStyle(numberStyle);
        } else {
            R214Cell6.setCellValue("");
            R214Cell6.setCellStyle(textStyle);
        }
        Cell R214Cell7 = row.createCell(9);
        if (record2.getR214_BAL_SUB_BWP1() != null) {
            R214Cell7.setCellValue(record2.getR214_BAL_SUB_BWP1().doubleValue());
            R214Cell7.setCellStyle(numberStyle);
        } else {
            R214Cell7.setCellValue("");
            R214Cell7.setCellStyle(textStyle);
        }
        Cell R214Cell8 = row.createCell(10);
        if (record2.getR214_BAL_SUB_BWP2() != null) {
            R214Cell8.setCellValue(record2.getR214_BAL_SUB_BWP2().doubleValue());
            R214Cell8.setCellStyle(numberStyle);
        } else {
            R214Cell8.setCellValue("");
            R214Cell8.setCellStyle(textStyle);
        }
        Cell R214Cell9 = row.createCell(11);
        if (record2.getR214_BAL_ACT_SUB_BWP1() != null) {
            R214Cell9.setCellValue(record2.getR214_BAL_ACT_SUB_BWP1().doubleValue());
            R214Cell9.setCellStyle(numberStyle);
        } else {
            R214Cell9.setCellValue("");
            R214Cell9.setCellStyle(textStyle);
        }
        Cell R214Cell10 = row.createCell(12);
        if (record2.getR214_BAL_ACT_SUB_BWP2() != null) {
            R214Cell10.setCellValue(record2.getR214_BAL_ACT_SUB_BWP2().doubleValue());
            R214Cell10.setCellStyle(numberStyle);
        } else {
            R214Cell10.setCellValue("");
            R214Cell10.setCellStyle(textStyle);
        }

        /* ================= R215 ================= */
        row = sheet.getRow(214);
        Cell R215Cell1 = row.createCell(3);
        if (record2.getR215_FIG_BAL_BWP1() != null) {
            R215Cell1.setCellValue(record2.getR215_FIG_BAL_BWP1().doubleValue());
            R215Cell1.setCellStyle(numberStyle);
        } else {
            R215Cell1.setCellValue("");
            R215Cell1.setCellStyle(textStyle);
        }
        Cell R215Cell2 = row.createCell(4);
        if (record2.getR215_FIG_BAL_BWP2() != null) {
            R215Cell2.setCellValue(record2.getR215_FIG_BAL_BWP2().doubleValue());
            R215Cell2.setCellStyle(numberStyle);
        } else {
            R215Cell2.setCellValue("");
            R215Cell2.setCellStyle(textStyle);
        }
        Cell R215Cell3 = row.createCell(5);
        if (record2.getR215_AMT_ADJ_BWP1() != null) {
            R215Cell3.setCellValue(record2.getR215_AMT_ADJ_BWP1().doubleValue());
            R215Cell3.setCellStyle(numberStyle);
        } else {
            R215Cell3.setCellValue("");
            R215Cell3.setCellStyle(textStyle);
        }
        Cell R215Cell4 = row.createCell(6);
        if (record2.getR215_AMT_ADJ_BWP2() != null) {
            R215Cell4.setCellValue(record2.getR215_AMT_ADJ_BWP2().doubleValue());
            R215Cell4.setCellStyle(numberStyle);
        } else {
            R215Cell4.setCellValue("");
            R215Cell4.setCellStyle(textStyle);
        }
        Cell R215Cell5 = row.createCell(7);
        if (record2.getR215_NET_AMT_BWP1() != null) {
            R215Cell5.setCellValue(record2.getR215_NET_AMT_BWP1().doubleValue());
            R215Cell5.setCellStyle(numberStyle);
        } else {
            R215Cell5.setCellValue("");
            R215Cell5.setCellStyle(textStyle);
        }
        Cell R215Cell6 = row.createCell(8);
        if (record2.getR215_NET_AMT_BWP2() != null) {
            R215Cell6.setCellValue(record2.getR215_NET_AMT_BWP2().doubleValue());
            R215Cell6.setCellStyle(numberStyle);
        } else {
            R215Cell6.setCellValue("");
            R215Cell6.setCellStyle(textStyle);
        }
        Cell R215Cell7 = row.createCell(9);
        if (record2.getR215_BAL_SUB_BWP1() != null) {
            R215Cell7.setCellValue(record2.getR215_BAL_SUB_BWP1().doubleValue());
            R215Cell7.setCellStyle(numberStyle);
        } else {
            R215Cell7.setCellValue("");
            R215Cell7.setCellStyle(textStyle);
        }
        Cell R215Cell8 = row.createCell(10);
        if (record2.getR215_BAL_SUB_BWP2() != null) {
            R215Cell8.setCellValue(record2.getR215_BAL_SUB_BWP2().doubleValue());
            R215Cell8.setCellStyle(numberStyle);
        } else {
            R215Cell8.setCellValue("");
            R215Cell8.setCellStyle(textStyle);
        }
        Cell R215Cell9 = row.createCell(11);
        if (record2.getR215_BAL_ACT_SUB_BWP1() != null) {
            R215Cell9.setCellValue(record2.getR215_BAL_ACT_SUB_BWP1().doubleValue());
            R215Cell9.setCellStyle(numberStyle);
        } else {
            R215Cell9.setCellValue("");
            R215Cell9.setCellStyle(textStyle);
        }
        Cell R215Cell10 = row.createCell(12);
        if (record2.getR215_BAL_ACT_SUB_BWP2() != null) {
            R215Cell10.setCellValue(record2.getR215_BAL_ACT_SUB_BWP2().doubleValue());
            R215Cell10.setCellStyle(numberStyle);
        } else {
            R215Cell10.setCellValue("");
            R215Cell10.setCellStyle(textStyle);
        }

        /* ================= R216 ================= */
        row = sheet.getRow(215);
        Cell R216Cell1 = row.createCell(3);
        if (record2.getR216_FIG_BAL_BWP1() != null) {
            R216Cell1.setCellValue(record2.getR216_FIG_BAL_BWP1().doubleValue());
            R216Cell1.setCellStyle(numberStyle);
        } else {
            R216Cell1.setCellValue("");
            R216Cell1.setCellStyle(textStyle);
        }
        Cell R216Cell2 = row.createCell(4);
        if (record2.getR216_FIG_BAL_BWP2() != null) {
            R216Cell2.setCellValue(record2.getR216_FIG_BAL_BWP2().doubleValue());
            R216Cell2.setCellStyle(numberStyle);
        } else {
            R216Cell2.setCellValue("");
            R216Cell2.setCellStyle(textStyle);
        }
        Cell R216Cell3 = row.createCell(5);
        if (record2.getR216_AMT_ADJ_BWP1() != null) {
            R216Cell3.setCellValue(record2.getR216_AMT_ADJ_BWP1().doubleValue());
            R216Cell3.setCellStyle(numberStyle);
        } else {
            R216Cell3.setCellValue("");
            R216Cell3.setCellStyle(textStyle);
        }
        Cell R216Cell4 = row.createCell(6);
        if (record2.getR216_AMT_ADJ_BWP2() != null) {
            R216Cell4.setCellValue(record2.getR216_AMT_ADJ_BWP2().doubleValue());
            R216Cell4.setCellStyle(numberStyle);
        } else {
            R216Cell4.setCellValue("");
            R216Cell4.setCellStyle(textStyle);
        }
        Cell R216Cell5 = row.createCell(7);
        if (record2.getR216_NET_AMT_BWP1() != null) {
            R216Cell5.setCellValue(record2.getR216_NET_AMT_BWP1().doubleValue());
            R216Cell5.setCellStyle(numberStyle);
        } else {
            R216Cell5.setCellValue("");
            R216Cell5.setCellStyle(textStyle);
        }
        Cell R216Cell6 = row.createCell(8);
        if (record2.getR216_NET_AMT_BWP2() != null) {
            R216Cell6.setCellValue(record2.getR216_NET_AMT_BWP2().doubleValue());
            R216Cell6.setCellStyle(numberStyle);
        } else {
            R216Cell6.setCellValue("");
            R216Cell6.setCellStyle(textStyle);
        }
        Cell R216Cell7 = row.createCell(9);
        if (record2.getR216_BAL_SUB_BWP1() != null) {
            R216Cell7.setCellValue(record2.getR216_BAL_SUB_BWP1().doubleValue());
            R216Cell7.setCellStyle(numberStyle);
        } else {
            R216Cell7.setCellValue("");
            R216Cell7.setCellStyle(textStyle);
        }
        Cell R216Cell8 = row.createCell(10);
        if (record2.getR216_BAL_SUB_BWP2() != null) {
            R216Cell8.setCellValue(record2.getR216_BAL_SUB_BWP2().doubleValue());
            R216Cell8.setCellStyle(numberStyle);
        } else {
            R216Cell8.setCellValue("");
            R216Cell8.setCellStyle(textStyle);
        }
        Cell R216Cell9 = row.createCell(11);
        if (record2.getR216_BAL_ACT_SUB_BWP1() != null) {
            R216Cell9.setCellValue(record2.getR216_BAL_ACT_SUB_BWP1().doubleValue());
            R216Cell9.setCellStyle(numberStyle);
        } else {
            R216Cell9.setCellValue("");
            R216Cell9.setCellStyle(textStyle);
        }
        Cell R216Cell10 = row.createCell(12);
        if (record2.getR216_BAL_ACT_SUB_BWP2() != null) {
            R216Cell10.setCellValue(record2.getR216_BAL_ACT_SUB_BWP2().doubleValue());
            R216Cell10.setCellStyle(numberStyle);
        } else {
            R216Cell10.setCellValue("");
            R216Cell10.setCellStyle(textStyle);
        }

        /* ================= R217 ================= */
        row = sheet.getRow(216);
        Cell R217Cell1 = row.createCell(3);
        if (record2.getR217_FIG_BAL_BWP1() != null) {
            R217Cell1.setCellValue(record2.getR217_FIG_BAL_BWP1().doubleValue());
            R217Cell1.setCellStyle(numberStyle);
        } else {
            R217Cell1.setCellValue("");
            R217Cell1.setCellStyle(textStyle);
        }
        Cell R217Cell2 = row.createCell(4);
        if (record2.getR217_FIG_BAL_BWP2() != null) {
            R217Cell2.setCellValue(record2.getR217_FIG_BAL_BWP2().doubleValue());
            R217Cell2.setCellStyle(numberStyle);
        } else {
            R217Cell2.setCellValue("");
            R217Cell2.setCellStyle(textStyle);
        }
        Cell R217Cell3 = row.createCell(5);
        if (record2.getR217_AMT_ADJ_BWP1() != null) {
            R217Cell3.setCellValue(record2.getR217_AMT_ADJ_BWP1().doubleValue());
            R217Cell3.setCellStyle(numberStyle);
        } else {
            R217Cell3.setCellValue("");
            R217Cell3.setCellStyle(textStyle);
        }
        Cell R217Cell4 = row.createCell(6);
        if (record2.getR217_AMT_ADJ_BWP2() != null) {
            R217Cell4.setCellValue(record2.getR217_AMT_ADJ_BWP2().doubleValue());
            R217Cell4.setCellStyle(numberStyle);
        } else {
            R217Cell4.setCellValue("");
            R217Cell4.setCellStyle(textStyle);
        }
        Cell R217Cell5 = row.createCell(7);
        if (record2.getR217_NET_AMT_BWP1() != null) {
            R217Cell5.setCellValue(record2.getR217_NET_AMT_BWP1().doubleValue());
            R217Cell5.setCellStyle(numberStyle);
        } else {
            R217Cell5.setCellValue("");
            R217Cell5.setCellStyle(textStyle);
        }
        Cell R217Cell6 = row.createCell(8);
        if (record2.getR217_NET_AMT_BWP2() != null) {
            R217Cell6.setCellValue(record2.getR217_NET_AMT_BWP2().doubleValue());
            R217Cell6.setCellStyle(numberStyle);
        } else {
            R217Cell6.setCellValue("");
            R217Cell6.setCellStyle(textStyle);
        }
        Cell R217Cell7 = row.createCell(9);
        if (record2.getR217_BAL_SUB_BWP1() != null) {
            R217Cell7.setCellValue(record2.getR217_BAL_SUB_BWP1().doubleValue());
            R217Cell7.setCellStyle(numberStyle);
        } else {
            R217Cell7.setCellValue("");
            R217Cell7.setCellStyle(textStyle);
        }
        Cell R217Cell8 = row.createCell(10);
        if (record2.getR217_BAL_SUB_BWP2() != null) {
            R217Cell8.setCellValue(record2.getR217_BAL_SUB_BWP2().doubleValue());
            R217Cell8.setCellStyle(numberStyle);
        } else {
            R217Cell8.setCellValue("");
            R217Cell8.setCellStyle(textStyle);
        }
        Cell R217Cell9 = row.createCell(11);
        if (record2.getR217_BAL_ACT_SUB_BWP1() != null) {
            R217Cell9.setCellValue(record2.getR217_BAL_ACT_SUB_BWP1().doubleValue());
            R217Cell9.setCellStyle(numberStyle);
        } else {
            R217Cell9.setCellValue("");
            R217Cell9.setCellStyle(textStyle);
        }
        Cell R217Cell10 = row.createCell(12);
        if (record2.getR217_BAL_ACT_SUB_BWP2() != null) {
            R217Cell10.setCellValue(record2.getR217_BAL_ACT_SUB_BWP2().doubleValue());
            R217Cell10.setCellStyle(numberStyle);
        } else {
            R217Cell10.setCellValue("");
            R217Cell10.setCellStyle(textStyle);
        }

        /* ================= R218 ================= */
        row = sheet.getRow(217);
        Cell R218Cell1 = row.createCell(3);
        if (record2.getR218_FIG_BAL_BWP1() != null) {
            R218Cell1.setCellValue(record2.getR218_FIG_BAL_BWP1().doubleValue());
            R218Cell1.setCellStyle(numberStyle);
        } else {
            R218Cell1.setCellValue("");
            R218Cell1.setCellStyle(textStyle);
        }
        Cell R218Cell2 = row.createCell(4);
        if (record2.getR218_FIG_BAL_BWP2() != null) {
            R218Cell2.setCellValue(record2.getR218_FIG_BAL_BWP2().doubleValue());
            R218Cell2.setCellStyle(numberStyle);
        } else {
            R218Cell2.setCellValue("");
            R218Cell2.setCellStyle(textStyle);
        }
        Cell R218Cell3 = row.createCell(5);
        if (record2.getR218_AMT_ADJ_BWP1() != null) {
            R218Cell3.setCellValue(record2.getR218_AMT_ADJ_BWP1().doubleValue());
            R218Cell3.setCellStyle(numberStyle);
        } else {
            R218Cell3.setCellValue("");
            R218Cell3.setCellStyle(textStyle);
        }
        Cell R218Cell4 = row.createCell(6);
        if (record2.getR218_AMT_ADJ_BWP2() != null) {
            R218Cell4.setCellValue(record2.getR218_AMT_ADJ_BWP2().doubleValue());
            R218Cell4.setCellStyle(numberStyle);
        } else {
            R218Cell4.setCellValue("");
            R218Cell4.setCellStyle(textStyle);
        }
        Cell R218Cell5 = row.createCell(7);
        if (record2.getR218_NET_AMT_BWP1() != null) {
            R218Cell5.setCellValue(record2.getR218_NET_AMT_BWP1().doubleValue());
            R218Cell5.setCellStyle(numberStyle);
        } else {
            R218Cell5.setCellValue("");
            R218Cell5.setCellStyle(textStyle);
        }
        Cell R218Cell6 = row.createCell(8);
        if (record2.getR218_NET_AMT_BWP2() != null) {
            R218Cell6.setCellValue(record2.getR218_NET_AMT_BWP2().doubleValue());
            R218Cell6.setCellStyle(numberStyle);
        } else {
            R218Cell6.setCellValue("");
            R218Cell6.setCellStyle(textStyle);
        }
        Cell R218Cell7 = row.createCell(9);
        if (record2.getR218_BAL_SUB_BWP1() != null) {
            R218Cell7.setCellValue(record2.getR218_BAL_SUB_BWP1().doubleValue());
            R218Cell7.setCellStyle(numberStyle);
        } else {
            R218Cell7.setCellValue("");
            R218Cell7.setCellStyle(textStyle);
        }
        Cell R218Cell8 = row.createCell(10);
        if (record2.getR218_BAL_SUB_BWP2() != null) {
            R218Cell8.setCellValue(record2.getR218_BAL_SUB_BWP2().doubleValue());
            R218Cell8.setCellStyle(numberStyle);
        } else {
            R218Cell8.setCellValue("");
            R218Cell8.setCellStyle(textStyle);
        }
        Cell R218Cell9 = row.createCell(11);
        if (record2.getR218_BAL_ACT_SUB_BWP1() != null) {
            R218Cell9.setCellValue(record2.getR218_BAL_ACT_SUB_BWP1().doubleValue());
            R218Cell9.setCellStyle(numberStyle);
        } else {
            R218Cell9.setCellValue("");
            R218Cell9.setCellStyle(textStyle);
        }
        Cell R218Cell10 = row.createCell(12);
        if (record2.getR218_BAL_ACT_SUB_BWP2() != null) {
            R218Cell10.setCellValue(record2.getR218_BAL_ACT_SUB_BWP2().doubleValue());
            R218Cell10.setCellStyle(numberStyle);
        } else {
            R218Cell10.setCellValue("");
            R218Cell10.setCellStyle(textStyle);
        }

        /* ================= R219 ================= */
        row = sheet.getRow(218);
        Cell R219Cell1 = row.createCell(3);
        if (record2.getR219_FIG_BAL_BWP1() != null) {
            R219Cell1.setCellValue(record2.getR219_FIG_BAL_BWP1().doubleValue());
            R219Cell1.setCellStyle(numberStyle);
        } else {
            R219Cell1.setCellValue("");
            R219Cell1.setCellStyle(textStyle);
        }
        Cell R219Cell2 = row.createCell(4);
        if (record2.getR219_FIG_BAL_BWP2() != null) {
            R219Cell2.setCellValue(record2.getR219_FIG_BAL_BWP2().doubleValue());
            R219Cell2.setCellStyle(numberStyle);
        } else {
            R219Cell2.setCellValue("");
            R219Cell2.setCellStyle(textStyle);
        }
        Cell R219Cell3 = row.createCell(5);
        if (record2.getR219_AMT_ADJ_BWP1() != null) {
            R219Cell3.setCellValue(record2.getR219_AMT_ADJ_BWP1().doubleValue());
            R219Cell3.setCellStyle(numberStyle);
        } else {
            R219Cell3.setCellValue("");
            R219Cell3.setCellStyle(textStyle);
        }
        Cell R219Cell4 = row.createCell(6);
        if (record2.getR219_AMT_ADJ_BWP2() != null) {
            R219Cell4.setCellValue(record2.getR219_AMT_ADJ_BWP2().doubleValue());
            R219Cell4.setCellStyle(numberStyle);
        } else {
            R219Cell4.setCellValue("");
            R219Cell4.setCellStyle(textStyle);
        }
        Cell R219Cell5 = row.createCell(7);
        if (record2.getR219_NET_AMT_BWP1() != null) {
            R219Cell5.setCellValue(record2.getR219_NET_AMT_BWP1().doubleValue());
            R219Cell5.setCellStyle(numberStyle);
        } else {
            R219Cell5.setCellValue("");
            R219Cell5.setCellStyle(textStyle);
        }
        Cell R219Cell6 = row.createCell(8);
        if (record2.getR219_NET_AMT_BWP2() != null) {
            R219Cell6.setCellValue(record2.getR219_NET_AMT_BWP2().doubleValue());
            R219Cell6.setCellStyle(numberStyle);
        } else {
            R219Cell6.setCellValue("");
            R219Cell6.setCellStyle(textStyle);
        }
        Cell R219Cell7 = row.createCell(9);
        if (record2.getR219_BAL_SUB_BWP1() != null) {
            R219Cell7.setCellValue(record2.getR219_BAL_SUB_BWP1().doubleValue());
            R219Cell7.setCellStyle(numberStyle);
        } else {
            R219Cell7.setCellValue("");
            R219Cell7.setCellStyle(textStyle);
        }
        Cell R219Cell8 = row.createCell(10);
        if (record2.getR219_BAL_SUB_BWP2() != null) {
            R219Cell8.setCellValue(record2.getR219_BAL_SUB_BWP2().doubleValue());
            R219Cell8.setCellStyle(numberStyle);
        } else {
            R219Cell8.setCellValue("");
            R219Cell8.setCellStyle(textStyle);
        }
        Cell R219Cell9 = row.createCell(11);
        if (record2.getR219_BAL_ACT_SUB_BWP1() != null) {
            R219Cell9.setCellValue(record2.getR219_BAL_ACT_SUB_BWP1().doubleValue());
            R219Cell9.setCellStyle(numberStyle);
        } else {
            R219Cell9.setCellValue("");
            R219Cell9.setCellStyle(textStyle);
        }
        Cell R219Cell10 = row.createCell(12);
        if (record2.getR219_BAL_ACT_SUB_BWP2() != null) {
            R219Cell10.setCellValue(record2.getR219_BAL_ACT_SUB_BWP2().doubleValue());
            R219Cell10.setCellStyle(numberStyle);
        } else {
            R219Cell10.setCellValue("");
            R219Cell10.setCellStyle(textStyle);
        }

        /* ================= R220 ================= */
        row = sheet.getRow(219);
        Cell R220Cell1 = row.createCell(3);
        if (record2.getR220_FIG_BAL_BWP1() != null) {
            R220Cell1.setCellValue(record2.getR220_FIG_BAL_BWP1().doubleValue());
            R220Cell1.setCellStyle(numberStyle);
        } else {
            R220Cell1.setCellValue("");
            R220Cell1.setCellStyle(textStyle);
        }
        Cell R220Cell2 = row.createCell(4);
        if (record2.getR220_FIG_BAL_BWP2() != null) {
            R220Cell2.setCellValue(record2.getR220_FIG_BAL_BWP2().doubleValue());
            R220Cell2.setCellStyle(numberStyle);
        } else {
            R220Cell2.setCellValue("");
            R220Cell2.setCellStyle(textStyle);
        }
        Cell R220Cell3 = row.createCell(5);
        if (record2.getR220_AMT_ADJ_BWP1() != null) {
            R220Cell3.setCellValue(record2.getR220_AMT_ADJ_BWP1().doubleValue());
            R220Cell3.setCellStyle(numberStyle);
        } else {
            R220Cell3.setCellValue("");
            R220Cell3.setCellStyle(textStyle);
        }
        Cell R220Cell4 = row.createCell(6);
        if (record2.getR220_AMT_ADJ_BWP2() != null) {
            R220Cell4.setCellValue(record2.getR220_AMT_ADJ_BWP2().doubleValue());
            R220Cell4.setCellStyle(numberStyle);
        } else {
            R220Cell4.setCellValue("");
            R220Cell4.setCellStyle(textStyle);
        }
        Cell R220Cell5 = row.createCell(7);
        if (record2.getR220_NET_AMT_BWP1() != null) {
            R220Cell5.setCellValue(record2.getR220_NET_AMT_BWP1().doubleValue());
            R220Cell5.setCellStyle(numberStyle);
        } else {
            R220Cell5.setCellValue("");
            R220Cell5.setCellStyle(textStyle);
        }
        Cell R220Cell6 = row.createCell(8);
        if (record2.getR220_NET_AMT_BWP2() != null) {
            R220Cell6.setCellValue(record2.getR220_NET_AMT_BWP2().doubleValue());
            R220Cell6.setCellStyle(numberStyle);
        } else {
            R220Cell6.setCellValue("");
            R220Cell6.setCellStyle(textStyle);
        }
        Cell R220Cell7 = row.createCell(9);
        if (record2.getR220_BAL_SUB_BWP1() != null) {
            R220Cell7.setCellValue(record2.getR220_BAL_SUB_BWP1().doubleValue());
            R220Cell7.setCellStyle(numberStyle);
        } else {
            R220Cell7.setCellValue("");
            R220Cell7.setCellStyle(textStyle);
        }
        Cell R220Cell8 = row.createCell(10);
        if (record2.getR220_BAL_SUB_BWP2() != null) {
            R220Cell8.setCellValue(record2.getR220_BAL_SUB_BWP2().doubleValue());
            R220Cell8.setCellStyle(numberStyle);
        } else {
            R220Cell8.setCellValue("");
            R220Cell8.setCellStyle(textStyle);
        }
        Cell R220Cell9 = row.createCell(11);
        if (record2.getR220_BAL_ACT_SUB_BWP1() != null) {
            R220Cell9.setCellValue(record2.getR220_BAL_ACT_SUB_BWP1().doubleValue());
            R220Cell9.setCellStyle(numberStyle);
        } else {
            R220Cell9.setCellValue("");
            R220Cell9.setCellStyle(textStyle);
        }
        Cell R220Cell10 = row.createCell(12);
        if (record2.getR220_BAL_ACT_SUB_BWP2() != null) {
            R220Cell10.setCellValue(record2.getR220_BAL_ACT_SUB_BWP2().doubleValue());
            R220Cell10.setCellStyle(numberStyle);
        } else {
            R220Cell10.setCellValue("");
            R220Cell10.setCellStyle(textStyle);
        }

        /* ================= R233 ================= */
        row = sheet.getRow(232);
        Cell R233Cell1 = row.createCell(3);
        if (record2.getR233_FIG_BAL_BWP1() != null) {
            R233Cell1.setCellValue(record2.getR233_FIG_BAL_BWP1().doubleValue());
            R233Cell1.setCellStyle(numberStyle);
        } else {
            R233Cell1.setCellValue("");
            R233Cell1.setCellStyle(textStyle);
        }
        Cell R233Cell2 = row.createCell(4);
        if (record2.getR233_FIG_BAL_BWP2() != null) {
            R233Cell2.setCellValue(record2.getR233_FIG_BAL_BWP2().doubleValue());
            R233Cell2.setCellStyle(numberStyle);
        } else {
            R233Cell2.setCellValue("");
            R233Cell2.setCellStyle(textStyle);
        }
        Cell R233Cell3 = row.createCell(5);
        if (record2.getR233_AMT_ADJ_BWP1() != null) {
            R233Cell3.setCellValue(record2.getR233_AMT_ADJ_BWP1().doubleValue());
            R233Cell3.setCellStyle(numberStyle);
        } else {
            R233Cell3.setCellValue("");
            R233Cell3.setCellStyle(textStyle);
        }
        Cell R233Cell4 = row.createCell(6);
        if (record2.getR233_AMT_ADJ_BWP2() != null) {
            R233Cell4.setCellValue(record2.getR233_AMT_ADJ_BWP2().doubleValue());
            R233Cell4.setCellStyle(numberStyle);
        } else {
            R233Cell4.setCellValue("");
            R233Cell4.setCellStyle(textStyle);
        }
        Cell R233Cell5 = row.createCell(7);
        if (record2.getR233_NET_AMT_BWP1() != null) {
            R233Cell5.setCellValue(record2.getR233_NET_AMT_BWP1().doubleValue());
            R233Cell5.setCellStyle(numberStyle);
        } else {
            R233Cell5.setCellValue("");
            R233Cell5.setCellStyle(textStyle);
        }
        Cell R233Cell6 = row.createCell(8);
        if (record2.getR233_NET_AMT_BWP2() != null) {
            R233Cell6.setCellValue(record2.getR233_NET_AMT_BWP2().doubleValue());
            R233Cell6.setCellStyle(numberStyle);
        } else {
            R233Cell6.setCellValue("");
            R233Cell6.setCellStyle(textStyle);
        }
        Cell R233Cell7 = row.createCell(9);
        if (record2.getR233_BAL_SUB_BWP1() != null) {
            R233Cell7.setCellValue(record2.getR233_BAL_SUB_BWP1().doubleValue());
            R233Cell7.setCellStyle(numberStyle);
        } else {
            R233Cell7.setCellValue("");
            R233Cell7.setCellStyle(textStyle);
        }
        Cell R233Cell8 = row.createCell(10);
        if (record2.getR233_BAL_SUB_BWP2() != null) {
            R233Cell8.setCellValue(record2.getR233_BAL_SUB_BWP2().doubleValue());
            R233Cell8.setCellStyle(numberStyle);
        } else {
            R233Cell8.setCellValue("");
            R233Cell8.setCellStyle(textStyle);
        }
        Cell R233Cell9 = row.createCell(11);
        if (record2.getR233_BAL_ACT_SUB_BWP1() != null) {
            R233Cell9.setCellValue(record2.getR233_BAL_ACT_SUB_BWP1().doubleValue());
            R233Cell9.setCellStyle(numberStyle);
        } else {
            R233Cell9.setCellValue("");
            R233Cell9.setCellStyle(textStyle);
        }
        Cell R233Cell10 = row.createCell(12);
        if (record2.getR233_BAL_ACT_SUB_BWP2() != null) {
            R233Cell10.setCellValue(record2.getR233_BAL_ACT_SUB_BWP2().doubleValue());
            R233Cell10.setCellStyle(numberStyle);
        } else {
            R233Cell10.setCellValue("");
            R233Cell10.setCellStyle(textStyle);
        }

        /* ================= R242 ================= */
        row = sheet.getRow(241);
        Cell R242Cell1 = row.createCell(3);
        if (record2.getR242_FIG_BAL_BWP1() != null) {
            R242Cell1.setCellValue(record2.getR242_FIG_BAL_BWP1().doubleValue());
            R242Cell1.setCellStyle(numberStyle);
        } else {
            R242Cell1.setCellValue("");
            R242Cell1.setCellStyle(textStyle);
        }
        Cell R242Cell2 = row.createCell(4);
        if (record2.getR242_FIG_BAL_BWP2() != null) {
            R242Cell2.setCellValue(record2.getR242_FIG_BAL_BWP2().doubleValue());
            R242Cell2.setCellStyle(numberStyle);
        } else {
            R242Cell2.setCellValue("");
            R242Cell2.setCellStyle(textStyle);
        }
        Cell R242Cell3 = row.createCell(5);
        if (record2.getR242_AMT_ADJ_BWP1() != null) {
            R242Cell3.setCellValue(record2.getR242_AMT_ADJ_BWP1().doubleValue());
            R242Cell3.setCellStyle(numberStyle);
        } else {
            R242Cell3.setCellValue("");
            R242Cell3.setCellStyle(textStyle);
        }
        Cell R242Cell4 = row.createCell(6);
        if (record2.getR242_AMT_ADJ_BWP2() != null) {
            R242Cell4.setCellValue(record2.getR242_AMT_ADJ_BWP2().doubleValue());
            R242Cell4.setCellStyle(numberStyle);
        } else {
            R242Cell4.setCellValue("");
            R242Cell4.setCellStyle(textStyle);
        }
        Cell R242Cell5 = row.createCell(7);
        if (record2.getR242_NET_AMT_BWP1() != null) {
            R242Cell5.setCellValue(record2.getR242_NET_AMT_BWP1().doubleValue());
            R242Cell5.setCellStyle(numberStyle);
        } else {
            R242Cell5.setCellValue("");
            R242Cell5.setCellStyle(textStyle);
        }
        Cell R242Cell6 = row.createCell(8);
        if (record2.getR242_NET_AMT_BWP2() != null) {
            R242Cell6.setCellValue(record2.getR242_NET_AMT_BWP2().doubleValue());
            R242Cell6.setCellStyle(numberStyle);
        } else {
            R242Cell6.setCellValue("");
            R242Cell6.setCellStyle(textStyle);
        }
        Cell R242Cell7 = row.createCell(9);
        if (record2.getR242_BAL_SUB_BWP1() != null) {
            R242Cell7.setCellValue(record2.getR242_BAL_SUB_BWP1().doubleValue());
            R242Cell7.setCellStyle(numberStyle);
        } else {
            R242Cell7.setCellValue("");
            R242Cell7.setCellStyle(textStyle);
        }
        Cell R242Cell8 = row.createCell(10);
        if (record2.getR242_BAL_SUB_BWP2() != null) {
            R242Cell8.setCellValue(record2.getR242_BAL_SUB_BWP2().doubleValue());
            R242Cell8.setCellStyle(numberStyle);
        } else {
            R242Cell8.setCellValue("");
            R242Cell8.setCellStyle(textStyle);
        }
        Cell R242Cell9 = row.createCell(11);
        if (record2.getR242_BAL_ACT_SUB_BWP1() != null) {
            R242Cell9.setCellValue(record2.getR242_BAL_ACT_SUB_BWP1().doubleValue());
            R242Cell9.setCellStyle(numberStyle);
        } else {
            R242Cell9.setCellValue("");
            R242Cell9.setCellStyle(textStyle);
        }
        Cell R242Cell10 = row.createCell(12);
        if (record2.getR242_BAL_ACT_SUB_BWP2() != null) {
            R242Cell10.setCellValue(record2.getR242_BAL_ACT_SUB_BWP2().doubleValue());
            R242Cell10.setCellStyle(numberStyle);
        } else {
            R242Cell10.setCellValue("");
            R242Cell10.setCellStyle(textStyle);
        }

        /* ================= R244 ================= */
        row = sheet.getRow(243);
        Cell R244Cell1 = row.createCell(3);
        if (record2.getR244_FIG_BAL_BWP1() != null) {
            R244Cell1.setCellValue(record2.getR244_FIG_BAL_BWP1().doubleValue());
            R244Cell1.setCellStyle(numberStyle);
        } else {
            R244Cell1.setCellValue("");
            R244Cell1.setCellStyle(textStyle);
        }
        Cell R244Cell2 = row.createCell(4);
        if (record2.getR244_FIG_BAL_BWP2() != null) {
            R244Cell2.setCellValue(record2.getR244_FIG_BAL_BWP2().doubleValue());
            R244Cell2.setCellStyle(numberStyle);
        } else {
            R244Cell2.setCellValue("");
            R244Cell2.setCellStyle(textStyle);
        }
        Cell R244Cell3 = row.createCell(5);
        if (record2.getR244_AMT_ADJ_BWP1() != null) {
            R244Cell3.setCellValue(record2.getR244_AMT_ADJ_BWP1().doubleValue());
            R244Cell3.setCellStyle(numberStyle);
        } else {
            R244Cell3.setCellValue("");
            R244Cell3.setCellStyle(textStyle);
        }
        Cell R244Cell4 = row.createCell(6);
        if (record2.getR244_AMT_ADJ_BWP2() != null) {
            R244Cell4.setCellValue(record2.getR244_AMT_ADJ_BWP2().doubleValue());
            R244Cell4.setCellStyle(numberStyle);
        } else {
            R244Cell4.setCellValue("");
            R244Cell4.setCellStyle(textStyle);
        }
        Cell R244Cell5 = row.createCell(7);
        if (record2.getR244_NET_AMT_BWP1() != null) {
            R244Cell5.setCellValue(record2.getR244_NET_AMT_BWP1().doubleValue());
            R244Cell5.setCellStyle(numberStyle);
        } else {
            R244Cell5.setCellValue("");
            R244Cell5.setCellStyle(textStyle);
        }
        Cell R244Cell6 = row.createCell(8);
        if (record2.getR244_NET_AMT_BWP2() != null) {
            R244Cell6.setCellValue(record2.getR244_NET_AMT_BWP2().doubleValue());
            R244Cell6.setCellStyle(numberStyle);
        } else {
            R244Cell6.setCellValue("");
            R244Cell6.setCellStyle(textStyle);
        }
        Cell R244Cell7 = row.createCell(9);
        if (record2.getR244_BAL_SUB_BWP1() != null) {
            R244Cell7.setCellValue(record2.getR244_BAL_SUB_BWP1().doubleValue());
            R244Cell7.setCellStyle(numberStyle);
        } else {
            R244Cell7.setCellValue("");
            R244Cell7.setCellStyle(textStyle);
        }
        Cell R244Cell8 = row.createCell(10);
        if (record2.getR244_BAL_SUB_BWP2() != null) {
            R244Cell8.setCellValue(record2.getR244_BAL_SUB_BWP2().doubleValue());
            R244Cell8.setCellStyle(numberStyle);
        } else {
            R244Cell8.setCellValue("");
            R244Cell8.setCellStyle(textStyle);
        }
        Cell R244Cell9 = row.createCell(11);
        if (record2.getR244_BAL_ACT_SUB_BWP1() != null) {
            R244Cell9.setCellValue(record2.getR244_BAL_ACT_SUB_BWP1().doubleValue());
            R244Cell9.setCellStyle(numberStyle);
        } else {
            R244Cell9.setCellValue("");
            R244Cell9.setCellStyle(textStyle);
        }
        Cell R244Cell10 = row.createCell(12);
        if (record2.getR244_BAL_ACT_SUB_BWP2() != null) {
            R244Cell10.setCellValue(record2.getR244_BAL_ACT_SUB_BWP2().doubleValue());
            R244Cell10.setCellStyle(numberStyle);
        } else {
            R244Cell10.setCellValue("");
            R244Cell10.setCellStyle(textStyle);
        }

        /* ================= R245 ================= */

        /* ================= R246 ================= */
        row = sheet.getRow(245);
        Cell R246Cell1 = row.createCell(3);
        if (record2.getR246_FIG_BAL_BWP1() != null) {
            R246Cell1.setCellValue(record2.getR246_FIG_BAL_BWP1().doubleValue());
            R246Cell1.setCellStyle(numberStyle);
        } else {
            R246Cell1.setCellValue("");
            R246Cell1.setCellStyle(textStyle);
        }
        Cell R246Cell2 = row.createCell(4);
        if (record2.getR246_FIG_BAL_BWP2() != null) {
            R246Cell2.setCellValue(record2.getR246_FIG_BAL_BWP2().doubleValue());
            R246Cell2.setCellStyle(numberStyle);
        } else {
            R246Cell2.setCellValue("");
            R246Cell2.setCellStyle(textStyle);
        }
        Cell R246Cell3 = row.createCell(5);
        if (record2.getR246_AMT_ADJ_BWP1() != null) {
            R246Cell3.setCellValue(record2.getR246_AMT_ADJ_BWP1().doubleValue());
            R246Cell3.setCellStyle(numberStyle);
        } else {
            R246Cell3.setCellValue("");
            R246Cell3.setCellStyle(textStyle);
        }
        Cell R246Cell4 = row.createCell(6);
        if (record2.getR246_AMT_ADJ_BWP2() != null) {
            R246Cell4.setCellValue(record2.getR246_AMT_ADJ_BWP2().doubleValue());
            R246Cell4.setCellStyle(numberStyle);
        } else {
            R246Cell4.setCellValue("");
            R246Cell4.setCellStyle(textStyle);
        }
        Cell R246Cell5 = row.createCell(7);
        if (record2.getR246_NET_AMT_BWP1() != null) {
            R246Cell5.setCellValue(record2.getR246_NET_AMT_BWP1().doubleValue());
            R246Cell5.setCellStyle(numberStyle);
        } else {
            R246Cell5.setCellValue("");
            R246Cell5.setCellStyle(textStyle);
        }
        Cell R246Cell6 = row.createCell(8);
        if (record2.getR246_NET_AMT_BWP2() != null) {
            R246Cell6.setCellValue(record2.getR246_NET_AMT_BWP2().doubleValue());
            R246Cell6.setCellStyle(numberStyle);
        } else {
            R246Cell6.setCellValue("");
            R246Cell6.setCellStyle(textStyle);
        }
        Cell R246Cell7 = row.createCell(9);
        if (record2.getR246_BAL_SUB_BWP1() != null) {
            R246Cell7.setCellValue(record2.getR246_BAL_SUB_BWP1().doubleValue());
            R246Cell7.setCellStyle(numberStyle);
        } else {
            R246Cell7.setCellValue("");
            R246Cell7.setCellStyle(textStyle);
        }
        Cell R246Cell8 = row.createCell(10);
        if (record2.getR246_BAL_SUB_BWP2() != null) {
            R246Cell8.setCellValue(record2.getR246_BAL_SUB_BWP2().doubleValue());
            R246Cell8.setCellStyle(numberStyle);
        } else {
            R246Cell8.setCellValue("");
            R246Cell8.setCellStyle(textStyle);
        }
        Cell R246Cell9 = row.createCell(11);
        if (record2.getR246_BAL_ACT_SUB_BWP1() != null) {
            R246Cell9.setCellValue(record2.getR246_BAL_ACT_SUB_BWP1().doubleValue());
            R246Cell9.setCellStyle(numberStyle);
        } else {
            R246Cell9.setCellValue("");
            R246Cell9.setCellStyle(textStyle);
        }
        Cell R246Cell10 = row.createCell(12);
        if (record2.getR246_BAL_ACT_SUB_BWP2() != null) {
            R246Cell10.setCellValue(record2.getR246_BAL_ACT_SUB_BWP2().doubleValue());
            R246Cell10.setCellStyle(numberStyle);
        } else {
            R246Cell10.setCellValue("");
            R246Cell10.setCellStyle(textStyle);
        }

        /* ================= R247 ================= */
        row = sheet.getRow(246);
        Cell R247Cell1 = row.createCell(3);
        if (record2.getR247_FIG_BAL_BWP1() != null) {
            R247Cell1.setCellValue(record2.getR247_FIG_BAL_BWP1().doubleValue());
            R247Cell1.setCellStyle(numberStyle);
        } else {
            R247Cell1.setCellValue("");
            R247Cell1.setCellStyle(textStyle);
        }
        Cell R247Cell2 = row.createCell(4);
        if (record2.getR247_FIG_BAL_BWP2() != null) {
            R247Cell2.setCellValue(record2.getR247_FIG_BAL_BWP2().doubleValue());
            R247Cell2.setCellStyle(numberStyle);
        } else {
            R247Cell2.setCellValue("");
            R247Cell2.setCellStyle(textStyle);
        }
        Cell R247Cell3 = row.createCell(5);
        if (record2.getR247_AMT_ADJ_BWP1() != null) {
            R247Cell3.setCellValue(record2.getR247_AMT_ADJ_BWP1().doubleValue());
            R247Cell3.setCellStyle(numberStyle);
        } else {
            R247Cell3.setCellValue("");
            R247Cell3.setCellStyle(textStyle);
        }
        Cell R247Cell4 = row.createCell(6);
        if (record2.getR247_AMT_ADJ_BWP2() != null) {
            R247Cell4.setCellValue(record2.getR247_AMT_ADJ_BWP2().doubleValue());
            R247Cell4.setCellStyle(numberStyle);
        } else {
            R247Cell4.setCellValue("");
            R247Cell4.setCellStyle(textStyle);
        }
        Cell R247Cell5 = row.createCell(7);
        if (record2.getR247_NET_AMT_BWP1() != null) {
            R247Cell5.setCellValue(record2.getR247_NET_AMT_BWP1().doubleValue());
            R247Cell5.setCellStyle(numberStyle);
        } else {
            R247Cell5.setCellValue("");
            R247Cell5.setCellStyle(textStyle);
        }
        Cell R247Cell6 = row.createCell(8);
        if (record2.getR247_NET_AMT_BWP2() != null) {
            R247Cell6.setCellValue(record2.getR247_NET_AMT_BWP2().doubleValue());
            R247Cell6.setCellStyle(numberStyle);
        } else {
            R247Cell6.setCellValue("");
            R247Cell6.setCellStyle(textStyle);
        }
        Cell R247Cell7 = row.createCell(9);
        if (record2.getR247_BAL_SUB_BWP1() != null) {
            R247Cell7.setCellValue(record2.getR247_BAL_SUB_BWP1().doubleValue());
            R247Cell7.setCellStyle(numberStyle);
        } else {
            R247Cell7.setCellValue("");
            R247Cell7.setCellStyle(textStyle);
        }
        Cell R247Cell8 = row.createCell(10);
        if (record2.getR247_BAL_SUB_BWP2() != null) {
            R247Cell8.setCellValue(record2.getR247_BAL_SUB_BWP2().doubleValue());
            R247Cell8.setCellStyle(numberStyle);
        } else {
            R247Cell8.setCellValue("");
            R247Cell8.setCellStyle(textStyle);
        }
        Cell R247Cell9 = row.createCell(11);
        if (record2.getR247_BAL_ACT_SUB_BWP1() != null) {
            R247Cell9.setCellValue(record2.getR247_BAL_ACT_SUB_BWP1().doubleValue());
            R247Cell9.setCellStyle(numberStyle);
        } else {
            R247Cell9.setCellValue("");
            R247Cell9.setCellStyle(textStyle);
        }
        Cell R247Cell10 = row.createCell(12);
        if (record2.getR247_BAL_ACT_SUB_BWP2() != null) {
            R247Cell10.setCellValue(record2.getR247_BAL_ACT_SUB_BWP2().doubleValue());
            R247Cell10.setCellStyle(numberStyle);
        } else {
            R247Cell10.setCellValue("");
            R247Cell10.setCellStyle(textStyle);
        }

        /* ================= R248 ================= */
        row = sheet.getRow(247);
        Cell R248Cell1 = row.createCell(3);
        if (record2.getR248_FIG_BAL_BWP1() != null) {
            R248Cell1.setCellValue(record2.getR248_FIG_BAL_BWP1().doubleValue());
            R248Cell1.setCellStyle(numberStyle);
        } else {
            R248Cell1.setCellValue("");
            R248Cell1.setCellStyle(textStyle);
        }
        Cell R248Cell2 = row.createCell(4);
        if (record2.getR248_FIG_BAL_BWP2() != null) {
            R248Cell2.setCellValue(record2.getR248_FIG_BAL_BWP2().doubleValue());
            R248Cell2.setCellStyle(numberStyle);
        } else {
            R248Cell2.setCellValue("");
            R248Cell2.setCellStyle(textStyle);
        }
        Cell R248Cell3 = row.createCell(5);
        if (record2.getR248_AMT_ADJ_BWP1() != null) {
            R248Cell3.setCellValue(record2.getR248_AMT_ADJ_BWP1().doubleValue());
            R248Cell3.setCellStyle(numberStyle);
        } else {
            R248Cell3.setCellValue("");
            R248Cell3.setCellStyle(textStyle);
        }
        Cell R248Cell4 = row.createCell(6);
        if (record2.getR248_AMT_ADJ_BWP2() != null) {
            R248Cell4.setCellValue(record2.getR248_AMT_ADJ_BWP2().doubleValue());
            R248Cell4.setCellStyle(numberStyle);
        } else {
            R248Cell4.setCellValue("");
            R248Cell4.setCellStyle(textStyle);
        }
        Cell R248Cell5 = row.createCell(7);
        if (record2.getR248_NET_AMT_BWP1() != null) {
            R248Cell5.setCellValue(record2.getR248_NET_AMT_BWP1().doubleValue());
            R248Cell5.setCellStyle(numberStyle);
        } else {
            R248Cell5.setCellValue("");
            R248Cell5.setCellStyle(textStyle);
        }
        Cell R248Cell6 = row.createCell(8);
        if (record2.getR248_NET_AMT_BWP2() != null) {
            R248Cell6.setCellValue(record2.getR248_NET_AMT_BWP2().doubleValue());
            R248Cell6.setCellStyle(numberStyle);
        } else {
            R248Cell6.setCellValue("");
            R248Cell6.setCellStyle(textStyle);
        }
        Cell R248Cell7 = row.createCell(9);
        if (record2.getR248_BAL_SUB_BWP1() != null) {
            R248Cell7.setCellValue(record2.getR248_BAL_SUB_BWP1().doubleValue());
            R248Cell7.setCellStyle(numberStyle);
        } else {
            R248Cell7.setCellValue("");
            R248Cell7.setCellStyle(textStyle);
        }
        Cell R248Cell8 = row.createCell(10);
        if (record2.getR248_BAL_SUB_BWP2() != null) {
            R248Cell8.setCellValue(record2.getR248_BAL_SUB_BWP2().doubleValue());
            R248Cell8.setCellStyle(numberStyle);
        } else {
            R248Cell8.setCellValue("");
            R248Cell8.setCellStyle(textStyle);
        }
        Cell R248Cell9 = row.createCell(11);
        if (record2.getR248_BAL_ACT_SUB_BWP1() != null) {
            R248Cell9.setCellValue(record2.getR248_BAL_ACT_SUB_BWP1().doubleValue());
            R248Cell9.setCellStyle(numberStyle);
        } else {
            R248Cell9.setCellValue("");
            R248Cell9.setCellStyle(textStyle);
        }
        Cell R248Cell10 = row.createCell(12);
        if (record2.getR248_BAL_ACT_SUB_BWP2() != null) {
            R248Cell10.setCellValue(record2.getR248_BAL_ACT_SUB_BWP2().doubleValue());
            R248Cell10.setCellStyle(numberStyle);
        } else {
            R248Cell10.setCellValue("");
            R248Cell10.setCellStyle(textStyle);
        }

        /* ================= R252 ================= */
        row = sheet.getRow(251);
        Cell R252Cell1 = row.createCell(3);
        if (record2.getR252_FIG_BAL_BWP1() != null) {
            R252Cell1.setCellValue(record2.getR252_FIG_BAL_BWP1().doubleValue());
            R252Cell1.setCellStyle(numberStyle);
        } else {
            R252Cell1.setCellValue("");
            R252Cell1.setCellStyle(textStyle);
        }
        Cell R252Cell2 = row.createCell(4);
        if (record2.getR252_FIG_BAL_BWP2() != null) {
            R252Cell2.setCellValue(record2.getR252_FIG_BAL_BWP2().doubleValue());
            R252Cell2.setCellStyle(numberStyle);
        } else {
            R252Cell2.setCellValue("");
            R252Cell2.setCellStyle(textStyle);
        }
        Cell R252Cell3 = row.createCell(5);
        if (record2.getR252_AMT_ADJ_BWP1() != null) {
            R252Cell3.setCellValue(record2.getR252_AMT_ADJ_BWP1().doubleValue());
            R252Cell3.setCellStyle(numberStyle);
        } else {
            R252Cell3.setCellValue("");
            R252Cell3.setCellStyle(textStyle);
        }
        Cell R252Cell4 = row.createCell(6);
        if (record2.getR252_AMT_ADJ_BWP2() != null) {
            R252Cell4.setCellValue(record2.getR252_AMT_ADJ_BWP2().doubleValue());
            R252Cell4.setCellStyle(numberStyle);
        } else {
            R252Cell4.setCellValue("");
            R252Cell4.setCellStyle(textStyle);
        }
        Cell R252Cell5 = row.createCell(7);
        if (record2.getR252_NET_AMT_BWP1() != null) {
            R252Cell5.setCellValue(record2.getR252_NET_AMT_BWP1().doubleValue());
            R252Cell5.setCellStyle(numberStyle);
        } else {
            R252Cell5.setCellValue("");
            R252Cell5.setCellStyle(textStyle);
        }
        Cell R252Cell6 = row.createCell(8);
        if (record2.getR252_NET_AMT_BWP2() != null) {
            R252Cell6.setCellValue(record2.getR252_NET_AMT_BWP2().doubleValue());
            R252Cell6.setCellStyle(numberStyle);
        } else {
            R252Cell6.setCellValue("");
            R252Cell6.setCellStyle(textStyle);
        }
        Cell R252Cell7 = row.createCell(9);
        if (record2.getR252_BAL_SUB_BWP1() != null) {
            R252Cell7.setCellValue(record2.getR252_BAL_SUB_BWP1().doubleValue());
            R252Cell7.setCellStyle(numberStyle);
        } else {
            R252Cell7.setCellValue("");
            R252Cell7.setCellStyle(textStyle);
        }
        Cell R252Cell8 = row.createCell(10);
        if (record2.getR252_BAL_SUB_BWP2() != null) {
            R252Cell8.setCellValue(record2.getR252_BAL_SUB_BWP2().doubleValue());
            R252Cell8.setCellStyle(numberStyle);
        } else {
            R252Cell8.setCellValue("");
            R252Cell8.setCellStyle(textStyle);
        }
        Cell R252Cell9 = row.createCell(11);
        if (record2.getR252_BAL_ACT_SUB_BWP1() != null) {
            R252Cell9.setCellValue(record2.getR252_BAL_ACT_SUB_BWP1().doubleValue());
            R252Cell9.setCellStyle(numberStyle);
        } else {
            R252Cell9.setCellValue("");
            R252Cell9.setCellStyle(textStyle);
        }
        Cell R252Cell10 = row.createCell(12);
        if (record2.getR252_BAL_ACT_SUB_BWP2() != null) {
            R252Cell10.setCellValue(record2.getR252_BAL_ACT_SUB_BWP2().doubleValue());
            R252Cell10.setCellStyle(numberStyle);
        } else {
            R252Cell10.setCellValue("");
            R252Cell10.setCellStyle(textStyle);
        }

        /* ================= R253 ================= */
        row = sheet.getRow(252);
        Cell R253Cell1 = row.createCell(3);
        if (record2.getR253_FIG_BAL_BWP1() != null) {
            R253Cell1.setCellValue(record2.getR253_FIG_BAL_BWP1().doubleValue());
            R253Cell1.setCellStyle(numberStyle);
        } else {
            R253Cell1.setCellValue("");
            R253Cell1.setCellStyle(textStyle);
        }
        Cell R253Cell2 = row.createCell(4);
        if (record2.getR253_FIG_BAL_BWP2() != null) {
            R253Cell2.setCellValue(record2.getR253_FIG_BAL_BWP2().doubleValue());
            R253Cell2.setCellStyle(numberStyle);
        } else {
            R253Cell2.setCellValue("");
            R253Cell2.setCellStyle(textStyle);
        }
        Cell R253Cell3 = row.createCell(5);
        if (record2.getR253_AMT_ADJ_BWP1() != null) {
            R253Cell3.setCellValue(record2.getR253_AMT_ADJ_BWP1().doubleValue());
            R253Cell3.setCellStyle(numberStyle);
        } else {
            R253Cell3.setCellValue("");
            R253Cell3.setCellStyle(textStyle);
        }
        Cell R253Cell4 = row.createCell(6);
        if (record2.getR253_AMT_ADJ_BWP2() != null) {
            R253Cell4.setCellValue(record2.getR253_AMT_ADJ_BWP2().doubleValue());
            R253Cell4.setCellStyle(numberStyle);
        } else {
            R253Cell4.setCellValue("");
            R253Cell4.setCellStyle(textStyle);
        }
        Cell R253Cell5 = row.createCell(7);
        if (record2.getR253_NET_AMT_BWP1() != null) {
            R253Cell5.setCellValue(record2.getR253_NET_AMT_BWP1().doubleValue());
            R253Cell5.setCellStyle(numberStyle);
        } else {
            R253Cell5.setCellValue("");
            R253Cell5.setCellStyle(textStyle);
        }
        Cell R253Cell6 = row.createCell(8);
        if (record2.getR253_NET_AMT_BWP2() != null) {
            R253Cell6.setCellValue(record2.getR253_NET_AMT_BWP2().doubleValue());
            R253Cell6.setCellStyle(numberStyle);
        } else {
            R253Cell6.setCellValue("");
            R253Cell6.setCellStyle(textStyle);
        }
        Cell R253Cell7 = row.createCell(9);
        if (record2.getR253_BAL_SUB_BWP1() != null) {
            R253Cell7.setCellValue(record2.getR253_BAL_SUB_BWP1().doubleValue());
            R253Cell7.setCellStyle(numberStyle);
        } else {
            R253Cell7.setCellValue("");
            R253Cell7.setCellStyle(textStyle);
        }
        Cell R253Cell8 = row.createCell(10);
        if (record2.getR253_BAL_SUB_BWP2() != null) {
            R253Cell8.setCellValue(record2.getR253_BAL_SUB_BWP2().doubleValue());
            R253Cell8.setCellStyle(numberStyle);
        } else {
            R253Cell8.setCellValue("");
            R253Cell8.setCellStyle(textStyle);
        }
        Cell R253Cell9 = row.createCell(11);
        if (record2.getR253_BAL_ACT_SUB_BWP1() != null) {
            R253Cell9.setCellValue(record2.getR253_BAL_ACT_SUB_BWP1().doubleValue());
            R253Cell9.setCellStyle(numberStyle);
        } else {
            R253Cell9.setCellValue("");
            R253Cell9.setCellStyle(textStyle);
        }
        Cell R253Cell10 = row.createCell(12);
        if (record2.getR253_BAL_ACT_SUB_BWP2() != null) {
            R253Cell10.setCellValue(record2.getR253_BAL_ACT_SUB_BWP2().doubleValue());
            R253Cell10.setCellStyle(numberStyle);
        } else {
            R253Cell10.setCellValue("");
            R253Cell10.setCellStyle(textStyle);
        }

        /* ================= R254 ================= */
        row = sheet.getRow(253);
        Cell R254Cell1 = row.createCell(3);
        if (record2.getR254_FIG_BAL_BWP1() != null) {
            R254Cell1.setCellValue(record2.getR254_FIG_BAL_BWP1().doubleValue());
            R254Cell1.setCellStyle(numberStyle);
        } else {
            R254Cell1.setCellValue("");
            R254Cell1.setCellStyle(textStyle);
        }
        Cell R254Cell2 = row.createCell(4);
        if (record2.getR254_FIG_BAL_BWP2() != null) {
            R254Cell2.setCellValue(record2.getR254_FIG_BAL_BWP2().doubleValue());
            R254Cell2.setCellStyle(numberStyle);
        } else {
            R254Cell2.setCellValue("");
            R254Cell2.setCellStyle(textStyle);
        }
        Cell R254Cell3 = row.createCell(5);
        if (record2.getR254_AMT_ADJ_BWP1() != null) {
            R254Cell3.setCellValue(record2.getR254_AMT_ADJ_BWP1().doubleValue());
            R254Cell3.setCellStyle(numberStyle);
        } else {
            R254Cell3.setCellValue("");
            R254Cell3.setCellStyle(textStyle);
        }
        Cell R254Cell4 = row.createCell(6);
        if (record2.getR254_AMT_ADJ_BWP2() != null) {
            R254Cell4.setCellValue(record2.getR254_AMT_ADJ_BWP2().doubleValue());
            R254Cell4.setCellStyle(numberStyle);
        } else {
            R254Cell4.setCellValue("");
            R254Cell4.setCellStyle(textStyle);
        }
        Cell R254Cell5 = row.createCell(7);
        if (record2.getR254_NET_AMT_BWP1() != null) {
            R254Cell5.setCellValue(record2.getR254_NET_AMT_BWP1().doubleValue());
            R254Cell5.setCellStyle(numberStyle);
        } else {
            R254Cell5.setCellValue("");
            R254Cell5.setCellStyle(textStyle);
        }
        Cell R254Cell6 = row.createCell(8);
        if (record2.getR254_NET_AMT_BWP2() != null) {
            R254Cell6.setCellValue(record2.getR254_NET_AMT_BWP2().doubleValue());
            R254Cell6.setCellStyle(numberStyle);
        } else {
            R254Cell6.setCellValue("");
            R254Cell6.setCellStyle(textStyle);
        }
        Cell R254Cell7 = row.createCell(9);
        if (record2.getR254_BAL_SUB_BWP1() != null) {
            R254Cell7.setCellValue(record2.getR254_BAL_SUB_BWP1().doubleValue());
            R254Cell7.setCellStyle(numberStyle);
        } else {
            R254Cell7.setCellValue("");
            R254Cell7.setCellStyle(textStyle);
        }
        Cell R254Cell8 = row.createCell(10);
        if (record2.getR254_BAL_SUB_BWP2() != null) {
            R254Cell8.setCellValue(record2.getR254_BAL_SUB_BWP2().doubleValue());
            R254Cell8.setCellStyle(numberStyle);
        } else {
            R254Cell8.setCellValue("");
            R254Cell8.setCellStyle(textStyle);
        }
        Cell R254Cell9 = row.createCell(11);
        if (record2.getR254_BAL_ACT_SUB_BWP1() != null) {
            R254Cell9.setCellValue(record2.getR254_BAL_ACT_SUB_BWP1().doubleValue());
            R254Cell9.setCellStyle(numberStyle);
        } else {
            R254Cell9.setCellValue("");
            R254Cell9.setCellStyle(textStyle);
        }
        Cell R254Cell10 = row.createCell(12);
        if (record2.getR254_BAL_ACT_SUB_BWP2() != null) {
            R254Cell10.setCellValue(record2.getR254_BAL_ACT_SUB_BWP2().doubleValue());
            R254Cell10.setCellStyle(numberStyle);
        } else {
            R254Cell10.setCellValue("");
            R254Cell10.setCellStyle(textStyle);
        }

        /* ================= R255 ================= */
        row = sheet.getRow(254);
        Cell R255Cell1 = row.createCell(3);
        if (record2.getR255_FIG_BAL_BWP1() != null) {
            R255Cell1.setCellValue(record2.getR255_FIG_BAL_BWP1().doubleValue());
            R255Cell1.setCellStyle(numberStyle);
        } else {
            R255Cell1.setCellValue("");
            R255Cell1.setCellStyle(textStyle);
        }
        Cell R255Cell2 = row.createCell(4);
        if (record2.getR255_FIG_BAL_BWP2() != null) {
            R255Cell2.setCellValue(record2.getR255_FIG_BAL_BWP2().doubleValue());
            R255Cell2.setCellStyle(numberStyle);
        } else {
            R255Cell2.setCellValue("");
            R255Cell2.setCellStyle(textStyle);
        }
        Cell R255Cell3 = row.createCell(5);
        if (record2.getR255_AMT_ADJ_BWP1() != null) {
            R255Cell3.setCellValue(record2.getR255_AMT_ADJ_BWP1().doubleValue());
            R255Cell3.setCellStyle(numberStyle);
        } else {
            R255Cell3.setCellValue("");
            R255Cell3.setCellStyle(textStyle);
        }
        Cell R255Cell4 = row.createCell(6);
        if (record2.getR255_AMT_ADJ_BWP2() != null) {
            R255Cell4.setCellValue(record2.getR255_AMT_ADJ_BWP2().doubleValue());
            R255Cell4.setCellStyle(numberStyle);
        } else {
            R255Cell4.setCellValue("");
            R255Cell4.setCellStyle(textStyle);
        }
        Cell R255Cell5 = row.createCell(7);
        if (record2.getR255_NET_AMT_BWP1() != null) {
            R255Cell5.setCellValue(record2.getR255_NET_AMT_BWP1().doubleValue());
            R255Cell5.setCellStyle(numberStyle);
        } else {
            R255Cell5.setCellValue("");
            R255Cell5.setCellStyle(textStyle);
        }
        Cell R255Cell6 = row.createCell(8);
        if (record2.getR255_NET_AMT_BWP2() != null) {
            R255Cell6.setCellValue(record2.getR255_NET_AMT_BWP2().doubleValue());
            R255Cell6.setCellStyle(numberStyle);
        } else {
            R255Cell6.setCellValue("");
            R255Cell6.setCellStyle(textStyle);
        }
        Cell R255Cell7 = row.createCell(9);
        if (record2.getR255_BAL_SUB_BWP1() != null) {
            R255Cell7.setCellValue(record2.getR255_BAL_SUB_BWP1().doubleValue());
            R255Cell7.setCellStyle(numberStyle);
        } else {
            R255Cell7.setCellValue("");
            R255Cell7.setCellStyle(textStyle);
        }
        Cell R255Cell8 = row.createCell(10);
        if (record2.getR255_BAL_SUB_BWP2() != null) {
            R255Cell8.setCellValue(record2.getR255_BAL_SUB_BWP2().doubleValue());
            R255Cell8.setCellStyle(numberStyle);
        } else {
            R255Cell8.setCellValue("");
            R255Cell8.setCellStyle(textStyle);
        }
        Cell R255Cell9 = row.createCell(11);
        if (record2.getR255_BAL_ACT_SUB_BWP1() != null) {
            R255Cell9.setCellValue(record2.getR255_BAL_ACT_SUB_BWP1().doubleValue());
            R255Cell9.setCellStyle(numberStyle);
        } else {
            R255Cell9.setCellValue("");
            R255Cell9.setCellStyle(textStyle);
        }
        Cell R255Cell10 = row.createCell(12);
        if (record2.getR255_BAL_ACT_SUB_BWP2() != null) {
            R255Cell10.setCellValue(record2.getR255_BAL_ACT_SUB_BWP2().doubleValue());
            R255Cell10.setCellStyle(numberStyle);
        } else {
            R255Cell10.setCellValue("");
            R255Cell10.setCellStyle(textStyle);
        }

        /* ================= R256 ================= */
        row = sheet.getRow(255);
        Cell R256Cell1 = row.createCell(3);
        if (record2.getR256_FIG_BAL_BWP1() != null) {
            R256Cell1.setCellValue(record2.getR256_FIG_BAL_BWP1().doubleValue());
            R256Cell1.setCellStyle(numberStyle);
        } else {
            R256Cell1.setCellValue("");
            R256Cell1.setCellStyle(textStyle);
        }
        Cell R256Cell2 = row.createCell(4);
        if (record2.getR256_FIG_BAL_BWP2() != null) {
            R256Cell2.setCellValue(record2.getR256_FIG_BAL_BWP2().doubleValue());
            R256Cell2.setCellStyle(numberStyle);
        } else {
            R256Cell2.setCellValue("");
            R256Cell2.setCellStyle(textStyle);
        }
        Cell R256Cell3 = row.createCell(5);
        if (record2.getR256_AMT_ADJ_BWP1() != null) {
            R256Cell3.setCellValue(record2.getR256_AMT_ADJ_BWP1().doubleValue());
            R256Cell3.setCellStyle(numberStyle);
        } else {
            R256Cell3.setCellValue("");
            R256Cell3.setCellStyle(textStyle);
        }
        Cell R256Cell4 = row.createCell(6);
        if (record2.getR256_AMT_ADJ_BWP2() != null) {
            R256Cell4.setCellValue(record2.getR256_AMT_ADJ_BWP2().doubleValue());
            R256Cell4.setCellStyle(numberStyle);
        } else {
            R256Cell4.setCellValue("");
            R256Cell4.setCellStyle(textStyle);
        }
        Cell R256Cell5 = row.createCell(7);
        if (record2.getR256_NET_AMT_BWP1() != null) {
            R256Cell5.setCellValue(record2.getR256_NET_AMT_BWP1().doubleValue());
            R256Cell5.setCellStyle(numberStyle);
        } else {
            R256Cell5.setCellValue("");
            R256Cell5.setCellStyle(textStyle);
        }
        Cell R256Cell6 = row.createCell(8);
        if (record2.getR256_NET_AMT_BWP2() != null) {
            R256Cell6.setCellValue(record2.getR256_NET_AMT_BWP2().doubleValue());
            R256Cell6.setCellStyle(numberStyle);
        } else {
            R256Cell6.setCellValue("");
            R256Cell6.setCellStyle(textStyle);
        }
        Cell R256Cell7 = row.createCell(9);
        if (record2.getR256_BAL_SUB_BWP1() != null) {
            R256Cell7.setCellValue(record2.getR256_BAL_SUB_BWP1().doubleValue());
            R256Cell7.setCellStyle(numberStyle);
        } else {
            R256Cell7.setCellValue("");
            R256Cell7.setCellStyle(textStyle);
        }
        Cell R256Cell8 = row.createCell(10);
        if (record2.getR256_BAL_SUB_BWP2() != null) {
            R256Cell8.setCellValue(record2.getR256_BAL_SUB_BWP2().doubleValue());
            R256Cell8.setCellStyle(numberStyle);
        } else {
            R256Cell8.setCellValue("");
            R256Cell8.setCellStyle(textStyle);
        }
        Cell R256Cell9 = row.createCell(11);
        if (record2.getR256_BAL_ACT_SUB_BWP1() != null) {
            R256Cell9.setCellValue(record2.getR256_BAL_ACT_SUB_BWP1().doubleValue());
            R256Cell9.setCellStyle(numberStyle);
        } else {
            R256Cell9.setCellValue("");
            R256Cell9.setCellStyle(textStyle);
        }
        Cell R256Cell10 = row.createCell(12);
        if (record2.getR256_BAL_ACT_SUB_BWP2() != null) {
            R256Cell10.setCellValue(record2.getR256_BAL_ACT_SUB_BWP2().doubleValue());
            R256Cell10.setCellStyle(numberStyle);
        } else {
            R256Cell10.setCellValue("");
            R256Cell10.setCellStyle(textStyle);
        }

        /* ================= R257 ================= */
        row = sheet.getRow(256);
        Cell R257Cell1 = row.createCell(3);
        if (record2.getR257_FIG_BAL_BWP1() != null) {
            R257Cell1.setCellValue(record2.getR257_FIG_BAL_BWP1().doubleValue());
            R257Cell1.setCellStyle(numberStyle);
        } else {
            R257Cell1.setCellValue("");
            R257Cell1.setCellStyle(textStyle);
        }
        Cell R257Cell2 = row.createCell(4);
        if (record2.getR257_FIG_BAL_BWP2() != null) {
            R257Cell2.setCellValue(record2.getR257_FIG_BAL_BWP2().doubleValue());
            R257Cell2.setCellStyle(numberStyle);
        } else {
            R257Cell2.setCellValue("");
            R257Cell2.setCellStyle(textStyle);
        }
        Cell R257Cell3 = row.createCell(5);
        if (record2.getR257_AMT_ADJ_BWP1() != null) {
            R257Cell3.setCellValue(record2.getR257_AMT_ADJ_BWP1().doubleValue());
            R257Cell3.setCellStyle(numberStyle);
        } else {
            R257Cell3.setCellValue("");
            R257Cell3.setCellStyle(textStyle);
        }
        Cell R257Cell4 = row.createCell(6);
        if (record2.getR257_AMT_ADJ_BWP2() != null) {
            R257Cell4.setCellValue(record2.getR257_AMT_ADJ_BWP2().doubleValue());
            R257Cell4.setCellStyle(numberStyle);
        } else {
            R257Cell4.setCellValue("");
            R257Cell4.setCellStyle(textStyle);
        }
        Cell R257Cell5 = row.createCell(7);
        if (record2.getR257_NET_AMT_BWP1() != null) {
            R257Cell5.setCellValue(record2.getR257_NET_AMT_BWP1().doubleValue());
            R257Cell5.setCellStyle(numberStyle);
        } else {
            R257Cell5.setCellValue("");
            R257Cell5.setCellStyle(textStyle);
        }
        Cell R257Cell6 = row.createCell(8);
        if (record2.getR257_NET_AMT_BWP2() != null) {
            R257Cell6.setCellValue(record2.getR257_NET_AMT_BWP2().doubleValue());
            R257Cell6.setCellStyle(numberStyle);
        } else {
            R257Cell6.setCellValue("");
            R257Cell6.setCellStyle(textStyle);
        }
        Cell R257Cell7 = row.createCell(9);
        if (record2.getR257_BAL_SUB_BWP1() != null) {
            R257Cell7.setCellValue(record2.getR257_BAL_SUB_BWP1().doubleValue());
            R257Cell7.setCellStyle(numberStyle);
        } else {
            R257Cell7.setCellValue("");
            R257Cell7.setCellStyle(textStyle);
        }
        Cell R257Cell8 = row.createCell(10);
        if (record2.getR257_BAL_SUB_BWP2() != null) {
            R257Cell8.setCellValue(record2.getR257_BAL_SUB_BWP2().doubleValue());
            R257Cell8.setCellStyle(numberStyle);
        } else {
            R257Cell8.setCellValue("");
            R257Cell8.setCellStyle(textStyle);
        }
        Cell R257Cell9 = row.createCell(11);
        if (record2.getR257_BAL_ACT_SUB_BWP1() != null) {
            R257Cell9.setCellValue(record2.getR257_BAL_ACT_SUB_BWP1().doubleValue());
            R257Cell9.setCellStyle(numberStyle);
        } else {
            R257Cell9.setCellValue("");
            R257Cell9.setCellStyle(textStyle);
        }
        Cell R257Cell10 = row.createCell(12);
        if (record2.getR257_BAL_ACT_SUB_BWP2() != null) {
            R257Cell10.setCellValue(record2.getR257_BAL_ACT_SUB_BWP2().doubleValue());
            R257Cell10.setCellStyle(numberStyle);
        } else {
            R257Cell10.setCellValue("");
            R257Cell10.setCellStyle(textStyle);
        }

        /* ================= R258 ================= */
        row = sheet.getRow(257);
        Cell R258Cell1 = row.createCell(3);
        if (record2.getR258_FIG_BAL_BWP1() != null) {
            R258Cell1.setCellValue(record2.getR258_FIG_BAL_BWP1().doubleValue());
            R258Cell1.setCellStyle(numberStyle);
        } else {
            R258Cell1.setCellValue("");
            R258Cell1.setCellStyle(textStyle);
        }
        Cell R258Cell2 = row.createCell(4);
        if (record2.getR258_FIG_BAL_BWP2() != null) {
            R258Cell2.setCellValue(record2.getR258_FIG_BAL_BWP2().doubleValue());
            R258Cell2.setCellStyle(numberStyle);
        } else {
            R258Cell2.setCellValue("");
            R258Cell2.setCellStyle(textStyle);
        }
        Cell R258Cell3 = row.createCell(5);
        if (record2.getR258_AMT_ADJ_BWP1() != null) {
            R258Cell3.setCellValue(record2.getR258_AMT_ADJ_BWP1().doubleValue());
            R258Cell3.setCellStyle(numberStyle);
        } else {
            R258Cell3.setCellValue("");
            R258Cell3.setCellStyle(textStyle);
        }
        Cell R258Cell4 = row.createCell(6);
        if (record2.getR258_AMT_ADJ_BWP2() != null) {
            R258Cell4.setCellValue(record2.getR258_AMT_ADJ_BWP2().doubleValue());
            R258Cell4.setCellStyle(numberStyle);
        } else {
            R258Cell4.setCellValue("");
            R258Cell4.setCellStyle(textStyle);
        }
        Cell R258Cell5 = row.createCell(7);
        if (record2.getR258_NET_AMT_BWP1() != null) {
            R258Cell5.setCellValue(record2.getR258_NET_AMT_BWP1().doubleValue());
            R258Cell5.setCellStyle(numberStyle);
        } else {
            R258Cell5.setCellValue("");
            R258Cell5.setCellStyle(textStyle);
        }
        Cell R258Cell6 = row.createCell(8);
        if (record2.getR258_NET_AMT_BWP2() != null) {
            R258Cell6.setCellValue(record2.getR258_NET_AMT_BWP2().doubleValue());
            R258Cell6.setCellStyle(numberStyle);
        } else {
            R258Cell6.setCellValue("");
            R258Cell6.setCellStyle(textStyle);
        }
        Cell R258Cell7 = row.createCell(9);
        if (record2.getR258_BAL_SUB_BWP1() != null) {
            R258Cell7.setCellValue(record2.getR258_BAL_SUB_BWP1().doubleValue());
            R258Cell7.setCellStyle(numberStyle);
        } else {
            R258Cell7.setCellValue("");
            R258Cell7.setCellStyle(textStyle);
        }
        Cell R258Cell8 = row.createCell(10);
        if (record2.getR258_BAL_SUB_BWP2() != null) {
            R258Cell8.setCellValue(record2.getR258_BAL_SUB_BWP2().doubleValue());
            R258Cell8.setCellStyle(numberStyle);
        } else {
            R258Cell8.setCellValue("");
            R258Cell8.setCellStyle(textStyle);
        }
        Cell R258Cell9 = row.createCell(11);
        if (record2.getR258_BAL_ACT_SUB_BWP1() != null) {
            R258Cell9.setCellValue(record2.getR258_BAL_ACT_SUB_BWP1().doubleValue());
            R258Cell9.setCellStyle(numberStyle);
        } else {
            R258Cell9.setCellValue("");
            R258Cell9.setCellStyle(textStyle);
        }
        Cell R258Cell10 = row.createCell(12);
        if (record2.getR258_BAL_ACT_SUB_BWP2() != null) {
            R258Cell10.setCellValue(record2.getR258_BAL_ACT_SUB_BWP2().doubleValue());
            R258Cell10.setCellStyle(numberStyle);
        } else {
            R258Cell10.setCellValue("");
            R258Cell10.setCellStyle(textStyle);
        }

        /* ================= R259 ================= */
        row = sheet.getRow(258);
        Cell R259Cell1 = row.createCell(3);
        if (record2.getR259_FIG_BAL_BWP1() != null) {
            R259Cell1.setCellValue(record2.getR259_FIG_BAL_BWP1().doubleValue());
            R259Cell1.setCellStyle(numberStyle);
        } else {
            R259Cell1.setCellValue("");
            R259Cell1.setCellStyle(textStyle);
        }
        Cell R259Cell2 = row.createCell(4);
        if (record2.getR259_FIG_BAL_BWP2() != null) {
            R259Cell2.setCellValue(record2.getR259_FIG_BAL_BWP2().doubleValue());
            R259Cell2.setCellStyle(numberStyle);
        } else {
            R259Cell2.setCellValue("");
            R259Cell2.setCellStyle(textStyle);
        }
        Cell R259Cell3 = row.createCell(5);
        if (record2.getR259_AMT_ADJ_BWP1() != null) {
            R259Cell3.setCellValue(record2.getR259_AMT_ADJ_BWP1().doubleValue());
            R259Cell3.setCellStyle(numberStyle);
        } else {
            R259Cell3.setCellValue("");
            R259Cell3.setCellStyle(textStyle);
        }
        Cell R259Cell4 = row.createCell(6);
        if (record2.getR259_AMT_ADJ_BWP2() != null) {
            R259Cell4.setCellValue(record2.getR259_AMT_ADJ_BWP2().doubleValue());
            R259Cell4.setCellStyle(numberStyle);
        } else {
            R259Cell4.setCellValue("");
            R259Cell4.setCellStyle(textStyle);
        }
        Cell R259Cell5 = row.createCell(7);
        if (record2.getR259_NET_AMT_BWP1() != null) {
            R259Cell5.setCellValue(record2.getR259_NET_AMT_BWP1().doubleValue());
            R259Cell5.setCellStyle(numberStyle);
        } else {
            R259Cell5.setCellValue("");
            R259Cell5.setCellStyle(textStyle);
        }
        Cell R259Cell6 = row.createCell(8);
        if (record2.getR259_NET_AMT_BWP2() != null) {
            R259Cell6.setCellValue(record2.getR259_NET_AMT_BWP2().doubleValue());
            R259Cell6.setCellStyle(numberStyle);
        } else {
            R259Cell6.setCellValue("");
            R259Cell6.setCellStyle(textStyle);
        }
        Cell R259Cell7 = row.createCell(9);
        if (record2.getR259_BAL_SUB_BWP1() != null) {
            R259Cell7.setCellValue(record2.getR259_BAL_SUB_BWP1().doubleValue());
            R259Cell7.setCellStyle(numberStyle);
        } else {
            R259Cell7.setCellValue("");
            R259Cell7.setCellStyle(textStyle);
        }
        Cell R259Cell8 = row.createCell(10);
        if (record2.getR259_BAL_SUB_BWP2() != null) {
            R259Cell8.setCellValue(record2.getR259_BAL_SUB_BWP2().doubleValue());
            R259Cell8.setCellStyle(numberStyle);
        } else {
            R259Cell8.setCellValue("");
            R259Cell8.setCellStyle(textStyle);
        }
        Cell R259Cell9 = row.createCell(11);
        if (record2.getR259_BAL_ACT_SUB_BWP1() != null) {
            R259Cell9.setCellValue(record2.getR259_BAL_ACT_SUB_BWP1().doubleValue());
            R259Cell9.setCellStyle(numberStyle);
        } else {
            R259Cell9.setCellValue("");
            R259Cell9.setCellStyle(textStyle);
        }
        Cell R259Cell10 = row.createCell(12);
        if (record2.getR259_BAL_ACT_SUB_BWP2() != null) {
            R259Cell10.setCellValue(record2.getR259_BAL_ACT_SUB_BWP2().doubleValue());
            R259Cell10.setCellStyle(numberStyle);
        } else {
            R259Cell10.setCellValue("");
            R259Cell10.setCellStyle(textStyle);
        }
        row = sheet.getRow(259);
        /* ================= R260 ================= */
        Cell R260Cell1 = row.createCell(3);
        if (record2.getR260_FIG_BAL_BWP1() != null) {
            R260Cell1.setCellValue(record2.getR260_FIG_BAL_BWP1().doubleValue());
            R260Cell1.setCellStyle(numberStyle);
        } else {
            R260Cell1.setCellValue("");
            R260Cell1.setCellStyle(textStyle);
        }
        Cell R260Cell2 = row.createCell(4);
        if (record2.getR260_FIG_BAL_BWP2() != null) {
            R260Cell2.setCellValue(record2.getR260_FIG_BAL_BWP2().doubleValue());
            R260Cell2.setCellStyle(numberStyle);
        } else {
            R260Cell2.setCellValue("");
            R260Cell2.setCellStyle(textStyle);
        }
        Cell R260Cell3 = row.createCell(5);
        if (record2.getR260_AMT_ADJ_BWP1() != null) {
            R260Cell3.setCellValue(record2.getR260_AMT_ADJ_BWP1().doubleValue());
            R260Cell3.setCellStyle(numberStyle);
        } else {
            R260Cell3.setCellValue("");
            R260Cell3.setCellStyle(textStyle);
        }
        Cell R260Cell4 = row.createCell(6);
        if (record2.getR260_AMT_ADJ_BWP2() != null) {
            R260Cell4.setCellValue(record2.getR260_AMT_ADJ_BWP2().doubleValue());
            R260Cell4.setCellStyle(numberStyle);
        } else {
            R260Cell4.setCellValue("");
            R260Cell4.setCellStyle(textStyle);
        }
        Cell R260Cell5 = row.createCell(7);
        if (record2.getR260_NET_AMT_BWP1() != null) {
            R260Cell5.setCellValue(record2.getR260_NET_AMT_BWP1().doubleValue());
            R260Cell5.setCellStyle(numberStyle);
        } else {
            R260Cell5.setCellValue("");
            R260Cell5.setCellStyle(textStyle);
        }
        Cell R260Cell6 = row.createCell(8);
        if (record2.getR260_NET_AMT_BWP2() != null) {
            R260Cell6.setCellValue(record2.getR260_NET_AMT_BWP2().doubleValue());
            R260Cell6.setCellStyle(numberStyle);
        } else {
            R260Cell6.setCellValue("");
            R260Cell6.setCellStyle(textStyle);
        }
        Cell R260Cell7 = row.createCell(9);
        if (record2.getR260_BAL_SUB_BWP1() != null) {
            R260Cell7.setCellValue(record2.getR260_BAL_SUB_BWP1().doubleValue());
            R260Cell7.setCellStyle(numberStyle);
        } else {
            R260Cell7.setCellValue("");
            R260Cell7.setCellStyle(textStyle);
        }
        Cell R260Cell8 = row.createCell(10);
        if (record2.getR260_BAL_SUB_BWP2() != null) {
            R260Cell8.setCellValue(record2.getR260_BAL_SUB_BWP2().doubleValue());
            R260Cell8.setCellStyle(numberStyle);
        } else {
            R260Cell8.setCellValue("");
            R260Cell8.setCellStyle(textStyle);
        }
        Cell R260Cell9 = row.createCell(11);
        if (record2.getR260_BAL_ACT_SUB_BWP1() != null) {
            R260Cell9.setCellValue(record2.getR260_BAL_ACT_SUB_BWP1().doubleValue());
            R260Cell9.setCellStyle(numberStyle);
        } else {
            R260Cell9.setCellValue("");
            R260Cell9.setCellStyle(textStyle);
        }
        Cell R260Cell10 = row.createCell(12);
        if (record2.getR260_BAL_ACT_SUB_BWP2() != null) {
            R260Cell10.setCellValue(record2.getR260_BAL_ACT_SUB_BWP2().doubleValue());
            R260Cell10.setCellStyle(numberStyle);
        } else {
            R260Cell10.setCellValue("");
            R260Cell10.setCellStyle(textStyle);
        }
    }

    private void populateEntity4Data(Sheet sheet, GL_SCH_Manual_Summary_Entity record3, CellStyle textStyle,
            CellStyle numberStyle) {

        Row row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);
        /* ================= R61 ================= */
        row = sheet.getRow(60);
        Cell R61Cell1 = row.createCell(3);
        if (record3.getR61_FIG_BAL_BWP1() != null) {
            R61Cell1.setCellValue(record3.getR61_FIG_BAL_BWP1().doubleValue());
            R61Cell1.setCellStyle(numberStyle);
        } else {
            R61Cell1.setCellValue("");
            R61Cell1.setCellStyle(textStyle);
        }
        Cell R61Cell2 = row.createCell(4);
        if (record3.getR61_FIG_BAL_BWP2() != null) {
            R61Cell2.setCellValue(record3.getR61_FIG_BAL_BWP2().doubleValue());
            R61Cell2.setCellStyle(numberStyle);
        } else {
            R61Cell2.setCellValue("");
            R61Cell2.setCellStyle(textStyle);
        }
        Cell R61Cell3 = row.createCell(5);
        if (record3.getR61_AMT_ADJ_BWP1() != null) {
            R61Cell3.setCellValue(record3.getR61_AMT_ADJ_BWP1().doubleValue());
            R61Cell3.setCellStyle(numberStyle);
        } else {
            R61Cell3.setCellValue("");
            R61Cell3.setCellStyle(textStyle);
        }
        Cell R61Cell4 = row.createCell(6);
        if (record3.getR61_AMT_ADJ_BWP2() != null) {
            R61Cell4.setCellValue(record3.getR61_AMT_ADJ_BWP2().doubleValue());
            R61Cell4.setCellStyle(numberStyle);
        } else {
            R61Cell4.setCellValue("");
            R61Cell4.setCellStyle(textStyle);
        }
        Cell R61Cell5 = row.createCell(7);
        if (record3.getR61_NET_AMT_BWP1() != null) {
            R61Cell5.setCellValue(record3.getR61_NET_AMT_BWP1().doubleValue());
            R61Cell5.setCellStyle(numberStyle);
        } else {
            R61Cell5.setCellValue("");
            R61Cell5.setCellStyle(textStyle);
        }
        Cell R61Cell6 = row.createCell(8);
        if (record3.getR61_NET_AMT_BWP2() != null) {
            R61Cell6.setCellValue(record3.getR61_NET_AMT_BWP2().doubleValue());
            R61Cell6.setCellStyle(numberStyle);
        } else {
            R61Cell6.setCellValue("");
            R61Cell6.setCellStyle(textStyle);
        }
        Cell R61Cell7 = row.createCell(9);
        if (record3.getR61_BAL_SUB_BWP1() != null) {
            R61Cell7.setCellValue(record3.getR61_BAL_SUB_BWP1().doubleValue());
            R61Cell7.setCellStyle(numberStyle);
        } else {
            R61Cell7.setCellValue("");
            R61Cell7.setCellStyle(textStyle);
        }
        Cell R61Cell8 = row.createCell(10);
        if (record3.getR61_BAL_SUB_BWP2() != null) {
            R61Cell8.setCellValue(record3.getR61_BAL_SUB_BWP2().doubleValue());
            R61Cell8.setCellStyle(numberStyle);
        } else {
            R61Cell8.setCellValue("");
            R61Cell8.setCellStyle(textStyle);
        }
        Cell R61Cell9 = row.createCell(11);
        if (record3.getR61_BAL_ACT_SUB_BWP1() != null) {
            R61Cell9.setCellValue(record3.getR61_BAL_ACT_SUB_BWP1().doubleValue());
            R61Cell9.setCellStyle(numberStyle);
        } else {
            R61Cell9.setCellValue("");
            R61Cell9.setCellStyle(textStyle);
        }
        Cell R61Cell10 = row.createCell(12);
        if (record3.getR61_BAL_ACT_SUB_BWP2() != null) {
            R61Cell10.setCellValue(record3.getR61_BAL_ACT_SUB_BWP2().doubleValue());
            R61Cell10.setCellStyle(numberStyle);
        } else {
            R61Cell10.setCellValue("");
            R61Cell10.setCellStyle(textStyle);
        }

        /* ================= R103 ================= */
        row = sheet.getRow(102);
        Cell R103Cell1 = row.createCell(3);
        if (record3.getR103_FIG_BAL_BWP1() != null) {
            R103Cell1.setCellValue(record3.getR103_FIG_BAL_BWP1().doubleValue());
            R103Cell1.setCellStyle(numberStyle);
        } else {
            R103Cell1.setCellValue("");
            R103Cell1.setCellStyle(textStyle);
        }
        Cell R103Cell2 = row.createCell(4);
        if (record3.getR103_FIG_BAL_BWP2() != null) {
            R103Cell2.setCellValue(record3.getR103_FIG_BAL_BWP2().doubleValue());
            R103Cell2.setCellStyle(numberStyle);
        } else {
            R103Cell2.setCellValue("");
            R103Cell2.setCellStyle(textStyle);
        }
        Cell R103Cell3 = row.createCell(5);
        if (record3.getR103_AMT_ADJ_BWP1() != null) {
            R103Cell3.setCellValue(record3.getR103_AMT_ADJ_BWP1().doubleValue());
            R103Cell3.setCellStyle(numberStyle);
        } else {
            R103Cell3.setCellValue("");
            R103Cell3.setCellStyle(textStyle);
        }
        Cell R103Cell4 = row.createCell(6);
        if (record3.getR103_AMT_ADJ_BWP2() != null) {
            R103Cell4.setCellValue(record3.getR103_AMT_ADJ_BWP2().doubleValue());
            R103Cell4.setCellStyle(numberStyle);
        } else {
            R103Cell4.setCellValue("");
            R103Cell4.setCellStyle(textStyle);
        }
        Cell R103Cell5 = row.createCell(7);
        if (record3.getR103_NET_AMT_BWP1() != null) {
            R103Cell5.setCellValue(record3.getR103_NET_AMT_BWP1().doubleValue());
            R103Cell5.setCellStyle(numberStyle);
        } else {
            R103Cell5.setCellValue("");
            R103Cell5.setCellStyle(textStyle);
        }
        Cell R103Cell6 = row.createCell(8);
        if (record3.getR103_NET_AMT_BWP2() != null) {
            R103Cell6.setCellValue(record3.getR103_NET_AMT_BWP2().doubleValue());
            R103Cell6.setCellStyle(numberStyle);
        } else {
            R103Cell6.setCellValue("");
            R103Cell6.setCellStyle(textStyle);
        }
        Cell R103Cell7 = row.createCell(9);
        if (record3.getR103_BAL_SUB_BWP1() != null) {
            R103Cell7.setCellValue(record3.getR103_BAL_SUB_BWP1().doubleValue());
            R103Cell7.setCellStyle(numberStyle);
        } else {
            R103Cell7.setCellValue("");
            R103Cell7.setCellStyle(textStyle);
        }
        Cell R103Cell8 = row.createCell(10);
        if (record3.getR103_BAL_SUB_BWP2() != null) {
            R103Cell8.setCellValue(record3.getR103_BAL_SUB_BWP2().doubleValue());
            R103Cell8.setCellStyle(numberStyle);
        } else {
            R103Cell8.setCellValue("");
            R103Cell8.setCellStyle(textStyle);
        }
        Cell R103Cell9 = row.createCell(11);
        if (record3.getR103_BAL_ACT_SUB_BWP1() != null) {
            R103Cell9.setCellValue(record3.getR103_BAL_ACT_SUB_BWP1().doubleValue());
            R103Cell9.setCellStyle(numberStyle);
        } else {
            R103Cell9.setCellValue("");
            R103Cell9.setCellStyle(textStyle);
        }
        Cell R103Cell10 = row.createCell(12);
        if (record3.getR103_BAL_ACT_SUB_BWP2() != null) {
            R103Cell10.setCellValue(record3.getR103_BAL_ACT_SUB_BWP2().doubleValue());
            R103Cell10.setCellStyle(numberStyle);
        } else {
            R103Cell10.setCellValue("");
            R103Cell10.setCellStyle(textStyle);
        }
        /* ================= R130 ================= */
        row = sheet.getRow(129);
        Cell R130Cell1 = row.createCell(3);
        if (record3.getR130_FIG_BAL_BWP1() != null) {
            R130Cell1.setCellValue(record3.getR130_FIG_BAL_BWP1().doubleValue());
            R130Cell1.setCellStyle(numberStyle);
        } else {
            R130Cell1.setCellValue("");
            R130Cell1.setCellStyle(textStyle);
        }
        Cell R130Cell2 = row.createCell(4);
        if (record3.getR130_FIG_BAL_BWP2() != null) {
            R130Cell2.setCellValue(record3.getR130_FIG_BAL_BWP2().doubleValue());
            R130Cell2.setCellStyle(numberStyle);
        } else {
            R130Cell2.setCellValue("");
            R130Cell2.setCellStyle(textStyle);
        }
        Cell R130Cell3 = row.createCell(5);
        if (record3.getR130_AMT_ADJ_BWP1() != null) {
            R130Cell3.setCellValue(record3.getR130_AMT_ADJ_BWP1().doubleValue());
            R130Cell3.setCellStyle(numberStyle);
        } else {
            R130Cell3.setCellValue("");
            R130Cell3.setCellStyle(textStyle);
        }
        Cell R130Cell4 = row.createCell(6);
        if (record3.getR130_AMT_ADJ_BWP2() != null) {
            R130Cell4.setCellValue(record3.getR130_AMT_ADJ_BWP2().doubleValue());
            R130Cell4.setCellStyle(numberStyle);
        } else {
            R130Cell4.setCellValue("");
            R130Cell4.setCellStyle(textStyle);
        }
        Cell R130Cell5 = row.createCell(7);
        if (record3.getR130_NET_AMT_BWP1() != null) {
            R130Cell5.setCellValue(record3.getR130_NET_AMT_BWP1().doubleValue());
            R130Cell5.setCellStyle(numberStyle);
        } else {
            R130Cell5.setCellValue("");
            R130Cell5.setCellStyle(textStyle);
        }
        Cell R130Cell6 = row.createCell(8);
        if (record3.getR130_NET_AMT_BWP2() != null) {
            R130Cell6.setCellValue(record3.getR130_NET_AMT_BWP2().doubleValue());
            R130Cell6.setCellStyle(numberStyle);
        } else {
            R130Cell6.setCellValue("");
            R130Cell6.setCellStyle(textStyle);
        }
        Cell R130Cell7 = row.createCell(9);
        if (record3.getR130_BAL_SUB_BWP1() != null) {
            R130Cell7.setCellValue(record3.getR130_BAL_SUB_BWP1().doubleValue());
            R130Cell7.setCellStyle(numberStyle);
        } else {
            R130Cell7.setCellValue("");
            R130Cell7.setCellStyle(textStyle);
        }
        Cell R130Cell8 = row.createCell(10);
        if (record3.getR130_BAL_SUB_BWP2() != null) {
            R130Cell8.setCellValue(record3.getR130_BAL_SUB_BWP2().doubleValue());
            R130Cell8.setCellStyle(numberStyle);
        } else {
            R130Cell8.setCellValue("");
            R130Cell8.setCellStyle(textStyle);
        }
        Cell R130Cell9 = row.createCell(11);
        if (record3.getR130_BAL_ACT_SUB_BWP1() != null) {
            R130Cell9.setCellValue(record3.getR130_BAL_ACT_SUB_BWP1().doubleValue());
            R130Cell9.setCellStyle(numberStyle);
        } else {
            R130Cell9.setCellValue("");
            R130Cell9.setCellStyle(textStyle);
        }
        Cell R130Cell10 = row.createCell(12);
        if (record3.getR130_BAL_ACT_SUB_BWP2() != null) {
            R130Cell10.setCellValue(record3.getR130_BAL_ACT_SUB_BWP2().doubleValue());
            R130Cell10.setCellStyle(numberStyle);
        } else {
            R130Cell10.setCellValue("");
            R130Cell10.setCellStyle(textStyle);
        }
        /* ================= R139 ================= */
        row = sheet.getRow(138);
        Cell R139Cell1 = row.createCell(3);
        if (record3.getR139_FIG_BAL_BWP1() != null) {
            R139Cell1.setCellValue(record3.getR139_FIG_BAL_BWP1().doubleValue());
            R139Cell1.setCellStyle(numberStyle);
        } else {
            R139Cell1.setCellValue("");
            R139Cell1.setCellStyle(textStyle);
        }
        Cell R139Cell2 = row.createCell(4);
        if (record3.getR139_FIG_BAL_BWP2() != null) {
            R139Cell2.setCellValue(record3.getR139_FIG_BAL_BWP2().doubleValue());
            R139Cell2.setCellStyle(numberStyle);
        } else {
            R139Cell2.setCellValue("");
            R139Cell2.setCellStyle(textStyle);
        }
        Cell R139Cell3 = row.createCell(5);
        if (record3.getR139_AMT_ADJ_BWP1() != null) {
            R139Cell3.setCellValue(record3.getR139_AMT_ADJ_BWP1().doubleValue());
            R139Cell3.setCellStyle(numberStyle);
        } else {
            R139Cell3.setCellValue("");
            R139Cell3.setCellStyle(textStyle);
        }
        Cell R139Cell4 = row.createCell(6);
        if (record3.getR139_AMT_ADJ_BWP2() != null) {
            R139Cell4.setCellValue(record3.getR139_AMT_ADJ_BWP2().doubleValue());
            R139Cell4.setCellStyle(numberStyle);
        } else {
            R139Cell4.setCellValue("");
            R139Cell4.setCellStyle(textStyle);
        }
        Cell R139Cell5 = row.createCell(7);
        if (record3.getR139_NET_AMT_BWP1() != null) {
            R139Cell5.setCellValue(record3.getR139_NET_AMT_BWP1().doubleValue());
            R139Cell5.setCellStyle(numberStyle);
        } else {
            R139Cell5.setCellValue("");
            R139Cell5.setCellStyle(textStyle);
        }
        Cell R139Cell6 = row.createCell(8);
        if (record3.getR139_NET_AMT_BWP2() != null) {
            R139Cell6.setCellValue(record3.getR139_NET_AMT_BWP2().doubleValue());
            R139Cell6.setCellStyle(numberStyle);
        } else {
            R139Cell6.setCellValue("");
            R139Cell6.setCellStyle(textStyle);
        }
        Cell R139Cell7 = row.createCell(9);
        if (record3.getR139_BAL_SUB_BWP1() != null) {
            R139Cell7.setCellValue(record3.getR139_BAL_SUB_BWP1().doubleValue());
            R139Cell7.setCellStyle(numberStyle);
        } else {
            R139Cell7.setCellValue("");
            R139Cell7.setCellStyle(textStyle);
        }
        Cell R139Cell8 = row.createCell(10);
        if (record3.getR139_BAL_SUB_BWP2() != null) {
            R139Cell8.setCellValue(record3.getR139_BAL_SUB_BWP2().doubleValue());
            R139Cell8.setCellStyle(numberStyle);
        } else {
            R139Cell8.setCellValue("");
            R139Cell8.setCellStyle(textStyle);
        }
        Cell R139Cell9 = row.createCell(11);
        if (record3.getR139_BAL_ACT_SUB_BWP1() != null) {
            R139Cell9.setCellValue(record3.getR139_BAL_ACT_SUB_BWP1().doubleValue());
            R139Cell9.setCellStyle(numberStyle);
        } else {
            R139Cell9.setCellValue("");
            R139Cell9.setCellStyle(textStyle);
        }
        Cell R139Cell10 = row.createCell(12);
        if (record3.getR139_BAL_ACT_SUB_BWP2() != null) {
            R139Cell10.setCellValue(record3.getR139_BAL_ACT_SUB_BWP2().doubleValue());
            R139Cell10.setCellStyle(numberStyle);
        } else {
            R139Cell10.setCellValue("");
            R139Cell10.setCellStyle(textStyle);
        }
        /* ================= R241 ================= */
        row = sheet.getRow(240);
        /* ================= R241 ================= */
        Cell R241Cell1 = row.createCell(3);
        if (record3.getR241_FIG_BAL_BWP1() != null) {
            R241Cell1.setCellValue(record3.getR241_FIG_BAL_BWP1().doubleValue());
            R241Cell1.setCellStyle(numberStyle);
        } else {
            R241Cell1.setCellValue("");
            R241Cell1.setCellStyle(textStyle);
        }
        Cell R241Cell2 = row.createCell(4);
        if (record3.getR241_FIG_BAL_BWP2() != null) {
            R241Cell2.setCellValue(record3.getR241_FIG_BAL_BWP2().doubleValue());
            R241Cell2.setCellStyle(numberStyle);
        } else {
            R241Cell2.setCellValue("");
            R241Cell2.setCellStyle(textStyle);
        }
        Cell R241Cell3 = row.createCell(5);
        if (record3.getR241_AMT_ADJ_BWP1() != null) {
            R241Cell3.setCellValue(record3.getR241_AMT_ADJ_BWP1().doubleValue());
            R241Cell3.setCellStyle(numberStyle);
        } else {
            R241Cell3.setCellValue("");
            R241Cell3.setCellStyle(textStyle);
        }
        Cell R241Cell4 = row.createCell(6);
        if (record3.getR241_AMT_ADJ_BWP2() != null) {
            R241Cell4.setCellValue(record3.getR241_AMT_ADJ_BWP2().doubleValue());
            R241Cell4.setCellStyle(numberStyle);
        } else {
            R241Cell4.setCellValue("");
            R241Cell4.setCellStyle(textStyle);
        }
        Cell R241Cell5 = row.createCell(7);
        if (record3.getR241_NET_AMT_BWP1() != null) {
            R241Cell5.setCellValue(record3.getR241_NET_AMT_BWP1().doubleValue());
            R241Cell5.setCellStyle(numberStyle);
        } else {
            R241Cell5.setCellValue("");
            R241Cell5.setCellStyle(textStyle);
        }
        Cell R241Cell6 = row.createCell(8);
        if (record3.getR241_NET_AMT_BWP2() != null) {
            R241Cell6.setCellValue(record3.getR241_NET_AMT_BWP2().doubleValue());
            R241Cell6.setCellStyle(numberStyle);
        } else {
            R241Cell6.setCellValue("");
            R241Cell6.setCellStyle(textStyle);
        }
        Cell R241Cell7 = row.createCell(9);
        if (record3.getR241_BAL_SUB_BWP1() != null) {
            R241Cell7.setCellValue(record3.getR241_BAL_SUB_BWP1().doubleValue());
            R241Cell7.setCellStyle(numberStyle);
        } else {
            R241Cell7.setCellValue("");
            R241Cell7.setCellStyle(textStyle);
        }
        Cell R241Cell8 = row.createCell(10);
        if (record3.getR241_BAL_SUB_BWP2() != null) {
            R241Cell8.setCellValue(record3.getR241_BAL_SUB_BWP2().doubleValue());
            R241Cell8.setCellStyle(numberStyle);
        } else {
            R241Cell8.setCellValue("");
            R241Cell8.setCellStyle(textStyle);
        }
        Cell R241Cell9 = row.createCell(11);
        if (record3.getR241_BAL_ACT_SUB_BWP1() != null) {
            R241Cell9.setCellValue(record3.getR241_BAL_ACT_SUB_BWP1().doubleValue());
            R241Cell9.setCellStyle(numberStyle);
        } else {
            R241Cell9.setCellValue("");
            R241Cell9.setCellStyle(textStyle);
        }
        Cell R241Cell10 = row.createCell(12);
        if (record3.getR241_BAL_ACT_SUB_BWP2() != null) {
            R241Cell10.setCellValue(record3.getR241_BAL_ACT_SUB_BWP2().doubleValue());
            R241Cell10.setCellStyle(numberStyle);
        } else {
            R241Cell10.setCellValue("");
            R241Cell10.setCellStyle(textStyle);
        }
        /* ================= R243 ================= */
        row = sheet.getRow(242);
        /* ================= R243 ================= */
        Cell R243Cell1 = row.createCell(3);
        if (record3.getR243_FIG_BAL_BWP1() != null) {
            R243Cell1.setCellValue(record3.getR243_FIG_BAL_BWP1().doubleValue());
            R243Cell1.setCellStyle(numberStyle);
        } else {
            R243Cell1.setCellValue("");
            R243Cell1.setCellStyle(textStyle);
        }
        Cell R243Cell2 = row.createCell(4);
        if (record3.getR243_FIG_BAL_BWP2() != null) {
            R243Cell2.setCellValue(record3.getR243_FIG_BAL_BWP2().doubleValue());
            R243Cell2.setCellStyle(numberStyle);
        } else {
            R243Cell2.setCellValue("");
            R243Cell2.setCellStyle(textStyle);
        }
        Cell R243Cell3 = row.createCell(5);
        if (record3.getR243_AMT_ADJ_BWP1() != null) {
            R243Cell3.setCellValue(record3.getR243_AMT_ADJ_BWP1().doubleValue());
            R243Cell3.setCellStyle(numberStyle);
        } else {
            R243Cell3.setCellValue("");
            R243Cell3.setCellStyle(textStyle);
        }
        Cell R243Cell4 = row.createCell(6);
        if (record3.getR243_AMT_ADJ_BWP2() != null) {
            R243Cell4.setCellValue(record3.getR243_AMT_ADJ_BWP2().doubleValue());
            R243Cell4.setCellStyle(numberStyle);
        } else {
            R243Cell4.setCellValue("");
            R243Cell4.setCellStyle(textStyle);
        }
        Cell R243Cell5 = row.createCell(7);
        if (record3.getR243_NET_AMT_BWP1() != null) {
            R243Cell5.setCellValue(record3.getR243_NET_AMT_BWP1().doubleValue());
            R243Cell5.setCellStyle(numberStyle);
        } else {
            R243Cell5.setCellValue("");
            R243Cell5.setCellStyle(textStyle);
        }
        Cell R243Cell6 = row.createCell(8);
        if (record3.getR243_NET_AMT_BWP2() != null) {
            R243Cell6.setCellValue(record3.getR243_NET_AMT_BWP2().doubleValue());
            R243Cell6.setCellStyle(numberStyle);
        } else {
            R243Cell6.setCellValue("");
            R243Cell6.setCellStyle(textStyle);
        }
        Cell R243Cell7 = row.createCell(9);
        if (record3.getR243_BAL_SUB_BWP1() != null) {
            R243Cell7.setCellValue(record3.getR243_BAL_SUB_BWP1().doubleValue());
            R243Cell7.setCellStyle(numberStyle);
        } else {
            R243Cell7.setCellValue("");
            R243Cell7.setCellStyle(textStyle);
        }
        Cell R243Cell8 = row.createCell(10);
        if (record3.getR243_BAL_SUB_BWP2() != null) {
            R243Cell8.setCellValue(record3.getR243_BAL_SUB_BWP2().doubleValue());
            R243Cell8.setCellStyle(numberStyle);
        } else {
            R243Cell8.setCellValue("");
            R243Cell8.setCellStyle(textStyle);
        }
        Cell R243Cell9 = row.createCell(11);
        if (record3.getR243_BAL_ACT_SUB_BWP1() != null) {
            R243Cell9.setCellValue(record3.getR243_BAL_ACT_SUB_BWP1().doubleValue());
            R243Cell9.setCellStyle(numberStyle);
        } else {
            R243Cell9.setCellValue("");
            R243Cell9.setCellStyle(textStyle);
        }
        Cell R243Cell10 = row.createCell(12);
        if (record3.getR243_BAL_ACT_SUB_BWP2() != null) {
            R243Cell10.setCellValue(record3.getR243_BAL_ACT_SUB_BWP2().doubleValue());
            R243Cell10.setCellStyle(numberStyle);
        } else {
            R243Cell10.setCellValue("");
            R243Cell10.setCellStyle(textStyle);
        }
        row = sheet.getRow(244);
        /* ================= R245 ================= */
        Cell R245Cell1 = row.createCell(3);
        if (record3.getR245_FIG_BAL_BWP1() != null) {
            R245Cell1.setCellValue(record3.getR245_FIG_BAL_BWP1().doubleValue());
            R245Cell1.setCellStyle(numberStyle);
        } else {
            R245Cell1.setCellValue("");
            R245Cell1.setCellStyle(textStyle);
        }
        Cell R245Cell2 = row.createCell(4);
        if (record3.getR245_FIG_BAL_BWP2() != null) {
            R245Cell2.setCellValue(record3.getR245_FIG_BAL_BWP2().doubleValue());
            R245Cell2.setCellStyle(numberStyle);
        } else {
            R245Cell2.setCellValue("");
            R245Cell2.setCellStyle(textStyle);
        }
        Cell R245Cell3 = row.createCell(5);
        if (record3.getR245_AMT_ADJ_BWP1() != null) {
            R245Cell3.setCellValue(record3.getR245_AMT_ADJ_BWP1().doubleValue());
            R245Cell3.setCellStyle(numberStyle);
        } else {
            R245Cell3.setCellValue("");
            R245Cell3.setCellStyle(textStyle);
        }
        Cell R245Cell4 = row.createCell(6);
        if (record3.getR245_AMT_ADJ_BWP2() != null) {
            R245Cell4.setCellValue(record3.getR245_AMT_ADJ_BWP2().doubleValue());
            R245Cell4.setCellStyle(numberStyle);
        } else {
            R245Cell4.setCellValue("");
            R245Cell4.setCellStyle(textStyle);
        }
        Cell R245Cell5 = row.createCell(7);
        if (record3.getR245_NET_AMT_BWP1() != null) {
            R245Cell5.setCellValue(record3.getR245_NET_AMT_BWP1().doubleValue());
            R245Cell5.setCellStyle(numberStyle);
        } else {
            R245Cell5.setCellValue("");
            R245Cell5.setCellStyle(textStyle);
        }
        Cell R245Cell6 = row.createCell(8);
        if (record3.getR245_NET_AMT_BWP2() != null) {
            R245Cell6.setCellValue(record3.getR245_NET_AMT_BWP2().doubleValue());
            R245Cell6.setCellStyle(numberStyle);
        } else {
            R245Cell6.setCellValue("");
            R245Cell6.setCellStyle(textStyle);
        }
        Cell R245Cell7 = row.createCell(9);
        if (record3.getR245_BAL_SUB_BWP1() != null) {
            R245Cell7.setCellValue(record3.getR245_BAL_SUB_BWP1().doubleValue());
            R245Cell7.setCellStyle(numberStyle);
        } else {
            R245Cell7.setCellValue("");
            R245Cell7.setCellStyle(textStyle);
        }
        Cell R245Cell8 = row.createCell(10);
        if (record3.getR245_BAL_SUB_BWP2() != null) {
            R245Cell8.setCellValue(record3.getR245_BAL_SUB_BWP2().doubleValue());
            R245Cell8.setCellStyle(numberStyle);
        } else {
            R245Cell8.setCellValue("");
            R245Cell8.setCellStyle(textStyle);
        }
        Cell R245Cell9 = row.createCell(11);
        if (record3.getR245_BAL_ACT_SUB_BWP1() != null) {
            R245Cell9.setCellValue(record3.getR245_BAL_ACT_SUB_BWP1().doubleValue());
            R245Cell9.setCellStyle(numberStyle);
        } else {
            R245Cell9.setCellValue("");
            R245Cell9.setCellStyle(textStyle);
        }
        Cell R245Cell10 = row.createCell(12);
        if (record3.getR245_BAL_ACT_SUB_BWP2() != null) {
            R245Cell10.setCellValue(record3.getR245_BAL_ACT_SUB_BWP2().doubleValue());
            R245Cell10.setCellStyle(numberStyle);
        } else {
            R245Cell10.setCellValue("");
            R245Cell10.setCellStyle(textStyle);
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
        List<GL_SCH_Archival_Summary_Entity3> dataList2 = GL_SCH_Archival_Summary_Repo3
                .getdatabydateListarchival(dateformat.parse(todate), version);
        List<GL_SCH_Manual_Archival_Summary_Entity> dataList3 = GL_SCH_Manual_Archival_Summary_Repo
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
                    GL_SCH_Archival_Summary_Entity2 record1 = dataList1.get(i);
                    GL_SCH_Archival_Summary_Entity3 record2 = dataList2.get(i);
                    GL_SCH_Manual_Archival_Summary_Entity record3 = dataList3.get(i);

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

        // Use your query to fetch by date
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
            // Only for specific row numbers
            int[] rows = { 61, 103, 130, 139, 241, 243, 245 };

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
                        Method setter = GL_SCH_Manual_Summary_Entity.class.getMethod(setterName,
                                getter.getReturnType());

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

        // FIRST COMMIT — forces immediate commit
        GL_SCH_Manual_summary_repo.saveAndFlush(existing);
        System.out.println("GL_SCH Summary updated and COMMITTED");

        // Execute procedure with updated data
        String oracleDate = new SimpleDateFormat("dd-MM-yyyy")
                .format(updatedEntity.getREPORT_DATE())
                .toUpperCase();

        String sql = "BEGIN BRRS.BRRS_GL_SCH_SUMMARY_PROCEDURE ('" + oracleDate + "'); END;";
        jdbcTemplate.execute(sql);

        System.out.println("Procedure executed for date: " + oracleDate);
    }

}