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

import com.bornfire.brrs.entities.BRRS_Expanded_Regu_BS_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Expanded_Regu_BS_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_DEP3_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Expanded_Regu_BS_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Expanded_Regu_BS_Summary_Repo;
import com.bornfire.brrs.entities.Expanded_Regu_BS_Archival_Detail_Entity;
import com.bornfire.brrs.entities.Expanded_Regu_BS_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Expanded_Regu_BS_Detail_Entity;
import com.bornfire.brrs.entities.Expanded_Regu_BS_Summary_Entity;
import com.bornfire.brrs.entities.Recon_Of_FS_Achival_Summary_Entity;

@Component
@Service

public class BRRS_Expanded_Regu_BS_ReportService<BBRS_Expanded_Regu_BS_Detail_Repo> {
    private static final Logger logger = LoggerFactory.getLogger(BRRS_Expanded_Regu_BS_ReportService.class);

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    private Environment env;
    @Autowired
    BRRS_Expanded_Regu_BS_Detail_Repo Expanded_Regu_BS_Detail_Repo;

    @Autowired
    BRRS_Expanded_Regu_BS_Summary_Repo Expanded_Regu_BS_Summary_Repo;

    @Autowired
    BRRS_Expanded_Regu_BS_Archival_Detail_Repo Expanded_Regu_BS_Archival_Detail_Repo;

    @Autowired
    BRRS_Expanded_Regu_BS_Archival_Summary_Repo Expanded_Regu_BS_Archival_Summary_Repo;

    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

    public ModelAndView getBRRS_Expanded_Regu_BS_View(String reportId, String fromdate, String todate, String currency,
            String dtltype, Pageable pageable, String type, BigDecimal version) {
        ModelAndView mv = new ModelAndView();
        Session hs = sessionFactory.getCurrentSession();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        System.out.println("testing");
        System.out.println(version);

        if (type.equals("ARCHIVAL") & version != null) {
            System.out.println(type);
            List<Expanded_Regu_BS_Archival_Summary_Entity> T1Master = new ArrayList<Expanded_Regu_BS_Archival_Summary_Entity>();
            System.out.println(version);
            try {
                Date d1 = dateformat.parse(todate);
                T1Master = Expanded_Regu_BS_Archival_Summary_Repo.getdatabydateListarchival(todate, version);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            mv.addObject("reportsummary", T1Master);
        } else {
            List<Expanded_Regu_BS_Summary_Entity> T1Master = new ArrayList<Expanded_Regu_BS_Summary_Entity>();
            try {
                Date d1 = dateformat.parse(todate);

                T1Master = Expanded_Regu_BS_Summary_Repo.getdatabydateList(dateformat.parse(todate));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            mv.addObject("reportsummary", T1Master);
        }

        mv.setViewName("BRRS/EXPANDED_REGU_BS");
        mv.addObject("displaymode", "summary");
        System.out.println("scv" + mv.getViewName());
        return mv;
    }

    public ModelAndView getBRRS_Expanded_Regu_BScurrentDtl(String reportId, String fromdate, String todate,
            String currency,
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
            String reportLabel = null;
            String reportAddlCriteria1 = null;
            // ✅ Split the filter string here
            if (filter != null && filter.contains(",")) {
                String[] parts = filter.split(",");
                if (parts.length >= 2) {
                    reportLabel = parts[0];
                    reportAddlCriteria1 = parts[1];
                }
            }
            if ("ARCHIVAL".equals(type) && version != null) {
                System.out.println(type);
                System.out.println(version);
                // 🔹 Archival branch
                List<Expanded_Regu_BS_Archival_Detail_Entity> T1Dt1;
                if (reportLabel != null && reportAddlCriteria1 != null) {
                    T1Dt1 = Expanded_Regu_BS_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel,
                            reportAddlCriteria1,
                            parsedDate, version);
                } else {
                    T1Dt1 = Expanded_Regu_BS_Archival_Detail_Repo.getdatabydateList(todate, version);
                    totalPages = Expanded_Regu_BS_Detail_Repo.getdatacount(parsedDate);
                    System.out.println(T1Dt1.size());
                    mv.addObject("pagination", "YES");
                }
                mv.addObject("reportdetails", T1Dt1);
                mv.addObject("reportmaster12", T1Dt1);
                System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
            } else {

                // 🔹 Current branch
                List<Expanded_Regu_BS_Detail_Entity> T1Dt1;
                if (reportLabel != null && reportAddlCriteria1 != null) {
                    T1Dt1 = Expanded_Regu_BS_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1,
                            parsedDate);
                    System.out.println(T1Dt1.size());
                } else {
                    T1Dt1 = Expanded_Regu_BS_Detail_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
                    System.out.println(T1Dt1.size());
                    totalPages = Expanded_Regu_BS_Detail_Repo.getdatacount(parsedDate);
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
        mv.setViewName("BRRS/EXPANDED_REGU_BS");
        mv.addObject("displaymode", "Details");
        mv.addObject("currentPage", currentPage);
        System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
        mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
        mv.addObject("reportsflag", "reportsflag");
        mv.addObject("menu", reportId);
        return mv;
    }

    public byte[] getExpanded_Regu_BSExcel(String filename, String reportId, String fromdate, String todate,
            String currency,
            String dtltype, String type, BigDecimal version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");

        // ARCHIVAL check
        System.out.println(type + "   " + version);
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
            logger.info("Service: Generating ARCHIVAL report for version {}", version);
            return getSummaryExcelARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);

        }

        // Fetch data
        List<Expanded_Regu_BS_Summary_Entity> dataList = Expanded_Regu_BS_Summary_Repo
                .getdatabydateList(dateformat.parse(todate));

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for Expanded_Regu_BS report. Returning empty result.");
            return new byte[0];
        }

        String templateDir = env.getProperty("output.exportpathtemp");
        String templateFileName = filename;
        System.out.println(filename);
        Path templatePath = Paths.get(templateDir, templateFileName);
        System.out.println(templatePath);

        logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

        if (!Files.exists(templatePath)) {
            throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
        }

        if (!Files.isReadable(templatePath)) {
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
                    Expanded_Regu_BS_Summary_Entity record = dataList.get(i);
                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }
                    row = sheet.getRow(6);
                    // ================= R7 =================
                    Cell R7cell2 = row.getCell(1);
                    if (R7cell2 == null) {
                        R7cell2 = row.createCell(1); // ⚠ only OK if cell exists in template
                    }

                    if (record.getR7_BAL_SHEET_PUB_FS() != null) {
                        R7cell2.setCellValue(record.getR7_BAL_SHEET_PUB_FS().doubleValue());
                    } else {
                         R7cell2.setCellValue(0); // or ""
                    }

                    Cell R7cell3 = row.getCell(2);
                    if (R7cell3 == null) {
                        R7cell3 = row.createCell(2);
                    }

                    if (record.getR7_UNDER_REG_SOC() != null) {
                        R7cell3.setCellValue(record.getR7_UNDER_REG_SOC().doubleValue());
                    } else {
                        R7cell3.setCellValue(0);
                    }

               // ================= R8 =================
                    row = sheet.getRow(7);
                    if (row == null) row = sheet.createRow(7);

                    Cell R8cell2 = row.getCell(1);
                    if (R8cell2 == null) R8cell2 = row.createCell(1);
                    if (record.getR8_BAL_SHEET_PUB_FS() != null) {
                        R8cell2.setCellValue(record.getR8_BAL_SHEET_PUB_FS().doubleValue());
                    } else {
                        R8cell2.setCellValue(0);
                    }

                    Cell R8cell3 = row.getCell(2);
                    if (R8cell3 == null) R8cell3 = row.createCell(2);
                    if (record.getR8_UNDER_REG_SOC() != null) {
                        R8cell3.setCellValue(record.getR8_UNDER_REG_SOC().doubleValue());
                    } else {
                        R8cell3.setCellValue(0);
                    }

                    // ================= R9 =================
                    row = sheet.getRow(8);
                    if (row == null) row = sheet.createRow(8);

