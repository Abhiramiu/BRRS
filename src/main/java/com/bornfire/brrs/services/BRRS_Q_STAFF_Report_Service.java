package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_Q_STAFF_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_Q_STAFF_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_Q_STAFF_Archival_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_Q_STAFF_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_Q_STAFF_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_Q_STAFF_Summary_Repo3;
import com.bornfire.brrs.entities.M_SRWA_12H_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12H_Summary_Entity;
import com.bornfire.brrs.entities.Q_BRANCHNET_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.Q_STAFF_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.Q_STAFF_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.Q_STAFF_Archival_Summary_Entity3;
import com.bornfire.brrs.entities.Q_STAFF_Summary_Entity1;
import com.bornfire.brrs.entities.Q_STAFF_Summary_Entity2;
import com.bornfire.brrs.entities.Q_STAFF_Summary_Entity3;

import java.lang.reflect.Method;

@Component
@Service
public class BRRS_Q_STAFF_Report_Service {

    private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_STAFF_Report_Service.class);

    @Autowired
    private Environment env;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private AuditService auditService;

    @Autowired
    private BRRS_Q_STAFF_Summary_Repo1 Q_STAFF_Summary_Repo1;

    @Autowired
    private BRRS_Q_STAFF_Summary_Repo2 Q_STAFF_Summary_Repo2;

    @Autowired
    private BRRS_Q_STAFF_Summary_Repo3 Q_STAFF_Summary_Repo3;

    @Autowired
    private BRRS_Q_STAFF_Archival_Summary_Repo1 Q_STAFF_Archival_Summary_Repo1;

    @Autowired
    private BRRS_Q_STAFF_Archival_Summary_Repo2 Q_STAFF_Archival_Summary_Repo2;

    @Autowired
    private BRRS_Q_STAFF_Archival_Summary_Repo3 Q_STAFF_Archival_Summary_Repo3;

    private final SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

    public ModelAndView getQ_STAFFView(String reportId, String fromdate, String todate,
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
                List<Q_STAFF_Archival_Summary_Entity1> T1Master = Q_STAFF_Archival_Summary_Repo1
                        .getdatabydateListarchival(d1, version);
                List<Q_STAFF_Archival_Summary_Entity2> T2Master = Q_STAFF_Archival_Summary_Repo2
                        .getdatabydateListarchival(d1, version);
                List<Q_STAFF_Archival_Summary_Entity3> T3Master = Q_STAFF_Archival_Summary_Repo3
                        .getdatabydateListarchival(d1, version);

                mv.addObject("reportsummary", T1Master);
                mv.addObject("reportsummary2", T2Master);
                mv.addObject("reportsummary3", T3Master);
            }

            // ---------- CASE 2: RESUB ----------
            else if ("RESUB".equalsIgnoreCase(type) && version != null) {
                List<Q_STAFF_Archival_Summary_Entity1> T1Master = Q_STAFF_Archival_Summary_Repo1
                        .getdatabydateListarchival(d1, version);
                List<Q_STAFF_Archival_Summary_Entity2> T2Master = Q_STAFF_Archival_Summary_Repo2
                        .getdatabydateListarchival(d1, version);
                List<Q_STAFF_Archival_Summary_Entity3> T3Master = Q_STAFF_Archival_Summary_Repo3
                        .getdatabydateListarchival(d1, version);

                mv.addObject("reportsummary", T1Master);
                mv.addObject("reportsummary2", T2Master);
                mv.addObject("reportsummary3", T3Master);
            }

            // ---------- CASE 3: NORMAL ----------
            else {
                List<Q_STAFF_Summary_Entity1> T1Master = Q_STAFF_Summary_Repo1.getdatabydateListWithVersion(todate);
                List<Q_STAFF_Summary_Entity2> T2Master = Q_STAFF_Summary_Repo2.getdatabydateListWithVersion(todate);
                List<Q_STAFF_Summary_Entity3> T3Master = Q_STAFF_Summary_Repo3.getdatabydateListWithVersion(todate);
                System.out.println("T1Master Size " + T1Master.size());
                mv.addObject("reportsummary", T1Master);
                mv.addObject("reportsummary2", T2Master);
                mv.addObject("reportsummary3", T3Master);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        mv.setViewName("BRRS/Q_STAFF");
        mv.addObject("displaymode", "summary");
        System.out.println("View set to: " + mv.getViewName());
        return mv;
    }

    public void updateReport(Q_STAFF_Summary_Entity1 updatedEntity) {
        System.out.println("Came to services 1");
        System.out.println("Report Date: " + updatedEntity.getReportDate());

        Q_STAFF_Summary_Entity1 existing = Q_STAFF_Summary_Repo1.findById(updatedEntity.getReportDate())
                .orElseThrow(() -> new RuntimeException(
                        "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

        try {
            // 1️⃣ Loop from R11 to R15 and copy fields
            for (int i = 9; i <= 15; i++) {
                String prefix = "R" + i + "_";

                String[] fields = { "STAFF_COMPLEMENT", "LOCAL", "EXPARIATES", "TOTAL" };

                for (String field : fields) {
                    String getterName = "get" + prefix + field;
                    String setterName = "set" + prefix + field;

                    try {
                        Method getter = Q_STAFF_Summary_Entity1.class.getMethod(getterName);
                        Method setter = Q_STAFF_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

                        Object newValue = getter.invoke(updatedEntity);
                        setter.invoke(existing, newValue);

                    } catch (NoSuchMethodException e) {
                        // Skip missing fields
                        continue;
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while updating report fields", e);
        }
        System.out.println("Testing 1");
        // 3️⃣ Save updated entity
        Q_STAFF_Summary_Repo1.save(existing);

    }

    public void updateReport2(Q_STAFF_Summary_Entity2 updatedEntity) {
        System.out.println("Came to services 2");
        System.out.println("Report Date: " + updatedEntity.getReportDate());

        Q_STAFF_Summary_Entity2 existing = Q_STAFF_Summary_Repo2
                .findById(updatedEntity.getReportDate())
                .orElse(null);

        if (existing == null) {
            System.out.println("⚠️ No existing record found — creating new record for date: "
                    + updatedEntity.getReportDate());
            Q_STAFF_Summary_Repo2.save(updatedEntity);
            return;
        }

        try {
            for (int i = 21; i <= 28; i++) {
                String prefix = "R" + i + "_";
                String[] fields = { "SENIOR_MANAGEMENT_COMPENSATION", "LOCAL", "EXPARIATES", "TOTAL" };

                for (String field : fields) {
                    String getterName = "get" + prefix + field;
                    String setterName = "set" + prefix + field;

                    try {
                        Method getter = Q_STAFF_Summary_Entity2.class.getMethod(getterName);
                        Method setter = Q_STAFF_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
                        Object newValue = getter.invoke(updatedEntity);
                        setter.invoke(existing, newValue);
                    } catch (NoSuchMethodException e) {
                        // Skip missing fields safely
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating report fields", e);
        }

        Q_STAFF_Summary_Repo2.save(existing);
    }

    public void updateReport3(Q_STAFF_Summary_Entity3 updatedEntity) {
        System.out.println("Came to services 3");
        System.out.println("Report Date: " + updatedEntity.getReportDate());

        Q_STAFF_Summary_Entity3 existing = Q_STAFF_Summary_Repo3
                .findById(updatedEntity.getReportDate())
                .orElse(null);

        if (existing == null) {
            System.out.println("⚠️ No existing record found — creating new record for date: "
                    + updatedEntity.getReportDate());
            Q_STAFF_Summary_Repo3.save(updatedEntity);
            return;
        }

        try {
            for (int i = 33; i <= 38; i++) {
                String prefix = "R" + i + "_";
                String[] fields = { "STAFF_LOANS", "ORIGINAL_AMT", "BALANCE_OUTSTANDING", "NO_OF_ACS",
                        "INTEREST_RATE" };

                for (String field : fields) {
                    String getterName = "get" + prefix + field;
                    String setterName = "set" + prefix + field;

                    try {
                        Method getter = Q_STAFF_Summary_Entity3.class.getMethod(getterName);
                        Method setter = Q_STAFF_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());
                        Object newValue = getter.invoke(updatedEntity);
                        setter.invoke(existing, newValue);
                    } catch (NoSuchMethodException e) {
                        // Skip missing fields safely
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating report fields", e);
        }

        Q_STAFF_Summary_Repo3.save(existing);
    }

    // public List<Object> getQ_STAFFArchival() {
    // List<Object> Q_STAFFArchivallist = new ArrayList<>();
    // try {
    // Q_STAFFArchivallist = Q_STAFF_Archival_Summary_Repo1.getQ_STAFFarchival();
    // Q_STAFFArchivallist = Q_STAFF_Archival_Summary_Repo2.getQ_STAFFarchival();
    // Q_STAFFArchivallist = Q_STAFF_Archival_Summary_Repo3.getQ_STAFFarchival();

    // System.out.println("countser" + Q_STAFFArchivallist.size());
    // } catch (Exception e) {
    // System.err.println("Error fetching M_STAFF2 Archival data: " +
    // e.getMessage());
    // e.printStackTrace();
    // }
    // return Q_STAFFArchivallist;
    // }

    public byte[] BRRS_Q_STAFFExcel(String filename, String reportId, String fromdate, String todate, String currency,
            String dtltype, String type, String version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");
        System.out.println(type);
        System.out.println(version);
        Date reportDate = dateformat.parse(todate);

        // ARCHIVAL check
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
            logger.info("Service: Generating ARCHIVAL report for version {}", version);
            return getQ_STAFFArchival(filename, reportId, fromdate, todate, currency, dtltype, type, version);
        }
        // RESUB check
        else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
            logger.info("Service: Generating RESUB report for version {}", version);

            List<Q_STAFF_Archival_Summary_Entity1> T1Master1 = Q_STAFF_Archival_Summary_Repo1
                    .getdatabydateListarchival(reportDate, version);
            List<Q_STAFF_Archival_Summary_Entity2> T1Master2 = Q_STAFF_Archival_Summary_Repo2
                    .getdatabydateListarchival(reportDate, version);
            List<Q_STAFF_Archival_Summary_Entity3> T1Master3 = Q_STAFF_Archival_Summary_Repo3
                    .getdatabydateListarchival(reportDate, version);

            // Generate Excel for RESUB
            return BRRS_Q_STAFFResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
        }
        List<Q_STAFF_Summary_Entity1> dataList = Q_STAFF_Summary_Repo1.getdatabydateList(dateformat.parse(todate));
        List<Q_STAFF_Summary_Entity2> dataList1 = Q_STAFF_Summary_Repo2.getdatabydateList(dateformat.parse(todate));
        List<Q_STAFF_Summary_Entity3> dataList2 = Q_STAFF_Summary_Repo3.getdatabydateList(dateformat.parse(todate));

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for brrs2.4 report. Returning empty result.");
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

            int startRow = 8;

            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {
                    Q_STAFF_Summary_Entity1 record = dataList.get(i);
                    Q_STAFF_Summary_Entity2 record1 = dataList1.get(i);
                    Q_STAFF_Summary_Entity3 record2 = dataList2.get(i);

                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }

                    // R9 Col B
                    Cell R9cell1 = row.createCell(1);
                    if (record.getR9_LOCAL() != null) {
                        R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
                        R9cell1.setCellStyle(numberStyle);
                    } else {
                        R9cell1.setCellValue("");
                        R9cell1.setCellStyle(textStyle);
                    }

                    // R9 Col C
                    Cell R9cell2 = row.createCell(2);
                    if (record.getR9_EXPARIATES() != null) {
                        R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
                        R9cell2.setCellStyle(numberStyle);
                    } else {
                        R9cell2.setCellValue("");
                        R9cell2.setCellStyle(textStyle);
                    }
                    // R10 Col B
                    row = sheet.getRow(9);
                    // R10 Col B
                    Cell R10cell1 = row.createCell(1);
                    if (record.getR10_LOCAL() != null) {
                        R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
                        R10cell1.setCellStyle(numberStyle);
                    } else {
                        R10cell1.setCellValue("");
                        R10cell1.setCellStyle(textStyle);
                    }

                    // R10 Col C
                    Cell R10cell2 = row.createCell(2);
                    if (record.getR10_EXPARIATES() != null) {
                        R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
                        R10cell2.setCellStyle(numberStyle);
                    } else {
                        R10cell2.setCellValue("");
                        R10cell2.setCellStyle(textStyle);
                    }
                    // R11 Col B
                    row = sheet.getRow(10);
                    // R11 Col B
                    Cell R11cell1 = row.createCell(1);
                    if (record.getR11_LOCAL() != null) {
                        R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
                        R11cell1.setCellStyle(numberStyle);
                    } else {
                        R11cell1.setCellValue("");
                        R11cell1.setCellStyle(textStyle);
                    }

                    // R11 Col C
                    Cell R11cell2 = row.createCell(2);
                    if (record.getR11_EXPARIATES() != null) {
                        R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
                        R11cell2.setCellStyle(numberStyle);
                    } else {
                        R11cell2.setCellValue("");
                        R11cell2.setCellStyle(textStyle);
                    }
                    // R12 Col B
                    row = sheet.getRow(11);

                    Cell R12cell1 = row.createCell(1);
                    if (record.getR12_LOCAL() != null) {
                        R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
                        R12cell1.setCellStyle(numberStyle);
                    } else {
                        R12cell1.setCellValue("");
                        R12cell1.setCellStyle(textStyle);
                    }

                    // R12 Col C
                    Cell R12cell2 = row.createCell(2);
                    if (record.getR12_EXPARIATES() != null) {
                        R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
                        R12cell2.setCellStyle(numberStyle);
                    } else {
                        R12cell2.setCellValue("");
                        R12cell2.setCellStyle(textStyle);
                    }
                    // R13 Col B
                    row = sheet.getRow(12);

                    Cell R13cell1 = row.createCell(1);
                    if (record.getR13_LOCAL() != null) {
                        R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
                        R13cell1.setCellStyle(numberStyle);
                    } else {
                        R13cell1.setCellValue("");
                        R13cell1.setCellStyle(textStyle);
                    }

                    // R13 Col C
                    Cell R13cell2 = row.createCell(2);
                    if (record.getR13_EXPARIATES() != null) {
                        R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
                        R13cell2.setCellStyle(numberStyle);
                    } else {
                        R13cell2.setCellValue("");
                        R13cell2.setCellStyle(textStyle);
                    }
                    // R14 Col B
                    row = sheet.getRow(13);
                    Cell R14cell1 = row.createCell(1);
                    if (record.getR14_LOCAL() != null) {
                        R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
                        R14cell1.setCellStyle(numberStyle);
                    } else {
                        R14cell1.setCellValue("");
                        R14cell1.setCellStyle(textStyle);
                    }

                    // R14 Col C
                    Cell R14cell2 = row.createCell(2);
                    if (record.getR14_EXPARIATES() != null) {
                        R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
                        R14cell2.setCellStyle(numberStyle);
                    } else {
                        R14cell2.setCellValue("");
                        R14cell2.setCellStyle(textStyle);
                    }

                    // TABLE 2
                    // R21 Col B
                    row = sheet.getRow(20);
                    Cell R21cell1 = row.createCell(1);
                    if (record1.getR21_LOCAL() != null) {
                        R21cell1.setCellValue(record1.getR21_LOCAL().doubleValue());
                        R21cell1.setCellStyle(numberStyle);
                    } else {
                        R21cell1.setCellValue("");
                        R21cell1.setCellStyle(textStyle);
                    }
                    // R21 COL C
                    Cell R21cell2 = row.createCell(2);
                    if (record1.getR21_EXPARIATES() != null) {
                        R21cell2.setCellValue(record1.getR21_EXPARIATES().doubleValue());
                        R21cell2.setCellStyle(numberStyle);
                    } else {
                        R21cell2.setCellValue("");
                        R21cell2.setCellStyle(textStyle);
                    }
                    // R22 Col B
                    row = sheet.getRow(21);
                    Cell R22cell1 = row.createCell(1);
                    if (record1.getR22_LOCAL() != null) {
                        R22cell1.setCellValue(record1.getR22_LOCAL().doubleValue());
                        R22cell1.setCellStyle(numberStyle);
                    } else {
                        R22cell1.setCellValue("");
                        R22cell1.setCellStyle(textStyle);
                    }

                    // R22 Col C
                    Cell R22cell2 = row.createCell(2);
                    if (record1.getR22_EXPARIATES() != null) {
                        R22cell2.setCellValue(record1.getR22_EXPARIATES().doubleValue());
                        R22cell2.setCellStyle(numberStyle);
                    } else {
                        R22cell2.setCellValue("");
                        R22cell2.setCellStyle(textStyle);
                    }
                    // R23 Col B
                    row = sheet.getRow(22);
                    // R23 Col B
                    Cell R23cell1 = row.createCell(1);
                    if (record1.getR23_LOCAL() != null) {
                        R23cell1.setCellValue(record1.getR23_LOCAL().doubleValue());
                        R23cell1.setCellStyle(numberStyle);
                    } else {
                        R23cell1.setCellValue("");
                        R23cell1.setCellStyle(textStyle);
                    }

                    // R23 Col C
                    Cell R23cell2 = row.createCell(2);
                    if (record1.getR23_EXPARIATES() != null) {
                        R23cell2.setCellValue(record1.getR23_EXPARIATES().doubleValue());
                        R23cell2.setCellStyle(numberStyle);
                    } else {
                        R23cell2.setCellValue("");
                        R23cell2.setCellStyle(textStyle);
                    }
                    // R24 Col B
                    row = sheet.getRow(23);
                    // R24 Col B
                    Cell R24cell1 = row.createCell(1);
                    if (record1.getR24_LOCAL() != null) {
                        R24cell1.setCellValue(record1.getR24_LOCAL().doubleValue());
                        R24cell1.setCellStyle(numberStyle);
                    } else {
                        R24cell1.setCellValue("");
                        R24cell1.setCellStyle(textStyle);
                    }

                    // R24 Col C
                    Cell R24cell2 = row.createCell(2);
                    if (record1.getR24_EXPARIATES() != null) {
                        R24cell2.setCellValue(record1.getR24_EXPARIATES().doubleValue());
                        R24cell2.setCellStyle(numberStyle);
                    } else {
                        R24cell2.setCellValue("");
                        R24cell2.setCellStyle(textStyle);
                    }
                    // R25 Col B
                    row = sheet.getRow(24);
                    // R25 Col B
                    Cell R25cell1 = row.createCell(1);
                    if (record1.getR25_LOCAL() != null) {
                        R25cell1.setCellValue(record1.getR25_LOCAL().doubleValue());
                        R25cell1.setCellStyle(numberStyle);
                    } else {
                        R25cell1.setCellValue("");
                        R25cell1.setCellStyle(textStyle);
                    }

                    // R25 Col C
                    Cell R25cell2 = row.createCell(2);
                    if (record1.getR25_EXPARIATES() != null) {
                        R25cell2.setCellValue(record1.getR25_EXPARIATES().doubleValue());
                        R25cell2.setCellStyle(numberStyle);
                    } else {
                        R25cell2.setCellValue("");
                        R25cell2.setCellStyle(textStyle);
                    }
                    // R26 Col B
                    row = sheet.getRow(25);
                    // R26 Col B
                    Cell R26cell1 = row.createCell(1);
                    if (record1.getR26_LOCAL() != null) {
                        R26cell1.setCellValue(record1.getR26_LOCAL().doubleValue());
                        R26cell1.setCellStyle(numberStyle);
                    } else {
                        R26cell1.setCellValue("");
                        R26cell1.setCellStyle(textStyle);
                    }

                    // R26 Col C
                    Cell R26cell2 = row.createCell(2);
                    if (record1.getR26_EXPARIATES() != null) {
                        R26cell2.setCellValue(record1.getR26_EXPARIATES().doubleValue());
                        R26cell2.setCellStyle(numberStyle);
                    } else {
                        R26cell2.setCellValue("");
                        R26cell2.setCellStyle(textStyle);
                    }
                    // R27 Col B
                    row = sheet.getRow(26);
                    // R27 Col B
                    Cell R27cell1 = row.createCell(1);
                    if (record1.getR27_LOCAL() != null) {
                        R27cell1.setCellValue(record1.getR27_LOCAL().doubleValue());
                        R27cell1.setCellStyle(numberStyle);
                    } else {
                        R27cell1.setCellValue("");
                        R27cell1.setCellStyle(textStyle);
                    }

                    // R27 Col C
                    Cell R27cell2 = row.createCell(2);
                    if (record1.getR27_EXPARIATES() != null) {
                        R27cell2.setCellValue(record1.getR27_EXPARIATES().doubleValue());
                        R27cell2.setCellStyle(numberStyle);
                    } else {
                        R27cell2.setCellValue("");
                        R27cell2.setCellStyle(textStyle);
                    }
                    // TABLE 3
                    // R33 Col B
                    row = sheet.getRow(32);
                    Cell R33cell1 = row.createCell(1);
                    if (record2.getR33_ORIGINAL_AMT() != null) {
                        R33cell1.setCellValue(record2.getR33_ORIGINAL_AMT().doubleValue());
                        R33cell1.setCellStyle(numberStyle);
                    } else {
                        R33cell1.setCellValue("");
                        R33cell1.setCellStyle(textStyle);
                    }

                    // R33 Col C
                    Cell R33cell2 = row.createCell(2);
                    if (record2.getR33_BALANCE_OUTSTANDING() != null) {
                        R33cell2.setCellValue(record2.getR33_BALANCE_OUTSTANDING().doubleValue());
                        R33cell2.setCellStyle(numberStyle);
                    } else {
                        R33cell2.setCellValue("");
                        R33cell2.setCellStyle(textStyle);
                    }
                    // R33 Col D
                    Cell R33cell3 = row.createCell(3);
                    if (record2.getR33_NO_OF_ACS() != null) {
                        R33cell3.setCellValue(record2.getR33_NO_OF_ACS().doubleValue());
                        R33cell3.setCellStyle(numberStyle);
                    } else {
                        R33cell3.setCellValue("");
                        R33cell3.setCellStyle(textStyle);
                    }

                    // R33 Col E
                    Cell R33cell4 = row.createCell(4);
                    if (record2.getR33_INTEREST_RATE() != null) {
                        R33cell4.setCellValue(record2.getR33_INTEREST_RATE().doubleValue());
                        R33cell4.setCellStyle(numberStyle);
                    } else {
                        R33cell4.setCellValue("");
                        R33cell4.setCellStyle(textStyle);
                    }
                    // R34 Col B
                    row = sheet.getRow(33);
                    Cell R34cell1 = row.createCell(1);
                    if (record2.getR34_ORIGINAL_AMT() != null) {
                        R34cell1.setCellValue(record2.getR34_ORIGINAL_AMT().doubleValue());
                        R34cell1.setCellStyle(numberStyle);
                    } else {
                        R34cell1.setCellValue("");
                        R34cell1.setCellStyle(textStyle);
                    }

                    // R34 Col C
                    Cell R34cell2 = row.createCell(2);
                    if (record2.getR34_BALANCE_OUTSTANDING() != null) {
                        R34cell2.setCellValue(record2.getR34_BALANCE_OUTSTANDING().doubleValue());
                        R34cell2.setCellStyle(numberStyle);
                    } else {
                        R34cell2.setCellValue("");
                        R34cell2.setCellStyle(textStyle);
                    }
                    // R34 Col D
                    Cell R34cell3 = row.createCell(3);
                    if (record2.getR34_NO_OF_ACS() != null) {
                        R34cell3.setCellValue(record2.getR34_NO_OF_ACS().doubleValue());
                        R34cell3.setCellStyle(numberStyle);
                    } else {
                        R34cell3.setCellValue("");
                        R34cell3.setCellStyle(textStyle);
                    }

                    // R34 Col E
                    Cell R34cell4 = row.createCell(4);
                    if (record2.getR34_INTEREST_RATE() != null) {
                        R34cell4.setCellValue(record2.getR34_INTEREST_RATE().doubleValue());
                        R34cell4.setCellStyle(numberStyle);
                    } else {
                        R34cell4.setCellValue("");
                        R34cell4.setCellStyle(textStyle);
                    }
                    // R35 Col B
                    row = sheet.getRow(34);

                    Cell R35cell1 = row.createCell(1);
                    if (record2.getR35_ORIGINAL_AMT() != null) {
                        R35cell1.setCellValue(record2.getR35_ORIGINAL_AMT().doubleValue());
                        R35cell1.setCellStyle(numberStyle);
                    } else {
                        R35cell1.setCellValue("");
                        R35cell1.setCellStyle(textStyle);
                    }

                    // R35 Col C
                    Cell R35cell2 = row.createCell(2);
                    if (record2.getR35_BALANCE_OUTSTANDING() != null) {
                        R35cell2.setCellValue(record2.getR35_BALANCE_OUTSTANDING().doubleValue());
                        R35cell2.setCellStyle(numberStyle);
                    } else {
                        R35cell2.setCellValue("");
                        R35cell2.setCellStyle(textStyle);
                    }
                    // R35 Col D
                    Cell R35cell3 = row.createCell(3);
                    if (record2.getR35_NO_OF_ACS() != null) {
                        R35cell3.setCellValue(record2.getR35_NO_OF_ACS().doubleValue());
                        R35cell3.setCellStyle(numberStyle);
                    } else {
                        R35cell3.setCellValue("");
                        R35cell3.setCellStyle(textStyle);
                    }

                    // R35 Col E
                    Cell R35cell4 = row.createCell(4);
                    if (record2.getR35_INTEREST_RATE() != null) {
                        R35cell4.setCellValue(record2.getR35_INTEREST_RATE().doubleValue());
                        R35cell4.setCellStyle(numberStyle);
                    } else {
                        R35cell4.setCellValue("");
                        R35cell4.setCellStyle(textStyle);
                    }
                    // R36 Col B
                    row = sheet.getRow(35);
                    Cell R36cell1 = row.createCell(1);
                    if (record2.getR36_ORIGINAL_AMT() != null) {
                        R36cell1.setCellValue(record2.getR36_ORIGINAL_AMT().doubleValue());
                        R36cell1.setCellStyle(numberStyle);
                    } else {
                        R36cell1.setCellValue("");
                        R36cell1.setCellStyle(textStyle);
                    }

                    // R36 Col C
                    Cell R36cell2 = row.createCell(2);
                    if (record2.getR36_BALANCE_OUTSTANDING() != null) {
                        R36cell2.setCellValue(record2.getR36_BALANCE_OUTSTANDING().doubleValue());
                        R36cell2.setCellStyle(numberStyle);
                    } else {
                        R36cell2.setCellValue("");
                        R36cell2.setCellStyle(textStyle);
                    }
                    // R36 Col D
                    Cell R36cell3 = row.createCell(3);
                    if (record2.getR36_NO_OF_ACS() != null) {
                        R36cell3.setCellValue(record2.getR36_NO_OF_ACS().doubleValue());
                        R36cell3.setCellStyle(numberStyle);
                    } else {
                        R36cell3.setCellValue("");
                        R36cell3.setCellStyle(textStyle);
                    }

                    // R36 Col E
                    Cell R36cell4 = row.createCell(4);
                    if (record2.getR36_INTEREST_RATE() != null) {
                        R36cell4.setCellValue(record2.getR36_INTEREST_RATE().doubleValue());
                        R36cell4.setCellStyle(numberStyle);
                    } else {
                        R36cell4.setCellValue("");
                        R36cell4.setCellStyle(textStyle);
                    }
                    // R37 Col B
                    row = sheet.getRow(36);
                    Cell R37cell1 = row.createCell(1);
                    if (record2.getR37_ORIGINAL_AMT() != null) {
                        R37cell1.setCellValue(record2.getR37_ORIGINAL_AMT().doubleValue());
                        R37cell1.setCellStyle(numberStyle);
                    } else {
                        R37cell1.setCellValue("");
                        R37cell1.setCellStyle(textStyle);
                    }

                    // R37 Col C
                    Cell R37cell2 = row.createCell(2);
                    if (record2.getR37_BALANCE_OUTSTANDING() != null) {
                        R37cell2.setCellValue(record2.getR37_BALANCE_OUTSTANDING().doubleValue());
                        R37cell2.setCellStyle(numberStyle);
                    } else {
                        R37cell2.setCellValue("");
                        R37cell2.setCellStyle(textStyle);
                    }
                    // R37 Col D
                    Cell R37cell3 = row.createCell(3);
                    if (record2.getR37_NO_OF_ACS() != null) {
                        R37cell3.setCellValue(record2.getR37_NO_OF_ACS().doubleValue());
                        R37cell3.setCellStyle(numberStyle);
                    } else {
                        R37cell3.setCellValue("");
                        R37cell3.setCellStyle(textStyle);
                    }

                    // R37 Col E
                    Cell R37cell4 = row.createCell(4);
                    if (record2.getR37_INTEREST_RATE() != null) {
                        R37cell4.setCellValue(record2.getR37_INTEREST_RATE().doubleValue());
                        R37cell4.setCellStyle(numberStyle);
                    } else {
                        R37cell4.setCellValue("");
                        R37cell4.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(37);
                    // R37 Col E
                    Cell R38cell4 = row.createCell(4);
                    if (record2.getR38_INTEREST_RATE() != null) {
                        R38cell4.setCellValue(record2.getR38_INTEREST_RATE().doubleValue());
                        R38cell4.setCellStyle(numberStyle);
                    } else {
                        R38cell4.setCellValue("");
                        R38cell4.setCellStyle(textStyle);
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

    public byte[] getQ_STAFFArchival(String filename, String reportId, String fromdate, String todate,
            String currency, String dtltype, String type, String version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");
        if (type.equals("ARCHIVAL") & version != null) {

        }
        List<Q_STAFF_Archival_Summary_Entity1> dataList = Q_STAFF_Archival_Summary_Repo1
                .getdatabydateListarchival(dateformat.parse(todate), version);
        List<Q_STAFF_Archival_Summary_Entity2> dataList1 = Q_STAFF_Archival_Summary_Repo2
                .getdatabydateListarchival(dateformat.parse(todate), version);
        List<Q_STAFF_Archival_Summary_Entity3> dataList2 = Q_STAFF_Archival_Summary_Repo3
                .getdatabydateListarchival(dateformat.parse(todate), version);

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for M_LA1 report. Returning empty result.");
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
            int startRow = 8;

            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {
                    Q_STAFF_Archival_Summary_Entity1 record = dataList.get(i);
                    Q_STAFF_Archival_Summary_Entity2 record1 = dataList1.get(i);
                    Q_STAFF_Archival_Summary_Entity3 record2 = dataList2.get(i);
                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }

                    // R9 Col B
                    Cell R9cell1 = row.createCell(1);
                    if (record.getR9_LOCAL() != null) {
                        R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
                        R9cell1.setCellStyle(numberStyle);
                    } else {
                        R9cell1.setCellValue("");
                        R9cell1.setCellStyle(textStyle);
                    }

                    // R9 Col C
                    Cell R9cell2 = row.createCell(2);
                    if (record.getR9_EXPARIATES() != null) {
                        R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
                        R9cell2.setCellStyle(numberStyle);
                    } else {
                        R9cell2.setCellValue("");
                        R9cell2.setCellStyle(textStyle);
                    }
                    // R10 Col B
                    row = sheet.getRow(9);
                    // R10 Col B
                    Cell R10cell1 = row.createCell(1);
                    if (record.getR10_LOCAL() != null) {
                        R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
                        R10cell1.setCellStyle(numberStyle);
                    } else {
                        R10cell1.setCellValue("");
                        R10cell1.setCellStyle(textStyle);
                    }

                    // R10 Col C
                    Cell R10cell2 = row.createCell(2);
                    if (record.getR10_EXPARIATES() != null) {
                        R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
                        R10cell2.setCellStyle(numberStyle);
                    } else {
                        R10cell2.setCellValue("");
                        R10cell2.setCellStyle(textStyle);
                    }
                    // R11 Col B
                    row = sheet.getRow(10);
                    // R11 Col B
                    Cell R11cell1 = row.createCell(1);
                    if (record.getR11_LOCAL() != null) {
                        R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
                        R11cell1.setCellStyle(numberStyle);
                    } else {
                        R11cell1.setCellValue("");
                        R11cell1.setCellStyle(textStyle);
                    }

                    // R11 Col C
                    Cell R11cell2 = row.createCell(2);
                    if (record.getR11_EXPARIATES() != null) {
                        R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
                        R11cell2.setCellStyle(numberStyle);
                    } else {
                        R11cell2.setCellValue("");
                        R11cell2.setCellStyle(textStyle);
                    }
                    // R12 Col B
                    row = sheet.getRow(11);

                    Cell R12cell1 = row.createCell(1);
                    if (record.getR12_LOCAL() != null) {
                        R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
                        R12cell1.setCellStyle(numberStyle);
                    } else {
                        R12cell1.setCellValue("");
                        R12cell1.setCellStyle(textStyle);
                    }

                    // R12 Col C
                    Cell R12cell2 = row.createCell(2);
                    if (record.getR12_EXPARIATES() != null) {
                        R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
                        R12cell2.setCellStyle(numberStyle);
                    } else {
                        R12cell2.setCellValue("");
                        R12cell2.setCellStyle(textStyle);
                    }
                    // R13 Col B
                    row = sheet.getRow(12);

                    Cell R13cell1 = row.createCell(1);
                    if (record.getR13_LOCAL() != null) {
                        R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
                        R13cell1.setCellStyle(numberStyle);
                    } else {
                        R13cell1.setCellValue("");
                        R13cell1.setCellStyle(textStyle);
                    }

                    // R13 Col C
                    Cell R13cell2 = row.createCell(2);
                    if (record.getR13_EXPARIATES() != null) {
                        R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
                        R13cell2.setCellStyle(numberStyle);
                    } else {
                        R13cell2.setCellValue("");
                        R13cell2.setCellStyle(textStyle);
                    }
                    // R14 Col B
                    row = sheet.getRow(13);
                    Cell R14cell1 = row.createCell(1);
                    if (record.getR14_LOCAL() != null) {
                        R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
                        R14cell1.setCellStyle(numberStyle);
                    } else {
                        R14cell1.setCellValue("");
                        R14cell1.setCellStyle(textStyle);
                    }

                    // R14 Col C
                    Cell R14cell2 = row.createCell(2);
                    if (record.getR14_EXPARIATES() != null) {
                        R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
                        R14cell2.setCellStyle(numberStyle);
                    } else {
                        R14cell2.setCellValue("");
                        R14cell2.setCellStyle(textStyle);
                    }

                    // TABLE 2
                    // R21 Col B
                    row = sheet.getRow(20);
                    Cell R21cell1 = row.createCell(1);
                    if (record1.getR21_LOCAL() != null) {
                        R21cell1.setCellValue(record1.getR21_LOCAL().doubleValue());
                        R21cell1.setCellStyle(numberStyle);
                    } else {
                        R21cell1.setCellValue("");
                        R21cell1.setCellStyle(textStyle);
                    }
                    // R21 COL C
                    Cell R21cell2 = row.createCell(2);
                    if (record1.getR21_EXPARIATES() != null) {
                        R21cell2.setCellValue(record1.getR21_EXPARIATES().doubleValue());
                        R21cell2.setCellStyle(numberStyle);
                    } else {
                        R21cell2.setCellValue("");
                        R21cell2.setCellStyle(textStyle);
                    }
                    // R22 Col B
                    row = sheet.getRow(21);
                    Cell R22cell1 = row.createCell(1);
                    if (record1.getR22_LOCAL() != null) {
                        R22cell1.setCellValue(record1.getR22_LOCAL().doubleValue());
                        R22cell1.setCellStyle(numberStyle);
                    } else {
                        R22cell1.setCellValue("");
                        R22cell1.setCellStyle(textStyle);
                    }

                    // R22 Col C
                    Cell R22cell2 = row.createCell(2);
                    if (record1.getR22_EXPARIATES() != null) {
                        R22cell2.setCellValue(record1.getR22_EXPARIATES().doubleValue());
                        R22cell2.setCellStyle(numberStyle);
                    } else {
                        R22cell2.setCellValue("");
                        R22cell2.setCellStyle(textStyle);
                    }
                    // R23 Col B
                    row = sheet.getRow(22);
                    // R23 Col B
                    Cell R23cell1 = row.createCell(1);
                    if (record1.getR23_LOCAL() != null) {
                        R23cell1.setCellValue(record1.getR23_LOCAL().doubleValue());
                        R23cell1.setCellStyle(numberStyle);
                    } else {
                        R23cell1.setCellValue("");
                        R23cell1.setCellStyle(textStyle);
                    }

                    // R23 Col C
                    Cell R23cell2 = row.createCell(2);
                    if (record1.getR23_EXPARIATES() != null) {
                        R23cell2.setCellValue(record1.getR23_EXPARIATES().doubleValue());
                        R23cell2.setCellStyle(numberStyle);
                    } else {
                        R23cell2.setCellValue("");
                        R23cell2.setCellStyle(textStyle);
                    }
                    // R24 Col B
                    row = sheet.getRow(23);
                    // R24 Col B
                    Cell R24cell1 = row.createCell(1);
                    if (record1.getR24_LOCAL() != null) {
                        R24cell1.setCellValue(record1.getR24_LOCAL().doubleValue());
                        R24cell1.setCellStyle(numberStyle);
                    } else {
                        R24cell1.setCellValue("");
                        R24cell1.setCellStyle(textStyle);
                    }

                    // R24 Col C
                    Cell R24cell2 = row.createCell(2);
                    if (record1.getR24_EXPARIATES() != null) {
                        R24cell2.setCellValue(record1.getR24_EXPARIATES().doubleValue());
                        R24cell2.setCellStyle(numberStyle);
                    } else {
                        R24cell2.setCellValue("");
                        R24cell2.setCellStyle(textStyle);
                    }
                    // R25 Col B
                    row = sheet.getRow(24);
                    // R25 Col B
                    Cell R25cell1 = row.createCell(1);
                    if (record1.getR25_LOCAL() != null) {
                        R25cell1.setCellValue(record1.getR25_LOCAL().doubleValue());
                        R25cell1.setCellStyle(numberStyle);
                    } else {
                        R25cell1.setCellValue("");
                        R25cell1.setCellStyle(textStyle);
                    }

                    // R25 Col C
                    Cell R25cell2 = row.createCell(2);
                    if (record1.getR25_EXPARIATES() != null) {
                        R25cell2.setCellValue(record1.getR25_EXPARIATES().doubleValue());
                        R25cell2.setCellStyle(numberStyle);
                    } else {
                        R25cell2.setCellValue("");
                        R25cell2.setCellStyle(textStyle);
                    }
                    // R26 Col B
                    row = sheet.getRow(25);
                    // R26 Col B
                    Cell R26cell1 = row.createCell(1);
                    if (record1.getR26_LOCAL() != null) {
                        R26cell1.setCellValue(record1.getR26_LOCAL().doubleValue());
                        R26cell1.setCellStyle(numberStyle);
                    } else {
                        R26cell1.setCellValue("");
                        R26cell1.setCellStyle(textStyle);
                    }

                    // R26 Col C
                    Cell R26cell2 = row.createCell(2);
                    if (record1.getR26_EXPARIATES() != null) {
                        R26cell2.setCellValue(record1.getR26_EXPARIATES().doubleValue());
                        R26cell2.setCellStyle(numberStyle);
                    } else {
                        R26cell2.setCellValue("");
                        R26cell2.setCellStyle(textStyle);
                    }
                    // R27 Col B
                    row = sheet.getRow(26);
                    // R27 Col B
                    Cell R27cell1 = row.createCell(1);
                    if (record1.getR27_LOCAL() != null) {
                        R27cell1.setCellValue(record1.getR27_LOCAL().doubleValue());
                        R27cell1.setCellStyle(numberStyle);
                    } else {
                        R27cell1.setCellValue("");
                        R27cell1.setCellStyle(textStyle);
                    }

                    // R27 Col C
                    Cell R27cell2 = row.createCell(2);
                    if (record1.getR27_EXPARIATES() != null) {
                        R27cell2.setCellValue(record1.getR27_EXPARIATES().doubleValue());
                        R27cell2.setCellStyle(numberStyle);
                    } else {
                        R27cell2.setCellValue("");
                        R27cell2.setCellStyle(textStyle);
                    }
                    // TABLE 3
                    // R33 Col B
                    row = sheet.getRow(32);
                    Cell R33cell1 = row.createCell(1);
                    if (record2.getR33_ORIGINAL_AMT() != null) {
                        R33cell1.setCellValue(record2.getR33_ORIGINAL_AMT().doubleValue());
                        R33cell1.setCellStyle(numberStyle);
                    } else {
                        R33cell1.setCellValue("");
                        R33cell1.setCellStyle(textStyle);
                    }

                    // R33 Col C
                    Cell R33cell2 = row.createCell(2);
                    if (record2.getR33_BALANCE_OUTSTANDING() != null) {
                        R33cell2.setCellValue(record2.getR33_BALANCE_OUTSTANDING().doubleValue());
                        R33cell2.setCellStyle(numberStyle);
                    } else {
                        R33cell2.setCellValue("");
                        R33cell2.setCellStyle(textStyle);
                    }
                    // R33 Col D
                    Cell R33cell3 = row.createCell(3);
                    if (record2.getR33_NO_OF_ACS() != null) {
                        R33cell3.setCellValue(record2.getR33_NO_OF_ACS().doubleValue());
                        R33cell3.setCellStyle(numberStyle);
                    } else {
                        R33cell3.setCellValue("");
                        R33cell3.setCellStyle(textStyle);
                    }

                    // R33 Col E
                    Cell R33cell4 = row.createCell(4);
                    if (record2.getR33_INTEREST_RATE() != null) {
                        R33cell4.setCellValue(record2.getR33_INTEREST_RATE().doubleValue());
                        R33cell4.setCellStyle(numberStyle);
                    } else {
                        R33cell4.setCellValue("");
                        R33cell4.setCellStyle(textStyle);
                    }
                    // R34 Col B
                    row = sheet.getRow(33);
                    Cell R34cell1 = row.createCell(1);
                    if (record2.getR34_ORIGINAL_AMT() != null) {
                        R34cell1.setCellValue(record2.getR34_ORIGINAL_AMT().doubleValue());
                        R34cell1.setCellStyle(numberStyle);
                    } else {
                        R34cell1.setCellValue("");
                        R34cell1.setCellStyle(textStyle);
                    }

                    // R34 Col C
                    Cell R34cell2 = row.createCell(2);
                    if (record2.getR34_BALANCE_OUTSTANDING() != null) {
                        R34cell2.setCellValue(record2.getR34_BALANCE_OUTSTANDING().doubleValue());
                        R34cell2.setCellStyle(numberStyle);
                    } else {
                        R34cell2.setCellValue("");
                        R34cell2.setCellStyle(textStyle);
                    }
                    // R34 Col D
                    Cell R34cell3 = row.createCell(3);
                    if (record2.getR34_NO_OF_ACS() != null) {
                        R34cell3.setCellValue(record2.getR34_NO_OF_ACS().doubleValue());
                        R34cell3.setCellStyle(numberStyle);
                    } else {
                        R34cell3.setCellValue("");
                        R34cell3.setCellStyle(textStyle);
                    }

                    // R34 Col E
                    Cell R34cell4 = row.createCell(4);
                    if (record2.getR34_INTEREST_RATE() != null) {
                        R34cell4.setCellValue(record2.getR34_INTEREST_RATE().doubleValue());
                        R34cell4.setCellStyle(numberStyle);
                    } else {
                        R34cell4.setCellValue("");
                        R34cell4.setCellStyle(textStyle);
                    }
                    // R35 Col B
                    row = sheet.getRow(34);

                    Cell R35cell1 = row.createCell(1);
                    if (record2.getR35_ORIGINAL_AMT() != null) {
                        R35cell1.setCellValue(record2.getR35_ORIGINAL_AMT().doubleValue());
                        R35cell1.setCellStyle(numberStyle);
                    } else {
                        R35cell1.setCellValue("");
                        R35cell1.setCellStyle(textStyle);
                    }

                    // R35 Col C
                    Cell R35cell2 = row.createCell(2);
                    if (record2.getR35_BALANCE_OUTSTANDING() != null) {
                        R35cell2.setCellValue(record2.getR35_BALANCE_OUTSTANDING().doubleValue());
                        R35cell2.setCellStyle(numberStyle);
                    } else {
                        R35cell2.setCellValue("");
                        R35cell2.setCellStyle(textStyle);
                    }
                    // R35 Col D
                    Cell R35cell3 = row.createCell(3);
                    if (record2.getR35_NO_OF_ACS() != null) {
                        R35cell3.setCellValue(record2.getR35_NO_OF_ACS().doubleValue());
                        R35cell3.setCellStyle(numberStyle);
                    } else {
                        R35cell3.setCellValue("");
                        R35cell3.setCellStyle(textStyle);
                    }

                    // R35 Col E
                    Cell R35cell4 = row.createCell(4);
                    if (record2.getR35_INTEREST_RATE() != null) {
                        R35cell4.setCellValue(record2.getR35_INTEREST_RATE().doubleValue());
                        R35cell4.setCellStyle(numberStyle);
                    } else {
                        R35cell4.setCellValue("");
                        R35cell4.setCellStyle(textStyle);
                    }
                    // R36 Col B
                    row = sheet.getRow(35);
                    Cell R36cell1 = row.createCell(1);
                    if (record2.getR36_ORIGINAL_AMT() != null) {
                        R36cell1.setCellValue(record2.getR36_ORIGINAL_AMT().doubleValue());
                        R36cell1.setCellStyle(numberStyle);
                    } else {
                        R36cell1.setCellValue("");
                        R36cell1.setCellStyle(textStyle);
                    }

                    // R36 Col C
                    Cell R36cell2 = row.createCell(2);
                    if (record2.getR36_BALANCE_OUTSTANDING() != null) {
                        R36cell2.setCellValue(record2.getR36_BALANCE_OUTSTANDING().doubleValue());
                        R36cell2.setCellStyle(numberStyle);
                    } else {
                        R36cell2.setCellValue("");
                        R36cell2.setCellStyle(textStyle);
                    }
                    // R36 Col D
                    Cell R36cell3 = row.createCell(3);
                    if (record2.getR36_NO_OF_ACS() != null) {
                        R36cell3.setCellValue(record2.getR36_NO_OF_ACS().doubleValue());
                        R36cell3.setCellStyle(numberStyle);
                    } else {
                        R36cell3.setCellValue("");
                        R36cell3.setCellStyle(textStyle);
                    }

                    // R36 Col E
                    Cell R36cell4 = row.createCell(4);
                    if (record2.getR36_INTEREST_RATE() != null) {
                        R36cell4.setCellValue(record2.getR36_INTEREST_RATE().doubleValue());
                        R36cell4.setCellStyle(numberStyle);
                    } else {
                        R36cell4.setCellValue("");
                        R36cell4.setCellStyle(textStyle);
                    }
                    // R37 Col B
                    row = sheet.getRow(36);
                    Cell R37cell1 = row.createCell(1);
                    if (record2.getR37_ORIGINAL_AMT() != null) {
                        R37cell1.setCellValue(record2.getR37_ORIGINAL_AMT().doubleValue());
                        R37cell1.setCellStyle(numberStyle);
                    } else {
                        R37cell1.setCellValue("");
                        R37cell1.setCellStyle(textStyle);
                    }

                    // R37 Col C
                    Cell R37cell2 = row.createCell(2);
                    if (record2.getR37_BALANCE_OUTSTANDING() != null) {
                        R37cell2.setCellValue(record2.getR37_BALANCE_OUTSTANDING().doubleValue());
                        R37cell2.setCellStyle(numberStyle);
                    } else {
                        R37cell2.setCellValue("");
                        R37cell2.setCellStyle(textStyle);
                    }
                    // R37 Col D
                    Cell R37cell3 = row.createCell(3);
                    if (record2.getR37_NO_OF_ACS() != null) {
                        R37cell3.setCellValue(record2.getR37_NO_OF_ACS().doubleValue());
                        R37cell3.setCellStyle(numberStyle);
                    } else {
                        R37cell3.setCellValue("");
                        R37cell3.setCellStyle(textStyle);
                    }

                    // R37 Col E
                    Cell R37cell4 = row.createCell(4);
                    if (record2.getR37_INTEREST_RATE() != null) {
                        R37cell4.setCellValue(record2.getR37_INTEREST_RATE().doubleValue());
                        R37cell4.setCellStyle(numberStyle);
                    } else {
                        R37cell4.setCellValue("");
                        R37cell4.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(37);
                    // R37 Col E
                    Cell R38cell4 = row.createCell(4);
                    if (record2.getR38_INTEREST_RATE() != null) {
                        R38cell4.setCellValue(record2.getR38_INTEREST_RATE().doubleValue());
                        R38cell4.setCellStyle(numberStyle);
                    } else {
                        R38cell4.setCellValue("");
                        R38cell4.setCellStyle(textStyle);
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

    ////////////////////////////////////////// RESUBMISSION///////////////////////////////////////////////////////////////////
    /// Report Date | Report Version | Domain
    /// RESUB VIEW


    public List<Object[]> getQ_STAFFResub() {
        List<Object[]> resubList = new ArrayList<>();
        try {
            List<Q_STAFF_Archival_Summary_Entity1> latestArchivalList = Q_STAFF_Archival_Summary_Repo1
                    .getdatabydateListWithVersion();

            if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
                for (Q_STAFF_Archival_Summary_Entity1 entity : latestArchivalList) {
                    resubList.add(new Object[] {
                            entity.getReportDate(),
                            entity.getReportVersion()
                    });
                }
                System.out.println("Fetched " + resubList.size() + " record(s)");
            } else {
                System.out.println("No archival data found.");
            }

        } catch (Exception e) {
            System.err.println("Error fetching M_SRWA_12H Resub data: " + e.getMessage());
            e.printStackTrace();
        }
        return resubList;
    }

    public List<Object[]> getQ_STAFFArchival() {
        List<Object[]> archivalList = new ArrayList<>();
        try {
            List<Q_STAFF_Archival_Summary_Entity1> latestArchivalList = Q_STAFF_Archival_Summary_Repo1
                    .getdatabydateListWithVersion();

            if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
                for (Q_STAFF_Archival_Summary_Entity1 entity : latestArchivalList) {
                    archivalList.add(new Object[] {
                            entity.getReportDate(),
                            entity.getReportVersion()
                    });
                }
                System.out.println("Fetched " + archivalList.size() + " record(s)");
            } else {
                System.out.println("No archival data found.");
            }

        } catch (Exception e) {
            System.err.println("Error fetching M_SRWA_12H Resub data: " + e.getMessage());
            e.printStackTrace();
        }
        return archivalList;
    }

    public void updateReportReSub(
            Q_STAFF_Summary_Entity1 updatedEntity1,
            Q_STAFF_Summary_Entity2 updatedEntity2,
            Q_STAFF_Summary_Entity3 updatedEntity3) {

        System.out.println("Came to Q_STAFF Resub Service");
        System.out.println("Report Date: " + updatedEntity1.getReportDate());

        Date reportDate = updatedEntity1.getReportDate();
        int newVersion = 1;

        try {
            // 🔹 Fetch the latest archival version for this report date from Entity1
            Optional<Q_STAFF_Archival_Summary_Entity1> latestArchivalOpt1 = Q_STAFF_Archival_Summary_Repo1
                    .getLatestArchivalVersionByDate(reportDate);

            if (latestArchivalOpt1.isPresent()) {
                Q_STAFF_Archival_Summary_Entity1 latestArchival = latestArchivalOpt1.get();
                try {
                    newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1;
                } catch (NumberFormatException e) {
                    System.err.println("Invalid version format. Defaulting to version 1");
                    newVersion = 1;
                }
            } else {
                System.out.println("No previous archival found for date: " + reportDate);
            }

            // 🔹 Prevent duplicate version number in Repo1
            boolean exists = Q_STAFF_Archival_Summary_Repo1
                    .findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
                    .isPresent();

            if (exists) {
                throw new RuntimeException("⚠ Version " + newVersion + " already exists for report date " + reportDate);
            }

            // Copy data from summary to archival entities for all 3 entities
            Q_STAFF_Archival_Summary_Entity1 archivalEntity1 = new Q_STAFF_Archival_Summary_Entity1();
            Q_STAFF_Archival_Summary_Entity2 archivalEntity2 = new Q_STAFF_Archival_Summary_Entity2();
            Q_STAFF_Archival_Summary_Entity3 archivalEntity3 = new Q_STAFF_Archival_Summary_Entity3();

            org.springframework.beans.BeanUtils.copyProperties(updatedEntity1, archivalEntity1);
            org.springframework.beans.BeanUtils.copyProperties(updatedEntity2, archivalEntity2);
            org.springframework.beans.BeanUtils.copyProperties(updatedEntity3, archivalEntity3);

            // Set common fields
            Date now = new Date();
            archivalEntity1.setReportDate(reportDate);
            archivalEntity2.setReportDate(reportDate);
            archivalEntity3.setReportDate(reportDate);

            archivalEntity1.setReportVersion(String.valueOf(newVersion));
            archivalEntity2.setReportVersion(String.valueOf(newVersion));
            archivalEntity3.setReportVersion(String.valueOf(newVersion));

            archivalEntity1.setReportResubDate(now);
            archivalEntity2.setReportResubDate(now);
            archivalEntity3.setReportResubDate(now);

            System.out.println("Saving new archival version: " + newVersion);

            // Save to all three archival repositories
            Q_STAFF_Archival_Summary_Repo1.save(archivalEntity1);
            Q_STAFF_Archival_Summary_Repo2.save(archivalEntity2);
            Q_STAFF_Archival_Summary_Repo3.save(archivalEntity3);

            System.out.println("Saved archival version successfully: " + newVersion);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while creating Q_STAFF archival resubmission record", e);
        }
    }

    public byte[] BRRS_Q_STAFFResubExcel(String filename, String reportId, String fromdate,
            String todate, String currency, String dtltype,
            String type, String version) throws Exception {

        logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

        if (type.equals("RESUB") & version != null) {

        }

        List<Q_STAFF_Archival_Summary_Entity1> dataList = Q_STAFF_Archival_Summary_Repo1
                .getdatabydateListarchival(dateformat.parse(todate), version);
        List<Q_STAFF_Archival_Summary_Entity2> dataList1 = Q_STAFF_Archival_Summary_Repo2
                .getdatabydateListarchival(dateformat.parse(todate), version);
        List<Q_STAFF_Archival_Summary_Entity3> dataList2 = Q_STAFF_Archival_Summary_Repo3
                .getdatabydateListarchival(dateformat.parse(todate), version);

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for M_SRWA_12H report. Returning empty result.");
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
            int startRow = 8;

            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {
                    Q_STAFF_Archival_Summary_Entity1 record = dataList.get(i);
                    Q_STAFF_Archival_Summary_Entity2 record1 = dataList1.get(i);
                    Q_STAFF_Archival_Summary_Entity3 record2 = dataList2.get(i);
                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }

                    // R9 Col B
                    Cell R9cell1 = row.createCell(1);
                    if (record.getR9_LOCAL() != null) {
                        R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
                        R9cell1.setCellStyle(numberStyle);
                    } else {
                        R9cell1.setCellValue("");
                        R9cell1.setCellStyle(textStyle);
                    }

                    // R9 Col C
                    Cell R9cell2 = row.createCell(2);
                    if (record.getR9_EXPARIATES() != null) {
                        R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
                        R9cell2.setCellStyle(numberStyle);
                    } else {
                        R9cell2.setCellValue("");
                        R9cell2.setCellStyle(textStyle);
                    }
                    // R10 Col B
                    row = sheet.getRow(9);
                    // R10 Col B
                    Cell R10cell1 = row.createCell(1);
                    if (record.getR10_LOCAL() != null) {
                        R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
                        R10cell1.setCellStyle(numberStyle);
                    } else {
                        R10cell1.setCellValue("");
                        R10cell1.setCellStyle(textStyle);
                    }

                    // R10 Col C
                    Cell R10cell2 = row.createCell(2);
                    if (record.getR10_EXPARIATES() != null) {
                        R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
                        R10cell2.setCellStyle(numberStyle);
                    } else {
                        R10cell2.setCellValue("");
                        R10cell2.setCellStyle(textStyle);
                    }
                    // R11 Col B
                    row = sheet.getRow(10);
                    // R11 Col B
                    Cell R11cell1 = row.createCell(1);
                    if (record.getR11_LOCAL() != null) {
                        R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
                        R11cell1.setCellStyle(numberStyle);
                    } else {
                        R11cell1.setCellValue("");
                        R11cell1.setCellStyle(textStyle);
                    }

                    // R11 Col C
                    Cell R11cell2 = row.createCell(2);
                    if (record.getR11_EXPARIATES() != null) {
                        R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
                        R11cell2.setCellStyle(numberStyle);
                    } else {
                        R11cell2.setCellValue("");
                        R11cell2.setCellStyle(textStyle);
                    }
                    // R12 Col B
                    row = sheet.getRow(11);

                    Cell R12cell1 = row.createCell(1);
                    if (record.getR12_LOCAL() != null) {
                        R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
                        R12cell1.setCellStyle(numberStyle);
                    } else {
                        R12cell1.setCellValue("");
                        R12cell1.setCellStyle(textStyle);
                    }

                    // R12 Col C
                    Cell R12cell2 = row.createCell(2);
                    if (record.getR12_EXPARIATES() != null) {
                        R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
                        R12cell2.setCellStyle(numberStyle);
                    } else {
                        R12cell2.setCellValue("");
                        R12cell2.setCellStyle(textStyle);
                    }
                    // R13 Col B
                    row = sheet.getRow(12);

                    Cell R13cell1 = row.createCell(1);
                    if (record.getR13_LOCAL() != null) {
                        R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
                        R13cell1.setCellStyle(numberStyle);
                    } else {
                        R13cell1.setCellValue("");
                        R13cell1.setCellStyle(textStyle);
                    }

                    // R13 Col C
                    Cell R13cell2 = row.createCell(2);
                    if (record.getR13_EXPARIATES() != null) {
                        R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
                        R13cell2.setCellStyle(numberStyle);
                    } else {
                        R13cell2.setCellValue("");
                        R13cell2.setCellStyle(textStyle);
                    }
                    // R14 Col B
                    row = sheet.getRow(13);
                    Cell R14cell1 = row.createCell(1);
                    if (record.getR14_LOCAL() != null) {
                        R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
                        R14cell1.setCellStyle(numberStyle);
                    } else {
                        R14cell1.setCellValue("");
                        R14cell1.setCellStyle(textStyle);
                    }

                    // R14 Col C
                    Cell R14cell2 = row.createCell(2);
                    if (record.getR14_EXPARIATES() != null) {
                        R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
                        R14cell2.setCellStyle(numberStyle);
                    } else {
                        R14cell2.setCellValue("");
                        R14cell2.setCellStyle(textStyle);
                    }

                    // TABLE 2
                    // R21 Col B
                    row = sheet.getRow(20);
                    Cell R21cell1 = row.createCell(1);
                    if (record1.getR21_LOCAL() != null) {
                        R21cell1.setCellValue(record1.getR21_LOCAL().doubleValue());
                        R21cell1.setCellStyle(numberStyle);
                    } else {
                        R21cell1.setCellValue("");
                        R21cell1.setCellStyle(textStyle);
                    }
                    // R21 COL C
                    Cell R21cell2 = row.createCell(2);
                    if (record1.getR21_EXPARIATES() != null) {
                        R21cell2.setCellValue(record1.getR21_EXPARIATES().doubleValue());
                        R21cell2.setCellStyle(numberStyle);
                    } else {
                        R21cell2.setCellValue("");
                        R21cell2.setCellStyle(textStyle);
                    }
                    // R22 Col B
                    row = sheet.getRow(21);
                    Cell R22cell1 = row.createCell(1);
                    if (record1.getR22_LOCAL() != null) {
                        R22cell1.setCellValue(record1.getR22_LOCAL().doubleValue());
                        R22cell1.setCellStyle(numberStyle);
                    } else {
                        R22cell1.setCellValue("");
                        R22cell1.setCellStyle(textStyle);
                    }

                    // R22 Col C
                    Cell R22cell2 = row.createCell(2);
                    if (record1.getR22_EXPARIATES() != null) {
                        R22cell2.setCellValue(record1.getR22_EXPARIATES().doubleValue());
                        R22cell2.setCellStyle(numberStyle);
                    } else {
                        R22cell2.setCellValue("");
                        R22cell2.setCellStyle(textStyle);
                    }
                    // R23 Col B
                    row = sheet.getRow(22);
                    // R23 Col B
                    Cell R23cell1 = row.createCell(1);
                    if (record1.getR23_LOCAL() != null) {
                        R23cell1.setCellValue(record1.getR23_LOCAL().doubleValue());
                        R23cell1.setCellStyle(numberStyle);
                    } else {
                        R23cell1.setCellValue("");
                        R23cell1.setCellStyle(textStyle);
                    }

                    // R23 Col C
                    Cell R23cell2 = row.createCell(2);
                    if (record1.getR23_EXPARIATES() != null) {
                        R23cell2.setCellValue(record1.getR23_EXPARIATES().doubleValue());
                        R23cell2.setCellStyle(numberStyle);
                    } else {
                        R23cell2.setCellValue("");
                        R23cell2.setCellStyle(textStyle);
                    }
                    // R24 Col B
                    row = sheet.getRow(23);
                    // R24 Col B
                    Cell R24cell1 = row.createCell(1);
                    if (record1.getR24_LOCAL() != null) {
                        R24cell1.setCellValue(record1.getR24_LOCAL().doubleValue());
                        R24cell1.setCellStyle(numberStyle);
                    } else {
                        R24cell1.setCellValue("");
                        R24cell1.setCellStyle(textStyle);
                    }

                    // R24 Col C
                    Cell R24cell2 = row.createCell(2);
                    if (record1.getR24_EXPARIATES() != null) {
                        R24cell2.setCellValue(record1.getR24_EXPARIATES().doubleValue());
                        R24cell2.setCellStyle(numberStyle);
                    } else {
                        R24cell2.setCellValue("");
                        R24cell2.setCellStyle(textStyle);
                    }
                    // R25 Col B
                    row = sheet.getRow(24);
                    // R25 Col B
                    Cell R25cell1 = row.createCell(1);
                    if (record1.getR25_LOCAL() != null) {
                        R25cell1.setCellValue(record1.getR25_LOCAL().doubleValue());
                        R25cell1.setCellStyle(numberStyle);
                    } else {
                        R25cell1.setCellValue("");
                        R25cell1.setCellStyle(textStyle);
                    }

                    // R25 Col C
                    Cell R25cell2 = row.createCell(2);
                    if (record1.getR25_EXPARIATES() != null) {
                        R25cell2.setCellValue(record1.getR25_EXPARIATES().doubleValue());
                        R25cell2.setCellStyle(numberStyle);
                    } else {
                        R25cell2.setCellValue("");
                        R25cell2.setCellStyle(textStyle);
                    }
                    // R26 Col B
                    row = sheet.getRow(25);
                    // R26 Col B
                    Cell R26cell1 = row.createCell(1);
                    if (record1.getR26_LOCAL() != null) {
                        R26cell1.setCellValue(record1.getR26_LOCAL().doubleValue());
                        R26cell1.setCellStyle(numberStyle);
                    } else {
                        R26cell1.setCellValue("");
                        R26cell1.setCellStyle(textStyle);
                    }

                    // R26 Col C
                    Cell R26cell2 = row.createCell(2);
                    if (record1.getR26_EXPARIATES() != null) {
                        R26cell2.setCellValue(record1.getR26_EXPARIATES().doubleValue());
                        R26cell2.setCellStyle(numberStyle);
                    } else {
                        R26cell2.setCellValue("");
                        R26cell2.setCellStyle(textStyle);
                    }
                    // R27 Col B
                    row = sheet.getRow(26);
                    // R27 Col B
                    Cell R27cell1 = row.createCell(1);
                    if (record1.getR27_LOCAL() != null) {
                        R27cell1.setCellValue(record1.getR27_LOCAL().doubleValue());
                        R27cell1.setCellStyle(numberStyle);
                    } else {
                        R27cell1.setCellValue("");
                        R27cell1.setCellStyle(textStyle);
                    }

                    // R27 Col C
                    Cell R27cell2 = row.createCell(2);
                    if (record1.getR27_EXPARIATES() != null) {
                        R27cell2.setCellValue(record1.getR27_EXPARIATES().doubleValue());
                        R27cell2.setCellStyle(numberStyle);
                    } else {
                        R27cell2.setCellValue("");
                        R27cell2.setCellStyle(textStyle);
                    }
                    // TABLE 3
                    // R33 Col B
                    row = sheet.getRow(32);
                    Cell R33cell1 = row.createCell(1);
                    if (record2.getR33_ORIGINAL_AMT() != null) {
                        R33cell1.setCellValue(record2.getR33_ORIGINAL_AMT().doubleValue());
                        R33cell1.setCellStyle(numberStyle);
                    } else {
                        R33cell1.setCellValue("");
                        R33cell1.setCellStyle(textStyle);
                    }

                    // R33 Col C
                    Cell R33cell2 = row.createCell(2);
                    if (record2.getR33_BALANCE_OUTSTANDING() != null) {
                        R33cell2.setCellValue(record2.getR33_BALANCE_OUTSTANDING().doubleValue());
                        R33cell2.setCellStyle(numberStyle);
                    } else {
                        R33cell2.setCellValue("");
                        R33cell2.setCellStyle(textStyle);
                    }
                    // R33 Col D
                    Cell R33cell3 = row.createCell(3);
                    if (record2.getR33_NO_OF_ACS() != null) {
                        R33cell3.setCellValue(record2.getR33_NO_OF_ACS().doubleValue());
                        R33cell3.setCellStyle(numberStyle);
                    } else {
                        R33cell3.setCellValue("");
                        R33cell3.setCellStyle(textStyle);
                    }

                    // R33 Col E
                    Cell R33cell4 = row.createCell(4);
                    if (record2.getR33_INTEREST_RATE() != null) {
                        R33cell4.setCellValue(record2.getR33_INTEREST_RATE().doubleValue());
                        R33cell4.setCellStyle(numberStyle);
                    } else {
                        R33cell4.setCellValue("");
                        R33cell4.setCellStyle(textStyle);
                    }
                    // R34 Col B
                    row = sheet.getRow(33);
                    Cell R34cell1 = row.createCell(1);
                    if (record2.getR34_ORIGINAL_AMT() != null) {
                        R34cell1.setCellValue(record2.getR34_ORIGINAL_AMT().doubleValue());
                        R34cell1.setCellStyle(numberStyle);
                    } else {
                        R34cell1.setCellValue("");
                        R34cell1.setCellStyle(textStyle);
                    }

                    // R34 Col C
                    Cell R34cell2 = row.createCell(2);
                    if (record2.getR34_BALANCE_OUTSTANDING() != null) {
                        R34cell2.setCellValue(record2.getR34_BALANCE_OUTSTANDING().doubleValue());
                        R34cell2.setCellStyle(numberStyle);
                    } else {
                        R34cell2.setCellValue("");
                        R34cell2.setCellStyle(textStyle);
                    }
                    // R34 Col D
                    Cell R34cell3 = row.createCell(3);
                    if (record2.getR34_NO_OF_ACS() != null) {
                        R34cell3.setCellValue(record2.getR34_NO_OF_ACS().doubleValue());
                        R34cell3.setCellStyle(numberStyle);
                    } else {
                        R34cell3.setCellValue("");
                        R34cell3.setCellStyle(textStyle);
                    }

                    // R34 Col E
                    Cell R34cell4 = row.createCell(4);
                    if (record2.getR34_INTEREST_RATE() != null) {
                        R34cell4.setCellValue(record2.getR34_INTEREST_RATE().doubleValue());
                        R34cell4.setCellStyle(numberStyle);
                    } else {
                        R34cell4.setCellValue("");
                        R34cell4.setCellStyle(textStyle);
                    }
                    // R35 Col B
                    row = sheet.getRow(34);

                    Cell R35cell1 = row.createCell(1);
                    if (record2.getR35_ORIGINAL_AMT() != null) {
                        R35cell1.setCellValue(record2.getR35_ORIGINAL_AMT().doubleValue());
                        R35cell1.setCellStyle(numberStyle);
                    } else {
                        R35cell1.setCellValue("");
                        R35cell1.setCellStyle(textStyle);
                    }

                    // R35 Col C
                    Cell R35cell2 = row.createCell(2);
                    if (record2.getR35_BALANCE_OUTSTANDING() != null) {
                        R35cell2.setCellValue(record2.getR35_BALANCE_OUTSTANDING().doubleValue());
                        R35cell2.setCellStyle(numberStyle);
                    } else {
                        R35cell2.setCellValue("");
                        R35cell2.setCellStyle(textStyle);
                    }
                    // R35 Col D
                    Cell R35cell3 = row.createCell(3);
                    if (record2.getR35_NO_OF_ACS() != null) {
                        R35cell3.setCellValue(record2.getR35_NO_OF_ACS().doubleValue());
                        R35cell3.setCellStyle(numberStyle);
                    } else {
                        R35cell3.setCellValue("");
                        R35cell3.setCellStyle(textStyle);
                    }

                    // R35 Col E
                    Cell R35cell4 = row.createCell(4);
                    if (record2.getR35_INTEREST_RATE() != null) {
                        R35cell4.setCellValue(record2.getR35_INTEREST_RATE().doubleValue());
                        R35cell4.setCellStyle(numberStyle);
                    } else {
                        R35cell4.setCellValue("");
                        R35cell4.setCellStyle(textStyle);
                    }
                    // R36 Col B
                    row = sheet.getRow(35);
                    Cell R36cell1 = row.createCell(1);
                    if (record2.getR36_ORIGINAL_AMT() != null) {
                        R36cell1.setCellValue(record2.getR36_ORIGINAL_AMT().doubleValue());
                        R36cell1.setCellStyle(numberStyle);
                    } else {
                        R36cell1.setCellValue("");
                        R36cell1.setCellStyle(textStyle);
                    }

                    // R36 Col C
                    Cell R36cell2 = row.createCell(2);
                    if (record2.getR36_BALANCE_OUTSTANDING() != null) {
                        R36cell2.setCellValue(record2.getR36_BALANCE_OUTSTANDING().doubleValue());
                        R36cell2.setCellStyle(numberStyle);
                    } else {
                        R36cell2.setCellValue("");
                        R36cell2.setCellStyle(textStyle);
                    }
                    // R36 Col D
                    Cell R36cell3 = row.createCell(3);
                    if (record2.getR36_NO_OF_ACS() != null) {
                        R36cell3.setCellValue(record2.getR36_NO_OF_ACS().doubleValue());
                        R36cell3.setCellStyle(numberStyle);
                    } else {
                        R36cell3.setCellValue("");
                        R36cell3.setCellStyle(textStyle);
                    }

                    // R36 Col E
                    Cell R36cell4 = row.createCell(4);
                    if (record2.getR36_INTEREST_RATE() != null) {
                        R36cell4.setCellValue(record2.getR36_INTEREST_RATE().doubleValue());
                        R36cell4.setCellStyle(numberStyle);
                    } else {
                        R36cell4.setCellValue("");
                        R36cell4.setCellStyle(textStyle);
                    }
                    // R37 Col B
                    row = sheet.getRow(36);
                    Cell R37cell1 = row.createCell(1);
                    if (record2.getR37_ORIGINAL_AMT() != null) {
                        R37cell1.setCellValue(record2.getR37_ORIGINAL_AMT().doubleValue());
                        R37cell1.setCellStyle(numberStyle);
                    } else {
                        R37cell1.setCellValue("");
                        R37cell1.setCellStyle(textStyle);
                    }

                    // R37 Col C
                    Cell R37cell2 = row.createCell(2);
                    if (record2.getR37_BALANCE_OUTSTANDING() != null) {
                        R37cell2.setCellValue(record2.getR37_BALANCE_OUTSTANDING().doubleValue());
                        R37cell2.setCellStyle(numberStyle);
                    } else {
                        R37cell2.setCellValue("");
                        R37cell2.setCellStyle(textStyle);
                    }
                    // R37 Col D
                    Cell R37cell3 = row.createCell(3);
                    if (record2.getR37_NO_OF_ACS() != null) {
                        R37cell3.setCellValue(record2.getR37_NO_OF_ACS().doubleValue());
                        R37cell3.setCellStyle(numberStyle);
                    } else {
                        R37cell3.setCellValue("");
                        R37cell3.setCellStyle(textStyle);
                    }

                    // R37 Col E
                    Cell R37cell4 = row.createCell(4);
                    if (record2.getR37_INTEREST_RATE() != null) {
                        R37cell4.setCellValue(record2.getR37_INTEREST_RATE().doubleValue());
                        R37cell4.setCellStyle(numberStyle);
                    } else {
                        R37cell4.setCellValue("");
                        R37cell4.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(37);
                    // R37 Col E
                    Cell R38cell4 = row.createCell(4);
                    if (record2.getR38_INTEREST_RATE() != null) {
                        R38cell4.setCellValue(record2.getR38_INTEREST_RATE().doubleValue());
                        R38cell4.setCellStyle(numberStyle);
                    } else {
                        R38cell4.setCellValue("");
                        R38cell4.setCellStyle(textStyle);
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
}
