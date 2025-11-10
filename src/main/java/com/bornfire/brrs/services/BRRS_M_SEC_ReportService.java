package com.bornfire.brrs.services;



import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
// import java.util.ArrayList;  // SHOW WARNING HERE
import java.util.Date;
import java.util.List;

// import javax.servlet.http.HttpServletRequest; // SHOW WARNING HERE

import org.apache.poi.ss.usermodel.*;
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

import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Entity3;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Entity4;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_M_SEC_Archival_Summary_Repo4;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Entity1;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Entity2;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Entity3;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Entity4;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Repo3;
import com.bornfire.brrs.entities.BRRS_M_SEC_Summary_Repo4;



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

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ================== VIEW ==================

	public ModelAndView getM_SECView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, String version) {
		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") & version != null) {
			List<BRRS_M_SEC_Archival_Summary_Entity1> T1Master = new ArrayList<BRRS_M_SEC_Archival_Summary_Entity1>();
			List<BRRS_M_SEC_Archival_Summary_Entity2> T2Master = new ArrayList<BRRS_M_SEC_Archival_Summary_Entity2>();
			List<BRRS_M_SEC_Archival_Summary_Entity3> T3Master = new ArrayList<BRRS_M_SEC_Archival_Summary_Entity3>();
			List<BRRS_M_SEC_Archival_Summary_Entity4> T4Master = new ArrayList<BRRS_M_SEC_Archival_Summary_Entity4>();
			try {
				Date d1 = dateformat.parse(todate);

				// T1Master = hs.createQuery("from BRF1_REPORT_ENTITY a where a.report_date = ?1
				// ", BRF1_REPORT_ENTITY.class)
				// .setParameter(1, df.parse(todate)).getResultList();
				T1Master = archivalSummaryRepo1.getdatabydateListarchival(dateformat.parse(todate), version);
				T2Master = archivalSummaryRepo2.getdatabydateListarchival(dateformat.parse(todate), version);
				T3Master = archivalSummaryRepo3.getdatabydateListarchival(dateformat.parse(todate), version);
				T4Master = archivalSummaryRepo4.getdatabydateListarchival(dateformat.parse(todate), version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary1", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
			mv.addObject("reportsummary4", T4Master);
		} else {
			List<BRRS_M_SEC_Summary_Entity1> T1Master = new ArrayList<BRRS_M_SEC_Summary_Entity1>();
			List<BRRS_M_SEC_Summary_Entity2> T2Master = new ArrayList<BRRS_M_SEC_Summary_Entity2>();
			List<BRRS_M_SEC_Summary_Entity3> T3Master = new ArrayList<BRRS_M_SEC_Summary_Entity3>();
			List<BRRS_M_SEC_Summary_Entity4> T4Master = new ArrayList<BRRS_M_SEC_Summary_Entity4>();

			try {
				Date d1 = dateformat.parse(todate);

				T1Master = secSummaryRepo1.getdatabydateList(dateformat.parse(todate));
				T2Master = secSummaryRepo2.getdatabydateList(dateformat.parse(todate));
				T3Master = secSummaryRepo3.getdatabydateList(dateformat.parse(todate));
				T4Master = secSummaryRepo4.getdatabydateList(dateformat.parse(todate));

				System.out.println("Size of t1master is :" + T1Master.size());

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary1", T1Master);
			mv.addObject("reportsummary2", T2Master);
			mv.addObject("reportsummary3", T3Master);
			mv.addObject("reportsummary4", T4Master);

		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_SEC");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public void updateReport(BRRS_M_SEC_Summary_Entity1 updatedEntity) {
		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		BRRS_M_SEC_Summary_Entity1 existing = secSummaryRepo1.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		try {
			// 1️⃣ Loop from R11 to R15 and copy fields
			for (int i = 11; i <= 18; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "TCA" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SEC_Summary_Entity1.class.getMethod(getterName);
						Method setter = BRRS_M_SEC_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R19 totals
			String[] totalFields = { "TCA" };

			for (String field : totalFields) {
				String getterName = "getR19_" + field;
				String setterName = "setR19_" + field;

				try {
					Method getter = BRRS_M_SEC_Summary_Entity1.class.getMethod(getterName);
					Method setter = BRRS_M_SEC_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

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
		secSummaryRepo1.save(existing);

	}

	public void updateArchivalReport(BRRS_M_SEC_Archival_Summary_Entity1 updatedEntity) {
		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		BRRS_M_SEC_Archival_Summary_Entity1 existing = archivalSummaryRepo1.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		try {
			// 1️⃣ Loop from R11 to R18 and copy fields
			for (int i = 11; i <= 18; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "TCA" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SEC_Archival_Summary_Entity1.class.getMethod(getterName);
						Method setter = BRRS_M_SEC_Archival_Summary_Entity1.class.getMethod(setterName,
								getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R19 totals
			String[] totalFields = { "TCA" };

			for (String field : totalFields) {
				String getterName = "getR19_" + field;
				String setterName = "setR19_" + field;

				try {
					Method getter = BRRS_M_SEC_Archival_Summary_Entity1.class.getMethod(getterName);
					Method setter = BRRS_M_SEC_Archival_Summary_Entity1.class.getMethod(setterName,
							getter.getReturnType());

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
		archivalSummaryRepo1.save(existing);

	}

	public void updateReport2(BRRS_M_SEC_Summary_Entity2 updatedEntity) {
		System.out.println("Came to services 2");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		BRRS_M_SEC_Summary_Entity2 existing = secSummaryRepo2.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		try {
			// 1️⃣ Loop from R11 to R15 and copy fields
			for (int i = 11; i <= 15; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "TCA2" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SEC_Summary_Entity2.class.getMethod(getterName);
						Method setter = BRRS_M_SEC_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R16 totals
			String[] totalFields = { "TCA2" };

			for (String field : totalFields) {
				String getterName = "getR16_" + field;
				String setterName = "setR16_" + field;

				try {
					Method getter = BRRS_M_SEC_Summary_Entity2.class.getMethod(getterName);
					Method setter = BRRS_M_SEC_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

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
		secSummaryRepo2.save(existing);

	}

	public void updateArchivalReport2(BRRS_M_SEC_Archival_Summary_Entity2 updatedEntity) {
		System.out.println("Came to services 2");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		BRRS_M_SEC_Archival_Summary_Entity2 existing = archivalSummaryRepo2.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		try {
			// 1️⃣ Loop from R11 to R18 and copy fields
			for (int i = 11; i <= 15; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "TCA2" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SEC_Archival_Summary_Entity2.class.getMethod(getterName);
						Method setter = BRRS_M_SEC_Archival_Summary_Entity2.class.getMethod(setterName,
								getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R16 totals
			String[] totalFields = { "TCA2" };

			for (String field : totalFields) {
				String getterName = "getR16_" + field;
				String setterName = "setR16_" + field;

				try {
					Method getter = BRRS_M_SEC_Archival_Summary_Entity2.class.getMethod(getterName);
					Method setter = BRRS_M_SEC_Archival_Summary_Entity2.class.getMethod(setterName,
							getter.getReturnType());

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
		System.out.println("Testing 2");
		// 3️⃣ Save updated entity
		archivalSummaryRepo2.save(existing);

	}

	public void updateReport3(BRRS_M_SEC_Summary_Entity3 updatedEntity) {
		System.out.println("Came to services 3");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		BRRS_M_SEC_Summary_Entity3 existing = secSummaryRepo3.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		try {
			// 1️⃣ Loop from R11 to R15 and copy fields
			for (int i = 26; i <= 30; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "0_1Y_FT", "0_1Y_HTM", "0_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
						"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SEC_Summary_Entity3.class.getMethod(getterName);
						Method setter = BRRS_M_SEC_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R31 totals
			String[] totalFields = { "O_1Y_FT", "O_1Y_HTM", "O_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
					"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };

			for (String field : totalFields) {
				String getterName = "getR31_" + field;
				String setterName = "setR31_" + field;

				try {
					Method getter = BRRS_M_SEC_Summary_Entity3.class.getMethod(getterName);
					Method setter = BRRS_M_SEC_Summary_Entity3.class.getMethod(setterName, getter.getReturnType());

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
		secSummaryRepo3.save(existing);

	}

	public void updateArchivalReport3(BRRS_M_SEC_Archival_Summary_Entity3 updatedEntity) {
		System.out.println("Came to services 3");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		BRRS_M_SEC_Archival_Summary_Entity3 existing = archivalSummaryRepo3.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		try {
			// 1️⃣ Loop from R11 to R18 and copy fields
			for (int i = 26; i <= 30; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "O_1Y_FT", "O_1Y_HTM", "O_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
						"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SEC_Archival_Summary_Entity3.class.getMethod(getterName);
						Method setter = BRRS_M_SEC_Archival_Summary_Entity3.class.getMethod(setterName,
								getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R31 totals
			String[] totalFields = { "O_1Y_FT", "O_1Y_HTM", "O_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
					"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };

			for (String field : totalFields) {
				String getterName = "getR31_" + field;
				String setterName = "setR31_" + field;

				try {
					Method getter = BRRS_M_SEC_Archival_Summary_Entity3.class.getMethod(getterName);
					Method setter = BRRS_M_SEC_Archival_Summary_Entity3.class.getMethod(setterName,
							getter.getReturnType());

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
		System.out.println("Testing 3");
		// 3️⃣ Save updated entity
		archivalSummaryRepo3.save(existing);

	}

	public void updateReport4(BRRS_M_SEC_Summary_Entity4 updatedEntity) {
		System.out.println("Came to services 4");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		BRRS_M_SEC_Summary_Entity4 existing = secSummaryRepo4.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		try {
			// 1️⃣ Loop from R11 to R15 and copy fields
			for (int i = 36; i <= 42; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "0_1Y_FT", "0_1Y_HTM", "0_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
						"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SEC_Summary_Entity4.class.getMethod(getterName);
						Method setter = BRRS_M_SEC_Summary_Entity4.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R43 totals
			String[] totalFields = { "0_1Y_FT", "0_1Y_HTM", "0_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
					"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };

			for (String field : totalFields) {
				String getterName = "getR43_" + field;
				String setterName = "setR43_" + field;

				try {
					Method getter = BRRS_M_SEC_Summary_Entity4.class.getMethod(getterName);
					Method setter = BRRS_M_SEC_Summary_Entity4.class.getMethod(setterName, getter.getReturnType());

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
		System.out.println("Testing 4");
		// 3️⃣ Save updated entity
		secSummaryRepo4.save(existing);

	}

	public void updateArchivalReport4(BRRS_M_SEC_Archival_Summary_Entity4 updatedEntity) {
		System.out.println("Came to services 4");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		BRRS_M_SEC_Archival_Summary_Entity4 existing = archivalSummaryRepo4.findById(updatedEntity.getREPORT_DATE())
				.orElseThrow(() -> new RuntimeException(
						"Record not found for REPORT_DATE: " + updatedEntity.getREPORT_DATE()));

		try {
			// 1️⃣ Loop from R11 to R18 and copy fields
			for (int i = 36; i <= 42; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "O_1Y_FT", "O_1Y_HTM", "O_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
						"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = BRRS_M_SEC_Archival_Summary_Entity4.class.getMethod(getterName);
						Method setter = BRRS_M_SEC_Archival_Summary_Entity4.class.getMethod(setterName,
								getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R43 totals
			String[] totalFields = { "O_1Y_FT", "O_1Y_HTM", "O_1Y_TOTAL", "1_5Y_FT", "1_5Y_HTM", "1_5Y_TOTAL",
					"O5Y_FT", "O5Y_HTM", "O5Y_TOTAL", "T_FT", "T_HTM", "T_TOTAL" };

			for (String field : totalFields) {
				String getterName = "getR43_" + field;
				String setterName = "setR43_" + field;

				try {
					Method getter = BRRS_M_SEC_Archival_Summary_Entity4.class.getMethod(getterName);
					Method setter = BRRS_M_SEC_Archival_Summary_Entity4.class.getMethod(setterName,
							getter.getReturnType());

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
		System.out.println("Testing 4");
		// 3️⃣ Save updated entity
		archivalSummaryRepo4.save(existing);

	}

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

	// @Transactional(readOnly = true)
	public byte[] getM_SECExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process.", type);

		// ARCHIVAL branch
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.trim().isEmpty()) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version, type);
			return getExcelBRRS_M_SEC_ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		Date parsedToDate = dateformat.parse(todate);

		// Fetch each summary (each table uses its own entity/repo)
		List<BRRS_M_SEC_Summary_Entity1> list1 = secSummaryRepo1.getdatabydateList(parsedToDate);
		List<BRRS_M_SEC_Summary_Entity2> list2 = secSummaryRepo2.getdatabydateList(parsedToDate);
		List<BRRS_M_SEC_Summary_Entity3> list3 = secSummaryRepo3.getdatabydateList(parsedToDate);
		List<BRRS_M_SEC_Summary_Entity4> list4 = secSummaryRepo4.getdatabydateList(parsedToDate);

		if ((list1 == null || list1.isEmpty()) &&
				(list2 == null || list2.isEmpty()) &&
				(list3 == null || list3.isEmpty()) &&
				(list4 == null || list4.isEmpty())) {
			logger.warn("Service: No data found for all summaries. Returning empty result.");
			return new byte[0];
		}

		// Template lookup (filesystem then classpath)
		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = (templateDir != null && !templateDir.trim().isEmpty())
				? Paths.get(templateDir, filename)
				: null;

		InputStream templateInput = null;
		if (templatePath != null && Files.exists(templatePath) && Files.isReadable(templatePath)) {
			templateInput = Files.newInputStream(templatePath);
		} else {
			String cpPath = "/templates/" + filename;
			templateInput = getClass().getResourceAsStream(cpPath);
			if (templateInput == null) {
				String msg = "Template file not found on filesystem or classpath: " + filename;
				logger.error(msg);
				throw new FileNotFoundException(msg);
			}
		}

		try (InputStream in = templateInput;
				Workbook workbook = WorkbookFactory.create(in);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);
			CreationHelper createHelper = workbook.getCreationHelper();

			// Styles
			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));

			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			// int startRow = 10;

			// if (!list1.isEmpty()) {
			// for (int i = 0; i < list1.size(); i++) {
			// BRRS_M_SEC_Summary_Entity1 record = list1.get(i);
			// System.out.println("rownumber=" + startRow + i);
			// Row row = sheet.getRow(startRow + i);
			// if (row == null) {
			// row = sheet.createRow(startRow + i);
			// }
			// // row11
			// // Column B2 - Original Amount
			// Cell cellB = row.createCell(1);
			// if (record.getR11_TCA() != null) {
			// cellB.setCellValue(record.getR11_TCA().doubleValue());
			// cellB.setCellStyle(numberStyle);
			// } else {
			// cellB.setCellValue("");
			// cellB.setCellStyle(textStyle);
			// }

			// // row12
			// row = sheet.getRow(11);
			// cellB = row.createCell(1);
			// if (record.getR12_TCA() != null) {
			// cellB.setCellValue(record.getR12_TCA().doubleValue());
			// cellB.setCellStyle(numberStyle);
			// } else {
			// cellB.setCellValue("");
			// cellB.setCellStyle(textStyle);
			// }

			// // row13
			// row = sheet.getRow(12);
			// cellB = row.createCell(1);
			// if (record.getR13_TCA() != null) {
			// cellB.setCellValue(record.getR13_TCA().doubleValue());
			// cellB.setCellStyle(numberStyle);
			// } else {
			// cellB.setCellValue("");
			// cellB.setCellStyle(textStyle);
			// }

			// // row14
			// row = sheet.getRow(13);
			// cellB = row.createCell(1);
			// if (record.getR14_TCA() != null) {
			// cellB.setCellValue(record.getR14_TCA().doubleValue());
			// cellB.setCellStyle(numberStyle);
			// } else {
			// cellB.setCellValue("");
			// cellB.setCellStyle(textStyle);
			// }
			// // row15
			// row = sheet.getRow(14);
			// cellB = row.createCell(1);
			// if (record.getR15_TCA() != null) {
			// cellB.setCellValue(record.getR15_TCA().doubleValue());
			// cellB.setCellStyle(numberStyle);
			// } else {
			// cellB.setCellValue("");
			// cellB.setCellStyle(textStyle);
			// }

			// // row16
			// row = sheet.getRow(15);
			// cellB = row.createCell(1);
			// if (record.getR16_TCA() != null) {
			// cellB.setCellValue(record.getR16_TCA().doubleValue());
			// cellB.setCellStyle(numberStyle);
			// } else {
			// cellB.setCellValue("");
			// cellB.setCellStyle(textStyle);
			// }

			// // row17
			// row = sheet.getRow(16);
			// cellB = row.createCell(1);
			// if (record.getR17_TCA() != null) {
			// cellB.setCellValue(record.getR17_TCA().doubleValue());
			// cellB.setCellStyle(numberStyle);
			// } else {
			// cellB.setCellValue("");
			// cellB.setCellStyle(textStyle);
			// }
			// // row18
			// row = sheet.getRow(17);
			// cellB = row.createCell(1);
			// if (record.getR18_TCA() != null) {
			// cellB.setCellValue(record.getR18_TCA().doubleValue());
			// cellB.setCellStyle(numberStyle);
			// } else {
			// cellB.setCellValue("");
			// cellB.setCellStyle(textStyle);
			// }
			// }
			// }

			int startRow = 10;

			if (!list1.isEmpty()) {
				for (int i = 0; i < list1.size(); i++) {
					BRRS_M_SEC_Summary_Entity1 record = list1.get(i);
					System.out.println("rownumber=" + (startRow + i));
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

			if (!list2.isEmpty()) {
				for (int i = 0; i < list2.size(); i++) {
					BRRS_M_SEC_Summary_Entity2 record = list2.get(i);
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

			if (!list3.isEmpty()) {
				for (int i = 0; i < list3.size(); i++) {
					BRRS_M_SEC_Summary_Entity3 record = list3.get(i);
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

			if (!list4.isEmpty()) {
				for (int i = 0; i < list4.size(); i++) {
					BRRS_M_SEC_Summary_Entity4 record = list4.get(i);
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

	public byte[] getExcelBRRS_M_SEC_ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting ARCHIVAL Excel generation for version {}", version);

		if (version == null || version.trim().isEmpty()) {
			throw new IllegalArgumentException("ARCHIVAL version cannot be null or empty");
		}

		Date parsedToDate = dateformat.parse(todate);

		// Fetch archival data for all four summaries
		List<BRRS_M_SEC_Archival_Summary_Entity1> list1 = archivalSummaryRepo1.getdatabydateListarchival(parsedToDate,
				version);
		List<BRRS_M_SEC_Archival_Summary_Entity2> list2 = archivalSummaryRepo2.getdatabydateListarchival(parsedToDate,
				version);
		List<BRRS_M_SEC_Archival_Summary_Entity3> list3 = archivalSummaryRepo3.getdatabydateListarchival(parsedToDate,
				version);
		List<BRRS_M_SEC_Archival_Summary_Entity4> list4 = archivalSummaryRepo4.getdatabydateListarchival(parsedToDate,
				version);

		if ((list1 == null || list1.isEmpty()) &&
				(list2 == null || list2.isEmpty()) &&
				(list3 == null || list3.isEmpty()) &&
				(list4 == null || list4.isEmpty())) {
			logger.warn("ARCHIVAL: No data found for version {}. Returning empty result.", version);
			return new byte[0];
		}

		// Template lookup (filesystem then classpath)
		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = (templateDir != null && !templateDir.trim().isEmpty())
				? Paths.get(templateDir, filename)
				: null;

		InputStream templateInput;
		if (templatePath != null && Files.exists(templatePath) && Files.isReadable(templatePath)) {
			templateInput = Files.newInputStream(templatePath);
		} else {
			String cpPath = "/templates/" + filename;
			templateInput = getClass().getResourceAsStream(cpPath);
			if (templateInput == null) {
				String msg = "ARCHIVAL template file not found: " + filename;
				logger.error(msg);
				throw new FileNotFoundException(msg);
			}
		}

		try (InputStream in = templateInput;
				Workbook workbook = WorkbookFactory.create(in);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);
			CreationHelper createHelper = workbook.getCreationHelper();

			// Styles
			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));

			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			int startRow = 10;

			if (!list1.isEmpty()) {
				for (int i = 0; i < list1.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity1 record = list1.get(i);
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

			if (!list2.isEmpty()) {
				for (int i = 0; i < list2.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity2 record = list2.get(i);
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

			if (!list3.isEmpty()) {
				for (int i = 0; i < list3.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity3 record = list3.get(i);
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

			if (!list4.isEmpty()) {
				for (int i = 0; i < list4.size(); i++) {
					BRRS_M_SEC_Archival_Summary_Entity4 record = list4.get(i);
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

	public List<Object> getM_SECArchival() {
		List<Object> M_SECArchivallist = new ArrayList<>();
		List<Object> M_SECArchivallist2 = new ArrayList<>();
		List<Object> M_SECArchivallist3 = new ArrayList<>();
		List<Object> M_SECArchivallist4 = new ArrayList<>();
		try {
			M_SECArchivallist = archivalSummaryRepo1.getM_SECarchival();
			M_SECArchivallist2 = archivalSummaryRepo2.getM_SECarchival();
			M_SECArchivallist3 = archivalSummaryRepo3.getM_SECarchival();
			M_SECArchivallist4 = archivalSummaryRepo4.getM_SECarchival();

			System.out.println("countser" + M_SECArchivallist.size());
			System.out.println("countser" + M_SECArchivallist2.size());
			System.out.println("countser" + M_SECArchivallist3.size());
			System.out.println("countser" + M_SECArchivallist4.size());
		} catch (Exception e) {
			// Log the exception
			System.err.println("Error fetching M_SEC Archival data: " + e.getMessage());
			e.printStackTrace();

			// Optionally, you can rethrow it or return empty list
			// throw new RuntimeException("Failed to fetch data", e);
		}
		return M_SECArchivallist;
	}
}
