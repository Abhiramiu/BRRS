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

import com.bornfire.brrs.entities.BRRS_M_AIDP_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Archival_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_M_AIDP_Archival_Summary_Repo4;
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
import com.bornfire.brrs.entities.M_SFINP2_Archival_Summary_Entity;


@Component
@Service
public class BRRS_M_AIDP_ReportService {


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
	BRRS_M_AIDP_Archival_Summary_Repo1	M_AIDP_Archival_Summary_Repo1;
	@Autowired
	BRRS_M_AIDP_Archival_Summary_Repo2	M_AIDP_Archival_Summary_Repo2;
	
	@Autowired
	BRRS_M_AIDP_Archival_Summary_Repo3	M_AIDP_Archival_Summary_Repo3;
	
	@Autowired
	BRRS_M_AIDP_Archival_Summary_Repo4	M_AIDP_Archival_Summary_Repo4;
	
	@Autowired
	BRRS_M_SFINP2_Summary_Repo M_SFINP2_Summary_Repo;

	@Autowired
	BRRS_M_SFINP2_Archival_Detail_Repo M_SFINP2_Archival_Detail_Repo;

	@Autowired
	BRRS_M_SFINP2_Archival_Summary_Repo M_SFINP2_Archival_Summary_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_AIDPView(String reportId, String fromdate, String todate, String currency,
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
			List<BRRS_M_AIDP_Summary_Entity1> T1Master = new ArrayList<BRRS_M_AIDP_Summary_Entity1>();
			List<BRRS_M_AIDP_Summary_Entity2> T2Master = new ArrayList<BRRS_M_AIDP_Summary_Entity2>();
			List<BRRS_M_AIDP_Summary_Entity3> T3Master = new ArrayList<BRRS_M_AIDP_Summary_Entity3>();
			List<BRRS_M_AIDP_Summary_Entity4> T4Master = new ArrayList<BRRS_M_AIDP_Summary_Entity4>();
			
			try {
				Date d1 = dateformat.parse(todate);

				T1Master = BRRS_M_aidpRepo1.getdatabydateList(dateformat.parse(todate));
				T2Master = BRRS_M_aidpRepo2.getdatabydateList(dateformat.parse(todate));
				T3Master = BRRS_M_aidpRepo3.getdatabydateList(dateformat.parse(todate));
				T4Master = BRRS_M_aidpRepo4.getdatabydateList(dateformat.parse(todate));
				
				
				
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
		mv.setViewName("BRRS/M_AIDP");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	
	public void updateReport(BRRS_M_AIDP_Summary_Entity1 updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    BRRS_M_AIDP_Summary_Entity1 existing = BRRS_M_aidpRepo1.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 11; i <= 50; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "NAME_OF_BANK", "TYPE_OF_ACC", "PURPOSE", "CURRENCY",
	                                "BANK_RATE", "AMT_LESS_184_DAYS", "AMT_MORE_184_DAYS" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = BRRS_M_AIDP_Summary_Entity1.class.getMethod(getterName);
	                    Method setter = BRRS_M_AIDP_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R51 totals
	        String[] totalFields = { "TOT_AMT_LESS_184_DAYS", "TOT_AMT_MORE_184_DAYS" };
	        for (String field : totalFields) {
	            String getterName = "getR51_" + field;
	            String setterName = "setR51_" + field;

	            try {
	                Method getter = BRRS_M_AIDP_Summary_Entity1.class.getMethod(getterName);
	                Method setter = BRRS_M_AIDP_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

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
	    BRRS_M_aidpRepo1.save(existing);
	}

	public void updateReport2(BRRS_M_AIDP_Summary_Entity2 updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    BRRS_M_AIDP_Summary_Entity2 existing = BRRS_M_aidpRepo2.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 56; i <= 95; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "NAME_OF_BANK", "TYPE_OF_ACC", "PURPOSE", "CURRENCY",
	                                "BANK_RATE", "AMT_LESS_184_DAYS", "AMT_MORE_184_DAYS" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = BRRS_M_AIDP_Summary_Entity2.class.getMethod(getterName);
	                    Method setter = BRRS_M_AIDP_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R51 totals
	        String[] totalFields = { "TOT_AMT_LESS_184_DAYS", "TOT_AMT_MORE_184_DAYS" };
	        for (String field : totalFields) {
	            String getterName = "getR96_" + field;
	            String setterName = "setR96_" + field;

	            try {
	                Method getter = BRRS_M_AIDP_Summary_Entity2.class.getMethod(getterName);
	                Method setter = BRRS_M_AIDP_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

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
	    BRRS_M_aidpRepo2.save(existing);
	}

	public void updateReport3(BRRS_M_AIDP_Summary_Entity3 updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    BRRS_M_AIDP_Summary_Entity3 existing = BRRS_M_aidpRepo3.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 101; i <= 141; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "NAME_OF_BANK", "TYPE_OF_ACC", "PURPOSE", "CURRENCY",
	                                "BANK_RATE", "AMT_DEMAND", "AMT_TIME" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = BRRS_M_AIDP_Summary_Entity3.class.getMethod(getterName);
	                    Method setter = BRRS_M_AIDP_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R51 totals
	        String[] totalFields = { "TOT_AMT_DEMAND", "TOT_AMT_TIME" };
	        for (String field : totalFields) {
	            String getterName = "getR142_" + field;
	            String setterName = "setR142_" + field;

	            try {
	                Method getter = BRRS_M_AIDP_Summary_Entity3.class.getMethod(getterName);
	                Method setter = BRRS_M_AIDP_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());

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
	    BRRS_M_aidpRepo3.save(existing);
	}
	
	public void updateReport4(BRRS_M_AIDP_Summary_Entity4 updatedEntity) {
	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

	    BRRS_M_AIDP_Summary_Entity4 existing = BRRS_M_aidpRepo4.findById(updatedEntity.getREPORT_DATE())
	            .orElseThrow(() -> new RuntimeException(
	                    "Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

	    try {
	        // 1️⃣ Loop from R11 to R50 and copy fields
	        for (int i = 147; i <= 193; i++) {
	            String prefix = "R" + i + "_";

	            String[] fields = { "NAME_OF_BANK","COUNTRY", "TYPE_OF_ACC", "PURPOSE", "CURRENCY",
	                                "BANK_RATE", "AMT_DEMAND", "AMT_TIME" };

	            for (String field : fields) {
	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter = BRRS_M_AIDP_Summary_Entity4.class.getMethod(getterName);
	                    Method setter = BRRS_M_AIDP_Summary_Entity4.class.getMethod(setterName, getter.getReturnType());

	                    Object newValue = getter.invoke(updatedEntity);
	                    setter.invoke(existing, newValue);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        // 2️⃣ Handle R51 totals
	        String[] totalFields = { "TOT_AMT_DEMAND", "TOT_AMT_TIME" };
	        for (String field : totalFields) {
	            String getterName = "getR194_" + field;
	            String setterName = "setR194_" + field;

	            try {
	                Method getter = BRRS_M_AIDP_Summary_Entity4.class.getMethod(getterName);
	                Method setter = BRRS_M_AIDP_Summary_Entity4.class.getMethod(setterName, getter.getReturnType());

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
	    BRRS_M_aidpRepo4.save(existing);
	}
	
	public List<Object> getM_AIDPArchival() {
		List<Object> M_AIDPArchivallist = new ArrayList<>();
		try {
			M_AIDPArchivallist = M_AIDP_Archival_Summary_Repo1.getM_AIDParchival();
			M_AIDPArchivallist = M_AIDP_Archival_Summary_Repo2.getM_AIDParchival();
			M_AIDPArchivallist = M_AIDP_Archival_Summary_Repo3.getM_AIDParchival();
			M_AIDPArchivallist = M_AIDP_Archival_Summary_Repo4.getM_AIDParchival();
			System.out.println("countser" + M_AIDPArchivallist.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_LA1 Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_AIDPArchivallist;
	}
	
	
	
	public byte[] getM_AIDPExcel(String filename, String reportId, String fromdate, String todate, String currency,
            String dtltype, String type, String version) throws Exception {
	logger.info("Service: Starting Excel generation process in memory.");
	
	System.out.println("came to excel download service");
	List<BRRS_M_AIDP_Summary_Entity1> dataList = BRRS_M_aidpRepo1.getdatabydateList(dateformat.parse(todate));
	List<BRRS_M_AIDP_Summary_Entity2> dataList2 = BRRS_M_aidpRepo2.getdatabydateList(dateformat.parse(todate));
	List<BRRS_M_AIDP_Summary_Entity3> dataList3 = BRRS_M_aidpRepo3.getdatabydateList(dateformat.parse(todate));
	List<BRRS_M_AIDP_Summary_Entity4> dataList4 = BRRS_M_aidpRepo4.getdatabydateList(dateformat.parse(todate));
	
	if (dataList.isEmpty()) {
	logger.warn("Service: No data found for BRF7.3 report. Returning empty result.");
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
	
	
	
	String[] rowCodesPart1 = {
		    "R11", "R12", "R13", "R14", "R15",
		    "R16", "R17", "R18", "R19", "R20",
		    "R21", "R22", "R23", "R24", "R25",
		    "R26", "R27", "R28", "R29", "R30",
		    "R31", "R32", "R33", "R34", "R35",
		    "R36", "R37", "R38", "R39", "R40",
		    "R41", "R42", "R43", "R44", "R45",
		    "R46", "R47", "R48", "R49", "R50"
		};

	String[] rowCodesPart2 = {
		    "R56", "R57", "R58", "R59", "R60",
		    "R61", "R62", "R63", "R64", "R65",
		    "R66", "R67", "R68", "R69", "R70",
		    "R71", "R72", "R73", "R74", "R75",
		    "R76", "R77", "R78", "R79", "R80",
		    "R81", "R82", "R83", "R84", "R85",
		    "R86", "R87", "R88", "R89", "R90",
		    "R91", "R92", "R93", "R94", "R95"
		};

	String[] rowCodesPart3 = {
		    "R101", "R102", "R103", "R104", "R105",
		    "R106", "R107", "R108", "R109", "R110",
		    "R111", "R112", "R113", "R114", "R115",
		    "R116", "R117", "R118", "R119", "R120",
		    "R121", "R122", "R123", "R124", "R125",
		    "R126", "R127", "R128", "R129", "R130",
		    "R131", "R132", "R133", "R134", "R135",
		    "R136", "R137", "R138", "R139", "R140",
		    "R141"
		};

	String[] rowCodesPart4 = {
		    "R147", "R148", "R149", "R150", "R151",
		    "R152", "R153", "R154", "R155", "R156",
		    "R157", "R158", "R159", "R160", "R161",
		    "R162", "R163", "R164", "R165", "R166",
		    "R167", "R168", "R169", "R170", "R171",
		    "R172", "R173", "R174", "R175", "R176",
		    "R177", "R178", "R179", "R180", "R181",
		    "R182", "R183", "R184", "R185", "R186",
		    "R187", "R188", "R189", "R190", "R191",
		    "R192", "R193"
		};

String[] fieldSuffixes = {
"NAME_OF_BANK","TYPE_OF_ACC","PURPOSE","CURRENCY","BANK_RATE","AMT_LESS_184_DAYS","AMT_MORE_184_DAYS"    
};

String[] fieldSuffixes2 = {
"NAME_OF_BANK","TYPE_OF_ACC","PURPOSE","CURRENCY","BANK_RATE","AMT_DEMAND","AMT_TIME"    
};

String[] fieldSuffixes3 = {
"NAME_OF_BANK","COUNTRY","TYPE_OF_ACC","PURPOSE","CURRENCY","BANK_RATE","AMT_DEMAND","AMT_TIME"    
};


	// First set: R11 - R50 at row 11
	writeRowData1(sheet, dataList, rowCodesPart1, fieldSuffixes, 10, numberStyle, textStyle);
	
	// First set: R56 - R95 at row 56
	writeRowData2(sheet, dataList2, rowCodesPart2, fieldSuffixes, 55, numberStyle, textStyle);

	// Third Set: R101 - R141 at row 101
	writeRowData3(sheet, dataList3, rowCodesPart3, fieldSuffixes2, 100, numberStyle, textStyle);

	// Fourth Set: R147 - R196 at row 146
	writeRowData4(sheet, dataList4, rowCodesPart4, fieldSuffixes3, 146, numberStyle, textStyle);

	workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
	workbook.write(out);
	logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
	
	return out.toByteArray();
	}
}
	
	private void writeRowData1(Sheet sheet, List<BRRS_M_AIDP_Summary_Entity1> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			System.out.println("came to write row data 1 method");
			
			for (BRRS_M_AIDP_Summary_Entity1 record : dataList) {
			
			for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
			  String rowCode = rowCodes[rowIndex];
			  Row row = sheet.getRow(baseRow + rowIndex);
			 
			  if (row == null) row = sheet.createRow(baseRow + rowIndex);
			
			  for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
			      String fieldName = rowCode+ "_" + fieldSuffixes[colIndex];
			   
			      // 👉 Skip column B (index 1)
			      int excelColIndex = (colIndex >= 1) ? colIndex + 1 : colIndex;

			      Cell cell = row.createCell(excelColIndex);
			      try {
			          Field field = BRRS_M_AIDP_Summary_Entity1.class.getDeclaredField(fieldName);
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

	
	private void writeRowData2(Sheet sheet, List<BRRS_M_AIDP_Summary_Entity2> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			System.out.println("came to write row data 1 method");
			
			for (BRRS_M_AIDP_Summary_Entity2 record : dataList) {
			
			for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
			  String rowCode = rowCodes[rowIndex];
			  Row row = sheet.getRow(baseRow + rowIndex);
			 
			  if (row == null) row = sheet.createRow(baseRow + rowIndex);
			
			  for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
			      String fieldName = rowCode+ "_" + fieldSuffixes[colIndex];
			   
			      // 👉 Skip column B (index 1)
			      int excelColIndex = (colIndex >= 1) ? colIndex + 1 : colIndex;

			      Cell cell = row.createCell(excelColIndex);
			      try {
			          Field field = BRRS_M_AIDP_Summary_Entity2.class.getDeclaredField(fieldName);
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
	
	private void writeRowData3(Sheet sheet, List<BRRS_M_AIDP_Summary_Entity3> dataList,
		    String[] rowCodes, String[] fieldSuffixes, int baseRow,
		    CellStyle numberStyle, CellStyle textStyle) {
			System.out.println("came to write row data 1 method");
			
			for (BRRS_M_AIDP_Summary_Entity3 record : dataList) {
			
			for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
			  String rowCode = rowCodes[rowIndex];
			  Row row = sheet.getRow(baseRow + rowIndex);
			 
			  if (row == null) row = sheet.createRow(baseRow + rowIndex);
			
			  for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
			      String fieldName = rowCode+ "_" + fieldSuffixes[colIndex];
			   
			      // 👉 Skip column B (index 1)
			      int excelColIndex = (colIndex >= 1) ? colIndex + 1 : colIndex;

			      Cell cell = row.createCell(excelColIndex);
			      try {
			          Field field = BRRS_M_AIDP_Summary_Entity3.class.getDeclaredField(fieldName);
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
	
	private void writeRowData4(Sheet sheet, List<BRRS_M_AIDP_Summary_Entity4> dataList,
	        String[] rowCodes, String[] fieldSuffixes, int baseRow,
	        CellStyle numberStyle, CellStyle textStyle) {
	    System.out.println("came to write row data 4 method");

	    for (BRRS_M_AIDP_Summary_Entity4 record : dataList) {

	        for (int rowIndex = 0; rowIndex < rowCodes.length; rowIndex++) {
	            String rowCode = rowCodes[rowIndex];
	            Row row = sheet.getRow(baseRow + rowIndex);

	            if (row == null) row = sheet.createRow(baseRow + rowIndex);

	            for (int colIndex = 0; colIndex < fieldSuffixes.length; colIndex++) {
	                String fieldName = rowCode + "_" + fieldSuffixes[colIndex];

	                // 👉 Direct mapping, don’t skip B
	                int excelColIndex = colIndex;

	                Cell cell = row.createCell(excelColIndex);
	                try {
	                    Field field = BRRS_M_AIDP_Summary_Entity4.class.getDeclaredField(fieldName);
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
