package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.M_LA3_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_LA3_Archival_Detail_Repo;
import com.bornfire.brrs.entities.M_LA3_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_LA3_Archival_Summary_Repo;
import com.bornfire.brrs.entities.M_LA3_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_LA3_Detail_Repo;
import com.bornfire.brrs.entities.M_LA3_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_LA3_Summary_Repo;

import java.math.BigDecimal;

@Component
@Service
public class BRRS_M_LA3_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LA3_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_M_LA3_Detail_Repo M_LA3_DETAIL_Repo;

	@Autowired
	BRRS_M_LA3_Summary_Repo M_LA3_Summary_Repo;

	@Autowired
	BRRS_M_LA3_Archival_Detail_Repo M_LA3_Archival_Detail_Repo;

	@Autowired
	BRRS_M_LA3_Archival_Summary_Repo M_LA3_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_LA3View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_LA3_Archival_Summary_Entity> T1Master = new ArrayList<M_LA3_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_LA3_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
			List<M_LA3_Summary_Entity> T1Master = new ArrayList<M_LA3_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_LA3_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_LA3");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getM_LA3currentDtl(String reportId, String fromdate, String todate, String currency,
	        String dtltype, Pageable pageable, String filter, String type, String version) {

	    int pageSize = pageable != null ? pageable.getPageSize() : 10;
	    int currentPage = pageable != null ? pageable.getPageNumber() : 0;
	    int totalPages = 0;

	    ModelAndView mv = new ModelAndView();
	    Session hs = sessionFactory.getCurrentSession();

	    try {
	        Date parsedDate = null;
	        if (todate != null && !todate.isEmpty()) {
	            parsedDate = dateformat.parse(todate);
	        }

	        String rowId = null;
	        String columnId = null;

	        // ✅ Split filter string into rowId & columnId
	        if (filter != null && filter.contains(",")) {
	            String[] parts = filter.split(",");
	            if (parts.length >= 2) {
	                rowId = parts[0];
	                columnId = parts[1];
	            }
	        }

	        if ("ARCHIVAL".equals(type) && version != null) {
	            // 🔹 Archival branch
	            List<M_LA3_Archival_Detail_Entity> T1Dt1;
	            if (rowId != null && columnId != null) {
	                T1Dt1 = M_LA3_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
	            } else {
	                T1Dt1 = M_LA3_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
	                mv.addObject("pagination", "YES");
	            }

	            mv.addObject("reportdetails", T1Dt1);
	            mv.addObject("reportmaster12", T1Dt1);
	            System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

	        } else {
	            // 🔹 Current branch
	            List<M_LA3_Detail_Entity> T1Dt1;
	            if (rowId != null && columnId != null) {
	                T1Dt1 = M_LA3_DETAIL_Repo.getdatabydateListrow( parsedDate, columnId,rowId);
	            } else {
	                T1Dt1 = M_LA3_DETAIL_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
	                totalPages = M_LA3_DETAIL_Repo.getdatacount(parsedDate);
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

	    // ✅ Common attributes
	    mv.setViewName("BRRS/M_LA3");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("currentPage", currentPage);
	    System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	    mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	    mv.addObject("reportsflag", "reportsflag");
	    mv.addObject("menu", reportId);

	    return mv;
	}


	public byte[] BRRS_M_LA3Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_LA3ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_LA3_Summary_Entity> dataList = M_LA3_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA3 report. Returning empty result.");
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
int startRow = 9;
			
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LA3_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					Cell cell1 = row.createCell(1);
					if (record.getR10_no_of_ac() != null) {
						cell1.setCellValue(record.getR10_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					Cell cell2 = row.createCell(2);
					if (record.getR10_approved_limit() != null) {
						cell2.setCellValue(record.getR10_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					Cell cell3 = row.createCell(3);
					if (record.getR10_amount_outstanding() != null) {
						cell3.setCellValue(record.getR10_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// ====== R11 ======
					row = sheet.getRow(10);
					if (row == null) {
					    row = sheet.createRow(10);
					}
					cell1 = row.createCell(1);
					if (record.getR11_no_of_ac() != null) {
					    cell1.setCellValue(record.getR11_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR11_approved_limit() != null) {
					    cell2.setCellValue(record.getR11_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR11_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR11_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R12 ======
					row = sheet.getRow(11);
					if (row == null) {
					    row = sheet.createRow(11);
					}
					cell1 = row.createCell(1);
					if (record.getR12_no_of_ac() != null) {
					    cell1.setCellValue(record.getR12_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_approved_limit() != null) {
					    cell2.setCellValue(record.getR12_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR12_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R13 ======
					row = sheet.getRow(12);
					if (row == null) {
					    row = sheet.createRow(12);
					}
					cell1 = row.createCell(1);
					if (record.getR13_no_of_ac() != null) {
					    cell1.setCellValue(record.getR13_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_approved_limit() != null) {
					    cell2.setCellValue(record.getR13_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR13_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R14 ======
					row = sheet.getRow(13);
					if (row == null) {
					    row = sheet.createRow(13);
					}
					cell1 = row.createCell(1);
					if (record.getR14_no_of_ac() != null) {
					    cell1.setCellValue(record.getR14_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_approved_limit() != null) {
					    cell2.setCellValue(record.getR14_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR14_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R15 ======
					row = sheet.getRow(14);
					if (row == null) {
					    row = sheet.createRow(14);
					}
					cell1 = row.createCell(1);
					if (record.getR15_no_of_ac() != null) {
					    cell1.setCellValue(record.getR15_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR15_approved_limit() != null) {
					    cell2.setCellValue(record.getR15_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR15_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					// ====== R21 ======
					row = sheet.getRow(20);
					if (row == null) {
					    row = sheet.createRow(20);
					}
					cell1 = row.createCell(1);
					if (record.getR21_no_of_ac() != null) {
					    cell1.setCellValue(record.getR21_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR21_approved_limit() != null) {
					    cell2.setCellValue(record.getR21_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR21_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR21_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R22 ======
					row = sheet.getRow(21);
					if (row == null) {
					    row = sheet.createRow(21);
					}
					cell1 = row.createCell(1);
					if (record.getR22_no_of_ac() != null) {
					    cell1.setCellValue(record.getR22_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR22_approved_limit() != null) {
					    cell2.setCellValue(record.getR22_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR22_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR22_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R23 ======
					row = sheet.getRow(22);
					if (row == null) {
					    row = sheet.createRow(22);
					}
					cell1 = row.createCell(1);
					if (record.getR23_no_of_ac() != null) {
					    cell1.setCellValue(record.getR23_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR23_approved_limit() != null) {
					    cell2.setCellValue(record.getR23_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR23_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR23_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R24 ======
					row = sheet.getRow(23);
					if (row == null) {
					    row = sheet.createRow(23);
					}
					cell1 = row.createCell(1);
					if (record.getR24_no_of_ac() != null) {
					    cell1.setCellValue(record.getR24_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR24_approved_limit() != null) {
					    cell2.setCellValue(record.getR24_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR24_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR24_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R25 ======
					row = sheet.getRow(24);
					if (row == null) {
					    row = sheet.createRow(24);
					}
					cell1 = row.createCell(1);
					if (record.getR25_no_of_ac() != null) {
					    cell1.setCellValue(record.getR25_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR25_approved_limit() != null) {
					    cell2.setCellValue(record.getR25_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR25_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR25_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R26 ======
					row = sheet.getRow(25);
					if (row == null) {
					    row = sheet.createRow(25);
					}
					cell1 = row.createCell(1);
					if (record.getR26_no_of_ac() != null) {
					    cell1.setCellValue(record.getR26_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR26_approved_limit() != null) {
					    cell2.setCellValue(record.getR26_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR26_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR26_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R27 ======
					row = sheet.getRow(26);
					if (row == null) {
					    row = sheet.createRow(26);
					}
					cell1 = row.createCell(1);
					if (record.getR27_no_of_ac() != null) {
					    cell1.setCellValue(record.getR27_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR27_approved_limit() != null) {
					    cell2.setCellValue(record.getR27_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR27_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR27_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R28 ======
					row = sheet.getRow(27);
					if (row == null) {
					    row = sheet.createRow(27);
					}
					cell1 = row.createCell(1);
					if (record.getR28_no_of_ac() != null) {
					    cell1.setCellValue(record.getR28_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR28_approved_limit() != null) {
					    cell2.setCellValue(record.getR28_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR28_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR28_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R29 ======
					row = sheet.getRow(28);
					if (row == null) {
					    row = sheet.createRow(28);
					}
					cell1 = row.createCell(1);
					if (record.getR29_no_of_ac() != null) {
					    cell1.setCellValue(record.getR29_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR29_approved_limit() != null) {
					    cell2.setCellValue(record.getR29_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR29_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR29_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R30 ======
					row = sheet.getRow(29);
					if (row == null) {
					    row = sheet.createRow(29);
					}
					cell1 = row.createCell(1);
					if (record.getR30_no_of_ac() != null) {
					    cell1.setCellValue(record.getR30_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR30_approved_limit() != null) {
					    cell2.setCellValue(record.getR30_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR30_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR30_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					// ====== R37 ======
					row = sheet.getRow(36);
					if (row == null) {
					    row = sheet.createRow(36);
					}
					cell1 = row.createCell(1);
					if (record.getR37_no_of_ac() != null) {
					    cell1.setCellValue(record.getR37_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR37_credit_limit() != null) {
					    cell2.setCellValue(record.getR37_credit_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR37_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR37_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R38 ======
					row = sheet.getRow(37);
					if (row == null) {
					    row = sheet.createRow(37);
					}
					cell1 = row.createCell(1);
					if (record.getR38_no_of_ac() != null) {
					    cell1.setCellValue(record.getR38_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR38_credit_limit() != null) {
					    cell2.setCellValue(record.getR38_credit_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR38_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR38_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					// ====== R40 ======
					row = sheet.getRow(39);
					if (row == null) {
					    row = sheet.createRow(39);
					}
					cell1 = row.createCell(1);
					if (record.getR40_no_of_ac() != null) {
					    cell1.setCellValue(record.getR40_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR40_credit_limit() != null) {
					    cell2.setCellValue(record.getR40_credit_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR40_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR40_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R41 ======
					row = sheet.getRow(40);
					if (row == null) {
					    row = sheet.createRow(40);
					}
					cell1 = row.createCell(1);
					if (record.getR41_no_of_ac() != null) {
					    cell1.setCellValue(record.getR41_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR41_credit_limit() != null) {
					    cell2.setCellValue(record.getR41_credit_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR41_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR41_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
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
			// audit
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA3_SUMMARY", null,
						"BRRS_M_LA3_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	public byte[] BRRS_M_LA3DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_LA3 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_LA3Details");

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
			List<M_LA3_Detail_Entity> reportData = M_LA3_DETAIL_Repo.getdatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA3_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
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
				logger.info("No data found for BRRS_M_LA3 — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_LA3 Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getM_LA3Archival() {
		List<Object> M_LA3Archivallist = new ArrayList<>();
		try {
			M_LA3Archivallist = M_LA3_Archival_Summary_Repo.getM_LA3archival();
			System.out.println("countser" + M_LA3Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_LA3 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_LA3Archivallist;
	}

	public byte[] getExcelM_LA3ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_LA3_Archival_Summary_Entity> dataList = M_LA3_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA3 report. Returning empty result.");
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

int startRow = 9;
			
			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LA3_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber="+startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					Cell cell1 = row.createCell(1);
					if (record.getR10_no_of_ac() != null) {
						cell1.setCellValue(record.getR10_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					Cell cell2 = row.createCell(2);
					if (record.getR10_approved_limit() != null) {
						cell2.setCellValue(record.getR10_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					Cell cell3 = row.createCell(3);
					if (record.getR10_amount_outstanding() != null) {
						cell3.setCellValue(record.getR10_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// ====== R11 ======
					row = sheet.getRow(10);
					if (row == null) {
					    row = sheet.createRow(10);
					}
					cell1 = row.createCell(1);
					if (record.getR11_no_of_ac() != null) {
					    cell1.setCellValue(record.getR11_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR11_approved_limit() != null) {
					    cell2.setCellValue(record.getR11_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR11_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR11_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R12 ======
					row = sheet.getRow(11);
					if (row == null) {
					    row = sheet.createRow(11);
					}
					cell1 = row.createCell(1);
					if (record.getR12_no_of_ac() != null) {
					    cell1.setCellValue(record.getR12_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_approved_limit() != null) {
					    cell2.setCellValue(record.getR12_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR12_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R13 ======
					row = sheet.getRow(12);
					if (row == null) {
					    row = sheet.createRow(12);
					}
					cell1 = row.createCell(1);
					if (record.getR13_no_of_ac() != null) {
					    cell1.setCellValue(record.getR13_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_approved_limit() != null) {
					    cell2.setCellValue(record.getR13_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR13_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R14 ======
					row = sheet.getRow(13);
					if (row == null) {
					    row = sheet.createRow(13);
					}
					cell1 = row.createCell(1);
					if (record.getR14_no_of_ac() != null) {
					    cell1.setCellValue(record.getR14_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_approved_limit() != null) {
					    cell2.setCellValue(record.getR14_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR14_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R15 ======
					row = sheet.getRow(14);
					if (row == null) {
					    row = sheet.createRow(14);
					}
					cell1 = row.createCell(1);
					if (record.getR15_no_of_ac() != null) {
					    cell1.setCellValue(record.getR15_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR15_approved_limit() != null) {
					    cell2.setCellValue(record.getR15_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR15_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					// ====== R21 ======
					row = sheet.getRow(20);
					if (row == null) {
					    row = sheet.createRow(20);
					}
					cell1 = row.createCell(1);
					if (record.getR21_no_of_ac() != null) {
					    cell1.setCellValue(record.getR21_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR21_approved_limit() != null) {
					    cell2.setCellValue(record.getR21_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR21_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR21_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R22 ======
					row = sheet.getRow(21);
					if (row == null) {
					    row = sheet.createRow(21);
					}
					cell1 = row.createCell(1);
					if (record.getR22_no_of_ac() != null) {
					    cell1.setCellValue(record.getR22_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR22_approved_limit() != null) {
					    cell2.setCellValue(record.getR22_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR22_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR22_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R23 ======
					row = sheet.getRow(22);
					if (row == null) {
					    row = sheet.createRow(22);
					}
					cell1 = row.createCell(1);
					if (record.getR23_no_of_ac() != null) {
					    cell1.setCellValue(record.getR23_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR23_approved_limit() != null) {
					    cell2.setCellValue(record.getR23_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR23_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR23_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R24 ======
					row = sheet.getRow(23);
					if (row == null) {
					    row = sheet.createRow(23);
					}
					cell1 = row.createCell(1);
					if (record.getR24_no_of_ac() != null) {
					    cell1.setCellValue(record.getR24_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR24_approved_limit() != null) {
					    cell2.setCellValue(record.getR24_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR24_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR24_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R25 ======
					row = sheet.getRow(24);
					if (row == null) {
					    row = sheet.createRow(24);
					}
					cell1 = row.createCell(1);
					if (record.getR25_no_of_ac() != null) {
					    cell1.setCellValue(record.getR25_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR25_approved_limit() != null) {
					    cell2.setCellValue(record.getR25_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR25_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR25_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R26 ======
					row = sheet.getRow(25);
					if (row == null) {
					    row = sheet.createRow(25);
					}
					cell1 = row.createCell(1);
					if (record.getR26_no_of_ac() != null) {
					    cell1.setCellValue(record.getR26_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR26_approved_limit() != null) {
					    cell2.setCellValue(record.getR26_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR26_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR26_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R27 ======
					row = sheet.getRow(26);
					if (row == null) {
					    row = sheet.createRow(26);
					}
					cell1 = row.createCell(1);
					if (record.getR27_no_of_ac() != null) {
					    cell1.setCellValue(record.getR27_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR27_approved_limit() != null) {
					    cell2.setCellValue(record.getR27_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR27_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR27_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R28 ======
					row = sheet.getRow(27);
					if (row == null) {
					    row = sheet.createRow(27);
					}
					cell1 = row.createCell(1);
					if (record.getR28_no_of_ac() != null) {
					    cell1.setCellValue(record.getR28_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR28_approved_limit() != null) {
					    cell2.setCellValue(record.getR28_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR28_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR28_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R29 ======
					row = sheet.getRow(28);
					if (row == null) {
					    row = sheet.createRow(28);
					}
					cell1 = row.createCell(1);
					if (record.getR29_no_of_ac() != null) {
					    cell1.setCellValue(record.getR29_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR29_approved_limit() != null) {
					    cell2.setCellValue(record.getR29_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR29_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR29_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R30 ======
					row = sheet.getRow(29);
					if (row == null) {
					    row = sheet.createRow(29);
					}
					cell1 = row.createCell(1);
					if (record.getR30_no_of_ac() != null) {
					    cell1.setCellValue(record.getR30_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR30_approved_limit() != null) {
					    cell2.setCellValue(record.getR30_approved_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR30_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR30_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					// ====== R37 ======
					row = sheet.getRow(36);
					if (row == null) {
					    row = sheet.createRow(36);
					}
					cell1 = row.createCell(1);
					if (record.getR37_no_of_ac() != null) {
					    cell1.setCellValue(record.getR37_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR37_credit_limit() != null) {
					    cell2.setCellValue(record.getR37_credit_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR37_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR37_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R38 ======
					row = sheet.getRow(37);
					if (row == null) {
					    row = sheet.createRow(37);
					}
					cell1 = row.createCell(1);
					if (record.getR38_no_of_ac() != null) {
					    cell1.setCellValue(record.getR38_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR38_credit_limit() != null) {
					    cell2.setCellValue(record.getR38_credit_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR38_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR38_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}
					
					// ====== R40 ======
					row = sheet.getRow(39);
					if (row == null) {
					    row = sheet.createRow(39);
					}
					cell1 = row.createCell(1);
					if (record.getR40_no_of_ac() != null) {
					    cell1.setCellValue(record.getR40_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR40_credit_limit() != null) {
					    cell2.setCellValue(record.getR40_credit_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR40_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR40_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
					} else {
					    cell3.setCellValue("");
					    cell3.setCellStyle(textStyle);
					}

					// ====== R41 ======
					row = sheet.getRow(40);
					if (row == null) {
					    row = sheet.createRow(40);
					}
					cell1 = row.createCell(1);
					if (record.getR41_no_of_ac() != null) {
					    cell1.setCellValue(record.getR41_no_of_ac().doubleValue());
					    cell1.setCellStyle(numberStyle);
					} else {
					    cell1.setCellValue("");
					    cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR41_credit_limit() != null) {
					    cell2.setCellValue(record.getR41_credit_limit().doubleValue());
					    cell2.setCellStyle(numberStyle);
					} else {
					    cell2.setCellValue("");
					    cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR41_amount_outstanding() != null) {
					    cell3.setCellValue(record.getR41_amount_outstanding().doubleValue());
					    cell3.setCellStyle(numberStyle);
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
			logger.info("Generating Excel for BRRS_M_LA3 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MLA3Detail");

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
			List<M_LA3_Archival_Detail_Entity> reportData = M_LA3_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA3_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
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
				logger.info("No data found for BRRS_M_LA3 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_LA3Excel", e);
			return new byte[0];
		}
	}

}
