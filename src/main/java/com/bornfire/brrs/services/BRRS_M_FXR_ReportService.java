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

import javax.servlet.http.HttpServletRequest;

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

import java.lang.reflect.Method;
import java.math.BigDecimal;


import com.bornfire.brrs.entities.M_FXR_Summary_Entity1;
import com.bornfire.brrs.entities.M_FXR_Summary_Entity2;
import com.bornfire.brrs.entities.M_FXR_Summary_Entity3;
import com.bornfire.brrs.entities.M_FXR_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_FXR_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.M_FXR_Archival_Summary_Entity3;
import com.bornfire.brrs.entities.M_FXR_Resub_Summary_Entity1;
import com.bornfire.brrs.entities.M_FXR_Resub_Summary_Entity2;
import com.bornfire.brrs.entities.M_FXR_Resub_Summary_Entity3;
//import com.bornfire.brf.entities.M_FXR_Summary_Entity4;
import com.bornfire.brrs.entities.BRRS_M_FXR_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_FXR_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_FXR_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_M_FXR_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_FXR_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_FXR_Archival_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_M_FXR_Resub_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_FXR_Resub_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_FXR_Resub_Summary_Repo3;
//import com.bornfire.brf.entities.BRRS_M_FXR_Summary_Repo4;


@Component
@Service
public class BRRS_M_FXR_ReportService {


	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_FXR_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	BRRS_M_FXR_Summary_Repo1	BRRS_M_FXR_Summary_Repo1;
				
	@Autowired
	BRRS_M_FXR_Summary_Repo2	BRRS_M_FXR_Summary_Repo2;
	@Autowired
	BRRS_M_FXR_Summary_Repo3	BRRS_M_FXR_Summary_Repo3;
	
	@Autowired
	BRRS_M_FXR_Archival_Summary_Repo1	BRRS_M_FXR_Archival_Summary_Repo1;
				
	@Autowired
	BRRS_M_FXR_Archival_Summary_Repo2	BRRS_M_FXR_Archival_Summary_Repo2;
	
	@Autowired
	BRRS_M_FXR_Archival_Summary_Repo3	BRRS_M_FXR_Archival_Summary_Repo3;
	
	@Autowired
	BRRS_M_FXR_Resub_Summary_Repo1	BRRS_M_FXR_Resub_Summary_Repo1;
	
	@Autowired
	BRRS_M_FXR_Resub_Summary_Repo2	BRRS_M_FXR_Resub_Summary_Repo2;
	
