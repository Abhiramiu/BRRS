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

import com.bornfire.brrs.entities.BRRS_M_TBS_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_TBS_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_TBS_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_TBS_Summary_Repo;
import com.bornfire.brrs.entities.M_TBS_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_TBS_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_TBS_Detail_Entity;
import com.bornfire.brrs.entities.M_TBS_Summary_Entity;

@Component
@Service

public class BRRS_M_TBS_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_TBS_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	BRRS_M_TBS_Detail_Repo brrs_M_TBS_detail_repo;

	@Autowired
	BRRS_M_TBS_Summary_Repo brrs_M_TBS_summary_repo;

	@Autowired
	BRRS_M_TBS_Archival_Detail_Repo M_TBS_Archival_Detail_Repo;

	@Autowired
	BRRS_M_TBS_Archival_Summary_Repo M_TBS_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	/*
	 * public ModelAndView getM_TBSView(String reportId, String fromdate,
	 * String todate, String currency, String dtltype, Pageable pageable, String
	 * type, String version) {
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
	 * List<M_TBS_Archival_Summary_Entity> T1Master = new
	 * ArrayList<M_TBS_Archival_Summary_Entity>(); System.out.println(version);
	 * try { Date d1 = dateformat.parse(todate);
	 * 
	 * 
	 * 
	 * T1Master =
	 * M_TBS_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(
	 * todate ), version);
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * 
	 * mv.addObject("reportsummary", T1Master); } else {
	 * List<M_TBS_Summary_Entity> T1Master = new
	 * ArrayList<M_TBS_Summary_Entity>(); try { Date d1 =
	 * dateformat.parse(todate);
	 * 
	 * 
	 * 
	 * T1Master =
	 * brrs_M_TBS_summary_repo.getdatabydateList(dateformat.parse(todate));
	 * mv.addObject("report_date", dateformat.format(d1));
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * mv.addObject("reportsummary", T1Master); }
	 * 
	 * 
	 * mv.setViewName("BRRS/M_TBS");
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

	public ModelAndView getM_TBSView(String reportId, String fromdate, String todate, String currency,
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

				List<M_TBS_Archival_Summary_Entity> T1Master = M_TBS_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_TBS_Archival_Summary_Entity> T1Master = M_TBS_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<M_TBS_Summary_Entity> T1Master = brrs_M_TBS_summary_repo
						.getdatabydateList(dateformat.parse(todate));
				mv.addObject("displaymode", "summary");
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if (version != null) {

					List<M_TBS_Archival_Detail_Entity> T1Master = M_TBS_Archival_Detail_Repo
							.getdatabydateListarchival(d1, version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_TBS_Detail_Entity> T1Master = brrs_M_TBS_detail_repo
							.getdatabydateList(dateformat.parse(todate));
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_TBS");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	/*
	 * public ModelAndView getM_TBScurrentDtl(String reportId, String fromdate,
	 * String todate, String currency, String dtltype, Pageable pageable, String
	 * type, BigDecimal version) {
	 * 
	 * ModelAndView mv = new ModelAndView();
	 * 
	 * 
	 * System.out.println("testing"); System.out.println(version);
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) { System.out.println(type);
	 * List<M_TBS_Archival_Detail_Entity> T1Master = new
	 * ArrayList<M_TBS_Archival_Detail_Entity>();
	 * 
	 * System.out.println(version); try {
	 * 
	 * 
	 * T1Master =
	 * M_TBS_Archival_Detail_Repo.getdatabydateListarchival(dateformat.parse(
	 * todate) , version); System.out.println("DETAIL");
	 * 
	 * } catch (ParseException e) { e.printStackTrace(); }
	 * 
	 * mv.addObject("reportsummary", T1Master);
	 * 
	 * } else {
	 * 
	 * List<M_TBS_Detail_Entity> T1Master = new
	 * ArrayList<M_TBS_Detail_Entity>();
	 * 
	 * try { Date d1 = dateformat.parse(todate);
	 * 
	 * T1Master =
	 * brrs_M_TBS_detail_repo.getdatabydateList(dateformat.parse(todate));
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
	 * mv.setViewName("BRRS/M_TBS");
	 * 
	 * mv.addObject("displaymode", "summary");
	 * 
	 * System.out.println("scv" + mv.getViewName());
	 * 
	 * return mv; }
	 */

	public byte[] getM_TBSExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_TBSARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		// Email check
		if ("email".equalsIgnoreCase(type) && version == null) {
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_TBSEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		} else if ("email".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_M_TBSARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);

		}

		/* ===================== NORMAL ===================== */
		List<M_TBS_Summary_Entity> dataList = brrs_M_TBS_summary_repo
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_TBS report. Returning empty result.");
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
					M_TBS_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					/*
					 * 	private BigDecimal R11_NV_LONG;
								private BigDecimal R11_NV_SHORT;
								private BigDecimal R11_FV_LONG;
								private BigDecimal R11_FV_SHORT;
								private BigDecimal R11_QFHA;
					 */
					//row11
					// Column C 
					//row=sheet.getRow(11);

					Cell cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
	 * public byte[] getM_TBSDetailExcel(String filename, String fromdate,
	 * String todate, String currency, String dtltype, String type, String version){
	 * try { logger.info("Generating Excel for M_TBS Details...");
	 * System.out.println("came to Detail download service");
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) { byte[] ARCHIVALreport =
	 * getM_TBSDetailExcelARCHIVAL(filename, fromdate, todate, currency,
	 * dtltype, type, version); return ARCHIVALreport; }
	 * 
	 * XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("M_TBSDetails");
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
	 * SimpleDateFormat("dd/MM/yyyy").parse(todate); List<M_TBS_Detail_Entity>
	 * reportData = brrs_M_TBS_detail_repo.getdatabydateList(parsedToDate);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_TBS_Detail_Entity item : reportData) { XSSFRow row =
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
	 * logger.info("No data found for M_TBS — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating M_TBS Excel", e);
	 * return new byte[0]; } }
	 */

	/*
	 * public List<Object> getM_TBSArchival() { List<Object>
	 * M_TBSArchivallist = new ArrayList<>(); try { M_TBSArchivallist =
	 * M_TBS_Archival_Summary_Repo.getM_TBSarchival();
	 * System.out.println("countser" + M_TBSArchivallist.size()); } catch
	 * (Exception e) { // Log the exception
	 * System.err.println("Error fetching M_TBS Archival data: " +
	 * e.getMessage()); e.printStackTrace();
	 * 
	 * // Optionally, you can rethrow it or return empty list // throw new
	 * RuntimeException("Failed to fetch data", e); } return M_TBSArchivallist;
	 * }
	 */

	// Archival View
	public List<Object[]> getM_TBSArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_TBS_Archival_Summary_Entity> repoData = M_TBS_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_TBS_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_TBS_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReportVersion());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_TBS Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public byte[] getExcelM_TBSARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_TBS_Archival_Summary_Entity> dataList = M_TBS_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_TBS report. Returning empty result.");
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
					M_TBS_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
	
	public byte[] BRRS_M_TBSEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		List<M_TBS_Summary_Entity> dataList = brrs_M_TBS_summary_repo
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_TBS_email report. Returning empty result.");
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
					M_TBS_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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

	public byte[] BRRS_M_TBSARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_TBS_Archival_Summary_Entity> dataList = M_TBS_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_TBS_email_ARCHIVAL report. Returning empty result.");
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
					M_TBS_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
	 * public byte[] getM_TBSDetailExcelARCHIVAL(String filename, String
	 * fromdate, String todate, String currency, String dtltype, String type, String
	 * version) { try {
	 * logger.info("Generating Excel for M_TBS ARCHIVAL Details...");
	 * System.out.println("came to Detail download service"); if
	 * (type.equals("ARCHIVAL") & version != null) {
	 * 
	 * } XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("M_TBSDetail");
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
	 * List<M_TBS_Archival_Detail_Entity> reportData =
	 * M_TBS_Archival_Detail_Repo.getdatabydateList(parsedToDate, version);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_TBS_Archival_Detail_Entity item : reportData) { XSSFRow row =
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
	 * logger.info("No data found for M_TBS — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating M_TBSExcel", e);
	 * return new byte[0]; } }
	 */

	

	public List<Object[]> getM_TBSResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_TBS_Archival_Summary_Entity> latestArchivalList = M_TBS_Archival_Summary_Repo
					.getdatabydateListWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_TBS_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_TBS Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	/*
	 * public void updateReportResub(M_TBS_Summary_Entity updatedEntity) {
	 * System.out.println("Came to Resub Service");
	 * System.out.println("Report Date: " + updatedEntity.getReport_date());
	 * 
	 * // Use entity field directly (same name as in entity) Date report_date =
	 * updatedEntity.getReport_date(); int newVersion = 1;
	 * 
	 * try { // ✅ use the same variable name as in repo method
	 * Optional<M_TBS_Archival_Summary_Entity> latestArchivalOpt =
	 * M_TBS_Archival_Summary_Repo.getLatestArchivalVersionByDate(report_date);
	 * 
	 * // Determine next version if (latestArchivalOpt.isPresent()) {
	 * M_TBS_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
	 * try { newVersion = Integer.parseInt(latestArchival.getReport_version()) + 1;
	 * } catch (NumberFormatException e) {
	 * System.err.println("Invalid version format. Defaulting to version 1");
	 * newVersion = 1; } } else {
	 * System.out.println("No previous archival found for date: " + report_date); }
	 * 
	 * // Prevent duplicate version boolean exists =
	 * M_TBS_Archival_Summary_Repo
	 * .findByReport_dateAndReport_version(report_date,
	 * BigDecimal.valueOf(newVersion)) .isPresent();
	 * 
	 * if (exists) { throw new RuntimeException("Version " + newVersion +
	 * " already exists for report date " + report_date); }
	 * 
	 * // Copy summary entity to archival entity M_TBS_Archival_Summary_Entity
	 * archivalEntity = new M_TBS_Archival_Summary_Entity();
	 * org.springframework.beans.BeanUtils.copyProperties(updatedEntity,
	 * archivalEntity);
	 * 
	 * archivalEntity.setReport_date(report_date);
	 * archivalEntity.setReport_version(String.valueOf(newVersion));
	 * archivalEntity.setReportResubDate(new Date());
	 * 
	 * System.out.println("Saving new archival version: " + newVersion);
	 * M_TBS_Archival_Summary_Repo.save(archivalEntity);
	 * 
	 * System.out.println("Saved archival version successfully: " + newVersion);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); throw new
	 * RuntimeException("Error while creating archival resubmission record", e); } }
	 */

	/// Downloaded for Archival & Resub
	public byte[] BRRS_M_TBSResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

		if (type.equals("RESUB") & version != null) {

		}

		List<M_TBS_Archival_Summary_Entity> dataList = M_TBS_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_TBS report. Returning empty result.");
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

					M_TBS_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell1 = row.getCell(2);
					if (record.getR11_NV_LONG() != null) {
						cell1.setCellValue(record.getR11_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR11_NV_SHORT() != null) {
						cell1.setCellValue(record.getR11_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

				    cell1 = row.getCell(4);
					if (record.getR11_FV_LONG() != null) {
						cell1.setCellValue(record.getR11_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR11_FV_SHORT() != null) {
						cell1.setCellValue(record.getR11_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR11_QFHA() != null) {
						cell1.setCellValue(record.getR11_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
//------ROW11---------						
					row=sheet.getRow(11);
				    cell1 = row.getCell(2);
					if (record.getR12_NV_LONG() != null) {
						cell1.setCellValue(record.getR12_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR12_NV_SHORT() != null) {
						cell1.setCellValue(record.getR12_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR12_FV_LONG() != null) {
						cell1.setCellValue(record.getR12_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR12_FV_SHORT() != null) {
						cell1.setCellValue(record.getR12_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR12_QFHA() != null) {
						cell1.setCellValue(record.getR12_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					//------ROW12---------						
					row=sheet.getRow(12);
				    cell1 = row.getCell(2);
					if (record.getR13_NV_LONG() != null) {
						cell1.setCellValue(record.getR13_NV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1=row.getCell(3);
					if (record.getR13_NV_SHORT() != null) {
						cell1.setCellValue(record.getR13_NV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR13_FV_LONG() != null) {
						cell1.setCellValue(record.getR13_FV_LONG().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}		
						
						
					cell1 = row.getCell(5);
					if (record.getR13_FV_SHORT() != null) {
						cell1.setCellValue(record.getR13_FV_SHORT().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	
					
					cell1 = row.getCell(6);
					if (record.getR13_QFHA() != null) {
						cell1.setCellValue(record.getR13_QFHA().doubleValue());
					
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}	

					//------ROW13---------
					row = sheet.getRow(13);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR14_NV_LONG() != null) {
					    cell1.setCellValue(record.getR14_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR14_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR14_FV_LONG() != null) {
					    cell1.setCellValue(record.getR14_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR14_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR14_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR14_QFHA() != null) {
					    cell1.setCellValue(record.getR14_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW14---------
					row = sheet.getRow(14);

					cell1 = row.getCell(2);
					if (record.getR15_NV_LONG() != null) {
					    cell1.setCellValue(record.getR15_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR15_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR15_FV_LONG() != null) {
					    cell1.setCellValue(record.getR15_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR15_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR15_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR15_QFHA() != null) {
					    cell1.setCellValue(record.getR15_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW15---------
					row = sheet.getRow(15);

					cell1 = row.getCell(2);
					if (record.getR16_NV_LONG() != null) {
					    cell1.setCellValue(record.getR16_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR16_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR16_FV_LONG() != null) {
					    cell1.setCellValue(record.getR16_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR16_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR16_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR16_QFHA() != null) {
					    cell1.setCellValue(record.getR16_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW16---------
					row = sheet.getRow(16);

					cell1 = row.getCell(2);
					if (record.getR17_NV_LONG() != null) {
					    cell1.setCellValue(record.getR17_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR17_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR17_FV_LONG() != null) {
					    cell1.setCellValue(record.getR17_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR17_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR17_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR17_QFHA() != null) {
					    cell1.setCellValue(record.getR17_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW17---------
					row = sheet.getRow(17);

					cell1 = row.getCell(2);
					if (record.getR18_NV_LONG() != null) {
					    cell1.setCellValue(record.getR18_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR18_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR18_FV_LONG() != null) {
					    cell1.setCellValue(record.getR18_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR18_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR18_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR18_QFHA() != null) {
					    cell1.setCellValue(record.getR18_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW18---------
					row = sheet.getRow(18);

					cell1 = row.getCell(2);
					if (record.getR19_NV_LONG() != null) {
					    cell1.setCellValue(record.getR19_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR19_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR19_FV_LONG() != null) {
					    cell1.setCellValue(record.getR19_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR19_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR19_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR19_QFHA() != null) {
					    cell1.setCellValue(record.getR19_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW19---------
					row = sheet.getRow(19);

					cell1 = row.getCell(2);
					if (record.getR20_NV_LONG() != null) {
					    cell1.setCellValue(record.getR20_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR20_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR20_FV_LONG() != null) {
					    cell1.setCellValue(record.getR20_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR20_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR20_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR20_QFHA() != null) {
					    cell1.setCellValue(record.getR20_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW20---------
					row = sheet.getRow(20);

					cell1 = row.getCell(2);
					if (record.getR21_NV_LONG() != null) {
					    cell1.setCellValue(record.getR21_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR21_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR21_FV_LONG() != null) {
					    cell1.setCellValue(record.getR21_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR21_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR21_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR21_QFHA() != null) {
					    cell1.setCellValue(record.getR21_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW21---------
					row = sheet.getRow(21);

					cell1 = row.getCell(2);
					if (record.getR22_NV_LONG() != null) {
					    cell1.setCellValue(record.getR22_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR22_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR22_FV_LONG() != null) {
					    cell1.setCellValue(record.getR22_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR22_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR22_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR22_QFHA() != null) {
					    cell1.setCellValue(record.getR22_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW22---------
					row = sheet.getRow(22);

					cell1 = row.getCell(2);
					if (record.getR23_NV_LONG() != null) {
					    cell1.setCellValue(record.getR23_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR23_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR23_FV_LONG() != null) {
					    cell1.setCellValue(record.getR23_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR23_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR23_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR23_QFHA() != null) {
					    cell1.setCellValue(record.getR23_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW23---------
					row = sheet.getRow(23);

					cell1 = row.getCell(2);
					if (record.getR24_NV_LONG() != null) {
					    cell1.setCellValue(record.getR24_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR24_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR24_FV_LONG() != null) {
					    cell1.setCellValue(record.getR24_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR24_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR24_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR24_QFHA() != null) {
					    cell1.setCellValue(record.getR24_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW24---------
					row = sheet.getRow(24);

					cell1 = row.getCell(2);
					if (record.getR25_NV_LONG() != null) {
					    cell1.setCellValue(record.getR25_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR25_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR25_FV_LONG() != null) {
					    cell1.setCellValue(record.getR25_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR25_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR25_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR25_QFHA() != null) {
					    cell1.setCellValue(record.getR25_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW25---------
					row = sheet.getRow(25);

					cell1 = row.getCell(2);
					if (record.getR26_NV_LONG() != null) {
					    cell1.setCellValue(record.getR26_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR26_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR26_FV_LONG() != null) {
					    cell1.setCellValue(record.getR26_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR26_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR26_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR26_QFHA() != null) {
					    cell1.setCellValue(record.getR26_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW26---------
					row = sheet.getRow(26);

					cell1 = row.getCell(2);
					if (record.getR27_NV_LONG() != null) {
					    cell1.setCellValue(record.getR27_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR27_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR27_FV_LONG() != null) {
					    cell1.setCellValue(record.getR27_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR27_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR27_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR27_QFHA() != null) {
					    cell1.setCellValue(record.getR27_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW27---------
					row = sheet.getRow(27);

					cell1 = row.getCell(2);
					if (record.getR28_NV_LONG() != null) {
					    cell1.setCellValue(record.getR28_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR28_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR28_FV_LONG() != null) {
					    cell1.setCellValue(record.getR28_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR28_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR28_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR28_QFHA() != null) {
					    cell1.setCellValue(record.getR28_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW28---------
					row = sheet.getRow(28);

					cell1 = row.getCell(2);
					if (record.getR29_NV_LONG() != null) {
					    cell1.setCellValue(record.getR29_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR29_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR29_FV_LONG() != null) {
					    cell1.setCellValue(record.getR29_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR29_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR29_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR29_QFHA() != null) {
					    cell1.setCellValue(record.getR29_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW29---------
					row = sheet.getRow(29);

					cell1 = row.getCell(2);
					if (record.getR30_NV_LONG() != null) {
					    cell1.setCellValue(record.getR30_NV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR30_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_NV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(4);
					if (record.getR30_FV_LONG() != null) {
					    cell1.setCellValue(record.getR30_FV_LONG().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(5);
					if (record.getR30_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR30_FV_SHORT().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(6);
					if (record.getR30_QFHA() != null) {
					    cell1.setCellValue(record.getR30_QFHA().doubleValue());
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}
					//------ROW30--------- R31
					row = sheet.getRow(30);

					// NV_LONG
					cell1 = row.getCell(2);
					if (record.getR31_NV_LONG() != null) {
					    cell1.setCellValue(record.getR31_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// NV_SHORT
					cell1 = row.getCell(3);
					if (record.getR31_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_LONG
					cell1 = row.getCell(4);
					if (record.getR31_FV_LONG() != null) {
					    cell1.setCellValue(record.getR31_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// FV_SHORT
					cell1 = row.getCell(5);
					if (record.getR31_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR31_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					// QFHA
					cell1 = row.getCell(6);
					if (record.getR31_QFHA() != null) {
					    cell1.setCellValue(record.getR31_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW31--------- R32
					row = sheet.getRow(31);

					cell1 = row.getCell(2);
					if (record.getR32_NV_LONG() != null) {
					    cell1.setCellValue(record.getR32_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR32_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR32_FV_LONG() != null) {
					    cell1.setCellValue(record.getR32_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR32_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR32_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR32_QFHA() != null) {
					    cell1.setCellValue(record.getR32_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW32--------- R33
					row = sheet.getRow(32);

					cell1 = row.getCell(2);
					if (record.getR33_NV_LONG() != null) {
					    cell1.setCellValue(record.getR33_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR33_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR33_FV_LONG() != null) {
					    cell1.setCellValue(record.getR33_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR33_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR33_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR33_QFHA() != null) {
					    cell1.setCellValue(record.getR33_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW33--------- R34
					row = sheet.getRow(33);

					cell1 = row.getCell(2);
					if (record.getR34_NV_LONG() != null) {
					    cell1.setCellValue(record.getR34_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR34_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR34_FV_LONG() != null) {
					    cell1.setCellValue(record.getR34_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR34_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR34_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR34_QFHA() != null) {
					    cell1.setCellValue(record.getR34_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW34--------- R35
					row = sheet.getRow(34);

					cell1 = row.getCell(2);
					if (record.getR35_NV_LONG() != null) {
					    cell1.setCellValue(record.getR35_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR35_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR35_FV_LONG() != null) {
					    cell1.setCellValue(record.getR35_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR35_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR35_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR35_QFHA() != null) {
					    cell1.setCellValue(record.getR35_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW35--------- R36
					row = sheet.getRow(35);

					cell1 = row.getCell(2);
					if (record.getR36_NV_LONG() != null) {
					    cell1.setCellValue(record.getR36_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR36_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR36_FV_LONG() != null) {
					    cell1.setCellValue(record.getR36_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR36_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR36_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR36_QFHA() != null) {
					    cell1.setCellValue(record.getR36_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW36--------- R37
					row = sheet.getRow(36);

					cell1 = row.getCell(2);
					if (record.getR37_NV_LONG() != null) {
					    cell1.setCellValue(record.getR37_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR37_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR37_FV_LONG() != null) {
					    cell1.setCellValue(record.getR37_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR37_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR37_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR37_QFHA() != null) {
					    cell1.setCellValue(record.getR37_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW37--------- R38
					row = sheet.getRow(37);

					cell1 = row.getCell(2);
					if (record.getR38_NV_LONG() != null) {
					    cell1.setCellValue(record.getR38_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR38_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR38_FV_LONG() != null) {
					    cell1.setCellValue(record.getR38_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR38_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR38_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR38_QFHA() != null) {
					    cell1.setCellValue(record.getR38_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW38--------- R39
					row = sheet.getRow(38);

					cell1 = row.getCell(2);
					if (record.getR39_NV_LONG() != null) {
					    cell1.setCellValue(record.getR39_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR39_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR39_FV_LONG() != null) {
					    cell1.setCellValue(record.getR39_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR39_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR39_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR39_QFHA() != null) {
					    cell1.setCellValue(record.getR39_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW39--------- R40
					row = sheet.getRow(39);

					cell1 = row.getCell(2);
					if (record.getR40_NV_LONG() != null) {
					    cell1.setCellValue(record.getR40_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR40_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR40_FV_LONG() != null) {
					    cell1.setCellValue(record.getR40_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR40_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR40_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR40_QFHA() != null) {
					    cell1.setCellValue(record.getR40_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					//------ROW40--------- R41
					row = sheet.getRow(40);

					cell1 = row.getCell(2);
					if (record.getR41_NV_LONG() != null) {
					    cell1.setCellValue(record.getR41_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR41_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR41_FV_LONG() != null) {
					    cell1.setCellValue(record.getR41_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR41_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR41_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR41_QFHA() != null) {
					    cell1.setCellValue(record.getR41_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW41--------- R42
					row = sheet.getRow(41);

					cell1 = row.getCell(2);
					if (record.getR42_NV_LONG() != null) {
					    cell1.setCellValue(record.getR42_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR42_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR42_FV_LONG() != null) {
					    cell1.setCellValue(record.getR42_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR42_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR42_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR42_QFHA() != null) {
					    cell1.setCellValue(record.getR42_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW42--------- R43
					row = sheet.getRow(42);

					cell1 = row.getCell(2);
					if (record.getR43_NV_LONG() != null) {
					    cell1.setCellValue(record.getR43_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR43_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR43_FV_LONG() != null) {
					    cell1.setCellValue(record.getR43_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR43_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR43_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR43_QFHA() != null) {
					    cell1.setCellValue(record.getR43_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW43--------- R44
					row = sheet.getRow(43);

					cell1 = row.getCell(2);
					if (record.getR44_NV_LONG() != null) {
					    cell1.setCellValue(record.getR44_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR44_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR44_FV_LONG() != null) {
					    cell1.setCellValue(record.getR44_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR44_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR44_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR44_QFHA() != null) {
					    cell1.setCellValue(record.getR44_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW44--------- R45
					row = sheet.getRow(44);

					cell1 = row.getCell(2);
					if (record.getR45_NV_LONG() != null) {
					    cell1.setCellValue(record.getR45_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR45_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR45_FV_LONG() != null) {
					    cell1.setCellValue(record.getR45_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR45_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR45_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR45_QFHA() != null) {
					    cell1.setCellValue(record.getR45_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW45--------- R46
					row = sheet.getRow(45);

					cell1 = row.getCell(2);
					if (record.getR46_NV_LONG() != null) {
					    cell1.setCellValue(record.getR46_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR46_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR46_FV_LONG() != null) {
					    cell1.setCellValue(record.getR46_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR46_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR46_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR46_QFHA() != null) {
					    cell1.setCellValue(record.getR46_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW46--------- R47
					row = sheet.getRow(46);

					cell1 = row.getCell(2);
					if (record.getR47_NV_LONG() != null) {
					    cell1.setCellValue(record.getR47_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR47_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR47_FV_LONG() != null) {
					    cell1.setCellValue(record.getR47_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR47_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR47_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR47_QFHA() != null) {
					    cell1.setCellValue(record.getR47_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW47--------- R48
					row = sheet.getRow(47);

					cell1 = row.getCell(2);
					if (record.getR48_NV_LONG() != null) {
					    cell1.setCellValue(record.getR48_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR48_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR48_FV_LONG() != null) {
					    cell1.setCellValue(record.getR48_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR48_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR48_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR48_QFHA() != null) {
					    cell1.setCellValue(record.getR48_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW48--------- R49
					row = sheet.getRow(48);

					cell1 = row.getCell(2);
					if (record.getR49_NV_LONG() != null) {
					    cell1.setCellValue(record.getR49_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR49_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR49_FV_LONG() != null) {
					    cell1.setCellValue(record.getR49_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR49_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR49_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR49_QFHA() != null) {
					    cell1.setCellValue(record.getR49_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }



					//------ROW49--------- R50
					row = sheet.getRow(49);

					cell1 = row.getCell(2);
					if (record.getR50_NV_LONG() != null) {
					    cell1.setCellValue(record.getR50_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR50_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR50_FV_LONG() != null) {
					    cell1.setCellValue(record.getR50_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR50_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR50_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR50_QFHA() != null) {
					    cell1.setCellValue(record.getR50_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					//------ROW50--------- R51
					row = sheet.getRow(50);

					cell1 = row.getCell(2);
					if (record.getR51_NV_LONG() != null) {
					    cell1.setCellValue(record.getR51_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR51_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR51_FV_LONG() != null) {
					    cell1.setCellValue(record.getR51_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR51_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR51_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR51_QFHA() != null) {
					    cell1.setCellValue(record.getR51_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }
					
					
					//------ROW51--------- R52
					row = sheet.getRow(51);

					cell1 = row.getCell(2);
					if (record.getR52_NV_LONG() != null) {
					    cell1.setCellValue(record.getR52_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR52_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR52_FV_LONG() != null) {
					    cell1.setCellValue(record.getR52_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR52_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR52_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR52_QFHA() != null) {
					    cell1.setCellValue(record.getR52_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW52--------- R53
					row = sheet.getRow(52);

					cell1 = row.getCell(2);
					if (record.getR53_NV_LONG() != null) {
					    cell1.setCellValue(record.getR53_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR53_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR53_FV_LONG() != null) {
					    cell1.setCellValue(record.getR53_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR53_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR53_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR53_QFHA() != null) {
					    cell1.setCellValue(record.getR53_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW53--------- R54
					row = sheet.getRow(53);

					cell1 = row.getCell(2);
					if (record.getR54_NV_LONG() != null) {
					    cell1.setCellValue(record.getR54_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR54_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR54_FV_LONG() != null) {
					    cell1.setCellValue(record.getR54_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR54_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR54_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR54_QFHA() != null) {
					    cell1.setCellValue(record.getR54_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					
					//------ROW54--------- R55
					row = sheet.getRow(54);

					cell1 = row.getCell(2);
					if (record.getR55_NV_LONG() != null) {
					    cell1.setCellValue(record.getR55_NV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(3);
					if (record.getR55_NV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_NV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(4);
					if (record.getR55_FV_LONG() != null) {
					    cell1.setCellValue(record.getR55_FV_LONG().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(5);
					if (record.getR55_FV_SHORT() != null) {
					    cell1.setCellValue(record.getR55_FV_SHORT().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

					cell1 = row.getCell(6);
					if (record.getR55_QFHA() != null) {
					    cell1.setCellValue(record.getR55_QFHA().doubleValue());
					} else { cell1.setCellValue(""); cell1.setCellStyle(textStyle); }

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
	public void updateReport(M_TBS_Summary_Entity updatedEntity) {

	    System.out.println("Came to TBS Summary services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	    M_TBS_Summary_Entity existingSummary = brrs_M_TBS_summary_repo
	            .findById(updatedEntity.getReportDate())
	            .orElseThrow(() -> new RuntimeException(
	                    "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    // 2️⃣ Fetch or create DETAIL (LA2 style)
	    M_TBS_Detail_Entity existingDetail = brrs_M_TBS_detail_repo
	            .findById(updatedEntity.getReportDate())
	            .orElseGet(() -> {
	                M_TBS_Detail_Entity d = new M_TBS_Detail_Entity();
	                d.setReportDate(updatedEntity.getReportDate());
	                return d;
	            });

	    try {

	        String[] fields = { "NV_LONG", "NV_SHORT", "FV_LONG", "FV_SHORT", "QFHA" };

	        // ---------- Helper: copy rows into BOTH ----------
	        java.util.function.Consumer<int[]> copyRows = (rows) -> {
	            for (int row : rows) {
	                String prefix = "R" + row + "_";
	                for (String field : fields) {
	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;
	                    try {
	                        Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);

	                        Method summarySetter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                        Method detailSetter  = M_TBS_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

	                        Object newValue = getter.invoke(updatedEntity);

	                        // set into SUMMARY
	                        summarySetter.invoke(existingSummary, newValue);

	                        // set into DETAIL
	                        detailSetter.invoke(existingDetail, newValue);

	                    } catch (NoSuchMethodException e) {
	                        // skip missing safely
	                    } catch (Exception e) {
	                        throw new RuntimeException(e);
	                    }
	                }
	            }
	        };

	        // ---------- Helper: copy total row into BOTH ----------
	        java.util.function.Consumer<Integer> copyTotal = (row) -> {
	            String prefix = "R" + row + "_";
	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;
	                try {
	                    Method getter = M_TBS_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter = M_TBS_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
	                    Method detailSetter  = M_TBS_Detail_Entity.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);

	                    // set into SUMMARY
	                    summarySetter.invoke(existingSummary, newValue);

	                    // set into DETAIL
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    // skip
	                } catch (Exception e) {
	                    throw new RuntimeException(e);
	                }
	            }
	        };

	        // 3️⃣ R11 = SUM(C12:C16) + C21
	        copyRows.accept(new int[] { 12, 13, 14, 15, 16, 21 });
	        copyTotal.accept(11);

	        // 4️⃣ R16 = SUM(C17:C20)
	        copyRows.accept(new int[] { 17, 18, 19, 20 });
	        copyTotal.accept(16);

	        // 5️⃣ R22 = SUM(C23:C27) + C33
	        copyRows.accept(new int[] { 23, 24, 25, 26, 27, 33 });
	        copyTotal.accept(22);

	        // 6️⃣ R27 = SUM(C28:C32)
	        copyRows.accept(new int[] { 28, 29, 30, 31, 32 });
	        copyTotal.accept(27);

	        // 7️⃣ R34 = C35 + C36 + C40
	        copyRows.accept(new int[] { 35, 36, 40 });
	        copyTotal.accept(34);

	        // 8️⃣ R36 = SUM(C37:C39)
	        copyRows.accept(new int[] { 37, 38, 39 });
	        copyTotal.accept(36);

	        // 9️⃣ R41 = C42 + C43 + C44 + C49
	        copyRows.accept(new int[] { 42, 43, 44, 49 });
	        copyTotal.accept(41);

	        // 🔟 R44 = SUM(C45:C48)
	        copyRows.accept(new int[] { 45, 46, 47, 48 });
	        copyTotal.accept(44);

	        // 1️⃣1️⃣ R50 = SUM(C51:C54)
	        copyRows.accept(new int[] { 51, 52, 53, 54 });
	        copyTotal.accept(50);

	        // 1️⃣2️⃣ R55 = C50 + C41 + C34 + C22 + C11
	        copyRows.accept(new int[] { 50, 41, 34, 22, 11 });
	        copyTotal.accept(55);

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating M_TBS Summary & Detail report fields", e);
	    }

	    // 4️⃣ Save BOTH in same transaction
	    brrs_M_TBS_summary_repo.save(existingSummary);
	    brrs_M_TBS_detail_repo.save(existingDetail);
	}


}