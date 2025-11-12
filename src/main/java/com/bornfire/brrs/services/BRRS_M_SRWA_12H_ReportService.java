package com.bornfire.brrs.services;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
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
import com.bornfire.brrs.entities.BRRS_M_SRWA_12H_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12H_Detail_Repo;
import com.bornfire.brrs.entities.M_SRWA_12H_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12H_Summary_Repo;
import com.bornfire.brrs.entities.M_SRWA_12H_Archival_Summary_Entity;

@Component
@Service

public class BRRS_M_SRWA_12H_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12H_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_SRWA_12H_Detail_Repo M_SRWA_12H_DETAIL_Repo;

	@Autowired
	BRRS_M_SRWA_12H_Summary_Repo M_SRWA_12H_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12H_Archival_Summary_Repo M_SRWA_12H_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// Detail View
	// public ModelAndView getM_SRWA_12HView(String reportId, String fromdate,
	// String
	// todate, String currency, String dtltype, Pageable pageable, String type,
	// String version) {

	// ModelAndView mv = new ModelAndView(); Session hs =
	// sessionFactory.getCurrentSession();

	// int pageSize = pageable.getPageSize(); int currentPage =
	// pageable.getPageNumber(); int startItem = currentPage * pageSize;

	// if (type.equals("ARCHIVAL") & version != null ) {

	// List<M_SRWA_12H_Archival_Summary_Entity> T1Master = new
	// ArrayList<M_SRWA_12H_Archival_Summary_Entity>(); try { Date d1 =
	// dateformat.parse(todate);

	// T1Master =
	// M_SRWA_12H_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse
	// (todate), version);

	// } catch (ParseException e) { e.printStackTrace(); }

	// mv.addObject("reportsummary", T1Master); } else {
	// List<M_SRWA_12H_Summary_Entity> T1Master = new
	// ArrayList<M_SRWA_12H_Summary_Entity>();

	// try { Date d1 = dateformat.parse(todate);

	// T1Master =
	// M_SRWA_12H_Summary_Repo.getdatabydateList(dateformat.parse(todate));

	// System.out.println("Size of t1master is :"+T1Master.size());

	// } catch (ParseException e) { e.printStackTrace(); }
	// mv.addObject("reportsummary", T1Master); }

	// // T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
	// mv.setViewName("BRRS/M_SRWA_12H"); mv.addObject("displaymode", "summary");
	// System.out.println("scv" + mv.getViewName()); return mv;

	// }

	public ModelAndView getM_SRWA_12HView(String reportId, String fromdate, String todate,
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
            List<M_SRWA_12H_Archival_Summary_Entity> T1Master = 
                M_SRWA_12H_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 2: RESUB ----------
        else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<M_SRWA_12H_Archival_Summary_Entity> T1Master =
                M_SRWA_12H_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
            
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<M_SRWA_12H_Summary_Entity> T1Master = 
                M_SRWA_12H_Summary_Repo.getdatabydateListWithVersion(todate);
            System.out.println("T1Master Size "+T1Master.size());
            mv.addObject("reportsummary", T1Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12H");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	public void updateReport(M_SRWA_12H_Summary_Entity updatedEntity) {
		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_SRWA_12H_Summary_Entity existing = M_SRWA_12H_Summary_Repo
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
						Method getter = M_SRWA_12H_Summary_Entity.class.getMethod(getterName);
						Method setter = M_SRWA_12H_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
		M_SRWA_12H_Summary_Repo.save(existing);

	}

	// Detail View
	// public ModelAndView getM_SRWA_12HcurrentDtl(String reportId, String fromdate,
	// String todate, String currency,
	// String dtltype, Pageable pageable, String filter) {

	// int pageSize = pageable.getPageSize();
	// int currentPage = pageable.getPageNumber();
	// int totalPages=0;
	// ModelAndView mv = new ModelAndView();

	// Session hs = sessionFactory.getCurrentSession();
	// List<M_SRWA_12H_Detail_Entity> T1Dt1 = new
	// ArrayList<M_SRWA_12H_Detail_Entity>();

	// try {
	// Date d1 = dateformat.parse(todate);
	// String rowId = null;
	// String columnId = null;
	// if (filter != null && filter.contains(",")) {
	// String[] parts = filter.split(",");
	// if (parts.length >= 2) {
	// rowId = parts[0];
	// columnId = parts[1];
	// }
	// }

	// if (rowId != null && columnId != null) {
	// System.out.println("Testing");
	// T1Dt1=M_SRWA_12H_DETAIL_Repo.getdatabydateListrow(dateformat.parse(todate),columnId,rowId);

	// } else {

	// T1Dt1 = M_SRWA_12H_DETAIL_Repo.getdatabydateList(d1,currentPage,pageSize);
	// totalPages=M_SRWA_12H_DETAIL_Repo.getdatacount(dateformat.parse(todate));
	// mv.addObject("pagination","YES");

	// }

	// } catch (ParseException e) {
	// e.printStackTrace();
	// }

	// //Page<Object> T1Dt1Page = new PageImpl<Object>(pagedlist,
	// PageRequest.of(currentPage, pageSize), T1Dt1.size());

	// mv.setViewName("BRRS/M_SRWA_12H");
	// mv.addObject("currentPage", currentPage);
	// mv.addObject("totalPages",(int)Math.ceil((double)totalPages / 100));
	// mv.addObject("displaymode", "Details");
	// //mv.addObject("reportdetails", T1Dt1Page.getContent());
	// mv.addObject("reportdetails",T1Dt1 );
	// mv.addObject("reportmaster12", T1Dt1);
	// //mv.addObject("reportmaster1", qr);
	// //mv.addObject("singledetail", new T1CurProdDetail());
	// mv.addObject("reportsflag", "reportsflag");
	// mv.addObject("menu", reportId);
	// return mv;
	// }

	// Download For Summary
	public byte[] BRRS_M_SRWA_12HExcel(String filename, String reportId,
			String fromdate, String todate,
			String currency, String dtltype,
			String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

    // ARCHIVAL check
    if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating ARCHIVAL report for version {}", version);
        return BRRS_M_SRWA_12HArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }
    // RESUB check
    else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating RESUB report for version {}", version);

       
        List<M_SRWA_12H_Archival_Summary_Entity> T1Master =
                M_SRWA_12H_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

        // Generate Excel for RESUB
        return BRRS_M_SRWA_12HResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }




		// Default (LIVE) case
		List<M_SRWA_12H_Summary_Entity> dataList = M_SRWA_12H_Summary_Repo.getdatabydateList(reportDate);

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

					M_SRWA_12H_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR12_ISSUER() != null) {
						cell2.setCellValue(record.getR12_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR12_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR12_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row12
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR12_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR12_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row12
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR12_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR12_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row12
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR12_OTHER() != null) {
						cell7.setCellValue(record.getR12_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row12
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR12_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR12_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row12
					// Column J
					Cell cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR13_ISSUER() != null) {
						cell2.setCellValue(record.getR13_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR13_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR13_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR13_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR13_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR13_OTHER() != null) {
						cell7.setCellValue(record.getR13_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row13
					// Column I
					cell8 = row.createCell(8);
					if (record.getR13_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR13_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row13
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR14_ISSUER() != null) {
						cell2.setCellValue(record.getR14_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR14_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR14_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR14_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR14_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row14
					// Column H
					cell7 = row.createCell(7);
					if (record.getR14_OTHER() != null) {
						cell7.setCellValue(record.getR14_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row14
					// Column I
					cell8 = row.createCell(8);
					if (record.getR14_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR14_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row14
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR15_ISSUER() != null) {
						cell2.setCellValue(record.getR15_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR15_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row15
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR15_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR15_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row15
					// Column G
					cell6 = row.createCell(6);
					if (record.getR15_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR15_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row15
					// Column H
					cell7 = row.createCell(7);
					if (record.getR15_OTHER() != null) {
						cell7.setCellValue(record.getR15_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row15
					// Column I
					cell8 = row.createCell(8);
					if (record.getR15_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR15_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row15
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR16_ISSUER() != null) {
						cell2.setCellValue(record.getR16_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR16_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row16
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR16_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR16_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row16
					// Column G
					cell6 = row.createCell(6);
					if (record.getR16_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR16_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row16
					// Column H
					cell7 = row.createCell(7);
					if (record.getR16_OTHER() != null) {
						cell7.setCellValue(record.getR16_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row16
					// Column I
					cell8 = row.createCell(8);
					if (record.getR16_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR16_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row16
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR17_ISSUER() != null) {
						cell2.setCellValue(record.getR17_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					// Column D
					cell3 = row.createCell(3);
					if (record.getR17_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR17_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row17
					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR17_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR17_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row17
					// Column G
					cell6 = row.createCell(6);
					if (record.getR17_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR17_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row17
					// Column H
					cell7 = row.createCell(7);
					if (record.getR17_OTHER() != null) {
						cell7.setCellValue(record.getR17_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row17
					// Column I
					cell8 = row.createCell(8);
					if (record.getR17_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR17_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row17
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR18_ISSUER() != null) {
						cell2.setCellValue(record.getR18_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					// Column D
					cell3 = row.createCell(3);
					if (record.getR18_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR18_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row18
					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR18_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR18_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row18
					// Column G
					cell6 = row.createCell(6);
					if (record.getR18_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR18_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row18
					// Column H
					cell7 = row.createCell(7);
					if (record.getR18_OTHER() != null) {
						cell7.setCellValue(record.getR18_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row18
					// Column I
					cell8 = row.createCell(8);
					if (record.getR18_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR18_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row18
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR19_ISSUER() != null) {
						cell2.setCellValue(record.getR19_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					// Column D
					cell3 = row.createCell(3);
					if (record.getR19_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR19_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row19
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR19_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR19_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row19
					// Column G
					cell6 = row.createCell(6);
					if (record.getR19_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR19_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row19
					// Column H
					cell7 = row.createCell(7);
					if (record.getR19_OTHER() != null) {
						cell7.setCellValue(record.getR19_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row19
					// Column I
					cell8 = row.createCell(8);
					if (record.getR19_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR19_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row19
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR20_ISSUER() != null) {
						cell2.setCellValue(record.getR20_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR20_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row20
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR20_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR20_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row20
					// Column G
					cell6 = row.createCell(6);
					if (record.getR20_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR20_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row20
					// Column H
					cell7 = row.createCell(7);
					if (record.getR20_OTHER() != null) {
						cell7.setCellValue(record.getR20_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row20
					// Column I
					cell8 = row.createCell(8);
					if (record.getR20_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR20_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row20
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR21_ISSUER() != null) {
						cell2.setCellValue(record.getR21_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR21_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row21
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR21_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR21_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row21
					// Column G
					cell6 = row.createCell(6);
					if (record.getR21_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR21_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row21
					// Column H
					cell7 = row.createCell(7);
					if (record.getR21_OTHER() != null) {
						cell7.setCellValue(record.getR21_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row21
					// Column I
					cell8 = row.createCell(8);
					if (record.getR21_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR21_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row21
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR22_ISSUER() != null) {
						cell2.setCellValue(record.getR22_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR22_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR22_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR22_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR22_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row22
					// Column H
					cell7 = row.createCell(7);
					if (record.getR22_OTHER() != null) {
						cell7.setCellValue(record.getR22_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row22
					// Column I
					cell8 = row.createCell(8);
					if (record.getR22_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR22_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row22
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR23_ISSUER() != null) {
						cell2.setCellValue(record.getR23_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR23_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR23_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR23_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR23_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row23
					// Column H
					cell7 = row.createCell(7);
					if (record.getR23_OTHER() != null) {
						cell7.setCellValue(record.getR23_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row23
					// Column I
					cell8 = row.createCell(8);
					if (record.getR23_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR23_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row23
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR24_ISSUER() != null) {
						cell2.setCellValue(record.getR24_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR24_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR24_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR24_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR24_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row24
					// Column H
					cell7 = row.createCell(7);
					if (record.getR24_OTHER() != null) {
						cell7.setCellValue(record.getR24_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row24
					// Column I
					cell8 = row.createCell(8);
					if (record.getR24_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR24_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row24
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR25_ISSUER() != null) {
						cell2.setCellValue(record.getR25_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR25_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row25
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR25_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR25_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row25
					// Column G
					cell6 = row.createCell(6);
					if (record.getR25_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR25_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row25
					// Column H
					cell7 = row.createCell(7);
					if (record.getR25_OTHER() != null) {
						cell7.setCellValue(record.getR25_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row25
					// Column I
					cell8 = row.createCell(8);
					if (record.getR25_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR25_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row25
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR26_ISSUER() != null) {
						cell2.setCellValue(record.getR26_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR26_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row26
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR26_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR26_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row26
					// Column G
					cell6 = row.createCell(6);
					if (record.getR26_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR26_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row26
					// Column H
					cell7 = row.createCell(7);
					if (record.getR26_OTHER() != null) {
						cell7.setCellValue(record.getR26_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row26
					// Column I
					cell8 = row.createCell(8);
					if (record.getR26_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR26_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row26
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR27_ISSUER() != null) {
						cell2.setCellValue(record.getR27_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR27_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row27
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR27_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR27_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row27
					// Column G
					cell6 = row.createCell(6);
					if (record.getR27_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR27_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row27
					// Column H
					cell7 = row.createCell(7);
					if (record.getR27_OTHER() != null) {
						cell7.setCellValue(record.getR27_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row27
					// Column I
					cell8 = row.createCell(8);
					if (record.getR27_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR27_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row27
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR28_ISSUER() != null) {
						cell2.setCellValue(record.getR28_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR28_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row28
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR28_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR28_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row28
					// Column G
					cell6 = row.createCell(6);
					if (record.getR28_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR28_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row28
					// Column H
					cell7 = row.createCell(7);
					if (record.getR28_OTHER() != null) {
						cell7.setCellValue(record.getR28_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row28
					// Column I
					cell8 = row.createCell(8);
					if (record.getR28_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR28_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row28
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR29_ISSUER() != null) {
						cell2.setCellValue(record.getR29_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row29
					// Column D
					cell3 = row.createCell(3);
					if (record.getR29_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR29_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row29
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR29_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR29_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row29
					// Column G
					cell6 = row.createCell(6);
					if (record.getR29_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR29_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row29
					// Column H
					cell7 = row.createCell(7);
					if (record.getR29_OTHER() != null) {
						cell7.setCellValue(record.getR29_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row29
					// Column I
					cell8 = row.createCell(8);
					if (record.getR29_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR29_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row29
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR30_ISSUER() != null) {
						cell2.setCellValue(record.getR30_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					// Column D
					cell3 = row.createCell(3);
					if (record.getR30_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR30_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row30
					// Column E
					cell4 = row.createCell(4);
					if (record.getR30_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR30_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record.getR30_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR30_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row30
					// Column G
					cell6 = row.createCell(6);
					if (record.getR30_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR30_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row30
					// Column H
					cell7 = row.createCell(7);
					if (record.getR30_OTHER() != null) {
						cell7.setCellValue(record.getR30_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row30
					// Column I
					cell8 = row.createCell(8);
					if (record.getR30_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR30_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row30
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR31_ISSUER() != null) {
						cell2.setCellValue(record.getR31_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR31_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row31
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR31_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR31_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row31
					// Column G
					cell6 = row.createCell(6);
					if (record.getR31_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR31_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row31
					// Column H
					cell7 = row.createCell(7);
					if (record.getR31_OTHER() != null) {
						cell7.setCellValue(record.getR31_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row31
					// Column I
					cell8 = row.createCell(8);
					if (record.getR31_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR31_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row31
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR32_ISSUER() != null) {
						cell2.setCellValue(record.getR32_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row32
					// Column D
					cell3 = row.createCell(3);
					if (record.getR32_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR32_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row32
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR32_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR32_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row32
					// Column G
					cell6 = row.createCell(6);
					if (record.getR32_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR32_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row32
					// Column H
					cell7 = row.createCell(7);
					if (record.getR32_OTHER() != null) {
						cell7.setCellValue(record.getR32_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row32
					// Column I
					cell8 = row.createCell(8);
					if (record.getR32_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR32_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row32
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR33_ISSUER() != null) {
						cell2.setCellValue(record.getR33_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR33_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR33_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR33_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR33_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR33_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR33_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR33_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR33_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR33_OTHER() != null) {
						cell7.setCellValue(record.getR33_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR33_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR33_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR34_ISSUER() != null) {
						cell2.setCellValue(record.getR34_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR34_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR34_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR34_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR34_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR34_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR34_OTHER() != null) {
						cell7.setCellValue(record.getR34_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR34_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR34_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR35_ISSUER() != null) {
						cell2.setCellValue(record.getR35_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR35_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR35_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR35_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR35_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR35_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR35_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR35_OTHER() != null) {
						cell7.setCellValue(record.getR35_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR35_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR35_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR36_ISSUER() != null) {
						cell2.setCellValue(record.getR36_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR36_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR36_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR36_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR36_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR36_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR36_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR36_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR36_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR36_OTHER() != null) {
						cell7.setCellValue(record.getR36_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR36_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR36_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR37_ISSUER() != null) {
						cell2.setCellValue(record.getR37_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR37_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR37_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR37_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR37_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR37_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR37_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR37_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR37_OTHER() != null) {
						cell7.setCellValue(record.getR37_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR37_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR37_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR37_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR37_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row38
					row = sheet.getRow(37);

					// row38
					// Column C
					cell2 = row.createCell(2);
					if (record.getR38_ISSUER() != null) {
						cell2.setCellValue(record.getR38_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR38_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR38_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR38_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR38_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR38_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR38_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR38_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR38_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR38_OTHER() != null) {
						cell7.setCellValue(record.getR38_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR38_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR38_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR38_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR38_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row39
					row = sheet.getRow(38);

					// row39
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_ISSUER() != null) {
						cell2.setCellValue(record.getR39_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR39_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR39_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR39_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR39_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR39_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR39_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR39_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR39_OTHER() != null) {
						cell7.setCellValue(record.getR39_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR39_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR39_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR39_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR39_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row40
					row = sheet.getRow(39);

					// row40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_ISSUER() != null) {
						cell2.setCellValue(record.getR40_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR40_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR40_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR40_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR40_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR40_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR40_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR40_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR40_OTHER() != null) {
						cell7.setCellValue(record.getR40_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR40_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR40_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR40_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR40_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row41
					row = sheet.getRow(40);

					// row41
					// Column C
					cell2 = row.createCell(2);
					if (record.getR41_ISSUER() != null) {
						cell2.setCellValue(record.getR41_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR41_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR41_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR41_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR41_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR41_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR41_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR41_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR41_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR41_OTHER() != null) {
						cell7.setCellValue(record.getR41_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR41_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR41_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR41_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR41_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row42
					row = sheet.getRow(41);

					// row42
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_ISSUER() != null) {
						cell2.setCellValue(record.getR42_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR42_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR42_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR42_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR42_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR42_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR42_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR42_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR42_OTHER() != null) {
						cell7.setCellValue(record.getR42_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR42_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR42_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR42_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR42_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row43
					row = sheet.getRow(42);

					// row43
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_ISSUER() != null) {
						cell2.setCellValue(record.getR43_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR43_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR43_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR43_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR43_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR43_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR43_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR43_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR43_OTHER() != null) {
						cell7.setCellValue(record.getR43_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR43_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR43_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR43_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR43_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row44
					row = sheet.getRow(43);

					// row44
					// Column C
					cell2 = row.createCell(2);
					if (record.getR44_ISSUER() != null) {
						cell2.setCellValue(record.getR44_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR44_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR44_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR44_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR44_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR44_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR44_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR44_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR44_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR44_OTHER() != null) {
						cell7.setCellValue(record.getR44_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR44_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR44_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR44_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR44_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row45
					row = sheet.getRow(44);

					// row45
					// Column C
					cell2 = row.createCell(2);
					if (record.getR45_ISSUER() != null) {
						cell2.setCellValue(record.getR45_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR45_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR45_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR45_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR45_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR45_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR45_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR45_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR45_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR45_OTHER() != null) {
						cell7.setCellValue(record.getR45_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR45_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR45_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR45_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR45_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row46
					row = sheet.getRow(45);

					// row46
					// Column C
					cell2 = row.createCell(2);
					if (record.getR46_ISSUER() != null) {
						cell2.setCellValue(record.getR46_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR46_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR46_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR46_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR46_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR46_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR46_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR46_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR46_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR46_OTHER() != null) {
						cell7.setCellValue(record.getR46_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR46_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR46_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR46_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR46_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row47
					row = sheet.getRow(46);

					// row47
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_ISSUER() != null) {
						cell2.setCellValue(record.getR47_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR47_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR47_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR47_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR47_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR47_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR47_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR47_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR47_OTHER() != null) {
						cell7.setCellValue(record.getR47_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR47_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR47_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR47_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR47_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row48
					row = sheet.getRow(47);

					// row48
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_ISSUER() != null) {
						cell2.setCellValue(record.getR48_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR48_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR48_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR48_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR48_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR48_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR48_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR48_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR48_OTHER() != null) {
						cell7.setCellValue(record.getR48_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR48_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR48_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR48_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR48_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row49
					row = sheet.getRow(48);

					// row49
					// Column C
					cell2 = row.createCell(2);
					if (record.getR49_ISSUER() != null) {
						cell2.setCellValue(record.getR49_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR49_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR49_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR49_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR49_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR49_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR49_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR49_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR49_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR49_OTHER() != null) {
						cell7.setCellValue(record.getR49_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR49_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR49_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR49_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR49_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row50
					row = sheet.getRow(49);

					// row50
					// Column C
					cell2 = row.createCell(2);
					if (record.getR50_ISSUER() != null) {
						cell2.setCellValue(record.getR50_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR50_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR50_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR50_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR50_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR50_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR50_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR50_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR50_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR50_OTHER() != null) {
						cell7.setCellValue(record.getR50_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR50_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR50_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR50_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR50_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row51
					row = sheet.getRow(50);

					// row51
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_ISSUER() != null) {
						cell2.setCellValue(record.getR51_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR51_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row51
					// Column E
					cell4 = row.createCell(4);
					if (record.getR51_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR51_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row51
					// Column F
					cell5 = row.createCell(5);
					if (record.getR51_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR51_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row51
					// Column G
					cell6 = row.createCell(6);
					if (record.getR51_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR51_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row51
					// Column H
					cell7 = row.createCell(7);
					if (record.getR51_OTHER() != null) {
						cell7.setCellValue(record.getR51_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row51
					// Column I
					cell8 = row.createCell(8);
					if (record.getR51_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR51_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row51
					// Column J
					cell9 = row.createCell(9);
					if (record.getR51_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR51_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row52
					row = sheet.getRow(51);

					// row52
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_ISSUER() != null) {
						cell2.setCellValue(record.getR52_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR52_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row52
					// Column E
					cell4 = row.createCell(4);
					if (record.getR52_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR52_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row52
					// Column F
					cell5 = row.createCell(5);
					if (record.getR52_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR52_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row52
					// Column G
					cell6 = row.createCell(6);
					if (record.getR52_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR52_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row52
					// Column H
					cell7 = row.createCell(7);
					if (record.getR52_OTHER() != null) {
						cell7.setCellValue(record.getR52_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row52
					// Column I
					cell8 = row.createCell(8);
					if (record.getR52_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR52_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row52
					// Column J
					cell9 = row.createCell(9);
					if (record.getR52_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR52_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row53
					row = sheet.getRow(52);

					// row53
					// Column C
					cell2 = row.createCell(2);
					if (record.getR53_ISSUER() != null) {
						cell2.setCellValue(record.getR53_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					// Column D
					cell3 = row.createCell(3);
					if (record.getR53_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR53_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row53
					// Column E
					cell4 = row.createCell(4);
					if (record.getR53_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR53_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row53
					// Column F
					cell5 = row.createCell(5);
					if (record.getR53_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR53_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row53
					// Column G
					cell6 = row.createCell(6);
					if (record.getR53_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR53_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row53
					// Column H
					cell7 = row.createCell(7);
					if (record.getR53_OTHER() != null) {
						cell7.setCellValue(record.getR53_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row53
					// Column I
					cell8 = row.createCell(8);
					if (record.getR53_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR53_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row53
					// Column J
					cell9 = row.createCell(9);
					if (record.getR53_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR53_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row54
					row = sheet.getRow(53);

					// row54
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_ISSUER() != null) {
						cell2.setCellValue(record.getR54_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR54_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row54
					// Column E
					cell4 = row.createCell(4);
					if (record.getR54_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR54_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row54
					// Column F
					cell5 = row.createCell(5);
					if (record.getR54_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR54_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row54
					// Column G
					cell6 = row.createCell(6);
					if (record.getR54_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR54_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row54
					// Column H
					cell7 = row.createCell(7);
					if (record.getR54_OTHER() != null) {
						cell7.setCellValue(record.getR54_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row54
					// Column I
					cell8 = row.createCell(8);
					if (record.getR54_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR54_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row54
					// Column J
					cell9 = row.createCell(9);
					if (record.getR54_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR54_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row55
					row = sheet.getRow(54);

					// row55
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_ISSUER() != null) {
						cell2.setCellValue(record.getR55_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR55_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row55
					// Column E
					cell4 = row.createCell(4);
					if (record.getR55_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR55_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row55
					// Column F
					cell5 = row.createCell(5);
					if (record.getR55_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR55_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row55
					// Column G
					cell6 = row.createCell(6);
					if (record.getR55_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR55_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row55
					// Column H
					cell7 = row.createCell(7);
					if (record.getR55_OTHER() != null) {
						cell7.setCellValue(record.getR55_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row55
					// Column I
					cell8 = row.createCell(8);
					if (record.getR55_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR55_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row55
					// Column J
					cell9 = row.createCell(9);
					if (record.getR55_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR55_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row56
					row = sheet.getRow(55);

					// row56
					// Column C
					cell2 = row.createCell(2);
					if (record.getR56_ISSUER() != null) {
						cell2.setCellValue(record.getR56_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row56
					// Column D
					cell3 = row.createCell(3);
					if (record.getR56_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR56_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row56
					// Column E
					cell4 = row.createCell(4);
					if (record.getR56_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR56_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row56
					// Column F
					cell5 = row.createCell(5);
					if (record.getR56_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR56_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row56
					// Column G
					cell6 = row.createCell(6);
					if (record.getR56_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR56_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row56
					// Column H
					cell7 = row.createCell(7);
					if (record.getR56_OTHER() != null) {
						cell7.setCellValue(record.getR56_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row56
					// Column I
					cell8 = row.createCell(8);
					if (record.getR56_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR56_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row56
					// Column J
					cell9 = row.createCell(9);
					if (record.getR56_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR56_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row57
					row = sheet.getRow(56);

					// row57
					// Column C
					cell2 = row.createCell(2);
					if (record.getR57_ISSUER() != null) {
						cell2.setCellValue(record.getR57_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row57
					// Column D
					cell3 = row.createCell(3);
					if (record.getR57_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR57_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row57
					// Column E
					cell4 = row.createCell(4);
					if (record.getR57_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR57_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row57
					// Column F
					cell5 = row.createCell(5);
					if (record.getR57_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR57_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row57
					// Column G
					cell6 = row.createCell(6);
					if (record.getR57_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR57_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row57
					// Column H
					cell7 = row.createCell(7);
					if (record.getR57_OTHER() != null) {
						cell7.setCellValue(record.getR57_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row57
					// Column I
					cell8 = row.createCell(8);
					if (record.getR57_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR57_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row57
					// Column J
					cell9 = row.createCell(9);
					if (record.getR57_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR57_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row58
					row = sheet.getRow(57);

					// row58
					// Column C
					cell2 = row.createCell(2);
					if (record.getR58_ISSUER() != null) {
						cell2.setCellValue(record.getR58_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row58
					// Column D
					cell3 = row.createCell(3);
					if (record.getR58_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR58_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row58
					// Column E
					cell4 = row.createCell(4);
					if (record.getR58_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR58_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row58
					// Column F
					cell5 = row.createCell(5);
					if (record.getR58_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR58_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row58
					// Column G
					cell6 = row.createCell(6);
					if (record.getR58_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR58_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row58
					// Column H
					cell7 = row.createCell(7);
					if (record.getR58_OTHER() != null) {
						cell7.setCellValue(record.getR58_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row58
					// Column I
					cell8 = row.createCell(8);
					if (record.getR58_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR58_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row58
					// Column J
					cell9 = row.createCell(9);
					if (record.getR58_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR58_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row59
					row = sheet.getRow(58);

					// row59
					// Column C
					cell2 = row.createCell(2);
					if (record.getR59_ISSUER() != null) {
						cell2.setCellValue(record.getR59_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row59
					// Column D
					cell3 = row.createCell(3);
					if (record.getR59_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR59_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row59
					// Column E
					cell4 = row.createCell(4);
					if (record.getR59_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR59_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row59
					// Column F
					cell5 = row.createCell(5);
					if (record.getR59_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR59_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row59
					// Column G
					cell6 = row.createCell(6);
					if (record.getR59_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR59_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row59
					// Column H
					cell7 = row.createCell(7);
					if (record.getR59_OTHER() != null) {
						cell7.setCellValue(record.getR59_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row59
					// Column I
					cell8 = row.createCell(8);
					if (record.getR59_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR59_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row59
					// Column J
					cell9 = row.createCell(9);
					if (record.getR59_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR59_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row60
					row = sheet.getRow(59);

					// row60
					// Column C
					cell2 = row.createCell(2);
					if (record.getR60_ISSUER() != null) {
						cell2.setCellValue(record.getR60_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row60
					// Column D
					cell3 = row.createCell(3);
					if (record.getR60_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR60_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row60
					// Column E
					cell4 = row.createCell(4);
					if (record.getR60_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR60_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row60
					// Column F
					cell5 = row.createCell(5);
					if (record.getR60_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR60_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row60
					// Column G
					cell6 = row.createCell(6);
					if (record.getR60_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR60_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row60
					// Column H
					cell7 = row.createCell(7);
					if (record.getR60_OTHER() != null) {
						cell7.setCellValue(record.getR60_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row60
					// Column I
					cell8 = row.createCell(8);
					if (record.getR60_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR60_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row60
					// Column J
					cell9 = row.createCell(9);
					if (record.getR60_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR60_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row61
					row = sheet.getRow(60);

					// row61
					// Column C
					cell2 = row.createCell(2);
					if (record.getR61_ISSUER() != null) {
						cell2.setCellValue(record.getR61_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row61
					// Column D
					cell3 = row.createCell(3);
					if (record.getR61_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR61_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row61
					// Column E
					cell4 = row.createCell(4);
					if (record.getR61_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR61_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row61
					// Column F
					cell5 = row.createCell(5);
					if (record.getR61_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR61_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row61
					// Column G
					cell6 = row.createCell(6);
					if (record.getR61_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR61_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row61
					// Column H
					cell7 = row.createCell(7);
					if (record.getR61_OTHER() != null) {
						cell7.setCellValue(record.getR61_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row61
					// Column I
					cell8 = row.createCell(8);
					if (record.getR61_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR61_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row61
					// Column J
					cell9 = row.createCell(9);
					if (record.getR61_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR61_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row62
					row = sheet.getRow(61);

					// row62
					// Column C
					cell2 = row.createCell(2);
					if (record.getR62_ISSUER() != null) {
						cell2.setCellValue(record.getR62_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row62
					// Column D
					cell3 = row.createCell(3);
					if (record.getR62_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR62_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row62
					// Column E
					cell4 = row.createCell(4);
					if (record.getR62_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR62_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row62
					// Column F
					cell5 = row.createCell(5);
					if (record.getR62_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR62_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row62
					// Column G
					cell6 = row.createCell(6);
					if (record.getR62_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR62_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row62
					// Column H
					cell7 = row.createCell(7);
					if (record.getR62_OTHER() != null) {
						cell7.setCellValue(record.getR62_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row62
					// Column I
					cell8 = row.createCell(8);
					if (record.getR62_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR62_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row62
					// Column J
					cell9 = row.createCell(9);
					if (record.getR62_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR62_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row63
					row = sheet.getRow(62);

					// row63
					// Column C
					cell2 = row.createCell(2);
					if (record.getR63_ISSUER() != null) {
						cell2.setCellValue(record.getR63_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row63
					// Column D
					cell3 = row.createCell(3);
					if (record.getR63_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR63_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row63
					// Column E
					cell4 = row.createCell(4);
					if (record.getR63_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR63_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row63
					// Column F
					cell5 = row.createCell(5);
					if (record.getR63_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR63_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row63
					// Column G
					cell6 = row.createCell(6);
					if (record.getR63_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR63_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row63
					// Column H
					cell7 = row.createCell(7);
					if (record.getR63_OTHER() != null) {
						cell7.setCellValue(record.getR63_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row63
					// Column I
					cell8 = row.createCell(8);
					if (record.getR63_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR63_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row63
					// Column J
					cell9 = row.createCell(9);
					if (record.getR63_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR63_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row64
					row = sheet.getRow(63);

					// row64
					// Column C
					cell2 = row.createCell(2);
					if (record.getR64_ISSUER() != null) {
						cell2.setCellValue(record.getR64_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row64
					// Column D
					cell3 = row.createCell(3);
					if (record.getR64_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR64_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row64
					// Column E
					cell4 = row.createCell(4);
					if (record.getR64_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR64_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row64
					// Column F
					cell5 = row.createCell(5);
					if (record.getR64_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR64_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row64
					// Column G
					cell6 = row.createCell(6);
					if (record.getR64_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR64_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row64
					// Column H
					cell7 = row.createCell(7);
					if (record.getR64_OTHER() != null) {
						cell7.setCellValue(record.getR64_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row64
					// Column I
					cell8 = row.createCell(8);
					if (record.getR64_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR64_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row64
					// Column J
					cell9 = row.createCell(9);
					if (record.getR64_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR64_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row65
					row = sheet.getRow(64);

					// row65
					// Column C
					cell2 = row.createCell(2);
					if (record.getR65_ISSUER() != null) {
						cell2.setCellValue(record.getR65_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row65
					// Column D
					cell3 = row.createCell(3);
					if (record.getR65_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR65_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row65
					// Column E
					cell4 = row.createCell(4);
					if (record.getR65_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR65_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row65
					// Column F
					cell5 = row.createCell(5);
					if (record.getR65_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR65_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row65
					// Column G
					cell6 = row.createCell(6);
					if (record.getR65_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR65_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row65
					// Column H
					cell7 = row.createCell(7);
					if (record.getR65_OTHER() != null) {
						cell7.setCellValue(record.getR65_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row65
					// Column I
					cell8 = row.createCell(8);
					if (record.getR65_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR65_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row65
					// Column J
					cell9 = row.createCell(9);
					if (record.getR65_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR65_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row66
					row = sheet.getRow(65);

					// row66
					// Column C
					cell2 = row.createCell(2);
					if (record.getR66_ISSUER() != null) {
						cell2.setCellValue(record.getR66_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row66
					// Column D
					cell3 = row.createCell(3);
					if (record.getR66_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR66_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row66
					// Column E
					cell4 = row.createCell(4);
					if (record.getR66_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR66_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row66
					// Column F
					cell5 = row.createCell(5);
					if (record.getR66_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR66_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row66
					// Column G
					cell6 = row.createCell(6);
					if (record.getR66_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR66_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row66
					// Column H
					cell7 = row.createCell(7);
					if (record.getR66_OTHER() != null) {
						cell7.setCellValue(record.getR66_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row66
					// Column I
					cell8 = row.createCell(8);
					if (record.getR66_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR66_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row66
					// Column J
					cell9 = row.createCell(9);
					if (record.getR66_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR66_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row67
					row = sheet.getRow(66);

					// row67
					// Column C
					cell2 = row.createCell(2);
					if (record.getR67_ISSUER() != null) {
						cell2.setCellValue(record.getR67_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row67
					// Column D
					cell3 = row.createCell(3);
					if (record.getR67_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR67_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row67
					// Column E
					cell4 = row.createCell(4);
					if (record.getR67_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR67_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row67
					// Column F
					cell5 = row.createCell(5);
					if (record.getR67_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR67_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row67
					// Column G
					cell6 = row.createCell(6);
					if (record.getR67_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR67_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row67
					// Column H
					cell7 = row.createCell(7);
					if (record.getR67_OTHER() != null) {
						cell7.setCellValue(record.getR67_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row67
					// Column I
					cell8 = row.createCell(8);
					if (record.getR67_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR67_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row67
					// Column J
					cell9 = row.createCell(9);
					if (record.getR67_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR67_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row68
					row = sheet.getRow(67);

					// row68
					// Column C
					cell2 = row.createCell(2);
					if (record.getR68_ISSUER() != null) {
						cell2.setCellValue(record.getR68_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row68
					// Column D
					cell3 = row.createCell(3);
					if (record.getR68_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR68_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row68
					// Column E
					cell4 = row.createCell(4);
					if (record.getR68_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR68_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row68
					// Column F
					cell5 = row.createCell(5);
					if (record.getR68_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR68_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row68
					// Column G
					cell6 = row.createCell(6);
					if (record.getR68_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR68_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row68
					// Column H
					cell7 = row.createCell(7);
					if (record.getR68_OTHER() != null) {
						cell7.setCellValue(record.getR68_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row68
					// Column I
					cell8 = row.createCell(8);
					if (record.getR68_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR68_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row68
					// Column J
					cell9 = row.createCell(9);
					if (record.getR68_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR68_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row69
					row = sheet.getRow(68);

					// row69
					// Column C
					cell2 = row.createCell(2);
					if (record.getR69_ISSUER() != null) {
						cell2.setCellValue(record.getR69_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row69
					// Column D
					cell3 = row.createCell(3);
					if (record.getR69_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR69_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row69
					// Column E
					cell4 = row.createCell(4);
					if (record.getR69_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR69_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row69
					// Column F
					cell5 = row.createCell(5);
					if (record.getR69_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR69_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row69
					// Column G
					cell6 = row.createCell(6);
					if (record.getR69_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR69_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row69
					// Column H
					cell7 = row.createCell(7);
					if (record.getR69_OTHER() != null) {
						cell7.setCellValue(record.getR69_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row69
					// Column I
					cell8 = row.createCell(8);
					if (record.getR69_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR69_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row69
					// Column J
					cell9 = row.createCell(9);
					if (record.getR69_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR69_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row70
					row = sheet.getRow(69);

					// row70
					// Column C
					cell2 = row.createCell(2);
					if (record.getR70_ISSUER() != null) {
						cell2.setCellValue(record.getR70_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row70
					// Column D
					cell3 = row.createCell(3);
					if (record.getR70_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR70_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row70
					// Column E
					cell4 = row.createCell(4);
					if (record.getR70_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR70_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row70
					// Column F
					cell5 = row.createCell(5);
					if (record.getR70_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR70_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row70
					// Column G
					cell6 = row.createCell(6);
					if (record.getR70_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR70_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row70
					// Column H
					cell7 = row.createCell(7);
					if (record.getR70_OTHER() != null) {
						cell7.setCellValue(record.getR70_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row70
					// Column I
					cell8 = row.createCell(8);
					if (record.getR70_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR70_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row70
					// Column J
					cell9 = row.createCell(9);
					if (record.getR70_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR70_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row71
					row = sheet.getRow(70);

					// row71
					// Column C
					cell2 = row.createCell(2);
					if (record.getR71_ISSUER() != null) {
						cell2.setCellValue(record.getR71_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row71
					// Column D
					cell3 = row.createCell(3);
					if (record.getR71_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR71_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row71
					// Column E
					cell4 = row.createCell(4);
					if (record.getR71_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR71_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row71
					// Column F
					cell5 = row.createCell(5);
					if (record.getR71_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR71_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row71
					// Column G
					cell6 = row.createCell(6);
					if (record.getR71_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR71_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row71
					// Column H
					cell7 = row.createCell(7);
					if (record.getR71_OTHER() != null) {
						cell7.setCellValue(record.getR71_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row71
					// Column I
					cell8 = row.createCell(8);
					if (record.getR71_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR71_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row71
					// Column J
					cell9 = row.createCell(9);
					if (record.getR71_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR71_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row72
					row = sheet.getRow(71);

					// row72
					// Column C
					cell2 = row.createCell(2);
					if (record.getR72_ISSUER() != null) {
						cell2.setCellValue(record.getR72_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row72
					// Column D
					cell3 = row.createCell(3);
					if (record.getR72_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR72_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row72
					// Column E
					cell4 = row.createCell(4);
					if (record.getR72_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR72_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row72
					// Column F
					cell5 = row.createCell(5);
					if (record.getR72_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR72_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row72
					// Column G
					cell6 = row.createCell(6);
					if (record.getR72_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR72_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row72
					// Column H
					cell7 = row.createCell(7);
					if (record.getR72_OTHER() != null) {
						cell7.setCellValue(record.getR72_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row72
					// Column I
					cell8 = row.createCell(8);
					if (record.getR72_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR72_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row72
					// Column J
					cell9 = row.createCell(9);
					if (record.getR72_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR72_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row73
					row = sheet.getRow(72);

					// row73
					// Column C
					cell2 = row.createCell(2);
					if (record.getR73_ISSUER() != null) {
						cell2.setCellValue(record.getR73_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row73
					// Column D
					cell3 = row.createCell(3);
					if (record.getR73_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR73_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row73
					// Column E
					cell4 = row.createCell(4);
					if (record.getR73_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR73_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row73
					// Column F
					cell5 = row.createCell(5);
					if (record.getR73_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR73_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row73
					// Column G
					cell6 = row.createCell(6);
					if (record.getR73_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR73_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row73
					// Column H
					cell7 = row.createCell(7);
					if (record.getR73_OTHER() != null) {
						cell7.setCellValue(record.getR73_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row73
					// Column I
					cell8 = row.createCell(8);
					if (record.getR73_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR73_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row73
					// Column J
					cell9 = row.createCell(9);
					if (record.getR73_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR73_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row74
					row = sheet.getRow(73);

					// row74
					// Column C
					cell2 = row.createCell(2);
					if (record.getR74_ISSUER() != null) {
						cell2.setCellValue(record.getR74_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row74
					// Column D
					cell3 = row.createCell(3);
					if (record.getR74_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR74_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row74
					// Column E
					cell4 = row.createCell(4);
					if (record.getR74_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR74_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row74
					// Column F
					cell5 = row.createCell(5);
					if (record.getR74_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR74_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row74
					// Column G
					cell6 = row.createCell(6);
					if (record.getR74_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR74_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row74
					// Column H
					cell7 = row.createCell(7);
					if (record.getR74_OTHER() != null) {
						cell7.setCellValue(record.getR74_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row74
					// Column I
					cell8 = row.createCell(8);
					if (record.getR74_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR74_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row74
					// Column J
					cell9 = row.createCell(9);
					if (record.getR74_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR74_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row75
					row = sheet.getRow(74);

					// row75
					// Column C
					cell2 = row.createCell(2);
					if (record.getR75_ISSUER() != null) {
						cell2.setCellValue(record.getR75_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row75
					// Column D
					cell3 = row.createCell(3);
					if (record.getR75_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR75_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row75
					// Column E
					cell4 = row.createCell(4);
					if (record.getR75_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR75_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row75
					// Column F
					cell5 = row.createCell(5);
					if (record.getR75_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR75_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row75
					// Column G
					cell6 = row.createCell(6);
					if (record.getR75_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR75_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row75
					// Column H
					cell7 = row.createCell(7);
					if (record.getR75_OTHER() != null) {
						cell7.setCellValue(record.getR75_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row75
					// Column I
					cell8 = row.createCell(8);
					if (record.getR75_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR75_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row75
					// Column J
					cell9 = row.createCell(9);
					if (record.getR75_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR75_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row76
					row = sheet.getRow(75);

					// row76
					// Column C
					cell2 = row.createCell(2);
					if (record.getR76_ISSUER() != null) {
						cell2.setCellValue(record.getR76_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row76
					// Column D
					cell3 = row.createCell(3);
					if (record.getR76_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR76_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row76
					// Column E
					cell4 = row.createCell(4);
					if (record.getR76_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR76_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row76
					// Column F
					cell5 = row.createCell(5);
					if (record.getR76_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR76_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row76
					// Column G
					cell6 = row.createCell(6);
					if (record.getR76_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR76_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row76
					// Column H
					cell7 = row.createCell(7);
					if (record.getR76_OTHER() != null) {
						cell7.setCellValue(record.getR76_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row76
					// Column I
					cell8 = row.createCell(8);
					if (record.getR76_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR76_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row76
					// Column J
					cell9 = row.createCell(9);
					if (record.getR76_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR76_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row77
					row = sheet.getRow(76);

					// row77
					// Column C
					cell2 = row.createCell(2);
					if (record.getR77_ISSUER() != null) {
						cell2.setCellValue(record.getR77_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row77
					// Column D
					cell3 = row.createCell(3);
					if (record.getR77_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR77_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row77
					// Column E
					cell4 = row.createCell(4);
					if (record.getR77_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR77_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row77
					// Column F
					cell5 = row.createCell(5);
					if (record.getR77_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR77_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row77
					// Column G
					cell6 = row.createCell(6);
					if (record.getR77_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR77_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row77
					// Column H
					cell7 = row.createCell(7);
					if (record.getR77_OTHER() != null) {
						cell7.setCellValue(record.getR77_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row77
					// Column I
					cell8 = row.createCell(8);
					if (record.getR77_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR77_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row77
					// Column J
					cell9 = row.createCell(9);
					if (record.getR77_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR77_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row78
					row = sheet.getRow(77);

					// row78
					// Column C
					cell2 = row.createCell(2);
					if (record.getR78_ISSUER() != null) {
						cell2.setCellValue(record.getR78_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row78
					// Column D
					cell3 = row.createCell(3);
					if (record.getR78_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR78_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row78
					// Column E
					cell4 = row.createCell(4);
					if (record.getR78_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR78_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row78
					// Column F
					cell5 = row.createCell(5);
					if (record.getR78_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR78_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row78
					// Column G
					cell6 = row.createCell(6);
					if (record.getR78_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR78_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row78
					// Column H
					cell7 = row.createCell(7);
					if (record.getR78_OTHER() != null) {
						cell7.setCellValue(record.getR78_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row78
					// Column I
					cell8 = row.createCell(8);
					if (record.getR78_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR78_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row78
					// Column J
					cell9 = row.createCell(9);
					if (record.getR78_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR78_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row79
					row = sheet.getRow(78);

					// row79
					// Column C
					cell2 = row.createCell(2);
					if (record.getR79_ISSUER() != null) {
						cell2.setCellValue(record.getR79_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row79
					// Column D
					cell3 = row.createCell(3);
					if (record.getR79_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR79_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row79
					// Column E
					cell4 = row.createCell(4);
					if (record.getR79_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR79_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row79
					// Column F
					cell5 = row.createCell(5);
					if (record.getR79_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR79_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row79
					// Column G
					cell6 = row.createCell(6);
					if (record.getR79_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR79_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row79
					// Column H
					cell7 = row.createCell(7);
					if (record.getR79_OTHER() != null) {
						cell7.setCellValue(record.getR79_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row79
					// Column I
					cell8 = row.createCell(8);
					if (record.getR79_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR79_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row79
					// Column J
					cell9 = row.createCell(9);
					if (record.getR79_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR79_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row80
					row = sheet.getRow(79);

					// row80
					// Column C
					cell2 = row.createCell(2);
					if (record.getR80_ISSUER() != null) {
						cell2.setCellValue(record.getR80_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row80
					// Column D
					cell3 = row.createCell(3);
					if (record.getR80_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR80_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row80
					// Column E
					cell4 = row.createCell(4);
					if (record.getR80_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR80_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row80
					// Column F
					cell5 = row.createCell(5);
					if (record.getR80_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR80_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row80
					// Column G
					cell6 = row.createCell(6);
					if (record.getR80_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR80_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row80
					// Column H
					cell7 = row.createCell(7);
					if (record.getR80_OTHER() != null) {
						cell7.setCellValue(record.getR80_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row80
					// Column I
					cell8 = row.createCell(8);
					if (record.getR80_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR80_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row80
					// Column J
					cell9 = row.createCell(9);
					if (record.getR80_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR80_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					row = sheet.getRow(80);
					Cell cell1 = row.createCell(1);
					if (record.getR81_PRODUCT() != null) {
						cell1.setCellValue(record.getR81_PRODUCT().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(numberStyle);
					}

					// Column C
					cell2 = row.createCell(2);
					if (record.getR81_ISSUER() != null) {
						cell2.setCellValue(record.getR81_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row80
					// Column D
					cell3 = row.createCell(3);
					if (record.getR81_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR81_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					row = sheet.getRow(80);

					cell2 = row.getCell(1);
					if (cell2 == null)
						cell2 = row.createCell(1);

					if (record.getR81_PRODUCT() != null) {
						cell2.setCellValue(record.getR81_PRODUCT().doubleValue());
					} else {
						cell2.setCellValue(0); // or leave previous value
					}

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);

					if (record.getR81_ISSUER() != null) {
						cell2.setCellValue(record.getR81_ISSUER().doubleValue());
					} else {
						cell2.setCellValue(0); // or leave previous value
					}

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);

					if (record.getR81_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR81_ISSUES_RATING().doubleValue());
					} else {
						cell3.setCellValue(0); // or leave previous value
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

	public byte[] BRRS_M_SRWA_12HArchivalExcel(String filename, String reportId, String fromdate,
			String todate, String currency, String dtltype,
			String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_SRWA_12H_Archival_Summary_Entity> dataList = M_SRWA_12H_Archival_Summary_Repo
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
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_SRWA_12H_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR12_ISSUER() != null) {
						cell2.setCellValue(record.getR12_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR12_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR12_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row12
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR12_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR12_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row12
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR12_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR12_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row12
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR12_OTHER() != null) {
						cell7.setCellValue(record.getR12_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row12
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR12_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR12_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row12
					// Column J
					Cell cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR13_ISSUER() != null) {
						cell2.setCellValue(record.getR13_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR13_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR13_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR13_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR13_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR13_OTHER() != null) {
						cell7.setCellValue(record.getR13_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row13
					// Column I
					cell8 = row.createCell(8);
					if (record.getR13_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR13_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row13
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR14_ISSUER() != null) {
						cell2.setCellValue(record.getR14_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR14_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR14_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR14_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR14_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row14
					// Column H
					cell7 = row.createCell(7);
					if (record.getR14_OTHER() != null) {
						cell7.setCellValue(record.getR14_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row14
					// Column I
					cell8 = row.createCell(8);
					if (record.getR14_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR14_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row14
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR15_ISSUER() != null) {
						cell2.setCellValue(record.getR15_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR15_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row15
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR15_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR15_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row15
					// Column G
					cell6 = row.createCell(6);
					if (record.getR15_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR15_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row15
					// Column H
					cell7 = row.createCell(7);
					if (record.getR15_OTHER() != null) {
						cell7.setCellValue(record.getR15_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row15
					// Column I
					cell8 = row.createCell(8);
					if (record.getR15_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR15_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row15
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR16_ISSUER() != null) {
						cell2.setCellValue(record.getR16_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR16_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row16
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR16_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR16_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row16
					// Column G
					cell6 = row.createCell(6);
					if (record.getR16_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR16_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row16
					// Column H
					cell7 = row.createCell(7);
					if (record.getR16_OTHER() != null) {
						cell7.setCellValue(record.getR16_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row16
					// Column I
					cell8 = row.createCell(8);
					if (record.getR16_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR16_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row16
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR17_ISSUER() != null) {
						cell2.setCellValue(record.getR17_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					// Column D
					cell3 = row.createCell(3);
					if (record.getR17_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR17_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row17
					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR17_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR17_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row17
					// Column G
					cell6 = row.createCell(6);
					if (record.getR17_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR17_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row17
					// Column H
					cell7 = row.createCell(7);
					if (record.getR17_OTHER() != null) {
						cell7.setCellValue(record.getR17_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row17
					// Column I
					cell8 = row.createCell(8);
					if (record.getR17_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR17_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row17
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR18_ISSUER() != null) {
						cell2.setCellValue(record.getR18_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					// Column D
					cell3 = row.createCell(3);
					if (record.getR18_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR18_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row18
					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR18_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR18_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row18
					// Column G
					cell6 = row.createCell(6);
					if (record.getR18_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR18_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row18
					// Column H
					cell7 = row.createCell(7);
					if (record.getR18_OTHER() != null) {
						cell7.setCellValue(record.getR18_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row18
					// Column I
					cell8 = row.createCell(8);
					if (record.getR18_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR18_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row18
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR19_ISSUER() != null) {
						cell2.setCellValue(record.getR19_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					// Column D
					cell3 = row.createCell(3);
					if (record.getR19_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR19_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row19
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR19_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR19_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row19
					// Column G
					cell6 = row.createCell(6);
					if (record.getR19_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR19_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row19
					// Column H
					cell7 = row.createCell(7);
					if (record.getR19_OTHER() != null) {
						cell7.setCellValue(record.getR19_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row19
					// Column I
					cell8 = row.createCell(8);
					if (record.getR19_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR19_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row19
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR20_ISSUER() != null) {
						cell2.setCellValue(record.getR20_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR20_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row20
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR20_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR20_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row20
					// Column G
					cell6 = row.createCell(6);
					if (record.getR20_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR20_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row20
					// Column H
					cell7 = row.createCell(7);
					if (record.getR20_OTHER() != null) {
						cell7.setCellValue(record.getR20_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row20
					// Column I
					cell8 = row.createCell(8);
					if (record.getR20_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR20_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row20
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR21_ISSUER() != null) {
						cell2.setCellValue(record.getR21_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR21_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row21
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR21_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR21_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row21
					// Column G
					cell6 = row.createCell(6);
					if (record.getR21_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR21_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row21
					// Column H
					cell7 = row.createCell(7);
					if (record.getR21_OTHER() != null) {
						cell7.setCellValue(record.getR21_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row21
					// Column I
					cell8 = row.createCell(8);
					if (record.getR21_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR21_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row21
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR22_ISSUER() != null) {
						cell2.setCellValue(record.getR22_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR22_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR22_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR22_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR22_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row22
					// Column H
					cell7 = row.createCell(7);
					if (record.getR22_OTHER() != null) {
						cell7.setCellValue(record.getR22_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row22
					// Column I
					cell8 = row.createCell(8);
					if (record.getR22_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR22_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row22
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR23_ISSUER() != null) {
						cell2.setCellValue(record.getR23_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR23_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR23_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR23_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR23_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row23
					// Column H
					cell7 = row.createCell(7);
					if (record.getR23_OTHER() != null) {
						cell7.setCellValue(record.getR23_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row23
					// Column I
					cell8 = row.createCell(8);
					if (record.getR23_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR23_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row23
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR24_ISSUER() != null) {
						cell2.setCellValue(record.getR24_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR24_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR24_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR24_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR24_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row24
					// Column H
					cell7 = row.createCell(7);
					if (record.getR24_OTHER() != null) {
						cell7.setCellValue(record.getR24_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row24
					// Column I
					cell8 = row.createCell(8);
					if (record.getR24_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR24_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row24
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR25_ISSUER() != null) {
						cell2.setCellValue(record.getR25_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR25_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row25
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR25_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR25_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row25
					// Column G
					cell6 = row.createCell(6);
					if (record.getR25_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR25_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row25
					// Column H
					cell7 = row.createCell(7);
					if (record.getR25_OTHER() != null) {
						cell7.setCellValue(record.getR25_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row25
					// Column I
					cell8 = row.createCell(8);
					if (record.getR25_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR25_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row25
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR26_ISSUER() != null) {
						cell2.setCellValue(record.getR26_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR26_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row26
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR26_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR26_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row26
					// Column G
					cell6 = row.createCell(6);
					if (record.getR26_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR26_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row26
					// Column H
					cell7 = row.createCell(7);
					if (record.getR26_OTHER() != null) {
						cell7.setCellValue(record.getR26_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row26
					// Column I
					cell8 = row.createCell(8);
					if (record.getR26_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR26_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row26
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR27_ISSUER() != null) {
						cell2.setCellValue(record.getR27_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR27_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row27
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR27_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR27_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row27
					// Column G
					cell6 = row.createCell(6);
					if (record.getR27_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR27_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row27
					// Column H
					cell7 = row.createCell(7);
					if (record.getR27_OTHER() != null) {
						cell7.setCellValue(record.getR27_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row27
					// Column I
					cell8 = row.createCell(8);
					if (record.getR27_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR27_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row27
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR28_ISSUER() != null) {
						cell2.setCellValue(record.getR28_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR28_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row28
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR28_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR28_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row28
					// Column G
					cell6 = row.createCell(6);
					if (record.getR28_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR28_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row28
					// Column H
					cell7 = row.createCell(7);
					if (record.getR28_OTHER() != null) {
						cell7.setCellValue(record.getR28_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row28
					// Column I
					cell8 = row.createCell(8);
					if (record.getR28_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR28_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row28
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR29_ISSUER() != null) {
						cell2.setCellValue(record.getR29_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row29
					// Column D
					cell3 = row.createCell(3);
					if (record.getR29_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR29_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row29
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR29_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR29_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row29
					// Column G
					cell6 = row.createCell(6);
					if (record.getR29_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR29_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row29
					// Column H
					cell7 = row.createCell(7);
					if (record.getR29_OTHER() != null) {
						cell7.setCellValue(record.getR29_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row29
					// Column I
					cell8 = row.createCell(8);
					if (record.getR29_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR29_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row29
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR30_ISSUER() != null) {
						cell2.setCellValue(record.getR30_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					// Column D
					cell3 = row.createCell(3);
					if (record.getR30_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR30_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row30
					// Column E
					cell4 = row.createCell(4);
					if (record.getR30_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR30_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record.getR30_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR30_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row30
					// Column G
					cell6 = row.createCell(6);
					if (record.getR30_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR30_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row30
					// Column H
					cell7 = row.createCell(7);
					if (record.getR30_OTHER() != null) {
						cell7.setCellValue(record.getR30_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row30
					// Column I
					cell8 = row.createCell(8);
					if (record.getR30_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR30_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row30
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR31_ISSUER() != null) {
						cell2.setCellValue(record.getR31_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR31_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row31
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR31_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR31_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row31
					// Column G
					cell6 = row.createCell(6);
					if (record.getR31_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR31_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row31
					// Column H
					cell7 = row.createCell(7);
					if (record.getR31_OTHER() != null) {
						cell7.setCellValue(record.getR31_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row31
					// Column I
					cell8 = row.createCell(8);
					if (record.getR31_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR31_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row31
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR32_ISSUER() != null) {
						cell2.setCellValue(record.getR32_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row32
					// Column D
					cell3 = row.createCell(3);
					if (record.getR32_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR32_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row32
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR32_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR32_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row32
					// Column G
					cell6 = row.createCell(6);
					if (record.getR32_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR32_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row32
					// Column H
					cell7 = row.createCell(7);
					if (record.getR32_OTHER() != null) {
						cell7.setCellValue(record.getR32_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row32
					// Column I
					cell8 = row.createCell(8);
					if (record.getR32_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR32_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row32
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR33_ISSUER() != null) {
						cell2.setCellValue(record.getR33_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR33_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR33_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR33_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR33_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR33_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR33_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR33_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR33_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR33_OTHER() != null) {
						cell7.setCellValue(record.getR33_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR33_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR33_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR34_ISSUER() != null) {
						cell2.setCellValue(record.getR34_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR34_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR34_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR34_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR34_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR34_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR34_OTHER() != null) {
						cell7.setCellValue(record.getR34_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR34_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR34_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR35_ISSUER() != null) {
						cell2.setCellValue(record.getR35_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR35_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR35_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR35_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR35_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR35_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR35_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR35_OTHER() != null) {
						cell7.setCellValue(record.getR35_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR35_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR35_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR36_ISSUER() != null) {
						cell2.setCellValue(record.getR36_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR36_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR36_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR36_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR36_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR36_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR36_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR36_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR36_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR36_OTHER() != null) {
						cell7.setCellValue(record.getR36_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR36_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR36_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR37_ISSUER() != null) {
						cell2.setCellValue(record.getR37_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR37_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR37_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR37_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR37_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR37_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR37_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR37_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR37_OTHER() != null) {
						cell7.setCellValue(record.getR37_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR37_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR37_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR37_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR37_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row38
					row = sheet.getRow(37);

					// row38
					// Column C
					cell2 = row.createCell(2);
					if (record.getR38_ISSUER() != null) {
						cell2.setCellValue(record.getR38_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR38_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR38_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR38_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR38_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR38_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR38_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR38_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR38_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR38_OTHER() != null) {
						cell7.setCellValue(record.getR38_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR38_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR38_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR38_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR38_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row39
					row = sheet.getRow(38);

					// row39
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_ISSUER() != null) {
						cell2.setCellValue(record.getR39_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR39_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR39_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR39_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR39_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR39_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR39_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR39_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR39_OTHER() != null) {
						cell7.setCellValue(record.getR39_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR39_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR39_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR39_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR39_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row40
					row = sheet.getRow(39);

					// row40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_ISSUER() != null) {
						cell2.setCellValue(record.getR40_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR40_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR40_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR40_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR40_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR40_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR40_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR40_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR40_OTHER() != null) {
						cell7.setCellValue(record.getR40_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR40_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR40_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR40_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR40_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row41
					row = sheet.getRow(40);

					// row41
					// Column C
					cell2 = row.createCell(2);
					if (record.getR41_ISSUER() != null) {
						cell2.setCellValue(record.getR41_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR41_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR41_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR41_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR41_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR41_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR41_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR41_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR41_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR41_OTHER() != null) {
						cell7.setCellValue(record.getR41_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR41_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR41_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR41_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR41_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row42
					row = sheet.getRow(41);

					// row42
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_ISSUER() != null) {
						cell2.setCellValue(record.getR42_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR42_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR42_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR42_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR42_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR42_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR42_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR42_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR42_OTHER() != null) {
						cell7.setCellValue(record.getR42_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR42_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR42_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR42_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR42_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row43
					row = sheet.getRow(42);

					// row43
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_ISSUER() != null) {
						cell2.setCellValue(record.getR43_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR43_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR43_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR43_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR43_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR43_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR43_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR43_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR43_OTHER() != null) {
						cell7.setCellValue(record.getR43_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR43_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR43_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR43_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR43_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row44
					row = sheet.getRow(43);

					// row44
					// Column C
					cell2 = row.createCell(2);
					if (record.getR44_ISSUER() != null) {
						cell2.setCellValue(record.getR44_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR44_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR44_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR44_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR44_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR44_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR44_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR44_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR44_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR44_OTHER() != null) {
						cell7.setCellValue(record.getR44_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR44_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR44_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR44_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR44_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row45
					row = sheet.getRow(44);

					// row45
					// Column C
					cell2 = row.createCell(2);
					if (record.getR45_ISSUER() != null) {
						cell2.setCellValue(record.getR45_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR45_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR45_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR45_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR45_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR45_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR45_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR45_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR45_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR45_OTHER() != null) {
						cell7.setCellValue(record.getR45_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR45_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR45_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR45_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR45_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row46
					row = sheet.getRow(45);

					// row46
					// Column C
					cell2 = row.createCell(2);
					if (record.getR46_ISSUER() != null) {
						cell2.setCellValue(record.getR46_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR46_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR46_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR46_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR46_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR46_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR46_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR46_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR46_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR46_OTHER() != null) {
						cell7.setCellValue(record.getR46_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR46_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR46_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR46_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR46_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row47
					row = sheet.getRow(46);

					// row47
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_ISSUER() != null) {
						cell2.setCellValue(record.getR47_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR47_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR47_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR47_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR47_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR47_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR47_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR47_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR47_OTHER() != null) {
						cell7.setCellValue(record.getR47_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR47_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR47_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR47_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR47_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row48
					row = sheet.getRow(47);

					// row48
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_ISSUER() != null) {
						cell2.setCellValue(record.getR48_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR48_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR48_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR48_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR48_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR48_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR48_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR48_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR48_OTHER() != null) {
						cell7.setCellValue(record.getR48_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR48_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR48_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR48_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR48_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row49
					row = sheet.getRow(48);

					// row49
					// Column C
					cell2 = row.createCell(2);
					if (record.getR49_ISSUER() != null) {
						cell2.setCellValue(record.getR49_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR49_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR49_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR49_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR49_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR49_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR49_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR49_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR49_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR49_OTHER() != null) {
						cell7.setCellValue(record.getR49_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR49_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR49_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR49_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR49_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row50
					row = sheet.getRow(49);

					// row50
					// Column C
					cell2 = row.createCell(2);
					if (record.getR50_ISSUER() != null) {
						cell2.setCellValue(record.getR50_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR50_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR50_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR50_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR50_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR50_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR50_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR50_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR50_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR50_OTHER() != null) {
						cell7.setCellValue(record.getR50_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR50_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR50_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR50_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR50_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row51
					row = sheet.getRow(50);

					// row51
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_ISSUER() != null) {
						cell2.setCellValue(record.getR51_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR51_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row51
					// Column E
					cell4 = row.createCell(4);
					if (record.getR51_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR51_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row51
					// Column F
					cell5 = row.createCell(5);
					if (record.getR51_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR51_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row51
					// Column G
					cell6 = row.createCell(6);
					if (record.getR51_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR51_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row51
					// Column H
					cell7 = row.createCell(7);
					if (record.getR51_OTHER() != null) {
						cell7.setCellValue(record.getR51_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row51
					// Column I
					cell8 = row.createCell(8);
					if (record.getR51_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR51_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row51
					// Column J
					cell9 = row.createCell(9);
					if (record.getR51_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR51_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row52
					row = sheet.getRow(51);

					// row52
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_ISSUER() != null) {
						cell2.setCellValue(record.getR52_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR52_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row52
					// Column E
					cell4 = row.createCell(4);
					if (record.getR52_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR52_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row52
					// Column F
					cell5 = row.createCell(5);
					if (record.getR52_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR52_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row52
					// Column G
					cell6 = row.createCell(6);
					if (record.getR52_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR52_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row52
					// Column H
					cell7 = row.createCell(7);
					if (record.getR52_OTHER() != null) {
						cell7.setCellValue(record.getR52_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row52
					// Column I
					cell8 = row.createCell(8);
					if (record.getR52_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR52_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row52
					// Column J
					cell9 = row.createCell(9);
					if (record.getR52_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR52_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row53
					row = sheet.getRow(52);

					// row53
					// Column C
					cell2 = row.createCell(2);
					if (record.getR53_ISSUER() != null) {
						cell2.setCellValue(record.getR53_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					// Column D
					cell3 = row.createCell(3);
					if (record.getR53_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR53_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row53
					// Column E
					cell4 = row.createCell(4);
					if (record.getR53_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR53_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row53
					// Column F
					cell5 = row.createCell(5);
					if (record.getR53_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR53_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row53
					// Column G
					cell6 = row.createCell(6);
					if (record.getR53_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR53_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row53
					// Column H
					cell7 = row.createCell(7);
					if (record.getR53_OTHER() != null) {
						cell7.setCellValue(record.getR53_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row53
					// Column I
					cell8 = row.createCell(8);
					if (record.getR53_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR53_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row53
					// Column J
					cell9 = row.createCell(9);
					if (record.getR53_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR53_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row54
					row = sheet.getRow(53);

					// row54
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_ISSUER() != null) {
						cell2.setCellValue(record.getR54_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR54_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row54
					// Column E
					cell4 = row.createCell(4);
					if (record.getR54_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR54_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row54
					// Column F
					cell5 = row.createCell(5);
					if (record.getR54_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR54_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row54
					// Column G
					cell6 = row.createCell(6);
					if (record.getR54_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR54_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row54
					// Column H
					cell7 = row.createCell(7);
					if (record.getR54_OTHER() != null) {
						cell7.setCellValue(record.getR54_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row54
					// Column I
					cell8 = row.createCell(8);
					if (record.getR54_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR54_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row54
					// Column J
					cell9 = row.createCell(9);
					if (record.getR54_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR54_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row55
					row = sheet.getRow(54);

					// row55
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_ISSUER() != null) {
						cell2.setCellValue(record.getR55_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR55_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row55
					// Column E
					cell4 = row.createCell(4);
					if (record.getR55_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR55_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row55
					// Column F
					cell5 = row.createCell(5);
					if (record.getR55_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR55_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row55
					// Column G
					cell6 = row.createCell(6);
					if (record.getR55_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR55_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row55
					// Column H
					cell7 = row.createCell(7);
					if (record.getR55_OTHER() != null) {
						cell7.setCellValue(record.getR55_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row55
					// Column I
					cell8 = row.createCell(8);
					if (record.getR55_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR55_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row55
					// Column J
					cell9 = row.createCell(9);
					if (record.getR55_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR55_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row56
					row = sheet.getRow(55);

					// row56
					// Column C
					cell2 = row.createCell(2);
					if (record.getR56_ISSUER() != null) {
						cell2.setCellValue(record.getR56_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row56
					// Column D
					cell3 = row.createCell(3);
					if (record.getR56_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR56_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row56
					// Column E
					cell4 = row.createCell(4);
					if (record.getR56_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR56_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row56
					// Column F
					cell5 = row.createCell(5);
					if (record.getR56_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR56_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row56
					// Column G
					cell6 = row.createCell(6);
					if (record.getR56_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR56_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row56
					// Column H
					cell7 = row.createCell(7);
					if (record.getR56_OTHER() != null) {
						cell7.setCellValue(record.getR56_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row56
					// Column I
					cell8 = row.createCell(8);
					if (record.getR56_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR56_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row56
					// Column J
					cell9 = row.createCell(9);
					if (record.getR56_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR56_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row57
					row = sheet.getRow(56);

					// row57
					// Column C
					cell2 = row.createCell(2);
					if (record.getR57_ISSUER() != null) {
						cell2.setCellValue(record.getR57_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row57
					// Column D
					cell3 = row.createCell(3);
					if (record.getR57_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR57_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row57
					// Column E
					cell4 = row.createCell(4);
					if (record.getR57_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR57_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row57
					// Column F
					cell5 = row.createCell(5);
					if (record.getR57_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR57_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row57
					// Column G
					cell6 = row.createCell(6);
					if (record.getR57_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR57_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row57
					// Column H
					cell7 = row.createCell(7);
					if (record.getR57_OTHER() != null) {
						cell7.setCellValue(record.getR57_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row57
					// Column I
					cell8 = row.createCell(8);
					if (record.getR57_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR57_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row57
					// Column J
					cell9 = row.createCell(9);
					if (record.getR57_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR57_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row58
					row = sheet.getRow(57);

					// row58
					// Column C
					cell2 = row.createCell(2);
					if (record.getR58_ISSUER() != null) {
						cell2.setCellValue(record.getR58_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row58
					// Column D
					cell3 = row.createCell(3);
					if (record.getR58_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR58_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row58
					// Column E
					cell4 = row.createCell(4);
					if (record.getR58_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR58_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row58
					// Column F
					cell5 = row.createCell(5);
					if (record.getR58_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR58_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row58
					// Column G
					cell6 = row.createCell(6);
					if (record.getR58_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR58_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row58
					// Column H
					cell7 = row.createCell(7);
					if (record.getR58_OTHER() != null) {
						cell7.setCellValue(record.getR58_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row58
					// Column I
					cell8 = row.createCell(8);
					if (record.getR58_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR58_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row58
					// Column J
					cell9 = row.createCell(9);
					if (record.getR58_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR58_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row59
					row = sheet.getRow(58);

					// row59
					// Column C
					cell2 = row.createCell(2);
					if (record.getR59_ISSUER() != null) {
						cell2.setCellValue(record.getR59_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row59
					// Column D
					cell3 = row.createCell(3);
					if (record.getR59_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR59_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row59
					// Column E
					cell4 = row.createCell(4);
					if (record.getR59_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR59_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row59
					// Column F
					cell5 = row.createCell(5);
					if (record.getR59_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR59_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row59
					// Column G
					cell6 = row.createCell(6);
					if (record.getR59_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR59_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row59
					// Column H
					cell7 = row.createCell(7);
					if (record.getR59_OTHER() != null) {
						cell7.setCellValue(record.getR59_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row59
					// Column I
					cell8 = row.createCell(8);
					if (record.getR59_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR59_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row59
					// Column J
					cell9 = row.createCell(9);
					if (record.getR59_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR59_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row60
					row = sheet.getRow(59);

					// row60
					// Column C
					cell2 = row.createCell(2);
					if (record.getR60_ISSUER() != null) {
						cell2.setCellValue(record.getR60_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row60
					// Column D
					cell3 = row.createCell(3);
					if (record.getR60_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR60_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row60
					// Column E
					cell4 = row.createCell(4);
					if (record.getR60_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR60_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row60
					// Column F
					cell5 = row.createCell(5);
					if (record.getR60_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR60_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row60
					// Column G
					cell6 = row.createCell(6);
					if (record.getR60_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR60_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row60
					// Column H
					cell7 = row.createCell(7);
					if (record.getR60_OTHER() != null) {
						cell7.setCellValue(record.getR60_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row60
					// Column I
					cell8 = row.createCell(8);
					if (record.getR60_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR60_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row60
					// Column J
					cell9 = row.createCell(9);
					if (record.getR60_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR60_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row61
					row = sheet.getRow(60);

					// row61
					// Column C
					cell2 = row.createCell(2);
					if (record.getR61_ISSUER() != null) {
						cell2.setCellValue(record.getR61_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row61
					// Column D
					cell3 = row.createCell(3);
					if (record.getR61_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR61_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row61
					// Column E
					cell4 = row.createCell(4);
					if (record.getR61_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR61_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row61
					// Column F
					cell5 = row.createCell(5);
					if (record.getR61_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR61_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row61
					// Column G
					cell6 = row.createCell(6);
					if (record.getR61_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR61_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row61
					// Column H
					cell7 = row.createCell(7);
					if (record.getR61_OTHER() != null) {
						cell7.setCellValue(record.getR61_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row61
					// Column I
					cell8 = row.createCell(8);
					if (record.getR61_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR61_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row61
					// Column J
					cell9 = row.createCell(9);
					if (record.getR61_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR61_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row62
					row = sheet.getRow(61);

					// row62
					// Column C
					cell2 = row.createCell(2);
					if (record.getR62_ISSUER() != null) {
						cell2.setCellValue(record.getR62_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row62
					// Column D
					cell3 = row.createCell(3);
					if (record.getR62_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR62_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row62
					// Column E
					cell4 = row.createCell(4);
					if (record.getR62_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR62_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row62
					// Column F
					cell5 = row.createCell(5);
					if (record.getR62_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR62_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row62
					// Column G
					cell6 = row.createCell(6);
					if (record.getR62_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR62_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row62
					// Column H
					cell7 = row.createCell(7);
					if (record.getR62_OTHER() != null) {
						cell7.setCellValue(record.getR62_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row62
					// Column I
					cell8 = row.createCell(8);
					if (record.getR62_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR62_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row62
					// Column J
					cell9 = row.createCell(9);
					if (record.getR62_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR62_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row63
					row = sheet.getRow(62);

					// row63
					// Column C
					cell2 = row.createCell(2);
					if (record.getR63_ISSUER() != null) {
						cell2.setCellValue(record.getR63_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row63
					// Column D
					cell3 = row.createCell(3);
					if (record.getR63_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR63_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row63
					// Column E
					cell4 = row.createCell(4);
					if (record.getR63_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR63_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row63
					// Column F
					cell5 = row.createCell(5);
					if (record.getR63_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR63_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row63
					// Column G
					cell6 = row.createCell(6);
					if (record.getR63_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR63_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row63
					// Column H
					cell7 = row.createCell(7);
					if (record.getR63_OTHER() != null) {
						cell7.setCellValue(record.getR63_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row63
					// Column I
					cell8 = row.createCell(8);
					if (record.getR63_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR63_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row63
					// Column J
					cell9 = row.createCell(9);
					if (record.getR63_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR63_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row64
					row = sheet.getRow(63);

					// row64
					// Column C
					cell2 = row.createCell(2);
					if (record.getR64_ISSUER() != null) {
						cell2.setCellValue(record.getR64_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row64
					// Column D
					cell3 = row.createCell(3);
					if (record.getR64_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR64_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row64
					// Column E
					cell4 = row.createCell(4);
					if (record.getR64_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR64_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row64
					// Column F
					cell5 = row.createCell(5);
					if (record.getR64_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR64_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row64
					// Column G
					cell6 = row.createCell(6);
					if (record.getR64_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR64_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row64
					// Column H
					cell7 = row.createCell(7);
					if (record.getR64_OTHER() != null) {
						cell7.setCellValue(record.getR64_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row64
					// Column I
					cell8 = row.createCell(8);
					if (record.getR64_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR64_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row64
					// Column J
					cell9 = row.createCell(9);
					if (record.getR64_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR64_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row65
					row = sheet.getRow(64);

					// row65
					// Column C
					cell2 = row.createCell(2);
					if (record.getR65_ISSUER() != null) {
						cell2.setCellValue(record.getR65_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row65
					// Column D
					cell3 = row.createCell(3);
					if (record.getR65_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR65_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row65
					// Column E
					cell4 = row.createCell(4);
					if (record.getR65_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR65_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row65
					// Column F
					cell5 = row.createCell(5);
					if (record.getR65_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR65_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row65
					// Column G
					cell6 = row.createCell(6);
					if (record.getR65_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR65_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row65
					// Column H
					cell7 = row.createCell(7);
					if (record.getR65_OTHER() != null) {
						cell7.setCellValue(record.getR65_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row65
					// Column I
					cell8 = row.createCell(8);
					if (record.getR65_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR65_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row65
					// Column J
					cell9 = row.createCell(9);
					if (record.getR65_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR65_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row66
					row = sheet.getRow(65);

					// row66
					// Column C
					cell2 = row.createCell(2);
					if (record.getR66_ISSUER() != null) {
						cell2.setCellValue(record.getR66_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row66
					// Column D
					cell3 = row.createCell(3);
					if (record.getR66_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR66_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row66
					// Column E
					cell4 = row.createCell(4);
					if (record.getR66_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR66_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row66
					// Column F
					cell5 = row.createCell(5);
					if (record.getR66_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR66_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row66
					// Column G
					cell6 = row.createCell(6);
					if (record.getR66_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR66_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row66
					// Column H
					cell7 = row.createCell(7);
					if (record.getR66_OTHER() != null) {
						cell7.setCellValue(record.getR66_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row66
					// Column I
					cell8 = row.createCell(8);
					if (record.getR66_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR66_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row66
					// Column J
					cell9 = row.createCell(9);
					if (record.getR66_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR66_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row67
					row = sheet.getRow(66);

					// row67
					// Column C
					cell2 = row.createCell(2);
					if (record.getR67_ISSUER() != null) {
						cell2.setCellValue(record.getR67_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row67
					// Column D
					cell3 = row.createCell(3);
					if (record.getR67_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR67_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row67
					// Column E
					cell4 = row.createCell(4);
					if (record.getR67_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR67_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row67
					// Column F
					cell5 = row.createCell(5);
					if (record.getR67_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR67_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row67
					// Column G
					cell6 = row.createCell(6);
					if (record.getR67_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR67_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row67
					// Column H
					cell7 = row.createCell(7);
					if (record.getR67_OTHER() != null) {
						cell7.setCellValue(record.getR67_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row67
					// Column I
					cell8 = row.createCell(8);
					if (record.getR67_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR67_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row67
					// Column J
					cell9 = row.createCell(9);
					if (record.getR67_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR67_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row68
					row = sheet.getRow(67);

					// row68
					// Column C
					cell2 = row.createCell(2);
					if (record.getR68_ISSUER() != null) {
						cell2.setCellValue(record.getR68_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row68
					// Column D
					cell3 = row.createCell(3);
					if (record.getR68_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR68_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row68
					// Column E
					cell4 = row.createCell(4);
					if (record.getR68_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR68_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row68
					// Column F
					cell5 = row.createCell(5);
					if (record.getR68_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR68_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row68
					// Column G
					cell6 = row.createCell(6);
					if (record.getR68_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR68_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row68
					// Column H
					cell7 = row.createCell(7);
					if (record.getR68_OTHER() != null) {
						cell7.setCellValue(record.getR68_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row68
					// Column I
					cell8 = row.createCell(8);
					if (record.getR68_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR68_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row68
					// Column J
					cell9 = row.createCell(9);
					if (record.getR68_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR68_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row69
					row = sheet.getRow(68);

					// row69
					// Column C
					cell2 = row.createCell(2);
					if (record.getR69_ISSUER() != null) {
						cell2.setCellValue(record.getR69_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row69
					// Column D
					cell3 = row.createCell(3);
					if (record.getR69_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR69_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row69
					// Column E
					cell4 = row.createCell(4);
					if (record.getR69_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR69_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row69
					// Column F
					cell5 = row.createCell(5);
					if (record.getR69_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR69_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row69
					// Column G
					cell6 = row.createCell(6);
					if (record.getR69_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR69_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row69
					// Column H
					cell7 = row.createCell(7);
					if (record.getR69_OTHER() != null) {
						cell7.setCellValue(record.getR69_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row69
					// Column I
					cell8 = row.createCell(8);
					if (record.getR69_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR69_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row69
					// Column J
					cell9 = row.createCell(9);
					if (record.getR69_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR69_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row70
					row = sheet.getRow(69);

					// row70
					// Column C
					cell2 = row.createCell(2);
					if (record.getR70_ISSUER() != null) {
						cell2.setCellValue(record.getR70_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row70
					// Column D
					cell3 = row.createCell(3);
					if (record.getR70_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR70_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row70
					// Column E
					cell4 = row.createCell(4);
					if (record.getR70_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR70_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row70
					// Column F
					cell5 = row.createCell(5);
					if (record.getR70_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR70_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row70
					// Column G
					cell6 = row.createCell(6);
					if (record.getR70_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR70_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row70
					// Column H
					cell7 = row.createCell(7);
					if (record.getR70_OTHER() != null) {
						cell7.setCellValue(record.getR70_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row70
					// Column I
					cell8 = row.createCell(8);
					if (record.getR70_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR70_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row70
					// Column J
					cell9 = row.createCell(9);
					if (record.getR70_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR70_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row71
					row = sheet.getRow(70);

					// row71
					// Column C
					cell2 = row.createCell(2);
					if (record.getR71_ISSUER() != null) {
						cell2.setCellValue(record.getR71_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row71
					// Column D
					cell3 = row.createCell(3);
					if (record.getR71_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR71_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row71
					// Column E
					cell4 = row.createCell(4);
					if (record.getR71_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR71_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row71
					// Column F
					cell5 = row.createCell(5);
					if (record.getR71_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR71_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row71
					// Column G
					cell6 = row.createCell(6);
					if (record.getR71_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR71_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row71
					// Column H
					cell7 = row.createCell(7);
					if (record.getR71_OTHER() != null) {
						cell7.setCellValue(record.getR71_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row71
					// Column I
					cell8 = row.createCell(8);
					if (record.getR71_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR71_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row71
					// Column J
					cell9 = row.createCell(9);
					if (record.getR71_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR71_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row72
					row = sheet.getRow(71);

					// row72
					// Column C
					cell2 = row.createCell(2);
					if (record.getR72_ISSUER() != null) {
						cell2.setCellValue(record.getR72_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row72
					// Column D
					cell3 = row.createCell(3);
					if (record.getR72_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR72_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row72
					// Column E
					cell4 = row.createCell(4);
					if (record.getR72_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR72_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row72
					// Column F
					cell5 = row.createCell(5);
					if (record.getR72_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR72_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row72
					// Column G
					cell6 = row.createCell(6);
					if (record.getR72_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR72_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row72
					// Column H
					cell7 = row.createCell(7);
					if (record.getR72_OTHER() != null) {
						cell7.setCellValue(record.getR72_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row72
					// Column I
					cell8 = row.createCell(8);
					if (record.getR72_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR72_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row72
					// Column J
					cell9 = row.createCell(9);
					if (record.getR72_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR72_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row73
					row = sheet.getRow(72);

					// row73
					// Column C
					cell2 = row.createCell(2);
					if (record.getR73_ISSUER() != null) {
						cell2.setCellValue(record.getR73_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row73
					// Column D
					cell3 = row.createCell(3);
					if (record.getR73_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR73_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row73
					// Column E
					cell4 = row.createCell(4);
					if (record.getR73_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR73_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row73
					// Column F
					cell5 = row.createCell(5);
					if (record.getR73_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR73_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row73
					// Column G
					cell6 = row.createCell(6);
					if (record.getR73_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR73_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row73
					// Column H
					cell7 = row.createCell(7);
					if (record.getR73_OTHER() != null) {
						cell7.setCellValue(record.getR73_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row73
					// Column I
					cell8 = row.createCell(8);
					if (record.getR73_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR73_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row73
					// Column J
					cell9 = row.createCell(9);
					if (record.getR73_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR73_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row74
					row = sheet.getRow(73);

					// row74
					// Column C
					cell2 = row.createCell(2);
					if (record.getR74_ISSUER() != null) {
						cell2.setCellValue(record.getR74_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row74
					// Column D
					cell3 = row.createCell(3);
					if (record.getR74_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR74_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row74
					// Column E
					cell4 = row.createCell(4);
					if (record.getR74_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR74_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row74
					// Column F
					cell5 = row.createCell(5);
					if (record.getR74_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR74_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row74
					// Column G
					cell6 = row.createCell(6);
					if (record.getR74_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR74_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row74
					// Column H
					cell7 = row.createCell(7);
					if (record.getR74_OTHER() != null) {
						cell7.setCellValue(record.getR74_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row74
					// Column I
					cell8 = row.createCell(8);
					if (record.getR74_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR74_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row74
					// Column J
					cell9 = row.createCell(9);
					if (record.getR74_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR74_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row75
					row = sheet.getRow(74);

					// row75
					// Column C
					cell2 = row.createCell(2);
					if (record.getR75_ISSUER() != null) {
						cell2.setCellValue(record.getR75_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row75
					// Column D
					cell3 = row.createCell(3);
					if (record.getR75_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR75_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row75
					// Column E
					cell4 = row.createCell(4);
					if (record.getR75_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR75_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row75
					// Column F
					cell5 = row.createCell(5);
					if (record.getR75_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR75_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row75
					// Column G
					cell6 = row.createCell(6);
					if (record.getR75_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR75_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row75
					// Column H
					cell7 = row.createCell(7);
					if (record.getR75_OTHER() != null) {
						cell7.setCellValue(record.getR75_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row75
					// Column I
					cell8 = row.createCell(8);
					if (record.getR75_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR75_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row75
					// Column J
					cell9 = row.createCell(9);
					if (record.getR75_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR75_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row76
					row = sheet.getRow(75);

					// row76
					// Column C
					cell2 = row.createCell(2);
					if (record.getR76_ISSUER() != null) {
						cell2.setCellValue(record.getR76_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row76
					// Column D
					cell3 = row.createCell(3);
					if (record.getR76_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR76_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row76
					// Column E
					cell4 = row.createCell(4);
					if (record.getR76_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR76_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row76
					// Column F
					cell5 = row.createCell(5);
					if (record.getR76_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR76_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row76
					// Column G
					cell6 = row.createCell(6);
					if (record.getR76_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR76_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row76
					// Column H
					cell7 = row.createCell(7);
					if (record.getR76_OTHER() != null) {
						cell7.setCellValue(record.getR76_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row76
					// Column I
					cell8 = row.createCell(8);
					if (record.getR76_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR76_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row76
					// Column J
					cell9 = row.createCell(9);
					if (record.getR76_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR76_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row77
					row = sheet.getRow(76);

					// row77
					// Column C
					cell2 = row.createCell(2);
					if (record.getR77_ISSUER() != null) {
						cell2.setCellValue(record.getR77_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row77
					// Column D
					cell3 = row.createCell(3);
					if (record.getR77_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR77_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row77
					// Column E
					cell4 = row.createCell(4);
					if (record.getR77_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR77_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row77
					// Column F
					cell5 = row.createCell(5);
					if (record.getR77_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR77_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row77
					// Column G
					cell6 = row.createCell(6);
					if (record.getR77_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR77_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row77
					// Column H
					cell7 = row.createCell(7);
					if (record.getR77_OTHER() != null) {
						cell7.setCellValue(record.getR77_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row77
					// Column I
					cell8 = row.createCell(8);
					if (record.getR77_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR77_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row77
					// Column J
					cell9 = row.createCell(9);
					if (record.getR77_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR77_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row78
					row = sheet.getRow(77);

					// row78
					// Column C
					cell2 = row.createCell(2);
					if (record.getR78_ISSUER() != null) {
						cell2.setCellValue(record.getR78_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row78
					// Column D
					cell3 = row.createCell(3);
					if (record.getR78_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR78_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row78
					// Column E
					cell4 = row.createCell(4);
					if (record.getR78_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR78_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row78
					// Column F
					cell5 = row.createCell(5);
					if (record.getR78_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR78_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row78
					// Column G
					cell6 = row.createCell(6);
					if (record.getR78_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR78_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row78
					// Column H
					cell7 = row.createCell(7);
					if (record.getR78_OTHER() != null) {
						cell7.setCellValue(record.getR78_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row78
					// Column I
					cell8 = row.createCell(8);
					if (record.getR78_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR78_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row78
					// Column J
					cell9 = row.createCell(9);
					if (record.getR78_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR78_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row79
					row = sheet.getRow(78);

					// row79
					// Column C
					cell2 = row.createCell(2);
					if (record.getR79_ISSUER() != null) {
						cell2.setCellValue(record.getR79_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row79
					// Column D
					cell3 = row.createCell(3);
					if (record.getR79_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR79_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row79
					// Column E
					cell4 = row.createCell(4);
					if (record.getR79_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR79_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row79
					// Column F
					cell5 = row.createCell(5);
					if (record.getR79_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR79_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row79
					// Column G
					cell6 = row.createCell(6);
					if (record.getR79_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR79_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row79
					// Column H
					cell7 = row.createCell(7);
					if (record.getR79_OTHER() != null) {
						cell7.setCellValue(record.getR79_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row79
					// Column I
					cell8 = row.createCell(8);
					if (record.getR79_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR79_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row79
					// Column J
					cell9 = row.createCell(9);
					if (record.getR79_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR79_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row80
					row = sheet.getRow(79);

					// row80
					// Column C
					cell2 = row.createCell(2);
					if (record.getR80_ISSUER() != null) {
						cell2.setCellValue(record.getR80_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row80
					// Column D
					cell3 = row.createCell(3);
					if (record.getR80_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR80_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row80
					// Column E
					cell4 = row.createCell(4);
					if (record.getR80_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR80_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row80
					// Column F
					cell5 = row.createCell(5);
					if (record.getR80_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR80_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row80
					// Column G
					cell6 = row.createCell(6);
					if (record.getR80_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR80_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row80
					// Column H
					cell7 = row.createCell(7);
					if (record.getR80_OTHER() != null) {
						cell7.setCellValue(record.getR80_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row80
					// Column I
					cell8 = row.createCell(8);
					if (record.getR80_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR80_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row80
					// Column J
					cell9 = row.createCell(9);
					if (record.getR80_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR80_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					row = sheet.getRow(80);
					Cell cell1 = row.createCell(1);
					if (record.getR81_PRODUCT() != null) {
						cell1.setCellValue(record.getR81_PRODUCT().doubleValue());
						cell1.setCellStyle(numberStyle);

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(numberStyle);
					}

					// Column C
					cell2 = row.createCell(2);
					if (record.getR81_ISSUER() != null) {
						cell2.setCellValue(record.getR81_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row80
					// Column D
					cell3 = row.createCell(3);
					if (record.getR81_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR81_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					row = sheet.getRow(80);

					cell2 = row.getCell(1);
					if (cell2 == null)
						cell2 = row.createCell(1);

					if (record.getR81_PRODUCT() != null) {
						cell2.setCellValue(record.getR81_PRODUCT().doubleValue());
					} else {
						cell2.setCellValue(0); // or leave previous value
					}

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);

					if (record.getR81_ISSUER() != null) {
						cell2.setCellValue(record.getR81_ISSUER().doubleValue());
					} else {
						cell2.setCellValue(0); // or leave previous value
					}

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);

					if (record.getR81_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR81_ISSUES_RATING().doubleValue());
					} else {
						cell3.setCellValue(0); // or leave previous value
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
//////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////	
/// Report Date | Report Version | Domain
/// RESUB VIEW
public List<Object[]> getM_SRWA_12HResub() {
    List<Object[]> resubList = new ArrayList<>();
    try {
        List<M_SRWA_12H_Archival_Summary_Entity> latestArchivalList =
                M_SRWA_12H_Archival_Summary_Repo.getdatabydateListWithVersion();

        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
            for (M_SRWA_12H_Archival_Summary_Entity entity : latestArchivalList) {
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



	//Archival View
	public List<Object[]> getM_SRWA_12HArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_SRWA_12H_Archival_Summary_Entity> repoData = M_SRWA_12H_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SRWA_12H_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReportDate(), 
							entity.getReportVersion() 
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SRWA_12H_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12H Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Resubmit the values , latest version and Resub Date
	public void updateReportReSub(M_SRWA_12H_Summary_Entity updatedEntity) {
		System.out.println("Came to Resub Service");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		Date reportDate = updatedEntity.getReportDate();
		int newVersion = 1;

		try {
			// Fetch the latest archival version for this report date
			Optional<M_SRWA_12H_Archival_Summary_Entity> latestArchivalOpt = M_SRWA_12H_Archival_Summary_Repo
					.getLatestArchivalVersionByDate(reportDate);

			// Determine next version number
			if (latestArchivalOpt.isPresent()) {
				M_SRWA_12H_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
				try {
					newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1;
				} catch (NumberFormatException e) {
					System.err.println("Invalid version format. Defaulting to version 1");
					newVersion = 1;
				}
			} else {
				System.out.println("No previous archival found for date: " + reportDate);
			}

			// Prevent duplicate version number
			boolean exists = M_SRWA_12H_Archival_Summary_Repo
					.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
					.isPresent();

			if (exists) {
				throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
			}

			// Copy summary entity to archival entity
			M_SRWA_12H_Archival_Summary_Entity archivalEntity = new M_SRWA_12H_Archival_Summary_Entity();
			org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

			archivalEntity.setReportDate(reportDate);
			archivalEntity.setReportVersion(String.valueOf(newVersion));
			archivalEntity.setReportResubDate(new Date());

			System.out.println("Saving new archival version: " + newVersion);

			// Save new version to repository
			M_SRWA_12H_Archival_Summary_Repo.save(archivalEntity);

			System.out.println(" Saved archival version successfully: " + newVersion);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while creating archival resubmission record", e);
		}
	}

	/// Downloaded for Archival & Resub
	public byte[] BRRS_M_SRWA_12HResubExcel(String filename, String reportId, String fromdate,
        String todate, String currency, String dtltype,
        String type, String version) throws Exception {

    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

    if (type.equals("RESUB") & version != null) {
       
    }

    List<M_SRWA_12H_Archival_Summary_Entity> dataList =
        M_SRWA_12H_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

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
			// --- End of Style Definitions ---

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_SRWA_12H_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR12_ISSUER() != null) {
						cell2.setCellValue(record.getR12_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR12_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR12_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row12
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR12_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR12_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row12
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR12_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR12_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row12
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR12_OTHER() != null) {
						cell7.setCellValue(record.getR12_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row12
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR12_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR12_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row12
					// Column J
					Cell cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR13_ISSUER() != null) {
						cell2.setCellValue(record.getR13_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR13_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR13_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR13_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR13_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR13_OTHER() != null) {
						cell7.setCellValue(record.getR13_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row13
					// Column I
					cell8 = row.createCell(8);
					if (record.getR13_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR13_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row13
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR14_ISSUER() != null) {
						cell2.setCellValue(record.getR14_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR14_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR14_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR14_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR14_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row14
					// Column H
					cell7 = row.createCell(7);
					if (record.getR14_OTHER() != null) {
						cell7.setCellValue(record.getR14_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row14
					// Column I
					cell8 = row.createCell(8);
					if (record.getR14_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR14_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row14
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR15_ISSUER() != null) {
						cell2.setCellValue(record.getR15_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR15_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row15
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR15_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR15_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row15
					// Column G
					cell6 = row.createCell(6);
					if (record.getR15_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR15_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row15
					// Column H
					cell7 = row.createCell(7);
					if (record.getR15_OTHER() != null) {
						cell7.setCellValue(record.getR15_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row15
					// Column I
					cell8 = row.createCell(8);
					if (record.getR15_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR15_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row15
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR16_ISSUER() != null) {
						cell2.setCellValue(record.getR16_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR16_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row16
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR16_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR16_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row16
					// Column G
					cell6 = row.createCell(6);
					if (record.getR16_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR16_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row16
					// Column H
					cell7 = row.createCell(7);
					if (record.getR16_OTHER() != null) {
						cell7.setCellValue(record.getR16_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row16
					// Column I
					cell8 = row.createCell(8);
					if (record.getR16_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR16_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row16
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR17_ISSUER() != null) {
						cell2.setCellValue(record.getR17_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row17
					// Column D
					cell3 = row.createCell(3);
					if (record.getR17_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR17_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row17
					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR17_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR17_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row17
					// Column G
					cell6 = row.createCell(6);
					if (record.getR17_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR17_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row17
					// Column H
					cell7 = row.createCell(7);
					if (record.getR17_OTHER() != null) {
						cell7.setCellValue(record.getR17_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row17
					// Column I
					cell8 = row.createCell(8);
					if (record.getR17_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR17_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row17
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR18_ISSUER() != null) {
						cell2.setCellValue(record.getR18_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row18
					// Column D
					cell3 = row.createCell(3);
					if (record.getR18_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR18_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row18
					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR18_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR18_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row18
					// Column G
					cell6 = row.createCell(6);
					if (record.getR18_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR18_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row18
					// Column H
					cell7 = row.createCell(7);
					if (record.getR18_OTHER() != null) {
						cell7.setCellValue(record.getR18_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row18
					// Column I
					cell8 = row.createCell(8);
					if (record.getR18_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR18_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row18
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR19_ISSUER() != null) {
						cell2.setCellValue(record.getR19_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row19
					// Column D
					cell3 = row.createCell(3);
					if (record.getR19_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR19_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row19
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR19_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR19_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row19
					// Column G
					cell6 = row.createCell(6);
					if (record.getR19_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR19_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row19
					// Column H
					cell7 = row.createCell(7);
					if (record.getR19_OTHER() != null) {
						cell7.setCellValue(record.getR19_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row19
					// Column I
					cell8 = row.createCell(8);
					if (record.getR19_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR19_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row19
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR20_ISSUER() != null) {
						cell2.setCellValue(record.getR20_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR20_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row20
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR20_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR20_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row20
					// Column G
					cell6 = row.createCell(6);
					if (record.getR20_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR20_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row20
					// Column H
					cell7 = row.createCell(7);
					if (record.getR20_OTHER() != null) {
						cell7.setCellValue(record.getR20_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row20
					// Column I
					cell8 = row.createCell(8);
					if (record.getR20_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR20_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row20
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR21_ISSUER() != null) {
						cell2.setCellValue(record.getR21_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR21_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row21
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR21_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR21_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row21
					// Column G
					cell6 = row.createCell(6);
					if (record.getR21_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR21_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row21
					// Column H
					cell7 = row.createCell(7);
					if (record.getR21_OTHER() != null) {
						cell7.setCellValue(record.getR21_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row21
					// Column I
					cell8 = row.createCell(8);
					if (record.getR21_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR21_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row21
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR22_ISSUER() != null) {
						cell2.setCellValue(record.getR22_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR22_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR22_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR22_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR22_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row22
					// Column H
					cell7 = row.createCell(7);
					if (record.getR22_OTHER() != null) {
						cell7.setCellValue(record.getR22_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row22
					// Column I
					cell8 = row.createCell(8);
					if (record.getR22_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR22_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row22
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR23_ISSUER() != null) {
						cell2.setCellValue(record.getR23_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR23_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR23_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR23_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR23_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row23
					// Column H
					cell7 = row.createCell(7);
					if (record.getR23_OTHER() != null) {
						cell7.setCellValue(record.getR23_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row23
					// Column I
					cell8 = row.createCell(8);
					if (record.getR23_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR23_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row23
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR24_ISSUER() != null) {
						cell2.setCellValue(record.getR24_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR24_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR24_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR24_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR24_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row24
					// Column H
					cell7 = row.createCell(7);
					if (record.getR24_OTHER() != null) {
						cell7.setCellValue(record.getR24_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row24
					// Column I
					cell8 = row.createCell(8);
					if (record.getR24_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR24_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row24
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR25_ISSUER() != null) {
						cell2.setCellValue(record.getR25_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row25
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR25_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row25
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR25_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR25_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row25
					// Column G
					cell6 = row.createCell(6);
					if (record.getR25_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR25_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row25
					// Column H
					cell7 = row.createCell(7);
					if (record.getR25_OTHER() != null) {
						cell7.setCellValue(record.getR25_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row25
					// Column I
					cell8 = row.createCell(8);
					if (record.getR25_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR25_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row25
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR26_ISSUER() != null) {
						cell2.setCellValue(record.getR26_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row26
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR26_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row26
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR26_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR26_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row26
					// Column G
					cell6 = row.createCell(6);
					if (record.getR26_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR26_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row26
					// Column H
					cell7 = row.createCell(7);
					if (record.getR26_OTHER() != null) {
						cell7.setCellValue(record.getR26_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row26
					// Column I
					cell8 = row.createCell(8);
					if (record.getR26_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR26_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row26
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR27_ISSUER() != null) {
						cell2.setCellValue(record.getR27_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR27_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row27
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR27_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR27_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row27
					// Column G
					cell6 = row.createCell(6);
					if (record.getR27_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR27_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row27
					// Column H
					cell7 = row.createCell(7);
					if (record.getR27_OTHER() != null) {
						cell7.setCellValue(record.getR27_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row27
					// Column I
					cell8 = row.createCell(8);
					if (record.getR27_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR27_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row27
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR28_ISSUER() != null) {
						cell2.setCellValue(record.getR28_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR28_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row28
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR28_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR28_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row28
					// Column G
					cell6 = row.createCell(6);
					if (record.getR28_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR28_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row28
					// Column H
					cell7 = row.createCell(7);
					if (record.getR28_OTHER() != null) {
						cell7.setCellValue(record.getR28_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row28
					// Column I
					cell8 = row.createCell(8);
					if (record.getR28_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR28_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row28
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR29_ISSUER() != null) {
						cell2.setCellValue(record.getR29_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row29
					// Column D
					cell3 = row.createCell(3);
					if (record.getR29_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR29_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row29
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR29_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR29_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row29
					// Column G
					cell6 = row.createCell(6);
					if (record.getR29_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR29_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row29
					// Column H
					cell7 = row.createCell(7);
					if (record.getR29_OTHER() != null) {
						cell7.setCellValue(record.getR29_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row29
					// Column I
					cell8 = row.createCell(8);
					if (record.getR29_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR29_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row29
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR30_ISSUER() != null) {
						cell2.setCellValue(record.getR30_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					// Column D
					cell3 = row.createCell(3);
					if (record.getR30_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR30_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row30
					// Column E
					cell4 = row.createCell(4);
					if (record.getR30_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR30_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record.getR30_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR30_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row30
					// Column G
					cell6 = row.createCell(6);
					if (record.getR30_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR30_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row30
					// Column H
					cell7 = row.createCell(7);
					if (record.getR30_OTHER() != null) {
						cell7.setCellValue(record.getR30_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row30
					// Column I
					cell8 = row.createCell(8);
					if (record.getR30_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR30_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row30
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR31_ISSUER() != null) {
						cell2.setCellValue(record.getR31_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR31_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row31
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR31_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR31_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row31
					// Column G
					cell6 = row.createCell(6);
					if (record.getR31_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR31_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row31
					// Column H
					cell7 = row.createCell(7);
					if (record.getR31_OTHER() != null) {
						cell7.setCellValue(record.getR31_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row31
					// Column I
					cell8 = row.createCell(8);
					if (record.getR31_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR31_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row31
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR32_ISSUER() != null) {
						cell2.setCellValue(record.getR32_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row32
					// Column D
					cell3 = row.createCell(3);
					if (record.getR32_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR32_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row32
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR32_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR32_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row32
					// Column G
					cell6 = row.createCell(6);
					if (record.getR32_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR32_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row32
					// Column H
					cell7 = row.createCell(7);
					if (record.getR32_OTHER() != null) {
						cell7.setCellValue(record.getR32_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row32
					// Column I
					cell8 = row.createCell(8);
					if (record.getR32_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR32_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row32
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR33_ISSUER() != null) {
						cell2.setCellValue(record.getR33_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR33_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR33_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR33_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR33_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR33_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR33_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR33_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR33_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR33_OTHER() != null) {
						cell7.setCellValue(record.getR33_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR33_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR33_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR34_ISSUER() != null) {
						cell2.setCellValue(record.getR34_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR34_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR34_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR34_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR34_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR34_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR34_OTHER() != null) {
						cell7.setCellValue(record.getR34_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR34_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR34_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR35_ISSUER() != null) {
						cell2.setCellValue(record.getR35_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR35_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR35_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR35_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR35_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR35_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR35_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR35_OTHER() != null) {
						cell7.setCellValue(record.getR35_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR35_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR35_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR36_ISSUER() != null) {
						cell2.setCellValue(record.getR36_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR36_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR36_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR36_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR36_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR36_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR36_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR36_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR36_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR36_OTHER() != null) {
						cell7.setCellValue(record.getR36_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR36_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR36_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
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
					cell2 = row.createCell(2);
					if (record.getR37_ISSUER() != null) {
						cell2.setCellValue(record.getR37_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR37_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR37_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR37_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR37_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR37_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR37_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR37_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR37_OTHER() != null) {
						cell7.setCellValue(record.getR37_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR37_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR37_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR37_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR37_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row38
					row = sheet.getRow(37);

					// row38
					// Column C
					cell2 = row.createCell(2);
					if (record.getR38_ISSUER() != null) {
						cell2.setCellValue(record.getR38_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR38_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR38_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR38_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR38_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR38_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR38_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR38_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR38_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR38_OTHER() != null) {
						cell7.setCellValue(record.getR38_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR38_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR38_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR38_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR38_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row39
					row = sheet.getRow(38);

					// row39
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_ISSUER() != null) {
						cell2.setCellValue(record.getR39_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR39_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR39_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR39_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR39_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR39_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR39_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR39_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR39_OTHER() != null) {
						cell7.setCellValue(record.getR39_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR39_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR39_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR39_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR39_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row40
					row = sheet.getRow(39);

					// row40
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_ISSUER() != null) {
						cell2.setCellValue(record.getR40_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR40_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR40_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR40_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR40_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR40_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR40_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR40_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR40_OTHER() != null) {
						cell7.setCellValue(record.getR40_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR40_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR40_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR40_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR40_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row41
					row = sheet.getRow(40);

					// row41
					// Column C
					cell2 = row.createCell(2);
					if (record.getR41_ISSUER() != null) {
						cell2.setCellValue(record.getR41_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR41_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR41_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR41_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR41_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR41_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR41_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR41_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR41_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR41_OTHER() != null) {
						cell7.setCellValue(record.getR41_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR41_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR41_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR41_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR41_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row42
					row = sheet.getRow(41);

					// row42
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_ISSUER() != null) {
						cell2.setCellValue(record.getR42_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR42_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR42_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR42_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR42_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR42_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR42_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR42_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR42_OTHER() != null) {
						cell7.setCellValue(record.getR42_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR42_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR42_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR42_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR42_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row43
					row = sheet.getRow(42);

					// row43
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_ISSUER() != null) {
						cell2.setCellValue(record.getR43_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR43_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR43_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR43_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR43_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR43_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR43_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR43_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR43_OTHER() != null) {
						cell7.setCellValue(record.getR43_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR43_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR43_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR43_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR43_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row44
					row = sheet.getRow(43);

					// row44
					// Column C
					cell2 = row.createCell(2);
					if (record.getR44_ISSUER() != null) {
						cell2.setCellValue(record.getR44_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR44_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR44_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR44_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR44_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR44_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR44_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR44_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR44_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR44_OTHER() != null) {
						cell7.setCellValue(record.getR44_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR44_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR44_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR44_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR44_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row45
					row = sheet.getRow(44);

					// row45
					// Column C
					cell2 = row.createCell(2);
					if (record.getR45_ISSUER() != null) {
						cell2.setCellValue(record.getR45_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR45_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR45_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR45_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR45_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR45_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR45_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR45_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR45_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR45_OTHER() != null) {
						cell7.setCellValue(record.getR45_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR45_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR45_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR45_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR45_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row46
					row = sheet.getRow(45);

					// row46
					// Column C
					cell2 = row.createCell(2);
					if (record.getR46_ISSUER() != null) {
						cell2.setCellValue(record.getR46_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR46_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR46_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR46_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR46_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR46_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR46_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR46_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR46_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR46_OTHER() != null) {
						cell7.setCellValue(record.getR46_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR46_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR46_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR46_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR46_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row47
					row = sheet.getRow(46);

					// row47
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_ISSUER() != null) {
						cell2.setCellValue(record.getR47_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR47_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR47_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR47_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR47_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR47_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR47_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR47_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR47_OTHER() != null) {
						cell7.setCellValue(record.getR47_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR47_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR47_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR47_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR47_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row48
					row = sheet.getRow(47);

					// row48
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_ISSUER() != null) {
						cell2.setCellValue(record.getR48_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR48_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR48_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR48_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR48_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR48_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR48_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR48_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR48_OTHER() != null) {
						cell7.setCellValue(record.getR48_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR48_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR48_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR48_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR48_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row49
					row = sheet.getRow(48);

					// row49
					// Column C
					cell2 = row.createCell(2);
					if (record.getR49_ISSUER() != null) {
						cell2.setCellValue(record.getR49_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR49_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR49_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR49_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR49_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR49_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR49_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR49_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR49_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR49_OTHER() != null) {
						cell7.setCellValue(record.getR49_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR49_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR49_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR49_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR49_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row50
					row = sheet.getRow(49);

					// row50
					// Column C
					cell2 = row.createCell(2);
					if (record.getR50_ISSUER() != null) {
						cell2.setCellValue(record.getR50_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row33
					// Column D
					cell3 = row.createCell(3);
					if (record.getR50_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR50_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record.getR50_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR50_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR50_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR50_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record.getR50_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR50_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record.getR50_OTHER() != null) {
						cell7.setCellValue(record.getR50_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row33
					// Column I
					cell8 = row.createCell(8);
					if (record.getR50_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR50_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row33
					// Column J
					cell9 = row.createCell(9);
					if (record.getR50_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR50_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row51
					row = sheet.getRow(50);

					// row51
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_ISSUER() != null) {
						cell2.setCellValue(record.getR51_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR51_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row51
					// Column E
					cell4 = row.createCell(4);
					if (record.getR51_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR51_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row51
					// Column F
					cell5 = row.createCell(5);
					if (record.getR51_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR51_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row51
					// Column G
					cell6 = row.createCell(6);
					if (record.getR51_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR51_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row51
					// Column H
					cell7 = row.createCell(7);
					if (record.getR51_OTHER() != null) {
						cell7.setCellValue(record.getR51_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row51
					// Column I
					cell8 = row.createCell(8);
					if (record.getR51_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR51_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row51
					// Column J
					cell9 = row.createCell(9);
					if (record.getR51_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR51_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row52
					row = sheet.getRow(51);

					// row52
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_ISSUER() != null) {
						cell2.setCellValue(record.getR52_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR52_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row52
					// Column E
					cell4 = row.createCell(4);
					if (record.getR52_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR52_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row52
					// Column F
					cell5 = row.createCell(5);
					if (record.getR52_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR52_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row52
					// Column G
					cell6 = row.createCell(6);
					if (record.getR52_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR52_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row52
					// Column H
					cell7 = row.createCell(7);
					if (record.getR52_OTHER() != null) {
						cell7.setCellValue(record.getR52_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row52
					// Column I
					cell8 = row.createCell(8);
					if (record.getR52_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR52_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row52
					// Column J
					cell9 = row.createCell(9);
					if (record.getR52_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR52_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row53
					row = sheet.getRow(52);

					// row53
					// Column C
					cell2 = row.createCell(2);
					if (record.getR53_ISSUER() != null) {
						cell2.setCellValue(record.getR53_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					// Column D
					cell3 = row.createCell(3);
					if (record.getR53_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR53_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row53
					// Column E
					cell4 = row.createCell(4);
					if (record.getR53_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR53_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row53
					// Column F
					cell5 = row.createCell(5);
					if (record.getR53_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR53_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row53
					// Column G
					cell6 = row.createCell(6);
					if (record.getR53_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR53_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row53
					// Column H
					cell7 = row.createCell(7);
					if (record.getR53_OTHER() != null) {
						cell7.setCellValue(record.getR53_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row53
					// Column I
					cell8 = row.createCell(8);
					if (record.getR53_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR53_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row53
					// Column J
					cell9 = row.createCell(9);
					if (record.getR53_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR53_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row54
					row = sheet.getRow(53);

					// row54
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_ISSUER() != null) {
						cell2.setCellValue(record.getR54_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR54_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row54
					// Column E
					cell4 = row.createCell(4);
					if (record.getR54_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR54_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row54
					// Column F
					cell5 = row.createCell(5);
					if (record.getR54_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR54_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row54
					// Column G
					cell6 = row.createCell(6);
					if (record.getR54_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR54_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row54
					// Column H
					cell7 = row.createCell(7);
					if (record.getR54_OTHER() != null) {
						cell7.setCellValue(record.getR54_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row54
					// Column I
					cell8 = row.createCell(8);
					if (record.getR54_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR54_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row54
					// Column J
					cell9 = row.createCell(9);
					if (record.getR54_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR54_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row55
					row = sheet.getRow(54);

					// row55
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_ISSUER() != null) {
						cell2.setCellValue(record.getR55_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR55_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row55
					// Column E
					cell4 = row.createCell(4);
					if (record.getR55_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR55_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row55
					// Column F
					cell5 = row.createCell(5);
					if (record.getR55_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR55_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row55
					// Column G
					cell6 = row.createCell(6);
					if (record.getR55_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR55_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row55
					// Column H
					cell7 = row.createCell(7);
					if (record.getR55_OTHER() != null) {
						cell7.setCellValue(record.getR55_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row55
					// Column I
					cell8 = row.createCell(8);
					if (record.getR55_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR55_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row55
					// Column J
					cell9 = row.createCell(9);
					if (record.getR55_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR55_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row56
					row = sheet.getRow(55);

					// row56
					// Column C
					cell2 = row.createCell(2);
					if (record.getR56_ISSUER() != null) {
						cell2.setCellValue(record.getR56_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row56
					// Column D
					cell3 = row.createCell(3);
					if (record.getR56_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR56_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row56
					// Column E
					cell4 = row.createCell(4);
					if (record.getR56_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR56_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row56
					// Column F
					cell5 = row.createCell(5);
					if (record.getR56_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR56_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row56
					// Column G
					cell6 = row.createCell(6);
					if (record.getR56_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR56_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row56
					// Column H
					cell7 = row.createCell(7);
					if (record.getR56_OTHER() != null) {
						cell7.setCellValue(record.getR56_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row56
					// Column I
					cell8 = row.createCell(8);
					if (record.getR56_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR56_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row56
					// Column J
					cell9 = row.createCell(9);
					if (record.getR56_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR56_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row57
					row = sheet.getRow(56);

					// row57
					// Column C
					cell2 = row.createCell(2);
					if (record.getR57_ISSUER() != null) {
						cell2.setCellValue(record.getR57_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row57
					// Column D
					cell3 = row.createCell(3);
					if (record.getR57_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR57_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row57
					// Column E
					cell4 = row.createCell(4);
					if (record.getR57_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR57_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row57
					// Column F
					cell5 = row.createCell(5);
					if (record.getR57_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR57_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row57
					// Column G
					cell6 = row.createCell(6);
					if (record.getR57_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR57_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row57
					// Column H
					cell7 = row.createCell(7);
					if (record.getR57_OTHER() != null) {
						cell7.setCellValue(record.getR57_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row57
					// Column I
					cell8 = row.createCell(8);
					if (record.getR57_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR57_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row57
					// Column J
					cell9 = row.createCell(9);
					if (record.getR57_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR57_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row58
					row = sheet.getRow(57);

					// row58
					// Column C
					cell2 = row.createCell(2);
					if (record.getR58_ISSUER() != null) {
						cell2.setCellValue(record.getR58_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row58
					// Column D
					cell3 = row.createCell(3);
					if (record.getR58_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR58_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row58
					// Column E
					cell4 = row.createCell(4);
					if (record.getR58_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR58_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row58
					// Column F
					cell5 = row.createCell(5);
					if (record.getR58_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR58_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row58
					// Column G
					cell6 = row.createCell(6);
					if (record.getR58_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR58_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row58
					// Column H
					cell7 = row.createCell(7);
					if (record.getR58_OTHER() != null) {
						cell7.setCellValue(record.getR58_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row58
					// Column I
					cell8 = row.createCell(8);
					if (record.getR58_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR58_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row58
					// Column J
					cell9 = row.createCell(9);
					if (record.getR58_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR58_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row59
					row = sheet.getRow(58);

					// row59
					// Column C
					cell2 = row.createCell(2);
					if (record.getR59_ISSUER() != null) {
						cell2.setCellValue(record.getR59_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row59
					// Column D
					cell3 = row.createCell(3);
					if (record.getR59_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR59_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row59
					// Column E
					cell4 = row.createCell(4);
					if (record.getR59_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR59_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row59
					// Column F
					cell5 = row.createCell(5);
					if (record.getR59_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR59_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row59
					// Column G
					cell6 = row.createCell(6);
					if (record.getR59_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR59_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row59
					// Column H
					cell7 = row.createCell(7);
					if (record.getR59_OTHER() != null) {
						cell7.setCellValue(record.getR59_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row59
					// Column I
					cell8 = row.createCell(8);
					if (record.getR59_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR59_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row59
					// Column J
					cell9 = row.createCell(9);
					if (record.getR59_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR59_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row60
					row = sheet.getRow(59);

					// row60
					// Column C
					cell2 = row.createCell(2);
					if (record.getR60_ISSUER() != null) {
						cell2.setCellValue(record.getR60_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row60
					// Column D
					cell3 = row.createCell(3);
					if (record.getR60_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR60_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row60
					// Column E
					cell4 = row.createCell(4);
					if (record.getR60_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR60_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row60
					// Column F
					cell5 = row.createCell(5);
					if (record.getR60_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR60_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row60
					// Column G
					cell6 = row.createCell(6);
					if (record.getR60_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR60_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row60
					// Column H
					cell7 = row.createCell(7);
					if (record.getR60_OTHER() != null) {
						cell7.setCellValue(record.getR60_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row60
					// Column I
					cell8 = row.createCell(8);
					if (record.getR60_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR60_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row60
					// Column J
					cell9 = row.createCell(9);
					if (record.getR60_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR60_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row61
					row = sheet.getRow(60);

					// row61
					// Column C
					cell2 = row.createCell(2);
					if (record.getR61_ISSUER() != null) {
						cell2.setCellValue(record.getR61_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row61
					// Column D
					cell3 = row.createCell(3);
					if (record.getR61_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR61_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row61
					// Column E
					cell4 = row.createCell(4);
					if (record.getR61_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR61_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row61
					// Column F
					cell5 = row.createCell(5);
					if (record.getR61_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR61_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row61
					// Column G
					cell6 = row.createCell(6);
					if (record.getR61_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR61_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row61
					// Column H
					cell7 = row.createCell(7);
					if (record.getR61_OTHER() != null) {
						cell7.setCellValue(record.getR61_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row61
					// Column I
					cell8 = row.createCell(8);
					if (record.getR61_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR61_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row61
					// Column J
					cell9 = row.createCell(9);
					if (record.getR61_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR61_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row62
					row = sheet.getRow(61);

					// row62
					// Column C
					cell2 = row.createCell(2);
					if (record.getR62_ISSUER() != null) {
						cell2.setCellValue(record.getR62_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row62
					// Column D
					cell3 = row.createCell(3);
					if (record.getR62_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR62_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row62
					// Column E
					cell4 = row.createCell(4);
					if (record.getR62_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR62_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row62
					// Column F
					cell5 = row.createCell(5);
					if (record.getR62_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR62_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row62
					// Column G
					cell6 = row.createCell(6);
					if (record.getR62_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR62_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row62
					// Column H
					cell7 = row.createCell(7);
					if (record.getR62_OTHER() != null) {
						cell7.setCellValue(record.getR62_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row62
					// Column I
					cell8 = row.createCell(8);
					if (record.getR62_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR62_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row62
					// Column J
					cell9 = row.createCell(9);
					if (record.getR62_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR62_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row63
					row = sheet.getRow(62);

					// row63
					// Column C
					cell2 = row.createCell(2);
					if (record.getR63_ISSUER() != null) {
						cell2.setCellValue(record.getR63_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row63
					// Column D
					cell3 = row.createCell(3);
					if (record.getR63_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR63_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row63
					// Column E
					cell4 = row.createCell(4);
					if (record.getR63_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR63_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row63
					// Column F
					cell5 = row.createCell(5);
					if (record.getR63_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR63_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row63
					// Column G
					cell6 = row.createCell(6);
					if (record.getR63_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR63_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row63
					// Column H
					cell7 = row.createCell(7);
					if (record.getR63_OTHER() != null) {
						cell7.setCellValue(record.getR63_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row63
					// Column I
					cell8 = row.createCell(8);
					if (record.getR63_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR63_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row63
					// Column J
					cell9 = row.createCell(9);
					if (record.getR63_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR63_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row64
					row = sheet.getRow(63);

					// row64
					// Column C
					cell2 = row.createCell(2);
					if (record.getR64_ISSUER() != null) {
						cell2.setCellValue(record.getR64_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row64
					// Column D
					cell3 = row.createCell(3);
					if (record.getR64_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR64_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row64
					// Column E
					cell4 = row.createCell(4);
					if (record.getR64_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR64_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row64
					// Column F
					cell5 = row.createCell(5);
					if (record.getR64_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR64_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row64
					// Column G
					cell6 = row.createCell(6);
					if (record.getR64_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR64_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row64
					// Column H
					cell7 = row.createCell(7);
					if (record.getR64_OTHER() != null) {
						cell7.setCellValue(record.getR64_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row64
					// Column I
					cell8 = row.createCell(8);
					if (record.getR64_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR64_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row64
					// Column J
					cell9 = row.createCell(9);
					if (record.getR64_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR64_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row65
					row = sheet.getRow(64);

					// row65
					// Column C
					cell2 = row.createCell(2);
					if (record.getR65_ISSUER() != null) {
						cell2.setCellValue(record.getR65_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row65
					// Column D
					cell3 = row.createCell(3);
					if (record.getR65_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR65_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row65
					// Column E
					cell4 = row.createCell(4);
					if (record.getR65_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR65_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row65
					// Column F
					cell5 = row.createCell(5);
					if (record.getR65_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR65_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row65
					// Column G
					cell6 = row.createCell(6);
					if (record.getR65_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR65_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row65
					// Column H
					cell7 = row.createCell(7);
					if (record.getR65_OTHER() != null) {
						cell7.setCellValue(record.getR65_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row65
					// Column I
					cell8 = row.createCell(8);
					if (record.getR65_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR65_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row65
					// Column J
					cell9 = row.createCell(9);
					if (record.getR65_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR65_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row66
					row = sheet.getRow(65);

					// row66
					// Column C
					cell2 = row.createCell(2);
					if (record.getR66_ISSUER() != null) {
						cell2.setCellValue(record.getR66_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row66
					// Column D
					cell3 = row.createCell(3);
					if (record.getR66_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR66_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row66
					// Column E
					cell4 = row.createCell(4);
					if (record.getR66_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR66_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row66
					// Column F
					cell5 = row.createCell(5);
					if (record.getR66_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR66_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row66
					// Column G
					cell6 = row.createCell(6);
					if (record.getR66_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR66_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row66
					// Column H
					cell7 = row.createCell(7);
					if (record.getR66_OTHER() != null) {
						cell7.setCellValue(record.getR66_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row66
					// Column I
					cell8 = row.createCell(8);
					if (record.getR66_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR66_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row66
					// Column J
					cell9 = row.createCell(9);
					if (record.getR66_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR66_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row67
					row = sheet.getRow(66);

					// row67
					// Column C
					cell2 = row.createCell(2);
					if (record.getR67_ISSUER() != null) {
						cell2.setCellValue(record.getR67_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row67
					// Column D
					cell3 = row.createCell(3);
					if (record.getR67_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR67_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row67
					// Column E
					cell4 = row.createCell(4);
					if (record.getR67_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR67_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row67
					// Column F
					cell5 = row.createCell(5);
					if (record.getR67_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR67_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row67
					// Column G
					cell6 = row.createCell(6);
					if (record.getR67_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR67_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row67
					// Column H
					cell7 = row.createCell(7);
					if (record.getR67_OTHER() != null) {
						cell7.setCellValue(record.getR67_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row67
					// Column I
					cell8 = row.createCell(8);
					if (record.getR67_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR67_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row67
					// Column J
					cell9 = row.createCell(9);
					if (record.getR67_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR67_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row68
					row = sheet.getRow(67);

					// row68
					// Column C
					cell2 = row.createCell(2);
					if (record.getR68_ISSUER() != null) {
						cell2.setCellValue(record.getR68_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row68
					// Column D
					cell3 = row.createCell(3);
					if (record.getR68_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR68_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row68
					// Column E
					cell4 = row.createCell(4);
					if (record.getR68_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR68_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row68
					// Column F
					cell5 = row.createCell(5);
					if (record.getR68_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR68_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row68
					// Column G
					cell6 = row.createCell(6);
					if (record.getR68_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR68_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row68
					// Column H
					cell7 = row.createCell(7);
					if (record.getR68_OTHER() != null) {
						cell7.setCellValue(record.getR68_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row68
					// Column I
					cell8 = row.createCell(8);
					if (record.getR68_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR68_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row68
					// Column J
					cell9 = row.createCell(9);
					if (record.getR68_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR68_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row69
					row = sheet.getRow(68);

					// row69
					// Column C
					cell2 = row.createCell(2);
					if (record.getR69_ISSUER() != null) {
						cell2.setCellValue(record.getR69_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row69
					// Column D
					cell3 = row.createCell(3);
					if (record.getR69_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR69_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row69
					// Column E
					cell4 = row.createCell(4);
					if (record.getR69_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR69_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row69
					// Column F
					cell5 = row.createCell(5);
					if (record.getR69_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR69_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row69
					// Column G
					cell6 = row.createCell(6);
					if (record.getR69_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR69_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row69
					// Column H
					cell7 = row.createCell(7);
					if (record.getR69_OTHER() != null) {
						cell7.setCellValue(record.getR69_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row69
					// Column I
					cell8 = row.createCell(8);
					if (record.getR69_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR69_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row69
					// Column J
					cell9 = row.createCell(9);
					if (record.getR69_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR69_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row70
					row = sheet.getRow(69);

					// row70
					// Column C
					cell2 = row.createCell(2);
					if (record.getR70_ISSUER() != null) {
						cell2.setCellValue(record.getR70_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row70
					// Column D
					cell3 = row.createCell(3);
					if (record.getR70_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR70_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row70
					// Column E
					cell4 = row.createCell(4);
					if (record.getR70_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR70_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row70
					// Column F
					cell5 = row.createCell(5);
					if (record.getR70_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR70_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row70
					// Column G
					cell6 = row.createCell(6);
					if (record.getR70_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR70_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row70
					// Column H
					cell7 = row.createCell(7);
					if (record.getR70_OTHER() != null) {
						cell7.setCellValue(record.getR70_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row70
					// Column I
					cell8 = row.createCell(8);
					if (record.getR70_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR70_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row70
					// Column J
					cell9 = row.createCell(9);
					if (record.getR70_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR70_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row71
					row = sheet.getRow(70);

					// row71
					// Column C
					cell2 = row.createCell(2);
					if (record.getR71_ISSUER() != null) {
						cell2.setCellValue(record.getR71_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row71
					// Column D
					cell3 = row.createCell(3);
					if (record.getR71_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR71_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row71
					// Column E
					cell4 = row.createCell(4);
					if (record.getR71_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR71_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row71
					// Column F
					cell5 = row.createCell(5);
					if (record.getR71_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR71_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row71
					// Column G
					cell6 = row.createCell(6);
					if (record.getR71_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR71_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row71
					// Column H
					cell7 = row.createCell(7);
					if (record.getR71_OTHER() != null) {
						cell7.setCellValue(record.getR71_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row71
					// Column I
					cell8 = row.createCell(8);
					if (record.getR71_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR71_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row71
					// Column J
					cell9 = row.createCell(9);
					if (record.getR71_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR71_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row72
					row = sheet.getRow(71);

					// row72
					// Column C
					cell2 = row.createCell(2);
					if (record.getR72_ISSUER() != null) {
						cell2.setCellValue(record.getR72_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row72
					// Column D
					cell3 = row.createCell(3);
					if (record.getR72_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR72_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row72
					// Column E
					cell4 = row.createCell(4);
					if (record.getR72_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR72_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row72
					// Column F
					cell5 = row.createCell(5);
					if (record.getR72_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR72_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row72
					// Column G
					cell6 = row.createCell(6);
					if (record.getR72_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR72_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row72
					// Column H
					cell7 = row.createCell(7);
					if (record.getR72_OTHER() != null) {
						cell7.setCellValue(record.getR72_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row72
					// Column I
					cell8 = row.createCell(8);
					if (record.getR72_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR72_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row72
					// Column J
					cell9 = row.createCell(9);
					if (record.getR72_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR72_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row73
					row = sheet.getRow(72);

					// row73
					// Column C
					cell2 = row.createCell(2);
					if (record.getR73_ISSUER() != null) {
						cell2.setCellValue(record.getR73_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row73
					// Column D
					cell3 = row.createCell(3);
					if (record.getR73_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR73_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row73
					// Column E
					cell4 = row.createCell(4);
					if (record.getR73_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR73_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row73
					// Column F
					cell5 = row.createCell(5);
					if (record.getR73_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR73_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row73
					// Column G
					cell6 = row.createCell(6);
					if (record.getR73_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR73_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row73
					// Column H
					cell7 = row.createCell(7);
					if (record.getR73_OTHER() != null) {
						cell7.setCellValue(record.getR73_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row73
					// Column I
					cell8 = row.createCell(8);
					if (record.getR73_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR73_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row73
					// Column J
					cell9 = row.createCell(9);
					if (record.getR73_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR73_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row74
					row = sheet.getRow(73);

					// row74
					// Column C
					cell2 = row.createCell(2);
					if (record.getR74_ISSUER() != null) {
						cell2.setCellValue(record.getR74_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row74
					// Column D
					cell3 = row.createCell(3);
					if (record.getR74_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR74_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row74
					// Column E
					cell4 = row.createCell(4);
					if (record.getR74_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR74_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row74
					// Column F
					cell5 = row.createCell(5);
					if (record.getR74_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR74_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row74
					// Column G
					cell6 = row.createCell(6);
					if (record.getR74_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR74_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row74
					// Column H
					cell7 = row.createCell(7);
					if (record.getR74_OTHER() != null) {
						cell7.setCellValue(record.getR74_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row74
					// Column I
					cell8 = row.createCell(8);
					if (record.getR74_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR74_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row74
					// Column J
					cell9 = row.createCell(9);
					if (record.getR74_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR74_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row75
					row = sheet.getRow(74);

					// row75
					// Column C
					cell2 = row.createCell(2);
					if (record.getR75_ISSUER() != null) {
						cell2.setCellValue(record.getR75_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row75
					// Column D
					cell3 = row.createCell(3);
					if (record.getR75_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR75_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row75
					// Column E
					cell4 = row.createCell(4);
					if (record.getR75_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR75_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row75
					// Column F
					cell5 = row.createCell(5);
					if (record.getR75_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR75_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row75
					// Column G
					cell6 = row.createCell(6);
					if (record.getR75_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR75_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row75
					// Column H
					cell7 = row.createCell(7);
					if (record.getR75_OTHER() != null) {
						cell7.setCellValue(record.getR75_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row75
					// Column I
					cell8 = row.createCell(8);
					if (record.getR75_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR75_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row75
					// Column J
					cell9 = row.createCell(9);
					if (record.getR75_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR75_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row76
					row = sheet.getRow(75);

					// row76
					// Column C
					cell2 = row.createCell(2);
					if (record.getR76_ISSUER() != null) {
						cell2.setCellValue(record.getR76_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row76
					// Column D
					cell3 = row.createCell(3);
					if (record.getR76_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR76_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row76
					// Column E
					cell4 = row.createCell(4);
					if (record.getR76_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR76_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row76
					// Column F
					cell5 = row.createCell(5);
					if (record.getR76_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR76_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row76
					// Column G
					cell6 = row.createCell(6);
					if (record.getR76_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR76_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row76
					// Column H
					cell7 = row.createCell(7);
					if (record.getR76_OTHER() != null) {
						cell7.setCellValue(record.getR76_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row76
					// Column I
					cell8 = row.createCell(8);
					if (record.getR76_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR76_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row76
					// Column J
					cell9 = row.createCell(9);
					if (record.getR76_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR76_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row77
					row = sheet.getRow(76);

					// row77
					// Column C
					cell2 = row.createCell(2);
					if (record.getR77_ISSUER() != null) {
						cell2.setCellValue(record.getR77_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row77
					// Column D
					cell3 = row.createCell(3);
					if (record.getR77_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR77_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row77
					// Column E
					cell4 = row.createCell(4);
					if (record.getR77_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR77_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row77
					// Column F
					cell5 = row.createCell(5);
					if (record.getR77_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR77_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row77
					// Column G
					cell6 = row.createCell(6);
					if (record.getR77_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR77_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row77
					// Column H
					cell7 = row.createCell(7);
					if (record.getR77_OTHER() != null) {
						cell7.setCellValue(record.getR77_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row77
					// Column I
					cell8 = row.createCell(8);
					if (record.getR77_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR77_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row77
					// Column J
					cell9 = row.createCell(9);
					if (record.getR77_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR77_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row78
					row = sheet.getRow(77);

					// row78
					// Column C
					cell2 = row.createCell(2);
					if (record.getR78_ISSUER() != null) {
						cell2.setCellValue(record.getR78_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row78
					// Column D
					cell3 = row.createCell(3);
					if (record.getR78_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR78_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row78
					// Column E
					cell4 = row.createCell(4);
					if (record.getR78_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR78_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row78
					// Column F
					cell5 = row.createCell(5);
					if (record.getR78_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR78_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row78
					// Column G
					cell6 = row.createCell(6);
					if (record.getR78_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR78_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row78
					// Column H
					cell7 = row.createCell(7);
					if (record.getR78_OTHER() != null) {
						cell7.setCellValue(record.getR78_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row78
					// Column I
					cell8 = row.createCell(8);
					if (record.getR78_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR78_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row78
					// Column J
					cell9 = row.createCell(9);
					if (record.getR78_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR78_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					// row79
					row = sheet.getRow(78);

					// row79
					// Column C
					cell2 = row.createCell(2);
					if (record.getR79_ISSUER() != null) {
						cell2.setCellValue(record.getR79_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row79
					// Column D
					cell3 = row.createCell(3);
					if (record.getR79_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR79_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row79
					// Column E
					cell4 = row.createCell(4);
					if (record.getR79_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR79_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row79
					// Column F
					cell5 = row.createCell(5);
					if (record.getR79_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR79_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row79
					// Column G
					cell6 = row.createCell(6);
					if (record.getR79_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR79_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row79
					// Column H
					cell7 = row.createCell(7);
					if (record.getR79_OTHER() != null) {
						cell7.setCellValue(record.getR79_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row79
					// Column I
					cell8 = row.createCell(8);
					if (record.getR79_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR79_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row79
					// Column J
					cell9 = row.createCell(9);
					if (record.getR79_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR79_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}
					// row80
					row = sheet.getRow(79);

					// row80
					// Column C
					cell2 = row.createCell(2);
					if (record.getR80_ISSUER() != null) {
						cell2.setCellValue(record.getR80_ISSUER().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row80
					// Column D
					cell3 = row.createCell(3);
					if (record.getR80_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR80_ISSUES_RATING().doubleValue());
						cell3.setCellStyle(numberStyle);

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(numberStyle);

					}

					// row80
					// Column E
					cell4 = row.createCell(4);
					if (record.getR80_1YR_VAL_OF_CRM() != null) {
						cell4.setCellValue(record.getR80_1YR_VAL_OF_CRM().doubleValue());
						cell4.setCellStyle(numberStyle);

					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(numberStyle);

					}

					// row80
					// Column F
					cell5 = row.createCell(5);
					if (record.getR80_1YR_5YR_VAL_OF_CRM() != null) {
						cell5.setCellValue(record.getR80_1YR_5YR_VAL_OF_CRM().doubleValue());
						cell5.setCellStyle(numberStyle);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(numberStyle);

					}

					// row80
					// Column G
					cell6 = row.createCell(6);
					if (record.getR80_5YR_VAL_OF_CRM() != null) {
						cell6.setCellValue(record.getR80_5YR_VAL_OF_CRM().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(numberStyle);
					}

					// row80
					// Column H
					cell7 = row.createCell(7);
					if (record.getR80_OTHER() != null) {
						cell7.setCellValue(record.getR80_OTHER().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(numberStyle);

					}

					// row80
					// Column I
					cell8 = row.createCell(8);
					if (record.getR80_STD_SUPERVISORY_HAIRCUT() != null) {
						cell8.setCellValue(record.getR80_STD_SUPERVISORY_HAIRCUT().doubleValue());
						cell8.setCellStyle(numberStyle);

					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(numberStyle);

					}

					// row80
					// Column J
					cell9 = row.createCell(9);
					if (record.getR80_APPLICABLE_RISK_WEIGHT() != null) {
						cell9.setCellValue(record.getR80_APPLICABLE_RISK_WEIGHT().doubleValue());
						cell9.setCellStyle(numberStyle);

					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(numberStyle);

					}

					row = sheet.getRow(80);

					cell2 = row.getCell(1);
					if (cell2 == null)
						cell2 = row.createCell(1);

					if (record.getR81_PRODUCT() != null) {
						cell2.setCellValue(record.getR81_PRODUCT().doubleValue());
					} else {
						cell2.setCellValue(0); // or leave previous value
					}

					cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);

					if (record.getR81_ISSUER() != null) {
						cell2.setCellValue(record.getR81_ISSUER().doubleValue());
					} else {
						cell2.setCellValue(0); // or leave previous value
					}

					cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);

					if (record.getR81_ISSUES_RATING() != null) {
						cell3.setCellValue(record.getR81_ISSUES_RATING().doubleValue());
					} else {
						cell3.setCellValue(0); // or leave previous value
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