	@Autowired
	BRRS_M_FXR_Resub_Summary_Repo3	BRRS_M_FXR_Resub_Summary_Repo3;
	
	
//	@Autowired
//	BRRS_M_FXR_Summary_Repo4	BRRS_M_FXR_Summary_Repo4;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_FXRView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_FXR_Archival_Summary_Entity1> T1Master = new ArrayList<M_FXR_Archival_Summary_Entity1>();
			List<M_FXR_Archival_Summary_Entity2> T2Master = new ArrayList<M_FXR_Archival_Summary_Entity2>();
			List<M_FXR_Archival_Summary_Entity3> T3Master = new ArrayList<M_FXR_Archival_Summary_Entity3>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BRRS_M_FXR_Archival_Summary_Repo1.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = BRRS_M_FXR_Archival_Summary_Repo2.getdatabydateListarchival(dateformat.parse(todate), version);
				T3Master = BRRS_M_FXR_Archival_Summary_Repo3.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary1", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
		} 
		
		
		else if ("RESUB".equalsIgnoreCase(type) && version != null) {
            List<M_FXR_Resub_Summary_Entity1> T1Master = new ArrayList<M_FXR_Resub_Summary_Entity1>();
            List<M_FXR_Resub_Summary_Entity2> T2Master = new ArrayList<M_FXR_Resub_Summary_Entity2>();
            List<M_FXR_Resub_Summary_Entity3> T3Master = new ArrayList<M_FXR_Resub_Summary_Entity3>();
            try {
				Date d1 = dateformat.parse(todate);
            T1Master = BRRS_M_FXR_Resub_Summary_Repo1.getdatabydateListResub(dateformat.parse(todate), version);
             
            T2Master = BRRS_M_FXR_Resub_Summary_Repo2.getdatabydateListResub(dateformat.parse(todate), version);
            
            T3Master = BRRS_M_FXR_Resub_Summary_Repo3.getdatabydateListResub(dateformat.parse(todate), version);
            
            } catch (ParseException e) {
				e.printStackTrace();
			}
                
                mv.addObject("reportsummary1", T1Master);
                mv.addObject("reportsummary2", T2Master);
                mv.addObject("reportsummary3", T3Master);
		}
		
		
		else {
			List<M_FXR_Summary_Entity1> T1Master = new ArrayList<M_FXR_Summary_Entity1>();
			List<M_FXR_Summary_Entity2> T2Master = new ArrayList<M_FXR_Summary_Entity2>();
			List<M_FXR_Summary_Entity3> T3Master = new ArrayList<M_FXR_Summary_Entity3>();
//			List<M_FXR_Summary_Entity4> T4Master = new ArrayList<M_FXR_Summary_Entity4>();
			
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = BRRS_M_FXR_Summary_Repo1.getdatabydateList(dateformat.parse(todate));
				T2Master = BRRS_M_FXR_Summary_Repo2.getdatabydateList(dateformat.parse(todate));
				T3Master = BRRS_M_FXR_Summary_Repo3.getdatabydateList(dateformat.parse(todate));
//				T4Master = BRRS_M_FXR_Summary_Repo4.getdatabydateList(dateformat.parse(todate));
				
				
				
				System.out.println("Size of t1master is :"+T1Master.size());
				
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary1", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
//			mv.addObject("reportsummary4", T4Master);
		}

		
		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_FXR");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}
	
	
	public void updateReport1(M_FXR_Summary_Entity1 updatedEntity) {
	    System.out.println("Came to services1");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_FXR_Summary_Entity1 existing = BRRS_M_FXR_Summary_Repo1.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // 1️⃣ Loop from R11 to R16 and copy fields
	        for (int i = 11; i <= 16; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "net_spot_position", "net_forward_position", "guarantees",
	                                "net_future_inc_or_exp", "net_delta_wei_fx_opt_posi", "other_items",
	                                "net_long_position", "or", "net_short_position" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_FXR_Summary_Entity1.class.getMethod(getterName);
	                    Method setter = M_FXR_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R17 totals
	        String[] totalFields = { "net_long_position", "net_short_position" };
	        for (String field : totalFields) {
	            String getterName = "getR17_" + field;
	            String setterName = "setR17_" + field;

	            try {
	                Method getter = M_FXR_Summary_Entity1.class.getMethod(getterName);
	                Method setter = M_FXR_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

	                Object newValue = getter.invoke(updatedEntity);
	                setter.invoke(existing, newValue);

	            } catch (NoSuchMethodException e) {
	                // Skip if not present
	                continue;
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    BRRS_M_FXR_Summary_Repo1.save(existing);
	}

	public void updateReport2(M_FXR_Summary_Entity2 updatedEntity) {
	    System.out.println("Came to services2");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_FXR_Summary_Entity2 existing = BRRS_M_FXR_Summary_Repo2.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 21; i <= 22; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "long", "short" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_FXR_Summary_Entity2.class.getMethod(getterName);
	                    Method setter = M_FXR_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	            String[] formulaFields = { "total_gross_long_short", "net_position" };
	            for (String field : formulaFields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_FXR_Summary_Entity2.class.getMethod(getterName);
	                    Method setter = M_FXR_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R23 totals
	            String getterName = "getR23_net_position";
	            String setterName = "setR23_net_position";

	            try {
	                Method getter = M_FXR_Summary_Entity2.class.getMethod(getterName);
	                Method setter = M_FXR_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

	                Object newValue = getter.invoke(updatedEntity);
	                setter.invoke(existing, newValue);

	            } catch (NoSuchMethodException e) {
	                // Skip if not present
	                //continue;
	            }
	        

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    BRRS_M_FXR_Summary_Repo2.save(existing);
	}

	public void updateReport3(M_FXR_Summary_Entity3 updatedEntity) {
	    System.out.println("Came to services3");
	    System.out.println("Report Date: " + updatedEntity.getReport_date());

	    M_FXR_Summary_Entity3 existing = BRRS_M_FXR_Summary_Repo3.findById(updatedEntity.getReport_date())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));


	    try {

	            String[] fields = {"greater_net_long_or_short", "abs_value_net_gold_posi", "capital_charge"};

	            for (String field : fields) {
	                String getterName = "getR29_" + field;
	                String setterName = "setR29_" + field;

	                try {
	                    Method getter = M_FXR_Summary_Entity3.class.getMethod(getterName);
	                    Method setter = M_FXR_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }

	            String getterName = "getR30_capital_require";
	            String setterName = "setR30_capital_require";

	            try {
	                Method getter = M_FXR_Summary_Entity3.class.getMethod(getterName);
	                Method setter = M_FXR_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());

	                Object newValue = getter.invoke(updatedEntity);
	                setter.invoke(existing, newValue);

	            } catch (NoSuchMethodException e) {
	                // Skip if not present
	                //continue;
	            }

	            
	    }catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    BRRS_M_FXR_Summary_Repo3.save(existing);
}

	
//	public List<Object> getM_FXRResub() {
//	    List<Object> M_FXRResub = new ArrayList<>();
//	    try {
//	        List<Object> list1 = BRRS_M_FXR_Resub_Summary_Repo1.getM_FXRResub();
//	        List<Object> list2 = BRRS_M_FXR_Resub_Summary_Repo2.getM_FXRResub();
//	        List<Object> list3 = BRRS_M_FXR_Resub_Summary_Repo3.getM_FXRResub();
//
//	        M_FXRResub.addAll(list1);
//	        M_FXRResub.addAll(list2);
//	        M_FXRResub.addAll(list3);
//
//	        System.out.println("Total combined size: " + M_FXRResub.size());
//	    } catch (Exception e) {
//	        System.err.println("Error fetching M_FXR Resub data: " + e.getMessage());
//	        e.printStackTrace();
//	    }
//	    return M_FXRResub;
//	}
//	
//	public void updateReportResub1(M_FXR_Resub_Summary_Entity1 updatedEntity) {
//	    System.out.println("Came to services1");
//	    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//	    M_FXR_Resub_Summary_Entity1 existing = BRRS_M_FXR_Resub_Summary_Repo1.findById(updatedEntity.getReport_date())
//	            .orElseThrow(() -> new RuntimeException(
//	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));
//
//	    try {
//	        // 1️⃣ Loop from R11 to R16 and copy fields
//	        for (int i = 11; i <= 16; i++) {
//	            String prefix = "R" + i + "_";
//
//	            String[] fields = { "currency", "net_spot_position", "net_forward_position", "guarantees",
//	                                "net_future_inc_or_exp", "net_delta_wei_fx_opt_posi", "other_items",
//	                                "net_long_position", "or", "net_short_position" };
//
//	            for (String field : fields) {
//	                String getterName = "get" + prefix + field;
//	                String setterName = "set" + prefix + field;
//
//	                try {
//	                    Method getter = M_FXR_Resub_Summary_Entity1.class.getMethod(getterName);
//	                    Method setter = M_FXR_Resub_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());
//
//	                    Object newValue = getter.invoke(updatedEntity);
//	                    setter.invoke(existing, newValue);
//
//	                } catch (NoSuchMethodException e) {
//	                    // Skip missing fields
//	                    continue;
//	                }
//	            }
//	        }
//
//	        // 2️⃣ Handle R17 totals
//	        String[] totalFields = { "net_long_position", "net_short_position" };
//	        for (String field : totalFields) {
//	            String getterName = "getR17_" + field;
//	            String setterName = "setR17_" + field;
//
//	            try {
//	                Method getter = M_FXR_Resub_Summary_Entity1.class.getMethod(getterName);
//	                Method setter = M_FXR_Resub_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());
//
//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//	                // Skip if not present
//	                continue;
//	            }
//	        }
//
//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//
//	    // 3️⃣ Save updated entity
//	    BRRS_M_FXR_Resub_Summary_Repo1.save(existing);
//	}
//
//	public void updateReportResub2(M_FXR_Resub_Summary_Entity2 updatedEntity) {
//	    System.out.println("Came to services2");
//	    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//	    M_FXR_Resub_Summary_Entity2 existing = BRRS_M_FXR_Resub_Summary_Repo2.findById(updatedEntity.getReport_date())
//	            .orElseThrow(() -> new RuntimeException(
//	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));
//
//	    try {
//	        // 1️⃣ Loop from R11 to R50 and copy fields
//	        for (int i = 21; i <= 22; i++) {
//	            String prefix = "R" + i + "_";
//
//	            String[] fields = { "long", "short" };
//
//	            for (String field : fields) {
//	                String getterName = "get" + prefix + field;
//	                String setterName = "set" + prefix + field;
//
//	                try {
//	                    Method getter = M_FXR_Resub_Summary_Entity2.class.getMethod(getterName);
//	                    Method setter = M_FXR_Resub_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
//
//	                    Object newValue = getter.invoke(updatedEntity);
//	                    setter.invoke(existing, newValue);
//
//	                } catch (NoSuchMethodException e) {
//	                    // Skip missing fields
//	                    continue;
//	                }
//	            }
//	            String[] formulaFields = { "total_gross_long_short", "net_position" };
//	            for (String field : formulaFields) {
//	                String getterName = "get" + prefix + field;
//	                String setterName = "set" + prefix + field;
//
//	                try {
//	                    Method getter = M_FXR_Resub_Summary_Entity2.class.getMethod(getterName);
//	                    Method setter = M_FXR_Resub_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
//
//	                    Object newValue = getter.invoke(updatedEntity);
//	                    setter.invoke(existing, newValue);
//
//	                } catch (NoSuchMethodException e) {
//	                    continue;
//	                }
//	            }
//	        }
//
//	        // 2️⃣ Handle R23 totals
//	            String getterName = "getR23_net_position";
//	            String setterName = "setR23_net_position";
//
//	            try {
//	                Method getter = M_FXR_Resub_Summary_Entity2.class.getMethod(getterName);
//	                Method setter = M_FXR_Resub_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
//
//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//	                // Skip if not present
//	                //continue;
//	            }
//	        
//
//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//
//	    // 3️⃣ Save updated entity
//	    BRRS_M_FXR_Resub_Summary_Repo2.save(existing);
//	}
//
//	public void updateReportResub3(M_FXR_Resub_Summary_Entity3 updatedEntity) {
//	    System.out.println("Came to services3");
//	    System.out.println("Report Date: " + updatedEntity.getReport_date());
//
//	    M_FXR_Resub_Summary_Entity3 existing = BRRS_M_FXR_Resub_Summary_Repo3.findById(updatedEntity.getReport_date())
//	            .orElseThrow(() -> new RuntimeException(
//	                    "Record not found for REPORT_DATE: " + updatedEntity.getReport_date()));
//
//
//	    try {
//
//	            String[] fields = {"greater_net_long_or_short", "abs_value_net_gold_posi", "capital_require", "capital_charge"};
//
//	            for (String field : fields) {
//	                String getterName = "getR29_" + field;
//	                String setterName = "setR29_" + field;
//
//	                try {
//	                    Method getter = M_FXR_Resub_Summary_Entity3.class.getMethod(getterName);
//	                    Method setter = M_FXR_Resub_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());
//
//	                    Object newValue = getter.invoke(updatedEntity);
//	                    setter.invoke(existing, newValue);
//
//	                } catch (NoSuchMethodException e) {
//	                    // Skip missing fields
//	                    continue;
//	                }
//	            }
//	    
//	    }catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//
//	    // 3️⃣ Save updated entity
//	    BRRS_M_FXR_Resub_Summary_Repo3.save(existing);
//}
//



	public byte[] getM_FXRExcel(String filename, String reportId, String fromdate, String todate, String currency,
									 String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		// ARCHIVAL check
		if ("ARCHIVAL".equals(type) && version != null) {
			byte[] ARCHIVALreport = getExcelM_FXRARCHIVAL(filename, reportId, fromdate, 
					todate, currency, dtltype, type,
					version);
			return ARCHIVALreport;
		}


		// Fetch data

		List<M_FXR_Summary_Entity1> dataList1 =BRRS_M_FXR_Summary_Repo1.getdatabydateList(dateformat.parse(todate)) ;

		List<M_FXR_Summary_Entity2> dataList2 =BRRS_M_FXR_Summary_Repo2.getdatabydateList(dateformat.parse(todate)) ;

		List<M_FXR_Summary_Entity3> dataList3 =BRRS_M_FXR_Summary_Repo3.getdatabydateList(dateformat.parse(todate)) ;
		
		if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty()) {
			logger.warn("Service: No data found for M_FXR report. Returning empty result.");
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

			int startRow = 10;

			if (!dataList1.isEmpty() || !dataList2.isEmpty() || !dataList3.isEmpty()) {
			    for (int i = 0; i < dataList1.size(); i++) {
			        M_FXR_Summary_Entity1 record1 = dataList1.get(i);
			        System.out.println("rownumber=" + (startRow + i));
					
				       
			       Row row;
			        Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
			        CellStyle originalStyle;

			     
			        
			        
			        // ===== Row 11 / Col B =====
					row = sheet.getRow(10);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record1.getR11_currency() != null)
//					cell1.setCellValue(record1.getR11_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);


					// ===== R11 / Col C =====
			        
			        cell2 = row.getCell(2);
			        if (cell2 == null) cell2 = row.createCell(2);
			        originalStyle = cell2.getCellStyle();
			        if (record1.getR11_net_spot_position() != null) 
			        cell2.setCellValue(record1.getR11_net_spot_position().doubleValue());
			        else cell2.setCellValue("");
			        cell2.setCellStyle(originalStyle);


					// ===== R11 / Col D =====

			        cell3 = row.getCell(3);
			        if (cell3 == null) cell3 = row.createCell(3);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR11_net_forward_position() != null) 
			        cell3.setCellValue(record1.getR11_net_forward_position().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);
			        
					// ===== R11 / Col E =====

			        cell4 = row.getCell(4);
			        if (cell4 == null) cell4 = row.createCell(4);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR11_guarantees() != null) 
			        cell4.setCellValue(record1.getR11_guarantees().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);
			        
					// ===== R11 / Col F =====

			        cell5 = row.getCell(5);
			        if (cell5 == null) cell5 = row.createCell(5);
			        originalStyle = cell5.getCellStyle();
			        if (record1.getR11_net_future_inc_or_exp() != null) 
			        cell5.setCellValue(record1.getR11_net_future_inc_or_exp().doubleValue());
			        else cell5.setCellValue("");
			        cell5.setCellStyle(originalStyle);
			     
					// ===== R11 / Col G =====

			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record1.getR11_net_delta_wei_fx_opt_posi() != null) 
			        cell6.setCellValue(record1.getR11_net_delta_wei_fx_opt_posi().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);

			        
					// ===== R11 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record1.getR11_other_items() != null) 
			        cell7.setCellValue(record1.getR11_other_items().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);


					// ===== R11 / Col I =====

			        cell8 = row.getCell(8);
			        if (cell8 == null) cell8 = row.createCell(8);
			        originalStyle = cell8.getCellStyle();
			        if (record1.getR11_net_long_position() != null) 
			        cell8.setCellValue(record1.getR11_net_long_position().doubleValue());
			        else cell8.setCellValue("");
			        cell8.setCellStyle(originalStyle);

					// ===== R11 / Col J =====

			        cell9 = row.getCell(9);
			        if (cell9 == null) cell9 = row.createCell(9);
			        originalStyle = cell9.getCellStyle();
			        if (record1.getR11_or() != null) 
			        cell9.setCellValue(record1.getR11_or().doubleValue());
			        else cell9.setCellValue("");
			        cell9.setCellStyle(originalStyle);
			        
					// ===== R11 / Col K =====

			        cell10 = row.getCell(10);
			        if (cell10 == null) cell10 = row.createCell(10);
			        originalStyle = cell10.getCellStyle();
			        if (record1.getR11_net_short_position() != null) 
			        cell10.setCellValue(record1.getR11_net_short_position().doubleValue());
			        else cell10.setCellValue("");
			        cell10.setCellStyle(originalStyle);

			        // ===== R12 / Col B =====
					row = sheet.getRow(11);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record1.getR12_currency() != null)
