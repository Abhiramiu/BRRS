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


import com.bornfire.brrs.entities.M_CA3_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_CA3_Summary_Repo;
import com.bornfire.brrs.entities.M_CA3_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_CA3_Detail_Repo;


@Component
@Service

public class BRRS_M_CA3_ReportService {
private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA3_ReportService.class);
	


	@Autowired
	private Environment env;
	
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	BRRS_M_CA3_Detail_Repo BRRS_M_CA3_detail_Repo;
	
	@Autowired
	BRRS_M_CA3_Summary_Repo BRRS_M_CA3_Summary_Repo;
   

	
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	public ModelAndView getM_CA3View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;	

		List<M_CA3_Summary_Entity> T1Master = new ArrayList<M_CA3_Summary_Entity>();
		try {
			Date d1 = dateformat.parse(todate);
			// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

			//T1Master = hs.createQuery("from  BRF1_REPORT_ENTITY a where a.report_date = ?1 ", BRF1_REPORT_ENTITY.class)
				//	.setParameter(1, df.parse(todate)).getResultList();
			T1Master = BRRS_M_CA3_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRRS/M_CA3");
		
		mv.addObject("reportsummary", T1Master);
		//mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		//mv.addObject("reportsflag", "reportsflag");
		//mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}
	
	
	public ModelAndView getM_CA3currentDtl(
	        String reportId, String fromdate, String todate, String currency,
	        String dtltype, Pageable pageable, String filter) {

	    int pageSize = pageable.getPageSize();
	    int currentPage = pageable.getPageNumber();
	    int totalPages=0;

	    ModelAndView mv = new ModelAndView();
	    List<M_CA3_Detail_Entity> T1Dt1 = new ArrayList<>();

	    try {
	        Date d1 = dateformat.parse(todate);

	        String rowId = null;
	        String columnId = null;

	        // ✅ Split the filter string here
	        if (filter != null && filter.contains(",")) {
	            String[] parts = filter.split(",");
	            if (parts.length >= 2) {
	                rowId = parts[0];
	                columnId = parts[1];
	            }
	        }

	        if (rowId != null && columnId != null) {
	            T1Dt1 = BRRS_M_CA3_detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId);
	        } else {
	        	T1Dt1 = BRRS_M_CA3_detail_Repo.getdatabydateList(d1,currentPage,pageSize);
				totalPages=BRRS_M_CA3_detail_Repo.getdatacount(dateformat.parse(todate));
				mv.addObject("pagination","YES");
	        }

	        System.out.println("LISTCOUNT: " + T1Dt1.size());

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_CA3");
	    mv.addObject("currentPage", currentPage);
		mv.addObject("totalPages",(int)Math.ceil((double)totalPages / 100));
	    mv.addObject("displaymode", "Details");
	    mv.addObject("reportdetails", T1Dt1);
	    mv.addObject("reportmaster12", T1Dt1);
	    mv.addObject("reportsflag", "reportsflag");
	    mv.addObject("menu", reportId);
	    return mv;
	}
public byte[] BRRS_M_CA3Excel(String filename,String reportId, String fromdate, String todate, String currency, String dtltype) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		List<M_CA3_Summary_Entity> dataList =BRRS_M_CA3_Summary_Repo.getdatabydateList(dateformat.parse(todate)) ;

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

			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA3_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

		
					//row10
					// Column b
					Cell cell1 = row.createCell(1);
					if (record.getR10_PRODUCT() != null) {
						cell1.setCellValue(record.getR10_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
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
					 cell1 = row.createCell(1);
					if (record.getR11_PRODUCT() != null) {
						cell1.setCellValue(record.getR11_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR12_PRODUCT() != null) {
						cell1.setCellValue(record.getR12_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR13_PRODUCT() != null) {
						cell1.setCellValue(record.getR13_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
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
					 cell1 = row.createCell(1);
					if (record.getR14_PRODUCT() != null) {
						cell1.setCellValue(record.getR14_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR15_PRODUCT() != null) {
						cell1.setCellValue(record.getR15_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR16_PRODUCT() != null) {
						cell1.setCellValue(record.getR16_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR17_PRODUCT() != null) {
						cell1.setCellValue(record.getR17_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR18_PRODUCT() != null) {
						cell1.setCellValue(record.getR18_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR19_PRODUCT() != null) {
						cell1.setCellValue(record.getR19_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR20_PRODUCT() != null) {
						cell1.setCellValue(record.getR20_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR24_PRODUCT() != null) {
						cell1.setCellValue(record.getR24_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR25_PRODUCT() != null) {
						cell1.setCellValue(record.getR25_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR26_PRODUCT() != null) {
						cell1.setCellValue(record.getR26_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR27_PRODUCT() != null) {
						cell1.setCellValue(record.getR27_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR28_PRODUCT() != null) {
						cell1.setCellValue(record.getR28_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR29_PRODUCT() != null) {
						cell1.setCellValue(record.getR29_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR36_PRODUCT() != null) {
						cell1.setCellValue(record.getR36_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR37_PRODUCT() != null) {
						cell1.setCellValue(record.getR37_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR38_PRODUCT() != null) {
						cell1.setCellValue(record.getR38_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR39_PRODUCT() != null) {
						cell1.setCellValue(record.getR39_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR40_PRODUCT() != null) {
						cell1.setCellValue(record.getR40_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR41_PRODUCT() != null) {
						cell1.setCellValue(record.getR41_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR44_PRODUCT() != null) {
						cell1.setCellValue(record.getR44_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR45_PRODUCT() != null) {
						cell1.setCellValue(record.getR45_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR46_PRODUCT() != null) {
						cell1.setCellValue(record.getR46_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR50_PRODUCT() != null) {
						cell1.setCellValue(record.getR50_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR51_PRODUCT() != null) {
						cell1.setCellValue(record.getR51_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR52_PRODUCT() != null) {
						cell1.setCellValue(record.getR52_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR53_PRODUCT() != null) {
						cell1.setCellValue(record.getR53_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR54_PRODUCT() != null) {
						cell1.setCellValue(record.getR54_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR55_PRODUCT() != null) {
						cell1.setCellValue(record.getR55_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR58_PRODUCT() != null) {
						cell1.setCellValue(record.getR58_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR59_PRODUCT() != null) {
						cell1.setCellValue(record.getR59_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
					 cell1 = row.createCell(1);
					if (record.getR15_PRODUCT() != null) {
						cell1.setCellValue(record.getR15_PRODUCT());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

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
	
	
	public byte[] BRRS_M_CA3DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
	    try {
	        logger.info("Generating Excel for BRRS_M_CA1 Details...");
	        System.out.println("came to Detail download service");

	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("BRRS_M_CA3Details");

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
	        balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
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
	        List<M_CA3_Detail_Entity> reportData = BRRS_M_CA3_detail_Repo.getdatabydateList(parsedToDate);

	        if (reportData != null && !reportData.isEmpty()) {
	            int rowIndex = 1;
	            for (M_CA3_Detail_Entity item : reportData) {
	                XSSFRow row = sheet.createRow(rowIndex++);

	                row.createCell(0).setCellValue(item.getCUST_ID());
	                row.createCell(1).setCellValue(item.getACCT_NUMBER());
	                row.createCell(2).setCellValue(item.getACCT_NAME());

	                // ACCT BALANCE (right aligned, 3 decimal places)
	                Cell balanceCell = row.createCell(3);
	                if (item.getACCT_BALANCE_IN_PULA() != null) {
	                    balanceCell.setCellValue(item.getACCT_BALANCE_IN_PULA().doubleValue());
	                } else {
	                    balanceCell.setCellValue(0.000);
	                }
	                balanceCell.setCellStyle(balanceStyle);

	                row.createCell(4).setCellValue(item.getROW_ID());
	                row.createCell(5).setCellValue(item.getCOLUMN_ID());
	                row.createCell(6).setCellValue(
	                    item.getREPORT_DATE() != null ?
	                    new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE()) : ""
	                );

	                // Apply data style for all other cells
	                for (int j = 0; j < 7; j++) {
	                    if (j != 3) {
	                        row.getCell(j).setCellStyle(dataStyle);
	                    }
	                }
	            }
	        } else {
	            logger.info("No data found for BRRS_M_CA3Details — only header will be written.");
	        }

	        // Write to byte[]
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        workbook.write(bos);
	        workbook.close();

	        logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
	        return bos.toByteArray();

	    } catch (Exception e) {
	        logger.error("Error generating BRRS_M_CA3 Excel", e);
	        return new byte[0];
	    }
	}


}

