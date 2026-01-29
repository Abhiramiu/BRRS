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
import com.bornfire.brrs.entities.BRRS_M_SRWA_12H_New_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12H_New_Detail_Repo;
import com.bornfire.brrs.entities.M_SRWA_12H_New_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12H_New_Summary_Repo;
import com.bornfire.brrs.entities.M_SRWA_12H_New_Archival_Summary_Entity;

@Component
@Service

public class BRRS_M_SRWA_12H_New_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12H_New_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_SRWA_12H_New_Detail_Repo M_SRWA_12H_New_DETAIL_Repo;

	@Autowired
	BRRS_M_SRWA_12H_New_Summary_Repo M_SRWA_12H_New_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12H_New_Archival_Summary_Repo M_SRWA_12H_New_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SRWA_12H_NewView(String reportId, String fromdate, String todate,
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
            List<M_SRWA_12H_New_Archival_Summary_Entity> T1Master = 
                M_SRWA_12H_New_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<M_SRWA_12H_New_Archival_Summary_Entity> T1Master =
                M_SRWA_12H_New_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<M_SRWA_12H_New_Summary_Entity> T1Master = 
                M_SRWA_12H_New_Summary_Repo.getdatabydateList(dateformat.parse(todate));
            System.out.println("T1Master Size "+T1Master.size());
            mv.addObject("reportsummary", T1Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12H_New");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport(M_SRWA_12H_New_Summary_Entity updatedEntity) {
		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_SRWA_12H_New_Summary_Entity existing = M_SRWA_12H_New_Summary_Repo
				.findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		try {
			// 1️⃣ Loop from R11 to R15 and copy fields
			for (int i = 12; i <= 81; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "PRODUCT", "ISSUER", "ISSUES_RATING", "1YR_VAL_OF_CRM", "1YR_5YR_VAL_OF_CRM",
						"5YR_VAL_OF_CRM", "OTHER", "STD_SUPERVISORY_HAIRCUT", "APPLICABLE_RISK_WEIGHT" };

				for (String field : fields) {
					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;

					try {
						Method getter = M_SRWA_12H_New_Summary_Entity.class.getMethod(getterName);
						Method setter = M_SRWA_12H_New_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
		M_SRWA_12H_New_Summary_Repo.save(existing);

	}

	// Download For Summary
	public byte[] BRRS_M_SRWA_12H_NewExcel(String filename, String reportId,
			String fromdate, String todate,
			String currency, String dtltype,
			String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

    // ARCHIVAL check
    if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
        logger.info("Service: Generating ARCHIVAL report for version {}", version);
        return BRRS_M_SRWA_12H_NewArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }
    // RESUB check
    // else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
    //     logger.info("Service: Generating RESUB report for version {}", version);

       
    //     List<M_SRWA_12H_New_Archival_Summary_Entity> T1Master =
    //             M_SRWA_12H_New_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

    //     // Generate Excel for RESUB
    //     return BRRS_M_SRWA_12H_NewResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    // }




		// Default (LIVE) case
		List<M_SRWA_12H_New_Summary_Entity> dataList = M_SRWA_12H_New_Summary_Repo.getdatabydateList(reportDate);

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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_SRWA_12H_New_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column C
					Cell cell2 = row.createCell(1);
					if (record.getR12_ISSUER() != null) {
						cell2.setCellValue(record.getR12_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(2);
					if (record.getR12_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR12_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row12
					// Column E
					Cell cell4 = row.createCell(3);
					if (record.getR12_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR12_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row12
					// Column F
					Cell cell5 = row.createCell(4);
					if (record.getR12_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR12_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row12
					// Column G
					Cell cell6 = row.createCell(5);
					if (record.getR12_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR12_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row12
					// Column H
					Cell cell7 = row.createCell(6);
					if (record.getR12_OTHER() != null) {
						cell7.setCellValue(record.getR12_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row12
					// Column I
					Cell cell8 = row.createCell(7);
					if (record.getR12_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR12_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row12
					// Column J
					Cell cell9 = row.createCell(8);
					if (record.getR12_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR12_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row13
					row = sheet.getRow(12);
					// row13
					// Column C
					cell2 = row.createCell(1);
					if (record.getR13_ISSUER() != null) {
						cell2.setCellValue(record.getR13_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(2);
					if (record.getR13_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR13_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(3);
					if (record.getR13_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR13_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row13
					// Column F
					cell5 = row.createCell(4);
					if (record.getR13_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR13_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row13
					// Column G
					cell6 = row.createCell(5);
					if (record.getR13_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR13_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(6);
					if (record.getR13_OTHER() != null) {
						cell7.setCellValue(record.getR13_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row13
					// Column I
					cell8 = row.createCell(7);
					if (record.getR13_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR13_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row13
					// Column J
					cell9 = row.createCell(8);
					if (record.getR13_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR13_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row14
					row = sheet.getRow(13);
					// row14
					// Column C
					cell2 = row.createCell(1);
					if (record.getR14_ISSUER() != null) {
						cell2.setCellValue(record.getR14_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					// Column D
					cell3 = row.createCell(2);
					if (record.getR14_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR14_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row14
					// Column E
					cell4 = row.createCell(3);
					if (record.getR14_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR14_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row14
					// Column F
					cell5 = row.createCell(4);
					if (record.getR14_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR14_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row14
					// Column G
					cell6 = row.createCell(5);
					if (record.getR14_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR14_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row14
					// Column H
					cell7 = row.createCell(6);
					if (record.getR14_OTHER() != null) {
						cell7.setCellValue(record.getR14_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row14
					// Column I
					cell8 = row.createCell(7);
					if (record.getR14_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR14_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row14
					// Column J
					cell9 = row.createCell(8);
					if (record.getR14_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR14_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row15
					row = sheet.getRow(14);
					// row15
					// Column C
					cell2 = row.createCell(1);
					if (record.getR15_ISSUER() != null) {
						cell2.setCellValue(record.getR15_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					// Column D
					cell3 = row.createCell(2);
					if (record.getR15_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR15_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row15
					// Column E
					cell4 = row.createCell(3);
					if (record.getR15_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR15_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row15
					// Column F
					cell5 = row.createCell(4);
					if (record.getR15_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR15_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row15
					// Column G
					cell6 = row.createCell(5);
					if (record.getR15_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR15_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row15
					// Column H
					cell7 = row.createCell(6);
					if (record.getR15_OTHER() != null) {
						cell7.setCellValue(record.getR15_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row15
					// Column I
					cell8 = row.createCell(7);
					if (record.getR15_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR15_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row15
					// Column J
					cell9 = row.createCell(8);
					if (record.getR15_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR15_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row16
					row = sheet.getRow(15);
					// row16
					// Column C
					cell2 = row.createCell(1);
					if (record.getR16_ISSUER() != null) {
						cell2.setCellValue(record.getR16_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					// Column D
					cell3 = row.createCell(2);
					if (record.getR16_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR16_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row16
					// Column E
					cell4 = row.createCell(3);
					if (record.getR16_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR16_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row16
					// Column F
					cell5 = row.createCell(4);
					if (record.getR16_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR16_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row16
					// Column G
					cell6 = row.createCell(5);
					if (record.getR16_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR16_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row16
					// Column H
					cell7 = row.createCell(6);
					if (record.getR16_OTHER() != null) {
						cell7.setCellValue(record.getR16_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row16
					// Column I
					cell8 = row.createCell(7);
					if (record.getR16_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR16_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row16
					// Column J
					cell9 = row.createCell(8);
					if (record.getR16_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR16_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row17
					row = sheet.getRow(16);
					// row17
					// Column C
					cell2 = row.createCell(1);
					if (record.getR17_ISSUER() != null) {
						cell2.setCellValue(record.getR17_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					// Column D
					cell3 = row.createCell(2);
					if (record.getR17_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR17_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row17
					// Column E
					cell4 = row.createCell(3);
					if (record.getR17_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR17_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row17
					// Column F
					cell5 = row.createCell(4);
					if (record.getR17_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR17_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row17
					// Column G
					cell6 = row.createCell(5);
					if (record.getR17_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR17_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row17
					// Column H
					cell7 = row.createCell(6);
					if (record.getR17_OTHER() != null) {
						cell7.setCellValue(record.getR17_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row17
					// Column I
					cell8 = row.createCell(7);
					if (record.getR17_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR17_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row17
					// Column J
					cell9 = row.createCell(8);
					if (record.getR17_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR17_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row18
					row = sheet.getRow(17);
					// row18
					// Column C
					cell2 = row.createCell(1);
					if (record.getR18_ISSUER() != null) {
						cell2.setCellValue(record.getR18_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					// Column D
					cell3 = row.createCell(2);
					if (record.getR18_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR18_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row18
					// Column E
					cell4 = row.createCell(3);
					if (record.getR18_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR18_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row18
					// Column F
					cell5 = row.createCell(4);
					if (record.getR18_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR18_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row18
					// Column G
					cell6 = row.createCell(5);
					if (record.getR18_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR18_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row18
					// Column H
					cell7 = row.createCell(6);
					if (record.getR18_OTHER() != null) {
						cell7.setCellValue(record.getR18_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row18
					// Column I
					cell8 = row.createCell(7);
					if (record.getR18_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR18_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row18
					// Column J
					cell9 = row.createCell(8);
					if (record.getR18_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR18_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row19
					row = sheet.getRow(18);
					// row19
					// Column C
					cell2 = row.createCell(1);
					if (record.getR19_ISSUER() != null) {
						cell2.setCellValue(record.getR19_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					// Column D
					cell3 = row.createCell(2);
					if (record.getR19_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR19_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row19
					// Column E
					cell4 = row.createCell(3);
					if (record.getR19_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR19_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row19
					// Column F
					cell5 = row.createCell(4);
					if (record.getR19_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR19_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row19
					// Column G
					cell6 = row.createCell(5);
					if (record.getR19_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR19_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row19
					// Column H
					cell7 = row.createCell(6);
					if (record.getR19_OTHER() != null) {
						cell7.setCellValue(record.getR19_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row19
					// Column I
					cell8 = row.createCell(7);
					if (record.getR19_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR19_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row19
					// Column J
					cell9 = row.createCell(8);
					if (record.getR19_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR19_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row20
					row = sheet.getRow(19);
					// row20
					// Column C
					cell2 = row.createCell(1);
					if (record.getR20_ISSUER() != null) {
						cell2.setCellValue(record.getR20_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					// Column D
					cell3 = row.createCell(2);
					if (record.getR20_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR20_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row20
					// Column E
					cell4 = row.createCell(3);
					if (record.getR20_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR20_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row20
					// Column F
					cell5 = row.createCell(4);
					if (record.getR20_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR20_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row20
					// Column G
					cell6 = row.createCell(5);
					if (record.getR20_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR20_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row20
					// Column H
					cell7 = row.createCell(6);
					if (record.getR20_OTHER() != null) {
						cell7.setCellValue(record.getR20_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row20
					// Column I
					cell8 = row.createCell(7);
					if (record.getR20_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR20_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row20
					// Column J
					cell9 = row.createCell(8);
					if (record.getR20_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR20_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row21
					row = sheet.getRow(20);
					// row21
					// Column C
					cell2 = row.createCell(1);
					if (record.getR21_ISSUER() != null) {
						cell2.setCellValue(record.getR21_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row21
					// Column D
					cell3 = row.createCell(2);
					if (record.getR21_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR21_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row21
					// Column E
					cell4 = row.createCell(3);
					if (record.getR21_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR21_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row21
					// Column F
					cell5 = row.createCell(4);
					if (record.getR21_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR21_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row21
					// Column G
					cell6 = row.createCell(5);
					if (record.getR21_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR21_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row21
					// Column H
					cell7 = row.createCell(6);
					if (record.getR21_OTHER() != null) {
						cell7.setCellValue(record.getR21_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row21
					// Column I
					cell8 = row.createCell(7);
					if (record.getR21_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR21_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row21
					// Column J
					cell9 = row.createCell(8);
					if (record.getR21_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR21_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row22
					row = sheet.getRow(21);
					// row22
					// Column C
					cell2 = row.createCell(1);
					if (record.getR22_ISSUER() != null) {
						cell2.setCellValue(record.getR22_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row22
					// Column D
					cell3 = row.createCell(2);
					if (record.getR22_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR22_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row22
					// Column E
					cell4 = row.createCell(3);
					if (record.getR22_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR22_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row22
					// Column F
					cell5 = row.createCell(4);
					if (record.getR22_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR22_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row22
					// Column G
					cell6 = row.createCell(5);
					if (record.getR22_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR22_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row22
					// Column H
					cell7 = row.createCell(6);
					if (record.getR22_OTHER() != null) {
						cell7.setCellValue(record.getR22_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row22
					// Column I
					cell8 = row.createCell(7);
					if (record.getR22_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR22_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row22
					// Column J
					cell9 = row.createCell(8);
					if (record.getR22_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR22_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row23
					row = sheet.getRow(22);
					// row23
					// Column C
					cell2 = row.createCell(1);
					if (record.getR23_ISSUER() != null) {
						cell2.setCellValue(record.getR23_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					// Column D
					cell3 = row.createCell(2);
					if (record.getR23_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR23_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row23
					// Column E
					cell4 = row.createCell(3);
					if (record.getR23_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR23_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row23
					// Column F
					cell5 = row.createCell(4);
					if (record.getR23_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR23_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row23
					// Column G
					cell6 = row.createCell(5);
					if (record.getR23_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR23_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row23
					// Column H
					cell7 = row.createCell(6);
					if (record.getR23_OTHER() != null) {
						cell7.setCellValue(record.getR23_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row23
					// Column I
					cell8 = row.createCell(7);
					if (record.getR23_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR23_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row23
					// Column J
					cell9 = row.createCell(8);
					if (record.getR23_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR23_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row24
					row = sheet.getRow(23);
					// row24
					// Column C
					cell2 = row.createCell(1);
					if (record.getR24_ISSUER() != null) {
						cell2.setCellValue(record.getR24_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					// Column D
					cell3 = row.createCell(2);
					if (record.getR24_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR24_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row24
					// Column E
					cell4 = row.createCell(3);
					if (record.getR24_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR24_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row24
					// Column F
					cell5 = row.createCell(4);
					if (record.getR24_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR24_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row24
					// Column G
					cell6 = row.createCell(5);
					if (record.getR24_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR24_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row24
					// Column H
					cell7 = row.createCell(6);
					if (record.getR24_OTHER() != null) {
						cell7.setCellValue(record.getR24_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row24
					// Column I
					cell8 = row.createCell(7);
					if (record.getR24_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR24_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row24
					// Column J
					cell9 = row.createCell(8);
					if (record.getR24_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR24_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row25
					row = sheet.getRow(24);
					// row25
					// Column C
					cell2 = row.createCell(1);
					if (record.getR25_ISSUER() != null) {
						cell2.setCellValue(record.getR25_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					// Column D
					cell3 = row.createCell(2);
					if (record.getR25_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR25_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row25
					// Column E
					cell4 = row.createCell(3);
					if (record.getR25_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR25_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row25
					// Column F
					cell5 = row.createCell(4);
					if (record.getR25_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR25_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row25
					// Column G
					cell6 = row.createCell(5);
					if (record.getR25_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR25_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row25
					// Column H
					cell7 = row.createCell(6);
					if (record.getR25_OTHER() != null) {
						cell7.setCellValue(record.getR25_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row25
					// Column I
					cell8 = row.createCell(7);
					if (record.getR25_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR25_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row25
					// Column J
					cell9 = row.createCell(8);
					if (record.getR25_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR25_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row26
					row = sheet.getRow(25);
					// row26
					// Column C
					cell2 = row.createCell(1);
					if (record.getR26_ISSUER() != null) {
						cell2.setCellValue(record.getR26_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					// Column D
					cell3 = row.createCell(2);
					if (record.getR26_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR26_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row26
					// Column E
					cell4 = row.createCell(3);
					if (record.getR26_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR26_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row26
					// Column F
					cell5 = row.createCell(4);
					if (record.getR26_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR26_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row26
					// Column G
					cell6 = row.createCell(5);
					if (record.getR26_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR26_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row26
					// Column H
					cell7 = row.createCell(6);
					if (record.getR26_OTHER() != null) {
						cell7.setCellValue(record.getR26_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row26
					// Column I
					cell8 = row.createCell(7);
					if (record.getR26_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR26_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row26
					// Column J
					cell9 = row.createCell(8);
					if (record.getR26_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR26_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row27
					row = sheet.getRow(26);
					// row27
					// Column C
					cell2 = row.createCell(1);
					if (record.getR27_ISSUER() != null) {
						cell2.setCellValue(record.getR27_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					// Column D
					cell3 = row.createCell(2);
					if (record.getR27_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR27_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row27
					// Column E
					cell4 = row.createCell(3);
					if (record.getR27_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR27_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row27
					// Column F
					cell5 = row.createCell(4);
					if (record.getR27_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR27_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row27
					// Column G
					cell6 = row.createCell(5);
					if (record.getR27_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR27_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row27
					// Column H
					cell7 = row.createCell(6);
					if (record.getR27_OTHER() != null) {
						cell7.setCellValue(record.getR27_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row27
					// Column I
					cell8 = row.createCell(7);
					if (record.getR27_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR27_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row27
					// Column J
					cell9 = row.createCell(8);
					if (record.getR27_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR27_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row28
					row = sheet.getRow(27);
					// row28
					// Column C
					cell2 = row.createCell(1);
					if (record.getR28_ISSUER() != null) {
						cell2.setCellValue(record.getR28_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					// Column D
					cell3 = row.createCell(2);
					if (record.getR28_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR28_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row28
					// Column E
					cell4 = row.createCell(3);
					if (record.getR28_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR28_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row28
					// Column F
					cell5 = row.createCell(4);
					if (record.getR28_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR28_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row28
					// Column G
					cell6 = row.createCell(5);
					if (record.getR28_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR28_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row28
					// Column H
					cell7 = row.createCell(6);
					if (record.getR28_OTHER() != null) {
						cell7.setCellValue(record.getR28_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row28
					// Column I
					cell8 = row.createCell(7);
					if (record.getR28_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR28_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row28
					// Column J
					cell9 = row.createCell(8);
					if (record.getR28_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR28_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row29
					row = sheet.getRow(28);
					// row29
					// Column C
					cell2 = row.createCell(1);
					if (record.getR29_ISSUER() != null) {
						cell2.setCellValue(record.getR29_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row29
					// Column D
					cell3 = row.createCell(2);
					if (record.getR29_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR29_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row29
					// Column E
					cell4 = row.createCell(3);
					if (record.getR29_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR29_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row29
					// Column F
					cell5 = row.createCell(4);
					if (record.getR29_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR29_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row29
					// Column G
					cell6 = row.createCell(5);
					if (record.getR29_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR29_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row29
					// Column H
					cell7 = row.createCell(6);
					if (record.getR29_OTHER() != null) {
						cell7.setCellValue(record.getR29_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row29
					// Column I
					cell8 = row.createCell(7);
					if (record.getR29_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR29_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row29
					// Column J
					cell9 = row.createCell(8);
					if (record.getR29_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR29_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row30
					row = sheet.getRow(29);
					// row30
					// Column C
					cell2 = row.createCell(1);
					if (record.getR30_ISSUER() != null) {
						cell2.setCellValue(record.getR30_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					// Column D
					cell3 = row.createCell(2);
					if (record.getR30_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR30_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row30
					// Column E
					cell4 = row.createCell(3);
					if (record.getR30_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR30_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row30
					// Column F
					cell5 = row.createCell(4);
					if (record.getR30_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR30_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row30
					// Column G
					cell6 = row.createCell(5);
					if (record.getR30_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR30_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row30
					// Column H
					cell7 = row.createCell(6);
					if (record.getR30_OTHER() != null) {
						cell7.setCellValue(record.getR30_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row30
					// Column I
					cell8 = row.createCell(7);
					if (record.getR30_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR30_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row30
					// Column J
					cell9 = row.createCell(8);
					if (record.getR30_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR30_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row31
					row = sheet.getRow(30);
					// row31
					// Column C
					cell2 = row.createCell(1);
					if (record.getR31_ISSUER() != null) {
						cell2.setCellValue(record.getR31_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row31
					// Column D
					cell3 = row.createCell(2);
					if (record.getR31_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR31_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row31
					// Column E
					cell4 = row.createCell(3);
					if (record.getR31_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR31_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row31
					// Column F
					cell5 = row.createCell(4);
					if (record.getR31_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR31_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row31
					// Column G
					cell6 = row.createCell(5);
					if (record.getR31_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR31_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row31
					// Column H
					cell7 = row.createCell(6);
					if (record.getR31_OTHER() != null) {
						cell7.setCellValue(record.getR31_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row31
					// Column I
					cell8 = row.createCell(7);
					if (record.getR31_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR31_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row31
					// Column J
					cell9 = row.createCell(8);
					if (record.getR31_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR31_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row32
					row = sheet.getRow(31);
					// row32
					// Column C
					cell2 = row.createCell(1);
					if (record.getR32_ISSUER() != null) {
						cell2.setCellValue(record.getR32_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row32
					// Column D
					cell3 = row.createCell(2);
					if (record.getR32_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR32_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row32
					// Column E
					cell4 = row.createCell(3);
					if (record.getR32_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR32_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row32
					// Column F
					cell5 = row.createCell(4);
					if (record.getR32_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR32_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row32
					// Column G
					cell6 = row.createCell(5);
					if (record.getR32_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR32_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row32
					// Column H
					cell7 = row.createCell(6);
					if (record.getR32_OTHER() != null) {
						cell7.setCellValue(record.getR32_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row32
					// Column I
					cell8 = row.createCell(7);
					if (record.getR32_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR32_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row32
					// Column J
					cell9 = row.createCell(8);
					if (record.getR32_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR32_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row33
					row = sheet.getRow(32);
					// row33
					// Column C
					cell2 = row.createCell(1);
					if (record.getR33_ISSUER() != null) {
						cell2.setCellValue(record.getR33_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(2);
					if (record.getR33_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR33_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(3);
					if (record.getR33_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR33_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(4);
					if (record.getR33_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR33_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(5);
					if (record.getR33_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR33_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(6);
					if (record.getR33_OTHER() != null) {
						cell7.setCellValue(record.getR33_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(7);
					if (record.getR33_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR33_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(8);
					if (record.getR33_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR33_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row34
					row = sheet.getRow(33);

					// row34
					// Column C
					cell2 = row.createCell(1);
					if (record.getR34_ISSUER() != null) {
						cell2.setCellValue(record.getR34_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(2);
					if (record.getR34_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR34_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(3);
					if (record.getR34_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR34_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(4);
					if (record.getR34_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR34_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(5);
					if (record.getR34_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR34_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(6);
					if (record.getR34_OTHER() != null) {
						cell7.setCellValue(record.getR34_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(7);
					if (record.getR34_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR34_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(8);
					if (record.getR34_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR34_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row35
					row = sheet.getRow(34);

					// row35
					// Column C
					cell2 = row.createCell(1);
					if (record.getR35_ISSUER() != null) {
						cell2.setCellValue(record.getR35_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(2);
					if (record.getR35_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR35_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(3);
					if (record.getR35_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR35_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(4);
					if (record.getR35_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR35_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(5);
					if (record.getR35_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR35_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(6);
					if (record.getR35_OTHER() != null) {
						cell7.setCellValue(record.getR35_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(7);
					if (record.getR35_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR35_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(8);
					if (record.getR35_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR35_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row36
					row = sheet.getRow(35);

					// row36
					// Column C
					cell2 = row.createCell(1);
					if (record.getR36_ISSUER() != null) {
						cell2.setCellValue(record.getR36_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(2);
					if (record.getR36_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR36_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(3);
					if (record.getR36_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR36_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(4);
					if (record.getR36_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR36_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(5);
					if (record.getR36_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR36_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(6);
					if (record.getR36_OTHER() != null) {
						cell7.setCellValue(record.getR36_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(7);
					if (record.getR36_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR36_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(8);
					if (record.getR36_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR36_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row37
					row = sheet.getRow(36);

					// row37
					// Column C
					cell2 = row.createCell(1);
					if (record.getR37_ISSUER() != null) {
						cell2.setCellValue(record.getR37_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(2);
					if (record.getR37_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR37_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(3);
					if (record.getR37_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR37_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(4);
					if (record.getR37_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR37_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(5);
					if (record.getR37_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR37_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(6);
					if (record.getR37_OTHER() != null) {
						cell7.setCellValue(record.getR37_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(7);
					if (record.getR37_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR37_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(8);
					if (record.getR37_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR37_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					
				}
				// workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

	public byte[] BRRS_M_SRWA_12H_NewArchivalExcel(String filename, String reportId, String fromdate,
			String todate, String currency, String dtltype,
			String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_SRWA_12H_New_Archival_Summary_Entity> dataList = M_SRWA_12H_New_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12H_New report. Returning empty result.");
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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_SRWA_12H_New_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column C
					Cell cell2 = row.createCell(1);
					if (record.getR12_ISSUER() != null) {
						cell2.setCellValue(record.getR12_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(2);
					if (record.getR12_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR12_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row12
					// Column E
					Cell cell4 = row.createCell(3);
					if (record.getR12_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR12_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row12
					// Column F
					Cell cell5 = row.createCell(4);
					if (record.getR12_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR12_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row12
					// Column G
					Cell cell6 = row.createCell(5);
					if (record.getR12_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR12_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row12
					// Column H
					Cell cell7 = row.createCell(6);
					if (record.getR12_OTHER() != null) {
						cell7.setCellValue(record.getR12_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row12
					// Column I
					Cell cell8 = row.createCell(7);
					if (record.getR12_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR12_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row12
					// Column J
					Cell cell9 = row.createCell(8);
					if (record.getR12_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR12_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row13
					row = sheet.getRow(12);
					// row13
					// Column C
					cell2 = row.createCell(1);
					if (record.getR13_ISSUER() != null) {
						cell2.setCellValue(record.getR13_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(2);
					if (record.getR13_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR13_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(3);
					if (record.getR13_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR13_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row13
					// Column F
					cell5 = row.createCell(4);
					if (record.getR13_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR13_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row13
					// Column G
					cell6 = row.createCell(5);
					if (record.getR13_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR13_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(6);
					if (record.getR13_OTHER() != null) {
						cell7.setCellValue(record.getR13_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row13
					// Column I
					cell8 = row.createCell(7);
					if (record.getR13_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR13_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row13
					// Column J
					cell9 = row.createCell(8);
					if (record.getR13_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR13_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row14
					row = sheet.getRow(13);
					// row14
					// Column C
					cell2 = row.createCell(1);
					if (record.getR14_ISSUER() != null) {
						cell2.setCellValue(record.getR14_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					// Column D
					cell3 = row.createCell(2);
					if (record.getR14_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR14_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row14
					// Column E
					cell4 = row.createCell(3);
					if (record.getR14_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR14_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row14
					// Column F
					cell5 = row.createCell(4);
					if (record.getR14_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR14_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row14
					// Column G
					cell6 = row.createCell(5);
					if (record.getR14_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR14_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row14
					// Column H
					cell7 = row.createCell(6);
					if (record.getR14_OTHER() != null) {
						cell7.setCellValue(record.getR14_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row14
					// Column I
					cell8 = row.createCell(7);
					if (record.getR14_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR14_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row14
					// Column J
					cell9 = row.createCell(8);
					if (record.getR14_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR14_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row15
					row = sheet.getRow(14);
					// row15
					// Column C
					cell2 = row.createCell(1);
					if (record.getR15_ISSUER() != null) {
						cell2.setCellValue(record.getR15_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					// Column D
					cell3 = row.createCell(2);
					if (record.getR15_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR15_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row15
					// Column E
					cell4 = row.createCell(3);
					if (record.getR15_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR15_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row15
					// Column F
					cell5 = row.createCell(4);
					if (record.getR15_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR15_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row15
					// Column G
					cell6 = row.createCell(5);
					if (record.getR15_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR15_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row15
					// Column H
					cell7 = row.createCell(6);
					if (record.getR15_OTHER() != null) {
						cell7.setCellValue(record.getR15_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row15
					// Column I
					cell8 = row.createCell(7);
					if (record.getR15_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR15_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row15
					// Column J
					cell9 = row.createCell(8);
					if (record.getR15_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR15_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row16
					row = sheet.getRow(15);
					// row16
					// Column C
					cell2 = row.createCell(1);
					if (record.getR16_ISSUER() != null) {
						cell2.setCellValue(record.getR16_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					// Column D
					cell3 = row.createCell(2);
					if (record.getR16_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR16_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row16
					// Column E
					cell4 = row.createCell(3);
					if (record.getR16_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR16_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row16
					// Column F
					cell5 = row.createCell(4);
					if (record.getR16_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR16_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row16
					// Column G
					cell6 = row.createCell(5);
					if (record.getR16_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR16_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row16
					// Column H
					cell7 = row.createCell(6);
					if (record.getR16_OTHER() != null) {
						cell7.setCellValue(record.getR16_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row16
					// Column I
					cell8 = row.createCell(7);
					if (record.getR16_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR16_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row16
					// Column J
					cell9 = row.createCell(8);
					if (record.getR16_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR16_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row17
					row = sheet.getRow(16);
					// row17
					// Column C
					cell2 = row.createCell(1);
					if (record.getR17_ISSUER() != null) {
						cell2.setCellValue(record.getR17_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					// Column D
					cell3 = row.createCell(2);
					if (record.getR17_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR17_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row17
					// Column E
					cell4 = row.createCell(3);
					if (record.getR17_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR17_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row17
					// Column F
					cell5 = row.createCell(4);
					if (record.getR17_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR17_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row17
					// Column G
					cell6 = row.createCell(5);
					if (record.getR17_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR17_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row17
					// Column H
					cell7 = row.createCell(6);
					if (record.getR17_OTHER() != null) {
						cell7.setCellValue(record.getR17_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row17
					// Column I
					cell8 = row.createCell(7);
					if (record.getR17_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR17_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row17
					// Column J
					cell9 = row.createCell(8);
					if (record.getR17_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR17_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row18
					row = sheet.getRow(17);
					// row18
					// Column C
					cell2 = row.createCell(1);
					if (record.getR18_ISSUER() != null) {
						cell2.setCellValue(record.getR18_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					// Column D
					cell3 = row.createCell(2);
					if (record.getR18_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR18_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row18
					// Column E
					cell4 = row.createCell(3);
					if (record.getR18_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR18_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row18
					// Column F
					cell5 = row.createCell(4);
					if (record.getR18_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR18_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row18
					// Column G
					cell6 = row.createCell(5);
					if (record.getR18_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR18_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row18
					// Column H
					cell7 = row.createCell(6);
					if (record.getR18_OTHER() != null) {
						cell7.setCellValue(record.getR18_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row18
					// Column I
					cell8 = row.createCell(7);
					if (record.getR18_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR18_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row18
					// Column J
					cell9 = row.createCell(8);
					if (record.getR18_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR18_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row19
					row = sheet.getRow(18);
					// row19
					// Column C
					cell2 = row.createCell(1);
					if (record.getR19_ISSUER() != null) {
						cell2.setCellValue(record.getR19_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					// Column D
					cell3 = row.createCell(2);
					if (record.getR19_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR19_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row19
					// Column E
					cell4 = row.createCell(3);
					if (record.getR19_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR19_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row19
					// Column F
					cell5 = row.createCell(4);
					if (record.getR19_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR19_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row19
					// Column G
					cell6 = row.createCell(5);
					if (record.getR19_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR19_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row19
					// Column H
					cell7 = row.createCell(6);
					if (record.getR19_OTHER() != null) {
						cell7.setCellValue(record.getR19_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row19
					// Column I
					cell8 = row.createCell(7);
					if (record.getR19_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR19_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row19
					// Column J
					cell9 = row.createCell(8);
					if (record.getR19_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR19_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row20
					row = sheet.getRow(19);
					// row20
					// Column C
					cell2 = row.createCell(1);
					if (record.getR20_ISSUER() != null) {
						cell2.setCellValue(record.getR20_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					// Column D
					cell3 = row.createCell(2);
					if (record.getR20_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR20_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row20
					// Column E
					cell4 = row.createCell(3);
					if (record.getR20_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR20_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row20
					// Column F
					cell5 = row.createCell(4);
					if (record.getR20_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR20_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row20
					// Column G
					cell6 = row.createCell(5);
					if (record.getR20_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR20_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row20
					// Column H
					cell7 = row.createCell(6);
					if (record.getR20_OTHER() != null) {
						cell7.setCellValue(record.getR20_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row20
					// Column I
					cell8 = row.createCell(7);
					if (record.getR20_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR20_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row20
					// Column J
					cell9 = row.createCell(8);
					if (record.getR20_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR20_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row21
					row = sheet.getRow(20);
					// row21
					// Column C
					cell2 = row.createCell(1);
					if (record.getR21_ISSUER() != null) {
						cell2.setCellValue(record.getR21_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row21
					// Column D
					cell3 = row.createCell(2);
					if (record.getR21_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR21_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row21
					// Column E
					cell4 = row.createCell(3);
					if (record.getR21_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR21_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row21
					// Column F
					cell5 = row.createCell(4);
					if (record.getR21_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR21_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row21
					// Column G
					cell6 = row.createCell(5);
					if (record.getR21_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR21_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row21
					// Column H
					cell7 = row.createCell(6);
					if (record.getR21_OTHER() != null) {
						cell7.setCellValue(record.getR21_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row21
					// Column I
					cell8 = row.createCell(7);
					if (record.getR21_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR21_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row21
					// Column J
					cell9 = row.createCell(8);
					if (record.getR21_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR21_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row22
					row = sheet.getRow(21);
					// row22
					// Column C
					cell2 = row.createCell(1);
					if (record.getR22_ISSUER() != null) {
						cell2.setCellValue(record.getR22_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row22
					// Column D
					cell3 = row.createCell(2);
					if (record.getR22_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR22_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row22
					// Column E
					cell4 = row.createCell(3);
					if (record.getR22_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR22_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row22
					// Column F
					cell5 = row.createCell(4);
					if (record.getR22_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR22_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row22
					// Column G
					cell6 = row.createCell(5);
					if (record.getR22_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR22_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row22
					// Column H
					cell7 = row.createCell(6);
					if (record.getR22_OTHER() != null) {
						cell7.setCellValue(record.getR22_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row22
					// Column I
					cell8 = row.createCell(7);
					if (record.getR22_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR22_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row22
					// Column J
					cell9 = row.createCell(8);
					if (record.getR22_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR22_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row23
					row = sheet.getRow(22);
					// row23
					// Column C
					cell2 = row.createCell(1);
					if (record.getR23_ISSUER() != null) {
						cell2.setCellValue(record.getR23_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					// Column D
					cell3 = row.createCell(2);
					if (record.getR23_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR23_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row23
					// Column E
					cell4 = row.createCell(3);
					if (record.getR23_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR23_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row23
					// Column F
					cell5 = row.createCell(4);
					if (record.getR23_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR23_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row23
					// Column G
					cell6 = row.createCell(5);
					if (record.getR23_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR23_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row23
					// Column H
					cell7 = row.createCell(6);
					if (record.getR23_OTHER() != null) {
						cell7.setCellValue(record.getR23_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row23
					// Column I
					cell8 = row.createCell(7);
					if (record.getR23_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR23_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row23
					// Column J
					cell9 = row.createCell(8);
					if (record.getR23_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR23_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row24
					row = sheet.getRow(23);
					// row24
					// Column C
					cell2 = row.createCell(1);
					if (record.getR24_ISSUER() != null) {
						cell2.setCellValue(record.getR24_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					// Column D
					cell3 = row.createCell(2);
					if (record.getR24_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR24_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row24
					// Column E
					cell4 = row.createCell(3);
					if (record.getR24_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR24_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row24
					// Column F
					cell5 = row.createCell(4);
					if (record.getR24_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR24_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row24
					// Column G
					cell6 = row.createCell(5);
					if (record.getR24_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR24_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row24
					// Column H
					cell7 = row.createCell(6);
					if (record.getR24_OTHER() != null) {
						cell7.setCellValue(record.getR24_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row24
					// Column I
					cell8 = row.createCell(7);
					if (record.getR24_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR24_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row24
					// Column J
					cell9 = row.createCell(8);
					if (record.getR24_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR24_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row25
					row = sheet.getRow(24);
					// row25
					// Column C
					cell2 = row.createCell(1);
					if (record.getR25_ISSUER() != null) {
						cell2.setCellValue(record.getR25_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					// Column D
					cell3 = row.createCell(2);
					if (record.getR25_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR25_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row25
					// Column E
					cell4 = row.createCell(3);
					if (record.getR25_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR25_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row25
					// Column F
					cell5 = row.createCell(4);
					if (record.getR25_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR25_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row25
					// Column G
					cell6 = row.createCell(5);
					if (record.getR25_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR25_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row25
					// Column H
					cell7 = row.createCell(6);
					if (record.getR25_OTHER() != null) {
						cell7.setCellValue(record.getR25_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row25
					// Column I
					cell8 = row.createCell(7);
					if (record.getR25_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR25_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row25
					// Column J
					cell9 = row.createCell(8);
					if (record.getR25_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR25_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row26
					row = sheet.getRow(25);
					// row26
					// Column C
					cell2 = row.createCell(1);
					if (record.getR26_ISSUER() != null) {
						cell2.setCellValue(record.getR26_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					// Column D
					cell3 = row.createCell(2);
					if (record.getR26_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR26_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row26
					// Column E
					cell4 = row.createCell(3);
					if (record.getR26_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR26_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row26
					// Column F
					cell5 = row.createCell(4);
					if (record.getR26_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR26_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row26
					// Column G
					cell6 = row.createCell(5);
					if (record.getR26_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR26_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row26
					// Column H
					cell7 = row.createCell(6);
					if (record.getR26_OTHER() != null) {
						cell7.setCellValue(record.getR26_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row26
					// Column I
					cell8 = row.createCell(7);
					if (record.getR26_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR26_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row26
					// Column J
					cell9 = row.createCell(8);
					if (record.getR26_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR26_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row27
					row = sheet.getRow(26);
					// row27
					// Column C
					cell2 = row.createCell(1);
					if (record.getR27_ISSUER() != null) {
						cell2.setCellValue(record.getR27_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					// Column D
					cell3 = row.createCell(2);
					if (record.getR27_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR27_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row27
					// Column E
					cell4 = row.createCell(3);
					if (record.getR27_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR27_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row27
					// Column F
					cell5 = row.createCell(4);
					if (record.getR27_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR27_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row27
					// Column G
					cell6 = row.createCell(5);
					if (record.getR27_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR27_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row27
					// Column H
					cell7 = row.createCell(6);
					if (record.getR27_OTHER() != null) {
						cell7.setCellValue(record.getR27_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row27
					// Column I
					cell8 = row.createCell(7);
					if (record.getR27_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR27_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row27
					// Column J
					cell9 = row.createCell(8);
					if (record.getR27_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR27_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row28
					row = sheet.getRow(27);
					// row28
					// Column C
					cell2 = row.createCell(1);
					if (record.getR28_ISSUER() != null) {
						cell2.setCellValue(record.getR28_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					// Column D
					cell3 = row.createCell(2);
					if (record.getR28_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR28_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row28
					// Column E
					cell4 = row.createCell(3);
					if (record.getR28_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR28_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row28
					// Column F
					cell5 = row.createCell(4);
					if (record.getR28_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR28_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row28
					// Column G
					cell6 = row.createCell(5);
					if (record.getR28_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR28_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row28
					// Column H
					cell7 = row.createCell(6);
					if (record.getR28_OTHER() != null) {
						cell7.setCellValue(record.getR28_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row28
					// Column I
					cell8 = row.createCell(7);
					if (record.getR28_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR28_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row28
					// Column J
					cell9 = row.createCell(8);
					if (record.getR28_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR28_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row29
					row = sheet.getRow(28);
					// row29
					// Column C
					cell2 = row.createCell(1);
					if (record.getR29_ISSUER() != null) {
						cell2.setCellValue(record.getR29_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row29
					// Column D
					cell3 = row.createCell(2);
					if (record.getR29_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR29_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row29
					// Column E
					cell4 = row.createCell(3);
					if (record.getR29_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR29_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row29
					// Column F
					cell5 = row.createCell(4);
					if (record.getR29_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR29_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row29
					// Column G
					cell6 = row.createCell(5);
					if (record.getR29_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR29_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row29
					// Column H
					cell7 = row.createCell(6);
					if (record.getR29_OTHER() != null) {
						cell7.setCellValue(record.getR29_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row29
					// Column I
					cell8 = row.createCell(7);
					if (record.getR29_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR29_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row29
					// Column J
					cell9 = row.createCell(8);
					if (record.getR29_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR29_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row30
					row = sheet.getRow(29);
					// row30
					// Column C
					cell2 = row.createCell(1);
					if (record.getR30_ISSUER() != null) {
						cell2.setCellValue(record.getR30_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					// Column D
					cell3 = row.createCell(2);
					if (record.getR30_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR30_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row30
					// Column E
					cell4 = row.createCell(3);
					if (record.getR30_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR30_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row30
					// Column F
					cell5 = row.createCell(4);
					if (record.getR30_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR30_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row30
					// Column G
					cell6 = row.createCell(5);
					if (record.getR30_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR30_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row30
					// Column H
					cell7 = row.createCell(6);
					if (record.getR30_OTHER() != null) {
						cell7.setCellValue(record.getR30_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row30
					// Column I
					cell8 = row.createCell(7);
					if (record.getR30_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR30_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row30
					// Column J
					cell9 = row.createCell(8);
					if (record.getR30_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR30_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row31
					row = sheet.getRow(30);
					// row31
					// Column C
					cell2 = row.createCell(1);
					if (record.getR31_ISSUER() != null) {
						cell2.setCellValue(record.getR31_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row31
					// Column D
					cell3 = row.createCell(2);
					if (record.getR31_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR31_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row31
					// Column E
					cell4 = row.createCell(3);
					if (record.getR31_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR31_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row31
					// Column F
					cell5 = row.createCell(4);
					if (record.getR31_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR31_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row31
					// Column G
					cell6 = row.createCell(5);
					if (record.getR31_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR31_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row31
					// Column H
					cell7 = row.createCell(6);
					if (record.getR31_OTHER() != null) {
						cell7.setCellValue(record.getR31_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row31
					// Column I
					cell8 = row.createCell(7);
					if (record.getR31_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR31_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row31
					// Column J
					cell9 = row.createCell(8);
					if (record.getR31_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR31_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row32
					row = sheet.getRow(31);
					// row32
					// Column C
					cell2 = row.createCell(1);
					if (record.getR32_ISSUER() != null) {
						cell2.setCellValue(record.getR32_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row32
					// Column D
					cell3 = row.createCell(2);
					if (record.getR32_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR32_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row32
					// Column E
					cell4 = row.createCell(3);
					if (record.getR32_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR32_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row32
					// Column F
					cell5 = row.createCell(4);
					if (record.getR32_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR32_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row32
					// Column G
					cell6 = row.createCell(5);
					if (record.getR32_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR32_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row32
					// Column H
					cell7 = row.createCell(6);
					if (record.getR32_OTHER() != null) {
						cell7.setCellValue(record.getR32_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row32
					// Column I
					cell8 = row.createCell(7);
					if (record.getR32_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR32_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row32
					// Column J
					cell9 = row.createCell(8);
					if (record.getR32_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR32_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row33
					row = sheet.getRow(32);
					// row33
					// Column C
					cell2 = row.createCell(1);
					if (record.getR33_ISSUER() != null) {
						cell2.setCellValue(record.getR33_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(2);
					if (record.getR33_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR33_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(3);
					if (record.getR33_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR33_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(4);
					if (record.getR33_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR33_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(5);
					if (record.getR33_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR33_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(6);
					if (record.getR33_OTHER() != null) {
						cell7.setCellValue(record.getR33_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(7);
					if (record.getR33_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR33_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(8);
					if (record.getR33_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR33_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row34
					row = sheet.getRow(33);

					// row34
					// Column C
					cell2 = row.createCell(1);
					if (record.getR34_ISSUER() != null) {
						cell2.setCellValue(record.getR34_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(2);
					if (record.getR34_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR34_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(3);
					if (record.getR34_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR34_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(4);
					if (record.getR34_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR34_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(5);
					if (record.getR34_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR34_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(6);
					if (record.getR34_OTHER() != null) {
						cell7.setCellValue(record.getR34_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(7);
					if (record.getR34_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR34_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(8);
					if (record.getR34_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR34_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row35
					row = sheet.getRow(34);

					// row35
					// Column C
					cell2 = row.createCell(1);
					if (record.getR35_ISSUER() != null) {
						cell2.setCellValue(record.getR35_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(2);
					if (record.getR35_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR35_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(3);
					if (record.getR35_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR35_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(4);
					if (record.getR35_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR35_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(5);
					if (record.getR35_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR35_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(6);
					if (record.getR35_OTHER() != null) {
						cell7.setCellValue(record.getR35_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(7);
					if (record.getR35_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR35_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(8);
					if (record.getR35_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR35_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row36
					row = sheet.getRow(35);

					// row36
					// Column C
					cell2 = row.createCell(1);
					if (record.getR36_ISSUER() != null) {
						cell2.setCellValue(record.getR36_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(2);
					if (record.getR36_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR36_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(3);
					if (record.getR36_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR36_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(4);
					if (record.getR36_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR36_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(5);
					if (record.getR36_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR36_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(6);
					if (record.getR36_OTHER() != null) {
						cell7.setCellValue(record.getR36_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(7);
					if (record.getR36_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR36_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(8);
					if (record.getR36_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR36_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row37
					row = sheet.getRow(36);

					// row37
					// Column C
					cell2 = row.createCell(1);
					if (record.getR37_ISSUER() != null) {
						cell2.setCellValue(record.getR37_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(2);
					if (record.getR37_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR37_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(3);
					if (record.getR37_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR37_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(4);
					if (record.getR37_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR37_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(5);
					if (record.getR37_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR37_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(6);
					if (record.getR37_OTHER() != null) {
						cell7.setCellValue(record.getR37_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(7);
					if (record.getR37_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR37_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(8);
					if (record.getR37_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR37_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
				}
				// workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

public List<Object[]> getM_SRWA_12H_NewResub() {
    List<Object[]> resubList = new ArrayList<>();
    try {
        List<M_SRWA_12H_New_Archival_Summary_Entity> latestArchivalList =
                M_SRWA_12H_New_Archival_Summary_Repo.getdatabydateListWithVersion();

        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
            for (M_SRWA_12H_New_Archival_Summary_Entity entity : latestArchivalList) {
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
        System.err.println("Error fetching M_SRWA_12H_New Resub data: " + e.getMessage());
        e.printStackTrace();
    }
    return resubList;
}



	//Archival View
	public List<Object[]> getM_SRWA_12H_NewArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_SRWA_12H_New_Archival_Summary_Entity> repoData = M_SRWA_12H_New_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SRWA_12H_New_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReportDate(), 
							entity.getReportVersion() 
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SRWA_12H_New_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12H_New Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}





}
