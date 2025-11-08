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
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_BOP_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_BOP_Summary_Repo;
import com.bornfire.brrs.entities.M_BOP_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_BOP_Summary_Entity;
import com.bornfire.brrs.entities.Q_RLFA2_Summary_Entity;




@Component
@Service
public class BRRS_M_BOP_ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_BOP_ReportService.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	SessionFactory sessionFactory;
	

	
	@Autowired
	BRRS_M_BOP_Summary_Repo M_BOP_Summary_Repo; 
	
	
	
	@Autowired
	BRRS_M_BOP_Archival_Summary_Repo M_BOP_Archival_Summary_Repo; 

	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_BOPview(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;	
		
		System.out.println("testing");
		System.out.println(version);

		
		if (type.equals("ARCHIVAL") & version != null) {
			System.out.println(type);
			List<M_BOP_Archival_Summary_Entity> T1Master = new ArrayList<M_BOP_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_BOP_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
			List<M_BOP_Summary_Entity> T1Master = new ArrayList<M_BOP_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_BOP_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_BOP");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}
	
	/*
	 * public ModelAndView getM_BOPcurrentDtl( String reportId, String fromdate,
	 * String todate, String currency, String dtltype, Pageable pageable, String
	 * Filter, String type, String version) {
	 * 
	 * int pageSize = pageable != null ? pageable.getPageSize() : 10; int
	 * currentPage = pageable != null ? pageable.getPageNumber() : 0; int totalPages
	 * = 0;
	 * 
	 * ModelAndView mv = new ModelAndView(); Session hs =
	 * sessionFactory.getCurrentSession();
	 * 
	 * try { Date parsedDate = null; if (todate != null && !todate.isEmpty()) {
	 * parsedDate = dateformat.parse(todate); }
	 * 
	 * String rowId = null; String columnId = null;
	 * 
	 * // ✅ Split filter string into rowId & columnId if (Filter != null &&
	 * Filter.contains(",")) { String[] parts = Filter.split(","); if (parts.length
	 * >= 2) { rowId = parts[0]; columnId = parts[1]; } } System.out.println(type);
	 * if ("ARCHIVAL".equals(type) && version != null) { System.out.println(type);
	 * // 🔹 Archival branch List<M_BOP_Archival_Detail_Entity> T1Dt1; if (rowId !=
	 * null && columnId != null) { T1Dt1 =
	 * M_BOP_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId,
	 * parsedDate, version); } else { T1Dt1 =
	 * M_BOP_Archival_Detail_Repo.getdatabydateList(parsedDate, version); }
	 * 
	 * mv.addObject("reportdetails", T1Dt1); mv.addObject("reportmaster12", T1Dt1);
	 * System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
	 * 
	 * } else { // 🔹 Current branch List<M_BOP_Detail_Entity> T1Dt1; if (rowId !=
	 * null && columnId != null) { T1Dt1 =
	 * M_BOP_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate); }
	 * else { T1Dt1 = M_BOP_Detail_Repo.getdatabydateList(parsedDate, currentPage,
	 * pageSize); totalPages = M_BOP_Detail_Repo.getdatacount(parsedDate);
	 * mv.addObject("pagination", "YES"); }
	 * 
	 * mv.addObject("reportdetails", T1Dt1); mv.addObject("reportmaster12", T1Dt1);
	 * System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0)); }
	 * 
	 * } catch (ParseException e) { e.printStackTrace();
	 * mv.addObject("errorMessage", "Invalid date format: " + todate); } catch
	 * (Exception e) { e.printStackTrace(); mv.addObject("errorMessage",
	 * "Unexpected error: " + e.getMessage()); }
	 * 
	 * // ✅ Common attributes mv.setViewName("BRRS/M_BOP");
	 * mv.addObject("displaymode", "Details"); mv.addObject("currentPage",
	 * currentPage); System.out.println("totalPages: " + (int) Math.ceil((double)
	 * totalPages / 100)); mv.addObject("totalPages", (int) Math.ceil((double)
	 * totalPages / 100)); mv.addObject("reportsflag", "reportsflag");
	 * mv.addObject("menu", reportId);
	 * 
	 * return mv; }
	 */
	
	public byte[] getBRRS_M_BOPExcel(String filename,String reportId, String fromdate, String todate, String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory."); 
		
		
		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_BOPARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		} 

		List<M_BOP_Summary_Entity> dataList =M_BOP_Summary_Repo.getdatabydateList(dateformat.parse(todate)) ;

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRSM_BOP report. Returning empty result.");
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
			
			
			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_BOP_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row	row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
				
					//row13
					// Column B
					Cell cell2 = row.createCell(1);
					if (record.getR13_open_position() != null) {
						cell2.setCellValue(record.getR13_open_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//row13
					// Column C
					Cell cell3 = row.createCell(2);
					if (record.getR13_cpdm_dt_inc() != null) {
						cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//row13
					// Column D
					Cell cell4 = row.createCell(3);
					if (record.getR13_cpdm_dt_dec() != null) {
						cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row13
					// Column B
					Cell cell6 = row.createCell(5);
					if (record.getR13_cpdm_dt_der() != null) {
						cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					Cell cell7 = row.createCell(6);
					if (record.getR13_cpdm_dt_dto() != null) {
						cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					
					//ROW 14
					row = sheet.getRow(13);

					cell2 = row.createCell(1);
					if (record.getR14_open_position() != null) {
						cell2.setCellValue(record.getR14_open_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row14
					// Column C
					cell3 = row.createCell(2);
					if (record.getR14_cpdm_dt_inc() != null) {
						cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//row14
					// Column D
					cell4 = row.createCell(3);
					if (record.getR14_cpdm_dt_dec() != null) {
						cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row14
					// Column f
					cell6 = row.createCell(5);
					if (record.getR14_cpdm_dt_der() != null) {
						cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					//row14
					//column g
					
					cell7 = row.createCell(6);
					if (record.getR14_cpdm_dt_dto() != null) {
						cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
	                

					
					

					//ROW 16
					row = sheet.getRow(15);

					
					cell2 = row.createCell(1);
					if (record.getR16_open_position() != null) {
						cell2.setCellValue(record.getR16_open_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row16
					// Column C
					cell3 = row.createCell(2);
					if (record.getR16_cpdm_dt_inc() != null) {
						cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//row16
					// Column D
					cell4 = row.createCell(3);
					if (record.getR16_cpdm_dt_dec() != null) {
						cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row16
					// Column f
					cell6 = row.createCell(5);
					if (record.getR16_cpdm_dt_der() != null) {
						cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					//row16
					//column g
					
					cell7 = row.createCell(6);
					if (record.getR16_cpdm_dt_dto() != null) {
						cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
	                

					

					//ROW 17
	row = sheet.getRow(16);

	cell2 = row.createCell(1);
	if (record.getR17_open_position() != null) {
	    cell2.setCellValue(record.getR17_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	// Column C
	cell3 = row.createCell(2);
	if (record.getR17_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	// Column D
	cell4 = row.createCell(3);
	if (record.getR17_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	// Column F
	cell6 = row.createCell(5);
	if (record.getR17_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	// Column G
	cell7 = row.createCell(6);
	if (record.getR17_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 18
	row = sheet.getRow(17);

	cell2 = row.createCell(1);
	if (record.getR18_open_position() != null) {
	    cell2.setCellValue(record.getR18_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR18_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR18_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR18_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR18_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 19
	row = sheet.getRow(18);

	cell2 = row.createCell(1);
	if (record.getR19_open_position() != null) {
	    cell2.setCellValue(record.getR19_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR19_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR19_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR19_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR19_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 20
	row = sheet.getRow(19);

	cell2 = row.createCell(1);
	if (record.getR20_open_position() != null) {
	    cell2.setCellValue(record.getR20_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR20_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR20_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR20_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR20_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	
	//ROW 21
	row = sheet.getRow(20);

	cell2 = row.createCell(1);
	if (record.getR21_open_position() != null) {
	    cell2.setCellValue(record.getR21_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR21_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR21_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR21_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR21_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	
	//ROW 22
	row = sheet.getRow(21);

	cell2 = row.createCell(1);
	if (record.getR22_open_position() != null) {
	    cell2.setCellValue(record.getR22_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR22_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR22_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR22_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR22_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 23
	row = sheet.getRow(22);

	cell2 = row.createCell(1);
	if (record.getR23_open_position() != null) {
	    cell2.setCellValue(record.getR23_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR23_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR23_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR23_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR23_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	

	//ROW 25
	row = sheet.getRow(24);

	cell2 = row.createCell(1);
	if (record.getR25_open_position() != null) {
	    cell2.setCellValue(record.getR25_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR25_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR25_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	Cell cell5 = row.createCell(4);
	if (record.getR25_net() != null) {
	    cell5.setCellValue(record.getR25_net().doubleValue());
	    cell5.setCellStyle(numberStyle);
	} else {
	    cell5.setCellValue("");
	    cell5.setCellStyle(textStyle);
	}

	cell6 = row.createCell(5);
	if (record.getR25_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR25_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	Cell cell8 = row.createCell(7);
	if (record.getR25_cp() != null) {
	    cell8.setCellValue(record.getR25_cp().doubleValue());
	    cell8.setCellStyle(numberStyle);
	} else {
	    cell8.setCellValue("");
	    cell8.setCellStyle(textStyle);
	}

	//ROW 26
	row = sheet.getRow(25);

	cell2 = row.createCell(1);
	if (record.getR26_open_position() != null) {
	    cell2.setCellValue(record.getR26_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR26_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR26_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR26_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR26_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	

	//ROW 28
	row = sheet.getRow(27);

	cell2 = row.createCell(1);
	if (record.getR28_open_position() != null) {
	    cell2.setCellValue(record.getR28_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR28_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR28_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR28_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR28_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 29
	row = sheet.getRow(28);

	cell2 = row.createCell(1);
	if (record.getR29_open_position() != null) {
	    cell2.setCellValue(record.getR29_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR29_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR29_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	
	cell6 = row.createCell(5);
	if (record.getR29_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR29_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 30
	row = sheet.getRow(29);

	cell2 = row.createCell(1);
	if (record.getR30_open_position() != null) {
	    cell2.setCellValue(record.getR30_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR30_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR30_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	
	cell6 = row.createCell(5);
	if (record.getR30_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR30_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 31
	row = sheet.getRow(30);

	cell2 = row.createCell(1);
	if (record.getR31_open_position() != null) {
	    cell2.setCellValue(record.getR31_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR31_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR31_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR31_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR31_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	
	//ROW 32
	row = sheet.getRow(31);

	cell2 = row.createCell(1);
	if (record.getR32_open_position() != null) {
	    cell2.setCellValue(record.getR32_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR32_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR32_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR32_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR32_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	
	//ROW 33
	row = sheet.getRow(32);

	cell2 = row.createCell(1);
	if (record.getR33_open_position() != null) {
	    cell2.setCellValue(record.getR33_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR33_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR33_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR33_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR33_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 34
	row = sheet.getRow(33);

	cell2 = row.createCell(1);
	if (record.getR34_open_position() != null) {
	    cell2.setCellValue(record.getR34_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR34_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR34_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR34_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR34_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 35
	row = sheet.getRow(34);

	cell2 = row.createCell(1);
	if (record.getR35_open_position() != null) {
	    cell2.setCellValue(record.getR35_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR35_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR35_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR35_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR35_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	
}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {
				
			}
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}
	
	
	/*
	 * public byte[] getBRRSM_BOPDetailExcel(String filename, String fromdate,
	 * String todate, String currency, String dtltype, String type, String version)
	 * { try { logger.info("Generating Excel for BRRS_M_BOP Details...");
	 * System.out.println("This is also checking purpose...");
	 * System.out.println("came to Detail download service");
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) { byte[] ARCHIVALreport =
	 * getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
	 * version); return ARCHIVALreport; }
	 * 
	 * XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("BRRSM_BOPDetails");
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
	 * balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
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
	 * SimpleDateFormat("dd/MM/yyyy").parse(todate); List<M_BOP_Detail_Entity>
	 * reportData = M_BOP_Detail_Repo.getdatabydateList(parsedToDate);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_BOP_Detail_Entity item : reportData) { XSSFRow row =
	 * sheet.createRow(rowIndex++);
	 * 
	 * row.createCell(0).setCellValue(item.getCust_id());
	 * row.createCell(1).setCellValue(item.getAcct_number());
	 * row.createCell(2).setCellValue(item.getAcct_name());
	 * 
	 * // ACCT BALANCE (right aligned, 3 decimal places) Cell balanceCell =
	 * row.createCell(3); if (item.getAcct_balance_in_pula() != null) {
	 * balanceCell.setCellValue(item.getAcct_balance_in_pula() .doubleValue()); }
	 * else { balanceCell.setCellValue(0.00); }
	 * 
	 * balanceCell.setCellStyle(balanceStyle);
	 * 
	 * row.createCell(4).setCellValue(item.getRow_id());
	 * row.createCell(5).setCellValue(item.getColumn_id());
	 * row.createCell(6).setCellValue( item.getReport_date() != null ? new
	 * SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date()) : "" ); // Apply
	 * data style for all other cells for (int j = 0; j < 7; j++) { if (j != 3) {
	 * row.getCell(j).setCellStyle(dataStyle); } }
	 * 
	 * } } else {
	 * logger.info("No data found for BRRS_M_BOP — only header will be written."); }
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating BRRSM_OR2 Excel", e);
	 * return new byte[0]; } }
	 */
	
	public List<Object> getM_BOPArchival() {
		List<Object> M_BOPArchivallist = new ArrayList<>();
		try {
			M_BOPArchivallist = M_BOP_Archival_Summary_Repo.getM_BOParchival();
			System.out.println("countser" + M_BOPArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_OR2 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_BOPArchivallist;
	}
	
	public byte[] getExcelM_BOPARCHIVAL(String filename, String reportId, String fromdate, String todate,
			   String currency, String dtltype, String type, String version) throws Exception {
		
		logger.info("Service: Starting Excel generation process in memory.");
		
		if (type.equals("ARCHIVAL") & version != null) {

		}
		
		List<M_BOP_Archival_Summary_Entity> dataList = M_BOP_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_BOP report. Returning empty result.");
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
			
			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_BOP_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row	row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
				
					//row13
					// Column B
					Cell cell2 = row.createCell(1);
					if (record.getR13_open_position() != null) {
						cell2.setCellValue(record.getR13_open_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					//row13
					// Column C
					Cell cell3 = row.createCell(2);
					if (record.getR13_cpdm_dt_inc() != null) {
						cell3.setCellValue(record.getR13_cpdm_dt_inc().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//row13
					// Column D
					Cell cell4 = row.createCell(3);
					if (record.getR13_cpdm_dt_dec() != null) {
						cell4.setCellValue(record.getR13_cpdm_dt_dec().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row13
					// Column B
					Cell cell6 = row.createCell(5);
					if (record.getR13_cpdm_dt_der() != null) {
						cell6.setCellValue(record.getR13_cpdm_dt_der().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					Cell cell7 = row.createCell(6);
					if (record.getR13_cpdm_dt_dto() != null) {
						cell7.setCellValue(record.getR13_cpdm_dt_dto().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					
					//ROW 14
					row = sheet.getRow(13);

					cell2 = row.createCell(1);
					if (record.getR14_open_position() != null) {
						cell2.setCellValue(record.getR14_open_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row14
					// Column C
					cell3 = row.createCell(2);
					if (record.getR14_cpdm_dt_inc() != null) {
						cell3.setCellValue(record.getR14_cpdm_dt_inc().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//row14
					// Column D
					cell4 = row.createCell(3);
					if (record.getR14_cpdm_dt_dec() != null) {
						cell4.setCellValue(record.getR14_cpdm_dt_dec().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row14
					// Column f
					cell6 = row.createCell(5);
					if (record.getR14_cpdm_dt_der() != null) {
						cell6.setCellValue(record.getR14_cpdm_dt_der().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					//row14
					//column g
					
					cell7 = row.createCell(6);
					if (record.getR14_cpdm_dt_dto() != null) {
						cell7.setCellValue(record.getR14_cpdm_dt_dto().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
	                

					
					

					//ROW 16
					row = sheet.getRow(15);

					
					cell2 = row.createCell(1);
					if (record.getR16_open_position() != null) {
						cell2.setCellValue(record.getR16_open_position().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					//row16
					// Column C
					cell3 = row.createCell(2);
					if (record.getR16_cpdm_dt_inc() != null) {
						cell3.setCellValue(record.getR16_cpdm_dt_inc().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					//row16
					// Column D
					cell4 = row.createCell(3);
					if (record.getR16_cpdm_dt_dec() != null) {
						cell4.setCellValue(record.getR16_cpdm_dt_dec().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					//row16
					// Column f
					cell6 = row.createCell(5);
					if (record.getR16_cpdm_dt_der() != null) {
						cell6.setCellValue(record.getR16_cpdm_dt_der().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					//row16
					//column g
					
					cell7 = row.createCell(6);
					if (record.getR16_cpdm_dt_dto() != null) {
						cell7.setCellValue(record.getR16_cpdm_dt_dto().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
	                

					

					//ROW 17
	row = sheet.getRow(16);

	cell2 = row.createCell(1);
	if (record.getR17_open_position() != null) {
	    cell2.setCellValue(record.getR17_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	// Column C
	cell3 = row.createCell(2);
	if (record.getR17_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR17_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	// Column D
	cell4 = row.createCell(3);
	if (record.getR17_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR17_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	// Column F
	cell6 = row.createCell(5);
	if (record.getR17_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR17_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	// Column G
	cell7 = row.createCell(6);
	if (record.getR17_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR17_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 18
	row = sheet.getRow(17);

	cell2 = row.createCell(1);
	if (record.getR18_open_position() != null) {
	    cell2.setCellValue(record.getR18_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR18_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR18_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR18_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR18_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR18_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR18_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR18_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR18_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 19
	row = sheet.getRow(18);

	cell2 = row.createCell(1);
	if (record.getR19_open_position() != null) {
	    cell2.setCellValue(record.getR19_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR19_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR19_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR19_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR19_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR19_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR19_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR19_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR19_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 20
	row = sheet.getRow(19);

	cell2 = row.createCell(1);
	if (record.getR20_open_position() != null) {
	    cell2.setCellValue(record.getR20_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR20_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR20_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR20_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR20_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR20_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR20_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR20_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR20_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	
	//ROW 21
	row = sheet.getRow(20);

	cell2 = row.createCell(1);
	if (record.getR21_open_position() != null) {
	    cell2.setCellValue(record.getR21_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR21_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR21_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR21_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR21_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR21_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR21_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR21_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR21_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	
	//ROW 22
	row = sheet.getRow(21);

	cell2 = row.createCell(1);
	if (record.getR22_open_position() != null) {
	    cell2.setCellValue(record.getR22_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR22_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR22_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR22_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR22_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR22_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR22_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR22_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR22_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 23
	row = sheet.getRow(22);

	cell2 = row.createCell(1);
	if (record.getR23_open_position() != null) {
	    cell2.setCellValue(record.getR23_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR23_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR23_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR23_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR23_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR23_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR23_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR23_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR23_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	

	//ROW 25
	row = sheet.getRow(24);

	cell2 = row.createCell(1);
	if (record.getR25_open_position() != null) {
	    cell2.setCellValue(record.getR25_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR25_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR25_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR25_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR25_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	Cell cell5 = row.createCell(4);
	if (record.getR25_net() != null) {
	    cell5.setCellValue(record.getR25_net().doubleValue());
	    cell5.setCellStyle(numberStyle);
	} else {
	    cell5.setCellValue("");
	    cell5.setCellStyle(textStyle);
	}

	cell6 = row.createCell(5);
	if (record.getR25_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR25_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR25_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR25_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	Cell cell8 = row.createCell(7);
	if (record.getR25_cp() != null) {
	    cell8.setCellValue(record.getR25_cp().doubleValue());
	    cell8.setCellStyle(numberStyle);
	} else {
	    cell8.setCellValue("");
	    cell8.setCellStyle(textStyle);
	}

	//ROW 26
	row = sheet.getRow(25);

	cell2 = row.createCell(1);
	if (record.getR26_open_position() != null) {
	    cell2.setCellValue(record.getR26_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR26_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR26_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR26_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR26_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR26_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR26_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR26_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR26_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	

	//ROW 28
	row = sheet.getRow(27);

	cell2 = row.createCell(1);
	if (record.getR28_open_position() != null) {
	    cell2.setCellValue(record.getR28_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR28_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR28_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR28_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR28_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR28_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR28_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR28_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR28_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 29
	row = sheet.getRow(28);

	cell2 = row.createCell(1);
	if (record.getR29_open_position() != null) {
	    cell2.setCellValue(record.getR29_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR29_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR29_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR29_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR29_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	
	cell6 = row.createCell(5);
	if (record.getR29_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR29_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR29_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR29_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 30
	row = sheet.getRow(29);

	cell2 = row.createCell(1);
	if (record.getR30_open_position() != null) {
	    cell2.setCellValue(record.getR30_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR30_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR30_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR30_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR30_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	
	cell6 = row.createCell(5);
	if (record.getR30_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR30_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR30_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR30_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 31
	row = sheet.getRow(30);

	cell2 = row.createCell(1);
	if (record.getR31_open_position() != null) {
	    cell2.setCellValue(record.getR31_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR31_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR31_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR31_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR31_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR31_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR31_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR31_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR31_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	
	//ROW 32
	row = sheet.getRow(31);

	cell2 = row.createCell(1);
	if (record.getR32_open_position() != null) {
	    cell2.setCellValue(record.getR32_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR32_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR32_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR32_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR32_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR32_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR32_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR32_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR32_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	
	//ROW 33
	row = sheet.getRow(32);

	cell2 = row.createCell(1);
	if (record.getR33_open_position() != null) {
	    cell2.setCellValue(record.getR33_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR33_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR33_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR33_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR33_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR33_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR33_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR33_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR33_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 34
	row = sheet.getRow(33);

	cell2 = row.createCell(1);
	if (record.getR34_open_position() != null) {
	    cell2.setCellValue(record.getR34_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR34_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR34_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR34_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR34_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR34_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR34_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR34_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR34_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

	

	//ROW 35
	row = sheet.getRow(34);

	cell2 = row.createCell(1);
	if (record.getR35_open_position() != null) {
	    cell2.setCellValue(record.getR35_open_position().doubleValue());
	    cell2.setCellStyle(numberStyle);
	} else {
	    cell2.setCellValue("");
	    cell2.setCellStyle(textStyle);
	}

	cell3 = row.createCell(2);
	if (record.getR35_cpdm_dt_inc() != null) {
	    cell3.setCellValue(record.getR35_cpdm_dt_inc().doubleValue());
	    cell3.setCellStyle(numberStyle);
	} else {
	    cell3.setCellValue("");
	    cell3.setCellStyle(textStyle);
	}

	cell4 = row.createCell(3);
	if (record.getR35_cpdm_dt_dec() != null) {
	    cell4.setCellValue(record.getR35_cpdm_dt_dec().doubleValue());
	    cell4.setCellStyle(numberStyle);
	} else {
	    cell4.setCellValue("");
	    cell4.setCellStyle(textStyle);
	}

	

	cell6 = row.createCell(5);
	if (record.getR35_cpdm_dt_der() != null) {
	    cell6.setCellValue(record.getR35_cpdm_dt_der().doubleValue());
	    cell6.setCellStyle(numberStyle);
	} else {
	    cell6.setCellValue("");
	    cell6.setCellStyle(textStyle);
	}

	cell7 = row.createCell(6);
	if (record.getR35_cpdm_dt_dto() != null) {
	    cell7.setCellValue(record.getR35_cpdm_dt_dto().doubleValue());
	    cell7.setCellStyle(numberStyle);
	} else {
	    cell7.setCellValue("");
	    cell7.setCellStyle(textStyle);
	}

}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {
				
			}
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
				
			
			
		
		
		
		
		
			
		
	}
	
	/*
	 * public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String
	 * todate, String currency, String dtltype, String type, String version) {
	 * 
	 * try { logger.info("Generating Excel for BRRS_M_BOP ARCHIVAL Details...");
	 * System.out.println("came to Detail download service"); if
	 * (type.equals("ARCHIVAL") & version != null) {
	 * 
	 * } XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("MSFinP2Detail");
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
	 * List<M_BOP_Archival_Detail_Entity> reportData = M_BOP_Archival_Detail_Repo
	 * .getdatabydateList(parsedToDate, version);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_BOP_Archival_Detail_Entity item : reportData) { XSSFRow row =
	 * sheet.createRow(rowIndex++);
	 * 
	 * row.createCell(0).setCellValue(item.getCust_id());
	 * row.createCell(1).setCellValue(item.getAcct_number());
	 * row.createCell(2).setCellValue(item.getAcct_name() );
	 * 
	 * // ACCT BALANCE (right aligned, 3 decimal places) Cell balanceCell =
	 * row.createCell(3); if (item.getAcct_balance_in_pula() != null) {
	 * balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue()); }
	 * else { balanceCell.setCellValue(0.000); }
	 * balanceCell.setCellStyle(balanceStyle);
	 * 
	 * row.createCell(4).setCellValue(item.getRow_id());
	 * row.createCell(5).setCellValue(item.getColumn_id()); row.createCell(6)
	 * .setCellValue(item.getReport_date() != null ? new
	 * SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date()) : "");
	 * 
	 * // Apply data style for all other cells for (int j = 0; j < 7; j++) { if (j
	 * != 3) { row.getCell(j).setCellStyle(dataStyle); } } } } else {
	 * logger.info("No data found for BRRS_M_OR2 — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating BRRS_M_OR2Excel", e);
	 * return new byte[0]; } }
	 */
	
	
	
	
	public void updateReport(M_BOP_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_BOP_Summary_Entity existing = M_BOP_Summary_Repo.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // 1️⃣ Loop from R10 to R63 and copy fields
	    	
	        for (int i = 13; i <= 36; i++) {
				
	        	 String prefix = "R" + i + "_";

	            String[] fields = { "product",
	        	        "open_position",
	        	        "cpdm_dt_inc",
	        	        "cpdm_dt_dec",
	        	        "net",
	        	        "cpdm_dt_der",
	        	        "cpdm_dt_dto",
	        	        "cp" };

	            for (String field : fields) {
	            	   String getterName = "get" + prefix + field; // e.g., getR10_orig_amt
	                   String setterName = "set" + prefix + field; // e.g., setR10_orig_amt

	                try {
	                    Method getter = M_BOP_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_BOP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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

	    // 3️⃣ Save updated entity
	    M_BOP_Summary_Repo.save(existing);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
