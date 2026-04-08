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

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
/*import org.apache.poi.ss.usermodel.FillPatternType;*/
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
/*import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;*/
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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


import com.bornfire.brrs.entities.BRRS_DEFERRED_TAX_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_DEFERRED_TAX_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_DEFERRED_TAX_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_DEFERRED_TAX_Summary_Repo;
import com.bornfire.brrs.entities.DEFERRED_TAX_Archival_Detail_Entity;
import com.bornfire.brrs.entities.DEFERRED_TAX_Archival_Summary_Entity;
import com.bornfire.brrs.entities.DEFERRED_TAX_Detail_Entity;
import com.bornfire.brrs.entities.DEFERRED_TAX_Summary_Entity;

@Component
@Service

public class BRRS_DEFERRED_TAX_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_DEFERRED_TAX_ReportService.class);

	

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	
	@Autowired 
	BRRS_DEFERRED_TAX_Summary_Repo DTAX_summary_repo;
	 
	@Autowired
	BRRS_DEFERRED_TAX_Archival_Summary_Repo DTAX_Archival_Summary_Repo;
	
	@Autowired
	BRRS_DEFERRED_TAX_Detail_Repo DTAX_detail_repo;
	
	
	@Autowired
	BRRS_DEFERRED_TAX_Archival_Detail_Repo DTAX_Archival_Detail_Repo;
	  
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	
	public ModelAndView getDTAXView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		

		System.out.println("testing");
		System.out.println(version);

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {

		    System.out.println("ARCHIVAL MODE");
		    System.out.println("version = " + version);

		    List<DEFERRED_TAX_Archival_Summary_Entity> T1Master = new ArrayList<>();

		 
		    try {
		        Date dt = dateformat.parse(todate);

		        T1Master = DTAX_Archival_Summary_Repo.getdatabydateListarchival(dt, version);

		        System.out.println("T1Master size = " + T1Master.size());
		      

		    } catch (ParseException e) {
		        e.printStackTrace();
		    }

		    mv.addObject("reportsummary", T1Master);

		 
		} else {

			List<DEFERRED_TAX_Summary_Entity> T1Master = new ArrayList<DEFERRED_TAX_Summary_Entity>();

	
			try {
				Date d1 = dateformat.parse(todate);
				
				T1Master = DTAX_summary_repo.getdatabydateList(dateformat.parse(todate));

			
				System.out.println("T1Master size " + T1Master.size());
				mv.addObject("report_date", dateformat.format(d1));

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);

		}

	

		mv.setViewName("BRRS/DTAX");

		mv.addObject("displaymode", "summary");

		System.out.println("scv" + mv.getViewName());

		return mv;

	}
	 
	
	
	public ModelAndView getDTAXcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();

		//Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;

			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLable = null;
			String reportAddlCriteria_1 = null;
			// ✅ Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLable = parts[0];
					reportAddlCriteria_1 = parts[1];
				}
			}

			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<DEFERRED_TAX_Archival_Detail_Entity> T1Dt1;
				
				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = DTAX_Archival_Detail_Repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate, version);
				} else {
					T1Dt1 = DTAX_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<DEFERRED_TAX_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					T1Dt1 = DTAX_detail_repo.GetDataByRowIdAndColumnId(reportLable, reportAddlCriteria_1, parsedDate);
				} else {
					T1Dt1 = DTAX_detail_repo.getdatabydateList(parsedDate);
					totalPages = DTAX_detail_repo.getdatacount(parsedDate);
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

	

	
		mv.setViewName("BRRS/DTAX");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		return mv;
	}
	
	
	
	

	public byte[] getDTAXExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelDTAXARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<DEFERRED_TAX_Summary_Entity> dataList = DTAX_summary_repo.getdatabydateList(dateformat.parse(todate));
	

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  DTAX report. Returning empty result.");
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
					DEFERRED_TAX_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

  Cell cellB , cellC ,cellD , cellE , cellF , cellG , cellH, cellI;
