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

import com.bornfire.brrs.entities.M_CA1_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_CA1_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_CA1_Archival_Summary_Repo;
import com.bornfire.brrs.entities.M_CA1_Summary_Entity;
import com.bornfire.brrs.entities.M_CA7_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_CA7_Summary_Entity;
import com.bornfire.brrs.entities.M_CA1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_CA1_Summary_Repo;


@Component
@Service

public class BRRS_M_CA1_ReportService {
private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA1_ReportService.class);
	


	@Autowired
	private Environment env;
	
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	BRRS_M_CA1_Detail_Repo BRRS_M_CA1_detail_Repo;
	
	@Autowired
	BRRS_M_CA1_Summary_Repo BRRS_M_CA1_Summary_Repo;
   
	@Autowired
	BRRS_M_CA1_Archival_Summary_Repo BRRS_M_CA1_Archival_Summary_Repo;
	
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	public ModelAndView getM_CA1View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {


		
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;	
		try {
			Date d1 = dateformat.parse(todate);

	 // ---------- CASE 1: ARCHIVAL ----------
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
            List<M_CA1_Archival_Summary_Entity> T1Master = 
            		BRRS_M_CA1_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
         
            mv.addObject("reportsummary", T1Master);
        }

        // ---------- CASE 3: NORMAL ----------
        else {
            List<M_CA1_Summary_Entity> T1Master = 
            		BRRS_M_CA1_Summary_Repo.getdatabydateList(dateformat.parse(todate));
            System.out.println("T1Master Size "+T1Master.size());
            mv.addObject("reportsummary", T1Master);
        }

		} catch (ParseException e) {
			e.printStackTrace();
		}
			
		mv.setViewName("BRRS/M_CA1");
			
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());

		return mv;


	}

		
	
	
	public ModelAndView getM_CA1currentDtl(
	        String reportId, String fromdate, String todate, String currency,
	        String dtltype, Pageable pageable, String filter) {

	    int pageSize = pageable.getPageSize();
	    int currentPage = pageable.getPageNumber();
	    int totalPages=0;

	    ModelAndView mv = new ModelAndView();
	    List<M_CA1_Detail_Entity> T1Dt1 = new ArrayList<>();

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
	            T1Dt1 = BRRS_M_CA1_detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId);
	        } else {
	            T1Dt1 = BRRS_M_CA1_detail_Repo.getdatabydateList(d1,currentPage,pageSize);
				totalPages=BRRS_M_CA1_detail_Repo.getdatacount(dateformat.parse(todate));
				mv.addObject("pagination","YES");
	        }

	        System.out.println("LISTCOUNT: " + T1Dt1.size());

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_CA1");
	    mv.addObject("currentPage", currentPage);
		mv.addObject("totalPages",(int)Math.ceil((double)totalPages / 100));
	    mv.addObject("displaymode", "Details");
	    mv.addObject("reportdetails", T1Dt1);
	    mv.addObject("reportmaster12", T1Dt1);
	    mv.addObject("reportsflag", "reportsflag");
	    mv.addObject("menu", reportId);
	    return mv;
	}
