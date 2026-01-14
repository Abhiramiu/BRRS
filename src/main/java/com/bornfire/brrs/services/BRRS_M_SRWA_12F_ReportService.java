package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BDISB1_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BDISB1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BDISB1_Detail_Entity;
import com.bornfire.brrs.entities.BDISB1_Summary_Entity;
import com.bornfire.brrs.entities.BDISB2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BDISB2_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Detail_Repo;
import com.bornfire.brrs.entities.M_SRWA_12F_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12H_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Summary_Repo;
import com.bornfire.brrs.entities.M_INT_RATES_FCA_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_INT_RATES_FCA_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12F_Detail_Entity;

import java.math.BigDecimal;

@Component
@Service

public class BRRS_M_SRWA_12F_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12F_ReportService.class);

	@Autowired
	private Environment env;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_SRWA_12F_Summary_Repo M_SRWA_12F_Summary_Repo;

	@Autowired
	BRRS_M_SRWA_12F_Archival_Summary_Repo M_SRWA_12F_Archival_Summary_Repo;
	
	@Autowired
	BRRS_M_SRWA_12F_Detail_Repo M_SRWA_12F_Detail_Repo;

	@Autowired
	BRRS_M_SRWA_12F_Archival_Detail_Repo M_SRWA_12F_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SRWA12FView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<M_SRWA_12F_Archival_Summary_Entity> T1Master = M_SRWA_12F_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_SRWA_12F_Archival_Summary_Entity> T1Master = M_SRWA_12F_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_SRWA_12F_Summary_Entity> T1Master = M_SRWA_12F_Summary_Repo
						.getdatabydateList(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("reportsummary", T1Master);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12F");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}
	
	public ModelAndView getM_SRWA12FcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version) {

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

			// ✅ Split filter string into rowId & columnId
			if (Filter != null && Filter.contains(",")) {
				String[] parts = Filter.split(",");
				if (parts.length >= 2) {
					rowId = parts[0];
					columnId = parts[1];
				}
			}
			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<M_SRWA_12F_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = M_SRWA_12F_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate,
							version);
				} else {
					T1Dt1 = M_SRWA_12F_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_SRWA_12F_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = M_SRWA_12F_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = M_SRWA_12F_Detail_Repo.getdatabydateList(parsedDate);
					System.out.println("bdisb2 size is : " + T1Dt1.size());
					totalPages = M_SRWA_12F_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/M_SRWA_12F");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	
	public void updateDetailFromForm(Date reportDate, Map<String, String> params) {

	    System.out.println("Updating Corporate Risk detail table");

	    List<M_SRWA_12F_Detail_Entity> allModifiedRows = new ArrayList<>();

	    for (Map.Entry<String, String> entry : params.entrySet()) {

	        String key = entry.getKey();
	        String value = entry.getValue();

	        // ✅ Allow only valid column keys
	        if (!key.matches(
	                "R\\d+_C\\d+_(" +
	                        "NAME_OF_CORPORATE|" +
	                        "CREDIT_RATING|" +
	                        "RATING_AGENCY|" +
	                        "EXPOSURE_AMT|" +
	                        "RISK_WEIGHT|" +
	                        "RISK_WEIGHTED_AMT" +
	                        ")"
	        )) {
	            continue;
	        }

	        // 🔹 Parse key
	        String[] parts = key.split("_");
	        String reportLable = parts[0];      // R10, R11...
	        String addlCriteria = parts[1];     // C1, C2...
	        String columnName = key.replaceFirst("R\\d+_C\\d+_", "");

	        // 🔹 Fetch matching rows
	        List<M_SRWA_12F_Detail_Entity> rows =
	                M_SRWA_12F_Detail_Repo
	                        .findByReportDateAndReportLableAndReportAddlCriteria1(
	                                reportDate, reportLable, addlCriteria);

	        for (M_SRWA_12F_Detail_Entity row : rows) {

	            /* =======================
	               NUMERIC COLUMNS
	               ======================= */

	            if ("EXPOSURE_AMT".equals(columnName)) {

	                BigDecimal num = (value == null || value.trim().isEmpty())
	                        ? BigDecimal.ZERO
	                        : new BigDecimal(value.replace(",", ""));
	                row.setEXPOSURE_AMT(num);

	            } else if ("RISK_WEIGHT".equals(columnName)) {

	                BigDecimal num = (value == null || value.trim().isEmpty())
	                        ? BigDecimal.ZERO
	                        : new BigDecimal(value.replace(",", ""));
	                row.setRISK_WEIGHT(num);

	            } else if ("RISK_WEIGHTED_AMT".equals(columnName)) {

	                BigDecimal num = (value == null || value.trim().isEmpty())
	                        ? BigDecimal.ZERO
	                        : new BigDecimal(value.replace(",", ""));
	                row.setRISK_WEIGHTED_AMT(num);
	            }

	            /* =======================
	               STRING COLUMNS
	               ======================= */

	            else if ("NAME_OF_CORPORATE".equals(columnName)) {
	                row.setNAME_OF_CORPORATE(value);

	            } else if ("CREDIT_RATING".equals(columnName)) {
	                row.setCREDIT_RATING(value);

	            } else if ("RATING_AGENCY".equals(columnName)) {
	                row.setRATING_AGENCY(value);
	            }

	            // 🔹 Mark modified
	            row.setModifyFlg("Y");
	        }

	        allModifiedRows.addAll(rows);
	    }

	    if (!allModifiedRows.isEmpty()) {
	        M_SRWA_12F_Detail_Repo.saveAll(allModifiedRows);
	    }

	    callSummaryProcedure(reportDate);
	}
	
	
	private void callSummaryProcedure(Date reportDate) {

		String sql = "{ call BRRS_M_SRWA_12F_SUMMARY_PROCEDURE(?) }";

		jdbcTemplate.update(connection -> {
			CallableStatement cs = connection.prepareCall(sql);

			// Force exact format expected by procedure
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			sdf.setLenient(false);

			String formattedDate = sdf.format(reportDate);

			cs.setString(1, formattedDate); // 🔥 THIS IS MANDATORY
			return cs;
		});

		System.out.println(
				"✅ Summary procedure executed for date: " + new SimpleDateFormat("dd-MM-yyyy").format(reportDate));
	}





//	public void updateReport(M_SRWA_12F_Summary_Entity updatedEntity) {

//	    System.out.println("Came to services");
//	    System.out.println("Report Date: " + updatedEntity.getReportDate());

//	    M_SRWA_12F_Summary_Entity existing =
//	            M_SRWA_12F_Summary_Repo.findTopByReportDateOrderByReportVersionDesc(updatedEntity.getReportDate())
//	                    .orElseThrow(() ->
//	                            new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

//	    try {
	        // 1️⃣ Update R14 to R36 (based on your loop)
//	        for (int i = 11; i <= 36; i++) {
//	            String prefix = "R" + i + "_";

//	            String[] fields = {
//	                    "NAME_OF_CORPORATE",
//	                    "CREDIT_RATING",
//	                    "RATING_AGENCY",
//	                    "EXPOSURE_AMT",
//	                    "RISK_WEIGHT",
//	                    "RISK_WEIGHTED_AMT"
//	            };

//	            for (String field : fields) {
//	                String getterName = "get" + prefix + field;
//	                String setterName = "set" + prefix + field;

//	                try {
//	                    Method getter = M_SRWA_12F_Summary_Entity.class.getMethod(getterName);
//	                    Method setter = M_SRWA_12F_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

//	                    Object newValue = getter.invoke(updatedEntity);

	                    // ✔ Prevent null overwriting if updatedEntity does not send the field
//	                    if (newValue != null) {
//	                        setter.invoke(existing, newValue);
//	                    }

//	                } catch (NoSuchMethodException e) {
//	                    System.out.println("Skipping missing field: " + setterName);
//	                }
//	            }
//	        }

	        // 2️⃣ Handle R37 TOTAL
//	        String prefix = "R37_";
//	        String[] totalFields = {
//	                "NAME_OF_CORPORATE",
//	                "CREDIT_RATING",
//	                "RATING_AGENCY",
//	                "EXPOSURE_AMT",
//	                "RISK_WEIGHT",
//	                "RISK_WEIGHTED_AMT"
//	        };

//	        for (String field : totalFields) {

//	            String getterName = "get" + prefix + field;
//	            String setterName = "set" + prefix + field;

//	            try {
//	                Method getter = M_SRWA_12F_Summary_Entity.class.getMethod(getterName);
//	                Method setter = M_SRWA_12F_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

//	                Object newValue = getter.invoke(updatedEntity);

//	                if (newValue != null) {
//	                    setter.invoke(existing, newValue);
//	                }

//	            } catch (NoSuchMethodException e) {
//	                System.out.println("Skipping missing total field: " + setterName);
//	            }
//	        }

//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }

//	    System.out.println("Saving updated entity");
//	    M_SRWA_12F_Summary_Repo.save(existing);
//	}


	public byte[] getM_SRWA_12FExcel(String filename, String reportId, String fromdate, String todate, String currency,
									 String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

    // ARCHIVAL check
    if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating ARCHIVAL report for version {}", version);
        return getExcelM_SRWA_12FARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }
    // RESUB check
    else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
        logger.info("Service: Generating RESUB report for version {}", version);

       
        List<M_SRWA_12F_Archival_Summary_Entity> T1Master =
                M_SRWA_12F_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

        // Generate Excel for RESUB
        return BRRS_M_SRWA_12FResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
    }


		// Default (LIVE) case
		List<M_SRWA_12F_Summary_Entity> dataList1 = M_SRWA_12F_Summary_Repo.getdatabydateList(reportDate);

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
			font.setFontHeightInPoints((short)8); // size 8
			font.setFontName("Arial");    

			CellStyle numberStyle = workbook.createCellStyle();
			//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			int startRow = 10;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {

					M_SRWA_12F_Summary_Entity record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR11_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR11_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR12_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR12_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR13_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR13_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR14_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR14_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR15_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR15_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR16_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR16_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR17_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR17_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR18_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR18_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR19_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR19_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR20_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR20_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR21_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR21_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR22_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR22_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR23_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR23_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR24_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR24_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR25_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR25_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR26_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR26_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR27_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR27_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR28_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR28_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR29_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR29_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR30_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR30_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record.getR30_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR30_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR31_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR31_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR32_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR32_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR33_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR33_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR33_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR33_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR34_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row34
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR34_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR35_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row35
					// Column F
					cell5 = row.createCell(5);
					if (record.getR35_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR35_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR36_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR36_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record.getR36_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR36_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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

//	public List<Object> getM_SRWA12FArchival() {
//		List<Object> M_SRWA12FArchivallist = new ArrayList<>();
//		List<Object> M_FXRArchivallist2 = new ArrayList<>();
//		List<Object> M_FXRArchivallist3 = new ArrayList<>();
//		try {
//			M_SRWA12FArchivallist = M_SRWA_12F_Archival_Summary_Repo.getM_SRWA12Farchival();

//			System.out.println("countser" + M_SRWA12FArchivallist.size());
//			System.out.println("countser" + M_FXRArchivallist.size());
//			System.out.println("countser" + M_FXRArchivallist.size());
//		} catch (Exception e) {
			// Log the exception
//			System.err.println("Error fetching M_SECL Archival data: " + e.getMessage());
//			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
	//	}
	//	return M_SRWA12FArchivallist;
//	}

	public byte[] getExcelM_SRWA_12FARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<M_SRWA_12F_Archival_Summary_Entity> dataList1 = M_SRWA_12F_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);
		

			
		
		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12G report. Returning empty result.");
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

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {

					M_SRWA_12F_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record1.getR11_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR11_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record1.getR11_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR11_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR12_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR12_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR12_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR12_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR13_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR13_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR13_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR13_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR14_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR14_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR14_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR14_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR15_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR15_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR15_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR15_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR16_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR16_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row16
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR16_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR16_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR17_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR17_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row17
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR17_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR17_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR18_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR18_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row18
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR18_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR18_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR19_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR19_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row19
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR19_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR19_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR20_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR20_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR20_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR20_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR21_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR21_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR21_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR21_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR22_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR22_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR22_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR22_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR23_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR23_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR23_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR23_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR24_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR24_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR24_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR24_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR25_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR25_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR25_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR25_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR26_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR26_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR26_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR26_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR27_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR27_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR27_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR27_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR28_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR28_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR28_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR28_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR29_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR29_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR29_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR29_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR30_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR30_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR30_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR30_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR31_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR31_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row31
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR31_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR31_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR32_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR32_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR32_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR32_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR33_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR33_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR33_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR33_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR34_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR34_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row34
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR34_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR34_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR35_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR35_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row35
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR35_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR35_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR36_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR36_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR36_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR36_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
public List<Object[]> getM_SRWA_12FResub() {
List<Object[]> resubList = new ArrayList<>();
try {
List<M_SRWA_12F_Archival_Summary_Entity> latestArchivalList = M_SRWA_12F_Archival_Summary_Repo
.getdatabydateListWithVersionAll();

if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
for (M_SRWA_12F_Archival_Summary_Entity entity : latestArchivalList) {
Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion() };
resubList.add(row);
}
System.out.println("Fetched " + resubList.size() + " record(s)");
} else {
System.out.println("No archival data found.");
}
} catch (Exception e) {
System.err.println("Error fetching M_SRWA_12F Resub data: " + e.getMessage());
e.printStackTrace();
}
return resubList;
}

