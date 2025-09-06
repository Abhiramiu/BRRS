package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

import com.bornfire.brrs.entities.BRRS_M_CA5_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA5_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA5_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_CA5_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA5_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA5_Summary_Repo2;
import com.bornfire.brrs.entities.M_CA5_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_CA5_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA5_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.M_CA5_Detail_Entity;
import com.bornfire.brrs.entities.M_CA5_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA5_Summary_Entity2;






@Component
@Service

public class BRRS_M_CA5_ReportService {
	private static final Logger logger = LoggerFactory.getLogger( BRRS_M_CA5_ReportService.class);

	@Autowired
	private Environment env;
	
    @Autowired
	SessionFactory sessionFactory;

	
	@Autowired
	BRRS_M_CA5_Summary_Repo1 M_CA5_Summary_Repo1;

	@Autowired
	BRRS_M_CA5_Summary_Repo2 M_CA5_Summary_Repo2;
	
	@Autowired
	BRRS_M_CA5_Detail_Repo M_CA5_Detail_Repo;

	@Autowired
	BRRS_M_CA5_Archival_Summary_Repo1 M_CA5_Archival_Summary_Repo1;
	
	@Autowired
	BRRS_M_CA5_Archival_Summary_Repo2 M_CA5_Archival_Summary_Repo2;
	
