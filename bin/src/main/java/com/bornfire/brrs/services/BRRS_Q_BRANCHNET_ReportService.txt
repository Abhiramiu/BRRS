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
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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
import com.bornfire.brrs.entities.BRRS_Q_BRANCHNET_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_Q_BRANCHNET_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_Q_BRANCHNET_Archival_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_Q_BRANCHNET_Archival_Summary_Repo4;
import com.bornfire.brrs.entities.BRRS_Q_BRANCHNET_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_Q_BRANCHNET_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_Q_BRANCHNET_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_Q_BRANCHNET_Summary_Repo4;
import com.bornfire.brrs.entities.Q_BRANCHNET_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.Q_BRANCHNET_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.Q_BRANCHNET_Archival_Summary_Entity3;
import com.bornfire.brrs.entities.Q_BRANCHNET_Archival_Summary_Entity4;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity1;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity2;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity3;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity4;

@Component
@Service
public class BRRS_Q_BRANCHNET_ReportService {

    private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_BRANCHNET_ReportService.class);

    @Autowired
    private Environment env;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    AuditService auditService;

    @Autowired
    BRRS_Q_BRANCHNET_Summary_Repo1 Q_BRANCHNET_Summary_Repo1;
    @Autowired
    BRRS_Q_BRANCHNET_Summary_Repo2 Q_BRANCHNET_Summary_Repo2;
    @Autowired
    BRRS_Q_BRANCHNET_Summary_Repo3 Q_BRANCHNET_Summary_Repo3;
    @Autowired
    BRRS_Q_BRANCHNET_Summary_Repo4 Q_BRANCHNET_Summary_Repo4;

    @Autowired
    BRRS_Q_BRANCHNET_Archival_Summary_Repo1 Q_BRANCHNET_Archival_Summary_Repo1;
    @Autowired
    BRRS_Q_BRANCHNET_Archival_Summary_Repo2 Q_BRANCHNET_Archival_Summary_Repo2;
    @Autowired
    BRRS_Q_BRANCHNET_Archival_Summary_Repo3 Q_BRANCHNET_Archival_Summary_Repo3;
    @Autowired
    BRRS_Q_BRANCHNET_Archival_Summary_Repo4 Q_BRANCHNET_Archival_Summary_Repo4;

    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

    public ModelAndView getQ_BRANCHNETView(
            String reportId, String fromdate, String todate,
            String currency, String dtltype, Pageable pageable,
            String type, BigDecimal version) {

        ModelAndView mv = new ModelAndView();
        Session hs = sessionFactory.getCurrentSession();

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        try {
            Date d1 = dateformat.parse(todate);

            // ---------- CASE 1: ARCHIVAL ----------
            if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

                List<Q_BRANCHNET_Archival_Summary_Entity1> T1Master = Q_BRANCHNET_Archival_Summary_Repo1
                        .getdatabydateListarchival(d1, version);
                List<Q_BRANCHNET_Archival_Summary_Entity2> T2Master = Q_BRANCHNET_Archival_Summary_Repo2
                        .getdatabydateListarchival(d1, version);
                List<Q_BRANCHNET_Archival_Summary_Entity3> T3Master = Q_BRANCHNET_Archival_Summary_Repo3
                        .getdatabydateListarchival(d1, version);
                List<Q_BRANCHNET_Archival_Summary_Entity4> T4Master = Q_BRANCHNET_Archival_Summary_Repo4
                        .getdatabydateListarchival(d1, version);

                mv.addObject("reportsummary", T1Master);
                mv.addObject("reportsummary1", T2Master);
                mv.addObject("reportsummary2", T3Master);
                mv.addObject("reportsummary3", T4Master);
            }

            // ---------- CASE 2: RESUB ----------
            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

                List<Q_BRANCHNET_Archival_Summary_Entity1> T1Master = Q_BRANCHNET_Archival_Summary_Repo1
                        .getdatabydateListarchival(d1, version);
                List<Q_BRANCHNET_Archival_Summary_Entity2> T2Master = Q_BRANCHNET_Archival_Summary_Repo2
                        .getdatabydateListarchival(d1, version);
                List<Q_BRANCHNET_Archival_Summary_Entity3> T3Master = Q_BRANCHNET_Archival_Summary_Repo3
                        .getdatabydateListarchival(d1, version);
                List<Q_BRANCHNET_Archival_Summary_Entity4> T4Master = Q_BRANCHNET_Archival_Summary_Repo4
                        .getdatabydateListarchival(d1, version);

                mv.addObject("reportsummary", T1Master);
                mv.addObject("reportsummary1", T2Master);
                mv.addObject("reportsummary2", T3Master);
                mv.addObject("reportsummary3", T4Master);
            }

            // ---------- CASE 3: NORMAL ----------
            else {

                List<Q_BRANCHNET_Summary_Entity1> T1Master = Q_BRANCHNET_Summary_Repo1.getdatabydateList(d1);
                List<Q_BRANCHNET_Summary_Entity2> T2Master = Q_BRANCHNET_Summary_Repo2.getdatabydateList(d1);
                List<Q_BRANCHNET_Summary_Entity3> T3Master = Q_BRANCHNET_Summary_Repo3.getdatabydateList(d1);
                List<Q_BRANCHNET_Summary_Entity4> T4Master = Q_BRANCHNET_Summary_Repo4.getdatabydateList(d1);

                System.out.println("T1Master Size: " + T1Master.size());
                System.out.println("T2Master Size: " + T2Master.size());
                System.out.println("T3Master Size: " + T3Master.size());
                System.out.println("T4Master Size: " + T4Master.size());

                mv.addObject("reportsummary", T1Master);
                mv.addObject("reportsummary1", T2Master);
                mv.addObject("reportsummary2", T3Master);
                mv.addObject("reportsummary3", T4Master);
            }

            mv.setViewName("BRRS/Q_BRANCHNET");
            mv.addObject("displaymode", "summary");
            System.out.println("✅ View set: " + mv.getViewName());

        } catch (ParseException e) {
            e.printStackTrace();
            mv.addObject("error", "Invalid date format for: " + todate);
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("error", "An error occurred while fetching Q_BRANCHNET data.");
        }

        return mv;
    }

    public void QBranchnetUpdate1(Q_BRANCHNET_Summary_Entity1 updatedEntity) {
        System.out.println("Came to services");
        System.out.println("Report Date: " + updatedEntity.getReportDate());

        Q_BRANCHNET_Summary_Entity1 existing = Q_BRANCHNET_Summary_Repo1.findById(updatedEntity.getReportDate())
                .orElseThrow(() -> new RuntimeException(
                        "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

        try {
            // 1️⃣ Loop from R10 to R19 and copy fields
            for (int i = 10; i <= 20; i++) {
                String prefix = "R" + i + "_";

                String[] fields = { "bran_sub_bran_district", "no1_of_branches", "no1_of_sub_branches",
                        "no1_of_agencies" };

                for (String field : fields) {
                    String getterName = "getR" + i + "_" + field;
                    String setterName = "setR" + i + "_" + field;

                    try {
                        Method getter = Q_BRANCHNET_Summary_Entity1.class.getMethod(getterName);
                        Method setter = Q_BRANCHNET_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

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

        // 3️⃣ Save updated entity
        Q_BRANCHNET_Summary_Repo1.save(existing);
    }

    public void QBranchnetUpdate2(Q_BRANCHNET_Summary_Entity2 updatedEntity) {
        System.out.println("Came to services");
        System.out.println("Report Date: " + updatedEntity.getReportDate());

        Q_BRANCHNET_Summary_Entity2 existing = Q_BRANCHNET_Summary_Repo2.findById(updatedEntity.getReportDate())
                .orElseThrow(() -> new RuntimeException(
                        "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

        try {
            // 1️⃣ Loop from R25 to R34 and copy fields
            for (int i = 25; i <= 35; i++) {
                String prefix = "R" + i + "_";

                String[] fields = { "atm_mini_atm_district", "no_of_atms", "no_of_mini_atms", "encashment_points" };

                for (String field : fields) {
                    String getterName = "getR" + i + "_" + field;
                    String setterName = "setR" + i + "_" + field;

                    try {
                        Method getter = Q_BRANCHNET_Summary_Entity2.class.getMethod(getterName);
                        Method setter = Q_BRANCHNET_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

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

        // 3️⃣ Save updated entity
        Q_BRANCHNET_Summary_Repo2.save(existing);
    }

    public void QBranchnetUpdate3(Q_BRANCHNET_Summary_Entity3 updatedEntity) {
        System.out.println("Came to services");
        System.out.println("Report Date: " + updatedEntity.getReportDate());

        Q_BRANCHNET_Summary_Entity3 existing = Q_BRANCHNET_Summary_Repo3.findById(updatedEntity.getReportDate())
                .orElseThrow(() -> new RuntimeException(
                        "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

        try {
            // 1️⃣ Loop from R40 to R49 and copy fields
            for (int i = 40; i <= 50; i++) {
                String prefix = "R" + i + "_";

                String[] fields = { "debit_district", "opening_no_of_cards", "no_of_cards_issued", "no_cards_of_closed",
                        "closing_bal_of_active_cards" };

                for (String field : fields) {
                    String getterName = "getR" + i + "_" + field;
                    String setterName = "setR" + i + "_" + field;

                    try {
                        Method getter = Q_BRANCHNET_Summary_Entity3.class.getMethod(getterName);
                        Method setter = Q_BRANCHNET_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());

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

        // 3️⃣ Save updated entity
        Q_BRANCHNET_Summary_Repo3.save(existing);
    }

    public void QBranchnetUpdate4(Q_BRANCHNET_Summary_Entity4 updatedEntity) {
        System.out.println("Came to services");
        System.out.println("Report Date: " + updatedEntity.getReportDate());

        Q_BRANCHNET_Summary_Entity4 existing = Q_BRANCHNET_Summary_Repo4.findById(updatedEntity.getReportDate())
                .orElseThrow(() -> new RuntimeException(
                        "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

        try {
            // 1️⃣ Loop from R55 to R64 and copy fields
            for (int i = 55; i <= 65; i++) {
                String prefix = "R" + i + "_";

                String[] fields = { "credit_district", "opening_no_of_cards", "no_of_cards_issued",
                        "no_cards_of_closed", "closing_bal_of_active_cards" };

                for (String field : fields) {
                    String getterName = "getR" + i + "_" + field;
                    String setterName = "setR" + i + "_" + field;

                    try {
                        Method getter = Q_BRANCHNET_Summary_Entity4.class.getMethod(getterName);
                        Method setter = Q_BRANCHNET_Summary_Entity4.class.getMethod(setterName, getter.getReturnType());

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

        // 3️⃣ Save updated entity
        Q_BRANCHNET_Summary_Repo4.save(existing);
    }

    // public List<Object> getQ_BRANCHNETArchival() {
    // List<Object> Q_BRANCHNETArchivallist = new ArrayList<>();
    // List<Object> Q_BRANCHNETArchivallist1 = new ArrayList<>();
    // List<Object> Q_BRANCHNETArchivallist2 = new ArrayList<>();
    // List<Object> Q_BRANCHNETArchivallist3 = new ArrayList<>();
    // try {
    // Q_BRANCHNETArchivallist =
    // Q_BRANCHNET_Archival_Summary_Repo1.getQ_BRANCHNETarchival();
    // Q_BRANCHNETArchivallist1 =
    // Q_BRANCHNET_Archival_Summary_Repo2.getQ_BRANCHNETarchival();
    // Q_BRANCHNETArchivallist2 =
    // Q_BRANCHNET_Archival_Summary_Repo3.getQ_BRANCHNETarchival();
    // Q_BRANCHNETArchivallist3 =
    // Q_BRANCHNET_Archival_Summary_Repo3.getQ_BRANCHNETarchival();
    // System.out.println("countser" + Q_BRANCHNETArchivallist.size());
    // System.out.println("countser" + Q_BRANCHNETArchivallist1.size());
    // System.out.println("countser" + Q_BRANCHNETArchivallist2.size());
    // System.out.println("countser" + Q_BRANCHNETArchivallist3.size());
    // } catch (Exception e) {
    // // Log the exception
    // System.err.println("Error fetching Q_BRANCHNET Archival data: " +
    // e.getMessage());
    // e.printStackTrace();

    // }
    // return Q_BRANCHNETArchivallist;
    // }
    public byte[] BRRS_Q_BRANCHNETExcel(String filename, String reportId, String fromdate, String todate,
            String currency,
            String dtltype, String type, BigDecimal version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");
        System.out.println(type);
        System.out.println(version);
        Date reportDate = dateformat.parse(todate);

        // ARCHIVAL check
        if (type.equals("ARCHIVAL") & version != null) {
            logger.info("Service: Generating ARCHIVAL report for version {}", version);
            return getExcelQ_BRANCHNETARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);

        }
        // RESUB check
        // if (type.equals("RESUB") & version != null) {
        //     logger.info("Service: Generating RESUB report for version {}", version);

        //     List<Q_BRANCHNET_Archival_Summary_Entity1> dataList = Q_BRANCHNET_Archival_Summary_Repo1
        //             .getdatabydateListarchival(dateformat.parse(todate), version);
        //     List<Q_BRANCHNET_Archival_Summary_Entity2> dataList1 = Q_BRANCHNET_Archival_Summary_Repo2
        //             .getdatabydateListarchival(dateformat.parse(todate), version);
        //     List<Q_BRANCHNET_Archival_Summary_Entity3> dataList2 = Q_BRANCHNET_Archival_Summary_Repo3
        //             .getdatabydateListarchival(dateformat.parse(todate), version);
        //     List<Q_BRANCHNET_Archival_Summary_Entity4> dataList3 = Q_BRANCHNET_Archival_Summary_Repo4
        //             .getdatabydateListarchival(dateformat.parse(todate), version);
        //     // Generate Excel for RESUB
        //     return BRRS_Q_BRANCHNETResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
        // }
        List<Q_BRANCHNET_Summary_Entity1> dataList = Q_BRANCHNET_Summary_Repo1
                .getdatabydateList(dateformat.parse(todate));
        List<Q_BRANCHNET_Summary_Entity2> dataList1 = Q_BRANCHNET_Summary_Repo2
                .getdatabydateList(dateformat.parse(todate));
        List<Q_BRANCHNET_Summary_Entity3> dataList2 = Q_BRANCHNET_Summary_Repo3
                .getdatabydateList(dateformat.parse(todate));
        List<Q_BRANCHNET_Summary_Entity4> dataList3 = Q_BRANCHNET_Summary_Repo4
                .getdatabydateList(dateformat.parse(todate));

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

            // --- End of Style Definitions ---
            int startRow = 9;

            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {

                    Q_BRANCHNET_Summary_Entity1 record = dataList.get(i);
                    Q_BRANCHNET_Summary_Entity2 record1 = dataList1.get(i);
                    Q_BRANCHNET_Summary_Entity3 record2 = dataList2.get(i);
                    Q_BRANCHNET_Summary_Entity4 record3 = dataList3.get(i);
                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }
                    // R10 Col C

                    Cell R10cell1 = row.createCell(2);
                    if (record.getR10_no1_of_branches() != null) {
                        R10cell1.setCellValue(record.getR10_no1_of_branches().doubleValue());
                        R10cell1.setCellStyle(numberStyle);
                    } else {
                        R10cell1.setCellValue("");
                        R10cell1.setCellStyle(textStyle);
                    }

                    // R10 Col D
                    Cell R10cell2 = row.createCell(3);
                    if (record.getR10_no1_of_sub_branches() != null) {
                        R10cell2.setCellValue(record.getR10_no1_of_sub_branches().doubleValue());
                        R10cell2.setCellStyle(numberStyle);
                    } else {
                        R10cell2.setCellValue("");
                        R10cell2.setCellStyle(textStyle);
                    }

                    // R10 Col E
                    Cell R10cell3 = row.createCell(4);
                    if (record.getR10_no1_of_agencies() != null) {
                        R10cell3.setCellValue(record.getR10_no1_of_agencies().doubleValue());
                        R10cell3.setCellStyle(numberStyle);
                    } else {
                        R10cell3.setCellValue("");
                        R10cell3.setCellStyle(textStyle);
                    }
                    // R11 Col C
                    row = sheet.getRow(10);
                    Cell R11cell1 = row.createCell(2);
                    if (record.getR11_no1_of_branches() != null) {
                        R11cell1.setCellValue(record.getR11_no1_of_branches().doubleValue());
                        R11cell1.setCellStyle(numberStyle);
                    } else {
                        R11cell1.setCellValue("");
                        R11cell1.setCellStyle(textStyle);
                    }

                    // R11 Col D
                    Cell R11cell2 = row.createCell(3);
                    if (record.getR11_no1_of_sub_branches() != null) {
                        R11cell2.setCellValue(record.getR11_no1_of_sub_branches().doubleValue());
                        R11cell2.setCellStyle(numberStyle);
                    } else {
                        R11cell2.setCellValue("");
                        R11cell2.setCellStyle(textStyle);
                    }

                    // R11 Col E
                    Cell R11cell3 = row.createCell(4);
                    if (record.getR11_no1_of_agencies() != null) {
                        R11cell3.setCellValue(record.getR11_no1_of_agencies().doubleValue());
                        R11cell3.setCellStyle(numberStyle);
                    } else {
                        R11cell3.setCellValue("");
                        R11cell3.setCellStyle(textStyle);
                    }
                    // R12 Col C
                    row = sheet.getRow(11);
                    Cell R12cell1 = row.createCell(2);
                    if (record.getR12_no1_of_branches() != null) {
                        R12cell1.setCellValue(record.getR12_no1_of_branches().doubleValue());
                        R12cell1.setCellStyle(numberStyle);
                    } else {
                        R12cell1.setCellValue("");
                        R12cell1.setCellStyle(textStyle);
                    }

                    // R12 Col D
                    Cell R12cell2 = row.createCell(3);
                    if (record.getR12_no1_of_sub_branches() != null) {
                        R12cell2.setCellValue(record.getR12_no1_of_sub_branches().doubleValue());
                        R12cell2.setCellStyle(numberStyle);
                    } else {
                        R12cell2.setCellValue("");
                        R12cell2.setCellStyle(textStyle);
                    }

                    // R12 Col E
                    Cell R12cell3 = row.createCell(4);
                    if (record.getR12_no1_of_agencies() != null) {
                        R12cell3.setCellValue(record.getR12_no1_of_agencies().doubleValue());
                        R12cell3.setCellStyle(numberStyle);
                    } else {
                        R12cell3.setCellValue("");
                        R12cell3.setCellStyle(textStyle);
                    }
                    // R13 Col C
                    row = sheet.getRow(12);
                    Cell R13cell1 = row.createCell(2);
                    if (record.getR13_no1_of_branches() != null) {
                        R13cell1.setCellValue(record.getR13_no1_of_branches().doubleValue());
                        R13cell1.setCellStyle(numberStyle);
                    } else {
                        R13cell1.setCellValue("");
                        R13cell1.setCellStyle(textStyle);
                    }

                    // R13 Col D
                    Cell R13cell2 = row.createCell(3);
                    if (record.getR13_no1_of_sub_branches() != null) {
                        R13cell2.setCellValue(record.getR13_no1_of_sub_branches().doubleValue());
                        R13cell2.setCellStyle(numberStyle);
                    } else {
                        R13cell2.setCellValue("");
                        R13cell2.setCellStyle(textStyle);
                    }

                    // R13 Col E
                    Cell R13cell3 = row.createCell(4);
                    if (record.getR13_no1_of_agencies() != null) {
                        R13cell3.setCellValue(record.getR13_no1_of_agencies().doubleValue());
                        R13cell3.setCellStyle(numberStyle);
                    } else {
                        R13cell3.setCellValue("");
                        R13cell3.setCellStyle(textStyle);
                    }
                    // R14 Col C
                    row = sheet.getRow(13);
                    Cell R14cell1 = row.createCell(2);
                    if (record.getR14_no1_of_branches() != null) {
                        R14cell1.setCellValue(record.getR14_no1_of_branches().doubleValue());
                        R14cell1.setCellStyle(numberStyle);
                    } else {
                        R14cell1.setCellValue("");
                        R14cell1.setCellStyle(textStyle);
                    }

                    // R14 Col D
                    Cell R14cell2 = row.createCell(3);
                    if (record.getR14_no1_of_sub_branches() != null) {
                        R14cell2.setCellValue(record.getR14_no1_of_sub_branches().doubleValue());
                        R14cell2.setCellStyle(numberStyle);
                    } else {
                        R14cell2.setCellValue("");
                        R14cell2.setCellStyle(textStyle);
                    }

                    // R14 Col E
                    Cell R14cell3 = row.createCell(4);
                    if (record.getR14_no1_of_agencies() != null) {
                        R14cell3.setCellValue(record.getR14_no1_of_agencies().doubleValue());
                        R14cell3.setCellStyle(numberStyle);
                    } else {
                        R14cell3.setCellValue("");
                        R14cell3.setCellStyle(textStyle);
                    }
                    // R15 Col C
                    row = sheet.getRow(14);
                    Cell R15cell1 = row.createCell(2);
                    if (record.getR15_no1_of_branches() != null) {
                        R15cell1.setCellValue(record.getR15_no1_of_branches().doubleValue());
                        R15cell1.setCellStyle(numberStyle);
                    } else {
                        R15cell1.setCellValue("");
                        R15cell1.setCellStyle(textStyle);
                    }

                    // R15 Col D
                    Cell R15cell2 = row.createCell(3);
                    if (record.getR15_no1_of_sub_branches() != null) {
                        R15cell2.setCellValue(record.getR15_no1_of_sub_branches().doubleValue());
                        R15cell2.setCellStyle(numberStyle);
                    } else {
                        R15cell2.setCellValue("");
                        R15cell2.setCellStyle(textStyle);
                    }

                    // R15 Col E
                    Cell R15cell3 = row.createCell(4);
                    if (record.getR15_no1_of_agencies() != null) {
                        R15cell3.setCellValue(record.getR15_no1_of_agencies().doubleValue());
                        R15cell3.setCellStyle(numberStyle);
                    } else {
                        R15cell3.setCellValue("");
                        R15cell3.setCellStyle(textStyle);
                    }
                    // R16 Col C
                    row = sheet.getRow(15);
                    Cell R16cell1 = row.createCell(2);
                    if (record.getR16_no1_of_branches() != null) {
                        R16cell1.setCellValue(record.getR16_no1_of_branches().doubleValue());
                        R16cell1.setCellStyle(numberStyle);
                    } else {
                        R16cell1.setCellValue("");
                        R16cell1.setCellStyle(textStyle);
                    }

                    // R16 Col D
                    Cell R16cell2 = row.createCell(3);
                    if (record.getR16_no1_of_sub_branches() != null) {
                        R16cell2.setCellValue(record.getR16_no1_of_sub_branches().doubleValue());
                        R16cell2.setCellStyle(numberStyle);
                    } else {
                        R16cell2.setCellValue("");
                        R16cell2.setCellStyle(textStyle);
                    }

                    // R16 Col E
                    Cell R16cell3 = row.createCell(4);
                    if (record.getR16_no1_of_agencies() != null) {
                        R16cell3.setCellValue(record.getR16_no1_of_agencies().doubleValue());
                        R16cell3.setCellStyle(numberStyle);
                    } else {
                        R16cell3.setCellValue("");
                        R16cell3.setCellStyle(textStyle);
                    }
                    // R17 Col C
                    row = sheet.getRow(16);
                    Cell R17cell1 = row.createCell(2);
                    if (record.getR17_no1_of_branches() != null) {
                        R17cell1.setCellValue(record.getR17_no1_of_branches().doubleValue());
                        R17cell1.setCellStyle(numberStyle);
                    } else {
                        R17cell1.setCellValue("");
                        R17cell1.setCellStyle(textStyle);
                    }

                    // R17 Col D
                    Cell R17cell2 = row.createCell(3);
                    if (record.getR17_no1_of_sub_branches() != null) {
                        R17cell2.setCellValue(record.getR17_no1_of_sub_branches().doubleValue());
                        R17cell2.setCellStyle(numberStyle);
                    } else {
                        R17cell2.setCellValue("");
                        R17cell2.setCellStyle(textStyle);
                    }

                    // R17 Col E
                    Cell R17cell3 = row.createCell(4);
                    if (record.getR17_no1_of_agencies() != null) {
                        R17cell3.setCellValue(record.getR17_no1_of_agencies().doubleValue());
                        R17cell3.setCellStyle(numberStyle);
                    } else {
                        R17cell3.setCellValue("");
                        R17cell3.setCellStyle(textStyle);
                    }
                    // R18 Col C
                    row = sheet.getRow(17);
                    Cell R18cell1 = row.createCell(2);
                    if (record.getR18_no1_of_branches() != null) {
                        R18cell1.setCellValue(record.getR18_no1_of_branches().doubleValue());
                        R18cell1.setCellStyle(numberStyle);
                    } else {
                        R18cell1.setCellValue("");
                        R18cell1.setCellStyle(textStyle);
                    }

                    // R18 Col D
                    Cell R18cell2 = row.createCell(3);
                    if (record.getR18_no1_of_sub_branches() != null) {
                        R18cell2.setCellValue(record.getR18_no1_of_sub_branches().doubleValue());
                        R18cell2.setCellStyle(numberStyle);
                    } else {
                        R18cell2.setCellValue("");
                        R18cell2.setCellStyle(textStyle);
                    }

                    // R18 Col E
                    Cell R18cell3 = row.createCell(4);
                    if (record.getR18_no1_of_agencies() != null) {
                        R18cell3.setCellValue(record.getR18_no1_of_agencies().doubleValue());
                        R18cell3.setCellStyle(numberStyle);
                    } else {
                        R18cell3.setCellValue("");
                        R18cell3.setCellStyle(textStyle);
                    }
                    // R19 Col C
                    row = sheet.getRow(18);
                    Cell R19cell1 = row.createCell(2);
                    if (record.getR19_no1_of_branches() != null) {
                        R19cell1.setCellValue(record.getR19_no1_of_branches().doubleValue());
                        R19cell1.setCellStyle(numberStyle);
                    } else {
                        R19cell1.setCellValue("");
                        R19cell1.setCellStyle(textStyle);
                    }

                    // R19 Col D
                    Cell R19cell2 = row.createCell(3);
                    if (record.getR19_no1_of_sub_branches() != null) {
                        R19cell2.setCellValue(record.getR19_no1_of_sub_branches().doubleValue());
                        R19cell2.setCellStyle(numberStyle);
                    } else {
                        R19cell2.setCellValue("");
                        R19cell2.setCellStyle(textStyle);
                    }

                    // R19 Col E
                    Cell R19cell3 = row.createCell(4);
                    if (record.getR19_no1_of_agencies() != null) {
                        R19cell3.setCellValue(record.getR19_no1_of_agencies().doubleValue());
                        R19cell3.setCellStyle(numberStyle);
                    } else {
                        R19cell3.setCellValue("");
                        R19cell3.setCellStyle(textStyle);
                    }
                    // TABLE 2
                    // R25 Col C
                    row = sheet.getRow(24);
                    Cell R25cell1 = row.createCell(2);
                    if (record1.getR25_no_of_atms() != null) {
                        R25cell1.setCellValue(record1.getR25_no_of_atms().doubleValue());
                        R25cell1.setCellStyle(numberStyle);
                    } else {
                        R25cell1.setCellValue("");
                        R25cell1.setCellStyle(textStyle);
                    }

                    // R25 Col D
                    Cell R25cell2 = row.createCell(3);
                    if (record1.getR25_no_of_mini_atms() != null) {
                        R25cell2.setCellValue(record1.getR25_no_of_mini_atms().doubleValue());
                        R25cell2.setCellStyle(numberStyle);
                    } else {
                        R25cell2.setCellValue("");
                        R25cell2.setCellStyle(textStyle);
                    }

                    // R25 Col E
                    Cell R25cell3 = row.createCell(4);
                    if (record1.getR25_encashment_points() != null) {
                        R25cell3.setCellValue(record1.getR25_encashment_points().doubleValue());
                        R25cell3.setCellStyle(numberStyle);
                    } else {
                        R25cell3.setCellValue("");
                        R25cell3.setCellStyle(textStyle);
                    }
                    // R26 Col C
                    row = sheet.getRow(25);
                    Cell R26cell1 = row.createCell(2);
                    if (record1.getR26_no_of_atms() != null) {
                        R26cell1.setCellValue(record1.getR26_no_of_atms().doubleValue());
                        R26cell1.setCellStyle(numberStyle);
                    } else {
                        R26cell1.setCellValue("");
                        R26cell1.setCellStyle(textStyle);
                    }

                    // R26 Col D
                    Cell R26cell2 = row.createCell(3);
                    if (record1.getR26_no_of_mini_atms() != null) {
                        R26cell2.setCellValue(record1.getR26_no_of_mini_atms().doubleValue());
                        R26cell2.setCellStyle(numberStyle);
                    } else {
                        R26cell2.setCellValue("");
                        R26cell2.setCellStyle(textStyle);
                    }

                    // R26 Col E
                    Cell R26cell3 = row.createCell(4);
                    if (record1.getR26_encashment_points() != null) {
                        R26cell3.setCellValue(record1.getR26_encashment_points().doubleValue());
                        R26cell3.setCellStyle(numberStyle);
                    } else {
                        R26cell3.setCellValue("");
                        R26cell3.setCellStyle(textStyle);
                    }
                    // R27 Col C
                    row = sheet.getRow(26);
                    Cell R27cell1 = row.createCell(2);
                    if (record1.getR27_no_of_atms() != null) {
                        R27cell1.setCellValue(record1.getR27_no_of_atms().doubleValue());
                        R27cell1.setCellStyle(numberStyle);
                    } else {
                        R27cell1.setCellValue("");
                        R27cell1.setCellStyle(textStyle);
                    }

                    // R27 Col D
                    Cell R27cell2 = row.createCell(3);
                    if (record1.getR27_no_of_mini_atms() != null) {
                        R27cell2.setCellValue(record1.getR27_no_of_mini_atms().doubleValue());
                        R27cell2.setCellStyle(numberStyle);
                    } else {
                        R27cell2.setCellValue("");
                        R27cell2.setCellStyle(textStyle);
                    }

                    // R27 Col E
                    Cell R27cell3 = row.createCell(4);
                    if (record1.getR27_encashment_points() != null) {
                        R27cell3.setCellValue(record1.getR27_encashment_points().doubleValue());
                        R27cell3.setCellStyle(numberStyle);
                    } else {
                        R27cell3.setCellValue("");
                        R27cell3.setCellStyle(textStyle);
                    }
                    // R28 Col C
                    row = sheet.getRow(27);
                    Cell R28cell1 = row.createCell(2);
                    if (record1.getR28_no_of_atms() != null) {
                        R28cell1.setCellValue(record1.getR28_no_of_atms().doubleValue());
                        R28cell1.setCellStyle(numberStyle);
                    } else {
                        R28cell1.setCellValue("");
                        R28cell1.setCellStyle(textStyle);
                    }

                    // R28 Col D
                    Cell R28cell2 = row.createCell(3);
                    if (record1.getR28_no_of_mini_atms() != null) {
                        R28cell2.setCellValue(record1.getR28_no_of_mini_atms().doubleValue());
                        R28cell2.setCellStyle(numberStyle);
                    } else {
                        R28cell2.setCellValue("");
                        R28cell2.setCellStyle(textStyle);
                    }

                    // R28 Col E
                    Cell R28cell3 = row.createCell(4);
                    if (record1.getR28_encashment_points() != null) {
                        R28cell3.setCellValue(record1.getR28_encashment_points().doubleValue());
                        R28cell3.setCellStyle(numberStyle);
                    } else {
                        R28cell3.setCellValue("");
                        R28cell3.setCellStyle(textStyle);
                    }
                    // R29 Col C
                    row = sheet.getRow(28);
                    Cell R29cell1 = row.createCell(2);
                    if (record1.getR29_no_of_atms() != null) {
                        R29cell1.setCellValue(record1.getR29_no_of_atms().doubleValue());
                        R29cell1.setCellStyle(numberStyle);
                    } else {
                        R29cell1.setCellValue("");
                        R29cell1.setCellStyle(textStyle);
                    }

                    // R29 Col D
                    Cell R29cell2 = row.createCell(3);
                    if (record1.getR29_no_of_mini_atms() != null) {
                        R29cell2.setCellValue(record1.getR29_no_of_mini_atms().doubleValue());
                        R29cell2.setCellStyle(numberStyle);
                    } else {
                        R29cell2.setCellValue("");
                        R29cell2.setCellStyle(textStyle);
                    }

                    // R29 Col E
                    Cell R29cell3 = row.createCell(4);
                    if (record1.getR29_encashment_points() != null) {
                        R29cell3.setCellValue(record1.getR29_encashment_points().doubleValue());
                        R29cell3.setCellStyle(numberStyle);
                    } else {
                        R29cell3.setCellValue("");
                        R29cell3.setCellStyle(textStyle);
                    }
                    // R30 Col C
                    row = sheet.getRow(29);
                    Cell R30cell1 = row.createCell(2);
                    if (record1.getR30_no_of_atms() != null) {
                        R30cell1.setCellValue(record1.getR30_no_of_atms().doubleValue());
                        R30cell1.setCellStyle(numberStyle);
                    } else {
                        R30cell1.setCellValue("");
                        R30cell1.setCellStyle(textStyle);
                    }

                    // R30 Col D
                    Cell R30cell2 = row.createCell(3);
                    if (record1.getR30_no_of_mini_atms() != null) {
                        R30cell2.setCellValue(record1.getR30_no_of_mini_atms().doubleValue());
                        R30cell2.setCellStyle(numberStyle);
                    } else {
                        R30cell2.setCellValue("");
                        R30cell2.setCellStyle(textStyle);
                    }

                    // R30 Col E
                    Cell R30cell3 = row.createCell(4);
                    if (record1.getR30_encashment_points() != null) {
                        R30cell3.setCellValue(record1.getR30_encashment_points().doubleValue());
                        R30cell3.setCellStyle(numberStyle);
                    } else {
                        R30cell3.setCellValue("");
                        R30cell3.setCellStyle(textStyle);
                    }
                    // R31 Col C
                    row = sheet.getRow(30);
                    Cell R31cell1 = row.createCell(2);
                    if (record1.getR31_no_of_atms() != null) {
                        R31cell1.setCellValue(record1.getR31_no_of_atms().doubleValue());
                        R31cell1.setCellStyle(numberStyle);
                    } else {
                        R31cell1.setCellValue("");
                        R31cell1.setCellStyle(textStyle);
                    }

                    // R31 Col D
                    Cell R31cell2 = row.createCell(3);
                    if (record1.getR31_no_of_mini_atms() != null) {
                        R31cell2.setCellValue(record1.getR31_no_of_mini_atms().doubleValue());
                        R31cell2.setCellStyle(numberStyle);
                    } else {
                        R31cell2.setCellValue("");
                        R31cell2.setCellStyle(textStyle);
                    }

                    // R31 Col E
                    Cell R31cell3 = row.createCell(4);
                    if (record1.getR31_encashment_points() != null) {
                        R31cell3.setCellValue(record1.getR31_encashment_points().doubleValue());
                        R31cell3.setCellStyle(numberStyle);
                    } else {
                        R31cell3.setCellValue("");
                        R31cell3.setCellStyle(textStyle);
                    }
                    // R32 Col C
                    row = sheet.getRow(31);
                    Cell R32cell1 = row.createCell(2);
                    if (record1.getR32_no_of_atms() != null) {
                        R32cell1.setCellValue(record1.getR32_no_of_atms().doubleValue());
                        R32cell1.setCellStyle(numberStyle);
                    } else {
                        R32cell1.setCellValue("");
                        R32cell1.setCellStyle(textStyle);
                    }

                    // R32 Col D
                    Cell R32cell2 = row.createCell(3);
                    if (record1.getR32_no_of_mini_atms() != null) {
                        R32cell2.setCellValue(record1.getR32_no_of_mini_atms().doubleValue());
                        R32cell2.setCellStyle(numberStyle);
                    } else {
                        R32cell2.setCellValue("");
                        R32cell2.setCellStyle(textStyle);
                    }

                    // R32 Col E
                    Cell R32cell3 = row.createCell(4);
                    if (record1.getR32_encashment_points() != null) {
                        R32cell3.setCellValue(record1.getR32_encashment_points().doubleValue());
                        R32cell3.setCellStyle(numberStyle);
                    } else {
                        R32cell3.setCellValue("");
                        R32cell3.setCellStyle(textStyle);
                    }
                    // R33 Col C
                    row = sheet.getRow(32);
                    Cell R33cell1 = row.createCell(2);
                    if (record1.getR33_no_of_atms() != null) {
                        R33cell1.setCellValue(record1.getR33_no_of_atms().doubleValue());
                        R33cell1.setCellStyle(numberStyle);
                    } else {
                        R33cell1.setCellValue("");
                        R33cell1.setCellStyle(textStyle);
                    }

                    // R33 Col D
                    Cell R33cell2 = row.createCell(3);
                    if (record1.getR33_no_of_mini_atms() != null) {
                        R33cell2.setCellValue(record1.getR33_no_of_mini_atms().doubleValue());
                        R33cell2.setCellStyle(numberStyle);
                    } else {
                        R33cell2.setCellValue("");
                        R33cell2.setCellStyle(textStyle);
                    }

                    // R33 Col E
                    Cell R33cell3 = row.createCell(4);
                    if (record1.getR33_encashment_points() != null) {
                        R33cell3.setCellValue(record1.getR33_encashment_points().doubleValue());
                        R33cell3.setCellStyle(numberStyle);
                    } else {
                        R33cell3.setCellValue("");
                        R33cell3.setCellStyle(textStyle);
                    }
                    // R34 Col C
                    row = sheet.getRow(33);
                    Cell R34cell1 = row.createCell(2);
                    if (record1.getR34_no_of_atms() != null) {
                        R34cell1.setCellValue(record1.getR34_no_of_atms().doubleValue());
                        R34cell1.setCellStyle(numberStyle);
                    } else {
                        R34cell1.setCellValue("");
                        R34cell1.setCellStyle(textStyle);
                    }

                    // R34 Col D
                    Cell R34cell2 = row.createCell(3);
                    if (record1.getR34_no_of_mini_atms() != null) {
                        R34cell2.setCellValue(record1.getR34_no_of_mini_atms().doubleValue());
                        R34cell2.setCellStyle(numberStyle);
                    } else {
                        R34cell2.setCellValue("");
                        R34cell2.setCellStyle(textStyle);
                    }

                    // R34 Col E
                    Cell R34cell3 = row.createCell(4);
                    if (record1.getR34_encashment_points() != null) {
                        R34cell3.setCellValue(record1.getR34_encashment_points().doubleValue());
                        R34cell3.setCellStyle(numberStyle);
                    } else {
                        R34cell3.setCellValue("");
                        R34cell3.setCellStyle(textStyle);
                    }
                    // TABLE 3
                    // R40 Col C
                    row = sheet.getRow(39);
                    Cell R40cell1 = row.createCell(2);
                    if (record2.getR40_opening_no_of_cards() != null) {
                        R40cell1.setCellValue(record2.getR40_opening_no_of_cards().doubleValue());
                        R40cell1.setCellStyle(numberStyle);
                    } else {
                        R40cell1.setCellValue("");
                        R40cell1.setCellStyle(textStyle);
                    }

                    // R40 Col D
                    Cell R40cell2 = row.createCell(3);
                    if (record2.getR40_no_of_cards_issued() != null) {
                        R40cell2.setCellValue(record2.getR40_no_of_cards_issued().doubleValue());
                        R40cell2.setCellStyle(numberStyle);
                    } else {
                        R40cell2.setCellValue("");
                        R40cell2.setCellStyle(textStyle);
                    }

                    // R40 Col E
                    Cell R40cell3 = row.createCell(4);
                    if (record2.getR40_no_cards_of_closed() != null) {
                        R40cell3.setCellValue(record2.getR40_no_cards_of_closed().doubleValue());
                        R40cell3.setCellStyle(numberStyle);
                    } else {
                        R40cell3.setCellValue("");
                        R40cell3.setCellStyle(textStyle);
                    }

                    // R41 Col C
                    row = sheet.getRow(40);
                    Cell R41cell1 = row.createCell(2);
                    if (record2.getR41_opening_no_of_cards() != null) {
                        R41cell1.setCellValue(record2.getR41_opening_no_of_cards().doubleValue());
                        R41cell1.setCellStyle(numberStyle);
                    } else {
                        R41cell1.setCellValue("");
                        R41cell1.setCellStyle(textStyle);
                    }

                    // R41 Col D
                    Cell R41cell2 = row.createCell(3);
                    if (record2.getR41_no_of_cards_issued() != null) {
                        R41cell2.setCellValue(record2.getR41_no_of_cards_issued().doubleValue());
                        R41cell2.setCellStyle(numberStyle);
                    } else {
                        R41cell2.setCellValue("");
                        R41cell2.setCellStyle(textStyle);
                    }

                    // R41 Col E
                    Cell R41cell3 = row.createCell(4);
                    if (record2.getR41_no_cards_of_closed() != null) {
                        R41cell3.setCellValue(record2.getR41_no_cards_of_closed().doubleValue());
                        R41cell3.setCellStyle(numberStyle);
                    } else {
                        R41cell3.setCellValue("");
                        R41cell3.setCellStyle(textStyle);
                    }

                    // R42 Col C
                    row = sheet.getRow(41);
                    Cell R42cell1 = row.createCell(2);
                    if (record2.getR42_opening_no_of_cards() != null) {
                        R42cell1.setCellValue(record2.getR42_opening_no_of_cards().doubleValue());
                        R42cell1.setCellStyle(numberStyle);
                    } else {
                        R42cell1.setCellValue("");
                        R42cell1.setCellStyle(textStyle);
                    }

                    // R42 Col D
                    Cell R42cell2 = row.createCell(3);
                    if (record2.getR42_no_of_cards_issued() != null) {
                        R42cell2.setCellValue(record2.getR42_no_of_cards_issued().doubleValue());
                        R42cell2.setCellStyle(numberStyle);
                    } else {
                        R42cell2.setCellValue("");
                        R42cell2.setCellStyle(textStyle);
                    }

                    // R42 Col E
                    Cell R42cell3 = row.createCell(4);
                    if (record2.getR42_no_cards_of_closed() != null) {
                        R42cell3.setCellValue(record2.getR42_no_cards_of_closed().doubleValue());
                        R42cell3.setCellStyle(numberStyle);
                    } else {
                        R42cell3.setCellValue("");
                        R42cell3.setCellStyle(textStyle);
                    }

                    // R43 Col C
                    row = sheet.getRow(42);
                    Cell R43cell1 = row.createCell(2);
                    if (record2.getR43_opening_no_of_cards() != null) {
                        R43cell1.setCellValue(record2.getR43_opening_no_of_cards().doubleValue());
                        R43cell1.setCellStyle(numberStyle);
                    } else {
                        R43cell1.setCellValue("");
                        R43cell1.setCellStyle(textStyle);
                    }

                    // R43 Col D
                    Cell R43cell2 = row.createCell(3);
                    if (record2.getR43_no_of_cards_issued() != null) {
                        R43cell2.setCellValue(record2.getR43_no_of_cards_issued().doubleValue());
                        R43cell2.setCellStyle(numberStyle);
                    } else {
                        R43cell2.setCellValue("");
                        R43cell2.setCellStyle(textStyle);
                    }

                    // R43 Col E
                    Cell R43cell3 = row.createCell(4);
                    if (record2.getR43_no_cards_of_closed() != null) {
                        R43cell3.setCellValue(record2.getR43_no_cards_of_closed().doubleValue());
                        R43cell3.setCellStyle(numberStyle);
                    } else {
                        R43cell3.setCellValue("");
                        R43cell3.setCellStyle(textStyle);
                    }
                    // R44 Col C
                    row = sheet.getRow(43);
                    Cell R44cell1 = row.createCell(2);
                    if (record2.getR44_opening_no_of_cards() != null) {
                        R44cell1.setCellValue(record2.getR44_opening_no_of_cards().doubleValue());
                        R44cell1.setCellStyle(numberStyle);
                    } else {
                        R44cell1.setCellValue("");
                        R44cell1.setCellStyle(textStyle);
                    }

                    // R44 Col D
                    Cell R44cell2 = row.createCell(3);
                    if (record2.getR44_no_of_cards_issued() != null) {
                        R44cell2.setCellValue(record2.getR44_no_of_cards_issued().doubleValue());
                        R44cell2.setCellStyle(numberStyle);
                    } else {
                        R44cell2.setCellValue("");
                        R44cell2.setCellStyle(textStyle);
                    }

                    // R44 Col E
                    Cell R44cell3 = row.createCell(4);
                    if (record2.getR44_no_cards_of_closed() != null) {
                        R44cell3.setCellValue(record2.getR44_no_cards_of_closed().doubleValue());
                        R44cell3.setCellStyle(numberStyle);
                    } else {
                        R44cell3.setCellValue("");
                        R44cell3.setCellStyle(textStyle);
                    }

                    // R45 Col C
                    row = sheet.getRow(44);
                    Cell R45cell1 = row.createCell(2);
                    if (record2.getR45_opening_no_of_cards() != null) {
                        R45cell1.setCellValue(record2.getR45_opening_no_of_cards().doubleValue());
                        R45cell1.setCellStyle(numberStyle);
                    } else {
                        R45cell1.setCellValue("");
                        R45cell1.setCellStyle(textStyle);
                    }

                    // R45 Col D
                    Cell R45cell2 = row.createCell(3);
                    if (record2.getR45_no_of_cards_issued() != null) {
                        R45cell2.setCellValue(record2.getR45_no_of_cards_issued().doubleValue());
                        R45cell2.setCellStyle(numberStyle);
                    } else {
                        R45cell2.setCellValue("");
                        R45cell2.setCellStyle(textStyle);
                    }

                    // R45 Col E
                    Cell R45cell3 = row.createCell(4);
                    if (record2.getR45_no_cards_of_closed() != null) {
                        R45cell3.setCellValue(record2.getR45_no_cards_of_closed().doubleValue());
                        R45cell3.setCellStyle(numberStyle);
                    } else {
                        R45cell3.setCellValue("");
                        R45cell3.setCellStyle(textStyle);
                    }

                    // R46 Col C
                    row = sheet.getRow(45);
                    Cell R46cell1 = row.createCell(2);
                    if (record2.getR46_opening_no_of_cards() != null) {
                        R46cell1.setCellValue(record2.getR46_opening_no_of_cards().doubleValue());
                        R46cell1.setCellStyle(numberStyle);
                    } else {
                        R46cell1.setCellValue("");
                        R46cell1.setCellStyle(textStyle);
                    }

                    // R46 Col D
                    Cell R46cell2 = row.createCell(3);
                    if (record2.getR46_no_of_cards_issued() != null) {
                        R46cell2.setCellValue(record2.getR46_no_of_cards_issued().doubleValue());
                        R46cell2.setCellStyle(numberStyle);
                    } else {
                        R46cell2.setCellValue("");
                        R46cell2.setCellStyle(textStyle);
                    }

                    // R46 Col E
                    Cell R46cell3 = row.createCell(4);
                    if (record2.getR46_no_cards_of_closed() != null) {
                        R46cell3.setCellValue(record2.getR46_no_cards_of_closed().doubleValue());
                        R46cell3.setCellStyle(numberStyle);
                    } else {
                        R46cell3.setCellValue("");
                        R46cell3.setCellStyle(textStyle);
                    }

                    // R47 Col C
                    row = sheet.getRow(46);
                    Cell R47cell1 = row.createCell(2);
                    if (record2.getR47_opening_no_of_cards() != null) {
                        R47cell1.setCellValue(record2.getR47_opening_no_of_cards().doubleValue());
                        R47cell1.setCellStyle(numberStyle);
                    } else {
                        R47cell1.setCellValue("");
                        R47cell1.setCellStyle(textStyle);
                    }

                    // R47 Col D
                    Cell R47cell2 = row.createCell(3);
                    if (record2.getR47_no_of_cards_issued() != null) {
                        R47cell2.setCellValue(record2.getR47_no_of_cards_issued().doubleValue());
                        R47cell2.setCellStyle(numberStyle);
                    } else {
                        R47cell2.setCellValue("");
                        R47cell2.setCellStyle(textStyle);
                    }

                    // R47 Col E
                    Cell R47cell3 = row.createCell(4);
                    if (record2.getR47_no_cards_of_closed() != null) {
                        R47cell3.setCellValue(record2.getR47_no_cards_of_closed().doubleValue());
                        R47cell3.setCellStyle(numberStyle);
                    } else {
                        R47cell3.setCellValue("");
                        R47cell3.setCellStyle(textStyle);
                    }

                    // R48 Col C
                    row = sheet.getRow(47);
                    Cell R48cell1 = row.createCell(2);
                    if (record2.getR48_opening_no_of_cards() != null) {
                        R48cell1.setCellValue(record2.getR48_opening_no_of_cards().doubleValue());
                        R48cell1.setCellStyle(numberStyle);
                    } else {
                        R48cell1.setCellValue("");
                        R48cell1.setCellStyle(textStyle);
                    }

                    // R48 Col D
                    Cell R48cell2 = row.createCell(3);
                    if (record2.getR48_no_of_cards_issued() != null) {
                        R48cell2.setCellValue(record2.getR48_no_of_cards_issued().doubleValue());
                        R48cell2.setCellStyle(numberStyle);
                    } else {
                        R48cell2.setCellValue("");
                        R48cell2.setCellStyle(textStyle);
                    }

                    // R48 Col E
                    Cell R48cell3 = row.createCell(4);
                    if (record2.getR48_no_cards_of_closed() != null) {
                        R48cell3.setCellValue(record2.getR48_no_cards_of_closed().doubleValue());
                        R48cell3.setCellStyle(numberStyle);
                    } else {
                        R48cell3.setCellValue("");
                        R48cell3.setCellStyle(textStyle);
                    }

                    // R49 Col C
                    row = sheet.getRow(48);
                    Cell R49cell1 = row.createCell(2);
                    if (record2.getR49_opening_no_of_cards() != null) {
                        R49cell1.setCellValue(record2.getR49_opening_no_of_cards().doubleValue());
                        R49cell1.setCellStyle(numberStyle);
                    } else {
                        R49cell1.setCellValue("");
                        R49cell1.setCellStyle(textStyle);
                    }

                    // R49 Col D
                    Cell R49cell2 = row.createCell(3);
                    if (record2.getR49_no_of_cards_issued() != null) {
                        R49cell2.setCellValue(record2.getR49_no_of_cards_issued().doubleValue());
                        R49cell2.setCellStyle(numberStyle);
                    } else {
                        R49cell2.setCellValue("");
                        R49cell2.setCellStyle(textStyle);
                    }

                    // R49 Col E
                    Cell R49cell3 = row.createCell(4);
                    if (record2.getR49_no_cards_of_closed() != null) {
                        R49cell3.setCellValue(record2.getR49_no_cards_of_closed().doubleValue());
                        R49cell3.setCellStyle(numberStyle);
                    } else {
                        R49cell3.setCellValue("");
                        R49cell3.setCellStyle(textStyle);
                    }

                    // TABLE 4
                    // R55 Col C
                    row = sheet.getRow(54);
                    Cell R55cell1 = row.createCell(2);
                    if (record3.getR55_opening_no_of_cards() != null) {
                        R55cell1.setCellValue(record3.getR55_opening_no_of_cards().doubleValue());
                        R55cell1.setCellStyle(numberStyle);
                    } else {
                        R55cell1.setCellValue("");
                        R55cell1.setCellStyle(textStyle);
                    }

                    // R55 Col D
                    Cell R55cell2 = row.createCell(3);
                    if (record3.getR55_no_of_cards_issued() != null) {
                        R55cell2.setCellValue(record3.getR55_no_of_cards_issued().doubleValue());
                        R55cell2.setCellStyle(numberStyle);
                    } else {
                        R55cell2.setCellValue("");
                        R55cell2.setCellStyle(textStyle);
                    }

                    // R55 Col E
                    Cell R55cell3 = row.createCell(4);
                    if (record3.getR55_no_cards_of_closed() != null) {
                        R55cell3.setCellValue(record3.getR55_no_cards_of_closed().doubleValue());
                        R55cell3.setCellStyle(numberStyle);
                    } else {
                        R55cell3.setCellValue("");
                        R55cell3.setCellStyle(textStyle);
                    }

                    // R56 Col C
                    row = sheet.getRow(55);
                    Cell R56cell1 = row.createCell(2);
                    if (record3.getR56_opening_no_of_cards() != null) {
                        R56cell1.setCellValue(record3.getR56_opening_no_of_cards().doubleValue());
                        R56cell1.setCellStyle(numberStyle);
                    } else {
                        R56cell1.setCellValue("");
                        R56cell1.setCellStyle(textStyle);
                    }

                    // R56 Col D
                    Cell R56cell2 = row.createCell(3);
                    if (record3.getR56_no_of_cards_issued() != null) {
                        R56cell2.setCellValue(record3.getR56_no_of_cards_issued().doubleValue());
                        R56cell2.setCellStyle(numberStyle);
                    } else {
                        R56cell2.setCellValue("");
                        R56cell2.setCellStyle(textStyle);
                    }

                    // R56 Col E
                    Cell R56cell3 = row.createCell(4);
                    if (record3.getR56_no_cards_of_closed() != null) {
                        R56cell3.setCellValue(record3.getR56_no_cards_of_closed().doubleValue());
                        R56cell3.setCellStyle(numberStyle);
                    } else {
                        R56cell3.setCellValue("");
                        R56cell3.setCellStyle(textStyle);
                    }

                    // R57 Col C
                    row = sheet.getRow(56);
                    Cell R57cell1 = row.createCell(2);
                    if (record3.getR57_opening_no_of_cards() != null) {
                        R57cell1.setCellValue(record3.getR57_opening_no_of_cards().doubleValue());
                        R57cell1.setCellStyle(numberStyle);
                    } else {
                        R57cell1.setCellValue("");
                        R57cell1.setCellStyle(textStyle);
                    }

                    // R57 Col D
                    Cell R57cell2 = row.createCell(3);
                    if (record3.getR57_no_of_cards_issued() != null) {
                        R57cell2.setCellValue(record3.getR57_no_of_cards_issued().doubleValue());
                        R57cell2.setCellStyle(numberStyle);
                    } else {
                        R57cell2.setCellValue("");
                        R57cell2.setCellStyle(textStyle);
                    }

                    // R57 Col E
                    Cell R57cell3 = row.createCell(4);
                    if (record3.getR57_no_cards_of_closed() != null) {
                        R57cell3.setCellValue(record3.getR57_no_cards_of_closed().doubleValue());
                        R57cell3.setCellStyle(numberStyle);
                    } else {
                        R57cell3.setCellValue("");
                        R57cell3.setCellStyle(textStyle);
                    }

                    // R58 Col C
                    row = sheet.getRow(57);
                    Cell R58cell1 = row.createCell(2);
                    if (record3.getR58_opening_no_of_cards() != null) {
                        R58cell1.setCellValue(record3.getR58_opening_no_of_cards().doubleValue());
                        R58cell1.setCellStyle(numberStyle);
                    } else {
                        R58cell1.setCellValue("");
                        R58cell1.setCellStyle(textStyle);
                    }

                    // R58 Col D
                    Cell R58cell2 = row.createCell(3);
                    if (record3.getR58_no_of_cards_issued() != null) {
                        R58cell2.setCellValue(record3.getR58_no_of_cards_issued().doubleValue());
                        R58cell2.setCellStyle(numberStyle);
                    } else {
                        R58cell2.setCellValue("");
                        R58cell2.setCellStyle(textStyle);
                    }

                    // R58 Col E
                    Cell R58cell3 = row.createCell(4);
                    if (record3.getR58_no_cards_of_closed() != null) {
                        R58cell3.setCellValue(record3.getR58_no_cards_of_closed().doubleValue());
                        R58cell3.setCellStyle(numberStyle);
                    } else {
                        R58cell3.setCellValue("");
                        R58cell3.setCellStyle(textStyle);
                    }

                    // R59 Col C
                    row = sheet.getRow(58);
                    Cell R59cell1 = row.createCell(2);
                    if (record3.getR59_opening_no_of_cards() != null) {
                        R59cell1.setCellValue(record3.getR59_opening_no_of_cards().doubleValue());
                        R59cell1.setCellStyle(numberStyle);
                    } else {
                        R59cell1.setCellValue("");
                        R59cell1.setCellStyle(textStyle);
                    }

                    // R59 Col D
                    Cell R59cell2 = row.createCell(3);
                    if (record3.getR59_no_of_cards_issued() != null) {
                        R59cell2.setCellValue(record3.getR59_no_of_cards_issued().doubleValue());
                        R59cell2.setCellStyle(numberStyle);
                    } else {
                        R59cell2.setCellValue("");
                        R59cell2.setCellStyle(textStyle);
                    }

                    // R59 Col E
                    Cell R59cell3 = row.createCell(4);
                    if (record3.getR59_no_cards_of_closed() != null) {
                        R59cell3.setCellValue(record3.getR59_no_cards_of_closed().doubleValue());
                        R59cell3.setCellStyle(numberStyle);
                    } else {
                        R59cell3.setCellValue("");
                        R59cell3.setCellStyle(textStyle);
                    }

                    // R60 Col C
                    row = sheet.getRow(59);
                    Cell R60cell1 = row.createCell(2);
                    if (record3.getR60_opening_no_of_cards() != null) {
                        R60cell1.setCellValue(record3.getR60_opening_no_of_cards().doubleValue());
                        R60cell1.setCellStyle(numberStyle);
                    } else {
                        R60cell1.setCellValue("");
                        R60cell1.setCellStyle(textStyle);
                    }

                    // R60 Col D
                    Cell R60cell2 = row.createCell(3);
                    if (record3.getR60_no_of_cards_issued() != null) {
                        R60cell2.setCellValue(record3.getR60_no_of_cards_issued().doubleValue());
                        R60cell2.setCellStyle(numberStyle);
                    } else {
                        R60cell2.setCellValue("");
                        R60cell2.setCellStyle(textStyle);
                    }
                    // R60 Col E
                    Cell R60cell3 = row.createCell(4);
                    if (record3.getR60_no_cards_of_closed() != null) {
                        R60cell3.setCellValue(record3.getR60_no_cards_of_closed().doubleValue());
                        R60cell3.setCellStyle(numberStyle);
                    } else {
                        R60cell3.setCellValue("");
                        R60cell3.setCellStyle(textStyle);
                    }

                    // R61 Col C
                    row = sheet.getRow(60);
                    Cell R61cell1 = row.createCell(2);
                    if (record3.getR61_opening_no_of_cards() != null) {
                        R61cell1.setCellValue(record3.getR61_opening_no_of_cards().doubleValue());
                        R61cell1.setCellStyle(numberStyle);
                    } else {
                        R61cell1.setCellValue("");
                        R61cell1.setCellStyle(textStyle);
                    }

                    // R61 Col D
                    Cell R61cell2 = row.createCell(3);
                    if (record3.getR61_no_of_cards_issued() != null) {
                        R61cell2.setCellValue(record3.getR61_no_of_cards_issued().doubleValue());
                        R61cell2.setCellStyle(numberStyle);
                    } else {
                        R61cell2.setCellValue("");
                        R61cell2.setCellStyle(textStyle);
                    }
                    // R61 Col E
                    Cell R61cell3 = row.createCell(4);
                    if (record3.getR61_no_cards_of_closed() != null) {
                        R61cell3.setCellValue(record3.getR61_no_cards_of_closed().doubleValue());
                        R61cell3.setCellStyle(numberStyle);
                    } else {
                        R61cell3.setCellValue("");
                        R61cell3.setCellStyle(textStyle);
                    }

                    // R62 Col C
                    row = sheet.getRow(61);
                    Cell R62cell1 = row.createCell(2);
                    if (record3.getR62_opening_no_of_cards() != null) {
                        R62cell1.setCellValue(record3.getR62_opening_no_of_cards().doubleValue());
                        R62cell1.setCellStyle(numberStyle);
                    } else {
                        R62cell1.setCellValue("");
                        R62cell1.setCellStyle(textStyle);
                    }

                    // R62 Col D
                    Cell R62cell2 = row.createCell(3);
                    if (record3.getR62_no_of_cards_issued() != null) {
                        R62cell2.setCellValue(record3.getR62_no_of_cards_issued().doubleValue());
                        R62cell2.setCellStyle(numberStyle);
                    } else {
                        R62cell2.setCellValue("");
                        R62cell2.setCellStyle(textStyle);
                    }
                    // R62 Col E
                    Cell R62cell3 = row.createCell(4);
                    if (record3.getR62_no_cards_of_closed() != null) {
                        R62cell3.setCellValue(record3.getR62_no_cards_of_closed().doubleValue());
                        R62cell3.setCellStyle(numberStyle);
                    } else {
                        R62cell3.setCellValue("");
                        R62cell3.setCellStyle(textStyle);
                    }

                    // R63 Col C
                    row = sheet.getRow(62);
                    Cell R63cell1 = row.createCell(2);
                    if (record3.getR63_opening_no_of_cards() != null) {
                        R63cell1.setCellValue(record3.getR63_opening_no_of_cards().doubleValue());
                        R63cell1.setCellStyle(numberStyle);
                    } else {
                        R63cell1.setCellValue("");
                        R63cell1.setCellStyle(textStyle);
                    }

                    // R63 Col D
                    Cell R63cell2 = row.createCell(3);
                    if (record3.getR63_no_of_cards_issued() != null) {
                        R63cell2.setCellValue(record3.getR63_no_of_cards_issued().doubleValue());
                        R63cell2.setCellStyle(numberStyle);
                    } else {
                        R63cell2.setCellValue("");
                        R63cell2.setCellStyle(textStyle);
                    }
                    // R63 Col E
                    Cell R63cell3 = row.createCell(4);
                    if (record3.getR63_no_cards_of_closed() != null) {
                        R63cell3.setCellValue(record3.getR63_no_cards_of_closed().doubleValue());
                        R63cell3.setCellStyle(numberStyle);
                    } else {
                        R63cell3.setCellValue("");
                        R63cell3.setCellStyle(textStyle);
                    }

                    // R64 Col C
                    row = sheet.getRow(63);
                    Cell R64cell1 = row.createCell(2);
                    if (record3.getR64_opening_no_of_cards() != null) {
                        R64cell1.setCellValue(record3.getR64_opening_no_of_cards().doubleValue());
                        R64cell1.setCellStyle(numberStyle);
                    } else {
                        R64cell1.setCellValue("");
                        R64cell1.setCellStyle(textStyle);
                    }

                    // R64 Col D
                    Cell R64cell2 = row.createCell(3);
                    if (record3.getR64_no_of_cards_issued() != null) {
                        R64cell2.setCellValue(record3.getR64_no_of_cards_issued().doubleValue());
                        R64cell2.setCellStyle(numberStyle);
                    } else {
                        R64cell2.setCellValue("");
                        R64cell2.setCellStyle(textStyle);
                    }
                    // R64 Col E
                    Cell R64cell3 = row.createCell(4);
                    if (record3.getR64_no_cards_of_closed() != null) {
                        R64cell3.setCellValue(record3.getR64_no_cards_of_closed().doubleValue());
                        R64cell3.setCellStyle(numberStyle);
                    } else {
                        R64cell3.setCellValue("");
                        R64cell3.setCellStyle(textStyle);
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

    public byte[] getExcelQ_BRANCHNETARCHIVAL(String filename, String reportId, String fromdate, String todate,
            String currency, String dtltype, String type, BigDecimal version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {

        }
        List<Q_BRANCHNET_Archival_Summary_Entity1> dataList = Q_BRANCHNET_Archival_Summary_Repo1
                .getdatabydateListarchival(dateformat.parse(todate), version);
        List<Q_BRANCHNET_Archival_Summary_Entity2> dataList1 = Q_BRANCHNET_Archival_Summary_Repo2
                .getdatabydateListarchival(dateformat.parse(todate), version);
        List<Q_BRANCHNET_Archival_Summary_Entity3> dataList2 = Q_BRANCHNET_Archival_Summary_Repo3
                .getdatabydateListarchival(dateformat.parse(todate), version);
        List<Q_BRANCHNET_Archival_Summary_Entity4> dataList3 = Q_BRANCHNET_Archival_Summary_Repo4
                .getdatabydateListarchival(dateformat.parse(todate), version);

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for Q_BRANCHNET report. Returning empty result.");
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
            int startRow = 9;

            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {

                    Q_BRANCHNET_Archival_Summary_Entity1 record = dataList.get(i);
                    Q_BRANCHNET_Archival_Summary_Entity2 record1 = dataList1.get(i);
                    Q_BRANCHNET_Archival_Summary_Entity3 record2 = dataList2.get(i);
                    Q_BRANCHNET_Archival_Summary_Entity4 record3 = dataList3.get(i);
                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }
                    // R10 Col C

                    Cell R10cell1 = row.createCell(2);
                    if (record.getR10_no1_of_branches() != null) {
                        R10cell1.setCellValue(record.getR10_no1_of_branches().doubleValue());
                        R10cell1.setCellStyle(numberStyle);
                    } else {
                        R10cell1.setCellValue("");
                        R10cell1.setCellStyle(textStyle);
                    }

                    // R10 Col D
                    Cell R10cell2 = row.createCell(3);
                    if (record.getR10_no1_of_sub_branches() != null) {
                        R10cell2.setCellValue(record.getR10_no1_of_sub_branches().doubleValue());
                        R10cell2.setCellStyle(numberStyle);
                    } else {
                        R10cell2.setCellValue("");
                        R10cell2.setCellStyle(textStyle);
                    }

                    // R10 Col E
                    Cell R10cell3 = row.createCell(4);
                    if (record.getR10_no1_of_agencies() != null) {
                        R10cell3.setCellValue(record.getR10_no1_of_agencies().doubleValue());
                        R10cell3.setCellStyle(numberStyle);
                    } else {
                        R10cell3.setCellValue("");
                        R10cell3.setCellStyle(textStyle);
                    }
                    // R11 Col C
                    row = sheet.getRow(10);
                    Cell R11cell1 = row.createCell(2);
                    if (record.getR11_no1_of_branches() != null) {
                        R11cell1.setCellValue(record.getR11_no1_of_branches().doubleValue());
                        R11cell1.setCellStyle(numberStyle);
                    } else {
                        R11cell1.setCellValue("");
                        R11cell1.setCellStyle(textStyle);
                    }

                    // R11 Col D
                    Cell R11cell2 = row.createCell(3);
                    if (record.getR11_no1_of_sub_branches() != null) {
                        R11cell2.setCellValue(record.getR11_no1_of_sub_branches().doubleValue());
                        R11cell2.setCellStyle(numberStyle);
                    } else {
                        R11cell2.setCellValue("");
                        R11cell2.setCellStyle(textStyle);
                    }

                    // R11 Col E
                    Cell R11cell3 = row.createCell(4);
                    if (record.getR11_no1_of_agencies() != null) {
                        R11cell3.setCellValue(record.getR11_no1_of_agencies().doubleValue());
                        R11cell3.setCellStyle(numberStyle);
                    } else {
                        R11cell3.setCellValue("");
                        R11cell3.setCellStyle(textStyle);
                    }
                    // R12 Col C
                    row = sheet.getRow(11);
                    Cell R12cell1 = row.createCell(2);
                    if (record.getR12_no1_of_branches() != null) {
                        R12cell1.setCellValue(record.getR12_no1_of_branches().doubleValue());
                        R12cell1.setCellStyle(numberStyle);
                    } else {
                        R12cell1.setCellValue("");
                        R12cell1.setCellStyle(textStyle);
                    }

                    // R12 Col D
                    Cell R12cell2 = row.createCell(3);
                    if (record.getR12_no1_of_sub_branches() != null) {
                        R12cell2.setCellValue(record.getR12_no1_of_sub_branches().doubleValue());
                        R12cell2.setCellStyle(numberStyle);
                    } else {
                        R12cell2.setCellValue("");
                        R12cell2.setCellStyle(textStyle);
                    }

                    // R12 Col E
                    Cell R12cell3 = row.createCell(4);
                    if (record.getR12_no1_of_agencies() != null) {
                        R12cell3.setCellValue(record.getR12_no1_of_agencies().doubleValue());
                        R12cell3.setCellStyle(numberStyle);
                    } else {
                        R12cell3.setCellValue("");
                        R12cell3.setCellStyle(textStyle);
                    }
                    // R13 Col C
                    row = sheet.getRow(12);
                    Cell R13cell1 = row.createCell(2);
                    if (record.getR13_no1_of_branches() != null) {
                        R13cell1.setCellValue(record.getR13_no1_of_branches().doubleValue());
                        R13cell1.setCellStyle(numberStyle);
                    } else {
                        R13cell1.setCellValue("");
                        R13cell1.setCellStyle(textStyle);
                    }

                    // R13 Col D
                    Cell R13cell2 = row.createCell(3);
                    if (record.getR13_no1_of_sub_branches() != null) {
                        R13cell2.setCellValue(record.getR13_no1_of_sub_branches().doubleValue());
                        R13cell2.setCellStyle(numberStyle);
                    } else {
                        R13cell2.setCellValue("");
                        R13cell2.setCellStyle(textStyle);
                    }

                    // R13 Col E
                    Cell R13cell3 = row.createCell(4);
                    if (record.getR13_no1_of_agencies() != null) {
                        R13cell3.setCellValue(record.getR13_no1_of_agencies().doubleValue());
                        R13cell3.setCellStyle(numberStyle);
                    } else {
                        R13cell3.setCellValue("");
                        R13cell3.setCellStyle(textStyle);
                    }
                    // R14 Col C
                    row = sheet.getRow(13);
                    Cell R14cell1 = row.createCell(2);
                    if (record.getR14_no1_of_branches() != null) {
                        R14cell1.setCellValue(record.getR14_no1_of_branches().doubleValue());
                        R14cell1.setCellStyle(numberStyle);
                    } else {
                        R14cell1.setCellValue("");
                        R14cell1.setCellStyle(textStyle);
                    }

                    // R14 Col D
                    Cell R14cell2 = row.createCell(3);
                    if (record.getR14_no1_of_sub_branches() != null) {
                        R14cell2.setCellValue(record.getR14_no1_of_sub_branches().doubleValue());
                        R14cell2.setCellStyle(numberStyle);
                    } else {
                        R14cell2.setCellValue("");
                        R14cell2.setCellStyle(textStyle);
                    }

                    // R14 Col E
                    Cell R14cell3 = row.createCell(4);
                    if (record.getR14_no1_of_agencies() != null) {
                        R14cell3.setCellValue(record.getR14_no1_of_agencies().doubleValue());
                        R14cell3.setCellStyle(numberStyle);
                    } else {
                        R14cell3.setCellValue("");
                        R14cell3.setCellStyle(textStyle);
                    }
                    // R15 Col C
                    row = sheet.getRow(14);
                    Cell R15cell1 = row.createCell(2);
                    if (record.getR15_no1_of_branches() != null) {
                        R15cell1.setCellValue(record.getR15_no1_of_branches().doubleValue());
                        R15cell1.setCellStyle(numberStyle);
                    } else {
                        R15cell1.setCellValue("");
                        R15cell1.setCellStyle(textStyle);
                    }

                    // R15 Col D
                    Cell R15cell2 = row.createCell(3);
                    if (record.getR15_no1_of_sub_branches() != null) {
                        R15cell2.setCellValue(record.getR15_no1_of_sub_branches().doubleValue());
                        R15cell2.setCellStyle(numberStyle);
                    } else {
                        R15cell2.setCellValue("");
                        R15cell2.setCellStyle(textStyle);
                    }

                    // R15 Col E
                    Cell R15cell3 = row.createCell(4);
                    if (record.getR15_no1_of_agencies() != null) {
                        R15cell3.setCellValue(record.getR15_no1_of_agencies().doubleValue());
                        R15cell3.setCellStyle(numberStyle);
                    } else {
                        R15cell3.setCellValue("");
                        R15cell3.setCellStyle(textStyle);
                    }
                    // R16 Col C
                    row = sheet.getRow(15);
                    Cell R16cell1 = row.createCell(2);
                    if (record.getR16_no1_of_branches() != null) {
                        R16cell1.setCellValue(record.getR16_no1_of_branches().doubleValue());
                        R16cell1.setCellStyle(numberStyle);
                    } else {
                        R16cell1.setCellValue("");
                        R16cell1.setCellStyle(textStyle);
                    }

                    // R16 Col D
                    Cell R16cell2 = row.createCell(3);
                    if (record.getR16_no1_of_sub_branches() != null) {
                        R16cell2.setCellValue(record.getR16_no1_of_sub_branches().doubleValue());
                        R16cell2.setCellStyle(numberStyle);
                    } else {
                        R16cell2.setCellValue("");
                        R16cell2.setCellStyle(textStyle);
                    }

                    // R16 Col E
                    Cell R16cell3 = row.createCell(4);
                    if (record.getR16_no1_of_agencies() != null) {
                        R16cell3.setCellValue(record.getR16_no1_of_agencies().doubleValue());
                        R16cell3.setCellStyle(numberStyle);
                    } else {
                        R16cell3.setCellValue("");
                        R16cell3.setCellStyle(textStyle);
                    }
                    // R17 Col C
                    row = sheet.getRow(16);
                    Cell R17cell1 = row.createCell(2);
                    if (record.getR17_no1_of_branches() != null) {
                        R17cell1.setCellValue(record.getR17_no1_of_branches().doubleValue());
                        R17cell1.setCellStyle(numberStyle);
                    } else {
                        R17cell1.setCellValue("");
                        R17cell1.setCellStyle(textStyle);
                    }

                    // R17 Col D
                    Cell R17cell2 = row.createCell(3);
                    if (record.getR17_no1_of_sub_branches() != null) {
                        R17cell2.setCellValue(record.getR17_no1_of_sub_branches().doubleValue());
                        R17cell2.setCellStyle(numberStyle);
                    } else {
                        R17cell2.setCellValue("");
                        R17cell2.setCellStyle(textStyle);
                    }

                    // R17 Col E
                    Cell R17cell3 = row.createCell(4);
                    if (record.getR17_no1_of_agencies() != null) {
                        R17cell3.setCellValue(record.getR17_no1_of_agencies().doubleValue());
                        R17cell3.setCellStyle(numberStyle);
                    } else {
                        R17cell3.setCellValue("");
                        R17cell3.setCellStyle(textStyle);
                    }
                    // R18 Col C
                    row = sheet.getRow(17);
                    Cell R18cell1 = row.createCell(2);
                    if (record.getR18_no1_of_branches() != null) {
                        R18cell1.setCellValue(record.getR18_no1_of_branches().doubleValue());
                        R18cell1.setCellStyle(numberStyle);
                    } else {
                        R18cell1.setCellValue("");
                        R18cell1.setCellStyle(textStyle);
                    }

                    // R18 Col D
                    Cell R18cell2 = row.createCell(3);
                    if (record.getR18_no1_of_sub_branches() != null) {
                        R18cell2.setCellValue(record.getR18_no1_of_sub_branches().doubleValue());
                        R18cell2.setCellStyle(numberStyle);
                    } else {
                        R18cell2.setCellValue("");
                        R18cell2.setCellStyle(textStyle);
                    }

                    // R18 Col E
                    Cell R18cell3 = row.createCell(4);
                    if (record.getR18_no1_of_agencies() != null) {
                        R18cell3.setCellValue(record.getR18_no1_of_agencies().doubleValue());
                        R18cell3.setCellStyle(numberStyle);
                    } else {
                        R18cell3.setCellValue("");
                        R18cell3.setCellStyle(textStyle);
                    }
                    // R19 Col C
                    row = sheet.getRow(18);
                    Cell R19cell1 = row.createCell(2);
                    if (record.getR19_no1_of_branches() != null) {
                        R19cell1.setCellValue(record.getR19_no1_of_branches().doubleValue());
                        R19cell1.setCellStyle(numberStyle);
                    } else {
                        R19cell1.setCellValue("");
                        R19cell1.setCellStyle(textStyle);
                    }

                    // R19 Col D
                    Cell R19cell2 = row.createCell(3);
                    if (record.getR19_no1_of_sub_branches() != null) {
                        R19cell2.setCellValue(record.getR19_no1_of_sub_branches().doubleValue());
                        R19cell2.setCellStyle(numberStyle);
                    } else {
                        R19cell2.setCellValue("");
                        R19cell2.setCellStyle(textStyle);
                    }

                    // R19 Col E
                    Cell R19cell3 = row.createCell(4);
                    if (record.getR19_no1_of_agencies() != null) {
                        R19cell3.setCellValue(record.getR19_no1_of_agencies().doubleValue());
                        R19cell3.setCellStyle(numberStyle);
                    } else {
                        R19cell3.setCellValue("");
                        R19cell3.setCellStyle(textStyle);
                    }
                    // TABLE 2
                    // R25 Col C
                    row = sheet.getRow(24);
                    Cell R25cell1 = row.createCell(2);
                    if (record1.getR25_no_of_atms() != null) {
                        R25cell1.setCellValue(record1.getR25_no_of_atms().doubleValue());
                        R25cell1.setCellStyle(numberStyle);
                    } else {
                        R25cell1.setCellValue("");
                        R25cell1.setCellStyle(textStyle);
                    }

                    // R25 Col D
                    Cell R25cell2 = row.createCell(3);
                    if (record1.getR25_no_of_mini_atms() != null) {
                        R25cell2.setCellValue(record1.getR25_no_of_mini_atms().doubleValue());
                        R25cell2.setCellStyle(numberStyle);
                    } else {
                        R25cell2.setCellValue("");
                        R25cell2.setCellStyle(textStyle);
                    }

                    // R25 Col E
                    Cell R25cell3 = row.createCell(4);
                    if (record1.getR25_encashment_points() != null) {
                        R25cell3.setCellValue(record1.getR25_encashment_points().doubleValue());
                        R25cell3.setCellStyle(numberStyle);
                    } else {
                        R25cell3.setCellValue("");
                        R25cell3.setCellStyle(textStyle);
                    }
                    // R26 Col C
                    row = sheet.getRow(25);
                    Cell R26cell1 = row.createCell(2);
                    if (record1.getR26_no_of_atms() != null) {
                        R26cell1.setCellValue(record1.getR26_no_of_atms().doubleValue());
                        R26cell1.setCellStyle(numberStyle);
                    } else {
                        R26cell1.setCellValue("");
                        R26cell1.setCellStyle(textStyle);
                    }

                    // R26 Col D
                    Cell R26cell2 = row.createCell(3);
                    if (record1.getR26_no_of_mini_atms() != null) {
                        R26cell2.setCellValue(record1.getR26_no_of_mini_atms().doubleValue());
                        R26cell2.setCellStyle(numberStyle);
                    } else {
                        R26cell2.setCellValue("");
                        R26cell2.setCellStyle(textStyle);
                    }

                    // R26 Col E
                    Cell R26cell3 = row.createCell(4);
                    if (record1.getR26_encashment_points() != null) {
                        R26cell3.setCellValue(record1.getR26_encashment_points().doubleValue());
                        R26cell3.setCellStyle(numberStyle);
                    } else {
                        R26cell3.setCellValue("");
                        R26cell3.setCellStyle(textStyle);
                    }
                    // R27 Col C
                    row = sheet.getRow(26);
                    Cell R27cell1 = row.createCell(2);
                    if (record1.getR27_no_of_atms() != null) {
                        R27cell1.setCellValue(record1.getR27_no_of_atms().doubleValue());
                        R27cell1.setCellStyle(numberStyle);
                    } else {
                        R27cell1.setCellValue("");
                        R27cell1.setCellStyle(textStyle);
                    }

                    // R27 Col D
                    Cell R27cell2 = row.createCell(3);
                    if (record1.getR27_no_of_mini_atms() != null) {
                        R27cell2.setCellValue(record1.getR27_no_of_mini_atms().doubleValue());
                        R27cell2.setCellStyle(numberStyle);
                    } else {
                        R27cell2.setCellValue("");
                        R27cell2.setCellStyle(textStyle);
                    }

                    // R27 Col E
                    Cell R27cell3 = row.createCell(4);
                    if (record1.getR27_encashment_points() != null) {
                        R27cell3.setCellValue(record1.getR27_encashment_points().doubleValue());
                        R27cell3.setCellStyle(numberStyle);
                    } else {
                        R27cell3.setCellValue("");
                        R27cell3.setCellStyle(textStyle);
                    }
                    // R28 Col C
                    row = sheet.getRow(27);
                    Cell R28cell1 = row.createCell(2);
                    if (record1.getR28_no_of_atms() != null) {
                        R28cell1.setCellValue(record1.getR28_no_of_atms().doubleValue());
                        R28cell1.setCellStyle(numberStyle);
                    } else {
                        R28cell1.setCellValue("");
                        R28cell1.setCellStyle(textStyle);
                    }

                    // R28 Col D
                    Cell R28cell2 = row.createCell(3);
                    if (record1.getR28_no_of_mini_atms() != null) {
                        R28cell2.setCellValue(record1.getR28_no_of_mini_atms().doubleValue());
                        R28cell2.setCellStyle(numberStyle);
                    } else {
                        R28cell2.setCellValue("");
                        R28cell2.setCellStyle(textStyle);
                    }

                    // R28 Col E
                    Cell R28cell3 = row.createCell(4);
                    if (record1.getR28_encashment_points() != null) {
                        R28cell3.setCellValue(record1.getR28_encashment_points().doubleValue());
                        R28cell3.setCellStyle(numberStyle);
                    } else {
                        R28cell3.setCellValue("");
                        R28cell3.setCellStyle(textStyle);
                    }
                    // R29 Col C
                    row = sheet.getRow(28);
                    Cell R29cell1 = row.createCell(2);
                    if (record1.getR29_no_of_atms() != null) {
                        R29cell1.setCellValue(record1.getR29_no_of_atms().doubleValue());
                        R29cell1.setCellStyle(numberStyle);
                    } else {
                        R29cell1.setCellValue("");
                        R29cell1.setCellStyle(textStyle);
                    }

                    // R29 Col D
                    Cell R29cell2 = row.createCell(3);
                    if (record1.getR29_no_of_mini_atms() != null) {
                        R29cell2.setCellValue(record1.getR29_no_of_mini_atms().doubleValue());
                        R29cell2.setCellStyle(numberStyle);
                    } else {
                        R29cell2.setCellValue("");
                        R29cell2.setCellStyle(textStyle);
                    }

                    // R29 Col E
                    Cell R29cell3 = row.createCell(4);
                    if (record1.getR29_encashment_points() != null) {
                        R29cell3.setCellValue(record1.getR29_encashment_points().doubleValue());
                        R29cell3.setCellStyle(numberStyle);
                    } else {
                        R29cell3.setCellValue("");
                        R29cell3.setCellStyle(textStyle);
                    }
                    // R30 Col C
                    row = sheet.getRow(29);
                    Cell R30cell1 = row.createCell(2);
                    if (record1.getR30_no_of_atms() != null) {
                        R30cell1.setCellValue(record1.getR30_no_of_atms().doubleValue());
                        R30cell1.setCellStyle(numberStyle);
                    } else {
                        R30cell1.setCellValue("");
                        R30cell1.setCellStyle(textStyle);
                    }

                    // R30 Col D
                    Cell R30cell2 = row.createCell(3);
                    if (record1.getR30_no_of_mini_atms() != null) {
                        R30cell2.setCellValue(record1.getR30_no_of_mini_atms().doubleValue());
                        R30cell2.setCellStyle(numberStyle);
                    } else {
                        R30cell2.setCellValue("");
                        R30cell2.setCellStyle(textStyle);
                    }

                    // R30 Col E
                    Cell R30cell3 = row.createCell(4);
                    if (record1.getR30_encashment_points() != null) {
                        R30cell3.setCellValue(record1.getR30_encashment_points().doubleValue());
                        R30cell3.setCellStyle(numberStyle);
                    } else {
                        R30cell3.setCellValue("");
                        R30cell3.setCellStyle(textStyle);
                    }
                    // R31 Col C
                    row = sheet.getRow(30);
                    Cell R31cell1 = row.createCell(2);
                    if (record1.getR31_no_of_atms() != null) {
                        R31cell1.setCellValue(record1.getR31_no_of_atms().doubleValue());
                        R31cell1.setCellStyle(numberStyle);
                    } else {
                        R31cell1.setCellValue("");
                        R31cell1.setCellStyle(textStyle);
                    }

                    // R31 Col D
                    Cell R31cell2 = row.createCell(3);
                    if (record1.getR31_no_of_mini_atms() != null) {
                        R31cell2.setCellValue(record1.getR31_no_of_mini_atms().doubleValue());
                        R31cell2.setCellStyle(numberStyle);
                    } else {
                        R31cell2.setCellValue("");
                        R31cell2.setCellStyle(textStyle);
                    }

                    // R31 Col E
                    Cell R31cell3 = row.createCell(4);
                    if (record1.getR31_encashment_points() != null) {
                        R31cell3.setCellValue(record1.getR31_encashment_points().doubleValue());
                        R31cell3.setCellStyle(numberStyle);
                    } else {
                        R31cell3.setCellValue("");
                        R31cell3.setCellStyle(textStyle);
                    }
                    // R32 Col C
                    row = sheet.getRow(31);
                    Cell R32cell1 = row.createCell(2);
                    if (record1.getR32_no_of_atms() != null) {
                        R32cell1.setCellValue(record1.getR32_no_of_atms().doubleValue());
                        R32cell1.setCellStyle(numberStyle);
                    } else {
                        R32cell1.setCellValue("");
                        R32cell1.setCellStyle(textStyle);
                    }

                    // R32 Col D
                    Cell R32cell2 = row.createCell(3);
                    if (record1.getR32_no_of_mini_atms() != null) {
                        R32cell2.setCellValue(record1.getR32_no_of_mini_atms().doubleValue());
                        R32cell2.setCellStyle(numberStyle);
                    } else {
                        R32cell2.setCellValue("");
                        R32cell2.setCellStyle(textStyle);
                    }

                    // R32 Col E
                    Cell R32cell3 = row.createCell(4);
                    if (record1.getR32_encashment_points() != null) {
                        R32cell3.setCellValue(record1.getR32_encashment_points().doubleValue());
                        R32cell3.setCellStyle(numberStyle);
                    } else {
                        R32cell3.setCellValue("");
                        R32cell3.setCellStyle(textStyle);
                    }
                    // R33 Col C
                    row = sheet.getRow(32);
                    Cell R33cell1 = row.createCell(2);
                    if (record1.getR33_no_of_atms() != null) {
                        R33cell1.setCellValue(record1.getR33_no_of_atms().doubleValue());
                        R33cell1.setCellStyle(numberStyle);
                    } else {
                        R33cell1.setCellValue("");
                        R33cell1.setCellStyle(textStyle);
                    }

                    // R33 Col D
                    Cell R33cell2 = row.createCell(3);
                    if (record1.getR33_no_of_mini_atms() != null) {
                        R33cell2.setCellValue(record1.getR33_no_of_mini_atms().doubleValue());
                        R33cell2.setCellStyle(numberStyle);
                    } else {
                        R33cell2.setCellValue("");
                        R33cell2.setCellStyle(textStyle);
                    }

                    // R33 Col E
                    Cell R33cell3 = row.createCell(4);
                    if (record1.getR33_encashment_points() != null) {
                        R33cell3.setCellValue(record1.getR33_encashment_points().doubleValue());
                        R33cell3.setCellStyle(numberStyle);
                    } else {
                        R33cell3.setCellValue("");
                        R33cell3.setCellStyle(textStyle);
                    }
                    // R34 Col C
                    row = sheet.getRow(33);
                    Cell R34cell1 = row.createCell(2);
                    if (record1.getR34_no_of_atms() != null) {
                        R34cell1.setCellValue(record1.getR34_no_of_atms().doubleValue());
                        R34cell1.setCellStyle(numberStyle);
                    } else {
                        R34cell1.setCellValue("");
                        R34cell1.setCellStyle(textStyle);
                    }

                    // R34 Col D
                    Cell R34cell2 = row.createCell(3);
                    if (record1.getR34_no_of_mini_atms() != null) {
                        R34cell2.setCellValue(record1.getR34_no_of_mini_atms().doubleValue());
                        R34cell2.setCellStyle(numberStyle);
                    } else {
                        R34cell2.setCellValue("");
                        R34cell2.setCellStyle(textStyle);
                    }

                    // R34 Col E
                    Cell R34cell3 = row.createCell(4);
                    if (record1.getR34_encashment_points() != null) {
                        R34cell3.setCellValue(record1.getR34_encashment_points().doubleValue());
                        R34cell3.setCellStyle(numberStyle);
                    } else {
                        R34cell3.setCellValue("");
                        R34cell3.setCellStyle(textStyle);
                    }
                    // TABLE 3
                    // R40 Col C
                    row = sheet.getRow(39);
                    Cell R40cell1 = row.createCell(2);
                    if (record2.getR40_opening_no_of_cards() != null) {
                        R40cell1.setCellValue(record2.getR40_opening_no_of_cards().doubleValue());
                        R40cell1.setCellStyle(numberStyle);
                    } else {
                        R40cell1.setCellValue("");
                        R40cell1.setCellStyle(textStyle);
                    }

                    // R40 Col D
                    Cell R40cell2 = row.createCell(3);
                    if (record2.getR40_no_of_cards_issued() != null) {
                        R40cell2.setCellValue(record2.getR40_no_of_cards_issued().doubleValue());
                        R40cell2.setCellStyle(numberStyle);
                    } else {
                        R40cell2.setCellValue("");
                        R40cell2.setCellStyle(textStyle);
                    }

                    // R40 Col E
                    Cell R40cell3 = row.createCell(4);
                    if (record2.getR40_no_cards_of_closed() != null) {
                        R40cell3.setCellValue(record2.getR40_no_cards_of_closed().doubleValue());
                        R40cell3.setCellStyle(numberStyle);
                    } else {
                        R40cell3.setCellValue("");
                        R40cell3.setCellStyle(textStyle);
                    }

                    // R41 Col C
                    row = sheet.getRow(40);
                    Cell R41cell1 = row.createCell(2);
                    if (record2.getR41_opening_no_of_cards() != null) {
                        R41cell1.setCellValue(record2.getR41_opening_no_of_cards().doubleValue());
                        R41cell1.setCellStyle(numberStyle);
                    } else {
                        R41cell1.setCellValue("");
                        R41cell1.setCellStyle(textStyle);
                    }

                    // R41 Col D
                    Cell R41cell2 = row.createCell(3);
                    if (record2.getR41_no_of_cards_issued() != null) {
                        R41cell2.setCellValue(record2.getR41_no_of_cards_issued().doubleValue());
                        R41cell2.setCellStyle(numberStyle);
                    } else {
                        R41cell2.setCellValue("");
                        R41cell2.setCellStyle(textStyle);
                    }

                    // R41 Col E
                    Cell R41cell3 = row.createCell(4);
                    if (record2.getR41_no_cards_of_closed() != null) {
                        R41cell3.setCellValue(record2.getR41_no_cards_of_closed().doubleValue());
                        R41cell3.setCellStyle(numberStyle);
                    } else {
                        R41cell3.setCellValue("");
                        R41cell3.setCellStyle(textStyle);
                    }

                    // R42 Col C
                    row = sheet.getRow(41);
                    Cell R42cell1 = row.createCell(2);
                    if (record2.getR42_opening_no_of_cards() != null) {
                        R42cell1.setCellValue(record2.getR42_opening_no_of_cards().doubleValue());
                        R42cell1.setCellStyle(numberStyle);
                    } else {
                        R42cell1.setCellValue("");
                        R42cell1.setCellStyle(textStyle);
                    }

                    // R42 Col D
                    Cell R42cell2 = row.createCell(3);
                    if (record2.getR42_no_of_cards_issued() != null) {
                        R42cell2.setCellValue(record2.getR42_no_of_cards_issued().doubleValue());
                        R42cell2.setCellStyle(numberStyle);
                    } else {
                        R42cell2.setCellValue("");
                        R42cell2.setCellStyle(textStyle);
                    }

                    // R42 Col E
                    Cell R42cell3 = row.createCell(4);
                    if (record2.getR42_no_cards_of_closed() != null) {
                        R42cell3.setCellValue(record2.getR42_no_cards_of_closed().doubleValue());
                        R42cell3.setCellStyle(numberStyle);
                    } else {
                        R42cell3.setCellValue("");
                        R42cell3.setCellStyle(textStyle);
                    }

                    // R43 Col C
                    row = sheet.getRow(42);
                    Cell R43cell1 = row.createCell(2);
                    if (record2.getR43_opening_no_of_cards() != null) {
                        R43cell1.setCellValue(record2.getR43_opening_no_of_cards().doubleValue());
                        R43cell1.setCellStyle(numberStyle);
                    } else {
                        R43cell1.setCellValue("");
                        R43cell1.setCellStyle(textStyle);
                    }

                    // R43 Col D
                    Cell R43cell2 = row.createCell(3);
                    if (record2.getR43_no_of_cards_issued() != null) {
                        R43cell2.setCellValue(record2.getR43_no_of_cards_issued().doubleValue());
                        R43cell2.setCellStyle(numberStyle);
                    } else {
                        R43cell2.setCellValue("");
                        R43cell2.setCellStyle(textStyle);
                    }

                    // R43 Col E
                    Cell R43cell3 = row.createCell(4);
                    if (record2.getR43_no_cards_of_closed() != null) {
                        R43cell3.setCellValue(record2.getR43_no_cards_of_closed().doubleValue());
                        R43cell3.setCellStyle(numberStyle);
                    } else {
                        R43cell3.setCellValue("");
                        R43cell3.setCellStyle(textStyle);
                    }
                    // R44 Col C
                    row = sheet.getRow(43);
                    Cell R44cell1 = row.createCell(2);
                    if (record2.getR44_opening_no_of_cards() != null) {
                        R44cell1.setCellValue(record2.getR44_opening_no_of_cards().doubleValue());
                        R44cell1.setCellStyle(numberStyle);
                    } else {
                        R44cell1.setCellValue("");
                        R44cell1.setCellStyle(textStyle);
                    }

                    // R44 Col D
                    Cell R44cell2 = row.createCell(3);
                    if (record2.getR44_no_of_cards_issued() != null) {
                        R44cell2.setCellValue(record2.getR44_no_of_cards_issued().doubleValue());
                        R44cell2.setCellStyle(numberStyle);
                    } else {
                        R44cell2.setCellValue("");
                        R44cell2.setCellStyle(textStyle);
                    }

                    // R44 Col E
                    Cell R44cell3 = row.createCell(4);
                    if (record2.getR44_no_cards_of_closed() != null) {
                        R44cell3.setCellValue(record2.getR44_no_cards_of_closed().doubleValue());
                        R44cell3.setCellStyle(numberStyle);
                    } else {
                        R44cell3.setCellValue("");
                        R44cell3.setCellStyle(textStyle);
                    }

                    // R45 Col C
                    row = sheet.getRow(44);
                    Cell R45cell1 = row.createCell(2);
                    if (record2.getR45_opening_no_of_cards() != null) {
                        R45cell1.setCellValue(record2.getR45_opening_no_of_cards().doubleValue());
                        R45cell1.setCellStyle(numberStyle);
                    } else {
                        R45cell1.setCellValue("");
                        R45cell1.setCellStyle(textStyle);
                    }

                    // R45 Col D
                    Cell R45cell2 = row.createCell(3);
                    if (record2.getR45_no_of_cards_issued() != null) {
                        R45cell2.setCellValue(record2.getR45_no_of_cards_issued().doubleValue());
                        R45cell2.setCellStyle(numberStyle);
                    } else {
                        R45cell2.setCellValue("");
                        R45cell2.setCellStyle(textStyle);
                    }

                    // R45 Col E
                    Cell R45cell3 = row.createCell(4);
                    if (record2.getR45_no_cards_of_closed() != null) {
                        R45cell3.setCellValue(record2.getR45_no_cards_of_closed().doubleValue());
                        R45cell3.setCellStyle(numberStyle);
                    } else {
                        R45cell3.setCellValue("");
                        R45cell3.setCellStyle(textStyle);
                    }

                    // R46 Col C
                    row = sheet.getRow(45);
                    Cell R46cell1 = row.createCell(2);
                    if (record2.getR46_opening_no_of_cards() != null) {
                        R46cell1.setCellValue(record2.getR46_opening_no_of_cards().doubleValue());
                        R46cell1.setCellStyle(numberStyle);
                    } else {
                        R46cell1.setCellValue("");
                        R46cell1.setCellStyle(textStyle);
                    }

                    // R46 Col D
                    Cell R46cell2 = row.createCell(3);
                    if (record2.getR46_no_of_cards_issued() != null) {
                        R46cell2.setCellValue(record2.getR46_no_of_cards_issued().doubleValue());
                        R46cell2.setCellStyle(numberStyle);
                    } else {
                        R46cell2.setCellValue("");
                        R46cell2.setCellStyle(textStyle);
                    }

                    // R46 Col E
                    Cell R46cell3 = row.createCell(4);
                    if (record2.getR46_no_cards_of_closed() != null) {
                        R46cell3.setCellValue(record2.getR46_no_cards_of_closed().doubleValue());
                        R46cell3.setCellStyle(numberStyle);
                    } else {
                        R46cell3.setCellValue("");
                        R46cell3.setCellStyle(textStyle);
                    }

                    // R47 Col C
                    row = sheet.getRow(46);
                    Cell R47cell1 = row.createCell(2);
                    if (record2.getR47_opening_no_of_cards() != null) {
                        R47cell1.setCellValue(record2.getR47_opening_no_of_cards().doubleValue());
                        R47cell1.setCellStyle(numberStyle);
                    } else {
                        R47cell1.setCellValue("");
                        R47cell1.setCellStyle(textStyle);
                    }

                    // R47 Col D
                    Cell R47cell2 = row.createCell(3);
                    if (record2.getR47_no_of_cards_issued() != null) {
                        R47cell2.setCellValue(record2.getR47_no_of_cards_issued().doubleValue());
                        R47cell2.setCellStyle(numberStyle);
                    } else {
                        R47cell2.setCellValue("");
                        R47cell2.setCellStyle(textStyle);
                    }

                    // R47 Col E
                    Cell R47cell3 = row.createCell(4);
                    if (record2.getR47_no_cards_of_closed() != null) {
                        R47cell3.setCellValue(record2.getR47_no_cards_of_closed().doubleValue());
                        R47cell3.setCellStyle(numberStyle);
                    } else {
                        R47cell3.setCellValue("");
                        R47cell3.setCellStyle(textStyle);
                    }

                    // R48 Col C
                    row = sheet.getRow(47);
                    Cell R48cell1 = row.createCell(2);
                    if (record2.getR48_opening_no_of_cards() != null) {
                        R48cell1.setCellValue(record2.getR48_opening_no_of_cards().doubleValue());
                        R48cell1.setCellStyle(numberStyle);
                    } else {
                        R48cell1.setCellValue("");
                        R48cell1.setCellStyle(textStyle);
                    }

                    // R48 Col D
                    Cell R48cell2 = row.createCell(3);
                    if (record2.getR48_no_of_cards_issued() != null) {
                        R48cell2.setCellValue(record2.getR48_no_of_cards_issued().doubleValue());
                        R48cell2.setCellStyle(numberStyle);
                    } else {
                        R48cell2.setCellValue("");
                        R48cell2.setCellStyle(textStyle);
                    }

                    // R48 Col E
                    Cell R48cell3 = row.createCell(4);
                    if (record2.getR48_no_cards_of_closed() != null) {
                        R48cell3.setCellValue(record2.getR48_no_cards_of_closed().doubleValue());
                        R48cell3.setCellStyle(numberStyle);
                    } else {
                        R48cell3.setCellValue("");
                        R48cell3.setCellStyle(textStyle);
                    }

                    // R49 Col C
                    row = sheet.getRow(48);
                    Cell R49cell1 = row.createCell(2);
                    if (record2.getR49_opening_no_of_cards() != null) {
                        R49cell1.setCellValue(record2.getR49_opening_no_of_cards().doubleValue());
                        R49cell1.setCellStyle(numberStyle);
                    } else {
                        R49cell1.setCellValue("");
                        R49cell1.setCellStyle(textStyle);
                    }

                    // R49 Col D
                    Cell R49cell2 = row.createCell(3);
                    if (record2.getR49_no_of_cards_issued() != null) {
                        R49cell2.setCellValue(record2.getR49_no_of_cards_issued().doubleValue());
                        R49cell2.setCellStyle(numberStyle);
                    } else {
                        R49cell2.setCellValue("");
                        R49cell2.setCellStyle(textStyle);
                    }

                    // R49 Col E
                    Cell R49cell3 = row.createCell(4);
                    if (record2.getR49_no_cards_of_closed() != null) {
                        R49cell3.setCellValue(record2.getR49_no_cards_of_closed().doubleValue());
                        R49cell3.setCellStyle(numberStyle);
                    } else {
                        R49cell3.setCellValue("");
                        R49cell3.setCellStyle(textStyle);
                    }

                    // TABLE 4
                    // R55 Col C
                    row = sheet.getRow(54);
                    Cell R55cell1 = row.createCell(2);
                    if (record3.getR55_opening_no_of_cards() != null) {
                        R55cell1.setCellValue(record3.getR55_opening_no_of_cards().doubleValue());
                        R55cell1.setCellStyle(numberStyle);
                    } else {
                        R55cell1.setCellValue("");
                        R55cell1.setCellStyle(textStyle);
                    }

                    // R55 Col D
                    Cell R55cell2 = row.createCell(3);
                    if (record3.getR55_no_of_cards_issued() != null) {
                        R55cell2.setCellValue(record3.getR55_no_of_cards_issued().doubleValue());
                        R55cell2.setCellStyle(numberStyle);
                    } else {
                        R55cell2.setCellValue("");
                        R55cell2.setCellStyle(textStyle);
                    }

                    // R55 Col E
                    Cell R55cell3 = row.createCell(4);
                    if (record3.getR55_no_cards_of_closed() != null) {
                        R55cell3.setCellValue(record3.getR55_no_cards_of_closed().doubleValue());
                        R55cell3.setCellStyle(numberStyle);
                    } else {
                        R55cell3.setCellValue("");
                        R55cell3.setCellStyle(textStyle);
                    }

                    // R56 Col C
                    row = sheet.getRow(55);
                    Cell R56cell1 = row.createCell(2);
                    if (record3.getR56_opening_no_of_cards() != null) {
                        R56cell1.setCellValue(record3.getR56_opening_no_of_cards().doubleValue());
                        R56cell1.setCellStyle(numberStyle);
                    } else {
                        R56cell1.setCellValue("");
                        R56cell1.setCellStyle(textStyle);
                    }

                    // R56 Col D
                    Cell R56cell2 = row.createCell(3);
                    if (record3.getR56_no_of_cards_issued() != null) {
                        R56cell2.setCellValue(record3.getR56_no_of_cards_issued().doubleValue());
                        R56cell2.setCellStyle(numberStyle);
                    } else {
                        R56cell2.setCellValue("");
                        R56cell2.setCellStyle(textStyle);
                    }

                    // R56 Col E
                    Cell R56cell3 = row.createCell(4);
                    if (record3.getR56_no_cards_of_closed() != null) {
                        R56cell3.setCellValue(record3.getR56_no_cards_of_closed().doubleValue());
                        R56cell3.setCellStyle(numberStyle);
                    } else {
                        R56cell3.setCellValue("");
                        R56cell3.setCellStyle(textStyle);
                    }

                    // R57 Col C
                    row = sheet.getRow(56);
                    Cell R57cell1 = row.createCell(2);
                    if (record3.getR57_opening_no_of_cards() != null) {
                        R57cell1.setCellValue(record3.getR57_opening_no_of_cards().doubleValue());
                        R57cell1.setCellStyle(numberStyle);
                    } else {
                        R57cell1.setCellValue("");
                        R57cell1.setCellStyle(textStyle);
                    }

                    // R57 Col D
                    Cell R57cell2 = row.createCell(3);
                    if (record3.getR57_no_of_cards_issued() != null) {
                        R57cell2.setCellValue(record3.getR57_no_of_cards_issued().doubleValue());
                        R57cell2.setCellStyle(numberStyle);
                    } else {
                        R57cell2.setCellValue("");
                        R57cell2.setCellStyle(textStyle);
                    }

                    // R57 Col E
                    Cell R57cell3 = row.createCell(4);
                    if (record3.getR57_no_cards_of_closed() != null) {
                        R57cell3.setCellValue(record3.getR57_no_cards_of_closed().doubleValue());
                        R57cell3.setCellStyle(numberStyle);
                    } else {
                        R57cell3.setCellValue("");
                        R57cell3.setCellStyle(textStyle);
                    }

                    // R58 Col C
                    row = sheet.getRow(57);
                    Cell R58cell1 = row.createCell(2);
                    if (record3.getR58_opening_no_of_cards() != null) {
                        R58cell1.setCellValue(record3.getR58_opening_no_of_cards().doubleValue());
                        R58cell1.setCellStyle(numberStyle);
                    } else {
                        R58cell1.setCellValue("");
                        R58cell1.setCellStyle(textStyle);
                    }

                    // R58 Col D
                    Cell R58cell2 = row.createCell(3);
                    if (record3.getR58_no_of_cards_issued() != null) {
                        R58cell2.setCellValue(record3.getR58_no_of_cards_issued().doubleValue());
                        R58cell2.setCellStyle(numberStyle);
                    } else {
                        R58cell2.setCellValue("");
                        R58cell2.setCellStyle(textStyle);
                    }

                    // R58 Col E
                    Cell R58cell3 = row.createCell(4);
                    if (record3.getR58_no_cards_of_closed() != null) {
                        R58cell3.setCellValue(record3.getR58_no_cards_of_closed().doubleValue());
                        R58cell3.setCellStyle(numberStyle);
                    } else {
                        R58cell3.setCellValue("");
                        R58cell3.setCellStyle(textStyle);
                    }

                    // R59 Col C
                    row = sheet.getRow(58);
                    Cell R59cell1 = row.createCell(2);
                    if (record3.getR59_opening_no_of_cards() != null) {
                        R59cell1.setCellValue(record3.getR59_opening_no_of_cards().doubleValue());
                        R59cell1.setCellStyle(numberStyle);
                    } else {
                        R59cell1.setCellValue("");
                        R59cell1.setCellStyle(textStyle);
                    }

                    // R59 Col D
                    Cell R59cell2 = row.createCell(3);
                    if (record3.getR59_no_of_cards_issued() != null) {
                        R59cell2.setCellValue(record3.getR59_no_of_cards_issued().doubleValue());
                        R59cell2.setCellStyle(numberStyle);
                    } else {
                        R59cell2.setCellValue("");
                        R59cell2.setCellStyle(textStyle);
                    }

                    // R59 Col E
                    Cell R59cell3 = row.createCell(4);
                    if (record3.getR59_no_cards_of_closed() != null) {
                        R59cell3.setCellValue(record3.getR59_no_cards_of_closed().doubleValue());
                        R59cell3.setCellStyle(numberStyle);
                    } else {
                        R59cell3.setCellValue("");
                        R59cell3.setCellStyle(textStyle);
                    }

                    // R60 Col C
                    row = sheet.getRow(59);
                    Cell R60cell1 = row.createCell(2);
                    if (record3.getR60_opening_no_of_cards() != null) {
                        R60cell1.setCellValue(record3.getR60_opening_no_of_cards().doubleValue());
                        R60cell1.setCellStyle(numberStyle);
                    } else {
                        R60cell1.setCellValue("");
                        R60cell1.setCellStyle(textStyle);
                    }

                    // R60 Col D
                    Cell R60cell2 = row.createCell(3);
                    if (record3.getR60_no_of_cards_issued() != null) {
                        R60cell2.setCellValue(record3.getR60_no_of_cards_issued().doubleValue());
                        R60cell2.setCellStyle(numberStyle);
                    } else {
                        R60cell2.setCellValue("");
                        R60cell2.setCellStyle(textStyle);
                    }
                    // R60 Col E
                    Cell R60cell3 = row.createCell(4);
                    if (record3.getR60_no_cards_of_closed() != null) {
                        R60cell3.setCellValue(record3.getR60_no_cards_of_closed().doubleValue());
                        R60cell3.setCellStyle(numberStyle);
                    } else {
                        R60cell3.setCellValue("");
                        R60cell3.setCellStyle(textStyle);
                    }

                    // R61 Col C
                    row = sheet.getRow(60);
                    Cell R61cell1 = row.createCell(2);
                    if (record3.getR61_opening_no_of_cards() != null) {
                        R61cell1.setCellValue(record3.getR61_opening_no_of_cards().doubleValue());
                        R61cell1.setCellStyle(numberStyle);
                    } else {
                        R61cell1.setCellValue("");
                        R61cell1.setCellStyle(textStyle);
                    }

                    // R61 Col D
                    Cell R61cell2 = row.createCell(3);
                    if (record3.getR61_no_of_cards_issued() != null) {
                        R61cell2.setCellValue(record3.getR61_no_of_cards_issued().doubleValue());
                        R61cell2.setCellStyle(numberStyle);
                    } else {
                        R61cell2.setCellValue("");
                        R61cell2.setCellStyle(textStyle);
                    }
                    // R61 Col E
                    Cell R61cell3 = row.createCell(4);
                    if (record3.getR61_no_cards_of_closed() != null) {
                        R61cell3.setCellValue(record3.getR61_no_cards_of_closed().doubleValue());
                        R61cell3.setCellStyle(numberStyle);
                    } else {
                        R61cell3.setCellValue("");
                        R61cell3.setCellStyle(textStyle);
                    }

                    // R62 Col C
                    row = sheet.getRow(61);
                    Cell R62cell1 = row.createCell(2);
                    if (record3.getR62_opening_no_of_cards() != null) {
                        R62cell1.setCellValue(record3.getR62_opening_no_of_cards().doubleValue());
                        R62cell1.setCellStyle(numberStyle);
                    } else {
                        R62cell1.setCellValue("");
                        R62cell1.setCellStyle(textStyle);
                    }

                    // R62 Col D
                    Cell R62cell2 = row.createCell(3);
                    if (record3.getR62_no_of_cards_issued() != null) {
                        R62cell2.setCellValue(record3.getR62_no_of_cards_issued().doubleValue());
                        R62cell2.setCellStyle(numberStyle);
                    } else {
                        R62cell2.setCellValue("");
                        R62cell2.setCellStyle(textStyle);
                    }
                    // R62 Col E
                    Cell R62cell3 = row.createCell(4);
                    if (record3.getR62_no_cards_of_closed() != null) {
                        R62cell3.setCellValue(record3.getR62_no_cards_of_closed().doubleValue());
                        R62cell3.setCellStyle(numberStyle);
                    } else {
                        R62cell3.setCellValue("");
                        R62cell3.setCellStyle(textStyle);
                    }

                    // R63 Col C
                    row = sheet.getRow(62);
                    Cell R63cell1 = row.createCell(2);
                    if (record3.getR63_opening_no_of_cards() != null) {
                        R63cell1.setCellValue(record3.getR63_opening_no_of_cards().doubleValue());
                        R63cell1.setCellStyle(numberStyle);
                    } else {
                        R63cell1.setCellValue("");
                        R63cell1.setCellStyle(textStyle);
                    }

                    // R63 Col D
                    Cell R63cell2 = row.createCell(3);
                    if (record3.getR63_no_of_cards_issued() != null) {
                        R63cell2.setCellValue(record3.getR63_no_of_cards_issued().doubleValue());
                        R63cell2.setCellStyle(numberStyle);
                    } else {
                        R63cell2.setCellValue("");
                        R63cell2.setCellStyle(textStyle);
                    }
                    // R63 Col E
                    Cell R63cell3 = row.createCell(4);
                    if (record3.getR63_no_cards_of_closed() != null) {
                        R63cell3.setCellValue(record3.getR63_no_cards_of_closed().doubleValue());
                        R63cell3.setCellStyle(numberStyle);
                    } else {
                        R63cell3.setCellValue("");
                        R63cell3.setCellStyle(textStyle);
                    }

                    // R64 Col C
                    row = sheet.getRow(63);
                    Cell R64cell1 = row.createCell(2);
                    if (record3.getR64_opening_no_of_cards() != null) {
                        R64cell1.setCellValue(record3.getR64_opening_no_of_cards().doubleValue());
                        R64cell1.setCellStyle(numberStyle);
                    } else {
                        R64cell1.setCellValue("");
                        R64cell1.setCellStyle(textStyle);
                    }

                    // R64 Col D
                    Cell R64cell2 = row.createCell(3);
                    if (record3.getR64_no_of_cards_issued() != null) {
                        R64cell2.setCellValue(record3.getR64_no_of_cards_issued().doubleValue());
                        R64cell2.setCellStyle(numberStyle);
                    } else {
                        R64cell2.setCellValue("");
                        R64cell2.setCellStyle(textStyle);
                    }
                    // R64 Col E
                    Cell R64cell3 = row.createCell(4);
                    if (record3.getR64_no_cards_of_closed() != null) {
                        R64cell3.setCellValue(record3.getR64_no_cards_of_closed().doubleValue());
                        R64cell3.setCellStyle(numberStyle);
                    } else {
                        R64cell3.setCellValue("");
                        R64cell3.setCellStyle(textStyle);
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

    public List<Object[]> getQ_BRANCHNETResub() {
        List<Object[]> resubList = new ArrayList<>();
        try {
            List<Q_BRANCHNET_Archival_Summary_Entity1> latestArchivalList = Q_BRANCHNET_Archival_Summary_Repo1
                    .getdatabydateListWithVersion();

            if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
                for (Q_BRANCHNET_Archival_Summary_Entity1 entity : latestArchivalList) {
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
            System.err.println("Error fetching Q_BRANCHNET Resub data: " + e.getMessage());
            e.printStackTrace();
        }
        return resubList;
    }

    public List<Object[]> getQ_BRANCHNETArchival() {
        List<Object[]> archivalList = new ArrayList<>();
        try {
            List<Q_BRANCHNET_Archival_Summary_Entity1> latestArchivalList = Q_BRANCHNET_Archival_Summary_Repo1
                    .getdatabydateListWithVersion();

            if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
                for (Q_BRANCHNET_Archival_Summary_Entity1 entity : latestArchivalList) {
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
            System.err.println("Error fetching Q_BRANCHNET Resub data: " + e.getMessage());
            e.printStackTrace();
        }
        return archivalList;
    }

    // public void updateReportReSub(
    //         Q_BRANCHNET_Summary_Entity1 updatedEntity1,
    //         Q_BRANCHNET_Summary_Entity2 updatedEntity2,
    //         Q_BRANCHNET_Summary_Entity3 updatedEntity3,
    //         Q_BRANCHNET_Summary_Entity4 updatedEntity4) {

    //     System.out.println("Came to Q_BRANCHNET Resub Service");
    //     System.out.println("Report Date: " + updatedEntity1.getReportDate());

    //     Date reportDate = updatedEntity1.getReportDate();
    //     int newVersion = 1;

    //     try {
    //         // 🔹 Fetch the latest archival version for this report date from Entity1
    //         Optional<Q_BRANCHNET_Archival_Summary_Entity1> latestArchivalOpt1 = Q_BRANCHNET_Archival_Summary_Repo1
    //                 .getLatestArchivalVersionByDate(reportDate);

    //         if (latestArchivalOpt1.isPresent()) {
    //             Q_BRANCHNET_Archival_Summary_Entity1 latestArchival = latestArchivalOpt1.get();
    //             try {
    //                 newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1;
    //             } catch (NumberFormatException e) {
    //                 System.err.println("Invalid version format. Defaulting to version 1");
    //                 newVersion = 1;
    //             }
    //         } else {
    //             System.out.println("No previous archival found for date: " + reportDate);
    //         }

    //         // 🔹 Prevent duplicate version number in Repo1
    //         boolean exists = Q_BRANCHNET_Archival_Summary_Repo1
    //                 .findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
    //                 .isPresent();

    //         if (exists) {
    //             throw new RuntimeException("⚠ Version " + newVersion + " already exists for report date " + reportDate);
    //         }

    //         // Copy data from summary to archival entities for all 3 entities
    //         Q_BRANCHNET_Archival_Summary_Entity1 archivalEntity1 = new Q_BRANCHNET_Archival_Summary_Entity1();
    //         Q_BRANCHNET_Archival_Summary_Entity2 archivalEntity2 = new Q_BRANCHNET_Archival_Summary_Entity2();
    //         Q_BRANCHNET_Archival_Summary_Entity3 archivalEntity3 = new Q_BRANCHNET_Archival_Summary_Entity3();
    //         Q_BRANCHNET_Archival_Summary_Entity4 archivalEntity4 = new Q_BRANCHNET_Archival_Summary_Entity4();

    //         org.springframework.beans.BeanUtils.copyProperties(updatedEntity1, archivalEntity1);
    //         org.springframework.beans.BeanUtils.copyProperties(updatedEntity2, archivalEntity2);
    //         org.springframework.beans.BeanUtils.copyProperties(updatedEntity3, archivalEntity3);
    //         org.springframework.beans.BeanUtils.copyProperties(updatedEntity4, archivalEntity4);

    //         // Set common fields
    //         Date now = new Date();
    //         archivalEntity1.setReportDate(reportDate);
    //         archivalEntity2.setReportDate(reportDate);
    //         archivalEntity3.setReportDate(reportDate);
    //         archivalEntity4.setReportDate(reportDate);

    //         archivalEntity1.setReportVersion(String.valueOf(newVersion));
    //         archivalEntity2.setReportVersion(String.valueOf(newVersion));
    //         archivalEntity3.setReportVersion(String.valueOf(newVersion));
    //         archivalEntity4.setReportVersion(String.valueOf(newVersion));

    //         archivalEntity1.setReportResubDate(now);
    //         archivalEntity2.setReportResubDate(now);
    //         archivalEntity3.setReportResubDate(now);
    //         archivalEntity4.setReportResubDate(now);

    //         System.out.println("Saving new archival version: " + newVersion);

    //         // Save to all three archival repositories
    //         Q_BRANCHNET_Archival_Summary_Repo1.save(archivalEntity1);
    //         Q_BRANCHNET_Archival_Summary_Repo2.save(archivalEntity2);
    //         Q_BRANCHNET_Archival_Summary_Repo3.save(archivalEntity3);
    //         Q_BRANCHNET_Archival_Summary_Repo4.save(archivalEntity4);

    //         System.out.println("Saved archival version successfully: " + newVersion);

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         throw new RuntimeException("Error while creating Q_BRANCHNET archival resubmission record", e);
    //     }
    // }

    // public byte[] BRRS_Q_BRANCHNETResubExcel(String filename, String reportId, String fromdate,
    //         String todate, String currency, String dtltype,
    //         String type, String version) throws Exception {

    //     logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

    //     if (type.equals("RESUB") & version != null) {

    //     }

    //     List<Q_BRANCHNET_Archival_Summary_Entity1> dataList = Q_BRANCHNET_Archival_Summary_Repo1
    //             .getdatabydateListarchival(dateformat.parse(todate), version);
    //     List<Q_BRANCHNET_Archival_Summary_Entity2> dataList1 = Q_BRANCHNET_Archival_Summary_Repo2
    //             .getdatabydateListarchival(dateformat.parse(todate), version);
    //     List<Q_BRANCHNET_Archival_Summary_Entity3> dataList2 = Q_BRANCHNET_Archival_Summary_Repo3
    //             .getdatabydateListarchival(dateformat.parse(todate), version);
    //     List<Q_BRANCHNET_Archival_Summary_Entity4> dataList3 = Q_BRANCHNET_Archival_Summary_Repo4
    //             .getdatabydateListarchival(dateformat.parse(todate), version);

    //     if (dataList.isEmpty()) {
    //         logger.warn("Service: No data found for Q_BRANCHNET report. Returning empty result.");
    //         return new byte[0];
    //     }

    //     String templateDir = env.getProperty("output.exportpathtemp");
    //     String templateFileName = filename;
    //     System.out.println(filename);
    //     Path templatePath = Paths.get(templateDir, templateFileName);
    //     System.out.println(templatePath);

    //     logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

    //     if (!Files.exists(templatePath)) {
    //         // This specific exception will be caught by the controller.
    //         throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
    //     }
    //     if (!Files.isReadable(templatePath)) {
    //         // A specific exception for permission errors.
    //         throw new SecurityException(
    //                 "Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
    //     }

    //     // This try-with-resources block is perfect. It guarantees all resources are
    //     // closed automatically.
    //     try (InputStream templateInputStream = Files.newInputStream(templatePath);
    //             Workbook workbook = WorkbookFactory.create(templateInputStream);
    //             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

    //         Sheet sheet = workbook.getSheetAt(0);

    //         // --- Style Definitions ---
    //         CreationHelper createHelper = workbook.getCreationHelper();

    //         CellStyle dateStyle = workbook.createCellStyle();
    //         dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
    //         dateStyle.setBorderBottom(BorderStyle.THIN);
    //         dateStyle.setBorderTop(BorderStyle.THIN);
    //         dateStyle.setBorderLeft(BorderStyle.THIN);
    //         dateStyle.setBorderRight(BorderStyle.THIN);

    //         CellStyle textStyle = workbook.createCellStyle();
    //         textStyle.setBorderBottom(BorderStyle.THIN);
    //         textStyle.setBorderTop(BorderStyle.THIN);
    //         textStyle.setBorderLeft(BorderStyle.THIN);
    //         textStyle.setBorderRight(BorderStyle.THIN);

    //         // Create the font
    //         Font font = workbook.createFont();
    //         font.setFontHeightInPoints((short) 8); // size 8
    //         font.setFontName("Arial");

    //         CellStyle numberStyle = workbook.createCellStyle();
    //         // numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
    //         numberStyle.setBorderBottom(BorderStyle.THIN);
    //         numberStyle.setBorderTop(BorderStyle.THIN);
    //         numberStyle.setBorderLeft(BorderStyle.THIN);
    //         numberStyle.setBorderRight(BorderStyle.THIN);
    //         numberStyle.setFont(font);
    //         // --- End of Style Definitions ---
    //         int startRow = 9;

    //         if (!dataList.isEmpty()) {
    //             for (int i = 0; i < dataList.size(); i++) {

    //                 Q_BRANCHNET_Archival_Summary_Entity1 record = dataList.get(i);
    //                 Q_BRANCHNET_Archival_Summary_Entity2 record1 = dataList1.get(i);
    //                 Q_BRANCHNET_Archival_Summary_Entity3 record2 = dataList2.get(i);
    //                 Q_BRANCHNET_Archival_Summary_Entity4 record3 = dataList3.get(i);
    //                 System.out.println("rownumber=" + startRow + i);
    //                 Row row = sheet.getRow(startRow + i);
    //                 if (row == null) {
    //                     row = sheet.createRow(startRow + i);
    //                 }
    //                 // R10 Col C

    //                 Cell R10cell1 = row.createCell(2);
    //                 if (record.getR10_no1_of_branches() != null) {
    //                     R10cell1.setCellValue(record.getR10_no1_of_branches().doubleValue());
    //                     R10cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R10cell1.setCellValue("");
    //                     R10cell1.setCellStyle(textStyle);
    //                 }

    //                 // R10 Col D
    //                 Cell R10cell2 = row.createCell(3);
    //                 if (record.getR10_no1_of_sub_branches() != null) {
    //                     R10cell2.setCellValue(record.getR10_no1_of_sub_branches().doubleValue());
    //                     R10cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R10cell2.setCellValue("");
    //                     R10cell2.setCellStyle(textStyle);
    //                 }

    //                 // R10 Col E
    //                 Cell R10cell3 = row.createCell(4);
    //                 if (record.getR10_no1_of_agencies() != null) {
    //                     R10cell3.setCellValue(record.getR10_no1_of_agencies().doubleValue());
    //                     R10cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R10cell3.setCellValue("");
    //                     R10cell3.setCellStyle(textStyle);
    //                 }
    //                 // R11 Col C
    //                 row = sheet.getRow(10);
    //                 Cell R11cell1 = row.createCell(2);
    //                 if (record.getR11_no1_of_branches() != null) {
    //                     R11cell1.setCellValue(record.getR11_no1_of_branches().doubleValue());
    //                     R11cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R11cell1.setCellValue("");
    //                     R11cell1.setCellStyle(textStyle);
    //                 }

    //                 // R11 Col D
    //                 Cell R11cell2 = row.createCell(3);
    //                 if (record.getR11_no1_of_sub_branches() != null) {
    //                     R11cell2.setCellValue(record.getR11_no1_of_sub_branches().doubleValue());
    //                     R11cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R11cell2.setCellValue("");
    //                     R11cell2.setCellStyle(textStyle);
    //                 }

    //                 // R11 Col E
    //                 Cell R11cell3 = row.createCell(4);
    //                 if (record.getR11_no1_of_agencies() != null) {
    //                     R11cell3.setCellValue(record.getR11_no1_of_agencies().doubleValue());
    //                     R11cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R11cell3.setCellValue("");
    //                     R11cell3.setCellStyle(textStyle);
    //                 }
    //                 // R12 Col C
    //                 row = sheet.getRow(11);
    //                 Cell R12cell1 = row.createCell(2);
    //                 if (record.getR12_no1_of_branches() != null) {
    //                     R12cell1.setCellValue(record.getR12_no1_of_branches().doubleValue());
    //                     R12cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R12cell1.setCellValue("");
    //                     R12cell1.setCellStyle(textStyle);
    //                 }

    //                 // R12 Col D
    //                 Cell R12cell2 = row.createCell(3);
    //                 if (record.getR12_no1_of_sub_branches() != null) {
    //                     R12cell2.setCellValue(record.getR12_no1_of_sub_branches().doubleValue());
    //                     R12cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R12cell2.setCellValue("");
    //                     R12cell2.setCellStyle(textStyle);
    //                 }

    //                 // R12 Col E
    //                 Cell R12cell3 = row.createCell(4);
    //                 if (record.getR12_no1_of_agencies() != null) {
    //                     R12cell3.setCellValue(record.getR12_no1_of_agencies().doubleValue());
    //                     R12cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R12cell3.setCellValue("");
    //                     R12cell3.setCellStyle(textStyle);
    //                 }
    //                 // R13 Col C
    //                 row = sheet.getRow(12);
    //                 Cell R13cell1 = row.createCell(2);
    //                 if (record.getR13_no1_of_branches() != null) {
    //                     R13cell1.setCellValue(record.getR13_no1_of_branches().doubleValue());
    //                     R13cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R13cell1.setCellValue("");
    //                     R13cell1.setCellStyle(textStyle);
    //                 }

    //                 // R13 Col D
    //                 Cell R13cell2 = row.createCell(3);
    //                 if (record.getR13_no1_of_sub_branches() != null) {
    //                     R13cell2.setCellValue(record.getR13_no1_of_sub_branches().doubleValue());
    //                     R13cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R13cell2.setCellValue("");
    //                     R13cell2.setCellStyle(textStyle);
    //                 }

    //                 // R13 Col E
    //                 Cell R13cell3 = row.createCell(4);
    //                 if (record.getR13_no1_of_agencies() != null) {
    //                     R13cell3.setCellValue(record.getR13_no1_of_agencies().doubleValue());
    //                     R13cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R13cell3.setCellValue("");
    //                     R13cell3.setCellStyle(textStyle);
    //                 }
    //                 // R14 Col C
    //                 row = sheet.getRow(13);
    //                 Cell R14cell1 = row.createCell(2);
    //                 if (record.getR14_no1_of_branches() != null) {
    //                     R14cell1.setCellValue(record.getR14_no1_of_branches().doubleValue());
    //                     R14cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R14cell1.setCellValue("");
    //                     R14cell1.setCellStyle(textStyle);
    //                 }

    //                 // R14 Col D
    //                 Cell R14cell2 = row.createCell(3);
    //                 if (record.getR14_no1_of_sub_branches() != null) {
    //                     R14cell2.setCellValue(record.getR14_no1_of_sub_branches().doubleValue());
    //                     R14cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R14cell2.setCellValue("");
    //                     R14cell2.setCellStyle(textStyle);
    //                 }

    //                 // R14 Col E
    //                 Cell R14cell3 = row.createCell(4);
    //                 if (record.getR14_no1_of_agencies() != null) {
    //                     R14cell3.setCellValue(record.getR14_no1_of_agencies().doubleValue());
    //                     R14cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R14cell3.setCellValue("");
    //                     R14cell3.setCellStyle(textStyle);
    //                 }
    //                 // R15 Col C
    //                 row = sheet.getRow(14);
    //                 Cell R15cell1 = row.createCell(2);
    //                 if (record.getR15_no1_of_branches() != null) {
    //                     R15cell1.setCellValue(record.getR15_no1_of_branches().doubleValue());
    //                     R15cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R15cell1.setCellValue("");
    //                     R15cell1.setCellStyle(textStyle);
    //                 }

    //                 // R15 Col D
    //                 Cell R15cell2 = row.createCell(3);
    //                 if (record.getR15_no1_of_sub_branches() != null) {
    //                     R15cell2.setCellValue(record.getR15_no1_of_sub_branches().doubleValue());
    //                     R15cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R15cell2.setCellValue("");
    //                     R15cell2.setCellStyle(textStyle);
    //                 }

    //                 // R15 Col E
    //                 Cell R15cell3 = row.createCell(4);
    //                 if (record.getR15_no1_of_agencies() != null) {
    //                     R15cell3.setCellValue(record.getR15_no1_of_agencies().doubleValue());
    //                     R15cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R15cell3.setCellValue("");
    //                     R15cell3.setCellStyle(textStyle);
    //                 }
    //                 // R16 Col C
    //                 row = sheet.getRow(15);
    //                 Cell R16cell1 = row.createCell(2);
    //                 if (record.getR16_no1_of_branches() != null) {
    //                     R16cell1.setCellValue(record.getR16_no1_of_branches().doubleValue());
    //                     R16cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R16cell1.setCellValue("");
    //                     R16cell1.setCellStyle(textStyle);
    //                 }

    //                 // R16 Col D
    //                 Cell R16cell2 = row.createCell(3);
    //                 if (record.getR16_no1_of_sub_branches() != null) {
    //                     R16cell2.setCellValue(record.getR16_no1_of_sub_branches().doubleValue());
    //                     R16cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R16cell2.setCellValue("");
    //                     R16cell2.setCellStyle(textStyle);
    //                 }

    //                 // R16 Col E
    //                 Cell R16cell3 = row.createCell(4);
    //                 if (record.getR16_no1_of_agencies() != null) {
    //                     R16cell3.setCellValue(record.getR16_no1_of_agencies().doubleValue());
    //                     R16cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R16cell3.setCellValue("");
    //                     R16cell3.setCellStyle(textStyle);
    //                 }
    //                 // R17 Col C
    //                 row = sheet.getRow(16);
    //                 Cell R17cell1 = row.createCell(2);
    //                 if (record.getR17_no1_of_branches() != null) {
    //                     R17cell1.setCellValue(record.getR17_no1_of_branches().doubleValue());
    //                     R17cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R17cell1.setCellValue("");
    //                     R17cell1.setCellStyle(textStyle);
    //                 }

    //                 // R17 Col D
    //                 Cell R17cell2 = row.createCell(3);
    //                 if (record.getR17_no1_of_sub_branches() != null) {
    //                     R17cell2.setCellValue(record.getR17_no1_of_sub_branches().doubleValue());
    //                     R17cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R17cell2.setCellValue("");
    //                     R17cell2.setCellStyle(textStyle);
    //                 }

    //                 // R17 Col E
    //                 Cell R17cell3 = row.createCell(4);
    //                 if (record.getR17_no1_of_agencies() != null) {
    //                     R17cell3.setCellValue(record.getR17_no1_of_agencies().doubleValue());
    //                     R17cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R17cell3.setCellValue("");
    //                     R17cell3.setCellStyle(textStyle);
    //                 }
    //                 // R18 Col C
    //                 row = sheet.getRow(17);
    //                 Cell R18cell1 = row.createCell(2);
    //                 if (record.getR18_no1_of_branches() != null) {
    //                     R18cell1.setCellValue(record.getR18_no1_of_branches().doubleValue());
    //                     R18cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R18cell1.setCellValue("");
    //                     R18cell1.setCellStyle(textStyle);
    //                 }

    //                 // R18 Col D
    //                 Cell R18cell2 = row.createCell(3);
    //                 if (record.getR18_no1_of_sub_branches() != null) {
    //                     R18cell2.setCellValue(record.getR18_no1_of_sub_branches().doubleValue());
    //                     R18cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R18cell2.setCellValue("");
    //                     R18cell2.setCellStyle(textStyle);
    //                 }

    //                 // R18 Col E
    //                 Cell R18cell3 = row.createCell(4);
    //                 if (record.getR18_no1_of_agencies() != null) {
    //                     R18cell3.setCellValue(record.getR18_no1_of_agencies().doubleValue());
    //                     R18cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R18cell3.setCellValue("");
    //                     R18cell3.setCellStyle(textStyle);
    //                 }
    //                 // R19 Col C
    //                 row = sheet.getRow(18);
    //                 Cell R19cell1 = row.createCell(2);
    //                 if (record.getR19_no1_of_branches() != null) {
    //                     R19cell1.setCellValue(record.getR19_no1_of_branches().doubleValue());
    //                     R19cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R19cell1.setCellValue("");
    //                     R19cell1.setCellStyle(textStyle);
    //                 }

    //                 // R19 Col D
    //                 Cell R19cell2 = row.createCell(3);
    //                 if (record.getR19_no1_of_sub_branches() != null) {
    //                     R19cell2.setCellValue(record.getR19_no1_of_sub_branches().doubleValue());
    //                     R19cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R19cell2.setCellValue("");
    //                     R19cell2.setCellStyle(textStyle);
    //                 }

    //                 // R19 Col E
    //                 Cell R19cell3 = row.createCell(4);
    //                 if (record.getR19_no1_of_agencies() != null) {
    //                     R19cell3.setCellValue(record.getR19_no1_of_agencies().doubleValue());
    //                     R19cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R19cell3.setCellValue("");
    //                     R19cell3.setCellStyle(textStyle);
    //                 }
    //                 // TABLE 2
    //                 // R25 Col C
    //                 row = sheet.getRow(24);
    //                 Cell R25cell1 = row.createCell(2);
    //                 if (record1.getR25_no_of_atms() != null) {
    //                     R25cell1.setCellValue(record1.getR25_no_of_atms().doubleValue());
    //                     R25cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R25cell1.setCellValue("");
    //                     R25cell1.setCellStyle(textStyle);
    //                 }

    //                 // R25 Col D
    //                 Cell R25cell2 = row.createCell(3);
    //                 if (record1.getR25_no_of_mini_atms() != null) {
    //                     R25cell2.setCellValue(record1.getR25_no_of_mini_atms().doubleValue());
    //                     R25cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R25cell2.setCellValue("");
    //                     R25cell2.setCellStyle(textStyle);
    //                 }

    //                 // R25 Col E
    //                 Cell R25cell3 = row.createCell(4);
    //                 if (record1.getR25_encashment_points() != null) {
    //                     R25cell3.setCellValue(record1.getR25_encashment_points().doubleValue());
    //                     R25cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R25cell3.setCellValue("");
    //                     R25cell3.setCellStyle(textStyle);
    //                 }
    //                 // R26 Col C
    //                 row = sheet.getRow(25);
    //                 Cell R26cell1 = row.createCell(2);
    //                 if (record1.getR26_no_of_atms() != null) {
    //                     R26cell1.setCellValue(record1.getR26_no_of_atms().doubleValue());
    //                     R26cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R26cell1.setCellValue("");
    //                     R26cell1.setCellStyle(textStyle);
    //                 }

    //                 // R26 Col D
    //                 Cell R26cell2 = row.createCell(3);
    //                 if (record1.getR26_no_of_mini_atms() != null) {
    //                     R26cell2.setCellValue(record1.getR26_no_of_mini_atms().doubleValue());
    //                     R26cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R26cell2.setCellValue("");
    //                     R26cell2.setCellStyle(textStyle);
    //                 }

    //                 // R26 Col E
    //                 Cell R26cell3 = row.createCell(4);
    //                 if (record1.getR26_encashment_points() != null) {
    //                     R26cell3.setCellValue(record1.getR26_encashment_points().doubleValue());
    //                     R26cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R26cell3.setCellValue("");
    //                     R26cell3.setCellStyle(textStyle);
    //                 }
    //                 // R27 Col C
    //                 row = sheet.getRow(26);
    //                 Cell R27cell1 = row.createCell(2);
    //                 if (record1.getR27_no_of_atms() != null) {
    //                     R27cell1.setCellValue(record1.getR27_no_of_atms().doubleValue());
    //                     R27cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R27cell1.setCellValue("");
    //                     R27cell1.setCellStyle(textStyle);
    //                 }

    //                 // R27 Col D
    //                 Cell R27cell2 = row.createCell(3);
    //                 if (record1.getR27_no_of_mini_atms() != null) {
    //                     R27cell2.setCellValue(record1.getR27_no_of_mini_atms().doubleValue());
    //                     R27cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R27cell2.setCellValue("");
    //                     R27cell2.setCellStyle(textStyle);
    //                 }

    //                 // R27 Col E
    //                 Cell R27cell3 = row.createCell(4);
    //                 if (record1.getR27_encashment_points() != null) {
    //                     R27cell3.setCellValue(record1.getR27_encashment_points().doubleValue());
    //                     R27cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R27cell3.setCellValue("");
    //                     R27cell3.setCellStyle(textStyle);
    //                 }
    //                 // R28 Col C
    //                 row = sheet.getRow(27);
    //                 Cell R28cell1 = row.createCell(2);
    //                 if (record1.getR28_no_of_atms() != null) {
    //                     R28cell1.setCellValue(record1.getR28_no_of_atms().doubleValue());
    //                     R28cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R28cell1.setCellValue("");
    //                     R28cell1.setCellStyle(textStyle);
    //                 }

    //                 // R28 Col D
    //                 Cell R28cell2 = row.createCell(3);
    //                 if (record1.getR28_no_of_mini_atms() != null) {
    //                     R28cell2.setCellValue(record1.getR28_no_of_mini_atms().doubleValue());
    //                     R28cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R28cell2.setCellValue("");
    //                     R28cell2.setCellStyle(textStyle);
    //                 }

    //                 // R28 Col E
    //                 Cell R28cell3 = row.createCell(4);
    //                 if (record1.getR28_encashment_points() != null) {
    //                     R28cell3.setCellValue(record1.getR28_encashment_points().doubleValue());
    //                     R28cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R28cell3.setCellValue("");
    //                     R28cell3.setCellStyle(textStyle);
    //                 }
    //                 // R29 Col C
    //                 row = sheet.getRow(28);
    //                 Cell R29cell1 = row.createCell(2);
    //                 if (record1.getR29_no_of_atms() != null) {
    //                     R29cell1.setCellValue(record1.getR29_no_of_atms().doubleValue());
    //                     R29cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R29cell1.setCellValue("");
    //                     R29cell1.setCellStyle(textStyle);
    //                 }

    //                 // R29 Col D
    //                 Cell R29cell2 = row.createCell(3);
    //                 if (record1.getR29_no_of_mini_atms() != null) {
    //                     R29cell2.setCellValue(record1.getR29_no_of_mini_atms().doubleValue());
    //                     R29cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R29cell2.setCellValue("");
    //                     R29cell2.setCellStyle(textStyle);
    //                 }

    //                 // R29 Col E
    //                 Cell R29cell3 = row.createCell(4);
    //                 if (record1.getR29_encashment_points() != null) {
    //                     R29cell3.setCellValue(record1.getR29_encashment_points().doubleValue());
    //                     R29cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R29cell3.setCellValue("");
    //                     R29cell3.setCellStyle(textStyle);
    //                 }
    //                 // R30 Col C
    //                 row = sheet.getRow(29);
    //                 Cell R30cell1 = row.createCell(2);
    //                 if (record1.getR30_no_of_atms() != null) {
    //                     R30cell1.setCellValue(record1.getR30_no_of_atms().doubleValue());
    //                     R30cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R30cell1.setCellValue("");
    //                     R30cell1.setCellStyle(textStyle);
    //                 }

    //                 // R30 Col D
    //                 Cell R30cell2 = row.createCell(3);
    //                 if (record1.getR30_no_of_mini_atms() != null) {
    //                     R30cell2.setCellValue(record1.getR30_no_of_mini_atms().doubleValue());
    //                     R30cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R30cell2.setCellValue("");
    //                     R30cell2.setCellStyle(textStyle);
    //                 }

    //                 // R30 Col E
    //                 Cell R30cell3 = row.createCell(4);
    //                 if (record1.getR30_encashment_points() != null) {
    //                     R30cell3.setCellValue(record1.getR30_encashment_points().doubleValue());
    //                     R30cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R30cell3.setCellValue("");
    //                     R30cell3.setCellStyle(textStyle);
    //                 }
    //                 // R31 Col C
    //                 row = sheet.getRow(30);
    //                 Cell R31cell1 = row.createCell(2);
    //                 if (record1.getR31_no_of_atms() != null) {
    //                     R31cell1.setCellValue(record1.getR31_no_of_atms().doubleValue());
    //                     R31cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R31cell1.setCellValue("");
    //                     R31cell1.setCellStyle(textStyle);
    //                 }

    //                 // R31 Col D
    //                 Cell R31cell2 = row.createCell(3);
    //                 if (record1.getR31_no_of_mini_atms() != null) {
    //                     R31cell2.setCellValue(record1.getR31_no_of_mini_atms().doubleValue());
    //                     R31cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R31cell2.setCellValue("");
    //                     R31cell2.setCellStyle(textStyle);
    //                 }

    //                 // R31 Col E
    //                 Cell R31cell3 = row.createCell(4);
    //                 if (record1.getR31_encashment_points() != null) {
    //                     R31cell3.setCellValue(record1.getR31_encashment_points().doubleValue());
    //                     R31cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R31cell3.setCellValue("");
    //                     R31cell3.setCellStyle(textStyle);
    //                 }
    //                 // R32 Col C
    //                 row = sheet.getRow(31);
    //                 Cell R32cell1 = row.createCell(2);
    //                 if (record1.getR32_no_of_atms() != null) {
    //                     R32cell1.setCellValue(record1.getR32_no_of_atms().doubleValue());
    //                     R32cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R32cell1.setCellValue("");
    //                     R32cell1.setCellStyle(textStyle);
    //                 }

    //                 // R32 Col D
    //                 Cell R32cell2 = row.createCell(3);
    //                 if (record1.getR32_no_of_mini_atms() != null) {
    //                     R32cell2.setCellValue(record1.getR32_no_of_mini_atms().doubleValue());
    //                     R32cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R32cell2.setCellValue("");
    //                     R32cell2.setCellStyle(textStyle);
    //                 }

    //                 // R32 Col E
    //                 Cell R32cell3 = row.createCell(4);
    //                 if (record1.getR32_encashment_points() != null) {
    //                     R32cell3.setCellValue(record1.getR32_encashment_points().doubleValue());
    //                     R32cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R32cell3.setCellValue("");
    //                     R32cell3.setCellStyle(textStyle);
    //                 }
    //                 // R33 Col C
    //                 row = sheet.getRow(32);
    //                 Cell R33cell1 = row.createCell(2);
    //                 if (record1.getR33_no_of_atms() != null) {
    //                     R33cell1.setCellValue(record1.getR33_no_of_atms().doubleValue());
    //                     R33cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R33cell1.setCellValue("");
    //                     R33cell1.setCellStyle(textStyle);
    //                 }

    //                 // R33 Col D
    //                 Cell R33cell2 = row.createCell(3);
    //                 if (record1.getR33_no_of_mini_atms() != null) {
    //                     R33cell2.setCellValue(record1.getR33_no_of_mini_atms().doubleValue());
    //                     R33cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R33cell2.setCellValue("");
    //                     R33cell2.setCellStyle(textStyle);
    //                 }

    //                 // R33 Col E
    //                 Cell R33cell3 = row.createCell(4);
    //                 if (record1.getR33_encashment_points() != null) {
    //                     R33cell3.setCellValue(record1.getR33_encashment_points().doubleValue());
    //                     R33cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R33cell3.setCellValue("");
    //                     R33cell3.setCellStyle(textStyle);
    //                 }
    //                 // R34 Col C
    //                 row = sheet.getRow(33);
    //                 Cell R34cell1 = row.createCell(2);
    //                 if (record1.getR34_no_of_atms() != null) {
    //                     R34cell1.setCellValue(record1.getR34_no_of_atms().doubleValue());
    //                     R34cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R34cell1.setCellValue("");
    //                     R34cell1.setCellStyle(textStyle);
    //                 }

    //                 // R34 Col D
    //                 Cell R34cell2 = row.createCell(3);
    //                 if (record1.getR34_no_of_mini_atms() != null) {
    //                     R34cell2.setCellValue(record1.getR34_no_of_mini_atms().doubleValue());
    //                     R34cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R34cell2.setCellValue("");
    //                     R34cell2.setCellStyle(textStyle);
    //                 }

    //                 // R34 Col E
    //                 Cell R34cell3 = row.createCell(4);
    //                 if (record1.getR34_encashment_points() != null) {
    //                     R34cell3.setCellValue(record1.getR34_encashment_points().doubleValue());
    //                     R34cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R34cell3.setCellValue("");
    //                     R34cell3.setCellStyle(textStyle);
    //                 }
    //                 // TABLE 3
    //                 // R40 Col C
    //                 row = sheet.getRow(39);
    //                 Cell R40cell1 = row.createCell(2);
    //                 if (record2.getR40_opening_no_of_cards() != null) {
    //                     R40cell1.setCellValue(record2.getR40_opening_no_of_cards().doubleValue());
    //                     R40cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R40cell1.setCellValue("");
    //                     R40cell1.setCellStyle(textStyle);
    //                 }

    //                 // R40 Col D
    //                 Cell R40cell2 = row.createCell(3);
    //                 if (record2.getR40_no_of_cards_issued() != null) {
    //                     R40cell2.setCellValue(record2.getR40_no_of_cards_issued().doubleValue());
    //                     R40cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R40cell2.setCellValue("");
    //                     R40cell2.setCellStyle(textStyle);
    //                 }

    //                 // R40 Col E
    //                 Cell R40cell3 = row.createCell(4);
    //                 if (record2.getR40_no_cards_of_closed() != null) {
    //                     R40cell3.setCellValue(record2.getR40_no_cards_of_closed().doubleValue());
    //                     R40cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R40cell3.setCellValue("");
    //                     R40cell3.setCellStyle(textStyle);
    //                 }

    //                 // R41 Col C
    //                 row = sheet.getRow(40);
    //                 Cell R41cell1 = row.createCell(2);
    //                 if (record2.getR41_opening_no_of_cards() != null) {
    //                     R41cell1.setCellValue(record2.getR41_opening_no_of_cards().doubleValue());
    //                     R41cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R41cell1.setCellValue("");
    //                     R41cell1.setCellStyle(textStyle);
    //                 }

    //                 // R41 Col D
    //                 Cell R41cell2 = row.createCell(3);
    //                 if (record2.getR41_no_of_cards_issued() != null) {
    //                     R41cell2.setCellValue(record2.getR41_no_of_cards_issued().doubleValue());
    //                     R41cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R41cell2.setCellValue("");
    //                     R41cell2.setCellStyle(textStyle);
    //                 }

    //                 // R41 Col E
    //                 Cell R41cell3 = row.createCell(4);
    //                 if (record2.getR41_no_cards_of_closed() != null) {
    //                     R41cell3.setCellValue(record2.getR41_no_cards_of_closed().doubleValue());
    //                     R41cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R41cell3.setCellValue("");
    //                     R41cell3.setCellStyle(textStyle);
    //                 }

    //                 // R42 Col C
    //                 row = sheet.getRow(41);
    //                 Cell R42cell1 = row.createCell(2);
    //                 if (record2.getR42_opening_no_of_cards() != null) {
    //                     R42cell1.setCellValue(record2.getR42_opening_no_of_cards().doubleValue());
    //                     R42cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R42cell1.setCellValue("");
    //                     R42cell1.setCellStyle(textStyle);
    //                 }

    //                 // R42 Col D
    //                 Cell R42cell2 = row.createCell(3);
    //                 if (record2.getR42_no_of_cards_issued() != null) {
    //                     R42cell2.setCellValue(record2.getR42_no_of_cards_issued().doubleValue());
    //                     R42cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R42cell2.setCellValue("");
    //                     R42cell2.setCellStyle(textStyle);
    //                 }

    //                 // R42 Col E
    //                 Cell R42cell3 = row.createCell(4);
    //                 if (record2.getR42_no_cards_of_closed() != null) {
    //                     R42cell3.setCellValue(record2.getR42_no_cards_of_closed().doubleValue());
    //                     R42cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R42cell3.setCellValue("");
    //                     R42cell3.setCellStyle(textStyle);
    //                 }

    //                 // R43 Col C
    //                 row = sheet.getRow(42);
    //                 Cell R43cell1 = row.createCell(2);
    //                 if (record2.getR43_opening_no_of_cards() != null) {
    //                     R43cell1.setCellValue(record2.getR43_opening_no_of_cards().doubleValue());
    //                     R43cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R43cell1.setCellValue("");
    //                     R43cell1.setCellStyle(textStyle);
    //                 }

    //                 // R43 Col D
    //                 Cell R43cell2 = row.createCell(3);
    //                 if (record2.getR43_no_of_cards_issued() != null) {
    //                     R43cell2.setCellValue(record2.getR43_no_of_cards_issued().doubleValue());
    //                     R43cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R43cell2.setCellValue("");
    //                     R43cell2.setCellStyle(textStyle);
    //                 }

    //                 // R43 Col E
    //                 Cell R43cell3 = row.createCell(4);
    //                 if (record2.getR43_no_cards_of_closed() != null) {
    //                     R43cell3.setCellValue(record2.getR43_no_cards_of_closed().doubleValue());
    //                     R43cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R43cell3.setCellValue("");
    //                     R43cell3.setCellStyle(textStyle);
    //                 }
    //                 // R44 Col C
    //                 row = sheet.getRow(43);
    //                 Cell R44cell1 = row.createCell(2);
    //                 if (record2.getR44_opening_no_of_cards() != null) {
    //                     R44cell1.setCellValue(record2.getR44_opening_no_of_cards().doubleValue());
    //                     R44cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R44cell1.setCellValue("");
    //                     R44cell1.setCellStyle(textStyle);
    //                 }

    //                 // R44 Col D
    //                 Cell R44cell2 = row.createCell(3);
    //                 if (record2.getR44_no_of_cards_issued() != null) {
    //                     R44cell2.setCellValue(record2.getR44_no_of_cards_issued().doubleValue());
    //                     R44cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R44cell2.setCellValue("");
    //                     R44cell2.setCellStyle(textStyle);
    //                 }

    //                 // R44 Col E
    //                 Cell R44cell3 = row.createCell(4);
    //                 if (record2.getR44_no_cards_of_closed() != null) {
    //                     R44cell3.setCellValue(record2.getR44_no_cards_of_closed().doubleValue());
    //                     R44cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R44cell3.setCellValue("");
    //                     R44cell3.setCellStyle(textStyle);
    //                 }

    //                 // R45 Col C
    //                 row = sheet.getRow(44);
    //                 Cell R45cell1 = row.createCell(2);
    //                 if (record2.getR45_opening_no_of_cards() != null) {
    //                     R45cell1.setCellValue(record2.getR45_opening_no_of_cards().doubleValue());
    //                     R45cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R45cell1.setCellValue("");
    //                     R45cell1.setCellStyle(textStyle);
    //                 }

    //                 // R45 Col D
    //                 Cell R45cell2 = row.createCell(3);
    //                 if (record2.getR45_no_of_cards_issued() != null) {
    //                     R45cell2.setCellValue(record2.getR45_no_of_cards_issued().doubleValue());
    //                     R45cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R45cell2.setCellValue("");
    //                     R45cell2.setCellStyle(textStyle);
    //                 }

    //                 // R45 Col E
    //                 Cell R45cell3 = row.createCell(4);
    //                 if (record2.getR45_no_cards_of_closed() != null) {
    //                     R45cell3.setCellValue(record2.getR45_no_cards_of_closed().doubleValue());
    //                     R45cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R45cell3.setCellValue("");
    //                     R45cell3.setCellStyle(textStyle);
    //                 }

    //                 // R46 Col C
    //                 row = sheet.getRow(45);
    //                 Cell R46cell1 = row.createCell(2);
    //                 if (record2.getR46_opening_no_of_cards() != null) {
    //                     R46cell1.setCellValue(record2.getR46_opening_no_of_cards().doubleValue());
    //                     R46cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R46cell1.setCellValue("");
    //                     R46cell1.setCellStyle(textStyle);
    //                 }

    //                 // R46 Col D
    //                 Cell R46cell2 = row.createCell(3);
    //                 if (record2.getR46_no_of_cards_issued() != null) {
    //                     R46cell2.setCellValue(record2.getR46_no_of_cards_issued().doubleValue());
    //                     R46cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R46cell2.setCellValue("");
    //                     R46cell2.setCellStyle(textStyle);
    //                 }

    //                 // R46 Col E
    //                 Cell R46cell3 = row.createCell(4);
    //                 if (record2.getR46_no_cards_of_closed() != null) {
    //                     R46cell3.setCellValue(record2.getR46_no_cards_of_closed().doubleValue());
    //                     R46cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R46cell3.setCellValue("");
    //                     R46cell3.setCellStyle(textStyle);
    //                 }

    //                 // R47 Col C
    //                 row = sheet.getRow(46);
    //                 Cell R47cell1 = row.createCell(2);
    //                 if (record2.getR47_opening_no_of_cards() != null) {
    //                     R47cell1.setCellValue(record2.getR47_opening_no_of_cards().doubleValue());
    //                     R47cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R47cell1.setCellValue("");
    //                     R47cell1.setCellStyle(textStyle);
    //                 }

    //                 // R47 Col D
    //                 Cell R47cell2 = row.createCell(3);
    //                 if (record2.getR47_no_of_cards_issued() != null) {
    //                     R47cell2.setCellValue(record2.getR47_no_of_cards_issued().doubleValue());
    //                     R47cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R47cell2.setCellValue("");
    //                     R47cell2.setCellStyle(textStyle);
    //                 }

    //                 // R47 Col E
    //                 Cell R47cell3 = row.createCell(4);
    //                 if (record2.getR47_no_cards_of_closed() != null) {
    //                     R47cell3.setCellValue(record2.getR47_no_cards_of_closed().doubleValue());
    //                     R47cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R47cell3.setCellValue("");
    //                     R47cell3.setCellStyle(textStyle);
    //                 }

    //                 // R48 Col C
    //                 row = sheet.getRow(47);
    //                 Cell R48cell1 = row.createCell(2);
    //                 if (record2.getR48_opening_no_of_cards() != null) {
    //                     R48cell1.setCellValue(record2.getR48_opening_no_of_cards().doubleValue());
    //                     R48cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R48cell1.setCellValue("");
    //                     R48cell1.setCellStyle(textStyle);
    //                 }

    //                 // R48 Col D
    //                 Cell R48cell2 = row.createCell(3);
    //                 if (record2.getR48_no_of_cards_issued() != null) {
    //                     R48cell2.setCellValue(record2.getR48_no_of_cards_issued().doubleValue());
    //                     R48cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R48cell2.setCellValue("");
    //                     R48cell2.setCellStyle(textStyle);
    //                 }

    //                 // R48 Col E
    //                 Cell R48cell3 = row.createCell(4);
    //                 if (record2.getR48_no_cards_of_closed() != null) {
    //                     R48cell3.setCellValue(record2.getR48_no_cards_of_closed().doubleValue());
    //                     R48cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R48cell3.setCellValue("");
    //                     R48cell3.setCellStyle(textStyle);
    //                 }

    //                 // R49 Col C
    //                 row = sheet.getRow(48);
    //                 Cell R49cell1 = row.createCell(2);
    //                 if (record2.getR49_opening_no_of_cards() != null) {
    //                     R49cell1.setCellValue(record2.getR49_opening_no_of_cards().doubleValue());
    //                     R49cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R49cell1.setCellValue("");
    //                     R49cell1.setCellStyle(textStyle);
    //                 }

    //                 // R49 Col D
    //                 Cell R49cell2 = row.createCell(3);
    //                 if (record2.getR49_no_of_cards_issued() != null) {
    //                     R49cell2.setCellValue(record2.getR49_no_of_cards_issued().doubleValue());
    //                     R49cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R49cell2.setCellValue("");
    //                     R49cell2.setCellStyle(textStyle);
    //                 }

    //                 // R49 Col E
    //                 Cell R49cell3 = row.createCell(4);
    //                 if (record2.getR49_no_cards_of_closed() != null) {
    //                     R49cell3.setCellValue(record2.getR49_no_cards_of_closed().doubleValue());
    //                     R49cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R49cell3.setCellValue("");
    //                     R49cell3.setCellStyle(textStyle);
    //                 }

    //                 // TABLE 4
    //                 // R55 Col C
    //                 row = sheet.getRow(54);
    //                 Cell R55cell1 = row.createCell(2);
    //                 if (record3.getR55_opening_no_of_cards() != null) {
    //                     R55cell1.setCellValue(record3.getR55_opening_no_of_cards().doubleValue());
    //                     R55cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R55cell1.setCellValue("");
    //                     R55cell1.setCellStyle(textStyle);
    //                 }

    //                 // R55 Col D
    //                 Cell R55cell2 = row.createCell(3);
    //                 if (record3.getR55_no_of_cards_issued() != null) {
    //                     R55cell2.setCellValue(record3.getR55_no_of_cards_issued().doubleValue());
    //                     R55cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R55cell2.setCellValue("");
    //                     R55cell2.setCellStyle(textStyle);
    //                 }

    //                 // R55 Col E
    //                 Cell R55cell3 = row.createCell(4);
    //                 if (record3.getR55_no_cards_of_closed() != null) {
    //                     R55cell3.setCellValue(record3.getR55_no_cards_of_closed().doubleValue());
    //                     R55cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R55cell3.setCellValue("");
    //                     R55cell3.setCellStyle(textStyle);
    //                 }

    //                 // R56 Col C
    //                 row = sheet.getRow(55);
    //                 Cell R56cell1 = row.createCell(2);
    //                 if (record3.getR56_opening_no_of_cards() != null) {
    //                     R56cell1.setCellValue(record3.getR56_opening_no_of_cards().doubleValue());
    //                     R56cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R56cell1.setCellValue("");
    //                     R56cell1.setCellStyle(textStyle);
    //                 }

    //                 // R56 Col D
    //                 Cell R56cell2 = row.createCell(3);
    //                 if (record3.getR56_no_of_cards_issued() != null) {
    //                     R56cell2.setCellValue(record3.getR56_no_of_cards_issued().doubleValue());
    //                     R56cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R56cell2.setCellValue("");
    //                     R56cell2.setCellStyle(textStyle);
    //                 }

    //                 // R56 Col E
    //                 Cell R56cell3 = row.createCell(4);
    //                 if (record3.getR56_no_cards_of_closed() != null) {
    //                     R56cell3.setCellValue(record3.getR56_no_cards_of_closed().doubleValue());
    //                     R56cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R56cell3.setCellValue("");
    //                     R56cell3.setCellStyle(textStyle);
    //                 }

    //                 // R57 Col C
    //                 row = sheet.getRow(56);
    //                 Cell R57cell1 = row.createCell(2);
    //                 if (record3.getR57_opening_no_of_cards() != null) {
    //                     R57cell1.setCellValue(record3.getR57_opening_no_of_cards().doubleValue());
    //                     R57cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R57cell1.setCellValue("");
    //                     R57cell1.setCellStyle(textStyle);
    //                 }

    //                 // R57 Col D
    //                 Cell R57cell2 = row.createCell(3);
    //                 if (record3.getR57_no_of_cards_issued() != null) {
    //                     R57cell2.setCellValue(record3.getR57_no_of_cards_issued().doubleValue());
    //                     R57cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R57cell2.setCellValue("");
    //                     R57cell2.setCellStyle(textStyle);
    //                 }

    //                 // R57 Col E
    //                 Cell R57cell3 = row.createCell(4);
    //                 if (record3.getR57_no_cards_of_closed() != null) {
    //                     R57cell3.setCellValue(record3.getR57_no_cards_of_closed().doubleValue());
    //                     R57cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R57cell3.setCellValue("");
    //                     R57cell3.setCellStyle(textStyle);
    //                 }

    //                 // R58 Col C
    //                 row = sheet.getRow(57);
    //                 Cell R58cell1 = row.createCell(2);
    //                 if (record3.getR58_opening_no_of_cards() != null) {
    //                     R58cell1.setCellValue(record3.getR58_opening_no_of_cards().doubleValue());
    //                     R58cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R58cell1.setCellValue("");
    //                     R58cell1.setCellStyle(textStyle);
    //                 }

    //                 // R58 Col D
    //                 Cell R58cell2 = row.createCell(3);
    //                 if (record3.getR58_no_of_cards_issued() != null) {
    //                     R58cell2.setCellValue(record3.getR58_no_of_cards_issued().doubleValue());
    //                     R58cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R58cell2.setCellValue("");
    //                     R58cell2.setCellStyle(textStyle);
    //                 }

    //                 // R58 Col E
    //                 Cell R58cell3 = row.createCell(4);
    //                 if (record3.getR58_no_cards_of_closed() != null) {
    //                     R58cell3.setCellValue(record3.getR58_no_cards_of_closed().doubleValue());
    //                     R58cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R58cell3.setCellValue("");
    //                     R58cell3.setCellStyle(textStyle);
    //                 }

    //                 // R59 Col C
    //                 row = sheet.getRow(58);
    //                 Cell R59cell1 = row.createCell(2);
    //                 if (record3.getR59_opening_no_of_cards() != null) {
    //                     R59cell1.setCellValue(record3.getR59_opening_no_of_cards().doubleValue());
    //                     R59cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R59cell1.setCellValue("");
    //                     R59cell1.setCellStyle(textStyle);
    //                 }

    //                 // R59 Col D
    //                 Cell R59cell2 = row.createCell(3);
    //                 if (record3.getR59_no_of_cards_issued() != null) {
    //                     R59cell2.setCellValue(record3.getR59_no_of_cards_issued().doubleValue());
    //                     R59cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R59cell2.setCellValue("");
    //                     R59cell2.setCellStyle(textStyle);
    //                 }

    //                 // R59 Col E
    //                 Cell R59cell3 = row.createCell(4);
    //                 if (record3.getR59_no_cards_of_closed() != null) {
    //                     R59cell3.setCellValue(record3.getR59_no_cards_of_closed().doubleValue());
    //                     R59cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R59cell3.setCellValue("");
    //                     R59cell3.setCellStyle(textStyle);
    //                 }

    //                 // R60 Col C
    //                 row = sheet.getRow(59);
    //                 Cell R60cell1 = row.createCell(2);
    //                 if (record3.getR60_opening_no_of_cards() != null) {
    //                     R60cell1.setCellValue(record3.getR60_opening_no_of_cards().doubleValue());
    //                     R60cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R60cell1.setCellValue("");
    //                     R60cell1.setCellStyle(textStyle);
    //                 }

    //                 // R60 Col D
    //                 Cell R60cell2 = row.createCell(3);
    //                 if (record3.getR60_no_of_cards_issued() != null) {
    //                     R60cell2.setCellValue(record3.getR60_no_of_cards_issued().doubleValue());
    //                     R60cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R60cell2.setCellValue("");
    //                     R60cell2.setCellStyle(textStyle);
    //                 }
    //                 // R60 Col E
    //                 Cell R60cell3 = row.createCell(4);
    //                 if (record3.getR60_no_cards_of_closed() != null) {
    //                     R60cell3.setCellValue(record3.getR60_no_cards_of_closed().doubleValue());
    //                     R60cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R60cell3.setCellValue("");
    //                     R60cell3.setCellStyle(textStyle);
    //                 }

    //                 // R61 Col C
    //                 row = sheet.getRow(60);
    //                 Cell R61cell1 = row.createCell(2);
    //                 if (record3.getR61_opening_no_of_cards() != null) {
    //                     R61cell1.setCellValue(record3.getR61_opening_no_of_cards().doubleValue());
    //                     R61cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R61cell1.setCellValue("");
    //                     R61cell1.setCellStyle(textStyle);
    //                 }

    //                 // R61 Col D
    //                 Cell R61cell2 = row.createCell(3);
    //                 if (record3.getR61_no_of_cards_issued() != null) {
    //                     R61cell2.setCellValue(record3.getR61_no_of_cards_issued().doubleValue());
    //                     R61cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R61cell2.setCellValue("");
    //                     R61cell2.setCellStyle(textStyle);
    //                 }
    //                 // R61 Col E
    //                 Cell R61cell3 = row.createCell(4);
    //                 if (record3.getR61_no_cards_of_closed() != null) {
    //                     R61cell3.setCellValue(record3.getR61_no_cards_of_closed().doubleValue());
    //                     R61cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R61cell3.setCellValue("");
    //                     R61cell3.setCellStyle(textStyle);
    //                 }

    //                 // R62 Col C
    //                 row = sheet.getRow(61);
    //                 Cell R62cell1 = row.createCell(2);
    //                 if (record3.getR62_opening_no_of_cards() != null) {
    //                     R62cell1.setCellValue(record3.getR62_opening_no_of_cards().doubleValue());
    //                     R62cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R62cell1.setCellValue("");
    //                     R62cell1.setCellStyle(textStyle);
    //                 }

    //                 // R62 Col D
    //                 Cell R62cell2 = row.createCell(3);
    //                 if (record3.getR62_no_of_cards_issued() != null) {
    //                     R62cell2.setCellValue(record3.getR62_no_of_cards_issued().doubleValue());
    //                     R62cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R62cell2.setCellValue("");
    //                     R62cell2.setCellStyle(textStyle);
    //                 }
    //                 // R62 Col E
    //                 Cell R62cell3 = row.createCell(4);
    //                 if (record3.getR62_no_cards_of_closed() != null) {
    //                     R62cell3.setCellValue(record3.getR62_no_cards_of_closed().doubleValue());
    //                     R62cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R62cell3.setCellValue("");
    //                     R62cell3.setCellStyle(textStyle);
    //                 }

    //                 // R63 Col C
    //                 row = sheet.getRow(62);
    //                 Cell R63cell1 = row.createCell(2);
    //                 if (record3.getR63_opening_no_of_cards() != null) {
    //                     R63cell1.setCellValue(record3.getR63_opening_no_of_cards().doubleValue());
    //                     R63cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R63cell1.setCellValue("");
    //                     R63cell1.setCellStyle(textStyle);
    //                 }

    //                 // R63 Col D
    //                 Cell R63cell2 = row.createCell(3);
    //                 if (record3.getR63_no_of_cards_issued() != null) {
    //                     R63cell2.setCellValue(record3.getR63_no_of_cards_issued().doubleValue());
    //                     R63cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R63cell2.setCellValue("");
    //                     R63cell2.setCellStyle(textStyle);
    //                 }
    //                 // R63 Col E
    //                 Cell R63cell3 = row.createCell(4);
    //                 if (record3.getR63_no_cards_of_closed() != null) {
    //                     R63cell3.setCellValue(record3.getR63_no_cards_of_closed().doubleValue());
    //                     R63cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R63cell3.setCellValue("");
    //                     R63cell3.setCellStyle(textStyle);
    //                 }

    //                 // R64 Col C
    //                 row = sheet.getRow(63);
    //                 Cell R64cell1 = row.createCell(2);
    //                 if (record3.getR64_opening_no_of_cards() != null) {
    //                     R64cell1.setCellValue(record3.getR64_opening_no_of_cards().doubleValue());
    //                     R64cell1.setCellStyle(numberStyle);
    //                 } else {
    //                     R64cell1.setCellValue("");
    //                     R64cell1.setCellStyle(textStyle);
    //                 }

    //                 // R64 Col D
    //                 Cell R64cell2 = row.createCell(3);
    //                 if (record3.getR64_no_of_cards_issued() != null) {
    //                     R64cell2.setCellValue(record3.getR64_no_of_cards_issued().doubleValue());
    //                     R64cell2.setCellStyle(numberStyle);
    //                 } else {
    //                     R64cell2.setCellValue("");
    //                     R64cell2.setCellStyle(textStyle);
    //                 }
    //                 // R64 Col E
    //                 Cell R64cell3 = row.createCell(4);
    //                 if (record3.getR64_no_cards_of_closed() != null) {
    //                     R64cell3.setCellValue(record3.getR64_no_cards_of_closed().doubleValue());
    //                     R64cell3.setCellStyle(numberStyle);
    //                 } else {
    //                     R64cell3.setCellValue("");
    //                     R64cell3.setCellStyle(textStyle);
    //                 }
    //             }
    //             workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
    //         } else {

    //         }
    //         // Write the final workbook content to the in-memory stream.
    //         workbook.write(out);
    //         logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
    //         return out.toByteArray();
    //     }
    // }

}
