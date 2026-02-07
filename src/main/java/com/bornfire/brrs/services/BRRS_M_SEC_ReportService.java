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
// import java.util.ArrayList;  // SHOW WARNING HERE
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

// import javax.servlet.http.HttpServletRequest; // SHOW WARNING HERE
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook; // SHOW WARNING HERE
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

import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Detail1_Repo;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Detail2_Repo;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Detail3_Repo;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Detail4_Repo;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Entity3;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Entity4;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Repo4;
import com.bornfire.brrs.entities.BRRS_M_SEC_Detail1_Repo;
import com.bornfire.brrs.entities.BRRS_M_SEC_Detail2_Repo;
import com.bornfire.brrs.entities.BRRS_M_SEC_Detail3_Repo;
import com.bornfire.brrs.entities.BRRS_M_SEC_Detail4_Repo;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Entity1;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Entity3;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Entity4;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Repo4;
import com.bornfire.brrs.entities.M_SEC_Archival_Detail1_Entity;
import com.bornfire.brrs.entities.M_SEC_Archival_Detail2_Entity;
import com.bornfire.brrs.entities.M_SEC_Archival_Detail3_Entity;
import com.bornfire.brrs.entities.M_SEC_Archival_Detail4_Entity;
import com.bornfire.brrs.entities.M_SEC_Detail1_Entity;
import com.bornfire.brrs.entities.M_SEC_Detail2_Entity;
import com.bornfire.brrs.entities.M_SEC_Detail3_Entity;
import com.bornfire.brrs.entities.M_SEC_Detail4_Entity;