//Archival View
public List<Object[]> getM_SRWA_12FArchival() {
List<Object[]> archivalList = new ArrayList<>();

try {
List<M_SRWA_12F_Archival_Summary_Entity> repoData = M_SRWA_12F_Archival_Summary_Repo
.getdatabydateListWithVersionAll();

if (repoData != null && !repoData.isEmpty()) {
for (M_SRWA_12F_Archival_Summary_Entity entity : repoData) {
Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion() };
archivalList.add(row);
}

System.out.println("Fetched " + archivalList.size() + " archival records");
M_SRWA_12F_Archival_Summary_Entity first = repoData.get(0);
System.out.println("Latest archival version: " + first.getReportVersion());
} else {
System.out.println("No archival data found.");
}

} catch (Exception e) {
System.err.println("Error fetching BDISB1 Archival data: " + e.getMessage());
e.printStackTrace();
}

return archivalList;
}

@Transactional
public void updateReportReSub(BDISB1_Summary_Entity updatedEntity) {

System.out.println("Came to Resub Service");

Date reportDate = updatedEntity.getReportDate();
System.out.println("Report Date: " + reportDate);

try {

/* =========================================================
* 1️⃣ FETCH LATEST ARCHIVAL VERSION
* ========================================================= */
Optional<M_SRWA_12F_Archival_Summary_Entity> latestArchivalOpt =
M_SRWA_12F_Archival_Summary_Repo
.getLatestArchivalVersionByDate(reportDate);

int newVersion = 1;
if (latestArchivalOpt.isPresent()) {
try {
newVersion =
Integer.parseInt(latestArchivalOpt.get().getReportVersion()) + 1;
} catch (NumberFormatException e) {
newVersion = 1;
}
}

boolean exists =
M_SRWA_12F_Archival_Summary_Repo
.findByReportDateAndReportVersion(
reportDate, String.valueOf(newVersion))
.isPresent();

if (exists) {
throw new RuntimeException(
"Version " + newVersion + " already exists for report date " + reportDate);
}

/* =========================================================
* 2️⃣ CREATE NEW ARCHIVAL ENTITY (BASE COPY)
* ========================================================= */
M_SRWA_12F_Archival_Summary_Entity archivalEntity =
new M_SRWA_12F_Archival_Summary_Entity();

if (latestArchivalOpt.isPresent()) {
BeanUtils.copyProperties(latestArchivalOpt.get(), archivalEntity);
}

archivalEntity.setReportDate(reportDate);
archivalEntity.setReportVersion(String.valueOf(newVersion));
archivalEntity.setModify_flg("Y");

/* =========================================================
* 3️⃣ READ RAW REQUEST PARAMETERS
* ========================================================= */
HttpServletRequest request =
((ServletRequestAttributes) RequestContextHolder
.getRequestAttributes()).getRequest();

Map<String, String[]> parameterMap = request.getParameterMap();

for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

String key = entry.getKey();        // R5_C11_FIRST_NAME
String value = entry.getValue()[0];

// Ignore non-data params
if ("asondate".equalsIgnoreCase(key)
|| "type".equalsIgnoreCase(key)) {
continue;
}

// Normalize: R5_C11_FIRST_NAME → R5_FIRST_NAME
String normalizedKey = key.replaceFirst("_C\\d+_", "_");

/* =====================================================
* 4️⃣ APPLY VALUES (EXPLICIT MAPPING)
* ===================================================== */

// ======================= R5 =======================

if ("R11_NAME_OF_CORPORATE".equals(normalizedKey)) {
archivalEntity.setR11_NAME_OF_CORPORATE(value);

} else if ("R11_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR11_CREDIT_RATING(value);

} else if ("R11_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR11_RATING_AGENCY(value);

} else if ("R11_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR11_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R11_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR11_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R11_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR11_RISK_WEIGHTED_AMT(parseBigDecimal(value));



} else if ("R12_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR12_NAME_OF_CORPORATE(value);

} else if ("R12_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR12_CREDIT_RATING(value);

} else if ("R12_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR12_RATING_AGENCY(value);

} else if ("R12_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR12_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R12_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR12_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R12_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR12_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R13_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR13_NAME_OF_CORPORATE(value);

} else if ("R13_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR13_CREDIT_RATING(value);

} else if ("R13_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR13_RATING_AGENCY(value);

} else if ("R13_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR13_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R13_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR13_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R13_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR13_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R14_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR14_NAME_OF_CORPORATE(value);

} else if ("R14_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR14_CREDIT_RATING(value);

} else if ("R14_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR14_RATING_AGENCY(value);

} else if ("R14_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR14_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R14_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR14_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R14_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR14_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R15_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR15_NAME_OF_CORPORATE(value);

} else if ("R15_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR15_CREDIT_RATING(value);

} else if ("R15_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR15_RATING_AGENCY(value);

} else if ("R15_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR15_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R15_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR15_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R15_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR15_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R16_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR16_NAME_OF_CORPORATE(value);

} else if ("R16_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR16_CREDIT_RATING(value);

} else if ("R16_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR16_RATING_AGENCY(value);

} else if ("R16_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR16_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R16_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR16_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R16_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR16_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R17_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR17_NAME_OF_CORPORATE(value);

} else if ("R17_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR17_CREDIT_RATING(value);

} else if ("R17_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR17_RATING_AGENCY(value);

} else if ("R17_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR17_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R17_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR17_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R17_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR17_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R18_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR18_NAME_OF_CORPORATE(value);

} else if ("R18_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR18_CREDIT_RATING(value);

} else if ("R18_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR18_RATING_AGENCY(value);

} else if ("R18_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR18_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R18_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR18_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R18_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR18_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R19_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR19_NAME_OF_CORPORATE(value);

} else if ("R19_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR19_CREDIT_RATING(value);

} else if ("R19_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR19_RATING_AGENCY(value);

} else if ("R19_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR19_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R19_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR19_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R19_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR19_RISK_WEIGHTED_AMT(parseBigDecimal(value));



} else if ("R20_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR20_NAME_OF_CORPORATE(value);

} else if ("R20_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR20_CREDIT_RATING(value);

} else if ("R20_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR20_RATING_AGENCY(value);

} else if ("R20_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR20_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R20_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR20_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R20_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR20_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R21_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR21_NAME_OF_CORPORATE(value);

} else if ("R21_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR21_CREDIT_RATING(value);

} else if ("R21_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR21_RATING_AGENCY(value);

} else if ("R21_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR21_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R21_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR21_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R21_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR21_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R22_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR22_NAME_OF_CORPORATE(value);

} else if ("R22_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR22_CREDIT_RATING(value);

} else if ("R22_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR22_RATING_AGENCY(value);

} else if ("R22_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR22_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R22_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR22_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R22_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR22_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R23_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR23_NAME_OF_CORPORATE(value);

} else if ("R23_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR23_CREDIT_RATING(value);

} else if ("R23_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR23_RATING_AGENCY(value);

} else if ("R23_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR23_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R23_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR23_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R23_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR23_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R24_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR24_NAME_OF_CORPORATE(value);

} else if ("R24_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR24_CREDIT_RATING(value);

} else if ("R24_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR24_RATING_AGENCY(value);

} else if ("R24_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR24_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R24_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR24_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R24_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR24_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R25_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR25_NAME_OF_CORPORATE(value);

} else if ("R25_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR25_CREDIT_RATING(value);

} else if ("R25_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR25_RATING_AGENCY(value);

} else if ("R25_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR25_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R25_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR25_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R25_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR25_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R26_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR26_NAME_OF_CORPORATE(value);

} else if ("R26_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR26_CREDIT_RATING(value);

} else if ("R26_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR26_RATING_AGENCY(value);

} else if ("R26_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR26_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R26_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR26_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R26_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR26_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R27_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR27_NAME_OF_CORPORATE(value);

} else if ("R27_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR27_CREDIT_RATING(value);

} else if ("R27_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR27_RATING_AGENCY(value);

} else if ("R27_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR27_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R27_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR27_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R27_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR27_RISK_WEIGHTED_AMT(parseBigDecimal(value));



} else if ("R28_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR28_NAME_OF_CORPORATE(value);

} else if ("R28_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR28_CREDIT_RATING(value);

} else if ("R28_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR28_RATING_AGENCY(value);

} else if ("R28_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR28_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R28_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR28_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R28_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR28_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R29_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR29_NAME_OF_CORPORATE(value);

} else if ("R29_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR29_CREDIT_RATING(value);

} else if ("R29_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR29_RATING_AGENCY(value);

} else if ("R29_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR29_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R29_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR29_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R29_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR29_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R30_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR30_NAME_OF_CORPORATE(value);

} else if ("R30_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR30_CREDIT_RATING(value);

} else if ("R30_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR30_RATING_AGENCY(value);

} else if ("R30_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR30_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R30_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR30_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R30_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR30_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R31_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR31_NAME_OF_CORPORATE(value);

} else if ("R31_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR31_CREDIT_RATING(value);

} else if ("R31_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR31_RATING_AGENCY(value);

} else if ("R31_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR31_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R31_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR31_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R31_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR31_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R32_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR32_NAME_OF_CORPORATE(value);

} else if ("R32_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR32_CREDIT_RATING(value);

} else if ("R32_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR32_RATING_AGENCY(value);

} else if ("R32_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR32_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R32_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR32_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R32_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR32_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R33_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR33_NAME_OF_CORPORATE(value);

} else if ("R33_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR33_CREDIT_RATING(value);

} else if ("R33_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR33_RATING_AGENCY(value);

} else if ("R33_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR33_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R33_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR33_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R33_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR33_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R34_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR34_NAME_OF_CORPORATE(value);

} else if ("R34_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR34_CREDIT_RATING(value);

} else if ("R34_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR34_RATING_AGENCY(value);

} else if ("R34_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR34_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R34_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR34_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R34_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR34_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R35_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR35_NAME_OF_CORPORATE(value);

} else if ("R35_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR35_CREDIT_RATING(value);

} else if ("R35_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR35_RATING_AGENCY(value);

} else if ("R35_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR35_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R35_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR35_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R35_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR35_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R36_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR36_NAME_OF_CORPORATE(value);

} else if ("R36_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR36_CREDIT_RATING(value);

} else if ("R36_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR36_RATING_AGENCY(value);

} else if ("R36_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR36_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R36_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR36_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R36_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR36_RISK_WEIGHTED_AMT(parseBigDecimal(value));




} else if ("R37_NAME_OF_CORPORATE".equals(normalizedKey)) {
    archivalEntity.setR37_NAME_OF_CORPORATE(value);

} else if ("R37_CREDIT_RATING".equals(normalizedKey)) {
    archivalEntity.setR37_CREDIT_RATING(value);

} else if ("R37_RATING_AGENCY".equals(normalizedKey)) {
    archivalEntity.setR37_RATING_AGENCY(value);

} else if ("R37_EXPOSURE_AMT".equals(normalizedKey)) {
    archivalEntity.setR37_EXPOSURE_AMT(parseBigDecimal(value));

} else if ("R37_RISK_WEIGHT".equals(normalizedKey)) {
    archivalEntity.setR37_RISK_WEIGHT(parseBigDecimal(value));

} else if ("R37_RISK_WEIGHTED_AMT".equals(normalizedKey)) {
    archivalEntity.setR37_RISK_WEIGHTED_AMT(parseBigDecimal(value));
}


}

/* =========================================================
* 5️⃣ SET RESUB METADATA
* ========================================================= */
archivalEntity.setReportDate(reportDate);
archivalEntity.setReportVersion(String.valueOf(newVersion));
archivalEntity.setReportResubDate(new Date());

/* =========================================================
* 6️⃣ SAVE NEW ARCHIVAL VERSION
* ========================================================= */
M_SRWA_12F_Archival_Summary_Repo.save(archivalEntity);

System.out.println("✅ RESUB saved successfully. Version = " + newVersion);

} catch (Exception e) {
e.printStackTrace();
throw new RuntimeException(
"Error while creating archival resubmission record", e);
}
}