//					cell1.setCellValue(record1.getR12_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);


			        
					// ===== R12 / Col C =====
			        
			        cell2 = row.getCell(2);
			        if (cell2 == null) cell2 = row.createCell(2);
			        originalStyle = cell2.getCellStyle();
			        if (record1.getR12_net_spot_position() != null) 
			        cell2.setCellValue(record1.getR12_net_spot_position().doubleValue());
			        else cell2.setCellValue("");
			        cell2.setCellStyle(originalStyle);


					// ===== R12 / Col D =====

			        cell3 = row.getCell(3);
			        if (cell3 == null) cell3 = row.createCell(3);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR12_net_forward_position() != null) 
			        cell3.setCellValue(record1.getR12_net_forward_position().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);
			        
					// ===== R12 / Col E =====

			        cell4 = row.getCell(4);
			        if (cell4 == null) cell4 = row.createCell(4);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR12_guarantees() != null) 
			        cell4.setCellValue(record1.getR12_guarantees().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);
			        
					// ===== R12 / Col F =====

			        cell5 = row.getCell(5);
			        if (cell5 == null) cell5 = row.createCell(5);
			        originalStyle = cell5.getCellStyle();
			        if (record1.getR12_net_future_inc_or_exp() != null) 
			        cell5.setCellValue(record1.getR12_net_future_inc_or_exp().doubleValue());
			        else cell5.setCellValue("");
			        cell5.setCellStyle(originalStyle);
			     
					// ===== R12 / Col G =====

			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record1.getR12_net_delta_wei_fx_opt_posi() != null) 
			        cell6.setCellValue(record1.getR12_net_delta_wei_fx_opt_posi().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);

			        
					// ===== R12 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record1.getR12_other_items() != null) 
			        cell7.setCellValue(record1.getR12_other_items().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);


					// ===== R12 / Col I =====

			        cell8 = row.getCell(8);
			        if (cell8 == null) cell8 = row.createCell(8);
			        originalStyle = cell8.getCellStyle();
			        if (record1.getR12_net_long_position() != null) 
			        cell8.setCellValue(record1.getR12_net_long_position().doubleValue());
			        else cell8.setCellValue("");
			        cell8.setCellStyle(originalStyle);

					// ===== R12 / Col J =====

			        cell9 = row.getCell(9);
			        if (cell9 == null) cell9 = row.createCell(9);
			        originalStyle = cell9.getCellStyle();
			        if (record1.getR12_or() != null) 
			        cell9.setCellValue(record1.getR12_or().doubleValue());
			        else cell9.setCellValue("");
			        cell9.setCellStyle(originalStyle);
			        
					// ===== R12 / Col K =====

			        cell10 = row.getCell(10);
			        if (cell10 == null) cell10 = row.createCell(10);
			        originalStyle = cell10.getCellStyle();
			        if (record1.getR12_net_short_position() != null) 
			        cell10.setCellValue(record1.getR12_net_short_position().doubleValue());
			        else cell10.setCellValue("");
			        cell10.setCellStyle(originalStyle);
			        
			        
			        // ===== R13 / Col B =====
					row = sheet.getRow(12);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record1.getR13_currency() != null)