@Component
@Service
public class BRRS_M_SEC_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SEC_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private SessionFactory sessionFactory;

	
	@Autowired
	private BRRS_M_SEC_Summary_Repo1 secSummaryRepo1;
	@Autowired
	private BRRS_M_SEC_Summary_Repo2 secSummaryRepo2;
	@Autowired
	private BRRS_M_SEC_Summary_Repo3 secSummaryRepo3;
	@Autowired
	private BRRS_M_SEC_Summary_Repo4 secSummaryRepo4;

	
	@Autowired
	BRRS_M_SEC_Archival_Summary_Repo1 archivalSummaryRepo1;
	@Autowired
	BRRS_M_SEC_Archival_Summary_Repo2 archivalSummaryRepo2;
	@Autowired
	BRRS_M_SEC_Archival_Summary_Repo3 archivalSummaryRepo3;
	@Autowired
	BRRS_M_SEC_Archival_Summary_Repo4 archivalSummaryRepo4;
	
	
	
	 @Autowired
		private BRRS_M_SEC_Detail1_Repo secDetailRepo1;
		@Autowired
		private BRRS_M_SEC_Detail2_Repo secDetailRepo2;
		@Autowired
		private BRRS_M_SEC_Detail3_Repo secDetailRepo3;
		@Autowired
		private BRRS_M_SEC_Detail4_Repo secDetailRepo4;
		
		
		
		@Autowired
		BRRS_M_SEC_Archival_Detail1_Repo archivalDetailRepo1;
		@Autowired
		BRRS_M_SEC_Archival_Detail2_Repo archivalDetailRepo2;
		@Autowired
		BRRS_M_SEC_Archival_Detail3_Repo archivalDetailRepo3;
		@Autowired
		BRRS_M_SEC_Archival_Detail4_Repo archivalDetailRepo4;
		
		
		

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ================== VIEW ==================
	public ModelAndView getM_SECView(
	        String reportId,
	        String fromdate,
	        String todate,
	        String currency,
	        String dtltype,
	        Pageable pageable,
	        String type,
	        BigDecimal version) {

	    ModelAndView mv = new ModelAndView();
	    Session hs = sessionFactory.getCurrentSession();

	    int pageSize = pageable.getPageSize();
	    int currentPage = pageable.getPageNumber();
	    int startItem = currentPage * pageSize;

	    try {
	        Date d1 = dateformat.parse(todate);

	        // ---------- CASE 1: ARCHIVAL ----------
	        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

	           List<BRRS_M_SEC_Archival_Summary_Entity1> T1Master = archivalSummaryRepo1
	                        .getdatabydateListarchival(d1, version);
	                List<BRRS_M_SEC_Archival_Summary_Entity2> T2Master = archivalSummaryRepo2
	                        .getdatabydateListarchival(d1, version);
	                List<BRRS_M_SEC_Archival_Summary_Entity3> T3Master = archivalSummaryRepo3
	                        .getdatabydateListarchival(d1, version);
	                List<BRRS_M_SEC_Archival_Summary_Entity4> T4Master = archivalSummaryRepo4
	                        .getdatabydateListarchival(d1, version);
							
							
	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary1", T1Master);
	                mv.addObject("reportsummary2", T2Master);
	                mv.addObject("reportsummary3", T3Master);
	                mv.addObject("reportsummary4", T4Master);
	        }

	        // ---------- CASE 2: RESUB ----------
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	           List<BRRS_M_SEC_Archival_Summary_Entity1> T1Master = archivalSummaryRepo1
	                        .getdatabydateListarchival(d1, version);
	                List<BRRS_M_SEC_Archival_Summary_Entity2> T2Master = archivalSummaryRepo2
	                        .getdatabydateListarchival(d1, version);
	                List<BRRS_M_SEC_Archival_Summary_Entity3> T3Master = archivalSummaryRepo3
	                        .getdatabydateListarchival(d1, version);
	                List<BRRS_M_SEC_Archival_Summary_Entity4> T4Master = archivalSummaryRepo4
	                        .getdatabydateListarchival(d1, version);
							
	            mv.addObject("displaymode", "summary");
				
	            mv.addObject("reportsummary1", T1Master);
	                mv.addObject("reportsummary2", T2Master);
	                mv.addObject("reportsummary3", T3Master);
	                mv.addObject("reportsummary4", T4Master);
	                
	        }

	        // ---------- CASE 3: NORMAL ----------
	        else {

	                List<BRRS_M_SEC_Summary_Entity1> T1Master = secSummaryRepo1.getdatabydateList(d1);
	                List<BRRS_M_SEC_Summary_Entity2> T2Master = secSummaryRepo2.getdatabydateList(d1);
	                List<BRRS_M_SEC_Summary_Entity3> T3Master = secSummaryRepo3.getdatabydateList(d1);
	                List<BRRS_M_SEC_Summary_Entity4> T4Master = secSummaryRepo4.getdatabydateList(d1);
	                

	                System.out.println("T1Master Size: " + T1Master.size());
	                System.out.println("T2Master Size: " + T2Master.size());
	                System.out.println("T3Master Size: " + T3Master.size());
	                System.out.println("T4Master Size: " + T4Master.size());
					
					
	            mv.addObject("displaymode", "summary");
				
	            
	            mv.addObject("reportsummary1", T1Master);
	                mv.addObject("reportsummary2", T2Master);
	                mv.addObject("reportsummary3", T3Master);
	                mv.addObject("reportsummary4", T4Master);
	        }

	        // ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
	        if ("detail".equalsIgnoreCase(dtltype)) {

	            // DETAIL + ARCHIVAL
	            if (version != null) {

	                List<M_SEC_Archival_Detail1_Entity> T1Master =
	                       archivalDetailRepo1
	                                .getdatabydateListarchival(d1, version);
	List<M_SEC_Archival_Detail2_Entity> T2Master =
	                       archivalDetailRepo2
	                                .getdatabydateListarchival(d1, version);
	  List<M_SEC_Archival_Detail3_Entity> T3Master =
	                       archivalDetailRepo3
	                                .getdatabydateListarchival(d1, version);             
List<M_SEC_Archival_Detail4_Entity> T4Master =
	                       archivalDetailRepo4
	                                .getdatabydateListarchival(d1, version);
									
									
	                mv.addObject("displaymode", "Details");
	                  mv.addObject("reportsummary1", T1Master);
	                mv.addObject("reportsummary2", T2Master);
	                mv.addObject("reportsummary3", T3Master);
	                mv.addObject("reportsummary4", T4Master);
	            }
	            // DETAIL + NORMAL
	            else {

	                                 List<M_SEC_Detail1_Entity> T1Master =
	                        secDetailRepo1
	                                .getdatabydateList(dateformat.parse(todate));
									 List<M_SEC_Detail2_Entity> T2Master =
	                        secDetailRepo2
	                                .getdatabydateList(dateformat.parse(todate));
									 List<M_SEC_Detail3_Entity> T3Master =
	                        secDetailRepo3
	                                .getdatabydateList(dateformat.parse(todate));
									 List<M_SEC_Detail4_Entity> T4Master =
	                        secDetailRepo4
	                                .getdatabydateList(dateformat.parse(todate));
	
	                mv.addObject("displaymode", "Details");
	                mv.addObject("reportsummary1", T1Master);
	                mv.addObject("reportsummary2", T2Master);
	                mv.addObject("reportsummary3", T3Master);
	                mv.addObject("reportsummary4", T4Master);
	            }
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_SEC");
	   
	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}
	
	
	@Transactional
	public void updateReport(BRRS_M_SEC_Summary_Entity1 updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	     BRRS_M_SEC_Summary_Entity1 existingSummary =
	            secSummaryRepo1.findById(updatedEntity.getReportDate())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    // 2️⃣ Fetch or create DETAIL
	      M_SEC_Detail1_Entity existingDetail =
	            secDetailRepo1.findById(updatedEntity.getReportDate())
	                    .orElseGet(() -> {
	                          M_SEC_Detail1_Entity d = new   M_SEC_Detail1_Entity();
	                        d.setReportDate(updatedEntity.getReportDate());
	                        return d;
	                    });

	  try {
	        // 1️⃣ Loop from R11 to R18 and copy fields
	    	
	        for (int i = 11; i <= 19; i++) {
				
	        	 String prefix = "R" + i + "_";

	            String[] fields = { "TCA" };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter =
	                              BRRS_M_SEC_Summary_Entity1.class.getMethod(getterName);

	                    Method summarySetter =
	                              BRRS_M_SEC_Summary_Entity1.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Method detailSetter =
	                              M_SEC_Detail1_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);

	                    // ✅ set into SUMMARY
	                    summarySetter.invoke(existingSummary, newValue);

	                    // ✅ set into DETAIL
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    // skip missing fields safely
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save BOTH (same transaction)
	    secSummaryRepo1.save(existingSummary);
	    secDetailRepo1.save(existingDetail);
		
		}
		
		@Transactional
	public void updateReport1(BRRS_M_SEC_Summary_Entity2 updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	     BRRS_M_SEC_Summary_Entity2 existingSummary =
	            secSummaryRepo2.findById(updatedEntity.getReportDate())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    // 2️⃣ Fetch or create DETAIL
	      M_SEC_Detail2_Entity existingDetail =
	            secDetailRepo2.findById(updatedEntity.getReportDate())
	                    .orElseGet(() -> {
	                          M_SEC_Detail2_Entity d = new   M_SEC_Detail2_Entity();
	                        d.setReportDate(updatedEntity.getReportDate());
	                        return d;
	                    });

	  try {
	        // 1️⃣ Loop from R11 to R16 and copy fields
	    	
	        for (int i = 11; i <= 16; i++) {
				
	        	 String prefix = "R" + i + "_";

	            String[] fields = { "TCA2" };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter =
	                              BRRS_M_SEC_Summary_Entity2.class.getMethod(getterName);

	                    Method summarySetter =
	                              BRRS_M_SEC_Summary_Entity2.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Method detailSetter =
	                              M_SEC_Detail2_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);

	                    // ✅ set into SUMMARY
	                    summarySetter.invoke(existingSummary, newValue);

	                    // ✅ set into DETAIL
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    // skip missing fields safely
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save BOTH (same transaction)
	    secSummaryRepo2.save(existingSummary);
	    secDetailRepo2.save(existingDetail);
		
		}
		
		
		@Transactional
	public void updateReport2(BRRS_M_SEC_Summary_Entity3 updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	     BRRS_M_SEC_Summary_Entity3 existingSummary =
	            secSummaryRepo3.findById(updatedEntity.getReportDate())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    // 2️⃣ Fetch or create DETAIL
	      M_SEC_Detail3_Entity existingDetail =
	            secDetailRepo3.findById(updatedEntity.getReportDate())
	                    .orElseGet(() -> {
	                          M_SEC_Detail3_Entity d = new   M_SEC_Detail3_Entity();
	                        d.setReportDate(updatedEntity.getReportDate());
	                        return d;
	                    });

	  try {
	        // 1️⃣ Loop from R26 to R30 and copy fields
	    	
	        for (int i = 26; i <= 31; i++) {
				
	        	 String prefix = "R" + i + "_";

	            String[] fields = {"0_1Y_FT", "0_1Y_HTM", "0_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
						"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL"};

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter =
	                              BRRS_M_SEC_Summary_Entity3.class.getMethod(getterName);

	                    Method summarySetter =
	                              BRRS_M_SEC_Summary_Entity3.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Method detailSetter =
	                              M_SEC_Detail3_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);

	                    // ✅ set into SUMMARY
	                    summarySetter.invoke(existingSummary, newValue);

	                    // ✅ set into DETAIL
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    // skip missing fields safely
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save BOTH (same transaction)
	    secSummaryRepo3.save(existingSummary);
	    secDetailRepo3.save(existingDetail);
		
		}
		
		
			@Transactional
	public void updateReport3(BRRS_M_SEC_Summary_Entity4 updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	     BRRS_M_SEC_Summary_Entity4 existingSummary =
	            secSummaryRepo4.findById(updatedEntity.getReportDate())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    // 2️⃣ Fetch or create DETAIL
	      M_SEC_Detail4_Entity existingDetail =
	            secDetailRepo4.findById(updatedEntity.getReportDate())
	                    .orElseGet(() -> {
	                          M_SEC_Detail4_Entity d = new   M_SEC_Detail4_Entity();
	                        d.setReportDate(updatedEntity.getReportDate());
	                        return d;
	                    });

	  try {
	        // 1️⃣ Loop from R36 to R43 and copy fields
	    	
	        for (int i = 36; i <= 43; i++) {
				
	        	 String prefix = "R" + i + "_";

	            String[] fields = {"0_1Y_FT", "0_1Y_HTM", "0_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
					"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL"};

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter =
	                              BRRS_M_SEC_Summary_Entity4.class.getMethod(getterName);

	                    Method summarySetter =
	                              BRRS_M_SEC_Summary_Entity4.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Method detailSetter =
	                              M_SEC_Detail4_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);

	                    // ✅ set into SUMMARY
	                    summarySetter.invoke(existingSummary, newValue);

	                    // ✅ set into DETAIL
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    // skip missing fields safely
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("Error while updating report fields", e);
	    }

	    // 3️⃣ Save BOTH (same transaction)
	    secSummaryRepo4.save(existingSummary);
	    secDetailRepo4.save(existingDetail);
		
		}
		
//	public void updateReport(BRRS_M_SEC_Summary_Entity1 updatedEntity) {
//		System.out.println("Came to services 1");
//		System.out.println("Report Date: " + updatedEntity.getReportDate());
//
//		BRRS_M_SEC_Summary_Entity1 existing = secSummaryRepo1.findById(updatedEntity.getReportDate())
//				.orElseThrow(() -> new RuntimeException(
//						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
//
//		try {
//			// 1️⃣ Loop from R11 to R15 and copy fields
//			for (int i = 11; i <= 18; i++) {
//				String prefix = "R" + i + "_";
//
//				String[] fields = { "TCA" };
//
//				for (String field : fields) {
//					String getterName = "get" + prefix + field;
//					String setterName = "set" + prefix + field;
//
//					try {
//						Method getter = BRRS_M_SEC_Summary_Entity1.class.getMethod(getterName);
//						Method setter = BRRS_M_SEC_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(updatedEntity);
//						setter.invoke(existing, newValue);
//
//					} catch (NoSuchMethodException e) {
//						// Skip missing fields
//						continue;
//					}
//				}
//			}
//
//			// 2️⃣ Handle R19 totals
//			String[] totalFields = { "TCA" };
//
//			for (String field : totalFields) {
//				String getterName = "getR19_" + field;
//				String setterName = "setR19_" + field;
//
//				try {
//					Method getter = BRRS_M_SEC_Summary_Entity1.class.getMethod(getterName);
//					Method setter = BRRS_M_SEC_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());
//
//					Object newValue = getter.invoke(updatedEntity);
//					setter.invoke(existing, newValue);
//
//				} catch (NoSuchMethodException e) {
//					// Skip if not present
//					continue;
//				}
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating report fields", e);
//		}
//		System.out.println("Testing 1");
//		// 3️⃣ Save updated entity
//		secSummaryRepo1.save(existing);
//
//	}
//
//	public void updateReport2(BRRS_M_SEC_Summary_Entity2 updatedEntity) {
//		System.out.println("Came to services 2");
//		System.out.println("Report Date: " + updatedEntity.getReportDate());
//
//		BRRS_M_SEC_Summary_Entity2 existing = secSummaryRepo2.findById(updatedEntity.getReportDate())
//				.orElseThrow(() -> new RuntimeException(
//						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
//
//		try {
//			// 1️⃣ Loop from R11 to R15 and copy fields
//			for (int i = 11; i <= 15; i++) {
//				String prefix = "R" + i + "_";
//
//				String[] fields = { "TCA2" };
//
//				for (String field : fields) {
//					String getterName = "get" + prefix + field;
//					String setterName = "set" + prefix + field;
//
//					try {
//						Method getter = BRRS_M_SEC_Summary_Entity2.class.getMethod(getterName);
//						Method setter = BRRS_M_SEC_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(updatedEntity);
//						setter.invoke(existing, newValue);
//
//					} catch (NoSuchMethodException e) {
//						// Skip missing fields
//						continue;
//					}
//				}
//			}
//
//			// 2️⃣ Handle R16 totals
//			String[] totalFields = { "TCA2" };
//
//			for (String field : totalFields) {
//				String getterName = "getR16_" + field;
//				String setterName = "setR16_" + field;
//
//				try {
//					Method getter = BRRS_M_SEC_Summary_Entity2.class.getMethod(getterName);
//					Method setter = BRRS_M_SEC_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
//
//					Object newValue = getter.invoke(updatedEntity);
//					setter.invoke(existing, newValue);
//
//				} catch (NoSuchMethodException e) {
//					// Skip if not present
//					continue;
//				}
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating report fields", e);
//		}
//		System.out.println("Testing 1");
//		// 3️⃣ Save updated entity
//		secSummaryRepo2.save(existing);
//
//	}
//
//
//	public void updateReport3(BRRS_M_SEC_Summary_Entity3 updatedEntity) {
//		System.out.println("Came to services 3");
//		System.out.println("Report Date: " + updatedEntity.getReportDate());
//
//		BRRS_M_SEC_Summary_Entity3 existing = secSummaryRepo3.findById(updatedEntity.getReportDate())
//				.orElseThrow(() -> new RuntimeException(
//						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
//
//		try {
//			// 1️⃣ Loop from R11 to R15 and copy fields
//			for (int i = 26; i <= 30; i++) {
//				String prefix = "R" + i + "_";
//
//				String[] fields = { "0_1Y_FT", "0_1Y_HTM", "0_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
//						"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };
//
//				for (String field : fields) {
//					String getterName = "get" + prefix + field;
//					String setterName = "set" + prefix + field;
//
//					try {
//						Method getter = BRRS_M_SEC_Summary_Entity3.class.getMethod(getterName);
//						Method setter = BRRS_M_SEC_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(updatedEntity);
//						setter.invoke(existing, newValue);
//
//					} catch (NoSuchMethodException e) {
//						// Skip missing fields
//						continue;
//					}
//				}
//			}
//
//			// 2️⃣ Handle R31 totals
//			String[] totalFields = { "0_1Y_FT", "0_1Y_HTM", "0_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
//					"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };
//
//			for (String field : totalFields) {
//				String getterName = "getR31_" + field;
//				String setterName = "setR31_" + field;
//
//				try {
//					Method getter = BRRS_M_SEC_Summary_Entity3.class.getMethod(getterName);
//					Method setter = BRRS_M_SEC_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());
//
//					Object newValue = getter.invoke(updatedEntity);
//					setter.invoke(existing, newValue);
//
//				} catch (NoSuchMethodException e) {
//					// Skip if not present
//					continue;
//				}
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating report fields", e);
//		}
//		System.out.println("Testing 1");
//		// 3️⃣ Save updated entity
//		secSummaryRepo3.save(existing);
//
//	}
//
//
//	public void updateReport4(BRRS_M_SEC_Summary_Entity4 updatedEntity) {
//		System.out.println("Came to services 4");
//		System.out.println("Report Date: " + updatedEntity.getReportDate());
//
//		BRRS_M_SEC_Summary_Entity4 existing = secSummaryRepo4.findById(updatedEntity.getReportDate())
//				.orElseThrow(() -> new RuntimeException(
//						"Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));
//
//		try {
//			// 1️⃣ Loop from R11 to R15 and copy fields
//			for (int i = 36; i <= 42; i++) {
//				String prefix = "R" + i + "_";
//
//				String[] fields = { "0_1Y_FT", "0_1Y_HTM", "0_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
//						"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };
//
//				for (String field : fields) {
//					String getterName = "get" + prefix + field;
//					String setterName = "set" + prefix + field;
//
//					try {
//						Method getter = BRRS_M_SEC_Summary_Entity4.class.getMethod(getterName);
//						Method setter = BRRS_M_SEC_Summary_Entity4.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(updatedEntity);
//						setter.invoke(existing, newValue);
//
//					} catch (NoSuchMethodException e) {
//						// Skip missing fields
//						continue;
//					}
//				}
//			}
//
//			// 2️⃣ Handle R43 totals
//			String[] totalFields = { "0_1Y_FT", "0_1Y_HTM", "0_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
//					"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };
//
//			for (String field : totalFields) {
//				String getterName = "getR43_" + field;
//				String setterName = "setR43_" + field;
//
//				try {
//					Method getter = BRRS_M_SEC_Summary_Entity4.class.getMethod(getterName);
//					Method setter = BRRS_M_SEC_Summary_Entity4.class.getMethod(setterName, getter.getReturnType());
//
//					Object newValue = getter.invoke(updatedEntity);
//					setter.invoke(existing, newValue);
//
//				} catch (NoSuchMethodException e) {
//					// Skip if not present
//					continue;
//				}
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating report fields", e);
//		}
//		System.out.println("Testing 4");
//		// 3️⃣ Save updated entity
//		secSummaryRepo4.save(existing);
//
//	}

	
	// // ================== DETAIL ==================
	// public ModelAndView getM_SECcurrentDtl(String reportId, String fromdate,
	// String todate, String currency,
	// String dtltype, Pageable pageable, String Filter, String type, String
	// version) {

	// int pageSize = pageable != null ? pageable.getPageSize() : 10;
	// int currentPage = pageable != null ? pageable.getPageNumber() : 0;
	// int totalPages = 0;

	// ModelAndView mv = new ModelAndView();
	// Session hs = sessionFactory.getCurrentSession();

	// try {
	// Date parsedDate = todate != null && !todate.isEmpty() ?
	// dateformat.parse(todate) : null;

	// String rowId = null;
	// String columnId = null;
	// if (Filter != null && Filter.contains(",")) {
	// String[] parts = Filter.split(",");
	// if (parts.length >= 2) {
	// rowId = parts[0];
	// columnId = parts[1];
	// }
	// }

	// if ("ARCHIVAL".equals(type) && version != null) {
	// List<BRRS_M_SEC_ARCHIVAL_DETAIL_Entity1> T1Dt1;
	// if (rowId != null && columnId != null) {
	// T1Dt1 = archivalDetailRepo1.GetDataByRowIdAndColumnId(rowId, columnId,
	// parsedDate, version);
	// } else {
	// T1Dt1 = archivalDetailRepo1.getdatabydateList(parsedDate, version);
	// }
	// mv.addObject("reportdetails", T1Dt1);
	// mv.addObject("reportmaster12", T1Dt1);

	// } else {
	// List<BRRS_M_SEC_Detail_Entity1> T1Dt1;
	// if (rowId != null && columnId != null) {
	// T1Dt1 = secDetailRepo1.GetDataByRowIdAndColumnId(rowId, columnId,
	// parsedDate);
	// } else {
	// T1Dt1 = secDetailRepo1.getdatabydateList(parsedDate, currentPage, pageSize);
	// totalPages = secDetailRepo1.getdatacount(parsedDate);
	// mv.addObject("pagination", "YES");
	// }
	// mv.addObject("reportdetails", T1Dt1);
	// mv.addObject("reportmaster12", T1Dt1);
	// }

	// } catch (ParseException e) {
	// logger.error("Invalid date format: {}", todate, e);
	// mv.addObject("errorMessage", "Invalid date format: " + todate);
	// } catch (Exception e) {
	// logger.error("Unexpected error", e);
	// mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
	// }

	// mv.setViewName("BRRS/M_SEC");
	// mv.addObject("displaymode", "Details");
	// mv.addObject("currentPage", currentPage);
	// mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
	// mv.addObject("reportsflag", "reportsflag");
	// mv.addObject("menu", reportId);

	// return mv;
	// }

	// ================== UPDATE METHODS ==================
	// public void updateReport(BRRS_M_SEC_Summary_Entity1 updatedEntity) {
	// BRRS_M_SEC_Summary_Entity1 existing =
	// secSummaryRepo1.findById(updatedEntity.getREPORT_DATE())
	// .orElseThrow(() -> new RuntimeException(
	// "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	// try {
	// for (int i = 11; i <= 18; i++) {
	// copyField(updatedEntity, existing, "R" + i + "_TCA");
	// }
	// copyField(updatedEntity, existing, "R19_TCA");

	// secSummaryRepo1.save(existing);
	// } catch (Exception e) {
	// throw new RuntimeException("Error while updating report fields", e);
	// }
	// }

	// public void updateReport2(BRRS_M_SEC_Summary_Entity2 updatedEntity) {
	// BRRS_M_SEC_Summary_Entity2 existing =
	// secSummaryRepo2.findById(updatedEntity.getREPORT_DATE())
	// .orElseThrow(() -> new RuntimeException(
	// "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	// try {
	// for (int i = 11; i <= 15; i++) {
	// copyField(updatedEntity, existing, "R" + i + "_TCA");
	// }
	// copyField(updatedEntity, existing, "R16_TCA");

	// secSummaryRepo2.save(existing);
	// } catch (Exception e) {
	// throw new RuntimeException("Error while updating report fields", e);
	// }
	// }

	// public void updateReport3(BRRS_M_SEC_Summary_Entity3 updatedEntity) {
	// BRRS_M_SEC_Summary_Entity3 existing =
	// secSummaryRepo3.findById(updatedEntity.getREPORT_DATE())
	// .orElseThrow(() -> new RuntimeException(
	// "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	// try {
	// for (int i = 26; i <= 30; i++) {
	// copyMultipleFields(updatedEntity, existing, i);
	// }
	// copyMultipleFields(updatedEntity, existing, 31);

	// secSummaryRepo3.save(existing);
	// } catch (Exception e) {
	// throw new RuntimeException("Error while updating report fields", e);
	// }
	// }

	// public void updateReport4(BRRS_M_SEC_Summary_Entity4 updatedEntity) {
	// BRRS_M_SEC_Summary_Entity4 existing =
	// secSummaryRepo4.findById(updatedEntity.getREPORT_DATE())
	// .orElseThrow(() -> new RuntimeException(
	// "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	// try {
	// for (int i = 36; i <= 42; i++) {
	// copyMultipleFields(updatedEntity, existing, i);
	// }
	// copyMultipleFields(updatedEntity, existing, 43);

	// secSummaryRepo4.save(existing);
	// } catch (Exception e) {
	// throw new RuntimeException("Error while updating report fields", e);
	// }
	// }

	// private void copyField(Object source, Object target, String fieldName) throws
	// Exception {
	// try {
	// Method getter = source.getClass().getMethod("get" + fieldName);
	// Method setter = target.getClass().getMethod("set" + fieldName,
	// getter.getReturnType());
	// Object value = getter.invoke(source);
	// setter.invoke(target, value);
	// } catch (NoSuchMethodException e) {
	// // skip missing field
	// }
	// }

	// private void copyMultipleFields(Object source, Object target, int row) throws
	// Exception {
	// String[] fields = { "O_1Y_FT", "O_1Y_HTM", "O_1Y_TOTAL", "1_5Y_FT",
	// "1_5Y_HTM", "1_5Y_TOTAL",
	// "O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };
	// for (String field : fields) {
	// copyField(source, target, "R" + row + "_" + field);
	// }
	// }

	public byte[] getM_SECExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		
		
		
		// ARCHIVAL check
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
            logger.info("Service: Generating ARCHIVAL report for version {}", version);
            return getExcelM_SECARCHIVAL(filename, reportId, fromdate, todate,
                    currency, dtltype, type, version);
        }
        // Email check
         if ("email".equalsIgnoreCase(type)  && version == null) {
            logger.info("Service: Generating Email report for version {}", version);
            return BRRS_M_SECEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
        } else if ("email".equalsIgnoreCase(type) && version != null) {
            logger.info("Service: Generating Email report for version {}", version);
            return BRRS_M_SECRCHIVALEmailExcel(filename, reportId, fromdate, todate,
                    currency, dtltype, type, version);

        }


		// Default (LIVE) case
		List<BRRS_M_SEC_Summary_Entity1> dataList1 = secSummaryRepo1.getdatabydateList(dateformat.parse(todate));
		List<BRRS_M_SEC_Summary_Entity2> dataList2 = secSummaryRepo2.getdatabydateList(dateformat.parse(todate));
		List<BRRS_M_SEC_Summary_Entity3> dataList3 = secSummaryRepo3.getdatabydateList(dateformat.parse(todate));
		List<BRRS_M_SEC_Summary_Entity4> dataList4 = secSummaryRepo4.getdatabydateList(dateformat.parse(todate));

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

if (!dataList1.isEmpty()) {
	for (int i = 0; i < dataList1.size(); i++) {

		BRRS_M_SEC_Summary_Entity1 record = dataList1.get(i);
		System.out.println("rownumber=" + startRow + i);
		Row row = sheet.getRow(startRow + i);
		if (row == null) {
			row = sheet.createRow(startRow + i);
		}
					// row11
					Cell cellB = row.createCell(1);
					if (record.getR11_TCA() != null) {
						cellB.setCellValue(record.getR11_TCA().longValue()); // ← whole number
						cellB.setCellStyle(numberStyle); // ← format changed below
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					cellB = row.createCell(1);
					if (record.getR12_TCA() != null) {
						cellB.setCellValue(record.getR12_TCA().longValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					cellB = row.createCell(1);
					if (record.getR13_TCA() != null) {
						cellB.setCellValue(record.getR13_TCA().longValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					cellB = row.createCell(1);
					if (record.getR14_TCA() != null) {
						cellB.setCellValue(record.getR14_TCA().longValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					cellB = row.createCell(1);
					if (record.getR15_TCA() != null) {
						cellB.setCellValue(record.getR15_TCA().longValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					cellB = row.createCell(1);
					if (record.getR16_TCA() != null) {
						cellB.setCellValue(record.getR16_TCA().longValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					cellB = row.createCell(1);
					if (record.getR17_TCA() != null) {
						cellB.setCellValue(record.getR17_TCA().longValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					cellB = row.createCell(1);
					if (record.getR18_TCA() != null) {
						cellB.setCellValue(record.getR18_TCA().longValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
				}
			}

			startRow = 10;

			if (!dataList2.isEmpty()) {
				for (int i = 0; i < dataList2.size(); i++) {

					BRRS_M_SEC_Summary_Entity2 record = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row11
					// Column B2 - Original Amount
					Cell cellB = row.createCell(6);
					if (record.getR11_TCA2() != null) {
						cellB.setCellValue(record.getR11_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);

					cellB = row.createCell(6);
					if (record.getR12_TCA2() != null) {
						cellB.setCellValue(record.getR12_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cellB = row.createCell(6);
					if (record.getR13_TCA2() != null) {
						cellB.setCellValue(record.getR13_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					cellB = row.createCell(6);
					if (record.getR14_TCA2() != null) {
						cellB.setCellValue(record.getR14_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					cellB = row.createCell(6);
					if (record.getR15_TCA2() != null) {
						cellB.setCellValue(record.getR15_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
				}
			}

			startRow = 25;

			if (!dataList3.isEmpty()) {
				for (int i = 0; i < dataList3.size(); i++) {

					BRRS_M_SEC_Summary_Entity3 record = dataList3.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell cellB = row.createCell(1);
					if (record.getR26_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR26_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(2);
					if (record.getR26_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR26_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					Cell cellE = row.createCell(4);
					if (record.getR26_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR26_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					Cell cellf = row.createCell(5);
					if (record.getR26_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR26_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					Cell cellH = row.createCell(7);
					if (record.getR26_O5Y_FT() != null) {
						cellH.setCellValue(record.getR26_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					Cell celli = row.createCell(8);
					if (record.getR26_O5Y_HTM() != null) {
						celli.setCellValue(record.getR26_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
					// row27

					row = sheet.getRow(26);

					cellB = row.createCell(1);
					if (record.getR27_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR27_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR27_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR27_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR27_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR27_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR27_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR27_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR27_O5Y_FT() != null) {
						cellH.setCellValue(record.getR27_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR27_O5Y_HTM() != null) {
						celli.setCellValue(record.getR27_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row28

					row = sheet.getRow(27);

					cellB = row.createCell(1);
					if (record.getR28_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR28_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR28_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR28_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR28_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR28_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR28_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR28_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR28_O5Y_FT() != null) {
						cellH.setCellValue(record.getR28_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR28_O5Y_HTM() != null) {
						celli.setCellValue(record.getR28_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row29

					row = sheet.getRow(28);

					cellB = row.createCell(1);
					if (record.getR29_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR29_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR29_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR29_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR29_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR29_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR29_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR29_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR29_O5Y_FT() != null) {
						cellH.setCellValue(record.getR29_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR29_O5Y_HTM() != null) {
						celli.setCellValue(record.getR29_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row30

					row = sheet.getRow(29);

					cellB = row.createCell(1);
					if (record.getR30_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR30_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR30_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR30_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR30_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR30_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR30_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR30_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR30_O5Y_FT() != null) {
						cellH.setCellValue(record.getR30_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR30_O5Y_HTM() != null) {
						celli.setCellValue(record.getR30_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
				}
			}

			startRow = 35;

			if (!dataList4.isEmpty()) {
				for (int i = 0; i < dataList4.size(); i++) {

					BRRS_M_SEC_Summary_Entity4 record = dataList4.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					Cell cellB = row.createCell(1);
					if (record.getR36_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR36_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(2);
					if (record.getR36_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR36_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					Cell cellE = row.createCell(4);
					if (record.getR36_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR36_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					Cell cellf = row.createCell(5);
					if (record.getR36_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR36_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					Cell cellH = row.createCell(7);
					if (record.getR36_O5Y_FT() != null) {
						cellH.setCellValue(record.getR36_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					Cell celli = row.createCell(8);
					if (record.getR36_O5Y_HTM() != null) {
						celli.setCellValue(record.getR36_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
					// row37

					row = sheet.getRow(36);

					cellB = row.createCell(1);
					if (record.getR37_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR37_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR37_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR37_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR37_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR37_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR37_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR37_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR37_O5Y_FT() != null) {
						cellH.setCellValue(record.getR37_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR37_O5Y_HTM() != null) {
						celli.setCellValue(record.getR37_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row38

					row = sheet.getRow(37);

					cellB = row.createCell(1);
					if (record.getR38_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR38_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR38_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR38_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR38_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR38_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR38_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR38_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR38_O5Y_FT() != null) {
						cellH.setCellValue(record.getR38_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR38_O5Y_HTM() != null) {
						celli.setCellValue(record.getR38_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row39

					row = sheet.getRow(38);

					cellB = row.createCell(1);
					if (record.getR39_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR39_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR39_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR39_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR39_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR39_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR39_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR39_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR39_O5Y_FT() != null) {
						cellH.setCellValue(record.getR39_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR39_O5Y_HTM() != null) {
						celli.setCellValue(record.getR39_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);

					cellB = row.createCell(1);
					if (record.getR40_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR40_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR40_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR40_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR40_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR40_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR40_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR40_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR40_O5Y_FT() != null) {
						cellH.setCellValue(record.getR40_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR40_O5Y_HTM() != null) {
						celli.setCellValue(record.getR40_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);

					cellB = row.createCell(1);
					if (record.getR41_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR41_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR41_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR41_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellE = row.createCell(4);
					if (record.getR41_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR41_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR41_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR41_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR41_O5Y_FT() != null) {
						cellH.setCellValue(record.getR41_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR41_O5Y_HTM() != null) {
						celli.setCellValue(record.getR41_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					cellB = row.createCell(1);
					if (record.getR42_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR42_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR42_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR42_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR42_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR42_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR42_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR42_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR42_O5Y_FT() != null) {
						cellH.setCellValue(record.getR42_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR42_O5Y_HTM() != null) {
						celli.setCellValue(record.getR42_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
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

	public byte[] getExcelM_SECARCHIVAL(String filename, String reportId, String fromdate,
            String todate, String currency, String dtltype,
            String type, BigDecimal version) throws Exception {

logger.info("Service: Starting Excel generation process in memory.");

// --- LOAD ARCHIVAL DATA ---
List<BRRS_M_SEC_Archival_Summary_Entity1> dataList1 =
archivalSummaryRepo1.getdatabydateListarchival(dateformat.parse(todate), version);
List<BRRS_M_SEC_Archival_Summary_Entity2> dataList2 =
archivalSummaryRepo2.getdatabydateListarchival(dateformat.parse(todate), version);
List<BRRS_M_SEC_Archival_Summary_Entity3> dataList3 =
archivalSummaryRepo3.getdatabydateListarchival(dateformat.parse(todate), version);
List<BRRS_M_SEC_Archival_Summary_Entity4> dataList4 =
archivalSummaryRepo4.getdatabydateListarchival(dateformat.parse(todate), version);

// --- LOG IF EMPTY BUT DO NOT CANCEL DOWNLOAD ---
if (dataList1.isEmpty()) logger.warn("ARCHIVAL List1 is empty (continuing).");
if (dataList2.isEmpty()) logger.warn("ARCHIVAL List2 is empty (continuing).");
if (dataList3.isEmpty()) logger.warn("ARCHIVAL List3 is empty (continuing).");
if (dataList4.isEmpty()) logger.warn("ARCHIVAL List4 is empty (continuing).");

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

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity1 record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row11
					// Column B2 - Original Amount
					Cell cellB = row.createCell(1);
					if (record.getR11_TCA() != null) {
						cellB.setCellValue(record.getR11_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					cellB = row.createCell(1);
					if (record.getR12_TCA() != null) {
						cellB.setCellValue(record.getR12_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					cellB = row.createCell(1);
					if (record.getR13_TCA() != null) {
						cellB.setCellValue(record.getR13_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					cellB = row.createCell(1);
					if (record.getR14_TCA() != null) {
						cellB.setCellValue(record.getR14_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
					// row15
					row = sheet.getRow(14);
					cellB = row.createCell(1);
					if (record.getR15_TCA() != null) {
						cellB.setCellValue(record.getR15_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					cellB = row.createCell(1);
					if (record.getR16_TCA() != null) {
						cellB.setCellValue(record.getR16_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					cellB = row.createCell(1);
					if (record.getR17_TCA() != null) {
						cellB.setCellValue(record.getR17_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
					// row18
					row = sheet.getRow(17);
					cellB = row.createCell(1);
					if (record.getR18_TCA() != null) {
						cellB.setCellValue(record.getR18_TCA().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
				}
			}

			startRow = 10;

			if (!dataList2.isEmpty()) {
				for (int i = 0; i < dataList2.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity2 record = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row11
					// Column B2 - Original Amount
					Cell cellB = row.createCell(6);
					if (record.getR11_TCA2() != null) {
						cellB.setCellValue(record.getR11_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);

					cellB = row.createCell(6);
					if (record.getR12_TCA2() != null) {
						cellB.setCellValue(record.getR12_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);

					cellB = row.createCell(6);
					if (record.getR13_TCA2() != null) {
						cellB.setCellValue(record.getR13_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					cellB = row.createCell(6);
					if (record.getR14_TCA2() != null) {
						cellB.setCellValue(record.getR14_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					cellB = row.createCell(6);
					if (record.getR15_TCA2() != null) {
						cellB.setCellValue(record.getR15_TCA2().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}
				}
			}

			startRow = 25;

			if (!dataList3.isEmpty()) {
				for (int i = 0; i < dataList3.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity3 record = dataList3.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell cellB = row.createCell(1);
					if (record.getR26_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR26_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(2);
					if (record.getR26_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR26_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					Cell cellE = row.createCell(4);
					if (record.getR26_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR26_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					Cell cellf = row.createCell(5);
					if (record.getR26_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR26_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					Cell cellH = row.createCell(7);
					if (record.getR26_O5Y_FT() != null) {
						cellH.setCellValue(record.getR26_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					Cell celli = row.createCell(8);
					if (record.getR26_O5Y_HTM() != null) {
						celli.setCellValue(record.getR26_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
					// row27

					row = sheet.getRow(26);

					cellB = row.createCell(1);
					if (record.getR27_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR27_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR27_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR27_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR27_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR27_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR27_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR27_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR27_O5Y_FT() != null) {
						cellH.setCellValue(record.getR27_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR27_O5Y_HTM() != null) {
						celli.setCellValue(record.getR27_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);

					cellB = row.createCell(1);
					if (record.getR28_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR28_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR28_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR28_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR28_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR28_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR28_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR28_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR28_O5Y_FT() != null) {
						cellH.setCellValue(record.getR28_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR28_O5Y_HTM() != null) {
						celli.setCellValue(record.getR28_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cellB = row.createCell(1);
					if (record.getR29_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR29_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR29_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR29_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR29_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR29_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR29_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR29_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR29_O5Y_FT() != null) {
						cellH.setCellValue(record.getR29_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR29_O5Y_HTM() != null) {
						celli.setCellValue(record.getR29_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cellB = row.createCell(1);
					if (record.getR30_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR30_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR30_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR30_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR30_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR30_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR30_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR30_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR30_O5Y_FT() != null) {
						cellH.setCellValue(record.getR30_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR30_O5Y_HTM() != null) {
						celli.setCellValue(record.getR30_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
				}
			}

			startRow = 35;

			if (!dataList4.isEmpty()) {
				for (int i = 0; i < dataList4.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity4 record = dataList4.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cellB = row.createCell(1);
					if (record.getR36_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR36_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(2);
					if (record.getR36_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR36_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					Cell cellE = row.createCell(4);
					if (record.getR36_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR36_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					Cell cellf = row.createCell(5);
					if (record.getR36_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR36_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					Cell cellH = row.createCell(7);
					if (record.getR36_O5Y_FT() != null) {
						cellH.setCellValue(record.getR36_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					Cell celli = row.createCell(8);
					if (record.getR36_O5Y_HTM() != null) {
						celli.setCellValue(record.getR36_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}
					// row37

					row = sheet.getRow(36);

					cellB = row.createCell(1);
					if (record.getR37_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR37_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR37_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR37_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR37_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR37_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR37_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR37_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR37_O5Y_FT() != null) {
						cellH.setCellValue(record.getR37_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR37_O5Y_HTM() != null) {
						celli.setCellValue(record.getR37_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row38

					row = sheet.getRow(37);

					cellB = row.createCell(1);
					if (record.getR38_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR38_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR38_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR38_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR38_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR38_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR38_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR38_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR38_O5Y_FT() != null) {
						cellH.setCellValue(record.getR38_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR38_O5Y_HTM() != null) {
						celli.setCellValue(record.getR38_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row39

					row = sheet.getRow(38);

					cellB = row.createCell(1);
					if (record.getR39_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR39_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR39_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR39_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR39_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR39_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR39_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR39_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR39_O5Y_FT() != null) {
						cellH.setCellValue(record.getR39_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR39_O5Y_HTM() != null) {
						celli.setCellValue(record.getR39_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row40
					row = sheet.getRow(39);

					cellB = row.createCell(1);
					if (record.getR40_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR40_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR40_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR40_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR40_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR40_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR40_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR40_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR40_O5Y_FT() != null) {
						cellH.setCellValue(record.getR40_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR40_O5Y_HTM() != null) {
						celli.setCellValue(record.getR40_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);

					cellB = row.createCell(1);
					if (record.getR41_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR41_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR41_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR41_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					cellE = row.createCell(4);
					if (record.getR41_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR41_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR41_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR41_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR41_O5Y_FT() != null) {
						cellH.setCellValue(record.getR41_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR41_O5Y_HTM() != null) {
						celli.setCellValue(record.getR41_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					cellB = row.createCell(1);
					if (record.getR42_0_1Y_FT() != null) {
						cellB.setCellValue(record.getR42_0_1Y_FT().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(2);
					if (record.getR42_0_1Y_HTM() != null) {
						cellC.setCellValue(record.getR42_0_1Y_HTM().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// row11
					// Column D4 - No. of Accounts
					cellE = row.createCell(4);
					if (record.getR42_1_5Y_FT() != null) {
						cellE.setCellValue(record.getR42_1_5Y_FT().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}
					cellf = row.createCell(5);
					if (record.getR42_1_5Y_HTM() != null) {
						cellf.setCellValue(record.getR42_1_5Y_HTM().doubleValue());
						cellf.setCellStyle(numberStyle);
					} else {
						cellf.setCellValue("");
						cellf.setCellStyle(textStyle);
					}

					cellH = row.createCell(7);
					if (record.getR42_O5Y_FT() != null) {
						cellH.setCellValue(record.getR42_O5Y_FT().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					celli = row.createCell(8);
					if (record.getR42_O5Y_HTM() != null) {
						celli.setCellValue(record.getR42_O5Y_HTM().doubleValue());
						celli.setCellStyle(numberStyle);
					} else {
						celli.setCellValue("");
						celli.setCellStyle(textStyle);
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
	
	
//RESUBMISSION
/// Report Date | Report Version | Domain
/// RESUB VIEW
	public List<Object[]> getM_SECResub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<BRRS_M_SEC_Archival_Summary_Entity1> latestArchivalList =archivalSummaryRepo1
	                .getdatabydateListWithVersion();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (BRRS_M_SEC_Archival_Summary_Entity1 entity : latestArchivalList) {
	                resubList.add(new Object[] {
	                        entity.getReportDate(),
	                        entity.getReportVersion()
	                });
	            }
	            System.out.println("Fetched " + resubList.size() + " record(s)");
	        } else {
	            System.out.println("No archival data found.");
	        }

	    } catch (Exception e) {
	        System.err.println("Error fetching M_SEC Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}

	public List<Object[]> getM_SECArchival() {
	    List<Object[]> archivalList = new ArrayList<>();
	    try {
	        List<BRRS_M_SEC_Archival_Summary_Entity1> latestArchivalList =archivalSummaryRepo1
	                .getdatabydateListWithVersion();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (BRRS_M_SEC_Archival_Summary_Entity1 entity : latestArchivalList) {
	                archivalList.add(new Object[] {
	                        entity.getReportDate(),
	                        entity.getReportVersion()
	                });
	            }
	            System.out.println("Fetched " + archivalList.size() + " record(s)");
	        } else {
	            System.out.println("No archival data found.");
	        }

	    } catch (Exception e) {
	        System.err.println("Error fetching M_SEC Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return archivalList;
	}

//	public void updateReportReSub(
//	       BRRS_M_SEC_Summary_Entity1 updatedEntity1,
//	       BRRS_M_SEC_Summary_Entity2 updatedEntity2,
//	       BRRS_M_SEC_Summary_Entity3 updatedEntity3,
//	       BRRS_M_SEC_Summary_Entity4 updatedEntity4) {
//
//	    System.out.println("Came t M_SEC Resub Service");
//	    System.out.println("Report Date: " + updatedEntity1.getReportDate());
//
//	    Date reportDate = updatedEntity1.getReportDate();
//	    int newVersion = 1;
//
//	    try {
//	        // 🔹 Fetch the latest archival version for this report date from Entity1
//	        Optional<BRRS_M_SEC_Archival_Summary_Entity1> latestArchivalOpt1 =archivalSummaryRepo1
//	                .getLatestArchivalVersionByDate(reportDate);
//
//	        if (latestArchivalOpt1.isPresent()) {
//	        	BRRS_M_SEC_Archival_Summary_Entity1 latestArchival = latestArchivalOpt1.get();
//	            try {
//	                newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1;
//	            } catch (NumberFormatException e) {
//	                System.err.println("Invalid version format. Defaulting to version 1");
//	                newVersion = 1;
//	            }
//	        } else {
//	            System.out.println("No previous archival found for date: " + reportDate);
//	        }
//
//	        // 🔹 Prevent duplicate version number in Repo1
//	        boolean exists =archivalSummaryRepo1
//	                .findByReportDateAndReportVersion(reportDate, BigDecimal.valueOf(newVersion))
//	                .isPresent();
//
//	        if (exists) {
//	            throw new RuntimeException("⚠ Version " + newVersion + " already exists for report date " + reportDate);
//	        }
//
//	        // Copy data from summary to archival entities for all 3 entities
//	        BRRS_M_SEC_Archival_Summary_Entity1 archivalEntity1 = new BRRS_M_SEC_Archival_Summary_Entity1();
//	        BRRS_M_SEC_Archival_Summary_Entity2 archivalEntity2 = new BRRS_M_SEC_Archival_Summary_Entity2();
//	        BRRS_M_SEC_Archival_Summary_Entity3 archivalEntity3 = new BRRS_M_SEC_Archival_Summary_Entity3();
//	        BRRS_M_SEC_Archival_Summary_Entity4 archivalEntity4 = new BRRS_M_SEC_Archival_Summary_Entity4();
//	       
//	        org.springframework.beans.BeanUtils.copyProperties(updatedEntity1, archivalEntity1);
//	        org.springframework.beans.BeanUtils.copyProperties(updatedEntity2, archivalEntity2);
//	        org.springframework.beans.BeanUtils.copyProperties(updatedEntity3, archivalEntity3);
//	        org.springframework.beans.BeanUtils.copyProperties(updatedEntity4, archivalEntity4);
//	        
//
//	        // Set common fields
//	        Date now = new Date();
//	        archivalEntity1.setReportDate(reportDate);
//	        archivalEntity2.setReportDate(reportDate);
//	        archivalEntity3.setReportDate(reportDate);
//	        archivalEntity4.setReportDate(reportDate);
//	        
//
//	        archivalEntity1.setReportVersion(BigDecimal.valueOf(newVersion));
//	        archivalEntity2.setReportVersion(BigDecimal.valueOf(newVersion));
//	        archivalEntity3.setReportVersion(BigDecimal.valueOf(newVersion));
//	        archivalEntity4.setReportVersion(BigDecimal.valueOf(newVersion));
//	        
//
//	        archivalEntity1.setReportResubDate(now);
//	        archivalEntity2.setReportResubDate(now);
//	        archivalEntity3.setReportResubDate(now);
//	        archivalEntity4.setReportResubDate(now);
//	        
//
//	        System.out.println("Saving new archival version: " + newVersion);
//
//	        // Save to all three archival repositories
//	        archivalSummaryRepo1.save(archivalEntity1);
//	        archivalSummaryRepo2.save(archivalEntity2);
//	        archivalSummaryRepo3.save(archivalEntity3);
//	        archivalSummaryRepo4.save(archivalEntity4);
//	       
//	        System.out.println("Saved archival version successfully: " + newVersion);
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        throw new RuntimeException("Error while creating M_SEC archival resubmission record", e);
//	    }
//	}

/// Downloaded for Archival & Resub
public byte[] BRRS_M_SECResubExcel(String filename, String reportId, String fromdate,
String todate, String currency, String dtltype,
String type, BigDecimal version) throws Exception {

	 logger.info("Service: Starting Excel generation process in memory for M_SEC RESUB Excel.");

	    Date reportDate = dateformat.parse(todate);

	    // 🔹 If RESUB is called but version missing → throw error
	    if ("RESUB".equalsIgnoreCase(type) && version == null) {
	        throw new RuntimeException("RESUB Excel requested but version is missing.");
	    }

	    // 🔹 If it's NORMAL download, ignore version and take live summary
	    if (!"RESUB".equalsIgnoreCase(type)) {
	        version = null; // ensures archival fetch does NOT filter version
	    }

	    // 🔹 Fetch archival or summary based on RESUB/NORMAL
	    List<BRRS_M_SEC_Archival_Summary_Entity1> dataList1 =
	            archivalSummaryRepo1.getdatabydateListarchival(reportDate, version);
	    List<BRRS_M_SEC_Archival_Summary_Entity2> dataList2 =
	            archivalSummaryRepo2.getdatabydateListarchival(reportDate, version);
	    List<BRRS_M_SEC_Archival_Summary_Entity3> dataList3 =
	            archivalSummaryRepo3.getdatabydateListarchival(reportDate, version);
	    List<BRRS_M_SEC_Archival_Summary_Entity4> dataList4 =
	            archivalSummaryRepo4.getdatabydateListarchival(reportDate, version);

	    // 🔹 Validate all 4 lists
	    if (dataList1.isEmpty() || dataList2.isEmpty() || dataList3.isEmpty() || dataList4.isEmpty()) {
	        logger.warn("Service: No data found for M_SEC Report (Archival or Live). Returning empty result.");
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
int startRow = 10;

if (!dataList1.isEmpty()) {
	for (int i = 0; i < dataList1.size(); i++) {
		BRRS_M_SEC_Archival_Summary_Entity1 record = dataList1.get(i);
		System.out.println("rownumber=" + startRow + i);
		Row row = sheet.getRow(startRow + i);
		if (row == null) {
			row = sheet.createRow(startRow + i);
		}
		// row11
		// Column B2 - Original Amount
		Cell cellB = row.createCell(1);
		if (record.getR11_TCA() != null) {
			cellB.setCellValue(record.getR11_TCA().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// row12
		row = sheet.getRow(11);

		cellB = row.createCell(1);
		if (record.getR12_TCA() != null) {
			cellB.setCellValue(record.getR12_TCA().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// row13
		row = sheet.getRow(12);
		cellB = row.createCell(1);
		if (record.getR13_TCA() != null) {
			cellB.setCellValue(record.getR13_TCA().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// row14
		row = sheet.getRow(13);
		cellB = row.createCell(1);
		if (record.getR14_TCA() != null) {
			cellB.setCellValue(record.getR14_TCA().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}
		// row15
		row = sheet.getRow(14);
		cellB = row.createCell(1);
		if (record.getR15_TCA() != null) {
			cellB.setCellValue(record.getR15_TCA().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// row16
		row = sheet.getRow(15);
		cellB = row.createCell(1);
		if (record.getR16_TCA() != null) {
			cellB.setCellValue(record.getR16_TCA().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		// row17
		row = sheet.getRow(16);
		cellB = row.createCell(1);
		if (record.getR17_TCA() != null) {
			cellB.setCellValue(record.getR17_TCA().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}
		// row18
		row = sheet.getRow(17);
		cellB = row.createCell(1);
		if (record.getR18_TCA() != null) {
			cellB.setCellValue(record.getR18_TCA().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}
	}
}

startRow = 10;

if (!dataList2.isEmpty()) {
	for (int i = 0; i < dataList2.size(); i++) {
		BRRS_M_SEC_Archival_Summary_Entity2 record = dataList2.get(i);
		System.out.println("rownumber=" + startRow + i);
		Row row = sheet.getRow(startRow + i);
		if (row == null) {
			row = sheet.createRow(startRow + i);
		}
		// row11
		// Column B2 - Original Amount
		Cell cellB = row.createCell(6);
		if (record.getR11_TCA2() != null) {
			cellB.setCellValue(record.getR11_TCA2().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		row = sheet.getRow(11);

		cellB = row.createCell(6);
		if (record.getR12_TCA2() != null) {
			cellB.setCellValue(record.getR12_TCA2().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		row = sheet.getRow(12);

		cellB = row.createCell(6);
		if (record.getR13_TCA2() != null) {
			cellB.setCellValue(record.getR13_TCA2().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		row = sheet.getRow(13);

		cellB = row.createCell(6);
		if (record.getR14_TCA2() != null) {
			cellB.setCellValue(record.getR14_TCA2().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		row = sheet.getRow(14);

		cellB = row.createCell(6);
		if (record.getR15_TCA2() != null) {
			cellB.setCellValue(record.getR15_TCA2().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}
	}
}

startRow = 25;

if (!dataList3.isEmpty()) {
	for (int i = 0; i < dataList3.size(); i++) {
		BRRS_M_SEC_Archival_Summary_Entity3 record = dataList3.get(i);
		System.out.println("rownumber=" + startRow + i);
		Row row = sheet.getRow(startRow + i);
		if (row == null) {
			row = sheet.createRow(startRow + i);
		}
		Cell cellB = row.createCell(1);
		if (record.getR26_0_1Y_FT() != null) {
			cellB.setCellValue(record.getR26_0_1Y_FT().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		Cell cellC = row.createCell(2);
		if (record.getR26_0_1Y_HTM() != null) {
			cellC.setCellValue(record.getR26_0_1Y_HTM().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// row11
		// Column D4 - No. of Accounts
		Cell cellE = row.createCell(4);
		if (record.getR26_1_5Y_FT() != null) {
			cellE.setCellValue(record.getR26_1_5Y_FT().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}
		Cell cellf = row.createCell(5);
		if (record.getR26_1_5Y_HTM() != null) {
			cellf.setCellValue(record.getR26_1_5Y_HTM().doubleValue());
			cellf.setCellStyle(numberStyle);
		} else {
			cellf.setCellValue("");
			cellf.setCellStyle(textStyle);
		}

		Cell cellH = row.createCell(7);
		if (record.getR26_O5Y_FT() != null) {
			cellH.setCellValue(record.getR26_O5Y_FT().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		Cell celli = row.createCell(8);
		if (record.getR26_O5Y_HTM() != null) {
			celli.setCellValue(record.getR26_O5Y_HTM().doubleValue());
			celli.setCellStyle(numberStyle);
		} else {
			celli.setCellValue("");
			celli.setCellStyle(textStyle);
		}
		// row27

		row = sheet.getRow(26);

		cellB = row.createCell(1);
		if (record.getR27_0_1Y_FT() != null) {
			cellB.setCellValue(record.getR27_0_1Y_FT().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		cellC = row.createCell(2);
		if (record.getR27_0_1Y_HTM() != null) {
			cellC.setCellValue(record.getR27_0_1Y_HTM().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// row11
		// Column D4 - No. of Accounts
		cellE = row.createCell(4);
		if (record.getR27_1_5Y_FT() != null) {
			cellE.setCellValue(record.getR27_1_5Y_FT().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}
		cellf = row.createCell(5);
		if (record.getR27_1_5Y_HTM() != null) {
			cellf.setCellValue(record.getR27_1_5Y_HTM().doubleValue());
			cellf.setCellStyle(numberStyle);
		} else {
			cellf.setCellValue("");
			cellf.setCellStyle(textStyle);
		}

		cellH = row.createCell(7);
		if (record.getR27_O5Y_FT() != null) {
			cellH.setCellValue(record.getR27_O5Y_FT().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		celli = row.createCell(8);
		if (record.getR27_O5Y_HTM() != null) {
			celli.setCellValue(record.getR27_O5Y_HTM().doubleValue());
			celli.setCellStyle(numberStyle);
		} else {
			celli.setCellValue("");
			celli.setCellStyle(textStyle);
		}

		// row28
		row = sheet.getRow(27);

		cellB = row.createCell(1);
		if (record.getR28_0_1Y_FT() != null) {
			cellB.setCellValue(record.getR28_0_1Y_FT().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		cellC = row.createCell(2);
		if (record.getR28_0_1Y_HTM() != null) {
			cellC.setCellValue(record.getR28_0_1Y_HTM().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// row11
		// Column D4 - No. of Accounts
		cellE = row.createCell(4);
		if (record.getR28_1_5Y_FT() != null) {
			cellE.setCellValue(record.getR28_1_5Y_FT().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}
		cellf = row.createCell(5);
		if (record.getR28_1_5Y_HTM() != null) {
			cellf.setCellValue(record.getR28_1_5Y_HTM().doubleValue());
			cellf.setCellStyle(numberStyle);
		} else {
			cellf.setCellValue("");
			cellf.setCellStyle(textStyle);
		}

		cellH = row.createCell(7);
		if (record.getR28_O5Y_FT() != null) {
			cellH.setCellValue(record.getR28_O5Y_FT().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		celli = row.createCell(8);
		if (record.getR28_O5Y_HTM() != null) {
			celli.setCellValue(record.getR28_O5Y_HTM().doubleValue());
			celli.setCellStyle(numberStyle);
		} else {
			celli.setCellValue("");
			celli.setCellStyle(textStyle);
		}

		// row29
		row = sheet.getRow(28);

		cellB = row.createCell(1);
		if (record.getR29_0_1Y_FT() != null) {
			cellB.setCellValue(record.getR29_0_1Y_FT().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		cellC = row.createCell(2);
		if (record.getR29_0_1Y_HTM() != null) {
			cellC.setCellValue(record.getR29_0_1Y_HTM().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// row11
		// Column D4 - No. of Accounts
		cellE = row.createCell(4);
		if (record.getR29_1_5Y_FT() != null) {
			cellE.setCellValue(record.getR29_1_5Y_FT().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}
		cellf = row.createCell(5);
		if (record.getR29_1_5Y_HTM() != null) {
			cellf.setCellValue(record.getR29_1_5Y_HTM().doubleValue());
			cellf.setCellStyle(numberStyle);
		} else {
			cellf.setCellValue("");
			cellf.setCellStyle(textStyle);
		}

		cellH = row.createCell(7);
		if (record.getR29_O5Y_FT() != null) {
			cellH.setCellValue(record.getR29_O5Y_FT().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		celli = row.createCell(8);
		if (record.getR29_O5Y_HTM() != null) {
			celli.setCellValue(record.getR29_O5Y_HTM().doubleValue());
			celli.setCellStyle(numberStyle);
		} else {
			celli.setCellValue("");
			celli.setCellStyle(textStyle);
		}

		// row30
		row = sheet.getRow(29);

		cellB = row.createCell(1);
		if (record.getR30_0_1Y_FT() != null) {
			cellB.setCellValue(record.getR30_0_1Y_FT().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		cellC = row.createCell(2);
		if (record.getR30_0_1Y_HTM() != null) {
			cellC.setCellValue(record.getR30_0_1Y_HTM().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// row11
		// Column D4 - No. of Accounts
		cellE = row.createCell(4);
		if (record.getR30_1_5Y_FT() != null) {
			cellE.setCellValue(record.getR30_1_5Y_FT().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}
		cellf = row.createCell(5);
		if (record.getR30_1_5Y_HTM() != null) {
			cellf.setCellValue(record.getR30_1_5Y_HTM().doubleValue());
			cellf.setCellStyle(numberStyle);
		} else {
			cellf.setCellValue("");
			cellf.setCellStyle(textStyle);
		}

		cellH = row.createCell(7);
		if (record.getR30_O5Y_FT() != null) {
			cellH.setCellValue(record.getR30_O5Y_FT().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		celli = row.createCell(8);
		if (record.getR30_O5Y_HTM() != null) {
			celli.setCellValue(record.getR30_O5Y_HTM().doubleValue());
			celli.setCellStyle(numberStyle);
		} else {
			celli.setCellValue("");
			celli.setCellStyle(textStyle);
		}
	}
}

startRow = 35;

if (!dataList4.isEmpty()) {
	for (int i = 0; i < dataList4.size(); i++) {
		BRRS_M_SEC_Archival_Summary_Entity4 record = dataList4.get(i);
		System.out.println("rownumber=" + startRow + i);
		Row row = sheet.getRow(startRow + i);
		if (row == null) {
			row = sheet.createRow(startRow + i);
		}

		Cell cellB = row.createCell(1);
		if (record.getR36_0_1Y_FT() != null) {
			cellB.setCellValue(record.getR36_0_1Y_FT().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		Cell cellC = row.createCell(2);
		if (record.getR36_0_1Y_HTM() != null) {
			cellC.setCellValue(record.getR36_0_1Y_HTM().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// row11
		// Column D4 - No. of Accounts
		Cell cellE = row.createCell(4);
		if (record.getR36_1_5Y_FT() != null) {
			cellE.setCellValue(record.getR36_1_5Y_FT().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}
		Cell cellf = row.createCell(5);
		if (record.getR36_1_5Y_HTM() != null) {
			cellf.setCellValue(record.getR36_1_5Y_HTM().doubleValue());
			cellf.setCellStyle(numberStyle);
		} else {
			cellf.setCellValue("");
			cellf.setCellStyle(textStyle);
		}

		Cell cellH = row.createCell(7);
		if (record.getR36_O5Y_FT() != null) {
			cellH.setCellValue(record.getR36_O5Y_FT().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		Cell celli = row.createCell(8);
		if (record.getR36_O5Y_HTM() != null) {
			celli.setCellValue(record.getR36_O5Y_HTM().doubleValue());
			celli.setCellStyle(numberStyle);
		} else {
			celli.setCellValue("");
			celli.setCellStyle(textStyle);
		}
		// row37

		row = sheet.getRow(36);

		cellB = row.createCell(1);
		if (record.getR37_0_1Y_FT() != null) {
			cellB.setCellValue(record.getR37_0_1Y_FT().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		cellC = row.createCell(2);
		if (record.getR37_0_1Y_HTM() != null) {
			cellC.setCellValue(record.getR37_0_1Y_HTM().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// row11
		// Column D4 - No. of Accounts
		cellE = row.createCell(4);
		if (record.getR37_1_5Y_FT() != null) {
			cellE.setCellValue(record.getR37_1_5Y_FT().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}
		cellf = row.createCell(5);
		if (record.getR37_1_5Y_HTM() != null) {
			cellf.setCellValue(record.getR37_1_5Y_HTM().doubleValue());
			cellf.setCellStyle(numberStyle);
		} else {
			cellf.setCellValue("");
			cellf.setCellStyle(textStyle);
		}

		cellH = row.createCell(7);
		if (record.getR37_O5Y_FT() != null) {
			cellH.setCellValue(record.getR37_O5Y_FT().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		celli = row.createCell(8);
		if (record.getR37_O5Y_HTM() != null) {
			celli.setCellValue(record.getR37_O5Y_HTM().doubleValue());
			celli.setCellStyle(numberStyle);
		} else {
			celli.setCellValue("");
			celli.setCellStyle(textStyle);
		}

		// row38

		row = sheet.getRow(37);

		cellB = row.createCell(1);
		if (record.getR38_0_1Y_FT() != null) {
			cellB.setCellValue(record.getR38_0_1Y_FT().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		cellC = row.createCell(2);
		if (record.getR38_0_1Y_HTM() != null) {
			cellC.setCellValue(record.getR38_0_1Y_HTM().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// row11
		// Column D4 - No. of Accounts
		cellE = row.createCell(4);
		if (record.getR38_1_5Y_FT() != null) {
			cellE.setCellValue(record.getR38_1_5Y_FT().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}
		cellf = row.createCell(5);
		if (record.getR38_1_5Y_HTM() != null) {
			cellf.setCellValue(record.getR38_1_5Y_HTM().doubleValue());
			cellf.setCellStyle(numberStyle);
		} else {
			cellf.setCellValue("");
			cellf.setCellStyle(textStyle);
		}

		cellH = row.createCell(7);
		if (record.getR38_O5Y_FT() != null) {
			cellH.setCellValue(record.getR38_O5Y_FT().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		celli = row.createCell(8);
		if (record.getR38_O5Y_HTM() != null) {
			celli.setCellValue(record.getR38_O5Y_HTM().doubleValue());
			celli.setCellStyle(numberStyle);
		} else {
			celli.setCellValue("");
			celli.setCellStyle(textStyle);
		}

		// row39

		row = sheet.getRow(38);

		cellB = row.createCell(1);
		if (record.getR39_0_1Y_FT() != null) {
			cellB.setCellValue(record.getR39_0_1Y_FT().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		cellC = row.createCell(2);
		if (record.getR39_0_1Y_HTM() != null) {
			cellC.setCellValue(record.getR39_0_1Y_HTM().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// row11
		// Column D4 - No. of Accounts
		cellE = row.createCell(4);
		if (record.getR39_1_5Y_FT() != null) {
			cellE.setCellValue(record.getR39_1_5Y_FT().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}
		cellf = row.createCell(5);
		if (record.getR39_1_5Y_HTM() != null) {
			cellf.setCellValue(record.getR39_1_5Y_HTM().doubleValue());
			cellf.setCellStyle(numberStyle);
		} else {
			cellf.setCellValue("");
			cellf.setCellStyle(textStyle);
		}

		cellH = row.createCell(7);
		if (record.getR39_O5Y_FT() != null) {
			cellH.setCellValue(record.getR39_O5Y_FT().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		celli = row.createCell(8);
		if (record.getR39_O5Y_HTM() != null) {
			celli.setCellValue(record.getR39_O5Y_HTM().doubleValue());
			celli.setCellStyle(numberStyle);
		} else {
			celli.setCellValue("");
			celli.setCellStyle(textStyle);
		}

		// row40
		row = sheet.getRow(39);

		cellB = row.createCell(1);
		if (record.getR40_0_1Y_FT() != null) {
			cellB.setCellValue(record.getR40_0_1Y_FT().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		cellC = row.createCell(2);
		if (record.getR40_0_1Y_HTM() != null) {
			cellC.setCellValue(record.getR40_0_1Y_HTM().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// row11
		// Column D4 - No. of Accounts
		cellE = row.createCell(4);
		if (record.getR40_1_5Y_FT() != null) {
			cellE.setCellValue(record.getR40_1_5Y_FT().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}
		cellf = row.createCell(5);
		if (record.getR40_1_5Y_HTM() != null) {
			cellf.setCellValue(record.getR40_1_5Y_HTM().doubleValue());
			cellf.setCellStyle(numberStyle);
		} else {
			cellf.setCellValue("");
			cellf.setCellStyle(textStyle);
		}

		cellH = row.createCell(7);
		if (record.getR40_O5Y_FT() != null) {
			cellH.setCellValue(record.getR40_O5Y_FT().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		celli = row.createCell(8);
		if (record.getR40_O5Y_HTM() != null) {
			celli.setCellValue(record.getR40_O5Y_HTM().doubleValue());
			celli.setCellStyle(numberStyle);
		} else {
			celli.setCellValue("");
			celli.setCellStyle(textStyle);
		}

		// row41
		row = sheet.getRow(40);

		cellB = row.createCell(1);
		if (record.getR41_0_1Y_FT() != null) {
			cellB.setCellValue(record.getR41_0_1Y_FT().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		cellC = row.createCell(2);
		if (record.getR41_0_1Y_HTM() != null) {
			cellC.setCellValue(record.getR41_0_1Y_HTM().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		cellE = row.createCell(4);
		if (record.getR41_1_5Y_FT() != null) {
			cellE.setCellValue(record.getR41_1_5Y_FT().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}
		cellf = row.createCell(5);
		if (record.getR41_1_5Y_HTM() != null) {
			cellf.setCellValue(record.getR41_1_5Y_HTM().doubleValue());
			cellf.setCellStyle(numberStyle);
		} else {
			cellf.setCellValue("");
			cellf.setCellStyle(textStyle);
		}

		cellH = row.createCell(7);
		if (record.getR41_O5Y_FT() != null) {
			cellH.setCellValue(record.getR41_O5Y_FT().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		celli = row.createCell(8);
		if (record.getR41_O5Y_HTM() != null) {
			celli.setCellValue(record.getR41_O5Y_HTM().doubleValue());
			celli.setCellStyle(numberStyle);
		} else {
			celli.setCellValue("");
			celli.setCellStyle(textStyle);
		}

		// row42
		row = sheet.getRow(41);

		cellB = row.createCell(1);
		if (record.getR42_0_1Y_FT() != null) {
			cellB.setCellValue(record.getR42_0_1Y_FT().doubleValue());
			cellB.setCellStyle(numberStyle);
		} else {
			cellB.setCellValue("");
			cellB.setCellStyle(textStyle);
		}

		cellC = row.createCell(2);
		if (record.getR42_0_1Y_HTM() != null) {
			cellC.setCellValue(record.getR42_0_1Y_HTM().doubleValue());
			cellC.setCellStyle(numberStyle);
		} else {
			cellC.setCellValue("");
			cellC.setCellStyle(textStyle);
		}

		// row11
		// Column D4 - No. of Accounts
		cellE = row.createCell(4);
		if (record.getR42_1_5Y_FT() != null) {
			cellE.setCellValue(record.getR42_1_5Y_FT().doubleValue());
			cellE.setCellStyle(numberStyle);
		} else {
			cellE.setCellValue("");
			cellE.setCellStyle(textStyle);
		}
		cellf = row.createCell(5);
		if (record.getR42_1_5Y_HTM() != null) {
			cellf.setCellValue(record.getR42_1_5Y_HTM().doubleValue());
			cellf.setCellStyle(numberStyle);
		} else {
			cellf.setCellValue("");
			cellf.setCellStyle(textStyle);
		}

		cellH = row.createCell(7);
		if (record.getR42_O5Y_FT() != null) {
			cellH.setCellValue(record.getR42_O5Y_FT().doubleValue());
			cellH.setCellStyle(numberStyle);
		} else {
			cellH.setCellValue("");
			cellH.setCellStyle(textStyle);
		}

		celli = row.createCell(8);
		if (record.getR42_O5Y_HTM() != null) {
			celli.setCellValue(record.getR42_O5Y_HTM().doubleValue());
			celli.setCellStyle(numberStyle);
		} else {
			celli.setCellValue("");
			celli.setCellStyle(textStyle);
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




public byte[] BRRS_M_SECEmailExcel(String filename, String reportId, String fromdate, String todate,
		String currency, String dtltype, String type, BigDecimal version) throws Exception {

	logger.info("Service: Starting Excel generation process in memory.");

	

	List<BRRS_M_SEC_Summary_Entity1> dataList1 = secSummaryRepo1.getdatabydateList(dateformat.parse(todate));
	List<BRRS_M_SEC_Summary_Entity2> dataList2 = secSummaryRepo2.getdatabydateList(dateformat.parse(todate));
	List<BRRS_M_SEC_Summary_Entity3> dataList3 = secSummaryRepo3.getdatabydateList(dateformat.parse(todate));
	List<BRRS_M_SEC_Summary_Entity4> dataList4 = secSummaryRepo4.getdatabydateList(dateformat.parse(todate));

	

	if (dataList1.isEmpty()) {
		logger.warn("Service: No data found for M_SEC_email report. Returning empty result.");
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

		int startRow = 9;

		
		
		
		
if (!dataList1.isEmpty()) {
for (int i = 0; i < dataList1.size(); i++) {
	BRRS_M_SEC_Summary_Entity1 record = dataList1.get(i);
	System.out.println("rownumber=" + startRow + i);
	Row row = sheet.getRow(startRow + i);
	if (row == null) {
		row = sheet.createRow(startRow + i);
	}
	// row10
	// Column B2 - Original Amount
	Cell cellB = row.createCell(2);
	if (record.getR11_TCA() != null) {
		cellB.setCellValue(record.getR11_TCA().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}

	// row11
	row = sheet.getRow(10);

	cellB = row.createCell(2);
	if (record.getR12_TCA() != null) {
		cellB.setCellValue(record.getR12_TCA().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}

	// row12
	row = sheet.getRow(11);
	cellB = row.createCell(2);
	if (record.getR13_TCA() != null) {
		cellB.setCellValue(record.getR13_TCA().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}

	// row13
	row = sheet.getRow(12);
	cellB = row.createCell(2);
	if (record.getR14_TCA() != null) {
		cellB.setCellValue(record.getR14_TCA().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}
	
	// row14
	row = sheet.getRow(13);
	cellB = row.createCell(2);
	if (record.getR15_TCA() != null) {
		cellB.setCellValue(record.getR15_TCA().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}

	// row15
	row = sheet.getRow(14);
	cellB = row.createCell(2);
	if (record.getR16_TCA() != null) {
		cellB.setCellValue(record.getR16_TCA().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}

	// row16
	row = sheet.getRow(15);
	cellB = row.createCell(2);
	if (record.getR17_TCA() != null) {
		cellB.setCellValue(record.getR17_TCA().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}
	
	// row17
	row = sheet.getRow(16);
	cellB = row.createCell(2);
	if (record.getR18_TCA() != null) {
		cellB.setCellValue(record.getR18_TCA().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}
	
	// row18
	row = sheet.getRow(17);
	cellB = row.createCell(2);
	if (record.getR19_TCA() != null) {
		cellB.setCellValue(record.getR19_TCA().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}
}
}

startRow = 9;

if (!dataList2.isEmpty()) {
for (int i = 0; i < dataList2.size(); i++) {
	BRRS_M_SEC_Summary_Entity2 record = dataList2.get(i);
	System.out.println("rownumber=" + startRow + i);
	Row row = sheet.getRow(startRow + i);
	if (row == null) {
		row = sheet.createRow(startRow + i);
	}
	// row10
	// Column B2 - Original Amount
	Cell cellB = row.createCell(7);
	if (record.getR11_TCA2() != null) {
		cellB.setCellValue(record.getR11_TCA2().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}

	row = sheet.getRow(10);

	cellB = row.createCell(7);
	if (record.getR12_TCA2() != null) {
		cellB.setCellValue(record.getR12_TCA2().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}

	row = sheet.getRow(11);

	cellB = row.createCell(7);
	if (record.getR13_TCA2() != null) {
		cellB.setCellValue(record.getR13_TCA2().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}

	row = sheet.getRow(12);

	cellB = row.createCell(7);
	if (record.getR14_TCA2() != null) {
		cellB.setCellValue(record.getR14_TCA2().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}

	row = sheet.getRow(13);

	cellB = row.createCell(7);
	if (record.getR15_TCA2() != null) {
		cellB.setCellValue(record.getR15_TCA2().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}
	
	row = sheet.getRow(14);

	cellB = row.createCell(7);
	if (record.getR16_TCA2() != null) {
		cellB.setCellValue(record.getR16_TCA2().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}
}
}

startRow = 24;

if (!dataList3.isEmpty()) {
for (int i = 0; i < dataList3.size(); i++) {
	BRRS_M_SEC_Summary_Entity3 record = dataList3.get(i);
	System.out.println("rownumber=" + startRow + i);
	Row row = sheet.getRow(startRow + i);
	if (row == null) {
		row = sheet.createRow(startRow + i);
	}
	Cell cellB = row.createCell(2);
	if (record.getR26_0_1Y_FT() != null) {
		cellB.setCellValue(record.getR26_0_1Y_FT().doubleValue());
		cellB.setCellStyle(numberStyle);
	} else {
		cellB.setCellValue("");
		cellB.setCellStyle(textStyle);
	}

	Cell cellC = row.createCell(3);
	if (record.getR26_0_1Y_HTM() != null) {
		cellC.setCellValue(record.getR26_0_1Y_HTM().doubleValue());
		cellC.setCellStyle(numberStyle);
	} else {
		cellC.setCellValue("");
		cellC.setCellStyle(textStyle);
	}

	//Total D IN EXCEL E

	Cell cellD = row.createCell(4);
	if (record.getR26_0_1Y_TOTAL() != null) {
		cellD.setCellValue(record.getR26_0_1Y_TOTAL().doubleValue());
		cellD.setCellStyle(numberStyle);
	} else {
		cellD.setCellValue("");
		cellD.setCellStyle(textStyle);
	}
	
	
	// row11
	// Column E - No. of Accounts
	Cell cellE = row.createCell(5);
	if (record.getR26_1_5Y_FT() != null) {
		cellE.setCellValue(record.getR26_1_5Y_FT().doubleValue());
		cellE.setCellStyle(numberStyle);
	} else {
		cellE.setCellValue("");
		cellE.setCellStyle(textStyle);
	}
	Cell cellf = row.createCell(6);
	if (record.getR26_1_5Y_HTM() != null) {
		cellf.setCellValue(record.getR26_1_5Y_HTM().doubleValue());
		cellf.setCellStyle(numberStyle);
	} else {
		cellf.setCellValue("");
		cellf.setCellStyle(textStyle);
	}
	
	//Total G IN EXCEL H

	Cell cellG = row.createCell(7);
	if (record.getR26_1_5Y_TOTAL() != null) {
		cellG.setCellValue(record.getR26_1_5Y_TOTAL().doubleValue());
		cellG.setCellStyle(numberStyle);
	} else {
		cellG.setCellValue("");
		cellG.setCellStyle(textStyle);
	}

//	Cell cellH = row.createCell(8);
//	if (record.getR26_O5Y_FT() != null) {
//		cellH.setCellValue(record.getR26_O5Y_FT().doubleValue());
//		cellH.setCellStyle(numberStyle);
//	} else {
//		cellH.setCellValue("");
//		cellH.setCellStyle(textStyle);
//	}
//
//	Cell celli = row.createCell(9);
//	if (record.getR26_O5Y_HTM() != null) {
//		celli.setCellValue(record.getR26_O5Y_HTM().doubleValue());
//		celli.setCellStyle(numberStyle);
//	} else {
//		celli.setCellValue("");
//		celli.setCellStyle(textStyle);
//	}
//	
//	//J 
//	
//	Cell cellJ = row.createCell(10);
//	if (record.getR26_O5Y_TOTAL() != null) {
//		cellJ.setCellValue(record.getR26_O5Y_TOTAL().doubleValue());
//		cellJ.setCellStyle(numberStyle);
//	} else {
//		cellJ.setCellValue("");
//		cellJ.setCellStyle(textStyle);
//	}
	
	//K
	
	Cell cellK = row.createCell(8);
	if (record.getR26_T_FT() != null) {
		cellK.setCellValue(record.getR26_T_FT().doubleValue());
		cellK.setCellStyle(numberStyle);
	} else {
		cellK.setCellValue("");
		cellK.setCellStyle(textStyle);
	}
	
	//L
	
	Cell cellL = row.createCell(9);
	if (record.getR26_T_HTM() != null) {
		cellL.setCellValue(record.getR26_T_HTM().doubleValue());
		cellL.setCellStyle(numberStyle);
	} else {
		cellL.setCellValue("");
		cellL.setCellStyle(textStyle);
	}
	
	//M
	
	Cell cellM = row.createCell(10);
	if (record.getR26_T_TOTAL() != null) {
		cellM.setCellValue(record.getR26_T_TOTAL().doubleValue());
		cellM.setCellStyle(numberStyle);
	} else {
		cellM.setCellValue("");
		cellM.setCellStyle(textStyle);
	}
	
	// ===================== R27 OLD =====================
	
	// row26

	row = sheet.getRow(25);

	

cellB = row.createCell(2);
if (record.getR27_0_1Y_FT() != null) {
cellB.setCellValue(record.getR27_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

cellC = row.createCell(3);
if (record.getR27_0_1Y_HTM() != null) {
cellC.setCellValue(record.getR27_0_1Y_HTM().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

//Total D → Excel E
cellD = row.createCell(4);
if (record.getR27_0_1Y_TOTAL() != null) {
cellD.setCellValue(record.getR27_0_1Y_TOTAL().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//Column F
cellE = row.createCell(5);
if (record.getR27_1_5Y_FT() != null) {
cellE.setCellValue(record.getR27_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

cellf = row.createCell(6);
if (record.getR27_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR27_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
cellG = row.createCell(7);
if (record.getR27_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR27_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}

//cellH = row.createCell(8);
//if (record.getR27_O5Y_FT() != null) {
//cellH.setCellValue(record.getR27_O5Y_FT().doubleValue());
//cellH.setCellStyle(numberStyle);
//} else {
//cellH.setCellValue("");
//cellH.setCellStyle(textStyle);
//}
//
//celli = row.createCell(9);
//if (record.getR27_O5Y_HTM() != null) {
//celli.setCellValue(record.getR27_O5Y_HTM().doubleValue());
//celli.setCellStyle(numberStyle);
//} else {
//celli.setCellValue("");
//celli.setCellStyle(textStyle);
//}
//
////J
//cellJ = row.createCell(10);
//if (record.getR27_O5Y_TOTAL() != null) {
//cellJ.setCellValue(record.getR27_O5Y_TOTAL().doubleValue());
//cellJ.setCellStyle(numberStyle);
//} else {
//cellJ.setCellValue("");
//cellJ.setCellStyle(textStyle);
//}

//K
cellK = row.createCell(8);
if (record.getR27_T_FT() != null) {
cellK.setCellValue(record.getR27_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}

//L
cellL = row.createCell(9);
if (record.getR27_T_HTM() != null) {
cellL.setCellValue(record.getR27_T_HTM().doubleValue());
cellL.setCellStyle(numberStyle);
} else {
cellL.setCellValue("");
cellL.setCellStyle(textStyle);
}

//M
cellM = row.createCell(10);
if (record.getR27_T_TOTAL() != null) {
cellM.setCellValue(record.getR27_T_TOTAL().doubleValue());
cellM.setCellStyle(numberStyle);
} else {
cellM.setCellValue("");
cellM.setCellStyle(textStyle);
}


//===================== R28 =====================
	// row27
	row = sheet.getRow(26);

	

cellB = row.createCell(2);
if (record.getR28_0_1Y_FT() != null) {
cellB.setCellValue(record.getR28_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

cellC = row.createCell(3);
if (record.getR28_0_1Y_HTM() != null) {
cellC.setCellValue(record.getR28_0_1Y_HTM().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

//Total D → Excel E
cellD = row.createCell(4);
if (record.getR28_0_1Y_TOTAL() != null) {
cellD.setCellValue(record.getR28_0_1Y_TOTAL().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//Column F
cellE = row.createCell(5);
if (record.getR28_1_5Y_FT() != null) {
cellE.setCellValue(record.getR28_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

cellf = row.createCell(6);
if (record.getR28_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR28_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
cellG = row.createCell(7);
if (record.getR28_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR28_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}
//
//cellH = row.createCell(8);
//if (record.getR28_O5Y_FT() != null) {
//cellH.setCellValue(record.getR28_O5Y_FT().doubleValue());
//cellH.setCellStyle(numberStyle);
//} else {
//cellH.setCellValue("");
//cellH.setCellStyle(textStyle);
//}
//
//celli = row.createCell(9);
//if (record.getR28_O5Y_HTM() != null) {
//celli.setCellValue(record.getR28_O5Y_HTM().doubleValue());
//celli.setCellStyle(numberStyle);
//} else {
//celli.setCellValue("");
//celli.setCellStyle(textStyle);
//}
//
////J
//cellJ = row.createCell(10);
//if (record.getR28_O5Y_TOTAL() != null) {
//cellJ.setCellValue(record.getR28_O5Y_TOTAL().doubleValue());
//cellJ.setCellStyle(numberStyle);
//} else {
//cellJ.setCellValue("");
//cellJ.setCellStyle(textStyle);
//}

//K
cellK = row.createCell(8);
if (record.getR28_T_FT() != null) {
cellK.setCellValue(record.getR28_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}

//L
cellL = row.createCell(9);
if (record.getR28_T_HTM() != null) {
cellL.setCellValue(record.getR28_T_HTM().doubleValue());
cellL.setCellStyle(numberStyle);
} else {
cellL.setCellValue("");
cellL.setCellStyle(textStyle);
}

//M
cellM = row.createCell(10);
if (record.getR28_T_TOTAL() != null) {
cellM.setCellValue(record.getR28_T_TOTAL().doubleValue());
cellM.setCellStyle(numberStyle);
} else {
cellM.setCellValue("");
cellM.setCellStyle(textStyle);
}


// ===================== R29 =====================

	// row28
	row = sheet.getRow(27);

cellB = row.createCell(2);
if (record.getR29_0_1Y_FT() != null) {
cellB.setCellValue(record.getR29_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

cellC = row.createCell(3);
if (record.getR29_0_1Y_HTM() != null) {
cellC.setCellValue(record.getR29_0_1Y_HTM().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

//Total D → Excel E
cellD = row.createCell(4);
if (record.getR29_0_1Y_TOTAL() != null) {
cellD.setCellValue(record.getR29_0_1Y_TOTAL().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//Column F
cellE = row.createCell(5);
if (record.getR29_1_5Y_FT() != null) {
cellE.setCellValue(record.getR29_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

cellf = row.createCell(6);
if (record.getR29_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR29_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
cellG = row.createCell(7);
if (record.getR29_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR29_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}

//cellH = row.createCell(8);
//if (record.getR29_O5Y_FT() != null) {
//cellH.setCellValue(record.getR29_O5Y_FT().doubleValue());
//cellH.setCellStyle(numberStyle);
//} else {
//cellH.setCellValue("");
//cellH.setCellStyle(textStyle);
//}
//
//celli = row.createCell(9);
//if (record.getR29_O5Y_HTM() != null) {
//celli.setCellValue(record.getR29_O5Y_HTM().doubleValue());
//celli.setCellStyle(numberStyle);
//} else {
//celli.setCellValue("");
//celli.setCellStyle(textStyle);
//}
//
////J
//cellJ = row.createCell(10);
//if (record.getR29_O5Y_TOTAL() != null) {
//cellJ.setCellValue(record.getR29_O5Y_TOTAL().doubleValue());
//cellJ.setCellStyle(numberStyle);
//} else {
//cellJ.setCellValue("");
//cellJ.setCellStyle(textStyle);
//}

//K
cellK = row.createCell(8);
if (record.getR29_T_FT() != null) {
cellK.setCellValue(record.getR29_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}

//L
cellL = row.createCell(9);
if (record.getR29_T_HTM() != null) {
cellL.setCellValue(record.getR29_T_HTM().doubleValue());
cellL.setCellStyle(numberStyle);
} else {
cellL.setCellValue("");
cellL.setCellStyle(textStyle);
}

//M
cellM = row.createCell(10);
if (record.getR29_T_TOTAL() != null) {
cellM.setCellValue(record.getR29_T_TOTAL().doubleValue());
cellM.setCellStyle(numberStyle);
} else {
cellM.setCellValue("");
cellM.setCellStyle(textStyle);
}

//===================== R30 =====================

	// row29
	row = sheet.getRow(28);

	

cellB = row.createCell(2);
if (record.getR30_0_1Y_FT() != null) {
cellB.setCellValue(record.getR30_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

cellC = row.createCell(3);
if (record.getR30_0_1Y_HTM() != null) {
cellC.setCellValue(record.getR30_0_1Y_HTM().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

//Total D → Excel E
cellD = row.createCell(4);
if (record.getR30_0_1Y_TOTAL() != null) {
cellD.setCellValue(record.getR30_0_1Y_TOTAL().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//Column F
cellE = row.createCell(5);
if (record.getR30_1_5Y_FT() != null) {
cellE.setCellValue(record.getR30_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

cellf = row.createCell(6);
if (record.getR30_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR30_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
cellG = row.createCell(7);
if (record.getR30_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR30_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}

//cellH = row.createCell(8);
//if (record.getR30_O5Y_FT() != null) {
//cellH.setCellValue(record.getR30_O5Y_FT().doubleValue());
//cellH.setCellStyle(numberStyle);
//} else {
//cellH.setCellValue("");
//cellH.setCellStyle(textStyle);
//}
//
//celli = row.createCell(9);
//if (record.getR30_O5Y_HTM() != null) {
//celli.setCellValue(record.getR30_O5Y_HTM().doubleValue());
//celli.setCellStyle(numberStyle);
//} else {
//celli.setCellValue("");
//celli.setCellStyle(textStyle);
//}
//
////J
//cellJ = row.createCell(10);
//if (record.getR30_O5Y_TOTAL() != null) {
//cellJ.setCellValue(record.getR30_O5Y_TOTAL().doubleValue());
//cellJ.setCellStyle(numberStyle);
//} else {
//cellJ.setCellValue("");
//cellJ.setCellStyle(textStyle);
//}

//K
cellK = row.createCell(8);
if (record.getR30_T_FT() != null) {
cellK.setCellValue(record.getR30_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}

//L
cellL = row.createCell(9);
if (record.getR30_T_HTM() != null) {
cellL.setCellValue(record.getR30_T_HTM().doubleValue());
cellL.setCellStyle(numberStyle);
} else {
cellL.setCellValue("");
cellL.setCellStyle(textStyle);
}

//M
cellM = row.createCell(10);
if (record.getR30_T_TOTAL() != null) {
cellM.setCellValue(record.getR30_T_TOTAL().doubleValue());
cellM.setCellStyle(numberStyle);
} else {
cellM.setCellValue("");
cellM.setCellStyle(textStyle);
}

//===================== R31 =====================

//row30
	row = sheet.getRow(29);

cellB = row.createCell(2);
if (record.getR31_0_1Y_FT() != null) {
cellB.setCellValue(record.getR31_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

cellC = row.createCell(3);
if (record.getR31_0_1Y_HTM() != null) {
cellC.setCellValue(record.getR31_0_1Y_HTM().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

//Total D → Excel E
cellD = row.createCell(4);
if (record.getR31_0_1Y_TOTAL() != null) {
cellD.setCellValue(record.getR31_0_1Y_TOTAL().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//Column F
cellE = row.createCell(5);
if (record.getR31_1_5Y_FT() != null) {
cellE.setCellValue(record.getR31_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

cellf = row.createCell(6);
if (record.getR31_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR31_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
cellG = row.createCell(7);
if (record.getR31_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR31_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}

//cellH = row.createCell(8);
//if (record.getR31_O5Y_FT() != null) {
//cellH.setCellValue(record.getR31_O5Y_FT().doubleValue());
//cellH.setCellStyle(numberStyle);
//} else {
//cellH.setCellValue("");
//cellH.setCellStyle(textStyle);
//}
//
//celli = row.createCell(9);
//if (record.getR31_O5Y_HTM() != null) {
//celli.setCellValue(record.getR31_O5Y_HTM().doubleValue());
//celli.setCellStyle(numberStyle);
//} else {
//celli.setCellValue("");
//celli.setCellStyle(textStyle);
//}
//
////J
//cellJ = row.createCell(10);
//if (record.getR31_O5Y_TOTAL() != null) {
//cellJ.setCellValue(record.getR31_O5Y_TOTAL().doubleValue());
//cellJ.setCellStyle(numberStyle);
//} else {
//cellJ.setCellValue("");
//cellJ.setCellStyle(textStyle);
//}

//K
cellK = row.createCell(8);
if (record.getR31_T_FT() != null) {
cellK.setCellValue(record.getR31_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}

//L
cellL = row.createCell(9);
if (record.getR31_T_HTM() != null) {
cellL.setCellValue(record.getR31_T_HTM().doubleValue());
cellL.setCellStyle(numberStyle);
} else {
cellL.setCellValue("");
cellL.setCellStyle(textStyle);
}

//M
cellM = row.createCell(10);
if (record.getR31_T_TOTAL() != null) {
cellM.setCellValue(record.getR31_T_TOTAL().doubleValue());
cellM.setCellStyle(numberStyle);
} else {
cellM.setCellValue("");
cellM.setCellStyle(textStyle);
}





}
}

startRow = 34;

if (!dataList4.isEmpty()) {
for (int i = 0; i < dataList4.size(); i++) {
	BRRS_M_SEC_Summary_Entity4 record = dataList4.get(i);
	System.out.println("rownumber=" + startRow + i);
	Row row = sheet.getRow(startRow + i);
	if (row == null) {
		row = sheet.createRow(startRow + i);
	}

	// ===================== R36 =====================

Cell cellB = row.createCell(2);
if (record.getR36_0_1Y_FT() != null) {
cellB.setCellValue(record.getR36_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

Cell cellC = row.createCell(3);
if (record.getR36_0_1Y_HTM() != null) {
cellC.setCellValue(record.getR36_0_1Y_HTM().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

//Total D → Excel E
Cell cellD = row.createCell(4);
if (record.getR36_0_1Y_TOTAL() != null) {
cellD.setCellValue(record.getR36_0_1Y_TOTAL().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//Column F
Cell cellE = row.createCell(5);
if (record.getR36_1_5Y_FT() != null) {
cellE.setCellValue(record.getR36_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

Cell cellf = row.createCell(6);
if (record.getR36_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR36_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
Cell cellG = row.createCell(7);
if (record.getR36_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR36_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}

//Cell cellH = row.createCell(8);
//if (record.getR36_O5Y_FT() != null) {
//cellH.setCellValue(record.getR36_O5Y_FT().doubleValue());
//cellH.setCellStyle(numberStyle);
//} else {
//cellH.setCellValue("");
//cellH.setCellStyle(textStyle);
//}
//
//Cell celli = row.createCell(9);
//if (record.getR36_O5Y_HTM() != null) {
//celli.setCellValue(record.getR36_O5Y_HTM().doubleValue());
//celli.setCellStyle(numberStyle);
//} else {
//celli.setCellValue("");
//celli.setCellStyle(textStyle);
//}
//
////J
//Cell cellJ = row.createCell(10);
//if (record.getR36_O5Y_TOTAL() != null) {
//cellJ.setCellValue(record.getR36_O5Y_TOTAL().doubleValue());
//cellJ.setCellStyle(numberStyle);
//} else {
//cellJ.setCellValue("");
//cellJ.setCellStyle(textStyle);
//}

//K
Cell cellK = row.createCell(8);
if (record.getR36_T_FT() != null) {
cellK.setCellValue(record.getR36_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}

//L
Cell cellL = row.createCell(9);
if (record.getR36_T_HTM() != null) {
cellL.setCellValue(record.getR36_T_HTM().doubleValue());
cellL.setCellStyle(numberStyle);
} else {
cellL.setCellValue("");
cellL.setCellStyle(textStyle);
}

//M
Cell cellM = row.createCell(10);
if (record.getR36_T_TOTAL() != null) {
cellM.setCellValue(record.getR36_T_TOTAL().doubleValue());
cellM.setCellStyle(numberStyle);
} else {
cellM.setCellValue("");
cellM.setCellStyle(textStyle);
}

// ===================== R37 =====================


	// row36

	row = sheet.getRow(35);



cellB = row.createCell(2);
if (record.getR37_0_1Y_FT() != null) {
cellB.setCellValue(record.getR37_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

cellC = row.createCell(3);
if (record.getR37_0_1Y_HTM() != null) {
cellC.setCellValue(record.getR37_0_1Y_HTM().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

//Total D → Excel E
cellD = row.createCell(4);
if (record.getR37_0_1Y_TOTAL() != null) {
cellD.setCellValue(record.getR37_0_1Y_TOTAL().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//Column F
cellE = row.createCell(5);
if (record.getR37_1_5Y_FT() != null) {
cellE.setCellValue(record.getR37_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

cellf = row.createCell(6);
if (record.getR37_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR37_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
cellG = row.createCell(7);
if (record.getR37_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR37_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}

//cellH = row.createCell(8);
//if (record.getR37_O5Y_FT() != null) {
//cellH.setCellValue(record.getR37_O5Y_FT().doubleValue());
//cellH.setCellStyle(numberStyle);
//} else {
//cellH.setCellValue("");
//cellH.setCellStyle(textStyle);
//}
//
//celli = row.createCell(9);
//if (record.getR37_O5Y_HTM() != null) {
//celli.setCellValue(record.getR37_O5Y_HTM().doubleValue());
//celli.setCellStyle(numberStyle);
//} else {
//celli.setCellValue("");
//celli.setCellStyle(textStyle);
//}
//
////J
//cellJ = row.createCell(10);
//if (record.getR37_O5Y_TOTAL() != null) {
//cellJ.setCellValue(record.getR37_O5Y_TOTAL().doubleValue());
//cellJ.setCellStyle(numberStyle);
//} else {
//cellJ.setCellValue("");
//cellJ.setCellStyle(textStyle);
//}

//K
cellK = row.createCell(8);
if (record.getR37_T_FT() != null) {
cellK.setCellValue(record.getR37_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}

//L
cellL = row.createCell(9);
if (record.getR37_T_HTM() != null) {
cellL.setCellValue(record.getR37_T_HTM().doubleValue());
cellL.setCellStyle(numberStyle);
} else {
cellL.setCellValue("");
cellL.setCellStyle(textStyle);
}

//M
cellM = row.createCell(10);
if (record.getR37_T_TOTAL() != null) {
cellM.setCellValue(record.getR37_T_TOTAL().doubleValue());
cellM.setCellStyle(numberStyle);
} else {
cellM.setCellValue("");
cellM.setCellStyle(textStyle);
}

//===================== R38 =====================

	// row37

	row = sheet.getRow(36);



cellB = row.createCell(2);
if (record.getR38_0_1Y_FT() != null) {
cellB.setCellValue(record.getR38_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

cellC = row.createCell(3);
if (record.getR38_0_1Y_HTM() != null) {
cellC.setCellValue(record.getR38_0_1Y_HTM().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

//Total D → Excel E
cellD = row.createCell(4);
if (record.getR38_0_1Y_TOTAL() != null) {
cellD.setCellValue(record.getR38_0_1Y_TOTAL().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//Column F
cellE = row.createCell(5);
if (record.getR38_1_5Y_FT() != null) {
cellE.setCellValue(record.getR38_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

cellf = row.createCell(6);
if (record.getR38_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR38_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
cellG = row.createCell(7);
if (record.getR38_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR38_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}

//cellH = row.createCell(8);
//if (record.getR38_O5Y_FT() != null) {
//cellH.setCellValue(record.getR38_O5Y_FT().doubleValue());
//cellH.setCellStyle(numberStyle);
//} else {
//cellH.setCellValue("");
//cellH.setCellStyle(textStyle);
//}
//
//celli = row.createCell(9);
//if (record.getR38_O5Y_HTM() != null) {
//celli.setCellValue(record.getR38_O5Y_HTM().doubleValue());
//celli.setCellStyle(numberStyle);
//} else {
//celli.setCellValue("");
//celli.setCellStyle(textStyle);
//}
//
////J
//cellJ = row.createCell(10);
//if (record.getR38_O5Y_TOTAL() != null) {
//cellJ.setCellValue(record.getR38_O5Y_TOTAL().doubleValue());
//cellJ.setCellStyle(numberStyle);
//} else {
//cellJ.setCellValue("");
//cellJ.setCellStyle(textStyle);
//}

//K
cellK = row.createCell(8);
if (record.getR38_T_FT() != null) {
cellK.setCellValue(record.getR38_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}

//L
cellL = row.createCell(9);
if (record.getR38_T_HTM() != null) {
cellL.setCellValue(record.getR38_T_HTM().doubleValue());
cellL.setCellStyle(numberStyle);
} else {
cellL.setCellValue("");
cellL.setCellStyle(textStyle);
}

//M
cellM = row.createCell(10);
if (record.getR38_T_TOTAL() != null) {
cellM.setCellValue(record.getR38_T_TOTAL().doubleValue());
cellM.setCellStyle(numberStyle);
} else {
cellM.setCellValue("");
cellM.setCellStyle(textStyle);
}



//===================== R39 =====================

	// row38

	row = sheet.getRow(37);



cellB = row.createCell(2);
if (record.getR39_0_1Y_FT() != null) {
cellB.setCellValue(record.getR39_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

cellC = row.createCell(3);
if (record.getR39_0_1Y_HTM() != null) {
cellC.setCellValue(record.getR39_0_1Y_HTM().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

//Total D → Excel E
cellD = row.createCell(4);
if (record.getR39_0_1Y_TOTAL() != null) {
cellD.setCellValue(record.getR39_0_1Y_TOTAL().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//Column F
cellE = row.createCell(5);
if (record.getR39_1_5Y_FT() != null) {
cellE.setCellValue(record.getR39_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

cellf = row.createCell(6);
if (record.getR39_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR39_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
cellG = row.createCell(7);
if (record.getR39_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR39_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}

//cellH = row.createCell(8);
//if (record.getR39_O5Y_FT() != null) {
//cellH.setCellValue(record.getR39_O5Y_FT().doubleValue());
//cellH.setCellStyle(numberStyle);
//} else {
//cellH.setCellValue("");
//cellH.setCellStyle(textStyle);
//}
//
//celli = row.createCell(9);
//if (record.getR39_O5Y_HTM() != null) {
//celli.setCellValue(record.getR39_O5Y_HTM().doubleValue());
//celli.setCellStyle(numberStyle);
//} else {
//celli.setCellValue("");
//celli.setCellStyle(textStyle);
//}
//
////J
//cellJ = row.createCell(10);
//if (record.getR39_O5Y_TOTAL() != null) {
//cellJ.setCellValue(record.getR39_O5Y_TOTAL().doubleValue());
//cellJ.setCellStyle(numberStyle);
//} else {
//cellJ.setCellValue("");
//cellJ.setCellStyle(textStyle);
//}

//K
cellK = row.createCell(8);
if (record.getR39_T_FT() != null) {
cellK.setCellValue(record.getR39_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}

//L
cellL = row.createCell(9);
if (record.getR39_T_HTM() != null) {
cellL.setCellValue(record.getR39_T_HTM().doubleValue());
cellL.setCellStyle(numberStyle);
} else {
cellL.setCellValue("");
cellL.setCellStyle(textStyle);
}

//M
cellM = row.createCell(10);
if (record.getR39_T_TOTAL() != null) {
cellM.setCellValue(record.getR39_T_TOTAL().doubleValue());
cellM.setCellStyle(numberStyle);
} else {
cellM.setCellValue("");
cellM.setCellStyle(textStyle);
}
//===================== R40 =====================

	// row39
	row = sheet.getRow(38);

	

cellB = row.createCell(2);
if (record.getR40_0_1Y_FT() != null) {
cellB.setCellValue(record.getR40_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

cellC = row.createCell(3);
if (record.getR40_0_1Y_HTM() != null) {
cellC.setCellValue(record.getR40_0_1Y_HTM().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

//Total D → Excel E
cellD = row.createCell(4);
if (record.getR40_0_1Y_TOTAL() != null) {
cellD.setCellValue(record.getR40_0_1Y_TOTAL().doubleValue());
cellD.setCellStyle(numberStyle);
} else {
cellD.setCellValue("");
cellD.setCellStyle(textStyle);
}

//Column F
cellE = row.createCell(5);
if (record.getR40_1_5Y_FT() != null) {
cellE.setCellValue(record.getR40_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

cellf = row.createCell(6);
if (record.getR40_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR40_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
cellG = row.createCell(7);
if (record.getR40_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR40_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}

//cellH = row.createCell(8);
//if (record.getR40_O5Y_FT() != null) {
//cellH.setCellValue(record.getR40_O5Y_FT().doubleValue());
//cellH.setCellStyle(numberStyle);
//} else {
//cellH.setCellValue("");
//cellH.setCellStyle(textStyle);
//}
//
//celli = row.createCell(9);
//if (record.getR40_O5Y_HTM() != null) {
//celli.setCellValue(record.getR40_O5Y_HTM().doubleValue());
//celli.setCellStyle(numberStyle);
//} else {
//celli.setCellValue("");
//celli.setCellStyle(textStyle);
//}
//
////J
//cellJ = row.createCell(10);
//if (record.getR40_O5Y_TOTAL() != null) {
//cellJ.setCellValue(record.getR40_O5Y_TOTAL().doubleValue());
//cellJ.setCellStyle(numberStyle);
//} else {
//cellJ.setCellValue("");
//cellJ.setCellStyle(textStyle);
//}

//K
cellK = row.createCell(8);
if (record.getR40_T_FT() != null) {
cellK.setCellValue(record.getR40_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}

//L
cellL = row.createCell(9);
if (record.getR40_T_HTM() != null) {
cellL.setCellValue(record.getR40_T_HTM().doubleValue());
cellL.setCellStyle(numberStyle);
} else {
cellL.setCellValue("");
cellL.setCellStyle(textStyle);
}

//M
cellM = row.createCell(10);
if (record.getR40_T_TOTAL() != null) {
cellM.setCellValue(record.getR40_T_TOTAL().doubleValue());
cellM.setCellStyle(numberStyle);
} else {
cellM.setCellValue("");
cellM.setCellStyle(textStyle);
}

//===================== R41 =====================
	// row40
	row = sheet.getRow(39);


cellB = row.createCell(2);
if (record.getR41_0_1Y_FT() != null) {
cellB.setCellValue(record.getR41_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

cellC = row.createCell(3);
if (record.getR41_0_1Y_HTM() != null) {
cellC.setCellValue(record.getR41_0_1Y_HTM().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}


//Total D → Excel E
cellD = row.createCell(4);
if (record.getR41_0_1Y_TOTAL() != null) {
 cellD.setCellValue(record.getR41_0_1Y_TOTAL().doubleValue());
 cellD.setCellStyle(numberStyle);
} else {
 cellD.setCellValue("");
 cellD.setCellStyle(textStyle);
}


//Column F
cellE = row.createCell(5);
if (record.getR41_1_5Y_FT() != null) {
cellE.setCellValue(record.getR41_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

cellf = row.createCell(6);
if (record.getR41_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR41_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
cellG = row.createCell(7);
if (record.getR41_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR41_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}

//cellH = row.createCell(8);
//if (record.getR41_O5Y_FT() != null) {
//cellH.setCellValue(record.getR41_O5Y_FT().doubleValue());
//cellH.setCellStyle(numberStyle);
//} else {
//cellH.setCellValue("");
//cellH.setCellStyle(textStyle);
//}
//
//celli = row.createCell(9);
//if (record.getR41_O5Y_HTM() != null) {
//celli.setCellValue(record.getR41_O5Y_HTM().doubleValue());
//celli.setCellStyle(numberStyle);
//} else {
//celli.setCellValue("");
//celli.setCellStyle(textStyle);
//}
//
////J
//cellJ = row.createCell(10);
//if (record.getR41_O5Y_TOTAL() != null) {
//cellJ.setCellValue(record.getR41_O5Y_TOTAL().doubleValue());
//cellJ.setCellStyle(numberStyle);
//} else {
//cellJ.setCellValue("");
//cellJ.setCellStyle(textStyle);
//}

//K
cellK = row.createCell(8);
if (record.getR41_T_FT() != null) {
cellK.setCellValue(record.getR41_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}

//L
cellL = row.createCell(9);
if (record.getR41_T_HTM() != null) {
 cellL.setCellValue(record.getR41_T_HTM().doubleValue());
 cellL.setCellStyle(numberStyle);
} else {
 cellL.setCellValue("");
 cellL.setCellStyle(textStyle);
}

//M
cellM = row.createCell(10);
if (record.getR41_T_TOTAL() != null) {
 cellM.setCellValue(record.getR41_T_TOTAL().doubleValue());
 cellM.setCellStyle(numberStyle);
} else {
 cellM.setCellValue("");
 cellM.setCellStyle(textStyle);
}





// ===================== R42 =====================
	// row41
	row = sheet.getRow(40);



cellB = row.createCell(2);
if (record.getR42_0_1Y_FT() != null) {
cellB.setCellValue(record.getR42_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}

cellC = row.createCell(3);
if (record.getR42_0_1Y_HTM() != null) {
cellC.setCellValue(record.getR42_0_1Y_HTM().doubleValue());
cellC.setCellStyle(numberStyle);
} else {
cellC.setCellValue("");
cellC.setCellStyle(textStyle);
}

////Total D → Excel E
//cellD = row.createCell(4);
//if (record.getR42_0_1Y_TOTAL() != null) {
//cellD.setCellValue(record.getR42_0_1Y_TOTAL().doubleValue());
//cellD.setCellStyle(numberStyle);
//} else {
//cellD.setCellValue("");
//cellD.setCellStyle(textStyle);
//}

//Column F
cellE = row.createCell(5);
if (record.getR42_1_5Y_FT() != null) {
cellE.setCellValue(record.getR42_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

cellf = row.createCell(6);
if (record.getR42_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR42_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
cellG = row.createCell(7);
if (record.getR42_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR42_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}

//cellH = row.createCell(8);
//if (record.getR42_O5Y_FT() != null) {
//cellH.setCellValue(record.getR42_O5Y_FT().doubleValue());
//cellH.setCellStyle(numberStyle);
//} else {
//cellH.setCellValue("");
//cellH.setCellStyle(textStyle);
//}
//
//celli = row.createCell(9);
//if (record.getR42_O5Y_HTM() != null) {
//celli.setCellValue(record.getR42_O5Y_HTM().doubleValue());
//celli.setCellStyle(numberStyle);
//} else {
//celli.setCellValue("");
//celli.setCellStyle(textStyle);
//}
//
////J
//cellJ = row.createCell(10);
//if (record.getR42_O5Y_TOTAL() != null) {
//cellJ.setCellValue(record.getR42_O5Y_TOTAL().doubleValue());
//cellJ.setCellStyle(numberStyle);
//} else {
//cellJ.setCellValue("");
//cellJ.setCellStyle(textStyle);
//}

//K
cellK = row.createCell(8);
if (record.getR42_T_FT() != null) {
cellK.setCellValue(record.getR42_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}

////L
//cellL = row.createCell(12);
//if (record.getR42_T_HTM() != null) {
//cellL.setCellValue(record.getR42_T_HTM().doubleValue());
//cellL.setCellStyle(numberStyle);
//} else {
//cellL.setCellValue("");
//cellL.setCellStyle(textStyle);
//}
//
////M
//cellM = row.createCell(13);
//if (record.getR42_T_TOTAL() != null) {
//cellM.setCellValue(record.getR42_T_TOTAL().doubleValue());
//cellM.setCellStyle(numberStyle);
//} else {
//cellM.setCellValue("");
//cellM.setCellStyle(textStyle);
//}
//
//				
				
				
			// ===================== R43 =====================
			
			// row42
	row = sheet.getRow(41);

cellB = row.createCell(2);
if (record.getR43_0_1Y_FT() != null) {
cellB.setCellValue(record.getR43_0_1Y_FT().doubleValue());
cellB.setCellStyle(numberStyle);
} else {
cellB.setCellValue("");
cellB.setCellStyle(textStyle);
}





//Column F
cellE = row.createCell(5);
if (record.getR43_1_5Y_FT() != null) {
cellE.setCellValue(record.getR43_1_5Y_FT().doubleValue());
cellE.setCellStyle(numberStyle);
} else {
cellE.setCellValue("");
cellE.setCellStyle(textStyle);
}

cellf = row.createCell(6);
if (record.getR43_1_5Y_HTM() != null) {
cellf.setCellValue(record.getR43_1_5Y_HTM().doubleValue());
cellf.setCellStyle(numberStyle);
} else {
cellf.setCellValue("");
cellf.setCellStyle(textStyle);
}

//Total G → Excel H
cellG = row.createCell(7);
if (record.getR43_1_5Y_TOTAL() != null) {
cellG.setCellValue(record.getR43_1_5Y_TOTAL().doubleValue());
cellG.setCellStyle(numberStyle);
} else {
cellG.setCellValue("");
cellG.setCellStyle(textStyle);
}



//K
cellK = row.createCell(8);
if (record.getR43_T_FT() != null) {
cellK.setCellValue(record.getR43_T_FT().doubleValue());
cellK.setCellStyle(numberStyle);
} else {
cellK.setCellValue("");
cellK.setCellStyle(textStyle);
}


	

		
				
				
					}

			workbook.setForceFormulaRecalculation(true);
		} else {

		}

//Write the final workbook content to the in-memory stream.
		workbook.write(out);

		logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

		return out.toByteArray();
	}

}


public byte[] BRRS_M_SECRCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
		String currency, String dtltype, String type, BigDecimal version) throws Exception {

	logger.info("Service: Starting Excel generation process in memory.");

	if (type.equals("ARCHIVAL") & version != null) {

	}

	List<BRRS_M_SEC_Archival_Summary_Entity1> dataList1 =
			archivalSummaryRepo1.getdatabydateListarchival(dateformat.parse(todate), version);
			List<BRRS_M_SEC_Archival_Summary_Entity2> dataList2 =
			archivalSummaryRepo2.getdatabydateListarchival(dateformat.parse(todate), version);
			List<BRRS_M_SEC_Archival_Summary_Entity3> dataList3 =
			archivalSummaryRepo3.getdatabydateListarchival(dateformat.parse(todate), version);
			List<BRRS_M_SEC_Archival_Summary_Entity4> dataList4 =
			archivalSummaryRepo4.getdatabydateListarchival(dateformat.parse(todate), version);

			// --- LOG IF EMPTY BUT DO NOT CANCEL DOWNLOAD ---
			if (dataList1.isEmpty()) logger.warn("ARCHIVAL List1 is empty (continuing).");
			if (dataList2.isEmpty()) logger.warn("ARCHIVAL List2 is empty (continuing).");
			if (dataList3.isEmpty()) logger.warn("ARCHIVAL List3 is empty (continuing).");
			if (dataList4.isEmpty()) logger.warn("ARCHIVAL List4 is empty (continuing).");

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

		int startRow = 10;

		
				
				
				if (!dataList1.isEmpty()) {
					for (int i = 0; i < dataList1.size(); i++) {
						BRRS_M_SEC_Archival_Summary_Entity1 record = dataList1.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						// row10
						// Column B2 - Original Amount
						Cell cellB = row.createCell(2);
						if (record.getR11_TCA() != null) {
							cellB.setCellValue(record.getR11_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row11
						row = sheet.getRow(10);

						cellB = row.createCell(2);
						if (record.getR12_TCA() != null) {
							cellB.setCellValue(record.getR12_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row12
						row = sheet.getRow(11);
						cellB = row.createCell(2);
						if (record.getR13_TCA() != null) {
							cellB.setCellValue(record.getR13_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row13
						row = sheet.getRow(12);
						cellB = row.createCell(2);
						if (record.getR14_TCA() != null) {
							cellB.setCellValue(record.getR14_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}
						
						// row14
						row = sheet.getRow(13);
						cellB = row.createCell(2);
						if (record.getR15_TCA() != null) {
							cellB.setCellValue(record.getR15_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row15
						row = sheet.getRow(14);
						cellB = row.createCell(2);
						if (record.getR16_TCA() != null) {
							cellB.setCellValue(record.getR16_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						// row16
						row = sheet.getRow(15);
						cellB = row.createCell(2);
						if (record.getR17_TCA() != null) {
							cellB.setCellValue(record.getR17_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}
						
						// row17
						row = sheet.getRow(16);
						cellB = row.createCell(2);
						if (record.getR18_TCA() != null) {
							cellB.setCellValue(record.getR18_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}
						
						// row18
						row = sheet.getRow(17);
						cellB = row.createCell(2);
						if (record.getR19_TCA() != null) {
							cellB.setCellValue(record.getR19_TCA().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}
					}
					}

					startRow = 9;

					if (!dataList2.isEmpty()) {
					for (int i = 0; i < dataList2.size(); i++) {
						BRRS_M_SEC_Archival_Summary_Entity2 record = dataList2.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						// row10
						// Column B2 - Original Amount
						Cell cellB = row.createCell(7);
						if (record.getR11_TCA2() != null) {
							cellB.setCellValue(record.getR11_TCA2().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						row = sheet.getRow(10);

						cellB = row.createCell(7);
						if (record.getR12_TCA2() != null) {
							cellB.setCellValue(record.getR12_TCA2().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						row = sheet.getRow(11);

						cellB = row.createCell(7);
						if (record.getR13_TCA2() != null) {
							cellB.setCellValue(record.getR13_TCA2().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						row = sheet.getRow(12);

						cellB = row.createCell(7);
						if (record.getR14_TCA2() != null) {
							cellB.setCellValue(record.getR14_TCA2().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						row = sheet.getRow(13);

						cellB = row.createCell(7);
						if (record.getR15_TCA2() != null) {
							cellB.setCellValue(record.getR15_TCA2().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}
						
						row = sheet.getRow(14);

						cellB = row.createCell(7);
						if (record.getR16_TCA2() != null) {
							cellB.setCellValue(record.getR16_TCA2().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}
					}
					}

					startRow = 24;

					if (!dataList3.isEmpty()) {
					for (int i = 0; i < dataList3.size(); i++) {
						BRRS_M_SEC_Archival_Summary_Entity3 record = dataList3.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						Cell cellB = row.createCell(2);
						if (record.getR26_0_1Y_FT() != null) {
							cellB.setCellValue(record.getR26_0_1Y_FT().doubleValue());
							cellB.setCellStyle(numberStyle);
						} else {
							cellB.setCellValue("");
							cellB.setCellStyle(textStyle);
						}

						Cell cellC = row.createCell(3);
						if (record.getR26_0_1Y_HTM() != null) {
							cellC.setCellValue(record.getR26_0_1Y_HTM().doubleValue());
							cellC.setCellStyle(numberStyle);
						} else {
							cellC.setCellValue("");
							cellC.setCellStyle(textStyle);
						}

						//Total D IN EXCEL E

						Cell cellD = row.createCell(4);
						if (record.getR26_0_1Y_TOTAL() != null) {
							cellD.setCellValue(record.getR26_0_1Y_TOTAL().doubleValue());
							cellD.setCellStyle(numberStyle);
						} else {
							cellD.setCellValue("");
							cellD.setCellStyle(textStyle);
						}
						
						
						// row11
						// Column E - No. of Accounts
						Cell cellE = row.createCell(5);
						if (record.getR26_1_5Y_FT() != null) {
							cellE.setCellValue(record.getR26_1_5Y_FT().doubleValue());
							cellE.setCellStyle(numberStyle);
						} else {
							cellE.setCellValue("");
							cellE.setCellStyle(textStyle);
						}
						Cell cellf = row.createCell(6);
						if (record.getR26_1_5Y_HTM() != null) {
							cellf.setCellValue(record.getR26_1_5Y_HTM().doubleValue());
							cellf.setCellStyle(numberStyle);
						} else {
							cellf.setCellValue("");
							cellf.setCellStyle(textStyle);
						}
						
						//Total G IN EXCEL H

						Cell cellG = row.createCell(7);
						if (record.getR26_1_5Y_TOTAL() != null) {
							cellG.setCellValue(record.getR26_1_5Y_TOTAL().doubleValue());
							cellG.setCellStyle(numberStyle);
						} else {
							cellG.setCellValue("");
							cellG.setCellStyle(textStyle);
						}

//						Cell cellH = row.createCell(8);
//						if (record.getR26_O5Y_FT() != null) {
//							cellH.setCellValue(record.getR26_O5Y_FT().doubleValue());
//							cellH.setCellStyle(numberStyle);
//						} else {
//							cellH.setCellValue("");
//							cellH.setCellStyle(textStyle);
//						}
					//
//						Cell celli = row.createCell(9);
//						if (record.getR26_O5Y_HTM() != null) {
//							celli.setCellValue(record.getR26_O5Y_HTM().doubleValue());
//							celli.setCellStyle(numberStyle);
//						} else {
//							celli.setCellValue("");
//							celli.setCellStyle(textStyle);
//						}
					//	
//						//J 
					//	
//						Cell cellJ = row.createCell(10);
//						if (record.getR26_O5Y_TOTAL() != null) {
//							cellJ.setCellValue(record.getR26_O5Y_TOTAL().doubleValue());
//							cellJ.setCellStyle(numberStyle);
//						} else {
//							cellJ.setCellValue("");
//							cellJ.setCellStyle(textStyle);
//						}
						
						//K
						
						Cell cellK = row.createCell(8);
						if (record.getR26_T_FT() != null) {
							cellK.setCellValue(record.getR26_T_FT().doubleValue());
							cellK.setCellStyle(numberStyle);
						} else {
							cellK.setCellValue("");
							cellK.setCellStyle(textStyle);
						}
						
						//L
						
						Cell cellL = row.createCell(9);
						if (record.getR26_T_HTM() != null) {
							cellL.setCellValue(record.getR26_T_HTM().doubleValue());
							cellL.setCellStyle(numberStyle);
						} else {
							cellL.setCellValue("");
							cellL.setCellStyle(textStyle);
						}
						
						//M
						
						Cell cellM = row.createCell(10);
						if (record.getR26_T_TOTAL() != null) {
							cellM.setCellValue(record.getR26_T_TOTAL().doubleValue());
							cellM.setCellStyle(numberStyle);
						} else {
							cellM.setCellValue("");
							cellM.setCellStyle(textStyle);
						}
						
						// ===================== R27 OLD =====================
						
						// row26

						row = sheet.getRow(25);

						

					cellB = row.createCell(2);
					if (record.getR27_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR27_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR27_0_1Y_HTM() != null) {
					cellC.setCellValue(record.getR27_0_1Y_HTM().doubleValue());
					cellC.setCellStyle(numberStyle);
					} else {
					cellC.setCellValue("");
					cellC.setCellStyle(textStyle);
					}

					//Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR27_0_1Y_TOTAL() != null) {
					cellD.setCellValue(record.getR27_0_1Y_TOTAL().doubleValue());
					cellD.setCellStyle(numberStyle);
					} else {
					cellD.setCellValue("");
					cellD.setCellStyle(textStyle);
					}

					//Column F
					cellE = row.createCell(5);
					if (record.getR27_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR27_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR27_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR27_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR27_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR27_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}

					//cellH = row.createCell(8);
					//if (record.getR27_O5Y_FT() != null) {
					//cellH.setCellValue(record.getR27_O5Y_FT().doubleValue());
					//cellH.setCellStyle(numberStyle);
					//} else {
					//cellH.setCellValue("");
					//cellH.setCellStyle(textStyle);
					//}
					//
					//celli = row.createCell(9);
					//if (record.getR27_O5Y_HTM() != null) {
					//celli.setCellValue(record.getR27_O5Y_HTM().doubleValue());
					//celli.setCellStyle(numberStyle);
					//} else {
					//celli.setCellValue("");
					//celli.setCellStyle(textStyle);
					//}
					//
					////J
					//cellJ = row.createCell(10);
					//if (record.getR27_O5Y_TOTAL() != null) {
					//cellJ.setCellValue(record.getR27_O5Y_TOTAL().doubleValue());
					//cellJ.setCellStyle(numberStyle);
					//} else {
					//cellJ.setCellValue("");
					//cellJ.setCellStyle(textStyle);
					//}

					//K
					cellK = row.createCell(8);
					if (record.getR27_T_FT() != null) {
					cellK.setCellValue(record.getR27_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

					//L
					cellL = row.createCell(9);
					if (record.getR27_T_HTM() != null) {
					cellL.setCellValue(record.getR27_T_HTM().doubleValue());
					cellL.setCellStyle(numberStyle);
					} else {
					cellL.setCellValue("");
					cellL.setCellStyle(textStyle);
					}

					//M
					cellM = row.createCell(10);
					if (record.getR27_T_TOTAL() != null) {
					cellM.setCellValue(record.getR27_T_TOTAL().doubleValue());
					cellM.setCellStyle(numberStyle);
					} else {
					cellM.setCellValue("");
					cellM.setCellStyle(textStyle);
					}


					//===================== R28 =====================
						// row27
						row = sheet.getRow(26);

						

					cellB = row.createCell(2);
					if (record.getR28_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR28_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR28_0_1Y_HTM() != null) {
					cellC.setCellValue(record.getR28_0_1Y_HTM().doubleValue());
					cellC.setCellStyle(numberStyle);
					} else {
					cellC.setCellValue("");
					cellC.setCellStyle(textStyle);
					}

					//Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR28_0_1Y_TOTAL() != null) {
					cellD.setCellValue(record.getR28_0_1Y_TOTAL().doubleValue());
					cellD.setCellStyle(numberStyle);
					} else {
					cellD.setCellValue("");
					cellD.setCellStyle(textStyle);
					}

					//Column F
					cellE = row.createCell(5);
					if (record.getR28_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR28_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR28_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR28_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR28_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR28_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}
					//
					//cellH = row.createCell(8);
					//if (record.getR28_O5Y_FT() != null) {
					//cellH.setCellValue(record.getR28_O5Y_FT().doubleValue());
					//cellH.setCellStyle(numberStyle);
					//} else {
					//cellH.setCellValue("");
					//cellH.setCellStyle(textStyle);
					//}
					//
					//celli = row.createCell(9);
					//if (record.getR28_O5Y_HTM() != null) {
					//celli.setCellValue(record.getR28_O5Y_HTM().doubleValue());
					//celli.setCellStyle(numberStyle);
					//} else {
					//celli.setCellValue("");
					//celli.setCellStyle(textStyle);
					//}
					//
					////J
					//cellJ = row.createCell(10);
					//if (record.getR28_O5Y_TOTAL() != null) {
					//cellJ.setCellValue(record.getR28_O5Y_TOTAL().doubleValue());
					//cellJ.setCellStyle(numberStyle);
					//} else {
					//cellJ.setCellValue("");
					//cellJ.setCellStyle(textStyle);
					//}

					//K
					cellK = row.createCell(8);
					if (record.getR28_T_FT() != null) {
					cellK.setCellValue(record.getR28_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

					//L
					cellL = row.createCell(9);
					if (record.getR28_T_HTM() != null) {
					cellL.setCellValue(record.getR28_T_HTM().doubleValue());
					cellL.setCellStyle(numberStyle);
					} else {
					cellL.setCellValue("");
					cellL.setCellStyle(textStyle);
					}

					//M
					cellM = row.createCell(10);
					if (record.getR28_T_TOTAL() != null) {
					cellM.setCellValue(record.getR28_T_TOTAL().doubleValue());
					cellM.setCellStyle(numberStyle);
					} else {
					cellM.setCellValue("");
					cellM.setCellStyle(textStyle);
					}


					// ===================== R29 =====================

						// row28
						row = sheet.getRow(27);

					cellB = row.createCell(2);
					if (record.getR29_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR29_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR29_0_1Y_HTM() != null) {
					cellC.setCellValue(record.getR29_0_1Y_HTM().doubleValue());
					cellC.setCellStyle(numberStyle);
					} else {
					cellC.setCellValue("");
					cellC.setCellStyle(textStyle);
					}

					//Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR29_0_1Y_TOTAL() != null) {
					cellD.setCellValue(record.getR29_0_1Y_TOTAL().doubleValue());
					cellD.setCellStyle(numberStyle);
					} else {
					cellD.setCellValue("");
					cellD.setCellStyle(textStyle);
					}

					//Column F
					cellE = row.createCell(5);
					if (record.getR29_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR29_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR29_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR29_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR29_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR29_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}

					//cellH = row.createCell(8);
					//if (record.getR29_O5Y_FT() != null) {
					//cellH.setCellValue(record.getR29_O5Y_FT().doubleValue());
					//cellH.setCellStyle(numberStyle);
					//} else {
					//cellH.setCellValue("");
					//cellH.setCellStyle(textStyle);
					//}
					//
					//celli = row.createCell(9);
					//if (record.getR29_O5Y_HTM() != null) {
					//celli.setCellValue(record.getR29_O5Y_HTM().doubleValue());
					//celli.setCellStyle(numberStyle);
					//} else {
					//celli.setCellValue("");
					//celli.setCellStyle(textStyle);
					//}
					//
					////J
					//cellJ = row.createCell(10);
					//if (record.getR29_O5Y_TOTAL() != null) {
					//cellJ.setCellValue(record.getR29_O5Y_TOTAL().doubleValue());
					//cellJ.setCellStyle(numberStyle);
					//} else {
					//cellJ.setCellValue("");
					//cellJ.setCellStyle(textStyle);
					//}

					//K
					cellK = row.createCell(8);
					if (record.getR29_T_FT() != null) {
					cellK.setCellValue(record.getR29_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

					//L
					cellL = row.createCell(9);
					if (record.getR29_T_HTM() != null) {
					cellL.setCellValue(record.getR29_T_HTM().doubleValue());
					cellL.setCellStyle(numberStyle);
					} else {
					cellL.setCellValue("");
					cellL.setCellStyle(textStyle);
					}

					//M
					cellM = row.createCell(10);
					if (record.getR29_T_TOTAL() != null) {
					cellM.setCellValue(record.getR29_T_TOTAL().doubleValue());
					cellM.setCellStyle(numberStyle);
					} else {
					cellM.setCellValue("");
					cellM.setCellStyle(textStyle);
					}

					//===================== R30 =====================

						// row29
						row = sheet.getRow(28);

						

					cellB = row.createCell(2);
					if (record.getR30_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR30_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR30_0_1Y_HTM() != null) {
					cellC.setCellValue(record.getR30_0_1Y_HTM().doubleValue());
					cellC.setCellStyle(numberStyle);
					} else {
					cellC.setCellValue("");
					cellC.setCellStyle(textStyle);
					}

					//Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR30_0_1Y_TOTAL() != null) {
					cellD.setCellValue(record.getR30_0_1Y_TOTAL().doubleValue());
					cellD.setCellStyle(numberStyle);
					} else {
					cellD.setCellValue("");
					cellD.setCellStyle(textStyle);
					}

					//Column F
					cellE = row.createCell(5);
					if (record.getR30_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR30_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR30_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR30_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR30_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR30_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}

					//cellH = row.createCell(8);
					//if (record.getR30_O5Y_FT() != null) {
					//cellH.setCellValue(record.getR30_O5Y_FT().doubleValue());
					//cellH.setCellStyle(numberStyle);
					//} else {
					//cellH.setCellValue("");
					//cellH.setCellStyle(textStyle);
					//}
					//
					//celli = row.createCell(9);
					//if (record.getR30_O5Y_HTM() != null) {
					//celli.setCellValue(record.getR30_O5Y_HTM().doubleValue());
					//celli.setCellStyle(numberStyle);
					//} else {
					//celli.setCellValue("");
					//celli.setCellStyle(textStyle);
					//}
					//
					////J
					//cellJ = row.createCell(10);
					//if (record.getR30_O5Y_TOTAL() != null) {
					//cellJ.setCellValue(record.getR30_O5Y_TOTAL().doubleValue());
					//cellJ.setCellStyle(numberStyle);
					//} else {
					//cellJ.setCellValue("");
					//cellJ.setCellStyle(textStyle);
					//}

					//K
					cellK = row.createCell(8);
					if (record.getR30_T_FT() != null) {
					cellK.setCellValue(record.getR30_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

					//L
					cellL = row.createCell(9);
					if (record.getR30_T_HTM() != null) {
					cellL.setCellValue(record.getR30_T_HTM().doubleValue());
					cellL.setCellStyle(numberStyle);
					} else {
					cellL.setCellValue("");
					cellL.setCellStyle(textStyle);
					}

					//M
					cellM = row.createCell(10);
					if (record.getR30_T_TOTAL() != null) {
					cellM.setCellValue(record.getR30_T_TOTAL().doubleValue());
					cellM.setCellStyle(numberStyle);
					} else {
					cellM.setCellValue("");
					cellM.setCellStyle(textStyle);
					}

					//===================== R31 =====================

					//row30
						row = sheet.getRow(29);

					cellB = row.createCell(2);
					if (record.getR31_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR31_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR31_0_1Y_HTM() != null) {
					cellC.setCellValue(record.getR31_0_1Y_HTM().doubleValue());
					cellC.setCellStyle(numberStyle);
					} else {
					cellC.setCellValue("");
					cellC.setCellStyle(textStyle);
					}

					//Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR31_0_1Y_TOTAL() != null) {
					cellD.setCellValue(record.getR31_0_1Y_TOTAL().doubleValue());
					cellD.setCellStyle(numberStyle);
					} else {
					cellD.setCellValue("");
					cellD.setCellStyle(textStyle);
					}

					//Column F
					cellE = row.createCell(5);
					if (record.getR31_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR31_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR31_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR31_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR31_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR31_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}

					//cellH = row.createCell(8);
					//if (record.getR31_O5Y_FT() != null) {
					//cellH.setCellValue(record.getR31_O5Y_FT().doubleValue());
					//cellH.setCellStyle(numberStyle);
					//} else {
					//cellH.setCellValue("");
					//cellH.setCellStyle(textStyle);
					//}
					//
					//celli = row.createCell(9);
					//if (record.getR31_O5Y_HTM() != null) {
					//celli.setCellValue(record.getR31_O5Y_HTM().doubleValue());
					//celli.setCellStyle(numberStyle);
					//} else {
					//celli.setCellValue("");
					//celli.setCellStyle(textStyle);
					//}
					//
					////J
					//cellJ = row.createCell(10);
					//if (record.getR31_O5Y_TOTAL() != null) {
					//cellJ.setCellValue(record.getR31_O5Y_TOTAL().doubleValue());
					//cellJ.setCellStyle(numberStyle);
					//} else {
					//cellJ.setCellValue("");
					//cellJ.setCellStyle(textStyle);
					//}

					//K
					cellK = row.createCell(8);
					if (record.getR31_T_FT() != null) {
					cellK.setCellValue(record.getR31_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

					//L
					cellL = row.createCell(9);
					if (record.getR31_T_HTM() != null) {
					cellL.setCellValue(record.getR31_T_HTM().doubleValue());
					cellL.setCellStyle(numberStyle);
					} else {
					cellL.setCellValue("");
					cellL.setCellStyle(textStyle);
					}

					//M
					cellM = row.createCell(10);
					if (record.getR31_T_TOTAL() != null) {
					cellM.setCellValue(record.getR31_T_TOTAL().doubleValue());
					cellM.setCellStyle(numberStyle);
					} else {
					cellM.setCellValue("");
					cellM.setCellStyle(textStyle);
					}


					}
					}

					startRow = 34;

					if (!dataList4.isEmpty()) {
					for (int i = 0; i < dataList4.size(); i++) {
						BRRS_M_SEC_Archival_Summary_Entity4 record = dataList4.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// ===================== R36 =====================

					Cell cellB = row.createCell(2);
					if (record.getR36_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR36_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}

					Cell cellC = row.createCell(3);
					if (record.getR36_0_1Y_HTM() != null) {
					cellC.setCellValue(record.getR36_0_1Y_HTM().doubleValue());
					cellC.setCellStyle(numberStyle);
					} else {
					cellC.setCellValue("");
					cellC.setCellStyle(textStyle);
					}

					//Total D → Excel E
					Cell cellD = row.createCell(4);
					if (record.getR36_0_1Y_TOTAL() != null) {
					cellD.setCellValue(record.getR36_0_1Y_TOTAL().doubleValue());
					cellD.setCellStyle(numberStyle);
					} else {
					cellD.setCellValue("");
					cellD.setCellStyle(textStyle);
					}

					//Column F
					Cell cellE = row.createCell(5);
					if (record.getR36_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR36_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					Cell cellf = row.createCell(6);
					if (record.getR36_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR36_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					Cell cellG = row.createCell(7);
					if (record.getR36_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR36_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}

					//Cell cellH = row.createCell(8);
					//if (record.getR36_O5Y_FT() != null) {
					//cellH.setCellValue(record.getR36_O5Y_FT().doubleValue());
					//cellH.setCellStyle(numberStyle);
					//} else {
					//cellH.setCellValue("");
					//cellH.setCellStyle(textStyle);
					//}
					//
					//Cell celli = row.createCell(9);
					//if (record.getR36_O5Y_HTM() != null) {
					//celli.setCellValue(record.getR36_O5Y_HTM().doubleValue());
					//celli.setCellStyle(numberStyle);
					//} else {
					//celli.setCellValue("");
					//celli.setCellStyle(textStyle);
					//}
					//
					////J
					//Cell cellJ = row.createCell(10);
					//if (record.getR36_O5Y_TOTAL() != null) {
					//cellJ.setCellValue(record.getR36_O5Y_TOTAL().doubleValue());
					//cellJ.setCellStyle(numberStyle);
					//} else {
					//cellJ.setCellValue("");
					//cellJ.setCellStyle(textStyle);
					//}

					//K
					Cell cellK = row.createCell(8);
					if (record.getR36_T_FT() != null) {
					cellK.setCellValue(record.getR36_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

					//L
					Cell cellL = row.createCell(9);
					if (record.getR36_T_HTM() != null) {
					cellL.setCellValue(record.getR36_T_HTM().doubleValue());
					cellL.setCellStyle(numberStyle);
					} else {
					cellL.setCellValue("");
					cellL.setCellStyle(textStyle);
					}

					//M
					Cell cellM = row.createCell(10);
					if (record.getR36_T_TOTAL() != null) {
					cellM.setCellValue(record.getR36_T_TOTAL().doubleValue());
					cellM.setCellStyle(numberStyle);
					} else {
					cellM.setCellValue("");
					cellM.setCellStyle(textStyle);
					}

					// ===================== R37 =====================


						// row36

						row = sheet.getRow(35);



					cellB = row.createCell(2);
					if (record.getR37_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR37_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR37_0_1Y_HTM() != null) {
					cellC.setCellValue(record.getR37_0_1Y_HTM().doubleValue());
					cellC.setCellStyle(numberStyle);
					} else {
					cellC.setCellValue("");
					cellC.setCellStyle(textStyle);
					}

					//Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR37_0_1Y_TOTAL() != null) {
					cellD.setCellValue(record.getR37_0_1Y_TOTAL().doubleValue());
					cellD.setCellStyle(numberStyle);
					} else {
					cellD.setCellValue("");
					cellD.setCellStyle(textStyle);
					}

					//Column F
					cellE = row.createCell(5);
					if (record.getR37_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR37_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR37_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR37_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR37_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR37_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}

					//cellH = row.createCell(8);
					//if (record.getR37_O5Y_FT() != null) {
					//cellH.setCellValue(record.getR37_O5Y_FT().doubleValue());
					//cellH.setCellStyle(numberStyle);
					//} else {
					//cellH.setCellValue("");
					//cellH.setCellStyle(textStyle);
					//}
					//
					//celli = row.createCell(9);
					//if (record.getR37_O5Y_HTM() != null) {
					//celli.setCellValue(record.getR37_O5Y_HTM().doubleValue());
					//celli.setCellStyle(numberStyle);
					//} else {
					//celli.setCellValue("");
					//celli.setCellStyle(textStyle);
					//}
					//
					////J
					//cellJ = row.createCell(10);
					//if (record.getR37_O5Y_TOTAL() != null) {
					//cellJ.setCellValue(record.getR37_O5Y_TOTAL().doubleValue());
					//cellJ.setCellStyle(numberStyle);
					//} else {
					//cellJ.setCellValue("");
					//cellJ.setCellStyle(textStyle);
					//}

					//K
					cellK = row.createCell(8);
					if (record.getR37_T_FT() != null) {
					cellK.setCellValue(record.getR37_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

					//L
					cellL = row.createCell(9);
					if (record.getR37_T_HTM() != null) {
					cellL.setCellValue(record.getR37_T_HTM().doubleValue());
					cellL.setCellStyle(numberStyle);
					} else {
					cellL.setCellValue("");
					cellL.setCellStyle(textStyle);
					}

					//M
					cellM = row.createCell(10);
					if (record.getR37_T_TOTAL() != null) {
					cellM.setCellValue(record.getR37_T_TOTAL().doubleValue());
					cellM.setCellStyle(numberStyle);
					} else {
					cellM.setCellValue("");
					cellM.setCellStyle(textStyle);
					}

					//===================== R38 =====================

						// row37

						row = sheet.getRow(36);



					cellB = row.createCell(2);
					if (record.getR38_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR38_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR38_0_1Y_HTM() != null) {
					cellC.setCellValue(record.getR38_0_1Y_HTM().doubleValue());
					cellC.setCellStyle(numberStyle);
					} else {
					cellC.setCellValue("");
					cellC.setCellStyle(textStyle);
					}

					//Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR38_0_1Y_TOTAL() != null) {
					cellD.setCellValue(record.getR38_0_1Y_TOTAL().doubleValue());
					cellD.setCellStyle(numberStyle);
					} else {
					cellD.setCellValue("");
					cellD.setCellStyle(textStyle);
					}

					//Column F
					cellE = row.createCell(5);
					if (record.getR38_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR38_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR38_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR38_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR38_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR38_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}

					//cellH = row.createCell(8);
					//if (record.getR38_O5Y_FT() != null) {
					//cellH.setCellValue(record.getR38_O5Y_FT().doubleValue());
					//cellH.setCellStyle(numberStyle);
					//} else {
					//cellH.setCellValue("");
					//cellH.setCellStyle(textStyle);
					//}
					//
					//celli = row.createCell(9);
					//if (record.getR38_O5Y_HTM() != null) {
					//celli.setCellValue(record.getR38_O5Y_HTM().doubleValue());
					//celli.setCellStyle(numberStyle);
					//} else {
					//celli.setCellValue("");
					//celli.setCellStyle(textStyle);
					//}
					//
					////J
					//cellJ = row.createCell(10);
					//if (record.getR38_O5Y_TOTAL() != null) {
					//cellJ.setCellValue(record.getR38_O5Y_TOTAL().doubleValue());
					//cellJ.setCellStyle(numberStyle);
					//} else {
					//cellJ.setCellValue("");
					//cellJ.setCellStyle(textStyle);
					//}

					//K
					cellK = row.createCell(8);
					if (record.getR38_T_FT() != null) {
					cellK.setCellValue(record.getR38_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

					//L
					cellL = row.createCell(9);
					if (record.getR38_T_HTM() != null) {
					cellL.setCellValue(record.getR38_T_HTM().doubleValue());
					cellL.setCellStyle(numberStyle);
					} else {
					cellL.setCellValue("");
					cellL.setCellStyle(textStyle);
					}

					//M
					cellM = row.createCell(10);
					if (record.getR38_T_TOTAL() != null) {
					cellM.setCellValue(record.getR38_T_TOTAL().doubleValue());
					cellM.setCellStyle(numberStyle);
					} else {
					cellM.setCellValue("");
					cellM.setCellStyle(textStyle);
					}



					//===================== R39 =====================

						// row38

						row = sheet.getRow(37);



					cellB = row.createCell(2);
					if (record.getR39_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR39_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR39_0_1Y_HTM() != null) {
					cellC.setCellValue(record.getR39_0_1Y_HTM().doubleValue());
					cellC.setCellStyle(numberStyle);
					} else {
					cellC.setCellValue("");
					cellC.setCellStyle(textStyle);
					}

					//Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR39_0_1Y_TOTAL() != null) {
					cellD.setCellValue(record.getR39_0_1Y_TOTAL().doubleValue());
					cellD.setCellStyle(numberStyle);
					} else {
					cellD.setCellValue("");
					cellD.setCellStyle(textStyle);
					}

					//Column F
					cellE = row.createCell(5);
					if (record.getR39_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR39_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR39_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR39_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR39_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR39_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}

					//cellH = row.createCell(8);
					//if (record.getR39_O5Y_FT() != null) {
					//cellH.setCellValue(record.getR39_O5Y_FT().doubleValue());
					//cellH.setCellStyle(numberStyle);
					//} else {
					//cellH.setCellValue("");
					//cellH.setCellStyle(textStyle);
					//}
					//
					//celli = row.createCell(9);
					//if (record.getR39_O5Y_HTM() != null) {
					//celli.setCellValue(record.getR39_O5Y_HTM().doubleValue());
					//celli.setCellStyle(numberStyle);
					//} else {
					//celli.setCellValue("");
					//celli.setCellStyle(textStyle);
					//}
					//
					////J
					//cellJ = row.createCell(10);
					//if (record.getR39_O5Y_TOTAL() != null) {
					//cellJ.setCellValue(record.getR39_O5Y_TOTAL().doubleValue());
					//cellJ.setCellStyle(numberStyle);
					//} else {
					//cellJ.setCellValue("");
					//cellJ.setCellStyle(textStyle);
					//}

					//K
					cellK = row.createCell(8);
					if (record.getR39_T_FT() != null) {
					cellK.setCellValue(record.getR39_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

					//L
					cellL = row.createCell(9);
					if (record.getR39_T_HTM() != null) {
					cellL.setCellValue(record.getR39_T_HTM().doubleValue());
					cellL.setCellStyle(numberStyle);
					} else {
					cellL.setCellValue("");
					cellL.setCellStyle(textStyle);
					}

					//M
					cellM = row.createCell(10);
					if (record.getR39_T_TOTAL() != null) {
					cellM.setCellValue(record.getR39_T_TOTAL().doubleValue());
					cellM.setCellStyle(numberStyle);
					} else {
					cellM.setCellValue("");
					cellM.setCellStyle(textStyle);
					}
					//===================== R40 =====================

						// row39
						row = sheet.getRow(38);

						

					cellB = row.createCell(2);
					if (record.getR40_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR40_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR40_0_1Y_HTM() != null) {
					cellC.setCellValue(record.getR40_0_1Y_HTM().doubleValue());
					cellC.setCellStyle(numberStyle);
					} else {
					cellC.setCellValue("");
					cellC.setCellStyle(textStyle);
					}

					//Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR40_0_1Y_TOTAL() != null) {
					cellD.setCellValue(record.getR40_0_1Y_TOTAL().doubleValue());
					cellD.setCellStyle(numberStyle);
					} else {
					cellD.setCellValue("");
					cellD.setCellStyle(textStyle);
					}

					//Column F
					cellE = row.createCell(5);
					if (record.getR40_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR40_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR40_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR40_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR40_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR40_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}

					//cellH = row.createCell(8);
					//if (record.getR40_O5Y_FT() != null) {
					//cellH.setCellValue(record.getR40_O5Y_FT().doubleValue());
					//cellH.setCellStyle(numberStyle);
					//} else {
					//cellH.setCellValue("");
					//cellH.setCellStyle(textStyle);
					//}
					//
					//celli = row.createCell(9);
					//if (record.getR40_O5Y_HTM() != null) {
					//celli.setCellValue(record.getR40_O5Y_HTM().doubleValue());
					//celli.setCellStyle(numberStyle);
					//} else {
					//celli.setCellValue("");
					//celli.setCellStyle(textStyle);
					//}
					//
					////J
					//cellJ = row.createCell(10);
					//if (record.getR40_O5Y_TOTAL() != null) {
					//cellJ.setCellValue(record.getR40_O5Y_TOTAL().doubleValue());
					//cellJ.setCellStyle(numberStyle);
					//} else {
					//cellJ.setCellValue("");
					//cellJ.setCellStyle(textStyle);
					//}

					//K
					cellK = row.createCell(8);
					if (record.getR40_T_FT() != null) {
					cellK.setCellValue(record.getR40_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

					//L
					cellL = row.createCell(9);
					if (record.getR40_T_HTM() != null) {
					cellL.setCellValue(record.getR40_T_HTM().doubleValue());
					cellL.setCellStyle(numberStyle);
					} else {
					cellL.setCellValue("");
					cellL.setCellStyle(textStyle);
					}

					//M
					cellM = row.createCell(10);
					if (record.getR40_T_TOTAL() != null) {
					cellM.setCellValue(record.getR40_T_TOTAL().doubleValue());
					cellM.setCellStyle(numberStyle);
					} else {
					cellM.setCellValue("");
					cellM.setCellStyle(textStyle);
					}

					//===================== R41 =====================
						// row40
						row = sheet.getRow(39);


					cellB = row.createCell(2);
					if (record.getR41_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR41_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR41_0_1Y_HTM() != null) {
					cellC.setCellValue(record.getR41_0_1Y_HTM().doubleValue());
					cellC.setCellStyle(numberStyle);
					} else {
					cellC.setCellValue("");
					cellC.setCellStyle(textStyle);
					}


					//Total D → Excel E
					cellD = row.createCell(4);
					if (record.getR41_0_1Y_TOTAL() != null) {
					 cellD.setCellValue(record.getR41_0_1Y_TOTAL().doubleValue());
					 cellD.setCellStyle(numberStyle);
					} else {
					 cellD.setCellValue("");
					 cellD.setCellStyle(textStyle);
					}


					//Column F
					cellE = row.createCell(5);
					if (record.getR41_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR41_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR41_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR41_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR41_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR41_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}

					//cellH = row.createCell(8);
					//if (record.getR41_O5Y_FT() != null) {
					//cellH.setCellValue(record.getR41_O5Y_FT().doubleValue());
					//cellH.setCellStyle(numberStyle);
					//} else {
					//cellH.setCellValue("");
					//cellH.setCellStyle(textStyle);
					//}
					//
					//celli = row.createCell(9);
					//if (record.getR41_O5Y_HTM() != null) {
					//celli.setCellValue(record.getR41_O5Y_HTM().doubleValue());
					//celli.setCellStyle(numberStyle);
					//} else {
					//celli.setCellValue("");
					//celli.setCellStyle(textStyle);
					//}
					//
					////J
					//cellJ = row.createCell(10);
					//if (record.getR41_O5Y_TOTAL() != null) {
					//cellJ.setCellValue(record.getR41_O5Y_TOTAL().doubleValue());
					//cellJ.setCellStyle(numberStyle);
					//} else {
					//cellJ.setCellValue("");
					//cellJ.setCellStyle(textStyle);
					//}

					//K
					cellK = row.createCell(8);
					if (record.getR41_T_FT() != null) {
					cellK.setCellValue(record.getR41_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

					//L
					cellL = row.createCell(9);
					if (record.getR41_T_HTM() != null) {
					 cellL.setCellValue(record.getR41_T_HTM().doubleValue());
					 cellL.setCellStyle(numberStyle);
					} else {
					 cellL.setCellValue("");
					 cellL.setCellStyle(textStyle);
					}

					//M
					cellM = row.createCell(10);
					if (record.getR41_T_TOTAL() != null) {
					 cellM.setCellValue(record.getR41_T_TOTAL().doubleValue());
					 cellM.setCellStyle(numberStyle);
					} else {
					 cellM.setCellValue("");
					 cellM.setCellStyle(textStyle);
					}





					// ===================== R42 =====================
						// row41
						row = sheet.getRow(40);



					cellB = row.createCell(2);
					if (record.getR42_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR42_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}

					cellC = row.createCell(3);
					if (record.getR42_0_1Y_HTM() != null) {
					cellC.setCellValue(record.getR42_0_1Y_HTM().doubleValue());
					cellC.setCellStyle(numberStyle);
					} else {
					cellC.setCellValue("");
					cellC.setCellStyle(textStyle);
					}

					////Total D → Excel E
					//cellD = row.createCell(4);
					//if (record.getR42_0_1Y_TOTAL() != null) {
					//cellD.setCellValue(record.getR42_0_1Y_TOTAL().doubleValue());
					//cellD.setCellStyle(numberStyle);
					//} else {
					//cellD.setCellValue("");
					//cellD.setCellStyle(textStyle);
					//}

					//Column F
					cellE = row.createCell(5);
					if (record.getR42_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR42_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR42_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR42_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR42_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR42_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}

					//cellH = row.createCell(8);
					//if (record.getR42_O5Y_FT() != null) {
					//cellH.setCellValue(record.getR42_O5Y_FT().doubleValue());
					//cellH.setCellStyle(numberStyle);
					//} else {
					//cellH.setCellValue("");
					//cellH.setCellStyle(textStyle);
					//}
					//
					//celli = row.createCell(9);
					//if (record.getR42_O5Y_HTM() != null) {
					//celli.setCellValue(record.getR42_O5Y_HTM().doubleValue());
					//celli.setCellStyle(numberStyle);
					//} else {
					//celli.setCellValue("");
					//celli.setCellStyle(textStyle);
					//}
					//
					////J
					//cellJ = row.createCell(10);
					//if (record.getR42_O5Y_TOTAL() != null) {
					//cellJ.setCellValue(record.getR42_O5Y_TOTAL().doubleValue());
					//cellJ.setCellStyle(numberStyle);
					//} else {
					//cellJ.setCellValue("");
					//cellJ.setCellStyle(textStyle);
					//}

					//K
					cellK = row.createCell(8);
					if (record.getR42_T_FT() != null) {
					cellK.setCellValue(record.getR42_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

					////L
					//cellL = row.createCell(12);
					//if (record.getR42_T_HTM() != null) {
					//cellL.setCellValue(record.getR42_T_HTM().doubleValue());
					//cellL.setCellStyle(numberStyle);
					//} else {
					//cellL.setCellValue("");
					//cellL.setCellStyle(textStyle);
					//}
					//
					////M
					//cellM = row.createCell(13);
					//if (record.getR42_T_TOTAL() != null) {
					//cellM.setCellValue(record.getR42_T_TOTAL().doubleValue());
					//cellM.setCellStyle(numberStyle);
					//} else {
					//cellM.setCellValue("");
					//cellM.setCellStyle(textStyle);
					//}
					//
//									
									
									
								// ===================== R43 =====================
								
								// row42
						row = sheet.getRow(41);

					cellB = row.createCell(2);
					if (record.getR43_0_1Y_FT() != null) {
					cellB.setCellValue(record.getR43_0_1Y_FT().doubleValue());
					cellB.setCellStyle(numberStyle);
					} else {
					cellB.setCellValue("");
					cellB.setCellStyle(textStyle);
					}





					//Column F
					cellE = row.createCell(5);
					if (record.getR43_1_5Y_FT() != null) {
					cellE.setCellValue(record.getR43_1_5Y_FT().doubleValue());
					cellE.setCellStyle(numberStyle);
					} else {
					cellE.setCellValue("");
					cellE.setCellStyle(textStyle);
					}

					cellf = row.createCell(6);
					if (record.getR43_1_5Y_HTM() != null) {
					cellf.setCellValue(record.getR43_1_5Y_HTM().doubleValue());
					cellf.setCellStyle(numberStyle);
					} else {
					cellf.setCellValue("");
					cellf.setCellStyle(textStyle);
					}

					//Total G → Excel H
					cellG = row.createCell(7);
					if (record.getR43_1_5Y_TOTAL() != null) {
					cellG.setCellValue(record.getR43_1_5Y_TOTAL().doubleValue());
					cellG.setCellStyle(numberStyle);
					} else {
					cellG.setCellValue("");
					cellG.setCellStyle(textStyle);
					}



					//K
					cellK = row.createCell(8);
					if (record.getR43_T_FT() != null) {
					cellK.setCellValue(record.getR43_T_FT().doubleValue());
					cellK.setCellStyle(numberStyle);
					} else {
					cellK.setCellValue("");
					cellK.setCellStyle(textStyle);
					}

				

				
				
						}

			workbook.setForceFormulaRecalculation(true);
		} else {

		}

//Write the final workbook content to the in-memory stream.
		workbook.write(out);

		logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

		return out.toByteArray();
	}

}


















}



	