private BigDecimal parseBigDecimal(String value) {
return (value == null || value.trim().isEmpty())
? BigDecimal.ZERO
: new BigDecimal(value.replace(",", ""));
}



// Resubmit the values , latest version and Resub Date
public void updateReportReSub(M_SRWA_12F_Summary_Entity updatedEntity) {
System.out.println("Came to Resub Service");
System.out.println("Report Date: " + updatedEntity.getReportDate());

Date reportDate = updatedEntity.getReportDate();
int newVersion = 1;

try {
// Fetch the latest archival version for this report date
Optional<M_SRWA_12F_Archival_Summary_Entity> latestArchivalOpt = M_SRWA_12F_Archival_Summary_Repo
.getLatestArchivalVersionByDate(reportDate);

// Determine next version number
if (latestArchivalOpt.isPresent()) {
M_SRWA_12F_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
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
boolean exists = M_SRWA_12F_Archival_Summary_Repo
.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion))
.isPresent();

if (exists) {
throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
}

// Copy summary entity to archival entity
M_SRWA_12F_Archival_Summary_Entity archivalEntity = new M_SRWA_12F_Archival_Summary_Entity();
org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

archivalEntity.setReportDate(reportDate);
archivalEntity.setReportVersion(String.valueOf(newVersion));
archivalEntity.setReportResubDate(new Date());

System.out.println("Saving new archival version: " + newVersion);

// Save new version to repository
M_SRWA_12F_Archival_Summary_Repo.save(archivalEntity);

System.out.println(" Saved archival version successfully: " + newVersion);

} catch (Exception e) {
e.printStackTrace();
throw new RuntimeException("Error while creating archival resubmission record", e);
}
}