//					cell1.setCellValue(record1.getR13_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

			        


					// ===== R13 / Col C =====
			        
			        cell2 = row.getCell(2);
			        if (cell2 == null) cell2 = row.createCell(2);
			        originalStyle = cell2.getCellStyle();
			        if (record1.getR13_net_spot_position() != null) 
			        cell2.setCellValue(record1.getR13_net_spot_position().doubleValue());
			        else cell2.setCellValue("");
			        cell2.setCellStyle(originalStyle);


					// ===== R13 / Col D =====

			        cell3 = row.getCell(3);
			        if (cell3 == null) cell3 = row.createCell(3);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR13_net_forward_position() != null) 
			        cell3.setCellValue(record1.getR13_net_forward_position().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);
			        
					// ===== R13 / Col E =====

			        cell4 = row.getCell(4);
			        if (cell4 == null) cell4 = row.createCell(4);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR13_guarantees() != null) 
			        cell4.setCellValue(record1.getR13_guarantees().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);
			        
					// ===== R13 / Col F =====

			        cell5 = row.getCell(5);
			        if (cell5 == null) cell5 = row.createCell(5);
			        originalStyle = cell5.getCellStyle();
			        if (record1.getR13_net_future_inc_or_exp() != null) 
			        cell5.setCellValue(record1.getR13_net_future_inc_or_exp().doubleValue());
			        else cell5.setCellValue("");
			        cell5.setCellStyle(originalStyle);
			     
					// ===== R13 / Col G =====

			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record1.getR13_net_delta_wei_fx_opt_posi() != null) 
			        cell6.setCellValue(record1.getR13_net_delta_wei_fx_opt_posi().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);

			        
					// ===== R13 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record1.getR13_other_items() != null) 
			        cell7.setCellValue(record1.getR13_other_items().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);


					// ===== R13 / Col I =====

			        cell8 = row.getCell(8);
			        if (cell8 == null) cell8 = row.createCell(8);
			        originalStyle = cell8.getCellStyle();
			        if (record1.getR13_net_long_position() != null) 
			        cell8.setCellValue(record1.getR13_net_long_position().doubleValue());
			        else cell8.setCellValue("");
			        cell8.setCellStyle(originalStyle);

					// ===== R13 / Col J =====

			        cell9 = row.getCell(9);
			        if (cell9 == null) cell9 = row.createCell(9);
			        originalStyle = cell9.getCellStyle();
			        if (record1.getR13_or() != null) 
			        cell9.setCellValue(record1.getR13_or().doubleValue());
			        else cell9.setCellValue("");
			        cell9.setCellStyle(originalStyle);
			        
					// ===== R13 / Col K =====

			        cell10 = row.getCell(10);
			        if (cell10 == null) cell10 = row.createCell(10);
			        originalStyle = cell10.getCellStyle();
			        if (record1.getR13_net_short_position() != null) 
			        cell10.setCellValue(record1.getR13_net_short_position().doubleValue());
			        else cell10.setCellValue("");
			        cell10.setCellStyle(originalStyle);

			        
			        // ===== R14 / Col B =====
					row = sheet.getRow(13);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record1.getR14_currency() != null)
//					cell1.setCellValue(record1.getR14_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

			        

					// ===== R14 / Col C =====
			        
			        cell2 = row.getCell(2);
			        if (cell2 == null) cell2 = row.createCell(2);
			        originalStyle = cell2.getCellStyle();
			        if (record1.getR14_net_spot_position() != null) 
			        cell2.setCellValue(record1.getR14_net_spot_position().doubleValue());
			        else cell2.setCellValue("");
			        cell2.setCellStyle(originalStyle);


					// ===== R14 / Col D =====

			        cell3 = row.getCell(3);
			        if (cell3 == null) cell3 = row.createCell(3);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR14_net_forward_position() != null) 
			        cell3.setCellValue(record1.getR14_net_forward_position().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);
			        
					// ===== R14 / Col E =====

			        cell4 = row.getCell(4);
			        if (cell4 == null) cell4 = row.createCell(4);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR14_guarantees() != null) 
			        cell4.setCellValue(record1.getR14_guarantees().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);
			        
					// ===== R14 / Col F =====

			        cell5 = row.getCell(5);
			        if (cell5 == null) cell5 = row.createCell(5);
			        originalStyle = cell5.getCellStyle();
			        if (record1.getR14_net_future_inc_or_exp() != null) 
			        cell5.setCellValue(record1.getR14_net_future_inc_or_exp().doubleValue());
			        else cell5.setCellValue("");
			        cell5.setCellStyle(originalStyle);
			     
					// ===== R14 / Col G =====

			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record1.getR14_net_delta_wei_fx_opt_posi() != null) 
			        cell6.setCellValue(record1.getR14_net_delta_wei_fx_opt_posi().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);

			        
					// ===== R14 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record1.getR14_other_items() != null) 
			        cell7.setCellValue(record1.getR14_other_items().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);


					// ===== R14 / Col I =====

			        cell8 = row.getCell(8);
			        if (cell8 == null) cell8 = row.createCell(8);
			        originalStyle = cell8.getCellStyle();
			        if (record1.getR14_net_long_position() != null) 
			        cell8.setCellValue(record1.getR14_net_long_position().doubleValue());
			        else cell8.setCellValue("");
			        cell8.setCellStyle(originalStyle);

					// ===== R14 / Col J =====

			        cell9 = row.getCell(9);
			        if (cell9 == null) cell9 = row.createCell(9);
			        originalStyle = cell9.getCellStyle();
			        if (record1.getR14_or() != null) 
			        cell9.setCellValue(record1.getR14_or().doubleValue());
			        else cell9.setCellValue("");
			        cell9.setCellStyle(originalStyle);
			        
					// ===== R14 / Col K =====

			        cell10 = row.getCell(10);
			        if (cell10 == null) cell10 = row.createCell(10);
			        originalStyle = cell10.getCellStyle();
			        if (record1.getR14_net_short_position() != null) 
			        cell10.setCellValue(record1.getR14_net_short_position().doubleValue());
			        else cell10.setCellValue("");
			        cell10.setCellStyle(originalStyle);

			        	
			        
			        // ===== R15 / Col B =====
					row = sheet.getRow(14);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record1.getR15_currency() != null)