	@Autowired
	BRRS_M_CA5_Archival_Detail_Repo M_CA5_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_CA5View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;	
		
	
		if (type.equals("ARCHIVAL") & version != null) {
			List<M_CA5_Archival_Summary_Entity1> T1Master = new ArrayList<M_CA5_Archival_Summary_Entity1>();
			List<M_CA5_Archival_Summary_Entity2> T1Master1 = new ArrayList<M_CA5_Archival_Summary_Entity2>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_CA5_Archival_Summary_Repo1.getdatabydateListarchival(dateformat.parse(todate), version);
				T1Master1 = M_CA5_Archival_Summary_Repo2.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T1Master1);
		} else {
			List<M_CA5_Summary_Entity1> T1Master = new ArrayList<M_CA5_Summary_Entity1>();
			List<M_CA5_Summary_Entity2> T1Master1 = new ArrayList<M_CA5_Summary_Entity2>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_CA5_Summary_Repo1.getdatabydateList(dateformat.parse(todate));
				T1Master1 = M_CA5_Summary_Repo2.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T1Master1);
		}
		
		mv.setViewName("BRRS/M_CA5");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getM_CA5currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {
		int pageSize = 10;       // default
		int currentPage = 0;     // default
		if (pageable != null) {
		pageSize = pageable.getPageSize();
		currentPage = pageable.getPageNumber();
		}
		int startItem = currentPage * pageSize;

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		try {
		Date parsedDate = null;
		if (todate != null && !todate.isEmpty()) {
		parsedDate = dateformat.parse(todate);
		}

		String rowId = null;
		String columnId = null;

		// ✅ Split the filter string safely
		if (filter != null && filter.contains(",")) {
		String[] parts = filter.split(",");
		if (parts.length >= 2) {
		rowId = parts[0];
		columnId = parts[1];
		}
		}

		if ("ARCHIVAL".equals(type) && version != null) {
		// 🔹 Archival branch
		List<M_CA5_Archival_Detail_Entity> T1Dt1;
		if (rowId != null && columnId != null) {
		T1Dt1 = M_CA5_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
		} else {
		T1Dt1 = M_CA5_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
		}

		mv.addObject("reportdetails", T1Dt1);
		mv.addObject("reportmaster12", T1Dt1);
		System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

		} else {
		// 🔹 Current branch
		List<M_CA5_Detail_Entity> T1Dt1;
		if (rowId != null && columnId != null) {
		T1Dt1 = M_CA5_Detail_Repo
		.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
		} else {
		T1Dt1 = M_CA5_Detail_Repo.getdatabydateList(parsedDate);
		}

		mv.addObject("reportdetails", T1Dt1);
		mv.addObject("reportmaster12", T1Dt1);
		System.out.println("DETAIL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
		}

		} catch (ParseException e) {
		e.printStackTrace();
		mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
		e.printStackTrace();
		mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}
		mv.setViewName("BRRS/M_CA5");
		mv.addObject("displaymode", "Details");
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;

}
	
	public byte[] BRRS_M_CA5Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for type: {}", type);

		// ARCHIVAL check
					if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
						logger.info("Service: Generating ARCHIVAL report for version {}", version);
						return getExcelM_CA5ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
					}

		List<M_CA5_Summary_Entity1> dataList =M_CA5_Summary_Repo1.getdatabydateList(dateformat.parse(todate)) ;
		List<M_CA5_Summary_Entity2> dataList1 =M_CA5_Summary_Repo2.getdatabydateList(dateformat.parse(todate));
		
		if (dataList.isEmpty() && dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_CA5 report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);
		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException("Template file not readable: " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			// --- Style Definitions ---
			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8);
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			if (!dataList.isEmpty()) {
				populateEntity1Data(sheet, dataList.get(0), textStyle, numberStyle);
			}

			if (!dataList1.isEmpty()) {
				populateEntity2Data(sheet, dataList1.get(0), textStyle, numberStyle);
			}

			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}
	
	
	
	private void populateEntity1Data(Sheet sheet, M_CA5_Summary_Entity1 record, CellStyle textStyle, CellStyle numberStyle) {
	    
	    // R0020 - ROW 10 (Index 9)
	    Row row = sheet.getRow(13) != null ? sheet.getRow(13) : sheet.createRow(13);	    
	 

                   // Column 2: Note holders
					Cell cell2 = row.createCell(2);
					if (record.getR14_note_holders() != null) {
					    cell2.setCellValue(record.getR14_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					// Column 3: Name of instrument programe
					Cell cell3 = row.createCell(3);
					if (record.getR14_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR14_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// Column 4: Issuing entity if issued throughan spv
					Cell cell4 = row.createCell(4);
					if (record.getR14_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR14_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					// Column 5: Issuance date
					Cell cell5 = row.createCell(5);
					if (record.getR14_issuance_date() != null) {
						Date utilDate = java.sql.Date.valueOf(record.getR14_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					// Column 6: Contractual maturity date
					Cell cell6 = row.createCell(6);
					if (record.getR14_contractual_maturity_date() != null) {
						Date utilDate = java.sql.Date.valueOf(record.getR14_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					// Column 7: Effective maturity date if date
					Cell cell7 = row.createCell(7);
					if (record.getR14_effective_maturity_date_if_date() != null) {
						Date utilDate = java.sql.Date.valueOf(record.getR14_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					// Column 8: Amount
					Cell cell8 = row.createCell(8);
					if (record.getR14_amount() != null) {
					    cell8.setCellValue(record.getR14_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R15 ---
					 cell2 = row.createCell(2);
					if (record.getR15_note_holders() != null) {
					    cell2.setCellValue(record.getR15_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

				     cell3 = row.createCell(3);
					if (record.getR15_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR15_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					 cell4 = row.createCell(4);
					if (record.getR15_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR15_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					 cell5 = row.createCell(5);
					if (record.getR15_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR15_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

				    cell6 = row.createCell(6);
					if (record.getR15_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR15_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR15_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR15_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR15_amount() != null) {
					    cell8.setCellValue(record.getR15_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R16 ---
				    cell2 = row.createCell(2);
					if (record.getR16_note_holders() != null) {
					    cell2.setCellValue(record.getR16_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					 cell3 = row.createCell(3);
					if (record.getR16_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR16_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

				    cell4 = row.createCell(4);
					if (record.getR16_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR16_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

				   cell5 = row.createCell(5);
					if (record.getR16_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR16_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR16_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR16_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR16_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR16_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR16_amount() != null) {
					    cell8.setCellValue(record.getR16_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R17 ---
					 cell2 = row.createCell(2);
					if (record.getR17_note_holders() != null) {
					    cell2.setCellValue(record.getR17_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR17_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					 cell4 = row.createCell(4);
					if (record.getR17_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR17_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					 cell5 = row.createCell(5);
					if (record.getR17_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR17_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR17_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR17_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR17_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR17_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR17_amount() != null) {
					    cell8.setCellValue(record.getR17_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R18 ---
					 cell2 = row.createCell(2);
					if (record.getR18_note_holders() != null) {
					    cell2.setCellValue(record.getR18_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					 cell3 = row.createCell(3);
					if (record.getR18_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR18_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					 cell4 = row.createCell(4);
					if (record.getR18_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR18_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					 cell5 = row.createCell(5);
					if (record.getR18_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR18_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR18_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR18_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR18_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR18_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR18_amount() != null) {
					    cell8.setCellValue(record.getR18_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R19 ---
					 cell2 = row.createCell(2);
					if (record.getR19_note_holders() != null) {
					    cell2.setCellValue(record.getR19_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					 cell3 = row.createCell(3);
					if (record.getR19_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR19_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					 cell4 = row.createCell(4);
					if (record.getR19_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR19_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					 cell5 = row.createCell(5);
					if (record.getR19_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR19_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR19_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR19_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR19_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR19_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR19_amount() != null) {
					    cell8.setCellValue(record.getR19_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R20 ---
					 cell2 = row.createCell(2);
					if (record.getR20_note_holders() != null) {
					    cell2.setCellValue(record.getR20_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					 cell3 = row.createCell(3);
					if (record.getR20_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR20_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					 cell4 = row.createCell(4);
					if (record.getR20_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR20_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					 cell5 = row.createCell(5);
					if (record.getR20_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR20_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR20_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR20_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR20_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR20_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR20_amount() != null) {
					    cell8.setCellValue(record.getR20_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R21 ---
					 cell2 = row.createCell(2);
					if (record.getR21_note_holders() != null) {
					    cell2.setCellValue(record.getR21_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					 cell3 = row.createCell(3);
					if (record.getR21_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR21_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR21_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR21_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					 cell5 = row.createCell(5);
					if (record.getR21_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR21_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR21_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR21_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR21_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR21_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR21_amount() != null) {
					    cell8.setCellValue(record.getR21_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R22 ---
					 cell2 = row.createCell(2);
					if (record.getR22_note_holders() != null) {
					    cell2.setCellValue(record.getR22_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					 cell3 = row.createCell(3);
					if (record.getR22_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR22_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					 cell4 = row.createCell(4);
					if (record.getR22_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR22_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					 cell5 = row.createCell(5);
					if (record.getR22_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR22_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR22_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR22_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR22_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR22_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR22_amount() != null) {
					    cell8.setCellValue(record.getR22_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R23 ---
					 cell2 = row.createCell(2);
					if (record.getR23_note_holders() != null) {
					    cell2.setCellValue(record.getR23_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR23_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR23_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					 cell4 = row.createCell(4);
					if (record.getR23_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR23_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR23_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR23_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR23_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR23_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR23_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR23_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR23_amount() != null) {
					    cell8.setCellValue(record.getR23_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R24 ---
					 cell2 = row.createCell(2);
					if (record.getR24_note_holders() != null) {
					    cell2.setCellValue(record.getR24_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					 cell3 = row.createCell(3);
					if (record.getR24_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR24_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					 cell4 = row.createCell(4);
					if (record.getR24_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR24_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					 cell5 = row.createCell(5);
					if (record.getR24_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR24_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR24_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR24_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR24_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR24_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR24_amount() != null) {
					    cell8.setCellValue(record.getR24_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R25 ---
					 cell2 = row.createCell(2);
					if (record.getR25_note_holders() != null) {
					    cell2.setCellValue(record.getR25_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					 cell3 = row.createCell(3);
					if (record.getR25_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR25_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					 cell4 = row.createCell(4);
					if (record.getR25_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR25_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					 cell5 = row.createCell(5);
					if (record.getR25_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR25_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR25_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR25_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR25_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR25_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR25_amount() != null) {
					    cell8.setCellValue(record.getR25_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R26 ---
					 cell2 = row.createCell(2);
					if (record.getR26_note_holders() != null) {
					    cell2.setCellValue(record.getR26_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					 cell3 = row.createCell(3);
					if (record.getR26_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR26_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					 cell4 = row.createCell(4);
					if (record.getR26_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR26_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR26_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR26_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR26_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR26_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR26_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR26_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR26_amount() != null) {
					    cell8.setCellValue(record.getR26_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R27 ---
					cell2 = row.createCell(2);
					if (record.getR27_note_holders() != null) {
					    cell2.setCellValue(record.getR27_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					 cell3 = row.createCell(3);
					if (record.getR27_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR27_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					 cell4 = row.createCell(4);
					if (record.getR27_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR27_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					 cell5 = row.createCell(5);
					if (record.getR27_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR27_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR27_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR27_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR27_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR27_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR27_amount() != null) {
					    cell8.setCellValue(record.getR27_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					// --- R28 ---
					 cell2 = row.createCell(2);
					if (record.getR28_note_holders() != null) {
					    cell2.setCellValue(record.getR28_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR28_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR28_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR28_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR28_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					 cell5 = row.createCell(5);
					if (record.getR28_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR28_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					 cell6 = row.createCell(6);
					if (record.getR28_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR28_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					 cell7 = row.createCell(7);
					if (record.getR28_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR28_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					 cell8 = row.createCell(8);
					if (record.getR28_amount() != null) {
					    cell8.setCellValue(record.getR28_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}
					
					// --- R35 ---
					cell2 = row.createCell(2);
					if (record.getR35_note_holders() != null) {
					    cell2.setCellValue(record.getR35_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR35_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR35_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR35_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR35_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR35_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR35_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR35_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR35_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR35_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR35_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR35_amount() != null) {
					    cell8.setCellValue(record.getR35_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					Cell cell9 = row.createCell(9);
					if (record.getR35_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR35_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

										
					// --- R36 ---
					cell2 = row.createCell(2);
					if (record.getR36_note_holders() != null) {
					    cell2.setCellValue(record.getR36_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR36_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR36_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR36_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR36_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR36_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR36_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR36_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR36_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR36_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR36_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR36_amount() != null) {
					    cell8.setCellValue(record.getR36_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

				     cell9 = row.createCell(9);
					if (record.getR36_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR36_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

				
					// --- R37 ---
					cell2 = row.createCell(2);
					if (record.getR37_note_holders() != null) {
					    cell2.setCellValue(record.getR37_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR37_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR37_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR37_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR37_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR37_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR37_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR37_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR37_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR37_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR37_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR37_amount() != null) {
					    cell8.setCellValue(record.getR37_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

				    cell9 = row.createCell(9);
					if (record.getR37_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR37_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					

					// --- R38 ---
					cell2 = row.createCell(2);
					if (record.getR38_note_holders() != null) {
					    cell2.setCellValue(record.getR38_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR38_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR38_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR38_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR38_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR38_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR38_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR38_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR38_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR38_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR38_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR38_amount() != null) {
					    cell8.setCellValue(record.getR38_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR38_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR38_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

				
					// --- R39 ---
					cell2 = row.createCell(2);
					if (record.getR39_note_holders() != null) {
					    cell2.setCellValue(record.getR39_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR39_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR39_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR39_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR39_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR39_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR39_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR39_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR39_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR39_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR39_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR39_amount() != null) {
					    cell8.setCellValue(record.getR39_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR39_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR39_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R40 ---
					cell2 = row.createCell(2);
					if (record.getR40_note_holders() != null) {
					    cell2.setCellValue(record.getR40_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR40_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR40_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR40_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR40_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR40_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR40_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR40_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR40_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR40_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR40_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR40_amount() != null) {
					    cell8.setCellValue(record.getR40_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR40_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR40_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					

					// --- R41 ---
					cell2 = row.createCell(2);
					if (record.getR41_note_holders() != null) {
					    cell2.setCellValue(record.getR41_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR41_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR41_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR41_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR41_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR41_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR41_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR41_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR41_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR41_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR41_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR41_amount() != null) {
					    cell8.setCellValue(record.getR41_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR41_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR41_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R42 ---
					cell2 = row.createCell(2);
					if (record.getR42_note_holders() != null) {
					    cell2.setCellValue(record.getR42_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR42_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR42_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR42_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR42_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR42_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR42_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR42_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR42_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR42_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR42_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR42_amount() != null) {
					    cell8.setCellValue(record.getR42_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR42_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR42_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R43 ---
					cell2 = row.createCell(2);
					if (record.getR43_note_holders() != null) {
					    cell2.setCellValue(record.getR43_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR43_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR43_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR43_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR43_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR43_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR43_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR43_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR43_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR43_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR43_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR43_amount() != null) {
					    cell8.setCellValue(record.getR43_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR43_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR43_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R44 ---
					cell2 = row.createCell(2);
					if (record.getR44_note_holders() != null) {
					    cell2.setCellValue(record.getR44_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR44_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR44_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR44_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR44_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR44_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR44_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR44_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR44_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR44_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR44_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR44_amount() != null) {
					    cell8.setCellValue(record.getR44_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR44_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR44_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R45 ---
					cell2 = row.createCell(2);
					if (record.getR45_note_holders() != null) {
					    cell2.setCellValue(record.getR45_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR45_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR45_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR45_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR45_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR45_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR45_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR45_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR45_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR45_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR45_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR45_amount() != null) {
					    cell8.setCellValue(record.getR45_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR45_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR45_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R46 ---
					cell2 = row.createCell(2);
					if (record.getR46_note_holders() != null) {
					    cell2.setCellValue(record.getR46_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR46_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR46_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR46_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR46_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR46_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR46_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR46_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR46_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR46_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR46_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR46_amount() != null) {
					    cell8.setCellValue(record.getR46_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR46_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR46_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R47 ---
					cell2 = row.createCell(2);
					if (record.getR47_note_holders() != null) {
					    cell2.setCellValue(record.getR47_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR47_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR47_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR47_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR47_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR47_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR47_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR47_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR47_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR47_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR47_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR47_amount() != null) {
					    cell8.setCellValue(record.getR47_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR47_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR47_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R48 ---
					cell2 = row.createCell(2);
					if (record.getR48_note_holders() != null) {
					    cell2.setCellValue(record.getR48_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR48_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR48_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR48_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR48_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR48_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR48_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR48_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR48_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR48_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR48_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR48_amount() != null) {
					    cell8.setCellValue(record.getR48_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR48_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR48_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R49 ---
					cell2 = row.createCell(2);
					if (record.getR49_note_holders() != null) {
					    cell2.setCellValue(record.getR49_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR49_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR49_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR49_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR49_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR49_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR49_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR49_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR49_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR49_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR49_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR49_amount() != null) {
					    cell8.setCellValue(record.getR49_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR49_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR49_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R50 ---
					cell2 = row.createCell(2);
					if (record.getR50_note_holders() != null) {
					    cell2.setCellValue(record.getR50_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR50_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR50_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR50_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR50_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR50_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR50_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR50_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR50_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR50_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR50_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR50_amount() != null) {
					    cell8.setCellValue(record.getR50_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR50_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR50_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R51 ---
					cell2 = row.createCell(2);
					if (record.getR51_note_holders() != null) {
					    cell2.setCellValue(record.getR51_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR51_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR51_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR51_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR51_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR51_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR51_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR51_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR51_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR51_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR51_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR51_amount() != null) {
					    cell8.setCellValue(record.getR51_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR51_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR51_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R52 ---
					cell2 = row.createCell(2);
					if (record.getR52_note_holders() != null) {
					    cell2.setCellValue(record.getR52_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR52_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR52_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR52_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR52_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR52_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR52_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR52_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR52_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR52_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR52_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR52_amount() != null) {
					    cell8.setCellValue(record.getR52_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR52_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR52_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					

					// --- R53 ---
					cell2 = row.createCell(2);
					if (record.getR53_note_holders() != null) {
					    cell2.setCellValue(record.getR53_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR53_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR53_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR53_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR53_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR53_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR53_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR53_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR53_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR53_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR53_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR53_amount() != null) {
					    cell8.setCellValue(record.getR53_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR53_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR53_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R54 ---
					cell2 = row.createCell(2);
					if (record.getR54_note_holders() != null) {
					    cell2.setCellValue(record.getR54_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR54_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR54_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR54_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR54_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR54_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR54_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR54_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR54_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR54_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR54_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR54_amount() != null) {
					    cell8.setCellValue(record.getR54_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR54_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR54_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					

					// --- R55 ---
					cell2 = row.createCell(2);
					if (record.getR55_note_holders() != null) {
					    cell2.setCellValue(record.getR55_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR55_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR55_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR55_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR55_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR55_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR55_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR55_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR55_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR55_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR55_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR55_amount() != null) {
					    cell8.setCellValue(record.getR55_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR55_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR55_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R56 ---
					cell2 = row.createCell(2);
					if (record.getR56_note_holders() != null) {
					    cell2.setCellValue(record.getR56_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR56_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR56_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR56_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR56_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR56_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR56_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR56_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR56_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR56_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR56_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR56_amount() != null) {
					    cell8.setCellValue(record.getR56_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR56_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR56_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R57 ---
					cell2 = row.createCell(2);
					if (record.getR57_note_holders() != null) {
					    cell2.setCellValue(record.getR57_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR57_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR57_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR57_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR57_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR57_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR57_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR57_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR57_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR57_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR57_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR57_amount() != null) {
					    cell8.setCellValue(record.getR57_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR57_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR57_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R58 ---
					cell2 = row.createCell(2);
					if (record.getR58_note_holders() != null) {
					    cell2.setCellValue(record.getR58_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR58_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR58_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR58_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR58_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR58_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR58_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR58_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR58_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR58_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR58_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR58_amount() != null) {
					    cell8.setCellValue(record.getR58_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR58_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR58_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R59 ---
					cell2 = row.createCell(2);
					if (record.getR59_note_holders() != null) {
					    cell2.setCellValue(record.getR59_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR59_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR59_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR59_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR59_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR59_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR59_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR59_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR59_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR59_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR59_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR59_amount() != null) {
					    cell8.setCellValue(record.getR59_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR59_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR59_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R60 ---
					cell2 = row.createCell(2);
					if (record.getR60_note_holders() != null) {
					    cell2.setCellValue(record.getR60_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR60_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR60_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR60_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR60_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR60_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR60_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR60_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR60_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR60_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR60_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR60_amount() != null) {
					    cell8.setCellValue(record.getR60_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR60_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR60_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R61 ---
					cell2 = row.createCell(2);
					if (record.getR61_note_holders() != null) {
					    cell2.setCellValue(record.getR61_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR61_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR61_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR61_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR61_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR61_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR61_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR61_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR61_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR61_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR61_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR61_amount() != null) {
					    cell8.setCellValue(record.getR61_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR61_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR61_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R62 ---
					cell2 = row.createCell(2);
					if (record.getR62_note_holders() != null) {
					    cell2.setCellValue(record.getR62_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR62_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR62_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR62_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR62_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR62_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR62_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR62_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR62_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR62_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR62_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR62_amount() != null) {
					    cell8.setCellValue(record.getR62_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR62_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR62_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// --- R63 ---
					cell2 = row.createCell(2);
					if (record.getR63_note_holders() != null) {
					    cell2.setCellValue(record.getR63_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR63_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR63_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR63_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR63_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR63_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR63_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR63_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR63_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR63_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR63_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR63_amount() != null) {
					    cell8.setCellValue(record.getR63_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR63_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR63_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R64 ---
					cell2 = row.createCell(2);
					if (record.getR64_note_holders() != null) {
					    cell2.setCellValue(record.getR64_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR64_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR64_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR64_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR64_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR64_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR64_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR64_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR64_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR64_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR64_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR64_amount() != null) {
					    cell8.setCellValue(record.getR64_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR64_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR64_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R65 ---
					cell2 = row.createCell(2);
					if (record.getR65_note_holders() != null) {
					    cell2.setCellValue(record.getR65_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR65_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR65_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR65_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR65_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR65_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR65_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR65_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR65_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR65_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR65_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR65_amount() != null) {
					    cell8.setCellValue(record.getR65_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR65_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR65_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R66 ---
					cell2 = row.createCell(2);
					if (record.getR66_note_holders() != null) {
					    cell2.setCellValue(record.getR66_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR66_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR66_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR66_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR66_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR66_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR66_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR66_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR66_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR66_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR66_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR66_amount() != null) {
					    cell8.setCellValue(record.getR66_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR66_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR66_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R67 ---
					cell2 = row.createCell(2);
					if (record.getR67_note_holders() != null) {
					    cell2.setCellValue(record.getR67_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR67_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR67_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR67_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR67_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR67_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR67_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR67_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR67_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR67_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR67_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR67_amount() != null) {
					    cell8.setCellValue(record.getR67_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR67_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR67_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R68 ---
					cell2 = row.createCell(2);
					if (record.getR68_note_holders() != null) {
					    cell2.setCellValue(record.getR68_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR68_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR68_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR68_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR68_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR68_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR68_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR68_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR68_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR68_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR68_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR68_amount() != null) {
					    cell8.setCellValue(record.getR68_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR68_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR68_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R69 ---
					cell2 = row.createCell(2);
					if (record.getR69_note_holders() != null) {
					    cell2.setCellValue(record.getR69_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR69_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR69_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR69_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR69_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR69_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR69_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR69_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR69_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR69_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR69_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR69_amount() != null) {
					    cell8.setCellValue(record.getR69_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR69_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR69_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R70 ---
					cell2 = row.createCell(2);
					if (record.getR70_note_holders() != null) {
					    cell2.setCellValue(record.getR70_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR70_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR70_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR70_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR70_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR70_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR70_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR70_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR70_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR70_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR70_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR70_amount() != null) {
					    cell8.setCellValue(record.getR70_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR70_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR70_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R71 ---
					cell2 = row.createCell(2);
					if (record.getR71_note_holders() != null) {
					    cell2.setCellValue(record.getR71_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR71_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR71_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR71_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR71_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR71_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR71_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR71_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR71_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR71_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR71_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR71_amount() != null) {
					    cell8.setCellValue(record.getR71_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR71_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR71_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R72 ---
					cell2 = row.createCell(2);
					if (record.getR72_note_holders() != null) {
					    cell2.setCellValue(record.getR72_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR72_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR72_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR72_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR72_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR72_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR72_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR72_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR72_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR72_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR72_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR72_amount() != null) {
					    cell8.setCellValue(record.getR72_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR72_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR72_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R73 ---
					cell2 = row.createCell(2);
					if (record.getR73_note_holders() != null) {
					    cell2.setCellValue(record.getR73_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR73_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR73_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR73_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR73_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR73_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR73_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR73_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR73_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR73_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR73_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR73_amount() != null) {
					    cell8.setCellValue(record.getR73_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR73_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR73_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R74 ---
					cell2 = row.createCell(2);
					if (record.getR74_note_holders() != null) {
					    cell2.setCellValue(record.getR74_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR74_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR74_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR74_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR74_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR74_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR74_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR74_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR74_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR74_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR74_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR74_amount() != null) {
					    cell8.setCellValue(record.getR74_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR74_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR74_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R75 ---
					cell2 = row.createCell(2);
					if (record.getR75_note_holders() != null) {
					    cell2.setCellValue(record.getR75_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR75_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR75_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR75_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR75_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR75_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR75_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR75_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR75_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR75_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR75_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR75_amount() != null) {
					    cell8.setCellValue(record.getR75_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR75_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR75_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R76 ---
					cell2 = row.createCell(2);
					if (record.getR76_note_holders() != null) {
					    cell2.setCellValue(record.getR76_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR76_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR76_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR76_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR76_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR76_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR76_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR76_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR76_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR76_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR76_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR76_amount() != null) {
					    cell8.setCellValue(record.getR76_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR76_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR76_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R77 ---
					cell2 = row.createCell(2);
					if (record.getR77_note_holders() != null) {
					    cell2.setCellValue(record.getR77_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR77_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR77_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR77_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR77_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR77_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR77_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR77_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR77_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR77_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR77_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR77_amount() != null) {
					    cell8.setCellValue(record.getR77_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR77_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR77_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// --- R78 ---
					cell2 = row.createCell(2);
					if (record.getR78_note_holders() != null) {
					    cell2.setCellValue(record.getR78_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR78_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR78_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR78_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR78_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR78_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR78_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR78_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR78_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR78_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR78_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR78_amount() != null) {
					    cell8.setCellValue(record.getR78_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR78_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR78_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R79 ---
					cell2 = row.createCell(2);
					if (record.getR79_note_holders() != null) {
					    cell2.setCellValue(record.getR79_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR79_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR79_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR79_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR79_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR79_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR79_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR79_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR79_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR79_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR79_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR79_amount() != null) {
					    cell8.setCellValue(record.getR79_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR79_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR79_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R80 ---
					cell2 = row.createCell(2);
					if (record.getR80_note_holders() != null) {
					    cell2.setCellValue(record.getR80_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR80_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR80_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR80_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR80_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR80_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR80_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR80_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR80_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR80_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR80_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR80_amount() != null) {
					    cell8.setCellValue(record.getR80_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR80_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR80_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R81 ---
					cell2 = row.createCell(2);
					if (record.getR81_note_holders() != null) {
					    cell2.setCellValue(record.getR81_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR81_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR81_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR81_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR81_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR81_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR81_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR81_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR81_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR81_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR81_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR81_amount() != null) {
					    cell8.setCellValue(record.getR81_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR81_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR81_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R82 ---
					cell2 = row.createCell(2);
					if (record.getR82_note_holders() != null) {
					    cell2.setCellValue(record.getR82_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR82_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR82_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR82_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR82_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR82_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR82_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR82_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR82_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR82_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR82_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR82_amount() != null) {
					    cell8.setCellValue(record.getR82_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR82_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR82_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R83 ---
					cell2 = row.createCell(2);
					if (record.getR83_note_holders() != null) {
					    cell2.setCellValue(record.getR83_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR83_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR83_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR83_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR83_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR83_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR83_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR83_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR83_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR83_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR83_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR83_amount() != null) {
					    cell8.setCellValue(record.getR83_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR83_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR83_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					

					// --- R84 ---
					cell2 = row.createCell(2);
					if (record.getR84_note_holders() != null) {
					    cell2.setCellValue(record.getR84_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR84_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR84_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR84_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR84_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR84_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR84_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR84_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR84_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR84_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR84_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR84_amount() != null) {
					    cell8.setCellValue(record.getR84_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR84_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR84_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R85 ---
					cell2 = row.createCell(2);
					if (record.getR85_note_holders() != null) {
					    cell2.setCellValue(record.getR85_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR85_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR85_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR85_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR85_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR85_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR85_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR85_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR85_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR85_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR85_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR85_amount() != null) {
					    cell8.setCellValue(record.getR85_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR85_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR85_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R86 ---
					cell2 = row.createCell(2);
					if (record.getR86_note_holders() != null) {
					    cell2.setCellValue(record.getR86_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR86_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR86_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR86_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR86_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR86_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR86_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR86_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR86_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR86_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR86_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR86_amount() != null) {
					    cell8.setCellValue(record.getR86_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR86_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR86_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R87 ---
					cell2 = row.createCell(2);
					if (record.getR87_note_holders() != null) {
					    cell2.setCellValue(record.getR87_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR87_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR87_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR87_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR87_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR87_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR87_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR87_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR87_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR87_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR87_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR87_amount() != null) {
					    cell8.setCellValue(record.getR87_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR87_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR87_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// --- R88 ---
					cell2 = row.createCell(2);
					if (record.getR88_note_holders() != null) {
					    cell2.setCellValue(record.getR88_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR88_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR88_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR88_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR88_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR88_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR88_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR88_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR88_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR88_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR88_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR88_amount() != null) {
					    cell8.setCellValue(record.getR88_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR88_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR88_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					 
					// --- R89 ---
					cell2 = row.createCell(2);
					if (record.getR89_note_holders() != null) {
					    cell2.setCellValue(record.getR89_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR89_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR89_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR89_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR89_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR89_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR89_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR89_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR89_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR89_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR89_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR89_amount() != null) {
					    cell8.setCellValue(record.getR89_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR89_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR89_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R90 ---
					cell2 = row.createCell(2);
					if (record.getR90_note_holders() != null) {
					    cell2.setCellValue(record.getR90_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR90_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR90_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR90_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR90_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR90_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR90_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR90_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR90_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR90_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR90_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR90_amount() != null) {
					    cell8.setCellValue(record.getR90_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR90_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR90_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R91 ---
					cell2 = row.createCell(2);
					if (record.getR91_note_holders() != null) {
					    cell2.setCellValue(record.getR91_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR91_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR91_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR91_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR91_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR91_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR91_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR91_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR91_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR91_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR91_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR91_amount() != null) {
					    cell8.setCellValue(record.getR91_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					cell9 = row.createCell(9);
					if (record.getR91_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR91_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R92 ---
					cell2 = row.createCell(2);
					if (record.getR92_note_holders() != null) {
					    cell2.setCellValue(record.getR92_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR92_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR92_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR92_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR92_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR92_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR92_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR92_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR92_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR92_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR92_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR92_amount() != null) {
					    cell8.setCellValue(record.getR92_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR92_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR92_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}



					// --- R93 ---
					cell2 = row.createCell(2);
					if (record.getR93_note_holders() != null) {
					    cell2.setCellValue(record.getR93_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR93_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR93_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR93_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR93_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR93_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR93_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR93_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR93_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR93_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR93_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR93_amount() != null) {
					    cell8.setCellValue(record.getR93_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR93_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR93_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R94 ---
					cell2 = row.createCell(2);
					if (record.getR94_note_holders() != null) {
					    cell2.setCellValue(record.getR94_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR94_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR94_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR94_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR94_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR94_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR94_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR94_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR94_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR94_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR94_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR94_amount() != null) {
					    cell8.setCellValue(record.getR94_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR94_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR94_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R95 ---
					cell2 = row.createCell(2);
					if (record.getR95_note_holders() != null) {
					    cell2.setCellValue(record.getR95_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR95_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR95_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR95_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR95_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR95_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR95_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR95_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR95_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR95_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR95_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR95_amount() != null) {
					    cell8.setCellValue(record.getR95_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR95_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR95_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R96 ---
					cell2 = row.createCell(2);
					if (record.getR96_note_holders() != null) {
					    cell2.setCellValue(record.getR96_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR96_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR96_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR96_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR96_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR96_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR96_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR96_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR96_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR96_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR96_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR96_amount() != null) {
					    cell8.setCellValue(record.getR96_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR96_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR96_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					// --- R97 ---
					cell2 = row.createCell(2);
					if (record.getR97_note_holders() != null) {
					    cell2.setCellValue(record.getR97_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR97_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR97_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR97_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR97_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR97_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR97_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR97_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR97_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR97_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR97_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR97_amount() != null) {
					    cell8.setCellValue(record.getR97_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR97_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR97_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R98 ---
					cell2 = row.createCell(2);
					if (record.getR98_note_holders() != null) {
					    cell2.setCellValue(record.getR98_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR98_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR98_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR98_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR98_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR98_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR98_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR98_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR98_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR98_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR98_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR98_amount() != null) {
					    cell8.setCellValue(record.getR98_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR98_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR98_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R99 ---
					cell2 = row.createCell(2);
					if (record.getR99_note_holders() != null) {
					    cell2.setCellValue(record.getR99_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR99_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR99_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR99_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR99_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR99_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR99_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR99_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR99_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR99_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR99_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR99_amount() != null) {
					    cell8.setCellValue(record.getR99_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR99_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR99_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}

					
					// --- R100 ---
					cell2 = row.createCell(2);
					if (record.getR100_note_holders() != null) {
					    cell2.setCellValue(record.getR100_note_holders());
					    cell2.setCellStyle(textStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR100_name_of_instrument_programe() != null) {
					    cell3.setCellValue(record.getR100_name_of_instrument_programe());
					    cell3.setCellStyle(textStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR100_issuing_entity_if_issued_throughan_spv() != null) {
					    cell4.setCellValue(record.getR100_issuing_entity_if_issued_throughan_spv());
					    cell4.setCellStyle(textStyle);
					} else {
					    cell4.setCellValue("");
					    cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR100_issuance_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR100_issuance_date().toString());
					    cell5.setCellValue(utilDate);
					} else {
					    cell5.setCellValue("");
					    cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR100_contractual_maturity_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR100_contractual_maturity_date().toString());
					    cell6.setCellValue(utilDate);
					} else {
					    cell6.setCellValue("");
					    cell6.setCellStyle(textStyle);
					}

					cell7 = row.createCell(7);
					if (record.getR100_effective_maturity_date_if_date() != null) {
					    Date utilDate = java.sql.Date.valueOf(record.getR100_effective_maturity_date_if_date().toString());
					    cell7.setCellValue(utilDate);
					} else {
					    cell7.setCellValue("");
					    cell7.setCellStyle(textStyle);
					}

					cell8 = row.createCell(8);
					if (record.getR100_amount() != null) {
					    cell8.setCellValue(record.getR100_amount().doubleValue());
					    cell8.setCellStyle(numberStyle);
					} else {
					    cell8.setCellValue("");
					    cell8.setCellStyle(textStyle);
					}

					 cell9 = row.createCell(9);
					if (record.getR100_amount_derecognised_p000() != null) {
					    cell9.setCellValue(record.getR100_amount_derecognised_p000().doubleValue());
					    cell9.setCellStyle(numberStyle);
					} else {
					    cell9.setCellValue("");
					    cell9.setCellStyle(textStyle);
					}
	}
				
private void populateEntity2Data(Sheet sheet, M_CA5_Summary_Entity2 record1, CellStyle textStyle, CellStyle numberStyle) {
			    	    
					    // R0530 - ROW 61 (Index 60)
Row row = sheet.getRow(100) != null ? sheet.getRow(100) : sheet.createRow(100);
					    
					   
			
							// --- R101 ---
							Cell cell2 = row.createCell(2);
							if (record1.getR101_note_holders() != null) {
							    cell2.setCellValue(record1.getR101_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}	
							
							Cell cell3 = row.createCell(3);
							if (record1.getR101_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR101_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(4);
							if (record1.getR101_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR101_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							Cell cell5 = row.createCell(5);
							if (record1.getR101_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR101_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							Cell cell6 = row.createCell(6);
							if (record1.getR101_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR101_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							Cell cell7 = row.createCell(7);
							if (record1.getR101_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR101_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							Cell cell8 = row.createCell(8);
							if (record1.getR101_amount() != null) {
							    cell8.setCellValue(record1.getR101_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							Cell cell9 = row.createCell(9);
							if (record1.getR101_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR101_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

						
							// --- R102 ---
							cell2 = row.createCell(2);
							if (record1.getR102_note_holders() != null) {
							    cell2.setCellValue(record1.getR102_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR102_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR102_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR102_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR102_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR102_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR102_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR102_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR102_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR102_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR102_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR102_amount() != null) {
							    cell8.setCellValue(record1.getR102_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR102_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR102_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R103 ---
							cell2 = row.createCell(2);
							if (record1.getR103_note_holders() != null) {
							    cell2.setCellValue(record1.getR103_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR103_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR103_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR103_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR103_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR103_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR103_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR103_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR103_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR103_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR103_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR103_amount() != null) {
							    cell8.setCellValue(record1.getR103_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR103_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR103_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R104 ---
							cell2 = row.createCell(2);
							if (record1.getR104_note_holders() != null) {
							    cell2.setCellValue(record1.getR104_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR104_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR104_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR104_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR104_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR104_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR104_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR104_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR104_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR104_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR104_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR104_amount() != null) {
							    cell8.setCellValue(record1.getR104_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR104_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR104_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R105 ---
							cell2 = row.createCell(2);
							if (record1.getR105_note_holders() != null) {
							    cell2.setCellValue(record1.getR105_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR105_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR105_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR105_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR105_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR105_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR105_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR105_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR105_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR105_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR105_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR105_amount() != null) {
							    cell8.setCellValue(record1.getR105_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR105_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR105_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R106 ---
							cell2 = row.createCell(2);
							if (record1.getR106_note_holders() != null) {
							    cell2.setCellValue(record1.getR106_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR106_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR106_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR106_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR106_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR106_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR106_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR106_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR106_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR106_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR106_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR106_amount() != null) {
							    cell8.setCellValue(record1.getR106_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR106_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR106_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R107 ---
							cell2 = row.createCell(2);
							if (record1.getR107_note_holders() != null) {
							    cell2.setCellValue(record1.getR107_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR107_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR107_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR107_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR107_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR107_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR107_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR107_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR107_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR107_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR107_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR107_amount() != null) {
							    cell8.setCellValue(record1.getR107_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR107_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR107_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R108 ---
							cell2 = row.createCell(2);
							if (record1.getR108_note_holders() != null) {
							    cell2.setCellValue(record1.getR108_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR108_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR108_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR108_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR108_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR108_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR108_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR108_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR108_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR108_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR108_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR108_amount() != null) {
							    cell8.setCellValue(record1.getR108_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR108_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR108_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R109 ---
							cell2 = row.createCell(2);
							if (record1.getR109_note_holders() != null) {
							    cell2.setCellValue(record1.getR109_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR109_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR109_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR109_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR109_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR109_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR109_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR109_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR109_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR109_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR109_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR109_amount() != null) {
							    cell8.setCellValue(record1.getR109_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR109_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR109_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R110 ---
							cell2 = row.createCell(2);
							if (record1.getR110_note_holders() != null) {
							    cell2.setCellValue(record1.getR110_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR110_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR110_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR110_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR110_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR110_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR110_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR110_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR110_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR110_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR110_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR110_amount() != null) {
							    cell8.setCellValue(record1.getR110_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR110_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR110_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R111 ---
							cell2 = row.createCell(2);
							if (record1.getR111_note_holders() != null) {
							    cell2.setCellValue(record1.getR111_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR111_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR111_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR111_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR111_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR111_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR111_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR111_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR111_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR111_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR111_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR111_amount() != null) {
							    cell8.setCellValue(record1.getR111_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR111_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR111_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R112 ---
							cell2 = row.createCell(2);
							if (record1.getR112_note_holders() != null) {
							    cell2.setCellValue(record1.getR112_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR112_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR112_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR112_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR112_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR112_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR112_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR112_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR112_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR112_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR112_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR112_amount() != null) {
							    cell8.setCellValue(record1.getR112_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR112_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR112_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R113 ---
							cell2 = row.createCell(2);
							if (record1.getR113_note_holders() != null) {
							    cell2.setCellValue(record1.getR113_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR113_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR113_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR113_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR113_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR113_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR113_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR113_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR113_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR113_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR113_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR113_amount() != null) {
							    cell8.setCellValue(record1.getR113_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR113_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR113_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R114 ---
							cell2 = row.createCell(2);
							if (record1.getR114_note_holders() != null) {
							    cell2.setCellValue(record1.getR114_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR114_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR114_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR114_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR114_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR114_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR114_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR114_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR114_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR114_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR114_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR114_amount() != null) {
							    cell8.setCellValue(record1.getR114_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR114_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR114_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R115 ---
							cell2 = row.createCell(2);
							if (record1.getR115_note_holders() != null) {
							    cell2.setCellValue(record1.getR115_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR115_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR115_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR115_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR115_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR115_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR115_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR115_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR115_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR115_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR115_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR115_amount() != null) {
							    cell8.setCellValue(record1.getR115_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR115_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR115_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R116 ---
							cell2 = row.createCell(2);
							if (record1.getR116_note_holders() != null) {
							    cell2.setCellValue(record1.getR116_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR116_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR116_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR116_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR116_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR116_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR116_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR116_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR116_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR116_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR116_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR116_amount() != null) {
							    cell8.setCellValue(record1.getR116_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR116_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR116_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R117 ---
							cell2 = row.createCell(2);
							if (record1.getR117_note_holders() != null) {
							    cell2.setCellValue(record1.getR117_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR117_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR117_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR117_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR117_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR117_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR117_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR117_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR117_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR117_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR117_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR117_amount() != null) {
							    cell8.setCellValue(record1.getR117_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR117_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR117_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R118 ---
							cell2 = row.createCell(2);
							if (record1.getR118_note_holders() != null) {
							    cell2.setCellValue(record1.getR118_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR118_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR118_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR118_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR118_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR118_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR118_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR118_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR118_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR118_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR118_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR118_amount() != null) {
							    cell8.setCellValue(record1.getR118_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR118_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR118_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R119 ---
							cell2 = row.createCell(2);
							if (record1.getR119_note_holders() != null) {
							    cell2.setCellValue(record1.getR119_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR119_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR119_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR119_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR119_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR119_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR119_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR119_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR119_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR119_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR119_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR119_amount() != null) {
							    cell8.setCellValue(record1.getR119_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR119_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR119_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R120 ---
							cell2 = row.createCell(2);
							if (record1.getR120_note_holders() != null) {
							    cell2.setCellValue(record1.getR120_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR120_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR120_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR120_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR120_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR120_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR120_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR120_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR120_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR120_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR120_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR120_amount() != null) {
							    cell8.setCellValue(record1.getR120_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR120_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR120_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R121 ---
							cell2 = row.createCell(2);
							if (record1.getR121_note_holders() != null) {
							    cell2.setCellValue(record1.getR121_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR121_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR121_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR121_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR121_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR121_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR121_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR121_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR121_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR121_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR121_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR121_amount() != null) {
							    cell8.setCellValue(record1.getR121_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR121_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR121_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R122 ---
							cell2 = row.createCell(2);
							if (record1.getR122_note_holders() != null) {
							    cell2.setCellValue(record1.getR122_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR122_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR122_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR122_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR122_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR122_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR122_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR122_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR122_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR122_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR122_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR122_amount() != null) {
							    cell8.setCellValue(record1.getR122_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR122_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR122_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R123 ---
							cell2 = row.createCell(2);
							if (record1.getR123_note_holders() != null) {
							    cell2.setCellValue(record1.getR123_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR123_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR123_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR123_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR123_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR123_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR123_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR123_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR123_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR123_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR123_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR123_amount() != null) {
							    cell8.setCellValue(record1.getR123_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR123_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR123_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R124 ---
							cell2 = row.createCell(2);
							if (record1.getR124_note_holders() != null) {
							    cell2.setCellValue(record1.getR124_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR124_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR124_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR124_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR124_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR124_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR124_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR124_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR124_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR124_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR124_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR124_amount() != null) {
							    cell8.setCellValue(record1.getR124_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR124_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR124_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R125 ---
							cell2 = row.createCell(2);
							if (record1.getR125_note_holders() != null) {
							    cell2.setCellValue(record1.getR125_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR125_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR125_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR125_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR125_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR125_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR125_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR125_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR125_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR125_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR125_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR125_amount() != null) {
							    cell8.setCellValue(record1.getR125_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR125_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR125_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R126 ---
							cell2 = row.createCell(2);
							if (record1.getR126_note_holders() != null) {
							    cell2.setCellValue(record1.getR126_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR126_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR126_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR126_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR126_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR126_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR126_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR126_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR126_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR126_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR126_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR126_amount() != null) {
							    cell8.setCellValue(record1.getR126_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR126_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR126_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R127 ---
							cell2 = row.createCell(2);
							if (record1.getR127_note_holders() != null) {
							    cell2.setCellValue(record1.getR127_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR127_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR127_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR127_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR127_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR127_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR127_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR127_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR127_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR127_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR127_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR127_amount() != null) {
							    cell8.setCellValue(record1.getR127_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR127_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR127_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R128 ---
							cell2 = row.createCell(2);
							if (record1.getR128_note_holders() != null) {
							    cell2.setCellValue(record1.getR128_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR128_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR128_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR128_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR128_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR128_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR128_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR128_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR128_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR128_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR128_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR128_amount() != null) {
							    cell8.setCellValue(record1.getR128_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR128_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR128_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R129 ---
							cell2 = row.createCell(2);
							if (record1.getR129_note_holders() != null) {
							    cell2.setCellValue(record1.getR129_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR129_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR129_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR129_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR129_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR129_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR129_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR129_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR129_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR129_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR129_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR129_amount() != null) {
							    cell8.setCellValue(record1.getR129_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR129_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR129_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R130 ---
							cell2 = row.createCell(2);
							if (record1.getR130_note_holders() != null) {
							    cell2.setCellValue(record1.getR130_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR130_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR130_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR130_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR130_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR130_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR130_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR130_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR130_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR130_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR130_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR130_amount() != null) {
							    cell8.setCellValue(record1.getR130_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR130_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR130_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R131 ---
							cell2 = row.createCell(2);
							if (record1.getR131_note_holders() != null) {
							    cell2.setCellValue(record1.getR131_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR131_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR131_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR131_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR131_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR131_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR131_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR131_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR131_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR131_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR131_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR131_amount() != null) {
							    cell8.setCellValue(record1.getR131_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR131_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR131_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R132 ---
							cell2 = row.createCell(2);
							if (record1.getR132_note_holders() != null) {
							    cell2.setCellValue(record1.getR132_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR132_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR132_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR132_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR132_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR132_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR132_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR132_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR132_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR132_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR132_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR132_amount() != null) {
							    cell8.setCellValue(record1.getR132_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR132_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR132_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R133 ---
							cell2 = row.createCell(2);
							if (record1.getR133_note_holders() != null) {
							    cell2.setCellValue(record1.getR133_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR133_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR133_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR133_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR133_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR133_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR133_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR133_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR133_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR133_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR133_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR133_amount() != null) {
							    cell8.setCellValue(record1.getR133_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR133_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR133_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R134 ---
							cell2 = row.createCell(2);
							if (record1.getR134_note_holders() != null) {
							    cell2.setCellValue(record1.getR134_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR134_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR134_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR134_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR134_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR134_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR134_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR134_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR134_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR134_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR134_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR134_amount() != null) {
							    cell8.setCellValue(record1.getR134_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR134_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR134_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R135 ---
							cell2 = row.createCell(2);
							if (record1.getR135_note_holders() != null) {
							    cell2.setCellValue(record1.getR135_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR135_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR135_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR135_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR135_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR135_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR135_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR135_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR135_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR135_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR135_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR135_amount() != null) {
							    cell8.setCellValue(record1.getR135_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR135_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR135_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R136 ---
							cell2 = row.createCell(2);
							if (record1.getR136_note_holders() != null) {
							    cell2.setCellValue(record1.getR136_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR136_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR136_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR136_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR136_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR136_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR136_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR136_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR136_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR136_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR136_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR136_amount() != null) {
							    cell8.setCellValue(record1.getR136_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR136_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR136_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R137 ---
							cell2 = row.createCell(2);
							if (record1.getR137_note_holders() != null) {
							    cell2.setCellValue(record1.getR137_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR137_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR137_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR137_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR137_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR137_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR137_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR137_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR137_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR137_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR137_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR137_amount() != null) {
							    cell8.setCellValue(record1.getR137_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR137_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR137_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R138 ---
							cell2 = row.createCell(2);
							if (record1.getR138_note_holders() != null) {
							    cell2.setCellValue(record1.getR138_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR138_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR138_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR138_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR138_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR138_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR138_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR138_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR138_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR138_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR138_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR138_amount() != null) {
							    cell8.setCellValue(record1.getR138_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR138_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR138_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R139 ---
							cell2 = row.createCell(2);
							if (record1.getR139_note_holders() != null) {
							    cell2.setCellValue(record1.getR139_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR139_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR139_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR139_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR139_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR139_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR139_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR139_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR139_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR139_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR139_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR139_amount() != null) {
							    cell8.setCellValue(record1.getR139_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR139_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR139_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R140 ---
							cell2 = row.createCell(2);
							if (record1.getR140_note_holders() != null) {
							    cell2.setCellValue(record1.getR140_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR140_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR140_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR140_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR140_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR140_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR140_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR140_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR140_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR140_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR140_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR140_amount() != null) {
							    cell8.setCellValue(record1.getR140_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR140_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR140_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R141 ---
							cell2 = row.createCell(2);
							if (record1.getR141_note_holders() != null) {
							    cell2.setCellValue(record1.getR141_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR141_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR141_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR141_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR141_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR141_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR141_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR141_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR141_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR141_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR141_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR141_amount() != null) {
							    cell8.setCellValue(record1.getR141_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR141_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR141_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R142 ---
							cell2 = row.createCell(2);
							if (record1.getR142_note_holders() != null) {
							    cell2.setCellValue(record1.getR142_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR142_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR142_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR142_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR142_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR142_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR142_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR142_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR142_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR142_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR142_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR142_amount() != null) {
							    cell8.setCellValue(record1.getR142_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR142_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR142_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R143 ---
							cell2 = row.createCell(2);
							if (record1.getR143_note_holders() != null) {
							    cell2.setCellValue(record1.getR143_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR143_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR143_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR143_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR143_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR143_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR143_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR143_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR143_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR143_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR143_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR143_amount() != null) {
							    cell8.setCellValue(record1.getR143_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR143_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR143_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R144 ---
							cell2 = row.createCell(2);
							if (record1.getR144_note_holders() != null) {
							    cell2.setCellValue(record1.getR144_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR144_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR144_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR144_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR144_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR144_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR144_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR144_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR144_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR144_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR144_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR144_amount() != null) {
							    cell8.setCellValue(record1.getR144_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR144_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR144_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R145 ---
							cell2 = row.createCell(2);
							if (record1.getR145_note_holders() != null) {
							    cell2.setCellValue(record1.getR145_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR145_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR145_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR145_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR145_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR145_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR145_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR145_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR145_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR145_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR145_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR145_amount() != null) {
							    cell8.setCellValue(record1.getR145_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR145_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR145_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R146 ---
							cell2 = row.createCell(2);
							if (record1.getR146_note_holders() != null) {
							    cell2.setCellValue(record1.getR146_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR146_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR146_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR146_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR146_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR146_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR146_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR146_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR146_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR146_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR146_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR146_amount() != null) {
							    cell8.setCellValue(record1.getR146_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR146_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR146_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							// --- R147 ---
							cell2 = row.createCell(2);
							if (record1.getR147_note_holders() != null) {
							    cell2.setCellValue(record1.getR147_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR147_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR147_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR147_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR147_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR147_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR147_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR147_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR147_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR147_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR147_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR147_amount() != null) {
							    cell8.setCellValue(record1.getR147_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR147_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR147_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R148 ---
							cell2 = row.createCell(2);
							if (record1.getR148_note_holders() != null) {
							    cell2.setCellValue(record1.getR148_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR148_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR148_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR148_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR148_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR148_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR148_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR148_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR148_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR148_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR148_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR148_amount() != null) {
							    cell8.setCellValue(record1.getR148_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR148_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR148_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R149 ---
							cell2 = row.createCell(2);
							if (record1.getR149_note_holders() != null) {
							    cell2.setCellValue(record1.getR149_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR149_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR149_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR149_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR149_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR149_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR149_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR149_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR149_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR149_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR149_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR149_amount() != null) {
							    cell8.setCellValue(record1.getR149_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR149_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR149_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}

							
							// --- R150 ---
							cell2 = row.createCell(2);
							if (record1.getR150_note_holders() != null) {
							    cell2.setCellValue(record1.getR150_note_holders());
							    cell2.setCellStyle(textStyle);
							} else {
							    cell2.setCellValue("");
							    cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record1.getR150_name_of_instrument_programe() != null) {
							    cell3.setCellValue(record1.getR150_name_of_instrument_programe());
							    cell3.setCellStyle(textStyle);
							} else {
							    cell3.setCellValue("");
							    cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record1.getR150_issuing_entity_if_issued_throughan_spv() != null) {
							    cell4.setCellValue(record1.getR150_issuing_entity_if_issued_throughan_spv());
							    cell4.setCellStyle(textStyle);
							} else {
							    cell4.setCellValue("");
							    cell4.setCellStyle(textStyle);
							}

							cell5 = row.createCell(5);
							if (record1.getR150_issuance_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR150_issuance_date().toString());
							    cell5.setCellValue(utilDate);
							} else {
							    cell5.setCellValue("");
							    cell5.setCellStyle(textStyle);
							}

							cell6 = row.createCell(6);
							if (record1.getR150_contractual_maturity_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR150_contractual_maturity_date().toString());
							    cell6.setCellValue(utilDate);
							} else {
							    cell6.setCellValue("");
							    cell6.setCellStyle(textStyle);
							}

							cell7 = row.createCell(7);
							if (record1.getR150_effective_maturity_date_if_date() != null) {
							    Date utilDate = java.sql.Date.valueOf(record1.getR150_effective_maturity_date_if_date().toString());
							    cell7.setCellValue(utilDate);
							} else {
							    cell7.setCellValue("");
							    cell7.setCellStyle(textStyle);
							}

							cell8 = row.createCell(8);
							if (record1.getR150_amount() != null) {
							    cell8.setCellValue(record1.getR150_amount().doubleValue());
							    cell8.setCellStyle(numberStyle);
							} else {
							    cell8.setCellValue("");
							    cell8.setCellStyle(textStyle);
							}

							cell9 = row.createCell(9);
							if (record1.getR150_amount_derecognised_p000() != null) {
							    cell9.setCellValue(record1.getR150_amount_derecognised_p000().doubleValue());
							    cell9.setCellStyle(numberStyle);
							} else {
							    cell9.setCellValue("");
							    cell9.setCellStyle(textStyle);
							}
					
							
						}
						



public List<Object> getM_CA5Archival() {
	List<Object> M_CA5ArchivalList = new ArrayList<>();
	try {
		M_CA5ArchivalList.addAll(M_CA5_Archival_Summary_Repo1.getM_CA5archival());
		M_CA5ArchivalList.addAll(M_CA5_Archival_Summary_Repo2.getM_CA5archival());
		logger.info("Fetched {} archival records.", M_CA5ArchivalList.size());
	} catch (Exception e) {
		logger.error("Error fetching M_CA5 Archival data: {}", e.getMessage(), e);
	}
	return M_CA5ArchivalList;
}




public byte[] BRRS_M_CA5DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
		String type, String version) {
    try {
        logger.info("Generating Excel for M_CA5 Details...");
        System.out.println("came to Detail download service");
        
        if (type.equals("ARCHIVAL") & version != null) {
			byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
					version);
			return ARCHIVALreport;
		}
			        XSSFWorkbook workbook = new XSSFWorkbook();
			        XSSFSheet sheet = workbook.createSheet("BWRBR_M_CA5Details");

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
			        String[] headers = {
			            "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE"
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

			        // Get data
			        Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			        List<M_CA5_Detail_Entity> reportData = M_CA5_Detail_Repo.getdatabydateList(parsedToDate);

			        if (reportData != null && !reportData.isEmpty()) {
			            int rowIndex = 1;
			            for (M_CA5_Detail_Entity item : reportData) {
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
			                row.createCell(6).setCellValue(
			                    item.getReportDate() != null ?
			                    new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : ""
			                );

			                // Apply data style for all other cells
			                for (int j = 0; j < 7; j++) {
			                    if (j != 3) {
			                        row.getCell(j).setCellStyle(dataStyle);
			                    }
			                }
			            }
			        } else {
			            logger.info("No data found for BWRBR_M_CA5 — only header will be written.");
			        }

			        // Write to byte[]
			        ByteArrayOutputStream bos = new ByteArrayOutputStream();
			        workbook.write(bos);
			        workbook.close();

			        logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			        return bos.toByteArray();

			    } catch (Exception e) {
			        logger.error("Error generating BWRBR_M_CA5 Excel", e);
			        return new byte[0];
			    }
			}





public byte[] getExcelM_CA5ARCHIVAL(String filename, String reportId, String fromdate, String todate, String currency,
		String dtltype, String type, String version) throws Exception {

	logger.info("Service: Starting Excel generation process in memory for type: {}", type);

	if (type.equals("ARCHIVAL") & version != null) {

	}
	List<M_CA5_Archival_Summary_Entity1> dataList = M_CA5_Archival_Summary_Repo1
			.getdatabydateListarchival(dateformat.parse(todate), version);
	List<M_CA5_Archival_Summary_Entity2> dataList1 = M_CA5_Archival_Summary_Repo2
			.getdatabydateListarchival(dateformat.parse(todate), version);


	if (dataList.isEmpty() && dataList1.isEmpty()) {
		logger.warn("Service: No data found for M_CA5 report. Returning empty result.");
		return new byte[0];
	}

	String templateDir = env.getProperty("output.exportpathtemp");
	Path templatePath = Paths.get(templateDir, filename);
	logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

	if (!Files.exists(templatePath)) {
		throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
	}
	if (!Files.isReadable(templatePath)) {
		throw new SecurityException("Template file not readable: " + templatePath.toAbsolutePath());
	}

	try (InputStream templateInputStream = Files.newInputStream(templatePath);
			Workbook workbook = WorkbookFactory.create(templateInputStream);
			ByteArrayOutputStream out = new ByteArrayOutputStream()) {

		Sheet sheet = workbook.getSheetAt(0);

		// --- Style Definitions ---
		CellStyle textStyle = workbook.createCellStyle();
		textStyle.setBorderBottom(BorderStyle.THIN);
		textStyle.setBorderTop(BorderStyle.THIN);
		textStyle.setBorderLeft(BorderStyle.THIN);
		textStyle.setBorderRight(BorderStyle.THIN);

		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setFontName("Arial");

		CellStyle numberStyle = workbook.createCellStyle();
		numberStyle.setBorderBottom(BorderStyle.THIN);
		numberStyle.setBorderTop(BorderStyle.THIN);
		numberStyle.setBorderLeft(BorderStyle.THIN);
		numberStyle.setBorderRight(BorderStyle.THIN);
		numberStyle.setFont(font);
		// --- End of Style Definitions ---

		if (!dataList.isEmpty()) {
			populateEntity1Data(sheet, dataList.get(0), textStyle, numberStyle);
		}

		if (!dataList1.isEmpty()) {
			populateEntity2Data(sheet, dataList1.get(0), textStyle, numberStyle);
		}

		workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
		workbook.write(out);
		logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
		return out.toByteArray();
	}
}



private void populateEntity1Data(Sheet sheet, M_CA5_Archival_Summary_Entity1 record, CellStyle textStyle, CellStyle numberStyle) {
    
    // R0020 - ROW 10 (Index 9)
    Row row = sheet.getRow(13) != null ? sheet.getRow(13) : sheet.createRow(13);	    
 

               // Column 2: Note holders
				Cell cell2 = row.createCell(2);
				if (record.getR14_note_holders() != null) {
				    cell2.setCellValue(record.getR14_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				// Column 3: Name of instrument programe
				Cell cell3 = row.createCell(3);
				if (record.getR14_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR14_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				// Column 4: Issuing entity if issued throughan spv
				Cell cell4 = row.createCell(4);
				if (record.getR14_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR14_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				// Column 5: Issuance date
				Cell cell5 = row.createCell(5);
				if (record.getR14_issuance_date() != null) {
					Date utilDate = java.sql.Date.valueOf(record.getR14_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				// Column 6: Contractual maturity date
				Cell cell6 = row.createCell(6);
				if (record.getR14_contractual_maturity_date() != null) {
					Date utilDate = java.sql.Date.valueOf(record.getR14_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				// Column 7: Effective maturity date if date
				Cell cell7 = row.createCell(7);
				if (record.getR14_effective_maturity_date_if_date() != null) {
					Date utilDate = java.sql.Date.valueOf(record.getR14_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				// Column 8: Amount
				Cell cell8 = row.createCell(8);
				if (record.getR14_amount() != null) {
				    cell8.setCellValue(record.getR14_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R15 ---
				 cell2 = row.createCell(2);
				if (record.getR15_note_holders() != null) {
				    cell2.setCellValue(record.getR15_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

			     cell3 = row.createCell(3);
				if (record.getR15_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR15_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				 cell4 = row.createCell(4);
				if (record.getR15_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR15_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				 cell5 = row.createCell(5);
				if (record.getR15_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR15_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

			    cell6 = row.createCell(6);
				if (record.getR15_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR15_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR15_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR15_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR15_amount() != null) {
				    cell8.setCellValue(record.getR15_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R16 ---
			    cell2 = row.createCell(2);
				if (record.getR16_note_holders() != null) {
				    cell2.setCellValue(record.getR16_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				 cell3 = row.createCell(3);
				if (record.getR16_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR16_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

			    cell4 = row.createCell(4);
				if (record.getR16_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR16_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

			   cell5 = row.createCell(5);
				if (record.getR16_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR16_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR16_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR16_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR16_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR16_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR16_amount() != null) {
				    cell8.setCellValue(record.getR16_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R17 ---
				 cell2 = row.createCell(2);
				if (record.getR17_note_holders() != null) {
				    cell2.setCellValue(record.getR17_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR17_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR17_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				 cell4 = row.createCell(4);
				if (record.getR17_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR17_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				 cell5 = row.createCell(5);
				if (record.getR17_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR17_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR17_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR17_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR17_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR17_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR17_amount() != null) {
				    cell8.setCellValue(record.getR17_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R18 ---
				 cell2 = row.createCell(2);
				if (record.getR18_note_holders() != null) {
				    cell2.setCellValue(record.getR18_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				 cell3 = row.createCell(3);
				if (record.getR18_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR18_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				 cell4 = row.createCell(4);
				if (record.getR18_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR18_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				 cell5 = row.createCell(5);
				if (record.getR18_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR18_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR18_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR18_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR18_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR18_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR18_amount() != null) {
				    cell8.setCellValue(record.getR18_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R19 ---
				 cell2 = row.createCell(2);
				if (record.getR19_note_holders() != null) {
				    cell2.setCellValue(record.getR19_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				 cell3 = row.createCell(3);
				if (record.getR19_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR19_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				 cell4 = row.createCell(4);
				if (record.getR19_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR19_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				 cell5 = row.createCell(5);
				if (record.getR19_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR19_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR19_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR19_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR19_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR19_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR19_amount() != null) {
				    cell8.setCellValue(record.getR19_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R20 ---
				 cell2 = row.createCell(2);
				if (record.getR20_note_holders() != null) {
				    cell2.setCellValue(record.getR20_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				 cell3 = row.createCell(3);
				if (record.getR20_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR20_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				 cell4 = row.createCell(4);
				if (record.getR20_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR20_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				 cell5 = row.createCell(5);
				if (record.getR20_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR20_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR20_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR20_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR20_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR20_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR20_amount() != null) {
				    cell8.setCellValue(record.getR20_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R21 ---
				 cell2 = row.createCell(2);
				if (record.getR21_note_holders() != null) {
				    cell2.setCellValue(record.getR21_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				 cell3 = row.createCell(3);
				if (record.getR21_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR21_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR21_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR21_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				 cell5 = row.createCell(5);
				if (record.getR21_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR21_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR21_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR21_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR21_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR21_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR21_amount() != null) {
				    cell8.setCellValue(record.getR21_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R22 ---
				 cell2 = row.createCell(2);
				if (record.getR22_note_holders() != null) {
				    cell2.setCellValue(record.getR22_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				 cell3 = row.createCell(3);
				if (record.getR22_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR22_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				 cell4 = row.createCell(4);
				if (record.getR22_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR22_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				 cell5 = row.createCell(5);
				if (record.getR22_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR22_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR22_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR22_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR22_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR22_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR22_amount() != null) {
				    cell8.setCellValue(record.getR22_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R23 ---
				 cell2 = row.createCell(2);
				if (record.getR23_note_holders() != null) {
				    cell2.setCellValue(record.getR23_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR23_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR23_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				 cell4 = row.createCell(4);
				if (record.getR23_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR23_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR23_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR23_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR23_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR23_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR23_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR23_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR23_amount() != null) {
				    cell8.setCellValue(record.getR23_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R24 ---
				 cell2 = row.createCell(2);
				if (record.getR24_note_holders() != null) {
				    cell2.setCellValue(record.getR24_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				 cell3 = row.createCell(3);
				if (record.getR24_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR24_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				 cell4 = row.createCell(4);
				if (record.getR24_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR24_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				 cell5 = row.createCell(5);
				if (record.getR24_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR24_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR24_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR24_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR24_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR24_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR24_amount() != null) {
				    cell8.setCellValue(record.getR24_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R25 ---
				 cell2 = row.createCell(2);
				if (record.getR25_note_holders() != null) {
				    cell2.setCellValue(record.getR25_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				 cell3 = row.createCell(3);
				if (record.getR25_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR25_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				 cell4 = row.createCell(4);
				if (record.getR25_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR25_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				 cell5 = row.createCell(5);
				if (record.getR25_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR25_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR25_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR25_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR25_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR25_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR25_amount() != null) {
				    cell8.setCellValue(record.getR25_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R26 ---
				 cell2 = row.createCell(2);
				if (record.getR26_note_holders() != null) {
				    cell2.setCellValue(record.getR26_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				 cell3 = row.createCell(3);
				if (record.getR26_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR26_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				 cell4 = row.createCell(4);
				if (record.getR26_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR26_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR26_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR26_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR26_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR26_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR26_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR26_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR26_amount() != null) {
				    cell8.setCellValue(record.getR26_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R27 ---
				cell2 = row.createCell(2);
				if (record.getR27_note_holders() != null) {
				    cell2.setCellValue(record.getR27_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				 cell3 = row.createCell(3);
				if (record.getR27_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR27_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				 cell4 = row.createCell(4);
				if (record.getR27_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR27_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				 cell5 = row.createCell(5);
				if (record.getR27_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR27_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR27_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR27_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR27_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR27_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR27_amount() != null) {
				    cell8.setCellValue(record.getR27_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				// --- R28 ---
				 cell2 = row.createCell(2);
				if (record.getR28_note_holders() != null) {
				    cell2.setCellValue(record.getR28_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR28_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR28_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR28_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR28_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				 cell5 = row.createCell(5);
				if (record.getR28_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR28_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				 cell6 = row.createCell(6);
				if (record.getR28_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR28_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				 cell7 = row.createCell(7);
				if (record.getR28_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR28_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				 cell8 = row.createCell(8);
				if (record.getR28_amount() != null) {
				    cell8.setCellValue(record.getR28_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}
				
				// --- R35 ---
				cell2 = row.createCell(2);
				if (record.getR35_note_holders() != null) {
				    cell2.setCellValue(record.getR35_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR35_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR35_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR35_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR35_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR35_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR35_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR35_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR35_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR35_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR35_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR35_amount() != null) {
				    cell8.setCellValue(record.getR35_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				Cell cell9 = row.createCell(9);
				if (record.getR35_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR35_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

									
				// --- R36 ---
				cell2 = row.createCell(2);
				if (record.getR36_note_holders() != null) {
				    cell2.setCellValue(record.getR36_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR36_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR36_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR36_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR36_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR36_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR36_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR36_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR36_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR36_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR36_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR36_amount() != null) {
				    cell8.setCellValue(record.getR36_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

			     cell9 = row.createCell(9);
				if (record.getR36_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR36_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

			
				// --- R37 ---
				cell2 = row.createCell(2);
				if (record.getR37_note_holders() != null) {
				    cell2.setCellValue(record.getR37_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR37_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR37_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR37_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR37_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR37_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR37_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR37_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR37_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR37_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR37_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR37_amount() != null) {
				    cell8.setCellValue(record.getR37_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

			    cell9 = row.createCell(9);
				if (record.getR37_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR37_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				

				// --- R38 ---
				cell2 = row.createCell(2);
				if (record.getR38_note_holders() != null) {
				    cell2.setCellValue(record.getR38_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR38_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR38_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR38_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR38_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR38_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR38_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR38_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR38_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR38_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR38_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR38_amount() != null) {
				    cell8.setCellValue(record.getR38_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR38_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR38_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

			
				// --- R39 ---
				cell2 = row.createCell(2);
				if (record.getR39_note_holders() != null) {
				    cell2.setCellValue(record.getR39_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR39_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR39_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR39_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR39_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR39_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR39_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR39_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR39_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR39_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR39_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR39_amount() != null) {
				    cell8.setCellValue(record.getR39_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record.getR39_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR39_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R40 ---
				cell2 = row.createCell(2);
				if (record.getR40_note_holders() != null) {
				    cell2.setCellValue(record.getR40_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR40_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR40_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR40_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR40_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR40_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR40_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR40_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR40_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR40_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR40_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR40_amount() != null) {
				    cell8.setCellValue(record.getR40_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR40_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR40_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				

				// --- R41 ---
				cell2 = row.createCell(2);
				if (record.getR41_note_holders() != null) {
				    cell2.setCellValue(record.getR41_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR41_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR41_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR41_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR41_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR41_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR41_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR41_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR41_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR41_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR41_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR41_amount() != null) {
				    cell8.setCellValue(record.getR41_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR41_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR41_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R42 ---
				cell2 = row.createCell(2);
				if (record.getR42_note_holders() != null) {
				    cell2.setCellValue(record.getR42_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR42_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR42_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR42_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR42_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR42_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR42_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR42_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR42_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR42_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR42_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR42_amount() != null) {
				    cell8.setCellValue(record.getR42_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR42_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR42_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R43 ---
				cell2 = row.createCell(2);
				if (record.getR43_note_holders() != null) {
				    cell2.setCellValue(record.getR43_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR43_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR43_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR43_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR43_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR43_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR43_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR43_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR43_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR43_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR43_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR43_amount() != null) {
				    cell8.setCellValue(record.getR43_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR43_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR43_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R44 ---
				cell2 = row.createCell(2);
				if (record.getR44_note_holders() != null) {
				    cell2.setCellValue(record.getR44_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR44_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR44_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR44_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR44_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR44_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR44_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR44_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR44_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR44_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR44_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR44_amount() != null) {
				    cell8.setCellValue(record.getR44_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR44_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR44_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R45 ---
				cell2 = row.createCell(2);
				if (record.getR45_note_holders() != null) {
				    cell2.setCellValue(record.getR45_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR45_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR45_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR45_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR45_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR45_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR45_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR45_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR45_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR45_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR45_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR45_amount() != null) {
				    cell8.setCellValue(record.getR45_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR45_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR45_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R46 ---
				cell2 = row.createCell(2);
				if (record.getR46_note_holders() != null) {
				    cell2.setCellValue(record.getR46_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR46_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR46_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR46_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR46_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR46_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR46_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR46_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR46_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR46_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR46_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR46_amount() != null) {
				    cell8.setCellValue(record.getR46_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR46_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR46_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R47 ---
				cell2 = row.createCell(2);
				if (record.getR47_note_holders() != null) {
				    cell2.setCellValue(record.getR47_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR47_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR47_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR47_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR47_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR47_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR47_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR47_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR47_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR47_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR47_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR47_amount() != null) {
				    cell8.setCellValue(record.getR47_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR47_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR47_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R48 ---
				cell2 = row.createCell(2);
				if (record.getR48_note_holders() != null) {
				    cell2.setCellValue(record.getR48_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR48_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR48_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR48_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR48_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR48_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR48_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR48_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR48_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR48_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR48_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR48_amount() != null) {
				    cell8.setCellValue(record.getR48_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR48_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR48_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R49 ---
				cell2 = row.createCell(2);
				if (record.getR49_note_holders() != null) {
				    cell2.setCellValue(record.getR49_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR49_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR49_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR49_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR49_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR49_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR49_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR49_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR49_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR49_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR49_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR49_amount() != null) {
				    cell8.setCellValue(record.getR49_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR49_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR49_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R50 ---
				cell2 = row.createCell(2);
				if (record.getR50_note_holders() != null) {
				    cell2.setCellValue(record.getR50_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR50_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR50_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR50_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR50_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR50_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR50_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR50_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR50_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR50_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR50_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR50_amount() != null) {
				    cell8.setCellValue(record.getR50_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR50_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR50_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R51 ---
				cell2 = row.createCell(2);
				if (record.getR51_note_holders() != null) {
				    cell2.setCellValue(record.getR51_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR51_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR51_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR51_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR51_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR51_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR51_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR51_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR51_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR51_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR51_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR51_amount() != null) {
				    cell8.setCellValue(record.getR51_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR51_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR51_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R52 ---
				cell2 = row.createCell(2);
				if (record.getR52_note_holders() != null) {
				    cell2.setCellValue(record.getR52_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR52_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR52_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR52_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR52_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR52_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR52_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR52_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR52_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR52_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR52_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR52_amount() != null) {
				    cell8.setCellValue(record.getR52_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR52_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR52_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				

				// --- R53 ---
				cell2 = row.createCell(2);
				if (record.getR53_note_holders() != null) {
				    cell2.setCellValue(record.getR53_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR53_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR53_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR53_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR53_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR53_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR53_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR53_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR53_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR53_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR53_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR53_amount() != null) {
				    cell8.setCellValue(record.getR53_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR53_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR53_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R54 ---
				cell2 = row.createCell(2);
				if (record.getR54_note_holders() != null) {
				    cell2.setCellValue(record.getR54_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR54_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR54_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR54_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR54_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR54_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR54_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR54_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR54_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR54_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR54_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR54_amount() != null) {
				    cell8.setCellValue(record.getR54_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR54_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR54_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				

				// --- R55 ---
				cell2 = row.createCell(2);
				if (record.getR55_note_holders() != null) {
				    cell2.setCellValue(record.getR55_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR55_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR55_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR55_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR55_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR55_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR55_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR55_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR55_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR55_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR55_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR55_amount() != null) {
				    cell8.setCellValue(record.getR55_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR55_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR55_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R56 ---
				cell2 = row.createCell(2);
				if (record.getR56_note_holders() != null) {
				    cell2.setCellValue(record.getR56_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR56_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR56_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR56_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR56_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR56_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR56_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR56_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR56_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR56_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR56_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR56_amount() != null) {
				    cell8.setCellValue(record.getR56_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR56_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR56_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R57 ---
				cell2 = row.createCell(2);
				if (record.getR57_note_holders() != null) {
				    cell2.setCellValue(record.getR57_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR57_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR57_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR57_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR57_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR57_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR57_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR57_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR57_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR57_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR57_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR57_amount() != null) {
				    cell8.setCellValue(record.getR57_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR57_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR57_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R58 ---
				cell2 = row.createCell(2);
				if (record.getR58_note_holders() != null) {
				    cell2.setCellValue(record.getR58_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR58_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR58_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR58_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR58_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR58_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR58_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR58_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR58_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR58_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR58_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR58_amount() != null) {
				    cell8.setCellValue(record.getR58_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record.getR58_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR58_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R59 ---
				cell2 = row.createCell(2);
				if (record.getR59_note_holders() != null) {
				    cell2.setCellValue(record.getR59_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR59_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR59_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR59_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR59_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR59_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR59_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR59_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR59_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR59_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR59_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR59_amount() != null) {
				    cell8.setCellValue(record.getR59_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR59_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR59_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R60 ---
				cell2 = row.createCell(2);
				if (record.getR60_note_holders() != null) {
				    cell2.setCellValue(record.getR60_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR60_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR60_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR60_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR60_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR60_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR60_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR60_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR60_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR60_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR60_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR60_amount() != null) {
				    cell8.setCellValue(record.getR60_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR60_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR60_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R61 ---
				cell2 = row.createCell(2);
				if (record.getR61_note_holders() != null) {
				    cell2.setCellValue(record.getR61_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR61_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR61_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR61_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR61_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR61_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR61_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR61_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR61_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR61_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR61_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR61_amount() != null) {
				    cell8.setCellValue(record.getR61_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record.getR61_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR61_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R62 ---
				cell2 = row.createCell(2);
				if (record.getR62_note_holders() != null) {
				    cell2.setCellValue(record.getR62_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR62_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR62_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR62_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR62_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR62_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR62_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR62_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR62_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR62_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR62_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR62_amount() != null) {
				    cell8.setCellValue(record.getR62_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR62_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR62_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				// --- R63 ---
				cell2 = row.createCell(2);
				if (record.getR63_note_holders() != null) {
				    cell2.setCellValue(record.getR63_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR63_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR63_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR63_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR63_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR63_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR63_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR63_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR63_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR63_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR63_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR63_amount() != null) {
				    cell8.setCellValue(record.getR63_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR63_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR63_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R64 ---
				cell2 = row.createCell(2);
				if (record.getR64_note_holders() != null) {
				    cell2.setCellValue(record.getR64_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR64_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR64_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR64_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR64_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR64_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR64_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR64_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR64_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR64_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR64_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR64_amount() != null) {
				    cell8.setCellValue(record.getR64_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR64_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR64_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R65 ---
				cell2 = row.createCell(2);
				if (record.getR65_note_holders() != null) {
				    cell2.setCellValue(record.getR65_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR65_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR65_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR65_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR65_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR65_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR65_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR65_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR65_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR65_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR65_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR65_amount() != null) {
				    cell8.setCellValue(record.getR65_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR65_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR65_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R66 ---
				cell2 = row.createCell(2);
				if (record.getR66_note_holders() != null) {
				    cell2.setCellValue(record.getR66_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR66_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR66_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR66_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR66_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR66_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR66_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR66_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR66_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR66_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR66_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR66_amount() != null) {
				    cell8.setCellValue(record.getR66_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR66_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR66_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R67 ---
				cell2 = row.createCell(2);
				if (record.getR67_note_holders() != null) {
				    cell2.setCellValue(record.getR67_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR67_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR67_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR67_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR67_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR67_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR67_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR67_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR67_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR67_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR67_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR67_amount() != null) {
				    cell8.setCellValue(record.getR67_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record.getR67_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR67_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R68 ---
				cell2 = row.createCell(2);
				if (record.getR68_note_holders() != null) {
				    cell2.setCellValue(record.getR68_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR68_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR68_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR68_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR68_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR68_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR68_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR68_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR68_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR68_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR68_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR68_amount() != null) {
				    cell8.setCellValue(record.getR68_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record.getR68_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR68_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R69 ---
				cell2 = row.createCell(2);
				if (record.getR69_note_holders() != null) {
				    cell2.setCellValue(record.getR69_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR69_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR69_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR69_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR69_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR69_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR69_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR69_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR69_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR69_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR69_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR69_amount() != null) {
				    cell8.setCellValue(record.getR69_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR69_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR69_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R70 ---
				cell2 = row.createCell(2);
				if (record.getR70_note_holders() != null) {
				    cell2.setCellValue(record.getR70_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR70_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR70_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR70_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR70_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR70_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR70_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR70_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR70_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR70_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR70_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR70_amount() != null) {
				    cell8.setCellValue(record.getR70_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record.getR70_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR70_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R71 ---
				cell2 = row.createCell(2);
				if (record.getR71_note_holders() != null) {
				    cell2.setCellValue(record.getR71_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR71_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR71_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR71_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR71_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR71_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR71_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR71_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR71_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR71_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR71_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR71_amount() != null) {
				    cell8.setCellValue(record.getR71_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record.getR71_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR71_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R72 ---
				cell2 = row.createCell(2);
				if (record.getR72_note_holders() != null) {
				    cell2.setCellValue(record.getR72_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR72_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR72_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR72_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR72_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR72_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR72_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR72_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR72_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR72_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR72_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR72_amount() != null) {
				    cell8.setCellValue(record.getR72_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR72_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR72_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R73 ---
				cell2 = row.createCell(2);
				if (record.getR73_note_holders() != null) {
				    cell2.setCellValue(record.getR73_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR73_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR73_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR73_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR73_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR73_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR73_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR73_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR73_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR73_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR73_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR73_amount() != null) {
				    cell8.setCellValue(record.getR73_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR73_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR73_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R74 ---
				cell2 = row.createCell(2);
				if (record.getR74_note_holders() != null) {
				    cell2.setCellValue(record.getR74_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR74_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR74_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR74_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR74_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR74_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR74_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR74_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR74_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR74_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR74_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR74_amount() != null) {
				    cell8.setCellValue(record.getR74_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR74_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR74_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R75 ---
				cell2 = row.createCell(2);
				if (record.getR75_note_holders() != null) {
				    cell2.setCellValue(record.getR75_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR75_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR75_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR75_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR75_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR75_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR75_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR75_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR75_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR75_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR75_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR75_amount() != null) {
				    cell8.setCellValue(record.getR75_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR75_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR75_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R76 ---
				cell2 = row.createCell(2);
				if (record.getR76_note_holders() != null) {
				    cell2.setCellValue(record.getR76_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR76_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR76_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR76_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR76_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR76_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR76_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR76_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR76_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR76_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR76_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR76_amount() != null) {
				    cell8.setCellValue(record.getR76_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR76_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR76_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R77 ---
				cell2 = row.createCell(2);
				if (record.getR77_note_holders() != null) {
				    cell2.setCellValue(record.getR77_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR77_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR77_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR77_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR77_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR77_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR77_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR77_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR77_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR77_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR77_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR77_amount() != null) {
				    cell8.setCellValue(record.getR77_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR77_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR77_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				// --- R78 ---
				cell2 = row.createCell(2);
				if (record.getR78_note_holders() != null) {
				    cell2.setCellValue(record.getR78_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR78_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR78_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR78_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR78_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR78_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR78_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR78_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR78_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR78_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR78_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR78_amount() != null) {
				    cell8.setCellValue(record.getR78_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR78_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR78_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R79 ---
				cell2 = row.createCell(2);
				if (record.getR79_note_holders() != null) {
				    cell2.setCellValue(record.getR79_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR79_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR79_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR79_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR79_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR79_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR79_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR79_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR79_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR79_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR79_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR79_amount() != null) {
				    cell8.setCellValue(record.getR79_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record.getR79_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR79_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R80 ---
				cell2 = row.createCell(2);
				if (record.getR80_note_holders() != null) {
				    cell2.setCellValue(record.getR80_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR80_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR80_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR80_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR80_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR80_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR80_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR80_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR80_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR80_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR80_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR80_amount() != null) {
				    cell8.setCellValue(record.getR80_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR80_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR80_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R81 ---
				cell2 = row.createCell(2);
				if (record.getR81_note_holders() != null) {
				    cell2.setCellValue(record.getR81_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR81_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR81_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR81_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR81_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR81_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR81_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR81_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR81_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR81_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR81_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR81_amount() != null) {
				    cell8.setCellValue(record.getR81_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR81_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR81_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R82 ---
				cell2 = row.createCell(2);
				if (record.getR82_note_holders() != null) {
				    cell2.setCellValue(record.getR82_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR82_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR82_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR82_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR82_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR82_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR82_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR82_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR82_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR82_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR82_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR82_amount() != null) {
				    cell8.setCellValue(record.getR82_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR82_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR82_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R83 ---
				cell2 = row.createCell(2);
				if (record.getR83_note_holders() != null) {
				    cell2.setCellValue(record.getR83_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR83_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR83_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR83_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR83_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR83_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR83_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR83_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR83_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR83_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR83_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR83_amount() != null) {
				    cell8.setCellValue(record.getR83_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR83_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR83_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				

				// --- R84 ---
				cell2 = row.createCell(2);
				if (record.getR84_note_holders() != null) {
				    cell2.setCellValue(record.getR84_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR84_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR84_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR84_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR84_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR84_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR84_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR84_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR84_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR84_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR84_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR84_amount() != null) {
				    cell8.setCellValue(record.getR84_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR84_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR84_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R85 ---
				cell2 = row.createCell(2);
				if (record.getR85_note_holders() != null) {
				    cell2.setCellValue(record.getR85_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR85_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR85_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR85_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR85_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR85_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR85_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR85_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR85_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR85_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR85_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR85_amount() != null) {
				    cell8.setCellValue(record.getR85_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR85_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR85_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R86 ---
				cell2 = row.createCell(2);
				if (record.getR86_note_holders() != null) {
				    cell2.setCellValue(record.getR86_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR86_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR86_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR86_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR86_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR86_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR86_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR86_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR86_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR86_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR86_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR86_amount() != null) {
				    cell8.setCellValue(record.getR86_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR86_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR86_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R87 ---
				cell2 = row.createCell(2);
				if (record.getR87_note_holders() != null) {
				    cell2.setCellValue(record.getR87_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR87_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR87_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR87_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR87_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR87_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR87_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR87_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR87_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR87_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR87_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR87_amount() != null) {
				    cell8.setCellValue(record.getR87_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR87_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR87_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				// --- R88 ---
				cell2 = row.createCell(2);
				if (record.getR88_note_holders() != null) {
				    cell2.setCellValue(record.getR88_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR88_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR88_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR88_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR88_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR88_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR88_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR88_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR88_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR88_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR88_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR88_amount() != null) {
				    cell8.setCellValue(record.getR88_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR88_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR88_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				 
				// --- R89 ---
				cell2 = row.createCell(2);
				if (record.getR89_note_holders() != null) {
				    cell2.setCellValue(record.getR89_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR89_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR89_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR89_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR89_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR89_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR89_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR89_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR89_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR89_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR89_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR89_amount() != null) {
				    cell8.setCellValue(record.getR89_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR89_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR89_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R90 ---
				cell2 = row.createCell(2);
				if (record.getR90_note_holders() != null) {
				    cell2.setCellValue(record.getR90_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR90_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR90_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR90_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR90_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR90_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR90_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR90_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR90_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR90_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR90_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR90_amount() != null) {
				    cell8.setCellValue(record.getR90_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR90_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR90_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R91 ---
				cell2 = row.createCell(2);
				if (record.getR91_note_holders() != null) {
				    cell2.setCellValue(record.getR91_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR91_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR91_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR91_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR91_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR91_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR91_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR91_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR91_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR91_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR91_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR91_amount() != null) {
				    cell8.setCellValue(record.getR91_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				cell9 = row.createCell(9);
				if (record.getR91_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR91_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R92 ---
				cell2 = row.createCell(2);
				if (record.getR92_note_holders() != null) {
				    cell2.setCellValue(record.getR92_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR92_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR92_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR92_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR92_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR92_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR92_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR92_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR92_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR92_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR92_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR92_amount() != null) {
				    cell8.setCellValue(record.getR92_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR92_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR92_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}



				// --- R93 ---
				cell2 = row.createCell(2);
				if (record.getR93_note_holders() != null) {
				    cell2.setCellValue(record.getR93_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR93_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR93_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR93_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR93_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR93_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR93_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR93_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR93_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR93_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR93_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR93_amount() != null) {
				    cell8.setCellValue(record.getR93_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR93_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR93_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R94 ---
				cell2 = row.createCell(2);
				if (record.getR94_note_holders() != null) {
				    cell2.setCellValue(record.getR94_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR94_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR94_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR94_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR94_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR94_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR94_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR94_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR94_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR94_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR94_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR94_amount() != null) {
				    cell8.setCellValue(record.getR94_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR94_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR94_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R95 ---
				cell2 = row.createCell(2);
				if (record.getR95_note_holders() != null) {
				    cell2.setCellValue(record.getR95_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR95_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR95_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR95_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR95_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR95_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR95_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR95_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR95_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR95_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR95_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR95_amount() != null) {
				    cell8.setCellValue(record.getR95_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR95_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR95_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R96 ---
				cell2 = row.createCell(2);
				if (record.getR96_note_holders() != null) {
				    cell2.setCellValue(record.getR96_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR96_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR96_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR96_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR96_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR96_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR96_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR96_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR96_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR96_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR96_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR96_amount() != null) {
				    cell8.setCellValue(record.getR96_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR96_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR96_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				// --- R97 ---
				cell2 = row.createCell(2);
				if (record.getR97_note_holders() != null) {
				    cell2.setCellValue(record.getR97_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR97_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR97_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR97_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR97_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR97_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR97_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR97_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR97_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR97_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR97_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR97_amount() != null) {
				    cell8.setCellValue(record.getR97_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR97_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR97_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R98 ---
				cell2 = row.createCell(2);
				if (record.getR98_note_holders() != null) {
				    cell2.setCellValue(record.getR98_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR98_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR98_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR98_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR98_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR98_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR98_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR98_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR98_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR98_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR98_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR98_amount() != null) {
				    cell8.setCellValue(record.getR98_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR98_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR98_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R99 ---
				cell2 = row.createCell(2);
				if (record.getR99_note_holders() != null) {
				    cell2.setCellValue(record.getR99_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR99_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR99_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR99_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR99_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR99_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR99_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR99_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR99_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR99_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR99_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR99_amount() != null) {
				    cell8.setCellValue(record.getR99_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR99_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR99_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}

				
				// --- R100 ---
				cell2 = row.createCell(2);
				if (record.getR100_note_holders() != null) {
				    cell2.setCellValue(record.getR100_note_holders());
				    cell2.setCellStyle(textStyle);
				} else {
				    cell2.setCellValue("");
				    cell2.setCellStyle(textStyle);
				}

				cell3 = row.createCell(3);
				if (record.getR100_name_of_instrument_programe() != null) {
				    cell3.setCellValue(record.getR100_name_of_instrument_programe());
				    cell3.setCellStyle(textStyle);
				} else {
				    cell3.setCellValue("");
				    cell3.setCellStyle(textStyle);
				}

				cell4 = row.createCell(4);
				if (record.getR100_issuing_entity_if_issued_throughan_spv() != null) {
				    cell4.setCellValue(record.getR100_issuing_entity_if_issued_throughan_spv());
				    cell4.setCellStyle(textStyle);
				} else {
				    cell4.setCellValue("");
				    cell4.setCellStyle(textStyle);
				}

				cell5 = row.createCell(5);
				if (record.getR100_issuance_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR100_issuance_date().toString());
				    cell5.setCellValue(utilDate);
				} else {
				    cell5.setCellValue("");
				    cell5.setCellStyle(textStyle);
				}

				cell6 = row.createCell(6);
				if (record.getR100_contractual_maturity_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR100_contractual_maturity_date().toString());
				    cell6.setCellValue(utilDate);
				} else {
				    cell6.setCellValue("");
				    cell6.setCellStyle(textStyle);
				}

				cell7 = row.createCell(7);
				if (record.getR100_effective_maturity_date_if_date() != null) {
				    Date utilDate = java.sql.Date.valueOf(record.getR100_effective_maturity_date_if_date().toString());
				    cell7.setCellValue(utilDate);
				} else {
				    cell7.setCellValue("");
				    cell7.setCellStyle(textStyle);
				}

				cell8 = row.createCell(8);
				if (record.getR100_amount() != null) {
				    cell8.setCellValue(record.getR100_amount().doubleValue());
				    cell8.setCellStyle(numberStyle);
				} else {
				    cell8.setCellValue("");
				    cell8.setCellStyle(textStyle);
				}

				 cell9 = row.createCell(9);
				if (record.getR100_amount_derecognised_p000() != null) {
				    cell9.setCellValue(record.getR100_amount_derecognised_p000().doubleValue());
				    cell9.setCellStyle(numberStyle);
				} else {
				    cell9.setCellValue("");
				    cell9.setCellStyle(textStyle);
				}
}
			
private void populateEntity2Data(Sheet sheet, M_CA5_Archival_Summary_Entity2 record1, CellStyle textStyle, CellStyle numberStyle) {
		    	    
				    // R0530 - ROW 61 (Index 60)
Row row = sheet.getRow(100) != null ? sheet.getRow(100) : sheet.createRow(100);
				    
				   
		
						// --- R101 ---
						Cell cell2 = row.createCell(2);
						if (record1.getR101_note_holders() != null) {
						    cell2.setCellValue(record1.getR101_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}	
						
						Cell cell3 = row.createCell(3);
						if (record1.getR101_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR101_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(4);
						if (record1.getR101_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR101_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						Cell cell5 = row.createCell(5);
						if (record1.getR101_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR101_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						Cell cell6 = row.createCell(6);
						if (record1.getR101_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR101_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						Cell cell7 = row.createCell(7);
						if (record1.getR101_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR101_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						Cell cell8 = row.createCell(8);
						if (record1.getR101_amount() != null) {
						    cell8.setCellValue(record1.getR101_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						Cell cell9 = row.createCell(9);
						if (record1.getR101_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR101_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

					
						// --- R102 ---
						cell2 = row.createCell(2);
						if (record1.getR102_note_holders() != null) {
						    cell2.setCellValue(record1.getR102_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR102_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR102_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR102_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR102_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR102_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR102_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR102_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR102_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR102_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR102_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR102_amount() != null) {
						    cell8.setCellValue(record1.getR102_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR102_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR102_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R103 ---
						cell2 = row.createCell(2);
						if (record1.getR103_note_holders() != null) {
						    cell2.setCellValue(record1.getR103_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR103_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR103_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR103_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR103_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR103_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR103_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR103_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR103_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR103_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR103_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR103_amount() != null) {
						    cell8.setCellValue(record1.getR103_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR103_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR103_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R104 ---
						cell2 = row.createCell(2);
						if (record1.getR104_note_holders() != null) {
						    cell2.setCellValue(record1.getR104_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR104_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR104_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR104_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR104_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR104_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR104_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR104_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR104_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR104_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR104_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR104_amount() != null) {
						    cell8.setCellValue(record1.getR104_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR104_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR104_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R105 ---
						cell2 = row.createCell(2);
						if (record1.getR105_note_holders() != null) {
						    cell2.setCellValue(record1.getR105_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR105_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR105_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR105_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR105_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR105_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR105_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR105_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR105_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR105_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR105_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR105_amount() != null) {
						    cell8.setCellValue(record1.getR105_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR105_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR105_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R106 ---
						cell2 = row.createCell(2);
						if (record1.getR106_note_holders() != null) {
						    cell2.setCellValue(record1.getR106_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR106_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR106_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR106_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR106_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR106_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR106_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR106_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR106_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR106_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR106_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR106_amount() != null) {
						    cell8.setCellValue(record1.getR106_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR106_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR106_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R107 ---
						cell2 = row.createCell(2);
						if (record1.getR107_note_holders() != null) {
						    cell2.setCellValue(record1.getR107_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR107_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR107_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR107_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR107_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR107_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR107_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR107_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR107_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR107_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR107_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR107_amount() != null) {
						    cell8.setCellValue(record1.getR107_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR107_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR107_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R108 ---
						cell2 = row.createCell(2);
						if (record1.getR108_note_holders() != null) {
						    cell2.setCellValue(record1.getR108_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR108_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR108_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR108_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR108_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR108_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR108_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR108_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR108_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR108_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR108_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR108_amount() != null) {
						    cell8.setCellValue(record1.getR108_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR108_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR108_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R109 ---
						cell2 = row.createCell(2);
						if (record1.getR109_note_holders() != null) {
						    cell2.setCellValue(record1.getR109_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR109_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR109_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR109_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR109_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR109_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR109_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR109_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR109_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR109_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR109_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR109_amount() != null) {
						    cell8.setCellValue(record1.getR109_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR109_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR109_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R110 ---
						cell2 = row.createCell(2);
						if (record1.getR110_note_holders() != null) {
						    cell2.setCellValue(record1.getR110_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR110_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR110_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR110_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR110_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR110_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR110_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR110_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR110_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR110_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR110_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR110_amount() != null) {
						    cell8.setCellValue(record1.getR110_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR110_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR110_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R111 ---
						cell2 = row.createCell(2);
						if (record1.getR111_note_holders() != null) {
						    cell2.setCellValue(record1.getR111_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR111_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR111_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR111_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR111_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR111_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR111_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR111_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR111_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR111_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR111_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR111_amount() != null) {
						    cell8.setCellValue(record1.getR111_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR111_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR111_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R112 ---
						cell2 = row.createCell(2);
						if (record1.getR112_note_holders() != null) {
						    cell2.setCellValue(record1.getR112_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR112_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR112_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR112_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR112_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR112_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR112_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR112_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR112_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR112_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR112_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR112_amount() != null) {
						    cell8.setCellValue(record1.getR112_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR112_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR112_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R113 ---
						cell2 = row.createCell(2);
						if (record1.getR113_note_holders() != null) {
						    cell2.setCellValue(record1.getR113_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR113_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR113_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR113_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR113_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR113_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR113_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR113_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR113_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR113_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR113_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR113_amount() != null) {
						    cell8.setCellValue(record1.getR113_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR113_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR113_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R114 ---
						cell2 = row.createCell(2);
						if (record1.getR114_note_holders() != null) {
						    cell2.setCellValue(record1.getR114_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR114_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR114_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR114_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR114_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR114_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR114_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR114_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR114_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR114_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR114_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR114_amount() != null) {
						    cell8.setCellValue(record1.getR114_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR114_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR114_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R115 ---
						cell2 = row.createCell(2);
						if (record1.getR115_note_holders() != null) {
						    cell2.setCellValue(record1.getR115_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR115_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR115_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR115_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR115_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR115_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR115_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR115_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR115_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR115_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR115_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR115_amount() != null) {
						    cell8.setCellValue(record1.getR115_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR115_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR115_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R116 ---
						cell2 = row.createCell(2);
						if (record1.getR116_note_holders() != null) {
						    cell2.setCellValue(record1.getR116_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR116_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR116_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR116_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR116_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR116_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR116_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR116_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR116_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR116_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR116_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR116_amount() != null) {
						    cell8.setCellValue(record1.getR116_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR116_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR116_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R117 ---
						cell2 = row.createCell(2);
						if (record1.getR117_note_holders() != null) {
						    cell2.setCellValue(record1.getR117_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR117_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR117_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR117_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR117_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR117_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR117_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR117_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR117_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR117_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR117_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR117_amount() != null) {
						    cell8.setCellValue(record1.getR117_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR117_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR117_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R118 ---
						cell2 = row.createCell(2);
						if (record1.getR118_note_holders() != null) {
						    cell2.setCellValue(record1.getR118_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR118_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR118_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR118_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR118_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR118_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR118_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR118_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR118_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR118_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR118_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR118_amount() != null) {
						    cell8.setCellValue(record1.getR118_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR118_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR118_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R119 ---
						cell2 = row.createCell(2);
						if (record1.getR119_note_holders() != null) {
						    cell2.setCellValue(record1.getR119_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR119_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR119_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR119_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR119_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR119_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR119_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR119_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR119_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR119_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR119_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR119_amount() != null) {
						    cell8.setCellValue(record1.getR119_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR119_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR119_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R120 ---
						cell2 = row.createCell(2);
						if (record1.getR120_note_holders() != null) {
						    cell2.setCellValue(record1.getR120_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR120_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR120_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR120_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR120_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR120_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR120_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR120_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR120_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR120_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR120_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR120_amount() != null) {
						    cell8.setCellValue(record1.getR120_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR120_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR120_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R121 ---
						cell2 = row.createCell(2);
						if (record1.getR121_note_holders() != null) {
						    cell2.setCellValue(record1.getR121_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR121_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR121_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR121_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR121_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR121_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR121_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR121_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR121_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR121_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR121_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR121_amount() != null) {
						    cell8.setCellValue(record1.getR121_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR121_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR121_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R122 ---
						cell2 = row.createCell(2);
						if (record1.getR122_note_holders() != null) {
						    cell2.setCellValue(record1.getR122_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR122_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR122_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR122_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR122_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR122_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR122_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR122_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR122_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR122_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR122_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR122_amount() != null) {
						    cell8.setCellValue(record1.getR122_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR122_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR122_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R123 ---
						cell2 = row.createCell(2);
						if (record1.getR123_note_holders() != null) {
						    cell2.setCellValue(record1.getR123_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR123_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR123_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR123_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR123_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR123_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR123_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR123_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR123_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR123_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR123_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR123_amount() != null) {
						    cell8.setCellValue(record1.getR123_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR123_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR123_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R124 ---
						cell2 = row.createCell(2);
						if (record1.getR124_note_holders() != null) {
						    cell2.setCellValue(record1.getR124_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR124_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR124_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR124_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR124_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR124_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR124_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR124_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR124_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR124_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR124_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR124_amount() != null) {
						    cell8.setCellValue(record1.getR124_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR124_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR124_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R125 ---
						cell2 = row.createCell(2);
						if (record1.getR125_note_holders() != null) {
						    cell2.setCellValue(record1.getR125_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR125_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR125_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR125_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR125_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR125_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR125_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR125_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR125_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR125_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR125_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR125_amount() != null) {
						    cell8.setCellValue(record1.getR125_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR125_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR125_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R126 ---
						cell2 = row.createCell(2);
						if (record1.getR126_note_holders() != null) {
						    cell2.setCellValue(record1.getR126_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR126_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR126_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR126_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR126_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR126_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR126_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR126_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR126_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR126_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR126_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR126_amount() != null) {
						    cell8.setCellValue(record1.getR126_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR126_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR126_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R127 ---
						cell2 = row.createCell(2);
						if (record1.getR127_note_holders() != null) {
						    cell2.setCellValue(record1.getR127_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR127_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR127_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR127_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR127_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR127_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR127_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR127_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR127_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR127_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR127_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR127_amount() != null) {
						    cell8.setCellValue(record1.getR127_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR127_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR127_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R128 ---
						cell2 = row.createCell(2);
						if (record1.getR128_note_holders() != null) {
						    cell2.setCellValue(record1.getR128_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR128_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR128_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR128_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR128_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR128_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR128_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR128_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR128_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR128_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR128_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR128_amount() != null) {
						    cell8.setCellValue(record1.getR128_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR128_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR128_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R129 ---
						cell2 = row.createCell(2);
						if (record1.getR129_note_holders() != null) {
						    cell2.setCellValue(record1.getR129_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR129_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR129_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR129_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR129_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR129_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR129_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR129_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR129_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR129_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR129_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR129_amount() != null) {
						    cell8.setCellValue(record1.getR129_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR129_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR129_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R130 ---
						cell2 = row.createCell(2);
						if (record1.getR130_note_holders() != null) {
						    cell2.setCellValue(record1.getR130_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR130_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR130_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR130_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR130_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR130_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR130_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR130_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR130_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR130_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR130_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR130_amount() != null) {
						    cell8.setCellValue(record1.getR130_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR130_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR130_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R131 ---
						cell2 = row.createCell(2);
						if (record1.getR131_note_holders() != null) {
						    cell2.setCellValue(record1.getR131_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR131_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR131_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR131_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR131_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR131_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR131_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR131_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR131_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR131_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR131_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR131_amount() != null) {
						    cell8.setCellValue(record1.getR131_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR131_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR131_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R132 ---
						cell2 = row.createCell(2);
						if (record1.getR132_note_holders() != null) {
						    cell2.setCellValue(record1.getR132_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR132_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR132_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR132_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR132_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR132_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR132_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR132_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR132_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR132_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR132_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR132_amount() != null) {
						    cell8.setCellValue(record1.getR132_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR132_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR132_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R133 ---
						cell2 = row.createCell(2);
						if (record1.getR133_note_holders() != null) {
						    cell2.setCellValue(record1.getR133_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR133_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR133_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR133_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR133_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR133_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR133_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR133_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR133_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR133_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR133_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR133_amount() != null) {
						    cell8.setCellValue(record1.getR133_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR133_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR133_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R134 ---
						cell2 = row.createCell(2);
						if (record1.getR134_note_holders() != null) {
						    cell2.setCellValue(record1.getR134_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR134_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR134_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR134_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR134_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR134_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR134_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR134_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR134_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR134_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR134_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR134_amount() != null) {
						    cell8.setCellValue(record1.getR134_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR134_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR134_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R135 ---
						cell2 = row.createCell(2);
						if (record1.getR135_note_holders() != null) {
						    cell2.setCellValue(record1.getR135_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR135_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR135_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR135_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR135_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR135_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR135_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR135_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR135_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR135_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR135_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR135_amount() != null) {
						    cell8.setCellValue(record1.getR135_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR135_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR135_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R136 ---
						cell2 = row.createCell(2);
						if (record1.getR136_note_holders() != null) {
						    cell2.setCellValue(record1.getR136_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR136_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR136_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR136_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR136_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR136_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR136_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR136_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR136_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR136_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR136_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR136_amount() != null) {
						    cell8.setCellValue(record1.getR136_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR136_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR136_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R137 ---
						cell2 = row.createCell(2);
						if (record1.getR137_note_holders() != null) {
						    cell2.setCellValue(record1.getR137_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR137_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR137_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR137_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR137_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR137_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR137_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR137_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR137_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR137_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR137_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR137_amount() != null) {
						    cell8.setCellValue(record1.getR137_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR137_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR137_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R138 ---
						cell2 = row.createCell(2);
						if (record1.getR138_note_holders() != null) {
						    cell2.setCellValue(record1.getR138_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR138_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR138_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR138_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR138_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR138_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR138_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR138_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR138_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR138_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR138_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR138_amount() != null) {
						    cell8.setCellValue(record1.getR138_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR138_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR138_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R139 ---
						cell2 = row.createCell(2);
						if (record1.getR139_note_holders() != null) {
						    cell2.setCellValue(record1.getR139_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR139_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR139_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR139_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR139_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR139_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR139_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR139_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR139_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR139_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR139_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR139_amount() != null) {
						    cell8.setCellValue(record1.getR139_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR139_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR139_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R140 ---
						cell2 = row.createCell(2);
						if (record1.getR140_note_holders() != null) {
						    cell2.setCellValue(record1.getR140_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR140_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR140_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR140_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR140_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR140_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR140_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR140_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR140_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR140_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR140_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR140_amount() != null) {
						    cell8.setCellValue(record1.getR140_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR140_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR140_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R141 ---
						cell2 = row.createCell(2);
						if (record1.getR141_note_holders() != null) {
						    cell2.setCellValue(record1.getR141_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR141_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR141_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR141_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR141_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR141_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR141_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR141_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR141_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR141_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR141_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR141_amount() != null) {
						    cell8.setCellValue(record1.getR141_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR141_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR141_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R142 ---
						cell2 = row.createCell(2);
						if (record1.getR142_note_holders() != null) {
						    cell2.setCellValue(record1.getR142_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR142_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR142_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR142_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR142_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR142_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR142_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR142_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR142_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR142_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR142_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR142_amount() != null) {
						    cell8.setCellValue(record1.getR142_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR142_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR142_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R143 ---
						cell2 = row.createCell(2);
						if (record1.getR143_note_holders() != null) {
						    cell2.setCellValue(record1.getR143_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR143_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR143_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR143_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR143_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR143_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR143_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR143_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR143_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR143_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR143_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR143_amount() != null) {
						    cell8.setCellValue(record1.getR143_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR143_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR143_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R144 ---
						cell2 = row.createCell(2);
						if (record1.getR144_note_holders() != null) {
						    cell2.setCellValue(record1.getR144_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR144_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR144_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR144_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR144_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR144_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR144_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR144_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR144_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR144_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR144_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR144_amount() != null) {
						    cell8.setCellValue(record1.getR144_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR144_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR144_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R145 ---
						cell2 = row.createCell(2);
						if (record1.getR145_note_holders() != null) {
						    cell2.setCellValue(record1.getR145_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR145_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR145_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR145_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR145_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR145_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR145_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR145_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR145_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR145_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR145_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR145_amount() != null) {
						    cell8.setCellValue(record1.getR145_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR145_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR145_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R146 ---
						cell2 = row.createCell(2);
						if (record1.getR146_note_holders() != null) {
						    cell2.setCellValue(record1.getR146_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR146_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR146_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR146_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR146_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR146_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR146_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR146_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR146_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR146_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR146_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR146_amount() != null) {
						    cell8.setCellValue(record1.getR146_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR146_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR146_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						// --- R147 ---
						cell2 = row.createCell(2);
						if (record1.getR147_note_holders() != null) {
						    cell2.setCellValue(record1.getR147_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR147_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR147_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR147_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR147_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR147_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR147_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR147_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR147_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR147_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR147_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR147_amount() != null) {
						    cell8.setCellValue(record1.getR147_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR147_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR147_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R148 ---
						cell2 = row.createCell(2);
						if (record1.getR148_note_holders() != null) {
						    cell2.setCellValue(record1.getR148_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR148_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR148_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR148_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR148_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR148_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR148_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR148_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR148_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR148_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR148_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR148_amount() != null) {
						    cell8.setCellValue(record1.getR148_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR148_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR148_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R149 ---
						cell2 = row.createCell(2);
						if (record1.getR149_note_holders() != null) {
						    cell2.setCellValue(record1.getR149_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR149_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR149_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR149_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR149_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR149_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR149_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR149_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR149_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR149_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR149_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR149_amount() != null) {
						    cell8.setCellValue(record1.getR149_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR149_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR149_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}

						
						// --- R150 ---
						cell2 = row.createCell(2);
						if (record1.getR150_note_holders() != null) {
						    cell2.setCellValue(record1.getR150_note_holders());
						    cell2.setCellStyle(textStyle);
						} else {
						    cell2.setCellValue("");
						    cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record1.getR150_name_of_instrument_programe() != null) {
						    cell3.setCellValue(record1.getR150_name_of_instrument_programe());
						    cell3.setCellStyle(textStyle);
						} else {
						    cell3.setCellValue("");
						    cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record1.getR150_issuing_entity_if_issued_throughan_spv() != null) {
						    cell4.setCellValue(record1.getR150_issuing_entity_if_issued_throughan_spv());
						    cell4.setCellStyle(textStyle);
						} else {
						    cell4.setCellValue("");
						    cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record1.getR150_issuance_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR150_issuance_date().toString());
						    cell5.setCellValue(utilDate);
						} else {
						    cell5.setCellValue("");
						    cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record1.getR150_contractual_maturity_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR150_contractual_maturity_date().toString());
						    cell6.setCellValue(utilDate);
						} else {
						    cell6.setCellValue("");
						    cell6.setCellStyle(textStyle);
						}

						cell7 = row.createCell(7);
						if (record1.getR150_effective_maturity_date_if_date() != null) {
						    Date utilDate = java.sql.Date.valueOf(record1.getR150_effective_maturity_date_if_date().toString());
						    cell7.setCellValue(utilDate);
						} else {
						    cell7.setCellValue("");
						    cell7.setCellStyle(textStyle);
						}

						cell8 = row.createCell(8);
						if (record1.getR150_amount() != null) {
						    cell8.setCellValue(record1.getR150_amount().doubleValue());
						    cell8.setCellStyle(numberStyle);
						} else {
						    cell8.setCellValue("");
						    cell8.setCellStyle(textStyle);
						}

						cell9 = row.createCell(9);
						if (record1.getR150_amount_derecognised_p000() != null) {
						    cell9.setCellValue(record1.getR150_amount_derecognised_p000().doubleValue());
						    cell9.setCellStyle(numberStyle);
						} else {
						    cell9.setCellValue("");
						    cell9.setCellStyle(textStyle);
						}
				
						
					}

public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
		String dtltype, String type, String version) {
	try {
		logger.info("Generating Excel for BRRS_M_CA5 ARCHIVAL Details...");
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
		List<M_CA5_Archival_Detail_Entity> reportData = M_CA5_Archival_Detail_Repo.getdatabydateList(parsedToDate,
				version);

		if (reportData != null && !reportData.isEmpty()) {
			int rowIndex = 1;
			for (M_CA5_Archival_Detail_Entity item : reportData) {
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
			logger.info("No data found for BRRS_M_CA5 — only header will be written.");
		}

		// Write to byte[]
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();

		logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
		return bos.toByteArray();

	} catch (Exception e) {
		logger.error("Error generating BRRS_M_CA5Excel", e);
		return new byte[0];
	}
}




			}

							








