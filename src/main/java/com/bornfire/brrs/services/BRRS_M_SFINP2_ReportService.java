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

import com.bornfire.brrs.entities.M_SFINP2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Archival_Detail_Repo;
import com.bornfire.brrs.entities.M_SFINP2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.M_SFINP2_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Detail_Repo;
import com.bornfire.brrs.entities.M_SFINP2_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Summary_Repo;

import java.math.BigDecimal;

@Component
@Service
public class BRRS_M_SFINP2_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SFINP2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_M_SFINP2_Detail_Repo M_SFINP2_DETAIL_Repo;

	@Autowired
	BRRS_M_SFINP2_Summary_Repo M_SFINP2_Summary_Repo;

	@Autowired
	BRRS_M_SFINP2_Archival_Detail_Repo M_SFINP2_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SFINP2_Archival_Summary_Repo M_SFINP2_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SFINP2View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_SFINP2_Archival_Summary_Entity> T1Master = new ArrayList<M_SFINP2_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_SFINP2_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
			List<M_SFINP2_Summary_Entity> T1Master = new ArrayList<M_SFINP2_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_SFINP2_Summary_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_SFINP2");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getM_SFINP2currentDtl(String reportId, String fromdate, String todate, String currency,
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
	            List<M_SFINP2_Archival_Detail_Entity> T1Dt1;
	            if (rowId != null && columnId != null) {
	                T1Dt1 = M_SFINP2_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
	            } else {
	                T1Dt1 = M_SFINP2_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
	            }

	            mv.addObject("reportdetails", T1Dt1);
	            mv.addObject("reportmaster12", T1Dt1);
	            System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

	        } else {
	            // 🔹 Current branch
	            List<M_SFINP2_Detail_Entity> T1Dt1;
	            if (rowId != null && columnId != null) {
	                T1Dt1 = M_SFINP2_DETAIL_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
	            } else {
	                T1Dt1 = M_SFINP2_DETAIL_Repo.getdatabydateList(parsedDate, currentPage, pageSize);
	                totalPages = M_SFINP2_DETAIL_Repo.getdatacount(parsedDate);
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
	    mv.setViewName("BRRS/M_SFINP2");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("currentPage", currentPage);
	    System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	    mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	    mv.addObject("reportsflag", "reportsflag");
	    mv.addObject("menu", reportId);

	    return mv;
	}


	public byte[] BRRS_M_SFINP2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_SFINP2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_SFINP2_Summary_Entity> dataList = M_SFINP2_Summary_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SFINP2 report. Returning empty result.");
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
			int startRow = 10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SFINP2_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row11
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR11_MONTH_END() != null) {
						cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR11_AVERAGE() != null) {
						cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR12_MONTH_END() != null) {
						cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					cell3 = row.createCell(3);
					if (record.getR12_AVERAGE() != null) {
						cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row13
					row = sheet.getRow(12);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR13_MONTH_END() != null) {
						cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_AVERAGE() != null) {
						cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR14_MONTH_END() != null) {
						cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_AVERAGE() != null) {
						cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR15_MONTH_END() != null) {
						cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_AVERAGE() != null) {
						cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR16_MONTH_END() != null) {
						cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_AVERAGE() != null) {
						cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row19
					row = sheet.getRow(18);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR19_MONTH_END() != null) {
						cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR20_MONTH_END() != null) {
						cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_AVERAGE() != null) {
						cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR21_MONTH_END() != null) {
						cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_AVERAGE() != null) {
						cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR23_MONTH_END() != null) {
						cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_AVERAGE() != null) {
						cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR24_MONTH_END() != null) {
						cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_AVERAGE() != null) {
						cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR27_MONTH_END() != null) {
						cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_AVERAGE() != null) {
						cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR28_MONTH_END() != null) {
						cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_AVERAGE() != null) {
						cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					// row29
					// Column D
					cell3 = row.createCell(3);
					if (record.getR29_AVERAGE() != null) {
						cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row30
					row = sheet.getRow(29);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR30_MONTH_END() != null) {
						cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					// Column D
					cell3 = row.createCell(3);
					if (record.getR30_AVERAGE() != null) {
						cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row31
					row = sheet.getRow(30);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR31_MONTH_END() != null) {
						cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_AVERAGE() != null) {
						cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row34
					row = sheet.getRow(33);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR34_MONTH_END() != null) {
						cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row34
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_AVERAGE() != null) {
						cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row35
					row = sheet.getRow(34);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR35_MONTH_END() != null) {
						cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_AVERAGE() != null) {
						cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row36
					row = sheet.getRow(35);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR36_MONTH_END() != null) {
						cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row36
					// Column D
					cell3 = row.createCell(3);
					if (record.getR36_AVERAGE() != null) {
						cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR37_MONTH_END() != null) {
						cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_AVERAGE() != null) {
						cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR38_MONTH_END() != null) {
						cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row38
					// Column D
					cell3 = row.createCell(3);
					if (record.getR38_AVERAGE() != null) {
						cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_MONTH_END() != null) {
						cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_AVERAGE() != null) {
						cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_MONTH_END() != null) {
						cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_AVERAGE() != null) {
						cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_MONTH_END() != null) {
						cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row42
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_AVERAGE() != null) {
						cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row43
					row = sheet.getRow(42);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_MONTH_END() != null) {
						cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row43
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_AVERAGE() != null) {
						cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row44
					row = sheet.getRow(43);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR44_MONTH_END() != null) {
						cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row44
					// Column D
					cell3 = row.createCell(3);
					if (record.getR44_AVERAGE() != null) {
						cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row45
					row = sheet.getRow(44);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR45_MONTH_END() != null) {
						cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row45
					// Column D
					cell3 = row.createCell(3);
					if (record.getR45_AVERAGE() != null) {
						cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_MONTH_END() != null) {
						cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row47
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_AVERAGE() != null) {
						cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row48
					row = sheet.getRow(47);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_MONTH_END() != null) {
						cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row48
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_AVERAGE() != null) {
						cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row49
					row = sheet.getRow(48);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR49_MONTH_END() != null) {
						cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row49
					// Column D
					cell3 = row.createCell(3);
					if (record.getR49_AVERAGE() != null) {
						cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row51
					row = sheet.getRow(50);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_MONTH_END() != null) {
						cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_AVERAGE() != null) {
						cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_MONTH_END() != null) {
						cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_AVERAGE() != null) {
						cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR53_MONTH_END() != null) {
						cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					// Column D
					cell3 = row.createCell(3);
					if (record.getR53_AVERAGE() != null) {
						cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_MONTH_END() != null) {
						cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_AVERAGE() != null) {
						cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_MONTH_END() != null) {
						cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_AVERAGE() != null) {
						cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR56_MONTH_END() != null) {
						cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row56
					// Column D
					cell3 = row.createCell(3);
					if (record.getR56_AVERAGE() != null) {
						cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row57
					row = sheet.getRow(56);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR57_MONTH_END() != null) {
						cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row57
					// Column D
					cell3 = row.createCell(3);
					if (record.getR57_AVERAGE() != null) {
						cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row58
					row = sheet.getRow(57);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR58_MONTH_END() != null) {
						cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row58
					// Column D
					cell3 = row.createCell(3);
					if (record.getR58_AVERAGE() != null) {
						cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR60_MONTH_END() != null) {
						cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row60
					// Column D
					cell3 = row.createCell(3);
					if (record.getR60_AVERAGE() != null) {
						cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR61_MONTH_END() != null) {
						cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row61
					// Column D
					cell3 = row.createCell(3);
					if (record.getR61_AVERAGE() != null) {
						cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR62_MONTH_END() != null) {
						cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row62
					// Column D
					cell3 = row.createCell(3);
					if (record.getR62_AVERAGE() != null) {
						cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row64
					row = sheet.getRow(63);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR64_MONTH_END() != null) {
						cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row64
					// Column D
					cell3 = row.createCell(3);
					if (record.getR64_AVERAGE() != null) {
						cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row66
					row = sheet.getRow(65);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR66_MONTH_END() != null) {
						cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row66
					// Column D
					cell3 = row.createCell(3);
					if (record.getR66_AVERAGE() != null) {
						cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row67
					row = sheet.getRow(66);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR67_MONTH_END() != null) {
						cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row67
					// Column D
					cell3 = row.createCell(3);
					if (record.getR67_AVERAGE() != null) {
						cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row69
					row = sheet.getRow(68);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR69_MONTH_END() != null) {
						cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row69
					// Column D
					cell3 = row.createCell(3);
					if (record.getR69_AVERAGE() != null) {
						cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row70
					row = sheet.getRow(69);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR70_MONTH_END() != null) {
						cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row70
					// Column D
					cell3 = row.createCell(3);
					if (record.getR70_AVERAGE() != null) {
						cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row71
					row = sheet.getRow(70);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR71_MONTH_END() != null) {
						cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row71
					// Column D
					cell3 = row.createCell(3);
					if (record.getR71_AVERAGE() != null) {
						cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(71);
					// row72
					// Column D
					cell3 = row.createCell(3);
					if (record.getR72_AVERAGE() != null) {
						cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row74
					row = sheet.getRow(73);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR74_MONTH_END() != null) {
						cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row74
					// Column D
					cell3 = row.createCell(3);
					if (record.getR74_AVERAGE() != null) {
						cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row75
					row = sheet.getRow(74);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR75_MONTH_END() != null) {
						cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row75
					// Column D
					cell3 = row.createCell(3);
					if (record.getR75_AVERAGE() != null) {
						cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row76
					row = sheet.getRow(75);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR76_MONTH_END() != null) {
						cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row76
					// Column D
					cell3 = row.createCell(3);
					if (record.getR76_AVERAGE() != null) {
						cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row77
					row = sheet.getRow(76);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR77_MONTH_END() != null) {
						cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row77
					// Column D
					cell3 = row.createCell(3);
					if (record.getR77_AVERAGE() != null) {
						cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row78
					row = sheet.getRow(77);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR78_MONTH_END() != null) {
						cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row78
					// Column D
					cell3 = row.createCell(3);
					if (record.getR78_AVERAGE() != null) {
						cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row79
					row = sheet.getRow(78);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR79_MONTH_END() != null) {
						cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row79
					// Column D
					cell3 = row.createCell(3);
					if (record.getR79_AVERAGE() != null) {
						cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SFINP2_SUMMARY", null,
						"BRRS_M_SFINP2_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	public byte[] BRRS_M_SFINP2DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_SFINP2 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_SFINP2Details");

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
			List<M_SFINP2_Detail_Entity> reportData = M_SFINP2_DETAIL_Repo.getdatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SFINP2_Detail_Entity item : reportData) {
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
				logger.info("No data found for BRRS_M_SFINP2 — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_SFINP2 Excel", e);
			return new byte[0];
		}
	}

	public List<Object> getM_SFINP2Archival() {
		List<Object> M_SFINP2Archivallist = new ArrayList<>();
		try {
			M_SFINP2Archivallist = M_SFINP2_Archival_Summary_Repo.getM_SFINP2archival();
			System.out.println("countser" + M_SFINP2Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_SFINP2 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_SFINP2Archivallist;
	}

	public byte[] getExcelM_SFINP2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_SFINP2_Archival_Summary_Entity> dataList = M_SFINP2_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SFINP2 report. Returning empty result.");
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
					M_SFINP2_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row11
					// Column C
					Cell cell2 = row.createCell(2);
					if (record.getR11_MONTH_END() != null) {
						cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row11
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR11_AVERAGE() != null) {
						cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR12_MONTH_END() != null) {
						cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row12
					// Column D
					cell3 = row.createCell(3);
					if (record.getR12_AVERAGE() != null) {
						cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row13
					row = sheet.getRow(12);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR13_MONTH_END() != null) {
						cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row13
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_AVERAGE() != null) {
						cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR14_MONTH_END() != null) {
						cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row14
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_AVERAGE() != null) {
						cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR15_MONTH_END() != null) {
						cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row15
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_AVERAGE() != null) {
						cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR16_MONTH_END() != null) {
						cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row16
					// Column D
					cell3 = row.createCell(3);
					if (record.getR16_AVERAGE() != null) {
						cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row19
					row = sheet.getRow(18);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR19_MONTH_END() != null) {
						cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR20_MONTH_END() != null) {
						cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row20
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_AVERAGE() != null) {
						cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR21_MONTH_END() != null) {
						cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row21
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_AVERAGE() != null) {
						cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR23_MONTH_END() != null) {
						cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_AVERAGE() != null) {
						cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR24_MONTH_END() != null) {
						cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_AVERAGE() != null) {
						cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR27_MONTH_END() != null) {
						cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row27
					// Column D
					cell3 = row.createCell(3);
					if (record.getR27_AVERAGE() != null) {
						cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR28_MONTH_END() != null) {
						cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row28
					// Column D
					cell3 = row.createCell(3);
					if (record.getR28_AVERAGE() != null) {
						cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					// row29
					// Column D
					cell3 = row.createCell(3);
					if (record.getR29_AVERAGE() != null) {
						cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row30
					row = sheet.getRow(29);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR30_MONTH_END() != null) {
						cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row30
					// Column D
					cell3 = row.createCell(3);
					if (record.getR30_AVERAGE() != null) {
						cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row31
					row = sheet.getRow(30);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR31_MONTH_END() != null) {
						cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row31
					// Column D
					cell3 = row.createCell(3);
					if (record.getR31_AVERAGE() != null) {
						cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row34
					row = sheet.getRow(33);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR34_MONTH_END() != null) {
						cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row34
					// Column D
					cell3 = row.createCell(3);
					if (record.getR34_AVERAGE() != null) {
						cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row35
					row = sheet.getRow(34);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR35_MONTH_END() != null) {
						cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row35
					// Column D
					cell3 = row.createCell(3);
					if (record.getR35_AVERAGE() != null) {
						cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row36
					row = sheet.getRow(35);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR36_MONTH_END() != null) {
						cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row36
					// Column D
					cell3 = row.createCell(3);
					if (record.getR36_AVERAGE() != null) {
						cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR37_MONTH_END() != null) {
						cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row37
					// Column D
					cell3 = row.createCell(3);
					if (record.getR37_AVERAGE() != null) {
						cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR38_MONTH_END() != null) {
						cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row38
					// Column D
					cell3 = row.createCell(3);
					if (record.getR38_AVERAGE() != null) {
						cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR39_MONTH_END() != null) {
						cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row39
					// Column D
					cell3 = row.createCell(3);
					if (record.getR39_AVERAGE() != null) {
						cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR40_MONTH_END() != null) {
						cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row40
					// Column D
					cell3 = row.createCell(3);
					if (record.getR40_AVERAGE() != null) {
						cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR42_MONTH_END() != null) {
						cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row42
					// Column D
					cell3 = row.createCell(3);
					if (record.getR42_AVERAGE() != null) {
						cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row43
					row = sheet.getRow(42);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR43_MONTH_END() != null) {
						cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row43
					// Column D
					cell3 = row.createCell(3);
					if (record.getR43_AVERAGE() != null) {
						cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row44
					row = sheet.getRow(43);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR44_MONTH_END() != null) {
						cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row44
					// Column D
					cell3 = row.createCell(3);
					if (record.getR44_AVERAGE() != null) {
						cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row45
					row = sheet.getRow(44);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR45_MONTH_END() != null) {
						cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row45
					// Column D
					cell3 = row.createCell(3);
					if (record.getR45_AVERAGE() != null) {
						cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR47_MONTH_END() != null) {
						cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row47
					// Column D
					cell3 = row.createCell(3);
					if (record.getR47_AVERAGE() != null) {
						cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row48
					row = sheet.getRow(47);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR48_MONTH_END() != null) {
						cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row48
					// Column D
					cell3 = row.createCell(3);
					if (record.getR48_AVERAGE() != null) {
						cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row49
					row = sheet.getRow(48);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR49_MONTH_END() != null) {
						cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row49
					// Column D
					cell3 = row.createCell(3);
					if (record.getR49_AVERAGE() != null) {
						cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row51
					row = sheet.getRow(50);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR51_MONTH_END() != null) {
						cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row51
					// Column D
					cell3 = row.createCell(3);
					if (record.getR51_AVERAGE() != null) {
						cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row52
					row = sheet.getRow(51);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR52_MONTH_END() != null) {
						cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row52
					// Column D
					cell3 = row.createCell(3);
					if (record.getR52_AVERAGE() != null) {
						cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR53_MONTH_END() != null) {
						cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row53
					// Column D
					cell3 = row.createCell(3);
					if (record.getR53_AVERAGE() != null) {
						cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR54_MONTH_END() != null) {
						cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row54
					// Column D
					cell3 = row.createCell(3);
					if (record.getR54_AVERAGE() != null) {
						cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR55_MONTH_END() != null) {
						cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row55
					// Column D
					cell3 = row.createCell(3);
					if (record.getR55_AVERAGE() != null) {
						cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR56_MONTH_END() != null) {
						cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row56
					// Column D
					cell3 = row.createCell(3);
					if (record.getR56_AVERAGE() != null) {
						cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row57
					row = sheet.getRow(56);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR57_MONTH_END() != null) {
						cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row57
					// Column D
					cell3 = row.createCell(3);
					if (record.getR57_AVERAGE() != null) {
						cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row58
					row = sheet.getRow(57);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR58_MONTH_END() != null) {
						cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row58
					// Column D
					cell3 = row.createCell(3);
					if (record.getR58_AVERAGE() != null) {
						cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR60_MONTH_END() != null) {
						cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row60
					// Column D
					cell3 = row.createCell(3);
					if (record.getR60_AVERAGE() != null) {
						cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR61_MONTH_END() != null) {
						cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row61
					// Column D
					cell3 = row.createCell(3);
					if (record.getR61_AVERAGE() != null) {
						cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR62_MONTH_END() != null) {
						cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row62
					// Column D
					cell3 = row.createCell(3);
					if (record.getR62_AVERAGE() != null) {
						cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row64
					row = sheet.getRow(63);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR64_MONTH_END() != null) {
						cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row64
					// Column D
					cell3 = row.createCell(3);
					if (record.getR64_AVERAGE() != null) {
						cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row66
					row = sheet.getRow(65);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR66_MONTH_END() != null) {
						cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row66
					// Column D
					cell3 = row.createCell(3);
					if (record.getR66_AVERAGE() != null) {
						cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row67
					row = sheet.getRow(66);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR67_MONTH_END() != null) {
						cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row67
					// Column D
					cell3 = row.createCell(3);
					if (record.getR67_AVERAGE() != null) {
						cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row69
					row = sheet.getRow(68);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR69_MONTH_END() != null) {
						cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row69
					// Column D
					cell3 = row.createCell(3);
					if (record.getR69_AVERAGE() != null) {
						cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row70
					row = sheet.getRow(69);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR70_MONTH_END() != null) {
						cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row70
					// Column D
					cell3 = row.createCell(3);
					if (record.getR70_AVERAGE() != null) {
						cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row71
					row = sheet.getRow(70);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR71_MONTH_END() != null) {
						cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row71
					// Column D
					cell3 = row.createCell(3);
					if (record.getR71_AVERAGE() != null) {
						cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(71);
					// row72
					// Column D
					cell3 = row.createCell(3);
					if (record.getR72_AVERAGE() != null) {
						cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row74
					row = sheet.getRow(73);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR74_MONTH_END() != null) {
						cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row74
					// Column D
					cell3 = row.createCell(3);
					if (record.getR74_AVERAGE() != null) {
						cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row75
					row = sheet.getRow(74);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR75_MONTH_END() != null) {
						cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row75
					// Column D
					cell3 = row.createCell(3);
					if (record.getR75_AVERAGE() != null) {
						cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row76
					row = sheet.getRow(75);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR76_MONTH_END() != null) {
						cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row76
					// Column D
					cell3 = row.createCell(3);
					if (record.getR76_AVERAGE() != null) {
						cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row77
					row = sheet.getRow(76);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR77_MONTH_END() != null) {
						cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row77
					// Column D
					cell3 = row.createCell(3);
					if (record.getR77_AVERAGE() != null) {
						cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row78
					row = sheet.getRow(77);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR78_MONTH_END() != null) {
						cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row78
					// Column D
					cell3 = row.createCell(3);
					if (record.getR78_AVERAGE() != null) {
						cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row79
					row = sheet.getRow(78);
					// Column C
					cell2 = row.createCell(2);
					if (record.getR79_MONTH_END() != null) {
						cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// row79
					// Column D
					cell3 = row.createCell(3);
					if (record.getR79_AVERAGE() != null) {
						cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
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
			logger.info("Generating Excel for BRRS_M_SFINP2 ARCHIVAL Details...");
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
			List<M_SFINP2_Archival_Detail_Entity> reportData = M_SFINP2_Archival_Detail_Repo
					.getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SFINP2_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for BRRS_M_SFINP2 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_SFINP2Excel", e);
			return new byte[0];
		}
	}

}
