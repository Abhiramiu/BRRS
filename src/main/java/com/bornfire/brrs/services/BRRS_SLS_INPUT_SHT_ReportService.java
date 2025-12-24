package com.bornfire.brrs.services;

import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
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
import java.text.DateFormat;

import com.bornfire.brrs.entities.SLS_INPUT_SHT_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_SLS_INPUT_SHT_Detail_Repo;
import com.bornfire.brrs.entities.SLS_INPUT_SHT_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_SLS_INPUT_SHT_Summary_Repo;
import com.bornfire.brrs.entities.M_LCR_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LCR_Summary_Entity;



@Service
public class BRRS_SLS_INPUT_SHT_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_SLS_INPUT_SHT_ReportService.class);
	

	@Autowired
	private Environment env;
	
	@Autowired
	BRRS_SLS_INPUT_SHT_Detail_Repo BRRS_SLS_INPUT_SHT_Detail_Repo;
	
	@Autowired
	BRRS_SLS_INPUT_SHT_Summary_Repo BRRS_SLS_INPUT_SHT_Summary_Repo;
	
	@Autowired
	SessionFactory sessionFactory;
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
	
//	public ModelAndView getM_SLSView(String reportId, String fromdate, String todate, String currency, String dtltype,
//			Pageable pageable, String type, String version) {
//		ModelAndView mv = new ModelAndView();
//		Session hs = sessionFactory.getCurrentSession();
//		int pageSize = pageable.getPageSize();
//		int currentPage = pageable.getPageNumber();
//		int startItem = currentPage * pageSize;
//
//		System.out.println("testing");
//		System.out.println(version);
//
//		if (type.equals("ARCHIVAL") & version != null) {
//			System.out.println(type);
//			List<SLS_INPUT_SHT_Summary_Entity> T1Master = new ArrayList<SLS_INPUT_SHT_Summary_Entity>();
//			System.out.println(version);
//			try {
//				Date d1 = dateformat.parse(todate);
//
//				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
//				// ", BRF1_REPORT_ENTITY.class)
//				// .setParameter(1, df.parse(todate)).getResultList();
////				T1Master = BRRS_SLS_INPUT_SHT_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),
////						version);
//
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//
//			mv.addObject("reportsummary", T1Master);
//		} else {
//
//			List<SLS_INPUT_SHT_Summary_Entity> T1Master = new ArrayList<SLS_INPUT_SHT_Summary_Entity>();
//			try {
//				Date d1 = dateformat.parse(todate);
//				// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
//
//				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
//				// ", BRF1_REPORT_ENTITY.class)
//				// .setParameter(1, df.parse(todate)).getResultList();
//				T1Master = BRRS_SLS_INPUT_SHT_Summary_Repo.getdatabydateList(dateformat.parse(todate));
//
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//			mv.addObject("reportsummary", T1Master);
//		}
//
//		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
//
//		mv.setViewName("BRRS/SLS_REPORT");
//
//		// mv.addObject("reportsummary", T1Master);
//		// mv.addObject("reportmaster", T1Master);
//		mv.addObject("displaymode", "summary");
//		// mv.addObject("reportsflag", "reportsflag");
//		// mv.addObject("menu", reportId);
//		System.out.println("scv" + mv.getViewName());
//
//		return mv;
//	}
	
	
	public ModelAndView getRT_SLSView(String reportId, String fromdate, String todate, String currency, String dtltype, Pageable pageable) {
	    ModelAndView mv = new ModelAndView();
	    Date reportDate = null;
	    
	    // Use the format seen in your logs: 31-May-2025
	    // Adding Locale.ENGLISH is critical for month names like 'May' or 'Oct'
	    SimpleDateFormat logSdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
	    // Fallback for the standard 31/05/2025 format
	    SimpleDateFormat standardSdf = new SimpleDateFormat("dd/MM/yyyy");

	    // 1. ROUTING: If coming from Validations page
	    if ("report".equalsIgnoreCase(dtltype)) {
	        List<SLS_INPUT_SHT_Summary_Entity> slslist = BRRS_SLS_INPUT_SHT_Summary_Repo.rtslslist();
	        mv.addObject("slslist", slslist);
	        mv.setViewName("BRRS/SLS"); 
	        return mv;
	    }

	    // 2. DATA LOADING: Parse the todate
	    try {
	        if (todate != null && !todate.trim().isEmpty()) {
	            if (todate.contains("-")) {
	                reportDate = logSdf.parse(todate.trim());
	            } else {
	                reportDate = standardSdf.parse(todate.trim());
	            }
	        }
	    } catch (Exception e) {
	        logger.error("Date parse error for: " + todate);
	    }

	    // CRITICAL: If parsing failed, don't execute query (prevents ORA-00932)
	    if (reportDate == null) {
	        logger.warn("Report Date is null, redirecting to date selection list.");
	        List<SLS_INPUT_SHT_Summary_Entity> slslist = BRRS_SLS_INPUT_SHT_Summary_Repo.rtslslist();
	        mv.addObject("slslist", slslist);
	        mv.setViewName("BRRS/SLS");
	        return mv;
	    }

	    mv.addObject("reportdate", todate);
	    mv.addObject("currency", currency);

	    if ("Detail".equalsIgnoreCase(dtltype)) {
	        List<SLS_INPUT_SHT_Detail_Entity> detailList = BRRS_SLS_INPUT_SHT_Detail_Repo.slsdetaillist(
	                reportDate, (pageable.getPageNumber() * pageable.getPageSize()), pageable.getPageSize());
	        int totalCount = BRRS_SLS_INPUT_SHT_Detail_Repo.slsdetaillistcount(reportDate);
	        mv.addObject("slsdetaillist", detailList);
	        mv.addObject("currentPage", pageable.getPageNumber());
	        mv.addObject("totalPages", (int) Math.ceil((double) totalCount / pageable.getPageSize()));
	        mv.addObject("formmode", "Detail");
	    } else {
	        // This is where ORA-00932 was happening
	        mv.addObject("slslist", BRRS_SLS_INPUT_SHT_Summary_Repo.rtslslistbydate(reportDate, currency));
	        mv.addObject("currencylist", BRRS_SLS_INPUT_SHT_Summary_Repo.rtslslistonlydate(reportDate));
	        mv.addObject("formmode", "summary");
	    }

	    mv.setViewName("BRRS/SLS_REPORT");
	    return mv;
	}
	
	public byte[] getSlsExcel(String filename,String reportdate, String currency,String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		

		List<SLS_INPUT_SHT_Summary_Entity> dataList = BRRS_SLS_INPUT_SHT_Summary_Repo.rtslslistbydate(dateformat.parse(reportdate),currency);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for sls report. Returning empty result.");
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
			font.setFontHeightInPoints((short) 11); // size 8
			font.setFontName("Calibri");

			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

				
			
			  Row daterow=sheet.getRow(4);
			  Cell datecell =daterow.getCell(3);
			  datecell.setCellValue(reportdate);
			 
			
			
			int startRow = 10;

			if (!dataList.isEmpty()) {
				
				String [] fieldSuffixes= {"DAY1","DAY2_7","DAY8_14","DAY15_30","DAY31_TO_2M","MORE2M_TO_3M",
											"OVER3M_TO_6M","OVER6M_TO_1Y","OVER1Y_TO_3Y","OVER3Y_TO_5Y","OVER5Y"};
				
				
				
				for (int rowIndex = 10; rowIndex < 85; rowIndex++) {
					
					SLS_INPUT_SHT_Summary_Entity record = dataList.get(0);
					
					System.out.println(rowIndex);
					if(rowIndex==40 || rowIndex==41 || rowIndex==42 || rowIndex==43 || rowIndex==44 ||rowIndex==45 || rowIndex==74 
							|| rowIndex==75 || rowIndex==76 ||rowIndex==77|| rowIndex==78 ||rowIndex==79 || rowIndex==80 || rowIndex==82 ||rowIndex==85 ) {
						continue;
					}

					
					for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
						
						
					String fieldName = "R"+(rowIndex+1)+"_" + fieldSuffixes[colIndex];
					Row row = sheet.getRow(rowIndex);
					Cell cell = row.getCell(colIndex+3);
					
					 try {
				          Field field = SLS_INPUT_SHT_Summary_Entity.class.getDeclaredField(fieldName);
				          field.setAccessible(true);
				          Object value = field.get(record);
				          System.out.println("fieldname"+value);
				          if (value instanceof BigDecimal) {
				              cell.setCellValue(((BigDecimal) value).doubleValue());
				              //cell.setCellStyle(numberStyle);
				          } else {
				              cell.setCellValue(0.00);
				              //cell.setCellStyle(textStyle);
				          }
				      } catch (NoSuchFieldException | IllegalAccessException e) {
				          cell.setCellValue("");
				          cell.setCellStyle(textStyle);
				          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
				      }
						
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
	
	
	
	public byte[] getDetailExcel(String filename,String reportdate, String currency,String version){
		try {
			logger.info("Generating Excel for SLS Details...");
			
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("SLS_Details");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID",
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
			Date parsedToDate = new SimpleDateFormat("dd-MMM-yyyy").parse(reportdate);
			
			 int batchSize = 5000;
		     int offset = 0;
		     int rowIndex = 1;
		     List<SLS_INPUT_SHT_Detail_Entity> reportData= new ArrayList<SLS_INPUT_SHT_Detail_Entity>();
		     //System.out.println("offset=="+offset);
		     while (true) {
		    	 
		    	 //System.out.println("offsettest");
		    	  reportData = BRRS_SLS_INPUT_SHT_Detail_Repo.slsdetaillist(parsedToDate,offset,batchSize);
		    	    if (reportData.isEmpty()) break;  // <-- STOP when there is no more data

		    	    
		    	    if (reportData != null && !reportData.isEmpty()) {
						
						for (SLS_INPUT_SHT_Detail_Entity item : reportData) {
							XSSFRow row = sheet.createRow(rowIndex++);
							
							if(item.getCustId()!=null) {
								row.createCell(0).setCellValue(item.getCustId());
							}
							else {
								row.createCell(0).setCellValue(" ");
							}
							
							if(item.getAcid()!=null) {
								row.createCell(1).setCellValue(item.getAcid());
							}
							else {
								row.createCell(1).setCellValue(" ");
							}
							if(item.getAcctName()!=null) {
								row.createCell(2).setCellValue(item.getAcctName());
							}
							else {
								row.createCell(2).setCellValue("");
							}
							
							
							

							// ACCT BALANCE (right aligned, 3 decimal places)
							Cell balanceCell = row.createCell(3);
							if (item.getAcctBalanceLc() != null) {
								balanceCell.setCellValue(item.getAcctBalanceLc().doubleValue());
							} else {
								balanceCell.setCellValue(0.000);
							}
							balanceCell.setCellStyle(balanceStyle);

							row.createCell(4).setCellValue(item.getReportLabel());
							row.createCell(5)
									.setCellValue(item.getReportDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : "");

							
							// Apply data style for all other cells
							for (int j = 0; j < 5; j++) {
								if (j != 3) {
									row.getCell(j).setCellStyle(dataStyle);
								}
							}
						}
					} else {
						logger.info("No data found for sls — only header will be written.");
					}

		    	    offset += batchSize;
		    	   
		    	}
		     
			
			System.out.println(reportData.size());

			

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating sls Excel", e);
			return null;
		}
	}
	
	
	private final ConcurrentHashMap<String, byte[]> jobStorage = new ConcurrentHashMap<>();

	
    @Async
    public void generateReportAsync(String jobId, String filename, String reportDate, String currency,String version) {
        //System.out.println("Starting report generation for: " + filename);
		        
		byte[] fileData = getDetailExcel(filename,reportDate,currency, version);
		if (fileData == null) {
		    //logger.warn("Excel generation failed or no data for jobId: {}", jobId);
		    jobStorage.put(jobId, null); 
		} else {
		    jobStorage.put(jobId, fileData);
		}

		//System.out.println("Report generation completed for: " + filename);
    }

    
    public byte[] getReport(String jobId) {
    	 //System.out.println("Report generation completed for: " + jobId);
        return jobStorage.get(jobId);
    }
	
	
	
}