                    Cell R9cell2 = row.getCell(1);
                    if (R9cell2 == null) R9cell2 = row.createCell(1);
                    if (record.getR9_BAL_SHEET_PUB_FS() != null) {
                        R9cell2.setCellValue(record.getR9_BAL_SHEET_PUB_FS().doubleValue());
                    } else {
                        R9cell2.setCellValue(0);
                    }

                    Cell R9cell3 = row.getCell(2);
                    if (R9cell3 == null) R9cell3 = row.createCell(2);
                    if (record.getR9_UNDER_REG_SOC() != null) {
                        R9cell3.setCellValue(record.getR9_UNDER_REG_SOC().doubleValue());
                    } else {
                        R9cell3.setCellValue(0);
                    }

                    // ================= R10 =================
                    row = sheet.getRow(9);
                    if (row == null) row = sheet.createRow(9);

                    Cell R10cell2 = row.getCell(1);
                    if (R10cell2 == null) R10cell2 = row.createCell(1);
                    if (record.getR10_BAL_SHEET_PUB_FS() != null) {
                        R10cell2.setCellValue(record.getR10_BAL_SHEET_PUB_FS().doubleValue());
                    } else {
                        R10cell2.setCellValue(0);
                    }

                    Cell R10cell3 = row.getCell(2);
                    if (R10cell3 == null) R10cell3 = row.createCell(2);
                    if (record.getR10_UNDER_REG_SOC() != null) {
                        R10cell3.setCellValue(record.getR10_UNDER_REG_SOC().doubleValue());
                    } else {
                        R10cell3.setCellValue(0);
                    }

