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

import com.bornfire.brrs.entities.CBUAE_BRF1_1_Detail_Entity;
import com.bornfire.brrs.entities.CBUAE_BRF1_1_Detail_Repo;
import com.bornfire.brrs.entities.CBUAE_BRF1_1_Summary_Archival_Entity;
import com.bornfire.brrs.entities.CBUAE_BRF1_1_Summary_Archival_Repo;
import com.bornfire.brrs.entities.CBUAE_BRF1_1_Summary_Entity;
import com.bornfire.brrs.entities.CBUAE_BRF1_1_Summary_Repo;
import com.bornfire.brrs.entities.CBUAE_BRF1_1__Archival_Detail_Entity;
import com.bornfire.brrs.entities.CBUAE_BRF1_1__Archival_Detail_Repo;

@Component
@Service
public class CBUAE_BRF1_1_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(CBUAE_BRF1_1_ReportService.class);

	@Autowired
	CBUAE_BRF1_1__Archival_Detail_Repo archival_detail_repo;

	@Autowired
	CBUAE_BRF1_1_Summary_Archival_Repo BRF1_1_Summary_Archival_Repo;

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	CBUAE_BRF1_1_Detail_Repo BRF1_1_DETAIL_Repo;

	@Autowired
	CBUAE_BRF1_1_Summary_Repo BRF1_1REPORT_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRF1_1View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, String version) {
		System.out.println("getBRF1_1View");
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<CBUAE_BRF1_1_Summary_Archival_Entity> T1Master = new ArrayList<CBUAE_BRF1_1_Summary_Archival_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRF1_1_Summary_Archival_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		} else {
			List<CBUAE_BRF1_1_Summary_Entity> T1Master = new ArrayList<CBUAE_BRF1_1_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRF1_1REPORT_Repo.getdatabydateList(dateformat.parse(todate));

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);

		mv.setViewName("BRF/BRF1_1");

		// mv.addObject("reportmaster", T1Master);
		mv.addObject("displaymode", "summary");
		// mv.addObject("reportsflag", "reportsflag");
		// mv.addObject("menu", reportId);
		System.out.println("scv" + mv.getViewName());

		return mv;

	}

	public ModelAndView getBRF1_1currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		ModelAndView mv = new ModelAndView();

		Session hs = sessionFactory.getCurrentSession();

		if (type.equals("ARCHIVAL") & version != null) {
			List<CBUAE_BRF1_1__Archival_Detail_Entity> T1Dt1 = new ArrayList<CBUAE_BRF1_1__Archival_Detail_Entity>();
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
					T1Dt1 = archival_detail_repo.GetDataByRowIdAndColumnId(rowId, columnId, dateformat.parse(todate),
							version);

					System.out.println("countavd" + T1Dt1.size());
				} else {

					T1Dt1 = archival_detail_repo.getdatabydateList(dateformat.parse(todate), version);
					System.out.println("countavd" + T1Dt1.size());
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);

			} catch (ParseException e) {
				e.printStackTrace();
			}

		} else {
			List<CBUAE_BRF1_1_Detail_Entity> T1Dt1 = new ArrayList<CBUAE_BRF1_1_Detail_Entity>();
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
					T1Dt1 = BRF1_1_DETAIL_Repo.GetDataByRowIdAndColumnId(rowId, columnId, dateformat.parse(todate));
				} else {
					T1Dt1 = BRF1_1_DETAIL_Repo.getdatabydateList(dateformat.parse(todate));
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("LISTCOUNT" + T1Dt1.size());

			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		// Page<Object> T1Dt1Page = new PageImpl<Object>(pagedlist,
		// PageRequest.of(currentPage, pageSize), T1Dt1.size());
		mv.setViewName("BRF/BRF1_1");
		mv.addObject("displaymode", "Details");
		// mv.addObject("reportdetails", T1Dt1Page.getContent());

		// mv.addObject("reportmaster1", qr);
		// mv.addObject("singledetail", new T1CurProdDetail());
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}

	public byte[] getBRF1_1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {
			byte[] ARCHIVALreport = getBRF1_1ExcelARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype,
					type, version);
			return ARCHIVALreport;
		}

		List<CBUAE_BRF1_1_Summary_Entity> dataList = BRF1_1REPORT_Repo.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRF1.1 report. Returning empty result.");
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

			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					CBUAE_BRF1_1_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row13
					// Column 5:
					Cell cell5 = row.createCell(5);
					if (record.getR0020_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0020_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					Cell cell7 = row.createCell(7);
					if (record.getR0020_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0020_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					Cell cell9 = row.createCell(9);
					if (record.getR0020_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0020_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					Cell cell11 = row.createCell(11);
					if (record.getR0020_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0020_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0030_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0030_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0030_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0030_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0030_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0030_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0030_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0030_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0040_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0040_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0040_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0040_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0040_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0040_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0040_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0040_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0050_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0050_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0050_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0050_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0050_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0050_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0050_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0050_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0060_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0060_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0060_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0060_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0060_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0060_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0060_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0060_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0070_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0070_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0070_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0070_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0070_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0070_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0070_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0070_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0080_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0080_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0080_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0080_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0080_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0080_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0080_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0080_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0100_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0100_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0100_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0100_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0100_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0100_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0100_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0100_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0110_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0110_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0110_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0110_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0110_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0110_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0110_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0110_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0120_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0120_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0120_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0120_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0120_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0120_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0120_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0120_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0140_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0140_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0140_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0140_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0140_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0140_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0140_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0140_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0150_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0150_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0150_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0150_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0150_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0150_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0150_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0150_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0160_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0160_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0160_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0160_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0160_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0160_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0160_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0160_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0170_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0170_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0170_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0170_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0170_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0170_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0170_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0170_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0180_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0180_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0180_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0180_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0180_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0180_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0180_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0180_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0190_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0190_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0190_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0190_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0190_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0190_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0190_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0190_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0210_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0210_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0210_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0210_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0210_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0210_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0210_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0210_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0220_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0220_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0220_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0220_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0220_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0220_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0220_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0220_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0230_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0230_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0230_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0230_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0230_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0230_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0230_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0230_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0260_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0260_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0260_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0260_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0260_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0260_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0260_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0260_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0270_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0270_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0270_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0270_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0270_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0270_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0270_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0270_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0280_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0280_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0280_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0280_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0280_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0280_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0280_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0280_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0310_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0310_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0310_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0310_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0310_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0310_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0310_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0310_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row43
					row = sheet.getRow(42);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0320_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0320_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0320_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0320_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0320_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0320_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0320_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0320_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row44
					row = sheet.getRow(43);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0330_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0330_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0330_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0330_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0330_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0330_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0330_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0330_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0350_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0350_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0350_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0350_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0350_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0350_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0350_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0350_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0360_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0360_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0360_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0360_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0360_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0360_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0360_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0360_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);

					// Column 4
					Cell cell4 = row.createCell(4);
					if (record.getR0390_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0390_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0390_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0390_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					Cell cell6 = row.createCell(6);
					if (record.getR0390_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0390_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0390_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0390_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					Cell cell8 = row.createCell(8);
					if (record.getR0390_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0390_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0390_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0390_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					Cell cell10 = row.createCell(10);
					if (record.getR0390_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0390_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0390_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0390_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0400_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0400_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0390_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0390_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0390_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0390_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0390_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0390_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0390_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0390_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0390_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0390_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0390_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0390_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0390_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0390_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0400_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0400_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0400_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0400_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0400_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0400_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0400_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0400_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0400_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0400_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0400_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0400_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0400_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0400_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0400_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0400_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0420_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0420_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0420_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0420_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0420_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0420_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0420_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0420_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0420_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0420_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0420_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0420_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0420_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0420_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0420_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0420_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0430_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0430_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0430_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0430_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0430_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0430_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0430_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0430_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0430_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0430_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0430_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0430_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0430_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0430_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0430_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0430_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0450_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0450_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0450_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0450_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0450_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0450_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0450_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0450_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0450_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0450_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0450_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0450_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0450_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0450_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0450_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0450_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row57
					row = sheet.getRow(56);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0460_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0460_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0460_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0460_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0460_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0460_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0460_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0460_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0460_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0460_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0460_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0460_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0460_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0460_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0460_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0460_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0480_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0480_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0480_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0480_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0480_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0480_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0480_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0480_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0480_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0480_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0480_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0480_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0480_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0480_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0480_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0480_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0490_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0490_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0490_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0490_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0490_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0490_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0490_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0490_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0490_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0490_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0490_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0490_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0490_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0490_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0490_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0490_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row63
					row = sheet.getRow(62);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0520_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0520_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0520_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0520_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0520_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0520_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0520_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0520_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0520_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0520_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0520_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0520_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0520_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0520_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0520_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0520_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row64
					row = sheet.getRow(63);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0530_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0530_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0530_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0530_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0530_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0530_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0530_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0530_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0530_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0530_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0530_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0530_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0530_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0530_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0530_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0530_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row65
					row = sheet.getRow(64);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0540_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0540_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0540_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0540_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0540_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0540_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0540_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0540_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0540_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0540_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0540_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0540_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0540_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0540_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0540_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0540_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row66
					row = sheet.getRow(65);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0550_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0550_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0550_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0550_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0550_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0550_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0550_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0550_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0550_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0550_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0550_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0550_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0550_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0550_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0550_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0550_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row67
					row = sheet.getRow(66);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0560_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0560_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0560_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0560_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0560_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0560_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0560_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0560_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0560_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0560_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0560_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0560_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0560_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0560_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0560_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0560_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row69
					row = sheet.getRow(68);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0580_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0580_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0580_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0580_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0580_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0580_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0580_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0580_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0580_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0580_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0580_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0580_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0580_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0580_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0580_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0580_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row70
					row = sheet.getRow(69);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0590_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0590_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0590_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0590_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0590_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0590_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0590_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0590_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0590_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0590_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0590_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0590_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0590_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0590_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0590_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0590_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row71
					row = sheet.getRow(70);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0600_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0600_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0600_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0600_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0600_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0600_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0600_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0600_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0600_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0600_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0600_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0600_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0600_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0600_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0600_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0600_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row72
					row = sheet.getRow(71);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0610_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0610_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0610_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0610_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0610_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0610_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0610_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0610_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0610_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0610_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0610_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0610_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0610_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0610_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0610_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0610_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row73
					row = sheet.getRow(72);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0620_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0620_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0620_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0620_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0620_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0620_NO_ACCT_FCY_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0620_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0620_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0620_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0620_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0620_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0620_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0620_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0620_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0620_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0620_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row75
					row = sheet.getRow(74);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0640_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0640_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0640_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0640_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0640_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0640_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0640_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0640_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row76
					row = sheet.getRow(75);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0650_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0650_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0650_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0650_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0650_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0650_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0650_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0650_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row77
					row = sheet.getRow(76);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0660_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0660_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0660_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0660_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0660_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0660_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0660_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0660_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row78
					row = sheet.getRow(77);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0670_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0670_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0670_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0670_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0670_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0670_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0670_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0670_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row79
					row = sheet.getRow(78);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0680_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0680_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0680_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0680_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0680_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0680_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0680_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0680_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row80
					row = sheet.getRow(79);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0690_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0690_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0690_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0690_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0690_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0690_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0690_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0690_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row82
					row = sheet.getRow(81);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0710_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0710_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0710_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0710_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0710_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0710_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0710_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0710_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row83
					row = sheet.getRow(82);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0720_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0720_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0720_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0720_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0720_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0720_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0720_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0720_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row84
					row = sheet.getRow(83);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0730_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0730_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0730_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0730_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0730_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0730_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0730_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0730_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row85
					row = sheet.getRow(84);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0740_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0740_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0740_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0740_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0740_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0740_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0740_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0740_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row86
					row = sheet.getRow(85);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0750_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0750_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0750_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0750_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0750_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0750_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0750_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0750_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row87
					row = sheet.getRow(86);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0760_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0760_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0760_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0760_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0760_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0760_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0760_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0760_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row88
					row = sheet.getRow(87);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0770_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0770_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0770_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0770_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0770_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0770_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0770_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0770_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row89
					row = sheet.getRow(88);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0780_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0780_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0780_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0780_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0780_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0780_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0780_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0780_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row91
					row = sheet.getRow(90);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0800_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0800_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0800_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0800_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0800_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0800_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0800_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0800_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row92
					row = sheet.getRow(91);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0810_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0810_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0810_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0810_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0810_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0810_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0810_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0810_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row93
					row = sheet.getRow(92);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0820_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0820_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0820_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0820_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0820_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0820_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0820_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0820_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row95
					row = sheet.getRow(94);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0840_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0840_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0840_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0840_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0840_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0840_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0840_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0840_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row96
					row = sheet.getRow(95);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0850_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0850_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0850_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0850_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0850_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0850_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0850_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0850_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row97
					row = sheet.getRow(96);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0860_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0860_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0860_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0860_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0860_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0860_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0860_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0860_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row98
					row = sheet.getRow(97);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0870_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0870_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0870_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0870_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0870_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0870_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0870_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0870_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row99
					row = sheet.getRow(98);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0880_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0880_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0880_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0880_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0880_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0880_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0880_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0880_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row100
					row = sheet.getRow(99);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0890_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0890_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0890_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0890_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0890_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0890_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0890_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0890_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row101
					row = sheet.getRow(100);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0900_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0900_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0900_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0900_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0900_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0900_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0900_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0900_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
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

	public List<Object> getBRF1_1Archival() {
		List<Object> BRF1_1Archivallist = new ArrayList<>();
		try {
			BRF1_1Archivallist = BRF1_1_Summary_Archival_Repo.getbrf1_1archival();
			System.out.println("countser" + BRF1_1Archivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching BRF1_1 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return BRF1_1Archivallist;
	}

	public byte[] getBRF1_1ExcelARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<CBUAE_BRF1_1_Summary_Archival_Entity> dataList = BRF1_1_Summary_Archival_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRF1.1 report. Returning empty result.");
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

			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					CBUAE_BRF1_1_Summary_Archival_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row13
					// Column 5:
					Cell cell5 = row.createCell(5);
					if (record.getR0020_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0020_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					Cell cell7 = row.createCell(7);
					if (record.getR0020_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0020_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					Cell cell9 = row.createCell(9);
					if (record.getR0020_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0020_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					Cell cell11 = row.createCell(11);
					if (record.getR0020_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0020_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0030_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0030_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0030_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0030_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0030_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0030_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0030_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0030_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0040_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0040_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0040_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0040_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0040_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0040_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0040_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0040_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0050_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0050_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0050_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0050_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0050_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0050_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0050_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0050_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0060_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0060_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0060_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0060_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0060_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0060_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0060_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0060_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0070_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0070_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0070_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0070_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0070_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0070_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0070_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0070_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0080_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0080_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0080_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0080_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0080_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0080_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0080_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0080_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0100_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0100_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0100_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0100_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0100_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0100_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0100_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0100_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0110_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0110_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0110_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0110_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0110_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0110_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0110_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0110_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0120_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0120_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0120_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0120_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0120_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0120_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0120_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0120_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0140_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0140_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0140_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0140_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0140_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0140_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0140_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0140_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0150_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0150_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0150_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0150_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0150_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0150_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0150_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0150_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0160_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0160_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0160_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0160_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0160_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0160_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0160_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0160_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0170_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0170_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0170_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0170_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0170_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0170_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0170_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0170_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0180_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0180_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0180_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0180_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0180_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0180_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0180_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0180_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0190_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0190_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0190_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0190_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0190_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0190_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0190_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0190_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0210_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0210_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0210_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0210_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0210_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0210_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0210_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0210_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0220_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0220_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0220_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0220_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0220_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0220_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0220_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0220_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0230_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0230_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0230_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0230_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0230_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0230_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0230_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0230_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0260_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0260_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0260_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0260_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0260_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0260_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0260_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0260_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0270_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0270_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0270_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0270_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0270_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0270_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0270_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0270_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0280_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0280_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0280_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0280_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0280_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0280_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0280_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0280_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0310_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0310_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0310_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0310_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0310_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0310_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0310_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0310_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row43
					row = sheet.getRow(42);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0320_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0320_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0320_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0320_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0320_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0320_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0320_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0320_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row44
					row = sheet.getRow(43);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0330_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0330_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0330_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0330_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0330_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0330_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0330_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0330_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0350_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0350_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0350_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0350_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0350_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0350_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0350_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0350_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);
					// Column 5: Date
					cell5 = row.createCell(5);
					if (record.getR0360_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0360_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7: Date
					cell7 = row.createCell(7);
					if (record.getR0360_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0360_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9: Date
					cell9 = row.createCell(9);
					if (record.getR0360_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0360_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11: Date
					cell11 = row.createCell(11);
					if (record.getR0360_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0360_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);

					// Column 4
					Cell cell4 = row.createCell(4);
					if (record.getR0390_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0390_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0390_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0390_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					Cell cell6 = row.createCell(6);
					if (record.getR0390_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0390_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0390_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0390_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					Cell cell8 = row.createCell(8);
					if (record.getR0390_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0390_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0390_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0390_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					Cell cell10 = row.createCell(10);
					if (record.getR0390_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0390_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0390_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0390_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0400_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0400_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0390_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0390_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0390_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0390_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0390_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0390_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0390_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0390_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0390_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0390_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0390_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0390_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0390_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0390_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0400_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0400_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0400_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0400_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0400_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0400_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0400_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0400_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0400_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0400_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0400_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0400_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0400_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0400_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0400_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0400_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0420_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0420_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0420_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0420_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0420_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0420_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0420_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0420_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0420_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0420_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0420_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0420_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0420_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0420_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0420_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0420_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0430_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0430_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0430_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0430_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0430_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0430_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0430_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0430_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0430_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0430_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0430_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0430_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0430_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0430_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0430_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0430_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row56
					row = sheet.getRow(55);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0450_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0450_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0450_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0450_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0450_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0450_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0450_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0450_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0450_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0450_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0450_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0450_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0450_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0450_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0450_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0450_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row57
					row = sheet.getRow(56);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0460_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0460_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0460_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0460_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0460_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0460_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0460_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0460_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0460_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0460_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0460_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0460_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0460_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0460_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0460_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0460_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0480_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0480_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0480_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0480_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0480_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0480_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0480_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0480_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0480_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0480_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0480_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0480_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0480_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0480_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0480_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0480_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0490_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0490_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0490_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0490_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0490_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0490_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0490_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0490_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0490_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0490_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0490_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0490_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0490_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0490_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0490_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0490_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row63
					row = sheet.getRow(62);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0520_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0520_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0520_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0520_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0520_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0520_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0520_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0520_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0520_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0520_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0520_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0520_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0520_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0520_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0520_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0520_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row64
					row = sheet.getRow(63);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0530_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0530_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0530_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0530_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0530_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0530_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0530_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0530_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0530_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0530_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0530_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0530_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0530_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0530_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0530_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0530_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row65
					row = sheet.getRow(64);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0540_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0540_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0540_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0540_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0540_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0540_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0540_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0540_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0540_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0540_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0540_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0540_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0540_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0540_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0540_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0540_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row66
					row = sheet.getRow(65);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0550_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0550_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0550_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0550_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0550_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0550_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0550_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0550_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0550_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0550_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0550_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0550_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0550_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0550_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0550_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0550_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row67
					row = sheet.getRow(66);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0560_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0560_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0560_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0560_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0560_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0560_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0560_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0560_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0560_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0560_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0560_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0560_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0560_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0560_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0560_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0560_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row69
					row = sheet.getRow(68);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0580_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0580_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0580_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0580_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0580_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0580_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0580_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0580_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0580_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0580_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0580_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0580_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0580_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0580_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0580_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0580_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row70
					row = sheet.getRow(69);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0590_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0590_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0590_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0590_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0590_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0590_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0590_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0590_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0590_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0590_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0590_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0590_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0590_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0590_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0590_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0590_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row71
					row = sheet.getRow(70);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0600_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0600_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0600_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0600_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0600_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0600_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0600_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0600_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0600_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0600_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0600_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0600_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0600_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0600_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0600_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0600_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row72
					row = sheet.getRow(71);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0610_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0610_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0610_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0610_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0610_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0610_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0610_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0610_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0610_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0610_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0610_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0610_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0610_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0610_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0610_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0610_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row73
					row = sheet.getRow(72);

					// Column 4
					cell4 = row.createCell(4);
					if (record.getR0620_NO_ACCT_AED_RESIDENT() != null) {
						cell4.setCellValue(record.getR0620_NO_ACCT_AED_RESIDENT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0620_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0620_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 6
					cell6 = row.createCell(6);
					if (record.getR0620_NO_ACCT_FCY_RESIDENT() != null) {
						cell6.setCellValue(record.getR0620_NO_ACCT_AED_RESIDENT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0620_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0620_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 8
					cell8 = row.createCell(8);
					if (record.getR0620_NO_ACCT_AED_NON_RESIDENT() != null) {
						cell8.setCellValue(record.getR0620_NO_ACCT_AED_NON_RESIDENT().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0620_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0620_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 10
					cell10 = row.createCell(10);
					if (record.getR0620_NO_ACCT_FCY_NON_RESIDENT() != null) {
						cell10.setCellValue(record.getR0620_NO_ACCT_FCY_NON_RESIDENT().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0620_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0620_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row75
					row = sheet.getRow(74);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0640_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0640_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0640_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0640_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0640_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0640_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0640_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0640_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row76
					row = sheet.getRow(75);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0650_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0650_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0650_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0650_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0650_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0650_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0650_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0650_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row77
					row = sheet.getRow(76);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0660_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0660_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0660_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0660_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0660_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0660_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0660_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0660_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row78
					row = sheet.getRow(77);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0670_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0670_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0670_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0670_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0670_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0670_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0670_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0670_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row79
					row = sheet.getRow(78);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0680_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0680_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0680_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0680_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0680_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0680_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0680_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0680_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row80
					row = sheet.getRow(79);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0690_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0690_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0690_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0690_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0690_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0690_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0690_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0690_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row82
					row = sheet.getRow(81);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0710_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0710_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0710_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0710_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0710_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0710_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0710_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0710_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row83
					row = sheet.getRow(82);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0720_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0720_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0720_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0720_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0720_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0720_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0720_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0720_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row84
					row = sheet.getRow(83);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0730_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0730_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0730_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0730_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0730_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0730_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0730_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0730_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row85
					row = sheet.getRow(84);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0740_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0740_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0740_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0740_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0740_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0740_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0740_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0740_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row86
					row = sheet.getRow(85);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0750_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0750_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0750_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0750_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0750_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0750_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0750_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0750_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row87
					row = sheet.getRow(86);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0760_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0760_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0760_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0760_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0760_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0760_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0760_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0760_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row88
					row = sheet.getRow(87);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0770_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0770_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0770_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0770_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0770_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0770_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0770_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0770_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row89
					row = sheet.getRow(88);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0780_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0780_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0780_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0780_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0780_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0780_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0780_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0780_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row91
					row = sheet.getRow(90);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0800_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0800_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0800_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0800_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0800_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0800_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0800_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0800_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row92
					row = sheet.getRow(91);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0810_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0810_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0810_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0810_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0810_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0810_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0810_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0810_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row93
					row = sheet.getRow(92);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0820_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0820_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0820_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0820_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0820_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0820_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0820_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0820_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row95
					row = sheet.getRow(94);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0840_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0840_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0840_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0840_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0840_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0840_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0840_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0840_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row96
					row = sheet.getRow(95);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0850_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0850_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0850_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0850_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0850_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0850_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0850_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0850_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row97
					row = sheet.getRow(96);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0860_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0860_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0860_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0860_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0860_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0860_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0860_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0860_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row98
					row = sheet.getRow(97);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0870_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0870_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0870_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0870_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0870_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0870_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0870_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0870_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row99
					row = sheet.getRow(98);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0880_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0880_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0880_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0880_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0880_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0880_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0880_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0880_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row100
					row = sheet.getRow(99);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0890_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0890_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0890_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0890_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0890_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0890_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0890_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0890_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// row101
					row = sheet.getRow(100);

					// Column 5
					cell5 = row.createCell(5);
					if (record.getR0900_AMOUNT_AED_RESIDENT() != null) {
						cell5.setCellValue(record.getR0900_AMOUNT_AED_RESIDENT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Column 7
					cell7 = row.createCell(7);
					if (record.getR0900_AMOUNT_FCY_RESIDENT() != null) {
						cell7.setCellValue(record.getR0900_AMOUNT_FCY_RESIDENT().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Column 9
					cell9 = row.createCell(9);
					if (record.getR0900_AMOUNT_AED_NON_RESIDENT() != null) {
						cell9.setCellValue(record.getR0900_AMOUNT_AED_NON_RESIDENT().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Column 11
					cell11 = row.createCell(11);
					if (record.getR0900_AMOUNT_FCY_NON_RESIDENT() != null) {
						cell11.setCellValue(record.getR0900_AMOUNT_FCY_NON_RESIDENT().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
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

	// DetailExcel
	public byte[] getBRF1_1DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for BRF1_1 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getBRF1_1DetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRF1_1Details");

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
			List<CBUAE_BRF1_1_Detail_Entity> reportData = BRF1_1_DETAIL_Repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (CBUAE_BRF1_1_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInAed() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInAed().doubleValue());
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
				logger.info("No data found for BRF1_1 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRF1_1 Excel", e);
			return new byte[0];
		}
	}

	public byte[] getBRF1_1DetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRF1_1 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRF1_1Details");

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
			List<CBUAE_BRF1_1__Archival_Detail_Entity> reportData = archival_detail_repo.getdatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (CBUAE_BRF1_1__Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInAed() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInAed().doubleValue());
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
				logger.info("No data found for BRF1_1 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRF1_1 Excel", e);
			return new byte[0];
		}
	}

}
