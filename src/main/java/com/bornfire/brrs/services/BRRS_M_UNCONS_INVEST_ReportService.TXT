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

import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Entity3;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Entity4;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Archival_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Archival_Summary_Repo4;
import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_M_UNCONS_INVEST_Summary_Repo4;
import com.bornfire.brrs.entities.M_LA1_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_LA1_Summary_Entity;
import com.bornfire.brrs.entities.M_SFINP2_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_SFINP2_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_SFINP2_Detail_Entity;
import com.bornfire.brrs.entities.M_SFINP2_Summary_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Archival_Summary_Entity3;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Archival_Summary_Entity4;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity1;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity2;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity3;
import com.bornfire.brrs.entities.M_UNCONS_INVEST_Summary_Entity4;

import java.lang.reflect.Method;


@Component
@Service
public class BRRS_M_UNCONS_INVEST_ReportService {


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
	BRRS_M_UNCONS_INVEST_Summary_Repo1	M_UNCONS_INVEST_Summary_Repo1;
	
	@Autowired
	BRRS_M_UNCONS_INVEST_Summary_Repo2	M_UNCONS_INVEST_Summary_Repo2;
	
	@Autowired
	BRRS_M_UNCONS_INVEST_Summary_Repo3	M_UNCONS_INVEST_Summary_Repo3;
	
	@Autowired
	BRRS_M_UNCONS_INVEST_Summary_Repo4	M_UNCONS_INVEST_Summary_Repo4;
	
	@Autowired
	BRRS_M_UNCONS_INVEST_Archival_Summary_Repo1	M_UNCONS_INVEST_Archival_Summary_Repo1;
	
	@Autowired
	BRRS_M_UNCONS_INVEST_Archival_Summary_Repo2	M_UNCONS_INVEST_Archival_Summary_Repo2;
	
	@Autowired
	BRRS_M_UNCONS_INVEST_Archival_Summary_Repo3	M_UNCONS_INVEST_Archival_Summary_Repo3;
	
	@Autowired
	BRRS_M_UNCONS_INVEST_Archival_Summary_Repo4	M_UNCONS_INVEST_Archival_Summary_Repo4;
				
	
	
	@Autowired
	BRRS_M_SFINP2_Summary_Repo M_SFINP2_Summary_Repo;

