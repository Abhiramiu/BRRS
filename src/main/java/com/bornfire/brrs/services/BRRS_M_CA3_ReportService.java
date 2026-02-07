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
import java.util.Map;

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

import com.bornfire.brrs.entities.BRRS_M_CA3_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA3_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA3_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA3_Summary_Repo;
import com.bornfire.brrs.entities.M_CA3_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_CA3_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA3_Detail_Entity;
import com.bornfire.brrs.entities.M_CA3_Summary_Entity;

@Component
@Service

public class BRRS_M_CA3_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA3_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	BRRS_M_CA3_Detail_Repo brrs_M_CA3_detail_repo;

	@Autowired
	BRRS_M_CA3_Summary_Repo brrs_M_CA3_summary_repo;

	@Autowired
	BRRS_M_CA3_Archival_Detail_Repo M_CA3_Archival_Detail_Repo;

	@Autowired
	BRRS_M_CA3_Archival_Summary_Repo M_CA3_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	/*
	 * public ModelAndView getM_CA3View(String reportId, String fromdate, String
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
	 * List<M_CA3_Archival_Summary_Entity> T1Master = new
	 * ArrayList<M_CA3_Archival_Summary_Entity>(); System.out.println(version); try
	 * { Date d1 = dateformat.parse(todate);
	 * 
	 * 
	 * 
	 * T1Master =
	 * M_CA3_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate
	 * ), version);
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * 
	 * mv.addObject("reportsummary", T1Master); } else { List<M_CA3_Summary_Entity>
	 * T1Master = new ArrayList<M_CA3_Summary_Entity>(); try { Date d1 =
	 * dateformat.parse(todate);
	 * 
	 * 
	 * 
	 * T1Master =
	 * brrs_M_CA3_summary_repo.getdatabydateList(dateformat.parse(todate));
	 * mv.addObject("report_date", dateformat.format(d1));
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * mv.addObject("reportsummary", T1Master); }
	 * 
	 * 
	 * mv.setViewName("BRRS/M_CA3");
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

	public ModelAndView getM_CA3View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

				List<M_CA3_Archival_Summary_Entity> T1Master = M_CA3_Archival_Summary_Repo.getdatabydateListarchival(d1,
						version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_CA3_Archival_Summary_Entity> T1Master = M_CA3_Archival_Summary_Repo.getdatabydateListarchival(d1,
						version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<M_CA3_Summary_Entity> T1Master = brrs_M_CA3_summary_repo
						.getdatabydateList(dateformat.parse(todate));
				mv.addObject("displaymode", "summary");
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if (version != null) {

					List<M_CA3_Archival_Detail_Entity> T1Master = M_CA3_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_CA3_Detail_Entity> T1Master = brrs_M_CA3_detail_repo
							.getdatabydateList(dateformat.parse(todate));
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_CA3");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	/*
	 * public ModelAndView getM_CA3currentDtl(String reportId, String fromdate,
	 * String todate, String currency, String dtltype, Pageable pageable, String
	 * type, BigDecimal version) {
	 * 
	 * ModelAndView mv = new ModelAndView();
	 * 
	 * 
	 * System.out.println("testing"); System.out.println(version);
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) { System.out.println(type);
	 * List<M_CA3_Archival_Detail_Entity> T1Master = new
	 * ArrayList<M_CA3_Archival_Detail_Entity>();
	 * 
	 * System.out.println(version); try {
	 * 
	 * 
	 * T1Master =
	 * M_CA3_Archival_Detail_Repo.getdatabydateListarchival(dateformat.parse(todate)
	 * , version); System.out.println("DETAIL");
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * 
	 * mv.addObject("reportsummary", T1Master);
	 * 
	 * } else {
	 * 
	 * List<M_CA3_Detail_Entity> T1Master = new ArrayList<M_CA3_Detail_Entity>();
	 * 
	 * try { Date d1 = dateformat.parse(todate);
	 * 
	 * T1Master =
	 * brrs_M_CA3_detail_repo.getdatabydateList(dateformat.parse(todate));
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
	 * mv.setViewName("BRRS/M_CA3");
	 * 
	 * mv.addObject("displaymode", "summary");
	 * 
	 * System.out.println("scv" + mv.getViewName());
	 * 
	 * return mv; }
	 */

	public byte[] getM_CA3Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_CA3ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		// Email check
		if ("email".equalsIgnoreCase(type) && version == null) {
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_CA3EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		} else if ("email".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_CA3ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);

		}

		/* ===================== NORMAL ===================== */
		List<M_CA3_Summary_Entity> dataList = brrs_M_CA3_summary_repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA3 report. Returning empty result.");
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
					M_CA3_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//row10
					// Column b
					
					//column c
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					
									
					
					//row11
					row = sheet.getRow(10);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row12
					row = sheet.getRow(11);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row13
					row = sheet.getRow(12);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					

					//row14
					row = sheet.getRow(13);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					

					//row15
					row = sheet.getRow(14);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row16
					row = sheet.getRow(15);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row17
					row = sheet.getRow(16);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row18
					row = sheet.getRow(17);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row19
					row = sheet.getRow(18);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row20
					row = sheet.getRow(19);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
											}

					//row24
					row = sheet.getRow(23);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row25
					row = sheet.getRow(24);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row26
					row = sheet.getRow(25);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row27
					row = sheet.getRow(26);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row28
					row = sheet.getRow(27);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
											}

					//row29
					row = sheet.getRow(28);			
					// Column b
					
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row36
					row = sheet.getRow(35);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//row37
					row = sheet.getRow(36);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row38
					row = sheet.getRow(37);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row39
					row = sheet.getRow(38);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row40
					row = sheet.getRow(39);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row41
					row = sheet.getRow(40);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR41_AMOUNT() != null) {
						cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row44
					row = sheet.getRow(43);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row45
					row = sheet.getRow(44);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR45_AMOUNT() != null) {
						cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row46
					row = sheet.getRow(45);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR46_AMOUNT() != null) {
						cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
			
					} else {
						cell2.setCellValue("");

					}

					//row50
					row = sheet.getRow(49);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row51
					row = sheet.getRow(50);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row52
					row = sheet.getRow(51);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row53
					row = sheet.getRow(52);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row54
					row = sheet.getRow(53);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row55
					row = sheet.getRow(54);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

				

					//row58
					row = sheet.getRow(57);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row59
					row = sheet.getRow(58);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR59_AMOUNT() != null) {
						cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row60
					row = sheet.getRow(14);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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
	 * public byte[] getM_CA3DetailExcel(String filename, String fromdate, String
	 * todate, String currency, String dtltype, String type, String version){ try {
	 * logger.info("Generating Excel for M_CA3 Details...");
	 * System.out.println("came to Detail download service");
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) { byte[] ARCHIVALreport =
	 * getM_CA3DetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
	 * type, version); return ARCHIVALreport; }
	 * 
	 * XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("M_CA3Details");
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
	 * SimpleDateFormat("dd/MM/yyyy").parse(todate); List<M_CA3_Detail_Entity>
	 * reportData = brrs_M_CA3_detail_repo.getdatabydateList(parsedToDate);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_CA3_Detail_Entity item : reportData) { XSSFRow row =
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
	 * logger.info("No data found for M_CA3 — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating M_CA3 Excel", e);
	 * return new byte[0]; } }
	 */

	/*
	 * public List<Object> getM_CA3Archival() { List<Object> M_CA3Archivallist = new
	 * ArrayList<>(); try { M_CA3Archivallist =
	 * M_CA3_Archival_Summary_Repo.getM_CA3archival(); System.out.println("countser"
	 * + M_CA3Archivallist.size()); } catch (Exception e) { // Log the exception
	 * System.err.println("Error fetching M_CA3 Archival data: " + e.getMessage());
	 * e.printStackTrace();
	 * 
	 * // Optionally, you can rethrow it or return empty list // throw new
	 * RuntimeException("Failed to fetch data", e); } return M_CA3Archivallist; }
	 */

	// Archival View
	public List<Object[]> getM_CA3Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_CA3_Archival_Summary_Entity> repoData = M_CA3_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_CA3_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_date() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_CA3_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_CA3 Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public byte[] getExcelM_CA3ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_CA3_Archival_Summary_Entity> dataList = M_CA3_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA3 report. Returning empty result.");
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
					M_CA3_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//row10
					// Column b
					
					//column c
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					
									
					
					//row11
					row = sheet.getRow(10);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row12
					row = sheet.getRow(11);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row13
					row = sheet.getRow(12);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					

					//row14
					row = sheet.getRow(13);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					

					//row15
					row = sheet.getRow(14);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row16
					row = sheet.getRow(15);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row17
					row = sheet.getRow(16);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row18
					row = sheet.getRow(17);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row19
					row = sheet.getRow(18);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row20
					row = sheet.getRow(19);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
											}

					//row24
					row = sheet.getRow(23);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row25
					row = sheet.getRow(24);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row26
					row = sheet.getRow(25);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row27
					row = sheet.getRow(26);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row28
					row = sheet.getRow(27);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
											}

					//row29
					row = sheet.getRow(28);			
					// Column b
					
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row36
					row = sheet.getRow(35);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//row37
					row = sheet.getRow(36);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row38
					row = sheet.getRow(37);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row39
					row = sheet.getRow(38);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row40
					row = sheet.getRow(39);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row41
					row = sheet.getRow(40);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR41_AMOUNT() != null) {
						cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row44
					row = sheet.getRow(43);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row45
					row = sheet.getRow(44);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR45_AMOUNT() != null) {
						cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row46
					row = sheet.getRow(45);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR46_AMOUNT() != null) {
						cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
			
					} else {
						cell2.setCellValue("");

					}

					//row50
					row = sheet.getRow(49);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row51
					row = sheet.getRow(50);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row52
					row = sheet.getRow(51);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row53
					row = sheet.getRow(52);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row54
					row = sheet.getRow(53);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row55
					row = sheet.getRow(54);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

				

					//row58
					row = sheet.getRow(57);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row59
					row = sheet.getRow(58);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR59_AMOUNT() != null) {
						cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row60
					row = sheet.getRow(14);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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

	public byte[] BRRS_M_CA3EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		List<M_CA3_Summary_Entity> dataList = brrs_M_CA3_summary_repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA3_email report. Returning empty result.");
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
					M_CA3_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//row10
					// Column b
					
					//column c
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					
									
					
					//row11
					row = sheet.getRow(10);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row12
					row = sheet.getRow(11);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row13
					row = sheet.getRow(12);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					

					//row14
					row = sheet.getRow(13);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					

					//row15
					row = sheet.getRow(14);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row16
					row = sheet.getRow(15);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row17
					row = sheet.getRow(16);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row18
					row = sheet.getRow(17);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row19
					row = sheet.getRow(18);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row20
					row = sheet.getRow(19);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
											}

					//row24
					row = sheet.getRow(23);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row25
					row = sheet.getRow(24);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row26
					row = sheet.getRow(25);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row27
					row = sheet.getRow(26);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row28
					row = sheet.getRow(27);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
											}

					//row29
					row = sheet.getRow(28);			
					// Column b
					
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row36
					row = sheet.getRow(33);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//row37
					row = sheet.getRow(34);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row38
					row = sheet.getRow(35);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row39
					row = sheet.getRow(36);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row40
					row = sheet.getRow(40);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row41
					row = sheet.getRow(38);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR41_AMOUNT() != null) {
						cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row44
					row = sheet.getRow(41);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row45
					row = sheet.getRow(44);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR45_AMOUNT() != null) {
						cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row46
					row = sheet.getRow(45);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR46_AMOUNT() != null) {
						cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
			
					} else {
						cell2.setCellValue("");

					}

					//row50
					row = sheet.getRow(46);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row51
					row = sheet.getRow(47);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row52
					row = sheet.getRow(48);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row53
					row = sheet.getRow(49);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row54
					row = sheet.getRow(53);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row55
					row = sheet.getRow(50);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

				

					//row58
					row = sheet.getRow(54);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * //row59 row = sheet.getRow(58); // Column b
					 * 
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR59_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR59_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					//row60
					row = sheet.getRow(54);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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

	public byte[] BRRS_M_CA3ARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_CA3_Archival_Summary_Entity> dataList = M_CA3_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA3_email_ARCHIVAL report. Returning empty result.");
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
					M_CA3_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//row10
					// Column b
					
					//column c
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					
									
					
					//row11
					row = sheet.getRow(10);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row12
					row = sheet.getRow(11);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row13
					row = sheet.getRow(12);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					

					//row14
					row = sheet.getRow(13);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					

					//row15
					row = sheet.getRow(14);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row16
					row = sheet.getRow(15);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row17
					row = sheet.getRow(16);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row18
					row = sheet.getRow(17);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row19
					row = sheet.getRow(18);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row20
					row = sheet.getRow(19);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
											}

					//row24
					row = sheet.getRow(23);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row25
					row = sheet.getRow(24);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row26
					row = sheet.getRow(25);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row27
					row = sheet.getRow(26);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row28
					row = sheet.getRow(27);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
											}

					//row29
					row = sheet.getRow(28);			
					// Column b
					
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row36
					row = sheet.getRow(33);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//row37
					row = sheet.getRow(34);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row38
					row = sheet.getRow(35);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row39
					row = sheet.getRow(36);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row40
					row = sheet.getRow(40);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row41
					row = sheet.getRow(38);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR41_AMOUNT() != null) {
						cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row44
					row = sheet.getRow(41);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row45
					row = sheet.getRow(44);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR45_AMOUNT() != null) {
						cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row46
					row = sheet.getRow(45);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR46_AMOUNT() != null) {
						cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
			
					} else {
						cell2.setCellValue("");

					}

					//row50
					row = sheet.getRow(46);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row51
					row = sheet.getRow(47);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row52
					row = sheet.getRow(48);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row53
					row = sheet.getRow(49);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row54
					row = sheet.getRow(53);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row55
					row = sheet.getRow(50);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

				

					//row58
					row = sheet.getRow(54);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					/*
					 * //row59 row = sheet.getRow(58); // Column b
					 * 
					 * 
					 * // Column c cell2 = row.getCell(2); if (record.getR59_AMOUNT() != null) {
					 * cell2.setCellValue(record.getR59_AMOUNT().doubleValue());
					 * 
					 * } else { cell2.setCellValue("");
					 * 
					 * }
					 */

					//row60
					row = sheet.getRow(54);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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
	 * public byte[] getM_CA3DetailExcelARCHIVAL(String filename, String fromdate,
	 * String todate, String currency, String dtltype, String type, String version)
	 * { try { logger.info("Generating Excel for M_CA3 ARCHIVAL Details...");
	 * System.out.println("came to Detail download service"); if
	 * (type.equals("ARCHIVAL") & version != null) {
	 * 
	 * } XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("M_CA3Detail");
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
	 * List<M_CA3_Archival_Detail_Entity> reportData =
	 * M_CA3_Archival_Detail_Repo.getdatabydateList(parsedToDate, version);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_CA3_Archival_Detail_Entity item : reportData) { XSSFRow row =
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
	 * logger.info("No data found for M_CA3 — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating M_CA3Excel", e);
	 * return new byte[0]; } }
	 */

	@Transactional
	public void updateReport(M_CA3_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		// 1️⃣ Fetch existing SUMMARY
		M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		// 2️⃣ Fetch or create DETAIL
		M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseGet(() -> {
					M_CA3_Detail_Entity d = new M_CA3_Detail_Entity();
					d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
					return d;
				});

		try {

			// 🔁 Loop R11 → R23
			for (int i = 11; i <= 25; i++) {

				String prefix = "R" + i + "_";

				String[] fields = { "TOTAL" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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
		brrs_M_CA3_summary_repo.save(existingSummary);
		brrs_M_CA3_detail_repo.save(existingDetail);
	}

	
	@Transactional
	public void updateReport2(M_CA3_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    // 1️⃣ Fetch existing SUMMARY
	    M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    // 2️⃣ Fetch or create DETAIL
	    M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseGet(() -> {
	                M_CA3_Detail_Entity d = new M_CA3_Detail_Entity();
	                d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
	                return d;
	            });

	    try {

	        // 🔁 Loop R24 → R27 (AMOUNT)
	        for (int i = 24; i <= 27; i++) {

	            String prefix = "R" + i + "_";
	            String[] fields = { "AMOUNT" };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                    Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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

	        // 🔁 Handle R28 and R29 (AMOUNT)
	        int[] totals = {28, 29};
	        for (int i : totals) {

	            String prefix = "R" + i + "_";
	            String[] fields = { "AMOUNT" };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                    Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);

	                    // ✅ set into SUMMARY
	                    summarySetter.invoke(existingSummary, newValue);

	                    // ✅ set into DETAIL
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save BOTH (same transaction)
	    brrs_M_CA3_summary_repo.save(existingSummary);
	    brrs_M_CA3_detail_repo.save(existingDetail);
	}

	
	@Transactional
	public void updateReport3(M_CA3_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    // 1️⃣ Fetch existing SUMMARY
	    M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    // 2️⃣ Fetch or create DETAIL
	    M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseGet(() -> {
	                M_CA3_Detail_Entity d = new M_CA3_Detail_Entity();
	                d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
	                return d;
	            });

	    try {

	        // 🔁 Loop R36 → R40 (AMOUNT)
	        for (int i = 36; i <= 40; i++) {

	            String prefix = "R" + i + "_";
	            String[] fields = { "AMOUNT" };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                    Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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

	        // 🔁 Handle R41 (AMOUNT)
	        String[] totalFields = { "AMOUNT" };
	        for (String field : totalFields) {

	            String getterName = "getR41_" + field;
	            String setterName = "setR41_" + field;

	            try {
	                Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

	                Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

	                Object newValue = getter.invoke(updatedEntity);

	                // ✅ set into SUMMARY
	                summarySetter.invoke(existingSummary, newValue);

	                // ✅ set into DETAIL
	                detailSetter.invoke(existingDetail, newValue);

	            } catch (NoSuchMethodException e) {
	                continue;
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save BOTH (same transaction)
	    brrs_M_CA3_summary_repo.save(existingSummary);
	    brrs_M_CA3_detail_repo.save(existingDetail);
	}

	
	@Transactional
	public void updateReport4(M_CA3_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    // 1️⃣ Fetch existing SUMMARY
	    M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    // 2️⃣ Fetch or create DETAIL
	    M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseGet(() -> {
	                M_CA3_Detail_Entity d = new M_CA3_Detail_Entity();
	                d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
	                return d;
	            });

	    try {

	        // 🔁 Copy R44, R45, R46 (AMOUNT)
	        int[] rows = {44, 45, 46};

	        for (int i : rows) {

	            String prefix = "R" + i + "_";
	            String[] fields = { "AMOUNT" };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                    Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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
	    brrs_M_CA3_summary_repo.save(existingSummary);
	    brrs_M_CA3_detail_repo.save(existingDetail);
	}
	
	@Transactional
	public void updateReport5(M_CA3_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    // 1️⃣ Fetch existing SUMMARY
	    M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    // 2️⃣ Fetch or create DETAIL
	    M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseGet(() -> {
	                M_CA3_Detail_Entity d = new M_CA3_Detail_Entity();
	                d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
	                return d;
	            });

	    try {

	        // 🔁 Loop R50 → R55 (AMOUNT)
	        for (int i = 50; i <= 55; i++) {

	            String prefix = "R" + i + "_";
	            String[] fields = { "AMOUNT" };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                    Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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
	    brrs_M_CA3_summary_repo.save(existingSummary);
	    brrs_M_CA3_detail_repo.save(existingDetail);
	}

	@Transactional
	public void updateReport6(M_CA3_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    // 1️⃣ Fetch existing SUMMARY
	    M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    // 2️⃣ Fetch or create DETAIL
	    M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
	            .orElseGet(() -> {
	                M_CA3_Detail_Entity d = new M_CA3_Detail_Entity();
	                d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
	                return d;
	            });

	    try {

	        // 🔁 Loop R58 → R60 (AMOUNT)
	        for (int i = 58; i <= 60; i++) {

	            String prefix = "R" + i + "_";
	            String[] fields = { "AMOUNT" };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                    Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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
	    brrs_M_CA3_summary_repo.save(existingSummary);
	    brrs_M_CA3_detail_repo.save(existingDetail);
	}

	

	public List<Object[]> getM_CA3Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_CA3_Archival_Summary_Entity> latestArchivalList = M_CA3_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA3_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_date() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_CA3 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	/*
	 * public void updateReportResub(M_CA3_Summary_Entity updatedEntity) {
	 * System.out.println("Came to Resub Service");
	 * System.out.println("Report Date: " + updatedEntity.getReport_date());
	 * 
	 * // Use entity field directly (same name as in entity) Date report_date =
	 * updatedEntity.getReport_date(); int newVersion = 1;
	 * 
	 * try { // ✅ use the same variable name as in repo method
	 * Optional<M_CA3_Archival_Summary_Entity> latestArchivalOpt =
	 * M_CA3_Archival_Summary_Repo.getLatestArchivalVersionByDate(report_date);
	 * 
	 * // Determine next version if (latestArchivalOpt.isPresent()) {
	 * M_CA3_Archival_Summary_Entity latestArchival = latestArchivalOpt.get(); try {
	 * newVersion = Integer.parseInt(latestArchival.getReport_version()) + 1; }
	 * catch (NumberFormatException e) {
	 * System.err.println("Invalid version format. Defaulting to version 1");
	 * newVersion = 1; } } else {
	 * System.out.println("No previous archival found for date: " + report_date); }
	 * 
	 * // Prevent duplicate version boolean exists = M_CA3_Archival_Summary_Repo
	 * .findByReport_dateAndReport_version(report_date,
	 * BigDecimal.valueOf(newVersion)) .isPresent();
	 * 
	 * if (exists) { throw new RuntimeException("Version " + newVersion +
	 * " already exists for report date " + report_date); }
	 * 
	 * // Copy summary entity to archival entity M_CA3_Archival_Summary_Entity
	 * archivalEntity = new M_CA3_Archival_Summary_Entity();
	 * org.springframework.beans.BeanUtils.copyProperties(updatedEntity,
	 * archivalEntity);
	 * 
	 * archivalEntity.setReport_date(report_date);
	 * archivalEntity.setReport_version(String.valueOf(newVersion));
	 * archivalEntity.setReportResubDate(new Date());
	 * 
	 * System.out.println("Saving new archival version: " + newVersion);
	 * M_CA3_Archival_Summary_Repo.save(archivalEntity);
	 * 
	 * System.out.println("Saved archival version successfully: " + newVersion);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); throw new
	 * RuntimeException("Error while creating archival resubmission record", e); } }
	 */

	/// Downloaded for Archival & Resub
	public byte[] BRRS_M_CA3ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

		if (type.equals("RESUB") & version != null) {

		}

		List<M_CA3_Archival_Summary_Entity> dataList = M_CA3_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA3 report. Returning empty result.");
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

					M_CA3_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//row10
					// Column b
					
					//column c
					Cell cell2 = row.createCell(2);
					if (record.getR10_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					
									
					
					//row11
					row = sheet.getRow(10);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR11_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row12
					row = sheet.getRow(11);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR12_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					//row13
					row = sheet.getRow(12);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR13_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					

					//row14
					row = sheet.getRow(13);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR14_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					

					//row15
					row = sheet.getRow(14);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row16
					row = sheet.getRow(15);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR16_AMOUNT() != null) {
						cell2.setCellValue(record.getR16_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row17
					row = sheet.getRow(16);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR17_AMOUNT() != null) {
						cell2.setCellValue(record.getR17_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row18
					row = sheet.getRow(17);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR18_AMOUNT() != null) {
						cell2.setCellValue(record.getR18_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row19
					row = sheet.getRow(18);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR19_AMOUNT() != null) {
						cell2.setCellValue(record.getR19_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row20
					row = sheet.getRow(19);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR20_AMOUNT() != null) {
						cell2.setCellValue(record.getR20_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
											}

					//row24
					row = sheet.getRow(23);			
					// Column b
					

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR24_AMOUNT() != null) {
						cell2.setCellValue(record.getR24_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row25
					row = sheet.getRow(24);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR25_AMOUNT() != null) {
						cell2.setCellValue(record.getR25_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row26
					row = sheet.getRow(25);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR26_AMOUNT() != null) {
						cell2.setCellValue(record.getR26_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row27
					row = sheet.getRow(26);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR27_AMOUNT() != null) {
						cell2.setCellValue(record.getR27_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row28
					row = sheet.getRow(27);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR28_AMOUNT() != null) {
						cell2.setCellValue(record.getR28_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
											}

					//row29
					row = sheet.getRow(28);			
					// Column b
					
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR29_AMOUNT() != null) {
						cell2.setCellValue(record.getR29_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row36
					row = sheet.getRow(35);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR36_AMOUNT() != null) {
						cell2.setCellValue(record.getR36_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//row37
					row = sheet.getRow(36);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR37_AMOUNT() != null) {
						cell2.setCellValue(record.getR37_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row38
					row = sheet.getRow(37);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR38_AMOUNT() != null) {
						cell2.setCellValue(record.getR38_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row39
					row = sheet.getRow(38);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR39_AMOUNT() != null) {
						cell2.setCellValue(record.getR39_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row40
					row = sheet.getRow(39);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR40_AMOUNT() != null) {
						cell2.setCellValue(record.getR40_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row41
					row = sheet.getRow(40);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR41_AMOUNT() != null) {
						cell2.setCellValue(record.getR41_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row44
					row = sheet.getRow(43);			
					// Column b
					
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR44_AMOUNT() != null) {
						cell2.setCellValue(record.getR44_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row45
					row = sheet.getRow(44);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR45_AMOUNT() != null) {
						cell2.setCellValue(record.getR45_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row46
					row = sheet.getRow(45);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR46_AMOUNT() != null) {
						cell2.setCellValue(record.getR46_AMOUNT().doubleValue());
			
					} else {
						cell2.setCellValue("");

					}

					//row50
					row = sheet.getRow(49);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR50_AMOUNT() != null) {
						cell2.setCellValue(record.getR50_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row51
					row = sheet.getRow(50);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR51_AMOUNT() != null) {
						cell2.setCellValue(record.getR51_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row52
					row = sheet.getRow(51);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR52_AMOUNT() != null) {
						cell2.setCellValue(record.getR52_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row53
					row = sheet.getRow(52);			
					// Column b
				

					// Column c
					 cell2 = row.createCell(2);
					if (record.getR53_AMOUNT() != null) {
						cell2.setCellValue(record.getR53_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row54
					row = sheet.getRow(53);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR54_AMOUNT() != null) {
						cell2.setCellValue(record.getR54_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row55
					row = sheet.getRow(54);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR55_AMOUNT() != null) {
						cell2.setCellValue(record.getR55_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

				

					//row58
					row = sheet.getRow(57);			
					// Column b
				
					// Column c
					 cell2 = row.createCell(2);
					if (record.getR58_AMOUNT() != null) {
						cell2.setCellValue(record.getR58_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row59
					row = sheet.getRow(58);			
					// Column b
				

					// Column c
					 cell2 = row.getCell(2);
					if (record.getR59_AMOUNT() != null) {
						cell2.setCellValue(record.getR59_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

					}

					//row60
					row = sheet.getRow(14);			
					// Column b
				
					// Column c
					 cell2 = row.getCell(2);
					if (record.getR15_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_AMOUNT().doubleValue());

					} else {
						cell2.setCellValue("");

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

	@Transactional
	public void updateLA2Report(M_CA3_Summary_Entity updatedEntity) {

		System.out.println("Came to LA2 services");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		// 1️⃣ Fetch existing SUMMARY
		M_CA3_Summary_Entity existingSummary = brrs_M_CA3_summary_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Summary record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		// 2️⃣ Fetch or create DETAIL
		M_CA3_Detail_Entity existingDetail = brrs_M_CA3_detail_repo.findById(updatedEntity.getREPORT_DATE())
				.orElseGet(() -> {
					M_CA3_Detail_Entity d = new M_CA3_Detail_Entity();
					d.setREPORT_DATE(updatedEntity.getREPORT_DATE());
					return d;
				});

		try {

			// 🔁 Loop R11 → R23 (change if LA2 has different range)
			for (int i = 12; i <= 25; i++) {

				String prefix = "R" + i + "_";

				String[] fields = { "TOTAL" };

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA3_Summary_Entity.class.getMethod(getterName);

						Method summarySetter = M_CA3_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Method detailSetter = M_CA3_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

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
			throw new RuntimeException("Error while updating LA2 report fields", e);
		}

		// 3️⃣ Save BOTH (same transaction)
		brrs_M_CA3_summary_repo.save(existingSummary);
		brrs_M_CA3_detail_repo.save(existingDetail);
	}

}