//ROW 11
                    //COLUMN B 
					  
                    cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR11_31_asse_lc() != null) {
					    cellB.setCellValue(record.getR11_31_asse_lc().doubleValue());
					} else {
					    cellB.setCellValue(0);
					}

                    
                  //COLUMN C 

					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR11_31_asse_inr() != null) {
					    cellC.setCellValue(record.getR11_31_asse_inr().doubleValue());
					} else {
					    cellC.setCellValue(0);
					}
					
					
                  //COLUMN D 
					
					
					cellD = row.getCell(3);
					if (cellD == null) cellD= row.createCell(3);
					if (record.getR11_31_liab_lc() != null) {
					    cellD.setCellValue(record.getR11_31_liab_lc().doubleValue());
					} else {
					    cellD.setCellValue(0);
					}
					
					
                  //COLUMN E
					
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR11_31_liab_inr() != null) {
					    cellE.setCellValue(record.getR11_31_liab_inr().doubleValue());
					} else {
					    cellE.setCellValue(0);
					}
					
					
                  //COLUMN F 
					
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR11_30_asse_lc() != null) {
					    cellF.setCellValue(record.getR11_30_asse_lc().doubleValue());
					} else {
					    cellF.setCellValue(0);
					}
					
					
                  //COLUMN G 
					
					
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR11_30_asse_inr() != null) {
					    cellG.setCellValue(record.getR11_30_asse_inr().doubleValue());
					} else {
					    cellG.setCellValue(0);
					}
					
					
					
                  //COLUMN H
					
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR11_30_liab_lc() != null) {
					    cellH.setCellValue(record.getR11_30_liab_lc().doubleValue());
					} else {
					    cellH.setCellValue(0);
					}
					
					
                  //COLUMN I 
					
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR11_30_liab_inr() != null) {
					    cellI.setCellValue(record.getR11_30_liab_inr().doubleValue());
					} else {
					    cellI.setCellValue(0);
					}
					
					
					// ROW 12

