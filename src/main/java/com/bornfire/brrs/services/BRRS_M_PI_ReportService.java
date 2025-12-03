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

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
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

import com.bornfire.brrs.entities.BRRS_M_PI_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_PI_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_PI_Manual_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_PI_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_PI_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_PI_Manual_Summary_Repo;
import com.bornfire.brrs.entities.M_PI_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_PI_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_PI_Manual_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_PI_Detail_Entity;
import com.bornfire.brrs.entities.M_PI_Summary_Entity;
import com.bornfire.brrs.entities.M_PI_Manual_Summary_Entity;

@Component
@Service

public class BRRS_M_PI_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_PI_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	
	@Autowired
	BRRS_M_PI_Summary_Repo BRRS_M_PI_Summary_Repo;
	@Autowired
	BRRS_M_PI_Manual_Summary_Repo BRRS_M_PI_Manual_Summary_Repo;

	@Autowired
	BRRS_M_PI_Detail_Repo M_PI_Detail_Repo;

	@Autowired
	BRRS_M_PI_Archival_Detail_Repo M_PI_Archival_Detail_Repo;

	@Autowired
	BRRS_M_PI_Archival_Summary_Repo M_PI_Archival_Summary_Repo;
	@Autowired
	BRRS_M_PI_Manual_Archival_Summary_Repo M_PI_Manual_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_PIView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_PI_Archival_Summary_Entity> T1Master = new ArrayList<M_PI_Archival_Summary_Entity>();
			List<M_PI_Manual_Archival_Summary_Entity> T2Master = new ArrayList<M_PI_Manual_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_PI_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = M_PI_Manual_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);

		} else {

			List<M_PI_Summary_Entity> T1Master = new ArrayList<M_PI_Summary_Entity>();
			List<M_PI_Manual_Summary_Entity> T2Master = new ArrayList<M_PI_Manual_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_PI_Summary_Repo.getdatabydateList(dateformat.parse(todate));
				T2Master = BRRS_M_PI_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_PI");

		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getM_PIcurrentDtl(String reportId, String fromdate, String todate, String currency,
			  String dtltype, Pageable pageable, String Filter, String type, String version) {

	int pageSize = pageable != null ? pageable.getPageSize() : 10;
	int currentPage = pageable != null ? pageable.getPageNumber() : 0;
	int totalPages = 0;

	ModelAndView mv = new ModelAndView();
//	Session hs = sessionFactory.getCurrentSession();

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
	
		if ("ARCHIVAL".equals(type) && version != null) {
			// 🔹 Archival branch
			List<M_PI_Archival_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = M_PI_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
			} else {
				T1Dt1 = M_PI_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
			}

			mv.addObject("reportdetails", T1Dt1);
			mv.addObject("reportmaster12", T1Dt1);
			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

		} else {
			// 🔹 Current branch
			List<M_PI_Detail_Entity> T1Dt1;
			if (rowId != null && columnId != null) {
				T1Dt1 = M_PI_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
			} else {
				T1Dt1 = M_PI_Detail_Repo.getdatabydateList(parsedDate);
				totalPages = M_PI_Detail_Repo.getdatacount(parsedDate);
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
	mv.setViewName("BRRS/M_PI");
	mv.addObject("displaymode", "Details");
	mv.addObject("currentPage", currentPage);
	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	mv.addObject("reportsflag", "reportsflag");
	mv.addObject("menu", reportId);

	return mv;
}


	public void updateReport(M_PI_Manual_Summary_Entity updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_PI_Manual_Summary_Entity existing = BRRS_M_PI_Manual_Summary_Repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // ✅ Loop for amount_2 fields
	        int[] amount2Rows = {14, 18, 19, 25};
	        for (int i : amount2Rows) {
	            String prefix = "R" + i + "_";
	            String[] fields = {"VALUE"};

	            for (String field : fields) {
	                try {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter = M_PI_Manual_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_PI_Manual_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing getter/setter gracefully
	                    continue;
	                }
	            }
	        }

	        // ✅ Save after all updates
	        BRRS_M_PI_Manual_Summary_Repo.save(existing);

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
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

		List<M_PI_Summary_Entity> dataList = BRRS_M_PI_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		List<M_PI_Manual_Summary_Entity> dataList1 = BRRS_M_PI_Manual_Summary_Repo.getdatabydateList(dateformat.parse(todate));
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRF2.4 report. Returning empty result.");
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

			if (!dataList.isEmpty() || !dataList1.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_PI_Summary_Entity record = dataList.get(i);
					M_PI_Manual_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row8
					// Column F
					Cell cell5 = row.getCell(5);
					if (record.getR8_VALUE() != null) {
						cell5.setCellValue(record.getR8_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);

					}

					// row8
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row8
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row9
					row = sheet.getRow(8);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR9_VALUE() != null) {
						cell5.setCellValue(record.getR9_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row9
					// Column G
					cell6 = row.createCell(6);
					if (record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row9
					// Column H
					cell7 = row.createCell(7);
					if (record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row10
					row = sheet.getRow(9);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR10_VALUE() != null) {
						cell5.setCellValue(record.getR10_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cell6 = row.createCell(6);
					if (record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cell7 = row.createCell(7);
					if (record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR11_VALUE() != null) {
						cell5.setCellValue(record.getR11_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cell6 = row.createCell(6);
					if (record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cell7 = row.createCell(7);
					if (record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR12_VALUE() != null) {
						cell5.setCellValue(record.getR12_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					// Column H
					cell7 = row.createCell(7);
					if (record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR13_VALUE() != null) {
						cell5.setCellValue(record.getR13_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR14_VALUE() != null) {
						cell5.setCellValue(record1.getR14_VALUE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row14
					// Column H
					cell7 = row.createCell(7);
					if (record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR15_VALUE() != null) {
						cell5.setCellValue(record.getR15_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					// Column G
					cell6 = row.createCell(6);
					if (record.getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR16_VALUE() != null) {
						cell5.setCellValue(record.getR16_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row16
					// Column G
					cell6 = row.createCell(6);
					if (record.getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR17_VALUE() != null) {
						cell5.setCellValue(record.getR17_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row17
					// Column G
					cell6 = row.createCell(6);
					if (record.getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR18_VALUE() != null) {
						cell5.setCellValue(record1.getR18_VALUE().doubleValue() / 100);
						cell5.setCellStyle(percentStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row18
					// Column G
					cell6 = row.createCell(6);
					if (record.getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR19_VALUE() != null) {
						cell5.setCellValue(record1.getR19_VALUE().doubleValue() / 100);
						cell5.setCellStyle(percentStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row19
					// Column G
					cell6 = row.createCell(6);
					if (record.getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR20_VALUE() != null) {
						cell5.setCellValue(record.getR20_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row20
					// Column G
					cell6 = row.createCell(6);
					if (record.getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR21_VALUE() != null) {
						cell5.setCellValue(record.getR21_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					// Column G
					cell6 = row.createCell(6);
					if (record.getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR22_VALUE() != null) {
						cell5.setCellValue(record.getR22_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR23_VALUE() != null) {
						cell5.setCellValue(record.getR23_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR24_VALUE() != null) {
						cell5.setCellValue(record.getR24_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR25_VALUE() != null) {
						cell5.setCellValue(record1.getR25_VALUE().doubleValue() / 100);
						cell5.setCellStyle(percentStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					// Column G
					cell6 = row.createCell(6);
					if (record.getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR26_VALUE() != null) {
						cell5.setCellValue(record.getR26_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					// Column G
					cell6 = row.createCell(6);
					if (record.getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR27_VALUE() != null) {
						cell5.setCellValue(record.getR27_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					// Column G
					cell6 = row.createCell(6);
					if (record.getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
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

//	public byte[] BRRS_M_PIDetailExcel(String filename, String fromdate, String todate, String currency,
	public byte[] BRRS_M_PIDetailExcel(String filename, String fromdate, String todate, String currency,
			   String dtltype, String type, String version) {
try {
logger.info("Generating Excel for M_PI Details...");
System.out.println("came to Detail download service");


if (type.equals("ARCHIVAL") & version != null) {
byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
version);
return ARCHIVALreport;
}

XSSFWorkbook workbook = new XSSFWorkbook();
XSSFSheet sheet = workbook.createSheet("M_PIDetail");

//Common border style
BorderStyle border = BorderStyle.THIN;

//Header style (left aligned)
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

//Right-aligned header style for ACCT BALANCE
CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

//Default data style (left aligned)
CellStyle dataStyle = workbook.createCellStyle();
dataStyle.setAlignment(HorizontalAlignment.LEFT);
dataStyle.setBorderTop(border);
dataStyle.setBorderBottom(border);
dataStyle.setBorderLeft(border);
dataStyle.setBorderRight(border);

//ACCT BALANCE style (right aligned with thousand separator)
CellStyle balanceStyle = workbook.createCellStyle();
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
balanceStyle.setBorderTop(border);
balanceStyle.setBorderBottom(border);
balanceStyle.setBorderLeft(border);
balanceStyle.setBorderRight(border);






//Header row
String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE", "REPORT ADDL CRITERIA1",
"REPORT_DATE"
};

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

//Get data
Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
List<M_PI_Detail_Entity> reportData = M_PI_Detail_Repo.getdatabydateList(parsedToDate);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (M_PI_Detail_Entity item : reportData) {
XSSFRow row = sheet.createRow(rowIndex++);

row.createCell(0).setCellValue(item.getCustId());
row.createCell(1).setCellValue(item.getAcctNumber());
row.createCell(2).setCellValue(item.getAcctName());

//ACCT BALANCE (right aligned, 3 decimal places)
Cell balanceCell = row.createCell(3);
if (item.getAcctBalanceInpula() != null) {
balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
} else {
balanceCell.setCellValue(0);
}
balanceCell.setCellStyle(balanceStyle);

		row.createCell(4).setCellValue(item.getReportLable());
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
	logger.info("No data found for M_PI — only header will be written.");
}

//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating M_PI Excel", e);
return new byte[0];
}
}
	public List<Object> getM_PIArchival() {
		List<Object> M_PIArchivallist = new ArrayList<>();
		try {
			M_PIArchivallist = M_PI_Archival_Summary_Repo.getM_PIarchival();
			M_PIArchivallist = M_PI_Manual_Archival_Summary_Repo.getM_PIarchival();
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
		List<M_PI_Archival_Summary_Entity> dataList = M_PI_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_PI_Manual_Archival_Summary_Entity> dataList1 = M_PI_Manual_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty() || dataList1.isEmpty()) {
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

			if (!dataList.isEmpty() || !dataList1.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_PI_Archival_Summary_Entity record = dataList.get(i);
					M_PI_Manual_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					// row8
					// Column F
					Cell cell5 = row.getCell(5);
					if (record.getR8_VALUE() != null) {
						cell5.setCellValue(record.getR8_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);

					}

					// row8
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row8
					// Column H
					Cell cell7 = row.createCell(7);
					if (record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR8_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row9
					row = sheet.getRow(8);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR9_VALUE() != null) {
						cell5.setCellValue(record.getR9_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row9
					// Column G
					cell6 = row.createCell(6);
					if (record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row9
					// Column H
					cell7 = row.createCell(7);
					if (record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR9_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row10
					row = sheet.getRow(9);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR10_VALUE() != null) {
						cell5.setCellValue(record.getR10_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row10
					// Column G
					cell6 = row.createCell(6);
					if (record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row10
					// Column H
					cell7 = row.createCell(7);
					if (record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR10_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR11_VALUE() != null) {
						cell5.setCellValue(record.getR11_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row11
					// Column G
					cell6 = row.createCell(6);
					if (record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row11
					// Column H
					cell7 = row.createCell(7);
					if (record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR11_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR12_VALUE() != null) {
						cell5.setCellValue(record.getR12_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					// Column H
					cell7 = row.createCell(7);
					if (record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR12_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR13_VALUE() != null) {
						cell5.setCellValue(record.getR13_VALUE().doubleValue());

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					// Column H
					cell7 = row.createCell(7);
					if (record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR13_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR14_VALUE() != null) {
						cell5.setCellValue(record1.getR14_VALUE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row14
					// Column H
					cell7 = row.createCell(7);
					if (record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2() != null) {
						cell7.setCellValue(record.getR14_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS2().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR15_VALUE() != null) {
						cell5.setCellValue(record.getR15_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					// Column G
					cell6 = row.createCell(6);
					if (record.getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR15_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR16_VALUE() != null) {
						cell5.setCellValue(record.getR16_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row16
					// Column G
					cell6 = row.createCell(6);
					if (record.getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR16_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR17_VALUE() != null) {
						cell5.setCellValue(record.getR17_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row17
					// Column G
					cell6 = row.createCell(6);
					if (record.getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR17_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR18_VALUE() != null) {
						cell5.setCellValue(record1.getR18_VALUE().doubleValue() / 100);
						cell5.setCellStyle(percentStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row18
					// Column G
					cell6 = row.createCell(6);
					if (record.getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR18_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR19_VALUE() != null) {
						cell5.setCellValue(record1.getR19_VALUE().doubleValue() / 100);
						cell5.setCellStyle(percentStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row19
					// Column G
					cell6 = row.createCell(6);
					if (record.getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR19_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR20_VALUE() != null) {
						cell5.setCellValue(record.getR20_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row20
					// Column G
					cell6 = row.createCell(6);
					if (record.getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR20_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR21_VALUE() != null) {
						cell5.setCellValue(record.getR21_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					// Column G
					cell6 = row.createCell(6);
					if (record.getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR21_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR22_VALUE() != null) {
						cell5.setCellValue(record.getR22_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record.getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR22_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR23_VALUE() != null) {
						cell5.setCellValue(record.getR23_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record.getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR23_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR24_VALUE() != null) {
						cell5.setCellValue(record.getR24_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record.getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR24_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR25_VALUE() != null) {
						cell5.setCellValue(record1.getR25_VALUE().doubleValue() / 100);
						cell5.setCellStyle(percentStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					// Column G
					cell6 = row.createCell(6);
					if (record.getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR25_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR26_VALUE() != null) {
						cell5.setCellValue(record.getR26_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					// Column G
					cell6 = row.createCell(6);
					if (record.getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR26_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column F
					cell5 = row.getCell(5);
					if (record.getR27_VALUE() != null) {
						cell5.setCellValue(record.getR27_VALUE().doubleValue() / 100);

					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					// Column G
					cell6 = row.createCell(6);
					if (record.getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS() != null) {
						cell6.setCellValue(record.getR27_PRUDENTIAL_MINIMUM_AND_LIMIT_BENCHMARKS().doubleValue() / 100);
						cell6.setCellStyle(percentStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
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
XSSFSheet sheet = workbook.createSheet("M_PIDetail");

//Common border style
BorderStyle border = BorderStyle.THIN;

//Header style (left aligned)
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

//Right-aligned header style for ACCT BALANCE
CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

//Default data style (left aligned)
CellStyle dataStyle = workbook.createCellStyle();
dataStyle.setAlignment(HorizontalAlignment.LEFT);
dataStyle.setBorderTop(border);
dataStyle.setBorderBottom(border);
dataStyle.setBorderLeft(border);
dataStyle.setBorderRight(border);

//ACCT BALANCE style (right aligned with 3 decimals)
CellStyle balanceStyle = workbook.createCellStyle();
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
balanceStyle.setBorderTop(border);
balanceStyle.setBorderBottom(border);
balanceStyle.setBorderLeft(border);
balanceStyle.setBorderRight(border);


//Header row
String[] headers = {
"CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE", "REPORT ADDL CRITERIA", "REPORT_DATE"
};

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

//Get data
Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
List<M_PI_Archival_Detail_Entity> reportData = M_PI_Archival_Detail_Repo.getdatabydateList(parsedToDate,version);

if (reportData != null && !reportData.isEmpty()) {
int rowIndex = 1;
for (M_PI_Archival_Detail_Entity item : reportData) {
XSSFRow row = sheet.createRow(rowIndex++);

row.createCell(0).setCellValue(item.getCustId());
row.createCell(1).setCellValue(item.getAcctNumber());
row.createCell(2).setCellValue(item.getAcctName());

//ACCT BALANCE (right aligned, 3 decimal places with comma separator)
Cell balanceCell = row.createCell(3);

if (item.getAcctBalanceInpula() != null) {
balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
} else {
balanceCell.setCellValue(0);
}

//Create style with thousand separator and decimal point
DataFormat format = workbook.createDataFormat();

//Format: 1,234,567
balanceStyle.setDataFormat(format.getFormat("#,##0"));

//Right alignment (optional)
balanceStyle.setAlignment(HorizontalAlignment.RIGHT);

balanceCell.setCellStyle(balanceStyle);

row.createCell(4).setCellValue(item.getReportLable());
row.createCell(5).setCellValue(item.getReportAddlCriteria1());
row.createCell(6).setCellValue(
item.getReportDate() != null ?
new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : ""
);

//Apply data style for all other cells
for (int j = 0; j < 7; j++) {
if (j != 3) {
row.getCell(j).setCellStyle(dataStyle);
}
}
}
} else {
logger.info("No data found for M_PI — only header will be written.");
}
//Write to byte[]
ByteArrayOutputStream bos = new ByteArrayOutputStream();
workbook.write(bos);
workbook.close();

logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
return bos.toByteArray();

} catch (Exception e) {
logger.error("Error generating M_PI Excel", e);
return new byte[0];
}
}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_PI"); // ✅ match the report name
	    System.out.println("Hello");
	    if (acctNo != null) {
	    	M_PI_Detail_Entity dep3Entity = M_PI_Detail_Repo.findByAcctnumber(acctNo);
	        if (dep3Entity != null && dep3Entity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(dep3Entity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	        }
	        mv.addObject("Data", dep3Entity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}





	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
	    ModelAndView mv = new ModelAndView("BRRS/M_PI"); // ✅ match the report name

	    if (acctNo != null) {
	        M_PI_Detail_Entity la1Entity = M_PI_Detail_Repo.findByAcctnumber(acctNo);
	        if (la1Entity != null && la1Entity.getReportDate() != null) {
	            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
	            mv.addObject("asondate", formattedDate);
	            System.out.println(formattedDate);
	        }
	        mv.addObject("Data", la1Entity);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode", formMode != null ? formMode : "edit");
	    return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
	    try {
	        String acctNo = request.getParameter("acctNumber");
	        String provisionStr = request.getParameter("acctBalanceInpula");
	        String acctName = request.getParameter("acctName");
	        String reportDateStr = request.getParameter("reportDate");

	        logger.info("Received update for ACCT_NO: {}", acctNo);

	        M_PI_Detail_Entity existing = M_PI_Detail_Repo.findByAcctnumber(acctNo);
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

	        if (provisionStr != null && !provisionStr.isEmpty()) {
	            BigDecimal newProvision = new BigDecimal(provisionStr);
	            if (existing.getAcctBalanceInpula() == null ||
	                existing.getAcctBalanceInpula().compareTo(newProvision) != 0) {
	                existing.setAcctBalanceInpula(newProvision);
	                isChanged = true;
	                logger.info("Balance updated to {}", newProvision);
	            }
	        }
	        
	        

	        if (isChanged) {
	        	M_PI_Detail_Repo.save(existing);
	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

	            // Run summary procedure after commit
	            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	                @Override
	                public void afterCommit() {
	                    try {
	                        logger.info("Transaction committed — calling BRRS_M_PI_SUMMARY_PROCEDURE({})",
	                                formattedDate);
	                        jdbcTemplate.update("BEGIN BRRS_M_PI_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
	        logger.error("Error updating M_PI record", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}
	

	
}
