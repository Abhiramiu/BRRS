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

import com.bornfire.brrs.entities.BRRS_Q_BRANCHNET_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_BRANCHNET_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_BRANCHNET_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_BRANCHNET_Summary_Repo;
import com.bornfire.brrs.entities.Q_BRANCHNET_Archival_Detail_Entity;
import com.bornfire.brrs.entities.Q_BRANCHNET_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Q_BRANCHNET_Detail_Entity;
import com.bornfire.brrs.entities.Q_BRANCHNET_Summary_Entity;

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
	BRRS_Q_BRANCHNET_Summary_Repo Q_BRANCHNET_Summary_Repo;

	@Autowired
	BRRS_Q_BRANCHNET_Detail_Repo Q_BRANCHNET_Detail_Repo;

	@Autowired
	BRRS_Q_BRANCHNET_Archival_Summary_Repo Q_BRANCHNET_Archival_Summary_Repo;

	@Autowired
	BRRS_Q_BRANCHNET_Archival_Detail_Repo Q_BRANCHNET_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getQ_BRANCHNETView(String reportId, String fromdate, String todate, String currency,
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

				List<Q_BRANCHNET_Archival_Summary_Entity> T1Master = Q_BRANCHNET_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<Q_BRANCHNET_Archival_Summary_Entity> T1Master = Q_BRANCHNET_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<Q_BRANCHNET_Summary_Entity> T1Master = Q_BRANCHNET_Summary_Repo
						.getdatabydateList(dateformat.parse(todate));
				mv.addObject("displaymode", "summary");
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if (version != null) {
					List<Q_BRANCHNET_Archival_Detail_Entity> T1Master = Q_BRANCHNET_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<Q_BRANCHNET_Detail_Entity> T1Master = Q_BRANCHNET_Detail_Repo
							.getdatabydateList(dateformat.parse(todate));
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/Q_BRANCHNET");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	@Transactional
    public void QBranchnetUpdate1(Q_BRANCHNET_Summary_Entity updatedEntity) {

        System.out.println("Came to services 1");
        System.out.println("Report Date: " + updatedEntity.getReportDate());

        // 🔹 Fetch existing SUMMARY
        Q_BRANCHNET_Summary_Entity existingSummary = Q_BRANCHNET_Summary_Repo.findById(updatedEntity.getReportDate())
                .orElseThrow(() -> new RuntimeException(
                        "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

        // 🔹 Fetch or create DETAIL
        Q_BRANCHNET_Detail_Entity detailEntity = Q_BRANCHNET_Detail_Repo.findById(updatedEntity.getReportDate())
                .orElseGet(() -> {
                    Q_BRANCHNET_Detail_Entity d = new Q_BRANCHNET_Detail_Entity();
                    d.setReportDate(updatedEntity.getReportDate());
                    return d;
                });

        try {
            // 🔁 Loop R9 to R15
            for (int i = 10; i <= 20; i++) {
                String prefix = "R" + i + "_";
             String[] fields = { "bran_sub_bran_district", "no1_of_branches", "no1_of_sub_branches",
						"no1_of_agencies" };

                for (String field : fields) {

                    String getterName = "get" + prefix + field;
                    String setterName = "set" + prefix + field;

                    try {
                        // Getter from UPDATED entity
                        Method getter = Q_BRANCHNET_Summary_Entity.class.getMethod(getterName);

                        Object newValue = getter.invoke(updatedEntity);

                        // SUMMARY setter
                        Method summarySetter = Q_BRANCHNET_Summary_Entity.class.getMethod(
                                setterName, getter.getReturnType());

                        summarySetter.invoke(existingSummary, newValue);

                        // DETAIL setter
                        Method detailSetter = Q_BRANCHNET_Detail_Entity.class.getMethod(
                                setterName, getter.getReturnType());

                        detailSetter.invoke(detailEntity, newValue);

                    } catch (NoSuchMethodException e) {
                        // Field not present in entity – skip safely
                        continue;
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while updating report fields", e);
        }

        System.out.println("Saving Summary & Detail tables");

        // 💾 Save both tables
        Q_BRANCHNET_Summary_Repo.save(existingSummary);
        Q_BRANCHNET_Detail_Repo.save(detailEntity);

        System.out.println("Update completed successfully");
    }

    public void QBranchnetUpdate2(Q_BRANCHNET_Summary_Entity updatedEntity) {

        System.out.println("Came to services 1");
        System.out.println("Report Date: " + updatedEntity.getReportDate());

        // 🔹 Fetch existing SUMMARY
        Q_BRANCHNET_Summary_Entity existingSummary = Q_BRANCHNET_Summary_Repo.findById(updatedEntity.getReportDate())
                .orElseThrow(() -> new RuntimeException(
                        "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

        // 🔹 Fetch or create DETAIL
        Q_BRANCHNET_Detail_Entity detailEntity = Q_BRANCHNET_Detail_Repo.findById(updatedEntity.getReportDate())
                .orElseGet(() -> {
                    Q_BRANCHNET_Detail_Entity d = new Q_BRANCHNET_Detail_Entity();
                    d.setReportDate(updatedEntity.getReportDate());
                    return d;
                });

        try {
            // 🔁 Loop R9 to R15
            for (int i = 25; i <= 35; i++) {
                String prefix = "R" + i + "_";
             String[] fields = { "atm_mini_atm_district", "no_of_atms", "no_of_mini_atms", "encashment_points" };

                for (String field : fields) {

                    String getterName = "get" + prefix + field;
                    String setterName = "set" + prefix + field;

                    try {
                        // Getter from UPDATED entity
                        Method getter = Q_BRANCHNET_Summary_Entity.class.getMethod(getterName);

                        Object newValue = getter.invoke(updatedEntity);

                        // SUMMARY setter
                        Method summarySetter = Q_BRANCHNET_Summary_Entity.class.getMethod(
                                setterName, getter.getReturnType());

                        summarySetter.invoke(existingSummary, newValue);

                        // DETAIL setter
                        Method detailSetter = Q_BRANCHNET_Detail_Entity.class.getMethod(
                                setterName, getter.getReturnType());

                        detailSetter.invoke(detailEntity, newValue);

                    } catch (NoSuchMethodException e) {
                        // Field not present in entity – skip safely
                        continue;
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while updating report fields", e);
        }

        System.out.println("Saving Summary & Detail tables");

        // 💾 Save both tables
        Q_BRANCHNET_Summary_Repo.save(existingSummary);
        Q_BRANCHNET_Detail_Repo.save(detailEntity);

        System.out.println("Update completed successfully");
    }


    public void QBranchnetUpdate3(Q_BRANCHNET_Summary_Entity updatedEntity) {

        System.out.println("Came to services 1");
        System.out.println("Report Date: " + updatedEntity.getReportDate());

        // 🔹 Fetch existing SUMMARY
        Q_BRANCHNET_Summary_Entity existingSummary = Q_BRANCHNET_Summary_Repo.findById(updatedEntity.getReportDate())
                .orElseThrow(() -> new RuntimeException(
                        "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

        // 🔹 Fetch or create DETAIL
        Q_BRANCHNET_Detail_Entity detailEntity = Q_BRANCHNET_Detail_Repo.findById(updatedEntity.getReportDate())
                .orElseGet(() -> {
                    Q_BRANCHNET_Detail_Entity d = new Q_BRANCHNET_Detail_Entity();
                    d.setReportDate(updatedEntity.getReportDate());
                    return d;
                });

        try {
            // 🔁 Loop R9 to R15
            for (int i = 40; i <= 50; i++) {
                String prefix = "R" + i + "_";
             	String[] fields = { "debit_district", "opening_no_of_cards", "no_of_cards_issued", "no_cards_of_closed",
						"closing_bal_of_active_cards" };

                for (String field : fields) {

                    String getterName = "get" + prefix + field;
                    String setterName = "set" + prefix + field;

                    try {
                        // Getter from UPDATED entity
                        Method getter = Q_BRANCHNET_Summary_Entity.class.getMethod(getterName);

                        Object newValue = getter.invoke(updatedEntity);

                        // SUMMARY setter
                        Method summarySetter = Q_BRANCHNET_Summary_Entity.class.getMethod(
                                setterName, getter.getReturnType());

                        summarySetter.invoke(existingSummary, newValue);

                        // DETAIL setter
                        Method detailSetter = Q_BRANCHNET_Detail_Entity.class.getMethod(
                                setterName, getter.getReturnType());

                        detailSetter.invoke(detailEntity, newValue);

                    } catch (NoSuchMethodException e) {
                        // Field not present in entity – skip safely
                        continue;
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while updating report fields", e);
        }

        System.out.println("Saving Summary & Detail tables");

        // 💾 Save both tables
        Q_BRANCHNET_Summary_Repo.save(existingSummary);
        Q_BRANCHNET_Detail_Repo.save(detailEntity);

        System.out.println("Update completed successfully");
    }



    public void QBranchnetUpdate4(Q_BRANCHNET_Summary_Entity updatedEntity) {

        System.out.println("Came to services 1");
        System.out.println("Report Date: " + updatedEntity.getReportDate());

        // 🔹 Fetch existing SUMMARY
        Q_BRANCHNET_Summary_Entity existingSummary = Q_BRANCHNET_Summary_Repo.findById(updatedEntity.getReportDate())
                .orElseThrow(() -> new RuntimeException(
                        "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

        // 🔹 Fetch or create DETAIL
        Q_BRANCHNET_Detail_Entity detailEntity = Q_BRANCHNET_Detail_Repo.findById(updatedEntity.getReportDate())
                .orElseGet(() -> {
                    Q_BRANCHNET_Detail_Entity d = new Q_BRANCHNET_Detail_Entity();
                    d.setReportDate(updatedEntity.getReportDate());
                    return d;
                });

        try {
            // 🔁 Loop R9 to R15
            for (int i = 55; i <= 65; i++) {
                String prefix = "R" + i + "_";
             	String[] fields = { "credit_district", "opening_no_of_cards", "no_of_cards_issued",
						"no_cards_of_closed", "closing_bal_of_active_cards" };

                for (String field : fields) {

                    String getterName = "get" + prefix + field;
                    String setterName = "set" + prefix + field;

                    try {
                        // Getter from UPDATED entity
                        Method getter = Q_BRANCHNET_Summary_Entity.class.getMethod(getterName);

                        Object newValue = getter.invoke(updatedEntity);

                        // SUMMARY setter
                        Method summarySetter = Q_BRANCHNET_Summary_Entity.class.getMethod(
                                setterName, getter.getReturnType());

                        summarySetter.invoke(existingSummary, newValue);

                        // DETAIL setter
                        Method detailSetter = Q_BRANCHNET_Detail_Entity.class.getMethod(
                                setterName, getter.getReturnType());

                        detailSetter.invoke(detailEntity, newValue);

                    } catch (NoSuchMethodException e) {
                        // Field not present in entity – skip safely
                        continue;
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while updating report fields", e);
        }

        System.out.println("Saving Summary & Detail tables");

        // 💾 Save both tables
        Q_BRANCHNET_Summary_Repo.save(existingSummary);
        Q_BRANCHNET_Detail_Repo.save(detailEntity);

        System.out.println("Update completed successfully");
    }


	public byte[] BRRS_Q_BRANCHNETExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if (type.equals("ARCHIVAL") & version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelQ_BRANCHNETARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);

		}
		// Email check
		if ("email".equalsIgnoreCase(type) && version == null) {
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_Q_BRANCHNETEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		} else if ("email".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_Q_BRANCHNETEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);

		}
		List<Q_BRANCHNET_Summary_Entity> dataList = Q_BRANCHNET_Summary_Repo
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

					Q_BRANCHNET_Summary_Entity record = dataList.get(i);
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
					if (record.getR25_no_of_atms() != null) {
						R25cell1.setCellValue(record.getR25_no_of_atms().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col D
					Cell R25cell2 = row.createCell(3);
					if (record.getR25_no_of_mini_atms() != null) {
						R25cell2.setCellValue(record.getR25_no_of_mini_atms().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					// R25 Col E
					Cell R25cell3 = row.createCell(4);
					if (record.getR25_encashment_points() != null) {
						R25cell3.setCellValue(record.getR25_encashment_points().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}
					// R26 Col C
					row = sheet.getRow(25);
					Cell R26cell1 = row.createCell(2);
					if (record.getR26_no_of_atms() != null) {
						R26cell1.setCellValue(record.getR26_no_of_atms().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col D
					Cell R26cell2 = row.createCell(3);
					if (record.getR26_no_of_mini_atms() != null) {
						R26cell2.setCellValue(record.getR26_no_of_mini_atms().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					// R26 Col E
					Cell R26cell3 = row.createCell(4);
					if (record.getR26_encashment_points() != null) {
						R26cell3.setCellValue(record.getR26_encashment_points().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}
					// R27 Col C
					row = sheet.getRow(26);
					Cell R27cell1 = row.createCell(2);
					if (record.getR27_no_of_atms() != null) {
						R27cell1.setCellValue(record.getR27_no_of_atms().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col D
					Cell R27cell2 = row.createCell(3);
					if (record.getR27_no_of_mini_atms() != null) {
						R27cell2.setCellValue(record.getR27_no_of_mini_atms().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					// R27 Col E
					Cell R27cell3 = row.createCell(4);
					if (record.getR27_encashment_points() != null) {
						R27cell3.setCellValue(record.getR27_encashment_points().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}
					// R28 Col C
					row = sheet.getRow(27);
					Cell R28cell1 = row.createCell(2);
					if (record.getR28_no_of_atms() != null) {
						R28cell1.setCellValue(record.getR28_no_of_atms().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}

					// R28 Col D
					Cell R28cell2 = row.createCell(3);
					if (record.getR28_no_of_mini_atms() != null) {
						R28cell2.setCellValue(record.getR28_no_of_mini_atms().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}

					// R28 Col E
					Cell R28cell3 = row.createCell(4);
					if (record.getR28_encashment_points() != null) {
						R28cell3.setCellValue(record.getR28_encashment_points().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}
					// R29 Col C
					row = sheet.getRow(28);
					Cell R29cell1 = row.createCell(2);
					if (record.getR29_no_of_atms() != null) {
						R29cell1.setCellValue(record.getR29_no_of_atms().doubleValue());
						R29cell1.setCellStyle(numberStyle);
					} else {
						R29cell1.setCellValue("");
						R29cell1.setCellStyle(textStyle);
					}

					// R29 Col D
					Cell R29cell2 = row.createCell(3);
					if (record.getR29_no_of_mini_atms() != null) {
						R29cell2.setCellValue(record.getR29_no_of_mini_atms().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					// R29 Col E
					Cell R29cell3 = row.createCell(4);
					if (record.getR29_encashment_points() != null) {
						R29cell3.setCellValue(record.getR29_encashment_points().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}
					// R30 Col C
					row = sheet.getRow(29);
					Cell R30cell1 = row.createCell(2);
					if (record.getR30_no_of_atms() != null) {
						R30cell1.setCellValue(record.getR30_no_of_atms().doubleValue());
						R30cell1.setCellStyle(numberStyle);
					} else {
						R30cell1.setCellValue("");
						R30cell1.setCellStyle(textStyle);
					}

					// R30 Col D
					Cell R30cell2 = row.createCell(3);
					if (record.getR30_no_of_mini_atms() != null) {
						R30cell2.setCellValue(record.getR30_no_of_mini_atms().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					// R30 Col E
					Cell R30cell3 = row.createCell(4);
					if (record.getR30_encashment_points() != null) {
						R30cell3.setCellValue(record.getR30_encashment_points().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}
					// R31 Col C
					row = sheet.getRow(30);
					Cell R31cell1 = row.createCell(2);
					if (record.getR31_no_of_atms() != null) {
						R31cell1.setCellValue(record.getR31_no_of_atms().doubleValue());
						R31cell1.setCellStyle(numberStyle);
					} else {
						R31cell1.setCellValue("");
						R31cell1.setCellStyle(textStyle);
					}

					// R31 Col D
					Cell R31cell2 = row.createCell(3);
					if (record.getR31_no_of_mini_atms() != null) {
						R31cell2.setCellValue(record.getR31_no_of_mini_atms().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					// R31 Col E
					Cell R31cell3 = row.createCell(4);
					if (record.getR31_encashment_points() != null) {
						R31cell3.setCellValue(record.getR31_encashment_points().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// R32 Col C
					row = sheet.getRow(31);
					Cell R32cell1 = row.createCell(2);
					if (record.getR32_no_of_atms() != null) {
						R32cell1.setCellValue(record.getR32_no_of_atms().doubleValue());
						R32cell1.setCellStyle(numberStyle);
					} else {
						R32cell1.setCellValue("");
						R32cell1.setCellStyle(textStyle);
					}

					// R32 Col D
					Cell R32cell2 = row.createCell(3);
					if (record.getR32_no_of_mini_atms() != null) {
						R32cell2.setCellValue(record.getR32_no_of_mini_atms().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}

					// R32 Col E
					Cell R32cell3 = row.createCell(4);
					if (record.getR32_encashment_points() != null) {
						R32cell3.setCellValue(record.getR32_encashment_points().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}
					// R33 Col C
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(2);
					if (record.getR33_no_of_atms() != null) {
						R33cell1.setCellValue(record.getR33_no_of_atms().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col D
					Cell R33cell2 = row.createCell(3);
					if (record.getR33_no_of_mini_atms() != null) {
						R33cell2.setCellValue(record.getR33_no_of_mini_atms().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell3 = row.createCell(4);
					if (record.getR33_encashment_points() != null) {
						R33cell3.setCellValue(record.getR33_encashment_points().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}
					// R34 Col C
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(2);
					if (record.getR34_no_of_atms() != null) {
						R34cell1.setCellValue(record.getR34_no_of_atms().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col D
					Cell R34cell2 = row.createCell(3);
					if (record.getR34_no_of_mini_atms() != null) {
						R34cell2.setCellValue(record.getR34_no_of_mini_atms().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell3 = row.createCell(4);
					if (record.getR34_encashment_points() != null) {
						R34cell3.setCellValue(record.getR34_encashment_points().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// TABLE 3
					// R40 Col C
					row = sheet.getRow(39);
					Cell R40cell1 = row.createCell(2);
					if (record.getR40_opening_no_of_cards() != null) {
						R40cell1.setCellValue(record.getR40_opening_no_of_cards().doubleValue());
						R40cell1.setCellStyle(numberStyle);
					} else {
						R40cell1.setCellValue("");
						R40cell1.setCellStyle(textStyle);
					}

					// R40 Col D
					Cell R40cell2 = row.createCell(3);
					if (record.getR40_no_of_cards_issued() != null) {
						R40cell2.setCellValue(record.getR40_no_of_cards_issued().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					// R40 Col E
					Cell R40cell3 = row.createCell(4);
					if (record.getR40_no_cards_of_closed() != null) {
						R40cell3.setCellValue(record.getR40_no_cards_of_closed().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// R41 Col C
					row = sheet.getRow(40);
					Cell R41cell1 = row.createCell(2);
					if (record.getR41_opening_no_of_cards() != null) {
						R41cell1.setCellValue(record.getR41_opening_no_of_cards().doubleValue());
						R41cell1.setCellStyle(numberStyle);
					} else {
						R41cell1.setCellValue("");
						R41cell1.setCellStyle(textStyle);
					}

					// R41 Col D
					Cell R41cell2 = row.createCell(3);
					if (record.getR41_no_of_cards_issued() != null) {
						R41cell2.setCellValue(record.getR41_no_of_cards_issued().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					// R41 Col E
					Cell R41cell3 = row.createCell(4);
					if (record.getR41_no_cards_of_closed() != null) {
						R41cell3.setCellValue(record.getR41_no_cards_of_closed().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}

					// R42 Col C
					row = sheet.getRow(41);
					Cell R42cell1 = row.createCell(2);
					if (record.getR42_opening_no_of_cards() != null) {
						R42cell1.setCellValue(record.getR42_opening_no_of_cards().doubleValue());
						R42cell1.setCellStyle(numberStyle);
					} else {
						R42cell1.setCellValue("");
						R42cell1.setCellStyle(textStyle);
					}

					// R42 Col D
					Cell R42cell2 = row.createCell(3);
					if (record.getR42_no_of_cards_issued() != null) {
						R42cell2.setCellValue(record.getR42_no_of_cards_issued().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					// R42 Col E
					Cell R42cell3 = row.createCell(4);
					if (record.getR42_no_cards_of_closed() != null) {
						R42cell3.setCellValue(record.getR42_no_cards_of_closed().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}

					// R43 Col C
					row = sheet.getRow(42);
					Cell R43cell1 = row.createCell(2);
					if (record.getR43_opening_no_of_cards() != null) {
						R43cell1.setCellValue(record.getR43_opening_no_of_cards().doubleValue());
						R43cell1.setCellStyle(numberStyle);
					} else {
						R43cell1.setCellValue("");
						R43cell1.setCellStyle(textStyle);
					}

					// R43 Col D
					Cell R43cell2 = row.createCell(3);
					if (record.getR43_no_of_cards_issued() != null) {
						R43cell2.setCellValue(record.getR43_no_of_cards_issued().doubleValue());
						R43cell2.setCellStyle(numberStyle);
					} else {
						R43cell2.setCellValue("");
						R43cell2.setCellStyle(textStyle);
					}

					// R43 Col E
					Cell R43cell3 = row.createCell(4);
					if (record.getR43_no_cards_of_closed() != null) {
						R43cell3.setCellValue(record.getR43_no_cards_of_closed().doubleValue());
						R43cell3.setCellStyle(numberStyle);
					} else {
						R43cell3.setCellValue("");
						R43cell3.setCellStyle(textStyle);
					}
					// R44 Col C
					row = sheet.getRow(43);
					Cell R44cell1 = row.createCell(2);
					if (record.getR44_opening_no_of_cards() != null) {
						R44cell1.setCellValue(record.getR44_opening_no_of_cards().doubleValue());
						R44cell1.setCellStyle(numberStyle);
					} else {
						R44cell1.setCellValue("");
						R44cell1.setCellStyle(textStyle);
					}

					// R44 Col D
					Cell R44cell2 = row.createCell(3);
					if (record.getR44_no_of_cards_issued() != null) {
						R44cell2.setCellValue(record.getR44_no_of_cards_issued().doubleValue());
						R44cell2.setCellStyle(numberStyle);
					} else {
						R44cell2.setCellValue("");
						R44cell2.setCellStyle(textStyle);
					}

					// R44 Col E
					Cell R44cell3 = row.createCell(4);
					if (record.getR44_no_cards_of_closed() != null) {
						R44cell3.setCellValue(record.getR44_no_cards_of_closed().doubleValue());
						R44cell3.setCellStyle(numberStyle);
					} else {
						R44cell3.setCellValue("");
						R44cell3.setCellStyle(textStyle);
					}

					// R45 Col C
					row = sheet.getRow(44);
					Cell R45cell1 = row.createCell(2);
					if (record.getR45_opening_no_of_cards() != null) {
						R45cell1.setCellValue(record.getR45_opening_no_of_cards().doubleValue());
						R45cell1.setCellStyle(numberStyle);
					} else {
						R45cell1.setCellValue("");
						R45cell1.setCellStyle(textStyle);
					}

					// R45 Col D
					Cell R45cell2 = row.createCell(3);
					if (record.getR45_no_of_cards_issued() != null) {
						R45cell2.setCellValue(record.getR45_no_of_cards_issued().doubleValue());
						R45cell2.setCellStyle(numberStyle);
					} else {
						R45cell2.setCellValue("");
						R45cell2.setCellStyle(textStyle);
					}

					// R45 Col E
					Cell R45cell3 = row.createCell(4);
					if (record.getR45_no_cards_of_closed() != null) {
						R45cell3.setCellValue(record.getR45_no_cards_of_closed().doubleValue());
						R45cell3.setCellStyle(numberStyle);
					} else {
						R45cell3.setCellValue("");
						R45cell3.setCellStyle(textStyle);
					}

					// R46 Col C
					row = sheet.getRow(45);
					Cell R46cell1 = row.createCell(2);
					if (record.getR46_opening_no_of_cards() != null) {
						R46cell1.setCellValue(record.getR46_opening_no_of_cards().doubleValue());
						R46cell1.setCellStyle(numberStyle);
					} else {
						R46cell1.setCellValue("");
						R46cell1.setCellStyle(textStyle);
					}

					// R46 Col D
					Cell R46cell2 = row.createCell(3);
					if (record.getR46_no_of_cards_issued() != null) {
						R46cell2.setCellValue(record.getR46_no_of_cards_issued().doubleValue());
						R46cell2.setCellStyle(numberStyle);
					} else {
						R46cell2.setCellValue("");
						R46cell2.setCellStyle(textStyle);
					}

					// R46 Col E
					Cell R46cell3 = row.createCell(4);
					if (record.getR46_no_cards_of_closed() != null) {
						R46cell3.setCellValue(record.getR46_no_cards_of_closed().doubleValue());
						R46cell3.setCellStyle(numberStyle);
					} else {
						R46cell3.setCellValue("");
						R46cell3.setCellStyle(textStyle);
					}

					// R47 Col C
					row = sheet.getRow(46);
					Cell R47cell1 = row.createCell(2);
					if (record.getR47_opening_no_of_cards() != null) {
						R47cell1.setCellValue(record.getR47_opening_no_of_cards().doubleValue());
						R47cell1.setCellStyle(numberStyle);
					} else {
						R47cell1.setCellValue("");
						R47cell1.setCellStyle(textStyle);
					}

					// R47 Col D
					Cell R47cell2 = row.createCell(3);
					if (record.getR47_no_of_cards_issued() != null) {
						R47cell2.setCellValue(record.getR47_no_of_cards_issued().doubleValue());
						R47cell2.setCellStyle(numberStyle);
					} else {
						R47cell2.setCellValue("");
						R47cell2.setCellStyle(textStyle);
					}

					// R47 Col E
					Cell R47cell3 = row.createCell(4);
					if (record.getR47_no_cards_of_closed() != null) {
						R47cell3.setCellValue(record.getR47_no_cards_of_closed().doubleValue());
						R47cell3.setCellStyle(numberStyle);
					} else {
						R47cell3.setCellValue("");
						R47cell3.setCellStyle(textStyle);
					}

					// R48 Col C
					row = sheet.getRow(47);
					Cell R48cell1 = row.createCell(2);
					if (record.getR48_opening_no_of_cards() != null) {
						R48cell1.setCellValue(record.getR48_opening_no_of_cards().doubleValue());
						R48cell1.setCellStyle(numberStyle);
					} else {
						R48cell1.setCellValue("");
						R48cell1.setCellStyle(textStyle);
					}

					// R48 Col D
					Cell R48cell2 = row.createCell(3);
					if (record.getR48_no_of_cards_issued() != null) {
						R48cell2.setCellValue(record.getR48_no_of_cards_issued().doubleValue());
						R48cell2.setCellStyle(numberStyle);
					} else {
						R48cell2.setCellValue("");
						R48cell2.setCellStyle(textStyle);
					}

					// R48 Col E
					Cell R48cell3 = row.createCell(4);
					if (record.getR48_no_cards_of_closed() != null) {
						R48cell3.setCellValue(record.getR48_no_cards_of_closed().doubleValue());
						R48cell3.setCellStyle(numberStyle);
					} else {
						R48cell3.setCellValue("");
						R48cell3.setCellStyle(textStyle);
					}

					// R49 Col C
					row = sheet.getRow(48);
					Cell R49cell1 = row.createCell(2);
					if (record.getR49_opening_no_of_cards() != null) {
						R49cell1.setCellValue(record.getR49_opening_no_of_cards().doubleValue());
						R49cell1.setCellStyle(numberStyle);
					} else {
						R49cell1.setCellValue("");
						R49cell1.setCellStyle(textStyle);
					}

					// R49 Col D
					Cell R49cell2 = row.createCell(3);
					if (record.getR49_no_of_cards_issued() != null) {
						R49cell2.setCellValue(record.getR49_no_of_cards_issued().doubleValue());
						R49cell2.setCellStyle(numberStyle);
					} else {
						R49cell2.setCellValue("");
						R49cell2.setCellStyle(textStyle);
					}

					// R49 Col E
					Cell R49cell3 = row.createCell(4);
					if (record.getR49_no_cards_of_closed() != null) {
						R49cell3.setCellValue(record.getR49_no_cards_of_closed().doubleValue());
						R49cell3.setCellStyle(numberStyle);
					} else {
						R49cell3.setCellValue("");
						R49cell3.setCellStyle(textStyle);
					}

					// TABLE 4
					// R55 Col C
					row = sheet.getRow(54);
					Cell R55cell1 = row.createCell(2);
					if (record.getR55_opening_no_of_cards() != null) {
						R55cell1.setCellValue(record.getR55_opening_no_of_cards().doubleValue());
						R55cell1.setCellStyle(numberStyle);
					} else {
						R55cell1.setCellValue("");
						R55cell1.setCellStyle(textStyle);
					}

					// R55 Col D
					Cell R55cell2 = row.createCell(3);
					if (record.getR55_no_of_cards_issued() != null) {
						R55cell2.setCellValue(record.getR55_no_of_cards_issued().doubleValue());
						R55cell2.setCellStyle(numberStyle);
					} else {
						R55cell2.setCellValue("");
						R55cell2.setCellStyle(textStyle);
					}

					// R55 Col E
					Cell R55cell3 = row.createCell(4);
					if (record.getR55_no_cards_of_closed() != null) {
						R55cell3.setCellValue(record.getR55_no_cards_of_closed().doubleValue());
						R55cell3.setCellStyle(numberStyle);
					} else {
						R55cell3.setCellValue("");
						R55cell3.setCellStyle(textStyle);
					}

					// R56 Col C
					row = sheet.getRow(55);
					Cell R56cell1 = row.createCell(2);
					if (record.getR56_opening_no_of_cards() != null) {
						R56cell1.setCellValue(record.getR56_opening_no_of_cards().doubleValue());
						R56cell1.setCellStyle(numberStyle);
					} else {
						R56cell1.setCellValue("");
						R56cell1.setCellStyle(textStyle);
					}

					// R56 Col D
					Cell R56cell2 = row.createCell(3);
					if (record.getR56_no_of_cards_issued() != null) {
						R56cell2.setCellValue(record.getR56_no_of_cards_issued().doubleValue());
						R56cell2.setCellStyle(numberStyle);
					} else {
						R56cell2.setCellValue("");
						R56cell2.setCellStyle(textStyle);
					}

					// R56 Col E
					Cell R56cell3 = row.createCell(4);
					if (record.getR56_no_cards_of_closed() != null) {
						R56cell3.setCellValue(record.getR56_no_cards_of_closed().doubleValue());
						R56cell3.setCellStyle(numberStyle);
					} else {
						R56cell3.setCellValue("");
						R56cell3.setCellStyle(textStyle);
					}

					// R57 Col C
					row = sheet.getRow(56);
					Cell R57cell1 = row.createCell(2);
					if (record.getR57_opening_no_of_cards() != null) {
						R57cell1.setCellValue(record.getR57_opening_no_of_cards().doubleValue());
						R57cell1.setCellStyle(numberStyle);
					} else {
						R57cell1.setCellValue("");
						R57cell1.setCellStyle(textStyle);
					}

					// R57 Col D
					Cell R57cell2 = row.createCell(3);
					if (record.getR57_no_of_cards_issued() != null) {
						R57cell2.setCellValue(record.getR57_no_of_cards_issued().doubleValue());
						R57cell2.setCellStyle(numberStyle);
					} else {
						R57cell2.setCellValue("");
						R57cell2.setCellStyle(textStyle);
					}

					// R57 Col E
					Cell R57cell3 = row.createCell(4);
					if (record.getR57_no_cards_of_closed() != null) {
						R57cell3.setCellValue(record.getR57_no_cards_of_closed().doubleValue());
						R57cell3.setCellStyle(numberStyle);
					} else {
						R57cell3.setCellValue("");
						R57cell3.setCellStyle(textStyle);
					}

					// R58 Col C
					row = sheet.getRow(57);
					Cell R58cell1 = row.createCell(2);
					if (record.getR58_opening_no_of_cards() != null) {
						R58cell1.setCellValue(record.getR58_opening_no_of_cards().doubleValue());
						R58cell1.setCellStyle(numberStyle);
					} else {
						R58cell1.setCellValue("");
						R58cell1.setCellStyle(textStyle);
					}

					// R58 Col D
					Cell R58cell2 = row.createCell(3);
					if (record.getR58_no_of_cards_issued() != null) {
						R58cell2.setCellValue(record.getR58_no_of_cards_issued().doubleValue());
						R58cell2.setCellStyle(numberStyle);
					} else {
						R58cell2.setCellValue("");
						R58cell2.setCellStyle(textStyle);
					}

					// R58 Col E
					Cell R58cell3 = row.createCell(4);
					if (record.getR58_no_cards_of_closed() != null) {
						R58cell3.setCellValue(record.getR58_no_cards_of_closed().doubleValue());
						R58cell3.setCellStyle(numberStyle);
					} else {
						R58cell3.setCellValue("");
						R58cell3.setCellStyle(textStyle);
					}

					// R59 Col C
					row = sheet.getRow(58);
					Cell R59cell1 = row.createCell(2);
					if (record.getR59_opening_no_of_cards() != null) {
						R59cell1.setCellValue(record.getR59_opening_no_of_cards().doubleValue());
						R59cell1.setCellStyle(numberStyle);
					} else {
						R59cell1.setCellValue("");
						R59cell1.setCellStyle(textStyle);
					}

					// R59 Col D
					Cell R59cell2 = row.createCell(3);
					if (record.getR59_no_of_cards_issued() != null) {
						R59cell2.setCellValue(record.getR59_no_of_cards_issued().doubleValue());
						R59cell2.setCellStyle(numberStyle);
					} else {
						R59cell2.setCellValue("");
						R59cell2.setCellStyle(textStyle);
					}

					// R59 Col E
					Cell R59cell3 = row.createCell(4);
					if (record.getR59_no_cards_of_closed() != null) {
						R59cell3.setCellValue(record.getR59_no_cards_of_closed().doubleValue());
						R59cell3.setCellStyle(numberStyle);
					} else {
						R59cell3.setCellValue("");
						R59cell3.setCellStyle(textStyle);
					}

					// R60 Col C
					row = sheet.getRow(59);
					Cell R60cell1 = row.createCell(2);
					if (record.getR60_opening_no_of_cards() != null) {
						R60cell1.setCellValue(record.getR60_opening_no_of_cards().doubleValue());
						R60cell1.setCellStyle(numberStyle);
					} else {
						R60cell1.setCellValue("");
						R60cell1.setCellStyle(textStyle);
					}

					// R60 Col D
					Cell R60cell2 = row.createCell(3);
					if (record.getR60_no_of_cards_issued() != null) {
						R60cell2.setCellValue(record.getR60_no_of_cards_issued().doubleValue());
						R60cell2.setCellStyle(numberStyle);
					} else {
						R60cell2.setCellValue("");
						R60cell2.setCellStyle(textStyle);
					}
					// R60 Col E
					Cell R60cell3 = row.createCell(4);
					if (record.getR60_no_cards_of_closed() != null) {
						R60cell3.setCellValue(record.getR60_no_cards_of_closed().doubleValue());
						R60cell3.setCellStyle(numberStyle);
					} else {
						R60cell3.setCellValue("");
						R60cell3.setCellStyle(textStyle);
					}

					// R61 Col C
					row = sheet.getRow(60);
					Cell R61cell1 = row.createCell(2);
					if (record.getR61_opening_no_of_cards() != null) {
						R61cell1.setCellValue(record.getR61_opening_no_of_cards().doubleValue());
						R61cell1.setCellStyle(numberStyle);
					} else {
						R61cell1.setCellValue("");
						R61cell1.setCellStyle(textStyle);
					}

					// R61 Col D
					Cell R61cell2 = row.createCell(3);
					if (record.getR61_no_of_cards_issued() != null) {
						R61cell2.setCellValue(record.getR61_no_of_cards_issued().doubleValue());
						R61cell2.setCellStyle(numberStyle);
					} else {
						R61cell2.setCellValue("");
						R61cell2.setCellStyle(textStyle);
					}
					// R61 Col E
					Cell R61cell3 = row.createCell(4);
					if (record.getR61_no_cards_of_closed() != null) {
						R61cell3.setCellValue(record.getR61_no_cards_of_closed().doubleValue());
						R61cell3.setCellStyle(numberStyle);
					} else {
						R61cell3.setCellValue("");
						R61cell3.setCellStyle(textStyle);
					}

					// R62 Col C
					row = sheet.getRow(61);
					Cell R62cell1 = row.createCell(2);
					if (record.getR62_opening_no_of_cards() != null) {
						R62cell1.setCellValue(record.getR62_opening_no_of_cards().doubleValue());
						R62cell1.setCellStyle(numberStyle);
					} else {
						R62cell1.setCellValue("");
						R62cell1.setCellStyle(textStyle);
					}

					// R62 Col D
					Cell R62cell2 = row.createCell(3);
					if (record.getR62_no_of_cards_issued() != null) {
						R62cell2.setCellValue(record.getR62_no_of_cards_issued().doubleValue());
						R62cell2.setCellStyle(numberStyle);
					} else {
						R62cell2.setCellValue("");
						R62cell2.setCellStyle(textStyle);
					}
					// R62 Col E
					Cell R62cell3 = row.createCell(4);
					if (record.getR62_no_cards_of_closed() != null) {
						R62cell3.setCellValue(record.getR62_no_cards_of_closed().doubleValue());
						R62cell3.setCellStyle(numberStyle);
					} else {
						R62cell3.setCellValue("");
						R62cell3.setCellStyle(textStyle);
					}

					// R63 Col C
					row = sheet.getRow(62);
					Cell R63cell1 = row.createCell(2);
					if (record.getR63_opening_no_of_cards() != null) {
						R63cell1.setCellValue(record.getR63_opening_no_of_cards().doubleValue());
						R63cell1.setCellStyle(numberStyle);
					} else {
						R63cell1.setCellValue("");
						R63cell1.setCellStyle(textStyle);
					}

					// R63 Col D
					Cell R63cell2 = row.createCell(3);
					if (record.getR63_no_of_cards_issued() != null) {
						R63cell2.setCellValue(record.getR63_no_of_cards_issued().doubleValue());
						R63cell2.setCellStyle(numberStyle);
					} else {
						R63cell2.setCellValue("");
						R63cell2.setCellStyle(textStyle);
					}
					// R63 Col E
					Cell R63cell3 = row.createCell(4);
					if (record.getR63_no_cards_of_closed() != null) {
						R63cell3.setCellValue(record.getR63_no_cards_of_closed().doubleValue());
						R63cell3.setCellStyle(numberStyle);
					} else {
						R63cell3.setCellValue("");
						R63cell3.setCellStyle(textStyle);
					}

					// R64 Col C
					row = sheet.getRow(63);
					Cell R64cell1 = row.createCell(2);
					if (record.getR64_opening_no_of_cards() != null) {
						R64cell1.setCellValue(record.getR64_opening_no_of_cards().doubleValue());
						R64cell1.setCellStyle(numberStyle);
					} else {
						R64cell1.setCellValue("");
						R64cell1.setCellStyle(textStyle);
					}

					// R64 Col D
					Cell R64cell2 = row.createCell(3);
					if (record.getR64_no_of_cards_issued() != null) {
						R64cell2.setCellValue(record.getR64_no_of_cards_issued().doubleValue());
						R64cell2.setCellStyle(numberStyle);
					} else {
						R64cell2.setCellValue("");
						R64cell2.setCellStyle(textStyle);
					}
					// R64 Col E
					Cell R64cell3 = row.createCell(4);
					if (record.getR64_no_cards_of_closed() != null) {
						R64cell3.setCellValue(record.getR64_no_cards_of_closed().doubleValue());
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
		List<Q_BRANCHNET_Archival_Summary_Entity> dataList = Q_BRANCHNET_Archival_Summary_Repo
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

					Q_BRANCHNET_Archival_Summary_Entity record = dataList.get(i);
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
					if (record.getR25_no_of_atms() != null) {
						R25cell1.setCellValue(record.getR25_no_of_atms().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col D
					Cell R25cell2 = row.createCell(3);
					if (record.getR25_no_of_mini_atms() != null) {
						R25cell2.setCellValue(record.getR25_no_of_mini_atms().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					// R25 Col E
					Cell R25cell3 = row.createCell(4);
					if (record.getR25_encashment_points() != null) {
						R25cell3.setCellValue(record.getR25_encashment_points().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}
					// R26 Col C
					row = sheet.getRow(25);
					Cell R26cell1 = row.createCell(2);
					if (record.getR26_no_of_atms() != null) {
						R26cell1.setCellValue(record.getR26_no_of_atms().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col D
					Cell R26cell2 = row.createCell(3);
					if (record.getR26_no_of_mini_atms() != null) {
						R26cell2.setCellValue(record.getR26_no_of_mini_atms().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					// R26 Col E
					Cell R26cell3 = row.createCell(4);
					if (record.getR26_encashment_points() != null) {
						R26cell3.setCellValue(record.getR26_encashment_points().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}
					// R27 Col C
					row = sheet.getRow(26);
					Cell R27cell1 = row.createCell(2);
					if (record.getR27_no_of_atms() != null) {
						R27cell1.setCellValue(record.getR27_no_of_atms().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col D
					Cell R27cell2 = row.createCell(3);
					if (record.getR27_no_of_mini_atms() != null) {
						R27cell2.setCellValue(record.getR27_no_of_mini_atms().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					// R27 Col E
					Cell R27cell3 = row.createCell(4);
					if (record.getR27_encashment_points() != null) {
						R27cell3.setCellValue(record.getR27_encashment_points().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}
					// R28 Col C
					row = sheet.getRow(27);
					Cell R28cell1 = row.createCell(2);
					if (record.getR28_no_of_atms() != null) {
						R28cell1.setCellValue(record.getR28_no_of_atms().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}

					// R28 Col D
					Cell R28cell2 = row.createCell(3);
					if (record.getR28_no_of_mini_atms() != null) {
						R28cell2.setCellValue(record.getR28_no_of_mini_atms().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}

					// R28 Col E
					Cell R28cell3 = row.createCell(4);
					if (record.getR28_encashment_points() != null) {
						R28cell3.setCellValue(record.getR28_encashment_points().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}
					// R29 Col C
					row = sheet.getRow(28);
					Cell R29cell1 = row.createCell(2);
					if (record.getR29_no_of_atms() != null) {
						R29cell1.setCellValue(record.getR29_no_of_atms().doubleValue());
						R29cell1.setCellStyle(numberStyle);
					} else {
						R29cell1.setCellValue("");
						R29cell1.setCellStyle(textStyle);
					}

					// R29 Col D
					Cell R29cell2 = row.createCell(3);
					if (record.getR29_no_of_mini_atms() != null) {
						R29cell2.setCellValue(record.getR29_no_of_mini_atms().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					// R29 Col E
					Cell R29cell3 = row.createCell(4);
					if (record.getR29_encashment_points() != null) {
						R29cell3.setCellValue(record.getR29_encashment_points().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}
					// R30 Col C
					row = sheet.getRow(29);
					Cell R30cell1 = row.createCell(2);
					if (record.getR30_no_of_atms() != null) {
						R30cell1.setCellValue(record.getR30_no_of_atms().doubleValue());
						R30cell1.setCellStyle(numberStyle);
					} else {
						R30cell1.setCellValue("");
						R30cell1.setCellStyle(textStyle);
					}

					// R30 Col D
					Cell R30cell2 = row.createCell(3);
					if (record.getR30_no_of_mini_atms() != null) {
						R30cell2.setCellValue(record.getR30_no_of_mini_atms().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					// R30 Col E
					Cell R30cell3 = row.createCell(4);
					if (record.getR30_encashment_points() != null) {
						R30cell3.setCellValue(record.getR30_encashment_points().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}
					// R31 Col C
					row = sheet.getRow(30);
					Cell R31cell1 = row.createCell(2);
					if (record.getR31_no_of_atms() != null) {
						R31cell1.setCellValue(record.getR31_no_of_atms().doubleValue());
						R31cell1.setCellStyle(numberStyle);
					} else {
						R31cell1.setCellValue("");
						R31cell1.setCellStyle(textStyle);
					}

					// R31 Col D
					Cell R31cell2 = row.createCell(3);
					if (record.getR31_no_of_mini_atms() != null) {
						R31cell2.setCellValue(record.getR31_no_of_mini_atms().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					// R31 Col E
					Cell R31cell3 = row.createCell(4);
					if (record.getR31_encashment_points() != null) {
						R31cell3.setCellValue(record.getR31_encashment_points().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// R32 Col C
					row = sheet.getRow(31);
					Cell R32cell1 = row.createCell(2);
					if (record.getR32_no_of_atms() != null) {
						R32cell1.setCellValue(record.getR32_no_of_atms().doubleValue());
						R32cell1.setCellStyle(numberStyle);
					} else {
						R32cell1.setCellValue("");
						R32cell1.setCellStyle(textStyle);
					}

					// R32 Col D
					Cell R32cell2 = row.createCell(3);
					if (record.getR32_no_of_mini_atms() != null) {
						R32cell2.setCellValue(record.getR32_no_of_mini_atms().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}

					// R32 Col E
					Cell R32cell3 = row.createCell(4);
					if (record.getR32_encashment_points() != null) {
						R32cell3.setCellValue(record.getR32_encashment_points().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}
					// R33 Col C
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(2);
					if (record.getR33_no_of_atms() != null) {
						R33cell1.setCellValue(record.getR33_no_of_atms().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col D
					Cell R33cell2 = row.createCell(3);
					if (record.getR33_no_of_mini_atms() != null) {
						R33cell2.setCellValue(record.getR33_no_of_mini_atms().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell3 = row.createCell(4);
					if (record.getR33_encashment_points() != null) {
						R33cell3.setCellValue(record.getR33_encashment_points().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}
					// R34 Col C
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(2);
					if (record.getR34_no_of_atms() != null) {
						R34cell1.setCellValue(record.getR34_no_of_atms().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col D
					Cell R34cell2 = row.createCell(3);
					if (record.getR34_no_of_mini_atms() != null) {
						R34cell2.setCellValue(record.getR34_no_of_mini_atms().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell3 = row.createCell(4);
					if (record.getR34_encashment_points() != null) {
						R34cell3.setCellValue(record.getR34_encashment_points().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// TABLE 3
					// R40 Col C
					row = sheet.getRow(39);
					Cell R40cell1 = row.createCell(2);
					if (record.getR40_opening_no_of_cards() != null) {
						R40cell1.setCellValue(record.getR40_opening_no_of_cards().doubleValue());
						R40cell1.setCellStyle(numberStyle);
					} else {
						R40cell1.setCellValue("");
						R40cell1.setCellStyle(textStyle);
					}

					// R40 Col D
					Cell R40cell2 = row.createCell(3);
					if (record.getR40_no_of_cards_issued() != null) {
						R40cell2.setCellValue(record.getR40_no_of_cards_issued().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					// R40 Col E
					Cell R40cell3 = row.createCell(4);
					if (record.getR40_no_cards_of_closed() != null) {
						R40cell3.setCellValue(record.getR40_no_cards_of_closed().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// R41 Col C
					row = sheet.getRow(40);
					Cell R41cell1 = row.createCell(2);
					if (record.getR41_opening_no_of_cards() != null) {
						R41cell1.setCellValue(record.getR41_opening_no_of_cards().doubleValue());
						R41cell1.setCellStyle(numberStyle);
					} else {
						R41cell1.setCellValue("");
						R41cell1.setCellStyle(textStyle);
					}

					// R41 Col D
					Cell R41cell2 = row.createCell(3);
					if (record.getR41_no_of_cards_issued() != null) {
						R41cell2.setCellValue(record.getR41_no_of_cards_issued().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					// R41 Col E
					Cell R41cell3 = row.createCell(4);
					if (record.getR41_no_cards_of_closed() != null) {
						R41cell3.setCellValue(record.getR41_no_cards_of_closed().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}

					// R42 Col C
					row = sheet.getRow(41);
					Cell R42cell1 = row.createCell(2);
					if (record.getR42_opening_no_of_cards() != null) {
						R42cell1.setCellValue(record.getR42_opening_no_of_cards().doubleValue());
						R42cell1.setCellStyle(numberStyle);
					} else {
						R42cell1.setCellValue("");
						R42cell1.setCellStyle(textStyle);
					}

					// R42 Col D
					Cell R42cell2 = row.createCell(3);
					if (record.getR42_no_of_cards_issued() != null) {
						R42cell2.setCellValue(record.getR42_no_of_cards_issued().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					// R42 Col E
					Cell R42cell3 = row.createCell(4);
					if (record.getR42_no_cards_of_closed() != null) {
						R42cell3.setCellValue(record.getR42_no_cards_of_closed().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}

					// R43 Col C
					row = sheet.getRow(42);
					Cell R43cell1 = row.createCell(2);
					if (record.getR43_opening_no_of_cards() != null) {
						R43cell1.setCellValue(record.getR43_opening_no_of_cards().doubleValue());
						R43cell1.setCellStyle(numberStyle);
					} else {
						R43cell1.setCellValue("");
						R43cell1.setCellStyle(textStyle);
					}

					// R43 Col D
					Cell R43cell2 = row.createCell(3);
					if (record.getR43_no_of_cards_issued() != null) {
						R43cell2.setCellValue(record.getR43_no_of_cards_issued().doubleValue());
						R43cell2.setCellStyle(numberStyle);
					} else {
						R43cell2.setCellValue("");
						R43cell2.setCellStyle(textStyle);
					}

					// R43 Col E
					Cell R43cell3 = row.createCell(4);
					if (record.getR43_no_cards_of_closed() != null) {
						R43cell3.setCellValue(record.getR43_no_cards_of_closed().doubleValue());
						R43cell3.setCellStyle(numberStyle);
					} else {
						R43cell3.setCellValue("");
						R43cell3.setCellStyle(textStyle);
					}
					// R44 Col C
					row = sheet.getRow(43);
					Cell R44cell1 = row.createCell(2);
					if (record.getR44_opening_no_of_cards() != null) {
						R44cell1.setCellValue(record.getR44_opening_no_of_cards().doubleValue());
						R44cell1.setCellStyle(numberStyle);
					} else {
						R44cell1.setCellValue("");
						R44cell1.setCellStyle(textStyle);
					}

					// R44 Col D
					Cell R44cell2 = row.createCell(3);
					if (record.getR44_no_of_cards_issued() != null) {
						R44cell2.setCellValue(record.getR44_no_of_cards_issued().doubleValue());
						R44cell2.setCellStyle(numberStyle);
					} else {
						R44cell2.setCellValue("");
						R44cell2.setCellStyle(textStyle);
					}

					// R44 Col E
					Cell R44cell3 = row.createCell(4);
					if (record.getR44_no_cards_of_closed() != null) {
						R44cell3.setCellValue(record.getR44_no_cards_of_closed().doubleValue());
						R44cell3.setCellStyle(numberStyle);
					} else {
						R44cell3.setCellValue("");
						R44cell3.setCellStyle(textStyle);
					}

					// R45 Col C
					row = sheet.getRow(44);
					Cell R45cell1 = row.createCell(2);
					if (record.getR45_opening_no_of_cards() != null) {
						R45cell1.setCellValue(record.getR45_opening_no_of_cards().doubleValue());
						R45cell1.setCellStyle(numberStyle);
					} else {
						R45cell1.setCellValue("");
						R45cell1.setCellStyle(textStyle);
					}

					// R45 Col D
					Cell R45cell2 = row.createCell(3);
					if (record.getR45_no_of_cards_issued() != null) {
						R45cell2.setCellValue(record.getR45_no_of_cards_issued().doubleValue());
						R45cell2.setCellStyle(numberStyle);
					} else {
						R45cell2.setCellValue("");
						R45cell2.setCellStyle(textStyle);
					}

					// R45 Col E
					Cell R45cell3 = row.createCell(4);
					if (record.getR45_no_cards_of_closed() != null) {
						R45cell3.setCellValue(record.getR45_no_cards_of_closed().doubleValue());
						R45cell3.setCellStyle(numberStyle);
					} else {
						R45cell3.setCellValue("");
						R45cell3.setCellStyle(textStyle);
					}

					// R46 Col C
					row = sheet.getRow(45);
					Cell R46cell1 = row.createCell(2);
					if (record.getR46_opening_no_of_cards() != null) {
						R46cell1.setCellValue(record.getR46_opening_no_of_cards().doubleValue());
						R46cell1.setCellStyle(numberStyle);
					} else {
						R46cell1.setCellValue("");
						R46cell1.setCellStyle(textStyle);
					}

					// R46 Col D
					Cell R46cell2 = row.createCell(3);
					if (record.getR46_no_of_cards_issued() != null) {
						R46cell2.setCellValue(record.getR46_no_of_cards_issued().doubleValue());
						R46cell2.setCellStyle(numberStyle);
					} else {
						R46cell2.setCellValue("");
						R46cell2.setCellStyle(textStyle);
					}

					// R46 Col E
					Cell R46cell3 = row.createCell(4);
					if (record.getR46_no_cards_of_closed() != null) {
						R46cell3.setCellValue(record.getR46_no_cards_of_closed().doubleValue());
						R46cell3.setCellStyle(numberStyle);
					} else {
						R46cell3.setCellValue("");
						R46cell3.setCellStyle(textStyle);
					}

					// R47 Col C
					row = sheet.getRow(46);
					Cell R47cell1 = row.createCell(2);
					if (record.getR47_opening_no_of_cards() != null) {
						R47cell1.setCellValue(record.getR47_opening_no_of_cards().doubleValue());
						R47cell1.setCellStyle(numberStyle);
					} else {
						R47cell1.setCellValue("");
						R47cell1.setCellStyle(textStyle);
					}

					// R47 Col D
					Cell R47cell2 = row.createCell(3);
					if (record.getR47_no_of_cards_issued() != null) {
						R47cell2.setCellValue(record.getR47_no_of_cards_issued().doubleValue());
						R47cell2.setCellStyle(numberStyle);
					} else {
						R47cell2.setCellValue("");
						R47cell2.setCellStyle(textStyle);
					}

					// R47 Col E
					Cell R47cell3 = row.createCell(4);
					if (record.getR47_no_cards_of_closed() != null) {
						R47cell3.setCellValue(record.getR47_no_cards_of_closed().doubleValue());
						R47cell3.setCellStyle(numberStyle);
					} else {
						R47cell3.setCellValue("");
						R47cell3.setCellStyle(textStyle);
					}

					// R48 Col C
					row = sheet.getRow(47);
					Cell R48cell1 = row.createCell(2);
					if (record.getR48_opening_no_of_cards() != null) {
						R48cell1.setCellValue(record.getR48_opening_no_of_cards().doubleValue());
						R48cell1.setCellStyle(numberStyle);
					} else {
						R48cell1.setCellValue("");
						R48cell1.setCellStyle(textStyle);
					}

					// R48 Col D
					Cell R48cell2 = row.createCell(3);
					if (record.getR48_no_of_cards_issued() != null) {
						R48cell2.setCellValue(record.getR48_no_of_cards_issued().doubleValue());
						R48cell2.setCellStyle(numberStyle);
					} else {
						R48cell2.setCellValue("");
						R48cell2.setCellStyle(textStyle);
					}

					// R48 Col E
					Cell R48cell3 = row.createCell(4);
					if (record.getR48_no_cards_of_closed() != null) {
						R48cell3.setCellValue(record.getR48_no_cards_of_closed().doubleValue());
						R48cell3.setCellStyle(numberStyle);
					} else {
						R48cell3.setCellValue("");
						R48cell3.setCellStyle(textStyle);
					}

					// R49 Col C
					row = sheet.getRow(48);
					Cell R49cell1 = row.createCell(2);
					if (record.getR49_opening_no_of_cards() != null) {
						R49cell1.setCellValue(record.getR49_opening_no_of_cards().doubleValue());
						R49cell1.setCellStyle(numberStyle);
					} else {
						R49cell1.setCellValue("");
						R49cell1.setCellStyle(textStyle);
					}

					// R49 Col D
					Cell R49cell2 = row.createCell(3);
					if (record.getR49_no_of_cards_issued() != null) {
						R49cell2.setCellValue(record.getR49_no_of_cards_issued().doubleValue());
						R49cell2.setCellStyle(numberStyle);
					} else {
						R49cell2.setCellValue("");
						R49cell2.setCellStyle(textStyle);
					}

					// R49 Col E
					Cell R49cell3 = row.createCell(4);
					if (record.getR49_no_cards_of_closed() != null) {
						R49cell3.setCellValue(record.getR49_no_cards_of_closed().doubleValue());
						R49cell3.setCellStyle(numberStyle);
					} else {
						R49cell3.setCellValue("");
						R49cell3.setCellStyle(textStyle);
					}

					// TABLE 4
					// R55 Col C
					row = sheet.getRow(54);
					Cell R55cell1 = row.createCell(2);
					if (record.getR55_opening_no_of_cards() != null) {
						R55cell1.setCellValue(record.getR55_opening_no_of_cards().doubleValue());
						R55cell1.setCellStyle(numberStyle);
					} else {
						R55cell1.setCellValue("");
						R55cell1.setCellStyle(textStyle);
					}

					// R55 Col D
					Cell R55cell2 = row.createCell(3);
					if (record.getR55_no_of_cards_issued() != null) {
						R55cell2.setCellValue(record.getR55_no_of_cards_issued().doubleValue());
						R55cell2.setCellStyle(numberStyle);
					} else {
						R55cell2.setCellValue("");
						R55cell2.setCellStyle(textStyle);
					}

					// R55 Col E
					Cell R55cell3 = row.createCell(4);
					if (record.getR55_no_cards_of_closed() != null) {
						R55cell3.setCellValue(record.getR55_no_cards_of_closed().doubleValue());
						R55cell3.setCellStyle(numberStyle);
					} else {
						R55cell3.setCellValue("");
						R55cell3.setCellStyle(textStyle);
					}

					// R56 Col C
					row = sheet.getRow(55);
					Cell R56cell1 = row.createCell(2);
					if (record.getR56_opening_no_of_cards() != null) {
						R56cell1.setCellValue(record.getR56_opening_no_of_cards().doubleValue());
						R56cell1.setCellStyle(numberStyle);
					} else {
						R56cell1.setCellValue("");
						R56cell1.setCellStyle(textStyle);
					}

					// R56 Col D
					Cell R56cell2 = row.createCell(3);
					if (record.getR56_no_of_cards_issued() != null) {
						R56cell2.setCellValue(record.getR56_no_of_cards_issued().doubleValue());
						R56cell2.setCellStyle(numberStyle);
					} else {
						R56cell2.setCellValue("");
						R56cell2.setCellStyle(textStyle);
					}

					// R56 Col E
					Cell R56cell3 = row.createCell(4);
					if (record.getR56_no_cards_of_closed() != null) {
						R56cell3.setCellValue(record.getR56_no_cards_of_closed().doubleValue());
						R56cell3.setCellStyle(numberStyle);
					} else {
						R56cell3.setCellValue("");
						R56cell3.setCellStyle(textStyle);
					}

					// R57 Col C
					row = sheet.getRow(56);
					Cell R57cell1 = row.createCell(2);
					if (record.getR57_opening_no_of_cards() != null) {
						R57cell1.setCellValue(record.getR57_opening_no_of_cards().doubleValue());
						R57cell1.setCellStyle(numberStyle);
					} else {
						R57cell1.setCellValue("");
						R57cell1.setCellStyle(textStyle);
					}

					// R57 Col D
					Cell R57cell2 = row.createCell(3);
					if (record.getR57_no_of_cards_issued() != null) {
						R57cell2.setCellValue(record.getR57_no_of_cards_issued().doubleValue());
						R57cell2.setCellStyle(numberStyle);
					} else {
						R57cell2.setCellValue("");
						R57cell2.setCellStyle(textStyle);
					}

					// R57 Col E
					Cell R57cell3 = row.createCell(4);
					if (record.getR57_no_cards_of_closed() != null) {
						R57cell3.setCellValue(record.getR57_no_cards_of_closed().doubleValue());
						R57cell3.setCellStyle(numberStyle);
					} else {
						R57cell3.setCellValue("");
						R57cell3.setCellStyle(textStyle);
					}

					// R58 Col C
					row = sheet.getRow(57);
					Cell R58cell1 = row.createCell(2);
					if (record.getR58_opening_no_of_cards() != null) {
						R58cell1.setCellValue(record.getR58_opening_no_of_cards().doubleValue());
						R58cell1.setCellStyle(numberStyle);
					} else {
						R58cell1.setCellValue("");
						R58cell1.setCellStyle(textStyle);
					}

					// R58 Col D
					Cell R58cell2 = row.createCell(3);
					if (record.getR58_no_of_cards_issued() != null) {
						R58cell2.setCellValue(record.getR58_no_of_cards_issued().doubleValue());
						R58cell2.setCellStyle(numberStyle);
					} else {
						R58cell2.setCellValue("");
						R58cell2.setCellStyle(textStyle);
					}

					// R58 Col E
					Cell R58cell3 = row.createCell(4);
					if (record.getR58_no_cards_of_closed() != null) {
						R58cell3.setCellValue(record.getR58_no_cards_of_closed().doubleValue());
						R58cell3.setCellStyle(numberStyle);
					} else {
						R58cell3.setCellValue("");
						R58cell3.setCellStyle(textStyle);
					}

					// R59 Col C
					row = sheet.getRow(58);
					Cell R59cell1 = row.createCell(2);
					if (record.getR59_opening_no_of_cards() != null) {
						R59cell1.setCellValue(record.getR59_opening_no_of_cards().doubleValue());
						R59cell1.setCellStyle(numberStyle);
					} else {
						R59cell1.setCellValue("");
						R59cell1.setCellStyle(textStyle);
					}

					// R59 Col D
					Cell R59cell2 = row.createCell(3);
					if (record.getR59_no_of_cards_issued() != null) {
						R59cell2.setCellValue(record.getR59_no_of_cards_issued().doubleValue());
						R59cell2.setCellStyle(numberStyle);
					} else {
						R59cell2.setCellValue("");
						R59cell2.setCellStyle(textStyle);
					}

					// R59 Col E
					Cell R59cell3 = row.createCell(4);
					if (record.getR59_no_cards_of_closed() != null) {
						R59cell3.setCellValue(record.getR59_no_cards_of_closed().doubleValue());
						R59cell3.setCellStyle(numberStyle);
					} else {
						R59cell3.setCellValue("");
						R59cell3.setCellStyle(textStyle);
					}

					// R60 Col C
					row = sheet.getRow(59);
					Cell R60cell1 = row.createCell(2);
					if (record.getR60_opening_no_of_cards() != null) {
						R60cell1.setCellValue(record.getR60_opening_no_of_cards().doubleValue());
						R60cell1.setCellStyle(numberStyle);
					} else {
						R60cell1.setCellValue("");
						R60cell1.setCellStyle(textStyle);
					}

					// R60 Col D
					Cell R60cell2 = row.createCell(3);
					if (record.getR60_no_of_cards_issued() != null) {
						R60cell2.setCellValue(record.getR60_no_of_cards_issued().doubleValue());
						R60cell2.setCellStyle(numberStyle);
					} else {
						R60cell2.setCellValue("");
						R60cell2.setCellStyle(textStyle);
					}
					// R60 Col E
					Cell R60cell3 = row.createCell(4);
					if (record.getR60_no_cards_of_closed() != null) {
						R60cell3.setCellValue(record.getR60_no_cards_of_closed().doubleValue());
						R60cell3.setCellStyle(numberStyle);
					} else {
						R60cell3.setCellValue("");
						R60cell3.setCellStyle(textStyle);
					}

					// R61 Col C
					row = sheet.getRow(60);
					Cell R61cell1 = row.createCell(2);
					if (record.getR61_opening_no_of_cards() != null) {
						R61cell1.setCellValue(record.getR61_opening_no_of_cards().doubleValue());
						R61cell1.setCellStyle(numberStyle);
					} else {
						R61cell1.setCellValue("");
						R61cell1.setCellStyle(textStyle);
					}

					// R61 Col D
					Cell R61cell2 = row.createCell(3);
					if (record.getR61_no_of_cards_issued() != null) {
						R61cell2.setCellValue(record.getR61_no_of_cards_issued().doubleValue());
						R61cell2.setCellStyle(numberStyle);
					} else {
						R61cell2.setCellValue("");
						R61cell2.setCellStyle(textStyle);
					}
					// R61 Col E
					Cell R61cell3 = row.createCell(4);
					if (record.getR61_no_cards_of_closed() != null) {
						R61cell3.setCellValue(record.getR61_no_cards_of_closed().doubleValue());
						R61cell3.setCellStyle(numberStyle);
					} else {
						R61cell3.setCellValue("");
						R61cell3.setCellStyle(textStyle);
					}

					// R62 Col C
					row = sheet.getRow(61);
					Cell R62cell1 = row.createCell(2);
					if (record.getR62_opening_no_of_cards() != null) {
						R62cell1.setCellValue(record.getR62_opening_no_of_cards().doubleValue());
						R62cell1.setCellStyle(numberStyle);
					} else {
						R62cell1.setCellValue("");
						R62cell1.setCellStyle(textStyle);
					}

					// R62 Col D
					Cell R62cell2 = row.createCell(3);
					if (record.getR62_no_of_cards_issued() != null) {
						R62cell2.setCellValue(record.getR62_no_of_cards_issued().doubleValue());
						R62cell2.setCellStyle(numberStyle);
					} else {
						R62cell2.setCellValue("");
						R62cell2.setCellStyle(textStyle);
					}
					// R62 Col E
					Cell R62cell3 = row.createCell(4);
					if (record.getR62_no_cards_of_closed() != null) {
						R62cell3.setCellValue(record.getR62_no_cards_of_closed().doubleValue());
						R62cell3.setCellStyle(numberStyle);
					} else {
						R62cell3.setCellValue("");
						R62cell3.setCellStyle(textStyle);
					}

					// R63 Col C
					row = sheet.getRow(62);
					Cell R63cell1 = row.createCell(2);
					if (record.getR63_opening_no_of_cards() != null) {
						R63cell1.setCellValue(record.getR63_opening_no_of_cards().doubleValue());
						R63cell1.setCellStyle(numberStyle);
					} else {
						R63cell1.setCellValue("");
						R63cell1.setCellStyle(textStyle);
					}

					// R63 Col D
					Cell R63cell2 = row.createCell(3);
					if (record.getR63_no_of_cards_issued() != null) {
						R63cell2.setCellValue(record.getR63_no_of_cards_issued().doubleValue());
						R63cell2.setCellStyle(numberStyle);
					} else {
						R63cell2.setCellValue("");
						R63cell2.setCellStyle(textStyle);
					}
					// R63 Col E
					Cell R63cell3 = row.createCell(4);
					if (record.getR63_no_cards_of_closed() != null) {
						R63cell3.setCellValue(record.getR63_no_cards_of_closed().doubleValue());
						R63cell3.setCellStyle(numberStyle);
					} else {
						R63cell3.setCellValue("");
						R63cell3.setCellStyle(textStyle);
					}

					// R64 Col C
					row = sheet.getRow(63);
					Cell R64cell1 = row.createCell(2);
					if (record.getR64_opening_no_of_cards() != null) {
						R64cell1.setCellValue(record.getR64_opening_no_of_cards().doubleValue());
						R64cell1.setCellStyle(numberStyle);
					} else {
						R64cell1.setCellValue("");
						R64cell1.setCellStyle(textStyle);
					}

					// R64 Col D
					Cell R64cell2 = row.createCell(3);
					if (record.getR64_no_of_cards_issued() != null) {
						R64cell2.setCellValue(record.getR64_no_of_cards_issued().doubleValue());
						R64cell2.setCellStyle(numberStyle);
					} else {
						R64cell2.setCellValue("");
						R64cell2.setCellStyle(textStyle);
					}
					// R64 Col E
					Cell R64cell3 = row.createCell(4);
					if (record.getR64_no_cards_of_closed() != null) {
						R64cell3.setCellValue(record.getR64_no_cards_of_closed().doubleValue());
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
			List<Q_BRANCHNET_Archival_Summary_Entity> latestArchivalList = Q_BRANCHNET_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (Q_BRANCHNET_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion() });
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
			List<Q_BRANCHNET_Archival_Summary_Entity> latestArchivalList = Q_BRANCHNET_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (Q_BRANCHNET_Archival_Summary_Entity entity : latestArchivalList) {
					archivalList.add(new Object[] { entity.getReportDate(), entity.getReportVersion() });
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

	// Email
	public byte[] BRRS_Q_BRANCHNETEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		Date reportDate = dateformat.parse(todate);
		if (type.equals("email") & version != null) {

		}
		List<Q_BRANCHNET_Summary_Entity> dataList = Q_BRANCHNET_Summary_Repo.getdatabydateList(reportDate);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found forQ_BRANCHNET report. Returning empty result.");
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
					Q_BRANCHNET_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R10 Col E

					Cell R10cell1 = row.createCell(4);
					if (record.getR10_no1_of_branches() != null) {
						R10cell1.setCellValue(record.getR10_no1_of_branches().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col F
					Cell R10cell2 = row.createCell(5);
					if (record.getR10_no1_of_sub_branches() != null) {
						R10cell2.setCellValue(record.getR10_no1_of_sub_branches().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}

					// R10 Col G
					Cell R10cell3 = row.createCell(6);
					if (record.getR10_no1_of_agencies() != null) {
						R10cell3.setCellValue(record.getR10_no1_of_agencies().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}
					// R11 Col E
					row = sheet.getRow(10);
					Cell R11cell1 = row.createCell(4);
					if (record.getR11_no1_of_branches() != null) {
						R11cell1.setCellValue(record.getR11_no1_of_branches().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col F
					Cell R11cell2 = row.createCell(5);
					if (record.getR11_no1_of_sub_branches() != null) {
						R11cell2.setCellValue(record.getR11_no1_of_sub_branches().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 Col G
					Cell R11cell3 = row.createCell(6);
					if (record.getR11_no1_of_agencies() != null) {
						R11cell3.setCellValue(record.getR11_no1_of_agencies().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}
					// R12 Col E
					row = sheet.getRow(11);
					Cell R12cell1 = row.createCell(4);
					if (record.getR12_no1_of_branches() != null) {
						R12cell1.setCellValue(record.getR12_no1_of_branches().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col F
					Cell R12cell2 = row.createCell(5);
					if (record.getR12_no1_of_sub_branches() != null) {
						R12cell2.setCellValue(record.getR12_no1_of_sub_branches().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					// R12 Col G
					Cell R12cell3 = row.createCell(6);
					if (record.getR12_no1_of_agencies() != null) {
						R12cell3.setCellValue(record.getR12_no1_of_agencies().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}
					// R13 Col E
					row = sheet.getRow(12);
					Cell R13cell1 = row.createCell(4);
					if (record.getR13_no1_of_branches() != null) {
						R13cell1.setCellValue(record.getR13_no1_of_branches().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col F
					Cell R13cell2 = row.createCell(5);
					if (record.getR13_no1_of_sub_branches() != null) {
						R13cell2.setCellValue(record.getR13_no1_of_sub_branches().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					// R13 Col G
					Cell R13cell3 = row.createCell(6);
					if (record.getR13_no1_of_agencies() != null) {
						R13cell3.setCellValue(record.getR13_no1_of_agencies().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}
					// R14 Col E
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(4);
					if (record.getR14_no1_of_branches() != null) {
						R14cell1.setCellValue(record.getR14_no1_of_branches().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col F
					Cell R14cell2 = row.createCell(5);
					if (record.getR14_no1_of_sub_branches() != null) {
						R14cell2.setCellValue(record.getR14_no1_of_sub_branches().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// R14 Col G
					Cell R14cell3 = row.createCell(6);
					if (record.getR14_no1_of_agencies() != null) {
						R14cell3.setCellValue(record.getR14_no1_of_agencies().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}
					// R15 Col E
					row = sheet.getRow(14);
					Cell R15cell1 = row.createCell(4);
					if (record.getR15_no1_of_branches() != null) {
						R15cell1.setCellValue(record.getR15_no1_of_branches().doubleValue());
						R15cell1.setCellStyle(numberStyle);
					} else {
						R15cell1.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

					// R15 Col F
					Cell R15cell2 = row.createCell(5);
					if (record.getR15_no1_of_sub_branches() != null) {
						R15cell2.setCellValue(record.getR15_no1_of_sub_branches().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}

					// R15 Col G
					Cell R15cell3 = row.createCell(6);
					if (record.getR15_no1_of_agencies() != null) {
						R15cell3.setCellValue(record.getR15_no1_of_agencies().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}
					// R16 Col E
					row = sheet.getRow(15);
					Cell R16cell1 = row.createCell(4);
					if (record.getR16_no1_of_branches() != null) {
						R16cell1.setCellValue(record.getR16_no1_of_branches().doubleValue());
						R16cell1.setCellStyle(numberStyle);
					} else {
						R16cell1.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

					// R16 Col F
					Cell R16cell2 = row.createCell(5);
					if (record.getR16_no1_of_sub_branches() != null) {
						R16cell2.setCellValue(record.getR16_no1_of_sub_branches().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}

					// R16 Col G
					Cell R16cell3 = row.createCell(6);
					if (record.getR16_no1_of_agencies() != null) {
						R16cell3.setCellValue(record.getR16_no1_of_agencies().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}
					// R17 Col E
					row = sheet.getRow(16);
					Cell R17cell1 = row.createCell(4);
					if (record.getR17_no1_of_branches() != null) {
						R17cell1.setCellValue(record.getR17_no1_of_branches().doubleValue());
						R17cell1.setCellStyle(numberStyle);
					} else {
						R17cell1.setCellValue("");
						R17cell1.setCellStyle(textStyle);
					}

					// R17 Col F
					Cell R17cell2 = row.createCell(5);
					if (record.getR17_no1_of_sub_branches() != null) {
						R17cell2.setCellValue(record.getR17_no1_of_sub_branches().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// R17 Col G
					Cell R17cell3 = row.createCell(6);
					if (record.getR17_no1_of_agencies() != null) {
						R17cell3.setCellValue(record.getR17_no1_of_agencies().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}
					// R18 Col E
					row = sheet.getRow(17);
					Cell R18cell1 = row.createCell(4);
					if (record.getR18_no1_of_branches() != null) {
						R18cell1.setCellValue(record.getR18_no1_of_branches().doubleValue());
						R18cell1.setCellStyle(numberStyle);
					} else {
						R18cell1.setCellValue("");
						R18cell1.setCellStyle(textStyle);
					}

					// R18 Col F
					Cell R18cell2 = row.createCell(5);
					if (record.getR18_no1_of_sub_branches() != null) {
						R18cell2.setCellValue(record.getR18_no1_of_sub_branches().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					// R18 Col G
					Cell R18cell3 = row.createCell(6);
					if (record.getR18_no1_of_agencies() != null) {
						R18cell3.setCellValue(record.getR18_no1_of_agencies().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// R19 Col E
					row = sheet.getRow(18);
					Cell R19cell1 = row.createCell(4);
					if (record.getR19_no1_of_branches() != null) {
						R19cell1.setCellValue(record.getR19_no1_of_branches().doubleValue());
						R19cell1.setCellStyle(numberStyle);
					} else {
						R19cell1.setCellValue("");
						R19cell1.setCellStyle(textStyle);
					}

					// R19 Col F
					Cell R19cell2 = row.createCell(5);
					if (record.getR19_no1_of_sub_branches() != null) {
						R19cell2.setCellValue(record.getR19_no1_of_sub_branches().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					// R19 Col G
					Cell R19cell3 = row.createCell(6);
					if (record.getR19_no1_of_agencies() != null) {
						R19cell3.setCellValue(record.getR19_no1_of_agencies().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}
					// R20 Col E
					row = sheet.getRow(19);
					Cell R20cell1 = row.createCell(4);
					if (record.getR20_no1_of_branches() != null) {
						R20cell1.setCellValue(record.getR20_no1_of_branches().doubleValue());
						R20cell1.setCellStyle(numberStyle);
					} else {
						R20cell1.setCellValue("");
						R20cell1.setCellStyle(textStyle);
					}

					// R20 Col F
					Cell R20cell2 = row.createCell(5);
					if (record.getR20_no1_of_sub_branches() != null) {
						R20cell2.setCellValue(record.getR20_no1_of_sub_branches().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}

					// R20 Col G
					Cell R20cell3 = row.createCell(6);
					if (record.getR20_no1_of_agencies() != null) {
						R20cell3.setCellValue(record.getR20_no1_of_agencies().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}
					// TABLE 2
					// R25 Col E
					row = sheet.getRow(24);
					Cell R25cell1 = row.createCell(4);
					if (record.getR25_no_of_atms() != null) {
						R25cell1.setCellValue(record.getR25_no_of_atms().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col F
					Cell R25cell2 = row.createCell(5);
					if (record.getR25_no_of_mini_atms() != null) {
						R25cell2.setCellValue(record.getR25_no_of_mini_atms().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					// R25 Col G
					Cell R25cell3 = row.createCell(6);
					if (record.getR25_encashment_points() != null) {
						R25cell3.setCellValue(record.getR25_encashment_points().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}
					// R26 Col E
					row = sheet.getRow(25);
					Cell R26cell1 = row.createCell(4);
					if (record.getR26_no_of_atms() != null) {
						R26cell1.setCellValue(record.getR26_no_of_atms().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col F
					Cell R26cell2 = row.createCell(5);
					if (record.getR26_no_of_mini_atms() != null) {
						R26cell2.setCellValue(record.getR26_no_of_mini_atms().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					// R26 Col G
					Cell R26cell3 = row.createCell(6);
					if (record.getR26_encashment_points() != null) {
						R26cell3.setCellValue(record.getR26_encashment_points().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}
					// R27 Col E
					row = sheet.getRow(26);
					Cell R27cell1 = row.createCell(4);
					if (record.getR27_no_of_atms() != null) {
						R27cell1.setCellValue(record.getR27_no_of_atms().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col F
					Cell R27cell2 = row.createCell(5);
					if (record.getR27_no_of_mini_atms() != null) {
						R27cell2.setCellValue(record.getR27_no_of_mini_atms().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					// R27 Col G
					Cell R27cell3 = row.createCell(6);
					if (record.getR27_encashment_points() != null) {
						R27cell3.setCellValue(record.getR27_encashment_points().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}
					// R28 Col E
					row = sheet.getRow(27);
					Cell R28cell1 = row.createCell(4);
					if (record.getR28_no_of_atms() != null) {
						R28cell1.setCellValue(record.getR28_no_of_atms().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}

					// R28 Col F
					Cell R28cell2 = row.createCell(5);
					if (record.getR28_no_of_mini_atms() != null) {
						R28cell2.setCellValue(record.getR28_no_of_mini_atms().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}

					// R28 Col G
					Cell R28cell3 = row.createCell(6);
					if (record.getR28_encashment_points() != null) {
						R28cell3.setCellValue(record.getR28_encashment_points().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}
					// R29 Col E
					row = sheet.getRow(28);
					Cell R29cell1 = row.createCell(4);
					if (record.getR29_no_of_atms() != null) {
						R29cell1.setCellValue(record.getR29_no_of_atms().doubleValue());
						R29cell1.setCellStyle(numberStyle);
					} else {
						R29cell1.setCellValue("");
						R29cell1.setCellStyle(textStyle);
					}

					// R29 Col F
					Cell R29cell2 = row.createCell(5);
					if (record.getR29_no_of_mini_atms() != null) {
						R29cell2.setCellValue(record.getR29_no_of_mini_atms().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					// R29 Col G
					Cell R29cell3 = row.createCell(6);
					if (record.getR29_encashment_points() != null) {
						R29cell3.setCellValue(record.getR29_encashment_points().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}
					// R30 Col E
					row = sheet.getRow(29);
					Cell R30cell1 = row.createCell(4);
					if (record.getR30_no_of_atms() != null) {
						R30cell1.setCellValue(record.getR30_no_of_atms().doubleValue());
						R30cell1.setCellStyle(numberStyle);
					} else {
						R30cell1.setCellValue("");
						R30cell1.setCellStyle(textStyle);
					}

					// R30 Col F
					Cell R30cell2 = row.createCell(5);
					if (record.getR30_no_of_mini_atms() != null) {
						R30cell2.setCellValue(record.getR30_no_of_mini_atms().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					// R30 Col G
					Cell R30cell3 = row.createCell(6);
					if (record.getR30_encashment_points() != null) {
						R30cell3.setCellValue(record.getR30_encashment_points().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}
					// R31 Col E
					row = sheet.getRow(30);
					Cell R31cell1 = row.createCell(4);
					if (record.getR31_no_of_atms() != null) {
						R31cell1.setCellValue(record.getR31_no_of_atms().doubleValue());
						R31cell1.setCellStyle(numberStyle);
					} else {
						R31cell1.setCellValue("");
						R31cell1.setCellStyle(textStyle);
					}

					// R31 Col F
					Cell R31cell2 = row.createCell(5);
					if (record.getR31_no_of_mini_atms() != null) {
						R31cell2.setCellValue(record.getR31_no_of_mini_atms().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					// R31 Col G
					Cell R31cell3 = row.createCell(6);
					if (record.getR31_encashment_points() != null) {
						R31cell3.setCellValue(record.getR31_encashment_points().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// R32 Col E
					row = sheet.getRow(31);
					Cell R32cell1 = row.createCell(4);
					if (record.getR32_no_of_atms() != null) {
						R32cell1.setCellValue(record.getR32_no_of_atms().doubleValue());
						R32cell1.setCellStyle(numberStyle);
					} else {
						R32cell1.setCellValue("");
						R32cell1.setCellStyle(textStyle);
					}

					// R32 Col F
					Cell R32cell2 = row.createCell(5);
					if (record.getR32_no_of_mini_atms() != null) {
						R32cell2.setCellValue(record.getR32_no_of_mini_atms().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}

					// R32 Col G
					Cell R32cell3 = row.createCell(6);
					if (record.getR32_encashment_points() != null) {
						R32cell3.setCellValue(record.getR32_encashment_points().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}
					// R33 Col E
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(4);
					if (record.getR33_no_of_atms() != null) {
						R33cell1.setCellValue(record.getR33_no_of_atms().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col F
					Cell R33cell2 = row.createCell(5);
					if (record.getR33_no_of_mini_atms() != null) {
						R33cell2.setCellValue(record.getR33_no_of_mini_atms().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					// R33 Col G
					Cell R33cell3 = row.createCell(6);
					if (record.getR33_encashment_points() != null) {
						R33cell3.setCellValue(record.getR33_encashment_points().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}
					// R34 Col E
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(4);
					if (record.getR34_no_of_atms() != null) {
						R34cell1.setCellValue(record.getR34_no_of_atms().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col F
					Cell R34cell2 = row.createCell(5);
					if (record.getR34_no_of_mini_atms() != null) {
						R34cell2.setCellValue(record.getR34_no_of_mini_atms().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					// R34 Col G
					Cell R34cell3 = row.createCell(6);
					if (record.getR34_encashment_points() != null) {
						R34cell3.setCellValue(record.getR34_encashment_points().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// R35 Col E
					row = sheet.getRow(34);
					Cell R35cell1 = row.createCell(4);
					if (record.getR35_no_of_atms() != null) {
						R35cell1.setCellValue(record.getR35_no_of_atms().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col F
					Cell R35cell2 = row.createCell(5);
					if (record.getR35_no_of_mini_atms() != null) {
						R35cell2.setCellValue(record.getR35_no_of_mini_atms().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					// R35 Col G
					Cell R35cell3 = row.createCell(6);
					if (record.getR35_encashment_points() != null) {
						R35cell3.setCellValue(record.getR35_encashment_points().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}
					// TABLE 3
					// R40 Col E
					row = sheet.getRow(39);
					Cell R40cell1 = row.createCell(4);
					if (record.getR40_opening_no_of_cards() != null) {
						R40cell1.setCellValue(record.getR40_opening_no_of_cards().doubleValue());
						R40cell1.setCellStyle(numberStyle);
					} else {
						R40cell1.setCellValue("");
						R40cell1.setCellStyle(textStyle);
					}

					// R40 Col F
					Cell R40cell2 = row.createCell(5);
					if (record.getR40_no_of_cards_issued() != null) {
						R40cell2.setCellValue(record.getR40_no_of_cards_issued().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					// R40 Col G
					Cell R40cell3 = row.createCell(6);
					if (record.getR40_no_cards_of_closed() != null) {
						R40cell3.setCellValue(record.getR40_no_cards_of_closed().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}
					// R40 Col H
					Cell R40cell4 = row.createCell(7);
					if (record.getR40_closing_bal_of_active_cards() != null) {
						R40cell4.setCellValue(record.getR40_closing_bal_of_active_cards().doubleValue());
						R40cell4.setCellStyle(numberStyle);
					} else {
						R40cell4.setCellValue("");
						R40cell4.setCellStyle(textStyle);
					}

					// R41 Col E
					row = sheet.getRow(40);
					Cell R41cell1 = row.createCell(4);
					if (record.getR41_opening_no_of_cards() != null) {
						R41cell1.setCellValue(record.getR41_opening_no_of_cards().doubleValue());
						R41cell1.setCellStyle(numberStyle);
					} else {
						R41cell1.setCellValue("");
						R41cell1.setCellStyle(textStyle);
					}

					// R41 Col F
					Cell R41cell2 = row.createCell(5);
					if (record.getR41_no_of_cards_issued() != null) {
						R41cell2.setCellValue(record.getR41_no_of_cards_issued().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					// R41 Col G
					Cell R41cell3 = row.createCell(6);
					if (record.getR41_no_cards_of_closed() != null) {
						R41cell3.setCellValue(record.getR41_no_cards_of_closed().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}
					// R41 Col H
					Cell R41cell4 = row.createCell(7);
					if (record.getR41_closing_bal_of_active_cards() != null) {
						R41cell4.setCellValue(record.getR41_closing_bal_of_active_cards().doubleValue());
						R41cell4.setCellStyle(numberStyle);
					} else {
						R41cell4.setCellValue("");
						R41cell4.setCellStyle(textStyle);
					}
					// R42 Col E
					row = sheet.getRow(41);
					Cell R42cell1 = row.createCell(4);
					if (record.getR42_opening_no_of_cards() != null) {
						R42cell1.setCellValue(record.getR42_opening_no_of_cards().doubleValue());
						R42cell1.setCellStyle(numberStyle);
					} else {
						R42cell1.setCellValue("");
						R42cell1.setCellStyle(textStyle);
					}

					// R42 Col F
					Cell R42cell2 = row.createCell(5);
					if (record.getR42_no_of_cards_issued() != null) {
						R42cell2.setCellValue(record.getR42_no_of_cards_issued().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					// R42 Col G
					Cell R42cell3 = row.createCell(6);
					if (record.getR42_no_cards_of_closed() != null) {
						R42cell3.setCellValue(record.getR42_no_cards_of_closed().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}
					// R42 Col H
					Cell R42cell4 = row.createCell(7);
					if (record.getR42_closing_bal_of_active_cards() != null) {
						R42cell4.setCellValue(record.getR42_closing_bal_of_active_cards().doubleValue());
						R42cell4.setCellStyle(numberStyle);
					} else {
						R42cell4.setCellValue("");
						R42cell4.setCellStyle(textStyle);
					}
					// R43 Col E
					row = sheet.getRow(42);
					Cell R43cell1 = row.createCell(4);
					if (record.getR43_opening_no_of_cards() != null) {
						R43cell1.setCellValue(record.getR43_opening_no_of_cards().doubleValue());
						R43cell1.setCellStyle(numberStyle);
					} else {
						R43cell1.setCellValue("");
						R43cell1.setCellStyle(textStyle);
					}

					// R43 Col F
					Cell R43cell2 = row.createCell(5);
					if (record.getR43_no_of_cards_issued() != null) {
						R43cell2.setCellValue(record.getR43_no_of_cards_issued().doubleValue());
						R43cell2.setCellStyle(numberStyle);
					} else {
						R43cell2.setCellValue("");
						R43cell2.setCellStyle(textStyle);
					}

					// R43 Col G
					Cell R43cell3 = row.createCell(6);
					if (record.getR43_no_cards_of_closed() != null) {
						R43cell3.setCellValue(record.getR43_no_cards_of_closed().doubleValue());
						R43cell3.setCellStyle(numberStyle);
					} else {
						R43cell3.setCellValue("");
						R43cell3.setCellStyle(textStyle);
					}
					// R43 Col H
					Cell R43cell4 = row.createCell(7);
					if (record.getR43_closing_bal_of_active_cards() != null) {
						R43cell4.setCellValue(record.getR43_closing_bal_of_active_cards().doubleValue());
						R43cell4.setCellStyle(numberStyle);
					} else {
						R43cell4.setCellValue("");
						R43cell4.setCellStyle(textStyle);
					}

					// R44 Col E
					row = sheet.getRow(43);
					Cell R44cell1 = row.createCell(4);
					if (record.getR44_opening_no_of_cards() != null) {
						R44cell1.setCellValue(record.getR44_opening_no_of_cards().doubleValue());
						R44cell1.setCellStyle(numberStyle);
					} else {
						R44cell1.setCellValue("");
						R44cell1.setCellStyle(textStyle);
					}

					// R44 Col F
					Cell R44cell2 = row.createCell(5);
					if (record.getR44_no_of_cards_issued() != null) {
						R44cell2.setCellValue(record.getR44_no_of_cards_issued().doubleValue());
						R44cell2.setCellStyle(numberStyle);
					} else {
						R44cell2.setCellValue("");
						R44cell2.setCellStyle(textStyle);
					}

					// R44 Col G
					Cell R44cell3 = row.createCell(6);
					if (record.getR44_no_cards_of_closed() != null) {
						R44cell3.setCellValue(record.getR44_no_cards_of_closed().doubleValue());
						R44cell3.setCellStyle(numberStyle);
					} else {
						R44cell3.setCellValue("");
						R44cell3.setCellStyle(textStyle);
					}
					// R44 Col H
					Cell R44cell4 = row.createCell(7);
					if (record.getR44_closing_bal_of_active_cards() != null) {
						R44cell4.setCellValue(record.getR44_closing_bal_of_active_cards().doubleValue());
						R44cell4.setCellStyle(numberStyle);
					} else {
						R44cell4.setCellValue("");
						R44cell4.setCellStyle(textStyle);
					}
					// R45 Col E
					row = sheet.getRow(44);
					Cell R45cell1 = row.createCell(4);
					if (record.getR45_opening_no_of_cards() != null) {
						R45cell1.setCellValue(record.getR45_opening_no_of_cards().doubleValue());
						R45cell1.setCellStyle(numberStyle);
					} else {
						R45cell1.setCellValue("");
						R45cell1.setCellStyle(textStyle);
					}

					// R45 Col F
					Cell R45cell2 = row.createCell(5);
					if (record.getR45_no_of_cards_issued() != null) {
						R45cell2.setCellValue(record.getR45_no_of_cards_issued().doubleValue());
						R45cell2.setCellStyle(numberStyle);
					} else {
						R45cell2.setCellValue("");
						R45cell2.setCellStyle(textStyle);
					}

					// R45 Col G
					Cell R45cell3 = row.createCell(6);
					if (record.getR45_no_cards_of_closed() != null) {
						R45cell3.setCellValue(record.getR45_no_cards_of_closed().doubleValue());
						R45cell3.setCellStyle(numberStyle);
					} else {
						R45cell3.setCellValue("");
						R45cell3.setCellStyle(textStyle);
					}
					// R45 Col H
					Cell R45cell4 = row.createCell(7);
					if (record.getR45_closing_bal_of_active_cards() != null) {
						R45cell4.setCellValue(record.getR45_closing_bal_of_active_cards().doubleValue());
						R45cell4.setCellStyle(numberStyle);
					} else {
						R45cell4.setCellValue("");
						R45cell4.setCellStyle(textStyle);
					}
					// R46 Col E
					row = sheet.getRow(45);
					Cell R46cell1 = row.createCell(4);
					if (record.getR46_opening_no_of_cards() != null) {
						R46cell1.setCellValue(record.getR46_opening_no_of_cards().doubleValue());
						R46cell1.setCellStyle(numberStyle);
					} else {
						R46cell1.setCellValue("");
						R46cell1.setCellStyle(textStyle);
					}

					// R46 Col F
					Cell R46cell2 = row.createCell(5);
					if (record.getR46_no_of_cards_issued() != null) {
						R46cell2.setCellValue(record.getR46_no_of_cards_issued().doubleValue());
						R46cell2.setCellStyle(numberStyle);
					} else {
						R46cell2.setCellValue("");
						R46cell2.setCellStyle(textStyle);
					}

					// R46 Col G
					Cell R46cell3 = row.createCell(6);
					if (record.getR46_no_cards_of_closed() != null) {
						R46cell3.setCellValue(record.getR46_no_cards_of_closed().doubleValue());
						R46cell3.setCellStyle(numberStyle);
					} else {
						R46cell3.setCellValue("");
						R46cell3.setCellStyle(textStyle);
					}
					// R46 Col H
					Cell R46cell4 = row.createCell(7);
					if (record.getR46_closing_bal_of_active_cards() != null) {
						R46cell4.setCellValue(record.getR46_closing_bal_of_active_cards().doubleValue());
						R46cell4.setCellStyle(numberStyle);
					} else {
						R46cell4.setCellValue("");
						R46cell4.setCellStyle(textStyle);
					}
					// R47 Col E
					row = sheet.getRow(46);
					Cell R47cell1 = row.createCell(4);
					if (record.getR47_opening_no_of_cards() != null) {
						R47cell1.setCellValue(record.getR47_opening_no_of_cards().doubleValue());
						R47cell1.setCellStyle(numberStyle);
					} else {
						R47cell1.setCellValue("");
						R47cell1.setCellStyle(textStyle);
					}

					// R47 Col F
					Cell R47cell2 = row.createCell(5);
					if (record.getR47_no_of_cards_issued() != null) {
						R47cell2.setCellValue(record.getR47_no_of_cards_issued().doubleValue());
						R47cell2.setCellStyle(numberStyle);
					} else {
						R47cell2.setCellValue("");
						R47cell2.setCellStyle(textStyle);
					}

					// R47 Col G
					Cell R47cell3 = row.createCell(6);
					if (record.getR47_no_cards_of_closed() != null) {
						R47cell3.setCellValue(record.getR47_no_cards_of_closed().doubleValue());
						R47cell3.setCellStyle(numberStyle);
					} else {
						R47cell3.setCellValue("");
						R47cell3.setCellStyle(textStyle);
					}

					// R48 Col E
					row = sheet.getRow(47);
					Cell R48cell1 = row.createCell(4);
					if (record.getR48_opening_no_of_cards() != null) {
						R48cell1.setCellValue(record.getR48_opening_no_of_cards().doubleValue());
						R48cell1.setCellStyle(numberStyle);
					} else {
						R48cell1.setCellValue("");
						R48cell1.setCellStyle(textStyle);
					}

					// R48 Col F
					Cell R48cell2 = row.createCell(5);
					if (record.getR48_no_of_cards_issued() != null) {
						R48cell2.setCellValue(record.getR48_no_of_cards_issued().doubleValue());
						R48cell2.setCellStyle(numberStyle);
					} else {
						R48cell2.setCellValue("");
						R48cell2.setCellStyle(textStyle);
					}

					// R48 Col G
					Cell R48cell3 = row.createCell(6);
					if (record.getR48_no_cards_of_closed() != null) {
						R48cell3.setCellValue(record.getR48_no_cards_of_closed().doubleValue());
						R48cell3.setCellStyle(numberStyle);
					} else {
						R48cell3.setCellValue("");
						R48cell3.setCellStyle(textStyle);
					}

					// R49 Col E
					row = sheet.getRow(48);
					Cell R49cell1 = row.createCell(4);
					if (record.getR49_opening_no_of_cards() != null) {
						R49cell1.setCellValue(record.getR49_opening_no_of_cards().doubleValue());
						R49cell1.setCellStyle(numberStyle);
					} else {
						R49cell1.setCellValue("");
						R49cell1.setCellStyle(textStyle);
					}

					// R49 Col F
					Cell R49cell2 = row.createCell(5);
					if (record.getR49_no_of_cards_issued() != null) {
						R49cell2.setCellValue(record.getR49_no_of_cards_issued().doubleValue());
						R49cell2.setCellStyle(numberStyle);
					} else {
						R49cell2.setCellValue("");
						R49cell2.setCellStyle(textStyle);
					}

					// R49 Col G
					Cell R49cell3 = row.createCell(6);
					if (record.getR49_no_cards_of_closed() != null) {
						R49cell3.setCellValue(record.getR49_no_cards_of_closed().doubleValue());
						R49cell3.setCellStyle(numberStyle);
					} else {
						R49cell3.setCellValue("");
						R49cell3.setCellStyle(textStyle);
					}

					// TABLE 4
					// R55 Col E
					row = sheet.getRow(54);
					Cell R55cell1 = row.createCell(4);
					if (record.getR55_opening_no_of_cards() != null) {
						R55cell1.setCellValue(record.getR55_opening_no_of_cards().doubleValue());
						R55cell1.setCellStyle(numberStyle);
					} else {
						R55cell1.setCellValue("");
						R55cell1.setCellStyle(textStyle);
					}

					// R55 Col F
					Cell R55cell2 = row.createCell(5);
					if (record.getR55_no_of_cards_issued() != null) {
						R55cell2.setCellValue(record.getR55_no_of_cards_issued().doubleValue());
						R55cell2.setCellStyle(numberStyle);
					} else {
						R55cell2.setCellValue("");
						R55cell2.setCellStyle(textStyle);
					}

					// R55 Col G
					Cell R55cell3 = row.createCell(6);
					if (record.getR55_no_cards_of_closed() != null) {
						R55cell3.setCellValue(record.getR55_no_cards_of_closed().doubleValue());
						R55cell3.setCellStyle(numberStyle);
					} else {
						R55cell3.setCellValue("");
						R55cell3.setCellStyle(textStyle);
					}
					// R55 Col H
					Cell R55cell4 = row.createCell(7);
					if (record.getR55_closing_bal_of_active_cards() != null) {
						R55cell4.setCellValue(record.getR55_closing_bal_of_active_cards().doubleValue());
						R55cell4.setCellStyle(numberStyle);
					} else {
						R55cell4.setCellValue("");
						R55cell4.setCellStyle(textStyle);
					}
					// R56 Col E
					row = sheet.getRow(55);
					Cell R56cell1 = row.createCell(4);
					if (record.getR56_opening_no_of_cards() != null) {
						R56cell1.setCellValue(record.getR56_opening_no_of_cards().doubleValue());
						R56cell1.setCellStyle(numberStyle);
					} else {
						R56cell1.setCellValue("");
						R56cell1.setCellStyle(textStyle);
					}

					// R56 Col F
					Cell R56cell2 = row.createCell(5);
					if (record.getR56_no_of_cards_issued() != null) {
						R56cell2.setCellValue(record.getR56_no_of_cards_issued().doubleValue());
						R56cell2.setCellStyle(numberStyle);
					} else {
						R56cell2.setCellValue("");
						R56cell2.setCellStyle(textStyle);
					}

					// R56 Col G
					Cell R56cell3 = row.createCell(6);
					if (record.getR56_no_cards_of_closed() != null) {
						R56cell3.setCellValue(record.getR56_no_cards_of_closed().doubleValue());
						R56cell3.setCellStyle(numberStyle);
					} else {
						R56cell3.setCellValue("");
						R56cell3.setCellStyle(textStyle);
					}
					// R56 Col H
					Cell R56cell4 = row.createCell(7);
					if (record.getR56_closing_bal_of_active_cards() != null) {
						R56cell4.setCellValue(record.getR56_closing_bal_of_active_cards().doubleValue());
						R56cell4.setCellStyle(numberStyle);
					} else {
						R56cell4.setCellValue("");
						R56cell4.setCellStyle(textStyle);
					}
					// R57 Col E
					row = sheet.getRow(56);
					Cell R57cell1 = row.createCell(4);
					if (record.getR57_opening_no_of_cards() != null) {
						R57cell1.setCellValue(record.getR57_opening_no_of_cards().doubleValue());
						R57cell1.setCellStyle(numberStyle);
					} else {
						R57cell1.setCellValue("");
						R57cell1.setCellStyle(textStyle);
					}

					// R57 Col F
					Cell R57cell2 = row.createCell(5);
					if (record.getR57_no_of_cards_issued() != null) {
						R57cell2.setCellValue(record.getR57_no_of_cards_issued().doubleValue());
						R57cell2.setCellStyle(numberStyle);
					} else {
						R57cell2.setCellValue("");
						R57cell2.setCellStyle(textStyle);
					}

					// R57 Col G
					Cell R57cell3 = row.createCell(6);
					if (record.getR57_no_cards_of_closed() != null) {
						R57cell3.setCellValue(record.getR57_no_cards_of_closed().doubleValue());
						R57cell3.setCellStyle(numberStyle);
					} else {
						R57cell3.setCellValue("");
						R57cell3.setCellStyle(textStyle);
					}
					// R57 Col H
					Cell R57cell4 = row.createCell(7);
					if (record.getR57_closing_bal_of_active_cards() != null) {
						R57cell4.setCellValue(record.getR57_closing_bal_of_active_cards().doubleValue());
						R57cell4.setCellStyle(numberStyle);
					} else {
						R57cell4.setCellValue("");
						R57cell4.setCellStyle(textStyle);
					}
					// R58 Col E
					row = sheet.getRow(57);
					Cell R58cell1 = row.createCell(4);
					if (record.getR58_opening_no_of_cards() != null) {
						R58cell1.setCellValue(record.getR58_opening_no_of_cards().doubleValue());
						R58cell1.setCellStyle(numberStyle);
					} else {
						R58cell1.setCellValue("");
						R58cell1.setCellStyle(textStyle);
					}

					// R58 Col F
					Cell R58cell2 = row.createCell(5);
					if (record.getR58_no_of_cards_issued() != null) {
						R58cell2.setCellValue(record.getR58_no_of_cards_issued().doubleValue());
						R58cell2.setCellStyle(numberStyle);
					} else {
						R58cell2.setCellValue("");
						R58cell2.setCellStyle(textStyle);
					}

					// R58 Col G
					Cell R58cell3 = row.createCell(6);
					if (record.getR58_no_cards_of_closed() != null) {
						R58cell3.setCellValue(record.getR58_no_cards_of_closed().doubleValue());
						R58cell3.setCellStyle(numberStyle);
					} else {
						R58cell3.setCellValue("");
						R58cell3.setCellStyle(textStyle);
					}
					// R58 Col H
					Cell R58cell4 = row.createCell(7);
					if (record.getR58_closing_bal_of_active_cards() != null) {
						R58cell4.setCellValue(record.getR58_closing_bal_of_active_cards().doubleValue());
						R58cell4.setCellStyle(numberStyle);
					} else {
						R58cell4.setCellValue("");
						R58cell4.setCellStyle(textStyle);
					}
					// // R59 Col E
					// row = sheet.getRow(58);
					// Cell R59cell1 = row.createCell(4);
					// if (record.getR59_opening_no_of_cards() != null) {
					// 	R59cell1.setCellValue(record.getR59_opening_no_of_cards().doubleValue());
					// 	R59cell1.setCellStyle(numberStyle);
					// } else {
					// 	R59cell1.setCellValue("");
					// 	R59cell1.setCellStyle(textStyle);
					// }

					// // R59 Col F
					// Cell R59cell2 = row.createCell(5);
					// if (record.getR59_no_of_cards_issued() != null) {
					// 	R59cell2.setCellValue(record.getR59_no_of_cards_issued().doubleValue());
					// 	R59cell2.setCellStyle(numberStyle);
					// } else {
					// 	R59cell2.setCellValue("");
					// 	R59cell2.setCellStyle(textStyle);
					// }

					// // R59 Col G
					// Cell R59cell3 = row.createCell(6);
					// if (record.getR59_no_cards_of_closed() != null) {
					// 	R59cell3.setCellValue(record.getR59_no_cards_of_closed().doubleValue());
					// 	R59cell3.setCellStyle(numberStyle);
					// } else {
					// 	R59cell3.setCellValue("");
					// 	R59cell3.setCellStyle(textStyle);
					// }
					// // R59 Col H
					// Cell R59cell4 = row.createCell(7);
					// if (record.getR59_closing_bal_of_active_cards() != null) {
					// 	R59cell4.setCellValue(record.getR59_closing_bal_of_active_cards().doubleValue());
					// 	R59cell4.setCellStyle(numberStyle);
					// } else {
					// 	R59cell4.setCellValue("");
					// 	R59cell4.setCellStyle(textStyle);
					// }

					// R60 Col E
					row = sheet.getRow(59);
					Cell R60cell1 = row.createCell(4);
					if (record.getR60_opening_no_of_cards() != null) {
						R60cell1.setCellValue(record.getR60_opening_no_of_cards().doubleValue());
						R60cell1.setCellStyle(numberStyle);
					} else {
						R60cell1.setCellValue("");
						R60cell1.setCellStyle(textStyle);
					}

					// R60 Col F
					Cell R60cell2 = row.createCell(5);
					if (record.getR60_no_of_cards_issued() != null) {
						R60cell2.setCellValue(record.getR60_no_of_cards_issued().doubleValue());
						R60cell2.setCellStyle(numberStyle);
					} else {
						R60cell2.setCellValue("");
						R60cell2.setCellStyle(textStyle);
					}
					// R60 Col G
					Cell R60cell3 = row.createCell(6);
					if (record.getR60_no_cards_of_closed() != null) {
						R60cell3.setCellValue(record.getR60_no_cards_of_closed().doubleValue());
						R60cell3.setCellStyle(numberStyle);
					} else {
						R60cell3.setCellValue("");
						R60cell3.setCellStyle(textStyle);
					}
					// R60 Col H
					Cell R60cell4 = row.createCell(7);
					if (record.getR60_closing_bal_of_active_cards() != null) {
						R60cell4.setCellValue(record.getR60_closing_bal_of_active_cards().doubleValue());
						R60cell4.setCellStyle(numberStyle);
					} else {
						R60cell4.setCellValue("");
						R60cell4.setCellStyle(textStyle);
					}
					// R61 Col E
					row = sheet.getRow(60);
					Cell R61cell1 = row.createCell(4);
					if (record.getR61_opening_no_of_cards() != null) {
						R61cell1.setCellValue(record.getR61_opening_no_of_cards().doubleValue());
						R61cell1.setCellStyle(numberStyle);
					} else {
						R61cell1.setCellValue("");
						R61cell1.setCellStyle(textStyle);
					}

					// R61 Col F
					Cell R61cell2 = row.createCell(5);
					if (record.getR61_no_of_cards_issued() != null) {
						R61cell2.setCellValue(record.getR61_no_of_cards_issued().doubleValue());
						R61cell2.setCellStyle(numberStyle);
					} else {
						R61cell2.setCellValue("");
						R61cell2.setCellStyle(textStyle);
					}
					// R61 Col G
					Cell R61cell3 = row.createCell(6);
					if (record.getR61_no_cards_of_closed() != null) {
						R61cell3.setCellValue(record.getR61_no_cards_of_closed().doubleValue());
						R61cell3.setCellStyle(numberStyle);
					} else {
						R61cell3.setCellValue("");
						R61cell3.setCellStyle(textStyle);
					}
					// R61 Col H
					Cell R61cell4 = row.createCell(7);
					if (record.getR61_closing_bal_of_active_cards() != null) {
						R61cell4.setCellValue(record.getR61_closing_bal_of_active_cards().doubleValue());
						R61cell4.setCellStyle(numberStyle);
					} else {
						R61cell4.setCellValue("");
						R61cell4.setCellStyle(textStyle);
					}
					// R62 Col E
					row = sheet.getRow(61);
					Cell R62cell1 = row.createCell(4);
					if (record.getR62_opening_no_of_cards() != null) {
						R62cell1.setCellValue(record.getR62_opening_no_of_cards().doubleValue());
						R62cell1.setCellStyle(numberStyle);
					} else {
						R62cell1.setCellValue("");
						R62cell1.setCellStyle(textStyle);
					}

					// R62 Col F
					Cell R62cell2 = row.createCell(5);
					if (record.getR62_no_of_cards_issued() != null) {
						R62cell2.setCellValue(record.getR62_no_of_cards_issued().doubleValue());
						R62cell2.setCellStyle(numberStyle);
					} else {
						R62cell2.setCellValue("");
						R62cell2.setCellStyle(textStyle);
					}
					// R62 Col G
					Cell R62cell3 = row.createCell(6);
					if (record.getR62_no_cards_of_closed() != null) {
						R62cell3.setCellValue(record.getR62_no_cards_of_closed().doubleValue());
						R62cell3.setCellStyle(numberStyle);
					} else {
						R62cell3.setCellValue("");
						R62cell3.setCellStyle(textStyle);
					}
					// R62 Col H
					Cell R62cell4 = row.createCell(7);
					if (record.getR62_closing_bal_of_active_cards() != null) {
						R62cell4.setCellValue(record.getR62_closing_bal_of_active_cards().doubleValue());
						R62cell4.setCellStyle(numberStyle);
					} else {
						R62cell4.setCellValue("");
						R62cell4.setCellStyle(textStyle);
					}
					// R63 Col E
					row = sheet.getRow(62);
					Cell R63cell1 = row.createCell(4);
					if (record.getR63_opening_no_of_cards() != null) {
						R63cell1.setCellValue(record.getR63_opening_no_of_cards().doubleValue());
						R63cell1.setCellStyle(numberStyle);
					} else {
						R63cell1.setCellValue("");
						R63cell1.setCellStyle(textStyle);
					}

					// R63 Col F
					Cell R63cell2 = row.createCell(5);
					if (record.getR63_no_of_cards_issued() != null) {
						R63cell2.setCellValue(record.getR63_no_of_cards_issued().doubleValue());
						R63cell2.setCellStyle(numberStyle);
					} else {
						R63cell2.setCellValue("");
						R63cell2.setCellStyle(textStyle);
					}
					// R63 Col G
					Cell R63cell3 = row.createCell(6);
					if (record.getR63_no_cards_of_closed() != null) {
						R63cell3.setCellValue(record.getR63_no_cards_of_closed().doubleValue());
						R63cell3.setCellStyle(numberStyle);
					} else {
						R63cell3.setCellValue("");
						R63cell3.setCellStyle(textStyle);
					}
					// R63 Col H
					Cell R63cell4 = row.createCell(7);
					if (record.getR63_closing_bal_of_active_cards() != null) {
						R63cell4.setCellValue(record.getR63_closing_bal_of_active_cards().doubleValue());
						R63cell4.setCellStyle(numberStyle);
					} else {
						R63cell4.setCellValue("");
						R63cell4.setCellStyle(textStyle);
					}
					// R64 Col E
					row = sheet.getRow(63);
					Cell R64cell1 = row.createCell(4);
					if (record.getR64_opening_no_of_cards() != null) {
						R64cell1.setCellValue(record.getR64_opening_no_of_cards().doubleValue());
						R64cell1.setCellStyle(numberStyle);
					} else {
						R64cell1.setCellValue("");
						R64cell1.setCellStyle(textStyle);
					}

					// R64 Col F
					Cell R64cell2 = row.createCell(5);
					if (record.getR64_no_of_cards_issued() != null) {
						R64cell2.setCellValue(record.getR64_no_of_cards_issued().doubleValue());
						R64cell2.setCellStyle(numberStyle);
					} else {
						R64cell2.setCellValue("");
						R64cell2.setCellStyle(textStyle);
					}
					// R64 Col G
					Cell R64cell3 = row.createCell(6);
					if (record.getR64_no_cards_of_closed() != null) {
						R64cell3.setCellValue(record.getR64_no_cards_of_closed().doubleValue());
						R64cell3.setCellStyle(numberStyle);
					} else {
						R64cell3.setCellValue("");
						R64cell3.setCellStyle(textStyle);
					}
					// R64 Col H
					Cell R64cell4 = row.createCell(7);
					if (record.getR64_closing_bal_of_active_cards() != null) {
						R64cell4.setCellValue(record.getR64_closing_bal_of_active_cards().doubleValue());
						R64cell4.setCellStyle(numberStyle);
					} else {
						R64cell4.setCellValue("");
						R64cell4.setCellStyle(textStyle);
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

	public byte[] BRRS_Q_BRANCHNETEmailArchivalExcel(String filename, String reportId, String fromdate,
            String todate,
            String currency, String dtltype, String type, BigDecimal version) throws Exception {
        logger.info("Service: Starting Excel generation process in memory.");

        List<Q_BRANCHNET_Archival_Summary_Entity> dataList = Q_BRANCHNET_Archival_Summary_Repo
                .getdatabydateListarchival(dateformat.parse(todate), version);

        if (dataList.isEmpty()) {
            logger.warn("Service: No data found forQ_BRANCHNET report. Returning empty result.");
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
					Q_BRANCHNET_Archival_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R10 Col E

					Cell R10cell1 = row.createCell(4);
					if (record.getR10_no1_of_branches() != null) {
						R10cell1.setCellValue(record.getR10_no1_of_branches().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col F
					Cell R10cell2 = row.createCell(5);
					if (record.getR10_no1_of_sub_branches() != null) {
						R10cell2.setCellValue(record.getR10_no1_of_sub_branches().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}

					// R10 Col G
					Cell R10cell3 = row.createCell(6);
					if (record.getR10_no1_of_agencies() != null) {
						R10cell3.setCellValue(record.getR10_no1_of_agencies().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}
					// R11 Col E
					row = sheet.getRow(10);
					Cell R11cell1 = row.createCell(4);
					if (record.getR11_no1_of_branches() != null) {
						R11cell1.setCellValue(record.getR11_no1_of_branches().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col F
					Cell R11cell2 = row.createCell(5);
					if (record.getR11_no1_of_sub_branches() != null) {
						R11cell2.setCellValue(record.getR11_no1_of_sub_branches().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 Col G
					Cell R11cell3 = row.createCell(6);
					if (record.getR11_no1_of_agencies() != null) {
						R11cell3.setCellValue(record.getR11_no1_of_agencies().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}
					// R12 Col E
					row = sheet.getRow(11);
					Cell R12cell1 = row.createCell(4);
					if (record.getR12_no1_of_branches() != null) {
						R12cell1.setCellValue(record.getR12_no1_of_branches().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col F
					Cell R12cell2 = row.createCell(5);
					if (record.getR12_no1_of_sub_branches() != null) {
						R12cell2.setCellValue(record.getR12_no1_of_sub_branches().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					// R12 Col G
					Cell R12cell3 = row.createCell(6);
					if (record.getR12_no1_of_agencies() != null) {
						R12cell3.setCellValue(record.getR12_no1_of_agencies().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}
					// R13 Col E
					row = sheet.getRow(12);
					Cell R13cell1 = row.createCell(4);
					if (record.getR13_no1_of_branches() != null) {
						R13cell1.setCellValue(record.getR13_no1_of_branches().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col F
					Cell R13cell2 = row.createCell(5);
					if (record.getR13_no1_of_sub_branches() != null) {
						R13cell2.setCellValue(record.getR13_no1_of_sub_branches().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					// R13 Col G
					Cell R13cell3 = row.createCell(6);
					if (record.getR13_no1_of_agencies() != null) {
						R13cell3.setCellValue(record.getR13_no1_of_agencies().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}
					// R14 Col E
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(4);
					if (record.getR14_no1_of_branches() != null) {
						R14cell1.setCellValue(record.getR14_no1_of_branches().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col F
					Cell R14cell2 = row.createCell(5);
					if (record.getR14_no1_of_sub_branches() != null) {
						R14cell2.setCellValue(record.getR14_no1_of_sub_branches().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// R14 Col G
					Cell R14cell3 = row.createCell(6);
					if (record.getR14_no1_of_agencies() != null) {
						R14cell3.setCellValue(record.getR14_no1_of_agencies().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}
					// R15 Col E
					row = sheet.getRow(14);
					Cell R15cell1 = row.createCell(4);
					if (record.getR15_no1_of_branches() != null) {
						R15cell1.setCellValue(record.getR15_no1_of_branches().doubleValue());
						R15cell1.setCellStyle(numberStyle);
					} else {
						R15cell1.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

					// R15 Col F
					Cell R15cell2 = row.createCell(5);
					if (record.getR15_no1_of_sub_branches() != null) {
						R15cell2.setCellValue(record.getR15_no1_of_sub_branches().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}

					// R15 Col G
					Cell R15cell3 = row.createCell(6);
					if (record.getR15_no1_of_agencies() != null) {
						R15cell3.setCellValue(record.getR15_no1_of_agencies().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}
					// R16 Col E
					row = sheet.getRow(15);
					Cell R16cell1 = row.createCell(4);
					if (record.getR16_no1_of_branches() != null) {
						R16cell1.setCellValue(record.getR16_no1_of_branches().doubleValue());
						R16cell1.setCellStyle(numberStyle);
					} else {
						R16cell1.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

					// R16 Col F
					Cell R16cell2 = row.createCell(5);
					if (record.getR16_no1_of_sub_branches() != null) {
						R16cell2.setCellValue(record.getR16_no1_of_sub_branches().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}

					// R16 Col G
					Cell R16cell3 = row.createCell(6);
					if (record.getR16_no1_of_agencies() != null) {
						R16cell3.setCellValue(record.getR16_no1_of_agencies().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}
					// R17 Col E
					row = sheet.getRow(16);
					Cell R17cell1 = row.createCell(4);
					if (record.getR17_no1_of_branches() != null) {
						R17cell1.setCellValue(record.getR17_no1_of_branches().doubleValue());
						R17cell1.setCellStyle(numberStyle);
					} else {
						R17cell1.setCellValue("");
						R17cell1.setCellStyle(textStyle);
					}

					// R17 Col F
					Cell R17cell2 = row.createCell(5);
					if (record.getR17_no1_of_sub_branches() != null) {
						R17cell2.setCellValue(record.getR17_no1_of_sub_branches().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// R17 Col G
					Cell R17cell3 = row.createCell(6);
					if (record.getR17_no1_of_agencies() != null) {
						R17cell3.setCellValue(record.getR17_no1_of_agencies().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}
					// R18 Col E
					row = sheet.getRow(17);
					Cell R18cell1 = row.createCell(4);
					if (record.getR18_no1_of_branches() != null) {
						R18cell1.setCellValue(record.getR18_no1_of_branches().doubleValue());
						R18cell1.setCellStyle(numberStyle);
					} else {
						R18cell1.setCellValue("");
						R18cell1.setCellStyle(textStyle);
					}

					// R18 Col F
					Cell R18cell2 = row.createCell(5);
					if (record.getR18_no1_of_sub_branches() != null) {
						R18cell2.setCellValue(record.getR18_no1_of_sub_branches().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					// R18 Col G
					Cell R18cell3 = row.createCell(6);
					if (record.getR18_no1_of_agencies() != null) {
						R18cell3.setCellValue(record.getR18_no1_of_agencies().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// R19 Col E
					row = sheet.getRow(18);
					Cell R19cell1 = row.createCell(4);
					if (record.getR19_no1_of_branches() != null) {
						R19cell1.setCellValue(record.getR19_no1_of_branches().doubleValue());
						R19cell1.setCellStyle(numberStyle);
					} else {
						R19cell1.setCellValue("");
						R19cell1.setCellStyle(textStyle);
					}

					// R19 Col F
					Cell R19cell2 = row.createCell(5);
					if (record.getR19_no1_of_sub_branches() != null) {
						R19cell2.setCellValue(record.getR19_no1_of_sub_branches().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					// R19 Col G
					Cell R19cell3 = row.createCell(6);
					if (record.getR19_no1_of_agencies() != null) {
						R19cell3.setCellValue(record.getR19_no1_of_agencies().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}
					// R20 Col E
					row = sheet.getRow(19);
					Cell R20cell1 = row.createCell(4);
					if (record.getR20_no1_of_branches() != null) {
						R20cell1.setCellValue(record.getR20_no1_of_branches().doubleValue());
						R20cell1.setCellStyle(numberStyle);
					} else {
						R20cell1.setCellValue("");
						R20cell1.setCellStyle(textStyle);
					}

					// R20 Col F
					Cell R20cell2 = row.createCell(5);
					if (record.getR20_no1_of_sub_branches() != null) {
						R20cell2.setCellValue(record.getR20_no1_of_sub_branches().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}

					// R20 Col G
					Cell R20cell3 = row.createCell(6);
					if (record.getR20_no1_of_agencies() != null) {
						R20cell3.setCellValue(record.getR20_no1_of_agencies().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}
					// TABLE 2
					// R25 Col E
					row = sheet.getRow(24);
					Cell R25cell1 = row.createCell(4);
					if (record.getR25_no_of_atms() != null) {
						R25cell1.setCellValue(record.getR25_no_of_atms().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col F
					Cell R25cell2 = row.createCell(5);
					if (record.getR25_no_of_mini_atms() != null) {
						R25cell2.setCellValue(record.getR25_no_of_mini_atms().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					// R25 Col G
					Cell R25cell3 = row.createCell(6);
					if (record.getR25_encashment_points() != null) {
						R25cell3.setCellValue(record.getR25_encashment_points().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}
					// R26 Col E
					row = sheet.getRow(25);
					Cell R26cell1 = row.createCell(4);
					if (record.getR26_no_of_atms() != null) {
						R26cell1.setCellValue(record.getR26_no_of_atms().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col F
					Cell R26cell2 = row.createCell(5);
					if (record.getR26_no_of_mini_atms() != null) {
						R26cell2.setCellValue(record.getR26_no_of_mini_atms().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					// R26 Col G
					Cell R26cell3 = row.createCell(6);
					if (record.getR26_encashment_points() != null) {
						R26cell3.setCellValue(record.getR26_encashment_points().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}
					// R27 Col E
					row = sheet.getRow(26);
					Cell R27cell1 = row.createCell(4);
					if (record.getR27_no_of_atms() != null) {
						R27cell1.setCellValue(record.getR27_no_of_atms().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col F
					Cell R27cell2 = row.createCell(5);
					if (record.getR27_no_of_mini_atms() != null) {
						R27cell2.setCellValue(record.getR27_no_of_mini_atms().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					// R27 Col G
					Cell R27cell3 = row.createCell(6);
					if (record.getR27_encashment_points() != null) {
						R27cell3.setCellValue(record.getR27_encashment_points().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}
					// R28 Col E
					row = sheet.getRow(27);
					Cell R28cell1 = row.createCell(4);
					if (record.getR28_no_of_atms() != null) {
						R28cell1.setCellValue(record.getR28_no_of_atms().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}

					// R28 Col F
					Cell R28cell2 = row.createCell(5);
					if (record.getR28_no_of_mini_atms() != null) {
						R28cell2.setCellValue(record.getR28_no_of_mini_atms().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}

					// R28 Col G
					Cell R28cell3 = row.createCell(6);
					if (record.getR28_encashment_points() != null) {
						R28cell3.setCellValue(record.getR28_encashment_points().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}
					// R29 Col E
					row = sheet.getRow(28);
					Cell R29cell1 = row.createCell(4);
					if (record.getR29_no_of_atms() != null) {
						R29cell1.setCellValue(record.getR29_no_of_atms().doubleValue());
						R29cell1.setCellStyle(numberStyle);
					} else {
						R29cell1.setCellValue("");
						R29cell1.setCellStyle(textStyle);
					}

					// R29 Col F
					Cell R29cell2 = row.createCell(5);
					if (record.getR29_no_of_mini_atms() != null) {
						R29cell2.setCellValue(record.getR29_no_of_mini_atms().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					// R29 Col G
					Cell R29cell3 = row.createCell(6);
					if (record.getR29_encashment_points() != null) {
						R29cell3.setCellValue(record.getR29_encashment_points().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}
					// R30 Col E
					row = sheet.getRow(29);
					Cell R30cell1 = row.createCell(4);
					if (record.getR30_no_of_atms() != null) {
						R30cell1.setCellValue(record.getR30_no_of_atms().doubleValue());
						R30cell1.setCellStyle(numberStyle);
					} else {
						R30cell1.setCellValue("");
						R30cell1.setCellStyle(textStyle);
					}

					// R30 Col F
					Cell R30cell2 = row.createCell(5);
					if (record.getR30_no_of_mini_atms() != null) {
						R30cell2.setCellValue(record.getR30_no_of_mini_atms().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					// R30 Col G
					Cell R30cell3 = row.createCell(6);
					if (record.getR30_encashment_points() != null) {
						R30cell3.setCellValue(record.getR30_encashment_points().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}
					// R31 Col E
					row = sheet.getRow(30);
					Cell R31cell1 = row.createCell(4);
					if (record.getR31_no_of_atms() != null) {
						R31cell1.setCellValue(record.getR31_no_of_atms().doubleValue());
						R31cell1.setCellStyle(numberStyle);
					} else {
						R31cell1.setCellValue("");
						R31cell1.setCellStyle(textStyle);
					}

					// R31 Col F
					Cell R31cell2 = row.createCell(5);
					if (record.getR31_no_of_mini_atms() != null) {
						R31cell2.setCellValue(record.getR31_no_of_mini_atms().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					// R31 Col G
					Cell R31cell3 = row.createCell(6);
					if (record.getR31_encashment_points() != null) {
						R31cell3.setCellValue(record.getR31_encashment_points().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// R32 Col E
					row = sheet.getRow(31);
					Cell R32cell1 = row.createCell(4);
					if (record.getR32_no_of_atms() != null) {
						R32cell1.setCellValue(record.getR32_no_of_atms().doubleValue());
						R32cell1.setCellStyle(numberStyle);
					} else {
						R32cell1.setCellValue("");
						R32cell1.setCellStyle(textStyle);
					}

					// R32 Col F
					Cell R32cell2 = row.createCell(5);
					if (record.getR32_no_of_mini_atms() != null) {
						R32cell2.setCellValue(record.getR32_no_of_mini_atms().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}

					// R32 Col G
					Cell R32cell3 = row.createCell(6);
					if (record.getR32_encashment_points() != null) {
						R32cell3.setCellValue(record.getR32_encashment_points().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}
					// R33 Col E
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(4);
					if (record.getR33_no_of_atms() != null) {
						R33cell1.setCellValue(record.getR33_no_of_atms().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col F
					Cell R33cell2 = row.createCell(5);
					if (record.getR33_no_of_mini_atms() != null) {
						R33cell2.setCellValue(record.getR33_no_of_mini_atms().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					// R33 Col G
					Cell R33cell3 = row.createCell(6);
					if (record.getR33_encashment_points() != null) {
						R33cell3.setCellValue(record.getR33_encashment_points().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}
					// R34 Col E
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(4);
					if (record.getR34_no_of_atms() != null) {
						R34cell1.setCellValue(record.getR34_no_of_atms().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col F
					Cell R34cell2 = row.createCell(5);
					if (record.getR34_no_of_mini_atms() != null) {
						R34cell2.setCellValue(record.getR34_no_of_mini_atms().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					// R34 Col G
					Cell R34cell3 = row.createCell(6);
					if (record.getR34_encashment_points() != null) {
						R34cell3.setCellValue(record.getR34_encashment_points().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// R35 Col E
					row = sheet.getRow(34);
					Cell R35cell1 = row.createCell(4);
					if (record.getR35_no_of_atms() != null) {
						R35cell1.setCellValue(record.getR35_no_of_atms().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col F
					Cell R35cell2 = row.createCell(5);
					if (record.getR35_no_of_mini_atms() != null) {
						R35cell2.setCellValue(record.getR35_no_of_mini_atms().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					// R35 Col G
					Cell R35cell3 = row.createCell(6);
					if (record.getR35_encashment_points() != null) {
						R35cell3.setCellValue(record.getR35_encashment_points().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}
					// TABLE 3
					// R40 Col E
					row = sheet.getRow(39);
					Cell R40cell1 = row.createCell(4);
					if (record.getR40_opening_no_of_cards() != null) {
						R40cell1.setCellValue(record.getR40_opening_no_of_cards().doubleValue());
						R40cell1.setCellStyle(numberStyle);
					} else {
						R40cell1.setCellValue("");
						R40cell1.setCellStyle(textStyle);
					}

					// R40 Col F
					Cell R40cell2 = row.createCell(5);
					if (record.getR40_no_of_cards_issued() != null) {
						R40cell2.setCellValue(record.getR40_no_of_cards_issued().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					// R40 Col G
					Cell R40cell3 = row.createCell(6);
					if (record.getR40_no_cards_of_closed() != null) {
						R40cell3.setCellValue(record.getR40_no_cards_of_closed().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}
					// R40 Col H
					Cell R40cell4 = row.createCell(7);
					if (record.getR40_closing_bal_of_active_cards() != null) {
						R40cell4.setCellValue(record.getR40_closing_bal_of_active_cards().doubleValue());
						R40cell4.setCellStyle(numberStyle);
					} else {
						R40cell4.setCellValue("");
						R40cell4.setCellStyle(textStyle);
					}

					// R41 Col E
					row = sheet.getRow(40);
					Cell R41cell1 = row.createCell(4);
					if (record.getR41_opening_no_of_cards() != null) {
						R41cell1.setCellValue(record.getR41_opening_no_of_cards().doubleValue());
						R41cell1.setCellStyle(numberStyle);
					} else {
						R41cell1.setCellValue("");
						R41cell1.setCellStyle(textStyle);
					}

					// R41 Col F
					Cell R41cell2 = row.createCell(5);
					if (record.getR41_no_of_cards_issued() != null) {
						R41cell2.setCellValue(record.getR41_no_of_cards_issued().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					// R41 Col G
					Cell R41cell3 = row.createCell(6);
					if (record.getR41_no_cards_of_closed() != null) {
						R41cell3.setCellValue(record.getR41_no_cards_of_closed().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}
					// R41 Col H
					Cell R41cell4 = row.createCell(7);
					if (record.getR41_closing_bal_of_active_cards() != null) {
						R41cell4.setCellValue(record.getR41_closing_bal_of_active_cards().doubleValue());
						R41cell4.setCellStyle(numberStyle);
					} else {
						R41cell4.setCellValue("");
						R41cell4.setCellStyle(textStyle);
					}
					// R42 Col E
					row = sheet.getRow(41);
					Cell R42cell1 = row.createCell(4);
					if (record.getR42_opening_no_of_cards() != null) {
						R42cell1.setCellValue(record.getR42_opening_no_of_cards().doubleValue());
						R42cell1.setCellStyle(numberStyle);
					} else {
						R42cell1.setCellValue("");
						R42cell1.setCellStyle(textStyle);
					}

					// R42 Col F
					Cell R42cell2 = row.createCell(5);
					if (record.getR42_no_of_cards_issued() != null) {
						R42cell2.setCellValue(record.getR42_no_of_cards_issued().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					// R42 Col G
					Cell R42cell3 = row.createCell(6);
					if (record.getR42_no_cards_of_closed() != null) {
						R42cell3.setCellValue(record.getR42_no_cards_of_closed().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}
					// R42 Col H
					Cell R42cell4 = row.createCell(7);
					if (record.getR42_closing_bal_of_active_cards() != null) {
						R42cell4.setCellValue(record.getR42_closing_bal_of_active_cards().doubleValue());
						R42cell4.setCellStyle(numberStyle);
					} else {
						R42cell4.setCellValue("");
						R42cell4.setCellStyle(textStyle);
					}
					// R43 Col E
					row = sheet.getRow(42);
					Cell R43cell1 = row.createCell(4);
					if (record.getR43_opening_no_of_cards() != null) {
						R43cell1.setCellValue(record.getR43_opening_no_of_cards().doubleValue());
						R43cell1.setCellStyle(numberStyle);
					} else {
						R43cell1.setCellValue("");
						R43cell1.setCellStyle(textStyle);
					}

					// R43 Col F
					Cell R43cell2 = row.createCell(5);
					if (record.getR43_no_of_cards_issued() != null) {
						R43cell2.setCellValue(record.getR43_no_of_cards_issued().doubleValue());
						R43cell2.setCellStyle(numberStyle);
					} else {
						R43cell2.setCellValue("");
						R43cell2.setCellStyle(textStyle);
					}

					// R43 Col G
					Cell R43cell3 = row.createCell(6);
					if (record.getR43_no_cards_of_closed() != null) {
						R43cell3.setCellValue(record.getR43_no_cards_of_closed().doubleValue());
						R43cell3.setCellStyle(numberStyle);
					} else {
						R43cell3.setCellValue("");
						R43cell3.setCellStyle(textStyle);
					}
					// R43 Col H
					Cell R43cell4 = row.createCell(7);
					if (record.getR43_closing_bal_of_active_cards() != null) {
						R43cell4.setCellValue(record.getR43_closing_bal_of_active_cards().doubleValue());
						R43cell4.setCellStyle(numberStyle);
					} else {
						R43cell4.setCellValue("");
						R43cell4.setCellStyle(textStyle);
					}

					// R44 Col E
					row = sheet.getRow(43);
					Cell R44cell1 = row.createCell(4);
					if (record.getR44_opening_no_of_cards() != null) {
						R44cell1.setCellValue(record.getR44_opening_no_of_cards().doubleValue());
						R44cell1.setCellStyle(numberStyle);
					} else {
						R44cell1.setCellValue("");
						R44cell1.setCellStyle(textStyle);
					}

					// R44 Col F
					Cell R44cell2 = row.createCell(5);
					if (record.getR44_no_of_cards_issued() != null) {
						R44cell2.setCellValue(record.getR44_no_of_cards_issued().doubleValue());
						R44cell2.setCellStyle(numberStyle);
					} else {
						R44cell2.setCellValue("");
						R44cell2.setCellStyle(textStyle);
					}

					// R44 Col G
					Cell R44cell3 = row.createCell(6);
					if (record.getR44_no_cards_of_closed() != null) {
						R44cell3.setCellValue(record.getR44_no_cards_of_closed().doubleValue());
						R44cell3.setCellStyle(numberStyle);
					} else {
						R44cell3.setCellValue("");
						R44cell3.setCellStyle(textStyle);
					}
					// R44 Col H
					Cell R44cell4 = row.createCell(7);
					if (record.getR44_closing_bal_of_active_cards() != null) {
						R44cell4.setCellValue(record.getR44_closing_bal_of_active_cards().doubleValue());
						R44cell4.setCellStyle(numberStyle);
					} else {
						R44cell4.setCellValue("");
						R44cell4.setCellStyle(textStyle);
					}
					// R45 Col E
					row = sheet.getRow(44);
					Cell R45cell1 = row.createCell(4);
					if (record.getR45_opening_no_of_cards() != null) {
						R45cell1.setCellValue(record.getR45_opening_no_of_cards().doubleValue());
						R45cell1.setCellStyle(numberStyle);
					} else {
						R45cell1.setCellValue("");
						R45cell1.setCellStyle(textStyle);
					}

					// R45 Col F
					Cell R45cell2 = row.createCell(5);
					if (record.getR45_no_of_cards_issued() != null) {
						R45cell2.setCellValue(record.getR45_no_of_cards_issued().doubleValue());
						R45cell2.setCellStyle(numberStyle);
					} else {
						R45cell2.setCellValue("");
						R45cell2.setCellStyle(textStyle);
					}

					// R45 Col G
					Cell R45cell3 = row.createCell(6);
					if (record.getR45_no_cards_of_closed() != null) {
						R45cell3.setCellValue(record.getR45_no_cards_of_closed().doubleValue());
						R45cell3.setCellStyle(numberStyle);
					} else {
						R45cell3.setCellValue("");
						R45cell3.setCellStyle(textStyle);
					}
					// R45 Col H
					Cell R45cell4 = row.createCell(7);
					if (record.getR45_closing_bal_of_active_cards() != null) {
						R45cell4.setCellValue(record.getR45_closing_bal_of_active_cards().doubleValue());
						R45cell4.setCellStyle(numberStyle);
					} else {
						R45cell4.setCellValue("");
						R45cell4.setCellStyle(textStyle);
					}
					// R46 Col E
					row = sheet.getRow(45);
					Cell R46cell1 = row.createCell(4);
					if (record.getR46_opening_no_of_cards() != null) {
						R46cell1.setCellValue(record.getR46_opening_no_of_cards().doubleValue());
						R46cell1.setCellStyle(numberStyle);
					} else {
						R46cell1.setCellValue("");
						R46cell1.setCellStyle(textStyle);
					}

					// R46 Col F
					Cell R46cell2 = row.createCell(5);
					if (record.getR46_no_of_cards_issued() != null) {
						R46cell2.setCellValue(record.getR46_no_of_cards_issued().doubleValue());
						R46cell2.setCellStyle(numberStyle);
					} else {
						R46cell2.setCellValue("");
						R46cell2.setCellStyle(textStyle);
					}

					// R46 Col G
					Cell R46cell3 = row.createCell(6);
					if (record.getR46_no_cards_of_closed() != null) {
						R46cell3.setCellValue(record.getR46_no_cards_of_closed().doubleValue());
						R46cell3.setCellStyle(numberStyle);
					} else {
						R46cell3.setCellValue("");
						R46cell3.setCellStyle(textStyle);
					}
					// R46 Col H
					Cell R46cell4 = row.createCell(7);
					if (record.getR46_closing_bal_of_active_cards() != null) {
						R46cell4.setCellValue(record.getR46_closing_bal_of_active_cards().doubleValue());
						R46cell4.setCellStyle(numberStyle);
					} else {
						R46cell4.setCellValue("");
						R46cell4.setCellStyle(textStyle);
					}
					// R47 Col E
					row = sheet.getRow(46);
					Cell R47cell1 = row.createCell(4);
					if (record.getR47_opening_no_of_cards() != null) {
						R47cell1.setCellValue(record.getR47_opening_no_of_cards().doubleValue());
						R47cell1.setCellStyle(numberStyle);
					} else {
						R47cell1.setCellValue("");
						R47cell1.setCellStyle(textStyle);
					}

					// R47 Col F
					Cell R47cell2 = row.createCell(5);
					if (record.getR47_no_of_cards_issued() != null) {
						R47cell2.setCellValue(record.getR47_no_of_cards_issued().doubleValue());
						R47cell2.setCellStyle(numberStyle);
					} else {
						R47cell2.setCellValue("");
						R47cell2.setCellStyle(textStyle);
					}

					// R47 Col G
					Cell R47cell3 = row.createCell(6);
					if (record.getR47_no_cards_of_closed() != null) {
						R47cell3.setCellValue(record.getR47_no_cards_of_closed().doubleValue());
						R47cell3.setCellStyle(numberStyle);
					} else {
						R47cell3.setCellValue("");
						R47cell3.setCellStyle(textStyle);
					}

					// R48 Col E
					row = sheet.getRow(47);
					Cell R48cell1 = row.createCell(4);
					if (record.getR48_opening_no_of_cards() != null) {
						R48cell1.setCellValue(record.getR48_opening_no_of_cards().doubleValue());
						R48cell1.setCellStyle(numberStyle);
					} else {
						R48cell1.setCellValue("");
						R48cell1.setCellStyle(textStyle);
					}

					// R48 Col F
					Cell R48cell2 = row.createCell(5);
					if (record.getR48_no_of_cards_issued() != null) {
						R48cell2.setCellValue(record.getR48_no_of_cards_issued().doubleValue());
						R48cell2.setCellStyle(numberStyle);
					} else {
						R48cell2.setCellValue("");
						R48cell2.setCellStyle(textStyle);
					}

					// R48 Col G
					Cell R48cell3 = row.createCell(6);
					if (record.getR48_no_cards_of_closed() != null) {
						R48cell3.setCellValue(record.getR48_no_cards_of_closed().doubleValue());
						R48cell3.setCellStyle(numberStyle);
					} else {
						R48cell3.setCellValue("");
						R48cell3.setCellStyle(textStyle);
					}

					// R49 Col E
					row = sheet.getRow(48);
					Cell R49cell1 = row.createCell(4);
					if (record.getR49_opening_no_of_cards() != null) {
						R49cell1.setCellValue(record.getR49_opening_no_of_cards().doubleValue());
						R49cell1.setCellStyle(numberStyle);
					} else {
						R49cell1.setCellValue("");
						R49cell1.setCellStyle(textStyle);
					}

					// R49 Col F
					Cell R49cell2 = row.createCell(5);
					if (record.getR49_no_of_cards_issued() != null) {
						R49cell2.setCellValue(record.getR49_no_of_cards_issued().doubleValue());
						R49cell2.setCellStyle(numberStyle);
					} else {
						R49cell2.setCellValue("");
						R49cell2.setCellStyle(textStyle);
					}

					// R49 Col G
					Cell R49cell3 = row.createCell(6);
					if (record.getR49_no_cards_of_closed() != null) {
						R49cell3.setCellValue(record.getR49_no_cards_of_closed().doubleValue());
						R49cell3.setCellStyle(numberStyle);
					} else {
						R49cell3.setCellValue("");
						R49cell3.setCellStyle(textStyle);
					}

					// TABLE 4
					// R55 Col E
					row = sheet.getRow(54);
					Cell R55cell1 = row.createCell(4);
					if (record.getR55_opening_no_of_cards() != null) {
						R55cell1.setCellValue(record.getR55_opening_no_of_cards().doubleValue());
						R55cell1.setCellStyle(numberStyle);
					} else {
						R55cell1.setCellValue("");
						R55cell1.setCellStyle(textStyle);
					}

					// R55 Col F
					Cell R55cell2 = row.createCell(5);
					if (record.getR55_no_of_cards_issued() != null) {
						R55cell2.setCellValue(record.getR55_no_of_cards_issued().doubleValue());
						R55cell2.setCellStyle(numberStyle);
					} else {
						R55cell2.setCellValue("");
						R55cell2.setCellStyle(textStyle);
					}

					// R55 Col G
					Cell R55cell3 = row.createCell(6);
					if (record.getR55_no_cards_of_closed() != null) {
						R55cell3.setCellValue(record.getR55_no_cards_of_closed().doubleValue());
						R55cell3.setCellStyle(numberStyle);
					} else {
						R55cell3.setCellValue("");
						R55cell3.setCellStyle(textStyle);
					}
					// R55 Col H
					Cell R55cell4 = row.createCell(7);
					if (record.getR55_closing_bal_of_active_cards() != null) {
						R55cell4.setCellValue(record.getR55_closing_bal_of_active_cards().doubleValue());
						R55cell4.setCellStyle(numberStyle);
					} else {
						R55cell4.setCellValue("");
						R55cell4.setCellStyle(textStyle);
					}
					// R56 Col E
					row = sheet.getRow(55);
					Cell R56cell1 = row.createCell(4);
					if (record.getR56_opening_no_of_cards() != null) {
						R56cell1.setCellValue(record.getR56_opening_no_of_cards().doubleValue());
						R56cell1.setCellStyle(numberStyle);
					} else {
						R56cell1.setCellValue("");
						R56cell1.setCellStyle(textStyle);
					}

					// R56 Col F
					Cell R56cell2 = row.createCell(5);
					if (record.getR56_no_of_cards_issued() != null) {
						R56cell2.setCellValue(record.getR56_no_of_cards_issued().doubleValue());
						R56cell2.setCellStyle(numberStyle);
					} else {
						R56cell2.setCellValue("");
						R56cell2.setCellStyle(textStyle);
					}

					// R56 Col G
					Cell R56cell3 = row.createCell(6);
					if (record.getR56_no_cards_of_closed() != null) {
						R56cell3.setCellValue(record.getR56_no_cards_of_closed().doubleValue());
						R56cell3.setCellStyle(numberStyle);
					} else {
						R56cell3.setCellValue("");
						R56cell3.setCellStyle(textStyle);
					}
					// R56 Col H
					Cell R56cell4 = row.createCell(7);
					if (record.getR56_closing_bal_of_active_cards() != null) {
						R56cell4.setCellValue(record.getR56_closing_bal_of_active_cards().doubleValue());
						R56cell4.setCellStyle(numberStyle);
					} else {
						R56cell4.setCellValue("");
						R56cell4.setCellStyle(textStyle);
					}
					// R57 Col E
					row = sheet.getRow(56);
					Cell R57cell1 = row.createCell(4);
					if (record.getR57_opening_no_of_cards() != null) {
						R57cell1.setCellValue(record.getR57_opening_no_of_cards().doubleValue());
						R57cell1.setCellStyle(numberStyle);
					} else {
						R57cell1.setCellValue("");
						R57cell1.setCellStyle(textStyle);
					}

					// R57 Col F
					Cell R57cell2 = row.createCell(5);
					if (record.getR57_no_of_cards_issued() != null) {
						R57cell2.setCellValue(record.getR57_no_of_cards_issued().doubleValue());
						R57cell2.setCellStyle(numberStyle);
					} else {
						R57cell2.setCellValue("");
						R57cell2.setCellStyle(textStyle);
					}

					// R57 Col G
					Cell R57cell3 = row.createCell(6);
					if (record.getR57_no_cards_of_closed() != null) {
						R57cell3.setCellValue(record.getR57_no_cards_of_closed().doubleValue());
						R57cell3.setCellStyle(numberStyle);
					} else {
						R57cell3.setCellValue("");
						R57cell3.setCellStyle(textStyle);
					}
					// R57 Col H
					Cell R57cell4 = row.createCell(7);
					if (record.getR57_closing_bal_of_active_cards() != null) {
						R57cell4.setCellValue(record.getR57_closing_bal_of_active_cards().doubleValue());
						R57cell4.setCellStyle(numberStyle);
					} else {
						R57cell4.setCellValue("");
						R57cell4.setCellStyle(textStyle);
					}
					// R58 Col E
					row = sheet.getRow(57);
					Cell R58cell1 = row.createCell(4);
					if (record.getR58_opening_no_of_cards() != null) {
						R58cell1.setCellValue(record.getR58_opening_no_of_cards().doubleValue());
						R58cell1.setCellStyle(numberStyle);
					} else {
						R58cell1.setCellValue("");
						R58cell1.setCellStyle(textStyle);
					}

					// R58 Col F
					Cell R58cell2 = row.createCell(5);
					if (record.getR58_no_of_cards_issued() != null) {
						R58cell2.setCellValue(record.getR58_no_of_cards_issued().doubleValue());
						R58cell2.setCellStyle(numberStyle);
					} else {
						R58cell2.setCellValue("");
						R58cell2.setCellStyle(textStyle);
					}

					// R58 Col G
					Cell R58cell3 = row.createCell(6);
					if (record.getR58_no_cards_of_closed() != null) {
						R58cell3.setCellValue(record.getR58_no_cards_of_closed().doubleValue());
						R58cell3.setCellStyle(numberStyle);
					} else {
						R58cell3.setCellValue("");
						R58cell3.setCellStyle(textStyle);
					}
					// R58 Col H
					Cell R58cell4 = row.createCell(7);
					if (record.getR58_closing_bal_of_active_cards() != null) {
						R58cell4.setCellValue(record.getR58_closing_bal_of_active_cards().doubleValue());
						R58cell4.setCellStyle(numberStyle);
					} else {
						R58cell4.setCellValue("");
						R58cell4.setCellStyle(textStyle);
					}
					// R59 Col E
					row = sheet.getRow(58);
					Cell R59cell1 = row.createCell(4);
					if (record.getR59_opening_no_of_cards() != null) {
						R59cell1.setCellValue(record.getR59_opening_no_of_cards().doubleValue());
						R59cell1.setCellStyle(numberStyle);
					} else {
						R59cell1.setCellValue("");
						R59cell1.setCellStyle(textStyle);
					}

					// R59 Col F
					Cell R59cell2 = row.createCell(5);
					if (record.getR59_no_of_cards_issued() != null) {
						R59cell2.setCellValue(record.getR59_no_of_cards_issued().doubleValue());
						R59cell2.setCellStyle(numberStyle);
					} else {
						R59cell2.setCellValue("");
						R59cell2.setCellStyle(textStyle);
					}

					// R59 Col G
					Cell R59cell3 = row.createCell(6);
					if (record.getR59_no_cards_of_closed() != null) {
						R59cell3.setCellValue(record.getR59_no_cards_of_closed().doubleValue());
						R59cell3.setCellStyle(numberStyle);
					} else {
						R59cell3.setCellValue("");
						R59cell3.setCellStyle(textStyle);
					}
					// R59 Col H
					Cell R59cell4 = row.createCell(7);
					if (record.getR59_closing_bal_of_active_cards() != null) {
						R59cell4.setCellValue(record.getR59_closing_bal_of_active_cards().doubleValue());
						R59cell4.setCellStyle(numberStyle);
					} else {
						R59cell4.setCellValue("");
						R59cell4.setCellStyle(textStyle);
					}

					// R60 Col E
					row = sheet.getRow(59);
					Cell R60cell1 = row.createCell(4);
					if (record.getR60_opening_no_of_cards() != null) {
						R60cell1.setCellValue(record.getR60_opening_no_of_cards().doubleValue());
						R60cell1.setCellStyle(numberStyle);
					} else {
						R60cell1.setCellValue("");
						R60cell1.setCellStyle(textStyle);
					}

					// R60 Col F
					Cell R60cell2 = row.createCell(5);
					if (record.getR60_no_of_cards_issued() != null) {
						R60cell2.setCellValue(record.getR60_no_of_cards_issued().doubleValue());
						R60cell2.setCellStyle(numberStyle);
					} else {
						R60cell2.setCellValue("");
						R60cell2.setCellStyle(textStyle);
					}
					// R60 Col G
					Cell R60cell3 = row.createCell(6);
					if (record.getR60_no_cards_of_closed() != null) {
						R60cell3.setCellValue(record.getR60_no_cards_of_closed().doubleValue());
						R60cell3.setCellStyle(numberStyle);
					} else {
						R60cell3.setCellValue("");
						R60cell3.setCellStyle(textStyle);
					}
					// R60 Col H
					Cell R60cell4 = row.createCell(7);
					if (record.getR60_closing_bal_of_active_cards() != null) {
						R60cell4.setCellValue(record.getR60_closing_bal_of_active_cards().doubleValue());
						R60cell4.setCellStyle(numberStyle);
					} else {
						R60cell4.setCellValue("");
						R60cell4.setCellStyle(textStyle);
					}
					// R61 Col E
					row = sheet.getRow(60);
					Cell R61cell1 = row.createCell(4);
					if (record.getR61_opening_no_of_cards() != null) {
						R61cell1.setCellValue(record.getR61_opening_no_of_cards().doubleValue());
						R61cell1.setCellStyle(numberStyle);
					} else {
						R61cell1.setCellValue("");
						R61cell1.setCellStyle(textStyle);
					}

					// R61 Col F
					Cell R61cell2 = row.createCell(5);
					if (record.getR61_no_of_cards_issued() != null) {
						R61cell2.setCellValue(record.getR61_no_of_cards_issued().doubleValue());
						R61cell2.setCellStyle(numberStyle);
					} else {
						R61cell2.setCellValue("");
						R61cell2.setCellStyle(textStyle);
					}
					// R61 Col G
					Cell R61cell3 = row.createCell(6);
					if (record.getR61_no_cards_of_closed() != null) {
						R61cell3.setCellValue(record.getR61_no_cards_of_closed().doubleValue());
						R61cell3.setCellStyle(numberStyle);
					} else {
						R61cell3.setCellValue("");
						R61cell3.setCellStyle(textStyle);
					}
					// R61 Col H
					Cell R61cell4 = row.createCell(7);
					if (record.getR61_closing_bal_of_active_cards() != null) {
						R61cell4.setCellValue(record.getR61_closing_bal_of_active_cards().doubleValue());
						R61cell4.setCellStyle(numberStyle);
					} else {
						R61cell4.setCellValue("");
						R61cell4.setCellStyle(textStyle);
					}
					// R62 Col E
					row = sheet.getRow(61);
					Cell R62cell1 = row.createCell(4);
					if (record.getR62_opening_no_of_cards() != null) {
						R62cell1.setCellValue(record.getR62_opening_no_of_cards().doubleValue());
						R62cell1.setCellStyle(numberStyle);
					} else {
						R62cell1.setCellValue("");
						R62cell1.setCellStyle(textStyle);
					}

					// R62 Col F
					Cell R62cell2 = row.createCell(5);
					if (record.getR62_no_of_cards_issued() != null) {
						R62cell2.setCellValue(record.getR62_no_of_cards_issued().doubleValue());
						R62cell2.setCellStyle(numberStyle);
					} else {
						R62cell2.setCellValue("");
						R62cell2.setCellStyle(textStyle);
					}
					// R62 Col G
					Cell R62cell3 = row.createCell(6);
					if (record.getR62_no_cards_of_closed() != null) {
						R62cell3.setCellValue(record.getR62_no_cards_of_closed().doubleValue());
						R62cell3.setCellStyle(numberStyle);
					} else {
						R62cell3.setCellValue("");
						R62cell3.setCellStyle(textStyle);
					}
					// R62 Col H
					Cell R62cell4 = row.createCell(7);
					if (record.getR62_closing_bal_of_active_cards() != null) {
						R62cell4.setCellValue(record.getR62_closing_bal_of_active_cards().doubleValue());
						R62cell4.setCellStyle(numberStyle);
					} else {
						R62cell4.setCellValue("");
						R62cell4.setCellStyle(textStyle);
					}
					// R63 Col E
					row = sheet.getRow(62);
					Cell R63cell1 = row.createCell(4);
					if (record.getR63_opening_no_of_cards() != null) {
						R63cell1.setCellValue(record.getR63_opening_no_of_cards().doubleValue());
						R63cell1.setCellStyle(numberStyle);
					} else {
						R63cell1.setCellValue("");
						R63cell1.setCellStyle(textStyle);
					}

					// R63 Col F
					Cell R63cell2 = row.createCell(5);
					if (record.getR63_no_of_cards_issued() != null) {
						R63cell2.setCellValue(record.getR63_no_of_cards_issued().doubleValue());
						R63cell2.setCellStyle(numberStyle);
					} else {
						R63cell2.setCellValue("");
						R63cell2.setCellStyle(textStyle);
					}
					// R63 Col G
					Cell R63cell3 = row.createCell(6);
					if (record.getR63_no_cards_of_closed() != null) {
						R63cell3.setCellValue(record.getR63_no_cards_of_closed().doubleValue());
						R63cell3.setCellStyle(numberStyle);
					} else {
						R63cell3.setCellValue("");
						R63cell3.setCellStyle(textStyle);
					}
					// R63 Col H
					Cell R63cell4 = row.createCell(7);
					if (record.getR63_closing_bal_of_active_cards() != null) {
						R63cell4.setCellValue(record.getR63_closing_bal_of_active_cards().doubleValue());
						R63cell4.setCellStyle(numberStyle);
					} else {
						R63cell4.setCellValue("");
						R63cell4.setCellStyle(textStyle);
					}
					// R64 Col E
					row = sheet.getRow(63);
					Cell R64cell1 = row.createCell(4);
					if (record.getR64_opening_no_of_cards() != null) {
						R64cell1.setCellValue(record.getR64_opening_no_of_cards().doubleValue());
						R64cell1.setCellStyle(numberStyle);
					} else {
						R64cell1.setCellValue("");
						R64cell1.setCellStyle(textStyle);
					}

					// R64 Col F
					Cell R64cell2 = row.createCell(5);
					if (record.getR64_no_of_cards_issued() != null) {
						R64cell2.setCellValue(record.getR64_no_of_cards_issued().doubleValue());
						R64cell2.setCellStyle(numberStyle);
					} else {
						R64cell2.setCellValue("");
						R64cell2.setCellStyle(textStyle);
					}
					// R64 Col G
					Cell R64cell3 = row.createCell(6);
					if (record.getR64_no_cards_of_closed() != null) {
						R64cell3.setCellValue(record.getR64_no_cards_of_closed().doubleValue());
						R64cell3.setCellStyle(numberStyle);
					} else {
						R64cell3.setCellValue("");
						R64cell3.setCellStyle(textStyle);
					}
					// R64 Col H
					Cell R64cell4 = row.createCell(7);
					if (record.getR64_closing_bal_of_active_cards() != null) {
						R64cell4.setCellValue(record.getR64_closing_bal_of_active_cards().doubleValue());
						R64cell4.setCellStyle(numberStyle);
					} else {
						R64cell4.setCellValue("");
						R64cell4.setCellStyle(textStyle);
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