row = sheet.getRow(11);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR12_31_asse_lc() != null) {
cellB.setCellValue(record.getR12_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR12_31_asse_inr() != null) {
cellC.setCellValue(record.getR12_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR12_31_liab_lc() != null) {
cellD.setCellValue(record.getR12_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR12_31_liab_inr() != null) {
cellE.setCellValue(record.getR12_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR12_30_asse_lc() != null) {
cellF.setCellValue(record.getR12_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR12_30_asse_inr() != null) {
cellG.setCellValue(record.getR12_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR12_30_liab_lc() != null) {
cellH.setCellValue(record.getR12_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR12_30_liab_inr() != null) {
cellI.setCellValue(record.getR12_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 13

row = sheet.getRow(12);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR13_31_asse_lc() != null) {
cellB.setCellValue(record.getR13_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR13_31_asse_inr() != null) {
cellC.setCellValue(record.getR13_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR13_31_liab_lc() != null) {
cellD.setCellValue(record.getR13_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR13_31_liab_inr() != null) {
cellE.setCellValue(record.getR13_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR13_30_asse_lc() != null) {
cellF.setCellValue(record.getR13_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR13_30_asse_inr() != null) {
cellG.setCellValue(record.getR13_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR13_30_liab_lc() != null) {
cellH.setCellValue(record.getR13_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR13_30_liab_inr() != null) {
cellI.setCellValue(record.getR13_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 14

row = sheet.getRow(13);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR14_31_asse_lc() != null) {
cellB.setCellValue(record.getR14_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR14_31_asse_inr() != null) {
cellC.setCellValue(record.getR14_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR14_31_liab_lc() != null) {
cellD.setCellValue(record.getR14_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR14_31_liab_inr() != null) {
cellE.setCellValue(record.getR14_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR14_30_asse_lc() != null) {
cellF.setCellValue(record.getR14_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR14_30_asse_inr() != null) {
cellG.setCellValue(record.getR14_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR14_30_liab_lc() != null) {
cellH.setCellValue(record.getR14_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR14_30_liab_inr() != null) {
cellI.setCellValue(record.getR14_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}

//ROW 15

row = sheet.getRow(14);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR15_31_asse_lc() != null) {
cellB.setCellValue(record.getR15_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR15_31_asse_inr() != null) {
cellC.setCellValue(record.getR15_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR15_31_liab_lc() != null) {
cellD.setCellValue(record.getR15_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR15_31_liab_inr() != null) {
cellE.setCellValue(record.getR15_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR15_30_asse_lc() != null) {
cellF.setCellValue(record.getR15_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR15_30_asse_inr() != null) {
cellG.setCellValue(record.getR15_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR15_30_liab_lc() != null) {
cellH.setCellValue(record.getR15_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR15_30_liab_inr() != null) {
cellI.setCellValue(record.getR15_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 16

row = sheet.getRow(15);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR16_31_asse_lc() != null) {
cellB.setCellValue(record.getR16_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR16_31_asse_inr() != null) {
cellC.setCellValue(record.getR16_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR16_31_liab_lc() != null) {
cellD.setCellValue(record.getR16_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR16_31_liab_inr() != null) {
cellE.setCellValue(record.getR16_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR16_30_asse_lc() != null) {
cellF.setCellValue(record.getR16_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR16_30_asse_inr() != null) {
cellG.setCellValue(record.getR16_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR16_30_liab_lc() != null) {
cellH.setCellValue(record.getR16_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR16_30_liab_inr() != null) {
cellI.setCellValue(record.getR16_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}



//ROW 17

row = sheet.getRow(16);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR17_31_asse_lc() != null) {
cellB.setCellValue(record.getR17_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR17_31_asse_inr() != null) {
cellC.setCellValue(record.getR17_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR17_31_liab_lc() != null) {
cellD.setCellValue(record.getR17_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR17_31_liab_inr() != null) {
cellE.setCellValue(record.getR17_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR17_30_asse_lc() != null) {
cellF.setCellValue(record.getR17_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR17_30_asse_inr() != null) {
cellG.setCellValue(record.getR17_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR17_30_liab_lc() != null) {
cellH.setCellValue(record.getR17_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR17_30_liab_inr() != null) {
cellI.setCellValue(record.getR17_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 18

row = sheet.getRow(17);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR18_31_asse_lc() != null) {
cellB.setCellValue(record.getR18_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR18_31_asse_inr() != null) {
cellC.setCellValue(record.getR18_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR18_31_liab_lc() != null) {
cellD.setCellValue(record.getR18_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR18_31_liab_inr() != null) {
cellE.setCellValue(record.getR18_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR18_30_asse_lc() != null) {
cellF.setCellValue(record.getR18_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR18_30_asse_inr() != null) {
cellG.setCellValue(record.getR18_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR18_30_liab_lc() != null) {
cellH.setCellValue(record.getR18_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR18_30_liab_inr() != null) {
cellI.setCellValue(record.getR18_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}



//ROW 19

row = sheet.getRow(18);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR19_31_asse_lc() != null) {
cellB.setCellValue(record.getR19_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR19_31_asse_inr() != null) {
cellC.setCellValue(record.getR19_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR19_31_liab_lc() != null) {
cellD.setCellValue(record.getR19_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR19_31_liab_inr() != null) {
cellE.setCellValue(record.getR19_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR19_30_asse_lc() != null) {
cellF.setCellValue(record.getR19_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR19_30_asse_inr() != null) {
cellG.setCellValue(record.getR19_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR19_30_liab_lc() != null) {
cellH.setCellValue(record.getR19_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR19_30_liab_inr() != null) {
cellI.setCellValue(record.getR19_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 20

row = sheet.getRow(19);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR20_31_asse_lc() != null) {
cellB.setCellValue(record.getR20_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR20_31_asse_inr() != null) {
cellC.setCellValue(record.getR20_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR20_31_liab_lc() != null) {
cellD.setCellValue(record.getR20_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR20_31_liab_inr() != null) {
cellE.setCellValue(record.getR20_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR20_30_asse_lc() != null) {
cellF.setCellValue(record.getR20_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR20_30_asse_inr() != null) {
cellG.setCellValue(record.getR20_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR20_30_liab_lc() != null) {
cellH.setCellValue(record.getR20_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR20_30_liab_inr() != null) {
cellI.setCellValue(record.getR20_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 21

row = sheet.getRow(20);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR21_31_asse_lc() != null) {
cellB.setCellValue(record.getR21_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR21_31_asse_inr() != null) {
cellC.setCellValue(record.getR21_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR21_31_liab_lc() != null) {
cellD.setCellValue(record.getR21_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR21_31_liab_inr() != null) {
cellE.setCellValue(record.getR21_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR21_30_asse_lc() != null) {
cellF.setCellValue(record.getR21_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR21_30_asse_inr() != null) {
cellG.setCellValue(record.getR21_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR21_30_liab_lc() != null) {
cellH.setCellValue(record.getR21_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR21_30_liab_inr() != null) {
cellI.setCellValue(record.getR21_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}



//ROW 22

row = sheet.getRow(21);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR22_31_asse_lc() != null) {
cellB.setCellValue(record.getR22_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR22_31_asse_inr() != null) {
cellC.setCellValue(record.getR22_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR22_31_liab_lc() != null) {
cellD.setCellValue(record.getR22_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR22_31_liab_inr() != null) {
cellE.setCellValue(record.getR22_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR22_30_asse_lc() != null) {
cellF.setCellValue(record.getR22_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR22_30_asse_inr() != null) {
cellG.setCellValue(record.getR22_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR22_30_liab_lc() != null) {
cellH.setCellValue(record.getR22_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR22_30_liab_inr() != null) {
cellI.setCellValue(record.getR22_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 23

row = sheet.getRow(22);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR23_31_asse_lc() != null) {
cellB.setCellValue(record.getR23_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR23_31_asse_inr() != null) {
cellC.setCellValue(record.getR23_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR23_31_liab_lc() != null) {
cellD.setCellValue(record.getR23_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR23_31_liab_inr() != null) {
cellE.setCellValue(record.getR23_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR23_30_asse_lc() != null) {
cellF.setCellValue(record.getR23_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR23_30_asse_inr() != null) {
cellG.setCellValue(record.getR23_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR23_30_liab_lc() != null) {
cellH.setCellValue(record.getR23_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR23_30_liab_inr() != null) {
cellI.setCellValue(record.getR23_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 24

row = sheet.getRow(23);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR24_31_asse_lc() != null) {
cellB.setCellValue(record.getR24_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR24_31_asse_inr() != null) {
cellC.setCellValue(record.getR24_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR24_31_liab_lc() != null) {
cellD.setCellValue(record.getR24_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR24_31_liab_inr() != null) {
cellE.setCellValue(record.getR24_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR24_30_asse_lc() != null) {
cellF.setCellValue(record.getR24_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR24_30_asse_inr() != null) {
cellG.setCellValue(record.getR24_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR24_30_liab_lc() != null) {
cellH.setCellValue(record.getR24_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR24_30_liab_inr() != null) {
cellI.setCellValue(record.getR24_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
}


//ROW 25

row = sheet.getRow(24);

//COLUMN B
cellB = row.getCell(1);
if (cellB == null) cellB = row.createCell(1);
if (record.getR25_31_asse_lc() != null) {
cellB.setCellValue(record.getR25_31_asse_lc().doubleValue());
} else {
cellB.setCellValue(0);
}

//COLUMN C
cellC = row.getCell(2);
if (cellC == null) cellC = row.createCell(2);
if (record.getR25_31_asse_inr() != null) {
cellC.setCellValue(record.getR25_31_asse_inr().doubleValue());
} else {
cellC.setCellValue(0);
}

//COLUMN D
cellD = row.getCell(3);
if (cellD == null) cellD = row.createCell(3);
if (record.getR25_31_liab_lc() != null) {
cellD.setCellValue(record.getR25_31_liab_lc().doubleValue());
} else {
cellD.setCellValue(0);
}

//COLUMN E
cellE = row.getCell(4);
if (cellE == null) cellE = row.createCell(4);
if (record.getR25_31_liab_inr() != null) {
cellE.setCellValue(record.getR25_31_liab_inr().doubleValue());
} else {
cellE.setCellValue(0);
}

//COLUMN F
cellF = row.getCell(5);
if (cellF == null) cellF = row.createCell(5);
if (record.getR25_30_asse_lc() != null) {
cellF.setCellValue(record.getR25_30_asse_lc().doubleValue());
} else {
cellF.setCellValue(0);
}

//COLUMN G
cellG = row.getCell(6);
if (cellG == null) cellG = row.createCell(6);
if (record.getR25_30_asse_inr() != null) {
cellG.setCellValue(record.getR25_30_asse_inr().doubleValue());
} else {
cellG.setCellValue(0);
}

//COLUMN H
cellH = row.getCell(7);
if (cellH == null) cellH = row.createCell(7);
if (record.getR25_30_liab_lc() != null) {
cellH.setCellValue(record.getR25_30_liab_lc().doubleValue());
} else {
cellH.setCellValue(0);
}

//COLUMN I
cellI = row.getCell(8);
if (cellI == null) cellI = row.createCell(8);
if (record.getR25_30_liab_inr() != null) {
cellI.setCellValue(record.getR25_30_liab_inr().doubleValue());
} else {
cellI.setCellValue(0);
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

	
	public byte[] getExcelDTAXARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}
		
		List<DEFERRED_TAX_Archival_Summary_Entity> dataList = DTAX_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);
		


		

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for DTAX report. Returning empty result.");
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

			int startRow =10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					DEFERRED_TAX_Archival_Summary_Entity record = dataList.get(i);

				
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}


					Cell cellB , cellC ,cellD , cellE , cellF , cellG , cellH, cellI;
					//ROW 11
					                    //COLUMN B 
										  
					                    cellB = row.getCell(1);
										if (cellB == null) cellB = row.createCell(1);
										if (record.getR11_31_asse_lc() != null) {
										    cellB.setCellValue(record.getR11_31_asse_lc().doubleValue());
										} else {
										    cellB.setCellValue(0);
										}

					                    
					                  //COLUMN C 

										cellC = row.getCell(2);
										if (cellC == null) cellC = row.createCell(2);
										if (record.getR11_31_asse_inr() != null) {
										    cellC.setCellValue(record.getR11_31_asse_inr().doubleValue());
										} else {
										    cellC.setCellValue(0);
										}
										
										
					                  //COLUMN D 
										
										
										cellD = row.getCell(3);
										if (cellD == null) cellD= row.createCell(3);
										if (record.getR11_31_liab_lc() != null) {
										    cellD.setCellValue(record.getR11_31_liab_lc().doubleValue());
										} else {
										    cellD.setCellValue(0);
										}
										
										
					                  //COLUMN E
										
										cellE = row.getCell(4);
										if (cellE == null) cellE = row.createCell(4);
										if (record.getR11_31_liab_inr() != null) {
										    cellE.setCellValue(record.getR11_31_liab_inr().doubleValue());
										} else {
										    cellE.setCellValue(0);
										}
										
										
					                  //COLUMN F 
										
										cellF = row.getCell(5);
										if (cellF == null) cellF = row.createCell(5);
										if (record.getR11_30_asse_lc() != null) {
										    cellF.setCellValue(record.getR11_30_asse_lc().doubleValue());
										} else {
										    cellF.setCellValue(0);
										}
										
										
					                  //COLUMN G 
										
										
										cellG = row.getCell(6);
										if (cellG == null) cellG = row.createCell(6);
										if (record.getR11_30_asse_inr() != null) {
										    cellG.setCellValue(record.getR11_30_asse_inr().doubleValue());
										} else {
										    cellG.setCellValue(0);
										}
										
										
										
					                  //COLUMN H
										
										cellH = row.getCell(7);
										if (cellH == null) cellH = row.createCell(7);
										if (record.getR11_30_liab_lc() != null) {
										    cellH.setCellValue(record.getR11_30_liab_lc().doubleValue());
										} else {
										    cellH.setCellValue(0);
										}
										
										
					                  //COLUMN I 
										
										cellI = row.getCell(8);
										if (cellI == null) cellI = row.createCell(8);
										if (record.getR11_30_liab_inr() != null) {
										    cellI.setCellValue(record.getR11_30_liab_inr().doubleValue());
										} else {
										    cellI.setCellValue(0);
										}
										
										
										// ROW 12

					row = sheet.getRow(11);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR12_31_asse_lc() != null) {
					cellB.setCellValue(record.getR12_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR12_31_asse_inr() != null) {
					cellC.setCellValue(record.getR12_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR12_31_liab_lc() != null) {
					cellD.setCellValue(record.getR12_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR12_31_liab_inr() != null) {
					cellE.setCellValue(record.getR12_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR12_30_asse_lc() != null) {
					cellF.setCellValue(record.getR12_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR12_30_asse_inr() != null) {
					cellG.setCellValue(record.getR12_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR12_30_liab_lc() != null) {
					cellH.setCellValue(record.getR12_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR12_30_liab_inr() != null) {
					cellI.setCellValue(record.getR12_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 13

					row = sheet.getRow(12);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR13_31_asse_lc() != null) {
					cellB.setCellValue(record.getR13_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR13_31_asse_inr() != null) {
					cellC.setCellValue(record.getR13_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR13_31_liab_lc() != null) {
					cellD.setCellValue(record.getR13_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR13_31_liab_inr() != null) {
					cellE.setCellValue(record.getR13_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR13_30_asse_lc() != null) {
					cellF.setCellValue(record.getR13_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR13_30_asse_inr() != null) {
					cellG.setCellValue(record.getR13_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR13_30_liab_lc() != null) {
					cellH.setCellValue(record.getR13_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR13_30_liab_inr() != null) {
					cellI.setCellValue(record.getR13_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 14

					row = sheet.getRow(13);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR14_31_asse_lc() != null) {
					cellB.setCellValue(record.getR14_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR14_31_asse_inr() != null) {
					cellC.setCellValue(record.getR14_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR14_31_liab_lc() != null) {
					cellD.setCellValue(record.getR14_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR14_31_liab_inr() != null) {
					cellE.setCellValue(record.getR14_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR14_30_asse_lc() != null) {
					cellF.setCellValue(record.getR14_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR14_30_asse_inr() != null) {
					cellG.setCellValue(record.getR14_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR14_30_liab_lc() != null) {
					cellH.setCellValue(record.getR14_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR14_30_liab_inr() != null) {
					cellI.setCellValue(record.getR14_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}

					//ROW 15

					row = sheet.getRow(14);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR15_31_asse_lc() != null) {
					cellB.setCellValue(record.getR15_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR15_31_asse_inr() != null) {
					cellC.setCellValue(record.getR15_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR15_31_liab_lc() != null) {
					cellD.setCellValue(record.getR15_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR15_31_liab_inr() != null) {
					cellE.setCellValue(record.getR15_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR15_30_asse_lc() != null) {
					cellF.setCellValue(record.getR15_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR15_30_asse_inr() != null) {
					cellG.setCellValue(record.getR15_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR15_30_liab_lc() != null) {
					cellH.setCellValue(record.getR15_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR15_30_liab_inr() != null) {
					cellI.setCellValue(record.getR15_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 16

					row = sheet.getRow(15);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR16_31_asse_lc() != null) {
					cellB.setCellValue(record.getR16_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR16_31_asse_inr() != null) {
					cellC.setCellValue(record.getR16_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR16_31_liab_lc() != null) {
					cellD.setCellValue(record.getR16_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR16_31_liab_inr() != null) {
					cellE.setCellValue(record.getR16_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR16_30_asse_lc() != null) {
					cellF.setCellValue(record.getR16_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR16_30_asse_inr() != null) {
					cellG.setCellValue(record.getR16_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR16_30_liab_lc() != null) {
					cellH.setCellValue(record.getR16_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR16_30_liab_inr() != null) {
					cellI.setCellValue(record.getR16_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}



					//ROW 17

					row = sheet.getRow(16);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR17_31_asse_lc() != null) {
					cellB.setCellValue(record.getR17_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR17_31_asse_inr() != null) {
					cellC.setCellValue(record.getR17_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR17_31_liab_lc() != null) {
					cellD.setCellValue(record.getR17_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR17_31_liab_inr() != null) {
					cellE.setCellValue(record.getR17_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR17_30_asse_lc() != null) {
					cellF.setCellValue(record.getR17_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR17_30_asse_inr() != null) {
					cellG.setCellValue(record.getR17_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR17_30_liab_lc() != null) {
					cellH.setCellValue(record.getR17_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR17_30_liab_inr() != null) {
					cellI.setCellValue(record.getR17_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 18

					row = sheet.getRow(17);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR18_31_asse_lc() != null) {
					cellB.setCellValue(record.getR18_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR18_31_asse_inr() != null) {
					cellC.setCellValue(record.getR18_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR18_31_liab_lc() != null) {
					cellD.setCellValue(record.getR18_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR18_31_liab_inr() != null) {
					cellE.setCellValue(record.getR18_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR18_30_asse_lc() != null) {
					cellF.setCellValue(record.getR18_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR18_30_asse_inr() != null) {
					cellG.setCellValue(record.getR18_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR18_30_liab_lc() != null) {
					cellH.setCellValue(record.getR18_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR18_30_liab_inr() != null) {
					cellI.setCellValue(record.getR18_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}



					//ROW 19

					row = sheet.getRow(18);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR19_31_asse_lc() != null) {
					cellB.setCellValue(record.getR19_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR19_31_asse_inr() != null) {
					cellC.setCellValue(record.getR19_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR19_31_liab_lc() != null) {
					cellD.setCellValue(record.getR19_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR19_31_liab_inr() != null) {
					cellE.setCellValue(record.getR19_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR19_30_asse_lc() != null) {
					cellF.setCellValue(record.getR19_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR19_30_asse_inr() != null) {
					cellG.setCellValue(record.getR19_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR19_30_liab_lc() != null) {
					cellH.setCellValue(record.getR19_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR19_30_liab_inr() != null) {
					cellI.setCellValue(record.getR19_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 20

					row = sheet.getRow(19);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR20_31_asse_lc() != null) {
					cellB.setCellValue(record.getR20_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR20_31_asse_inr() != null) {
					cellC.setCellValue(record.getR20_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR20_31_liab_lc() != null) {
					cellD.setCellValue(record.getR20_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR20_31_liab_inr() != null) {
					cellE.setCellValue(record.getR20_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR20_30_asse_lc() != null) {
					cellF.setCellValue(record.getR20_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR20_30_asse_inr() != null) {
					cellG.setCellValue(record.getR20_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR20_30_liab_lc() != null) {
					cellH.setCellValue(record.getR20_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR20_30_liab_inr() != null) {
					cellI.setCellValue(record.getR20_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 21

					row = sheet.getRow(20);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR21_31_asse_lc() != null) {
					cellB.setCellValue(record.getR21_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR21_31_asse_inr() != null) {
					cellC.setCellValue(record.getR21_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR21_31_liab_lc() != null) {
					cellD.setCellValue(record.getR21_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR21_31_liab_inr() != null) {
					cellE.setCellValue(record.getR21_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR21_30_asse_lc() != null) {
					cellF.setCellValue(record.getR21_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR21_30_asse_inr() != null) {
					cellG.setCellValue(record.getR21_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR21_30_liab_lc() != null) {
					cellH.setCellValue(record.getR21_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR21_30_liab_inr() != null) {
					cellI.setCellValue(record.getR21_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}



					//ROW 22

					row = sheet.getRow(21);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR22_31_asse_lc() != null) {
					cellB.setCellValue(record.getR22_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR22_31_asse_inr() != null) {
					cellC.setCellValue(record.getR22_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR22_31_liab_lc() != null) {
					cellD.setCellValue(record.getR22_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR22_31_liab_inr() != null) {
					cellE.setCellValue(record.getR22_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR22_30_asse_lc() != null) {
					cellF.setCellValue(record.getR22_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR22_30_asse_inr() != null) {
					cellG.setCellValue(record.getR22_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR22_30_liab_lc() != null) {
					cellH.setCellValue(record.getR22_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR22_30_liab_inr() != null) {
					cellI.setCellValue(record.getR22_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 23

					row = sheet.getRow(22);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR23_31_asse_lc() != null) {
					cellB.setCellValue(record.getR23_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR23_31_asse_inr() != null) {
					cellC.setCellValue(record.getR23_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR23_31_liab_lc() != null) {
					cellD.setCellValue(record.getR23_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR23_31_liab_inr() != null) {
					cellE.setCellValue(record.getR23_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR23_30_asse_lc() != null) {
					cellF.setCellValue(record.getR23_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR23_30_asse_inr() != null) {
					cellG.setCellValue(record.getR23_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR23_30_liab_lc() != null) {
					cellH.setCellValue(record.getR23_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR23_30_liab_inr() != null) {
					cellI.setCellValue(record.getR23_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 24

					row = sheet.getRow(23);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR24_31_asse_lc() != null) {
					cellB.setCellValue(record.getR24_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR24_31_asse_inr() != null) {
					cellC.setCellValue(record.getR24_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR24_31_liab_lc() != null) {
					cellD.setCellValue(record.getR24_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR24_31_liab_inr() != null) {
					cellE.setCellValue(record.getR24_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR24_30_asse_lc() != null) {
					cellF.setCellValue(record.getR24_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR24_30_asse_inr() != null) {
					cellG.setCellValue(record.getR24_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR24_30_liab_lc() != null) {
					cellH.setCellValue(record.getR24_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR24_30_liab_inr() != null) {
					cellI.setCellValue(record.getR24_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
					}


					//ROW 25

					row = sheet.getRow(24);

					//COLUMN B
					cellB = row.getCell(1);
					if (cellB == null) cellB = row.createCell(1);
					if (record.getR25_31_asse_lc() != null) {
					cellB.setCellValue(record.getR25_31_asse_lc().doubleValue());
					} else {
					cellB.setCellValue(0);
					}

					//COLUMN C
					cellC = row.getCell(2);
					if (cellC == null) cellC = row.createCell(2);
					if (record.getR25_31_asse_inr() != null) {
					cellC.setCellValue(record.getR25_31_asse_inr().doubleValue());
					} else {
					cellC.setCellValue(0);
					}

					//COLUMN D
					cellD = row.getCell(3);
					if (cellD == null) cellD = row.createCell(3);
					if (record.getR25_31_liab_lc() != null) {
					cellD.setCellValue(record.getR25_31_liab_lc().doubleValue());
					} else {
					cellD.setCellValue(0);
					}

					//COLUMN E
					cellE = row.getCell(4);
					if (cellE == null) cellE = row.createCell(4);
					if (record.getR25_31_liab_inr() != null) {
					cellE.setCellValue(record.getR25_31_liab_inr().doubleValue());
					} else {
					cellE.setCellValue(0);
					}

					//COLUMN F
					cellF = row.getCell(5);
					if (cellF == null) cellF = row.createCell(5);
					if (record.getR25_30_asse_lc() != null) {
					cellF.setCellValue(record.getR25_30_asse_lc().doubleValue());
					} else {
					cellF.setCellValue(0);
					}

					//COLUMN G
					cellG = row.getCell(6);
					if (cellG == null) cellG = row.createCell(6);
					if (record.getR25_30_asse_inr() != null) {
					cellG.setCellValue(record.getR25_30_asse_inr().doubleValue());
					} else {
					cellG.setCellValue(0);
					}

					//COLUMN H
					cellH = row.getCell(7);
					if (cellH == null) cellH = row.createCell(7);
					if (record.getR25_30_liab_lc() != null) {
					cellH.setCellValue(record.getR25_30_liab_lc().doubleValue());
					} else {
					cellH.setCellValue(0);
					}

					//COLUMN I
					cellI = row.getCell(8);
					if (cellI == null) cellI = row.createCell(8);
					if (record.getR25_30_liab_inr() != null) {
					cellI.setCellValue(record.getR25_30_liab_inr().doubleValue());
					} else {
					cellI.setCellValue(0);
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
	
	
	public void updateReport(DEFERRED_TAX_Summary_Entity updatedEntity) {

		DEFERRED_TAX_Summary_Entity existing = DTAX_summary_repo.findById(updatedEntity.getReport_date()).orElseThrow(
				() -> new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

		int[] rows = { };

		String[] fields = { " "};

		try {
			for (int i : rows) {
				for (String field : fields) {

					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;

					try {
						Method getter = DEFERRED_TAX_Summary_Entity.class.getMethod(getterName);
						Method setter = DEFERRED_TAX_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Field not applicable for this row → skip safely
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		DTAX_summary_repo.save(existing);
	}
	

	//Archival View
	public List<Object[]> getDTAXArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<DEFERRED_TAX_Archival_Summary_Entity> repoData = DTAX_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (DEFERRED_TAX_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] {
							entity.getReport_date(), 
							entity.getReport_version(), 
							 entity.getReportResubDate()
					};
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				DEFERRED_TAX_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  DTAX  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	
	
	

	
	
	
	public byte[] getDTAXDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for  DTAX Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDTAXDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("DTAX Details");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 ) {  // MONTHLY_INT (3) and CREDIT_EQUIVALENT (4) nd DEBIT_EQUIVALENT(5)
				    cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
				    cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<DEFERRED_TAX_Detail_Entity> reportData = DTAX_detail_repo.getdatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (DEFERRED_TAX_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					// AVERAGE
//					 balanceCell = row.createCell(4);
//					if (item.getAverage() != null) {
//						balanceCell.setCellValue(item.getAverage().doubleValue());
//					} else {
//						balanceCell.setCellValue(0);
//					}
//					balanceCell.setCellStyle(balanceStyle);
					

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						 if (j != 3  ) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for DTAX — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating DTAX Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
	
	
	
	public byte[] getDTAXDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for DTAX ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("DTAX Detail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

// Header row
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 ) {  // MONTHLY_INT (3) and CREDIT_EQUIVALENT (4) nd DEBIT_EQUIVALENT(5)
				    cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
				    cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<DEFERRED_TAX_Archival_Detail_Entity> reportData = DTAX_Archival_Detail_Repo.getdatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (DEFERRED_TAX_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					 row.createCell(2).setCellValue(item.getAcctName()); 

// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

//					// AVERAGE
//					 balanceCell = row.createCell(4);
//					if (item.getAverage() != null) {
//						balanceCell.setCellValue(item.getAverage().doubleValue());
//					} else {
//						balanceCell.setCellValue(0);
//					}
//					balanceCell.setCellStyle(balanceStyle);
					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						 if (j != 3 ) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for DTAX — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating DTAX Excel", e);
			return new byte[0];
		}
	}
	
	
	
	
//	 @Autowired BRRS_DEFERRED_TAX_Detail_Repo DTAX_detail_repo;
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/DTAX"); 

		if (acctNo != null) {
			DEFERRED_TAX_Detail_Entity dtaxEntity = DTAX_detail_repo.findByAcctnumber(acctNo);
			if (dtaxEntity != null && dtaxEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(dtaxEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("d_taxData", dtaxEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("acctBalanceInpula");
//			String average = request.getParameter("average");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			DEFERRED_TAX_Detail_Entity existing = DTAX_detail_repo.findByAcctnumber(acctNo);
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

			 if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {
		            BigDecimal newacctBalanceInpula = new BigDecimal(acctBalanceInpula);
		            if (existing.getAcctBalanceInpula()  == null ||
		                existing.getAcctBalanceInpula().compareTo(newacctBalanceInpula) != 0) {
		            	 existing.setAcctBalanceInpula(newacctBalanceInpula);
		                isChanged = true;
		                logger.info("Balance updated to {}", newacctBalanceInpula);
		            }
		        }
			 
			 
//			 if (average != null && !average.isEmpty()) {
//		            BigDecimal newaverage = new BigDecimal(average);
//		            if (existing.getAverage()  == null ||
//		                existing.getAverage().compareTo(newaverage) != 0) {
//		            	 existing.setAverage(newaverage);
//		                isChanged = true;
//		                logger.info("Balance updated to {}", newaverage);
//		            }
//		        }
		        
			if (isChanged) {
				DTAX_detail_repo.save(existing);
				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_DEFERRED_TAX_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_DEFERRED_TAX_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating DTAX record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}
	
	
	
	

}