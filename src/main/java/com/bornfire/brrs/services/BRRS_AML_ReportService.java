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
import com.bornfire.brrs.entities.AML_Archival_Detail_Entity;
import com.bornfire.brrs.entities.AML_Archival_Summary_Entity;
import com.bornfire.brrs.entities.AML_Detail_Entity;
import com.bornfire.brrs.entities.AML_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_AML_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_AML_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_AML_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_AML_Summary_Repo;

@Component
@Service

public class BRRS_AML_ReportService {
    private static final Logger logger = LoggerFactory.getLogger(BRRS_AML_ReportService.class);

    @Autowired
    private Environment env;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    BRRS_AML_Summary_Repo aml_summary_repo;

    @Autowired
    BRRS_AML_Archival_Summary_Repo aml_Archival_Summary_Repo;

    @Autowired
    BRRS_AML_Detail_Repo aml_detail_repo;

    @Autowired
    BRRS_AML_Archival_Detail_Repo aml_Archival_Detail_Repo;

    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

    public ModelAndView getAMLView(String reportId, String fromdate, String todate, String currency, String dtltype,
            Pageable pageable, String type, String version) {

        ModelAndView mv = new ModelAndView();

        System.out.println("testing");
        System.out.println(version);

        if ("ARCHIVAL".equals(type) && version != null && !version.isEmpty()) {

            System.out.println("ARCHIVAL MODE");
            System.out.println("version = " + version);

            List<AML_Archival_Summary_Entity> T1Master = new ArrayList<>();

            try {
                Date dt = dateformat.parse(todate);

                T1Master = aml_Archival_Summary_Repo.getdatabydateListarchival(dt, version);

                System.out.println("T1Master size = " + T1Master.size());

            } catch (ParseException e) {
                e.printStackTrace();
            }

            mv.addObject("reportsummary", T1Master);

        } else {

            List<AML_Summary_Entity> T1Master = new ArrayList<AML_Summary_Entity>();

            try {
                Date d1 = dateformat.parse(todate);

                T1Master = aml_summary_repo.getdatabydateList(dateformat.parse(todate));

                System.out.println("T1Master size " + T1Master.size());
                mv.addObject("report_date", dateformat.format(d1));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            mv.addObject("reportsummary", T1Master);

        }

        mv.setViewName("BRRS/AML");

        mv.addObject("displaymode", "summary");

        System.out.println("scv" + mv.getViewName());

        return mv;

    }

    public ModelAndView getAMLcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
                List<AML_Archival_Detail_Entity> T1Dt1;
                if (reportLable != null && reportAddlCriteria_1 != null) {
                    T1Dt1 = aml_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1,
                            parsedDate, version);
                } else {
                    T1Dt1 = aml_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
                }

