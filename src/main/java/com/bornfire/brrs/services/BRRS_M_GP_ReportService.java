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

import javax.transaction.Transactional;

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

import com.bornfire.brrs.entities.BRRS_M_GP_Archival_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_GP_Archival_Summary_Repo;
import com.bornfire.brrs.entities.BRRS_M_GP_Detail_Repo;
import com.bornfire.brrs.entities.BRRS_M_GP_Summary_Repo;
import com.bornfire.brrs.entities.M_GP_Archival_Detail_Entity;
import com.bornfire.brrs.entities.M_GP_Archival_Summary_Entity;
import com.bornfire.brrs.entities.M_GP_Detail_Entity;
import com.bornfire.brrs.entities.M_GP_Summary_Entity;

@Component
@Service

public class BRRS_M_GP_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_GP_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_GP_Summary_Repo BRRS_M_GP_Summary_Repo;

	@Autowired
	BRRS_M_GP_Archival_Summary_Repo M_GP_Archival_Summary_Repo;
	
	@Autowired
	BRRS_M_GP_Detail_Repo M_GP_Detail_Repo;
	
	@Autowired
	BRRS_M_GP_Archival_Detail_Repo M_GP_Archival_Detail_Repo;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_GPView(
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

	            List<M_GP_Archival_Summary_Entity> T1Master =
	            		M_GP_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- CASE 2: RESUB ----------
	        else if ("RESUB".equalsIgnoreCase(type) && version != null) {

	            List<M_GP_Archival_Summary_Entity> T1Master =
	            		M_GP_Archival_Summary_Repo.getdatabydateListarchival(d1, version);
	            mv.addObject("displaymode", "summary");
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- CASE 3: NORMAL ----------
	        else {

	            List<M_GP_Summary_Entity> T1Master =
	            		BRRS_M_GP_Summary_Repo.getdatabydateList(dateformat.parse(todate));
	            mv.addObject("displaymode", "summary");
	            System.out.println("T1Master Size " + T1Master.size());
	            mv.addObject("reportsummary", T1Master);
	        }

	        // ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
	        if ("detail".equalsIgnoreCase(dtltype)) {

	            // DETAIL + ARCHIVAL
	            if (version != null) {

	                List<M_GP_Archival_Detail_Entity> T1Master =
	                       M_GP_Archival_Detail_Repo
	                                .getdatabydateListarchival(d1, version);
	                mv.addObject("displaymode", "Details");
	                mv.addObject("reportsummary", T1Master);
	            }
	            // DETAIL + NORMAL
	            else {

	                List<M_GP_Detail_Entity> T1Master =
	                        M_GP_Detail_Repo
	                                .getdatabydateList(dateformat.parse(todate));
	                mv.addObject("displaymode", "Details");
	                mv.addObject("reportsummary", T1Master);
	            }
	        }

	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    mv.setViewName("BRRS/M_GP");
	   
	    System.out.println("View set to: " + mv.getViewName());

	    return mv;
	}

	// public List<Object> getM_GPArchival() {
	// List<Object> M_GPArchivallist = new ArrayList<>();
	// try {
	// M_GPArchivallist = M_GP_Archival_Summary_Repo.getM_GParchival();
	// System.out.println("countser" + M_GPArchivallist.size());
	// } catch (Exception e) {
	// // Log the exception
	// System.err.println("Error fetching M_SIR Archival data: " + e.getMessage());
	// e.printStackTrace();
	// }
	// return M_GPArchivallist;
	// }
//	public void updateReport(M_GP_Summary_Entity Entity) {
//		System.out.println("Report Date: " + Entity.getReportDate());
//
//		M_GP_Summary_Entity existing = BRRS_M_GP_Summary_Repo.findById(Entity.getReportDate())
//				.orElseThrow(() -> new RuntimeException(
//						"Record not found for REPORT_DATE: " + Entity.getReportDate()));
//
//		try {
//			int[] rowNumbers = { 12, 13, 14, 15, 29, 38 };
//			String[] fields = {
//					"STAGE1_PROVISIONS",
//					"QUALIFY_STAGE2_PROVISIONS",
//					"TOTAL_GENERAL_PROVISIONS"
//			};
//
//			for (int row : rowNumbers) {
//				String prefix = "R" + row + "_";
//				for (String field : fields) {
//					String getterName = "get" + prefix + field;
//					String setterName = "set" + prefix + field;
//
//					try {
//						Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//						Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(Entity);
//						setter.invoke(existing, newValue);
//					} catch (NoSuchMethodException e) {
//						// Skip missing field
//						continue;
//					}
//				}
//			}
//
//			for (String field : fields) {
//				String getterName = "getR11_" + field;
//				String setterName = "setR11_" + field;
//				try {
//					Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//					Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//					Object newValue = getter.invoke(Entity);
//					setter.invoke(existing, newValue);
//				} catch (NoSuchMethodException e) {
//					continue;
//				}
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating report fields", e);
//		}
//
//		try {
//
//			for (int i = 16; i <= 28; i++) {
//				String prefix = "R" + i + "_";
//				String[] fields = {
//						"STAGE1_PROVISIONS",
//						"QUALIFY_STAGE2_PROVISIONS",
//						"TOTAL_GENERAL_PROVISIONS"
//				};
//
//				for (String field : fields) {
//					String getterName = "get" + prefix + field;
//					String setterName = "set" + prefix + field;
//
//					try {
//						Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//						Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(Entity);
//						setter.invoke(existing, newValue);
//
//					} catch (NoSuchMethodException e) {
//
//						continue;
//					}
//				}
//			}
//
//			String[] totalFields = {
//					"STAGE1_PROVISIONS",
//					"QUALIFY_STAGE2_PROVISIONS",
//					"TOTAL_GENERAL_PROVISIONS"
//			};
//
//			for (String field : totalFields) {
//				String getterName = "getR15_" + field;
//				String setterName = "setR15_" + field;
//
//				try {
//					Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//					Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//					Object newValue = getter.invoke(Entity);
//					setter.invoke(existing, newValue);
//
//				} catch (NoSuchMethodException e) {
//					// Skip if method not present
//					continue;
//				}
//			}
//
//			// R29 = sum of R30–R37
//			try {
//
//				for (int i = 30; i <= 37; i++) {
//					String prefix = "R" + i + "_";
//					String[] fields = {
//							"STAGE1_PROVISIONS",
//							"QUALIFY_STAGE2_PROVISIONS",
//							"TOTAL_GENERAL_PROVISIONS"
//					};
//
//					for (String field : fields) {
//						String getterName = "get" + prefix + field;
//						String setterName = "set" + prefix + field;
//
//						try {
//							Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//							Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//							Object newValue = getter.invoke(Entity);
//							setter.invoke(existing, newValue);
//
//						} catch (NoSuchMethodException e) {
//
//							continue;
//						}
//					}
//				}
//
//				String[] totalFields1 = {
//						"STAGE1_PROVISIONS",
//						"QUALIFY_STAGE2_PROVISIONS",
//						"TOTAL_GENERAL_PROVISIONS"
//				};
//
//				for (String field : totalFields1) {
//					String getterName = "getR29_" + field;
//					String setterName = "setR29_" + field;
//
//					try {
//						Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//						Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(Entity);
//						setter.invoke(existing, newValue);
//
//					} catch (NoSuchMethodException e) {
//						continue;
//					}
//				}
//
//			} catch (Exception e) {
//				throw new RuntimeException("Error while updating R29 fields (30-37 and total)", e);
//			}
//
//			// R38 = R39 + R40
//			try {
//				int[] rowNumbers = { 39, 40 };
//				String[] fields = {
//						"STAGE1_PROVISIONS",
//						"QUALIFY_STAGE2_PROVISIONS",
//						"TOTAL_GENERAL_PROVISIONS"
//				};
//
//				for (int row : rowNumbers) {
//					String prefix = "R" + row + "_";
//					for (String field : fields) {
//						String getterName = "get" + prefix + field;
//						String setterName = "set" + prefix + field;
//
//						try {
//							Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//							Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//							Object newValue = getter.invoke(Entity);
//							setter.invoke(existing, newValue);
//						} catch (NoSuchMethodException e) {
//							continue;
//						}
//					}
//				}
//
//				for (String field : fields) {
//					String getterName = "getR38_" + field;
//					String setterName = "setR38_" + field;
//					try {
//						Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//						Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//						Object newValue = getter.invoke(Entity);
//						setter.invoke(existing, newValue);
//					} catch (NoSuchMethodException e) {
//						continue;
//					}
//				}
//
//			} catch (Exception e) {
//				throw new RuntimeException("Error while updating report fields", e);
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating R29 fields (39-40 and total)", e);
//		}
//		// R41 = R42 + R43 + R44 + R49 + R63
//
//		try {
//			int[] rowNumbers = { 42, 43, 44, 49, 63 };
//			String[] fields = {
//					"STAGE1_PROVISIONS",
//					"QUALIFY_STAGE2_PROVISIONS",
//					"TOTAL_GENERAL_PROVISIONS"
//			};
//
//			for (int row : rowNumbers) {
//				String prefix = "R" + row + "_";
//				for (String field : fields) {
//					String getterName = "get" + prefix + field;
//					String setterName = "set" + prefix + field;
//
//					try {
//						Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//						Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(Entity);
//						setter.invoke(existing, newValue);
//					} catch (NoSuchMethodException e) {
//						// Skip missing field
//						continue;
//					}
//				}
//			}
//
//			for (String field : fields) {
//				String getterName = "getR41_" + field;
//				String setterName = "setR41_" + field;
//				try {
//					Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//					Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//					Object newValue = getter.invoke(Entity);
//					setter.invoke(existing, newValue);
//				} catch (NoSuchMethodException e) {
//					continue;
//				}
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating report fields", e);
//		}
//		// R44 = R45 + R48
//		try {
//			int[] rowNumbers = { 45, 46,47,48 };
//			String[] fields = {
//					"STAGE1_PROVISIONS",
//					"QUALIFY_STAGE2_PROVISIONS",
//					"TOTAL_GENERAL_PROVISIONS"
//			};
//
//			for (int row : rowNumbers) {
//				String prefix = "R" + row + "_";
//				for (String field : fields) {
//					String getterName = "get" + prefix + field;
//					String setterName = "set" + prefix + field;
//
//					try {
//						Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//						Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(Entity);
//						setter.invoke(existing, newValue);
//					} catch (NoSuchMethodException e) {
//						// Skip missing field
//						continue;
//					}
//				}
//			}
//
//			for (String field : fields) {
//				String getterName = "getR44_" + field;
//				String setterName = "setR44_" + field;
//				try {
//					Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//					Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//					Object newValue = getter.invoke(Entity);
//					setter.invoke(existing, newValue);
//				} catch (NoSuchMethodException e) {
//					continue;
//				}
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating report fields", e);
//		}
//
//		// 49=B50+B51+B52+B53+B57
//		try {
//			int[] rowNumbers = { 50, 51, 52, 53, 57 };
//			String[] fields = {
//					"STAGE1_PROVISIONS",
//					"QUALIFY_STAGE2_PROVISIONS",
//					"TOTAL_GENERAL_PROVISIONS"
//			};
//
//			for (int row : rowNumbers) {
//				String prefix = "R" + row + "_";
//				for (String field : fields) {
//					String getterName = "get" + prefix + field;
//					String setterName = "set" + prefix + field;
//
//					try {
//						Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//						Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(Entity);
//						setter.invoke(existing, newValue);
//					} catch (NoSuchMethodException e) {
//						// Skip missing field
//						continue;
//					}
//				}
//			}
//
//			for (String field : fields) {
//				String getterName = "getR49_" + field;
//				String setterName = "setR49_" + field;
//				try {
//					Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//					Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//					Object newValue = getter.invoke(Entity);
//					setter.invoke(existing, newValue);
//				} catch (NoSuchMethodException e) {
//					continue;
//				}
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating report fields", e);
//		}
//		// R53 = R54 to R56
//		try {
//
//			for (int i = 54; i <= 56; i++) {
//				String prefix = "R" + i + "_";
//				String[] fields = {
//						"STAGE1_PROVISIONS",
//						"QUALIFY_STAGE2_PROVISIONS",
//						"TOTAL_GENERAL_PROVISIONS"
//				};
//
//				for (String field : fields) {
//					String getterName = "get" + prefix + field;
//					String setterName = "set" + prefix + field;
//
//					try {
//						Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//						Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(Entity);
//						setter.invoke(existing, newValue);
//
//					} catch (NoSuchMethodException e) {
//
//						continue;
//					}
//				}
//			}
//
//			String[] totalFields1 = {
//					"STAGE1_PROVISIONS",
//					"QUALIFY_STAGE2_PROVISIONS",
//					"TOTAL_GENERAL_PROVISIONS"
//			};
//
//			for (String field : totalFields1) {
//				String getterName = "getR53_" + field;
//				String setterName = "setR53_" + field;
//
//				try {
//					Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//					Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//					Object newValue = getter.invoke(Entity);
//					setter.invoke(existing, newValue);
//
//				} catch (NoSuchMethodException e) {
//					continue;
//				}
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating R29 fields (54,55,56 and total)", e);
//		}
//		// R57 = R58 + R62
//		try {
//
//			for (int i = 58; i <= 62; i++) {
//				String prefix = "R" + i + "_";
//				String[] fields = {
//						"STAGE1_PROVISIONS",
//						"QUALIFY_STAGE2_PROVISIONS",
//						"TOTAL_GENERAL_PROVISIONS"
//				};
//
//				for (String field : fields) {
//					String getterName = "get" + prefix + field;
//					String setterName = "set" + prefix + field;
//
//					try {
//						Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//						Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(Entity);
//						setter.invoke(existing, newValue);
//
//					} catch (NoSuchMethodException e) {
//
//						continue;
//					}
//				}
//			}
//
//			String[] totalFields1 = {
//					"STAGE1_PROVISIONS",
//					"QUALIFY_STAGE2_PROVISIONS",
//					"TOTAL_GENERAL_PROVISIONS"
//			};
//
//			for (String field : totalFields1) {
//				String getterName = "getR57_" + field;
//				String setterName = "setR57_" + field;
//
//				try {
//					Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//					Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//					Object newValue = getter.invoke(Entity);
//					setter.invoke(existing, newValue);
//
//				} catch (NoSuchMethodException e) {
//					continue;
//				}
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating R29 fields (57 and total)", e);
//		}
//
//		// R64 = 11 + 41
//		try {
//			int[] rowNumbers = { 11, 41 };
//			String[] fields = {
//					"STAGE1_PROVISIONS",
//					"QUALIFY_STAGE2_PROVISIONS",
//					"TOTAL_GENERAL_PROVISIONS"
//			};
//
//			for (int row : rowNumbers) {
//				String prefix = "R" + row + "_";
//				for (String field : fields) {
//					String getterName = "get" + prefix + field;
//					String setterName = "set" + prefix + field;
//
//					try {
//						Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//						Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//
//						Object newValue = getter.invoke(Entity);
//						setter.invoke(existing, newValue);
//					} catch (NoSuchMethodException e) {
//						// Skip missing field
//						continue;
//					}
//				}
//			}
//
//			for (String field : fields) {
//				String getterName = "getR64_" + field;
//				String setterName = "setR64_" + field;
//				try {
//					Method getter = M_GP_Summary_Entity.class.getMethod(getterName);
//					Method setter = M_GP_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
//					Object newValue = getter.invoke(Entity);
//					setter.invoke(existing, newValue);
//				} catch (NoSuchMethodException e) {
//					continue;
//				}
//			}
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error while updating report fields", e);
//		}
//
//		BRRS_M_GP_Summary_Repo.save(existing);
//	}

	public byte[] getM_GPExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		
		
		
		// ARCHIVAL check
        if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
            logger.info("Service: Generating ARCHIVAL report for version {}", version);
            return getExcelM_GPARCHIVAL(filename, reportId, fromdate, todate,
                    currency, dtltype, type, version);
        }
        // Email check
         if ("email".equalsIgnoreCase(type)  && version == null) {
            logger.info("Service: Generating Email report for version {}", version);
            return BRRS_M_GPEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
        } else if ("email".equalsIgnoreCase(type) && version != null) {
            logger.info("Service: Generating Email report for version {}", version);
            return BRRS_M_GPARCHIVALEmailExcel(filename, reportId, fromdate, todate,
                    currency, dtltype, type, version);

        }

	    /* ===================== NORMAL ===================== */
	    List<M_GP_Summary_Entity> dataList =
	    		BRRS_M_GP_Summary_Repo.getdatabydateList(dateformat.parse(todate));

	    if (dataList.isEmpty()) {
	        logger.warn("Service: No data found for M_GP report. Returning empty result.");
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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_GP_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			
					// row10
					// Column C

					Cell cell1 = row.getCell(1);
					if (record.getR11_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR11_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR11_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR11_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR11_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR11_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(11);

					cell1 = row.getCell(1);
					if (record.getR12_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR12_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(12);

					cell1 = row.getCell(1);
					if (record.getR13_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR13_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(13);
					cell1 = row.getCell(1);
					if (record.getR14_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR14_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row10
					// Column C
					row = sheet.getRow(14);

					cell1 = row.getCell(1);
					if (record.getR15_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR15_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR15_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR15_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR15_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR15_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(15);

					cell1 = row.getCell(1);
					if (record.getR16_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR16_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(16);

					cell1 = row.getCell(1);
					if (record.getR17_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR17_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(17);
					cell1 = row.getCell(1);
					if (record.getR18_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR18_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR18_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row10
					// Column C
					row = sheet.getRow(18);

					cell1 = row.getCell(1);
					if (record.getR19_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR19_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR19_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(19);

					cell1 = row.getCell(1);
					if (record.getR20_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR20_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR20_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(20);

					cell1 = row.getCell(1);
					if (record.getR21_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR21_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR21_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(21);
					cell1 = row.getCell(1);
					if (record.getR22_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR22_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR22_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row10
					// Column C
					row = sheet.getRow(22);

					cell1 = row.getCell(1);
					if (record.getR23_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR23_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR23_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(23);

					cell1 = row.getCell(1);
					if (record.getR24_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR24_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR24_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(24);

					cell1 = row.getCell(1);
					if (record.getR25_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR25_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR25_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(25);
					cell1 = row.getCell(1);
					if (record.getR26_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR26_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR26_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row10
					// Column C
					row = sheet.getRow(26);

					cell1 = row.getCell(1);
					if (record.getR27_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR27_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR27_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(27);

					cell1 = row.getCell(1);
					if (record.getR28_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR28_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR28_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(28);

					cell1 = row.getCell(1);
					if (record.getR29_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR29_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR29_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR29_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR29_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR29_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(29);
					cell1 = row.getCell(1);
					if (record.getR30_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR30_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR30_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row10
					// Column C
					row = sheet.getRow(30);

					cell1 = row.getCell(1);
					if (record.getR31_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR31_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR31_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(31);

					cell1 = row.getCell(1);
					if (record.getR32_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR32_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR32_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(32);

					cell1 = row.getCell(1);
					if (record.getR33_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR33_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR33_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column C
					row = sheet.getRow(33);

					cell1 = row.getCell(1);
					if (record.getR34_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR34_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR34_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);

					cell1 = row.getCell(1);
					if (record.getR35_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR35_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR35_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column C
					row = sheet.getRow(35);

					cell1 = row.getCell(1);
					if (record.getR36_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR36_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR36_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column C
					row = sheet.getRow(36);

					cell1 = row.getCell(1);
					if (record.getR37_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR37_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR37_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);

					cell1 = row.getCell(1);
					if (record.getR38_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR38_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR38_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR38_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR38_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR38_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column C
					row = sheet.getRow(38);

					cell1 = row.getCell(1);
					if (record.getR39_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR39_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR39_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// ===== R40 =====
					row = sheet.getRow(39);

					cell1 = row.getCell(1);
					if (record.getR40_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR40_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR40_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R41 =====
					row = sheet.getRow(40);

					cell1 = row.getCell(1);
					if (record.getR41_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR41_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR41_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR41_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR41_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR41_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R42 =====
					row = sheet.getRow(41);

					cell1 = row.getCell(1);
					if (record.getR42_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR42_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR42_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R43 =====
					row = sheet.getRow(42);

					cell1 = row.getCell(1);
					if (record.getR43_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR43_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR43_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R44 =====
					row = sheet.getRow(43);

					cell1 = row.getCell(1);
					if (record.getR44_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR44_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR44_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR44_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR44_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR44_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R45 =====
					row = sheet.getRow(44);

					cell1 = row.getCell(1);
					if (record.getR45_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR45_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR45_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// ===== R46 =====
					row = sheet.getRow(45);

					cell1 = row.getCell(1);
					if (record.getR46_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR46_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR46_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R47 =====
					row = sheet.getRow(46);

					cell1 = row.getCell(1);
					if (record.getR47_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR47_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR47_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R48 =====
					row = sheet.getRow(47);

					cell1 = row.getCell(1);
					if (record.getR48_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR48_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR48_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R49 =====
					row = sheet.getRow(48);

					cell1 = row.getCell(1);
					if (record.getR49_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR49_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR49_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR49_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR49_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR49_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R50 =====
					row = sheet.getRow(49);

					cell1 = row.getCell(1);
					if (record.getR50_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR50_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR50_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// ===== R51 =====
					row = sheet.getRow(50);

					cell1 = row.getCell(1);
					if (record.getR51_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR51_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR51_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R52 =====
					row = sheet.getRow(51);

					cell1 = row.getCell(1);
					if (record.getR52_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR52_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR52_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R53 =====
					row = sheet.getRow(52);

					cell1 = row.getCell(1);
					if (record.getR53_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR53_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR53_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR53_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR53_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR53_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R54 =====
					row = sheet.getRow(53);

					cell1 = row.getCell(1);
					if (record.getR54_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR54_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR54_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R55 =====
					row = sheet.getRow(54);

					cell1 = row.getCell(1);
					if (record.getR55_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR55_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR55_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// ===== R56 =====
					row = sheet.getRow(55);

					cell1 = row.getCell(1);
					if (record.getR56_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR56_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR56_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R57 =====
					row = sheet.getRow(56);

					cell1 = row.getCell(1);
					if (record.getR57_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR57_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR57_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR57_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR57_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR57_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R58 =====
					row = sheet.getRow(57);

					cell1 = row.getCell(1);
					if (record.getR58_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR58_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR58_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R59 =====
					row = sheet.getRow(58);

					cell1 = row.getCell(1);
					if (record.getR59_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR59_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR59_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R60 =====
					row = sheet.getRow(59);

					cell1 = row.getCell(1);
					if (record.getR60_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR60_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR60_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R61 =====
					row = sheet.getRow(60);

					cell1 = row.getCell(1);
					if (record.getR61_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR61_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR61_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R62 =====
					row = sheet.getRow(61);

					cell1 = row.getCell(1);
					if (record.getR62_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR62_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR62_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R63 =====
					row = sheet.getRow(62);

					cell1 = row.getCell(1);
					if (record.getR63_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR63_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR63_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R64 =====
					row = sheet.getRow(63);

					cell1 = row.getCell(1);
					if (record.getR64_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR64_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR64_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR64_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR64_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR64_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
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

	public byte[] getExcelM_GPARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);

		List<M_GP_Archival_Summary_Entity> dataList = M_GP_Archival_Summary_Repo
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_GP report. Returning empty result.");
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

							int startRow = 11;

							if (!dataList.isEmpty()) {
								for (int i = 0; i < dataList.size(); i++) {

									M_GP_Archival_Summary_Entity record = dataList.get(i);
									System.out.println("rownumber=" + startRow + i);
									Row row = sheet.getRow(startRow + i);
									if (row == null) {
										row = sheet.createRow(startRow + i);
									}
							
									// row10
									// Column C

									Cell cell1 = row.getCell(1);
									if (record.getR11_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR11_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR11_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR11_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR11_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR11_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(11);

									cell1 = row.getCell(1);
									if (record.getR12_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR12_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR12_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(12);

									cell1 = row.getCell(1);
									if (record.getR13_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR13_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR13_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(13);
									cell1 = row.getCell(1);
									if (record.getR14_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR14_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR14_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row10
									// Column C
									row = sheet.getRow(14);

									cell1 = row.getCell(1);
									if (record.getR15_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR15_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR15_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR15_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR15_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR15_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(15);

									cell1 = row.getCell(1);
									if (record.getR16_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR16_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR16_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(16);

									cell1 = row.getCell(1);
									if (record.getR17_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR17_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR17_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(17);
									cell1 = row.getCell(1);
									if (record.getR18_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR18_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR18_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row10
									// Column C
									row = sheet.getRow(18);

									cell1 = row.getCell(1);
									if (record.getR19_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR19_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR19_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(19);

									cell1 = row.getCell(1);
									if (record.getR20_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR20_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR20_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(20);

									cell1 = row.getCell(1);
									if (record.getR21_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR21_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR21_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(21);
									cell1 = row.getCell(1);
									if (record.getR22_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR22_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR22_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row10
									// Column C
									row = sheet.getRow(22);

									cell1 = row.getCell(1);
									if (record.getR23_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR23_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR23_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(23);

									cell1 = row.getCell(1);
									if (record.getR24_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR24_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR24_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(24);

									cell1 = row.getCell(1);
									if (record.getR25_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR25_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR25_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(25);
									cell1 = row.getCell(1);
									if (record.getR26_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR26_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR26_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row10
									// Column C
									row = sheet.getRow(26);

									cell1 = row.getCell(1);
									if (record.getR27_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR27_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR27_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(27);

									cell1 = row.getCell(1);
									if (record.getR28_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR28_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR28_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(28);

									cell1 = row.getCell(1);
									if (record.getR29_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR29_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR29_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR29_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR29_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR29_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(29);
									cell1 = row.getCell(1);
									if (record.getR30_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR30_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR30_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row10
									// Column C
									row = sheet.getRow(30);

									cell1 = row.getCell(1);
									if (record.getR31_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR31_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR31_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(31);

									cell1 = row.getCell(1);
									if (record.getR32_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR32_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR32_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// row10
									// Column C
									row = sheet.getRow(32);

									cell1 = row.getCell(1);
									if (record.getR33_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column E
									cell1 = row.getCell(2);
									if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// row11
									// Column F
									cell1 = row.getCell(3);
									if (record.getR33_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR33_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// Column C
									row = sheet.getRow(33);

									cell1 = row.getCell(1);
									if (record.getR34_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR34_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR34_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									row = sheet.getRow(34);

									cell1 = row.getCell(1);
									if (record.getR35_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR35_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR35_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// Column C
									row = sheet.getRow(35);

									cell1 = row.getCell(1);
									if (record.getR36_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR36_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR36_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// Column C
									row = sheet.getRow(36);

									cell1 = row.getCell(1);
									if (record.getR37_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR37_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR37_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									row = sheet.getRow(37);

									cell1 = row.getCell(1);
									if (record.getR38_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR38_STAGE1_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR38_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR38_QUALIFY_STAGE2_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR38_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR38_TOTAL_GENERAL_PROVISIONS().doubleValue());

									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// Column C
									row = sheet.getRow(38);

									cell1 = row.getCell(1);
									if (record.getR39_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR39_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR39_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// ===== R40 =====
									row = sheet.getRow(39);

									cell1 = row.getCell(1);
									if (record.getR40_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR40_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR40_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R41 =====
									row = sheet.getRow(40);

									cell1 = row.getCell(1);
									if (record.getR41_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR41_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR41_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR41_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR41_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR41_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R42 =====
									row = sheet.getRow(41);

									cell1 = row.getCell(1);
									if (record.getR42_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR42_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR42_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R43 =====
									row = sheet.getRow(42);

									cell1 = row.getCell(1);
									if (record.getR43_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR43_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR43_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R44 =====
									row = sheet.getRow(43);

									cell1 = row.getCell(1);
									if (record.getR44_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR44_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR44_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR44_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR44_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR44_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R45 =====
									row = sheet.getRow(44);

									cell1 = row.getCell(1);
									if (record.getR45_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR45_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR45_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// ===== R46 =====
									row = sheet.getRow(45);

									cell1 = row.getCell(1);
									if (record.getR46_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR46_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR46_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R47 =====
									row = sheet.getRow(46);

									cell1 = row.getCell(1);
									if (record.getR47_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR47_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR47_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R48 =====
									row = sheet.getRow(47);

									cell1 = row.getCell(1);
									if (record.getR48_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR48_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR48_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R49 =====
									row = sheet.getRow(48);

									cell1 = row.getCell(1);
									if (record.getR49_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR49_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR49_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR49_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR49_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR49_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R50 =====
									row = sheet.getRow(49);

									cell1 = row.getCell(1);
									if (record.getR50_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR50_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR50_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// ===== R51 =====
									row = sheet.getRow(50);

									cell1 = row.getCell(1);
									if (record.getR51_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR51_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR51_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R52 =====
									row = sheet.getRow(51);

									cell1 = row.getCell(1);
									if (record.getR52_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR52_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR52_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R53 =====
									row = sheet.getRow(52);

									cell1 = row.getCell(1);
									if (record.getR53_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR53_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR53_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR53_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR53_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR53_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R54 =====
									row = sheet.getRow(53);

									cell1 = row.getCell(1);
									if (record.getR54_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR54_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR54_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R55 =====
									row = sheet.getRow(54);

									cell1 = row.getCell(1);
									if (record.getR55_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR55_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR55_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}
									// ===== R56 =====
									row = sheet.getRow(55);

									cell1 = row.getCell(1);
									if (record.getR56_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR56_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR56_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R57 =====
									row = sheet.getRow(56);

									cell1 = row.getCell(1);
									if (record.getR57_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR57_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR57_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR57_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR57_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR57_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R58 =====
									row = sheet.getRow(57);

									cell1 = row.getCell(1);
									if (record.getR58_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR58_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR58_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R59 =====
									row = sheet.getRow(58);

									cell1 = row.getCell(1);
									if (record.getR59_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR59_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR59_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R60 =====
									row = sheet.getRow(59);

									cell1 = row.getCell(1);
									if (record.getR60_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR60_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR60_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R61 =====
									row = sheet.getRow(60);

									cell1 = row.getCell(1);
									if (record.getR61_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR61_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR61_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R62 =====
									row = sheet.getRow(61);

									cell1 = row.getCell(1);
									if (record.getR62_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR62_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR62_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R63 =====
									row = sheet.getRow(62);

									cell1 = row.getCell(1);
									if (record.getR63_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR63_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR63_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									// ===== R64 =====
									row = sheet.getRow(63);

									cell1 = row.getCell(1);
									if (record.getR64_STAGE1_PROVISIONS() != null) {
										cell1.setCellValue(record.getR64_STAGE1_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(2);
									if (record.getR64_QUALIFY_STAGE2_PROVISIONS() != null) {
										cell1.setCellValue(record.getR64_QUALIFY_STAGE2_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
									}

									cell1 = row.getCell(3);
									if (record.getR64_TOTAL_GENERAL_PROVISIONS() != null) {
										cell1.setCellValue(record.getR64_TOTAL_GENERAL_PROVISIONS().doubleValue());
									} else {
										cell1.setCellValue("");
										cell1.setCellStyle(textStyle);
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

	////////////////////////////////////////// RESUBMISSION///////////////////////////////////////////////////////////////////
	/// Report Date | Report Version | Domain
	/// RESUB VIEW

	public List<Object[]> getM_GPResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_GP_Archival_Summary_Entity> latestArchivalList = M_GP_Archival_Summary_Repo
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_GP_Archival_Summary_Entity entity : latestArchivalList) {
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
			System.err.println("Error fetching M_GP Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

//	public List<Object[]> getM_GPArchival() {
//		List<Object[]> archivalList = new ArrayList<>();
//		try {
//			List<M_GP_Archival_Summary_Entity> latestArchivalList = M_GP_Archival_Summary_Repo
//					.getdatabydateListWithVersion();
//
//			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
//				for (M_GP_Archival_Summary_Entity entity : latestArchivalList) {
//					archivalList.add(new Object[] {
//							entity.getReportDate(),
//							entity.getReportVersion()
//					});
//				}
//				System.out.println("Fetched " + archivalList.size() + " record(s)");
//			} else {
//				System.out.println("No archival data found.");
//			}
//
//		} catch (Exception e) {
//			System.err.println("Error fetching M_GP Resub data: " + e.getMessage());
//			e.printStackTrace();
//		}
//		return archivalList;
//	}

	// Resubmit the values , latest version and Resub Date
//	public void updateReportReSub(M_GP_Summary_Entity updatedEntity) {
//		System.out.println("Came to Resub Service");
//		System.out.println("Report Date: " + updatedEntity.getReportDate());
//
//		Date reportDate = updatedEntity.getReportDate();
//		int newVersion = 1;
//
//		try {
//			// Fetch the latest archival version for this report date
//			Optional<M_GP_Archival_Summary_Entity> latestArchivalOpt = M_GP_Archival_Summary_Repo
//					.getLatestArchivalVersionByDate(reportDate);
//
//			// Determine next version number
//			if (latestArchivalOpt.isPresent()) {
//				M_GP_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
//				try {
//					newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1;
//				} catch (NumberFormatException e) {
//					System.err.println("Invalid version format. Defaulting to version 1");
//					newVersion = 1;
//				}
//			} else {
//				System.out.println("No previous archival found for date: " + reportDate);
//			}
//
//			// Prevent duplicate version number
//			boolean exists = M_GP_Archival_Summary_Repo
//					.findByReportDateAndReportVersion(reportDate, BigDecimal.valueOf(newVersion))
//					.isPresent();
//
//			if (exists) {
//				throw new RuntimeException("Version " + newVersion + " already exists for report date " + reportDate);
//			}
//
//			// Copy summary entity to archival entity
//			M_GP_Archival_Summary_Entity archivalEntity = new M_GP_Archival_Summary_Entity();
//			org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);
//
//			archivalEntity.setReportDate(reportDate);
//			archivalEntity.setReportVersion(BigDecimal.valueOf(newVersion));
//			archivalEntity.setReportResubDate(new Date());
//
//			System.out.println("Saving new archival version: " + newVersion);
//
//			// Save new version to repository
//			M_GP_Archival_Summary_Repo.save(archivalEntity);
//
//			System.out.println(" Saved archival version successfully: " + newVersion);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException("Error while creating archival resubmission record", e);
//		}
//	}

	/// Downloaded for Archival & Resub
	public byte[] BRRS_M_GPResubExcel(String filename, String reportId, String fromdate,
        String todate, String currency, String dtltype,
        String type, BigDecimal version) throws Exception {

    logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

    if (type.equals("RESUB") & version != null) {
       
    }

    List<M_GP_Archival_Summary_Entity> dataList =
        M_GP_Archival_Summary_Repo.getdatabydateListarchival(dateformat.parse(todate), version);

    if (dataList.isEmpty()) {
        logger.warn("Service: No data found for M_GP report. Returning empty result.");
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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_GP_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
			
					// row10
					// Column C

					Cell cell1 = row.getCell(1);
					if (record.getR11_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR11_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR11_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR11_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR11_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR11_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(11);

					cell1 = row.getCell(1);
					if (record.getR12_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR12_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR12_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(12);

					cell1 = row.getCell(1);
					if (record.getR13_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR13_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR13_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(13);
					cell1 = row.getCell(1);
					if (record.getR14_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR14_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR14_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row10
					// Column C
					row = sheet.getRow(14);

					cell1 = row.getCell(1);
					if (record.getR15_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR15_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR15_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR15_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR15_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR15_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(15);

					cell1 = row.getCell(1);
					if (record.getR16_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR16_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR16_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(16);

					cell1 = row.getCell(1);
					if (record.getR17_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR17_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR17_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(17);
					cell1 = row.getCell(1);
					if (record.getR18_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR18_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR18_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row10
					// Column C
					row = sheet.getRow(18);

					cell1 = row.getCell(1);
					if (record.getR19_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR19_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR19_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(19);

					cell1 = row.getCell(1);
					if (record.getR20_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR20_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR20_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(20);

					cell1 = row.getCell(1);
					if (record.getR21_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR21_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR21_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(21);
					cell1 = row.getCell(1);
					if (record.getR22_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR22_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR22_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row10
					// Column C
					row = sheet.getRow(22);

					cell1 = row.getCell(1);
					if (record.getR23_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR23_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR23_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(23);

					cell1 = row.getCell(1);
					if (record.getR24_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR24_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR24_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(24);

					cell1 = row.getCell(1);
					if (record.getR25_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR25_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR25_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(25);
					cell1 = row.getCell(1);
					if (record.getR26_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR26_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR26_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row10
					// Column C
					row = sheet.getRow(26);

					cell1 = row.getCell(1);
					if (record.getR27_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR27_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR27_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(27);

					cell1 = row.getCell(1);
					if (record.getR28_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR28_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR28_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(28);

					cell1 = row.getCell(1);
					if (record.getR29_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR29_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR29_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR29_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR29_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR29_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(29);
					cell1 = row.getCell(1);
					if (record.getR30_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR30_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR30_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row10
					// Column C
					row = sheet.getRow(30);

					cell1 = row.getCell(1);
					if (record.getR31_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR31_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR31_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(31);

					cell1 = row.getCell(1);
					if (record.getR32_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR32_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR32_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row10
					// Column C
					row = sheet.getRow(32);

					cell1 = row.getCell(1);
					if (record.getR33_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column E
					cell1 = row.getCell(2);
					if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// row11
					// Column F
					cell1 = row.getCell(3);
					if (record.getR33_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR33_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column C
					row = sheet.getRow(33);

					cell1 = row.getCell(1);
					if (record.getR34_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR34_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR34_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);

					cell1 = row.getCell(1);
					if (record.getR35_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR35_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR35_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column C
					row = sheet.getRow(35);

					cell1 = row.getCell(1);
					if (record.getR36_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR36_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR36_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Column C
					row = sheet.getRow(36);

					cell1 = row.getCell(1);
					if (record.getR37_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR37_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR37_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);

					cell1 = row.getCell(1);
					if (record.getR38_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR38_STAGE1_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR38_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR38_QUALIFY_STAGE2_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR38_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR38_TOTAL_GENERAL_PROVISIONS().doubleValue());

					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// Column C
					row = sheet.getRow(38);

					cell1 = row.getCell(1);
					if (record.getR39_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR39_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR39_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// ===== R40 =====
					row = sheet.getRow(39);

					cell1 = row.getCell(1);
					if (record.getR40_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR40_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR40_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R41 =====
					row = sheet.getRow(40);

					cell1 = row.getCell(1);
					if (record.getR41_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR41_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR41_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR41_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR41_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR41_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R42 =====
					row = sheet.getRow(41);

					cell1 = row.getCell(1);
					if (record.getR42_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR42_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR42_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R43 =====
					row = sheet.getRow(42);

					cell1 = row.getCell(1);
					if (record.getR43_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR43_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR43_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R44 =====
					row = sheet.getRow(43);

					cell1 = row.getCell(1);
					if (record.getR44_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR44_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR44_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR44_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR44_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR44_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R45 =====
					row = sheet.getRow(44);

					cell1 = row.getCell(1);
					if (record.getR45_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR45_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR45_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// ===== R46 =====
					row = sheet.getRow(45);

					cell1 = row.getCell(1);
					if (record.getR46_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR46_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR46_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R47 =====
					row = sheet.getRow(46);

					cell1 = row.getCell(1);
					if (record.getR47_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR47_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR47_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R48 =====
					row = sheet.getRow(47);

					cell1 = row.getCell(1);
					if (record.getR48_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR48_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR48_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R49 =====
					row = sheet.getRow(48);

					cell1 = row.getCell(1);
					if (record.getR49_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR49_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR49_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR49_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR49_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR49_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R50 =====
					row = sheet.getRow(49);

					cell1 = row.getCell(1);
					if (record.getR50_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR50_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR50_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// ===== R51 =====
					row = sheet.getRow(50);

					cell1 = row.getCell(1);
					if (record.getR51_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR51_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR51_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R52 =====
					row = sheet.getRow(51);

					cell1 = row.getCell(1);
					if (record.getR52_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR52_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR52_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R53 =====
					row = sheet.getRow(52);

					cell1 = row.getCell(1);
					if (record.getR53_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR53_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR53_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR53_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR53_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR53_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R54 =====
					row = sheet.getRow(53);

					cell1 = row.getCell(1);
					if (record.getR54_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR54_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR54_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R55 =====
					row = sheet.getRow(54);

					cell1 = row.getCell(1);
					if (record.getR55_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR55_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR55_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					// ===== R56 =====
					row = sheet.getRow(55);

					cell1 = row.getCell(1);
					if (record.getR56_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR56_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR56_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R57 =====
					row = sheet.getRow(56);

					cell1 = row.getCell(1);
					if (record.getR57_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR57_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR57_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR57_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR57_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR57_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R58 =====
					row = sheet.getRow(57);

					cell1 = row.getCell(1);
					if (record.getR58_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR58_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR58_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R59 =====
					row = sheet.getRow(58);

					cell1 = row.getCell(1);
					if (record.getR59_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR59_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR59_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R60 =====
					row = sheet.getRow(59);

					cell1 = row.getCell(1);
					if (record.getR60_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR60_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR60_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R61 =====
					row = sheet.getRow(60);

					cell1 = row.getCell(1);
					if (record.getR61_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR61_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR61_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R62 =====
					row = sheet.getRow(61);

					cell1 = row.getCell(1);
					if (record.getR62_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR62_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR62_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R63 =====
					row = sheet.getRow(62);

					cell1 = row.getCell(1);
					if (record.getR63_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR63_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR63_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ===== R64 =====
					row = sheet.getRow(63);

					cell1 = row.getCell(1);
					if (record.getR64_STAGE1_PROVISIONS() != null) {
						cell1.setCellValue(record.getR64_STAGE1_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(2);
					if (record.getR64_QUALIFY_STAGE2_PROVISIONS() != null) {
						cell1.setCellValue(record.getR64_QUALIFY_STAGE2_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell1 = row.getCell(3);
					if (record.getR64_TOTAL_GENERAL_PROVISIONS() != null) {
						cell1.setCellValue(record.getR64_TOTAL_GENERAL_PROVISIONS().doubleValue());
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
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
	
	public List<Object> getM_GPArchival() {
		List<Object> M_GPArchivallist = new ArrayList<>();
		try {
			M_GPArchivallist = M_GP_Archival_Summary_Repo.getM_GP_archival();
		
		
			System.out.println("countser" + M_GPArchivallist.size());
			
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_GPArchivallist Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_GPArchivallist;
	}
	
	

@Transactional
	public void updateReport(M_GP_Summary_Entity updatedEntity) {

	    System.out.println("Came to services");
	    System.out.println("Report Date: " + updatedEntity.getReportDate());

	    // 1️⃣ Fetch existing SUMMARY
	     M_GP_Summary_Entity existingSummary =
	    		 BRRS_M_GP_Summary_Repo.findById(updatedEntity.getReportDate())
	                    .orElseThrow(() -> new RuntimeException(
	                            "Summary record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

	    // 2️⃣ Fetch or create DETAIL
	      M_GP_Detail_Entity existingDetail =
	            M_GP_Detail_Repo.findById(updatedEntity.getReportDate())
	                    .orElseGet(() -> {
	                          M_GP_Detail_Entity d = new   M_GP_Detail_Entity();
	                        d.setReportDate(updatedEntity.getReportDate());
	                        return d;
	                    });

	  try {
	        // 1️⃣ Loop from R11 to R64 and copy fields
	    	
	        for (int i = 11; i <= 64; i++) {
				
	        	 String prefix = "R" + i + "_";

	            String[] fields = { "STAGE1_PROVISIONS",
					"QUALIFY_STAGE2_PROVISIONS",
					"TOTAL_GENERAL_PROVISIONS" };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {
	                    Method getter =
	                              M_GP_Summary_Entity.class.getMethod(getterName);

	                    Method summarySetter =
	                              M_GP_Summary_Entity.class.getMethod(
	                                    setterName, getter.getReturnType());

	                    Method detailSetter =
	                              M_GP_Detail_Entity.class.getMethod(
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
	  BRRS_M_GP_Summary_Repo.save(existingSummary);
	    M_GP_Detail_Repo.save(existingDetail);
		
		}


public byte[] BRRS_M_GPEmailExcel(String filename, String reportId, String fromdate, String todate,
		String currency, String dtltype, String type, BigDecimal version) throws Exception {

	logger.info("Service: Starting Excel generation process in memory.");

	

	List<M_GP_Summary_Entity> dataList = BRRS_M_GP_Summary_Repo
			.getdatabydateList(dateformat.parse(todate));

	if (dataList.isEmpty()) {
		logger.warn("Service: No data found for M_GP_email report. Returning empty result.");
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

		int startRow = 7;

		if (!dataList.isEmpty()) {
			for (int i = 0; i < dataList.size(); i++) {
				M_GP_Summary_Entity record = dataList.get(i);
				System.out.println("rownumber=" + startRow + i);
				Row row = sheet.getRow(startRow + i);
				if (row == null) {
					row = sheet.createRow(startRow + i);
				}
				
				//-------------------8
				
				
				Cell cell1 = row.getCell(1);
				if (record.getR12_STAGE1_PROVISIONS() != null) {
					cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				// row11
				// Column E
				 cell1 = row.getCell(2);
				if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
					cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				
			//-------------------9
				
				// Column C
				row = sheet.getRow(8);

				cell1 = row.getCell(1);
				if (record.getR13_STAGE1_PROVISIONS() != null) {
					cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				// row11
				// Column E
				cell1 = row.getCell(2);
				if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
					cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				
			//-------------------10
			
				row = sheet.getRow(9);
				
				cell1 = row.getCell(1);
				if (record.getR14_STAGE1_PROVISIONS() != null) {
					cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				// row11
				// Column E
				cell1 = row.getCell(2);
				if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
					cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				
			//-------------------12
			
			row = sheet.getRow(11);

				cell1 = row.getCell(1);
				if (record.getR16_STAGE1_PROVISIONS() != null) {
					cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				// row11
				// Column E
				cell1 = row.getCell(2);
				if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
					cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
			
			//-------------------13
			
			row = sheet.getRow(12);

				cell1 = row.getCell(1);
				if (record.getR17_STAGE1_PROVISIONS() != null) {
					cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				// row11
				// Column E
				cell1 = row.getCell(2);
				if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
					cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				
				
					//-------------------14
					
					
					
row = sheet.getRow(13);

cell1 = row.getCell(1);
if (record.getR18_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------15
row = sheet.getRow(14);

cell1 = row.getCell(1);
if (record.getR19_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------16
row = sheet.getRow(15);

cell1 = row.getCell(1);
if (record.getR20_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------17
row = sheet.getRow(16);

cell1 = row.getCell(1);
if (record.getR21_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------18
row = sheet.getRow(17);

cell1 = row.getCell(1);
if (record.getR22_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------19
row = sheet.getRow(18);

cell1 = row.getCell(1);
if (record.getR23_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------20
row = sheet.getRow(19);

cell1 = row.getCell(1);
if (record.getR24_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------21
row = sheet.getRow(20);

cell1 = row.getCell(1);
if (record.getR25_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------22
row = sheet.getRow(21);

cell1 = row.getCell(1);
if (record.getR26_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------23
row = sheet.getRow(22);

cell1 = row.getCell(1);
if (record.getR27_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------24
row = sheet.getRow(23);

cell1 = row.getCell(1);
if (record.getR28_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}



//-------------------26
row = sheet.getRow(25);

cell1 = row.getCell(1);
if (record.getR30_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------27
row = sheet.getRow(26);

cell1 = row.getCell(1);
if (record.getR31_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------28
row = sheet.getRow(27);

cell1 = row.getCell(1);
if (record.getR32_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------29
row = sheet.getRow(28);

cell1 = row.getCell(1);
if (record.getR33_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------30
row = sheet.getRow(29);

cell1 = row.getCell(1);
if (record.getR34_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------31
row = sheet.getRow(30);

cell1 = row.getCell(1);
if (record.getR35_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------32
row = sheet.getRow(31);

cell1 = row.getCell(1);
if (record.getR36_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------33
row = sheet.getRow(32);

cell1 = row.getCell(1);
if (record.getR37_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}


//-------------------35



row = sheet.getRow(34);

cell1 = row.getCell(1);
if (record.getR39_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------36
row = sheet.getRow(35);

cell1 = row.getCell(1);
if (record.getR40_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
	

	//-------------------38
row = sheet.getRow(37);

cell1 = row.getCell(1);
if (record.getR42_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------39
row = sheet.getRow(38);

cell1 = row.getCell(1);
if (record.getR43_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
	
//-------------------41	
row = sheet.getRow(40);

cell1 = row.getCell(1);
if (record.getR45_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------42
row = sheet.getRow(41);

cell1 = row.getCell(1);
if (record.getR46_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	


//-------------------43
row = sheet.getRow(42);

cell1 = row.getCell(1);
if (record.getR47_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------44
row = sheet.getRow(43);

cell1 = row.getCell(1);
if (record.getR48_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------46
row = sheet.getRow(45);

cell1 = row.getCell(1);
if (record.getR50_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------47
row = sheet.getRow(46);

cell1 = row.getCell(1);
if (record.getR51_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------48
row = sheet.getRow(47);

cell1 = row.getCell(1);
if (record.getR52_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}


//-------------------50
row = sheet.getRow(49);

cell1 = row.getCell(1);
if (record.getR54_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------51
row = sheet.getRow(50);

cell1 = row.getCell(1);
if (record.getR55_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------52
row = sheet.getRow(51);

cell1 = row.getCell(1);
if (record.getR56_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------54
row = sheet.getRow(53);

cell1 = row.getCell(1);
if (record.getR58_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------55
row = sheet.getRow(54);

cell1 = row.getCell(1);
if (record.getR59_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------56
row = sheet.getRow(55);

cell1 = row.getCell(1);
if (record.getR60_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------57
row = sheet.getRow(56);

cell1 = row.getCell(1);
if (record.getR61_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------58
row = sheet.getRow(57);

cell1 = row.getCell(1);
if (record.getR62_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------59
row = sheet.getRow(58);

cell1 = row.getCell(1);
if (record.getR63_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
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



public byte[] BRRS_M_GPARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
		String currency, String dtltype, String type, BigDecimal version) throws Exception {

	logger.info("Service: Starting Excel generation process in memory.");

	if (type.equals("ARCHIVAL") & version != null) {

	}

	List<M_GP_Archival_Summary_Entity> dataList = M_GP_Archival_Summary_Repo
			.getdatabydateListarchival(dateformat.parse(todate), version);

	if (dataList.isEmpty()) {
		logger.warn("Service: No data found for M_GP_email_ARCHIVAL report. Returning empty result.");
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

		int startRow = 7;

		if (!dataList.isEmpty()) {
			for (int i = 0; i < dataList.size(); i++) {
				M_GP_Archival_Summary_Entity record = dataList.get(i);
				System.out.println("rownumber=" + startRow + i);
				Row row = sheet.getRow(startRow + i);
				if (row == null) {
					row = sheet.createRow(startRow + i);
				}

				
									//-------------------8
				
				
				Cell cell1 = row.getCell(1);
				if (record.getR12_STAGE1_PROVISIONS() != null) {
					cell1.setCellValue(record.getR12_STAGE1_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				// row11
				// Column E
				 cell1 = row.getCell(2);
				if (record.getR12_QUALIFY_STAGE2_PROVISIONS() != null) {
					cell1.setCellValue(record.getR12_QUALIFY_STAGE2_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				
			//-------------------9
				
				// Column C
				row = sheet.getRow(8);

				cell1 = row.getCell(1);
				if (record.getR13_STAGE1_PROVISIONS() != null) {
					cell1.setCellValue(record.getR13_STAGE1_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				// row11
				// Column E
				cell1 = row.getCell(2);
				if (record.getR13_QUALIFY_STAGE2_PROVISIONS() != null) {
					cell1.setCellValue(record.getR13_QUALIFY_STAGE2_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				
			//-------------------10
			
				row = sheet.getRow(9);
				
				cell1 = row.getCell(1);
				if (record.getR14_STAGE1_PROVISIONS() != null) {
					cell1.setCellValue(record.getR14_STAGE1_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				// row11
				// Column E
				cell1 = row.getCell(2);
				if (record.getR14_QUALIFY_STAGE2_PROVISIONS() != null) {
					cell1.setCellValue(record.getR14_QUALIFY_STAGE2_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				
			//-------------------12
			
			row = sheet.getRow(11);

				cell1 = row.getCell(1);
				if (record.getR16_STAGE1_PROVISIONS() != null) {
					cell1.setCellValue(record.getR16_STAGE1_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				// row11
				// Column E
				cell1 = row.getCell(2);
				if (record.getR16_QUALIFY_STAGE2_PROVISIONS() != null) {
					cell1.setCellValue(record.getR16_QUALIFY_STAGE2_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
			
			//-------------------13
			
			row = sheet.getRow(12);

				cell1 = row.getCell(1);
				if (record.getR17_STAGE1_PROVISIONS() != null) {
					cell1.setCellValue(record.getR17_STAGE1_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				// row11
				// Column E
				cell1 = row.getCell(2);
				if (record.getR17_QUALIFY_STAGE2_PROVISIONS() != null) {
					cell1.setCellValue(record.getR17_QUALIFY_STAGE2_PROVISIONS().doubleValue());

				} else {
					cell1.setCellValue("");
					cell1.setCellStyle(textStyle);
				}
				
				
					//-------------------14
					
					
					
row = sheet.getRow(13);

cell1 = row.getCell(1);
if (record.getR18_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR18_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR18_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR18_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------15
row = sheet.getRow(14);

cell1 = row.getCell(1);
if (record.getR19_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR19_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR19_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR19_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------16
row = sheet.getRow(15);

cell1 = row.getCell(1);
if (record.getR20_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR20_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR20_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR20_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------17
row = sheet.getRow(16);

cell1 = row.getCell(1);
if (record.getR21_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR21_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR21_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR21_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------18
row = sheet.getRow(17);

cell1 = row.getCell(1);
if (record.getR22_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR22_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR22_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR22_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------19
row = sheet.getRow(18);

cell1 = row.getCell(1);
if (record.getR23_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR23_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR23_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR23_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------20
row = sheet.getRow(19);

cell1 = row.getCell(1);
if (record.getR24_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR24_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR24_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR24_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------21
row = sheet.getRow(20);

cell1 = row.getCell(1);
if (record.getR25_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR25_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR25_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR25_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------22
row = sheet.getRow(21);

cell1 = row.getCell(1);
if (record.getR26_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR26_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR26_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR26_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------23
row = sheet.getRow(22);

cell1 = row.getCell(1);
if (record.getR27_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR27_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR27_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR27_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------24
row = sheet.getRow(23);

cell1 = row.getCell(1);
if (record.getR28_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR28_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR28_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR28_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}



//-------------------26
row = sheet.getRow(25);

cell1 = row.getCell(1);
if (record.getR30_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR30_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR30_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR30_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------27
row = sheet.getRow(26);

cell1 = row.getCell(1);
if (record.getR31_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR31_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR31_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR31_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------28
row = sheet.getRow(27);

cell1 = row.getCell(1);
if (record.getR32_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR32_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR32_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR32_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------29
row = sheet.getRow(28);

cell1 = row.getCell(1);
if (record.getR33_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR33_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR33_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR33_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------30
row = sheet.getRow(29);

cell1 = row.getCell(1);
if (record.getR34_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR34_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR34_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR34_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------31
row = sheet.getRow(30);

cell1 = row.getCell(1);
if (record.getR35_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR35_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR35_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR35_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------32
row = sheet.getRow(31);

cell1 = row.getCell(1);
if (record.getR36_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR36_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR36_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR36_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------33
row = sheet.getRow(32);

cell1 = row.getCell(1);
if (record.getR37_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR37_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR37_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR37_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}


//-------------------35



row = sheet.getRow(34);

cell1 = row.getCell(1);
if (record.getR39_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR39_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR39_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR39_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------36
row = sheet.getRow(35);

cell1 = row.getCell(1);
if (record.getR40_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR40_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR40_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR40_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
	

	//-------------------38
row = sheet.getRow(37);

cell1 = row.getCell(1);
if (record.getR42_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR42_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR42_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR42_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------39
row = sheet.getRow(38);

cell1 = row.getCell(1);
if (record.getR43_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR43_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR43_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR43_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}
	
//-------------------41	
row = sheet.getRow(40);

cell1 = row.getCell(1);
if (record.getR45_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR45_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR45_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR45_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------42
row = sheet.getRow(41);

cell1 = row.getCell(1);
if (record.getR46_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR46_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR46_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR46_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}	


//-------------------43
row = sheet.getRow(42);

cell1 = row.getCell(1);
if (record.getR47_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR47_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR47_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR47_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------44
row = sheet.getRow(43);

cell1 = row.getCell(1);
if (record.getR48_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR48_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR48_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR48_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------46
row = sheet.getRow(45);

cell1 = row.getCell(1);
if (record.getR50_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR50_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR50_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR50_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------47
row = sheet.getRow(46);

cell1 = row.getCell(1);
if (record.getR51_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR51_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR51_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR51_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------48
row = sheet.getRow(47);

cell1 = row.getCell(1);
if (record.getR52_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR52_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR52_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR52_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}


//-------------------50
row = sheet.getRow(49);

cell1 = row.getCell(1);
if (record.getR54_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR54_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR54_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR54_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------51
row = sheet.getRow(50);

cell1 = row.getCell(1);
if (record.getR55_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR55_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR55_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR55_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------52
row = sheet.getRow(51);

cell1 = row.getCell(1);
if (record.getR56_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR56_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR56_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR56_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------54
row = sheet.getRow(53);

cell1 = row.getCell(1);
if (record.getR58_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR58_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR58_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR58_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------55
row = sheet.getRow(54);

cell1 = row.getCell(1);
if (record.getR59_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR59_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR59_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR59_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------56
row = sheet.getRow(55);

cell1 = row.getCell(1);
if (record.getR60_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR60_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR60_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR60_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------57
row = sheet.getRow(56);

cell1 = row.getCell(1);
if (record.getR61_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR61_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR61_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR61_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------58
row = sheet.getRow(57);

cell1 = row.getCell(1);
if (record.getR62_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR62_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR62_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR62_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

//-------------------59
row = sheet.getRow(58);

cell1 = row.getCell(1);
if (record.getR63_STAGE1_PROVISIONS() != null) {
cell1.setCellValue(record.getR63_STAGE1_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);
}

cell1 = row.getCell(2);
if (record.getR63_QUALIFY_STAGE2_PROVISIONS() != null) {
cell1.setCellValue(record.getR63_QUALIFY_STAGE2_PROVISIONS().doubleValue());
} else {
cell1.setCellValue("");
cell1.setCellStyle(textStyle);

				
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