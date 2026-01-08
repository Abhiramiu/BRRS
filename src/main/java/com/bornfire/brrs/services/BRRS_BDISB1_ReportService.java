package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BDISB1_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BDISB1_Detail_Entity;
import com.bornfire.brrs.entities.BRRS_BDISB1_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_BDISB1_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_BDISB1_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_BDISB1_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Archival_Summary_Repo;
import com.bornfire.brrs.entities.M_SRWA_12F_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12G_Summary_Entity;
import com.bornfire.brrs.entities.M_SRWA_12H_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BRRS_M_SRWA_12F_Summary_Repo;
import com.bornfire.brrs.entities.BDISB1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BDISB1_Archival_Summary_PK;
import com.bornfire.brrs.entities.BDISB1_Summary_Entity;
import com.bornfire.brrs.entities.BDISB2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BDISB2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.BDISB2_Detail_Entity;
import com.bornfire.brrs.entities.BDISB2_Summary_Entity;
import com.bornfire.brrs.entities.BDISB3_Archival_Detail_Entity;
import com.bornfire.brrs.entities.BDISB3_Detail_Entity;
import com.bornfire.brrs.entities.M_CA5_Summary_Entity1;
import com.bornfire.brrs.entities.M_SRWA_12F_Archival_Summary_Entity;

import java.math.BigDecimal;
import java.time.LocalDate;



@Component
@Service

public class BRRS_BDISB1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_BDISB1_ReportService.class);

	@Autowired
	private Environment env;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_BDISB1_Summary_Repo  BDISB1_Summary_Repo;

	@Autowired
	BRRS_BDISB1_Archival_Summary_Repo BDISB1_Archival_Summary_Repo;
	
	@Autowired
	BRRS_BDISB1_Detail_Repo  BDISB1_Detail_Repo;

	@Autowired
   BRRS_BDISB1_Archival_Detail_Repo BDISB1_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBDISB1View(String reportId, String fromdate, String todate, String currency, String dtltype,
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
				List<BDISB1_Archival_Summary_Entity> T1Master = BDISB1_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<BDISB1_Archival_Summary_Entity> T1Master = BDISB1_Archival_Summary_Repo
						.getdatabydateListarchival(d1, version);

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<BDISB1_Summary_Entity> T1Master = BDISB1_Summary_Repo
						.getdatabydateList(dateformat.parse(todate));
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("reportsummary", T1Master);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/BDISB1");
		mv.addObject("displaymode", "summary");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	
	public ModelAndView getBDISB1currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version) {

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
			if (Filter != null && Filter.contains(",")) {
				String[] parts = Filter.split(",");
				if (parts.length >= 2) {
					rowId = parts[0];
					columnId = parts[1];
				}
			}
			System.out.println(type);
			if ("ARCHIVAL".equals(type) && version != null) {
				System.out.println(type);
				// 🔹 Archival branch
				List<BDISB1_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BDISB1_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate,
							version);
				} else {
					T1Dt1 = BDISB1_Archival_Detail_Repo.getdatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<BDISB1_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = BDISB1_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = BDISB1_Detail_Repo.getdatabydateList(parsedDate);
					System.out.println("bdisb2 size is : " + T1Dt1.size());
					totalPages = BDISB1_Detail_Repo.getdatacount(parsedDate);
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
		mv.setViewName("BRRS/BDISB1");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}
	
	
	
	public void updateDetailFromForm(Date reportDate, Map<String, String> params) {

	    System.out.println("Updating BDISB1 detail table");

	    for (Map.Entry<String, String> entry : params.entrySet()) {

	        String key = entry.getKey();
	        String value = entry.getValue();

	        // ✅ Allow only valid BDISB1 keys
	        if (!key.matches(
	                "R\\d+_C\\d+_(" +
	                        "RECORD_NUMBER|" +
	                        "TITLE|" +
	                        "FIRST_NAME|" +
	                        "MIDDLE_NAME|" +
	                        "SURNAME|" +
	                        "PREVIOUS_NAME|" +
	                        "GENDER|" +
	                        "IDENTIFICATION_TYPE|" +
	                        "PASSPORT_NUMBER|" +
	                        "DATE_OF_BIRTH|" +
	                        "HOME_ADDRESS|" +
	                        "POSTAL_ADDRESS|" +
	                        "RESIDENCE|" +
	                        "EMAIL|" +
	                        "LANDLINE|" +
	                        "MOBILE_PHONE_NUMBER|" +
	                        "MOBILE_MONEY_NUMBER|" +
	                        "PRODUCT_TYPE|" +
	                        "ACCOUNT_BY_OWNERSHIP|" +
	                        "ACCOUNT_NUMBER|" +
	                        "ACCOUNT_HOLDER_INDICATOR|" +
	                        "STATUS_OF_ACCOUNT|" +
	                        "NOT_FIT_FOR_STP|" +
	                        "BRANCH_CODE_AND_NAME|" +
	                        "ACCOUNT_BALANCE_IN_PULA|" +
	                        "CURRENCY_OF_ACCOUNT|" +
	                        "EXCHANGE_RATE" +
	                        ")"
	        )) {
	            continue;
	        }

	        // 🔹 Parse key parts
	        String[] parts = key.split("_");
	        String reportLable = parts[0];      // R5, R6...
	        String addlCriteria = parts[1];     // C1, C2...
	        String columnName = key.replaceFirst("R\\d+_C\\d+_", "");

	        // 🔹 Fetch rows
	        List<BDISB1_Detail_Entity> rows =
	                BDISB1_Detail_Repo
	                        .findByReportDateAndReportLableAndReportAddlCriteria1(
	                                reportDate, reportLable, addlCriteria);

	        for (BDISB1_Detail_Entity row : rows) {

	            /* =======================
	               NUMERIC COLUMNS
	               ======================= */

	            if ("ACCOUNT_HOLDER_INDICATOR".equals(columnName)) {

	                BigDecimal num = (value == null || value.trim().isEmpty())
	                        ? BigDecimal.ZERO
	                        : new BigDecimal(value.replace(",", ""));
	                row.setACCOUNT_HOLDER_INDICATOR(num);

	            } else if ("ACCOUNT_BALANCE_IN_PULA".equals(columnName)) {

	                BigDecimal num = (value == null || value.trim().isEmpty())
	                        ? BigDecimal.ZERO
	                        : new BigDecimal(value.replace(",", ""));
	                row.setACCOUNT_BALANCE_IN_PULA(num);

	            } else if ("EXCHANGE_RATE".equals(columnName)) {

	                BigDecimal num = (value == null || value.trim().isEmpty())
	                        ? BigDecimal.ZERO
	                        : new BigDecimal(value.replace(",", ""));
	                row.setEXCHANGE_RATE(num);
	            }

	            /* =======================
	               DATE COLUMN
	               ======================= */
	            else if ("DATE_OF_BIRTH".equals(columnName)) {

	                if (value == null || value.trim().isEmpty()) {
	                    row.setDATE_OF_BIRTH(null);
	                } else {
	                    try {
	                        // HTML <input type="date"> sends yyyy-MM-dd
	                        LocalDate localDate = LocalDate.parse(value);
	                        row.setDATE_OF_BIRTH(java.sql.Date.valueOf(localDate));
	                    } catch (Exception e) {
	                        throw new RuntimeException("Invalid DATE_OF_BIRTH: " + value);
	                    }
	                }
	            }


	            /* =======================
	               STRING COLUMNS
	               ======================= */
	            else if ("RECORD_NUMBER".equals(columnName)) {
	                row.setRECORD_NUMBER(value);

	            } else if ("TITLE".equals(columnName)) {
	                row.setTITLE(value);

	            } else if ("FIRST_NAME".equals(columnName)) {
	                row.setFIRST_NAME(value);

	            } else if ("MIDDLE_NAME".equals(columnName)) {
	                row.setMIDDLE_NAME(value);

	            } else if ("SURNAME".equals(columnName)) {
	                row.setSURNAME(value);

	            } else if ("PREVIOUS_NAME".equals(columnName)) {
	                row.setPREVIOUS_NAME(value);

	            } else if ("GENDER".equals(columnName)) {
	                row.setGENDER(value);

	            } else if ("IDENTIFICATION_TYPE".equals(columnName)) {
	                row.setIDENTIFICATION_TYPE(value);

	            } else if ("PASSPORT_NUMBER".equals(columnName)) {
	                row.setPASSPORT_NUMBER(value);

	            } else if ("HOME_ADDRESS".equals(columnName)) {
	                row.setHOME_ADDRESS(value);

	            } else if ("POSTAL_ADDRESS".equals(columnName)) {
	                row.setPOSTAL_ADDRESS(value);

	            } else if ("RESIDENCE".equals(columnName)) {
	                row.setRESIDENCE(value);

	            } else if ("EMAIL".equals(columnName)) {
	                row.setEMAIL(value);

	            } else if ("LANDLINE".equals(columnName)) {
	                row.setLANDLINE(value);

	            } else if ("MOBILE_PHONE_NUMBER".equals(columnName)) {
	                row.setMOBILE_PHONE_NUMBER(value);

	            } else if ("MOBILE_MONEY_NUMBER".equals(columnName)) {
	                row.setMOBILE_MONEY_NUMBER(value);

	            } else if ("PRODUCT_TYPE".equals(columnName)) {
	                row.setPRODUCT_TYPE(value);

	            } else if ("ACCOUNT_BY_OWNERSHIP".equals(columnName)) {
	                row.setACCOUNT_BY_OWNERSHIP(value);

	            } else if ("ACCOUNT_NUMBER".equals(columnName)) {
	                row.setACCOUNT_NUMBER(value);

	            } else if ("STATUS_OF_ACCOUNT".equals(columnName)) {
	                row.setSTATUS_OF_ACCOUNT(value);

	            } else if ("NOT_FIT_FOR_STP".equals(columnName)) {
	                row.setNOT_FIT_FOR_STP(value);

	            } else if ("BRANCH_CODE_AND_NAME".equals(columnName)) {
	                row.setBRANCH_CODE_AND_NAME(value);

	            } else if ("CURRENCY_OF_ACCOUNT".equals(columnName)) {
	                row.setCURRENCY_OF_ACCOUNT(value);
	            }

	            // 🔹 Mark modified
	            row.setModifyFlg("Y");
	        }

	        BDISB1_Detail_Repo.saveAll(rows);
	    }

	    callSummaryProcedure(reportDate);
	}

	
	private void callSummaryProcedure(Date reportDate) {

		String sql = "{ call BRRS_BDISB1_SUMMARY_PROCEDURE(?) }";

		jdbcTemplate.update(connection -> {
			CallableStatement cs = connection.prepareCall(sql);

			// Force exact format expected by procedure
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			sdf.setLenient(false);

			String formattedDate = sdf.format(reportDate);

			cs.setString(1, formattedDate); // 🔥 THIS IS MANDATORY
			return cs;
		});

		System.out.println(
				"✅ Summary procedure executed for date: " + new SimpleDateFormat("dd-MM-yyyy").format(reportDate));
	}
	
	
//	public void updateReport(BDISB1_Summary_Entity updatedEntity) {
//	    System.out.println("Came to services");
//		    System.out.println("Report Date: " + updatedEntity.getReportDate());
//
//		    BDISB1_Summary_Entity existing = BDISB1_Summary_Repo.findById(updatedEntity.getReportDate())
//		            .orElseThrow(() -> new RuntimeException(
//	                    "Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

//	try {
	    // 1️⃣ Loop through R14 to R100
//	    for (int i = 5; i <= 11; i++) {
//	        String prefix = "R" + i + "_";

//	        String[] fields = {
//	        		"RECORD_NUMBER",
//	        		"TITLE",
//	        		"FIRST_NAME",
//	        		"MIDDLE_NAME",
//	        		"SURNAME",
//	        		"PREVIOUS_NAME",
//	        		"GENDER",
//	        		"IDENTIFICATION_TYPE",
//	        		"PASSPORT_NUMBER",
//	        		"DATE_OF_BIRTH",
//	        		"HOME_ADDRESS",
//	        		"POSTAL_ADDRESS",
//	        		"RESIDENCE",
//	        		"EMAIL",
//	        		"LANDLINE",
//	        		"MOBILE_PHONE_NUMBER",
//	        		"MOBILE_MONEY_NUMBER",
//	        		"PRODUCT_TYPE",
//	        		"ACCOUNT_BY_OWNERSHIP",
//	        		"ACCOUNT_NUMBER",
//	        		"ACCOUNT_HOLDER_INDICATOR",
//	        		"STATUS_OF_ACCOUNT",
//	        		"NOT_FIT_FOR_STP",
//	        		"BRANCH_CODE_AND_NAME",
//	        		"ACCOUNT_BALANCE_IN_PULA",
//	        		"CURRENCY_OF_ACCOUNT",
//	        		"EXCHANGE_RATE"

//	        };

//	        for (String field : fields) {
//	            String getterName = "get" + prefix + field;
//	            String setterName = "set" + prefix + field;

//	            try {
//	                Method getter = BDISB1_Summary_Entity.class.getMethod(getterName);
//	                Method setter = BDISB1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);

//	            } catch (NoSuchMethodException e) {
//	                // Skip missing fields
//	                continue;
//	            }
//	        }
//	    }

	    // 2️⃣ Handle R100 total fields using same structure
//	      String prefix = "R11_";
//	      String[] totalFields = {
//	      		"TITLE",
//	      		"FIRST_NAME",
//	      		"MIDDLE_NAME",
//	      		"SURNAME",
//	      		"PREVIOUS_NAME",
//	      		"GENDER",
//	      		"IDENTIFICATION_TYPE",
//	      		"PASSPORT_NUMBER",
//	      		"DATE_OF_BIRTH",
//	      		"HOME_ADDRESS",
//	      		"POSTAL_ADDRESS",
//	      		"RESIDENCE",
//	      		"EMAIL",
//	      		"LANDLINE",
//	      		"MOBILE_PHONE_NUMBER",
//	      		"MOBILE_MONEY_NUMBER",
//	      		"PRODUCT_TYPE",
//	      		"ACCOUNT_BY_OWNERSHIP",
//	      		"ACCOUNT_NUMBER",
//	      		"ACCOUNT_HOLDER_INDICATOR",
//	      		"STATUS_OF_ACCOUNT",
//	      		"NOT_FIT_FOR_STP",
//	      		"BRANCH_CODE_AND_NAME",
//	      		"ACCOUNT_BALANCE_IN_PULA",
//	      		"CURRENCY_OF_ACCOUNT",
//	      		"EXCHANGE_RATE"

//	      };

//	      for (String field : totalFields) {
//	          String getterName = "get" + prefix + field;
//	          String setterName = "set" + prefix + field;

//	          try {
//	              Method getter = BDISB1_Summary_Entity.class.getMethod(getterName);
//	              Method setter = BDISB1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

//	              Object newValue = getter.invoke(updatedEntity);
//	              setter.invoke(existing, newValue);

//	          } catch (NoSuchMethodException e) {
	              // Skip missing total fields
//	              continue;
//	          }
//	      }

//	  } catch (Exception e) {
//	      throw new RuntimeException("Error while updating report fields", e);
//	  }

//	  try {
//	  	existing.setR5_DATE_OF_BIRTH(updatedEntity.getR5_DATE_OF_BIRTH());
//	  	existing.setR6_DATE_OF_BIRTH(updatedEntity.getR6_DATE_OF_BIRTH());
//	  	existing.setR7_DATE_OF_BIRTH(updatedEntity.getR7_DATE_OF_BIRTH());
//	  	existing.setR8_DATE_OF_BIRTH(updatedEntity.getR8_DATE_OF_BIRTH());
//	  	existing.setR9_DATE_OF_BIRTH(updatedEntity.getR9_DATE_OF_BIRTH());
//	  	existing.setR10_DATE_OF_BIRTH(updatedEntity.getR10_DATE_OF_BIRTH());
//	  	existing.setR11_DATE_OF_BIRTH(updatedEntity.getR11_DATE_OF_BIRTH());
		

		    	
		        
//		    } catch (Exception e) {
//		        throw new RuntimeException("Error while updating date fields", e);
//		    }
		    
		   
		    // 3️⃣ Save updated entity
	//	    BDISB1_Summary_Repo.save(existing);
	//	}



	


	//public void updateArchivalReport(BDISB1_Archival_Summary_Entity updatedEntity) {

	//    System.out.println("Came to services 1");
	//    System.out.println("Report Date: " + updatedEntity.getReportDate());
	//    System.out.println("Report Version: " + updatedEntity.getReportVersion());

	    // Composite PK
	//    BDISB1_Archival_Summary_PK pk =
	//            new BDISB1_Archival_Summary_PK(
	//                    updatedEntity.getReportDate(),
	//                    updatedEntity.getReportVersion()
	//            );

	//    BDISB1_Archival_Summary_Entity existing =
	//            BDISB1_Archival_Summary_Repo.findById(pk)
	//            .orElseThrow(() -> new RuntimeException(
	//                    "Record not found for REPORT_DATE: "
	//                            + updatedEntity.getReportDate()
	//                            + " and REPORT_VERSION: "
	//                            + updatedEntity.getReportVersion()
	//            ));

	//    try {
	 //       for (int i = 5; i <= 11; i++) {

	 //           String prefix = "R" + i + "_";

	 //          String[] fields = {
	//                "RECORD_NUMBER",
	//                "TITLE",
	//                "FIRST_NAME",
	//                "MIDDLE_NAME",
	//                "SURNAME",
	//                "PREVIOUS_NAME",
	//                "GENDER",
	//                "IDENTIFICATION_TYPE",
	//                "PASSPORT_NUMBER",
	//                "DATE_OF_BIRTH",
	//                "HOME_ADDRESS",
	//                "POSTAL_ADDRESS",
	//                "RESIDENCE",
	//                "EMAIL",
	//                "LANDLINE",
	//                "MOBILE_PHONE_NUMBER",
	//                "MOBILE_MONEY_NUMBER",
	//                "PRODUCT_TYPE",
	//                "ACCOUNT_BY_OWNERSHIP",
	//                "ACCOUNT_NUMBER",
	//                "ACCOUNT_HOLDER_INDICATOR",
	//                "STATUS_OF_ACCOUNT",
	//                "NOT_FIT_FOR_STP",
	//                "BRANCH_CODE_AND_NAME",
	//                "ACCOUNT_BALANCE_IN_PULA",
	//                "CURRENCY_OF_ACCOUNT",
	//                "EXCHANGE_RATE"
	//            };

	//           for (String field : fields) {

	//                String getterName = "get" + prefix + field;
	//               String setterName = "set" + prefix + field;

	//              try {
	//                    Method getter =
	//                            BDISB1_Archival_Summary_Entity.class.getMethod(getterName);

	//                    Method setter =
	//                            BDISB1_Archival_Summary_Entity.class
	//                                    .getMethod(setterName, getter.getReturnType());

	//                   Object newValue = getter.invoke(updatedEntity);

	                    // ✅ DO NOT overwrite with null
	//                    if (newValue != null) {
	//                        setter.invoke(existing, newValue);
	//                   }

	//              } catch (NoSuchMethodException e) {
	//                   System.out.println("Missing field: " + getterName);
	//                }
//           }
	//       }

	        // Safety log
	//       System.out.println("Before Save → VERSION = " + existing.getReportVersion());

	//      BDISB1_Archival_Summary_Repo.save(existing);

	//   } catch (Exception e) {
	//       throw new RuntimeException("Error while updating report fields", e);
	        //    }
	//}

	


	
	public byte[] getBDISB1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			 String dtltype, String type, String version) throws Exception {
logger.info("Service: Starting Excel generation process in memory.");
logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
Date reportDate = dateformat.parse(todate);

// ARCHIVAL check
if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating ARCHIVAL report for version {}", version);
return getExcelBDISB1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}
// RESUB check
else if ("RESUB".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
logger.info("Service: Generating RESUB report for version {}", version);


List<BDISB1_Archival_Summary_Entity> T1Master =
BDISB1_Archival_Summary_Repo.getdatabydateListarchival(reportDate, version);

// Generate Excel for RESUB
return BRRSBDISB1ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
}




// Default (LIVE) case
List<BDISB1_Summary_Entity> dataList1 = BDISB1_Summary_Repo.getdatabydateList(reportDate);

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

int startRow = 4;