//					cell1.setCellValue(record1.getR15_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

			        
			        

					// ===== R15 / Col C =====
			        
			        cell2 = row.getCell(2);
			        if (cell2 == null) cell2 = row.createCell(2);
			        originalStyle = cell2.getCellStyle();
			        if (record1.getR15_net_spot_position() != null) 
			        cell2.setCellValue(record1.getR15_net_spot_position().doubleValue());
			        else cell2.setCellValue("");
			        cell2.setCellStyle(originalStyle);


					// ===== R15 / Col D =====

			        cell3 = row.getCell(3);
			        if (cell3 == null) cell3 = row.createCell(3);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR15_net_forward_position() != null) 
			        cell3.setCellValue(record1.getR15_net_forward_position().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);
			        
					// ===== R15 / Col E =====

			        cell4 = row.getCell(4);
			        if (cell4 == null) cell4 = row.createCell(4);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR15_guarantees() != null) 
			        cell4.setCellValue(record1.getR15_guarantees().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);
			        
					// ===== R15 / Col F =====

			        cell5 = row.getCell(5);
			        if (cell5 == null) cell5 = row.createCell(5);
			        originalStyle = cell5.getCellStyle();
			        if (record1.getR15_net_future_inc_or_exp() != null) 
			        cell5.setCellValue(record1.getR15_net_future_inc_or_exp().doubleValue());
			        else cell5.setCellValue("");
			        cell5.setCellStyle(originalStyle);
			     
					// ===== R15 / Col G =====

			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record1.getR15_net_delta_wei_fx_opt_posi() != null) 
			        cell6.setCellValue(record1.getR15_net_delta_wei_fx_opt_posi().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);

			        
					// ===== R15 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record1.getR15_other_items() != null) 
			        cell7.setCellValue(record1.getR15_other_items().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);


					// ===== R15 / Col I =====

			        cell8 = row.getCell(8);
			        if (cell8 == null) cell8 = row.createCell(8);
			        originalStyle = cell8.getCellStyle();
			        if (record1.getR15_net_long_position() != null) 
			        cell8.setCellValue(record1.getR15_net_long_position().doubleValue());
			        else cell8.setCellValue("");
			        cell8.setCellStyle(originalStyle);

					// ===== R15 / Col J =====

			        cell9 = row.getCell(9);
			        if (cell9 == null) cell9 = row.createCell(9);
			        originalStyle = cell9.getCellStyle();
			        if (record1.getR15_or() != null) 
			        cell9.setCellValue(record1.getR15_or().doubleValue());
			        else cell9.setCellValue("");
			        cell9.setCellStyle(originalStyle);
			        
					// ===== R15 / Col K =====

			        cell10 = row.getCell(10);
			        if (cell10 == null) cell10 = row.createCell(10);
			        originalStyle = cell10.getCellStyle();
			        if (record1.getR15_net_short_position() != null) 
			        cell10.setCellValue(record1.getR15_net_short_position().doubleValue());
			        else cell10.setCellValue("");
			        cell10.setCellStyle(originalStyle);

			        // ===== R16 / Col B =====
					row = sheet.getRow(15);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record1.getR16_currency() != null)
//					cell1.setCellValue(record1.getR16_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

			       

					// ===== R16 / Col C =====
			        
			        cell2 = row.getCell(2);
			        if (cell2 == null) cell2 = row.createCell(2);
			        originalStyle = cell2.getCellStyle();
			        if (record1.getR16_net_spot_position() != null) 
			        cell2.setCellValue(record1.getR16_net_spot_position().doubleValue());
			        else cell2.setCellValue("");
			        cell2.setCellStyle(originalStyle);


					// ===== R16 / Col D =====

			        cell3 = row.getCell(3);
			        if (cell3 == null) cell3 = row.createCell(3);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR16_net_forward_position() != null) 
			        cell3.setCellValue(record1.getR16_net_forward_position().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);
			        
					// ===== R16 / Col E =====

			        cell4 = row.getCell(4);
			        if (cell4 == null) cell4 = row.createCell(4);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR16_guarantees() != null) 
			        cell4.setCellValue(record1.getR16_guarantees().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);
			        
					// ===== R16 / Col F =====

			        cell5 = row.getCell(5);
			        if (cell5 == null) cell5 = row.createCell(5);
			        originalStyle = cell5.getCellStyle();
			        if (record1.getR16_net_future_inc_or_exp() != null) 
			        cell5.setCellValue(record1.getR16_net_future_inc_or_exp().doubleValue());
			        else cell5.setCellValue("");
			        cell5.setCellStyle(originalStyle);
			     
					// ===== R16 / Col G =====

			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record1.getR16_net_delta_wei_fx_opt_posi() != null) 
			        cell6.setCellValue(record1.getR16_net_delta_wei_fx_opt_posi().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);

			        
					// ===== R16 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record1.getR16_other_items() != null) 
			        cell7.setCellValue(record1.getR16_other_items().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);


					// ===== R16 / Col I =====

			        cell8 = row.getCell(8);
			        if (cell8 == null) cell8 = row.createCell(8);
			        originalStyle = cell8.getCellStyle();
			        if (record1.getR16_net_long_position() != null) 
			        cell8.setCellValue(record1.getR16_net_long_position().doubleValue());
			        else cell8.setCellValue("");
			        cell8.setCellStyle(originalStyle);

					// ===== R16 / Col J =====

			        cell9 = row.getCell(9);
			        if (cell9 == null) cell9 = row.createCell(9);
			        originalStyle = cell9.getCellStyle();
			        if (record1.getR16_or() != null) 
			        cell9.setCellValue(record1.getR16_or().doubleValue());
			        else cell9.setCellValue("");
			        cell9.setCellStyle(originalStyle);
			        
					// ===== R16 / Col K =====

			        cell10 = row.getCell(10);
			        if (cell10 == null) cell10 = row.createCell(10);
			        originalStyle = cell10.getCellStyle();
			        if (record1.getR16_net_short_position() != null) 
			        cell10.setCellValue(record1.getR16_net_short_position().doubleValue());
			        else cell10.setCellValue("");
			        cell10.setCellStyle(originalStyle);

			    }
				        
		   for (int i = 0; i < dataList2.size(); i++) {
		        M_FXR_Summary_Entity2 record2 = dataList2.get(i);
			        System.out.println("rownumber=" + (startRow + i));
					
			        
			        Row row;
			        Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
			        CellStyle originalStyle;


					// ===== R21 / Col G =====
			        row = sheet.getRow(20);
			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record2.getR21_long() != null) 
			        cell6.setCellValue(record2.getR21_long().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);


					// ===== R21 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record2.getR21_short() != null) 
			        cell7.setCellValue(record2.getR21_short().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);
			        

					// ===== R22 / Col G =====
			        row = sheet.getRow(21);
			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record2.getR22_long() != null) 
			        cell6.setCellValue(record2.getR22_long().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);


					// ===== R22 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record2.getR22_short() != null) 
			        cell7.setCellValue(record2.getR22_short().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);
			        
			        
		   }
		   
		   
		   for (int i = 0; i < dataList3.size(); i++) {
		        M_FXR_Summary_Entity3 record3 = dataList3.get(i);
		        System.out.println("rownumber=" + (startRow + i));
		        
		        
		        
		        
		        Row row;
		        Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
		        CellStyle originalStyle;

		     // ===== R30 / Col I =====
		        row = sheet.getRow(29);
		        cell8 = row.getCell(8);
		        if (cell8 == null) cell8 = row.createCell(8);
		        originalStyle = cell8.getCellStyle();

		        if (record3.getR30_capital_require() != null)
		        	cell8.setCellValue(record3.getR30_capital_require().doubleValue());
		        else {
		            cell8.setCellValue("");
		        }

		        // Keep the same style (make sure your template cell is formatted as Percentage)
		        cell8.setCellStyle(originalStyle);
		    
		        
		        
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

	public List<Object> getM_FXRArchival() {
		List<Object> M_FXRArchivallist = new ArrayList<>();
//		List<Object> M_FXRArchivallist2 = new ArrayList<>();
//		List<Object> M_FXRArchivallist3 = new ArrayList<>();
		try {
			M_FXRArchivallist = BRRS_M_FXR_Archival_Summary_Repo1.getM_FXRarchival();
			M_FXRArchivallist = BRRS_M_FXR_Archival_Summary_Repo2.getM_FXRarchival();
			M_FXRArchivallist = BRRS_M_FXR_Archival_Summary_Repo3.getM_FXRarchival();
			
			System.out.println("countser" + M_FXRArchivallist.size());
//			System.out.println("countser" + M_FXRArchivallist.size());
//			System.out.println("countser" + M_FXRArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_FXR Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_FXRArchivallist;
	}


	public byte[] getExcelM_FXRARCHIVAL(String filename, String reportId, String fromdate,
			String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
			List<M_FXR_Archival_Summary_Entity1> dataList1 = BRRS_M_FXR_Archival_Summary_Repo1
					.getdatabydateListarchival(dateformat.parse(todate), version);
			List<M_FXR_Archival_Summary_Entity2> dataList2 = BRRS_M_FXR_Archival_Summary_Repo2
					.getdatabydateListarchival(dateformat.parse(todate), version);
			List<M_FXR_Archival_Summary_Entity3> dataList3 = BRRS_M_FXR_Archival_Summary_Repo3
					.getdatabydateListarchival(dateformat.parse(todate), version);

			
		
		if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty()) {
			logger.warn("Service: No data found for M_FXR report. Returning empty result.");
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

			if (!dataList1.isEmpty() || !dataList2.isEmpty() || !dataList3.isEmpty()) {
			    for (int i = 0; i < dataList1.size(); i++) {
			        M_FXR_Archival_Summary_Entity1 record1 = dataList1.get(i);
			        System.out.println("rownumber=" + (startRow + i));
					
			        
			        
			        Row row;

			        Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
			        CellStyle originalStyle;

			        // ===== Row 11 / Col B =====
					row = sheet.getRow(10);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record1.getR11_currency() != null)
//					cell1.setCellValue(record1.getR11_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);


					// ===== R11 / Col C =====
			        
			        cell2 = row.getCell(2);
			        if (cell2 == null) cell2 = row.createCell(2);
			        originalStyle = cell2.getCellStyle();
			        if (record1.getR11_net_spot_position() != null) 
			        cell2.setCellValue(record1.getR11_net_spot_position().doubleValue());
			        else cell2.setCellValue("");
			        cell2.setCellStyle(originalStyle);


					// ===== R11 / Col D =====

			        cell3 = row.getCell(3);
			        if (cell3 == null) cell3 = row.createCell(3);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR11_net_forward_position() != null) 
			        cell3.setCellValue(record1.getR11_net_forward_position().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);
			        
					// ===== R11 / Col E =====

			        cell4 = row.getCell(4);
			        if (cell4 == null) cell4 = row.createCell(4);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR11_guarantees() != null) 
			        cell4.setCellValue(record1.getR11_guarantees().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);
			        
					// ===== R11 / Col F =====

			        cell5 = row.getCell(5);
			        if (cell5 == null) cell5 = row.createCell(5);
			        originalStyle = cell5.getCellStyle();
			        if (record1.getR11_net_future_inc_or_exp() != null) 
			        cell5.setCellValue(record1.getR11_net_future_inc_or_exp().doubleValue());
			        else cell5.setCellValue("");
			        cell5.setCellStyle(originalStyle);
			     
					// ===== R11 / Col G =====

			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record1.getR11_net_delta_wei_fx_opt_posi() != null) 
			        cell6.setCellValue(record1.getR11_net_delta_wei_fx_opt_posi().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);

			        
					// ===== R11 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record1.getR11_other_items() != null) 
			        cell7.setCellValue(record1.getR11_other_items().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);


					// ===== R11 / Col I =====

			        cell8 = row.getCell(8);
			        if (cell8 == null) cell8 = row.createCell(8);
			        originalStyle = cell8.getCellStyle();
			        if (record1.getR11_net_long_position() != null) 
			        cell8.setCellValue(record1.getR11_net_long_position().doubleValue());
			        else cell8.setCellValue("");
			        cell8.setCellStyle(originalStyle);

					// ===== R11 / Col J =====

			        cell9 = row.getCell(9);
			        if (cell9 == null) cell9 = row.createCell(9);
			        originalStyle = cell9.getCellStyle();
			        if (record1.getR11_or() != null) 
			        cell9.setCellValue(record1.getR11_or().doubleValue());
			        else cell9.setCellValue("");
			        cell9.setCellStyle(originalStyle);
			        
					// ===== R11 / Col K =====

			        cell10 = row.getCell(10);
			        if (cell10 == null) cell10 = row.createCell(10);
			        originalStyle = cell10.getCellStyle();
			        if (record1.getR11_net_short_position() != null) 
			        cell10.setCellValue(record1.getR11_net_short_position().doubleValue());
			        else cell10.setCellValue("");
			        cell10.setCellStyle(originalStyle);

			        // ===== R12 / Col B =====
					row = sheet.getRow(11);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record1.getR12_currency() != null)
