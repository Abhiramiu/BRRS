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

import com.bornfire.brrs.entities.BRRS_DBS10_FINCON_II_1A_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_DBS10_FINCON_II_1A_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_DBS10_FINCON_II_1A_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_DBS10_FINCON_II_1A_Summary_Repo;
import com.bornfire.brrs.entities.DBS10_FINCON_II_1A_Archival_Summary_Entity;
import com.bornfire.brrs.entities.DBS10_FINCON_II_1A_Summary_Entity;
import com.bornfire.brrs.entities.DBS10_FINCON_II_1A_Archival_Detail_Entity;
import com.bornfire.brrs.entities.DBS10_FINCON_II_1A_Detail_Entity;

@Component
@Service

public class BRRS_DBS10_FINCON_II_1A_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_DBS10_FINCON_II_1A_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_DBS10_FINCON_II_1A_Detail_Repo BRRS_DBS10_FINCON_II_1A_Detail_Repo;

	@Autowired
	BRRS_DBS10_FINCON_II_1A_Summary_Repo BRRS_DBS10_FINCON_II_1A_Summary_Repo;

	@Autowired
	BRRS_DBS10_FINCON_II_1A_Archival_Detail_Repo BRRS_DBS10_FINCON_II_1A_Archival_Detail_Repo;

	@Autowired
	BRRS_DBS10_FINCON_II_1A_Archival_Summary_Repo BRRS_DBS10_FINCON_II_1A_Archival_Summary_Repo;
	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
	
	public ModelAndView getDBS10_FINCON_II_1AView(String reportId, String fromdate, String todate, String currency, String dtltype,
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
			List<DBS10_FINCON_II_1A_Archival_Summary_Entity> T1Master = new ArrayList<DBS10_FINCON_II_1A_Archival_Summary_Entity>();
			System.out.println(version);
			try {
				Date d1 = dateformat.parse(todate);

			
				T1Master = BRRS_DBS10_FINCON_II_1A_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate),
						version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {

			List<DBS10_FINCON_II_1A_Summary_Entity> T1Master = new ArrayList<DBS10_FINCON_II_1A_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
			
				T1Master = BRRS_DBS10_FINCON_II_1A_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/DBS10_FINCON_II_1A");

	
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());

		return mv;
	}
	
	public ModelAndView getDBS10_FINCON_II_1AcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
                List<DBS10_FINCON_II_1A_Archival_Detail_Entity> T1Dt1;
                if (reportLabel != null && reportAddlCriteria1 != null) {
                    T1Dt1 = BRRS_DBS10_FINCON_II_1A_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel,
                            reportAddlCriteria1,
                            parsedDate, version);
                } else {
                    T1Dt1 = BRRS_DBS10_FINCON_II_1A_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
                }

                mv.addObject("reportdetails", T1Dt1);
                mv.addObject("reportmaster12", T1Dt1);
                System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

            } else {
                // ?? Current branch
                List<DBS10_FINCON_II_1A_Detail_Entity> T1Dt1;

                if (reportLabel != null && reportAddlCriteria1 != null) {
                    T1Dt1 = BRRS_DBS10_FINCON_II_1A_Detail_Repo.GetDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1,
                            parsedDate);
                } else {
                    T1Dt1 = BRRS_DBS10_FINCON_II_1A_Detail_Repo.getdatabydateList(parsedDate);
                    totalPages = BRRS_DBS10_FINCON_II_1A_Detail_Repo.getdatacount(parsedDate);
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

        mv.setViewName("BRRS/DBS10_FINCON_II_1A");
        mv.addObject("displaymode", "Details");
        mv.addObject("currentPage", currentPage);
        System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
        mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
        mv.addObject("reportsflag", "reportsflag");
        mv.addObject("menu", reportId);
        return mv;
    }


	
	public byte[] getDBS10_FINCON_II_1AExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelDBS10_FINCON_II_1AARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<DBS10_FINCON_II_1A_Summary_Entity> dataList = BRRS_DBS10_FINCON_II_1A_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for DBS10_FINCON_II_1A report. Returning empty result.");
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

			int startRow = 4;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					DBS10_FINCON_II_1A_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R5
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR5_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR5_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

			
					// R6
					// Column D
					row = sheet.getRow(5);
					
					cell3 = row.getCell(3);
					if (record.getR6_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR6_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R7
					// Column D
					row = sheet.getRow(6);
					
					cell3 = row.getCell(3);
					if (record.getR7_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR7_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R8
					// Column D
					row = sheet.getRow(7);
					
					cell3 = row.getCell(3);
					if (record.getR8_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR8_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R9
					// Column D
					row = sheet.getRow(8);
					
					cell3 = row.getCell(3);
					if (record.getR9_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR9_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R10
					// Column D
					row = sheet.getRow(9);
					
					cell3 = row.getCell(3);
					if (record.getR10_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR10_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R11
					// Column D
					row = sheet.getRow(10);
					
					cell3 = row.getCell(3);
					if (record.getR11_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR11_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R12
					// Column D
					row = sheet.getRow(11);
					
					cell3 = row.getCell(3);
					if (record.getR12_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR12_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R14
					// Column D
					row = sheet.getRow(13);
					
					cell3 = row.getCell(3);
					if (record.getR14_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR14_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R15
					// Column D
					row = sheet.getRow(14);
					
					cell3 = row.getCell(3);
					if (record.getR15_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR15_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R16
					// Column D
					row = sheet.getRow(15);
					
					cell3 = row.getCell(3);
					if (record.getR16_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR16_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					// R17
					// Column D
					row = sheet.getRow(16);
					
					cell3 = row.getCell(3);
					if (record.getR17_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR17_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R18
					// Column D
					row = sheet.getRow(17);
					
					cell3 = row.getCell(3);
					if (record.getR18_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR18_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R19
					// Column D
					row = sheet.getRow(18);
					
					cell3 = row.getCell(3);
					if (record.getR19_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR19_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					// R20
					// Column D
					row = sheet.getRow(19);
					
					cell3 = row.getCell(3);
					if (record.getR20_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR20_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R21
					// Column D
					row = sheet.getRow(20);
					
					cell3 = row.getCell(3);
					if (record.getR21_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR21_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R22
					// Column D
					row = sheet.getRow(21);
					
					cell3 = row.getCell(3);
					if (record.getR22_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR22_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R23
					// Column D
					row = sheet.getRow(22);
					
					cell3 = row.getCell(3);
					if (record.getR23_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR23_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R24
					// Column D
					row = sheet.getRow(23);
					
					cell3 = row.getCell(3);
					if (record.getR24_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR24_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R25
					// Column D
					row = sheet.getRow(24);
					
					cell3 = row.getCell(3);
					if (record.getR25_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR25_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R26
					// Column D
					row = sheet.getRow(25);
					
					cell3 = row.getCell(3);
					if (record.getR26_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR26_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R27
					// Column D
					row = sheet.getRow(26);
					
					cell3 = row.getCell(3);
					if (record.getR27_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR27_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R28
					// Column D
					row = sheet.getRow(27);
					
					cell3 = row.getCell(3);
					if (record.getR28_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR28_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R31
					// Column D
					row = sheet.getRow(30);
					
					cell3 = row.getCell(3);
					if (record.getR31_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR31_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R32
					// Column D
					row = sheet.getRow(31);
					
					cell3 = row.getCell(3);
					if (record.getR32_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR32_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R34
					// Column D
					row = sheet.getRow(33);
					
					cell3 = row.getCell(3);
					if (record.getR34_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR34_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R35
					// Column D
					row = sheet.getRow(34);
					
					cell3 = row.getCell(3);
					if (record.getR35_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR35_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R36
					// Column D
					row = sheet.getRow(35);
					
					cell3 = row.getCell(3);
					if (record.getR36_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR36_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R37
					// Column D
					row = sheet.getRow(36);
					
					cell3 = row.getCell(3);
					if (record.getR37_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR37_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R38
					// Column D
					row = sheet.getRow(37);
					
					cell3 = row.getCell(3);
					if (record.getR38_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR38_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R39
					// Column D
					row = sheet.getRow(38);
					
					cell3 = row.getCell(3);
					if (record.getR39_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR39_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R40
					// Column D
					row = sheet.getRow(39);
					
					cell3 = row.getCell(3);
					if (record.getR40_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR40_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R41
					// Column D
					row = sheet.getRow(40);
					
					cell3 = row.getCell(3);
					if (record.getR41_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR41_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R42
					// Column D
					row = sheet.getRow(41);
					
					cell3 = row.getCell(3);
					if (record.getR42_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR42_AMOUNT_X010().doubleValue());
						
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

	public byte[] getDBS10_FINCON_II_1ADetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for DBS10_FINCON_II_1A Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("DBS10_FINCON_II_1ADetails");

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
			List<DBS10_FINCON_II_1A_Detail_Entity> reportData = BRRS_DBS10_FINCON_II_1A_Detail_Repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (DBS10_FINCON_II_1A_Detail_Entity item : reportData) {
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

					row.createCell(4).setCellValue(item.getReportLable());
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
				logger.info("No data found for DBS10_FINCON_II_1A — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating DBS10_FINCON_II_1A Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getDBS10_FINCON_II_1AArchival() {
		List<Object> DBS10_FINCON_II_1AArchivallist = new ArrayList<>();
		try {
			DBS10_FINCON_II_1AArchivallist = BRRS_DBS10_FINCON_II_1A_Archival_Summary_Repo.getDBS10_FINCON_II_1Aarchival();
			System.out.println("countser" + DBS10_FINCON_II_1AArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching DBS10_FINCON_II_1A Archival data: " + e.getMessage());
			e.printStackTrace();

		}
		return DBS10_FINCON_II_1AArchivallist;
	}

	public byte[] getExcelDBS10_FINCON_II_1AARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<DBS10_FINCON_II_1A_Archival_Summary_Entity> dataList = BRRS_DBS10_FINCON_II_1A_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for DBS10_FINCON_II_1A report. Returning empty result.");
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
			int startRow = 4;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					DBS10_FINCON_II_1A_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					// R5
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR5_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR5_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

			
					// R6
					// Column D
					row = sheet.getRow(5);
					
					cell3 = row.getCell(3);
					if (record.getR6_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR6_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R7
					// Column D
					row = sheet.getRow(6);
					
					cell3 = row.getCell(3);
					if (record.getR7_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR7_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R8
					// Column D
					row = sheet.getRow(7);
					
					cell3 = row.getCell(3);
					if (record.getR8_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR8_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R9
					// Column D
					row = sheet.getRow(8);
					
					cell3 = row.getCell(3);
					if (record.getR9_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR9_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R10
					// Column D
					row = sheet.getRow(9);
					
					cell3 = row.getCell(3);
					if (record.getR10_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR10_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R11
					// Column D
					row = sheet.getRow(10);
					
					cell3 = row.getCell(3);
					if (record.getR11_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR11_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R12
					// Column D
					row = sheet.getRow(11);
					
					cell3 = row.getCell(3);
					if (record.getR12_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR12_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R14
					// Column D
					row = sheet.getRow(13);
					
					cell3 = row.getCell(3);
					if (record.getR14_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR14_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R15
					// Column D
					row = sheet.getRow(14);
					
					cell3 = row.getCell(3);
					if (record.getR15_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR15_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R16
					// Column D
					row = sheet.getRow(15);
					
					cell3 = row.getCell(3);
					if (record.getR16_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR16_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					// R17
					// Column D
					row = sheet.getRow(16);
					
					cell3 = row.getCell(3);
					if (record.getR17_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR17_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R18
					// Column D
					row = sheet.getRow(17);
					
					cell3 = row.getCell(3);
					if (record.getR18_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR18_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R19
					// Column D
					row = sheet.getRow(18);
					
					cell3 = row.getCell(3);
					if (record.getR19_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR19_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					
					// R20
					// Column D
					row = sheet.getRow(19);
					
					cell3 = row.getCell(3);
					if (record.getR20_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR20_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R21
					// Column D
					row = sheet.getRow(20);
					
					cell3 = row.getCell(3);
					if (record.getR21_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR21_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R22
					// Column D
					row = sheet.getRow(21);
					
					cell3 = row.getCell(3);
					if (record.getR22_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR22_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R23
					// Column D
					row = sheet.getRow(22);
					
					cell3 = row.getCell(3);
					if (record.getR23_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR23_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R24
					// Column D
					row = sheet.getRow(23);
					
					cell3 = row.getCell(3);
					if (record.getR24_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR24_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R25
					// Column D
					row = sheet.getRow(24);
					
					cell3 = row.getCell(3);
					if (record.getR25_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR25_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R26
					// Column D
					row = sheet.getRow(25);
					
					cell3 = row.getCell(3);
					if (record.getR26_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR26_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R27
					// Column D
					row = sheet.getRow(26);
					
					cell3 = row.getCell(3);
					if (record.getR27_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR27_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R28
					// Column D
					row = sheet.getRow(27);
					
					cell3 = row.getCell(3);
					if (record.getR28_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR28_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R31
					// Column D
					row = sheet.getRow(30);
					
					cell3 = row.getCell(3);
					if (record.getR31_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR31_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R32
					// Column D
					row = sheet.getRow(31);
					
					cell3 = row.getCell(3);
					if (record.getR32_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR32_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R34
					// Column D
					row = sheet.getRow(33);
					
					cell3 = row.getCell(3);
					if (record.getR34_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR34_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R35
					// Column D
					row = sheet.getRow(34);
					
					cell3 = row.getCell(3);
					if (record.getR35_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR35_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R36
					// Column D
					row = sheet.getRow(35);
					
					cell3 = row.getCell(3);
					if (record.getR36_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR36_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R37
					// Column D
					row = sheet.getRow(36);
					
					cell3 = row.getCell(3);
					if (record.getR37_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR37_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R38
					// Column D
					row = sheet.getRow(37);
					
					cell3 = row.getCell(3);
					if (record.getR38_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR38_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R39
					// Column D
					row = sheet.getRow(38);
					
					cell3 = row.getCell(3);
					if (record.getR39_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR39_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R40
					// Column D
					row = sheet.getRow(39);
					
					cell3 = row.getCell(3);
					if (record.getR40_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR40_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R41
					// Column D
					row = sheet.getRow(40);
					
					cell3 = row.getCell(3);
					if (record.getR41_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR41_AMOUNT_X010().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					
					// R42
					// Column D
					row = sheet.getRow(41);
					
					cell3 = row.getCell(3);
					if (record.getR42_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR42_AMOUNT_X010().doubleValue());
						
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

	
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_DBS10_FINCON_II_1A ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("DBS10_FINCON_II_1ADetails");

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
			List<DBS10_FINCON_II_1A_Archival_Detail_Entity> reportData = BRRS_DBS10_FINCON_II_1A_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (DBS10_FINCON_II_1A_Archival_Detail_Entity item : reportData) {
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

					row.createCell(4).setCellValue(item.getReportLable());
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
				logger.info("No data found for DBS10_FINCON_II_1A — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating DBS10_FINCON_II_1A Excel", e);
			return new byte[0];
		}
	}

	
	@Autowired
	private BRRS_DBS10_FINCON_II_1A_Detail_Repo DBS10_FINCON_II_1A_Detail_Repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/DBS10_FINCON_II_1A"); 

		if (acctNo != null) {
			DBS10_FINCON_II_1A_Detail_Entity mDBS10_FINCON_II_1AEntity = DBS10_FINCON_II_1A_Detail_Repo.findByAcctnumber(acctNo);
			if (mDBS10_FINCON_II_1AEntity != null && mDBS10_FINCON_II_1AEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(mDBS10_FINCON_II_1AEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", mDBS10_FINCON_II_1AEntity);
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

			DBS10_FINCON_II_1A_Detail_Entity existing = DBS10_FINCON_II_1A_Detail_Repo.findByAcctnumber(acctNo);
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
				DBS10_FINCON_II_1A_Detail_Repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_DBS10_FINCON_II_1A_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_DBS10_FINCON_II_1A_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating DBS10_FINCON_II_1A record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}


}
