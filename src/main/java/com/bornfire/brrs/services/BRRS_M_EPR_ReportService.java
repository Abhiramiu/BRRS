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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;


import com.bornfire.brrs.entities.BRRS_M_EPR_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_EPR_Summary_Repo;
import com.bornfire.brrs.entities.M_EPR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_EPR_Summary_Entity;

@Component
@Service

public class BRRS_M_EPR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_EPR_ReportService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	/*
	 * @Autowired BRRS_M_EPR_Detail_Repo brrs_m_epr_detail_repo;
	 */
	@Autowired
	BRRS_M_EPR_Summary_Repo brrs_m_epr_summary_repo;
	
	/*
	 * @Autowired BRRS_M_EPR_Archival_Detail_Repo m_epr_Archival_Detail_Repo;
	 */

	@Autowired
	BRRS_M_EPR_Archival_Summary_Repo m_epr_Archival_Summary_Repo;
	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_EPRView(String reportId, String fromdate, String todate, String currency, String dtltype,
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
			List<M_EPR_Archival_Summary_Entity> T1Master = new ArrayList<M_EPR_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();

				T1Master = m_epr_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
		List<M_EPR_Summary_Entity> T1Master = new ArrayList<M_EPR_Summary_Entity>();
		try {
			Date d1 = dateformat.parse(todate);
			// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

			// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
			// ", BRF1_REPORT_ENTITY.class)
			// .setParameter(1, df.parse(todate)).getResultList();
			
			
			T1Master = brrs_m_epr_summary_repo.getdatabydateList(dateformat.parse(todate));
			mv.addObject("report_date", dateformat.format(d1));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		mv.addObject("reportsummary", T1Master);
		}
		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_EPR");

		
		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	

	public byte[] getM_EPRExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		
		

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_EPRARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<M_EPR_Summary_Entity> dataList = brrs_m_epr_summary_repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_EPR report. Returning empty result.");
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
					M_EPR_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column B
					Cell cellB = row.createCell(1);
					if (record.getR11_market() != null) {
						cellB.setCellValue(record.getR11_market().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row11
					// Column C
					Cell cellC = row.createCell(2);
					if (record.getR11_gpfsr_nom_amt() != null) {
						cellC.setCellValue(record.getR11_gpfsr_nom_amt().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D
					Cell cellD = row.createCell(3);
					if (record.getR11_gpfsr_pos_att8_per_spe_ris() != null) {
						cellD.setCellValue(record.getR11_gpfsr_pos_att8_per_spe_ris().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR11_gpfsr_nom_amt1() != null) {
						cellF.setCellValue(record.getR11_gpfsr_nom_amt1().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR11_gpfsr_pos_att4_per_spe_ris() != null) {
						cellG.setCellValue(record.getR11_gpfsr_pos_att4_per_spe_ris().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR11_gpfsr_nom_amt2() != null) {
						cellI.setCellValue(record.getR11_gpfsr_nom_amt2().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR11_gpfsr_pos_att2_per_spe_ris() != null) {
						cellJ.setCellValue(record.getR11_gpfsr_pos_att2_per_spe_ris().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR11_net_pos_gen_mar_ris() != null) {
						cellM.setCellValue(record.getR11_net_pos_gen_mar_ris().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}
					
					// row12
					row = sheet.getRow(11);
					
					// row12
					// Column B  ->Market
					 cellB = row.createCell(1);
					if (record.getR12_market() != null) {
						cellB.setCellValue(record.getR12_market().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
					
					
					// row12
					// Column C -->Nominal Amount
					 cellC = row.createCell(2);
					if (record.getR12_gpfsr_nom_amt() != null) {
						cellC.setCellValue(record.getR12_gpfsr_nom_amt().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row12
					// Column D -->Positions Attracting 8 Percent Specific Risk
					 cellD = row.createCell(3);
					if (record.getR12_gpfsr_pos_att8_per_spe_ris() != null) {
						cellD.setCellValue(record.getR12_gpfsr_pos_att8_per_spe_ris().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row12
					// Column F -->Nominal Amount
					 cellF = row.createCell(5);
					if (record.getR12_gpfsr_nom_amt1() != null) {
						cellF.setCellValue(record.getR12_gpfsr_nom_amt1().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row12
					// Column G -->Positions Attracting 4 Percent Specific Risk
					 cellG = row.createCell(6);
					if (record.getR12_gpfsr_pos_att4_per_spe_ris() != null) {
						cellG.setCellValue(record.getR12_gpfsr_pos_att4_per_spe_ris().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row12
					// Column I -->Nominal Amount
					 cellI = row.createCell(8);
					if (record.getR12_gpfsr_nom_amt2() != null) {
						cellI.setCellValue(record.getR12_gpfsr_nom_amt2().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row12
					// Column J -->Positions Attracting 2 Percent Specific Risk
				       cellJ = row.createCell(9);
					if (record.getR12_gpfsr_pos_att2_per_spe_ris() != null) {
						cellJ.setCellValue(record.getR12_gpfsr_pos_att2_per_spe_ris().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row12
					// Column M -->Net Positions for General Market Risk
					 cellM = row.createCell(12);
					if (record.getR12_net_pos_gen_mar_ris() != null) {
						cellM.setCellValue(record.getR12_net_pos_gen_mar_ris().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}
					
					
					// ---- row13 ----
					row = sheet.getRow(12);

					// row13
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR13_market() != null) {
					    cellB.setCellValue(record.getR13_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row13
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR13_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR13_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row13
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR13_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR13_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row13
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR13_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR13_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row13
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR13_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR13_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row13
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR13_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR13_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row13
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR13_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR13_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row13
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR13_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR13_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row14 ----
					row = sheet.getRow(13);

					// row14
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR14_market() != null) {
					    cellB.setCellValue(record.getR14_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row14
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR14_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR14_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row14
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR14_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR14_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row14
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR14_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR14_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row14
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR14_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR14_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row14
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR14_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR14_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row14
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR14_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR14_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row14
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR14_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR14_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row15 ----
					row = sheet.getRow(14);

					// row15
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR15_market() != null) {
					    cellB.setCellValue(record.getR15_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row15
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR15_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR15_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row15
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR15_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR15_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row15
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR15_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR15_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row15
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR15_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR15_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row15
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR15_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR15_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row15
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR15_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR15_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row15
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR15_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR15_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row16 ----
					row = sheet.getRow(15);

					// row16
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR16_market() != null) {
					    cellB.setCellValue(record.getR16_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row16
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR16_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR16_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row16
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR16_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR16_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row16
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR16_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR16_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row16
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR16_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR16_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row16
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR16_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR16_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row16
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR16_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR16_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row16
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR16_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR16_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row17 ----
					row = sheet.getRow(16);

					// row17
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR17_market() != null) {
					    cellB.setCellValue(record.getR17_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row17
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR17_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR17_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row17
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR17_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR17_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row17
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR17_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR17_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row17
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR17_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR17_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row17
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR17_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR17_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row17
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR17_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR17_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row17
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR17_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR17_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row18 ----
					row = sheet.getRow(17);

					// row18
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR18_market() != null) {
					    cellB.setCellValue(record.getR18_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row18
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR18_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR18_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row18
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR18_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR18_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row18
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR18_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR18_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row18
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR18_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR18_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row18
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR18_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR18_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row18
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR18_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR18_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row18
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR18_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR18_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row19 ----
					row = sheet.getRow(18);

					// row19
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR19_market() != null) {
					    cellB.setCellValue(record.getR19_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row19
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR19_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR19_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row19
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR19_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR19_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row19
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR19_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR19_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row19
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR19_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR19_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row19
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR19_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR19_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row19
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR19_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR19_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row19
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR19_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR19_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row20 ----
					row = sheet.getRow(19);

					// row20
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR20_market() != null) {
					    cellB.setCellValue(record.getR20_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row20
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR20_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR20_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row20
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR20_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR20_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row20
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR20_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR20_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row20
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR20_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR20_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row20
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR20_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR20_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row20
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR20_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR20_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row20
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR20_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR20_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row21 ----
					row = sheet.getRow(20);

					// row21
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR21_market() != null) {
					    cellB.setCellValue(record.getR21_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row21
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR21_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR21_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row21
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR21_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR21_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row21
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR21_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR21_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row21
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR21_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR21_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row21
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR21_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR21_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row21
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR21_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR21_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row21
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR21_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR21_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row22 ----
					row = sheet.getRow(21);

					// row22
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR22_market() != null) {
					    cellB.setCellValue(record.getR22_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row22
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR22_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR22_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row22
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR22_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR22_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row22
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR22_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR22_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row22
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR22_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR22_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row22
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR22_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR22_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row22
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR22_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR22_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row22
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR22_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR22_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row23 ----
					row = sheet.getRow(22);

					
					// row23
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR23_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR23_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row23
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR23_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR23_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row23
					// Column E -->Charge
				Cell cellE = row.createCell(4);
					if (record.getR23_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR23_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// row23
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR23_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR23_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row23
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR23_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR23_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}
					
					// row23
					// Column H -->Charge
				Cell cellH = row.createCell(7);
					if (record.getR23_gpfsr_chrg1() != null) {
					    cellH.setCellValue(record.getR23_gpfsr_chrg1().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}

					// row23
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR23_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR23_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row23
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR23_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR23_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}
					
					// row23
					// Column K -->Charge
				Cell cellK = row.createCell(10);
					if (record.getR23_gpfsr_chrg2() != null) {
					    cellK.setCellValue(record.getR23_gpfsr_chrg2().doubleValue());
					    cellK.setCellStyle(numberStyle);
					} else {
					    cellK.setCellValue("");
					    cellK.setCellStyle(textStyle);
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
	 * public byte[] getM_EPRDetailExcel(String filename, String fromdate, String
	 * todate, String currency, String dtltype, String type, String version){ try {
	 * logger.info("Generating Excel for M_EPR Details...");
	 * System.out.println("came to Detail download service");
	 * 
	 * if (type.equals("ARCHIVAL") & version != null) { byte[] ARCHIVALreport =
	 * getM_EPRDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
	 * type, version); return ARCHIVALreport; }
	 * 
	 * XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("M_EPRDetails");
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
	 * SimpleDateFormat("dd/MM/yyyy").parse(todate); List<M_EPR_Detail_Entity>
	 * reportData = brrs_m_epr_detail_repo.getdatabydateList(parsedToDate);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_EPR_Detail_Entity item : reportData) { XSSFRow row =
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
	 * logger.info("No data found for M_EPR — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating M_EPR Excel", e);
	 * return new byte[0]; } }
	 */

	public List<Object> getM_EPRArchival() {
		List<Object> M_EPRArchivallist = new ArrayList<>();
		try {
			M_EPRArchivallist = m_epr_Archival_Summary_Repo.getM_EPRarchival();
			System.out.println("countser" + M_EPRArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_EPR Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_EPRArchivallist;
	}	
	
	
	public byte[] getExcelM_EPRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_EPR_Archival_Summary_Entity> dataList = m_epr_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_EPR report. Returning empty result.");
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
					M_EPR_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					
					// row11
					// Column B
					Cell cellB = row.createCell(1);
					if (record.getR11_market() != null) {
						cellB.setCellValue(record.getR11_market().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row11
					// Column C
					Cell cellC = row.createCell(2);
					if (record.getR11_gpfsr_nom_amt() != null) {
						cellC.setCellValue(record.getR11_gpfsr_nom_amt().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D
					Cell cellD = row.createCell(3);
					if (record.getR11_gpfsr_pos_att8_per_spe_ris() != null) {
						cellD.setCellValue(record.getR11_gpfsr_pos_att8_per_spe_ris().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cellF = row.createCell(5);
					if (record.getR11_gpfsr_nom_amt1() != null) {
						cellF.setCellValue(record.getR11_gpfsr_nom_amt1().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row11
					// Column G
					Cell cellG = row.createCell(6);
					if (record.getR11_gpfsr_pos_att4_per_spe_ris() != null) {
						cellG.setCellValue(record.getR11_gpfsr_pos_att4_per_spe_ris().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row11
					// Column I
					Cell cellI = row.createCell(8);
					if (record.getR11_gpfsr_nom_amt2() != null) {
						cellI.setCellValue(record.getR11_gpfsr_nom_amt2().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row11
					// Column J
					Cell cellJ = row.createCell(9);
					if (record.getR11_gpfsr_pos_att2_per_spe_ris() != null) {
						cellJ.setCellValue(record.getR11_gpfsr_pos_att2_per_spe_ris().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row11
					// Column M
					Cell cellM = row.createCell(12);
					if (record.getR11_net_pos_gen_mar_ris() != null) {
						cellM.setCellValue(record.getR11_net_pos_gen_mar_ris().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}
					
					// row12
					row = sheet.getRow(11);
					
					// row12
					// Column B  ->Market
					 cellB = row.createCell(1);
					if (record.getR12_market() != null) {
						cellB.setCellValue(record.getR12_market().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
					
					
					// row12
					// Column C -->Nominal Amount
					 cellC = row.createCell(2);
					if (record.getR12_gpfsr_nom_amt() != null) {
						cellC.setCellValue(record.getR12_gpfsr_nom_amt().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row12
					// Column D -->Positions Attracting 8 Percent Specific Risk
					 cellD = row.createCell(3);
					if (record.getR12_gpfsr_pos_att8_per_spe_ris() != null) {
						cellD.setCellValue(record.getR12_gpfsr_pos_att8_per_spe_ris().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row12
					// Column F -->Nominal Amount
					 cellF = row.createCell(5);
					if (record.getR12_gpfsr_nom_amt1() != null) {
						cellF.setCellValue(record.getR12_gpfsr_nom_amt1().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// row12
					// Column G -->Positions Attracting 4 Percent Specific Risk
					 cellG = row.createCell(6);
					if (record.getR12_gpfsr_pos_att4_per_spe_ris() != null) {
						cellG.setCellValue(record.getR12_gpfsr_pos_att4_per_spe_ris().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// row12
					// Column I -->Nominal Amount
					 cellI = row.createCell(8);
					if (record.getR12_gpfsr_nom_amt2() != null) {
						cellI.setCellValue(record.getR12_gpfsr_nom_amt2().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// row12
					// Column J -->Positions Attracting 2 Percent Specific Risk
				       cellJ = row.createCell(9);
					if (record.getR12_gpfsr_pos_att2_per_spe_ris() != null) {
						cellJ.setCellValue(record.getR12_gpfsr_pos_att2_per_spe_ris().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// row12
					// Column M -->Net Positions for General Market Risk
					 cellM = row.createCell(12);
					if (record.getR12_net_pos_gen_mar_ris() != null) {
						cellM.setCellValue(record.getR12_net_pos_gen_mar_ris().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}
					
					
					// ---- row13 ----
					row = sheet.getRow(12);

					// row13
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR13_market() != null) {
					    cellB.setCellValue(record.getR13_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row13
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR13_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR13_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row13
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR13_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR13_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row13
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR13_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR13_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row13
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR13_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR13_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row13
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR13_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR13_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row13
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR13_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR13_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row13
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR13_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR13_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row14 ----
					row = sheet.getRow(13);

					// row14
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR14_market() != null) {
					    cellB.setCellValue(record.getR14_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row14
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR14_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR14_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row14
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR14_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR14_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row14
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR14_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR14_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row14
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR14_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR14_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row14
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR14_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR14_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row14
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR14_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR14_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row14
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR14_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR14_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row15 ----
					row = sheet.getRow(14);

					// row15
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR15_market() != null) {
					    cellB.setCellValue(record.getR15_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row15
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR15_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR15_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row15
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR15_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR15_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row15
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR15_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR15_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row15
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR15_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR15_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row15
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR15_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR15_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row15
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR15_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR15_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row15
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR15_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR15_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row16 ----
					row = sheet.getRow(15);

					// row16
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR16_market() != null) {
					    cellB.setCellValue(record.getR16_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row16
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR16_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR16_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row16
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR16_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR16_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row16
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR16_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR16_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row16
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR16_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR16_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row16
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR16_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR16_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row16
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR16_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR16_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row16
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR16_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR16_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row17 ----
					row = sheet.getRow(16);

					// row17
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR17_market() != null) {
					    cellB.setCellValue(record.getR17_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row17
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR17_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR17_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row17
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR17_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR17_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row17
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR17_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR17_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row17
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR17_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR17_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row17
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR17_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR17_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row17
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR17_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR17_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row17
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR17_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR17_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row18 ----
					row = sheet.getRow(17);

					// row18
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR18_market() != null) {
					    cellB.setCellValue(record.getR18_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row18
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR18_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR18_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row18
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR18_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR18_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row18
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR18_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR18_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row18
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR18_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR18_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row18
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR18_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR18_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row18
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR18_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR18_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row18
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR18_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR18_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row19 ----
					row = sheet.getRow(18);

					// row19
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR19_market() != null) {
					    cellB.setCellValue(record.getR19_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row19
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR19_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR19_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row19
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR19_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR19_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row19
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR19_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR19_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row19
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR19_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR19_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row19
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR19_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR19_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row19
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR19_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR19_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row19
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR19_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR19_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row20 ----
					row = sheet.getRow(19);

					// row20
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR20_market() != null) {
					    cellB.setCellValue(record.getR20_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row20
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR20_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR20_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row20
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR20_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR20_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row20
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR20_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR20_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row20
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR20_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR20_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row20
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR20_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR20_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row20
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR20_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR20_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row20
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR20_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR20_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row21 ----
					row = sheet.getRow(20);

					// row21
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR21_market() != null) {
					    cellB.setCellValue(record.getR21_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row21
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR21_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR21_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row21
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR21_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR21_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row21
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR21_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR21_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row21
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR21_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR21_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row21
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR21_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR21_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row21
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR21_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR21_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row21
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR21_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR21_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row22 ----
					row = sheet.getRow(21);

					// row22
					// Column B -->Market
					cellB = row.createCell(1);
					if (record.getR22_market() != null) {
					    cellB.setCellValue(record.getR22_market().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// row22
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR22_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR22_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row22
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR22_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR22_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// row22
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR22_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR22_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row22
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR22_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR22_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}

					// row22
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR22_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR22_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row22
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR22_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR22_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}

					// row22
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR22_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR22_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}

					// ---- row23 ----
					row = sheet.getRow(22);

					
					// row23
					// Column C -->Nominal Amount
					cellC = row.createCell(2);
					if (record.getR23_gpfsr_nom_amt() != null) {
					    cellC.setCellValue(record.getR23_gpfsr_nom_amt().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// row23
					// Column D -->Positions Attracting 8 Percent Specific Risk
					cellD = row.createCell(3);
					if (record.getR23_gpfsr_pos_att8_per_spe_ris() != null) {
					    cellD.setCellValue(record.getR23_gpfsr_pos_att8_per_spe_ris().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// row23
					// Column E -->Charge
				Cell cellE = row.createCell(4);
					if (record.getR23_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR23_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// row23
					// Column F -->Nominal Amount
					cellF = row.createCell(5);
					if (record.getR23_gpfsr_nom_amt1() != null) {
					    cellF.setCellValue(record.getR23_gpfsr_nom_amt1().doubleValue());
					    cellF.setCellStyle(numberStyle);
					} else {
					    cellF.setCellValue("");
					    cellF.setCellStyle(textStyle);
					}

					// row23
					// Column G -->Positions Attracting 4 Percent Specific Risk
					cellG = row.createCell(6);
					if (record.getR23_gpfsr_pos_att4_per_spe_ris() != null) {
					    cellG.setCellValue(record.getR23_gpfsr_pos_att4_per_spe_ris().doubleValue());
					    cellG.setCellStyle(numberStyle);
					} else {
					    cellG.setCellValue("");
					    cellG.setCellStyle(textStyle);
					}
					
					// row23
					// Column H -->Charge
				Cell cellH = row.createCell(7);
					if (record.getR23_gpfsr_chrg1() != null) {
					    cellH.setCellValue(record.getR23_gpfsr_chrg1().doubleValue());
					    cellH.setCellStyle(numberStyle);
					} else {
					    cellH.setCellValue("");
					    cellH.setCellStyle(textStyle);
					}

					// row23
					// Column I -->Nominal Amount
					cellI = row.createCell(8);
					if (record.getR23_gpfsr_nom_amt2() != null) {
					    cellI.setCellValue(record.getR23_gpfsr_nom_amt2().doubleValue());
					    cellI.setCellStyle(numberStyle);
					} else {
					    cellI.setCellValue("");
					    cellI.setCellStyle(textStyle);
					}

					// row23
					// Column J -->Positions Attracting 2 Percent Specific Risk
					cellJ = row.createCell(9);
					if (record.getR23_gpfsr_pos_att2_per_spe_ris() != null) {
					    cellJ.setCellValue(record.getR23_gpfsr_pos_att2_per_spe_ris().doubleValue());
					    cellJ.setCellStyle(numberStyle);
					} else {
					    cellJ.setCellValue("");
					    cellJ.setCellStyle(textStyle);
					}
					
					// row23
					// Column K -->Charge
				Cell cellK = row.createCell(10);
					if (record.getR23_gpfsr_chrg2() != null) {
					    cellK.setCellValue(record.getR23_gpfsr_chrg2().doubleValue());
					    cellK.setCellStyle(numberStyle);
					} else {
					    cellK.setCellValue("");
					    cellK.setCellStyle(textStyle);
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
	 * public byte[] getM_EPRDetailExcelARCHIVAL(String filename, String fromdate,
	 * String todate, String currency, String dtltype, String type, String version)
	 * { try { logger.info("Generating Excel for M_EPR ARCHIVAL Details...");
	 * System.out.println("came to Detail download service"); if
	 * (type.equals("ARCHIVAL") & version != null) {
	 * 
	 * } XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("M_EPRDetail");
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
	 * List<M_EPR_Archival_Detail_Entity> reportData =
	 * m_epr_Archival_Detail_Repo.getdatabydateList(parsedToDate, version);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_EPR_Archival_Detail_Entity item : reportData) { XSSFRow row =
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
	 * logger.info("No data found for M_EPR — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating M_EPRExcel", e);
	 * return new byte[0]; } }
	 */
	
	
	
	
	public void updateReport(M_EPR_Summary_Entity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_EPR_Summary_Entity existing = brrs_m_epr_summary_repo.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // 1️⃣ Loop from R11 to R23 and copy fields
	    	
	        for (int i = 11; i <= 23; i++) {
				
	        	 String prefix = "R" + i + "_";

	            String[] fields = {"market","gpfsr_nom_amt","gpfsr_pos_att8_per_spe_ris","gpfsr_chrg","gpfsr_nom_amt1","gpfsr_pos_att4_per_spe_ris","gpfsr_chrg1", 
	                	"gpfsr_nom_amt2","gpfsr_pos_att2_per_spe_ris","gpfsr_chrg2","tot_spe_ris_chrg","net_pos_gen_mar_ris","gen_mar_ris_chrg_8per","2per_gen_mar_ris_chrg_div_port",
	                	"tot_gen_mar_risk_chrg","tot_mar_ris_chrg" };

	            for (String field : fields) {
	            	   String getterName = "get" + prefix + field; // e.g., getR10
	                   String setterName = "set" + prefix + field; // e.g., setR10

	                try {
	                    Method getter = M_EPR_Summary_Entity.class.getMethod(getterName);
	                    Method setter = M_EPR_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

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
	    brrs_m_epr_summary_repo.save(existing);
	}

  
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}