if (!dataList1.isEmpty()) {
for (int i = 0; i < dataList1.size(); i++) {

BDISB1_Summary_Entity record = dataList1.get(i);
System.out.println("rownumber=" + startRow + i);
Row row = sheet.getRow(startRow + i);
if (row == null) {
row = sheet.createRow(startRow + i);
}




//Cell1 - R5_TITLE
Cell cell0 = row.createCell(0);
if (record.getR5_RECORD_NUMBER() != null) {
cell0.setCellValue(record.getR5_RECORD_NUMBER());
cell0.setCellStyle(textStyle);
} else {
cell0.setCellValue("");
cell0.setCellStyle(textStyle);
}

//Cell1 - R5_TITLE
Cell cell1 = row.createCell(1);
if (record.getR5_TITLE() != null) {
 cell1.setCellValue(record.getR5_TITLE());
 cell1.setCellStyle(textStyle);
} else {
 cell1.setCellValue("");
 cell1.setCellStyle(textStyle);
}

//Cell2 - R5_FIRST_NAME
Cell cell2 = row.createCell(2);
if (record.getR5_FIRST_NAME() != null) {
 cell2.setCellValue(record.getR5_FIRST_NAME());
 cell2.setCellStyle(textStyle);
} else {
 cell2.setCellValue("");
 cell2.setCellStyle(textStyle);
}

//Cell3 - R5_MIDDLE_NAME
Cell cell3 = row.createCell(3);
if (record.getR5_MIDDLE_NAME() != null) {
 cell3.setCellValue(record.getR5_MIDDLE_NAME());
 cell3.setCellStyle(textStyle);
} else {
 cell3.setCellValue("");
 cell3.setCellStyle(textStyle);
}

//Cell4 - R5_SURNAME
Cell cell4 = row.createCell(4);
if (record.getR5_SURNAME() != null) {
 cell4.setCellValue(record.getR5_SURNAME());
 cell4.setCellStyle(textStyle);
} else {
 cell4.setCellValue("");
 cell4.setCellStyle(textStyle);
}

//Cell5 - R5_PREVIOUS_NAME
Cell cell5 = row.createCell(5);
if (record.getR5_PREVIOUS_NAME() != null) {
 cell5.setCellValue(record.getR5_PREVIOUS_NAME());
 cell5.setCellStyle(textStyle);
} else {
 cell5.setCellValue("");
 cell5.setCellStyle(textStyle);
}

//Cell6 - R5_GENDER
Cell cell6 = row.createCell(6);
if (record.getR5_GENDER() != null) {
 cell6.setCellValue(record.getR5_GENDER());
 cell6.setCellStyle(textStyle);
} else {
 cell6.setCellValue("");
 cell6.setCellStyle(textStyle);
}

//Cell7 - R5_IDENTIFICATION_TYPE
Cell cell7 = row.createCell(7);
if (record.getR5_IDENTIFICATION_TYPE() != null) {
 cell7.setCellValue(record.getR5_IDENTIFICATION_TYPE());
 cell7.setCellStyle(textStyle);
} else {
 cell7.setCellValue("");
 cell7.setCellStyle(textStyle);
}

//Cell8 - R5_PASSPORT_NUMBER
Cell cell8 = row.createCell(8);
if (record.getR5_PASSPORT_NUMBER() != null) {
 cell8.setCellValue(record.getR5_PASSPORT_NUMBER());
 cell8.setCellStyle(textStyle);
} else {
 cell8.setCellValue("");
 cell8.setCellStyle(textStyle);
}

//Cell9 - R5_DATE_OF_BIRTH
Cell cell9 = row.createCell(9);
if (record.getR5_DATE_OF_BIRTH() != null) {
 cell9.setCellValue(record.getR5_DATE_OF_BIRTH());
 cell9.setCellStyle(dateStyle);
} else {
 cell9.setCellValue("");
 cell9.setCellStyle(textStyle);
}

//Cell10 - R5_HOME_ADDRESS
Cell cell10 = row.createCell(10);
if (record.getR5_HOME_ADDRESS() != null) {
 cell10.setCellValue(record.getR5_HOME_ADDRESS());
 cell10.setCellStyle(textStyle);
} else {
 cell10.setCellValue("");
 cell10.setCellStyle(textStyle);
}

//Cell11 - R5_POSTAL_ADDRESS
Cell cell11 = row.createCell(11);
if (record.getR5_POSTAL_ADDRESS() != null) {
 cell11.setCellValue(record.getR5_POSTAL_ADDRESS());
 cell11.setCellStyle(textStyle);
} else {
 cell11.setCellValue("");
 cell11.setCellStyle(textStyle);
}

//Cell12 - R5_RESIDENCE
Cell cell12 = row.createCell(12);
if (record.getR5_RESIDENCE() != null) {
 cell12.setCellValue(record.getR5_RESIDENCE());
 cell12.setCellStyle(textStyle);
} else {
 cell12.setCellValue("");
 cell12.setCellStyle(textStyle);
}

//Cell13 - R5_EMAIL
Cell cell13 = row.createCell(13);
if (record.getR5_EMAIL() != null) {
 cell13.setCellValue(record.getR5_EMAIL());
 cell13.setCellStyle(textStyle);
} else {
 cell13.setCellValue("");
 cell13.setCellStyle(textStyle);
}

//Cell14 - R5_LANDLINE
Cell cell14 = row.createCell(14);
if (record.getR5_LANDLINE() != null) {
 cell14.setCellValue(record.getR5_LANDLINE());
 cell14.setCellStyle(textStyle);
} else {
 cell14.setCellValue("");
 cell14.setCellStyle(textStyle);
}

//Cell15 - R5_MOBILE_PHONE_NUMBER
Cell cell15 = row.createCell(15);
if (record.getR5_MOBILE_PHONE_NUMBER() != null) {
 cell15.setCellValue(record.getR5_MOBILE_PHONE_NUMBER());
 cell15.setCellStyle(textStyle);
} else {
 cell15.setCellValue("");
 cell15.setCellStyle(textStyle);
}

//Cell16 - R5_MOBILE_MONEY_NUMBER
Cell cell16 = row.createCell(16);
if (record.getR5_MOBILE_MONEY_NUMBER() != null) {
 cell16.setCellValue(record.getR5_MOBILE_MONEY_NUMBER());
 cell16.setCellStyle(textStyle);
} else {
 cell16.setCellValue("");
 cell16.setCellStyle(textStyle);
}

//Cell17 - R5_PRODUCT_TYPE
Cell cell17 = row.createCell(17);
if (record.getR5_PRODUCT_TYPE() != null) {
 cell17.setCellValue(record.getR5_PRODUCT_TYPE());
 cell17.setCellStyle(textStyle);
} else {
 cell17.setCellValue("");
 cell17.setCellStyle(textStyle);
}

//Cell18 - R5_ACCOUNT_BY_OWNERSHIP
Cell cell18 = row.createCell(18);
if (record.getR5_ACCOUNT_BY_OWNERSHIP() != null) {
 cell18.setCellValue(record.getR5_ACCOUNT_BY_OWNERSHIP());
 cell18.setCellStyle(textStyle);
} else {
 cell18.setCellValue("");
 cell18.setCellStyle(textStyle);
}

//Cell19 - R5_ACCOUNT_NUMBER
Cell cell19 = row.createCell(19);
if (record.getR5_ACCOUNT_NUMBER() != null) {
 cell19.setCellValue(record.getR5_ACCOUNT_NUMBER());
 cell19.setCellStyle(textStyle);
} else {
 cell19.setCellValue("");
 cell19.setCellStyle(textStyle);
}

//Cell20 - R5_ACCOUNT_HOLDER_INDICATOR
Cell cell20 = row.createCell(20);
if (record.getR5_ACCOUNT_HOLDER_INDICATOR() != null) {
	  cell20.setCellValue(record.getR5_ACCOUNT_HOLDER_INDICATOR().doubleValue());
	  cell20.setCellStyle(numberStyle);
	} else {
	  cell20.setCellValue("");
	  cell20.setCellStyle(textStyle);
	}

//Cell21 - R5_STATUS_OF_ACCOUNT
Cell cell21 = row.createCell(21);
if (record.getR5_STATUS_OF_ACCOUNT() != null) {
 cell21.setCellValue(record.getR5_STATUS_OF_ACCOUNT());
 cell21.setCellStyle(textStyle);
} else {
 cell21.setCellValue("");
 cell21.setCellStyle(textStyle);
}

//Cell22 - R5_NOT_FIT_FOR_STP
Cell cell22 = row.createCell(22);
if (record.getR5_NOT_FIT_FOR_STP() != null) {
 cell22.setCellValue(record.getR5_NOT_FIT_FOR_STP());
 cell22.setCellStyle(textStyle);
} else {
 cell22.setCellValue("");
 cell22.setCellStyle(textStyle);
}

//Cell23 - R5_BRANCH_CODE_AND_NAME
Cell cell23 = row.createCell(23);
if (record.getR5_BRANCH_CODE_AND_NAME() != null) {
 cell23.setCellValue(record.getR5_BRANCH_CODE_AND_NAME());
 cell23.setCellStyle(textStyle);
} else {
 cell23.setCellValue("");
 cell23.setCellStyle(textStyle);
}

//Cell24 - R5_ACCOUNT_BALANCE_IN_PULA
Cell cell24 = row.createCell(24);
if (record.getR5_ACCOUNT_BALANCE_IN_PULA() != null) {
 cell24.setCellValue(record.getR5_ACCOUNT_BALANCE_IN_PULA().doubleValue());
 cell24.setCellStyle(numberStyle);
} else {
 cell24.setCellValue("");
 cell24.setCellStyle(textStyle);
}

//Cell25 - R5_CURRENCY_OF_ACCOUNT
Cell cell25 = row.createCell(25);
if (record.getR5_CURRENCY_OF_ACCOUNT() != null) {
 cell25.setCellValue(record.getR5_CURRENCY_OF_ACCOUNT());
 cell25.setCellStyle(textStyle);
} else {
 cell25.setCellValue("");
 cell25.setCellStyle(textStyle);
}

//Cell26 - R5_EXCHANGE_RATE
Cell cell26 = row.createCell(26);
if (record.getR5_EXCHANGE_RATE() != null) {
 cell26.setCellValue(record.getR5_EXCHANGE_RATE().doubleValue());
 cell26.setCellStyle(numberStyle);
} else {
 cell26.setCellValue("");
 cell26.setCellStyle(textStyle);
}


row = sheet.getRow(5);
//====================== R6 ======================

//Cell1 - R5_TITLE
cell0 = row.createCell(0);
if (record.getR6_RECORD_NUMBER() != null) {
cell0.setCellValue(record.getR6_RECORD_NUMBER());
cell0.setCellStyle(textStyle);
} else {
cell0.setCellValue("");
cell0.setCellStyle(textStyle);
}

//Cell1 - R6_TITLE
cell1 = row.createCell(1);
if (record.getR6_TITLE() != null) {
  cell1.setCellValue(record.getR6_TITLE());
  cell1.setCellStyle(textStyle);
} else {
  cell1.setCellValue("");
  cell1.setCellStyle(textStyle);
}

//Cell2 - R6_FIRST_NAME
cell2 = row.createCell(2);
if (record.getR6_FIRST_NAME() != null) {
  cell2.setCellValue(record.getR6_FIRST_NAME());
  cell2.setCellStyle(textStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}

//Cell3 - R6_MIDDLE_NAME
cell3 = row.createCell(3);
if (record.getR6_MIDDLE_NAME() != null) {
  cell3.setCellValue(record.getR6_MIDDLE_NAME());
  cell3.setCellStyle(textStyle);
} else {
  cell3.setCellValue("");
  cell3.setCellStyle(textStyle);
}

//Cell4 - R6_SURNAME
cell4 = row.createCell(4);
if (record.getR6_SURNAME() != null) {
  cell4.setCellValue(record.getR6_SURNAME());
  cell4.setCellStyle(textStyle);
} else {
  cell4.setCellValue("");
  cell4.setCellStyle(textStyle);
}

//Cell5 - R6_PREVIOUS_NAME
cell5 = row.createCell(5);
if (record.getR6_PREVIOUS_NAME() != null) {
  cell5.setCellValue(record.getR6_PREVIOUS_NAME());
  cell5.setCellStyle(textStyle);
} else {
  cell5.setCellValue("");
  cell5.setCellStyle(textStyle);
}

//Cell6 - R6_GENDER
cell6 = row.createCell(6);
if (record.getR6_GENDER() != null) {
  cell6.setCellValue(record.getR6_GENDER());
  cell6.setCellStyle(textStyle);
} else {
  cell6.setCellValue("");
  cell6.setCellStyle(textStyle);
}

//Cell7 - R6_IDENTIFICATION_TYPE
cell7 = row.createCell(7);
if (record.getR6_IDENTIFICATION_TYPE() != null) {
  cell7.setCellValue(record.getR6_IDENTIFICATION_TYPE());
  cell7.setCellStyle(textStyle);
} else {
  cell7.setCellValue("");
  cell7.setCellStyle(textStyle);
}

//Cell8 - R6_PASSPORT_NUMBER
cell8 = row.createCell(8);
if (record.getR6_PASSPORT_NUMBER() != null) {
  cell8.setCellValue(record.getR6_PASSPORT_NUMBER());
  cell8.setCellStyle(textStyle);
} else {
  cell8.setCellValue("");
  cell8.setCellStyle(textStyle);
}

//Cell9 - R6_DATE_OF_BIRTH
cell9 = row.createCell(9);
if (record.getR6_DATE_OF_BIRTH() != null) {
  cell9.setCellValue(record.getR6_DATE_OF_BIRTH());
  cell9.setCellStyle(dateStyle);
} else {
  cell9.setCellValue("");
  cell9.setCellStyle(textStyle);
}

//Cell10 - R6_HOME_ADDRESS
cell10 = row.createCell(10);
if (record.getR6_HOME_ADDRESS() != null) {
  cell10.setCellValue(record.getR6_HOME_ADDRESS());
  cell10.setCellStyle(textStyle);
} else {
  cell10.setCellValue("");
  cell10.setCellStyle(textStyle);
}

//Cell11 - R6_POSTAL_ADDRESS
cell11 = row.createCell(11);
if (record.getR6_POSTAL_ADDRESS() != null) {
  cell11.setCellValue(record.getR6_POSTAL_ADDRESS());
  cell11.setCellStyle(textStyle);
} else {
  cell11.setCellValue("");
  cell11.setCellStyle(textStyle);
}

//Cell12 - R6_RESIDENCE
cell12 = row.createCell(12);
if (record.getR6_RESIDENCE() != null) {
  cell12.setCellValue(record.getR6_RESIDENCE());
  cell12.setCellStyle(textStyle);
} else {
  cell12.setCellValue("");
  cell12.setCellStyle(textStyle);
}

//Cell13 - R6_EMAIL
cell13 = row.createCell(13);
if (record.getR6_EMAIL() != null) {
  cell13.setCellValue(record.getR6_EMAIL());
  cell13.setCellStyle(textStyle);
} else {
  cell13.setCellValue("");
  cell13.setCellStyle(textStyle);
}

//Cell14 - R6_LANDLINE
cell14 = row.createCell(14);
if (record.getR6_LANDLINE() != null) {
  cell14.setCellValue(record.getR6_LANDLINE());
  cell14.setCellStyle(textStyle);
} else {
  cell14.setCellValue("");
  cell14.setCellStyle(textStyle);
}

//Cell15 - R6_MOBILE_PHONE_NUMBER
cell15 = row.createCell(15);
if (record.getR6_MOBILE_PHONE_NUMBER() != null) {
  cell15.setCellValue(record.getR6_MOBILE_PHONE_NUMBER());
  cell15.setCellStyle(textStyle);
} else {
  cell15.setCellValue("");
  cell15.setCellStyle(textStyle);
}

//Cell16 - R6_MOBILE_MONEY_NUMBER
cell16 = row.createCell(16);
if (record.getR6_MOBILE_MONEY_NUMBER() != null) {
  cell16.setCellValue(record.getR6_MOBILE_MONEY_NUMBER());
  cell16.setCellStyle(textStyle);
} else {
  cell16.setCellValue("");
  cell16.setCellStyle(textStyle);
}

//Cell17 - R6_PRODUCT_TYPE
cell17 = row.createCell(17);
if (record.getR6_PRODUCT_TYPE() != null) {
  cell17.setCellValue(record.getR6_PRODUCT_TYPE());
  cell17.setCellStyle(textStyle);
} else {
  cell17.setCellValue("");
  cell17.setCellStyle(textStyle);
}

//Cell18 - R6_ACCOUNT_BY_OWNERSHIP
cell18 = row.createCell(18);
if (record.getR6_ACCOUNT_BY_OWNERSHIP() != null) {
  cell18.setCellValue(record.getR6_ACCOUNT_BY_OWNERSHIP());
  cell18.setCellStyle(textStyle);
} else {
  cell18.setCellValue("");
  cell18.setCellStyle(textStyle);
}

//Cell19 - R6_ACCOUNT_NUMBER
cell19 = row.createCell(19);
if (record.getR6_ACCOUNT_NUMBER() != null) {
  cell19.setCellValue(record.getR6_ACCOUNT_NUMBER());
  cell19.setCellStyle(textStyle);
} else {
  cell19.setCellValue("");
  cell19.setCellStyle(textStyle);
}

//Cell20 - R6_ACCOUNT_HOLDER_INDICATOR
cell20 = row.createCell(20);
if (record.getR6_ACCOUNT_HOLDER_INDICATOR() != null) {
  cell20.setCellValue(record.getR6_ACCOUNT_HOLDER_INDICATOR().doubleValue());
  cell20.setCellStyle(numberStyle);
} else {
  cell20.setCellValue("");
  cell20.setCellStyle(textStyle);
}

//Cell21 - R6_STATUS_OF_ACCOUNT
cell21 = row.createCell(21);
if (record.getR6_STATUS_OF_ACCOUNT() != null) {
  cell21.setCellValue(record.getR6_STATUS_OF_ACCOUNT());
  cell21.setCellStyle(textStyle);
} else {
  cell21.setCellValue("");
  cell21.setCellStyle(textStyle);
}

//Cell22 - R6_NOT_FIT_FOR_STP
cell22 = row.createCell(22);
if (record.getR6_NOT_FIT_FOR_STP() != null) {
  cell22.setCellValue(record.getR6_NOT_FIT_FOR_STP());
  cell22.setCellStyle(textStyle);
} else {
  cell22.setCellValue("");
  cell22.setCellStyle(textStyle);
}

//Cell23 - R6_BRANCH_CODE_AND_NAME
cell23 = row.createCell(23);
if (record.getR6_BRANCH_CODE_AND_NAME() != null) {
  cell23.setCellValue(record.getR6_BRANCH_CODE_AND_NAME());
  cell23.setCellStyle(textStyle);
} else {
  cell23.setCellValue("");
  cell23.setCellStyle(textStyle);
}

//Cell24 - R6_ACCOUNT_BALANCE_IN_PULA
cell24 = row.createCell(24);
if (record.getR6_ACCOUNT_BALANCE_IN_PULA() != null) {
  cell24.setCellValue(record.getR6_ACCOUNT_BALANCE_IN_PULA().doubleValue());
  cell24.setCellStyle(numberStyle);
} else {
  cell24.setCellValue("");
  cell24.setCellStyle(textStyle);
}

//Cell25 - R6_CURRENCY_OF_ACCOUNT
cell25 = row.createCell(25);
if (record.getR6_CURRENCY_OF_ACCOUNT() != null) {
  cell25.setCellValue(record.getR6_CURRENCY_OF_ACCOUNT());
  cell25.setCellStyle(textStyle);
} else {
  cell25.setCellValue("");
  cell25.setCellStyle(textStyle);
}

//Cell26 - R6_EXCHANGE_RATE
cell26 = row.createCell(26);
if (record.getR6_EXCHANGE_RATE() != null) {
  cell26.setCellValue(record.getR6_EXCHANGE_RATE().doubleValue());
  cell26.setCellStyle(numberStyle);
} else {
  cell26.setCellValue("");
  cell26.setCellStyle(textStyle);
}



row = sheet.getRow(6);
//====================== R7 ======================

//Cell0 - R7_RECORD_NUMBER
cell0 = row.createCell(0);
if (record.getR7_RECORD_NUMBER() != null) {
cell0.setCellValue(record.getR7_RECORD_NUMBER());
cell0.setCellStyle(textStyle);
} else {
cell0.setCellValue("");
cell0.setCellStyle(textStyle);
}

//Cell1 - R7_TITLE
cell1 = row.createCell(1);
if (record.getR7_TITLE() != null) {
  cell1.setCellValue(record.getR7_TITLE());
  cell1.setCellStyle(textStyle);
} else {
  cell1.setCellValue("");
  cell1.setCellStyle(textStyle);
}

//Cell2 - R7_FIRST_NAME
cell2 = row.createCell(2);
if (record.getR7_FIRST_NAME() != null) {
  cell2.setCellValue(record.getR7_FIRST_NAME());
  cell2.setCellStyle(textStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}

//Cell3 - R7_MIDDLE_NAME
cell3 = row.createCell(3);
if (record.getR7_MIDDLE_NAME() != null) {
  cell3.setCellValue(record.getR7_MIDDLE_NAME());
  cell3.setCellStyle(textStyle);
} else {
  cell3.setCellValue("");
  cell3.setCellStyle(textStyle);
}

//Cell4 - R7_SURNAME
cell4 = row.createCell(4);
if (record.getR7_SURNAME() != null) {
  cell4.setCellValue(record.getR7_SURNAME());
  cell4.setCellStyle(textStyle);
} else {
  cell4.setCellValue("");
  cell4.setCellStyle(textStyle);
}

//Cell5 - R7_PREVIOUS_NAME
cell5 = row.createCell(5);
if (record.getR7_PREVIOUS_NAME() != null) {
  cell5.setCellValue(record.getR7_PREVIOUS_NAME());
  cell5.setCellStyle(textStyle);
} else {
  cell5.setCellValue("");
  cell5.setCellStyle(textStyle);
}

//Cell6 - R7_GENDER
cell6 = row.createCell(6);
if (record.getR7_GENDER() != null) {
  cell6.setCellValue(record.getR7_GENDER());
  cell6.setCellStyle(textStyle);
} else {
  cell6.setCellValue("");
  cell6.setCellStyle(textStyle);
}

//Cell7 - R7_IDENTIFICATION_TYPE
cell7 = row.createCell(7);
if (record.getR7_IDENTIFICATION_TYPE() != null) {
  cell7.setCellValue(record.getR7_IDENTIFICATION_TYPE());
  cell7.setCellStyle(textStyle);
} else {
  cell7.setCellValue("");
  cell7.setCellStyle(textStyle);
}

//Cell8 - R7_PASSPORT_NUMBER
cell8 = row.createCell(8);
if (record.getR7_PASSPORT_NUMBER() != null) {
  cell8.setCellValue(record.getR7_PASSPORT_NUMBER());
  cell8.setCellStyle(textStyle);
} else {
  cell8.setCellValue("");
  cell8.setCellStyle(textStyle);
}

//Cell9 - R7_DATE_OF_BIRTH
cell9 = row.createCell(9);
if (record.getR7_DATE_OF_BIRTH() != null) {
  cell9.setCellValue(record.getR7_DATE_OF_BIRTH());
  cell9.setCellStyle(dateStyle);
} else {
  cell9.setCellValue("");
  cell9.setCellStyle(textStyle);
}

//Cell10 - R7_HOME_ADDRESS
cell10 = row.createCell(10);
if (record.getR7_HOME_ADDRESS() != null) {
  cell10.setCellValue(record.getR7_HOME_ADDRESS());
  cell10.setCellStyle(textStyle);
} else {
  cell10.setCellValue("");
  cell10.setCellStyle(textStyle);
}

//Cell11 - R7_POSTAL_ADDRESS
cell11 = row.createCell(11);
if (record.getR7_POSTAL_ADDRESS() != null) {
  cell11.setCellValue(record.getR7_POSTAL_ADDRESS());
  cell11.setCellStyle(textStyle);
} else {
  cell11.setCellValue("");
  cell11.setCellStyle(textStyle);
}

//Cell12 - R7_RESIDENCE
cell12 = row.createCell(12);
if (record.getR7_RESIDENCE() != null) {
  cell12.setCellValue(record.getR7_RESIDENCE());
  cell12.setCellStyle(textStyle);
} else {
  cell12.setCellValue("");
  cell12.setCellStyle(textStyle);
}

//Cell13 - R7_EMAIL
cell13 = row.createCell(13);
if (record.getR7_EMAIL() != null) {
  cell13.setCellValue(record.getR7_EMAIL());
  cell13.setCellStyle(textStyle);
} else {
  cell13.setCellValue("");
  cell13.setCellStyle(textStyle);
}

//Cell14 - R7_LANDLINE
cell14 = row.createCell(14);
if (record.getR7_LANDLINE() != null) {
  cell14.setCellValue(record.getR7_LANDLINE());
  cell14.setCellStyle(textStyle);
} else {
  cell14.setCellValue("");
  cell14.setCellStyle(textStyle);
}

//Cell15 - R7_MOBILE_PHONE_NUMBER
cell15 = row.createCell(15);
if (record.getR7_MOBILE_PHONE_NUMBER() != null) {
  cell15.setCellValue(record.getR7_MOBILE_PHONE_NUMBER());
  cell15.setCellStyle(textStyle);
} else {
  cell15.setCellValue("");
  cell15.setCellStyle(textStyle);
}

//Cell16 - R7_MOBILE_MONEY_NUMBER
cell16 = row.createCell(16);
if (record.getR7_MOBILE_MONEY_NUMBER() != null) {
  cell16.setCellValue(record.getR7_MOBILE_MONEY_NUMBER());
  cell16.setCellStyle(textStyle);
} else {
  cell16.setCellValue("");
  cell16.setCellStyle(textStyle);
}

//Cell17 - R7_PRODUCT_TYPE
cell17 = row.createCell(17);
if (record.getR7_PRODUCT_TYPE() != null) {
  cell17.setCellValue(record.getR7_PRODUCT_TYPE());
  cell17.setCellStyle(textStyle);
} else {
  cell17.setCellValue("");
  cell17.setCellStyle(textStyle);
}

//Cell18 - R7_ACCOUNT_BY_OWNERSHIP
cell18 = row.createCell(18);
if (record.getR7_ACCOUNT_BY_OWNERSHIP() != null) {
  cell18.setCellValue(record.getR7_ACCOUNT_BY_OWNERSHIP());
  cell18.setCellStyle(textStyle);
} else {
  cell18.setCellValue("");
  cell18.setCellStyle(textStyle);
}

//Cell19 - R7_ACCOUNT_NUMBER
cell19 = row.createCell(19);
if (record.getR7_ACCOUNT_NUMBER() != null) {
  cell19.setCellValue(record.getR7_ACCOUNT_NUMBER());
  cell19.setCellStyle(textStyle);
} else {
  cell19.setCellValue("");
  cell19.setCellStyle(textStyle);
}

//Cell20 - R7_ACCOUNT_HOLDER_INDICATOR
cell20 = row.createCell(20);
if (record.getR11_ACCOUNT_HOLDER_INDICATOR() != null) {
  cell20.setCellValue(record.getR11_ACCOUNT_HOLDER_INDICATOR().doubleValue());
  cell20.setCellStyle(numberStyle);
} else {
  cell20.setCellValue("");
  cell20.setCellStyle(textStyle);
}

//Cell21 - R7_STATUS_OF_ACCOUNT
cell21 = row.createCell(21);
if (record.getR7_STATUS_OF_ACCOUNT() != null) {
  cell21.setCellValue(record.getR7_STATUS_OF_ACCOUNT());
  cell21.setCellStyle(textStyle);
} else {
  cell21.setCellValue("");
  cell21.setCellStyle(textStyle);
}

//Cell22 - R7_NOT_FIT_FOR_STP
cell22 = row.createCell(22);
if (record.getR7_NOT_FIT_FOR_STP() != null) {
  cell22.setCellValue(record.getR7_NOT_FIT_FOR_STP());
  cell22.setCellStyle(textStyle);
} else {
  cell22.setCellValue("");
  cell22.setCellStyle(textStyle);
}

//Cell23 - R7_BRANCH_CODE_AND_NAME
cell23 = row.createCell(23);
if (record.getR7_BRANCH_CODE_AND_NAME() != null) {
  cell23.setCellValue(record.getR7_BRANCH_CODE_AND_NAME());
  cell23.setCellStyle(textStyle);
} else {
  cell23.setCellValue("");
  cell23.setCellStyle(textStyle);
}

//Cell24 - R7_ACCOUNT_BALANCE_IN_PULA
cell24 = row.createCell(24);
if (record.getR7_ACCOUNT_BALANCE_IN_PULA() != null) {
  cell24.setCellValue(record.getR7_ACCOUNT_BALANCE_IN_PULA().doubleValue());
  cell24.setCellStyle(numberStyle);
} else {
  cell24.setCellValue("");
  cell24.setCellStyle(textStyle);
}

//Cell25 - R7_CURRENCY_OF_ACCOUNT
cell25 = row.createCell(25);
if (record.getR7_CURRENCY_OF_ACCOUNT() != null) {
  cell25.setCellValue(record.getR7_CURRENCY_OF_ACCOUNT());
  cell25.setCellStyle(textStyle);
} else {
  cell25.setCellValue("");
  cell25.setCellStyle(textStyle);
}

//Cell26 - R7_EXCHANGE_RATE
cell26 = row.createCell(26);
if (record.getR7_EXCHANGE_RATE() != null) {
  cell26.setCellValue(record.getR7_EXCHANGE_RATE().doubleValue());
  cell26.setCellStyle(numberStyle);
} else {
  cell26.setCellValue("");
  cell26.setCellStyle(textStyle);
}



row = sheet.getRow(7);
//====================== R8 ======================

//Cell0 - R8_RECORD_NUMBER
cell0 = row.createCell(0);
if (record.getR8_RECORD_NUMBER() != null) {
cell0.setCellValue(record.getR8_RECORD_NUMBER());
cell0.setCellStyle(textStyle);
} else {
cell0.setCellValue("");
cell0.setCellStyle(textStyle);
}

//Cell1 - R8_TITLE
cell1 = row.createCell(1);
if (record.getR8_TITLE() != null) {
  cell1.setCellValue(record.getR8_TITLE());
  cell1.setCellStyle(textStyle);
} else {
  cell1.setCellValue("");
  cell1.setCellStyle(textStyle);
}

//Cell2 - R8_FIRST_NAME
cell2 = row.createCell(2);
if (record.getR8_FIRST_NAME() != null) {
  cell2.setCellValue(record.getR8_FIRST_NAME());
  cell2.setCellStyle(textStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}

//Cell3 - R8_MIDDLE_NAME
cell3 = row.createCell(3);
if (record.getR8_MIDDLE_NAME() != null) {
  cell3.setCellValue(record.getR8_MIDDLE_NAME());
  cell3.setCellStyle(textStyle);
} else {
  cell3.setCellValue("");
  cell3.setCellStyle(textStyle);
}

//Cell4 - R8_SURNAME
cell4 = row.createCell(4);
if (record.getR8_SURNAME() != null) {
  cell4.setCellValue(record.getR8_SURNAME());
  cell4.setCellStyle(textStyle);
} else {
  cell4.setCellValue("");
  cell4.setCellStyle(textStyle);
}

//Cell5 - R8_PREVIOUS_NAME
cell5 = row.createCell(5);
if (record.getR8_PREVIOUS_NAME() != null) {
  cell5.setCellValue(record.getR8_PREVIOUS_NAME());
  cell5.setCellStyle(textStyle);
} else {
  cell5.setCellValue("");
  cell5.setCellStyle(textStyle);
}

//Cell6 - R8_GENDER
cell6 = row.createCell(6);
if (record.getR8_GENDER() != null) {
  cell6.setCellValue(record.getR8_GENDER());
  cell6.setCellStyle(textStyle);
} else {
  cell6.setCellValue("");
  cell6.setCellStyle(textStyle);
}

//Cell7 - R8_IDENTIFICATION_TYPE
cell7 = row.createCell(7);
if (record.getR8_IDENTIFICATION_TYPE() != null) {
  cell7.setCellValue(record.getR8_IDENTIFICATION_TYPE());
  cell7.setCellStyle(textStyle);
} else {
  cell7.setCellValue("");
  cell7.setCellStyle(textStyle);
}

//Cell8 - R8_PASSPORT_NUMBER
cell8 = row.createCell(8);
if (record.getR8_PASSPORT_NUMBER() != null) {
  cell8.setCellValue(record.getR8_PASSPORT_NUMBER());
  cell8.setCellStyle(textStyle);
} else {
  cell8.setCellValue("");
  cell8.setCellStyle(textStyle);
}

//Cell9 - R8_DATE_OF_BIRTH
cell9 = row.createCell(9);
if (record.getR8_DATE_OF_BIRTH() != null) {
  cell9.setCellValue(record.getR8_DATE_OF_BIRTH());
  cell9.setCellStyle(dateStyle);
} else {
  cell9.setCellValue("");
  cell9.setCellStyle(textStyle);
}

//Cell10 - R8_HOME_ADDRESS
cell10 = row.createCell(10);
if (record.getR8_HOME_ADDRESS() != null) {
  cell10.setCellValue(record.getR8_HOME_ADDRESS());
  cell10.setCellStyle(textStyle);
} else {
  cell10.setCellValue("");
  cell10.setCellStyle(textStyle);
}

//Cell11 - R8_POSTAL_ADDRESS
cell11 = row.createCell(11);
if (record.getR8_POSTAL_ADDRESS() != null) {
  cell11.setCellValue(record.getR8_POSTAL_ADDRESS());
  cell11.setCellStyle(textStyle);
} else {
  cell11.setCellValue("");
  cell11.setCellStyle(textStyle);
}

//Cell12 - R8_RESIDENCE
cell12 = row.createCell(12);
if (record.getR8_RESIDENCE() != null) {
  cell12.setCellValue(record.getR8_RESIDENCE());
  cell12.setCellStyle(textStyle);
} else {
  cell12.setCellValue("");
  cell12.setCellStyle(textStyle);
}

//Cell13 - R8_EMAIL
cell13 = row.createCell(13);
if (record.getR8_EMAIL() != null) {
  cell13.setCellValue(record.getR8_EMAIL());
  cell13.setCellStyle(textStyle);
} else {
  cell13.setCellValue("");
  cell13.setCellStyle(textStyle);
}

//Cell14 - R8_LANDLINE
cell14 = row.createCell(14);
if (record.getR8_LANDLINE() != null) {
  cell14.setCellValue(record.getR8_LANDLINE());
  cell14.setCellStyle(textStyle);
} else {
  cell14.setCellValue("");
  cell14.setCellStyle(textStyle);
}

//Cell15 - R8_MOBILE_PHONE_NUMBER
cell15 = row.createCell(15);
if (record.getR8_MOBILE_PHONE_NUMBER() != null) {
  cell15.setCellValue(record.getR8_MOBILE_PHONE_NUMBER());
  cell15.setCellStyle(textStyle);
} else {
  cell15.setCellValue("");
  cell15.setCellStyle(textStyle);
}

//Cell16 - R8_MOBILE_MONEY_NUMBER
cell16 = row.createCell(16);
if (record.getR8_MOBILE_MONEY_NUMBER() != null) {
  cell16.setCellValue(record.getR8_MOBILE_MONEY_NUMBER());
  cell16.setCellStyle(textStyle);
} else {
  cell16.setCellValue("");
  cell16.setCellStyle(textStyle);
}

//Cell17 - R8_PRODUCT_TYPE
cell17 = row.createCell(17);
if (record.getR8_PRODUCT_TYPE() != null) {
  cell17.setCellValue(record.getR8_PRODUCT_TYPE());
  cell17.setCellStyle(textStyle);
} else {
  cell17.setCellValue("");
  cell17.setCellStyle(textStyle);
}

//Cell18 - R8_ACCOUNT_BY_OWNERSHIP
cell18 = row.createCell(18);
if (record.getR8_ACCOUNT_BY_OWNERSHIP() != null) {
  cell18.setCellValue(record.getR8_ACCOUNT_BY_OWNERSHIP());
  cell18.setCellStyle(textStyle);
} else {
  cell18.setCellValue("");
  cell18.setCellStyle(textStyle);
}

//Cell19 - R8_ACCOUNT_NUMBER
cell19 = row.createCell(19);
if (record.getR8_ACCOUNT_NUMBER() != null) {
  cell19.setCellValue(record.getR8_ACCOUNT_NUMBER());
  cell19.setCellStyle(textStyle);
} else {
  cell19.setCellValue("");
  cell19.setCellStyle(textStyle);
}

//Cell20 - R8_ACCOUNT_HOLDER_INDICATOR
cell20 = row.createCell(20);
if (record.getR8_ACCOUNT_HOLDER_INDICATOR() != null) {
  cell20.setCellValue(record.getR8_ACCOUNT_HOLDER_INDICATOR().doubleValue());
  cell20.setCellStyle(numberStyle);
} else {
  cell20.setCellValue("");
  cell20.setCellStyle(textStyle);
}

//Cell21 - R8_STATUS_OF_ACCOUNT
cell21 = row.createCell(21);
if (record.getR8_STATUS_OF_ACCOUNT() != null) {
  cell21.setCellValue(record.getR8_STATUS_OF_ACCOUNT());
  cell21.setCellStyle(textStyle);
} else {
  cell21.setCellValue("");
  cell21.setCellStyle(textStyle);
}

//Cell22 - R8_NOT_FIT_FOR_STP
cell22 = row.createCell(22);
if (record.getR8_NOT_FIT_FOR_STP() != null) {
  cell22.setCellValue(record.getR8_NOT_FIT_FOR_STP());
  cell22.setCellStyle(textStyle);
} else {
  cell22.setCellValue("");
  cell22.setCellStyle(textStyle);
}

//Cell23 - R8_BRANCH_CODE_AND_NAME
cell23 = row.createCell(23);
if (record.getR8_BRANCH_CODE_AND_NAME() != null) {
  cell23.setCellValue(record.getR8_BRANCH_CODE_AND_NAME());
  cell23.setCellStyle(textStyle);
} else {
  cell23.setCellValue("");
  cell23.setCellStyle(textStyle);
}

//Cell24 - R8_ACCOUNT_BALANCE_IN_PULA
cell24 = row.createCell(24);
if (record.getR8_ACCOUNT_BALANCE_IN_PULA() != null) {
  cell24.setCellValue(record.getR8_ACCOUNT_BALANCE_IN_PULA().doubleValue());
  cell24.setCellStyle(numberStyle);
} else {
  cell24.setCellValue("");
  cell24.setCellStyle(textStyle);
}

//Cell25 - R8_CURRENCY_OF_ACCOUNT
cell25 = row.createCell(25);
if (record.getR8_CURRENCY_OF_ACCOUNT() != null) {
  cell25.setCellValue(record.getR8_CURRENCY_OF_ACCOUNT());
  cell25.setCellStyle(textStyle);
} else {
  cell25.setCellValue("");
  cell25.setCellStyle(textStyle);
}

//Cell26 - R8_EXCHANGE_RATE
cell26 = row.createCell(26);
if (record.getR8_EXCHANGE_RATE() != null) {
  cell26.setCellValue(record.getR8_EXCHANGE_RATE().doubleValue());
  cell26.setCellStyle(numberStyle);
} else {
  cell26.setCellValue("");
  cell26.setCellStyle(textStyle);
}


//====================== R9 ======================
row = sheet.getRow(8);
//Cell0 - R9_RECORD_NUMBER
cell0 = row.createCell(0);
if (record.getR9_RECORD_NUMBER() != null) {
cell0.setCellValue(record.getR9_RECORD_NUMBER());
cell0.setCellStyle(textStyle);
} else {
cell0.setCellValue("");
cell0.setCellStyle(textStyle);
}

//Cell1 - R9_TITLE
cell1 = row.createCell(1);
if (record.getR9_TITLE() != null) {
  cell1.setCellValue(record.getR9_TITLE());
  cell1.setCellStyle(textStyle);
} else {
  cell1.setCellValue("");
  cell1.setCellStyle(textStyle);
}

//Cell2 - R9_FIRST_NAME
cell2 = row.createCell(2);
if (record.getR9_FIRST_NAME() != null) {
  cell2.setCellValue(record.getR9_FIRST_NAME());
  cell2.setCellStyle(textStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}

//Cell3 - R9_MIDDLE_NAME
cell3 = row.createCell(3);
if (record.getR9_MIDDLE_NAME() != null) {
  cell3.setCellValue(record.getR9_MIDDLE_NAME());
  cell3.setCellStyle(textStyle);
} else {
  cell3.setCellValue("");
  cell3.setCellStyle(textStyle);
}

//Cell4 - R9_SURNAME
cell4 = row.createCell(4);
if (record.getR9_SURNAME() != null) {
  cell4.setCellValue(record.getR9_SURNAME());
  cell4.setCellStyle(textStyle);
} else {
  cell4.setCellValue("");
  cell4.setCellStyle(textStyle);
}

//Cell5 - R9_PREVIOUS_NAME
cell5 = row.createCell(5);
if (record.getR9_PREVIOUS_NAME() != null) {
  cell5.setCellValue(record.getR9_PREVIOUS_NAME());
  cell5.setCellStyle(textStyle);
} else {
  cell5.setCellValue("");
  cell5.setCellStyle(textStyle);
}

//Cell6 - R9_GENDER
cell6 = row.createCell(6);
if (record.getR9_GENDER() != null) {
  cell6.setCellValue(record.getR9_GENDER());
  cell6.setCellStyle(textStyle);
} else {
  cell6.setCellValue("");
  cell6.setCellStyle(textStyle);
}

//Cell7 - R9_IDENTIFICATION_TYPE
cell7 = row.createCell(7);
if (record.getR9_IDENTIFICATION_TYPE() != null) {
  cell7.setCellValue(record.getR9_IDENTIFICATION_TYPE());
  cell7.setCellStyle(textStyle);
} else {
  cell7.setCellValue("");
  cell7.setCellStyle(textStyle);
}

//Cell8 - R9_PASSPORT_NUMBER
cell8 = row.createCell(8);
if (record.getR9_PASSPORT_NUMBER() != null) {
  cell8.setCellValue(record.getR9_PASSPORT_NUMBER());
  cell8.setCellStyle(textStyle);
} else {
  cell8.setCellValue("");
  cell8.setCellStyle(textStyle);
}

//Cell9 - R9_DATE_OF_BIRTH
cell9 = row.createCell(9);
if (record.getR9_DATE_OF_BIRTH() != null) {
  cell9.setCellValue(record.getR9_DATE_OF_BIRTH());
  cell9.setCellStyle(dateStyle);
} else {
  cell9.setCellValue("");
  cell9.setCellStyle(textStyle);
}

//Cell10 - R9_HOME_ADDRESS
cell10 = row.createCell(10);
if (record.getR9_HOME_ADDRESS() != null) {
  cell10.setCellValue(record.getR9_HOME_ADDRESS());
  cell10.setCellStyle(textStyle);
} else {
  cell10.setCellValue("");
  cell10.setCellStyle(textStyle);
}

//Cell11 - R9_POSTAL_ADDRESS
cell11 = row.createCell(11);
if (record.getR9_POSTAL_ADDRESS() != null) {
  cell11.setCellValue(record.getR9_POSTAL_ADDRESS());
  cell11.setCellStyle(textStyle);
} else {
  cell11.setCellValue("");
  cell11.setCellStyle(textStyle);
}

//Cell12 - R9_RESIDENCE
cell12 = row.createCell(12);
if (record.getR9_RESIDENCE() != null) {
  cell12.setCellValue(record.getR9_RESIDENCE());
  cell12.setCellStyle(textStyle);
} else {
  cell12.setCellValue("");
  cell12.setCellStyle(textStyle);
}

//Cell13 - R9_EMAIL
cell13 = row.createCell(13);
if (record.getR9_EMAIL() != null) {
  cell13.setCellValue(record.getR9_EMAIL());
  cell13.setCellStyle(textStyle);
} else {
  cell13.setCellValue("");
  cell13.setCellStyle(textStyle);
}

//Cell14 - R9_LANDLINE
cell14 = row.createCell(14);
if (record.getR9_LANDLINE() != null) {
  cell14.setCellValue(record.getR9_LANDLINE());
  cell14.setCellStyle(textStyle);
} else {
  cell14.setCellValue("");
  cell14.setCellStyle(textStyle);
}

//Cell15 - R9_MOBILE_PHONE_NUMBER
cell15 = row.createCell(15);
if (record.getR9_MOBILE_PHONE_NUMBER() != null) {
  cell15.setCellValue(record.getR9_MOBILE_PHONE_NUMBER());
  cell15.setCellStyle(textStyle);
} else {
  cell15.setCellValue("");
  cell15.setCellStyle(textStyle);
}

//Cell16 - R9_MOBILE_MONEY_NUMBER
cell16 = row.createCell(16);
if (record.getR9_MOBILE_MONEY_NUMBER() != null) {
  cell16.setCellValue(record.getR9_MOBILE_MONEY_NUMBER());
  cell16.setCellStyle(textStyle);
} else {
  cell16.setCellValue("");
  cell16.setCellStyle(textStyle);
}

//Cell17 - R9_PRODUCT_TYPE
cell17 = row.createCell(17);
if (record.getR9_PRODUCT_TYPE() != null) {
  cell17.setCellValue(record.getR9_PRODUCT_TYPE());
  cell17.setCellStyle(textStyle);
} else {
  cell17.setCellValue("");
  cell17.setCellStyle(textStyle);
}

//Cell18 - R9_ACCOUNT_BY_OWNERSHIP
cell18 = row.createCell(18);
if (record.getR9_ACCOUNT_BY_OWNERSHIP() != null) {
  cell18.setCellValue(record.getR9_ACCOUNT_BY_OWNERSHIP());
  cell18.setCellStyle(textStyle);
} else {
  cell18.setCellValue("");
  cell18.setCellStyle(textStyle);
}

//Cell19 - R9_ACCOUNT_NUMBER
cell19 = row.createCell(19);
if (record.getR9_ACCOUNT_NUMBER() != null) {
  cell19.setCellValue(record.getR9_ACCOUNT_NUMBER());
  cell19.setCellStyle(textStyle);
} else {
  cell19.setCellValue("");
  cell19.setCellStyle(textStyle);
}

//Cell20 - R9_ACCOUNT_HOLDER_INDICATOR
cell20 = row.createCell(20);
if (record.getR9_ACCOUNT_HOLDER_INDICATOR() != null) {
  cell20.setCellValue(record.getR9_ACCOUNT_HOLDER_INDICATOR().doubleValue());
  cell20.setCellStyle(numberStyle);
} else {
  cell20.setCellValue("");
  cell20.setCellStyle(textStyle);
}

//Cell21 - R9_STATUS_OF_ACCOUNT
cell21 = row.createCell(21);
if (record.getR9_STATUS_OF_ACCOUNT() != null) {
  cell21.setCellValue(record.getR9_STATUS_OF_ACCOUNT());
  cell21.setCellStyle(textStyle);
} else {
  cell21.setCellValue("");
  cell21.setCellStyle(textStyle);
}

//Cell22 - R9_NOT_FIT_FOR_STP
cell22 = row.createCell(22);
if (record.getR9_NOT_FIT_FOR_STP() != null) {
  cell22.setCellValue(record.getR9_NOT_FIT_FOR_STP());
  cell22.setCellStyle(textStyle);
} else {
  cell22.setCellValue("");
  cell22.setCellStyle(textStyle);
}

//Cell23 - R9_BRANCH_CODE_AND_NAME
cell23 = row.createCell(23);
if (record.getR9_BRANCH_CODE_AND_NAME() != null) {
  cell23.setCellValue(record.getR9_BRANCH_CODE_AND_NAME());
  cell23.setCellStyle(textStyle);
} else {
  cell23.setCellValue("");
  cell23.setCellStyle(textStyle);
}

//Cell24 - R9_ACCOUNT_BALANCE_IN_PULA
cell24 = row.createCell(24);
if (record.getR9_ACCOUNT_BALANCE_IN_PULA() != null) {
  cell24.setCellValue(record.getR9_ACCOUNT_BALANCE_IN_PULA().doubleValue());
  cell24.setCellStyle(numberStyle);
} else {
  cell24.setCellValue("");
  cell24.setCellStyle(textStyle);
}

//Cell25 - R9_CURRENCY_OF_ACCOUNT
cell25 = row.createCell(25);
if (record.getR9_CURRENCY_OF_ACCOUNT() != null) {
  cell25.setCellValue(record.getR9_CURRENCY_OF_ACCOUNT());
  cell25.setCellStyle(textStyle);
} else {
  cell25.setCellValue("");
  cell25.setCellStyle(textStyle);
}

//Cell26 - R9_EXCHANGE_RATE
cell26 = row.createCell(26);
if (record.getR9_EXCHANGE_RATE() != null) {
  cell26.setCellValue(record.getR9_EXCHANGE_RATE().doubleValue());
  cell26.setCellStyle(numberStyle);
} else {
  cell26.setCellValue("");
  cell26.setCellStyle(textStyle);
}



row = sheet.getRow(9);
//====================== R10 ======================

//Cell0 - R10_RECORD_NUMBER
cell0 = row.createCell(0);
if (record.getR10_RECORD_NUMBER() != null) {
cell0.setCellValue(record.getR10_RECORD_NUMBER());
cell0.setCellStyle(textStyle);
} else {
cell0.setCellValue("");
cell0.setCellStyle(textStyle);
}

//Cell1 - R10_TITLE
cell1 = row.createCell(1);
if (record.getR10_TITLE() != null) {
  cell1.setCellValue(record.getR10_TITLE());
  cell1.setCellStyle(textStyle);
} else {
  cell1.setCellValue("");
  cell1.setCellStyle(textStyle);
}

//Cell2 - R10_FIRST_NAME
cell2 = row.createCell(2);
if (record.getR10_FIRST_NAME() != null) {
  cell2.setCellValue(record.getR10_FIRST_NAME());
  cell2.setCellStyle(textStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}

//Cell3 - R10_MIDDLE_NAME
cell3 = row.createCell(3);
if (record.getR10_MIDDLE_NAME() != null) {
  cell3.setCellValue(record.getR10_MIDDLE_NAME());
  cell3.setCellStyle(textStyle);
} else {
  cell3.setCellValue("");
  cell3.setCellStyle(textStyle);
}

//Cell4 - R10_SURNAME
cell4 = row.createCell(4);
if (record.getR10_SURNAME() != null) {
  cell4.setCellValue(record.getR10_SURNAME());
  cell4.setCellStyle(textStyle);
} else {
  cell4.setCellValue("");
  cell4.setCellStyle(textStyle);
}

//Cell5 - R10_PREVIOUS_NAME
cell5 = row.createCell(5);
if (record.getR10_PREVIOUS_NAME() != null) {
  cell5.setCellValue(record.getR10_PREVIOUS_NAME());
  cell5.setCellStyle(textStyle);
} else {
  cell5.setCellValue("");
  cell5.setCellStyle(textStyle);
}

//Cell6 - R10_GENDER
cell6 = row.createCell(6);
if (record.getR10_GENDER() != null) {
  cell6.setCellValue(record.getR10_GENDER());
  cell6.setCellStyle(textStyle);
} else {
  cell6.setCellValue("");
  cell6.setCellStyle(textStyle);
}

//Cell7 - R10_IDENTIFICATION_TYPE
cell7 = row.createCell(7);
if (record.getR10_IDENTIFICATION_TYPE() != null) {
  cell7.setCellValue(record.getR10_IDENTIFICATION_TYPE());
  cell7.setCellStyle(textStyle);
} else {
  cell7.setCellValue("");
  cell7.setCellStyle(textStyle);
}

//Cell8 - R10_PASSPORT_NUMBER
cell8 = row.createCell(8);
if (record.getR10_PASSPORT_NUMBER() != null) {
  cell8.setCellValue(record.getR10_PASSPORT_NUMBER());
  cell8.setCellStyle(textStyle);
} else {
  cell8.setCellValue("");
  cell8.setCellStyle(textStyle);
}

//Cell9 - R10_DATE_OF_BIRTH
cell9 = row.createCell(9);
if (record.getR10_DATE_OF_BIRTH() != null) {
  cell9.setCellValue(record.getR10_DATE_OF_BIRTH());
  cell9.setCellStyle(dateStyle);
} else {
  cell9.setCellValue("");
  cell9.setCellStyle(textStyle);
}

//Cell10 - R10_HOME_ADDRESS
cell10 = row.createCell(10);
if (record.getR10_HOME_ADDRESS() != null) {
  cell10.setCellValue(record.getR10_HOME_ADDRESS());
  cell10.setCellStyle(textStyle);
} else {
  cell10.setCellValue("");
  cell10.setCellStyle(textStyle);
}

//Cell11 - R10_POSTAL_ADDRESS
cell11 = row.createCell(11);
if (record.getR10_POSTAL_ADDRESS() != null) {
  cell11.setCellValue(record.getR10_POSTAL_ADDRESS());
  cell11.setCellStyle(textStyle);
} else {
  cell11.setCellValue("");
  cell11.setCellStyle(textStyle);
}

//Cell12 - R10_RESIDENCE
cell12 = row.createCell(12);
if (record.getR10_RESIDENCE() != null) {
  cell12.setCellValue(record.getR10_RESIDENCE());
  cell12.setCellStyle(textStyle);
} else {
  cell12.setCellValue("");
  cell12.setCellStyle(textStyle);
}

//Cell13 - R10_EMAIL
cell13 = row.createCell(13);
if (record.getR10_EMAIL() != null) {
  cell13.setCellValue(record.getR10_EMAIL());
  cell13.setCellStyle(textStyle);
} else {
  cell13.setCellValue("");
  cell13.setCellStyle(textStyle);
}

//Cell14 - R10_LANDLINE
cell14 = row.createCell(14);
if (record.getR10_LANDLINE() != null) {
  cell14.setCellValue(record.getR10_LANDLINE());
  cell14.setCellStyle(textStyle);
} else {
  cell14.setCellValue("");
  cell14.setCellStyle(textStyle);
}

//Cell15 - R10_MOBILE_PHONE_NUMBER
cell15 = row.createCell(15);
if (record.getR10_MOBILE_PHONE_NUMBER() != null) {
  cell15.setCellValue(record.getR10_MOBILE_PHONE_NUMBER());
  cell15.setCellStyle(textStyle);
} else {
  cell15.setCellValue("");
  cell15.setCellStyle(textStyle);
}

//Cell16 - R10_MOBILE_MONEY_NUMBER
cell16 = row.createCell(16);
if (record.getR10_MOBILE_MONEY_NUMBER() != null) {
  cell16.setCellValue(record.getR10_MOBILE_MONEY_NUMBER());
  cell16.setCellStyle(textStyle);
} else {
  cell16.setCellValue("");
  cell16.setCellStyle(textStyle);
}

//Cell17 - R10_PRODUCT_TYPE
cell17 = row.createCell(17);
if (record.getR10_PRODUCT_TYPE() != null) {
  cell17.setCellValue(record.getR10_PRODUCT_TYPE());
  cell17.setCellStyle(textStyle);
} else {
  cell17.setCellValue("");
  cell17.setCellStyle(textStyle);
}

//Cell18 - R10_ACCOUNT_BY_OWNERSHIP
cell18 = row.createCell(18);
if (record.getR10_ACCOUNT_BY_OWNERSHIP() != null) {
  cell18.setCellValue(record.getR10_ACCOUNT_BY_OWNERSHIP());
  cell18.setCellStyle(textStyle);
} else {
  cell18.setCellValue("");
  cell18.setCellStyle(textStyle);
}

//Cell19 - R10_ACCOUNT_NUMBER
cell19 = row.createCell(19);
if (record.getR10_ACCOUNT_NUMBER() != null) {
  cell19.setCellValue(record.getR10_ACCOUNT_NUMBER());
  cell19.setCellStyle(textStyle);
} else {
  cell19.setCellValue("");
  cell19.setCellStyle(textStyle);
}

//Cell20 - R10_ACCOUNT_HOLDER_INDICATOR
cell20 = row.createCell(20);
if (record.getR10_ACCOUNT_HOLDER_INDICATOR() != null) {
  cell20.setCellValue(record.getR10_ACCOUNT_HOLDER_INDICATOR().doubleValue());
  cell20.setCellStyle(numberStyle);
} else {
  cell20.setCellValue("");
  cell20.setCellStyle(textStyle);
}

//Cell21 - R10_STATUS_OF_ACCOUNT
cell21 = row.createCell(21);
if (record.getR10_STATUS_OF_ACCOUNT() != null) {
  cell21.setCellValue(record.getR10_STATUS_OF_ACCOUNT());
  cell21.setCellStyle(textStyle);
} else {
  cell21.setCellValue("");
  cell21.setCellStyle(textStyle);
}

//Cell22 - R10_NOT_FIT_FOR_STP
cell22 = row.createCell(22);
if (record.getR10_NOT_FIT_FOR_STP() != null) {
  cell22.setCellValue(record.getR10_NOT_FIT_FOR_STP());
  cell22.setCellStyle(textStyle);
} else {
  cell22.setCellValue("");
  cell22.setCellStyle(textStyle);
}

//Cell23 - R10_BRANCH_CODE_AND_NAME
cell23 = row.createCell(23);
if (record.getR10_BRANCH_CODE_AND_NAME() != null) {
  cell23.setCellValue(record.getR10_BRANCH_CODE_AND_NAME());
  cell23.setCellStyle(textStyle);
} else {
  cell23.setCellValue("");
  cell23.setCellStyle(textStyle);
}

//Cell24 - R10_ACCOUNT_BALANCE_IN_PULA
cell24 = row.createCell(24);
if (record.getR10_ACCOUNT_BALANCE_IN_PULA() != null) {
  cell24.setCellValue(record.getR10_ACCOUNT_BALANCE_IN_PULA().doubleValue());
  cell24.setCellStyle(numberStyle);
} else {
  cell24.setCellValue("");
  cell24.setCellStyle(textStyle);
}

//Cell25 - R10_CURRENCY_OF_ACCOUNT
cell25 = row.createCell(25);
if (record.getR10_CURRENCY_OF_ACCOUNT() != null) {
  cell25.setCellValue(record.getR10_CURRENCY_OF_ACCOUNT());
  cell25.setCellStyle(textStyle);
} else {
  cell25.setCellValue("");
  cell25.setCellStyle(textStyle);
}

//Cell26 - R10_EXCHANGE_RATE
cell26 = row.createCell(26);
if (record.getR10_EXCHANGE_RATE() != null) {
  cell26.setCellValue(record.getR10_EXCHANGE_RATE().doubleValue());
  cell26.setCellStyle(numberStyle);
} else {
  cell26.setCellValue("");
  cell26.setCellStyle(textStyle);
}

//====================== R11 ======================
row = sheet.getRow(10);
//Cell0 - R11_RECORD_NUMBER
cell0 = row.createCell(0);
if (record.getR11_RECORD_NUMBER() != null) {
cell0.setCellValue(record.getR11_RECORD_NUMBER());
cell0.setCellStyle(textStyle);
} else {
cell0.setCellValue("");
cell0.setCellStyle(textStyle);
}

//Cell1 - R11_TITLE
cell1 = row.createCell(1);
if (record.getR11_TITLE() != null) {
  cell1.setCellValue(record.getR11_TITLE());
  cell1.setCellStyle(textStyle);
} else {
  cell1.setCellValue("");
  cell1.setCellStyle(textStyle);
}

//Cell2 - R11_FIRST_NAME
cell2 = row.createCell(2);
if (record.getR11_FIRST_NAME() != null) {
  cell2.setCellValue(record.getR11_FIRST_NAME());
  cell2.setCellStyle(textStyle);
} else {
  cell2.setCellValue("");
  cell2.setCellStyle(textStyle);
}

//Cell3 - R11_MIDDLE_NAME
cell3 = row.createCell(3);
if (record.getR11_MIDDLE_NAME() != null) {
  cell3.setCellValue(record.getR11_MIDDLE_NAME());
  cell3.setCellStyle(textStyle);
} else {
  cell3.setCellValue("");
  cell3.setCellStyle(textStyle);
}

//Cell4 - R11_SURNAME
cell4 = row.createCell(4);
if (record.getR11_SURNAME() != null) {
  cell4.setCellValue(record.getR11_SURNAME());
  cell4.setCellStyle(textStyle);
} else {
  cell4.setCellValue("");
  cell4.setCellStyle(textStyle);
}

//Cell5 - R11_PREVIOUS_NAME
cell5 = row.createCell(5);
if (record.getR11_PREVIOUS_NAME() != null) {
  cell5.setCellValue(record.getR11_PREVIOUS_NAME());
  cell5.setCellStyle(textStyle);
} else {
  cell5.setCellValue("");
  cell5.setCellStyle(textStyle);
}

//Cell6 - R11_GENDER
cell6 = row.createCell(6);
if (record.getR11_GENDER() != null) {
  cell6.setCellValue(record.getR11_GENDER());
  cell6.setCellStyle(textStyle);
} else {
  cell6.setCellValue("");
  cell6.setCellStyle(textStyle);
}

//Cell7 - R11_IDENTIFICATION_TYPE
cell7 = row.createCell(7);
if (record.getR11_IDENTIFICATION_TYPE() != null) {
  cell7.setCellValue(record.getR11_IDENTIFICATION_TYPE());
  cell7.setCellStyle(textStyle);
} else {
  cell7.setCellValue("");
  cell7.setCellStyle(textStyle);
}

//Cell8 - R11_PASSPORT_NUMBER
cell8 = row.createCell(8);
if (record.getR11_PASSPORT_NUMBER() != null) {
  cell8.setCellValue(record.getR11_PASSPORT_NUMBER());
  cell8.setCellStyle(textStyle);
} else {
  cell8.setCellValue("");
  cell8.setCellStyle(textStyle);
}

//Cell9 - R11_DATE_OF_BIRTH
cell9 = row.createCell(9);
if (record.getR11_DATE_OF_BIRTH() != null) {
  cell9.setCellValue(record.getR11_DATE_OF_BIRTH());
  cell9.setCellStyle(dateStyle);
} else {
  cell9.setCellValue("");
  cell9.setCellStyle(textStyle);
}

//Cell10 - R11_HOME_ADDRESS
cell10 = row.createCell(10);
if (record.getR11_HOME_ADDRESS() != null) {
  cell10.setCellValue(record.getR11_HOME_ADDRESS());
  cell10.setCellStyle(textStyle);
} else {
  cell10.setCellValue("");
  cell10.setCellStyle(textStyle);
}

//Cell11 - R11_POSTAL_ADDRESS
cell11 = row.createCell(11);
if (record.getR11_POSTAL_ADDRESS() != null) {
  cell11.setCellValue(record.getR11_POSTAL_ADDRESS());
  cell11.setCellStyle(textStyle);
} else {
  cell11.setCellValue("");
  cell11.setCellStyle(textStyle);
}

//Cell12 - R11_RESIDENCE
cell12 = row.createCell(12);
if (record.getR11_RESIDENCE() != null) {
  cell12.setCellValue(record.getR11_RESIDENCE());
  cell12.setCellStyle(textStyle);
} else {
  cell12.setCellValue("");
  cell12.setCellStyle(textStyle);
}

//Cell13 - R11_EMAIL
cell13 = row.createCell(13);
if (record.getR11_EMAIL() != null) {
  cell13.setCellValue(record.getR11_EMAIL());
  cell13.setCellStyle(textStyle);
} else {
  cell13.setCellValue("");
  cell13.setCellStyle(textStyle);
}

//Cell14 - R11_LANDLINE
cell14 = row.createCell(14);
if (record.getR11_LANDLINE() != null) {
  cell14.setCellValue(record.getR11_LANDLINE());
  cell14.setCellStyle(textStyle);
} else {
  cell14.setCellValue("");
  cell14.setCellStyle(textStyle);
}

//Cell15 - R11_MOBILE_PHONE_NUMBER
cell15 = row.createCell(15);
if (record.getR11_MOBILE_PHONE_NUMBER() != null) {
  cell15.setCellValue(record.getR11_MOBILE_PHONE_NUMBER());
  cell15.setCellStyle(textStyle);
} else {
  cell15.setCellValue("");
  cell15.setCellStyle(textStyle);
}

//Cell16 - R11_MOBILE_MONEY_NUMBER
cell16 = row.createCell(16);
if (record.getR11_MOBILE_MONEY_NUMBER() != null) {
  cell16.setCellValue(record.getR11_MOBILE_MONEY_NUMBER());
  cell16.setCellStyle(textStyle);
} else {
  cell16.setCellValue("");
  cell16.setCellStyle(textStyle);
}

//Cell17 - R11_PRODUCT_TYPE
cell17 = row.createCell(17);
if (record.getR11_PRODUCT_TYPE() != null) {
  cell17.setCellValue(record.getR11_PRODUCT_TYPE());
  cell17.setCellStyle(textStyle);
} else {
  cell17.setCellValue("");
  cell17.setCellStyle(textStyle);
}

//Cell18 - R11_ACCOUNT_BY_OWNERSHIP
cell18 = row.createCell(18);
if (record.getR11_ACCOUNT_BY_OWNERSHIP() != null) {
  cell18.setCellValue(record.getR11_ACCOUNT_BY_OWNERSHIP());
  cell18.setCellStyle(textStyle);
} else {
  cell18.setCellValue("");
  cell18.setCellStyle(textStyle);
}

//Cell19 - R11_ACCOUNT_NUMBER
cell19 = row.createCell(19);
if (record.getR11_ACCOUNT_NUMBER() != null) {
  cell19.setCellValue(record.getR11_ACCOUNT_NUMBER());
  cell19.setCellStyle(textStyle);
} else {
  cell19.setCellValue("");
  cell19.setCellStyle(textStyle);
}

//Cell20 - R11_ACCOUNT_HOLDER_INDICATOR

cell20 = row.createCell(20);
if (record.getR11_ACCOUNT_HOLDER_INDICATOR() != null) {
  cell20.setCellValue(record.getR11_ACCOUNT_HOLDER_INDICATOR().doubleValue());
  cell20.setCellStyle(numberStyle);
} else {
  cell20.setCellValue("");
  cell20.setCellStyle(textStyle);
}

//Cell21 - R11_STATUS_OF_ACCOUNT
cell21 = row.createCell(21);
if (record.getR11_STATUS_OF_ACCOUNT() != null) {
  cell21.setCellValue(record.getR11_STATUS_OF_ACCOUNT());
  cell21.setCellStyle(textStyle);
} else {
  cell21.setCellValue("");
  cell21.setCellStyle(textStyle);
}

//Cell22 - R11_NOT_FIT_FOR_STP
cell22 = row.createCell(22);
if (record.getR11_NOT_FIT_FOR_STP() != null) {
  cell22.setCellValue(record.getR11_NOT_FIT_FOR_STP());
  cell22.setCellStyle(textStyle);
} else {
  cell22.setCellValue("");
  cell22.setCellStyle(textStyle);
}

//Cell23 - R11_BRANCH_CODE_AND_NAME
cell23 = row.createCell(23);
if (record.getR11_BRANCH_CODE_AND_NAME() != null) {
  cell23.setCellValue(record.getR11_BRANCH_CODE_AND_NAME());
  cell23.setCellStyle(textStyle);
} else {
  cell23.setCellValue("");
  cell23.setCellStyle(textStyle);
}

//Cell24 - R11_ACCOUNT_BALANCE_IN_PULA
cell24 = row.createCell(24);
if (record.getR11_ACCOUNT_BALANCE_IN_PULA() != null) {
  cell24.setCellValue(record.getR11_ACCOUNT_BALANCE_IN_PULA().doubleValue());
  cell24.setCellStyle(numberStyle);
} else {
  cell24.setCellValue("");
  cell24.setCellStyle(textStyle);
}

//Cell25 - R11_CURRENCY_OF_ACCOUNT
cell25 = row.createCell(25);
if (record.getR11_CURRENCY_OF_ACCOUNT() != null) {
  cell25.setCellValue(record.getR11_CURRENCY_OF_ACCOUNT());
  cell25.setCellStyle(textStyle);
} else {
  cell25.setCellValue("");
  cell25.setCellStyle(textStyle);
}

//Cell26 - R11_EXCHANGE_RATE
cell26 = row.createCell(26);
if (record.getR11_EXCHANGE_RATE() != null) {
  cell26.setCellValue(record.getR11_EXCHANGE_RATE().doubleValue());
  cell26.setCellStyle(numberStyle);
} else {
  cell26.setCellValue("");
  cell26.setCellStyle(textStyle);
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

	
	public byte[] getExcelBDISB1ARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<BDISB1_Archival_Summary_Entity> dataList1 = BDISB1_Archival_Summary_Repo
					.getdatabydateListarchival(dateformat.parse(todate), version);
		

			
		
		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_BDISB1 report. Returning empty result.");
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

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {

					BDISB1_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					//Cell1 - R5_TITLE
					Cell cell0 = row.createCell(0);
					if (record1.getR5_RECORD_NUMBER() != null) {
					cell0.setCellValue(record1.getR5_RECORD_NUMBER());
					cell0.setCellStyle(textStyle);
					} else {
					cell0.setCellValue("");
					cell0.setCellStyle(textStyle);
					}

					//Cell1 - R5_TITLE
					Cell cell1 = row.createCell(1);
					if (record1.getR5_TITLE() != null) {
					 cell1.setCellValue(record1.getR5_TITLE());
					 cell1.setCellStyle(textStyle);
					} else {
					 cell1.setCellValue("");
					 cell1.setCellStyle(textStyle);
					}

					//Cell2 - R5_FIRST_NAME
					Cell cell2 = row.createCell(2);
					if (record1.getR5_FIRST_NAME() != null) {
					 cell2.setCellValue(record1.getR5_FIRST_NAME());
					 cell2.setCellStyle(textStyle);
					} else {
					 cell2.setCellValue("");
					 cell2.setCellStyle(textStyle);
					}

					//Cell3 - R5_MIDDLE_NAME
					Cell cell3 = row.createCell(3);
					if (record1.getR5_MIDDLE_NAME() != null) {
					 cell3.setCellValue(record1.getR5_MIDDLE_NAME());
					 cell3.setCellStyle(textStyle);
					} else {
					 cell3.setCellValue("");
					 cell3.setCellStyle(textStyle);
					}

					//Cell4 - R5_SURNAME
					Cell cell4 = row.createCell(4);
					if (record1.getR5_SURNAME() != null) {
					 cell4.setCellValue(record1.getR5_SURNAME());
					 cell4.setCellStyle(textStyle);
					} else {
					 cell4.setCellValue("");
					 cell4.setCellStyle(textStyle);
					}

					//Cell5 - R5_PREVIOUS_NAME
					Cell cell5 = row.createCell(5);
					if (record1.getR5_PREVIOUS_NAME() != null) {
					 cell5.setCellValue(record1.getR5_PREVIOUS_NAME());
					 cell5.setCellStyle(textStyle);
					} else {
					 cell5.setCellValue("");
					 cell5.setCellStyle(textStyle);
					}

					//Cell6 - R5_GENDER
					Cell cell6 = row.createCell(6);
					if (record1.getR5_GENDER() != null) {
					 cell6.setCellValue(record1.getR5_GENDER());
					 cell6.setCellStyle(textStyle);
					} else {
					 cell6.setCellValue("");
					 cell6.setCellStyle(textStyle);
					}

					//Cell7 - R5_IDENTIFICATION_TYPE
					Cell cell7 = row.createCell(7);
					if (record1.getR5_IDENTIFICATION_TYPE() != null) {
					 cell7.setCellValue(record1.getR5_IDENTIFICATION_TYPE());
					 cell7.setCellStyle(textStyle);
					} else {
					 cell7.setCellValue("");
					 cell7.setCellStyle(textStyle);
					}

					//Cell8 - R5_PASSPORT_NUMBER
					Cell cell8 = row.createCell(8);
					if (record1.getR5_PASSPORT_NUMBER() != null) {
					 cell8.setCellValue(record1.getR5_PASSPORT_NUMBER());
					 cell8.setCellStyle(textStyle);
					} else {
					 cell8.setCellValue("");
					 cell8.setCellStyle(textStyle);
					}

					//Cell9 - R5_DATE_OF_BIRTH
					Cell cell9 = row.createCell(9);
					if (record1.getR5_DATE_OF_BIRTH() != null) {
					 cell9.setCellValue(record1.getR5_DATE_OF_BIRTH());
					 cell9.setCellStyle(dateStyle);
					} else {
					 cell9.setCellValue("");
					 cell9.setCellStyle(textStyle);
					}

					//Cell10 - R5_HOME_ADDRESS
					Cell cell10 = row.createCell(10);
					if (record1.getR5_HOME_ADDRESS() != null) {
					 cell10.setCellValue(record1.getR5_HOME_ADDRESS());
					 cell10.setCellStyle(textStyle);
					} else {
					 cell10.setCellValue("");
					 cell10.setCellStyle(textStyle);
					}

					//Cell11 - R5_POSTAL_ADDRESS
					Cell cell11 = row.createCell(11);
					if (record1.getR5_POSTAL_ADDRESS() != null) {
					 cell11.setCellValue(record1.getR5_POSTAL_ADDRESS());
					 cell11.setCellStyle(textStyle);
					} else {
					 cell11.setCellValue("");
					 cell11.setCellStyle(textStyle);
					}

					//Cell12 - R5_RESIDENCE
					Cell cell12 = row.createCell(12);
					if (record1.getR5_RESIDENCE() != null) {
					 cell12.setCellValue(record1.getR5_RESIDENCE());
					 cell12.setCellStyle(textStyle);
					} else {
					 cell12.setCellValue("");
					 cell12.setCellStyle(textStyle);
					}

					//Cell13 - R5_EMAIL
					Cell cell13 = row.createCell(13);
					if (record1.getR5_EMAIL() != null) {
					 cell13.setCellValue(record1.getR5_EMAIL());
					 cell13.setCellStyle(textStyle);
					} else {
					 cell13.setCellValue("");
					 cell13.setCellStyle(textStyle);
					}

					//Cell14 - R5_LANDLINE
					Cell cell14 = row.createCell(14);
					if (record1.getR5_LANDLINE() != null) {
					 cell14.setCellValue(record1.getR5_LANDLINE());
					 cell14.setCellStyle(textStyle);
					} else {
					 cell14.setCellValue("");
					 cell14.setCellStyle(textStyle);
					}

					//Cell15 - R5_MOBILE_PHONE_NUMBER
					Cell cell15 = row.createCell(15);
					if (record1.getR5_MOBILE_PHONE_NUMBER() != null) {
					 cell15.setCellValue(record1.getR5_MOBILE_PHONE_NUMBER());
					 cell15.setCellStyle(textStyle);
					} else {
					 cell15.setCellValue("");
					 cell15.setCellStyle(textStyle);
					}

					//Cell16 - R5_MOBILE_MONEY_NUMBER
					Cell cell16 = row.createCell(16);
					if (record1.getR5_MOBILE_MONEY_NUMBER() != null) {
					 cell16.setCellValue(record1.getR5_MOBILE_MONEY_NUMBER());
					 cell16.setCellStyle(textStyle);
					} else {
					 cell16.setCellValue("");
					 cell16.setCellStyle(textStyle);
					}

					//Cell17 - R5_PRODUCT_TYPE
					Cell cell17 = row.createCell(17);
					if (record1.getR5_PRODUCT_TYPE() != null) {
					 cell17.setCellValue(record1.getR5_PRODUCT_TYPE());
					 cell17.setCellStyle(textStyle);
					} else {
					 cell17.setCellValue("");
					 cell17.setCellStyle(textStyle);
					}

					//Cell18 - R5_ACCOUNT_BY_OWNERSHIP
					Cell cell18 = row.createCell(18);
					if (record1.getR5_ACCOUNT_BY_OWNERSHIP() != null) {
					 cell18.setCellValue(record1.getR5_ACCOUNT_BY_OWNERSHIP());
					 cell18.setCellStyle(textStyle);
					} else {
					 cell18.setCellValue("");
					 cell18.setCellStyle(textStyle);
					}

					//Cell19 - R5_ACCOUNT_NUMBER
					Cell cell19 = row.createCell(19);
					if (record1.getR5_ACCOUNT_NUMBER() != null) {
					 cell19.setCellValue(record1.getR5_ACCOUNT_NUMBER());
					 cell19.setCellStyle(textStyle);
					} else {
					 cell19.setCellValue("");
					 cell19.setCellStyle(textStyle);
					}

					//Cell20 - R5_ACCOUNT_HOLDER_INDICATOR
					Cell cell20 = row.createCell(20);
					if (record1.getR5_ACCOUNT_HOLDER_INDICATOR() != null) {
						  cell20.setCellValue(record1.getR5_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						  cell20.setCellStyle(numberStyle);
						} else {
						  cell20.setCellValue("");
						  cell20.setCellStyle(textStyle);
						}

					//Cell21 - R5_STATUS_OF_ACCOUNT
					Cell cell21 = row.createCell(21);
					if (record1.getR5_STATUS_OF_ACCOUNT() != null) {
					 cell21.setCellValue(record1.getR5_STATUS_OF_ACCOUNT());
					 cell21.setCellStyle(textStyle);
					} else {
					 cell21.setCellValue("");
					 cell21.setCellStyle(textStyle);
					}

					//Cell22 - R5_NOT_FIT_FOR_STP
					Cell cell22 = row.createCell(22);
					if (record1.getR5_NOT_FIT_FOR_STP() != null) {
					 cell22.setCellValue(record1.getR5_NOT_FIT_FOR_STP());
					 cell22.setCellStyle(textStyle);
					} else {
					 cell22.setCellValue("");
					 cell22.setCellStyle(textStyle);
					}

					//Cell23 - R5_BRANCH_CODE_AND_NAME
					Cell cell23 = row.createCell(23);
					if (record1.getR5_BRANCH_CODE_AND_NAME() != null) {
					 cell23.setCellValue(record1.getR5_BRANCH_CODE_AND_NAME());
					 cell23.setCellStyle(textStyle);
					} else {
					 cell23.setCellValue("");
					 cell23.setCellStyle(textStyle);
					}

					//Cell24 - R5_ACCOUNT_BALANCE_IN_PULA
					Cell cell24 = row.createCell(24);
					if (record1.getR5_ACCOUNT_BALANCE_IN_PULA() != null) {
					 cell24.setCellValue(record1.getR5_ACCOUNT_BALANCE_IN_PULA().doubleValue());
					 cell24.setCellStyle(numberStyle);
					} else {
					 cell24.setCellValue("");
					 cell24.setCellStyle(textStyle);
					}

					//Cell25 - R5_CURRENCY_OF_ACCOUNT
					Cell cell25 = row.createCell(25);
					if (record1.getR5_CURRENCY_OF_ACCOUNT() != null) {
					 cell25.setCellValue(record1.getR5_CURRENCY_OF_ACCOUNT());
					 cell25.setCellStyle(textStyle);
					} else {
					 cell25.setCellValue("");
					 cell25.setCellStyle(textStyle);
					}

					//Cell26 - R5_EXCHANGE_RATE
					Cell cell26 = row.createCell(26);
					if (record1.getR5_EXCHANGE_RATE() != null) {
					 cell26.setCellValue(record1.getR5_EXCHANGE_RATE().doubleValue());
					 cell26.setCellStyle(numberStyle);
					} else {
					 cell26.setCellValue("");
					 cell26.setCellStyle(textStyle);
					}


					row = sheet.getRow(5);
					//====================== R6 ======================

					//Cell1 - R5_TITLE
					cell0 = row.createCell(0);
					if (record1.getR6_RECORD_NUMBER() != null) {
					cell0.setCellValue(record1.getR6_RECORD_NUMBER());
					cell0.setCellStyle(textStyle);
					} else {
					cell0.setCellValue("");
					cell0.setCellStyle(textStyle);
					}

					//Cell1 - R6_TITLE
					cell1 = row.createCell(1);
					if (record1.getR6_TITLE() != null) {
					  cell1.setCellValue(record1.getR6_TITLE());
					  cell1.setCellStyle(textStyle);
					} else {
					  cell1.setCellValue("");
					  cell1.setCellStyle(textStyle);
					}

					//Cell2 - R6_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR6_FIRST_NAME() != null) {
					  cell2.setCellValue(record1.getR6_FIRST_NAME());
					  cell2.setCellStyle(textStyle);
					} else {
					  cell2.setCellValue("");
					  cell2.setCellStyle(textStyle);
					}

					//Cell3 - R6_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR6_MIDDLE_NAME() != null) {
					  cell3.setCellValue(record1.getR6_MIDDLE_NAME());
					  cell3.setCellStyle(textStyle);
					} else {
					  cell3.setCellValue("");
					  cell3.setCellStyle(textStyle);
					}

					//Cell4 - R6_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR6_SURNAME() != null) {
					  cell4.setCellValue(record1.getR6_SURNAME());
					  cell4.setCellStyle(textStyle);
					} else {
					  cell4.setCellValue("");
					  cell4.setCellStyle(textStyle);
					}

					//Cell5 - R6_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR6_PREVIOUS_NAME() != null) {
					  cell5.setCellValue(record1.getR6_PREVIOUS_NAME());
					  cell5.setCellStyle(textStyle);
					} else {
					  cell5.setCellValue("");
					  cell5.setCellStyle(textStyle);
					}

					//Cell6 - R6_GENDER
					cell6 = row.createCell(6);
					if (record1.getR6_GENDER() != null) {
					  cell6.setCellValue(record1.getR6_GENDER());
					  cell6.setCellStyle(textStyle);
					} else {
					  cell6.setCellValue("");
					  cell6.setCellStyle(textStyle);
					}

					//Cell7 - R6_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR6_IDENTIFICATION_TYPE() != null) {
					  cell7.setCellValue(record1.getR6_IDENTIFICATION_TYPE());
					  cell7.setCellStyle(textStyle);
					} else {
					  cell7.setCellValue("");
					  cell7.setCellStyle(textStyle);
					}

					//Cell8 - R6_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR6_PASSPORT_NUMBER() != null) {
					  cell8.setCellValue(record1.getR6_PASSPORT_NUMBER());
					  cell8.setCellStyle(textStyle);
					} else {
					  cell8.setCellValue("");
					  cell8.setCellStyle(textStyle);
					}

					//Cell9 - R6_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR6_DATE_OF_BIRTH() != null) {
					  cell9.setCellValue(record1.getR6_DATE_OF_BIRTH());
					  cell9.setCellStyle(dateStyle);
					} else {
					  cell9.setCellValue("");
					  cell9.setCellStyle(textStyle);
					}

					//Cell10 - R6_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR6_HOME_ADDRESS() != null) {
					  cell10.setCellValue(record1.getR6_HOME_ADDRESS());
					  cell10.setCellStyle(textStyle);
					} else {
					  cell10.setCellValue("");
					  cell10.setCellStyle(textStyle);
					}

					//Cell11 - R6_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR6_POSTAL_ADDRESS() != null) {
					  cell11.setCellValue(record1.getR6_POSTAL_ADDRESS());
					  cell11.setCellStyle(textStyle);
					} else {
					  cell11.setCellValue("");
					  cell11.setCellStyle(textStyle);
					}

					//Cell12 - R6_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR6_RESIDENCE() != null) {
					  cell12.setCellValue(record1.getR6_RESIDENCE());
					  cell12.setCellStyle(textStyle);
					} else {
					  cell12.setCellValue("");
					  cell12.setCellStyle(textStyle);
					}

					//Cell13 - R6_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR6_EMAIL() != null) {
					  cell13.setCellValue(record1.getR6_EMAIL());
					  cell13.setCellStyle(textStyle);
					} else {
					  cell13.setCellValue("");
					  cell13.setCellStyle(textStyle);
					}

					//Cell14 - R6_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR6_LANDLINE() != null) {
					  cell14.setCellValue(record1.getR6_LANDLINE());
					  cell14.setCellStyle(textStyle);
					} else {
					  cell14.setCellValue("");
					  cell14.setCellStyle(textStyle);
					}

					//Cell15 - R6_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR6_MOBILE_PHONE_NUMBER() != null) {
					  cell15.setCellValue(record1.getR6_MOBILE_PHONE_NUMBER());
					  cell15.setCellStyle(textStyle);
					} else {
					  cell15.setCellValue("");
					  cell15.setCellStyle(textStyle);
					}

					//Cell16 - R6_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR6_MOBILE_MONEY_NUMBER() != null) {
					  cell16.setCellValue(record1.getR6_MOBILE_MONEY_NUMBER());
					  cell16.setCellStyle(textStyle);
					} else {
					  cell16.setCellValue("");
					  cell16.setCellStyle(textStyle);
					}

					//Cell17 - R6_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR6_PRODUCT_TYPE() != null) {
					  cell17.setCellValue(record1.getR6_PRODUCT_TYPE());
					  cell17.setCellStyle(textStyle);
					} else {
					  cell17.setCellValue("");
					  cell17.setCellStyle(textStyle);
					}

					//Cell18 - R6_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR6_ACCOUNT_BY_OWNERSHIP() != null) {
					  cell18.setCellValue(record1.getR6_ACCOUNT_BY_OWNERSHIP());
					  cell18.setCellStyle(textStyle);
					} else {
					  cell18.setCellValue("");
					  cell18.setCellStyle(textStyle);
					}

					//Cell19 - R6_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR6_ACCOUNT_NUMBER() != null) {
					  cell19.setCellValue(record1.getR6_ACCOUNT_NUMBER());
					  cell19.setCellStyle(textStyle);
					} else {
					  cell19.setCellValue("");
					  cell19.setCellStyle(textStyle);
					}

					//Cell20 - R6_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR6_ACCOUNT_HOLDER_INDICATOR() != null) {
					  cell20.setCellValue(record1.getR6_ACCOUNT_HOLDER_INDICATOR().doubleValue());
					  cell20.setCellStyle(numberStyle);
					} else {
					  cell20.setCellValue("");
					  cell20.setCellStyle(textStyle);
					}

					//Cell21 - R6_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR6_STATUS_OF_ACCOUNT() != null) {
					  cell21.setCellValue(record1.getR6_STATUS_OF_ACCOUNT());
					  cell21.setCellStyle(textStyle);
					} else {
					  cell21.setCellValue("");
					  cell21.setCellStyle(textStyle);
					}

					//Cell22 - R6_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR6_NOT_FIT_FOR_STP() != null) {
					  cell22.setCellValue(record1.getR6_NOT_FIT_FOR_STP());
					  cell22.setCellStyle(textStyle);
					} else {
					  cell22.setCellValue("");
					  cell22.setCellStyle(textStyle);
					}

					//Cell23 - R6_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR6_BRANCH_CODE_AND_NAME() != null) {
					  cell23.setCellValue(record1.getR6_BRANCH_CODE_AND_NAME());
					  cell23.setCellStyle(textStyle);
					} else {
					  cell23.setCellValue("");
					  cell23.setCellStyle(textStyle);
					}

					//Cell24 - R6_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR6_ACCOUNT_BALANCE_IN_PULA() != null) {
					  cell24.setCellValue(record1.getR6_ACCOUNT_BALANCE_IN_PULA().doubleValue());
					  cell24.setCellStyle(numberStyle);
					} else {
					  cell24.setCellValue("");
					  cell24.setCellStyle(textStyle);
					}

					//Cell25 - R6_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR6_CURRENCY_OF_ACCOUNT() != null) {
					  cell25.setCellValue(record1.getR6_CURRENCY_OF_ACCOUNT());
					  cell25.setCellStyle(textStyle);
					} else {
					  cell25.setCellValue("");
					  cell25.setCellStyle(textStyle);
					}

					//Cell26 - R6_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR6_EXCHANGE_RATE() != null) {
					  cell26.setCellValue(record1.getR6_EXCHANGE_RATE().doubleValue());
					  cell26.setCellStyle(numberStyle);
					} else {
					  cell26.setCellValue("");
					  cell26.setCellStyle(textStyle);
					}



					row = sheet.getRow(6);
					//====================== R7 ======================

					//Cell0 - R7_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR7_RECORD_NUMBER() != null) {
					cell0.setCellValue(record1.getR7_RECORD_NUMBER());
					cell0.setCellStyle(textStyle);
					} else {
					cell0.setCellValue("");
					cell0.setCellStyle(textStyle);
					}

					//Cell1 - R7_TITLE
					cell1 = row.createCell(1);
					if (record1.getR7_TITLE() != null) {
					  cell1.setCellValue(record1.getR7_TITLE());
					  cell1.setCellStyle(textStyle);
					} else {
					  cell1.setCellValue("");
					  cell1.setCellStyle(textStyle);
					}

					//Cell2 - R7_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR7_FIRST_NAME() != null) {
					  cell2.setCellValue(record1.getR7_FIRST_NAME());
					  cell2.setCellStyle(textStyle);
					} else {
					  cell2.setCellValue("");
					  cell2.setCellStyle(textStyle);
					}

					//Cell3 - R7_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR7_MIDDLE_NAME() != null) {
					  cell3.setCellValue(record1.getR7_MIDDLE_NAME());
					  cell3.setCellStyle(textStyle);
					} else {
					  cell3.setCellValue("");
					  cell3.setCellStyle(textStyle);
					}

					//Cell4 - R7_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR7_SURNAME() != null) {
					  cell4.setCellValue(record1.getR7_SURNAME());
					  cell4.setCellStyle(textStyle);
					} else {
					  cell4.setCellValue("");
					  cell4.setCellStyle(textStyle);
					}

					//Cell5 - R7_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR7_PREVIOUS_NAME() != null) {
					  cell5.setCellValue(record1.getR7_PREVIOUS_NAME());
					  cell5.setCellStyle(textStyle);
					} else {
					  cell5.setCellValue("");
					  cell5.setCellStyle(textStyle);
					}

					//Cell6 - R7_GENDER
					cell6 = row.createCell(6);
					if (record1.getR7_GENDER() != null) {
					  cell6.setCellValue(record1.getR7_GENDER());
					  cell6.setCellStyle(textStyle);
					} else {
					  cell6.setCellValue("");
					  cell6.setCellStyle(textStyle);
					}

					//Cell7 - R7_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR7_IDENTIFICATION_TYPE() != null) {
					  cell7.setCellValue(record1.getR7_IDENTIFICATION_TYPE());
					  cell7.setCellStyle(textStyle);
					} else {
					  cell7.setCellValue("");
					  cell7.setCellStyle(textStyle);
					}

					//Cell8 - R7_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR7_PASSPORT_NUMBER() != null) {
					  cell8.setCellValue(record1.getR7_PASSPORT_NUMBER());
					  cell8.setCellStyle(textStyle);
					} else {
					  cell8.setCellValue("");
					  cell8.setCellStyle(textStyle);
					}

					//Cell9 - R7_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR7_DATE_OF_BIRTH() != null) {
					  cell9.setCellValue(record1.getR7_DATE_OF_BIRTH());
					  cell9.setCellStyle(dateStyle);
					} else {
					  cell9.setCellValue("");
					  cell9.setCellStyle(textStyle);
					}

					//Cell10 - R7_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR7_HOME_ADDRESS() != null) {
					  cell10.setCellValue(record1.getR7_HOME_ADDRESS());
					  cell10.setCellStyle(textStyle);
					} else {
					  cell10.setCellValue("");
					  cell10.setCellStyle(textStyle);
					}

					//Cell11 - R7_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR7_POSTAL_ADDRESS() != null) {
					  cell11.setCellValue(record1.getR7_POSTAL_ADDRESS());
					  cell11.setCellStyle(textStyle);
					} else {
					  cell11.setCellValue("");
					  cell11.setCellStyle(textStyle);
					}

					//Cell12 - R7_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR7_RESIDENCE() != null) {
					  cell12.setCellValue(record1.getR7_RESIDENCE());
					  cell12.setCellStyle(textStyle);
					} else {
					  cell12.setCellValue("");
					  cell12.setCellStyle(textStyle);
					}

					//Cell13 - R7_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR7_EMAIL() != null) {
					  cell13.setCellValue(record1.getR7_EMAIL());
					  cell13.setCellStyle(textStyle);
					} else {
					  cell13.setCellValue("");
					  cell13.setCellStyle(textStyle);
					}

					//Cell14 - R7_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR7_LANDLINE() != null) {
					  cell14.setCellValue(record1.getR7_LANDLINE());
					  cell14.setCellStyle(textStyle);
					} else {
					  cell14.setCellValue("");
					  cell14.setCellStyle(textStyle);
					}

					//Cell15 - R7_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR7_MOBILE_PHONE_NUMBER() != null) {
					  cell15.setCellValue(record1.getR7_MOBILE_PHONE_NUMBER());
					  cell15.setCellStyle(textStyle);
					} else {
					  cell15.setCellValue("");
					  cell15.setCellStyle(textStyle);
					}

					//Cell16 - R7_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR7_MOBILE_MONEY_NUMBER() != null) {
					  cell16.setCellValue(record1.getR7_MOBILE_MONEY_NUMBER());
					  cell16.setCellStyle(textStyle);
					} else {
					  cell16.setCellValue("");
					  cell16.setCellStyle(textStyle);
					}

					//Cell17 - R7_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR7_PRODUCT_TYPE() != null) {
					  cell17.setCellValue(record1.getR7_PRODUCT_TYPE());
					  cell17.setCellStyle(textStyle);
					} else {
					  cell17.setCellValue("");
					  cell17.setCellStyle(textStyle);
					}

					//Cell18 - R7_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR7_ACCOUNT_BY_OWNERSHIP() != null) {
					  cell18.setCellValue(record1.getR7_ACCOUNT_BY_OWNERSHIP());
					  cell18.setCellStyle(textStyle);
					} else {
					  cell18.setCellValue("");
					  cell18.setCellStyle(textStyle);
					}

					//Cell19 - R7_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR7_ACCOUNT_NUMBER() != null) {
					  cell19.setCellValue(record1.getR7_ACCOUNT_NUMBER());
					  cell19.setCellStyle(textStyle);
					} else {
					  cell19.setCellValue("");
					  cell19.setCellStyle(textStyle);
					}

					//Cell20 - R7_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR11_ACCOUNT_HOLDER_INDICATOR() != null) {
					  cell20.setCellValue(record1.getR11_ACCOUNT_HOLDER_INDICATOR().doubleValue());
					  cell20.setCellStyle(numberStyle);
					} else {
					  cell20.setCellValue("");
					  cell20.setCellStyle(textStyle);
					}

					//Cell21 - R7_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR7_STATUS_OF_ACCOUNT() != null) {
					  cell21.setCellValue(record1.getR7_STATUS_OF_ACCOUNT());
					  cell21.setCellStyle(textStyle);
					} else {
					  cell21.setCellValue("");
					  cell21.setCellStyle(textStyle);
					}

					//Cell22 - R7_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR7_NOT_FIT_FOR_STP() != null) {
					  cell22.setCellValue(record1.getR7_NOT_FIT_FOR_STP());
					  cell22.setCellStyle(textStyle);
					} else {
					  cell22.setCellValue("");
					  cell22.setCellStyle(textStyle);
					}

					//Cell23 - R7_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR7_BRANCH_CODE_AND_NAME() != null) {
					  cell23.setCellValue(record1.getR7_BRANCH_CODE_AND_NAME());
					  cell23.setCellStyle(textStyle);
					} else {
					  cell23.setCellValue("");
					  cell23.setCellStyle(textStyle);
					}

					//Cell24 - R7_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR7_ACCOUNT_BALANCE_IN_PULA() != null) {
					  cell24.setCellValue(record1.getR7_ACCOUNT_BALANCE_IN_PULA().doubleValue());
					  cell24.setCellStyle(numberStyle);
					} else {
					  cell24.setCellValue("");
					  cell24.setCellStyle(textStyle);
					}

					//Cell25 - R7_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR7_CURRENCY_OF_ACCOUNT() != null) {
					  cell25.setCellValue(record1.getR7_CURRENCY_OF_ACCOUNT());
					  cell25.setCellStyle(textStyle);
					} else {
					  cell25.setCellValue("");
					  cell25.setCellStyle(textStyle);
					}

					//Cell26 - R7_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR7_EXCHANGE_RATE() != null) {
					  cell26.setCellValue(record1.getR7_EXCHANGE_RATE().doubleValue());
					  cell26.setCellStyle(numberStyle);
					} else {
					  cell26.setCellValue("");
					  cell26.setCellStyle(textStyle);
					}



					row = sheet.getRow(7);
					//====================== R8 ======================

					//Cell0 - R8_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR8_RECORD_NUMBER() != null) {
					cell0.setCellValue(record1.getR8_RECORD_NUMBER());
					cell0.setCellStyle(textStyle);
					} else {
					cell0.setCellValue("");
					cell0.setCellStyle(textStyle);
					}

					//Cell1 - R8_TITLE
					cell1 = row.createCell(1);
					if (record1.getR8_TITLE() != null) {
					  cell1.setCellValue(record1.getR8_TITLE());
					  cell1.setCellStyle(textStyle);
					} else {
					  cell1.setCellValue("");
					  cell1.setCellStyle(textStyle);
					}

					//Cell2 - R8_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR8_FIRST_NAME() != null) {
					  cell2.setCellValue(record1.getR8_FIRST_NAME());
					  cell2.setCellStyle(textStyle);
					} else {
					  cell2.setCellValue("");
					  cell2.setCellStyle(textStyle);
					}

					//Cell3 - R8_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR8_MIDDLE_NAME() != null) {
					  cell3.setCellValue(record1.getR8_MIDDLE_NAME());
					  cell3.setCellStyle(textStyle);
					} else {
					  cell3.setCellValue("");
					  cell3.setCellStyle(textStyle);
					}

					//Cell4 - R8_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR8_SURNAME() != null) {
					  cell4.setCellValue(record1.getR8_SURNAME());
					  cell4.setCellStyle(textStyle);
					} else {
					  cell4.setCellValue("");
					  cell4.setCellStyle(textStyle);
					}

					//Cell5 - R8_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR8_PREVIOUS_NAME() != null) {
					  cell5.setCellValue(record1.getR8_PREVIOUS_NAME());
					  cell5.setCellStyle(textStyle);
					} else {
					  cell5.setCellValue("");
					  cell5.setCellStyle(textStyle);
					}

					//Cell6 - R8_GENDER
					cell6 = row.createCell(6);
					if (record1.getR8_GENDER() != null) {
					  cell6.setCellValue(record1.getR8_GENDER());
					  cell6.setCellStyle(textStyle);
					} else {
					  cell6.setCellValue("");
					  cell6.setCellStyle(textStyle);
					}

					//Cell7 - R8_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR8_IDENTIFICATION_TYPE() != null) {
					  cell7.setCellValue(record1.getR8_IDENTIFICATION_TYPE());
					  cell7.setCellStyle(textStyle);
					} else {
					  cell7.setCellValue("");
					  cell7.setCellStyle(textStyle);
					}

					//Cell8 - R8_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR8_PASSPORT_NUMBER() != null) {
					  cell8.setCellValue(record1.getR8_PASSPORT_NUMBER());
					  cell8.setCellStyle(textStyle);
					} else {
					  cell8.setCellValue("");
					  cell8.setCellStyle(textStyle);
					}

					//Cell9 - R8_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR8_DATE_OF_BIRTH() != null) {
					  cell9.setCellValue(record1.getR8_DATE_OF_BIRTH());
					  cell9.setCellStyle(dateStyle);
					} else {
					  cell9.setCellValue("");
					  cell9.setCellStyle(textStyle);
					}

					//Cell10 - R8_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR8_HOME_ADDRESS() != null) {
					  cell10.setCellValue(record1.getR8_HOME_ADDRESS());
					  cell10.setCellStyle(textStyle);
					} else {
					  cell10.setCellValue("");
					  cell10.setCellStyle(textStyle);
					}

					//Cell11 - R8_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR8_POSTAL_ADDRESS() != null) {
					  cell11.setCellValue(record1.getR8_POSTAL_ADDRESS());
					  cell11.setCellStyle(textStyle);
					} else {
					  cell11.setCellValue("");
					  cell11.setCellStyle(textStyle);
					}

					//Cell12 - R8_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR8_RESIDENCE() != null) {
					  cell12.setCellValue(record1.getR8_RESIDENCE());
					  cell12.setCellStyle(textStyle);
					} else {
					  cell12.setCellValue("");
					  cell12.setCellStyle(textStyle);
					}

					//Cell13 - R8_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR8_EMAIL() != null) {
					  cell13.setCellValue(record1.getR8_EMAIL());
					  cell13.setCellStyle(textStyle);
					} else {
					  cell13.setCellValue("");
					  cell13.setCellStyle(textStyle);
					}

					//Cell14 - R8_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR8_LANDLINE() != null) {
					  cell14.setCellValue(record1.getR8_LANDLINE());
					  cell14.setCellStyle(textStyle);
					} else {
					  cell14.setCellValue("");
					  cell14.setCellStyle(textStyle);
					}

					//Cell15 - R8_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR8_MOBILE_PHONE_NUMBER() != null) {
					  cell15.setCellValue(record1.getR8_MOBILE_PHONE_NUMBER());
					  cell15.setCellStyle(textStyle);
					} else {
					  cell15.setCellValue("");
					  cell15.setCellStyle(textStyle);
					}

					//Cell16 - R8_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR8_MOBILE_MONEY_NUMBER() != null) {
					  cell16.setCellValue(record1.getR8_MOBILE_MONEY_NUMBER());
					  cell16.setCellStyle(textStyle);
					} else {
					  cell16.setCellValue("");
					  cell16.setCellStyle(textStyle);
					}

					//Cell17 - R8_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR8_PRODUCT_TYPE() != null) {
					  cell17.setCellValue(record1.getR8_PRODUCT_TYPE());
					  cell17.setCellStyle(textStyle);
					} else {
					  cell17.setCellValue("");
					  cell17.setCellStyle(textStyle);
					}

					//Cell18 - R8_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR8_ACCOUNT_BY_OWNERSHIP() != null) {
					  cell18.setCellValue(record1.getR8_ACCOUNT_BY_OWNERSHIP());
					  cell18.setCellStyle(textStyle);
					} else {
					  cell18.setCellValue("");
					  cell18.setCellStyle(textStyle);
					}

					//Cell19 - R8_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR8_ACCOUNT_NUMBER() != null) {
					  cell19.setCellValue(record1.getR8_ACCOUNT_NUMBER());
					  cell19.setCellStyle(textStyle);
					} else {
					  cell19.setCellValue("");
					  cell19.setCellStyle(textStyle);
					}

					//Cell20 - R8_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR8_ACCOUNT_HOLDER_INDICATOR() != null) {
					  cell20.setCellValue(record1.getR8_ACCOUNT_HOLDER_INDICATOR().doubleValue());
					  cell20.setCellStyle(numberStyle);
					} else {
					  cell20.setCellValue("");
					  cell20.setCellStyle(textStyle);
					}

					//Cell21 - R8_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR8_STATUS_OF_ACCOUNT() != null) {
					  cell21.setCellValue(record1.getR8_STATUS_OF_ACCOUNT());
					  cell21.setCellStyle(textStyle);
					} else {
					  cell21.setCellValue("");
					  cell21.setCellStyle(textStyle);
					}

					//Cell22 - R8_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR8_NOT_FIT_FOR_STP() != null) {
					  cell22.setCellValue(record1.getR8_NOT_FIT_FOR_STP());
					  cell22.setCellStyle(textStyle);
					} else {
					  cell22.setCellValue("");
					  cell22.setCellStyle(textStyle);
					}

					//Cell23 - R8_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR8_BRANCH_CODE_AND_NAME() != null) {
					  cell23.setCellValue(record1.getR8_BRANCH_CODE_AND_NAME());
					  cell23.setCellStyle(textStyle);
					} else {
					  cell23.setCellValue("");
					  cell23.setCellStyle(textStyle);
					}

					//Cell24 - R8_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR8_ACCOUNT_BALANCE_IN_PULA() != null) {
					  cell24.setCellValue(record1.getR8_ACCOUNT_BALANCE_IN_PULA().doubleValue());
					  cell24.setCellStyle(numberStyle);
					} else {
					  cell24.setCellValue("");
					  cell24.setCellStyle(textStyle);
					}

					//Cell25 - R8_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR8_CURRENCY_OF_ACCOUNT() != null) {
					  cell25.setCellValue(record1.getR8_CURRENCY_OF_ACCOUNT());
					  cell25.setCellStyle(textStyle);
					} else {
					  cell25.setCellValue("");
					  cell25.setCellStyle(textStyle);
					}

					//Cell26 - R8_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR8_EXCHANGE_RATE() != null) {
					  cell26.setCellValue(record1.getR8_EXCHANGE_RATE().doubleValue());
					  cell26.setCellStyle(numberStyle);
					} else {
					  cell26.setCellValue("");
					  cell26.setCellStyle(textStyle);
					}


					//====================== R9 ======================
					row = sheet.getRow(8);
					//Cell0 - R9_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR9_RECORD_NUMBER() != null) {
					cell0.setCellValue(record1.getR9_RECORD_NUMBER());
					cell0.setCellStyle(textStyle);
					} else {
					cell0.setCellValue("");
					cell0.setCellStyle(textStyle);
					}

					//Cell1 - R9_TITLE
					cell1 = row.createCell(1);
					if (record1.getR9_TITLE() != null) {
					  cell1.setCellValue(record1.getR9_TITLE());
					  cell1.setCellStyle(textStyle);
					} else {
					  cell1.setCellValue("");
					  cell1.setCellStyle(textStyle);
					}

					//Cell2 - R9_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR9_FIRST_NAME() != null) {
					  cell2.setCellValue(record1.getR9_FIRST_NAME());
					  cell2.setCellStyle(textStyle);
					} else {
					  cell2.setCellValue("");
					  cell2.setCellStyle(textStyle);
					}

					//Cell3 - R9_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR9_MIDDLE_NAME() != null) {
					  cell3.setCellValue(record1.getR9_MIDDLE_NAME());
					  cell3.setCellStyle(textStyle);
					} else {
					  cell3.setCellValue("");
					  cell3.setCellStyle(textStyle);
					}

					//Cell4 - R9_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR9_SURNAME() != null) {
					  cell4.setCellValue(record1.getR9_SURNAME());
					  cell4.setCellStyle(textStyle);
					} else {
					  cell4.setCellValue("");
					  cell4.setCellStyle(textStyle);
					}

					//Cell5 - R9_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR9_PREVIOUS_NAME() != null) {
					  cell5.setCellValue(record1.getR9_PREVIOUS_NAME());
					  cell5.setCellStyle(textStyle);
					} else {
					  cell5.setCellValue("");
					  cell5.setCellStyle(textStyle);
					}

					//Cell6 - R9_GENDER
					cell6 = row.createCell(6);
					if (record1.getR9_GENDER() != null) {
					  cell6.setCellValue(record1.getR9_GENDER());
					  cell6.setCellStyle(textStyle);
					} else {
					  cell6.setCellValue("");
					  cell6.setCellStyle(textStyle);
					}

					//Cell7 - R9_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR9_IDENTIFICATION_TYPE() != null) {
					  cell7.setCellValue(record1.getR9_IDENTIFICATION_TYPE());
					  cell7.setCellStyle(textStyle);
					} else {
					  cell7.setCellValue("");
					  cell7.setCellStyle(textStyle);
					}

					//Cell8 - R9_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR9_PASSPORT_NUMBER() != null) {
					  cell8.setCellValue(record1.getR9_PASSPORT_NUMBER());
					  cell8.setCellStyle(textStyle);
					} else {
					  cell8.setCellValue("");
					  cell8.setCellStyle(textStyle);
					}

					//Cell9 - R9_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR9_DATE_OF_BIRTH() != null) {
					  cell9.setCellValue(record1.getR9_DATE_OF_BIRTH());
					  cell9.setCellStyle(dateStyle);
					} else {
					  cell9.setCellValue("");
					  cell9.setCellStyle(textStyle);
					}

					//Cell10 - R9_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR9_HOME_ADDRESS() != null) {
					  cell10.setCellValue(record1.getR9_HOME_ADDRESS());
					  cell10.setCellStyle(textStyle);
					} else {
					  cell10.setCellValue("");
					  cell10.setCellStyle(textStyle);
					}

					//Cell11 - R9_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR9_POSTAL_ADDRESS() != null) {
					  cell11.setCellValue(record1.getR9_POSTAL_ADDRESS());
					  cell11.setCellStyle(textStyle);
					} else {
					  cell11.setCellValue("");
					  cell11.setCellStyle(textStyle);
					}

					//Cell12 - R9_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR9_RESIDENCE() != null) {
					  cell12.setCellValue(record1.getR9_RESIDENCE());
					  cell12.setCellStyle(textStyle);
					} else {
					  cell12.setCellValue("");
					  cell12.setCellStyle(textStyle);
					}

					//Cell13 - R9_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR9_EMAIL() != null) {
					  cell13.setCellValue(record1.getR9_EMAIL());
					  cell13.setCellStyle(textStyle);
					} else {
					  cell13.setCellValue("");
					  cell13.setCellStyle(textStyle);
					}

					//Cell14 - R9_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR9_LANDLINE() != null) {
					  cell14.setCellValue(record1.getR9_LANDLINE());
					  cell14.setCellStyle(textStyle);
					} else {
					  cell14.setCellValue("");
					  cell14.setCellStyle(textStyle);
					}

					//Cell15 - R9_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR9_MOBILE_PHONE_NUMBER() != null) {
					  cell15.setCellValue(record1.getR9_MOBILE_PHONE_NUMBER());
					  cell15.setCellStyle(textStyle);
					} else {
					  cell15.setCellValue("");
					  cell15.setCellStyle(textStyle);
					}

					//Cell16 - R9_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR9_MOBILE_MONEY_NUMBER() != null) {
					  cell16.setCellValue(record1.getR9_MOBILE_MONEY_NUMBER());
					  cell16.setCellStyle(textStyle);
					} else {
					  cell16.setCellValue("");
					  cell16.setCellStyle(textStyle);
					}

					//Cell17 - R9_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR9_PRODUCT_TYPE() != null) {
					  cell17.setCellValue(record1.getR9_PRODUCT_TYPE());
					  cell17.setCellStyle(textStyle);
					} else {
					  cell17.setCellValue("");
					  cell17.setCellStyle(textStyle);
					}

					//Cell18 - R9_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR9_ACCOUNT_BY_OWNERSHIP() != null) {
					  cell18.setCellValue(record1.getR9_ACCOUNT_BY_OWNERSHIP());
					  cell18.setCellStyle(textStyle);
					} else {
					  cell18.setCellValue("");
					  cell18.setCellStyle(textStyle);
					}

					//Cell19 - R9_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR9_ACCOUNT_NUMBER() != null) {
					  cell19.setCellValue(record1.getR9_ACCOUNT_NUMBER());
					  cell19.setCellStyle(textStyle);
					} else {
					  cell19.setCellValue("");
					  cell19.setCellStyle(textStyle);
					}

					//Cell20 - R9_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR9_ACCOUNT_HOLDER_INDICATOR() != null) {
					  cell20.setCellValue(record1.getR9_ACCOUNT_HOLDER_INDICATOR().doubleValue());
					  cell20.setCellStyle(numberStyle);
					} else {
					  cell20.setCellValue("");
					  cell20.setCellStyle(textStyle);
					}

					//Cell21 - R9_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR9_STATUS_OF_ACCOUNT() != null) {
					  cell21.setCellValue(record1.getR9_STATUS_OF_ACCOUNT());
					  cell21.setCellStyle(textStyle);
					} else {
					  cell21.setCellValue("");
					  cell21.setCellStyle(textStyle);
					}

					//Cell22 - R9_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR9_NOT_FIT_FOR_STP() != null) {
					  cell22.setCellValue(record1.getR9_NOT_FIT_FOR_STP());
					  cell22.setCellStyle(textStyle);
					} else {
					  cell22.setCellValue("");
					  cell22.setCellStyle(textStyle);
					}

					//Cell23 - R9_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR9_BRANCH_CODE_AND_NAME() != null) {
					  cell23.setCellValue(record1.getR9_BRANCH_CODE_AND_NAME());
					  cell23.setCellStyle(textStyle);
					} else {
					  cell23.setCellValue("");
					  cell23.setCellStyle(textStyle);
					}

					//Cell24 - R9_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR9_ACCOUNT_BALANCE_IN_PULA() != null) {
					  cell24.setCellValue(record1.getR9_ACCOUNT_BALANCE_IN_PULA().doubleValue());
					  cell24.setCellStyle(numberStyle);
					} else {
					  cell24.setCellValue("");
					  cell24.setCellStyle(textStyle);
					}

					//Cell25 - R9_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR9_CURRENCY_OF_ACCOUNT() != null) {
					  cell25.setCellValue(record1.getR9_CURRENCY_OF_ACCOUNT());
					  cell25.setCellStyle(textStyle);
					} else {
					  cell25.setCellValue("");
					  cell25.setCellStyle(textStyle);
					}

					//Cell26 - R9_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR9_EXCHANGE_RATE() != null) {
					  cell26.setCellValue(record1.getR9_EXCHANGE_RATE().doubleValue());
					  cell26.setCellStyle(numberStyle);
					} else {
					  cell26.setCellValue("");
					  cell26.setCellStyle(textStyle);
					}



					row = sheet.getRow(9);
					//====================== R10 ======================

					//Cell0 - R10_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR10_RECORD_NUMBER() != null) {
					cell0.setCellValue(record1.getR10_RECORD_NUMBER());
					cell0.setCellStyle(textStyle);
					} else {
					cell0.setCellValue("");
					cell0.setCellStyle(textStyle);
					}

					//Cell1 - R10_TITLE
					cell1 = row.createCell(1);
					if (record1.getR10_TITLE() != null) {
					  cell1.setCellValue(record1.getR10_TITLE());
					  cell1.setCellStyle(textStyle);
					} else {
					  cell1.setCellValue("");
					  cell1.setCellStyle(textStyle);
					}

					//Cell2 - R10_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR10_FIRST_NAME() != null) {
					  cell2.setCellValue(record1.getR10_FIRST_NAME());
					  cell2.setCellStyle(textStyle);
					} else {
					  cell2.setCellValue("");
					  cell2.setCellStyle(textStyle);
					}

					//Cell3 - R10_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR10_MIDDLE_NAME() != null) {
					  cell3.setCellValue(record1.getR10_MIDDLE_NAME());
					  cell3.setCellStyle(textStyle);
					} else {
					  cell3.setCellValue("");
					  cell3.setCellStyle(textStyle);
					}

					//Cell4 - R10_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR10_SURNAME() != null) {
					  cell4.setCellValue(record1.getR10_SURNAME());
					  cell4.setCellStyle(textStyle);
					} else {
					  cell4.setCellValue("");
					  cell4.setCellStyle(textStyle);
					}

					//Cell5 - R10_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR10_PREVIOUS_NAME() != null) {
					  cell5.setCellValue(record1.getR10_PREVIOUS_NAME());
					  cell5.setCellStyle(textStyle);
					} else {
					  cell5.setCellValue("");
					  cell5.setCellStyle(textStyle);
					}

					//Cell6 - R10_GENDER
					cell6 = row.createCell(6);
					if (record1.getR10_GENDER() != null) {
					  cell6.setCellValue(record1.getR10_GENDER());
					  cell6.setCellStyle(textStyle);
					} else {
					  cell6.setCellValue("");
					  cell6.setCellStyle(textStyle);
					}

					//Cell7 - R10_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR10_IDENTIFICATION_TYPE() != null) {
					  cell7.setCellValue(record1.getR10_IDENTIFICATION_TYPE());
					  cell7.setCellStyle(textStyle);
					} else {
					  cell7.setCellValue("");
					  cell7.setCellStyle(textStyle);
					}

					//Cell8 - R10_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR10_PASSPORT_NUMBER() != null) {
					  cell8.setCellValue(record1.getR10_PASSPORT_NUMBER());
					  cell8.setCellStyle(textStyle);
					} else {
					  cell8.setCellValue("");
					  cell8.setCellStyle(textStyle);
					}

					//Cell9 - R10_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR10_DATE_OF_BIRTH() != null) {
					  cell9.setCellValue(record1.getR10_DATE_OF_BIRTH());
					  cell9.setCellStyle(dateStyle);
					} else {
					  cell9.setCellValue("");
					  cell9.setCellStyle(textStyle);
					}

					//Cell10 - R10_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR10_HOME_ADDRESS() != null) {
					  cell10.setCellValue(record1.getR10_HOME_ADDRESS());
					  cell10.setCellStyle(textStyle);
					} else {
					  cell10.setCellValue("");
					  cell10.setCellStyle(textStyle);
					}

					//Cell11 - R10_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR10_POSTAL_ADDRESS() != null) {
					  cell11.setCellValue(record1.getR10_POSTAL_ADDRESS());
					  cell11.setCellStyle(textStyle);
					} else {
					  cell11.setCellValue("");
					  cell11.setCellStyle(textStyle);
					}

					//Cell12 - R10_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR10_RESIDENCE() != null) {
					  cell12.setCellValue(record1.getR10_RESIDENCE());
					  cell12.setCellStyle(textStyle);
					} else {
					  cell12.setCellValue("");
					  cell12.setCellStyle(textStyle);
					}

					//Cell13 - R10_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR10_EMAIL() != null) {
					  cell13.setCellValue(record1.getR10_EMAIL());
					  cell13.setCellStyle(textStyle);
					} else {
					  cell13.setCellValue("");
					  cell13.setCellStyle(textStyle);
					}

					//Cell14 - R10_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR10_LANDLINE() != null) {
					  cell14.setCellValue(record1.getR10_LANDLINE());
					  cell14.setCellStyle(textStyle);
					} else {
					  cell14.setCellValue("");
					  cell14.setCellStyle(textStyle);
					}

					//Cell15 - R10_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR10_MOBILE_PHONE_NUMBER() != null) {
					  cell15.setCellValue(record1.getR10_MOBILE_PHONE_NUMBER());
					  cell15.setCellStyle(textStyle);
					} else {
					  cell15.setCellValue("");
					  cell15.setCellStyle(textStyle);
					}

					//Cell16 - R10_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR10_MOBILE_MONEY_NUMBER() != null) {
					  cell16.setCellValue(record1.getR10_MOBILE_MONEY_NUMBER());
					  cell16.setCellStyle(textStyle);
					} else {
					  cell16.setCellValue("");
					  cell16.setCellStyle(textStyle);
					}

					//Cell17 - R10_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR10_PRODUCT_TYPE() != null) {
					  cell17.setCellValue(record1.getR10_PRODUCT_TYPE());
					  cell17.setCellStyle(textStyle);
					} else {
					  cell17.setCellValue("");
					  cell17.setCellStyle(textStyle);
					}

					//Cell18 - R10_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR10_ACCOUNT_BY_OWNERSHIP() != null) {
					  cell18.setCellValue(record1.getR10_ACCOUNT_BY_OWNERSHIP());
					  cell18.setCellStyle(textStyle);
					} else {
					  cell18.setCellValue("");
					  cell18.setCellStyle(textStyle);
					}

					//Cell19 - R10_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR10_ACCOUNT_NUMBER() != null) {
					  cell19.setCellValue(record1.getR10_ACCOUNT_NUMBER());
					  cell19.setCellStyle(textStyle);
					} else {
					  cell19.setCellValue("");
					  cell19.setCellStyle(textStyle);
					}

					//Cell20 - R10_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR10_ACCOUNT_HOLDER_INDICATOR() != null) {
					  cell20.setCellValue(record1.getR10_ACCOUNT_HOLDER_INDICATOR().doubleValue());
					  cell20.setCellStyle(numberStyle);
					} else {
					  cell20.setCellValue("");
					  cell20.setCellStyle(textStyle);
					}

					//Cell21 - R10_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR10_STATUS_OF_ACCOUNT() != null) {
					  cell21.setCellValue(record1.getR10_STATUS_OF_ACCOUNT());
					  cell21.setCellStyle(textStyle);
					} else {
					  cell21.setCellValue("");
					  cell21.setCellStyle(textStyle);
					}

					//Cell22 - R10_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR10_NOT_FIT_FOR_STP() != null) {
					  cell22.setCellValue(record1.getR10_NOT_FIT_FOR_STP());
					  cell22.setCellStyle(textStyle);
					} else {
					  cell22.setCellValue("");
					  cell22.setCellStyle(textStyle);
					}

					//Cell23 - R10_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR10_BRANCH_CODE_AND_NAME() != null) {
					  cell23.setCellValue(record1.getR10_BRANCH_CODE_AND_NAME());
					  cell23.setCellStyle(textStyle);
					} else {
					  cell23.setCellValue("");
					  cell23.setCellStyle(textStyle);
					}

					//Cell24 - R10_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR10_ACCOUNT_BALANCE_IN_PULA() != null) {
					  cell24.setCellValue(record1.getR10_ACCOUNT_BALANCE_IN_PULA().doubleValue());
					  cell24.setCellStyle(numberStyle);
					} else {
					  cell24.setCellValue("");
					  cell24.setCellStyle(textStyle);
					}

					//Cell25 - R10_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR10_CURRENCY_OF_ACCOUNT() != null) {
					  cell25.setCellValue(record1.getR10_CURRENCY_OF_ACCOUNT());
					  cell25.setCellStyle(textStyle);
					} else {
					  cell25.setCellValue("");
					  cell25.setCellStyle(textStyle);
					}

					//Cell26 - R10_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR10_EXCHANGE_RATE() != null) {
					  cell26.setCellValue(record1.getR10_EXCHANGE_RATE().doubleValue());
					  cell26.setCellStyle(numberStyle);
					} else {
					  cell26.setCellValue("");
					  cell26.setCellStyle(textStyle);
					}

					//====================== R11 ======================
					row = sheet.getRow(10);
					//Cell0 - R11_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR11_RECORD_NUMBER() != null) {
					cell0.setCellValue(record1.getR11_RECORD_NUMBER());
					cell0.setCellStyle(textStyle);
					} else {
					cell0.setCellValue("");
					cell0.setCellStyle(textStyle);
					}

					//Cell1 - R11_TITLE
					cell1 = row.createCell(1);
					if (record1.getR11_TITLE() != null) {
					  cell1.setCellValue(record1.getR11_TITLE());
					  cell1.setCellStyle(textStyle);
					} else {
					  cell1.setCellValue("");
					  cell1.setCellStyle(textStyle);
					}

					//Cell2 - R11_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR11_FIRST_NAME() != null) {
					  cell2.setCellValue(record1.getR11_FIRST_NAME());
					  cell2.setCellStyle(textStyle);
					} else {
					  cell2.setCellValue("");
					  cell2.setCellStyle(textStyle);
					}

					//Cell3 - R11_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR11_MIDDLE_NAME() != null) {
					  cell3.setCellValue(record1.getR11_MIDDLE_NAME());
					  cell3.setCellStyle(textStyle);
					} else {
					  cell3.setCellValue("");
					  cell3.setCellStyle(textStyle);
					}

					//Cell4 - R11_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR11_SURNAME() != null) {
					  cell4.setCellValue(record1.getR11_SURNAME());
					  cell4.setCellStyle(textStyle);
					} else {
					  cell4.setCellValue("");
					  cell4.setCellStyle(textStyle);
					}

					//Cell5 - R11_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR11_PREVIOUS_NAME() != null) {
					  cell5.setCellValue(record1.getR11_PREVIOUS_NAME());
					  cell5.setCellStyle(textStyle);
					} else {
					  cell5.setCellValue("");
					  cell5.setCellStyle(textStyle);
					}

					//Cell6 - R11_GENDER
					cell6 = row.createCell(6);
					if (record1.getR11_GENDER() != null) {
					  cell6.setCellValue(record1.getR11_GENDER());
					  cell6.setCellStyle(textStyle);
					} else {
					  cell6.setCellValue("");
					  cell6.setCellStyle(textStyle);
					}

					//Cell7 - R11_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR11_IDENTIFICATION_TYPE() != null) {
					  cell7.setCellValue(record1.getR11_IDENTIFICATION_TYPE());
					  cell7.setCellStyle(textStyle);
					} else {
					  cell7.setCellValue("");
					  cell7.setCellStyle(textStyle);
					}

					//Cell8 - R11_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR11_PASSPORT_NUMBER() != null) {
					  cell8.setCellValue(record1.getR11_PASSPORT_NUMBER());
					  cell8.setCellStyle(textStyle);
					} else {
					  cell8.setCellValue("");
					  cell8.setCellStyle(textStyle);
					}

					//Cell9 - R11_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR11_DATE_OF_BIRTH() != null) {
					  cell9.setCellValue(record1.getR11_DATE_OF_BIRTH());
					  cell9.setCellStyle(dateStyle);
					} else {
					  cell9.setCellValue("");
					  cell9.setCellStyle(textStyle);
					}

					//Cell10 - R11_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR11_HOME_ADDRESS() != null) {
					  cell10.setCellValue(record1.getR11_HOME_ADDRESS());
					  cell10.setCellStyle(textStyle);
					} else {
					  cell10.setCellValue("");
					  cell10.setCellStyle(textStyle);
					}

					//Cell11 - R11_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR11_POSTAL_ADDRESS() != null) {
					  cell11.setCellValue(record1.getR11_POSTAL_ADDRESS());
					  cell11.setCellStyle(textStyle);
					} else {
					  cell11.setCellValue("");
					  cell11.setCellStyle(textStyle);
					}

					//Cell12 - R11_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR11_RESIDENCE() != null) {
					  cell12.setCellValue(record1.getR11_RESIDENCE());
					  cell12.setCellStyle(textStyle);
					} else {
					  cell12.setCellValue("");
					  cell12.setCellStyle(textStyle);
					}

					//Cell13 - R11_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR11_EMAIL() != null) {
					  cell13.setCellValue(record1.getR11_EMAIL());
					  cell13.setCellStyle(textStyle);
					} else {
					  cell13.setCellValue("");
					  cell13.setCellStyle(textStyle);
					}

					//Cell14 - R11_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR11_LANDLINE() != null) {
					  cell14.setCellValue(record1.getR11_LANDLINE());
					  cell14.setCellStyle(textStyle);
					} else {
					  cell14.setCellValue("");
					  cell14.setCellStyle(textStyle);
					}

					//Cell15 - R11_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR11_MOBILE_PHONE_NUMBER() != null) {
					  cell15.setCellValue(record1.getR11_MOBILE_PHONE_NUMBER());
					  cell15.setCellStyle(textStyle);
					} else {
					  cell15.setCellValue("");
					  cell15.setCellStyle(textStyle);
					}

					//Cell16 - R11_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR11_MOBILE_MONEY_NUMBER() != null) {
					  cell16.setCellValue(record1.getR11_MOBILE_MONEY_NUMBER());
					  cell16.setCellStyle(textStyle);
					} else {
					  cell16.setCellValue("");
					  cell16.setCellStyle(textStyle);
					}

					//Cell17 - R11_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR11_PRODUCT_TYPE() != null) {
					  cell17.setCellValue(record1.getR11_PRODUCT_TYPE());
					  cell17.setCellStyle(textStyle);
					} else {
					  cell17.setCellValue("");
					  cell17.setCellStyle(textStyle);
					}

					//Cell18 - R11_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR11_ACCOUNT_BY_OWNERSHIP() != null) {
					  cell18.setCellValue(record1.getR11_ACCOUNT_BY_OWNERSHIP());
					  cell18.setCellStyle(textStyle);
					} else {
					  cell18.setCellValue("");
					  cell18.setCellStyle(textStyle);
					}

					//Cell19 - R11_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR11_ACCOUNT_NUMBER() != null) {
					  cell19.setCellValue(record1.getR11_ACCOUNT_NUMBER());
					  cell19.setCellStyle(textStyle);
					} else {
					  cell19.setCellValue("");
					  cell19.setCellStyle(textStyle);
					}

					//Cell20 - R11_ACCOUNT_HOLDER_INDICATOR

					cell20 = row.createCell(20);
					if (record1.getR11_ACCOUNT_HOLDER_INDICATOR() != null) {
					  cell20.setCellValue(record1.getR11_ACCOUNT_HOLDER_INDICATOR().doubleValue());
					  cell20.setCellStyle(numberStyle);
					} else {
					  cell20.setCellValue("");
					  cell20.setCellStyle(textStyle);
					}

					//Cell21 - R11_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR11_STATUS_OF_ACCOUNT() != null) {
					  cell21.setCellValue(record1.getR11_STATUS_OF_ACCOUNT());
					  cell21.setCellStyle(textStyle);
					} else {
					  cell21.setCellValue("");
					  cell21.setCellStyle(textStyle);
					}

					//Cell22 - R11_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR11_NOT_FIT_FOR_STP() != null) {
					  cell22.setCellValue(record1.getR11_NOT_FIT_FOR_STP());
					  cell22.setCellStyle(textStyle);
					} else {
					  cell22.setCellValue("");
					  cell22.setCellStyle(textStyle);
					}

					//Cell23 - R11_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR11_BRANCH_CODE_AND_NAME() != null) {
					  cell23.setCellValue(record1.getR11_BRANCH_CODE_AND_NAME());
					  cell23.setCellStyle(textStyle);
					} else {
					  cell23.setCellValue("");
					  cell23.setCellStyle(textStyle);
					}

					//Cell24 - R11_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR11_ACCOUNT_BALANCE_IN_PULA() != null) {
					  cell24.setCellValue(record1.getR11_ACCOUNT_BALANCE_IN_PULA().doubleValue());
					  cell24.setCellStyle(numberStyle);
					} else {
					  cell24.setCellValue("");
					  cell24.setCellStyle(textStyle);
					}

					//Cell25 - R11_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR11_CURRENCY_OF_ACCOUNT() != null) {
					  cell25.setCellValue(record1.getR11_CURRENCY_OF_ACCOUNT());
					  cell25.setCellStyle(textStyle);
					} else {
					  cell25.setCellValue("");
					  cell25.setCellStyle(textStyle);
					}

					//Cell26 - R11_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR11_EXCHANGE_RATE() != null) {
					  cell26.setCellValue(record1.getR11_EXCHANGE_RATE().doubleValue());
					  cell26.setCellStyle(numberStyle);
					} else {
					  cell26.setCellValue("");
					  cell26.setCellStyle(textStyle);
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
	
	
//////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////	
/// Report Date | Report Version | Domain
/// RESUB VIEW
public List<Object[]> getBDISB1Resub() {
List<Object[]> resubList = new ArrayList<>();
try {
List<BDISB1_Archival_Summary_Entity> latestArchivalList = BDISB1_Archival_Summary_Repo
.getdatabydateListWithVersionAll();

if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
for (BDISB1_Archival_Summary_Entity entity : latestArchivalList) {
Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion() };
resubList.add(row);
}
System.out.println("Fetched " + resubList.size() + " record(s)");
} else {
System.out.println("No archival data found.");
}
} catch (Exception e) {
System.err.println("Error fetching BDISB1 Resub data: " + e.getMessage());
e.printStackTrace();
}
return resubList;
}