//					cell1.setCellValue(record1.getR12_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);


			        
					// ===== R12 / Col C =====
			        
			        cell2 = row.getCell(2);
			        if (cell2 == null) cell2 = row.createCell(2);
			        originalStyle = cell2.getCellStyle();
			        if (record1.getR12_net_spot_position() != null) 
			        cell2.setCellValue(record1.getR12_net_spot_position().doubleValue());
			        else cell2.setCellValue("");
			        cell2.setCellStyle(originalStyle);


					// ===== R12 / Col D =====

			        cell3 = row.getCell(3);
			        if (cell3 == null) cell3 = row.createCell(3);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR12_net_forward_position() != null) 
			        cell3.setCellValue(record1.getR12_net_forward_position().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);
			        
					// ===== R12 / Col E =====

			        cell4 = row.getCell(4);
			        if (cell4 == null) cell4 = row.createCell(4);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR12_guarantees() != null) 
			        cell4.setCellValue(record1.getR12_guarantees().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);
			        
					// ===== R12 / Col F =====

			        cell5 = row.getCell(5);
			        if (cell5 == null) cell5 = row.createCell(5);
			        originalStyle = cell5.getCellStyle();
			        if (record1.getR12_net_future_inc_or_exp() != null) 
			        cell5.setCellValue(record1.getR12_net_future_inc_or_exp().doubleValue());
			        else cell5.setCellValue("");
			        cell5.setCellStyle(originalStyle);
			     
					// ===== R12 / Col G =====

			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record1.getR12_net_delta_wei_fx_opt_posi() != null) 
			        cell6.setCellValue(record1.getR12_net_delta_wei_fx_opt_posi().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);

			        
					// ===== R12 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record1.getR12_other_items() != null) 
			        cell7.setCellValue(record1.getR12_other_items().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);


					// ===== R12 / Col I =====

			        cell8 = row.getCell(8);
			        if (cell8 == null) cell8 = row.createCell(8);
			        originalStyle = cell8.getCellStyle();
			        if (record1.getR12_net_long_position() != null) 
			        cell8.setCellValue(record1.getR12_net_long_position().doubleValue());
			        else cell8.setCellValue("");
			        cell8.setCellStyle(originalStyle);

					// ===== R12 / Col J =====

			        cell9 = row.getCell(9);
			        if (cell9 == null) cell9 = row.createCell(9);
			        originalStyle = cell9.getCellStyle();
			        if (record1.getR12_or() != null) 
			        cell9.setCellValue(record1.getR12_or().doubleValue());
			        else cell9.setCellValue("");
			        cell9.setCellStyle(originalStyle);
			        
					// ===== R12 / Col K =====

			        cell10 = row.getCell(10);
			        if (cell10 == null) cell10 = row.createCell(10);
			        originalStyle = cell10.getCellStyle();
			        if (record1.getR12_net_short_position() != null) 
			        cell10.setCellValue(record1.getR12_net_short_position().doubleValue());
			        else cell10.setCellValue("");
			        cell10.setCellStyle(originalStyle);
			        
			        
			        // ===== R13 / Col B =====
					row = sheet.getRow(12);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record1.getR13_currency() != null)
