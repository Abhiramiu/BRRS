package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.*;



@Component
@Service
public class BRRS_M_NOSVOS_ReportService {



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
	BRRS_M_AIDP_Summary_Repo1	BRRS_M_aidpRepo1;
				
	@Autowired
	BRRS_M_AIDP_Summary_Repo2	BRRS_M_aidpRepo2;
	@Autowired
	BRRS_M_AIDP_Summary_Repo3	BRRS_M_aidpRepo3;
	@Autowired
	BRRS_M_AIDP_Summary_Repo4	BRRS_M_aidpRepo4;
	
	@Autowired
	BrrsMNosvosP1ArchivalRepository	BrrsMNosvosP1ArchivalRepository;
	@Autowired
	BrrsMNosvosP2ArchivalRepository	BrrsMNosvosP2ArchivalRepository;
	
	@Autowired
	BrrsMNosvosP3ArchivalRepository	BrrsMNosvosP3ArchivalRepository;
	
	@Autowired
	BrrsMNosvosP4ArchivalRepository	BrrsMNosvosP4ArchivalRepository;
	
	@Autowired
	BrrsMNosvosP5ArchivalRepository	BrrsMNosvosP5ArchivalRepository;
	
	@Autowired
	BrrsMNosvosP1ArchivalDetailRepository	BrrsMNosvosP1ArchivalRepositoryDetail;
	@Autowired
	BrrsMNosvosP2ArchivalDetailRepository	BrrsMNosvosP2ArchivalRepositoryDetail;
	
	@Autowired
	BrrsMNosvosP3ArchivalDetailRepository	BrrsMNosvosP3ArchivalRepositoryDetail;
	
	@Autowired
	BrrsMNosvosP4ArchivalDetailRepository	BrrsMNosvosP4ArchivalRepositoryDetail;
	
	@Autowired
	BrrsMNosvosP5ArchivalDetailRepository	BrrsMNosvosP5ArchivalRepositoryDetail;
	
	@Autowired
	BRRS_M_SFINP2_Summary_Repo M_SFINP2_Summary_Repo;

	@Autowired
	BRRS_M_SFINP2_Archival_Detail_Repo M_SFINP2_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SFINP2_Archival_Summary_Repo M_SFINP2_Archival_Summary_Repo;
	
	@Autowired
	BrrsMNosvosP1Repository BrrsMNosvosP1Repository;
	
	@Autowired
	BrrsMNosvosP2Repository BrrsMNosvosP2Repository;
	
	@Autowired
	BrrsMNosvosP3Repository BrrsMNosvosP3Repository;
	
	@Autowired
	BrrsMNosvosP4Repository BrrsMNosvosP4Repository;
	
	@Autowired
	BrrsMNosvosP5Repository BrrsMNosvosP5epository;
	
	
	@Autowired
	BrrsMNosvosP1DetailRepository BrrsMNosvosP1Repositorydetail;
	
	@Autowired
	BrrsMNosvosP2DetailRepository BrrsMNosvosP2Repositorydetail;
	
	@Autowired
	BrrsMNosvosP3DetailRepository BrrsMNosvosP3Repositorydetail;
	
	@Autowired
	BrrsMNosvosP4DetailRepository BrrsMNosvosP4Repositorydetail;
	
	@Autowired
	BrrsMNosvosP5DetailRepository BrrsMNosvosP5Repositorydetail;
	
	
	@Autowired
	BrrsMNosvosP1ResbuSummaryRepo BrrsMNosvosP1ResbuSummaryRepo;
	
	@Autowired
	BrrsMNosvosP2ResbuSummaryRepo BrrsMNosvosP2ResbuSummaryRepo;
	
	@Autowired
	BrrsMNosvosP3ResbuSummaryRepo BrrsMNosvosP3ResbuSummaryRepo;
	
	@Autowired
	BrrsMNosvosP4ResbuSummaryRepo BrrsMNosvosP4ResbuSummaryRepo;
	
	@Autowired
	BrrsMNosvosP5ResbuSummaryRepo BrrsMNosvosP5ResbuSummaryRepo;
	
	@Autowired
	BrrsMNosvosP1ResbuDetailRepo BrrsMNosvosP1ResbuDetailRepo;
	
	@Autowired
	BrrsMNosvosP2ResbuDetailRepo BrrsMNosvosP2ResbuDetailRepo;
	
	@Autowired
	BrrsMNosvosP3ResbuDetailRepo BrrsMNosvosP3ResbuDetailRepo;
	
	@Autowired
	BrrsMNosvosP4ResbuDetailRepo BrrsMNosvosP4ResbuDetailRepo;
	
	@Autowired
	BrrsMNosvosP5ResbuDetailRepo BrrsMNosvosP5ResbuDetailRepo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_NOSVOSView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) throws ParseException {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		
		String displayMode = "summary";

		if ( ( "ARCHIVAL".equalsIgnoreCase(type) && "summary".equalsIgnoreCase(dtltype) && version != null ) || ( "ARCHIVAL".equalsIgnoreCase(type) && !"detail".equalsIgnoreCase(dtltype) && version != null) ) {
			List<BrrsMNosvosP1Archival> T1Master = new ArrayList<BrrsMNosvosP1Archival>();
			List<BrrsMNosvosP2Archival> T2Master = new ArrayList<BrrsMNosvosP2Archival>();
			List<BrrsMNosvosP3Archival> T3Master = new ArrayList<BrrsMNosvosP3Archival>();
			List<BrrsMNosvosP4Archival> T4Master = new ArrayList<BrrsMNosvosP4Archival>();
			List<BrrsMNosvosP5Archival> T5Master = new ArrayList<BrrsMNosvosP5Archival>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BrrsMNosvosP1ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = BrrsMNosvosP2ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);
				T3Master = BrrsMNosvosP3ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);
				T4Master = BrrsMNosvosP4ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);
				T5Master = BrrsMNosvosP5ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
			mv.addObject("reportsummary4", T4Master);
			mv.addObject("reportsummary5", T5Master);
			displayMode = "summary";
		}
		else if ( "ARCHIVAL".equalsIgnoreCase(type) && "detail".equalsIgnoreCase(dtltype) && version != null  ) {
			List<BrrsMNosvosP1ArchivalDetail> T1Master = new ArrayList<BrrsMNosvosP1ArchivalDetail>();
			List<BrrsMNosvosP2ArchivalDetail> T2Master = new ArrayList<BrrsMNosvosP2ArchivalDetail>();
			List<BrrsMNosvosP3ArchivalDetail> T3Master = new ArrayList<BrrsMNosvosP3ArchivalDetail>();
			List<BrrsMNosvosP4ArchivalDetail> T4Master = new ArrayList<BrrsMNosvosP4ArchivalDetail>();
			List<BrrsMNosvosP5ArchivalDetail> T5Master = new ArrayList<BrrsMNosvosP5ArchivalDetail>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BrrsMNosvosP1ArchivalRepositoryDetail.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = BrrsMNosvosP2ArchivalRepositoryDetail.getdatabydateListarchival(dateformat.parse(todate), version);
				T3Master = BrrsMNosvosP3ArchivalRepositoryDetail.getdatabydateListarchival(dateformat.parse(todate), version);
				T4Master = BrrsMNosvosP4ArchivalRepositoryDetail.getdatabydateListarchival(dateformat.parse(todate), version);
				T5Master = BrrsMNosvosP5ArchivalRepositoryDetail.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
			mv.addObject("reportsummary4", T4Master);
			mv.addObject("reportsummary5", T5Master);
			
			displayMode = "detail";
		}
		else if  (( "RESUB".equalsIgnoreCase(type) && "summary".equalsIgnoreCase(dtltype) && version != null) ||  ( "RESUB".equalsIgnoreCase(type) && !"detail".equalsIgnoreCase(dtltype) && version != null ) ) {
			List<BrrsMNosvosP1ResbuSummaryEntity> T1Master = new ArrayList<BrrsMNosvosP1ResbuSummaryEntity>();
			List<BrrsMNosvosP2ResbuSummaryEntity> T2Master = new ArrayList<BrrsMNosvosP2ResbuSummaryEntity>();
			List<BrrsMNosvosP3ResbuSummaryEntity> T3Master = new ArrayList<BrrsMNosvosP3ResbuSummaryEntity>();
			List<BrrsMNosvosP4ResbuSummaryEntity> T4Master = new ArrayList<BrrsMNosvosP4ResbuSummaryEntity>();
			List<BrrsMNosvosP5ResbuSummaryEntity> T5Master = new ArrayList<BrrsMNosvosP5ResbuSummaryEntity>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BrrsMNosvosP1ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = BrrsMNosvosP2ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
				T3Master = BrrsMNosvosP3ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
				T4Master = BrrsMNosvosP4ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
				T5Master = BrrsMNosvosP5ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
			mv.addObject("reportsummary4", T4Master);
			mv.addObject("reportsummary5", T5Master);
			displayMode = "summary";
		}
		else if  ( "RESUB".equalsIgnoreCase(type) && "detail".equalsIgnoreCase(dtltype) && version != null) {
			System.out.println("1111111");
			List<BrrsMNosvosP1ResbuDetailEntity> T1Master = new ArrayList<BrrsMNosvosP1ResbuDetailEntity>();
			System.out.println("2222222222");
			List<BrrsMNosvosP2ResbuDetailEntity> T2Master = new ArrayList<BrrsMNosvosP2ResbuDetailEntity>();
			List<BrrsMNosvosP3ResbuDetailEntity> T3Master = new ArrayList<BrrsMNosvosP3ResbuDetailEntity>();
			List<BrrsMNosvosP4ResbuDetailEntity> T4Master = new ArrayList<BrrsMNosvosP4ResbuDetailEntity>();
			List<BrrsMNosvosP5ResbuDetailEntity> T5Master = new ArrayList<BrrsMNosvosP5ResbuDetailEntity>();
			System.out.println("3333333333");
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				System.out.println("44444444");
				T1Master = BrrsMNosvosP1ResbuDetailRepo.getdatabydateListarchival(dateformat.parse(todate), version);
				System.out.println("55555555");
				T2Master = BrrsMNosvosP2ResbuDetailRepo.getdatabydateListarchival(dateformat.parse(todate), version);
				System.out.println("666666666");
				T3Master = BrrsMNosvosP3ResbuDetailRepo.getdatabydateListarchival(dateformat.parse(todate), version);
				T4Master = BrrsMNosvosP4ResbuDetailRepo.getdatabydateListarchival(dateformat.parse(todate), version);
				T5Master = BrrsMNosvosP5ResbuDetailRepo.getdatabydateListarchival(dateformat.parse(todate), version);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
			mv.addObject("reportsummary4", T4Master);
			mv.addObject("reportsummary5", T5Master);
			displayMode = "detail";
			
		}
		else if( !"ARCHIVAL".equalsIgnoreCase(type) && "detail".equalsIgnoreCase(dtltype)){
			List<BrrsMNosvosP1Detail> T1Master = new ArrayList<BrrsMNosvosP1Detail>();
			List<BrrsMNosvosP2Detail> T2Master = new ArrayList<BrrsMNosvosP2Detail>();
			List<BrrsMNosvosP3Detail> T3Master = new ArrayList<BrrsMNosvosP3Detail>();
			List<BrrsMNosvosP4Detail> T4Master = new ArrayList<BrrsMNosvosP4Detail>();
			List<BrrsMNosvosP5Detail> T5Master = new ArrayList<BrrsMNosvosP5Detail>();

				Date d1 = dateformat.parse(todate);
				System.out.println(todate);
				T1Master = BrrsMNosvosP1Repositorydetail.getDataByDate(dateformat.parse(todate));
				T2Master = BrrsMNosvosP2Repositorydetail.getDataByDate(dateformat.parse(todate));
				T3Master = BrrsMNosvosP3Repositorydetail.getDataByDate(dateformat.parse(todate));
				T4Master = BrrsMNosvosP4Repositorydetail.getDataByDate(dateformat.parse(todate));
				T5Master = BrrsMNosvosP5Repositorydetail.getDataByDate(dateformat.parse(todate));
				
				displayMode = "detail";
				System.out.println("Detail");
				
				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary2", T2Master);
				mv.addObject("reportsummary3", T3Master);
				mv.addObject("reportsummary4", T4Master);
				mv.addObject("reportsummary5", T5Master);
			}
		else {
			List<BrrsMNosvosP1> T1Master = new ArrayList<BrrsMNosvosP1>();
			List<BrrsMNosvosP2> T2Master = new ArrayList<BrrsMNosvosP2>();
			List<BrrsMNosvosP3> T3Master = new ArrayList<BrrsMNosvosP3>();
			List<BrrsMNosvosP4> T4Master = new ArrayList<BrrsMNosvosP4>();
			List<BrrsMNosvosP5> T5Master = new ArrayList<BrrsMNosvosP5>();
			
			try {
				Date d1 = dateformat.parse(todate);
				System.out.println(todate);
				T1Master = BrrsMNosvosP1Repository.getDataByDate(dateformat.parse(todate));
				T2Master = BrrsMNosvosP2Repository.getDataByDate(dateformat.parse(todate));
				T3Master = BrrsMNosvosP3Repository.getDataByDate(dateformat.parse(todate));
				T4Master = BrrsMNosvosP4Repository.getDataByDate(dateformat.parse(todate));
				T5Master = BrrsMNosvosP5epository.getDataByDate(dateformat.parse(todate));
				
				
				
				System.out.println("Size of t1master is : --- "+T5Master.size());
				displayMode = "summary";
				
				System.out.println("Summary");
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
			mv.addObject("reportsummary4", T4Master);
			mv.addObject("reportsummary5", T5Master);
		}

		
		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_NOSVOS");
		mv.addObject("displaymode", displayMode);
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	private Date normalizeDate(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}
	
	
public void updateReport(BrrsMNosvosP1 updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());
	    Date cleanDate = normalizeDate(updatedEntity.getREPORT_DATE());

	    BrrsMNosvosP1 existing = BrrsMNosvosP1Repository.findById(cleanDate)
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + cleanDate));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 1; i <= 101; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "NAME_OF_BANK_AND_COUNTRY_NOSTRO", "TYPE_OF_ACCOUNT_NOSTRO", "PURPOSE_NOSTRO", "CURRENCY_NOSTRO",
	                                "SOVEREIGN_RATING_AAA_AA_A1_NOSTRO", "RISK_WEIGHT_NOSTRO", "AMOUNT_DEMAND_NOSTRO" ,"AMOUNT_TIME_NOSTRO", "RISK_WEIGHTED_AMOUNT_NOSTRO"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = BrrsMNosvosP1.class.getMethod(getterName);
	                    Method setter = BrrsMNosvosP1.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R51 totals
	        String[] totalFields = { "TOTAL_AMOUNT_DEMAND_NOSTRO", "TOTAL_AMOUNT_TIME_NOSTRO", "TOTAL_RISK_WEIGHTED_AMOUNT_NOSTRO" };
	        for (String field : totalFields) {
	            String getterName = "getR101_" + field;
	            String setterName = "setR101_" + field;

	            try {
	                Method getter = BrrsMNosvosP1.class.getMethod(getterName);
	                Method setter = BrrsMNosvosP1.class.getMethod(setterName, getter.getReturnType());

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
	    BrrsMNosvosP1Repository.save(existing);
	    
	    BrrsMNosvosP1Detail detail = new BrrsMNosvosP1Detail();

	    BeanUtils.copyProperties(existing, detail);


	    BrrsMNosvosP1Repositorydetail.save(detail);
	}

	public void updateReport2(BrrsMNosvosP2 updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());
	    Date cleanDate = normalizeDate(updatedEntity.getREPORT_DATE());
	    
	    BrrsMNosvosP2 existing = BrrsMNosvosP2Repository.findById(cleanDate)
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + cleanDate));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 1; i <= 101; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "NAME_OF_BANK_AND_COUNTRY_VOSTRO", "TYPE_OF_ACCOUNT_VOSTRO", "PURPOSE_VOSTRO", "CURRENCY_VOSTRO",
	                                "AMOUNT_DEMAND_VOSTRO", "AMOUNT_TIME_VOSTRO"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = BrrsMNosvosP2.class.getMethod(getterName);
	                    Method setter = BrrsMNosvosP2.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R51 totals
	        String[] totalFields = { "TOATAL_AMOUNT_DEMAND_VOSTRO", "TOTAL_AMOUNT_TIME_VOSTRO" };
	        for (String field : totalFields) {
	            String getterName = "getR101_" + field;
	            String setterName = "setR101_" + field;

	            try {
	                Method getter = BrrsMNosvosP2.class.getMethod(getterName);
	                Method setter = BrrsMNosvosP2.class.getMethod(setterName, getter.getReturnType());

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
	    BrrsMNosvosP2Repository.save(existing);
	    
	    BrrsMNosvosP2Detail detail = new BrrsMNosvosP2Detail();

	    BeanUtils.copyProperties(existing, detail);


	    BrrsMNosvosP2Repositorydetail.save(detail);
	}

	public void updateReport3(BrrsMNosvosP3 updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());
	    Date cleanDate = normalizeDate(updatedEntity.getREPORT_DATE());

	    BrrsMNosvosP3 existing = BrrsMNosvosP3Repository.findById(cleanDate)
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + cleanDate));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 1; i <= 101; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "NAME_OF_BANK_NOSTRO1", "TYPE_OF_ACCOUNT_NOSTRO1", "PURPOSE_NOSTRO1", "CURRENCY_NOSTRO1",
	                                "SOVEREIGN_RATING_NOSTRO1", "RISK_WEIGHT_NOSTRO1", "AMOUNT_DEMAND_NOSTRO1", "AMOUNT_TIME_NOSTRO1", "RISK_WEIGHTED_AMOUNT_NOSTRO1" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = BrrsMNosvosP3.class.getMethod(getterName);
	                    Method setter = BrrsMNosvosP3.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R51 totals
	        String[] totalFields = { "TOTAL_AMOUNT_DEMAND_NOSTRO1", "TOTAL_AMOUNT_TIME_NOSTRO1", "TOTAL_RISK_WEIGHTED_AMOUNT_NOSTRO1" };
	        for (String field : totalFields) {
	            String getterName = "getR101_" + field;
	            String setterName = "setR101_" + field;

	            try {
	                Method getter = BrrsMNosvosP3.class.getMethod(getterName);
	                Method setter = BrrsMNosvosP3.class.getMethod(setterName, getter.getReturnType());

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
	    BrrsMNosvosP3Repository.save(existing);
	    
	    BrrsMNosvosP3Detail detail = new BrrsMNosvosP3Detail();

	    BeanUtils.copyProperties(existing, detail);


	    BrrsMNosvosP3Repositorydetail.save(detail);
	}
	
	public void updateReport4(BrrsMNosvosP4 updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());
	    Date cleanDate = normalizeDate(updatedEntity.getREPORT_DATE());

	    BrrsMNosvosP4 existing = BrrsMNosvosP4Repository.findById(cleanDate)
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + cleanDate));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 1; i <= 101; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "NAME_OF_BANK_VOSTRO1","TYPE_OF_ACCOUNT_VOSTRO1", "PURPOSE_VOSTRO1", "CURRENCY_VOSTRO1", "AMOUNT_DEMAND_VOSTRO1",
	                                "AMOUNT_TIME_VOSTRO1"};

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = BrrsMNosvosP4.class.getMethod(getterName);
	                    Method setter = BrrsMNosvosP4.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R51 totals
	        String[] totalFields = { "TOTAL_AMOUNT_DEMAND_VOSTRO1", "TOTAL_AMOUNT_TIME_VOSTRO1" };
	        for (String field : totalFields) {
	            String getterName = "getR101_" + field;
	            String setterName = "setR101_" + field;

	            try {
	                Method getter = BrrsMNosvosP4.class.getMethod(getterName);
	                Method setter = BrrsMNosvosP4.class.getMethod(setterName, getter.getReturnType());

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
	    BrrsMNosvosP4Repository.save(existing);
	    
	    BrrsMNosvosP4Detail detail = new BrrsMNosvosP4Detail();

	    BeanUtils.copyProperties(existing, detail);


	    BrrsMNosvosP4Repositorydetail.save(detail);
	}
	
	public void updateReport5(BrrsMNosvosP5 updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());
	    Date cleanDate = normalizeDate(updatedEntity.getREPORT_DATE());

	    BrrsMNosvosP5 existing = BrrsMNosvosP5epository.findById(cleanDate)
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + cleanDate));

	    
	    BeanUtils.copyProperties(updatedEntity, existing);
	    // 3️⃣ Save updated entity
	    BrrsMNosvosP5epository.save(existing);
	    
	    BrrsMNosvosP5Detail detail = new BrrsMNosvosP5Detail();

	    BeanUtils.copyProperties(existing, detail);


	    BrrsMNosvosP5Repositorydetail.save(detail);
	}
	
	public List<Object> getM_NOSVOSArchival() {
		List<Object> M_NOSVOSArchivallist = new ArrayList<>();
		try {
			M_NOSVOSArchivallist = BrrsMNosvosP1ArchivalRepository.getM_NOSVOSarchival();
			M_NOSVOSArchivallist = BrrsMNosvosP2ArchivalRepository.getM_NOSVOSarchival();
			M_NOSVOSArchivallist = BrrsMNosvosP3ArchivalRepository.getM_NOSVOSarchival();
			M_NOSVOSArchivallist = BrrsMNosvosP4ArchivalRepository.getM_NOSVOSarchival();
			System.out.println("countser" + M_NOSVOSArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_NOSVOS Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_NOSVOSArchivallist;
	}
	
	public List<Object[]> getM_NOSVOSArchivalFirstList() {
	    List<Object[]> archivalList = new ArrayList<>();
	    try {
	        List<BrrsMNosvosP1Archival> latestArchivalList = BrrsMNosvosP1ArchivalRepository.getdatabydateListWithVersion();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (BrrsMNosvosP1Archival entity : latestArchivalList) {
	                archivalList.add(new Object[] {
	                        entity.getREPORT_DATE(),
	                        entity.getREPORT_VERSION(),
	                        entity.getREPORT_RESUB_DATE()
	                });
	            }
	            System.out.println("Fetched " + archivalList.size() + " record(s)");
	        } else {
	            System.out.println("No archival data found.");
	        }

	    } catch (Exception e) {
	        System.err.println("Error fetching M_SRWA_12B Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return archivalList;
	}
	
	public byte[] getM_NOSVOSExcel(String filename, String reportId, String fromdate, String todate, String currency,
            String dtltype, String type, BigDecimal version) throws Exception {
	logger.info("Service: Starting Excel generation process in memory.");
	System.out.println(type);
	System.out.println(version);
	
	if (type.equals("ARCHIVAL") & version != null) {
		byte[] ARCHIVALreport = getExcelM_NOSVOSARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
				version);
		return ARCHIVALreport;
	}
	
	if (type.equals("RESUB") & version != null) {
		byte[] ARCHIVALreport = getExcelM_NOSVOSRESUB(filename, reportId, fromdate, todate, currency, dtltype, type,
				version);
		return ARCHIVALreport;
	}
	
	
	
	System.out.println("came to excel download service");
	List<BrrsMNosvosP1> dataList = BrrsMNosvosP1Repository.getData();
	List<BrrsMNosvosP2> dataList2 = BrrsMNosvosP2Repository.getData();
	List<BrrsMNosvosP3> dataList3 = BrrsMNosvosP3Repository.getData();
	List<BrrsMNosvosP4> dataList4 = BrrsMNosvosP4Repository.getData();
	
	List<BrrsMNosvosP1> dataListEmail = BrrsMNosvosP1Repository.getDataByDate(dateformat.parse(todate));
	List<BrrsMNosvosP2> dataListEmail2 = BrrsMNosvosP2Repository.getDataByDate(dateformat.parse(todate));
	List<BrrsMNosvosP3> dataListEmail3 = BrrsMNosvosP3Repository.getDataByDate(dateformat.parse(todate));
	List<BrrsMNosvosP4> dataListEmail4 = BrrsMNosvosP4Repository.getDataByDate(dateformat.parse(todate));
	List<BrrsMNosvosP5> dataListEmail5 = BrrsMNosvosP5epository.getDataByDate(dateformat.parse(todate));
	
	

		
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M-NOSVOS report. Returning empty result.");
			return new byte[0];
			}
			if (dataList2.isEmpty()) {
			    logger.error("No data found for Entity2 - check query for date: {}", todate);
			}
			if (dataList3.isEmpty()) {
			    logger.error("No data found for Entity3 - check query for date: {}", todate);
			}
			if (dataList4.isEmpty()) {
			    logger.error("No data found for Entity4 - check query for date: {}", todate);
			}

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M-NOSVOS report. Returning empty result.");
			return new byte[0];
			}
			if (dataList2.isEmpty()) {
			    logger.error("No data found for Entity2 - check query for date: {}", todate);
			}
			if (dataList3.isEmpty()) {
			    logger.error("No data found for Entity3 - check query for date: {}", todate);
			}
			if (dataList4.isEmpty()) {
			    logger.error("No data found for Entity4 - check query for date: {}", todate);
			}
	
	
	
	
	String templateDir = env.getProperty("output.exportpathtemp");
	Path templatePath = Paths.get(templateDir, filename);
	
	logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());
	
	if (!Files.exists(templatePath)) {
	throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
	}
	if (!Files.isReadable(templatePath)) {
	throw new SecurityException("Template file exists but is not readable: " + templatePath.toAbsolutePath());
	}
	
	try (InputStream templateInputStream = Files.newInputStream(templatePath);
	Workbook workbook = WorkbookFactory.create(templateInputStream);
	ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	
	Sheet sheet = workbook.getSheetAt(0);
	
	CreationHelper createHelper = workbook.getCreationHelper();
	
	Font font = workbook.createFont();
	font.setFontHeightInPoints((short) 8);
	font.setFontName("Arial");
	
	CellStyle textStyle = workbook.createCellStyle();
	textStyle.setBorderBottom(BorderStyle.THIN);
	textStyle.setBorderTop(BorderStyle.THIN);
	textStyle.setBorderLeft(BorderStyle.THIN);
	textStyle.setBorderRight(BorderStyle.THIN);
	textStyle.setFont(font);
	
	CellStyle numberStyle = workbook.createCellStyle();
	//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.000"));
	numberStyle.setBorderBottom(BorderStyle.THIN);
	numberStyle.setBorderTop(BorderStyle.THIN);
	numberStyle.setBorderLeft(BorderStyle.THIN);
	numberStyle.setBorderRight(BorderStyle.THIN);
	numberStyle.setFont(font);
	
	if (dtltype.equals("email_report") ) {
		
		String[] rowCodesPart1 = new String[101];
		
		for (int i = 1; i <= 101; i++) {
		    rowCodesPart1[i - 1] = "R" + i;
		}
	
	
		String[] fieldSuffixes = {
		"NAME_OF_BANK_AND_COUNTRY_NOSTRO","TYPE_OF_ACCOUNT_NOSTRO","PURPOSE_NOSTRO","CURRENCY_NOSTRO","SOVEREIGN_RATING_AAA_AA_A1_NOSTRO","RISK_WEIGHT_NOSTRO","AMOUNT_DEMAND_NOSTRO","RISK_WEIGHTED_AMOUNT_NOSTRO"   
		};
	
		String[] fieldSuffixes2 = {
		"NAME_OF_BANK_AND_COUNTRY_VOSTRO","TYPE_OF_ACCOUNT_VOSTRO","PURPOSE_VOSTRO","CURRENCY_VOSTRO","AMOUNT_DEMAND_VOSTRO","AMOUNT_TIME_VOSTRO"   
		};
	
		String[] fieldSuffixes3 = {
		"NAME_OF_BANK_NOSTRO1","TYPE_OF_ACCOUNT_NOSTRO1","PURPOSE_NOSTRO1","CURRENCY_NOSTRO1","SOVEREIGN_RATING_NOSTRO1","RISK_WEIGHT_NOSTRO1","AMOUNT_DEMAND_NOSTRO1","AMOUNT_TIME_NOSTRO1","RISK_WEIGHTED_AMOUNT_NOSTRO1"
		};
	
		String[] fieldSuffixes4 = {
		"NAME_OF_BANK_VOSTRO1","TYPE_OF_ACCOUNT_VOSTRO1","PURPOSE_VOSTRO1","CURRENCY_VOSTRO1","AMOUNT_DEMAND_VOSTRO1","AMOUNT_TIME_VOSTRO1"
		};
	
	
	
		// First set: R11 - R50 at row 11
		writeEmailExcelRowData1(sheet, dataListEmail, rowCodesPart1, fieldSuffixes, 10, numberStyle, textStyle);
		
//		// First set: R56 - R95 at row 56
		writeEmailExcelRowData2(sheet, dataListEmail2, rowCodesPart1, fieldSuffixes2, 10, numberStyle, textStyle);
//	
//		// Third Set: R101 - R141 at row 101
		writeEmailExcelRowData3(sheet, dataListEmail3, rowCodesPart1, fieldSuffixes3, 118, numberStyle, textStyle);
//	
//		// Fourth Set: R147 - R196 at row 146
		writeEmailExcelRowData4(sheet, dataListEmail4, rowCodesPart1, fieldSuffixes4, 118, numberStyle, textStyle);
		
		writeEmailExcelRowData5(sheet, dataListEmail5, rowCodesPart1, fieldSuffixes4, 118, numberStyle, textStyle);
		
	}else {
	
		String[] rowCodesPart1 = new String[101];
	
		for (int i = 1; i <= 101; i++) {
		    rowCodesPart1[i - 1] = "R" + i;
		}
	
	
		String[] fieldSuffixes = {
		"NAME_OF_BANK_AND_COUNTRY_NOSTRO","TYPE_OF_ACCOUNT_NOSTRO","PURPOSE_NOSTRO","CURRENCY_NOSTRO","SOVEREIGN_RATING_AAA_AA_A1_NOSTRO","RISK_WEIGHT_NOSTRO","AMOUNT_DEMAND_NOSTRO","AMOUNT_TIME_NOSTRO","RISK_WEIGHTED_AMOUNT_NOSTRO"   
		};
	
		String[] fieldSuffixes2 = {
		"NAME_OF_BANK_AND_COUNTRY_VOSTRO","TYPE_OF_ACCOUNT_VOSTRO","PURPOSE_VOSTRO","CURRENCY_VOSTRO","AMOUNT_DEMAND_VOSTRO","AMOUNT_TIME_VOSTRO"   
		};
	
		String[] fieldSuffixes3 = {
		"NAME_OF_BANK_NOSTRO1","TYPE_OF_ACCOUNT_NOSTRO1","PURPOSE_NOSTRO1","CURRENCY_NOSTRO1","SOVEREIGN_RATING_NOSTRO1","RISK_WEIGHT_NOSTRO1","AMOUNT_DEMAND_NOSTRO1","AMOUNT_TIME_NOSTRO1","RISK_WEIGHTED_AMOUNT_NOSTRO1"
		};
	
		String[] fieldSuffixes4 = {
		"NAME_OF_BANK_VOSTRO1","TYPE_OF_ACCOUNT_VOSTRO1","PURPOSE_VOSTRO1","CURRENCY_VOSTRO1","AMOUNT_DEMAND_VOSTRO1","AMOUNT_TIME_VOSTRO1"
		};
	
	
	
		// First set: R11 - R50 at row 11
		writeRowData1(sheet, dataList, rowCodesPart1, fieldSuffixes, 10, numberStyle, textStyle);
		
		// First set: R56 - R95 at row 56
		writeRowData2(sheet, dataList2, rowCodesPart1, fieldSuffixes2, 10, numberStyle, textStyle);
	
		// Third Set: R101 - R141 at row 101
		writeRowData3(sheet, dataList3, rowCodesPart1, fieldSuffixes3, 118, numberStyle, textStyle);
	
		// Fourth Set: R147 - R196 at row 146
		writeRowData4(sheet, dataList4, rowCodesPart1, fieldSuffixes4, 118, numberStyle, textStyle);
	
	}

	workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
	workbook.write(out);
	logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
	
	return out.toByteArray();
	}
}
	
	private void writeEmailResubExcelRowData1(Sheet sheet, List<BrrsMNosvosP1ResbuSummaryEntity> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			
		System.out.println("came to write row data 1 method");
		
		BrrsMNosvosP1ResbuSummaryEntity record1 = dataList.get(0);
		
		 Row  row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);
		 Cell cell1 = row.createCell(0);

		 if (record1.getR1_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR1_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell1.setCellValue(record1.getR1_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell1.setCellStyle(numberStyle);
		 } else {
		     cell1.setCellValue("");
		     cell1.setCellStyle(textStyle);
		 }

		  Cell cell2 = row.createCell(1);

		 if (record1.getR1_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR1_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell2.setCellValue(record1.getR1_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell2.setCellStyle(numberStyle);
		 } else {
		     cell2.setCellValue("");
		     cell2.setCellStyle(textStyle);
		 }

		  Cell cell3 = row.createCell(2);

		 if (record1.getR1_PURPOSE_NOSTRO() != null && !record1.getR1_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell3.setCellValue(record1.getR1_PURPOSE_NOSTRO().toString().trim() );
		     cell3.setCellStyle(numberStyle);
		 } else {
		     cell3.setCellValue("");
		     cell3.setCellStyle(textStyle);
		 }

		  Cell cell4 = row.createCell(3);

		 if (record1.getR1_CURRENCY_NOSTRO() != null && !record1.getR1_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell4.setCellValue(record1.getR1_CURRENCY_NOSTRO().toString().trim() );
		     cell4.setCellStyle(numberStyle);
		 } else {
		     cell4.setCellValue("");
		     cell4.setCellStyle(textStyle);
		 }

		  Cell cell5 = row.createCell(4);

		 if (record1.getR1_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR1_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell5.setCellValue(record1.getR1_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell5.setCellStyle(numberStyle);
		 } else {
		     cell5.setCellValue("");
		     cell5.setCellStyle(textStyle);
		 }

		  Cell cell6 = row.createCell(5);

		 if (record1.getR1_RISK_WEIGHT_NOSTRO() != null && !record1.getR1_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell6.setCellValue(record1.getR1_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell6.setCellStyle(numberStyle);
		 } else {
		     cell6.setCellValue("");
		     cell6.setCellStyle(textStyle);
		 }

		  Cell cell7 = row.createCell(6);

		 if (record1.getR1_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR1_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell7.setCellValue(record1.getR1_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell7.setCellStyle(numberStyle);
		 } else {
		     cell7.setCellValue("");
		     cell7.setCellStyle(textStyle);
		 }

		  Cell cell8 = row.createCell(7);

		 if (record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell8.setCellValue(record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell8.setCellStyle(numberStyle);
		 } else {
		     cell8.setCellValue("");
		     cell8.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
		  Cell cell9 = row.createCell(0);

		 if (record1.getR2_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR2_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell9.setCellValue(record1.getR2_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell9.setCellStyle(numberStyle);
		 } else {
		     cell9.setCellValue("");
		     cell9.setCellStyle(textStyle);
		 }

		  Cell cell10 = row.createCell(1);

		 if (record1.getR2_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR2_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell10.setCellValue(record1.getR2_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell10.setCellStyle(numberStyle);
		 } else {
		     cell10.setCellValue("");
		     cell10.setCellStyle(textStyle);
		 }

		  Cell cell11 = row.createCell(2);

		 if (record1.getR2_PURPOSE_NOSTRO() != null && !record1.getR2_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell11.setCellValue(record1.getR2_PURPOSE_NOSTRO().toString().trim() );
		     cell11.setCellStyle(numberStyle);
		 } else {
		     cell11.setCellValue("");
		     cell11.setCellStyle(textStyle);
		 }

		  Cell cell12 = row.createCell(3);

		 if (record1.getR2_CURRENCY_NOSTRO() != null && !record1.getR2_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell12.setCellValue(record1.getR2_CURRENCY_NOSTRO().toString().trim() );
		     cell12.setCellStyle(numberStyle);
		 } else {
		     cell12.setCellValue("");
		     cell12.setCellStyle(textStyle);
		 }

		  Cell cell13 = row.createCell(4);

		 if (record1.getR2_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR2_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell13.setCellValue(record1.getR2_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell13.setCellStyle(numberStyle);
		 } else {
		     cell13.setCellValue("");
		     cell13.setCellStyle(textStyle);
		 }

		  Cell cell14 = row.createCell(5);

		 if (record1.getR2_RISK_WEIGHT_NOSTRO() != null && !record1.getR2_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell14.setCellValue(record1.getR2_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell14.setCellStyle(numberStyle);
		 } else {
		     cell14.setCellValue("");
		     cell14.setCellStyle(textStyle);
		 }

		  Cell cell15 = row.createCell(6);

		 if (record1.getR2_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR2_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell15.setCellValue(record1.getR2_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell15.setCellStyle(numberStyle);
		 } else {
		     cell15.setCellValue("");
		     cell15.setCellStyle(textStyle);
		 }

		  Cell cell16 = row.createCell(7);

		 if (record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell16.setCellValue(record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell16.setCellStyle(numberStyle);
		 } else {
		     cell16.setCellValue("");
		     cell16.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(12) != null ? sheet.getRow(12) : sheet.createRow(12);
		  Cell cell17 = row.createCell(0);

		 if (record1.getR3_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR3_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell17.setCellValue(record1.getR3_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell17.setCellStyle(numberStyle);
		 } else {
		     cell17.setCellValue("");
		     cell17.setCellStyle(textStyle);
		 }

		  Cell cell18 = row.createCell(1);

		 if (record1.getR3_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR3_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell18.setCellValue(record1.getR3_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell18.setCellStyle(numberStyle);
		 } else {
		     cell18.setCellValue("");
		     cell18.setCellStyle(textStyle);
		 }

		  Cell cell19 = row.createCell(2);

		 if (record1.getR3_PURPOSE_NOSTRO() != null && !record1.getR3_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell19.setCellValue(record1.getR3_PURPOSE_NOSTRO().toString().trim() );
		     cell19.setCellStyle(numberStyle);
		 } else {
		     cell19.setCellValue("");
		     cell19.setCellStyle(textStyle);
		 }

		  Cell cell20 = row.createCell(3);

		 if (record1.getR3_CURRENCY_NOSTRO() != null && !record1.getR3_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell20.setCellValue(record1.getR3_CURRENCY_NOSTRO().toString().trim() );
		     cell20.setCellStyle(numberStyle);
		 } else {
		     cell20.setCellValue("");
		     cell20.setCellStyle(textStyle);
		 }

		  Cell cell21 = row.createCell(4);

		 if (record1.getR3_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR3_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell21.setCellValue(record1.getR3_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell21.setCellStyle(numberStyle);
		 } else {
		     cell21.setCellValue("");
		     cell21.setCellStyle(textStyle);
		 }

		  Cell cell22 = row.createCell(5);

		 if (record1.getR3_RISK_WEIGHT_NOSTRO() != null && !record1.getR3_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell22.setCellValue(record1.getR3_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell22.setCellStyle(numberStyle);
		 } else {
		     cell22.setCellValue("");
		     cell22.setCellStyle(textStyle);
		 }

		  Cell cell23 = row.createCell(6);

		 if (record1.getR3_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR3_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell23.setCellValue(record1.getR3_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell23.setCellStyle(numberStyle);
		 } else {
		     cell23.setCellValue("");
		     cell23.setCellStyle(textStyle);
		 }

		  Cell cell24 = row.createCell(7);

		 if (record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell24.setCellValue(record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell24.setCellStyle(numberStyle);
		 } else {
		     cell24.setCellValue("");
		     cell24.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(13) != null ? sheet.getRow(13) : sheet.createRow(13);
		  Cell cell25 = row.createCell(0);

		 if (record1.getR4_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR4_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell25.setCellValue(record1.getR4_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell25.setCellStyle(numberStyle);
		 } else {
		     cell25.setCellValue("");
		     cell25.setCellStyle(textStyle);
		 }

		  Cell cell26 = row.createCell(1);

		 if (record1.getR4_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR4_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell26.setCellValue(record1.getR4_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell26.setCellStyle(numberStyle);
		 } else {
		     cell26.setCellValue("");
		     cell26.setCellStyle(textStyle);
		 }

		  Cell cell27 = row.createCell(2);

		 if (record1.getR4_PURPOSE_NOSTRO() != null && !record1.getR4_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell27.setCellValue(record1.getR4_PURPOSE_NOSTRO().toString().trim() );
		     cell27.setCellStyle(numberStyle);
		 } else {
		     cell27.setCellValue("");
		     cell27.setCellStyle(textStyle);
		 }

		  Cell cell28 = row.createCell(3);

		 if (record1.getR4_CURRENCY_NOSTRO() != null && !record1.getR4_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell28.setCellValue(record1.getR4_CURRENCY_NOSTRO().toString().trim() );
		     cell28.setCellStyle(numberStyle);
		 } else {
		     cell28.setCellValue("");
		     cell28.setCellStyle(textStyle);
		 }

		  Cell cell29 = row.createCell(4);

		 if (record1.getR4_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR4_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell29.setCellValue(record1.getR4_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell29.setCellStyle(numberStyle);
		 } else {
		     cell29.setCellValue("");
		     cell29.setCellStyle(textStyle);
		 }

		  Cell cell30 = row.createCell(5);

		 if (record1.getR4_RISK_WEIGHT_NOSTRO() != null && !record1.getR4_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell30.setCellValue(record1.getR4_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell30.setCellStyle(numberStyle);
		 } else {
		     cell30.setCellValue("");
		     cell30.setCellStyle(textStyle);
		 }

		  Cell cell31 = row.createCell(6);

		 if (record1.getR4_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR4_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell31.setCellValue(record1.getR4_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell31.setCellStyle(numberStyle);
		 } else {
		     cell31.setCellValue("");
		     cell31.setCellStyle(textStyle);
		 }

		  Cell cell32 = row.createCell(7);

		 if (record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell32.setCellValue(record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell32.setCellStyle(numberStyle);
		 } else {
		     cell32.setCellValue("");
		     cell32.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(14) != null ? sheet.getRow(14) : sheet.createRow(14);
		  Cell cell33 = row.createCell(0);

		 if (record1.getR5_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR5_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell33.setCellValue(record1.getR5_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell33.setCellStyle(numberStyle);
		 } else {
		     cell33.setCellValue("");
		     cell33.setCellStyle(textStyle);
		 }

		  Cell cell34 = row.createCell(1);

		 if (record1.getR5_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR5_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell34.setCellValue(record1.getR5_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell34.setCellStyle(numberStyle);
		 } else {
		     cell34.setCellValue("");
		     cell34.setCellStyle(textStyle);
		 }

		  Cell cell35 = row.createCell(2);

		 if (record1.getR5_PURPOSE_NOSTRO() != null && !record1.getR5_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell35.setCellValue(record1.getR5_PURPOSE_NOSTRO().toString().trim() );
		     cell35.setCellStyle(numberStyle);
		 } else {
		     cell35.setCellValue("");
		     cell35.setCellStyle(textStyle);
		 }

		  Cell cell36 = row.createCell(3);

		 if (record1.getR5_CURRENCY_NOSTRO() != null && !record1.getR5_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell36.setCellValue(record1.getR5_CURRENCY_NOSTRO().toString().trim() );
		     cell36.setCellStyle(numberStyle);
		 } else {
		     cell36.setCellValue("");
		     cell36.setCellStyle(textStyle);
		 }

		  Cell cell37 = row.createCell(4);

		 if (record1.getR5_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR5_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell37.setCellValue(record1.getR5_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell37.setCellStyle(numberStyle);
		 } else {
		     cell37.setCellValue("");
		     cell37.setCellStyle(textStyle);
		 }

		  Cell cell38 = row.createCell(5);

		 if (record1.getR5_RISK_WEIGHT_NOSTRO() != null && !record1.getR5_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell38.setCellValue(record1.getR5_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell38.setCellStyle(numberStyle);
		 } else {
		     cell38.setCellValue("");
		     cell38.setCellStyle(textStyle);
		 }

		  Cell cell39 = row.createCell(6);

		 if (record1.getR5_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR5_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell39.setCellValue(record1.getR5_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell39.setCellStyle(numberStyle);
		 } else {
		     cell39.setCellValue("");
		     cell39.setCellStyle(textStyle);
		 }

		  Cell cell40 = row.createCell(7);

		 if (record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell40.setCellValue(record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell40.setCellStyle(numberStyle);
		 } else {
		     cell40.setCellValue("");
		     cell40.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(15) != null ? sheet.getRow(15) : sheet.createRow(15);
		  Cell cell41 = row.createCell(0);

		 if (record1.getR6_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR6_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell41.setCellValue(record1.getR6_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell41.setCellStyle(numberStyle);
		 } else {
		     cell41.setCellValue("");
		     cell41.setCellStyle(textStyle);
		 }

		  Cell cell42 = row.createCell(1);

		 if (record1.getR6_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR6_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell42.setCellValue(record1.getR6_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell42.setCellStyle(numberStyle);
		 } else {
		     cell42.setCellValue("");
		     cell42.setCellStyle(textStyle);
		 }

		  Cell cell43 = row.createCell(2);

		 if (record1.getR6_PURPOSE_NOSTRO() != null && !record1.getR6_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell43.setCellValue(record1.getR6_PURPOSE_NOSTRO().toString().trim() );
		     cell43.setCellStyle(numberStyle);
		 } else {
		     cell43.setCellValue("");
		     cell43.setCellStyle(textStyle);
		 }

		  Cell cell44 = row.createCell(3);

		 if (record1.getR6_CURRENCY_NOSTRO() != null && !record1.getR6_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell44.setCellValue(record1.getR6_CURRENCY_NOSTRO().toString().trim() );
		     cell44.setCellStyle(numberStyle);
		 } else {
		     cell44.setCellValue("");
		     cell44.setCellStyle(textStyle);
		 }

		  Cell cell45 = row.createCell(4);

		 if (record1.getR6_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR6_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell45.setCellValue(record1.getR6_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell45.setCellStyle(numberStyle);
		 } else {
		     cell45.setCellValue("");
		     cell45.setCellStyle(textStyle);
		 }

		  Cell cell46 = row.createCell(5);

		 if (record1.getR6_RISK_WEIGHT_NOSTRO() != null && !record1.getR6_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell46.setCellValue(record1.getR6_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell46.setCellStyle(numberStyle);
		 } else {
		     cell46.setCellValue("");
		     cell46.setCellStyle(textStyle);
		 }

		  Cell cell47 = row.createCell(6);

		 if (record1.getR6_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR6_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell47.setCellValue(record1.getR6_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell47.setCellStyle(numberStyle);
		 } else {
		     cell47.setCellValue("");
		     cell47.setCellStyle(textStyle);
		 }

		  Cell cell48 = row.createCell(7);

		 if (record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell48.setCellValue(record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell48.setCellStyle(numberStyle);
		 } else {
		     cell48.setCellValue("");
		     cell48.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(16) != null ? sheet.getRow(16) : sheet.createRow(16);
		  Cell cell49 = row.createCell(0);

		 if (record1.getR7_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR7_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell49.setCellValue(record1.getR7_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell49.setCellStyle(numberStyle);
		 } else {
		     cell49.setCellValue("");
		     cell49.setCellStyle(textStyle);
		 }

		  Cell cell50 = row.createCell(1);

		 if (record1.getR7_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR7_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell50.setCellValue(record1.getR7_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell50.setCellStyle(numberStyle);
		 } else {
		     cell50.setCellValue("");
		     cell50.setCellStyle(textStyle);
		 }

		  Cell cell51 = row.createCell(2);

		 if (record1.getR7_PURPOSE_NOSTRO() != null && !record1.getR7_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell51.setCellValue(record1.getR7_PURPOSE_NOSTRO().toString().trim() );
		     cell51.setCellStyle(numberStyle);
		 } else {
		     cell51.setCellValue("");
		     cell51.setCellStyle(textStyle);
		 }

		  Cell cell52 = row.createCell(3);

		 if (record1.getR7_CURRENCY_NOSTRO() != null && !record1.getR7_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell52.setCellValue(record1.getR7_CURRENCY_NOSTRO().toString().trim() );
		     cell52.setCellStyle(numberStyle);
		 } else {
		     cell52.setCellValue("");
		     cell52.setCellStyle(textStyle);
		 }

		  Cell cell53 = row.createCell(4);

		 if (record1.getR7_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR7_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell53.setCellValue(record1.getR7_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell53.setCellStyle(numberStyle);
		 } else {
		     cell53.setCellValue("");
		     cell53.setCellStyle(textStyle);
		 }

		  Cell cell54 = row.createCell(5);

		 if (record1.getR7_RISK_WEIGHT_NOSTRO() != null && !record1.getR7_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell54.setCellValue(record1.getR7_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell54.setCellStyle(numberStyle);
		 } else {
		     cell54.setCellValue("");
		     cell54.setCellStyle(textStyle);
		 }

		  Cell cell55 = row.createCell(6);

		 if (record1.getR7_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR7_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell55.setCellValue(record1.getR7_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell55.setCellStyle(numberStyle);
		 } else {
		     cell55.setCellValue("");
		     cell55.setCellStyle(textStyle);
		 }

		  Cell cell56 = row.createCell(7);

		 if (record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell56.setCellValue(record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell56.setCellStyle(numberStyle);
		 } else {
		     cell56.setCellValue("");
		     cell56.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(17) != null ? sheet.getRow(17) : sheet.createRow(17);
		  Cell cell57 = row.createCell(0);

		 if (record1.getR8_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR8_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell57.setCellValue(record1.getR8_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell57.setCellStyle(numberStyle);
		 } else {
		     cell57.setCellValue("");
		     cell57.setCellStyle(textStyle);
		 }

		  Cell cell58 = row.createCell(1);

		 if (record1.getR8_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR8_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell58.setCellValue(record1.getR8_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell58.setCellStyle(numberStyle);
		 } else {
		     cell58.setCellValue("");
		     cell58.setCellStyle(textStyle);
		 }

		  Cell cell59 = row.createCell(2);

		 if (record1.getR8_PURPOSE_NOSTRO() != null && !record1.getR8_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell59.setCellValue(record1.getR8_PURPOSE_NOSTRO().toString().trim() );
		     cell59.setCellStyle(numberStyle);
		 } else {
		     cell59.setCellValue("");
		     cell59.setCellStyle(textStyle);
		 }

		  Cell cell60 = row.createCell(3);

		 if (record1.getR8_CURRENCY_NOSTRO() != null && !record1.getR8_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell60.setCellValue(record1.getR8_CURRENCY_NOSTRO().toString().trim() );
		     cell60.setCellStyle(numberStyle);
		 } else {
		     cell60.setCellValue("");
		     cell60.setCellStyle(textStyle);
		 }

		  Cell cell61 = row.createCell(4);

		 if (record1.getR8_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR8_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell61.setCellValue(record1.getR8_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell61.setCellStyle(numberStyle);
		 } else {
		     cell61.setCellValue("");
		     cell61.setCellStyle(textStyle);
		 }

		  Cell cell62 = row.createCell(5);

		 if (record1.getR8_RISK_WEIGHT_NOSTRO() != null && !record1.getR8_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell62.setCellValue(record1.getR8_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell62.setCellStyle(numberStyle);
		 } else {
		     cell62.setCellValue("");
		     cell62.setCellStyle(textStyle);
		 }

		  Cell cell63 = row.createCell(6);

		 if (record1.getR8_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR8_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell63.setCellValue(record1.getR8_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell63.setCellStyle(numberStyle);
		 } else {
		     cell63.setCellValue("");
		     cell63.setCellStyle(textStyle);
		 }

		  Cell cell64 = row.createCell(7);

		 if (record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell64.setCellValue(record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell64.setCellStyle(numberStyle);
		 } else {
		     cell64.setCellValue("");
		     cell64.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(18) != null ? sheet.getRow(18) : sheet.createRow(18);
		  Cell cell65 = row.createCell(0);

		 if (record1.getR9_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR9_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell65.setCellValue(record1.getR9_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell65.setCellStyle(numberStyle);
		 } else {
		     cell65.setCellValue("");
		     cell65.setCellStyle(textStyle);
		 }

		  Cell cell66 = row.createCell(1);

		 if (record1.getR9_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR9_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell66.setCellValue(record1.getR9_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell66.setCellStyle(numberStyle);
		 } else {
		     cell66.setCellValue("");
		     cell66.setCellStyle(textStyle);
		 }

		  Cell cell67 = row.createCell(2);

		 if (record1.getR9_PURPOSE_NOSTRO() != null && !record1.getR9_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell67.setCellValue(record1.getR9_PURPOSE_NOSTRO().toString().trim() );
		     cell67.setCellStyle(numberStyle);
		 } else {
		     cell67.setCellValue("");
		     cell67.setCellStyle(textStyle);
		 }

		  Cell cell68 = row.createCell(3);

		 if (record1.getR9_CURRENCY_NOSTRO() != null && !record1.getR9_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell68.setCellValue(record1.getR9_CURRENCY_NOSTRO().toString().trim() );
		     cell68.setCellStyle(numberStyle);
		 } else {
		     cell68.setCellValue("");
		     cell68.setCellStyle(textStyle);
		 }

		  Cell cell69 = row.createCell(4);

		 if (record1.getR9_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR9_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell69.setCellValue(record1.getR9_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell69.setCellStyle(numberStyle);
		 } else {
		     cell69.setCellValue("");
		     cell69.setCellStyle(textStyle);
		 }

		  Cell cell70 = row.createCell(5);

		 if (record1.getR9_RISK_WEIGHT_NOSTRO() != null && !record1.getR9_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell70.setCellValue(record1.getR9_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell70.setCellStyle(numberStyle);
		 } else {
		     cell70.setCellValue("");
		     cell70.setCellStyle(textStyle);
		 }

		  Cell cell71 = row.createCell(6);

		 if (record1.getR9_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR9_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell71.setCellValue(record1.getR9_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell71.setCellStyle(numberStyle);
		 } else {
		     cell71.setCellValue("");
		     cell71.setCellStyle(textStyle);
		 }

		  Cell cell72 = row.createCell(7);

		 if (record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell72.setCellValue(record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell72.setCellStyle(numberStyle);
		 } else {
		     cell72.setCellValue("");
		     cell72.setCellStyle(textStyle);
		 }




		}
	
	private void writeEmailResubExcelRowData2(Sheet sheet, List<BrrsMNosvosP2ResbuSummaryEntity> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
		
			System.out.println("came to write row data 1 method");
		
			BrrsMNosvosP2ResbuSummaryEntity record1 = dataList.get(0);
			
			Row  row = sheet.getRow(23) != null ? sheet.getRow(23) : sheet.createRow(23);
			 Cell cell1 = row.createCell(0);

			 if (record1.getR1_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR1_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell1.setCellValue(record1.getR1_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell1.setCellStyle(numberStyle);
			 } else {
			     cell1.setCellValue("");
			     cell1.setCellStyle(textStyle);
			 }

			  Cell cell2 = row.createCell(1);

			 if (record1.getR1_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR1_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell2.setCellValue(record1.getR1_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell2.setCellStyle(numberStyle);
			 } else {
			     cell2.setCellValue("");
			     cell2.setCellStyle(textStyle);
			 }

			  Cell cell3 = row.createCell(2);

			 if (record1.getR1_PURPOSE_VOSTRO() != null && !record1.getR1_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell3.setCellValue(record1.getR1_PURPOSE_VOSTRO().toString().trim() );
			     cell3.setCellStyle(numberStyle);
			 } else {
			     cell3.setCellValue("");
			     cell3.setCellStyle(textStyle);
			 }

			  Cell cell4 = row.createCell(3);

			 if (record1.getR1_CURRENCY_VOSTRO() != null && !record1.getR1_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell4.setCellValue(record1.getR1_CURRENCY_VOSTRO().toString().trim() );
			     cell4.setCellStyle(numberStyle);
			 } else {
			     cell4.setCellValue("");
			     cell4.setCellStyle(textStyle);
			 }

			  Cell cell7 = row.createCell(6);

			 if (record1.getR1_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR1_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell7.setCellValue(record1.getR1_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell7.setCellStyle(numberStyle);
			 } else {
			     cell7.setCellValue("");
			     cell7.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(24) != null ? sheet.getRow(24) : sheet.createRow(24);
			  Cell cell9 = row.createCell(0);

			 if (record1.getR2_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR2_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell9.setCellValue(record1.getR2_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell9.setCellStyle(numberStyle);
			 } else {
			     cell9.setCellValue("");
			     cell9.setCellStyle(textStyle);
			 }

			  Cell cell10 = row.createCell(1);

			 if (record1.getR2_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR2_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell10.setCellValue(record1.getR2_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell10.setCellStyle(numberStyle);
			 } else {
			     cell10.setCellValue("");
			     cell10.setCellStyle(textStyle);
			 }

			  Cell cell11 = row.createCell(2);

			 if (record1.getR2_PURPOSE_VOSTRO() != null && !record1.getR2_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell11.setCellValue(record1.getR2_PURPOSE_VOSTRO().toString().trim() );
			     cell11.setCellStyle(numberStyle);
			 } else {
			     cell11.setCellValue("");
			     cell11.setCellStyle(textStyle);
			 }

			  Cell cell12 = row.createCell(3);

			 if (record1.getR2_CURRENCY_VOSTRO() != null && !record1.getR2_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell12.setCellValue(record1.getR2_CURRENCY_VOSTRO().toString().trim() );
			     cell12.setCellStyle(numberStyle);
			 } else {
			     cell12.setCellValue("");
			     cell12.setCellStyle(textStyle);
			 }

			  Cell cell15 = row.createCell(6);

			 if (record1.getR2_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR2_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell15.setCellValue(record1.getR2_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell15.setCellStyle(numberStyle);
			 } else {
			     cell15.setCellValue("");
			     cell15.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(25) != null ? sheet.getRow(25) : sheet.createRow(25);
			  Cell cell17 = row.createCell(0);

			 if (record1.getR3_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR3_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell17.setCellValue(record1.getR3_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell17.setCellStyle(numberStyle);
			 } else {
			     cell17.setCellValue("");
			     cell17.setCellStyle(textStyle);
			 }

			  Cell cell18 = row.createCell(1);

			 if (record1.getR3_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR3_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell18.setCellValue(record1.getR3_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell18.setCellStyle(numberStyle);
			 } else {
			     cell18.setCellValue("");
			     cell18.setCellStyle(textStyle);
			 }

			  Cell cell19 = row.createCell(2);

			 if (record1.getR3_PURPOSE_VOSTRO() != null && !record1.getR3_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell19.setCellValue(record1.getR3_PURPOSE_VOSTRO().toString().trim() );
			     cell19.setCellStyle(numberStyle);
			 } else {
			     cell19.setCellValue("");
			     cell19.setCellStyle(textStyle);
			 }

			  Cell cell20 = row.createCell(3);

			 if (record1.getR3_CURRENCY_VOSTRO() != null && !record1.getR3_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell20.setCellValue(record1.getR3_CURRENCY_VOSTRO().toString().trim() );
			     cell20.setCellStyle(numberStyle);
			 } else {
			     cell20.setCellValue("");
			     cell20.setCellStyle(textStyle);
			 }

			  Cell cell23 = row.createCell(6);

			 if (record1.getR3_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR3_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell23.setCellValue(record1.getR3_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell23.setCellStyle(numberStyle);
			 } else {
			     cell23.setCellValue("");
			     cell23.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(26) != null ? sheet.getRow(26) : sheet.createRow(26);
			  Cell cell25 = row.createCell(0);

			 if (record1.getR4_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR4_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell25.setCellValue(record1.getR4_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell25.setCellStyle(numberStyle);
			 } else {
			     cell25.setCellValue("");
			     cell25.setCellStyle(textStyle);
			 }

			  Cell cell26 = row.createCell(1);

			 if (record1.getR4_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR4_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell26.setCellValue(record1.getR4_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell26.setCellStyle(numberStyle);
			 } else {
			     cell26.setCellValue("");
			     cell26.setCellStyle(textStyle);
			 }

			  Cell cell27 = row.createCell(2);

			 if (record1.getR4_PURPOSE_VOSTRO() != null && !record1.getR4_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell27.setCellValue(record1.getR4_PURPOSE_VOSTRO().toString().trim() );
			     cell27.setCellStyle(numberStyle);
			 } else {
			     cell27.setCellValue("");
			     cell27.setCellStyle(textStyle);
			 }

			  Cell cell28 = row.createCell(3);

			 if (record1.getR4_CURRENCY_VOSTRO() != null && !record1.getR4_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell28.setCellValue(record1.getR4_CURRENCY_VOSTRO().toString().trim() );
			     cell28.setCellStyle(numberStyle);
			 } else {
			     cell28.setCellValue("");
			     cell28.setCellStyle(textStyle);
			 }

			  Cell cell31 = row.createCell(6);

			 if (record1.getR4_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR4_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell31.setCellValue(record1.getR4_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell31.setCellStyle(numberStyle);
			 } else {
			     cell31.setCellValue("");
			     cell31.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(27) != null ? sheet.getRow(27) : sheet.createRow(27);
			  Cell cell33 = row.createCell(0);

			 if (record1.getR5_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR5_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell33.setCellValue(record1.getR5_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell33.setCellStyle(numberStyle);
			 } else {
			     cell33.setCellValue("");
			     cell33.setCellStyle(textStyle);
			 }

			  Cell cell34 = row.createCell(1);

			 if (record1.getR5_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR5_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell34.setCellValue(record1.getR5_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell34.setCellStyle(numberStyle);
			 } else {
			     cell34.setCellValue("");
			     cell34.setCellStyle(textStyle);
			 }

			  Cell cell35 = row.createCell(2);

			 if (record1.getR5_PURPOSE_VOSTRO() != null && !record1.getR5_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell35.setCellValue(record1.getR5_PURPOSE_VOSTRO().toString().trim() );
			     cell35.setCellStyle(numberStyle);
			 } else {
			     cell35.setCellValue("");
			     cell35.setCellStyle(textStyle);
			 }

			  Cell cell36 = row.createCell(3);

			 if (record1.getR5_CURRENCY_VOSTRO() != null && !record1.getR5_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell36.setCellValue(record1.getR5_CURRENCY_VOSTRO().toString().trim() );
			     cell36.setCellStyle(numberStyle);
			 } else {
			     cell36.setCellValue("");
			     cell36.setCellStyle(textStyle);
			 }

			  Cell cell39 = row.createCell(6);

			 if (record1.getR5_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR5_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell39.setCellValue(record1.getR5_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell39.setCellStyle(numberStyle);
			 } else {
			     cell39.setCellValue("");
			     cell39.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(28) != null ? sheet.getRow(28) : sheet.createRow(28);
			  Cell cell41 = row.createCell(0);

			 if (record1.getR6_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR6_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell41.setCellValue(record1.getR6_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell41.setCellStyle(numberStyle);
			 } else {
			     cell41.setCellValue("");
			     cell41.setCellStyle(textStyle);
			 }

			  Cell cell42 = row.createCell(1);

			 if (record1.getR6_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR6_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell42.setCellValue(record1.getR6_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell42.setCellStyle(numberStyle);
			 } else {
			     cell42.setCellValue("");
			     cell42.setCellStyle(textStyle);
			 }

			  Cell cell43 = row.createCell(2);

			 if (record1.getR6_PURPOSE_VOSTRO() != null && !record1.getR6_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell43.setCellValue(record1.getR6_PURPOSE_VOSTRO().toString().trim() );
			     cell43.setCellStyle(numberStyle);
			 } else {
			     cell43.setCellValue("");
			     cell43.setCellStyle(textStyle);
			 }

			  Cell cell44 = row.createCell(3);

			 if (record1.getR6_CURRENCY_VOSTRO() != null && !record1.getR6_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell44.setCellValue(record1.getR6_CURRENCY_VOSTRO().toString().trim() );
			     cell44.setCellStyle(numberStyle);
			 } else {
			     cell44.setCellValue("");
			     cell44.setCellStyle(textStyle);
			 }

			  Cell cell47 = row.createCell(6);

			 if (record1.getR6_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR6_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell47.setCellValue(record1.getR6_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell47.setCellStyle(numberStyle);
			 } else {
			     cell47.setCellValue("");
			     cell47.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(29) != null ? sheet.getRow(29) : sheet.createRow(29);
			  Cell cell49 = row.createCell(0);

			 if (record1.getR7_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR7_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell49.setCellValue(record1.getR7_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell49.setCellStyle(numberStyle);
			 } else {
			     cell49.setCellValue("");
			     cell49.setCellStyle(textStyle);
			 }

			  Cell cell50 = row.createCell(1);

			 if (record1.getR7_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR7_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell50.setCellValue(record1.getR7_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell50.setCellStyle(numberStyle);
			 } else {
			     cell50.setCellValue("");
			     cell50.setCellStyle(textStyle);
			 }

			  Cell cell51 = row.createCell(2);

			 if (record1.getR7_PURPOSE_VOSTRO() != null && !record1.getR7_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell51.setCellValue(record1.getR7_PURPOSE_VOSTRO().toString().trim() );
			     cell51.setCellStyle(numberStyle);
			 } else {
			     cell51.setCellValue("");
			     cell51.setCellStyle(textStyle);
			 }

			  Cell cell52 = row.createCell(3);

			 if (record1.getR7_CURRENCY_VOSTRO() != null && !record1.getR7_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell52.setCellValue(record1.getR7_CURRENCY_VOSTRO().toString().trim() );
			     cell52.setCellStyle(numberStyle);
			 } else {
			     cell52.setCellValue("");
			     cell52.setCellStyle(textStyle);
			 }

			  Cell cell55 = row.createCell(6);

			 if (record1.getR7_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR7_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell55.setCellValue(record1.getR7_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell55.setCellStyle(numberStyle);
			 } else {
			     cell55.setCellValue("");
			     cell55.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(30) != null ? sheet.getRow(30) : sheet.createRow(30);
			  Cell cell57 = row.createCell(0);

			 if (record1.getR8_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR8_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell57.setCellValue(record1.getR8_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell57.setCellStyle(numberStyle);
			 } else {
			     cell57.setCellValue("");
			     cell57.setCellStyle(textStyle);
			 }

			  Cell cell58 = row.createCell(1);

			 if (record1.getR8_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR8_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell58.setCellValue(record1.getR8_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell58.setCellStyle(numberStyle);
			 } else {
			     cell58.setCellValue("");
			     cell58.setCellStyle(textStyle);
			 }

			  Cell cell59 = row.createCell(2);

			 if (record1.getR8_PURPOSE_VOSTRO() != null && !record1.getR8_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell59.setCellValue(record1.getR8_PURPOSE_VOSTRO().toString().trim() );
			     cell59.setCellStyle(numberStyle);
			 } else {
			     cell59.setCellValue("");
			     cell59.setCellStyle(textStyle);
			 }

			  Cell cell60 = row.createCell(3);

			 if (record1.getR8_CURRENCY_VOSTRO() != null && !record1.getR8_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell60.setCellValue(record1.getR8_CURRENCY_VOSTRO().toString().trim() );
			     cell60.setCellStyle(numberStyle);
			 } else {
			     cell60.setCellValue("");
			     cell60.setCellStyle(textStyle);
			 }

			  Cell cell63 = row.createCell(6);

			 if (record1.getR8_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR8_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell63.setCellValue(record1.getR8_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell63.setCellStyle(numberStyle);
			 } else {
			     cell63.setCellValue("");
			     cell63.setCellStyle(textStyle);
			 }



			
		}
	
	private void writeEmailResubExcelRowData3(Sheet sheet, List<BrrsMNosvosP3ResbuSummaryEntity> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			
		System.out.println("came to write row data 1 method");
		
		BrrsMNosvosP3ResbuSummaryEntity record1 = dataList.get(0);
		
		Row  row = sheet.getRow(36) != null ? sheet.getRow(36) : sheet.createRow(36);
		 Cell cell1 = row.createCell(0);

		 if (record1.getR1_NAME_OF_BANK_NOSTRO1() != null && !record1.getR1_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell1.setCellValue(record1.getR1_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell1.setCellStyle(numberStyle);
		 } else {
		     cell1.setCellValue("");
		     cell1.setCellStyle(textStyle);
		 }

		  Cell cell2 = row.createCell(1);

		 if (record1.getR1_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR1_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell2.setCellValue(record1.getR1_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell2.setCellStyle(numberStyle);
		 } else {
		     cell2.setCellValue("");
		     cell2.setCellStyle(textStyle);
		 }

		  Cell cell3 = row.createCell(2);

		 if (record1.getR1_PURPOSE_NOSTRO1() != null && !record1.getR1_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell3.setCellValue(record1.getR1_PURPOSE_NOSTRO1().toString().trim() );
		     cell3.setCellStyle(numberStyle);
		 } else {
		     cell3.setCellValue("");
		     cell3.setCellStyle(textStyle);
		 }

		  Cell cell4 = row.createCell(3);

		 if (record1.getR1_CURRENCY_NOSTRO1() != null && !record1.getR1_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell4.setCellValue(record1.getR1_CURRENCY_NOSTRO1().toString().trim() );
		     cell4.setCellStyle(numberStyle);
		 } else {
		     cell4.setCellValue("");
		     cell4.setCellStyle(textStyle);
		 }

		  Cell cell5 = row.createCell(4);

		 if (record1.getR1_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR1_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell5.setCellValue(record1.getR1_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell5.setCellStyle(numberStyle);
		 } else {
		     cell5.setCellValue("");
		     cell5.setCellStyle(textStyle);
		 }

		  Cell cell6 = row.createCell(5);

		 if (record1.getR1_RISK_WEIGHT_NOSTRO1() != null && !record1.getR1_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell6.setCellValue(record1.getR1_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell6.setCellStyle(numberStyle);
		 } else {
		     cell6.setCellValue("");
		     cell6.setCellStyle(textStyle);
		 }

		  Cell cell7 = row.createCell(6);

		 if (record1.getR1_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR1_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell7.setCellValue(record1.getR1_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell7.setCellStyle(numberStyle);
		 } else {
		     cell7.setCellValue("");
		     cell7.setCellStyle(textStyle);
		 }

		  Cell cell8 = row.createCell(7);

		 if (record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell8.setCellValue(record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell8.setCellStyle(numberStyle);
		 } else {
		     cell8.setCellValue("");
		     cell8.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(37) != null ? sheet.getRow(37) : sheet.createRow(37);
		  Cell cell9 = row.createCell(0);

		 if (record1.getR2_NAME_OF_BANK_NOSTRO1() != null && !record1.getR2_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell9.setCellValue(record1.getR2_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell9.setCellStyle(numberStyle);
		 } else {
		     cell9.setCellValue("");
		     cell9.setCellStyle(textStyle);
		 }

		  Cell cell10 = row.createCell(1);

		 if (record1.getR2_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR2_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell10.setCellValue(record1.getR2_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell10.setCellStyle(numberStyle);
		 } else {
		     cell10.setCellValue("");
		     cell10.setCellStyle(textStyle);
		 }

		  Cell cell11 = row.createCell(2);

		 if (record1.getR2_PURPOSE_NOSTRO1() != null && !record1.getR2_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell11.setCellValue(record1.getR2_PURPOSE_NOSTRO1().toString().trim() );
		     cell11.setCellStyle(numberStyle);
		 } else {
		     cell11.setCellValue("");
		     cell11.setCellStyle(textStyle);
		 }

		  Cell cell12 = row.createCell(3);

		 if (record1.getR2_CURRENCY_NOSTRO1() != null && !record1.getR2_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell12.setCellValue(record1.getR2_CURRENCY_NOSTRO1().toString().trim() );
		     cell12.setCellStyle(numberStyle);
		 } else {
		     cell12.setCellValue("");
		     cell12.setCellStyle(textStyle);
		 }

		  Cell cell13 = row.createCell(4);

		 if (record1.getR2_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR2_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell13.setCellValue(record1.getR2_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell13.setCellStyle(numberStyle);
		 } else {
		     cell13.setCellValue("");
		     cell13.setCellStyle(textStyle);
		 }

		  Cell cell14 = row.createCell(5);

		 if (record1.getR2_RISK_WEIGHT_NOSTRO1() != null && !record1.getR2_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell14.setCellValue(record1.getR2_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell14.setCellStyle(numberStyle);
		 } else {
		     cell14.setCellValue("");
		     cell14.setCellStyle(textStyle);
		 }

		  Cell cell15 = row.createCell(6);

		 if (record1.getR2_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR2_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell15.setCellValue(record1.getR2_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell15.setCellStyle(numberStyle);
		 } else {
		     cell15.setCellValue("");
		     cell15.setCellStyle(textStyle);
		 }

		  Cell cell16 = row.createCell(7);

		 if (record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell16.setCellValue(record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell16.setCellStyle(numberStyle);
		 } else {
		     cell16.setCellValue("");
		     cell16.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(38) != null ? sheet.getRow(38) : sheet.createRow(38);
		  Cell cell17 = row.createCell(0);

		 if (record1.getR3_NAME_OF_BANK_NOSTRO1() != null && !record1.getR3_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell17.setCellValue(record1.getR3_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell17.setCellStyle(numberStyle);
		 } else {
		     cell17.setCellValue("");
		     cell17.setCellStyle(textStyle);
		 }

		  Cell cell18 = row.createCell(1);

		 if (record1.getR3_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR3_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell18.setCellValue(record1.getR3_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell18.setCellStyle(numberStyle);
		 } else {
		     cell18.setCellValue("");
		     cell18.setCellStyle(textStyle);
		 }

		  Cell cell19 = row.createCell(2);

		 if (record1.getR3_PURPOSE_NOSTRO1() != null && !record1.getR3_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell19.setCellValue(record1.getR3_PURPOSE_NOSTRO1().toString().trim() );
		     cell19.setCellStyle(numberStyle);
		 } else {
		     cell19.setCellValue("");
		     cell19.setCellStyle(textStyle);
		 }

		  Cell cell20 = row.createCell(3);

		 if (record1.getR3_CURRENCY_NOSTRO1() != null && !record1.getR3_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell20.setCellValue(record1.getR3_CURRENCY_NOSTRO1().toString().trim() );
		     cell20.setCellStyle(numberStyle);
		 } else {
		     cell20.setCellValue("");
		     cell20.setCellStyle(textStyle);
		 }

		  Cell cell21 = row.createCell(4);

		 if (record1.getR3_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR3_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell21.setCellValue(record1.getR3_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell21.setCellStyle(numberStyle);
		 } else {
		     cell21.setCellValue("");
		     cell21.setCellStyle(textStyle);
		 }

		  Cell cell22 = row.createCell(5);

		 if (record1.getR3_RISK_WEIGHT_NOSTRO1() != null && !record1.getR3_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell22.setCellValue(record1.getR3_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell22.setCellStyle(numberStyle);
		 } else {
		     cell22.setCellValue("");
		     cell22.setCellStyle(textStyle);
		 }

		  Cell cell23 = row.createCell(6);

		 if (record1.getR3_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR3_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell23.setCellValue(record1.getR3_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell23.setCellStyle(numberStyle);
		 } else {
		     cell23.setCellValue("");
		     cell23.setCellStyle(textStyle);
		 }

		  Cell cell24 = row.createCell(7);

		 if (record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell24.setCellValue(record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell24.setCellStyle(numberStyle);
		 } else {
		     cell24.setCellValue("");
		     cell24.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(39) != null ? sheet.getRow(39) : sheet.createRow(39);
		  Cell cell25 = row.createCell(0);

		 if (record1.getR4_NAME_OF_BANK_NOSTRO1() != null && !record1.getR4_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell25.setCellValue(record1.getR4_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell25.setCellStyle(numberStyle);
		 } else {
		     cell25.setCellValue("");
		     cell25.setCellStyle(textStyle);
		 }

		  Cell cell26 = row.createCell(1);

		 if (record1.getR4_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR4_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell26.setCellValue(record1.getR4_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell26.setCellStyle(numberStyle);
		 } else {
		     cell26.setCellValue("");
		     cell26.setCellStyle(textStyle);
		 }

		  Cell cell27 = row.createCell(2);

		 if (record1.getR4_PURPOSE_NOSTRO1() != null && !record1.getR4_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell27.setCellValue(record1.getR4_PURPOSE_NOSTRO1().toString().trim() );
		     cell27.setCellStyle(numberStyle);
		 } else {
		     cell27.setCellValue("");
		     cell27.setCellStyle(textStyle);
		 }

		  Cell cell28 = row.createCell(3);

		 if (record1.getR4_CURRENCY_NOSTRO1() != null && !record1.getR4_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell28.setCellValue(record1.getR4_CURRENCY_NOSTRO1().toString().trim() );
		     cell28.setCellStyle(numberStyle);
		 } else {
		     cell28.setCellValue("");
		     cell28.setCellStyle(textStyle);
		 }

		  Cell cell29 = row.createCell(4);

		 if (record1.getR4_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR4_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell29.setCellValue(record1.getR4_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell29.setCellStyle(numberStyle);
		 } else {
		     cell29.setCellValue("");
		     cell29.setCellStyle(textStyle);
		 }

		  Cell cell30 = row.createCell(5);

		 if (record1.getR4_RISK_WEIGHT_NOSTRO1() != null && !record1.getR4_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell30.setCellValue(record1.getR4_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell30.setCellStyle(numberStyle);
		 } else {
		     cell30.setCellValue("");
		     cell30.setCellStyle(textStyle);
		 }

		  Cell cell31 = row.createCell(6);

		 if (record1.getR4_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR4_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell31.setCellValue(record1.getR4_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell31.setCellStyle(numberStyle);
		 } else {
		     cell31.setCellValue("");
		     cell31.setCellStyle(textStyle);
		 }

		  Cell cell32 = row.createCell(7);

		 if (record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell32.setCellValue(record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell32.setCellStyle(numberStyle);
		 } else {
		     cell32.setCellValue("");
		     cell32.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(40) != null ? sheet.getRow(40) : sheet.createRow(40);
		  Cell cell33 = row.createCell(0);

		 if (record1.getR5_NAME_OF_BANK_NOSTRO1() != null && !record1.getR5_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell33.setCellValue(record1.getR5_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell33.setCellStyle(numberStyle);
		 } else {
		     cell33.setCellValue("");
		     cell33.setCellStyle(textStyle);
		 }

		  Cell cell34 = row.createCell(1);

		 if (record1.getR5_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR5_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell34.setCellValue(record1.getR5_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell34.setCellStyle(numberStyle);
		 } else {
		     cell34.setCellValue("");
		     cell34.setCellStyle(textStyle);
		 }

		  Cell cell35 = row.createCell(2);

		 if (record1.getR5_PURPOSE_NOSTRO1() != null && !record1.getR5_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell35.setCellValue(record1.getR5_PURPOSE_NOSTRO1().toString().trim() );
		     cell35.setCellStyle(numberStyle);
		 } else {
		     cell35.setCellValue("");
		     cell35.setCellStyle(textStyle);
		 }

		  Cell cell36 = row.createCell(3);

		 if (record1.getR5_CURRENCY_NOSTRO1() != null && !record1.getR5_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell36.setCellValue(record1.getR5_CURRENCY_NOSTRO1().toString().trim() );
		     cell36.setCellStyle(numberStyle);
		 } else {
		     cell36.setCellValue("");
		     cell36.setCellStyle(textStyle);
		 }

		  Cell cell37 = row.createCell(4);

		 if (record1.getR5_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR5_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell37.setCellValue(record1.getR5_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell37.setCellStyle(numberStyle);
		 } else {
		     cell37.setCellValue("");
		     cell37.setCellStyle(textStyle);
		 }

		  Cell cell38 = row.createCell(5);

		 if (record1.getR5_RISK_WEIGHT_NOSTRO1() != null && !record1.getR5_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell38.setCellValue(record1.getR5_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell38.setCellStyle(numberStyle);
		 } else {
		     cell38.setCellValue("");
		     cell38.setCellStyle(textStyle);
		 }

		  Cell cell39 = row.createCell(6);

		 if (record1.getR5_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR5_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell39.setCellValue(record1.getR5_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell39.setCellStyle(numberStyle);
		 } else {
		     cell39.setCellValue("");
		     cell39.setCellStyle(textStyle);
		 }

		  Cell cell40 = row.createCell(7);

		 if (record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell40.setCellValue(record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell40.setCellStyle(numberStyle);
		 } else {
		     cell40.setCellValue("");
		     cell40.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(41) != null ? sheet.getRow(41) : sheet.createRow(41);
		  Cell cell41 = row.createCell(0);

		 if (record1.getR6_NAME_OF_BANK_NOSTRO1() != null && !record1.getR6_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell41.setCellValue(record1.getR6_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell41.setCellStyle(numberStyle);
		 } else {
		     cell41.setCellValue("");
		     cell41.setCellStyle(textStyle);
		 }

		  Cell cell42 = row.createCell(1);

		 if (record1.getR6_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR6_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell42.setCellValue(record1.getR6_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell42.setCellStyle(numberStyle);
		 } else {
		     cell42.setCellValue("");
		     cell42.setCellStyle(textStyle);
		 }

		  Cell cell43 = row.createCell(2);

		 if (record1.getR6_PURPOSE_NOSTRO1() != null && !record1.getR6_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell43.setCellValue(record1.getR6_PURPOSE_NOSTRO1().toString().trim() );
		     cell43.setCellStyle(numberStyle);
		 } else {
		     cell43.setCellValue("");
		     cell43.setCellStyle(textStyle);
		 }

		  Cell cell44 = row.createCell(3);

		 if (record1.getR6_CURRENCY_NOSTRO1() != null && !record1.getR6_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell44.setCellValue(record1.getR6_CURRENCY_NOSTRO1().toString().trim() );
		     cell44.setCellStyle(numberStyle);
		 } else {
		     cell44.setCellValue("");
		     cell44.setCellStyle(textStyle);
		 }

		  Cell cell45 = row.createCell(4);

		 if (record1.getR6_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR6_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell45.setCellValue(record1.getR6_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell45.setCellStyle(numberStyle);
		 } else {
		     cell45.setCellValue("");
		     cell45.setCellStyle(textStyle);
		 }

		  Cell cell46 = row.createCell(5);

		 if (record1.getR6_RISK_WEIGHT_NOSTRO1() != null && !record1.getR6_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell46.setCellValue(record1.getR6_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell46.setCellStyle(numberStyle);
		 } else {
		     cell46.setCellValue("");
		     cell46.setCellStyle(textStyle);
		 }

		  Cell cell47 = row.createCell(6);

		 if (record1.getR6_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR6_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell47.setCellValue(record1.getR6_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell47.setCellStyle(numberStyle);
		 } else {
		     cell47.setCellValue("");
		     cell47.setCellStyle(textStyle);
		 }

		  Cell cell48 = row.createCell(7);

		 if (record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell48.setCellValue(record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell48.setCellStyle(numberStyle);
		 } else {
		     cell48.setCellValue("");
		     cell48.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(42) != null ? sheet.getRow(42) : sheet.createRow(42);
		  Cell cell49 = row.createCell(0);

		 if (record1.getR7_NAME_OF_BANK_NOSTRO1() != null && !record1.getR7_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell49.setCellValue(record1.getR7_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell49.setCellStyle(numberStyle);
		 } else {
		     cell49.setCellValue("");
		     cell49.setCellStyle(textStyle);
		 }

		  Cell cell50 = row.createCell(1);

		 if (record1.getR7_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR7_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell50.setCellValue(record1.getR7_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell50.setCellStyle(numberStyle);
		 } else {
		     cell50.setCellValue("");
		     cell50.setCellStyle(textStyle);
		 }

		  Cell cell51 = row.createCell(2);

		 if (record1.getR7_PURPOSE_NOSTRO1() != null && !record1.getR7_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell51.setCellValue(record1.getR7_PURPOSE_NOSTRO1().toString().trim() );
		     cell51.setCellStyle(numberStyle);
		 } else {
		     cell51.setCellValue("");
		     cell51.setCellStyle(textStyle);
		 }

		  Cell cell52 = row.createCell(3);

		 if (record1.getR7_CURRENCY_NOSTRO1() != null && !record1.getR7_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell52.setCellValue(record1.getR7_CURRENCY_NOSTRO1().toString().trim() );
		     cell52.setCellStyle(numberStyle);
		 } else {
		     cell52.setCellValue("");
		     cell52.setCellStyle(textStyle);
		 }

		  Cell cell53 = row.createCell(4);

		 if (record1.getR7_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR7_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell53.setCellValue(record1.getR7_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell53.setCellStyle(numberStyle);
		 } else {
		     cell53.setCellValue("");
		     cell53.setCellStyle(textStyle);
		 }

		  Cell cell54 = row.createCell(5);

		 if (record1.getR7_RISK_WEIGHT_NOSTRO1() != null && !record1.getR7_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell54.setCellValue(record1.getR7_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell54.setCellStyle(numberStyle);
		 } else {
		     cell54.setCellValue("");
		     cell54.setCellStyle(textStyle);
		 }

		  Cell cell55 = row.createCell(6);

		 if (record1.getR7_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR7_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell55.setCellValue(record1.getR7_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell55.setCellStyle(numberStyle);
		 } else {
		     cell55.setCellValue("");
		     cell55.setCellStyle(textStyle);
		 }

		  Cell cell56 = row.createCell(7);

		 if (record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell56.setCellValue(record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell56.setCellStyle(numberStyle);
		 } else {
		     cell56.setCellValue("");
		     cell56.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(43) != null ? sheet.getRow(43) : sheet.createRow(43);
		  Cell cell57 = row.createCell(0);

		 if (record1.getR8_NAME_OF_BANK_NOSTRO1() != null && !record1.getR8_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell57.setCellValue(record1.getR8_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell57.setCellStyle(numberStyle);
		 } else {
		     cell57.setCellValue("");
		     cell57.setCellStyle(textStyle);
		 }

		  Cell cell58 = row.createCell(1);

		 if (record1.getR8_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR8_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell58.setCellValue(record1.getR8_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell58.setCellStyle(numberStyle);
		 } else {
		     cell58.setCellValue("");
		     cell58.setCellStyle(textStyle);
		 }

		  Cell cell59 = row.createCell(2);

		 if (record1.getR8_PURPOSE_NOSTRO1() != null && !record1.getR8_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell59.setCellValue(record1.getR8_PURPOSE_NOSTRO1().toString().trim() );
		     cell59.setCellStyle(numberStyle);
		 } else {
		     cell59.setCellValue("");
		     cell59.setCellStyle(textStyle);
		 }

		  Cell cell60 = row.createCell(3);

		 if (record1.getR8_CURRENCY_NOSTRO1() != null && !record1.getR8_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell60.setCellValue(record1.getR8_CURRENCY_NOSTRO1().toString().trim() );
		     cell60.setCellStyle(numberStyle);
		 } else {
		     cell60.setCellValue("");
		     cell60.setCellStyle(textStyle);
		 }

		  Cell cell61 = row.createCell(4);

		 if (record1.getR8_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR8_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell61.setCellValue(record1.getR8_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell61.setCellStyle(numberStyle);
		 } else {
		     cell61.setCellValue("");
		     cell61.setCellStyle(textStyle);
		 }

		  Cell cell62 = row.createCell(5);

		 if (record1.getR8_RISK_WEIGHT_NOSTRO1() != null && !record1.getR8_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell62.setCellValue(record1.getR8_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell62.setCellStyle(numberStyle);
		 } else {
		     cell62.setCellValue("");
		     cell62.setCellStyle(textStyle);
		 }

		  Cell cell63 = row.createCell(6);

		 if (record1.getR8_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR8_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell63.setCellValue(record1.getR8_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell63.setCellStyle(numberStyle);
		 } else {
		     cell63.setCellValue("");
		     cell63.setCellStyle(textStyle);
		 }

		  Cell cell64 = row.createCell(7);

		 if (record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell64.setCellValue(record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell64.setCellStyle(numberStyle);
		 } else {
		     cell64.setCellValue("");
		     cell64.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(44) != null ? sheet.getRow(44) : sheet.createRow(44);
		  Cell cell65 = row.createCell(0);

		 if (record1.getR9_NAME_OF_BANK_NOSTRO1() != null && !record1.getR9_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell65.setCellValue(record1.getR9_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell65.setCellStyle(numberStyle);
		 } else {
		     cell65.setCellValue("");
		     cell65.setCellStyle(textStyle);
		 }

		  Cell cell66 = row.createCell(1);

		 if (record1.getR9_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR9_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell66.setCellValue(record1.getR9_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell66.setCellStyle(numberStyle);
		 } else {
		     cell66.setCellValue("");
		     cell66.setCellStyle(textStyle);
		 }

		  Cell cell67 = row.createCell(2);

		 if (record1.getR9_PURPOSE_NOSTRO1() != null && !record1.getR9_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell67.setCellValue(record1.getR9_PURPOSE_NOSTRO1().toString().trim() );
		     cell67.setCellStyle(numberStyle);
		 } else {
		     cell67.setCellValue("");
		     cell67.setCellStyle(textStyle);
		 }

		  Cell cell68 = row.createCell(3);

		 if (record1.getR9_CURRENCY_NOSTRO1() != null && !record1.getR9_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell68.setCellValue(record1.getR9_CURRENCY_NOSTRO1().toString().trim() );
		     cell68.setCellStyle(numberStyle);
		 } else {
		     cell68.setCellValue("");
		     cell68.setCellStyle(textStyle);
		 }

		  Cell cell69 = row.createCell(4);

		 if (record1.getR9_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR9_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell69.setCellValue(record1.getR9_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell69.setCellStyle(numberStyle);
		 } else {
		     cell69.setCellValue("");
		     cell69.setCellStyle(textStyle);
		 }

		  Cell cell70 = row.createCell(5);

		 if (record1.getR9_RISK_WEIGHT_NOSTRO1() != null && !record1.getR9_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell70.setCellValue(record1.getR9_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell70.setCellStyle(numberStyle);
		 } else {
		     cell70.setCellValue("");
		     cell70.setCellStyle(textStyle);
		 }

		  Cell cell71 = row.createCell(6);

		 if (record1.getR9_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR9_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell71.setCellValue(record1.getR9_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell71.setCellStyle(numberStyle);
		 } else {
		     cell71.setCellValue("");
		     cell71.setCellStyle(textStyle);
		 }

		  Cell cell72 = row.createCell(7);

		 if (record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell72.setCellValue(record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell72.setCellStyle(numberStyle);
		 } else {
		     cell72.setCellValue("");
		     cell72.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(45) != null ? sheet.getRow(45) : sheet.createRow(45);
		  Cell cell73 = row.createCell(0);

		 if (record1.getR10_NAME_OF_BANK_NOSTRO1() != null && !record1.getR10_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell73.setCellValue(record1.getR10_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell73.setCellStyle(numberStyle);
		 } else {
		     cell73.setCellValue("");
		     cell73.setCellStyle(textStyle);
		 }

		  Cell cell74 = row.createCell(1);

		 if (record1.getR10_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR10_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell74.setCellValue(record1.getR10_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell74.setCellStyle(numberStyle);
		 } else {
		     cell74.setCellValue("");
		     cell74.setCellStyle(textStyle);
		 }

		  Cell cell75 = row.createCell(2);

		 if (record1.getR10_PURPOSE_NOSTRO1() != null && !record1.getR10_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell75.setCellValue(record1.getR10_PURPOSE_NOSTRO1().toString().trim() );
		     cell75.setCellStyle(numberStyle);
		 } else {
		     cell75.setCellValue("");
		     cell75.setCellStyle(textStyle);
		 }

		  Cell cell76 = row.createCell(3);

		 if (record1.getR10_CURRENCY_NOSTRO1() != null && !record1.getR10_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell76.setCellValue(record1.getR10_CURRENCY_NOSTRO1().toString().trim() );
		     cell76.setCellStyle(numberStyle);
		 } else {
		     cell76.setCellValue("");
		     cell76.setCellStyle(textStyle);
		 }

		  Cell cell77 = row.createCell(4);

		 if (record1.getR10_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR10_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell77.setCellValue(record1.getR10_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell77.setCellStyle(numberStyle);
		 } else {
		     cell77.setCellValue("");
		     cell77.setCellStyle(textStyle);
		 }

		  Cell cell78 = row.createCell(5);

		 if (record1.getR10_RISK_WEIGHT_NOSTRO1() != null && !record1.getR10_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell78.setCellValue(record1.getR10_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell78.setCellStyle(numberStyle);
		 } else {
		     cell78.setCellValue("");
		     cell78.setCellStyle(textStyle);
		 }

		  Cell cell79 = row.createCell(6);

		 if (record1.getR10_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR10_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell79.setCellValue(record1.getR10_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell79.setCellStyle(numberStyle);
		 } else {
		     cell79.setCellValue("");
		     cell79.setCellStyle(textStyle);
		 }

		  Cell cell80 = row.createCell(7);

		 if (record1.getR10_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR10_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell80.setCellValue(record1.getR10_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell80.setCellStyle(numberStyle);
		 } else {
		     cell80.setCellValue("");
		     cell80.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(46) != null ? sheet.getRow(46) : sheet.createRow(46);
		  Cell cell81 = row.createCell(0);

		 if (record1.getR11_NAME_OF_BANK_NOSTRO1() != null && !record1.getR11_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell81.setCellValue(record1.getR11_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell81.setCellStyle(numberStyle);
		 } else {
		     cell81.setCellValue("");
		     cell81.setCellStyle(textStyle);
		 }

		  Cell cell82 = row.createCell(1);

		 if (record1.getR11_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR11_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell82.setCellValue(record1.getR11_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell82.setCellStyle(numberStyle);
		 } else {
		     cell82.setCellValue("");
		     cell82.setCellStyle(textStyle);
		 }

		  Cell cell83 = row.createCell(2);

		 if (record1.getR11_PURPOSE_NOSTRO1() != null && !record1.getR11_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell83.setCellValue(record1.getR11_PURPOSE_NOSTRO1().toString().trim() );
		     cell83.setCellStyle(numberStyle);
		 } else {
		     cell83.setCellValue("");
		     cell83.setCellStyle(textStyle);
		 }

		  Cell cell84 = row.createCell(3);

		 if (record1.getR11_CURRENCY_NOSTRO1() != null && !record1.getR11_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell84.setCellValue(record1.getR11_CURRENCY_NOSTRO1().toString().trim() );
		     cell84.setCellStyle(numberStyle);
		 } else {
		     cell84.setCellValue("");
		     cell84.setCellStyle(textStyle);
		 }

		  Cell cell85 = row.createCell(4);

		 if (record1.getR11_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR11_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell85.setCellValue(record1.getR11_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell85.setCellStyle(numberStyle);
		 } else {
		     cell85.setCellValue("");
		     cell85.setCellStyle(textStyle);
		 }

		  Cell cell86 = row.createCell(5);

		 if (record1.getR11_RISK_WEIGHT_NOSTRO1() != null && !record1.getR11_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell86.setCellValue(record1.getR11_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell86.setCellStyle(numberStyle);
		 } else {
		     cell86.setCellValue("");
		     cell86.setCellStyle(textStyle);
		 }

		  Cell cell87 = row.createCell(6);

		 if (record1.getR11_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR11_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell87.setCellValue(record1.getR11_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell87.setCellStyle(numberStyle);
		 } else {
		     cell87.setCellValue("");
		     cell87.setCellStyle(textStyle);
		 }

		  Cell cell88 = row.createCell(7);

		 if (record1.getR11_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR11_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell88.setCellValue(record1.getR11_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell88.setCellStyle(numberStyle);
		 } else {
		     cell88.setCellValue("");
		     cell88.setCellStyle(textStyle);
		 }



			
		}
	
	private void writeEmailResubExcelRowData4(Sheet sheet, List<BrrsMNosvosP4ResbuSummaryEntity> dataList,
	        String[] rowCodes, String[] fieldSuffixes, int baseRow,
	        CellStyle numberStyle, CellStyle textStyle) {
	    System.out.println("came to write row data 4 method");

	    System.out.println("came to write row data 1 method");
		
	    BrrsMNosvosP4ResbuSummaryEntity record1 = dataList.get(0);
	    
	    Row  row = sheet.getRow(51) != null ? sheet.getRow(51) : sheet.createRow(51);
	    Cell cell1 = row.createCell(0);

	    if (record1.getR1_NAME_OF_BANK_VOSTRO1() != null && !record1.getR1_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell1.setCellValue(record1.getR1_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell1.setCellStyle(numberStyle);
	    } else {
	        cell1.setCellValue("");
	        cell1.setCellStyle(textStyle);
	    }

	     Cell cell2 = row.createCell(1);

	    if (record1.getR1_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR1_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell2.setCellValue(record1.getR1_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell2.setCellStyle(numberStyle);
	    } else {
	        cell2.setCellValue("");
	        cell2.setCellStyle(textStyle);
	    }

	     Cell cell3 = row.createCell(2);

	    if (record1.getR1_PURPOSE_VOSTRO1() != null && !record1.getR1_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell3.setCellValue(record1.getR1_PURPOSE_VOSTRO1().toString().trim() );
	        cell3.setCellStyle(numberStyle);
	    } else {
	        cell3.setCellValue("");
	        cell3.setCellStyle(textStyle);
	    }

	     Cell cell4 = row.createCell(3);

	    if (record1.getR1_CURRENCY_VOSTRO1() != null && !record1.getR1_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell4.setCellValue(record1.getR1_CURRENCY_VOSTRO1().toString().trim() );
	        cell4.setCellStyle(numberStyle);
	    } else {
	        cell4.setCellValue("");
	        cell4.setCellStyle(textStyle);
	    }

	     Cell cell7 = row.createCell(6);

	    if (record1.getR1_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR1_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell7.setCellValue(record1.getR1_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell7.setCellStyle(numberStyle);
	    } else {
	        cell7.setCellValue("");
	        cell7.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(52) != null ? sheet.getRow(52) : sheet.createRow(52);
	     Cell cell9 = row.createCell(0);

	    if (record1.getR2_NAME_OF_BANK_VOSTRO1() != null && !record1.getR2_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell9.setCellValue(record1.getR2_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell9.setCellStyle(numberStyle);
	    } else {
	        cell9.setCellValue("");
	        cell9.setCellStyle(textStyle);
	    }

	     Cell cell10 = row.createCell(1);

	    if (record1.getR2_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR2_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell10.setCellValue(record1.getR2_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell10.setCellStyle(numberStyle);
	    } else {
	        cell10.setCellValue("");
	        cell10.setCellStyle(textStyle);
	    }

	     Cell cell11 = row.createCell(2);

	    if (record1.getR2_PURPOSE_VOSTRO1() != null && !record1.getR2_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell11.setCellValue(record1.getR2_PURPOSE_VOSTRO1().toString().trim() );
	        cell11.setCellStyle(numberStyle);
	    } else {
	        cell11.setCellValue("");
	        cell11.setCellStyle(textStyle);
	    }

	     Cell cell12 = row.createCell(3);

	    if (record1.getR2_CURRENCY_VOSTRO1() != null && !record1.getR2_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell12.setCellValue(record1.getR2_CURRENCY_VOSTRO1().toString().trim() );
	        cell12.setCellStyle(numberStyle);
	    } else {
	        cell12.setCellValue("");
	        cell12.setCellStyle(textStyle);
	    }

	     Cell cell15 = row.createCell(6);

	    if (record1.getR2_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR2_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell15.setCellValue(record1.getR2_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell15.setCellStyle(numberStyle);
	    } else {
	        cell15.setCellValue("");
	        cell15.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(53) != null ? sheet.getRow(53) : sheet.createRow(53);
	     Cell cell17 = row.createCell(0);

	    if (record1.getR3_NAME_OF_BANK_VOSTRO1() != null && !record1.getR3_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell17.setCellValue(record1.getR3_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell17.setCellStyle(numberStyle);
	    } else {
	        cell17.setCellValue("");
	        cell17.setCellStyle(textStyle);
	    }

	     Cell cell18 = row.createCell(1);

	    if (record1.getR3_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR3_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell18.setCellValue(record1.getR3_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell18.setCellStyle(numberStyle);
	    } else {
	        cell18.setCellValue("");
	        cell18.setCellStyle(textStyle);
	    }

	     Cell cell19 = row.createCell(2);

	    if (record1.getR3_PURPOSE_VOSTRO1() != null && !record1.getR3_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell19.setCellValue(record1.getR3_PURPOSE_VOSTRO1().toString().trim() );
	        cell19.setCellStyle(numberStyle);
	    } else {
	        cell19.setCellValue("");
	        cell19.setCellStyle(textStyle);
	    }

	     Cell cell20 = row.createCell(3);

	    if (record1.getR3_CURRENCY_VOSTRO1() != null && !record1.getR3_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell20.setCellValue(record1.getR3_CURRENCY_VOSTRO1().toString().trim() );
	        cell20.setCellStyle(numberStyle);
	    } else {
	        cell20.setCellValue("");
	        cell20.setCellStyle(textStyle);
	    }

	     Cell cell23 = row.createCell(6);

	    if (record1.getR3_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR3_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell23.setCellValue(record1.getR3_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell23.setCellStyle(numberStyle);
	    } else {
	        cell23.setCellValue("");
	        cell23.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(54) != null ? sheet.getRow(54) : sheet.createRow(54);
	     Cell cell25 = row.createCell(0);

	    if (record1.getR4_NAME_OF_BANK_VOSTRO1() != null && !record1.getR4_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell25.setCellValue(record1.getR4_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell25.setCellStyle(numberStyle);
	    } else {
	        cell25.setCellValue("");
	        cell25.setCellStyle(textStyle);
	    }

	     Cell cell26 = row.createCell(1);

	    if (record1.getR4_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR4_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell26.setCellValue(record1.getR4_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell26.setCellStyle(numberStyle);
	    } else {
	        cell26.setCellValue("");
	        cell26.setCellStyle(textStyle);
	    }

	     Cell cell27 = row.createCell(2);

	    if (record1.getR4_PURPOSE_VOSTRO1() != null && !record1.getR4_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell27.setCellValue(record1.getR4_PURPOSE_VOSTRO1().toString().trim() );
	        cell27.setCellStyle(numberStyle);
	    } else {
	        cell27.setCellValue("");
	        cell27.setCellStyle(textStyle);
	    }

	     Cell cell28 = row.createCell(3);

	    if (record1.getR4_CURRENCY_VOSTRO1() != null && !record1.getR4_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell28.setCellValue(record1.getR4_CURRENCY_VOSTRO1().toString().trim() );
	        cell28.setCellStyle(numberStyle);
	    } else {
	        cell28.setCellValue("");
	        cell28.setCellStyle(textStyle);
	    }

	     Cell cell31 = row.createCell(6);

	    if (record1.getR4_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR4_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell31.setCellValue(record1.getR4_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell31.setCellStyle(numberStyle);
	    } else {
	        cell31.setCellValue("");
	        cell31.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(55) != null ? sheet.getRow(55) : sheet.createRow(55);
	     Cell cell33 = row.createCell(0);

	    if (record1.getR5_NAME_OF_BANK_VOSTRO1() != null && !record1.getR5_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell33.setCellValue(record1.getR5_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell33.setCellStyle(numberStyle);
	    } else {
	        cell33.setCellValue("");
	        cell33.setCellStyle(textStyle);
	    }

	     Cell cell34 = row.createCell(1);

	    if (record1.getR5_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR5_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell34.setCellValue(record1.getR5_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell34.setCellStyle(numberStyle);
	    } else {
	        cell34.setCellValue("");
	        cell34.setCellStyle(textStyle);
	    }

	     Cell cell35 = row.createCell(2);

	    if (record1.getR5_PURPOSE_VOSTRO1() != null && !record1.getR5_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell35.setCellValue(record1.getR5_PURPOSE_VOSTRO1().toString().trim() );
	        cell35.setCellStyle(numberStyle);
	    } else {
	        cell35.setCellValue("");
	        cell35.setCellStyle(textStyle);
	    }

	     Cell cell36 = row.createCell(3);

	    if (record1.getR5_CURRENCY_VOSTRO1() != null && !record1.getR5_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell36.setCellValue(record1.getR5_CURRENCY_VOSTRO1().toString().trim() );
	        cell36.setCellStyle(numberStyle);
	    } else {
	        cell36.setCellValue("");
	        cell36.setCellStyle(textStyle);
	    }

	     Cell cell39 = row.createCell(6);

	    if (record1.getR5_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR5_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell39.setCellValue(record1.getR5_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell39.setCellStyle(numberStyle);
	    } else {
	        cell39.setCellValue("");
	        cell39.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(56) != null ? sheet.getRow(56) : sheet.createRow(56);
	     Cell cell41 = row.createCell(0);

	    if (record1.getR6_NAME_OF_BANK_VOSTRO1() != null && !record1.getR6_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell41.setCellValue(record1.getR6_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell41.setCellStyle(numberStyle);
	    } else {
	        cell41.setCellValue("");
	        cell41.setCellStyle(textStyle);
	    }

	     Cell cell42 = row.createCell(1);

	    if (record1.getR6_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR6_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell42.setCellValue(record1.getR6_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell42.setCellStyle(numberStyle);
	    } else {
	        cell42.setCellValue("");
	        cell42.setCellStyle(textStyle);
	    }

	     Cell cell43 = row.createCell(2);

	    if (record1.getR6_PURPOSE_VOSTRO1() != null && !record1.getR6_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell43.setCellValue(record1.getR6_PURPOSE_VOSTRO1().toString().trim() );
	        cell43.setCellStyle(numberStyle);
	    } else {
	        cell43.setCellValue("");
	        cell43.setCellStyle(textStyle);
	    }

	     Cell cell44 = row.createCell(3);

	    if (record1.getR6_CURRENCY_VOSTRO1() != null && !record1.getR6_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell44.setCellValue(record1.getR6_CURRENCY_VOSTRO1().toString().trim() );
	        cell44.setCellStyle(numberStyle);
	    } else {
	        cell44.setCellValue("");
	        cell44.setCellStyle(textStyle);
	    }

	     Cell cell47 = row.createCell(6);

	    if (record1.getR6_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR6_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell47.setCellValue(record1.getR6_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell47.setCellStyle(numberStyle);
	    } else {
	        cell47.setCellValue("");
	        cell47.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(57) != null ? sheet.getRow(57) : sheet.createRow(57);
	     Cell cell49 = row.createCell(0);

	    if (record1.getR7_NAME_OF_BANK_VOSTRO1() != null && !record1.getR7_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell49.setCellValue(record1.getR7_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell49.setCellStyle(numberStyle);
	    } else {
	        cell49.setCellValue("");
	        cell49.setCellStyle(textStyle);
	    }

	     Cell cell50 = row.createCell(1);

	    if (record1.getR7_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR7_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell50.setCellValue(record1.getR7_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell50.setCellStyle(numberStyle);
	    } else {
	        cell50.setCellValue("");
	        cell50.setCellStyle(textStyle);
	    }

	     Cell cell51 = row.createCell(2);

	    if (record1.getR7_PURPOSE_VOSTRO1() != null && !record1.getR7_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell51.setCellValue(record1.getR7_PURPOSE_VOSTRO1().toString().trim() );
	        cell51.setCellStyle(numberStyle);
	    } else {
	        cell51.setCellValue("");
	        cell51.setCellStyle(textStyle);
	    }

	     Cell cell52 = row.createCell(3);

	    if (record1.getR7_CURRENCY_VOSTRO1() != null && !record1.getR7_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell52.setCellValue(record1.getR7_CURRENCY_VOSTRO1().toString().trim() );
	        cell52.setCellStyle(numberStyle);
	    } else {
	        cell52.setCellValue("");
	        cell52.setCellStyle(textStyle);
	    }

	     Cell cell55 = row.createCell(6);

	    if (record1.getR7_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR7_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell55.setCellValue(record1.getR7_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell55.setCellStyle(numberStyle);
	    } else {
	        cell55.setCellValue("");
	        cell55.setCellStyle(textStyle);
	    }



		
	}
	
	private void writeEmailArchExcelRowData1(Sheet sheet, List<BrrsMNosvosP1Archival> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			
		System.out.println("came to write row data 1 method");
		
		BrrsMNosvosP1Archival record1 = dataList.get(0);
		
		 Row  row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);
		 Cell cell1 = row.createCell(0);

		 if (record1.getR1_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR1_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell1.setCellValue(record1.getR1_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell1.setCellStyle(numberStyle);
		 } else {
		     cell1.setCellValue("");
		     cell1.setCellStyle(textStyle);
		 }

		  Cell cell2 = row.createCell(1);

		 if (record1.getR1_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR1_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell2.setCellValue(record1.getR1_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell2.setCellStyle(numberStyle);
		 } else {
		     cell2.setCellValue("");
		     cell2.setCellStyle(textStyle);
		 }

		  Cell cell3 = row.createCell(2);

		 if (record1.getR1_PURPOSE_NOSTRO() != null && !record1.getR1_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell3.setCellValue(record1.getR1_PURPOSE_NOSTRO().toString().trim() );
		     cell3.setCellStyle(numberStyle);
		 } else {
		     cell3.setCellValue("");
		     cell3.setCellStyle(textStyle);
		 }

		  Cell cell4 = row.createCell(3);

		 if (record1.getR1_CURRENCY_NOSTRO() != null && !record1.getR1_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell4.setCellValue(record1.getR1_CURRENCY_NOSTRO().toString().trim() );
		     cell4.setCellStyle(numberStyle);
		 } else {
		     cell4.setCellValue("");
		     cell4.setCellStyle(textStyle);
		 }

		  Cell cell5 = row.createCell(4);

		 if (record1.getR1_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR1_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell5.setCellValue(record1.getR1_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell5.setCellStyle(numberStyle);
		 } else {
		     cell5.setCellValue("");
		     cell5.setCellStyle(textStyle);
		 }

		  Cell cell6 = row.createCell(5);

		 if (record1.getR1_RISK_WEIGHT_NOSTRO() != null && !record1.getR1_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell6.setCellValue(record1.getR1_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell6.setCellStyle(numberStyle);
		 } else {
		     cell6.setCellValue("");
		     cell6.setCellStyle(textStyle);
		 }

		  Cell cell7 = row.createCell(6);

		 if (record1.getR1_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR1_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell7.setCellValue(record1.getR1_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell7.setCellStyle(numberStyle);
		 } else {
		     cell7.setCellValue("");
		     cell7.setCellStyle(textStyle);
		 }

		  Cell cell8 = row.createCell(7);

		 if (record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell8.setCellValue(record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell8.setCellStyle(numberStyle);
		 } else {
		     cell8.setCellValue("");
		     cell8.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
		  Cell cell9 = row.createCell(0);

		 if (record1.getR2_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR2_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell9.setCellValue(record1.getR2_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell9.setCellStyle(numberStyle);
		 } else {
		     cell9.setCellValue("");
		     cell9.setCellStyle(textStyle);
		 }

		  Cell cell10 = row.createCell(1);

		 if (record1.getR2_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR2_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell10.setCellValue(record1.getR2_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell10.setCellStyle(numberStyle);
		 } else {
		     cell10.setCellValue("");
		     cell10.setCellStyle(textStyle);
		 }

		  Cell cell11 = row.createCell(2);

		 if (record1.getR2_PURPOSE_NOSTRO() != null && !record1.getR2_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell11.setCellValue(record1.getR2_PURPOSE_NOSTRO().toString().trim() );
		     cell11.setCellStyle(numberStyle);
		 } else {
		     cell11.setCellValue("");
		     cell11.setCellStyle(textStyle);
		 }

		  Cell cell12 = row.createCell(3);

		 if (record1.getR2_CURRENCY_NOSTRO() != null && !record1.getR2_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell12.setCellValue(record1.getR2_CURRENCY_NOSTRO().toString().trim() );
		     cell12.setCellStyle(numberStyle);
		 } else {
		     cell12.setCellValue("");
		     cell12.setCellStyle(textStyle);
		 }

		  Cell cell13 = row.createCell(4);

		 if (record1.getR2_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR2_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell13.setCellValue(record1.getR2_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell13.setCellStyle(numberStyle);
		 } else {
		     cell13.setCellValue("");
		     cell13.setCellStyle(textStyle);
		 }

		  Cell cell14 = row.createCell(5);

		 if (record1.getR2_RISK_WEIGHT_NOSTRO() != null && !record1.getR2_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell14.setCellValue(record1.getR2_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell14.setCellStyle(numberStyle);
		 } else {
		     cell14.setCellValue("");
		     cell14.setCellStyle(textStyle);
		 }

		  Cell cell15 = row.createCell(6);

		 if (record1.getR2_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR2_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell15.setCellValue(record1.getR2_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell15.setCellStyle(numberStyle);
		 } else {
		     cell15.setCellValue("");
		     cell15.setCellStyle(textStyle);
		 }

		  Cell cell16 = row.createCell(7);

		 if (record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell16.setCellValue(record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell16.setCellStyle(numberStyle);
		 } else {
		     cell16.setCellValue("");
		     cell16.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(12) != null ? sheet.getRow(12) : sheet.createRow(12);
		  Cell cell17 = row.createCell(0);

		 if (record1.getR3_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR3_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell17.setCellValue(record1.getR3_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell17.setCellStyle(numberStyle);
		 } else {
		     cell17.setCellValue("");
		     cell17.setCellStyle(textStyle);
		 }

		  Cell cell18 = row.createCell(1);

		 if (record1.getR3_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR3_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell18.setCellValue(record1.getR3_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell18.setCellStyle(numberStyle);
		 } else {
		     cell18.setCellValue("");
		     cell18.setCellStyle(textStyle);
		 }

		  Cell cell19 = row.createCell(2);

		 if (record1.getR3_PURPOSE_NOSTRO() != null && !record1.getR3_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell19.setCellValue(record1.getR3_PURPOSE_NOSTRO().toString().trim() );
		     cell19.setCellStyle(numberStyle);
		 } else {
		     cell19.setCellValue("");
		     cell19.setCellStyle(textStyle);
		 }

		  Cell cell20 = row.createCell(3);

		 if (record1.getR3_CURRENCY_NOSTRO() != null && !record1.getR3_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell20.setCellValue(record1.getR3_CURRENCY_NOSTRO().toString().trim() );
		     cell20.setCellStyle(numberStyle);
		 } else {
		     cell20.setCellValue("");
		     cell20.setCellStyle(textStyle);
		 }

		  Cell cell21 = row.createCell(4);

		 if (record1.getR3_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR3_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell21.setCellValue(record1.getR3_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell21.setCellStyle(numberStyle);
		 } else {
		     cell21.setCellValue("");
		     cell21.setCellStyle(textStyle);
		 }

		  Cell cell22 = row.createCell(5);

		 if (record1.getR3_RISK_WEIGHT_NOSTRO() != null && !record1.getR3_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell22.setCellValue(record1.getR3_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell22.setCellStyle(numberStyle);
		 } else {
		     cell22.setCellValue("");
		     cell22.setCellStyle(textStyle);
		 }

		  Cell cell23 = row.createCell(6);

		 if (record1.getR3_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR3_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell23.setCellValue(record1.getR3_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell23.setCellStyle(numberStyle);
		 } else {
		     cell23.setCellValue("");
		     cell23.setCellStyle(textStyle);
		 }

		  Cell cell24 = row.createCell(7);

		 if (record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell24.setCellValue(record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell24.setCellStyle(numberStyle);
		 } else {
		     cell24.setCellValue("");
		     cell24.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(13) != null ? sheet.getRow(13) : sheet.createRow(13);
		  Cell cell25 = row.createCell(0);

		 if (record1.getR4_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR4_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell25.setCellValue(record1.getR4_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell25.setCellStyle(numberStyle);
		 } else {
		     cell25.setCellValue("");
		     cell25.setCellStyle(textStyle);
		 }

		  Cell cell26 = row.createCell(1);

		 if (record1.getR4_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR4_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell26.setCellValue(record1.getR4_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell26.setCellStyle(numberStyle);
		 } else {
		     cell26.setCellValue("");
		     cell26.setCellStyle(textStyle);
		 }

		  Cell cell27 = row.createCell(2);

		 if (record1.getR4_PURPOSE_NOSTRO() != null && !record1.getR4_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell27.setCellValue(record1.getR4_PURPOSE_NOSTRO().toString().trim() );
		     cell27.setCellStyle(numberStyle);
		 } else {
		     cell27.setCellValue("");
		     cell27.setCellStyle(textStyle);
		 }

		  Cell cell28 = row.createCell(3);

		 if (record1.getR4_CURRENCY_NOSTRO() != null && !record1.getR4_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell28.setCellValue(record1.getR4_CURRENCY_NOSTRO().toString().trim() );
		     cell28.setCellStyle(numberStyle);
		 } else {
		     cell28.setCellValue("");
		     cell28.setCellStyle(textStyle);
		 }

		  Cell cell29 = row.createCell(4);

		 if (record1.getR4_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR4_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell29.setCellValue(record1.getR4_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell29.setCellStyle(numberStyle);
		 } else {
		     cell29.setCellValue("");
		     cell29.setCellStyle(textStyle);
		 }

		  Cell cell30 = row.createCell(5);

		 if (record1.getR4_RISK_WEIGHT_NOSTRO() != null && !record1.getR4_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell30.setCellValue(record1.getR4_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell30.setCellStyle(numberStyle);
		 } else {
		     cell30.setCellValue("");
		     cell30.setCellStyle(textStyle);
		 }

		  Cell cell31 = row.createCell(6);

		 if (record1.getR4_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR4_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell31.setCellValue(record1.getR4_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell31.setCellStyle(numberStyle);
		 } else {
		     cell31.setCellValue("");
		     cell31.setCellStyle(textStyle);
		 }

		  Cell cell32 = row.createCell(7);

		 if (record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell32.setCellValue(record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell32.setCellStyle(numberStyle);
		 } else {
		     cell32.setCellValue("");
		     cell32.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(14) != null ? sheet.getRow(14) : sheet.createRow(14);
		  Cell cell33 = row.createCell(0);

		 if (record1.getR5_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR5_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell33.setCellValue(record1.getR5_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell33.setCellStyle(numberStyle);
		 } else {
		     cell33.setCellValue("");
		     cell33.setCellStyle(textStyle);
		 }

		  Cell cell34 = row.createCell(1);

		 if (record1.getR5_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR5_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell34.setCellValue(record1.getR5_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell34.setCellStyle(numberStyle);
		 } else {
		     cell34.setCellValue("");
		     cell34.setCellStyle(textStyle);
		 }

		  Cell cell35 = row.createCell(2);

		 if (record1.getR5_PURPOSE_NOSTRO() != null && !record1.getR5_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell35.setCellValue(record1.getR5_PURPOSE_NOSTRO().toString().trim() );
		     cell35.setCellStyle(numberStyle);
		 } else {
		     cell35.setCellValue("");
		     cell35.setCellStyle(textStyle);
		 }

		  Cell cell36 = row.createCell(3);

		 if (record1.getR5_CURRENCY_NOSTRO() != null && !record1.getR5_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell36.setCellValue(record1.getR5_CURRENCY_NOSTRO().toString().trim() );
		     cell36.setCellStyle(numberStyle);
		 } else {
		     cell36.setCellValue("");
		     cell36.setCellStyle(textStyle);
		 }

		  Cell cell37 = row.createCell(4);

		 if (record1.getR5_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR5_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell37.setCellValue(record1.getR5_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell37.setCellStyle(numberStyle);
		 } else {
		     cell37.setCellValue("");
		     cell37.setCellStyle(textStyle);
		 }

		  Cell cell38 = row.createCell(5);

		 if (record1.getR5_RISK_WEIGHT_NOSTRO() != null && !record1.getR5_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell38.setCellValue(record1.getR5_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell38.setCellStyle(numberStyle);
		 } else {
		     cell38.setCellValue("");
		     cell38.setCellStyle(textStyle);
		 }

		  Cell cell39 = row.createCell(6);

		 if (record1.getR5_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR5_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell39.setCellValue(record1.getR5_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell39.setCellStyle(numberStyle);
		 } else {
		     cell39.setCellValue("");
		     cell39.setCellStyle(textStyle);
		 }

		  Cell cell40 = row.createCell(7);

		 if (record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell40.setCellValue(record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell40.setCellStyle(numberStyle);
		 } else {
		     cell40.setCellValue("");
		     cell40.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(15) != null ? sheet.getRow(15) : sheet.createRow(15);
		  Cell cell41 = row.createCell(0);

		 if (record1.getR6_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR6_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell41.setCellValue(record1.getR6_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell41.setCellStyle(numberStyle);
		 } else {
		     cell41.setCellValue("");
		     cell41.setCellStyle(textStyle);
		 }

		  Cell cell42 = row.createCell(1);

		 if (record1.getR6_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR6_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell42.setCellValue(record1.getR6_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell42.setCellStyle(numberStyle);
		 } else {
		     cell42.setCellValue("");
		     cell42.setCellStyle(textStyle);
		 }

		  Cell cell43 = row.createCell(2);

		 if (record1.getR6_PURPOSE_NOSTRO() != null && !record1.getR6_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell43.setCellValue(record1.getR6_PURPOSE_NOSTRO().toString().trim() );
		     cell43.setCellStyle(numberStyle);
		 } else {
		     cell43.setCellValue("");
		     cell43.setCellStyle(textStyle);
		 }

		  Cell cell44 = row.createCell(3);

		 if (record1.getR6_CURRENCY_NOSTRO() != null && !record1.getR6_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell44.setCellValue(record1.getR6_CURRENCY_NOSTRO().toString().trim() );
		     cell44.setCellStyle(numberStyle);
		 } else {
		     cell44.setCellValue("");
		     cell44.setCellStyle(textStyle);
		 }

		  Cell cell45 = row.createCell(4);

		 if (record1.getR6_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR6_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell45.setCellValue(record1.getR6_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell45.setCellStyle(numberStyle);
		 } else {
		     cell45.setCellValue("");
		     cell45.setCellStyle(textStyle);
		 }

		  Cell cell46 = row.createCell(5);

		 if (record1.getR6_RISK_WEIGHT_NOSTRO() != null && !record1.getR6_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell46.setCellValue(record1.getR6_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell46.setCellStyle(numberStyle);
		 } else {
		     cell46.setCellValue("");
		     cell46.setCellStyle(textStyle);
		 }

		  Cell cell47 = row.createCell(6);

		 if (record1.getR6_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR6_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell47.setCellValue(record1.getR6_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell47.setCellStyle(numberStyle);
		 } else {
		     cell47.setCellValue("");
		     cell47.setCellStyle(textStyle);
		 }

		  Cell cell48 = row.createCell(7);

		 if (record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell48.setCellValue(record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell48.setCellStyle(numberStyle);
		 } else {
		     cell48.setCellValue("");
		     cell48.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(16) != null ? sheet.getRow(16) : sheet.createRow(16);
		  Cell cell49 = row.createCell(0);

		 if (record1.getR7_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR7_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell49.setCellValue(record1.getR7_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell49.setCellStyle(numberStyle);
		 } else {
		     cell49.setCellValue("");
		     cell49.setCellStyle(textStyle);
		 }

		  Cell cell50 = row.createCell(1);

		 if (record1.getR7_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR7_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell50.setCellValue(record1.getR7_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell50.setCellStyle(numberStyle);
		 } else {
		     cell50.setCellValue("");
		     cell50.setCellStyle(textStyle);
		 }

		  Cell cell51 = row.createCell(2);

		 if (record1.getR7_PURPOSE_NOSTRO() != null && !record1.getR7_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell51.setCellValue(record1.getR7_PURPOSE_NOSTRO().toString().trim() );
		     cell51.setCellStyle(numberStyle);
		 } else {
		     cell51.setCellValue("");
		     cell51.setCellStyle(textStyle);
		 }

		  Cell cell52 = row.createCell(3);

		 if (record1.getR7_CURRENCY_NOSTRO() != null && !record1.getR7_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell52.setCellValue(record1.getR7_CURRENCY_NOSTRO().toString().trim() );
		     cell52.setCellStyle(numberStyle);
		 } else {
		     cell52.setCellValue("");
		     cell52.setCellStyle(textStyle);
		 }

		  Cell cell53 = row.createCell(4);

		 if (record1.getR7_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR7_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell53.setCellValue(record1.getR7_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell53.setCellStyle(numberStyle);
		 } else {
		     cell53.setCellValue("");
		     cell53.setCellStyle(textStyle);
		 }

		  Cell cell54 = row.createCell(5);

		 if (record1.getR7_RISK_WEIGHT_NOSTRO() != null && !record1.getR7_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell54.setCellValue(record1.getR7_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell54.setCellStyle(numberStyle);
		 } else {
		     cell54.setCellValue("");
		     cell54.setCellStyle(textStyle);
		 }

		  Cell cell55 = row.createCell(6);

		 if (record1.getR7_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR7_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell55.setCellValue(record1.getR7_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell55.setCellStyle(numberStyle);
		 } else {
		     cell55.setCellValue("");
		     cell55.setCellStyle(textStyle);
		 }

		  Cell cell56 = row.createCell(7);

		 if (record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell56.setCellValue(record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell56.setCellStyle(numberStyle);
		 } else {
		     cell56.setCellValue("");
		     cell56.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(17) != null ? sheet.getRow(17) : sheet.createRow(17);
		  Cell cell57 = row.createCell(0);

		 if (record1.getR8_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR8_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell57.setCellValue(record1.getR8_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell57.setCellStyle(numberStyle);
		 } else {
		     cell57.setCellValue("");
		     cell57.setCellStyle(textStyle);
		 }

		  Cell cell58 = row.createCell(1);

		 if (record1.getR8_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR8_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell58.setCellValue(record1.getR8_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell58.setCellStyle(numberStyle);
		 } else {
		     cell58.setCellValue("");
		     cell58.setCellStyle(textStyle);
		 }

		  Cell cell59 = row.createCell(2);

		 if (record1.getR8_PURPOSE_NOSTRO() != null && !record1.getR8_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell59.setCellValue(record1.getR8_PURPOSE_NOSTRO().toString().trim() );
		     cell59.setCellStyle(numberStyle);
		 } else {
		     cell59.setCellValue("");
		     cell59.setCellStyle(textStyle);
		 }

		  Cell cell60 = row.createCell(3);

		 if (record1.getR8_CURRENCY_NOSTRO() != null && !record1.getR8_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell60.setCellValue(record1.getR8_CURRENCY_NOSTRO().toString().trim() );
		     cell60.setCellStyle(numberStyle);
		 } else {
		     cell60.setCellValue("");
		     cell60.setCellStyle(textStyle);
		 }

		  Cell cell61 = row.createCell(4);

		 if (record1.getR8_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR8_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell61.setCellValue(record1.getR8_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell61.setCellStyle(numberStyle);
		 } else {
		     cell61.setCellValue("");
		     cell61.setCellStyle(textStyle);
		 }

		  Cell cell62 = row.createCell(5);

		 if (record1.getR8_RISK_WEIGHT_NOSTRO() != null && !record1.getR8_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell62.setCellValue(record1.getR8_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell62.setCellStyle(numberStyle);
		 } else {
		     cell62.setCellValue("");
		     cell62.setCellStyle(textStyle);
		 }

		  Cell cell63 = row.createCell(6);

		 if (record1.getR8_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR8_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell63.setCellValue(record1.getR8_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell63.setCellStyle(numberStyle);
		 } else {
		     cell63.setCellValue("");
		     cell63.setCellStyle(textStyle);
		 }

		  Cell cell64 = row.createCell(7);

		 if (record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell64.setCellValue(record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell64.setCellStyle(numberStyle);
		 } else {
		     cell64.setCellValue("");
		     cell64.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(18) != null ? sheet.getRow(18) : sheet.createRow(18);
		  Cell cell65 = row.createCell(0);

		 if (record1.getR9_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR9_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell65.setCellValue(record1.getR9_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell65.setCellStyle(numberStyle);
		 } else {
		     cell65.setCellValue("");
		     cell65.setCellStyle(textStyle);
		 }

		  Cell cell66 = row.createCell(1);

		 if (record1.getR9_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR9_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell66.setCellValue(record1.getR9_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell66.setCellStyle(numberStyle);
		 } else {
		     cell66.setCellValue("");
		     cell66.setCellStyle(textStyle);
		 }

		  Cell cell67 = row.createCell(2);

		 if (record1.getR9_PURPOSE_NOSTRO() != null && !record1.getR9_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell67.setCellValue(record1.getR9_PURPOSE_NOSTRO().toString().trim() );
		     cell67.setCellStyle(numberStyle);
		 } else {
		     cell67.setCellValue("");
		     cell67.setCellStyle(textStyle);
		 }

		  Cell cell68 = row.createCell(3);

		 if (record1.getR9_CURRENCY_NOSTRO() != null && !record1.getR9_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell68.setCellValue(record1.getR9_CURRENCY_NOSTRO().toString().trim() );
		     cell68.setCellStyle(numberStyle);
		 } else {
		     cell68.setCellValue("");
		     cell68.setCellStyle(textStyle);
		 }

		  Cell cell69 = row.createCell(4);

		 if (record1.getR9_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR9_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell69.setCellValue(record1.getR9_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell69.setCellStyle(numberStyle);
		 } else {
		     cell69.setCellValue("");
		     cell69.setCellStyle(textStyle);
		 }

		  Cell cell70 = row.createCell(5);

		 if (record1.getR9_RISK_WEIGHT_NOSTRO() != null && !record1.getR9_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell70.setCellValue(record1.getR9_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell70.setCellStyle(numberStyle);
		 } else {
		     cell70.setCellValue("");
		     cell70.setCellStyle(textStyle);
		 }

		  Cell cell71 = row.createCell(6);

		 if (record1.getR9_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR9_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell71.setCellValue(record1.getR9_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell71.setCellStyle(numberStyle);
		 } else {
		     cell71.setCellValue("");
		     cell71.setCellStyle(textStyle);
		 }

		  Cell cell72 = row.createCell(7);

		 if (record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell72.setCellValue(record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell72.setCellStyle(numberStyle);
		 } else {
		     cell72.setCellValue("");
		     cell72.setCellStyle(textStyle);
		 }




		}
	
	private void writeEmailArchExcelRowData2(Sheet sheet, List<BrrsMNosvosP2Archival> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
		
			System.out.println("came to write row data 1 method");
		
			BrrsMNosvosP2Archival record1 = dataList.get(0);
			
			Row  row = sheet.getRow(23) != null ? sheet.getRow(23) : sheet.createRow(23);
			 Cell cell1 = row.createCell(0);

			 if (record1.getR1_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR1_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell1.setCellValue(record1.getR1_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell1.setCellStyle(numberStyle);
			 } else {
			     cell1.setCellValue("");
			     cell1.setCellStyle(textStyle);
			 }

			  Cell cell2 = row.createCell(1);

			 if (record1.getR1_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR1_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell2.setCellValue(record1.getR1_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell2.setCellStyle(numberStyle);
			 } else {
			     cell2.setCellValue("");
			     cell2.setCellStyle(textStyle);
			 }

			  Cell cell3 = row.createCell(2);

			 if (record1.getR1_PURPOSE_VOSTRO() != null && !record1.getR1_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell3.setCellValue(record1.getR1_PURPOSE_VOSTRO().toString().trim() );
			     cell3.setCellStyle(numberStyle);
			 } else {
			     cell3.setCellValue("");
			     cell3.setCellStyle(textStyle);
			 }

			  Cell cell4 = row.createCell(3);

			 if (record1.getR1_CURRENCY_VOSTRO() != null && !record1.getR1_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell4.setCellValue(record1.getR1_CURRENCY_VOSTRO().toString().trim() );
			     cell4.setCellStyle(numberStyle);
			 } else {
			     cell4.setCellValue("");
			     cell4.setCellStyle(textStyle);
			 }

			  Cell cell7 = row.createCell(6);

			 if (record1.getR1_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR1_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell7.setCellValue(record1.getR1_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell7.setCellStyle(numberStyle);
			 } else {
			     cell7.setCellValue("");
			     cell7.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(24) != null ? sheet.getRow(24) : sheet.createRow(24);
			  Cell cell9 = row.createCell(0);

			 if (record1.getR2_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR2_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell9.setCellValue(record1.getR2_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell9.setCellStyle(numberStyle);
			 } else {
			     cell9.setCellValue("");
			     cell9.setCellStyle(textStyle);
			 }

			  Cell cell10 = row.createCell(1);

			 if (record1.getR2_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR2_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell10.setCellValue(record1.getR2_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell10.setCellStyle(numberStyle);
			 } else {
			     cell10.setCellValue("");
			     cell10.setCellStyle(textStyle);
			 }

			  Cell cell11 = row.createCell(2);

			 if (record1.getR2_PURPOSE_VOSTRO() != null && !record1.getR2_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell11.setCellValue(record1.getR2_PURPOSE_VOSTRO().toString().trim() );
			     cell11.setCellStyle(numberStyle);
			 } else {
			     cell11.setCellValue("");
			     cell11.setCellStyle(textStyle);
			 }

			  Cell cell12 = row.createCell(3);

			 if (record1.getR2_CURRENCY_VOSTRO() != null && !record1.getR2_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell12.setCellValue(record1.getR2_CURRENCY_VOSTRO().toString().trim() );
			     cell12.setCellStyle(numberStyle);
			 } else {
			     cell12.setCellValue("");
			     cell12.setCellStyle(textStyle);
			 }

			  Cell cell15 = row.createCell(6);

			 if (record1.getR2_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR2_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell15.setCellValue(record1.getR2_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell15.setCellStyle(numberStyle);
			 } else {
			     cell15.setCellValue("");
			     cell15.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(25) != null ? sheet.getRow(25) : sheet.createRow(25);
			  Cell cell17 = row.createCell(0);

			 if (record1.getR3_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR3_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell17.setCellValue(record1.getR3_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell17.setCellStyle(numberStyle);
			 } else {
			     cell17.setCellValue("");
			     cell17.setCellStyle(textStyle);
			 }

			  Cell cell18 = row.createCell(1);

			 if (record1.getR3_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR3_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell18.setCellValue(record1.getR3_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell18.setCellStyle(numberStyle);
			 } else {
			     cell18.setCellValue("");
			     cell18.setCellStyle(textStyle);
			 }

			  Cell cell19 = row.createCell(2);

			 if (record1.getR3_PURPOSE_VOSTRO() != null && !record1.getR3_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell19.setCellValue(record1.getR3_PURPOSE_VOSTRO().toString().trim() );
			     cell19.setCellStyle(numberStyle);
			 } else {
			     cell19.setCellValue("");
			     cell19.setCellStyle(textStyle);
			 }

			  Cell cell20 = row.createCell(3);

			 if (record1.getR3_CURRENCY_VOSTRO() != null && !record1.getR3_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell20.setCellValue(record1.getR3_CURRENCY_VOSTRO().toString().trim() );
			     cell20.setCellStyle(numberStyle);
			 } else {
			     cell20.setCellValue("");
			     cell20.setCellStyle(textStyle);
			 }

			  Cell cell23 = row.createCell(6);

			 if (record1.getR3_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR3_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell23.setCellValue(record1.getR3_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell23.setCellStyle(numberStyle);
			 } else {
			     cell23.setCellValue("");
			     cell23.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(26) != null ? sheet.getRow(26) : sheet.createRow(26);
			  Cell cell25 = row.createCell(0);

			 if (record1.getR4_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR4_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell25.setCellValue(record1.getR4_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell25.setCellStyle(numberStyle);
			 } else {
			     cell25.setCellValue("");
			     cell25.setCellStyle(textStyle);
			 }

			  Cell cell26 = row.createCell(1);

			 if (record1.getR4_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR4_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell26.setCellValue(record1.getR4_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell26.setCellStyle(numberStyle);
			 } else {
			     cell26.setCellValue("");
			     cell26.setCellStyle(textStyle);
			 }

			  Cell cell27 = row.createCell(2);

			 if (record1.getR4_PURPOSE_VOSTRO() != null && !record1.getR4_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell27.setCellValue(record1.getR4_PURPOSE_VOSTRO().toString().trim() );
			     cell27.setCellStyle(numberStyle);
			 } else {
			     cell27.setCellValue("");
			     cell27.setCellStyle(textStyle);
			 }

			  Cell cell28 = row.createCell(3);

			 if (record1.getR4_CURRENCY_VOSTRO() != null && !record1.getR4_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell28.setCellValue(record1.getR4_CURRENCY_VOSTRO().toString().trim() );
			     cell28.setCellStyle(numberStyle);
			 } else {
			     cell28.setCellValue("");
			     cell28.setCellStyle(textStyle);
			 }

			  Cell cell31 = row.createCell(6);

			 if (record1.getR4_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR4_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell31.setCellValue(record1.getR4_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell31.setCellStyle(numberStyle);
			 } else {
			     cell31.setCellValue("");
			     cell31.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(27) != null ? sheet.getRow(27) : sheet.createRow(27);
			  Cell cell33 = row.createCell(0);

			 if (record1.getR5_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR5_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell33.setCellValue(record1.getR5_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell33.setCellStyle(numberStyle);
			 } else {
			     cell33.setCellValue("");
			     cell33.setCellStyle(textStyle);
			 }

			  Cell cell34 = row.createCell(1);

			 if (record1.getR5_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR5_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell34.setCellValue(record1.getR5_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell34.setCellStyle(numberStyle);
			 } else {
			     cell34.setCellValue("");
			     cell34.setCellStyle(textStyle);
			 }

			  Cell cell35 = row.createCell(2);

			 if (record1.getR5_PURPOSE_VOSTRO() != null && !record1.getR5_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell35.setCellValue(record1.getR5_PURPOSE_VOSTRO().toString().trim() );
			     cell35.setCellStyle(numberStyle);
			 } else {
			     cell35.setCellValue("");
			     cell35.setCellStyle(textStyle);
			 }

			  Cell cell36 = row.createCell(3);

			 if (record1.getR5_CURRENCY_VOSTRO() != null && !record1.getR5_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell36.setCellValue(record1.getR5_CURRENCY_VOSTRO().toString().trim() );
			     cell36.setCellStyle(numberStyle);
			 } else {
			     cell36.setCellValue("");
			     cell36.setCellStyle(textStyle);
			 }

			  Cell cell39 = row.createCell(6);

			 if (record1.getR5_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR5_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell39.setCellValue(record1.getR5_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell39.setCellStyle(numberStyle);
			 } else {
			     cell39.setCellValue("");
			     cell39.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(28) != null ? sheet.getRow(28) : sheet.createRow(28);
			  Cell cell41 = row.createCell(0);

			 if (record1.getR6_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR6_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell41.setCellValue(record1.getR6_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell41.setCellStyle(numberStyle);
			 } else {
			     cell41.setCellValue("");
			     cell41.setCellStyle(textStyle);
			 }

			  Cell cell42 = row.createCell(1);

			 if (record1.getR6_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR6_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell42.setCellValue(record1.getR6_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell42.setCellStyle(numberStyle);
			 } else {
			     cell42.setCellValue("");
			     cell42.setCellStyle(textStyle);
			 }

			  Cell cell43 = row.createCell(2);

			 if (record1.getR6_PURPOSE_VOSTRO() != null && !record1.getR6_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell43.setCellValue(record1.getR6_PURPOSE_VOSTRO().toString().trim() );
			     cell43.setCellStyle(numberStyle);
			 } else {
			     cell43.setCellValue("");
			     cell43.setCellStyle(textStyle);
			 }

			  Cell cell44 = row.createCell(3);

			 if (record1.getR6_CURRENCY_VOSTRO() != null && !record1.getR6_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell44.setCellValue(record1.getR6_CURRENCY_VOSTRO().toString().trim() );
			     cell44.setCellStyle(numberStyle);
			 } else {
			     cell44.setCellValue("");
			     cell44.setCellStyle(textStyle);
			 }

			  Cell cell47 = row.createCell(6);

			 if (record1.getR6_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR6_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell47.setCellValue(record1.getR6_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell47.setCellStyle(numberStyle);
			 } else {
			     cell47.setCellValue("");
			     cell47.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(29) != null ? sheet.getRow(29) : sheet.createRow(29);
			  Cell cell49 = row.createCell(0);

			 if (record1.getR7_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR7_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell49.setCellValue(record1.getR7_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell49.setCellStyle(numberStyle);
			 } else {
			     cell49.setCellValue("");
			     cell49.setCellStyle(textStyle);
			 }

			  Cell cell50 = row.createCell(1);

			 if (record1.getR7_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR7_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell50.setCellValue(record1.getR7_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell50.setCellStyle(numberStyle);
			 } else {
			     cell50.setCellValue("");
			     cell50.setCellStyle(textStyle);
			 }

			  Cell cell51 = row.createCell(2);

			 if (record1.getR7_PURPOSE_VOSTRO() != null && !record1.getR7_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell51.setCellValue(record1.getR7_PURPOSE_VOSTRO().toString().trim() );
			     cell51.setCellStyle(numberStyle);
			 } else {
			     cell51.setCellValue("");
			     cell51.setCellStyle(textStyle);
			 }

			  Cell cell52 = row.createCell(3);

			 if (record1.getR7_CURRENCY_VOSTRO() != null && !record1.getR7_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell52.setCellValue(record1.getR7_CURRENCY_VOSTRO().toString().trim() );
			     cell52.setCellStyle(numberStyle);
			 } else {
			     cell52.setCellValue("");
			     cell52.setCellStyle(textStyle);
			 }

			  Cell cell55 = row.createCell(6);

			 if (record1.getR7_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR7_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell55.setCellValue(record1.getR7_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell55.setCellStyle(numberStyle);
			 } else {
			     cell55.setCellValue("");
			     cell55.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(30) != null ? sheet.getRow(30) : sheet.createRow(30);
			  Cell cell57 = row.createCell(0);

			 if (record1.getR8_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR8_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell57.setCellValue(record1.getR8_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell57.setCellStyle(numberStyle);
			 } else {
			     cell57.setCellValue("");
			     cell57.setCellStyle(textStyle);
			 }

			  Cell cell58 = row.createCell(1);

			 if (record1.getR8_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR8_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell58.setCellValue(record1.getR8_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell58.setCellStyle(numberStyle);
			 } else {
			     cell58.setCellValue("");
			     cell58.setCellStyle(textStyle);
			 }

			  Cell cell59 = row.createCell(2);

			 if (record1.getR8_PURPOSE_VOSTRO() != null && !record1.getR8_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell59.setCellValue(record1.getR8_PURPOSE_VOSTRO().toString().trim() );
			     cell59.setCellStyle(numberStyle);
			 } else {
			     cell59.setCellValue("");
			     cell59.setCellStyle(textStyle);
			 }

			  Cell cell60 = row.createCell(3);

			 if (record1.getR8_CURRENCY_VOSTRO() != null && !record1.getR8_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell60.setCellValue(record1.getR8_CURRENCY_VOSTRO().toString().trim() );
			     cell60.setCellStyle(numberStyle);
			 } else {
			     cell60.setCellValue("");
			     cell60.setCellStyle(textStyle);
			 }

			  Cell cell63 = row.createCell(6);

			 if (record1.getR8_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR8_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell63.setCellValue(record1.getR8_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell63.setCellStyle(numberStyle);
			 } else {
			     cell63.setCellValue("");
			     cell63.setCellStyle(textStyle);
			 }



			
		}
	
	private void writeEmailArchExcelRowData3(Sheet sheet, List<BrrsMNosvosP3Archival> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			
		System.out.println("came to write row data 1 method");
		
		BrrsMNosvosP3Archival record1 = dataList.get(0);
		
		Row  row = sheet.getRow(36) != null ? sheet.getRow(36) : sheet.createRow(36);
		 Cell cell1 = row.createCell(0);

		 if (record1.getR1_NAME_OF_BANK_NOSTRO1() != null && !record1.getR1_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell1.setCellValue(record1.getR1_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell1.setCellStyle(numberStyle);
		 } else {
		     cell1.setCellValue("");
		     cell1.setCellStyle(textStyle);
		 }

		  Cell cell2 = row.createCell(1);

		 if (record1.getR1_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR1_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell2.setCellValue(record1.getR1_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell2.setCellStyle(numberStyle);
		 } else {
		     cell2.setCellValue("");
		     cell2.setCellStyle(textStyle);
		 }

		  Cell cell3 = row.createCell(2);

		 if (record1.getR1_PURPOSE_NOSTRO1() != null && !record1.getR1_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell3.setCellValue(record1.getR1_PURPOSE_NOSTRO1().toString().trim() );
		     cell3.setCellStyle(numberStyle);
		 } else {
		     cell3.setCellValue("");
		     cell3.setCellStyle(textStyle);
		 }

		  Cell cell4 = row.createCell(3);

		 if (record1.getR1_CURRENCY_NOSTRO1() != null && !record1.getR1_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell4.setCellValue(record1.getR1_CURRENCY_NOSTRO1().toString().trim() );
		     cell4.setCellStyle(numberStyle);
		 } else {
		     cell4.setCellValue("");
		     cell4.setCellStyle(textStyle);
		 }

		  Cell cell5 = row.createCell(4);

		 if (record1.getR1_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR1_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell5.setCellValue(record1.getR1_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell5.setCellStyle(numberStyle);
		 } else {
		     cell5.setCellValue("");
		     cell5.setCellStyle(textStyle);
		 }

		  Cell cell6 = row.createCell(5);

		 if (record1.getR1_RISK_WEIGHT_NOSTRO1() != null && !record1.getR1_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell6.setCellValue(record1.getR1_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell6.setCellStyle(numberStyle);
		 } else {
		     cell6.setCellValue("");
		     cell6.setCellStyle(textStyle);
		 }

		  Cell cell7 = row.createCell(6);

		 if (record1.getR1_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR1_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell7.setCellValue(record1.getR1_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell7.setCellStyle(numberStyle);
		 } else {
		     cell7.setCellValue("");
		     cell7.setCellStyle(textStyle);
		 }

		  Cell cell8 = row.createCell(7);

		 if (record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell8.setCellValue(record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell8.setCellStyle(numberStyle);
		 } else {
		     cell8.setCellValue("");
		     cell8.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(37) != null ? sheet.getRow(37) : sheet.createRow(37);
		  Cell cell9 = row.createCell(0);

		 if (record1.getR2_NAME_OF_BANK_NOSTRO1() != null && !record1.getR2_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell9.setCellValue(record1.getR2_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell9.setCellStyle(numberStyle);
		 } else {
		     cell9.setCellValue("");
		     cell9.setCellStyle(textStyle);
		 }

		  Cell cell10 = row.createCell(1);

		 if (record1.getR2_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR2_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell10.setCellValue(record1.getR2_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell10.setCellStyle(numberStyle);
		 } else {
		     cell10.setCellValue("");
		     cell10.setCellStyle(textStyle);
		 }

		  Cell cell11 = row.createCell(2);

		 if (record1.getR2_PURPOSE_NOSTRO1() != null && !record1.getR2_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell11.setCellValue(record1.getR2_PURPOSE_NOSTRO1().toString().trim() );
		     cell11.setCellStyle(numberStyle);
		 } else {
		     cell11.setCellValue("");
		     cell11.setCellStyle(textStyle);
		 }

		  Cell cell12 = row.createCell(3);

		 if (record1.getR2_CURRENCY_NOSTRO1() != null && !record1.getR2_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell12.setCellValue(record1.getR2_CURRENCY_NOSTRO1().toString().trim() );
		     cell12.setCellStyle(numberStyle);
		 } else {
		     cell12.setCellValue("");
		     cell12.setCellStyle(textStyle);
		 }

		  Cell cell13 = row.createCell(4);

		 if (record1.getR2_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR2_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell13.setCellValue(record1.getR2_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell13.setCellStyle(numberStyle);
		 } else {
		     cell13.setCellValue("");
		     cell13.setCellStyle(textStyle);
		 }

		  Cell cell14 = row.createCell(5);

		 if (record1.getR2_RISK_WEIGHT_NOSTRO1() != null && !record1.getR2_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell14.setCellValue(record1.getR2_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell14.setCellStyle(numberStyle);
		 } else {
		     cell14.setCellValue("");
		     cell14.setCellStyle(textStyle);
		 }

		  Cell cell15 = row.createCell(6);

		 if (record1.getR2_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR2_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell15.setCellValue(record1.getR2_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell15.setCellStyle(numberStyle);
		 } else {
		     cell15.setCellValue("");
		     cell15.setCellStyle(textStyle);
		 }

		  Cell cell16 = row.createCell(7);

		 if (record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell16.setCellValue(record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell16.setCellStyle(numberStyle);
		 } else {
		     cell16.setCellValue("");
		     cell16.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(38) != null ? sheet.getRow(38) : sheet.createRow(38);
		  Cell cell17 = row.createCell(0);

		 if (record1.getR3_NAME_OF_BANK_NOSTRO1() != null && !record1.getR3_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell17.setCellValue(record1.getR3_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell17.setCellStyle(numberStyle);
		 } else {
		     cell17.setCellValue("");
		     cell17.setCellStyle(textStyle);
		 }

		  Cell cell18 = row.createCell(1);

		 if (record1.getR3_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR3_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell18.setCellValue(record1.getR3_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell18.setCellStyle(numberStyle);
		 } else {
		     cell18.setCellValue("");
		     cell18.setCellStyle(textStyle);
		 }

		  Cell cell19 = row.createCell(2);

		 if (record1.getR3_PURPOSE_NOSTRO1() != null && !record1.getR3_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell19.setCellValue(record1.getR3_PURPOSE_NOSTRO1().toString().trim() );
		     cell19.setCellStyle(numberStyle);
		 } else {
		     cell19.setCellValue("");
		     cell19.setCellStyle(textStyle);
		 }

		  Cell cell20 = row.createCell(3);

		 if (record1.getR3_CURRENCY_NOSTRO1() != null && !record1.getR3_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell20.setCellValue(record1.getR3_CURRENCY_NOSTRO1().toString().trim() );
		     cell20.setCellStyle(numberStyle);
		 } else {
		     cell20.setCellValue("");
		     cell20.setCellStyle(textStyle);
		 }

		  Cell cell21 = row.createCell(4);

		 if (record1.getR3_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR3_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell21.setCellValue(record1.getR3_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell21.setCellStyle(numberStyle);
		 } else {
		     cell21.setCellValue("");
		     cell21.setCellStyle(textStyle);
		 }

		  Cell cell22 = row.createCell(5);

		 if (record1.getR3_RISK_WEIGHT_NOSTRO1() != null && !record1.getR3_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell22.setCellValue(record1.getR3_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell22.setCellStyle(numberStyle);
		 } else {
		     cell22.setCellValue("");
		     cell22.setCellStyle(textStyle);
		 }

		  Cell cell23 = row.createCell(6);

		 if (record1.getR3_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR3_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell23.setCellValue(record1.getR3_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell23.setCellStyle(numberStyle);
		 } else {
		     cell23.setCellValue("");
		     cell23.setCellStyle(textStyle);
		 }

		  Cell cell24 = row.createCell(7);

		 if (record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell24.setCellValue(record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell24.setCellStyle(numberStyle);
		 } else {
		     cell24.setCellValue("");
		     cell24.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(39) != null ? sheet.getRow(39) : sheet.createRow(39);
		  Cell cell25 = row.createCell(0);

		 if (record1.getR4_NAME_OF_BANK_NOSTRO1() != null && !record1.getR4_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell25.setCellValue(record1.getR4_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell25.setCellStyle(numberStyle);
		 } else {
		     cell25.setCellValue("");
		     cell25.setCellStyle(textStyle);
		 }

		  Cell cell26 = row.createCell(1);

		 if (record1.getR4_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR4_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell26.setCellValue(record1.getR4_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell26.setCellStyle(numberStyle);
		 } else {
		     cell26.setCellValue("");
		     cell26.setCellStyle(textStyle);
		 }

		  Cell cell27 = row.createCell(2);

		 if (record1.getR4_PURPOSE_NOSTRO1() != null && !record1.getR4_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell27.setCellValue(record1.getR4_PURPOSE_NOSTRO1().toString().trim() );
		     cell27.setCellStyle(numberStyle);
		 } else {
		     cell27.setCellValue("");
		     cell27.setCellStyle(textStyle);
		 }

		  Cell cell28 = row.createCell(3);

		 if (record1.getR4_CURRENCY_NOSTRO1() != null && !record1.getR4_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell28.setCellValue(record1.getR4_CURRENCY_NOSTRO1().toString().trim() );
		     cell28.setCellStyle(numberStyle);
		 } else {
		     cell28.setCellValue("");
		     cell28.setCellStyle(textStyle);
		 }

		  Cell cell29 = row.createCell(4);

		 if (record1.getR4_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR4_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell29.setCellValue(record1.getR4_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell29.setCellStyle(numberStyle);
		 } else {
		     cell29.setCellValue("");
		     cell29.setCellStyle(textStyle);
		 }

		  Cell cell30 = row.createCell(5);

		 if (record1.getR4_RISK_WEIGHT_NOSTRO1() != null && !record1.getR4_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell30.setCellValue(record1.getR4_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell30.setCellStyle(numberStyle);
		 } else {
		     cell30.setCellValue("");
		     cell30.setCellStyle(textStyle);
		 }

		  Cell cell31 = row.createCell(6);

		 if (record1.getR4_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR4_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell31.setCellValue(record1.getR4_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell31.setCellStyle(numberStyle);
		 } else {
		     cell31.setCellValue("");
		     cell31.setCellStyle(textStyle);
		 }

		  Cell cell32 = row.createCell(7);

		 if (record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell32.setCellValue(record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell32.setCellStyle(numberStyle);
		 } else {
		     cell32.setCellValue("");
		     cell32.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(40) != null ? sheet.getRow(40) : sheet.createRow(40);
		  Cell cell33 = row.createCell(0);

		 if (record1.getR5_NAME_OF_BANK_NOSTRO1() != null && !record1.getR5_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell33.setCellValue(record1.getR5_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell33.setCellStyle(numberStyle);
		 } else {
		     cell33.setCellValue("");
		     cell33.setCellStyle(textStyle);
		 }

		  Cell cell34 = row.createCell(1);

		 if (record1.getR5_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR5_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell34.setCellValue(record1.getR5_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell34.setCellStyle(numberStyle);
		 } else {
		     cell34.setCellValue("");
		     cell34.setCellStyle(textStyle);
		 }

		  Cell cell35 = row.createCell(2);

		 if (record1.getR5_PURPOSE_NOSTRO1() != null && !record1.getR5_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell35.setCellValue(record1.getR5_PURPOSE_NOSTRO1().toString().trim() );
		     cell35.setCellStyle(numberStyle);
		 } else {
		     cell35.setCellValue("");
		     cell35.setCellStyle(textStyle);
		 }

		  Cell cell36 = row.createCell(3);

		 if (record1.getR5_CURRENCY_NOSTRO1() != null && !record1.getR5_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell36.setCellValue(record1.getR5_CURRENCY_NOSTRO1().toString().trim() );
		     cell36.setCellStyle(numberStyle);
		 } else {
		     cell36.setCellValue("");
		     cell36.setCellStyle(textStyle);
		 }

		  Cell cell37 = row.createCell(4);

		 if (record1.getR5_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR5_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell37.setCellValue(record1.getR5_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell37.setCellStyle(numberStyle);
		 } else {
		     cell37.setCellValue("");
		     cell37.setCellStyle(textStyle);
		 }

		  Cell cell38 = row.createCell(5);

		 if (record1.getR5_RISK_WEIGHT_NOSTRO1() != null && !record1.getR5_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell38.setCellValue(record1.getR5_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell38.setCellStyle(numberStyle);
		 } else {
		     cell38.setCellValue("");
		     cell38.setCellStyle(textStyle);
		 }

		  Cell cell39 = row.createCell(6);

		 if (record1.getR5_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR5_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell39.setCellValue(record1.getR5_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell39.setCellStyle(numberStyle);
		 } else {
		     cell39.setCellValue("");
		     cell39.setCellStyle(textStyle);
		 }

		  Cell cell40 = row.createCell(7);

		 if (record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell40.setCellValue(record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell40.setCellStyle(numberStyle);
		 } else {
		     cell40.setCellValue("");
		     cell40.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(41) != null ? sheet.getRow(41) : sheet.createRow(41);
		  Cell cell41 = row.createCell(0);

		 if (record1.getR6_NAME_OF_BANK_NOSTRO1() != null && !record1.getR6_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell41.setCellValue(record1.getR6_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell41.setCellStyle(numberStyle);
		 } else {
		     cell41.setCellValue("");
		     cell41.setCellStyle(textStyle);
		 }

		  Cell cell42 = row.createCell(1);

		 if (record1.getR6_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR6_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell42.setCellValue(record1.getR6_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell42.setCellStyle(numberStyle);
		 } else {
		     cell42.setCellValue("");
		     cell42.setCellStyle(textStyle);
		 }

		  Cell cell43 = row.createCell(2);

		 if (record1.getR6_PURPOSE_NOSTRO1() != null && !record1.getR6_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell43.setCellValue(record1.getR6_PURPOSE_NOSTRO1().toString().trim() );
		     cell43.setCellStyle(numberStyle);
		 } else {
		     cell43.setCellValue("");
		     cell43.setCellStyle(textStyle);
		 }

		  Cell cell44 = row.createCell(3);

		 if (record1.getR6_CURRENCY_NOSTRO1() != null && !record1.getR6_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell44.setCellValue(record1.getR6_CURRENCY_NOSTRO1().toString().trim() );
		     cell44.setCellStyle(numberStyle);
		 } else {
		     cell44.setCellValue("");
		     cell44.setCellStyle(textStyle);
		 }

		  Cell cell45 = row.createCell(4);

		 if (record1.getR6_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR6_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell45.setCellValue(record1.getR6_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell45.setCellStyle(numberStyle);
		 } else {
		     cell45.setCellValue("");
		     cell45.setCellStyle(textStyle);
		 }

		  Cell cell46 = row.createCell(5);

		 if (record1.getR6_RISK_WEIGHT_NOSTRO1() != null && !record1.getR6_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell46.setCellValue(record1.getR6_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell46.setCellStyle(numberStyle);
		 } else {
		     cell46.setCellValue("");
		     cell46.setCellStyle(textStyle);
		 }

		  Cell cell47 = row.createCell(6);

		 if (record1.getR6_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR6_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell47.setCellValue(record1.getR6_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell47.setCellStyle(numberStyle);
		 } else {
		     cell47.setCellValue("");
		     cell47.setCellStyle(textStyle);
		 }

		  Cell cell48 = row.createCell(7);

		 if (record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell48.setCellValue(record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell48.setCellStyle(numberStyle);
		 } else {
		     cell48.setCellValue("");
		     cell48.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(42) != null ? sheet.getRow(42) : sheet.createRow(42);
		  Cell cell49 = row.createCell(0);

		 if (record1.getR7_NAME_OF_BANK_NOSTRO1() != null && !record1.getR7_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell49.setCellValue(record1.getR7_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell49.setCellStyle(numberStyle);
		 } else {
		     cell49.setCellValue("");
		     cell49.setCellStyle(textStyle);
		 }

		  Cell cell50 = row.createCell(1);

		 if (record1.getR7_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR7_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell50.setCellValue(record1.getR7_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell50.setCellStyle(numberStyle);
		 } else {
		     cell50.setCellValue("");
		     cell50.setCellStyle(textStyle);
		 }

		  Cell cell51 = row.createCell(2);

		 if (record1.getR7_PURPOSE_NOSTRO1() != null && !record1.getR7_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell51.setCellValue(record1.getR7_PURPOSE_NOSTRO1().toString().trim() );
		     cell51.setCellStyle(numberStyle);
		 } else {
		     cell51.setCellValue("");
		     cell51.setCellStyle(textStyle);
		 }

		  Cell cell52 = row.createCell(3);

		 if (record1.getR7_CURRENCY_NOSTRO1() != null && !record1.getR7_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell52.setCellValue(record1.getR7_CURRENCY_NOSTRO1().toString().trim() );
		     cell52.setCellStyle(numberStyle);
		 } else {
		     cell52.setCellValue("");
		     cell52.setCellStyle(textStyle);
		 }

		  Cell cell53 = row.createCell(4);

		 if (record1.getR7_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR7_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell53.setCellValue(record1.getR7_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell53.setCellStyle(numberStyle);
		 } else {
		     cell53.setCellValue("");
		     cell53.setCellStyle(textStyle);
		 }

		  Cell cell54 = row.createCell(5);

		 if (record1.getR7_RISK_WEIGHT_NOSTRO1() != null && !record1.getR7_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell54.setCellValue(record1.getR7_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell54.setCellStyle(numberStyle);
		 } else {
		     cell54.setCellValue("");
		     cell54.setCellStyle(textStyle);
		 }

		  Cell cell55 = row.createCell(6);

		 if (record1.getR7_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR7_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell55.setCellValue(record1.getR7_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell55.setCellStyle(numberStyle);
		 } else {
		     cell55.setCellValue("");
		     cell55.setCellStyle(textStyle);
		 }

		  Cell cell56 = row.createCell(7);

		 if (record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell56.setCellValue(record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell56.setCellStyle(numberStyle);
		 } else {
		     cell56.setCellValue("");
		     cell56.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(43) != null ? sheet.getRow(43) : sheet.createRow(43);
		  Cell cell57 = row.createCell(0);

		 if (record1.getR8_NAME_OF_BANK_NOSTRO1() != null && !record1.getR8_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell57.setCellValue(record1.getR8_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell57.setCellStyle(numberStyle);
		 } else {
		     cell57.setCellValue("");
		     cell57.setCellStyle(textStyle);
		 }

		  Cell cell58 = row.createCell(1);

		 if (record1.getR8_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR8_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell58.setCellValue(record1.getR8_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell58.setCellStyle(numberStyle);
		 } else {
		     cell58.setCellValue("");
		     cell58.setCellStyle(textStyle);
		 }

		  Cell cell59 = row.createCell(2);

		 if (record1.getR8_PURPOSE_NOSTRO1() != null && !record1.getR8_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell59.setCellValue(record1.getR8_PURPOSE_NOSTRO1().toString().trim() );
		     cell59.setCellStyle(numberStyle);
		 } else {
		     cell59.setCellValue("");
		     cell59.setCellStyle(textStyle);
		 }

		  Cell cell60 = row.createCell(3);

		 if (record1.getR8_CURRENCY_NOSTRO1() != null && !record1.getR8_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell60.setCellValue(record1.getR8_CURRENCY_NOSTRO1().toString().trim() );
		     cell60.setCellStyle(numberStyle);
		 } else {
		     cell60.setCellValue("");
		     cell60.setCellStyle(textStyle);
		 }

		  Cell cell61 = row.createCell(4);

		 if (record1.getR8_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR8_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell61.setCellValue(record1.getR8_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell61.setCellStyle(numberStyle);
		 } else {
		     cell61.setCellValue("");
		     cell61.setCellStyle(textStyle);
		 }

		  Cell cell62 = row.createCell(5);

		 if (record1.getR8_RISK_WEIGHT_NOSTRO1() != null && !record1.getR8_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell62.setCellValue(record1.getR8_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell62.setCellStyle(numberStyle);
		 } else {
		     cell62.setCellValue("");
		     cell62.setCellStyle(textStyle);
		 }

		  Cell cell63 = row.createCell(6);

		 if (record1.getR8_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR8_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell63.setCellValue(record1.getR8_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell63.setCellStyle(numberStyle);
		 } else {
		     cell63.setCellValue("");
		     cell63.setCellStyle(textStyle);
		 }

		  Cell cell64 = row.createCell(7);

		 if (record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell64.setCellValue(record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell64.setCellStyle(numberStyle);
		 } else {
		     cell64.setCellValue("");
		     cell64.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(44) != null ? sheet.getRow(44) : sheet.createRow(44);
		  Cell cell65 = row.createCell(0);

		 if (record1.getR9_NAME_OF_BANK_NOSTRO1() != null && !record1.getR9_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell65.setCellValue(record1.getR9_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell65.setCellStyle(numberStyle);
		 } else {
		     cell65.setCellValue("");
		     cell65.setCellStyle(textStyle);
		 }

		  Cell cell66 = row.createCell(1);

		 if (record1.getR9_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR9_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell66.setCellValue(record1.getR9_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell66.setCellStyle(numberStyle);
		 } else {
		     cell66.setCellValue("");
		     cell66.setCellStyle(textStyle);
		 }

		  Cell cell67 = row.createCell(2);

		 if (record1.getR9_PURPOSE_NOSTRO1() != null && !record1.getR9_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell67.setCellValue(record1.getR9_PURPOSE_NOSTRO1().toString().trim() );
		     cell67.setCellStyle(numberStyle);
		 } else {
		     cell67.setCellValue("");
		     cell67.setCellStyle(textStyle);
		 }

		  Cell cell68 = row.createCell(3);

		 if (record1.getR9_CURRENCY_NOSTRO1() != null && !record1.getR9_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell68.setCellValue(record1.getR9_CURRENCY_NOSTRO1().toString().trim() );
		     cell68.setCellStyle(numberStyle);
		 } else {
		     cell68.setCellValue("");
		     cell68.setCellStyle(textStyle);
		 }

		  Cell cell69 = row.createCell(4);

		 if (record1.getR9_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR9_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell69.setCellValue(record1.getR9_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell69.setCellStyle(numberStyle);
		 } else {
		     cell69.setCellValue("");
		     cell69.setCellStyle(textStyle);
		 }

		  Cell cell70 = row.createCell(5);

		 if (record1.getR9_RISK_WEIGHT_NOSTRO1() != null && !record1.getR9_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell70.setCellValue(record1.getR9_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell70.setCellStyle(numberStyle);
		 } else {
		     cell70.setCellValue("");
		     cell70.setCellStyle(textStyle);
		 }

		  Cell cell71 = row.createCell(6);

		 if (record1.getR9_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR9_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell71.setCellValue(record1.getR9_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell71.setCellStyle(numberStyle);
		 } else {
		     cell71.setCellValue("");
		     cell71.setCellStyle(textStyle);
		 }

		  Cell cell72 = row.createCell(7);

		 if (record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell72.setCellValue(record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell72.setCellStyle(numberStyle);
		 } else {
		     cell72.setCellValue("");
		     cell72.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(45) != null ? sheet.getRow(45) : sheet.createRow(45);
		  Cell cell73 = row.createCell(0);

		 if (record1.getR10_NAME_OF_BANK_NOSTRO1() != null && !record1.getR10_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell73.setCellValue(record1.getR10_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell73.setCellStyle(numberStyle);
		 } else {
		     cell73.setCellValue("");
		     cell73.setCellStyle(textStyle);
		 }

		  Cell cell74 = row.createCell(1);

		 if (record1.getR10_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR10_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell74.setCellValue(record1.getR10_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell74.setCellStyle(numberStyle);
		 } else {
		     cell74.setCellValue("");
		     cell74.setCellStyle(textStyle);
		 }

		  Cell cell75 = row.createCell(2);

		 if (record1.getR10_PURPOSE_NOSTRO1() != null && !record1.getR10_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell75.setCellValue(record1.getR10_PURPOSE_NOSTRO1().toString().trim() );
		     cell75.setCellStyle(numberStyle);
		 } else {
		     cell75.setCellValue("");
		     cell75.setCellStyle(textStyle);
		 }

		  Cell cell76 = row.createCell(3);

		 if (record1.getR10_CURRENCY_NOSTRO1() != null && !record1.getR10_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell76.setCellValue(record1.getR10_CURRENCY_NOSTRO1().toString().trim() );
		     cell76.setCellStyle(numberStyle);
		 } else {
		     cell76.setCellValue("");
		     cell76.setCellStyle(textStyle);
		 }

		  Cell cell77 = row.createCell(4);

		 if (record1.getR10_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR10_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell77.setCellValue(record1.getR10_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell77.setCellStyle(numberStyle);
		 } else {
		     cell77.setCellValue("");
		     cell77.setCellStyle(textStyle);
		 }

		  Cell cell78 = row.createCell(5);

		 if (record1.getR10_RISK_WEIGHT_NOSTRO1() != null && !record1.getR10_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell78.setCellValue(record1.getR10_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell78.setCellStyle(numberStyle);
		 } else {
		     cell78.setCellValue("");
		     cell78.setCellStyle(textStyle);
		 }

		  Cell cell79 = row.createCell(6);

		 if (record1.getR10_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR10_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell79.setCellValue(record1.getR10_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell79.setCellStyle(numberStyle);
		 } else {
		     cell79.setCellValue("");
		     cell79.setCellStyle(textStyle);
		 }

		  Cell cell80 = row.createCell(7);

		 if (record1.getR10_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR10_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell80.setCellValue(record1.getR10_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell80.setCellStyle(numberStyle);
		 } else {
		     cell80.setCellValue("");
		     cell80.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(46) != null ? sheet.getRow(46) : sheet.createRow(46);
		  Cell cell81 = row.createCell(0);

		 if (record1.getR11_NAME_OF_BANK_NOSTRO1() != null && !record1.getR11_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell81.setCellValue(record1.getR11_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell81.setCellStyle(numberStyle);
		 } else {
		     cell81.setCellValue("");
		     cell81.setCellStyle(textStyle);
		 }

		  Cell cell82 = row.createCell(1);

		 if (record1.getR11_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR11_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell82.setCellValue(record1.getR11_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell82.setCellStyle(numberStyle);
		 } else {
		     cell82.setCellValue("");
		     cell82.setCellStyle(textStyle);
		 }

		  Cell cell83 = row.createCell(2);

		 if (record1.getR11_PURPOSE_NOSTRO1() != null && !record1.getR11_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell83.setCellValue(record1.getR11_PURPOSE_NOSTRO1().toString().trim() );
		     cell83.setCellStyle(numberStyle);
		 } else {
		     cell83.setCellValue("");
		     cell83.setCellStyle(textStyle);
		 }

		  Cell cell84 = row.createCell(3);

		 if (record1.getR11_CURRENCY_NOSTRO1() != null && !record1.getR11_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell84.setCellValue(record1.getR11_CURRENCY_NOSTRO1().toString().trim() );
		     cell84.setCellStyle(numberStyle);
		 } else {
		     cell84.setCellValue("");
		     cell84.setCellStyle(textStyle);
		 }

		  Cell cell85 = row.createCell(4);

		 if (record1.getR11_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR11_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell85.setCellValue(record1.getR11_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell85.setCellStyle(numberStyle);
		 } else {
		     cell85.setCellValue("");
		     cell85.setCellStyle(textStyle);
		 }

		  Cell cell86 = row.createCell(5);

		 if (record1.getR11_RISK_WEIGHT_NOSTRO1() != null && !record1.getR11_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell86.setCellValue(record1.getR11_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell86.setCellStyle(numberStyle);
		 } else {
		     cell86.setCellValue("");
		     cell86.setCellStyle(textStyle);
		 }

		  Cell cell87 = row.createCell(6);

		 if (record1.getR11_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR11_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell87.setCellValue(record1.getR11_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell87.setCellStyle(numberStyle);
		 } else {
		     cell87.setCellValue("");
		     cell87.setCellStyle(textStyle);
		 }

		  Cell cell88 = row.createCell(7);

		 if (record1.getR11_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR11_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell88.setCellValue(record1.getR11_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell88.setCellStyle(numberStyle);
		 } else {
		     cell88.setCellValue("");
		     cell88.setCellStyle(textStyle);
		 }



			
		}
	
	private void writeEmailArchExcelRowData4(Sheet sheet, List<BrrsMNosvosP4Archival> dataList,
	        String[] rowCodes, String[] fieldSuffixes, int baseRow,
	        CellStyle numberStyle, CellStyle textStyle) {
	    System.out.println("came to write row data 4 method");

	    System.out.println("came to write row data 1 method");
		
	    BrrsMNosvosP4Archival record1 = dataList.get(0);
	    
	    Row  row = sheet.getRow(51) != null ? sheet.getRow(51) : sheet.createRow(51);
	    Cell cell1 = row.createCell(0);

	    if (record1.getR1_NAME_OF_BANK_VOSTRO1() != null && !record1.getR1_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell1.setCellValue(record1.getR1_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell1.setCellStyle(numberStyle);
	    } else {
	        cell1.setCellValue("");
	        cell1.setCellStyle(textStyle);
	    }

	     Cell cell2 = row.createCell(1);

	    if (record1.getR1_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR1_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell2.setCellValue(record1.getR1_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell2.setCellStyle(numberStyle);
	    } else {
	        cell2.setCellValue("");
	        cell2.setCellStyle(textStyle);
	    }

	     Cell cell3 = row.createCell(2);

	    if (record1.getR1_PURPOSE_VOSTRO1() != null && !record1.getR1_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell3.setCellValue(record1.getR1_PURPOSE_VOSTRO1().toString().trim() );
	        cell3.setCellStyle(numberStyle);
	    } else {
	        cell3.setCellValue("");
	        cell3.setCellStyle(textStyle);
	    }

	     Cell cell4 = row.createCell(3);

	    if (record1.getR1_CURRENCY_VOSTRO1() != null && !record1.getR1_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell4.setCellValue(record1.getR1_CURRENCY_VOSTRO1().toString().trim() );
	        cell4.setCellStyle(numberStyle);
	    } else {
	        cell4.setCellValue("");
	        cell4.setCellStyle(textStyle);
	    }

	     Cell cell7 = row.createCell(6);

	    if (record1.getR1_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR1_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell7.setCellValue(record1.getR1_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell7.setCellStyle(numberStyle);
	    } else {
	        cell7.setCellValue("");
	        cell7.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(52) != null ? sheet.getRow(52) : sheet.createRow(52);
	     Cell cell9 = row.createCell(0);

	    if (record1.getR2_NAME_OF_BANK_VOSTRO1() != null && !record1.getR2_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell9.setCellValue(record1.getR2_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell9.setCellStyle(numberStyle);
	    } else {
	        cell9.setCellValue("");
	        cell9.setCellStyle(textStyle);
	    }

	     Cell cell10 = row.createCell(1);

	    if (record1.getR2_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR2_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell10.setCellValue(record1.getR2_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell10.setCellStyle(numberStyle);
	    } else {
	        cell10.setCellValue("");
	        cell10.setCellStyle(textStyle);
	    }

	     Cell cell11 = row.createCell(2);

	    if (record1.getR2_PURPOSE_VOSTRO1() != null && !record1.getR2_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell11.setCellValue(record1.getR2_PURPOSE_VOSTRO1().toString().trim() );
	        cell11.setCellStyle(numberStyle);
	    } else {
	        cell11.setCellValue("");
	        cell11.setCellStyle(textStyle);
	    }

	     Cell cell12 = row.createCell(3);

	    if (record1.getR2_CURRENCY_VOSTRO1() != null && !record1.getR2_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell12.setCellValue(record1.getR2_CURRENCY_VOSTRO1().toString().trim() );
	        cell12.setCellStyle(numberStyle);
	    } else {
	        cell12.setCellValue("");
	        cell12.setCellStyle(textStyle);
	    }

	     Cell cell15 = row.createCell(6);

	    if (record1.getR2_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR2_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell15.setCellValue(record1.getR2_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell15.setCellStyle(numberStyle);
	    } else {
	        cell15.setCellValue("");
	        cell15.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(53) != null ? sheet.getRow(53) : sheet.createRow(53);
	     Cell cell17 = row.createCell(0);

	    if (record1.getR3_NAME_OF_BANK_VOSTRO1() != null && !record1.getR3_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell17.setCellValue(record1.getR3_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell17.setCellStyle(numberStyle);
	    } else {
	        cell17.setCellValue("");
	        cell17.setCellStyle(textStyle);
	    }

	     Cell cell18 = row.createCell(1);

	    if (record1.getR3_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR3_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell18.setCellValue(record1.getR3_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell18.setCellStyle(numberStyle);
	    } else {
	        cell18.setCellValue("");
	        cell18.setCellStyle(textStyle);
	    }

	     Cell cell19 = row.createCell(2);

	    if (record1.getR3_PURPOSE_VOSTRO1() != null && !record1.getR3_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell19.setCellValue(record1.getR3_PURPOSE_VOSTRO1().toString().trim() );
	        cell19.setCellStyle(numberStyle);
	    } else {
	        cell19.setCellValue("");
	        cell19.setCellStyle(textStyle);
	    }

	     Cell cell20 = row.createCell(3);

	    if (record1.getR3_CURRENCY_VOSTRO1() != null && !record1.getR3_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell20.setCellValue(record1.getR3_CURRENCY_VOSTRO1().toString().trim() );
	        cell20.setCellStyle(numberStyle);
	    } else {
	        cell20.setCellValue("");
	        cell20.setCellStyle(textStyle);
	    }

	     Cell cell23 = row.createCell(6);

	    if (record1.getR3_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR3_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell23.setCellValue(record1.getR3_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell23.setCellStyle(numberStyle);
	    } else {
	        cell23.setCellValue("");
	        cell23.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(54) != null ? sheet.getRow(54) : sheet.createRow(54);
	     Cell cell25 = row.createCell(0);

	    if (record1.getR4_NAME_OF_BANK_VOSTRO1() != null && !record1.getR4_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell25.setCellValue(record1.getR4_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell25.setCellStyle(numberStyle);
	    } else {
	        cell25.setCellValue("");
	        cell25.setCellStyle(textStyle);
	    }

	     Cell cell26 = row.createCell(1);

	    if (record1.getR4_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR4_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell26.setCellValue(record1.getR4_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell26.setCellStyle(numberStyle);
	    } else {
	        cell26.setCellValue("");
	        cell26.setCellStyle(textStyle);
	    }

	     Cell cell27 = row.createCell(2);

	    if (record1.getR4_PURPOSE_VOSTRO1() != null && !record1.getR4_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell27.setCellValue(record1.getR4_PURPOSE_VOSTRO1().toString().trim() );
	        cell27.setCellStyle(numberStyle);
	    } else {
	        cell27.setCellValue("");
	        cell27.setCellStyle(textStyle);
	    }

	     Cell cell28 = row.createCell(3);

	    if (record1.getR4_CURRENCY_VOSTRO1() != null && !record1.getR4_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell28.setCellValue(record1.getR4_CURRENCY_VOSTRO1().toString().trim() );
	        cell28.setCellStyle(numberStyle);
	    } else {
	        cell28.setCellValue("");
	        cell28.setCellStyle(textStyle);
	    }

	     Cell cell31 = row.createCell(6);

	    if (record1.getR4_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR4_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell31.setCellValue(record1.getR4_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell31.setCellStyle(numberStyle);
	    } else {
	        cell31.setCellValue("");
	        cell31.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(55) != null ? sheet.getRow(55) : sheet.createRow(55);
	     Cell cell33 = row.createCell(0);

	    if (record1.getR5_NAME_OF_BANK_VOSTRO1() != null && !record1.getR5_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell33.setCellValue(record1.getR5_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell33.setCellStyle(numberStyle);
	    } else {
	        cell33.setCellValue("");
	        cell33.setCellStyle(textStyle);
	    }

	     Cell cell34 = row.createCell(1);

	    if (record1.getR5_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR5_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell34.setCellValue(record1.getR5_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell34.setCellStyle(numberStyle);
	    } else {
	        cell34.setCellValue("");
	        cell34.setCellStyle(textStyle);
	    }

	     Cell cell35 = row.createCell(2);

	    if (record1.getR5_PURPOSE_VOSTRO1() != null && !record1.getR5_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell35.setCellValue(record1.getR5_PURPOSE_VOSTRO1().toString().trim() );
	        cell35.setCellStyle(numberStyle);
	    } else {
	        cell35.setCellValue("");
	        cell35.setCellStyle(textStyle);
	    }

	     Cell cell36 = row.createCell(3);

	    if (record1.getR5_CURRENCY_VOSTRO1() != null && !record1.getR5_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell36.setCellValue(record1.getR5_CURRENCY_VOSTRO1().toString().trim() );
	        cell36.setCellStyle(numberStyle);
	    } else {
	        cell36.setCellValue("");
	        cell36.setCellStyle(textStyle);
	    }

	     Cell cell39 = row.createCell(6);

	    if (record1.getR5_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR5_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell39.setCellValue(record1.getR5_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell39.setCellStyle(numberStyle);
	    } else {
	        cell39.setCellValue("");
	        cell39.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(56) != null ? sheet.getRow(56) : sheet.createRow(56);
	     Cell cell41 = row.createCell(0);

	    if (record1.getR6_NAME_OF_BANK_VOSTRO1() != null && !record1.getR6_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell41.setCellValue(record1.getR6_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell41.setCellStyle(numberStyle);
	    } else {
	        cell41.setCellValue("");
	        cell41.setCellStyle(textStyle);
	    }

	     Cell cell42 = row.createCell(1);

	    if (record1.getR6_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR6_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell42.setCellValue(record1.getR6_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell42.setCellStyle(numberStyle);
	    } else {
	        cell42.setCellValue("");
	        cell42.setCellStyle(textStyle);
	    }

	     Cell cell43 = row.createCell(2);

	    if (record1.getR6_PURPOSE_VOSTRO1() != null && !record1.getR6_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell43.setCellValue(record1.getR6_PURPOSE_VOSTRO1().toString().trim() );
	        cell43.setCellStyle(numberStyle);
	    } else {
	        cell43.setCellValue("");
	        cell43.setCellStyle(textStyle);
	    }

	     Cell cell44 = row.createCell(3);

	    if (record1.getR6_CURRENCY_VOSTRO1() != null && !record1.getR6_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell44.setCellValue(record1.getR6_CURRENCY_VOSTRO1().toString().trim() );
	        cell44.setCellStyle(numberStyle);
	    } else {
	        cell44.setCellValue("");
	        cell44.setCellStyle(textStyle);
	    }

	     Cell cell47 = row.createCell(6);

	    if (record1.getR6_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR6_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell47.setCellValue(record1.getR6_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell47.setCellStyle(numberStyle);
	    } else {
	        cell47.setCellValue("");
	        cell47.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(57) != null ? sheet.getRow(57) : sheet.createRow(57);
	     Cell cell49 = row.createCell(0);

	    if (record1.getR7_NAME_OF_BANK_VOSTRO1() != null && !record1.getR7_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell49.setCellValue(record1.getR7_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell49.setCellStyle(numberStyle);
	    } else {
	        cell49.setCellValue("");
	        cell49.setCellStyle(textStyle);
	    }

	     Cell cell50 = row.createCell(1);

	    if (record1.getR7_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR7_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell50.setCellValue(record1.getR7_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell50.setCellStyle(numberStyle);
	    } else {
	        cell50.setCellValue("");
	        cell50.setCellStyle(textStyle);
	    }

	     Cell cell51 = row.createCell(2);

	    if (record1.getR7_PURPOSE_VOSTRO1() != null && !record1.getR7_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell51.setCellValue(record1.getR7_PURPOSE_VOSTRO1().toString().trim() );
	        cell51.setCellStyle(numberStyle);
	    } else {
	        cell51.setCellValue("");
	        cell51.setCellStyle(textStyle);
	    }

	     Cell cell52 = row.createCell(3);

	    if (record1.getR7_CURRENCY_VOSTRO1() != null && !record1.getR7_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell52.setCellValue(record1.getR7_CURRENCY_VOSTRO1().toString().trim() );
	        cell52.setCellStyle(numberStyle);
	    } else {
	        cell52.setCellValue("");
	        cell52.setCellStyle(textStyle);
	    }

	     Cell cell55 = row.createCell(6);

	    if (record1.getR7_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR7_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell55.setCellValue(record1.getR7_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell55.setCellStyle(numberStyle);
	    } else {
	        cell55.setCellValue("");
	        cell55.setCellStyle(textStyle);
	    }



		
	}
	
	private void writeEmailExcelRowData1(Sheet sheet, List<BrrsMNosvosP1> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			
		System.out.println("came to write row data 1 method");
		
		BrrsMNosvosP1 record1 = dataList.get(0);
		
		 Row  row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);
		 Cell cell1 = row.createCell(0);

		 if (record1.getR1_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR1_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell1.setCellValue(record1.getR1_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell1.setCellStyle(numberStyle);
		 } else {
		     cell1.setCellValue("");
		     cell1.setCellStyle(textStyle);
		 }

		  Cell cell2 = row.createCell(1);

		 if (record1.getR1_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR1_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell2.setCellValue(record1.getR1_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell2.setCellStyle(numberStyle);
		 } else {
		     cell2.setCellValue("");
		     cell2.setCellStyle(textStyle);
		 }

		  Cell cell3 = row.createCell(2);

		 if (record1.getR1_PURPOSE_NOSTRO() != null && !record1.getR1_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell3.setCellValue(record1.getR1_PURPOSE_NOSTRO().toString().trim() );
		     cell3.setCellStyle(numberStyle);
		 } else {
		     cell3.setCellValue("");
		     cell3.setCellStyle(textStyle);
		 }

		  Cell cell4 = row.createCell(3);

		 if (record1.getR1_CURRENCY_NOSTRO() != null && !record1.getR1_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell4.setCellValue(record1.getR1_CURRENCY_NOSTRO().toString().trim() );
		     cell4.setCellStyle(numberStyle);
		 } else {
		     cell4.setCellValue("");
		     cell4.setCellStyle(textStyle);
		 }

		  Cell cell5 = row.createCell(4);

		 if (record1.getR1_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR1_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell5.setCellValue(record1.getR1_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell5.setCellStyle(numberStyle);
		 } else {
		     cell5.setCellValue("");
		     cell5.setCellStyle(textStyle);
		 }

		  Cell cell6 = row.createCell(5);

		 if (record1.getR1_RISK_WEIGHT_NOSTRO() != null && !record1.getR1_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell6.setCellValue(record1.getR1_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell6.setCellStyle(numberStyle);
		 } else {
		     cell6.setCellValue("");
		     cell6.setCellStyle(textStyle);
		 }

		  Cell cell7 = row.createCell(6);

		 if (record1.getR1_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR1_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell7.setCellValue(record1.getR1_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell7.setCellStyle(numberStyle);
		 } else {
		     cell7.setCellValue("");
		     cell7.setCellStyle(textStyle);
		 }

		  Cell cell8 = row.createCell(7);

		 if (record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell8.setCellValue(record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell8.setCellStyle(numberStyle);
		 } else {
		     cell8.setCellValue("");
		     cell8.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
		  Cell cell9 = row.createCell(0);

		 if (record1.getR2_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR2_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell9.setCellValue(record1.getR2_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell9.setCellStyle(numberStyle);
		 } else {
		     cell9.setCellValue("");
		     cell9.setCellStyle(textStyle);
		 }

		  Cell cell10 = row.createCell(1);

		 if (record1.getR2_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR2_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell10.setCellValue(record1.getR2_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell10.setCellStyle(numberStyle);
		 } else {
		     cell10.setCellValue("");
		     cell10.setCellStyle(textStyle);
		 }

		  Cell cell11 = row.createCell(2);

		 if (record1.getR2_PURPOSE_NOSTRO() != null && !record1.getR2_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell11.setCellValue(record1.getR2_PURPOSE_NOSTRO().toString().trim() );
		     cell11.setCellStyle(numberStyle);
		 } else {
		     cell11.setCellValue("");
		     cell11.setCellStyle(textStyle);
		 }

		  Cell cell12 = row.createCell(3);

		 if (record1.getR2_CURRENCY_NOSTRO() != null && !record1.getR2_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell12.setCellValue(record1.getR2_CURRENCY_NOSTRO().toString().trim() );
		     cell12.setCellStyle(numberStyle);
		 } else {
		     cell12.setCellValue("");
		     cell12.setCellStyle(textStyle);
		 }

		  Cell cell13 = row.createCell(4);

		 if (record1.getR2_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR2_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell13.setCellValue(record1.getR2_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell13.setCellStyle(numberStyle);
		 } else {
		     cell13.setCellValue("");
		     cell13.setCellStyle(textStyle);
		 }

		  Cell cell14 = row.createCell(5);

		 if (record1.getR2_RISK_WEIGHT_NOSTRO() != null && !record1.getR2_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell14.setCellValue(record1.getR2_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell14.setCellStyle(numberStyle);
		 } else {
		     cell14.setCellValue("");
		     cell14.setCellStyle(textStyle);
		 }

		  Cell cell15 = row.createCell(6);

		 if (record1.getR2_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR2_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell15.setCellValue(record1.getR2_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell15.setCellStyle(numberStyle);
		 } else {
		     cell15.setCellValue("");
		     cell15.setCellStyle(textStyle);
		 }

		  Cell cell16 = row.createCell(7);

		 if (record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell16.setCellValue(record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell16.setCellStyle(numberStyle);
		 } else {
		     cell16.setCellValue("");
		     cell16.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(12) != null ? sheet.getRow(12) : sheet.createRow(12);
		  Cell cell17 = row.createCell(0);

		 if (record1.getR3_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR3_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell17.setCellValue(record1.getR3_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell17.setCellStyle(numberStyle);
		 } else {
		     cell17.setCellValue("");
		     cell17.setCellStyle(textStyle);
		 }

		  Cell cell18 = row.createCell(1);

		 if (record1.getR3_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR3_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell18.setCellValue(record1.getR3_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell18.setCellStyle(numberStyle);
		 } else {
		     cell18.setCellValue("");
		     cell18.setCellStyle(textStyle);
		 }

		  Cell cell19 = row.createCell(2);

		 if (record1.getR3_PURPOSE_NOSTRO() != null && !record1.getR3_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell19.setCellValue(record1.getR3_PURPOSE_NOSTRO().toString().trim() );
		     cell19.setCellStyle(numberStyle);
		 } else {
		     cell19.setCellValue("");
		     cell19.setCellStyle(textStyle);
		 }

		  Cell cell20 = row.createCell(3);

		 if (record1.getR3_CURRENCY_NOSTRO() != null && !record1.getR3_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell20.setCellValue(record1.getR3_CURRENCY_NOSTRO().toString().trim() );
		     cell20.setCellStyle(numberStyle);
		 } else {
		     cell20.setCellValue("");
		     cell20.setCellStyle(textStyle);
		 }

		  Cell cell21 = row.createCell(4);

		 if (record1.getR3_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR3_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell21.setCellValue(record1.getR3_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell21.setCellStyle(numberStyle);
		 } else {
		     cell21.setCellValue("");
		     cell21.setCellStyle(textStyle);
		 }

		  Cell cell22 = row.createCell(5);

		 if (record1.getR3_RISK_WEIGHT_NOSTRO() != null && !record1.getR3_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell22.setCellValue(record1.getR3_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell22.setCellStyle(numberStyle);
		 } else {
		     cell22.setCellValue("");
		     cell22.setCellStyle(textStyle);
		 }

		  Cell cell23 = row.createCell(6);

		 if (record1.getR3_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR3_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell23.setCellValue(record1.getR3_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell23.setCellStyle(numberStyle);
		 } else {
		     cell23.setCellValue("");
		     cell23.setCellStyle(textStyle);
		 }

		  Cell cell24 = row.createCell(7);

		 if (record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell24.setCellValue(record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell24.setCellStyle(numberStyle);
		 } else {
		     cell24.setCellValue("");
		     cell24.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(13) != null ? sheet.getRow(13) : sheet.createRow(13);
		  Cell cell25 = row.createCell(0);

		 if (record1.getR4_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR4_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell25.setCellValue(record1.getR4_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell25.setCellStyle(numberStyle);
		 } else {
		     cell25.setCellValue("");
		     cell25.setCellStyle(textStyle);
		 }

		  Cell cell26 = row.createCell(1);

		 if (record1.getR4_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR4_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell26.setCellValue(record1.getR4_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell26.setCellStyle(numberStyle);
		 } else {
		     cell26.setCellValue("");
		     cell26.setCellStyle(textStyle);
		 }

		  Cell cell27 = row.createCell(2);

		 if (record1.getR4_PURPOSE_NOSTRO() != null && !record1.getR4_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell27.setCellValue(record1.getR4_PURPOSE_NOSTRO().toString().trim() );
		     cell27.setCellStyle(numberStyle);
		 } else {
		     cell27.setCellValue("");
		     cell27.setCellStyle(textStyle);
		 }

		  Cell cell28 = row.createCell(3);

		 if (record1.getR4_CURRENCY_NOSTRO() != null && !record1.getR4_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell28.setCellValue(record1.getR4_CURRENCY_NOSTRO().toString().trim() );
		     cell28.setCellStyle(numberStyle);
		 } else {
		     cell28.setCellValue("");
		     cell28.setCellStyle(textStyle);
		 }

		  Cell cell29 = row.createCell(4);

		 if (record1.getR4_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR4_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell29.setCellValue(record1.getR4_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell29.setCellStyle(numberStyle);
		 } else {
		     cell29.setCellValue("");
		     cell29.setCellStyle(textStyle);
		 }

		  Cell cell30 = row.createCell(5);

		 if (record1.getR4_RISK_WEIGHT_NOSTRO() != null && !record1.getR4_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell30.setCellValue(record1.getR4_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell30.setCellStyle(numberStyle);
		 } else {
		     cell30.setCellValue("");
		     cell30.setCellStyle(textStyle);
		 }

		  Cell cell31 = row.createCell(6);

		 if (record1.getR4_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR4_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell31.setCellValue(record1.getR4_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell31.setCellStyle(numberStyle);
		 } else {
		     cell31.setCellValue("");
		     cell31.setCellStyle(textStyle);
		 }

		  Cell cell32 = row.createCell(7);

		 if (record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell32.setCellValue(record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell32.setCellStyle(numberStyle);
		 } else {
		     cell32.setCellValue("");
		     cell32.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(14) != null ? sheet.getRow(14) : sheet.createRow(14);
		  Cell cell33 = row.createCell(0);

		 if (record1.getR5_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR5_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell33.setCellValue(record1.getR5_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell33.setCellStyle(numberStyle);
		 } else {
		     cell33.setCellValue("");
		     cell33.setCellStyle(textStyle);
		 }

		  Cell cell34 = row.createCell(1);

		 if (record1.getR5_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR5_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell34.setCellValue(record1.getR5_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell34.setCellStyle(numberStyle);
		 } else {
		     cell34.setCellValue("");
		     cell34.setCellStyle(textStyle);
		 }

		  Cell cell35 = row.createCell(2);

		 if (record1.getR5_PURPOSE_NOSTRO() != null && !record1.getR5_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell35.setCellValue(record1.getR5_PURPOSE_NOSTRO().toString().trim() );
		     cell35.setCellStyle(numberStyle);
		 } else {
		     cell35.setCellValue("");
		     cell35.setCellStyle(textStyle);
		 }

		  Cell cell36 = row.createCell(3);

		 if (record1.getR5_CURRENCY_NOSTRO() != null && !record1.getR5_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell36.setCellValue(record1.getR5_CURRENCY_NOSTRO().toString().trim() );
		     cell36.setCellStyle(numberStyle);
		 } else {
		     cell36.setCellValue("");
		     cell36.setCellStyle(textStyle);
		 }

		  Cell cell37 = row.createCell(4);

		 if (record1.getR5_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR5_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell37.setCellValue(record1.getR5_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell37.setCellStyle(numberStyle);
		 } else {
		     cell37.setCellValue("");
		     cell37.setCellStyle(textStyle);
		 }

		  Cell cell38 = row.createCell(5);

		 if (record1.getR5_RISK_WEIGHT_NOSTRO() != null && !record1.getR5_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell38.setCellValue(record1.getR5_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell38.setCellStyle(numberStyle);
		 } else {
		     cell38.setCellValue("");
		     cell38.setCellStyle(textStyle);
		 }

		  Cell cell39 = row.createCell(6);

		 if (record1.getR5_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR5_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell39.setCellValue(record1.getR5_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell39.setCellStyle(numberStyle);
		 } else {
		     cell39.setCellValue("");
		     cell39.setCellStyle(textStyle);
		 }

		  Cell cell40 = row.createCell(7);

		 if (record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell40.setCellValue(record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell40.setCellStyle(numberStyle);
		 } else {
		     cell40.setCellValue("");
		     cell40.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(15) != null ? sheet.getRow(15) : sheet.createRow(15);
		  Cell cell41 = row.createCell(0);

		 if (record1.getR6_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR6_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell41.setCellValue(record1.getR6_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell41.setCellStyle(numberStyle);
		 } else {
		     cell41.setCellValue("");
		     cell41.setCellStyle(textStyle);
		 }

		  Cell cell42 = row.createCell(1);

		 if (record1.getR6_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR6_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell42.setCellValue(record1.getR6_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell42.setCellStyle(numberStyle);
		 } else {
		     cell42.setCellValue("");
		     cell42.setCellStyle(textStyle);
		 }

		  Cell cell43 = row.createCell(2);

		 if (record1.getR6_PURPOSE_NOSTRO() != null && !record1.getR6_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell43.setCellValue(record1.getR6_PURPOSE_NOSTRO().toString().trim() );
		     cell43.setCellStyle(numberStyle);
		 } else {
		     cell43.setCellValue("");
		     cell43.setCellStyle(textStyle);
		 }

		  Cell cell44 = row.createCell(3);

		 if (record1.getR6_CURRENCY_NOSTRO() != null && !record1.getR6_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell44.setCellValue(record1.getR6_CURRENCY_NOSTRO().toString().trim() );
		     cell44.setCellStyle(numberStyle);
		 } else {
		     cell44.setCellValue("");
		     cell44.setCellStyle(textStyle);
		 }

		  Cell cell45 = row.createCell(4);

		 if (record1.getR6_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR6_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell45.setCellValue(record1.getR6_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell45.setCellStyle(numberStyle);
		 } else {
		     cell45.setCellValue("");
		     cell45.setCellStyle(textStyle);
		 }

		  Cell cell46 = row.createCell(5);

		 if (record1.getR6_RISK_WEIGHT_NOSTRO() != null && !record1.getR6_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell46.setCellValue(record1.getR6_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell46.setCellStyle(numberStyle);
		 } else {
		     cell46.setCellValue("");
		     cell46.setCellStyle(textStyle);
		 }

		  Cell cell47 = row.createCell(6);

		 if (record1.getR6_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR6_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell47.setCellValue(record1.getR6_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell47.setCellStyle(numberStyle);
		 } else {
		     cell47.setCellValue("");
		     cell47.setCellStyle(textStyle);
		 }

		  Cell cell48 = row.createCell(7);

		 if (record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell48.setCellValue(record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell48.setCellStyle(numberStyle);
		 } else {
		     cell48.setCellValue("");
		     cell48.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(16) != null ? sheet.getRow(16) : sheet.createRow(16);
		  Cell cell49 = row.createCell(0);

		 if (record1.getR7_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR7_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell49.setCellValue(record1.getR7_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell49.setCellStyle(numberStyle);
		 } else {
		     cell49.setCellValue("");
		     cell49.setCellStyle(textStyle);
		 }

		  Cell cell50 = row.createCell(1);

		 if (record1.getR7_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR7_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell50.setCellValue(record1.getR7_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell50.setCellStyle(numberStyle);
		 } else {
		     cell50.setCellValue("");
		     cell50.setCellStyle(textStyle);
		 }

		  Cell cell51 = row.createCell(2);

		 if (record1.getR7_PURPOSE_NOSTRO() != null && !record1.getR7_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell51.setCellValue(record1.getR7_PURPOSE_NOSTRO().toString().trim() );
		     cell51.setCellStyle(numberStyle);
		 } else {
		     cell51.setCellValue("");
		     cell51.setCellStyle(textStyle);
		 }

		  Cell cell52 = row.createCell(3);

		 if (record1.getR7_CURRENCY_NOSTRO() != null && !record1.getR7_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell52.setCellValue(record1.getR7_CURRENCY_NOSTRO().toString().trim() );
		     cell52.setCellStyle(numberStyle);
		 } else {
		     cell52.setCellValue("");
		     cell52.setCellStyle(textStyle);
		 }

		  Cell cell53 = row.createCell(4);

		 if (record1.getR7_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR7_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell53.setCellValue(record1.getR7_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell53.setCellStyle(numberStyle);
		 } else {
		     cell53.setCellValue("");
		     cell53.setCellStyle(textStyle);
		 }

		  Cell cell54 = row.createCell(5);

		 if (record1.getR7_RISK_WEIGHT_NOSTRO() != null && !record1.getR7_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell54.setCellValue(record1.getR7_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell54.setCellStyle(numberStyle);
		 } else {
		     cell54.setCellValue("");
		     cell54.setCellStyle(textStyle);
		 }

		  Cell cell55 = row.createCell(6);

		 if (record1.getR7_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR7_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell55.setCellValue(record1.getR7_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell55.setCellStyle(numberStyle);
		 } else {
		     cell55.setCellValue("");
		     cell55.setCellStyle(textStyle);
		 }

		  Cell cell56 = row.createCell(7);

		 if (record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell56.setCellValue(record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell56.setCellStyle(numberStyle);
		 } else {
		     cell56.setCellValue("");
		     cell56.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(17) != null ? sheet.getRow(17) : sheet.createRow(17);
		  Cell cell57 = row.createCell(0);

		 if (record1.getR8_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR8_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell57.setCellValue(record1.getR8_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell57.setCellStyle(numberStyle);
		 } else {
		     cell57.setCellValue("");
		     cell57.setCellStyle(textStyle);
		 }

		  Cell cell58 = row.createCell(1);

		 if (record1.getR8_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR8_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell58.setCellValue(record1.getR8_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell58.setCellStyle(numberStyle);
		 } else {
		     cell58.setCellValue("");
		     cell58.setCellStyle(textStyle);
		 }

		  Cell cell59 = row.createCell(2);

		 if (record1.getR8_PURPOSE_NOSTRO() != null && !record1.getR8_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell59.setCellValue(record1.getR8_PURPOSE_NOSTRO().toString().trim() );
		     cell59.setCellStyle(numberStyle);
		 } else {
		     cell59.setCellValue("");
		     cell59.setCellStyle(textStyle);
		 }

		  Cell cell60 = row.createCell(3);

		 if (record1.getR8_CURRENCY_NOSTRO() != null && !record1.getR8_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell60.setCellValue(record1.getR8_CURRENCY_NOSTRO().toString().trim() );
		     cell60.setCellStyle(numberStyle);
		 } else {
		     cell60.setCellValue("");
		     cell60.setCellStyle(textStyle);
		 }

		  Cell cell61 = row.createCell(4);

		 if (record1.getR8_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR8_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell61.setCellValue(record1.getR8_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell61.setCellStyle(numberStyle);
		 } else {
		     cell61.setCellValue("");
		     cell61.setCellStyle(textStyle);
		 }

		  Cell cell62 = row.createCell(5);

		 if (record1.getR8_RISK_WEIGHT_NOSTRO() != null && !record1.getR8_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell62.setCellValue(record1.getR8_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell62.setCellStyle(numberStyle);
		 } else {
		     cell62.setCellValue("");
		     cell62.setCellStyle(textStyle);
		 }

		  Cell cell63 = row.createCell(6);

		 if (record1.getR8_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR8_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell63.setCellValue(record1.getR8_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell63.setCellStyle(numberStyle);
		 } else {
		     cell63.setCellValue("");
		     cell63.setCellStyle(textStyle);
		 }

		  Cell cell64 = row.createCell(7);

		 if (record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell64.setCellValue(record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell64.setCellStyle(numberStyle);
		 } else {
		     cell64.setCellValue("");
		     cell64.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(18) != null ? sheet.getRow(18) : sheet.createRow(18);
		  Cell cell65 = row.createCell(0);

		 if (record1.getR9_NAME_OF_BANK_AND_COUNTRY_NOSTRO() != null && !record1.getR9_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell65.setCellValue(record1.getR9_NAME_OF_BANK_AND_COUNTRY_NOSTRO().toString().trim() );
		     cell65.setCellStyle(numberStyle);
		 } else {
		     cell65.setCellValue("");
		     cell65.setCellStyle(textStyle);
		 }

		  Cell cell66 = row.createCell(1);

		 if (record1.getR9_TYPE_OF_ACCOUNT_NOSTRO() != null && !record1.getR9_TYPE_OF_ACCOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell66.setCellValue(record1.getR9_TYPE_OF_ACCOUNT_NOSTRO().toString().trim() );
		     cell66.setCellStyle(numberStyle);
		 } else {
		     cell66.setCellValue("");
		     cell66.setCellStyle(textStyle);
		 }

		  Cell cell67 = row.createCell(2);

		 if (record1.getR9_PURPOSE_NOSTRO() != null && !record1.getR9_PURPOSE_NOSTRO().toString().trim().equals("N/A") ) {
		     cell67.setCellValue(record1.getR9_PURPOSE_NOSTRO().toString().trim() );
		     cell67.setCellStyle(numberStyle);
		 } else {
		     cell67.setCellValue("");
		     cell67.setCellStyle(textStyle);
		 }

		  Cell cell68 = row.createCell(3);

		 if (record1.getR9_CURRENCY_NOSTRO() != null && !record1.getR9_CURRENCY_NOSTRO().toString().trim().equals("N/A") ) {
		     cell68.setCellValue(record1.getR9_CURRENCY_NOSTRO().toString().trim() );
		     cell68.setCellStyle(numberStyle);
		 } else {
		     cell68.setCellValue("");
		     cell68.setCellStyle(textStyle);
		 }

		  Cell cell69 = row.createCell(4);

		 if (record1.getR9_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO() != null && !record1.getR9_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim().equals("N/A") ) {
		     cell69.setCellValue(record1.getR9_SOVEREIGN_RATING_AAA_AA_A1_NOSTRO().toString().trim() );
		     cell69.setCellStyle(numberStyle);
		 } else {
		     cell69.setCellValue("");
		     cell69.setCellStyle(textStyle);
		 }

		  Cell cell70 = row.createCell(5);

		 if (record1.getR9_RISK_WEIGHT_NOSTRO() != null && !record1.getR9_RISK_WEIGHT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell70.setCellValue(record1.getR9_RISK_WEIGHT_NOSTRO().toString().trim() );
		     cell70.setCellStyle(numberStyle);
		 } else {
		     cell70.setCellValue("");
		     cell70.setCellStyle(textStyle);
		 }

		  Cell cell71 = row.createCell(6);

		 if (record1.getR9_AMOUNT_DEMAND_NOSTRO() != null && !record1.getR9_AMOUNT_DEMAND_NOSTRO().toString().trim().equals("N/A") ) {
		     cell71.setCellValue(record1.getR9_AMOUNT_DEMAND_NOSTRO().toString().trim() );
		     cell71.setCellStyle(numberStyle);
		 } else {
		     cell71.setCellValue("");
		     cell71.setCellStyle(textStyle);
		 }

		  Cell cell72 = row.createCell(7);

		 if (record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO() != null && !record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim().equals("N/A") ) {
		     cell72.setCellValue(record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO().toString().trim() );
		     cell72.setCellStyle(numberStyle);
		 } else {
		     cell72.setCellValue("");
		     cell72.setCellStyle(textStyle);
		 }




		}
	
	private void writeEmailExcelRowData2(Sheet sheet, List<BrrsMNosvosP2> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
		
			System.out.println("came to write row data 1 method");
		
			BrrsMNosvosP2 record1 = dataList.get(0);
			
			Row  row = sheet.getRow(23) != null ? sheet.getRow(23) : sheet.createRow(23);
			 Cell cell1 = row.createCell(0);

			 if (record1.getR1_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR1_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell1.setCellValue(record1.getR1_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell1.setCellStyle(numberStyle);
			 } else {
			     cell1.setCellValue("");
			     cell1.setCellStyle(textStyle);
			 }

			  Cell cell2 = row.createCell(1);

			 if (record1.getR1_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR1_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell2.setCellValue(record1.getR1_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell2.setCellStyle(numberStyle);
			 } else {
			     cell2.setCellValue("");
			     cell2.setCellStyle(textStyle);
			 }

			  Cell cell3 = row.createCell(2);

			 if (record1.getR1_PURPOSE_VOSTRO() != null && !record1.getR1_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell3.setCellValue(record1.getR1_PURPOSE_VOSTRO().toString().trim() );
			     cell3.setCellStyle(numberStyle);
			 } else {
			     cell3.setCellValue("");
			     cell3.setCellStyle(textStyle);
			 }

			  Cell cell4 = row.createCell(3);

			 if (record1.getR1_CURRENCY_VOSTRO() != null && !record1.getR1_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell4.setCellValue(record1.getR1_CURRENCY_VOSTRO().toString().trim() );
			     cell4.setCellStyle(numberStyle);
			 } else {
			     cell4.setCellValue("");
			     cell4.setCellStyle(textStyle);
			 }

			  Cell cell7 = row.createCell(6);

			 if (record1.getR1_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR1_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell7.setCellValue(record1.getR1_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell7.setCellStyle(numberStyle);
			 } else {
			     cell7.setCellValue("");
			     cell7.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(24) != null ? sheet.getRow(24) : sheet.createRow(24);
			  Cell cell9 = row.createCell(0);

			 if (record1.getR2_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR2_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell9.setCellValue(record1.getR2_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell9.setCellStyle(numberStyle);
			 } else {
			     cell9.setCellValue("");
			     cell9.setCellStyle(textStyle);
			 }

			  Cell cell10 = row.createCell(1);

			 if (record1.getR2_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR2_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell10.setCellValue(record1.getR2_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell10.setCellStyle(numberStyle);
			 } else {
			     cell10.setCellValue("");
			     cell10.setCellStyle(textStyle);
			 }

			  Cell cell11 = row.createCell(2);

			 if (record1.getR2_PURPOSE_VOSTRO() != null && !record1.getR2_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell11.setCellValue(record1.getR2_PURPOSE_VOSTRO().toString().trim() );
			     cell11.setCellStyle(numberStyle);
			 } else {
			     cell11.setCellValue("");
			     cell11.setCellStyle(textStyle);
			 }

			  Cell cell12 = row.createCell(3);

			 if (record1.getR2_CURRENCY_VOSTRO() != null && !record1.getR2_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell12.setCellValue(record1.getR2_CURRENCY_VOSTRO().toString().trim() );
			     cell12.setCellStyle(numberStyle);
			 } else {
			     cell12.setCellValue("");
			     cell12.setCellStyle(textStyle);
			 }

			  Cell cell15 = row.createCell(6);

			 if (record1.getR2_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR2_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell15.setCellValue(record1.getR2_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell15.setCellStyle(numberStyle);
			 } else {
			     cell15.setCellValue("");
			     cell15.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(25) != null ? sheet.getRow(25) : sheet.createRow(25);
			  Cell cell17 = row.createCell(0);

			 if (record1.getR3_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR3_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell17.setCellValue(record1.getR3_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell17.setCellStyle(numberStyle);
			 } else {
			     cell17.setCellValue("");
			     cell17.setCellStyle(textStyle);
			 }

			  Cell cell18 = row.createCell(1);

			 if (record1.getR3_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR3_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell18.setCellValue(record1.getR3_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell18.setCellStyle(numberStyle);
			 } else {
			     cell18.setCellValue("");
			     cell18.setCellStyle(textStyle);
			 }

			  Cell cell19 = row.createCell(2);

			 if (record1.getR3_PURPOSE_VOSTRO() != null && !record1.getR3_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell19.setCellValue(record1.getR3_PURPOSE_VOSTRO().toString().trim() );
			     cell19.setCellStyle(numberStyle);
			 } else {
			     cell19.setCellValue("");
			     cell19.setCellStyle(textStyle);
			 }

			  Cell cell20 = row.createCell(3);

			 if (record1.getR3_CURRENCY_VOSTRO() != null && !record1.getR3_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell20.setCellValue(record1.getR3_CURRENCY_VOSTRO().toString().trim() );
			     cell20.setCellStyle(numberStyle);
			 } else {
			     cell20.setCellValue("");
			     cell20.setCellStyle(textStyle);
			 }

			  Cell cell23 = row.createCell(6);

			 if (record1.getR3_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR3_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell23.setCellValue(record1.getR3_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell23.setCellStyle(numberStyle);
			 } else {
			     cell23.setCellValue("");
			     cell23.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(26) != null ? sheet.getRow(26) : sheet.createRow(26);
			  Cell cell25 = row.createCell(0);

			 if (record1.getR4_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR4_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell25.setCellValue(record1.getR4_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell25.setCellStyle(numberStyle);
			 } else {
			     cell25.setCellValue("");
			     cell25.setCellStyle(textStyle);
			 }

			  Cell cell26 = row.createCell(1);

			 if (record1.getR4_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR4_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell26.setCellValue(record1.getR4_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell26.setCellStyle(numberStyle);
			 } else {
			     cell26.setCellValue("");
			     cell26.setCellStyle(textStyle);
			 }

			  Cell cell27 = row.createCell(2);

			 if (record1.getR4_PURPOSE_VOSTRO() != null && !record1.getR4_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell27.setCellValue(record1.getR4_PURPOSE_VOSTRO().toString().trim() );
			     cell27.setCellStyle(numberStyle);
			 } else {
			     cell27.setCellValue("");
			     cell27.setCellStyle(textStyle);
			 }

			  Cell cell28 = row.createCell(3);

			 if (record1.getR4_CURRENCY_VOSTRO() != null && !record1.getR4_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell28.setCellValue(record1.getR4_CURRENCY_VOSTRO().toString().trim() );
			     cell28.setCellStyle(numberStyle);
			 } else {
			     cell28.setCellValue("");
			     cell28.setCellStyle(textStyle);
			 }

			  Cell cell31 = row.createCell(6);

			 if (record1.getR4_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR4_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell31.setCellValue(record1.getR4_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell31.setCellStyle(numberStyle);
			 } else {
			     cell31.setCellValue("");
			     cell31.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(27) != null ? sheet.getRow(27) : sheet.createRow(27);
			  Cell cell33 = row.createCell(0);

			 if (record1.getR5_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR5_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell33.setCellValue(record1.getR5_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell33.setCellStyle(numberStyle);
			 } else {
			     cell33.setCellValue("");
			     cell33.setCellStyle(textStyle);
			 }

			  Cell cell34 = row.createCell(1);

			 if (record1.getR5_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR5_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell34.setCellValue(record1.getR5_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell34.setCellStyle(numberStyle);
			 } else {
			     cell34.setCellValue("");
			     cell34.setCellStyle(textStyle);
			 }

			  Cell cell35 = row.createCell(2);

			 if (record1.getR5_PURPOSE_VOSTRO() != null && !record1.getR5_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell35.setCellValue(record1.getR5_PURPOSE_VOSTRO().toString().trim() );
			     cell35.setCellStyle(numberStyle);
			 } else {
			     cell35.setCellValue("");
			     cell35.setCellStyle(textStyle);
			 }

			  Cell cell36 = row.createCell(3);

			 if (record1.getR5_CURRENCY_VOSTRO() != null && !record1.getR5_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell36.setCellValue(record1.getR5_CURRENCY_VOSTRO().toString().trim() );
			     cell36.setCellStyle(numberStyle);
			 } else {
			     cell36.setCellValue("");
			     cell36.setCellStyle(textStyle);
			 }

			  Cell cell39 = row.createCell(6);

			 if (record1.getR5_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR5_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell39.setCellValue(record1.getR5_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell39.setCellStyle(numberStyle);
			 } else {
			     cell39.setCellValue("");
			     cell39.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(28) != null ? sheet.getRow(28) : sheet.createRow(28);
			  Cell cell41 = row.createCell(0);

			 if (record1.getR6_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR6_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell41.setCellValue(record1.getR6_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell41.setCellStyle(numberStyle);
			 } else {
			     cell41.setCellValue("");
			     cell41.setCellStyle(textStyle);
			 }

			  Cell cell42 = row.createCell(1);

			 if (record1.getR6_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR6_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell42.setCellValue(record1.getR6_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell42.setCellStyle(numberStyle);
			 } else {
			     cell42.setCellValue("");
			     cell42.setCellStyle(textStyle);
			 }

			  Cell cell43 = row.createCell(2);

			 if (record1.getR6_PURPOSE_VOSTRO() != null && !record1.getR6_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell43.setCellValue(record1.getR6_PURPOSE_VOSTRO().toString().trim() );
			     cell43.setCellStyle(numberStyle);
			 } else {
			     cell43.setCellValue("");
			     cell43.setCellStyle(textStyle);
			 }

			  Cell cell44 = row.createCell(3);

			 if (record1.getR6_CURRENCY_VOSTRO() != null && !record1.getR6_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell44.setCellValue(record1.getR6_CURRENCY_VOSTRO().toString().trim() );
			     cell44.setCellStyle(numberStyle);
			 } else {
			     cell44.setCellValue("");
			     cell44.setCellStyle(textStyle);
			 }

			  Cell cell47 = row.createCell(6);

			 if (record1.getR6_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR6_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell47.setCellValue(record1.getR6_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell47.setCellStyle(numberStyle);
			 } else {
			     cell47.setCellValue("");
			     cell47.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(29) != null ? sheet.getRow(29) : sheet.createRow(29);
			  Cell cell49 = row.createCell(0);

			 if (record1.getR7_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR7_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell49.setCellValue(record1.getR7_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell49.setCellStyle(numberStyle);
			 } else {
			     cell49.setCellValue("");
			     cell49.setCellStyle(textStyle);
			 }

			  Cell cell50 = row.createCell(1);

			 if (record1.getR7_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR7_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell50.setCellValue(record1.getR7_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell50.setCellStyle(numberStyle);
			 } else {
			     cell50.setCellValue("");
			     cell50.setCellStyle(textStyle);
			 }

			  Cell cell51 = row.createCell(2);

			 if (record1.getR7_PURPOSE_VOSTRO() != null && !record1.getR7_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell51.setCellValue(record1.getR7_PURPOSE_VOSTRO().toString().trim() );
			     cell51.setCellStyle(numberStyle);
			 } else {
			     cell51.setCellValue("");
			     cell51.setCellStyle(textStyle);
			 }

			  Cell cell52 = row.createCell(3);

			 if (record1.getR7_CURRENCY_VOSTRO() != null && !record1.getR7_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell52.setCellValue(record1.getR7_CURRENCY_VOSTRO().toString().trim() );
			     cell52.setCellStyle(numberStyle);
			 } else {
			     cell52.setCellValue("");
			     cell52.setCellStyle(textStyle);
			 }

			  Cell cell55 = row.createCell(6);

			 if (record1.getR7_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR7_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell55.setCellValue(record1.getR7_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell55.setCellStyle(numberStyle);
			 } else {
			     cell55.setCellValue("");
			     cell55.setCellStyle(textStyle);
			 }


			  row = sheet.getRow(30) != null ? sheet.getRow(30) : sheet.createRow(30);
			  Cell cell57 = row.createCell(0);

			 if (record1.getR8_NAME_OF_BANK_AND_COUNTRY_VOSTRO() != null && !record1.getR8_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell57.setCellValue(record1.getR8_NAME_OF_BANK_AND_COUNTRY_VOSTRO().toString().trim() );
			     cell57.setCellStyle(numberStyle);
			 } else {
			     cell57.setCellValue("");
			     cell57.setCellStyle(textStyle);
			 }

			  Cell cell58 = row.createCell(1);

			 if (record1.getR8_TYPE_OF_ACCOUNT_VOSTRO() != null && !record1.getR8_TYPE_OF_ACCOUNT_VOSTRO().toString().trim().equals("N/A") ) {
			     cell58.setCellValue(record1.getR8_TYPE_OF_ACCOUNT_VOSTRO().toString().trim() );
			     cell58.setCellStyle(numberStyle);
			 } else {
			     cell58.setCellValue("");
			     cell58.setCellStyle(textStyle);
			 }

			  Cell cell59 = row.createCell(2);

			 if (record1.getR8_PURPOSE_VOSTRO() != null && !record1.getR8_PURPOSE_VOSTRO().toString().trim().equals("N/A") ) {
			     cell59.setCellValue(record1.getR8_PURPOSE_VOSTRO().toString().trim() );
			     cell59.setCellStyle(numberStyle);
			 } else {
			     cell59.setCellValue("");
			     cell59.setCellStyle(textStyle);
			 }

			  Cell cell60 = row.createCell(3);

			 if (record1.getR8_CURRENCY_VOSTRO() != null && !record1.getR8_CURRENCY_VOSTRO().toString().trim().equals("N/A") ) {
			     cell60.setCellValue(record1.getR8_CURRENCY_VOSTRO().toString().trim() );
			     cell60.setCellStyle(numberStyle);
			 } else {
			     cell60.setCellValue("");
			     cell60.setCellStyle(textStyle);
			 }

			  Cell cell63 = row.createCell(6);

			 if (record1.getR8_AMOUNT_DEMAND_VOSTRO() != null && !record1.getR8_AMOUNT_DEMAND_VOSTRO().toString().trim().equals("N/A") ) {
			     cell63.setCellValue(record1.getR8_AMOUNT_DEMAND_VOSTRO().toString().trim() );
			     cell63.setCellStyle(numberStyle);
			 } else {
			     cell63.setCellValue("");
			     cell63.setCellStyle(textStyle);
			 }



			
		}
	
	private void writeEmailExcelRowData3(Sheet sheet, List<BrrsMNosvosP3> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			
		System.out.println("came to write row data 1 method");
		
		BrrsMNosvosP3 record1 = dataList.get(0);
		
		Row  row = sheet.getRow(36) != null ? sheet.getRow(36) : sheet.createRow(36);
		 Cell cell1 = row.createCell(0);

		 if (record1.getR1_NAME_OF_BANK_NOSTRO1() != null && !record1.getR1_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell1.setCellValue(record1.getR1_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell1.setCellStyle(numberStyle);
		 } else {
		     cell1.setCellValue("");
		     cell1.setCellStyle(textStyle);
		 }

		  Cell cell2 = row.createCell(1);

		 if (record1.getR1_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR1_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell2.setCellValue(record1.getR1_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell2.setCellStyle(numberStyle);
		 } else {
		     cell2.setCellValue("");
		     cell2.setCellStyle(textStyle);
		 }

		  Cell cell3 = row.createCell(2);

		 if (record1.getR1_PURPOSE_NOSTRO1() != null && !record1.getR1_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell3.setCellValue(record1.getR1_PURPOSE_NOSTRO1().toString().trim() );
		     cell3.setCellStyle(numberStyle);
		 } else {
		     cell3.setCellValue("");
		     cell3.setCellStyle(textStyle);
		 }

		  Cell cell4 = row.createCell(3);

		 if (record1.getR1_CURRENCY_NOSTRO1() != null && !record1.getR1_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell4.setCellValue(record1.getR1_CURRENCY_NOSTRO1().toString().trim() );
		     cell4.setCellStyle(numberStyle);
		 } else {
		     cell4.setCellValue("");
		     cell4.setCellStyle(textStyle);
		 }

		  Cell cell5 = row.createCell(4);

		 if (record1.getR1_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR1_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell5.setCellValue(record1.getR1_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell5.setCellStyle(numberStyle);
		 } else {
		     cell5.setCellValue("");
		     cell5.setCellStyle(textStyle);
		 }

		  Cell cell6 = row.createCell(5);

		 if (record1.getR1_RISK_WEIGHT_NOSTRO1() != null && !record1.getR1_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell6.setCellValue(record1.getR1_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell6.setCellStyle(numberStyle);
		 } else {
		     cell6.setCellValue("");
		     cell6.setCellStyle(textStyle);
		 }

		  Cell cell7 = row.createCell(6);

		 if (record1.getR1_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR1_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell7.setCellValue(record1.getR1_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell7.setCellStyle(numberStyle);
		 } else {
		     cell7.setCellValue("");
		     cell7.setCellStyle(textStyle);
		 }

		  Cell cell8 = row.createCell(7);

		 if (record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell8.setCellValue(record1.getR1_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell8.setCellStyle(numberStyle);
		 } else {
		     cell8.setCellValue("");
		     cell8.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(37) != null ? sheet.getRow(37) : sheet.createRow(37);
		  Cell cell9 = row.createCell(0);

		 if (record1.getR2_NAME_OF_BANK_NOSTRO1() != null && !record1.getR2_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell9.setCellValue(record1.getR2_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell9.setCellStyle(numberStyle);
		 } else {
		     cell9.setCellValue("");
		     cell9.setCellStyle(textStyle);
		 }

		  Cell cell10 = row.createCell(1);

		 if (record1.getR2_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR2_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell10.setCellValue(record1.getR2_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell10.setCellStyle(numberStyle);
		 } else {
		     cell10.setCellValue("");
		     cell10.setCellStyle(textStyle);
		 }

		  Cell cell11 = row.createCell(2);

		 if (record1.getR2_PURPOSE_NOSTRO1() != null && !record1.getR2_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell11.setCellValue(record1.getR2_PURPOSE_NOSTRO1().toString().trim() );
		     cell11.setCellStyle(numberStyle);
		 } else {
		     cell11.setCellValue("");
		     cell11.setCellStyle(textStyle);
		 }

		  Cell cell12 = row.createCell(3);

		 if (record1.getR2_CURRENCY_NOSTRO1() != null && !record1.getR2_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell12.setCellValue(record1.getR2_CURRENCY_NOSTRO1().toString().trim() );
		     cell12.setCellStyle(numberStyle);
		 } else {
		     cell12.setCellValue("");
		     cell12.setCellStyle(textStyle);
		 }

		  Cell cell13 = row.createCell(4);

		 if (record1.getR2_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR2_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell13.setCellValue(record1.getR2_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell13.setCellStyle(numberStyle);
		 } else {
		     cell13.setCellValue("");
		     cell13.setCellStyle(textStyle);
		 }

		  Cell cell14 = row.createCell(5);

		 if (record1.getR2_RISK_WEIGHT_NOSTRO1() != null && !record1.getR2_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell14.setCellValue(record1.getR2_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell14.setCellStyle(numberStyle);
		 } else {
		     cell14.setCellValue("");
		     cell14.setCellStyle(textStyle);
		 }

		  Cell cell15 = row.createCell(6);

		 if (record1.getR2_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR2_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell15.setCellValue(record1.getR2_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell15.setCellStyle(numberStyle);
		 } else {
		     cell15.setCellValue("");
		     cell15.setCellStyle(textStyle);
		 }

		  Cell cell16 = row.createCell(7);

		 if (record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell16.setCellValue(record1.getR2_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell16.setCellStyle(numberStyle);
		 } else {
		     cell16.setCellValue("");
		     cell16.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(38) != null ? sheet.getRow(38) : sheet.createRow(38);
		  Cell cell17 = row.createCell(0);

		 if (record1.getR3_NAME_OF_BANK_NOSTRO1() != null && !record1.getR3_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell17.setCellValue(record1.getR3_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell17.setCellStyle(numberStyle);
		 } else {
		     cell17.setCellValue("");
		     cell17.setCellStyle(textStyle);
		 }

		  Cell cell18 = row.createCell(1);

		 if (record1.getR3_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR3_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell18.setCellValue(record1.getR3_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell18.setCellStyle(numberStyle);
		 } else {
		     cell18.setCellValue("");
		     cell18.setCellStyle(textStyle);
		 }

		  Cell cell19 = row.createCell(2);

		 if (record1.getR3_PURPOSE_NOSTRO1() != null && !record1.getR3_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell19.setCellValue(record1.getR3_PURPOSE_NOSTRO1().toString().trim() );
		     cell19.setCellStyle(numberStyle);
		 } else {
		     cell19.setCellValue("");
		     cell19.setCellStyle(textStyle);
		 }

		  Cell cell20 = row.createCell(3);

		 if (record1.getR3_CURRENCY_NOSTRO1() != null && !record1.getR3_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell20.setCellValue(record1.getR3_CURRENCY_NOSTRO1().toString().trim() );
		     cell20.setCellStyle(numberStyle);
		 } else {
		     cell20.setCellValue("");
		     cell20.setCellStyle(textStyle);
		 }

		  Cell cell21 = row.createCell(4);

		 if (record1.getR3_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR3_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell21.setCellValue(record1.getR3_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell21.setCellStyle(numberStyle);
		 } else {
		     cell21.setCellValue("");
		     cell21.setCellStyle(textStyle);
		 }

		  Cell cell22 = row.createCell(5);

		 if (record1.getR3_RISK_WEIGHT_NOSTRO1() != null && !record1.getR3_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell22.setCellValue(record1.getR3_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell22.setCellStyle(numberStyle);
		 } else {
		     cell22.setCellValue("");
		     cell22.setCellStyle(textStyle);
		 }

		  Cell cell23 = row.createCell(6);

		 if (record1.getR3_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR3_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell23.setCellValue(record1.getR3_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell23.setCellStyle(numberStyle);
		 } else {
		     cell23.setCellValue("");
		     cell23.setCellStyle(textStyle);
		 }

		  Cell cell24 = row.createCell(7);

		 if (record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell24.setCellValue(record1.getR3_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell24.setCellStyle(numberStyle);
		 } else {
		     cell24.setCellValue("");
		     cell24.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(39) != null ? sheet.getRow(39) : sheet.createRow(39);
		  Cell cell25 = row.createCell(0);

		 if (record1.getR4_NAME_OF_BANK_NOSTRO1() != null && !record1.getR4_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell25.setCellValue(record1.getR4_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell25.setCellStyle(numberStyle);
		 } else {
		     cell25.setCellValue("");
		     cell25.setCellStyle(textStyle);
		 }

		  Cell cell26 = row.createCell(1);

		 if (record1.getR4_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR4_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell26.setCellValue(record1.getR4_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell26.setCellStyle(numberStyle);
		 } else {
		     cell26.setCellValue("");
		     cell26.setCellStyle(textStyle);
		 }

		  Cell cell27 = row.createCell(2);

		 if (record1.getR4_PURPOSE_NOSTRO1() != null && !record1.getR4_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell27.setCellValue(record1.getR4_PURPOSE_NOSTRO1().toString().trim() );
		     cell27.setCellStyle(numberStyle);
		 } else {
		     cell27.setCellValue("");
		     cell27.setCellStyle(textStyle);
		 }

		  Cell cell28 = row.createCell(3);

		 if (record1.getR4_CURRENCY_NOSTRO1() != null && !record1.getR4_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell28.setCellValue(record1.getR4_CURRENCY_NOSTRO1().toString().trim() );
		     cell28.setCellStyle(numberStyle);
		 } else {
		     cell28.setCellValue("");
		     cell28.setCellStyle(textStyle);
		 }

		  Cell cell29 = row.createCell(4);

		 if (record1.getR4_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR4_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell29.setCellValue(record1.getR4_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell29.setCellStyle(numberStyle);
		 } else {
		     cell29.setCellValue("");
		     cell29.setCellStyle(textStyle);
		 }

		  Cell cell30 = row.createCell(5);

		 if (record1.getR4_RISK_WEIGHT_NOSTRO1() != null && !record1.getR4_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell30.setCellValue(record1.getR4_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell30.setCellStyle(numberStyle);
		 } else {
		     cell30.setCellValue("");
		     cell30.setCellStyle(textStyle);
		 }

		  Cell cell31 = row.createCell(6);

		 if (record1.getR4_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR4_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell31.setCellValue(record1.getR4_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell31.setCellStyle(numberStyle);
		 } else {
		     cell31.setCellValue("");
		     cell31.setCellStyle(textStyle);
		 }

		  Cell cell32 = row.createCell(7);

		 if (record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell32.setCellValue(record1.getR4_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell32.setCellStyle(numberStyle);
		 } else {
		     cell32.setCellValue("");
		     cell32.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(40) != null ? sheet.getRow(40) : sheet.createRow(40);
		  Cell cell33 = row.createCell(0);

		 if (record1.getR5_NAME_OF_BANK_NOSTRO1() != null && !record1.getR5_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell33.setCellValue(record1.getR5_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell33.setCellStyle(numberStyle);
		 } else {
		     cell33.setCellValue("");
		     cell33.setCellStyle(textStyle);
		 }

		  Cell cell34 = row.createCell(1);

		 if (record1.getR5_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR5_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell34.setCellValue(record1.getR5_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell34.setCellStyle(numberStyle);
		 } else {
		     cell34.setCellValue("");
		     cell34.setCellStyle(textStyle);
		 }

		  Cell cell35 = row.createCell(2);

		 if (record1.getR5_PURPOSE_NOSTRO1() != null && !record1.getR5_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell35.setCellValue(record1.getR5_PURPOSE_NOSTRO1().toString().trim() );
		     cell35.setCellStyle(numberStyle);
		 } else {
		     cell35.setCellValue("");
		     cell35.setCellStyle(textStyle);
		 }

		  Cell cell36 = row.createCell(3);

		 if (record1.getR5_CURRENCY_NOSTRO1() != null && !record1.getR5_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell36.setCellValue(record1.getR5_CURRENCY_NOSTRO1().toString().trim() );
		     cell36.setCellStyle(numberStyle);
		 } else {
		     cell36.setCellValue("");
		     cell36.setCellStyle(textStyle);
		 }

		  Cell cell37 = row.createCell(4);

		 if (record1.getR5_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR5_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell37.setCellValue(record1.getR5_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell37.setCellStyle(numberStyle);
		 } else {
		     cell37.setCellValue("");
		     cell37.setCellStyle(textStyle);
		 }

		  Cell cell38 = row.createCell(5);

		 if (record1.getR5_RISK_WEIGHT_NOSTRO1() != null && !record1.getR5_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell38.setCellValue(record1.getR5_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell38.setCellStyle(numberStyle);
		 } else {
		     cell38.setCellValue("");
		     cell38.setCellStyle(textStyle);
		 }

		  Cell cell39 = row.createCell(6);

		 if (record1.getR5_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR5_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell39.setCellValue(record1.getR5_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell39.setCellStyle(numberStyle);
		 } else {
		     cell39.setCellValue("");
		     cell39.setCellStyle(textStyle);
		 }

		  Cell cell40 = row.createCell(7);

		 if (record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell40.setCellValue(record1.getR5_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell40.setCellStyle(numberStyle);
		 } else {
		     cell40.setCellValue("");
		     cell40.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(41) != null ? sheet.getRow(41) : sheet.createRow(41);
		  Cell cell41 = row.createCell(0);

		 if (record1.getR6_NAME_OF_BANK_NOSTRO1() != null && !record1.getR6_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell41.setCellValue(record1.getR6_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell41.setCellStyle(numberStyle);
		 } else {
		     cell41.setCellValue("");
		     cell41.setCellStyle(textStyle);
		 }

		  Cell cell42 = row.createCell(1);

		 if (record1.getR6_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR6_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell42.setCellValue(record1.getR6_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell42.setCellStyle(numberStyle);
		 } else {
		     cell42.setCellValue("");
		     cell42.setCellStyle(textStyle);
		 }

		  Cell cell43 = row.createCell(2);

		 if (record1.getR6_PURPOSE_NOSTRO1() != null && !record1.getR6_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell43.setCellValue(record1.getR6_PURPOSE_NOSTRO1().toString().trim() );
		     cell43.setCellStyle(numberStyle);
		 } else {
		     cell43.setCellValue("");
		     cell43.setCellStyle(textStyle);
		 }

		  Cell cell44 = row.createCell(3);

		 if (record1.getR6_CURRENCY_NOSTRO1() != null && !record1.getR6_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell44.setCellValue(record1.getR6_CURRENCY_NOSTRO1().toString().trim() );
		     cell44.setCellStyle(numberStyle);
		 } else {
		     cell44.setCellValue("");
		     cell44.setCellStyle(textStyle);
		 }

		  Cell cell45 = row.createCell(4);

		 if (record1.getR6_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR6_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell45.setCellValue(record1.getR6_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell45.setCellStyle(numberStyle);
		 } else {
		     cell45.setCellValue("");
		     cell45.setCellStyle(textStyle);
		 }

		  Cell cell46 = row.createCell(5);

		 if (record1.getR6_RISK_WEIGHT_NOSTRO1() != null && !record1.getR6_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell46.setCellValue(record1.getR6_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell46.setCellStyle(numberStyle);
		 } else {
		     cell46.setCellValue("");
		     cell46.setCellStyle(textStyle);
		 }

		  Cell cell47 = row.createCell(6);

		 if (record1.getR6_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR6_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell47.setCellValue(record1.getR6_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell47.setCellStyle(numberStyle);
		 } else {
		     cell47.setCellValue("");
		     cell47.setCellStyle(textStyle);
		 }

		  Cell cell48 = row.createCell(7);

		 if (record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell48.setCellValue(record1.getR6_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell48.setCellStyle(numberStyle);
		 } else {
		     cell48.setCellValue("");
		     cell48.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(42) != null ? sheet.getRow(42) : sheet.createRow(42);
		  Cell cell49 = row.createCell(0);

		 if (record1.getR7_NAME_OF_BANK_NOSTRO1() != null && !record1.getR7_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell49.setCellValue(record1.getR7_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell49.setCellStyle(numberStyle);
		 } else {
		     cell49.setCellValue("");
		     cell49.setCellStyle(textStyle);
		 }

		  Cell cell50 = row.createCell(1);

		 if (record1.getR7_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR7_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell50.setCellValue(record1.getR7_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell50.setCellStyle(numberStyle);
		 } else {
		     cell50.setCellValue("");
		     cell50.setCellStyle(textStyle);
		 }

		  Cell cell51 = row.createCell(2);

		 if (record1.getR7_PURPOSE_NOSTRO1() != null && !record1.getR7_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell51.setCellValue(record1.getR7_PURPOSE_NOSTRO1().toString().trim() );
		     cell51.setCellStyle(numberStyle);
		 } else {
		     cell51.setCellValue("");
		     cell51.setCellStyle(textStyle);
		 }

		  Cell cell52 = row.createCell(3);

		 if (record1.getR7_CURRENCY_NOSTRO1() != null && !record1.getR7_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell52.setCellValue(record1.getR7_CURRENCY_NOSTRO1().toString().trim() );
		     cell52.setCellStyle(numberStyle);
		 } else {
		     cell52.setCellValue("");
		     cell52.setCellStyle(textStyle);
		 }

		  Cell cell53 = row.createCell(4);

		 if (record1.getR7_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR7_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell53.setCellValue(record1.getR7_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell53.setCellStyle(numberStyle);
		 } else {
		     cell53.setCellValue("");
		     cell53.setCellStyle(textStyle);
		 }

		  Cell cell54 = row.createCell(5);

		 if (record1.getR7_RISK_WEIGHT_NOSTRO1() != null && !record1.getR7_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell54.setCellValue(record1.getR7_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell54.setCellStyle(numberStyle);
		 } else {
		     cell54.setCellValue("");
		     cell54.setCellStyle(textStyle);
		 }

		  Cell cell55 = row.createCell(6);

		 if (record1.getR7_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR7_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell55.setCellValue(record1.getR7_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell55.setCellStyle(numberStyle);
		 } else {
		     cell55.setCellValue("");
		     cell55.setCellStyle(textStyle);
		 }

		  Cell cell56 = row.createCell(7);

		 if (record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell56.setCellValue(record1.getR7_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell56.setCellStyle(numberStyle);
		 } else {
		     cell56.setCellValue("");
		     cell56.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(43) != null ? sheet.getRow(43) : sheet.createRow(43);
		  Cell cell57 = row.createCell(0);

		 if (record1.getR8_NAME_OF_BANK_NOSTRO1() != null && !record1.getR8_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell57.setCellValue(record1.getR8_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell57.setCellStyle(numberStyle);
		 } else {
		     cell57.setCellValue("");
		     cell57.setCellStyle(textStyle);
		 }

		  Cell cell58 = row.createCell(1);

		 if (record1.getR8_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR8_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell58.setCellValue(record1.getR8_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell58.setCellStyle(numberStyle);
		 } else {
		     cell58.setCellValue("");
		     cell58.setCellStyle(textStyle);
		 }

		  Cell cell59 = row.createCell(2);

		 if (record1.getR8_PURPOSE_NOSTRO1() != null && !record1.getR8_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell59.setCellValue(record1.getR8_PURPOSE_NOSTRO1().toString().trim() );
		     cell59.setCellStyle(numberStyle);
		 } else {
		     cell59.setCellValue("");
		     cell59.setCellStyle(textStyle);
		 }

		  Cell cell60 = row.createCell(3);

		 if (record1.getR8_CURRENCY_NOSTRO1() != null && !record1.getR8_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell60.setCellValue(record1.getR8_CURRENCY_NOSTRO1().toString().trim() );
		     cell60.setCellStyle(numberStyle);
		 } else {
		     cell60.setCellValue("");
		     cell60.setCellStyle(textStyle);
		 }

		  Cell cell61 = row.createCell(4);

		 if (record1.getR8_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR8_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell61.setCellValue(record1.getR8_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell61.setCellStyle(numberStyle);
		 } else {
		     cell61.setCellValue("");
		     cell61.setCellStyle(textStyle);
		 }

		  Cell cell62 = row.createCell(5);

		 if (record1.getR8_RISK_WEIGHT_NOSTRO1() != null && !record1.getR8_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell62.setCellValue(record1.getR8_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell62.setCellStyle(numberStyle);
		 } else {
		     cell62.setCellValue("");
		     cell62.setCellStyle(textStyle);
		 }

		  Cell cell63 = row.createCell(6);

		 if (record1.getR8_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR8_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell63.setCellValue(record1.getR8_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell63.setCellStyle(numberStyle);
		 } else {
		     cell63.setCellValue("");
		     cell63.setCellStyle(textStyle);
		 }

		  Cell cell64 = row.createCell(7);

		 if (record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell64.setCellValue(record1.getR8_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell64.setCellStyle(numberStyle);
		 } else {
		     cell64.setCellValue("");
		     cell64.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(44) != null ? sheet.getRow(44) : sheet.createRow(44);
		  Cell cell65 = row.createCell(0);

		 if (record1.getR9_NAME_OF_BANK_NOSTRO1() != null && !record1.getR9_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell65.setCellValue(record1.getR9_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell65.setCellStyle(numberStyle);
		 } else {
		     cell65.setCellValue("");
		     cell65.setCellStyle(textStyle);
		 }

		  Cell cell66 = row.createCell(1);

		 if (record1.getR9_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR9_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell66.setCellValue(record1.getR9_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell66.setCellStyle(numberStyle);
		 } else {
		     cell66.setCellValue("");
		     cell66.setCellStyle(textStyle);
		 }

		  Cell cell67 = row.createCell(2);

		 if (record1.getR9_PURPOSE_NOSTRO1() != null && !record1.getR9_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell67.setCellValue(record1.getR9_PURPOSE_NOSTRO1().toString().trim() );
		     cell67.setCellStyle(numberStyle);
		 } else {
		     cell67.setCellValue("");
		     cell67.setCellStyle(textStyle);
		 }

		  Cell cell68 = row.createCell(3);

		 if (record1.getR9_CURRENCY_NOSTRO1() != null && !record1.getR9_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell68.setCellValue(record1.getR9_CURRENCY_NOSTRO1().toString().trim() );
		     cell68.setCellStyle(numberStyle);
		 } else {
		     cell68.setCellValue("");
		     cell68.setCellStyle(textStyle);
		 }

		  Cell cell69 = row.createCell(4);

		 if (record1.getR9_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR9_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell69.setCellValue(record1.getR9_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell69.setCellStyle(numberStyle);
		 } else {
		     cell69.setCellValue("");
		     cell69.setCellStyle(textStyle);
		 }

		  Cell cell70 = row.createCell(5);

		 if (record1.getR9_RISK_WEIGHT_NOSTRO1() != null && !record1.getR9_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell70.setCellValue(record1.getR9_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell70.setCellStyle(numberStyle);
		 } else {
		     cell70.setCellValue("");
		     cell70.setCellStyle(textStyle);
		 }

		  Cell cell71 = row.createCell(6);

		 if (record1.getR9_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR9_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell71.setCellValue(record1.getR9_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell71.setCellStyle(numberStyle);
		 } else {
		     cell71.setCellValue("");
		     cell71.setCellStyle(textStyle);
		 }

		  Cell cell72 = row.createCell(7);

		 if (record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell72.setCellValue(record1.getR9_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell72.setCellStyle(numberStyle);
		 } else {
		     cell72.setCellValue("");
		     cell72.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(45) != null ? sheet.getRow(45) : sheet.createRow(45);
		  Cell cell73 = row.createCell(0);

		 if (record1.getR10_NAME_OF_BANK_NOSTRO1() != null && !record1.getR10_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell73.setCellValue(record1.getR10_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell73.setCellStyle(numberStyle);
		 } else {
		     cell73.setCellValue("");
		     cell73.setCellStyle(textStyle);
		 }

		  Cell cell74 = row.createCell(1);

		 if (record1.getR10_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR10_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell74.setCellValue(record1.getR10_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell74.setCellStyle(numberStyle);
		 } else {
		     cell74.setCellValue("");
		     cell74.setCellStyle(textStyle);
		 }

		  Cell cell75 = row.createCell(2);

		 if (record1.getR10_PURPOSE_NOSTRO1() != null && !record1.getR10_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell75.setCellValue(record1.getR10_PURPOSE_NOSTRO1().toString().trim() );
		     cell75.setCellStyle(numberStyle);
		 } else {
		     cell75.setCellValue("");
		     cell75.setCellStyle(textStyle);
		 }

		  Cell cell76 = row.createCell(3);

		 if (record1.getR10_CURRENCY_NOSTRO1() != null && !record1.getR10_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell76.setCellValue(record1.getR10_CURRENCY_NOSTRO1().toString().trim() );
		     cell76.setCellStyle(numberStyle);
		 } else {
		     cell76.setCellValue("");
		     cell76.setCellStyle(textStyle);
		 }

		  Cell cell77 = row.createCell(4);

		 if (record1.getR10_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR10_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell77.setCellValue(record1.getR10_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell77.setCellStyle(numberStyle);
		 } else {
		     cell77.setCellValue("");
		     cell77.setCellStyle(textStyle);
		 }

		  Cell cell78 = row.createCell(5);

		 if (record1.getR10_RISK_WEIGHT_NOSTRO1() != null && !record1.getR10_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell78.setCellValue(record1.getR10_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell78.setCellStyle(numberStyle);
		 } else {
		     cell78.setCellValue("");
		     cell78.setCellStyle(textStyle);
		 }

		  Cell cell79 = row.createCell(6);

		 if (record1.getR10_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR10_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell79.setCellValue(record1.getR10_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell79.setCellStyle(numberStyle);
		 } else {
		     cell79.setCellValue("");
		     cell79.setCellStyle(textStyle);
		 }

		  Cell cell80 = row.createCell(7);

		 if (record1.getR10_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR10_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell80.setCellValue(record1.getR10_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell80.setCellStyle(numberStyle);
		 } else {
		     cell80.setCellValue("");
		     cell80.setCellStyle(textStyle);
		 }


		  row = sheet.getRow(46) != null ? sheet.getRow(46) : sheet.createRow(46);
		  Cell cell81 = row.createCell(0);

		 if (record1.getR11_NAME_OF_BANK_NOSTRO1() != null && !record1.getR11_NAME_OF_BANK_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell81.setCellValue(record1.getR11_NAME_OF_BANK_NOSTRO1().toString().trim() );
		     cell81.setCellStyle(numberStyle);
		 } else {
		     cell81.setCellValue("");
		     cell81.setCellStyle(textStyle);
		 }

		  Cell cell82 = row.createCell(1);

		 if (record1.getR11_TYPE_OF_ACCOUNT_NOSTRO1() != null && !record1.getR11_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell82.setCellValue(record1.getR11_TYPE_OF_ACCOUNT_NOSTRO1().toString().trim() );
		     cell82.setCellStyle(numberStyle);
		 } else {
		     cell82.setCellValue("");
		     cell82.setCellStyle(textStyle);
		 }

		  Cell cell83 = row.createCell(2);

		 if (record1.getR11_PURPOSE_NOSTRO1() != null && !record1.getR11_PURPOSE_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell83.setCellValue(record1.getR11_PURPOSE_NOSTRO1().toString().trim() );
		     cell83.setCellStyle(numberStyle);
		 } else {
		     cell83.setCellValue("");
		     cell83.setCellStyle(textStyle);
		 }

		  Cell cell84 = row.createCell(3);

		 if (record1.getR11_CURRENCY_NOSTRO1() != null && !record1.getR11_CURRENCY_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell84.setCellValue(record1.getR11_CURRENCY_NOSTRO1().toString().trim() );
		     cell84.setCellStyle(numberStyle);
		 } else {
		     cell84.setCellValue("");
		     cell84.setCellStyle(textStyle);
		 }

		  Cell cell85 = row.createCell(4);

		 if (record1.getR11_SOVEREIGN_RATING_NOSTRO1() != null && !record1.getR11_SOVEREIGN_RATING_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell85.setCellValue(record1.getR11_SOVEREIGN_RATING_NOSTRO1().toString().trim() );
		     cell85.setCellStyle(numberStyle);
		 } else {
		     cell85.setCellValue("");
		     cell85.setCellStyle(textStyle);
		 }

		  Cell cell86 = row.createCell(5);

		 if (record1.getR11_RISK_WEIGHT_NOSTRO1() != null && !record1.getR11_RISK_WEIGHT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell86.setCellValue(record1.getR11_RISK_WEIGHT_NOSTRO1().toString().trim() );
		     cell86.setCellStyle(numberStyle);
		 } else {
		     cell86.setCellValue("");
		     cell86.setCellStyle(textStyle);
		 }

		  Cell cell87 = row.createCell(6);

		 if (record1.getR11_AMOUNT_DEMAND_NOSTRO1() != null && !record1.getR11_AMOUNT_DEMAND_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell87.setCellValue(record1.getR11_AMOUNT_DEMAND_NOSTRO1().toString().trim() );
		     cell87.setCellStyle(numberStyle);
		 } else {
		     cell87.setCellValue("");
		     cell87.setCellStyle(textStyle);
		 }

		  Cell cell88 = row.createCell(7);

		 if (record1.getR11_RISK_WEIGHTED_AMOUNT_NOSTRO1() != null && !record1.getR11_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim().equals("N/A") ) {
		     cell88.setCellValue(record1.getR11_RISK_WEIGHTED_AMOUNT_NOSTRO1().toString().trim() );
		     cell88.setCellStyle(numberStyle);
		 } else {
		     cell88.setCellValue("");
		     cell88.setCellStyle(textStyle);
		 }



			
		}
	
	private void writeEmailExcelRowData5(Sheet sheet, List<BrrsMNosvosP5> dataList,
	        String[] rowCodes, String[] fieldSuffixes, int baseRow,
	        CellStyle numberStyle, CellStyle textStyle) {
	    System.out.println("came to write row data 4 method");

	    System.out.println("came to write row data 1 method");
		
	    BrrsMNosvosP5 record1 = dataList.get(0);
	    
	    Row row = sheet.getRow(62) != null ? sheet.getRow(62) : sheet.createRow(62);
	    Cell cell1 = row.createCell(0);

	   if (record1.getR1_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR1_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell1.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell1.setCellStyle(numberStyle);
	   } else {
	       cell1.setCellValue("");
	       cell1.setCellStyle(textStyle);
	   }

	    Cell cell2 = row.createCell(1);

	   if (record1.getR1_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR1_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell2.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell2.setCellStyle(numberStyle);
	   } else {
	       cell2.setCellValue("");
	       cell2.setCellStyle(textStyle);
	   }

	    Cell cell3 = row.createCell(2);

	   if (record1.getR1_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR1_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell3.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell3.setCellStyle(numberStyle);
	   } else {
	       cell3.setCellValue("");
	       cell3.setCellStyle(textStyle);
	   }

	    Cell cell4 = row.createCell(3);

	   if (record1.getR1_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR1_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell4.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell4.setCellStyle(numberStyle);
	   } else {
	       cell4.setCellValue("");
	       cell4.setCellStyle(textStyle);
	   }

	    Cell cell5 = row.createCell(4);

	   if (record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell5.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell5.setCellStyle(numberStyle);
	   } else {
	       cell5.setCellValue("");
	       cell5.setCellStyle(textStyle);
	   }

	    Cell cell6 = row.createCell(5);

	   if (record1.getR1_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR1_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell6.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell6.setCellStyle(numberStyle);
	   } else {
	       cell6.setCellValue("");
	       cell6.setCellStyle(textStyle);
	   }

	    Cell cell7 = row.createCell(6);

	   if (record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell7.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell7.setCellStyle(numberStyle);
	   } else {
	       cell7.setCellValue("");
	       cell7.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(63) != null ? sheet.getRow(63) : sheet.createRow(63);
	    Cell cell8 = row.createCell(0);

	   if (record1.getR2_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR2_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell8.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell8.setCellStyle(numberStyle);
	   } else {
	       cell8.setCellValue("");
	       cell8.setCellStyle(textStyle);
	   }

	    Cell cell9 = row.createCell(1);

	   if (record1.getR2_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR2_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell9.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell9.setCellStyle(numberStyle);
	   } else {
	       cell9.setCellValue("");
	       cell9.setCellStyle(textStyle);
	   }

	    Cell cell10 = row.createCell(2);

	   if (record1.getR2_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR2_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell10.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell10.setCellStyle(numberStyle);
	   } else {
	       cell10.setCellValue("");
	       cell10.setCellStyle(textStyle);
	   }

	    Cell cell11 = row.createCell(3);

	   if (record1.getR2_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR2_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell11.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell11.setCellStyle(numberStyle);
	   } else {
	       cell11.setCellValue("");
	       cell11.setCellStyle(textStyle);
	   }

	    Cell cell12 = row.createCell(4);

	   if (record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell12.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell12.setCellStyle(numberStyle);
	   } else {
	       cell12.setCellValue("");
	       cell12.setCellStyle(textStyle);
	   }

	    Cell cell13 = row.createCell(5);

	   if (record1.getR2_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR2_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell13.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell13.setCellStyle(numberStyle);
	   } else {
	       cell13.setCellValue("");
	       cell13.setCellStyle(textStyle);
	   }

	    Cell cell14 = row.createCell(6);

	   if (record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell14.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell14.setCellStyle(numberStyle);
	   } else {
	       cell14.setCellValue("");
	       cell14.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(64) != null ? sheet.getRow(64) : sheet.createRow(64);
	    Cell cell15 = row.createCell(0);

	   if (record1.getR3_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR3_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell15.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell15.setCellStyle(numberStyle);
	   } else {
	       cell15.setCellValue("");
	       cell15.setCellStyle(textStyle);
	   }

	    Cell cell16 = row.createCell(1);

	   if (record1.getR3_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR3_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell16.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell16.setCellStyle(numberStyle);
	   } else {
	       cell16.setCellValue("");
	       cell16.setCellStyle(textStyle);
	   }

	    Cell cell17 = row.createCell(2);

	   if (record1.getR3_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR3_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell17.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell17.setCellStyle(numberStyle);
	   } else {
	       cell17.setCellValue("");
	       cell17.setCellStyle(textStyle);
	   }

	    Cell cell18 = row.createCell(3);

	   if (record1.getR3_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR3_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell18.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell18.setCellStyle(numberStyle);
	   } else {
	       cell18.setCellValue("");
	       cell18.setCellStyle(textStyle);
	   }

	    Cell cell19 = row.createCell(4);

	   if (record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell19.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell19.setCellStyle(numberStyle);
	   } else {
	       cell19.setCellValue("");
	       cell19.setCellStyle(textStyle);
	   }

	    Cell cell20 = row.createCell(5);

	   if (record1.getR3_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR3_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell20.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell20.setCellStyle(numberStyle);
	   } else {
	       cell20.setCellValue("");
	       cell20.setCellStyle(textStyle);
	   }

	    Cell cell21 = row.createCell(6);

	   if (record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell21.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell21.setCellStyle(numberStyle);
	   } else {
	       cell21.setCellValue("");
	       cell21.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(65) != null ? sheet.getRow(65) : sheet.createRow(65);
	    Cell cell22 = row.createCell(0);

	   if (record1.getR4_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR4_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell22.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell22.setCellStyle(numberStyle);
	   } else {
	       cell22.setCellValue("");
	       cell22.setCellStyle(textStyle);
	   }

	    Cell cell23 = row.createCell(1);

	   if (record1.getR4_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR4_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell23.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell23.setCellStyle(numberStyle);
	   } else {
	       cell23.setCellValue("");
	       cell23.setCellStyle(textStyle);
	   }

	    Cell cell24 = row.createCell(2);

	   if (record1.getR4_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR4_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell24.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell24.setCellStyle(numberStyle);
	   } else {
	       cell24.setCellValue("");
	       cell24.setCellStyle(textStyle);
	   }

	    Cell cell25 = row.createCell(3);

	   if (record1.getR4_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR4_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell25.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell25.setCellStyle(numberStyle);
	   } else {
	       cell25.setCellValue("");
	       cell25.setCellStyle(textStyle);
	   }

	    Cell cell26 = row.createCell(4);

	   if (record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell26.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell26.setCellStyle(numberStyle);
	   } else {
	       cell26.setCellValue("");
	       cell26.setCellStyle(textStyle);
	   }

	    Cell cell27 = row.createCell(5);

	   if (record1.getR4_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR4_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell27.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell27.setCellStyle(numberStyle);
	   } else {
	       cell27.setCellValue("");
	       cell27.setCellStyle(textStyle);
	   }

	    Cell cell28 = row.createCell(6);

	   if (record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell28.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell28.setCellStyle(numberStyle);
	   } else {
	       cell28.setCellValue("");
	       cell28.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(66) != null ? sheet.getRow(66) : sheet.createRow(66);
	    Cell cell29 = row.createCell(0);

	   if (record1.getR5_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR5_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell29.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell29.setCellStyle(numberStyle);
	   } else {
	       cell29.setCellValue("");
	       cell29.setCellStyle(textStyle);
	   }

	    Cell cell30 = row.createCell(1);

	   if (record1.getR5_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR5_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell30.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell30.setCellStyle(numberStyle);
	   } else {
	       cell30.setCellValue("");
	       cell30.setCellStyle(textStyle);
	   }

	    Cell cell31 = row.createCell(2);

	   if (record1.getR5_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR5_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell31.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell31.setCellStyle(numberStyle);
	   } else {
	       cell31.setCellValue("");
	       cell31.setCellStyle(textStyle);
	   }

	    Cell cell32 = row.createCell(3);

	   if (record1.getR5_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR5_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell32.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell32.setCellStyle(numberStyle);
	   } else {
	       cell32.setCellValue("");
	       cell32.setCellStyle(textStyle);
	   }

	    Cell cell33 = row.createCell(4);

	   if (record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell33.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell33.setCellStyle(numberStyle);
	   } else {
	       cell33.setCellValue("");
	       cell33.setCellStyle(textStyle);
	   }

	    Cell cell34 = row.createCell(5);

	   if (record1.getR5_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR5_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell34.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell34.setCellStyle(numberStyle);
	   } else {
	       cell34.setCellValue("");
	       cell34.setCellStyle(textStyle);
	   }

	    Cell cell35 = row.createCell(6);

	   if (record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell35.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell35.setCellStyle(numberStyle);
	   } else {
	       cell35.setCellValue("");
	       cell35.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(67) != null ? sheet.getRow(67) : sheet.createRow(67);
	    Cell cell36 = row.createCell(0);

	   if (record1.getR6_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR6_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell36.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell36.setCellStyle(numberStyle);
	   } else {
	       cell36.setCellValue("");
	       cell36.setCellStyle(textStyle);
	   }

	    Cell cell37 = row.createCell(1);

	   if (record1.getR6_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR6_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell37.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell37.setCellStyle(numberStyle);
	   } else {
	       cell37.setCellValue("");
	       cell37.setCellStyle(textStyle);
	   }

	    Cell cell38 = row.createCell(2);

	   if (record1.getR6_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR6_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell38.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell38.setCellStyle(numberStyle);
	   } else {
	       cell38.setCellValue("");
	       cell38.setCellStyle(textStyle);
	   }

	    Cell cell39 = row.createCell(3);

	   if (record1.getR6_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR6_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell39.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell39.setCellStyle(numberStyle);
	   } else {
	       cell39.setCellValue("");
	       cell39.setCellStyle(textStyle);
	   }

	    Cell cell40 = row.createCell(4);

	   if (record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell40.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell40.setCellStyle(numberStyle);
	   } else {
	       cell40.setCellValue("");
	       cell40.setCellStyle(textStyle);
	   }

	    Cell cell41 = row.createCell(5);

	   if (record1.getR6_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR6_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell41.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell41.setCellStyle(numberStyle);
	   } else {
	       cell41.setCellValue("");
	       cell41.setCellStyle(textStyle);
	   }

	    Cell cell42 = row.createCell(6);

	   if (record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell42.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell42.setCellStyle(numberStyle);
	   } else {
	       cell42.setCellValue("");
	       cell42.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(68) != null ? sheet.getRow(68) : sheet.createRow(68);
	    Cell cell43 = row.createCell(0);

	   if (record1.getR7_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR7_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell43.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell43.setCellStyle(numberStyle);
	   } else {
	       cell43.setCellValue("");
	       cell43.setCellStyle(textStyle);
	   }

	    Cell cell44 = row.createCell(1);

	   if (record1.getR7_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR7_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell44.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell44.setCellStyle(numberStyle);
	   } else {
	       cell44.setCellValue("");
	       cell44.setCellStyle(textStyle);
	   }

	    Cell cell45 = row.createCell(2);

	   if (record1.getR7_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR7_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell45.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell45.setCellStyle(numberStyle);
	   } else {
	       cell45.setCellValue("");
	       cell45.setCellStyle(textStyle);
	   }

	    Cell cell46 = row.createCell(3);

	   if (record1.getR7_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR7_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell46.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell46.setCellStyle(numberStyle);
	   } else {
	       cell46.setCellValue("");
	       cell46.setCellStyle(textStyle);
	   }

	    Cell cell47 = row.createCell(4);

	   if (record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell47.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell47.setCellStyle(numberStyle);
	   } else {
	       cell47.setCellValue("");
	       cell47.setCellStyle(textStyle);
	   }

	    Cell cell48 = row.createCell(5);

	   if (record1.getR7_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR7_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell48.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell48.setCellStyle(numberStyle);
	   } else {
	       cell48.setCellValue("");
	       cell48.setCellStyle(textStyle);
	   }

	    Cell cell49 = row.createCell(6);

	   if (record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell49.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell49.setCellStyle(numberStyle);
	   } else {
	       cell49.setCellValue("");
	       cell49.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(69) != null ? sheet.getRow(69) : sheet.createRow(69);
	    Cell cell50 = row.createCell(0);

	   if (record1.getR8_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR8_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell50.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell50.setCellStyle(numberStyle);
	   } else {
	       cell50.setCellValue("");
	       cell50.setCellStyle(textStyle);
	   }

	    Cell cell51 = row.createCell(1);

	   if (record1.getR8_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR8_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell51.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell51.setCellStyle(numberStyle);
	   } else {
	       cell51.setCellValue("");
	       cell51.setCellStyle(textStyle);
	   }

	    Cell cell52 = row.createCell(2);

	   if (record1.getR8_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR8_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell52.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell52.setCellStyle(numberStyle);
	   } else {
	       cell52.setCellValue("");
	       cell52.setCellStyle(textStyle);
	   }

	    Cell cell53 = row.createCell(3);

	   if (record1.getR8_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR8_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell53.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell53.setCellStyle(numberStyle);
	   } else {
	       cell53.setCellValue("");
	       cell53.setCellStyle(textStyle);
	   }

	    Cell cell54 = row.createCell(4);

	   if (record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell54.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell54.setCellStyle(numberStyle);
	   } else {
	       cell54.setCellValue("");
	       cell54.setCellStyle(textStyle);
	   }

	    Cell cell55 = row.createCell(5);

	   if (record1.getR8_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR8_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell55.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell55.setCellStyle(numberStyle);
	   } else {
	       cell55.setCellValue("");
	       cell55.setCellStyle(textStyle);
	   }

	    Cell cell56 = row.createCell(6);

	   if (record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell56.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell56.setCellStyle(numberStyle);
	   } else {
	       cell56.setCellValue("");
	       cell56.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(70) != null ? sheet.getRow(70) : sheet.createRow(70);
	    Cell cell57 = row.createCell(0);

	   if (record1.getR9_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR9_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell57.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell57.setCellStyle(numberStyle);
	   } else {
	       cell57.setCellValue("");
	       cell57.setCellStyle(textStyle);
	   }

	    Cell cell58 = row.createCell(1);

	   if (record1.getR9_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR9_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell58.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell58.setCellStyle(numberStyle);
	   } else {
	       cell58.setCellValue("");
	       cell58.setCellStyle(textStyle);
	   }

	    Cell cell59 = row.createCell(2);

	   if (record1.getR9_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR9_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell59.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell59.setCellStyle(numberStyle);
	   } else {
	       cell59.setCellValue("");
	       cell59.setCellStyle(textStyle);
	   }

	    Cell cell60 = row.createCell(3);

	   if (record1.getR9_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR9_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell60.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell60.setCellStyle(numberStyle);
	   } else {
	       cell60.setCellValue("");
	       cell60.setCellStyle(textStyle);
	   }

	    Cell cell61 = row.createCell(4);

	   if (record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell61.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell61.setCellStyle(numberStyle);
	   } else {
	       cell61.setCellValue("");
	       cell61.setCellStyle(textStyle);
	   }

	    Cell cell62 = row.createCell(5);

	   if (record1.getR9_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR9_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell62.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell62.setCellStyle(numberStyle);
	   } else {
	       cell62.setCellValue("");
	       cell62.setCellStyle(textStyle);
	   }

	    Cell cell63 = row.createCell(6);

	   if (record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell63.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell63.setCellStyle(numberStyle);
	   } else {
	       cell63.setCellValue("");
	       cell63.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(71) != null ? sheet.getRow(71) : sheet.createRow(71);
	    Cell cell64 = row.createCell(0);

	   if (record1.getR10_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR10_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell64.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell64.setCellStyle(numberStyle);
	   } else {
	       cell64.setCellValue("");
	       cell64.setCellStyle(textStyle);
	   }

	    Cell cell65 = row.createCell(1);

	   if (record1.getR10_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR10_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell65.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell65.setCellStyle(numberStyle);
	   } else {
	       cell65.setCellValue("");
	       cell65.setCellStyle(textStyle);
	   }

	    Cell cell66 = row.createCell(2);

	   if (record1.getR10_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR10_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell66.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell66.setCellStyle(numberStyle);
	   } else {
	       cell66.setCellValue("");
	       cell66.setCellStyle(textStyle);
	   }

	    Cell cell67 = row.createCell(3);

	   if (record1.getR10_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR10_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell67.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell67.setCellStyle(numberStyle);
	   } else {
	       cell67.setCellValue("");
	       cell67.setCellStyle(textStyle);
	   }

	    Cell cell68 = row.createCell(4);

	   if (record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell68.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell68.setCellStyle(numberStyle);
	   } else {
	       cell68.setCellValue("");
	       cell68.setCellStyle(textStyle);
	   }

	    Cell cell69 = row.createCell(5);

	   if (record1.getR10_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR10_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell69.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell69.setCellStyle(numberStyle);
	   } else {
	       cell69.setCellValue("");
	       cell69.setCellStyle(textStyle);
	   }

	    Cell cell70 = row.createCell(6);

	   if (record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell70.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell70.setCellStyle(numberStyle);
	   } else {
	       cell70.setCellValue("");
	       cell70.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(72) != null ? sheet.getRow(72) : sheet.createRow(72);
	    Cell cell71 = row.createCell(0);

	   if (record1.getR11_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR11_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell71.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell71.setCellStyle(numberStyle);
	   } else {
	       cell71.setCellValue("");
	       cell71.setCellStyle(textStyle);
	   }

	    Cell cell72 = row.createCell(1);

	   if (record1.getR11_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR11_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell72.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell72.setCellStyle(numberStyle);
	   } else {
	       cell72.setCellValue("");
	       cell72.setCellStyle(textStyle);
	   }

	    Cell cell73 = row.createCell(2);

	   if (record1.getR11_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR11_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell73.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell73.setCellStyle(numberStyle);
	   } else {
	       cell73.setCellValue("");
	       cell73.setCellStyle(textStyle);
	   }

	    Cell cell74 = row.createCell(3);

	   if (record1.getR11_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR11_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell74.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell74.setCellStyle(numberStyle);
	   } else {
	       cell74.setCellValue("");
	       cell74.setCellStyle(textStyle);
	   }

	    Cell cell75 = row.createCell(4);

	   if (record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell75.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell75.setCellStyle(numberStyle);
	   } else {
	       cell75.setCellValue("");
	       cell75.setCellStyle(textStyle);
	   }

	    Cell cell76 = row.createCell(5);

	   if (record1.getR11_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR11_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell76.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell76.setCellStyle(numberStyle);
	   } else {
	       cell76.setCellValue("");
	       cell76.setCellStyle(textStyle);
	   }

	    Cell cell77 = row.createCell(6);

	   if (record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell77.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell77.setCellStyle(numberStyle);
	   } else {
	       cell77.setCellValue("");
	       cell77.setCellStyle(textStyle);
	   }

	   row = sheet.getRow(77) != null ? sheet.getRow(77) : sheet.createRow(77);
	   Cell cell78 = row.createCell(0);

	  if (record1.getR1_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR1_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell78.setCellValue(record1.getR1_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell78.setCellStyle(numberStyle);
	  } else {
	      cell78.setCellValue("");
	      cell78.setCellStyle(textStyle);
	  }

	   Cell cell79 = row.createCell(1);

	  if (record1.getR1_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR1_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell79.setCellValue(record1.getR1_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell79.setCellStyle(numberStyle);
	  } else {
	      cell79.setCellValue("");
	      cell79.setCellStyle(textStyle);
	  }

	   Cell cell80 = row.createCell(2);

	  if (record1.getR1_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR1_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell80.setCellValue(record1.getR1_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell80.setCellStyle(numberStyle);
	  } else {
	      cell80.setCellValue("");
	      cell80.setCellStyle(textStyle);
	  }

	   Cell cell81 = row.createCell(3);

	  if (record1.getR1_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR1_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell81.setCellValue(record1.getR1_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell81.setCellStyle(numberStyle);
	  } else {
	      cell81.setCellValue("");
	      cell81.setCellStyle(textStyle);
	  }

	   Cell cell82 = row.createCell(4);

	  if (record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell82.setCellValue(record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell82.setCellStyle(numberStyle);
	  } else {
	      cell82.setCellValue("");
	      cell82.setCellStyle(textStyle);
	  }

	   Cell cell83 = row.createCell(5);

	  if (record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell83.setCellValue(record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell83.setCellStyle(numberStyle);
	  } else {
	      cell83.setCellValue("");
	      cell83.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(78) != null ? sheet.getRow(78) : sheet.createRow(78);
	   Cell cell84 = row.createCell(0);

	  if (record1.getR2_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR2_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell84.setCellValue(record1.getR2_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell84.setCellStyle(numberStyle);
	  } else {
	      cell84.setCellValue("");
	      cell84.setCellStyle(textStyle);
	  }

	   Cell cell85 = row.createCell(1);

	  if (record1.getR2_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR2_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell85.setCellValue(record1.getR2_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell85.setCellStyle(numberStyle);
	  } else {
	      cell85.setCellValue("");
	      cell85.setCellStyle(textStyle);
	  }

	   Cell cell86 = row.createCell(2);

	  if (record1.getR2_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR2_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell86.setCellValue(record1.getR2_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell86.setCellStyle(numberStyle);
	  } else {
	      cell86.setCellValue("");
	      cell86.setCellStyle(textStyle);
	  }

	   Cell cell87 = row.createCell(3);

	  if (record1.getR2_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR2_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell87.setCellValue(record1.getR2_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell87.setCellStyle(numberStyle);
	  } else {
	      cell87.setCellValue("");
	      cell87.setCellStyle(textStyle);
	  }

	   Cell cell88 = row.createCell(4);

	  if (record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell88.setCellValue(record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell88.setCellStyle(numberStyle);
	  } else {
	      cell88.setCellValue("");
	      cell88.setCellStyle(textStyle);
	  }

	   Cell cell89 = row.createCell(5);

	  if (record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell89.setCellValue(record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell89.setCellStyle(numberStyle);
	  } else {
	      cell89.setCellValue("");
	      cell89.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(79) != null ? sheet.getRow(79) : sheet.createRow(79);
	   Cell cell90 = row.createCell(0);

	  if (record1.getR3_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR3_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell90.setCellValue(record1.getR3_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell90.setCellStyle(numberStyle);
	  } else {
	      cell90.setCellValue("");
	      cell90.setCellStyle(textStyle);
	  }

	   Cell cell91 = row.createCell(1);

	  if (record1.getR3_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR3_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell91.setCellValue(record1.getR3_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell91.setCellStyle(numberStyle);
	  } else {
	      cell91.setCellValue("");
	      cell91.setCellStyle(textStyle);
	  }

	   Cell cell92 = row.createCell(2);

	  if (record1.getR3_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR3_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell92.setCellValue(record1.getR3_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell92.setCellStyle(numberStyle);
	  } else {
	      cell92.setCellValue("");
	      cell92.setCellStyle(textStyle);
	  }

	   Cell cell93 = row.createCell(3);

	  if (record1.getR3_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR3_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell93.setCellValue(record1.getR3_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell93.setCellStyle(numberStyle);
	  } else {
	      cell93.setCellValue("");
	      cell93.setCellStyle(textStyle);
	  }

	   Cell cell94 = row.createCell(4);

	  if (record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell94.setCellValue(record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell94.setCellStyle(numberStyle);
	  } else {
	      cell94.setCellValue("");
	      cell94.setCellStyle(textStyle);
	  }

	   Cell cell95 = row.createCell(5);

	  if (record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell95.setCellValue(record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell95.setCellStyle(numberStyle);
	  } else {
	      cell95.setCellValue("");
	      cell95.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(80) != null ? sheet.getRow(80) : sheet.createRow(80);
	   Cell cell96 = row.createCell(0);

	  if (record1.getR4_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR4_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell96.setCellValue(record1.getR4_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell96.setCellStyle(numberStyle);
	  } else {
	      cell96.setCellValue("");
	      cell96.setCellStyle(textStyle);
	  }

	   Cell cell97 = row.createCell(1);

	  if (record1.getR4_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR4_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell97.setCellValue(record1.getR4_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell97.setCellStyle(numberStyle);
	  } else {
	      cell97.setCellValue("");
	      cell97.setCellStyle(textStyle);
	  }

	   Cell cell98 = row.createCell(2);

	  if (record1.getR4_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR4_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell98.setCellValue(record1.getR4_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell98.setCellStyle(numberStyle);
	  } else {
	      cell98.setCellValue("");
	      cell98.setCellStyle(textStyle);
	  }

	   Cell cell99 = row.createCell(3);

	  if (record1.getR4_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR4_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell99.setCellValue(record1.getR4_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell99.setCellStyle(numberStyle);
	  } else {
	      cell99.setCellValue("");
	      cell99.setCellStyle(textStyle);
	  }

	   Cell cell100 = row.createCell(4);

	  if (record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell100.setCellValue(record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell100.setCellStyle(numberStyle);
	  } else {
	      cell100.setCellValue("");
	      cell100.setCellStyle(textStyle);
	  }

	   Cell cell101 = row.createCell(5);

	  if (record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell101.setCellValue(record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell101.setCellStyle(numberStyle);
	  } else {
	      cell101.setCellValue("");
	      cell101.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(81) != null ? sheet.getRow(81) : sheet.createRow(81);
	   Cell cell102 = row.createCell(0);

	  if (record1.getR5_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR5_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell102.setCellValue(record1.getR5_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell102.setCellStyle(numberStyle);
	  } else {
	      cell102.setCellValue("");
	      cell102.setCellStyle(textStyle);
	  }

	   Cell cell103 = row.createCell(1);

	  if (record1.getR5_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR5_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell103.setCellValue(record1.getR5_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell103.setCellStyle(numberStyle);
	  } else {
	      cell103.setCellValue("");
	      cell103.setCellStyle(textStyle);
	  }

	   Cell cell104 = row.createCell(2);

	  if (record1.getR5_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR5_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell104.setCellValue(record1.getR5_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell104.setCellStyle(numberStyle);
	  } else {
	      cell104.setCellValue("");
	      cell104.setCellStyle(textStyle);
	  }

	   Cell cell105 = row.createCell(3);

	  if (record1.getR5_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR5_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell105.setCellValue(record1.getR5_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell105.setCellStyle(numberStyle);
	  } else {
	      cell105.setCellValue("");
	      cell105.setCellStyle(textStyle);
	  }

	   Cell cell106 = row.createCell(4);

	  if (record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell106.setCellValue(record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell106.setCellStyle(numberStyle);
	  } else {
	      cell106.setCellValue("");
	      cell106.setCellStyle(textStyle);
	  }

	   Cell cell107 = row.createCell(5);

	  if (record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell107.setCellValue(record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell107.setCellStyle(numberStyle);
	  } else {
	      cell107.setCellValue("");
	      cell107.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(82) != null ? sheet.getRow(82) : sheet.createRow(82);
	   Cell cell108 = row.createCell(0);

	  if (record1.getR6_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR6_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell108.setCellValue(record1.getR6_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell108.setCellStyle(numberStyle);
	  } else {
	      cell108.setCellValue("");
	      cell108.setCellStyle(textStyle);
	  }

	   Cell cell109 = row.createCell(1);

	  if (record1.getR6_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR6_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell109.setCellValue(record1.getR6_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell109.setCellStyle(numberStyle);
	  } else {
	      cell109.setCellValue("");
	      cell109.setCellStyle(textStyle);
	  }

	   Cell cell110 = row.createCell(2);

	  if (record1.getR6_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR6_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell110.setCellValue(record1.getR6_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell110.setCellStyle(numberStyle);
	  } else {
	      cell110.setCellValue("");
	      cell110.setCellStyle(textStyle);
	  }

	   Cell cell111 = row.createCell(3);

	  if (record1.getR6_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR6_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell111.setCellValue(record1.getR6_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell111.setCellStyle(numberStyle);
	  } else {
	      cell111.setCellValue("");
	      cell111.setCellStyle(textStyle);
	  }

	   Cell cell112 = row.createCell(4);

	  if (record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell112.setCellValue(record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell112.setCellStyle(numberStyle);
	  } else {
	      cell112.setCellValue("");
	      cell112.setCellStyle(textStyle);
	  }

	   Cell cell113 = row.createCell(5);

	  if (record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell113.setCellValue(record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell113.setCellStyle(numberStyle);
	  } else {
	      cell113.setCellValue("");
	      cell113.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(83) != null ? sheet.getRow(83) : sheet.createRow(83);
	   Cell cell114 = row.createCell(0);

	  if (record1.getR7_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR7_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell114.setCellValue(record1.getR7_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell114.setCellStyle(numberStyle);
	  } else {
	      cell114.setCellValue("");
	      cell114.setCellStyle(textStyle);
	  }

	   Cell cell115 = row.createCell(1);

	  if (record1.getR7_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR7_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell115.setCellValue(record1.getR7_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell115.setCellStyle(numberStyle);
	  } else {
	      cell115.setCellValue("");
	      cell115.setCellStyle(textStyle);
	  }

	   Cell cell116 = row.createCell(2);

	  if (record1.getR7_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR7_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell116.setCellValue(record1.getR7_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell116.setCellStyle(numberStyle);
	  } else {
	      cell116.setCellValue("");
	      cell116.setCellStyle(textStyle);
	  }

	   Cell cell117 = row.createCell(3);

	  if (record1.getR7_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR7_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell117.setCellValue(record1.getR7_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell117.setCellStyle(numberStyle);
	  } else {
	      cell117.setCellValue("");
	      cell117.setCellStyle(textStyle);
	  }

	   Cell cell118 = row.createCell(4);

	  if (record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell118.setCellValue(record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell118.setCellStyle(numberStyle);
	  } else {
	      cell118.setCellValue("");
	      cell118.setCellStyle(textStyle);
	  }

	   Cell cell119 = row.createCell(5);

	  if (record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell119.setCellValue(record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell119.setCellStyle(numberStyle);
	  } else {
	      cell119.setCellValue("");
	      cell119.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(84) != null ? sheet.getRow(84) : sheet.createRow(84);
	   Cell cell120 = row.createCell(0);

	  if (record1.getR8_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR8_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell120.setCellValue(record1.getR8_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell120.setCellStyle(numberStyle);
	  } else {
	      cell120.setCellValue("");
	      cell120.setCellStyle(textStyle);
	  }

	   Cell cell121 = row.createCell(1);

	  if (record1.getR8_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR8_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell121.setCellValue(record1.getR8_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell121.setCellStyle(numberStyle);
	  } else {
	      cell121.setCellValue("");
	      cell121.setCellStyle(textStyle);
	  }

	   Cell cell122 = row.createCell(2);

	  if (record1.getR8_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR8_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell122.setCellValue(record1.getR8_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell122.setCellStyle(numberStyle);
	  } else {
	      cell122.setCellValue("");
	      cell122.setCellStyle(textStyle);
	  }

	   Cell cell123 = row.createCell(3);

	  if (record1.getR8_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR8_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell123.setCellValue(record1.getR8_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell123.setCellStyle(numberStyle);
	  } else {
	      cell123.setCellValue("");
	      cell123.setCellStyle(textStyle);
	  }

	   Cell cell124 = row.createCell(4);

	  if (record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell124.setCellValue(record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell124.setCellStyle(numberStyle);
	  } else {
	      cell124.setCellValue("");
	      cell124.setCellStyle(textStyle);
	  }

	   Cell cell125 = row.createCell(5);

	  if (record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell125.setCellValue(record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell125.setCellStyle(numberStyle);
	  } else {
	      cell125.setCellValue("");
	      cell125.setCellStyle(textStyle);
	  }

	}
	
	private void writeEmailExcelRowData4(Sheet sheet, List<BrrsMNosvosP4> dataList,
	        String[] rowCodes, String[] fieldSuffixes, int baseRow,
	        CellStyle numberStyle, CellStyle textStyle) {
	    System.out.println("came to write row data 4 method");

	    System.out.println("came to write row data 1 method");
		
	    BrrsMNosvosP4 record1 = dataList.get(0);
	    
	    Row  row = sheet.getRow(51) != null ? sheet.getRow(51) : sheet.createRow(51);
	    Cell cell1 = row.createCell(0);

	    if (record1.getR1_NAME_OF_BANK_VOSTRO1() != null && !record1.getR1_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell1.setCellValue(record1.getR1_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell1.setCellStyle(numberStyle);
	    } else {
	        cell1.setCellValue("");
	        cell1.setCellStyle(textStyle);
	    }

	     Cell cell2 = row.createCell(1);

	    if (record1.getR1_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR1_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell2.setCellValue(record1.getR1_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell2.setCellStyle(numberStyle);
	    } else {
	        cell2.setCellValue("");
	        cell2.setCellStyle(textStyle);
	    }

	     Cell cell3 = row.createCell(2);

	    if (record1.getR1_PURPOSE_VOSTRO1() != null && !record1.getR1_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell3.setCellValue(record1.getR1_PURPOSE_VOSTRO1().toString().trim() );
	        cell3.setCellStyle(numberStyle);
	    } else {
	        cell3.setCellValue("");
	        cell3.setCellStyle(textStyle);
	    }

	     Cell cell4 = row.createCell(3);

	    if (record1.getR1_CURRENCY_VOSTRO1() != null && !record1.getR1_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell4.setCellValue(record1.getR1_CURRENCY_VOSTRO1().toString().trim() );
	        cell4.setCellStyle(numberStyle);
	    } else {
	        cell4.setCellValue("");
	        cell4.setCellStyle(textStyle);
	    }

	     Cell cell7 = row.createCell(6);

	    if (record1.getR1_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR1_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell7.setCellValue(record1.getR1_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell7.setCellStyle(numberStyle);
	    } else {
	        cell7.setCellValue("");
	        cell7.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(52) != null ? sheet.getRow(52) : sheet.createRow(52);
	     Cell cell9 = row.createCell(0);

	    if (record1.getR2_NAME_OF_BANK_VOSTRO1() != null && !record1.getR2_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell9.setCellValue(record1.getR2_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell9.setCellStyle(numberStyle);
	    } else {
	        cell9.setCellValue("");
	        cell9.setCellStyle(textStyle);
	    }

	     Cell cell10 = row.createCell(1);

	    if (record1.getR2_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR2_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell10.setCellValue(record1.getR2_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell10.setCellStyle(numberStyle);
	    } else {
	        cell10.setCellValue("");
	        cell10.setCellStyle(textStyle);
	    }

	     Cell cell11 = row.createCell(2);

	    if (record1.getR2_PURPOSE_VOSTRO1() != null && !record1.getR2_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell11.setCellValue(record1.getR2_PURPOSE_VOSTRO1().toString().trim() );
	        cell11.setCellStyle(numberStyle);
	    } else {
	        cell11.setCellValue("");
	        cell11.setCellStyle(textStyle);
	    }

	     Cell cell12 = row.createCell(3);

	    if (record1.getR2_CURRENCY_VOSTRO1() != null && !record1.getR2_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell12.setCellValue(record1.getR2_CURRENCY_VOSTRO1().toString().trim() );
	        cell12.setCellStyle(numberStyle);
	    } else {
	        cell12.setCellValue("");
	        cell12.setCellStyle(textStyle);
	    }

	     Cell cell15 = row.createCell(6);

	    if (record1.getR2_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR2_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell15.setCellValue(record1.getR2_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell15.setCellStyle(numberStyle);
	    } else {
	        cell15.setCellValue("");
	        cell15.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(53) != null ? sheet.getRow(53) : sheet.createRow(53);
	     Cell cell17 = row.createCell(0);

	    if (record1.getR3_NAME_OF_BANK_VOSTRO1() != null && !record1.getR3_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell17.setCellValue(record1.getR3_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell17.setCellStyle(numberStyle);
	    } else {
	        cell17.setCellValue("");
	        cell17.setCellStyle(textStyle);
	    }

	     Cell cell18 = row.createCell(1);

	    if (record1.getR3_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR3_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell18.setCellValue(record1.getR3_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell18.setCellStyle(numberStyle);
	    } else {
	        cell18.setCellValue("");
	        cell18.setCellStyle(textStyle);
	    }

	     Cell cell19 = row.createCell(2);

	    if (record1.getR3_PURPOSE_VOSTRO1() != null && !record1.getR3_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell19.setCellValue(record1.getR3_PURPOSE_VOSTRO1().toString().trim() );
	        cell19.setCellStyle(numberStyle);
	    } else {
	        cell19.setCellValue("");
	        cell19.setCellStyle(textStyle);
	    }

	     Cell cell20 = row.createCell(3);

	    if (record1.getR3_CURRENCY_VOSTRO1() != null && !record1.getR3_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell20.setCellValue(record1.getR3_CURRENCY_VOSTRO1().toString().trim() );
	        cell20.setCellStyle(numberStyle);
	    } else {
	        cell20.setCellValue("");
	        cell20.setCellStyle(textStyle);
	    }

	     Cell cell23 = row.createCell(6);

	    if (record1.getR3_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR3_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell23.setCellValue(record1.getR3_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell23.setCellStyle(numberStyle);
	    } else {
	        cell23.setCellValue("");
	        cell23.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(54) != null ? sheet.getRow(54) : sheet.createRow(54);
	     Cell cell25 = row.createCell(0);

	    if (record1.getR4_NAME_OF_BANK_VOSTRO1() != null && !record1.getR4_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell25.setCellValue(record1.getR4_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell25.setCellStyle(numberStyle);
	    } else {
	        cell25.setCellValue("");
	        cell25.setCellStyle(textStyle);
	    }

	     Cell cell26 = row.createCell(1);

	    if (record1.getR4_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR4_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell26.setCellValue(record1.getR4_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell26.setCellStyle(numberStyle);
	    } else {
	        cell26.setCellValue("");
	        cell26.setCellStyle(textStyle);
	    }

	     Cell cell27 = row.createCell(2);

	    if (record1.getR4_PURPOSE_VOSTRO1() != null && !record1.getR4_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell27.setCellValue(record1.getR4_PURPOSE_VOSTRO1().toString().trim() );
	        cell27.setCellStyle(numberStyle);
	    } else {
	        cell27.setCellValue("");
	        cell27.setCellStyle(textStyle);
	    }

	     Cell cell28 = row.createCell(3);

	    if (record1.getR4_CURRENCY_VOSTRO1() != null && !record1.getR4_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell28.setCellValue(record1.getR4_CURRENCY_VOSTRO1().toString().trim() );
	        cell28.setCellStyle(numberStyle);
	    } else {
	        cell28.setCellValue("");
	        cell28.setCellStyle(textStyle);
	    }

	     Cell cell31 = row.createCell(6);

	    if (record1.getR4_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR4_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell31.setCellValue(record1.getR4_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell31.setCellStyle(numberStyle);
	    } else {
	        cell31.setCellValue("");
	        cell31.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(55) != null ? sheet.getRow(55) : sheet.createRow(55);
	     Cell cell33 = row.createCell(0);

	    if (record1.getR5_NAME_OF_BANK_VOSTRO1() != null && !record1.getR5_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell33.setCellValue(record1.getR5_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell33.setCellStyle(numberStyle);
	    } else {
	        cell33.setCellValue("");
	        cell33.setCellStyle(textStyle);
	    }

	     Cell cell34 = row.createCell(1);

	    if (record1.getR5_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR5_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell34.setCellValue(record1.getR5_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell34.setCellStyle(numberStyle);
	    } else {
	        cell34.setCellValue("");
	        cell34.setCellStyle(textStyle);
	    }

	     Cell cell35 = row.createCell(2);

	    if (record1.getR5_PURPOSE_VOSTRO1() != null && !record1.getR5_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell35.setCellValue(record1.getR5_PURPOSE_VOSTRO1().toString().trim() );
	        cell35.setCellStyle(numberStyle);
	    } else {
	        cell35.setCellValue("");
	        cell35.setCellStyle(textStyle);
	    }

	     Cell cell36 = row.createCell(3);

	    if (record1.getR5_CURRENCY_VOSTRO1() != null && !record1.getR5_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell36.setCellValue(record1.getR5_CURRENCY_VOSTRO1().toString().trim() );
	        cell36.setCellStyle(numberStyle);
	    } else {
	        cell36.setCellValue("");
	        cell36.setCellStyle(textStyle);
	    }

	     Cell cell39 = row.createCell(6);

	    if (record1.getR5_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR5_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell39.setCellValue(record1.getR5_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell39.setCellStyle(numberStyle);
	    } else {
	        cell39.setCellValue("");
	        cell39.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(56) != null ? sheet.getRow(56) : sheet.createRow(56);
	     Cell cell41 = row.createCell(0);

	    if (record1.getR6_NAME_OF_BANK_VOSTRO1() != null && !record1.getR6_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell41.setCellValue(record1.getR6_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell41.setCellStyle(numberStyle);
	    } else {
	        cell41.setCellValue("");
	        cell41.setCellStyle(textStyle);
	    }

	     Cell cell42 = row.createCell(1);

	    if (record1.getR6_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR6_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell42.setCellValue(record1.getR6_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell42.setCellStyle(numberStyle);
	    } else {
	        cell42.setCellValue("");
	        cell42.setCellStyle(textStyle);
	    }

	     Cell cell43 = row.createCell(2);

	    if (record1.getR6_PURPOSE_VOSTRO1() != null && !record1.getR6_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell43.setCellValue(record1.getR6_PURPOSE_VOSTRO1().toString().trim() );
	        cell43.setCellStyle(numberStyle);
	    } else {
	        cell43.setCellValue("");
	        cell43.setCellStyle(textStyle);
	    }

	     Cell cell44 = row.createCell(3);

	    if (record1.getR6_CURRENCY_VOSTRO1() != null && !record1.getR6_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell44.setCellValue(record1.getR6_CURRENCY_VOSTRO1().toString().trim() );
	        cell44.setCellStyle(numberStyle);
	    } else {
	        cell44.setCellValue("");
	        cell44.setCellStyle(textStyle);
	    }

	     Cell cell47 = row.createCell(6);

	    if (record1.getR6_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR6_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell47.setCellValue(record1.getR6_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell47.setCellStyle(numberStyle);
	    } else {
	        cell47.setCellValue("");
	        cell47.setCellStyle(textStyle);
	    }


	     row = sheet.getRow(57) != null ? sheet.getRow(57) : sheet.createRow(57);
	     Cell cell49 = row.createCell(0);

	    if (record1.getR7_NAME_OF_BANK_VOSTRO1() != null && !record1.getR7_NAME_OF_BANK_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell49.setCellValue(record1.getR7_NAME_OF_BANK_VOSTRO1().toString().trim() );
	        cell49.setCellStyle(numberStyle);
	    } else {
	        cell49.setCellValue("");
	        cell49.setCellStyle(textStyle);
	    }

	     Cell cell50 = row.createCell(1);

	    if (record1.getR7_TYPE_OF_ACCOUNT_VOSTRO1() != null && !record1.getR7_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell50.setCellValue(record1.getR7_TYPE_OF_ACCOUNT_VOSTRO1().toString().trim() );
	        cell50.setCellStyle(numberStyle);
	    } else {
	        cell50.setCellValue("");
	        cell50.setCellStyle(textStyle);
	    }

	     Cell cell51 = row.createCell(2);

	    if (record1.getR7_PURPOSE_VOSTRO1() != null && !record1.getR7_PURPOSE_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell51.setCellValue(record1.getR7_PURPOSE_VOSTRO1().toString().trim() );
	        cell51.setCellStyle(numberStyle);
	    } else {
	        cell51.setCellValue("");
	        cell51.setCellStyle(textStyle);
	    }

	     Cell cell52 = row.createCell(3);

	    if (record1.getR7_CURRENCY_VOSTRO1() != null && !record1.getR7_CURRENCY_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell52.setCellValue(record1.getR7_CURRENCY_VOSTRO1().toString().trim() );
	        cell52.setCellStyle(numberStyle);
	    } else {
	        cell52.setCellValue("");
	        cell52.setCellStyle(textStyle);
	    }

	     Cell cell55 = row.createCell(6);

	    if (record1.getR7_AMOUNT_DEMAND_VOSTRO1() != null && !record1.getR7_AMOUNT_DEMAND_VOSTRO1().toString().trim().equals("N/A") ) {
	        cell55.setCellValue(record1.getR7_AMOUNT_DEMAND_VOSTRO1().toString().trim() );
	        cell55.setCellStyle(numberStyle);
	    } else {
	        cell55.setCellValue("");
	        cell55.setCellStyle(textStyle);
	    }



		
	}
	
	private void writeRowData1(Sheet sheet, List<BrrsMNosvosP1> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			System.out.println("came to write row data 1 method");
			
			for (BrrsMNosvosP1 record : dataList) {
			
			for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
			  String rowCode = rowCodes[rowIndex];
			  Row row = sheet.getRow(baseRow + rowIndex);
			 
			  if (row == null) row = sheet.createRow(baseRow + rowIndex);
			
			  for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
			      String fieldName = rowCode+ "_" + fieldSuffixes[colIndex];
			   
			      // 👉 Skip column B (index 1)
			      int excelColIndex = 1+colIndex ;

			      Cell cell = row.createCell(excelColIndex);
			      try {
			          Field field = BrrsMNosvosP1.class.getDeclaredField(fieldName);
			          field.setAccessible(true);
			          Object value = field.get(record);
			         
			          if (value == null || "N/A".equals(value.toString().trim())) {
	                        // ✅ keep cell with style but no value
	                        cell.setCellValue("");
	                        cell.setCellStyle(textStyle);
	                        continue;
	                    }

			          if (value instanceof BigDecimal) {
			              cell.setCellValue(((BigDecimal) value).doubleValue());
			              cell.setCellStyle(numberStyle);
			          }else if (value instanceof String) {
			        	    cell.setCellValue((String) value);
			        	    cell.setCellStyle(textStyle);
			        	}  else {
			              cell.setCellValue("");
			              cell.setCellStyle(textStyle);
			          }
			      } catch (NoSuchFieldException | IllegalAccessException e) {
			          cell.setCellValue("");
			          cell.setCellStyle(textStyle);
			          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
			      }
			  }
			}
			}
		}


	
	private void writeRowData2(Sheet sheet, List<BrrsMNosvosP2> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			System.out.println("came to write row data 1 method");
			
			for (BrrsMNosvosP2 record : dataList) {
			
			for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
			  String rowCode = rowCodes[rowIndex];
			  Row row = sheet.getRow(baseRow + rowIndex);
			 
			  if (row == null) row = sheet.createRow(baseRow + rowIndex);
			
			  for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
			      String fieldName = rowCode+ "_" + fieldSuffixes[colIndex];
			   
			      // 👉 Skip column B (index 1)
			      int excelColIndex = 11+colIndex;

			      Cell cell = row.createCell(excelColIndex);
			      try {
			          Field field = BrrsMNosvosP2.class.getDeclaredField(fieldName);
			          field.setAccessible(true);
			          Object value = field.get(record);
			         
			          if (value == null || "N/A".equals(value.toString().trim())) {
	                        // ✅ keep cell with style but no value
	                        cell.setCellValue("");
	                        cell.setCellStyle(textStyle);
	                        continue;
	                    }
			          
			          if (value instanceof BigDecimal) {
			              cell.setCellValue(((BigDecimal) value).doubleValue());
			              cell.setCellStyle(numberStyle);
			          }else if (value instanceof String) {
			        	    cell.setCellValue((String) value);
			        	    cell.setCellStyle(textStyle);
			        	}  else {
			              cell.setCellValue("");
			              cell.setCellStyle(textStyle);
			          }
			      } catch (NoSuchFieldException | IllegalAccessException e) {
			          cell.setCellValue("");
			          cell.setCellStyle(textStyle);
			          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
			      }
			  }
			}
			}
		}
	
	private void writeRowData3(Sheet sheet, List<BrrsMNosvosP3> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			System.out.println("came to write row data 1 method");
			
			for (BrrsMNosvosP3 record : dataList) {
			
			for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
			  String rowCode = rowCodes[rowIndex];
			  Row row = sheet.getRow(baseRow + rowIndex);
			 
			  if (row == null) row = sheet.createRow(baseRow + rowIndex);
			
			  for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
			      String fieldName = rowCode+ "_" + fieldSuffixes[colIndex];
			   
			      // 👉 Skip column B (index 1)
			      int excelColIndex = 1+colIndex ;

			      Cell cell = row.createCell(excelColIndex);
			      try {
			          Field field = BrrsMNosvosP3.class.getDeclaredField(fieldName);
			          field.setAccessible(true);
			          Object value = field.get(record);
			         
			          if (value == null || "N/A".equals(value.toString().trim())) {
	                        // ✅ keep cell with style but no value
	                        cell.setCellValue("");
	                        cell.setCellStyle(textStyle);
	                        continue;
	                    }

			          if (value instanceof BigDecimal) {
			              cell.setCellValue(((BigDecimal) value).doubleValue());
			              cell.setCellStyle(numberStyle);
			          }else if (value instanceof String) {
			        	    cell.setCellValue((String) value);
			        	    cell.setCellStyle(textStyle);
			        	}  else {
			              cell.setCellValue("");
			              cell.setCellStyle(textStyle);
			          }
			      } catch (NoSuchFieldException | IllegalAccessException e) {
			          cell.setCellValue("");
			          cell.setCellStyle(textStyle);
			          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
			      }
			  }
			}
			}
		}
	
	private void writeRowData4(Sheet sheet, List<BrrsMNosvosP4> dataList,
	        String[] rowCodes, String[] fieldSuffixes, int baseRow,
	        CellStyle numberStyle, CellStyle textStyle) {
	    System.out.println("came to write row data 4 method");

	    for (BrrsMNosvosP4 record : dataList) {

	        for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
	            String rowCode = rowCodes[rowIndex];
	            Row row = sheet.getRow(baseRow + rowIndex);

	            if (row == null) row = sheet.createRow(baseRow + rowIndex);

	            for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
	                String fieldName = rowCode + "_" + fieldSuffixes[colIndex];

	                // 👉 Direct mapping, don’t skip B
	                int excelColIndex = 11+colIndex;

	                Cell cell = row.createCell(excelColIndex);
	                try {
	                    Field field = BrrsMNosvosP4.class.getDeclaredField(fieldName);
	                    field.setAccessible(true);
	                    Object value = field.get(record);

	                    if (value == null || "N/A".equals(value.toString().trim())) {
	                        cell.setCellValue(""); // keep style, blank value
	                        cell.setCellStyle(textStyle);
	                        continue;
	                    }

	                    if (value instanceof BigDecimal) {
	                        cell.setCellValue(((BigDecimal) value).doubleValue());
	                        cell.setCellStyle(numberStyle);
	                    } else if (value instanceof String) {
	                        cell.setCellValue((String) value);
	                        cell.setCellStyle(textStyle);
	                    } else {
	                        cell.setCellValue(value.toString());
	                        cell.setCellStyle(textStyle);
	                    }
	                } catch (NoSuchFieldException | IllegalAccessException e) {
	                    cell.setCellValue("");
	                    cell.setCellStyle(textStyle);
	                    LoggerFactory.getLogger(getClass())
	                        .warn("Field not found or inaccessible: {}", fieldName);
	                }
	            }
	        }
	    }
	}
	
	
	public byte[] getExcelM_NOSVOSRESUB(String filename, String reportId, String fromdate, String todate, String currency,
            String dtltype, String type, BigDecimal version) throws Exception {
	logger.info("Service: Starting Excel generation process in memory.");

	System.out.println("came to excel download service");
	List<BrrsMNosvosP1ResbuSummaryEntity> dataList = BrrsMNosvosP1ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
	List<BrrsMNosvosP2ResbuSummaryEntity> dataList2 = BrrsMNosvosP2ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
	List<BrrsMNosvosP3ResbuSummaryEntity> dataList3 = BrrsMNosvosP3ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
	List<BrrsMNosvosP4ResbuSummaryEntity> dataList4 = BrrsMNosvosP4ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
	List<BrrsMNosvosP5ResbuSummaryEntity> dataList5 = BrrsMNosvosP5ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
	
	if (dataList.isEmpty()) {
	logger.warn("Service: No data found for M-NOSVOS report. Returning empty result.");
	return new byte[0];
	}
	if (dataList2.isEmpty()) {
	    logger.error("No data found for Entity2 - check query for date: {}", todate);
	}
	if (dataList3.isEmpty()) {
	    logger.error("No data found for Entity3 - check query for date: {}", todate);
	}
	if (dataList4.isEmpty()) {
	    logger.error("No data found for Entity4 - check query for date: {}", todate);
	}
	
	String templateDir = env.getProperty("output.exportpathtemp");
	Path templatePath = Paths.get(templateDir, filename);
	
	logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());
	
	if (!Files.exists(templatePath)) {
	throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
	}
	if (!Files.isReadable(templatePath)) {
	throw new SecurityException("Template file exists but is not readable: " + templatePath.toAbsolutePath());
	}
	
	try (InputStream templateInputStream = Files.newInputStream(templatePath);
	Workbook workbook = WorkbookFactory.create(templateInputStream);
	ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	
	Sheet sheet = workbook.getSheetAt(0);
	
	CreationHelper createHelper = workbook.getCreationHelper();
	
	Font font = workbook.createFont();
	font.setFontHeightInPoints((short) 8);
	font.setFontName("Arial");
	
	CellStyle textStyle = workbook.createCellStyle();
	textStyle.setBorderBottom(BorderStyle.THIN);
	textStyle.setBorderTop(BorderStyle.THIN);
	textStyle.setBorderLeft(BorderStyle.THIN);
	textStyle.setBorderRight(BorderStyle.THIN);
	textStyle.setFont(font);
	
	CellStyle numberStyle = workbook.createCellStyle();
	//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.000"));
	numberStyle.setBorderBottom(BorderStyle.THIN);
	numberStyle.setBorderTop(BorderStyle.THIN);
	numberStyle.setBorderLeft(BorderStyle.THIN);
	numberStyle.setBorderRight(BorderStyle.THIN);
	numberStyle.setFont(font);
	
if (dtltype.equals("email_report") ) {
	
	List<BrrsMNosvosP1ResbuSummaryEntity> dataListEmail = BrrsMNosvosP1ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
	List<BrrsMNosvosP2ResbuSummaryEntity> dataListEmail2 = BrrsMNosvosP2ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
	List<BrrsMNosvosP3ResbuSummaryEntity> dataListEmail3 = BrrsMNosvosP3ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
	List<BrrsMNosvosP4ResbuSummaryEntity> dataListEmail4 = BrrsMNosvosP4ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
	List<BrrsMNosvosP5ResbuSummaryEntity> dataListEmail5 = BrrsMNosvosP5ResbuSummaryRepo.getdatabydateListarchival(dateformat.parse(todate), version);
	
		
		String[] rowCodesPart1 = new String[101];
		
		for (int i = 1; i <= 101; i++) {
		    rowCodesPart1[i - 1] = "R" + i;
		}
	
	
		String[] fieldSuffixes = {
		"NAME_OF_BANK_AND_COUNTRY_NOSTRO","TYPE_OF_ACCOUNT_NOSTRO","PURPOSE_NOSTRO","CURRENCY_NOSTRO","SOVEREIGN_RATING_AAA_AA_A1_NOSTRO","RISK_WEIGHT_NOSTRO","AMOUNT_DEMAND_NOSTRO","RISK_WEIGHTED_AMOUNT_NOSTRO"   
		};
	
		String[] fieldSuffixes2 = {
		"NAME_OF_BANK_AND_COUNTRY_VOSTRO","TYPE_OF_ACCOUNT_VOSTRO","PURPOSE_VOSTRO","CURRENCY_VOSTRO","AMOUNT_DEMAND_VOSTRO","AMOUNT_TIME_VOSTRO"   
		};
	
		String[] fieldSuffixes3 = {
		"NAME_OF_BANK_NOSTRO1","TYPE_OF_ACCOUNT_NOSTRO1","PURPOSE_NOSTRO1","CURRENCY_NOSTRO1","SOVEREIGN_RATING_NOSTRO1","RISK_WEIGHT_NOSTRO1","AMOUNT_DEMAND_NOSTRO1","AMOUNT_TIME_NOSTRO1","RISK_WEIGHTED_AMOUNT_NOSTRO1"
		};
	
		String[] fieldSuffixes4 = {
		"NAME_OF_BANK_VOSTRO1","TYPE_OF_ACCOUNT_VOSTRO1","PURPOSE_VOSTRO1","CURRENCY_VOSTRO1","AMOUNT_DEMAND_VOSTRO1","AMOUNT_TIME_VOSTRO1"
		};
	
	
	
		// First set: R11 - R50 at row 11
		writeEmailResubExcelRowData1(sheet, dataListEmail, rowCodesPart1, fieldSuffixes, 10, numberStyle, textStyle);
		
//		// First set: R56 - R95 at row 56
		writeEmailResubExcelRowData2(sheet, dataListEmail2, rowCodesPart1, fieldSuffixes2, 10, numberStyle, textStyle);
//	
//		// Third Set: R101 - R141 at row 101
		writeEmailResubExcelRowData3(sheet, dataListEmail3, rowCodesPart1, fieldSuffixes3, 118, numberStyle, textStyle);
//	
//		// Fourth Set: R147 - R196 at row 146
		writeEmailResubExcelRowData4(sheet, dataListEmail4, rowCodesPart1, fieldSuffixes4, 118, numberStyle, textStyle);
		
		writeEmailResubExcelRowData5(sheet, dataListEmail5, rowCodesPart1, fieldSuffixes4, 118, numberStyle, textStyle);
		
		workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
		workbook.write(out);
		logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
		
		return out.toByteArray();
		
	}
	
	String[] rowCodesPart1 = new String[101];

	for (int i = 1; i <= 101; i++) {
	    rowCodesPart1[i - 1] = "R" + i;
	}

	String[] fieldSuffixes = {
			"NAME_OF_BANK_AND_COUNTRY_NOSTRO","TYPE_OF_ACCOUNT_NOSTRO","PURPOSE_NOSTRO","CURRENCY_NOSTRO","SOVEREIGN_RATING_AAA_AA_A1_NOSTRO","RISK_WEIGHT_NOSTRO","AMOUNT_DEMAND_NOSTRO","AMOUNT_TIME_NOSTRO","RISK_WEIGHTED_AMOUNT_NOSTRO"   
			};

			String[] fieldSuffixes2 = {
			"NAME_OF_BANK_AND_COUNTRY_VOSTRO","TYPE_OF_ACCOUNT_VOSTRO","PURPOSE_VOSTRO","CURRENCY_VOSTRO","AMOUNT_DEMAND_VOSTRO","AMOUNT_TIME_VOSTRO"   
			};

			String[] fieldSuffixes3 = {
			"NAME_OF_BANK_NOSTRO1","TYPE_OF_ACCOUNT_NOSTRO1","PURPOSE_NOSTRO1","CURRENCY_NOSTRO1","SOVEREIGN_RATING_NOSTRO1","RISK_WEIGHT_NOSTRO1","AMOUNT_DEMAND_NOSTRO1","AMOUNT_TIME_NOSTRO1","RISK_WEIGHTED_AMOUNT_NOSTRO1"
			};

			String[] fieldSuffixes4 = {
			"NAME_OF_BANK_VOSTRO1","TYPE_OF_ACCOUNT_VOSTRO1","PURPOSE_VOSTRO1","CURRENCY_VOSTRO1","AMOUNT_DEMAND_VOSTRO1","AMOUNT_TIME_VOSTRO1"
			};

	// First set: R11 - R50 at row 11
	writeRowDataResub01(sheet, dataList, rowCodesPart1, fieldSuffixes, 10, numberStyle, textStyle);
	
	// First set: R56 - R95 at row 56
	writeRowDataResub02(sheet, dataList2, rowCodesPart1, fieldSuffixes2, 10, numberStyle, textStyle);

	// Third Set: R101 - R141 at row 101
	writeRowDataResub03(sheet, dataList3, rowCodesPart1, fieldSuffixes3, 118, numberStyle, textStyle);

	// Fourth Set: R147 - R196 at row 146
	writeRowDataResub04(sheet, dataList4, rowCodesPart1, fieldSuffixes4, 118, numberStyle, textStyle);

	workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
	workbook.write(out);
	logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
	
	return out.toByteArray();
	}
}

	
	public byte[] getExcelM_NOSVOSARCHIVAL(String filename, String reportId, String fromdate, String todate, String currency,
            String dtltype, String type, BigDecimal version) throws Exception {
	logger.info("Service: Starting Excel generation process in memory.");

	System.out.println("came to excel download service");
	List<BrrsMNosvosP1Archival> dataList = BrrsMNosvosP1ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);
	List<BrrsMNosvosP2Archival> dataList2 = BrrsMNosvosP2ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);
	List<BrrsMNosvosP3Archival> dataList3 = BrrsMNosvosP3ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);
	List<BrrsMNosvosP4Archival> dataList4 = BrrsMNosvosP4ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);
	
	if (dataList.isEmpty()) {
	logger.warn("Service: No data found for M-NOSVOS report. Returning empty result.");
	return new byte[0];
	}
	if (dataList2.isEmpty()) {
	    logger.error("No data found for Entity2 - check query for date: {}", todate);
	}
	if (dataList3.isEmpty()) {
	    logger.error("No data found for Entity3 - check query for date: {}", todate);
	}
	if (dataList4.isEmpty()) {
	    logger.error("No data found for Entity4 - check query for date: {}", todate);
	}
	
	String templateDir = env.getProperty("output.exportpathtemp");
	Path templatePath = Paths.get(templateDir, filename);
	
	logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());
	
	if (!Files.exists(templatePath)) {
	throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
	}
	if (!Files.isReadable(templatePath)) {
	throw new SecurityException("Template file exists but is not readable: " + templatePath.toAbsolutePath());
	}
	
	try (InputStream templateInputStream = Files.newInputStream(templatePath);
	Workbook workbook = WorkbookFactory.create(templateInputStream);
	ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	
	Sheet sheet = workbook.getSheetAt(0);
	
	CreationHelper createHelper = workbook.getCreationHelper();
	
	Font font = workbook.createFont();
	font.setFontHeightInPoints((short) 8);
	font.setFontName("Arial");
	
	CellStyle textStyle = workbook.createCellStyle();
	textStyle.setBorderBottom(BorderStyle.THIN);
	textStyle.setBorderTop(BorderStyle.THIN);
	textStyle.setBorderLeft(BorderStyle.THIN);
	textStyle.setBorderRight(BorderStyle.THIN);
	textStyle.setFont(font);
	
	CellStyle numberStyle = workbook.createCellStyle();
	//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.000"));
	numberStyle.setBorderBottom(BorderStyle.THIN);
	numberStyle.setBorderTop(BorderStyle.THIN);
	numberStyle.setBorderLeft(BorderStyle.THIN);
	numberStyle.setBorderRight(BorderStyle.THIN);
	numberStyle.setFont(font);
	
if (dtltype.equals("email_report") ) {
	
	List<BrrsMNosvosP1Archival> dataListEmail = BrrsMNosvosP1ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate),version);
	List<BrrsMNosvosP2Archival> dataListEmail2 = BrrsMNosvosP2ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate),version);
	List<BrrsMNosvosP3Archival> dataListEmail3 = BrrsMNosvosP3ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate),version);
	List<BrrsMNosvosP4Archival> dataListEmail4 = BrrsMNosvosP4ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate),version);
	List<BrrsMNosvosP5Archival> dataListEmail5 = BrrsMNosvosP5ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate),version);
		
		String[] rowCodesPart1 = new String[101];
		
		for (int i = 1; i <= 101; i++) {
		    rowCodesPart1[i - 1] = "R" + i;
		}
	
	
		String[] fieldSuffixes = {
		"NAME_OF_BANK_AND_COUNTRY_NOSTRO","TYPE_OF_ACCOUNT_NOSTRO","PURPOSE_NOSTRO","CURRENCY_NOSTRO","SOVEREIGN_RATING_AAA_AA_A1_NOSTRO","RISK_WEIGHT_NOSTRO","AMOUNT_DEMAND_NOSTRO","RISK_WEIGHTED_AMOUNT_NOSTRO"   
		};
	
		String[] fieldSuffixes2 = {
		"NAME_OF_BANK_AND_COUNTRY_VOSTRO","TYPE_OF_ACCOUNT_VOSTRO","PURPOSE_VOSTRO","CURRENCY_VOSTRO","AMOUNT_DEMAND_VOSTRO","AMOUNT_TIME_VOSTRO"   
		};
	
		String[] fieldSuffixes3 = {
		"NAME_OF_BANK_NOSTRO1","TYPE_OF_ACCOUNT_NOSTRO1","PURPOSE_NOSTRO1","CURRENCY_NOSTRO1","SOVEREIGN_RATING_NOSTRO1","RISK_WEIGHT_NOSTRO1","AMOUNT_DEMAND_NOSTRO1","AMOUNT_TIME_NOSTRO1","RISK_WEIGHTED_AMOUNT_NOSTRO1"
		};
	
		String[] fieldSuffixes4 = {
		"NAME_OF_BANK_VOSTRO1","TYPE_OF_ACCOUNT_VOSTRO1","PURPOSE_VOSTRO1","CURRENCY_VOSTRO1","AMOUNT_DEMAND_VOSTRO1","AMOUNT_TIME_VOSTRO1"
		};
	
	
	
		// First set: R11 - R50 at row 11
		writeEmailArchExcelRowData1(sheet, dataListEmail, rowCodesPart1, fieldSuffixes, 10, numberStyle, textStyle);
		
//		// First set: R56 - R95 at row 56
		writeEmailArchExcelRowData2(sheet, dataListEmail2, rowCodesPart1, fieldSuffixes2, 10, numberStyle, textStyle);
//	
//		// Third Set: R101 - R141 at row 101
		writeEmailArchExcelRowData3(sheet, dataListEmail3, rowCodesPart1, fieldSuffixes3, 118, numberStyle, textStyle);
//	
//		// Fourth Set: R147 - R196 at row 146
		writeEmailArchExcelRowData4(sheet, dataListEmail4, rowCodesPart1, fieldSuffixes4, 118, numberStyle, textStyle);
		
		writeEmailArchExcelRowData5(sheet, dataListEmail5, rowCodesPart1, fieldSuffixes4, 118, numberStyle, textStyle);
		
		workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
		workbook.write(out);
		logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
		
		return out.toByteArray();
		
	}
	
	String[] rowCodesPart1 = new String[101];

	for (int i = 1; i <= 101; i++) {
	    rowCodesPart1[i - 1] = "R" + i;
	}

	String[] fieldSuffixes = {
			"NAME_OF_BANK_AND_COUNTRY_NOSTRO","TYPE_OF_ACCOUNT_NOSTRO","PURPOSE_NOSTRO","CURRENCY_NOSTRO","SOVEREIGN_RATING_AAA_AA_A1_NOSTRO","RISK_WEIGHT_NOSTRO","AMOUNT_DEMAND_NOSTRO","AMOUNT_TIME_NOSTRO","RISK_WEIGHTED_AMOUNT_NOSTRO"   
			};

			String[] fieldSuffixes2 = {
			"NAME_OF_BANK_AND_COUNTRY_VOSTRO","TYPE_OF_ACCOUNT_VOSTRO","PURPOSE_VOSTRO","CURRENCY_VOSTRO","AMOUNT_DEMAND_VOSTRO","AMOUNT_TIME_VOSTRO"   
			};

			String[] fieldSuffixes3 = {
			"NAME_OF_BANK_NOSTRO1","TYPE_OF_ACCOUNT_NOSTRO1","PURPOSE_NOSTRO1","CURRENCY_NOSTRO1","SOVEREIGN_RATING_NOSTRO1","RISK_WEIGHT_NOSTRO1","AMOUNT_DEMAND_NOSTRO1","AMOUNT_TIME_NOSTRO1","RISK_WEIGHTED_AMOUNT_NOSTRO1"
			};

			String[] fieldSuffixes4 = {
			"NAME_OF_BANK_VOSTRO1","TYPE_OF_ACCOUNT_VOSTRO1","PURPOSE_VOSTRO1","CURRENCY_VOSTRO1","AMOUNT_DEMAND_VOSTRO1","AMOUNT_TIME_VOSTRO1"
			};

	// First set: R11 - R50 at row 11
	writeRowData01(sheet, dataList, rowCodesPart1, fieldSuffixes, 10, numberStyle, textStyle);
	
	// First set: R56 - R95 at row 56
	writeRowData02(sheet, dataList2, rowCodesPart1, fieldSuffixes2, 10, numberStyle, textStyle);

	// Third Set: R101 - R141 at row 101
	writeRowData03(sheet, dataList3, rowCodesPart1, fieldSuffixes3, 118, numberStyle, textStyle);

	// Fourth Set: R147 - R196 at row 146
	writeRowData04(sheet, dataList4, rowCodesPart1, fieldSuffixes4, 118, numberStyle, textStyle);

	workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
	workbook.write(out);
	logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
	
	return out.toByteArray();
	}
}
	


	private void writeEmailResubExcelRowData5(Sheet sheet, List<BrrsMNosvosP5ResbuSummaryEntity> dataList,
	        String[] rowCodes, String[] fieldSuffixes, int baseRow,
	        CellStyle numberStyle, CellStyle textStyle) {
	    System.out.println("came to write row data 4 method");

	    System.out.println("came to write row data 1 method");
		
	    BrrsMNosvosP5ResbuSummaryEntity record1 = dataList.get(0);
	    
	    Row row = sheet.getRow(62) != null ? sheet.getRow(62) : sheet.createRow(62);
	    Cell cell1 = row.createCell(0);

	   if (record1.getR1_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR1_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell1.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell1.setCellStyle(numberStyle);
	   } else {
	       cell1.setCellValue("");
	       cell1.setCellStyle(textStyle);
	   }

	    Cell cell2 = row.createCell(1);

	   if (record1.getR1_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR1_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell2.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell2.setCellStyle(numberStyle);
	   } else {
	       cell2.setCellValue("");
	       cell2.setCellStyle(textStyle);
	   }

	    Cell cell3 = row.createCell(2);

	   if (record1.getR1_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR1_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell3.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell3.setCellStyle(numberStyle);
	   } else {
	       cell3.setCellValue("");
	       cell3.setCellStyle(textStyle);
	   }

	    Cell cell4 = row.createCell(3);

	   if (record1.getR1_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR1_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell4.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell4.setCellStyle(numberStyle);
	   } else {
	       cell4.setCellValue("");
	       cell4.setCellStyle(textStyle);
	   }

	    Cell cell5 = row.createCell(4);

	   if (record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell5.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell5.setCellStyle(numberStyle);
	   } else {
	       cell5.setCellValue("");
	       cell5.setCellStyle(textStyle);
	   }

	    Cell cell6 = row.createCell(5);

	   if (record1.getR1_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR1_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell6.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell6.setCellStyle(numberStyle);
	   } else {
	       cell6.setCellValue("");
	       cell6.setCellStyle(textStyle);
	   }

	    Cell cell7 = row.createCell(6);

	   if (record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell7.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell7.setCellStyle(numberStyle);
	   } else {
	       cell7.setCellValue("");
	       cell7.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(63) != null ? sheet.getRow(63) : sheet.createRow(63);
	    Cell cell8 = row.createCell(0);

	   if (record1.getR2_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR2_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell8.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell8.setCellStyle(numberStyle);
	   } else {
	       cell8.setCellValue("");
	       cell8.setCellStyle(textStyle);
	   }

	    Cell cell9 = row.createCell(1);

	   if (record1.getR2_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR2_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell9.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell9.setCellStyle(numberStyle);
	   } else {
	       cell9.setCellValue("");
	       cell9.setCellStyle(textStyle);
	   }

	    Cell cell10 = row.createCell(2);

	   if (record1.getR2_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR2_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell10.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell10.setCellStyle(numberStyle);
	   } else {
	       cell10.setCellValue("");
	       cell10.setCellStyle(textStyle);
	   }

	    Cell cell11 = row.createCell(3);

	   if (record1.getR2_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR2_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell11.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell11.setCellStyle(numberStyle);
	   } else {
	       cell11.setCellValue("");
	       cell11.setCellStyle(textStyle);
	   }

	    Cell cell12 = row.createCell(4);

	   if (record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell12.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell12.setCellStyle(numberStyle);
	   } else {
	       cell12.setCellValue("");
	       cell12.setCellStyle(textStyle);
	   }

	    Cell cell13 = row.createCell(5);

	   if (record1.getR2_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR2_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell13.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell13.setCellStyle(numberStyle);
	   } else {
	       cell13.setCellValue("");
	       cell13.setCellStyle(textStyle);
	   }

	    Cell cell14 = row.createCell(6);

	   if (record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell14.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell14.setCellStyle(numberStyle);
	   } else {
	       cell14.setCellValue("");
	       cell14.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(64) != null ? sheet.getRow(64) : sheet.createRow(64);
	    Cell cell15 = row.createCell(0);

	   if (record1.getR3_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR3_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell15.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell15.setCellStyle(numberStyle);
	   } else {
	       cell15.setCellValue("");
	       cell15.setCellStyle(textStyle);
	   }

	    Cell cell16 = row.createCell(1);

	   if (record1.getR3_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR3_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell16.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell16.setCellStyle(numberStyle);
	   } else {
	       cell16.setCellValue("");
	       cell16.setCellStyle(textStyle);
	   }

	    Cell cell17 = row.createCell(2);

	   if (record1.getR3_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR3_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell17.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell17.setCellStyle(numberStyle);
	   } else {
	       cell17.setCellValue("");
	       cell17.setCellStyle(textStyle);
	   }

	    Cell cell18 = row.createCell(3);

	   if (record1.getR3_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR3_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell18.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell18.setCellStyle(numberStyle);
	   } else {
	       cell18.setCellValue("");
	       cell18.setCellStyle(textStyle);
	   }

	    Cell cell19 = row.createCell(4);

	   if (record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell19.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell19.setCellStyle(numberStyle);
	   } else {
	       cell19.setCellValue("");
	       cell19.setCellStyle(textStyle);
	   }

	    Cell cell20 = row.createCell(5);

	   if (record1.getR3_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR3_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell20.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell20.setCellStyle(numberStyle);
	   } else {
	       cell20.setCellValue("");
	       cell20.setCellStyle(textStyle);
	   }

	    Cell cell21 = row.createCell(6);

	   if (record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell21.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell21.setCellStyle(numberStyle);
	   } else {
	       cell21.setCellValue("");
	       cell21.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(65) != null ? sheet.getRow(65) : sheet.createRow(65);
	    Cell cell22 = row.createCell(0);

	   if (record1.getR4_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR4_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell22.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell22.setCellStyle(numberStyle);
	   } else {
	       cell22.setCellValue("");
	       cell22.setCellStyle(textStyle);
	   }

	    Cell cell23 = row.createCell(1);

	   if (record1.getR4_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR4_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell23.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell23.setCellStyle(numberStyle);
	   } else {
	       cell23.setCellValue("");
	       cell23.setCellStyle(textStyle);
	   }

	    Cell cell24 = row.createCell(2);

	   if (record1.getR4_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR4_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell24.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell24.setCellStyle(numberStyle);
	   } else {
	       cell24.setCellValue("");
	       cell24.setCellStyle(textStyle);
	   }

	    Cell cell25 = row.createCell(3);

	   if (record1.getR4_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR4_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell25.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell25.setCellStyle(numberStyle);
	   } else {
	       cell25.setCellValue("");
	       cell25.setCellStyle(textStyle);
	   }

	    Cell cell26 = row.createCell(4);

	   if (record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell26.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell26.setCellStyle(numberStyle);
	   } else {
	       cell26.setCellValue("");
	       cell26.setCellStyle(textStyle);
	   }

	    Cell cell27 = row.createCell(5);

	   if (record1.getR4_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR4_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell27.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell27.setCellStyle(numberStyle);
	   } else {
	       cell27.setCellValue("");
	       cell27.setCellStyle(textStyle);
	   }

	    Cell cell28 = row.createCell(6);

	   if (record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell28.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell28.setCellStyle(numberStyle);
	   } else {
	       cell28.setCellValue("");
	       cell28.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(66) != null ? sheet.getRow(66) : sheet.createRow(66);
	    Cell cell29 = row.createCell(0);

	   if (record1.getR5_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR5_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell29.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell29.setCellStyle(numberStyle);
	   } else {
	       cell29.setCellValue("");
	       cell29.setCellStyle(textStyle);
	   }

	    Cell cell30 = row.createCell(1);

	   if (record1.getR5_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR5_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell30.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell30.setCellStyle(numberStyle);
	   } else {
	       cell30.setCellValue("");
	       cell30.setCellStyle(textStyle);
	   }

	    Cell cell31 = row.createCell(2);

	   if (record1.getR5_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR5_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell31.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell31.setCellStyle(numberStyle);
	   } else {
	       cell31.setCellValue("");
	       cell31.setCellStyle(textStyle);
	   }

	    Cell cell32 = row.createCell(3);

	   if (record1.getR5_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR5_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell32.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell32.setCellStyle(numberStyle);
	   } else {
	       cell32.setCellValue("");
	       cell32.setCellStyle(textStyle);
	   }

	    Cell cell33 = row.createCell(4);

	   if (record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell33.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell33.setCellStyle(numberStyle);
	   } else {
	       cell33.setCellValue("");
	       cell33.setCellStyle(textStyle);
	   }

	    Cell cell34 = row.createCell(5);

	   if (record1.getR5_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR5_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell34.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell34.setCellStyle(numberStyle);
	   } else {
	       cell34.setCellValue("");
	       cell34.setCellStyle(textStyle);
	   }

	    Cell cell35 = row.createCell(6);

	   if (record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell35.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell35.setCellStyle(numberStyle);
	   } else {
	       cell35.setCellValue("");
	       cell35.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(67) != null ? sheet.getRow(67) : sheet.createRow(67);
	    Cell cell36 = row.createCell(0);

	   if (record1.getR6_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR6_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell36.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell36.setCellStyle(numberStyle);
	   } else {
	       cell36.setCellValue("");
	       cell36.setCellStyle(textStyle);
	   }

	    Cell cell37 = row.createCell(1);

	   if (record1.getR6_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR6_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell37.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell37.setCellStyle(numberStyle);
	   } else {
	       cell37.setCellValue("");
	       cell37.setCellStyle(textStyle);
	   }

	    Cell cell38 = row.createCell(2);

	   if (record1.getR6_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR6_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell38.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell38.setCellStyle(numberStyle);
	   } else {
	       cell38.setCellValue("");
	       cell38.setCellStyle(textStyle);
	   }

	    Cell cell39 = row.createCell(3);

	   if (record1.getR6_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR6_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell39.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell39.setCellStyle(numberStyle);
	   } else {
	       cell39.setCellValue("");
	       cell39.setCellStyle(textStyle);
	   }

	    Cell cell40 = row.createCell(4);

	   if (record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell40.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell40.setCellStyle(numberStyle);
	   } else {
	       cell40.setCellValue("");
	       cell40.setCellStyle(textStyle);
	   }

	    Cell cell41 = row.createCell(5);

	   if (record1.getR6_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR6_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell41.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell41.setCellStyle(numberStyle);
	   } else {
	       cell41.setCellValue("");
	       cell41.setCellStyle(textStyle);
	   }

	    Cell cell42 = row.createCell(6);

	   if (record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell42.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell42.setCellStyle(numberStyle);
	   } else {
	       cell42.setCellValue("");
	       cell42.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(68) != null ? sheet.getRow(68) : sheet.createRow(68);
	    Cell cell43 = row.createCell(0);

	   if (record1.getR7_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR7_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell43.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell43.setCellStyle(numberStyle);
	   } else {
	       cell43.setCellValue("");
	       cell43.setCellStyle(textStyle);
	   }

	    Cell cell44 = row.createCell(1);

	   if (record1.getR7_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR7_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell44.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell44.setCellStyle(numberStyle);
	   } else {
	       cell44.setCellValue("");
	       cell44.setCellStyle(textStyle);
	   }

	    Cell cell45 = row.createCell(2);

	   if (record1.getR7_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR7_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell45.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell45.setCellStyle(numberStyle);
	   } else {
	       cell45.setCellValue("");
	       cell45.setCellStyle(textStyle);
	   }

	    Cell cell46 = row.createCell(3);

	   if (record1.getR7_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR7_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell46.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell46.setCellStyle(numberStyle);
	   } else {
	       cell46.setCellValue("");
	       cell46.setCellStyle(textStyle);
	   }

	    Cell cell47 = row.createCell(4);

	   if (record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell47.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell47.setCellStyle(numberStyle);
	   } else {
	       cell47.setCellValue("");
	       cell47.setCellStyle(textStyle);
	   }

	    Cell cell48 = row.createCell(5);

	   if (record1.getR7_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR7_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell48.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell48.setCellStyle(numberStyle);
	   } else {
	       cell48.setCellValue("");
	       cell48.setCellStyle(textStyle);
	   }

	    Cell cell49 = row.createCell(6);

	   if (record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell49.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell49.setCellStyle(numberStyle);
	   } else {
	       cell49.setCellValue("");
	       cell49.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(69) != null ? sheet.getRow(69) : sheet.createRow(69);
	    Cell cell50 = row.createCell(0);

	   if (record1.getR8_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR8_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell50.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell50.setCellStyle(numberStyle);
	   } else {
	       cell50.setCellValue("");
	       cell50.setCellStyle(textStyle);
	   }

	    Cell cell51 = row.createCell(1);

	   if (record1.getR8_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR8_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell51.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell51.setCellStyle(numberStyle);
	   } else {
	       cell51.setCellValue("");
	       cell51.setCellStyle(textStyle);
	   }

	    Cell cell52 = row.createCell(2);

	   if (record1.getR8_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR8_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell52.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell52.setCellStyle(numberStyle);
	   } else {
	       cell52.setCellValue("");
	       cell52.setCellStyle(textStyle);
	   }

	    Cell cell53 = row.createCell(3);

	   if (record1.getR8_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR8_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell53.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell53.setCellStyle(numberStyle);
	   } else {
	       cell53.setCellValue("");
	       cell53.setCellStyle(textStyle);
	   }

	    Cell cell54 = row.createCell(4);

	   if (record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell54.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell54.setCellStyle(numberStyle);
	   } else {
	       cell54.setCellValue("");
	       cell54.setCellStyle(textStyle);
	   }

	    Cell cell55 = row.createCell(5);

	   if (record1.getR8_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR8_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell55.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell55.setCellStyle(numberStyle);
	   } else {
	       cell55.setCellValue("");
	       cell55.setCellStyle(textStyle);
	   }

	    Cell cell56 = row.createCell(6);

	   if (record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell56.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell56.setCellStyle(numberStyle);
	   } else {
	       cell56.setCellValue("");
	       cell56.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(70) != null ? sheet.getRow(70) : sheet.createRow(70);
	    Cell cell57 = row.createCell(0);

	   if (record1.getR9_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR9_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell57.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell57.setCellStyle(numberStyle);
	   } else {
	       cell57.setCellValue("");
	       cell57.setCellStyle(textStyle);
	   }

	    Cell cell58 = row.createCell(1);

	   if (record1.getR9_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR9_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell58.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell58.setCellStyle(numberStyle);
	   } else {
	       cell58.setCellValue("");
	       cell58.setCellStyle(textStyle);
	   }

	    Cell cell59 = row.createCell(2);

	   if (record1.getR9_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR9_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell59.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell59.setCellStyle(numberStyle);
	   } else {
	       cell59.setCellValue("");
	       cell59.setCellStyle(textStyle);
	   }

	    Cell cell60 = row.createCell(3);

	   if (record1.getR9_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR9_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell60.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell60.setCellStyle(numberStyle);
	   } else {
	       cell60.setCellValue("");
	       cell60.setCellStyle(textStyle);
	   }

	    Cell cell61 = row.createCell(4);

	   if (record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell61.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell61.setCellStyle(numberStyle);
	   } else {
	       cell61.setCellValue("");
	       cell61.setCellStyle(textStyle);
	   }

	    Cell cell62 = row.createCell(5);

	   if (record1.getR9_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR9_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell62.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell62.setCellStyle(numberStyle);
	   } else {
	       cell62.setCellValue("");
	       cell62.setCellStyle(textStyle);
	   }

	    Cell cell63 = row.createCell(6);

	   if (record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell63.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell63.setCellStyle(numberStyle);
	   } else {
	       cell63.setCellValue("");
	       cell63.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(71) != null ? sheet.getRow(71) : sheet.createRow(71);
	    Cell cell64 = row.createCell(0);

	   if (record1.getR10_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR10_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell64.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell64.setCellStyle(numberStyle);
	   } else {
	       cell64.setCellValue("");
	       cell64.setCellStyle(textStyle);
	   }

	    Cell cell65 = row.createCell(1);

	   if (record1.getR10_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR10_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell65.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell65.setCellStyle(numberStyle);
	   } else {
	       cell65.setCellValue("");
	       cell65.setCellStyle(textStyle);
	   }

	    Cell cell66 = row.createCell(2);

	   if (record1.getR10_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR10_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell66.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell66.setCellStyle(numberStyle);
	   } else {
	       cell66.setCellValue("");
	       cell66.setCellStyle(textStyle);
	   }

	    Cell cell67 = row.createCell(3);

	   if (record1.getR10_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR10_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell67.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell67.setCellStyle(numberStyle);
	   } else {
	       cell67.setCellValue("");
	       cell67.setCellStyle(textStyle);
	   }

	    Cell cell68 = row.createCell(4);

	   if (record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell68.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell68.setCellStyle(numberStyle);
	   } else {
	       cell68.setCellValue("");
	       cell68.setCellStyle(textStyle);
	   }

	    Cell cell69 = row.createCell(5);

	   if (record1.getR10_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR10_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell69.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell69.setCellStyle(numberStyle);
	   } else {
	       cell69.setCellValue("");
	       cell69.setCellStyle(textStyle);
	   }

	    Cell cell70 = row.createCell(6);

	   if (record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell70.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell70.setCellStyle(numberStyle);
	   } else {
	       cell70.setCellValue("");
	       cell70.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(72) != null ? sheet.getRow(72) : sheet.createRow(72);
	    Cell cell71 = row.createCell(0);

	   if (record1.getR11_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR11_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell71.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell71.setCellStyle(numberStyle);
	   } else {
	       cell71.setCellValue("");
	       cell71.setCellStyle(textStyle);
	   }

	    Cell cell72 = row.createCell(1);

	   if (record1.getR11_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR11_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell72.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell72.setCellStyle(numberStyle);
	   } else {
	       cell72.setCellValue("");
	       cell72.setCellStyle(textStyle);
	   }

	    Cell cell73 = row.createCell(2);

	   if (record1.getR11_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR11_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell73.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell73.setCellStyle(numberStyle);
	   } else {
	       cell73.setCellValue("");
	       cell73.setCellStyle(textStyle);
	   }

	    Cell cell74 = row.createCell(3);

	   if (record1.getR11_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR11_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell74.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell74.setCellStyle(numberStyle);
	   } else {
	       cell74.setCellValue("");
	       cell74.setCellStyle(textStyle);
	   }

	    Cell cell75 = row.createCell(4);

	   if (record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell75.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell75.setCellStyle(numberStyle);
	   } else {
	       cell75.setCellValue("");
	       cell75.setCellStyle(textStyle);
	   }

	    Cell cell76 = row.createCell(5);

	   if (record1.getR11_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR11_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell76.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell76.setCellStyle(numberStyle);
	   } else {
	       cell76.setCellValue("");
	       cell76.setCellStyle(textStyle);
	   }

	    Cell cell77 = row.createCell(6);

	   if (record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell77.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell77.setCellStyle(numberStyle);
	   } else {
	       cell77.setCellValue("");
	       cell77.setCellStyle(textStyle);
	   }

	   row = sheet.getRow(77) != null ? sheet.getRow(77) : sheet.createRow(77);
	   Cell cell78 = row.createCell(0);

	  if (record1.getR1_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR1_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell78.setCellValue(record1.getR1_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell78.setCellStyle(numberStyle);
	  } else {
	      cell78.setCellValue("");
	      cell78.setCellStyle(textStyle);
	  }

	   Cell cell79 = row.createCell(1);

	  if (record1.getR1_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR1_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell79.setCellValue(record1.getR1_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell79.setCellStyle(numberStyle);
	  } else {
	      cell79.setCellValue("");
	      cell79.setCellStyle(textStyle);
	  }

	   Cell cell80 = row.createCell(2);

	  if (record1.getR1_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR1_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell80.setCellValue(record1.getR1_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell80.setCellStyle(numberStyle);
	  } else {
	      cell80.setCellValue("");
	      cell80.setCellStyle(textStyle);
	  }

	   Cell cell81 = row.createCell(3);

	  if (record1.getR1_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR1_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell81.setCellValue(record1.getR1_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell81.setCellStyle(numberStyle);
	  } else {
	      cell81.setCellValue("");
	      cell81.setCellStyle(textStyle);
	  }

	   Cell cell82 = row.createCell(4);

	  if (record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell82.setCellValue(record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell82.setCellStyle(numberStyle);
	  } else {
	      cell82.setCellValue("");
	      cell82.setCellStyle(textStyle);
	  }

	   Cell cell83 = row.createCell(5);

	  if (record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell83.setCellValue(record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell83.setCellStyle(numberStyle);
	  } else {
	      cell83.setCellValue("");
	      cell83.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(78) != null ? sheet.getRow(78) : sheet.createRow(78);
	   Cell cell84 = row.createCell(0);

	  if (record1.getR2_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR2_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell84.setCellValue(record1.getR2_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell84.setCellStyle(numberStyle);
	  } else {
	      cell84.setCellValue("");
	      cell84.setCellStyle(textStyle);
	  }

	   Cell cell85 = row.createCell(1);

	  if (record1.getR2_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR2_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell85.setCellValue(record1.getR2_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell85.setCellStyle(numberStyle);
	  } else {
	      cell85.setCellValue("");
	      cell85.setCellStyle(textStyle);
	  }

	   Cell cell86 = row.createCell(2);

	  if (record1.getR2_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR2_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell86.setCellValue(record1.getR2_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell86.setCellStyle(numberStyle);
	  } else {
	      cell86.setCellValue("");
	      cell86.setCellStyle(textStyle);
	  }

	   Cell cell87 = row.createCell(3);

	  if (record1.getR2_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR2_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell87.setCellValue(record1.getR2_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell87.setCellStyle(numberStyle);
	  } else {
	      cell87.setCellValue("");
	      cell87.setCellStyle(textStyle);
	  }

	   Cell cell88 = row.createCell(4);

	  if (record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell88.setCellValue(record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell88.setCellStyle(numberStyle);
	  } else {
	      cell88.setCellValue("");
	      cell88.setCellStyle(textStyle);
	  }

	   Cell cell89 = row.createCell(5);

	  if (record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell89.setCellValue(record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell89.setCellStyle(numberStyle);
	  } else {
	      cell89.setCellValue("");
	      cell89.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(79) != null ? sheet.getRow(79) : sheet.createRow(79);
	   Cell cell90 = row.createCell(0);

	  if (record1.getR3_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR3_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell90.setCellValue(record1.getR3_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell90.setCellStyle(numberStyle);
	  } else {
	      cell90.setCellValue("");
	      cell90.setCellStyle(textStyle);
	  }

	   Cell cell91 = row.createCell(1);

	  if (record1.getR3_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR3_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell91.setCellValue(record1.getR3_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell91.setCellStyle(numberStyle);
	  } else {
	      cell91.setCellValue("");
	      cell91.setCellStyle(textStyle);
	  }

	   Cell cell92 = row.createCell(2);

	  if (record1.getR3_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR3_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell92.setCellValue(record1.getR3_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell92.setCellStyle(numberStyle);
	  } else {
	      cell92.setCellValue("");
	      cell92.setCellStyle(textStyle);
	  }

	   Cell cell93 = row.createCell(3);

	  if (record1.getR3_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR3_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell93.setCellValue(record1.getR3_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell93.setCellStyle(numberStyle);
	  } else {
	      cell93.setCellValue("");
	      cell93.setCellStyle(textStyle);
	  }

	   Cell cell94 = row.createCell(4);

	  if (record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell94.setCellValue(record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell94.setCellStyle(numberStyle);
	  } else {
	      cell94.setCellValue("");
	      cell94.setCellStyle(textStyle);
	  }

	   Cell cell95 = row.createCell(5);

	  if (record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell95.setCellValue(record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell95.setCellStyle(numberStyle);
	  } else {
	      cell95.setCellValue("");
	      cell95.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(80) != null ? sheet.getRow(80) : sheet.createRow(80);
	   Cell cell96 = row.createCell(0);

	  if (record1.getR4_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR4_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell96.setCellValue(record1.getR4_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell96.setCellStyle(numberStyle);
	  } else {
	      cell96.setCellValue("");
	      cell96.setCellStyle(textStyle);
	  }

	   Cell cell97 = row.createCell(1);

	  if (record1.getR4_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR4_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell97.setCellValue(record1.getR4_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell97.setCellStyle(numberStyle);
	  } else {
	      cell97.setCellValue("");
	      cell97.setCellStyle(textStyle);
	  }

	   Cell cell98 = row.createCell(2);

	  if (record1.getR4_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR4_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell98.setCellValue(record1.getR4_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell98.setCellStyle(numberStyle);
	  } else {
	      cell98.setCellValue("");
	      cell98.setCellStyle(textStyle);
	  }

	   Cell cell99 = row.createCell(3);

	  if (record1.getR4_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR4_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell99.setCellValue(record1.getR4_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell99.setCellStyle(numberStyle);
	  } else {
	      cell99.setCellValue("");
	      cell99.setCellStyle(textStyle);
	  }

	   Cell cell100 = row.createCell(4);

	  if (record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell100.setCellValue(record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell100.setCellStyle(numberStyle);
	  } else {
	      cell100.setCellValue("");
	      cell100.setCellStyle(textStyle);
	  }

	   Cell cell101 = row.createCell(5);

	  if (record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell101.setCellValue(record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell101.setCellStyle(numberStyle);
	  } else {
	      cell101.setCellValue("");
	      cell101.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(81) != null ? sheet.getRow(81) : sheet.createRow(81);
	   Cell cell102 = row.createCell(0);

	  if (record1.getR5_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR5_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell102.setCellValue(record1.getR5_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell102.setCellStyle(numberStyle);
	  } else {
	      cell102.setCellValue("");
	      cell102.setCellStyle(textStyle);
	  }

	   Cell cell103 = row.createCell(1);

	  if (record1.getR5_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR5_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell103.setCellValue(record1.getR5_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell103.setCellStyle(numberStyle);
	  } else {
	      cell103.setCellValue("");
	      cell103.setCellStyle(textStyle);
	  }

	   Cell cell104 = row.createCell(2);

	  if (record1.getR5_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR5_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell104.setCellValue(record1.getR5_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell104.setCellStyle(numberStyle);
	  } else {
	      cell104.setCellValue("");
	      cell104.setCellStyle(textStyle);
	  }

	   Cell cell105 = row.createCell(3);

	  if (record1.getR5_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR5_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell105.setCellValue(record1.getR5_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell105.setCellStyle(numberStyle);
	  } else {
	      cell105.setCellValue("");
	      cell105.setCellStyle(textStyle);
	  }

	   Cell cell106 = row.createCell(4);

	  if (record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell106.setCellValue(record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell106.setCellStyle(numberStyle);
	  } else {
	      cell106.setCellValue("");
	      cell106.setCellStyle(textStyle);
	  }

	   Cell cell107 = row.createCell(5);

	  if (record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell107.setCellValue(record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell107.setCellStyle(numberStyle);
	  } else {
	      cell107.setCellValue("");
	      cell107.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(82) != null ? sheet.getRow(82) : sheet.createRow(82);
	   Cell cell108 = row.createCell(0);

	  if (record1.getR6_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR6_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell108.setCellValue(record1.getR6_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell108.setCellStyle(numberStyle);
	  } else {
	      cell108.setCellValue("");
	      cell108.setCellStyle(textStyle);
	  }

	   Cell cell109 = row.createCell(1);

	  if (record1.getR6_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR6_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell109.setCellValue(record1.getR6_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell109.setCellStyle(numberStyle);
	  } else {
	      cell109.setCellValue("");
	      cell109.setCellStyle(textStyle);
	  }

	   Cell cell110 = row.createCell(2);

	  if (record1.getR6_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR6_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell110.setCellValue(record1.getR6_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell110.setCellStyle(numberStyle);
	  } else {
	      cell110.setCellValue("");
	      cell110.setCellStyle(textStyle);
	  }

	   Cell cell111 = row.createCell(3);

	  if (record1.getR6_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR6_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell111.setCellValue(record1.getR6_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell111.setCellStyle(numberStyle);
	  } else {
	      cell111.setCellValue("");
	      cell111.setCellStyle(textStyle);
	  }

	   Cell cell112 = row.createCell(4);

	  if (record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell112.setCellValue(record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell112.setCellStyle(numberStyle);
	  } else {
	      cell112.setCellValue("");
	      cell112.setCellStyle(textStyle);
	  }

	   Cell cell113 = row.createCell(5);

	  if (record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell113.setCellValue(record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell113.setCellStyle(numberStyle);
	  } else {
	      cell113.setCellValue("");
	      cell113.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(83) != null ? sheet.getRow(83) : sheet.createRow(83);
	   Cell cell114 = row.createCell(0);

	  if (record1.getR7_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR7_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell114.setCellValue(record1.getR7_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell114.setCellStyle(numberStyle);
	  } else {
	      cell114.setCellValue("");
	      cell114.setCellStyle(textStyle);
	  }

	   Cell cell115 = row.createCell(1);

	  if (record1.getR7_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR7_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell115.setCellValue(record1.getR7_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell115.setCellStyle(numberStyle);
	  } else {
	      cell115.setCellValue("");
	      cell115.setCellStyle(textStyle);
	  }

	   Cell cell116 = row.createCell(2);

	  if (record1.getR7_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR7_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell116.setCellValue(record1.getR7_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell116.setCellStyle(numberStyle);
	  } else {
	      cell116.setCellValue("");
	      cell116.setCellStyle(textStyle);
	  }

	   Cell cell117 = row.createCell(3);

	  if (record1.getR7_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR7_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell117.setCellValue(record1.getR7_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell117.setCellStyle(numberStyle);
	  } else {
	      cell117.setCellValue("");
	      cell117.setCellStyle(textStyle);
	  }

	   Cell cell118 = row.createCell(4);

	  if (record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell118.setCellValue(record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell118.setCellStyle(numberStyle);
	  } else {
	      cell118.setCellValue("");
	      cell118.setCellStyle(textStyle);
	  }

	   Cell cell119 = row.createCell(5);

	  if (record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell119.setCellValue(record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell119.setCellStyle(numberStyle);
	  } else {
	      cell119.setCellValue("");
	      cell119.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(84) != null ? sheet.getRow(84) : sheet.createRow(84);
	   Cell cell120 = row.createCell(0);

	  if (record1.getR8_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR8_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell120.setCellValue(record1.getR8_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell120.setCellStyle(numberStyle);
	  } else {
	      cell120.setCellValue("");
	      cell120.setCellStyle(textStyle);
	  }

	   Cell cell121 = row.createCell(1);

	  if (record1.getR8_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR8_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell121.setCellValue(record1.getR8_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell121.setCellStyle(numberStyle);
	  } else {
	      cell121.setCellValue("");
	      cell121.setCellStyle(textStyle);
	  }

	   Cell cell122 = row.createCell(2);

	  if (record1.getR8_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR8_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell122.setCellValue(record1.getR8_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell122.setCellStyle(numberStyle);
	  } else {
	      cell122.setCellValue("");
	      cell122.setCellStyle(textStyle);
	  }

	   Cell cell123 = row.createCell(3);

	  if (record1.getR8_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR8_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell123.setCellValue(record1.getR8_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell123.setCellStyle(numberStyle);
	  } else {
	      cell123.setCellValue("");
	      cell123.setCellStyle(textStyle);
	  }

	   Cell cell124 = row.createCell(4);

	  if (record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell124.setCellValue(record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell124.setCellStyle(numberStyle);
	  } else {
	      cell124.setCellValue("");
	      cell124.setCellStyle(textStyle);
	  }

	   Cell cell125 = row.createCell(5);

	  if (record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell125.setCellValue(record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell125.setCellStyle(numberStyle);
	  } else {
	      cell125.setCellValue("");
	      cell125.setCellStyle(textStyle);
	  }

	}

	private void writeEmailArchExcelRowData5(Sheet sheet, List<BrrsMNosvosP5Archival> dataList,
	        String[] rowCodes, String[] fieldSuffixes, int baseRow,
	        CellStyle numberStyle, CellStyle textStyle) {
	    System.out.println("came to write row data 4 method");

	    System.out.println("came to write row data 1 method");
		
	    BrrsMNosvosP5Archival record1 = dataList.get(0);
	    
	    Row row = sheet.getRow(62) != null ? sheet.getRow(62) : sheet.createRow(62);
	    Cell cell1 = row.createCell(0);

	   if (record1.getR1_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR1_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell1.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell1.setCellStyle(numberStyle);
	   } else {
	       cell1.setCellValue("");
	       cell1.setCellStyle(textStyle);
	   }

	    Cell cell2 = row.createCell(1);

	   if (record1.getR1_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR1_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell2.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell2.setCellStyle(numberStyle);
	   } else {
	       cell2.setCellValue("");
	       cell2.setCellStyle(textStyle);
	   }

	    Cell cell3 = row.createCell(2);

	   if (record1.getR1_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR1_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell3.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell3.setCellStyle(numberStyle);
	   } else {
	       cell3.setCellValue("");
	       cell3.setCellStyle(textStyle);
	   }

	    Cell cell4 = row.createCell(3);

	   if (record1.getR1_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR1_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell4.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell4.setCellStyle(numberStyle);
	   } else {
	       cell4.setCellValue("");
	       cell4.setCellStyle(textStyle);
	   }

	    Cell cell5 = row.createCell(4);

	   if (record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell5.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell5.setCellStyle(numberStyle);
	   } else {
	       cell5.setCellValue("");
	       cell5.setCellStyle(textStyle);
	   }

	    Cell cell6 = row.createCell(5);

	   if (record1.getR1_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR1_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell6.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell6.setCellStyle(numberStyle);
	   } else {
	       cell6.setCellValue("");
	       cell6.setCellStyle(textStyle);
	   }

	    Cell cell7 = row.createCell(6);

	   if (record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell7.setCellValue(record1.getR1_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell7.setCellStyle(numberStyle);
	   } else {
	       cell7.setCellValue("");
	       cell7.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(63) != null ? sheet.getRow(63) : sheet.createRow(63);
	    Cell cell8 = row.createCell(0);

	   if (record1.getR2_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR2_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell8.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell8.setCellStyle(numberStyle);
	   } else {
	       cell8.setCellValue("");
	       cell8.setCellStyle(textStyle);
	   }

	    Cell cell9 = row.createCell(1);

	   if (record1.getR2_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR2_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell9.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell9.setCellStyle(numberStyle);
	   } else {
	       cell9.setCellValue("");
	       cell9.setCellStyle(textStyle);
	   }

	    Cell cell10 = row.createCell(2);

	   if (record1.getR2_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR2_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell10.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell10.setCellStyle(numberStyle);
	   } else {
	       cell10.setCellValue("");
	       cell10.setCellStyle(textStyle);
	   }

	    Cell cell11 = row.createCell(3);

	   if (record1.getR2_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR2_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell11.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell11.setCellStyle(numberStyle);
	   } else {
	       cell11.setCellValue("");
	       cell11.setCellStyle(textStyle);
	   }

	    Cell cell12 = row.createCell(4);

	   if (record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell12.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell12.setCellStyle(numberStyle);
	   } else {
	       cell12.setCellValue("");
	       cell12.setCellStyle(textStyle);
	   }

	    Cell cell13 = row.createCell(5);

	   if (record1.getR2_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR2_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell13.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell13.setCellStyle(numberStyle);
	   } else {
	       cell13.setCellValue("");
	       cell13.setCellStyle(textStyle);
	   }

	    Cell cell14 = row.createCell(6);

	   if (record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell14.setCellValue(record1.getR2_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell14.setCellStyle(numberStyle);
	   } else {
	       cell14.setCellValue("");
	       cell14.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(64) != null ? sheet.getRow(64) : sheet.createRow(64);
	    Cell cell15 = row.createCell(0);

	   if (record1.getR3_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR3_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell15.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell15.setCellStyle(numberStyle);
	   } else {
	       cell15.setCellValue("");
	       cell15.setCellStyle(textStyle);
	   }

	    Cell cell16 = row.createCell(1);

	   if (record1.getR3_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR3_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell16.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell16.setCellStyle(numberStyle);
	   } else {
	       cell16.setCellValue("");
	       cell16.setCellStyle(textStyle);
	   }

	    Cell cell17 = row.createCell(2);

	   if (record1.getR3_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR3_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell17.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell17.setCellStyle(numberStyle);
	   } else {
	       cell17.setCellValue("");
	       cell17.setCellStyle(textStyle);
	   }

	    Cell cell18 = row.createCell(3);

	   if (record1.getR3_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR3_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell18.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell18.setCellStyle(numberStyle);
	   } else {
	       cell18.setCellValue("");
	       cell18.setCellStyle(textStyle);
	   }

	    Cell cell19 = row.createCell(4);

	   if (record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell19.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell19.setCellStyle(numberStyle);
	   } else {
	       cell19.setCellValue("");
	       cell19.setCellStyle(textStyle);
	   }

	    Cell cell20 = row.createCell(5);

	   if (record1.getR3_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR3_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell20.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell20.setCellStyle(numberStyle);
	   } else {
	       cell20.setCellValue("");
	       cell20.setCellStyle(textStyle);
	   }

	    Cell cell21 = row.createCell(6);

	   if (record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell21.setCellValue(record1.getR3_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell21.setCellStyle(numberStyle);
	   } else {
	       cell21.setCellValue("");
	       cell21.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(65) != null ? sheet.getRow(65) : sheet.createRow(65);
	    Cell cell22 = row.createCell(0);

	   if (record1.getR4_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR4_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell22.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell22.setCellStyle(numberStyle);
	   } else {
	       cell22.setCellValue("");
	       cell22.setCellStyle(textStyle);
	   }

	    Cell cell23 = row.createCell(1);

	   if (record1.getR4_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR4_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell23.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell23.setCellStyle(numberStyle);
	   } else {
	       cell23.setCellValue("");
	       cell23.setCellStyle(textStyle);
	   }

	    Cell cell24 = row.createCell(2);

	   if (record1.getR4_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR4_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell24.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell24.setCellStyle(numberStyle);
	   } else {
	       cell24.setCellValue("");
	       cell24.setCellStyle(textStyle);
	   }

	    Cell cell25 = row.createCell(3);

	   if (record1.getR4_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR4_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell25.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell25.setCellStyle(numberStyle);
	   } else {
	       cell25.setCellValue("");
	       cell25.setCellStyle(textStyle);
	   }

	    Cell cell26 = row.createCell(4);

	   if (record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell26.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell26.setCellStyle(numberStyle);
	   } else {
	       cell26.setCellValue("");
	       cell26.setCellStyle(textStyle);
	   }

	    Cell cell27 = row.createCell(5);

	   if (record1.getR4_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR4_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell27.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell27.setCellStyle(numberStyle);
	   } else {
	       cell27.setCellValue("");
	       cell27.setCellStyle(textStyle);
	   }

	    Cell cell28 = row.createCell(6);

	   if (record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell28.setCellValue(record1.getR4_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell28.setCellStyle(numberStyle);
	   } else {
	       cell28.setCellValue("");
	       cell28.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(66) != null ? sheet.getRow(66) : sheet.createRow(66);
	    Cell cell29 = row.createCell(0);

	   if (record1.getR5_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR5_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell29.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell29.setCellStyle(numberStyle);
	   } else {
	       cell29.setCellValue("");
	       cell29.setCellStyle(textStyle);
	   }

	    Cell cell30 = row.createCell(1);

	   if (record1.getR5_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR5_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell30.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell30.setCellStyle(numberStyle);
	   } else {
	       cell30.setCellValue("");
	       cell30.setCellStyle(textStyle);
	   }

	    Cell cell31 = row.createCell(2);

	   if (record1.getR5_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR5_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell31.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell31.setCellStyle(numberStyle);
	   } else {
	       cell31.setCellValue("");
	       cell31.setCellStyle(textStyle);
	   }

	    Cell cell32 = row.createCell(3);

	   if (record1.getR5_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR5_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell32.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell32.setCellStyle(numberStyle);
	   } else {
	       cell32.setCellValue("");
	       cell32.setCellStyle(textStyle);
	   }

	    Cell cell33 = row.createCell(4);

	   if (record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell33.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell33.setCellStyle(numberStyle);
	   } else {
	       cell33.setCellValue("");
	       cell33.setCellStyle(textStyle);
	   }

	    Cell cell34 = row.createCell(5);

	   if (record1.getR5_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR5_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell34.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell34.setCellStyle(numberStyle);
	   } else {
	       cell34.setCellValue("");
	       cell34.setCellStyle(textStyle);
	   }

	    Cell cell35 = row.createCell(6);

	   if (record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell35.setCellValue(record1.getR5_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell35.setCellStyle(numberStyle);
	   } else {
	       cell35.setCellValue("");
	       cell35.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(67) != null ? sheet.getRow(67) : sheet.createRow(67);
	    Cell cell36 = row.createCell(0);

	   if (record1.getR6_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR6_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell36.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell36.setCellStyle(numberStyle);
	   } else {
	       cell36.setCellValue("");
	       cell36.setCellStyle(textStyle);
	   }

	    Cell cell37 = row.createCell(1);

	   if (record1.getR6_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR6_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell37.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell37.setCellStyle(numberStyle);
	   } else {
	       cell37.setCellValue("");
	       cell37.setCellStyle(textStyle);
	   }

	    Cell cell38 = row.createCell(2);

	   if (record1.getR6_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR6_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell38.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell38.setCellStyle(numberStyle);
	   } else {
	       cell38.setCellValue("");
	       cell38.setCellStyle(textStyle);
	   }

	    Cell cell39 = row.createCell(3);

	   if (record1.getR6_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR6_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell39.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell39.setCellStyle(numberStyle);
	   } else {
	       cell39.setCellValue("");
	       cell39.setCellStyle(textStyle);
	   }

	    Cell cell40 = row.createCell(4);

	   if (record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell40.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell40.setCellStyle(numberStyle);
	   } else {
	       cell40.setCellValue("");
	       cell40.setCellStyle(textStyle);
	   }

	    Cell cell41 = row.createCell(5);

	   if (record1.getR6_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR6_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell41.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell41.setCellStyle(numberStyle);
	   } else {
	       cell41.setCellValue("");
	       cell41.setCellStyle(textStyle);
	   }

	    Cell cell42 = row.createCell(6);

	   if (record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell42.setCellValue(record1.getR6_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell42.setCellStyle(numberStyle);
	   } else {
	       cell42.setCellValue("");
	       cell42.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(68) != null ? sheet.getRow(68) : sheet.createRow(68);
	    Cell cell43 = row.createCell(0);

	   if (record1.getR7_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR7_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell43.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell43.setCellStyle(numberStyle);
	   } else {
	       cell43.setCellValue("");
	       cell43.setCellStyle(textStyle);
	   }

	    Cell cell44 = row.createCell(1);

	   if (record1.getR7_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR7_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell44.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell44.setCellStyle(numberStyle);
	   } else {
	       cell44.setCellValue("");
	       cell44.setCellStyle(textStyle);
	   }

	    Cell cell45 = row.createCell(2);

	   if (record1.getR7_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR7_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell45.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell45.setCellStyle(numberStyle);
	   } else {
	       cell45.setCellValue("");
	       cell45.setCellStyle(textStyle);
	   }

	    Cell cell46 = row.createCell(3);

	   if (record1.getR7_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR7_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell46.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell46.setCellStyle(numberStyle);
	   } else {
	       cell46.setCellValue("");
	       cell46.setCellStyle(textStyle);
	   }

	    Cell cell47 = row.createCell(4);

	   if (record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell47.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell47.setCellStyle(numberStyle);
	   } else {
	       cell47.setCellValue("");
	       cell47.setCellStyle(textStyle);
	   }

	    Cell cell48 = row.createCell(5);

	   if (record1.getR7_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR7_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell48.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell48.setCellStyle(numberStyle);
	   } else {
	       cell48.setCellValue("");
	       cell48.setCellStyle(textStyle);
	   }

	    Cell cell49 = row.createCell(6);

	   if (record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell49.setCellValue(record1.getR7_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell49.setCellStyle(numberStyle);
	   } else {
	       cell49.setCellValue("");
	       cell49.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(69) != null ? sheet.getRow(69) : sheet.createRow(69);
	    Cell cell50 = row.createCell(0);

	   if (record1.getR8_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR8_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell50.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell50.setCellStyle(numberStyle);
	   } else {
	       cell50.setCellValue("");
	       cell50.setCellStyle(textStyle);
	   }

	    Cell cell51 = row.createCell(1);

	   if (record1.getR8_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR8_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell51.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell51.setCellStyle(numberStyle);
	   } else {
	       cell51.setCellValue("");
	       cell51.setCellStyle(textStyle);
	   }

	    Cell cell52 = row.createCell(2);

	   if (record1.getR8_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR8_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell52.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell52.setCellStyle(numberStyle);
	   } else {
	       cell52.setCellValue("");
	       cell52.setCellStyle(textStyle);
	   }

	    Cell cell53 = row.createCell(3);

	   if (record1.getR8_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR8_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell53.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell53.setCellStyle(numberStyle);
	   } else {
	       cell53.setCellValue("");
	       cell53.setCellStyle(textStyle);
	   }

	    Cell cell54 = row.createCell(4);

	   if (record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell54.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell54.setCellStyle(numberStyle);
	   } else {
	       cell54.setCellValue("");
	       cell54.setCellStyle(textStyle);
	   }

	    Cell cell55 = row.createCell(5);

	   if (record1.getR8_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR8_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell55.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell55.setCellStyle(numberStyle);
	   } else {
	       cell55.setCellValue("");
	       cell55.setCellStyle(textStyle);
	   }

	    Cell cell56 = row.createCell(6);

	   if (record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell56.setCellValue(record1.getR8_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell56.setCellStyle(numberStyle);
	   } else {
	       cell56.setCellValue("");
	       cell56.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(70) != null ? sheet.getRow(70) : sheet.createRow(70);
	    Cell cell57 = row.createCell(0);

	   if (record1.getR9_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR9_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell57.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell57.setCellStyle(numberStyle);
	   } else {
	       cell57.setCellValue("");
	       cell57.setCellStyle(textStyle);
	   }

	    Cell cell58 = row.createCell(1);

	   if (record1.getR9_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR9_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell58.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell58.setCellStyle(numberStyle);
	   } else {
	       cell58.setCellValue("");
	       cell58.setCellStyle(textStyle);
	   }

	    Cell cell59 = row.createCell(2);

	   if (record1.getR9_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR9_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell59.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell59.setCellStyle(numberStyle);
	   } else {
	       cell59.setCellValue("");
	       cell59.setCellStyle(textStyle);
	   }

	    Cell cell60 = row.createCell(3);

	   if (record1.getR9_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR9_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell60.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell60.setCellStyle(numberStyle);
	   } else {
	       cell60.setCellValue("");
	       cell60.setCellStyle(textStyle);
	   }

	    Cell cell61 = row.createCell(4);

	   if (record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell61.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell61.setCellStyle(numberStyle);
	   } else {
	       cell61.setCellValue("");
	       cell61.setCellStyle(textStyle);
	   }

	    Cell cell62 = row.createCell(5);

	   if (record1.getR9_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR9_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell62.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell62.setCellStyle(numberStyle);
	   } else {
	       cell62.setCellValue("");
	       cell62.setCellStyle(textStyle);
	   }

	    Cell cell63 = row.createCell(6);

	   if (record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell63.setCellValue(record1.getR9_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell63.setCellStyle(numberStyle);
	   } else {
	       cell63.setCellValue("");
	       cell63.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(71) != null ? sheet.getRow(71) : sheet.createRow(71);
	    Cell cell64 = row.createCell(0);

	   if (record1.getR10_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR10_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell64.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell64.setCellStyle(numberStyle);
	   } else {
	       cell64.setCellValue("");
	       cell64.setCellStyle(textStyle);
	   }

	    Cell cell65 = row.createCell(1);

	   if (record1.getR10_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR10_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell65.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell65.setCellStyle(numberStyle);
	   } else {
	       cell65.setCellValue("");
	       cell65.setCellStyle(textStyle);
	   }

	    Cell cell66 = row.createCell(2);

	   if (record1.getR10_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR10_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell66.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell66.setCellStyle(numberStyle);
	   } else {
	       cell66.setCellValue("");
	       cell66.setCellStyle(textStyle);
	   }

	    Cell cell67 = row.createCell(3);

	   if (record1.getR10_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR10_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell67.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell67.setCellStyle(numberStyle);
	   } else {
	       cell67.setCellValue("");
	       cell67.setCellStyle(textStyle);
	   }

	    Cell cell68 = row.createCell(4);

	   if (record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell68.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell68.setCellStyle(numberStyle);
	   } else {
	       cell68.setCellValue("");
	       cell68.setCellStyle(textStyle);
	   }

	    Cell cell69 = row.createCell(5);

	   if (record1.getR10_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR10_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell69.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell69.setCellStyle(numberStyle);
	   } else {
	       cell69.setCellValue("");
	       cell69.setCellStyle(textStyle);
	   }

	    Cell cell70 = row.createCell(6);

	   if (record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell70.setCellValue(record1.getR10_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell70.setCellStyle(numberStyle);
	   } else {
	       cell70.setCellValue("");
	       cell70.setCellStyle(textStyle);
	   }


	    row = sheet.getRow(72) != null ? sheet.getRow(72) : sheet.createRow(72);
	    Cell cell71 = row.createCell(0);

	   if (record1.getR11_DUE_FROM_DOMESTIC_NAME_OF_BANK() != null && !record1.getR11_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	       cell71.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_NAME_OF_BANK().toString().trim() );
	       cell71.setCellStyle(numberStyle);
	   } else {
	       cell71.setCellValue("");
	       cell71.setCellStyle(textStyle);
	   }

	    Cell cell72 = row.createCell(1);

	   if (record1.getR11_DUE_FROM_DOMESTIC_ACCT_TYPE() != null && !record1.getR11_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	       cell72.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_ACCT_TYPE().toString().trim() );
	       cell72.setCellStyle(numberStyle);
	   } else {
	       cell72.setCellValue("");
	       cell72.setCellStyle(textStyle);
	   }

	    Cell cell73 = row.createCell(2);

	   if (record1.getR11_DUE_FROM_DOMESTIC_PURPOSE() != null && !record1.getR11_DUE_FROM_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	       cell73.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_PURPOSE().toString().trim() );
	       cell73.setCellStyle(numberStyle);
	   } else {
	       cell73.setCellValue("");
	       cell73.setCellStyle(textStyle);
	   }

	    Cell cell74 = row.createCell(3);

	   if (record1.getR11_DUE_FROM_DOMESTIC_AMOUNT() != null && !record1.getR11_DUE_FROM_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	       cell74.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_AMOUNT().toString().trim() );
	       cell74.setCellStyle(numberStyle);
	   } else {
	       cell74.setCellValue("");
	       cell74.setCellStyle(textStyle);
	   }

	    Cell cell75 = row.createCell(4);

	   if (record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT() != null && !record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	       cell75.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT().toString().trim() );
	       cell75.setCellStyle(numberStyle);
	   } else {
	       cell75.setCellValue("");
	       cell75.setCellStyle(textStyle);
	   }

	    Cell cell76 = row.createCell(5);

	   if (record1.getR11_DUE_FROM_DOMESTIC_CURRENCY() != null && !record1.getR11_DUE_FROM_DOMESTIC_CURRENCY().toString().trim().equals("N/A") ) {
	       cell76.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_CURRENCY().toString().trim() );
	       cell76.setCellStyle(numberStyle);
	   } else {
	       cell76.setCellValue("");
	       cell76.setCellStyle(textStyle);
	   }

	    Cell cell77 = row.createCell(6);

	   if (record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	       cell77.setCellValue(record1.getR11_DUE_FROM_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	       cell77.setCellStyle(numberStyle);
	   } else {
	       cell77.setCellValue("");
	       cell77.setCellStyle(textStyle);
	   }

	   row = sheet.getRow(77) != null ? sheet.getRow(77) : sheet.createRow(77);
	   Cell cell78 = row.createCell(0);

	  if (record1.getR1_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR1_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell78.setCellValue(record1.getR1_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell78.setCellStyle(numberStyle);
	  } else {
	      cell78.setCellValue("");
	      cell78.setCellStyle(textStyle);
	  }

	   Cell cell79 = row.createCell(1);

	  if (record1.getR1_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR1_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell79.setCellValue(record1.getR1_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell79.setCellStyle(numberStyle);
	  } else {
	      cell79.setCellValue("");
	      cell79.setCellStyle(textStyle);
	  }

	   Cell cell80 = row.createCell(2);

	  if (record1.getR1_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR1_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell80.setCellValue(record1.getR1_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell80.setCellStyle(numberStyle);
	  } else {
	      cell80.setCellValue("");
	      cell80.setCellStyle(textStyle);
	  }

	   Cell cell81 = row.createCell(3);

	  if (record1.getR1_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR1_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell81.setCellValue(record1.getR1_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell81.setCellStyle(numberStyle);
	  } else {
	      cell81.setCellValue("");
	      cell81.setCellStyle(textStyle);
	  }

	   Cell cell82 = row.createCell(4);

	  if (record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell82.setCellValue(record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell82.setCellStyle(numberStyle);
	  } else {
	      cell82.setCellValue("");
	      cell82.setCellStyle(textStyle);
	  }

	   Cell cell83 = row.createCell(5);

	  if (record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell83.setCellValue(record1.getR1_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell83.setCellStyle(numberStyle);
	  } else {
	      cell83.setCellValue("");
	      cell83.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(78) != null ? sheet.getRow(78) : sheet.createRow(78);
	   Cell cell84 = row.createCell(0);

	  if (record1.getR2_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR2_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell84.setCellValue(record1.getR2_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell84.setCellStyle(numberStyle);
	  } else {
	      cell84.setCellValue("");
	      cell84.setCellStyle(textStyle);
	  }

	   Cell cell85 = row.createCell(1);

	  if (record1.getR2_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR2_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell85.setCellValue(record1.getR2_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell85.setCellStyle(numberStyle);
	  } else {
	      cell85.setCellValue("");
	      cell85.setCellStyle(textStyle);
	  }

	   Cell cell86 = row.createCell(2);

	  if (record1.getR2_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR2_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell86.setCellValue(record1.getR2_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell86.setCellStyle(numberStyle);
	  } else {
	      cell86.setCellValue("");
	      cell86.setCellStyle(textStyle);
	  }

	   Cell cell87 = row.createCell(3);

	  if (record1.getR2_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR2_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell87.setCellValue(record1.getR2_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell87.setCellStyle(numberStyle);
	  } else {
	      cell87.setCellValue("");
	      cell87.setCellStyle(textStyle);
	  }

	   Cell cell88 = row.createCell(4);

	  if (record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell88.setCellValue(record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell88.setCellStyle(numberStyle);
	  } else {
	      cell88.setCellValue("");
	      cell88.setCellStyle(textStyle);
	  }

	   Cell cell89 = row.createCell(5);

	  if (record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell89.setCellValue(record1.getR2_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell89.setCellStyle(numberStyle);
	  } else {
	      cell89.setCellValue("");
	      cell89.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(79) != null ? sheet.getRow(79) : sheet.createRow(79);
	   Cell cell90 = row.createCell(0);

	  if (record1.getR3_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR3_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell90.setCellValue(record1.getR3_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell90.setCellStyle(numberStyle);
	  } else {
	      cell90.setCellValue("");
	      cell90.setCellStyle(textStyle);
	  }

	   Cell cell91 = row.createCell(1);

	  if (record1.getR3_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR3_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell91.setCellValue(record1.getR3_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell91.setCellStyle(numberStyle);
	  } else {
	      cell91.setCellValue("");
	      cell91.setCellStyle(textStyle);
	  }

	   Cell cell92 = row.createCell(2);

	  if (record1.getR3_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR3_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell92.setCellValue(record1.getR3_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell92.setCellStyle(numberStyle);
	  } else {
	      cell92.setCellValue("");
	      cell92.setCellStyle(textStyle);
	  }

	   Cell cell93 = row.createCell(3);

	  if (record1.getR3_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR3_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell93.setCellValue(record1.getR3_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell93.setCellStyle(numberStyle);
	  } else {
	      cell93.setCellValue("");
	      cell93.setCellStyle(textStyle);
	  }

	   Cell cell94 = row.createCell(4);

	  if (record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell94.setCellValue(record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell94.setCellStyle(numberStyle);
	  } else {
	      cell94.setCellValue("");
	      cell94.setCellStyle(textStyle);
	  }

	   Cell cell95 = row.createCell(5);

	  if (record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell95.setCellValue(record1.getR3_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell95.setCellStyle(numberStyle);
	  } else {
	      cell95.setCellValue("");
	      cell95.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(80) != null ? sheet.getRow(80) : sheet.createRow(80);
	   Cell cell96 = row.createCell(0);

	  if (record1.getR4_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR4_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell96.setCellValue(record1.getR4_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell96.setCellStyle(numberStyle);
	  } else {
	      cell96.setCellValue("");
	      cell96.setCellStyle(textStyle);
	  }

	   Cell cell97 = row.createCell(1);

	  if (record1.getR4_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR4_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell97.setCellValue(record1.getR4_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell97.setCellStyle(numberStyle);
	  } else {
	      cell97.setCellValue("");
	      cell97.setCellStyle(textStyle);
	  }

	   Cell cell98 = row.createCell(2);

	  if (record1.getR4_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR4_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell98.setCellValue(record1.getR4_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell98.setCellStyle(numberStyle);
	  } else {
	      cell98.setCellValue("");
	      cell98.setCellStyle(textStyle);
	  }

	   Cell cell99 = row.createCell(3);

	  if (record1.getR4_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR4_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell99.setCellValue(record1.getR4_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell99.setCellStyle(numberStyle);
	  } else {
	      cell99.setCellValue("");
	      cell99.setCellStyle(textStyle);
	  }

	   Cell cell100 = row.createCell(4);

	  if (record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell100.setCellValue(record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell100.setCellStyle(numberStyle);
	  } else {
	      cell100.setCellValue("");
	      cell100.setCellStyle(textStyle);
	  }

	   Cell cell101 = row.createCell(5);

	  if (record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell101.setCellValue(record1.getR4_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell101.setCellStyle(numberStyle);
	  } else {
	      cell101.setCellValue("");
	      cell101.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(81) != null ? sheet.getRow(81) : sheet.createRow(81);
	   Cell cell102 = row.createCell(0);

	  if (record1.getR5_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR5_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell102.setCellValue(record1.getR5_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell102.setCellStyle(numberStyle);
	  } else {
	      cell102.setCellValue("");
	      cell102.setCellStyle(textStyle);
	  }

	   Cell cell103 = row.createCell(1);

	  if (record1.getR5_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR5_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell103.setCellValue(record1.getR5_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell103.setCellStyle(numberStyle);
	  } else {
	      cell103.setCellValue("");
	      cell103.setCellStyle(textStyle);
	  }

	   Cell cell104 = row.createCell(2);

	  if (record1.getR5_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR5_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell104.setCellValue(record1.getR5_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell104.setCellStyle(numberStyle);
	  } else {
	      cell104.setCellValue("");
	      cell104.setCellStyle(textStyle);
	  }

	   Cell cell105 = row.createCell(3);

	  if (record1.getR5_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR5_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell105.setCellValue(record1.getR5_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell105.setCellStyle(numberStyle);
	  } else {
	      cell105.setCellValue("");
	      cell105.setCellStyle(textStyle);
	  }

	   Cell cell106 = row.createCell(4);

	  if (record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell106.setCellValue(record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell106.setCellStyle(numberStyle);
	  } else {
	      cell106.setCellValue("");
	      cell106.setCellStyle(textStyle);
	  }

	   Cell cell107 = row.createCell(5);

	  if (record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell107.setCellValue(record1.getR5_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell107.setCellStyle(numberStyle);
	  } else {
	      cell107.setCellValue("");
	      cell107.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(82) != null ? sheet.getRow(82) : sheet.createRow(82);
	   Cell cell108 = row.createCell(0);

	  if (record1.getR6_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR6_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell108.setCellValue(record1.getR6_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell108.setCellStyle(numberStyle);
	  } else {
	      cell108.setCellValue("");
	      cell108.setCellStyle(textStyle);
	  }

	   Cell cell109 = row.createCell(1);

	  if (record1.getR6_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR6_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell109.setCellValue(record1.getR6_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell109.setCellStyle(numberStyle);
	  } else {
	      cell109.setCellValue("");
	      cell109.setCellStyle(textStyle);
	  }

	   Cell cell110 = row.createCell(2);

	  if (record1.getR6_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR6_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell110.setCellValue(record1.getR6_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell110.setCellStyle(numberStyle);
	  } else {
	      cell110.setCellValue("");
	      cell110.setCellStyle(textStyle);
	  }

	   Cell cell111 = row.createCell(3);

	  if (record1.getR6_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR6_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell111.setCellValue(record1.getR6_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell111.setCellStyle(numberStyle);
	  } else {
	      cell111.setCellValue("");
	      cell111.setCellStyle(textStyle);
	  }

	   Cell cell112 = row.createCell(4);

	  if (record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell112.setCellValue(record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell112.setCellStyle(numberStyle);
	  } else {
	      cell112.setCellValue("");
	      cell112.setCellStyle(textStyle);
	  }

	   Cell cell113 = row.createCell(5);

	  if (record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell113.setCellValue(record1.getR6_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell113.setCellStyle(numberStyle);
	  } else {
	      cell113.setCellValue("");
	      cell113.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(83) != null ? sheet.getRow(83) : sheet.createRow(83);
	   Cell cell114 = row.createCell(0);

	  if (record1.getR7_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR7_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell114.setCellValue(record1.getR7_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell114.setCellStyle(numberStyle);
	  } else {
	      cell114.setCellValue("");
	      cell114.setCellStyle(textStyle);
	  }

	   Cell cell115 = row.createCell(1);

	  if (record1.getR7_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR7_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell115.setCellValue(record1.getR7_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell115.setCellStyle(numberStyle);
	  } else {
	      cell115.setCellValue("");
	      cell115.setCellStyle(textStyle);
	  }

	   Cell cell116 = row.createCell(2);

	  if (record1.getR7_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR7_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell116.setCellValue(record1.getR7_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell116.setCellStyle(numberStyle);
	  } else {
	      cell116.setCellValue("");
	      cell116.setCellStyle(textStyle);
	  }

	   Cell cell117 = row.createCell(3);

	  if (record1.getR7_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR7_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell117.setCellValue(record1.getR7_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell117.setCellStyle(numberStyle);
	  } else {
	      cell117.setCellValue("");
	      cell117.setCellStyle(textStyle);
	  }

	   Cell cell118 = row.createCell(4);

	  if (record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell118.setCellValue(record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell118.setCellStyle(numberStyle);
	  } else {
	      cell118.setCellValue("");
	      cell118.setCellStyle(textStyle);
	  }

	   Cell cell119 = row.createCell(5);

	  if (record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell119.setCellValue(record1.getR7_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell119.setCellStyle(numberStyle);
	  } else {
	      cell119.setCellValue("");
	      cell119.setCellStyle(textStyle);
	  }


	   row = sheet.getRow(84) != null ? sheet.getRow(84) : sheet.createRow(84);
	   Cell cell120 = row.createCell(0);

	  if (record1.getR8_DUE_TO_DOMESTIC_NAME_OF_BANK() != null && !record1.getR8_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim().equals("N/A") ) {
	      cell120.setCellValue(record1.getR8_DUE_TO_DOMESTIC_NAME_OF_BANK().toString().trim() );
	      cell120.setCellStyle(numberStyle);
	  } else {
	      cell120.setCellValue("");
	      cell120.setCellStyle(textStyle);
	  }

	   Cell cell121 = row.createCell(1);

	  if (record1.getR8_DUE_TO_DOMESTIC_ACCT_TYPE() != null && !record1.getR8_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim().equals("N/A") ) {
	      cell121.setCellValue(record1.getR8_DUE_TO_DOMESTIC_ACCT_TYPE().toString().trim() );
	      cell121.setCellStyle(numberStyle);
	  } else {
	      cell121.setCellValue("");
	      cell121.setCellStyle(textStyle);
	  }

	   Cell cell122 = row.createCell(2);

	  if (record1.getR8_DUE_TO_DOMESTIC_PURPOSE() != null && !record1.getR8_DUE_TO_DOMESTIC_PURPOSE().toString().trim().equals("N/A") ) {
	      cell122.setCellValue(record1.getR8_DUE_TO_DOMESTIC_PURPOSE().toString().trim() );
	      cell122.setCellStyle(numberStyle);
	  } else {
	      cell122.setCellValue("");
	      cell122.setCellStyle(textStyle);
	  }

	   Cell cell123 = row.createCell(3);

	  if (record1.getR8_DUE_TO_DOMESTIC_AMOUNT() != null && !record1.getR8_DUE_TO_DOMESTIC_AMOUNT().toString().trim().equals("N/A") ) {
	      cell123.setCellValue(record1.getR8_DUE_TO_DOMESTIC_AMOUNT().toString().trim() );
	      cell123.setCellStyle(numberStyle);
	  } else {
	      cell123.setCellValue("");
	      cell123.setCellStyle(textStyle);
	  }

	   Cell cell124 = row.createCell(4);

	  if (record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT() != null && !record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim().equals("N/A") ) {
	      cell124.setCellValue(record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT().toString().trim() );
	      cell124.setCellStyle(numberStyle);
	  } else {
	      cell124.setCellValue("");
	      cell124.setCellStyle(textStyle);
	  }

	   Cell cell125 = row.createCell(5);

	  if (record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT() != null && !record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim().equals("N/A") ) {
	      cell125.setCellValue(record1.getR8_DUE_TO_DOMESTIC_RISK_WEIGHT_AMT().toString().trim() );
	      cell125.setCellStyle(numberStyle);
	  } else {
	      cell125.setCellValue("");
	      cell125.setCellStyle(textStyle);
	  }

	}
	
	private void writeRowDataResub01(Sheet sheet, List<BrrsMNosvosP1ResbuSummaryEntity> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			System.out.println("came to write row data 1 method");
			
			for (BrrsMNosvosP1ResbuSummaryEntity record : dataList) {
			
			for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
			  String rowCode = rowCodes[rowIndex];
			  Row row = sheet.getRow(baseRow + rowIndex);
			 
			  if (row == null) row = sheet.createRow(baseRow + rowIndex);
			
			  for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
			      String fieldName = rowCode+ "_" + fieldSuffixes[colIndex];
			   
			      // 👉 Skip column B (index 1)
			      int excelColIndex =  colIndex + 1;

			      Cell cell = row.createCell(excelColIndex);
			      try {
			          Field field = BrrsMNosvosP1ResbuSummaryEntity.class.getDeclaredField(fieldName);
			          field.setAccessible(true);
			          Object value = field.get(record);
			         
			          if (value == null || "N/A".equals(value.toString().trim())) {
	                        // ✅ keep cell with style but no value
	                        cell.setCellValue("");
	                        cell.setCellStyle(textStyle);
	                        continue;
	                    }

			          if (value instanceof BigDecimal) {
			              cell.setCellValue(((BigDecimal) value).doubleValue());
			              cell.setCellStyle(numberStyle);
			          }else if (value instanceof String) {
			        	    cell.setCellValue((String) value);
			        	    cell.setCellStyle(textStyle);
			        	}  else {
			              cell.setCellValue("");
			              cell.setCellStyle(textStyle);
			          }
			      } catch (NoSuchFieldException | IllegalAccessException e) {
			          cell.setCellValue("");
			          cell.setCellStyle(textStyle);
			          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
			      }
			  }
			}
			}
		}

	
	private void writeRowDataResub02(Sheet sheet, List<BrrsMNosvosP2ResbuSummaryEntity> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			System.out.println("came to write row data 1 method");
			
			for (BrrsMNosvosP2ResbuSummaryEntity record : dataList) {
			
			for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
			  String rowCode = rowCodes[rowIndex];
			  Row row = sheet.getRow(baseRow + rowIndex);
			 
			  if (row == null) row = sheet.createRow(baseRow + rowIndex);
			
			  for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
			      String fieldName = rowCode+ "_" + fieldSuffixes[colIndex];
			   
			      // 👉 Skip column B (index 1)
			      int excelColIndex = colIndex + 11 ;

			      Cell cell = row.createCell(excelColIndex);
			      try {
			          Field field = BrrsMNosvosP2ResbuSummaryEntity.class.getDeclaredField(fieldName);
			          field.setAccessible(true);
			          Object value = field.get(record);
			         
			          if (value == null || "N/A".equals(value.toString().trim())) {
	                        // ✅ keep cell with style but no value
	                        cell.setCellValue("");
	                        cell.setCellStyle(textStyle);
	                        continue;
	                    }
			          
			          if (value instanceof BigDecimal) {
			              cell.setCellValue(((BigDecimal) value).doubleValue());
			              cell.setCellStyle(numberStyle);
			          }else if (value instanceof String) {
			        	    cell.setCellValue((String) value);
			        	    cell.setCellStyle(textStyle);
			        	}  else {
			              cell.setCellValue("");
			              cell.setCellStyle(textStyle);
			          }
			      } catch (NoSuchFieldException | IllegalAccessException e) {
			          cell.setCellValue("");
			          cell.setCellStyle(textStyle);
			          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
			      }
			  }
			}
			}
		}
	
	private void writeRowDataResub03(Sheet sheet, List<BrrsMNosvosP3ResbuSummaryEntity> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			System.out.println("came to write row data 1 method");
			
			for (BrrsMNosvosP3ResbuSummaryEntity record : dataList) {
			
			for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
			  String rowCode = rowCodes[rowIndex];
			  Row row = sheet.getRow(baseRow + rowIndex);
			 
			  if (row == null) row = sheet.createRow(baseRow + rowIndex);
			
			  for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
			      String fieldName = rowCode+ "_" + fieldSuffixes[colIndex];
			   
			      // 👉 Skip column B (index 1)
			      int excelColIndex = colIndex + 1 ;

			      Cell cell = row.createCell(excelColIndex);
			      try {
			          Field field = BrrsMNosvosP3ResbuSummaryEntity.class.getDeclaredField(fieldName);
			          field.setAccessible(true);
			          Object value = field.get(record);
			         
			          if (value == null || "N/A".equals(value.toString().trim())) {
	                        // ✅ keep cell with style but no value
	                        cell.setCellValue("");
	                        cell.setCellStyle(textStyle);
	                        continue;
	                    }

			          if (value instanceof BigDecimal) {
			              cell.setCellValue(((BigDecimal) value).doubleValue());
			              cell.setCellStyle(numberStyle);
			          }else if (value instanceof String) {
			        	    cell.setCellValue((String) value);
			        	    cell.setCellStyle(textStyle);
			        	}  else {
			              cell.setCellValue("");
			              cell.setCellStyle(textStyle);
			          }
			      } catch (NoSuchFieldException | IllegalAccessException e) {
			          cell.setCellValue("");
			          cell.setCellStyle(textStyle);
			          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
			      }
			  }
			}
			}
		}
	
	private void writeRowDataResub04(Sheet sheet, List<BrrsMNosvosP4ResbuSummaryEntity> dataList,
	        String[] rowCodes, String[] fieldSuffixes, int baseRow,
	        CellStyle numberStyle, CellStyle textStyle) {
	    System.out.println("came to write row data 4 method");

	    for (BrrsMNosvosP4ResbuSummaryEntity record : dataList) {

	        for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
	            String rowCode = rowCodes[rowIndex];
	            Row row = sheet.getRow(baseRow + rowIndex);

	            if (row == null) row = sheet.createRow(baseRow + rowIndex);

	            for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
	                String fieldName = rowCode + "_" + fieldSuffixes[colIndex];

	                // 👉 Direct mapping, don’t skip B
	                int excelColIndex = 11+colIndex;

	                Cell cell = row.createCell(excelColIndex);
	                try {
	                    Field field = BrrsMNosvosP4ResbuSummaryEntity.class.getDeclaredField(fieldName);
	                    field.setAccessible(true);
	                    Object value = field.get(record);

	                    if (value == null || "N/A".equals(value.toString().trim())) {
	                        cell.setCellValue(""); // keep style, blank value
	                        cell.setCellStyle(textStyle);
	                        continue;
	                    }

	                    if (value instanceof BigDecimal) {
	                        cell.setCellValue(((BigDecimal) value).doubleValue());
	                        cell.setCellStyle(numberStyle);
	                    } else if (value instanceof String) {
	                        cell.setCellValue((String) value);
	                        cell.setCellStyle(textStyle);
	                    } else {
	                        cell.setCellValue(value.toString());
	                        cell.setCellStyle(textStyle);
	                    }
	                } catch (NoSuchFieldException | IllegalAccessException e) {
	                    cell.setCellValue("");
	                    cell.setCellStyle(textStyle);
	                    LoggerFactory.getLogger(getClass())
	                        .warn("Field not found or inaccessible: {}", fieldName);
	                }
	            }
	        }
	    }
	}
	
	private void writeRowData01(Sheet sheet, List<BrrsMNosvosP1Archival> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			System.out.println("came to write row data 1 method");
			
			for (BrrsMNosvosP1Archival record : dataList) {
			
			for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
			  String rowCode = rowCodes[rowIndex];
			  Row row = sheet.getRow(baseRow + rowIndex);
			 
			  if (row == null) row = sheet.createRow(baseRow + rowIndex);
			
			  for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
			      String fieldName = rowCode+ "_" + fieldSuffixes[colIndex];
			   
			      // 👉 Skip column B (index 1)
			      int excelColIndex =  colIndex + 1;

			      Cell cell = row.createCell(excelColIndex);
			      try {
			          Field field = BrrsMNosvosP1Archival.class.getDeclaredField(fieldName);
			          field.setAccessible(true);
			          Object value = field.get(record);
			         
			          if (value == null || "N/A".equals(value.toString().trim())) {
	                        // ✅ keep cell with style but no value
	                        cell.setCellValue("");
	                        cell.setCellStyle(textStyle);
	                        continue;
	                    }

			          if (value instanceof BigDecimal) {
			              cell.setCellValue(((BigDecimal) value).doubleValue());
			              cell.setCellStyle(numberStyle);
			          }else if (value instanceof String) {
			        	    cell.setCellValue((String) value);
			        	    cell.setCellStyle(textStyle);
			        	}  else {
			              cell.setCellValue("");
			              cell.setCellStyle(textStyle);
			          }
			      } catch (NoSuchFieldException | IllegalAccessException e) {
			          cell.setCellValue("");
			          cell.setCellStyle(textStyle);
			          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
			      }
			  }
			}
			}
		}

	
	private void writeRowData02(Sheet sheet, List<BrrsMNosvosP2Archival> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			System.out.println("came to write row data 1 method");
			
			for (BrrsMNosvosP2Archival record : dataList) {
			
			for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
			  String rowCode = rowCodes[rowIndex];
			  Row row = sheet.getRow(baseRow + rowIndex);
			 
			  if (row == null) row = sheet.createRow(baseRow + rowIndex);
			
			  for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
			      String fieldName = rowCode+ "_" + fieldSuffixes[colIndex];
			   
			      // 👉 Skip column B (index 1)
			      int excelColIndex = colIndex + 11 ;

			      Cell cell = row.createCell(excelColIndex);
			      try {
			          Field field = BrrsMNosvosP2Archival.class.getDeclaredField(fieldName);
			          field.setAccessible(true);
			          Object value = field.get(record);
			         
			          if (value == null || "N/A".equals(value.toString().trim())) {
	                        // ✅ keep cell with style but no value
	                        cell.setCellValue("");
	                        cell.setCellStyle(textStyle);
	                        continue;
	                    }
			          
			          if (value instanceof BigDecimal) {
			              cell.setCellValue(((BigDecimal) value).doubleValue());
			              cell.setCellStyle(numberStyle);
			          }else if (value instanceof String) {
			        	    cell.setCellValue((String) value);
			        	    cell.setCellStyle(textStyle);
			        	}  else {
			              cell.setCellValue("");
			              cell.setCellStyle(textStyle);
			          }
			      } catch (NoSuchFieldException | IllegalAccessException e) {
			          cell.setCellValue("");
			          cell.setCellStyle(textStyle);
			          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
			      }
			  }
			}
			}
		}
	
	private void writeRowData03(Sheet sheet, List<BrrsMNosvosP3Archival> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			System.out.println("came to write row data 1 method");
			
			for (BrrsMNosvosP3Archival record : dataList) {
			
			for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
			  String rowCode = rowCodes[rowIndex];
			  Row row = sheet.getRow(baseRow + rowIndex);
			 
			  if (row == null) row = sheet.createRow(baseRow + rowIndex);
			
			  for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
			      String fieldName = rowCode+ "_" + fieldSuffixes[colIndex];
			   
			      // 👉 Skip column B (index 1)
			      int excelColIndex = colIndex + 1 ;

			      Cell cell = row.createCell(excelColIndex);
			      try {
			          Field field = BrrsMNosvosP3Archival.class.getDeclaredField(fieldName);
			          field.setAccessible(true);
			          Object value = field.get(record);
			         
			          if (value == null || "N/A".equals(value.toString().trim())) {
	                        // ✅ keep cell with style but no value
	                        cell.setCellValue("");
	                        cell.setCellStyle(textStyle);
	                        continue;
	                    }

			          if (value instanceof BigDecimal) {
			              cell.setCellValue(((BigDecimal) value).doubleValue());
			              cell.setCellStyle(numberStyle);
			          }else if (value instanceof String) {
			        	    cell.setCellValue((String) value);
			        	    cell.setCellStyle(textStyle);
			        	}  else {
			              cell.setCellValue("");
			              cell.setCellStyle(textStyle);
			          }
			      } catch (NoSuchFieldException | IllegalAccessException e) {
			          cell.setCellValue("");
			          cell.setCellStyle(textStyle);
			          LoggerFactory.getLogger(getClass()).warn("Field not found or inaccessible: {}", fieldName);
			      }
			  }
			}
			}
		}
	
	private void writeRowData04(Sheet sheet, List<BrrsMNosvosP4Archival> dataList,
	        String[] rowCodes, String[] fieldSuffixes, int baseRow,
	        CellStyle numberStyle, CellStyle textStyle) {
	    System.out.println("came to write row data 4 method");

	    for (BrrsMNosvosP4Archival record : dataList) {

	        for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
	            String rowCode = rowCodes[rowIndex];
	            Row row = sheet.getRow(baseRow + rowIndex);

	            if (row == null) row = sheet.createRow(baseRow + rowIndex);

	            for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
	                String fieldName = rowCode + "_" + fieldSuffixes[colIndex];

	                // 👉 Direct mapping, don’t skip B
	                int excelColIndex = 11+colIndex;

	                Cell cell = row.createCell(excelColIndex);
	                try {
	                    Field field = BrrsMNosvosP4Archival.class.getDeclaredField(fieldName);
	                    field.setAccessible(true);
	                    Object value = field.get(record);

	                    if (value == null || "N/A".equals(value.toString().trim())) {
	                        cell.setCellValue(""); // keep style, blank value
	                        cell.setCellStyle(textStyle);
	                        continue;
	                    }

	                    if (value instanceof BigDecimal) {
	                        cell.setCellValue(((BigDecimal) value).doubleValue());
	                        cell.setCellStyle(numberStyle);
	                    } else if (value instanceof String) {
	                        cell.setCellValue((String) value);
	                        cell.setCellStyle(textStyle);
	                    } else {
	                        cell.setCellValue(value.toString());
	                        cell.setCellStyle(textStyle);
	                    }
	                } catch (NoSuchFieldException | IllegalAccessException e) {
	                    cell.setCellValue("");
	                    cell.setCellStyle(textStyle);
	                    LoggerFactory.getLogger(getClass())
	                        .warn("Field not found or inaccessible: {}", fieldName);
	                }
	            }
	        }
	    }
	}

	public List<Object[]> getM_NOSVOSResub() {
	    List<Object[]> resubList = new ArrayList<>();
	    try {
	        List<BrrsMNosvosP1ResbuSummaryEntity> latestArchivalList = BrrsMNosvosP1ResbuSummaryRepo
	                .getdatabydateListWithVersion();

	        if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
	            for (BrrsMNosvosP1ResbuSummaryEntity entity : latestArchivalList) {
	                resubList.add(new Object[] {
	                        entity.getREPORT_DATE(),
	                        entity.getREPORT_VERSION(),
	                        entity.getREPORT_RESUB_DATE()
	                });
	            }
	            System.out.println("Fetched " + resubList.size() + " record(s)");
	        } else {
	            System.out.println("No archival data found.");
	        }

	    } catch (Exception e) {
	        System.err.println("Error fetching M_SRWA_12B Resub data: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return resubList;
	}
	
	public void updateReportResub(BrrsMNosvosP1ResbuSummaryEntity updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());
	    Date cleanDate = normalizeDate(updatedEntity.getREPORT_DATE());
	    
	    BRRS_NOSVOS_Summary_PK pk = new BRRS_NOSVOS_Summary_PK();
	    pk.setREPORT_DATE(cleanDate);
	    pk.setREPORT_VERSION(updatedEntity.getREPORT_VERSION());
	    

	    
	    Optional<BrrsMNosvosP1ResbuSummaryEntity> latestArchivalOpt1 = BrrsMNosvosP1ResbuSummaryRepo
                .getLatestArchivalVersionByDate(cleanDate);
	    int newVersion=0;
        if (latestArchivalOpt1.isPresent()) {
        	BrrsMNosvosP1ResbuSummaryEntity latestArchival = latestArchivalOpt1.get();
            try {
                newVersion = Integer.parseInt(latestArchival.getREPORT_VERSION()) + 1;
            } catch (NumberFormatException e) {
                System.err.println("Invalid version format. Defaulting to version 1");
                newVersion = 1;
            }
        } else {
            System.out.println("No previous archival found for date: " + cleanDate);
        }
        
        String version = String.valueOf(newVersion);
        
        BrrsMNosvosP1ResbuSummaryEntity summarySave = new BrrsMNosvosP1ResbuSummaryEntity();
        
        org.springframework.beans.BeanUtils.copyProperties(updatedEntity, summarySave);
        
        Date now = new Date();
        summarySave.setREPORT_DATE(cleanDate);
        summarySave.setREPORT_RESUB_DATE(now);
        summarySave.setREPORT_VERSION(version);
        
        ;
	    // 3️⃣ Save updated entity
	    BrrsMNosvosP1ResbuSummaryRepo.save(summarySave);
	    
	    BrrsMNosvosP1ResbuDetailEntity resubDetail = new BrrsMNosvosP1ResbuDetailEntity();
	    BrrsMNosvosP1ArchivalDetail   resubArchDetail = new BrrsMNosvosP1ArchivalDetail();
	    BrrsMNosvosP1Archival resubArchSummary = new BrrsMNosvosP1Archival();

	    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubDetail);
	    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubArchSummary);
	    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubArchDetail);
	    

	    BrrsMNosvosP1ArchivalRepositoryDetail.save(resubArchDetail);
	    
	    BrrsMNosvosP1ArchivalRepository.save(resubArchSummary);

	    BrrsMNosvosP1ResbuDetailRepo.save(resubDetail);
	}

	public void updateReport2Resub(BrrsMNosvosP2ResbuSummaryEntity updatedEntity) {
		 System.out.println("Came to services");
		    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());
		    Date cleanDate = normalizeDate(updatedEntity.getREPORT_DATE());
		    
		    BRRS_NOSVOS_Summary_PK pk = new BRRS_NOSVOS_Summary_PK();
		    pk.setREPORT_DATE(cleanDate);
		    pk.setREPORT_VERSION(updatedEntity.getREPORT_VERSION());
		    

		    
		    Optional<BrrsMNosvosP2ResbuSummaryEntity> latestArchivalOpt1 = BrrsMNosvosP2ResbuSummaryRepo
	                .getLatestArchivalVersionByDate(cleanDate);
		    int newVersion=0;
	        if (latestArchivalOpt1.isPresent()) {
	        	BrrsMNosvosP2ResbuSummaryEntity latestArchival = latestArchivalOpt1.get();
	            try {
	                newVersion = Integer.parseInt(latestArchival.getREPORT_VERSION()) + 1;
	            } catch (NumberFormatException e) {
	                System.err.println("Invalid version format. Defaulting to version 1");
	                newVersion = 1;
	            }
	        } else {
	            System.out.println("No previous archival found for date: " + cleanDate);
	        }
	        
	        String version = String.valueOf(newVersion);
	        
	        BrrsMNosvosP2ResbuSummaryEntity summarySave = new BrrsMNosvosP2ResbuSummaryEntity();
	        
	        org.springframework.beans.BeanUtils.copyProperties(updatedEntity, summarySave);
	        
	        Date now = new Date();
	        summarySave.setREPORT_DATE(cleanDate);
	        summarySave.setREPORT_RESUB_DATE(now);
	        summarySave.setREPORT_VERSION(version);
	        
	        ;
		    // 3️⃣ Save updated entity
		    BrrsMNosvosP2ResbuSummaryRepo.save(summarySave);
		    
		    BrrsMNosvosP2ResbuDetailEntity resubDetail = new BrrsMNosvosP2ResbuDetailEntity();
		    BrrsMNosvosP2ArchivalDetail   resubArchDetail = new BrrsMNosvosP2ArchivalDetail();
		    BrrsMNosvosP2Archival resubArchSummary = new BrrsMNosvosP2Archival();

		    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubDetail);
		    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubArchSummary);
		    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubArchDetail);
		    

		    BrrsMNosvosP2ArchivalRepositoryDetail.save(resubArchDetail);
		    
		    BrrsMNosvosP2ArchivalRepository.save(resubArchSummary);

		    BrrsMNosvosP2ResbuDetailRepo.save(resubDetail);
	}

	public void updateReport3Resub(BrrsMNosvosP3ResbuSummaryEntity updatedEntity) {
		System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());
	    Date cleanDate = normalizeDate(updatedEntity.getREPORT_DATE());
	    
	    BRRS_NOSVOS_Summary_PK pk = new BRRS_NOSVOS_Summary_PK();
	    pk.setREPORT_DATE(cleanDate);
	    pk.setREPORT_VERSION(updatedEntity.getREPORT_VERSION());
	    

	    
	    Optional<BrrsMNosvosP3ResbuSummaryEntity> latestArchivalOpt1 = BrrsMNosvosP3ResbuSummaryRepo
                .getLatestArchivalVersionByDate(cleanDate);
	    int newVersion=0;
        if (latestArchivalOpt1.isPresent()) {
        	BrrsMNosvosP3ResbuSummaryEntity latestArchival = latestArchivalOpt1.get();
            try {
                newVersion = Integer.parseInt(latestArchival.getREPORT_VERSION()) + 1;
            } catch (NumberFormatException e) {
                System.err.println("Invalid version format. Defaulting to version 1");
                newVersion = 1;
            }
        } else {
            System.out.println("No previous archival found for date: " + cleanDate);
        }
        
        String version = String.valueOf(newVersion);
        
        BrrsMNosvosP3ResbuSummaryEntity summarySave = new BrrsMNosvosP3ResbuSummaryEntity();
        
        org.springframework.beans.BeanUtils.copyProperties(updatedEntity, summarySave);
        
        Date now = new Date();
        summarySave.setREPORT_DATE(cleanDate);
        summarySave.setREPORT_RESUB_DATE(now);
        summarySave.setREPORT_VERSION(version);
        
        ;
	    // 3️⃣ Save updated entity
	    BrrsMNosvosP3ResbuSummaryRepo.save(summarySave);
	    
	    BrrsMNosvosP3ResbuDetailEntity resubDetail = new BrrsMNosvosP3ResbuDetailEntity();
	    BrrsMNosvosP3ArchivalDetail   resubArchDetail = new BrrsMNosvosP3ArchivalDetail();
	    BrrsMNosvosP3Archival resubArchSummary = new BrrsMNosvosP3Archival();

	    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubDetail);
	    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubArchSummary);
	    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubArchDetail);
	    

	    BrrsMNosvosP3ArchivalRepositoryDetail.save(resubArchDetail);
	    
	    BrrsMNosvosP3ArchivalRepository.save(resubArchSummary);

	    BrrsMNosvosP3ResbuDetailRepo.save(resubDetail);
	}
	
	public void updateReport4Resub(BrrsMNosvosP4ResbuSummaryEntity updatedEntity) {
		System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());
	    Date cleanDate = normalizeDate(updatedEntity.getREPORT_DATE());
	    
	    BRRS_NOSVOS_Summary_PK pk = new BRRS_NOSVOS_Summary_PK();
	    pk.setREPORT_DATE(cleanDate);
	    pk.setREPORT_VERSION(updatedEntity.getREPORT_VERSION());
	    

	    
	    Optional<BrrsMNosvosP4ResbuSummaryEntity> latestArchivalOpt1 = BrrsMNosvosP4ResbuSummaryRepo
                .getLatestArchivalVersionByDate(cleanDate);
	    int newVersion=0;
        if (latestArchivalOpt1.isPresent()) {
        	BrrsMNosvosP4ResbuSummaryEntity latestArchival = latestArchivalOpt1.get();
            try {
                newVersion = Integer.parseInt(latestArchival.getREPORT_VERSION()) + 1;
            } catch (NumberFormatException e) {
                System.err.println("Invalid version format. Defaulting to version 1");
                newVersion = 1;
            }
        } else {
            System.out.println("No previous archival found for date: " + cleanDate);
        }
        
        String version = String.valueOf(newVersion);
        
        BrrsMNosvosP4ResbuSummaryEntity summarySave = new BrrsMNosvosP4ResbuSummaryEntity();
        
        org.springframework.beans.BeanUtils.copyProperties(updatedEntity, summarySave);
        
        Date now = new Date();
        summarySave.setREPORT_DATE(cleanDate);
        summarySave.setREPORT_RESUB_DATE(now);
        summarySave.setREPORT_VERSION(version);
        
        ;
	    // 3️⃣ Save updated entity
	    BrrsMNosvosP4ResbuSummaryRepo.save(summarySave);
	    
	    BrrsMNosvosP4ResbuDetailEntity resubDetail = new BrrsMNosvosP4ResbuDetailEntity();
	    BrrsMNosvosP4ArchivalDetail   resubArchDetail = new BrrsMNosvosP4ArchivalDetail();
	    BrrsMNosvosP4Archival resubArchSummary = new BrrsMNosvosP4Archival();

	    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubDetail);
	    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubArchSummary);
	    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubArchDetail);
	    
	    BrrsMNosvosP4ArchivalRepositoryDetail.save(resubArchDetail);
	    
	    BrrsMNosvosP4ArchivalRepository.save(resubArchSummary);

	    BrrsMNosvosP4ResbuDetailRepo.save(resubDetail);
	}
	
	public void updateReport5Resub(BrrsMNosvosP5ResbuSummaryEntity updatedEntity) {
		System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());
	    Date cleanDate = normalizeDate(updatedEntity.getREPORT_DATE());
	    
	    BRRS_NOSVOS_Summary_PK pk = new BRRS_NOSVOS_Summary_PK();
	    pk.setREPORT_DATE(cleanDate);
	    pk.setREPORT_VERSION(updatedEntity.getREPORT_VERSION());
	    

	    
	    Optional<BrrsMNosvosP5ResbuSummaryEntity> latestArchivalOpt1 = BrrsMNosvosP5ResbuSummaryRepo
                .getLatestArchivalVersionByDate(cleanDate);
	    int newVersion=0;
        if (latestArchivalOpt1.isPresent()) {
        	BrrsMNosvosP5ResbuSummaryEntity latestArchival = latestArchivalOpt1.get();
            try {
                newVersion = Integer.parseInt(latestArchival.getREPORT_VERSION()) + 1;
            } catch (NumberFormatException e) {
                System.err.println("Invalid version format. Defaulting to version 1");
                newVersion = 1;
            }
        } else {
            System.out.println("No previous archival found for date: " + cleanDate);
        }
        
        String version = String.valueOf(newVersion);
        
        BrrsMNosvosP5ResbuSummaryEntity summarySave = new BrrsMNosvosP5ResbuSummaryEntity();
        
        org.springframework.beans.BeanUtils.copyProperties(updatedEntity, summarySave);
        
        Date now = new Date();
        summarySave.setREPORT_DATE(cleanDate);
        summarySave.setREPORT_RESUB_DATE(now);
        summarySave.setREPORT_VERSION(version);
        
        ;
	    // 3️⃣ Save updated entity
	    BrrsMNosvosP5ResbuSummaryRepo.save(summarySave);
	    
	    BrrsMNosvosP5ResbuDetailEntity resubDetail = new BrrsMNosvosP5ResbuDetailEntity();
	    BrrsMNosvosP5ArchivalDetail   resubArchDetail = new BrrsMNosvosP5ArchivalDetail();
	    BrrsMNosvosP5Archival resubArchSummary = new BrrsMNosvosP5Archival();

	    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubDetail);
	    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubArchSummary);
	    org.springframework.beans.BeanUtils.copyProperties(summarySave, resubArchDetail);
	    
	    BrrsMNosvosP5ArchivalRepositoryDetail.save(resubArchDetail);
	    
	    BrrsMNosvosP5ArchivalRepository.save(resubArchSummary);

	    BrrsMNosvosP5ResbuDetailRepo.save(resubDetail);
	}

}