// Archival View
public List<Object[]> getBDISB1Archival() {
List<Object[]> archivalList = new ArrayList<>();

try {
List<BDISB1_Archival_Summary_Entity> repoData = BDISB1_Archival_Summary_Repo
.getdatabydateListWithVersionAll();

if (repoData != null && !repoData.isEmpty()) {
for (BDISB1_Archival_Summary_Entity entity : repoData) {
Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion() };
archivalList.add(row);
}

System.out.println("Fetched " + archivalList.size() + " archival records");
BDISB1_Archival_Summary_Entity first = repoData.get(0);
System.out.println("Latest archival version: " + first.getReportVersion());
} else {
System.out.println("No archival data found.");
}

} catch (Exception e) {
System.err.println("Error fetching BDISB1 Archival data: " + e.getMessage());
e.printStackTrace();
}

return archivalList;
}

@Transactional
public void updateReportReSub(BDISB1_Summary_Entity updatedEntity) {

    System.out.println("Came to Resub Service");

    Date reportDate = updatedEntity.getReportDate();
    System.out.println("Report Date: " + reportDate);

    try {

        /* =========================================================
         * 1️⃣ FETCH LATEST ARCHIVAL VERSION
         * ========================================================= */
        Optional<BDISB1_Archival_Summary_Entity> latestArchivalOpt =
                BDISB1_Archival_Summary_Repo
                        .getLatestArchivalVersionByDate(reportDate);

        int newVersion = 1;
        if (latestArchivalOpt.isPresent()) {
            try {
                newVersion =
                        Integer.parseInt(latestArchivalOpt.get().getReportVersion()) + 1;
            } catch (NumberFormatException e) {
                newVersion = 1;
            }
        }

        boolean exists =
                BDISB1_Archival_Summary_Repo
                        .findByReportDateAndReportVersion(
                                reportDate, String.valueOf(newVersion))
                        .isPresent();

        if (exists) {
            throw new RuntimeException(
                    "Version " + newVersion + " already exists for report date " + reportDate);
        }

        /* =========================================================
         * 2️⃣ CREATE NEW ARCHIVAL ENTITY (BASE COPY)
         * ========================================================= */
        BDISB1_Archival_Summary_Entity archivalEntity =
                new BDISB1_Archival_Summary_Entity();

        if (latestArchivalOpt.isPresent()) {
            BeanUtils.copyProperties(latestArchivalOpt.get(), archivalEntity);
        }

        archivalEntity.setReportDate(reportDate);
        archivalEntity.setReportVersion(String.valueOf(newVersion));
        archivalEntity.setModify_flg("Y");

        /* =========================================================
         * 3️⃣ READ RAW REQUEST PARAMETERS
         * ========================================================= */
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes()).getRequest();

        Map<String, String[]> parameterMap = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

            String key = entry.getKey();        // R5_C11_FIRST_NAME
            String value = entry.getValue()[0];

            // Ignore non-data params
            if ("asondate".equalsIgnoreCase(key)
                    || "type".equalsIgnoreCase(key)) {
                continue;
            }

            // Normalize: R5_C11_FIRST_NAME → R5_FIRST_NAME
            String normalizedKey = key.replaceFirst("_C\\d+_", "_");

            /* =====================================================
             * 4️⃣ APPLY VALUES (EXPLICIT MAPPING)
             * ===================================================== */

            // ======================= R5 =======================

            if ("R5_RECORD_NUMBER".equals(normalizedKey)) {
                archivalEntity.setR5_RECORD_NUMBER(value);

            } else if ("R5_TITLE".equals(normalizedKey)) {
                archivalEntity.setR5_TITLE(value);

            } else if ("R5_FIRST_NAME".equals(normalizedKey)) {
                archivalEntity.setR5_FIRST_NAME(value);

            } else if ("R5_MIDDLE_NAME".equals(normalizedKey)) {
                archivalEntity.setR5_MIDDLE_NAME(value);

            } else if ("R5_SURNAME".equals(normalizedKey)) {
                archivalEntity.setR5_SURNAME(value);

            } else if ("R5_PREVIOUS_NAME".equals(normalizedKey)) {
                archivalEntity.setR5_PREVIOUS_NAME(value);

            } else if ("R5_GENDER".equals(normalizedKey)) {
                archivalEntity.setR5_GENDER(value);

            } else if ("R5_IDENTIFICATION_TYPE".equals(normalizedKey)) {
                archivalEntity.setR5_IDENTIFICATION_TYPE(value);

            } else if ("R5_PASSPORT_NUMBER".equals(normalizedKey)) {
                archivalEntity.setR5_PASSPORT_NUMBER(value);

            } else if ("R5_DATE_OF_BIRTH".equals(normalizedKey)) {
                archivalEntity.setR5_DATE_OF_BIRTH(Date(value));

            } else if ("R5_HOME_ADDRESS".equals(normalizedKey)) {
                archivalEntity.setR5_HOME_ADDRESS(value);

            } else if ("R5_POSTAL_ADDRESS".equals(normalizedKey)) {
                archivalEntity.setR5_POSTAL_ADDRESS(value);

            } else if ("R5_RESIDENCE".equals(normalizedKey)) {
                archivalEntity.setR5_RESIDENCE(value);

            } else if ("R5_EMAIL".equals(normalizedKey)) {
                archivalEntity.setR5_EMAIL(value);

            } else if ("R5_LANDLINE".equals(normalizedKey)) {
                archivalEntity.setR5_LANDLINE(value);

            } else if ("R5_MOBILE_PHONE_NUMBER".equals(normalizedKey)) {
                archivalEntity.setR5_MOBILE_PHONE_NUMBER(value);

            } else if ("R5_MOBILE_MONEY_NUMBER".equals(normalizedKey)) {
                archivalEntity.setR5_MOBILE_MONEY_NUMBER(value);

            } else if ("R5_PRODUCT_TYPE".equals(normalizedKey)) {
                archivalEntity.setR5_PRODUCT_TYPE(value);

            } else if ("R5_ACCOUNT_BY_OWNERSHIP".equals(normalizedKey)) {
                archivalEntity.setR5_ACCOUNT_BY_OWNERSHIP(value);

            } else if ("R5_ACCOUNT_NUMBER".equals(normalizedKey)) {
                archivalEntity.setR5_ACCOUNT_NUMBER(value);

            } else if ("R5_ACCOUNT_HOLDER_INDICATOR".equals(normalizedKey)) {
                archivalEntity.setR5_ACCOUNT_HOLDER_INDICATOR(parseBigDecimal(value));

            } else if ("R5_STATUS_OF_ACCOUNT".equals(normalizedKey)) {
                archivalEntity.setR5_STATUS_OF_ACCOUNT(value);

            } else if ("R5_NOT_FIT_FOR_STP".equals(normalizedKey)) {
                archivalEntity.setR5_NOT_FIT_FOR_STP(value);

            } else if ("R5_BRANCH_CODE_AND_NAME".equals(normalizedKey)) {
                archivalEntity.setR5_BRANCH_CODE_AND_NAME(value);

            } else if ("R5_ACCOUNT_BALANCE_IN_PULA".equals(normalizedKey)) {
                archivalEntity.setR5_ACCOUNT_BALANCE_IN_PULA(parseBigDecimal(value));

            } else if ("R5_CURRENCY_OF_ACCOUNT".equals(normalizedKey)) {
                archivalEntity.setR5_CURRENCY_OF_ACCOUNT(value);

            } else if ("R5_EXCHANGE_RATE".equals(normalizedKey)) {
                archivalEntity.setR5_EXCHANGE_RATE(parseBigDecimal(value));
            }
                
                else if ("R6_RECORD_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR6_RECORD_NUMBER(value);

                } else if ("R6_TITLE".equals(normalizedKey)) {
                    archivalEntity.setR6_TITLE(value);

                } else if ("R6_FIRST_NAME".equals(normalizedKey)) {
                    archivalEntity.setR6_FIRST_NAME(value);

                } else if ("R6_MIDDLE_NAME".equals(normalizedKey)) {
                    archivalEntity.setR6_MIDDLE_NAME(value);

                } else if ("R6_SURNAME".equals(normalizedKey)) {
                    archivalEntity.setR6_SURNAME(value);

                } else if ("R6_PREVIOUS_NAME".equals(normalizedKey)) {
                    archivalEntity.setR6_PREVIOUS_NAME(value);

                } else if ("R6_GENDER".equals(normalizedKey)) {
                    archivalEntity.setR6_GENDER(value);

                } else if ("R6_IDENTIFICATION_TYPE".equals(normalizedKey)) {
                    archivalEntity.setR6_IDENTIFICATION_TYPE(value);

                } else if ("R6_PASSPORT_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR6_PASSPORT_NUMBER(value);

                } else if ("R6_DATE_OF_BIRTH".equals(normalizedKey)) {
                    archivalEntity.setR6_DATE_OF_BIRTH(Date(value));

                } else if ("R6_HOME_ADDRESS".equals(normalizedKey)) {
                    archivalEntity.setR6_HOME_ADDRESS(value);

                } else if ("R6_POSTAL_ADDRESS".equals(normalizedKey)) {
                    archivalEntity.setR6_POSTAL_ADDRESS(value);

                } else if ("R6_RESIDENCE".equals(normalizedKey)) {
                    archivalEntity.setR6_RESIDENCE(value);

                } else if ("R6_EMAIL".equals(normalizedKey)) {
                    archivalEntity.setR6_EMAIL(value);

                } else if ("R6_LANDLINE".equals(normalizedKey)) {
                    archivalEntity.setR6_LANDLINE(value);

                } else if ("R6_MOBILE_PHONE_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR6_MOBILE_PHONE_NUMBER(value);

                } else if ("R6_MOBILE_MONEY_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR6_MOBILE_MONEY_NUMBER(value);

                } else if ("R6_PRODUCT_TYPE".equals(normalizedKey)) {
                    archivalEntity.setR6_PRODUCT_TYPE(value);

                } else if ("R6_ACCOUNT_BY_OWNERSHIP".equals(normalizedKey)) {
                    archivalEntity.setR6_ACCOUNT_BY_OWNERSHIP(value);

                } else if ("R6_ACCOUNT_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR6_ACCOUNT_NUMBER(value);

                } else if ("R6_ACCOUNT_HOLDER_INDICATOR".equals(normalizedKey)) {
                    archivalEntity.setR6_ACCOUNT_HOLDER_INDICATOR(parseBigDecimal(value));

                } else if ("R6_STATUS_OF_ACCOUNT".equals(normalizedKey)) {
                    archivalEntity.setR6_STATUS_OF_ACCOUNT(value);

                } else if ("R6_NOT_FIT_FOR_STP".equals(normalizedKey)) {
                    archivalEntity.setR6_NOT_FIT_FOR_STP(value);

                } else if ("R6_BRANCH_CODE_AND_NAME".equals(normalizedKey)) {
                    archivalEntity.setR6_BRANCH_CODE_AND_NAME(value);

                } else if ("R6_ACCOUNT_BALANCE_IN_PULA".equals(normalizedKey)) {
                    archivalEntity.setR6_ACCOUNT_BALANCE_IN_PULA(parseBigDecimal(value));

                } else if ("R6_CURRENCY_OF_ACCOUNT".equals(normalizedKey)) {
                    archivalEntity.setR6_CURRENCY_OF_ACCOUNT(value);

                } else if ("R6_EXCHANGE_RATE".equals(normalizedKey)) {
                    archivalEntity.setR6_EXCHANGE_RATE(parseBigDecimal(value));
                }
            
                else if ("R7_RECORD_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR7_RECORD_NUMBER(value);

                } else if ("R7_TITLE".equals(normalizedKey)) {
                    archivalEntity.setR7_TITLE(value);

                } else if ("R7_FIRST_NAME".equals(normalizedKey)) {
                    archivalEntity.setR7_FIRST_NAME(value);

                } else if ("R7_MIDDLE_NAME".equals(normalizedKey)) {
                    archivalEntity.setR7_MIDDLE_NAME(value);

                } else if ("R7_SURNAME".equals(normalizedKey)) {
                    archivalEntity.setR7_SURNAME(value);

                } else if ("R7_PREVIOUS_NAME".equals(normalizedKey)) {
                    archivalEntity.setR7_PREVIOUS_NAME(value);

                } else if ("R7_GENDER".equals(normalizedKey)) {
                    archivalEntity.setR7_GENDER(value);

                } else if ("R7_IDENTIFICATION_TYPE".equals(normalizedKey)) {
                    archivalEntity.setR7_IDENTIFICATION_TYPE(value);

                } else if ("R7_PASSPORT_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR7_PASSPORT_NUMBER(value);

                } else if ("R7_DATE_OF_BIRTH".equals(normalizedKey)) {
                    archivalEntity.setR7_DATE_OF_BIRTH(Date(value));

                } else if ("R7_HOME_ADDRESS".equals(normalizedKey)) {
                    archivalEntity.setR7_HOME_ADDRESS(value);

                } else if ("R7_POSTAL_ADDRESS".equals(normalizedKey)) {
                    archivalEntity.setR7_POSTAL_ADDRESS(value);

                } else if ("R7_RESIDENCE".equals(normalizedKey)) {
                    archivalEntity.setR7_RESIDENCE(value);

                } else if ("R7_EMAIL".equals(normalizedKey)) {
                    archivalEntity.setR7_EMAIL(value);

                } else if ("R7_LANDLINE".equals(normalizedKey)) {
                    archivalEntity.setR7_LANDLINE(value);

                } else if ("R7_MOBILE_PHONE_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR7_MOBILE_PHONE_NUMBER(value);

                } else if ("R7_MOBILE_MONEY_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR7_MOBILE_MONEY_NUMBER(value);

                } else if ("R7_PRODUCT_TYPE".equals(normalizedKey)) {
                    archivalEntity.setR7_PRODUCT_TYPE(value);

                } else if ("R7_ACCOUNT_BY_OWNERSHIP".equals(normalizedKey)) {
                    archivalEntity.setR7_ACCOUNT_BY_OWNERSHIP(value);

                } else if ("R7_ACCOUNT_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR7_ACCOUNT_NUMBER(value);

                } else if ("R7_ACCOUNT_HOLDER_INDICATOR".equals(normalizedKey)) {
                    archivalEntity.setR7_ACCOUNT_HOLDER_INDICATOR(parseBigDecimal(value));

                } else if ("R7_STATUS_OF_ACCOUNT".equals(normalizedKey)) {
                    archivalEntity.setR7_STATUS_OF_ACCOUNT(value);

                } else if ("R7_NOT_FIT_FOR_STP".equals(normalizedKey)) {
                    archivalEntity.setR7_NOT_FIT_FOR_STP(value);

                } else if ("R7_BRANCH_CODE_AND_NAME".equals(normalizedKey)) {
                    archivalEntity.setR7_BRANCH_CODE_AND_NAME(value);

                } else if ("R7_ACCOUNT_BALANCE_IN_PULA".equals(normalizedKey)) {
                    archivalEntity.setR7_ACCOUNT_BALANCE_IN_PULA(parseBigDecimal(value));

                } else if ("R7_CURRENCY_OF_ACCOUNT".equals(normalizedKey)) {
                    archivalEntity.setR7_CURRENCY_OF_ACCOUNT(value);

                } else if ("R7_EXCHANGE_RATE".equals(normalizedKey)) {
                    archivalEntity.setR7_EXCHANGE_RATE(parseBigDecimal(value));
                }
            
                else if ("R8_RECORD_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR8_RECORD_NUMBER(value);

                } else if ("R8_TITLE".equals(normalizedKey)) {
                    archivalEntity.setR8_TITLE(value);

                } else if ("R8_FIRST_NAME".equals(normalizedKey)) {
                    archivalEntity.setR8_FIRST_NAME(value);

                } else if ("R8_MIDDLE_NAME".equals(normalizedKey)) {
                    archivalEntity.setR8_MIDDLE_NAME(value);

                } else if ("R8_SURNAME".equals(normalizedKey)) {
                    archivalEntity.setR8_SURNAME(value);

                } else if ("R8_PREVIOUS_NAME".equals(normalizedKey)) {
                    archivalEntity.setR8_PREVIOUS_NAME(value);

                } else if ("R8_GENDER".equals(normalizedKey)) {
                    archivalEntity.setR8_GENDER(value);

                } else if ("R8_IDENTIFICATION_TYPE".equals(normalizedKey)) {
                    archivalEntity.setR8_IDENTIFICATION_TYPE(value);

                } else if ("R8_PASSPORT_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR8_PASSPORT_NUMBER(value);

                } else if ("R8_DATE_OF_BIRTH".equals(normalizedKey)) {
                    archivalEntity.setR8_DATE_OF_BIRTH(Date(value));

                } else if ("R8_HOME_ADDRESS".equals(normalizedKey)) {
                    archivalEntity.setR8_HOME_ADDRESS(value);

                } else if ("R8_POSTAL_ADDRESS".equals(normalizedKey)) {
                    archivalEntity.setR8_POSTAL_ADDRESS(value);

                } else if ("R8_RESIDENCE".equals(normalizedKey)) {
                    archivalEntity.setR8_RESIDENCE(value);

                } else if ("R8_EMAIL".equals(normalizedKey)) {
                    archivalEntity.setR8_EMAIL(value);

                } else if ("R8_LANDLINE".equals(normalizedKey)) {
                    archivalEntity.setR8_LANDLINE(value);

                } else if ("R8_MOBILE_PHONE_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR8_MOBILE_PHONE_NUMBER(value);

                } else if ("R8_MOBILE_MONEY_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR8_MOBILE_MONEY_NUMBER(value);

                } else if ("R8_PRODUCT_TYPE".equals(normalizedKey)) {
                    archivalEntity.setR8_PRODUCT_TYPE(value);

                } else if ("R8_ACCOUNT_BY_OWNERSHIP".equals(normalizedKey)) {
                    archivalEntity.setR8_ACCOUNT_BY_OWNERSHIP(value);

                } else if ("R8_ACCOUNT_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR8_ACCOUNT_NUMBER(value);

                } else if ("R8_ACCOUNT_HOLDER_INDICATOR".equals(normalizedKey)) {
                    archivalEntity.setR8_ACCOUNT_HOLDER_INDICATOR(parseBigDecimal(value));

                } else if ("R8_STATUS_OF_ACCOUNT".equals(normalizedKey)) {
                    archivalEntity.setR8_STATUS_OF_ACCOUNT(value);

                } else if ("R8_NOT_FIT_FOR_STP".equals(normalizedKey)) {
                    archivalEntity.setR8_NOT_FIT_FOR_STP(value);

                } else if ("R8_BRANCH_CODE_AND_NAME".equals(normalizedKey)) {
                    archivalEntity.setR8_BRANCH_CODE_AND_NAME(value);

                } else if ("R8_ACCOUNT_BALANCE_IN_PULA".equals(normalizedKey)) {
                    archivalEntity.setR8_ACCOUNT_BALANCE_IN_PULA(parseBigDecimal(value));

                } else if ("R8_CURRENCY_OF_ACCOUNT".equals(normalizedKey)) {
                    archivalEntity.setR8_CURRENCY_OF_ACCOUNT(value);

                } else if ("R8_EXCHANGE_RATE".equals(normalizedKey)) {
                    archivalEntity.setR8_EXCHANGE_RATE(parseBigDecimal(value));
                }
            
                else if ("R9_RECORD_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR9_RECORD_NUMBER(value);

                } else if ("R9_TITLE".equals(normalizedKey)) {
                    archivalEntity.setR9_TITLE(value);

                } else if ("R9_FIRST_NAME".equals(normalizedKey)) {
                    archivalEntity.setR9_FIRST_NAME(value);

                } else if ("R9_MIDDLE_NAME".equals(normalizedKey)) {
                    archivalEntity.setR9_MIDDLE_NAME(value);

                } else if ("R9_SURNAME".equals(normalizedKey)) {
                    archivalEntity.setR9_SURNAME(value);

                } else if ("R9_PREVIOUS_NAME".equals(normalizedKey)) {
                    archivalEntity.setR9_PREVIOUS_NAME(value);

                } else if ("R9_GENDER".equals(normalizedKey)) {
                    archivalEntity.setR9_GENDER(value);

                } else if ("R9_IDENTIFICATION_TYPE".equals(normalizedKey)) {
                    archivalEntity.setR9_IDENTIFICATION_TYPE(value);

                } else if ("R9_PASSPORT_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR9_PASSPORT_NUMBER(value);

                } else if ("R9_DATE_OF_BIRTH".equals(normalizedKey)) {
                    archivalEntity.setR9_DATE_OF_BIRTH(Date(value));

                } else if ("R9_HOME_ADDRESS".equals(normalizedKey)) {
                    archivalEntity.setR9_HOME_ADDRESS(value);

                } else if ("R9_POSTAL_ADDRESS".equals(normalizedKey)) {
                    archivalEntity.setR9_POSTAL_ADDRESS(value);

                } else if ("R9_RESIDENCE".equals(normalizedKey)) {
                    archivalEntity.setR9_RESIDENCE(value);

                } else if ("R9_EMAIL".equals(normalizedKey)) {
                    archivalEntity.setR9_EMAIL(value);

                } else if ("R9_LANDLINE".equals(normalizedKey)) {
                    archivalEntity.setR9_LANDLINE(value);

                } else if ("R9_MOBILE_PHONE_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR9_MOBILE_PHONE_NUMBER(value);

                } else if ("R9_MOBILE_MONEY_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR9_MOBILE_MONEY_NUMBER(value);

                } else if ("R9_PRODUCT_TYPE".equals(normalizedKey)) {
                    archivalEntity.setR9_PRODUCT_TYPE(value);

                } else if ("R9_ACCOUNT_BY_OWNERSHIP".equals(normalizedKey)) {
                    archivalEntity.setR9_ACCOUNT_BY_OWNERSHIP(value);

                } else if ("R9_ACCOUNT_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR9_ACCOUNT_NUMBER(value);

                } else if ("R9_ACCOUNT_HOLDER_INDICATOR".equals(normalizedKey)) {
                    archivalEntity.setR9_ACCOUNT_HOLDER_INDICATOR(parseBigDecimal(value));

                } else if ("R9_STATUS_OF_ACCOUNT".equals(normalizedKey)) {
                    archivalEntity.setR9_STATUS_OF_ACCOUNT(value);

                } else if ("R9_NOT_FIT_FOR_STP".equals(normalizedKey)) {
                    archivalEntity.setR9_NOT_FIT_FOR_STP(value);

                } else if ("R9_BRANCH_CODE_AND_NAME".equals(normalizedKey)) {
                    archivalEntity.setR9_BRANCH_CODE_AND_NAME(value);

                } else if ("R9_ACCOUNT_BALANCE_IN_PULA".equals(normalizedKey)) {
                    archivalEntity.setR9_ACCOUNT_BALANCE_IN_PULA(parseBigDecimal(value));

                } else if ("R9_CURRENCY_OF_ACCOUNT".equals(normalizedKey)) {
                    archivalEntity.setR9_CURRENCY_OF_ACCOUNT(value);

                } else if ("R9_EXCHANGE_RATE".equals(normalizedKey)) {
                    archivalEntity.setR9_EXCHANGE_RATE(parseBigDecimal(value));
                }
            
                else if ("R10_RECORD_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR10_RECORD_NUMBER(value);

                } else if ("R10_TITLE".equals(normalizedKey)) {
                    archivalEntity.setR10_TITLE(value);

                } else if ("R10_FIRST_NAME".equals(normalizedKey)) {
                    archivalEntity.setR10_FIRST_NAME(value);

                } else if ("R10_MIDDLE_NAME".equals(normalizedKey)) {
                    archivalEntity.setR10_MIDDLE_NAME(value);

                } else if ("R10_SURNAME".equals(normalizedKey)) {
                    archivalEntity.setR10_SURNAME(value);

                } else if ("R10_PREVIOUS_NAME".equals(normalizedKey)) {
                    archivalEntity.setR10_PREVIOUS_NAME(value);

                } else if ("R10_GENDER".equals(normalizedKey)) {
                    archivalEntity.setR10_GENDER(value);

                } else if ("R10_IDENTIFICATION_TYPE".equals(normalizedKey)) {
                    archivalEntity.setR10_IDENTIFICATION_TYPE(value);

                } else if ("R10_PASSPORT_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR10_PASSPORT_NUMBER(value);

                } else if ("R10_DATE_OF_BIRTH".equals(normalizedKey)) {
                    archivalEntity.setR10_DATE_OF_BIRTH(Date(value));

                } else if ("R10_HOME_ADDRESS".equals(normalizedKey)) {
                    archivalEntity.setR10_HOME_ADDRESS(value);

                } else if ("R10_POSTAL_ADDRESS".equals(normalizedKey)) {
                    archivalEntity.setR10_POSTAL_ADDRESS(value);

                } else if ("R10_RESIDENCE".equals(normalizedKey)) {
                    archivalEntity.setR10_RESIDENCE(value);

                } else if ("R10_EMAIL".equals(normalizedKey)) {
                    archivalEntity.setR10_EMAIL(value);

                } else if ("R10_LANDLINE".equals(normalizedKey)) {
                    archivalEntity.setR10_LANDLINE(value);

                } else if ("R10_MOBILE_PHONE_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR10_MOBILE_PHONE_NUMBER(value);

                } else if ("R10_MOBILE_MONEY_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR10_MOBILE_MONEY_NUMBER(value);

                } else if ("R10_PRODUCT_TYPE".equals(normalizedKey)) {
                    archivalEntity.setR10_PRODUCT_TYPE(value);

                } else if ("R10_ACCOUNT_BY_OWNERSHIP".equals(normalizedKey)) {
                    archivalEntity.setR10_ACCOUNT_BY_OWNERSHIP(value);

                } else if ("R10_ACCOUNT_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR10_ACCOUNT_NUMBER(value);

                } else if ("R10_ACCOUNT_HOLDER_INDICATOR".equals(normalizedKey)) {
                    archivalEntity.setR10_ACCOUNT_HOLDER_INDICATOR(parseBigDecimal(value));

                } else if ("R10_STATUS_OF_ACCOUNT".equals(normalizedKey)) {
                    archivalEntity.setR10_STATUS_OF_ACCOUNT(value);

                } else if ("R10_NOT_FIT_FOR_STP".equals(normalizedKey)) {
                    archivalEntity.setR10_NOT_FIT_FOR_STP(value);

                } else if ("R10_BRANCH_CODE_AND_NAME".equals(normalizedKey)) {
                    archivalEntity.setR10_BRANCH_CODE_AND_NAME(value);

                } else if ("R10_ACCOUNT_BALANCE_IN_PULA".equals(normalizedKey)) {
                    archivalEntity.setR10_ACCOUNT_BALANCE_IN_PULA(parseBigDecimal(value));

                } else if ("R10_CURRENCY_OF_ACCOUNT".equals(normalizedKey)) {
                    archivalEntity.setR10_CURRENCY_OF_ACCOUNT(value);

                } else if ("R10_EXCHANGE_RATE".equals(normalizedKey)) {
                    archivalEntity.setR10_EXCHANGE_RATE(parseBigDecimal(value));
                }
            
                else if ("R11_RECORD_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR11_RECORD_NUMBER(value);

                } else if ("R11_TITLE".equals(normalizedKey)) {
                    archivalEntity.setR11_TITLE(value);

                } else if ("R11_FIRST_NAME".equals(normalizedKey)) {
                    archivalEntity.setR11_FIRST_NAME(value);

                } else if ("R11_MIDDLE_NAME".equals(normalizedKey)) {
                    archivalEntity.setR11_MIDDLE_NAME(value);

                } else if ("R11_SURNAME".equals(normalizedKey)) {
                    archivalEntity.setR11_SURNAME(value);

                } else if ("R11_PREVIOUS_NAME".equals(normalizedKey)) {
                    archivalEntity.setR11_PREVIOUS_NAME(value);

                } else if ("R11_GENDER".equals(normalizedKey)) {
                    archivalEntity.setR11_GENDER(value);

                } else if ("R11_IDENTIFICATION_TYPE".equals(normalizedKey)) {
                    archivalEntity.setR11_IDENTIFICATION_TYPE(value);

                } else if ("R11_PASSPORT_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR11_PASSPORT_NUMBER(value);

                } else if ("R11_DATE_OF_BIRTH".equals(normalizedKey)) {
                    archivalEntity.setR11_DATE_OF_BIRTH(Date(value));

                } else if ("R11_HOME_ADDRESS".equals(normalizedKey)) {
                    archivalEntity.setR11_HOME_ADDRESS(value);

                } else if ("R11_POSTAL_ADDRESS".equals(normalizedKey)) {
                    archivalEntity.setR11_POSTAL_ADDRESS(value);

                } else if ("R11_RESIDENCE".equals(normalizedKey)) {
                    archivalEntity.setR11_RESIDENCE(value);

                } else if ("R11_EMAIL".equals(normalizedKey)) {
                    archivalEntity.setR11_EMAIL(value);

                } else if ("R11_LANDLINE".equals(normalizedKey)) {
                    archivalEntity.setR11_LANDLINE(value);

                } else if ("R11_MOBILE_PHONE_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR11_MOBILE_PHONE_NUMBER(value);

                } else if ("R11_MOBILE_MONEY_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR11_MOBILE_MONEY_NUMBER(value);

                } else if ("R11_PRODUCT_TYPE".equals(normalizedKey)) {
                    archivalEntity.setR11_PRODUCT_TYPE(value);

                } else if ("R11_ACCOUNT_BY_OWNERSHIP".equals(normalizedKey)) {
                    archivalEntity.setR11_ACCOUNT_BY_OWNERSHIP(value);

                } else if ("R11_ACCOUNT_NUMBER".equals(normalizedKey)) {
                    archivalEntity.setR11_ACCOUNT_NUMBER(value);

                } else if ("R11_ACCOUNT_HOLDER_INDICATOR".equals(normalizedKey)) {
                    archivalEntity.setR11_ACCOUNT_HOLDER_INDICATOR(parseBigDecimal(value));

                } else if ("R11_STATUS_OF_ACCOUNT".equals(normalizedKey)) {
                    archivalEntity.setR11_STATUS_OF_ACCOUNT(value);

                } else if ("R11_NOT_FIT_FOR_STP".equals(normalizedKey)) {
                    archivalEntity.setR11_NOT_FIT_FOR_STP(value);

                } else if ("R11_BRANCH_CODE_AND_NAME".equals(normalizedKey)) {
                    archivalEntity.setR11_BRANCH_CODE_AND_NAME(value);

                } else if ("R11_ACCOUNT_BALANCE_IN_PULA".equals(normalizedKey)) {
                    archivalEntity.setR11_ACCOUNT_BALANCE_IN_PULA(parseBigDecimal(value));

                } else if ("R11_CURRENCY_OF_ACCOUNT".equals(normalizedKey)) {
                    archivalEntity.setR11_CURRENCY_OF_ACCOUNT(value);

                } else if ("R11_EXCHANGE_RATE".equals(normalizedKey)) {
                    archivalEntity.setR11_EXCHANGE_RATE(parseBigDecimal(value));
                }
            
            


}

