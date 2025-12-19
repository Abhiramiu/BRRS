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
import java.util.Locale;
import java.util.Optional;

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

import com.bornfire.brrs.entities.BRRS_M_SCI_E_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_ATF_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_ATF_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_Q_ATF_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_Q_ATF_Summary_Repo;
import com.bornfire.brrs.entities.M_SCI_E_Detail_Entity;
import com.bornfire.brrs.entities.Q_ATF_Archival_Detail_Entity;
import com.bornfire.brrs.entities.Q_ATF_Archival_Summary_Entity;
import com.bornfire.brrs.entities.Q_ATF_Detail_Entity;
import com.bornfire.brrs.entities.Q_ATF_Summary_Entity;

@Component
@Service

public class BRRS_Q_ATF_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_ATF_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;


	@Autowired
	BRRS_Q_ATF_Summary_Repo brrs_q_atf_summary_repo;
	


	@Autowired
	BRRS_Q_ATF_Archival_Summary_Repo q_atf_Archival_Summary_Repo;

	
	
	@Autowired
	BRRS_Q_ATF_Detail_Repo brrs_q_atf_detail_repo;
	


	@Autowired
	BRRS_Q_ATF_Archival_Detail_Repo q_atf_Archival_detail_Repo;

	
	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	
	
	
	
	public ModelAndView getQ_ATFView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<Q_ATF_Archival_Summary_Entity> T1Master = new ArrayList<Q_ATF_Archival_Summary_Entity>();
		
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = q_atf_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);
			


			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			
		} else {
			List<Q_ATF_Summary_Entity> T1Master = new ArrayList<>();
			

			try {
				
				// FIX the month name before parsing
			    if(todate != null) {
			        todate = todate.trim().replace("Sept", "Sep");
			    }
			    
			    // Matches "30-Sep-2025"
			    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

			    Date d1 = dateformat.parse(todate);  // todate = "30-Sep-2025"

			    T1Master = brrs_q_atf_summary_repo.getdatabydateList(d1);
			   
			    
			    System.out.println("T1Master size for ATF is: " + T1Master.size());

			} catch (ParseException e) {
			    e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/Q_ATF");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}
	public ModelAndView getQ_ATFcurrentDtl(String reportId, String fromdate, String todate, String currency,
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
	            List<Q_ATF_Archival_Detail_Entity> T1Dt1;
	            if (rowId != null && columnId != null) {
	                T1Dt1 = q_atf_Archival_detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
	            } else {
	                T1Dt1 = q_atf_Archival_detail_Repo.getdatabydateList(parsedDate, version);
	                
	            }

	            mv.addObject("reportdetails", T1Dt1);
	           
	            System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

	        } else {
	            // 🔹 Current branch
	            List<Q_ATF_Detail_Entity> T1Dt1;
	            if (rowId != null && columnId != null) {
	                T1Dt1 = brrs_q_atf_detail_repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
	            } else {
	                T1Dt1 = brrs_q_atf_detail_repo.getdatabydateList(parsedDate);
	                totalPages = brrs_q_atf_detail_repo.getdatacount(parsedDate);
	                mv.addObject("pagination", "YES");
	            }

	            mv.addObject("reportdetails", T1Dt1);
	           
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
	    mv.setViewName("BRRS/Q_ATF");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("currentPage", currentPage);
	    System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
	    mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	    mv.addObject("reportsflag", "reportsflag");
	    mv.addObject("menu", reportId);

	    return mv;
	}
	
	public byte[] getBRRS_Q_ATFExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		
		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelQ_ATFARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<Q_ATF_Summary_Entity> dataList = brrs_q_atf_summary_repo
				.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_Q_ATF report. Returning empty result.");
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

			int startRow =11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_ATF_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					
// row12
					
					
					
					// Column 2 - _num_depo
					Cell cellB = row.createCell(1);
					if (record.getR12_num_depo() != null) {
					    cellB.setCellValue(record.getR12_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}
					
					// Column 3 - _num_depo_acc
					Cell cellC = row.createCell(2);
					if (record.getR12_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR12_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}
					
					// Column 4 - _num_borrowers
					Cell cellD = row.createCell(3);
					if (record.getR12_num_borrowers() != null) {
					    cellD.setCellValue(record.getR12_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// Column 5 - _num_loan_acc
					Cell cellE = row.createCell(4);
					if (record.getR12_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR12_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}
			
					
					
					// ======================= R13 =======================
					// row13
					row = sheet.getRow(12);
					
					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR13_num_depo() != null) {
					    cellB.setCellValue(record.getR13_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR13_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR13_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR13_num_borrowers() != null) {
					    cellD.setCellValue(record.getR13_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR13_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR13_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R14 =======================
					// row14
					row = sheet.getRow(13);
					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR14_num_depo() != null) {
					    cellB.setCellValue(record.getR14_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR14_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR14_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR14_num_borrowers() != null) {
					    cellD.setCellValue(record.getR14_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR14_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR14_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// ======================= R16 =======================

					// row16
					row = sheet.getRow(15);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR16_num_depo() != null) {
					    cellB.setCellValue(record.getR16_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR16_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR16_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR16_num_borrowers() != null) {
					    cellD.setCellValue(record.getR16_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR16_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR16_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R17 =======================

					// row17
					row = sheet.getRow(16);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR17_num_depo() != null) {
					    cellB.setCellValue(record.getR17_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR17_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR17_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR17_num_borrowers() != null) {
					    cellD.setCellValue(record.getR17_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR17_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR17_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R18 =======================

					// row18
					row = sheet.getRow(17);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR18_num_depo() != null) {
					    cellB.setCellValue(record.getR18_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR18_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR18_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR18_num_borrowers() != null) {
					    cellD.setCellValue(record.getR18_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR18_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR18_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R19 =======================

					// row19
					row = sheet.getRow(18);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR19_num_depo() != null) {
					    cellB.setCellValue(record.getR19_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR19_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR19_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR19_num_borrowers() != null) {
					    cellD.setCellValue(record.getR19_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR19_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR19_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R20 =======================

					// row20
					row = sheet.getRow(19);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR20_num_depo() != null) {
					    cellB.setCellValue(record.getR20_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR20_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR20_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR20_num_borrowers() != null) {
					    cellD.setCellValue(record.getR20_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR20_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR20_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R21 =======================

					// row21
					row = sheet.getRow(20);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR21_num_depo() != null) {
					    cellB.setCellValue(record.getR21_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR21_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR21_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR21_num_borrowers() != null) {
					    cellD.setCellValue(record.getR21_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR21_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR21_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R22 =======================

					// row22
					row = sheet.getRow(21);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR22_num_depo() != null) {
					    cellB.setCellValue(record.getR22_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR22_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR22_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR22_num_borrowers() != null) {
					    cellD.setCellValue(record.getR22_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR22_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR22_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R23 =======================

					// row23
					row = sheet.getRow(22);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR23_num_depo() != null) {
					    cellB.setCellValue(record.getR23_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR23_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR23_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR23_num_borrowers() != null) {
					    cellD.setCellValue(record.getR23_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR23_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR23_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R24 =======================

					// row24
					row = sheet.getRow(23);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR24_num_depo() != null) {
					    cellB.setCellValue(record.getR24_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR24_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR24_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR24_num_borrowers() != null) {
					    cellD.setCellValue(record.getR24_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR24_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR24_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R25 =======================

					// row25
					row = sheet.getRow(24);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR25_num_depo() != null) {
					    cellB.setCellValue(record.getR25_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR25_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR25_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR25_num_borrowers() != null) {
					    cellD.setCellValue(record.getR25_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR25_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR25_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R26 =======================

					// row26
					row = sheet.getRow(25);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR26_num_depo() != null) {
					    cellB.setCellValue(record.getR26_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR26_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR26_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR26_num_borrowers() != null) {
					    cellD.setCellValue(record.getR26_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR26_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR26_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R27 =======================

					// row27
					row = sheet.getRow(26);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR27_num_depo() != null) {
					    cellB.setCellValue(record.getR27_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR27_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR27_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR27_num_borrowers() != null) {
					    cellD.setCellValue(record.getR27_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR27_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR27_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R28 =======================

					// row28
					row = sheet.getRow(27);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR28_num_depo() != null) {
					    cellB.setCellValue(record.getR28_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR28_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR28_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR28_num_borrowers() != null) {
					    cellD.setCellValue(record.getR28_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR28_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR28_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// ======================= R30 =======================

					// row30
					row = sheet.getRow(29);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR30_num_depo() != null) {
					    cellB.setCellValue(record.getR30_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR30_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR30_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR30_num_borrowers() != null) {
					    cellD.setCellValue(record.getR30_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR30_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR30_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R31 =======================

					// row31
					row = sheet.getRow(30);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR31_num_depo() != null) {
					    cellB.setCellValue(record.getR31_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR31_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR31_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR31_num_borrowers() != null) {
					    cellD.setCellValue(record.getR31_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR31_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR31_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R32 =======================

					// row32
					row = sheet.getRow(31);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR32_num_depo() != null) {
					    cellB.setCellValue(record.getR32_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR32_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR32_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR32_num_borrowers() != null) {
					    cellD.setCellValue(record.getR32_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR32_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR32_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R33 =======================

					// row33
					row = sheet.getRow(32);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR33_num_depo() != null) {
					    cellB.setCellValue(record.getR33_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR33_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR33_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR33_num_borrowers() != null) {
					    cellD.setCellValue(record.getR33_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR33_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR33_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R34 =======================

					// row34
					row = sheet.getRow(33);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR34_num_depo() != null) {
					    cellB.setCellValue(record.getR34_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR34_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR34_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR34_num_borrowers() != null) {
					    cellD.setCellValue(record.getR34_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR34_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR34_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R35 =======================

					// row35
					row = sheet.getRow(34);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR35_num_depo() != null) {
					    cellB.setCellValue(record.getR35_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR35_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR35_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR35_num_borrowers() != null) {
					    cellD.setCellValue(record.getR35_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR35_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR35_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R36 =======================

					// row36
					row = sheet.getRow(35);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR36_num_depo() != null) {
					    cellB.setCellValue(record.getR36_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR36_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR36_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR36_num_borrowers() != null) {
					    cellD.setCellValue(record.getR36_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR36_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR36_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R37 =======================

					// row37
					row = sheet.getRow(36);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR37_num_depo() != null) {
					    cellB.setCellValue(record.getR37_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR37_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR37_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR37_num_borrowers() != null) {
					    cellD.setCellValue(record.getR37_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR37_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR37_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



				
					// ======================= R39 =======================

					// row39
					row = sheet.getRow(38);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR39_num_depo() != null) {
					    cellB.setCellValue(record.getR39_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR39_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR39_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR39_num_borrowers() != null) {
					    cellD.setCellValue(record.getR39_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR39_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR39_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R40 =======================

					// row40
					row = sheet.getRow(39);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR40_num_depo() != null) {
					    cellB.setCellValue(record.getR40_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR40_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR40_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR40_num_borrowers() != null) {
					    cellD.setCellValue(record.getR40_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR40_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR40_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// ======================= R42 =======================

					// row42
					row = sheet.getRow(41);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR42_num_depo() != null) {
					    cellB.setCellValue(record.getR42_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR42_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR42_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR42_num_borrowers() != null) {
					    cellD.setCellValue(record.getR42_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR42_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR42_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R43 =======================

					// row43
					row = sheet.getRow(42);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR43_num_depo() != null) {
					    cellB.setCellValue(record.getR43_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR43_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR43_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR43_num_borrowers() != null) {
					    cellD.setCellValue(record.getR43_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR43_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR43_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}
					
					// ======================= R45 =======================

					// row45
					row = sheet.getRow(44);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR45_num_depo() != null) {
					    cellB.setCellValue(record.getR45_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR45_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR45_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR45_num_borrowers() != null) {
					    cellD.setCellValue(record.getR45_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR45_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR45_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R46 =======================

					// row46
					row = sheet.getRow(45);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR46_num_depo() != null) {
					    cellB.setCellValue(record.getR46_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR46_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR46_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR46_num_borrowers() != null) {
					    cellD.setCellValue(record.getR46_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR46_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR46_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R47 =======================

					// row47
					row = sheet.getRow(46);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR47_num_depo() != null) {
					    cellB.setCellValue(record.getR47_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR47_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR47_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR47_num_borrowers() != null) {
					    cellD.setCellValue(record.getR47_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR47_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR47_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R48 =======================

					// row48
					row = sheet.getRow(47);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR48_num_depo() != null) {
					    cellB.setCellValue(record.getR48_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR48_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR48_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR48_num_borrowers() != null) {
					    cellD.setCellValue(record.getR48_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR48_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR48_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					
					
					// ======================= R50 =======================

					// row50
					row = sheet.getRow(49);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR50_num_depo() != null) {
					    cellB.setCellValue(record.getR50_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR50_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR50_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR50_num_borrowers() != null) {
					    cellD.setCellValue(record.getR50_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR50_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR50_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R51 =======================

					// row51
					row = sheet.getRow(50);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR51_num_depo() != null) {
					    cellB.setCellValue(record.getR51_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR51_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR51_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR51_num_borrowers() != null) {
					    cellD.setCellValue(record.getR51_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR51_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR51_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R52 =======================

					// row52
					row = sheet.getRow(51);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR52_num_depo() != null) {
					    cellB.setCellValue(record.getR52_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR52_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR52_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR52_num_borrowers() != null) {
					    cellD.setCellValue(record.getR52_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR52_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR52_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// ======================= R54 =======================

					// row54
					row = sheet.getRow(53);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR54_num_depo() != null) {
					    cellB.setCellValue(record.getR54_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR54_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR54_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR54_num_borrowers() != null) {
					    cellD.setCellValue(record.getR54_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR54_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR54_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R55 =======================

					// row55
					row = sheet.getRow(54);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR55_num_depo() != null) {
					    cellB.setCellValue(record.getR55_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR55_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR55_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR55_num_borrowers() != null) {
					    cellD.setCellValue(record.getR55_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR55_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR55_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R56 =======================

					// row56
					row = sheet.getRow(55);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR56_num_depo() != null) {
					    cellB.setCellValue(record.getR56_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR56_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR56_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR56_num_borrowers() != null) {
					    cellD.setCellValue(record.getR56_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR56_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR56_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}


					
					// ======================= R58 =======================

					// row58
					row = sheet.getRow(57);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR58_num_depo() != null) {
					    cellB.setCellValue(record.getR58_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR58_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR58_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR58_num_borrowers() != null) {
					    cellD.setCellValue(record.getR58_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR58_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR58_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R59 =======================

					// row59
					row = sheet.getRow(58);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR59_num_depo() != null) {
					    cellB.setCellValue(record.getR59_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR59_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR59_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR59_num_borrowers() != null) {
					    cellD.setCellValue(record.getR59_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR59_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR59_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R60 =======================

					// row60
					row = sheet.getRow(59);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR60_num_depo() != null) {
					    cellB.setCellValue(record.getR60_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR60_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR60_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR60_num_borrowers() != null) {
					    cellD.setCellValue(record.getR60_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR60_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR60_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R61 =======================

					// row61
					row = sheet.getRow(60);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR61_num_depo() != null) {
					    cellB.setCellValue(record.getR61_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR61_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR61_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR61_num_borrowers() != null) {
					    cellD.setCellValue(record.getR61_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR61_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR61_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R62 =======================

					// row62
					row = sheet.getRow(61);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR62_num_depo() != null) {
					    cellB.setCellValue(record.getR62_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR62_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR62_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR62_num_borrowers() != null) {
					    cellD.setCellValue(record.getR62_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR62_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR62_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R63 =======================

					// row63
					row = sheet.getRow(62);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR63_num_depo() != null) {
					    cellB.setCellValue(record.getR63_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR63_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR63_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR63_num_borrowers() != null) {
					    cellD.setCellValue(record.getR63_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR63_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR63_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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
	

	
	public byte[] getQ_ATFDetailExcel(String filename, String fromdate, String todate,
	        String currency, String dtltype, String type, String version) {

	    logger.info("▶ Entered getQ_ATFDetailExcel()");
	    logger.info("Input Params → filename={}, fromdate={}, todate={}, currency={}, dtltype={}, type={}, version={}",
	            filename, fromdate, todate, currency, dtltype, type, version);

	    try {
	        logger.info("Generating Excel for Q_ATF Details...");
	        System.out.println("came to Detail download service");

	        // ARCHIVAL routing
	        if ("ARCHIVAL".equals(type) && version != null) {
	            logger.info("ARCHIVAL request detected → Redirecting to ARCHIVAL Excel method");
	            byte[] archivalReport = getQ_ATFDetailExcelARCHIVAL(
	                    filename, fromdate, todate, currency, dtltype, type, version);
	            logger.info("ARCHIVAL Excel generated successfully, bytes={}", archivalReport.length);
	            return archivalReport;
	        }

	        logger.info("Proceeding with NON-ARCHIVAL Excel generation");

	        // Workbook & Sheet
	        logger.info("Creating XSSFWorkbook");
	        XSSFWorkbook workbook = new XSSFWorkbook();

	        logger.info("Creating sheet: Q_ATFDetails");
	        XSSFSheet sheet = workbook.createSheet("Q_ATFDetails");

	        // Border style
	        logger.debug("Initializing common border style");
	        BorderStyle border = BorderStyle.THIN;

	        // Header style
	        logger.debug("Creating header cell style");
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

	        // Right-aligned header
	        logger.debug("Creating right-aligned header style for ACCT BALANCE");
	        CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
	        rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
	        rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

	        // Data style
	        logger.debug("Creating default data style");
	        CellStyle dataStyle = workbook.createCellStyle();
	        dataStyle.setAlignment(HorizontalAlignment.LEFT);
	        dataStyle.setBorderTop(border);
	        dataStyle.setBorderBottom(border);
	        dataStyle.setBorderLeft(border);
	        dataStyle.setBorderRight(border);

	        // Balance style
	        logger.debug("Creating balance style with 3 decimal format");
	        CellStyle balanceStyle = workbook.createCellStyle();
	        balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	        balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
	        balanceStyle.setBorderTop(border);
	        balanceStyle.setBorderBottom(border);
	        balanceStyle.setBorderLeft(border);
	        balanceStyle.setBorderRight(border);

	        // Header row
	        logger.info("Creating header row");
	        String[] headers = {
	                "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE",
	                "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE"
	        };

	        XSSFRow headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(i == 3 ? rightAlignedHeaderStyle : headerStyle);
	            sheet.setColumnWidth(i, 5000);
	        }

	        logger.info("Header row created successfully");

	        // Fetch data
	        logger.info("Parsing todate={}", todate);
	        Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);

	        logger.info("Fetching Q_ATF Detail data from repository for date={}", parsedToDate);
	        List<Q_ATF_Detail_Entity> reportData =
	                brrs_q_atf_detail_repo.getdatabydateList(parsedToDate);

	        logger.info("Repository returned {} records",
	                reportData != null ? reportData.size() : 0);

	        // Populate rows
	        if (reportData != null && !reportData.isEmpty()) {
	            int rowIndex = 1;

	            for (Q_ATF_Detail_Entity item : reportData) {
	                logger.debug("Writing row {} → CustId={}, AcctNo={}",
	                        rowIndex, item.getCustId(), item.getAcctNumber());

	                XSSFRow row = sheet.createRow(rowIndex++);

	                row.createCell(0).setCellValue(item.getCustId());
	                row.createCell(1).setCellValue(item.getAcctNumber());
	                row.createCell(2).setCellValue(item.getAcctName());

	                Cell balanceCell = row.createCell(3);
	                if (item.getAcctBalanceInpula() != null) {
	                    balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
	                } else {
	                    balanceCell.setCellValue(0.000);
	                }
	                balanceCell.setCellStyle(balanceStyle);

	                row.createCell(4).setCellValue(item.getReportLabel());
	                row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
	                row.createCell(6).setCellValue(
	                        item.getReportDate() != null
	                                ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
	                                : ""
	                );

	                for (int j = 0; j < 7; j++) {
	                    if (j != 3) {
	                        row.getCell(j).setCellStyle(dataStyle);
	                    }
	                }
	            }
	        } else {
	            logger.warn("No data found for Q_ATF — only header written");
	        }

	        // Write output
	        logger.info("Writing workbook to ByteArrayOutputStream");
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        workbook.write(bos);
	        workbook.close();

	        logger.info("✔ Q_ATF Excel generation completed successfully. Total rows={}",
	                reportData != null ? reportData.size() : 0);

	        return bos.toByteArray();

	    } catch (Exception e) {
	        logger.error("❌ Error generating Q_ATF Excel", e);
	        return new byte[0];
	    }
	}

	
	
	
	public byte[] getQ_ATFDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for Q_ATF ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Q_ATFDetail");

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
			String[] headers = {  "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE",  "REPORT LABLE", "REPORT ADDL CRITERIA1", "REPORT_DATE" };

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
			List<Q_ATF_Archival_Detail_Entity> reportData = q_atf_Archival_detail_Repo.getdatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Q_ATF_Archival_Detail_Entity item : reportData) {
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

					
					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
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
				logger.info("No data found for Q_ATF — only header will be written.");
			}

// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Q_ATFExcel", e);
			return new byte[0];
		}
	}

	
	public List<Object> getQ_ATFArchival() {
		List<Object> Q_ATFArchivallist = new ArrayList<>();
		try {
			Q_ATFArchivallist = q_atf_Archival_Summary_Repo.getQ_ATFarchival();
			
			System.out.println("countser" + Q_ATFArchivallist.size());
			
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching Q_ATFArchivallist Archival data: " + e.getMessage());
			e.printStackTrace();

			
		}
		return Q_ATFArchivallist;
	}
	
	

	
	public byte[] getExcelQ_ATFARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<Q_ATF_Archival_Summary_Entity> dataList = q_atf_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_CA4 report. Returning empty result.");
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

			int startRow =11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_ATF_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					
// row12
					
					
					
					// Column 2 - _num_depo
					Cell cellB = row.createCell(1);
					if (record.getR12_num_depo() != null) {
					    cellB.setCellValue(record.getR12_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}
					
					// Column 3 - _num_depo_acc
					Cell cellC = row.createCell(2);
					if (record.getR12_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR12_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}
					
					// Column 4 - _num_borrowers
					Cell cellD = row.createCell(3);
					if (record.getR12_num_borrowers() != null) {
					    cellD.setCellValue(record.getR12_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}
					
					// Column 5 - _num_loan_acc
					Cell cellE = row.createCell(4);
					if (record.getR12_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR12_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}
			
					
					
					// ======================= R13 =======================
					// row13
					row = sheet.getRow(12);
					
					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR13_num_depo() != null) {
					    cellB.setCellValue(record.getR13_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR13_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR13_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR13_num_borrowers() != null) {
					    cellD.setCellValue(record.getR13_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR13_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR13_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R14 =======================
					// row14
					row = sheet.getRow(13);
					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR14_num_depo() != null) {
					    cellB.setCellValue(record.getR14_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR14_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR14_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR14_num_borrowers() != null) {
					    cellD.setCellValue(record.getR14_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR14_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR14_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// ======================= R16 =======================

					// row16
					row = sheet.getRow(15);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR16_num_depo() != null) {
					    cellB.setCellValue(record.getR16_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR16_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR16_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR16_num_borrowers() != null) {
					    cellD.setCellValue(record.getR16_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR16_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR16_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R17 =======================

					// row17
					row = sheet.getRow(16);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR17_num_depo() != null) {
					    cellB.setCellValue(record.getR17_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR17_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR17_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR17_num_borrowers() != null) {
					    cellD.setCellValue(record.getR17_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR17_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR17_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R18 =======================

					// row18
					row = sheet.getRow(17);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR18_num_depo() != null) {
					    cellB.setCellValue(record.getR18_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR18_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR18_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR18_num_borrowers() != null) {
					    cellD.setCellValue(record.getR18_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR18_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR18_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R19 =======================

					// row19
					row = sheet.getRow(18);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR19_num_depo() != null) {
					    cellB.setCellValue(record.getR19_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR19_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR19_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR19_num_borrowers() != null) {
					    cellD.setCellValue(record.getR19_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR19_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR19_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R20 =======================

					// row20
					row = sheet.getRow(19);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR20_num_depo() != null) {
					    cellB.setCellValue(record.getR20_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR20_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR20_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR20_num_borrowers() != null) {
					    cellD.setCellValue(record.getR20_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR20_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR20_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R21 =======================

					// row21
					row = sheet.getRow(20);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR21_num_depo() != null) {
					    cellB.setCellValue(record.getR21_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR21_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR21_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR21_num_borrowers() != null) {
					    cellD.setCellValue(record.getR21_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR21_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR21_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R22 =======================

					// row22
					row = sheet.getRow(21);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR22_num_depo() != null) {
					    cellB.setCellValue(record.getR22_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR22_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR22_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR22_num_borrowers() != null) {
					    cellD.setCellValue(record.getR22_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR22_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR22_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R23 =======================

					// row23
					row = sheet.getRow(22);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR23_num_depo() != null) {
					    cellB.setCellValue(record.getR23_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR23_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR23_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR23_num_borrowers() != null) {
					    cellD.setCellValue(record.getR23_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR23_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR23_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R24 =======================

					// row24
					row = sheet.getRow(23);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR24_num_depo() != null) {
					    cellB.setCellValue(record.getR24_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR24_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR24_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR24_num_borrowers() != null) {
					    cellD.setCellValue(record.getR24_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR24_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR24_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R25 =======================

					// row25
					row = sheet.getRow(24);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR25_num_depo() != null) {
					    cellB.setCellValue(record.getR25_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR25_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR25_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR25_num_borrowers() != null) {
					    cellD.setCellValue(record.getR25_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR25_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR25_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R26 =======================

					// row26
					row = sheet.getRow(25);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR26_num_depo() != null) {
					    cellB.setCellValue(record.getR26_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR26_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR26_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR26_num_borrowers() != null) {
					    cellD.setCellValue(record.getR26_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR26_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR26_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R27 =======================

					// row27
					row = sheet.getRow(26);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR27_num_depo() != null) {
					    cellB.setCellValue(record.getR27_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR27_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR27_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR27_num_borrowers() != null) {
					    cellD.setCellValue(record.getR27_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR27_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR27_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R28 =======================

					// row28
					row = sheet.getRow(27);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR28_num_depo() != null) {
					    cellB.setCellValue(record.getR28_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR28_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR28_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR28_num_borrowers() != null) {
					    cellD.setCellValue(record.getR28_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR28_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR28_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// ======================= R30 =======================

					// row30
					row = sheet.getRow(29);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR30_num_depo() != null) {
					    cellB.setCellValue(record.getR30_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR30_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR30_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR30_num_borrowers() != null) {
					    cellD.setCellValue(record.getR30_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR30_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR30_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R31 =======================

					// row31
					row = sheet.getRow(30);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR31_num_depo() != null) {
					    cellB.setCellValue(record.getR31_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR31_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR31_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR31_num_borrowers() != null) {
					    cellD.setCellValue(record.getR31_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR31_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR31_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R32 =======================

					// row32
					row = sheet.getRow(31);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR32_num_depo() != null) {
					    cellB.setCellValue(record.getR32_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR32_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR32_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR32_num_borrowers() != null) {
					    cellD.setCellValue(record.getR32_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR32_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR32_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R33 =======================

					// row33
					row = sheet.getRow(32);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR33_num_depo() != null) {
					    cellB.setCellValue(record.getR33_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR33_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR33_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR33_num_borrowers() != null) {
					    cellD.setCellValue(record.getR33_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR33_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR33_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R34 =======================

					// row34
					row = sheet.getRow(33);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR34_num_depo() != null) {
					    cellB.setCellValue(record.getR34_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR34_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR34_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR34_num_borrowers() != null) {
					    cellD.setCellValue(record.getR34_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR34_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR34_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R35 =======================

					// row35
					row = sheet.getRow(34);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR35_num_depo() != null) {
					    cellB.setCellValue(record.getR35_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR35_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR35_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR35_num_borrowers() != null) {
					    cellD.setCellValue(record.getR35_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR35_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR35_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R36 =======================

					// row36
					row = sheet.getRow(35);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR36_num_depo() != null) {
					    cellB.setCellValue(record.getR36_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR36_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR36_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR36_num_borrowers() != null) {
					    cellD.setCellValue(record.getR36_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR36_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR36_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R37 =======================

					// row37
					row = sheet.getRow(36);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR37_num_depo() != null) {
					    cellB.setCellValue(record.getR37_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR37_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR37_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR37_num_borrowers() != null) {
					    cellD.setCellValue(record.getR37_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR37_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR37_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



				
					// ======================= R39 =======================

					// row39
					row = sheet.getRow(38);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR39_num_depo() != null) {
					    cellB.setCellValue(record.getR39_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR39_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR39_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR39_num_borrowers() != null) {
					    cellD.setCellValue(record.getR39_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR39_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR39_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R40 =======================

					// row40
					row = sheet.getRow(39);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR40_num_depo() != null) {
					    cellB.setCellValue(record.getR40_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR40_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR40_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR40_num_borrowers() != null) {
					    cellD.setCellValue(record.getR40_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR40_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR40_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// ======================= R42 =======================

					// row42
					row = sheet.getRow(41);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR42_num_depo() != null) {
					    cellB.setCellValue(record.getR42_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR42_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR42_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR42_num_borrowers() != null) {
					    cellD.setCellValue(record.getR42_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR42_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR42_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R43 =======================

					// row43
					row = sheet.getRow(42);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR43_num_depo() != null) {
					    cellB.setCellValue(record.getR43_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR43_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR43_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR43_num_borrowers() != null) {
					    cellD.setCellValue(record.getR43_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR43_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR43_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}
					
					// ======================= R45 =======================

					// row45
					row = sheet.getRow(44);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR45_num_depo() != null) {
					    cellB.setCellValue(record.getR45_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR45_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR45_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR45_num_borrowers() != null) {
					    cellD.setCellValue(record.getR45_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR45_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR45_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R46 =======================

					// row46
					row = sheet.getRow(45);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR46_num_depo() != null) {
					    cellB.setCellValue(record.getR46_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR46_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR46_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR46_num_borrowers() != null) {
					    cellD.setCellValue(record.getR46_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR46_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR46_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R47 =======================

					// row47
					row = sheet.getRow(46);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR47_num_depo() != null) {
					    cellB.setCellValue(record.getR47_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR47_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR47_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR47_num_borrowers() != null) {
					    cellD.setCellValue(record.getR47_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR47_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR47_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R48 =======================

					// row48
					row = sheet.getRow(47);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR48_num_depo() != null) {
					    cellB.setCellValue(record.getR48_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR48_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR48_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR48_num_borrowers() != null) {
					    cellD.setCellValue(record.getR48_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR48_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR48_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					
					
					// ======================= R50 =======================

					// row50
					row = sheet.getRow(49);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR50_num_depo() != null) {
					    cellB.setCellValue(record.getR50_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR50_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR50_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR50_num_borrowers() != null) {
					    cellD.setCellValue(record.getR50_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR50_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR50_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R51 =======================

					// row51
					row = sheet.getRow(50);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR51_num_depo() != null) {
					    cellB.setCellValue(record.getR51_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR51_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR51_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR51_num_borrowers() != null) {
					    cellD.setCellValue(record.getR51_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR51_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR51_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R52 =======================

					// row52
					row = sheet.getRow(51);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR52_num_depo() != null) {
					    cellB.setCellValue(record.getR52_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR52_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR52_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR52_num_borrowers() != null) {
					    cellD.setCellValue(record.getR52_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR52_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR52_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}

					// ======================= R54 =======================

					// row54
					row = sheet.getRow(53);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR54_num_depo() != null) {
					    cellB.setCellValue(record.getR54_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR54_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR54_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR54_num_borrowers() != null) {
					    cellD.setCellValue(record.getR54_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR54_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR54_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R55 =======================

					// row55
					row = sheet.getRow(54);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR55_num_depo() != null) {
					    cellB.setCellValue(record.getR55_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR55_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR55_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR55_num_borrowers() != null) {
					    cellD.setCellValue(record.getR55_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR55_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR55_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R56 =======================

					// row56
					row = sheet.getRow(55);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR56_num_depo() != null) {
					    cellB.setCellValue(record.getR56_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR56_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR56_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR56_num_borrowers() != null) {
					    cellD.setCellValue(record.getR56_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR56_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR56_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}


					
					// ======================= R58 =======================

					// row58
					row = sheet.getRow(57);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR58_num_depo() != null) {
					    cellB.setCellValue(record.getR58_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR58_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR58_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR58_num_borrowers() != null) {
					    cellD.setCellValue(record.getR58_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR58_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR58_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R59 =======================

					// row59
					row = sheet.getRow(58);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR59_num_depo() != null) {
					    cellB.setCellValue(record.getR59_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR59_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR59_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR59_num_borrowers() != null) {
					    cellD.setCellValue(record.getR59_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR59_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR59_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R60 =======================

					// row60
					row = sheet.getRow(59);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR60_num_depo() != null) {
					    cellB.setCellValue(record.getR60_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR60_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR60_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR60_num_borrowers() != null) {
					    cellD.setCellValue(record.getR60_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR60_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR60_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R61 =======================

					// row61
					row = sheet.getRow(60);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR61_num_depo() != null) {
					    cellB.setCellValue(record.getR61_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR61_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR61_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR61_num_borrowers() != null) {
					    cellD.setCellValue(record.getR61_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR61_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR61_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R62 =======================

					// row62
					row = sheet.getRow(61);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR62_num_depo() != null) {
					    cellB.setCellValue(record.getR62_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR62_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR62_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR62_num_borrowers() != null) {
					    cellD.setCellValue(record.getR62_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR62_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR62_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
					}



					// ======================= R63 =======================

					// row63
					row = sheet.getRow(62);

					// Column 2 - _num_depo
					cellB = row.createCell(1);
					if (record.getR63_num_depo() != null) {
					    cellB.setCellValue(record.getR63_num_depo().doubleValue());
					    cellB.setCellStyle(numberStyle);
					} else {
					    cellB.setCellValue("");
					    cellB.setCellStyle(textStyle);
					}

					// Column 3 - _num_depo_acc
					cellC = row.createCell(2);
					if (record.getR63_num_depo_acc() != null) {
					    cellC.setCellValue(record.getR63_num_depo_acc().doubleValue());
					    cellC.setCellStyle(numberStyle);
					} else {
					    cellC.setCellValue("");
					    cellC.setCellStyle(textStyle);
					}

					// Column 4 - _num_borrowers
					cellD = row.createCell(3);
					if (record.getR63_num_borrowers() != null) {
					    cellD.setCellValue(record.getR63_num_borrowers().doubleValue());
					    cellD.setCellStyle(numberStyle);
					} else {
					    cellD.setCellValue("");
					    cellD.setCellStyle(textStyle);
					}

					// Column 5 - _num_loan_acc
					cellE = row.createCell(4);
					if (record.getR63_num_loan_acc() != null) {
					    cellE.setCellValue(record.getR63_num_loan_acc().doubleValue());
					    cellE.setCellStyle(numberStyle);
					} else {
					    cellE.setCellValue("");
					    cellE.setCellStyle(textStyle);
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

	
	
	
	
	 
	 @Autowired BRRS_Q_ATF_Detail_Repo q_atf_detail_repo;
		
		
		@Autowired
		private JdbcTemplate jdbcTemplate;

		public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
			ModelAndView mv = new ModelAndView("BRRS/Q_ATF"); 

			if (acctNo != null) {
				Q_ATF_Detail_Entity qatfEntity = q_atf_detail_repo.findByAcctnumber(acctNo);
				if (qatfEntity != null && qatfEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(qatfEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("qatfData", qatfEntity);
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
				String acctName = request.getParameter("acctName");
				String reportDateStr = request.getParameter("reportDate");

				logger.info("Received update for ACCT_NO: {}", acctNo);

				Q_ATF_Detail_Entity existing = q_atf_detail_repo.findByAcctnumber(acctNo);
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
			        
				if (isChanged) {
					q_atf_detail_repo.save(existing);
					logger.info("Record updated successfully for account {}", acctNo);

					// Format date for procedure
					String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

					// Run summary procedure after commit
					TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
						@Override
						public void afterCommit() {
							try {
								logger.info("Transaction committed — calling BRRS_Q_ATF_SUMMARY_PROCEDURE({})",
										formattedDate);
								jdbcTemplate.update("BEGIN BRRS_Q_ATF_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
				logger.error("Error updating Q_ATF record", e);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Error updating record: " + e.getMessage());
			}
		}

		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}