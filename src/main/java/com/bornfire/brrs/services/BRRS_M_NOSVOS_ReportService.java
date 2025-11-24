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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Entity1;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Entity3;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Entity4;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Summary_Repo4;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_SFINP2_Summary_Repo;
import com.bornfire.brrs.entities.BrrsMNosvosP1;
import com.bornfire.brrs.entities.BrrsMNosvosP1Archival;
import com.bornfire.brrs.entities.BrrsMNosvosP1ArchivalRepository;
import com.bornfire.brrs.entities.BrrsMNosvosP1Repository;
import com.bornfire.brrs.entities.BrrsMNosvosP2;
import com.bornfire.brrs.entities.BrrsMNosvosP2Archival;
import com.bornfire.brrs.entities.BrrsMNosvosP2ArchivalRepository;
import com.bornfire.brrs.entities.BrrsMNosvosP2Repository;
import com.bornfire.brrs.entities.BrrsMNosvosP3;
import com.bornfire.brrs.entities.BrrsMNosvosP3Archival;
import com.bornfire.brrs.entities.BrrsMNosvosP3ArchivalRepository;
import com.bornfire.brrs.entities.BrrsMNosvosP3Repository;
import com.bornfire.brrs.entities.BrrsMNosvosP4;
import com.bornfire.brrs.entities.BrrsMNosvosP4Archival;
import com.bornfire.brrs.entities.BrrsMNosvosP4ArchivalRepository;
import com.bornfire.brrs.entities.BrrsMNosvosP4Repository;
import com.bornfire.brrs.entities.M_AIDP_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_AIDP_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.M_AIDP_Archival_Summary_Entity3;
import com.bornfire.brrs.entities.M_AIDP_Archival_Summary_Entity4;


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

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_NOSVOSView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<BrrsMNosvosP1Archival> T1Master = new ArrayList<BrrsMNosvosP1Archival>();
			List<BrrsMNosvosP2Archival> T2Master = new ArrayList<BrrsMNosvosP2Archival>();
			List<BrrsMNosvosP3Archival> T3Master = new ArrayList<BrrsMNosvosP3Archival>();
			List<BrrsMNosvosP4Archival> T4Master = new ArrayList<BrrsMNosvosP4Archival>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = BrrsMNosvosP1ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = BrrsMNosvosP2ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);
				T3Master = BrrsMNosvosP3ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);
				T4Master = BrrsMNosvosP4ArchivalRepository.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
			mv.addObject("reportsummary4", T4Master);
		} else {
			List<BrrsMNosvosP1> T1Master = new ArrayList<BrrsMNosvosP1>();
			List<BrrsMNosvosP2> T2Master = new ArrayList<BrrsMNosvosP2>();
			List<BrrsMNosvosP3> T3Master = new ArrayList<BrrsMNosvosP3>();
			List<BrrsMNosvosP4> T4Master = new ArrayList<BrrsMNosvosP4>();
			
			try {
				Date d1 = dateformat.parse(todate);
				System.out.println(todate);
				T1Master = BrrsMNosvosP1Repository.getDataByDate(dateformat.parse(todate));
				T2Master = BrrsMNosvosP2Repository.getDataByDate(dateformat.parse(todate));
				T3Master = BrrsMNosvosP3Repository.getDataByDate(dateformat.parse(todate));
				T4Master = BrrsMNosvosP4Repository.getDataByDate(dateformat.parse(todate));
				
				
				
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
		mv.setViewName("BRRS/M_NOSVOS");
		mv.addObject("displaymode", "summary");
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
	
	
	
	public byte[] getM_NOSVOSExcel(String filename, String reportId, String fromdate, String todate, String currency,
            String dtltype, String type, String version) throws Exception {
	logger.info("Service: Starting Excel generation process in memory.");
	System.out.println(type);
	System.out.println(version);
	if (type.equals("ARCHIVAL") & version != null) {
		byte[] ARCHIVALreport = getExcelM_NOSVOSARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
				version);
		return ARCHIVALreport;
	}
	
	
	System.out.println("came to excel download service");
	List<BrrsMNosvosP1> dataList = BrrsMNosvosP1Repository.getData();
	List<BrrsMNosvosP2> dataList2 = BrrsMNosvosP2Repository.getData();
	List<BrrsMNosvosP3> dataList3 = BrrsMNosvosP3Repository.getData();
	List<BrrsMNosvosP4> dataList4 = BrrsMNosvosP4Repository.getData();
	
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

	workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
	workbook.write(out);
	logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
	
	return out.toByteArray();
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
	

	
	public byte[] getExcelM_NOSVOSARCHIVAL(String filename, String reportId, String fromdate, String todate, String currency,
            String dtltype, String type, String version) throws Exception {
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


}
