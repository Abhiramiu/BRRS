package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;


import com.bornfire.brrs.entities.BRRS_MDISB4_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_MDISB4_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_MDISB4_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_MDISB4_Summary_Repo;
import com.bornfire.brrs.entities.MDISB4_Archival_Detail_Entity;
import com.bornfire.brrs.entities.MDISB4_Archival_Summary_Entity;
import com.bornfire.brrs.entities.MDISB4_Detail_Entity;
import com.bornfire.brrs.entities.MDISB4_Summary_Entity;

@Component
@Service

public class BRRS_MDISB4_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_MDISB4_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_MDISB4_Detail_Repo BRRS_MDISB4_Detail_Repo;

	@Autowired
	BRRS_MDISB4_Summary_Repo BRRS_MDISB4_Summary_Repo;

	@Autowired
	BRRS_MDISB4_Archival_Detail_Repo BRRS_MDISB4_Archival_Detail_Repo;

	@Autowired
	BRRS_MDISB4_Archival_Summary_Repo BRRS_MDISB4_Archival_Summary_Repo;
	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	
	public ModelAndView getMDISB4View(String reportId, String fromdate, String todate, String currency, String dtltype,
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
			List<MDISB4_Archival_Summary_Entity> T1Master = new ArrayList<MDISB4_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

			
				T1Master = BRRS_MDISB4_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),
						version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {

			List<MDISB4_Summary_Entity> T1Master = new ArrayList<MDISB4_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
			
				T1Master = BRRS_MDISB4_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/MDISB4");

	
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());

		return mv;
	}
	
	
	public ModelAndView getMDISB4currentDtl(String reportId, String fromdate, String todate, String currency,
            String dtltype, Pageable pageable, String filter, String type, String version) {

        int pageSize = pageable != null ? pageable.getPageSize() : 10;
        int currentPage = pageable != null ? pageable.getPageNumber() : 0;
        int totalPages = 0;

        ModelAndView mv = new ModelAndView();

        // Session hs = sessionFactory.getCurrentSession();

        try {
            Date parsedDate = null;

            if (todate != null && !todate.isEmpty()) {
                parsedDate = dateformat.parse(todate);
            }

            String reportLabel = null;
            String reportAddlCriteria1 = null;
            // ? Split filter string into rowId & columnId
            if (filter != null && filter.contains(",")) {
                String[] parts = filter.split(",");
                if (parts.length >= 2) {
                    reportLabel = parts[0];
                    reportAddlCriteria1 = parts[1];
                }
            }

            System.out.println(type);
            if ("ARCHIVAL".equals(type) && version != null) {
                System.out.println(type);
                // ?? Archival branch
                List<MDISB4_Archival_Detail_Entity> T1Dt1;
                if (reportLabel != null && reportAddlCriteria1 != null) {
                    T1Dt1 = BRRS_MDISB4_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel,
                            reportAddlCriteria1,
                            parsedDate, version);
                } else {
                    T1Dt1 = BRRS_MDISB4_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
                }

                mv.addObject("reportdetails", T1Dt1);
                mv.addObject("reportmaster12", T1Dt1);
                System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

            } else {
                // ?? Current branch
                List<MDISB4_Detail_Entity> T1Dt1;

                if (reportLabel != null && reportAddlCriteria1 != null) {
                    T1Dt1 = BRRS_MDISB4_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1,
                            parsedDate);
                } else {
                    T1Dt1 = BRRS_MDISB4_Detail_Repo.getdatabydateList(parsedDate);
                    totalPages = BRRS_MDISB4_Detail_Repo.getdatacount(parsedDate);
                    mv.addObject("pagination", "YES");

                }

                mv.addObject("reportdetails", T1Dt1);
                mv.addObject("reportmaster12", T1Dt1);

                System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            mv.addObject("errorMessage", "Invalid date format: " + todate);
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
        }

        mv.setViewName("BRRS/MDISB4");
        mv.addObject("displaymode", "Details");
        mv.addObject("currentPage", currentPage);
        System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
        mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
        mv.addObject("reportsflag", "reportsflag");
        mv.addObject("menu", reportId);
        return mv;
    }


	
	public byte[] getMDISB4Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelMDISB4ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<MDISB4_Summary_Entity> dataList = BRRS_MDISB4_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MDISB4 report. Returning empty result.");
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

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					MDISB4_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R6
					// Column B
					Cell cell1 = row.getCell(1);
					if (record.getR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					Cell cell2 = row.getCell(2);
					if (record.getR6_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR6_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					Cell cell4 = row.getCell(4);
					if (record.getR6_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR6_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					Cell cell5 = row.getCell(5);
					if (record.getR6_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR6_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					Cell cell6 = row.getCell(6);
					if (record.getR6_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR6_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					
					// R7
					row = sheet.getRow(6);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR7_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR7_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR7_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR7_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR7_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR7_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR7_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR7_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					
					// R8
					row = sheet.getRow(7);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR8_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR8_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR8_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR8_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR8_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR8_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR8_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR8_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					
					// R9
					row = sheet.getRow(8);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR9_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR9_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR9_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR9_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR9_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR9_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR9_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR9_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// R10
					row = sheet.getRow(9);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR10_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR10_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR10_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR10_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR10_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR10_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR10_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// R11
					row = sheet.getRow(10);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR11_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR11_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR11_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR11_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR11_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR11_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR11_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					// R12
					row = sheet.getRow(11);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR12_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR12_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR12_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR12_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR12_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR12_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR12_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
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

	public byte[] getMDISB4DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for MDISB4 Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MDISB4Details");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCOUNT BALANCE IN PULA", "ROWID", "COLUMNID",
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
			List<MDISB4_Detail_Entity> reportData = BRRS_MDISB4_Detail_Repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (MDISB4_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
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
				logger.info("No data found for MDISB4 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating MDISB4 Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getMDISB4Archival() {
		List<Object> MDISB4Archivallist = new ArrayList<>();
		try {
			MDISB4Archivallist = BRRS_MDISB4_Archival_Summary_Repo.getMDISB4archival();
			System.out.println("countser" + MDISB4Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching MDISB4 Archival data: " + e.getMessage());
			e.printStackTrace();

		}
		return MDISB4Archivallist;
	}
	
	
	
	public byte[] getExcelMDISB4ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<MDISB4_Archival_Summary_Entity> dataList = BRRS_MDISB4_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MDISB4 report. Returning empty result.");
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
			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					MDISB4_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					// R6
					// Column B
					Cell cell1 = row.getCell(1);
					if (record.getR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					Cell cell2 = row.getCell(2);
					if (record.getR6_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR6_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					Cell cell4 = row.getCell(4);
					if (record.getR6_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR6_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					Cell cell5 = row.getCell(5);
					if (record.getR6_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR6_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					Cell cell6 = row.getCell(6);
					if (record.getR6_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR6_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					
					// R7
					row = sheet.getRow(6);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR7_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR7_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR7_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR7_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR7_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR7_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR7_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR7_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					
					// R8
					row = sheet.getRow(7);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR8_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR8_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR8_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR8_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR8_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR8_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR8_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR8_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					
					// R9
					row = sheet.getRow(8);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR9_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR9_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR9_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR9_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR9_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR9_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR9_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR9_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// R10
					row = sheet.getRow(9);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR10_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR10_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR10_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR10_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR10_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR10_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR10_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// R11
					row = sheet.getRow(10);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR11_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR11_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR11_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR11_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR11_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR11_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR11_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					// R12
					row = sheet.getRow(11);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR12_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR12_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR12_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR12_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR12_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR12_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR12_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
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

	
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_MDISB4 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MDISB4Details");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "PROVISION AMOUNT", "ROWID", "COLUMNID",
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
			List<MDISB4_Archival_Detail_Entity> reportData = BRRS_MDISB4_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (MDISB4_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
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
				logger.info("No data found for MDISB4 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating MDISB4 Excel", e);
			return new byte[0];
		}
	}

	
	@Autowired
	private BRRS_MDISB4_Detail_Repo MDISB4_Detail_Repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/MDISB4"); 

		if (acctNo != null) {
			MDISB4_Detail_Entity mMDISB4Entity = MDISB4_Detail_Repo.findByAcctNumber(acctNo);
			if (mMDISB4Entity != null && mMDISB4Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(mMDISB4Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", mMDISB4Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInPula");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			MDISB4_Detail_Entity existing = MDISB4_Detail_Repo.findByAcctNumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			if (acctName != null && !acctName.isEmpty()) {
				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
					existing.setAcctName(acctName);
					isChanged = true;
					logger.info("Account name updated to {}", acctName);
				}
			}

			if (provisionStr != null && !provisionStr.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr);
				if (existing.getAcctBalanceInPula() == null || existing.getAcctBalanceInPula().compareTo(newProvision) != 0) {
					existing.setAcctBalanceInPula(newProvision);
					isChanged = true;
					logger.info("Provision updated to {}", newProvision);
				}
			}
			if (isChanged) {
				MDISB4_Detail_Repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_MDISB4_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_MDISB4_SUMMARY_PROCEDURE(?); END;", formattedDate);
							logger.info("Procedure executed successfully after commit.");
						} catch (Exception e) {
							logger.error("Error executing procedure after commit", e);
						}
					}
				});

				return ResponseEntity.ok("Record updated successfully!");
			} else {
				logger.info("No changes detected for ACCT_NO: {}", acctNo);
				return ResponseEntity.ok("No changes were made.");
			}

		} catch (Exception e) {
			logger.error("Error updating MDISB4 record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}



}