/* =========================================================
* 5️⃣ SET RESUB METADATA
* ========================================================= */
archivalEntity.setReportDate(reportDate);
archivalEntity.setReportVersion(String.valueOf(newVersion));
archivalEntity.setReportResubDate(new Date());

/* =========================================================
* 6️⃣ SAVE NEW ARCHIVAL VERSION
* ========================================================= */
BDISB1_Archival_Summary_Repo.save(archivalEntity);

System.out.println("✅ RESUB saved successfully. Version = " + newVersion);

} catch (Exception e) {
e.printStackTrace();
throw new RuntimeException(
"Error while creating archival resubmission record", e);
}
}

private BigDecimal parseBigDecimal(String value) {
return (value == null || value.trim().isEmpty())
? BigDecimal.ZERO
: new BigDecimal(value.replace(",", ""));
}

private Date Date(String value) {

    if (value == null || value.trim().isEmpty()) {
        return null;
    }

    try {
        // HTML <input type="date"> sends yyyy-MM-dd
        LocalDate localDate = LocalDate.parse(value);
        return java.sql.Date.valueOf(localDate);

    } catch (Exception e) {
        throw new RuntimeException("Invalid DATE_OF_BIRTH: " + value, e);
    }
}




/// Downloaded for Archival & Resub
public byte[] BRRSBDISB1ResubExcel(String filename, String reportId, String fromdate,
String todate, String currency, String dtltype,
String type, String version) throws Exception {

logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

if (type.equals("RESUB") & version != null) {

}

List<BDISB1_Archival_Summary_Entity> dataList1 =
BDISB1_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

if (dataList1.isEmpty()) {
logger.warn("Service: No data found for M_BDISB1 report. Returning empty result.");
return new byte[0];
}

String templateDir = env.getProperty("output.exportpathtemp");
String templateFileName = filename;
System.out.println(filename);
Path templatePath = Paths.get(templateDir, templateFileName);
System.out.println(templatePath);

logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

if (!Files.exists(templatePath)) {
//This specific exception will be caught by the controller.
throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
}
if (!Files.isReadable(templatePath)) {
//A specific exception for permission errors.
throw new SecurityException(
"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
}

//This try-with-resources block is perfect. It guarantees all resources are
//closed automatically.
try (InputStream templateInputStream = Files.newInputStream(templatePath);
Workbook workbook = WorkbookFactory.create(templateInputStream);
ByteArrayOutputStream out = new ByteArrayOutputStream()) {

Sheet sheet = workbook.getSheetAt(0);

//--- Style Definitions ---
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

//Create the font
Font font = workbook.createFont();
font.setFontHeightInPoints((short) 8); // size 8
font.setFontName("Arial");

CellStyle numberStyle = workbook.createCellStyle();
//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
numberStyle.setBorderBottom(BorderStyle.THIN);
numberStyle.setBorderTop(BorderStyle.THIN);
numberStyle.setBorderLeft(BorderStyle.THIN);
numberStyle.setBorderRight(BorderStyle.THIN);
numberStyle.setFont(font);
//--- End of Style Definitions ---
int startRow = 4;

if (!dataList1.isEmpty()) {
	for (int i = 0; i < dataList1.size(); i++) {

		BDISB1_Archival_Summary_Entity record1 = dataList1.get(i);
		System.out.println("rownumber=" + startRow + i);
		Row row = sheet.getRow(startRow + i);
		if (row == null) {
			row = sheet.createRow(startRow + i);
		}
		//Cell1 - R5_TITLE
		Cell cell0 = row.createCell(0);
		if (record1.getR5_RECORD_NUMBER() != null) {
		cell0.setCellValue(record1.getR5_RECORD_NUMBER());
		cell0.setCellStyle(textStyle);
		} else {
		cell0.setCellValue("");
		cell0.setCellStyle(textStyle);
		}

		//Cell1 - R5_TITLE
		Cell cell1 = row.createCell(1);
		if (record1.getR5_TITLE() != null) {
		 cell1.setCellValue(record1.getR5_TITLE());
		 cell1.setCellStyle(textStyle);
		} else {
		 cell1.setCellValue("");
		 cell1.setCellStyle(textStyle);
		}

		//Cell2 - R5_FIRST_NAME
		Cell cell2 = row.createCell(2);
		if (record1.getR5_FIRST_NAME() != null) {
		 cell2.setCellValue(record1.getR5_FIRST_NAME());
		 cell2.setCellStyle(textStyle);
		} else {
		 cell2.setCellValue("");
		 cell2.setCellStyle(textStyle);
		}

		//Cell3 - R5_MIDDLE_NAME
		Cell cell3 = row.createCell(3);
		if (record1.getR5_MIDDLE_NAME() != null) {
		 cell3.setCellValue(record1.getR5_MIDDLE_NAME());
		 cell3.setCellStyle(textStyle);
		} else {
		 cell3.setCellValue("");
		 cell3.setCellStyle(textStyle);
		}

		//Cell4 - R5_SURNAME
		Cell cell4 = row.createCell(4);
		if (record1.getR5_SURNAME() != null) {
		 cell4.setCellValue(record1.getR5_SURNAME());
		 cell4.setCellStyle(textStyle);
		} else {
		 cell4.setCellValue("");
		 cell4.setCellStyle(textStyle);
		}

		//Cell5 - R5_PREVIOUS_NAME
		Cell cell5 = row.createCell(5);
		if (record1.getR5_PREVIOUS_NAME() != null) {
		 cell5.setCellValue(record1.getR5_PREVIOUS_NAME());
		 cell5.setCellStyle(textStyle);
		} else {
		 cell5.setCellValue("");
		 cell5.setCellStyle(textStyle);
		}

		//Cell6 - R5_GENDER
		Cell cell6 = row.createCell(6);
		if (record1.getR5_GENDER() != null) {
		 cell6.setCellValue(record1.getR5_GENDER());
		 cell6.setCellStyle(textStyle);
		} else {
		 cell6.setCellValue("");
		 cell6.setCellStyle(textStyle);
		}

		//Cell7 - R5_IDENTIFICATION_TYPE
		Cell cell7 = row.createCell(7);
		if (record1.getR5_IDENTIFICATION_TYPE() != null) {
		 cell7.setCellValue(record1.getR5_IDENTIFICATION_TYPE());
		 cell7.setCellStyle(textStyle);
		} else {
		 cell7.setCellValue("");
		 cell7.setCellStyle(textStyle);
		}

		//Cell8 - R5_PASSPORT_NUMBER
		Cell cell8 = row.createCell(8);
		if (record1.getR5_PASSPORT_NUMBER() != null) {
		 cell8.setCellValue(record1.getR5_PASSPORT_NUMBER());
		 cell8.setCellStyle(textStyle);
		} else {
		 cell8.setCellValue("");
		 cell8.setCellStyle(textStyle);
		}

		//Cell9 - R5_DATE_OF_BIRTH
		Cell cell9 = row.createCell(9);
		if (record1.getR5_DATE_OF_BIRTH() != null) {
		 cell9.setCellValue(record1.getR5_DATE_OF_BIRTH());
		 cell9.setCellStyle(dateStyle);
		} else {
		 cell9.setCellValue("");
		 cell9.setCellStyle(textStyle);
		}

		//Cell10 - R5_HOME_ADDRESS
		Cell cell10 = row.createCell(10);
		if (record1.getR5_HOME_ADDRESS() != null) {
		 cell10.setCellValue(record1.getR5_HOME_ADDRESS());
		 cell10.setCellStyle(textStyle);
		} else {
		 cell10.setCellValue("");
		 cell10.setCellStyle(textStyle);
		}

		//Cell11 - R5_POSTAL_ADDRESS
		Cell cell11 = row.createCell(11);
		if (record1.getR5_POSTAL_ADDRESS() != null) {
		 cell11.setCellValue(record1.getR5_POSTAL_ADDRESS());
		 cell11.setCellStyle(textStyle);
		} else {
		 cell11.setCellValue("");
		 cell11.setCellStyle(textStyle);
		}

		//Cell12 - R5_RESIDENCE
		Cell cell12 = row.createCell(12);
		if (record1.getR5_RESIDENCE() != null) {
		 cell12.setCellValue(record1.getR5_RESIDENCE());
		 cell12.setCellStyle(textStyle);
		} else {
		 cell12.setCellValue("");
		 cell12.setCellStyle(textStyle);
		}

		//Cell13 - R5_EMAIL
		Cell cell13 = row.createCell(13);
		if (record1.getR5_EMAIL() != null) {
		 cell13.setCellValue(record1.getR5_EMAIL());
		 cell13.setCellStyle(textStyle);
		} else {
		 cell13.setCellValue("");
		 cell13.setCellStyle(textStyle);
		}

		//Cell14 - R5_LANDLINE
		Cell cell14 = row.createCell(14);
		if (record1.getR5_LANDLINE() != null) {
		 cell14.setCellValue(record1.getR5_LANDLINE());
		 cell14.setCellStyle(textStyle);
		} else {
		 cell14.setCellValue("");
		 cell14.setCellStyle(textStyle);
		}

		//Cell15 - R5_MOBILE_PHONE_NUMBER
		Cell cell15 = row.createCell(15);
		if (record1.getR5_MOBILE_PHONE_NUMBER() != null) {
		 cell15.setCellValue(record1.getR5_MOBILE_PHONE_NUMBER());
		 cell15.setCellStyle(textStyle);
		} else {
		 cell15.setCellValue("");
		 cell15.setCellStyle(textStyle);
		}

		//Cell16 - R5_MOBILE_MONEY_NUMBER
		Cell cell16 = row.createCell(16);
		if (record1.getR5_MOBILE_MONEY_NUMBER() != null) {
		 cell16.setCellValue(record1.getR5_MOBILE_MONEY_NUMBER());
		 cell16.setCellStyle(textStyle);
		} else {
		 cell16.setCellValue("");
		 cell16.setCellStyle(textStyle);
		}

		//Cell17 - R5_PRODUCT_TYPE
		Cell cell17 = row.createCell(17);
		if (record1.getR5_PRODUCT_TYPE() != null) {
		 cell17.setCellValue(record1.getR5_PRODUCT_TYPE());
		 cell17.setCellStyle(textStyle);
		} else {
		 cell17.setCellValue("");
		 cell17.setCellStyle(textStyle);
		}

		//Cell18 - R5_ACCOUNT_BY_OWNERSHIP
		Cell cell18 = row.createCell(18);
		if (record1.getR5_ACCOUNT_BY_OWNERSHIP() != null) {
		 cell18.setCellValue(record1.getR5_ACCOUNT_BY_OWNERSHIP());
		 cell18.setCellStyle(textStyle);
		} else {
		 cell18.setCellValue("");
		 cell18.setCellStyle(textStyle);
		}

		//Cell19 - R5_ACCOUNT_NUMBER
		Cell cell19 = row.createCell(19);
		if (record1.getR5_ACCOUNT_NUMBER() != null) {
		 cell19.setCellValue(record1.getR5_ACCOUNT_NUMBER());
		 cell19.setCellStyle(textStyle);
		} else {
		 cell19.setCellValue("");
		 cell19.setCellStyle(textStyle);
		}

		//Cell20 - R5_ACCOUNT_HOLDER_INDICATOR
		Cell cell20 = row.createCell(20);
		if (record1.getR5_ACCOUNT_HOLDER_INDICATOR() != null) {
			  cell20.setCellValue(record1.getR5_ACCOUNT_HOLDER_INDICATOR().doubleValue());
			  cell20.setCellStyle(numberStyle);
			} else {
			  cell20.setCellValue("");
			  cell20.setCellStyle(textStyle);
			}

		//Cell21 - R5_STATUS_OF_ACCOUNT
		Cell cell21 = row.createCell(21);
		if (record1.getR5_STATUS_OF_ACCOUNT() != null) {
		 cell21.setCellValue(record1.getR5_STATUS_OF_ACCOUNT());
		 cell21.setCellStyle(textStyle);
		} else {
		 cell21.setCellValue("");
		 cell21.setCellStyle(textStyle);
		}

		//Cell22 - R5_NOT_FIT_FOR_STP
		Cell cell22 = row.createCell(22);
		if (record1.getR5_NOT_FIT_FOR_STP() != null) {
		 cell22.setCellValue(record1.getR5_NOT_FIT_FOR_STP());
		 cell22.setCellStyle(textStyle);
		} else {
		 cell22.setCellValue("");
		 cell22.setCellStyle(textStyle);
		}

		//Cell23 - R5_BRANCH_CODE_AND_NAME
		Cell cell23 = row.createCell(23);
		if (record1.getR5_BRANCH_CODE_AND_NAME() != null) {
		 cell23.setCellValue(record1.getR5_BRANCH_CODE_AND_NAME());
		 cell23.setCellStyle(textStyle);
		} else {
		 cell23.setCellValue("");
		 cell23.setCellStyle(textStyle);
		}

		//Cell24 - R5_ACCOUNT_BALANCE_IN_PULA
		Cell cell24 = row.createCell(24);
		if (record1.getR5_ACCOUNT_BALANCE_IN_PULA() != null) {
		 cell24.setCellValue(record1.getR5_ACCOUNT_BALANCE_IN_PULA().doubleValue());
		 cell24.setCellStyle(numberStyle);
		} else {
		 cell24.setCellValue("");
		 cell24.setCellStyle(textStyle);
		}

		//Cell25 - R5_CURRENCY_OF_ACCOUNT
		Cell cell25 = row.createCell(25);
		if (record1.getR5_CURRENCY_OF_ACCOUNT() != null) {
		 cell25.setCellValue(record1.getR5_CURRENCY_OF_ACCOUNT());
		 cell25.setCellStyle(textStyle);
		} else {
		 cell25.setCellValue("");
		 cell25.setCellStyle(textStyle);
		}

		//Cell26 - R5_EXCHANGE_RATE
		Cell cell26 = row.createCell(26);
		if (record1.getR5_EXCHANGE_RATE() != null) {
		 cell26.setCellValue(record1.getR5_EXCHANGE_RATE().doubleValue());
		 cell26.setCellStyle(numberStyle);
		} else {
		 cell26.setCellValue("");
		 cell26.setCellStyle(textStyle);
		}


		row = sheet.getRow(5);
		//====================== R6 ======================

		//Cell1 - R5_TITLE
		cell0 = row.createCell(0);
		if (record1.getR6_RECORD_NUMBER() != null) {
		cell0.setCellValue(record1.getR6_RECORD_NUMBER());
		cell0.setCellStyle(textStyle);
		} else {
		cell0.setCellValue("");
		cell0.setCellStyle(textStyle);
		}

		//Cell1 - R6_TITLE
		cell1 = row.createCell(1);
		if (record1.getR6_TITLE() != null) {
		  cell1.setCellValue(record1.getR6_TITLE());
		  cell1.setCellStyle(textStyle);
		} else {
		  cell1.setCellValue("");
		  cell1.setCellStyle(textStyle);
		}

		//Cell2 - R6_FIRST_NAME
		cell2 = row.createCell(2);
		if (record1.getR6_FIRST_NAME() != null) {
		  cell2.setCellValue(record1.getR6_FIRST_NAME());
		  cell2.setCellStyle(textStyle);
		} else {
		  cell2.setCellValue("");
		  cell2.setCellStyle(textStyle);
		}

		//Cell3 - R6_MIDDLE_NAME
		cell3 = row.createCell(3);
		if (record1.getR6_MIDDLE_NAME() != null) {
		  cell3.setCellValue(record1.getR6_MIDDLE_NAME());
		  cell3.setCellStyle(textStyle);
		} else {
		  cell3.setCellValue("");
		  cell3.setCellStyle(textStyle);
		}

		//Cell4 - R6_SURNAME
		cell4 = row.createCell(4);
		if (record1.getR6_SURNAME() != null) {
		  cell4.setCellValue(record1.getR6_SURNAME());
		  cell4.setCellStyle(textStyle);
		} else {
		  cell4.setCellValue("");
		  cell4.setCellStyle(textStyle);
		}

		//Cell5 - R6_PREVIOUS_NAME
		cell5 = row.createCell(5);
		if (record1.getR6_PREVIOUS_NAME() != null) {
		  cell5.setCellValue(record1.getR6_PREVIOUS_NAME());
		  cell5.setCellStyle(textStyle);
		} else {
		  cell5.setCellValue("");
		  cell5.setCellStyle(textStyle);
		}

		//Cell6 - R6_GENDER
		cell6 = row.createCell(6);
		if (record1.getR6_GENDER() != null) {
		  cell6.setCellValue(record1.getR6_GENDER());
		  cell6.setCellStyle(textStyle);
		} else {
		  cell6.setCellValue("");
		  cell6.setCellStyle(textStyle);
		}

		//Cell7 - R6_IDENTIFICATION_TYPE
		cell7 = row.createCell(7);
		if (record1.getR6_IDENTIFICATION_TYPE() != null) {
		  cell7.setCellValue(record1.getR6_IDENTIFICATION_TYPE());
		  cell7.setCellStyle(textStyle);
		} else {
		  cell7.setCellValue("");
		  cell7.setCellStyle(textStyle);
		}

		//Cell8 - R6_PASSPORT_NUMBER
		cell8 = row.createCell(8);
		if (record1.getR6_PASSPORT_NUMBER() != null) {
		  cell8.setCellValue(record1.getR6_PASSPORT_NUMBER());
		  cell8.setCellStyle(textStyle);
		} else {
		  cell8.setCellValue("");
		  cell8.setCellStyle(textStyle);
		}

		//Cell9 - R6_DATE_OF_BIRTH
		cell9 = row.createCell(9);
		if (record1.getR6_DATE_OF_BIRTH() != null) {
		  cell9.setCellValue(record1.getR6_DATE_OF_BIRTH());
		  cell9.setCellStyle(dateStyle);
		} else {
		  cell9.setCellValue("");
		  cell9.setCellStyle(textStyle);
		}

		//Cell10 - R6_HOME_ADDRESS
		cell10 = row.createCell(10);
		if (record1.getR6_HOME_ADDRESS() != null) {
		  cell10.setCellValue(record1.getR6_HOME_ADDRESS());
		  cell10.setCellStyle(textStyle);
		} else {
		  cell10.setCellValue("");
		  cell10.setCellStyle(textStyle);
		}

		//Cell11 - R6_POSTAL_ADDRESS
		cell11 = row.createCell(11);
		if (record1.getR6_POSTAL_ADDRESS() != null) {
		  cell11.setCellValue(record1.getR6_POSTAL_ADDRESS());
		  cell11.setCellStyle(textStyle);
		} else {
		  cell11.setCellValue("");
		  cell11.setCellStyle(textStyle);
		}

		//Cell12 - R6_RESIDENCE
		cell12 = row.createCell(12);
		if (record1.getR6_RESIDENCE() != null) {
		  cell12.setCellValue(record1.getR6_RESIDENCE());
		  cell12.setCellStyle(textStyle);
		} else {
		  cell12.setCellValue("");
		  cell12.setCellStyle(textStyle);
		}

		//Cell13 - R6_EMAIL
		cell13 = row.createCell(13);
		if (record1.getR6_EMAIL() != null) {
		  cell13.setCellValue(record1.getR6_EMAIL());
		  cell13.setCellStyle(textStyle);
		} else {
		  cell13.setCellValue("");
		  cell13.setCellStyle(textStyle);
		}

		//Cell14 - R6_LANDLINE
		cell14 = row.createCell(14);
		if (record1.getR6_LANDLINE() != null) {
		  cell14.setCellValue(record1.getR6_LANDLINE());
		  cell14.setCellStyle(textStyle);
		} else {
		  cell14.setCellValue("");
		  cell14.setCellStyle(textStyle);
		}

		//Cell15 - R6_MOBILE_PHONE_NUMBER
		cell15 = row.createCell(15);
		if (record1.getR6_MOBILE_PHONE_NUMBER() != null) {
		  cell15.setCellValue(record1.getR6_MOBILE_PHONE_NUMBER());
		  cell15.setCellStyle(textStyle);
		} else {
		  cell15.setCellValue("");
		  cell15.setCellStyle(textStyle);
		}

		//Cell16 - R6_MOBILE_MONEY_NUMBER
		cell16 = row.createCell(16);
		if (record1.getR6_MOBILE_MONEY_NUMBER() != null) {
		  cell16.setCellValue(record1.getR6_MOBILE_MONEY_NUMBER());
		  cell16.setCellStyle(textStyle);
		} else {
		  cell16.setCellValue("");
		  cell16.setCellStyle(textStyle);
		}

		//Cell17 - R6_PRODUCT_TYPE
		cell17 = row.createCell(17);
		if (record1.getR6_PRODUCT_TYPE() != null) {
		  cell17.setCellValue(record1.getR6_PRODUCT_TYPE());
		  cell17.setCellStyle(textStyle);
		} else {
		  cell17.setCellValue("");
		  cell17.setCellStyle(textStyle);
		}

		//Cell18 - R6_ACCOUNT_BY_OWNERSHIP
		cell18 = row.createCell(18);
		if (record1.getR6_ACCOUNT_BY_OWNERSHIP() != null) {
		  cell18.setCellValue(record1.getR6_ACCOUNT_BY_OWNERSHIP());
		  cell18.setCellStyle(textStyle);
		} else {
		  cell18.setCellValue("");
		  cell18.setCellStyle(textStyle);
		}

		//Cell19 - R6_ACCOUNT_NUMBER
		cell19 = row.createCell(19);
		if (record1.getR6_ACCOUNT_NUMBER() != null) {
		  cell19.setCellValue(record1.getR6_ACCOUNT_NUMBER());
		  cell19.setCellStyle(textStyle);
		} else {
		  cell19.setCellValue("");
		  cell19.setCellStyle(textStyle);
		}

		//Cell20 - R6_ACCOUNT_HOLDER_INDICATOR
		cell20 = row.createCell(20);
		if (record1.getR6_ACCOUNT_HOLDER_INDICATOR() != null) {
		  cell20.setCellValue(record1.getR6_ACCOUNT_HOLDER_INDICATOR().doubleValue());
		  cell20.setCellStyle(numberStyle);
		} else {
		  cell20.setCellValue("");
		  cell20.setCellStyle(textStyle);
		}

		//Cell21 - R6_STATUS_OF_ACCOUNT
		cell21 = row.createCell(21);
		if (record1.getR6_STATUS_OF_ACCOUNT() != null) {
		  cell21.setCellValue(record1.getR6_STATUS_OF_ACCOUNT());
		  cell21.setCellStyle(textStyle);
		} else {
		  cell21.setCellValue("");
		  cell21.setCellStyle(textStyle);
		}

		//Cell22 - R6_NOT_FIT_FOR_STP
		cell22 = row.createCell(22);
		if (record1.getR6_NOT_FIT_FOR_STP() != null) {
		  cell22.setCellValue(record1.getR6_NOT_FIT_FOR_STP());
		  cell22.setCellStyle(textStyle);
		} else {
		  cell22.setCellValue("");
		  cell22.setCellStyle(textStyle);
		}

		//Cell23 - R6_BRANCH_CODE_AND_NAME
		cell23 = row.createCell(23);
		if (record1.getR6_BRANCH_CODE_AND_NAME() != null) {
		  cell23.setCellValue(record1.getR6_BRANCH_CODE_AND_NAME());
		  cell23.setCellStyle(textStyle);
		} else {
		  cell23.setCellValue("");
		  cell23.setCellStyle(textStyle);
		}

		//Cell24 - R6_ACCOUNT_BALANCE_IN_PULA
		cell24 = row.createCell(24);
		if (record1.getR6_ACCOUNT_BALANCE_IN_PULA() != null) {
		  cell24.setCellValue(record1.getR6_ACCOUNT_BALANCE_IN_PULA().doubleValue());
		  cell24.setCellStyle(numberStyle);
		} else {
		  cell24.setCellValue("");
		  cell24.setCellStyle(textStyle);
		}

		//Cell25 - R6_CURRENCY_OF_ACCOUNT
		cell25 = row.createCell(25);
		if (record1.getR6_CURRENCY_OF_ACCOUNT() != null) {
		  cell25.setCellValue(record1.getR6_CURRENCY_OF_ACCOUNT());
		  cell25.setCellStyle(textStyle);
		} else {
		  cell25.setCellValue("");
		  cell25.setCellStyle(textStyle);
		}

		//Cell26 - R6_EXCHANGE_RATE
		cell26 = row.createCell(26);
		if (record1.getR6_EXCHANGE_RATE() != null) {
		  cell26.setCellValue(record1.getR6_EXCHANGE_RATE().doubleValue());
		  cell26.setCellStyle(numberStyle);
		} else {
		  cell26.setCellValue("");
		  cell26.setCellStyle(textStyle);
		}



		row = sheet.getRow(6);
		//====================== R7 ======================

		//Cell0 - R7_record1_NUMBER
		cell0 = row.createCell(0);
		if (record1.getR7_RECORD_NUMBER() != null) {
		cell0.setCellValue(record1.getR7_RECORD_NUMBER());
		cell0.setCellStyle(textStyle);
		} else {
		cell0.setCellValue("");
		cell0.setCellStyle(textStyle);
		}

		//Cell1 - R7_TITLE
		cell1 = row.createCell(1);
		if (record1.getR7_TITLE() != null) {
		  cell1.setCellValue(record1.getR7_TITLE());
		  cell1.setCellStyle(textStyle);
		} else {
		  cell1.setCellValue("");
		  cell1.setCellStyle(textStyle);
		}

		//Cell2 - R7_FIRST_NAME
		cell2 = row.createCell(2);
		if (record1.getR7_FIRST_NAME() != null) {
		  cell2.setCellValue(record1.getR7_FIRST_NAME());
		  cell2.setCellStyle(textStyle);
		} else {
		  cell2.setCellValue("");
		  cell2.setCellStyle(textStyle);
		}

		//Cell3 - R7_MIDDLE_NAME
		cell3 = row.createCell(3);
		if (record1.getR7_MIDDLE_NAME() != null) {
		  cell3.setCellValue(record1.getR7_MIDDLE_NAME());
		  cell3.setCellStyle(textStyle);
		} else {
		  cell3.setCellValue("");
		  cell3.setCellStyle(textStyle);
		}

		//Cell4 - R7_SURNAME
		cell4 = row.createCell(4);
		if (record1.getR7_SURNAME() != null) {
		  cell4.setCellValue(record1.getR7_SURNAME());
		  cell4.setCellStyle(textStyle);
		} else {
		  cell4.setCellValue("");
		  cell4.setCellStyle(textStyle);
		}

		//Cell5 - R7_PREVIOUS_NAME
		cell5 = row.createCell(5);
		if (record1.getR7_PREVIOUS_NAME() != null) {
		  cell5.setCellValue(record1.getR7_PREVIOUS_NAME());
		  cell5.setCellStyle(textStyle);
		} else {
		  cell5.setCellValue("");
		  cell5.setCellStyle(textStyle);
		}

		//Cell6 - R7_GENDER
		cell6 = row.createCell(6);
		if (record1.getR7_GENDER() != null) {
		  cell6.setCellValue(record1.getR7_GENDER());
		  cell6.setCellStyle(textStyle);
		} else {
		  cell6.setCellValue("");
		  cell6.setCellStyle(textStyle);
		}

		//Cell7 - R7_IDENTIFICATION_TYPE
		cell7 = row.createCell(7);
		if (record1.getR7_IDENTIFICATION_TYPE() != null) {
		  cell7.setCellValue(record1.getR7_IDENTIFICATION_TYPE());
		  cell7.setCellStyle(textStyle);
		} else {
		  cell7.setCellValue("");
		  cell7.setCellStyle(textStyle);
		}

		//Cell8 - R7_PASSPORT_NUMBER
		cell8 = row.createCell(8);
		if (record1.getR7_PASSPORT_NUMBER() != null) {
		  cell8.setCellValue(record1.getR7_PASSPORT_NUMBER());
		  cell8.setCellStyle(textStyle);
		} else {
		  cell8.setCellValue("");
		  cell8.setCellStyle(textStyle);
		}

		//Cell9 - R7_DATE_OF_BIRTH
		cell9 = row.createCell(9);
		if (record1.getR7_DATE_OF_BIRTH() != null) {
		  cell9.setCellValue(record1.getR7_DATE_OF_BIRTH());
		  cell9.setCellStyle(dateStyle);
		} else {
		  cell9.setCellValue("");
		  cell9.setCellStyle(textStyle);
		}

		//Cell10 - R7_HOME_ADDRESS
		cell10 = row.createCell(10);
		if (record1.getR7_HOME_ADDRESS() != null) {
		  cell10.setCellValue(record1.getR7_HOME_ADDRESS());
		  cell10.setCellStyle(textStyle);
		} else {
		  cell10.setCellValue("");
		  cell10.setCellStyle(textStyle);
		}

		//Cell11 - R7_POSTAL_ADDRESS
		cell11 = row.createCell(11);
		if (record1.getR7_POSTAL_ADDRESS() != null) {
		  cell11.setCellValue(record1.getR7_POSTAL_ADDRESS());
		  cell11.setCellStyle(textStyle);
		} else {
		  cell11.setCellValue("");
		  cell11.setCellStyle(textStyle);
		}

		//Cell12 - R7_RESIDENCE
		cell12 = row.createCell(12);
		if (record1.getR7_RESIDENCE() != null) {
		  cell12.setCellValue(record1.getR7_RESIDENCE());
		  cell12.setCellStyle(textStyle);
		} else {
		  cell12.setCellValue("");
		  cell12.setCellStyle(textStyle);
		}

		//Cell13 - R7_EMAIL
		cell13 = row.createCell(13);
		if (record1.getR7_EMAIL() != null) {
		  cell13.setCellValue(record1.getR7_EMAIL());
		  cell13.setCellStyle(textStyle);
		} else {
		  cell13.setCellValue("");
		  cell13.setCellStyle(textStyle);
		}

		//Cell14 - R7_LANDLINE
		cell14 = row.createCell(14);
		if (record1.getR7_LANDLINE() != null) {
		  cell14.setCellValue(record1.getR7_LANDLINE());
		  cell14.setCellStyle(textStyle);
		} else {
		  cell14.setCellValue("");
		  cell14.setCellStyle(textStyle);
		}

		//Cell15 - R7_MOBILE_PHONE_NUMBER
		cell15 = row.createCell(15);
		if (record1.getR7_MOBILE_PHONE_NUMBER() != null) {
		  cell15.setCellValue(record1.getR7_MOBILE_PHONE_NUMBER());
		  cell15.setCellStyle(textStyle);
		} else {
		  cell15.setCellValue("");
		  cell15.setCellStyle(textStyle);
		}

		//Cell16 - R7_MOBILE_MONEY_NUMBER
		cell16 = row.createCell(16);
		if (record1.getR7_MOBILE_MONEY_NUMBER() != null) {
		  cell16.setCellValue(record1.getR7_MOBILE_MONEY_NUMBER());
		  cell16.setCellStyle(textStyle);
		} else {
		  cell16.setCellValue("");
		  cell16.setCellStyle(textStyle);
		}

		//Cell17 - R7_PRODUCT_TYPE
		cell17 = row.createCell(17);
		if (record1.getR7_PRODUCT_TYPE() != null) {
		  cell17.setCellValue(record1.getR7_PRODUCT_TYPE());
		  cell17.setCellStyle(textStyle);
		} else {
		  cell17.setCellValue("");
		  cell17.setCellStyle(textStyle);
		}

		//Cell18 - R7_ACCOUNT_BY_OWNERSHIP
		cell18 = row.createCell(18);
		if (record1.getR7_ACCOUNT_BY_OWNERSHIP() != null) {
		  cell18.setCellValue(record1.getR7_ACCOUNT_BY_OWNERSHIP());
		  cell18.setCellStyle(textStyle);
		} else {
		  cell18.setCellValue("");
		  cell18.setCellStyle(textStyle);
		}

		//Cell19 - R7_ACCOUNT_NUMBER
		cell19 = row.createCell(19);
		if (record1.getR7_ACCOUNT_NUMBER() != null) {
		  cell19.setCellValue(record1.getR7_ACCOUNT_NUMBER());
		  cell19.setCellStyle(textStyle);
		} else {
		  cell19.setCellValue("");
		  cell19.setCellStyle(textStyle);
		}

		//Cell20 - R7_ACCOUNT_HOLDER_INDICATOR
		cell20 = row.createCell(20);
		if (record1.getR11_ACCOUNT_HOLDER_INDICATOR() != null) {
		  cell20.setCellValue(record1.getR11_ACCOUNT_HOLDER_INDICATOR().doubleValue());
		  cell20.setCellStyle(numberStyle);
		} else {
		  cell20.setCellValue("");
		  cell20.setCellStyle(textStyle);
		}

		//Cell21 - R7_STATUS_OF_ACCOUNT
		cell21 = row.createCell(21);
		if (record1.getR7_STATUS_OF_ACCOUNT() != null) {
		  cell21.setCellValue(record1.getR7_STATUS_OF_ACCOUNT());
		  cell21.setCellStyle(textStyle);
		} else {
		  cell21.setCellValue("");
		  cell21.setCellStyle(textStyle);
		}

		//Cell22 - R7_NOT_FIT_FOR_STP
		cell22 = row.createCell(22);
		if (record1.getR7_NOT_FIT_FOR_STP() != null) {
		  cell22.setCellValue(record1.getR7_NOT_FIT_FOR_STP());
		  cell22.setCellStyle(textStyle);
		} else {
		  cell22.setCellValue("");
		  cell22.setCellStyle(textStyle);
		}

		//Cell23 - R7_BRANCH_CODE_AND_NAME
		cell23 = row.createCell(23);
		if (record1.getR7_BRANCH_CODE_AND_NAME() != null) {
		  cell23.setCellValue(record1.getR7_BRANCH_CODE_AND_NAME());
		  cell23.setCellStyle(textStyle);
		} else {
		  cell23.setCellValue("");
		  cell23.setCellStyle(textStyle);
		}

		//Cell24 - R7_ACCOUNT_BALANCE_IN_PULA
		cell24 = row.createCell(24);
		if (record1.getR7_ACCOUNT_BALANCE_IN_PULA() != null) {
		  cell24.setCellValue(record1.getR7_ACCOUNT_BALANCE_IN_PULA().doubleValue());
		  cell24.setCellStyle(numberStyle);
		} else {
		  cell24.setCellValue("");
		  cell24.setCellStyle(textStyle);
		}

		//Cell25 - R7_CURRENCY_OF_ACCOUNT
		cell25 = row.createCell(25);
		if (record1.getR7_CURRENCY_OF_ACCOUNT() != null) {
		  cell25.setCellValue(record1.getR7_CURRENCY_OF_ACCOUNT());
		  cell25.setCellStyle(textStyle);
		} else {
		  cell25.setCellValue("");
		  cell25.setCellStyle(textStyle);
		}

		//Cell26 - R7_EXCHANGE_RATE
		cell26 = row.createCell(26);
		if (record1.getR7_EXCHANGE_RATE() != null) {
		  cell26.setCellValue(record1.getR7_EXCHANGE_RATE().doubleValue());
		  cell26.setCellStyle(numberStyle);
		} else {
		  cell26.setCellValue("");
		  cell26.setCellStyle(textStyle);
		}



		row = sheet.getRow(7);
		//====================== R8 ======================

		//Cell0 - R8_record1_NUMBER
		cell0 = row.createCell(0);
		if (record1.getR8_RECORD_NUMBER() != null) {
		cell0.setCellValue(record1.getR8_RECORD_NUMBER());
		cell0.setCellStyle(textStyle);
		} else {
		cell0.setCellValue("");
		cell0.setCellStyle(textStyle);
		}

		//Cell1 - R8_TITLE
		cell1 = row.createCell(1);
		if (record1.getR8_TITLE() != null) {
		  cell1.setCellValue(record1.getR8_TITLE());
		  cell1.setCellStyle(textStyle);
		} else {
		  cell1.setCellValue("");
		  cell1.setCellStyle(textStyle);
		}

		//Cell2 - R8_FIRST_NAME
		cell2 = row.createCell(2);
		if (record1.getR8_FIRST_NAME() != null) {
		  cell2.setCellValue(record1.getR8_FIRST_NAME());
		  cell2.setCellStyle(textStyle);
		} else {
		  cell2.setCellValue("");
		  cell2.setCellStyle(textStyle);
		}

		//Cell3 - R8_MIDDLE_NAME
		cell3 = row.createCell(3);
		if (record1.getR8_MIDDLE_NAME() != null) {
		  cell3.setCellValue(record1.getR8_MIDDLE_NAME());
		  cell3.setCellStyle(textStyle);
		} else {
		  cell3.setCellValue("");
		  cell3.setCellStyle(textStyle);
		}

		//Cell4 - R8_SURNAME
		cell4 = row.createCell(4);
		if (record1.getR8_SURNAME() != null) {
		  cell4.setCellValue(record1.getR8_SURNAME());
		  cell4.setCellStyle(textStyle);
		} else {
		  cell4.setCellValue("");
		  cell4.setCellStyle(textStyle);
		}

		//Cell5 - R8_PREVIOUS_NAME
		cell5 = row.createCell(5);
		if (record1.getR8_PREVIOUS_NAME() != null) {
		  cell5.setCellValue(record1.getR8_PREVIOUS_NAME());
		  cell5.setCellStyle(textStyle);
		} else {
		  cell5.setCellValue("");
		  cell5.setCellStyle(textStyle);
		}

		//Cell6 - R8_GENDER
		cell6 = row.createCell(6);
		if (record1.getR8_GENDER() != null) {
		  cell6.setCellValue(record1.getR8_GENDER());
		  cell6.setCellStyle(textStyle);
		} else {
		  cell6.setCellValue("");
		  cell6.setCellStyle(textStyle);
		}

		//Cell7 - R8_IDENTIFICATION_TYPE
		cell7 = row.createCell(7);
		if (record1.getR8_IDENTIFICATION_TYPE() != null) {
		  cell7.setCellValue(record1.getR8_IDENTIFICATION_TYPE());
		  cell7.setCellStyle(textStyle);
		} else {
		  cell7.setCellValue("");
		  cell7.setCellStyle(textStyle);
		}

		//Cell8 - R8_PASSPORT_NUMBER
		cell8 = row.createCell(8);
		if (record1.getR8_PASSPORT_NUMBER() != null) {
		  cell8.setCellValue(record1.getR8_PASSPORT_NUMBER());
		  cell8.setCellStyle(textStyle);
		} else {
		  cell8.setCellValue("");
		  cell8.setCellStyle(textStyle);
		}

		//Cell9 - R8_DATE_OF_BIRTH
		cell9 = row.createCell(9);
		if (record1.getR8_DATE_OF_BIRTH() != null) {
		  cell9.setCellValue(record1.getR8_DATE_OF_BIRTH());
		  cell9.setCellStyle(dateStyle);
		} else {
		  cell9.setCellValue("");
		  cell9.setCellStyle(textStyle);
		}

		//Cell10 - R8_HOME_ADDRESS
		cell10 = row.createCell(10);
		if (record1.getR8_HOME_ADDRESS() != null) {
		  cell10.setCellValue(record1.getR8_HOME_ADDRESS());
		  cell10.setCellStyle(textStyle);
		} else {
		  cell10.setCellValue("");
		  cell10.setCellStyle(textStyle);
		}

		//Cell11 - R8_POSTAL_ADDRESS
		cell11 = row.createCell(11);
		if (record1.getR8_POSTAL_ADDRESS() != null) {
		  cell11.setCellValue(record1.getR8_POSTAL_ADDRESS());
		  cell11.setCellStyle(textStyle);
		} else {
		  cell11.setCellValue("");
		  cell11.setCellStyle(textStyle);
		}

		//Cell12 - R8_RESIDENCE
		cell12 = row.createCell(12);
		if (record1.getR8_RESIDENCE() != null) {
		  cell12.setCellValue(record1.getR8_RESIDENCE());
		  cell12.setCellStyle(textStyle);
		} else {
		  cell12.setCellValue("");
		  cell12.setCellStyle(textStyle);
		}

		//Cell13 - R8_EMAIL
		cell13 = row.createCell(13);
		if (record1.getR8_EMAIL() != null) {
		  cell13.setCellValue(record1.getR8_EMAIL());
		  cell13.setCellStyle(textStyle);
		} else {
		  cell13.setCellValue("");
		  cell13.setCellStyle(textStyle);
		}

		//Cell14 - R8_LANDLINE
		cell14 = row.createCell(14);
		if (record1.getR8_LANDLINE() != null) {
		  cell14.setCellValue(record1.getR8_LANDLINE());
		  cell14.setCellStyle(textStyle);
		} else {
		  cell14.setCellValue("");
		  cell14.setCellStyle(textStyle);
		}

		//Cell15 - R8_MOBILE_PHONE_NUMBER
		cell15 = row.createCell(15);
		if (record1.getR8_MOBILE_PHONE_NUMBER() != null) {
		  cell15.setCellValue(record1.getR8_MOBILE_PHONE_NUMBER());
		  cell15.setCellStyle(textStyle);
		} else {
		  cell15.setCellValue("");
		  cell15.setCellStyle(textStyle);
		}

		//Cell16 - R8_MOBILE_MONEY_NUMBER
		cell16 = row.createCell(16);
		if (record1.getR8_MOBILE_MONEY_NUMBER() != null) {
		  cell16.setCellValue(record1.getR8_MOBILE_MONEY_NUMBER());
		  cell16.setCellStyle(textStyle);
		} else {
		  cell16.setCellValue("");
		  cell16.setCellStyle(textStyle);
		}

		//Cell17 - R8_PRODUCT_TYPE
		cell17 = row.createCell(17);
		if (record1.getR8_PRODUCT_TYPE() != null) {
		  cell17.setCellValue(record1.getR8_PRODUCT_TYPE());
		  cell17.setCellStyle(textStyle);
		} else {
		  cell17.setCellValue("");
		  cell17.setCellStyle(textStyle);
		}

		//Cell18 - R8_ACCOUNT_BY_OWNERSHIP
		cell18 = row.createCell(18);
		if (record1.getR8_ACCOUNT_BY_OWNERSHIP() != null) {
		  cell18.setCellValue(record1.getR8_ACCOUNT_BY_OWNERSHIP());
		  cell18.setCellStyle(textStyle);
		} else {
		  cell18.setCellValue("");
		  cell18.setCellStyle(textStyle);
		}

		//Cell19 - R8_ACCOUNT_NUMBER
		cell19 = row.createCell(19);
		if (record1.getR8_ACCOUNT_NUMBER() != null) {
		  cell19.setCellValue(record1.getR8_ACCOUNT_NUMBER());
		  cell19.setCellStyle(textStyle);
		} else {
		  cell19.setCellValue("");
		  cell19.setCellStyle(textStyle);
		}

		//Cell20 - R8_ACCOUNT_HOLDER_INDICATOR
		cell20 = row.createCell(20);
		if (record1.getR8_ACCOUNT_HOLDER_INDICATOR() != null) {
		  cell20.setCellValue(record1.getR8_ACCOUNT_HOLDER_INDICATOR().doubleValue());
		  cell20.setCellStyle(numberStyle);
		} else {
		  cell20.setCellValue("");
		  cell20.setCellStyle(textStyle);
		}

		//Cell21 - R8_STATUS_OF_ACCOUNT
		cell21 = row.createCell(21);
		if (record1.getR8_STATUS_OF_ACCOUNT() != null) {
		  cell21.setCellValue(record1.getR8_STATUS_OF_ACCOUNT());
		  cell21.setCellStyle(textStyle);
		} else {
		  cell21.setCellValue("");
		  cell21.setCellStyle(textStyle);
		}

		//Cell22 - R8_NOT_FIT_FOR_STP
		cell22 = row.createCell(22);
		if (record1.getR8_NOT_FIT_FOR_STP() != null) {
		  cell22.setCellValue(record1.getR8_NOT_FIT_FOR_STP());
		  cell22.setCellStyle(textStyle);
		} else {
		  cell22.setCellValue("");
		  cell22.setCellStyle(textStyle);
		}

		//Cell23 - R8_BRANCH_CODE_AND_NAME
		cell23 = row.createCell(23);
		if (record1.getR8_BRANCH_CODE_AND_NAME() != null) {
		  cell23.setCellValue(record1.getR8_BRANCH_CODE_AND_NAME());
		  cell23.setCellStyle(textStyle);
		} else {
		  cell23.setCellValue("");
		  cell23.setCellStyle(textStyle);
		}

		//Cell24 - R8_ACCOUNT_BALANCE_IN_PULA
		cell24 = row.createCell(24);
		if (record1.getR8_ACCOUNT_BALANCE_IN_PULA() != null) {
		  cell24.setCellValue(record1.getR8_ACCOUNT_BALANCE_IN_PULA().doubleValue());
		  cell24.setCellStyle(numberStyle);
		} else {
		  cell24.setCellValue("");
		  cell24.setCellStyle(textStyle);
		}

		//Cell25 - R8_CURRENCY_OF_ACCOUNT
		cell25 = row.createCell(25);
		if (record1.getR8_CURRENCY_OF_ACCOUNT() != null) {
		  cell25.setCellValue(record1.getR8_CURRENCY_OF_ACCOUNT());
		  cell25.setCellStyle(textStyle);
		} else {
		  cell25.setCellValue("");
		  cell25.setCellStyle(textStyle);
		}

		//Cell26 - R8_EXCHANGE_RATE
		cell26 = row.createCell(26);
		if (record1.getR8_EXCHANGE_RATE() != null) {
		  cell26.setCellValue(record1.getR8_EXCHANGE_RATE().doubleValue());
		  cell26.setCellStyle(numberStyle);
		} else {
		  cell26.setCellValue("");
		  cell26.setCellStyle(textStyle);
		}


		//====================== R9 ======================
		row = sheet.getRow(8);
		//Cell0 - R9_record1_NUMBER
		cell0 = row.createCell(0);
		if (record1.getR9_RECORD_NUMBER() != null) {
		cell0.setCellValue(record1.getR9_RECORD_NUMBER());
		cell0.setCellStyle(textStyle);
		} else {
		cell0.setCellValue("");
		cell0.setCellStyle(textStyle);
		}

		//Cell1 - R9_TITLE
		cell1 = row.createCell(1);
		if (record1.getR9_TITLE() != null) {
		  cell1.setCellValue(record1.getR9_TITLE());
		  cell1.setCellStyle(textStyle);
		} else {
		  cell1.setCellValue("");
		  cell1.setCellStyle(textStyle);
		}

		//Cell2 - R9_FIRST_NAME
		cell2 = row.createCell(2);
		if (record1.getR9_FIRST_NAME() != null) {
		  cell2.setCellValue(record1.getR9_FIRST_NAME());
		  cell2.setCellStyle(textStyle);
		} else {
		  cell2.setCellValue("");
		  cell2.setCellStyle(textStyle);
		}

		//Cell3 - R9_MIDDLE_NAME
		cell3 = row.createCell(3);
		if (record1.getR9_MIDDLE_NAME() != null) {
		  cell3.setCellValue(record1.getR9_MIDDLE_NAME());
		  cell3.setCellStyle(textStyle);
		} else {
		  cell3.setCellValue("");
		  cell3.setCellStyle(textStyle);
		}

		//Cell4 - R9_SURNAME
		cell4 = row.createCell(4);
		if (record1.getR9_SURNAME() != null) {
		  cell4.setCellValue(record1.getR9_SURNAME());
		  cell4.setCellStyle(textStyle);
		} else {
		  cell4.setCellValue("");
		  cell4.setCellStyle(textStyle);
		}

		//Cell5 - R9_PREVIOUS_NAME
		cell5 = row.createCell(5);
		if (record1.getR9_PREVIOUS_NAME() != null) {
		  cell5.setCellValue(record1.getR9_PREVIOUS_NAME());
		  cell5.setCellStyle(textStyle);
		} else {
		  cell5.setCellValue("");
		  cell5.setCellStyle(textStyle);
		}

		//Cell6 - R9_GENDER
		cell6 = row.createCell(6);
		if (record1.getR9_GENDER() != null) {
		  cell6.setCellValue(record1.getR9_GENDER());
		  cell6.setCellStyle(textStyle);
		} else {
		  cell6.setCellValue("");
		  cell6.setCellStyle(textStyle);
		}

		//Cell7 - R9_IDENTIFICATION_TYPE
		cell7 = row.createCell(7);
		if (record1.getR9_IDENTIFICATION_TYPE() != null) {
		  cell7.setCellValue(record1.getR9_IDENTIFICATION_TYPE());
		  cell7.setCellStyle(textStyle);
		} else {
		  cell7.setCellValue("");
		  cell7.setCellStyle(textStyle);
		}

		//Cell8 - R9_PASSPORT_NUMBER
		cell8 = row.createCell(8);
		if (record1.getR9_PASSPORT_NUMBER() != null) {
		  cell8.setCellValue(record1.getR9_PASSPORT_NUMBER());
		  cell8.setCellStyle(textStyle);
		} else {
		  cell8.setCellValue("");
		  cell8.setCellStyle(textStyle);
		}

		//Cell9 - R9_DATE_OF_BIRTH
		cell9 = row.createCell(9);
		if (record1.getR9_DATE_OF_BIRTH() != null) {
		  cell9.setCellValue(record1.getR9_DATE_OF_BIRTH());
		  cell9.setCellStyle(dateStyle);
		} else {
		  cell9.setCellValue("");
		  cell9.setCellStyle(textStyle);
		}

		//Cell10 - R9_HOME_ADDRESS
		cell10 = row.createCell(10);
		if (record1.getR9_HOME_ADDRESS() != null) {
		  cell10.setCellValue(record1.getR9_HOME_ADDRESS());
		  cell10.setCellStyle(textStyle);
		} else {
		  cell10.setCellValue("");
		  cell10.setCellStyle(textStyle);
		}

		//Cell11 - R9_POSTAL_ADDRESS
		cell11 = row.createCell(11);
		if (record1.getR9_POSTAL_ADDRESS() != null) {
		  cell11.setCellValue(record1.getR9_POSTAL_ADDRESS());
		  cell11.setCellStyle(textStyle);
		} else {
		  cell11.setCellValue("");
		  cell11.setCellStyle(textStyle);
		}

		//Cell12 - R9_RESIDENCE
		cell12 = row.createCell(12);
		if (record1.getR9_RESIDENCE() != null) {
		  cell12.setCellValue(record1.getR9_RESIDENCE());
		  cell12.setCellStyle(textStyle);
		} else {
		  cell12.setCellValue("");
		  cell12.setCellStyle(textStyle);
		}

		//Cell13 - R9_EMAIL
		cell13 = row.createCell(13);
		if (record1.getR9_EMAIL() != null) {
		  cell13.setCellValue(record1.getR9_EMAIL());
		  cell13.setCellStyle(textStyle);
		} else {
		  cell13.setCellValue("");
		  cell13.setCellStyle(textStyle);
		}

		//Cell14 - R9_LANDLINE
		cell14 = row.createCell(14);
		if (record1.getR9_LANDLINE() != null) {
		  cell14.setCellValue(record1.getR9_LANDLINE());
		  cell14.setCellStyle(textStyle);
		} else {
		  cell14.setCellValue("");
		  cell14.setCellStyle(textStyle);
		}

		//Cell15 - R9_MOBILE_PHONE_NUMBER
		cell15 = row.createCell(15);
		if (record1.getR9_MOBILE_PHONE_NUMBER() != null) {
		  cell15.setCellValue(record1.getR9_MOBILE_PHONE_NUMBER());
		  cell15.setCellStyle(textStyle);
		} else {
		  cell15.setCellValue("");
		  cell15.setCellStyle(textStyle);
		}

		//Cell16 - R9_MOBILE_MONEY_NUMBER
		cell16 = row.createCell(16);
		if (record1.getR9_MOBILE_MONEY_NUMBER() != null) {
		  cell16.setCellValue(record1.getR9_MOBILE_MONEY_NUMBER());
		  cell16.setCellStyle(textStyle);
		} else {
		  cell16.setCellValue("");
		  cell16.setCellStyle(textStyle);
		}

		//Cell17 - R9_PRODUCT_TYPE
		cell17 = row.createCell(17);
		if (record1.getR9_PRODUCT_TYPE() != null) {
		  cell17.setCellValue(record1.getR9_PRODUCT_TYPE());
		  cell17.setCellStyle(textStyle);
		} else {
		  cell17.setCellValue("");
		  cell17.setCellStyle(textStyle);
		}

		//Cell18 - R9_ACCOUNT_BY_OWNERSHIP
		cell18 = row.createCell(18);
		if (record1.getR9_ACCOUNT_BY_OWNERSHIP() != null) {
		  cell18.setCellValue(record1.getR9_ACCOUNT_BY_OWNERSHIP());
		  cell18.setCellStyle(textStyle);
		} else {
		  cell18.setCellValue("");
		  cell18.setCellStyle(textStyle);
		}

		//Cell19 - R9_ACCOUNT_NUMBER
		cell19 = row.createCell(19);
		if (record1.getR9_ACCOUNT_NUMBER() != null) {
		  cell19.setCellValue(record1.getR9_ACCOUNT_NUMBER());
		  cell19.setCellStyle(textStyle);
		} else {
		  cell19.setCellValue("");
		  cell19.setCellStyle(textStyle);
		}

		//Cell20 - R9_ACCOUNT_HOLDER_INDICATOR
		cell20 = row.createCell(20);
		if (record1.getR9_ACCOUNT_HOLDER_INDICATOR() != null) {
		  cell20.setCellValue(record1.getR9_ACCOUNT_HOLDER_INDICATOR().doubleValue());
		  cell20.setCellStyle(numberStyle);
		} else {
		  cell20.setCellValue("");
		  cell20.setCellStyle(textStyle);
		}

		//Cell21 - R9_STATUS_OF_ACCOUNT
		cell21 = row.createCell(21);
		if (record1.getR9_STATUS_OF_ACCOUNT() != null) {
		  cell21.setCellValue(record1.getR9_STATUS_OF_ACCOUNT());
		  cell21.setCellStyle(textStyle);
		} else {
		  cell21.setCellValue("");
		  cell21.setCellStyle(textStyle);
		}

		//Cell22 - R9_NOT_FIT_FOR_STP
		cell22 = row.createCell(22);
		if (record1.getR9_NOT_FIT_FOR_STP() != null) {
		  cell22.setCellValue(record1.getR9_NOT_FIT_FOR_STP());
		  cell22.setCellStyle(textStyle);
		} else {
		  cell22.setCellValue("");
		  cell22.setCellStyle(textStyle);
		}

		//Cell23 - R9_BRANCH_CODE_AND_NAME
		cell23 = row.createCell(23);
		if (record1.getR9_BRANCH_CODE_AND_NAME() != null) {
		  cell23.setCellValue(record1.getR9_BRANCH_CODE_AND_NAME());
		  cell23.setCellStyle(textStyle);
		} else {
		  cell23.setCellValue("");
		  cell23.setCellStyle(textStyle);
		}

		//Cell24 - R9_ACCOUNT_BALANCE_IN_PULA
		cell24 = row.createCell(24);
		if (record1.getR9_ACCOUNT_BALANCE_IN_PULA() != null) {
		  cell24.setCellValue(record1.getR9_ACCOUNT_BALANCE_IN_PULA().doubleValue());
		  cell24.setCellStyle(numberStyle);
		} else {
		  cell24.setCellValue("");
		  cell24.setCellStyle(textStyle);
		}

		//Cell25 - R9_CURRENCY_OF_ACCOUNT
		cell25 = row.createCell(25);
		if (record1.getR9_CURRENCY_OF_ACCOUNT() != null) {
		  cell25.setCellValue(record1.getR9_CURRENCY_OF_ACCOUNT());
		  cell25.setCellStyle(textStyle);
		} else {
		  cell25.setCellValue("");
		  cell25.setCellStyle(textStyle);
		}

		//Cell26 - R9_EXCHANGE_RATE
		cell26 = row.createCell(26);
		if (record1.getR9_EXCHANGE_RATE() != null) {
		  cell26.setCellValue(record1.getR9_EXCHANGE_RATE().doubleValue());
		  cell26.setCellStyle(numberStyle);
		} else {
		  cell26.setCellValue("");
		  cell26.setCellStyle(textStyle);
		}



		row = sheet.getRow(9);
		//====================== R10 ======================

		//Cell0 - R10_record1_NUMBER
		cell0 = row.createCell(0);
		if (record1.getR10_RECORD_NUMBER() != null) {
		cell0.setCellValue(record1.getR10_RECORD_NUMBER());
		cell0.setCellStyle(textStyle);
		} else {
		cell0.setCellValue("");
		cell0.setCellStyle(textStyle);
		}

		//Cell1 - R10_TITLE
		cell1 = row.createCell(1);
		if (record1.getR10_TITLE() != null) {
		  cell1.setCellValue(record1.getR10_TITLE());
		  cell1.setCellStyle(textStyle);
		} else {
		  cell1.setCellValue("");
		  cell1.setCellStyle(textStyle);
		}

		//Cell2 - R10_FIRST_NAME
		cell2 = row.createCell(2);
		if (record1.getR10_FIRST_NAME() != null) {
		  cell2.setCellValue(record1.getR10_FIRST_NAME());
		  cell2.setCellStyle(textStyle);
		} else {
		  cell2.setCellValue("");
		  cell2.setCellStyle(textStyle);
		}

		//Cell3 - R10_MIDDLE_NAME
		cell3 = row.createCell(3);
		if (record1.getR10_MIDDLE_NAME() != null) {
		  cell3.setCellValue(record1.getR10_MIDDLE_NAME());
		  cell3.setCellStyle(textStyle);
		} else {
		  cell3.setCellValue("");
		  cell3.setCellStyle(textStyle);
		}

		//Cell4 - R10_SURNAME
		cell4 = row.createCell(4);
		if (record1.getR10_SURNAME() != null) {
		  cell4.setCellValue(record1.getR10_SURNAME());
		  cell4.setCellStyle(textStyle);
		} else {
		  cell4.setCellValue("");
		  cell4.setCellStyle(textStyle);
		}

		//Cell5 - R10_PREVIOUS_NAME
		cell5 = row.createCell(5);
		if (record1.getR10_PREVIOUS_NAME() != null) {
		  cell5.setCellValue(record1.getR10_PREVIOUS_NAME());
		  cell5.setCellStyle(textStyle);
		} else {
		  cell5.setCellValue("");
		  cell5.setCellStyle(textStyle);
		}

		//Cell6 - R10_GENDER
		cell6 = row.createCell(6);
		if (record1.getR10_GENDER() != null) {
		  cell6.setCellValue(record1.getR10_GENDER());
		  cell6.setCellStyle(textStyle);
		} else {
		  cell6.setCellValue("");
		  cell6.setCellStyle(textStyle);
		}

		//Cell7 - R10_IDENTIFICATION_TYPE
		cell7 = row.createCell(7);
		if (record1.getR10_IDENTIFICATION_TYPE() != null) {
		  cell7.setCellValue(record1.getR10_IDENTIFICATION_TYPE());
		  cell7.setCellStyle(textStyle);
		} else {
		  cell7.setCellValue("");
		  cell7.setCellStyle(textStyle);
		}

		//Cell8 - R10_PASSPORT_NUMBER
		cell8 = row.createCell(8);
		if (record1.getR10_PASSPORT_NUMBER() != null) {
		  cell8.setCellValue(record1.getR10_PASSPORT_NUMBER());
		  cell8.setCellStyle(textStyle);
		} else {
		  cell8.setCellValue("");
		  cell8.setCellStyle(textStyle);
		}

		//Cell9 - R10_DATE_OF_BIRTH
		cell9 = row.createCell(9);
		if (record1.getR10_DATE_OF_BIRTH() != null) {
		  cell9.setCellValue(record1.getR10_DATE_OF_BIRTH());
		  cell9.setCellStyle(dateStyle);
		} else {
		  cell9.setCellValue("");
		  cell9.setCellStyle(textStyle);
		}

		//Cell10 - R10_HOME_ADDRESS
		cell10 = row.createCell(10);
		if (record1.getR10_HOME_ADDRESS() != null) {
		  cell10.setCellValue(record1.getR10_HOME_ADDRESS());
		  cell10.setCellStyle(textStyle);
		} else {
		  cell10.setCellValue("");
		  cell10.setCellStyle(textStyle);
		}

		//Cell11 - R10_POSTAL_ADDRESS
		cell11 = row.createCell(11);
		if (record1.getR10_POSTAL_ADDRESS() != null) {
		  cell11.setCellValue(record1.getR10_POSTAL_ADDRESS());
		  cell11.setCellStyle(textStyle);
		} else {
		  cell11.setCellValue("");
		  cell11.setCellStyle(textStyle);
		}

		//Cell12 - R10_RESIDENCE
		cell12 = row.createCell(12);
		if (record1.getR10_RESIDENCE() != null) {
		  cell12.setCellValue(record1.getR10_RESIDENCE());
		  cell12.setCellStyle(textStyle);
		} else {
		  cell12.setCellValue("");
		  cell12.setCellStyle(textStyle);
		}

		//Cell13 - R10_EMAIL
		cell13 = row.createCell(13);
		if (record1.getR10_EMAIL() != null) {
		  cell13.setCellValue(record1.getR10_EMAIL());
		  cell13.setCellStyle(textStyle);
		} else {
		  cell13.setCellValue("");
		  cell13.setCellStyle(textStyle);
		}

		//Cell14 - R10_LANDLINE
		cell14 = row.createCell(14);
		if (record1.getR10_LANDLINE() != null) {
		  cell14.setCellValue(record1.getR10_LANDLINE());
		  cell14.setCellStyle(textStyle);
		} else {
		  cell14.setCellValue("");
		  cell14.setCellStyle(textStyle);
		}

		//Cell15 - R10_MOBILE_PHONE_NUMBER
		cell15 = row.createCell(15);
		if (record1.getR10_MOBILE_PHONE_NUMBER() != null) {
		  cell15.setCellValue(record1.getR10_MOBILE_PHONE_NUMBER());
		  cell15.setCellStyle(textStyle);
		} else {
		  cell15.setCellValue("");
		  cell15.setCellStyle(textStyle);
		}

		//Cell16 - R10_MOBILE_MONEY_NUMBER
		cell16 = row.createCell(16);
		if (record1.getR10_MOBILE_MONEY_NUMBER() != null) {
		  cell16.setCellValue(record1.getR10_MOBILE_MONEY_NUMBER());
		  cell16.setCellStyle(textStyle);
		} else {
		  cell16.setCellValue("");
		  cell16.setCellStyle(textStyle);
		}

		//Cell17 - R10_PRODUCT_TYPE
		cell17 = row.createCell(17);
		if (record1.getR10_PRODUCT_TYPE() != null) {
		  cell17.setCellValue(record1.getR10_PRODUCT_TYPE());
		  cell17.setCellStyle(textStyle);
		} else {
		  cell17.setCellValue("");
		  cell17.setCellStyle(textStyle);
		}

		//Cell18 - R10_ACCOUNT_BY_OWNERSHIP
		cell18 = row.createCell(18);
		if (record1.getR10_ACCOUNT_BY_OWNERSHIP() != null) {
		  cell18.setCellValue(record1.getR10_ACCOUNT_BY_OWNERSHIP());
		  cell18.setCellStyle(textStyle);
		} else {
		  cell18.setCellValue("");
		  cell18.setCellStyle(textStyle);
		}

		//Cell19 - R10_ACCOUNT_NUMBER
		cell19 = row.createCell(19);
		if (record1.getR10_ACCOUNT_NUMBER() != null) {
		  cell19.setCellValue(record1.getR10_ACCOUNT_NUMBER());
		  cell19.setCellStyle(textStyle);
		} else {
		  cell19.setCellValue("");
		  cell19.setCellStyle(textStyle);
		}

		//Cell20 - R10_ACCOUNT_HOLDER_INDICATOR
		cell20 = row.createCell(20);
		if (record1.getR10_ACCOUNT_HOLDER_INDICATOR() != null) {
		  cell20.setCellValue(record1.getR10_ACCOUNT_HOLDER_INDICATOR().doubleValue());
		  cell20.setCellStyle(numberStyle);
		} else {
		  cell20.setCellValue("");
		  cell20.setCellStyle(textStyle);
		}

		//Cell21 - R10_STATUS_OF_ACCOUNT
		cell21 = row.createCell(21);
		if (record1.getR10_STATUS_OF_ACCOUNT() != null) {
		  cell21.setCellValue(record1.getR10_STATUS_OF_ACCOUNT());
		  cell21.setCellStyle(textStyle);
		} else {
		  cell21.setCellValue("");
		  cell21.setCellStyle(textStyle);
		}

		//Cell22 - R10_NOT_FIT_FOR_STP
		cell22 = row.createCell(22);
		if (record1.getR10_NOT_FIT_FOR_STP() != null) {
		  cell22.setCellValue(record1.getR10_NOT_FIT_FOR_STP());
		  cell22.setCellStyle(textStyle);
		} else {
		  cell22.setCellValue("");
		  cell22.setCellStyle(textStyle);
		}

		//Cell23 - R10_BRANCH_CODE_AND_NAME
		cell23 = row.createCell(23);
		if (record1.getR10_BRANCH_CODE_AND_NAME() != null) {
		  cell23.setCellValue(record1.getR10_BRANCH_CODE_AND_NAME());
		  cell23.setCellStyle(textStyle);
		} else {
		  cell23.setCellValue("");
		  cell23.setCellStyle(textStyle);
		}

		//Cell24 - R10_ACCOUNT_BALANCE_IN_PULA
		cell24 = row.createCell(24);
		if (record1.getR10_ACCOUNT_BALANCE_IN_PULA() != null) {
		  cell24.setCellValue(record1.getR10_ACCOUNT_BALANCE_IN_PULA().doubleValue());
		  cell24.setCellStyle(numberStyle);
		} else {
		  cell24.setCellValue("");
		  cell24.setCellStyle(textStyle);
		}

		//Cell25 - R10_CURRENCY_OF_ACCOUNT
		cell25 = row.createCell(25);
		if (record1.getR10_CURRENCY_OF_ACCOUNT() != null) {
		  cell25.setCellValue(record1.getR10_CURRENCY_OF_ACCOUNT());
		  cell25.setCellStyle(textStyle);
		} else {
		  cell25.setCellValue("");
		  cell25.setCellStyle(textStyle);
		}

		//Cell26 - R10_EXCHANGE_RATE
		cell26 = row.createCell(26);
		if (record1.getR10_EXCHANGE_RATE() != null) {
		  cell26.setCellValue(record1.getR10_EXCHANGE_RATE().doubleValue());
		  cell26.setCellStyle(numberStyle);
		} else {
		  cell26.setCellValue("");
		  cell26.setCellStyle(textStyle);
		}

		//====================== R11 ======================
		row = sheet.getRow(10);
		//Cell0 - R11_record1_NUMBER
		cell0 = row.createCell(0);
		if (record1.getR11_RECORD_NUMBER() != null) {
		cell0.setCellValue(record1.getR11_RECORD_NUMBER());
		cell0.setCellStyle(textStyle);
		} else {
		cell0.setCellValue("");
		cell0.setCellStyle(textStyle);
		}

		//Cell1 - R11_TITLE
		cell1 = row.createCell(1);
		if (record1.getR11_TITLE() != null) {
		  cell1.setCellValue(record1.getR11_TITLE());
		  cell1.setCellStyle(textStyle);
		} else {
		  cell1.setCellValue("");
		  cell1.setCellStyle(textStyle);
		}

		//Cell2 - R11_FIRST_NAME
		cell2 = row.createCell(2);
		if (record1.getR11_FIRST_NAME() != null) {
		  cell2.setCellValue(record1.getR11_FIRST_NAME());
		  cell2.setCellStyle(textStyle);
		} else {
		  cell2.setCellValue("");
		  cell2.setCellStyle(textStyle);
		}

		//Cell3 - R11_MIDDLE_NAME
		cell3 = row.createCell(3);
		if (record1.getR11_MIDDLE_NAME() != null) {
		  cell3.setCellValue(record1.getR11_MIDDLE_NAME());
		  cell3.setCellStyle(textStyle);
		} else {
		  cell3.setCellValue("");
		  cell3.setCellStyle(textStyle);
		}

		//Cell4 - R11_SURNAME
		cell4 = row.createCell(4);
		if (record1.getR11_SURNAME() != null) {
		  cell4.setCellValue(record1.getR11_SURNAME());
		  cell4.setCellStyle(textStyle);
		} else {
		  cell4.setCellValue("");
		  cell4.setCellStyle(textStyle);
		}

		//Cell5 - R11_PREVIOUS_NAME
		cell5 = row.createCell(5);
		if (record1.getR11_PREVIOUS_NAME() != null) {
		  cell5.setCellValue(record1.getR11_PREVIOUS_NAME());
		  cell5.setCellStyle(textStyle);
		} else {
		  cell5.setCellValue("");
		  cell5.setCellStyle(textStyle);
		}

		//Cell6 - R11_GENDER
		cell6 = row.createCell(6);
		if (record1.getR11_GENDER() != null) {
		  cell6.setCellValue(record1.getR11_GENDER());
		  cell6.setCellStyle(textStyle);
		} else {
		  cell6.setCellValue("");
		  cell6.setCellStyle(textStyle);
		}

		//Cell7 - R11_IDENTIFICATION_TYPE
		cell7 = row.createCell(7);
		if (record1.getR11_IDENTIFICATION_TYPE() != null) {
		  cell7.setCellValue(record1.getR11_IDENTIFICATION_TYPE());
		  cell7.setCellStyle(textStyle);
		} else {
		  cell7.setCellValue("");
		  cell7.setCellStyle(textStyle);
		}

		//Cell8 - R11_PASSPORT_NUMBER
		cell8 = row.createCell(8);
		if (record1.getR11_PASSPORT_NUMBER() != null) {
		  cell8.setCellValue(record1.getR11_PASSPORT_NUMBER());
		  cell8.setCellStyle(textStyle);
		} else {
		  cell8.setCellValue("");
		  cell8.setCellStyle(textStyle);
		}

		//Cell9 - R11_DATE_OF_BIRTH
		cell9 = row.createCell(9);
		if (record1.getR11_DATE_OF_BIRTH() != null) {
		  cell9.setCellValue(record1.getR11_DATE_OF_BIRTH());
		  cell9.setCellStyle(dateStyle);
		} else {
		  cell9.setCellValue("");
		  cell9.setCellStyle(textStyle);
		}

		//Cell10 - R11_HOME_ADDRESS
		cell10 = row.createCell(10);
		if (record1.getR11_HOME_ADDRESS() != null) {
		  cell10.setCellValue(record1.getR11_HOME_ADDRESS());
		  cell10.setCellStyle(textStyle);
		} else {
		  cell10.setCellValue("");
		  cell10.setCellStyle(textStyle);
		}

		//Cell11 - R11_POSTAL_ADDRESS
		cell11 = row.createCell(11);
		if (record1.getR11_POSTAL_ADDRESS() != null) {
		  cell11.setCellValue(record1.getR11_POSTAL_ADDRESS());
		  cell11.setCellStyle(textStyle);
		} else {
		  cell11.setCellValue("");
		  cell11.setCellStyle(textStyle);
		}

		//Cell12 - R11_RESIDENCE
		cell12 = row.createCell(12);
		if (record1.getR11_RESIDENCE() != null) {
		  cell12.setCellValue(record1.getR11_RESIDENCE());
		  cell12.setCellStyle(textStyle);
		} else {
		  cell12.setCellValue("");
		  cell12.setCellStyle(textStyle);
		}

		//Cell13 - R11_EMAIL
		cell13 = row.createCell(13);
		if (record1.getR11_EMAIL() != null) {
		  cell13.setCellValue(record1.getR11_EMAIL());
		  cell13.setCellStyle(textStyle);
		} else {
		  cell13.setCellValue("");
		  cell13.setCellStyle(textStyle);
		}

		//Cell14 - R11_LANDLINE
		cell14 = row.createCell(14);
		if (record1.getR11_LANDLINE() != null) {
		  cell14.setCellValue(record1.getR11_LANDLINE());
		  cell14.setCellStyle(textStyle);
		} else {
		  cell14.setCellValue("");
		  cell14.setCellStyle(textStyle);
		}

		//Cell15 - R11_MOBILE_PHONE_NUMBER
		cell15 = row.createCell(15);
		if (record1.getR11_MOBILE_PHONE_NUMBER() != null) {
		  cell15.setCellValue(record1.getR11_MOBILE_PHONE_NUMBER());
		  cell15.setCellStyle(textStyle);
		} else {
		  cell15.setCellValue("");
		  cell15.setCellStyle(textStyle);
		}

		//Cell16 - R11_MOBILE_MONEY_NUMBER
		cell16 = row.createCell(16);
		if (record1.getR11_MOBILE_MONEY_NUMBER() != null) {
		  cell16.setCellValue(record1.getR11_MOBILE_MONEY_NUMBER());
		  cell16.setCellStyle(textStyle);
		} else {
		  cell16.setCellValue("");
		  cell16.setCellStyle(textStyle);
		}

		//Cell17 - R11_PRODUCT_TYPE
		cell17 = row.createCell(17);
		if (record1.getR11_PRODUCT_TYPE() != null) {
		  cell17.setCellValue(record1.getR11_PRODUCT_TYPE());
		  cell17.setCellStyle(textStyle);
		} else {
		  cell17.setCellValue("");
		  cell17.setCellStyle(textStyle);
		}

		//Cell18 - R11_ACCOUNT_BY_OWNERSHIP
		cell18 = row.createCell(18);
		if (record1.getR11_ACCOUNT_BY_OWNERSHIP() != null) {
		  cell18.setCellValue(record1.getR11_ACCOUNT_BY_OWNERSHIP());
		  cell18.setCellStyle(textStyle);
		} else {
		  cell18.setCellValue("");
		  cell18.setCellStyle(textStyle);
		}

		//Cell19 - R11_ACCOUNT_NUMBER
		cell19 = row.createCell(19);
		if (record1.getR11_ACCOUNT_NUMBER() != null) {
		  cell19.setCellValue(record1.getR11_ACCOUNT_NUMBER());
		  cell19.setCellStyle(textStyle);
		} else {
		  cell19.setCellValue("");
		  cell19.setCellStyle(textStyle);
		}

		//Cell20 - R11_ACCOUNT_HOLDER_INDICATOR

		cell20 = row.createCell(20);
		if (record1.getR11_ACCOUNT_HOLDER_INDICATOR() != null) {
		  cell20.setCellValue(record1.getR11_ACCOUNT_HOLDER_INDICATOR().doubleValue());
		  cell20.setCellStyle(numberStyle);
		} else {
		  cell20.setCellValue("");
		  cell20.setCellStyle(textStyle);
		}

		//Cell21 - R11_STATUS_OF_ACCOUNT
		cell21 = row.createCell(21);
		if (record1.getR11_STATUS_OF_ACCOUNT() != null) {
		  cell21.setCellValue(record1.getR11_STATUS_OF_ACCOUNT());
		  cell21.setCellStyle(textStyle);
		} else {
		  cell21.setCellValue("");
		  cell21.setCellStyle(textStyle);
		}

		//Cell22 - R11_NOT_FIT_FOR_STP
		cell22 = row.createCell(22);
		if (record1.getR11_NOT_FIT_FOR_STP() != null) {
		  cell22.setCellValue(record1.getR11_NOT_FIT_FOR_STP());
		  cell22.setCellStyle(textStyle);
		} else {
		  cell22.setCellValue("");
		  cell22.setCellStyle(textStyle);
		}

		//Cell23 - R11_BRANCH_CODE_AND_NAME
		cell23 = row.createCell(23);
		if (record1.getR11_BRANCH_CODE_AND_NAME() != null) {
		  cell23.setCellValue(record1.getR11_BRANCH_CODE_AND_NAME());
		  cell23.setCellStyle(textStyle);
		} else {
		  cell23.setCellValue("");
		  cell23.setCellStyle(textStyle);
		}

		//Cell24 - R11_ACCOUNT_BALANCE_IN_PULA
		cell24 = row.createCell(24);
		if (record1.getR11_ACCOUNT_BALANCE_IN_PULA() != null) {
		  cell24.setCellValue(record1.getR11_ACCOUNT_BALANCE_IN_PULA().doubleValue());
		  cell24.setCellStyle(numberStyle);
		} else {
		  cell24.setCellValue("");
		  cell24.setCellStyle(textStyle);
		}

		//Cell25 - R11_CURRENCY_OF_ACCOUNT
		cell25 = row.createCell(25);
		if (record1.getR11_CURRENCY_OF_ACCOUNT() != null) {
		  cell25.setCellValue(record1.getR11_CURRENCY_OF_ACCOUNT());
		  cell25.setCellStyle(textStyle);
		} else {
		  cell25.setCellValue("");
		  cell25.setCellStyle(textStyle);
		}

		//Cell26 - R11_EXCHANGE_RATE
		cell26 = row.createCell(26);
		if (record1.getR11_EXCHANGE_RATE() != null) {
		  cell26.setCellValue(record1.getR11_EXCHANGE_RATE().doubleValue());
		  cell26.setCellStyle(numberStyle);
		} else {
		  cell26.setCellValue("");
		  cell26.setCellStyle(textStyle);
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

public byte[] getBDISB1DetailExcel(String filename, String fromdate, String todate,
        String currency, String dtltype, String type, String version) {

    try {
        logger.info("Generating Excel for BDISB1 Details...");
        System.out.println("came to Detail download service");

        // ================= ARCHIVAL HANDLING =================
        if ("ARCHIVAL".equals(type) && version != null) {
            return getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type, version);
        }

        // ================= WORKBOOK & SHEET =================
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("BDISB3Detail");

        BorderStyle border = BorderStyle.THIN;

        // ================= HEADER STYLE =================
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

        CellStyle rightHeaderStyle = workbook.createCellStyle();
        rightHeaderStyle.cloneStyleFrom(headerStyle);
        rightHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

        // ================= DATA STYLES =================
        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setAlignment(HorizontalAlignment.LEFT);
        textStyle.setBorderTop(border);
        textStyle.setBorderBottom(border);
        textStyle.setBorderLeft(border);
        textStyle.setBorderRight(border);

        CellStyle amountStyle = workbook.createCellStyle();
        amountStyle.setAlignment(HorizontalAlignment.RIGHT);
        amountStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        amountStyle.setBorderTop(border);
        amountStyle.setBorderBottom(border);
        amountStyle.setBorderLeft(border);
        amountStyle.setBorderRight(border);

        // ================= HEADER ROW =================
        String[] headers = {
            "FIRST NAME",
            "ACCOUNT BALANCE IN PULA",
            "REPORT LABEL",
            "REPORT ADDL CRITERIA1",
            "REPORT DATE"
        };

        XSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle((i == 0 || i == 1) ? rightHeaderStyle : headerStyle);
            sheet.setColumnWidth(i, 6000);
        }

        // ================= DATA FETCH =================
        Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
        List<BDISB1_Detail_Entity> reportData = BDISB1_Detail_Repo.getdatabydateList(parsedToDate);

        // ================= DATA ROWS =================
        int rowIndex = 1;

        if (reportData != null && !reportData.isEmpty()) {
            for (BDISB1_Detail_Entity item : reportData) {

                XSSFRow row = sheet.createRow(rowIndex++);

                // Column 0 - AGGREGATE BALANCE
                Cell c0 = row.createCell(0);
                c0.setCellValue(item.getFIRST_NAME());
                c0.setCellStyle(textStyle);

                // Column 1 - COMPENSATABLE AMOUNT
                Cell c1 = row.createCell(1);
                c1.setCellValue(item.getACCOUNT_BALANCE_IN_PULA() != null
                        ? item.getACCOUNT_BALANCE_IN_PULA().doubleValue() : 0);
                c1.setCellStyle(amountStyle);

                // Column 2 - REPORT LABEL
                Cell c2 = row.createCell(2);
                c2.setCellValue(item.getReportLable());
                c2.setCellStyle(textStyle);

                // Column 3 - REPORT ADDL CRITERIA 1
                Cell c3 = row.createCell(3);
                c3.setCellValue(item.getReportAddlCriteria1());
                c3.setCellStyle(textStyle);

                // Column 4 - REPORT DATE
                Cell c4 = row.createCell(4);
                c4.setCellValue(item.getReportDate() != null
                        ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
                        : "");
                c4.setCellStyle(textStyle);
            }
        } else {
            logger.info("No data found for BDISB1 — only header written.");
        }

        // ================= WRITE FILE =================
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        logger.info("Excel generation completed with {} row(s).",
                reportData != null ? reportData.size() : 0);

        return bos.toByteArray();

    } catch (Exception e) {
        logger.error("Error generating BDISB1 Excel", e);
        return new byte[0];
    }
}

public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate,
        String currency, String dtltype, String type, String version) {

    try {
        logger.info("Generating Excel for BRRS_BDISB1 ARCHIVAL Details...");
        System.out.println("came to Detail download service");

        // ================= WORKBOOK & SHEET =================
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("BDISB3Detail");

        BorderStyle border = BorderStyle.THIN;

        // ================= HEADER STYLE =================
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

        CellStyle rightHeaderStyle = workbook.createCellStyle();
        rightHeaderStyle.cloneStyleFrom(headerStyle);
        rightHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

        // ================= DATA STYLES =================
        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setAlignment(HorizontalAlignment.LEFT);
        textStyle.setBorderTop(border);
        textStyle.setBorderBottom(border);
        textStyle.setBorderLeft(border);
        textStyle.setBorderRight(border);

        CellStyle amountStyle = workbook.createCellStyle();
        amountStyle.setAlignment(HorizontalAlignment.RIGHT);
        amountStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        amountStyle.setBorderTop(border);
        amountStyle.setBorderBottom(border);
        amountStyle.setBorderLeft(border);
        amountStyle.setBorderRight(border);

        // ================= HEADER ROW =================
        String[] headers = {
            "FIRST NAME",
            "ACCOUNT BALANCE IN PULA",
            "REPORT LABEL",
            "REPORT ADDL CRITERIA1",
            "REPORT DATE"
        };

        XSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle((i == 0 || i == 1) ? rightHeaderStyle : headerStyle);
            sheet.setColumnWidth(i, 6000);
        }

        // ================= DATA FETCH =================
        Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
        List<BDISB1_Archival_Detail_Entity> reportData =
        		BDISB1_Archival_Detail_Repo.getdatabydateList(parsedToDate, version);

        // ================= DATA ROWS =================
        int rowIndex = 1;

        if (reportData != null && !reportData.isEmpty()) {
            for (BDISB1_Archival_Detail_Entity item : reportData) {

                XSSFRow row = sheet.createRow(rowIndex++);

                // Column 0 - AGGREGATE BALANCE
                Cell c0 = row.createCell(0);
                c0.setCellValue(item.getFIRST_NAME());
                c0.setCellStyle(textStyle);

                // Column 1 - COMPENSATABLE AMOUNT
                Cell c1 = row.createCell(1);
                c1.setCellValue(item.getACCOUNT_BALANCE_IN_PULA() != null
                        ? item.getACCOUNT_BALANCE_IN_PULA().doubleValue() : 0);
                c1.setCellStyle(amountStyle);

                // Column 2 - REPORT LABEL
                Cell c2 = row.createCell(2);
                c2.setCellValue(item.getReportLable());
                c2.setCellStyle(textStyle);

                // Column 3 - REPORT ADDL CRITERIA 1
                Cell c3 = row.createCell(3);
                c3.setCellValue(item.getReportAddlCriteria1());
                c3.setCellStyle(textStyle);

                // Column 4 - REPORT DATE
                Cell c4 = row.createCell(4);
                c4.setCellValue(item.getReportDate() != null
                        ? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
                        : "");
                c4.setCellStyle(textStyle);
            }
        } else {
            logger.info("No archival data found for BDISB1 — only header written.");
        }

        // ================= WRITE FILE =================
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        logger.info("ARCHIVAL Excel generation completed with {} row(s).",
                reportData != null ? reportData.size() : 0);

        return bos.toByteArray();

    } catch (Exception e) {
        logger.error("Error generating BDISB1 ARCHIVAL Excel", e);
        return new byte[0];
    }
}




}
