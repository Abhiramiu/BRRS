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
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;
import com.bornfire.brrs.entities.PL_SCHS_Archival_Detail_Entity;
import com.bornfire.brrs.entities.PL_SCHS_Archival_Summary_Entity;
import com.bornfire.brrs.entities.PL_SCHS_Detail_Entity;
import com.bornfire.brrs.entities.PL_SCHS_Manual_Archival_Summary_Entity;
import com.bornfire.brrs.entities.PL_SCHS_Manual_Summary_Entity;
import com.bornfire.brrs.entities.PL_SCHS_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_PL_SCHS_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_PL_SCHS_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_PL_SCHS_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_PL_SCHS_Manual_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_PL_SCHS_Manual_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_PL_SCHS_Summary_Repo;
import com.bornfire.brrs.entities.M_FAS_Manual_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_FAS_Manual_Summary_Entity;

@Component
@Service

public class BRRS_PL_SCHS_ReportService {
    private static final Logger logger = LoggerFactory.getLogger(BRRS_PL_SCHS_ReportService.class);

    @Autowired
    private Environment env;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    BRRS_PL_SCHS_Summary_Repo PL_SCHS_summary_repo;

    @Autowired
    BRRS_PL_SCHS_Archival_Summary_Repo PL_SCHS_Archival_Summary_Repo;

    @Autowired
    BRRS_PL_SCHS_Detail_Repo PL_SCHS_detail_repo;

    @Autowired
    BRRS_PL_SCHS_Archival_Detail_Repo PL_SCHS_Archival_Detail_Repo;

    @Autowired
    BRRS_PL_SCHS_Manual_Summary_Repo PL_SCHS_Manual_Summary_Repo;

    @Autowired
    BRRS_PL_SCHS_Manual_Archival_Summary_Repo PL_SCHS_Manual_Archival_Summary_Repo;

    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

    public ModelAndView getPL_SCHSView(String reportId, String fromdate, String todate,
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
                List<PL_SCHS_Archival_Summary_Entity> T1Master = PL_SCHS_Archival_Summary_Repo
                        .getdatabydateListarchival(d1, version);
                List<PL_SCHS_Manual_Archival_Summary_Entity> T2Master = PL_SCHS_Manual_Archival_Summary_Repo
                        .getdatabydateListarchival(d1, version);
                mv.addObject("reportsummary", T1Master);
                mv.addObject("reportsummary1", T2Master);
                System.out.println("T1Master Size " + T1Master.size());
                System.out.println("T2Master Size " + T2Master.size());

            }

            // ---------- CASE 3: NORMAL ----------
            else {
                List<PL_SCHS_Summary_Entity> T1Master = PL_SCHS_summary_repo
                        .getdatabydateList(dateformat.parse(todate));
                List<PL_SCHS_Manual_Summary_Entity> T2Master = PL_SCHS_Manual_Summary_Repo
                        .getdatabydateList(dateformat.parse(todate));

                mv.addObject("reportsummary", T1Master);
                mv.addObject("reportsummary1", T2Master);
                System.out.println("T1Master Size " + T1Master.size());
                System.out.println("T2Master Size " + T2Master.size());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        mv.setViewName("BRRS/PL_SCHS");
        mv.addObject("displaymode", "summary");
        System.out.println("View set to: " + mv.getViewName());
        return mv;
    }

    public void updateReport(PL_SCHS_Manual_Summary_Entity updatedEntity) {

        PL_SCHS_Manual_Summary_Entity existing = PL_SCHS_Manual_Summary_Repo.findById(updatedEntity.getReport_date())
                .orElseThrow(() -> new RuntimeException("Record not found for REPORT_DATE: "
                        + updatedEntity.getReport_date()));

        int[] rows = {
                12,
                18, 19, 21, 22, 23, 24, 25, 26, 27, 28, 29,
                42,
                54,
                61
        };

        String[] fields = {
                "intrest_div",
                "other_income",
                "operating_expenses",
                "fig_bal_sheet",
                "fig_bal_sheet_bwp",
                "amt_statement_adj",
                "amt_statement_adj_bwp",
                "net_amt",
                "net_amt_bwp",
                "bal_sub",
                "bal_sub_bwp",
                "bal_sub_diaries",
                "bal_sub_diaries_bwp"
        };

        try {
            for (int i : rows) {
                for (String field : fields) {

                    String getterName = "getR" + i + "_" + field;
                    String setterName = "setR" + i + "_" + field;

                    try {
                        Method getter = PL_SCHS_Manual_Summary_Entity.class
                                .getMethod(getterName);
                        Method setter = PL_SCHS_Manual_Summary_Entity.class
                                .getMethod(setterName, getter.getReturnType());

                        Object newValue = getter.invoke(updatedEntity);
                        setter.invoke(existing, newValue);

                    } catch (NoSuchMethodException e) {
                        // Field not applicable for this row → skip safely
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating report fields", e);
        }

        PL_SCHS_Manual_Summary_Repo.save(existing);
    }

    public ModelAndView getPL_SCHScurrentDtl(String reportId, String fromdate, String todate, String currency,
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
                List<PL_SCHS_Archival_Detail_Entity> T1Dt1;
                if (reportLabel != null && reportAddlCriteria1 != null) {
                    T1Dt1 = PL_SCHS_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel,
                            reportAddlCriteria1,
                            parsedDate, version);
                } else {
                    T1Dt1 = PL_SCHS_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
                }

                mv.addObject("reportdetails", T1Dt1);
                mv.addObject("reportmaster12", T1Dt1);
                System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

            } else {
                // ?? Current branch
                List<PL_SCHS_Detail_Entity> T1Dt1;

                if (reportLabel != null && reportAddlCriteria1 != null) {
                    T1Dt1 = PL_SCHS_detail_repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1,
                            parsedDate);
                } else {
                    T1Dt1 = PL_SCHS_detail_repo.getdatabydateList(parsedDate);
                    totalPages = PL_SCHS_detail_repo.getdatacount(parsedDate);
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

        mv.setViewName("BRRS/PL_SCHS");
        mv.addObject("displaymode", "Details");
        mv.addObject("currentPage", currentPage);
        System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
        mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
        mv.addObject("reportsflag", "reportsflag");
        mv.addObject("menu", reportId);
        return mv;
    }

    public byte[] getPL_SCHSExcel(String filename, String reportId, String fromdate, String todate,
            String currency,
            String dtltype, String type, String version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");
        System.out.println(type);
        System.out.println(version);
        Date reportDate = dateformat.parse(todate);

        // ARCHIVAL check
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
            logger.info("Service: Generating ARCHIVAL report for version {}", version);
            return getExcelPL_SCHSARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);

        }

        List<PL_SCHS_Summary_Entity> dataList = PL_SCHS_summary_repo
                .getdatabydateList(dateformat.parse(todate));
        List<PL_SCHS_Manual_Summary_Entity> dataList1 = PL_SCHS_Manual_Summary_Repo
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
            int startRow = 8;

            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {

                    PL_SCHS_Summary_Entity record = dataList.get(i);
                    PL_SCHS_Manual_Summary_Entity record1 = dataList1.get(i);
                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }
                    Cell R9Cell1 = row.createCell(3);
                    if (record.getR9_fig_bal_sheet() != null) {
                        R9Cell1.setCellValue(record.getR9_fig_bal_sheet().doubleValue());
                        R9Cell1.setCellStyle(numberStyle);
                    } else {
                        R9Cell1.setCellValue("");
                        R9Cell1.setCellStyle(textStyle);
                    }

                    // R9 Col E
                    Cell R9Cell2 = row.createCell(4);
                    if (record.getR9_fig_bal_sheet_bwp() != null) {
                        R9Cell2.setCellValue(record.getR9_fig_bal_sheet_bwp().doubleValue());
                        R9Cell2.setCellStyle(numberStyle);
                    } else {
                        R9Cell2.setCellValue("");
                        R9Cell2.setCellStyle(textStyle);
                    }

                    // R9 Col F
                    Cell R9Cell3 = row.createCell(5);
                    if (record.getR9_amt_statement_adj() != null) {
                        R9Cell3.setCellValue(record.getR9_amt_statement_adj().doubleValue());
                        R9Cell3.setCellStyle(numberStyle);
                    } else {
                        R9Cell3.setCellValue("");
                        R9Cell3.setCellStyle(textStyle);
                    }
                    // R9 Col G
                    Cell R9Cell4 = row.createCell(6);
                    if (record.getR9_amt_statement_adj_bwp() != null) {
                        R9Cell4.setCellValue(record.getR9_amt_statement_adj_bwp().doubleValue());
                        R9Cell4.setCellStyle(numberStyle);
                    } else {
                        R9Cell4.setCellValue("");
                        R9Cell4.setCellStyle(textStyle);
                    }
                    // // R9 Col H
                    // Cell R9Cell5 = row.createCell(7);
                    // if (record.getR9_net_amt() != null) {
                    // R9Cell5.setCellValue(record.getR9_net_amt().doubleValue());
                    // R9Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R9Cell5.setCellValue("");
                    // R9Cell5.setCellStyle(textStyle);
                    // }
                    // R9 Col I
                    Cell R9Cell6 = row.createCell(8);
                    if (record.getR9_net_amt_bwp() != null) {
                        R9Cell6.setCellValue(record.getR9_net_amt_bwp().doubleValue());
                        R9Cell6.setCellStyle(numberStyle);
                    } else {
                        R9Cell6.setCellValue("");
                        R9Cell6.setCellStyle(textStyle);
                    }
                    // R9 Col J
                    Cell R9Cell7 = row.createCell(9);
                    if (record.getR9_bal_sub() != null) {
                        R9Cell7.setCellValue(record.getR9_bal_sub().doubleValue());
                        R9Cell7.setCellStyle(numberStyle);
                    } else {
                        R9Cell7.setCellValue("");
                        R9Cell7.setCellStyle(textStyle);
                    }
                    // R9 Col K
                    Cell R9Cell8 = row.createCell(10);
                    if (record.getR9_bal_sub_bwp() != null) {
                        R9Cell8.setCellValue(record.getR9_bal_sub_bwp().doubleValue());
                        R9Cell8.setCellStyle(numberStyle);
                    } else {
                        R9Cell8.setCellValue("");
                        R9Cell8.setCellStyle(textStyle);
                    }
                    // R9 Col L
                    Cell R9Cell9 = row.createCell(11);
                    if (record.getR9_bal_sub_diaries() != null) {
                        R9Cell9.setCellValue(record.getR9_bal_sub_diaries().doubleValue());
                        R9Cell9.setCellStyle(numberStyle);
                    } else {
                        R9Cell9.setCellValue("");
                        R9Cell9.setCellStyle(textStyle);
                    }
                    // R9 Col M
                    Cell R9Cell10 = row.createCell(12);
                    if (record.getR9_bal_sub_diaries_bwp() != null) {
                        R9Cell10.setCellValue(record.getR9_bal_sub_diaries_bwp().doubleValue());
                        R9Cell10.setCellStyle(numberStyle);
                    } else {
                        R9Cell10.setCellValue("");
                        R9Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(9);
                    Cell R10Cell1 = row.createCell(3);
                    if (record.getR10_fig_bal_sheet() != null) {
                        R10Cell1.setCellValue(record.getR10_fig_bal_sheet().doubleValue());
                        R10Cell1.setCellStyle(numberStyle);
                    } else {
                        R10Cell1.setCellValue("");
                        R10Cell1.setCellStyle(textStyle);
                    }

                    // R10 Col E
                    Cell R10Cell2 = row.createCell(4);
                    if (record.getR10_fig_bal_sheet_bwp() != null) {
                        R10Cell2.setCellValue(record.getR10_fig_bal_sheet_bwp().doubleValue());
                        R10Cell2.setCellStyle(numberStyle);
                    } else {
                        R10Cell2.setCellValue("");
                        R10Cell2.setCellStyle(textStyle);
                    }

                    // R10 Col F
                    Cell R10Cell3 = row.createCell(5);
                    if (record.getR10_amt_statement_adj() != null) {
                        R10Cell3.setCellValue(record.getR10_amt_statement_adj().doubleValue());
                        R10Cell3.setCellStyle(numberStyle);
                    } else {
                        R10Cell3.setCellValue("");
                        R10Cell3.setCellStyle(textStyle);
                    }
                    // R10 Col G
                    Cell R10Cell4 = row.createCell(6);
                    if (record.getR10_amt_statement_adj_bwp() != null) {
                        R10Cell4.setCellValue(record.getR10_amt_statement_adj_bwp().doubleValue());
                        R10Cell4.setCellStyle(numberStyle);
                    } else {
                        R10Cell4.setCellValue("");
                        R10Cell4.setCellStyle(textStyle);
                    }
                    // R10 Col H
                    // Cell R10Cell5 = row.createCell(7);
                    // if (record.getR10_net_amt() != null) {
                    // R10Cell5.setCellValue(record.getR10_net_amt().doubleValue());
                    // R10Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R10Cell5.setCellValue("");
                    // R10Cell5.setCellStyle(textStyle);
                    // }
                    // R10 Col I
                    Cell R10Cell6 = row.createCell(8);
                    if (record.getR10_net_amt_bwp() != null) {
                        R10Cell6.setCellValue(record.getR10_net_amt_bwp().doubleValue());
                        R10Cell6.setCellStyle(numberStyle);
                    } else {
                        R10Cell6.setCellValue("");
                        R10Cell6.setCellStyle(textStyle);
                    }
                    // R10 Col J
                    Cell R10Cell7 = row.createCell(9);
                    if (record.getR10_bal_sub() != null) {
                        R10Cell7.setCellValue(record.getR10_bal_sub().doubleValue());
                        R10Cell7.setCellStyle(numberStyle);
                    } else {
                        R10Cell7.setCellValue("");
                        R10Cell7.setCellStyle(textStyle);
                    }
                    // R10 Col K
                    Cell R10Cell8 = row.createCell(10);
                    if (record.getR10_bal_sub_bwp() != null) {
                        R10Cell8.setCellValue(record.getR10_bal_sub_bwp().doubleValue());
                        R10Cell8.setCellStyle(numberStyle);
                    } else {
                        R10Cell8.setCellValue("");
                        R10Cell8.setCellStyle(textStyle);
                    }
                    // R10 Col L
                    Cell R10Cell9 = row.createCell(11);
                    if (record.getR10_bal_sub_diaries() != null) {
                        R10Cell9.setCellValue(record.getR10_bal_sub_diaries().doubleValue());
                        R10Cell9.setCellStyle(numberStyle);
                    } else {
                        R10Cell9.setCellValue("");
                        R10Cell9.setCellStyle(textStyle);
                    }
                    // R10 Col M
                    Cell R10Cell10 = row.createCell(12);
                    if (record.getR10_bal_sub_diaries_bwp() != null) {
                        R10Cell10.setCellValue(record.getR10_bal_sub_diaries_bwp().doubleValue());
                        R10Cell10.setCellStyle(numberStyle);
                    } else {
                        R10Cell10.setCellValue("");
                        R10Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(10);
                    Cell R11Cell1 = row.createCell(3);
                    if (record.getR11_fig_bal_sheet() != null) {
                        R11Cell1.setCellValue(record.getR11_fig_bal_sheet().doubleValue());
                        R11Cell1.setCellStyle(numberStyle);
                    } else {
                        R11Cell1.setCellValue("");
                        R11Cell1.setCellStyle(textStyle);
                    }

                    // R11 Col E
                    Cell R11Cell2 = row.createCell(4);
                    if (record.getR11_fig_bal_sheet_bwp() != null) {
                        R11Cell2.setCellValue(record.getR11_fig_bal_sheet_bwp().doubleValue());
                        R11Cell2.setCellStyle(numberStyle);
                    } else {
                        R11Cell2.setCellValue("");
                        R11Cell2.setCellStyle(textStyle);
                    }

                    // R11 Col F
                    Cell R11Cell3 = row.createCell(5);
                    if (record.getR11_amt_statement_adj() != null) {
                        R11Cell3.setCellValue(record.getR11_amt_statement_adj().doubleValue());
                        R11Cell3.setCellStyle(numberStyle);
                    } else {
                        R11Cell3.setCellValue("");
                        R11Cell3.setCellStyle(textStyle);
                    }
                    // R11 Col G
                    Cell R11Cell4 = row.createCell(6);
                    if (record.getR11_amt_statement_adj_bwp() != null) {
                        R11Cell4.setCellValue(record.getR11_amt_statement_adj_bwp().doubleValue());
                        R11Cell4.setCellStyle(numberStyle);
                    } else {
                        R11Cell4.setCellValue("");
                        R11Cell4.setCellStyle(textStyle);
                    }
                    // // R11 Col H
                    // Cell R11Cell5 = row.createCell(7);
                    // if (record.getR11_net_amt() != null) {
                    // R11Cell5.setCellValue(record.getR11_net_amt().doubleValue());
                    // R11Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R11Cell5.setCellValue("");
                    // R11Cell5.setCellStyle(textStyle);
                    // }
                    // R11 Col I
                    Cell R11Cell6 = row.createCell(8);
                    if (record.getR11_net_amt_bwp() != null) {
                        R11Cell6.setCellValue(record.getR11_net_amt_bwp().doubleValue());
                        R11Cell6.setCellStyle(numberStyle);
                    } else {
                        R11Cell6.setCellValue("");
                        R11Cell6.setCellStyle(textStyle);
                    }
                    // R11 Col J
                    Cell R11Cell7 = row.createCell(9);
                    if (record.getR11_bal_sub() != null) {
                        R11Cell7.setCellValue(record.getR11_bal_sub().doubleValue());
                        R11Cell7.setCellStyle(numberStyle);
                    } else {
                        R11Cell7.setCellValue("");
                        R11Cell7.setCellStyle(textStyle);
                    }
                    // R11 Col K
                    Cell R11Cell8 = row.createCell(10);
                    if (record.getR11_bal_sub_bwp() != null) {
                        R11Cell8.setCellValue(record.getR11_bal_sub_bwp().doubleValue());
                        R11Cell8.setCellStyle(numberStyle);
                    } else {
                        R11Cell8.setCellValue("");
                        R11Cell8.setCellStyle(textStyle);
                    }
                    // R11 Col L
                    Cell R11Cell9 = row.createCell(11);
                    if (record.getR11_bal_sub_diaries() != null) {
                        R11Cell9.setCellValue(record.getR11_bal_sub_diaries().doubleValue());
                        R11Cell9.setCellStyle(numberStyle);
                    } else {
                        R11Cell9.setCellValue("");
                        R11Cell9.setCellStyle(textStyle);
                    }
                    // R11 Col M
                    Cell R11Cell10 = row.createCell(12);
                    if (record.getR11_bal_sub_diaries_bwp() != null) {
                        R11Cell10.setCellValue(record.getR11_bal_sub_diaries_bwp().doubleValue());
                        R11Cell10.setCellStyle(numberStyle);
                    } else {
                        R11Cell10.setCellValue("");
                        R11Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(11);
                    Cell R12Cell1 = row.createCell(3);
                    if (record1.getR12_fig_bal_sheet() != null) {
                        R12Cell1.setCellValue(record1.getR12_fig_bal_sheet().doubleValue());
                        R12Cell1.setCellStyle(numberStyle);
                    } else {
                        R12Cell1.setCellValue("");
                        R12Cell1.setCellStyle(textStyle);
                    }

                    // R12 Col E
                    Cell R12Cell2 = row.createCell(4);
                    if (record1.getR12_fig_bal_sheet_bwp() != null) {
                        R12Cell2.setCellValue(record1.getR12_fig_bal_sheet_bwp().doubleValue());
                        R12Cell2.setCellStyle(numberStyle);
                    } else {
                        R12Cell2.setCellValue("");
                        R12Cell2.setCellStyle(textStyle);
                    }

                    // R12 Col F
                    Cell R12Cell3 = row.createCell(5);
                    if (record1.getR12_amt_statement_adj() != null) {
                        R12Cell3.setCellValue(record1.getR12_amt_statement_adj().doubleValue());
                        R12Cell3.setCellStyle(numberStyle);
                    } else {
                        R12Cell3.setCellValue("");
                        R12Cell3.setCellStyle(textStyle);
                    }
                    // R12 Col G
                    Cell R12Cell4 = row.createCell(6);
                    if (record1.getR12_amt_statement_adj_bwp() != null) {
                        R12Cell4.setCellValue(record1.getR12_amt_statement_adj_bwp().doubleValue());
                        R12Cell4.setCellStyle(numberStyle);
                    } else {
                        R12Cell4.setCellValue("");
                        R12Cell4.setCellStyle(textStyle);
                    }
                    // R12 Col H
                    // Cell R12Cell5 = row.createCell(7);
                    // if (record1.getR12_net_amt() != null) {
                    // R12Cell5.setCellValue(record1.getR12_net_amt().doubleValue());
                    // R12Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R12Cell5.setCellValue("");
                    // R12Cell5.setCellStyle(textStyle);
                    // }
                    // R12 Col I
                    Cell R12Cell6 = row.createCell(8);
                    if (record1.getR12_net_amt_bwp() != null) {
                        R12Cell6.setCellValue(record1.getR12_net_amt_bwp().doubleValue());
                        R12Cell6.setCellStyle(numberStyle);
                    } else {
                        R12Cell6.setCellValue("");
                        R12Cell6.setCellStyle(textStyle);
                    }
                    // R12 Col J
                    Cell R12Cell7 = row.createCell(9);
                    if (record1.getR12_bal_sub() != null) {
                        R12Cell7.setCellValue(record1.getR12_bal_sub().doubleValue());
                        R12Cell7.setCellStyle(numberStyle);
                    } else {
                        R12Cell7.setCellValue("");
                        R12Cell7.setCellStyle(textStyle);
                    }
                    // R12 Col K
                    Cell R12Cell8 = row.createCell(10);
                    if (record1.getR12_bal_sub_bwp() != null) {
                        R12Cell8.setCellValue(record1.getR12_bal_sub_bwp().doubleValue());
                        R12Cell8.setCellStyle(numberStyle);
                    } else {
                        R12Cell8.setCellValue("");
                        R12Cell8.setCellStyle(textStyle);
                    }
                    // R12 Col L
                    Cell R12Cell9 = row.createCell(11);
                    if (record1.getR12_bal_sub_diaries() != null) {
                        R12Cell9.setCellValue(record1.getR12_bal_sub_diaries().doubleValue());
                        R12Cell9.setCellStyle(numberStyle);
                    } else {
                        R12Cell9.setCellValue("");
                        R12Cell9.setCellStyle(textStyle);
                    }

                    // R12 Col M
                    Cell R12Cell10 = row.createCell(12);
                    if (record1.getR12_bal_sub_diaries_bwp() != null) {
                        R12Cell10.setCellValue(record1.getR12_bal_sub_diaries_bwp().doubleValue());
                        R12Cell10.setCellStyle(numberStyle);
                    } else {
                        R12Cell10.setCellValue("");
                        R12Cell10.setCellStyle(textStyle);
                    }
                    // row = sheet.getRow(12);
                    // Cell R13Cell1 = row.createCell(3);
                    // if (record.getR13_fig_bal_sheet() != null) {
                    // R13Cell1.setCellValue(record.getR13_fig_bal_sheet().doubleValue());
                    // R13Cell1.setCellStyle(numberStyle);
                    // } else {
                    // R13Cell1.setCellValue("");
                    // R13Cell1.setCellStyle(textStyle);
                    // }

                    // // R13 Col E
                    // Cell R13Cell2 = row.createCell(4);
                    // if (record.getR13_fig_bal_sheet_bwp() != null) {
                    // R13Cell2.setCellValue(record.getR13_fig_bal_sheet_bwp().doubleValue());
                    // R13Cell2.setCellStyle(numberStyle);
                    // } else {
                    // R13Cell2.setCellValue("");
                    // R13Cell2.setCellStyle(textStyle);
                    // }

                    // // R13 Col F
                    // Cell R13Cell3 = row.createCell(5);
                    // if (record.getR13_amt_statement_adj() != null) {
                    // R13Cell3.setCellValue(record.getR13_amt_statement_adj().doubleValue());
                    // R13Cell3.setCellStyle(numberStyle);
                    // } else {
                    // R13Cell3.setCellValue("");
                    // R13Cell3.setCellStyle(textStyle);
                    // }
                    // // R13 Col G
                    // Cell R13Cell4 = row.createCell(6);
                    // if (record.getR13_amt_statement_adj_bwp() != null) {
                    // R13Cell4.setCellValue(record.getR13_amt_statement_adj_bwp().doubleValue());
                    // R13Cell4.setCellStyle(numberStyle);
                    // } else {
                    // R13Cell4.setCellValue("");
                    // R13Cell4.setCellStyle(textStyle);
                    // }
                    // // R13 Col H
                    // Cell R13Cell5 = row.createCell(7);
                    // if (record.getR13_net_amt() != null) {
                    // R13Cell5.setCellValue(record.getR13_net_amt().doubleValue());
                    // R13Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R13Cell5.setCellValue("");
                    // R13Cell5.setCellStyle(textStyle);
                    // }
                    // // R13 Col I
                    // Cell R13Cell6 = row.createCell(8);
                    // if (record.getR13_net_amt_bwp() != null) {
                    // R13Cell6.setCellValue(record.getR13_net_amt_bwp().doubleValue());
                    // R13Cell6.setCellStyle(numberStyle);
                    // } else {
                    // R13Cell6.setCellValue("");
                    // R13Cell6.setCellStyle(textStyle);
                    // }
                    // // R13 Col J
                    // Cell R13Cell7 = row.createCell(9);
                    // if (record.getR13_bal_sub() != null) {
                    // R13Cell7.setCellValue(record.getR13_bal_sub().doubleValue());
                    // R13Cell7.setCellStyle(numberStyle);
                    // } else {
                    // R13Cell7.setCellValue("");
                    // R13Cell7.setCellStyle(textStyle);
                    // }
                    // // R13 Col K
                    // Cell R13Cell8 = row.createCell(10);
                    // if (record.getR13_bal_sub_bwp() != null) {
                    // R13Cell8.setCellValue(record.getR13_bal_sub_bwp().doubleValue());
                    // R13Cell8.setCellStyle(numberStyle);
                    // } else {
                    // R13Cell8.setCellValue("");
                    // R13Cell8.setCellStyle(textStyle);
                    // }
                    // // R13 Col L
                    // Cell R13Cell9 = row.createCell(11);
                    // if (record.getR13_bal_sub_diaries() != null) {
                    // R13Cell9.setCellValue(record.getR13_bal_sub_diaries().doubleValue());
                    // R13Cell9.setCellStyle(numberStyle);
                    // } else {
                    // R13Cell9.setCellValue("");
                    // R13Cell9.setCellStyle(textStyle);
                    // }
                    // // R13 Col M
                    // Cell R13Cell10 = row.createCell(12);
                    // if (record.getR13_bal_sub_diaries_bwp() != null) {
                    // R13Cell10.setCellValue(record.getR13_bal_sub_diaries_bwp().doubleValue());
                    // R13Cell10.setCellStyle(numberStyle);
                    // } else {
                    // R13Cell10.setCellValue("");
                    // R13Cell10.setCellStyle(textStyle);
                    // }

                    row = sheet.getRow(16);
                    Cell R17Cell1 = row.createCell(3);
                    if (record.getR17_fig_bal_sheet() != null) {
                        R17Cell1.setCellValue(record.getR17_fig_bal_sheet().doubleValue());
                        R17Cell1.setCellStyle(numberStyle);
                    } else {
                        R17Cell1.setCellValue("");
                        R17Cell1.setCellStyle(textStyle);
                    }

                    // R17 Col E
                    Cell R17Cell2 = row.createCell(4);
                    if (record.getR17_fig_bal_sheet_bwp() != null) {
                        R17Cell2.setCellValue(record.getR17_fig_bal_sheet_bwp().doubleValue());
                        R17Cell2.setCellStyle(numberStyle);
                    } else {
                        R17Cell2.setCellValue("");
                        R17Cell2.setCellStyle(textStyle);
                    }

                    // R17 Col F
                    Cell R17Cell3 = row.createCell(5);
                    if (record.getR17_amt_statement_adj() != null) {
                        R17Cell3.setCellValue(record.getR17_amt_statement_adj().doubleValue());
                        R17Cell3.setCellStyle(numberStyle);
                    } else {
                        R17Cell3.setCellValue("");
                        R17Cell3.setCellStyle(textStyle);
                    }
                    // R17 Col G
                    Cell R17Cell4 = row.createCell(6);
                    if (record.getR17_amt_statement_adj_bwp() != null) {
                        R17Cell4.setCellValue(record.getR17_amt_statement_adj_bwp().doubleValue());
                        R17Cell4.setCellStyle(numberStyle);
                    } else {
                        R17Cell4.setCellValue("");
                        R17Cell4.setCellStyle(textStyle);
                    }
                    // R17 Col H
                    // Cell R17Cell5 = row.createCell(7);
                    // if (record.getR17_net_amt() != null) {
                    // R17Cell5.setCellValue(record.getR17_net_amt().doubleValue());
                    // R17Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R17Cell5.setCellValue("");
                    // R17Cell5.setCellStyle(textStyle);
                    // }
                    // R17 Col I
                    Cell R17Cell6 = row.createCell(8);
                    if (record.getR17_net_amt_bwp() != null) {
                        R17Cell6.setCellValue(record.getR17_net_amt_bwp().doubleValue());
                        R17Cell6.setCellStyle(numberStyle);
                    } else {
                        R17Cell6.setCellValue("");
                        R17Cell6.setCellStyle(textStyle);
                    }
                    // R17 Col J
                    Cell R17Cell7 = row.createCell(9);
                    if (record.getR17_bal_sub() != null) {
                        R17Cell7.setCellValue(record.getR17_bal_sub().doubleValue());
                        R17Cell7.setCellStyle(numberStyle);
                    } else {
                        R17Cell7.setCellValue("");
                        R17Cell7.setCellStyle(textStyle);
                    }
                    // R17 Col K
                    Cell R17Cell8 = row.createCell(10);
                    if (record.getR17_bal_sub_bwp() != null) {
                        R17Cell8.setCellValue(record.getR17_bal_sub_bwp().doubleValue());
                        R17Cell8.setCellStyle(numberStyle);
                    } else {
                        R17Cell8.setCellValue("");
                        R17Cell8.setCellStyle(textStyle);
                    }
                    // R17 Col L
                    Cell R17Cell9 = row.createCell(11);
                    if (record.getR17_bal_sub_diaries() != null) {
                        R17Cell9.setCellValue(record.getR17_bal_sub_diaries().doubleValue());
                        R17Cell9.setCellStyle(numberStyle);
                    } else {
                        R17Cell9.setCellValue("");
                        R17Cell9.setCellStyle(textStyle);
                    }
                    // R17 Col M
                    Cell R17Cell10 = row.createCell(12);
                    if (record.getR17_bal_sub_diaries_bwp() != null) {
                        R17Cell10.setCellValue(record.getR17_bal_sub_diaries_bwp().doubleValue());
                        R17Cell10.setCellStyle(numberStyle);
                    } else {
                        R17Cell10.setCellValue("");
                        R17Cell10.setCellStyle(textStyle);
                    }

                    row = sheet.getRow(17);
                    Cell R18Cell1 = row.createCell(3);
                    if (record1.getR18_fig_bal_sheet() != null) {
                        R18Cell1.setCellValue(record1.getR18_fig_bal_sheet().doubleValue());
                        R18Cell1.setCellStyle(numberStyle);
                    } else {
                        R18Cell1.setCellValue("");
                        R18Cell1.setCellStyle(textStyle);
                    }

                    // R18 Col E
                    Cell R18Cell2 = row.createCell(4);
                    if (record1.getR18_fig_bal_sheet_bwp() != null) {
                        R18Cell2.setCellValue(record1.getR18_fig_bal_sheet_bwp().doubleValue());
                        R18Cell2.setCellStyle(numberStyle);
                    } else {
                        R18Cell2.setCellValue("");
                        R18Cell2.setCellStyle(textStyle);
                    }

                    // R18 Col F
                    Cell R18Cell3 = row.createCell(5);
                    if (record1.getR18_amt_statement_adj() != null) {
                        R18Cell3.setCellValue(record1.getR18_amt_statement_adj().doubleValue());
                        R18Cell3.setCellStyle(numberStyle);
                    } else {
                        R18Cell3.setCellValue("");
                        R18Cell3.setCellStyle(textStyle);
                    }
                    // R18 Col G
                    Cell R18Cell4 = row.createCell(6);
                    if (record1.getR18_amt_statement_adj_bwp() != null) {
                        R18Cell4.setCellValue(record1.getR18_amt_statement_adj_bwp().doubleValue());
                        R18Cell4.setCellStyle(numberStyle);
                    } else {
                        R18Cell4.setCellValue("");
                        R18Cell4.setCellStyle(textStyle);
                    }
                    // // R18 Col H
                    // Cell R18Cell5 = row.createCell(7);
                    // if (record1.getR18_net_amt() != null) {
                    // R18Cell5.setCellValue(record1.getR18_net_amt().doubleValue());
                    // R18Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R18Cell5.setCellValue("");
                    // R18Cell5.setCellStyle(textStyle);
                    // }
                    // R18 Col I
                    Cell R18Cell6 = row.createCell(8);
                    if (record1.getR18_net_amt_bwp() != null) {
                        R18Cell6.setCellValue(record1.getR18_net_amt_bwp().doubleValue());
                        R18Cell6.setCellStyle(numberStyle);
                    } else {
                        R18Cell6.setCellValue("");
                        R18Cell6.setCellStyle(textStyle);
                    }
                    // R18 Col J
                    Cell R18Cell7 = row.createCell(9);
                    if (record1.getR18_bal_sub() != null) {
                        R18Cell7.setCellValue(record1.getR18_bal_sub().doubleValue());
                        R18Cell7.setCellStyle(numberStyle);
                    } else {
                        R18Cell7.setCellValue("");
                        R18Cell7.setCellStyle(textStyle);
                    }
                    // R18 Col K
                    Cell R18Cell8 = row.createCell(10);
                    if (record1.getR18_bal_sub_bwp() != null) {
                        R18Cell8.setCellValue(record1.getR18_bal_sub_bwp().doubleValue());
                        R18Cell8.setCellStyle(numberStyle);
                    } else {
                        R18Cell8.setCellValue("");
                        R18Cell8.setCellStyle(textStyle);
                    }
                    // R18 Col L
                    Cell R18Cell9 = row.createCell(11);
                    if (record1.getR18_bal_sub_diaries() != null) {
                        R18Cell9.setCellValue(record1.getR18_bal_sub_diaries().doubleValue());
                        R18Cell9.setCellStyle(numberStyle);
                    } else {
                        R18Cell9.setCellValue("");
                        R18Cell9.setCellStyle(textStyle);
                    }
                    // R18 Col M
                    Cell R18Cell10 = row.createCell(12);
                    if (record1.getR18_bal_sub_diaries_bwp() != null) {
                        R18Cell10.setCellValue(record1.getR18_bal_sub_diaries_bwp().doubleValue());
                        R18Cell10.setCellStyle(numberStyle);
                    } else {
                        R18Cell10.setCellValue("");
                        R18Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(18);
                    Cell R19Cell1 = row.createCell(3);
                    if (record1.getR19_fig_bal_sheet() != null) {
                        R19Cell1.setCellValue(record1.getR19_fig_bal_sheet().doubleValue());
                        R19Cell1.setCellStyle(numberStyle);
                    } else {
                        R19Cell1.setCellValue("");
                        R19Cell1.setCellStyle(textStyle);
                    }

                    // R19 Col E
                    Cell R19Cell2 = row.createCell(4);
                    if (record1.getR19_fig_bal_sheet_bwp() != null) {
                        R19Cell2.setCellValue(record1.getR19_fig_bal_sheet_bwp().doubleValue());
                        R19Cell2.setCellStyle(numberStyle);
                    } else {
                        R19Cell2.setCellValue("");
                        R19Cell2.setCellStyle(textStyle);
                    }

                    // R19 Col F
                    Cell R19Cell3 = row.createCell(5);
                    if (record1.getR19_amt_statement_adj() != null) {
                        R19Cell3.setCellValue(record1.getR19_amt_statement_adj().doubleValue());
                        R19Cell3.setCellStyle(numberStyle);
                    } else {
                        R19Cell3.setCellValue("");
                        R19Cell3.setCellStyle(textStyle);
                    }
                    // R19 Col G
                    Cell R19Cell4 = row.createCell(6);
                    if (record1.getR19_amt_statement_adj_bwp() != null) {
                        R19Cell4.setCellValue(record1.getR19_amt_statement_adj_bwp().doubleValue());
                        R19Cell4.setCellStyle(numberStyle);
                    } else {
                        R19Cell4.setCellValue("");
                        R19Cell4.setCellStyle(textStyle);
                    }
                    // R19 Col H
                    // Cell R19Cell5 = row.createCell(7);
                    // if (record1.getR19_net_amt() != null) {
                    // R19Cell5.setCellValue(record1.getR19_net_amt().doubleValue());
                    // R19Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R19Cell5.setCellValue("");
                    // R19Cell5.setCellStyle(textStyle);
                    // }
                    // R19 Col I
                    Cell R19Cell6 = row.createCell(8);
                    if (record1.getR19_net_amt_bwp() != null) {
                        R19Cell6.setCellValue(record1.getR19_net_amt_bwp().doubleValue());
                        R19Cell6.setCellStyle(numberStyle);
                    } else {
                        R19Cell6.setCellValue("");
                        R19Cell6.setCellStyle(textStyle);
                    }
                    // R19 Col J
                    Cell R19Cell7 = row.createCell(9);
                    if (record1.getR19_bal_sub() != null) {
                        R19Cell7.setCellValue(record1.getR19_bal_sub().doubleValue());
                        R19Cell7.setCellStyle(numberStyle);
                    } else {
                        R19Cell7.setCellValue("");
                        R19Cell7.setCellStyle(textStyle);
                    }
                    // R19 Col K
                    Cell R19Cell8 = row.createCell(10);
                    if (record1.getR19_bal_sub_bwp() != null) {
                        R19Cell8.setCellValue(record1.getR19_bal_sub_bwp().doubleValue());
                        R19Cell8.setCellStyle(numberStyle);
                    } else {
                        R19Cell8.setCellValue("");
                        R19Cell8.setCellStyle(textStyle);
                    }
                    // R19 Col L
                    Cell R19Cell9 = row.createCell(11);
                    if (record1.getR19_bal_sub_diaries() != null) {
                        R19Cell9.setCellValue(record1.getR19_bal_sub_diaries().doubleValue());
                        R19Cell9.setCellStyle(numberStyle);
                    } else {
                        R19Cell9.setCellValue("");
                        R19Cell9.setCellStyle(textStyle);
                    }
                    // R19 Col M
                    Cell R19Cell10 = row.createCell(12);
                    if (record1.getR19_bal_sub_diaries_bwp() != null) {
                        R19Cell10.setCellValue(record1.getR19_bal_sub_diaries_bwp().doubleValue());
                        R19Cell10.setCellStyle(numberStyle);
                    } else {
                        R19Cell10.setCellValue("");
                        R19Cell10.setCellStyle(textStyle);
                    }

                    row = sheet.getRow(19);
                    Cell R20Cell1 = row.createCell(3);
                    if (record.getR20_fig_bal_sheet() != null) {
                        R20Cell1.setCellValue(record.getR20_fig_bal_sheet().doubleValue());
                        R20Cell1.setCellStyle(numberStyle);
                    } else {
                        R20Cell1.setCellValue("");
                        R20Cell1.setCellStyle(textStyle);
                    }

                    // R20 Col E
                    Cell R20Cell2 = row.createCell(4);
                    if (record.getR20_fig_bal_sheet_bwp() != null) {
                        R20Cell2.setCellValue(record.getR20_fig_bal_sheet_bwp().doubleValue());
                        R20Cell2.setCellStyle(numberStyle);
                    } else {
                        R20Cell2.setCellValue("");
                        R20Cell2.setCellStyle(textStyle);
                    }

                    // R20 Col F
                    Cell R20Cell3 = row.createCell(5);
                    if (record.getR20_amt_statement_adj() != null) {
                        R20Cell3.setCellValue(record.getR20_amt_statement_adj().doubleValue());
                        R20Cell3.setCellStyle(numberStyle);
                    } else {
                        R20Cell3.setCellValue("");
                        R20Cell3.setCellStyle(textStyle);
                    }
                    // R20 Col G
                    Cell R20Cell4 = row.createCell(6);
                    if (record.getR20_amt_statement_adj_bwp() != null) {
                        R20Cell4.setCellValue(record.getR20_amt_statement_adj_bwp().doubleValue());
                        R20Cell4.setCellStyle(numberStyle);
                    } else {
                        R20Cell4.setCellValue("");
                        R20Cell4.setCellStyle(textStyle);
                    }
                    // // R20 Col H
                    // Cell R20Cell5 = row.createCell(7);
                    // if (record.getR20_net_amt() != null) {
                    // R20Cell5.setCellValue(record.getR20_net_amt().doubleValue());
                    // R20Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R20Cell5.setCellValue("");
                    // R20Cell5.setCellStyle(textStyle);
                    // }
                    // R20 Col I
                    Cell R20Cell6 = row.createCell(8);
                    if (record.getR20_net_amt_bwp() != null) {
                        R20Cell6.setCellValue(record.getR20_net_amt_bwp().doubleValue());
                        R20Cell6.setCellStyle(numberStyle);
                    } else {
                        R20Cell6.setCellValue("");
                        R20Cell6.setCellStyle(textStyle);
                    }
                    // R20 Col J
                    Cell R20Cell7 = row.createCell(9);
                    if (record.getR20_bal_sub() != null) {
                        R20Cell7.setCellValue(record.getR20_bal_sub().doubleValue());
                        R20Cell7.setCellStyle(numberStyle);
                    } else {
                        R20Cell7.setCellValue("");
                        R20Cell7.setCellStyle(textStyle);
                    }
                    // R20 Col K
                    Cell R20Cell8 = row.createCell(10);
                    if (record.getR20_bal_sub_bwp() != null) {
                        R20Cell8.setCellValue(record.getR20_bal_sub_bwp().doubleValue());
                        R20Cell8.setCellStyle(numberStyle);
                    } else {
                        R20Cell8.setCellValue("");
                        R20Cell8.setCellStyle(textStyle);
                    }
                    // R20 Col L
                    Cell R20Cell9 = row.createCell(11);
                    if (record.getR20_bal_sub_diaries() != null) {
                        R20Cell9.setCellValue(record.getR20_bal_sub_diaries().doubleValue());
                        R20Cell9.setCellStyle(numberStyle);
                    } else {
                        R20Cell9.setCellValue("");
                        R20Cell9.setCellStyle(textStyle);
                    }
                    // R20 Col M
                    Cell R20Cell10 = row.createCell(12);
                    if (record.getR20_bal_sub_diaries_bwp() != null) {
                        R20Cell10.setCellValue(record.getR20_bal_sub_diaries_bwp().doubleValue());
                        R20Cell10.setCellStyle(numberStyle);
                    } else {
                        R20Cell10.setCellValue("");
                        R20Cell10.setCellStyle(textStyle);
                    }

                    row = sheet.getRow(20);
                    Cell R21Cell1 = row.createCell(3);
                    if (record1.getR21_fig_bal_sheet() != null) {
                        R21Cell1.setCellValue(record1.getR21_fig_bal_sheet().doubleValue());
                        R21Cell1.setCellStyle(numberStyle);
                    } else {
                        R21Cell1.setCellValue("");
                        R21Cell1.setCellStyle(textStyle);
                    }

                    // R21 Col E
                    Cell R21Cell2 = row.createCell(4);
                    if (record1.getR21_fig_bal_sheet_bwp() != null) {
                        R21Cell2.setCellValue(record1.getR21_fig_bal_sheet_bwp().doubleValue());
                        R21Cell2.setCellStyle(numberStyle);
                    } else {
                        R21Cell2.setCellValue("");
                        R21Cell2.setCellStyle(textStyle);
                    }

                    // R21 Col F
                    Cell R21Cell3 = row.createCell(5);
                    if (record1.getR21_amt_statement_adj() != null) {
                        R21Cell3.setCellValue(record1.getR21_amt_statement_adj().doubleValue());
                        R21Cell3.setCellStyle(numberStyle);
                    } else {
                        R21Cell3.setCellValue("");
                        R21Cell3.setCellStyle(textStyle);
                    }
                    // R21 Col G
                    Cell R21Cell4 = row.createCell(6);
                    if (record1.getR21_amt_statement_adj_bwp() != null) {
                        R21Cell4.setCellValue(record1.getR21_amt_statement_adj_bwp().doubleValue());
                        R21Cell4.setCellStyle(numberStyle);
                    } else {
                        R21Cell4.setCellValue("");
                        R21Cell4.setCellStyle(textStyle);
                    }
                    // // R21 Col H
                    // Cell R21Cell5 = row.createCell(7);
                    // if (record1.getR21_net_amt() != null) {
                    // R21Cell5.setCellValue(record1.getR21_net_amt().doubleValue());
                    // R21Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R21Cell5.setCellValue("");
                    // R21Cell5.setCellStyle(textStyle);
                    // }
                    // R21 Col I
                    Cell R21Cell6 = row.createCell(8);
                    if (record1.getR21_net_amt_bwp() != null) {
                        R21Cell6.setCellValue(record1.getR21_net_amt_bwp().doubleValue());
                        R21Cell6.setCellStyle(numberStyle);
                    } else {
                        R21Cell6.setCellValue("");
                        R21Cell6.setCellStyle(textStyle);
                    }
                    // R21 Col J
                    Cell R21Cell7 = row.createCell(9);
                    if (record1.getR21_bal_sub() != null) {
                        R21Cell7.setCellValue(record1.getR21_bal_sub().doubleValue());
                        R21Cell7.setCellStyle(numberStyle);
                    } else {
                        R21Cell7.setCellValue("");
                        R21Cell7.setCellStyle(textStyle);
                    }
                    // R21 Col K
                    Cell R21Cell8 = row.createCell(10);
                    if (record1.getR21_bal_sub_bwp() != null) {
                        R21Cell8.setCellValue(record1.getR21_bal_sub_bwp().doubleValue());
                        R21Cell8.setCellStyle(numberStyle);
                    } else {
                        R21Cell8.setCellValue("");
                        R21Cell8.setCellStyle(textStyle);
                    }
                    // R21 Col L
                    Cell R21Cell9 = row.createCell(11);
                    if (record1.getR21_bal_sub_diaries() != null) {
                        R21Cell9.setCellValue(record1.getR21_bal_sub_diaries().doubleValue());
                        R21Cell9.setCellStyle(numberStyle);
                    } else {
                        R21Cell9.setCellValue("");
                        R21Cell9.setCellStyle(textStyle);
                    }
                    // R21 Col M
                    Cell R21Cell10 = row.createCell(12);
                    if (record1.getR21_bal_sub_diaries_bwp() != null) {
                        R21Cell10.setCellValue(record1.getR21_bal_sub_diaries_bwp().doubleValue());
                        R21Cell10.setCellStyle(numberStyle);
                    } else {
                        R21Cell10.setCellValue("");
                        R21Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(21);
                    Cell R22Cell1 = row.createCell(3);
                    if (record1.getR22_fig_bal_sheet() != null) {
                        R22Cell1.setCellValue(record1.getR22_fig_bal_sheet().doubleValue());
                        R22Cell1.setCellStyle(numberStyle);
                    } else {
                        R22Cell1.setCellValue("");
                        R22Cell1.setCellStyle(textStyle);
                    }

                    // R22 Col E
                    Cell R22Cell2 = row.createCell(4);
                    if (record1.getR22_fig_bal_sheet_bwp() != null) {
                        R22Cell2.setCellValue(record1.getR22_fig_bal_sheet_bwp().doubleValue());
                        R22Cell2.setCellStyle(numberStyle);
                    } else {
                        R22Cell2.setCellValue("");
                        R22Cell2.setCellStyle(textStyle);
                    }

                    // R22 Col F
                    Cell R22Cell3 = row.createCell(5);
                    if (record1.getR22_amt_statement_adj() != null) {
                        R22Cell3.setCellValue(record1.getR22_amt_statement_adj().doubleValue());
                        R22Cell3.setCellStyle(numberStyle);
                    } else {
                        R22Cell3.setCellValue("");
                        R22Cell3.setCellStyle(textStyle);
                    }
                    // R22 Col G
                    Cell R22Cell4 = row.createCell(6);
                    if (record1.getR22_amt_statement_adj_bwp() != null) {
                        R22Cell4.setCellValue(record1.getR22_amt_statement_adj_bwp().doubleValue());
                        R22Cell4.setCellStyle(numberStyle);
                    } else {
                        R22Cell4.setCellValue("");
                        R22Cell4.setCellStyle(textStyle);
                    }
                    // // R22 Col H
                    // Cell R22Cell5 = row.createCell(7);
                    // if (record1.getR22_net_amt() != null) {
                    // R22Cell5.setCellValue(record1.getR22_net_amt().doubleValue());
                    // R22Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R22Cell5.setCellValue("");
                    // R22Cell5.setCellStyle(textStyle);
                    // }
                    // R22 Col I
                    Cell R22Cell6 = row.createCell(8);
                    if (record1.getR22_net_amt_bwp() != null) {
                        R22Cell6.setCellValue(record1.getR22_net_amt_bwp().doubleValue());
                        R22Cell6.setCellStyle(numberStyle);
                    } else {
                        R22Cell6.setCellValue("");
                        R22Cell6.setCellStyle(textStyle);
                    }
                    // R22 Col J
                    Cell R22Cell7 = row.createCell(9);
                    if (record1.getR22_bal_sub() != null) {
                        R22Cell7.setCellValue(record1.getR22_bal_sub().doubleValue());
                        R22Cell7.setCellStyle(numberStyle);
                    } else {
                        R22Cell7.setCellValue("");
                        R22Cell7.setCellStyle(textStyle);
                    }
                    // R22 Col K
                    Cell R22Cell8 = row.createCell(10);
                    if (record1.getR22_bal_sub_bwp() != null) {
                        R22Cell8.setCellValue(record1.getR22_bal_sub_bwp().doubleValue());
                        R22Cell8.setCellStyle(numberStyle);
                    } else {
                        R22Cell8.setCellValue("");
                        R22Cell8.setCellStyle(textStyle);
                    }
                    // R22 Col L
                    Cell R22Cell9 = row.createCell(11);
                    if (record1.getR22_bal_sub_diaries() != null) {
                        R22Cell9.setCellValue(record1.getR22_bal_sub_diaries().doubleValue());
                        R22Cell9.setCellStyle(numberStyle);
                    } else {
                        R22Cell9.setCellValue("");
                        R22Cell9.setCellStyle(textStyle);
                    }
                    // R22 Col M
                    Cell R22Cell10 = row.createCell(12);
                    if (record1.getR22_bal_sub_diaries_bwp() != null) {
                        R22Cell10.setCellValue(record1.getR22_bal_sub_diaries_bwp().doubleValue());
                        R22Cell10.setCellStyle(numberStyle);
                    } else {
                        R22Cell10.setCellValue("");
                        R22Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(22);
                    Cell R23Cell1 = row.createCell(3);
                    if (record1.getR23_fig_bal_sheet() != null) {
                        R23Cell1.setCellValue(record1.getR23_fig_bal_sheet().doubleValue());
                        R23Cell1.setCellStyle(numberStyle);
                    } else {
                        R23Cell1.setCellValue("");
                        R23Cell1.setCellStyle(textStyle);
                    }

                    // R23 Col E
                    Cell R23Cell2 = row.createCell(4);
                    if (record1.getR23_fig_bal_sheet_bwp() != null) {
                        R23Cell2.setCellValue(record1.getR23_fig_bal_sheet_bwp().doubleValue());
                        R23Cell2.setCellStyle(numberStyle);
                    } else {
                        R23Cell2.setCellValue("");
                        R23Cell2.setCellStyle(textStyle);
                    }

                    // R23 Col F
                    Cell R23Cell3 = row.createCell(5);
                    if (record1.getR23_amt_statement_adj() != null) {
                        R23Cell3.setCellValue(record1.getR23_amt_statement_adj().doubleValue());
                        R23Cell3.setCellStyle(numberStyle);
                    } else {
                        R23Cell3.setCellValue("");
                        R23Cell3.setCellStyle(textStyle);
                    }
                    // R23 Col G
                    Cell R23Cell4 = row.createCell(6);
                    if (record1.getR23_amt_statement_adj_bwp() != null) {
                        R23Cell4.setCellValue(record1.getR23_amt_statement_adj_bwp().doubleValue());
                        R23Cell4.setCellStyle(numberStyle);
                    } else {
                        R23Cell4.setCellValue("");
                        R23Cell4.setCellStyle(textStyle);
                    }
                    // // R23 Col H
                    // Cell R23Cell5 = row.createCell(7);
                    // if (record1.getR23_net_amt() != null) {
                    // R23Cell5.setCellValue(record1.getR23_net_amt().doubleValue());
                    // R23Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R23Cell5.setCellValue("");
                    // R23Cell5.setCellStyle(textStyle);
                    // }
                    // R23 Col I
                    Cell R23Cell6 = row.createCell(8);
                    if (record1.getR23_net_amt_bwp() != null) {
                        R23Cell6.setCellValue(record1.getR23_net_amt_bwp().doubleValue());
                        R23Cell6.setCellStyle(numberStyle);
                    } else {
                        R23Cell6.setCellValue("");
                        R23Cell6.setCellStyle(textStyle);
                    }
                    // R23 Col J
                    Cell R23Cell7 = row.createCell(9);
                    if (record1.getR23_bal_sub() != null) {
                        R23Cell7.setCellValue(record1.getR23_bal_sub().doubleValue());
                        R23Cell7.setCellStyle(numberStyle);
                    } else {
                        R23Cell7.setCellValue("");
                        R23Cell7.setCellStyle(textStyle);
                    }
                    // R23 Col K
                    Cell R23Cell8 = row.createCell(10);
                    if (record1.getR23_bal_sub_bwp() != null) {
                        R23Cell8.setCellValue(record1.getR23_bal_sub_bwp().doubleValue());
                        R23Cell8.setCellStyle(numberStyle);
                    } else {
                        R23Cell8.setCellValue("");
                        R23Cell8.setCellStyle(textStyle);
                    }
                    // R23 Col L
                    Cell R23Cell9 = row.createCell(11);
                    if (record1.getR23_bal_sub_diaries() != null) {
                        R23Cell9.setCellValue(record1.getR23_bal_sub_diaries().doubleValue());
                        R23Cell9.setCellStyle(numberStyle);
                    } else {
                        R23Cell9.setCellValue("");
                        R23Cell9.setCellStyle(textStyle);
                    }
                    // R23 Col M
                    Cell R23Cell10 = row.createCell(12);
                    if (record1.getR23_bal_sub_diaries_bwp() != null) {
                        R23Cell10.setCellValue(record1.getR23_bal_sub_diaries_bwp().doubleValue());
                        R23Cell10.setCellStyle(numberStyle);
                    } else {
                        R23Cell10.setCellValue("");
                        R23Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(23);
                    Cell R24Cell1 = row.createCell(3);
                    if (record1.getR24_fig_bal_sheet() != null) {
                        R24Cell1.setCellValue(record1.getR24_fig_bal_sheet().doubleValue());
                        R24Cell1.setCellStyle(numberStyle);
                    } else {
                        R24Cell1.setCellValue("");
                        R24Cell1.setCellStyle(textStyle);
                    }

                    // R24 Col E
                    Cell R24Cell2 = row.createCell(4);
                    if (record1.getR24_fig_bal_sheet_bwp() != null) {
                        R24Cell2.setCellValue(record1.getR24_fig_bal_sheet_bwp().doubleValue());
                        R24Cell2.setCellStyle(numberStyle);
                    } else {
                        R24Cell2.setCellValue("");
                        R24Cell2.setCellStyle(textStyle);
                    }

                    // R24 Col F
                    Cell R24Cell3 = row.createCell(5);
                    if (record1.getR24_amt_statement_adj() != null) {
                        R24Cell3.setCellValue(record1.getR24_amt_statement_adj().doubleValue());
                        R24Cell3.setCellStyle(numberStyle);
                    } else {
                        R24Cell3.setCellValue("");
                        R24Cell3.setCellStyle(textStyle);
                    }
                    // R24 Col G
                    Cell R24Cell4 = row.createCell(6);
                    if (record1.getR24_amt_statement_adj_bwp() != null) {
                        R24Cell4.setCellValue(record1.getR24_amt_statement_adj_bwp().doubleValue());
                        R24Cell4.setCellStyle(numberStyle);
                    } else {
                        R24Cell4.setCellValue("");
                        R24Cell4.setCellStyle(textStyle);
                    }
                    // // R24 Col H
                    // Cell R24Cell5 = row.createCell(7);
                    // if (record1.getR24_net_amt() != null) {
                    // R24Cell5.setCellValue(record1.getR24_net_amt().doubleValue());
                    // R24Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R24Cell5.setCellValue("");
                    // R24Cell5.setCellStyle(textStyle);
                    // }
                    // R24 Col I
                    Cell R24Cell6 = row.createCell(8);
                    if (record1.getR24_net_amt_bwp() != null) {
                        R24Cell6.setCellValue(record1.getR24_net_amt_bwp().doubleValue());
                        R24Cell6.setCellStyle(numberStyle);
                    } else {
                        R24Cell6.setCellValue("");
                        R24Cell6.setCellStyle(textStyle);
                    }
                    // R24 Col J
                    Cell R24Cell7 = row.createCell(9);
                    if (record1.getR24_bal_sub() != null) {
                        R24Cell7.setCellValue(record1.getR24_bal_sub().doubleValue());
                        R24Cell7.setCellStyle(numberStyle);
                    } else {
                        R24Cell7.setCellValue("");
                        R24Cell7.setCellStyle(textStyle);
                    }
                    // R24 Col K
                    Cell R24Cell8 = row.createCell(10);
                    if (record1.getR24_bal_sub_bwp() != null) {
                        R24Cell8.setCellValue(record1.getR24_bal_sub_bwp().doubleValue());
                        R24Cell8.setCellStyle(numberStyle);
                    } else {
                        R24Cell8.setCellValue("");
                        R24Cell8.setCellStyle(textStyle);
                    }
                    // R24 Col L
                    Cell R24Cell9 = row.createCell(11);
                    if (record1.getR24_bal_sub_diaries() != null) {
                        R24Cell9.setCellValue(record1.getR24_bal_sub_diaries().doubleValue());
                        R24Cell9.setCellStyle(numberStyle);
                    } else {
                        R24Cell9.setCellValue("");
                        R24Cell9.setCellStyle(textStyle);
                    }
                    // R24 Col M
                    Cell R24Cell10 = row.createCell(12);
                    if (record1.getR24_bal_sub_diaries_bwp() != null) {
                        R24Cell10.setCellValue(record1.getR24_bal_sub_diaries_bwp().doubleValue());
                        R24Cell10.setCellStyle(numberStyle);
                    } else {
                        R24Cell10.setCellValue("");
                        R24Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(24);
                    Cell R25Cell1 = row.createCell(3);
                    if (record1.getR25_fig_bal_sheet() != null) {
                        R25Cell1.setCellValue(record1.getR25_fig_bal_sheet().doubleValue());
                        R25Cell1.setCellStyle(numberStyle);
                    } else {
                        R25Cell1.setCellValue("");
                        R25Cell1.setCellStyle(textStyle);
                    }

                    // R25 Col E
                    Cell R25Cell2 = row.createCell(4);
                    if (record1.getR25_fig_bal_sheet_bwp() != null) {
                        R25Cell2.setCellValue(record1.getR25_fig_bal_sheet_bwp().doubleValue());
                        R25Cell2.setCellStyle(numberStyle);
                    } else {
                        R25Cell2.setCellValue("");
                        R25Cell2.setCellStyle(textStyle);
                    }

                    // R25 Col F
                    Cell R25Cell3 = row.createCell(5);
                    if (record1.getR25_amt_statement_adj() != null) {
                        R25Cell3.setCellValue(record1.getR25_amt_statement_adj().doubleValue());
                        R25Cell3.setCellStyle(numberStyle);
                    } else {
                        R25Cell3.setCellValue("");
                        R25Cell3.setCellStyle(textStyle);
                    }
                    // R25 Col G
                    Cell R25Cell4 = row.createCell(6);
                    if (record1.getR25_amt_statement_adj_bwp() != null) {
                        R25Cell4.setCellValue(record1.getR25_amt_statement_adj_bwp().doubleValue());
                        R25Cell4.setCellStyle(numberStyle);
                    } else {
                        R25Cell4.setCellValue("");
                        R25Cell4.setCellStyle(textStyle);
                    }
                    // // R25 Col H
                    // Cell R25Cell5 = row.createCell(7);
                    // if (record1.getR25_net_amt() != null) {
                    // R25Cell5.setCellValue(record1.getR25_net_amt().doubleValue());
                    // R25Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R25Cell5.setCellValue("");
                    // R25Cell5.setCellStyle(textStyle);
                    // }
                    // R25 Col I
                    Cell R25Cell6 = row.createCell(8);
                    if (record1.getR25_net_amt_bwp() != null) {
                        R25Cell6.setCellValue(record1.getR25_net_amt_bwp().doubleValue());
                        R25Cell6.setCellStyle(numberStyle);
                    } else {
                        R25Cell6.setCellValue("");
                        R25Cell6.setCellStyle(textStyle);
                    }
                    // R25 Col J
                    Cell R25Cell7 = row.createCell(9);
                    if (record1.getR25_bal_sub() != null) {
                        R25Cell7.setCellValue(record1.getR25_bal_sub().doubleValue());
                        R25Cell7.setCellStyle(numberStyle);
                    } else {
                        R25Cell7.setCellValue("");
                        R25Cell7.setCellStyle(textStyle);
                    }
                    // R25 Col K
                    Cell R25Cell8 = row.createCell(10);
                    if (record1.getR25_bal_sub_bwp() != null) {
                        R25Cell8.setCellValue(record1.getR25_bal_sub_bwp().doubleValue());
                        R25Cell8.setCellStyle(numberStyle);
                    } else {
                        R25Cell8.setCellValue("");
                        R25Cell8.setCellStyle(textStyle);
                    }
                    // R25 Col L
                    Cell R25Cell9 = row.createCell(11);
                    if (record1.getR25_bal_sub_diaries() != null) {
                        R25Cell9.setCellValue(record1.getR25_bal_sub_diaries().doubleValue());
                        R25Cell9.setCellStyle(numberStyle);
                    } else {
                        R25Cell9.setCellValue("");
                        R25Cell9.setCellStyle(textStyle);
                    }
                    // R25 Col M
                    Cell R25Cell10 = row.createCell(12);
                    if (record1.getR25_bal_sub_diaries_bwp() != null) {
                        R25Cell10.setCellValue(record1.getR25_bal_sub_diaries_bwp().doubleValue());
                        R25Cell10.setCellStyle(numberStyle);
                    } else {
                        R25Cell10.setCellValue("");
                        R25Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(25);
                    Cell R26Cell1 = row.createCell(3);
                    if (record1.getR26_fig_bal_sheet() != null) {
                        R26Cell1.setCellValue(record1.getR26_fig_bal_sheet().doubleValue());
                        R26Cell1.setCellStyle(numberStyle);
                    } else {
                        R26Cell1.setCellValue("");
                        R26Cell1.setCellStyle(textStyle);
                    }

                    // R26 Col E
                    Cell R26Cell2 = row.createCell(4);
                    if (record1.getR26_fig_bal_sheet_bwp() != null) {
                        R26Cell2.setCellValue(record1.getR26_fig_bal_sheet_bwp().doubleValue());
                        R26Cell2.setCellStyle(numberStyle);
                    } else {
                        R26Cell2.setCellValue("");
                        R26Cell2.setCellStyle(textStyle);
                    }

                    // R26 Col F
                    Cell R26Cell3 = row.createCell(5);
                    if (record1.getR26_amt_statement_adj() != null) {
                        R26Cell3.setCellValue(record1.getR26_amt_statement_adj().doubleValue());
                        R26Cell3.setCellStyle(numberStyle);
                    } else {
                        R26Cell3.setCellValue("");
                        R26Cell3.setCellStyle(textStyle);
                    }
                    // R26 Col G
                    Cell R26Cell4 = row.createCell(6);
                    if (record1.getR26_amt_statement_adj_bwp() != null) {
                        R26Cell4.setCellValue(record1.getR26_amt_statement_adj_bwp().doubleValue());
                        R26Cell4.setCellStyle(numberStyle);
                    } else {
                        R26Cell4.setCellValue("");
                        R26Cell4.setCellStyle(textStyle);
                    }
                    // // R26 Col H
                    // Cell R26Cell5 = row.createCell(7);
                    // if (record1.getR26_net_amt() != null) {
                    // R26Cell5.setCellValue(record1.getR26_net_amt().doubleValue());
                    // R26Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R26Cell5.setCellValue("");
                    // R26Cell5.setCellStyle(textStyle);
                    // }
                    // R26 Col I
                    Cell R26Cell6 = row.createCell(8);
                    if (record1.getR26_net_amt_bwp() != null) {
                        R26Cell6.setCellValue(record1.getR26_net_amt_bwp().doubleValue());
                        R26Cell6.setCellStyle(numberStyle);
                    } else {
                        R26Cell6.setCellValue("");
                        R26Cell6.setCellStyle(textStyle);
                    }
                    // R26 Col J
                    Cell R26Cell7 = row.createCell(9);
                    if (record1.getR26_bal_sub() != null) {
                        R26Cell7.setCellValue(record1.getR26_bal_sub().doubleValue());
                        R26Cell7.setCellStyle(numberStyle);
                    } else {
                        R26Cell7.setCellValue("");
                        R26Cell7.setCellStyle(textStyle);
                    }
                    // R26 Col K
                    Cell R26Cell8 = row.createCell(10);
                    if (record1.getR26_bal_sub_bwp() != null) {
                        R26Cell8.setCellValue(record1.getR26_bal_sub_bwp().doubleValue());
                        R26Cell8.setCellStyle(numberStyle);
                    } else {
                        R26Cell8.setCellValue("");
                        R26Cell8.setCellStyle(textStyle);
                    }
                    // R26 Col L
                    Cell R26Cell9 = row.createCell(11);
                    if (record1.getR26_bal_sub_diaries() != null) {
                        R26Cell9.setCellValue(record1.getR26_bal_sub_diaries().doubleValue());
                        R26Cell9.setCellStyle(numberStyle);
                    } else {
                        R26Cell9.setCellValue("");
                        R26Cell9.setCellStyle(textStyle);
                    }
                    // R26 Col M
                    Cell R26Cell10 = row.createCell(12);
                    if (record1.getR26_bal_sub_diaries_bwp() != null) {
                        R26Cell10.setCellValue(record1.getR26_bal_sub_diaries_bwp().doubleValue());
                        R26Cell10.setCellStyle(numberStyle);
                    } else {
                        R26Cell10.setCellValue("");
                        R26Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(26);
                    Cell R27Cell1 = row.createCell(3);
                    if (record1.getR27_fig_bal_sheet() != null) {
                        R27Cell1.setCellValue(record1.getR27_fig_bal_sheet().doubleValue());
                        R27Cell1.setCellStyle(numberStyle);
                    } else {
                        R27Cell1.setCellValue("");
                        R27Cell1.setCellStyle(textStyle);
                    }

                    // R27 Col E
                    Cell R27Cell2 = row.createCell(4);
                    if (record1.getR27_fig_bal_sheet_bwp() != null) {
                        R27Cell2.setCellValue(record1.getR27_fig_bal_sheet_bwp().doubleValue());
                        R27Cell2.setCellStyle(numberStyle);
                    } else {
                        R27Cell2.setCellValue("");
                        R27Cell2.setCellStyle(textStyle);
                    }

                    // R27 Col F
                    Cell R27Cell3 = row.createCell(5);
                    if (record1.getR27_amt_statement_adj() != null) {
                        R27Cell3.setCellValue(record1.getR27_amt_statement_adj().doubleValue());
                        R27Cell3.setCellStyle(numberStyle);
                    } else {
                        R27Cell3.setCellValue("");
                        R27Cell3.setCellStyle(textStyle);
                    }
                    // R27 Col G
                    Cell R27Cell4 = row.createCell(6);
                    if (record1.getR27_amt_statement_adj_bwp() != null) {
                        R27Cell4.setCellValue(record1.getR27_amt_statement_adj_bwp().doubleValue());
                        R27Cell4.setCellStyle(numberStyle);
                    } else {
                        R27Cell4.setCellValue("");
                        R27Cell4.setCellStyle(textStyle);
                    }
                    // // R27 Col H
                    // Cell R27Cell5 = row.createCell(7);
                    // if (record1.getR27_net_amt() != null) {
                    // R27Cell5.setCellValue(record1.getR27_net_amt().doubleValue());
                    // R27Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R27Cell5.setCellValue("");
                    // R27Cell5.setCellStyle(textStyle);
                    // }
                    // R27 Col I
                    Cell R27Cell6 = row.createCell(8);
                    if (record1.getR27_net_amt_bwp() != null) {
                        R27Cell6.setCellValue(record1.getR27_net_amt_bwp().doubleValue());
                        R27Cell6.setCellStyle(numberStyle);
                    } else {
                        R27Cell6.setCellValue("");
                        R27Cell6.setCellStyle(textStyle);
                    }
                    // R27 Col J
                    Cell R27Cell7 = row.createCell(9);
                    if (record1.getR27_bal_sub() != null) {
                        R27Cell7.setCellValue(record1.getR27_bal_sub().doubleValue());
                        R27Cell7.setCellStyle(numberStyle);
                    } else {
                        R27Cell7.setCellValue("");
                        R27Cell7.setCellStyle(textStyle);
                    }
                    // R27 Col K
                    Cell R27Cell8 = row.createCell(10);
                    if (record1.getR27_bal_sub_bwp() != null) {
                        R27Cell8.setCellValue(record1.getR27_bal_sub_bwp().doubleValue());
                        R27Cell8.setCellStyle(numberStyle);
                    } else {
                        R27Cell8.setCellValue("");
                        R27Cell8.setCellStyle(textStyle);
                    }
                    // R27 Col L
                    Cell R27Cell9 = row.createCell(11);
                    if (record1.getR27_bal_sub_diaries() != null) {
                        R27Cell9.setCellValue(record1.getR27_bal_sub_diaries().doubleValue());
                        R27Cell9.setCellStyle(numberStyle);
                    } else {
                        R27Cell9.setCellValue("");
                        R27Cell9.setCellStyle(textStyle);
                    }
                    // R27 Col M
                    Cell R27Cell10 = row.createCell(12);
                    if (record1.getR27_bal_sub_diaries_bwp() != null) {
                        R27Cell10.setCellValue(record1.getR27_bal_sub_diaries_bwp().doubleValue());
                        R27Cell10.setCellStyle(numberStyle);
                    } else {
                        R27Cell10.setCellValue("");
                        R27Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(27);
                    Cell R28Cell1 = row.createCell(3);
                    if (record1.getR28_fig_bal_sheet() != null) {
                        R28Cell1.setCellValue(record1.getR28_fig_bal_sheet().doubleValue());
                        R28Cell1.setCellStyle(numberStyle);
                    } else {
                        R28Cell1.setCellValue("");
                        R28Cell1.setCellStyle(textStyle);
                    }

                    // R28 Col E
                    Cell R28Cell2 = row.createCell(4);
                    if (record1.getR28_fig_bal_sheet_bwp() != null) {
                        R28Cell2.setCellValue(record1.getR28_fig_bal_sheet_bwp().doubleValue());
                        R28Cell2.setCellStyle(numberStyle);
                    } else {
                        R28Cell2.setCellValue("");
                        R28Cell2.setCellStyle(textStyle);
                    }

                    // R28 Col F
                    Cell R28Cell3 = row.createCell(5);
                    if (record1.getR28_amt_statement_adj() != null) {
                        R28Cell3.setCellValue(record1.getR28_amt_statement_adj().doubleValue());
                        R28Cell3.setCellStyle(numberStyle);
                    } else {
                        R28Cell3.setCellValue("");
                        R28Cell3.setCellStyle(textStyle);
                    }
                    // R28 Col G
                    Cell R28Cell4 = row.createCell(6);
                    if (record1.getR28_amt_statement_adj_bwp() != null) {
                        R28Cell4.setCellValue(record1.getR28_amt_statement_adj_bwp().doubleValue());
                        R28Cell4.setCellStyle(numberStyle);
                    } else {
                        R28Cell4.setCellValue("");
                        R28Cell4.setCellStyle(textStyle);
                    }
                    // // R28 Col H
                    // Cell R28Cell5 = row.createCell(7);
                    // if (record1.getR28_net_amt() != null) {
                    // R28Cell5.setCellValue(record1.getR28_net_amt().doubleValue());
                    // R28Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R28Cell5.setCellValue("");
                    // R28Cell5.setCellStyle(textStyle);
                    // }
                    // R28 Col I
                    Cell R28Cell6 = row.createCell(8);
                    if (record1.getR28_net_amt_bwp() != null) {
                        R28Cell6.setCellValue(record1.getR28_net_amt_bwp().doubleValue());
                        R28Cell6.setCellStyle(numberStyle);
                    } else {
                        R28Cell6.setCellValue("");
                        R28Cell6.setCellStyle(textStyle);
                    }
                    // R28 Col J
                    Cell R28Cell7 = row.createCell(9);
                    if (record1.getR28_bal_sub() != null) {
                        R28Cell7.setCellValue(record1.getR28_bal_sub().doubleValue());
                        R28Cell7.setCellStyle(numberStyle);
                    } else {
                        R28Cell7.setCellValue("");
                        R28Cell7.setCellStyle(textStyle);
                    }
                    // R28 Col K
                    Cell R28Cell8 = row.createCell(10);
                    if (record1.getR28_bal_sub_bwp() != null) {
                        R28Cell8.setCellValue(record1.getR28_bal_sub_bwp().doubleValue());
                        R28Cell8.setCellStyle(numberStyle);
                    } else {
                        R28Cell8.setCellValue("");
                        R28Cell8.setCellStyle(textStyle);
                    }
                    // R28 Col L
                    Cell R28Cell9 = row.createCell(11);
                    if (record1.getR28_bal_sub_diaries() != null) {
                        R28Cell9.setCellValue(record1.getR28_bal_sub_diaries().doubleValue());
                        R28Cell9.setCellStyle(numberStyle);
                    } else {
                        R28Cell9.setCellValue("");
                        R28Cell9.setCellStyle(textStyle);
                    }
                    // R28 Col M
                    Cell R28Cell10 = row.createCell(12);
                    if (record1.getR28_bal_sub_diaries_bwp() != null) {
                        R28Cell10.setCellValue(record1.getR28_bal_sub_diaries_bwp().doubleValue());
                        R28Cell10.setCellStyle(numberStyle);
                    } else {
                        R28Cell10.setCellValue("");
                        R28Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(28);
                    Cell R29Cell1 = row.createCell(3);
                    if (record1.getR29_fig_bal_sheet() != null) {
                        R29Cell1.setCellValue(record1.getR29_fig_bal_sheet().doubleValue());
                        R29Cell1.setCellStyle(numberStyle);
                    } else {
                        R29Cell1.setCellValue("");
                        R29Cell1.setCellStyle(textStyle);
                    }

                    // R29 Col E
                    Cell R29Cell2 = row.createCell(4);
                    if (record1.getR29_fig_bal_sheet_bwp() != null) {
                        R29Cell2.setCellValue(record1.getR29_fig_bal_sheet_bwp().doubleValue());
                        R29Cell2.setCellStyle(numberStyle);
                    } else {
                        R29Cell2.setCellValue("");
                        R29Cell2.setCellStyle(textStyle);
                    }

                    // R29 Col F
                    Cell R29Cell3 = row.createCell(5);
                    if (record1.getR29_amt_statement_adj() != null) {
                        R29Cell3.setCellValue(record1.getR29_amt_statement_adj().doubleValue());
                        R29Cell3.setCellStyle(numberStyle);
                    } else {
                        R29Cell3.setCellValue("");
                        R29Cell3.setCellStyle(textStyle);
                    }
                    // R29 Col G
                    Cell R29Cell4 = row.createCell(6);
                    if (record1.getR29_amt_statement_adj_bwp() != null) {
                        R29Cell4.setCellValue(record1.getR29_amt_statement_adj_bwp().doubleValue());
                        R29Cell4.setCellStyle(numberStyle);
                    } else {
                        R29Cell4.setCellValue("");
                        R29Cell4.setCellStyle(textStyle);
                    }
                    // // R29 Col H
                    // Cell R29Cell5 = row.createCell(7);
                    // if (record1.getR29_net_amt() != null) {
                    // R29Cell5.setCellValue(record1.getR29_net_amt().doubleValue());
                    // R29Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R29Cell5.setCellValue("");
                    // R29Cell5.setCellStyle(textStyle);
                    // }
                    // R29 Col I
                    Cell R29Cell6 = row.createCell(8);
                    if (record1.getR29_net_amt_bwp() != null) {
                        R29Cell6.setCellValue(record1.getR29_net_amt_bwp().doubleValue());
                        R29Cell6.setCellStyle(numberStyle);
                    } else {
                        R29Cell6.setCellValue("");
                        R29Cell6.setCellStyle(textStyle);
                    }
                    // R29 Col J
                    Cell R29Cell7 = row.createCell(9);
                    if (record1.getR29_bal_sub() != null) {
                        R29Cell7.setCellValue(record1.getR29_bal_sub().doubleValue());
                        R29Cell7.setCellStyle(numberStyle);
                    } else {
                        R29Cell7.setCellValue("");
                        R29Cell7.setCellStyle(textStyle);
                    }
                    // R29 Col K
                    Cell R29Cell8 = row.createCell(10);
                    if (record1.getR29_bal_sub_bwp() != null) {
                        R29Cell8.setCellValue(record1.getR29_bal_sub_bwp().doubleValue());
                        R29Cell8.setCellStyle(numberStyle);
                    } else {
                        R29Cell8.setCellValue("");
                        R29Cell8.setCellStyle(textStyle);
                    }
                    // R29 Col L
                    Cell R29Cell9 = row.createCell(11);
                    if (record1.getR29_bal_sub_diaries() != null) {
                        R29Cell9.setCellValue(record1.getR29_bal_sub_diaries().doubleValue());
                        R29Cell9.setCellStyle(numberStyle);
                    } else {
                        R29Cell9.setCellValue("");
                        R29Cell9.setCellStyle(textStyle);
                    }
                    // R29 Col M
                    Cell R29Cell10 = row.createCell(12);
                    if (record1.getR29_bal_sub_diaries_bwp() != null) {
                        R29Cell10.setCellValue(record1.getR29_bal_sub_diaries_bwp().doubleValue());
                        R29Cell10.setCellStyle(numberStyle);
                    } else {
                        R29Cell10.setCellValue("");
                        R29Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(29);
                    Cell R30Cell1 = row.createCell(3);
                    if (record.getR30_fig_bal_sheet() != null) {
                        R30Cell1.setCellValue(record.getR30_fig_bal_sheet().doubleValue());
                        R30Cell1.setCellStyle(numberStyle);
                    } else {
                        R30Cell1.setCellValue("");
                        R30Cell1.setCellStyle(textStyle);
                    }

                    // R30 Col E
                    Cell R30Cell2 = row.createCell(4);
                    if (record.getR30_fig_bal_sheet_bwp() != null) {
                        R30Cell2.setCellValue(record.getR30_fig_bal_sheet_bwp().doubleValue());
                        R30Cell2.setCellStyle(numberStyle);
                    } else {
                        R30Cell2.setCellValue("");
                        R30Cell2.setCellStyle(textStyle);
                    }

                    // R30 Col F
                    Cell R30Cell3 = row.createCell(5);
                    if (record.getR30_amt_statement_adj() != null) {
                        R30Cell3.setCellValue(record.getR30_amt_statement_adj().doubleValue());
                        R30Cell3.setCellStyle(numberStyle);
                    } else {
                        R30Cell3.setCellValue("");
                        R30Cell3.setCellStyle(textStyle);
                    }
                    // R30 Col G
                    Cell R30Cell4 = row.createCell(6);
                    if (record.getR30_amt_statement_adj_bwp() != null) {
                        R30Cell4.setCellValue(record.getR30_amt_statement_adj_bwp().doubleValue());
                        R30Cell4.setCellStyle(numberStyle);
                    } else {
                        R30Cell4.setCellValue("");
                        R30Cell4.setCellStyle(textStyle);
                    }
                    // // R30 Col H
                    // Cell R30Cell5 = row.createCell(7);
                    // if (record.getR30_net_amt() != null) {
                    // R30Cell5.setCellValue(record.getR30_net_amt().doubleValue());
                    // R30Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R30Cell5.setCellValue("");
                    // R30Cell5.setCellStyle(textStyle);
                    // }
                    // R30 Col I
                    Cell R30Cell6 = row.createCell(8);
                    if (record.getR30_net_amt_bwp() != null) {
                        R30Cell6.setCellValue(record.getR30_net_amt_bwp().doubleValue());
                        R30Cell6.setCellStyle(numberStyle);
                    } else {
                        R30Cell6.setCellValue("");
                        R30Cell6.setCellStyle(textStyle);
                    }
                    // R30 Col J
                    Cell R30Cell7 = row.createCell(9);
                    if (record.getR30_bal_sub() != null) {
                        R30Cell7.setCellValue(record.getR30_bal_sub().doubleValue());
                        R30Cell7.setCellStyle(numberStyle);
                    } else {
                        R30Cell7.setCellValue("");
                        R30Cell7.setCellStyle(textStyle);
                    }
                    // R30 Col K
                    Cell R30Cell8 = row.createCell(10);
                    if (record.getR30_bal_sub_bwp() != null) {
                        R30Cell8.setCellValue(record.getR30_bal_sub_bwp().doubleValue());
                        R30Cell8.setCellStyle(numberStyle);
                    } else {
                        R30Cell8.setCellValue("");
                        R30Cell8.setCellStyle(textStyle);
                    }
                    // R30 Col L
                    Cell R30Cell9 = row.createCell(11);
                    if (record.getR30_bal_sub_diaries() != null) {
                        R30Cell9.setCellValue(record.getR30_bal_sub_diaries().doubleValue());
                        R30Cell9.setCellStyle(numberStyle);
                    } else {
                        R30Cell9.setCellValue("");
                        R30Cell9.setCellStyle(textStyle);
                    }
                    // R30 Col M
                    Cell R30Cell10 = row.createCell(12);
                    if (record.getR30_bal_sub_diaries_bwp() != null) {
                        R30Cell10.setCellValue(record.getR30_bal_sub_diaries_bwp().doubleValue());
                        R30Cell10.setCellStyle(numberStyle);
                    } else {
                        R30Cell10.setCellValue("");
                        R30Cell10.setCellStyle(textStyle);
                    }
                    // row = sheet.getRow(30);
                    // Cell R31Cell1 = row.createCell(3);
                    // if (record.getR31_fig_bal_sheet() != null) {
                    // R31Cell1.setCellValue(record.getR31_fig_bal_sheet().doubleValue());
                    // R31Cell1.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell1.setCellValue("");
                    // R31Cell1.setCellStyle(textStyle);
                    // }

                    // // R31 Col E
                    // Cell R31Cell2 = row.createCell(4);
                    // if (record.getR31_fig_bal_sheet_bwp() != null) {
                    // R31Cell2.setCellValue(record.getR31_fig_bal_sheet_bwp().doubleValue());
                    // R31Cell2.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell2.setCellValue("");
                    // R31Cell2.setCellStyle(textStyle);
                    // }

                    // // R31 Col F
                    // Cell R31Cell3 = row.createCell(5);
                    // if (record.getR31_amt_statement_adj() != null) {
                    // R31Cell3.setCellValue(record.getR31_amt_statement_adj().doubleValue());
                    // R31Cell3.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell3.setCellValue("");
                    // R31Cell3.setCellStyle(textStyle);
                    // }
                    // // R31 Col G
                    // Cell R31Cell4 = row.createCell(6);
                    // if (record.getR31_amt_statement_adj_bwp() != null) {
                    // R31Cell4.setCellValue(record.getR31_amt_statement_adj_bwp().doubleValue());
                    // R31Cell4.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell4.setCellValue("");
                    // R31Cell4.setCellStyle(textStyle);
                    // }
                    // // R31 Col H
                    // Cell R31Cell5 = row.createCell(7);
                    // if (record.getR31_net_amt() != null) {
                    // R31Cell5.setCellValue(record.getR31_net_amt().doubleValue());
                    // R31Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell5.setCellValue("");
                    // R31Cell5.setCellStyle(textStyle);
                    // }
                    // // R31 Col I
                    // Cell R31Cell6 = row.createCell(8);
                    // if (record.getR31_net_amt_bwp() != null) {
                    // R31Cell6.setCellValue(record.getR31_net_amt_bwp().doubleValue());
                    // R31Cell6.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell6.setCellValue("");
                    // R31Cell6.setCellStyle(textStyle);
                    // }
                    // // R31 Col J
                    // Cell R31Cell7 = row.createCell(9);
                    // if (record.getR31_bal_sub() != null) {
                    // R31Cell7.setCellValue(record.getR31_bal_sub().doubleValue());
                    // R31Cell7.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell7.setCellValue("");
                    // R31Cell7.setCellStyle(textStyle);
                    // }
                    // // R31 Col K
                    // Cell R31Cell8 = row.createCell(10);
                    // if (record.getR31_bal_sub_bwp() != null) {
                    // R31Cell8.setCellValue(record.getR31_bal_sub_bwp().doubleValue());
                    // R31Cell8.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell8.setCellValue("");
                    // R31Cell8.setCellStyle(textStyle);
                    // }
                    // // R31 Col L
                    // Cell R31Cell9 = row.createCell(11);
                    // if (record.getR31_bal_sub_diaries() != null) {
                    // R31Cell9.setCellValue(record.getR31_bal_sub_diaries().doubleValue());
                    // R31Cell9.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell9.setCellValue("");
                    // R31Cell9.setCellStyle(textStyle);
                    // }
                    // // R31 Col M
                    // Cell R31Cell10 = row.createCell(12);
                    // if (record.getR31_bal_sub_diaries_bwp() != null) {
                    // R31Cell10.setCellValue(record.getR31_bal_sub_diaries_bwp().doubleValue());
                    // R31Cell10.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell10.setCellValue("");
                    // R31Cell10.setCellStyle(textStyle);
                    // }
                    row = sheet.getRow(39);
                    Cell R40Cell1 = row.createCell(3);
                    if (record.getR40_fig_bal_sheet() != null) {
                        R40Cell1.setCellValue(record.getR40_fig_bal_sheet().doubleValue());
                        R40Cell1.setCellStyle(numberStyle);
                    } else {
                        R40Cell1.setCellValue("");
                        R40Cell1.setCellStyle(textStyle);
                    }

                    // R40 Col E
                    Cell R40Cell2 = row.createCell(4);
                    if (record.getR40_fig_bal_sheet_bwp() != null) {
                        R40Cell2.setCellValue(record.getR40_fig_bal_sheet_bwp().doubleValue());
                        R40Cell2.setCellStyle(numberStyle);
                    } else {
                        R40Cell2.setCellValue("");
                        R40Cell2.setCellStyle(textStyle);
                    }

                    // R40 Col F
                    Cell R40Cell3 = row.createCell(5);
                    if (record.getR40_amt_statement_adj() != null) {
                        R40Cell3.setCellValue(record.getR40_amt_statement_adj().doubleValue());
                        R40Cell3.setCellStyle(numberStyle);
                    } else {
                        R40Cell3.setCellValue("");
                        R40Cell3.setCellStyle(textStyle);
                    }
                    // R40 Col G
                    Cell R40Cell4 = row.createCell(6);
                    if (record.getR40_amt_statement_adj_bwp() != null) {
                        R40Cell4.setCellValue(record.getR40_amt_statement_adj_bwp().doubleValue());
                        R40Cell4.setCellStyle(numberStyle);
                    } else {
                        R40Cell4.setCellValue("");
                        R40Cell4.setCellStyle(textStyle);
                    }
                    // // R40 Col H
                    // Cell R40Cell5 = row.createCell(7);
                    // if (record.getR40_net_amt() != null) {
                    // R40Cell5.setCellValue(record.getR40_net_amt().doubleValue());
                    // R40Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R40Cell5.setCellValue("");
                    // R40Cell5.setCellStyle(textStyle);
                    // }
                    // R40 Col I
                    Cell R40Cell6 = row.createCell(8);
                    if (record.getR40_net_amt_bwp() != null) {
                        R40Cell6.setCellValue(record.getR40_net_amt_bwp().doubleValue());
                        R40Cell6.setCellStyle(numberStyle);
                    } else {
                        R40Cell6.setCellValue("");
                        R40Cell6.setCellStyle(textStyle);
                    }
                    // R40 Col J
                    Cell R40Cell7 = row.createCell(9);
                    if (record.getR40_bal_sub() != null) {
                        R40Cell7.setCellValue(record.getR40_bal_sub().doubleValue());
                        R40Cell7.setCellStyle(numberStyle);
                    } else {
                        R40Cell7.setCellValue("");
                        R40Cell7.setCellStyle(textStyle);
                    }
                    // R40 Col K
                    Cell R40Cell8 = row.createCell(10);
                    if (record.getR40_bal_sub_bwp() != null) {
                        R40Cell8.setCellValue(record.getR40_bal_sub_bwp().doubleValue());
                        R40Cell8.setCellStyle(numberStyle);
                    } else {
                        R40Cell8.setCellValue("");
                        R40Cell8.setCellStyle(textStyle);
                    }
                    // R40 Col L
                    Cell R40Cell9 = row.createCell(11);
                    if (record.getR40_bal_sub_diaries() != null) {
                        R40Cell9.setCellValue(record.getR40_bal_sub_diaries().doubleValue());
                        R40Cell9.setCellStyle(numberStyle);
                    } else {
                        R40Cell9.setCellValue("");
                        R40Cell9.setCellStyle(textStyle);
                    }
                    // R40 Col M
                    Cell R40Cell10 = row.createCell(12);
                    if (record.getR40_bal_sub_diaries_bwp() != null) {
                        R40Cell10.setCellValue(record.getR40_bal_sub_diaries_bwp().doubleValue());
                        R40Cell10.setCellStyle(numberStyle);
                    } else {
                        R40Cell10.setCellValue("");
                        R40Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(40);
                    Cell R41Cell1 = row.createCell(3);
                    if (record.getR41_fig_bal_sheet() != null) {
                        R41Cell1.setCellValue(record.getR41_fig_bal_sheet().doubleValue());
                        R41Cell1.setCellStyle(numberStyle);
                    } else {
                        R41Cell1.setCellValue("");
                        R41Cell1.setCellStyle(textStyle);
                    }

                    // R41 Col E
                    Cell R41Cell2 = row.createCell(4);
                    if (record.getR41_fig_bal_sheet_bwp() != null) {
                        R41Cell2.setCellValue(record.getR41_fig_bal_sheet_bwp().doubleValue());
                        R41Cell2.setCellStyle(numberStyle);
                    } else {
                        R41Cell2.setCellValue("");
                        R41Cell2.setCellStyle(textStyle);
                    }

                    // R41 Col F
                    Cell R41Cell3 = row.createCell(5);
                    if (record.getR41_amt_statement_adj() != null) {
                        R41Cell3.setCellValue(record.getR41_amt_statement_adj().doubleValue());
                        R41Cell3.setCellStyle(numberStyle);
                    } else {
                        R41Cell3.setCellValue("");
                        R41Cell3.setCellStyle(textStyle);
                    }
                    // R41 Col G
                    Cell R41Cell4 = row.createCell(6);
                    if (record.getR41_amt_statement_adj_bwp() != null) {
                        R41Cell4.setCellValue(record.getR41_amt_statement_adj_bwp().doubleValue());
                        R41Cell4.setCellStyle(numberStyle);
                    } else {
                        R41Cell4.setCellValue("");
                        R41Cell4.setCellStyle(textStyle);
                    }
                    // // R41 Col H
                    // Cell R41Cell5 = row.createCell(7);
                    // if (record.getR41_net_amt() != null) {
                    // R41Cell5.setCellValue(record.getR41_net_amt().doubleValue());
                    // R41Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R41Cell5.setCellValue("");
                    // R41Cell5.setCellStyle(textStyle);
                    // }
                    // R41 Col I
                    Cell R41Cell6 = row.createCell(8);
                    if (record.getR41_net_amt_bwp() != null) {
                        R41Cell6.setCellValue(record.getR41_net_amt_bwp().doubleValue());
                        R41Cell6.setCellStyle(numberStyle);
                    } else {
                        R41Cell6.setCellValue("");
                        R41Cell6.setCellStyle(textStyle);
                    }
                    // R41 Col J
                    Cell R41Cell7 = row.createCell(9);
                    if (record.getR41_bal_sub() != null) {
                        R41Cell7.setCellValue(record.getR41_bal_sub().doubleValue());
                        R41Cell7.setCellStyle(numberStyle);
                    } else {
                        R41Cell7.setCellValue("");
                        R41Cell7.setCellStyle(textStyle);
                    }
                    // R41 Col K
                    Cell R41Cell8 = row.createCell(10);
                    if (record.getR41_bal_sub_bwp() != null) {
                        R41Cell8.setCellValue(record.getR41_bal_sub_bwp().doubleValue());
                        R41Cell8.setCellStyle(numberStyle);
                    } else {
                        R41Cell8.setCellValue("");
                        R41Cell8.setCellStyle(textStyle);
                    }
                    // R41 Col L
                    Cell R41Cell9 = row.createCell(11);
                    if (record.getR41_bal_sub_diaries() != null) {
                        R41Cell9.setCellValue(record.getR41_bal_sub_diaries().doubleValue());
                        R41Cell9.setCellStyle(numberStyle);
                    } else {
                        R41Cell9.setCellValue("");
                        R41Cell9.setCellStyle(textStyle);
                    }
                    // R41 Col M
                    Cell R41Cell10 = row.createCell(12);
                    if (record.getR41_bal_sub_diaries_bwp() != null) {
                        R41Cell10.setCellValue(record.getR41_bal_sub_diaries_bwp().doubleValue());
                        R41Cell10.setCellStyle(numberStyle);
                    } else {
                        R41Cell10.setCellValue("");
                        R41Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(41);
                    Cell R42Cell1 = row.createCell(3);
                    if (record1.getR42_fig_bal_sheet() != null) {
                        R42Cell1.setCellValue(record1.getR42_fig_bal_sheet().doubleValue());
                        R42Cell1.setCellStyle(numberStyle);
                    } else {
                        R42Cell1.setCellValue("");
                        R42Cell1.setCellStyle(textStyle);
                    }

                    // R42 Col E
                    Cell R42Cell2 = row.createCell(4);
                    if (record1.getR42_fig_bal_sheet_bwp() != null) {
                        R42Cell2.setCellValue(record1.getR42_fig_bal_sheet_bwp().doubleValue());
                        R42Cell2.setCellStyle(numberStyle);
                    } else {
                        R42Cell2.setCellValue("");
                        R42Cell2.setCellStyle(textStyle);
                    }

                    // R42 Col F
                    Cell R42Cell3 = row.createCell(5);
                    if (record1.getR42_amt_statement_adj() != null) {
                        R42Cell3.setCellValue(record1.getR42_amt_statement_adj().doubleValue());
                        R42Cell3.setCellStyle(numberStyle);
                    } else {
                        R42Cell3.setCellValue("");
                        R42Cell3.setCellStyle(textStyle);
                    }
                    // R42 Col G
                    Cell R42Cell4 = row.createCell(6);
                    if (record1.getR42_amt_statement_adj_bwp() != null) {
                        R42Cell4.setCellValue(record1.getR42_amt_statement_adj_bwp().doubleValue());
                        R42Cell4.setCellStyle(numberStyle);
                    } else {
                        R42Cell4.setCellValue("");
                        R42Cell4.setCellStyle(textStyle);
                    }
                    // // R42 Col H
                    // Cell R42Cell5 = row.createCell(7);
                    // if (record1.getR42_net_amt() != null) {
                    // R42Cell5.setCellValue(record1.getR42_net_amt().doubleValue());
                    // R42Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R42Cell5.setCellValue("");
                    // R42Cell5.setCellStyle(textStyle);
                    // }
                    // R42 Col I
                    Cell R42Cell6 = row.createCell(8);
                    if (record1.getR42_net_amt_bwp() != null) {
                        R42Cell6.setCellValue(record1.getR42_net_amt_bwp().doubleValue());
                        R42Cell6.setCellStyle(numberStyle);
                    } else {
                        R42Cell6.setCellValue("");
                        R42Cell6.setCellStyle(textStyle);
                    }
                    // R42 Col J
                    Cell R42Cell7 = row.createCell(9);
                    if (record1.getR42_bal_sub() != null) {
                        R42Cell7.setCellValue(record1.getR42_bal_sub().doubleValue());
                        R42Cell7.setCellStyle(numberStyle);
                    } else {
                        R42Cell7.setCellValue("");
                        R42Cell7.setCellStyle(textStyle);
                    }
                    // R42 Col K
                    Cell R42Cell8 = row.createCell(10);
                    if (record1.getR42_bal_sub_bwp() != null) {
                        R42Cell8.setCellValue(record1.getR42_bal_sub_bwp().doubleValue());
                        R42Cell8.setCellStyle(numberStyle);
                    } else {
                        R42Cell8.setCellValue("");
                        R42Cell8.setCellStyle(textStyle);
                    }
                    // R42 Col L
                    Cell R42Cell9 = row.createCell(11);
                    if (record1.getR42_bal_sub_diaries() != null) {
                        R42Cell9.setCellValue(record1.getR42_bal_sub_diaries().doubleValue());
                        R42Cell9.setCellStyle(numberStyle);
                    } else {
                        R42Cell9.setCellValue("");
                        R42Cell9.setCellStyle(textStyle);
                    }
                    // R42 Col M
                    Cell R42Cell10 = row.createCell(12);
                    if (record1.getR42_bal_sub_diaries_bwp() != null) {
                        R42Cell10.setCellValue(record1.getR42_bal_sub_diaries_bwp().doubleValue());
                        R42Cell10.setCellStyle(numberStyle);
                    } else {
                        R42Cell10.setCellValue("");
                        R42Cell10.setCellStyle(textStyle);
                    }
                    // row = sheet.getRow(42);
                    // Cell R43Cell1 = row.createCell(3);
                    // if (record.getR43_fig_bal_sheet() != null) {
                    // R43Cell1.setCellValue(record.getR43_fig_bal_sheet().doubleValue());
                    // R43Cell1.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell1.setCellValue("");
                    // R43Cell1.setCellStyle(textStyle);
                    // }

                    // // R43 Col E
                    // Cell R43Cell2 = row.createCell(4);
                    // if (record.getR43_fig_bal_sheet_bwp() != null) {
                    // R43Cell2.setCellValue(record.getR43_fig_bal_sheet_bwp().doubleValue());
                    // R43Cell2.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell2.setCellValue("");
                    // R43Cell2.setCellStyle(textStyle);
                    // }

                    // // R43 Col F
                    // Cell R43Cell3 = row.createCell(5);
                    // if (record.getR43_amt_statement_adj() != null) {
                    // R43Cell3.setCellValue(record.getR43_amt_statement_adj().doubleValue());
                    // R43Cell3.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell3.setCellValue("");
                    // R43Cell3.setCellStyle(textStyle);
                    // }
                    // // R43 Col G
                    // Cell R43Cell4 = row.createCell(6);
                    // if (record.getR43_amt_statement_adj_bwp() != null) {
                    // R43Cell4.setCellValue(record.getR43_amt_statement_adj_bwp().doubleValue());
                    // R43Cell4.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell4.setCellValue("");
                    // R43Cell4.setCellStyle(textStyle);
                    // }
                    // // R43 Col H
                    // Cell R43Cell5 = row.createCell(7);
                    // if (record.getR43_net_amt() != null) {
                    // R43Cell5.setCellValue(record.getR43_net_amt().doubleValue());
                    // R43Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell5.setCellValue("");
                    // R43Cell5.setCellStyle(textStyle);
                    // }
                    // // R43 Col I
                    // Cell R43Cell6 = row.createCell(8);
                    // if (record.getR43_net_amt_bwp() != null) {
                    // R43Cell6.setCellValue(record.getR43_net_amt_bwp().doubleValue());
                    // R43Cell6.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell6.setCellValue("");
                    // R43Cell6.setCellStyle(textStyle);
                    // }
                    // // R43 Col J
                    // Cell R43Cell7 = row.createCell(9);
                    // if (record.getR43_bal_sub() != null) {
                    // R43Cell7.setCellValue(record.getR43_bal_sub().doubleValue());
                    // R43Cell7.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell7.setCellValue("");
                    // R43Cell7.setCellStyle(textStyle);
                    // }
                    // // R43 Col K
                    // Cell R43Cell8 = row.createCell(10);
                    // if (record.getR43_bal_sub_bwp() != null) {
                    // R43Cell8.setCellValue(record.getR43_bal_sub_bwp().doubleValue());
                    // R43Cell8.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell8.setCellValue("");
                    // R43Cell8.setCellStyle(textStyle);
                    // }
                    // // R43 Col L
                    // Cell R43Cell9 = row.createCell(11);
                    // if (record.getR43_bal_sub_diaries() != null) {
                    // R43Cell9.setCellValue(record.getR43_bal_sub_diaries().doubleValue());
                    // R43Cell9.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell9.setCellValue("");
                    // R43Cell9.setCellStyle(textStyle);
                    // }
                    // // R43 Col M
                    // Cell R43Cell10 = row.createCell(12);
                    // if (record.getR43_bal_sub_diaries_bwp() != null) {
                    // R43Cell10.setCellValue(record.getR43_bal_sub_diaries_bwp().doubleValue());
                    // R43Cell10.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell10.setCellValue("");
                    // R43Cell10.setCellStyle(textStyle);
                    // }

                    row = sheet.getRow(47);
                    Cell R48Cell1 = row.createCell(3);
                    if (record.getR48_fig_bal_sheet() != null) {
                        R48Cell1.setCellValue(record.getR48_fig_bal_sheet().doubleValue());
                        R48Cell1.setCellStyle(numberStyle);
                    } else {
                        R48Cell1.setCellValue("");
                        R48Cell1.setCellStyle(textStyle);
                    }

                    // R48 Col E
                    Cell R48Cell2 = row.createCell(4);
                    if (record.getR48_fig_bal_sheet_bwp() != null) {
                        R48Cell2.setCellValue(record.getR48_fig_bal_sheet_bwp().doubleValue());
                        R48Cell2.setCellStyle(numberStyle);
                    } else {
                        R48Cell2.setCellValue("");
                        R48Cell2.setCellStyle(textStyle);
                    }

                    // R48 Col F
                    Cell R48Cell3 = row.createCell(5);
                    if (record.getR48_amt_statement_adj() != null) {
                        R48Cell3.setCellValue(record.getR48_amt_statement_adj().doubleValue());
                        R48Cell3.setCellStyle(numberStyle);
                    } else {
                        R48Cell3.setCellValue("");
                        R48Cell3.setCellStyle(textStyle);
                    }
                    // R48 Col G
                    Cell R48Cell4 = row.createCell(6);
                    if (record.getR48_amt_statement_adj_bwp() != null) {
                        R48Cell4.setCellValue(record.getR48_amt_statement_adj_bwp().doubleValue());
                        R48Cell4.setCellStyle(numberStyle);
                    } else {
                        R48Cell4.setCellValue("");
                        R48Cell4.setCellStyle(textStyle);
                    }
                    // // R48 Col H
                    // Cell R48Cell5 = row.createCell(7);
                    // if (record.getR48_net_amt() != null) {
                    // R48Cell5.setCellValue(record.getR48_net_amt().doubleValue());
                    // R48Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R48Cell5.setCellValue("");
                    // R48Cell5.setCellStyle(textStyle);
                    // }
                    // R48 Col I
                    Cell R48Cell6 = row.createCell(8);
                    if (record.getR48_net_amt_bwp() != null) {
                        R48Cell6.setCellValue(record.getR48_net_amt_bwp().doubleValue());
                        R48Cell6.setCellStyle(numberStyle);
                    } else {
                        R48Cell6.setCellValue("");
                        R48Cell6.setCellStyle(textStyle);
                    }
                    // R48 Col J
                    Cell R48Cell7 = row.createCell(9);
                    if (record.getR48_bal_sub() != null) {
                        R48Cell7.setCellValue(record.getR48_bal_sub().doubleValue());
                        R48Cell7.setCellStyle(numberStyle);
                    } else {
                        R48Cell7.setCellValue("");
                        R48Cell7.setCellStyle(textStyle);
                    }
                    // R48 Col K
                    Cell R48Cell8 = row.createCell(10);
                    if (record.getR48_bal_sub_bwp() != null) {
                        R48Cell8.setCellValue(record.getR48_bal_sub_bwp().doubleValue());
                        R48Cell8.setCellStyle(numberStyle);
                    } else {
                        R48Cell8.setCellValue("");
                        R48Cell8.setCellStyle(textStyle);
                    }
                    // R48 Col L
                    Cell R48Cell9 = row.createCell(11);
                    if (record.getR48_bal_sub_diaries() != null) {
                        R48Cell9.setCellValue(record.getR48_bal_sub_diaries().doubleValue());
                        R48Cell9.setCellStyle(numberStyle);
                    } else {
                        R48Cell9.setCellValue("");
                        R48Cell9.setCellStyle(textStyle);
                    }
                    // R48 Col M
                    Cell R48Cell10 = row.createCell(12);
                    if (record.getR48_bal_sub_diaries_bwp() != null) {
                        R48Cell10.setCellValue(record.getR48_bal_sub_diaries_bwp().doubleValue());
                        R48Cell10.setCellStyle(numberStyle);
                    } else {
                        R48Cell10.setCellValue("");
                        R48Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(48);
                    Cell R49Cell1 = row.createCell(3);
                    if (record.getR49_fig_bal_sheet() != null) {
                        R49Cell1.setCellValue(record.getR49_fig_bal_sheet().doubleValue());
                        R49Cell1.setCellStyle(numberStyle);
                    } else {
                        R49Cell1.setCellValue("");
                        R49Cell1.setCellStyle(textStyle);
                    }

                    // R49 Col E
                    Cell R49Cell2 = row.createCell(4);
                    if (record.getR49_fig_bal_sheet_bwp() != null) {
                        R49Cell2.setCellValue(record.getR49_fig_bal_sheet_bwp().doubleValue());
                        R49Cell2.setCellStyle(numberStyle);
                    } else {
                        R49Cell2.setCellValue("");
                        R49Cell2.setCellStyle(textStyle);
                    }

                    // R49 Col F
                    Cell R49Cell3 = row.createCell(5);
                    if (record.getR49_amt_statement_adj() != null) {
                        R49Cell3.setCellValue(record.getR49_amt_statement_adj().doubleValue());
                        R49Cell3.setCellStyle(numberStyle);
                    } else {
                        R49Cell3.setCellValue("");
                        R49Cell3.setCellStyle(textStyle);
                    }
                    // R49 Col G
                    Cell R49Cell4 = row.createCell(6);
                    if (record.getR49_amt_statement_adj_bwp() != null) {
                        R49Cell4.setCellValue(record.getR49_amt_statement_adj_bwp().doubleValue());
                        R49Cell4.setCellStyle(numberStyle);
                    } else {
                        R49Cell4.setCellValue("");
                        R49Cell4.setCellStyle(textStyle);
                    }
                    // // R49 Col H
                    // Cell R49Cell5 = row.createCell(7);
                    // if (record.getR49_net_amt() != null) {
                    // R49Cell5.setCellValue(record.getR49_net_amt().doubleValue());
                    // R49Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R49Cell5.setCellValue("");
                    // R49Cell5.setCellStyle(textStyle);
                    // }
                    // R49 Col I
                    Cell R49Cell6 = row.createCell(8);
                    if (record.getR49_net_amt_bwp() != null) {
                        R49Cell6.setCellValue(record.getR49_net_amt_bwp().doubleValue());
                        R49Cell6.setCellStyle(numberStyle);
                    } else {
                        R49Cell6.setCellValue("");
                        R49Cell6.setCellStyle(textStyle);
                    }
                    // R49 Col J
                    Cell R49Cell7 = row.createCell(9);
                    if (record.getR49_bal_sub() != null) {
                        R49Cell7.setCellValue(record.getR49_bal_sub().doubleValue());
                        R49Cell7.setCellStyle(numberStyle);
                    } else {
                        R49Cell7.setCellValue("");
                        R49Cell7.setCellStyle(textStyle);
                    }
                    // R49 Col K
                    Cell R49Cell8 = row.createCell(10);
                    if (record.getR49_bal_sub_bwp() != null) {
                        R49Cell8.setCellValue(record.getR49_bal_sub_bwp().doubleValue());
                        R49Cell8.setCellStyle(numberStyle);
                    } else {
                        R49Cell8.setCellValue("");
                        R49Cell8.setCellStyle(textStyle);
                    }
                    // R49 Col L
                    Cell R49Cell9 = row.createCell(11);
                    if (record.getR49_bal_sub_diaries() != null) {
                        R49Cell9.setCellValue(record.getR49_bal_sub_diaries().doubleValue());
                        R49Cell9.setCellStyle(numberStyle);
                    } else {
                        R49Cell9.setCellValue("");
                        R49Cell9.setCellStyle(textStyle);
                    }
                    // R49 Col M
                    Cell R49Cell10 = row.createCell(12);
                    if (record.getR49_bal_sub_diaries_bwp() != null) {
                        R49Cell10.setCellValue(record.getR49_bal_sub_diaries_bwp().doubleValue());
                        R49Cell10.setCellStyle(numberStyle);
                    } else {
                        R49Cell10.setCellValue("");
                        R49Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(49);
                    Cell R50Cell1 = row.createCell(3);
                    if (record.getR50_fig_bal_sheet() != null) {
                        R50Cell1.setCellValue(record.getR50_fig_bal_sheet().doubleValue());
                        R50Cell1.setCellStyle(numberStyle);
                    } else {
                        R50Cell1.setCellValue("");
                        R50Cell1.setCellStyle(textStyle);
                    }

                    // R50 Col E
                    Cell R50Cell2 = row.createCell(4);
                    if (record.getR50_fig_bal_sheet_bwp() != null) {
                        R50Cell2.setCellValue(record.getR50_fig_bal_sheet_bwp().doubleValue());
                        R50Cell2.setCellStyle(numberStyle);
                    } else {
                        R50Cell2.setCellValue("");
                        R50Cell2.setCellStyle(textStyle);
                    }

                    // R50 Col F
                    Cell R50Cell3 = row.createCell(5);
                    if (record.getR50_amt_statement_adj() != null) {
                        R50Cell3.setCellValue(record.getR50_amt_statement_adj().doubleValue());
                        R50Cell3.setCellStyle(numberStyle);
                    } else {
                        R50Cell3.setCellValue("");
                        R50Cell3.setCellStyle(textStyle);
                    }
                    // R50 Col G
                    Cell R50Cell4 = row.createCell(6);
                    if (record.getR50_amt_statement_adj_bwp() != null) {
                        R50Cell4.setCellValue(record.getR50_amt_statement_adj_bwp().doubleValue());
                        R50Cell4.setCellStyle(numberStyle);
                    } else {
                        R50Cell4.setCellValue("");
                        R50Cell4.setCellStyle(textStyle);
                    }
                    // // R50 Col H
                    // Cell R50Cell5 = row.createCell(7);
                    // if (record.getR50_net_amt() != null) {
                    // R50Cell5.setCellValue(record.getR50_net_amt().doubleValue());
                    // R50Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R50Cell5.setCellValue("");
                    // R50Cell5.setCellStyle(textStyle);
                    // }
                    // R50 Col I
                    Cell R50Cell6 = row.createCell(8);
                    if (record.getR50_net_amt_bwp() != null) {
                        R50Cell6.setCellValue(record.getR50_net_amt_bwp().doubleValue());
                        R50Cell6.setCellStyle(numberStyle);
                    } else {
                        R50Cell6.setCellValue("");
                        R50Cell6.setCellStyle(textStyle);
                    }
                    // R50 Col J
                    Cell R50Cell7 = row.createCell(9);
                    if (record.getR50_bal_sub() != null) {
                        R50Cell7.setCellValue(record.getR50_bal_sub().doubleValue());
                        R50Cell7.setCellStyle(numberStyle);
                    } else {
                        R50Cell7.setCellValue("");
                        R50Cell7.setCellStyle(textStyle);
                    }
                    // R50 Col K
                    Cell R50Cell8 = row.createCell(10);
                    if (record.getR50_bal_sub_bwp() != null) {
                        R50Cell8.setCellValue(record.getR50_bal_sub_bwp().doubleValue());
                        R50Cell8.setCellStyle(numberStyle);
                    } else {
                        R50Cell8.setCellValue("");
                        R50Cell8.setCellStyle(textStyle);
                    }
                    // R50 Col L
                    Cell R50Cell9 = row.createCell(11);
                    if (record.getR50_bal_sub_diaries() != null) {
                        R50Cell9.setCellValue(record.getR50_bal_sub_diaries().doubleValue());
                        R50Cell9.setCellStyle(numberStyle);
                    } else {
                        R50Cell9.setCellValue("");
                        R50Cell9.setCellStyle(textStyle);
                    }
                    // R50 Col M
                    Cell R50Cell10 = row.createCell(12);
                    if (record.getR50_bal_sub_diaries_bwp() != null) {
                        R50Cell10.setCellValue(record.getR50_bal_sub_diaries_bwp().doubleValue());
                        R50Cell10.setCellStyle(numberStyle);
                    } else {
                        R50Cell10.setCellValue("");
                        R50Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(50);
                    Cell R51Cell1 = row.createCell(3);
                    if (record.getR51_fig_bal_sheet() != null) {
                        R51Cell1.setCellValue(record.getR51_fig_bal_sheet().doubleValue());
                        R51Cell1.setCellStyle(numberStyle);
                    } else {
                        R51Cell1.setCellValue("");
                        R51Cell1.setCellStyle(textStyle);
                    }

                    // R51 Col E
                    Cell R51Cell2 = row.createCell(4);
                    if (record.getR51_fig_bal_sheet_bwp() != null) {
                        R51Cell2.setCellValue(record.getR51_fig_bal_sheet_bwp().doubleValue());
                        R51Cell2.setCellStyle(numberStyle);
                    } else {
                        R51Cell2.setCellValue("");
                        R51Cell2.setCellStyle(textStyle);
                    }

                    // R51 Col F
                    Cell R51Cell3 = row.createCell(5);
                    if (record.getR51_amt_statement_adj() != null) {
                        R51Cell3.setCellValue(record.getR51_amt_statement_adj().doubleValue());
                        R51Cell3.setCellStyle(numberStyle);
                    } else {
                        R51Cell3.setCellValue("");
                        R51Cell3.setCellStyle(textStyle);
                    }
                    // R51 Col G
                    Cell R51Cell4 = row.createCell(6);
                    if (record.getR51_amt_statement_adj_bwp() != null) {
                        R51Cell4.setCellValue(record.getR51_amt_statement_adj_bwp().doubleValue());
                        R51Cell4.setCellStyle(numberStyle);
                    } else {
                        R51Cell4.setCellValue("");
                        R51Cell4.setCellStyle(textStyle);
                    }
                    // // R51 Col H
                    // Cell R51Cell5 = row.createCell(7);
                    // if (record.getR51_net_amt() != null) {
                    // R51Cell5.setCellValue(record.getR51_net_amt().doubleValue());
                    // R51Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R51Cell5.setCellValue("");
                    // R51Cell5.setCellStyle(textStyle);
                    // }
                    // R51 Col I
                    Cell R51Cell6 = row.createCell(8);
                    if (record.getR51_net_amt_bwp() != null) {
                        R51Cell6.setCellValue(record.getR51_net_amt_bwp().doubleValue());
                        R51Cell6.setCellStyle(numberStyle);
                    } else {
                        R51Cell6.setCellValue("");
                        R51Cell6.setCellStyle(textStyle);
                    }
                    // R51 Col J
                    Cell R51Cell7 = row.createCell(9);
                    if (record.getR51_bal_sub() != null) {
                        R51Cell7.setCellValue(record.getR51_bal_sub().doubleValue());
                        R51Cell7.setCellStyle(numberStyle);
                    } else {
                        R51Cell7.setCellValue("");
                        R51Cell7.setCellStyle(textStyle);
                    }
                    // R51 Col K
                    Cell R51Cell8 = row.createCell(10);
                    if (record.getR51_bal_sub_bwp() != null) {
                        R51Cell8.setCellValue(record.getR51_bal_sub_bwp().doubleValue());
                        R51Cell8.setCellStyle(numberStyle);
                    } else {
                        R51Cell8.setCellValue("");
                        R51Cell8.setCellStyle(textStyle);
                    }
                    // R51 Col L
                    Cell R51Cell9 = row.createCell(11);
                    if (record.getR51_bal_sub_diaries() != null) {
                        R51Cell9.setCellValue(record.getR51_bal_sub_diaries().doubleValue());
                        R51Cell9.setCellStyle(numberStyle);
                    } else {
                        R51Cell9.setCellValue("");
                        R51Cell9.setCellStyle(textStyle);
                    }
                    // R51 Col M
                    Cell R51Cell10 = row.createCell(12);
                    if (record.getR51_bal_sub_diaries_bwp() != null) {
                        R51Cell10.setCellValue(record.getR51_bal_sub_diaries_bwp().doubleValue());
                        R51Cell10.setCellStyle(numberStyle);
                    } else {
                        R51Cell10.setCellValue("");
                        R51Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(51);
                    Cell R52Cell1 = row.createCell(3);
                    if (record.getR52_fig_bal_sheet() != null) {
                        R52Cell1.setCellValue(record.getR52_fig_bal_sheet().doubleValue());
                        R52Cell1.setCellStyle(numberStyle);
                    } else {
                        R52Cell1.setCellValue("");
                        R52Cell1.setCellStyle(textStyle);
                    }

                    // R52 Col E
                    Cell R52Cell2 = row.createCell(4);
                    if (record.getR52_fig_bal_sheet_bwp() != null) {
                        R52Cell2.setCellValue(record.getR52_fig_bal_sheet_bwp().doubleValue());
                        R52Cell2.setCellStyle(numberStyle);
                    } else {
                        R52Cell2.setCellValue("");
                        R52Cell2.setCellStyle(textStyle);
                    }

                    // R52 Col F
                    Cell R52Cell3 = row.createCell(5);
                    if (record.getR52_amt_statement_adj() != null) {
                        R52Cell3.setCellValue(record.getR52_amt_statement_adj().doubleValue());
                        R52Cell3.setCellStyle(numberStyle);
                    } else {
                        R52Cell3.setCellValue("");
                        R52Cell3.setCellStyle(textStyle);
                    }
                    // R52 Col G
                    Cell R52Cell4 = row.createCell(6);
                    if (record.getR52_amt_statement_adj_bwp() != null) {
                        R52Cell4.setCellValue(record.getR52_amt_statement_adj_bwp().doubleValue());
                        R52Cell4.setCellStyle(numberStyle);
                    } else {
                        R52Cell4.setCellValue("");
                        R52Cell4.setCellStyle(textStyle);
                    }
                    // // R52 Col H
                    // Cell R52Cell5 = row.createCell(7);
                    // if (record.getR52_net_amt() != null) {
                    // R52Cell5.setCellValue(record.getR52_net_amt().doubleValue());
                    // R52Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R52Cell5.setCellValue("");
                    // R52Cell5.setCellStyle(textStyle);
                    // }
                    // R52 Col I
                    Cell R52Cell6 = row.createCell(8);
                    if (record.getR52_net_amt_bwp() != null) {
                        R52Cell6.setCellValue(record.getR52_net_amt_bwp().doubleValue());
                        R52Cell6.setCellStyle(numberStyle);
                    } else {
                        R52Cell6.setCellValue("");
                        R52Cell6.setCellStyle(textStyle);
                    }
                    // R52 Col J
                    Cell R52Cell7 = row.createCell(9);
                    if (record.getR52_bal_sub() != null) {
                        R52Cell7.setCellValue(record.getR52_bal_sub().doubleValue());
                        R52Cell7.setCellStyle(numberStyle);
                    } else {
                        R52Cell7.setCellValue("");
                        R52Cell7.setCellStyle(textStyle);
                    }
                    // R52 Col K
                    Cell R52Cell8 = row.createCell(10);
                    if (record.getR52_bal_sub_bwp() != null) {
                        R52Cell8.setCellValue(record.getR52_bal_sub_bwp().doubleValue());
                        R52Cell8.setCellStyle(numberStyle);
                    } else {
                        R52Cell8.setCellValue("");
                        R52Cell8.setCellStyle(textStyle);
                    }
                    // R52 Col L
                    Cell R52Cell9 = row.createCell(11);
                    if (record.getR52_bal_sub_diaries() != null) {
                        R52Cell9.setCellValue(record.getR52_bal_sub_diaries().doubleValue());
                        R52Cell9.setCellStyle(numberStyle);
                    } else {
                        R52Cell9.setCellValue("");
                        R52Cell9.setCellStyle(textStyle);
                    }
                    // R52 Col M
                    Cell R52Cell10 = row.createCell(12);
                    if (record.getR52_bal_sub_diaries_bwp() != null) {
                        R52Cell10.setCellValue(record.getR52_bal_sub_diaries_bwp().doubleValue());
                        R52Cell10.setCellStyle(numberStyle);
                    } else {
                        R52Cell10.setCellValue("");
                        R52Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(52);
                    Cell R53Cell1 = row.createCell(3);
                    if (record.getR53_fig_bal_sheet() != null) {
                        R53Cell1.setCellValue(record.getR53_fig_bal_sheet().doubleValue());
                        R53Cell1.setCellStyle(numberStyle);
                    } else {
                        R53Cell1.setCellValue("");
                        R53Cell1.setCellStyle(textStyle);
                    }

                    // R53 Col E
                    Cell R53Cell2 = row.createCell(4);
                    if (record.getR53_fig_bal_sheet_bwp() != null) {
                        R53Cell2.setCellValue(record.getR53_fig_bal_sheet_bwp().doubleValue());
                        R53Cell2.setCellStyle(numberStyle);
                    } else {
                        R53Cell2.setCellValue("");
                        R53Cell2.setCellStyle(textStyle);
                    }

                    // R53 Col F
                    Cell R53Cell3 = row.createCell(5);
                    if (record.getR53_amt_statement_adj() != null) {
                        R53Cell3.setCellValue(record.getR53_amt_statement_adj().doubleValue());
                        R53Cell3.setCellStyle(numberStyle);
                    } else {
                        R53Cell3.setCellValue("");
                        R53Cell3.setCellStyle(textStyle);
                    }
                    // R53 Col G
                    Cell R53Cell4 = row.createCell(6);
                    if (record.getR53_amt_statement_adj_bwp() != null) {
                        R53Cell4.setCellValue(record.getR53_amt_statement_adj_bwp().doubleValue());
                        R53Cell4.setCellStyle(numberStyle);
                    } else {
                        R53Cell4.setCellValue("");
                        R53Cell4.setCellStyle(textStyle);
                    }
                    // // R53 Col H
                    // Cell R53Cell5 = row.createCell(7);
                    // if (record.getR53_net_amt() != null) {
                    // R53Cell5.setCellValue(record.getR53_net_amt().doubleValue());
                    // R53Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R53Cell5.setCellValue("");
                    // R53Cell5.setCellStyle(textStyle);
                    // }
                    // R53 Col I
                    Cell R53Cell6 = row.createCell(8);
                    if (record.getR53_net_amt_bwp() != null) {
                        R53Cell6.setCellValue(record.getR53_net_amt_bwp().doubleValue());
                        R53Cell6.setCellStyle(numberStyle);
                    } else {
                        R53Cell6.setCellValue("");
                        R53Cell6.setCellStyle(textStyle);
                    }
                    // R53 Col J
                    Cell R53Cell7 = row.createCell(9);
                    if (record.getR53_bal_sub() != null) {
                        R53Cell7.setCellValue(record.getR53_bal_sub().doubleValue());
                        R53Cell7.setCellStyle(numberStyle);
                    } else {
                        R53Cell7.setCellValue("");
                        R53Cell7.setCellStyle(textStyle);
                    }
                    // R53 Col K
                    Cell R53Cell8 = row.createCell(10);
                    if (record.getR53_bal_sub_bwp() != null) {
                        R53Cell8.setCellValue(record.getR53_bal_sub_bwp().doubleValue());
                        R53Cell8.setCellStyle(numberStyle);
                    } else {
                        R53Cell8.setCellValue("");
                        R53Cell8.setCellStyle(textStyle);
                    }
                    // R53 Col L
                    Cell R53Cell9 = row.createCell(11);
                    if (record.getR53_bal_sub_diaries() != null) {
                        R53Cell9.setCellValue(record.getR53_bal_sub_diaries().doubleValue());
                        R53Cell9.setCellStyle(numberStyle);
                    } else {
                        R53Cell9.setCellValue("");
                        R53Cell9.setCellStyle(textStyle);
                    }
                    // R53 Col M
                    Cell R53Cell10 = row.createCell(12);
                    if (record.getR53_bal_sub_diaries_bwp() != null) {
                        R53Cell10.setCellValue(record.getR53_bal_sub_diaries_bwp().doubleValue());
                        R53Cell10.setCellStyle(numberStyle);
                    } else {
                        R53Cell10.setCellValue("");
                        R53Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(53);
                    Cell R54Cell1 = row.createCell(3);
                    if (record1.getR54_fig_bal_sheet() != null) {
                        R54Cell1.setCellValue(record1.getR54_fig_bal_sheet().doubleValue());
                        R54Cell1.setCellStyle(numberStyle);
                    } else {
                        R54Cell1.setCellValue("");
                        R54Cell1.setCellStyle(textStyle);
                    }

                    // R54 Col E
                    Cell R54Cell2 = row.createCell(4);
                    if (record1.getR54_fig_bal_sheet_bwp() != null) {
                        R54Cell2.setCellValue(record1.getR54_fig_bal_sheet_bwp().doubleValue());
                        R54Cell2.setCellStyle(numberStyle);
                    } else {
                        R54Cell2.setCellValue("");
                        R54Cell2.setCellStyle(textStyle);
                    }

                    // R54 Col F
                    Cell R54Cell3 = row.createCell(5);
                    if (record1.getR54_amt_statement_adj() != null) {
                        R54Cell3.setCellValue(record1.getR54_amt_statement_adj().doubleValue());
                        R54Cell3.setCellStyle(numberStyle);
                    } else {
                        R54Cell3.setCellValue("");
                        R54Cell3.setCellStyle(textStyle);
                    }
                    // R54 Col G
                    Cell R54Cell4 = row.createCell(6);
                    if (record1.getR54_amt_statement_adj_bwp() != null) {
                        R54Cell4.setCellValue(record1.getR54_amt_statement_adj_bwp().doubleValue());
                        R54Cell4.setCellStyle(numberStyle);
                    } else {
                        R54Cell4.setCellValue("");
                        R54Cell4.setCellStyle(textStyle);
                    }
                    // // R54 Col H
                    // Cell R54Cell5 = row.createCell(7);
                    // if (record1.getR54_net_amt() != null) {
                    // R54Cell5.setCellValue(record1.getR54_net_amt().doubleValue());
                    // R54Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R54Cell5.setCellValue("");
                    // R54Cell5.setCellStyle(textStyle);
                    // }
                    // R54 Col I
                    Cell R54Cell6 = row.createCell(8);
                    if (record1.getR54_net_amt_bwp() != null) {
                        R54Cell6.setCellValue(record1.getR54_net_amt_bwp().doubleValue());
                        R54Cell6.setCellStyle(numberStyle);
                    } else {
                        R54Cell6.setCellValue("");
                        R54Cell6.setCellStyle(textStyle);
                    }
                    // R54 Col J
                    Cell R54Cell7 = row.createCell(9);
                    if (record1.getR54_bal_sub() != null) {
                        R54Cell7.setCellValue(record1.getR54_bal_sub().doubleValue());
                        R54Cell7.setCellStyle(numberStyle);
                    } else {
                        R54Cell7.setCellValue("");
                        R54Cell7.setCellStyle(textStyle);
                    }
                    // R54 Col K
                    Cell R54Cell8 = row.createCell(10);
                    if (record1.getR54_bal_sub_bwp() != null) {
                        R54Cell8.setCellValue(record1.getR54_bal_sub_bwp().doubleValue());
                        R54Cell8.setCellStyle(numberStyle);
                    } else {
                        R54Cell8.setCellValue("");
                        R54Cell8.setCellStyle(textStyle);
                    }
                    // R54 Col L
                    Cell R54Cell9 = row.createCell(11);
                    if (record1.getR54_bal_sub_diaries() != null) {
                        R54Cell9.setCellValue(record1.getR54_bal_sub_diaries().doubleValue());
                        R54Cell9.setCellStyle(numberStyle);
                    } else {
                        R54Cell9.setCellValue("");
                        R54Cell9.setCellStyle(textStyle);
                    }
                    // R54 Col M
                    Cell R54Cell10 = row.createCell(12);
                    if (record1.getR54_bal_sub_diaries_bwp() != null) {
                        R54Cell10.setCellValue(record1.getR54_bal_sub_diaries_bwp().doubleValue());
                        R54Cell10.setCellStyle(numberStyle);
                    } else {
                        R54Cell10.setCellValue("");
                        R54Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(54);
                    Cell R55Cell1 = row.createCell(3);
                    if (record.getR55_fig_bal_sheet() != null) {
                        R55Cell1.setCellValue(record.getR55_fig_bal_sheet().doubleValue());
                        R55Cell1.setCellStyle(numberStyle);
                    } else {
                        R55Cell1.setCellValue("");
                        R55Cell1.setCellStyle(textStyle);
                    }

                    // R55 Col E
                    Cell R55Cell2 = row.createCell(4);
                    if (record.getR55_fig_bal_sheet_bwp() != null) {
                        R55Cell2.setCellValue(record.getR55_fig_bal_sheet_bwp().doubleValue());
                        R55Cell2.setCellStyle(numberStyle);
                    } else {
                        R55Cell2.setCellValue("");
                        R55Cell2.setCellStyle(textStyle);
                    }

                    // R55 Col F
                    Cell R55Cell3 = row.createCell(5);
                    if (record.getR55_amt_statement_adj() != null) {
                        R55Cell3.setCellValue(record.getR55_amt_statement_adj().doubleValue());
                        R55Cell3.setCellStyle(numberStyle);
                    } else {
                        R55Cell3.setCellValue("");
                        R55Cell3.setCellStyle(textStyle);
                    }
                    // R55 Col G
                    Cell R55Cell4 = row.createCell(6);
                    if (record.getR55_amt_statement_adj_bwp() != null) {
                        R55Cell4.setCellValue(record.getR55_amt_statement_adj_bwp().doubleValue());
                        R55Cell4.setCellStyle(numberStyle);
                    } else {
                        R55Cell4.setCellValue("");
                        R55Cell4.setCellStyle(textStyle);
                    }
                    // // R55 Col H
                    // Cell R55Cell5 = row.createCell(7);
                    // if (record.getR55_net_amt() != null) {
                    // R55Cell5.setCellValue(record.getR55_net_amt().doubleValue());
                    // R55Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R55Cell5.setCellValue("");
                    // R55Cell5.setCellStyle(textStyle);
                    // }
                    // R55 Col I
                    Cell R55Cell6 = row.createCell(8);
                    if (record.getR55_net_amt_bwp() != null) {
                        R55Cell6.setCellValue(record.getR55_net_amt_bwp().doubleValue());
                        R55Cell6.setCellStyle(numberStyle);
                    } else {
                        R55Cell6.setCellValue("");
                        R55Cell6.setCellStyle(textStyle);
                    }
                    // R55 Col J
                    Cell R55Cell7 = row.createCell(9);
                    if (record.getR55_bal_sub() != null) {
                        R55Cell7.setCellValue(record.getR55_bal_sub().doubleValue());
                        R55Cell7.setCellStyle(numberStyle);
                    } else {
                        R55Cell7.setCellValue("");
                        R55Cell7.setCellStyle(textStyle);
                    }
                    // R55 Col K
                    Cell R55Cell8 = row.createCell(10);
                    if (record.getR55_bal_sub_bwp() != null) {
                        R55Cell8.setCellValue(record.getR55_bal_sub_bwp().doubleValue());
                        R55Cell8.setCellStyle(numberStyle);
                    } else {
                        R55Cell8.setCellValue("");
                        R55Cell8.setCellStyle(textStyle);
                    }
                    // R55 Col L
                    Cell R55Cell9 = row.createCell(11);
                    if (record.getR55_bal_sub_diaries() != null) {
                        R55Cell9.setCellValue(record.getR55_bal_sub_diaries().doubleValue());
                        R55Cell9.setCellStyle(numberStyle);
                    } else {
                        R55Cell9.setCellValue("");
                        R55Cell9.setCellStyle(textStyle);
                    }
                    // R55 Col M
                    Cell R55Cell10 = row.createCell(12);
                    if (record.getR55_bal_sub_diaries_bwp() != null) {
                        R55Cell10.setCellValue(record.getR55_bal_sub_diaries_bwp().doubleValue());
                        R55Cell10.setCellStyle(numberStyle);
                    } else {
                        R55Cell10.setCellValue("");
                        R55Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(55);
                    Cell R56Cell1 = row.createCell(3);
                    if (record.getR56_fig_bal_sheet() != null) {
                        R56Cell1.setCellValue(record.getR56_fig_bal_sheet().doubleValue());
                        R56Cell1.setCellStyle(numberStyle);
                    } else {
                        R56Cell1.setCellValue("");
                        R56Cell1.setCellStyle(textStyle);
                    }

                    // R56 Col E
                    Cell R56Cell2 = row.createCell(4);
                    if (record.getR56_fig_bal_sheet_bwp() != null) {
                        R56Cell2.setCellValue(record.getR56_fig_bal_sheet_bwp().doubleValue());
                        R56Cell2.setCellStyle(numberStyle);
                    } else {
                        R56Cell2.setCellValue("");
                        R56Cell2.setCellStyle(textStyle);
                    }

                    // R56 Col F
                    Cell R56Cell3 = row.createCell(5);
                    if (record.getR56_amt_statement_adj() != null) {
                        R56Cell3.setCellValue(record.getR56_amt_statement_adj().doubleValue());
                        R56Cell3.setCellStyle(numberStyle);
                    } else {
                        R56Cell3.setCellValue("");
                        R56Cell3.setCellStyle(textStyle);
                    }
                    // R56 Col G
                    Cell R56Cell4 = row.createCell(6);
                    if (record.getR56_amt_statement_adj_bwp() != null) {
                        R56Cell4.setCellValue(record.getR56_amt_statement_adj_bwp().doubleValue());
                        R56Cell4.setCellStyle(numberStyle);
                    } else {
                        R56Cell4.setCellValue("");
                        R56Cell4.setCellStyle(textStyle);
                    }
                    // // R56 Col H
                    // Cell R56Cell5 = row.createCell(7);
                    // if (record.getR56_net_amt() != null) {
                    // R56Cell5.setCellValue(record.getR56_net_amt().doubleValue());
                    // R56Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R56Cell5.setCellValue("");
                    // R56Cell5.setCellStyle(textStyle);
                    // }
                    // R56 Col I
                    Cell R56Cell6 = row.createCell(8);
                    if (record.getR56_net_amt_bwp() != null) {
                        R56Cell6.setCellValue(record.getR56_net_amt_bwp().doubleValue());
                        R56Cell6.setCellStyle(numberStyle);
                    } else {
                        R56Cell6.setCellValue("");
                        R56Cell6.setCellStyle(textStyle);
                    }
                    // R56 Col J
                    Cell R56Cell7 = row.createCell(9);
                    if (record.getR56_bal_sub() != null) {
                        R56Cell7.setCellValue(record.getR56_bal_sub().doubleValue());
                        R56Cell7.setCellStyle(numberStyle);
                    } else {
                        R56Cell7.setCellValue("");
                        R56Cell7.setCellStyle(textStyle);
                    }
                    // R56 Col K
                    Cell R56Cell8 = row.createCell(10);
                    if (record.getR56_bal_sub_bwp() != null) {
                        R56Cell8.setCellValue(record.getR56_bal_sub_bwp().doubleValue());
                        R56Cell8.setCellStyle(numberStyle);
                    } else {
                        R56Cell8.setCellValue("");
                        R56Cell8.setCellStyle(textStyle);
                    }
                    // R56 Col L
                    Cell R56Cell9 = row.createCell(11);
                    if (record.getR56_bal_sub_diaries() != null) {
                        R56Cell9.setCellValue(record.getR56_bal_sub_diaries().doubleValue());
                        R56Cell9.setCellStyle(numberStyle);
                    } else {
                        R56Cell9.setCellValue("");
                        R56Cell9.setCellStyle(textStyle);
                    }
                    // R56 Col M
                    Cell R56Cell10 = row.createCell(12);
                    if (record.getR56_bal_sub_diaries_bwp() != null) {
                        R56Cell10.setCellValue(record.getR56_bal_sub_diaries_bwp().doubleValue());
                        R56Cell10.setCellStyle(numberStyle);
                    } else {
                        R56Cell10.setCellValue("");
                        R56Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(56);
                    Cell R57Cell1 = row.createCell(3);
                    if (record.getR57_fig_bal_sheet() != null) {
                        R57Cell1.setCellValue(record.getR57_fig_bal_sheet().doubleValue());
                        R57Cell1.setCellStyle(numberStyle);
                    } else {
                        R57Cell1.setCellValue("");
                        R57Cell1.setCellStyle(textStyle);
                    }

                    // R57 Col E
                    Cell R57Cell2 = row.createCell(4);
                    if (record.getR57_fig_bal_sheet_bwp() != null) {
                        R57Cell2.setCellValue(record.getR57_fig_bal_sheet_bwp().doubleValue());
                        R57Cell2.setCellStyle(numberStyle);
                    } else {
                        R57Cell2.setCellValue("");
                        R57Cell2.setCellStyle(textStyle);
                    }

                    // R57 Col F
                    Cell R57Cell3 = row.createCell(5);
                    if (record.getR57_amt_statement_adj() != null) {
                        R57Cell3.setCellValue(record.getR57_amt_statement_adj().doubleValue());
                        R57Cell3.setCellStyle(numberStyle);
                    } else {
                        R57Cell3.setCellValue("");
                        R57Cell3.setCellStyle(textStyle);
                    }
                    // R57 Col G
                    Cell R57Cell4 = row.createCell(6);
                    if (record.getR57_amt_statement_adj_bwp() != null) {
                        R57Cell4.setCellValue(record.getR57_amt_statement_adj_bwp().doubleValue());
                        R57Cell4.setCellStyle(numberStyle);
                    } else {
                        R57Cell4.setCellValue("");
                        R57Cell4.setCellStyle(textStyle);
                    }
                    // // R57 Col H
                    // Cell R57Cell5 = row.createCell(7);
                    // if (record.getR57_net_amt() != null) {
                    // R57Cell5.setCellValue(record.getR57_net_amt().doubleValue());
                    // R57Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R57Cell5.setCellValue("");
                    // R57Cell5.setCellStyle(textStyle);
                    // }
                    // R57 Col I
                    Cell R57Cell6 = row.createCell(8);
                    if (record.getR57_net_amt_bwp() != null) {
                        R57Cell6.setCellValue(record.getR57_net_amt_bwp().doubleValue());
                        R57Cell6.setCellStyle(numberStyle);
                    } else {
                        R57Cell6.setCellValue("");
                        R57Cell6.setCellStyle(textStyle);
                    }
                    // R57 Col J
                    Cell R57Cell7 = row.createCell(9);
                    if (record.getR57_bal_sub() != null) {
                        R57Cell7.setCellValue(record.getR57_bal_sub().doubleValue());
                        R57Cell7.setCellStyle(numberStyle);
                    } else {
                        R57Cell7.setCellValue("");
                        R57Cell7.setCellStyle(textStyle);
                    }
                    // R57 Col K
                    Cell R57Cell8 = row.createCell(10);
                    if (record.getR57_bal_sub_bwp() != null) {
                        R57Cell8.setCellValue(record.getR57_bal_sub_bwp().doubleValue());
                        R57Cell8.setCellStyle(numberStyle);
                    } else {
                        R57Cell8.setCellValue("");
                        R57Cell8.setCellStyle(textStyle);
                    }
                    // R57 Col L
                    Cell R57Cell9 = row.createCell(11);
                    if (record.getR57_bal_sub_diaries() != null) {
                        R57Cell9.setCellValue(record.getR57_bal_sub_diaries().doubleValue());
                        R57Cell9.setCellStyle(numberStyle);
                    } else {
                        R57Cell9.setCellValue("");
                        R57Cell9.setCellStyle(textStyle);
                    }
                    // R57 Col M
                    Cell R57Cell10 = row.createCell(12);
                    if (record.getR57_bal_sub_diaries_bwp() != null) {
                        R57Cell10.setCellValue(record.getR57_bal_sub_diaries_bwp().doubleValue());
                        R57Cell10.setCellStyle(numberStyle);
                    } else {
                        R57Cell10.setCellValue("");
                        R57Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(57);
                    Cell R58Cell1 = row.createCell(3);
                    if (record.getR58_fig_bal_sheet() != null) {
                        R58Cell1.setCellValue(record.getR58_fig_bal_sheet().doubleValue());
                        R58Cell1.setCellStyle(numberStyle);
                    } else {
                        R58Cell1.setCellValue("");
                        R58Cell1.setCellStyle(textStyle);
                    }

                    // R58 Col E
                    Cell R58Cell2 = row.createCell(4);
                    if (record.getR58_fig_bal_sheet_bwp() != null) {
                        R58Cell2.setCellValue(record.getR58_fig_bal_sheet_bwp().doubleValue());
                        R58Cell2.setCellStyle(numberStyle);
                    } else {
                        R58Cell2.setCellValue("");
                        R58Cell2.setCellStyle(textStyle);
                    }

                    // R58 Col F
                    Cell R58Cell3 = row.createCell(5);
                    if (record.getR58_amt_statement_adj() != null) {
                        R58Cell3.setCellValue(record.getR58_amt_statement_adj().doubleValue());
                        R58Cell3.setCellStyle(numberStyle);
                    } else {
                        R58Cell3.setCellValue("");
                        R58Cell3.setCellStyle(textStyle);
                    }
                    // R58 Col G
                    Cell R58Cell4 = row.createCell(6);
                    if (record.getR58_amt_statement_adj_bwp() != null) {
                        R58Cell4.setCellValue(record.getR58_amt_statement_adj_bwp().doubleValue());
                        R58Cell4.setCellStyle(numberStyle);
                    } else {
                        R58Cell4.setCellValue("");
                        R58Cell4.setCellStyle(textStyle);
                    }
                    // // R58 Col H
                    // Cell R58Cell5 = row.createCell(7);
                    // if (record.getR58_net_amt() != null) {
                    // R58Cell5.setCellValue(record.getR58_net_amt().doubleValue());
                    // R58Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R58Cell5.setCellValue("");
                    // R58Cell5.setCellStyle(textStyle);
                    // }
                    // R58 Col I
                    Cell R58Cell6 = row.createCell(8);
                    if (record.getR58_net_amt_bwp() != null) {
                        R58Cell6.setCellValue(record.getR58_net_amt_bwp().doubleValue());
                        R58Cell6.setCellStyle(numberStyle);
                    } else {
                        R58Cell6.setCellValue("");
                        R58Cell6.setCellStyle(textStyle);
                    }
                    // R58 Col J
                    Cell R58Cell7 = row.createCell(9);
                    if (record.getR58_bal_sub() != null) {
                        R58Cell7.setCellValue(record.getR58_bal_sub().doubleValue());
                        R58Cell7.setCellStyle(numberStyle);
                    } else {
                        R58Cell7.setCellValue("");
                        R58Cell7.setCellStyle(textStyle);
                    }
                    // R58 Col K
                    Cell R58Cell8 = row.createCell(10);
                    if (record.getR58_bal_sub_bwp() != null) {
                        R58Cell8.setCellValue(record.getR58_bal_sub_bwp().doubleValue());
                        R58Cell8.setCellStyle(numberStyle);
                    } else {
                        R58Cell8.setCellValue("");
                        R58Cell8.setCellStyle(textStyle);
                    }
                    // R58 Col L
                    Cell R58Cell9 = row.createCell(11);
                    if (record.getR58_bal_sub_diaries() != null) {
                        R58Cell9.setCellValue(record.getR58_bal_sub_diaries().doubleValue());
                        R58Cell9.setCellStyle(numberStyle);
                    } else {
                        R58Cell9.setCellValue("");
                        R58Cell9.setCellStyle(textStyle);
                    }
                    // R58 Col M
                    Cell R58Cell10 = row.createCell(12);
                    if (record.getR58_bal_sub_diaries_bwp() != null) {
                        R58Cell10.setCellValue(record.getR58_bal_sub_diaries_bwp().doubleValue());
                        R58Cell10.setCellStyle(numberStyle);
                    } else {
                        R58Cell10.setCellValue("");
                        R58Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(58);
                    Cell R59Cell1 = row.createCell(3);
                    if (record.getR59_fig_bal_sheet() != null) {
                        R59Cell1.setCellValue(record.getR59_fig_bal_sheet().doubleValue());
                        R59Cell1.setCellStyle(numberStyle);
                    } else {
                        R59Cell1.setCellValue("");
                        R59Cell1.setCellStyle(textStyle);
                    }

                    // R59 Col E
                    Cell R59Cell2 = row.createCell(4);
                    if (record.getR59_fig_bal_sheet_bwp() != null) {
                        R59Cell2.setCellValue(record.getR59_fig_bal_sheet_bwp().doubleValue());
                        R59Cell2.setCellStyle(numberStyle);
                    } else {
                        R59Cell2.setCellValue("");
                        R59Cell2.setCellStyle(textStyle);
                    }

                    // R59 Col F
                    Cell R59Cell3 = row.createCell(5);
                    if (record.getR59_amt_statement_adj() != null) {
                        R59Cell3.setCellValue(record.getR59_amt_statement_adj().doubleValue());
                        R59Cell3.setCellStyle(numberStyle);
                    } else {
                        R59Cell3.setCellValue("");
                        R59Cell3.setCellStyle(textStyle);
                    }
                    // R59 Col G
                    Cell R59Cell4 = row.createCell(6);
                    if (record.getR59_amt_statement_adj_bwp() != null) {
                        R59Cell4.setCellValue(record.getR59_amt_statement_adj_bwp().doubleValue());
                        R59Cell4.setCellStyle(numberStyle);
                    } else {
                        R59Cell4.setCellValue("");
                        R59Cell4.setCellStyle(textStyle);
                    }
                    // // R59 Col H
                    // Cell R59Cell5 = row.createCell(7);
                    // if (record.getR59_net_amt() != null) {
                    // R59Cell5.setCellValue(record.getR59_net_amt().doubleValue());
                    // R59Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R59Cell5.setCellValue("");
                    // R59Cell5.setCellStyle(textStyle);
                    // }
                    // R59 Col I
                    Cell R59Cell6 = row.createCell(8);
                    if (record.getR59_net_amt_bwp() != null) {
                        R59Cell6.setCellValue(record.getR59_net_amt_bwp().doubleValue());
                        R59Cell6.setCellStyle(numberStyle);
                    } else {
                        R59Cell6.setCellValue("");
                        R59Cell6.setCellStyle(textStyle);
                    }
                    // R59 Col J
                    Cell R59Cell7 = row.createCell(9);
                    if (record.getR59_bal_sub() != null) {
                        R59Cell7.setCellValue(record.getR59_bal_sub().doubleValue());
                        R59Cell7.setCellStyle(numberStyle);
                    } else {
                        R59Cell7.setCellValue("");
                        R59Cell7.setCellStyle(textStyle);
                    }
                    // R59 Col K
                    Cell R59Cell8 = row.createCell(10);
                    if (record.getR59_bal_sub_bwp() != null) {
                        R59Cell8.setCellValue(record.getR59_bal_sub_bwp().doubleValue());
                        R59Cell8.setCellStyle(numberStyle);
                    } else {
                        R59Cell8.setCellValue("");
                        R59Cell8.setCellStyle(textStyle);
                    }
                    // R59 Col L
                    Cell R59Cell9 = row.createCell(11);
                    if (record.getR59_bal_sub_diaries() != null) {
                        R59Cell9.setCellValue(record.getR59_bal_sub_diaries().doubleValue());
                        R59Cell9.setCellStyle(numberStyle);
                    } else {
                        R59Cell9.setCellValue("");
                        R59Cell9.setCellStyle(textStyle);
                    }
                    // R59 Col M
                    Cell R59Cell10 = row.createCell(12);
                    if (record.getR59_bal_sub_diaries_bwp() != null) {
                        R59Cell10.setCellValue(record.getR59_bal_sub_diaries_bwp().doubleValue());
                        R59Cell10.setCellStyle(numberStyle);
                    } else {
                        R59Cell10.setCellValue("");
                        R59Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(59);
                    Cell R60Cell1 = row.createCell(3);
                    if (record.getR60_fig_bal_sheet() != null) {
                        R60Cell1.setCellValue(record.getR60_fig_bal_sheet().doubleValue());
                        R60Cell1.setCellStyle(numberStyle);
                    } else {
                        R60Cell1.setCellValue("");
                        R60Cell1.setCellStyle(textStyle);
                    }

                    // R60 Col E
                    Cell R60Cell2 = row.createCell(4);
                    if (record.getR60_fig_bal_sheet_bwp() != null) {
                        R60Cell2.setCellValue(record.getR60_fig_bal_sheet_bwp().doubleValue());
                        R60Cell2.setCellStyle(numberStyle);
                    } else {
                        R60Cell2.setCellValue("");
                        R60Cell2.setCellStyle(textStyle);
                    }

                    // R60 Col F
                    Cell R60Cell3 = row.createCell(5);
                    if (record.getR60_amt_statement_adj() != null) {
                        R60Cell3.setCellValue(record.getR60_amt_statement_adj().doubleValue());
                        R60Cell3.setCellStyle(numberStyle);
                    } else {
                        R60Cell3.setCellValue("");
                        R60Cell3.setCellStyle(textStyle);
                    }
                    // R60 Col G
                    Cell R60Cell4 = row.createCell(6);
                    if (record.getR60_amt_statement_adj_bwp() != null) {
                        R60Cell4.setCellValue(record.getR60_amt_statement_adj_bwp().doubleValue());
                        R60Cell4.setCellStyle(numberStyle);
                    } else {
                        R60Cell4.setCellValue("");
                        R60Cell4.setCellStyle(textStyle);
                    }
                    // // R60 Col H
                    // Cell R60Cell5 = row.createCell(7);
                    // if (record.getR60_net_amt() != null) {
                    // R60Cell5.setCellValue(record.getR60_net_amt().doubleValue());
                    // R60Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R60Cell5.setCellValue("");
                    // R60Cell5.setCellStyle(textStyle);
                    // }
                    // R60 Col I
                    Cell R60Cell6 = row.createCell(8);
                    if (record.getR60_net_amt_bwp() != null) {
                        R60Cell6.setCellValue(record.getR60_net_amt_bwp().doubleValue());
                        R60Cell6.setCellStyle(numberStyle);
                    } else {
                        R60Cell6.setCellValue("");
                        R60Cell6.setCellStyle(textStyle);
                    }
                    // R60 Col J
                    Cell R60Cell7 = row.createCell(9);
                    if (record.getR60_bal_sub() != null) {
                        R60Cell7.setCellValue(record.getR60_bal_sub().doubleValue());
                        R60Cell7.setCellStyle(numberStyle);
                    } else {
                        R60Cell7.setCellValue("");
                        R60Cell7.setCellStyle(textStyle);
                    }
                    // R60 Col K
                    Cell R60Cell8 = row.createCell(10);
                    if (record.getR60_bal_sub_bwp() != null) {
                        R60Cell8.setCellValue(record.getR60_bal_sub_bwp().doubleValue());
                        R60Cell8.setCellStyle(numberStyle);
                    } else {
                        R60Cell8.setCellValue("");
                        R60Cell8.setCellStyle(textStyle);
                    }
                    // R60 Col L
                    Cell R60Cell9 = row.createCell(11);
                    if (record.getR60_bal_sub_diaries() != null) {
                        R60Cell9.setCellValue(record.getR60_bal_sub_diaries().doubleValue());
                        R60Cell9.setCellStyle(numberStyle);
                    } else {
                        R60Cell9.setCellValue("");
                        R60Cell9.setCellStyle(textStyle);
                    }
                    // R60 Col M
                    Cell R60Cell10 = row.createCell(12);
                    if (record.getR60_bal_sub_diaries_bwp() != null) {
                        R60Cell10.setCellValue(record.getR60_bal_sub_diaries_bwp().doubleValue());
                        R60Cell10.setCellStyle(numberStyle);
                    } else {
                        R60Cell10.setCellValue("");
                        R60Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(60);
                    Cell R61Cell1 = row.createCell(3);
                    if (record1.getR61_fig_bal_sheet() != null) {
                        R61Cell1.setCellValue(record1.getR61_fig_bal_sheet().doubleValue());
                        R61Cell1.setCellStyle(numberStyle);
                    } else {
                        R61Cell1.setCellValue("");
                        R61Cell1.setCellStyle(textStyle);
                    }

                    // R61 Col E
                    Cell R61Cell2 = row.createCell(4);
                    if (record1.getR61_fig_bal_sheet_bwp() != null) {
                        R61Cell2.setCellValue(record1.getR61_fig_bal_sheet_bwp().doubleValue());
                        R61Cell2.setCellStyle(numberStyle);
                    } else {
                        R61Cell2.setCellValue("");
                        R61Cell2.setCellStyle(textStyle);
                    }

                    // R61 Col F
                    Cell R61Cell3 = row.createCell(5);
                    if (record1.getR61_amt_statement_adj() != null) {
                        R61Cell3.setCellValue(record1.getR61_amt_statement_adj().doubleValue());
                        R61Cell3.setCellStyle(numberStyle);
                    } else {
                        R61Cell3.setCellValue("");
                        R61Cell3.setCellStyle(textStyle);
                    }
                    // R61 Col G
                    Cell R61Cell4 = row.createCell(6);
                    if (record1.getR61_amt_statement_adj_bwp() != null) {
                        R61Cell4.setCellValue(record1.getR61_amt_statement_adj_bwp().doubleValue());
                        R61Cell4.setCellStyle(numberStyle);
                    } else {
                        R61Cell4.setCellValue("");
                        R61Cell4.setCellStyle(textStyle);
                    }
                    // // R61 Col H
                    // Cell R61Cell5 = row.createCell(7);
                    // if (record1.getR61_net_amt() != null) {
                    // R61Cell5.setCellValue(record1.getR61_net_amt().doubleValue());
                    // R61Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R61Cell5.setCellValue("");
                    // R61Cell5.setCellStyle(textStyle);
                    // }
                    // R61 Col I
                    Cell R61Cell6 = row.createCell(8);
                    if (record1.getR61_net_amt_bwp() != null) {
                        R61Cell6.setCellValue(record1.getR61_net_amt_bwp().doubleValue());
                        R61Cell6.setCellStyle(numberStyle);
                    } else {
                        R61Cell6.setCellValue("");
                        R61Cell6.setCellStyle(textStyle);
                    }
                    // R61 Col J
                    Cell R61Cell7 = row.createCell(9);
                    if (record1.getR61_bal_sub() != null) {
                        R61Cell7.setCellValue(record1.getR61_bal_sub().doubleValue());
                        R61Cell7.setCellStyle(numberStyle);
                    } else {
                        R61Cell7.setCellValue("");
                        R61Cell7.setCellStyle(textStyle);
                    }
                    // R61 Col K
                    Cell R61Cell8 = row.createCell(10);
                    if (record1.getR61_bal_sub_bwp() != null) {
                        R61Cell8.setCellValue(record1.getR61_bal_sub_bwp().doubleValue());
                        R61Cell8.setCellStyle(numberStyle);
                    } else {
                        R61Cell8.setCellValue("");
                        R61Cell8.setCellStyle(textStyle);
                    }
                    // R61 Col L
                    Cell R61Cell9 = row.createCell(11);
                    if (record1.getR61_bal_sub_diaries() != null) {
                        R61Cell9.setCellValue(record1.getR61_bal_sub_diaries().doubleValue());
                        R61Cell9.setCellStyle(numberStyle);
                    } else {
                        R61Cell9.setCellValue("");
                        R61Cell9.setCellStyle(textStyle);
                    }
                    // R61 Col M
                    Cell R61Cell10 = row.createCell(12);
                    if (record1.getR61_bal_sub_diaries_bwp() != null) {
                        R61Cell10.setCellValue(record1.getR61_bal_sub_diaries_bwp().doubleValue());
                        R61Cell10.setCellStyle(numberStyle);
                    } else {
                        R61Cell10.setCellValue("");
                        R61Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(61);
                    Cell R62Cell1 = row.createCell(3);
                    if (record.getR62_fig_bal_sheet() != null) {
                        R62Cell1.setCellValue(record.getR62_fig_bal_sheet().doubleValue());
                        R62Cell1.setCellStyle(numberStyle);
                    } else {
                        R62Cell1.setCellValue("");
                        R62Cell1.setCellStyle(textStyle);
                    }

                    // R62 Col E
                    Cell R62Cell2 = row.createCell(4);
                    if (record.getR62_fig_bal_sheet_bwp() != null) {
                        R62Cell2.setCellValue(record.getR62_fig_bal_sheet_bwp().doubleValue());
                        R62Cell2.setCellStyle(numberStyle);
                    } else {
                        R62Cell2.setCellValue("");
                        R62Cell2.setCellStyle(textStyle);
                    }

                    // R62 Col F
                    Cell R62Cell3 = row.createCell(5);
                    if (record.getR62_amt_statement_adj() != null) {
                        R62Cell3.setCellValue(record.getR62_amt_statement_adj().doubleValue());
                        R62Cell3.setCellStyle(numberStyle);
                    } else {
                        R62Cell3.setCellValue("");
                        R62Cell3.setCellStyle(textStyle);
                    }
                    // R62 Col G
                    Cell R62Cell4 = row.createCell(6);
                    if (record.getR62_amt_statement_adj_bwp() != null) {
                        R62Cell4.setCellValue(record.getR62_amt_statement_adj_bwp().doubleValue());
                        R62Cell4.setCellStyle(numberStyle);
                    } else {
                        R62Cell4.setCellValue("");
                        R62Cell4.setCellStyle(textStyle);
                    }
                    // // R62 Col H
                    // Cell R62Cell5 = row.createCell(7);
                    // if (record.getR62_net_amt() != null) {
                    // R62Cell5.setCellValue(record.getR62_net_amt().doubleValue());
                    // R62Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R62Cell5.setCellValue("");
                    // R62Cell5.setCellStyle(textStyle);
                    // }
                    // R62 Col I
                    Cell R62Cell6 = row.createCell(8);
                    if (record.getR62_net_amt_bwp() != null) {
                        R62Cell6.setCellValue(record.getR62_net_amt_bwp().doubleValue());
                        R62Cell6.setCellStyle(numberStyle);
                    } else {
                        R62Cell6.setCellValue("");
                        R62Cell6.setCellStyle(textStyle);
                    }
                    // R62 Col J
                    Cell R62Cell7 = row.createCell(9);
                    if (record.getR62_bal_sub() != null) {
                        R62Cell7.setCellValue(record.getR62_bal_sub().doubleValue());
                        R62Cell7.setCellStyle(numberStyle);
                    } else {
                        R62Cell7.setCellValue("");
                        R62Cell7.setCellStyle(textStyle);
                    }
                    // R62 Col K
                    Cell R62Cell8 = row.createCell(10);
                    if (record.getR62_bal_sub_bwp() != null) {
                        R62Cell8.setCellValue(record.getR62_bal_sub_bwp().doubleValue());
                        R62Cell8.setCellStyle(numberStyle);
                    } else {
                        R62Cell8.setCellValue("");
                        R62Cell8.setCellStyle(textStyle);
                    }
                    // R62 Col L
                    Cell R62Cell9 = row.createCell(11);
                    if (record.getR62_bal_sub_diaries() != null) {
                        R62Cell9.setCellValue(record.getR62_bal_sub_diaries().doubleValue());
                        R62Cell9.setCellStyle(numberStyle);
                    } else {
                        R62Cell9.setCellValue("");
                        R62Cell9.setCellStyle(textStyle);
                    }
                    // R62 Col M
                    Cell R62Cell10 = row.createCell(12);
                    if (record.getR62_bal_sub_diaries_bwp() != null) {
                        R62Cell10.setCellValue(record.getR62_bal_sub_diaries_bwp().doubleValue());
                        R62Cell10.setCellStyle(numberStyle);
                    } else {
                        R62Cell10.setCellValue("");
                        R62Cell10.setCellStyle(textStyle);
                    }
                    // row = sheet.getRow(62);
                    // Cell R63Cell1 = row.createCell(3);
                    // if (record.getR63_fig_bal_sheet() != null) {
                    // R63Cell1.setCellValue(record.getR63_fig_bal_sheet().doubleValue());
                    // R63Cell1.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell1.setCellValue("");
                    // R63Cell1.setCellStyle(textStyle);
                    // }

                    // // R63 Col E
                    // Cell R63Cell2 = row.createCell(4);
                    // if (record.getR63_fig_bal_sheet_bwp() != null) {
                    // R63Cell2.setCellValue(record.getR63_fig_bal_sheet_bwp().doubleValue());
                    // R63Cell2.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell2.setCellValue("");
                    // R63Cell2.setCellStyle(textStyle);
                    // }

                    // // R63 Col F
                    // Cell R63Cell3 = row.createCell(5);
                    // if (record.getR63_amt_statement_adj() != null) {
                    // R63Cell3.setCellValue(record.getR63_amt_statement_adj().doubleValue());
                    // R63Cell3.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell3.setCellValue("");
                    // R63Cell3.setCellStyle(textStyle);
                    // }
                    // // R63 Col G
                    // Cell R63Cell4 = row.createCell(6);
                    // if (record.getR63_amt_statement_adj_bwp() != null) {
                    // R63Cell4.setCellValue(record.getR63_amt_statement_adj_bwp().doubleValue());
                    // R63Cell4.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell4.setCellValue("");
                    // R63Cell4.setCellStyle(textStyle);
                    // }
                    // // R63 Col H
                    // Cell R63Cell5 = row.createCell(7);
                    // if (record.getR63_net_amt() != null) {
                    // R63Cell5.setCellValue(record.getR63_net_amt().doubleValue());
                    // R63Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell5.setCellValue("");
                    // R63Cell5.setCellStyle(textStyle);
                    // }
                    // // R63 Col I
                    // Cell R63Cell6 = row.createCell(8);
                    // if (record.getR63_net_amt_bwp() != null) {
                    // R63Cell6.setCellValue(record.getR63_net_amt_bwp().doubleValue());
                    // R63Cell6.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell6.setCellValue("");
                    // R63Cell6.setCellStyle(textStyle);
                    // }
                    // // R63 Col J
                    // Cell R63Cell7 = row.createCell(9);
                    // if (record.getR63_bal_sub() != null) {
                    // R63Cell7.setCellValue(record.getR63_bal_sub().doubleValue());
                    // R63Cell7.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell7.setCellValue("");
                    // R63Cell7.setCellStyle(textStyle);
                    // }
                    // // R63 Col K
                    // Cell R63Cell8 = row.createCell(10);
                    // if (record.getR63_bal_sub_bwp() != null) {
                    // R63Cell8.setCellValue(record.getR63_bal_sub_bwp().doubleValue());
                    // R63Cell8.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell8.setCellValue("");
                    // R63Cell8.setCellStyle(textStyle);
                    // }
                    // // R63 Col L
                    // Cell R63Cell9 = row.createCell(11);
                    // if (record.getR63_bal_sub_diaries() != null) {
                    // R63Cell9.setCellValue(record.getR63_bal_sub_diaries().doubleValue());
                    // R63Cell9.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell9.setCellValue("");
                    // R63Cell9.setCellStyle(textStyle);
                    // }
                    // // R63 Col M
                    // Cell R63Cell10 = row.createCell(12);
                    // if (record.getR63_bal_sub_diaries_bwp() != null) {
                    // R63Cell10.setCellValue(record.getR63_bal_sub_diaries_bwp().doubleValue());
                    // R63Cell10.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell10.setCellValue("");
                    // R63Cell10.setCellStyle(textStyle);
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

    public byte[] getExcelPL_SCHSARCHIVAL(String filename, String reportId, String fromdate, String todate,
            String currency, String dtltype, String type, String version) throws Exception {

        logger.info("Service: Starting Excel generation process in memory.");

        if (type.equals("ARCHIVAL") & version != null) {

        }

        List<PL_SCHS_Archival_Summary_Entity> dataList = PL_SCHS_Archival_Summary_Repo
                .getdatabydateListarchival(dateformat.parse(todate), version);
        List<PL_SCHS_Manual_Archival_Summary_Entity> dataList1 = PL_SCHS_Manual_Archival_Summary_Repo
                .getdatabydateListarchival(dateformat.parse(todate), version);

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for PL_SCHS report. Returning empty result.");
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
                    PL_SCHS_Archival_Summary_Entity record = dataList.get(i);
                    PL_SCHS_Manual_Archival_Summary_Entity record1 = dataList1.get(i);

                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }
                    Cell R9Cell1 = row.createCell(3);
                    if (record.getR9_fig_bal_sheet() != null) {
                        R9Cell1.setCellValue(record.getR9_fig_bal_sheet().doubleValue());
                        R9Cell1.setCellStyle(numberStyle);
                    } else {
                        R9Cell1.setCellValue("");
                        R9Cell1.setCellStyle(textStyle);
                    }

                    // R9 Col E
                    Cell R9Cell2 = row.createCell(4);
                    if (record.getR9_fig_bal_sheet_bwp() != null) {
                        R9Cell2.setCellValue(record.getR9_fig_bal_sheet_bwp().doubleValue());
                        R9Cell2.setCellStyle(numberStyle);
                    } else {
                        R9Cell2.setCellValue("");
                        R9Cell2.setCellStyle(textStyle);
                    }

                    // R9 Col F
                    Cell R9Cell3 = row.createCell(5);
                    if (record.getR9_amt_statement_adj() != null) {
                        R9Cell3.setCellValue(record.getR9_amt_statement_adj().doubleValue());
                        R9Cell3.setCellStyle(numberStyle);
                    } else {
                        R9Cell3.setCellValue("");
                        R9Cell3.setCellStyle(textStyle);
                    }
                    // R9 Col G
                    Cell R9Cell4 = row.createCell(6);
                    if (record.getR9_amt_statement_adj_bwp() != null) {
                        R9Cell4.setCellValue(record.getR9_amt_statement_adj_bwp().doubleValue());
                        R9Cell4.setCellStyle(numberStyle);
                    } else {
                        R9Cell4.setCellValue("");
                        R9Cell4.setCellStyle(textStyle);
                    }
                    // // R9 Col H
                    // Cell R9Cell5 = row.createCell(7);
                    // if (record.getR9_net_amt() != null) {
                    // R9Cell5.setCellValue(record.getR9_net_amt().doubleValue());
                    // R9Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R9Cell5.setCellValue("");
                    // R9Cell5.setCellStyle(textStyle);
                    // }
                    // R9 Col I
                    Cell R9Cell6 = row.createCell(8);
                    if (record.getR9_net_amt_bwp() != null) {
                        R9Cell6.setCellValue(record.getR9_net_amt_bwp().doubleValue());
                        R9Cell6.setCellStyle(numberStyle);
                    } else {
                        R9Cell6.setCellValue("");
                        R9Cell6.setCellStyle(textStyle);
                    }
                    // R9 Col J
                    Cell R9Cell7 = row.createCell(9);
                    if (record.getR9_bal_sub() != null) {
                        R9Cell7.setCellValue(record.getR9_bal_sub().doubleValue());
                        R9Cell7.setCellStyle(numberStyle);
                    } else {
                        R9Cell7.setCellValue("");
                        R9Cell7.setCellStyle(textStyle);
                    }
                    // R9 Col K
                    Cell R9Cell8 = row.createCell(10);
                    if (record.getR9_bal_sub_bwp() != null) {
                        R9Cell8.setCellValue(record.getR9_bal_sub_bwp().doubleValue());
                        R9Cell8.setCellStyle(numberStyle);
                    } else {
                        R9Cell8.setCellValue("");
                        R9Cell8.setCellStyle(textStyle);
                    }
                    // R9 Col L
                    Cell R9Cell9 = row.createCell(11);
                    if (record.getR9_bal_sub_diaries() != null) {
                        R9Cell9.setCellValue(record.getR9_bal_sub_diaries().doubleValue());
                        R9Cell9.setCellStyle(numberStyle);
                    } else {
                        R9Cell9.setCellValue("");
                        R9Cell9.setCellStyle(textStyle);
                    }
                    // R9 Col M
                    Cell R9Cell10 = row.createCell(12);
                    if (record.getR9_bal_sub_diaries_bwp() != null) {
                        R9Cell10.setCellValue(record.getR9_bal_sub_diaries_bwp().doubleValue());
                        R9Cell10.setCellStyle(numberStyle);
                    } else {
                        R9Cell10.setCellValue("");
                        R9Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(9);
                    Cell R10Cell1 = row.createCell(3);
                    if (record.getR10_fig_bal_sheet() != null) {
                        R10Cell1.setCellValue(record.getR10_fig_bal_sheet().doubleValue());
                        R10Cell1.setCellStyle(numberStyle);
                    } else {
                        R10Cell1.setCellValue("");
                        R10Cell1.setCellStyle(textStyle);
                    }

                    // R10 Col E
                    Cell R10Cell2 = row.createCell(4);
                    if (record.getR10_fig_bal_sheet_bwp() != null) {
                        R10Cell2.setCellValue(record.getR10_fig_bal_sheet_bwp().doubleValue());
                        R10Cell2.setCellStyle(numberStyle);
                    } else {
                        R10Cell2.setCellValue("");
                        R10Cell2.setCellStyle(textStyle);
                    }

                    // R10 Col F
                    Cell R10Cell3 = row.createCell(5);
                    if (record.getR10_amt_statement_adj() != null) {
                        R10Cell3.setCellValue(record.getR10_amt_statement_adj().doubleValue());
                        R10Cell3.setCellStyle(numberStyle);
                    } else {
                        R10Cell3.setCellValue("");
                        R10Cell3.setCellStyle(textStyle);
                    }
                    // R10 Col G
                    Cell R10Cell4 = row.createCell(6);
                    if (record.getR10_amt_statement_adj_bwp() != null) {
                        R10Cell4.setCellValue(record.getR10_amt_statement_adj_bwp().doubleValue());
                        R10Cell4.setCellStyle(numberStyle);
                    } else {
                        R10Cell4.setCellValue("");
                        R10Cell4.setCellStyle(textStyle);
                    }
                    // R10 Col H
                    // Cell R10Cell5 = row.createCell(7);
                    // if (record.getR10_net_amt() != null) {
                    // R10Cell5.setCellValue(record.getR10_net_amt().doubleValue());
                    // R10Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R10Cell5.setCellValue("");
                    // R10Cell5.setCellStyle(textStyle);
                    // }
                    // R10 Col I
                    Cell R10Cell6 = row.createCell(8);
                    if (record.getR10_net_amt_bwp() != null) {
                        R10Cell6.setCellValue(record.getR10_net_amt_bwp().doubleValue());
                        R10Cell6.setCellStyle(numberStyle);
                    } else {
                        R10Cell6.setCellValue("");
                        R10Cell6.setCellStyle(textStyle);
                    }
                    // R10 Col J
                    Cell R10Cell7 = row.createCell(9);
                    if (record.getR10_bal_sub() != null) {
                        R10Cell7.setCellValue(record.getR10_bal_sub().doubleValue());
                        R10Cell7.setCellStyle(numberStyle);
                    } else {
                        R10Cell7.setCellValue("");
                        R10Cell7.setCellStyle(textStyle);
                    }
                    // R10 Col K
                    Cell R10Cell8 = row.createCell(10);
                    if (record.getR10_bal_sub_bwp() != null) {
                        R10Cell8.setCellValue(record.getR10_bal_sub_bwp().doubleValue());
                        R10Cell8.setCellStyle(numberStyle);
                    } else {
                        R10Cell8.setCellValue("");
                        R10Cell8.setCellStyle(textStyle);
                    }
                    // R10 Col L
                    Cell R10Cell9 = row.createCell(11);
                    if (record.getR10_bal_sub_diaries() != null) {
                        R10Cell9.setCellValue(record.getR10_bal_sub_diaries().doubleValue());
                        R10Cell9.setCellStyle(numberStyle);
                    } else {
                        R10Cell9.setCellValue("");
                        R10Cell9.setCellStyle(textStyle);
                    }
                    // R10 Col M
                    Cell R10Cell10 = row.createCell(12);
                    if (record.getR10_bal_sub_diaries_bwp() != null) {
                        R10Cell10.setCellValue(record.getR10_bal_sub_diaries_bwp().doubleValue());
                        R10Cell10.setCellStyle(numberStyle);
                    } else {
                        R10Cell10.setCellValue("");
                        R10Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(10);
                    Cell R11Cell1 = row.createCell(3);
                    if (record.getR11_fig_bal_sheet() != null) {
                        R11Cell1.setCellValue(record.getR11_fig_bal_sheet().doubleValue());
                        R11Cell1.setCellStyle(numberStyle);
                    } else {
                        R11Cell1.setCellValue("");
                        R11Cell1.setCellStyle(textStyle);
                    }

                    // R11 Col E
                    Cell R11Cell2 = row.createCell(4);
                    if (record.getR11_fig_bal_sheet_bwp() != null) {
                        R11Cell2.setCellValue(record.getR11_fig_bal_sheet_bwp().doubleValue());
                        R11Cell2.setCellStyle(numberStyle);
                    } else {
                        R11Cell2.setCellValue("");
                        R11Cell2.setCellStyle(textStyle);
                    }

                    // R11 Col F
                    Cell R11Cell3 = row.createCell(5);
                    if (record.getR11_amt_statement_adj() != null) {
                        R11Cell3.setCellValue(record.getR11_amt_statement_adj().doubleValue());
                        R11Cell3.setCellStyle(numberStyle);
                    } else {
                        R11Cell3.setCellValue("");
                        R11Cell3.setCellStyle(textStyle);
                    }
                    // R11 Col G
                    Cell R11Cell4 = row.createCell(6);
                    if (record.getR11_amt_statement_adj_bwp() != null) {
                        R11Cell4.setCellValue(record.getR11_amt_statement_adj_bwp().doubleValue());
                        R11Cell4.setCellStyle(numberStyle);
                    } else {
                        R11Cell4.setCellValue("");
                        R11Cell4.setCellStyle(textStyle);
                    }
                    // // R11 Col H
                    // Cell R11Cell5 = row.createCell(7);
                    // if (record.getR11_net_amt() != null) {
                    // R11Cell5.setCellValue(record.getR11_net_amt().doubleValue());
                    // R11Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R11Cell5.setCellValue("");
                    // R11Cell5.setCellStyle(textStyle);
                    // }
                    // R11 Col I
                    Cell R11Cell6 = row.createCell(8);
                    if (record.getR11_net_amt_bwp() != null) {
                        R11Cell6.setCellValue(record.getR11_net_amt_bwp().doubleValue());
                        R11Cell6.setCellStyle(numberStyle);
                    } else {
                        R11Cell6.setCellValue("");
                        R11Cell6.setCellStyle(textStyle);
                    }
                    // R11 Col J
                    Cell R11Cell7 = row.createCell(9);
                    if (record.getR11_bal_sub() != null) {
                        R11Cell7.setCellValue(record.getR11_bal_sub().doubleValue());
                        R11Cell7.setCellStyle(numberStyle);
                    } else {
                        R11Cell7.setCellValue("");
                        R11Cell7.setCellStyle(textStyle);
                    }
                    // R11 Col K
                    Cell R11Cell8 = row.createCell(10);
                    if (record.getR11_bal_sub_bwp() != null) {
                        R11Cell8.setCellValue(record.getR11_bal_sub_bwp().doubleValue());
                        R11Cell8.setCellStyle(numberStyle);
                    } else {
                        R11Cell8.setCellValue("");
                        R11Cell8.setCellStyle(textStyle);
                    }
                    // R11 Col L
                    Cell R11Cell9 = row.createCell(11);
                    if (record.getR11_bal_sub_diaries() != null) {
                        R11Cell9.setCellValue(record.getR11_bal_sub_diaries().doubleValue());
                        R11Cell9.setCellStyle(numberStyle);
                    } else {
                        R11Cell9.setCellValue("");
                        R11Cell9.setCellStyle(textStyle);
                    }
                    // R11 Col M
                    Cell R11Cell10 = row.createCell(12);
                    if (record.getR11_bal_sub_diaries_bwp() != null) {
                        R11Cell10.setCellValue(record.getR11_bal_sub_diaries_bwp().doubleValue());
                        R11Cell10.setCellStyle(numberStyle);
                    } else {
                        R11Cell10.setCellValue("");
                        R11Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(11);
                    Cell R12Cell1 = row.createCell(3);
                    if (record1.getR12_fig_bal_sheet() != null) {
                        R12Cell1.setCellValue(record1.getR12_fig_bal_sheet().doubleValue());
                        R12Cell1.setCellStyle(numberStyle);
                    } else {
                        R12Cell1.setCellValue("");
                        R12Cell1.setCellStyle(textStyle);
                    }

                    // R12 Col E
                    Cell R12Cell2 = row.createCell(4);
                    if (record1.getR12_fig_bal_sheet_bwp() != null) {
                        R12Cell2.setCellValue(record1.getR12_fig_bal_sheet_bwp().doubleValue());
                        R12Cell2.setCellStyle(numberStyle);
                    } else {
                        R12Cell2.setCellValue("");
                        R12Cell2.setCellStyle(textStyle);
                    }

                    // R12 Col F
                    Cell R12Cell3 = row.createCell(5);
                    if (record1.getR12_amt_statement_adj() != null) {
                        R12Cell3.setCellValue(record1.getR12_amt_statement_adj().doubleValue());
                        R12Cell3.setCellStyle(numberStyle);
                    } else {
                        R12Cell3.setCellValue("");
                        R12Cell3.setCellStyle(textStyle);
                    }
                    // R12 Col G
                    Cell R12Cell4 = row.createCell(6);
                    if (record1.getR12_amt_statement_adj_bwp() != null) {
                        R12Cell4.setCellValue(record1.getR12_amt_statement_adj_bwp().doubleValue());
                        R12Cell4.setCellStyle(numberStyle);
                    } else {
                        R12Cell4.setCellValue("");
                        R12Cell4.setCellStyle(textStyle);
                    }
                    // R12 Col H
                    // Cell R12Cell5 = row.createCell(7);
                    // if (record1.getR12_net_amt() != null) {
                    // R12Cell5.setCellValue(record1.getR12_net_amt().doubleValue());
                    // R12Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R12Cell5.setCellValue("");
                    // R12Cell5.setCellStyle(textStyle);
                    // }
                    // R12 Col I
                    Cell R12Cell6 = row.createCell(8);
                    if (record1.getR12_net_amt_bwp() != null) {
                        R12Cell6.setCellValue(record1.getR12_net_amt_bwp().doubleValue());
                        R12Cell6.setCellStyle(numberStyle);
                    } else {
                        R12Cell6.setCellValue("");
                        R12Cell6.setCellStyle(textStyle);
                    }
                    // R12 Col J
                    Cell R12Cell7 = row.createCell(9);
                    if (record1.getR12_bal_sub() != null) {
                        R12Cell7.setCellValue(record1.getR12_bal_sub().doubleValue());
                        R12Cell7.setCellStyle(numberStyle);
                    } else {
                        R12Cell7.setCellValue("");
                        R12Cell7.setCellStyle(textStyle);
                    }
                    // R12 Col K
                    Cell R12Cell8 = row.createCell(10);
                    if (record1.getR12_bal_sub_bwp() != null) {
                        R12Cell8.setCellValue(record1.getR12_bal_sub_bwp().doubleValue());
                        R12Cell8.setCellStyle(numberStyle);
                    } else {
                        R12Cell8.setCellValue("");
                        R12Cell8.setCellStyle(textStyle);
                    }
                    // R12 Col L
                    Cell R12Cell9 = row.createCell(11);
                    if (record1.getR12_bal_sub_diaries() != null) {
                        R12Cell9.setCellValue(record1.getR12_bal_sub_diaries().doubleValue());
                        R12Cell9.setCellStyle(numberStyle);
                    } else {
                        R12Cell9.setCellValue("");
                        R12Cell9.setCellStyle(textStyle);
                    }

                    // R12 Col M
                    Cell R12Cell10 = row.createCell(12);
                    if (record1.getR12_bal_sub_diaries_bwp() != null) {
                        R12Cell10.setCellValue(record1.getR12_bal_sub_diaries_bwp().doubleValue());
                        R12Cell10.setCellStyle(numberStyle);
                    } else {
                        R12Cell10.setCellValue("");
                        R12Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(12);
                    Cell R13Cell1 = row.createCell(3);
                    if (record.getR13_fig_bal_sheet() != null) {
                        R13Cell1.setCellValue(record.getR13_fig_bal_sheet().doubleValue());
                        R13Cell1.setCellStyle(numberStyle);
                    } else {
                        R13Cell1.setCellValue("");
                        R13Cell1.setCellStyle(textStyle);
                    }

                    // R13 Col E
                    Cell R13Cell2 = row.createCell(4);
                    if (record.getR13_fig_bal_sheet_bwp() != null) {
                        R13Cell2.setCellValue(record.getR13_fig_bal_sheet_bwp().doubleValue());
                        R13Cell2.setCellStyle(numberStyle);
                    } else {
                        R13Cell2.setCellValue("");
                        R13Cell2.setCellStyle(textStyle);
                    }

                    // R13 Col F
                    Cell R13Cell3 = row.createCell(5);
                    if (record.getR13_amt_statement_adj() != null) {
                        R13Cell3.setCellValue(record.getR13_amt_statement_adj().doubleValue());
                        R13Cell3.setCellStyle(numberStyle);
                    } else {
                        R13Cell3.setCellValue("");
                        R13Cell3.setCellStyle(textStyle);
                    }
                    // R13 Col G
                    Cell R13Cell4 = row.createCell(6);
                    if (record.getR13_amt_statement_adj_bwp() != null) {
                        R13Cell4.setCellValue(record.getR13_amt_statement_adj_bwp().doubleValue());
                        R13Cell4.setCellStyle(numberStyle);
                    } else {
                        R13Cell4.setCellValue("");
                        R13Cell4.setCellStyle(textStyle);
                    }
                    // R13 Col H
                    Cell R13Cell5 = row.createCell(7);
                    if (record.getR13_net_amt() != null) {
                        R13Cell5.setCellValue(record.getR13_net_amt().doubleValue());
                        R13Cell5.setCellStyle(numberStyle);
                    } else {
                        R13Cell5.setCellValue("");
                        R13Cell5.setCellStyle(textStyle);
                    }
                    // R13 Col I
                    Cell R13Cell6 = row.createCell(8);
                    if (record.getR13_net_amt_bwp() != null) {
                        R13Cell6.setCellValue(record.getR13_net_amt_bwp().doubleValue());
                        R13Cell6.setCellStyle(numberStyle);
                    } else {
                        R13Cell6.setCellValue("");
                        R13Cell6.setCellStyle(textStyle);
                    }
                    // R13 Col J
                    Cell R13Cell7 = row.createCell(9);
                    if (record.getR13_bal_sub() != null) {
                        R13Cell7.setCellValue(record.getR13_bal_sub().doubleValue());
                        R13Cell7.setCellStyle(numberStyle);
                    } else {
                        R13Cell7.setCellValue("");
                        R13Cell7.setCellStyle(textStyle);
                    }
                    // R13 Col K
                    Cell R13Cell8 = row.createCell(10);
                    if (record.getR13_bal_sub_bwp() != null) {
                        R13Cell8.setCellValue(record.getR13_bal_sub_bwp().doubleValue());
                        R13Cell8.setCellStyle(numberStyle);
                    } else {
                        R13Cell8.setCellValue("");
                        R13Cell8.setCellStyle(textStyle);
                    }
                    // R13 Col L
                    Cell R13Cell9 = row.createCell(11);
                    if (record.getR13_bal_sub_diaries() != null) {
                        R13Cell9.setCellValue(record.getR13_bal_sub_diaries().doubleValue());
                        R13Cell9.setCellStyle(numberStyle);
                    } else {
                        R13Cell9.setCellValue("");
                        R13Cell9.setCellStyle(textStyle);
                    }
                    // R13 Col M
                    Cell R13Cell10 = row.createCell(12);
                    if (record.getR13_bal_sub_diaries_bwp() != null) {
                        R13Cell10.setCellValue(record.getR13_bal_sub_diaries_bwp().doubleValue());
                        R13Cell10.setCellStyle(numberStyle);
                    } else {
                        R13Cell10.setCellValue("");
                        R13Cell10.setCellStyle(textStyle);
                    }

                    row = sheet.getRow(16);
                    Cell R17Cell1 = row.createCell(3);
                    if (record.getR17_fig_bal_sheet() != null) {
                        R17Cell1.setCellValue(record.getR17_fig_bal_sheet().doubleValue());
                        R17Cell1.setCellStyle(numberStyle);
                    } else {
                        R17Cell1.setCellValue("");
                        R17Cell1.setCellStyle(textStyle);
                    }

                    // R17 Col E
                    Cell R17Cell2 = row.createCell(4);
                    if (record.getR17_fig_bal_sheet_bwp() != null) {
                        R17Cell2.setCellValue(record.getR17_fig_bal_sheet_bwp().doubleValue());
                        R17Cell2.setCellStyle(numberStyle);
                    } else {
                        R17Cell2.setCellValue("");
                        R17Cell2.setCellStyle(textStyle);
                    }

                    // R17 Col F
                    Cell R17Cell3 = row.createCell(5);
                    if (record.getR17_amt_statement_adj() != null) {
                        R17Cell3.setCellValue(record.getR17_amt_statement_adj().doubleValue());
                        R17Cell3.setCellStyle(numberStyle);
                    } else {
                        R17Cell3.setCellValue("");
                        R17Cell3.setCellStyle(textStyle);
                    }
                    // R17 Col G
                    Cell R17Cell4 = row.createCell(6);
                    if (record.getR17_amt_statement_adj_bwp() != null) {
                        R17Cell4.setCellValue(record.getR17_amt_statement_adj_bwp().doubleValue());
                        R17Cell4.setCellStyle(numberStyle);
                    } else {
                        R17Cell4.setCellValue("");
                        R17Cell4.setCellStyle(textStyle);
                    }
                    // R17 Col H
                    // Cell R17Cell5 = row.createCell(7);
                    // if (record.getR17_net_amt() != null) {
                    // R17Cell5.setCellValue(record.getR17_net_amt().doubleValue());
                    // R17Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R17Cell5.setCellValue("");
                    // R17Cell5.setCellStyle(textStyle);
                    // }
                    // R17 Col I
                    Cell R17Cell6 = row.createCell(8);
                    if (record.getR17_net_amt_bwp() != null) {
                        R17Cell6.setCellValue(record.getR17_net_amt_bwp().doubleValue());
                        R17Cell6.setCellStyle(numberStyle);
                    } else {
                        R17Cell6.setCellValue("");
                        R17Cell6.setCellStyle(textStyle);
                    }
                    // R17 Col J
                    Cell R17Cell7 = row.createCell(9);
                    if (record.getR17_bal_sub() != null) {
                        R17Cell7.setCellValue(record.getR17_bal_sub().doubleValue());
                        R17Cell7.setCellStyle(numberStyle);
                    } else {
                        R17Cell7.setCellValue("");
                        R17Cell7.setCellStyle(textStyle);
                    }
                    // R17 Col K
                    Cell R17Cell8 = row.createCell(10);
                    if (record.getR17_bal_sub_bwp() != null) {
                        R17Cell8.setCellValue(record.getR17_bal_sub_bwp().doubleValue());
                        R17Cell8.setCellStyle(numberStyle);
                    } else {
                        R17Cell8.setCellValue("");
                        R17Cell8.setCellStyle(textStyle);
                    }
                    // R17 Col L
                    Cell R17Cell9 = row.createCell(11);
                    if (record.getR17_bal_sub_diaries() != null) {
                        R17Cell9.setCellValue(record.getR17_bal_sub_diaries().doubleValue());
                        R17Cell9.setCellStyle(numberStyle);
                    } else {
                        R17Cell9.setCellValue("");
                        R17Cell9.setCellStyle(textStyle);
                    }
                    // R17 Col M
                    Cell R17Cell10 = row.createCell(12);
                    if (record.getR17_bal_sub_diaries_bwp() != null) {
                        R17Cell10.setCellValue(record.getR17_bal_sub_diaries_bwp().doubleValue());
                        R17Cell10.setCellStyle(numberStyle);
                    } else {
                        R17Cell10.setCellValue("");
                        R17Cell10.setCellStyle(textStyle);
                    }

                    row = sheet.getRow(17);
                    Cell R18Cell1 = row.createCell(3);
                    if (record1.getR18_fig_bal_sheet() != null) {
                        R18Cell1.setCellValue(record1.getR18_fig_bal_sheet().doubleValue());
                        R18Cell1.setCellStyle(numberStyle);
                    } else {
                        R18Cell1.setCellValue("");
                        R18Cell1.setCellStyle(textStyle);
                    }

                    // R18 Col E
                    Cell R18Cell2 = row.createCell(4);
                    if (record1.getR18_fig_bal_sheet_bwp() != null) {
                        R18Cell2.setCellValue(record1.getR18_fig_bal_sheet_bwp().doubleValue());
                        R18Cell2.setCellStyle(numberStyle);
                    } else {
                        R18Cell2.setCellValue("");
                        R18Cell2.setCellStyle(textStyle);
                    }

                    // R18 Col F
                    Cell R18Cell3 = row.createCell(5);
                    if (record1.getR18_amt_statement_adj() != null) {
                        R18Cell3.setCellValue(record1.getR18_amt_statement_adj().doubleValue());
                        R18Cell3.setCellStyle(numberStyle);
                    } else {
                        R18Cell3.setCellValue("");
                        R18Cell3.setCellStyle(textStyle);
                    }
                    // R18 Col G
                    Cell R18Cell4 = row.createCell(6);
                    if (record1.getR18_amt_statement_adj_bwp() != null) {
                        R18Cell4.setCellValue(record1.getR18_amt_statement_adj_bwp().doubleValue());
                        R18Cell4.setCellStyle(numberStyle);
                    } else {
                        R18Cell4.setCellValue("");
                        R18Cell4.setCellStyle(textStyle);
                    }
                    // // R18 Col H
                    // Cell R18Cell5 = row.createCell(7);
                    // if (record1.getR18_net_amt() != null) {
                    // R18Cell5.setCellValue(record1.getR18_net_amt().doubleValue());
                    // R18Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R18Cell5.setCellValue("");
                    // R18Cell5.setCellStyle(textStyle);
                    // }
                    // R18 Col I
                    Cell R18Cell6 = row.createCell(8);
                    if (record1.getR18_net_amt_bwp() != null) {
                        R18Cell6.setCellValue(record1.getR18_net_amt_bwp().doubleValue());
                        R18Cell6.setCellStyle(numberStyle);
                    } else {
                        R18Cell6.setCellValue("");
                        R18Cell6.setCellStyle(textStyle);
                    }
                    // R18 Col J
                    Cell R18Cell7 = row.createCell(9);
                    if (record1.getR18_bal_sub() != null) {
                        R18Cell7.setCellValue(record1.getR18_bal_sub().doubleValue());
                        R18Cell7.setCellStyle(numberStyle);
                    } else {
                        R18Cell7.setCellValue("");
                        R18Cell7.setCellStyle(textStyle);
                    }
                    // R18 Col K
                    Cell R18Cell8 = row.createCell(10);
                    if (record1.getR18_bal_sub_bwp() != null) {
                        R18Cell8.setCellValue(record1.getR18_bal_sub_bwp().doubleValue());
                        R18Cell8.setCellStyle(numberStyle);
                    } else {
                        R18Cell8.setCellValue("");
                        R18Cell8.setCellStyle(textStyle);
                    }
                    // R18 Col L
                    Cell R18Cell9 = row.createCell(11);
                    if (record1.getR18_bal_sub_diaries() != null) {
                        R18Cell9.setCellValue(record1.getR18_bal_sub_diaries().doubleValue());
                        R18Cell9.setCellStyle(numberStyle);
                    } else {
                        R18Cell9.setCellValue("");
                        R18Cell9.setCellStyle(textStyle);
                    }
                    // R18 Col M
                    Cell R18Cell10 = row.createCell(12);
                    if (record1.getR18_bal_sub_diaries_bwp() != null) {
                        R18Cell10.setCellValue(record1.getR18_bal_sub_diaries_bwp().doubleValue());
                        R18Cell10.setCellStyle(numberStyle);
                    } else {
                        R18Cell10.setCellValue("");
                        R18Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(18);
                    Cell R19Cell1 = row.createCell(3);
                    if (record1.getR19_fig_bal_sheet() != null) {
                        R19Cell1.setCellValue(record1.getR19_fig_bal_sheet().doubleValue());
                        R19Cell1.setCellStyle(numberStyle);
                    } else {
                        R19Cell1.setCellValue("");
                        R19Cell1.setCellStyle(textStyle);
                    }

                    // R19 Col E
                    Cell R19Cell2 = row.createCell(4);
                    if (record1.getR19_fig_bal_sheet_bwp() != null) {
                        R19Cell2.setCellValue(record1.getR19_fig_bal_sheet_bwp().doubleValue());
                        R19Cell2.setCellStyle(numberStyle);
                    } else {
                        R19Cell2.setCellValue("");
                        R19Cell2.setCellStyle(textStyle);
                    }

                    // R19 Col F
                    Cell R19Cell3 = row.createCell(5);
                    if (record1.getR19_amt_statement_adj() != null) {
                        R19Cell3.setCellValue(record1.getR19_amt_statement_adj().doubleValue());
                        R19Cell3.setCellStyle(numberStyle);
                    } else {
                        R19Cell3.setCellValue("");
                        R19Cell3.setCellStyle(textStyle);
                    }
                    // R19 Col G
                    Cell R19Cell4 = row.createCell(6);
                    if (record1.getR19_amt_statement_adj_bwp() != null) {
                        R19Cell4.setCellValue(record1.getR19_amt_statement_adj_bwp().doubleValue());
                        R19Cell4.setCellStyle(numberStyle);
                    } else {
                        R19Cell4.setCellValue("");
                        R19Cell4.setCellStyle(textStyle);
                    }
                    // R19 Col H
                    // Cell R19Cell5 = row.createCell(7);
                    // if (record1.getR19_net_amt() != null) {
                    // R19Cell5.setCellValue(record1.getR19_net_amt().doubleValue());
                    // R19Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R19Cell5.setCellValue("");
                    // R19Cell5.setCellStyle(textStyle);
                    // }
                    // R19 Col I
                    Cell R19Cell6 = row.createCell(8);
                    if (record1.getR19_net_amt_bwp() != null) {
                        R19Cell6.setCellValue(record1.getR19_net_amt_bwp().doubleValue());
                        R19Cell6.setCellStyle(numberStyle);
                    } else {
                        R19Cell6.setCellValue("");
                        R19Cell6.setCellStyle(textStyle);
                    }
                    // R19 Col J
                    Cell R19Cell7 = row.createCell(9);
                    if (record1.getR19_bal_sub() != null) {
                        R19Cell7.setCellValue(record1.getR19_bal_sub().doubleValue());
                        R19Cell7.setCellStyle(numberStyle);
                    } else {
                        R19Cell7.setCellValue("");
                        R19Cell7.setCellStyle(textStyle);
                    }
                    // R19 Col K
                    Cell R19Cell8 = row.createCell(10);
                    if (record1.getR19_bal_sub_bwp() != null) {
                        R19Cell8.setCellValue(record1.getR19_bal_sub_bwp().doubleValue());
                        R19Cell8.setCellStyle(numberStyle);
                    } else {
                        R19Cell8.setCellValue("");
                        R19Cell8.setCellStyle(textStyle);
                    }
                    // R19 Col L
                    Cell R19Cell9 = row.createCell(11);
                    if (record1.getR19_bal_sub_diaries() != null) {
                        R19Cell9.setCellValue(record1.getR19_bal_sub_diaries().doubleValue());
                        R19Cell9.setCellStyle(numberStyle);
                    } else {
                        R19Cell9.setCellValue("");
                        R19Cell9.setCellStyle(textStyle);
                    }
                    // R19 Col M
                    Cell R19Cell10 = row.createCell(12);
                    if (record1.getR19_bal_sub_diaries_bwp() != null) {
                        R19Cell10.setCellValue(record1.getR19_bal_sub_diaries_bwp().doubleValue());
                        R19Cell10.setCellStyle(numberStyle);
                    } else {
                        R19Cell10.setCellValue("");
                        R19Cell10.setCellStyle(textStyle);
                    }

                    row = sheet.getRow(19);
                    Cell R20Cell1 = row.createCell(3);
                    if (record.getR20_fig_bal_sheet() != null) {
                        R20Cell1.setCellValue(record.getR20_fig_bal_sheet().doubleValue());
                        R20Cell1.setCellStyle(numberStyle);
                    } else {
                        R20Cell1.setCellValue("");
                        R20Cell1.setCellStyle(textStyle);
                    }

                    // R20 Col E
                    Cell R20Cell2 = row.createCell(4);
                    if (record.getR20_fig_bal_sheet_bwp() != null) {
                        R20Cell2.setCellValue(record.getR20_fig_bal_sheet_bwp().doubleValue());
                        R20Cell2.setCellStyle(numberStyle);
                    } else {
                        R20Cell2.setCellValue("");
                        R20Cell2.setCellStyle(textStyle);
                    }

                    // R20 Col F
                    Cell R20Cell3 = row.createCell(5);
                    if (record.getR20_amt_statement_adj() != null) {
                        R20Cell3.setCellValue(record.getR20_amt_statement_adj().doubleValue());
                        R20Cell3.setCellStyle(numberStyle);
                    } else {
                        R20Cell3.setCellValue("");
                        R20Cell3.setCellStyle(textStyle);
                    }
                    // R20 Col G
                    Cell R20Cell4 = row.createCell(6);
                    if (record.getR20_amt_statement_adj_bwp() != null) {
                        R20Cell4.setCellValue(record.getR20_amt_statement_adj_bwp().doubleValue());
                        R20Cell4.setCellStyle(numberStyle);
                    } else {
                        R20Cell4.setCellValue("");
                        R20Cell4.setCellStyle(textStyle);
                    }
                    // // R20 Col H
                    // Cell R20Cell5 = row.createCell(7);
                    // if (record.getR20_net_amt() != null) {
                    // R20Cell5.setCellValue(record.getR20_net_amt().doubleValue());
                    // R20Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R20Cell5.setCellValue("");
                    // R20Cell5.setCellStyle(textStyle);
                    // }
                    // R20 Col I
                    Cell R20Cell6 = row.createCell(8);
                    if (record.getR20_net_amt_bwp() != null) {
                        R20Cell6.setCellValue(record.getR20_net_amt_bwp().doubleValue());
                        R20Cell6.setCellStyle(numberStyle);
                    } else {
                        R20Cell6.setCellValue("");
                        R20Cell6.setCellStyle(textStyle);
                    }
                    // R20 Col J
                    Cell R20Cell7 = row.createCell(9);
                    if (record.getR20_bal_sub() != null) {
                        R20Cell7.setCellValue(record.getR20_bal_sub().doubleValue());
                        R20Cell7.setCellStyle(numberStyle);
                    } else {
                        R20Cell7.setCellValue("");
                        R20Cell7.setCellStyle(textStyle);
                    }
                    // R20 Col K
                    Cell R20Cell8 = row.createCell(10);
                    if (record.getR20_bal_sub_bwp() != null) {
                        R20Cell8.setCellValue(record.getR20_bal_sub_bwp().doubleValue());
                        R20Cell8.setCellStyle(numberStyle);
                    } else {
                        R20Cell8.setCellValue("");
                        R20Cell8.setCellStyle(textStyle);
                    }
                    // R20 Col L
                    Cell R20Cell9 = row.createCell(11);
                    if (record.getR20_bal_sub_diaries() != null) {
                        R20Cell9.setCellValue(record.getR20_bal_sub_diaries().doubleValue());
                        R20Cell9.setCellStyle(numberStyle);
                    } else {
                        R20Cell9.setCellValue("");
                        R20Cell9.setCellStyle(textStyle);
                    }
                    // R20 Col M
                    Cell R20Cell10 = row.createCell(12);
                    if (record.getR20_bal_sub_diaries_bwp() != null) {
                        R20Cell10.setCellValue(record.getR20_bal_sub_diaries_bwp().doubleValue());
                        R20Cell10.setCellStyle(numberStyle);
                    } else {
                        R20Cell10.setCellValue("");
                        R20Cell10.setCellStyle(textStyle);
                    }

                    row = sheet.getRow(20);
                    Cell R21Cell1 = row.createCell(3);
                    if (record1.getR21_fig_bal_sheet() != null) {
                        R21Cell1.setCellValue(record1.getR21_fig_bal_sheet().doubleValue());
                        R21Cell1.setCellStyle(numberStyle);
                    } else {
                        R21Cell1.setCellValue("");
                        R21Cell1.setCellStyle(textStyle);
                    }

                    // R21 Col E
                    Cell R21Cell2 = row.createCell(4);
                    if (record1.getR21_fig_bal_sheet_bwp() != null) {
                        R21Cell2.setCellValue(record1.getR21_fig_bal_sheet_bwp().doubleValue());
                        R21Cell2.setCellStyle(numberStyle);
                    } else {
                        R21Cell2.setCellValue("");
                        R21Cell2.setCellStyle(textStyle);
                    }

                    // R21 Col F
                    Cell R21Cell3 = row.createCell(5);
                    if (record1.getR21_amt_statement_adj() != null) {
                        R21Cell3.setCellValue(record1.getR21_amt_statement_adj().doubleValue());
                        R21Cell3.setCellStyle(numberStyle);
                    } else {
                        R21Cell3.setCellValue("");
                        R21Cell3.setCellStyle(textStyle);
                    }
                    // R21 Col G
                    Cell R21Cell4 = row.createCell(6);
                    if (record1.getR21_amt_statement_adj_bwp() != null) {
                        R21Cell4.setCellValue(record1.getR21_amt_statement_adj_bwp().doubleValue());
                        R21Cell4.setCellStyle(numberStyle);
                    } else {
                        R21Cell4.setCellValue("");
                        R21Cell4.setCellStyle(textStyle);
                    }
                    // // R21 Col H
                    // Cell R21Cell5 = row.createCell(7);
                    // if (record1.getR21_net_amt() != null) {
                    // R21Cell5.setCellValue(record1.getR21_net_amt().doubleValue());
                    // R21Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R21Cell5.setCellValue("");
                    // R21Cell5.setCellStyle(textStyle);
                    // }
                    // R21 Col I
                    Cell R21Cell6 = row.createCell(8);
                    if (record1.getR21_net_amt_bwp() != null) {
                        R21Cell6.setCellValue(record1.getR21_net_amt_bwp().doubleValue());
                        R21Cell6.setCellStyle(numberStyle);
                    } else {
                        R21Cell6.setCellValue("");
                        R21Cell6.setCellStyle(textStyle);
                    }
                    // R21 Col J
                    Cell R21Cell7 = row.createCell(9);
                    if (record1.getR21_bal_sub() != null) {
                        R21Cell7.setCellValue(record1.getR21_bal_sub().doubleValue());
                        R21Cell7.setCellStyle(numberStyle);
                    } else {
                        R21Cell7.setCellValue("");
                        R21Cell7.setCellStyle(textStyle);
                    }
                    // R21 Col K
                    Cell R21Cell8 = row.createCell(10);
                    if (record1.getR21_bal_sub_bwp() != null) {
                        R21Cell8.setCellValue(record1.getR21_bal_sub_bwp().doubleValue());
                        R21Cell8.setCellStyle(numberStyle);
                    } else {
                        R21Cell8.setCellValue("");
                        R21Cell8.setCellStyle(textStyle);
                    }
                    // R21 Col L
                    Cell R21Cell9 = row.createCell(11);
                    if (record1.getR21_bal_sub_diaries() != null) {
                        R21Cell9.setCellValue(record1.getR21_bal_sub_diaries().doubleValue());
                        R21Cell9.setCellStyle(numberStyle);
                    } else {
                        R21Cell9.setCellValue("");
                        R21Cell9.setCellStyle(textStyle);
                    }
                    // R21 Col M
                    Cell R21Cell10 = row.createCell(12);
                    if (record1.getR21_bal_sub_diaries_bwp() != null) {
                        R21Cell10.setCellValue(record1.getR21_bal_sub_diaries_bwp().doubleValue());
                        R21Cell10.setCellStyle(numberStyle);
                    } else {
                        R21Cell10.setCellValue("");
                        R21Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(21);
                    Cell R22Cell1 = row.createCell(3);
                    if (record1.getR22_fig_bal_sheet() != null) {
                        R22Cell1.setCellValue(record1.getR22_fig_bal_sheet().doubleValue());
                        R22Cell1.setCellStyle(numberStyle);
                    } else {
                        R22Cell1.setCellValue("");
                        R22Cell1.setCellStyle(textStyle);
                    }

                    // R22 Col E
                    Cell R22Cell2 = row.createCell(4);
                    if (record1.getR22_fig_bal_sheet_bwp() != null) {
                        R22Cell2.setCellValue(record1.getR22_fig_bal_sheet_bwp().doubleValue());
                        R22Cell2.setCellStyle(numberStyle);
                    } else {
                        R22Cell2.setCellValue("");
                        R22Cell2.setCellStyle(textStyle);
                    }

                    // R22 Col F
                    Cell R22Cell3 = row.createCell(5);
                    if (record1.getR22_amt_statement_adj() != null) {
                        R22Cell3.setCellValue(record1.getR22_amt_statement_adj().doubleValue());
                        R22Cell3.setCellStyle(numberStyle);
                    } else {
                        R22Cell3.setCellValue("");
                        R22Cell3.setCellStyle(textStyle);
                    }
                    // R22 Col G
                    Cell R22Cell4 = row.createCell(6);
                    if (record1.getR22_amt_statement_adj_bwp() != null) {
                        R22Cell4.setCellValue(record1.getR22_amt_statement_adj_bwp().doubleValue());
                        R22Cell4.setCellStyle(numberStyle);
                    } else {
                        R22Cell4.setCellValue("");
                        R22Cell4.setCellStyle(textStyle);
                    }
                    // // R22 Col H
                    // Cell R22Cell5 = row.createCell(7);
                    // if (record1.getR22_net_amt() != null) {
                    // R22Cell5.setCellValue(record1.getR22_net_amt().doubleValue());
                    // R22Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R22Cell5.setCellValue("");
                    // R22Cell5.setCellStyle(textStyle);
                    // }
                    // R22 Col I
                    Cell R22Cell6 = row.createCell(8);
                    if (record1.getR22_net_amt_bwp() != null) {
                        R22Cell6.setCellValue(record1.getR22_net_amt_bwp().doubleValue());
                        R22Cell6.setCellStyle(numberStyle);
                    } else {
                        R22Cell6.setCellValue("");
                        R22Cell6.setCellStyle(textStyle);
                    }
                    // R22 Col J
                    Cell R22Cell7 = row.createCell(9);
                    if (record1.getR22_bal_sub() != null) {
                        R22Cell7.setCellValue(record1.getR22_bal_sub().doubleValue());
                        R22Cell7.setCellStyle(numberStyle);
                    } else {
                        R22Cell7.setCellValue("");
                        R22Cell7.setCellStyle(textStyle);
                    }
                    // R22 Col K
                    Cell R22Cell8 = row.createCell(10);
                    if (record1.getR22_bal_sub_bwp() != null) {
                        R22Cell8.setCellValue(record1.getR22_bal_sub_bwp().doubleValue());
                        R22Cell8.setCellStyle(numberStyle);
                    } else {
                        R22Cell8.setCellValue("");
                        R22Cell8.setCellStyle(textStyle);
                    }
                    // R22 Col L
                    Cell R22Cell9 = row.createCell(11);
                    if (record1.getR22_bal_sub_diaries() != null) {
                        R22Cell9.setCellValue(record1.getR22_bal_sub_diaries().doubleValue());
                        R22Cell9.setCellStyle(numberStyle);
                    } else {
                        R22Cell9.setCellValue("");
                        R22Cell9.setCellStyle(textStyle);
                    }
                    // R22 Col M
                    Cell R22Cell10 = row.createCell(12);
                    if (record1.getR22_bal_sub_diaries_bwp() != null) {
                        R22Cell10.setCellValue(record1.getR22_bal_sub_diaries_bwp().doubleValue());
                        R22Cell10.setCellStyle(numberStyle);
                    } else {
                        R22Cell10.setCellValue("");
                        R22Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(22);
                    Cell R23Cell1 = row.createCell(3);
                    if (record1.getR23_fig_bal_sheet() != null) {
                        R23Cell1.setCellValue(record1.getR23_fig_bal_sheet().doubleValue());
                        R23Cell1.setCellStyle(numberStyle);
                    } else {
                        R23Cell1.setCellValue("");
                        R23Cell1.setCellStyle(textStyle);
                    }

                    // R23 Col E
                    Cell R23Cell2 = row.createCell(4);
                    if (record1.getR23_fig_bal_sheet_bwp() != null) {
                        R23Cell2.setCellValue(record1.getR23_fig_bal_sheet_bwp().doubleValue());
                        R23Cell2.setCellStyle(numberStyle);
                    } else {
                        R23Cell2.setCellValue("");
                        R23Cell2.setCellStyle(textStyle);
                    }

                    // R23 Col F
                    Cell R23Cell3 = row.createCell(5);
                    if (record1.getR23_amt_statement_adj() != null) {
                        R23Cell3.setCellValue(record1.getR23_amt_statement_adj().doubleValue());
                        R23Cell3.setCellStyle(numberStyle);
                    } else {
                        R23Cell3.setCellValue("");
                        R23Cell3.setCellStyle(textStyle);
                    }
                    // R23 Col G
                    Cell R23Cell4 = row.createCell(6);
                    if (record1.getR23_amt_statement_adj_bwp() != null) {
                        R23Cell4.setCellValue(record1.getR23_amt_statement_adj_bwp().doubleValue());
                        R23Cell4.setCellStyle(numberStyle);
                    } else {
                        R23Cell4.setCellValue("");
                        R23Cell4.setCellStyle(textStyle);
                    }
                    // // R23 Col H
                    // Cell R23Cell5 = row.createCell(7);
                    // if (record1.getR23_net_amt() != null) {
                    // R23Cell5.setCellValue(record1.getR23_net_amt().doubleValue());
                    // R23Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R23Cell5.setCellValue("");
                    // R23Cell5.setCellStyle(textStyle);
                    // }
                    // R23 Col I
                    Cell R23Cell6 = row.createCell(8);
                    if (record1.getR23_net_amt_bwp() != null) {
                        R23Cell6.setCellValue(record1.getR23_net_amt_bwp().doubleValue());
                        R23Cell6.setCellStyle(numberStyle);
                    } else {
                        R23Cell6.setCellValue("");
                        R23Cell6.setCellStyle(textStyle);
                    }
                    // R23 Col J
                    Cell R23Cell7 = row.createCell(9);
                    if (record1.getR23_bal_sub() != null) {
                        R23Cell7.setCellValue(record1.getR23_bal_sub().doubleValue());
                        R23Cell7.setCellStyle(numberStyle);
                    } else {
                        R23Cell7.setCellValue("");
                        R23Cell7.setCellStyle(textStyle);
                    }
                    // R23 Col K
                    Cell R23Cell8 = row.createCell(10);
                    if (record1.getR23_bal_sub_bwp() != null) {
                        R23Cell8.setCellValue(record1.getR23_bal_sub_bwp().doubleValue());
                        R23Cell8.setCellStyle(numberStyle);
                    } else {
                        R23Cell8.setCellValue("");
                        R23Cell8.setCellStyle(textStyle);
                    }
                    // R23 Col L
                    Cell R23Cell9 = row.createCell(11);
                    if (record1.getR23_bal_sub_diaries() != null) {
                        R23Cell9.setCellValue(record1.getR23_bal_sub_diaries().doubleValue());
                        R23Cell9.setCellStyle(numberStyle);
                    } else {
                        R23Cell9.setCellValue("");
                        R23Cell9.setCellStyle(textStyle);
                    }
                    // R23 Col M
                    Cell R23Cell10 = row.createCell(12);
                    if (record1.getR23_bal_sub_diaries_bwp() != null) {
                        R23Cell10.setCellValue(record1.getR23_bal_sub_diaries_bwp().doubleValue());
                        R23Cell10.setCellStyle(numberStyle);
                    } else {
                        R23Cell10.setCellValue("");
                        R23Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(23);
                    Cell R24Cell1 = row.createCell(3);
                    if (record1.getR24_fig_bal_sheet() != null) {
                        R24Cell1.setCellValue(record1.getR24_fig_bal_sheet().doubleValue());
                        R24Cell1.setCellStyle(numberStyle);
                    } else {
                        R24Cell1.setCellValue("");
                        R24Cell1.setCellStyle(textStyle);
                    }

                    // R24 Col E
                    Cell R24Cell2 = row.createCell(4);
                    if (record1.getR24_fig_bal_sheet_bwp() != null) {
                        R24Cell2.setCellValue(record1.getR24_fig_bal_sheet_bwp().doubleValue());
                        R24Cell2.setCellStyle(numberStyle);
                    } else {
                        R24Cell2.setCellValue("");
                        R24Cell2.setCellStyle(textStyle);
                    }

                    // R24 Col F
                    Cell R24Cell3 = row.createCell(5);
                    if (record1.getR24_amt_statement_adj() != null) {
                        R24Cell3.setCellValue(record1.getR24_amt_statement_adj().doubleValue());
                        R24Cell3.setCellStyle(numberStyle);
                    } else {
                        R24Cell3.setCellValue("");
                        R24Cell3.setCellStyle(textStyle);
                    }
                    // R24 Col G
                    Cell R24Cell4 = row.createCell(6);
                    if (record1.getR24_amt_statement_adj_bwp() != null) {
                        R24Cell4.setCellValue(record1.getR24_amt_statement_adj_bwp().doubleValue());
                        R24Cell4.setCellStyle(numberStyle);
                    } else {
                        R24Cell4.setCellValue("");
                        R24Cell4.setCellStyle(textStyle);
                    }
                    // // R24 Col H
                    // Cell R24Cell5 = row.createCell(7);
                    // if (record1.getR24_net_amt() != null) {
                    // R24Cell5.setCellValue(record1.getR24_net_amt().doubleValue());
                    // R24Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R24Cell5.setCellValue("");
                    // R24Cell5.setCellStyle(textStyle);
                    // }
                    // R24 Col I
                    Cell R24Cell6 = row.createCell(8);
                    if (record1.getR24_net_amt_bwp() != null) {
                        R24Cell6.setCellValue(record1.getR24_net_amt_bwp().doubleValue());
                        R24Cell6.setCellStyle(numberStyle);
                    } else {
                        R24Cell6.setCellValue("");
                        R24Cell6.setCellStyle(textStyle);
                    }
                    // R24 Col J
                    Cell R24Cell7 = row.createCell(9);
                    if (record1.getR24_bal_sub() != null) {
                        R24Cell7.setCellValue(record1.getR24_bal_sub().doubleValue());
                        R24Cell7.setCellStyle(numberStyle);
                    } else {
                        R24Cell7.setCellValue("");
                        R24Cell7.setCellStyle(textStyle);
                    }
                    // R24 Col K
                    Cell R24Cell8 = row.createCell(10);
                    if (record1.getR24_bal_sub_bwp() != null) {
                        R24Cell8.setCellValue(record1.getR24_bal_sub_bwp().doubleValue());
                        R24Cell8.setCellStyle(numberStyle);
                    } else {
                        R24Cell8.setCellValue("");
                        R24Cell8.setCellStyle(textStyle);
                    }
                    // R24 Col L
                    Cell R24Cell9 = row.createCell(11);
                    if (record1.getR24_bal_sub_diaries() != null) {
                        R24Cell9.setCellValue(record1.getR24_bal_sub_diaries().doubleValue());
                        R24Cell9.setCellStyle(numberStyle);
                    } else {
                        R24Cell9.setCellValue("");
                        R24Cell9.setCellStyle(textStyle);
                    }
                    // R24 Col M
                    Cell R24Cell10 = row.createCell(12);
                    if (record1.getR24_bal_sub_diaries_bwp() != null) {
                        R24Cell10.setCellValue(record1.getR24_bal_sub_diaries_bwp().doubleValue());
                        R24Cell10.setCellStyle(numberStyle);
                    } else {
                        R24Cell10.setCellValue("");
                        R24Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(24);
                    Cell R25Cell1 = row.createCell(3);
                    if (record1.getR25_fig_bal_sheet() != null) {
                        R25Cell1.setCellValue(record1.getR25_fig_bal_sheet().doubleValue());
                        R25Cell1.setCellStyle(numberStyle);
                    } else {
                        R25Cell1.setCellValue("");
                        R25Cell1.setCellStyle(textStyle);
                    }

                    // R25 Col E
                    Cell R25Cell2 = row.createCell(4);
                    if (record1.getR25_fig_bal_sheet_bwp() != null) {
                        R25Cell2.setCellValue(record1.getR25_fig_bal_sheet_bwp().doubleValue());
                        R25Cell2.setCellStyle(numberStyle);
                    } else {
                        R25Cell2.setCellValue("");
                        R25Cell2.setCellStyle(textStyle);
                    }

                    // R25 Col F
                    Cell R25Cell3 = row.createCell(5);
                    if (record1.getR25_amt_statement_adj() != null) {
                        R25Cell3.setCellValue(record1.getR25_amt_statement_adj().doubleValue());
                        R25Cell3.setCellStyle(numberStyle);
                    } else {
                        R25Cell3.setCellValue("");
                        R25Cell3.setCellStyle(textStyle);
                    }
                    // R25 Col G
                    Cell R25Cell4 = row.createCell(6);
                    if (record1.getR25_amt_statement_adj_bwp() != null) {
                        R25Cell4.setCellValue(record1.getR25_amt_statement_adj_bwp().doubleValue());
                        R25Cell4.setCellStyle(numberStyle);
                    } else {
                        R25Cell4.setCellValue("");
                        R25Cell4.setCellStyle(textStyle);
                    }
                    // // R25 Col H
                    // Cell R25Cell5 = row.createCell(7);
                    // if (record1.getR25_net_amt() != null) {
                    // R25Cell5.setCellValue(record1.getR25_net_amt().doubleValue());
                    // R25Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R25Cell5.setCellValue("");
                    // R25Cell5.setCellStyle(textStyle);
                    // }
                    // R25 Col I
                    Cell R25Cell6 = row.createCell(8);
                    if (record1.getR25_net_amt_bwp() != null) {
                        R25Cell6.setCellValue(record1.getR25_net_amt_bwp().doubleValue());
                        R25Cell6.setCellStyle(numberStyle);
                    } else {
                        R25Cell6.setCellValue("");
                        R25Cell6.setCellStyle(textStyle);
                    }
                    // R25 Col J
                    Cell R25Cell7 = row.createCell(9);
                    if (record1.getR25_bal_sub() != null) {
                        R25Cell7.setCellValue(record1.getR25_bal_sub().doubleValue());
                        R25Cell7.setCellStyle(numberStyle);
                    } else {
                        R25Cell7.setCellValue("");
                        R25Cell7.setCellStyle(textStyle);
                    }
                    // R25 Col K
                    Cell R25Cell8 = row.createCell(10);
                    if (record1.getR25_bal_sub_bwp() != null) {
                        R25Cell8.setCellValue(record1.getR25_bal_sub_bwp().doubleValue());
                        R25Cell8.setCellStyle(numberStyle);
                    } else {
                        R25Cell8.setCellValue("");
                        R25Cell8.setCellStyle(textStyle);
                    }
                    // R25 Col L
                    Cell R25Cell9 = row.createCell(11);
                    if (record1.getR25_bal_sub_diaries() != null) {
                        R25Cell9.setCellValue(record1.getR25_bal_sub_diaries().doubleValue());
                        R25Cell9.setCellStyle(numberStyle);
                    } else {
                        R25Cell9.setCellValue("");
                        R25Cell9.setCellStyle(textStyle);
                    }
                    // R25 Col M
                    Cell R25Cell10 = row.createCell(12);
                    if (record1.getR25_bal_sub_diaries_bwp() != null) {
                        R25Cell10.setCellValue(record1.getR25_bal_sub_diaries_bwp().doubleValue());
                        R25Cell10.setCellStyle(numberStyle);
                    } else {
                        R25Cell10.setCellValue("");
                        R25Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(25);
                    Cell R26Cell1 = row.createCell(3);
                    if (record1.getR26_fig_bal_sheet() != null) {
                        R26Cell1.setCellValue(record1.getR26_fig_bal_sheet().doubleValue());
                        R26Cell1.setCellStyle(numberStyle);
                    } else {
                        R26Cell1.setCellValue("");
                        R26Cell1.setCellStyle(textStyle);
                    }

                    // R26 Col E
                    Cell R26Cell2 = row.createCell(4);
                    if (record1.getR26_fig_bal_sheet_bwp() != null) {
                        R26Cell2.setCellValue(record1.getR26_fig_bal_sheet_bwp().doubleValue());
                        R26Cell2.setCellStyle(numberStyle);
                    } else {
                        R26Cell2.setCellValue("");
                        R26Cell2.setCellStyle(textStyle);
                    }

                    // R26 Col F
                    Cell R26Cell3 = row.createCell(5);
                    if (record1.getR26_amt_statement_adj() != null) {
                        R26Cell3.setCellValue(record1.getR26_amt_statement_adj().doubleValue());
                        R26Cell3.setCellStyle(numberStyle);
                    } else {
                        R26Cell3.setCellValue("");
                        R26Cell3.setCellStyle(textStyle);
                    }
                    // R26 Col G
                    Cell R26Cell4 = row.createCell(6);
                    if (record1.getR26_amt_statement_adj_bwp() != null) {
                        R26Cell4.setCellValue(record1.getR26_amt_statement_adj_bwp().doubleValue());
                        R26Cell4.setCellStyle(numberStyle);
                    } else {
                        R26Cell4.setCellValue("");
                        R26Cell4.setCellStyle(textStyle);
                    }
                    // // R26 Col H
                    // Cell R26Cell5 = row.createCell(7);
                    // if (record1.getR26_net_amt() != null) {
                    // R26Cell5.setCellValue(record1.getR26_net_amt().doubleValue());
                    // R26Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R26Cell5.setCellValue("");
                    // R26Cell5.setCellStyle(textStyle);
                    // }
                    // R26 Col I
                    Cell R26Cell6 = row.createCell(8);
                    if (record1.getR26_net_amt_bwp() != null) {
                        R26Cell6.setCellValue(record1.getR26_net_amt_bwp().doubleValue());
                        R26Cell6.setCellStyle(numberStyle);
                    } else {
                        R26Cell6.setCellValue("");
                        R26Cell6.setCellStyle(textStyle);
                    }
                    // R26 Col J
                    Cell R26Cell7 = row.createCell(9);
                    if (record1.getR26_bal_sub() != null) {
                        R26Cell7.setCellValue(record1.getR26_bal_sub().doubleValue());
                        R26Cell7.setCellStyle(numberStyle);
                    } else {
                        R26Cell7.setCellValue("");
                        R26Cell7.setCellStyle(textStyle);
                    }
                    // R26 Col K
                    Cell R26Cell8 = row.createCell(10);
                    if (record1.getR26_bal_sub_bwp() != null) {
                        R26Cell8.setCellValue(record1.getR26_bal_sub_bwp().doubleValue());
                        R26Cell8.setCellStyle(numberStyle);
                    } else {
                        R26Cell8.setCellValue("");
                        R26Cell8.setCellStyle(textStyle);
                    }
                    // R26 Col L
                    Cell R26Cell9 = row.createCell(11);
                    if (record1.getR26_bal_sub_diaries() != null) {
                        R26Cell9.setCellValue(record1.getR26_bal_sub_diaries().doubleValue());
                        R26Cell9.setCellStyle(numberStyle);
                    } else {
                        R26Cell9.setCellValue("");
                        R26Cell9.setCellStyle(textStyle);
                    }
                    // R26 Col M
                    Cell R26Cell10 = row.createCell(12);
                    if (record1.getR26_bal_sub_diaries_bwp() != null) {
                        R26Cell10.setCellValue(record1.getR26_bal_sub_diaries_bwp().doubleValue());
                        R26Cell10.setCellStyle(numberStyle);
                    } else {
                        R26Cell10.setCellValue("");
                        R26Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(26);
                    Cell R27Cell1 = row.createCell(3);
                    if (record1.getR27_fig_bal_sheet() != null) {
                        R27Cell1.setCellValue(record1.getR27_fig_bal_sheet().doubleValue());
                        R27Cell1.setCellStyle(numberStyle);
                    } else {
                        R27Cell1.setCellValue("");
                        R27Cell1.setCellStyle(textStyle);
                    }

                    // R27 Col E
                    Cell R27Cell2 = row.createCell(4);
                    if (record1.getR27_fig_bal_sheet_bwp() != null) {
                        R27Cell2.setCellValue(record1.getR27_fig_bal_sheet_bwp().doubleValue());
                        R27Cell2.setCellStyle(numberStyle);
                    } else {
                        R27Cell2.setCellValue("");
                        R27Cell2.setCellStyle(textStyle);
                    }

                    // R27 Col F
                    Cell R27Cell3 = row.createCell(5);
                    if (record1.getR27_amt_statement_adj() != null) {
                        R27Cell3.setCellValue(record1.getR27_amt_statement_adj().doubleValue());
                        R27Cell3.setCellStyle(numberStyle);
                    } else {
                        R27Cell3.setCellValue("");
                        R27Cell3.setCellStyle(textStyle);
                    }
                    // R27 Col G
                    Cell R27Cell4 = row.createCell(6);
                    if (record1.getR27_amt_statement_adj_bwp() != null) {
                        R27Cell4.setCellValue(record1.getR27_amt_statement_adj_bwp().doubleValue());
                        R27Cell4.setCellStyle(numberStyle);
                    } else {
                        R27Cell4.setCellValue("");
                        R27Cell4.setCellStyle(textStyle);
                    }
                    // // R27 Col H
                    // Cell R27Cell5 = row.createCell(7);
                    // if (record1.getR27_net_amt() != null) {
                    // R27Cell5.setCellValue(record1.getR27_net_amt().doubleValue());
                    // R27Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R27Cell5.setCellValue("");
                    // R27Cell5.setCellStyle(textStyle);
                    // }
                    // R27 Col I
                    Cell R27Cell6 = row.createCell(8);
                    if (record1.getR27_net_amt_bwp() != null) {
                        R27Cell6.setCellValue(record1.getR27_net_amt_bwp().doubleValue());
                        R27Cell6.setCellStyle(numberStyle);
                    } else {
                        R27Cell6.setCellValue("");
                        R27Cell6.setCellStyle(textStyle);
                    }
                    // R27 Col J
                    Cell R27Cell7 = row.createCell(9);
                    if (record1.getR27_bal_sub() != null) {
                        R27Cell7.setCellValue(record1.getR27_bal_sub().doubleValue());
                        R27Cell7.setCellStyle(numberStyle);
                    } else {
                        R27Cell7.setCellValue("");
                        R27Cell7.setCellStyle(textStyle);
                    }
                    // R27 Col K
                    Cell R27Cell8 = row.createCell(10);
                    if (record1.getR27_bal_sub_bwp() != null) {
                        R27Cell8.setCellValue(record1.getR27_bal_sub_bwp().doubleValue());
                        R27Cell8.setCellStyle(numberStyle);
                    } else {
                        R27Cell8.setCellValue("");
                        R27Cell8.setCellStyle(textStyle);
                    }
                    // R27 Col L
                    Cell R27Cell9 = row.createCell(11);
                    if (record1.getR27_bal_sub_diaries() != null) {
                        R27Cell9.setCellValue(record1.getR27_bal_sub_diaries().doubleValue());
                        R27Cell9.setCellStyle(numberStyle);
                    } else {
                        R27Cell9.setCellValue("");
                        R27Cell9.setCellStyle(textStyle);
                    }
                    // R27 Col M
                    Cell R27Cell10 = row.createCell(12);
                    if (record1.getR27_bal_sub_diaries_bwp() != null) {
                        R27Cell10.setCellValue(record1.getR27_bal_sub_diaries_bwp().doubleValue());
                        R27Cell10.setCellStyle(numberStyle);
                    } else {
                        R27Cell10.setCellValue("");
                        R27Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(27);
                    Cell R28Cell1 = row.createCell(3);
                    if (record1.getR28_fig_bal_sheet() != null) {
                        R28Cell1.setCellValue(record1.getR28_fig_bal_sheet().doubleValue());
                        R28Cell1.setCellStyle(numberStyle);
                    } else {
                        R28Cell1.setCellValue("");
                        R28Cell1.setCellStyle(textStyle);
                    }

                    // R28 Col E
                    Cell R28Cell2 = row.createCell(4);
                    if (record1.getR28_fig_bal_sheet_bwp() != null) {
                        R28Cell2.setCellValue(record1.getR28_fig_bal_sheet_bwp().doubleValue());
                        R28Cell2.setCellStyle(numberStyle);
                    } else {
                        R28Cell2.setCellValue("");
                        R28Cell2.setCellStyle(textStyle);
                    }

                    // R28 Col F
                    Cell R28Cell3 = row.createCell(5);
                    if (record1.getR28_amt_statement_adj() != null) {
                        R28Cell3.setCellValue(record1.getR28_amt_statement_adj().doubleValue());
                        R28Cell3.setCellStyle(numberStyle);
                    } else {
                        R28Cell3.setCellValue("");
                        R28Cell3.setCellStyle(textStyle);
                    }
                    // R28 Col G
                    Cell R28Cell4 = row.createCell(6);
                    if (record1.getR28_amt_statement_adj_bwp() != null) {
                        R28Cell4.setCellValue(record1.getR28_amt_statement_adj_bwp().doubleValue());
                        R28Cell4.setCellStyle(numberStyle);
                    } else {
                        R28Cell4.setCellValue("");
                        R28Cell4.setCellStyle(textStyle);
                    }
                    // // R28 Col H
                    // Cell R28Cell5 = row.createCell(7);
                    // if (record1.getR28_net_amt() != null) {
                    // R28Cell5.setCellValue(record1.getR28_net_amt().doubleValue());
                    // R28Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R28Cell5.setCellValue("");
                    // R28Cell5.setCellStyle(textStyle);
                    // }
                    // R28 Col I
                    Cell R28Cell6 = row.createCell(8);
                    if (record1.getR28_net_amt_bwp() != null) {
                        R28Cell6.setCellValue(record1.getR28_net_amt_bwp().doubleValue());
                        R28Cell6.setCellStyle(numberStyle);
                    } else {
                        R28Cell6.setCellValue("");
                        R28Cell6.setCellStyle(textStyle);
                    }
                    // R28 Col J
                    Cell R28Cell7 = row.createCell(9);
                    if (record1.getR28_bal_sub() != null) {
                        R28Cell7.setCellValue(record1.getR28_bal_sub().doubleValue());
                        R28Cell7.setCellStyle(numberStyle);
                    } else {
                        R28Cell7.setCellValue("");
                        R28Cell7.setCellStyle(textStyle);
                    }
                    // R28 Col K
                    Cell R28Cell8 = row.createCell(10);
                    if (record1.getR28_bal_sub_bwp() != null) {
                        R28Cell8.setCellValue(record1.getR28_bal_sub_bwp().doubleValue());
                        R28Cell8.setCellStyle(numberStyle);
                    } else {
                        R28Cell8.setCellValue("");
                        R28Cell8.setCellStyle(textStyle);
                    }
                    // R28 Col L
                    Cell R28Cell9 = row.createCell(11);
                    if (record1.getR28_bal_sub_diaries() != null) {
                        R28Cell9.setCellValue(record1.getR28_bal_sub_diaries().doubleValue());
                        R28Cell9.setCellStyle(numberStyle);
                    } else {
                        R28Cell9.setCellValue("");
                        R28Cell9.setCellStyle(textStyle);
                    }
                    // R28 Col M
                    Cell R28Cell10 = row.createCell(12);
                    if (record1.getR28_bal_sub_diaries_bwp() != null) {
                        R28Cell10.setCellValue(record1.getR28_bal_sub_diaries_bwp().doubleValue());
                        R28Cell10.setCellStyle(numberStyle);
                    } else {
                        R28Cell10.setCellValue("");
                        R28Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(28);
                    Cell R29Cell1 = row.createCell(3);
                    if (record1.getR29_fig_bal_sheet() != null) {
                        R29Cell1.setCellValue(record1.getR29_fig_bal_sheet().doubleValue());
                        R29Cell1.setCellStyle(numberStyle);
                    } else {
                        R29Cell1.setCellValue("");
                        R29Cell1.setCellStyle(textStyle);
                    }

                    // R29 Col E
                    Cell R29Cell2 = row.createCell(4);
                    if (record1.getR29_fig_bal_sheet_bwp() != null) {
                        R29Cell2.setCellValue(record1.getR29_fig_bal_sheet_bwp().doubleValue());
                        R29Cell2.setCellStyle(numberStyle);
                    } else {
                        R29Cell2.setCellValue("");
                        R29Cell2.setCellStyle(textStyle);
                    }

                    // R29 Col F
                    Cell R29Cell3 = row.createCell(5);
                    if (record1.getR29_amt_statement_adj() != null) {
                        R29Cell3.setCellValue(record1.getR29_amt_statement_adj().doubleValue());
                        R29Cell3.setCellStyle(numberStyle);
                    } else {
                        R29Cell3.setCellValue("");
                        R29Cell3.setCellStyle(textStyle);
                    }
                    // R29 Col G
                    Cell R29Cell4 = row.createCell(6);
                    if (record1.getR29_amt_statement_adj_bwp() != null) {
                        R29Cell4.setCellValue(record1.getR29_amt_statement_adj_bwp().doubleValue());
                        R29Cell4.setCellStyle(numberStyle);
                    } else {
                        R29Cell4.setCellValue("");
                        R29Cell4.setCellStyle(textStyle);
                    }
                    // // R29 Col H
                    // Cell R29Cell5 = row.createCell(7);
                    // if (record1.getR29_net_amt() != null) {
                    // R29Cell5.setCellValue(record1.getR29_net_amt().doubleValue());
                    // R29Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R29Cell5.setCellValue("");
                    // R29Cell5.setCellStyle(textStyle);
                    // }
                    // R29 Col I
                    Cell R29Cell6 = row.createCell(8);
                    if (record1.getR29_net_amt_bwp() != null) {
                        R29Cell6.setCellValue(record1.getR29_net_amt_bwp().doubleValue());
                        R29Cell6.setCellStyle(numberStyle);
                    } else {
                        R29Cell6.setCellValue("");
                        R29Cell6.setCellStyle(textStyle);
                    }
                    // R29 Col J
                    Cell R29Cell7 = row.createCell(9);
                    if (record1.getR29_bal_sub() != null) {
                        R29Cell7.setCellValue(record1.getR29_bal_sub().doubleValue());
                        R29Cell7.setCellStyle(numberStyle);
                    } else {
                        R29Cell7.setCellValue("");
                        R29Cell7.setCellStyle(textStyle);
                    }
                    // R29 Col K
                    Cell R29Cell8 = row.createCell(10);
                    if (record1.getR29_bal_sub_bwp() != null) {
                        R29Cell8.setCellValue(record1.getR29_bal_sub_bwp().doubleValue());
                        R29Cell8.setCellStyle(numberStyle);
                    } else {
                        R29Cell8.setCellValue("");
                        R29Cell8.setCellStyle(textStyle);
                    }
                    // R29 Col L
                    Cell R29Cell9 = row.createCell(11);
                    if (record1.getR29_bal_sub_diaries() != null) {
                        R29Cell9.setCellValue(record1.getR29_bal_sub_diaries().doubleValue());
                        R29Cell9.setCellStyle(numberStyle);
                    } else {
                        R29Cell9.setCellValue("");
                        R29Cell9.setCellStyle(textStyle);
                    }
                    // R29 Col M
                    Cell R29Cell10 = row.createCell(12);
                    if (record1.getR29_bal_sub_diaries_bwp() != null) {
                        R29Cell10.setCellValue(record1.getR29_bal_sub_diaries_bwp().doubleValue());
                        R29Cell10.setCellStyle(numberStyle);
                    } else {
                        R29Cell10.setCellValue("");
                        R29Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(29);
                    Cell R30Cell1 = row.createCell(3);
                    if (record.getR30_fig_bal_sheet() != null) {
                        R30Cell1.setCellValue(record.getR30_fig_bal_sheet().doubleValue());
                        R30Cell1.setCellStyle(numberStyle);
                    } else {
                        R30Cell1.setCellValue("");
                        R30Cell1.setCellStyle(textStyle);
                    }

                    // R30 Col E
                    Cell R30Cell2 = row.createCell(4);
                    if (record.getR30_fig_bal_sheet_bwp() != null) {
                        R30Cell2.setCellValue(record.getR30_fig_bal_sheet_bwp().doubleValue());
                        R30Cell2.setCellStyle(numberStyle);
                    } else {
                        R30Cell2.setCellValue("");
                        R30Cell2.setCellStyle(textStyle);
                    }

                    // R30 Col F
                    Cell R30Cell3 = row.createCell(5);
                    if (record.getR30_amt_statement_adj() != null) {
                        R30Cell3.setCellValue(record.getR30_amt_statement_adj().doubleValue());
                        R30Cell3.setCellStyle(numberStyle);
                    } else {
                        R30Cell3.setCellValue("");
                        R30Cell3.setCellStyle(textStyle);
                    }
                    // R30 Col G
                    Cell R30Cell4 = row.createCell(6);
                    if (record.getR30_amt_statement_adj_bwp() != null) {
                        R30Cell4.setCellValue(record.getR30_amt_statement_adj_bwp().doubleValue());
                        R30Cell4.setCellStyle(numberStyle);
                    } else {
                        R30Cell4.setCellValue("");
                        R30Cell4.setCellStyle(textStyle);
                    }
                    // // R30 Col H
                    // Cell R30Cell5 = row.createCell(7);
                    // if (record.getR30_net_amt() != null) {
                    // R30Cell5.setCellValue(record.getR30_net_amt().doubleValue());
                    // R30Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R30Cell5.setCellValue("");
                    // R30Cell5.setCellStyle(textStyle);
                    // }
                    // R30 Col I
                    Cell R30Cell6 = row.createCell(8);
                    if (record.getR30_net_amt_bwp() != null) {
                        R30Cell6.setCellValue(record.getR30_net_amt_bwp().doubleValue());
                        R30Cell6.setCellStyle(numberStyle);
                    } else {
                        R30Cell6.setCellValue("");
                        R30Cell6.setCellStyle(textStyle);
                    }
                    // R30 Col J
                    Cell R30Cell7 = row.createCell(9);
                    if (record.getR30_bal_sub() != null) {
                        R30Cell7.setCellValue(record.getR30_bal_sub().doubleValue());
                        R30Cell7.setCellStyle(numberStyle);
                    } else {
                        R30Cell7.setCellValue("");
                        R30Cell7.setCellStyle(textStyle);
                    }
                    // R30 Col K
                    Cell R30Cell8 = row.createCell(10);
                    if (record.getR30_bal_sub_bwp() != null) {
                        R30Cell8.setCellValue(record.getR30_bal_sub_bwp().doubleValue());
                        R30Cell8.setCellStyle(numberStyle);
                    } else {
                        R30Cell8.setCellValue("");
                        R30Cell8.setCellStyle(textStyle);
                    }
                    // R30 Col L
                    Cell R30Cell9 = row.createCell(11);
                    if (record.getR30_bal_sub_diaries() != null) {
                        R30Cell9.setCellValue(record.getR30_bal_sub_diaries().doubleValue());
                        R30Cell9.setCellStyle(numberStyle);
                    } else {
                        R30Cell9.setCellValue("");
                        R30Cell9.setCellStyle(textStyle);
                    }
                    // R30 Col M
                    Cell R30Cell10 = row.createCell(12);
                    if (record.getR30_bal_sub_diaries_bwp() != null) {
                        R30Cell10.setCellValue(record.getR30_bal_sub_diaries_bwp().doubleValue());
                        R30Cell10.setCellStyle(numberStyle);
                    } else {
                        R30Cell10.setCellValue("");
                        R30Cell10.setCellStyle(textStyle);
                    }
                    // row = sheet.getRow(30);
                    // Cell R31Cell1 = row.createCell(3);
                    // if (record.getR31_fig_bal_sheet() != null) {
                    // R31Cell1.setCellValue(record.getR31_fig_bal_sheet().doubleValue());
                    // R31Cell1.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell1.setCellValue("");
                    // R31Cell1.setCellStyle(textStyle);
                    // }

                    // // R31 Col E
                    // Cell R31Cell2 = row.createCell(4);
                    // if (record.getR31_fig_bal_sheet_bwp() != null) {
                    // R31Cell2.setCellValue(record.getR31_fig_bal_sheet_bwp().doubleValue());
                    // R31Cell2.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell2.setCellValue("");
                    // R31Cell2.setCellStyle(textStyle);
                    // }

                    // // R31 Col F
                    // Cell R31Cell3 = row.createCell(5);
                    // if (record.getR31_amt_statement_adj() != null) {
                    // R31Cell3.setCellValue(record.getR31_amt_statement_adj().doubleValue());
                    // R31Cell3.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell3.setCellValue("");
                    // R31Cell3.setCellStyle(textStyle);
                    // }
                    // // R31 Col G
                    // Cell R31Cell4 = row.createCell(6);
                    // if (record.getR31_amt_statement_adj_bwp() != null) {
                    // R31Cell4.setCellValue(record.getR31_amt_statement_adj_bwp().doubleValue());
                    // R31Cell4.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell4.setCellValue("");
                    // R31Cell4.setCellStyle(textStyle);
                    // }
                    // // R31 Col H
                    // Cell R31Cell5 = row.createCell(7);
                    // if (record.getR31_net_amt() != null) {
                    // R31Cell5.setCellValue(record.getR31_net_amt().doubleValue());
                    // R31Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell5.setCellValue("");
                    // R31Cell5.setCellStyle(textStyle);
                    // }
                    // // R31 Col I
                    // Cell R31Cell6 = row.createCell(8);
                    // if (record.getR31_net_amt_bwp() != null) {
                    // R31Cell6.setCellValue(record.getR31_net_amt_bwp().doubleValue());
                    // R31Cell6.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell6.setCellValue("");
                    // R31Cell6.setCellStyle(textStyle);
                    // }
                    // // R31 Col J
                    // Cell R31Cell7 = row.createCell(9);
                    // if (record.getR31_bal_sub() != null) {
                    // R31Cell7.setCellValue(record.getR31_bal_sub().doubleValue());
                    // R31Cell7.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell7.setCellValue("");
                    // R31Cell7.setCellStyle(textStyle);
                    // }
                    // // R31 Col K
                    // Cell R31Cell8 = row.createCell(10);
                    // if (record.getR31_bal_sub_bwp() != null) {
                    // R31Cell8.setCellValue(record.getR31_bal_sub_bwp().doubleValue());
                    // R31Cell8.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell8.setCellValue("");
                    // R31Cell8.setCellStyle(textStyle);
                    // }
                    // // R31 Col L
                    // Cell R31Cell9 = row.createCell(11);
                    // if (record.getR31_bal_sub_diaries() != null) {
                    // R31Cell9.setCellValue(record.getR31_bal_sub_diaries().doubleValue());
                    // R31Cell9.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell9.setCellValue("");
                    // R31Cell9.setCellStyle(textStyle);
                    // }
                    // // R31 Col M
                    // Cell R31Cell10 = row.createCell(12);
                    // if (record.getR31_bal_sub_diaries_bwp() != null) {
                    // R31Cell10.setCellValue(record.getR31_bal_sub_diaries_bwp().doubleValue());
                    // R31Cell10.setCellStyle(numberStyle);
                    // } else {
                    // R31Cell10.setCellValue("");
                    // R31Cell10.setCellStyle(textStyle);
                    // }
                    row = sheet.getRow(39);
                    Cell R40Cell1 = row.createCell(3);
                    if (record.getR40_fig_bal_sheet() != null) {
                        R40Cell1.setCellValue(record.getR40_fig_bal_sheet().doubleValue());
                        R40Cell1.setCellStyle(numberStyle);
                    } else {
                        R40Cell1.setCellValue("");
                        R40Cell1.setCellStyle(textStyle);
                    }

                    // R40 Col E
                    Cell R40Cell2 = row.createCell(4);
                    if (record.getR40_fig_bal_sheet_bwp() != null) {
                        R40Cell2.setCellValue(record.getR40_fig_bal_sheet_bwp().doubleValue());
                        R40Cell2.setCellStyle(numberStyle);
                    } else {
                        R40Cell2.setCellValue("");
                        R40Cell2.setCellStyle(textStyle);
                    }

                    // R40 Col F
                    Cell R40Cell3 = row.createCell(5);
                    if (record.getR40_amt_statement_adj() != null) {
                        R40Cell3.setCellValue(record.getR40_amt_statement_adj().doubleValue());
                        R40Cell3.setCellStyle(numberStyle);
                    } else {
                        R40Cell3.setCellValue("");
                        R40Cell3.setCellStyle(textStyle);
                    }
                    // R40 Col G
                    Cell R40Cell4 = row.createCell(6);
                    if (record.getR40_amt_statement_adj_bwp() != null) {
                        R40Cell4.setCellValue(record.getR40_amt_statement_adj_bwp().doubleValue());
                        R40Cell4.setCellStyle(numberStyle);
                    } else {
                        R40Cell4.setCellValue("");
                        R40Cell4.setCellStyle(textStyle);
                    }
                    // // R40 Col H
                    // Cell R40Cell5 = row.createCell(7);
                    // if (record.getR40_net_amt() != null) {
                    // R40Cell5.setCellValue(record.getR40_net_amt().doubleValue());
                    // R40Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R40Cell5.setCellValue("");
                    // R40Cell5.setCellStyle(textStyle);
                    // }
                    // R40 Col I
                    Cell R40Cell6 = row.createCell(8);
                    if (record.getR40_net_amt_bwp() != null) {
                        R40Cell6.setCellValue(record.getR40_net_amt_bwp().doubleValue());
                        R40Cell6.setCellStyle(numberStyle);
                    } else {
                        R40Cell6.setCellValue("");
                        R40Cell6.setCellStyle(textStyle);
                    }
                    // R40 Col J
                    Cell R40Cell7 = row.createCell(9);
                    if (record.getR40_bal_sub() != null) {
                        R40Cell7.setCellValue(record.getR40_bal_sub().doubleValue());
                        R40Cell7.setCellStyle(numberStyle);
                    } else {
                        R40Cell7.setCellValue("");
                        R40Cell7.setCellStyle(textStyle);
                    }
                    // R40 Col K
                    Cell R40Cell8 = row.createCell(10);
                    if (record.getR40_bal_sub_bwp() != null) {
                        R40Cell8.setCellValue(record.getR40_bal_sub_bwp().doubleValue());
                        R40Cell8.setCellStyle(numberStyle);
                    } else {
                        R40Cell8.setCellValue("");
                        R40Cell8.setCellStyle(textStyle);
                    }
                    // R40 Col L
                    Cell R40Cell9 = row.createCell(11);
                    if (record.getR40_bal_sub_diaries() != null) {
                        R40Cell9.setCellValue(record.getR40_bal_sub_diaries().doubleValue());
                        R40Cell9.setCellStyle(numberStyle);
                    } else {
                        R40Cell9.setCellValue("");
                        R40Cell9.setCellStyle(textStyle);
                    }
                    // R40 Col M
                    Cell R40Cell10 = row.createCell(12);
                    if (record.getR40_bal_sub_diaries_bwp() != null) {
                        R40Cell10.setCellValue(record.getR40_bal_sub_diaries_bwp().doubleValue());
                        R40Cell10.setCellStyle(numberStyle);
                    } else {
                        R40Cell10.setCellValue("");
                        R40Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(40);
                    Cell R41Cell1 = row.createCell(3);
                    if (record.getR41_fig_bal_sheet() != null) {
                        R41Cell1.setCellValue(record.getR41_fig_bal_sheet().doubleValue());
                        R41Cell1.setCellStyle(numberStyle);
                    } else {
                        R41Cell1.setCellValue("");
                        R41Cell1.setCellStyle(textStyle);
                    }

                    // R41 Col E
                    Cell R41Cell2 = row.createCell(4);
                    if (record.getR41_fig_bal_sheet_bwp() != null) {
                        R41Cell2.setCellValue(record.getR41_fig_bal_sheet_bwp().doubleValue());
                        R41Cell2.setCellStyle(numberStyle);
                    } else {
                        R41Cell2.setCellValue("");
                        R41Cell2.setCellStyle(textStyle);
                    }

                    // R41 Col F
                    Cell R41Cell3 = row.createCell(5);
                    if (record.getR41_amt_statement_adj() != null) {
                        R41Cell3.setCellValue(record.getR41_amt_statement_adj().doubleValue());
                        R41Cell3.setCellStyle(numberStyle);
                    } else {
                        R41Cell3.setCellValue("");
                        R41Cell3.setCellStyle(textStyle);
                    }
                    // R41 Col G
                    Cell R41Cell4 = row.createCell(6);
                    if (record.getR41_amt_statement_adj_bwp() != null) {
                        R41Cell4.setCellValue(record.getR41_amt_statement_adj_bwp().doubleValue());
                        R41Cell4.setCellStyle(numberStyle);
                    } else {
                        R41Cell4.setCellValue("");
                        R41Cell4.setCellStyle(textStyle);
                    }
                    // // R41 Col H
                    // Cell R41Cell5 = row.createCell(7);
                    // if (record.getR41_net_amt() != null) {
                    // R41Cell5.setCellValue(record.getR41_net_amt().doubleValue());
                    // R41Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R41Cell5.setCellValue("");
                    // R41Cell5.setCellStyle(textStyle);
                    // }
                    // R41 Col I
                    Cell R41Cell6 = row.createCell(8);
                    if (record.getR41_net_amt_bwp() != null) {
                        R41Cell6.setCellValue(record.getR41_net_amt_bwp().doubleValue());
                        R41Cell6.setCellStyle(numberStyle);
                    } else {
                        R41Cell6.setCellValue("");
                        R41Cell6.setCellStyle(textStyle);
                    }
                    // R41 Col J
                    Cell R41Cell7 = row.createCell(9);
                    if (record.getR41_bal_sub() != null) {
                        R41Cell7.setCellValue(record.getR41_bal_sub().doubleValue());
                        R41Cell7.setCellStyle(numberStyle);
                    } else {
                        R41Cell7.setCellValue("");
                        R41Cell7.setCellStyle(textStyle);
                    }
                    // R41 Col K
                    Cell R41Cell8 = row.createCell(10);
                    if (record.getR41_bal_sub_bwp() != null) {
                        R41Cell8.setCellValue(record.getR41_bal_sub_bwp().doubleValue());
                        R41Cell8.setCellStyle(numberStyle);
                    } else {
                        R41Cell8.setCellValue("");
                        R41Cell8.setCellStyle(textStyle);
                    }
                    // R41 Col L
                    Cell R41Cell9 = row.createCell(11);
                    if (record.getR41_bal_sub_diaries() != null) {
                        R41Cell9.setCellValue(record.getR41_bal_sub_diaries().doubleValue());
                        R41Cell9.setCellStyle(numberStyle);
                    } else {
                        R41Cell9.setCellValue("");
                        R41Cell9.setCellStyle(textStyle);
                    }
                    // R41 Col M
                    Cell R41Cell10 = row.createCell(12);
                    if (record.getR41_bal_sub_diaries_bwp() != null) {
                        R41Cell10.setCellValue(record.getR41_bal_sub_diaries_bwp().doubleValue());
                        R41Cell10.setCellStyle(numberStyle);
                    } else {
                        R41Cell10.setCellValue("");
                        R41Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(41);
                    Cell R42Cell1 = row.createCell(3);
                    if (record1.getR42_fig_bal_sheet() != null) {
                        R42Cell1.setCellValue(record1.getR42_fig_bal_sheet().doubleValue());
                        R42Cell1.setCellStyle(numberStyle);
                    } else {
                        R42Cell1.setCellValue("");
                        R42Cell1.setCellStyle(textStyle);
                    }

                    // R42 Col E
                    Cell R42Cell2 = row.createCell(4);
                    if (record1.getR42_fig_bal_sheet_bwp() != null) {
                        R42Cell2.setCellValue(record1.getR42_fig_bal_sheet_bwp().doubleValue());
                        R42Cell2.setCellStyle(numberStyle);
                    } else {
                        R42Cell2.setCellValue("");
                        R42Cell2.setCellStyle(textStyle);
                    }

                    // R42 Col F
                    Cell R42Cell3 = row.createCell(5);
                    if (record1.getR42_amt_statement_adj() != null) {
                        R42Cell3.setCellValue(record1.getR42_amt_statement_adj().doubleValue());
                        R42Cell3.setCellStyle(numberStyle);
                    } else {
                        R42Cell3.setCellValue("");
                        R42Cell3.setCellStyle(textStyle);
                    }
                    // R42 Col G
                    Cell R42Cell4 = row.createCell(6);
                    if (record1.getR42_amt_statement_adj_bwp() != null) {
                        R42Cell4.setCellValue(record1.getR42_amt_statement_adj_bwp().doubleValue());
                        R42Cell4.setCellStyle(numberStyle);
                    } else {
                        R42Cell4.setCellValue("");
                        R42Cell4.setCellStyle(textStyle);
                    }
                    // // R42 Col H
                    // Cell R42Cell5 = row.createCell(7);
                    // if (record1.getR42_net_amt() != null) {
                    // R42Cell5.setCellValue(record1.getR42_net_amt().doubleValue());
                    // R42Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R42Cell5.setCellValue("");
                    // R42Cell5.setCellStyle(textStyle);
                    // }
                    // R42 Col I
                    Cell R42Cell6 = row.createCell(8);
                    if (record1.getR42_net_amt_bwp() != null) {
                        R42Cell6.setCellValue(record1.getR42_net_amt_bwp().doubleValue());
                        R42Cell6.setCellStyle(numberStyle);
                    } else {
                        R42Cell6.setCellValue("");
                        R42Cell6.setCellStyle(textStyle);
                    }
                    // R42 Col J
                    Cell R42Cell7 = row.createCell(9);
                    if (record1.getR42_bal_sub() != null) {
                        R42Cell7.setCellValue(record1.getR42_bal_sub().doubleValue());
                        R42Cell7.setCellStyle(numberStyle);
                    } else {
                        R42Cell7.setCellValue("");
                        R42Cell7.setCellStyle(textStyle);
                    }
                    // R42 Col K
                    Cell R42Cell8 = row.createCell(10);
                    if (record1.getR42_bal_sub_bwp() != null) {
                        R42Cell8.setCellValue(record1.getR42_bal_sub_bwp().doubleValue());
                        R42Cell8.setCellStyle(numberStyle);
                    } else {
                        R42Cell8.setCellValue("");
                        R42Cell8.setCellStyle(textStyle);
                    }
                    // R42 Col L
                    Cell R42Cell9 = row.createCell(11);
                    if (record1.getR42_bal_sub_diaries() != null) {
                        R42Cell9.setCellValue(record1.getR42_bal_sub_diaries().doubleValue());
                        R42Cell9.setCellStyle(numberStyle);
                    } else {
                        R42Cell9.setCellValue("");
                        R42Cell9.setCellStyle(textStyle);
                    }
                    // R42 Col M
                    Cell R42Cell10 = row.createCell(12);
                    if (record1.getR42_bal_sub_diaries_bwp() != null) {
                        R42Cell10.setCellValue(record1.getR42_bal_sub_diaries_bwp().doubleValue());
                        R42Cell10.setCellStyle(numberStyle);
                    } else {
                        R42Cell10.setCellValue("");
                        R42Cell10.setCellStyle(textStyle);
                    }
                    // row = sheet.getRow(42);
                    // Cell R43Cell1 = row.createCell(3);
                    // if (record.getR43_fig_bal_sheet() != null) {
                    // R43Cell1.setCellValue(record.getR43_fig_bal_sheet().doubleValue());
                    // R43Cell1.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell1.setCellValue("");
                    // R43Cell1.setCellStyle(textStyle);
                    // }

                    // // R43 Col E
                    // Cell R43Cell2 = row.createCell(4);
                    // if (record.getR43_fig_bal_sheet_bwp() != null) {
                    // R43Cell2.setCellValue(record.getR43_fig_bal_sheet_bwp().doubleValue());
                    // R43Cell2.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell2.setCellValue("");
                    // R43Cell2.setCellStyle(textStyle);
                    // }

                    // // R43 Col F
                    // Cell R43Cell3 = row.createCell(5);
                    // if (record.getR43_amt_statement_adj() != null) {
                    // R43Cell3.setCellValue(record.getR43_amt_statement_adj().doubleValue());
                    // R43Cell3.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell3.setCellValue("");
                    // R43Cell3.setCellStyle(textStyle);
                    // }
                    // // R43 Col G
                    // Cell R43Cell4 = row.createCell(6);
                    // if (record.getR43_amt_statement_adj_bwp() != null) {
                    // R43Cell4.setCellValue(record.getR43_amt_statement_adj_bwp().doubleValue());
                    // R43Cell4.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell4.setCellValue("");
                    // R43Cell4.setCellStyle(textStyle);
                    // }
                    // // R43 Col H
                    // Cell R43Cell5 = row.createCell(7);
                    // if (record.getR43_net_amt() != null) {
                    // R43Cell5.setCellValue(record.getR43_net_amt().doubleValue());
                    // R43Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell5.setCellValue("");
                    // R43Cell5.setCellStyle(textStyle);
                    // }
                    // // R43 Col I
                    // Cell R43Cell6 = row.createCell(8);
                    // if (record.getR43_net_amt_bwp() != null) {
                    // R43Cell6.setCellValue(record.getR43_net_amt_bwp().doubleValue());
                    // R43Cell6.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell6.setCellValue("");
                    // R43Cell6.setCellStyle(textStyle);
                    // }
                    // // R43 Col J
                    // Cell R43Cell7 = row.createCell(9);
                    // if (record.getR43_bal_sub() != null) {
                    // R43Cell7.setCellValue(record.getR43_bal_sub().doubleValue());
                    // R43Cell7.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell7.setCellValue("");
                    // R43Cell7.setCellStyle(textStyle);
                    // }
                    // // R43 Col K
                    // Cell R43Cell8 = row.createCell(10);
                    // if (record.getR43_bal_sub_bwp() != null) {
                    // R43Cell8.setCellValue(record.getR43_bal_sub_bwp().doubleValue());
                    // R43Cell8.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell8.setCellValue("");
                    // R43Cell8.setCellStyle(textStyle);
                    // }
                    // // R43 Col L
                    // Cell R43Cell9 = row.createCell(11);
                    // if (record.getR43_bal_sub_diaries() != null) {
                    // R43Cell9.setCellValue(record.getR43_bal_sub_diaries().doubleValue());
                    // R43Cell9.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell9.setCellValue("");
                    // R43Cell9.setCellStyle(textStyle);
                    // }
                    // // R43 Col M
                    // Cell R43Cell10 = row.createCell(12);
                    // if (record.getR43_bal_sub_diaries_bwp() != null) {
                    // R43Cell10.setCellValue(record.getR43_bal_sub_diaries_bwp().doubleValue());
                    // R43Cell10.setCellStyle(numberStyle);
                    // } else {
                    // R43Cell10.setCellValue("");
                    // R43Cell10.setCellStyle(textStyle);
                    // }

                    row = sheet.getRow(47);
                    Cell R48Cell1 = row.createCell(3);
                    if (record.getR48_fig_bal_sheet() != null) {
                        R48Cell1.setCellValue(record.getR48_fig_bal_sheet().doubleValue());
                        R48Cell1.setCellStyle(numberStyle);
                    } else {
                        R48Cell1.setCellValue("");
                        R48Cell1.setCellStyle(textStyle);
                    }

                    // R48 Col E
                    Cell R48Cell2 = row.createCell(4);
                    if (record.getR48_fig_bal_sheet_bwp() != null) {
                        R48Cell2.setCellValue(record.getR48_fig_bal_sheet_bwp().doubleValue());
                        R48Cell2.setCellStyle(numberStyle);
                    } else {
                        R48Cell2.setCellValue("");
                        R48Cell2.setCellStyle(textStyle);
                    }

                    // R48 Col F
                    Cell R48Cell3 = row.createCell(5);
                    if (record.getR48_amt_statement_adj() != null) {
                        R48Cell3.setCellValue(record.getR48_amt_statement_adj().doubleValue());
                        R48Cell3.setCellStyle(numberStyle);
                    } else {
                        R48Cell3.setCellValue("");
                        R48Cell3.setCellStyle(textStyle);
                    }
                    // R48 Col G
                    Cell R48Cell4 = row.createCell(6);
                    if (record.getR48_amt_statement_adj_bwp() != null) {
                        R48Cell4.setCellValue(record.getR48_amt_statement_adj_bwp().doubleValue());
                        R48Cell4.setCellStyle(numberStyle);
                    } else {
                        R48Cell4.setCellValue("");
                        R48Cell4.setCellStyle(textStyle);
                    }
                    // // R48 Col H
                    // Cell R48Cell5 = row.createCell(7);
                    // if (record.getR48_net_amt() != null) {
                    // R48Cell5.setCellValue(record.getR48_net_amt().doubleValue());
                    // R48Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R48Cell5.setCellValue("");
                    // R48Cell5.setCellStyle(textStyle);
                    // }
                    // R48 Col I
                    Cell R48Cell6 = row.createCell(8);
                    if (record.getR48_net_amt_bwp() != null) {
                        R48Cell6.setCellValue(record.getR48_net_amt_bwp().doubleValue());
                        R48Cell6.setCellStyle(numberStyle);
                    } else {
                        R48Cell6.setCellValue("");
                        R48Cell6.setCellStyle(textStyle);
                    }
                    // R48 Col J
                    Cell R48Cell7 = row.createCell(9);
                    if (record.getR48_bal_sub() != null) {
                        R48Cell7.setCellValue(record.getR48_bal_sub().doubleValue());
                        R48Cell7.setCellStyle(numberStyle);
                    } else {
                        R48Cell7.setCellValue("");
                        R48Cell7.setCellStyle(textStyle);
                    }
                    // R48 Col K
                    Cell R48Cell8 = row.createCell(10);
                    if (record.getR48_bal_sub_bwp() != null) {
                        R48Cell8.setCellValue(record.getR48_bal_sub_bwp().doubleValue());
                        R48Cell8.setCellStyle(numberStyle);
                    } else {
                        R48Cell8.setCellValue("");
                        R48Cell8.setCellStyle(textStyle);
                    }
                    // R48 Col L
                    Cell R48Cell9 = row.createCell(11);
                    if (record.getR48_bal_sub_diaries() != null) {
                        R48Cell9.setCellValue(record.getR48_bal_sub_diaries().doubleValue());
                        R48Cell9.setCellStyle(numberStyle);
                    } else {
                        R48Cell9.setCellValue("");
                        R48Cell9.setCellStyle(textStyle);
                    }
                    // R48 Col M
                    Cell R48Cell10 = row.createCell(12);
                    if (record.getR48_bal_sub_diaries_bwp() != null) {
                        R48Cell10.setCellValue(record.getR48_bal_sub_diaries_bwp().doubleValue());
                        R48Cell10.setCellStyle(numberStyle);
                    } else {
                        R48Cell10.setCellValue("");
                        R48Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(48);
                    Cell R49Cell1 = row.createCell(3);
                    if (record.getR49_fig_bal_sheet() != null) {
                        R49Cell1.setCellValue(record.getR49_fig_bal_sheet().doubleValue());
                        R49Cell1.setCellStyle(numberStyle);
                    } else {
                        R49Cell1.setCellValue("");
                        R49Cell1.setCellStyle(textStyle);
                    }

                    // R49 Col E
                    Cell R49Cell2 = row.createCell(4);
                    if (record.getR49_fig_bal_sheet_bwp() != null) {
                        R49Cell2.setCellValue(record.getR49_fig_bal_sheet_bwp().doubleValue());
                        R49Cell2.setCellStyle(numberStyle);
                    } else {
                        R49Cell2.setCellValue("");
                        R49Cell2.setCellStyle(textStyle);
                    }

                    // R49 Col F
                    Cell R49Cell3 = row.createCell(5);
                    if (record.getR49_amt_statement_adj() != null) {
                        R49Cell3.setCellValue(record.getR49_amt_statement_adj().doubleValue());
                        R49Cell3.setCellStyle(numberStyle);
                    } else {
                        R49Cell3.setCellValue("");
                        R49Cell3.setCellStyle(textStyle);
                    }
                    // R49 Col G
                    Cell R49Cell4 = row.createCell(6);
                    if (record.getR49_amt_statement_adj_bwp() != null) {
                        R49Cell4.setCellValue(record.getR49_amt_statement_adj_bwp().doubleValue());
                        R49Cell4.setCellStyle(numberStyle);
                    } else {
                        R49Cell4.setCellValue("");
                        R49Cell4.setCellStyle(textStyle);
                    }
                    // // R49 Col H
                    // Cell R49Cell5 = row.createCell(7);
                    // if (record.getR49_net_amt() != null) {
                    // R49Cell5.setCellValue(record.getR49_net_amt().doubleValue());
                    // R49Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R49Cell5.setCellValue("");
                    // R49Cell5.setCellStyle(textStyle);
                    // }
                    // R49 Col I
                    Cell R49Cell6 = row.createCell(8);
                    if (record.getR49_net_amt_bwp() != null) {
                        R49Cell6.setCellValue(record.getR49_net_amt_bwp().doubleValue());
                        R49Cell6.setCellStyle(numberStyle);
                    } else {
                        R49Cell6.setCellValue("");
                        R49Cell6.setCellStyle(textStyle);
                    }
                    // R49 Col J
                    Cell R49Cell7 = row.createCell(9);
                    if (record.getR49_bal_sub() != null) {
                        R49Cell7.setCellValue(record.getR49_bal_sub().doubleValue());
                        R49Cell7.setCellStyle(numberStyle);
                    } else {
                        R49Cell7.setCellValue("");
                        R49Cell7.setCellStyle(textStyle);
                    }
                    // R49 Col K
                    Cell R49Cell8 = row.createCell(10);
                    if (record.getR49_bal_sub_bwp() != null) {
                        R49Cell8.setCellValue(record.getR49_bal_sub_bwp().doubleValue());
                        R49Cell8.setCellStyle(numberStyle);
                    } else {
                        R49Cell8.setCellValue("");
                        R49Cell8.setCellStyle(textStyle);
                    }
                    // R49 Col L
                    Cell R49Cell9 = row.createCell(11);
                    if (record.getR49_bal_sub_diaries() != null) {
                        R49Cell9.setCellValue(record.getR49_bal_sub_diaries().doubleValue());
                        R49Cell9.setCellStyle(numberStyle);
                    } else {
                        R49Cell9.setCellValue("");
                        R49Cell9.setCellStyle(textStyle);
                    }
                    // R49 Col M
                    Cell R49Cell10 = row.createCell(12);
                    if (record.getR49_bal_sub_diaries_bwp() != null) {
                        R49Cell10.setCellValue(record.getR49_bal_sub_diaries_bwp().doubleValue());
                        R49Cell10.setCellStyle(numberStyle);
                    } else {
                        R49Cell10.setCellValue("");
                        R49Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(49);
                    Cell R50Cell1 = row.createCell(3);
                    if (record.getR50_fig_bal_sheet() != null) {
                        R50Cell1.setCellValue(record.getR50_fig_bal_sheet().doubleValue());
                        R50Cell1.setCellStyle(numberStyle);
                    } else {
                        R50Cell1.setCellValue("");
                        R50Cell1.setCellStyle(textStyle);
                    }

                    // R50 Col E
                    Cell R50Cell2 = row.createCell(4);
                    if (record.getR50_fig_bal_sheet_bwp() != null) {
                        R50Cell2.setCellValue(record.getR50_fig_bal_sheet_bwp().doubleValue());
                        R50Cell2.setCellStyle(numberStyle);
                    } else {
                        R50Cell2.setCellValue("");
                        R50Cell2.setCellStyle(textStyle);
                    }

                    // R50 Col F
                    Cell R50Cell3 = row.createCell(5);
                    if (record.getR50_amt_statement_adj() != null) {
                        R50Cell3.setCellValue(record.getR50_amt_statement_adj().doubleValue());
                        R50Cell3.setCellStyle(numberStyle);
                    } else {
                        R50Cell3.setCellValue("");
                        R50Cell3.setCellStyle(textStyle);
                    }
                    // R50 Col G
                    Cell R50Cell4 = row.createCell(6);
                    if (record.getR50_amt_statement_adj_bwp() != null) {
                        R50Cell4.setCellValue(record.getR50_amt_statement_adj_bwp().doubleValue());
                        R50Cell4.setCellStyle(numberStyle);
                    } else {
                        R50Cell4.setCellValue("");
                        R50Cell4.setCellStyle(textStyle);
                    }
                    // // R50 Col H
                    // Cell R50Cell5 = row.createCell(7);
                    // if (record.getR50_net_amt() != null) {
                    // R50Cell5.setCellValue(record.getR50_net_amt().doubleValue());
                    // R50Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R50Cell5.setCellValue("");
                    // R50Cell5.setCellStyle(textStyle);
                    // }
                    // R50 Col I
                    Cell R50Cell6 = row.createCell(8);
                    if (record.getR50_net_amt_bwp() != null) {
                        R50Cell6.setCellValue(record.getR50_net_amt_bwp().doubleValue());
                        R50Cell6.setCellStyle(numberStyle);
                    } else {
                        R50Cell6.setCellValue("");
                        R50Cell6.setCellStyle(textStyle);
                    }
                    // R50 Col J
                    Cell R50Cell7 = row.createCell(9);
                    if (record.getR50_bal_sub() != null) {
                        R50Cell7.setCellValue(record.getR50_bal_sub().doubleValue());
                        R50Cell7.setCellStyle(numberStyle);
                    } else {
                        R50Cell7.setCellValue("");
                        R50Cell7.setCellStyle(textStyle);
                    }
                    // R50 Col K
                    Cell R50Cell8 = row.createCell(10);
                    if (record.getR50_bal_sub_bwp() != null) {
                        R50Cell8.setCellValue(record.getR50_bal_sub_bwp().doubleValue());
                        R50Cell8.setCellStyle(numberStyle);
                    } else {
                        R50Cell8.setCellValue("");
                        R50Cell8.setCellStyle(textStyle);
                    }
                    // R50 Col L
                    Cell R50Cell9 = row.createCell(11);
                    if (record.getR50_bal_sub_diaries() != null) {
                        R50Cell9.setCellValue(record.getR50_bal_sub_diaries().doubleValue());
                        R50Cell9.setCellStyle(numberStyle);
                    } else {
                        R50Cell9.setCellValue("");
                        R50Cell9.setCellStyle(textStyle);
                    }
                    // R50 Col M
                    Cell R50Cell10 = row.createCell(12);
                    if (record.getR50_bal_sub_diaries_bwp() != null) {
                        R50Cell10.setCellValue(record.getR50_bal_sub_diaries_bwp().doubleValue());
                        R50Cell10.setCellStyle(numberStyle);
                    } else {
                        R50Cell10.setCellValue("");
                        R50Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(50);
                    Cell R51Cell1 = row.createCell(3);
                    if (record.getR51_fig_bal_sheet() != null) {
                        R51Cell1.setCellValue(record.getR51_fig_bal_sheet().doubleValue());
                        R51Cell1.setCellStyle(numberStyle);
                    } else {
                        R51Cell1.setCellValue("");
                        R51Cell1.setCellStyle(textStyle);
                    }

                    // R51 Col E
                    Cell R51Cell2 = row.createCell(4);
                    if (record.getR51_fig_bal_sheet_bwp() != null) {
                        R51Cell2.setCellValue(record.getR51_fig_bal_sheet_bwp().doubleValue());
                        R51Cell2.setCellStyle(numberStyle);
                    } else {
                        R51Cell2.setCellValue("");
                        R51Cell2.setCellStyle(textStyle);
                    }

                    // R51 Col F
                    Cell R51Cell3 = row.createCell(5);
                    if (record.getR51_amt_statement_adj() != null) {
                        R51Cell3.setCellValue(record.getR51_amt_statement_adj().doubleValue());
                        R51Cell3.setCellStyle(numberStyle);
                    } else {
                        R51Cell3.setCellValue("");
                        R51Cell3.setCellStyle(textStyle);
                    }
                    // R51 Col G
                    Cell R51Cell4 = row.createCell(6);
                    if (record.getR51_amt_statement_adj_bwp() != null) {
                        R51Cell4.setCellValue(record.getR51_amt_statement_adj_bwp().doubleValue());
                        R51Cell4.setCellStyle(numberStyle);
                    } else {
                        R51Cell4.setCellValue("");
                        R51Cell4.setCellStyle(textStyle);
                    }
                    // // R51 Col H
                    // Cell R51Cell5 = row.createCell(7);
                    // if (record.getR51_net_amt() != null) {
                    // R51Cell5.setCellValue(record.getR51_net_amt().doubleValue());
                    // R51Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R51Cell5.setCellValue("");
                    // R51Cell5.setCellStyle(textStyle);
                    // }
                    // R51 Col I
                    Cell R51Cell6 = row.createCell(8);
                    if (record.getR51_net_amt_bwp() != null) {
                        R51Cell6.setCellValue(record.getR51_net_amt_bwp().doubleValue());
                        R51Cell6.setCellStyle(numberStyle);
                    } else {
                        R51Cell6.setCellValue("");
                        R51Cell6.setCellStyle(textStyle);
                    }
                    // R51 Col J
                    Cell R51Cell7 = row.createCell(9);
                    if (record.getR51_bal_sub() != null) {
                        R51Cell7.setCellValue(record.getR51_bal_sub().doubleValue());
                        R51Cell7.setCellStyle(numberStyle);
                    } else {
                        R51Cell7.setCellValue("");
                        R51Cell7.setCellStyle(textStyle);
                    }
                    // R51 Col K
                    Cell R51Cell8 = row.createCell(10);
                    if (record.getR51_bal_sub_bwp() != null) {
                        R51Cell8.setCellValue(record.getR51_bal_sub_bwp().doubleValue());
                        R51Cell8.setCellStyle(numberStyle);
                    } else {
                        R51Cell8.setCellValue("");
                        R51Cell8.setCellStyle(textStyle);
                    }
                    // R51 Col L
                    Cell R51Cell9 = row.createCell(11);
                    if (record.getR51_bal_sub_diaries() != null) {
                        R51Cell9.setCellValue(record.getR51_bal_sub_diaries().doubleValue());
                        R51Cell9.setCellStyle(numberStyle);
                    } else {
                        R51Cell9.setCellValue("");
                        R51Cell9.setCellStyle(textStyle);
                    }
                    // R51 Col M
                    Cell R51Cell10 = row.createCell(12);
                    if (record.getR51_bal_sub_diaries_bwp() != null) {
                        R51Cell10.setCellValue(record.getR51_bal_sub_diaries_bwp().doubleValue());
                        R51Cell10.setCellStyle(numberStyle);
                    } else {
                        R51Cell10.setCellValue("");
                        R51Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(51);
                    Cell R52Cell1 = row.createCell(3);
                    if (record.getR52_fig_bal_sheet() != null) {
                        R52Cell1.setCellValue(record.getR52_fig_bal_sheet().doubleValue());
                        R52Cell1.setCellStyle(numberStyle);
                    } else {
                        R52Cell1.setCellValue("");
                        R52Cell1.setCellStyle(textStyle);
                    }

                    // R52 Col E
                    Cell R52Cell2 = row.createCell(4);
                    if (record.getR52_fig_bal_sheet_bwp() != null) {
                        R52Cell2.setCellValue(record.getR52_fig_bal_sheet_bwp().doubleValue());
                        R52Cell2.setCellStyle(numberStyle);
                    } else {
                        R52Cell2.setCellValue("");
                        R52Cell2.setCellStyle(textStyle);
                    }

                    // R52 Col F
                    Cell R52Cell3 = row.createCell(5);
                    if (record.getR52_amt_statement_adj() != null) {
                        R52Cell3.setCellValue(record.getR52_amt_statement_adj().doubleValue());
                        R52Cell3.setCellStyle(numberStyle);
                    } else {
                        R52Cell3.setCellValue("");
                        R52Cell3.setCellStyle(textStyle);
                    }
                    // R52 Col G
                    Cell R52Cell4 = row.createCell(6);
                    if (record.getR52_amt_statement_adj_bwp() != null) {
                        R52Cell4.setCellValue(record.getR52_amt_statement_adj_bwp().doubleValue());
                        R52Cell4.setCellStyle(numberStyle);
                    } else {
                        R52Cell4.setCellValue("");
                        R52Cell4.setCellStyle(textStyle);
                    }
                    // // R52 Col H
                    // Cell R52Cell5 = row.createCell(7);
                    // if (record.getR52_net_amt() != null) {
                    // R52Cell5.setCellValue(record.getR52_net_amt().doubleValue());
                    // R52Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R52Cell5.setCellValue("");
                    // R52Cell5.setCellStyle(textStyle);
                    // }
                    // R52 Col I
                    Cell R52Cell6 = row.createCell(8);
                    if (record.getR52_net_amt_bwp() != null) {
                        R52Cell6.setCellValue(record.getR52_net_amt_bwp().doubleValue());
                        R52Cell6.setCellStyle(numberStyle);
                    } else {
                        R52Cell6.setCellValue("");
                        R52Cell6.setCellStyle(textStyle);
                    }
                    // R52 Col J
                    Cell R52Cell7 = row.createCell(9);
                    if (record.getR52_bal_sub() != null) {
                        R52Cell7.setCellValue(record.getR52_bal_sub().doubleValue());
                        R52Cell7.setCellStyle(numberStyle);
                    } else {
                        R52Cell7.setCellValue("");
                        R52Cell7.setCellStyle(textStyle);
                    }
                    // R52 Col K
                    Cell R52Cell8 = row.createCell(10);
                    if (record.getR52_bal_sub_bwp() != null) {
                        R52Cell8.setCellValue(record.getR52_bal_sub_bwp().doubleValue());
                        R52Cell8.setCellStyle(numberStyle);
                    } else {
                        R52Cell8.setCellValue("");
                        R52Cell8.setCellStyle(textStyle);
                    }
                    // R52 Col L
                    Cell R52Cell9 = row.createCell(11);
                    if (record.getR52_bal_sub_diaries() != null) {
                        R52Cell9.setCellValue(record.getR52_bal_sub_diaries().doubleValue());
                        R52Cell9.setCellStyle(numberStyle);
                    } else {
                        R52Cell9.setCellValue("");
                        R52Cell9.setCellStyle(textStyle);
                    }
                    // R52 Col M
                    Cell R52Cell10 = row.createCell(12);
                    if (record.getR52_bal_sub_diaries_bwp() != null) {
                        R52Cell10.setCellValue(record.getR52_bal_sub_diaries_bwp().doubleValue());
                        R52Cell10.setCellStyle(numberStyle);
                    } else {
                        R52Cell10.setCellValue("");
                        R52Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(52);
                    Cell R53Cell1 = row.createCell(3);
                    if (record.getR53_fig_bal_sheet() != null) {
                        R53Cell1.setCellValue(record.getR53_fig_bal_sheet().doubleValue());
                        R53Cell1.setCellStyle(numberStyle);
                    } else {
                        R53Cell1.setCellValue("");
                        R53Cell1.setCellStyle(textStyle);
                    }

                    // R53 Col E
                    Cell R53Cell2 = row.createCell(4);
                    if (record.getR53_fig_bal_sheet_bwp() != null) {
                        R53Cell2.setCellValue(record.getR53_fig_bal_sheet_bwp().doubleValue());
                        R53Cell2.setCellStyle(numberStyle);
                    } else {
                        R53Cell2.setCellValue("");
                        R53Cell2.setCellStyle(textStyle);
                    }

                    // R53 Col F
                    Cell R53Cell3 = row.createCell(5);
                    if (record.getR53_amt_statement_adj() != null) {
                        R53Cell3.setCellValue(record.getR53_amt_statement_adj().doubleValue());
                        R53Cell3.setCellStyle(numberStyle);
                    } else {
                        R53Cell3.setCellValue("");
                        R53Cell3.setCellStyle(textStyle);
                    }
                    // R53 Col G
                    Cell R53Cell4 = row.createCell(6);
                    if (record.getR53_amt_statement_adj_bwp() != null) {
                        R53Cell4.setCellValue(record.getR53_amt_statement_adj_bwp().doubleValue());
                        R53Cell4.setCellStyle(numberStyle);
                    } else {
                        R53Cell4.setCellValue("");
                        R53Cell4.setCellStyle(textStyle);
                    }
                    // // R53 Col H
                    // Cell R53Cell5 = row.createCell(7);
                    // if (record.getR53_net_amt() != null) {
                    // R53Cell5.setCellValue(record.getR53_net_amt().doubleValue());
                    // R53Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R53Cell5.setCellValue("");
                    // R53Cell5.setCellStyle(textStyle);
                    // }
                    // R53 Col I
                    Cell R53Cell6 = row.createCell(8);
                    if (record.getR53_net_amt_bwp() != null) {
                        R53Cell6.setCellValue(record.getR53_net_amt_bwp().doubleValue());
                        R53Cell6.setCellStyle(numberStyle);
                    } else {
                        R53Cell6.setCellValue("");
                        R53Cell6.setCellStyle(textStyle);
                    }
                    // R53 Col J
                    Cell R53Cell7 = row.createCell(9);
                    if (record.getR53_bal_sub() != null) {
                        R53Cell7.setCellValue(record.getR53_bal_sub().doubleValue());
                        R53Cell7.setCellStyle(numberStyle);
                    } else {
                        R53Cell7.setCellValue("");
                        R53Cell7.setCellStyle(textStyle);
                    }
                    // R53 Col K
                    Cell R53Cell8 = row.createCell(10);
                    if (record.getR53_bal_sub_bwp() != null) {
                        R53Cell8.setCellValue(record.getR53_bal_sub_bwp().doubleValue());
                        R53Cell8.setCellStyle(numberStyle);
                    } else {
                        R53Cell8.setCellValue("");
                        R53Cell8.setCellStyle(textStyle);
                    }
                    // R53 Col L
                    Cell R53Cell9 = row.createCell(11);
                    if (record.getR53_bal_sub_diaries() != null) {
                        R53Cell9.setCellValue(record.getR53_bal_sub_diaries().doubleValue());
                        R53Cell9.setCellStyle(numberStyle);
                    } else {
                        R53Cell9.setCellValue("");
                        R53Cell9.setCellStyle(textStyle);
                    }
                    // R53 Col M
                    Cell R53Cell10 = row.createCell(12);
                    if (record.getR53_bal_sub_diaries_bwp() != null) {
                        R53Cell10.setCellValue(record.getR53_bal_sub_diaries_bwp().doubleValue());
                        R53Cell10.setCellStyle(numberStyle);
                    } else {
                        R53Cell10.setCellValue("");
                        R53Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(53);
                    Cell R54Cell1 = row.createCell(3);
                    if (record1.getR54_fig_bal_sheet() != null) {
                        R54Cell1.setCellValue(record1.getR54_fig_bal_sheet().doubleValue());
                        R54Cell1.setCellStyle(numberStyle);
                    } else {
                        R54Cell1.setCellValue("");
                        R54Cell1.setCellStyle(textStyle);
                    }

                    // R54 Col E
                    Cell R54Cell2 = row.createCell(4);
                    if (record1.getR54_fig_bal_sheet_bwp() != null) {
                        R54Cell2.setCellValue(record1.getR54_fig_bal_sheet_bwp().doubleValue());
                        R54Cell2.setCellStyle(numberStyle);
                    } else {
                        R54Cell2.setCellValue("");
                        R54Cell2.setCellStyle(textStyle);
                    }

                    // R54 Col F
                    Cell R54Cell3 = row.createCell(5);
                    if (record1.getR54_amt_statement_adj() != null) {
                        R54Cell3.setCellValue(record1.getR54_amt_statement_adj().doubleValue());
                        R54Cell3.setCellStyle(numberStyle);
                    } else {
                        R54Cell3.setCellValue("");
                        R54Cell3.setCellStyle(textStyle);
                    }
                    // R54 Col G
                    Cell R54Cell4 = row.createCell(6);
                    if (record1.getR54_amt_statement_adj_bwp() != null) {
                        R54Cell4.setCellValue(record1.getR54_amt_statement_adj_bwp().doubleValue());
                        R54Cell4.setCellStyle(numberStyle);
                    } else {
                        R54Cell4.setCellValue("");
                        R54Cell4.setCellStyle(textStyle);
                    }
                    // // R54 Col H
                    // Cell R54Cell5 = row.createCell(7);
                    // if (record1.getR54_net_amt() != null) {
                    // R54Cell5.setCellValue(record1.getR54_net_amt().doubleValue());
                    // R54Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R54Cell5.setCellValue("");
                    // R54Cell5.setCellStyle(textStyle);
                    // }
                    // R54 Col I
                    Cell R54Cell6 = row.createCell(8);
                    if (record1.getR54_net_amt_bwp() != null) {
                        R54Cell6.setCellValue(record1.getR54_net_amt_bwp().doubleValue());
                        R54Cell6.setCellStyle(numberStyle);
                    } else {
                        R54Cell6.setCellValue("");
                        R54Cell6.setCellStyle(textStyle);
                    }
                    // R54 Col J
                    Cell R54Cell7 = row.createCell(9);
                    if (record1.getR54_bal_sub() != null) {
                        R54Cell7.setCellValue(record1.getR54_bal_sub().doubleValue());
                        R54Cell7.setCellStyle(numberStyle);
                    } else {
                        R54Cell7.setCellValue("");
                        R54Cell7.setCellStyle(textStyle);
                    }
                    // R54 Col K
                    Cell R54Cell8 = row.createCell(10);
                    if (record1.getR54_bal_sub_bwp() != null) {
                        R54Cell8.setCellValue(record1.getR54_bal_sub_bwp().doubleValue());
                        R54Cell8.setCellStyle(numberStyle);
                    } else {
                        R54Cell8.setCellValue("");
                        R54Cell8.setCellStyle(textStyle);
                    }
                    // R54 Col L
                    Cell R54Cell9 = row.createCell(11);
                    if (record1.getR54_bal_sub_diaries() != null) {
                        R54Cell9.setCellValue(record1.getR54_bal_sub_diaries().doubleValue());
                        R54Cell9.setCellStyle(numberStyle);
                    } else {
                        R54Cell9.setCellValue("");
                        R54Cell9.setCellStyle(textStyle);
                    }
                    // R54 Col M
                    Cell R54Cell10 = row.createCell(12);
                    if (record1.getR54_bal_sub_diaries_bwp() != null) {
                        R54Cell10.setCellValue(record1.getR54_bal_sub_diaries_bwp().doubleValue());
                        R54Cell10.setCellStyle(numberStyle);
                    } else {
                        R54Cell10.setCellValue("");
                        R54Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(54);
                    Cell R55Cell1 = row.createCell(3);
                    if (record.getR55_fig_bal_sheet() != null) {
                        R55Cell1.setCellValue(record.getR55_fig_bal_sheet().doubleValue());
                        R55Cell1.setCellStyle(numberStyle);
                    } else {
                        R55Cell1.setCellValue("");
                        R55Cell1.setCellStyle(textStyle);
                    }

                    // R55 Col E
                    Cell R55Cell2 = row.createCell(4);
                    if (record.getR55_fig_bal_sheet_bwp() != null) {
                        R55Cell2.setCellValue(record.getR55_fig_bal_sheet_bwp().doubleValue());
                        R55Cell2.setCellStyle(numberStyle);
                    } else {
                        R55Cell2.setCellValue("");
                        R55Cell2.setCellStyle(textStyle);
                    }

                    // R55 Col F
                    Cell R55Cell3 = row.createCell(5);
                    if (record.getR55_amt_statement_adj() != null) {
                        R55Cell3.setCellValue(record.getR55_amt_statement_adj().doubleValue());
                        R55Cell3.setCellStyle(numberStyle);
                    } else {
                        R55Cell3.setCellValue("");
                        R55Cell3.setCellStyle(textStyle);
                    }
                    // R55 Col G
                    Cell R55Cell4 = row.createCell(6);
                    if (record.getR55_amt_statement_adj_bwp() != null) {
                        R55Cell4.setCellValue(record.getR55_amt_statement_adj_bwp().doubleValue());
                        R55Cell4.setCellStyle(numberStyle);
                    } else {
                        R55Cell4.setCellValue("");
                        R55Cell4.setCellStyle(textStyle);
                    }
                    // // R55 Col H
                    // Cell R55Cell5 = row.createCell(7);
                    // if (record.getR55_net_amt() != null) {
                    // R55Cell5.setCellValue(record.getR55_net_amt().doubleValue());
                    // R55Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R55Cell5.setCellValue("");
                    // R55Cell5.setCellStyle(textStyle);
                    // }
                    // R55 Col I
                    Cell R55Cell6 = row.createCell(8);
                    if (record.getR55_net_amt_bwp() != null) {
                        R55Cell6.setCellValue(record.getR55_net_amt_bwp().doubleValue());
                        R55Cell6.setCellStyle(numberStyle);
                    } else {
                        R55Cell6.setCellValue("");
                        R55Cell6.setCellStyle(textStyle);
                    }
                    // R55 Col J
                    Cell R55Cell7 = row.createCell(9);
                    if (record.getR55_bal_sub() != null) {
                        R55Cell7.setCellValue(record.getR55_bal_sub().doubleValue());
                        R55Cell7.setCellStyle(numberStyle);
                    } else {
                        R55Cell7.setCellValue("");
                        R55Cell7.setCellStyle(textStyle);
                    }
                    // R55 Col K
                    Cell R55Cell8 = row.createCell(10);
                    if (record.getR55_bal_sub_bwp() != null) {
                        R55Cell8.setCellValue(record.getR55_bal_sub_bwp().doubleValue());
                        R55Cell8.setCellStyle(numberStyle);
                    } else {
                        R55Cell8.setCellValue("");
                        R55Cell8.setCellStyle(textStyle);
                    }
                    // R55 Col L
                    Cell R55Cell9 = row.createCell(11);
                    if (record.getR55_bal_sub_diaries() != null) {
                        R55Cell9.setCellValue(record.getR55_bal_sub_diaries().doubleValue());
                        R55Cell9.setCellStyle(numberStyle);
                    } else {
                        R55Cell9.setCellValue("");
                        R55Cell9.setCellStyle(textStyle);
                    }
                    // R55 Col M
                    Cell R55Cell10 = row.createCell(12);
                    if (record.getR55_bal_sub_diaries_bwp() != null) {
                        R55Cell10.setCellValue(record.getR55_bal_sub_diaries_bwp().doubleValue());
                        R55Cell10.setCellStyle(numberStyle);
                    } else {
                        R55Cell10.setCellValue("");
                        R55Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(55);
                    Cell R56Cell1 = row.createCell(3);
                    if (record.getR56_fig_bal_sheet() != null) {
                        R56Cell1.setCellValue(record.getR56_fig_bal_sheet().doubleValue());
                        R56Cell1.setCellStyle(numberStyle);
                    } else {
                        R56Cell1.setCellValue("");
                        R56Cell1.setCellStyle(textStyle);
                    }

                    // R56 Col E
                    Cell R56Cell2 = row.createCell(4);
                    if (record.getR56_fig_bal_sheet_bwp() != null) {
                        R56Cell2.setCellValue(record.getR56_fig_bal_sheet_bwp().doubleValue());
                        R56Cell2.setCellStyle(numberStyle);
                    } else {
                        R56Cell2.setCellValue("");
                        R56Cell2.setCellStyle(textStyle);
                    }

                    // R56 Col F
                    Cell R56Cell3 = row.createCell(5);
                    if (record.getR56_amt_statement_adj() != null) {
                        R56Cell3.setCellValue(record.getR56_amt_statement_adj().doubleValue());
                        R56Cell3.setCellStyle(numberStyle);
                    } else {
                        R56Cell3.setCellValue("");
                        R56Cell3.setCellStyle(textStyle);
                    }
                    // R56 Col G
                    Cell R56Cell4 = row.createCell(6);
                    if (record.getR56_amt_statement_adj_bwp() != null) {
                        R56Cell4.setCellValue(record.getR56_amt_statement_adj_bwp().doubleValue());
                        R56Cell4.setCellStyle(numberStyle);
                    } else {
                        R56Cell4.setCellValue("");
                        R56Cell4.setCellStyle(textStyle);
                    }
                    // // R56 Col H
                    // Cell R56Cell5 = row.createCell(7);
                    // if (record.getR56_net_amt() != null) {
                    // R56Cell5.setCellValue(record.getR56_net_amt().doubleValue());
                    // R56Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R56Cell5.setCellValue("");
                    // R56Cell5.setCellStyle(textStyle);
                    // }
                    // R56 Col I
                    Cell R56Cell6 = row.createCell(8);
                    if (record.getR56_net_amt_bwp() != null) {
                        R56Cell6.setCellValue(record.getR56_net_amt_bwp().doubleValue());
                        R56Cell6.setCellStyle(numberStyle);
                    } else {
                        R56Cell6.setCellValue("");
                        R56Cell6.setCellStyle(textStyle);
                    }
                    // R56 Col J
                    Cell R56Cell7 = row.createCell(9);
                    if (record.getR56_bal_sub() != null) {
                        R56Cell7.setCellValue(record.getR56_bal_sub().doubleValue());
                        R56Cell7.setCellStyle(numberStyle);
                    } else {
                        R56Cell7.setCellValue("");
                        R56Cell7.setCellStyle(textStyle);
                    }
                    // R56 Col K
                    Cell R56Cell8 = row.createCell(10);
                    if (record.getR56_bal_sub_bwp() != null) {
                        R56Cell8.setCellValue(record.getR56_bal_sub_bwp().doubleValue());
                        R56Cell8.setCellStyle(numberStyle);
                    } else {
                        R56Cell8.setCellValue("");
                        R56Cell8.setCellStyle(textStyle);
                    }
                    // R56 Col L
                    Cell R56Cell9 = row.createCell(11);
                    if (record.getR56_bal_sub_diaries() != null) {
                        R56Cell9.setCellValue(record.getR56_bal_sub_diaries().doubleValue());
                        R56Cell9.setCellStyle(numberStyle);
                    } else {
                        R56Cell9.setCellValue("");
                        R56Cell9.setCellStyle(textStyle);
                    }
                    // R56 Col M
                    Cell R56Cell10 = row.createCell(12);
                    if (record.getR56_bal_sub_diaries_bwp() != null) {
                        R56Cell10.setCellValue(record.getR56_bal_sub_diaries_bwp().doubleValue());
                        R56Cell10.setCellStyle(numberStyle);
                    } else {
                        R56Cell10.setCellValue("");
                        R56Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(56);
                    Cell R57Cell1 = row.createCell(3);
                    if (record.getR57_fig_bal_sheet() != null) {
                        R57Cell1.setCellValue(record.getR57_fig_bal_sheet().doubleValue());
                        R57Cell1.setCellStyle(numberStyle);
                    } else {
                        R57Cell1.setCellValue("");
                        R57Cell1.setCellStyle(textStyle);
                    }

                    // R57 Col E
                    Cell R57Cell2 = row.createCell(4);
                    if (record.getR57_fig_bal_sheet_bwp() != null) {
                        R57Cell2.setCellValue(record.getR57_fig_bal_sheet_bwp().doubleValue());
                        R57Cell2.setCellStyle(numberStyle);
                    } else {
                        R57Cell2.setCellValue("");
                        R57Cell2.setCellStyle(textStyle);
                    }

                    // R57 Col F
                    Cell R57Cell3 = row.createCell(5);
                    if (record.getR57_amt_statement_adj() != null) {
                        R57Cell3.setCellValue(record.getR57_amt_statement_adj().doubleValue());
                        R57Cell3.setCellStyle(numberStyle);
                    } else {
                        R57Cell3.setCellValue("");
                        R57Cell3.setCellStyle(textStyle);
                    }
                    // R57 Col G
                    Cell R57Cell4 = row.createCell(6);
                    if (record.getR57_amt_statement_adj_bwp() != null) {
                        R57Cell4.setCellValue(record.getR57_amt_statement_adj_bwp().doubleValue());
                        R57Cell4.setCellStyle(numberStyle);
                    } else {
                        R57Cell4.setCellValue("");
                        R57Cell4.setCellStyle(textStyle);
                    }
                    // // R57 Col H
                    // Cell R57Cell5 = row.createCell(7);
                    // if (record.getR57_net_amt() != null) {
                    // R57Cell5.setCellValue(record.getR57_net_amt().doubleValue());
                    // R57Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R57Cell5.setCellValue("");
                    // R57Cell5.setCellStyle(textStyle);
                    // }
                    // R57 Col I
                    Cell R57Cell6 = row.createCell(8);
                    if (record.getR57_net_amt_bwp() != null) {
                        R57Cell6.setCellValue(record.getR57_net_amt_bwp().doubleValue());
                        R57Cell6.setCellStyle(numberStyle);
                    } else {
                        R57Cell6.setCellValue("");
                        R57Cell6.setCellStyle(textStyle);
                    }
                    // R57 Col J
                    Cell R57Cell7 = row.createCell(9);
                    if (record.getR57_bal_sub() != null) {
                        R57Cell7.setCellValue(record.getR57_bal_sub().doubleValue());
                        R57Cell7.setCellStyle(numberStyle);
                    } else {
                        R57Cell7.setCellValue("");
                        R57Cell7.setCellStyle(textStyle);
                    }
                    // R57 Col K
                    Cell R57Cell8 = row.createCell(10);
                    if (record.getR57_bal_sub_bwp() != null) {
                        R57Cell8.setCellValue(record.getR57_bal_sub_bwp().doubleValue());
                        R57Cell8.setCellStyle(numberStyle);
                    } else {
                        R57Cell8.setCellValue("");
                        R57Cell8.setCellStyle(textStyle);
                    }
                    // R57 Col L
                    Cell R57Cell9 = row.createCell(11);
                    if (record.getR57_bal_sub_diaries() != null) {
                        R57Cell9.setCellValue(record.getR57_bal_sub_diaries().doubleValue());
                        R57Cell9.setCellStyle(numberStyle);
                    } else {
                        R57Cell9.setCellValue("");
                        R57Cell9.setCellStyle(textStyle);
                    }
                    // R57 Col M
                    Cell R57Cell10 = row.createCell(12);
                    if (record.getR57_bal_sub_diaries_bwp() != null) {
                        R57Cell10.setCellValue(record.getR57_bal_sub_diaries_bwp().doubleValue());
                        R57Cell10.setCellStyle(numberStyle);
                    } else {
                        R57Cell10.setCellValue("");
                        R57Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(57);
                    Cell R58Cell1 = row.createCell(3);
                    if (record.getR58_fig_bal_sheet() != null) {
                        R58Cell1.setCellValue(record.getR58_fig_bal_sheet().doubleValue());
                        R58Cell1.setCellStyle(numberStyle);
                    } else {
                        R58Cell1.setCellValue("");
                        R58Cell1.setCellStyle(textStyle);
                    }

                    // R58 Col E
                    Cell R58Cell2 = row.createCell(4);
                    if (record.getR58_fig_bal_sheet_bwp() != null) {
                        R58Cell2.setCellValue(record.getR58_fig_bal_sheet_bwp().doubleValue());
                        R58Cell2.setCellStyle(numberStyle);
                    } else {
                        R58Cell2.setCellValue("");
                        R58Cell2.setCellStyle(textStyle);
                    }

                    // R58 Col F
                    Cell R58Cell3 = row.createCell(5);
                    if (record.getR58_amt_statement_adj() != null) {
                        R58Cell3.setCellValue(record.getR58_amt_statement_adj().doubleValue());
                        R58Cell3.setCellStyle(numberStyle);
                    } else {
                        R58Cell3.setCellValue("");
                        R58Cell3.setCellStyle(textStyle);
                    }
                    // R58 Col G
                    Cell R58Cell4 = row.createCell(6);
                    if (record.getR58_amt_statement_adj_bwp() != null) {
                        R58Cell4.setCellValue(record.getR58_amt_statement_adj_bwp().doubleValue());
                        R58Cell4.setCellStyle(numberStyle);
                    } else {
                        R58Cell4.setCellValue("");
                        R58Cell4.setCellStyle(textStyle);
                    }
                    // // R58 Col H
                    // Cell R58Cell5 = row.createCell(7);
                    // if (record.getR58_net_amt() != null) {
                    // R58Cell5.setCellValue(record.getR58_net_amt().doubleValue());
                    // R58Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R58Cell5.setCellValue("");
                    // R58Cell5.setCellStyle(textStyle);
                    // }
                    // R58 Col I
                    Cell R58Cell6 = row.createCell(8);
                    if (record.getR58_net_amt_bwp() != null) {
                        R58Cell6.setCellValue(record.getR58_net_amt_bwp().doubleValue());
                        R58Cell6.setCellStyle(numberStyle);
                    } else {
                        R58Cell6.setCellValue("");
                        R58Cell6.setCellStyle(textStyle);
                    }
                    // R58 Col J
                    Cell R58Cell7 = row.createCell(9);
                    if (record.getR58_bal_sub() != null) {
                        R58Cell7.setCellValue(record.getR58_bal_sub().doubleValue());
                        R58Cell7.setCellStyle(numberStyle);
                    } else {
                        R58Cell7.setCellValue("");
                        R58Cell7.setCellStyle(textStyle);
                    }
                    // R58 Col K
                    Cell R58Cell8 = row.createCell(10);
                    if (record.getR58_bal_sub_bwp() != null) {
                        R58Cell8.setCellValue(record.getR58_bal_sub_bwp().doubleValue());
                        R58Cell8.setCellStyle(numberStyle);
                    } else {
                        R58Cell8.setCellValue("");
                        R58Cell8.setCellStyle(textStyle);
                    }
                    // R58 Col L
                    Cell R58Cell9 = row.createCell(11);
                    if (record.getR58_bal_sub_diaries() != null) {
                        R58Cell9.setCellValue(record.getR58_bal_sub_diaries().doubleValue());
                        R58Cell9.setCellStyle(numberStyle);
                    } else {
                        R58Cell9.setCellValue("");
                        R58Cell9.setCellStyle(textStyle);
                    }
                    // R58 Col M
                    Cell R58Cell10 = row.createCell(12);
                    if (record.getR58_bal_sub_diaries_bwp() != null) {
                        R58Cell10.setCellValue(record.getR58_bal_sub_diaries_bwp().doubleValue());
                        R58Cell10.setCellStyle(numberStyle);
                    } else {
                        R58Cell10.setCellValue("");
                        R58Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(58);
                    Cell R59Cell1 = row.createCell(3);
                    if (record.getR59_fig_bal_sheet() != null) {
                        R59Cell1.setCellValue(record.getR59_fig_bal_sheet().doubleValue());
                        R59Cell1.setCellStyle(numberStyle);
                    } else {
                        R59Cell1.setCellValue("");
                        R59Cell1.setCellStyle(textStyle);
                    }

                    // R59 Col E
                    Cell R59Cell2 = row.createCell(4);
                    if (record.getR59_fig_bal_sheet_bwp() != null) {
                        R59Cell2.setCellValue(record.getR59_fig_bal_sheet_bwp().doubleValue());
                        R59Cell2.setCellStyle(numberStyle);
                    } else {
                        R59Cell2.setCellValue("");
                        R59Cell2.setCellStyle(textStyle);
                    }

                    // R59 Col F
                    Cell R59Cell3 = row.createCell(5);
                    if (record.getR59_amt_statement_adj() != null) {
                        R59Cell3.setCellValue(record.getR59_amt_statement_adj().doubleValue());
                        R59Cell3.setCellStyle(numberStyle);
                    } else {
                        R59Cell3.setCellValue("");
                        R59Cell3.setCellStyle(textStyle);
                    }
                    // R59 Col G
                    Cell R59Cell4 = row.createCell(6);
                    if (record.getR59_amt_statement_adj_bwp() != null) {
                        R59Cell4.setCellValue(record.getR59_amt_statement_adj_bwp().doubleValue());
                        R59Cell4.setCellStyle(numberStyle);
                    } else {
                        R59Cell4.setCellValue("");
                        R59Cell4.setCellStyle(textStyle);
                    }
                    // // R59 Col H
                    // Cell R59Cell5 = row.createCell(7);
                    // if (record.getR59_net_amt() != null) {
                    // R59Cell5.setCellValue(record.getR59_net_amt().doubleValue());
                    // R59Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R59Cell5.setCellValue("");
                    // R59Cell5.setCellStyle(textStyle);
                    // }
                    // R59 Col I
                    Cell R59Cell6 = row.createCell(8);
                    if (record.getR59_net_amt_bwp() != null) {
                        R59Cell6.setCellValue(record.getR59_net_amt_bwp().doubleValue());
                        R59Cell6.setCellStyle(numberStyle);
                    } else {
                        R59Cell6.setCellValue("");
                        R59Cell6.setCellStyle(textStyle);
                    }
                    // R59 Col J
                    Cell R59Cell7 = row.createCell(9);
                    if (record.getR59_bal_sub() != null) {
                        R59Cell7.setCellValue(record.getR59_bal_sub().doubleValue());
                        R59Cell7.setCellStyle(numberStyle);
                    } else {
                        R59Cell7.setCellValue("");
                        R59Cell7.setCellStyle(textStyle);
                    }
                    // R59 Col K
                    Cell R59Cell8 = row.createCell(10);
                    if (record.getR59_bal_sub_bwp() != null) {
                        R59Cell8.setCellValue(record.getR59_bal_sub_bwp().doubleValue());
                        R59Cell8.setCellStyle(numberStyle);
                    } else {
                        R59Cell8.setCellValue("");
                        R59Cell8.setCellStyle(textStyle);
                    }
                    // R59 Col L
                    Cell R59Cell9 = row.createCell(11);
                    if (record.getR59_bal_sub_diaries() != null) {
                        R59Cell9.setCellValue(record.getR59_bal_sub_diaries().doubleValue());
                        R59Cell9.setCellStyle(numberStyle);
                    } else {
                        R59Cell9.setCellValue("");
                        R59Cell9.setCellStyle(textStyle);
                    }
                    // R59 Col M
                    Cell R59Cell10 = row.createCell(12);
                    if (record.getR59_bal_sub_diaries_bwp() != null) {
                        R59Cell10.setCellValue(record.getR59_bal_sub_diaries_bwp().doubleValue());
                        R59Cell10.setCellStyle(numberStyle);
                    } else {
                        R59Cell10.setCellValue("");
                        R59Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(59);
                    Cell R60Cell1 = row.createCell(3);
                    if (record.getR60_fig_bal_sheet() != null) {
                        R60Cell1.setCellValue(record.getR60_fig_bal_sheet().doubleValue());
                        R60Cell1.setCellStyle(numberStyle);
                    } else {
                        R60Cell1.setCellValue("");
                        R60Cell1.setCellStyle(textStyle);
                    }

                    // R60 Col E
                    Cell R60Cell2 = row.createCell(4);
                    if (record.getR60_fig_bal_sheet_bwp() != null) {
                        R60Cell2.setCellValue(record.getR60_fig_bal_sheet_bwp().doubleValue());
                        R60Cell2.setCellStyle(numberStyle);
                    } else {
                        R60Cell2.setCellValue("");
                        R60Cell2.setCellStyle(textStyle);
                    }

                    // R60 Col F
                    Cell R60Cell3 = row.createCell(5);
                    if (record.getR60_amt_statement_adj() != null) {
                        R60Cell3.setCellValue(record.getR60_amt_statement_adj().doubleValue());
                        R60Cell3.setCellStyle(numberStyle);
                    } else {
                        R60Cell3.setCellValue("");
                        R60Cell3.setCellStyle(textStyle);
                    }
                    // R60 Col G
                    Cell R60Cell4 = row.createCell(6);
                    if (record.getR60_amt_statement_adj_bwp() != null) {
                        R60Cell4.setCellValue(record.getR60_amt_statement_adj_bwp().doubleValue());
                        R60Cell4.setCellStyle(numberStyle);
                    } else {
                        R60Cell4.setCellValue("");
                        R60Cell4.setCellStyle(textStyle);
                    }
                    // // R60 Col H
                    // Cell R60Cell5 = row.createCell(7);
                    // if (record.getR60_net_amt() != null) {
                    // R60Cell5.setCellValue(record.getR60_net_amt().doubleValue());
                    // R60Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R60Cell5.setCellValue("");
                    // R60Cell5.setCellStyle(textStyle);
                    // }
                    // R60 Col I
                    Cell R60Cell6 = row.createCell(8);
                    if (record.getR60_net_amt_bwp() != null) {
                        R60Cell6.setCellValue(record.getR60_net_amt_bwp().doubleValue());
                        R60Cell6.setCellStyle(numberStyle);
                    } else {
                        R60Cell6.setCellValue("");
                        R60Cell6.setCellStyle(textStyle);
                    }
                    // R60 Col J
                    Cell R60Cell7 = row.createCell(9);
                    if (record.getR60_bal_sub() != null) {
                        R60Cell7.setCellValue(record.getR60_bal_sub().doubleValue());
                        R60Cell7.setCellStyle(numberStyle);
                    } else {
                        R60Cell7.setCellValue("");
                        R60Cell7.setCellStyle(textStyle);
                    }
                    // R60 Col K
                    Cell R60Cell8 = row.createCell(10);
                    if (record.getR60_bal_sub_bwp() != null) {
                        R60Cell8.setCellValue(record.getR60_bal_sub_bwp().doubleValue());
                        R60Cell8.setCellStyle(numberStyle);
                    } else {
                        R60Cell8.setCellValue("");
                        R60Cell8.setCellStyle(textStyle);
                    }
                    // R60 Col L
                    Cell R60Cell9 = row.createCell(11);
                    if (record.getR60_bal_sub_diaries() != null) {
                        R60Cell9.setCellValue(record.getR60_bal_sub_diaries().doubleValue());
                        R60Cell9.setCellStyle(numberStyle);
                    } else {
                        R60Cell9.setCellValue("");
                        R60Cell9.setCellStyle(textStyle);
                    }
                    // R60 Col M
                    Cell R60Cell10 = row.createCell(12);
                    if (record.getR60_bal_sub_diaries_bwp() != null) {
                        R60Cell10.setCellValue(record.getR60_bal_sub_diaries_bwp().doubleValue());
                        R60Cell10.setCellStyle(numberStyle);
                    } else {
                        R60Cell10.setCellValue("");
                        R60Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(60);
                    Cell R61Cell1 = row.createCell(3);
                    if (record1.getR61_fig_bal_sheet() != null) {
                        R61Cell1.setCellValue(record1.getR61_fig_bal_sheet().doubleValue());
                        R61Cell1.setCellStyle(numberStyle);
                    } else {
                        R61Cell1.setCellValue("");
                        R61Cell1.setCellStyle(textStyle);
                    }

                    // R61 Col E
                    Cell R61Cell2 = row.createCell(4);
                    if (record1.getR61_fig_bal_sheet_bwp() != null) {
                        R61Cell2.setCellValue(record1.getR61_fig_bal_sheet_bwp().doubleValue());
                        R61Cell2.setCellStyle(numberStyle);
                    } else {
                        R61Cell2.setCellValue("");
                        R61Cell2.setCellStyle(textStyle);
                    }

                    // R61 Col F
                    Cell R61Cell3 = row.createCell(5);
                    if (record1.getR61_amt_statement_adj() != null) {
                        R61Cell3.setCellValue(record1.getR61_amt_statement_adj().doubleValue());
                        R61Cell3.setCellStyle(numberStyle);
                    } else {
                        R61Cell3.setCellValue("");
                        R61Cell3.setCellStyle(textStyle);
                    }
                    // R61 Col G
                    Cell R61Cell4 = row.createCell(6);
                    if (record1.getR61_amt_statement_adj_bwp() != null) {
                        R61Cell4.setCellValue(record1.getR61_amt_statement_adj_bwp().doubleValue());
                        R61Cell4.setCellStyle(numberStyle);
                    } else {
                        R61Cell4.setCellValue("");
                        R61Cell4.setCellStyle(textStyle);
                    }
                    // // R61 Col H
                    // Cell R61Cell5 = row.createCell(7);
                    // if (record1.getR61_net_amt() != null) {
                    // R61Cell5.setCellValue(record1.getR61_net_amt().doubleValue());
                    // R61Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R61Cell5.setCellValue("");
                    // R61Cell5.setCellStyle(textStyle);
                    // }
                    // R61 Col I
                    Cell R61Cell6 = row.createCell(8);
                    if (record1.getR61_net_amt_bwp() != null) {
                        R61Cell6.setCellValue(record1.getR61_net_amt_bwp().doubleValue());
                        R61Cell6.setCellStyle(numberStyle);
                    } else {
                        R61Cell6.setCellValue("");
                        R61Cell6.setCellStyle(textStyle);
                    }
                    // R61 Col J
                    Cell R61Cell7 = row.createCell(9);
                    if (record1.getR61_bal_sub() != null) {
                        R61Cell7.setCellValue(record1.getR61_bal_sub().doubleValue());
                        R61Cell7.setCellStyle(numberStyle);
                    } else {
                        R61Cell7.setCellValue("");
                        R61Cell7.setCellStyle(textStyle);
                    }
                    // R61 Col K
                    Cell R61Cell8 = row.createCell(10);
                    if (record1.getR61_bal_sub_bwp() != null) {
                        R61Cell8.setCellValue(record1.getR61_bal_sub_bwp().doubleValue());
                        R61Cell8.setCellStyle(numberStyle);
                    } else {
                        R61Cell8.setCellValue("");
                        R61Cell8.setCellStyle(textStyle);
                    }
                    // R61 Col L
                    Cell R61Cell9 = row.createCell(11);
                    if (record1.getR61_bal_sub_diaries() != null) {
                        R61Cell9.setCellValue(record1.getR61_bal_sub_diaries().doubleValue());
                        R61Cell9.setCellStyle(numberStyle);
                    } else {
                        R61Cell9.setCellValue("");
                        R61Cell9.setCellStyle(textStyle);
                    }
                    // R61 Col M
                    Cell R61Cell10 = row.createCell(12);
                    if (record1.getR61_bal_sub_diaries_bwp() != null) {
                        R61Cell10.setCellValue(record1.getR61_bal_sub_diaries_bwp().doubleValue());
                        R61Cell10.setCellStyle(numberStyle);
                    } else {
                        R61Cell10.setCellValue("");
                        R61Cell10.setCellStyle(textStyle);
                    }
                    row = sheet.getRow(61);
                    Cell R62Cell1 = row.createCell(3);
                    if (record.getR62_fig_bal_sheet() != null) {
                        R62Cell1.setCellValue(record.getR62_fig_bal_sheet().doubleValue());
                        R62Cell1.setCellStyle(numberStyle);
                    } else {
                        R62Cell1.setCellValue("");
                        R62Cell1.setCellStyle(textStyle);
                    }

                    // R62 Col E
                    Cell R62Cell2 = row.createCell(4);
                    if (record.getR62_fig_bal_sheet_bwp() != null) {
                        R62Cell2.setCellValue(record.getR62_fig_bal_sheet_bwp().doubleValue());
                        R62Cell2.setCellStyle(numberStyle);
                    } else {
                        R62Cell2.setCellValue("");
                        R62Cell2.setCellStyle(textStyle);
                    }

                    // R62 Col F
                    Cell R62Cell3 = row.createCell(5);
                    if (record.getR62_amt_statement_adj() != null) {
                        R62Cell3.setCellValue(record.getR62_amt_statement_adj().doubleValue());
                        R62Cell3.setCellStyle(numberStyle);
                    } else {
                        R62Cell3.setCellValue("");
                        R62Cell3.setCellStyle(textStyle);
                    }
                    // R62 Col G
                    Cell R62Cell4 = row.createCell(6);
                    if (record.getR62_amt_statement_adj_bwp() != null) {
                        R62Cell4.setCellValue(record.getR62_amt_statement_adj_bwp().doubleValue());
                        R62Cell4.setCellStyle(numberStyle);
                    } else {
                        R62Cell4.setCellValue("");
                        R62Cell4.setCellStyle(textStyle);
                    }
                    // // R62 Col H
                    // Cell R62Cell5 = row.createCell(7);
                    // if (record.getR62_net_amt() != null) {
                    // R62Cell5.setCellValue(record.getR62_net_amt().doubleValue());
                    // R62Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R62Cell5.setCellValue("");
                    // R62Cell5.setCellStyle(textStyle);
                    // }
                    // R62 Col I
                    Cell R62Cell6 = row.createCell(8);
                    if (record.getR62_net_amt_bwp() != null) {
                        R62Cell6.setCellValue(record.getR62_net_amt_bwp().doubleValue());
                        R62Cell6.setCellStyle(numberStyle);
                    } else {
                        R62Cell6.setCellValue("");
                        R62Cell6.setCellStyle(textStyle);
                    }
                    // R62 Col J
                    Cell R62Cell7 = row.createCell(9);
                    if (record.getR62_bal_sub() != null) {
                        R62Cell7.setCellValue(record.getR62_bal_sub().doubleValue());
                        R62Cell7.setCellStyle(numberStyle);
                    } else {
                        R62Cell7.setCellValue("");
                        R62Cell7.setCellStyle(textStyle);
                    }
                    // R62 Col K
                    Cell R62Cell8 = row.createCell(10);
                    if (record.getR62_bal_sub_bwp() != null) {
                        R62Cell8.setCellValue(record.getR62_bal_sub_bwp().doubleValue());
                        R62Cell8.setCellStyle(numberStyle);
                    } else {
                        R62Cell8.setCellValue("");
                        R62Cell8.setCellStyle(textStyle);
                    }
                    // R62 Col L
                    Cell R62Cell9 = row.createCell(11);
                    if (record.getR62_bal_sub_diaries() != null) {
                        R62Cell9.setCellValue(record.getR62_bal_sub_diaries().doubleValue());
                        R62Cell9.setCellStyle(numberStyle);
                    } else {
                        R62Cell9.setCellValue("");
                        R62Cell9.setCellStyle(textStyle);
                    }
                    // R62 Col M
                    Cell R62Cell10 = row.createCell(12);
                    if (record.getR62_bal_sub_diaries_bwp() != null) {
                        R62Cell10.setCellValue(record.getR62_bal_sub_diaries_bwp().doubleValue());
                        R62Cell10.setCellStyle(numberStyle);
                    } else {
                        R62Cell10.setCellValue("");
                        R62Cell10.setCellStyle(textStyle);
                    }
                    // row = sheet.getRow(62);
                    // Cell R63Cell1 = row.createCell(3);
                    // if (record.getR63_fig_bal_sheet() != null) {
                    // R63Cell1.setCellValue(record.getR63_fig_bal_sheet().doubleValue());
                    // R63Cell1.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell1.setCellValue("");
                    // R63Cell1.setCellStyle(textStyle);
                    // }

                    // // R63 Col E
                    // Cell R63Cell2 = row.createCell(4);
                    // if (record.getR63_fig_bal_sheet_bwp() != null) {
                    // R63Cell2.setCellValue(record.getR63_fig_bal_sheet_bwp().doubleValue());
                    // R63Cell2.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell2.setCellValue("");
                    // R63Cell2.setCellStyle(textStyle);
                    // }

                    // // R63 Col F
                    // Cell R63Cell3 = row.createCell(5);
                    // if (record.getR63_amt_statement_adj() != null) {
                    // R63Cell3.setCellValue(record.getR63_amt_statement_adj().doubleValue());
                    // R63Cell3.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell3.setCellValue("");
                    // R63Cell3.setCellStyle(textStyle);
                    // }
                    // // R63 Col G
                    // Cell R63Cell4 = row.createCell(6);
                    // if (record.getR63_amt_statement_adj_bwp() != null) {
                    // R63Cell4.setCellValue(record.getR63_amt_statement_adj_bwp().doubleValue());
                    // R63Cell4.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell4.setCellValue("");
                    // R63Cell4.setCellStyle(textStyle);
                    // }
                    // // R63 Col H
                    // Cell R63Cell5 = row.createCell(7);
                    // if (record.getR63_net_amt() != null) {
                    // R63Cell5.setCellValue(record.getR63_net_amt().doubleValue());
                    // R63Cell5.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell5.setCellValue("");
                    // R63Cell5.setCellStyle(textStyle);
                    // }
                    // // R63 Col I
                    // Cell R63Cell6 = row.createCell(8);
                    // if (record.getR63_net_amt_bwp() != null) {
                    // R63Cell6.setCellValue(record.getR63_net_amt_bwp().doubleValue());
                    // R63Cell6.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell6.setCellValue("");
                    // R63Cell6.setCellStyle(textStyle);
                    // }
                    // // R63 Col J
                    // Cell R63Cell7 = row.createCell(9);
                    // if (record.getR63_bal_sub() != null) {
                    // R63Cell7.setCellValue(record.getR63_bal_sub().doubleValue());
                    // R63Cell7.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell7.setCellValue("");
                    // R63Cell7.setCellStyle(textStyle);
                    // }
                    // // R63 Col K
                    // Cell R63Cell8 = row.createCell(10);
                    // if (record.getR63_bal_sub_bwp() != null) {
                    // R63Cell8.setCellValue(record.getR63_bal_sub_bwp().doubleValue());
                    // R63Cell8.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell8.setCellValue("");
                    // R63Cell8.setCellStyle(textStyle);
                    // }
                    // // R63 Col L
                    // Cell R63Cell9 = row.createCell(11);
                    // if (record.getR63_bal_sub_diaries() != null) {
                    // R63Cell9.setCellValue(record.getR63_bal_sub_diaries().doubleValue());
                    // R63Cell9.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell9.setCellValue("");
                    // R63Cell9.setCellStyle(textStyle);
                    // }
                    // // R63 Col M
                    // Cell R63Cell10 = row.createCell(12);
                    // if (record.getR63_bal_sub_diaries_bwp() != null) {
                    // R63Cell10.setCellValue(record.getR63_bal_sub_diaries_bwp().doubleValue());
                    // R63Cell10.setCellStyle(numberStyle);
                    // } else {
                    // R63Cell10.setCellValue("");
                    // R63Cell10.setCellStyle(textStyle);
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

    public List<Object> getPL_SCHSArchival() {
        List<Object> PL_SCHSArchivallist = new ArrayList<>();
        try {
            PL_SCHSArchivallist = PL_SCHS_Archival_Summary_Repo.getPL_SCHSarchival();

            System.out.println("countser" + PL_SCHSArchivallist.size());

        } catch (Exception e) {
            // Log the exception
            System.err.println("Error fetching PL_SCHSArchivallist Archival data: " + e.getMessage());
            e.printStackTrace();

            // Optionally, you can rethrow it or return empty list
            // throw new RuntimeException("Failed to fetch data", e);
        }
        return PL_SCHSArchivallist;
    }

    public byte[] getPL_SCHSDetailExcel(String filename, String fromdate, String todate, String currency,
            String dtltype,
            String type, String version) {
        try {
            logger.info("Generating Excel for PL_SCHS Details...");
            System.out.println("came to Detail download service");

            if (type.equals("ARCHIVAL") & version != null) {
                byte[] ARCHIVALreport = getPL_SCHSDetailExcelARCHIVAL(filename, fromdate, todate, currency,
                        dtltype, type,
                        version);
                return ARCHIVALreport;
            }

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("PL_SCHSDetails");

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
            List<PL_SCHS_Detail_Entity> reportData = PL_SCHS_detail_repo
                    .getdatabydateList(parsedToDate);

            if (reportData != null && !reportData.isEmpty()) {
                int rowIndex = 1;
                for (PL_SCHS_Detail_Entity item : reportData) {
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
                logger.info("No data found for PL_SCHS — only header will be written.");
            }

            // Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating PL_SCHS Excel", e);
            return new byte[0];
        }
    }

    public byte[] getPL_SCHSDetailExcelARCHIVAL(String filename, String fromdate, String todate,
            String currency,
            String dtltype, String type, String version) {
        try {
            logger.info("Generating Excel for PL_SCHS ARCHIVAL Details...");
            System.out.println("came to ARCHIVAL Detail download service");
            if (type.equals("ARCHIVAL") & version != null) {

            }
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("PL_SCHSDetail");

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
            List<PL_SCHS_Archival_Detail_Entity> reportData = PL_SCHS_Archival_Detail_Repo
                    .getdatabydateList(parsedToDate,
                            version);

            if (reportData != null && !reportData.isEmpty()) {
                int rowIndex = 1;
                for (PL_SCHS_Archival_Detail_Entity item : reportData) {
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
                logger.info("No data found for PL_SCHS — only header will be written.");
            }

            // Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating  PL_SCHS Excel", e);
            return new byte[0];
        }
    }

    @Autowired
    BRRS_PL_SCHS_Detail_Repo brrs_PL_SCHS_detail_repo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
        ModelAndView mv = new ModelAndView("BRRS/PL_SCHS");

        if (acctNo != null) {
            PL_SCHS_Detail_Entity PL_SCHSEntity = brrs_PL_SCHS_detail_repo
                    .findByAcctnumber(acctNo);
            if (PL_SCHSEntity != null && PL_SCHSEntity.getReportDate() != null) {
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
                        .format(PL_SCHSEntity.getReportDate());
                mv.addObject("asondate", formattedDate);
            }
            mv.addObject("PL_SCHSData", PL_SCHSEntity);
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

            PL_SCHS_Detail_Entity existing = brrs_PL_SCHS_detail_repo.findByAcctnumber(acctNo);
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
                brrs_PL_SCHS_detail_repo.save(existing);
                logger.info("Record updated successfully for account {}", acctNo);

                // Format date for procedure
                String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
                        .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

                // Run summary procedure after commit
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        try {
                            logger.info("Transaction committed — calling BRRS_PL_SCHS_SUMMARY_PROCEDURE({})",
                                    formattedDate);
                            jdbcTemplate.update("BEGIN BRRS_PL_SCHS_SUMMARY_PROCEDURE(?); END;",
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
            logger.error("Error updating PL_SCHS record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating record: " + e.getMessage());
        }
    }

}