	@Autowired
	BRRS_M_SFINP2_Archival_Detail_Repo M_SFINP2_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SFINP2_Archival_Summary_Repo M_SFINP2_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_UNCONS_INVESTView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_UNCONS_INVEST_Archival_Summary_Entity1> T1Master = new ArrayList<M_UNCONS_INVEST_Archival_Summary_Entity1>();
			List<M_UNCONS_INVEST_Archival_Summary_Entity2> T2Master = new ArrayList<M_UNCONS_INVEST_Archival_Summary_Entity2>();
			List<M_UNCONS_INVEST_Archival_Summary_Entity3> T3Master = new ArrayList<M_UNCONS_INVEST_Archival_Summary_Entity3>();
			List<M_UNCONS_INVEST_Archival_Summary_Entity4> T4Master = new ArrayList<M_UNCONS_INVEST_Archival_Summary_Entity4>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = M_UNCONS_INVEST_Archival_Summary_Repo1.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = M_UNCONS_INVEST_Archival_Summary_Repo2.getdatabydateListarchival(dateformat.parse(todate), version);
				T3Master = M_UNCONS_INVEST_Archival_Summary_Repo3.getdatabydateListarchival(dateformat.parse(todate), version);
				T4Master = M_UNCONS_INVEST_Archival_Summary_Repo4.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
			mv.addObject("reportsummary4", T4Master);
		} else {
			List<M_UNCONS_INVEST_Summary_Entity1> T1Master = new ArrayList<M_UNCONS_INVEST_Summary_Entity1>();
			List<M_UNCONS_INVEST_Summary_Entity2> T2Master = new ArrayList<M_UNCONS_INVEST_Summary_Entity2>();
			List<M_UNCONS_INVEST_Summary_Entity3> T3Master = new ArrayList<M_UNCONS_INVEST_Summary_Entity3>();
			List<M_UNCONS_INVEST_Summary_Entity4> T4Master = new ArrayList<M_UNCONS_INVEST_Summary_Entity4>();
			
			
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = M_UNCONS_INVEST_Summary_Repo1.getdatabydateList(dateformat.parse(todate));
				T2Master = M_UNCONS_INVEST_Summary_Repo2.getdatabydateList(dateformat.parse(todate));
				T3Master = M_UNCONS_INVEST_Summary_Repo3.getdatabydateList(dateformat.parse(todate));
				T4Master = M_UNCONS_INVEST_Summary_Repo4.getdatabydateList(dateformat.parse(todate));
				
				
				
				
				System.out.println("Size of t1master is :"+T1Master.size());
				
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
			mv.addObject("reportsummary4", T4Master);
			
		}

		
		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_UNCONS_INVEST");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	
	public void updateReport(M_UNCONS_INVEST_Summary_Entity1 updatedEntity) {
	    System.out.println("Came to services 1");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_UNCONS_INVEST_Summary_Entity1 existing = M_UNCONS_INVEST_Summary_Repo1.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R15 and copy fields
	        for (int i = 11; i <= 15; i++) {
	            String prefix = "R" + i + "_";
	            

	            String[] fields = {  "AMOUNT", "PERCENT_OF_CET1_HOLDING", "PERCENT_OF_ADDITIONAL_TIER_1_HOLDING",
	                                "PERCENT_OF_TIER_2_HOLDING"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_UNCONS_INVEST_Summary_Entity1.class.getMethod(getterName);
	                    Method setter = M_UNCONS_INVEST_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);
	                    

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R15 totals
	        String[] totalFields = { "AMOUNT", "PERCENT_OF_CET1_HOLDING","PERCENT_OF_ADDITIONAL_TIER_1_HOLDING","PERCENT_OF_TIER_2_HOLDING" };
	        
	        for (String field : totalFields) {
	            String getterName = "getR15_" + field;
	            String setterName = "setR15_" + field;

	            try {
	                Method getter = M_UNCONS_INVEST_Summary_Entity1.class.getMethod(getterName);
	                Method setter = M_UNCONS_INVEST_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

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
	    System.out.println("Testing 1");
	    // 3️⃣ Save updated entity
	    M_UNCONS_INVEST_Summary_Repo1.save(existing);
	   
	}
	
	public void updateArchivalReport(M_UNCONS_INVEST_Archival_Summary_Entity1 updatedEntity) {
	    System.out.println("Came to services 1");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_UNCONS_INVEST_Archival_Summary_Entity1 existing = M_UNCONS_INVEST_Archival_Summary_Repo1.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R15 and copy fields
	        for (int i = 11; i <= 15; i++) {
	            String prefix = "R" + i + "_";
	            

	            String[] fields = {  "AMOUNT", "PERCENT_OF_CET1_HOLDING", "PERCENT_OF_ADDITIONAL_TIER_1_HOLDING",
	                                "PERCENT_OF_TIER_2_HOLDING"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_UNCONS_INVEST_Archival_Summary_Entity1.class.getMethod(getterName);
	                    Method setter = M_UNCONS_INVEST_Archival_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);
	                    

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R15 totals
	        String[] totalFields = { "AMOUNT", "PERCENT_OF_CET1_HOLDING","PERCENT_OF_ADDITIONAL_TIER_1_HOLDING","PERCENT_OF_TIER_2_HOLDING" };
	        
	        for (String field : totalFields) {
	            String getterName = "getR15_" + field;
	            String setterName = "setR15_" + field;

	            try {
	                Method getter = M_UNCONS_INVEST_Archival_Summary_Entity1.class.getMethod(getterName);
	                Method setter = M_UNCONS_INVEST_Archival_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

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
	    System.out.println("Testing 1");
	    // 3️⃣ Save updated entity
	    M_UNCONS_INVEST_Archival_Summary_Repo1.save(existing);
	   
	}
	
	public void updateReport2(M_UNCONS_INVEST_Summary_Entity2 updatedEntity) {
	    System.out.println("Came to services 2");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_UNCONS_INVEST_Summary_Entity2 existing = M_UNCONS_INVEST_Summary_Repo2.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 22; i <= 24; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "ACCUULATED_EQUITY_INTEREST_5", "ASSETS", "LIABILITIES",
	                                "REVENUE", "PROFIT_OR_LOSS", "UNREG_SHARE_OF_LOSS" ,"CUMULATIVE_UNREG_SHARE_OF_LOSS" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_UNCONS_INVEST_Summary_Entity2.class.getMethod(getterName);
	                    Method setter = M_UNCONS_INVEST_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	    
	    // 3️⃣ Save updated entity
	    M_UNCONS_INVEST_Summary_Repo2.save(existing);
	}
	
	public void updateArchivalReport2(M_UNCONS_INVEST_Archival_Summary_Entity2 updatedEntity) {
	    System.out.println("Came to services 2");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_UNCONS_INVEST_Archival_Summary_Entity2 existing = M_UNCONS_INVEST_Archival_Summary_Repo2.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 22; i <= 24; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "ACCUULATED_EQUITY_INTEREST_5", "ASSETS", "LIABILITIES",
	                                "REVENUE", "PROFIT_OR_LOSS", "UNREG_SHARE_OF_LOSS" ,"CUMULATIVE_UNREG_SHARE_OF_LOSS" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_UNCONS_INVEST_Archival_Summary_Entity2.class.getMethod(getterName);
	                    Method setter = M_UNCONS_INVEST_Archival_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }
	    
	    // 3️⃣ Save updated entity
	    M_UNCONS_INVEST_Archival_Summary_Repo2.save(existing);
	}

	public void updateReport3(M_UNCONS_INVEST_Summary_Entity3 updatedEntity) {
	    System.out.println("Came to services 3");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_UNCONS_INVEST_Summary_Entity3 existing = M_UNCONS_INVEST_Summary_Repo3.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // ✅ Direct mapping for R29_FAIR_VALUE
	        existing.setR29_FAIR_VALUE(updatedEntity.getR29_FAIR_VALUE());

	        // Save back
	        M_UNCONS_INVEST_Summary_Repo3.save(existing);

	    } catch (Exception e) {
	        throw new RuntimeException("Error updating report: " + e.getMessage(), e);
	    }
	}
	
	public void updateArchivalReport3(M_UNCONS_INVEST_Archival_Summary_Entity3 updatedEntity) {
	    System.out.println("Came to services 3");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_UNCONS_INVEST_Archival_Summary_Entity3 existing = M_UNCONS_INVEST_Archival_Summary_Repo3.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // ✅ Direct mapping for R29_FAIR_VALUE
	        existing.setR29_FAIR_VALUE(updatedEntity.getR29_FAIR_VALUE());

	        // Save back
	        M_UNCONS_INVEST_Archival_Summary_Repo3.save(existing);

	    } catch (Exception e) {
	        throw new RuntimeException("Error updating report: " + e.getMessage(), e);
	    }
	}

	
	public void updateReport4(M_UNCONS_INVEST_Summary_Entity4 updatedEntity) {
	    System.out.println("Came to services 4");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_UNCONS_INVEST_Summary_Entity4 existing = M_UNCONS_INVEST_Summary_Repo4.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 35; i <= 38; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "COMPANY","JURISDICTION_OF_INCORP_1", "JURISDICTION_OF_INCORP_2", "LINE_OF_BUSINESS", "CURRENCY",
	                                "SHARE_CAPITAL", "ACCUMULATED_EQUITY_INTEREST" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_UNCONS_INVEST_Summary_Entity4.class.getMethod(getterName);
	                    Method setter = M_UNCONS_INVEST_Summary_Entity4.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	      

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    M_UNCONS_INVEST_Summary_Repo4.save(existing);
	}
	
	public void updateArchivalReport4(M_UNCONS_INVEST_Archival_Summary_Entity4 updatedEntity) {
	    System.out.println("Came to services 4");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    M_UNCONS_INVEST_Archival_Summary_Entity4 existing = M_UNCONS_INVEST_Archival_Summary_Repo4.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 35; i <= 38; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "COMPANY","JURISDICTION_OF_INCORP_1", "JURISDICTION_OF_INCORP_2", "LINE_OF_BUSINESS", "CURRENCY",
	                                "SHARE_CAPITAL", "ACCUMULATED_EQUITY_INTEREST" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = M_UNCONS_INVEST_Archival_Summary_Entity4.class.getMethod(getterName);
	                    Method setter = M_UNCONS_INVEST_Archival_Summary_Entity4.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	      

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save updated entity
	    M_UNCONS_INVEST_Archival_Summary_Repo4.save(existing);
	}
	
	
	
	public List<Object> getM_UNCONS_INVESTArchival() {
		List<Object> M_UNCONS_INVESTArchivallist = new ArrayList<>();
		try {
			M_UNCONS_INVESTArchivallist = M_UNCONS_INVEST_Archival_Summary_Repo1.getM_UNCONS_INVESTarchival();
			M_UNCONS_INVESTArchivallist = M_UNCONS_INVEST_Archival_Summary_Repo2.getM_UNCONS_INVESTarchival();
			M_UNCONS_INVESTArchivallist = M_UNCONS_INVEST_Archival_Summary_Repo3.getM_UNCONS_INVESTarchival();
			M_UNCONS_INVESTArchivallist = M_UNCONS_INVEST_Archival_Summary_Repo4.getM_UNCONS_INVESTarchival();
			System.out.println("countser" + M_UNCONS_INVESTArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_LA1 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_UNCONS_INVESTArchivallist;
	}
	
	public byte[] BRRS_M_UNCONS_INVESTExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		if (type.equals("ARCHIVAL") & version != null) {
			byte[] ARCHIVALreport = getExcelM_UNCONS_INVESTARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
			return ARCHIVALreport;
		}

		List<M_UNCONS_INVEST_Summary_Entity1> dataList = M_UNCONS_INVEST_Summary_Repo1.getdatabydateList(dateformat.parse(todate));
		List<M_UNCONS_INVEST_Summary_Entity2> dataList1 = M_UNCONS_INVEST_Summary_Repo2.getdatabydateList(dateformat.parse(todate));
		List<M_UNCONS_INVEST_Summary_Entity3> dataList2 = M_UNCONS_INVEST_Summary_Repo3.getdatabydateList(dateformat.parse(todate));
		List<M_UNCONS_INVEST_Summary_Entity4> dataList3 = M_UNCONS_INVEST_Summary_Repo4.getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRF2.4 report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---

			int startRow = 10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_UNCONS_INVEST_Summary_Entity1 record = dataList.get(i);
					M_UNCONS_INVEST_Summary_Entity2 record1 = dataList1.get(i);
					M_UNCONS_INVEST_Summary_Entity3 record2 = dataList2.get(i);
					M_UNCONS_INVEST_Summary_Entity4 record3 = dataList3.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR11_AMOUNT() != null) {
						cell3.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_PERCENT_OF_CET1_HOLDING() != null) {
						cell4.setCellValue(record.getR11_PERCENT_OF_CET1_HOLDING().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() != null) {
						cell5.setCellValue(record.getR11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row11
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR11_PERCENT_OF_TIER_2_HOLDING() != null) {
						cell6.setCellValue(record.getR11_PERCENT_OF_TIER_2_HOLDING().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR12_AMOUNT() != null) {
						cell3.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row12
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_PERCENT_OF_CET1_HOLDING() != null) {
						cell4.setCellValue(record.getR12_PERCENT_OF_CET1_HOLDING().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() != null) {
						cell5.setCellValue(record.getR12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row12
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_PERCENT_OF_TIER_2_HOLDING() != null) {
						cell6.setCellValue(record.getR12_PERCENT_OF_TIER_2_HOLDING().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR13_AMOUNT() != null) {
						cell3.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_PERCENT_OF_CET1_HOLDING() != null) {
						cell4.setCellValue(record.getR13_PERCENT_OF_CET1_HOLDING().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() != null) {
						cell5.setCellValue(record.getR13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_PERCENT_OF_TIER_2_HOLDING() != null) {
						cell6.setCellValue(record.getR13_PERCENT_OF_TIER_2_HOLDING().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR14_AMOUNT() != null) {
						cell3.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_PERCENT_OF_CET1_HOLDING() != null) {
						cell4.setCellValue(record.getR14_PERCENT_OF_CET1_HOLDING().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() != null) {
						cell5.setCellValue(record.getR14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_PERCENT_OF_TIER_2_HOLDING() != null) {
						cell6.setCellValue(record.getR14_PERCENT_OF_TIER_2_HOLDING().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row22
					row = sheet.getRow(21);
					// Column C
					Cell cell2 = row.getCell(2);
					if (record1.getR22_ACCUULATED_EQUITY_INTEREST_5() != null) {
						cell2.setCellValue(record1.getR22_ACCUULATED_EQUITY_INTEREST_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row22
					// Column D
					cell3 = row.createCell(3);
					if (record1.getR22_ASSETS() != null) {
						cell3.setCellValue(record1.getR22_ASSETS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR22_LIABILITIES() != null) {
						cell4.setCellValue(record1.getR22_LIABILITIES().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR22_REVENUE() != null) {
						cell5.setCellValue(record1.getR22_REVENUE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record1.getR22_PROFIT_OR_LOSS() != null) {
						cell6.setCellValue(record1.getR22_PROFIT_OR_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row22
					// Column H
					Cell cell7 = row.createCell(7);
					if (record1.getR22_UNREG_SHARE_OF_LOSS() != null) {
						cell7.setCellValue(record1.getR22_UNREG_SHARE_OF_LOSS().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row22
					// Column I
					Cell cell8 = row.createCell(8);
					if (record1.getR22_CUMULATIVE_UNREG_SHARE_OF_LOSS() != null) {
						cell8.setCellValue(record1.getR22_CUMULATIVE_UNREG_SHARE_OF_LOSS().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row23
					row = sheet.getRow(22);
					// Column C
					cell2 = row.getCell(2);
					if (record1.getR23_ACCUULATED_EQUITY_INTEREST_5() != null) {
						cell2.setCellValue(record1.getR23_ACCUULATED_EQUITY_INTEREST_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record1.getR23_ASSETS() != null) {
						cell3.setCellValue(record1.getR23_ASSETS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR23_LIABILITIES() != null) {
						cell4.setCellValue(record1.getR23_LIABILITIES().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR23_REVENUE() != null) {
						cell5.setCellValue(record1.getR23_REVENUE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record1.getR23_PROFIT_OR_LOSS() != null) {
						cell6.setCellValue(record1.getR23_PROFIT_OR_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row23
					// Column H
					cell7 = row.createCell(7);
					if (record1.getR23_UNREG_SHARE_OF_LOSS() != null) {
						cell7.setCellValue(record1.getR23_UNREG_SHARE_OF_LOSS().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row23
					// Column I
					cell8 = row.createCell(8);
					if (record1.getR23_CUMULATIVE_UNREG_SHARE_OF_LOSS() != null) {
						cell8.setCellValue(record1.getR23_CUMULATIVE_UNREG_SHARE_OF_LOSS().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// row24
					row = sheet.getRow(23);
					// Column C
					cell2 = row.getCell(2);
					if (record1.getR24_ACCUULATED_EQUITY_INTEREST_5() != null) {
						cell2.setCellValue(record1.getR24_ACCUULATED_EQUITY_INTEREST_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record1.getR24_ASSETS() != null) {
						cell3.setCellValue(record1.getR24_ASSETS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR24_LIABILITIES() != null) {
						cell4.setCellValue(record1.getR24_LIABILITIES().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR24_REVENUE() != null) {
						cell5.setCellValue(record1.getR24_REVENUE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record1.getR24_PROFIT_OR_LOSS() != null) {
						cell6.setCellValue(record1.getR24_PROFIT_OR_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row24
					// Column H
					cell7 = row.createCell(7);
					if (record1.getR24_UNREG_SHARE_OF_LOSS() != null) {
						cell7.setCellValue(record1.getR24_UNREG_SHARE_OF_LOSS().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row24
					// Column I
					cell8 = row.createCell(8);
					if (record1.getR24_CUMULATIVE_UNREG_SHARE_OF_LOSS() != null) {
						cell8.setCellValue(record1.getR24_CUMULATIVE_UNREG_SHARE_OF_LOSS().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row29
					row = sheet.getRow(28);
					// Column G
					cell6 = row.getCell(6);
					if (record2.getR29_FAIR_VALUE() != null) {
						cell6.setCellValue(record2.getR29_FAIR_VALUE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);

					}
					
					// row35
					row = sheet.getRow(34);
					// Column C
					cell2 = row.getCell(2);
					if (record3.getR35_COMPANY() != null) {
						cell2.setCellValue(record3.getR35_COMPANY().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row35
					// Column D
					cell3 = row.createCell(3);
					if (record3.getR35_JURISDICTION_OF_INCORP_1() != null) {
						cell3.setCellValue(record3.getR35_JURISDICTION_OF_INCORP_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row35
					// Column E
					cell4 = row.createCell(4);
					if (record3.getR35_JURISDICTION_OF_INCORP_2() != null) {
						cell4.setCellValue(record3.getR35_JURISDICTION_OF_INCORP_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row35
					// Column F
					cell5 = row.createCell(5);
					if (record3.getR35_LINE_OF_BUSINESS() != null) {
						cell5.setCellValue(record3.getR35_LINE_OF_BUSINESS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row35
					// Column G
					cell6 = row.createCell(6);
					if (record3.getR35_CURRENCY() != null) {
						cell6.setCellValue(record3.getR35_CURRENCY().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row35
					// Column H
					cell7 = row.createCell(7);
					if (record3.getR35_SHARE_CAPITAL() != null) {
						cell7.setCellValue(record3.getR35_SHARE_CAPITAL().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row35
					// Column I
					cell8 = row.createCell(8);
					if (record3.getR35_ACCUMULATED_EQUITY_INTEREST() != null) {
						cell8.setCellValue(record3.getR35_ACCUMULATED_EQUITY_INTEREST().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row36
					row = sheet.getRow(35);
					// Column C
					cell2 = row.getCell(2);
					if (record3.getR36_COMPANY() != null) {
						cell2.setCellValue(record3.getR36_COMPANY().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row36
					// Column D
					cell3 = row.createCell(3);
					if (record3.getR36_JURISDICTION_OF_INCORP_1() != null) {
						cell3.setCellValue(record3.getR36_JURISDICTION_OF_INCORP_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row36
					// Column E
					cell4 = row.createCell(4);
					if (record3.getR36_JURISDICTION_OF_INCORP_2() != null) {
						cell4.setCellValue(record3.getR36_JURISDICTION_OF_INCORP_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record3.getR36_LINE_OF_BUSINESS() != null) {
						cell5.setCellValue(record3.getR36_LINE_OF_BUSINESS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row36
					// Column G
					cell6 = row.createCell(6);
					if (record3.getR36_CURRENCY() != null) {
						cell6.setCellValue(record3.getR36_CURRENCY().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row36
					// Column H
					cell7 = row.createCell(7);
					if (record3.getR36_SHARE_CAPITAL() != null) {
						cell7.setCellValue(record3.getR36_SHARE_CAPITAL().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row36
					// Column I
					cell8 = row.createCell(8);
					if (record3.getR36_ACCUMULATED_EQUITY_INTEREST() != null) {
						cell8.setCellValue(record3.getR36_ACCUMULATED_EQUITY_INTEREST().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row37
					row = sheet.getRow(36);
					// Column C
					cell2 = row.getCell(2);
					if (record3.getR37_COMPANY() != null) {
						cell2.setCellValue(record3.getR37_COMPANY().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row37
					// Column D
					cell3 = row.createCell(3);
					if (record3.getR37_JURISDICTION_OF_INCORP_1() != null) {
						cell3.setCellValue(record3.getR37_JURISDICTION_OF_INCORP_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row37
					// Column E
					cell4 = row.createCell(4);
					if (record3.getR37_JURISDICTION_OF_INCORP_2() != null) {
						cell4.setCellValue(record3.getR37_JURISDICTION_OF_INCORP_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row37
					// Column F
					cell5 = row.createCell(5);
					if (record3.getR37_LINE_OF_BUSINESS() != null) {
						cell5.setCellValue(record3.getR37_LINE_OF_BUSINESS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row37
					// Column G
					cell6 = row.createCell(6);
					if (record3.getR37_CURRENCY() != null) {
						cell6.setCellValue(record3.getR37_CURRENCY().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row37
					// Column H
					cell7 = row.createCell(7);
					if (record3.getR37_SHARE_CAPITAL() != null) {
						cell7.setCellValue(record3.getR37_SHARE_CAPITAL().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row37
					// Column I
					cell8 = row.createCell(8);
					if (record3.getR37_ACCUMULATED_EQUITY_INTEREST() != null) {
						cell8.setCellValue(record3.getR37_ACCUMULATED_EQUITY_INTEREST().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row38
					row = sheet.getRow(37);
					// Column C
					cell2 = row.getCell(2);
					if (record3.getR38_COMPANY() != null) {
						cell2.setCellValue(record3.getR38_COMPANY().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row38
					// Column D
					cell3 = row.createCell(3);
					if (record3.getR38_JURISDICTION_OF_INCORP_1() != null) {
						cell3.setCellValue(record3.getR38_JURISDICTION_OF_INCORP_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row38
					// Column E
					cell4 = row.createCell(4);
					if (record3.getR38_JURISDICTION_OF_INCORP_2() != null) {
						cell4.setCellValue(record3.getR38_JURISDICTION_OF_INCORP_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row38
					// Column F
					cell5 = row.createCell(5);
					if (record3.getR38_LINE_OF_BUSINESS() != null) {
						cell5.setCellValue(record3.getR38_LINE_OF_BUSINESS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row38
					// Column G
					cell6 = row.createCell(6);
					if (record3.getR38_CURRENCY() != null) {
						cell6.setCellValue(record3.getR38_CURRENCY().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row38
					// Column H
					cell7 = row.createCell(7);
					if (record3.getR38_SHARE_CAPITAL() != null) {
						cell7.setCellValue(record3.getR38_SHARE_CAPITAL().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row38
					// Column I
					cell8 = row.createCell(8);
					if (record3.getR38_ACCUMULATED_EQUITY_INTEREST() != null) {
						cell8.setCellValue(record3.getR38_ACCUMULATED_EQUITY_INTEREST().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
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

	
	public byte[] getExcelM_UNCONS_INVESTARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_UNCONS_INVEST_Archival_Summary_Entity1> dataList = M_UNCONS_INVEST_Archival_Summary_Repo1.getdatabydateListarchival(dateformat.parse(todate),version);
		List<M_UNCONS_INVEST_Archival_Summary_Entity2> dataList1 = M_UNCONS_INVEST_Archival_Summary_Repo2.getdatabydateListarchival(dateformat.parse(todate),version);
		List<M_UNCONS_INVEST_Archival_Summary_Entity3> dataList2 = M_UNCONS_INVEST_Archival_Summary_Repo3.getdatabydateListarchival(dateformat.parse(todate),version);
		List<M_UNCONS_INVEST_Archival_Summary_Entity4> dataList3 = M_UNCONS_INVEST_Archival_Summary_Repo4.getdatabydateListarchival(dateformat.parse(todate),version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA1 report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---
			int startRow = 10;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_UNCONS_INVEST_Archival_Summary_Entity1 record = dataList.get(i);
					M_UNCONS_INVEST_Archival_Summary_Entity2 record1 = dataList1.get(i);
					M_UNCONS_INVEST_Archival_Summary_Entity3 record2 = dataList2.get(i);
					M_UNCONS_INVEST_Archival_Summary_Entity4 record3 = dataList3.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row11
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR11_AMOUNT() != null) {
						cell3.setCellValue(record.getR11_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_PERCENT_OF_CET1_HOLDING() != null) {
						cell4.setCellValue(record.getR11_PERCENT_OF_CET1_HOLDING().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() != null) {
						cell5.setCellValue(record.getR11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row11
					// Column G
					Cell cell6 = row.createCell(6);
					if (record.getR11_PERCENT_OF_TIER_2_HOLDING() != null) {
						cell6.setCellValue(record.getR11_PERCENT_OF_TIER_2_HOLDING().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR12_AMOUNT() != null) {
						cell3.setCellValue(record.getR12_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row12
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_PERCENT_OF_CET1_HOLDING() != null) {
						cell4.setCellValue(record.getR12_PERCENT_OF_CET1_HOLDING().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() != null) {
						cell5.setCellValue(record.getR12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row12
					// Column G
					cell6 = row.createCell(6);
					if (record.getR12_PERCENT_OF_TIER_2_HOLDING() != null) {
						cell6.setCellValue(record.getR12_PERCENT_OF_TIER_2_HOLDING().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR13_AMOUNT() != null) {
						cell3.setCellValue(record.getR13_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_PERCENT_OF_CET1_HOLDING() != null) {
						cell4.setCellValue(record.getR13_PERCENT_OF_CET1_HOLDING().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() != null) {
						cell5.setCellValue(record.getR13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row13
					// Column G
					cell6 = row.createCell(6);
					if (record.getR13_PERCENT_OF_TIER_2_HOLDING() != null) {
						cell6.setCellValue(record.getR13_PERCENT_OF_TIER_2_HOLDING().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.getCell(3);
					if (record.getR14_AMOUNT() != null) {
						cell3.setCellValue(record.getR14_AMOUNT().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);

					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_PERCENT_OF_CET1_HOLDING() != null) {
						cell4.setCellValue(record.getR14_PERCENT_OF_CET1_HOLDING().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() != null) {
						cell5.setCellValue(record.getR14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row14
					// Column G
					cell6 = row.createCell(6);
					if (record.getR14_PERCENT_OF_TIER_2_HOLDING() != null) {
						cell6.setCellValue(record.getR14_PERCENT_OF_TIER_2_HOLDING().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row22
					row = sheet.getRow(21);
					// Column C
					Cell cell2 = row.getCell(2);
					if (record1.getR22_ACCUULATED_EQUITY_INTEREST_5() != null) {
						cell2.setCellValue(record1.getR22_ACCUULATED_EQUITY_INTEREST_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row22
					// Column D
					cell3 = row.createCell(3);
					if (record1.getR22_ASSETS() != null) {
						cell3.setCellValue(record1.getR22_ASSETS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR22_LIABILITIES() != null) {
						cell4.setCellValue(record1.getR22_LIABILITIES().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR22_REVENUE() != null) {
						cell5.setCellValue(record1.getR22_REVENUE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record1.getR22_PROFIT_OR_LOSS() != null) {
						cell6.setCellValue(record1.getR22_PROFIT_OR_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row22
					// Column H
					Cell cell7 = row.createCell(7);
					if (record1.getR22_UNREG_SHARE_OF_LOSS() != null) {
						cell7.setCellValue(record1.getR22_UNREG_SHARE_OF_LOSS().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row22
					// Column I
					Cell cell8 = row.createCell(8);
					if (record1.getR22_CUMULATIVE_UNREG_SHARE_OF_LOSS() != null) {
						cell8.setCellValue(record1.getR22_CUMULATIVE_UNREG_SHARE_OF_LOSS().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row23
					row = sheet.getRow(22);
					// Column C
					cell2 = row.getCell(2);
					if (record1.getR23_ACCUULATED_EQUITY_INTEREST_5() != null) {
						cell2.setCellValue(record1.getR23_ACCUULATED_EQUITY_INTEREST_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row23
					// Column D
					cell3 = row.createCell(3);
					if (record1.getR23_ASSETS() != null) {
						cell3.setCellValue(record1.getR23_ASSETS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR23_LIABILITIES() != null) {
						cell4.setCellValue(record1.getR23_LIABILITIES().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR23_REVENUE() != null) {
						cell5.setCellValue(record1.getR23_REVENUE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record1.getR23_PROFIT_OR_LOSS() != null) {
						cell6.setCellValue(record1.getR23_PROFIT_OR_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row23
					// Column H
					cell7 = row.createCell(7);
					if (record1.getR23_UNREG_SHARE_OF_LOSS() != null) {
						cell7.setCellValue(record1.getR23_UNREG_SHARE_OF_LOSS().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row23
					// Column I
					cell8 = row.createCell(8);
					if (record1.getR23_CUMULATIVE_UNREG_SHARE_OF_LOSS() != null) {
						cell8.setCellValue(record1.getR23_CUMULATIVE_UNREG_SHARE_OF_LOSS().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					
					// row24
					row = sheet.getRow(23);
					// Column C
					cell2 = row.getCell(2);
					if (record1.getR24_ACCUULATED_EQUITY_INTEREST_5() != null) {
						cell2.setCellValue(record1.getR24_ACCUULATED_EQUITY_INTEREST_5().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row24
					// Column D
					cell3 = row.createCell(3);
					if (record1.getR24_ASSETS() != null) {
						cell3.setCellValue(record1.getR24_ASSETS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record1.getR24_LIABILITIES() != null) {
						cell4.setCellValue(record1.getR24_LIABILITIES().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record1.getR24_REVENUE() != null) {
						cell5.setCellValue(record1.getR24_REVENUE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record1.getR24_PROFIT_OR_LOSS() != null) {
						cell6.setCellValue(record1.getR24_PROFIT_OR_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row24
					// Column H
					cell7 = row.createCell(7);
					if (record1.getR24_UNREG_SHARE_OF_LOSS() != null) {
						cell7.setCellValue(record1.getR24_UNREG_SHARE_OF_LOSS().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row24
					// Column I
					cell8 = row.createCell(8);
					if (record1.getR24_CUMULATIVE_UNREG_SHARE_OF_LOSS() != null) {
						cell8.setCellValue(record1.getR24_CUMULATIVE_UNREG_SHARE_OF_LOSS().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row29
					row = sheet.getRow(28);
					// Column G
					cell6 = row.getCell(6);
					if (record2.getR29_FAIR_VALUE() != null) {
						cell6.setCellValue(record2.getR29_FAIR_VALUE().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);

					}
					
					// row35
					row = sheet.getRow(34);
					// Column C
					cell2 = row.getCell(2);
					if (record3.getR35_COMPANY() != null) {
						cell2.setCellValue(record3.getR35_COMPANY().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row35
					// Column D
					cell3 = row.createCell(3);
					if (record3.getR35_JURISDICTION_OF_INCORP_1() != null) {
						cell3.setCellValue(record3.getR35_JURISDICTION_OF_INCORP_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row35
					// Column E
					cell4 = row.createCell(4);
					if (record3.getR35_JURISDICTION_OF_INCORP_2() != null) {
						cell4.setCellValue(record3.getR35_JURISDICTION_OF_INCORP_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row35
					// Column F
					cell5 = row.createCell(5);
					if (record3.getR35_LINE_OF_BUSINESS() != null) {
						cell5.setCellValue(record3.getR35_LINE_OF_BUSINESS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row35
					// Column G
					cell6 = row.createCell(6);
					if (record3.getR35_CURRENCY() != null) {
						cell6.setCellValue(record3.getR35_CURRENCY().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row35
					// Column H
					cell7 = row.createCell(7);
					if (record3.getR35_SHARE_CAPITAL() != null) {
						cell7.setCellValue(record3.getR35_SHARE_CAPITAL().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row35
					// Column I
					cell8 = row.createCell(8);
					if (record3.getR35_ACCUMULATED_EQUITY_INTEREST() != null) {
						cell8.setCellValue(record3.getR35_ACCUMULATED_EQUITY_INTEREST().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row36
					row = sheet.getRow(35);
					// Column C
					cell2 = row.getCell(2);
					if (record3.getR36_COMPANY() != null) {
						cell2.setCellValue(record3.getR36_COMPANY().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row36
					// Column D
					cell3 = row.createCell(3);
					if (record3.getR36_JURISDICTION_OF_INCORP_1() != null) {
						cell3.setCellValue(record3.getR36_JURISDICTION_OF_INCORP_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row36
					// Column E
					cell4 = row.createCell(4);
					if (record3.getR36_JURISDICTION_OF_INCORP_2() != null) {
						cell4.setCellValue(record3.getR36_JURISDICTION_OF_INCORP_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record3.getR36_LINE_OF_BUSINESS() != null) {
						cell5.setCellValue(record3.getR36_LINE_OF_BUSINESS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row36
					// Column G
					cell6 = row.createCell(6);
					if (record3.getR36_CURRENCY() != null) {
						cell6.setCellValue(record3.getR36_CURRENCY().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row36
					// Column H
					cell7 = row.createCell(7);
					if (record3.getR36_SHARE_CAPITAL() != null) {
						cell7.setCellValue(record3.getR36_SHARE_CAPITAL().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row36
					// Column I
					cell8 = row.createCell(8);
					if (record3.getR36_ACCUMULATED_EQUITY_INTEREST() != null) {
						cell8.setCellValue(record3.getR36_ACCUMULATED_EQUITY_INTEREST().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row37
					row = sheet.getRow(36);
					// Column C
					cell2 = row.getCell(2);
					if (record3.getR37_COMPANY() != null) {
						cell2.setCellValue(record3.getR37_COMPANY().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row37
					// Column D
					cell3 = row.createCell(3);
					if (record3.getR37_JURISDICTION_OF_INCORP_1() != null) {
						cell3.setCellValue(record3.getR37_JURISDICTION_OF_INCORP_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row37
					// Column E
					cell4 = row.createCell(4);
					if (record3.getR37_JURISDICTION_OF_INCORP_2() != null) {
						cell4.setCellValue(record3.getR37_JURISDICTION_OF_INCORP_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row37
					// Column F
					cell5 = row.createCell(5);
					if (record3.getR37_LINE_OF_BUSINESS() != null) {
						cell5.setCellValue(record3.getR37_LINE_OF_BUSINESS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row37
					// Column G
					cell6 = row.createCell(6);
					if (record3.getR37_CURRENCY() != null) {
						cell6.setCellValue(record3.getR37_CURRENCY().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row37
					// Column H
					cell7 = row.createCell(7);
					if (record3.getR37_SHARE_CAPITAL() != null) {
						cell7.setCellValue(record3.getR37_SHARE_CAPITAL().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row37
					// Column I
					cell8 = row.createCell(8);
					if (record3.getR37_ACCUMULATED_EQUITY_INTEREST() != null) {
						cell8.setCellValue(record3.getR37_ACCUMULATED_EQUITY_INTEREST().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}
					
					// row38
					row = sheet.getRow(37);
					// Column C
					cell2 = row.getCell(2);
					if (record3.getR38_COMPANY() != null) {
						cell2.setCellValue(record3.getR38_COMPANY().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);

					}

					// row38
					// Column D
					cell3 = row.createCell(3);
					if (record3.getR38_JURISDICTION_OF_INCORP_1() != null) {
						cell3.setCellValue(record3.getR38_JURISDICTION_OF_INCORP_1().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// row38
					// Column E
					cell4 = row.createCell(4);
					if (record3.getR38_JURISDICTION_OF_INCORP_2() != null) {
						cell4.setCellValue(record3.getR38_JURISDICTION_OF_INCORP_2().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// row38
					// Column F
					cell5 = row.createCell(5);
					if (record3.getR38_LINE_OF_BUSINESS() != null) {
						cell5.setCellValue(record3.getR38_LINE_OF_BUSINESS().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// row38
					// Column G
					cell6 = row.createCell(6);
					if (record3.getR38_CURRENCY() != null) {
						cell6.setCellValue(record3.getR38_CURRENCY().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					// row38
					// Column H
					cell7 = row.createCell(7);
					if (record3.getR38_SHARE_CAPITAL() != null) {
						cell7.setCellValue(record3.getR38_SHARE_CAPITAL().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
					
					// row38
					// Column I
					cell8 = row.createCell(8);
					if (record3.getR38_ACCUMULATED_EQUITY_INTEREST() != null) {
						cell8.setCellValue(record3.getR38_ACCUMULATED_EQUITY_INTEREST().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
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

	}