                mv.addObject("reportdetails", T1Dt1);
                mv.addObject("reportmaster12", T1Dt1);
                System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

            } else {
                // ?? Current branch
                List<AML_Detail_Entity> T1Dt1;

                if (reportLable != null && reportAddlCriteria_1 != null) {
                    T1Dt1 = aml_detail_repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate);
                } else {
                    T1Dt1 = aml_detail_repo.getdatabydateList(parsedDate);
                    totalPages = aml_detail_repo.getdatacount(parsedDate);
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

        mv.setViewName("BRRS/AML");
        mv.addObject("displaymode", "Details");
        mv.addObject("currentPage", currentPage);
        System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
        mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
        mv.addObject("reportsflag", "reportsflag");
        mv.addObject("menu", reportId);
        return mv;
    }

    public byte[] getAMLExcel(String filename, String reportId, String fromdate, String todate, String currency,
            String dtltype, String type, String version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");

        // ARCHIVAL check
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
            logger.info("Service: Generating ARCHIVAL report for version {}", version);
            return getExcelAMLARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
        }

        // Fetch data

        List<AML_Summary_Entity> dataList = aml_summary_repo.getdatabydateList(dateformat.parse(todate));

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for  AML report. Returning empty result.");
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
                    AML_Summary_Entity record = dataList.get(i);

                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }

                    // A TABLE

                    Cell cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR11_cust_base_no_of_acct() != null) {
                        cellC.setCellValue(record.getR11_cust_base_no_of_acct().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    Cell cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(2);
                    if (record.getR11_cust_base_tot_dep() != null) {
                        cellD.setCellValue(record.getR11_cust_base_tot_dep().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 12
                    row = sheet.getRow(11);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR12_cust_base_no_of_acct() != null) {
                        cellC.setCellValue(record.getR12_cust_base_no_of_acct().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR12_cust_base_tot_dep() != null) {
                        cellD.setCellValue(record.getR12_cust_base_tot_dep().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 13
                    row = sheet.getRow(12);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR13_cust_base_no_of_acct() != null) {
                        cellC.setCellValue(record.getR13_cust_base_no_of_acct().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR13_cust_base_tot_dep() != null) {
                        cellD.setCellValue(record.getR13_cust_base_tot_dep().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 14
                    row = sheet.getRow(13);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR14_cust_base_no_of_acct() != null) {
                        cellC.setCellValue(record.getR14_cust_base_no_of_acct().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR14_cust_base_tot_dep() != null) {
                        cellD.setCellValue(record.getR14_cust_base_tot_dep().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // B1 TABLE

                    // ROW 21
                    row = sheet.getRow(20);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR21_cust_risk_pro_num_of_cust() != null) {
                        cellC.setCellValue(record.getR21_cust_risk_pro_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR21_cust_risk_pro_value() != null) {
                        cellD.setCellValue(record.getR21_cust_risk_pro_value().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 22
                    row = sheet.getRow(21);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR22_cust_risk_pro_num_of_cust() != null) {
                        cellC.setCellValue(record.getR22_cust_risk_pro_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR22_cust_risk_pro_value() != null) {
                        cellD.setCellValue(record.getR22_cust_risk_pro_value().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 23
                    row = sheet.getRow(22);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR23_cust_risk_pro_num_of_cust() != null) {
                        cellC.setCellValue(record.getR23_cust_risk_pro_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR23_cust_risk_pro_value() != null) {
                        cellD.setCellValue(record.getR23_cust_risk_pro_value().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // B2 TABLE

                    // ROW 30
                    row = sheet.getRow(29);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR30_b2_low_risk_no_cust() != null) {
                        cellC.setCellValue(record.getR30_b2_low_risk_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR30_b2_low_risk_deposit() != null) {
                        cellD.setCellValue(record.getR30_b2_low_risk_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    Cell cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR30_b2_medi_risk_no_cust() != null) {
                        cellE.setCellValue(record.getR30_b2_medi_risk_no_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    Cell cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR30_b2_medi_risk_deposit() != null) {
                        cellF.setCellValue(record.getR30_b2_medi_risk_deposit().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    Cell cellG = row.getCell(6);
                    if (cellG == null)
                        cellG = row.createCell(6);
                    if (record.getR30_b2_high_risk_no_cust() != null) {
                        cellG.setCellValue(record.getR30_b2_high_risk_no_cust().doubleValue());
                    } else {
                        cellG.setCellValue(0);
                    }

                    Cell cellH = row.getCell(7);
                    if (cellH == null)
                        cellH = row.createCell(7);
                    if (record.getR30_b2_high_risk_deposit() != null) {
                        cellH.setCellValue(record.getR30_b2_high_risk_deposit().doubleValue());
                    } else {
                        cellH.setCellValue(0);
                    }

                    // ROW 31
                    row = sheet.getRow(30);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR31_b2_low_risk_no_cust() != null) {
                        cellC.setCellValue(record.getR31_b2_low_risk_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR31_b2_low_risk_deposit() != null) {
                        cellD.setCellValue(record.getR31_b2_low_risk_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR31_b2_medi_risk_no_cust() != null) {
                        cellE.setCellValue(record.getR31_b2_medi_risk_no_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR31_b2_medi_risk_deposit() != null) {
                        cellF.setCellValue(record.getR31_b2_medi_risk_deposit().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    cellG = row.getCell(6);
                    if (cellG == null)
                        cellG = row.createCell(6);
                    if (record.getR31_b2_high_risk_no_cust() != null) {
                        cellG.setCellValue(record.getR31_b2_high_risk_no_cust().doubleValue());
                    } else {
                        cellG.setCellValue(0);
                    }

                    cellH = row.getCell(7);
                    if (cellH == null)
                        cellH = row.createCell(7);
                    if (record.getR31_b2_high_risk_deposit() != null) {
                        cellH.setCellValue(record.getR31_b2_high_risk_deposit().doubleValue());
                    } else {
                        cellH.setCellValue(0);
                    }

                    // ROW 32
                    row = sheet.getRow(31);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR32_b2_low_risk_no_cust() != null) {
                        cellC.setCellValue(record.getR32_b2_low_risk_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR32_b2_low_risk_deposit() != null) {
                        cellD.setCellValue(record.getR32_b2_low_risk_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // C TABLE 39 AND 40

                    // ROW 39
                    row = sheet.getRow(38);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR39_cust_base_no_cust() != null) {
                        cellC.setCellValue(record.getR39_cust_base_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR39_cust_base_deposits() != null) {
                        cellD.setCellValue(record.getR39_cust_base_deposits().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // ROW 40
                    row = sheet.getRow(39);

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR40_cust_base_no_cust() != null) {
                        cellC.setCellValue(record.getR40_cust_base_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR40_cust_base_deposits() != null) {
                        cellD.setCellValue(record.getR40_cust_base_deposits().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // D TABLE 39 AND 40 D AND E COLUMN

                    // ROW 51
                    row = sheet.getRow(50);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR51_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR51_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR51_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR51_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 52
                    row = sheet.getRow(51);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR52_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR52_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR52_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR52_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 53
                    row = sheet.getRow(52);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR53_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR53_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR53_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR53_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 54
                    row = sheet.getRow(53);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR54_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR54_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR54_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR54_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 57
                    row = sheet.getRow(56);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR57_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR57_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR57_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR57_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 58
                    row = sheet.getRow(57);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR58_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR58_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR58_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR58_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 59
                    row = sheet.getRow(58);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR59_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR59_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR59_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR59_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 60
                    row = sheet.getRow(59);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR60_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR60_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR60_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR60_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 61
                    row = sheet.getRow(60);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR61_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR61_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR61_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR61_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 62
                    row = sheet.getRow(61);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR62_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR62_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR62_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR62_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 63
                    row = sheet.getRow(62);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR63_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR63_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR63_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR63_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 64
                    row = sheet.getRow(63);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR64_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR64_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR64_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR64_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 65
                    row = sheet.getRow(64);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR65_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR65_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR65_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR65_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 66
                    row = sheet.getRow(65);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR66_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR66_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR66_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR66_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 67
                    row = sheet.getRow(66);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR67_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR67_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR67_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR67_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 68
                    row = sheet.getRow(67);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR68_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR68_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR68_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR68_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 69
                    row = sheet.getRow(68);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR69_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR69_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR69_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR69_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 72
                    row = sheet.getRow(71);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR72_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR72_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR72_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR72_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 73
                    row = sheet.getRow(72);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR73_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR73_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR73_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR73_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 74
                    row = sheet.getRow(73);

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR74_brkdown_num_of_cust() != null) {
                        cellD.setCellValue(record.getR74_brkdown_num_of_cust().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR74_brkdown_tot_depo() != null) {
                        cellE.setCellValue(record.getR74_brkdown_tot_depo().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // E1 TABLE

                    // ROW 82
                    row = sheet.getRow(81);

                    Cell cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR82_e1_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR82_e1_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(1);
                    if (record.getR82_e1_loan_on_bal_expo() != null) {
                        cellC.setCellValue(record.getR82_e1_loan_on_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR82_e1_deposit() != null) {
                        cellD.setCellValue(record.getR82_e1_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR82_e1_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR82_e1_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR82_e1_turnover() != null) {
                        cellF.setCellValue(record.getR82_e1_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 83
                    row = sheet.getRow(82);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR83_e1_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR83_e1_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR83_e1_loan_on_bal_expo() != null) {
                        cellC.setCellValue(record.getR83_e1_loan_on_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR83_e1_deposit() != null) {
                        cellD.setCellValue(record.getR83_e1_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR83_e1_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR83_e1_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR83_e1_turnover() != null) {
                        cellF.setCellValue(record.getR83_e1_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // E2 TABLE

                    // ROW 89
                    row = sheet.getRow(88);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR89_e2_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR89_e2_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR89_e2_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR89_e2_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR89_e2_deposit() != null) {
                        cellD.setCellValue(record.getR89_e2_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR89_e2_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR89_e2_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR89_e2_turnover() != null) {
                        cellF.setCellValue(record.getR89_e2_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 90
                    row = sheet.getRow(89);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR90_e2_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR90_e2_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR90_e2_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR90_e2_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR90_e2_deposit() != null) {
                        cellD.setCellValue(record.getR90_e2_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR90_e2_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR90_e2_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR90_e2_turnover() != null) {
                        cellF.setCellValue(record.getR90_e2_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // E3 TABLE

                    // ROW 96
                    row = sheet.getRow(95);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR96_e3_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR96_e3_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR96_e3_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR96_e3_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR96_e3_deposit() != null) {
                        cellD.setCellValue(record.getR96_e3_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR96_e3_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR96_e3_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR96_e3_turnover() != null) {
                        cellF.setCellValue(record.getR96_e3_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 97
                    row = sheet.getRow(96);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR97_e3_tot_no_cust() != null) {
                        cellB.setCellValue(record.getR97_e3_tot_no_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR97_e3_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR97_e3_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR97_e3_deposit() != null) {
                        cellD.setCellValue(record.getR97_e3_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR97_e3_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR97_e3_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR97_e3_turnover() != null) {
                        cellF.setCellValue(record.getR97_e3_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // F TABLE

                    // ROW 104
                    row = sheet.getRow(103);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR104_f_num_of_cust() != null) {
                        cellB.setCellValue(record.getR104_f_num_of_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR104_f_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR104_f_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR104_f_deposit() != null) {
                        cellD.setCellValue(record.getR104_f_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR104_f_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR104_f_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR104_f_turnover() != null) {
                        cellF.setCellValue(record.getR104_f_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 105
                    row = sheet.getRow(104);

                    cellB = row.getCell(1);
                    if (cellB == null)
                        cellB = row.createCell(1);
                    if (record.getR105_f_num_of_cust() != null) {
                        cellB.setCellValue(record.getR105_f_num_of_cust().doubleValue());
                    } else {
                        cellB.setCellValue(0);
                    }

                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR105_f_loans_bal_expo() != null) {
                        cellC.setCellValue(record.getR105_f_loans_bal_expo().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR105_f_deposit() != null) {
                        cellD.setCellValue(record.getR105_f_deposit().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR105_f_funds_behalf_cust() != null) {
                        cellE.setCellValue(record.getR105_f_funds_behalf_cust().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR105_f_turnover() != null) {
                        cellF.setCellValue(record.getR105_f_turnover().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // G1 TABLE

                    // ROW 111
                    row = sheet.getRow(110);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR111_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR111_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR111_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR111_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 112
                    row = sheet.getRow(111);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR112_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR112_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR112_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR112_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 114
                    row = sheet.getRow(113);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR114_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR114_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR114_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR114_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 115
                    row = sheet.getRow(114);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR115_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR115_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR115_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR115_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 117
                    row = sheet.getRow(116);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR117_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR117_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR117_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR117_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 118
                    row = sheet.getRow(117);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR118_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR118_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR118_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR118_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 120
                    row = sheet.getRow(119);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR120_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR120_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR120_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR120_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 121
                    row = sheet.getRow(120);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR121_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR121_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR121_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR121_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 123
                    row = sheet.getRow(122);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR123_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR123_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR123_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR123_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 124
                    row = sheet.getRow(123);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR124_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR124_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR124_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR124_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 126
                    row = sheet.getRow(125);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR126_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR126_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR126_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR126_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 127
                    row = sheet.getRow(126);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR127_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR127_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR127_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR127_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 128
                    row = sheet.getRow(127);

                    // Column D – Number of Transactions
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR128_g1_num_trans() != null) {
                        cellD.setCellValue(record.getR128_g1_num_trans().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR128_g1_val_trans() != null) {
                        cellE.setCellValue(record.getR128_g1_val_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // G2 TABLE

                    // ROW 135
                    row = sheet.getRow(134);

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR135_g2_val_transac() != null) {
                        cellE.setCellValue(record.getR135_g2_val_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 136
                    row = sheet.getRow(135);

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR136_g2_val_transac() != null) {
                        cellE.setCellValue(record.getR136_g2_val_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 138
                    row = sheet.getRow(137);

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR138_g2_val_transac() != null) {
                        cellE.setCellValue(record.getR138_g2_val_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 139
                    row = sheet.getRow(138);

                    // Column E – Value of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR139_g2_val_transac() != null) {
                        cellE.setCellValue(record.getR139_g2_val_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // H TABLE

                    // ROW 144
                    row = sheet.getRow(143);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR144_h_amount() != null) {
                        cellF.setCellValue(record.getR144_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 145
                    row = sheet.getRow(144);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR145_h_amount() != null) {
                        cellF.setCellValue(record.getR145_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 146
                    row = sheet.getRow(145);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR146_h_amount() != null) {
                        cellF.setCellValue(record.getR146_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 147
                    row = sheet.getRow(146);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR147_h_amount() != null) {
                        cellF.setCellValue(record.getR147_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 148
                    row = sheet.getRow(147);

                    // Column F – Amount
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR148_h_amount() != null) {
                        cellF.setCellValue(record.getR148_h_amount().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // I TABLE

                    // ROW 153
                    row = sheet.getRow(152);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR153_i_no_cust() != null) {
                        cellC.setCellValue(record.getR153_i_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Outstanding Balance
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR153_i_outs_bal() != null) {
                        cellD.setCellValue(record.getR153_i_outs_bal().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Turnover
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR153_i_turnover() != null) {
                        cellE.setCellValue(record.getR153_i_turnover().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 154
                    row = sheet.getRow(153);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR154_i_no_cust() != null) {
                        cellC.setCellValue(record.getR154_i_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Outstanding Balance
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR154_i_outs_bal() != null) {
                        cellD.setCellValue(record.getR154_i_outs_bal().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Turnover
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR154_i_turnover() != null) {
                        cellE.setCellValue(record.getR154_i_turnover().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // ROW 155
                    row = sheet.getRow(154);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR155_i_no_cust() != null) {
                        cellC.setCellValue(record.getR155_i_no_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // Column D – Outstanding Balance
                    cellD = row.getCell(3);
                    if (cellD == null)
                        cellD = row.createCell(3);
                    if (record.getR155_i_outs_bal() != null) {
                        cellD.setCellValue(record.getR155_i_outs_bal().doubleValue());
                    } else {
                        cellD.setCellValue(0);
                    }

                    // Column E – Turnover
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR155_i_turnover() != null) {
                        cellE.setCellValue(record.getR155_i_turnover().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // J TABLE

                    // ROW 161
                    row = sheet.getRow(160);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR161_j_num_of_cust() != null) {
                        cellC.setCellValue(record.getR161_j_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ROW 162
                    row = sheet.getRow(161);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR162_j_num_of_cust() != null) {
                        cellC.setCellValue(record.getR162_j_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // ROW 163
                    row = sheet.getRow(162);

                    // Column C – Number of Customers
                    cellC = row.getCell(2);
                    if (cellC == null)
                        cellC = row.createCell(2);
                    if (record.getR163_j_num_of_cust() != null) {
                        cellC.setCellValue(record.getR163_j_num_of_cust().doubleValue());
                    } else {
                        cellC.setCellValue(0);
                    }

                    // K TABLE

                    // ROW 170
                    row = sheet.getRow(169);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR170_k_num_of_trans() != null) {
                        cellE.setCellValue(record.getR170_k_num_of_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR170_k_value_of_trans() != null) {
                        cellF.setCellValue(record.getR170_k_value_of_trans().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 171
                    row = sheet.getRow(170);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR171_k_num_of_trans() != null) {
                        cellE.setCellValue(record.getR171_k_num_of_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR171_k_value_of_trans() != null) {
                        cellF.setCellValue(record.getR171_k_value_of_trans().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 172
                    row = sheet.getRow(171);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR172_k_num_of_trans() != null) {
                        cellE.setCellValue(record.getR172_k_num_of_trans().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR172_k_value_of_trans() != null) {
                        cellF.setCellValue(record.getR172_k_value_of_trans().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // L TABLE

                    // ROW 179
                    row = sheet.getRow(178);

                    // Column F – Number of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR179_l_num_of_transac() != null) {
                        cellF.setCellValue(record.getR179_l_num_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 180
                    row = sheet.getRow(179);

                    // Column F – Number of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR180_l_num_of_transac() != null) {
                        cellF.setCellValue(record.getR180_l_num_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 181
                    row = sheet.getRow(180);

                    // Column F – Number of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR181_l_num_of_transac() != null) {
                        cellF.setCellValue(record.getR181_l_num_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // M TABLE

                    // ROW 187
                    row = sheet.getRow(186);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR187_m_num_of_transac() != null) {
                        cellE.setCellValue(record.getR187_m_num_of_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR187_m_val_of_transac() != null) {
                        cellF.setCellValue(record.getR187_m_val_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // N TABLE

                    // ROW 192
                    row = sheet.getRow(191);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR192_n_num_of_transac() != null) {
                        cellE.setCellValue(record.getR192_n_num_of_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR192_n_val_of_transac() != null) {
                        cellF.setCellValue(record.getR192_n_val_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // O TABLE

                    // ROW 196
                    row = sheet.getRow(195);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR196_o_num_of_transac() != null) {
                        cellE.setCellValue(record.getR196_o_num_of_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR196_o_val_of_transac() != null) {
                        cellF.setCellValue(record.getR196_o_val_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
                    }

                    // ROW 201
                    row = sheet.getRow(200);

                    // Column E – Number of Transactions
                    cellE = row.getCell(4);
                    if (cellE == null)
                        cellE = row.createCell(4);
                    if (record.getR201_p_num_of_transac() != null) {
                        cellE.setCellValue(record.getR201_p_num_of_transac().doubleValue());
                    } else {
                        cellE.setCellValue(0);
                    }

                    // Column F – Value of Transactions
                    cellF = row.getCell(5);
                    if (cellF == null)
                        cellF = row.createCell(5);
                    if (record.getR201_p_val_of_transac() != null) {
                        cellF.setCellValue(record.getR201_p_val_of_transac().doubleValue());
                    } else {
                        cellF.setCellValue(0);
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

    public byte[] getExcelAMLARCHIVAL(String filename, String reportId, String fromdate, String todate,
            String currency, String dtltype, String type, String version) throws Exception {

        logger.info("Service: Starting Excel generation process in memory.");

        if (type.equals("ARCHIVAL") & version != null) {

        }

        List<AML_Archival_Summary_Entity> dataList = aml_Archival_Summary_Repo
                .getdatabydateListarchival(dateformat.parse(todate), version);

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for AML report. Returning empty result.");
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

            int startRow = 1;

            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {
                    AML_Archival_Summary_Entity record = dataList.get(i);

                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
                    }

                    // row2
                    // Column C

                    // Cell cellC = row.getCell(2);
                    // if (cellC == null)
                    // cellC = row.createCell(2);
                    // if (record.getR2_cap_ratio_buff_amt() != null) {
                    // cellC.setCellValue(record.getR2_cap_ratio_buff_amt().doubleValue());
                    // } else {
                    // cellC.setCellValue(0);
                    // }

                    // // ======================= R30 =======================
                    // row = sheet.getRow(29);
                    // cellC = row.getCell(2);
                    // if (cellC == null)
                    // cellC = row.createCell(2);
                    // if (record.getR30_cap_ratio_buff_amt() != null) {
                    // cellC.setCellValue(record.getR30_cap_ratio_buff_amt().doubleValue());
                    // } else {
                    // cellC.setCellValue(0);
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

    public List<Object> getAMLArchival() {
        List<Object> AMLArchivallist = new ArrayList<>();
        try {
            AMLArchivallist = aml_Archival_Summary_Repo.getAMLarchival();

            System.out.println("countser" + AMLArchivallist.size());

        } catch (Exception e) {
            // Log the exception
            System.err.println("Error fetching AMLArchivallist Archival data: " + e.getMessage());
            e.printStackTrace();

            // Optionally, you can rethrow it or return empty list
            // throw new RuntimeException("Failed to fetch data", e);
        }
        return AMLArchivallist;
    }

    public byte[] getAMLDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
            String type, String version) {
        try {
            logger.info("Generating Excel for AML Details...");
            System.out.println("came to Detail download service");

            if (type.equals("ARCHIVAL") & version != null) {
                byte[] ARCHIVALreport = getAMLDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
                        version);
                return ARCHIVALreport;
            }

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("AMLDetails");

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
            balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
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
            List<AML_Detail_Entity> reportData = aml_detail_repo.getdatabydateList(parsedToDate);

            if (reportData != null && !reportData.isEmpty()) {
                int rowIndex = 1;
                for (AML_Detail_Entity item : reportData) {
                    XSSFRow row = sheet.createRow(rowIndex++);

                    row.createCell(0).setCellValue(item.getCustId());
                    row.createCell(1).setCellValue(item.getAcctNumber());
                    row.createCell(2).setCellValue(item.getAcctName());
                    // ACCT BALANCE (right aligned, 3 decimal places)
                    Cell balanceCell = row.createCell(3);
                    if (item.getAcctBalanceInpula() != null) {
                        balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
                    } else {
                        balanceCell.setCellValue(0.000);
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
                logger.info("No data found for AML — only header will be written.");
            }

            // Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating AML Excel", e);
            return new byte[0];
        }
    }

    public byte[] getAMLDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
            String dtltype, String type, String version) {
        try {
            logger.info("Generating Excel for AML ARCHIVAL Details...");
            System.out.println("came to ARCHIVAL Detail download service");
            if (type.equals("ARCHIVAL") & version != null) {

            }
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("AMLDetail");

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
            balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
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
            List<AML_Archival_Detail_Entity> reportData = aml_Archival_Detail_Repo.getdatabydateList(parsedToDate,
                    version);

            if (reportData != null && !reportData.isEmpty()) {
                int rowIndex = 1;
                for (AML_Archival_Detail_Entity item : reportData) {
                    XSSFRow row = sheet.createRow(rowIndex++);

                    row.createCell(0).setCellValue(item.getCustId());
                    row.createCell(1).setCellValue(item.getAcctNumber());
                    row.createCell(2).setCellValue(item.getAcctName());

                    // ACCT BALANCE (right aligned, 3 decimal places)
                    Cell balanceCell = row.createCell(3);
                    if (item.getAcctBalanceInpula() != null) {
                        balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
                    } else {
                        balanceCell.setCellValue(0.000);
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
                logger.info("No data found for AML — only header will be written.");
            }

            // Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating  AML Excel", e);
            return new byte[0];
        }
    }

    @Autowired
    BRRS_AML_Detail_Repo brrs_aml_detail_repo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
        ModelAndView mv = new ModelAndView("BRRS/AML");

        if (acctNo != null) {
            AML_Detail_Entity amlEntity = brrs_aml_detail_repo.findByAcctnumber(acctNo);
            if (amlEntity != null && amlEntity.getReportDate() != null) {
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(amlEntity.getReportDate());
                mv.addObject("asondate", formattedDate);
            }
            mv.addObject("AMLData", amlEntity);
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

            AML_Detail_Entity existing = brrs_aml_detail_repo.findByAcctnumber(acctNo);
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
                brrs_aml_detail_repo.save(existing);
                logger.info("Record updated successfully for account {}", acctNo);

                // Format date for procedure
                String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
                        .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

                // Run summary procedure after commit
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        try {
                            logger.info("Transaction committed — calling BRRS_AML_SUMMARY_PROCEDURE({})",
                                    formattedDate);
                            jdbcTemplate.update("BEGIN BRRS_AML_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
            logger.error("Error updating AML record", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating record: " + e.getMessage());
        }
    }

}