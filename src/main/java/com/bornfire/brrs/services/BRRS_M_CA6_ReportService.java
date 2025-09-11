package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.M_CA6_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_CA6_Archival_Detail_Repo;
import com.bornfire.brrs.entities.M_CA6_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA6_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_CA6_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA6_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.M_CA6_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_CA6_Detail_Repo;
import com.bornfire.brrs.entities.M_CA6_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA6_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_CA6_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA6_Summary_Repo2;

import java.math.BigDecimal;

@Component
@Service
public class BRRS_M_CA6_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA6_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_M_CA6_Detail_Repo M_CA6_DETAIL_Repo;

	@Autowired
	BRRS_M_CA6_Summary_Repo1 M_CA6_Summary_Repo1;
	
	@Autowired
	BRRS_M_CA6_Summary_Repo2 M_CA6_Summary_Repo2;

	@Autowired
	BRRS_M_CA6_Archival_Detail_Repo M_CA6_Archival_Detail_Repo;

	@Autowired
	BRRS_M_CA6_Archival_Summary_Repo1 M_CA6_Archival_Summary_Repo1;
	
	@Autowired
	BRRS_M_CA6_Archival_Summary_Repo2 M_CA6_Archival_Summary_Repo2;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_CA6View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_CA6_Archival_Summary_Entity1> T1Master = new ArrayList<M_CA6_Archival_Summary_Entity1>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_CA6_Archival_Summary_Repo1.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
			List<M_CA6_Summary_Entity1> T1Master = new ArrayList<M_CA6_Summary_Entity1>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_CA6_Summary_Repo1.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_CA6");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getM_CA6currentDtl(String reportId, String fromdate, String todate, String currency,
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

	        // ✅ Split filter string into rowId & columnId
	        if (filter != null && filter.contains(",")) {
	            String[] parts = filter.split(",");
	            if (parts.length >= 2) {
	                rowId = parts[0];
	                columnId = parts[1];
	            }
	        }

	        if ("ARCHIVAL".equals(type) && version != null) {
	            // 🔹 Archival branch
	            List<M_CA6_Archival_Detail_Entity> T1Dt1;
	            if (rowId != null && columnId != null) {
	                T1Dt1 = M_CA6_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
	            } else {
	                T1Dt1 = M_CA6_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
	                mv.addObject("pagination", "YES");
	            }

	            mv.addObject("reportdetails", T1Dt1);
	            mv.addObject("reportmaster12", T1Dt1);
	            System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

	        } else {
	            // 🔹 Current branch
	            List<M_CA6_Detail_Entity> T1Dt1;
	            if (rowId != null && columnId != null) {
	                T1Dt1 = M_CA6_DETAIL_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
	            } else {
	                T1Dt1 = M_CA6_DETAIL_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
	                totalPages = M_CA6_DETAIL_Repo.getdatacount(parsedDate);
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
	    mv.setViewName("BRRS/M_CA6");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("currentPage", currentPage);
	    System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	    mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	    mv.addObject("reportsflag", "reportsflag");
	    mv.addObject("menu", reportId);

	    return mv;
	}


	public byte[] BRRS_M_CA6Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_CA6ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_CA6_Summary_Entity1> dataList = M_CA6_Summary_Repo1.getdatabydateList(dateformat.parse(todate));
		List<M_CA6_Summary_Entity2> dataList1 = M_CA6_Summary_Repo2.getdatabydateList(dateformat.parse(todate));


		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA6 report. Returning empty result.");
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
			int startRow = 11;
			
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					
					M_CA6_Summary_Entity1 record = dataList.get(i);
					M_CA6_Summary_Entity2 record2 = dataList1.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					//row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row15
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row16
					row = sheet.getRow(15);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row16
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row20
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row21
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//row24
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					//row28
					row = sheet.getRow(27);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR28_AMOUNT() != null) {
						cell3.setCellValue(record2.getR28_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row29
					row = sheet.getRow(28);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR29_AMOUNT() != null) {
						cell3.setCellValue(record2.getR29_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row30
					row = sheet.getRow(29);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR30_AMOUNT() != null) {
						cell3.setCellValue(record2.getR30_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row31
					row = sheet.getRow(30);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR31_AMOUNT() != null) {
						cell3.setCellValue(record2.getR31_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row32
					row = sheet.getRow(31);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR32_AMOUNT() != null) {
						cell3.setCellValue(record2.getR32_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row33
					row = sheet.getRow(32);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR33_AMOUNT() != null) {
						cell3.setCellValue(record2.getR33_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row34
					row = sheet.getRow(33);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR34_AMOUNT() != null) {
						cell3.setCellValue(record2.getR34_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row40
					row = sheet.getRow(39);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR40_AMOUNT() != null) {
						cell3.setCellValue(record2.getR40_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row41
					row = sheet.getRow(40);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR41_AMOUNT() != null) {
						cell3.setCellValue(record2.getR41_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row42
					row = sheet.getRow(41);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR42_AMOUNT() != null) {
						cell3.setCellValue(record2.getR42_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row43
					row = sheet.getRow(42);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR43_AMOUNT() != null) {
						cell3.setCellValue(record2.getR43_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row44
					row = sheet.getRow(43);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR44_AMOUNT() != null) {
						cell3.setCellValue(record2.getR44_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row45
					row = sheet.getRow(44);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR45_AMOUNT() != null) {
						cell3.setCellValue(record2.getR45_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row46
					row = sheet.getRow(45);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR46_AMOUNT() != null) {
						cell3.setCellValue(record2.getR46_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
				
					
										
				}
				
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			// audit
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_CA6_SUMMARY", null,
						"BRRS_M_CA6_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	public byte[] BRRS_M_CA6DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_CA6 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_CA6Details");

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
			List<M_CA6_Detail_Entity> reportData = M_CA6_DETAIL_Repo.getdatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_CA6_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(4).setCellValue(item.getRowId());
					row.createCell(5).setCellValue(item.getColumnId());
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
				logger.info("No data found for BRRS_M_CA6 — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_CA6 Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getM_CA6Archival() {
		List<Object> M_CA6Archivallist = new ArrayList<>();
		try {
			M_CA6Archivallist = M_CA6_Archival_Summary_Repo1.getM_CA6archival();
			M_CA6Archivallist = M_CA6_Archival_Summary_Repo2.getM_CA6archival();
			System.out.println("countser" + M_CA6Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_CA6 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_CA6Archivallist;
	}

	public byte[] getExcelM_CA6ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_CA6_Archival_Summary_Entity1> dataList = M_CA6_Archival_Summary_Repo1
				.getdatabydateListarchival(dateformat.parse(todate), version);
		List<M_CA6_Archival_Summary_Entity2> dataList1 = M_CA6_Archival_Summary_Repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA6 report. Returning empty result.");
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
					
					M_CA6_Archival_Summary_Entity1 record = dataList.get(i);
					M_CA6_Archival_Summary_Entity2 record2 = dataList1.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					//row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR12_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row12
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR12_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR13_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR13_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR14_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR14_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR15_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row15
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR15_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row16
					row = sheet.getRow(15);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR16_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row16
					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR16_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR20_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row20
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR20_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR21_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row21
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR21_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR22_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row22
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR22_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR23_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row23
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR23_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					//row24
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell3.setCellValue(record.getR24_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					//row24
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT() != null) {
						cell4.setCellValue(record.getR24_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					
					//row28
					row = sheet.getRow(27);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR28_AMOUNT() != null) {
						cell3.setCellValue(record2.getR28_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row29
					row = sheet.getRow(28);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR29_AMOUNT() != null) {
						cell3.setCellValue(record2.getR29_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row30
					row = sheet.getRow(29);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR30_AMOUNT() != null) {
						cell3.setCellValue(record2.getR30_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row31
					row = sheet.getRow(30);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR31_AMOUNT() != null) {
						cell3.setCellValue(record2.getR31_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row32
					row = sheet.getRow(31);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR32_AMOUNT() != null) {
						cell3.setCellValue(record2.getR32_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row33
					row = sheet.getRow(32);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR33_AMOUNT() != null) {
						cell3.setCellValue(record2.getR33_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row34
					row = sheet.getRow(33);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR34_AMOUNT() != null) {
						cell3.setCellValue(record2.getR34_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row40
					row = sheet.getRow(39);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR40_AMOUNT() != null) {
						cell3.setCellValue(record2.getR40_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row41
					row = sheet.getRow(40);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR41_AMOUNT() != null) {
						cell3.setCellValue(record2.getR41_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row42
					row = sheet.getRow(41);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR42_AMOUNT() != null) {
						cell3.setCellValue(record2.getR42_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row43
					row = sheet.getRow(42);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR43_AMOUNT() != null) {
						cell3.setCellValue(record2.getR43_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row44
					row = sheet.getRow(43);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR44_AMOUNT() != null) {
						cell3.setCellValue(record2.getR44_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row45
					row = sheet.getRow(44);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR45_AMOUNT() != null) {
						cell3.setCellValue(record2.getR45_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row46
					row = sheet.getRow(45);
					// Column D
					cell3 = row.createCell(5);
					if (record2.getR46_AMOUNT() != null) {
						cell3.setCellValue(record2.getR46_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
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
			logger.info("Generating Excel for BRRS_M_CA6 ARCHIVAL Details...");
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
			List<M_CA6_Archival_Detail_Entity> reportData = M_CA6_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_CA6_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getRowId());
					row.createCell(5).setCellValue(item.getColumnId());
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
				logger.info("No data found for BRRS_M_CA6 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_CA6Excel", e);
			return new byte[0];
		}
	}

}
