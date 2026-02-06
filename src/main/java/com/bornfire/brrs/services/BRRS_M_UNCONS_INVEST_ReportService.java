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

import javax.transaction.Transactional;

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

import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Summary_Repo;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Detail_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity;

@Component
@Service

public class BRRS_M_UNCONS_INVEST_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_UNCONS_INVEST_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_M_UNCONS_INVEST_Summary_Repo M_UNCONS_INVEST_Summary_Repo;

	@Autowired
	BRRS_M_UNCONS_INVEST_Detail_Repo M_UNCONS_INVEST_Detail_Repo;

	@Autowired
	BRRS_M_UNCONS_INVEST_Archival_Summary_Repo M_UNCONS_INVEST_Archival_Summary_Repo;

	@Autowired
	BRRS_M_UNCONS_INVEST_Archival_Detail_Repo M_UNCONS_INVEST_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_UNCONS_INVESTView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

				List<M_UNCONS_INVEST_Archival_Summary_Entity> T1Master = M_UNCONS_INVEST_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_UNCONS_INVEST_Archival_Summary_Entity> T1Master = M_UNCONS_INVEST_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<M_UNCONS_INVEST_Summary_Entity> T1Master = M_UNCONS_INVEST_Summary_Repo
						.getdatabydateList(dateformat.parse(todate));
				mv.addObject("displaymode", "summary");
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if (version != null) {
					List<M_UNCONS_INVEST_Archival_Detail_Entity> T1Master = M_UNCONS_INVEST_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_UNCONS_INVEST_Detail_Entity> T1Master = M_UNCONS_INVEST_Detail_Repo
							.getdatabydateList(dateformat.parse(todate));
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_UNCONS_INVEST");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	@Transactional
public void updateReport(M_UNCONS_INVEST_Summary_Entity updatedEntity) {
    System.out.println("Came to services 1");
    System.out.println("Report Date: " + updatedEntity.getReport_date());
  // 🔹 Fetch existing SUMMARY
    M_UNCONS_INVEST_Summary_Entity existingSummary = M_UNCONS_INVEST_Summary_Repo.findById(updatedEntity.getReport_date())
            .orElseThrow(() -> new RuntimeException(
                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

    // 🔹 Fetch or create DETAIL
    M_UNCONS_INVEST_Detail_Entity detailEntity = M_UNCONS_INVEST_Detail_Repo.findById(updatedEntity.getReport_date())
            .orElseGet(() -> {
                M_UNCONS_INVEST_Detail_Entity d = new M_UNCONS_INVEST_Detail_Entity();
                d.setReport_date(updatedEntity.getReport_date());
                return d;
            });

    try {
        // 1️⃣ Loop from R11 to R15 and copy fields
        for (int i = 11; i <= 15; i++) {
            String prefix = "R" + i + "_";
            

            String[] fields = {  "product","amount", "percent_of_cet1_holding", "percent_of_additional_tier_1_holding",
                                "percent_of_tier_2_holding"};

            for (String field : fields) {
                String getterName = "get" + prefix + field;
                String setterName = "set" + prefix + field;

                 try {
                    // Getter from UPDATED entity
                    Method getter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(getterName);

                    Object newValue = getter.invoke(updatedEntity);

                    // SUMMARY setter
                    Method summarySetter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(
                            setterName, getter.getReturnType());

                    summarySetter.invoke(existingSummary, newValue);

                    // DETAIL setter
                    Method detailSetter = M_UNCONS_INVEST_Detail_Entity.class.getMethod(
                            setterName, getter.getReturnType());

                    detailSetter.invoke(detailEntity, newValue);

                }catch (NoSuchMethodException e) {
                    // Skip missing fields
                    continue;
                }
            }
        }

        // 2️⃣ Handle R15 totals
        String[] totalFields = { "product","amount", "percent_of_cet1_holding","percent_of_additional_tier_1_holding","percent_of_tier_2_holding" };
        
        for (String field : totalFields) {
            String getterName = "getR15_" + field;
            String setterName = "setR15_" + field;

             try {
                    // Getter from UPDATED entity
                    Method getter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(getterName);

                    Object newValue = getter.invoke(updatedEntity);

                    // SUMMARY setter
                    Method summarySetter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(
                            setterName, getter.getReturnType());

                    summarySetter.invoke(existingSummary, newValue);

                    // DETAIL setter
                    Method detailSetter = M_UNCONS_INVEST_Detail_Entity.class.getMethod(
                            setterName, getter.getReturnType());

                    detailSetter.invoke(detailEntity, newValue);

                }catch (NoSuchMethodException e) {
                // Skip if not present
                continue;
            }
        }
      
    } catch (Exception e) {
        throw new RuntimeException("Error while updating report fields", e);
    }

    System.out.println("Saving Summary & Detail tables");

    // 💾 Save both tables
    M_UNCONS_INVEST_Summary_Repo.save(existingSummary);
    M_UNCONS_INVEST_Detail_Repo.save(detailEntity);

    System.out.println("Update completed successfully");
}


public void updateReport2(M_UNCONS_INVEST_Summary_Entity updatedEntity) {
    System.out.println("Came to services 2");
    System.out.println("Report Date: " + updatedEntity.getReport_date());

  // 🔹 Fetch existing SUMMARY
    M_UNCONS_INVEST_Summary_Entity existingSummary = M_UNCONS_INVEST_Summary_Repo.findById(updatedEntity.getReport_date())
            .orElseThrow(() -> new RuntimeException(
                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

    // 🔹 Fetch or create DETAIL
    M_UNCONS_INVEST_Detail_Entity detailEntity = M_UNCONS_INVEST_Detail_Repo.findById(updatedEntity.getReport_date())
            .orElseGet(() -> {
                M_UNCONS_INVEST_Detail_Entity d = new M_UNCONS_INVEST_Detail_Entity();
                d.setReport_date(updatedEntity.getReport_date());
                return d;
            });

    try {
        // 1️⃣ Loop from R11 to R50 and copy fields
        for (int i = 22; i <= 24; i++) {
            String prefix = "R" + i + "_";

            String[] fields = { "product","accuulated_equity_interest_5", "assets", "liabilities",
                                "revenue", "profit_or_loss", "unreg_share_of_loss" ,"cumulative_unreg_share_of_loss" };

            for (String field : fields) {
                String getterName = "get" + prefix + field;
                String setterName = "set" + prefix + field;

                try {
                    // Getter from UPDATED entity
                    Method getter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(getterName);

                    Object newValue = getter.invoke(updatedEntity);

                    // SUMMARY setter
                    Method summarySetter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(
                            setterName, getter.getReturnType());

                    summarySetter.invoke(existingSummary, newValue);

                    // DETAIL setter
                    Method detailSetter = M_UNCONS_INVEST_Detail_Entity.class.getMethod(
                            setterName, getter.getReturnType());

                    detailSetter.invoke(detailEntity, newValue);

                } catch (NoSuchMethodException e) {
                    // Skip missing fields
                    continue;
                }
            }
        }

        

      
    } catch (Exception e) {
        throw new RuntimeException("Error while updating report fields", e);
    }

    System.out.println("Saving Summary & Detail tables");

    // 💾 Save both tables
    M_UNCONS_INVEST_Summary_Repo.save(existingSummary);
    M_UNCONS_INVEST_Detail_Repo.save(detailEntity);

    System.out.println("Update completed successfully");
}


public void updateReport3(M_UNCONS_INVEST_Summary_Entity updatedEntity) {

    System.out.println("Came to services 3");
    System.out.println("Report Date: " + updatedEntity.getReport_date());

    // 🔹 Fetch existing SUMMARY
    M_UNCONS_INVEST_Summary_Entity existingSummary =
            M_UNCONS_INVEST_Summary_Repo.findById(updatedEntity.getReport_date())
            .orElseThrow(() -> new RuntimeException(
                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

    // 🔹 Fetch or create DETAIL
    M_UNCONS_INVEST_Detail_Entity detailEntity =
            M_UNCONS_INVEST_Detail_Repo.findById(updatedEntity.getReport_date())
            .orElseGet(() -> {
                M_UNCONS_INVEST_Detail_Entity d = new M_UNCONS_INVEST_Detail_Entity();
                d.setReport_date(updatedEntity.getReport_date());
                return d;
            });

    try {

        // 🔁 LOOP FOR R29 ONLY (LIKE OTHER METHODS)

        int i = 29;
        String prefix = "R" + i + "_";

        String[] fields = {
        		"product", "fair_value"
        };

        for (String field : fields) {

            String getterName = "get" + prefix + field;
            String setterName = "set" + prefix + field;

            try {

                // Getter from UPDATED entity
                Method getter =
                        M_UNCONS_INVEST_Summary_Entity.class.getMethod(getterName);

                Object newValue =
                        getter.invoke(updatedEntity);

                // SUMMARY setter
                Method summarySetter =
                        M_UNCONS_INVEST_Summary_Entity.class.getMethod(
                                setterName, getter.getReturnType());

                summarySetter.invoke(existingSummary, newValue);

                // DETAIL setter
                Method detailSetter =
                        M_UNCONS_INVEST_Detail_Entity.class.getMethod(
                                setterName, getter.getReturnType());

                detailSetter.invoke(detailEntity, newValue);

            } catch (NoSuchMethodException e) {
                // Skip if not present
                continue;
            }
        }

    } catch (Exception e) {
        throw new RuntimeException("Error while updating report fields", e);
    }

    System.out.println("Saving Summary & Detail tables");

    // 💾 Save both tables
    M_UNCONS_INVEST_Summary_Repo.save(existingSummary);
    M_UNCONS_INVEST_Detail_Repo.save(detailEntity);

    System.out.println("Update completed successfully");
}

public void updateReport4(M_UNCONS_INVEST_Summary_Entity updatedEntity) {
    System.out.println("Came to services 4");
    System.out.println("Report Date: " + updatedEntity.getReport_date());

  // 🔹 Fetch existing SUMMARY
    M_UNCONS_INVEST_Summary_Entity existingSummary = M_UNCONS_INVEST_Summary_Repo.findById(updatedEntity.getReport_date())
            .orElseThrow(() -> new RuntimeException(
                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

    // 🔹 Fetch or create DETAIL
    M_UNCONS_INVEST_Detail_Entity detailEntity = M_UNCONS_INVEST_Detail_Repo.findById(updatedEntity.getReport_date())
            .orElseGet(() -> {
                M_UNCONS_INVEST_Detail_Entity d = new M_UNCONS_INVEST_Detail_Entity();
                d.setReport_date(updatedEntity.getReport_date());
                return d;
            });

    try {
        // 1️⃣ Loop from R11 to R50 and copy fields
        for (int i = 35; i <= 38; i++) {
            String prefix = "R" + i + "_";

            String[] fields = { "product","company","jurisdiction_of_incorp_1", "jurisdiction_of_incorp_2", "line_of_business", "currency",
                                "share_capital", "accumulated_equity_interest" };

            for (String field : fields) {
                String getterName = "get" + prefix + field;
                String setterName = "set" + prefix + field;

                try {
                    // Getter from UPDATED entity
                    Method getter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(getterName);

                    Object newValue = getter.invoke(updatedEntity);

                    // SUMMARY setter
                    Method summarySetter = M_UNCONS_INVEST_Summary_Entity.class.getMethod(
                            setterName, getter.getReturnType());

                    summarySetter.invoke(existingSummary, newValue);

                    // DETAIL setter
                    Method detailSetter = M_UNCONS_INVEST_Detail_Entity.class.getMethod(
                            setterName, getter.getReturnType());

                    detailSetter.invoke(detailEntity, newValue);

                }catch (NoSuchMethodException e) {
                    // Skip missing fields
                    continue;
                }
            }
        }

      
    } catch (Exception e) {
        throw new RuntimeException("Error while updating report fields", e);
    }

    System.out.println("Saving Summary & Detail tables");

    // 💾 Save both tables
    M_UNCONS_INVEST_Summary_Repo.save(existingSummary);
    M_UNCONS_INVEST_Detail_Repo.save(detailEntity);

    System.out.println("Update completed successfully");
}



	public byte[] BRRS_M_UNCONS_INVESTExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if (type.equals("ARCHIVAL") & version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_UNCONS_INVESTARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);

		}
		// Email check
		if ("email".equalsIgnoreCase(type) && version == null) {
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_UNCONS_INVESTEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		} else if ("email".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_UNCONS_INVESTEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);

		}
		List<M_UNCONS_INVEST_Summary_Entity> dataList = M_UNCONS_INVEST_Summary_Repo
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

					int startRow = 10;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_UNCONS_INVEST_Summary_Entity record = dataList.get(i);

							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							// row11
							// Column D
							Cell cell3 = row.getCell(3);
							if (record.getR11_amount() != null) {
								cell3.setCellValue(record.getR11_amount().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);

							}

							// row11
							// Column E
							Cell cell4 = row.createCell(4);
							if (record.getR11_percent_of_cet1_holding() != null) {
								cell4.setCellValue(record.getR11_percent_of_cet1_holding().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// row11
							// Column F
							Cell cell5 = row.createCell(5);
							if (record.getR11_percent_of_additional_tier_1_holding() != null) {
								cell5.setCellValue(record.getR11_percent_of_additional_tier_1_holding().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}
							
							// row11
							// Column G
							Cell cell6 = row.createCell(6);
							if (record.getR11_percent_of_tier_2_holding() != null) {
								cell6.setCellValue(record.getR11_percent_of_tier_2_holding().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(11);
							// Column D
							cell3 = row.getCell(3);
							if (record.getR12_amount() != null) {
								cell3.setCellValue(record.getR12_amount().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);

							}

							// row12
							// Column E
							cell4 = row.createCell(4);
							if (record.getR12_percent_of_cet1_holding() != null) {
								cell4.setCellValue(record.getR12_percent_of_cet1_holding().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// row12
							// Column F
							cell5 = row.createCell(5);
							if (record.getR12_percent_of_additional_tier_1_holding() != null) {
								cell5.setCellValue(record.getR12_percent_of_additional_tier_1_holding().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}
							
							// row12
							// Column G
							cell6 = row.createCell(6);
							if (record.getR12_percent_of_tier_2_holding() != null) {
								cell6.setCellValue(record.getR12_percent_of_tier_2_holding().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
							
							// row13
							row = sheet.getRow(12);
							// Column D
							cell3 = row.getCell(3);
							if (record.getR13_amount() != null) {
								cell3.setCellValue(record.getR13_amount().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);

							}

							// row13
							// Column E
							cell4 = row.createCell(4);
							if (record.getR13_percent_of_cet1_holding() != null) {
								cell4.setCellValue(record.getR13_percent_of_cet1_holding().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// row13
							// Column F
							cell5 = row.createCell(5);
							if (record.getR13_percent_of_additional_tier_1_holding() != null) {
								cell5.setCellValue(record.getR13_percent_of_additional_tier_1_holding().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}
							
							// row13
							// Column G
							cell6 = row.createCell(6);
							if (record.getR13_percent_of_tier_2_holding() != null) {
								cell6.setCellValue(record.getR13_percent_of_tier_2_holding().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
							
							// row14
							row = sheet.getRow(13);
							// Column D
							cell3 = row.getCell(3);
							if (record.getR14_amount() != null) {
								cell3.setCellValue(record.getR14_amount().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);

							}

							// row14
							// Column E
							cell4 = row.createCell(4);
							if (record.getR14_percent_of_cet1_holding() != null) {
								cell4.setCellValue(record.getR14_percent_of_cet1_holding().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// row14
							// Column F
							cell5 = row.createCell(5);
							if (record.getR14_percent_of_additional_tier_1_holding() != null) {
								cell5.setCellValue(record.getR14_percent_of_additional_tier_1_holding().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}
							
							// row14
							// Column G
							cell6 = row.createCell(6);
							if (record.getR14_percent_of_tier_2_holding() != null) {
								cell6.setCellValue(record.getR14_percent_of_tier_2_holding().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
							
							// row15
							row = sheet.getRow(14);
						

							// row15
							// Column E
							cell4 = row.createCell(4);
							if (record.getR15_percent_of_cet1_holding() != null) {
								cell4.setCellValue(record.getR15_percent_of_cet1_holding().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// row15
							// Column F
							cell5 = row.createCell(5);
							if (record.getR15_percent_of_additional_tier_1_holding() != null) {
								cell5.setCellValue(record.getR15_percent_of_additional_tier_1_holding().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}
							
							// row15
							// Column G
							cell6 = row.createCell(6);
							if (record.getR15_percent_of_tier_2_holding() != null) {
								cell6.setCellValue(record.getR15_percent_of_tier_2_holding().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
							// row22
							row = sheet.getRow(21);
							// Column C
							Cell cell2 = row.getCell(2);
							if (record.getR22_accuulated_equity_interest_5() != null) {
								cell2.setCellValue(record.getR22_accuulated_equity_interest_5().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row22
							// Column D
							cell3 = row.createCell(3);
							if (record.getR22_assets() != null) {
								cell3.setCellValue(record.getR22_assets().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							
							// row22
							// Column E
							cell4 = row.createCell(4);
							if (record.getR22_liabilities() != null) {
								cell4.setCellValue(record.getR22_liabilities().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// row22
							// Column F
							cell5 = row.createCell(5);
							if (record.getR22_revenue() != null) {
								cell5.setCellValue(record.getR22_revenue().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}
							
							// row22
							// Column G
							cell6 = row.createCell(6);
							if (record.getR22_profit_or_loss() != null) {
								cell6.setCellValue(record.getR22_profit_or_loss().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
							
							// row22
							// Column H
							Cell cell7 = row.createCell(7);
							if (record.getR22_unreg_share_of_loss() != null) {
								cell7.setCellValue(record.getR22_unreg_share_of_loss().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
							
							// row22
							// Column I
							Cell cell8 = row.createCell(8);
							if (record.getR22_cumulative_unreg_share_of_loss() != null) {
								cell8.setCellValue(record.getR22_cumulative_unreg_share_of_loss().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}
							
							// row23
							row = sheet.getRow(22);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR23_accuulated_equity_interest_5() != null) {
								cell2.setCellValue(record.getR23_accuulated_equity_interest_5().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row23
							// Column D
							cell3 = row.createCell(3);
							if (record.getR23_assets() != null) {
								cell3.setCellValue(record.getR23_assets().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							
							// row23
							// Column E
							cell4 = row.createCell(4);
							if (record.getR23_liabilities() != null) {
								cell4.setCellValue(record.getR23_liabilities().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// row23
							// Column F
							cell5 = row.createCell(5);
							if (record.getR23_revenue() != null) {
								cell5.setCellValue(record.getR23_revenue().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}
							
							// row23
							// Column G
							cell6 = row.createCell(6);
							if (record.getR23_profit_or_loss() != null) {
								cell6.setCellValue(record.getR23_profit_or_loss().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
							
							// row23
							// Column H
							cell7 = row.createCell(7);
							if (record.getR23_unreg_share_of_loss() != null) {
								cell7.setCellValue(record.getR23_unreg_share_of_loss().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
							
							// row23
							// Column I
							cell8 = row.createCell(8);
							if (record.getR23_cumulative_unreg_share_of_loss() != null) {
								cell8.setCellValue(record.getR23_cumulative_unreg_share_of_loss().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}
							
							
							// row24
							row = sheet.getRow(23);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR24_accuulated_equity_interest_5() != null) {
								cell2.setCellValue(record.getR24_accuulated_equity_interest_5().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row24
							// Column D
							cell3 = row.createCell(3);
							if (record.getR24_assets() != null) {
								cell3.setCellValue(record.getR24_assets().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							
							// row24
							// Column E
							cell4 = row.createCell(4);
							if (record.getR24_liabilities() != null) {
								cell4.setCellValue(record.getR24_liabilities().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// row24
							// Column F
							cell5 = row.createCell(5);
							if (record.getR24_revenue() != null) {
								cell5.setCellValue(record.getR24_revenue().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}
							
							// row24
							// Column G
							cell6 = row.createCell(6);
							if (record.getR24_profit_or_loss() != null) {
								cell6.setCellValue(record.getR24_profit_or_loss().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
							
							// row24
							// Column H
							cell7 = row.createCell(7);
							if (record.getR24_unreg_share_of_loss() != null) {
								cell7.setCellValue(record.getR24_unreg_share_of_loss().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
							
							// row24
							// Column I
							cell8 = row.createCell(8);
							if (record.getR24_cumulative_unreg_share_of_loss() != null) {
								cell8.setCellValue(record.getR24_cumulative_unreg_share_of_loss().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}
							
							// row29
							row = sheet.getRow(28);
							// Column G
							cell6 = row.getCell(6);
							if (record.getR29_fair_value() != null) {
								cell6.setCellValue(record.getR29_fair_value().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);

							}
							
							// row35
							row = sheet.getRow(34);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR35_company() != null) {
								cell2.setCellValue(record.getR35_company().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row35
							// Column D
							cell3 = row.createCell(3);
							if (record.getR35_jurisdiction_of_incorp_1() != null) {
								cell3.setCellValue(record.getR35_jurisdiction_of_incorp_1().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							
							// row35
							// Column E
							cell4 = row.createCell(4);
							if (record.getR35_jurisdiction_of_incorp_2() != null) {
								cell4.setCellValue(record.getR35_jurisdiction_of_incorp_2().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// row35
							// Column F
							cell5 = row.createCell(5);
							if (record.getR35_line_of_business() != null) {
								cell5.setCellValue(record.getR35_line_of_business().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}
							
							// row35
							// Column G
							cell6 = row.createCell(6);
							if (record.getR35_currency() != null) {
								cell6.setCellValue(record.getR35_currency().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
							
							// row35
							// Column H
							cell7 = row.createCell(7);
							if (record.getR35_share_capital() != null) {
								cell7.setCellValue(record.getR35_share_capital().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
							
							// row35
							// Column I
							cell8 = row.createCell(8);
							if (record.getR35_accumulated_equity_interest() != null) {
								cell8.setCellValue(record.getR35_accumulated_equity_interest().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}
							
							// row36
							row = sheet.getRow(35);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR36_company() != null) {
								cell2.setCellValue(record.getR36_company().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row36
							// Column D
							cell3 = row.createCell(3);
							if (record.getR36_jurisdiction_of_incorp_1() != null) {
								cell3.setCellValue(record.getR36_jurisdiction_of_incorp_1().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							
							// row36
							// Column E
							cell4 = row.createCell(4);
							if (record.getR36_jurisdiction_of_incorp_2() != null) {
								cell4.setCellValue(record.getR36_jurisdiction_of_incorp_2().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// row36
							// Column F
							cell5 = row.createCell(5);
							if (record.getR36_line_of_business() != null) {
								cell5.setCellValue(record.getR36_line_of_business().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}
							
							// row36
							// Column G
							cell6 = row.createCell(6);
							if (record.getR36_currency() != null) {
								cell6.setCellValue(record.getR36_currency().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
							
							// row36
							// Column H
							cell7 = row.createCell(7);
							if (record.getR36_share_capital() != null) {
								cell7.setCellValue(record.getR36_share_capital().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
							
							// row36
							// Column I
							cell8 = row.createCell(8);
							if (record.getR36_accumulated_equity_interest() != null) {
								cell8.setCellValue(record.getR36_accumulated_equity_interest().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}
							
							// row37
							row = sheet.getRow(36);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR37_company() != null) {
								cell2.setCellValue(record.getR37_company().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row37
							// Column D
							cell3 = row.createCell(3);
							if (record.getR37_jurisdiction_of_incorp_1() != null) {
								cell3.setCellValue(record.getR37_jurisdiction_of_incorp_1().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							
							// row37
							// Column E
							cell4 = row.createCell(4);
							if (record.getR37_jurisdiction_of_incorp_2() != null) {
								cell4.setCellValue(record.getR37_jurisdiction_of_incorp_2().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// row37
							// Column F
							cell5 = row.createCell(5);
							if (record.getR37_line_of_business() != null) {
								cell5.setCellValue(record.getR37_line_of_business().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}
							
							// row37
							// Column G
							cell6 = row.createCell(6);
							if (record.getR37_currency() != null) {
								cell6.setCellValue(record.getR37_currency().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
							
							// row37
							// Column H
							cell7 = row.createCell(7);
							if (record.getR37_share_capital() != null) {
								cell7.setCellValue(record.getR37_share_capital().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
							
							// row37
							// Column I
							cell8 = row.createCell(8);
							if (record.getR37_accumulated_equity_interest() != null) {
								cell8.setCellValue(record.getR37_accumulated_equity_interest().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
							}
							
							// row38
							row = sheet.getRow(37);
							// Column C
							cell2 = row.getCell(2);
							if (record.getR38_company() != null) {
								cell2.setCellValue(record.getR38_company().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);

							}

							// row38
							// Column D
							cell3 = row.createCell(3);
							if (record.getR38_jurisdiction_of_incorp_1() != null) {
								cell3.setCellValue(record.getR38_jurisdiction_of_incorp_1().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							
							// row38
							// Column E
							cell4 = row.createCell(4);
							if (record.getR38_jurisdiction_of_incorp_2() != null) {
								cell4.setCellValue(record.getR38_jurisdiction_of_incorp_2().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							
							// row38
							// Column F
							cell5 = row.createCell(5);
							if (record.getR38_line_of_business() != null) {
								cell5.setCellValue(record.getR38_line_of_business().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}
							
							// row38
							// Column G
							cell6 = row.createCell(6);
							if (record.getR38_currency() != null) {
								cell6.setCellValue(record.getR38_currency().doubleValue());
								cell6.setCellStyle(numberStyle);
							} else {
								cell6.setCellValue("");
								cell6.setCellStyle(textStyle);
							}
							
							// row38
							// Column H
							cell7 = row.createCell(7);
							if (record.getR38_share_capital() != null) {
								cell7.setCellValue(record.getR38_share_capital().doubleValue());
								cell7.setCellStyle(numberStyle);
							} else {
								cell7.setCellValue("");
								cell7.setCellStyle(textStyle);
							}
							
							// row38
							// Column I
							cell8 = row.createCell(8);
							if (record.getR38_accumulated_equity_interest() != null) {
								cell8.setCellValue(record.getR38_accumulated_equity_interest().doubleValue());
								cell8.setCellStyle(numberStyle);
							} else {
								cell8.setCellValue("");
								cell8.setCellStyle(textStyle);
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

	public byte[] getExcelM_UNCONS_INVESTARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {

		}
		List<M_UNCONS_INVEST_Archival_Summary_Entity> dataList = M_UNCONS_INVEST_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_UNCONS_INVEST report. Returning empty result.");
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
			int startRow = 10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_UNCONS_INVEST_Archival_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR11_amount() != null) {
						cell3.setCellValue(record.getR11_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR11_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR11_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row11
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR11_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR11_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR12_amount() != null) {
						cell3.setCellValue(record.getR12_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row12
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR12_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR12_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row12
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR12_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR13_amount() != null) {
						cell3.setCellValue(record.getR13_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR13_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR13_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR13_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR14_amount() != null) {
						cell3.setCellValue(record.getR14_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR14_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR14_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR14_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row15
					row = sheet.getRow(14);
				

					// row15
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_percent_of_cet1_holding() != null) {
						cell4.setCellValue(record.getR15_percent_of_cet1_holding().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_percent_of_additional_tier_1_holding() != null) {
						cell5.setCellValue(record.getR15_percent_of_additional_tier_1_holding().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row15
					// Column G
					cell6 = row.createCell(6);
					if (record.getR15_percent_of_tier_2_holding() != null) {
						cell6.setCellValue(record.getR15_percent_of_tier_2_holding().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// row22
					row = sheet.getRow(21);
					// Column C
					Cell cell2 = row.getCell(2);
					if (record.getR22_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR22_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row22
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_assets() != null) {
						cell3.setCellValue(record.getR22_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_liabilities() != null) {
						cell4.setCellValue(record.getR22_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_revenue() != null) {
						cell5.setCellValue(record.getR22_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_profit_or_loss() != null) {
						cell6.setCellValue(record.getR22_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row22
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR22_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR22_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row22
					// Column I
					Cell cell8 = row.createCell(8);
					if (record.getR22_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR22_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row23
					row = sheet.getRow(22);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR23_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR23_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_assets() != null) {
						cell3.setCellValue(record.getR23_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_liabilities() != null) {
						cell4.setCellValue(record.getR23_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_revenue() != null) {
						cell5.setCellValue(record.getR23_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_profit_or_loss() != null) {
						cell6.setCellValue(record.getR23_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row23
					// Column H
					cell7 = row.createCell(7);
					if (record.getR23_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR23_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row23
					// Column I
					cell8 = row.createCell(8);
					if (record.getR23_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR23_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// row24
					row = sheet.getRow(23);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR24_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR24_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_assets() != null) {
						cell3.setCellValue(record.getR24_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_liabilities() != null) {
						cell4.setCellValue(record.getR24_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_revenue() != null) {
						cell5.setCellValue(record.getR24_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_profit_or_loss() != null) {
						cell6.setCellValue(record.getR24_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row24
					// Column H
					cell7 = row.createCell(7);
					if (record.getR24_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR24_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row24
					// Column I
					cell8 = row.createCell(8);
					if (record.getR24_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR24_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row29
					row = sheet.getRow(28);
					// Column G
					cell6 = row.getCell(6);
					if (record.getR29_fair_value() != null) {
						cell6.setCellValue(record.getR29_fair_value().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);

					}
					
					// row35
					row = sheet.getRow(34);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR35_company() != null) {
						cell2.setCellValue(record.getR35_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR35_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row35
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR35_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row35
					// Column F
					cell5 = row.createCell(5);
					if (record.getR35_line_of_business() != null) {
						cell5.setCellValue(record.getR35_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row35
					// Column G
					cell6 = row.createCell(6);
					if (record.getR35_currency() != null) {
						cell6.setCellValue(record.getR35_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row35
					// Column H
					cell7 = row.createCell(7);
					if (record.getR35_share_capital() != null) {
						cell7.setCellValue(record.getR35_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row35
					// Column I
					cell8 = row.createCell(8);
					if (record.getR35_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR35_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row36
					row = sheet.getRow(35);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR36_company() != null) {
						cell2.setCellValue(record.getR36_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row36
					// Column D
					cell3 = row.createCell(3);
					if (record.getR36_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR36_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row36
					// Column E
					cell4 = row.createCell(4);
					if (record.getR36_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR36_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record.getR36_line_of_business() != null) {
						cell5.setCellValue(record.getR36_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row36
					// Column G
					cell6 = row.createCell(6);
					if (record.getR36_currency() != null) {
						cell6.setCellValue(record.getR36_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row36
					// Column H
					cell7 = row.createCell(7);
					if (record.getR36_share_capital() != null) {
						cell7.setCellValue(record.getR36_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row36
					// Column I
					cell8 = row.createCell(8);
					if (record.getR36_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR36_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row37
					row = sheet.getRow(36);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR37_company() != null) {
						cell2.setCellValue(record.getR37_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR37_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row37
					// Column E
					cell4 = row.createCell(4);
					if (record.getR37_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR37_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row37
					// Column F
					cell5 = row.createCell(5);
					if (record.getR37_line_of_business() != null) {
						cell5.setCellValue(record.getR37_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row37
					// Column G
					cell6 = row.createCell(6);
					if (record.getR37_currency() != null) {
						cell6.setCellValue(record.getR37_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row37
					// Column H
					cell7 = row.createCell(7);
					if (record.getR37_share_capital() != null) {
						cell7.setCellValue(record.getR37_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row37
					// Column I
					cell8 = row.createCell(8);
					if (record.getR37_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR37_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row38
					row = sheet.getRow(37);
					// Column C
					cell2 = row.getCell(2);
					if (record.getR38_company() != null) {
						cell2.setCellValue(record.getR38_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row38
					// Column D
					cell3 = row.createCell(3);
					if (record.getR38_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR38_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row38
					// Column E
					cell4 = row.createCell(4);
					if (record.getR38_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR38_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row38
					// Column F
					cell5 = row.createCell(5);
					if (record.getR38_line_of_business() != null) {
						cell5.setCellValue(record.getR38_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row38
					// Column G
					cell6 = row.createCell(6);
					if (record.getR38_currency() != null) {
						cell6.setCellValue(record.getR38_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row38
					// Column H
					cell7 = row.createCell(7);
					if (record.getR38_share_capital() != null) {
						cell7.setCellValue(record.getR38_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row38
					// Column I
					cell8 = row.createCell(8);
					if (record.getR38_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR38_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
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

//	public List<Object[]> getM_UNCONS_INVESTResub() {
//		List<Object[]> resubList = new ArrayList<>();
//		try {
//			List<M_UNCONS_INVEST_Archival_Summary_Entity> latestArchivalList = M_UNCONS_INVEST_Archival_Summary_Repo
//					.getdatabydateListWithVersion();
//
//			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
//				for (M_UNCONS_INVEST_Archival_Summary_Entity entity : latestArchivalList) {
//					resubList.add(new Object[] { entity.getReport_date(), entity.getReport_version() });
//				}
//				System.out.println("Fetched " + resubList.size() + " record(s)");
//			} else {
//				System.out.println("No archival data found.");
//			}
//
//		} catch (Exception e) {
//			System.err.println("Error fetching M_UNCONS_INVEST Resub data: " + e.getMessage());
//			e.printStackTrace();
//		}
//		return resubList;
//	}

	public List<Object[]> getM_UNCONS_INVESTArchival() {
		List<Object[]> archivalList = new ArrayList<>();
		try {
			List<M_UNCONS_INVEST_Archival_Summary_Entity> latestArchivalList = M_UNCONS_INVEST_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_UNCONS_INVEST_Archival_Summary_Entity entity : latestArchivalList) {
					archivalList.add(new Object[] { entity.getReport_date(), entity.getReport_version() });
				}
				System.out.println("Fetched " + archivalList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_UNCONS_INVEST Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return archivalList;
	}

	// Email
	public byte[] BRRS_M_UNCONS_INVESTEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		Date reportDate = dateformat.parse(todate);
		if (type.equals("email") & version != null) {

		}
		List<M_UNCONS_INVEST_Summary_Entity> dataList = M_UNCONS_INVEST_Summary_Repo.getdatabydateList(reportDate);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found forM_UNCONS_INVEST report. Returning empty result.");
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
					M_UNCONS_INVEST_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column D
					Cell cell3 = row.getCell(4);
					if (record.getR11_amount() != null) {
						cell3.setCellValue(record.getR11_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}
					
					// row14
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(4);
					if (record.getR14_amount() != null) {
						cell3.setCellValue(record.getR14_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row15
					row = sheet.getRow(13);
					// Column D
					cell3 = row.getCell(4);
					if (record.getR15_amount() != null) {
						cell3.setCellValue(record.getR15_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}
					
					
					// row22
					row = sheet.getRow(20);
					// Column C
					Cell cell2 = row.getCell(3);
					if (record.getR22_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR22_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row22
					// Column D
					cell3 = row.createCell(4);
					if (record.getR22_assets() != null) {
						cell3.setCellValue(record.getR22_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row22
					// Column E
					Cell cell4 = row.createCell(5);
					if (record.getR22_liabilities() != null) {
						cell4.setCellValue(record.getR22_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row22
					// Column F
					Cell cell5 = row.createCell(6);
					if (record.getR22_revenue() != null) {
						cell5.setCellValue(record.getR22_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row22
					// Column G
					Cell cell6 = row.createCell(7);
					if (record.getR22_profit_or_loss() != null) {
						cell6.setCellValue(record.getR22_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row22
					// Column H
					Cell cell7 = row.createCell(8);
					if (record.getR22_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR22_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row22
					// Column I
					Cell cell8 = row.createCell(9);
					if (record.getR22_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR22_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row23
					row = sheet.getRow(21);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR23_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR23_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}
					
					// row23
					// Column H
					cell7 = row.createCell(8);
					if (record.getR23_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR23_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row23
					// Column I
					cell8 = row.createCell(9);
					if (record.getR23_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR23_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// row24
					row = sheet.getRow(22);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR24_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR24_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row24
					// Column D
					cell3 = row.createCell(4);
					if (record.getR24_assets() != null) {
						cell3.setCellValue(record.getR24_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row24
					// Column E
					cell4 = row.createCell(5);
					if (record.getR24_liabilities() != null) {
						cell4.setCellValue(record.getR24_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row24
					// Column F
					cell5 = row.createCell(6);
					if (record.getR24_revenue() != null) {
						cell5.setCellValue(record.getR24_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row24
					// Column G
					cell6 = row.createCell(7);
					if (record.getR24_profit_or_loss() != null) {
						cell6.setCellValue(record.getR24_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row24
					// Column H
					cell7 = row.createCell(8);
					if (record.getR24_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR24_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row24
					// Column I
					cell8 = row.createCell(9);
					if (record.getR24_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR24_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row29
					row = sheet.getRow(27);
					// Column G
					cell6 = row.getCell(7);
					if (record.getR29_fair_value() != null) {
						cell6.setCellValue(record.getR29_fair_value().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);

					}
					
					// row35
					row = sheet.getRow(33);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR35_company() != null) {
						cell2.setCellValue(record.getR35_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row35
					// Column D
					cell3 = row.createCell(4);
					if (record.getR35_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR35_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row35
					// Column E
					cell4 = row.createCell(5);
					if (record.getR35_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR35_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row35
					// Column F
					cell5 = row.createCell(6);
					if (record.getR35_line_of_business() != null) {
						cell5.setCellValue(record.getR35_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row35
					// Column G
					cell6 = row.createCell(7);
					if (record.getR35_currency() != null) {
						cell6.setCellValue(record.getR35_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row35
					// Column H
					cell7 = row.createCell(8);
					if (record.getR35_share_capital() != null) {
						cell7.setCellValue(record.getR35_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row35
					// Column I
					cell8 = row.createCell(9);
					if (record.getR35_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR35_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row36
					row = sheet.getRow(34);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR36_company() != null) {
						cell2.setCellValue(record.getR36_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}
					
					// row36
					// Column H
					cell7 = row.createCell(8);
					if (record.getR36_share_capital() != null) {
						cell7.setCellValue(record.getR36_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row36
					// Column I
					cell8 = row.createCell(9);
					if (record.getR36_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR36_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row37
					row = sheet.getRow(35);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR37_company() != null) {
						cell2.setCellValue(record.getR37_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row37
					// Column D
					cell3 = row.createCell(4);
					if (record.getR37_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR37_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row37
					// Column E
					cell4 = row.createCell(5);
					if (record.getR37_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR37_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row37
					// Column F
					cell5 = row.createCell(6);
					if (record.getR37_line_of_business() != null) {
						cell5.setCellValue(record.getR37_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row37
					// Column G
					cell6 = row.createCell(7);
					if (record.getR37_currency() != null) {
						cell6.setCellValue(record.getR37_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row37
					// Column H
					cell7 = row.createCell(8);
					if (record.getR37_share_capital() != null) {
						cell7.setCellValue(record.getR37_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row37
					// Column I
					cell8 = row.createCell(9);
					if (record.getR37_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR37_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row38
					row = sheet.getRow(36);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR38_company() != null) {
						cell2.setCellValue(record.getR38_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row38
					// Column D
					cell3 = row.createCell(4);
					if (record.getR38_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR38_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row38
					// Column E
					cell4 = row.createCell(5);
					if (record.getR38_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR38_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row38
					// Column F
					cell5 = row.createCell(6);
					if (record.getR38_line_of_business() != null) {
						cell5.setCellValue(record.getR38_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row38
					// Column G
					cell6 = row.createCell(7);
					if (record.getR38_currency() != null) {
						cell6.setCellValue(record.getR38_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row38
					// Column H
					cell7 = row.createCell(8);
					if (record.getR38_share_capital() != null) {
						cell7.setCellValue(record.getR38_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row38
					// Column I
					cell8 = row.createCell(9);
					if (record.getR38_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR38_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	public byte[] BRRS_M_UNCONS_INVESTEmailArchivalExcel(String filename, String reportId, String fromdate,
            String todate,
            String currency, String dtltype, String type, BigDecimal version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");

        List<M_UNCONS_INVEST_Archival_Summary_Entity> dataList = M_UNCONS_INVEST_Archival_Summary_Repo
                .getdatabydateListarchival(dateformat.parse(todate), version);

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found forM_UNCONS_INVEST report. Returning empty result.");
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
					M_UNCONS_INVEST_Archival_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column D
					Cell cell3 = row.getCell(4);
					if (record.getR11_amount() != null) {
						cell3.setCellValue(record.getR11_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}
					
					// row14
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(4);
					if (record.getR14_amount() != null) {
						cell3.setCellValue(record.getR14_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row15
					row = sheet.getRow(13);
					// Column D
					cell3 = row.getCell(4);
					if (record.getR15_amount() != null) {
						cell3.setCellValue(record.getR15_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}
					
					
					// row22
					row = sheet.getRow(20);
					// Column C
					Cell cell2 = row.getCell(3);
					if (record.getR22_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR22_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row22
					// Column D
					cell3 = row.createCell(4);
					if (record.getR22_assets() != null) {
						cell3.setCellValue(record.getR22_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row22
					// Column E
					Cell cell4 = row.createCell(5);
					if (record.getR22_liabilities() != null) {
						cell4.setCellValue(record.getR22_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row22
					// Column F
					Cell cell5 = row.createCell(6);
					if (record.getR22_revenue() != null) {
						cell5.setCellValue(record.getR22_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row22
					// Column G
					Cell cell6 = row.createCell(7);
					if (record.getR22_profit_or_loss() != null) {
						cell6.setCellValue(record.getR22_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row22
					// Column H
					Cell cell7 = row.createCell(8);
					if (record.getR22_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR22_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row22
					// Column I
					Cell cell8 = row.createCell(9);
					if (record.getR22_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR22_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row23
					row = sheet.getRow(21);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR23_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR23_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}
					
					// row23
					// Column H
					cell7 = row.createCell(8);
					if (record.getR23_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR23_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row23
					// Column I
					cell8 = row.createCell(9);
					if (record.getR23_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR23_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// row24
					row = sheet.getRow(22);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR24_accuulated_equity_interest_5() != null) {
						cell2.setCellValue(record.getR24_accuulated_equity_interest_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row24
					// Column D
					cell3 = row.createCell(4);
					if (record.getR24_assets() != null) {
						cell3.setCellValue(record.getR24_assets().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row24
					// Column E
					cell4 = row.createCell(5);
					if (record.getR24_liabilities() != null) {
						cell4.setCellValue(record.getR24_liabilities().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row24
					// Column F
					cell5 = row.createCell(6);
					if (record.getR24_revenue() != null) {
						cell5.setCellValue(record.getR24_revenue().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row24
					// Column G
					cell6 = row.createCell(7);
					if (record.getR24_profit_or_loss() != null) {
						cell6.setCellValue(record.getR24_profit_or_loss().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row24
					// Column H
					cell7 = row.createCell(8);
					if (record.getR24_unreg_share_of_loss() != null) {
						cell7.setCellValue(record.getR24_unreg_share_of_loss().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row24
					// Column I
					cell8 = row.createCell(9);
					if (record.getR24_cumulative_unreg_share_of_loss() != null) {
						cell8.setCellValue(record.getR24_cumulative_unreg_share_of_loss().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row29
					row = sheet.getRow(27);
					// Column G
					cell6 = row.getCell(7);
					if (record.getR29_fair_value() != null) {
						cell6.setCellValue(record.getR29_fair_value().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);

					}
					
					// row35
					row = sheet.getRow(33);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR35_company() != null) {
						cell2.setCellValue(record.getR35_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row35
					// Column D
					cell3 = row.createCell(4);
					if (record.getR35_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR35_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row35
					// Column E
					cell4 = row.createCell(5);
					if (record.getR35_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR35_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row35
					// Column F
					cell5 = row.createCell(6);
					if (record.getR35_line_of_business() != null) {
						cell5.setCellValue(record.getR35_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row35
					// Column G
					cell6 = row.createCell(7);
					if (record.getR35_currency() != null) {
						cell6.setCellValue(record.getR35_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row35
					// Column H
					cell7 = row.createCell(8);
					if (record.getR35_share_capital() != null) {
						cell7.setCellValue(record.getR35_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row35
					// Column I
					cell8 = row.createCell(9);
					if (record.getR35_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR35_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row36
					row = sheet.getRow(34);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR36_company() != null) {
						cell2.setCellValue(record.getR36_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}
					
					// row36
					// Column H
					cell7 = row.createCell(8);
					if (record.getR36_share_capital() != null) {
						cell7.setCellValue(record.getR36_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row36
					// Column I
					cell8 = row.createCell(9);
					if (record.getR36_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR36_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row37
					row = sheet.getRow(35);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR37_company() != null) {
						cell2.setCellValue(record.getR37_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row37
					// Column D
					cell3 = row.createCell(4);
					if (record.getR37_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR37_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row37
					// Column E
					cell4 = row.createCell(5);
					if (record.getR37_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR37_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row37
					// Column F
					cell5 = row.createCell(6);
					if (record.getR37_line_of_business() != null) {
						cell5.setCellValue(record.getR37_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row37
					// Column G
					cell6 = row.createCell(7);
					if (record.getR37_currency() != null) {
						cell6.setCellValue(record.getR37_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row37
					// Column H
					cell7 = row.createCell(8);
					if (record.getR37_share_capital() != null) {
						cell7.setCellValue(record.getR37_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row37
					// Column I
					cell8 = row.createCell(9);
					if (record.getR37_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR37_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row38
					row = sheet.getRow(36);
					// Column C
					cell2 = row.getCell(3);
					if (record.getR38_company() != null) {
						cell2.setCellValue(record.getR38_company().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row38
					// Column D
					cell3 = row.createCell(4);
					if (record.getR38_jurisdiction_of_incorp_1() != null) {
						cell3.setCellValue(record.getR38_jurisdiction_of_incorp_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row38
					// Column E
					cell4 = row.createCell(5);
					if (record.getR38_jurisdiction_of_incorp_2() != null) {
						cell4.setCellValue(record.getR38_jurisdiction_of_incorp_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row38
					// Column F
					cell5 = row.createCell(6);
					if (record.getR38_line_of_business() != null) {
						cell5.setCellValue(record.getR38_line_of_business().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row38
					// Column G
					cell6 = row.createCell(7);
					if (record.getR38_currency() != null) {
						cell6.setCellValue(record.getR38_currency().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row38
					// Column H
					cell7 = row.createCell(8);
					if (record.getR38_share_capital() != null) {
						cell7.setCellValue(record.getR38_share_capital().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row38
					// Column I
					cell8 = row.createCell(9);
					if (record.getR38_accumulated_equity_interest() != null) {
						cell8.setCellValue(record.getR38_accumulated_equity_interest().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

}
