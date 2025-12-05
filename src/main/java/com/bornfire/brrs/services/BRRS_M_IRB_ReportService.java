
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

import com.bornfire.brrs.entities.*;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Component
@Service

public class BRRS_M_IRB_ReportService {
    private static final Logger logger = LoggerFactory.getLogger(BRRS_M_PI_ReportService.class);

    @Autowired
    private Environment env;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    M_IRB_Summary_Repo2 m_irb_summary_repo_1;

    @Autowired
    M_IRB_Summary_Repo1 m_irb_summary_repo_2;

    @Autowired
    BRRS_M_IRB_Detail_Repo brrs_m_irb_detail_repo;

    @Autowired
    BRRS_M_IRB_Detail_Archival_Repo M_IRB_Archival_Detail_Repo;

    @Autowired
    M_IRB_Archival_Summary_Repo1 M_IRB_Archival_Summary_Repo_2;

    @Autowired
    M_IRB_Archival_Summary_Repo2 M_IRB_Archival_Summary_Repo_1;

    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

    public ModelAndView getM_IRBView(String reportId, String fromdate, String todate, String currency,
                                     String dtltype, Pageable pageable, String type, String version) {

        ModelAndView mv = new ModelAndView();
        Session hs = sessionFactory.getCurrentSession();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        if (type.equals("ARCHIVAL") & version != null) {
            List<M_IRB_Archival_Summary_Entity1> T1Master = new ArrayList<M_IRB_Archival_Summary_Entity1>();
            List<M_IRB_Archival_Summary_Entity2> T2Master = new ArrayList<M_IRB_Archival_Summary_Entity2>();
            try {
                Date d1 = dateformat.parse(todate);
                T2Master = M_IRB_Archival_Summary_Repo_1.getdatabydateListarchival(dateformat.parse(todate), version);
                T1Master = M_IRB_Archival_Summary_Repo_2.getdatabydateListarchival(dateformat.parse(todate), version);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            mv.addObject("reportsummary1", T1Master);
            mv.addObject("reportsummary2", T2Master);
        } else {

            List<M_IRB_Summary_Entity2> T2Master = new ArrayList<M_IRB_Summary_Entity2>();
            List<M_IRB_Summary_Entity1> T1Master = new ArrayList<M_IRB_Summary_Entity1>();
            try {
                Date d1 = dateformat.parse(todate);

                T2Master = m_irb_summary_repo_1.getdatabydateList(dateformat.parse(todate));
                T1Master = m_irb_summary_repo_2.getdatabydateList(dateformat.parse(todate));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            mv.addObject("reportsummary1", T1Master);
            mv.addObject("reportsummary2", T2Master);
        }

        // T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

        mv.setViewName("BRRS/M_IRB");

        // mv.addObject("reportmaster", T1Master);
        mv.addObject("displaymode", "summary");
        // mv.addObject("reportsflag", "reportsflag");
        // mv.addObject("menu", reportId);
        System.out.println("scv" + mv.getViewName());

        return mv;

    }

    public ModelAndView getM_IRBcurrentDtl(String reportId, String fromdate, String todate, String currency,
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

            // ✅ Split the filter string here
            if (filter != null && filter.contains(",")) {
                String[] parts = filter.split(",");
                if (parts.length >= 2) {
                    rowId = parts[0];
                    columnId = parts[1];
                }
            }

            if ("ARCHIVAL".equals(type) && version != null) {
                System.out.println(type);
                // 🔹 Archival branch
                List<BRRS_M_IRB_Detail_Archival_Entity> T1Dt1;
                if (rowId != null && columnId != null) {
                    T1Dt1 = M_IRB_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
                } else {
                    T1Dt1 = M_IRB_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
                    System.out.println(T1Dt1.size());
                }

                mv.addObject("reportdetails", T1Dt1);
                mv.addObject("reportmaster12", T1Dt1);
                System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

            } else {
                System.out.println("Praveen");
                // 🔹 Current branch
                List<BRRS_M_IRB_Detail_Entity> T1Dt1;
                if (rowId != null && columnId != null) {
                    T1Dt1 = brrs_m_irb_detail_repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
                } else {
                    T1Dt1 = brrs_m_irb_detail_repo.getdatabydateList(parsedDate, currentPage, pageSize);
                    totalPages = brrs_m_irb_detail_repo.getdatacount(parsedDate);
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
        mv.setViewName("BRRS/M_IRB");
        mv.addObject("displaymode", "Details");
        mv.addObject("currentPage", currentPage);
        System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
        mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
        mv.addObject("reportsflag", "reportsflag");
        mv.addObject("menu", reportId);

        return mv;
    }

    public byte[] BRRS_M_PIExcel(String filename, String reportId, String fromdate, String todate, String currency,
                                 String dtltype, String type, String version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");
        System.out.println(type);
        System.out.println(version);
        if (type.equals("ARCHIVAL") & version != null) {
            byte[] ARCHIVALreport = getExcelM_PIARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
                    version);
            return ARCHIVALreport;
        }

        List<M_IRB_Summary_Entity1> dataList = m_irb_summary_repo_2.getdatabydateList(dateformat.parse(todate));
        List<M_IRB_Summary_Entity2> dataList2 = m_irb_summary_repo_1.getdatabydateList(dateformat.parse(todate));

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for M_IRB report. Returning empty result.");
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

            if (!dataList.isEmpty()) {
                populateEntity1Data(sheet, dataList.get(0), textStyle, numberStyle);
            }

            if (!dataList2.isEmpty()) {
                populateEntity2Data(sheet, dataList2.get(0), textStyle, numberStyle);
            }

            int startRow = 7;

//            if (!dataList.isEmpty()) {
//                for (int i = 0; i < dataList.size(); i++) {
//                    M_IRB_SUMMARY_ENTITY_2 record = dataList.get(i);
//                    System.out.println("rownumber=" + startRow + i);
//                    Row row = sheet.getRow(startRow + i);
//                    if (row == null) {
//                        row = sheet.createRow(startRow + i);
//                    }
//
//
//
//
//
//
//
//
//
//                }
                workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
//            } else {
//
//            }

            // Write the final workbook content to the in-memory stream.
            workbook.write(out);

            logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

            return out.toByteArray();
        }
    }

    private void populateEntity2Data(Sheet sheet, M_IRB_Summary_Entity2 record, CellStyle textStyle, CellStyle numberStyle) {

                    Row row = sheet.getRow(53) != null ? sheet.getRow(53) : sheet.createRow(53);
                    // Row 54
//                    row = sheet.getRow(53);

                    Cell R54cell9 = row.createCell(10);
                    if (record.getR54_non_rate_sens_gov_bonds_non_rat_sens_itm() != null) {
                        R54cell9.setCellValue(record.getR54_non_rate_sens_gov_bonds_non_rat_sens_itm().doubleValue());
                        R54cell9.setCellStyle(numberStyle);
                    } else {
                        R54cell9.setCellValue("");
                        R54cell9.setCellStyle(textStyle);
                    }


// Row 55
                    row = sheet.getRow(54);

                    Cell R55cell9 = row.createCell(10);
                    if (record.getR55_non_rate_sens_other_invt_specify_non_rat_sens_itm() != null) {
                        R55cell9.setCellValue(record.getR55_non_rate_sens_other_invt_specify_non_rat_sens_itm().doubleValue());
                        R55cell9.setCellStyle(numberStyle);
                    } else {
                        R55cell9.setCellValue("");
                        R55cell9.setCellStyle(textStyle);
                    }


// Row 56
                    row = sheet.getRow(55);

                    Cell R56cell9 = row.createCell(10);
                    if (record.getR56_non_rate_sens_loans_and_adv_to_cust_non_rat_sens_itm() != null) {
                        R56cell9.setCellValue(record.getR56_non_rate_sens_loans_and_adv_to_cust_non_rat_sens_itm().doubleValue());
                        R56cell9.setCellStyle(numberStyle);
                    } else {
                        R56cell9.setCellValue("");
                        R56cell9.setCellStyle(textStyle);
                    }


// Row 57
                    row = sheet.getRow(56);

                    Cell R57cell9 = row.createCell(10);
                    if (record.getR57_non_rate_sens_prop_and_eqp_non_rat_sens_itm() != null) {
                        R57cell9.setCellValue(record.getR57_non_rate_sens_prop_and_eqp_non_rat_sens_itm().doubleValue());
                        R57cell9.setCellStyle(numberStyle);
                    } else {
                        R57cell9.setCellValue("");
                        R57cell9.setCellStyle(textStyle);
                    }


// Row 58
                    row = sheet.getRow(57);

                    Cell R58cell9 = row.createCell(10);
                    if (record.getR58_non_rate_sens_other_assets_specify_non_rat_sens_itm() != null) {
                        R58cell9.setCellValue(record.getR58_non_rate_sens_other_assets_specify_non_rat_sens_itm().doubleValue());
                        R58cell9.setCellStyle(numberStyle);
                    } else {
                        R58cell9.setCellValue("");
                        R58cell9.setCellStyle(textStyle);
                    }



// Row 61
                    row = sheet.getRow(60);
                    Cell R61cell1 = row.createCell(2);
                    if (record.getR61_lai_cap_amt_customer_deposits_up_to_1_mnt() != null) {
                        R61cell1.setCellValue(record.getR61_lai_cap_amt_customer_deposits_up_to_1_mnt().doubleValue());
                        R61cell1.setCellStyle(numberStyle);
                    } else {
                        R61cell1.setCellValue("");
                        R61cell1.setCellStyle(textStyle);
                    }

                    Cell R61cell2 = row.createCell(3);
                    if (record.getR61_lai_cap_amt_customer_deposits_mor_then_1_to_3_mon() != null) {
                        R61cell2.setCellValue(record.getR61_lai_cap_amt_customer_deposits_mor_then_1_to_3_mon().doubleValue());
                        R61cell2.setCellStyle(numberStyle);
                    } else {
                        R61cell2.setCellValue("");
                        R61cell2.setCellStyle(textStyle);
                    }

                    Cell R61cell3 = row.createCell(4);
                    if (record.getR61_lai_cap_amt_customer_deposits_mor_then_3_to_6_mon() != null) {
                        R61cell3.setCellValue(record.getR61_lai_cap_amt_customer_deposits_mor_then_3_to_6_mon().doubleValue());
                        R61cell3.setCellStyle(numberStyle);
                    } else {
                        R61cell3.setCellValue("");
                        R61cell3.setCellStyle(textStyle);
                    }

                    Cell R61cell4 = row.createCell(5);
                    if (record.getR61_lai_cap_amt_customer_deposits_mor_then_6_to_12_mon() != null) {
                        R61cell4.setCellValue(record.getR61_lai_cap_amt_customer_deposits_mor_then_6_to_12_mon().doubleValue());
                        R61cell4.setCellStyle(numberStyle);
                    } else {
                        R61cell4.setCellValue("");
                        R61cell4.setCellStyle(textStyle);
                    }

                    Cell R61cell5 = row.createCell(6);
                    if (record.getR61_lai_cap_amt_customer_deposits_mor_then_12_mon_to_3_year() != null) {
                        R61cell5.setCellValue(record.getR61_lai_cap_amt_customer_deposits_mor_then_12_mon_to_3_year().doubleValue());
                        R61cell5.setCellStyle(numberStyle);
                    } else {
                        R61cell5.setCellValue("");
                        R61cell5.setCellStyle(textStyle);
                    }

                    Cell R61cell6 = row.createCell(7);
                    if (record.getR61_lai_cap_amt_customer_deposits_mor_then_3_to_5_year() != null) {
                        R61cell6.setCellValue(record.getR61_lai_cap_amt_customer_deposits_mor_then_3_to_5_year().doubleValue());
                        R61cell6.setCellStyle(numberStyle);
                    } else {
                        R61cell6.setCellValue("");
                        R61cell6.setCellStyle(textStyle);
                    }

                    Cell R61cell7 = row.createCell(8);
                    if (record.getR61_lai_cap_amt_customer_deposits_mor_then_5_to_10_year() != null) {
                        R61cell7.setCellValue(record.getR61_lai_cap_amt_customer_deposits_mor_then_5_to_10_year().doubleValue());
                        R61cell7.setCellStyle(numberStyle);
                    } else {
                        R61cell7.setCellValue("");
                        R61cell7.setCellStyle(textStyle);
                    }

                    Cell R61cell8 = row.createCell(9);
                    if (record.getR61_lai_cap_amt_customer_deposits_mor_then_10_year() != null) {
                        R61cell8.setCellValue(record.getR61_lai_cap_amt_customer_deposits_mor_then_10_year().doubleValue());
                        R61cell8.setCellStyle(numberStyle);
                    } else {
                        R61cell8.setCellValue("");
                        R61cell8.setCellStyle(textStyle);
                    }



// Row 62
                    row = sheet.getRow(61);
                    Cell R62cell1 = row.createCell(2);
                    if (record.getR62_lai_cap_amt_bnk_of_botswana_up_to_1_mnt() != null) {
                        R62cell1.setCellValue(record.getR62_lai_cap_amt_bnk_of_botswana_up_to_1_mnt().doubleValue());
                        R62cell1.setCellStyle(numberStyle);
                    } else {
                        R62cell1.setCellValue("");
                        R62cell1.setCellStyle(textStyle);
                    }

                    Cell R62cell2 = row.createCell(3);
                    if (record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_1_to_3_mon() != null) {
                        R62cell2.setCellValue(record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_1_to_3_mon().doubleValue());
                        R62cell2.setCellStyle(numberStyle);
                    } else {
                        R62cell2.setCellValue("");
                        R62cell2.setCellStyle(textStyle);
                    }

                    Cell R62cell3 = row.createCell(4);
                    if (record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_3_to_6_mon() != null) {
                        R62cell3.setCellValue(record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_3_to_6_mon().doubleValue());
                        R62cell3.setCellStyle(numberStyle);
                    } else {
                        R62cell3.setCellValue("");
                        R62cell3.setCellStyle(textStyle);
                    }

                    Cell R62cell4 = row.createCell(5);
                    if (record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_6_to_12_mon() != null) {
                        R62cell4.setCellValue(record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_6_to_12_mon().doubleValue());
                        R62cell4.setCellStyle(numberStyle);
                    } else {
                        R62cell4.setCellValue("");
                        R62cell4.setCellStyle(textStyle);
                    }

                    Cell R62cell5 = row.createCell(6);
                    if (record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_12_mon_to_3_year() != null) {
                        R62cell5.setCellValue(record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_12_mon_to_3_year().doubleValue());
                        R62cell5.setCellStyle(numberStyle);
                    } else {
                        R62cell5.setCellValue("");
                        R62cell5.setCellStyle(textStyle);
                    }

                    Cell R62cell6 = row.createCell(7);
                    if (record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_3_to_5_year() != null) {
                        R62cell6.setCellValue(record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_3_to_5_year().doubleValue());
                        R62cell6.setCellStyle(numberStyle);
                    } else {
                        R62cell6.setCellValue("");
                        R62cell6.setCellStyle(textStyle);
                    }

                    Cell R62cell7 = row.createCell(8);
                    if (record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_5_to_10_year() != null) {
                        R62cell7.setCellValue(record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_5_to_10_year().doubleValue());
                        R62cell7.setCellStyle(numberStyle);
                    } else {
                        R62cell7.setCellValue("");
                        R62cell7.setCellStyle(textStyle);
                    }

                    Cell R62cell8 = row.createCell(9);
                    if (record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_10_year() != null) {
                        R62cell8.setCellValue(record.getR62_lai_cap_amt_bnk_of_botswana_mor_then_10_year().doubleValue());
                        R62cell8.setCellStyle(numberStyle);
                    } else {
                        R62cell8.setCellValue("");
                        R62cell8.setCellStyle(textStyle);
                    }


// Row 63
                    row = sheet.getRow(62);
                    Cell R63cell1 = row.createCell(2);
                    if (record.getR63_lai_cap_amt_doms_bnks_up_to_1_mnt() != null) {
                        R63cell1.setCellValue(record.getR63_lai_cap_amt_doms_bnks_up_to_1_mnt().doubleValue());
                        R63cell1.setCellStyle(numberStyle);
                    } else {
                        R63cell1.setCellValue("");
                        R63cell1.setCellStyle(textStyle);
                    }

                    Cell R63cell2 = row.createCell(3);
                    if (record.getR63_lai_cap_amt_doms_bnks_mor_then_1_to_3_mon() != null) {
                        R63cell2.setCellValue(record.getR63_lai_cap_amt_doms_bnks_mor_then_1_to_3_mon().doubleValue());
                        R63cell2.setCellStyle(numberStyle);
                    } else {
                        R63cell2.setCellValue("");
                        R63cell2.setCellStyle(textStyle);
                    }

                    Cell R63cell3 = row.createCell(4);
                    if (record.getR63_lai_cap_amt_doms_bnks_mor_then_3_to_6_mon() != null) {
                        R63cell3.setCellValue(record.getR63_lai_cap_amt_doms_bnks_mor_then_3_to_6_mon().doubleValue());
                        R63cell3.setCellStyle(numberStyle);
                    } else {
                        R63cell3.setCellValue("");
                        R63cell3.setCellStyle(textStyle);
                    }

                    Cell R63cell4 = row.createCell(5);
                    if (record.getR63_lai_cap_amt_doms_bnks_mor_then_6_to_12_mon() != null) {
                        R63cell4.setCellValue(record.getR63_lai_cap_amt_doms_bnks_mor_then_6_to_12_mon().doubleValue());
                        R63cell4.setCellStyle(numberStyle);
                    } else {
                        R63cell4.setCellValue("");
                        R63cell4.setCellStyle(textStyle);
                    }

                    Cell R63cell5 = row.createCell(6);
                    if (record.getR63_lai_cap_amt_doms_bnks_mor_then_12_mon_to_3_year() != null) {
                        R63cell5.setCellValue(record.getR63_lai_cap_amt_doms_bnks_mor_then_12_mon_to_3_year().doubleValue());
                        R63cell5.setCellStyle(numberStyle);
                    } else {
                        R63cell5.setCellValue("");
                        R63cell5.setCellStyle(textStyle);
                    }

                    Cell R63cell6 = row.createCell(7);
                    if (record.getR63_lai_cap_amt_doms_bnks_mor_then_3_to_5_year() != null) {
                        R63cell6.setCellValue(record.getR63_lai_cap_amt_doms_bnks_mor_then_3_to_5_year().doubleValue());
                        R63cell6.setCellStyle(numberStyle);
                    } else {
                        R63cell6.setCellValue("");
                        R63cell6.setCellStyle(textStyle);
                    }

                    Cell R63cell7 = row.createCell(8);
                    if (record.getR63_lai_cap_amt_doms_bnks_mor_then_5_to_10_year() != null) {
                        R63cell7.setCellValue(record.getR63_lai_cap_amt_doms_bnks_mor_then_5_to_10_year().doubleValue());
                        R63cell7.setCellStyle(numberStyle);
                    } else {
                        R63cell7.setCellValue("");
                        R63cell7.setCellStyle(textStyle);
                    }

                    Cell R63cell8 = row.createCell(9);
                    if (record.getR63_lai_cap_amt_doms_bnks_mor_then_10_year() != null) {
                        R63cell8.setCellValue(record.getR63_lai_cap_amt_doms_bnks_mor_then_10_year().doubleValue());
                        R63cell8.setCellStyle(numberStyle);
                    } else {
                        R63cell8.setCellValue("");
                        R63cell8.setCellStyle(textStyle);
                    }


// Row 64
                    row = sheet.getRow(63);
                    Cell R64cell1 = row.createCell(2);
                    if (record.getR64_lai_cap_amt_foreign_bnks_up_to_1_mnt() != null) {
                        R64cell1.setCellValue(record.getR64_lai_cap_amt_foreign_bnks_up_to_1_mnt().doubleValue());
                        R64cell1.setCellStyle(numberStyle);
                    } else {
                        R64cell1.setCellValue("");
                        R64cell1.setCellStyle(textStyle);
                    }

                    Cell R64cell2 = row.createCell(3);
                    if (record.getR64_lai_cap_amt_foreign_bnks_mor_then_1_to_3_mon() != null) {
                        R64cell2.setCellValue(record.getR64_lai_cap_amt_foreign_bnks_mor_then_1_to_3_mon().doubleValue());
                        R64cell2.setCellStyle(numberStyle);
                    } else {
                        R64cell2.setCellValue("");
                        R64cell2.setCellStyle(textStyle);
                    }

                    Cell R64cell3 = row.createCell(4);
                    if (record.getR64_lai_cap_amt_foreign_bnks_mor_then_3_to_6_mon() != null) {
                        R64cell3.setCellValue(record.getR64_lai_cap_amt_foreign_bnks_mor_then_3_to_6_mon().doubleValue());
                        R64cell3.setCellStyle(numberStyle);
                    } else {
                        R64cell3.setCellValue("");
                        R64cell3.setCellStyle(textStyle);
                    }

                    Cell R64cell4 = row.createCell(5);
                    if (record.getR64_lai_cap_amt_foreign_bnks_mor_then_6_to_12_mon() != null) {
                        R64cell4.setCellValue(record.getR64_lai_cap_amt_foreign_bnks_mor_then_6_to_12_mon().doubleValue());
                        R64cell4.setCellStyle(numberStyle);
                    } else {
                        R64cell4.setCellValue("");
                        R64cell4.setCellStyle(textStyle);
                    }

                    Cell R64cell5 = row.createCell(6);
                    if (record.getR64_lai_cap_amt_foreign_bnks_mor_then_12_mon_to_3_year() != null) {
                        R64cell5.setCellValue(record.getR64_lai_cap_amt_foreign_bnks_mor_then_12_mon_to_3_year().doubleValue());
                        R64cell5.setCellStyle(numberStyle);
                    } else {
                        R64cell5.setCellValue("");
                        R64cell5.setCellStyle(textStyle);
                    }

                    Cell R64cell6 = row.createCell(7);
                    if (record.getR64_lai_cap_amt_foreign_bnks_mor_then_3_to_5_year() != null) {
                        R64cell6.setCellValue(record.getR64_lai_cap_amt_foreign_bnks_mor_then_3_to_5_year().doubleValue());
                        R64cell6.setCellStyle(numberStyle);
                    } else {
                        R64cell6.setCellValue("");
                        R64cell6.setCellStyle(textStyle);
                    }

                    Cell R64cell7 = row.createCell(8);
                    if (record.getR64_lai_cap_amt_foreign_bnks_mor_then_5_to_10_year() != null) {
                        R64cell7.setCellValue(record.getR64_lai_cap_amt_foreign_bnks_mor_then_5_to_10_year().doubleValue());
                        R64cell7.setCellStyle(numberStyle);
                    } else {
                        R64cell7.setCellValue("");
                        R64cell7.setCellStyle(textStyle);
                    }

                    Cell R64cell8 = row.createCell(9);
                    if (record.getR64_lai_cap_amt_foreign_bnks_mor_then_10_year() != null) {
                        R64cell8.setCellValue(record.getR64_lai_cap_amt_foreign_bnks_mor_then_10_year().doubleValue());
                        R64cell8.setCellStyle(numberStyle);
                    } else {
                        R64cell8.setCellValue("");
                        R64cell8.setCellStyle(textStyle);
                    }


// Row 65
                    row = sheet.getRow(64);
                    Cell R65cell1 = row.createCell(2);
                    if (record.getR65_lai_cap_amt_related_comp_up_to_1_mnt() != null) {
                        R65cell1.setCellValue(record.getR65_lai_cap_amt_related_comp_up_to_1_mnt().doubleValue());
                        R65cell1.setCellStyle(numberStyle);
                    } else {
                        R65cell1.setCellValue("");
                        R65cell1.setCellStyle(textStyle);
                    }

                    Cell R65cell2 = row.createCell(3);
                    if (record.getR65_lai_cap_amt_related_comp_mor_then_1_to_3_mon() != null) {
                        R65cell2.setCellValue(record.getR65_lai_cap_amt_related_comp_mor_then_1_to_3_mon().doubleValue());
                        R65cell2.setCellStyle(numberStyle);
                    } else {
                        R65cell2.setCellValue("");
                        R65cell2.setCellStyle(textStyle);
                    }

                    Cell R65cell3 = row.createCell(4);
                    if (record.getR65_lai_cap_amt_related_comp_mor_then_3_to_6_mon() != null) {
                        R65cell3.setCellValue(record.getR65_lai_cap_amt_related_comp_mor_then_3_to_6_mon().doubleValue());
                        R65cell3.setCellStyle(numberStyle);
                    } else {
                        R65cell3.setCellValue("");
                        R65cell3.setCellStyle(textStyle);
                    }

                    Cell R65cell4 = row.createCell(5);
                    if (record.getR65_lai_cap_amt_related_comp_mor_then_6_to_12_mon() != null) {
                        R65cell4.setCellValue(record.getR65_lai_cap_amt_related_comp_mor_then_6_to_12_mon().doubleValue());
                        R65cell4.setCellStyle(numberStyle);
                    } else {
                        R65cell4.setCellValue("");
                        R65cell4.setCellStyle(textStyle);
                    }

                    Cell R65cell5 = row.createCell(6);
                    if (record.getR65_lai_cap_amt_related_comp_mor_then_12_mon_to_3_year() != null) {
                        R65cell5.setCellValue(record.getR65_lai_cap_amt_related_comp_mor_then_12_mon_to_3_year().doubleValue());
                        R65cell5.setCellStyle(numberStyle);
                    } else {
                        R65cell5.setCellValue("");
                        R65cell5.setCellStyle(textStyle);
                    }

                    Cell R65cell6 = row.createCell(7);
                    if (record.getR65_lai_cap_amt_related_comp_mor_then_3_to_5_year() != null) {
                        R65cell6.setCellValue(record.getR65_lai_cap_amt_related_comp_mor_then_3_to_5_year().doubleValue());
                        R65cell6.setCellStyle(numberStyle);
                    } else {
                        R65cell6.setCellValue("");
                        R65cell6.setCellStyle(textStyle);
                    }

                    Cell R65cell7 = row.createCell(8);
                    if (record.getR65_lai_cap_amt_related_comp_mor_then_5_to_10_year() != null) {
                        R65cell7.setCellValue(record.getR65_lai_cap_amt_related_comp_mor_then_5_to_10_year().doubleValue());
                        R65cell7.setCellStyle(numberStyle);
                    } else {
                        R65cell7.setCellValue("");
                        R65cell7.setCellStyle(textStyle);
                    }

                    Cell R65cell8 = row.createCell(9);
                    if (record.getR65_lai_cap_amt_related_comp_mor_then_10_year() != null) {
                        R65cell8.setCellValue(record.getR65_lai_cap_amt_related_comp_mor_then_10_year().doubleValue());
                        R65cell8.setCellStyle(numberStyle);
                    } else {
                        R65cell8.setCellValue("");
                        R65cell8.setCellStyle(textStyle);
                    }



// Row 66
                    row = sheet.getRow(65);
                    Cell R66cell1 = row.createCell(2);
                    if (record.getR66_lai_cap_borrowed_funds_up_to_1_mnt() != null) {
                        R66cell1.setCellValue(record.getR66_lai_cap_borrowed_funds_up_to_1_mnt().doubleValue());
                        R66cell1.setCellStyle(numberStyle);
                    } else {
                        R66cell1.setCellValue("");
                        R66cell1.setCellStyle(textStyle);
                    }

                    Cell R66cell2 = row.createCell(3);
                    if (record.getR66_lai_cap_borrowed_funds_mor_then_1_to_3_mon() != null) {
                        R66cell2.setCellValue(record.getR66_lai_cap_borrowed_funds_mor_then_1_to_3_mon().doubleValue());
                        R66cell2.setCellStyle(numberStyle);
                    } else {
                        R66cell2.setCellValue("");
                        R66cell2.setCellStyle(textStyle);
                    }

                    Cell R66cell3 = row.createCell(4);
                    if (record.getR66_lai_cap_borrowed_funds_mor_then_3_to_6_mon() != null) {
                        R66cell3.setCellValue(record.getR66_lai_cap_borrowed_funds_mor_then_3_to_6_mon().doubleValue());
                        R66cell3.setCellStyle(numberStyle);
                    } else {
                        R66cell3.setCellValue("");
                        R66cell3.setCellStyle(textStyle);
                    }

                    Cell R66cell4 = row.createCell(5);
                    if (record.getR66_lai_cap_borrowed_funds_mor_then_6_to_12_mon() != null) {
                        R66cell4.setCellValue(record.getR66_lai_cap_borrowed_funds_mor_then_6_to_12_mon().doubleValue());
                        R66cell4.setCellStyle(numberStyle);
                    } else {
                        R66cell4.setCellValue("");
                        R66cell4.setCellStyle(textStyle);
                    }

                    Cell R66cell5 = row.createCell(6);
                    if (record.getR66_lai_cap_borrowed_funds_mor_then_12_mon_to_3_year() != null) {
                        R66cell5.setCellValue(record.getR66_lai_cap_borrowed_funds_mor_then_12_mon_to_3_year().doubleValue());
                        R66cell5.setCellStyle(numberStyle);
                    } else {
                        R66cell5.setCellValue("");
                        R66cell5.setCellStyle(textStyle);
                    }

                    Cell R66cell6 = row.createCell(7);
                    if (record.getR66_lai_cap_borrowed_funds_mor_then_3_to_5_year() != null) {
                        R66cell6.setCellValue(record.getR66_lai_cap_borrowed_funds_mor_then_3_to_5_year().doubleValue());
                        R66cell6.setCellStyle(numberStyle);
                    } else {
                        R66cell6.setCellValue("");
                        R66cell6.setCellStyle(textStyle);
                    }

                    Cell R66cell7 = row.createCell(8);
                    if (record.getR66_lai_cap_borrowed_funds_mor_then_5_to_10_year() != null) {
                        R66cell7.setCellValue(record.getR66_lai_cap_borrowed_funds_mor_then_5_to_10_year().doubleValue());
                        R66cell7.setCellStyle(numberStyle);
                    } else {
                        R66cell7.setCellValue("");
                        R66cell7.setCellStyle(textStyle);
                    }

                    Cell R66cell8 = row.createCell(9);
                    if (record.getR66_lai_cap_borrowed_funds_mor_then_10_year() != null) {
                        R66cell8.setCellValue(record.getR66_lai_cap_borrowed_funds_mor_then_10_year().doubleValue());
                        R66cell8.setCellStyle(numberStyle);
                    } else {
                        R66cell8.setCellValue("");
                        R66cell8.setCellStyle(textStyle);
                    }


// Row 67
                    row = sheet.getRow(66);
                    Cell R67cell1 = row.createCell(2);
                    if (record.getR67_lai_cap_other_liabilities_specify_up_to_1_mnt() != null) {
                        R67cell1.setCellValue(record.getR67_lai_cap_other_liabilities_specify_up_to_1_mnt().doubleValue());
                        R67cell1.setCellStyle(numberStyle);
                    } else {
                        R67cell1.setCellValue("");
                        R67cell1.setCellStyle(textStyle);
                    }

                    Cell R67cell2 = row.createCell(3);
                    if (record.getR67_lai_cap_other_liabilities_specify_mor_then_1_to_3_mon() != null) {
                        R67cell2.setCellValue(record.getR67_lai_cap_other_liabilities_specify_mor_then_1_to_3_mon().doubleValue());
                        R67cell2.setCellStyle(numberStyle);
                    } else {
                        R67cell2.setCellValue("");
                        R67cell2.setCellStyle(textStyle);
                    }

                    Cell R67cell3 = row.createCell(4);
                    if (record.getR67_lai_cap_other_liabilities_specify_mor_then_3_to_6_mon() != null) {
                        R67cell3.setCellValue(record.getR67_lai_cap_other_liabilities_specify_mor_then_3_to_6_mon().doubleValue());
                        R67cell3.setCellStyle(numberStyle);
                    } else {
                        R67cell3.setCellValue("");
                        R67cell3.setCellStyle(textStyle);
                    }

                    Cell R67cell4 = row.createCell(5);
                    if (record.getR67_lai_cap_other_liabilities_specify_mor_then_6_to_12_mon() != null) {
                        R67cell4.setCellValue(record.getR67_lai_cap_other_liabilities_specify_mor_then_6_to_12_mon().doubleValue());
                        R67cell4.setCellStyle(numberStyle);
                    } else {
                        R67cell4.setCellValue("");
                        R67cell4.setCellStyle(textStyle);
                    }

                    Cell R67cell5 = row.createCell(6);
                    if (record.getR67_lai_cap_other_liabilities_specify_mor_then_12_mon_to_3_year() != null) {
                        R67cell5.setCellValue(record.getR67_lai_cap_other_liabilities_specify_mor_then_12_mon_to_3_year().doubleValue());
                        R67cell5.setCellStyle(numberStyle);
                    } else {
                        R67cell5.setCellValue("");
                        R67cell5.setCellStyle(textStyle);
                    }

                    Cell R67cell6 = row.createCell(7);
                    if (record.getR67_lai_cap_other_liabilities_specify_mor_then_3_to_5_year() != null) {
                        R67cell6.setCellValue(record.getR67_lai_cap_other_liabilities_specify_mor_then_3_to_5_year().doubleValue());
                        R67cell6.setCellStyle(numberStyle);
                    } else {
                        R67cell6.setCellValue("");
                        R67cell6.setCellStyle(textStyle);
                    }

                    Cell R67cell7 = row.createCell(8);
                    if (record.getR67_lai_cap_other_liabilities_specify_mor_then_5_to_10_year() != null) {
                        R67cell7.setCellValue(record.getR67_lai_cap_other_liabilities_specify_mor_then_5_to_10_year().doubleValue());
                        R67cell7.setCellStyle(numberStyle);
                    } else {
                        R67cell7.setCellValue("");
                        R67cell7.setCellStyle(textStyle);
                    }

                    Cell R67cell8 = row.createCell(9);
                    if (record.getR67_lai_cap_other_liabilities_specify_mor_then_10_year() != null) {
                        R67cell8.setCellValue(record.getR67_lai_cap_other_liabilities_specify_mor_then_10_year().doubleValue());
                        R67cell8.setCellStyle(numberStyle);
                    } else {
                        R67cell8.setCellValue("");
                        R67cell8.setCellStyle(textStyle);
                    }



// Row 69
                    row = sheet.getRow(68);
                    Cell R69cell1 = row.createCell(2);
                    if (record.getR69_dis_admt_amt_customer_deposits_up_to_1_mnt() != null) {
                        R69cell1.setCellValue(record.getR69_dis_admt_amt_customer_deposits_up_to_1_mnt().doubleValue());
                        R69cell1.setCellStyle(numberStyle);
                    } else {
                        R69cell1.setCellValue("");
                        R69cell1.setCellStyle(textStyle);
                    }

                    Cell R69cell2 = row.createCell(3);
                    if (record.getR69_dis_admt_amt_customer_deposits_mor_then_1_to_3_mon() != null) {
                        R69cell2.setCellValue(record.getR69_dis_admt_amt_customer_deposits_mor_then_1_to_3_mon().doubleValue());
                        R69cell2.setCellStyle(numberStyle);
                    } else {
                        R69cell2.setCellValue("");
                        R69cell2.setCellStyle(textStyle);
                    }

                    Cell R69cell3 = row.createCell(4);
                    if (record.getR69_dis_admt_amt_customer_deposits_mor_then_3_to_6_mon() != null) {
                        R69cell3.setCellValue(record.getR69_dis_admt_amt_customer_deposits_mor_then_3_to_6_mon().doubleValue());
                        R69cell3.setCellStyle(numberStyle);
                    } else {
                        R69cell3.setCellValue("");
                        R69cell3.setCellStyle(textStyle);
                    }

                    Cell R69cell4 = row.createCell(5);
                    if (record.getR69_dis_admt_amt_customer_deposits_mor_then_6_to_12_mon() != null) {
                        R69cell4.setCellValue(record.getR69_dis_admt_amt_customer_deposits_mor_then_6_to_12_mon().doubleValue());
                        R69cell4.setCellStyle(numberStyle);
                    } else {
                        R69cell4.setCellValue("");
                        R69cell4.setCellStyle(textStyle);
                    }

                    Cell R69cell5 = row.createCell(6);
                    if (record.getR69_dis_admt_amt_customer_deposits_mor_then_12_mon_to_3_year() != null) {
                        R69cell5.setCellValue(record.getR69_dis_admt_amt_customer_deposits_mor_then_12_mon_to_3_year().doubleValue());
                        R69cell5.setCellStyle(numberStyle);
                    } else {
                        R69cell5.setCellValue("");
                        R69cell5.setCellStyle(textStyle);
                    }

                    Cell R69cell6 = row.createCell(7);
                    if (record.getR69_dis_admt_amt_customer_deposits_mor_then_3_to_5_year() != null) {
                        R69cell6.setCellValue(record.getR69_dis_admt_amt_customer_deposits_mor_then_3_to_5_year().doubleValue());
                        R69cell6.setCellStyle(numberStyle);
                    } else {
                        R69cell6.setCellValue("");
                        R69cell6.setCellStyle(textStyle);
                    }

                    Cell R69cell7 = row.createCell(8);
                    if (record.getR69_dis_admt_amt_customer_deposits_mor_then_5_to_10_year() != null) {
                        R69cell7.setCellValue(record.getR69_dis_admt_amt_customer_deposits_mor_then_5_to_10_year().doubleValue());
                        R69cell7.setCellStyle(numberStyle);
                    } else {
                        R69cell7.setCellValue("");
                        R69cell7.setCellStyle(textStyle);
                    }

                    Cell R69cell8 = row.createCell(9);
                    if (record.getR69_dis_admt_amt_customer_deposits_mor_then_10_year() != null) {
                        R69cell8.setCellValue(record.getR69_dis_admt_amt_customer_deposits_mor_then_10_year().doubleValue());
                        R69cell8.setCellStyle(numberStyle);
                    } else {
                        R69cell8.setCellValue("");
                        R69cell8.setCellStyle(textStyle);
                    }


// Row 70
                    row = sheet.getRow(69);
                    Cell R70cell1 = row.createCell(2);
                    if (record.getR70_dis_admt_amt_bnk_of_botswana_up_to_1_mnt() != null) {
                        R70cell1.setCellValue(record.getR70_dis_admt_amt_bnk_of_botswana_up_to_1_mnt().doubleValue());
                        R70cell1.setCellStyle(numberStyle);
                    } else {
                        R70cell1.setCellValue("");
                        R70cell1.setCellStyle(textStyle);
                    }

                    Cell R70cell2 = row.createCell(3);
                    if (record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_1_to_3_mon() != null) {
                        R70cell2.setCellValue(record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_1_to_3_mon().doubleValue());
                        R70cell2.setCellStyle(numberStyle);
                    } else {
                        R70cell2.setCellValue("");
                        R70cell2.setCellStyle(textStyle);
                    }

                    Cell R70cell3 = row.createCell(4);
                    if (record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_3_to_6_mon() != null) {
                        R70cell3.setCellValue(record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_3_to_6_mon().doubleValue());
                        R70cell3.setCellStyle(numberStyle);
                    } else {
                        R70cell3.setCellValue("");
                        R70cell3.setCellStyle(textStyle);
                    }

                    Cell R70cell4 = row.createCell(5);
                    if (record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_6_to_12_mon() != null) {
                        R70cell4.setCellValue(record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_6_to_12_mon().doubleValue());
                        R70cell4.setCellStyle(numberStyle);
                    } else {
                        R70cell4.setCellValue("");
                        R70cell4.setCellStyle(textStyle);
                    }

                    Cell R70cell5 = row.createCell(6);
                    if (record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_12_mon_to_3_year() != null) {
                        R70cell5.setCellValue(record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_12_mon_to_3_year().doubleValue());
                        R70cell5.setCellStyle(numberStyle);
                    } else {
                        R70cell5.setCellValue("");
                        R70cell5.setCellStyle(textStyle);
                    }

                    Cell R70cell6 = row.createCell(7);
                    if (record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_3_to_5_year() != null) {
                        R70cell6.setCellValue(record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_3_to_5_year().doubleValue());
                        R70cell6.setCellStyle(numberStyle);
                    } else {
                        R70cell6.setCellValue("");
                        R70cell6.setCellStyle(textStyle);
                    }

                    Cell R70cell7 = row.createCell(8);
                    if (record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_5_to_10_year() != null) {
                        R70cell7.setCellValue(record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_5_to_10_year().doubleValue());
                        R70cell7.setCellStyle(numberStyle);
                    } else {
                        R70cell7.setCellValue("");
                        R70cell7.setCellStyle(textStyle);
                    }

                    Cell R70cell8 = row.createCell(9);
                    if (record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_10_year() != null) {
                        R70cell8.setCellValue(record.getR70_dis_admt_amt_bnk_of_botswana_mor_then_10_year().doubleValue());
                        R70cell8.setCellStyle(numberStyle);
                    } else {
                        R70cell8.setCellValue("");
                        R70cell8.setCellStyle(textStyle);
                    }


// Row 71
                    row = sheet.getRow(70);
                    Cell R71cell1 = row.createCell(2);
                    if (record.getR71_dis_admt_amt_doms_bnks_up_to_1_mnt() != null) {
                        R71cell1.setCellValue(record.getR71_dis_admt_amt_doms_bnks_up_to_1_mnt().doubleValue());
                        R71cell1.setCellStyle(numberStyle);
                    } else {
                        R71cell1.setCellValue("");
                        R71cell1.setCellStyle(textStyle);
                    }

                    Cell R71cell2 = row.createCell(3);
                    if (record.getR71_dis_admt_amt_doms_bnks_mor_then_1_to_3_mon() != null) {
                        R71cell2.setCellValue(record.getR71_dis_admt_amt_doms_bnks_mor_then_1_to_3_mon().doubleValue());
                        R71cell2.setCellStyle(numberStyle);
                    } else {
                        R71cell2.setCellValue("");
                        R71cell2.setCellStyle(textStyle);
                    }

                    Cell R71cell3 = row.createCell(4);
                    if (record.getR71_dis_admt_amt_doms_bnks_mor_then_3_to_6_mon() != null) {
                        R71cell3.setCellValue(record.getR71_dis_admt_amt_doms_bnks_mor_then_3_to_6_mon().doubleValue());
                        R71cell3.setCellStyle(numberStyle);
                    } else {
                        R71cell3.setCellValue("");
                        R71cell3.setCellStyle(textStyle);
                    }

                    Cell R71cell4 = row.createCell(5);
                    if (record.getR71_dis_admt_amt_doms_bnks_mor_then_6_to_12_mon() != null) {
                        R71cell4.setCellValue(record.getR71_dis_admt_amt_doms_bnks_mor_then_6_to_12_mon().doubleValue());
                        R71cell4.setCellStyle(numberStyle);
                    } else {
                        R71cell4.setCellValue("");
                        R71cell4.setCellStyle(textStyle);
                    }

                    Cell R71cell5 = row.createCell(6);
                    if (record.getR71_dis_admt_amt_doms_bnks_mor_then_12_mon_to_3_year() != null) {
                        R71cell5.setCellValue(record.getR71_dis_admt_amt_doms_bnks_mor_then_12_mon_to_3_year().doubleValue());
                        R71cell5.setCellStyle(numberStyle);
                    } else {
                        R71cell5.setCellValue("");
                        R71cell5.setCellStyle(textStyle);
                    }

                    Cell R71cell6 = row.createCell(7);
                    if (record.getR71_dis_admt_amt_doms_bnks_mor_then_3_to_5_year() != null) {
                        R71cell6.setCellValue(record.getR71_dis_admt_amt_doms_bnks_mor_then_3_to_5_year().doubleValue());
                        R71cell6.setCellStyle(numberStyle);
                    } else {
                        R71cell6.setCellValue("");
                        R71cell6.setCellStyle(textStyle);
                    }

                    Cell R71cell7 = row.createCell(8);
                    if (record.getR71_dis_admt_amt_doms_bnks_mor_then_5_to_10_year() != null) {
                        R71cell7.setCellValue(record.getR71_dis_admt_amt_doms_bnks_mor_then_5_to_10_year().doubleValue());
                        R71cell7.setCellStyle(numberStyle);
                    } else {
                        R71cell7.setCellValue("");
                        R71cell7.setCellStyle(textStyle);
                    }

                    Cell R71cell8 = row.createCell(9);
                    if (record.getR71_dis_admt_amt_doms_bnks_mor_then_10_year() != null) {
                        R71cell8.setCellValue(record.getR71_dis_admt_amt_doms_bnks_mor_then_10_year().doubleValue());
                        R71cell8.setCellStyle(numberStyle);
                    } else {
                        R71cell8.setCellValue("");
                        R71cell8.setCellStyle(textStyle);
                    }


// Row 72
                    row = sheet.getRow(71);
                    Cell R72cell1 = row.createCell(2);
                    if (record.getR72_dis_admt_amt_foreign_bnks_up_to_1_mnt() != null) {
                        R72cell1.setCellValue(record.getR72_dis_admt_amt_foreign_bnks_up_to_1_mnt().doubleValue());
                        R72cell1.setCellStyle(numberStyle);
                    } else {
                        R72cell1.setCellValue("");
                        R72cell1.setCellStyle(textStyle);
                    }

                    Cell R72cell2 = row.createCell(3);
                    if (record.getR72_dis_admt_amt_foreign_bnks_mor_then_1_to_3_mon() != null) {
                        R72cell2.setCellValue(record.getR72_dis_admt_amt_foreign_bnks_mor_then_1_to_3_mon().doubleValue());
                        R72cell2.setCellStyle(numberStyle);
                    } else {
                        R72cell2.setCellValue("");
                        R72cell2.setCellStyle(textStyle);
                    }

                    Cell R72cell3 = row.createCell(4);
                    if (record.getR72_dis_admt_amt_foreign_bnks_mor_then_3_to_6_mon() != null) {
                        R72cell3.setCellValue(record.getR72_dis_admt_amt_foreign_bnks_mor_then_3_to_6_mon().doubleValue());
                        R72cell3.setCellStyle(numberStyle);
                    } else {
                        R72cell3.setCellValue("");
                        R72cell3.setCellStyle(textStyle);
                    }

                    Cell R72cell4 = row.createCell(5);
                    if (record.getR72_dis_admt_amt_foreign_bnks_mor_then_6_to_12_mon() != null) {
                        R72cell4.setCellValue(record.getR72_dis_admt_amt_foreign_bnks_mor_then_6_to_12_mon().doubleValue());
                        R72cell4.setCellStyle(numberStyle);
                    } else {
                        R72cell4.setCellValue("");
                        R72cell4.setCellStyle(textStyle);
                    }

                    Cell R72cell5 = row.createCell(6);
                    if (record.getR72_dis_admt_amt_foreign_bnks_mor_then_12_mon_to_3_year() != null) {
                        R72cell5.setCellValue(record.getR72_dis_admt_amt_foreign_bnks_mor_then_12_mon_to_3_year().doubleValue());
                        R72cell5.setCellStyle(numberStyle);
                    } else {
                        R72cell5.setCellValue("");
                        R72cell5.setCellStyle(textStyle);
                    }

                    Cell R72cell6 = row.createCell(7);
                    if (record.getR72_dis_admt_amt_foreign_bnks_mor_then_3_to_5_year() != null) {
                        R72cell6.setCellValue(record.getR72_dis_admt_amt_foreign_bnks_mor_then_3_to_5_year().doubleValue());
                        R72cell6.setCellStyle(numberStyle);
                    } else {
                        R72cell6.setCellValue("");
                        R72cell6.setCellStyle(textStyle);
                    }

                    Cell R72cell7 = row.createCell(8);
                    if (record.getR72_dis_admt_amt_foreign_bnks_mor_then_5_to_10_year() != null) {
                        R72cell7.setCellValue(record.getR72_dis_admt_amt_foreign_bnks_mor_then_5_to_10_year().doubleValue());
                        R72cell7.setCellStyle(numberStyle);
                    } else {
                        R72cell7.setCellValue("");
                        R72cell7.setCellStyle(textStyle);
                    }

                    Cell R72cell8 = row.createCell(9);
                    if (record.getR72_dis_admt_amt_foreign_bnks_mor_then_10_year() != null) {
                        R72cell8.setCellValue(record.getR72_dis_admt_amt_foreign_bnks_mor_then_10_year().doubleValue());
                        R72cell8.setCellStyle(numberStyle);
                    } else {
                        R72cell8.setCellValue("");
                        R72cell8.setCellStyle(textStyle);
                    }


// Row 73
                    row = sheet.getRow(72);
                    Cell R73cell1 = row.createCell(2);
                    if (record.getR73_dis_admt_amt_related_comp_up_to_1_mnt() != null) {
                        R73cell1.setCellValue(record.getR73_dis_admt_amt_related_comp_up_to_1_mnt().doubleValue());
                        R73cell1.setCellStyle(numberStyle);
                    } else {
                        R73cell1.setCellValue("");
                        R73cell1.setCellStyle(textStyle);
                    }

                    Cell R73cell2 = row.createCell(3);
                    if (record.getR73_dis_admt_amt_related_comp_mor_then_1_to_3_mon() != null) {
                        R73cell2.setCellValue(record.getR73_dis_admt_amt_related_comp_mor_then_1_to_3_mon().doubleValue());
                        R73cell2.setCellStyle(numberStyle);
                    } else {
                        R73cell2.setCellValue("");
                        R73cell2.setCellStyle(textStyle);
                    }

                    Cell R73cell3 = row.createCell(4);
                    if (record.getR73_dis_admt_amt_related_comp_mor_then_3_to_6_mon() != null) {
                        R73cell3.setCellValue(record.getR73_dis_admt_amt_related_comp_mor_then_3_to_6_mon().doubleValue());
                        R73cell3.setCellStyle(numberStyle);
                    } else {
                        R73cell3.setCellValue("");
                        R73cell3.setCellStyle(textStyle);
                    }

                    Cell R73cell4 = row.createCell(5);
                    if (record.getR73_dis_admt_amt_related_comp_mor_then_6_to_12_mon() != null) {
                        R73cell4.setCellValue(record.getR73_dis_admt_amt_related_comp_mor_then_6_to_12_mon().doubleValue());
                        R73cell4.setCellStyle(numberStyle);
                    } else {
                        R73cell4.setCellValue("");
                        R73cell4.setCellStyle(textStyle);
                    }

                    Cell R73cell5 = row.createCell(6);
                    if (record.getR73_dis_admt_amt_related_comp_mor_then_12_mon_to_3_year() != null) {
                        R73cell5.setCellValue(record.getR73_dis_admt_amt_related_comp_mor_then_12_mon_to_3_year().doubleValue());
                        R73cell5.setCellStyle(numberStyle);
                    } else {
                        R73cell5.setCellValue("");
                        R73cell5.setCellStyle(textStyle);
                    }

                    Cell R73cell6 = row.createCell(7);
                    if (record.getR73_dis_admt_amt_related_comp_mor_then_3_to_5_year() != null) {
                        R73cell6.setCellValue(record.getR73_dis_admt_amt_related_comp_mor_then_3_to_5_year().doubleValue());
                        R73cell6.setCellStyle(numberStyle);
                    } else {
                        R73cell6.setCellValue("");
                        R73cell6.setCellStyle(textStyle);
                    }

                    Cell R73cell7 = row.createCell(8);
                    if (record.getR73_dis_admt_amt_related_comp_mor_then_5_to_10_year() != null) {
                        R73cell7.setCellValue(record.getR73_dis_admt_amt_related_comp_mor_then_5_to_10_year().doubleValue());
                        R73cell7.setCellStyle(numberStyle);
                    } else {
                        R73cell7.setCellValue("");
                        R73cell7.setCellStyle(textStyle);
                    }

                    Cell R73cell8 = row.createCell(9);
                    if (record.getR73_dis_admt_amt_related_comp_mor_then_10_year() != null) {
                        R73cell8.setCellValue(record.getR73_dis_admt_amt_related_comp_mor_then_10_year().doubleValue());
                        R73cell8.setCellStyle(numberStyle);
                    } else {
                        R73cell8.setCellValue("");
                        R73cell8.setCellStyle(textStyle);
                    }



// Row 74
                    row = sheet.getRow(73);
                    Cell R74cell1 = row.createCell(2);
                    if (record.getR74_dis_admt_borrowed_funds_up_to_1_mnt() != null) {
                        R74cell1.setCellValue(record.getR74_dis_admt_borrowed_funds_up_to_1_mnt().doubleValue());
                        R74cell1.setCellStyle(numberStyle);
                    } else {
                        R74cell1.setCellValue("");
                        R74cell1.setCellStyle(textStyle);
                    }

                    Cell R74cell2 = row.createCell(3);
                    if (record.getR74_dis_admt_borrowed_funds_mor_then_1_to_3_mon() != null) {
                        R74cell2.setCellValue(record.getR74_dis_admt_borrowed_funds_mor_then_1_to_3_mon().doubleValue());
                        R74cell2.setCellStyle(numberStyle);
                    } else {
                        R74cell2.setCellValue("");
                        R74cell2.setCellStyle(textStyle);
                    }

                    Cell R74cell3 = row.createCell(4);
                    if (record.getR74_dis_admt_borrowed_funds_mor_then_3_to_6_mon() != null) {
                        R74cell3.setCellValue(record.getR74_dis_admt_borrowed_funds_mor_then_3_to_6_mon().doubleValue());
                        R74cell3.setCellStyle(numberStyle);
                    } else {
                        R74cell3.setCellValue("");
                        R74cell3.setCellStyle(textStyle);
                    }

                    Cell R74cell4 = row.createCell(5);
                    if (record.getR74_dis_admt_borrowed_funds_mor_then_6_to_12_mon() != null) {
                        R74cell4.setCellValue(record.getR74_dis_admt_borrowed_funds_mor_then_6_to_12_mon().doubleValue());
                        R74cell4.setCellStyle(numberStyle);
                    } else {
                        R74cell4.setCellValue("");
                        R74cell4.setCellStyle(textStyle);
                    }

                    Cell R74cell5 = row.createCell(6);
                    if (record.getR74_dis_admt_borrowed_funds_mor_then_12_mon_to_3_year() != null) {
                        R74cell5.setCellValue(record.getR74_dis_admt_borrowed_funds_mor_then_12_mon_to_3_year().doubleValue());
                        R74cell5.setCellStyle(numberStyle);
                    } else {
                        R74cell5.setCellValue("");
                        R74cell5.setCellStyle(textStyle);
                    }

                    Cell R74cell6 = row.createCell(7);
                    if (record.getR74_dis_admt_borrowed_funds_mor_then_3_to_5_year() != null) {
                        R74cell6.setCellValue(record.getR74_dis_admt_borrowed_funds_mor_then_3_to_5_year().doubleValue());
                        R74cell6.setCellStyle(numberStyle);
                    } else {
                        R74cell6.setCellValue("");
                        R74cell6.setCellStyle(textStyle);
                    }

                    Cell R74cell7 = row.createCell(8);
                    if (record.getR74_dis_admt_borrowed_funds_mor_then_5_to_10_year() != null) {
                        R74cell7.setCellValue(record.getR74_dis_admt_borrowed_funds_mor_then_5_to_10_year().doubleValue());
                        R74cell7.setCellStyle(numberStyle);
                    } else {
                        R74cell7.setCellValue("");
                        R74cell7.setCellStyle(textStyle);
                    }

                    Cell R74cell8 = row.createCell(9);
                    if (record.getR74_dis_admt_borrowed_funds_mor_then_10_year() != null) {
                        R74cell8.setCellValue(record.getR74_dis_admt_borrowed_funds_mor_then_10_year().doubleValue());
                        R74cell8.setCellStyle(numberStyle);
                    } else {
                        R74cell8.setCellValue("");
                        R74cell8.setCellStyle(textStyle);
                    }


// Row 75
                    row = sheet.getRow(74);
                    Cell R75cell1 = row.createCell(2);
                    if (record.getR75_dis_admt_other_liabilities_specify_up_to_1_mnt() != null) {
                        R75cell1.setCellValue(record.getR75_dis_admt_other_liabilities_specify_up_to_1_mnt().doubleValue());
                        R75cell1.setCellStyle(numberStyle);
                    } else {
                        R75cell1.setCellValue("");
                        R75cell1.setCellStyle(textStyle);
                    }

                    Cell R75cell2 = row.createCell(3);
                    if (record.getR75_dis_admt_other_liabilities_specify_mor_then_1_to_3_mon() != null) {
                        R75cell2.setCellValue(record.getR75_dis_admt_other_liabilities_specify_mor_then_1_to_3_mon().doubleValue());
                        R75cell2.setCellStyle(numberStyle);
                    } else {
                        R75cell2.setCellValue("");
                        R75cell2.setCellStyle(textStyle);
                    }

                    Cell R75cell3 = row.createCell(4);
                    if (record.getR75_dis_admt_other_liabilities_specify_mor_then_3_to_6_mon() != null) {
                        R75cell3.setCellValue(record.getR75_dis_admt_other_liabilities_specify_mor_then_3_to_6_mon().doubleValue());
                        R75cell3.setCellStyle(numberStyle);
                    } else {
                        R75cell3.setCellValue("");
                        R75cell3.setCellStyle(textStyle);
                    }

                    Cell R75cell4 = row.createCell(5);
                    if (record.getR75_dis_admt_other_liabilities_specify_mor_then_6_to_12_mon() != null) {
                        R75cell4.setCellValue(record.getR75_dis_admt_other_liabilities_specify_mor_then_6_to_12_mon().doubleValue());
                        R75cell4.setCellStyle(numberStyle);
                    } else {
                        R75cell4.setCellValue("");
                        R75cell4.setCellStyle(textStyle);
                    }

                    Cell R75cell5 = row.createCell(6);
                    if (record.getR75_dis_admt_other_liabilities_specify_mor_then_12_mon_to_3_year() != null) {
                        R75cell5.setCellValue(record.getR75_dis_admt_other_liabilities_specify_mor_then_12_mon_to_3_year().doubleValue());
                        R75cell5.setCellStyle(numberStyle);
                    } else {
                        R75cell5.setCellValue("");
                        R75cell5.setCellStyle(textStyle);
                    }

                    Cell R75cell6 = row.createCell(7);
                    if (record.getR75_dis_admt_other_liabilities_specify_mor_then_3_to_5_year() != null) {
                        R75cell6.setCellValue(record.getR75_dis_admt_other_liabilities_specify_mor_then_3_to_5_year().doubleValue());
                        R75cell6.setCellStyle(numberStyle);
                    } else {
                        R75cell6.setCellValue("");
                        R75cell6.setCellStyle(textStyle);
                    }

                    Cell R75cell7 = row.createCell(8);
                    if (record.getR75_dis_admt_other_liabilities_specify_mor_then_5_to_10_year() != null) {
                        R75cell7.setCellValue(record.getR75_dis_admt_other_liabilities_specify_mor_then_5_to_10_year().doubleValue());
                        R75cell7.setCellStyle(numberStyle);
                    } else {
                        R75cell7.setCellValue("");
                        R75cell7.setCellStyle(textStyle);
                    }

                    Cell R75cell8 = row.createCell(9);
                    if (record.getR75_dis_admt_other_liabilities_specify_mor_then_10_year() != null) {
                        R75cell8.setCellValue(record.getR75_dis_admt_other_liabilities_specify_mor_then_10_year().doubleValue());
                        R75cell8.setCellStyle(numberStyle);
                    } else {
                        R75cell8.setCellValue("");
                        R75cell8.setCellStyle(textStyle);
                    }



// Row 77
                    row = sheet.getRow(76);
                    Cell R77cell1 = row.createCell(2);
                    if (record.getR77_fix_rate_itm_amt_customer_deposits_up_to_1_mnt() != null) {
                        R77cell1.setCellValue(record.getR77_fix_rate_itm_amt_customer_deposits_up_to_1_mnt().doubleValue());
                        R77cell1.setCellStyle(numberStyle);
                    } else {
                        R77cell1.setCellValue("");
                        R77cell1.setCellStyle(textStyle);
                    }

                    Cell R77cell2 = row.createCell(3);
                    if (record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_1_to_3_mon() != null) {
                        R77cell2.setCellValue(record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_1_to_3_mon().doubleValue());
                        R77cell2.setCellStyle(numberStyle);
                    } else {
                        R77cell2.setCellValue("");
                        R77cell2.setCellStyle(textStyle);
                    }

                    Cell R77cell3 = row.createCell(4);
                    if (record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_3_to_6_mon() != null) {
                        R77cell3.setCellValue(record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_3_to_6_mon().doubleValue());
                        R77cell3.setCellStyle(numberStyle);
                    } else {
                        R77cell3.setCellValue("");
                        R77cell3.setCellStyle(textStyle);
                    }

                    Cell R77cell4 = row.createCell(5);
                    if (record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_6_to_12_mon() != null) {
                        R77cell4.setCellValue(record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_6_to_12_mon().doubleValue());
                        R77cell4.setCellStyle(numberStyle);
                    } else {
                        R77cell4.setCellValue("");
                        R77cell4.setCellStyle(textStyle);
                    }

                    Cell R77cell5 = row.createCell(6);
                    if (record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_12_mon_to_3_year() != null) {
                        R77cell5.setCellValue(record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_12_mon_to_3_year().doubleValue());
                        R77cell5.setCellStyle(numberStyle);
                    } else {
                        R77cell5.setCellValue("");
                        R77cell5.setCellStyle(textStyle);
                    }

                    Cell R77cell6 = row.createCell(7);
                    if (record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_3_to_5_year() != null) {
                        R77cell6.setCellValue(record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_3_to_5_year().doubleValue());
                        R77cell6.setCellStyle(numberStyle);
                    } else {
                        R77cell6.setCellValue("");
                        R77cell6.setCellStyle(textStyle);
                    }

                    Cell R77cell7 = row.createCell(8);
                    if (record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_5_to_10_year() != null) {
                        R77cell7.setCellValue(record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_5_to_10_year().doubleValue());
                        R77cell7.setCellStyle(numberStyle);
                    } else {
                        R77cell7.setCellValue("");
                        R77cell7.setCellStyle(textStyle);
                    }

                    Cell R77cell8 = row.createCell(9);
                    if (record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_10_year() != null) {
                        R77cell8.setCellValue(record.getR77_fix_rate_itm_amt_customer_deposits_mor_then_10_year().doubleValue());
                        R77cell8.setCellStyle(numberStyle);
                    } else {
                        R77cell8.setCellValue("");
                        R77cell8.setCellStyle(textStyle);
                    }


// Row 78
                    row = sheet.getRow(77);
                    Cell R78cell1 = row.createCell(2);
                    if (record.getR78_fix_rate_itm_amt_bnk_of_botswana_up_to_1_mnt() != null) {
                        R78cell1.setCellValue(record.getR78_fix_rate_itm_amt_bnk_of_botswana_up_to_1_mnt().doubleValue());
                        R78cell1.setCellStyle(numberStyle);
                    } else {
                        R78cell1.setCellValue("");
                        R78cell1.setCellStyle(textStyle);
                    }

                    Cell R78cell2 = row.createCell(3);
                    if (record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_1_to_3_mon() != null) {
                        R78cell2.setCellValue(record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_1_to_3_mon().doubleValue());
                        R78cell2.setCellStyle(numberStyle);
                    } else {
                        R78cell2.setCellValue("");
                        R78cell2.setCellStyle(textStyle);
                    }

                    Cell R78cell3 = row.createCell(4);
                    if (record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_3_to_6_mon() != null) {
                        R78cell3.setCellValue(record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_3_to_6_mon().doubleValue());
                        R78cell3.setCellStyle(numberStyle);
                    } else {
                        R78cell3.setCellValue("");
                        R78cell3.setCellStyle(textStyle);
                    }

                    Cell R78cell4 = row.createCell(5);
                    if (record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_6_to_12_mon() != null) {
                        R78cell4.setCellValue(record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_6_to_12_mon().doubleValue());
                        R78cell4.setCellStyle(numberStyle);
                    } else {
                        R78cell4.setCellValue("");
                        R78cell4.setCellStyle(textStyle);
                    }

                    Cell R78cell5 = row.createCell(6);
                    if (record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_12_mon_to_3_year() != null) {
                        R78cell5.setCellValue(record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_12_mon_to_3_year().doubleValue());
                        R78cell5.setCellStyle(numberStyle);
                    } else {
                        R78cell5.setCellValue("");
                        R78cell5.setCellStyle(textStyle);
                    }

                    Cell R78cell6 = row.createCell(7);
                    if (record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_3_to_5_year() != null) {
                        R78cell6.setCellValue(record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_3_to_5_year().doubleValue());
                        R78cell6.setCellStyle(numberStyle);
                    } else {
                        R78cell6.setCellValue("");
                        R78cell6.setCellStyle(textStyle);
                    }

                    Cell R78cell7 = row.createCell(8);
                    if (record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_5_to_10_year() != null) {
                        R78cell7.setCellValue(record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_5_to_10_year().doubleValue());
                        R78cell7.setCellStyle(numberStyle);
                    } else {
                        R78cell7.setCellValue("");
                        R78cell7.setCellStyle(textStyle);
                    }

                    Cell R78cell8 = row.createCell(9);
                    if (record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_10_year() != null) {
                        R78cell8.setCellValue(record.getR78_fix_rate_itm_amt_bnk_of_botswana_mor_then_10_year().doubleValue());
                        R78cell8.setCellStyle(numberStyle);
                    } else {
                        R78cell8.setCellValue("");
                        R78cell8.setCellStyle(textStyle);
                    }


// Row 79
                    row = sheet.getRow(78);
                    Cell R79cell1 = row.createCell(2);
                    if (record.getR79_fix_rate_itm_amt_doms_bnks_up_to_1_mnt() != null) {
                        R79cell1.setCellValue(record.getR79_fix_rate_itm_amt_doms_bnks_up_to_1_mnt().doubleValue());
                        R79cell1.setCellStyle(numberStyle);
                    } else {
                        R79cell1.setCellValue("");
                        R79cell1.setCellStyle(textStyle);
                    }

                    Cell R79cell2 = row.createCell(3);
                    if (record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_1_to_3_mon() != null) {
                        R79cell2.setCellValue(record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_1_to_3_mon().doubleValue());
                        R79cell2.setCellStyle(numberStyle);
                    } else {
                        R79cell2.setCellValue("");
                        R79cell2.setCellStyle(textStyle);
                    }

                    Cell R79cell3 = row.createCell(4);
                    if (record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_3_to_6_mon() != null) {
                        R79cell3.setCellValue(record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_3_to_6_mon().doubleValue());
                        R79cell3.setCellStyle(numberStyle);
                    } else {
                        R79cell3.setCellValue("");
                        R79cell3.setCellStyle(textStyle);
                    }

                    Cell R79cell4 = row.createCell(5);
                    if (record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_6_to_12_mon() != null) {
                        R79cell4.setCellValue(record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_6_to_12_mon().doubleValue());
                        R79cell4.setCellStyle(numberStyle);
                    } else {
                        R79cell4.setCellValue("");
                        R79cell4.setCellStyle(textStyle);
                    }

                    Cell R79cell5 = row.createCell(6);
                    if (record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_12_mon_to_3_year() != null) {
                        R79cell5.setCellValue(record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_12_mon_to_3_year().doubleValue());
                        R79cell5.setCellStyle(numberStyle);
                    } else {
                        R79cell5.setCellValue("");
                        R79cell5.setCellStyle(textStyle);
                    }

                    Cell R79cell6 = row.createCell(7);
                    if (record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_3_to_5_year() != null) {
                        R79cell6.setCellValue(record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_3_to_5_year().doubleValue());
                        R79cell6.setCellStyle(numberStyle);
                    } else {
                        R79cell6.setCellValue("");
                        R79cell6.setCellStyle(textStyle);
                    }

                    Cell R79cell7 = row.createCell(8);
                    if (record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_5_to_10_year() != null) {
                        R79cell7.setCellValue(record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_5_to_10_year().doubleValue());
                        R79cell7.setCellStyle(numberStyle);
                    } else {
                        R79cell7.setCellValue("");
                        R79cell7.setCellStyle(textStyle);
                    }

                    Cell R79cell8 = row.createCell(9);
                    if (record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_10_year() != null) {
                        R79cell8.setCellValue(record.getR79_fix_rate_itm_amt_doms_bnks_mor_then_10_year().doubleValue());
                        R79cell8.setCellStyle(numberStyle);
                    } else {
                        R79cell8.setCellValue("");
                        R79cell8.setCellStyle(textStyle);
                    }



// Row 80
                    row = sheet.getRow(79);
                    Cell R80cell1 = row.createCell(2);
                    if (record.getR80_fix_rate_itm_amt_foreign_bnks_up_to_1_mnt() != null) {
                        R80cell1.setCellValue(record.getR80_fix_rate_itm_amt_foreign_bnks_up_to_1_mnt().doubleValue());
                        R80cell1.setCellStyle(numberStyle);
                    } else {
                        R80cell1.setCellValue("");
                        R80cell1.setCellStyle(textStyle);
                    }

                    Cell R80cell2 = row.createCell(3);
                    if (record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_1_to_3_mon() != null) {
                        R80cell2.setCellValue(record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_1_to_3_mon().doubleValue());
                        R80cell2.setCellStyle(numberStyle);
                    } else {
                        R80cell2.setCellValue("");
                        R80cell2.setCellStyle(textStyle);
                    }

                    Cell R80cell3 = row.createCell(4);
                    if (record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_3_to_6_mon() != null) {
                        R80cell3.setCellValue(record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_3_to_6_mon().doubleValue());
                        R80cell3.setCellStyle(numberStyle);
                    } else {
                        R80cell3.setCellValue("");
                        R80cell3.setCellStyle(textStyle);
                    }

                    Cell R80cell4 = row.createCell(5);
                    if (record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_6_to_12_mon() != null) {
                        R80cell4.setCellValue(record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_6_to_12_mon().doubleValue());
                        R80cell4.setCellStyle(numberStyle);
                    } else {
                        R80cell4.setCellValue("");
                        R80cell4.setCellStyle(textStyle);
                    }

                    Cell R80cell5 = row.createCell(6);
                    if (record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_12_mon_to_3_year() != null) {
                        R80cell5.setCellValue(record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_12_mon_to_3_year().doubleValue());
                        R80cell5.setCellStyle(numberStyle);
                    } else {
                        R80cell5.setCellValue("");
                        R80cell5.setCellStyle(textStyle);
                    }

                    Cell R80cell6 = row.createCell(7);
                    if (record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_3_to_5_year() != null) {
                        R80cell6.setCellValue(record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_3_to_5_year().doubleValue());
                        R80cell6.setCellStyle(numberStyle);
                    } else {
                        R80cell6.setCellValue("");
                        R80cell6.setCellStyle(textStyle);
                    }

                    Cell R80cell7 = row.createCell(8);
                    if (record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_5_to_10_year() != null) {
                        R80cell7.setCellValue(record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_5_to_10_year().doubleValue());
                        R80cell7.setCellStyle(numberStyle);
                    } else {
                        R80cell7.setCellValue("");
                        R80cell7.setCellStyle(textStyle);
                    }

                    Cell R80cell8 = row.createCell(9);
                    if (record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_10_year() != null) {
                        R80cell8.setCellValue(record.getR80_fix_rate_itm_amt_foreign_bnks_mor_then_10_year().doubleValue());
                        R80cell8.setCellStyle(numberStyle);
                    } else {
                        R80cell8.setCellValue("");
                        R80cell8.setCellStyle(textStyle);
                    }


// Row 81
                    row = sheet.getRow(80);
                    Cell R81cell1 = row.createCell(2);
                    if (record.getR81_fix_rate_itm_amt_related_comp_up_to_1_mnt() != null) {
                        R81cell1.setCellValue(record.getR81_fix_rate_itm_amt_related_comp_up_to_1_mnt().doubleValue());
                        R81cell1.setCellStyle(numberStyle);
                    } else {
                        R81cell1.setCellValue("");
                        R81cell1.setCellStyle(textStyle);
                    }

                    Cell R81cell2 = row.createCell(3);
                    if (record.getR81_fix_rate_itm_amt_related_comp_mor_then_1_to_3_mon() != null) {
                        R81cell2.setCellValue(record.getR81_fix_rate_itm_amt_related_comp_mor_then_1_to_3_mon().doubleValue());
                        R81cell2.setCellStyle(numberStyle);
                    } else {
                        R81cell2.setCellValue("");
                        R81cell2.setCellStyle(textStyle);
                    }

                    Cell R81cell3 = row.createCell(4);
                    if (record.getR81_fix_rate_itm_amt_related_comp_mor_then_3_to_6_mon() != null) {
                        R81cell3.setCellValue(record.getR81_fix_rate_itm_amt_related_comp_mor_then_3_to_6_mon().doubleValue());
                        R81cell3.setCellStyle(numberStyle);
                    } else {
                        R81cell3.setCellValue("");
                        R81cell3.setCellStyle(textStyle);
                    }

                    Cell R81cell4 = row.createCell(5);
                    if (record.getR81_fix_rate_itm_amt_related_comp_mor_then_6_to_12_mon() != null) {
                        R81cell4.setCellValue(record.getR81_fix_rate_itm_amt_related_comp_mor_then_6_to_12_mon().doubleValue());
                        R81cell4.setCellStyle(numberStyle);
                    } else {
                        R81cell4.setCellValue("");
                        R81cell4.setCellStyle(textStyle);
                    }

                    Cell R81cell5 = row.createCell(6);
                    if (record.getR81_fix_rate_itm_amt_related_comp_mor_then_12_mon_to_3_year() != null) {
                        R81cell5.setCellValue(record.getR81_fix_rate_itm_amt_related_comp_mor_then_12_mon_to_3_year().doubleValue());
                        R81cell5.setCellStyle(numberStyle);
                    } else {
                        R81cell5.setCellValue("");
                        R81cell5.setCellStyle(textStyle);
                    }

                    Cell R81cell6 = row.createCell(7);
                    if (record.getR81_fix_rate_itm_amt_related_comp_mor_then_3_to_5_year() != null) {
                        R81cell6.setCellValue(record.getR81_fix_rate_itm_amt_related_comp_mor_then_3_to_5_year().doubleValue());
                        R81cell6.setCellStyle(numberStyle);
                    } else {
                        R81cell6.setCellValue("");
                        R81cell6.setCellStyle(textStyle);
                    }

                    Cell R81cell7 = row.createCell(8);
                    if (record.getR81_fix_rate_itm_amt_related_comp_mor_then_5_to_10_year() != null) {
                        R81cell7.setCellValue(record.getR81_fix_rate_itm_amt_related_comp_mor_then_5_to_10_year().doubleValue());
                        R81cell7.setCellStyle(numberStyle);
                    } else {
                        R81cell7.setCellValue("");
                        R81cell7.setCellStyle(textStyle);
                    }

                    Cell R81cell8 = row.createCell(9);
                    if (record.getR81_fix_rate_itm_amt_related_comp_mor_then_10_year() != null) {
                        R81cell8.setCellValue(record.getR81_fix_rate_itm_amt_related_comp_mor_then_10_year().doubleValue());
                        R81cell8.setCellStyle(numberStyle);
                    } else {
                        R81cell8.setCellValue("");
                        R81cell8.setCellStyle(textStyle);
                    }


// Row 82
                    row = sheet.getRow(81);
                    Cell R82cell1 = row.createCell(2);
                    if (record.getR82_fix_rate_itm_borrowed_funds_up_to_1_mnt() != null) {
                        R82cell1.setCellValue(record.getR82_fix_rate_itm_borrowed_funds_up_to_1_mnt().doubleValue());
                        R82cell1.setCellStyle(numberStyle);
                    } else {
                        R82cell1.setCellValue("");
                        R82cell1.setCellStyle(textStyle);
                    }

                    Cell R82cell2 = row.createCell(3);
                    if (record.getR82_fix_rate_itm_borrowed_funds_mor_then_1_to_3_mon() != null) {
                        R82cell2.setCellValue(record.getR82_fix_rate_itm_borrowed_funds_mor_then_1_to_3_mon().doubleValue());
                        R82cell2.setCellStyle(numberStyle);
                    } else {
                        R82cell2.setCellValue("");
                        R82cell2.setCellStyle(textStyle);
                    }

                    Cell R82cell3 = row.createCell(4);
                    if (record.getR82_fix_rate_itm_borrowed_funds_mor_then_3_to_6_mon() != null) {
                        R82cell3.setCellValue(record.getR82_fix_rate_itm_borrowed_funds_mor_then_3_to_6_mon().doubleValue());
                        R82cell3.setCellStyle(numberStyle);
                    } else {
                        R82cell3.setCellValue("");
                        R82cell3.setCellStyle(textStyle);
                    }

                    Cell R82cell4 = row.createCell(5);
                    if (record.getR82_fix_rate_itm_borrowed_funds_mor_then_6_to_12_mon() != null) {
                        R82cell4.setCellValue(record.getR82_fix_rate_itm_borrowed_funds_mor_then_6_to_12_mon().doubleValue());
                        R82cell4.setCellStyle(numberStyle);
                    } else {
                        R82cell4.setCellValue("");
                        R82cell4.setCellStyle(textStyle);
                    }

                    Cell R82cell5 = row.createCell(6);
                    if (record.getR82_fix_rate_itm_borrowed_funds_mor_then_12_mon_to_3_year() != null) {
                        R82cell5.setCellValue(record.getR82_fix_rate_itm_borrowed_funds_mor_then_12_mon_to_3_year().doubleValue());
                        R82cell5.setCellStyle(numberStyle);
                    } else {
                        R82cell5.setCellValue("");
                        R82cell5.setCellStyle(textStyle);
                    }

                    Cell R82cell6 = row.createCell(7);
                    if (record.getR82_fix_rate_itm_borrowed_funds_mor_then_3_to_5_year() != null) {
                        R82cell6.setCellValue(record.getR82_fix_rate_itm_borrowed_funds_mor_then_3_to_5_year().doubleValue());
                        R82cell6.setCellStyle(numberStyle);
                    } else {
                        R82cell6.setCellValue("");
                        R82cell6.setCellStyle(textStyle);
                    }

                    Cell R82cell7 = row.createCell(8);
                    if (record.getR82_fix_rate_itm_borrowed_funds_mor_then_5_to_10_year() != null) {
                        R82cell7.setCellValue(record.getR82_fix_rate_itm_borrowed_funds_mor_then_5_to_10_year().doubleValue());
                        R82cell7.setCellStyle(numberStyle);
                    } else {
                        R82cell7.setCellValue("");
                        R82cell7.setCellStyle(textStyle);
                    }

                    Cell R82cell8 = row.createCell(9);
                    if (record.getR82_fix_rate_itm_borrowed_funds_mor_then_10_year() != null) {
                        R82cell8.setCellValue(record.getR82_fix_rate_itm_borrowed_funds_mor_then_10_year().doubleValue());
                        R82cell8.setCellStyle(numberStyle);
                    } else {
                        R82cell8.setCellValue("");
                        R82cell8.setCellStyle(textStyle);
                    }


// Row 83
                    row = sheet.getRow(82);
                    Cell R83cell1 = row.createCell(2);
                    if (record.getR83_fix_rate_itm_other_liab_specify_up_to_1_mnt() != null) {
                        R83cell1.setCellValue(record.getR83_fix_rate_itm_other_liab_specify_up_to_1_mnt().doubleValue());
                        R83cell1.setCellStyle(numberStyle);
                    } else {
                        R83cell1.setCellValue("");
                        R83cell1.setCellStyle(textStyle);
                    }

                    Cell R83cell2 = row.createCell(3);
                    if (record.getR83_fix_rate_itm_other_liab_specify_mor_then_1_to_3_mon() != null) {
                        R83cell2.setCellValue(record.getR83_fix_rate_itm_other_liab_specify_mor_then_1_to_3_mon().doubleValue());
                        R83cell2.setCellStyle(numberStyle);
                    } else {
                        R83cell2.setCellValue("");
                        R83cell2.setCellStyle(textStyle);
                    }

                    Cell R83cell3 = row.createCell(4);
                    if (record.getR83_fix_rate_itm_other_liab_specify_mor_then_3_to_6_mon() != null) {
                        R83cell3.setCellValue(record.getR83_fix_rate_itm_other_liab_specify_mor_then_3_to_6_mon().doubleValue());
                        R83cell3.setCellStyle(numberStyle);
                    } else {
                        R83cell3.setCellValue("");
                        R83cell3.setCellStyle(textStyle);
                    }

                    Cell R83cell4 = row.createCell(5);
                    if (record.getR83_fix_rate_itm_other_liab_specify_mor_then_6_to_12_mon() != null) {
                        R83cell4.setCellValue(record.getR83_fix_rate_itm_other_liab_specify_mor_then_6_to_12_mon().doubleValue());
                        R83cell4.setCellStyle(numberStyle);
                    } else {
                        R83cell4.setCellValue("");
                        R83cell4.setCellStyle(textStyle);
                    }

                    Cell R83cell5 = row.createCell(6);
                    if (record.getR83_fix_rate_itm_other_liab_specify_mor_then_12_mon_to_3_year() != null) {
                        R83cell5.setCellValue(record.getR83_fix_rate_itm_other_liab_specify_mor_then_12_mon_to_3_year().doubleValue());
                        R83cell5.setCellStyle(numberStyle);
                    } else {
                        R83cell5.setCellValue("");
                        R83cell5.setCellStyle(textStyle);
                    }

                    Cell R83cell6 = row.createCell(7);
                    if (record.getR83_fix_rate_itm_other_liab_specify_mor_then_3_to_5_year() != null) {
                        R83cell6.setCellValue(record.getR83_fix_rate_itm_other_liab_specify_mor_then_3_to_5_year().doubleValue());
                        R83cell6.setCellStyle(numberStyle);
                    } else {
                        R83cell6.setCellValue("");
                        R83cell6.setCellStyle(textStyle);
                    }

                    Cell R83cell7 = row.createCell(8);
                    if (record.getR83_fix_rate_itm_other_liab_specify_mor_then_5_to_10_year() != null) {
                        R83cell7.setCellValue(record.getR83_fix_rate_itm_other_liab_specify_mor_then_5_to_10_year().doubleValue());
                        R83cell7.setCellStyle(numberStyle);
                    } else {
                        R83cell7.setCellValue("");
                        R83cell7.setCellStyle(textStyle);
                    }

                    Cell R83cell8 = row.createCell(9);
                    if (record.getR83_fix_rate_itm_other_liab_specify_mor_then_10_year() != null) {
                        R83cell8.setCellValue(record.getR83_fix_rate_itm_other_liab_specify_mor_then_10_year().doubleValue());
                        R83cell8.setCellStyle(numberStyle);
                    } else {
                        R83cell8.setCellValue("");
                        R83cell8.setCellStyle(textStyle);
                    }


// Row 84
                    row = sheet.getRow(83);
                    Cell R84cell1 = row.createCell(2);
                    if (record.getR84_fix_rate_itm_non_rate_sens_items_up_to_1_mnt() != null) {
                        R84cell1.setCellValue(record.getR84_fix_rate_itm_non_rate_sens_items_up_to_1_mnt().doubleValue());
                        R84cell1.setCellStyle(numberStyle);
                    } else {
                        R84cell1.setCellValue("");
                        R84cell1.setCellStyle(textStyle);
                    }

                    Cell R84cell2 = row.createCell(3);
                    if (record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_1_to_3_mon() != null) {
                        R84cell2.setCellValue(record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_1_to_3_mon().doubleValue());
                        R84cell2.setCellStyle(numberStyle);
                    } else {
                        R84cell2.setCellValue("");
                        R84cell2.setCellStyle(textStyle);
                    }

                    Cell R84cell3 = row.createCell(4);
                    if (record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_3_to_6_mon() != null) {
                        R84cell3.setCellValue(record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_3_to_6_mon().doubleValue());
                        R84cell3.setCellStyle(numberStyle);
                    } else {
                        R84cell3.setCellValue("");
                        R84cell3.setCellStyle(textStyle);
                    }

                    Cell R84cell4 = row.createCell(5);
                    if (record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_6_to_12_mon() != null) {
                        R84cell4.setCellValue(record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_6_to_12_mon().doubleValue());
                        R84cell4.setCellStyle(numberStyle);
                    } else {
                        R84cell4.setCellValue("");
                        R84cell4.setCellStyle(textStyle);
                    }

                    Cell R84cell5 = row.createCell(6);
                    if (record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_12_mon_to_3_year() != null) {
                        R84cell5.setCellValue(record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_12_mon_to_3_year().doubleValue());
                        R84cell5.setCellStyle(numberStyle);
                    } else {
                        R84cell5.setCellValue("");
                        R84cell5.setCellStyle(textStyle);
                    }

                    Cell R84cell6 = row.createCell(7);
                    if (record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_3_to_5_year() != null) {
                        R84cell6.setCellValue(record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_3_to_5_year().doubleValue());
                        R84cell6.setCellStyle(numberStyle);
                    } else {
                        R84cell6.setCellValue("");
                        R84cell6.setCellStyle(textStyle);
                    }

                    Cell R84cell7 = row.createCell(8);
                    if (record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_5_to_10_year() != null) {
                        R84cell7.setCellValue(record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_5_to_10_year().doubleValue());
                        R84cell7.setCellStyle(numberStyle);
                    } else {
                        R84cell7.setCellValue("");
                        R84cell7.setCellStyle(textStyle);
                    }

                    Cell R84cell8 = row.createCell(9);
                    if (record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_10_year() != null) {
                        R84cell8.setCellValue(record.getR84_fix_rate_itm_non_rate_sens_items_mor_then_10_year().doubleValue());
                        R84cell8.setCellStyle(numberStyle);
                    } else {
                        R84cell8.setCellValue("");
                        R84cell8.setCellStyle(textStyle);
                    }

                    Cell R84cell9 = row.createCell(10);
                    if (record.getR84_fix_rate_itm_non_rate_sens_items_non_rat_sens_itm() != null) {
                        R84cell9.setCellValue(record.getR84_fix_rate_itm_non_rate_sens_items_non_rat_sens_itm().doubleValue());
                        R84cell9.setCellStyle(numberStyle);
                    } else {
                        R84cell9.setCellValue("");
                        R84cell9.setCellStyle(textStyle);
                    }


// Row 85
                    row = sheet.getRow(84);

                    Cell R85cell9 = row.createCell(10);
                    if (record.getR85_non_rat_sens_itm_amt_cust_deposits_non_rat_sens_itm() != null) {
                        R85cell9.setCellValue(record.getR85_non_rat_sens_itm_amt_cust_deposits_non_rat_sens_itm().doubleValue());
                        R85cell9.setCellStyle(numberStyle);
                    } else {
                        R85cell9.setCellValue("");
                        R85cell9.setCellStyle(textStyle);
                    }


// Row 86
                    row = sheet.getRow(85);

                    Cell R86cell9 = row.createCell(10);
                    if (record.getR86_non_rat_sens_itm_amt_bnk_of_botswana_non_rat_sens_itm() != null) {
                        R86cell9.setCellValue(record.getR86_non_rat_sens_itm_amt_bnk_of_botswana_non_rat_sens_itm().doubleValue());
                        R86cell9.setCellStyle(numberStyle);
                    } else {
                        R86cell9.setCellValue("");
                        R86cell9.setCellStyle(textStyle);
                    }


// Row 87
                    row = sheet.getRow(86);

                    Cell R87cell9 = row.createCell(10);
                    if (record.getR87_non_rat_sens_itm_amt_doms_bnks_non_rat_sens_itm() != null) {
                        R87cell9.setCellValue(record.getR87_non_rat_sens_itm_amt_doms_bnks_non_rat_sens_itm().doubleValue());
                        R87cell9.setCellStyle(numberStyle);
                    } else {
                        R87cell9.setCellValue("");
                        R87cell9.setCellStyle(textStyle);
                    }


// Row 88
                    row = sheet.getRow(87);

                    Cell R88cell9 = row.createCell(10);
                    if (record.getR88_non_rat_sens_itm_amt_foreign_bnks_non_rat_sens_itm() != null) {
                        R88cell9.setCellValue(record.getR88_non_rat_sens_itm_amt_foreign_bnks_non_rat_sens_itm().doubleValue());
                        R88cell9.setCellStyle(numberStyle);
                    } else {
                        R88cell9.setCellValue("");
                        R88cell9.setCellStyle(textStyle);
                    }


// Row 89
                    row = sheet.getRow(88);

                    Cell R89cell9 = row.createCell(10);
                    if (record.getR89_non_rat_sens_itm_amt_related_comp_non_rat_sens_itm() != null) {
                        R89cell9.setCellValue(record.getR89_non_rat_sens_itm_amt_related_comp_non_rat_sens_itm().doubleValue());
                        R89cell9.setCellStyle(numberStyle);
                    } else {
                        R89cell9.setCellValue("");
                        R89cell9.setCellStyle(textStyle);
                    }


// Row 90
                    row = sheet.getRow(89);

                    Cell R90cell9 = row.createCell(10);
                    if (record.getR90_non_rat_sens_itm_borrowed_funds_non_rat_sens_itm() != null) {
                        R90cell9.setCellValue(record.getR90_non_rat_sens_itm_borrowed_funds_non_rat_sens_itm().doubleValue());
                        R90cell9.setCellStyle(numberStyle);
                    } else {
                        R90cell9.setCellValue("");
                        R90cell9.setCellStyle(textStyle);
                    }


// Row 91
                    row = sheet.getRow(90);

                    Cell R91cell9 = row.createCell(10);
                    if (record.getR91_non_rat_sens_itm_other_liabilities_specify_non_rat_sens_itm() != null) {
                        R91cell9.setCellValue(record.getR91_non_rat_sens_itm_other_liabilities_specify_non_rat_sens_itm().doubleValue());
                        R91cell9.setCellStyle(numberStyle);
                    } else {
                        R91cell9.setCellValue("");
                        R91cell9.setCellStyle(textStyle);
                    }



// Row 96
                    row = sheet.getRow(95);
                    Cell R96cell1 = row.createCell(2);
                    if (record.getR96_dis_man_rate_net_funding_to_frm_trading_up_to_1_mnt() != null) {
                        R96cell1.setCellValue(record.getR96_dis_man_rate_net_funding_to_frm_trading_up_to_1_mnt().doubleValue());
                        R96cell1.setCellStyle(numberStyle);
                    } else {
                        R96cell1.setCellValue("");
                        R96cell1.setCellStyle(textStyle);
                    }

                    Cell R96cell2 = row.createCell(3);
                    if (record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_1_to_3_mon() != null) {
                        R96cell2.setCellValue(record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_1_to_3_mon().doubleValue());
                        R96cell2.setCellStyle(numberStyle);
                    } else {
                        R96cell2.setCellValue("");
                        R96cell2.setCellStyle(textStyle);
                    }

                    Cell R96cell3 = row.createCell(4);
                    if (record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_3_to_6_mon() != null) {
                        R96cell3.setCellValue(record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_3_to_6_mon().doubleValue());
                        R96cell3.setCellStyle(numberStyle);
                    } else {
                        R96cell3.setCellValue("");
                        R96cell3.setCellStyle(textStyle);
                    }

                    Cell R96cell4 = row.createCell(5);
                    if (record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_6_to_12_mon() != null) {
                        R96cell4.setCellValue(record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_6_to_12_mon().doubleValue());
                        R96cell4.setCellStyle(numberStyle);
                    } else {
                        R96cell4.setCellValue("");
                        R96cell4.setCellStyle(textStyle);
                    }

                    Cell R96cell5 = row.createCell(6);
                    if (record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_12_mon_to_3_year() != null) {
                        R96cell5.setCellValue(record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_12_mon_to_3_year().doubleValue());
                        R96cell5.setCellStyle(numberStyle);
                    } else {
                        R96cell5.setCellValue("");
                        R96cell5.setCellStyle(textStyle);
                    }

                    Cell R96cell6 = row.createCell(7);
                    if (record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_3_to_5_year() != null) {
                        R96cell6.setCellValue(record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_3_to_5_year().doubleValue());
                        R96cell6.setCellStyle(numberStyle);
                    } else {
                        R96cell6.setCellValue("");
                        R96cell6.setCellStyle(textStyle);
                    }

                    Cell R96cell7 = row.createCell(8);
                    if (record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_5_to_10_year() != null) {
                        R96cell7.setCellValue(record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_5_to_10_year().doubleValue());
                        R96cell7.setCellStyle(numberStyle);
                    } else {
                        R96cell7.setCellValue("");
                        R96cell7.setCellStyle(textStyle);
                    }

                    Cell R96cell8 = row.createCell(9);
                    if (record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_10_year() != null) {
                        R96cell8.setCellValue(record.getR96_dis_man_rate_net_funding_to_frm_trading_mor_then_10_year().doubleValue());
                        R96cell8.setCellStyle(numberStyle);
                    } else {
                        R96cell8.setCellValue("");
                        R96cell8.setCellStyle(textStyle);
                    }

                    Cell R96cell9 = row.createCell(10);
                    if (record.getR96_dis_man_rate_net_funding_to_frm_trading_non_rat_sens_itm() != null) {
                        R96cell9.setCellValue(record.getR96_dis_man_rate_net_funding_to_frm_trading_non_rat_sens_itm().doubleValue());
                        R96cell9.setCellStyle(numberStyle);
                    } else {
                        R96cell9.setCellValue("");
                        R96cell9.setCellStyle(textStyle);
                    }

// Row 100
                    row = sheet.getRow(99);
                    Cell R100cell1 = row.createCell(2);
                    if (record.getR100_of_which_pay_fix_and_recv_float_up_to_1_mnt() != null) {
                        R100cell1.setCellValue(record.getR100_of_which_pay_fix_and_recv_float_up_to_1_mnt().doubleValue());
                        R100cell1.setCellStyle(numberStyle);
                    } else {
                        R100cell1.setCellValue("");
                        R100cell1.setCellStyle(textStyle);
                    }

                    Cell R100cell2 = row.createCell(3);
                    if (record.getR100_of_which_pay_fix_and_recv_float_mor_then_1_to_3_mon() != null) {
                        R100cell2.setCellValue(record.getR100_of_which_pay_fix_and_recv_float_mor_then_1_to_3_mon().doubleValue());
                        R100cell2.setCellStyle(numberStyle);
                    } else {
                        R100cell2.setCellValue("");
                        R100cell2.setCellStyle(textStyle);
                    }

                    Cell R100cell3 = row.createCell(4);
                    if (record.getR100_of_which_pay_fix_and_recv_float_mor_then_3_to_6_mon() != null) {
                        R100cell3.setCellValue(record.getR100_of_which_pay_fix_and_recv_float_mor_then_3_to_6_mon().doubleValue());
                        R100cell3.setCellStyle(numberStyle);
                    } else {
                        R100cell3.setCellValue("");
                        R100cell3.setCellStyle(textStyle);
                    }

                    Cell R100cell4 = row.createCell(5);
                    if (record.getR100_of_which_pay_fix_and_recv_float_mor_then_6_to_12_mon() != null) {
                        R100cell4.setCellValue(record.getR100_of_which_pay_fix_and_recv_float_mor_then_6_to_12_mon().doubleValue());
                        R100cell4.setCellStyle(numberStyle);
                    } else {
                        R100cell4.setCellValue("");
                        R100cell4.setCellStyle(textStyle);
                    }

                    Cell R100cell5 = row.createCell(6);
                    if (record.getR100_of_which_pay_fix_and_recv_float_mor_then_12_mon_to_3_year() != null) {
                        R100cell5.setCellValue(record.getR100_of_which_pay_fix_and_recv_float_mor_then_12_mon_to_3_year().doubleValue());
                        R100cell5.setCellStyle(numberStyle);
                    } else {
                        R100cell5.setCellValue("");
                        R100cell5.setCellStyle(textStyle);
                    }

                    Cell R100cell6 = row.createCell(7);
                    if (record.getR100_of_which_pay_fix_and_recv_float_mor_then_3_to_5_year() != null) {
                        R100cell6.setCellValue(record.getR100_of_which_pay_fix_and_recv_float_mor_then_3_to_5_year().doubleValue());
                        R100cell6.setCellStyle(numberStyle);
                    } else {
                        R100cell6.setCellValue("");
                        R100cell6.setCellStyle(textStyle);
                    }

                    Cell R100cell7 = row.createCell(8);
                    if (record.getR100_of_which_pay_fix_and_recv_float_mor_then_5_to_10_year() != null) {
                        R100cell7.setCellValue(record.getR100_of_which_pay_fix_and_recv_float_mor_then_5_to_10_year().doubleValue());
                        R100cell7.setCellStyle(numberStyle);
                    } else {
                        R100cell7.setCellValue("");
                        R100cell7.setCellStyle(textStyle);
                    }

                    Cell R100cell8 = row.createCell(9);
                    if (record.getR100_of_which_pay_fix_and_recv_float_mor_then_10_year() != null) {
                        R100cell8.setCellValue(record.getR100_of_which_pay_fix_and_recv_float_mor_then_10_year().doubleValue());
                        R100cell8.setCellStyle(numberStyle);
                    } else {
                        R100cell8.setCellValue("");
                        R100cell8.setCellStyle(textStyle);
                    }

                    Cell R100cell9 = row.createCell(10);
                    if (record.getR100_of_which_pay_fix_and_recv_float_non_rat_sens_itm() != null) {
                        R100cell9.setCellValue(record.getR100_of_which_pay_fix_and_recv_float_non_rat_sens_itm().doubleValue());
                        R100cell9.setCellStyle(numberStyle);
                    } else {
                        R100cell9.setCellValue("");
                        R100cell9.setCellStyle(textStyle);
                    }


// Row 101
                    row = sheet.getRow(100);
                    Cell R101cell1 = row.createCell(2);
                    if (record.getR101_of_which_recv_fix_and_pay_float_up_to_1_mnt() != null) {
                        R101cell1.setCellValue(record.getR101_of_which_recv_fix_and_pay_float_up_to_1_mnt().doubleValue());
                        R101cell1.setCellStyle(numberStyle);
                    } else {
                        R101cell1.setCellValue("");
                        R101cell1.setCellStyle(textStyle);
                    }

                    Cell R101cell2 = row.createCell(3);
                    if (record.getR101_of_which_recv_fix_and_pay_float_mor_then_1_to_3_mon() != null) {
                        R101cell2.setCellValue(record.getR101_of_which_recv_fix_and_pay_float_mor_then_1_to_3_mon().doubleValue());
                        R101cell2.setCellStyle(numberStyle);
                    } else {
                        R101cell2.setCellValue("");
                        R101cell2.setCellStyle(textStyle);
                    }

                    Cell R101cell3 = row.createCell(4);
                    if (record.getR101_of_which_recv_fix_and_pay_float_mor_then_3_to_6_mon() != null) {
                        R101cell3.setCellValue(record.getR101_of_which_recv_fix_and_pay_float_mor_then_3_to_6_mon().doubleValue());
                        R101cell3.setCellStyle(numberStyle);
                    } else {
                        R101cell3.setCellValue("");
                        R101cell3.setCellStyle(textStyle);
                    }

                    Cell R101cell4 = row.createCell(5);
                    if (record.getR101_of_which_recv_fix_and_pay_float_mor_then_6_to_12_mon() != null) {
                        R101cell4.setCellValue(record.getR101_of_which_recv_fix_and_pay_float_mor_then_6_to_12_mon().doubleValue());
                        R101cell4.setCellStyle(numberStyle);
                    } else {
                        R101cell4.setCellValue("");
                        R101cell4.setCellStyle(textStyle);
                    }

                    Cell R101cell5 = row.createCell(6);
                    if (record.getR101_of_which_recv_fix_and_pay_float_mor_then_12_mon_to_3_year() != null) {
                        R101cell5.setCellValue(record.getR101_of_which_recv_fix_and_pay_float_mor_then_12_mon_to_3_year().doubleValue());
                        R101cell5.setCellStyle(numberStyle);
                    } else {
                        R101cell5.setCellValue("");
                        R101cell5.setCellStyle(textStyle);
                    }

                    Cell R101cell6 = row.createCell(7);
                    if (record.getR101_of_which_recv_fix_and_pay_float_mor_then_3_to_5_year() != null) {
                        R101cell6.setCellValue(record.getR101_of_which_recv_fix_and_pay_float_mor_then_3_to_5_year().doubleValue());
                        R101cell6.setCellStyle(numberStyle);
                    } else {
                        R101cell6.setCellValue("");
                        R101cell6.setCellStyle(textStyle);
                    }

                    Cell R101cell7 = row.createCell(8);
                    if (record.getR101_of_which_recv_fix_and_pay_float_mor_then_5_to_10_year() != null) {
                        R101cell7.setCellValue(record.getR101_of_which_recv_fix_and_pay_float_mor_then_5_to_10_year().doubleValue());
                        R101cell7.setCellStyle(numberStyle);
                    } else {
                        R101cell7.setCellValue("");
                        R101cell7.setCellStyle(textStyle);
                    }

                    Cell R101cell8 = row.createCell(9);
                    if (record.getR101_of_which_recv_fix_and_pay_float_mor_then_10_year() != null) {
                        R101cell8.setCellValue(record.getR101_of_which_recv_fix_and_pay_float_mor_then_10_year().doubleValue());
                        R101cell8.setCellStyle(numberStyle);
                    } else {
                        R101cell8.setCellValue("");
                        R101cell8.setCellStyle(textStyle);
                    }

                    Cell R101cell9 = row.createCell(10);
                    if (record.getR101_of_which_recv_fix_and_pay_float_non_rat_sens_itm() != null) {
                        R101cell9.setCellValue(record.getR101_of_which_recv_fix_and_pay_float_non_rat_sens_itm().doubleValue());
                        R101cell9.setCellStyle(numberStyle);
                    } else {
                        R101cell9.setCellValue("");
                        R101cell9.setCellStyle(textStyle);
                    }


// Row 102
                    row = sheet.getRow(101);
                    Cell R102cell1 = row.createCell(2);
                    if (record.getR102_fras_and_futures_up_to_1_mnt() != null) {
                        R102cell1.setCellValue(record.getR102_fras_and_futures_up_to_1_mnt().doubleValue());
                        R102cell1.setCellStyle(numberStyle);
                    } else {
                        R102cell1.setCellValue("");
                        R102cell1.setCellStyle(textStyle);
                    }

                    Cell R102cell2 = row.createCell(3);
                    if (record.getR102_fras_and_futures_mor_then_1_to_3_mon() != null) {
                        R102cell2.setCellValue(record.getR102_fras_and_futures_mor_then_1_to_3_mon().doubleValue());
                        R102cell2.setCellStyle(numberStyle);
                    } else {
                        R102cell2.setCellValue("");
                        R102cell2.setCellStyle(textStyle);
                    }

                    Cell R102cell3 = row.createCell(4);
                    if (record.getR102_fras_and_futures_mor_then_3_to_6_mon() != null) {
                        R102cell3.setCellValue(record.getR102_fras_and_futures_mor_then_3_to_6_mon().doubleValue());
                        R102cell3.setCellStyle(numberStyle);
                    } else {
                        R102cell3.setCellValue("");
                        R102cell3.setCellStyle(textStyle);
                    }

                    Cell R102cell4 = row.createCell(5);
                    if (record.getR102_fras_and_futures_mor_then_6_to_12_mon() != null) {
                        R102cell4.setCellValue(record.getR102_fras_and_futures_mor_then_6_to_12_mon().doubleValue());
                        R102cell4.setCellStyle(numberStyle);
                    } else {
                        R102cell4.setCellValue("");
                        R102cell4.setCellStyle(textStyle);
                    }

                    Cell R102cell5 = row.createCell(6);
                    if (record.getR102_fras_and_futures_mor_then_12_mon_to_3_year() != null) {
                        R102cell5.setCellValue(record.getR102_fras_and_futures_mor_then_12_mon_to_3_year().doubleValue());
                        R102cell5.setCellStyle(numberStyle);
                    } else {
                        R102cell5.setCellValue("");
                        R102cell5.setCellStyle(textStyle);
                    }

                    Cell R102cell6 = row.createCell(7);
                    if (record.getR102_fras_and_futures_mor_then_3_to_5_year() != null) {
                        R102cell6.setCellValue(record.getR102_fras_and_futures_mor_then_3_to_5_year().doubleValue());
                        R102cell6.setCellStyle(numberStyle);
                    } else {
                        R102cell6.setCellValue("");
                        R102cell6.setCellStyle(textStyle);
                    }

                    Cell R102cell7 = row.createCell(8);
                    if (record.getR102_fras_and_futures_mor_then_5_to_10_year() != null) {
                        R102cell7.setCellValue(record.getR102_fras_and_futures_mor_then_5_to_10_year().doubleValue());
                        R102cell7.setCellStyle(numberStyle);
                    } else {
                        R102cell7.setCellValue("");
                        R102cell7.setCellStyle(textStyle);
                    }

                    Cell R102cell8 = row.createCell(9);
                    if (record.getR102_fras_and_futures_mor_then_10_year() != null) {
                        R102cell8.setCellValue(record.getR102_fras_and_futures_mor_then_10_year().doubleValue());
                        R102cell8.setCellStyle(numberStyle);
                    } else {
                        R102cell8.setCellValue("");
                        R102cell8.setCellStyle(textStyle);
                    }

                    Cell R102cell9 = row.createCell(10);
                    if (record.getR102_fras_and_futures_non_rat_sens_itm() != null) {
                        R102cell9.setCellValue(record.getR102_fras_and_futures_non_rat_sens_itm().doubleValue());
                        R102cell9.setCellStyle(numberStyle);
                    } else {
                        R102cell9.setCellValue("");
                        R102cell9.setCellStyle(textStyle);
                    }


// Row 103
                    row = sheet.getRow(102);
                    Cell R103cell1 = row.createCell(2);
                    if (record.getR103_options_up_to_1_mnt() != null) {
                        R103cell1.setCellValue(record.getR103_options_up_to_1_mnt().doubleValue());
                        R103cell1.setCellStyle(numberStyle);
                    } else {
                        R103cell1.setCellValue("");
                        R103cell1.setCellStyle(textStyle);
                    }

                    Cell R103cell2 = row.createCell(3);
                    if (record.getR103_options_mor_then_1_to_3_mon() != null) {
                        R103cell2.setCellValue(record.getR103_options_mor_then_1_to_3_mon().doubleValue());
                        R103cell2.setCellStyle(numberStyle);
                    } else {
                        R103cell2.setCellValue("");
                        R103cell2.setCellStyle(textStyle);
                    }

                    Cell R103cell3 = row.createCell(4);
                    if (record.getR103_options_mor_then_3_to_6_mon() != null) {
                        R103cell3.setCellValue(record.getR103_options_mor_then_3_to_6_mon().doubleValue());
                        R103cell3.setCellStyle(numberStyle);
                    } else {
                        R103cell3.setCellValue("");
                        R103cell3.setCellStyle(textStyle);
                    }

                    Cell R103cell4 = row.createCell(5);
                    if (record.getR103_options_mor_then_6_to_12_mon() != null) {
                        R103cell4.setCellValue(record.getR103_options_mor_then_6_to_12_mon().doubleValue());
                        R103cell4.setCellStyle(numberStyle);
                    } else {
                        R103cell4.setCellValue("");
                        R103cell4.setCellStyle(textStyle);
                    }

                    Cell R103cell5 = row.createCell(6);
                    if (record.getR103_options_mor_then_12_mon_to_3_year() != null) {
                        R103cell5.setCellValue(record.getR103_options_mor_then_12_mon_to_3_year().doubleValue());
                        R103cell5.setCellStyle(numberStyle);
                    } else {
                        R103cell5.setCellValue("");
                        R103cell5.setCellStyle(textStyle);
                    }

                    Cell R103cell6 = row.createCell(7);
                    if (record.getR103_options_mor_then_3_to_5_year() != null) {
                        R103cell6.setCellValue(record.getR103_options_mor_then_3_to_5_year().doubleValue());
                        R103cell6.setCellStyle(numberStyle);
                    } else {
                        R103cell6.setCellValue("");
                        R103cell6.setCellStyle(textStyle);
                    }

                    Cell R103cell7 = row.createCell(8);
                    if (record.getR103_options_mor_then_5_to_10_year() != null) {
                        R103cell7.setCellValue(record.getR103_options_mor_then_5_to_10_year().doubleValue());
                        R103cell7.setCellStyle(numberStyle);
                    } else {
                        R103cell7.setCellValue("");
                        R103cell7.setCellStyle(textStyle);
                    }

                    Cell R103cell8 = row.createCell(9);
                    if (record.getR103_options_mor_then_10_year() != null) {
                        R103cell8.setCellValue(record.getR103_options_mor_then_10_year().doubleValue());
                        R103cell8.setCellStyle(numberStyle);
                    } else {
                        R103cell8.setCellValue("");
                        R103cell8.setCellStyle(textStyle);
                    }

                    Cell R103cell9 = row.createCell(10);
                    if (record.getR103_options_non_rat_sens_itm() != null) {
                        R103cell9.setCellValue(record.getR103_options_non_rat_sens_itm().doubleValue());
                        R103cell9.setCellStyle(numberStyle);
                    } else {
                        R103cell9.setCellValue("");
                        R103cell9.setCellStyle(textStyle);
                    }


// Row 104
                    row = sheet.getRow(103);
                    Cell R104cell1 = row.createCell(2);
                    if (record.getR104_other_specify_up_to_1_mnt() != null) {
                        R104cell1.setCellValue(record.getR104_other_specify_up_to_1_mnt().doubleValue());
                        R104cell1.setCellStyle(numberStyle);
                    } else {
                        R104cell1.setCellValue("");
                        R104cell1.setCellStyle(textStyle);
                    }

                    Cell R104cell2 = row.createCell(3);
                    if (record.getR104_other_specify_mor_then_1_to_3_mon() != null) {
                        R104cell2.setCellValue(record.getR104_other_specify_mor_then_1_to_3_mon().doubleValue());
                        R104cell2.setCellStyle(numberStyle);
                    } else {
                        R104cell2.setCellValue("");
                        R104cell2.setCellStyle(textStyle);
                    }

                    Cell R104cell3 = row.createCell(4);
                    if (record.getR104_other_specify_mor_then_3_to_6_mon() != null) {
                        R104cell3.setCellValue(record.getR104_other_specify_mor_then_3_to_6_mon().doubleValue());
                        R104cell3.setCellStyle(numberStyle);
                    } else {
                        R104cell3.setCellValue("");
                        R104cell3.setCellStyle(textStyle);
                    }

                    Cell R104cell4 = row.createCell(5);
                    if (record.getR104_other_specify_mor_then_6_to_12_mon() != null) {
                        R104cell4.setCellValue(record.getR104_other_specify_mor_then_6_to_12_mon().doubleValue());
                        R104cell4.setCellStyle(numberStyle);
                    } else {
                        R104cell4.setCellValue("");
                        R104cell4.setCellStyle(textStyle);
                    }

                    Cell R104cell5 = row.createCell(6);
                    if (record.getR104_other_specify_mor_then_12_mon_to_3_year() != null) {
                        R104cell5.setCellValue(record.getR104_other_specify_mor_then_12_mon_to_3_year().doubleValue());
                        R104cell5.setCellStyle(numberStyle);
                    } else {
                        R104cell5.setCellValue("");
                        R104cell5.setCellStyle(textStyle);
                    }

                    Cell R104cell6 = row.createCell(7);
                    if (record.getR104_other_specify_mor_then_3_to_5_year() != null) {
                        R104cell6.setCellValue(record.getR104_other_specify_mor_then_3_to_5_year().doubleValue());
                        R104cell6.setCellStyle(numberStyle);
                    } else {
                        R104cell6.setCellValue("");
                        R104cell6.setCellStyle(textStyle);
                    }

                    Cell R104cell7 = row.createCell(8);
                    if (record.getR104_other_specify_mor_then_5_to_10_year() != null) {
                        R104cell7.setCellValue(record.getR104_other_specify_mor_then_5_to_10_year().doubleValue());
                        R104cell7.setCellStyle(numberStyle);
                    } else {
                        R104cell7.setCellValue("");
                        R104cell7.setCellStyle(textStyle);
                    }

                    Cell R104cell8 = row.createCell(9);
                    if (record.getR104_other_specify_mor_then_10_year() != null) {
                        R104cell8.setCellValue(record.getR104_other_specify_mor_then_10_year().doubleValue());
                        R104cell8.setCellStyle(numberStyle);
                    } else {
                        R104cell8.setCellValue("");
                        R104cell8.setCellStyle(textStyle);
                    }

                    Cell R104cell9 = row.createCell(10);
                    if (record.getR104_other_specify_non_rat_sens_itm() != null) {
                        R104cell9.setCellValue(record.getR104_other_specify_non_rat_sens_itm().doubleValue());
                        R104cell9.setCellStyle(numberStyle);
                    } else {
                        R104cell9.setCellValue("");
                        R104cell9.setCellStyle(textStyle);
                    }



    }

    private void populateEntity1Data(Sheet sheet, M_IRB_Summary_Entity1 record, CellStyle textStyle, CellStyle numberStyle) {

        Row row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
//        Row row;
        // Row 12
//        row = sheet.getRow(11);
        Cell R12cell1 = row.createCell(2);
        if (record.getR12_assets_cash_up_to_1_mnt() != null) {
            R12cell1.setCellValue(record.getR12_assets_cash_up_to_1_mnt().doubleValue());
            R12cell1.setCellStyle(numberStyle);
        } else {
            R12cell1.setCellValue("");
            R12cell1.setCellStyle(textStyle);
        }

        Cell R12cell2 = row.createCell(3);
        if (record.getR12_assets_cash_mor_then_1_to_3_mon() != null) {
            R12cell2.setCellValue(record.getR12_assets_cash_mor_then_1_to_3_mon().doubleValue());
            R12cell2.setCellStyle(numberStyle);
        } else {
            R12cell2.setCellValue("");
            R12cell2.setCellStyle(textStyle);
        }

        Cell R12cell3 = row.createCell(4);
        if (record.getR12_assets_cash_mor_then_3_to_6_mon() != null) {
            R12cell3.setCellValue(record.getR12_assets_cash_mor_then_3_to_6_mon().doubleValue());
            R12cell3.setCellStyle(numberStyle);
        } else {
            R12cell3.setCellValue("");
            R12cell3.setCellStyle(textStyle);
        }

        Cell R12cell4 = row.createCell(5);
        if (record.getR12_assets_cash_mor_then_6_to_12_mon() != null) {
            R12cell4.setCellValue(record.getR12_assets_cash_mor_then_6_to_12_mon().doubleValue());
            R12cell4.setCellStyle(numberStyle);
        } else {
            R12cell4.setCellValue("");
            R12cell4.setCellStyle(textStyle);
        }

        Cell R12cell5 = row.createCell(6);
        if (record.getR12_assets_cash_mor_then_12_mon_to_3_year() != null) {
            R12cell5.setCellValue(record.getR12_assets_cash_mor_then_12_mon_to_3_year().doubleValue());
            R12cell5.setCellStyle(numberStyle);
        } else {
            R12cell5.setCellValue("");
            R12cell5.setCellStyle(textStyle);
        }

        Cell R12cell6 = row.createCell(7);
        if (record.getR12_assets_cash_mor_then_3_to_5_year() != null) {
            R12cell6.setCellValue(record.getR12_assets_cash_mor_then_3_to_5_year().doubleValue());
            R12cell6.setCellStyle(numberStyle);
        } else {
            R12cell6.setCellValue("");
            R12cell6.setCellStyle(textStyle);
        }

        Cell R12cell7 = row.createCell(8);
        if (record.getR12_assets_cash_mor_then_5_to_10_year() != null) {
            R12cell7.setCellValue(record.getR12_assets_cash_mor_then_5_to_10_year().doubleValue());
            R12cell7.setCellStyle(numberStyle);
        } else {
            R12cell7.setCellValue("");
            R12cell7.setCellStyle(textStyle);
        }

        Cell R12cell8 = row.createCell(9);
        if (record.getR12_assets_cash_mor_then_10_year() != null) {
            R12cell8.setCellValue(record.getR12_assets_cash_mor_then_10_year().doubleValue());
            R12cell8.setCellStyle(numberStyle);
        } else {
            R12cell8.setCellValue("");
            R12cell8.setCellStyle(textStyle);
        }



// Row 13
        row = sheet.getRow(12);
        Cell R13cell1 = row.createCell(2);
        if (record.getR13_bal_bnk_of_botswana_up_to_1_mnt() != null) {
            R13cell1.setCellValue(record.getR13_bal_bnk_of_botswana_up_to_1_mnt().doubleValue());
            R13cell1.setCellStyle(numberStyle);
        } else {
            R13cell1.setCellValue("");
            R13cell1.setCellStyle(textStyle);
        }

        Cell R13cell2 = row.createCell(3);
        if (record.getR13_bal_bnk_of_botswana_mor_then_1_to_3_mon() != null) {
            R13cell2.setCellValue(record.getR13_bal_bnk_of_botswana_mor_then_1_to_3_mon().doubleValue());
            R13cell2.setCellStyle(numberStyle);
        } else {
            R13cell2.setCellValue("");
            R13cell2.setCellStyle(textStyle);
        }

        Cell R13cell3 = row.createCell(4);
        if (record.getR13_bal_bnk_of_botswana_mor_then_3_to_6_mon() != null) {
            R13cell3.setCellValue(record.getR13_bal_bnk_of_botswana_mor_then_3_to_6_mon().doubleValue());
            R13cell3.setCellStyle(numberStyle);
        } else {
            R13cell3.setCellValue("");
            R13cell3.setCellStyle(textStyle);
        }

        Cell R13cell4 = row.createCell(5);
        if (record.getR13_bal_bnk_of_botswana_mor_then_6_to_12_mon() != null) {
            R13cell4.setCellValue(record.getR13_bal_bnk_of_botswana_mor_then_6_to_12_mon().doubleValue());
            R13cell4.setCellStyle(numberStyle);
        } else {
            R13cell4.setCellValue("");
            R13cell4.setCellStyle(textStyle);
        }

        Cell R13cell5 = row.createCell(6);
        if (record.getR13_bal_bnk_of_botswana_mor_then_12_mon_to_3_year() != null) {
            R13cell5.setCellValue(record.getR13_bal_bnk_of_botswana_mor_then_12_mon_to_3_year().doubleValue());
            R13cell5.setCellStyle(numberStyle);
        } else {
            R13cell5.setCellValue("");
            R13cell5.setCellStyle(textStyle);
        }

        Cell R13cell6 = row.createCell(7);
        if (record.getR13_bal_bnk_of_botswana_mor_then_3_to_5_year() != null) {
            R13cell6.setCellValue(record.getR13_bal_bnk_of_botswana_mor_then_3_to_5_year().doubleValue());
            R13cell6.setCellStyle(numberStyle);
        } else {
            R13cell6.setCellValue("");
            R13cell6.setCellStyle(textStyle);
        }

        Cell R13cell7 = row.createCell(8);
        if (record.getR13_bal_bnk_of_botswana_mor_then_5_to_10_year() != null) {
            R13cell7.setCellValue(record.getR13_bal_bnk_of_botswana_mor_then_5_to_10_year().doubleValue());
            R13cell7.setCellStyle(numberStyle);
        } else {
            R13cell7.setCellValue("");
            R13cell7.setCellStyle(textStyle);
        }

        Cell R13cell8 = row.createCell(9);
        if (record.getR13_bal_bnk_of_botswana_mor_then_10_year() != null) {
            R13cell8.setCellValue(record.getR13_bal_bnk_of_botswana_mor_then_10_year().doubleValue());
            R13cell8.setCellStyle(numberStyle);
        } else {
            R13cell8.setCellValue("");
            R13cell8.setCellStyle(textStyle);
        }


// Row 14
        row = sheet.getRow(13);
        Cell R14cell1 = row.createCell(2);
        if (record.getR14_bal_doms_bnks_up_to_1_mnt() != null) {
            R14cell1.setCellValue(record.getR14_bal_doms_bnks_up_to_1_mnt().doubleValue());
            R14cell1.setCellStyle(numberStyle);
        } else {
            R14cell1.setCellValue("");
            R14cell1.setCellStyle(textStyle);
        }

        Cell R14cell2 = row.createCell(3);
        if (record.getR14_bal_doms_bnks_mor_then_1_to_3_mon() != null) {
            R14cell2.setCellValue(record.getR14_bal_doms_bnks_mor_then_1_to_3_mon().doubleValue());
            R14cell2.setCellStyle(numberStyle);
        } else {
            R14cell2.setCellValue("");
            R14cell2.setCellStyle(textStyle);
        }

        Cell R14cell3 = row.createCell(4);
        if (record.getR14_bal_doms_bnks_mor_then_3_to_6_mon() != null) {
            R14cell3.setCellValue(record.getR14_bal_doms_bnks_mor_then_3_to_6_mon().doubleValue());
            R14cell3.setCellStyle(numberStyle);
        } else {
            R14cell3.setCellValue("");
            R14cell3.setCellStyle(textStyle);
        }

        Cell R14cell4 = row.createCell(5);
        if (record.getR14_bal_doms_bnks_mor_then_6_to_12_mon() != null) {
            R14cell4.setCellValue(record.getR14_bal_doms_bnks_mor_then_6_to_12_mon().doubleValue());
            R14cell4.setCellStyle(numberStyle);
        } else {
            R14cell4.setCellValue("");
            R14cell4.setCellStyle(textStyle);
        }

        Cell R14cell5 = row.createCell(6);
        if (record.getR14_bal_doms_bnks_mor_then_12_mon_to_3_year() != null) {
            R14cell5.setCellValue(record.getR14_bal_doms_bnks_mor_then_12_mon_to_3_year().doubleValue());
            R14cell5.setCellStyle(numberStyle);
        } else {
            R14cell5.setCellValue("");
            R14cell5.setCellStyle(textStyle);
        }

        Cell R14cell6 = row.createCell(7);
        if (record.getR14_bal_doms_bnks_mor_then_3_to_5_year() != null) {
            R14cell6.setCellValue(record.getR14_bal_doms_bnks_mor_then_3_to_5_year().doubleValue());
            R14cell6.setCellStyle(numberStyle);
        } else {
            R14cell6.setCellValue("");
            R14cell6.setCellStyle(textStyle);
        }

        Cell R14cell7 = row.createCell(8);
        if (record.getR14_bal_doms_bnks_mor_then_5_to_10_year() != null) {
            R14cell7.setCellValue(record.getR14_bal_doms_bnks_mor_then_5_to_10_year().doubleValue());
            R14cell7.setCellStyle(numberStyle);
        } else {
            R14cell7.setCellValue("");
            R14cell7.setCellStyle(textStyle);
        }

        Cell R14cell8 = row.createCell(9);
        if (record.getR14_bal_doms_bnks_mor_then_10_year() != null) {
            R14cell8.setCellValue(record.getR14_bal_doms_bnks_mor_then_10_year().doubleValue());
            R14cell8.setCellStyle(numberStyle);
        } else {
            R14cell8.setCellValue("");
            R14cell8.setCellStyle(textStyle);
        }

// Row 15
        row = sheet.getRow(14);
        Cell R15cell1 = row.createCell(2);
        if (record.getR15_bal_foreign_bnks_up_to_1_mnt() != null) {
            R15cell1.setCellValue(record.getR15_bal_foreign_bnks_up_to_1_mnt().doubleValue());
            R15cell1.setCellStyle(numberStyle);
        } else {
            R15cell1.setCellValue("");
            R15cell1.setCellStyle(textStyle);
        }

        Cell R15cell2 = row.createCell(3);
        if (record.getR15_bal_foreign_bnks_mor_then_1_to_3_mon() != null) {
            R15cell2.setCellValue(record.getR15_bal_foreign_bnks_mor_then_1_to_3_mon().doubleValue());
            R15cell2.setCellStyle(numberStyle);
        } else {
            R15cell2.setCellValue("");
            R15cell2.setCellStyle(textStyle);
        }

        Cell R15cell3 = row.createCell(4);
        if (record.getR15_bal_foreign_bnks_mor_then_3_to_6_mon() != null) {
            R15cell3.setCellValue(record.getR15_bal_foreign_bnks_mor_then_3_to_6_mon().doubleValue());
            R15cell3.setCellStyle(numberStyle);
        } else {
            R15cell3.setCellValue("");
            R15cell3.setCellStyle(textStyle);
        }

        Cell R15cell4 = row.createCell(5);
        if (record.getR15_bal_foreign_bnks_mor_then_6_to_12_mon() != null) {
            R15cell4.setCellValue(record.getR15_bal_foreign_bnks_mor_then_6_to_12_mon().doubleValue());
            R15cell4.setCellStyle(numberStyle);
        } else {
            R15cell4.setCellValue("");
            R15cell4.setCellStyle(textStyle);
        }

        Cell R15cell5 = row.createCell(6);
        if (record.getR15_bal_foreign_bnks_mor_then_12_mon_to_3_year() != null) {
            R15cell5.setCellValue(record.getR15_bal_foreign_bnks_mor_then_12_mon_to_3_year().doubleValue());
            R15cell5.setCellStyle(numberStyle);
        } else {
            R15cell5.setCellValue("");
            R15cell5.setCellStyle(textStyle);
        }

        Cell R15cell6 = row.createCell(7);
        if (record.getR15_bal_foreign_bnks_mor_then_3_to_5_year() != null) {
            R15cell6.setCellValue(record.getR15_bal_foreign_bnks_mor_then_3_to_5_year().doubleValue());
            R15cell6.setCellStyle(numberStyle);
        } else {
            R15cell6.setCellValue("");
            R15cell6.setCellStyle(textStyle);
        }

        Cell R15cell7 = row.createCell(8);
        if (record.getR15_bal_foreign_bnks_mor_then_5_to_10_year() != null) {
            R15cell7.setCellValue(record.getR15_bal_foreign_bnks_mor_then_5_to_10_year().doubleValue());
            R15cell7.setCellStyle(numberStyle);
        } else {
            R15cell7.setCellValue("");
            R15cell7.setCellStyle(textStyle);
        }

        Cell R15cell8 = row.createCell(9);
        if (record.getR15_bal_foreign_bnks_mor_then_10_year() != null) {
            R15cell8.setCellValue(record.getR15_bal_foreign_bnks_mor_then_10_year().doubleValue());
            R15cell8.setCellStyle(numberStyle);
        } else {
            R15cell8.setCellValue("");
            R15cell8.setCellStyle(textStyle);
        }


// Row 16
        row = sheet.getRow(15);
        Cell R16cell1 = row.createCell(2);
        if (record.getR16_bal_related_comp_up_to_1_mnt() != null) {
            R16cell1.setCellValue(record.getR16_bal_related_comp_up_to_1_mnt().doubleValue());
            R16cell1.setCellStyle(numberStyle);
        } else {
            R16cell1.setCellValue("");
            R16cell1.setCellStyle(textStyle);
        }

        Cell R16cell2 = row.createCell(3);
        if (record.getR16_bal_related_comp_mor_then_1_to_3_mon() != null) {
            R16cell2.setCellValue(record.getR16_bal_related_comp_mor_then_1_to_3_mon().doubleValue());
            R16cell2.setCellStyle(numberStyle);
        } else {
            R16cell2.setCellValue("");
            R16cell2.setCellStyle(textStyle);
        }

        Cell R16cell3 = row.createCell(4);
        if (record.getR16_bal_related_comp_mor_then_3_to_6_mon() != null) {
            R16cell3.setCellValue(record.getR16_bal_related_comp_mor_then_3_to_6_mon().doubleValue());
            R16cell3.setCellStyle(numberStyle);
        } else {
            R16cell3.setCellValue("");
            R16cell3.setCellStyle(textStyle);
        }

        Cell R16cell4 = row.createCell(5);
        if (record.getR16_bal_related_comp_mor_then_6_to_12_mon() != null) {
            R16cell4.setCellValue(record.getR16_bal_related_comp_mor_then_6_to_12_mon().doubleValue());
            R16cell4.setCellStyle(numberStyle);
        } else {
            R16cell4.setCellValue("");
            R16cell4.setCellStyle(textStyle);
        }

        Cell R16cell5 = row.createCell(6);
        if (record.getR16_bal_related_comp_mor_then_12_mon_to_3_year() != null) {
            R16cell5.setCellValue(record.getR16_bal_related_comp_mor_then_12_mon_to_3_year().doubleValue());
            R16cell5.setCellStyle(numberStyle);
        } else {
            R16cell5.setCellValue("");
            R16cell5.setCellStyle(textStyle);
        }

        Cell R16cell6 = row.createCell(7);
        if (record.getR16_bal_related_comp_mor_then_3_to_5_year() != null) {
            R16cell6.setCellValue(record.getR16_bal_related_comp_mor_then_3_to_5_year().doubleValue());
            R16cell6.setCellStyle(numberStyle);
        } else {
            R16cell6.setCellValue("");
            R16cell6.setCellStyle(textStyle);
        }

        Cell R16cell7 = row.createCell(8);
        if (record.getR16_bal_related_comp_mor_then_5_to_10_year() != null) {
            R16cell7.setCellValue(record.getR16_bal_related_comp_mor_then_5_to_10_year().doubleValue());
            R16cell7.setCellStyle(numberStyle);
        } else {
            R16cell7.setCellValue("");
            R16cell7.setCellStyle(textStyle);
        }

        Cell R16cell8 = row.createCell(9);
        if (record.getR16_bal_related_comp_mor_then_10_year() != null) {
            R16cell8.setCellValue(record.getR16_bal_related_comp_mor_then_10_year().doubleValue());
            R16cell8.setCellStyle(numberStyle);
        } else {
            R16cell8.setCellValue("");
            R16cell8.setCellStyle(textStyle);
        }


// Row 17
        row = sheet.getRow(16);
        Cell R17cell1 = row.createCell(2);
        if (record.getR17_bnk_of_botswana_cert_up_to_1_mnt() != null) {
            R17cell1.setCellValue(record.getR17_bnk_of_botswana_cert_up_to_1_mnt().doubleValue());
            R17cell1.setCellStyle(numberStyle);
        } else {
            R17cell1.setCellValue("");
            R17cell1.setCellStyle(textStyle);
        }

        Cell R17cell2 = row.createCell(3);
        if (record.getR17_bnk_of_botswana_cert_mor_then_1_to_3_mon() != null) {
            R17cell2.setCellValue(record.getR17_bnk_of_botswana_cert_mor_then_1_to_3_mon().doubleValue());
            R17cell2.setCellStyle(numberStyle);
        } else {
            R17cell2.setCellValue("");
            R17cell2.setCellStyle(textStyle);
        }

        Cell R17cell3 = row.createCell(4);
        if (record.getR17_bnk_of_botswana_cert_mor_then_3_to_6_mon() != null) {
            R17cell3.setCellValue(record.getR17_bnk_of_botswana_cert_mor_then_3_to_6_mon().doubleValue());
            R17cell3.setCellStyle(numberStyle);
        } else {
            R17cell3.setCellValue("");
            R17cell3.setCellStyle(textStyle);
        }

        Cell R17cell4 = row.createCell(5);
        if (record.getR17_bnk_of_botswana_cert_mor_then_6_to_12_mon() != null) {
            R17cell4.setCellValue(record.getR17_bnk_of_botswana_cert_mor_then_6_to_12_mon().doubleValue());
            R17cell4.setCellStyle(numberStyle);
        } else {
            R17cell4.setCellValue("");
            R17cell4.setCellStyle(textStyle);
        }

        Cell R17cell5 = row.createCell(6);
        if (record.getR17_bnk_of_botswana_cert_mor_then_12_mon_to_3_year() != null) {
            R17cell5.setCellValue(record.getR17_bnk_of_botswana_cert_mor_then_12_mon_to_3_year().doubleValue());
            R17cell5.setCellStyle(numberStyle);
        } else {
            R17cell5.setCellValue("");
            R17cell5.setCellStyle(textStyle);
        }

        Cell R17cell6 = row.createCell(7);
        if (record.getR17_bnk_of_botswana_cert_mor_then_3_to_5_year() != null) {
            R17cell6.setCellValue(record.getR17_bnk_of_botswana_cert_mor_then_3_to_5_year().doubleValue());
            R17cell6.setCellStyle(numberStyle);
        } else {
            R17cell6.setCellValue("");
            R17cell6.setCellStyle(textStyle);
        }

        Cell R17cell7 = row.createCell(8);
        if (record.getR17_bnk_of_botswana_cert_mor_then_5_to_10_year() != null) {
            R17cell7.setCellValue(record.getR17_bnk_of_botswana_cert_mor_then_5_to_10_year().doubleValue());
            R17cell7.setCellStyle(numberStyle);
        } else {
            R17cell7.setCellValue("");
            R17cell7.setCellStyle(textStyle);
        }

        Cell R17cell8 = row.createCell(9);
        if (record.getR17_bnk_of_botswana_cert_mor_then_10_year() != null) {
            R17cell8.setCellValue(record.getR17_bnk_of_botswana_cert_mor_then_10_year().doubleValue());
            R17cell8.setCellStyle(numberStyle);
        } else {
            R17cell8.setCellValue("");
            R17cell8.setCellStyle(textStyle);
        }


// Row 18
        row = sheet.getRow(17);
        Cell R18cell1 = row.createCell(2);
        if (record.getR18_gov_bonds_up_to_1_mnt() != null) {
            R18cell1.setCellValue(record.getR18_gov_bonds_up_to_1_mnt().doubleValue());
            R18cell1.setCellStyle(numberStyle);
        } else {
            R18cell1.setCellValue("");
            R18cell1.setCellStyle(textStyle);
        }

        Cell R18cell2 = row.createCell(3);
        if (record.getR18_gov_bonds_mor_then_1_to_3_mon() != null) {
            R18cell2.setCellValue(record.getR18_gov_bonds_mor_then_1_to_3_mon().doubleValue());
            R18cell2.setCellStyle(numberStyle);
        } else {
            R18cell2.setCellValue("");
            R18cell2.setCellStyle(textStyle);
        }

        Cell R18cell3 = row.createCell(4);
        if (record.getR18_gov_bonds_mor_then_3_to_6_mon() != null) {
            R18cell3.setCellValue(record.getR18_gov_bonds_mor_then_3_to_6_mon().doubleValue());
            R18cell3.setCellStyle(numberStyle);
        } else {
            R18cell3.setCellValue("");
            R18cell3.setCellStyle(textStyle);
        }

        Cell R18cell4 = row.createCell(5);
        if (record.getR18_gov_bonds_mor_then_6_to_12_mon() != null) {
            R18cell4.setCellValue(record.getR18_gov_bonds_mor_then_6_to_12_mon().doubleValue());
            R18cell4.setCellStyle(numberStyle);
        } else {
            R18cell4.setCellValue("");
            R18cell4.setCellStyle(textStyle);
        }

        Cell R18cell5 = row.createCell(6);
        if (record.getR18_gov_bonds_mor_then_12_mon_to_3_year() != null) {
            R18cell5.setCellValue(record.getR18_gov_bonds_mor_then_12_mon_to_3_year().doubleValue());
            R18cell5.setCellStyle(numberStyle);
        } else {
            R18cell5.setCellValue("");
            R18cell5.setCellStyle(textStyle);
        }

        Cell R18cell6 = row.createCell(7);
        if (record.getR18_gov_bonds_mor_then_3_to_5_year() != null) {
            R18cell6.setCellValue(record.getR18_gov_bonds_mor_then_3_to_5_year().doubleValue());
            R18cell6.setCellStyle(numberStyle);
        } else {
            R18cell6.setCellValue("");
            R18cell6.setCellStyle(textStyle);
        }

        Cell R18cell7 = row.createCell(8);
        if (record.getR18_gov_bonds_mor_then_5_to_10_year() != null) {
            R18cell7.setCellValue(record.getR18_gov_bonds_mor_then_5_to_10_year().doubleValue());
            R18cell7.setCellStyle(numberStyle);
        } else {
            R18cell7.setCellValue("");
            R18cell7.setCellStyle(textStyle);
        }

        Cell R18cell8 = row.createCell(9);
        if (record.getR18_gov_bonds_mor_then_10_year() != null) {
            R18cell8.setCellValue(record.getR18_gov_bonds_mor_then_10_year().doubleValue());
            R18cell8.setCellStyle(numberStyle);
        } else {
            R18cell8.setCellValue("");
            R18cell8.setCellStyle(textStyle);
        }


// Row 19
        row = sheet.getRow(18);
        Cell R19cell1 = row.createCell(2);
        if (record.getR19_other_invt_specify_up_to_1_mnt() != null) {
            R19cell1.setCellValue(record.getR19_other_invt_specify_up_to_1_mnt().doubleValue());
            R19cell1.setCellStyle(numberStyle);
        } else {
            R19cell1.setCellValue("");
            R19cell1.setCellStyle(textStyle);
        }

        Cell R19cell2 = row.createCell(3);
        if (record.getR19_other_invt_specify_mor_then_1_to_3_mon() != null) {
            R19cell2.setCellValue(record.getR19_other_invt_specify_mor_then_1_to_3_mon().doubleValue());
            R19cell2.setCellStyle(numberStyle);
        } else {
            R19cell2.setCellValue("");
            R19cell2.setCellStyle(textStyle);
        }

        Cell R19cell3 = row.createCell(4);
        if (record.getR19_other_invt_specify_mor_then_3_to_6_mon() != null) {
            R19cell3.setCellValue(record.getR19_other_invt_specify_mor_then_3_to_6_mon().doubleValue());
            R19cell3.setCellStyle(numberStyle);
        } else {
            R19cell3.setCellValue("");
            R19cell3.setCellStyle(textStyle);
        }

        Cell R19cell4 = row.createCell(5);
        if (record.getR19_other_invt_specify_mor_then_6_to_12_mon() != null) {
            R19cell4.setCellValue(record.getR19_other_invt_specify_mor_then_6_to_12_mon().doubleValue());
            R19cell4.setCellStyle(numberStyle);
        } else {
            R19cell4.setCellValue("");
            R19cell4.setCellStyle(textStyle);
        }

        Cell R19cell5 = row.createCell(6);
        if (record.getR19_other_invt_specify_mor_then_12_mon_to_3_year() != null) {
            R19cell5.setCellValue(record.getR19_other_invt_specify_mor_then_12_mon_to_3_year().doubleValue());
            R19cell5.setCellStyle(numberStyle);
        } else {
            R19cell5.setCellValue("");
            R19cell5.setCellStyle(textStyle);
        }

        Cell R19cell6 = row.createCell(7);
        if (record.getR19_other_invt_specify_mor_then_3_to_5_year() != null) {
            R19cell6.setCellValue(record.getR19_other_invt_specify_mor_then_3_to_5_year().doubleValue());
            R19cell6.setCellStyle(numberStyle);
        } else {
            R19cell6.setCellValue("");
            R19cell6.setCellStyle(textStyle);
        }

        Cell R19cell7 = row.createCell(8);
        if (record.getR19_other_invt_specify_mor_then_5_to_10_year() != null) {
            R19cell7.setCellValue(record.getR19_other_invt_specify_mor_then_5_to_10_year().doubleValue());
            R19cell7.setCellStyle(numberStyle);
        } else {
            R19cell7.setCellValue("");
            R19cell7.setCellStyle(textStyle);
        }

        Cell R19cell8 = row.createCell(9);
        if (record.getR19_other_invt_specify_mor_then_10_year() != null) {
            R19cell8.setCellValue(record.getR19_other_invt_specify_mor_then_10_year().doubleValue());
            R19cell8.setCellStyle(numberStyle);
        } else {
            R19cell8.setCellValue("");
            R19cell8.setCellStyle(textStyle);
        }


// Row 20
        row = sheet.getRow(19);
        Cell R20cell1 = row.createCell(2);
        if (record.getR20_loans_and_adv_to_cust_up_to_1_mnt() != null) {
            R20cell1.setCellValue(record.getR20_loans_and_adv_to_cust_up_to_1_mnt().doubleValue());
            R20cell1.setCellStyle(numberStyle);
        } else {
            R20cell1.setCellValue("");
            R20cell1.setCellStyle(textStyle);
        }

        Cell R20cell2 = row.createCell(3);
        if (record.getR20_loans_and_adv_to_cust_mor_then_1_to_3_mon() != null) {
            R20cell2.setCellValue(record.getR20_loans_and_adv_to_cust_mor_then_1_to_3_mon().doubleValue());
            R20cell2.setCellStyle(numberStyle);
        } else {
            R20cell2.setCellValue("");
            R20cell2.setCellStyle(textStyle);
        }

        Cell R20cell3 = row.createCell(4);
        if (record.getR20_loans_and_adv_to_cust_mor_then_3_to_6_mon() != null) {
            R20cell3.setCellValue(record.getR20_loans_and_adv_to_cust_mor_then_3_to_6_mon().doubleValue());
            R20cell3.setCellStyle(numberStyle);
        } else {
            R20cell3.setCellValue("");
            R20cell3.setCellStyle(textStyle);
        }

        Cell R20cell4 = row.createCell(5);
        if (record.getR20_loans_and_adv_to_cust_mor_then_6_to_12_mon() != null) {
            R20cell4.setCellValue(record.getR20_loans_and_adv_to_cust_mor_then_6_to_12_mon().doubleValue());
            R20cell4.setCellStyle(numberStyle);
        } else {
            R20cell4.setCellValue("");
            R20cell4.setCellStyle(textStyle);
        }

        Cell R20cell5 = row.createCell(6);
        if (record.getR20_loans_and_adv_to_cust_mor_then_12_mon_to_3_year() != null) {
            R20cell5.setCellValue(record.getR20_loans_and_adv_to_cust_mor_then_12_mon_to_3_year().doubleValue());
            R20cell5.setCellStyle(numberStyle);
        } else {
            R20cell5.setCellValue("");
            R20cell5.setCellStyle(textStyle);
        }

        Cell R20cell6 = row.createCell(7);
        if (record.getR20_loans_and_adv_to_cust_mor_then_3_to_5_year() != null) {
            R20cell6.setCellValue(record.getR20_loans_and_adv_to_cust_mor_then_3_to_5_year().doubleValue());
            R20cell6.setCellStyle(numberStyle);
        } else {
            R20cell6.setCellValue("");
            R20cell6.setCellStyle(textStyle);
        }

        Cell R20cell7 = row.createCell(8);
        if (record.getR20_loans_and_adv_to_cust_mor_then_5_to_10_year() != null) {
            R20cell7.setCellValue(record.getR20_loans_and_adv_to_cust_mor_then_5_to_10_year().doubleValue());
            R20cell7.setCellStyle(numberStyle);
        } else {
            R20cell7.setCellValue("");
            R20cell7.setCellStyle(textStyle);
        }

        Cell R20cell8 = row.createCell(9);
        if (record.getR20_loans_and_adv_to_cust_mor_then_10_year() != null) {
            R20cell8.setCellValue(record.getR20_loans_and_adv_to_cust_mor_then_10_year().doubleValue());
            R20cell8.setCellStyle(numberStyle);
        } else {
            R20cell8.setCellValue("");
            R20cell8.setCellStyle(textStyle);
        }


// Row 21
        row = sheet.getRow(20);
        Cell R21cell1 = row.createCell(2);
        if (record.getR21_prop_and_eqp_up_to_1_mnt() != null) {
            R21cell1.setCellValue(record.getR21_prop_and_eqp_up_to_1_mnt().doubleValue());
            R21cell1.setCellStyle(numberStyle);
        } else {
            R21cell1.setCellValue("");
            R21cell1.setCellStyle(textStyle);
        }

        Cell R21cell2 = row.createCell(3);
        if (record.getR21_prop_and_eqp_mor_then_1_to_3_mon() != null) {
            R21cell2.setCellValue(record.getR21_prop_and_eqp_mor_then_1_to_3_mon().doubleValue());
            R21cell2.setCellStyle(numberStyle);
        } else {
            R21cell2.setCellValue("");
            R21cell2.setCellStyle(textStyle);
        }

        Cell R21cell3 = row.createCell(4);
        if (record.getR21_prop_and_eqp_mor_then_3_to_6_mon() != null) {
            R21cell3.setCellValue(record.getR21_prop_and_eqp_mor_then_3_to_6_mon().doubleValue());
            R21cell3.setCellStyle(numberStyle);
        } else {
            R21cell3.setCellValue("");
            R21cell3.setCellStyle(textStyle);
        }

        Cell R21cell4 = row.createCell(5);
        if (record.getR21_prop_and_eqp_mor_then_6_to_12_mon() != null) {
            R21cell4.setCellValue(record.getR21_prop_and_eqp_mor_then_6_to_12_mon().doubleValue());
            R21cell4.setCellStyle(numberStyle);
        } else {
            R21cell4.setCellValue("");
            R21cell4.setCellStyle(textStyle);
        }

        Cell R21cell5 = row.createCell(6);
        if (record.getR21_prop_and_eqp_mor_then_12_mon_to_3_year() != null) {
            R21cell5.setCellValue(record.getR21_prop_and_eqp_mor_then_12_mon_to_3_year().doubleValue());
            R21cell5.setCellStyle(numberStyle);
        } else {
            R21cell5.setCellValue("");
            R21cell5.setCellStyle(textStyle);
        }

        Cell R21cell6 = row.createCell(7);
        if (record.getR21_prop_and_eqp_mor_then_3_to_5_year() != null) {
            R21cell6.setCellValue(record.getR21_prop_and_eqp_mor_then_3_to_5_year().doubleValue());
            R21cell6.setCellStyle(numberStyle);
        } else {
            R21cell6.setCellValue("");
            R21cell6.setCellStyle(textStyle);
        }

        Cell R21cell7 = row.createCell(8);
        if (record.getR21_prop_and_eqp_mor_then_5_to_10_year() != null) {
            R21cell7.setCellValue(record.getR21_prop_and_eqp_mor_then_5_to_10_year().doubleValue());
            R21cell7.setCellStyle(numberStyle);
        } else {
            R21cell7.setCellValue("");
            R21cell7.setCellStyle(textStyle);
        }

        Cell R21cell8 = row.createCell(9);
        if (record.getR21_prop_and_eqp_mor_then_10_year() != null) {
            R21cell8.setCellValue(record.getR21_prop_and_eqp_mor_then_10_year().doubleValue());
            R21cell8.setCellStyle(numberStyle);
        } else {
            R21cell8.setCellValue("");
            R21cell8.setCellStyle(textStyle);
        }


// Row 22
        row = sheet.getRow(21);
        Cell R22cell1 = row.createCell(2);
        if (record.getR22_other_assets_specify_up_to_1_mnt() != null) {
            R22cell1.setCellValue(record.getR22_other_assets_specify_up_to_1_mnt().doubleValue());
            R22cell1.setCellStyle(numberStyle);
        } else {
            R22cell1.setCellValue("");
            R22cell1.setCellStyle(textStyle);
        }

        Cell R22cell2 = row.createCell(3);
        if (record.getR22_other_assets_specify_mor_then_1_to_3_mon() != null) {
            R22cell2.setCellValue(record.getR22_other_assets_specify_mor_then_1_to_3_mon().doubleValue());
            R22cell2.setCellStyle(numberStyle);
        } else {
            R22cell2.setCellValue("");
            R22cell2.setCellStyle(textStyle);
        }

        Cell R22cell3 = row.createCell(4);
        if (record.getR22_other_assets_specify_mor_then_3_to_6_mon() != null) {
            R22cell3.setCellValue(record.getR22_other_assets_specify_mor_then_3_to_6_mon().doubleValue());
            R22cell3.setCellStyle(numberStyle);
        } else {
            R22cell3.setCellValue("");
            R22cell3.setCellStyle(textStyle);
        }

        Cell R22cell4 = row.createCell(5);
        if (record.getR22_other_assets_specify_mor_then_6_to_12_mon() != null) {
            R22cell4.setCellValue(record.getR22_other_assets_specify_mor_then_6_to_12_mon().doubleValue());
            R22cell4.setCellStyle(numberStyle);
        } else {
            R22cell4.setCellValue("");
            R22cell4.setCellStyle(textStyle);
        }

        Cell R22cell5 = row.createCell(6);
        if (record.getR22_other_assets_specify_mor_then_12_mon_to_3_year() != null) {
            R22cell5.setCellValue(record.getR22_other_assets_specify_mor_then_12_mon_to_3_year().doubleValue());
            R22cell5.setCellStyle(numberStyle);
        } else {
            R22cell5.setCellValue("");
            R22cell5.setCellStyle(textStyle);
        }

        Cell R22cell6 = row.createCell(7);
        if (record.getR22_other_assets_specify_mor_then_3_to_5_year() != null) {
            R22cell6.setCellValue(record.getR22_other_assets_specify_mor_then_3_to_5_year().doubleValue());
            R22cell6.setCellStyle(numberStyle);
        } else {
            R22cell6.setCellValue("");
            R22cell6.setCellStyle(textStyle);
        }

        Cell R22cell7 = row.createCell(8);
        if (record.getR22_other_assets_specify_mor_then_5_to_10_year() != null) {
            R22cell7.setCellValue(record.getR22_other_assets_specify_mor_then_5_to_10_year().doubleValue());
            R22cell7.setCellStyle(numberStyle);
        } else {
            R22cell7.setCellValue("");
            R22cell7.setCellStyle(textStyle);
        }

        Cell R22cell8 = row.createCell(9);
        if (record.getR22_other_assets_specify_mor_then_10_year() != null) {
            R22cell8.setCellValue(record.getR22_other_assets_specify_mor_then_10_year().doubleValue());
            R22cell8.setCellStyle(numberStyle);
        } else {
            R22cell8.setCellValue("");
            R22cell8.setCellStyle(textStyle);
        }


// Row 24
        row = sheet.getRow(23);
        Cell R24cell1 = row.createCell(2);
        if (record.getR24_dis_admt_cash_up_to_1_mnt() != null) {
            R24cell1.setCellValue(record.getR24_dis_admt_cash_up_to_1_mnt().doubleValue());
            R24cell1.setCellStyle(numberStyle);
        } else {
            R24cell1.setCellValue("");
            R24cell1.setCellStyle(textStyle);
        }

        Cell R24cell2 = row.createCell(3);
        if (record.getR24_dis_admt_cash_mor_then_1_to_3_mon() != null) {
            R24cell2.setCellValue(record.getR24_dis_admt_cash_mor_then_1_to_3_mon().doubleValue());
            R24cell2.setCellStyle(numberStyle);
        } else {
            R24cell2.setCellValue("");
            R24cell2.setCellStyle(textStyle);
        }

        Cell R24cell3 = row.createCell(4);
        if (record.getR24_dis_admt_cash_mor_then_3_to_6_mon() != null) {
            R24cell3.setCellValue(record.getR24_dis_admt_cash_mor_then_3_to_6_mon().doubleValue());
            R24cell3.setCellStyle(numberStyle);
        } else {
            R24cell3.setCellValue("");
            R24cell3.setCellStyle(textStyle);
        }

        Cell R24cell4 = row.createCell(5);
        if (record.getR24_dis_admt_cash_mor_then_6_to_12_mon() != null) {
            R24cell4.setCellValue(record.getR24_dis_admt_cash_mor_then_6_to_12_mon().doubleValue());
            R24cell4.setCellStyle(numberStyle);
        } else {
            R24cell4.setCellValue("");
            R24cell4.setCellStyle(textStyle);
        }

        Cell R24cell5 = row.createCell(6);
        if (record.getR24_dis_admt_cash_mor_then_12_mon_to_3_year() != null) {
            R24cell5.setCellValue(record.getR24_dis_admt_cash_mor_then_12_mon_to_3_year().doubleValue());
            R24cell5.setCellStyle(numberStyle);
        } else {
            R24cell5.setCellValue("");
            R24cell5.setCellStyle(textStyle);
        }

        Cell R24cell6 = row.createCell(7);
        if (record.getR24_dis_admt_cash_mor_then_3_to_5_year() != null) {
            R24cell6.setCellValue(record.getR24_dis_admt_cash_mor_then_3_to_5_year().doubleValue());
            R24cell6.setCellStyle(numberStyle);
        } else {
            R24cell6.setCellValue("");
            R24cell6.setCellStyle(textStyle);
        }

        Cell R24cell7 = row.createCell(8);
        if (record.getR24_dis_admt_cash_mor_then_5_to_10_year() != null) {
            R24cell7.setCellValue(record.getR24_dis_admt_cash_mor_then_5_to_10_year().doubleValue());
            R24cell7.setCellStyle(numberStyle);
        } else {
            R24cell7.setCellValue("");
            R24cell7.setCellStyle(textStyle);
        }

        Cell R24cell8 = row.createCell(9);
        if (record.getR24_dis_admt_cash_mor_then_10_year() != null) {
            R24cell8.setCellValue(record.getR24_dis_admt_cash_mor_then_10_year().doubleValue());
            R24cell8.setCellStyle(numberStyle);
        } else {
            R24cell8.setCellValue("");
            R24cell8.setCellStyle(textStyle);
        }


// Row 25
        row = sheet.getRow(24);
        Cell R25cell1 = row.createCell(2);
        if (record.getR25_dis_admt_bal_bnk_of_botswana_up_to_1_mnt() != null) {
            R25cell1.setCellValue(record.getR25_dis_admt_bal_bnk_of_botswana_up_to_1_mnt().doubleValue());
            R25cell1.setCellStyle(numberStyle);
        } else {
            R25cell1.setCellValue("");
            R25cell1.setCellStyle(textStyle);
        }

        Cell R25cell2 = row.createCell(3);
        if (record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_1_to_3_mon() != null) {
            R25cell2.setCellValue(record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_1_to_3_mon().doubleValue());
            R25cell2.setCellStyle(numberStyle);
        } else {
            R25cell2.setCellValue("");
            R25cell2.setCellStyle(textStyle);
        }

        Cell R25cell3 = row.createCell(4);
        if (record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_6_mon() != null) {
            R25cell3.setCellValue(record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_6_mon().doubleValue());
            R25cell3.setCellStyle(numberStyle);
        } else {
            R25cell3.setCellValue("");
            R25cell3.setCellStyle(textStyle);
        }

        Cell R25cell4 = row.createCell(5);
        if (record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_6_to_12_mon() != null) {
            R25cell4.setCellValue(record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_6_to_12_mon().doubleValue());
            R25cell4.setCellStyle(numberStyle);
        } else {
            R25cell4.setCellValue("");
            R25cell4.setCellStyle(textStyle);
        }

        Cell R25cell5 = row.createCell(6);
        if (record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_12_mon_to_3_year() != null) {
            R25cell5.setCellValue(record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_12_mon_to_3_year().doubleValue());
            R25cell5.setCellStyle(numberStyle);
        } else {
            R25cell5.setCellValue("");
            R25cell5.setCellStyle(textStyle);
        }

        Cell R25cell6 = row.createCell(7);
        if (record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_5_year() != null) {
            R25cell6.setCellValue(record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_3_to_5_year().doubleValue());
            R25cell6.setCellStyle(numberStyle);
        } else {
            R25cell6.setCellValue("");
            R25cell6.setCellStyle(textStyle);
        }

        Cell R25cell7 = row.createCell(8);
        if (record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_5_to_10_year() != null) {
            R25cell7.setCellValue(record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_5_to_10_year().doubleValue());
            R25cell7.setCellStyle(numberStyle);
        } else {
            R25cell7.setCellValue("");
            R25cell7.setCellStyle(textStyle);
        }

        Cell R25cell8 = row.createCell(9);
        if (record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_10_year() != null) {
            R25cell8.setCellValue(record.getR25_dis_admt_bal_bnk_of_botswana_mor_then_10_year().doubleValue());
            R25cell8.setCellStyle(numberStyle);
        } else {
            R25cell8.setCellValue("");
            R25cell8.setCellStyle(textStyle);
        }


// Row 26
        row = sheet.getRow(25);
        Cell R26cell1 = row.createCell(2);
        if (record.getR26_dis_admt_bal_doms_bnks_up_to_1_mnt() != null) {
            R26cell1.setCellValue(record.getR26_dis_admt_bal_doms_bnks_up_to_1_mnt().doubleValue());
            R26cell1.setCellStyle(numberStyle);
        } else {
            R26cell1.setCellValue("");
            R26cell1.setCellStyle(textStyle);
        }

        Cell R26cell2 = row.createCell(3);
        if (record.getR26_dis_admt_bal_doms_bnks_mor_then_1_to_3_mon() != null) {
            R26cell2.setCellValue(record.getR26_dis_admt_bal_doms_bnks_mor_then_1_to_3_mon().doubleValue());
            R26cell2.setCellStyle(numberStyle);
        } else {
            R26cell2.setCellValue("");
            R26cell2.setCellStyle(textStyle);
        }

        Cell R26cell3 = row.createCell(4);
        if (record.getR26_dis_admt_bal_doms_bnks_mor_then_3_to_6_mon() != null) {
            R26cell3.setCellValue(record.getR26_dis_admt_bal_doms_bnks_mor_then_3_to_6_mon().doubleValue());
            R26cell3.setCellStyle(numberStyle);
        } else {
            R26cell3.setCellValue("");
            R26cell3.setCellStyle(textStyle);
        }

        Cell R26cell4 = row.createCell(5);
        if (record.getR26_dis_admt_bal_doms_bnks_mor_then_6_to_12_mon() != null) {
            R26cell4.setCellValue(record.getR26_dis_admt_bal_doms_bnks_mor_then_6_to_12_mon().doubleValue());
            R26cell4.setCellStyle(numberStyle);
        } else {
            R26cell4.setCellValue("");
            R26cell4.setCellStyle(textStyle);
        }

        Cell R26cell5 = row.createCell(6);
        if (record.getR26_dis_admt_bal_doms_bnks_mor_then_12_mon_to_3_year() != null) {
            R26cell5.setCellValue(record.getR26_dis_admt_bal_doms_bnks_mor_then_12_mon_to_3_year().doubleValue());
            R26cell5.setCellStyle(numberStyle);
        } else {
            R26cell5.setCellValue("");
            R26cell5.setCellStyle(textStyle);
        }

        Cell R26cell6 = row.createCell(7);
        if (record.getR26_dis_admt_bal_doms_bnks_mor_then_3_to_5_year() != null) {
            R26cell6.setCellValue(record.getR26_dis_admt_bal_doms_bnks_mor_then_3_to_5_year().doubleValue());
            R26cell6.setCellStyle(numberStyle);
        } else {
            R26cell6.setCellValue("");
            R26cell6.setCellStyle(textStyle);
        }

        Cell R26cell7 = row.createCell(8);
        if (record.getR26_dis_admt_bal_doms_bnks_mor_then_5_to_10_year() != null) {
            R26cell7.setCellValue(record.getR26_dis_admt_bal_doms_bnks_mor_then_5_to_10_year().doubleValue());
            R26cell7.setCellStyle(numberStyle);
        } else {
            R26cell7.setCellValue("");
            R26cell7.setCellStyle(textStyle);
        }

        Cell R26cell8 = row.createCell(9);
        if (record.getR26_dis_admt_bal_doms_bnks_mor_then_10_year() != null) {
            R26cell8.setCellValue(record.getR26_dis_admt_bal_doms_bnks_mor_then_10_year().doubleValue());
            R26cell8.setCellStyle(numberStyle);
        } else {
            R26cell8.setCellValue("");
            R26cell8.setCellStyle(textStyle);
        }

// Row 27
        row = sheet.getRow(26);
        Cell R27cell1 = row.createCell(2);
        if (record.getR27_dis_admt_bal_foreign_bnks_up_to_1_mnt() != null) {
            R27cell1.setCellValue(record.getR27_dis_admt_bal_foreign_bnks_up_to_1_mnt().doubleValue());
            R27cell1.setCellStyle(numberStyle);
        } else {
            R27cell1.setCellValue("");
            R27cell1.setCellStyle(textStyle);
        }

        Cell R27cell2 = row.createCell(3);
        if (record.getR27_dis_admt_bal_foreign_bnks_mor_then_1_to_3_mon() != null) {
            R27cell2.setCellValue(record.getR27_dis_admt_bal_foreign_bnks_mor_then_1_to_3_mon().doubleValue());
            R27cell2.setCellStyle(numberStyle);
        } else {
            R27cell2.setCellValue("");
            R27cell2.setCellStyle(textStyle);
        }

        Cell R27cell3 = row.createCell(4);
        if (record.getR27_dis_admt_bal_foreign_bnks_mor_then_3_to_6_mon() != null) {
            R27cell3.setCellValue(record.getR27_dis_admt_bal_foreign_bnks_mor_then_3_to_6_mon().doubleValue());
            R27cell3.setCellStyle(numberStyle);
        } else {
            R27cell3.setCellValue("");
            R27cell3.setCellStyle(textStyle);
        }

        Cell R27cell4 = row.createCell(5);
        if (record.getR27_dis_admt_bal_foreign_bnks_mor_then_6_to_12_mon() != null) {
            R27cell4.setCellValue(record.getR27_dis_admt_bal_foreign_bnks_mor_then_6_to_12_mon().doubleValue());
            R27cell4.setCellStyle(numberStyle);
        } else {
            R27cell4.setCellValue("");
            R27cell4.setCellStyle(textStyle);
        }

        Cell R27cell5 = row.createCell(6);
        if (record.getR27_dis_admt_bal_foreign_bnks_mor_then_12_mon_to_3_year() != null) {
            R27cell5.setCellValue(record.getR27_dis_admt_bal_foreign_bnks_mor_then_12_mon_to_3_year().doubleValue());
            R27cell5.setCellStyle(numberStyle);
        } else {
            R27cell5.setCellValue("");
            R27cell5.setCellStyle(textStyle);
        }

        Cell R27cell6 = row.createCell(7);
        if (record.getR27_dis_admt_bal_foreign_bnks_mor_then_3_to_5_year() != null) {
            R27cell6.setCellValue(record.getR27_dis_admt_bal_foreign_bnks_mor_then_3_to_5_year().doubleValue());
            R27cell6.setCellStyle(numberStyle);
        } else {
            R27cell6.setCellValue("");
            R27cell6.setCellStyle(textStyle);
        }

        Cell R27cell7 = row.createCell(8);
        if (record.getR27_dis_admt_bal_foreign_bnks_mor_then_5_to_10_year() != null) {
            R27cell7.setCellValue(record.getR27_dis_admt_bal_foreign_bnks_mor_then_5_to_10_year().doubleValue());
            R27cell7.setCellStyle(numberStyle);
        } else {
            R27cell7.setCellValue("");
            R27cell7.setCellStyle(textStyle);
        }

        Cell R27cell8 = row.createCell(9);
        if (record.getR27_dis_admt_bal_foreign_bnks_mor_then_10_year() != null) {
            R27cell8.setCellValue(record.getR27_dis_admt_bal_foreign_bnks_mor_then_10_year().doubleValue());
            R27cell8.setCellStyle(numberStyle);
        } else {
            R27cell8.setCellValue("");
            R27cell8.setCellStyle(textStyle);
        }
// Row 28
        row = sheet.getRow(27);
        Cell R28cell1 = row.createCell(2);
        if (record.getR28_dis_admt_bal_related_comp_up_to_1_mnt() != null) {
            R28cell1.setCellValue(record.getR28_dis_admt_bal_related_comp_up_to_1_mnt().doubleValue());
            R28cell1.setCellStyle(numberStyle);
        } else {
            R28cell1.setCellValue("");
            R28cell1.setCellStyle(textStyle);
        }

        Cell R28cell2 = row.createCell(3);
        if (record.getR28_dis_admt_bal_related_comp_mor_then_1_to_3_mon() != null) {
            R28cell2.setCellValue(record.getR28_dis_admt_bal_related_comp_mor_then_1_to_3_mon().doubleValue());
            R28cell2.setCellStyle(numberStyle);
        } else {
            R28cell2.setCellValue("");
            R28cell2.setCellStyle(textStyle);
        }

        Cell R28cell3 = row.createCell(4);
        if (record.getR28_dis_admt_bal_related_comp_mor_then_3_to_6_mon() != null) {
            R28cell3.setCellValue(record.getR28_dis_admt_bal_related_comp_mor_then_3_to_6_mon().doubleValue());
            R28cell3.setCellStyle(numberStyle);
        } else {
            R28cell3.setCellValue("");
            R28cell3.setCellStyle(textStyle);
        }

        Cell R28cell4 = row.createCell(5);
        if (record.getR28_dis_admt_bal_related_comp_mor_then_6_to_12_mon() != null) {
            R28cell4.setCellValue(record.getR28_dis_admt_bal_related_comp_mor_then_6_to_12_mon().doubleValue());
            R28cell4.setCellStyle(numberStyle);
        } else {
            R28cell4.setCellValue("");
            R28cell4.setCellStyle(textStyle);
        }

        Cell R28cell5 = row.createCell(6);
        if (record.getR28_dis_admt_bal_related_comp_mor_then_12_mon_to_3_year() != null) {
            R28cell5.setCellValue(record.getR28_dis_admt_bal_related_comp_mor_then_12_mon_to_3_year().doubleValue());
            R28cell5.setCellStyle(numberStyle);
        } else {
            R28cell5.setCellValue("");
            R28cell5.setCellStyle(textStyle);
        }

        Cell R28cell6 = row.createCell(7);
        if (record.getR28_dis_admt_bal_related_comp_mor_then_3_to_5_year() != null) {
            R28cell6.setCellValue(record.getR28_dis_admt_bal_related_comp_mor_then_3_to_5_year().doubleValue());
            R28cell6.setCellStyle(numberStyle);
        } else {
            R28cell6.setCellValue("");
            R28cell6.setCellStyle(textStyle);
        }

        Cell R28cell7 = row.createCell(8);
        if (record.getR28_dis_admt_bal_related_comp_mor_then_5_to_10_year() != null) {
            R28cell7.setCellValue(record.getR28_dis_admt_bal_related_comp_mor_then_5_to_10_year().doubleValue());
            R28cell7.setCellStyle(numberStyle);
        } else {
            R28cell7.setCellValue("");
            R28cell7.setCellStyle(textStyle);
        }

        Cell R28cell8 = row.createCell(9);
        if (record.getR28_dis_admt_bal_related_comp_mor_then_10_year() != null) {
            R28cell8.setCellValue(record.getR28_dis_admt_bal_related_comp_mor_then_10_year().doubleValue());
            R28cell8.setCellStyle(numberStyle);
        } else {
            R28cell8.setCellValue("");
            R28cell8.setCellStyle(textStyle);
        }

// Row 29
        row = sheet.getRow(28);
        Cell R29cell1 = row.createCell(2);
        if (record.getR29_dis_admt_bnk_of_botswana_cert_up_to_1_mnt() != null) {
            R29cell1.setCellValue(record.getR29_dis_admt_bnk_of_botswana_cert_up_to_1_mnt().doubleValue());
            R29cell1.setCellStyle(numberStyle);
        } else {
            R29cell1.setCellValue("");
            R29cell1.setCellStyle(textStyle);
        }

        Cell R29cell2 = row.createCell(3);
        if (record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_1_to_3_mon() != null) {
            R29cell2.setCellValue(record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_1_to_3_mon().doubleValue());
            R29cell2.setCellStyle(numberStyle);
        } else {
            R29cell2.setCellValue("");
            R29cell2.setCellStyle(textStyle);
        }

        Cell R29cell3 = row.createCell(4);
        if (record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_6_mon() != null) {
            R29cell3.setCellValue(record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_6_mon().doubleValue());
            R29cell3.setCellStyle(numberStyle);
        } else {
            R29cell3.setCellValue("");
            R29cell3.setCellStyle(textStyle);
        }

        Cell R29cell4 = row.createCell(5);
        if (record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_6_to_12_mon() != null) {
            R29cell4.setCellValue(record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_6_to_12_mon().doubleValue());
            R29cell4.setCellStyle(numberStyle);
        } else {
            R29cell4.setCellValue("");
            R29cell4.setCellStyle(textStyle);
        }

        Cell R29cell5 = row.createCell(6);
        if (record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_12_mon_to_3_year() != null) {
            R29cell5.setCellValue(record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_12_mon_to_3_year().doubleValue());
            R29cell5.setCellStyle(numberStyle);
        } else {
            R29cell5.setCellValue("");
            R29cell5.setCellStyle(textStyle);
        }

        Cell R29cell6 = row.createCell(7);
        if (record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_5_year() != null) {
            R29cell6.setCellValue(record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_3_to_5_year().doubleValue());
            R29cell6.setCellStyle(numberStyle);
        } else {
            R29cell6.setCellValue("");
            R29cell6.setCellStyle(textStyle);
        }

        Cell R29cell7 = row.createCell(8);
        if (record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_5_to_10_year() != null) {
            R29cell7.setCellValue(record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_5_to_10_year().doubleValue());
            R29cell7.setCellStyle(numberStyle);
        } else {
            R29cell7.setCellValue("");
            R29cell7.setCellStyle(textStyle);
        }

        Cell R29cell8 = row.createCell(9);
        if (record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_10_year() != null) {
            R29cell8.setCellValue(record.getR29_dis_admt_bnk_of_botswana_cert_mor_then_10_year().doubleValue());
            R29cell8.setCellStyle(numberStyle);
        } else {
            R29cell8.setCellValue("");
            R29cell8.setCellStyle(textStyle);
        }


// Row 30
        row = sheet.getRow(29);
        Cell R30cell1 = row.createCell(2);
        if (record.getR30_dis_admt_gov_bonds_mor_then_1_to_3_mon() != null) {
            R30cell1.setCellValue(record.getR30_dis_admt_gov_bonds_mor_then_1_to_3_mon().doubleValue());
            R30cell1.setCellStyle(numberStyle);
        } else {
            R30cell1.setCellValue("");
            R30cell1.setCellStyle(textStyle);
        }

        Cell R30cell2 = row.createCell(3);
        if (record.getR30_dis_admt_gov_bonds_mor_then_3_to_6_mon() != null) {
            R30cell2.setCellValue(record.getR30_dis_admt_gov_bonds_mor_then_3_to_6_mon().doubleValue());
            R30cell2.setCellStyle(numberStyle);
        } else {
            R30cell2.setCellValue("");
            R30cell2.setCellStyle(textStyle);
        }

        Cell R30cell3 = row.createCell(4);
        if (record.getR30_dis_admt_gov_bonds_mor_then_6_to_12_mon() != null) {
            R30cell3.setCellValue(record.getR30_dis_admt_gov_bonds_mor_then_6_to_12_mon().doubleValue());
            R30cell3.setCellStyle(numberStyle);
        } else {
            R30cell3.setCellValue("");
            R30cell3.setCellStyle(textStyle);
        }

        Cell R30cell4 = row.createCell(5);
        if (record.getR30_dis_admt_gov_bonds_mor_then_12_mon_to_3_year() != null) {
            R30cell4.setCellValue(record.getR30_dis_admt_gov_bonds_mor_then_12_mon_to_3_year().doubleValue());
            R30cell4.setCellStyle(numberStyle);
        } else {
            R30cell4.setCellValue("");
            R30cell4.setCellStyle(textStyle);
        }

        Cell R30cell5 = row.createCell(6);
        if (record.getR30_dis_admt_gov_bonds_up_to_1_mnt() != null) {
            R30cell5.setCellValue(record.getR30_dis_admt_gov_bonds_up_to_1_mnt().doubleValue());
            R30cell5.setCellStyle(numberStyle);
        } else {
            R30cell5.setCellValue("");
            R30cell5.setCellStyle(textStyle);
        }

        Cell R30cell6 = row.createCell(7);
        if (record.getR30_dis_admt_gov_bonds_mor_then_3_to_5_year() != null) {
            R30cell6.setCellValue(record.getR30_dis_admt_gov_bonds_mor_then_3_to_5_year().doubleValue());
            R30cell6.setCellStyle(numberStyle);
        } else {
            R30cell6.setCellValue("");
            R30cell6.setCellStyle(textStyle);
        }

        Cell R30cell7 = row.createCell(8);
        if (record.getR30_dis_admt_gov_bonds_mor_then_5_to_10_year() != null) {
            R30cell7.setCellValue(record.getR30_dis_admt_gov_bonds_mor_then_5_to_10_year().doubleValue());
            R30cell7.setCellStyle(numberStyle);
        } else {
            R30cell7.setCellValue("");
            R30cell7.setCellStyle(textStyle);
        }

        Cell R30cell8 = row.createCell(9);
        if (record.getR30_dis_admt_gov_bonds_mor_then_10_year() != null) {
            R30cell8.setCellValue(record.getR30_dis_admt_gov_bonds_mor_then_10_year().doubleValue());
            R30cell8.setCellStyle(numberStyle);
        } else {
            R30cell8.setCellValue("");
            R30cell8.setCellStyle(textStyle);
        }

// Row 31
        row = sheet.getRow(30);
        Cell R31cell1 = row.createCell(2);
        if (record.getR31_dis_admt_other_invt_specify_up_to_1_mnt() != null) {
            R31cell1.setCellValue(record.getR31_dis_admt_other_invt_specify_up_to_1_mnt().doubleValue());
            R31cell1.setCellStyle(numberStyle);
        } else {
            R31cell1.setCellValue("");
            R31cell1.setCellStyle(textStyle);
        }

        Cell R31cell2 = row.createCell(3);
        if (record.getR31_dis_admt_other_invt_specify_mor_then_1_to_3_mon() != null) {
            R31cell2.setCellValue(record.getR31_dis_admt_other_invt_specify_mor_then_1_to_3_mon().doubleValue());
            R31cell2.setCellStyle(numberStyle);
        } else {
            R31cell2.setCellValue("");
            R31cell2.setCellStyle(textStyle);
        }

        Cell R31cell3 = row.createCell(4);
        if (record.getR31_dis_admt_other_invt_specify_mor_then_3_to_6_mon() != null) {
            R31cell3.setCellValue(record.getR31_dis_admt_other_invt_specify_mor_then_3_to_6_mon().doubleValue());
            R31cell3.setCellStyle(numberStyle);
        } else {
            R31cell3.setCellValue("");
            R31cell3.setCellStyle(textStyle);
        }

        Cell R31cell4 = row.createCell(5);
        if (record.getR31_dis_admt_other_invt_specify_mor_then_6_to_12_mon() != null) {
            R31cell4.setCellValue(record.getR31_dis_admt_other_invt_specify_mor_then_6_to_12_mon().doubleValue());
            R31cell4.setCellStyle(numberStyle);
        } else {
            R31cell4.setCellValue("");
            R31cell4.setCellStyle(textStyle);
        }

        Cell R31cell5 = row.createCell(6);
        if (record.getR31_dis_admt_other_invt_specify_mor_then_12_mon_to_3_year() != null) {
            R31cell5.setCellValue(record.getR31_dis_admt_other_invt_specify_mor_then_12_mon_to_3_year().doubleValue());
            R31cell5.setCellStyle(numberStyle);
        } else {
            R31cell5.setCellValue("");
            R31cell5.setCellStyle(textStyle);
        }

        Cell R31cell6 = row.createCell(7);
        if (record.getR31_dis_admt_other_invt_specify_mor_then_3_to_5_year() != null) {
            R31cell6.setCellValue(record.getR31_dis_admt_other_invt_specify_mor_then_3_to_5_year().doubleValue());
            R31cell6.setCellStyle(numberStyle);
        } else {
            R31cell6.setCellValue("");
            R31cell6.setCellStyle(textStyle);
        }

        Cell R31cell7 = row.createCell(8);
        if (record.getR31_dis_admt_other_invt_specify_mor_then_5_to_10_year() != null) {
            R31cell7.setCellValue(record.getR31_dis_admt_other_invt_specify_mor_then_5_to_10_year().doubleValue());
            R31cell7.setCellStyle(numberStyle);
        } else {
            R31cell7.setCellValue("");
            R31cell7.setCellStyle(textStyle);
        }

        Cell R31cell8 = row.createCell(9);
        if (record.getR31_dis_admt_other_invt_specify_mor_then_10_year() != null) {
            R31cell8.setCellValue(record.getR31_dis_admt_other_invt_specify_mor_then_10_year().doubleValue());
            R31cell8.setCellStyle(numberStyle);
        } else {
            R31cell8.setCellValue("");
            R31cell8.setCellStyle(textStyle);
        }


// Row 32
        row = sheet.getRow(31);
        Cell R32cell1 = row.createCell(2);
        if (record.getR32_dis_admt_loans_and_adv_to_cust_up_to_1_mnt() != null) {
            R32cell1.setCellValue(record.getR32_dis_admt_loans_and_adv_to_cust_up_to_1_mnt().doubleValue());
            R32cell1.setCellStyle(numberStyle);
        } else {
            R32cell1.setCellValue("");
            R32cell1.setCellStyle(textStyle);
        }

        Cell R32cell2 = row.createCell(3);
        if (record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_1_to_3_mon() != null) {
            R32cell2.setCellValue(record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_1_to_3_mon().doubleValue());
            R32cell2.setCellStyle(numberStyle);
        } else {
            R32cell2.setCellValue("");
            R32cell2.setCellStyle(textStyle);
        }

        Cell R32cell3 = row.createCell(4);
        if (record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_6_mon() != null) {
            R32cell3.setCellValue(record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_6_mon().doubleValue());
            R32cell3.setCellStyle(numberStyle);
        } else {
            R32cell3.setCellValue("");
            R32cell3.setCellStyle(textStyle);
        }

        Cell R32cell4 = row.createCell(5);
        if (record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_6_to_12_mon() != null) {
            R32cell4.setCellValue(record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_6_to_12_mon().doubleValue());
            R32cell4.setCellStyle(numberStyle);
        } else {
            R32cell4.setCellValue("");
            R32cell4.setCellStyle(textStyle);
        }

        Cell R32cell5 = row.createCell(6);
        if (record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_12_mon_to_3_year() != null) {
            R32cell5.setCellValue(record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_12_mon_to_3_year().doubleValue());
            R32cell5.setCellStyle(numberStyle);
        } else {
            R32cell5.setCellValue("");
            R32cell5.setCellStyle(textStyle);
        }

        Cell R32cell6 = row.createCell(7);
        if (record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_5_year() != null) {
            R32cell6.setCellValue(record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_3_to_5_year().doubleValue());
            R32cell6.setCellStyle(numberStyle);
        } else {
            R32cell6.setCellValue("");
            R32cell6.setCellStyle(textStyle);
        }

        Cell R32cell7 = row.createCell(8);
        if (record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_5_to_10_year() != null) {
            R32cell7.setCellValue(record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_5_to_10_year().doubleValue());
            R32cell7.setCellStyle(numberStyle);
        } else {
            R32cell7.setCellValue("");
            R32cell7.setCellStyle(textStyle);
        }

        Cell R32cell8 = row.createCell(9);
        if (record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_10_year() != null) {
            R32cell8.setCellValue(record.getR32_dis_admt_loans_and_adv_to_cust_mor_then_10_year().doubleValue());
            R32cell8.setCellStyle(numberStyle);
        } else {
            R32cell8.setCellValue("");
            R32cell8.setCellStyle(textStyle);
        }

// Row 33
        row = sheet.getRow(32);
        Cell R33cell1 = row.createCell(2);
        if (record.getR33_dis_admt_prop_and_eqp_up_to_1_mnt() != null) {
            R33cell1.setCellValue(record.getR33_dis_admt_prop_and_eqp_up_to_1_mnt().doubleValue());
            R33cell1.setCellStyle(numberStyle);
        } else {
            R33cell1.setCellValue("");
            R33cell1.setCellStyle(textStyle);
        }

        Cell R33cell2 = row.createCell(3);
        if (record.getR33_dis_admt_prop_and_eqp_mor_then_1_to_3_mon() != null) {
            R33cell2.setCellValue(record.getR33_dis_admt_prop_and_eqp_mor_then_1_to_3_mon().doubleValue());
            R33cell2.setCellStyle(numberStyle);
        } else {
            R33cell2.setCellValue("");
            R33cell2.setCellStyle(textStyle);
        }

        Cell R33cell3 = row.createCell(4);
        if (record.getR33_dis_admt_prop_and_eqp_mor_then_3_to_6_mon() != null) {
            R33cell3.setCellValue(record.getR33_dis_admt_prop_and_eqp_mor_then_3_to_6_mon().doubleValue());
            R33cell3.setCellStyle(numberStyle);
        } else {
            R33cell3.setCellValue("");
            R33cell3.setCellStyle(textStyle);
        }

        Cell R33cell4 = row.createCell(5);
        if (record.getR33_dis_admt_prop_and_eqp_mor_then_6_to_12_mon() != null) {
            R33cell4.setCellValue(record.getR33_dis_admt_prop_and_eqp_mor_then_6_to_12_mon().doubleValue());
            R33cell4.setCellStyle(numberStyle);
        } else {
            R33cell4.setCellValue("");
            R33cell4.setCellStyle(textStyle);
        }

        Cell R33cell5 = row.createCell(6);
        if (record.getR33_dis_admt_prop_and_eqp_mor_then_12_mon_to_3_year() != null) {
            R33cell5.setCellValue(record.getR33_dis_admt_prop_and_eqp_mor_then_12_mon_to_3_year().doubleValue());
            R33cell5.setCellStyle(numberStyle);
        } else {
            R33cell5.setCellValue("");
            R33cell5.setCellStyle(textStyle);
        }

        Cell R33cell6 = row.createCell(7);
        if (record.getR33_dis_admt_prop_and_eqp_mor_then_3_to_5_year() != null) {
            R33cell6.setCellValue(record.getR33_dis_admt_prop_and_eqp_mor_then_3_to_5_year().doubleValue());
            R33cell6.setCellStyle(numberStyle);
        } else {
            R33cell6.setCellValue("");
            R33cell6.setCellStyle(textStyle);
        }

        Cell R33cell7 = row.createCell(8);
        if (record.getR33_dis_admt_prop_and_eqp_mor_then_5_to_10_year() != null) {
            R33cell7.setCellValue(record.getR33_dis_admt_prop_and_eqp_mor_then_5_to_10_year().doubleValue());
            R33cell7.setCellStyle(numberStyle);
        } else {
            R33cell7.setCellValue("");
            R33cell7.setCellStyle(textStyle);
        }

        Cell R33cell8 = row.createCell(9);
        if (record.getR33_dis_admt_prop_and_eqp_mor_then_10_year() != null) {
            R33cell8.setCellValue(record.getR33_dis_admt_prop_and_eqp_mor_then_10_year().doubleValue());
            R33cell8.setCellStyle(numberStyle);
        } else {
            R33cell8.setCellValue("");
            R33cell8.setCellStyle(textStyle);
        }

// Row 34
        row = sheet.getRow(33);
        Cell R34cell1 = row.createCell(2);
        if (record.getR34_dis_admt_other_assets_specify_up_to_1_mnt() != null) {
            R34cell1.setCellValue(record.getR34_dis_admt_other_assets_specify_up_to_1_mnt().doubleValue());
            R34cell1.setCellStyle(numberStyle);
        } else {
            R34cell1.setCellValue("");
            R34cell1.setCellStyle(textStyle);
        }

        Cell R34cell2 = row.createCell(3);
        if (record.getR34_dis_admt_other_assets_specify_mor_then_1_to_3_mon() != null) {
            R34cell2.setCellValue(record.getR34_dis_admt_other_assets_specify_mor_then_1_to_3_mon().doubleValue());
            R34cell2.setCellStyle(numberStyle);
        } else {
            R34cell2.setCellValue("");
            R34cell2.setCellStyle(textStyle);
        }

        Cell R34cell3 = row.createCell(4);
        if (record.getR34_dis_admt_other_assets_specify_mor_then_3_to_6_mon() != null) {
            R34cell3.setCellValue(record.getR34_dis_admt_other_assets_specify_mor_then_3_to_6_mon().doubleValue());
            R34cell3.setCellStyle(numberStyle);
        } else {
            R34cell3.setCellValue("");
            R34cell3.setCellStyle(textStyle);
        }

        Cell R34cell4 = row.createCell(5);
        if (record.getR34_dis_admt_other_assets_specify_mor_then_6_to_12_mon() != null) {
            R34cell4.setCellValue(record.getR34_dis_admt_other_assets_specify_mor_then_6_to_12_mon().doubleValue());
            R34cell4.setCellStyle(numberStyle);
        } else {
            R34cell4.setCellValue("");
            R34cell4.setCellStyle(textStyle);
        }

        Cell R34cell5 = row.createCell(6);
        if (record.getR34_dis_admt_other_assets_specify_mor_then_12_mon_to_3_year() != null) {
            R34cell5.setCellValue(record.getR34_dis_admt_other_assets_specify_mor_then_12_mon_to_3_year().doubleValue());
            R34cell5.setCellStyle(numberStyle);
        } else {
            R34cell5.setCellValue("");
            R34cell5.setCellStyle(textStyle);
        }

        Cell R34cell6 = row.createCell(7);
        if (record.getR34_dis_admt_other_assets_specify_mor_then_3_to_5_year() != null) {
            R34cell6.setCellValue(record.getR34_dis_admt_other_assets_specify_mor_then_3_to_5_year().doubleValue());
            R34cell6.setCellStyle(numberStyle);
        } else {
            R34cell6.setCellValue("");
            R34cell6.setCellStyle(textStyle);
        }

        Cell R34cell7 = row.createCell(8);
        if (record.getR34_dis_admt_other_assets_specify_mor_then_5_to_10_year() != null) {
            R34cell7.setCellValue(record.getR34_dis_admt_other_assets_specify_mor_then_5_to_10_year().doubleValue());
            R34cell7.setCellStyle(numberStyle);
        } else {
            R34cell7.setCellValue("");
            R34cell7.setCellStyle(textStyle);
        }

        Cell R34cell8 = row.createCell(9);
        if (record.getR34_dis_admt_other_assets_specify_mor_then_10_year() != null) {
            R34cell8.setCellValue(record.getR34_dis_admt_other_assets_specify_mor_then_10_year().doubleValue());
            R34cell8.setCellStyle(numberStyle);
        } else {
            R34cell8.setCellValue("");
            R34cell8.setCellStyle(textStyle);
        }

// Row 36
        row = sheet.getRow(35);
        Cell R36cell1 = row.createCell(2);
        if (record.getR36_fix_rate_cash_up_to_1_mnt() != null) {
            R36cell1.setCellValue(record.getR36_fix_rate_cash_up_to_1_mnt().doubleValue());
            R36cell1.setCellStyle(numberStyle);
        } else {
            R36cell1.setCellValue("");
            R36cell1.setCellStyle(textStyle);
        }

        Cell R36cell2 = row.createCell(3);
        if (record.getR36_fix_rate_cash_mor_then_1_to_3_mon() != null) {
            R36cell2.setCellValue(record.getR36_fix_rate_cash_mor_then_1_to_3_mon().doubleValue());
            R36cell2.setCellStyle(numberStyle);
        } else {
            R36cell2.setCellValue("");
            R36cell2.setCellStyle(textStyle);
        }

        Cell R36cell3 = row.createCell(4);
        if (record.getR36_fix_rate_cash_mor_then_3_to_6_mon() != null) {
            R36cell3.setCellValue(record.getR36_fix_rate_cash_mor_then_3_to_6_mon().doubleValue());
            R36cell3.setCellStyle(numberStyle);
        } else {
            R36cell3.setCellValue("");
            R36cell3.setCellStyle(textStyle);
        }

        Cell R36cell4 = row.createCell(5);
        if (record.getR36_fix_rate_cash_mor_then_6_to_12_mon() != null) {
            R36cell4.setCellValue(record.getR36_fix_rate_cash_mor_then_6_to_12_mon().doubleValue());
            R36cell4.setCellStyle(numberStyle);
        } else {
            R36cell4.setCellValue("");
            R36cell4.setCellStyle(textStyle);
        }

        Cell R36cell5 = row.createCell(6);
        if (record.getR36_fix_rate_cash_mor_then_12_mon_to_3_year() != null) {
            R36cell5.setCellValue(record.getR36_fix_rate_cash_mor_then_12_mon_to_3_year().doubleValue());
            R36cell5.setCellStyle(numberStyle);
        } else {
            R36cell5.setCellValue("");
            R36cell5.setCellStyle(textStyle);
        }

        Cell R36cell6 = row.createCell(7);
        if (record.getR36_fix_rate_cash_mor_then_3_to_5_year() != null) {
            R36cell6.setCellValue(record.getR36_fix_rate_cash_mor_then_3_to_5_year().doubleValue());
            R36cell6.setCellStyle(numberStyle);
        } else {
            R36cell6.setCellValue("");
            R36cell6.setCellStyle(textStyle);
        }

        Cell R36cell7 = row.createCell(8);
        if (record.getR36_fix_rate_cash_mor_then_5_to_10_year() != null) {
            R36cell7.setCellValue(record.getR36_fix_rate_cash_mor_then_5_to_10_year().doubleValue());
            R36cell7.setCellStyle(numberStyle);
        } else {
            R36cell7.setCellValue("");
            R36cell7.setCellStyle(textStyle);
        }

        Cell R36cell8 = row.createCell(9);
        if (record.getR36_fix_rate_cash_mor_then_10_year() != null) {
            R36cell8.setCellValue(record.getR36_fix_rate_cash_mor_then_10_year().doubleValue());
            R36cell8.setCellStyle(numberStyle);
        } else {
            R36cell8.setCellValue("");
            R36cell8.setCellStyle(textStyle);
        }



// Row 37
        row = sheet.getRow(36);
        Cell R37cell1 = row.createCell(2);
        if (record.getR37_fix_rate_bal_bnk_of_botswana_up_to_1_mnt() != null) {
            R37cell1.setCellValue(record.getR37_fix_rate_bal_bnk_of_botswana_up_to_1_mnt().doubleValue());
            R37cell1.setCellStyle(numberStyle);
        } else {
            R37cell1.setCellValue("");
            R37cell1.setCellStyle(textStyle);
        }

        Cell R37cell2 = row.createCell(3);
        if (record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_1_to_3_mon() != null) {
            R37cell2.setCellValue(record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_1_to_3_mon().doubleValue());
            R37cell2.setCellStyle(numberStyle);
        } else {
            R37cell2.setCellValue("");
            R37cell2.setCellStyle(textStyle);
        }

        Cell R37cell3 = row.createCell(4);
        if (record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_6_mon() != null) {
            R37cell3.setCellValue(record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_6_mon().doubleValue());
            R37cell3.setCellStyle(numberStyle);
        } else {
            R37cell3.setCellValue("");
            R37cell3.setCellStyle(textStyle);
        }

        Cell R37cell4 = row.createCell(5);
        if (record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_6_to_12_mon() != null) {
            R37cell4.setCellValue(record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_6_to_12_mon().doubleValue());
            R37cell4.setCellStyle(numberStyle);
        } else {
            R37cell4.setCellValue("");
            R37cell4.setCellStyle(textStyle);
        }

        Cell R37cell5 = row.createCell(6);
        if (record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_12_mon_to_3_year() != null) {
            R37cell5.setCellValue(record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_12_mon_to_3_year().doubleValue());
            R37cell5.setCellStyle(numberStyle);
        } else {
            R37cell5.setCellValue("");
            R37cell5.setCellStyle(textStyle);
        }

        Cell R37cell6 = row.createCell(7);
        if (record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_5_year() != null) {
            R37cell6.setCellValue(record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_3_to_5_year().doubleValue());
            R37cell6.setCellStyle(numberStyle);
        } else {
            R37cell6.setCellValue("");
            R37cell6.setCellStyle(textStyle);
        }

        Cell R37cell7 = row.createCell(8);
        if (record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_5_to_10_year() != null) {
            R37cell7.setCellValue(record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_5_to_10_year().doubleValue());
            R37cell7.setCellStyle(numberStyle);
        } else {
            R37cell7.setCellValue("");
            R37cell7.setCellStyle(textStyle);
        }

        Cell R37cell8 = row.createCell(9);
        if (record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_10_year() != null) {
            R37cell8.setCellValue(record.getR37_fix_rate_bal_bnk_of_botswana_mor_then_10_year().doubleValue());
            R37cell8.setCellStyle(numberStyle);
        } else {
            R37cell8.setCellValue("");
            R37cell8.setCellStyle(textStyle);
        }




// Row 38
        row = sheet.getRow(37);
        Cell R38cell1 = row.createCell(2);
        if (record.getR38_fix_rate_bal_doms_bnks_up_to_1_mnt() != null) {
            R38cell1.setCellValue(record.getR38_fix_rate_bal_doms_bnks_up_to_1_mnt().doubleValue());
            R38cell1.setCellStyle(numberStyle);
        } else {
            R38cell1.setCellValue("");
            R38cell1.setCellStyle(textStyle);
        }

        Cell R38cell2 = row.createCell(3);
        if (record.getR38_fix_rate_bal_doms_bnks_mor_then_1_to_3_mon() != null) {
            R38cell2.setCellValue(record.getR38_fix_rate_bal_doms_bnks_mor_then_1_to_3_mon().doubleValue());
            R38cell2.setCellStyle(numberStyle);
        } else {
            R38cell2.setCellValue("");
            R38cell2.setCellStyle(textStyle);
        }

        Cell R38cell3 = row.createCell(4);
        if (record.getR38_fix_rate_bal_doms_bnks_mor_then_3_to_6_mon() != null) {
            R38cell3.setCellValue(record.getR38_fix_rate_bal_doms_bnks_mor_then_3_to_6_mon().doubleValue());
            R38cell3.setCellStyle(numberStyle);
        } else {
            R38cell3.setCellValue("");
            R38cell3.setCellStyle(textStyle);
        }

        Cell R38cell4 = row.createCell(5);
        if (record.getR38_fix_rate_bal_doms_bnks_mor_then_6_to_12_mon() != null) {
            R38cell4.setCellValue(record.getR38_fix_rate_bal_doms_bnks_mor_then_6_to_12_mon().doubleValue());
            R38cell4.setCellStyle(numberStyle);
        } else {
            R38cell4.setCellValue("");
            R38cell4.setCellStyle(textStyle);
        }

        Cell R38cell5 = row.createCell(6);
        if (record.getR38_fix_rate_bal_doms_bnks_mor_then_12_mon_to_3_year() != null) {
            R38cell5.setCellValue(record.getR38_fix_rate_bal_doms_bnks_mor_then_12_mon_to_3_year().doubleValue());
            R38cell5.setCellStyle(numberStyle);
        } else {
            R38cell5.setCellValue("");
            R38cell5.setCellStyle(textStyle);
        }

        Cell R38cell6 = row.createCell(7);
        if (record.getR38_fix_rate_bal_doms_bnks_mor_then_3_to_5_year() != null) {
            R38cell6.setCellValue(record.getR38_fix_rate_bal_doms_bnks_mor_then_3_to_5_year().doubleValue());
            R38cell6.setCellStyle(numberStyle);
        } else {
            R38cell6.setCellValue("");
            R38cell6.setCellStyle(textStyle);
        }

        Cell R38cell7 = row.createCell(8);
        if (record.getR38_fix_rate_bal_doms_bnks_mor_then_5_to_10_year() != null) {
            R38cell7.setCellValue(record.getR38_fix_rate_bal_doms_bnks_mor_then_5_to_10_year().doubleValue());
            R38cell7.setCellStyle(numberStyle);
        } else {
            R38cell7.setCellValue("");
            R38cell7.setCellStyle(textStyle);
        }

        Cell R38cell8 = row.createCell(9);
        if (record.getR38_fix_rate_bal_doms_bnks_mor_then_10_year() != null) {
            R38cell8.setCellValue(record.getR38_fix_rate_bal_doms_bnks_mor_then_10_year().doubleValue());
            R38cell8.setCellStyle(numberStyle);
        } else {
            R38cell8.setCellValue("");
            R38cell8.setCellStyle(textStyle);
        }


// Row 39
        row = sheet.getRow(38);
        Cell R39cell1 = row.createCell(2);
        if (record.getR39_fix_rate_bal_foreign_bnks_up_to_1_mnt() != null) {
            R39cell1.setCellValue(record.getR39_fix_rate_bal_foreign_bnks_up_to_1_mnt().doubleValue());
            R39cell1.setCellStyle(numberStyle);
        } else {
            R39cell1.setCellValue("");
            R39cell1.setCellStyle(textStyle);
        }

        Cell R39cell2 = row.createCell(3);
        if (record.getR39_fix_rate_bal_foreign_bnks_mor_then_1_to_3_mon() != null) {
            R39cell2.setCellValue(record.getR39_fix_rate_bal_foreign_bnks_mor_then_1_to_3_mon().doubleValue());
            R39cell2.setCellStyle(numberStyle);
        } else {
            R39cell2.setCellValue("");
            R39cell2.setCellStyle(textStyle);
        }

        Cell R39cell3 = row.createCell(4);
        if (record.getR39_fix_rate_bal_foreign_bnks_mor_then_3_to_6_mon() != null) {
            R39cell3.setCellValue(record.getR39_fix_rate_bal_foreign_bnks_mor_then_3_to_6_mon().doubleValue());
            R39cell3.setCellStyle(numberStyle);
        } else {
            R39cell3.setCellValue("");
            R39cell3.setCellStyle(textStyle);
        }

        Cell R39cell4 = row.createCell(5);
        if (record.getR39_fix_rate_bal_foreign_bnks_mor_then_6_to_12_mon() != null) {
            R39cell4.setCellValue(record.getR39_fix_rate_bal_foreign_bnks_mor_then_6_to_12_mon().doubleValue());
            R39cell4.setCellStyle(numberStyle);
        } else {
            R39cell4.setCellValue("");
            R39cell4.setCellStyle(textStyle);
        }

        Cell R39cell5 = row.createCell(6);
        if (record.getR39_fix_rate_bal_foreign_bnks_mor_then_12_mon_to_3_year() != null) {
            R39cell5.setCellValue(record.getR39_fix_rate_bal_foreign_bnks_mor_then_12_mon_to_3_year().doubleValue());
            R39cell5.setCellStyle(numberStyle);
        } else {
            R39cell5.setCellValue("");
            R39cell5.setCellStyle(textStyle);
        }

        Cell R39cell6 = row.createCell(7);
        if (record.getR39_fix_rate_bal_foreign_bnks_mor_then_3_to_5_year() != null) {
            R39cell6.setCellValue(record.getR39_fix_rate_bal_foreign_bnks_mor_then_3_to_5_year().doubleValue());
            R39cell6.setCellStyle(numberStyle);
        } else {
            R39cell6.setCellValue("");
            R39cell6.setCellStyle(textStyle);
        }

        Cell R39cell7 = row.createCell(8);
        if (record.getR39_fix_rate_bal_foreign_bnks_mor_then_5_to_10_year() != null) {
            R39cell7.setCellValue(record.getR39_fix_rate_bal_foreign_bnks_mor_then_5_to_10_year().doubleValue());
            R39cell7.setCellStyle(numberStyle);
        } else {
            R39cell7.setCellValue("");
            R39cell7.setCellStyle(textStyle);
        }

        Cell R39cell8 = row.createCell(9);
        if (record.getR39_fix_rate_bal_foreign_bnks_mor_then_10_year() != null) {
            R39cell8.setCellValue(record.getR39_fix_rate_bal_foreign_bnks_mor_then_10_year().doubleValue());
            R39cell8.setCellStyle(numberStyle);
        } else {
            R39cell8.setCellValue("");
            R39cell8.setCellStyle(textStyle);
        }


// Row 40
        row = sheet.getRow(39);
        Cell R40cell1 = row.createCell(2);
        if (record.getR40_fix_rate_bal_related_comp_up_to_1_mnt() != null) {
            R40cell1.setCellValue(record.getR40_fix_rate_bal_related_comp_up_to_1_mnt().doubleValue());
            R40cell1.setCellStyle(numberStyle);
        } else {
            R40cell1.setCellValue("");
            R40cell1.setCellStyle(textStyle);
        }

        Cell R40cell2 = row.createCell(3);
        if (record.getR40_fix_rate_bal_related_comp_mor_then_1_to_3_mon() != null) {
            R40cell2.setCellValue(record.getR40_fix_rate_bal_related_comp_mor_then_1_to_3_mon().doubleValue());
            R40cell2.setCellStyle(numberStyle);
        } else {
            R40cell2.setCellValue("");
            R40cell2.setCellStyle(textStyle);
        }

        Cell R40cell3 = row.createCell(4);
        if (record.getR40_fix_rate_bal_related_comp_mor_then_3_to_6_mon() != null) {
            R40cell3.setCellValue(record.getR40_fix_rate_bal_related_comp_mor_then_3_to_6_mon().doubleValue());
            R40cell3.setCellStyle(numberStyle);
        } else {
            R40cell3.setCellValue("");
            R40cell3.setCellStyle(textStyle);
        }

        Cell R40cell4 = row.createCell(5);
        if (record.getR40_fix_rate_bal_related_comp_mor_then_6_to_12_mon() != null) {
            R40cell4.setCellValue(record.getR40_fix_rate_bal_related_comp_mor_then_6_to_12_mon().doubleValue());
            R40cell4.setCellStyle(numberStyle);
        } else {
            R40cell4.setCellValue("");
            R40cell4.setCellStyle(textStyle);
        }

        Cell R40cell5 = row.createCell(6);
        if (record.getR40_fix_rate_bal_related_comp_mor_then_12_mon_to_3_year() != null) {
            R40cell5.setCellValue(record.getR40_fix_rate_bal_related_comp_mor_then_12_mon_to_3_year().doubleValue());
            R40cell5.setCellStyle(numberStyle);
        } else {
            R40cell5.setCellValue("");
            R40cell5.setCellStyle(textStyle);
        }

        Cell R40cell6 = row.createCell(7);
        if (record.getR40_fix_rate_bal_related_comp_mor_then_3_to_5_year() != null) {
            R40cell6.setCellValue(record.getR40_fix_rate_bal_related_comp_mor_then_3_to_5_year().doubleValue());
            R40cell6.setCellStyle(numberStyle);
        } else {
            R40cell6.setCellValue("");
            R40cell6.setCellStyle(textStyle);
        }

        Cell R40cell7 = row.createCell(8);
        if (record.getR40_fix_rate_bal_related_comp_mor_then_5_to_10_year() != null) {
            R40cell7.setCellValue(record.getR40_fix_rate_bal_related_comp_mor_then_5_to_10_year().doubleValue());
            R40cell7.setCellStyle(numberStyle);
        } else {
            R40cell7.setCellValue("");
            R40cell7.setCellStyle(textStyle);
        }

        Cell R40cell8 = row.createCell(9);
        if (record.getR40_fix_rate_bal_related_comp_mor_then_10_year() != null) {
            R40cell8.setCellValue(record.getR40_fix_rate_bal_related_comp_mor_then_10_year().doubleValue());
            R40cell8.setCellStyle(numberStyle);
        } else {
            R40cell8.setCellValue("");
            R40cell8.setCellStyle(textStyle);
        }



// Row 41
        row = sheet.getRow(40);
        Cell R41cell1 = row.createCell(2);
        if (record.getR41_fix_rate_bnk_of_botswana_cert_up_to_1_mnt() != null) {
            R41cell1.setCellValue(record.getR41_fix_rate_bnk_of_botswana_cert_up_to_1_mnt().doubleValue());
            R41cell1.setCellStyle(numberStyle);
        } else {
            R41cell1.setCellValue("");
            R41cell1.setCellStyle(textStyle);
        }

        Cell R41cell2 = row.createCell(3);
        if (record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_1_to_3_mon() != null) {
            R41cell2.setCellValue(record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_1_to_3_mon().doubleValue());
            R41cell2.setCellStyle(numberStyle);
        } else {
            R41cell2.setCellValue("");
            R41cell2.setCellStyle(textStyle);
        }

        Cell R41cell3 = row.createCell(4);
        if (record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_6_mon() != null) {
            R41cell3.setCellValue(record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_6_mon().doubleValue());
            R41cell3.setCellStyle(numberStyle);
        } else {
            R41cell3.setCellValue("");
            R41cell3.setCellStyle(textStyle);
        }

        Cell R41cell4 = row.createCell(5);
        if (record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_6_to_12_mon() != null) {
            R41cell4.setCellValue(record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_6_to_12_mon().doubleValue());
            R41cell4.setCellStyle(numberStyle);
        } else {
            R41cell4.setCellValue("");
            R41cell4.setCellStyle(textStyle);
        }

        Cell R41cell5 = row.createCell(6);
        if (record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_12_mon_to_3_year() != null) {
            R41cell5.setCellValue(record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_12_mon_to_3_year().doubleValue());
            R41cell5.setCellStyle(numberStyle);
        } else {
            R41cell5.setCellValue("");
            R41cell5.setCellStyle(textStyle);
        }

        Cell R41cell6 = row.createCell(7);
        if (record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_5_year() != null) {
            R41cell6.setCellValue(record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_3_to_5_year().doubleValue());
            R41cell6.setCellStyle(numberStyle);
        } else {
            R41cell6.setCellValue("");
            R41cell6.setCellStyle(textStyle);
        }

        Cell R41cell7 = row.createCell(8);
        if (record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_5_to_10_year() != null) {
            R41cell7.setCellValue(record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_5_to_10_year().doubleValue());
            R41cell7.setCellStyle(numberStyle);
        } else {
            R41cell7.setCellValue("");
            R41cell7.setCellStyle(textStyle);
        }

        Cell R41cell8 = row.createCell(9);
        if (record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_10_year() != null) {
            R41cell8.setCellValue(record.getR41_fix_rate_bnk_of_botswana_cert_mor_then_10_year().doubleValue());
            R41cell8.setCellStyle(numberStyle);
        } else {
            R41cell8.setCellValue("");
            R41cell8.setCellStyle(textStyle);
        }


// Row 42
        row = sheet.getRow(41);
        Cell R42cell1 = row.createCell(2);
        if (record.getR42_fix_rate_gov_bonds_up_to_1_mnt() != null) {
            R42cell1.setCellValue(record.getR42_fix_rate_gov_bonds_up_to_1_mnt().doubleValue());
            R42cell1.setCellStyle(numberStyle);
        } else {
            R42cell1.setCellValue("");
            R42cell1.setCellStyle(textStyle);
        }

        Cell R42cell2 = row.createCell(3);
        if (record.getR42_fix_rate_gov_bonds_mor_then_1_to_3_mon() != null) {
            R42cell2.setCellValue(record.getR42_fix_rate_gov_bonds_mor_then_1_to_3_mon().doubleValue());
            R42cell2.setCellStyle(numberStyle);
        } else {
            R42cell2.setCellValue("");
            R42cell2.setCellStyle(textStyle);
        }

        Cell R42cell3 = row.createCell(4);
        if (record.getR42_fix_rate_gov_bonds_mor_then_3_to_6_mon() != null) {
            R42cell3.setCellValue(record.getR42_fix_rate_gov_bonds_mor_then_3_to_6_mon().doubleValue());
            R42cell3.setCellStyle(numberStyle);
        } else {
            R42cell3.setCellValue("");
            R42cell3.setCellStyle(textStyle);
        }

        Cell R42cell4 = row.createCell(5);
        if (record.getR42_fix_rate_gov_bonds_mor_then_6_to_12_mon() != null) {
            R42cell4.setCellValue(record.getR42_fix_rate_gov_bonds_mor_then_6_to_12_mon().doubleValue());
            R42cell4.setCellStyle(numberStyle);
        } else {
            R42cell4.setCellValue("");
            R42cell4.setCellStyle(textStyle);
        }

        Cell R42cell5 = row.createCell(6);
        if (record.getR42_fix_rate_gov_bonds_mor_then_12_mon_to_3_year() != null) {
            R42cell5.setCellValue(record.getR42_fix_rate_gov_bonds_mor_then_12_mon_to_3_year().doubleValue());
            R42cell5.setCellStyle(numberStyle);
        } else {
            R42cell5.setCellValue("");
            R42cell5.setCellStyle(textStyle);
        }

        Cell R42cell6 = row.createCell(7);
        if (record.getR42_fix_rate_gov_bonds_mor_then_3_to_5_year() != null) {
            R42cell6.setCellValue(record.getR42_fix_rate_gov_bonds_mor_then_3_to_5_year().doubleValue());
            R42cell6.setCellStyle(numberStyle);
        } else {
            R42cell6.setCellValue("");
            R42cell6.setCellStyle(textStyle);
        }

        Cell R42cell7 = row.createCell(8);
        if (record.getR42_fix_rate_gov_bonds_mor_then_5_to_10_year() != null) {
            R42cell7.setCellValue(record.getR42_fix_rate_gov_bonds_mor_then_5_to_10_year().doubleValue());
            R42cell7.setCellStyle(numberStyle);
        } else {
            R42cell7.setCellValue("");
            R42cell7.setCellStyle(textStyle);
        }

        Cell R42cell8 = row.createCell(9);
        if (record.getR42_fix_rate_gov_bonds_mor_then_10_year() != null) {
            R42cell8.setCellValue(record.getR42_fix_rate_gov_bonds_mor_then_10_year().doubleValue());
            R42cell8.setCellStyle(numberStyle);
        } else {
            R42cell8.setCellValue("");
            R42cell8.setCellStyle(textStyle);
        }



// Row 43
        row = sheet.getRow(42);
        Cell R43cell1 = row.createCell(2);
        if (record.getR43_fix_rate_other_invt_specify_up_to_1_mnt() != null) {
            R43cell1.setCellValue(record.getR43_fix_rate_other_invt_specify_up_to_1_mnt().doubleValue());
            R43cell1.setCellStyle(numberStyle);
        } else {
            R43cell1.setCellValue("");
            R43cell1.setCellStyle(textStyle);
        }

        Cell R43cell2 = row.createCell(3);
        if (record.getR43_fix_rate_other_invt_specify_mor_then_1_to_3_mon() != null) {
            R43cell2.setCellValue(record.getR43_fix_rate_other_invt_specify_mor_then_1_to_3_mon().doubleValue());
            R43cell2.setCellStyle(numberStyle);
        } else {
            R43cell2.setCellValue("");
            R43cell2.setCellStyle(textStyle);
        }

        Cell R43cell3 = row.createCell(4);
        if (record.getR43_fix_rate_other_invt_specify_mor_then_3_to_6_mon() != null) {
            R43cell3.setCellValue(record.getR43_fix_rate_other_invt_specify_mor_then_3_to_6_mon().doubleValue());
            R43cell3.setCellStyle(numberStyle);
        } else {
            R43cell3.setCellValue("");
            R43cell3.setCellStyle(textStyle);
        }

        Cell R43cell4 = row.createCell(5);
        if (record.getR43_fix_rate_other_invt_specify_mor_then_6_to_12_mon() != null) {
            R43cell4.setCellValue(record.getR43_fix_rate_other_invt_specify_mor_then_6_to_12_mon().doubleValue());
            R43cell4.setCellStyle(numberStyle);
        } else {
            R43cell4.setCellValue("");
            R43cell4.setCellStyle(textStyle);
        }

        Cell R43cell5 = row.createCell(6);
        if (record.getR43_fix_rate_other_invt_specify_mor_then_12_mon_to_3_year() != null) {
            R43cell5.setCellValue(record.getR43_fix_rate_other_invt_specify_mor_then_12_mon_to_3_year().doubleValue());
            R43cell5.setCellStyle(numberStyle);
        } else {
            R43cell5.setCellValue("");
            R43cell5.setCellStyle(textStyle);
        }

        Cell R43cell6 = row.createCell(7);
        if (record.getR43_fix_rate_other_invt_specify_mor_then_3_to_5_year() != null) {
            R43cell6.setCellValue(record.getR43_fix_rate_other_invt_specify_mor_then_3_to_5_year().doubleValue());
            R43cell6.setCellStyle(numberStyle);
        } else {
            R43cell6.setCellValue("");
            R43cell6.setCellStyle(textStyle);
        }

        Cell R43cell7 = row.createCell(8);
        if (record.getR43_fix_rate_other_invt_specify_mor_then_5_to_10_year() != null) {
            R43cell7.setCellValue(record.getR43_fix_rate_other_invt_specify_mor_then_5_to_10_year().doubleValue());
            R43cell7.setCellStyle(numberStyle);
        } else {
            R43cell7.setCellValue("");
            R43cell7.setCellStyle(textStyle);
        }

        Cell R43cell8 = row.createCell(9);
        if (record.getR43_fix_rate_other_invt_specify_mor_then_10_year() != null) {
            R43cell8.setCellValue(record.getR43_fix_rate_other_invt_specify_mor_then_10_year().doubleValue());
            R43cell8.setCellStyle(numberStyle);
        } else {
            R43cell8.setCellValue("");
            R43cell8.setCellStyle(textStyle);
        }


// Row 44
        row = sheet.getRow(43);
        Cell R44cell1 = row.createCell(2);
        if (record.getR44_fix_rate_loans_and_adv_to_cust_up_to_1_mnt() != null) {
            R44cell1.setCellValue(record.getR44_fix_rate_loans_and_adv_to_cust_up_to_1_mnt().doubleValue());
            R44cell1.setCellStyle(numberStyle);
        } else {
            R44cell1.setCellValue("");
            R44cell1.setCellStyle(textStyle);
        }

        Cell R44cell2 = row.createCell(3);
        if (record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_1_to_3_mon() != null) {
            R44cell2.setCellValue(record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_1_to_3_mon().doubleValue());
            R44cell2.setCellStyle(numberStyle);
        } else {
            R44cell2.setCellValue("");
            R44cell2.setCellStyle(textStyle);
        }

        Cell R44cell3 = row.createCell(4);
        if (record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_6_mon() != null) {
            R44cell3.setCellValue(record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_6_mon().doubleValue());
            R44cell3.setCellStyle(numberStyle);
        } else {
            R44cell3.setCellValue("");
            R44cell3.setCellStyle(textStyle);
        }

        Cell R44cell4 = row.createCell(5);
        if (record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_6_to_12_mon() != null) {
            R44cell4.setCellValue(record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_6_to_12_mon().doubleValue());
            R44cell4.setCellStyle(numberStyle);
        } else {
            R44cell4.setCellValue("");
            R44cell4.setCellStyle(textStyle);
        }

        Cell R44cell5 = row.createCell(6);
        if (record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_12_mon_to_3_year() != null) {
            R44cell5.setCellValue(record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_12_mon_to_3_year().doubleValue());
            R44cell5.setCellStyle(numberStyle);
        } else {
            R44cell5.setCellValue("");
            R44cell5.setCellStyle(textStyle);
        }

        Cell R44cell6 = row.createCell(7);
        if (record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_5_year() != null) {
            R44cell6.setCellValue(record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_3_to_5_year().doubleValue());
            R44cell6.setCellStyle(numberStyle);
        } else {
            R44cell6.setCellValue("");
            R44cell6.setCellStyle(textStyle);
        }

        Cell R44cell7 = row.createCell(8);
        if (record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_5_to_10_year() != null) {
            R44cell7.setCellValue(record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_5_to_10_year().doubleValue());
            R44cell7.setCellStyle(numberStyle);
        } else {
            R44cell7.setCellValue("");
            R44cell7.setCellStyle(textStyle);
        }

        Cell R44cell8 = row.createCell(9);
        if (record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_10_year() != null) {
            R44cell8.setCellValue(record.getR44_fix_rate_loans_and_adv_to_cust_mor_then_10_year().doubleValue());
            R44cell8.setCellStyle(numberStyle);
        } else {
            R44cell8.setCellValue("");
            R44cell8.setCellStyle(textStyle);
        }


// Row 45
        row = sheet.getRow(44);
        Cell R45cell1 = row.createCell(2);
        if (record.getR45_fix_rate_prop_and_eqp_up_to_1_mnt() != null) {
            R45cell1.setCellValue(record.getR45_fix_rate_prop_and_eqp_up_to_1_mnt().doubleValue());
            R45cell1.setCellStyle(numberStyle);
        } else {
            R45cell1.setCellValue("");
            R45cell1.setCellStyle(textStyle);
        }

        Cell R45cell2 = row.createCell(3);
        if (record.getR45_fix_rate_prop_and_eqp_mor_then_1_to_3_mon() != null) {
            R45cell2.setCellValue(record.getR45_fix_rate_prop_and_eqp_mor_then_1_to_3_mon().doubleValue());
            R45cell2.setCellStyle(numberStyle);
        } else {
            R45cell2.setCellValue("");
            R45cell2.setCellStyle(textStyle);
        }

        Cell R45cell3 = row.createCell(4);
        if (record.getR45_fix_rate_prop_and_eqp_mor_then_3_to_6_mon() != null) {
            R45cell3.setCellValue(record.getR45_fix_rate_prop_and_eqp_mor_then_3_to_6_mon().doubleValue());
            R45cell3.setCellStyle(numberStyle);
        } else {
            R45cell3.setCellValue("");
            R45cell3.setCellStyle(textStyle);
        }

        Cell R45cell4 = row.createCell(5);
        if (record.getR45_fix_rate_prop_and_eqp_mor_then_6_to_12_mon() != null) {
            R45cell4.setCellValue(record.getR45_fix_rate_prop_and_eqp_mor_then_6_to_12_mon().doubleValue());
            R45cell4.setCellStyle(numberStyle);
        } else {
            R45cell4.setCellValue("");
            R45cell4.setCellStyle(textStyle);
        }

        Cell R45cell5 = row.createCell(6);
        if (record.getR45_fix_rate_prop_and_eqp_mor_then_12_mon_to_3_year() != null) {
            R45cell5.setCellValue(record.getR45_fix_rate_prop_and_eqp_mor_then_12_mon_to_3_year().doubleValue());
            R45cell5.setCellStyle(numberStyle);
        } else {
            R45cell5.setCellValue("");
            R45cell5.setCellStyle(textStyle);
        }

        Cell R45cell6 = row.createCell(7);
        if (record.getR45_fix_rate_prop_and_eqp_mor_then_3_to_5_year() != null) {
            R45cell6.setCellValue(record.getR45_fix_rate_prop_and_eqp_mor_then_3_to_5_year().doubleValue());
            R45cell6.setCellStyle(numberStyle);
        } else {
            R45cell6.setCellValue("");
            R45cell6.setCellStyle(textStyle);
        }

        Cell R45cell7 = row.createCell(8);
        if (record.getR45_fix_rate_prop_and_eqp_mor_then_5_to_10_year() != null) {
            R45cell7.setCellValue(record.getR45_fix_rate_prop_and_eqp_mor_then_5_to_10_year().doubleValue());
            R45cell7.setCellStyle(numberStyle);
        } else {
            R45cell7.setCellValue("");
            R45cell7.setCellStyle(textStyle);
        }

        Cell R45cell8 = row.createCell(9);
        if (record.getR45_fix_rate_prop_and_eqp_mor_then_10_year() != null) {
            R45cell8.setCellValue(record.getR45_fix_rate_prop_and_eqp_mor_then_10_year().doubleValue());
            R45cell8.setCellStyle(numberStyle);
        } else {
            R45cell8.setCellValue("");
            R45cell8.setCellStyle(textStyle);
        }


// Row 46
        row = sheet.getRow(45);
        Cell R46cell1 = row.createCell(2);
        if (record.getR46_fix_rate_other_assets_specify_up_to_1_mnt() != null) {
            R46cell1.setCellValue(record.getR46_fix_rate_other_assets_specify_up_to_1_mnt().doubleValue());
            R46cell1.setCellStyle(numberStyle);
        } else {
            R46cell1.setCellValue("");
            R46cell1.setCellStyle(textStyle);
        }

        Cell R46cell2 = row.createCell(3);
        if (record.getR46_fix_rate_other_assets_specify_mor_then_1_to_3_mon() != null) {
            R46cell2.setCellValue(record.getR46_fix_rate_other_assets_specify_mor_then_1_to_3_mon().doubleValue());
            R46cell2.setCellStyle(numberStyle);
        } else {
            R46cell2.setCellValue("");
            R46cell2.setCellStyle(textStyle);
        }

        Cell R46cell3 = row.createCell(4);
        if (record.getR46_fix_rate_other_assets_specify_mor_then_3_to_6_mon() != null) {
            R46cell3.setCellValue(record.getR46_fix_rate_other_assets_specify_mor_then_3_to_6_mon().doubleValue());
            R46cell3.setCellStyle(numberStyle);
        } else {
            R46cell3.setCellValue("");
            R46cell3.setCellStyle(textStyle);
        }

        Cell R46cell4 = row.createCell(5);
        if (record.getR46_fix_rate_other_assets_specify_mor_then_6_to_12_mon() != null) {
            R46cell4.setCellValue(record.getR46_fix_rate_other_assets_specify_mor_then_6_to_12_mon().doubleValue());
            R46cell4.setCellStyle(numberStyle);
        } else {
            R46cell4.setCellValue("");
            R46cell4.setCellStyle(textStyle);
        }

        Cell R46cell5 = row.createCell(6);
        if (record.getR46_fix_rate_other_assets_specify_mor_then_12_mon_to_3_year() != null) {
            R46cell5.setCellValue(record.getR46_fix_rate_other_assets_specify_mor_then_12_mon_to_3_year().doubleValue());
            R46cell5.setCellStyle(numberStyle);
        } else {
            R46cell5.setCellValue("");
            R46cell5.setCellStyle(textStyle);
        }

        Cell R46cell6 = row.createCell(7);
        if (record.getR46_fix_rate_other_assets_specify_mor_then_3_to_5_year() != null) {
            R46cell6.setCellValue(record.getR46_fix_rate_other_assets_specify_mor_then_3_to_5_year().doubleValue());
            R46cell6.setCellStyle(numberStyle);
        } else {
            R46cell6.setCellValue("");
            R46cell6.setCellStyle(textStyle);
        }

        Cell R46cell7 = row.createCell(8);
        if (record.getR46_fix_rate_other_assets_specify_mor_then_5_to_10_year() != null) {
            R46cell7.setCellValue(record.getR46_fix_rate_other_assets_specify_mor_then_5_to_10_year().doubleValue());
            R46cell7.setCellStyle(numberStyle);
        } else {
            R46cell7.setCellValue("");
            R46cell7.setCellStyle(textStyle);
        }

        Cell R46cell8 = row.createCell(9);
        if (record.getR46_fix_rate_other_assets_specify_mor_then_10_year() != null) {
            R46cell8.setCellValue(record.getR46_fix_rate_other_assets_specify_mor_then_10_year().doubleValue());
            R46cell8.setCellStyle(numberStyle);
        } else {
            R46cell8.setCellValue("");
            R46cell8.setCellStyle(textStyle);
        }


// Row 47
        row = sheet.getRow(46);
        Cell R47cell1 = row.createCell(2);
        if (record.getR47_non_rate_sensitive_items_up_to_1_mnt() != null) {
            R47cell1.setCellValue(record.getR47_non_rate_sensitive_items_up_to_1_mnt().doubleValue());
            R47cell1.setCellStyle(numberStyle);
        } else {
            R47cell1.setCellValue("");
            R47cell1.setCellStyle(textStyle);
        }

        Cell R47cell2 = row.createCell(3);
        if (record.getR47_non_rate_sensitive_items_mor_then_1_to_3_mon() != null) {
            R47cell2.setCellValue(record.getR47_non_rate_sensitive_items_mor_then_1_to_3_mon().doubleValue());
            R47cell2.setCellStyle(numberStyle);
        } else {
            R47cell2.setCellValue("");
            R47cell2.setCellStyle(textStyle);
        }

        Cell R47cell3 = row.createCell(4);
        if (record.getR47_non_rate_sensitive_items_mor_then_3_to_6_mon() != null) {
            R47cell3.setCellValue(record.getR47_non_rate_sensitive_items_mor_then_3_to_6_mon().doubleValue());
            R47cell3.setCellStyle(numberStyle);
        } else {
            R47cell3.setCellValue("");
            R47cell3.setCellStyle(textStyle);
        }

        Cell R47cell4 = row.createCell(5);
        if (record.getR47_non_rate_sensitive_items_mor_then_6_to_12_mon() != null) {
            R47cell4.setCellValue(record.getR47_non_rate_sensitive_items_mor_then_6_to_12_mon().doubleValue());
            R47cell4.setCellStyle(numberStyle);
        } else {
            R47cell4.setCellValue("");
            R47cell4.setCellStyle(textStyle);
        }

        Cell R47cell5 = row.createCell(6);
        if (record.getR47_non_rate_sensitive_items_mor_then_12_mon_to_3_year() != null) {
            R47cell5.setCellValue(record.getR47_non_rate_sensitive_items_mor_then_12_mon_to_3_year().doubleValue());
            R47cell5.setCellStyle(numberStyle);
        } else {
            R47cell5.setCellValue("");
            R47cell5.setCellStyle(textStyle);
        }

        Cell R47cell6 = row.createCell(7);
        if (record.getR47_non_rate_sensitive_items_mor_then_3_to_5_year() != null) {
            R47cell6.setCellValue(record.getR47_non_rate_sensitive_items_mor_then_3_to_5_year().doubleValue());
            R47cell6.setCellStyle(numberStyle);
        } else {
            R47cell6.setCellValue("");
            R47cell6.setCellStyle(textStyle);
        }

        Cell R47cell7 = row.createCell(8);
        if (record.getR47_non_rate_sensitive_items_mor_then_5_to_10_year() != null) {
            R47cell7.setCellValue(record.getR47_non_rate_sensitive_items_mor_then_5_to_10_year().doubleValue());
            R47cell7.setCellStyle(numberStyle);
        } else {
            R47cell7.setCellValue("");
            R47cell7.setCellStyle(textStyle);
        }

        Cell R47cell8 = row.createCell(9);
        if (record.getR47_non_rate_sensitive_items_mor_then_10_year() != null) {
            R47cell8.setCellValue(record.getR47_non_rate_sensitive_items_mor_then_10_year().doubleValue());
            R47cell8.setCellStyle(numberStyle);
        } else {
            R47cell8.setCellValue("");
            R47cell8.setCellStyle(textStyle);
        }

        Cell R47cell9 = row.createCell(10);
        if (record.getR47_non_rate_sensitive_items_non_rat_sens_itm() != null) {
            R47cell9.setCellValue(record.getR47_non_rate_sensitive_items_non_rat_sens_itm().doubleValue());
            R47cell9.setCellStyle(numberStyle);
        } else {
            R47cell9.setCellValue("");
            R47cell9.setCellStyle(textStyle);
        }


// Row 48
        row = sheet.getRow(47);


        Cell R48cell9 = row.createCell(10);
        if (record.getR48_non_rate_sens_cash_non_rat_sens_itm() != null) {
            R48cell9.setCellValue(record.getR48_non_rate_sens_cash_non_rat_sens_itm().doubleValue());
            R48cell9.setCellStyle(numberStyle);
        } else {
            R48cell9.setCellValue("");
            R48cell9.setCellStyle(textStyle);
        }


// Row 49
        row = sheet.getRow(48);

        Cell R49cell9 = row.createCell(10);
        if (record.getR49_non_rate_sens_bal_bnk_of_botswana_non_rat_sens_itm() != null) {
            R49cell9.setCellValue(record.getR49_non_rate_sens_bal_bnk_of_botswana_non_rat_sens_itm().doubleValue());
            R49cell9.setCellStyle(numberStyle);
        } else {
            R49cell9.setCellValue("");
            R49cell9.setCellStyle(textStyle);
        }


// Row 50
        row = sheet.getRow(49);

        Cell R50cell9 = row.createCell(10);
        if (record.getR50_non_rate_sens_bal_doms_bnks_non_rat_sens_itm() != null) {
            R50cell9.setCellValue(record.getR50_non_rate_sens_bal_doms_bnks_non_rat_sens_itm().doubleValue());
            R50cell9.setCellStyle(numberStyle);
        } else {
            R50cell9.setCellValue("");
            R50cell9.setCellStyle(textStyle);
        }


// Row 51
        row = sheet.getRow(50);

        Cell R51cell9 = row.createCell(10);
        if (record.getR51_non_rate_sens_bal_foreign_bnks_non_rat_sens_itm() != null) {
            R51cell9.setCellValue(record.getR51_non_rate_sens_bal_foreign_bnks_non_rat_sens_itm().doubleValue());
            R51cell9.setCellStyle(numberStyle);
        } else {
            R51cell9.setCellValue("");
            R51cell9.setCellStyle(textStyle);
        }


// Row 52
        row = sheet.getRow(51);
        Cell R52cell9 = row.createCell(10);
        if (record.getR52_non_rate_sens_bal_related_comp_non_rat_sens_itm() != null) {
            R52cell9.setCellValue(record.getR52_non_rate_sens_bal_related_comp_non_rat_sens_itm().doubleValue());
            R52cell9.setCellStyle(numberStyle);
        } else {
            R52cell9.setCellValue("");
            R52cell9.setCellStyle(textStyle);
        }


// Row 53
        row = sheet.getRow(52);
        Cell R53cell9 = row.createCell(10);
        if (record.getR53_non_rate_sens_bnk_of_botswana_cert_non_rat_sens_itm() != null) {
            R53cell9.setCellValue(record.getR53_non_rate_sens_bnk_of_botswana_cert_non_rat_sens_itm().doubleValue());
            R53cell9.setCellStyle(numberStyle);
        } else {
            R53cell9.setCellValue("");
            R53cell9.setCellStyle(textStyle);
        }


    }

    public byte[] BRRS_M_IRBDetailExcel(String filename, String fromdate, String todate, String currency,
                                       String dtltype, String type, String version) {

        try {
            logger.info("Generating Excel for BRRS_M_IRB Details...");
            System.out.println("came to Detail download service");
            if (type.equals("ARCHIVAL") & version != null) {
                byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
                        version);
                return ARCHIVALreport;
            }
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("BRRS_IRBDetails");

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
            List<BRRS_M_IRB_Detail_Entity> reportData = brrs_m_irb_detail_repo.getdatabydateList(parsedToDate);
            if (reportData != null && !reportData.isEmpty()) {
                int rowIndex = 1;
                for (BRRS_M_IRB_Detail_Entity item : reportData) {
                    XSSFRow row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(item.getCust_id());
                    row.createCell(1).setCellValue(item.getAcct_number());
                    row.createCell(2).setCellValue(item.getAcct_name());
// ACCT BALANCE (right aligned, 3 decimal places)
                    Cell balanceCell = row.createCell(3);
                    if (item.getAcct_balance_in_pula() != null) {
                        balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
                    } else {
                        balanceCell.setCellValue(0.000);
                    }
                    balanceCell.setCellStyle(balanceStyle);
                    row.createCell(4).setCellValue(item.getRow_id());
                    row.createCell(5).setCellValue(item.getColumn_id());
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
                logger.info("No data found for BRRS_M_PI — only header will be written.");
            }
// Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();
            logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
            return bos.toByteArray();
        } catch (Exception e) {
            logger.error("Error generating BRRS_M_PI Excel", e);
            return new byte[0];
        }
    }

    public List<Object> getM_IRBArchival() {
        List<Object> M_PIArchivallist = new ArrayList<>();
        try {
            M_PIArchivallist = M_IRB_Archival_Summary_Repo_1.getM_PIarchival();
            System.out.println("countser" + M_PIArchivallist.size());
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error fetching M_PI Archival data: " + e.getMessage());
            e.printStackTrace();

            // Optionally, you can rethrow it or return empty list
            // throw new RuntimeException("Failed to fetch data", e);
        }
        return M_PIArchivallist;
    }

    public byte[] getExcelM_PIARCHIVAL(String filename, String reportId, String fromdate, String todate,
                                       String currency, String dtltype, String type, String version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");
        if (type.equals("ARCHIVAL") & version != null) {

        }
        List<M_IRB_Archival_Summary_Entity2> dataList = M_IRB_Archival_Summary_Repo_1.getdatabydateListarchival(dateformat.parse(todate), version);
        List<M_IRB_Archival_Summary_Entity1> dataList2 = M_IRB_Archival_Summary_Repo_2.getdatabydateListarchival(dateformat.parse(todate), version);

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found for M_PI report. Returning empty result.");
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
            int startRow = 7;

            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {
                    M_IRB_Archival_Summary_Entity2 record = dataList.get(i);
                    System.out.println("rownumber=" + startRow + i);
                    Row row = sheet.getRow(startRow + i);
                    if (row == null) {
                        row = sheet.createRow(startRow + i);
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

    public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
                                         String dtltype, String type, String version) {
        try {
            logger.info("Generating Excel for BRRS_M_PI ARCHIVAL Details...");
            System.out.println("came to Detail download service");
            if (type.equals("ARCHIVAL") & version != null) {

            }
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("MSFinP2Detail");

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
            List<BRRS_M_IRB_Detail_Archival_Entity> reportData = M_IRB_Archival_Detail_Repo.getdatabydateList(parsedToDate,
                    version);
            System.out.println("Size");
            System.out.println(reportData.size());
            if (reportData != null && !reportData.isEmpty()) {
                int rowIndex = 1;
                for (BRRS_M_IRB_Detail_Archival_Entity item : reportData) {
                    XSSFRow row = sheet.createRow(rowIndex++);

                    row.createCell(0).setCellValue(item.getCust_id());
                    row.createCell(1).setCellValue(item.getAcct_number());
                    row.createCell(2).setCellValue(item.getAcct_name());

                    // ACCT BALANCE (right aligned, 3 decimal places)
                    Cell balanceCell = row.createCell(3);
                    if (item.getAcct_balance_in_pula() != null) {
                        balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
                    } else {
                        balanceCell.setCellValue(0.000);
                    }
                    balanceCell.setCellStyle(balanceStyle);

                    row.createCell(4).setCellValue(item.getRow_id());
                    row.createCell(5).setCellValue(item.getColumn_id());
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
                logger.info("No data found for BRRS_M_PI — only header will be written.");
            }

            // Write to byte[]
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
            return bos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generating BRRS_M_PIExcel", e);
            return new byte[0];
        }
    }
}