//					cell1.setCellValue(record1.getR13_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

			        


					// ===== R13 / Col C =====
			        
			        cell2 = row.getCell(2);
			        if (cell2 == null) cell2 = row.createCell(2);
			        originalStyle = cell2.getCellStyle();
			        if (record1.getR13_net_spot_position() != null) 
			        cell2.setCellValue(record1.getR13_net_spot_position().doubleValue());
			        else cell2.setCellValue("");
			        cell2.setCellStyle(originalStyle);


					// ===== R13 / Col D =====

			        cell3 = row.getCell(3);
			        if (cell3 == null) cell3 = row.createCell(3);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR13_net_forward_position() != null) 
			        cell3.setCellValue(record1.getR13_net_forward_position().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);
			        
					// ===== R13 / Col E =====

			        cell4 = row.getCell(4);
			        if (cell4 == null) cell4 = row.createCell(4);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR13_guarantees() != null) 
			        cell4.setCellValue(record1.getR13_guarantees().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);
			        
					// ===== R13 / Col F =====

			        cell5 = row.getCell(5);
			        if (cell5 == null) cell5 = row.createCell(5);
			        originalStyle = cell5.getCellStyle();
			        if (record1.getR13_net_future_inc_or_exp() != null) 
			        cell5.setCellValue(record1.getR13_net_future_inc_or_exp().doubleValue());
			        else cell5.setCellValue("");
			        cell5.setCellStyle(originalStyle);
			     
					// ===== R13 / Col G =====

			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record1.getR13_net_delta_wei_fx_opt_posi() != null) 
			        cell6.setCellValue(record1.getR13_net_delta_wei_fx_opt_posi().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);

			        
					// ===== R13 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record1.getR13_other_items() != null) 
			        cell7.setCellValue(record1.getR13_other_items().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);


					// ===== R13 / Col I =====

			        cell8 = row.getCell(8);
			        if (cell8 == null) cell8 = row.createCell(8);
			        originalStyle = cell8.getCellStyle();
			        if (record1.getR13_net_long_position() != null) 
			        cell8.setCellValue(record1.getR13_net_long_position().doubleValue());
			        else cell8.setCellValue("");
			        cell8.setCellStyle(originalStyle);

					// ===== R13 / Col J =====

			        cell9 = row.getCell(9);
			        if (cell9 == null) cell9 = row.createCell(9);
			        originalStyle = cell9.getCellStyle();
			        if (record1.getR13_or() != null) 
			        cell9.setCellValue(record1.getR13_or().doubleValue());
			        else cell9.setCellValue("");
			        cell9.setCellStyle(originalStyle);
			        
					// ===== R13 / Col K =====

			        cell10 = row.getCell(10);
			        if (cell10 == null) cell10 = row.createCell(10);
			        originalStyle = cell10.getCellStyle();
			        if (record1.getR13_net_short_position() != null) 
			        cell10.setCellValue(record1.getR13_net_short_position().doubleValue());
			        else cell10.setCellValue("");
			        cell10.setCellStyle(originalStyle);

			        
			        // ===== R14 / Col B =====
					row = sheet.getRow(13);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record1.getR14_currency() != null)
//					cell1.setCellValue(record1.getR14_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

			        

					// ===== R14 / Col C =====
			        
			        cell2 = row.getCell(2);
			        if (cell2 == null) cell2 = row.createCell(2);
			        originalStyle = cell2.getCellStyle();
			        if (record1.getR14_net_spot_position() != null) 
			        cell2.setCellValue(record1.getR14_net_spot_position().doubleValue());
			        else cell2.setCellValue("");
			        cell2.setCellStyle(originalStyle);


					// ===== R14 / Col D =====

			        cell3 = row.getCell(3);
			        if (cell3 == null) cell3 = row.createCell(3);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR14_net_forward_position() != null) 
			        cell3.setCellValue(record1.getR14_net_forward_position().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);
			        
					// ===== R14 / Col E =====

			        cell4 = row.getCell(4);
			        if (cell4 == null) cell4 = row.createCell(4);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR14_guarantees() != null) 
			        cell4.setCellValue(record1.getR14_guarantees().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);
			        
					// ===== R14 / Col F =====

			        cell5 = row.getCell(5);
			        if (cell5 == null) cell5 = row.createCell(5);
			        originalStyle = cell5.getCellStyle();
			        if (record1.getR14_net_future_inc_or_exp() != null) 
			        cell5.setCellValue(record1.getR14_net_future_inc_or_exp().doubleValue());
			        else cell5.setCellValue("");
			        cell5.setCellStyle(originalStyle);
			     
					// ===== R14 / Col G =====

			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record1.getR14_net_delta_wei_fx_opt_posi() != null) 
			        cell6.setCellValue(record1.getR14_net_delta_wei_fx_opt_posi().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);

			        
					// ===== R14 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record1.getR14_other_items() != null) 
			        cell7.setCellValue(record1.getR14_other_items().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);


					// ===== R14 / Col I =====

			        cell8 = row.getCell(8);
			        if (cell8 == null) cell8 = row.createCell(8);
			        originalStyle = cell8.getCellStyle();
			        if (record1.getR14_net_long_position() != null) 
			        cell8.setCellValue(record1.getR14_net_long_position().doubleValue());
			        else cell8.setCellValue("");
			        cell8.setCellStyle(originalStyle);

					// ===== R14 / Col J =====

			        cell9 = row.getCell(9);
			        if (cell9 == null) cell9 = row.createCell(9);
			        originalStyle = cell9.getCellStyle();
			        if (record1.getR14_or() != null) 
			        cell9.setCellValue(record1.getR14_or().doubleValue());
			        else cell9.setCellValue("");
			        cell9.setCellStyle(originalStyle);
			        
					// ===== R14 / Col K =====

			        cell10 = row.getCell(10);
			        if (cell10 == null) cell10 = row.createCell(10);
			        originalStyle = cell10.getCellStyle();
			        if (record1.getR14_net_short_position() != null) 
			        cell10.setCellValue(record1.getR14_net_short_position().doubleValue());
			        else cell10.setCellValue("");
			        cell10.setCellStyle(originalStyle);

			        	
			        
			        // ===== R15 / Col B =====
					row = sheet.getRow(14);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record1.getR15_currency() != null)
