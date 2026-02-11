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

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_EPR_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_EPR_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_EPR_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_EPR_RESUB_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_EPR_RESUB_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_EPR_Summary_Repo;
import com.bornfire.brrs.entities.M_EPR_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_EPR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_EPR_Detail_Entity;
import com.bornfire.brrs.entities.M_EPR_RESUB_Detail_Entity;
import com.bornfire.brrs.entities.M_EPR_RESUB_Summary_Entity;
import com.bornfire.brrs.entities.M_EPR_Summary_Entity;

@Component
@Service

public class BRRS_M_EPR_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_EPR_ReportService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;
	
	

	
	

	
	  @Autowired BRRS_M_EPR_Detail_Repo brrs_m_epr_detail_repo;
	 
	@Autowired
	BRRS_M_EPR_Summary_Repo brrs_m_epr_summary_repo;
	
	
	  @Autowired BRRS_M_EPR_Archival_Detail_Repo m_epr_Archival_Detail_Repo;
	 

	@Autowired
	BRRS_M_EPR_Archival_Summary_Repo m_epr_Archival_Summary_Repo;
	
	
	@Autowired
	BRRS_M_EPR_RESUB_Summary_Repo brrs_m_epr_resub_summary_repo;
	
    @Autowired
	BRRS_M_EPR_RESUB_Detail_Repo brrs_m_epr_resub_detail_repo;
	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_EPRView(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    int pageSize = pageable.getPageSize();
	    int currentPage = pageable.getPageNumber();
	    int startItem = currentPage * pageSize;

	    try {

	        // Parse only once
	        Date d1 = dateformat.parse(todate);

	        System.out.println("======= VIEW DEBUG =======");
	        System.out.println("TYPE      : " + type);
	        System.out.println("DTLTYPE   : " + dtltype);
	        System.out.println("DATE      : " + d1);
	        System.out.println("VERSION   : " + version);
	        System.out.println("==========================");

	        // ===========================================================
	        //SUMMARY SECTION
	        // ===========================================================

	        // ---------- ARCHIVAL SUMMARY ----------
	        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

	            List<M_EPR_Archival_Summary_Entity> T1Master =
	                    m_epr_Archival_Summary_Repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Archival Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- RESUB SUMMARY ----------
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_EPR_RESUB_Summary_Entity> T1Master =
	                    brrs_m_epr_resub_summary_repo
	                            .getdatabydateListarchival(d1, version);

	            System.out.println("Resub Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "resub");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- NORMAL SUMMARY ----------
	        else {

	            List<M_EPR_Summary_Entity> T1Master =
	                    brrs_m_epr_summary_repo
	                            .getdatabydateList(d1);

	            System.out.println("Normal Summary Size : " + T1Master.size());

	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ===========================================================
	        // DETAIL SECTION
	        // ===========================================================

	        if ("detail".equalsIgnoreCase(dtltype)) {

	            // ---------- ARCHIVAL DETAIL ----------
	            if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

	                List<M_EPR_Archival_Detail_Entity> T1Master =
	                        m_epr_Archival_Detail_Repo
	                                .getdatabydateListarchival(d1, version);

	                System.out.println("Archival Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- RESUB DETAIL ----------
	            else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	                List<M_EPR_RESUB_Detail_Entity> T1Master =
	                        brrs_m_epr_resub_detail_repo
	                                .getdatabydateListarchival(d1, version);

	                System.out.println("Resub Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "resub");
	                mv.addObject("reportsummary", T1Master);
	            }

	            // ---------- NORMAL DETAIL ----------
	            else {

	            	 List<M_EPR_Detail_Entity> T1Master =
		                        brrs_m_epr_detail_repo
		                                .getdatabydateList(d1);

	                System.out.println("Normal Detail Size : " + T1Master.size());

	                mv.addObject("displaymode", "detail");
	                mv.addObject("reportsummary", T1Master);
	            }
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_EPR");

	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}

	 

	public byte[] getM_EPRExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		
		
		// ARCHIVAL check
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
            logger.info("Service: Generating ARCHIVAL report for version {}", version);
            return getExcelM_EPRARCHIVAL(filename, reportId, fromdate, todate,
                    currency, dtltype, type, version);
        }
     // Resub check
     		if ("RESUB".equalsIgnoreCase(type) && version != null) {
     			logger.info("Service: Generating resub report for version {}", version);
     			return BRRS_M_EPRResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
     		}
        // Email check
         if ("email".equalsIgnoreCase(type)  && version == null) {
            logger.info("Service: Generating Email report for version {}", version);
            return BRRS_M_EPREmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
        }
         
         else if("email".equalsIgnoreCase(type) && version != null) {
 			logger.info("Service: Generating Email1 report for version {}", version);
 			return BRRS_M_EPREmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
 					version);
 		}else if ("email".equalsIgnoreCase(type) && version != null) {
            logger.info("Service: Generating Email report for version {}", version);
            return BRRS_M_EPRARCHIVALEmailExcel(filename, reportId, fromdate, todate,
                    currency, dtltype, type, version);
        }
        

	




	    /* ===================== NORMAL ===================== */
	    List<M_EPR_Summary_Entity> dataList =
	            brrs_m_epr_summary_repo.getdatabydateList(dateformat.parse(todate));

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
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}
	}

	
	
	//Archival View
	public List<Object[]> getM_EPRArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_EPR_Archival_Summary_Entity> repoData = m_epr_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_EPR_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReport_date(), 
							entity.getReport_version() 
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_EPR_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_EPR  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}
	
	
	public byte[] getExcelM_EPRARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

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

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}
	
	
	
	public byte[] BRRS_M_EPREmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		

		List<M_EPR_Summary_Entity> dataList = brrs_m_epr_summary_repo
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_EPR_email report. Returning empty result.");
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
					// Column E -->Charge
					Cell	cellE = row.createCell(4);
					if (record.getR11_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR11_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR11_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR11_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					Cell cellK = row.createCell(10);
					if (record.getR11_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR11_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
						// row11
					// Column N -->General Market Risk Change at 8%

					Cell cellN = row.createCell(13);
					if (record.getR11_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR11_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					Cell 	cellO = row.createCell(14);
					if (record.getR11_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR11_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					Cell cellP = row.createCell(15);
					if (record.getR11_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR11_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR12_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR12_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
				 cellH = row.createCell(7);
					if (record.getR12_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR12_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR12_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR12_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
						// row12
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR12_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR12_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR12_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR12_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR12_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR12_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR13_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR13_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR13_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR13_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR13_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR13_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
						// row13
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR13_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR13_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR13_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR13_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR13_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR13_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR14_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR14_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR14_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR14_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR14_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR14_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
						// row14
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR14_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR14_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR14_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR14_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR14_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR14_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR15_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR15_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					
					
						// row15
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR15_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR15_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR15_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR15_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR15_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR15_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR16_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR16_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					
					
					// row16
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR16_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR16_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR16_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR16_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR16_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR16_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR17_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR17_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR17_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR17_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR17_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR17_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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

					
					// row17
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR17_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR17_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR17_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR17_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR17_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR17_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR18_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR18_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR18_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR18_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR18_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR18_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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

					// row18
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR18_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR18_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR18_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR18_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR18_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR18_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR19_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR19_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR19_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR19_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR19_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR19_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
					// row19
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR19_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR19_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR19_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR19_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR19_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR19_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR20_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR20_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR20_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR20_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR20_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR20_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
					
					// row20
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR20_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR20_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR20_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR20_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR20_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR20_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR21_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR21_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR21_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR21_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR21_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR21_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
					
					
							
					
					
						// row21
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR21_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR21_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR21_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR21_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR21_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR21_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR22_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR22_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR22_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR22_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR22_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR22_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
					
						// row22
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR22_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR22_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR22_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR22_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}

					
					
					
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR22_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR22_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
				 cellE = row.createCell(4);
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
				 cellH = row.createCell(7);
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
				 cellK = row.createCell(10);
					if (record.getR23_gpfsr_chrg2() != null) {
					    cellK.setCellValue(record.getR23_gpfsr_chrg2().doubleValue());
					    cellK.setCellStyle(numberStyle);
					} else {
					    cellK.setCellValue("");
					    cellK.setCellStyle(textStyle);
					}
					
					
					
					// row23
					// Column L --> Total Specific Risk Charge

			Cell cellL = row.createCell(11);
					if (record.getR23_tot_spe_ris_chrg() != null) {
					    cellL.setCellValue(record.getR23_tot_spe_ris_chrg().doubleValue());
					    cellL.setCellStyle(numberStyle);
					} else {
					    cellL.setCellValue("");
					    cellL.setCellStyle(textStyle);
					}
					
					// row23
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR23_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR23_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR23_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR23_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}

					
					
					
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR23_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR23_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
					}
					
					
						// Column Q --> Total Market Risk Change

					Cell cellQ = row.createCell(16);
					if (record.getR23_tot_mar_ris_chrg() != null) {
					    cellQ.setCellValue(record.getR23_tot_mar_ris_chrg().doubleValue());
					    cellQ.setCellStyle(numberStyle);
					} else {
					    cellQ.setCellValue("");
					    cellQ.setCellStyle(textStyle);
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
	
	
	
	
	public byte[] BRRS_M_EPRARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<M_EPR_Archival_Summary_Entity> dataList = m_epr_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_EPR_email_ARCHIVAL report. Returning empty result.");
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
					// Column E -->Charge
					Cell	cellE = row.createCell(4);
					if (record.getR11_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR11_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					Cell cellH = row.createCell(7);
					if (record.getR11_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR11_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					Cell cellK = row.createCell(10);
					if (record.getR11_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR11_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
						// row11
					// Column N -->General Market Risk Change at 8%

					Cell cellN = row.createCell(13);
					if (record.getR11_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR11_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					Cell 	cellO = row.createCell(14);
					if (record.getR11_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR11_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					Cell cellP = row.createCell(15);
					if (record.getR11_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR11_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR12_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR12_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
				 cellH = row.createCell(7);
					if (record.getR12_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR12_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR12_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR12_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
						// row12
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR12_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR12_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR12_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR12_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR12_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR12_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR13_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR13_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR13_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR13_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR13_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR13_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
						// row13
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR13_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR13_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR13_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR13_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR13_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR13_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR14_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR14_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR14_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR14_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR14_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR14_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
						// row14
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR14_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR14_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR14_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR14_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR14_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR14_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR15_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR15_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					
					
						// row15
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR15_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR15_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR15_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR15_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR15_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR15_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR16_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR16_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					
					
					// row16
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR16_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR16_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR16_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR16_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR16_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR16_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR17_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR17_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR17_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR17_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR17_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR17_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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

					
					// row17
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR17_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR17_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR17_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR17_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR17_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR17_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR18_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR18_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR18_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR18_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR18_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR18_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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

					// row18
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR18_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR18_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR18_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR18_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR18_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR18_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR19_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR19_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR19_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR19_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR19_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR19_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
					// row19
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR19_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR19_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR19_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR19_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR19_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR19_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR20_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR20_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR20_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR20_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR20_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR20_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
					
					// row20
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR20_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR20_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR20_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR20_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR20_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR20_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR21_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR21_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR21_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR21_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR21_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR21_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
					
					
							
					
					
						// row21
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR21_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR21_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR21_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR21_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}
	
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR21_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR21_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
					// Column E -->Charge
					cellE = row.createCell(4);
					if (record.getR22_gpfsr_chrg() != null) {
					    cellE.setCellValue(record.getR22_gpfsr_chrg().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
					// Column H
					 cellH = row.createCell(7);
					if (record.getR22_gpfsr_chrg1() != null) { 
						cellH.setCellValue(record.getR22_gpfsr_chrg1().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
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
					// Column K ---------Charge

					 cellK = row.createCell(10);
					if (record.getR22_gpfsr_chrg2() != null) {
						cellK.setCellValue(record.getR22_gpfsr_chrg2().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
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
					
					
						// row22
					// Column N -->General Market Risk Change at 8%

					cellN = row.createCell(13);
					if (record.getR22_gen_mar_ris_chrg_8per() != null) {
					    cellN.setCellValue(record.getR22_gen_mar_ris_chrg_8per().doubleValue());
					    cellN.setCellStyle(numberStyle);
					} else {
					    cellN.setCellValue("");
					    cellN.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR22_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR22_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}

					
					
					
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR22_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR22_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
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
				 cellE = row.createCell(4);
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
				 cellH = row.createCell(7);
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
				 cellK = row.createCell(10);
					if (record.getR23_gpfsr_chrg2() != null) {
					    cellK.setCellValue(record.getR23_gpfsr_chrg2().doubleValue());
					    cellK.setCellStyle(numberStyle);
					} else {
					    cellK.setCellValue("");
					    cellK.setCellStyle(textStyle);
					}
					
					
					
					// row23
					// Column L --> Total Specific Risk Charge

			Cell cellL = row.createCell(11);
					if (record.getR23_tot_spe_ris_chrg() != null) {
					    cellL.setCellValue(record.getR23_tot_spe_ris_chrg().doubleValue());
					    cellL.setCellStyle(numberStyle);
					} else {
					    cellL.setCellValue("");
					    cellL.setCellStyle(textStyle);
					}
					
					// row23
					// Column M -->Net Positions for General Market Risk
					cellM = row.createCell(12);
					if (record.getR23_net_pos_gen_mar_ris() != null) {
					    cellM.setCellValue(record.getR23_net_pos_gen_mar_ris().doubleValue());
					    cellM.setCellStyle(numberStyle);
					} else {
					    cellM.setCellValue("");
					    cellM.setCellStyle(textStyle);
					}
					
					// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

					cellO = row.createCell(14);
					if (record.getR23_2per_gen_mar_ris_chrg_div_port() != null) {
					    cellO.setCellValue(record.getR23_2per_gen_mar_ris_chrg_div_port().doubleValue());
					    cellO.setCellStyle(numberStyle);
					} else {
					    cellO.setCellValue("");
					    cellO.setCellStyle(textStyle);
					}

					
					
					
						// Column P -->Total General Market Risk Charge


					cellP = row.createCell(15);
					if (record.getR23_tot_gen_mar_risk_chrg() != null) {
					    cellP.setCellValue(record.getR23_tot_gen_mar_risk_chrg().doubleValue());
					    cellP.setCellStyle(numberStyle);
					} else {
					    cellP.setCellValue("");
					    cellP.setCellStyle(textStyle);
					}
					
					
						// Column Q --> Total Market Risk Change

					Cell cellQ = row.createCell(16);
					if (record.getR23_tot_mar_ris_chrg() != null) {
					    cellQ.setCellValue(record.getR23_tot_mar_ris_chrg().doubleValue());
					    cellQ.setCellStyle(numberStyle);
					} else {
					    cellQ.setCellValue("");
					    cellQ.setCellStyle(textStyle);
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
	
	
	@Transactional
	public void updateReport(M_EPR_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    // 1️⃣ Fetch existing SUMMARY
	    M_EPR_Summary_Entity existingSummary =
	            brrs_m_epr_summary_repo.findById(updatedEntity.getReport_date())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    // 2️⃣ Fetch or create DETAIL
	    M_EPR_Detail_Entity existingDetail =
	            brrs_m_epr_detail_repo.findById(updatedEntity.getReport_date())
	                    .orElseGet(() -> {
	                        M_EPR_Detail_Entity d = new M_EPR_Detail_Entity();
	                        d.setReport_date(updatedEntity.getReport_date());
	                        return d;
	                    });

	    try {

	        // 🔁 Loop R11 → R23
	        for (int i = 11; i <= 23; i++) {

	            String prefix = "R" + i + "_";

	            String[] fields = {
	                "market",
	                "gpfsr_nom_amt",
	                "gpfsr_pos_att8_per_spe_ris",
	                "gpfsr_chrg",
	                "gpfsr_nom_amt1",
	                "gpfsr_pos_att4_per_spe_ris",
	                "gpfsr_chrg1",
	                "gpfsr_nom_amt2",
	                "gpfsr_pos_att2_per_spe_ris",
	                "gpfsr_chrg2",
	                "tot_spe_ris_chrg",
	                "net_pos_gen_mar_ris",
	                "gen_mar_ris_chrg_8per",
	                "2per_gen_mar_ris_chrg_div_port",
	                "tot_gen_mar_risk_chrg",
	                "tot_mar_ris_chrg"
	            };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter =
	                            M_EPR_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter =
	                            M_EPR_Summary_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Method detailSetter =
	                            M_EPR_Detail_Entity.class.getMethod(
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
	    brrs_m_epr_summary_repo.save(existingSummary);
	    brrs_m_epr_detail_repo.save(existingDetail);
	}


  
	

	public List<Object[]> getM_EPRResub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<M_EPR_Archival_Summary_Entity> latestArchivalList =
	                m_epr_Archival_Summary_Repo.getdatabydateListWithVersion();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (M_EPR_Archival_Summary_Entity entity : latestArchivalList) {
	                resubList.add(new Object[] {
	                    entity.getReport_date(),
	                    entity.getReport_version(),
	                    entity.getReportResubDate()
	                });
	            }
	            System.out.println("Fetched " + resubList.size() + " record(s)");
	        } else {
	            System.out.println("No archival data found.");
	        }

	    } catch (Exception e) {
	        System.err.println("Error fetching M_EPR Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}

	
	
	/// Downloaded for Archival & Resub
		public byte[] BRRS_M_EPRResubExcel(String filename, String reportId, String fromdate,
	        String todate, String currency, String dtltype,
	        String type, BigDecimal version) throws Exception {

	    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

	    if (type.equals("RESUB") & version != null) {
	       
	    }

	    List<M_EPR_RESUB_Summary_Entity> dataList =
	    		brrs_m_epr_resub_summary_repo.getdatabydateListarchival(dateformat.parse(todate), version);

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

						M_EPR_RESUB_Summary_Entity  record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
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
					workbook.setForceFormulaRecalculation(true);
				} else {

				}

				// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

				return out.toByteArray();
			}

		}
		
		
		@Transactional
	    public void updateResubReport(M_EPR_RESUB_Summary_Entity updatedEntity) {

	        Date reportDate = updatedEntity.getReport_date();

	        // ----------------------------------------------------
	        // GET CURRENT VERSION FROM RESUB TABLE
	        // ----------------------------------------------------

	        BigDecimal maxResubVer =
	            brrs_m_epr_resub_summary_repo.findMaxVersion(reportDate);

	        if (maxResubVer == null)
	            throw new RuntimeException("No record for: " + reportDate);

	        BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

	        Date now = new Date();

	        // ====================================================
	        // 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
	        // ====================================================

	        M_EPR_RESUB_Summary_Entity resubSummary =
	            new M_EPR_RESUB_Summary_Entity();

	        BeanUtils.copyProperties(updatedEntity, resubSummary,
	            "reportDate", "reportVersion", "reportResubDate");

	        resubSummary.setReport_date(reportDate);
	        resubSummary.setReport_version(newVersion);
	        resubSummary.setReportResubDate(now);

	        // ====================================================
	        // 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
	        // ====================================================

	        M_EPR_RESUB_Detail_Entity resubDetail =
	            new M_EPR_RESUB_Detail_Entity();

	        BeanUtils.copyProperties(updatedEntity, resubDetail,
	            "reportDate", "reportVersion", "reportResubDate");

	        resubDetail.setReport_date(reportDate);
	        resubDetail.setReport_version(newVersion);
	        resubDetail.setReportResubDate(now);

	        // ====================================================
	        // 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
	        // ====================================================

	        M_EPR_Archival_Summary_Entity archSummary =
	            new M_EPR_Archival_Summary_Entity();

	        BeanUtils.copyProperties(updatedEntity, archSummary,
	            "reportDate", "reportVersion", "reportResubDate");

	        archSummary.setReport_date(reportDate);
	        archSummary.setReport_version(newVersion);   // SAME VERSION
	        archSummary.setReportResubDate(now);

	        // ====================================================
	        // 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
	        // ====================================================

	        M_EPR_Archival_Detail_Entity archDetail =
	            new M_EPR_Archival_Detail_Entity();

	        BeanUtils.copyProperties(updatedEntity, archDetail,
	            "reportDate", "reportVersion", "reportResubDate");

	        archDetail.setReport_date(reportDate);
	        archDetail.setReport_version(newVersion);    // SAME VERSION
	        archDetail.setReportResubDate(now);

	        // ====================================================
	        // 6️⃣ SAVE ALL WITH SAME DATA
	        // ====================================================

	        brrs_m_epr_resub_summary_repo.save(resubSummary);
	        brrs_m_epr_resub_detail_repo.save(resubDetail);

	        m_epr_Archival_Summary_Repo.save(archSummary);
	        m_epr_Archival_Detail_Repo.save(archDetail);
	    }

		
		//Resub Email Format
		//Archival download for email
		public byte[] BRRS_M_EPREmailResubExcel(String filename, String reportId, String fromdate, String todate,
	            String currency,
	            String dtltype, String type, BigDecimal version) throws Exception {
	        logger.info("Service: Starting Excel generation process in memory.");
	        Date reportDate = dateformat.parse(todate);
	        if (type.equals("RESUB") & version != null) {

	        }
	        		List<M_EPR_RESUB_Summary_Entity> dataList = brrs_m_epr_resub_summary_repo
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

						M_EPR_RESUB_Summary_Entity record = dataList.get(i);
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
						// Column E -->Charge
						Cell	cellE = row.createCell(4);
						if (record.getR11_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR11_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
						// Column H
						Cell cellH = row.createCell(7);
						if (record.getR11_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR11_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
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
						// Column K ---------Charge

						Cell cellK = row.createCell(10);
						if (record.getR11_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR11_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
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
						
							// row11
						// Column N -->General Market Risk Change at 8%

						Cell cellN = row.createCell(13);
						if (record.getR11_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR11_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						Cell 	cellO = row.createCell(14);
						if (record.getR11_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR11_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						Cell cellP = row.createCell(15);
						if (record.getR11_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR11_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
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
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR12_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR12_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
						// Column H
					 cellH = row.createCell(7);
						if (record.getR12_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR12_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
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
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR12_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR12_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
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
						
							// row12
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR12_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR12_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR12_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR12_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR12_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR12_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
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
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR13_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR13_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
						// Column H
						 cellH = row.createCell(7);
						if (record.getR13_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR13_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
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
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR13_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR13_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
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
						
							// row13
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR13_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR13_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR13_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR13_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR13_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR13_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
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
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR14_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR14_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
						// Column H
						 cellH = row.createCell(7);
						if (record.getR14_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR14_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
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
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR14_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR14_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
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
						
							// row14
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR14_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR14_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR14_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR14_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR14_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR14_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
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
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR15_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR15_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
						
						
							// row15
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR15_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR15_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR15_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR15_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR15_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR15_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
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
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR16_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR16_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
						
						
						// row16
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR16_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR16_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR16_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR16_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR16_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR16_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
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
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR17_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR17_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
						// Column H
						 cellH = row.createCell(7);
						if (record.getR17_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR17_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
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
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR17_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR17_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
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

						
						// row17
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR17_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR17_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR17_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR17_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR17_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR17_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
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
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR18_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR18_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
						// Column H
						 cellH = row.createCell(7);
						if (record.getR18_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR18_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
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
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR18_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR18_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
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

						// row18
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR18_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR18_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR18_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR18_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR18_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR18_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
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
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR19_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR19_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
						// Column H
						 cellH = row.createCell(7);
						if (record.getR19_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR19_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
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
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR19_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR19_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
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
						
						// row19
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR19_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR19_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR19_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR19_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR19_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR19_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
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
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR20_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR20_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
						// Column H
						 cellH = row.createCell(7);
						if (record.getR20_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR20_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
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
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR20_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR20_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
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
						
						
						// row20
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR20_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR20_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR20_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR20_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR20_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR20_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
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
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR21_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR21_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
						// Column H
						 cellH = row.createCell(7);
						if (record.getR21_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR21_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
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
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR21_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR21_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
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
						
						
						
								
						
						
							// row21
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR21_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR21_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR21_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR21_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}
		
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR21_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR21_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
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
						// Column E -->Charge
						cellE = row.createCell(4);
						if (record.getR22_gpfsr_chrg() != null) {
						    cellE.setCellValue(record.getR22_gpfsr_chrg().doubleValue());
						    cellE.setCellStyle(numberStyle);
						} else {
						    cellE.setCellValue("");
						    cellE.setCellStyle(textStyle);
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
						// Column H
						 cellH = row.createCell(7);
						if (record.getR22_gpfsr_chrg1() != null) { 
							cellH.setCellValue(record.getR22_gpfsr_chrg1().doubleValue());
							cellH.setCellStyle(numberStyle);
						} else {
							cellH.setCellValue("");
							cellH.setCellStyle(textStyle);
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
						// Column K ---------Charge

						 cellK = row.createCell(10);
						if (record.getR22_gpfsr_chrg2() != null) {
							cellK.setCellValue(record.getR22_gpfsr_chrg2().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
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
						
						
							// row22
						// Column N -->General Market Risk Change at 8%

						cellN = row.createCell(13);
						if (record.getR22_gen_mar_ris_chrg_8per() != null) {
						    cellN.setCellValue(record.getR22_gen_mar_ris_chrg_8per().doubleValue());
						    cellN.setCellStyle(numberStyle);
						} else {
						    cellN.setCellValue("");
						    cellN.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR22_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR22_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}

						
						
						
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR22_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR22_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
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
					 cellE = row.createCell(4);
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
					 cellH = row.createCell(7);
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
					 cellK = row.createCell(10);
						if (record.getR23_gpfsr_chrg2() != null) {
						    cellK.setCellValue(record.getR23_gpfsr_chrg2().doubleValue());
						    cellK.setCellStyle(numberStyle);
						} else {
						    cellK.setCellValue("");
						    cellK.setCellStyle(textStyle);
						}
						
						
						
						// row23
						// Column L --> Total Specific Risk Charge

				Cell cellL = row.createCell(11);
						if (record.getR23_tot_spe_ris_chrg() != null) {
						    cellL.setCellValue(record.getR23_tot_spe_ris_chrg().doubleValue());
						    cellL.setCellStyle(numberStyle);
						} else {
						    cellL.setCellValue("");
						    cellL.setCellStyle(textStyle);
						}
						
						// row23
						// Column M -->Net Positions for General Market Risk
						cellM = row.createCell(12);
						if (record.getR23_net_pos_gen_mar_ris() != null) {
						    cellM.setCellValue(record.getR23_net_pos_gen_mar_ris().doubleValue());
						    cellM.setCellStyle(numberStyle);
						} else {
						    cellM.setCellValue("");
						    cellM.setCellStyle(textStyle);
						}
						
						// Column o -->2 Percent General Market Risk Change for well-diversified Portfolio

						cellO = row.createCell(14);
						if (record.getR23_2per_gen_mar_ris_chrg_div_port() != null) {
						    cellO.setCellValue(record.getR23_2per_gen_mar_ris_chrg_div_port().doubleValue());
						    cellO.setCellStyle(numberStyle);
						} else {
						    cellO.setCellValue("");
						    cellO.setCellStyle(textStyle);
						}

						
						
						
							// Column P -->Total General Market Risk Charge


						cellP = row.createCell(15);
						if (record.getR23_tot_gen_mar_risk_chrg() != null) {
						    cellP.setCellValue(record.getR23_tot_gen_mar_risk_chrg().doubleValue());
						    cellP.setCellStyle(numberStyle);
						} else {
						    cellP.setCellValue("");
						    cellP.setCellStyle(textStyle);
						}
						
						
							// Column Q --> Total Market Risk Change

						Cell cellQ = row.createCell(16);
						if (record.getR23_tot_mar_ris_chrg() != null) {
						    cellQ.setCellValue(record.getR23_tot_mar_ris_chrg().doubleValue());
						    cellQ.setCellStyle(numberStyle);
						} else {
						    cellQ.setCellValue("");
						    cellQ.setCellStyle(textStyle);
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