/// Downloaded for Archival & Resub
public byte[] BRRS_M_SRWA_12FResubExcel(String filename, String reportId, String fromdate,
String todate, String currency, String dtltype,
String type, String version) throws Exception {

logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

if (type.equals("RESUB") & version != null) {

}

List<M_SRWA_12F_Archival_Summary_Entity> dataList1 =
M_SRWA_12F_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

if (dataList1.isEmpty()) {
logger.warn("Service: No data found for M_SRWA_12G report. Returning empty result.");
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

if (!dataList1.isEmpty()) {
	for (int i = 0; i < dataList1.size(); i++) {

		M_SRWA_12F_Archival_Summary_Entity record1 = dataList1.get(i);
		System.out.println("rownumber=" + startRow + i);
		System.out.println("rownumber=" + startRow + i);
		Row row = sheet.getRow(startRow + i);
		if (row == null) {
			row = sheet.createRow(startRow + i);
		}

		// row11
		// Column E
		Cell cell4 = row.createCell(4);
		if (record1.getR11_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR11_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row11
		// Column F
		Cell cell5 = row.createCell(5);
		if (record1.getR11_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR11_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row12
		row = sheet.getRow(11);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR12_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR12_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row12
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR12_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR12_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row13
		row = sheet.getRow(12);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR13_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR13_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row13
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR13_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR13_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row14
		row = sheet.getRow(13);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR14_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR14_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row14
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR14_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR14_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row15
		row = sheet.getRow(14);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR15_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR15_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row15
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR15_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR15_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row16
		row = sheet.getRow(15);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR16_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR16_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row16
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR16_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR16_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row17
		row = sheet.getRow(16);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR17_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR17_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row17
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR17_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR17_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row18
		row = sheet.getRow(17);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR18_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR18_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row18
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR18_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR18_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row19
		row = sheet.getRow(18);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR19_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR19_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row19
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR19_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR19_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row20
		row = sheet.getRow(19);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR20_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR20_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row20
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR20_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR20_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row21
		row = sheet.getRow(20);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR21_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR21_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row21
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR21_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR21_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row22
		row = sheet.getRow(21);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR22_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR22_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row22
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR22_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR22_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row23
		row = sheet.getRow(22);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR23_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR23_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row23
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR23_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR23_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row24
		row = sheet.getRow(23);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR24_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR24_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row24
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR24_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR24_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row25
		row = sheet.getRow(24);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR25_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR25_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row25
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR25_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR25_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row26
		row = sheet.getRow(25);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR26_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR26_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row26
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR26_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR26_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row27
		row = sheet.getRow(26);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR27_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR27_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row27
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR27_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR27_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row28
		row = sheet.getRow(27);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR28_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR28_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row28
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR28_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR28_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row29
		row = sheet.getRow(28);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR29_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR29_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row29
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR29_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR29_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row30
		row = sheet.getRow(29);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR30_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR30_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row30
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR30_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR30_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row31
		row = sheet.getRow(30);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR31_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR31_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row31
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR31_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR31_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row32
		row = sheet.getRow(31);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR32_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR32_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row32
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR32_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR32_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row33
		row = sheet.getRow(32);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR33_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR33_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row33
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR33_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR33_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row34
		row = sheet.getRow(33);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR34_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR34_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row34
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR34_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR34_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row35
		row = sheet.getRow(34);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR35_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR35_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row35
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR35_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR35_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// row36
		row = sheet.getRow(35);
		// Column E
		cell4 = row.createCell(4);
		if (record1.getR36_EXPOSURE_AMT() != null) {
			cell4.setCellValue(record1.getR36_EXPOSURE_AMT().doubleValue());
			cell4.setCellStyle(numberStyle);
		} else {
			cell4.setCellValue("");
			cell4.setCellStyle(textStyle);
		}

		// row36
		// Column F
		cell5 = row.createCell(5);
		if (record1.getR36_RISK_WEIGHT() != null) {
			cell5.setCellValue(record1.getR36_RISK_WEIGHT().doubleValue());
			cell5.setCellStyle(numberStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
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

public byte[] getM_SRWA_12FDetailExcel(String filename, String fromdate, String todate,
        String currency, String dtltype, String type, String version) {

    try {
        logger.info("Generating Excel for M_SRWA_12F Details...");
        System.out.println("came to Detail download service");

        // ================= ARCHIVAL HANDLING =================
        if ("ARCHIVAL".equals(type) && version != null) {
            return getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type, version);
        }

        // ================= WORKBOOK & SHEET =================
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("M_SRWA_12FDetail");

        BorderStyle border = BorderStyle.THIN;

        // ================= HEADER STYLE =================
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

        CellStyle rightHeaderStyle = workbook.createCellStyle();
        rightHeaderStyle.cloneStyleFrom(headerStyle);
        rightHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

        // ================= DATA STYLES =================
        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setAlignment(HorizontalAlignment.LEFT);
        textStyle.setBorderTop(border);
        textStyle.setBorderBottom(border);
        textStyle.setBorderLeft(border);
        textStyle.setBorderRight(border);

        CellStyle amountStyle = workbook.createCellStyle();
        amountStyle.setAlignment(HorizontalAlignment.RIGHT);
        amountStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
        amountStyle.setBorderTop(border);
        amountStyle.setBorderBottom(border);
        amountStyle.setBorderLeft(border);
        amountStyle.setBorderRight(border);

        // ================= HEADER ROW =================
        String[] headers = {
        		"NAME_OF_CORPORATE",
        		"EXPOSURE_AMT",
        		"RISK_WEIGHT",
                "REPORT LABEL",
                "REPORT ADDL CRITERIA1",
                "REPORT DATE"
        };

        XSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle((i == 0 || i == 1) ? rightHeaderStyle : headerStyle);
            sheet.setColumnWidth(i, 6000);
        }

        // ================= DATA FETCH =================
        Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
        List<M_SRWA_12F_Detail_Entity> reportData = M_SRWA_12F_Detail_Repo.getdatabydateList(parsedToDate);

        // ================= DATA ROWS =================
        int rowIndex = 1;

        if (reportData != null && !reportData.isEmpty()) {
            for (M_SRWA_12F_Detail_Entity item : reportData) {

                XSSFRow row = sheet.createRow(rowIndex++);

                Cell c0 = row.createCell(0);
                c0.setCellValue(item.getNAME_OF_CORPORATE());
                c0.setCellStyle(textStyle);

                // Column 1 - COMPENSATABLE AMOUNT
                Cell c1 = row.createCell(1);
                c1.setCellValue(item.getEXPOSURE_AMT() != null
                        ? item.getEXPOSURE_AMT().doubleValue() : 0);
                c1.setCellStyle(amountStyle);
                
                Cell c2 = row.createCell(2);
                c2.setCellValue(item.getRISK_WEIGHT() != null
                        ? item.getRISK_WEIGHT().doubleValue() : 0);
                c2.setCellStyle(amountStyle);

                // Column 2 - REPORT LABEL
                Cell c3 = row.createCell(3);
                c3.setCellValue(item.getReportLable());
                c3.setCellStyle(textStyle);

                // Column 3 - REPORT ADDL CRITERIA 1
                Cell c4 = row.createCell(4);
                c4.setCellValue(item.getReportAddlCriteria1());
                c4.setCellStyle(textStyle);

                // Column 4 - REPORT DATE
                Cell c5 = row.createCell(5);
                c5.setCellValue(item.getReportDate() != null
                        ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
                        : "");
                c5.setCellStyle(textStyle);
            }
        } else {
            logger.info("No data found for M_SRWA_12F — only header written.");
        }

        // ================= WRITE FILE =================
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        logger.info("Excel generation completed with {} row(s).",
                reportData != null ? reportData.size() : 0);

        return bos.toByteArray();

    } catch (Exception e) {
        logger.error("Error generating BDISB3 Excel", e);
        return new byte[0];
    }
}

public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate,
        String currency, String dtltype, String type, String version) {

    try {
        logger.info("Generating Excel for BRRS_M_SRWA_12F ARCHIVAL Details...");
        System.out.println("came to Detail download service");

        // ================= WORKBOOK & SHEET =================
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("M_SRWA_12F");

        BorderStyle border = BorderStyle.THIN;

        // ================= HEADER STYLE =================
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

        CellStyle rightHeaderStyle = workbook.createCellStyle();
        rightHeaderStyle.cloneStyleFrom(headerStyle);
        rightHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

        // ================= DATA STYLES =================
        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setAlignment(HorizontalAlignment.LEFT);
        textStyle.setBorderTop(border);
        textStyle.setBorderBottom(border);
        textStyle.setBorderLeft(border);
        textStyle.setBorderRight(border);

        CellStyle amountStyle = workbook.createCellStyle();
        amountStyle.setAlignment(HorizontalAlignment.RIGHT);
        amountStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
        amountStyle.setBorderTop(border);
        amountStyle.setBorderBottom(border);
        amountStyle.setBorderLeft(border);
        amountStyle.setBorderRight(border);

        // ================= HEADER ROW =================
        String[] headers = {
        		"NAME_OF_CORPORATE",
        		"EXPOSURE_AMT",
        		"RISK_WEIGHT",
                "REPORT LABEL",
                "REPORT ADDL CRITERIA1",
                "REPORT DATE"
        };

        XSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle((i == 0 || i == 1) ? rightHeaderStyle : headerStyle);
            sheet.setColumnWidth(i, 6000);
        }

        // ================= DATA FETCH =================
        Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
        List<M_SRWA_12F_Archival_Detail_Entity> reportData =
                M_SRWA_12F_Archival_Detail_Repo.getdatabydateList(parsedToDate, version);

        // ================= DATA ROWS =================
        int rowIndex = 1;

        if (reportData != null && !reportData.isEmpty()) {
            for (M_SRWA_12F_Archival_Detail_Entity item : reportData) {

                XSSFRow row = sheet.createRow(rowIndex++);

             // Column 0 - AGGREGATE BALANCE
                Cell c0 = row.createCell(0);
                c0.setCellValue(item.getNAME_OF_CORPORATE());
                c0.setCellStyle(textStyle);

                // Column 1 - COMPENSATABLE AMOUNT
                Cell c1 = row.createCell(1);
                c1.setCellValue(item.getEXPOSURE_AMT() != null
                        ? item.getEXPOSURE_AMT().doubleValue() : 0);
                c1.setCellStyle(amountStyle);
                
                Cell c2 = row.createCell(2);
                c2.setCellValue(item.getRISK_WEIGHT() != null
                        ? item.getRISK_WEIGHT().doubleValue() : 0);
                c2.setCellStyle(amountStyle);

                // Column 2 - REPORT LABEL
                Cell c3 = row.createCell(3);
                c3.setCellValue(item.getReportLable());
                c3.setCellStyle(textStyle);

                // Column 3 - REPORT ADDL CRITERIA 1
                Cell c4 = row.createCell(4);
                c4.setCellValue(item.getReportAddlCriteria1());
                c4.setCellStyle(textStyle);

                // Column 4 - REPORT DATE
                Cell c5 = row.createCell(5);
                c5.setCellValue(item.getReportDate() != null
                        ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
                        : "");
                c5.setCellStyle(textStyle);
            }
        } else {
            logger.info("No archival data found for M_SRWA_12F — only header written.");
        }

        // ================= WRITE FILE =================
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        logger.info("ARCHIVAL Excel generation completed with {} row(s).",
                reportData != null ? reportData.size() : 0);

        return bos.toByteArray();

    } catch (Exception e) {
        logger.error("Error generating M_SRWA_12F ARCHIVAL Excel", e);
        return new byte[0];
    }
}

}