//					cell1.setCellValue(record1.getR15_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

			        
			        

					// ===== R15 / Col C =====
			        
			        cell2 = row.getCell(2);
			        if (cell2 == null) cell2 = row.createCell(2);
			        originalStyle = cell2.getCellStyle();
			        if (record1.getR15_net_spot_position() != null) 
			        cell2.setCellValue(record1.getR15_net_spot_position().doubleValue());
			        else cell2.setCellValue("");
			        cell2.setCellStyle(originalStyle);


					// ===== R15 / Col D =====

			        cell3 = row.getCell(3);
			        if (cell3 == null) cell3 = row.createCell(3);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR15_net_forward_position() != null) 
			        cell3.setCellValue(record1.getR15_net_forward_position().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);
			        
					// ===== R15 / Col E =====

			        cell4 = row.getCell(4);
			        if (cell4 == null) cell4 = row.createCell(4);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR15_guarantees() != null) 
			        cell4.setCellValue(record1.getR15_guarantees().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);
			        
					// ===== R15 / Col F =====

			        cell5 = row.getCell(5);
			        if (cell5 == null) cell5 = row.createCell(5);
			        originalStyle = cell5.getCellStyle();
			        if (record1.getR15_net_future_inc_or_exp() != null) 
			        cell5.setCellValue(record1.getR15_net_future_inc_or_exp().doubleValue());
			        else cell5.setCellValue("");
			        cell5.setCellStyle(originalStyle);
			     
					// ===== R15 / Col G =====

			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record1.getR15_net_delta_wei_fx_opt_posi() != null) 
			        cell6.setCellValue(record1.getR15_net_delta_wei_fx_opt_posi().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);

			        
					// ===== R15 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record1.getR15_other_items() != null) 
			        cell7.setCellValue(record1.getR15_other_items().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);


					// ===== R15 / Col I =====

			        cell8 = row.getCell(8);
			        if (cell8 == null) cell8 = row.createCell(8);
			        originalStyle = cell8.getCellStyle();
			        if (record1.getR15_net_long_position() != null) 
			        cell8.setCellValue(record1.getR15_net_long_position().doubleValue());
			        else cell8.setCellValue("");
			        cell8.setCellStyle(originalStyle);

					// ===== R15 / Col J =====

			        cell9 = row.getCell(9);
			        if (cell9 == null) cell9 = row.createCell(9);
			        originalStyle = cell9.getCellStyle();
			        if (record1.getR15_or() != null) 
			        cell9.setCellValue(record1.getR15_or().doubleValue());
			        else cell9.setCellValue("");
			        cell9.setCellStyle(originalStyle);
			        
					// ===== R15 / Col K =====

			        cell10 = row.getCell(10);
			        if (cell10 == null) cell10 = row.createCell(10);
			        originalStyle = cell10.getCellStyle();
			        if (record1.getR15_net_short_position() != null) 
			        cell10.setCellValue(record1.getR15_net_short_position().doubleValue());
			        else cell10.setCellValue("");
			        cell10.setCellStyle(originalStyle);

			        // ===== R16 / Col B =====
					row = sheet.getRow(15);
//					cell1 = row.getCell(1);
//					if (cell1 == null) cell1 = row.createCell(1);
//					originalStyle = cell1.getCellStyle();
//			// ✅ Handle String value 
//					if (record1.getR16_currency() != null)
//					cell1.setCellValue(record1.getR16_currency()); // String directly 
//					else cell1.setCellValue(""); 
//					cell1.setCellStyle(originalStyle);

			       

					// ===== R16 / Col C =====
			        
			        cell2 = row.getCell(2);
			        if (cell2 == null) cell2 = row.createCell(2);
			        originalStyle = cell2.getCellStyle();
			        if (record1.getR16_net_spot_position() != null) 
			        cell2.setCellValue(record1.getR16_net_spot_position().doubleValue());
			        else cell2.setCellValue("");
			        cell2.setCellStyle(originalStyle);


					// ===== R16 / Col D =====

			        cell3 = row.getCell(3);
			        if (cell3 == null) cell3 = row.createCell(3);
			        originalStyle = cell3.getCellStyle();
			        if (record1.getR16_net_forward_position() != null) 
			        cell3.setCellValue(record1.getR16_net_forward_position().doubleValue());
			        else cell3.setCellValue("");
			        cell3.setCellStyle(originalStyle);
			        
					// ===== R16 / Col E =====

			        cell4 = row.getCell(4);
			        if (cell4 == null) cell4 = row.createCell(4);
			        originalStyle = cell4.getCellStyle();
			        if (record1.getR16_guarantees() != null) 
			        cell4.setCellValue(record1.getR16_guarantees().doubleValue());
			        else cell4.setCellValue("");
			        cell4.setCellStyle(originalStyle);
			        
					// ===== R16 / Col F =====

			        cell5 = row.getCell(5);
			        if (cell5 == null) cell5 = row.createCell(5);
			        originalStyle = cell5.getCellStyle();
			        if (record1.getR16_net_future_inc_or_exp() != null) 
			        cell5.setCellValue(record1.getR16_net_future_inc_or_exp().doubleValue());
			        else cell5.setCellValue("");
			        cell5.setCellStyle(originalStyle);
			     
					// ===== R16 / Col G =====

			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record1.getR16_net_delta_wei_fx_opt_posi() != null) 
			        cell6.setCellValue(record1.getR16_net_delta_wei_fx_opt_posi().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);

			        
					// ===== R16 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record1.getR16_other_items() != null) 
			        cell7.setCellValue(record1.getR16_other_items().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);


					// ===== R16 / Col I =====

			        cell8 = row.getCell(8);
			        if (cell8 == null) cell8 = row.createCell(8);
			        originalStyle = cell8.getCellStyle();
			        if (record1.getR16_net_long_position() != null) 
			        cell8.setCellValue(record1.getR16_net_long_position().doubleValue());
			        else cell8.setCellValue("");
			        cell8.setCellStyle(originalStyle);

					// ===== R16 / Col J =====

			        cell9 = row.getCell(9);
			        if (cell9 == null) cell9 = row.createCell(9);
			        originalStyle = cell9.getCellStyle();
			        if (record1.getR16_or() != null) 
			        cell9.setCellValue(record1.getR16_or().doubleValue());
			        else cell9.setCellValue("");
			        cell9.setCellStyle(originalStyle);
			        
					// ===== R16 / Col K =====

			        cell10 = row.getCell(10);
			        if (cell10 == null) cell10 = row.createCell(10);
			        originalStyle = cell10.getCellStyle();
			        if (record1.getR16_net_short_position() != null) 
			        cell10.setCellValue(record1.getR16_net_short_position().doubleValue());
			        else cell10.setCellValue("");
			        cell10.setCellStyle(originalStyle);

			    }
				        
		   for (int i = 0; i < dataList2.size(); i++) {
		        M_FXR_Archival_Summary_Entity2 record2 = dataList2.get(i);
			        System.out.println("rownumber=" + (startRow + i));
					
			        
			        Row row;
			        Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
			        CellStyle originalStyle;


					// ===== R21 / Col G =====
			        row = sheet.getRow(20);
			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record2.getR21_long() != null) 
			        cell6.setCellValue(record2.getR21_long().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);


					// ===== R21 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record2.getR21_short() != null) 
			        cell7.setCellValue(record2.getR21_short().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);
			        

					// ===== R22 / Col G =====
			        row = sheet.getRow(21);
			        cell6 = row.getCell(6);
			        if (cell6 == null) cell6 = row.createCell(6);
			        originalStyle = cell6.getCellStyle();
			        if (record2.getR22_long() != null) 
			        cell6.setCellValue(record2.getR22_long().doubleValue());
			        else cell6.setCellValue("");
			        cell6.setCellStyle(originalStyle);


					// ===== R22 / Col H =====

			        cell7 = row.getCell(7);
			        if (cell7 == null) cell7 = row.createCell(7);
			        originalStyle = cell7.getCellStyle();
			        if (record2.getR22_short() != null) 
			        cell7.setCellValue(record2.getR22_short().doubleValue());
			        else cell7.setCellValue("");
			        cell7.setCellStyle(originalStyle);
			   
			        
		   }
		   
		   
		   for (int i = 0; i < dataList3.size(); i++) {
		        M_FXR_Archival_Summary_Entity3 record3 = dataList3.get(i);
		        System.out.println("rownumber=" + (startRow + i));
		        
		        Row row;
		        Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
		        CellStyle originalStyle;

		     // ===== R30 / Col I =====
		        row = sheet.getRow(29);
		        cell8 = row.getCell(8);
		        if (cell8 == null) cell8 = row.createCell(8);
		        originalStyle = cell8.getCellStyle();

		        if (record3.getR30_capital_require() != null)
		        	cell8.setCellValue(record3.getR30_capital_require().doubleValue());
		        else {
		            cell8.setCellValue("");
		        }

		        // Keep the same style (make sure your template cell is formatted as Percentage)
		        cell8.setCellStyle(originalStyle);
		    

		    
		        
		   }
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {
				
			}
		
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
}}}


	
	

