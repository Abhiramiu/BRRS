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
import java.util.Locale;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
/*import org.apache.poi.ss.usermodel.FillPatternType;*/
import org.apache.poi.ss.usermodel.Font;
/*import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;*/
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
/*import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_CR_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CR_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_CR_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CR_Summary_Repo;
import com.bornfire.brrs.entities.M_CR_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_CR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CR_Detail_Entity;
import com.bornfire.brrs.entities.M_CR_Summary_Entity;

@Component
@Service

public class BRRS_M_CR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CR_ReportService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	

	
	  @Autowired BRRS_M_CR_Detail_Repo brrs_M_CR_detail_repo;
	 
	@Autowired
	BRRS_M_CR_Summary_Repo brrs_M_CR_summary_repo;
	
	
	  @Autowired BRRS_M_CR_Archival_Detail_Repo M_CR_Archival_Detail_Repo;
	 

	@Autowired
	BRRS_M_CR_Archival_Summary_Repo M_CR_Archival_Summary_Repo;
	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	/*
	 * public ModelAndView getM_CRView(String reportId, String fromdate, String
	 * todate, String currency, String dtltype, Pageable pageable, String type,
	 * String version) {
	 * 
	 * ModelAndView mv = new ModelAndView(); Session hs =
	 * sessionFactory.getCurrentSession();
	 * 
	 * int pageSize = pageable.getPageSize(); int currentPage =
	 * pageable.getPageNumber(); int startItem = currentPage * pageSize;
	 * 
	 * System.out.println("testing"); System.out.println(version);
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) { System.out.println(type);
	 * List<M_CR_Archival_Summary_Entity> T1Master = new
	 * ArrayList<M_CR_Archival_Summary_Entity>(); System.out.println(version); try
	 * { Date d1 = dateformat.parse(todate);
	 * 
	 * 
	 * 
	 * T1Master =
	 * M_CR_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate
	 * ), version);
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * 
	 * mv.addObject("reportsummary", T1Master); } else { List<M_CR_Summary_Entity>
	 * T1Master = new ArrayList<M_CR_Summary_Entity>(); try { Date d1 =
	 * dateformat.parse(todate);
	 * 
	 * 
	 * 
	 * T1Master =
	 * brrs_M_CR_summary_repo.getdatabydateList(dateformat.parse(todate));
	 * mv.addObject("report_date", dateformat.format(d1));
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * mv.addObject("reportsummary", T1Master); }
	 * 
	 * 
	 * mv.setViewName("BRRS/M_CR");
	 * 
	 * 
	 * 
	 * mv.addObject("displaymode", "summary");
	 * 
	 * System.out.println("scv" + mv.getViewName());
	 * 
	 * return mv;
	 * 
	 * }
	 */
	
	public ModelAndView getM_CRView(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();
	    Session hs = sessionFactory.getCurrentSession();

	    int pageSize = pageable.getPageSize();
	    int currentPage = pageable.getPageNumber();
	    int startItem = currentPage * pageSize;

	    try {
	        Date d1 = dateformat.parse(todate);

	        // ---------- CASE 1: ARCHIVAL ----------
	        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

	            List<M_CR_Archival_Summary_Entity> T1Master =
	                    M_CR_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- CASE 2: RESUB ----------
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_CR_Archival_Summary_Entity> T1Master =
	                    M_CR_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- CASE 3: NORMAL ----------
	        else {

	            List<M_CR_Summary_Entity> T1Master =
	                    brrs_M_CR_summary_repo.getdatabydateList(dateformat.parse(todate));
	            mv.addObject("displaymode", "summary");
	            System.out.println("T1Master Size " + T1Master.size());
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
	        if ("detail".equalsIgnoreCase(dtltype)) {

	            // DETAIL + ARCHIVAL
	            if (version != null) {

	                List<M_CR_Archival_Detail_Entity> T1Master =
	                        M_CR_Archival_Detail_Repo
	                                .getdatabydateListarchival(d1, version);
	                mv.addObject("displaymode", "Details");
	                mv.addObject("reportsummary", T1Master);
	            }
	            // DETAIL + NORMAL
	            else {

	                List<M_CR_Detail_Entity> T1Master =
	                        brrs_M_CR_detail_repo
	                                .getdatabydateList(dateformat.parse(todate));
	                mv.addObject("displaymode", "Details");
	                mv.addObject("reportsummary", T1Master);
	            }
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_CR");
	   
	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}


	 
	 
	 
	/*
	 * public ModelAndView getM_CRcurrentDtl(String reportId, String fromdate,
	 * String todate, String currency, String dtltype, Pageable pageable, String
	 * type, BigDecimal version) {
	 * 
	 * ModelAndView mv = new ModelAndView();
	 * 
	 * 
	 * System.out.println("testing"); System.out.println(version);
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) { System.out.println(type);
	 * List<M_CR_Archival_Detail_Entity> T1Master = new
	 * ArrayList<M_CR_Archival_Detail_Entity>();
	 * 
	 * System.out.println(version); try {
	 * 
	 * 
	 * T1Master =
	 * M_CR_Archival_Detail_Repo.getdatabydateListarchival(dateformat.parse(todate)
	 * , version); System.out.println("DETAIL");
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * 
	 * mv.addObject("reportsummary", T1Master);
	 * 
	 * } else {
	 * 
	 * List<M_CR_Detail_Entity> T1Master = new ArrayList<M_CR_Detail_Entity>();
	 * 
	 * try { Date d1 = dateformat.parse(todate);
	 * 
	 * T1Master =
	 * brrs_M_CR_detail_repo.getdatabydateList(dateformat.parse(todate));
	 * 
	 * System.out.println("T1Master size " + T1Master.size());
	 * mv.addObject("report_date", dateformat.format(d1));
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * mv.addObject("reportsummary", T1Master);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * mv.setViewName("BRRS/M_CR");
	 * 
	 * mv.addObject("displaymode", "summary");
	 * 
	 * System.out.println("scv" + mv.getViewName());
	 * 
	 * return mv; }
	 */
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	

	public byte[] getM_CRExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		
		
		
		// ARCHIVAL check
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
            logger.info("Service: Generating ARCHIVAL report for version {}", version);
            return getExcelM_CRARCHIVAL(filename, reportId, fromdate, todate,
                    currency, dtltype, type, version);
        }
        // Email check
         if ("email".equalsIgnoreCase(type)  && version == null) {
            logger.info("Service: Generating Email report for version {}", version);
            return BRRS_M_CREmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
        } else if ("email".equalsIgnoreCase(type) && version != null) {
            logger.info("Service: Generating Email report for version {}", version);
            return BRRS_M_CRARCHIVALEmailExcel(filename, reportId, fromdate, todate,
                    currency, dtltype, type, version);

        }

	    /* ===================== NORMAL ===================== */
	    List<M_CR_Summary_Entity> dataList =
	            brrs_M_CR_summary_repo.getdatabydateList(dateformat.parse(todate));

	    if (dataList.isEmpty()) {
	        logger.warn("Service: No data found for M_CR report. Returning empty result.");
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
					M_CR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// ROW 10 DOWNLAOD

					Cell cell2 = row.createCell(0);
					if (record.getR10_PRODUCT() != null) {
						cell2.setCellValue(record.getR10_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(1);
					if (record.getR10_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(2);
					if (record.getR10_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(3);
					if (record.getR10_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					// ====================== R11 ======================
					cell2 = row.createCell(0);
					if (record.getR11_PRODUCT() != null) {
						cell2.setCellValue(record.getR11_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR11_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR11_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR11_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R12 ======================
					row = sheet.getRow(11);

					cell2 = row.createCell(0);
					if (record.getR12_PRODUCT() != null) {
						cell2.setCellValue(record.getR12_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR12_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR12_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR12_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R13 ======================
					row = sheet.getRow(12);

					// repeat same pattern...
					cell2 = row.createCell(0);
					if (record.getR13_PRODUCT() != null) {
						cell2.setCellValue(record.getR13_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR13_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR13_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR13_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R14 ======================
					row = sheet.getRow(13);

					cell2 = row.createCell(0);
					if (record.getR14_PRODUCT() != null) {
						cell2.setCellValue(record.getR14_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR14_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR14_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR14_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R15 ======================
					row = sheet.getRow(14);

					cell2 = row.createCell(0);
					if (record.getR15_PRODUCT() != null) {
						cell2.setCellValue(record.getR15_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR15_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR15_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR15_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R16 ======================
					row = sheet.getRow(15);

					cell2 = row.createCell(0);
					if (record.getR16_PRODUCT() != null) {
						cell2.setCellValue(record.getR16_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR16_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR16_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR16_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
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

	/*
	 * public byte[] getM_CRDetailExcel(String filename, String fromdate, String
	 * todate, String currency, String dtltype, String type, String version){ try {
	 * logger.info("Generating Excel for M_CR Details...");
	 * System.out.println("came to Detail download service");
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) { byte[] ARCHIVALreport =
	 * getM_CRDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
	 * type, version); return ARCHIVALreport; }
	 * 
	 * XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("M_CRDetails");
	 * 
	 * // Common border style BorderStyle border = BorderStyle.THIN;
	 * 
	 * // Header style (left aligned) CellStyle headerStyle =
	 * workbook.createCellStyle(); Font headerFont = workbook.createFont();
	 * headerFont.setBold(true); headerFont.setFontHeightInPoints((short) 10);
	 * headerStyle.setFont(headerFont);
	 * headerStyle.setAlignment(HorizontalAlignment.LEFT);
	 * headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	 * headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	 * headerStyle.setBorderTop(border); headerStyle.setBorderBottom(border);
	 * headerStyle.setBorderLeft(border); headerStyle.setBorderRight(border);
	 * 
	 * // Right-aligned header style for ACCT BALANCE CellStyle
	 * rightAlignedHeaderStyle = workbook.createCellStyle();
	 * rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
	 * rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * 
	 * // Default data style (left aligned) CellStyle dataStyle =
	 * workbook.createCellStyle(); dataStyle.setAlignment(HorizontalAlignment.LEFT);
	 * dataStyle.setBorderTop(border); dataStyle.setBorderBottom(border);
	 * dataStyle.setBorderLeft(border); dataStyle.setBorderRight(border);
	 * 
	 * // ACCT BALANCE style (right aligned with 3 decimals) CellStyle balanceStyle
	 * = workbook.createCellStyle();
	 * balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
	 * balanceStyle.setBorderTop(border); balanceStyle.setBorderBottom(border);
	 * balanceStyle.setBorderLeft(border); balanceStyle.setBorderRight(border);
	 * 
	 * // Header row String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME",
	 * "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE" };
	 * 
	 * XSSFRow headerRow = sheet.createRow(0); for (int i = 0; i < headers.length;
	 * i++) { Cell cell = headerRow.createCell(i); cell.setCellValue(headers[i]);
	 * 
	 * if (i == 3) { // ACCT BALANCE cell.setCellStyle(rightAlignedHeaderStyle); }
	 * else { cell.setCellStyle(headerStyle); }
	 * 
	 * sheet.setColumnWidth(i, 5000); }
	 * 
	 * // Get data Date parsedToDate = new
	 * SimpleDateFormat("dd/MM/yyyy").parse(todate); List<M_CR_Detail_Entity>
	 * reportData = brrs_M_CR_detail_repo.getdatabydateList(parsedToDate);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_CR_Detail_Entity item : reportData) { XSSFRow row =
	 * sheet.createRow(rowIndex++);
	 * 
	 * row.createCell(0).setCellValue(item.getCustId());
	 * row.createCell(1).setCellValue(item.getAcctNumber());
	 * row.createCell(2).setCellValue(item.getAcctName());
	 * 
	 * // ACCT BALANCE (right aligned, 3 decimal places) Cell balanceCell =
	 * row.createCell(3); if (item.getAcctBalanceInPula() != null) {
	 * balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue()); } else {
	 * balanceCell.setCellValue(0.000); } balanceCell.setCellStyle(balanceStyle);
	 * 
	 * row.createCell(4).setCellValue(item.getRowId());
	 * row.createCell(5).setCellValue(item.getColumnId()); row.createCell(6)
	 * .setCellValue(item.getReportDate() != null ? new
	 * SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : "");
	 * 
	 * // Apply data style for all other cells for (int j = 0; j < 7; j++) { if (j
	 * != 3) { row.getCell(j).setCellStyle(dataStyle); } } } } else {
	 * logger.info("No data found for M_CR — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating M_CR Excel", e);
	 * return new byte[0]; } }
	 */

	/*
	 * public List<Object> getM_CRArchival() { List<Object> M_CRArchivallist = new
	 * ArrayList<>(); try { M_CRArchivallist =
	 * M_CR_Archival_Summary_Repo.getM_CRarchival(); System.out.println("countser"
	 * + M_CRArchivallist.size()); } catch (Exception e) { // Log the exception
	 * System.err.println("Error fetching M_CR Archival data: " + e.getMessage());
	 * e.printStackTrace();
	 * 
	 * // Optionally, you can rethrow it or return empty list // throw new
	 * RuntimeException("Failed to fetch data", e); } return M_CRArchivallist; }
	 */
	
	//Archival View
		public List<Object[]> getM_CRArchival() {
			List<Object[]> archivalList = new ArrayList<>();

			try {
				List<M_CR_Archival_Summary_Entity> repoData = M_CR_Archival_Summary_Repo
						.getdatabydateListWithVersionAll();

				if (repoData != null && !repoData.isEmpty()) {
					for (M_CR_Archival_Summary_Entity entity : repoData) {
						Object[] row = new Object[] {
								entity.getReport_date(), 
								entity.getREPORT_VERSION() 
						};
						archivalList.add(row);
					}

					System.out.println("Fetched " + archivalList.size() + " archival records");
					M_CR_Archival_Summary_Entity first = repoData.get(0);
					System.out.println("Latest archival version: " + first.getReport_date());
				} else {
					System.out.println("No archival data found.");
				}

			} catch (Exception e) {
				System.err.println("Error fetching M_CR Archival data: " + e.getMessage());
				e.printStackTrace();
			}

			return archivalList;
		}
	
	
	public byte[] getExcelM_CRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_CR_Archival_Summary_Entity> dataList = M_CR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CR report. Returning empty result.");
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
					M_CR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					// ROW 10 DOWNLAOD

					Cell cell2 = row.createCell(0);
					if (record.getR10_PRODUCT() != null) {
						cell2.setCellValue(record.getR10_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(1);
					if (record.getR10_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(2);
					if (record.getR10_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(3);
					if (record.getR10_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					// ====================== R11 ======================
					cell2 = row.createCell(0);
					if (record.getR11_PRODUCT() != null) {
						cell2.setCellValue(record.getR11_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR11_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR11_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR11_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R12 ======================
					row = sheet.getRow(11);

					cell2 = row.createCell(0);
					if (record.getR12_PRODUCT() != null) {
						cell2.setCellValue(record.getR12_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR12_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR12_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR12_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R13 ======================
					row = sheet.getRow(12);

					// repeat same pattern...
					cell2 = row.createCell(0);
					if (record.getR13_PRODUCT() != null) {
						cell2.setCellValue(record.getR13_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR13_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR13_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR13_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R14 ======================
					row = sheet.getRow(13);

					cell2 = row.createCell(0);
					if (record.getR14_PRODUCT() != null) {
						cell2.setCellValue(record.getR14_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR14_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR14_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR14_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R15 ======================
					row = sheet.getRow(14);

					cell2 = row.createCell(0);
					if (record.getR15_PRODUCT() != null) {
						cell2.setCellValue(record.getR15_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR15_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR15_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR15_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R16 ======================
					row = sheet.getRow(15);

					cell2 = row.createCell(0);
					if (record.getR16_PRODUCT() != null) {
						cell2.setCellValue(record.getR16_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR16_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR16_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR16_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
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
	
	
	
	public byte[] BRRS_M_CREmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		

		List<M_CR_Summary_Entity> dataList = brrs_M_CR_summary_repo
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CR_email report. Returning empty result.");
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
					M_CR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					// ROW 10 DOWNLAOD

					Cell cell2 = row.createCell(0);
					if (record.getR10_PRODUCT() != null) {
						cell2.setCellValue(record.getR10_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(1);
					if (record.getR10_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(2);
					if (record.getR10_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(3);
					if (record.getR10_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					// ====================== R11 ======================
					cell2 = row.createCell(0);
					if (record.getR11_PRODUCT() != null) {
						cell2.setCellValue(record.getR11_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR11_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR11_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR11_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R12 ======================
					row = sheet.getRow(11);

					cell2 = row.createCell(0);
					if (record.getR12_PRODUCT() != null) {
						cell2.setCellValue(record.getR12_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR12_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR12_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR12_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R13 ======================
					row = sheet.getRow(12);

					// repeat same pattern...
					cell2 = row.createCell(0);
					if (record.getR13_PRODUCT() != null) {
						cell2.setCellValue(record.getR13_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR13_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR13_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR13_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R14 ======================
					row = sheet.getRow(13);

					cell2 = row.createCell(0);
					if (record.getR14_PRODUCT() != null) {
						cell2.setCellValue(record.getR14_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR14_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR14_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR14_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R15 ======================
					row = sheet.getRow(14);

					cell2 = row.createCell(0);
					if (record.getR15_PRODUCT() != null) {
						cell2.setCellValue(record.getR15_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR15_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR15_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR15_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R16 ======================
					row = sheet.getRow(15);

					cell2 = row.createCell(0);
					if (record.getR16_PRODUCT() != null) {
						cell2.setCellValue(record.getR16_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR16_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR16_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR16_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
	
	
	
	
	public byte[] BRRS_M_CRARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_CR_Archival_Summary_Entity> dataList = M_CR_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CR_email_ARCHIVAL report. Returning empty result.");
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
					M_CR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					// ROW 10 DOWNLAOD

					Cell cell2 = row.createCell(0);
					if (record.getR10_PRODUCT() != null) {
						cell2.setCellValue(record.getR10_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(1);
					if (record.getR10_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(2);
					if (record.getR10_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					Cell cell5 = row.createCell(3);
					if (record.getR10_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);

					// ====================== R11 ======================
					cell2 = row.createCell(0);
					if (record.getR11_PRODUCT() != null) {
						cell2.setCellValue(record.getR11_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR11_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR11_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR11_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R12 ======================
					row = sheet.getRow(11);

					cell2 = row.createCell(0);
					if (record.getR12_PRODUCT() != null) {
						cell2.setCellValue(record.getR12_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR12_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR12_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR12_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R13 ======================
					row = sheet.getRow(12);

					// repeat same pattern...
					cell2 = row.createCell(0);
					if (record.getR13_PRODUCT() != null) {
						cell2.setCellValue(record.getR13_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR13_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR13_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR13_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R14 ======================
					row = sheet.getRow(13);

					cell2 = row.createCell(0);
					if (record.getR14_PRODUCT() != null) {
						cell2.setCellValue(record.getR14_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR14_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR14_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR14_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R15 ======================
					row = sheet.getRow(14);

					cell2 = row.createCell(0);
					if (record.getR15_PRODUCT() != null) {
						cell2.setCellValue(record.getR15_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR15_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR15_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR15_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ====================== R16 ======================
					row = sheet.getRow(15);

					cell2 = row.createCell(0);
					if (record.getR16_PRODUCT() != null) {
						cell2.setCellValue(record.getR16_PRODUCT());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(1);
					if (record.getR16_TOTAL_LONG_POS() != null) {
						cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(2);
					if (record.getR16_TOTAL_SHORT_POS() != null) {
						cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(3);
					if (record.getR16_GROSS_OPEN_POS() != null) {
						cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
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
	
	
	
	

	/*
	 * public byte[] getM_CRDetailExcelARCHIVAL(String filename, String fromdate,
	 * String todate, String currency, String dtltype, String type, String version)
	 * { try { logger.info("Generating Excel for M_CR ARCHIVAL Details...");
	 * System.out.println("came to Detail download service"); if
	 * (type.equals("ARCHIVAL") & version != null) {
	 * 
	 * } XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("M_CRDetail");
	 * 
	 * // Common border style BorderStyle border = BorderStyle.THIN;
	 * 
	 * // Header style (left aligned) CellStyle headerStyle =
	 * workbook.createCellStyle(); Font headerFont = workbook.createFont();
	 * headerFont.setBold(true); headerFont.setFontHeightInPoints((short) 10);
	 * headerStyle.setFont(headerFont);
	 * headerStyle.setAlignment(HorizontalAlignment.LEFT);
	 * headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	 * headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	 * headerStyle.setBorderTop(border); headerStyle.setBorderBottom(border);
	 * headerStyle.setBorderLeft(border); headerStyle.setBorderRight(border);
	 * 
	 * // Right-aligned header style for ACCT BALANCE CellStyle
	 * rightAlignedHeaderStyle = workbook.createCellStyle();
	 * rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
	 * rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * 
	 * // Default data style (left aligned) CellStyle dataStyle =
	 * workbook.createCellStyle(); dataStyle.setAlignment(HorizontalAlignment.LEFT);
	 * dataStyle.setBorderTop(border); dataStyle.setBorderBottom(border);
	 * dataStyle.setBorderLeft(border); dataStyle.setBorderRight(border);
	 * 
	 * // ACCT BALANCE style (right aligned with 3 decimals) CellStyle balanceStyle
	 * = workbook.createCellStyle();
	 * balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
	 * balanceStyle.setBorderTop(border); balanceStyle.setBorderBottom(border);
	 * balanceStyle.setBorderLeft(border); balanceStyle.setBorderRight(border);
	 * 
	 * // Header row String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME",
	 * "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE" };
	 * 
	 * XSSFRow headerRow = sheet.createRow(0); for (int i = 0; i < headers.length;
	 * i++) { Cell cell = headerRow.createCell(i); cell.setCellValue(headers[i]);
	 * 
	 * if (i == 3) { // ACCT BALANCE cell.setCellStyle(rightAlignedHeaderStyle); }
	 * else { cell.setCellStyle(headerStyle); }
	 * 
	 * sheet.setColumnWidth(i, 5000); }
	 * 
	 * // Get data Date parsedToDate = new
	 * SimpleDateFormat("dd/MM/yyyy").parse(todate);
	 * List<M_CR_Archival_Detail_Entity> reportData =
	 * M_CR_Archival_Detail_Repo.getdatabydateList(parsedToDate, version);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_CR_Archival_Detail_Entity item : reportData) { XSSFRow row =
	 * sheet.createRow(rowIndex++);
	 * 
	 * row.createCell(0).setCellValue(item.getCustId());
	 * row.createCell(1).setCellValue(item.getAcctNumber());
	 * row.createCell(2).setCellValue(item.getAcctName());
	 * 
	 * // ACCT BALANCE (right aligned, 3 decimal places) Cell balanceCell =
	 * row.createCell(3); if (item.getAcctBalanceInPula() != null) {
	 * balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue()); } else {
	 * balanceCell.setCellValue(0.000); } balanceCell.setCellStyle(balanceStyle);
	 * 
	 * row.createCell(4).setCellValue(item.getRowId());
	 * row.createCell(5).setCellValue(item.getColumnId()); row.createCell(6)
	 * .setCellValue(item.getReportDate() != null ? new
	 * SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : "");
	 * 
	 * // Apply data style for all other cells for (int j = 0; j < 7; j++) { if (j
	 * != 3) { row.getCell(j).setCellStyle(dataStyle); }
	 * 
	 * } } } else {
	 * logger.info("No data found for M_CR — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating M_CRExcel", e);
	 * return new byte[0]; } }
	 */
	
	
	



	@Transactional
	public void updateReport(M_CR_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    // 1️⃣ Fetch existing SUMMARY
	    M_CR_Summary_Entity existingSummary =
	            brrs_M_CR_summary_repo.findById(updatedEntity.getReport_date())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    // 2️⃣ Fetch or create DETAIL
	    M_CR_Detail_Entity existingDetail =
	            brrs_M_CR_detail_repo.findById(updatedEntity.getReport_date())
	                    .orElseGet(() -> {
	                        M_CR_Detail_Entity d = new M_CR_Detail_Entity();
	                        d.setReport_date(updatedEntity.getReport_date());
	                        return d;
	                    });
	    
	    try {
			// 1️⃣ Loop from R11 to R50 and copy fields
			for (int i = 10; i <= 17; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "PRODUCT", "TOTAL_LONG_POS", "TOTAL_SHORT_POS", "GROSS_OPEN_POS",
						"CHARGE_BASIS_RISK", "CAPITAL_CHARGE_BASIS_RISK", "NET_OPEN_POS", "CHARGE_DIR_RISK",
						"CAPITAL_CHARGE_DIR_RISK", "TOTAL_CAPITAL_CHARGE" };


	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter =
	                            M_CR_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter =
	                            M_CR_Summary_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Method detailSetter =
	                            M_CR_Detail_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);

	                    // ✅ set into SUMMARY
	                    summarySetter.invoke(existingSummary, newValue);

	                    // ✅ set into DETAIL
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    // skip missing fields safely
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save BOTH (same transaction)
	    brrs_M_CR_summary_repo.save(existingSummary);
	    brrs_M_CR_detail_repo.save(existingDetail);
	}


  
	
	
	
	
	public List<Object[]> getM_CRResub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<M_CR_Archival_Summary_Entity> latestArchivalList = 
	        		M_CR_Archival_Summary_Repo.getdatabydateListWithVersionAll();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (M_CR_Archival_Summary_Entity entity : latestArchivalList) {
	                Object[] row = new Object[] {
	                    entity.getReport_date(),
	                    entity.getREPORT_VERSION()
	                };
	                resubList.add(row);
	            }
	            System.out.println("Fetched " + resubList.size() + " record(s)");
	        } else {
	            System.out.println("No archival data found.");
	        }
	    } catch (Exception e) {
	        System.err.println("Error fetching M_CR Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}
	
	
	
	
	
	/*
	 * public void updateReportResub(M_CR_Summary_Entity updatedEntity) {
	 * System.out.println("Came to Resub Service");
	 * System.out.println("Report Date: " + updatedEntity.getReport_date());
	 * 
	 * // Use entity field directly (same name as in entity) Date report_date =
	 * updatedEntity.getReport_date(); int newVersion = 1;
	 * 
	 * try { // ✅ use the same variable name as in repo method
	 * Optional<M_CR_Archival_Summary_Entity> latestArchivalOpt =
	 * M_CR_Archival_Summary_Repo.getLatestArchivalVersionByDate(report_date);
	 * 
	 * // Determine next version if (latestArchivalOpt.isPresent()) {
	 * M_CR_Archival_Summary_Entity latestArchival = latestArchivalOpt.get(); try {
	 * newVersion = Integer.parseInt(latestArchival.getReport_version()) + 1; }
	 * catch (NumberFormatException e) {
	 * System.err.println("Invalid version format. Defaulting to version 1");
	 * newVersion = 1; } } else {
	 * System.out.println("No previous archival found for date: " + report_date); }
	 * 
	 * // Prevent duplicate version boolean exists = M_CR_Archival_Summary_Repo
	 * .findByReport_dateAndReport_version(report_date,
	 * BigDecimal.valueOf(newVersion)) .isPresent();
	 * 
	 * if (exists) { throw new RuntimeException("Version " + newVersion +
	 * " already exists for report date " + report_date); }
	 * 
	 * // Copy summary entity to archival entity M_CR_Archival_Summary_Entity
	 * archivalEntity = new M_CR_Archival_Summary_Entity();
	 * org.springframework.beans.BeanUtils.copyProperties(updatedEntity,
	 * archivalEntity);
	 * 
	 * archivalEntity.setReport_date(report_date);
	 * archivalEntity.setReport_version(String.valueOf(newVersion));
	 * archivalEntity.setReportResubDate(new Date());
	 * 
	 * System.out.println("Saving new archival version: " + newVersion);
	 * M_CR_Archival_Summary_Repo.save(archivalEntity);
	 * 
	 * System.out.println("Saved archival version successfully: " + newVersion);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); throw new
	 * RuntimeException("Error while creating archival resubmission record", e); } }
	 */


	
	/// Downloaded for Archival & Resub
		public byte[] BRRS_M_CRResubExcel(String filename, String reportId, String fromdate,
	        String todate, String currency, String dtltype,
	        String type, BigDecimal version) throws Exception {

	    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

	    if (type.equals("RESUB") & version != null) {
	       
	    }

	    List<M_CR_Archival_Summary_Entity> dataList =
	    		M_CR_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

	    if (dataList.isEmpty()) {
	        logger.warn("Service: No data found for M_CR report. Returning empty result.");
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

						M_CR_Archival_Summary_Entity  record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// ROW 10 DOWNLAOD

						Cell cell2 = row.createCell(0);
						if (record.getR10_PRODUCT() != null) {
							cell2.setCellValue(record.getR10_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(1);
						if (record.getR10_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR10_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(2);
						if (record.getR10_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR10_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						Cell cell5 = row.createCell(3);
						if (record.getR10_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR10_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						row = sheet.getRow(10);

						// ====================== R11 ======================
						cell2 = row.createCell(0);
						if (record.getR11_PRODUCT() != null) {
							cell2.setCellValue(record.getR11_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(1);
						if (record.getR11_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR11_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(2);
						if (record.getR11_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR11_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(3);
						if (record.getR11_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR11_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// ====================== R12 ======================
						row = sheet.getRow(11);

						cell2 = row.createCell(0);
						if (record.getR12_PRODUCT() != null) {
							cell2.setCellValue(record.getR12_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(1);
						if (record.getR12_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR12_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(2);
						if (record.getR12_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR12_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(3);
						if (record.getR12_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR12_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// ====================== R13 ======================
						row = sheet.getRow(12);

						// repeat same pattern...
						cell2 = row.createCell(0);
						if (record.getR13_PRODUCT() != null) {
							cell2.setCellValue(record.getR13_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(1);
						if (record.getR13_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR13_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(2);
						if (record.getR13_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR13_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(3);
						if (record.getR13_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR13_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// ====================== R14 ======================
						row = sheet.getRow(13);

						cell2 = row.createCell(0);
						if (record.getR14_PRODUCT() != null) {
							cell2.setCellValue(record.getR14_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(1);
						if (record.getR14_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR14_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(2);
						if (record.getR14_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR14_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(3);
						if (record.getR14_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR14_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// ====================== R15 ======================
						row = sheet.getRow(14);

						cell2 = row.createCell(0);
						if (record.getR15_PRODUCT() != null) {
							cell2.setCellValue(record.getR15_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(1);
						if (record.getR15_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR15_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(2);
						if (record.getR15_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR15_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(3);
						if (record.getR15_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR15_GROSS_OPEN_POS().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						// ====================== R16 ======================
						row = sheet.getRow(15);

						cell2 = row.createCell(0);
						if (record.getR16_PRODUCT() != null) {
							cell2.setCellValue(record.getR16_PRODUCT());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(1);
						if (record.getR16_TOTAL_LONG_POS() != null) {
							cell3.setCellValue(record.getR16_TOTAL_LONG_POS().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(2);
						if (record.getR16_TOTAL_SHORT_POS() != null) {
							cell4.setCellValue(record.getR16_TOTAL_SHORT_POS().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(3);
						if (record.getR16_GROSS_OPEN_POS() != null) {
							cell5.setCellValue(record.getR16_GROSS_OPEN_POS().doubleValue());
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
	
	
	
	
	
		/*
		 * public void updateDetailFromForm(Date reportDate, Map<String, String> params)
		 * {
		 * 
		 * System.out.println("came to service for update ");
		 * 
		 * for (Map.Entry<String, String> entry : params.entrySet()) {
		 * 
		 * String key = entry.getKey(); String value = entry.getValue();
		 * 
		 * // ✅ Allow only valid keys for required columns if
		 * (!key.matches("R\\d+_C\\d+_(" + "MARKET|" + "GPFSR_NOM_AMT|" +
		 * "GPFSR_POS_ATT8_PER_SPE_RIS|" + "GPFSR_CHRG|" + "GPFSR_NOM_AMT1|" +
		 * "GPFSR_POS_ATT4_PER_SPE_RIS|" + "GPFSR_CHRG1|" + "GPFSR_NOM_AMT2|" +
		 * "GPFSR_POS_ATT2_PER_SPE_RIS|" + "GPFSR_CHRG2|" + "TOT_SPE_RIS_CHRG|" +
		 * "NET_POS_GEN_MAR_RIS|" + "GEN_MAR_RIS_CHRG_8PER|" +
		 * "2PER_GEN_MAR_RIS_CHRG_DIV_PORT|" + "TOT_GEN_MAR_RISK_CHRG|" +
		 * "TOT_MAR_RIS_CHRG|" + ")")) { continue; }
		 * 
		 * if (value == null || value.trim().isEmpty()) { value = "0"; }
		 * 
		 * String[] parts = key.split("_"); String reportLabel = parts[0]; // R1, R2,
		 * etc. String addlCriteria = parts[1]; // C1, C2, etc. String column =
		 * String.join("_", Arrays.copyOfRange(parts, 2, parts.length));
		 * 
		 * BigDecimal amount = new BigDecimal(value);
		 * 
		 * List<M_CR_Detail_Entity> rows = brrs_M_CR_detail_repo
		 * .findByReportDateAndReportLableAndReportAddlCriteria1(reportDate,
		 * reportLabel, addlCriteria);
		 * 
		 * for (M_CR_Detail_Entity row : rows) {
		 * 
		 * if ("MARKET".equals(column)) { row.setMARKET(amount);
		 * 
		 * } else if ("GPFSR_NOM_AMT".equals(column)) { row.setGPFSR_NOM_AMT(amount);
		 * 
		 * } else if ("GPFSR_POS_ATT8_PER_SPE_RIS".equals(column)) {
		 * row.setGPFSR_POS_ATT8_PER_SPE_RIS(amount);
		 * 
		 * }
		 * 
		 * else if ("GPFSR_CHRG".equals(column)) { row.setGPFSR_CHRG(amount);
		 * 
		 * }
		 * 
		 * else if ("GPFSR_NOM_AMT1".equals(column)) { row.setGPFSR_NOM_AMT1(amount);
		 * 
		 * }
		 * 
		 * else if ("GPFSR_POS_ATT4_PER_SPE_RIS".equals(column)) {
		 * row.setGPFSR_NOM_AMT1(amount);
		 * 
		 * }
		 * 
		 * 
		 * 
		 * }
		 * 
		 * brrs_M_CR_detail_repo.saveAll(rows); }
		 * 
		 * // ✅ CALL ORACLE PROCEDURE AFTER ALL UPDATES
		 * callSummaryProcedure(reportDate); }
		 * 
		 * private void callSummaryProcedure(Date reportDate) {
		 * 
		 * String sql = "{ call BRRS_M_CR_SUMMARY_PROCEDURE(?) }";
		 * 
		 * jdbcTemplate.update(connection -> { CallableStatement cs =
		 * connection.prepareCall(sql);
		 * 
		 * SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		 * sdf.setLenient(false);
		 * 
		 * String formattedDate = sdf.format(reportDate);
		 * 
		 * cs.setString(1, formattedDate); return cs; });
		 * 
		 * System.out.println( "✅ Summary procedure executed for date: " + new
		 * SimpleDateFormat("dd-MM-yyyy").format(reportDate)); }
		 */

	
	
	
	
	
	
	
	
	

}