                    // ================= R11 =================
                    row = sheet.getRow(10);
                    if (row == null) row = sheet.createRow(10);
                    Cell R11cell2 = row.getCell(1);
                    if (R11cell2 == null) R11cell2 = row.createCell(1);
                    R11cell2.setCellValue(record.getR11_BAL_SHEET_PUB_FS() != null ? record.getR11_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R11cell3 = row.getCell(2);
                    if (R11cell3 == null) R11cell3 = row.createCell(2);
                    R11cell3.setCellValue(record.getR11_UNDER_REG_SOC() != null ? record.getR11_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R12 =================
                    row = sheet.getRow(11);
                    if (row == null) row = sheet.createRow(11);
                    Cell R12cell2 = row.getCell(1);
                    if (R12cell2 == null) R12cell2 = row.createCell(1);
                    R12cell2.setCellValue(record.getR12_BAL_SHEET_PUB_FS() != null ? record.getR12_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R12cell3 = row.getCell(2);
                    if (R12cell3 == null) R12cell3 = row.createCell(2);
                    R12cell3.setCellValue(record.getR12_UNDER_REG_SOC() != null ? record.getR12_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R13 =================
                    row = sheet.getRow(12);
                    if (row == null) row = sheet.createRow(12);
                    Cell R13cell2 = row.getCell(1);
                    if (R13cell2 == null) R13cell2 = row.createCell(1);
                    R13cell2.setCellValue(record.getR13_BAL_SHEET_PUB_FS() != null ? record.getR13_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R13cell3 = row.getCell(2);
                    if (R13cell3 == null) R13cell3 = row.createCell(2);
                    R13cell3.setCellValue(record.getR13_UNDER_REG_SOC() != null ? record.getR13_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R14 =================
                    row = sheet.getRow(13);
                    if (row == null) row = sheet.createRow(13);
                    Cell R14cell2 = row.getCell(1);
                    if (R14cell2 == null) R14cell2 = row.createCell(1);
                    R14cell2.setCellValue(record.getR14_BAL_SHEET_PUB_FS() != null ? record.getR14_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R14cell3 = row.getCell(2);
                    if (R14cell3 == null) R14cell3 = row.createCell(2);
                    R14cell3.setCellValue(record.getR14_UNDER_REG_SOC() != null ? record.getR14_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R15 =================
                    row = sheet.getRow(14);
                    if (row == null) row = sheet.createRow(14);
                    Cell R15cell2 = row.getCell(1);
                    if (R15cell2 == null) R15cell2 = row.createCell(1);
                    R15cell2.setCellValue(record.getR15_BAL_SHEET_PUB_FS() != null ? record.getR15_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R15cell3 = row.getCell(2);
                    if (R15cell3 == null) R15cell3 = row.createCell(2);
                    R15cell3.setCellValue(record.getR15_UNDER_REG_SOC() != null ? record.getR15_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R16 =================
                    row = sheet.getRow(15);
                    if (row == null) row = sheet.createRow(15);
                    Cell R16cell2 = row.getCell(1);
                    if (R16cell2 == null) R16cell2 = row.createCell(1);
                    R16cell2.setCellValue(record.getR16_BAL_SHEET_PUB_FS() != null ? record.getR16_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R16cell3 = row.getCell(2);
                    if (R16cell3 == null) R16cell3 = row.createCell(2);
                    R16cell3.setCellValue(record.getR16_UNDER_REG_SOC() != null ? record.getR16_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R17 =================
                    row = sheet.getRow(16);
                    if (row == null) row = sheet.createRow(16);
                    Cell R17cell2 = row.getCell(1);
                    if (R17cell2 == null) R17cell2 = row.createCell(1);
                    R17cell2.setCellValue(record.getR17_BAL_SHEET_PUB_FS() != null ? record.getR17_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R17cell3 = row.getCell(2);
                    if (R17cell3 == null) R17cell3 = row.createCell(2);
                    R17cell3.setCellValue(record.getR17_UNDER_REG_SOC() != null ? record.getR17_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R18 =================
                    row = sheet.getRow(17);
                    if (row == null) row = sheet.createRow(17);
                    Cell R18cell2 = row.getCell(1);
                    if (R18cell2 == null) R18cell2 = row.createCell(1);
                    R18cell2.setCellValue(record.getR18_BAL_SHEET_PUB_FS() != null ? record.getR18_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R18cell3 = row.getCell(2);
                    if (R18cell3 == null) R18cell3 = row.createCell(2);
                    R18cell3.setCellValue(record.getR18_UNDER_REG_SOC() != null ? record.getR18_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R19 =================
                    row = sheet.getRow(18);
                    if (row == null) row = sheet.createRow(18);
                    Cell R19cell2 = row.getCell(1);
                    if (R19cell2 == null) R19cell2 = row.createCell(1);
                    R19cell2.setCellValue(record.getR19_BAL_SHEET_PUB_FS() != null ? record.getR19_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R19cell3 = row.getCell(2);
                    if (R19cell3 == null) R19cell3 = row.createCell(2);
                    R19cell3.setCellValue(record.getR19_UNDER_REG_SOC() != null ? record.getR19_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R20 =================
                    row = sheet.getRow(19);
                    if (row == null) row = sheet.createRow(19);
                    Cell R20cell2 = row.getCell(1);
                    if (R20cell2 == null) R20cell2 = row.createCell(1);
                    R20cell2.setCellValue(record.getR20_BAL_SHEET_PUB_FS() != null ? record.getR20_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R20cell3 = row.getCell(2);
                    if (R20cell3 == null) R20cell3 = row.createCell(2);
                    R20cell3.setCellValue(record.getR20_UNDER_REG_SOC() != null ? record.getR20_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R21 =================
                    row = sheet.getRow(20);
                    if (row == null) row = sheet.createRow(20);
                    Cell R21cell2 = row.getCell(1);
                    if (R21cell2 == null) R21cell2 = row.createCell(1);
                    R21cell2.setCellValue(record.getR21_BAL_SHEET_PUB_FS() != null ? record.getR21_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R21cell3 = row.getCell(2);
                    if (R21cell3 == null) R21cell3 = row.createCell(2);
                    R21cell3.setCellValue(record.getR21_UNDER_REG_SOC() != null ? record.getR21_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R22 =================
                    row = sheet.getRow(21);
                    if (row == null) row = sheet.createRow(21);
                    Cell R22cell2 = row.getCell(1);
                    if (R22cell2 == null) R22cell2 = row.createCell(1);
                    R22cell2.setCellValue(record.getR22_BAL_SHEET_PUB_FS() != null ? record.getR22_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R22cell3 = row.getCell(2);
                    if (R22cell3 == null) R22cell3 = row.createCell(2);
                    R22cell3.setCellValue(record.getR22_UNDER_REG_SOC() != null ? record.getR22_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R23 =================
                    row = sheet.getRow(22);
                    if (row == null) row = sheet.createRow(22);
                    Cell R23cell2 = row.getCell(1);
                    if (R23cell2 == null) R23cell2 = row.createCell(1);
                    R23cell2.setCellValue(record.getR23_BAL_SHEET_PUB_FS() != null ? record.getR23_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R23cell3 = row.getCell(2);
                    if (R23cell3 == null) R23cell3 = row.createCell(2);
                    R23cell3.setCellValue(record.getR23_UNDER_REG_SOC() != null ? record.getR23_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R24 =================
                    row = sheet.getRow(23);
                    if (row == null) row = sheet.createRow(23);
                    Cell R24cell2 = row.getCell(1);
                    if (R24cell2 == null) R24cell2 = row.createCell(1);
                    R24cell2.setCellValue(record.getR24_BAL_SHEET_PUB_FS() != null ? record.getR24_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R24cell3 = row.getCell(2);
                    if (R24cell3 == null) R24cell3 = row.createCell(2);
                    R24cell3.setCellValue(record.getR24_UNDER_REG_SOC() != null ? record.getR24_UNDER_REG_SOC().doubleValue() : 0);
                   // ================= R26 =================
                    row = sheet.getRow(25);
                    if (row == null) row = sheet.createRow(25);
                    Cell R26cell2 = row.getCell(1);
                    if (R26cell2 == null) R26cell2 = row.createCell(1);
                    R26cell2.setCellValue(record.getR26_BAL_SHEET_PUB_FS() != null ? record.getR26_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R26cell3 = row.getCell(2);
                    if (R26cell3 == null) R26cell3 = row.createCell(2);
                    R26cell3.setCellValue(record.getR26_UNDER_REG_SOC() != null ? record.getR26_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R27 =================
                    row = sheet.getRow(26);
                    if (row == null) row = sheet.createRow(26);
                    Cell R27cell2 = row.getCell(1);
                    if (R27cell2 == null) R27cell2 = row.createCell(1);
                    R27cell2.setCellValue(record.getR27_BAL_SHEET_PUB_FS() != null ? record.getR27_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R27cell3 = row.getCell(2);
                    if (R27cell3 == null) R27cell3 = row.createCell(2);
                    R27cell3.setCellValue(record.getR27_UNDER_REG_SOC() != null ? record.getR27_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R28 =================
                    row = sheet.getRow(27);
                    if (row == null) row = sheet.createRow(27);
                    Cell R28cell2 = row.getCell(1);
                    if (R28cell2 == null) R28cell2 = row.createCell(1);
                    R28cell2.setCellValue(record.getR28_BAL_SHEET_PUB_FS() != null ? record.getR28_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R28cell3 = row.getCell(2);
                    if (R28cell3 == null) R28cell3 = row.createCell(2);
                    R28cell3.setCellValue(record.getR28_UNDER_REG_SOC() != null ? record.getR28_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R29 =================
                    row = sheet.getRow(28);
                    if (row == null) row = sheet.createRow(28);
                    Cell R29cell2 = row.getCell(1);
                    if (R29cell2 == null) R29cell2 = row.createCell(1);
                    R29cell2.setCellValue(record.getR29_BAL_SHEET_PUB_FS() != null ? record.getR29_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R29cell3 = row.getCell(2);
                    if (R29cell3 == null) R29cell3 = row.createCell(2);
                    R29cell3.setCellValue(record.getR29_UNDER_REG_SOC() != null ? record.getR29_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R30 =================
                    row = sheet.getRow(29);
                    if (row == null) row = sheet.createRow(29);
                    Cell R30cell2 = row.getCell(1);
                    if (R30cell2 == null) R30cell2 = row.createCell(1);
                    R30cell2.setCellValue(record.getR30_BAL_SHEET_PUB_FS() != null ? record.getR30_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R30cell3 = row.getCell(2);
                    if (R30cell3 == null) R30cell3 = row.createCell(2);
                    R30cell3.setCellValue(record.getR30_UNDER_REG_SOC() != null ? record.getR30_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R31 =================
                    row = sheet.getRow(30);
                    if (row == null) row = sheet.createRow(30);
                    Cell R31cell2 = row.getCell(1);
                    if (R31cell2 == null) R31cell2 = row.createCell(1);
                    R31cell2.setCellValue(record.getR31_BAL_SHEET_PUB_FS() != null ? record.getR31_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R31cell3 = row.getCell(2);
                    if (R31cell3 == null) R31cell3 = row.createCell(2);
                    R31cell3.setCellValue(record.getR31_UNDER_REG_SOC() != null ? record.getR31_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R32 =================
                    row = sheet.getRow(31);
                    if (row == null) row = sheet.createRow(31);
                    Cell R32cell2 = row.getCell(1);
                    if (R32cell2 == null) R32cell2 = row.createCell(1);
                    R32cell2.setCellValue(record.getR32_BAL_SHEET_PUB_FS() != null ? record.getR32_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R32cell3 = row.getCell(2);
                    if (R32cell3 == null) R32cell3 = row.createCell(2);
                    R32cell3.setCellValue(record.getR32_UNDER_REG_SOC() != null ? record.getR32_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R33 =================
                    row = sheet.getRow(32);
                    if (row == null) row = sheet.createRow(32);
                    Cell R33cell2 = row.getCell(1);
                    if (R33cell2 == null) R33cell2 = row.createCell(1);
                    R33cell2.setCellValue(record.getR33_BAL_SHEET_PUB_FS() != null ? record.getR33_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R33cell3 = row.getCell(2);
                    if (R33cell3 == null) R33cell3 = row.createCell(2);
                    R33cell3.setCellValue(record.getR33_UNDER_REG_SOC() != null ? record.getR33_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R34 =================
                    row = sheet.getRow(33);
                    if (row == null) row = sheet.createRow(33);
                    Cell R34cell2 = row.getCell(1);
                    if (R34cell2 == null) R34cell2 = row.createCell(1);
                    R34cell2.setCellValue(record.getR34_BAL_SHEET_PUB_FS() != null ? record.getR34_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R34cell3 = row.getCell(2);
                    if (R34cell3 == null) R34cell3 = row.createCell(2);
                    R34cell3.setCellValue(record.getR34_UNDER_REG_SOC() != null ? record.getR34_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R35 =================
                    row = sheet.getRow(34);
                    if (row == null) row = sheet.createRow(34);
                    Cell R35cell2 = row.getCell(1);
                    if (R35cell2 == null) R35cell2 = row.createCell(1);
                    R35cell2.setCellValue(record.getR35_BAL_SHEET_PUB_FS() != null ? record.getR35_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R35cell3 = row.getCell(2);
                    if (R35cell3 == null) R35cell3 = row.createCell(2);
                    R35cell3.setCellValue(record.getR35_UNDER_REG_SOC() != null ? record.getR35_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R36 =================
                    row = sheet.getRow(35);
                    if (row == null) row = sheet.createRow(35);
                    Cell R36cell2 = row.getCell(1);
                    if (R36cell2 == null) R36cell2 = row.createCell(1);
                    R36cell2.setCellValue(record.getR36_BAL_SHEET_PUB_FS() != null ? record.getR36_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R36cell3 = row.getCell(2);
                    if (R36cell3 == null) R36cell3 = row.createCell(2);
                    R36cell3.setCellValue(record.getR36_UNDER_REG_SOC() != null ? record.getR36_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R37 =================
                    row = sheet.getRow(36);
                    if (row == null) row = sheet.createRow(36);
                    Cell R37cell2 = row.getCell(1);
                    if (R37cell2 == null) R37cell2 = row.createCell(1);
                    R37cell2.setCellValue(record.getR37_BAL_SHEET_PUB_FS() != null ? record.getR37_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R37cell3 = row.getCell(2);
                    if (R37cell3 == null) R37cell3 = row.createCell(2);
                    R37cell3.setCellValue(record.getR37_UNDER_REG_SOC() != null ? record.getR37_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R38 =================
                    row = sheet.getRow(37);
                    if (row == null) row = sheet.createRow(37);
                    Cell R38cell2 = row.getCell(1);
                    if (R38cell2 == null) R38cell2 = row.createCell(1);
                    R38cell2.setCellValue(record.getR38_BAL_SHEET_PUB_FS() != null ? record.getR38_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R38cell3 = row.getCell(2);
                    if (R38cell3 == null) R38cell3 = row.createCell(2);
                    R38cell3.setCellValue(record.getR38_UNDER_REG_SOC() != null ? record.getR38_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R39 =================
                    row = sheet.getRow(38);
                    if (row == null) row = sheet.createRow(38);
                    Cell R39cell2 = row.getCell(1);
                    if (R39cell2 == null) R39cell2 = row.createCell(1);
                    R39cell2.setCellValue(record.getR39_BAL_SHEET_PUB_FS() != null ? record.getR39_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R39cell3 = row.getCell(2);
                    if (R39cell3 == null) R39cell3 = row.createCell(2);
                    R39cell3.setCellValue(record.getR39_UNDER_REG_SOC() != null ? record.getR39_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R40 =================
                    row = sheet.getRow(39);
                    if (row == null) row = sheet.createRow(39);
                    Cell R40cell2 = row.getCell(1);
                    if (R40cell2 == null) R40cell2 = row.createCell(1);
                    R40cell2.setCellValue(record.getR40_BAL_SHEET_PUB_FS() != null ? record.getR40_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R40cell3 = row.getCell(2);
                    if (R40cell3 == null) R40cell3 = row.createCell(2);
                    R40cell3.setCellValue(record.getR40_UNDER_REG_SOC() != null ? record.getR40_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R41 =================
                    row = sheet.getRow(40);
                    if (row == null) row = sheet.createRow(40);
                    Cell R41cell2 = row.getCell(1);
                    if (R41cell2 == null) R41cell2 = row.createCell(1);
                    R41cell2.setCellValue(record.getR41_BAL_SHEET_PUB_FS() != null ? record.getR41_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R41cell3 = row.getCell(2);
                    if (R41cell3 == null) R41cell3 = row.createCell(2);
                    R41cell3.setCellValue(record.getR41_UNDER_REG_SOC() != null ? record.getR41_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R42 =================
                    row = sheet.getRow(41);
                    if (row == null) row = sheet.createRow(41);
                    Cell R42cell2 = row.getCell(1);
                    if (R42cell2 == null) R42cell2 = row.createCell(1);
                    R42cell2.setCellValue(record.getR42_BAL_SHEET_PUB_FS() != null ? record.getR42_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R42cell3 = row.getCell(2);
                    if (R42cell3 == null) R42cell3 = row.createCell(2);
                    R42cell3.setCellValue(record.getR42_UNDER_REG_SOC() != null ? record.getR42_UNDER_REG_SOC().doubleValue() : 0);

                   // ================= R44 =================
                    row = sheet.getRow(43);
                    if (row == null) row = sheet.createRow(43);
                    Cell R44cell2 = row.getCell(1);
                    if (R44cell2 == null) R44cell2 = row.createCell(1);
                    R44cell2.setCellValue(record.getR44_BAL_SHEET_PUB_FS() != null ? record.getR44_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R44cell3 = row.getCell(2);
                    if (R44cell3 == null) R44cell3 = row.createCell(2);
                    R44cell3.setCellValue(record.getR44_UNDER_REG_SOC() != null ? record.getR44_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R45 =================
                    row = sheet.getRow(44);
                    if (row == null) row = sheet.createRow(44);
                    Cell R45cell2 = row.getCell(1);
                    if (R45cell2 == null) R45cell2 = row.createCell(1);
                    R45cell2.setCellValue(record.getR45_BAL_SHEET_PUB_FS() != null ? record.getR45_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R45cell3 = row.getCell(2);
                    if (R45cell3 == null) R45cell3 = row.createCell(2);
                    R45cell3.setCellValue(record.getR45_UNDER_REG_SOC() != null ? record.getR45_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R46 =================
                    row = sheet.getRow(45);
                    if (row == null) row = sheet.createRow(45);
                    Cell R46cell2 = row.getCell(1);
                    if (R46cell2 == null) R46cell2 = row.createCell(1);
                    R46cell2.setCellValue(record.getR46_BAL_SHEET_PUB_FS() != null ? record.getR46_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R46cell3 = row.getCell(2);
                    if (R46cell3 == null) R46cell3 = row.createCell(2);
                    R46cell3.setCellValue(record.getR46_UNDER_REG_SOC() != null ? record.getR46_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R47 =================
                    row = sheet.getRow(46);
                    if (row == null) row = sheet.createRow(46);
                    Cell R47cell2 = row.getCell(1);
                    if (R47cell2 == null) R47cell2 = row.createCell(1);
                    R47cell2.setCellValue(record.getR47_BAL_SHEET_PUB_FS() != null ? record.getR47_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R47cell3 = row.getCell(2);
                    if (R47cell3 == null) R47cell3 = row.createCell(2);
                    R47cell3.setCellValue(record.getR47_UNDER_REG_SOC() != null ? record.getR47_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R48 =================
                    row = sheet.getRow(47);
                    if (row == null) row = sheet.createRow(47);
                    Cell R48cell2 = row.getCell(1);
                    if (R48cell2 == null) R48cell2 = row.createCell(1);
                    R48cell2.setCellValue(record.getR48_BAL_SHEET_PUB_FS() != null ? record.getR48_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R48cell3 = row.getCell(2);
                    if (R48cell3 == null) R48cell3 = row.createCell(2);
                    R48cell3.setCellValue(record.getR48_UNDER_REG_SOC() != null ? record.getR48_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R49 =================
                    row = sheet.getRow(48);
                    if (row == null) row = sheet.createRow(48);
                    Cell R49cell2 = row.getCell(1);
                    if (R49cell2 == null) R49cell2 = row.createCell(1);
                    R49cell2.setCellValue(record.getR49_BAL_SHEET_PUB_FS() != null ? record.getR49_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R49cell3 = row.getCell(2);
                    if (R49cell3 == null) R49cell3 = row.createCell(2);
                    R49cell3.setCellValue(record.getR49_UNDER_REG_SOC() != null ? record.getR49_UNDER_REG_SOC().doubleValue() : 0);

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

    public byte[] BRRS_Expanded_Regu_BSDetailExcel(String filename, String fromdate, String todate, String currency,
            String dtltype, String type, String version) {

        try {
            logger.info("Generating Excel for BRRSExpanded_Regu_BS Details...");
            System.out.println("came to Detail download service");
            System.out.println("Gopika");
            if (type.equals("ARCHIVAL") & version != null) {
                byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
                        version);
                return ARCHIVALreport;
            }
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("BRRS_Expanded_Regu_BSDetails");

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
            String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
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
            List<Expanded_Regu_BS_Detail_Entity> reportData = Expanded_Regu_BS_Detail_Repo
                    .getdatabydateList(parsedToDate);

            if (reportData != null && !reportData.isEmpty()) {
                int rowIndex = 1;
                for (Expanded_Regu_BS_Detail_Entity item : reportData) {
                    XSSFRow row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(item.getCustId());
                    row.createCell(1).setCellValue(item.getAcctNumber());
                    row.createCell(2).setCellValue(item.getAcctName());
                    // ACCT BALANCE (right aligned, 3 decimal places)
                    Cell balanceCell = row.createCell(3);
                    if (item.getAcctBalanceInPula() != null) {
                        balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
                    } else {
                        balanceCell.setCellValue(0);
                    }
                    balanceCell.setCellStyle(balanceStyle);
                    row.createCell(4).setCellValue(item.getReportLabel());
                    row.createCell(5).setCellValue(item.getReportAddlCriteria1());
                    row.createCell(6)
                            .setCellValue(item.getReportDate() != null
                                    ? new SimpleDateFormat("dd/MM/yyyy").format(item.getReportDate())
                                    : "");
                    // Apply data style for all other cells
                    for (int j = 0; j < 7; j++) {
                        if (j != 3) {
                            row.getCell(j).setCellStyle(dataStyle);
                        }
                    }
                }
            } else {
                logger.info("No data found for BRRS_Expanded_Regu_BS — only header will be written.");
            }
            // Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();
            logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
            return bos.toByteArray();
        } catch (Exception e) {
            logger.error("Error generating BRRS_Q_Expanded_Regu_BS Excel", e);
            return new byte[0];
        }
    }

    public List<Object> getExpanded_Regu_BSArchival() {
        List<Object> Expanded_Regu_BSArchivallist = new ArrayList<>();
        try {
            Expanded_Regu_BSArchivallist = Expanded_Regu_BS_Archival_Summary_Repo.getExpanded_Regu_BS_archival();
            System.out.println("countser" + Expanded_Regu_BSArchivallist.size());
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error fetching Expanded_Regu_BS Archival data: " + e.getMessage());
            e.printStackTrace();

            // Optionally, you can rethrow it or return empty list
            // throw new RuntimeException("Failed to fetch data", e);
        }
        return Expanded_Regu_BSArchivallist;
    }

    public byte[] getSummaryExcelARCHIVAL(String filename, String reportId, String fromdate, String todate,
            String currency, String dtltype, String type, BigDecimal version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");

        if (type.equals("ARCHIVAL") & version != null) {

        }
        List<Expanded_Regu_BS_Archival_Summary_Entity> dataList = Expanded_Regu_BS_Archival_Summary_Repo
                .getdatabydateListarchival(todate, version);

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for Expanded_Regu_BS report. Returning empty result.");
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
                    Expanded_Regu_BS_Archival_Summary_Entity record = dataList.get(i);
                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }
                            row = sheet.getRow(6);
                    // ================= R7 =================
                    Cell R7cell2 = row.getCell(1);
                    if (R7cell2 == null) {
                        R7cell2 = row.createCell(1); // ⚠ only OK if cell exists in template
                    }

                    if (record.getR7_BAL_SHEET_PUB_FS() != null) {
                        R7cell2.setCellValue(record.getR7_BAL_SHEET_PUB_FS().doubleValue());
                    } else {
                         R7cell2.setCellValue(0); // or ""
                    }

                    Cell R7cell3 = row.getCell(2);
                    if (R7cell3 == null) {
                        R7cell3 = row.createCell(2);
                    }

                    if (record.getR7_UNDER_REG_SOC() != null) {
                        R7cell3.setCellValue(record.getR7_UNDER_REG_SOC().doubleValue());
                    } else {
                        R7cell3.setCellValue(0);
                    }

               // ================= R8 =================
                    row = sheet.getRow(7);
                    if (row == null) row = sheet.createRow(7);

                    Cell R8cell2 = row.getCell(1);
                    if (R8cell2 == null) R8cell2 = row.createCell(1);
                    if (record.getR8_BAL_SHEET_PUB_FS() != null) {
                        R8cell2.setCellValue(record.getR8_BAL_SHEET_PUB_FS().doubleValue());
                    } else {
                        R8cell2.setCellValue(0);
                    }

                    Cell R8cell3 = row.getCell(2);
                    if (R8cell3 == null) R8cell3 = row.createCell(2);
                    if (record.getR8_UNDER_REG_SOC() != null) {
                        R8cell3.setCellValue(record.getR8_UNDER_REG_SOC().doubleValue());
                    } else {
                        R8cell3.setCellValue(0);
                    }

                    // ================= R9 =================
                    row = sheet.getRow(8);
                    if (row == null) row = sheet.createRow(8);

                    Cell R9cell2 = row.getCell(1);
                    if (R9cell2 == null) R9cell2 = row.createCell(1);
                    if (record.getR9_BAL_SHEET_PUB_FS() != null) {
                        R9cell2.setCellValue(record.getR9_BAL_SHEET_PUB_FS().doubleValue());
                    } else {
                        R9cell2.setCellValue(0);
                    }

                    Cell R9cell3 = row.getCell(2);
                    if (R9cell3 == null) R9cell3 = row.createCell(2);
                    if (record.getR9_UNDER_REG_SOC() != null) {
                        R9cell3.setCellValue(record.getR9_UNDER_REG_SOC().doubleValue());
                    } else {
                        R9cell3.setCellValue(0);
                    }

                    // ================= R10 =================
                    row = sheet.getRow(9);
                    if (row == null) row = sheet.createRow(9);

                    Cell R10cell2 = row.getCell(1);
                    if (R10cell2 == null) R10cell2 = row.createCell(1);
                    if (record.getR10_BAL_SHEET_PUB_FS() != null) {
                        R10cell2.setCellValue(record.getR10_BAL_SHEET_PUB_FS().doubleValue());
                    } else {
                        R10cell2.setCellValue(0);
                    }

                    Cell R10cell3 = row.getCell(2);
                    if (R10cell3 == null) R10cell3 = row.createCell(2);
                    if (record.getR10_UNDER_REG_SOC() != null) {
                        R10cell3.setCellValue(record.getR10_UNDER_REG_SOC().doubleValue());
                    } else {
                        R10cell3.setCellValue(0);
                    }

                    // ================= R11 =================
                    row = sheet.getRow(10);
                    if (row == null) row = sheet.createRow(10);
                    Cell R11cell2 = row.getCell(1);
                    if (R11cell2 == null) R11cell2 = row.createCell(1);
                    R11cell2.setCellValue(record.getR11_BAL_SHEET_PUB_FS() != null ? record.getR11_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R11cell3 = row.getCell(2);
                    if (R11cell3 == null) R11cell3 = row.createCell(2);
                    R11cell3.setCellValue(record.getR11_UNDER_REG_SOC() != null ? record.getR11_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R12 =================
                    row = sheet.getRow(11);
                    if (row == null) row = sheet.createRow(11);
                    Cell R12cell2 = row.getCell(1);
                    if (R12cell2 == null) R12cell2 = row.createCell(1);
                    R12cell2.setCellValue(record.getR12_BAL_SHEET_PUB_FS() != null ? record.getR12_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R12cell3 = row.getCell(2);
                    if (R12cell3 == null) R12cell3 = row.createCell(2);
                    R12cell3.setCellValue(record.getR12_UNDER_REG_SOC() != null ? record.getR12_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R13 =================
                    row = sheet.getRow(12);
                    if (row == null) row = sheet.createRow(12);
                    Cell R13cell2 = row.getCell(1);
                    if (R13cell2 == null) R13cell2 = row.createCell(1);
                    R13cell2.setCellValue(record.getR13_BAL_SHEET_PUB_FS() != null ? record.getR13_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R13cell3 = row.getCell(2);
                    if (R13cell3 == null) R13cell3 = row.createCell(2);
                    R13cell3.setCellValue(record.getR13_UNDER_REG_SOC() != null ? record.getR13_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R14 =================
                    row = sheet.getRow(13);
                    if (row == null) row = sheet.createRow(13);
                    Cell R14cell2 = row.getCell(1);
                    if (R14cell2 == null) R14cell2 = row.createCell(1);
                    R14cell2.setCellValue(record.getR14_BAL_SHEET_PUB_FS() != null ? record.getR14_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R14cell3 = row.getCell(2);
                    if (R14cell3 == null) R14cell3 = row.createCell(2);
                    R14cell3.setCellValue(record.getR14_UNDER_REG_SOC() != null ? record.getR14_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R15 =================
                    row = sheet.getRow(14);
                    if (row == null) row = sheet.createRow(14);
                    Cell R15cell2 = row.getCell(1);
                    if (R15cell2 == null) R15cell2 = row.createCell(1);
                    R15cell2.setCellValue(record.getR15_BAL_SHEET_PUB_FS() != null ? record.getR15_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R15cell3 = row.getCell(2);
                    if (R15cell3 == null) R15cell3 = row.createCell(2);
                    R15cell3.setCellValue(record.getR15_UNDER_REG_SOC() != null ? record.getR15_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R16 =================
                    row = sheet.getRow(15);
                    if (row == null) row = sheet.createRow(15);
                    Cell R16cell2 = row.getCell(1);
                    if (R16cell2 == null) R16cell2 = row.createCell(1);
                    R16cell2.setCellValue(record.getR16_BAL_SHEET_PUB_FS() != null ? record.getR16_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R16cell3 = row.getCell(2);
                    if (R16cell3 == null) R16cell3 = row.createCell(2);
                    R16cell3.setCellValue(record.getR16_UNDER_REG_SOC() != null ? record.getR16_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R17 =================
                    row = sheet.getRow(16);
                    if (row == null) row = sheet.createRow(16);
                    Cell R17cell2 = row.getCell(1);
                    if (R17cell2 == null) R17cell2 = row.createCell(1);
                    R17cell2.setCellValue(record.getR17_BAL_SHEET_PUB_FS() != null ? record.getR17_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R17cell3 = row.getCell(2);
                    if (R17cell3 == null) R17cell3 = row.createCell(2);
                    R17cell3.setCellValue(record.getR17_UNDER_REG_SOC() != null ? record.getR17_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R18 =================
                    row = sheet.getRow(17);
                    if (row == null) row = sheet.createRow(17);
                    Cell R18cell2 = row.getCell(1);
                    if (R18cell2 == null) R18cell2 = row.createCell(1);
                    R18cell2.setCellValue(record.getR18_BAL_SHEET_PUB_FS() != null ? record.getR18_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R18cell3 = row.getCell(2);
                    if (R18cell3 == null) R18cell3 = row.createCell(2);
                    R18cell3.setCellValue(record.getR18_UNDER_REG_SOC() != null ? record.getR18_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R19 =================
                    row = sheet.getRow(18);
                    if (row == null) row = sheet.createRow(18);
                    Cell R19cell2 = row.getCell(1);
                    if (R19cell2 == null) R19cell2 = row.createCell(1);
                    R19cell2.setCellValue(record.getR19_BAL_SHEET_PUB_FS() != null ? record.getR19_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R19cell3 = row.getCell(2);
                    if (R19cell3 == null) R19cell3 = row.createCell(2);
                    R19cell3.setCellValue(record.getR19_UNDER_REG_SOC() != null ? record.getR19_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R20 =================
                    row = sheet.getRow(19);
                    if (row == null) row = sheet.createRow(19);
                    Cell R20cell2 = row.getCell(1);
                    if (R20cell2 == null) R20cell2 = row.createCell(1);
                    R20cell2.setCellValue(record.getR20_BAL_SHEET_PUB_FS() != null ? record.getR20_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R20cell3 = row.getCell(2);
                    if (R20cell3 == null) R20cell3 = row.createCell(2);
                    R20cell3.setCellValue(record.getR20_UNDER_REG_SOC() != null ? record.getR20_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R21 =================
                    row = sheet.getRow(20);
                    if (row == null) row = sheet.createRow(20);
                    Cell R21cell2 = row.getCell(1);
                    if (R21cell2 == null) R21cell2 = row.createCell(1);
                    R21cell2.setCellValue(record.getR21_BAL_SHEET_PUB_FS() != null ? record.getR21_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R21cell3 = row.getCell(2);
                    if (R21cell3 == null) R21cell3 = row.createCell(2);
                    R21cell3.setCellValue(record.getR21_UNDER_REG_SOC() != null ? record.getR21_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R22 =================
                    row = sheet.getRow(21);
                    if (row == null) row = sheet.createRow(21);
                    Cell R22cell2 = row.getCell(1);
                    if (R22cell2 == null) R22cell2 = row.createCell(1);
                    R22cell2.setCellValue(record.getR22_BAL_SHEET_PUB_FS() != null ? record.getR22_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R22cell3 = row.getCell(2);
                    if (R22cell3 == null) R22cell3 = row.createCell(2);
                    R22cell3.setCellValue(record.getR22_UNDER_REG_SOC() != null ? record.getR22_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R23 =================
                    row = sheet.getRow(22);
                    if (row == null) row = sheet.createRow(22);
                    Cell R23cell2 = row.getCell(1);
                    if (R23cell2 == null) R23cell2 = row.createCell(1);
                    R23cell2.setCellValue(record.getR23_BAL_SHEET_PUB_FS() != null ? record.getR23_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R23cell3 = row.getCell(2);
                    if (R23cell3 == null) R23cell3 = row.createCell(2);
                    R23cell3.setCellValue(record.getR23_UNDER_REG_SOC() != null ? record.getR23_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R24 =================
                    row = sheet.getRow(23);
                    if (row == null) row = sheet.createRow(23);
                    Cell R24cell2 = row.getCell(1);
                    if (R24cell2 == null) R24cell2 = row.createCell(1);
                    R24cell2.setCellValue(record.getR24_BAL_SHEET_PUB_FS() != null ? record.getR24_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R24cell3 = row.getCell(2);
                    if (R24cell3 == null) R24cell3 = row.createCell(2);
                    R24cell3.setCellValue(record.getR24_UNDER_REG_SOC() != null ? record.getR24_UNDER_REG_SOC().doubleValue() : 0);
                   // ================= R26 =================
                    row = sheet.getRow(25);
                    if (row == null) row = sheet.createRow(25);
                    Cell R26cell2 = row.getCell(1);
                    if (R26cell2 == null) R26cell2 = row.createCell(1);
                    R26cell2.setCellValue(record.getR26_BAL_SHEET_PUB_FS() != null ? record.getR26_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R26cell3 = row.getCell(2);
                    if (R26cell3 == null) R26cell3 = row.createCell(2);
                    R26cell3.setCellValue(record.getR26_UNDER_REG_SOC() != null ? record.getR26_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R27 =================
                    row = sheet.getRow(26);
                    if (row == null) row = sheet.createRow(26);
                    Cell R27cell2 = row.getCell(1);
                    if (R27cell2 == null) R27cell2 = row.createCell(1);
                    R27cell2.setCellValue(record.getR27_BAL_SHEET_PUB_FS() != null ? record.getR27_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R27cell3 = row.getCell(2);
                    if (R27cell3 == null) R27cell3 = row.createCell(2);
                    R27cell3.setCellValue(record.getR27_UNDER_REG_SOC() != null ? record.getR27_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R28 =================
                    row = sheet.getRow(27);
                    if (row == null) row = sheet.createRow(27);
                    Cell R28cell2 = row.getCell(1);
                    if (R28cell2 == null) R28cell2 = row.createCell(1);
                    R28cell2.setCellValue(record.getR28_BAL_SHEET_PUB_FS() != null ? record.getR28_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R28cell3 = row.getCell(2);
                    if (R28cell3 == null) R28cell3 = row.createCell(2);
                    R28cell3.setCellValue(record.getR28_UNDER_REG_SOC() != null ? record.getR28_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R29 =================
                    row = sheet.getRow(28);
                    if (row == null) row = sheet.createRow(28);
                    Cell R29cell2 = row.getCell(1);
                    if (R29cell2 == null) R29cell2 = row.createCell(1);
                    R29cell2.setCellValue(record.getR29_BAL_SHEET_PUB_FS() != null ? record.getR29_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R29cell3 = row.getCell(2);
                    if (R29cell3 == null) R29cell3 = row.createCell(2);
                    R29cell3.setCellValue(record.getR29_UNDER_REG_SOC() != null ? record.getR29_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R30 =================
                    row = sheet.getRow(29);
                    if (row == null) row = sheet.createRow(29);
                    Cell R30cell2 = row.getCell(1);
                    if (R30cell2 == null) R30cell2 = row.createCell(1);
                    R30cell2.setCellValue(record.getR30_BAL_SHEET_PUB_FS() != null ? record.getR30_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R30cell3 = row.getCell(2);
                    if (R30cell3 == null) R30cell3 = row.createCell(2);
                    R30cell3.setCellValue(record.getR30_UNDER_REG_SOC() != null ? record.getR30_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R31 =================
                    row = sheet.getRow(30);
                    if (row == null) row = sheet.createRow(30);
                    Cell R31cell2 = row.getCell(1);
                    if (R31cell2 == null) R31cell2 = row.createCell(1);
                    R31cell2.setCellValue(record.getR31_BAL_SHEET_PUB_FS() != null ? record.getR31_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R31cell3 = row.getCell(2);
                    if (R31cell3 == null) R31cell3 = row.createCell(2);
                    R31cell3.setCellValue(record.getR31_UNDER_REG_SOC() != null ? record.getR31_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R32 =================
                    row = sheet.getRow(31);
                    if (row == null) row = sheet.createRow(31);
                    Cell R32cell2 = row.getCell(1);
                    if (R32cell2 == null) R32cell2 = row.createCell(1);
                    R32cell2.setCellValue(record.getR32_BAL_SHEET_PUB_FS() != null ? record.getR32_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R32cell3 = row.getCell(2);
                    if (R32cell3 == null) R32cell3 = row.createCell(2);
                    R32cell3.setCellValue(record.getR32_UNDER_REG_SOC() != null ? record.getR32_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R33 =================
                    row = sheet.getRow(32);
                    if (row == null) row = sheet.createRow(32);
                    Cell R33cell2 = row.getCell(1);
                    if (R33cell2 == null) R33cell2 = row.createCell(1);
                    R33cell2.setCellValue(record.getR33_BAL_SHEET_PUB_FS() != null ? record.getR33_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R33cell3 = row.getCell(2);
                    if (R33cell3 == null) R33cell3 = row.createCell(2);
                    R33cell3.setCellValue(record.getR33_UNDER_REG_SOC() != null ? record.getR33_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R34 =================
                    row = sheet.getRow(33);
                    if (row == null) row = sheet.createRow(33);
                    Cell R34cell2 = row.getCell(1);
                    if (R34cell2 == null) R34cell2 = row.createCell(1);
                    R34cell2.setCellValue(record.getR34_BAL_SHEET_PUB_FS() != null ? record.getR34_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R34cell3 = row.getCell(2);
                    if (R34cell3 == null) R34cell3 = row.createCell(2);
                    R34cell3.setCellValue(record.getR34_UNDER_REG_SOC() != null ? record.getR34_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R35 =================
                    row = sheet.getRow(34);
                    if (row == null) row = sheet.createRow(34);
                    Cell R35cell2 = row.getCell(1);
                    if (R35cell2 == null) R35cell2 = row.createCell(1);
                    R35cell2.setCellValue(record.getR35_BAL_SHEET_PUB_FS() != null ? record.getR35_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R35cell3 = row.getCell(2);
                    if (R35cell3 == null) R35cell3 = row.createCell(2);
                    R35cell3.setCellValue(record.getR35_UNDER_REG_SOC() != null ? record.getR35_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R36 =================
                    row = sheet.getRow(35);
                    if (row == null) row = sheet.createRow(35);
                    Cell R36cell2 = row.getCell(1);
                    if (R36cell2 == null) R36cell2 = row.createCell(1);
                    R36cell2.setCellValue(record.getR36_BAL_SHEET_PUB_FS() != null ? record.getR36_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R36cell3 = row.getCell(2);
                    if (R36cell3 == null) R36cell3 = row.createCell(2);
                    R36cell3.setCellValue(record.getR36_UNDER_REG_SOC() != null ? record.getR36_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R37 =================
                    row = sheet.getRow(36);
                    if (row == null) row = sheet.createRow(36);
                    Cell R37cell2 = row.getCell(1);
                    if (R37cell2 == null) R37cell2 = row.createCell(1);
                    R37cell2.setCellValue(record.getR37_BAL_SHEET_PUB_FS() != null ? record.getR37_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R37cell3 = row.getCell(2);
                    if (R37cell3 == null) R37cell3 = row.createCell(2);
                    R37cell3.setCellValue(record.getR37_UNDER_REG_SOC() != null ? record.getR37_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R38 =================
                    row = sheet.getRow(37);
                    if (row == null) row = sheet.createRow(37);
                    Cell R38cell2 = row.getCell(1);
                    if (R38cell2 == null) R38cell2 = row.createCell(1);
                    R38cell2.setCellValue(record.getR38_BAL_SHEET_PUB_FS() != null ? record.getR38_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R38cell3 = row.getCell(2);
                    if (R38cell3 == null) R38cell3 = row.createCell(2);
                    R38cell3.setCellValue(record.getR38_UNDER_REG_SOC() != null ? record.getR38_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R39 =================
                    row = sheet.getRow(38);
                    if (row == null) row = sheet.createRow(38);
                    Cell R39cell2 = row.getCell(1);
                    if (R39cell2 == null) R39cell2 = row.createCell(1);
                    R39cell2.setCellValue(record.getR39_BAL_SHEET_PUB_FS() != null ? record.getR39_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R39cell3 = row.getCell(2);
                    if (R39cell3 == null) R39cell3 = row.createCell(2);
                    R39cell3.setCellValue(record.getR39_UNDER_REG_SOC() != null ? record.getR39_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R40 =================
                    row = sheet.getRow(39);
                    if (row == null) row = sheet.createRow(39);
                    Cell R40cell2 = row.getCell(1);
                    if (R40cell2 == null) R40cell2 = row.createCell(1);
                    R40cell2.setCellValue(record.getR40_BAL_SHEET_PUB_FS() != null ? record.getR40_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R40cell3 = row.getCell(2);
                    if (R40cell3 == null) R40cell3 = row.createCell(2);
                    R40cell3.setCellValue(record.getR40_UNDER_REG_SOC() != null ? record.getR40_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R41 =================
                    row = sheet.getRow(40);
                    if (row == null) row = sheet.createRow(40);
                    Cell R41cell2 = row.getCell(1);
                    if (R41cell2 == null) R41cell2 = row.createCell(1);
                    R41cell2.setCellValue(record.getR41_BAL_SHEET_PUB_FS() != null ? record.getR41_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R41cell3 = row.getCell(2);
                    if (R41cell3 == null) R41cell3 = row.createCell(2);
                    R41cell3.setCellValue(record.getR41_UNDER_REG_SOC() != null ? record.getR41_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R42 =================
                    row = sheet.getRow(41);
                    if (row == null) row = sheet.createRow(41);
                    Cell R42cell2 = row.getCell(1);
                    if (R42cell2 == null) R42cell2 = row.createCell(1);
                    R42cell2.setCellValue(record.getR42_BAL_SHEET_PUB_FS() != null ? record.getR42_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R42cell3 = row.getCell(2);
                    if (R42cell3 == null) R42cell3 = row.createCell(2);
                    R42cell3.setCellValue(record.getR42_UNDER_REG_SOC() != null ? record.getR42_UNDER_REG_SOC().doubleValue() : 0);

                   // ================= R44 =================
                    row = sheet.getRow(43);
                    if (row == null) row = sheet.createRow(43);
                    Cell R44cell2 = row.getCell(1);
                    if (R44cell2 == null) R44cell2 = row.createCell(1);
                    R44cell2.setCellValue(record.getR44_BAL_SHEET_PUB_FS() != null ? record.getR44_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R44cell3 = row.getCell(2);
                    if (R44cell3 == null) R44cell3 = row.createCell(2);
                    R44cell3.setCellValue(record.getR44_UNDER_REG_SOC() != null ? record.getR44_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R45 =================
                    row = sheet.getRow(44);
                    if (row == null) row = sheet.createRow(44);
                    Cell R45cell2 = row.getCell(1);
                    if (R45cell2 == null) R45cell2 = row.createCell(1);
                    R45cell2.setCellValue(record.getR45_BAL_SHEET_PUB_FS() != null ? record.getR45_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R45cell3 = row.getCell(2);
                    if (R45cell3 == null) R45cell3 = row.createCell(2);
                    R45cell3.setCellValue(record.getR45_UNDER_REG_SOC() != null ? record.getR45_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R46 =================
                    row = sheet.getRow(45);
                    if (row == null) row = sheet.createRow(45);
                    Cell R46cell2 = row.getCell(1);
                    if (R46cell2 == null) R46cell2 = row.createCell(1);
                    R46cell2.setCellValue(record.getR46_BAL_SHEET_PUB_FS() != null ? record.getR46_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R46cell3 = row.getCell(2);
                    if (R46cell3 == null) R46cell3 = row.createCell(2);
                    R46cell3.setCellValue(record.getR46_UNDER_REG_SOC() != null ? record.getR46_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R47 =================
                    row = sheet.getRow(46);
                    if (row == null) row = sheet.createRow(46);
                    Cell R47cell2 = row.getCell(1);
                    if (R47cell2 == null) R47cell2 = row.createCell(1);
                    R47cell2.setCellValue(record.getR47_BAL_SHEET_PUB_FS() != null ? record.getR47_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R47cell3 = row.getCell(2);
                    if (R47cell3 == null) R47cell3 = row.createCell(2);
                    R47cell3.setCellValue(record.getR47_UNDER_REG_SOC() != null ? record.getR47_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R48 =================
                    row = sheet.getRow(47);
                    if (row == null) row = sheet.createRow(47);
                    Cell R48cell2 = row.getCell(1);
                    if (R48cell2 == null) R48cell2 = row.createCell(1);
                    R48cell2.setCellValue(record.getR48_BAL_SHEET_PUB_FS() != null ? record.getR48_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R48cell3 = row.getCell(2);
                    if (R48cell3 == null) R48cell3 = row.createCell(2);
                    R48cell3.setCellValue(record.getR48_UNDER_REG_SOC() != null ? record.getR48_UNDER_REG_SOC().doubleValue() : 0);

                    // ================= R49 =================
                    row = sheet.getRow(48);
                    if (row == null) row = sheet.createRow(48);
                    Cell R49cell2 = row.getCell(1);
                    if (R49cell2 == null) R49cell2 = row.createCell(1);
                    R49cell2.setCellValue(record.getR49_BAL_SHEET_PUB_FS() != null ? record.getR49_BAL_SHEET_PUB_FS().doubleValue() : 0);
                    Cell R49cell3 = row.getCell(2);
                    if (R49cell3 == null) R49cell3 = row.createCell(2);
                    R49cell3.setCellValue(record.getR49_UNDER_REG_SOC() != null ? record.getR49_UNDER_REG_SOC().doubleValue() : 0);

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

    public byte[] getDetailExcelARCHIVAL(String filename,
            String fromdate,
            String todate,
            String currency,
            String dtltype,
            String type,
            String version) {
        try {
            logger.info("Generating Excel for BRRS_Expanded_Regu_BS ARCHIVAL Details...");
            System.out.println("came to Detail download service");

            // --- Create workbook and sheet ---
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Expanded_Regu_BSDetail");

            BorderStyle border = BorderStyle.THIN;

            // Header style
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

            // Data style
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.LEFT);
            dataStyle.setBorderTop(border);
            dataStyle.setBorderBottom(border);
            dataStyle.setBorderLeft(border);
            dataStyle.setBorderRight(border);

            // Balance style
            CellStyle balanceStyle = workbook.createCellStyle();
            balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
            balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
            balanceStyle.setBorderTop(border);
            balanceStyle.setBorderBottom(border);
            balanceStyle.setBorderLeft(border);
            balanceStyle.setBorderRight(border);

            // --- Header row ---
            String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
                    "REPORT_DATE" };
            XSSFRow headerRow = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                if (i == 3) {
                    cell.setCellStyle(rightAlignedHeaderStyle);
                } else {
                    cell.setCellStyle(headerStyle);
                }
                sheet.setColumnWidth(i, 5000);
            }

            // --- Fetch data from DB ---
            // Date parsedToDate = new SimpleDateFormat("dd-MM-yyyy").parse(todate); // ✅
            // match with controller
            List<Expanded_Regu_BS_Archival_Detail_Entity> reportData = Expanded_Regu_BS_Archival_Detail_Repo
                    .getdatabydateList(todate, version);

            logger.info("Fetched {} rows from DB for ARCHIVAL", reportData != null ? reportData.size() : 0);

            if (reportData != null && !reportData.isEmpty()) {
                int rowIndex = 1;
                for (Expanded_Regu_BS_Archival_Detail_Entity item : reportData) {
                    XSSFRow row = sheet.createRow(rowIndex++);

                    row.createCell(0).setCellValue(item.getCustId());
                    row.createCell(1).setCellValue(item.getAcctNumber());
                    row.createCell(2).setCellValue(item.getAcctName());

                    // Balance
                    Cell balanceCell = row.createCell(3);
                    balanceCell.setCellValue(item.getAcctBalanceInPula() != null
                            ? item.getAcctBalanceInPula().doubleValue()
                            : 0);
                    balanceCell.setCellStyle(balanceStyle);

                    row.createCell(4).setCellValue(item.getReportLabel());
                    row.createCell(5).setCellValue(item.getReportAddlCriteria1());
                    row.createCell(6).setCellValue(item.getReportDate() != null
                            ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
                            : "");

                    // Apply data style except balance column
                    for (int j = 0; j < 7; j++) {
                        if (j != 3) {
                            row.getCell(j).setCellStyle(dataStyle);
                        }
                    }
                }
            } else {
                logger.info("⚠️ No data found for BRRS_Expanded_Regu_BS ARCHIVAL — only header will be written.");
            }

            // --- Write to byte[] ---
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating BRRS_Expanded_Regu_BS Excel", e);
            return new byte[0];
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
        ModelAndView mv = new ModelAndView("BRRS/EXPANDED_REGU_BS");

        if (acctNo != null) {
            Expanded_Regu_BS_Detail_Entity ReconFs = Expanded_Regu_BS_Detail_Repo.findByAcctnumber(acctNo);
            if (ReconFs != null && ReconFs.getReportDate() != null) {
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(ReconFs.getReportDate());
                mv.addObject("asondate", formattedDate);
            }
            mv.addObject("Expanded_Regu_BSData", ReconFs);
        }

        mv.addObject("displaymode", "edit");
        mv.addObject("formmode", formMode != null ? formMode : "edit");
        return mv;
    }

    @Transactional
    public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
        try {
            String acctNo = request.getParameter("acctNumber");
            String acctBalanceInpula = request.getParameter("acctBalanceInPula");
            String acctName = request.getParameter("acctName");
            String reportDateStr = request.getParameter("reportDate");

            logger.info("Received update for ACCT_NO: {}", acctNo);

            Expanded_Regu_BS_Detail_Entity existing = Expanded_Regu_BS_Detail_Repo.findByAcctnumber(acctNo);
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
                if (existing.getAcctBalanceInPula() == null ||
                        existing.getAcctBalanceInPula().compareTo(newacctBalanceInpula) != 0) {
                    existing.setAcctBalanceInPula(newacctBalanceInpula);
                    isChanged = true;
                    logger.info("Balance updated to {}", newacctBalanceInpula);
                }
            }

            if (isChanged) {
                Expanded_Regu_BS_Detail_Repo.save(existing);
                logger.info("Record updated successfully for account {}", acctNo);

                // Format date for procedure
                String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
                        .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

                // Run summary procedure after commit
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        try {
                            logger.info("Transaction committed — calling BRRS_Expanded_Regu_BS_SUMMARY_PROCEDURE({})",
                                    formattedDate);
                            jdbcTemplate.update("BEGIN BRRS_Expanded_Regu_BS_SUMMARY_PROCEDURE(?); END;",
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
            logger.error("Error updating ReconOfFS record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating record: " + e.getMessage());
        }
    }

}