public byte[] BRRS_M_CA1Excel(String filename,String reportId, String fromdate, String todate, String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");


		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_CA1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		List<M_CA1_Summary_Entity> dataList =BRRS_M_CA1_Summary_Repo.getdatabydateList(dateformat.parse(todate)) ;

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for CA1 report. Returning empty result.");
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
			int startRow = 8;


			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA1_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

		
					//row9
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR9_AMOUNT() != null) {
						cell3.setCellValue(record.getR9_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
									
					
					//row10
					row = sheet.getRow(9);			
					// Column D 
					 cell3 = row.getCell(3);
					if (record.getR10_AMOUNT() != null) {
						cell3.setCellValue(record.getR10_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row11
					row = sheet.getRow(10);			
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR11_AMOUNT() != null) {
						cell3.setCellValue(record.getR11_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row12
					row = sheet.getRow(11);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR12_AMOUNT() != null) {
						cell3.setCellValue(record.getR12_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row13
					row = sheet.getRow(12);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR13_AMOUNT() != null) {
						cell3.setCellValue(record.getR13_AMOUNT().doubleValue());
			
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

										
					
					

					//row17
					row = sheet.getRow(16);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR17_AMOUNT() != null) {
						cell3.setCellValue(record.getR17_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row18
					row = sheet.getRow(17);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR18_AMOUNT() != null) {
						cell3.setCellValue(record.getR18_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row19
					row = sheet.getRow(18);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR19_AMOUNT() != null) {
						cell3.setCellValue(record.getR19_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row20
					row = sheet.getRow(19);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR20_AMOUNT() != null) {
						cell3.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row21
					row = sheet.getRow(20);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR21_AMOUNT() != null) {
						cell3.setCellValue(record.getR21_AMOUNT().doubleValue());

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

public byte[] getExcelM_CA1ARCHIVAL(String filename,String reportId, String fromdate, String todate, String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");


		// ARCHIVAL check
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_CA1_Archival_Summary_Entity> dataList =BRRS_M_CA1_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),version) ;

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for CA1 report. Returning empty result.");
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
			int startRow = 8;


			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_CA1_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

		
					//row9
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR9_AMOUNT() != null) {
						cell3.setCellValue(record.getR9_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
									
					
					//row10
					row = sheet.getRow(9);			
					// Column D 
					 cell3 = row.getCell(3);
					if (record.getR10_AMOUNT() != null) {
						cell3.setCellValue(record.getR10_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row11
					row = sheet.getRow(10);			
					// Column D
					 cell3 = row.getCell(3);
					if (record.getR11_AMOUNT() != null) {
						cell3.setCellValue(record.getR11_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					//row12
					row = sheet.getRow(11);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR12_AMOUNT() != null) {
						cell3.setCellValue(record.getR12_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row13
					row = sheet.getRow(12);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR13_AMOUNT() != null) {
						cell3.setCellValue(record.getR13_AMOUNT().doubleValue());
			
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

										
					
					

					//row17
					row = sheet.getRow(16);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR17_AMOUNT() != null) {
						cell3.setCellValue(record.getR17_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row18
					row = sheet.getRow(17);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR18_AMOUNT() != null) {
						cell3.setCellValue(record.getR18_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row19
					row = sheet.getRow(18);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR19_AMOUNT() != null) {
						cell3.setCellValue(record.getR19_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row20
					row = sheet.getRow(19);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR20_AMOUNT() != null) {
						cell3.setCellValue(record.getR20_AMOUNT().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					

					//row21
					row = sheet.getRow(20);			
					// Column F 
					 cell3 = row.getCell(3);
					if (record.getR21_AMOUNT() != null) {
						cell3.setCellValue(record.getR21_AMOUNT().doubleValue());

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
	
	public byte[] BRRS_M_CA1DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
	    try {
	        logger.info("Generating Excel for BRRS_M_CA1 Details...");
	        System.out.println("came to Detail download service");

	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("BRRS_M_PIDetails");

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
	        List<M_CA1_Detail_Entity> reportData = BRRS_M_CA1_detail_Repo.getdatabydateList(parsedToDate);

	        if (reportData != null && !reportData.isEmpty()) {
	            int rowIndex = 1;
	            for (M_CA1_Detail_Entity item : reportData) {
	                XSSFRow row = sheet.createRow(rowIndex++);

	                row.createCell(0).setCellValue(item.getCust_id());
	                row.createCell(1).setCellValue(item.getAcct_name());
	                row.createCell(2).setCellValue(item.getAcct_name());

	                // ACCT BALANCE (right aligned, 3 decimal places)
	                Cell balanceCell = row.createCell(3);
	                if (item.getAcct_balance_in_pula() != null) {
	                    balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
	                } else {
	                    balanceCell.setCellValue(0.000);
	                }
	                balanceCell.setCellStyle(balanceStyle);

	                row.createCell(4).setCellValue(item.getReport_label());
	                row.createCell(5).setCellValue(item.getReport_addl_criteria_1());
	                row.createCell(6).setCellValue(
	                    item.getReport_date() != null ?
	                    new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date()) : ""
	                );

	                // Apply data style for all other cells
	                for (int j = 0; j < 7; j++) {
	                    if (j != 3) {
	                        row.getCell(j).setCellStyle(dataStyle);
	                    }
	                }
	            }
	        } else {
	            logger.info("No data found for BRRS_M_CA1Details — only header will be written.");
	        }

	        // Write to byte[]
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        workbook.write(bos);
	        workbook.close();

	        logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
	        return bos.toByteArray();

	    } catch (Exception e) {
	        logger.error("Error generating BRRS_M_PI Excel", e);
	        return new byte[0];
	    }
	}


public List<Object> getM_CA1Archival() {
		List<Object> M_CA1Archivallist = new ArrayList<>();
		try {
			M_CA1Archivallist = BRRS_M_CA1_Archival_Summary_Repo.getM_CA1archival();
			System.out.println("countser" + M_CA1Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_CA1 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_CA1Archivallist;
	}
}

