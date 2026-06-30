package com.bornfire.brrs.services;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.dto.ReportLineItemDTO;

import com.bornfire.brrs.entities.UserProfileRep;

@Component
@Service
public class BRRS_M_SFINP2_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SFINP2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

// ------------------------------
// Summary view 
// ------------------------------
	public ModelAndView getM_SFINP2View(String reportId, String fromdate, String todate, String currency,
			String dtltype, // kept but not used
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");

		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		try {

			// Parse date only once
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW DEBUG =======");
			System.out.println("TYPE    : " + type);
			System.out.println("DATE    : " + d1);
			System.out.println("VERSION : " + version);
			System.out.println("==========================");

			// ===========================================================
			// SUMMARY ONLY
			// ===========================================================

			/* ---------- ARCHIVAL SUMMARY ---------- */
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

				String sql = "SELECT * FROM BRRS_M_SFINP2_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
				List<M_SFINP2_Archival_Summary_Entity> summaryList = jdbcTemplate.query(sql,
						new Object[] { d1, version },
						new BeanPropertyRowMapper<>(M_SFINP2_Archival_Summary_Entity.class));

				System.out.println("Archival Summary Size : " + summaryList.size());

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", summaryList);
			}

			/* ---------- RESUB SUMMARY ---------- */
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				String sql = "SELECT * FROM BRRS_M_SFINP2_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
				List<M_SFINP2_RESUB_Summary_Entity> summaryList = jdbcTemplate.query(sql, new Object[] { d1, version },
						new BeanPropertyRowMapper<>(M_SFINP2_RESUB_Summary_Entity.class));

				System.out.println("Resub Summary Size : " + summaryList.size());

				mv.addObject("displaymode", "resub");
				mv.addObject("reportsummary", summaryList);
			}

			/* ---------- NORMAL SUMMARY ---------- */
			else {

				String sql = "SELECT * FROM BRRS_M_SFINP2_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				List<M_SFINP2_Summary_Entity> summaryList = jdbcTemplate.query(sql, new Object[] { d1 },
						new BeanPropertyRowMapper<>(M_SFINP2_Summary_Entity.class));

				System.out.println("Normal Summary Size : " + summaryList.size());

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", summaryList);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SFINP2");
		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

// ------------------------------
// Current detail 
// ------------------------------
	public ModelAndView getM_SFINP2currentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version, HttpServletRequest req1,
			Model md) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
		String userid = (String) req1.getSession().getAttribute("USERID");

		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String reportLable = null;
			String reportAddlCriteria_1 = null;

			// Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLable = parts[0];
					reportAddlCriteria_1 = parts[1];
				}
			}

			/*
			 * ========================================================= 
			 * ARCHIVAL DETAIL
			 * =========================================================
			 */
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

				List<M_SFINP2_Archival_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					String sql = "SELECT * FROM BRRS_M_SFINP2_ARCHIVALTABLE_DETAIL WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
					T1Dt1 = jdbcTemplate.query(sql,
							new Object[] { reportLable, reportAddlCriteria_1, parsedDate, version },
							new BeanPropertyRowMapper<>(M_SFINP2_Archival_Detail_Entity.class));
				} else {
					String sql = "SELECT * FROM BRRS_M_SFINP2_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
					T1Dt1 = jdbcTemplate.query(sql, new Object[] { parsedDate, version },
							new BeanPropertyRowMapper<>(M_SFINP2_Archival_Detail_Entity.class));
				}

				mv.addObject("reportdetails", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

			/*
			 * ========================================================= 
			 * RESUB DETAIL
			 * =========================================================
			 */
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_SFINP2_RESUB_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					String sql = "SELECT * FROM BRRS_M_SFINP2_RESUB_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
					T1Dt1 = jdbcTemplate.query(sql,
							new Object[] { reportLable, reportAddlCriteria_1, parsedDate, version },
							new BeanPropertyRowMapper<>(M_SFINP2_RESUB_Detail_Entity.class));
				} else {
					String sql = "SELECT * FROM BRRS_M_SFINP2_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
					T1Dt1 = jdbcTemplate.query(sql, new Object[] { parsedDate, version },
							new BeanPropertyRowMapper<>(M_SFINP2_RESUB_Detail_Entity.class));
				}

				mv.addObject("reportdetails", T1Dt1);
				System.out.println("RESUB COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

			/*
			 * ========================================================= 
			 * CURRENT DETAIL
			 * =========================================================
			 */
			else {

				List<M_SFINP2_Detail_Entity> T1Dt1;

				if (reportLable != null && reportAddlCriteria_1 != null) {
					String sql = "SELECT * FROM BRRS_M_SFINP2_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";
					T1Dt1 = jdbcTemplate.query(sql, new Object[] { reportLable, reportAddlCriteria_1, parsedDate },
							new BeanPropertyRowMapper<>(M_SFINP2_Detail_Entity.class));
				} else {
					String sql = "SELECT * FROM BRRS_M_SFINP2_DETAILTABLE WHERE REPORT_DATE = ?";
					T1Dt1 = jdbcTemplate.query(sql, new Object[] { parsedDate },
							new BeanPropertyRowMapper<>(M_SFINP2_Detail_Entity.class));

					String sqlCount = "SELECT COUNT(*) FROM BRRS_M_SFINP2_DETAILTABLE WHERE REPORT_DATE = ?";
					totalPages = jdbcTemplate.queryForObject(sqlCount, new Object[] { parsedDate }, Integer.class);

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

		// Common attributes
		mv.setViewName("BRRS/M_SFINP2");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
//	// ------------------------------
	// Updates and saves summary fields, triggers audit trail, and runs dynamic
	// database procedures after commit
	// ------------------------------
	// public void updateReport(M_SFINP2_Summary_Entity updatedEntity) {
//
//	    System.out.println("Came to services");
//	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());
//
//	    List<M_SFINP2_Summary_Entity> list =
//	            M_SFINP2_Summary_Repo.getdatabydateList(updatedEntity.getREPORT_DATE());
//
//	    M_SFINP2_Summary_Entity existing;
//
//	    if (list.isEmpty()) {
//	        // 🔹 INSERT
//	        existing = new M_SFINP2_Summary_Entity();
//	        existing.setREPORT_DATE(updatedEntity.getREPORT_DATE());
//	        System.out.println("Creating new record");
//	    } else {
//	        // 🔹 UPDATE
//	        existing = list.get(0);
//	        System.out.println("Updating existing record");
//	    }
//
//	    int[] allowedIndexes = {
//	        34, 35, 39, 40, 43, 47, 48,
//	        51, 52, 53, 54, 55, 56, 57, 58,
//	        61, 62
//	       
//	    };
//
//	    try {
//	        for (int i : allowedIndexes) {
//
//	            String field = "MONTH_END";
//
//	            String getterName = "getR" + i + "_" + field;
//	            String setterName = "setR" + i + "_" + field;
//
//	            try {
//	                Method getter =
//	                        M_SFINP2_Summary_Entity.class.getMethod(getterName);
//
//	                Method setter =
//	                        M_SFINP2_Summary_Entity.class.getMethod(
//	                                setterName,
//	                                getter.getReturnType()
//	                        );
//
//	                Object newValue = getter.invoke(updatedEntity);
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//	                // skip missing R fields
//	            }
//	        }
//
//	    } catch (Exception e) {
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//
//	    // ✅ ALWAYS SAVE
//	    M_SFINP2_Summary_Repo.save(existing);
//	}

	// ------------------------------
	// Updates and saves summary fields, triggers audit trail, and runs dynamic
	// database procedures after commit
	// ------------------------------
	public void updateReport(M_SFINP2_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());

		String selectSql = "SELECT * FROM BRRS_M_SFINP2_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		List<M_SFINP2_Summary_Entity> list = jdbcTemplate.query(selectSql,
				new Object[] { updatedEntity.getREPORT_DATE() },
				new BeanPropertyRowMapper<>(M_SFINP2_Summary_Entity.class));

		M_SFINP2_Summary_Entity existing;

		if (list.isEmpty()) {
			// INSERT
			existing = new M_SFINP2_Summary_Entity();
			existing.setREPORT_DATE(updatedEntity.getREPORT_DATE());
			System.out.println("Creating new record");
		} else {
			// UPDATE
			existing = list.get(0);
			System.out.println("Updating existing record");
		}

		// Audit old copy
		M_SFINP2_Summary_Entity oldcopy = new M_SFINP2_Summary_Entity();
		BeanUtils.copyProperties(existing, oldcopy);

		int[] allowedIndexes = { 34, 35, 43, 47, 48, 51, 52, 53, 54, 55, 56, 57, 58, 61, 62 };

		try {
			for (int i : allowedIndexes) {
				String field = "MONTH_END";
				String getterName = "getR" + i + "_" + field;
				String setterName = "setR" + i + "_" + field;

				try {
					Method getter = M_SFINP2_Summary_Entity.class.getMethod(getterName);
					Method setter = M_SFINP2_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
					Object newValue = getter.invoke(updatedEntity);
					setter.invoke(existing, newValue);
				} catch (NoSuchMethodException e) {
					// Skip missing fields
					continue;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Check changes before save
		String changes = auditService.getChanges(oldcopy, existing);

		// Save entity using dynamic jdbcTemplate save helper
		saveSummaryEntity(existing);

		// Audit only if changes found
		if (!changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existing, updatedEntity.getREPORT_DATE().toString(),
					"M SFINP2 Summary Screen", "BRRS_M_SFINP2_SUMMARY");
		}

		// CALL PROCEDURE AFTER COMMIT
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			// ------------------------------
			// Callback function executed synchronously after database transaction commit
			// succeeds
			// ------------------------------
			public void afterCommit() {
				try {
					String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(updatedEntity.getREPORT_DATE());
					logger.info("Transaction committed — calling BRRS_M_SFINP2_SUMMARY_PROCEDURE({})", formattedDate);
					jdbcTemplate.update("BEGIN BRRS_M_SFINP2_SUMMARY_PROCEDURE(?); END;", formattedDate);
					logger.info("Procedure executed successfully after commit.");
				} catch (Exception e) {
					logger.error("Procedure execution failed", e);
					throw new RuntimeException("Procedure execution failed", e);
				}
			}
		});
	}

//	// ------------------------------
	// Updates and saves summary fields, triggers audit trail, and runs dynamic
	// database procedures after commit
	// ------------------------------
	// public void updateReport(M_SFINP2_Summary_Entity updatedEntity) {

//
//	    System.out.println("Came to services");
//	    System.out.println("Report Date: " + updatedEntity.getREPORT_DATE());
//
//	    List<M_SFINP2_Summary_Entity> list =
//	            M_SFINP2_Summary_Repo.getdatabydateList(updatedEntity.getREPORT_DATE());
//
//	    M_SFINP2_Summary_Entity existing;
//
//	    if (list.isEmpty()) {
//
//	        // 🔹 INSERT
//	        existing = new M_SFINP2_Summary_Entity();
//	        existing.setREPORT_DATE(updatedEntity.getREPORT_DATE());
//
//	        System.out.println("Creating new record");
//
//	    } else {
//
//	        // 🔹 UPDATE
//	        existing = list.get(0);
//
//	        System.out.println("Updating existing record");
//	    }
//
//	    
//	    
//	    
//	    int[] allowedIndexes = {
//	        34, 35, 39, 40, 43, 47, 48,
//	        51, 52, 53, 54, 55, 56, 57, 58,
//	        61, 62
//	    };
//
//	    try {
//
//	        for (int i : allowedIndexes) {
//
//	            String field = "MONTH_END";
//
//	            String getterName = "getR" + i + "_" + field;
//	            String setterName = "setR" + i + "_" + field;
//
//	            try {
//
//	                Method getter =
//	                        M_SFINP2_Summary_Entity.class.getMethod(getterName);
//
//	                Method setter =
//	                        M_SFINP2_Summary_Entity.class.getMethod(
//	                                setterName,
//	                                getter.getReturnType()
//	                        );
//
//	                Object newValue = getter.invoke(updatedEntity);
//
//	                setter.invoke(existing, newValue);
//
//	            } catch (NoSuchMethodException e) {
//
//	                // skip missing fields
//	            }
//	        }
//
//	    } catch (Exception e) {
//
//	        throw new RuntimeException("Error while updating report fields", e);
//	    }
//
//	    // ✅ SAVE ENTITY
//	    M_SFINP2_Summary_Repo.save(existing);
//
//	    // 🔥 CALL PROCEDURE AFTER COMMIT
//	    TransactionSynchronizationManager.registerSynchronization(
//	            new TransactionSynchronizationAdapter() {
//
//	                @Override
//	                // ------------------------------
	// Callback function executed synchronously after database transaction commit
	// succeeds
	// ------------------------------
	// public void afterCommit() {
//
//	                    try {
//
//	                        String formattedDate =
//	                                new SimpleDateFormat("dd-MM-yyyy")
//	                                        .format(updatedEntity.getREPORT_DATE());
//
//	                        logger.info(
//	                                "Transaction committed — calling BRRS_M_SFINP2_SUMMARY_PROCEDURE({})",
//	                                formattedDate
//	                        );
//
//	                        jdbcTemplate.update(
//	                                "BEGIN BRRS_M_SFINP2_SUMMARY_PROCEDURE(?); END;",
//	                                formattedDate
//	                        );
//
//	                        logger.info("Procedure executed successfully after commit.");
//
//	                    } catch (Exception e) {
//
//	                        logger.error("Procedure execution failed", e);
//
//	                        throw new RuntimeException(
//	                                "Procedure execution failed",
//	                                e
//	                        );
//	                    }
//	                }
//	            }
//	    );
//	}
//	

	// ------------------------------
	// edit/view page 
	// ------------------------------
	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_SFINP2");

		if (acctNo != null) {
			String sql = "SELECT * FROM BRRS_M_SFINP2_DETAILTABLE WHERE ACCT_NUMBER = ?";
			List<M_SFINP2_Detail_Entity> results = jdbcTemplate.query(sql, new Object[] { acctNo },
					new BeanPropertyRowMapper<>(M_SFINP2_Detail_Entity.class));
			M_SFINP2_Detail_Entity msfinp2Entity = results.isEmpty() ? null : results.get(0);
			if (msfinp2Entity != null && msfinp2Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(msfinp2Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("msfinp2Data", msfinp2Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	// ------------------------------
	// UPDATE/EDIT DETAIL
	// ------------------------------
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String acctBalanceInpula = request.getParameter("acctBalanceInpula");
			String average = request.getParameter("average");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			String sql = "SELECT * FROM BRRS_M_SFINP2_DETAILTABLE WHERE ACCT_NUMBER = ?";
			List<M_SFINP2_Detail_Entity> results = jdbcTemplate.query(sql, new Object[] { acctNo },
					new BeanPropertyRowMapper<>(M_SFINP2_Detail_Entity.class));
			M_SFINP2_Detail_Entity existing = results.isEmpty() ? null : results.get(0);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			// Create old copy for audit comparison
			M_SFINP2_Detail_Entity oldcopy = new M_SFINP2_Detail_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

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
				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newacctBalanceInpula) != 0) {
					existing.setAcctBalanceInpula(newacctBalanceInpula);
					isChanged = true;
					logger.info("Balance updated to {}", newacctBalanceInpula);
				}
			}

			if (average != null && !average.isEmpty()) {
				BigDecimal newaverage = new BigDecimal(average);
				if (existing.getAverage() == null || existing.getAverage().compareTo(newaverage) != 0) {
					existing.setAverage(newaverage);
					isChanged = true;
					logger.info("Balance updated to {}", newaverage);
				}
			}

			if (isChanged) {
				saveDetailEntity(existing);

				// Audit comparison
				auditService.compareEntitiesmanual(oldcopy, existing, acctNo, "M_SFINP2 Detail Screen",
						"BRRS_M_SFINP2_DETAIL");

				logger.info("Record updated successfully for account {}", acctNo);

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// Run summary procedure after commit

				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

					@Override
					// ------------------------------
					// Callback function executed synchronously after database transaction commit
					// succeeds
					// ------------------------------
					public void afterCommit() {
						try {

							logger.info("Transaction committed — calling BRRS_M_SFINP2_SUMMARY_PROCEDURE({})",
									formattedDate);

							jdbcTemplate.update("BEGIN BRRS_M_SFINP2_SUMMARY_PROCEDURE(?); END;", formattedDate);

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
			logger.error("Error updating M_SFINP2 record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}


	// ------------------------------
	// ARCHIVAL LIST
	// ------------------------------
	public List<Object[]> getM_SFINP2Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			String sql = "SELECT * FROM BRRS_M_SFINP2_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
			List<M_SFINP2_Archival_Summary_Entity> repoData = jdbcTemplate.query(sql,
					new BeanPropertyRowMapper<>(M_SFINP2_Archival_Summary_Entity.class));

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SFINP2_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SFINP2_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  M_SFINP2  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// ------------------------------
	// RESUB LIST
	// ------------------------------
	public List<Object[]> getM_SFINP2Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			String sql = "SELECT * FROM BRRS_M_SFINP2_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
			List<M_SFINP2_Archival_Summary_Entity> latestArchivalList = jdbcTemplate.query(sql,
					new BeanPropertyRowMapper<>(M_SFINP2_Archival_Summary_Entity.class));

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_SFINP2_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SFINP2 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// ------------------------------
	// DETAIL EXCEL
	// ------------------------------
	public byte[] BRRS_M_SFINP2DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_SFINP2 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_SFINP2Details");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);
			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "CREDIT EQUIVALENT", "DEBIT EQUIVALENT",
					"REPORT LABEL", "REPORT ADDL CRETIRIA", "REPORT_DATE" };
			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				// Amount columns: ACCT BALANCE (i=3) and average (i=4)
				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}
			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			String sql = "SELECT * FROM BRRS_M_SFINP2_DETAILTABLE WHERE REPORT_DATE = ?";
			List<M_SFINP2_Detail_Entity> reportData = jdbcTemplate.query(sql, new Object[] { parsedToDate },
					new BeanPropertyRowMapper<>(M_SFINP2_Detail_Entity.class));
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SFINP2_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					// Average (right aligned, 3 decimal places)
					balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");
					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_SFINP2 — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_SFINP2 Excel", e);
			return null; // important
		}
	}

	// ------------------------------
	// ARCHIVAL EXCEL
	// ------------------------------
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_SFINP2 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MSFinP2Detail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "CREDIT EQUIVALENT", "DEBIT EQUIVALENT",
					"REPORT LABEL", "REPORT ADDL CRETIRIA", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				// Amount columns: ACCT BALANCE (i=3) and average (i=4)
				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			String sql = "SELECT * FROM BRRS_M_SFINP2_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
			List<M_SFINP2_Archival_Detail_Entity> reportData = jdbcTemplate.query(sql,
					new Object[] { parsedToDate, version },
					new BeanPropertyRowMapper<>(M_SFINP2_Archival_Detail_Entity.class));

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_SFINP2_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					// Average (right aligned, 3 decimal places)
					balanceCell = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_SFINP2 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_SFINP2Excel", e);
			return new byte[0];
		}
	}

	// ------------------------------
	// Extracts report line items from target spreadsheet template
	// ------------------------------
	public List<ReportLineItemDTO> getReportData(String filename) throws Exception {
		List<ReportLineItemDTO> reportData = new ArrayList<>();

		File file = new File(filename);
		if (!file.exists()) {
			throw new Exception("File not found: " + filename);
		}

		FileInputStream fis = new FileInputStream(file);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0);

		final int START_ROW_INDEX = 10;
		final int END_ROW_INDEX = 80;

		Iterator<Row> rowIterator = sheet.iterator();
		int srlNo = 1;

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			int currentRowIndex = row.getRowNum();

			if (currentRowIndex < START_ROW_INDEX) {
				continue;
			}

			if (currentRowIndex > END_ROW_INDEX) {
				break;
			}

			Cell fieldDescCell = row.getCell(0);

			if (fieldDescCell == null || fieldDescCell.getCellType() == Cell.CELL_TYPE_BLANK) {
				continue;
			}

			String fieldDesc = "";
			try {
				fieldDesc = fieldDescCell.getStringCellValue();
			} catch (IllegalStateException e) {

				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				CellValue cellValue = evaluator.evaluate(fieldDescCell);
				if (cellValue != null) {
					if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
						fieldDesc = cellValue.getStringValue();
					} else if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						fieldDesc = String.valueOf(cellValue.getNumberValue());
					} else if (cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
						fieldDesc = String.valueOf(cellValue.getBooleanValue());
					}

				}
				if (fieldDesc.isEmpty() && fieldDescCell.getCellType() == Cell.CELL_TYPE_FORMULA) {

					fieldDesc = fieldDescCell.getCellFormula();
				}
			} catch (Exception e) {
				System.err.println("Error reading cell A" + (currentRowIndex + 1) + ": " + e.getMessage());
				continue;
			}

			if (fieldDesc == null || fieldDesc.trim().isEmpty()) {
				continue;
			}

			ReportLineItemDTO dto = new ReportLineItemDTO();
			dto.setSrlNo(srlNo++);
			dto.setFieldDescription(fieldDesc.trim());

			dto.setReportLabel("R" + (currentRowIndex + 1));

			boolean hasFormula = false;
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if (cell != null && cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					hasFormula = true;
					break;
				}
			}
			dto.setHeader(hasFormula ? "Y" : " ");

			dto.setRemarks("");

			reportData.add(dto);
		}

		workbook.close();
		fis.close();

		System.out.println("✅ M_SFINP2 Report data processed (Excel Row " + (START_ROW_INDEX + 1) + " to "
				+ (END_ROW_INDEX + 1) + "). Total items: " + reportData.size());
		return reportData;
	}

// ------------------------------
// NORMAL SUMMARY EXCEL
// ------------------------------
	public byte[] BRRS_M_SFINP2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= VIEW SCREEN =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");

// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
// Redirecting to Archival
				return getExcelM_SFINP2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
// Redirecting to Resub Excel
				return BRRS_M_SFINP2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_SFINP2EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

// Fetch data

				List<M_SFINP2_Summary_Entity> dataList = jdbcTemplate.query(
						"SELECT * FROM BRRS_M_SFINP2_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
						new Object[] { dateformat.parse(todate) },
						new BeanPropertyRowMapper<>(M_SFINP2_Summary_Entity.class));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_SFINP2 report. Returning empty result.");
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
					throw new SecurityException("Template file exists but is not readable (check permissions): "
							+ templatePath.toAbsolutePath());
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

					int startRow = 6;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_SFINP2_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

//===== Row 6 / Col b (Date) =====

							Cell cell2 = row.getCell(1);
							if (cell2 == null) {
								cell2 = row.createCell(1);
							}

							if (record.getREPORT_DATE() != null) {
								cell2.setCellValue(record.getREPORT_DATE()); // java.util.Date
								cell2.setCellStyle(dateStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row11
// Column C
							row = sheet.getRow(10);
							cell2 = row.createCell(2);
							if (record.getR11_MONTH_END() != null) {
								cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row11
// Column D
							Cell cell3 = row.createCell(3);
							if (record.getR11_AVERAGE() != null) {
								cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row12
							row = sheet.getRow(11);
// Column C
							cell2 = row.createCell(2);
							if (record.getR12_MONTH_END() != null) {
								cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row12
// Column D
							cell3 = row.createCell(3);
							if (record.getR12_AVERAGE() != null) {
								cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
// row13
							row = sheet.getRow(12);
// Column C
							cell2 = row.createCell(2);
							if (record.getR13_MONTH_END() != null) {
								cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row13
// Column D
							cell3 = row.createCell(3);
							if (record.getR13_AVERAGE() != null) {
								cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row14
							row = sheet.getRow(13);
// Column C
							cell2 = row.createCell(2);
							if (record.getR14_MONTH_END() != null) {
								cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row14
// Column D
							cell3 = row.createCell(3);
							if (record.getR14_AVERAGE() != null) {
								cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row15
							row = sheet.getRow(14);
// Column C
							cell2 = row.createCell(2);
							if (record.getR15_MONTH_END() != null) {
								cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row15
// Column D
							cell3 = row.createCell(3);
							if (record.getR15_AVERAGE() != null) {
								cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row16
							row = sheet.getRow(15);
// Column C
							cell2 = row.createCell(2);
							if (record.getR16_MONTH_END() != null) {
								cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row16
// Column D
							cell3 = row.createCell(3);
							if (record.getR16_AVERAGE() != null) {
								cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
// row19
							row = sheet.getRow(18);
// Column C
							cell2 = row.createCell(2);
							if (record.getR19_MONTH_END() != null) {
								cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row20
							row = sheet.getRow(19);
// Column C
							cell2 = row.createCell(2);
							if (record.getR20_MONTH_END() != null) {
								cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row20
// Column D
							cell3 = row.createCell(3);
							if (record.getR20_AVERAGE() != null) {
								cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row21
							row = sheet.getRow(20);
// Column C
							cell2 = row.createCell(2);
							if (record.getR21_MONTH_END() != null) {
								cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row21
// Column D
							cell3 = row.createCell(3);
							if (record.getR21_AVERAGE() != null) {
								cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row23
							row = sheet.getRow(22);
// Column C
							cell2 = row.createCell(2);
							if (record.getR23_MONTH_END() != null) {
								cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row23
// Column D
							cell3 = row.createCell(3);
							if (record.getR23_AVERAGE() != null) {
								cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row24
							row = sheet.getRow(23);
// Column C
							cell2 = row.createCell(2);
							if (record.getR24_MONTH_END() != null) {
								cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row24
// Column D
							cell3 = row.createCell(3);
							if (record.getR24_AVERAGE() != null) {
								cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row27
							row = sheet.getRow(26);
// Column C
							cell2 = row.createCell(2);
							if (record.getR27_MONTH_END() != null) {
								cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row27
// Column D
							cell3 = row.createCell(3);
							if (record.getR27_AVERAGE() != null) {
								cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row28
							row = sheet.getRow(27);
// Column C
							cell2 = row.createCell(2);
							if (record.getR28_MONTH_END() != null) {
								cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row28
// Column D
							cell3 = row.createCell(3);
							if (record.getR28_AVERAGE() != null) {
								cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(28);
// row29
// Column D
							cell3 = row.createCell(3);
							if (record.getR29_AVERAGE() != null) {
								cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
// row30
							row = sheet.getRow(29);
// Column C
							cell2 = row.createCell(2);
							if (record.getR30_MONTH_END() != null) {
								cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row30
// Column D
							cell3 = row.createCell(3);
							if (record.getR30_AVERAGE() != null) {
								cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
// row31
							row = sheet.getRow(30);
// Column C
							cell2 = row.createCell(2);
							if (record.getR31_MONTH_END() != null) {
								cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row31
// Column D
							cell3 = row.createCell(3);
							if (record.getR31_AVERAGE() != null) {
								cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
// row34
							row = sheet.getRow(33);
// Column C
							cell2 = row.createCell(2);
							if (record.getR34_MONTH_END() != null) {
								cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row34
// Column D
							cell3 = row.createCell(3);
							if (record.getR34_AVERAGE() != null) {
								cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
// row35
							row = sheet.getRow(34);
// Column C
							cell2 = row.createCell(2);
							if (record.getR35_MONTH_END() != null) {
								cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row35
// Column D
							cell3 = row.createCell(3);
							if (record.getR35_AVERAGE() != null) {
								cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
// row36
							row = sheet.getRow(35);
// Column C
							cell2 = row.createCell(2);
							if (record.getR36_MONTH_END() != null) {
								cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row36
// Column D
							cell3 = row.createCell(3);
							if (record.getR36_AVERAGE() != null) {
								cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row37
							row = sheet.getRow(36);
// Column C
							cell2 = row.createCell(2);
							if (record.getR37_MONTH_END() != null) {
								cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row37
// Column D
							cell3 = row.createCell(3);
							if (record.getR37_AVERAGE() != null) {
								cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row38
							row = sheet.getRow(37);
// Column C
							cell2 = row.createCell(2);
							if (record.getR38_MONTH_END() != null) {
								cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row38
// Column D
							cell3 = row.createCell(3);
							if (record.getR38_AVERAGE() != null) {
								cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row39
							row = sheet.getRow(38);
// Column C
							cell2 = row.createCell(2);
							if (record.getR39_MONTH_END() != null) {
								cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row39
// Column D
							cell3 = row.createCell(3);
							if (record.getR39_AVERAGE() != null) {
								cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row40
							row = sheet.getRow(39);
// Column C
							cell2 = row.createCell(2);
							if (record.getR40_MONTH_END() != null) {
								cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row40
// Column D
							cell3 = row.createCell(3);
							if (record.getR40_AVERAGE() != null) {
								cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row42
							row = sheet.getRow(41);
// Column C
							cell2 = row.createCell(2);
							if (record.getR42_MONTH_END() != null) {
								cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row42
// Column D
							cell3 = row.createCell(3);
							if (record.getR42_AVERAGE() != null) {
								cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row43
							row = sheet.getRow(42);
// Column C
							cell2 = row.createCell(2);
							if (record.getR43_MONTH_END() != null) {
								cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row43
// Column D
							cell3 = row.createCell(3);
							if (record.getR43_AVERAGE() != null) {
								cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
// row44
							row = sheet.getRow(43);
// Column C
							cell2 = row.createCell(2);
							if (record.getR44_MONTH_END() != null) {
								cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row44
// Column D
							cell3 = row.createCell(3);
							if (record.getR44_AVERAGE() != null) {
								cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
// row45
							row = sheet.getRow(44);
// Column C
							cell2 = row.createCell(2);
							if (record.getR45_MONTH_END() != null) {
								cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row45
// Column D
							cell3 = row.createCell(3);
							if (record.getR45_AVERAGE() != null) {
								cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row47
							row = sheet.getRow(46);
// Column C
							cell2 = row.createCell(2);
							if (record.getR47_MONTH_END() != null) {
								cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row47
// Column D
							cell3 = row.createCell(3);
							if (record.getR47_AVERAGE() != null) {
								cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row48
							row = sheet.getRow(47);
// Column C
							cell2 = row.createCell(2);
							if (record.getR48_MONTH_END() != null) {
								cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row48
// Column D
							cell3 = row.createCell(3);
							if (record.getR48_AVERAGE() != null) {
								cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row49
							row = sheet.getRow(48);
// Column C
							cell2 = row.createCell(2);
							if (record.getR49_MONTH_END() != null) {
								cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row49
// Column D
							cell3 = row.createCell(3);
							if (record.getR49_AVERAGE() != null) {
								cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
// row51
							row = sheet.getRow(50);
// Column C
							cell2 = row.createCell(2);
							if (record.getR51_MONTH_END() != null) {
								cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row51
// Column D
							cell3 = row.createCell(3);
							if (record.getR51_AVERAGE() != null) {
								cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row52
							row = sheet.getRow(51);
// Column C
							cell2 = row.createCell(2);
							if (record.getR52_MONTH_END() != null) {
								cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row52
// Column D
							cell3 = row.createCell(3);
							if (record.getR52_AVERAGE() != null) {
								cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row53
							row = sheet.getRow(52);
// Column C
							cell2 = row.createCell(2);
							if (record.getR53_MONTH_END() != null) {
								cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row53
// Column D
							cell3 = row.createCell(3);
							if (record.getR53_AVERAGE() != null) {
								cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row54
							row = sheet.getRow(53);
// Column C
							cell2 = row.createCell(2);
							if (record.getR54_MONTH_END() != null) {
								cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row54
// Column D
							cell3 = row.createCell(3);
							if (record.getR54_AVERAGE() != null) {
								cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row55
							row = sheet.getRow(54);
// Column C
							cell2 = row.createCell(2);
							if (record.getR55_MONTH_END() != null) {
								cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row55
// Column D
							cell3 = row.createCell(3);
							if (record.getR55_AVERAGE() != null) {
								cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row56
							row = sheet.getRow(55);
// Column C
							cell2 = row.createCell(2);
							if (record.getR56_MONTH_END() != null) {
								cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row56
// Column D
							cell3 = row.createCell(3);
							if (record.getR56_AVERAGE() != null) {
								cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row57
							row = sheet.getRow(56);
// Column C
							cell2 = row.createCell(2);
							if (record.getR57_MONTH_END() != null) {
								cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row57
// Column D
							cell3 = row.createCell(3);
							if (record.getR57_AVERAGE() != null) {
								cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row58
							row = sheet.getRow(57);
// Column C
							cell2 = row.createCell(2);
							if (record.getR58_MONTH_END() != null) {
								cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row58
// Column D
							cell3 = row.createCell(3);
							if (record.getR58_AVERAGE() != null) {
								cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row60
							row = sheet.getRow(59);
// Column C
							cell2 = row.createCell(2);
							if (record.getR60_MONTH_END() != null) {
								cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row60
// Column D
							cell3 = row.createCell(3);
							if (record.getR60_AVERAGE() != null) {
								cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row61
							row = sheet.getRow(60);
// Column C
							cell2 = row.createCell(2);
							if (record.getR61_MONTH_END() != null) {
								cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row61
// Column D
							cell3 = row.createCell(3);
							if (record.getR61_AVERAGE() != null) {
								cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row62
							row = sheet.getRow(61);
// Column C
							cell2 = row.createCell(2);
							if (record.getR62_MONTH_END() != null) {
								cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row62
// Column D
							cell3 = row.createCell(3);
							if (record.getR62_AVERAGE() != null) {
								cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
// row64
							row = sheet.getRow(63);
// Column C
							cell2 = row.createCell(2);
							if (record.getR64_MONTH_END() != null) {
								cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row64
// Column D
							cell3 = row.createCell(3);
							if (record.getR64_AVERAGE() != null) {
								cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row66
							row = sheet.getRow(65);
// Column C
							cell2 = row.createCell(2);
							if (record.getR66_MONTH_END() != null) {
								cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row66
// Column D
							cell3 = row.createCell(3);
							if (record.getR66_AVERAGE() != null) {
								cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row67
							row = sheet.getRow(66);
// Column C
							cell2 = row.createCell(2);
							if (record.getR67_MONTH_END() != null) {
								cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row67
// Column D
							cell3 = row.createCell(3);
							if (record.getR67_AVERAGE() != null) {
								cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row69
							row = sheet.getRow(68);
// Column C
							cell2 = row.createCell(2);
							if (record.getR69_MONTH_END() != null) {
								cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row69
// Column D
							cell3 = row.createCell(3);
							if (record.getR69_AVERAGE() != null) {
								cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row70
							row = sheet.getRow(69);
// Column C
							cell2 = row.createCell(2);
							if (record.getR70_MONTH_END() != null) {
								cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row70
// Column D
							cell3 = row.createCell(3);
							if (record.getR70_AVERAGE() != null) {
								cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row71
							row = sheet.getRow(70);
// Column C
							cell2 = row.createCell(2);
							if (record.getR71_MONTH_END() != null) {
								cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row71
// Column D
							cell3 = row.createCell(3);
							if (record.getR71_AVERAGE() != null) {
								cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(71);
// row72
// Column D
							cell3 = row.createCell(3);
							if (record.getR72_AVERAGE() != null) {
								cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row74
							row = sheet.getRow(73);
// Column C
							cell2 = row.createCell(2);
							if (record.getR74_MONTH_END() != null) {
								cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row74
// Column D
							cell3 = row.createCell(3);
							if (record.getR74_AVERAGE() != null) {
								cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row75
							row = sheet.getRow(74);
// Column C
							cell2 = row.createCell(2);
							if (record.getR75_MONTH_END() != null) {
								cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row75
// Column D
							cell3 = row.createCell(3);
							if (record.getR75_AVERAGE() != null) {
								cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row76
							row = sheet.getRow(75);
// Column C
							cell2 = row.createCell(2);
							if (record.getR76_MONTH_END() != null) {
								cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row76
// Column D
							cell3 = row.createCell(3);
							if (record.getR76_AVERAGE() != null) {
								cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row77
							row = sheet.getRow(76);
// Column C
							cell2 = row.createCell(2);
							if (record.getR77_MONTH_END() != null) {
								cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row77
// Column D
							cell3 = row.createCell(3);
							if (record.getR77_AVERAGE() != null) {
								cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row78
							row = sheet.getRow(77);
// Column C
							cell2 = row.createCell(2);
							if (record.getR78_MONTH_END() != null) {
								cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row78
// Column D
							cell3 = row.createCell(3);
							if (record.getR78_AVERAGE() != null) {
								cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

// row79
							row = sheet.getRow(78);
// Column C
							cell2 = row.createCell(2);
							if (record.getR79_MONTH_END() != null) {
								cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

// row79
// Column D
							cell3 = row.createCell(3);
							if (record.getR79_AVERAGE() != null) {
								cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

						}
						workbook.setForceFormulaRecalculation(true);
					} else {

					}

// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SFINP2 SUMMARY", null,
								"BRRS_M_SFINP2_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}


// ------------------------------
// SUMMARY EMAIL EXCEL
// ------------------------------
	public byte[] BRRS_M_SFINP2EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
// Redirecting to Archival
				return BRRS_M_SFINP2ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
//  Redirecting to Resub Excel
				return BRRS_M_SFINP2EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			String sql = "SELECT * FROM BRRS_M_SFINP2_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			List<M_SFINP2_Summary_Entity> dataList = jdbcTemplate.query(sql, new Object[] { dateformat.parse(todate) },
					new BeanPropertyRowMapper<>(M_SFINP2_Summary_Entity.class));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_SFINP2 report. Returning empty result.");
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
				throw new SecurityException("Template file exists but is not readable (check permissions): "
						+ templatePath.toAbsolutePath());
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

				int startRow = 6;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_SFINP2_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						Cell cell2 = row.getCell(1);
						if (cell2 == null) {
							cell2 = row.createCell(1);
						}

						if (record.getREPORT_DATE() != null) {
							cell2.setCellValue(record.getREPORT_DATE()); // java.util.Date
							cell2.setCellStyle(dateStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

//row11--------------->23. Current deposits

// Column C
						row = sheet.getRow(10);
						cell2 = row.createCell(2);
						if (record.getR11_MONTH_END() != null) {
							cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row11
// Column D
						Cell cell3 = row.createCell(3);
						if (record.getR11_AVERAGE() != null) {
							cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row12------------------>24. Call deposits

						row = sheet.getRow(11);
// Column C
						cell2 = row.createCell(2);
						if (record.getR12_MONTH_END() != null) {
							cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row12
// Column D
						cell3 = row.createCell(3);
						if (record.getR12_AVERAGE() != null) {
							cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row13------------->25. Savings deposits

						row = sheet.getRow(12);
// Column C
						cell2 = row.createCell(2);
						if (record.getR13_MONTH_END() != null) {
							cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row13
// Column D
						cell3 = row.createCell(3);
						if (record.getR13_AVERAGE() != null) {
							cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row14------------26. Notice deposits

						row = sheet.getRow(13);
// Column C
						cell2 = row.createCell(2);
						if (record.getR14_MONTH_END() != null) {
							cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row14
// Column D
						cell3 = row.createCell(3);
						if (record.getR14_AVERAGE() != null) {
							cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row15----------->27. Fixed deposits

						row = sheet.getRow(14);
// Column C
						cell2 = row.createCell(2);
						if (record.getR15_MONTH_END() != null) {
							cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row15
// Column D
						cell3 = row.createCell(3);
						if (record.getR15_AVERAGE() != null) {
							cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row16---------------28. Certificates of deposit 

						row = sheet.getRow(15);
// Column C
						cell2 = row.createCell(2);
						if (record.getR16_MONTH_END() != null) {
							cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row16
// Column D
						cell3 = row.createCell(3);
						if (record.getR16_AVERAGE() != null) {
							cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row19-------------(a) Affiliated

						row = sheet.getRow(18);
// Column C
						cell2 = row.createCell(2);
						if (record.getR19_MONTH_END() != null) {
							cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row20
						row = sheet.getRow(19);
// Column C
						cell2 = row.createCell(2);
						if (record.getR20_MONTH_END() != null) {
							cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row20
// Column D
						cell3 = row.createCell(3);
						if (record.getR20_AVERAGE() != null) {
							cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row21
						row = sheet.getRow(20);
// Column C
						cell2 = row.createCell(2);
						if (record.getR21_MONTH_END() != null) {
							cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row21
// Column D
						cell3 = row.createCell(3);
						if (record.getR21_AVERAGE() != null) {
							cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row23
						row = sheet.getRow(22);
// Column C
						cell2 = row.createCell(2);
						if (record.getR23_MONTH_END() != null) {
							cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row23
// Column D
						cell3 = row.createCell(3);
						if (record.getR23_AVERAGE() != null) {
							cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row24
						row = sheet.getRow(23);
// Column C
						cell2 = row.createCell(2);
						if (record.getR24_MONTH_END() != null) {
							cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row24
// Column D
						cell3 = row.createCell(3);
						if (record.getR24_AVERAGE() != null) {
							cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row27
						row = sheet.getRow(26);
// Column C
						cell2 = row.createCell(2);
						if (record.getR27_MONTH_END() != null) {
							cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row27
// Column D
						cell3 = row.createCell(3);
						if (record.getR27_AVERAGE() != null) {
							cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row28
						row = sheet.getRow(27);
// Column C
						cell2 = row.createCell(2);
						if (record.getR28_MONTH_END() != null) {
							cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row28
// Column D
						cell3 = row.createCell(3);
						if (record.getR28_AVERAGE() != null) {
							cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(28);
// row29
// Column D
						cell3 = row.createCell(3);
						if (record.getR29_AVERAGE() != null) {
							cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
// row30
						row = sheet.getRow(29);
// Column C
						cell2 = row.createCell(2);
						if (record.getR30_MONTH_END() != null) {
							cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row30
// Column D
						cell3 = row.createCell(3);
						if (record.getR30_AVERAGE() != null) {
							cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
// row31
						row = sheet.getRow(30);
// Column C
						cell2 = row.createCell(2);
						if (record.getR31_MONTH_END() != null) {
							cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row31
// Column D
						cell3 = row.createCell(3);
						if (record.getR31_AVERAGE() != null) {
							cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row34
						row = sheet.getRow(33);
// Column C
						cell2 = row.createCell(2);
						if (record.getR34_MONTH_END() != null) {
							cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row34
// Column D
						cell3 = row.createCell(3);
						if (record.getR34_AVERAGE() != null) {
							cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row35
						row = sheet.getRow(34);
// Column C
						cell2 = row.createCell(2);
						if (record.getR35_MONTH_END() != null) {
							cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row35
// Column D
						cell3 = row.createCell(3);
						if (record.getR35_AVERAGE() != null) {
							cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row36
						row = sheet.getRow(35);
// Column C
						cell2 = row.createCell(2);
						if (record.getR36_MONTH_END() != null) {
							cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row36
// Column D
						cell3 = row.createCell(3);
						if (record.getR36_AVERAGE() != null) {
							cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row37
						row = sheet.getRow(36);
// Column C
						cell2 = row.createCell(2);
						if (record.getR37_MONTH_END() != null) {
							cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row37
// Column D
						cell3 = row.createCell(3);
						if (record.getR37_AVERAGE() != null) {
							cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row38
						row = sheet.getRow(37);
// Column C
						cell2 = row.createCell(2);
						if (record.getR38_MONTH_END() != null) {
							cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row38
// Column D
						cell3 = row.createCell(3);
						if (record.getR38_AVERAGE() != null) {
							cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row39
						row = sheet.getRow(38);
// Column C
						cell2 = row.createCell(2);
						if (record.getR39_MONTH_END() != null) {
							cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row39
// Column D
						cell3 = row.createCell(3);
						if (record.getR39_AVERAGE() != null) {
							cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row40
						row = sheet.getRow(39);
// Column C
						cell2 = row.createCell(2);
						if (record.getR40_MONTH_END() != null) {
							cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row40
// Column D
						cell3 = row.createCell(3);
						if (record.getR40_AVERAGE() != null) {
							cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row42
						row = sheet.getRow(41);
// Column C
						cell2 = row.createCell(2);
						if (record.getR42_MONTH_END() != null) {
							cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row42
// Column D
						cell3 = row.createCell(3);
						if (record.getR42_AVERAGE() != null) {
							cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row43
						row = sheet.getRow(42);
// Column C
						cell2 = row.createCell(2);
						if (record.getR43_MONTH_END() != null) {
							cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row43
// Column D
						cell3 = row.createCell(3);
						if (record.getR43_AVERAGE() != null) {
							cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
// row44
						row = sheet.getRow(43);
// Column C
						cell2 = row.createCell(2);
						if (record.getR44_MONTH_END() != null) {
							cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row44
// Column D
						cell3 = row.createCell(3);
						if (record.getR44_AVERAGE() != null) {
							cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
// row45
						row = sheet.getRow(44);
// Column C
						cell2 = row.createCell(2);
						if (record.getR45_MONTH_END() != null) {
							cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row45
// Column D
						cell3 = row.createCell(3);
						if (record.getR45_AVERAGE() != null) {
							cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row47
						row = sheet.getRow(46);
// Column C
						cell2 = row.createCell(2);
						if (record.getR47_MONTH_END() != null) {
							cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row47
// Column D
						cell3 = row.createCell(3);
						if (record.getR47_AVERAGE() != null) {
							cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row48
						row = sheet.getRow(47);
// Column C
						cell2 = row.createCell(2);
						if (record.getR48_MONTH_END() != null) {
							cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row48
// Column D
						cell3 = row.createCell(3);
						if (record.getR48_AVERAGE() != null) {
							cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row49
						row = sheet.getRow(48);
// Column C
						cell2 = row.createCell(2);
						if (record.getR49_MONTH_END() != null) {
							cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row49
// Column D
						cell3 = row.createCell(3);
						if (record.getR49_AVERAGE() != null) {
							cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row51
						row = sheet.getRow(50);
// Column C
						cell2 = row.createCell(2);
						if (record.getR51_MONTH_END() != null) {
							cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row51
// Column D
						cell3 = row.createCell(3);
						if (record.getR51_AVERAGE() != null) {
							cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row52
						row = sheet.getRow(51);
// Column C
						cell2 = row.createCell(2);
						if (record.getR52_MONTH_END() != null) {
							cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row52
// Column D
						cell3 = row.createCell(3);
						if (record.getR52_AVERAGE() != null) {
							cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row53
						row = sheet.getRow(52);
// Column C
						cell2 = row.createCell(2);
						if (record.getR53_MONTH_END() != null) {
							cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row53
// Column D
						cell3 = row.createCell(3);
						if (record.getR53_AVERAGE() != null) {
							cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row54
						row = sheet.getRow(53);
// Column C
						cell2 = row.createCell(2);
						if (record.getR54_MONTH_END() != null) {
							cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row54
// Column D
						cell3 = row.createCell(3);
						if (record.getR54_AVERAGE() != null) {
							cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row55
						row = sheet.getRow(54);
// Column C
						cell2 = row.createCell(2);
						if (record.getR55_MONTH_END() != null) {
							cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row55
// Column D
						cell3 = row.createCell(3);
						if (record.getR55_AVERAGE() != null) {
							cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row56
						row = sheet.getRow(55);
// Column C
						cell2 = row.createCell(2);
						if (record.getR56_MONTH_END() != null) {
							cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row56
// Column D
						cell3 = row.createCell(3);
						if (record.getR56_AVERAGE() != null) {
							cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row57
						row = sheet.getRow(56);
// Column C
						cell2 = row.createCell(2);
						if (record.getR57_MONTH_END() != null) {
							cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row57
// Column D
						cell3 = row.createCell(3);
						if (record.getR57_AVERAGE() != null) {
							cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row58
						row = sheet.getRow(57);
// Column C
						cell2 = row.createCell(2);
						if (record.getR58_MONTH_END() != null) {
							cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row58
// Column D
						cell3 = row.createCell(3);
						if (record.getR58_AVERAGE() != null) {
							cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row60
						row = sheet.getRow(59);
// Column C
						cell2 = row.createCell(2);
						if (record.getR60_MONTH_END() != null) {
							cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row60
// Column D
						cell3 = row.createCell(3);
						if (record.getR60_AVERAGE() != null) {
							cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row61
						row = sheet.getRow(60);
// Column C
						cell2 = row.createCell(2);
						if (record.getR61_MONTH_END() != null) {
							cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row61
// Column D
						cell3 = row.createCell(3);
						if (record.getR61_AVERAGE() != null) {
							cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row62
						row = sheet.getRow(61);
// Column C
						cell2 = row.createCell(2);
						if (record.getR62_MONTH_END() != null) {
							cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row62
// Column D
						cell3 = row.createCell(3);
						if (record.getR62_AVERAGE() != null) {
							cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row64
						row = sheet.getRow(63);
// Column C
						cell2 = row.createCell(2);
						if (record.getR64_MONTH_END() != null) {
							cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row64
// Column D
						cell3 = row.createCell(3);
						if (record.getR64_AVERAGE() != null) {
							cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row66
						row = sheet.getRow(65);
// Column C
						cell2 = row.createCell(2);
						if (record.getR66_MONTH_END() != null) {
							cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row66
// Column D
						cell3 = row.createCell(3);
						if (record.getR66_AVERAGE() != null) {
							cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row67
						row = sheet.getRow(66);
// Column C
						cell2 = row.createCell(2);
						if (record.getR67_MONTH_END() != null) {
							cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row67
// Column D
						cell3 = row.createCell(3);
						if (record.getR67_AVERAGE() != null) {
							cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row69
						row = sheet.getRow(68);
// Column C
						cell2 = row.createCell(2);
						if (record.getR69_MONTH_END() != null) {
							cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row69
// Column D
						cell3 = row.createCell(3);
						if (record.getR69_AVERAGE() != null) {
							cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row70
						row = sheet.getRow(69);
// Column C
						cell2 = row.createCell(2);
						if (record.getR70_MONTH_END() != null) {
							cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row70
// Column D
						cell3 = row.createCell(3);
						if (record.getR70_AVERAGE() != null) {
							cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row71
						row = sheet.getRow(70);
// Column C
						cell2 = row.createCell(2);
						if (record.getR71_MONTH_END() != null) {
							cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row71
// Column D
						cell3 = row.createCell(3);
						if (record.getR71_AVERAGE() != null) {
							cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(71);
// row72
// Column D
						cell3 = row.createCell(3);
						if (record.getR72_AVERAGE() != null) {
							cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row74
						row = sheet.getRow(73);
// Column C
						cell2 = row.createCell(2);
						if (record.getR74_MONTH_END() != null) {
							cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row74
// Column D
						cell3 = row.createCell(3);
						if (record.getR74_AVERAGE() != null) {
							cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row75
						row = sheet.getRow(74);
// Column C
						cell2 = row.createCell(2);
						if (record.getR75_MONTH_END() != null) {
							cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row75
// Column D
						cell3 = row.createCell(3);
						if (record.getR75_AVERAGE() != null) {
							cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row76
						row = sheet.getRow(75);
// Column C
						cell2 = row.createCell(2);
						if (record.getR76_MONTH_END() != null) {
							cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row76
// Column D
						cell3 = row.createCell(3);
						if (record.getR76_AVERAGE() != null) {
							cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row77
						row = sheet.getRow(76);
// Column C
						cell2 = row.createCell(2);
						if (record.getR77_MONTH_END() != null) {
							cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row77
// Column D
						cell3 = row.createCell(3);
						if (record.getR77_AVERAGE() != null) {
							cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row78
						row = sheet.getRow(77);
// Column C
						cell2 = row.createCell(2);
						if (record.getR78_MONTH_END() != null) {
							cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row78
// Column D
						cell3 = row.createCell(3);
						if (record.getR78_AVERAGE() != null) {
							cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

// row79
						row = sheet.getRow(78);
// Column C
						cell2 = row.createCell(2);
						if (record.getR79_MONTH_END() != null) {
							cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

// row79
// Column D
						cell3 = row.createCell(3);
						if (record.getR79_AVERAGE() != null) {
							cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

					}
					workbook.setForceFormulaRecalculation(true);
				} else {

				}

// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SFINP2 EMAIL SUMMARY", null,
							"BRRS_M_SFINP2_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

// ------------------------------
// Generates archival excel
// ------------------------------
	public byte[] getExcelM_SFINP2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
// Redirecting to Archival
				return BRRS_M_SFINP2ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SFINP2_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP2_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version },
				new BeanPropertyRowMapper<>(M_SFINP2_Archival_Summary_Entity.class));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SFINP2 report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SFINP2_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell cell2 = row.getCell(1);
					if (cell2 == null) {
						cell2 = row.createCell(1);
					}

					if (record.getREPORT_DATE() != null) {
						cell2.setCellValue(record.getREPORT_DATE()); // java.util.Date
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row11
// Column C
					cell2 = row.createCell(2);
					if (record.getR11_MONTH_END() != null) {
						cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row11
// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR11_AVERAGE() != null) {
						cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row12
					row = sheet.getRow(11);
// Column C
					cell2 = row.createCell(2);
					if (record.getR12_MONTH_END() != null) {
						cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row12
// Column D
					cell3 = row.createCell(3);
					if (record.getR12_AVERAGE() != null) {
						cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row13
					row = sheet.getRow(12);
// Column C
					cell2 = row.createCell(2);
					if (record.getR13_MONTH_END() != null) {
						cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row13
// Column D
					cell3 = row.createCell(3);
					if (record.getR13_AVERAGE() != null) {
						cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row14
					row = sheet.getRow(13);
// Column C
					cell2 = row.createCell(2);
					if (record.getR14_MONTH_END() != null) {
						cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row14
// Column D
					cell3 = row.createCell(3);
					if (record.getR14_AVERAGE() != null) {
						cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row15
					row = sheet.getRow(14);
// Column C
					cell2 = row.createCell(2);
					if (record.getR15_MONTH_END() != null) {
						cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row15
// Column D
					cell3 = row.createCell(3);
					if (record.getR15_AVERAGE() != null) {
						cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row16
					row = sheet.getRow(15);
// Column C
					cell2 = row.createCell(2);
					if (record.getR16_MONTH_END() != null) {
						cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row16
// Column D
					cell3 = row.createCell(3);
					if (record.getR16_AVERAGE() != null) {
						cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row19
					row = sheet.getRow(18);
// Column C
					cell2 = row.createCell(2);
					if (record.getR19_MONTH_END() != null) {
						cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row20
					row = sheet.getRow(19);
// Column C
					cell2 = row.createCell(2);
					if (record.getR20_MONTH_END() != null) {
						cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row20
// Column D
					cell3 = row.createCell(3);
					if (record.getR20_AVERAGE() != null) {
						cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row21
					row = sheet.getRow(20);
// Column C
					cell2 = row.createCell(2);
					if (record.getR21_MONTH_END() != null) {
						cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row21
// Column D
					cell3 = row.createCell(3);
					if (record.getR21_AVERAGE() != null) {
						cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row23
					row = sheet.getRow(22);
// Column C
					cell2 = row.createCell(2);
					if (record.getR23_MONTH_END() != null) {
						cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row23
// Column D
					cell3 = row.createCell(3);
					if (record.getR23_AVERAGE() != null) {
						cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row24
					row = sheet.getRow(23);
// Column C
					cell2 = row.createCell(2);
					if (record.getR24_MONTH_END() != null) {
						cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row24
// Column D
					cell3 = row.createCell(3);
					if (record.getR24_AVERAGE() != null) {
						cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row27
					row = sheet.getRow(26);
// Column C
					cell2 = row.createCell(2);
					if (record.getR27_MONTH_END() != null) {
						cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row27
// Column D
					cell3 = row.createCell(3);
					if (record.getR27_AVERAGE() != null) {
						cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row28
					row = sheet.getRow(27);
// Column C
					cell2 = row.createCell(2);
					if (record.getR28_MONTH_END() != null) {
						cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row28
// Column D
					cell3 = row.createCell(3);
					if (record.getR28_AVERAGE() != null) {
						cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
// row29
// Column D
					cell3 = row.createCell(3);
					if (record.getR29_AVERAGE() != null) {
						cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row30
					row = sheet.getRow(29);
// Column C
					cell2 = row.createCell(2);
					if (record.getR30_MONTH_END() != null) {
						cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row30
// Column D
					cell3 = row.createCell(3);
					if (record.getR30_AVERAGE() != null) {
						cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row31
					row = sheet.getRow(30);
// Column C
					cell2 = row.createCell(2);
					if (record.getR31_MONTH_END() != null) {
						cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row31
// Column D
					cell3 = row.createCell(3);
					if (record.getR31_AVERAGE() != null) {
						cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row34
					row = sheet.getRow(33);
// Column C
					cell2 = row.createCell(2);
					if (record.getR34_MONTH_END() != null) {
						cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row34
// Column D
					cell3 = row.createCell(3);
					if (record.getR34_AVERAGE() != null) {
						cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row35
					row = sheet.getRow(34);
// Column C
					cell2 = row.createCell(2);
					if (record.getR35_MONTH_END() != null) {
						cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row35
// Column D
					cell3 = row.createCell(3);
					if (record.getR35_AVERAGE() != null) {
						cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row36
					row = sheet.getRow(35);
// Column C
					cell2 = row.createCell(2);
					if (record.getR36_MONTH_END() != null) {
						cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row36
// Column D
					cell3 = row.createCell(3);
					if (record.getR36_AVERAGE() != null) {
						cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row37
					row = sheet.getRow(36);
// Column C
					cell2 = row.createCell(2);
					if (record.getR37_MONTH_END() != null) {
						cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row37
// Column D
					cell3 = row.createCell(3);
					if (record.getR37_AVERAGE() != null) {
						cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row38
					row = sheet.getRow(37);
// Column C
					cell2 = row.createCell(2);
					if (record.getR38_MONTH_END() != null) {
						cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row38
// Column D
					cell3 = row.createCell(3);
					if (record.getR38_AVERAGE() != null) {
						cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row39
					row = sheet.getRow(38);
// Column C
					cell2 = row.createCell(2);
					if (record.getR39_MONTH_END() != null) {
						cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row39
// Column D
					cell3 = row.createCell(3);
					if (record.getR39_AVERAGE() != null) {
						cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row40
					row = sheet.getRow(39);
// Column C
					cell2 = row.createCell(2);
					if (record.getR40_MONTH_END() != null) {
						cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row40
// Column D
					cell3 = row.createCell(3);
					if (record.getR40_AVERAGE() != null) {
						cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row42
					row = sheet.getRow(41);
// Column C
					cell2 = row.createCell(2);
					if (record.getR42_MONTH_END() != null) {
						cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row42
// Column D
					cell3 = row.createCell(3);
					if (record.getR42_AVERAGE() != null) {
						cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row43
					row = sheet.getRow(42);
// Column C
					cell2 = row.createCell(2);
					if (record.getR43_MONTH_END() != null) {
						cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row43
// Column D
					cell3 = row.createCell(3);
					if (record.getR43_AVERAGE() != null) {
						cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row44
					row = sheet.getRow(43);
// Column C
					cell2 = row.createCell(2);
					if (record.getR44_MONTH_END() != null) {
						cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row44
// Column D
					cell3 = row.createCell(3);
					if (record.getR44_AVERAGE() != null) {
						cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row45
					row = sheet.getRow(44);
// Column C
					cell2 = row.createCell(2);
					if (record.getR45_MONTH_END() != null) {
						cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row45
// Column D
					cell3 = row.createCell(3);
					if (record.getR45_AVERAGE() != null) {
						cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row47
					row = sheet.getRow(46);
// Column C
					cell2 = row.createCell(2);
					if (record.getR47_MONTH_END() != null) {
						cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row47
// Column D
					cell3 = row.createCell(3);
					if (record.getR47_AVERAGE() != null) {
						cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row48
					row = sheet.getRow(47);
// Column C
					cell2 = row.createCell(2);
					if (record.getR48_MONTH_END() != null) {
						cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row48
// Column D
					cell3 = row.createCell(3);
					if (record.getR48_AVERAGE() != null) {
						cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row49
					row = sheet.getRow(48);
// Column C
					cell2 = row.createCell(2);
					if (record.getR49_MONTH_END() != null) {
						cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row49
// Column D
					cell3 = row.createCell(3);
					if (record.getR49_AVERAGE() != null) {
						cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row51
					row = sheet.getRow(50);
// Column C
					cell2 = row.createCell(2);
					if (record.getR51_MONTH_END() != null) {
						cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row51
// Column D
					cell3 = row.createCell(3);
					if (record.getR51_AVERAGE() != null) {
						cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row52
					row = sheet.getRow(51);
// Column C
					cell2 = row.createCell(2);
					if (record.getR52_MONTH_END() != null) {
						cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row52
// Column D
					cell3 = row.createCell(3);
					if (record.getR52_AVERAGE() != null) {
						cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row53
					row = sheet.getRow(52);
// Column C
					cell2 = row.createCell(2);
					if (record.getR53_MONTH_END() != null) {
						cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row53
// Column D
					cell3 = row.createCell(3);
					if (record.getR53_AVERAGE() != null) {
						cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row54
					row = sheet.getRow(53);
// Column C
					cell2 = row.createCell(2);
					if (record.getR54_MONTH_END() != null) {
						cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row54
// Column D
					cell3 = row.createCell(3);
					if (record.getR54_AVERAGE() != null) {
						cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row55
					row = sheet.getRow(54);
// Column C
					cell2 = row.createCell(2);
					if (record.getR55_MONTH_END() != null) {
						cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row55
// Column D
					cell3 = row.createCell(3);
					if (record.getR55_AVERAGE() != null) {
						cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row56
					row = sheet.getRow(55);
// Column C
					cell2 = row.createCell(2);
					if (record.getR56_MONTH_END() != null) {
						cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row56
// Column D
					cell3 = row.createCell(3);
					if (record.getR56_AVERAGE() != null) {
						cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row57
					row = sheet.getRow(56);
// Column C
					cell2 = row.createCell(2);
					if (record.getR57_MONTH_END() != null) {
						cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row57
// Column D
					cell3 = row.createCell(3);
					if (record.getR57_AVERAGE() != null) {
						cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row58
					row = sheet.getRow(57);
// Column C
					cell2 = row.createCell(2);
					if (record.getR58_MONTH_END() != null) {
						cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row58
// Column D
					cell3 = row.createCell(3);
					if (record.getR58_AVERAGE() != null) {
						cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row60
					row = sheet.getRow(59);
// Column C
					cell2 = row.createCell(2);
					if (record.getR60_MONTH_END() != null) {
						cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row60
// Column D
					cell3 = row.createCell(3);
					if (record.getR60_AVERAGE() != null) {
						cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row61
					row = sheet.getRow(60);
// Column C
					cell2 = row.createCell(2);
					if (record.getR61_MONTH_END() != null) {
						cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row61
// Column D
					cell3 = row.createCell(3);
					if (record.getR61_AVERAGE() != null) {
						cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row62
					row = sheet.getRow(61);
// Column C
					cell2 = row.createCell(2);
					if (record.getR62_MONTH_END() != null) {
						cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row62
// Column D
					cell3 = row.createCell(3);
					if (record.getR62_AVERAGE() != null) {
						cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row64
					row = sheet.getRow(63);
// Column C
					cell2 = row.createCell(2);
					if (record.getR64_MONTH_END() != null) {
						cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row64
// Column D
					cell3 = row.createCell(3);
					if (record.getR64_AVERAGE() != null) {
						cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row66
					row = sheet.getRow(65);
// Column C
					cell2 = row.createCell(2);
					if (record.getR66_MONTH_END() != null) {
						cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row66
// Column D
					cell3 = row.createCell(3);
					if (record.getR66_AVERAGE() != null) {
						cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row67
					row = sheet.getRow(66);
// Column C
					cell2 = row.createCell(2);
					if (record.getR67_MONTH_END() != null) {
						cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row67
// Column D
					cell3 = row.createCell(3);
					if (record.getR67_AVERAGE() != null) {
						cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row69
					row = sheet.getRow(68);
// Column C
					cell2 = row.createCell(2);
					if (record.getR69_MONTH_END() != null) {
						cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row69
// Column D
					cell3 = row.createCell(3);
					if (record.getR69_AVERAGE() != null) {
						cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row70
					row = sheet.getRow(69);
// Column C
					cell2 = row.createCell(2);
					if (record.getR70_MONTH_END() != null) {
						cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row70
// Column D
					cell3 = row.createCell(3);
					if (record.getR70_AVERAGE() != null) {
						cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row71
					row = sheet.getRow(70);
// Column C
					cell2 = row.createCell(2);
					if (record.getR71_MONTH_END() != null) {
						cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row71
// Column D
					cell3 = row.createCell(3);
					if (record.getR71_AVERAGE() != null) {
						cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(71);
// row72
// Column D
					cell3 = row.createCell(3);
					if (record.getR72_AVERAGE() != null) {
						cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row74
					row = sheet.getRow(73);
// Column C
					cell2 = row.createCell(2);
					if (record.getR74_MONTH_END() != null) {
						cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row74
// Column D
					cell3 = row.createCell(3);
					if (record.getR74_AVERAGE() != null) {
						cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row75
					row = sheet.getRow(74);
// Column C
					cell2 = row.createCell(2);
					if (record.getR75_MONTH_END() != null) {
						cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row75
// Column D
					cell3 = row.createCell(3);
					if (record.getR75_AVERAGE() != null) {
						cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row76
					row = sheet.getRow(75);
// Column C
					cell2 = row.createCell(2);
					if (record.getR76_MONTH_END() != null) {
						cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row76
// Column D
					cell3 = row.createCell(3);
					if (record.getR76_AVERAGE() != null) {
						cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row77
					row = sheet.getRow(76);
// Column C
					cell2 = row.createCell(2);
					if (record.getR77_MONTH_END() != null) {
						cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row77
// Column D
					cell3 = row.createCell(3);
					if (record.getR77_AVERAGE() != null) {
						cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row78
					row = sheet.getRow(77);
// Column C
					cell2 = row.createCell(2);
					if (record.getR78_MONTH_END() != null) {
						cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row78
// Column D
					cell3 = row.createCell(3);
					if (record.getR78_AVERAGE() != null) {
						cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row79
					row = sheet.getRow(78);
// Column C
					cell2 = row.createCell(2);
					if (record.getR79_MONTH_END() != null) {
						cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row79
// Column D
					cell3 = row.createCell(3);
					if (record.getR79_AVERAGE() != null) {
						cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

				}

				workbook.setForceFormulaRecalculation(true);
			} else {

			}

//Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SFINP2 ARCHIVAL SUMMARY", null,
						"BRRS_M_SFINP2_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

// ------------------------------
// Archival Email Excel
// ------------------------------
	public byte[] BRRS_M_SFINP2ARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_SFINP2_Archival_Summary_Entity> dataList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP2_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version },
				new BeanPropertyRowMapper<>(M_SFINP2_Archival_Summary_Entity.class));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SFINP2 report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SFINP2_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell2 = row.getCell(1);
					if (cell2 == null) {
						cell2 = row.createCell(1);
					}

					if (record.getREPORT_DATE() != null) {
						cell2.setCellValue(record.getREPORT_DATE()); // java.util.Date
						cell2.setCellStyle(dateStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//row11--------------->23. Current deposits

// Column C
					row = sheet.getRow(10);
					cell2 = row.createCell(2);
					if (record.getR11_MONTH_END() != null) {
						cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row11
// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR11_AVERAGE() != null) {
						cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row12------------------>24. Call deposits

					row = sheet.getRow(11);
// Column C
					cell2 = row.createCell(2);
					if (record.getR12_MONTH_END() != null) {
						cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row12
// Column D
					cell3 = row.createCell(3);
					if (record.getR12_AVERAGE() != null) {
						cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row13------------->25. Savings deposits

					row = sheet.getRow(12);
// Column C
					cell2 = row.createCell(2);
					if (record.getR13_MONTH_END() != null) {
						cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row13
// Column D
					cell3 = row.createCell(3);
					if (record.getR13_AVERAGE() != null) {
						cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row14------------26. Notice deposits

					row = sheet.getRow(13);
// Column C
					cell2 = row.createCell(2);
					if (record.getR14_MONTH_END() != null) {
						cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row14
// Column D
					cell3 = row.createCell(3);
					if (record.getR14_AVERAGE() != null) {
						cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row15----------->27. Fixed deposits

					row = sheet.getRow(14);
// Column C
					cell2 = row.createCell(2);
					if (record.getR15_MONTH_END() != null) {
						cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row15
// Column D
					cell3 = row.createCell(3);
					if (record.getR15_AVERAGE() != null) {
						cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row16---------------28. Certificates of deposit 

					row = sheet.getRow(15);
// Column C
					cell2 = row.createCell(2);
					if (record.getR16_MONTH_END() != null) {
						cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row16
// Column D
					cell3 = row.createCell(3);
					if (record.getR16_AVERAGE() != null) {
						cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row19-------------(a) Affiliated

					row = sheet.getRow(18);
// Column C
					cell2 = row.createCell(2);
					if (record.getR19_MONTH_END() != null) {
						cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row20
					row = sheet.getRow(19);
// Column C
					cell2 = row.createCell(2);
					if (record.getR20_MONTH_END() != null) {
						cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row20
// Column D
					cell3 = row.createCell(3);
					if (record.getR20_AVERAGE() != null) {
						cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row21
					row = sheet.getRow(20);
// Column C
					cell2 = row.createCell(2);
					if (record.getR21_MONTH_END() != null) {
						cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row21
// Column D
					cell3 = row.createCell(3);
					if (record.getR21_AVERAGE() != null) {
						cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row23
					row = sheet.getRow(22);
// Column C
					cell2 = row.createCell(2);
					if (record.getR23_MONTH_END() != null) {
						cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row23
// Column D
					cell3 = row.createCell(3);
					if (record.getR23_AVERAGE() != null) {
						cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row24
					row = sheet.getRow(23);
// Column C
					cell2 = row.createCell(2);
					if (record.getR24_MONTH_END() != null) {
						cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row24
// Column D
					cell3 = row.createCell(3);
					if (record.getR24_AVERAGE() != null) {
						cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row27
					row = sheet.getRow(26);
// Column C
					cell2 = row.createCell(2);
					if (record.getR27_MONTH_END() != null) {
						cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row27
// Column D
					cell3 = row.createCell(3);
					if (record.getR27_AVERAGE() != null) {
						cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row28
					row = sheet.getRow(27);
// Column C
					cell2 = row.createCell(2);
					if (record.getR28_MONTH_END() != null) {
						cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row28
// Column D
					cell3 = row.createCell(3);
					if (record.getR28_AVERAGE() != null) {
						cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
// row29
// Column D
					cell3 = row.createCell(3);
					if (record.getR29_AVERAGE() != null) {
						cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row30
					row = sheet.getRow(29);
// Column C
					cell2 = row.createCell(2);
					if (record.getR30_MONTH_END() != null) {
						cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row30
// Column D
					cell3 = row.createCell(3);
					if (record.getR30_AVERAGE() != null) {
						cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row31
					row = sheet.getRow(30);
// Column C
					cell2 = row.createCell(2);
					if (record.getR31_MONTH_END() != null) {
						cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row31
// Column D
					cell3 = row.createCell(3);
					if (record.getR31_AVERAGE() != null) {
						cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row34
					row = sheet.getRow(33);
// Column C
					cell2 = row.createCell(2);
					if (record.getR34_MONTH_END() != null) {
						cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row34
// Column D
					cell3 = row.createCell(3);
					if (record.getR34_AVERAGE() != null) {
						cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row35
					row = sheet.getRow(34);
// Column C
					cell2 = row.createCell(2);
					if (record.getR35_MONTH_END() != null) {
						cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row35
// Column D
					cell3 = row.createCell(3);
					if (record.getR35_AVERAGE() != null) {
						cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row36
					row = sheet.getRow(35);
// Column C
					cell2 = row.createCell(2);
					if (record.getR36_MONTH_END() != null) {
						cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row36
// Column D
					cell3 = row.createCell(3);
					if (record.getR36_AVERAGE() != null) {
						cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row37
					row = sheet.getRow(36);
// Column C
					cell2 = row.createCell(2);
					if (record.getR37_MONTH_END() != null) {
						cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row37
// Column D
					cell3 = row.createCell(3);
					if (record.getR37_AVERAGE() != null) {
						cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row38
					row = sheet.getRow(37);
// Column C
					cell2 = row.createCell(2);
					if (record.getR38_MONTH_END() != null) {
						cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row38
// Column D
					cell3 = row.createCell(3);
					if (record.getR38_AVERAGE() != null) {
						cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row39
					row = sheet.getRow(38);
// Column C
					cell2 = row.createCell(2);
					if (record.getR39_MONTH_END() != null) {
						cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row39
// Column D
					cell3 = row.createCell(3);
					if (record.getR39_AVERAGE() != null) {
						cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row40
					row = sheet.getRow(39);
// Column C
					cell2 = row.createCell(2);
					if (record.getR40_MONTH_END() != null) {
						cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row40
// Column D
					cell3 = row.createCell(3);
					if (record.getR40_AVERAGE() != null) {
						cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row42
					row = sheet.getRow(41);
// Column C
					cell2 = row.createCell(2);
					if (record.getR42_MONTH_END() != null) {
						cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row42
// Column D
					cell3 = row.createCell(3);
					if (record.getR42_AVERAGE() != null) {
						cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row43
					row = sheet.getRow(42);
// Column C
					cell2 = row.createCell(2);
					if (record.getR43_MONTH_END() != null) {
						cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row43
// Column D
					cell3 = row.createCell(3);
					if (record.getR43_AVERAGE() != null) {
						cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row44
					row = sheet.getRow(43);
// Column C
					cell2 = row.createCell(2);
					if (record.getR44_MONTH_END() != null) {
						cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row44
// Column D
					cell3 = row.createCell(3);
					if (record.getR44_AVERAGE() != null) {
						cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row45
					row = sheet.getRow(44);
// Column C
					cell2 = row.createCell(2);
					if (record.getR45_MONTH_END() != null) {
						cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row45
// Column D
					cell3 = row.createCell(3);
					if (record.getR45_AVERAGE() != null) {
						cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row47
					row = sheet.getRow(46);
// Column C
					cell2 = row.createCell(2);
					if (record.getR47_MONTH_END() != null) {
						cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row47
// Column D
					cell3 = row.createCell(3);
					if (record.getR47_AVERAGE() != null) {
						cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row48
					row = sheet.getRow(47);
// Column C
					cell2 = row.createCell(2);
					if (record.getR48_MONTH_END() != null) {
						cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row48
// Column D
					cell3 = row.createCell(3);
					if (record.getR48_AVERAGE() != null) {
						cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row49
					row = sheet.getRow(48);
// Column C
					cell2 = row.createCell(2);
					if (record.getR49_MONTH_END() != null) {
						cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row49
// Column D
					cell3 = row.createCell(3);
					if (record.getR49_AVERAGE() != null) {
						cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row51
					row = sheet.getRow(50);
// Column C
					cell2 = row.createCell(2);
					if (record.getR51_MONTH_END() != null) {
						cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row51
// Column D
					cell3 = row.createCell(3);
					if (record.getR51_AVERAGE() != null) {
						cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row52
					row = sheet.getRow(51);
// Column C
					cell2 = row.createCell(2);
					if (record.getR52_MONTH_END() != null) {
						cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row52
// Column D
					cell3 = row.createCell(3);
					if (record.getR52_AVERAGE() != null) {
						cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row53
					row = sheet.getRow(52);
// Column C
					cell2 = row.createCell(2);
					if (record.getR53_MONTH_END() != null) {
						cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row53
// Column D
					cell3 = row.createCell(3);
					if (record.getR53_AVERAGE() != null) {
						cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row54
					row = sheet.getRow(53);
// Column C
					cell2 = row.createCell(2);
					if (record.getR54_MONTH_END() != null) {
						cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row54
// Column D
					cell3 = row.createCell(3);
					if (record.getR54_AVERAGE() != null) {
						cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row55
					row = sheet.getRow(54);
// Column C
					cell2 = row.createCell(2);
					if (record.getR55_MONTH_END() != null) {
						cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row55
// Column D
					cell3 = row.createCell(3);
					if (record.getR55_AVERAGE() != null) {
						cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row56
					row = sheet.getRow(55);
// Column C
					cell2 = row.createCell(2);
					if (record.getR56_MONTH_END() != null) {
						cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row56
// Column D
					cell3 = row.createCell(3);
					if (record.getR56_AVERAGE() != null) {
						cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row57
					row = sheet.getRow(56);
// Column C
					cell2 = row.createCell(2);
					if (record.getR57_MONTH_END() != null) {
						cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row57
// Column D
					cell3 = row.createCell(3);
					if (record.getR57_AVERAGE() != null) {
						cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row58
					row = sheet.getRow(57);
// Column C
					cell2 = row.createCell(2);
					if (record.getR58_MONTH_END() != null) {
						cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row58
// Column D
					cell3 = row.createCell(3);
					if (record.getR58_AVERAGE() != null) {
						cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row60
					row = sheet.getRow(59);
// Column C
					cell2 = row.createCell(2);
					if (record.getR60_MONTH_END() != null) {
						cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row60
// Column D
					cell3 = row.createCell(3);
					if (record.getR60_AVERAGE() != null) {
						cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row61
					row = sheet.getRow(60);
// Column C
					cell2 = row.createCell(2);
					if (record.getR61_MONTH_END() != null) {
						cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row61
// Column D
					cell3 = row.createCell(3);
					if (record.getR61_AVERAGE() != null) {
						cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row62
					row = sheet.getRow(61);
// Column C
					cell2 = row.createCell(2);
					if (record.getR62_MONTH_END() != null) {
						cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row62
// Column D
					cell3 = row.createCell(3);
					if (record.getR62_AVERAGE() != null) {
						cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row64
					row = sheet.getRow(63);
// Column C
					cell2 = row.createCell(2);
					if (record.getR64_MONTH_END() != null) {
						cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row64
// Column D
					cell3 = row.createCell(3);
					if (record.getR64_AVERAGE() != null) {
						cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row66
					row = sheet.getRow(65);
// Column C
					cell2 = row.createCell(2);
					if (record.getR66_MONTH_END() != null) {
						cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row66
// Column D
					cell3 = row.createCell(3);
					if (record.getR66_AVERAGE() != null) {
						cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row67
					row = sheet.getRow(66);
// Column C
					cell2 = row.createCell(2);
					if (record.getR67_MONTH_END() != null) {
						cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row67
// Column D
					cell3 = row.createCell(3);
					if (record.getR67_AVERAGE() != null) {
						cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row69
					row = sheet.getRow(68);
// Column C
					cell2 = row.createCell(2);
					if (record.getR69_MONTH_END() != null) {
						cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row69
// Column D
					cell3 = row.createCell(3);
					if (record.getR69_AVERAGE() != null) {
						cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row70
					row = sheet.getRow(69);
// Column C
					cell2 = row.createCell(2);
					if (record.getR70_MONTH_END() != null) {
						cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row70
// Column D
					cell3 = row.createCell(3);
					if (record.getR70_AVERAGE() != null) {
						cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row71
					row = sheet.getRow(70);
// Column C
					cell2 = row.createCell(2);
					if (record.getR71_MONTH_END() != null) {
						cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row71
// Column D
					cell3 = row.createCell(3);
					if (record.getR71_AVERAGE() != null) {
						cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(71);
// row72
// Column D
					cell3 = row.createCell(3);
					if (record.getR72_AVERAGE() != null) {
						cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row74
					row = sheet.getRow(73);
// Column C
					cell2 = row.createCell(2);
					if (record.getR74_MONTH_END() != null) {
						cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row74
// Column D
					cell3 = row.createCell(3);
					if (record.getR74_AVERAGE() != null) {
						cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row75
					row = sheet.getRow(74);
// Column C
					cell2 = row.createCell(2);
					if (record.getR75_MONTH_END() != null) {
						cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row75
// Column D
					cell3 = row.createCell(3);
					if (record.getR75_AVERAGE() != null) {
						cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row76
					row = sheet.getRow(75);
// Column C
					cell2 = row.createCell(2);
					if (record.getR76_MONTH_END() != null) {
						cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row76
// Column D
					cell3 = row.createCell(3);
					if (record.getR76_AVERAGE() != null) {
						cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row77
					row = sheet.getRow(76);
// Column C
					cell2 = row.createCell(2);
					if (record.getR77_MONTH_END() != null) {
						cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row77
// Column D
					cell3 = row.createCell(3);
					if (record.getR77_AVERAGE() != null) {
						cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row78
					row = sheet.getRow(77);
// Column C
					cell2 = row.createCell(2);
					if (record.getR78_MONTH_END() != null) {
						cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row78
// Column D
					cell3 = row.createCell(3);
					if (record.getR78_AVERAGE() != null) {
						cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row79
					row = sheet.getRow(78);
// Column C
					cell2 = row.createCell(2);
					if (record.getR79_MONTH_END() != null) {
						cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row79
// Column D
					cell3 = row.createCell(3);
					if (record.getR79_AVERAGE() != null) {
						cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SFINP2 EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_SFINP2_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}


// ------------------------------
// Resub Format excel
// ------------------------------
	public byte[] BRRS_M_SFINP2ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
// Redirecting to Resub Excel
				return BRRS_M_SFINP2EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SFINP2_RESUB_Summary_Entity> dataList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP2_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version },
				new BeanPropertyRowMapper<>(M_SFINP2_RESUB_Summary_Entity.class));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SFINP2 report. Returning empty result.");
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

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_SFINP2_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

// row11
// Column C
					row = sheet.getRow(10);
					Cell cell2 = row.createCell(2);
					if (record.getR11_MONTH_END() != null) {
						cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row11
// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR11_AVERAGE() != null) {
						cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row12
					row = sheet.getRow(11);
// Column C
					cell2 = row.createCell(2);
					if (record.getR12_MONTH_END() != null) {
						cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row12
// Column D
					cell3 = row.createCell(3);
					if (record.getR12_AVERAGE() != null) {
						cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row13
					row = sheet.getRow(12);
// Column C
					cell2 = row.createCell(2);
					if (record.getR13_MONTH_END() != null) {
						cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row13
// Column D
					cell3 = row.createCell(3);
					if (record.getR13_AVERAGE() != null) {
						cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row14
					row = sheet.getRow(13);
// Column C
					cell2 = row.createCell(2);
					if (record.getR14_MONTH_END() != null) {
						cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row14
// Column D
					cell3 = row.createCell(3);
					if (record.getR14_AVERAGE() != null) {
						cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row15
					row = sheet.getRow(14);
// Column C
					cell2 = row.createCell(2);
					if (record.getR15_MONTH_END() != null) {
						cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row15
// Column D
					cell3 = row.createCell(3);
					if (record.getR15_AVERAGE() != null) {
						cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row16
					row = sheet.getRow(15);
// Column C
					cell2 = row.createCell(2);
					if (record.getR16_MONTH_END() != null) {
						cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row16
// Column D
					cell3 = row.createCell(3);
					if (record.getR16_AVERAGE() != null) {
						cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row19
					row = sheet.getRow(18);
// Column C
					cell2 = row.createCell(2);
					if (record.getR19_MONTH_END() != null) {
						cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row20
					row = sheet.getRow(19);
// Column C
					cell2 = row.createCell(2);
					if (record.getR20_MONTH_END() != null) {
						cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row20
// Column D
					cell3 = row.createCell(3);
					if (record.getR20_AVERAGE() != null) {
						cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row21
					row = sheet.getRow(20);
// Column C
					cell2 = row.createCell(2);
					if (record.getR21_MONTH_END() != null) {
						cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row21
// Column D
					cell3 = row.createCell(3);
					if (record.getR21_AVERAGE() != null) {
						cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row23
					row = sheet.getRow(22);
// Column C
					cell2 = row.createCell(2);
					if (record.getR23_MONTH_END() != null) {
						cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row23
// Column D
					cell3 = row.createCell(3);
					if (record.getR23_AVERAGE() != null) {
						cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row24
					row = sheet.getRow(23);
// Column C
					cell2 = row.createCell(2);
					if (record.getR24_MONTH_END() != null) {
						cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row24
// Column D
					cell3 = row.createCell(3);
					if (record.getR24_AVERAGE() != null) {
						cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row27
					row = sheet.getRow(26);
// Column C
					cell2 = row.createCell(2);
					if (record.getR27_MONTH_END() != null) {
						cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row27
// Column D
					cell3 = row.createCell(3);
					if (record.getR27_AVERAGE() != null) {
						cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row28
					row = sheet.getRow(27);
// Column C
					cell2 = row.createCell(2);
					if (record.getR28_MONTH_END() != null) {
						cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row28
// Column D
					cell3 = row.createCell(3);
					if (record.getR28_AVERAGE() != null) {
						cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
// row29
// Column D
					cell3 = row.createCell(3);
					if (record.getR29_AVERAGE() != null) {
						cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row30
					row = sheet.getRow(29);
// Column C
					cell2 = row.createCell(2);
					if (record.getR30_MONTH_END() != null) {
						cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row30
// Column D
					cell3 = row.createCell(3);
					if (record.getR30_AVERAGE() != null) {
						cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row31
					row = sheet.getRow(30);
// Column C
					cell2 = row.createCell(2);
					if (record.getR31_MONTH_END() != null) {
						cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row31
// Column D
					cell3 = row.createCell(3);
					if (record.getR31_AVERAGE() != null) {
						cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row34
					row = sheet.getRow(33);
// Column C
					cell2 = row.createCell(2);
					if (record.getR34_MONTH_END() != null) {
						cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row34
// Column D
					cell3 = row.createCell(3);
					if (record.getR34_AVERAGE() != null) {
						cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row35
					row = sheet.getRow(34);
// Column C
					cell2 = row.createCell(2);
					if (record.getR35_MONTH_END() != null) {
						cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row35
// Column D
					cell3 = row.createCell(3);
					if (record.getR35_AVERAGE() != null) {
						cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row36
					row = sheet.getRow(35);
// Column C
					cell2 = row.createCell(2);
					if (record.getR36_MONTH_END() != null) {
						cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row36
// Column D
					cell3 = row.createCell(3);
					if (record.getR36_AVERAGE() != null) {
						cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row37
					row = sheet.getRow(36);
// Column C
					cell2 = row.createCell(2);
					if (record.getR37_MONTH_END() != null) {
						cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row37
// Column D
					cell3 = row.createCell(3);
					if (record.getR37_AVERAGE() != null) {
						cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row38
					row = sheet.getRow(37);
// Column C
					cell2 = row.createCell(2);
					if (record.getR38_MONTH_END() != null) {
						cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row38
// Column D
					cell3 = row.createCell(3);
					if (record.getR38_AVERAGE() != null) {
						cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row39
					row = sheet.getRow(38);
// Column C
					cell2 = row.createCell(2);
					if (record.getR39_MONTH_END() != null) {
						cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row39
// Column D
					cell3 = row.createCell(3);
					if (record.getR39_AVERAGE() != null) {
						cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row40
					row = sheet.getRow(39);
// Column C
					cell2 = row.createCell(2);
					if (record.getR40_MONTH_END() != null) {
						cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row40
// Column D
					cell3 = row.createCell(3);
					if (record.getR40_AVERAGE() != null) {
						cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row42
					row = sheet.getRow(41);
// Column C
					cell2 = row.createCell(2);
					if (record.getR42_MONTH_END() != null) {
						cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row42
// Column D
					cell3 = row.createCell(3);
					if (record.getR42_AVERAGE() != null) {
						cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row43
					row = sheet.getRow(42);
// Column C
					cell2 = row.createCell(2);
					if (record.getR43_MONTH_END() != null) {
						cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row43
// Column D
					cell3 = row.createCell(3);
					if (record.getR43_AVERAGE() != null) {
						cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row44
					row = sheet.getRow(43);
// Column C
					cell2 = row.createCell(2);
					if (record.getR44_MONTH_END() != null) {
						cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row44
// Column D
					cell3 = row.createCell(3);
					if (record.getR44_AVERAGE() != null) {
						cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row45
					row = sheet.getRow(44);
// Column C
					cell2 = row.createCell(2);
					if (record.getR45_MONTH_END() != null) {
						cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row45
// Column D
					cell3 = row.createCell(3);
					if (record.getR45_AVERAGE() != null) {
						cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row47
					row = sheet.getRow(46);
// Column C
					cell2 = row.createCell(2);
					if (record.getR47_MONTH_END() != null) {
						cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row47
// Column D
					cell3 = row.createCell(3);
					if (record.getR47_AVERAGE() != null) {
						cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row48
					row = sheet.getRow(47);
// Column C
					cell2 = row.createCell(2);
					if (record.getR48_MONTH_END() != null) {
						cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row48
// Column D
					cell3 = row.createCell(3);
					if (record.getR48_AVERAGE() != null) {
						cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row49
					row = sheet.getRow(48);
// Column C
					cell2 = row.createCell(2);
					if (record.getR49_MONTH_END() != null) {
						cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row49
// Column D
					cell3 = row.createCell(3);
					if (record.getR49_AVERAGE() != null) {
						cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row51
					row = sheet.getRow(50);
// Column C
					cell2 = row.createCell(2);
					if (record.getR51_MONTH_END() != null) {
						cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row51
// Column D
					cell3 = row.createCell(3);
					if (record.getR51_AVERAGE() != null) {
						cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row52
					row = sheet.getRow(51);
// Column C
					cell2 = row.createCell(2);
					if (record.getR52_MONTH_END() != null) {
						cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row52
// Column D
					cell3 = row.createCell(3);
					if (record.getR52_AVERAGE() != null) {
						cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row53
					row = sheet.getRow(52);
// Column C
					cell2 = row.createCell(2);
					if (record.getR53_MONTH_END() != null) {
						cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row53
// Column D
					cell3 = row.createCell(3);
					if (record.getR53_AVERAGE() != null) {
						cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row54
					row = sheet.getRow(53);
// Column C
					cell2 = row.createCell(2);
					if (record.getR54_MONTH_END() != null) {
						cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row54
// Column D
					cell3 = row.createCell(3);
					if (record.getR54_AVERAGE() != null) {
						cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row55
					row = sheet.getRow(54);
// Column C
					cell2 = row.createCell(2);
					if (record.getR55_MONTH_END() != null) {
						cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row55
// Column D
					cell3 = row.createCell(3);
					if (record.getR55_AVERAGE() != null) {
						cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row56
					row = sheet.getRow(55);
// Column C
					cell2 = row.createCell(2);
					if (record.getR56_MONTH_END() != null) {
						cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row56
// Column D
					cell3 = row.createCell(3);
					if (record.getR56_AVERAGE() != null) {
						cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row57
					row = sheet.getRow(56);
// Column C
					cell2 = row.createCell(2);
					if (record.getR57_MONTH_END() != null) {
						cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row57
// Column D
					cell3 = row.createCell(3);
					if (record.getR57_AVERAGE() != null) {
						cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row58
					row = sheet.getRow(57);
// Column C
					cell2 = row.createCell(2);
					if (record.getR58_MONTH_END() != null) {
						cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row58
// Column D
					cell3 = row.createCell(3);
					if (record.getR58_AVERAGE() != null) {
						cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row60
					row = sheet.getRow(59);
// Column C
					cell2 = row.createCell(2);
					if (record.getR60_MONTH_END() != null) {
						cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row60
// Column D
					cell3 = row.createCell(3);
					if (record.getR60_AVERAGE() != null) {
						cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row61
					row = sheet.getRow(60);
// Column C
					cell2 = row.createCell(2);
					if (record.getR61_MONTH_END() != null) {
						cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row61
// Column D
					cell3 = row.createCell(3);
					if (record.getR61_AVERAGE() != null) {
						cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row62
					row = sheet.getRow(61);
// Column C
					cell2 = row.createCell(2);
					if (record.getR62_MONTH_END() != null) {
						cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row62
// Column D
					cell3 = row.createCell(3);
					if (record.getR62_AVERAGE() != null) {
						cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row64
					row = sheet.getRow(63);
// Column C
					cell2 = row.createCell(2);
					if (record.getR64_MONTH_END() != null) {
						cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row64
// Column D
					cell3 = row.createCell(3);
					if (record.getR64_AVERAGE() != null) {
						cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row66
					row = sheet.getRow(65);
// Column C
					cell2 = row.createCell(2);
					if (record.getR66_MONTH_END() != null) {
						cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row66
// Column D
					cell3 = row.createCell(3);
					if (record.getR66_AVERAGE() != null) {
						cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row67
					row = sheet.getRow(66);
// Column C
					cell2 = row.createCell(2);
					if (record.getR67_MONTH_END() != null) {
						cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row67
// Column D
					cell3 = row.createCell(3);
					if (record.getR67_AVERAGE() != null) {
						cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row69
					row = sheet.getRow(68);
// Column C
					cell2 = row.createCell(2);
					if (record.getR69_MONTH_END() != null) {
						cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row69
// Column D
					cell3 = row.createCell(3);
					if (record.getR69_AVERAGE() != null) {
						cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row70
					row = sheet.getRow(69);
// Column C
					cell2 = row.createCell(2);
					if (record.getR70_MONTH_END() != null) {
						cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row70
// Column D
					cell3 = row.createCell(3);
					if (record.getR70_AVERAGE() != null) {
						cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row71
					row = sheet.getRow(70);
// Column C
					cell2 = row.createCell(2);
					if (record.getR71_MONTH_END() != null) {
						cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row71
// Column D
					cell3 = row.createCell(3);
					if (record.getR71_AVERAGE() != null) {
						cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(71);
// row72
// Column D
					cell3 = row.createCell(3);
					if (record.getR72_AVERAGE() != null) {
						cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row74
					row = sheet.getRow(73);
// Column C
					cell2 = row.createCell(2);
					if (record.getR74_MONTH_END() != null) {
						cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row74
// Column D
					cell3 = row.createCell(3);
					if (record.getR74_AVERAGE() != null) {
						cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row75
					row = sheet.getRow(74);
// Column C
					cell2 = row.createCell(2);
					if (record.getR75_MONTH_END() != null) {
						cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row75
// Column D
					cell3 = row.createCell(3);
					if (record.getR75_AVERAGE() != null) {
						cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row76
					row = sheet.getRow(75);
// Column C
					cell2 = row.createCell(2);
					if (record.getR76_MONTH_END() != null) {
						cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row76
// Column D
					cell3 = row.createCell(3);
					if (record.getR76_AVERAGE() != null) {
						cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row77
					row = sheet.getRow(76);
// Column C
					cell2 = row.createCell(2);
					if (record.getR77_MONTH_END() != null) {
						cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row77
// Column D
					cell3 = row.createCell(3);
					if (record.getR77_AVERAGE() != null) {
						cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row78
					row = sheet.getRow(77);
// Column C
					cell2 = row.createCell(2);
					if (record.getR78_MONTH_END() != null) {
						cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row78
// Column D
					cell3 = row.createCell(3);
					if (record.getR78_AVERAGE() != null) {
						cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row79
					row = sheet.getRow(78);
// Column C
					cell2 = row.createCell(2);
					if (record.getR79_MONTH_END() != null) {
						cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row79
// Column D
					cell3 = row.createCell(3);
					if (record.getR79_AVERAGE() != null) {
						cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SFINP2 RESUB SUMMARY", null,
						"BRRS_M_SFINP2_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

// ------------------------------
// Resub Email Excel
// ------------------------------
	public byte[] BRRS_M_SFINP2EmailResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_SFINP2_RESUB_Summary_Entity> dataList = jdbcTemplate.query(
				"SELECT * FROM BRRS_M_SFINP2_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				new Object[] { dateformat.parse(todate), version },
				new BeanPropertyRowMapper<>(M_SFINP2_RESUB_Summary_Entity.class));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SFINP2 report. Returning empty result.");
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

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SFINP2_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

//row11--------------->23. Current deposits

// Column C
					row = sheet.getRow(10);
					Cell cell2 = row.createCell(2);
					if (record.getR11_MONTH_END() != null) {
						cell2.setCellValue(record.getR11_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row11
// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR11_AVERAGE() != null) {
						cell3.setCellValue(record.getR11_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row12------------------>24. Call deposits

					row = sheet.getRow(11);
// Column C
					cell2 = row.createCell(2);
					if (record.getR12_MONTH_END() != null) {
						cell2.setCellValue(record.getR12_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row12
// Column D
					cell3 = row.createCell(3);
					if (record.getR12_AVERAGE() != null) {
						cell3.setCellValue(record.getR12_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row13------------->25. Savings deposits

					row = sheet.getRow(12);
// Column C
					cell2 = row.createCell(2);
					if (record.getR13_MONTH_END() != null) {
						cell2.setCellValue(record.getR13_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row13
// Column D
					cell3 = row.createCell(3);
					if (record.getR13_AVERAGE() != null) {
						cell3.setCellValue(record.getR13_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row14------------26. Notice deposits

					row = sheet.getRow(13);
// Column C
					cell2 = row.createCell(2);
					if (record.getR14_MONTH_END() != null) {
						cell2.setCellValue(record.getR14_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row14
// Column D
					cell3 = row.createCell(3);
					if (record.getR14_AVERAGE() != null) {
						cell3.setCellValue(record.getR14_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row15----------->27. Fixed deposits

					row = sheet.getRow(14);
// Column C
					cell2 = row.createCell(2);
					if (record.getR15_MONTH_END() != null) {
						cell2.setCellValue(record.getR15_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row15
// Column D
					cell3 = row.createCell(3);
					if (record.getR15_AVERAGE() != null) {
						cell3.setCellValue(record.getR15_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row16---------------28. Certificates of deposit 

					row = sheet.getRow(15);
// Column C
					cell2 = row.createCell(2);
					if (record.getR16_MONTH_END() != null) {
						cell2.setCellValue(record.getR16_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row16
// Column D
					cell3 = row.createCell(3);
					if (record.getR16_AVERAGE() != null) {
						cell3.setCellValue(record.getR16_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row19-------------(a) Affiliated

					row = sheet.getRow(18);
// Column C
					cell2 = row.createCell(2);
					if (record.getR19_MONTH_END() != null) {
						cell2.setCellValue(record.getR19_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row20
					row = sheet.getRow(19);
// Column C
					cell2 = row.createCell(2);
					if (record.getR20_MONTH_END() != null) {
						cell2.setCellValue(record.getR20_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row20
// Column D
					cell3 = row.createCell(3);
					if (record.getR20_AVERAGE() != null) {
						cell3.setCellValue(record.getR20_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row21
					row = sheet.getRow(20);
// Column C
					cell2 = row.createCell(2);
					if (record.getR21_MONTH_END() != null) {
						cell2.setCellValue(record.getR21_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row21
// Column D
					cell3 = row.createCell(3);
					if (record.getR21_AVERAGE() != null) {
						cell3.setCellValue(record.getR21_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row23
					row = sheet.getRow(22);
// Column C
					cell2 = row.createCell(2);
					if (record.getR23_MONTH_END() != null) {
						cell2.setCellValue(record.getR23_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row23
// Column D
					cell3 = row.createCell(3);
					if (record.getR23_AVERAGE() != null) {
						cell3.setCellValue(record.getR23_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row24
					row = sheet.getRow(23);
// Column C
					cell2 = row.createCell(2);
					if (record.getR24_MONTH_END() != null) {
						cell2.setCellValue(record.getR24_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row24
// Column D
					cell3 = row.createCell(3);
					if (record.getR24_AVERAGE() != null) {
						cell3.setCellValue(record.getR24_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row27
					row = sheet.getRow(26);
// Column C
					cell2 = row.createCell(2);
					if (record.getR27_MONTH_END() != null) {
						cell2.setCellValue(record.getR27_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row27
// Column D
					cell3 = row.createCell(3);
					if (record.getR27_AVERAGE() != null) {
						cell3.setCellValue(record.getR27_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row28
					row = sheet.getRow(27);
// Column C
					cell2 = row.createCell(2);
					if (record.getR28_MONTH_END() != null) {
						cell2.setCellValue(record.getR28_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row28
// Column D
					cell3 = row.createCell(3);
					if (record.getR28_AVERAGE() != null) {
						cell3.setCellValue(record.getR28_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
// row29
// Column D
					cell3 = row.createCell(3);
					if (record.getR29_AVERAGE() != null) {
						cell3.setCellValue(record.getR29_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row30
					row = sheet.getRow(29);
// Column C
					cell2 = row.createCell(2);
					if (record.getR30_MONTH_END() != null) {
						cell2.setCellValue(record.getR30_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row30
// Column D
					cell3 = row.createCell(3);
					if (record.getR30_AVERAGE() != null) {
						cell3.setCellValue(record.getR30_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row31
					row = sheet.getRow(30);
// Column C
					cell2 = row.createCell(2);
					if (record.getR31_MONTH_END() != null) {
						cell2.setCellValue(record.getR31_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row31
// Column D
					cell3 = row.createCell(3);
					if (record.getR31_AVERAGE() != null) {
						cell3.setCellValue(record.getR31_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row34
					row = sheet.getRow(33);
// Column C
					cell2 = row.createCell(2);
					if (record.getR34_MONTH_END() != null) {
						cell2.setCellValue(record.getR34_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row34
// Column D
					cell3 = row.createCell(3);
					if (record.getR34_AVERAGE() != null) {
						cell3.setCellValue(record.getR34_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row35
					row = sheet.getRow(34);
// Column C
					cell2 = row.createCell(2);
					if (record.getR35_MONTH_END() != null) {
						cell2.setCellValue(record.getR35_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row35
// Column D
					cell3 = row.createCell(3);
					if (record.getR35_AVERAGE() != null) {
						cell3.setCellValue(record.getR35_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row36
					row = sheet.getRow(35);
// Column C
					cell2 = row.createCell(2);
					if (record.getR36_MONTH_END() != null) {
						cell2.setCellValue(record.getR36_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row36
// Column D
					cell3 = row.createCell(3);
					if (record.getR36_AVERAGE() != null) {
						cell3.setCellValue(record.getR36_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row37
					row = sheet.getRow(36);
// Column C
					cell2 = row.createCell(2);
					if (record.getR37_MONTH_END() != null) {
						cell2.setCellValue(record.getR37_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row37
// Column D
					cell3 = row.createCell(3);
					if (record.getR37_AVERAGE() != null) {
						cell3.setCellValue(record.getR37_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row38
					row = sheet.getRow(37);
// Column C
					cell2 = row.createCell(2);
					if (record.getR38_MONTH_END() != null) {
						cell2.setCellValue(record.getR38_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row38
// Column D
					cell3 = row.createCell(3);
					if (record.getR38_AVERAGE() != null) {
						cell3.setCellValue(record.getR38_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row39
					row = sheet.getRow(38);
// Column C
					cell2 = row.createCell(2);
					if (record.getR39_MONTH_END() != null) {
						cell2.setCellValue(record.getR39_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row39
// Column D
					cell3 = row.createCell(3);
					if (record.getR39_AVERAGE() != null) {
						cell3.setCellValue(record.getR39_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row40
					row = sheet.getRow(39);
// Column C
					cell2 = row.createCell(2);
					if (record.getR40_MONTH_END() != null) {
						cell2.setCellValue(record.getR40_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row40
// Column D
					cell3 = row.createCell(3);
					if (record.getR40_AVERAGE() != null) {
						cell3.setCellValue(record.getR40_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row42
					row = sheet.getRow(41);
// Column C
					cell2 = row.createCell(2);
					if (record.getR42_MONTH_END() != null) {
						cell2.setCellValue(record.getR42_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row42
// Column D
					cell3 = row.createCell(3);
					if (record.getR42_AVERAGE() != null) {
						cell3.setCellValue(record.getR42_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row43
					row = sheet.getRow(42);
// Column C
					cell2 = row.createCell(2);
					if (record.getR43_MONTH_END() != null) {
						cell2.setCellValue(record.getR43_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row43
// Column D
					cell3 = row.createCell(3);
					if (record.getR43_AVERAGE() != null) {
						cell3.setCellValue(record.getR43_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row44
					row = sheet.getRow(43);
// Column C
					cell2 = row.createCell(2);
					if (record.getR44_MONTH_END() != null) {
						cell2.setCellValue(record.getR44_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row44
// Column D
					cell3 = row.createCell(3);
					if (record.getR44_AVERAGE() != null) {
						cell3.setCellValue(record.getR44_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
// row45
					row = sheet.getRow(44);
// Column C
					cell2 = row.createCell(2);
					if (record.getR45_MONTH_END() != null) {
						cell2.setCellValue(record.getR45_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row45
// Column D
					cell3 = row.createCell(3);
					if (record.getR45_AVERAGE() != null) {
						cell3.setCellValue(record.getR45_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row47
					row = sheet.getRow(46);
// Column C
					cell2 = row.createCell(2);
					if (record.getR47_MONTH_END() != null) {
						cell2.setCellValue(record.getR47_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row47
// Column D
					cell3 = row.createCell(3);
					if (record.getR47_AVERAGE() != null) {
						cell3.setCellValue(record.getR47_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row48
					row = sheet.getRow(47);
// Column C
					cell2 = row.createCell(2);
					if (record.getR48_MONTH_END() != null) {
						cell2.setCellValue(record.getR48_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row48
// Column D
					cell3 = row.createCell(3);
					if (record.getR48_AVERAGE() != null) {
						cell3.setCellValue(record.getR48_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row49
					row = sheet.getRow(48);
// Column C
					cell2 = row.createCell(2);
					if (record.getR49_MONTH_END() != null) {
						cell2.setCellValue(record.getR49_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row49
// Column D
					cell3 = row.createCell(3);
					if (record.getR49_AVERAGE() != null) {
						cell3.setCellValue(record.getR49_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row51
					row = sheet.getRow(50);
// Column C
					cell2 = row.createCell(2);
					if (record.getR51_MONTH_END() != null) {
						cell2.setCellValue(record.getR51_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row51
// Column D
					cell3 = row.createCell(3);
					if (record.getR51_AVERAGE() != null) {
						cell3.setCellValue(record.getR51_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row52
					row = sheet.getRow(51);
// Column C
					cell2 = row.createCell(2);
					if (record.getR52_MONTH_END() != null) {
						cell2.setCellValue(record.getR52_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row52
// Column D
					cell3 = row.createCell(3);
					if (record.getR52_AVERAGE() != null) {
						cell3.setCellValue(record.getR52_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row53
					row = sheet.getRow(52);
// Column C
					cell2 = row.createCell(2);
					if (record.getR53_MONTH_END() != null) {
						cell2.setCellValue(record.getR53_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row53
// Column D
					cell3 = row.createCell(3);
					if (record.getR53_AVERAGE() != null) {
						cell3.setCellValue(record.getR53_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row54
					row = sheet.getRow(53);
// Column C
					cell2 = row.createCell(2);
					if (record.getR54_MONTH_END() != null) {
						cell2.setCellValue(record.getR54_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row54
// Column D
					cell3 = row.createCell(3);
					if (record.getR54_AVERAGE() != null) {
						cell3.setCellValue(record.getR54_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row55
					row = sheet.getRow(54);
// Column C
					cell2 = row.createCell(2);
					if (record.getR55_MONTH_END() != null) {
						cell2.setCellValue(record.getR55_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row55
// Column D
					cell3 = row.createCell(3);
					if (record.getR55_AVERAGE() != null) {
						cell3.setCellValue(record.getR55_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row56
					row = sheet.getRow(55);
// Column C
					cell2 = row.createCell(2);
					if (record.getR56_MONTH_END() != null) {
						cell2.setCellValue(record.getR56_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row56
// Column D
					cell3 = row.createCell(3);
					if (record.getR56_AVERAGE() != null) {
						cell3.setCellValue(record.getR56_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row57
					row = sheet.getRow(56);
// Column C
					cell2 = row.createCell(2);
					if (record.getR57_MONTH_END() != null) {
						cell2.setCellValue(record.getR57_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row57
// Column D
					cell3 = row.createCell(3);
					if (record.getR57_AVERAGE() != null) {
						cell3.setCellValue(record.getR57_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row58
					row = sheet.getRow(57);
// Column C
					cell2 = row.createCell(2);
					if (record.getR58_MONTH_END() != null) {
						cell2.setCellValue(record.getR58_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row58
// Column D
					cell3 = row.createCell(3);
					if (record.getR58_AVERAGE() != null) {
						cell3.setCellValue(record.getR58_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row60
					row = sheet.getRow(59);
// Column C
					cell2 = row.createCell(2);
					if (record.getR60_MONTH_END() != null) {
						cell2.setCellValue(record.getR60_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row60
// Column D
					cell3 = row.createCell(3);
					if (record.getR60_AVERAGE() != null) {
						cell3.setCellValue(record.getR60_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row61
					row = sheet.getRow(60);
// Column C
					cell2 = row.createCell(2);
					if (record.getR61_MONTH_END() != null) {
						cell2.setCellValue(record.getR61_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row61
// Column D
					cell3 = row.createCell(3);
					if (record.getR61_AVERAGE() != null) {
						cell3.setCellValue(record.getR61_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row62
					row = sheet.getRow(61);
// Column C
					cell2 = row.createCell(2);
					if (record.getR62_MONTH_END() != null) {
						cell2.setCellValue(record.getR62_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row62
// Column D
					cell3 = row.createCell(3);
					if (record.getR62_AVERAGE() != null) {
						cell3.setCellValue(record.getR62_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row64
					row = sheet.getRow(63);
// Column C
					cell2 = row.createCell(2);
					if (record.getR64_MONTH_END() != null) {
						cell2.setCellValue(record.getR64_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row64
// Column D
					cell3 = row.createCell(3);
					if (record.getR64_AVERAGE() != null) {
						cell3.setCellValue(record.getR64_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row66
					row = sheet.getRow(65);
// Column C
					cell2 = row.createCell(2);
					if (record.getR66_MONTH_END() != null) {
						cell2.setCellValue(record.getR66_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row66
// Column D
					cell3 = row.createCell(3);
					if (record.getR66_AVERAGE() != null) {
						cell3.setCellValue(record.getR66_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row67
					row = sheet.getRow(66);
// Column C
					cell2 = row.createCell(2);
					if (record.getR67_MONTH_END() != null) {
						cell2.setCellValue(record.getR67_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row67
// Column D
					cell3 = row.createCell(3);
					if (record.getR67_AVERAGE() != null) {
						cell3.setCellValue(record.getR67_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row69
					row = sheet.getRow(68);
// Column C
					cell2 = row.createCell(2);
					if (record.getR69_MONTH_END() != null) {
						cell2.setCellValue(record.getR69_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row69
// Column D
					cell3 = row.createCell(3);
					if (record.getR69_AVERAGE() != null) {
						cell3.setCellValue(record.getR69_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row70
					row = sheet.getRow(69);
// Column C
					cell2 = row.createCell(2);
					if (record.getR70_MONTH_END() != null) {
						cell2.setCellValue(record.getR70_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row70
// Column D
					cell3 = row.createCell(3);
					if (record.getR70_AVERAGE() != null) {
						cell3.setCellValue(record.getR70_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row71
					row = sheet.getRow(70);
// Column C
					cell2 = row.createCell(2);
					if (record.getR71_MONTH_END() != null) {
						cell2.setCellValue(record.getR71_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row71
// Column D
					cell3 = row.createCell(3);
					if (record.getR71_AVERAGE() != null) {
						cell3.setCellValue(record.getR71_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(71);
// row72
// Column D
					cell3 = row.createCell(3);
					if (record.getR72_AVERAGE() != null) {
						cell3.setCellValue(record.getR72_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row74
					row = sheet.getRow(73);
// Column C
					cell2 = row.createCell(2);
					if (record.getR74_MONTH_END() != null) {
						cell2.setCellValue(record.getR74_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row74
// Column D
					cell3 = row.createCell(3);
					if (record.getR74_AVERAGE() != null) {
						cell3.setCellValue(record.getR74_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row75
					row = sheet.getRow(74);
// Column C
					cell2 = row.createCell(2);
					if (record.getR75_MONTH_END() != null) {
						cell2.setCellValue(record.getR75_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row75
// Column D
					cell3 = row.createCell(3);
					if (record.getR75_AVERAGE() != null) {
						cell3.setCellValue(record.getR75_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row76
					row = sheet.getRow(75);
// Column C
					cell2 = row.createCell(2);
					if (record.getR76_MONTH_END() != null) {
						cell2.setCellValue(record.getR76_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row76
// Column D
					cell3 = row.createCell(3);
					if (record.getR76_AVERAGE() != null) {
						cell3.setCellValue(record.getR76_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row77
					row = sheet.getRow(76);
// Column C
					cell2 = row.createCell(2);
					if (record.getR77_MONTH_END() != null) {
						cell2.setCellValue(record.getR77_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row77
// Column D
					cell3 = row.createCell(3);
					if (record.getR77_AVERAGE() != null) {
						cell3.setCellValue(record.getR77_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row78
					row = sheet.getRow(77);
// Column C
					cell2 = row.createCell(2);
					if (record.getR78_MONTH_END() != null) {
						cell2.setCellValue(record.getR78_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row78
// Column D
					cell3 = row.createCell(3);
					if (record.getR78_AVERAGE() != null) {
						cell3.setCellValue(record.getR78_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

// row79
					row = sheet.getRow(78);
// Column C
					cell2 = row.createCell(2);
					if (record.getR79_MONTH_END() != null) {
						cell2.setCellValue(record.getR79_MONTH_END().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

// row79
// Column D
					cell3 = row.createCell(3);
					if (record.getR79_AVERAGE() != null) {
						cell3.setCellValue(record.getR79_AVERAGE().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SFINP2 EMAIL RESUB SUMMARY", null,
						"BRRS_M_SFINP2_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}


	private static String getColumnName(java.lang.reflect.Field field) {
		if (field.isAnnotationPresent(javax.persistence.Column.class)) {
			javax.persistence.Column col = field.getAnnotation(javax.persistence.Column.class);
			return col.name();
		}
		return field.getName();
	}


	private void saveSummaryEntity(M_SFINP2_Summary_Entity entity) {
		String countSql = "SELECT COUNT(*) FROM BRRS_M_SFINP2_SUMMARYTABLE WHERE REPORT_DATE = ?";
		int count = jdbcTemplate.queryForObject(countSql, new Object[] { entity.getREPORT_DATE() }, Integer.class);

		if (count > 0) {
			StringBuilder sql = new StringBuilder("UPDATE BRRS_M_SFINP2_SUMMARYTABLE SET ");
			List<Object> params = new ArrayList<>();
			java.lang.reflect.Field[] fields = M_SFINP2_Summary_Entity.class.getDeclaredFields();
			boolean first = true;
			for (java.lang.reflect.Field field : fields) {
				if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				String name = field.getName();
				if ("REPORT_DATE".equalsIgnoreCase(name)) {
					continue;
				}
				if (!first) {
					sql.append(", ");
				}
				sql.append(name).append(" = ?");
				try {
					field.setAccessible(true);
					params.add(field.get(entity));
				} catch (Exception e) {
					params.add(null);
				}
				first = false;
			}
			sql.append(" WHERE REPORT_DATE = ?");
			params.add(entity.getREPORT_DATE());
			jdbcTemplate.update(sql.toString(), params.toArray());
		} else {
			StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_SFINP2_SUMMARYTABLE (");
			StringBuilder values = new StringBuilder(" VALUES (");
			List<Object> params = new ArrayList<>();
			java.lang.reflect.Field[] fields = M_SFINP2_Summary_Entity.class.getDeclaredFields();
			boolean first = true;
			for (java.lang.reflect.Field field : fields) {
				if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				String name = field.getName();
				if (!first) {
					sql.append(", ");
					values.append(", ");
				}
				sql.append(name);
				values.append("?");
				try {
					field.setAccessible(true);
					params.add(field.get(entity));
				} catch (Exception e) {
					params.add(null);
				}
				first = false;
			}
			sql.append(")").append(values).append(")");
			jdbcTemplate.update(sql.toString(), params.toArray());
		}
	}

	// ------------------------------
	// Helper method to save/update a detail record using jdbcTemplate and
	// reflection
	// ------------------------------

	private void saveDetailEntity(M_SFINP2_Detail_Entity entity) {
		String countSql = "SELECT COUNT(*) FROM BRRS_M_SFINP2_DETAILTABLE WHERE ACCT_NUMBER = ?";
		int count = jdbcTemplate.queryForObject(countSql, new Object[] { entity.getAcctNumber() }, Integer.class);

		if (count > 0) {
			StringBuilder sql = new StringBuilder("UPDATE BRRS_M_SFINP2_DETAILTABLE SET ");
			List<Object> params = new ArrayList<>();
			java.lang.reflect.Field[] fields = M_SFINP2_Detail_Entity.class.getDeclaredFields();
			boolean first = true;
			for (java.lang.reflect.Field field : fields) {
				if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				String colName = getColumnName(field);
				if ("ACCT_NUMBER".equalsIgnoreCase(colName)) {
					continue;
				}
				if (!first) {
					sql.append(", ");
				}
				sql.append(colName).append(" = ?");
				try {
					field.setAccessible(true);
					params.add(field.get(entity));
				} catch (Exception e) {
					params.add(null);
				}
				first = false;
			}
			sql.append(" WHERE ACCT_NUMBER = ?");
			params.add(entity.getAcctNumber());
			jdbcTemplate.update(sql.toString(), params.toArray());
		} else {
			StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_SFINP2_DETAILTABLE (");
			StringBuilder values = new StringBuilder(" VALUES (");
			List<Object> params = new ArrayList<>();
			java.lang.reflect.Field[] fields = M_SFINP2_Detail_Entity.class.getDeclaredFields();
			boolean first = true;
			for (java.lang.reflect.Field field : fields) {
				if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				String colName = getColumnName(field);
				if (!first) {
					sql.append(", ");
					values.append(", ");
				}
				sql.append(colName);
				values.append("?");
				try {
					field.setAccessible(true);
					params.add(field.get(entity));
				} catch (Exception e) {
					params.add(null);
				}
				first = false;
			}
			sql.append(")").append(values).append(")");
			jdbcTemplate.update(sql.toString(), params.toArray());
		}
	}

// ------------------------------
// M_SFINP2_Summary_Entity 
// ------------------------------
	public static class M_SFINP2_Summary_Entity {
		public String R10_PRODUCT;
		public String R10_CROSS_REFERENCE;
		public BigDecimal R10_MONTH_END;
		public BigDecimal R10_AVERAGE;
		public String R11_PRODUCT;
		public String R11_CROSS_REFERENCE;
		public BigDecimal R11_MONTH_END;
		public BigDecimal R11_AVERAGE;
		public String R12_PRODUCT;
		public String R12_CROSS_REFERENCE;
		public BigDecimal R12_MONTH_END;
		public BigDecimal R12_AVERAGE;
		public String R13_PRODUCT;
		public String R13_CROSS_REFERENCE;
		public BigDecimal R13_MONTH_END;
		public BigDecimal R13_AVERAGE;
		public String R14_PRODUCT;
		public String R14_CROSS_REFERENCE;
		public BigDecimal R14_MONTH_END;
		public BigDecimal R14_AVERAGE;
		public String R15_PRODUCT;
		public String R15_CROSS_REFERENCE;
		public BigDecimal R15_MONTH_END;
		public BigDecimal R15_AVERAGE;
		public String R16_PRODUCT;
		public String R16_CROSS_REFERENCE;
		public BigDecimal R16_MONTH_END;
		public BigDecimal R16_AVERAGE;
		public String R17_PRODUCT;
		public String R17_CROSS_REFERENCE;
		public BigDecimal R17_MONTH_END;
		public BigDecimal R17_AVERAGE;
		public String R18_PRODUCT;
		public String R18_CROSS_REFERENCE;
		public BigDecimal R18_MONTH_END;
		public BigDecimal R18_AVERAGE;
		public String R19_PRODUCT;
		public String R19_CROSS_REFERENCE;
		public BigDecimal R19_MONTH_END;
		public BigDecimal R19_AVERAGE;
		public String R20_PRODUCT;
		public String R20_CROSS_REFERENCE;
		public BigDecimal R20_MONTH_END;
		public BigDecimal R20_AVERAGE;
		public String R21_PRODUCT;
		public String R21_CROSS_REFERENCE;
		public BigDecimal R21_MONTH_END;
		public BigDecimal R21_AVERAGE;
		public String R22_PRODUCT;
		public String R22_CROSS_REFERENCE;
		public BigDecimal R22_MONTH_END;
		public BigDecimal R22_AVERAGE;
		public String R23_PRODUCT;
		public String R23_CROSS_REFERENCE;
		public BigDecimal R23_MONTH_END;
		public BigDecimal R23_AVERAGE;
		public String R24_PRODUCT;
		public String R24_CROSS_REFERENCE;
		public BigDecimal R24_MONTH_END;
		public BigDecimal R24_AVERAGE;
		public String R25_PRODUCT;
		public String R25_CROSS_REFERENCE;
		public BigDecimal R25_MONTH_END;
		public BigDecimal R25_AVERAGE;
		public String R26_PRODUCT;
		public String R26_CROSS_REFERENCE;
		public BigDecimal R26_MONTH_END;
		public BigDecimal R26_AVERAGE;
		public String R27_PRODUCT;
		public String R27_CROSS_REFERENCE;
		public BigDecimal R27_MONTH_END;
		public BigDecimal R27_AVERAGE;
		public String R28_PRODUCT;
		public String R28_CROSS_REFERENCE;
		public BigDecimal R28_MONTH_END;
		public BigDecimal R28_AVERAGE;
		public String R29_PRODUCT;
		public String R29_CROSS_REFERENCE;
		public BigDecimal R29_MONTH_END;
		public BigDecimal R29_AVERAGE;
		public String R30_PRODUCT;
		public String R30_CROSS_REFERENCE;
		public BigDecimal R30_MONTH_END;
		public BigDecimal R30_AVERAGE;
		public String R31_PRODUCT;
		public String R31_CROSS_REFERENCE;
		public BigDecimal R31_MONTH_END;
		public BigDecimal R31_AVERAGE;
		public String R32_PRODUCT;
		public String R32_CROSS_REFERENCE;
		public BigDecimal R32_MONTH_END;
		public BigDecimal R32_AVERAGE;
		public String R33_PRODUCT;
		public String R33_CROSS_REFERENCE;
		public BigDecimal R33_MONTH_END;
		public BigDecimal R33_AVERAGE;
		public String R34_PRODUCT;
		public String R34_CROSS_REFERENCE;
		public BigDecimal R34_MONTH_END;
		public BigDecimal R34_AVERAGE;
		public String R35_PRODUCT;
		public String R35_CROSS_REFERENCE;
		public BigDecimal R35_MONTH_END;
		public BigDecimal R35_AVERAGE;
		public String R36_PRODUCT;
		public String R36_CROSS_REFERENCE;
		public BigDecimal R36_MONTH_END;
		public BigDecimal R36_AVERAGE;
		public String R37_PRODUCT;
		public String R37_CROSS_REFERENCE;
		public BigDecimal R37_MONTH_END;
		public BigDecimal R37_AVERAGE;
		public String R38_PRODUCT;
		public String R38_CROSS_REFERENCE;
		public BigDecimal R38_MONTH_END;
		public BigDecimal R38_AVERAGE;
		public String R39_PRODUCT;
		public String R39_CROSS_REFERENCE;
		public BigDecimal R39_MONTH_END;
		public BigDecimal R39_AVERAGE;
		public String R40_PRODUCT;
		public String R40_CROSS_REFERENCE;
		public BigDecimal R40_MONTH_END;
		public BigDecimal R40_AVERAGE;
		public String R41_PRODUCT;
		public String R41_CROSS_REFERENCE;
		public BigDecimal R41_MONTH_END;
		public BigDecimal R41_AVERAGE;
		public String R42_PRODUCT;
		public String R42_CROSS_REFERENCE;
		public BigDecimal R42_MONTH_END;
		public BigDecimal R42_AVERAGE;
		public String R43_PRODUCT;
		public String R43_CROSS_REFERENCE;
		public BigDecimal R43_MONTH_END;
		public BigDecimal R43_AVERAGE;
		public String R44_PRODUCT;
		public String R44_CROSS_REFERENCE;
		public BigDecimal R44_MONTH_END;
		public BigDecimal R44_AVERAGE;
		public String R45_PRODUCT;
		public String R45_CROSS_REFERENCE;
		public BigDecimal R45_MONTH_END;
		public BigDecimal R45_AVERAGE;
		public String R46_PRODUCT;
		public String R46_CROSS_REFERENCE;
		public BigDecimal R46_MONTH_END;
		public BigDecimal R46_AVERAGE;
		public String R47_PRODUCT;
		public String R47_CROSS_REFERENCE;
		public BigDecimal R47_MONTH_END;
		public BigDecimal R47_AVERAGE;
		public String R48_PRODUCT;
		public String R48_CROSS_REFERENCE;
		public BigDecimal R48_MONTH_END;
		public BigDecimal R48_AVERAGE;
		public String R49_PRODUCT;
		public String R49_CROSS_REFERENCE;
		public BigDecimal R49_MONTH_END;
		public BigDecimal R49_AVERAGE;
		public String R50_PRODUCT;
		public String R50_CROSS_REFERENCE;
		public BigDecimal R50_MONTH_END;
		public BigDecimal R50_AVERAGE;
		public String R51_PRODUCT;
		public String R51_CROSS_REFERENCE;
		public BigDecimal R51_MONTH_END;
		public BigDecimal R51_AVERAGE;
		public String R52_PRODUCT;
		public String R52_CROSS_REFERENCE;
		public BigDecimal R52_MONTH_END;
		public BigDecimal R52_AVERAGE;
		public String R53_PRODUCT;
		public String R53_CROSS_REFERENCE;
		public BigDecimal R53_MONTH_END;
		public BigDecimal R53_AVERAGE;
		public String R54_PRODUCT;
		public String R54_CROSS_REFERENCE;
		public BigDecimal R54_MONTH_END;
		public BigDecimal R54_AVERAGE;
		public String R55_PRODUCT;
		public String R55_CROSS_REFERENCE;
		public BigDecimal R55_MONTH_END;
		public BigDecimal R55_AVERAGE;
		public String R56_PRODUCT;
		public String R56_CROSS_REFERENCE;
		public BigDecimal R56_MONTH_END;
		public BigDecimal R56_AVERAGE;
		public String R57_PRODUCT;
		public String R57_CROSS_REFERENCE;
		public BigDecimal R57_MONTH_END;
		public BigDecimal R57_AVERAGE;
		public String R58_PRODUCT;
		public String R58_CROSS_REFERENCE;
		public BigDecimal R58_MONTH_END;
		public BigDecimal R58_AVERAGE;
		public String R59_PRODUCT;
		public String R59_CROSS_REFERENCE;
		public BigDecimal R59_MONTH_END;
		public BigDecimal R59_AVERAGE;
		public String R60_PRODUCT;
		public String R60_CROSS_REFERENCE;
		public BigDecimal R60_MONTH_END;
		public BigDecimal R60_AVERAGE;
		public String R61_PRODUCT;
		public String R61_CROSS_REFERENCE;
		public BigDecimal R61_MONTH_END;
		public BigDecimal R61_AVERAGE;
		public String R62_PRODUCT;
		public String R62_CROSS_REFERENCE;
		public BigDecimal R62_MONTH_END;
		public BigDecimal R62_AVERAGE;
		public String R63_PRODUCT;
		public String R63_CROSS_REFERENCE;
		public BigDecimal R63_MONTH_END;
		public BigDecimal R63_AVERAGE;
		public String R64_PRODUCT;
		public String R64_CROSS_REFERENCE;
		public BigDecimal R64_MONTH_END;
		public BigDecimal R64_AVERAGE;
		public String R65_PRODUCT;
		public String R65_CROSS_REFERENCE;
		public BigDecimal R65_MONTH_END;
		public BigDecimal R65_AVERAGE;
		public String R66_PRODUCT;
		public String R66_CROSS_REFERENCE;
		public BigDecimal R66_MONTH_END;
		public BigDecimal R66_AVERAGE;
		public String R67_PRODUCT;
		public String R67_CROSS_REFERENCE;
		public BigDecimal R67_MONTH_END;
		public BigDecimal R67_AVERAGE;
		public String R68_PRODUCT;
		public String R68_CROSS_REFERENCE;
		public BigDecimal R68_MONTH_END;
		public BigDecimal R68_AVERAGE;
		public String R69_PRODUCT;
		public String R69_CROSS_REFERENCE;
		public BigDecimal R69_MONTH_END;
		public BigDecimal R69_AVERAGE;
		public String R70_PRODUCT;
		public String R70_CROSS_REFERENCE;
		public BigDecimal R70_MONTH_END;
		public BigDecimal R70_AVERAGE;
		public String R71_PRODUCT;
		public String R71_CROSS_REFERENCE;
		public BigDecimal R71_MONTH_END;
		public BigDecimal R71_AVERAGE;
		public String R72_PRODUCT;
		public String R72_CROSS_REFERENCE;
		public BigDecimal R72_MONTH_END;
		public BigDecimal R72_AVERAGE;
		public String R73_PRODUCT;
		public String R73_CROSS_REFERENCE;
		public BigDecimal R73_MONTH_END;
		public BigDecimal R73_AVERAGE;
		public String R74_PRODUCT;
		public String R74_CROSS_REFERENCE;
		public BigDecimal R74_MONTH_END;
		public BigDecimal R74_AVERAGE;
		public String R75_PRODUCT;
		public String R75_CROSS_REFERENCE;
		public BigDecimal R75_MONTH_END;
		public BigDecimal R75_AVERAGE;
		public String R76_PRODUCT;
		public String R76_CROSS_REFERENCE;
		public BigDecimal R76_MONTH_END;
		public BigDecimal R76_AVERAGE;
		public String R77_PRODUCT;
		public String R77_CROSS_REFERENCE;
		public BigDecimal R77_MONTH_END;
		public BigDecimal R77_AVERAGE;
		public String R78_PRODUCT;
		public String R78_CROSS_REFERENCE;
		public BigDecimal R78_MONTH_END;
		public BigDecimal R78_AVERAGE;
		public String R79_PRODUCT;
		public String R79_CROSS_REFERENCE;
		public BigDecimal R79_MONTH_END;
		public BigDecimal R79_AVERAGE;
		public String R80_PRODUCT;
		public String R80_CROSS_REFERENCE;
		public BigDecimal R80_MONTH_END;
		public BigDecimal R80_AVERAGE;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		public Date REPORT_DATE;
		public BigDecimal REPORT_VERSION;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public String getR10_CROSS_REFERENCE() {
			return R10_CROSS_REFERENCE;
		}

		public void setR10_CROSS_REFERENCE(String r10_CROSS_REFERENCE) {
			R10_CROSS_REFERENCE = r10_CROSS_REFERENCE;
		}

		public BigDecimal getR10_MONTH_END() {
			return R10_MONTH_END;
		}

		public void setR10_MONTH_END(BigDecimal r10_MONTH_END) {
			R10_MONTH_END = r10_MONTH_END;
		}

		public BigDecimal getR10_AVERAGE() {
			return R10_AVERAGE;
		}

		public void setR10_AVERAGE(BigDecimal r10_AVERAGE) {
			R10_AVERAGE = r10_AVERAGE;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public String getR11_CROSS_REFERENCE() {
			return R11_CROSS_REFERENCE;
		}

		public void setR11_CROSS_REFERENCE(String r11_CROSS_REFERENCE) {
			R11_CROSS_REFERENCE = r11_CROSS_REFERENCE;
		}

		public BigDecimal getR11_MONTH_END() {
			return R11_MONTH_END;
		}

		public void setR11_MONTH_END(BigDecimal r11_MONTH_END) {
			R11_MONTH_END = r11_MONTH_END;
		}

		public BigDecimal getR11_AVERAGE() {
			return R11_AVERAGE;
		}

		public void setR11_AVERAGE(BigDecimal r11_AVERAGE) {
			R11_AVERAGE = r11_AVERAGE;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public String getR12_CROSS_REFERENCE() {
			return R12_CROSS_REFERENCE;
		}

		public void setR12_CROSS_REFERENCE(String r12_CROSS_REFERENCE) {
			R12_CROSS_REFERENCE = r12_CROSS_REFERENCE;
		}

		public BigDecimal getR12_MONTH_END() {
			return R12_MONTH_END;
		}

		public void setR12_MONTH_END(BigDecimal r12_MONTH_END) {
			R12_MONTH_END = r12_MONTH_END;
		}

		public BigDecimal getR12_AVERAGE() {
			return R12_AVERAGE;
		}

		public void setR12_AVERAGE(BigDecimal r12_AVERAGE) {
			R12_AVERAGE = r12_AVERAGE;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public String getR13_CROSS_REFERENCE() {
			return R13_CROSS_REFERENCE;
		}

		public void setR13_CROSS_REFERENCE(String r13_CROSS_REFERENCE) {
			R13_CROSS_REFERENCE = r13_CROSS_REFERENCE;
		}

		public BigDecimal getR13_MONTH_END() {
			return R13_MONTH_END;
		}

		public void setR13_MONTH_END(BigDecimal r13_MONTH_END) {
			R13_MONTH_END = r13_MONTH_END;
		}

		public BigDecimal getR13_AVERAGE() {
			return R13_AVERAGE;
		}

		public void setR13_AVERAGE(BigDecimal r13_AVERAGE) {
			R13_AVERAGE = r13_AVERAGE;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public String getR14_CROSS_REFERENCE() {
			return R14_CROSS_REFERENCE;
		}

		public void setR14_CROSS_REFERENCE(String r14_CROSS_REFERENCE) {
			R14_CROSS_REFERENCE = r14_CROSS_REFERENCE;
		}

		public BigDecimal getR14_MONTH_END() {
			return R14_MONTH_END;
		}

		public void setR14_MONTH_END(BigDecimal r14_MONTH_END) {
			R14_MONTH_END = r14_MONTH_END;
		}

		public BigDecimal getR14_AVERAGE() {
			return R14_AVERAGE;
		}

		public void setR14_AVERAGE(BigDecimal r14_AVERAGE) {
			R14_AVERAGE = r14_AVERAGE;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public String getR15_CROSS_REFERENCE() {
			return R15_CROSS_REFERENCE;
		}

		public void setR15_CROSS_REFERENCE(String r15_CROSS_REFERENCE) {
			R15_CROSS_REFERENCE = r15_CROSS_REFERENCE;
		}

		public BigDecimal getR15_MONTH_END() {
			return R15_MONTH_END;
		}

		public void setR15_MONTH_END(BigDecimal r15_MONTH_END) {
			R15_MONTH_END = r15_MONTH_END;
		}

		public BigDecimal getR15_AVERAGE() {
			return R15_AVERAGE;
		}

		public void setR15_AVERAGE(BigDecimal r15_AVERAGE) {
			R15_AVERAGE = r15_AVERAGE;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public String getR16_CROSS_REFERENCE() {
			return R16_CROSS_REFERENCE;
		}

		public void setR16_CROSS_REFERENCE(String r16_CROSS_REFERENCE) {
			R16_CROSS_REFERENCE = r16_CROSS_REFERENCE;
		}

		public BigDecimal getR16_MONTH_END() {
			return R16_MONTH_END;
		}

		public void setR16_MONTH_END(BigDecimal r16_MONTH_END) {
			R16_MONTH_END = r16_MONTH_END;
		}

		public BigDecimal getR16_AVERAGE() {
			return R16_AVERAGE;
		}

		public void setR16_AVERAGE(BigDecimal r16_AVERAGE) {
			R16_AVERAGE = r16_AVERAGE;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public String getR17_CROSS_REFERENCE() {
			return R17_CROSS_REFERENCE;
		}

		public void setR17_CROSS_REFERENCE(String r17_CROSS_REFERENCE) {
			R17_CROSS_REFERENCE = r17_CROSS_REFERENCE;
		}

		public BigDecimal getR17_MONTH_END() {
			return R17_MONTH_END;
		}

		public void setR17_MONTH_END(BigDecimal r17_MONTH_END) {
			R17_MONTH_END = r17_MONTH_END;
		}

		public BigDecimal getR17_AVERAGE() {
			return R17_AVERAGE;
		}

		public void setR17_AVERAGE(BigDecimal r17_AVERAGE) {
			R17_AVERAGE = r17_AVERAGE;
		}

		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public String getR18_CROSS_REFERENCE() {
			return R18_CROSS_REFERENCE;
		}

		public void setR18_CROSS_REFERENCE(String r18_CROSS_REFERENCE) {
			R18_CROSS_REFERENCE = r18_CROSS_REFERENCE;
		}

		public BigDecimal getR18_MONTH_END() {
			return R18_MONTH_END;
		}

		public void setR18_MONTH_END(BigDecimal r18_MONTH_END) {
			R18_MONTH_END = r18_MONTH_END;
		}

		public BigDecimal getR18_AVERAGE() {
			return R18_AVERAGE;
		}

		public void setR18_AVERAGE(BigDecimal r18_AVERAGE) {
			R18_AVERAGE = r18_AVERAGE;
		}

		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public String getR19_CROSS_REFERENCE() {
			return R19_CROSS_REFERENCE;
		}

		public void setR19_CROSS_REFERENCE(String r19_CROSS_REFERENCE) {
			R19_CROSS_REFERENCE = r19_CROSS_REFERENCE;
		}

		public BigDecimal getR19_MONTH_END() {
			return R19_MONTH_END;
		}

		public void setR19_MONTH_END(BigDecimal r19_MONTH_END) {
			R19_MONTH_END = r19_MONTH_END;
		}

		public BigDecimal getR19_AVERAGE() {
			return R19_AVERAGE;
		}

		public void setR19_AVERAGE(BigDecimal r19_AVERAGE) {
			R19_AVERAGE = r19_AVERAGE;
		}

		public String getR20_PRODUCT() {
			return R20_PRODUCT;
		}

		public void setR20_PRODUCT(String r20_PRODUCT) {
			R20_PRODUCT = r20_PRODUCT;
		}

		public String getR20_CROSS_REFERENCE() {
			return R20_CROSS_REFERENCE;
		}

		public void setR20_CROSS_REFERENCE(String r20_CROSS_REFERENCE) {
			R20_CROSS_REFERENCE = r20_CROSS_REFERENCE;
		}

		public BigDecimal getR20_MONTH_END() {
			return R20_MONTH_END;
		}

		public void setR20_MONTH_END(BigDecimal r20_MONTH_END) {
			R20_MONTH_END = r20_MONTH_END;
		}

		public BigDecimal getR20_AVERAGE() {
			return R20_AVERAGE;
		}

		public void setR20_AVERAGE(BigDecimal r20_AVERAGE) {
			R20_AVERAGE = r20_AVERAGE;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			R21_PRODUCT = r21_PRODUCT;
		}

		public String getR21_CROSS_REFERENCE() {
			return R21_CROSS_REFERENCE;
		}

		public void setR21_CROSS_REFERENCE(String r21_CROSS_REFERENCE) {
			R21_CROSS_REFERENCE = r21_CROSS_REFERENCE;
		}

		public BigDecimal getR21_MONTH_END() {
			return R21_MONTH_END;
		}

		public void setR21_MONTH_END(BigDecimal r21_MONTH_END) {
			R21_MONTH_END = r21_MONTH_END;
		}

		public BigDecimal getR21_AVERAGE() {
			return R21_AVERAGE;
		}

		public void setR21_AVERAGE(BigDecimal r21_AVERAGE) {
			R21_AVERAGE = r21_AVERAGE;
		}

		public String getR22_PRODUCT() {
			return R22_PRODUCT;
		}

		public void setR22_PRODUCT(String r22_PRODUCT) {
			R22_PRODUCT = r22_PRODUCT;
		}

		public String getR22_CROSS_REFERENCE() {
			return R22_CROSS_REFERENCE;
		}

		public void setR22_CROSS_REFERENCE(String r22_CROSS_REFERENCE) {
			R22_CROSS_REFERENCE = r22_CROSS_REFERENCE;
		}

		public BigDecimal getR22_MONTH_END() {
			return R22_MONTH_END;
		}

		public void setR22_MONTH_END(BigDecimal r22_MONTH_END) {
			R22_MONTH_END = r22_MONTH_END;
		}

		public BigDecimal getR22_AVERAGE() {
			return R22_AVERAGE;
		}

		public void setR22_AVERAGE(BigDecimal r22_AVERAGE) {
			R22_AVERAGE = r22_AVERAGE;
		}

		public String getR23_PRODUCT() {
			return R23_PRODUCT;
		}

		public void setR23_PRODUCT(String r23_PRODUCT) {
			R23_PRODUCT = r23_PRODUCT;
		}

		public String getR23_CROSS_REFERENCE() {
			return R23_CROSS_REFERENCE;
		}

		public void setR23_CROSS_REFERENCE(String r23_CROSS_REFERENCE) {
			R23_CROSS_REFERENCE = r23_CROSS_REFERENCE;
		}

		public BigDecimal getR23_MONTH_END() {
			return R23_MONTH_END;
		}

		public void setR23_MONTH_END(BigDecimal r23_MONTH_END) {
			R23_MONTH_END = r23_MONTH_END;
		}

		public BigDecimal getR23_AVERAGE() {
			return R23_AVERAGE;
		}

		public void setR23_AVERAGE(BigDecimal r23_AVERAGE) {
			R23_AVERAGE = r23_AVERAGE;
		}

		public String getR24_PRODUCT() {
			return R24_PRODUCT;
		}

		public void setR24_PRODUCT(String r24_PRODUCT) {
			R24_PRODUCT = r24_PRODUCT;
		}

		public String getR24_CROSS_REFERENCE() {
			return R24_CROSS_REFERENCE;
		}

		public void setR24_CROSS_REFERENCE(String r24_CROSS_REFERENCE) {
			R24_CROSS_REFERENCE = r24_CROSS_REFERENCE;
		}

		public BigDecimal getR24_MONTH_END() {
			return R24_MONTH_END;
		}

		public void setR24_MONTH_END(BigDecimal r24_MONTH_END) {
			R24_MONTH_END = r24_MONTH_END;
		}

		public BigDecimal getR24_AVERAGE() {
			return R24_AVERAGE;
		}

		public void setR24_AVERAGE(BigDecimal r24_AVERAGE) {
			R24_AVERAGE = r24_AVERAGE;
		}

		public String getR25_PRODUCT() {
			return R25_PRODUCT;
		}

		public void setR25_PRODUCT(String r25_PRODUCT) {
			R25_PRODUCT = r25_PRODUCT;
		}

		public String getR25_CROSS_REFERENCE() {
			return R25_CROSS_REFERENCE;
		}

		public void setR25_CROSS_REFERENCE(String r25_CROSS_REFERENCE) {
			R25_CROSS_REFERENCE = r25_CROSS_REFERENCE;
		}

		public BigDecimal getR25_MONTH_END() {
			return R25_MONTH_END;
		}

		public void setR25_MONTH_END(BigDecimal r25_MONTH_END) {
			R25_MONTH_END = r25_MONTH_END;
		}

		public BigDecimal getR25_AVERAGE() {
			return R25_AVERAGE;
		}

		public void setR25_AVERAGE(BigDecimal r25_AVERAGE) {
			R25_AVERAGE = r25_AVERAGE;
		}

		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public String getR26_CROSS_REFERENCE() {
			return R26_CROSS_REFERENCE;
		}

		public void setR26_CROSS_REFERENCE(String r26_CROSS_REFERENCE) {
			R26_CROSS_REFERENCE = r26_CROSS_REFERENCE;
		}

		public BigDecimal getR26_MONTH_END() {
			return R26_MONTH_END;
		}

		public void setR26_MONTH_END(BigDecimal r26_MONTH_END) {
			R26_MONTH_END = r26_MONTH_END;
		}

		public BigDecimal getR26_AVERAGE() {
			return R26_AVERAGE;
		}

		public void setR26_AVERAGE(BigDecimal r26_AVERAGE) {
			R26_AVERAGE = r26_AVERAGE;
		}

		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public String getR27_CROSS_REFERENCE() {
			return R27_CROSS_REFERENCE;
		}

		public void setR27_CROSS_REFERENCE(String r27_CROSS_REFERENCE) {
			R27_CROSS_REFERENCE = r27_CROSS_REFERENCE;
		}

		public BigDecimal getR27_MONTH_END() {
			return R27_MONTH_END;
		}

		public void setR27_MONTH_END(BigDecimal r27_MONTH_END) {
			R27_MONTH_END = r27_MONTH_END;
		}

		public BigDecimal getR27_AVERAGE() {
			return R27_AVERAGE;
		}

		public void setR27_AVERAGE(BigDecimal r27_AVERAGE) {
			R27_AVERAGE = r27_AVERAGE;
		}

		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public String getR28_CROSS_REFERENCE() {
			return R28_CROSS_REFERENCE;
		}

		public void setR28_CROSS_REFERENCE(String r28_CROSS_REFERENCE) {
			R28_CROSS_REFERENCE = r28_CROSS_REFERENCE;
		}

		public BigDecimal getR28_MONTH_END() {
			return R28_MONTH_END;
		}

		public void setR28_MONTH_END(BigDecimal r28_MONTH_END) {
			R28_MONTH_END = r28_MONTH_END;
		}

		public BigDecimal getR28_AVERAGE() {
			return R28_AVERAGE;
		}

		public void setR28_AVERAGE(BigDecimal r28_AVERAGE) {
			R28_AVERAGE = r28_AVERAGE;
		}

		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public String getR29_CROSS_REFERENCE() {
			return R29_CROSS_REFERENCE;
		}

		public void setR29_CROSS_REFERENCE(String r29_CROSS_REFERENCE) {
			R29_CROSS_REFERENCE = r29_CROSS_REFERENCE;
		}

		public BigDecimal getR29_MONTH_END() {
			return R29_MONTH_END;
		}

		public void setR29_MONTH_END(BigDecimal r29_MONTH_END) {
			R29_MONTH_END = r29_MONTH_END;
		}

		public BigDecimal getR29_AVERAGE() {
			return R29_AVERAGE;
		}

		public void setR29_AVERAGE(BigDecimal r29_AVERAGE) {
			R29_AVERAGE = r29_AVERAGE;
		}

		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public String getR30_CROSS_REFERENCE() {
			return R30_CROSS_REFERENCE;
		}

		public void setR30_CROSS_REFERENCE(String r30_CROSS_REFERENCE) {
			R30_CROSS_REFERENCE = r30_CROSS_REFERENCE;
		}

		public BigDecimal getR30_MONTH_END() {
			return R30_MONTH_END;
		}

		public void setR30_MONTH_END(BigDecimal r30_MONTH_END) {
			R30_MONTH_END = r30_MONTH_END;
		}

		public BigDecimal getR30_AVERAGE() {
			return R30_AVERAGE;
		}

		public void setR30_AVERAGE(BigDecimal r30_AVERAGE) {
			R30_AVERAGE = r30_AVERAGE;
		}

		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public String getR31_CROSS_REFERENCE() {
			return R31_CROSS_REFERENCE;
		}

		public void setR31_CROSS_REFERENCE(String r31_CROSS_REFERENCE) {
			R31_CROSS_REFERENCE = r31_CROSS_REFERENCE;
		}

		public BigDecimal getR31_MONTH_END() {
			return R31_MONTH_END;
		}

		public void setR31_MONTH_END(BigDecimal r31_MONTH_END) {
			R31_MONTH_END = r31_MONTH_END;
		}

		public BigDecimal getR31_AVERAGE() {
			return R31_AVERAGE;
		}

		public void setR31_AVERAGE(BigDecimal r31_AVERAGE) {
			R31_AVERAGE = r31_AVERAGE;
		}

		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}

		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}

		public String getR32_CROSS_REFERENCE() {
			return R32_CROSS_REFERENCE;
		}

		public void setR32_CROSS_REFERENCE(String r32_CROSS_REFERENCE) {
			R32_CROSS_REFERENCE = r32_CROSS_REFERENCE;
		}

		public BigDecimal getR32_MONTH_END() {
			return R32_MONTH_END;
		}

		public void setR32_MONTH_END(BigDecimal r32_MONTH_END) {
			R32_MONTH_END = r32_MONTH_END;
		}

		public BigDecimal getR32_AVERAGE() {
			return R32_AVERAGE;
		}

		public void setR32_AVERAGE(BigDecimal r32_AVERAGE) {
			R32_AVERAGE = r32_AVERAGE;
		}

		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}

		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}

		public String getR33_CROSS_REFERENCE() {
			return R33_CROSS_REFERENCE;
		}

		public void setR33_CROSS_REFERENCE(String r33_CROSS_REFERENCE) {
			R33_CROSS_REFERENCE = r33_CROSS_REFERENCE;
		}

		public BigDecimal getR33_MONTH_END() {
			return R33_MONTH_END;
		}

		public void setR33_MONTH_END(BigDecimal r33_MONTH_END) {
			R33_MONTH_END = r33_MONTH_END;
		}

		public BigDecimal getR33_AVERAGE() {
			return R33_AVERAGE;
		}

		public void setR33_AVERAGE(BigDecimal r33_AVERAGE) {
			R33_AVERAGE = r33_AVERAGE;
		}

		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}

		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}

		public String getR34_CROSS_REFERENCE() {
			return R34_CROSS_REFERENCE;
		}

		public void setR34_CROSS_REFERENCE(String r34_CROSS_REFERENCE) {
			R34_CROSS_REFERENCE = r34_CROSS_REFERENCE;
		}

		public BigDecimal getR34_MONTH_END() {
			return R34_MONTH_END;
		}

		public void setR34_MONTH_END(BigDecimal r34_MONTH_END) {
			R34_MONTH_END = r34_MONTH_END;
		}

		public BigDecimal getR34_AVERAGE() {
			return R34_AVERAGE;
		}

		public void setR34_AVERAGE(BigDecimal r34_AVERAGE) {
			R34_AVERAGE = r34_AVERAGE;
		}

		public String getR35_PRODUCT() {
			return R35_PRODUCT;
		}

		public void setR35_PRODUCT(String r35_PRODUCT) {
			R35_PRODUCT = r35_PRODUCT;
		}

		public String getR35_CROSS_REFERENCE() {
			return R35_CROSS_REFERENCE;
		}

		public void setR35_CROSS_REFERENCE(String r35_CROSS_REFERENCE) {
			R35_CROSS_REFERENCE = r35_CROSS_REFERENCE;
		}

		public BigDecimal getR35_MONTH_END() {
			return R35_MONTH_END;
		}

		public void setR35_MONTH_END(BigDecimal r35_MONTH_END) {
			R35_MONTH_END = r35_MONTH_END;
		}

		public BigDecimal getR35_AVERAGE() {
			return R35_AVERAGE;
		}

		public void setR35_AVERAGE(BigDecimal r35_AVERAGE) {
			R35_AVERAGE = r35_AVERAGE;
		}

		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public String getR36_CROSS_REFERENCE() {
			return R36_CROSS_REFERENCE;
		}

		public void setR36_CROSS_REFERENCE(String r36_CROSS_REFERENCE) {
			R36_CROSS_REFERENCE = r36_CROSS_REFERENCE;
		}

		public BigDecimal getR36_MONTH_END() {
			return R36_MONTH_END;
		}

		public void setR36_MONTH_END(BigDecimal r36_MONTH_END) {
			R36_MONTH_END = r36_MONTH_END;
		}

		public BigDecimal getR36_AVERAGE() {
			return R36_AVERAGE;
		}

		public void setR36_AVERAGE(BigDecimal r36_AVERAGE) {
			R36_AVERAGE = r36_AVERAGE;
		}

		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public String getR37_CROSS_REFERENCE() {
			return R37_CROSS_REFERENCE;
		}

		public void setR37_CROSS_REFERENCE(String r37_CROSS_REFERENCE) {
			R37_CROSS_REFERENCE = r37_CROSS_REFERENCE;
		}

		public BigDecimal getR37_MONTH_END() {
			return R37_MONTH_END;
		}

		public void setR37_MONTH_END(BigDecimal r37_MONTH_END) {
			R37_MONTH_END = r37_MONTH_END;
		}

		public BigDecimal getR37_AVERAGE() {
			return R37_AVERAGE;
		}

		public void setR37_AVERAGE(BigDecimal r37_AVERAGE) {
			R37_AVERAGE = r37_AVERAGE;
		}

		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public String getR38_CROSS_REFERENCE() {
			return R38_CROSS_REFERENCE;
		}

		public void setR38_CROSS_REFERENCE(String r38_CROSS_REFERENCE) {
			R38_CROSS_REFERENCE = r38_CROSS_REFERENCE;
		}

		public BigDecimal getR38_MONTH_END() {
			return R38_MONTH_END;
		}

		public void setR38_MONTH_END(BigDecimal r38_MONTH_END) {
			R38_MONTH_END = r38_MONTH_END;
		}

		public BigDecimal getR38_AVERAGE() {
			return R38_AVERAGE;
		}

		public void setR38_AVERAGE(BigDecimal r38_AVERAGE) {
			R38_AVERAGE = r38_AVERAGE;
		}

		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public String getR39_CROSS_REFERENCE() {
			return R39_CROSS_REFERENCE;
		}

		public void setR39_CROSS_REFERENCE(String r39_CROSS_REFERENCE) {
			R39_CROSS_REFERENCE = r39_CROSS_REFERENCE;
		}

		public BigDecimal getR39_MONTH_END() {
			return R39_MONTH_END;
		}

		public void setR39_MONTH_END(BigDecimal r39_MONTH_END) {
			R39_MONTH_END = r39_MONTH_END;
		}

		public BigDecimal getR39_AVERAGE() {
			return R39_AVERAGE;
		}

		public void setR39_AVERAGE(BigDecimal r39_AVERAGE) {
			R39_AVERAGE = r39_AVERAGE;
		}

		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public String getR40_CROSS_REFERENCE() {
			return R40_CROSS_REFERENCE;
		}

		public void setR40_CROSS_REFERENCE(String r40_CROSS_REFERENCE) {
			R40_CROSS_REFERENCE = r40_CROSS_REFERENCE;
		}

		public BigDecimal getR40_MONTH_END() {
			return R40_MONTH_END;
		}

		public void setR40_MONTH_END(BigDecimal r40_MONTH_END) {
			R40_MONTH_END = r40_MONTH_END;
		}

		public BigDecimal getR40_AVERAGE() {
			return R40_AVERAGE;
		}

		public void setR40_AVERAGE(BigDecimal r40_AVERAGE) {
			R40_AVERAGE = r40_AVERAGE;
		}

		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public String getR41_CROSS_REFERENCE() {
			return R41_CROSS_REFERENCE;
		}

		public void setR41_CROSS_REFERENCE(String r41_CROSS_REFERENCE) {
			R41_CROSS_REFERENCE = r41_CROSS_REFERENCE;
		}

		public BigDecimal getR41_MONTH_END() {
			return R41_MONTH_END;
		}

		public void setR41_MONTH_END(BigDecimal r41_MONTH_END) {
			R41_MONTH_END = r41_MONTH_END;
		}

		public BigDecimal getR41_AVERAGE() {
			return R41_AVERAGE;
		}

		public void setR41_AVERAGE(BigDecimal r41_AVERAGE) {
			R41_AVERAGE = r41_AVERAGE;
		}

		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}

		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}

		public String getR42_CROSS_REFERENCE() {
			return R42_CROSS_REFERENCE;
		}

		public void setR42_CROSS_REFERENCE(String r42_CROSS_REFERENCE) {
			R42_CROSS_REFERENCE = r42_CROSS_REFERENCE;
		}

		public BigDecimal getR42_MONTH_END() {
			return R42_MONTH_END;
		}

		public void setR42_MONTH_END(BigDecimal r42_MONTH_END) {
			R42_MONTH_END = r42_MONTH_END;
		}

		public BigDecimal getR42_AVERAGE() {
			return R42_AVERAGE;
		}

		public void setR42_AVERAGE(BigDecimal r42_AVERAGE) {
			R42_AVERAGE = r42_AVERAGE;
		}

		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}

		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}

		public String getR43_CROSS_REFERENCE() {
			return R43_CROSS_REFERENCE;
		}

		public void setR43_CROSS_REFERENCE(String r43_CROSS_REFERENCE) {
			R43_CROSS_REFERENCE = r43_CROSS_REFERENCE;
		}

		public BigDecimal getR43_MONTH_END() {
			return R43_MONTH_END;
		}

		public void setR43_MONTH_END(BigDecimal r43_MONTH_END) {
			R43_MONTH_END = r43_MONTH_END;
		}

		public BigDecimal getR43_AVERAGE() {
			return R43_AVERAGE;
		}

		public void setR43_AVERAGE(BigDecimal r43_AVERAGE) {
			R43_AVERAGE = r43_AVERAGE;
		}

		public String getR44_PRODUCT() {
			return R44_PRODUCT;
		}

		public void setR44_PRODUCT(String r44_PRODUCT) {
			R44_PRODUCT = r44_PRODUCT;
		}

		public String getR44_CROSS_REFERENCE() {
			return R44_CROSS_REFERENCE;
		}

		public void setR44_CROSS_REFERENCE(String r44_CROSS_REFERENCE) {
			R44_CROSS_REFERENCE = r44_CROSS_REFERENCE;
		}

		public BigDecimal getR44_MONTH_END() {
			return R44_MONTH_END;
		}

		public void setR44_MONTH_END(BigDecimal r44_MONTH_END) {
			R44_MONTH_END = r44_MONTH_END;
		}

		public BigDecimal getR44_AVERAGE() {
			return R44_AVERAGE;
		}

		public void setR44_AVERAGE(BigDecimal r44_AVERAGE) {
			R44_AVERAGE = r44_AVERAGE;
		}

		public String getR45_PRODUCT() {
			return R45_PRODUCT;
		}

		public void setR45_PRODUCT(String r45_PRODUCT) {
			R45_PRODUCT = r45_PRODUCT;
		}

		public String getR45_CROSS_REFERENCE() {
			return R45_CROSS_REFERENCE;
		}

		public void setR45_CROSS_REFERENCE(String r45_CROSS_REFERENCE) {
			R45_CROSS_REFERENCE = r45_CROSS_REFERENCE;
		}

		public BigDecimal getR45_MONTH_END() {
			return R45_MONTH_END;
		}

		public void setR45_MONTH_END(BigDecimal r45_MONTH_END) {
			R45_MONTH_END = r45_MONTH_END;
		}

		public BigDecimal getR45_AVERAGE() {
			return R45_AVERAGE;
		}

		public void setR45_AVERAGE(BigDecimal r45_AVERAGE) {
			R45_AVERAGE = r45_AVERAGE;
		}

		public String getR46_PRODUCT() {
			return R46_PRODUCT;
		}

		public void setR46_PRODUCT(String r46_PRODUCT) {
			R46_PRODUCT = r46_PRODUCT;
		}

		public String getR46_CROSS_REFERENCE() {
			return R46_CROSS_REFERENCE;
		}

		public void setR46_CROSS_REFERENCE(String r46_CROSS_REFERENCE) {
			R46_CROSS_REFERENCE = r46_CROSS_REFERENCE;
		}

		public BigDecimal getR46_MONTH_END() {
			return R46_MONTH_END;
		}

		public void setR46_MONTH_END(BigDecimal r46_MONTH_END) {
			R46_MONTH_END = r46_MONTH_END;
		}

		public BigDecimal getR46_AVERAGE() {
			return R46_AVERAGE;
		}

		public void setR46_AVERAGE(BigDecimal r46_AVERAGE) {
			R46_AVERAGE = r46_AVERAGE;
		}

		public String getR47_PRODUCT() {
			return R47_PRODUCT;
		}

		public void setR47_PRODUCT(String r47_PRODUCT) {
			R47_PRODUCT = r47_PRODUCT;
		}

		public String getR47_CROSS_REFERENCE() {
			return R47_CROSS_REFERENCE;
		}

		public void setR47_CROSS_REFERENCE(String r47_CROSS_REFERENCE) {
			R47_CROSS_REFERENCE = r47_CROSS_REFERENCE;
		}

		public BigDecimal getR47_MONTH_END() {
			return R47_MONTH_END;
		}

		public void setR47_MONTH_END(BigDecimal r47_MONTH_END) {
			R47_MONTH_END = r47_MONTH_END;
		}

		public BigDecimal getR47_AVERAGE() {
			return R47_AVERAGE;
		}

		public void setR47_AVERAGE(BigDecimal r47_AVERAGE) {
			R47_AVERAGE = r47_AVERAGE;
		}

		public String getR48_PRODUCT() {
			return R48_PRODUCT;
		}

		public void setR48_PRODUCT(String r48_PRODUCT) {
			R48_PRODUCT = r48_PRODUCT;
		}

		public String getR48_CROSS_REFERENCE() {
			return R48_CROSS_REFERENCE;
		}

		public void setR48_CROSS_REFERENCE(String r48_CROSS_REFERENCE) {
			R48_CROSS_REFERENCE = r48_CROSS_REFERENCE;
		}

		public BigDecimal getR48_MONTH_END() {
			return R48_MONTH_END;
		}

		public void setR48_MONTH_END(BigDecimal r48_MONTH_END) {
			R48_MONTH_END = r48_MONTH_END;
		}

		public BigDecimal getR48_AVERAGE() {
			return R48_AVERAGE;
		}

		public void setR48_AVERAGE(BigDecimal r48_AVERAGE) {
			R48_AVERAGE = r48_AVERAGE;
		}

		public String getR49_PRODUCT() {
			return R49_PRODUCT;
		}

		public void setR49_PRODUCT(String r49_PRODUCT) {
			R49_PRODUCT = r49_PRODUCT;
		}

		public String getR49_CROSS_REFERENCE() {
			return R49_CROSS_REFERENCE;
		}

		public void setR49_CROSS_REFERENCE(String r49_CROSS_REFERENCE) {
			R49_CROSS_REFERENCE = r49_CROSS_REFERENCE;
		}

		public BigDecimal getR49_MONTH_END() {
			return R49_MONTH_END;
		}

		public void setR49_MONTH_END(BigDecimal r49_MONTH_END) {
			R49_MONTH_END = r49_MONTH_END;
		}

		public BigDecimal getR49_AVERAGE() {
			return R49_AVERAGE;
		}

		public void setR49_AVERAGE(BigDecimal r49_AVERAGE) {
			R49_AVERAGE = r49_AVERAGE;
		}

		public String getR50_PRODUCT() {
			return R50_PRODUCT;
		}

		public void setR50_PRODUCT(String r50_PRODUCT) {
			R50_PRODUCT = r50_PRODUCT;
		}

		public String getR50_CROSS_REFERENCE() {
			return R50_CROSS_REFERENCE;
		}

		public void setR50_CROSS_REFERENCE(String r50_CROSS_REFERENCE) {
			R50_CROSS_REFERENCE = r50_CROSS_REFERENCE;
		}

		public BigDecimal getR50_MONTH_END() {
			return R50_MONTH_END;
		}

		public void setR50_MONTH_END(BigDecimal r50_MONTH_END) {
			R50_MONTH_END = r50_MONTH_END;
		}

		public BigDecimal getR50_AVERAGE() {
			return R50_AVERAGE;
		}

		public void setR50_AVERAGE(BigDecimal r50_AVERAGE) {
			R50_AVERAGE = r50_AVERAGE;
		}

		public String getR51_PRODUCT() {
			return R51_PRODUCT;
		}

		public void setR51_PRODUCT(String r51_PRODUCT) {
			R51_PRODUCT = r51_PRODUCT;
		}

		public String getR51_CROSS_REFERENCE() {
			return R51_CROSS_REFERENCE;
		}

		public void setR51_CROSS_REFERENCE(String r51_CROSS_REFERENCE) {
			R51_CROSS_REFERENCE = r51_CROSS_REFERENCE;
		}

		public BigDecimal getR51_MONTH_END() {
			return R51_MONTH_END;
		}

		public void setR51_MONTH_END(BigDecimal r51_MONTH_END) {
			R51_MONTH_END = r51_MONTH_END;
		}

		public BigDecimal getR51_AVERAGE() {
			return R51_AVERAGE;
		}

		public void setR51_AVERAGE(BigDecimal r51_AVERAGE) {
			R51_AVERAGE = r51_AVERAGE;
		}

		public String getR52_PRODUCT() {
			return R52_PRODUCT;
		}

		public void setR52_PRODUCT(String r52_PRODUCT) {
			R52_PRODUCT = r52_PRODUCT;
		}

		public String getR52_CROSS_REFERENCE() {
			return R52_CROSS_REFERENCE;
		}

		public void setR52_CROSS_REFERENCE(String r52_CROSS_REFERENCE) {
			R52_CROSS_REFERENCE = r52_CROSS_REFERENCE;
		}

		public BigDecimal getR52_MONTH_END() {
			return R52_MONTH_END;
		}

		public void setR52_MONTH_END(BigDecimal r52_MONTH_END) {
			R52_MONTH_END = r52_MONTH_END;
		}

		public BigDecimal getR52_AVERAGE() {
			return R52_AVERAGE;
		}

		public void setR52_AVERAGE(BigDecimal r52_AVERAGE) {
			R52_AVERAGE = r52_AVERAGE;
		}

		public String getR53_PRODUCT() {
			return R53_PRODUCT;
		}

		public void setR53_PRODUCT(String r53_PRODUCT) {
			R53_PRODUCT = r53_PRODUCT;
		}

		public String getR53_CROSS_REFERENCE() {
			return R53_CROSS_REFERENCE;
		}

		public void setR53_CROSS_REFERENCE(String r53_CROSS_REFERENCE) {
			R53_CROSS_REFERENCE = r53_CROSS_REFERENCE;
		}

		public BigDecimal getR53_MONTH_END() {
			return R53_MONTH_END;
		}

		public void setR53_MONTH_END(BigDecimal r53_MONTH_END) {
			R53_MONTH_END = r53_MONTH_END;
		}

		public BigDecimal getR53_AVERAGE() {
			return R53_AVERAGE;
		}

		public void setR53_AVERAGE(BigDecimal r53_AVERAGE) {
			R53_AVERAGE = r53_AVERAGE;
		}

		public String getR54_PRODUCT() {
			return R54_PRODUCT;
		}

		public void setR54_PRODUCT(String r54_PRODUCT) {
			R54_PRODUCT = r54_PRODUCT;
		}

		public String getR54_CROSS_REFERENCE() {
			return R54_CROSS_REFERENCE;
		}

		public void setR54_CROSS_REFERENCE(String r54_CROSS_REFERENCE) {
			R54_CROSS_REFERENCE = r54_CROSS_REFERENCE;
		}

		public BigDecimal getR54_MONTH_END() {
			return R54_MONTH_END;
		}

		public void setR54_MONTH_END(BigDecimal r54_MONTH_END) {
			R54_MONTH_END = r54_MONTH_END;
		}

		public BigDecimal getR54_AVERAGE() {
			return R54_AVERAGE;
		}

		public void setR54_AVERAGE(BigDecimal r54_AVERAGE) {
			R54_AVERAGE = r54_AVERAGE;
		}

		public String getR55_PRODUCT() {
			return R55_PRODUCT;
		}

		public void setR55_PRODUCT(String r55_PRODUCT) {
			R55_PRODUCT = r55_PRODUCT;
		}

		public String getR55_CROSS_REFERENCE() {
			return R55_CROSS_REFERENCE;
		}

		public void setR55_CROSS_REFERENCE(String r55_CROSS_REFERENCE) {
			R55_CROSS_REFERENCE = r55_CROSS_REFERENCE;
		}

		public BigDecimal getR55_MONTH_END() {
			return R55_MONTH_END;
		}

		public void setR55_MONTH_END(BigDecimal r55_MONTH_END) {
			R55_MONTH_END = r55_MONTH_END;
		}

		public BigDecimal getR55_AVERAGE() {
			return R55_AVERAGE;
		}

		public void setR55_AVERAGE(BigDecimal r55_AVERAGE) {
			R55_AVERAGE = r55_AVERAGE;
		}

		public String getR56_PRODUCT() {
			return R56_PRODUCT;
		}

		public void setR56_PRODUCT(String r56_PRODUCT) {
			R56_PRODUCT = r56_PRODUCT;
		}

		public String getR56_CROSS_REFERENCE() {
			return R56_CROSS_REFERENCE;
		}

		public void setR56_CROSS_REFERENCE(String r56_CROSS_REFERENCE) {
			R56_CROSS_REFERENCE = r56_CROSS_REFERENCE;
		}

		public BigDecimal getR56_MONTH_END() {
			return R56_MONTH_END;
		}

		public void setR56_MONTH_END(BigDecimal r56_MONTH_END) {
			R56_MONTH_END = r56_MONTH_END;
		}

		public BigDecimal getR56_AVERAGE() {
			return R56_AVERAGE;
		}

		public void setR56_AVERAGE(BigDecimal r56_AVERAGE) {
			R56_AVERAGE = r56_AVERAGE;
		}

		public String getR57_PRODUCT() {
			return R57_PRODUCT;
		}

		public void setR57_PRODUCT(String r57_PRODUCT) {
			R57_PRODUCT = r57_PRODUCT;
		}

		public String getR57_CROSS_REFERENCE() {
			return R57_CROSS_REFERENCE;
		}

		public void setR57_CROSS_REFERENCE(String r57_CROSS_REFERENCE) {
			R57_CROSS_REFERENCE = r57_CROSS_REFERENCE;
		}

		public BigDecimal getR57_MONTH_END() {
			return R57_MONTH_END;
		}

		public void setR57_MONTH_END(BigDecimal r57_MONTH_END) {
			R57_MONTH_END = r57_MONTH_END;
		}

		public BigDecimal getR57_AVERAGE() {
			return R57_AVERAGE;
		}

		public void setR57_AVERAGE(BigDecimal r57_AVERAGE) {
			R57_AVERAGE = r57_AVERAGE;
		}

		public String getR58_PRODUCT() {
			return R58_PRODUCT;
		}

		public void setR58_PRODUCT(String r58_PRODUCT) {
			R58_PRODUCT = r58_PRODUCT;
		}

		public String getR58_CROSS_REFERENCE() {
			return R58_CROSS_REFERENCE;
		}

		public void setR58_CROSS_REFERENCE(String r58_CROSS_REFERENCE) {
			R58_CROSS_REFERENCE = r58_CROSS_REFERENCE;
		}

		public BigDecimal getR58_MONTH_END() {
			return R58_MONTH_END;
		}

		public void setR58_MONTH_END(BigDecimal r58_MONTH_END) {
			R58_MONTH_END = r58_MONTH_END;
		}

		public BigDecimal getR58_AVERAGE() {
			return R58_AVERAGE;
		}

		public void setR58_AVERAGE(BigDecimal r58_AVERAGE) {
			R58_AVERAGE = r58_AVERAGE;
		}

		public String getR59_PRODUCT() {
			return R59_PRODUCT;
		}

		public void setR59_PRODUCT(String r59_PRODUCT) {
			R59_PRODUCT = r59_PRODUCT;
		}

		public String getR59_CROSS_REFERENCE() {
			return R59_CROSS_REFERENCE;
		}

		public void setR59_CROSS_REFERENCE(String r59_CROSS_REFERENCE) {
			R59_CROSS_REFERENCE = r59_CROSS_REFERENCE;
		}

		public BigDecimal getR59_MONTH_END() {
			return R59_MONTH_END;
		}

		public void setR59_MONTH_END(BigDecimal r59_MONTH_END) {
			R59_MONTH_END = r59_MONTH_END;
		}

		public BigDecimal getR59_AVERAGE() {
			return R59_AVERAGE;
		}

		public void setR59_AVERAGE(BigDecimal r59_AVERAGE) {
			R59_AVERAGE = r59_AVERAGE;
		}

		public String getR60_PRODUCT() {
			return R60_PRODUCT;
		}

		public void setR60_PRODUCT(String r60_PRODUCT) {
			R60_PRODUCT = r60_PRODUCT;
		}

		public String getR60_CROSS_REFERENCE() {
			return R60_CROSS_REFERENCE;
		}

		public void setR60_CROSS_REFERENCE(String r60_CROSS_REFERENCE) {
			R60_CROSS_REFERENCE = r60_CROSS_REFERENCE;
		}

		public BigDecimal getR60_MONTH_END() {
			return R60_MONTH_END;
		}

		public void setR60_MONTH_END(BigDecimal r60_MONTH_END) {
			R60_MONTH_END = r60_MONTH_END;
		}

		public BigDecimal getR60_AVERAGE() {
			return R60_AVERAGE;
		}

		public void setR60_AVERAGE(BigDecimal r60_AVERAGE) {
			R60_AVERAGE = r60_AVERAGE;
		}

		public String getR61_PRODUCT() {
			return R61_PRODUCT;
		}

		public void setR61_PRODUCT(String r61_PRODUCT) {
			R61_PRODUCT = r61_PRODUCT;
		}

		public String getR61_CROSS_REFERENCE() {
			return R61_CROSS_REFERENCE;
		}

		public void setR61_CROSS_REFERENCE(String r61_CROSS_REFERENCE) {
			R61_CROSS_REFERENCE = r61_CROSS_REFERENCE;
		}

		public BigDecimal getR61_MONTH_END() {
			return R61_MONTH_END;
		}

		public void setR61_MONTH_END(BigDecimal r61_MONTH_END) {
			R61_MONTH_END = r61_MONTH_END;
		}

		public BigDecimal getR61_AVERAGE() {
			return R61_AVERAGE;
		}

		public void setR61_AVERAGE(BigDecimal r61_AVERAGE) {
			R61_AVERAGE = r61_AVERAGE;
		}

		public String getR62_PRODUCT() {
			return R62_PRODUCT;
		}

		public void setR62_PRODUCT(String r62_PRODUCT) {
			R62_PRODUCT = r62_PRODUCT;
		}

		public String getR62_CROSS_REFERENCE() {
			return R62_CROSS_REFERENCE;
		}

		public void setR62_CROSS_REFERENCE(String r62_CROSS_REFERENCE) {
			R62_CROSS_REFERENCE = r62_CROSS_REFERENCE;
		}

		public BigDecimal getR62_MONTH_END() {
			return R62_MONTH_END;
		}

		public void setR62_MONTH_END(BigDecimal r62_MONTH_END) {
			R62_MONTH_END = r62_MONTH_END;
		}

		public BigDecimal getR62_AVERAGE() {
			return R62_AVERAGE;
		}

		public void setR62_AVERAGE(BigDecimal r62_AVERAGE) {
			R62_AVERAGE = r62_AVERAGE;
		}

		public String getR63_PRODUCT() {
			return R63_PRODUCT;
		}

		public void setR63_PRODUCT(String r63_PRODUCT) {
			R63_PRODUCT = r63_PRODUCT;
		}

		public String getR63_CROSS_REFERENCE() {
			return R63_CROSS_REFERENCE;
		}

		public void setR63_CROSS_REFERENCE(String r63_CROSS_REFERENCE) {
			R63_CROSS_REFERENCE = r63_CROSS_REFERENCE;
		}

		public BigDecimal getR63_MONTH_END() {
			return R63_MONTH_END;
		}

		public void setR63_MONTH_END(BigDecimal r63_MONTH_END) {
			R63_MONTH_END = r63_MONTH_END;
		}

		public BigDecimal getR63_AVERAGE() {
			return R63_AVERAGE;
		}

		public void setR63_AVERAGE(BigDecimal r63_AVERAGE) {
			R63_AVERAGE = r63_AVERAGE;
		}

		public String getR64_PRODUCT() {
			return R64_PRODUCT;
		}

		public void setR64_PRODUCT(String r64_PRODUCT) {
			R64_PRODUCT = r64_PRODUCT;
		}

		public String getR64_CROSS_REFERENCE() {
			return R64_CROSS_REFERENCE;
		}

		public void setR64_CROSS_REFERENCE(String r64_CROSS_REFERENCE) {
			R64_CROSS_REFERENCE = r64_CROSS_REFERENCE;
		}

		public BigDecimal getR64_MONTH_END() {
			return R64_MONTH_END;
		}

		public void setR64_MONTH_END(BigDecimal r64_MONTH_END) {
			R64_MONTH_END = r64_MONTH_END;
		}

		public BigDecimal getR64_AVERAGE() {
			return R64_AVERAGE;
		}

		public void setR64_AVERAGE(BigDecimal r64_AVERAGE) {
			R64_AVERAGE = r64_AVERAGE;
		}

		public String getR65_PRODUCT() {
			return R65_PRODUCT;
		}

		public void setR65_PRODUCT(String r65_PRODUCT) {
			R65_PRODUCT = r65_PRODUCT;
		}

		public String getR65_CROSS_REFERENCE() {
			return R65_CROSS_REFERENCE;
		}

		public void setR65_CROSS_REFERENCE(String r65_CROSS_REFERENCE) {
			R65_CROSS_REFERENCE = r65_CROSS_REFERENCE;
		}

		public BigDecimal getR65_MONTH_END() {
			return R65_MONTH_END;
		}

		public void setR65_MONTH_END(BigDecimal r65_MONTH_END) {
			R65_MONTH_END = r65_MONTH_END;
		}

		public BigDecimal getR65_AVERAGE() {
			return R65_AVERAGE;
		}

		public void setR65_AVERAGE(BigDecimal r65_AVERAGE) {
			R65_AVERAGE = r65_AVERAGE;
		}

		public String getR66_PRODUCT() {
			return R66_PRODUCT;
		}

		public void setR66_PRODUCT(String r66_PRODUCT) {
			R66_PRODUCT = r66_PRODUCT;
		}

		public String getR66_CROSS_REFERENCE() {
			return R66_CROSS_REFERENCE;
		}

		public void setR66_CROSS_REFERENCE(String r66_CROSS_REFERENCE) {
			R66_CROSS_REFERENCE = r66_CROSS_REFERENCE;
		}

		public BigDecimal getR66_MONTH_END() {
			return R66_MONTH_END;
		}

		public void setR66_MONTH_END(BigDecimal r66_MONTH_END) {
			R66_MONTH_END = r66_MONTH_END;
		}

		public BigDecimal getR66_AVERAGE() {
			return R66_AVERAGE;
		}

		public void setR66_AVERAGE(BigDecimal r66_AVERAGE) {
			R66_AVERAGE = r66_AVERAGE;
		}

		public String getR67_PRODUCT() {
			return R67_PRODUCT;
		}

		public void setR67_PRODUCT(String r67_PRODUCT) {
			R67_PRODUCT = r67_PRODUCT;
		}

		public String getR67_CROSS_REFERENCE() {
			return R67_CROSS_REFERENCE;
		}

		public void setR67_CROSS_REFERENCE(String r67_CROSS_REFERENCE) {
			R67_CROSS_REFERENCE = r67_CROSS_REFERENCE;
		}

		public BigDecimal getR67_MONTH_END() {
			return R67_MONTH_END;
		}

		public void setR67_MONTH_END(BigDecimal r67_MONTH_END) {
			R67_MONTH_END = r67_MONTH_END;
		}

		public BigDecimal getR67_AVERAGE() {
			return R67_AVERAGE;
		}

		public void setR67_AVERAGE(BigDecimal r67_AVERAGE) {
			R67_AVERAGE = r67_AVERAGE;
		}

		public String getR68_PRODUCT() {
			return R68_PRODUCT;
		}

		public void setR68_PRODUCT(String r68_PRODUCT) {
			R68_PRODUCT = r68_PRODUCT;
		}

		public String getR68_CROSS_REFERENCE() {
			return R68_CROSS_REFERENCE;
		}

		public void setR68_CROSS_REFERENCE(String r68_CROSS_REFERENCE) {
			R68_CROSS_REFERENCE = r68_CROSS_REFERENCE;
		}

		public BigDecimal getR68_MONTH_END() {
			return R68_MONTH_END;
		}

		public void setR68_MONTH_END(BigDecimal r68_MONTH_END) {
			R68_MONTH_END = r68_MONTH_END;
		}

		public BigDecimal getR68_AVERAGE() {
			return R68_AVERAGE;
		}

		public void setR68_AVERAGE(BigDecimal r68_AVERAGE) {
			R68_AVERAGE = r68_AVERAGE;
		}

		public String getR69_PRODUCT() {
			return R69_PRODUCT;
		}

		public void setR69_PRODUCT(String r69_PRODUCT) {
			R69_PRODUCT = r69_PRODUCT;
		}

		public String getR69_CROSS_REFERENCE() {
			return R69_CROSS_REFERENCE;
		}

		public void setR69_CROSS_REFERENCE(String r69_CROSS_REFERENCE) {
			R69_CROSS_REFERENCE = r69_CROSS_REFERENCE;
		}

		public BigDecimal getR69_MONTH_END() {
			return R69_MONTH_END;
		}

		public void setR69_MONTH_END(BigDecimal r69_MONTH_END) {
			R69_MONTH_END = r69_MONTH_END;
		}

		public BigDecimal getR69_AVERAGE() {
			return R69_AVERAGE;
		}

		public void setR69_AVERAGE(BigDecimal r69_AVERAGE) {
			R69_AVERAGE = r69_AVERAGE;
		}

		public String getR70_PRODUCT() {
			return R70_PRODUCT;
		}

		public void setR70_PRODUCT(String r70_PRODUCT) {
			R70_PRODUCT = r70_PRODUCT;
		}

		public String getR70_CROSS_REFERENCE() {
			return R70_CROSS_REFERENCE;
		}

		public void setR70_CROSS_REFERENCE(String r70_CROSS_REFERENCE) {
			R70_CROSS_REFERENCE = r70_CROSS_REFERENCE;
		}

		public BigDecimal getR70_MONTH_END() {
			return R70_MONTH_END;
		}

		public void setR70_MONTH_END(BigDecimal r70_MONTH_END) {
			R70_MONTH_END = r70_MONTH_END;
		}

		public BigDecimal getR70_AVERAGE() {
			return R70_AVERAGE;
		}

		public void setR70_AVERAGE(BigDecimal r70_AVERAGE) {
			R70_AVERAGE = r70_AVERAGE;
		}

		public String getR71_PRODUCT() {
			return R71_PRODUCT;
		}

		public void setR71_PRODUCT(String r71_PRODUCT) {
			R71_PRODUCT = r71_PRODUCT;
		}

		public String getR71_CROSS_REFERENCE() {
			return R71_CROSS_REFERENCE;
		}

		public void setR71_CROSS_REFERENCE(String r71_CROSS_REFERENCE) {
			R71_CROSS_REFERENCE = r71_CROSS_REFERENCE;
		}

		public BigDecimal getR71_MONTH_END() {
			return R71_MONTH_END;
		}

		public void setR71_MONTH_END(BigDecimal r71_MONTH_END) {
			R71_MONTH_END = r71_MONTH_END;
		}

		public BigDecimal getR71_AVERAGE() {
			return R71_AVERAGE;
		}

		public void setR71_AVERAGE(BigDecimal r71_AVERAGE) {
			R71_AVERAGE = r71_AVERAGE;
		}

		public String getR72_PRODUCT() {
			return R72_PRODUCT;
		}

		public void setR72_PRODUCT(String r72_PRODUCT) {
			R72_PRODUCT = r72_PRODUCT;
		}

		public String getR72_CROSS_REFERENCE() {
			return R72_CROSS_REFERENCE;
		}

		public void setR72_CROSS_REFERENCE(String r72_CROSS_REFERENCE) {
			R72_CROSS_REFERENCE = r72_CROSS_REFERENCE;
		}

		public BigDecimal getR72_MONTH_END() {
			return R72_MONTH_END;
		}

		public void setR72_MONTH_END(BigDecimal r72_MONTH_END) {
			R72_MONTH_END = r72_MONTH_END;
		}

		public BigDecimal getR72_AVERAGE() {
			return R72_AVERAGE;
		}

		public void setR72_AVERAGE(BigDecimal r72_AVERAGE) {
			R72_AVERAGE = r72_AVERAGE;
		}

		public String getR73_PRODUCT() {
			return R73_PRODUCT;
		}

		public void setR73_PRODUCT(String r73_PRODUCT) {
			R73_PRODUCT = r73_PRODUCT;
		}

		public String getR73_CROSS_REFERENCE() {
			return R73_CROSS_REFERENCE;
		}

		public void setR73_CROSS_REFERENCE(String r73_CROSS_REFERENCE) {
			R73_CROSS_REFERENCE = r73_CROSS_REFERENCE;
		}

		public BigDecimal getR73_MONTH_END() {
			return R73_MONTH_END;
		}

		public void setR73_MONTH_END(BigDecimal r73_MONTH_END) {
			R73_MONTH_END = r73_MONTH_END;
		}

		public BigDecimal getR73_AVERAGE() {
			return R73_AVERAGE;
		}

		public void setR73_AVERAGE(BigDecimal r73_AVERAGE) {
			R73_AVERAGE = r73_AVERAGE;
		}

		public String getR74_PRODUCT() {
			return R74_PRODUCT;
		}

		public void setR74_PRODUCT(String r74_PRODUCT) {
			R74_PRODUCT = r74_PRODUCT;
		}

		public String getR74_CROSS_REFERENCE() {
			return R74_CROSS_REFERENCE;
		}

		public void setR74_CROSS_REFERENCE(String r74_CROSS_REFERENCE) {
			R74_CROSS_REFERENCE = r74_CROSS_REFERENCE;
		}

		public BigDecimal getR74_MONTH_END() {
			return R74_MONTH_END;
		}

		public void setR74_MONTH_END(BigDecimal r74_MONTH_END) {
			R74_MONTH_END = r74_MONTH_END;
		}

		public BigDecimal getR74_AVERAGE() {
			return R74_AVERAGE;
		}

		public void setR74_AVERAGE(BigDecimal r74_AVERAGE) {
			R74_AVERAGE = r74_AVERAGE;
		}

		public String getR75_PRODUCT() {
			return R75_PRODUCT;
		}

		public void setR75_PRODUCT(String r75_PRODUCT) {
			R75_PRODUCT = r75_PRODUCT;
		}

		public String getR75_CROSS_REFERENCE() {
			return R75_CROSS_REFERENCE;
		}

		public void setR75_CROSS_REFERENCE(String r75_CROSS_REFERENCE) {
			R75_CROSS_REFERENCE = r75_CROSS_REFERENCE;
		}

		public BigDecimal getR75_MONTH_END() {
			return R75_MONTH_END;
		}

		public void setR75_MONTH_END(BigDecimal r75_MONTH_END) {
			R75_MONTH_END = r75_MONTH_END;
		}

		public BigDecimal getR75_AVERAGE() {
			return R75_AVERAGE;
		}

		public void setR75_AVERAGE(BigDecimal r75_AVERAGE) {
			R75_AVERAGE = r75_AVERAGE;
		}

		public String getR76_PRODUCT() {
			return R76_PRODUCT;
		}

		public void setR76_PRODUCT(String r76_PRODUCT) {
			R76_PRODUCT = r76_PRODUCT;
		}

		public String getR76_CROSS_REFERENCE() {
			return R76_CROSS_REFERENCE;
		}

		public void setR76_CROSS_REFERENCE(String r76_CROSS_REFERENCE) {
			R76_CROSS_REFERENCE = r76_CROSS_REFERENCE;
		}

		public BigDecimal getR76_MONTH_END() {
			return R76_MONTH_END;
		}

		public void setR76_MONTH_END(BigDecimal r76_MONTH_END) {
			R76_MONTH_END = r76_MONTH_END;
		}

		public BigDecimal getR76_AVERAGE() {
			return R76_AVERAGE;
		}

		public void setR76_AVERAGE(BigDecimal r76_AVERAGE) {
			R76_AVERAGE = r76_AVERAGE;
		}

		public String getR77_PRODUCT() {
			return R77_PRODUCT;
		}

		public void setR77_PRODUCT(String r77_PRODUCT) {
			R77_PRODUCT = r77_PRODUCT;
		}

		public String getR77_CROSS_REFERENCE() {
			return R77_CROSS_REFERENCE;
		}

		public void setR77_CROSS_REFERENCE(String r77_CROSS_REFERENCE) {
			R77_CROSS_REFERENCE = r77_CROSS_REFERENCE;
		}

		public BigDecimal getR77_MONTH_END() {
			return R77_MONTH_END;
		}

		public void setR77_MONTH_END(BigDecimal r77_MONTH_END) {
			R77_MONTH_END = r77_MONTH_END;
		}

		public BigDecimal getR77_AVERAGE() {
			return R77_AVERAGE;
		}

		public void setR77_AVERAGE(BigDecimal r77_AVERAGE) {
			R77_AVERAGE = r77_AVERAGE;
		}

		public String getR78_PRODUCT() {
			return R78_PRODUCT;
		}

		public void setR78_PRODUCT(String r78_PRODUCT) {
			R78_PRODUCT = r78_PRODUCT;
		}

		public String getR78_CROSS_REFERENCE() {
			return R78_CROSS_REFERENCE;
		}

		public void setR78_CROSS_REFERENCE(String r78_CROSS_REFERENCE) {
			R78_CROSS_REFERENCE = r78_CROSS_REFERENCE;
		}

		public BigDecimal getR78_MONTH_END() {
			return R78_MONTH_END;
		}

		public void setR78_MONTH_END(BigDecimal r78_MONTH_END) {
			R78_MONTH_END = r78_MONTH_END;
		}

		public BigDecimal getR78_AVERAGE() {
			return R78_AVERAGE;
		}

		public void setR78_AVERAGE(BigDecimal r78_AVERAGE) {
			R78_AVERAGE = r78_AVERAGE;
		}

		public String getR79_PRODUCT() {
			return R79_PRODUCT;
		}

		public void setR79_PRODUCT(String r79_PRODUCT) {
			R79_PRODUCT = r79_PRODUCT;
		}

		public String getR79_CROSS_REFERENCE() {
			return R79_CROSS_REFERENCE;
		}

		public void setR79_CROSS_REFERENCE(String r79_CROSS_REFERENCE) {
			R79_CROSS_REFERENCE = r79_CROSS_REFERENCE;
		}

		public BigDecimal getR79_MONTH_END() {
			return R79_MONTH_END;
		}

		public void setR79_MONTH_END(BigDecimal r79_MONTH_END) {
			R79_MONTH_END = r79_MONTH_END;
		}

		public BigDecimal getR79_AVERAGE() {
			return R79_AVERAGE;
		}

		public void setR79_AVERAGE(BigDecimal r79_AVERAGE) {
			R79_AVERAGE = r79_AVERAGE;
		}

		public String getR80_PRODUCT() {
			return R80_PRODUCT;
		}

		public void setR80_PRODUCT(String r80_PRODUCT) {
			R80_PRODUCT = r80_PRODUCT;
		}

		public String getR80_CROSS_REFERENCE() {
			return R80_CROSS_REFERENCE;
		}

		public void setR80_CROSS_REFERENCE(String r80_CROSS_REFERENCE) {
			R80_CROSS_REFERENCE = r80_CROSS_REFERENCE;
		}

		public BigDecimal getR80_MONTH_END() {
			return R80_MONTH_END;
		}

		public void setR80_MONTH_END(BigDecimal r80_MONTH_END) {
			R80_MONTH_END = r80_MONTH_END;
		}

		public BigDecimal getR80_AVERAGE() {
			return R80_AVERAGE;
		}

		public void setR80_AVERAGE(BigDecimal r80_AVERAGE) {
			R80_AVERAGE = r80_AVERAGE;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date rEPORT_DATE) {
			REPORT_DATE = rEPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
			REPORT_VERSION = rEPORT_VERSION;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}

		public M_SFINP2_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

// ------------------------------
// M_SFINP2_Detail_Entity 
// ------------------------------
	public static class M_SFINP2_Detail_Entity {

		@Column(name = "CUST_ID")
		private String custId;
		@Id
		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "COLUMN_ID")
		private String columnId;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria_1;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
		private BigDecimal acctBalanceInpula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;
		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private char entityFlg;

		@Column(name = "MODIFY_FLG")
		private char modifyFlg;

		@Column(name = "DEL_FLG")
		private char delFlg;

// ---------- MISSING FIELDS ----------

		@Column(name = "REPORT_NAME_1")
		private String reportName1;

		@Column(name = "GL_CODE")
		private String glCode;

		@Column(name = "GL_SUB_CODE")
		private String glSubCode;

		@Column(name = "HEAD_ACC_NO")
		private String headAccNo;

		@Column(name = "DESCRIPTION")
		private String description;

		@Column(name = "CURRENCY")
		private String currency;

		@Column(name = "DEBIT_BALANCE", precision = 24, scale = 3)
		private BigDecimal debitBalance;

		@Column(name = "CREDIT_BALANCE", precision = 24, scale = 3)
		private BigDecimal creditBalance;

		@Column(name = "DEBIT_EQUIVALENT", precision = 24, scale = 3)
		private BigDecimal debitEquivalent;

		@Column(name = "CREDIT_EQUIVALENT", precision = 24, scale = 3)
		private BigDecimal creditEquivalent;

		@Column(name = "ENTRY_USER")
		private String entryUser;

		@Column(name = "ENTRY_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date entryDate;

		@Column(name = "AVERAGE", precision = 24, scale = 3)
		private BigDecimal average;

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getColumnId() {
			return columnId;
		}

		public void setColumnId(String columnId) {
			this.columnId = columnId;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria_1() {
			return reportAddlCriteria_1;
		}

		public void setReportAddlCriteria_1(String reportAddlCriteria_1) {
			this.reportAddlCriteria_1 = reportAddlCriteria_1;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public char getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(char entityFlg) {
			this.entityFlg = entityFlg;
		}

		public char getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(char modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public char getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(char delFlg) {
			this.delFlg = delFlg;
		}

		public String getReportName1() {
			return reportName1;
		}

		public void setReportName1(String reportName1) {
			this.reportName1 = reportName1;
		}

		public String getGlCode() {
			return glCode;
		}

		public void setGlCode(String glCode) {
			this.glCode = glCode;
		}

		public String getGlSubCode() {
			return glSubCode;
		}

		public void setGlSubCode(String glSubCode) {
			this.glSubCode = glSubCode;
		}

		public String getHeadAccNo() {
			return headAccNo;
		}

		public void setHeadAccNo(String headAccNo) {
			this.headAccNo = headAccNo;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public BigDecimal getDebitBalance() {
			return debitBalance;
		}

		public void setDebitBalance(BigDecimal debitBalance) {
			this.debitBalance = debitBalance;
		}

		public BigDecimal getCreditBalance() {
			return creditBalance;
		}

		public void setCreditBalance(BigDecimal creditBalance) {
			this.creditBalance = creditBalance;
		}

		public BigDecimal getDebitEquivalent() {
			return debitEquivalent;
		}

		public void setDebitEquivalent(BigDecimal debitEquivalent) {
			this.debitEquivalent = debitEquivalent;
		}

		public BigDecimal getCreditEquivalent() {
			return creditEquivalent;
		}

		public void setCreditEquivalent(BigDecimal creditEquivalent) {
			this.creditEquivalent = creditEquivalent;
		}

		public String getEntryUser() {
			return entryUser;
		}

		public void setEntryUser(String entryUser) {
			this.entryUser = entryUser;
		}

		public Date getEntryDate() {
			return entryDate;
		}

		public void setEntryDate(Date entryDate) {
			this.entryDate = entryDate;
		}

		public BigDecimal getAverage() {
			return average;
		}

		public void setAverage(BigDecimal average) {
			this.average = average;
		}

		public M_SFINP2_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

// ------------------------------
// M_SFINP2_Archival_Summary_Entity 
// ------------------------------
	public static class M_SFINP2_Archival_Summary_Entity {
		public String R10_PRODUCT;
		public String R10_CROSS_REFERENCE;
		public BigDecimal R10_MONTH_END;
		public BigDecimal R10_AVERAGE;
		public String R11_PRODUCT;
		public String R11_CROSS_REFERENCE;
		public BigDecimal R11_MONTH_END;
		public BigDecimal R11_AVERAGE;
		public String R12_PRODUCT;
		public String R12_CROSS_REFERENCE;
		public BigDecimal R12_MONTH_END;
		public BigDecimal R12_AVERAGE;
		public String R13_PRODUCT;
		public String R13_CROSS_REFERENCE;
		public BigDecimal R13_MONTH_END;
		public BigDecimal R13_AVERAGE;
		public String R14_PRODUCT;
		public String R14_CROSS_REFERENCE;
		public BigDecimal R14_MONTH_END;
		public BigDecimal R14_AVERAGE;
		public String R15_PRODUCT;
		public String R15_CROSS_REFERENCE;
		public BigDecimal R15_MONTH_END;
		public BigDecimal R15_AVERAGE;
		public String R16_PRODUCT;
		public String R16_CROSS_REFERENCE;
		public BigDecimal R16_MONTH_END;
		public BigDecimal R16_AVERAGE;
		public String R17_PRODUCT;
		public String R17_CROSS_REFERENCE;
		public BigDecimal R17_MONTH_END;
		public BigDecimal R17_AVERAGE;
		public String R18_PRODUCT;
		public String R18_CROSS_REFERENCE;
		public BigDecimal R18_MONTH_END;
		public BigDecimal R18_AVERAGE;
		public String R19_PRODUCT;
		public String R19_CROSS_REFERENCE;
		public BigDecimal R19_MONTH_END;
		public BigDecimal R19_AVERAGE;
		public String R20_PRODUCT;
		public String R20_CROSS_REFERENCE;
		public BigDecimal R20_MONTH_END;
		public BigDecimal R20_AVERAGE;
		public String R21_PRODUCT;
		public String R21_CROSS_REFERENCE;
		public BigDecimal R21_MONTH_END;
		public BigDecimal R21_AVERAGE;
		public String R22_PRODUCT;
		public String R22_CROSS_REFERENCE;
		public BigDecimal R22_MONTH_END;
		public BigDecimal R22_AVERAGE;
		public String R23_PRODUCT;
		public String R23_CROSS_REFERENCE;
		public BigDecimal R23_MONTH_END;
		public BigDecimal R23_AVERAGE;
		public String R24_PRODUCT;
		public String R24_CROSS_REFERENCE;
		public BigDecimal R24_MONTH_END;
		public BigDecimal R24_AVERAGE;
		public String R25_PRODUCT;
		public String R25_CROSS_REFERENCE;
		public BigDecimal R25_MONTH_END;
		public BigDecimal R25_AVERAGE;
		public String R26_PRODUCT;
		public String R26_CROSS_REFERENCE;
		public BigDecimal R26_MONTH_END;
		public BigDecimal R26_AVERAGE;
		public String R27_PRODUCT;
		public String R27_CROSS_REFERENCE;
		public BigDecimal R27_MONTH_END;
		public BigDecimal R27_AVERAGE;
		public String R28_PRODUCT;
		public String R28_CROSS_REFERENCE;
		public BigDecimal R28_MONTH_END;
		public BigDecimal R28_AVERAGE;
		public String R29_PRODUCT;
		public String R29_CROSS_REFERENCE;
		public BigDecimal R29_MONTH_END;
		public BigDecimal R29_AVERAGE;
		public String R30_PRODUCT;
		public String R30_CROSS_REFERENCE;
		public BigDecimal R30_MONTH_END;
		public BigDecimal R30_AVERAGE;
		public String R31_PRODUCT;
		public String R31_CROSS_REFERENCE;
		public BigDecimal R31_MONTH_END;
		public BigDecimal R31_AVERAGE;
		public String R32_PRODUCT;
		public String R32_CROSS_REFERENCE;
		public BigDecimal R32_MONTH_END;
		public BigDecimal R32_AVERAGE;
		public String R33_PRODUCT;
		public String R33_CROSS_REFERENCE;
		public BigDecimal R33_MONTH_END;
		public BigDecimal R33_AVERAGE;
		public String R34_PRODUCT;
		public String R34_CROSS_REFERENCE;
		public BigDecimal R34_MONTH_END;
		public BigDecimal R34_AVERAGE;
		public String R35_PRODUCT;
		public String R35_CROSS_REFERENCE;
		public BigDecimal R35_MONTH_END;
		public BigDecimal R35_AVERAGE;
		public String R36_PRODUCT;
		public String R36_CROSS_REFERENCE;
		public BigDecimal R36_MONTH_END;
		public BigDecimal R36_AVERAGE;
		public String R37_PRODUCT;
		public String R37_CROSS_REFERENCE;
		public BigDecimal R37_MONTH_END;
		public BigDecimal R37_AVERAGE;
		public String R38_PRODUCT;
		public String R38_CROSS_REFERENCE;
		public BigDecimal R38_MONTH_END;
		public BigDecimal R38_AVERAGE;
		public String R39_PRODUCT;
		public String R39_CROSS_REFERENCE;
		public BigDecimal R39_MONTH_END;
		public BigDecimal R39_AVERAGE;
		public String R40_PRODUCT;
		public String R40_CROSS_REFERENCE;
		public BigDecimal R40_MONTH_END;
		public BigDecimal R40_AVERAGE;
		public String R41_PRODUCT;
		public String R41_CROSS_REFERENCE;
		public BigDecimal R41_MONTH_END;
		public BigDecimal R41_AVERAGE;
		public String R42_PRODUCT;
		public String R42_CROSS_REFERENCE;
		public BigDecimal R42_MONTH_END;
		public BigDecimal R42_AVERAGE;
		public String R43_PRODUCT;
		public String R43_CROSS_REFERENCE;
		public BigDecimal R43_MONTH_END;
		public BigDecimal R43_AVERAGE;
		public String R44_PRODUCT;
		public String R44_CROSS_REFERENCE;
		public BigDecimal R44_MONTH_END;
		public BigDecimal R44_AVERAGE;
		public String R45_PRODUCT;
		public String R45_CROSS_REFERENCE;
		public BigDecimal R45_MONTH_END;
		public BigDecimal R45_AVERAGE;
		public String R46_PRODUCT;
		public String R46_CROSS_REFERENCE;
		public BigDecimal R46_MONTH_END;
		public BigDecimal R46_AVERAGE;
		public String R47_PRODUCT;
		public String R47_CROSS_REFERENCE;
		public BigDecimal R47_MONTH_END;
		public BigDecimal R47_AVERAGE;
		public String R48_PRODUCT;
		public String R48_CROSS_REFERENCE;
		public BigDecimal R48_MONTH_END;
		public BigDecimal R48_AVERAGE;
		public String R49_PRODUCT;
		public String R49_CROSS_REFERENCE;
		public BigDecimal R49_MONTH_END;
		public BigDecimal R49_AVERAGE;
		public String R50_PRODUCT;
		public String R50_CROSS_REFERENCE;
		public BigDecimal R50_MONTH_END;
		public BigDecimal R50_AVERAGE;
		public String R51_PRODUCT;
		public String R51_CROSS_REFERENCE;
		public BigDecimal R51_MONTH_END;
		public BigDecimal R51_AVERAGE;
		public String R52_PRODUCT;
		public String R52_CROSS_REFERENCE;
		public BigDecimal R52_MONTH_END;
		public BigDecimal R52_AVERAGE;
		public String R53_PRODUCT;
		public String R53_CROSS_REFERENCE;
		public BigDecimal R53_MONTH_END;
		public BigDecimal R53_AVERAGE;
		public String R54_PRODUCT;
		public String R54_CROSS_REFERENCE;
		public BigDecimal R54_MONTH_END;
		public BigDecimal R54_AVERAGE;
		public String R55_PRODUCT;
		public String R55_CROSS_REFERENCE;
		public BigDecimal R55_MONTH_END;
		public BigDecimal R55_AVERAGE;
		public String R56_PRODUCT;
		public String R56_CROSS_REFERENCE;
		public BigDecimal R56_MONTH_END;
		public BigDecimal R56_AVERAGE;
		public String R57_PRODUCT;
		public String R57_CROSS_REFERENCE;
		public BigDecimal R57_MONTH_END;
		public BigDecimal R57_AVERAGE;
		public String R58_PRODUCT;
		public String R58_CROSS_REFERENCE;
		public BigDecimal R58_MONTH_END;
		public BigDecimal R58_AVERAGE;
		public String R59_PRODUCT;
		public String R59_CROSS_REFERENCE;
		public BigDecimal R59_MONTH_END;
		public BigDecimal R59_AVERAGE;
		public String R60_PRODUCT;
		public String R60_CROSS_REFERENCE;
		public BigDecimal R60_MONTH_END;
		public BigDecimal R60_AVERAGE;
		public String R61_PRODUCT;
		public String R61_CROSS_REFERENCE;
		public BigDecimal R61_MONTH_END;
		public BigDecimal R61_AVERAGE;
		public String R62_PRODUCT;
		public String R62_CROSS_REFERENCE;
		public BigDecimal R62_MONTH_END;
		public BigDecimal R62_AVERAGE;
		public String R63_PRODUCT;
		public String R63_CROSS_REFERENCE;
		public BigDecimal R63_MONTH_END;
		public BigDecimal R63_AVERAGE;
		public String R64_PRODUCT;
		public String R64_CROSS_REFERENCE;
		public BigDecimal R64_MONTH_END;
		public BigDecimal R64_AVERAGE;
		public String R65_PRODUCT;
		public String R65_CROSS_REFERENCE;
		public BigDecimal R65_MONTH_END;
		public BigDecimal R65_AVERAGE;
		public String R66_PRODUCT;
		public String R66_CROSS_REFERENCE;
		public BigDecimal R66_MONTH_END;
		public BigDecimal R66_AVERAGE;
		public String R67_PRODUCT;
		public String R67_CROSS_REFERENCE;
		public BigDecimal R67_MONTH_END;
		public BigDecimal R67_AVERAGE;
		public String R68_PRODUCT;
		public String R68_CROSS_REFERENCE;
		public BigDecimal R68_MONTH_END;
		public BigDecimal R68_AVERAGE;
		public String R69_PRODUCT;
		public String R69_CROSS_REFERENCE;
		public BigDecimal R69_MONTH_END;
		public BigDecimal R69_AVERAGE;
		public String R70_PRODUCT;
		public String R70_CROSS_REFERENCE;
		public BigDecimal R70_MONTH_END;
		public BigDecimal R70_AVERAGE;
		public String R71_PRODUCT;
		public String R71_CROSS_REFERENCE;
		public BigDecimal R71_MONTH_END;
		public BigDecimal R71_AVERAGE;
		public String R72_PRODUCT;
		public String R72_CROSS_REFERENCE;
		public BigDecimal R72_MONTH_END;
		public BigDecimal R72_AVERAGE;
		public String R73_PRODUCT;
		public String R73_CROSS_REFERENCE;
		public BigDecimal R73_MONTH_END;
		public BigDecimal R73_AVERAGE;
		public String R74_PRODUCT;
		public String R74_CROSS_REFERENCE;
		public BigDecimal R74_MONTH_END;
		public BigDecimal R74_AVERAGE;
		public String R75_PRODUCT;
		public String R75_CROSS_REFERENCE;
		public BigDecimal R75_MONTH_END;
		public BigDecimal R75_AVERAGE;
		public String R76_PRODUCT;
		public String R76_CROSS_REFERENCE;
		public BigDecimal R76_MONTH_END;
		public BigDecimal R76_AVERAGE;
		public String R77_PRODUCT;
		public String R77_CROSS_REFERENCE;
		public BigDecimal R77_MONTH_END;
		public BigDecimal R77_AVERAGE;
		public String R78_PRODUCT;
		public String R78_CROSS_REFERENCE;
		public BigDecimal R78_MONTH_END;
		public BigDecimal R78_AVERAGE;
		public String R79_PRODUCT;
		public String R79_CROSS_REFERENCE;
		public BigDecimal R79_MONTH_END;
		public BigDecimal R79_AVERAGE;
		public String R80_PRODUCT;
		public String R80_CROSS_REFERENCE;
		public BigDecimal R80_MONTH_END;
		public BigDecimal R80_AVERAGE;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		public Date REPORT_DATE;
		@Id
		public BigDecimal REPORT_VERSION;
		@Column(name = "REPORT_RESUBDATE")

		private Date reportResubDate;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public String getR10_CROSS_REFERENCE() {
			return R10_CROSS_REFERENCE;
		}

		public void setR10_CROSS_REFERENCE(String r10_CROSS_REFERENCE) {
			R10_CROSS_REFERENCE = r10_CROSS_REFERENCE;
		}

		public BigDecimal getR10_MONTH_END() {
			return R10_MONTH_END;
		}

		public void setR10_MONTH_END(BigDecimal r10_MONTH_END) {
			R10_MONTH_END = r10_MONTH_END;
		}

		public BigDecimal getR10_AVERAGE() {
			return R10_AVERAGE;
		}

		public void setR10_AVERAGE(BigDecimal r10_AVERAGE) {
			R10_AVERAGE = r10_AVERAGE;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public String getR11_CROSS_REFERENCE() {
			return R11_CROSS_REFERENCE;
		}

		public void setR11_CROSS_REFERENCE(String r11_CROSS_REFERENCE) {
			R11_CROSS_REFERENCE = r11_CROSS_REFERENCE;
		}

		public BigDecimal getR11_MONTH_END() {
			return R11_MONTH_END;
		}

		public void setR11_MONTH_END(BigDecimal r11_MONTH_END) {
			R11_MONTH_END = r11_MONTH_END;
		}

		public BigDecimal getR11_AVERAGE() {
			return R11_AVERAGE;
		}

		public void setR11_AVERAGE(BigDecimal r11_AVERAGE) {
			R11_AVERAGE = r11_AVERAGE;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public String getR12_CROSS_REFERENCE() {
			return R12_CROSS_REFERENCE;
		}

		public void setR12_CROSS_REFERENCE(String r12_CROSS_REFERENCE) {
			R12_CROSS_REFERENCE = r12_CROSS_REFERENCE;
		}

		public BigDecimal getR12_MONTH_END() {
			return R12_MONTH_END;
		}

		public void setR12_MONTH_END(BigDecimal r12_MONTH_END) {
			R12_MONTH_END = r12_MONTH_END;
		}

		public BigDecimal getR12_AVERAGE() {
			return R12_AVERAGE;
		}

		public void setR12_AVERAGE(BigDecimal r12_AVERAGE) {
			R12_AVERAGE = r12_AVERAGE;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public String getR13_CROSS_REFERENCE() {
			return R13_CROSS_REFERENCE;
		}

		public void setR13_CROSS_REFERENCE(String r13_CROSS_REFERENCE) {
			R13_CROSS_REFERENCE = r13_CROSS_REFERENCE;
		}

		public BigDecimal getR13_MONTH_END() {
			return R13_MONTH_END;
		}

		public void setR13_MONTH_END(BigDecimal r13_MONTH_END) {
			R13_MONTH_END = r13_MONTH_END;
		}

		public BigDecimal getR13_AVERAGE() {
			return R13_AVERAGE;
		}

		public void setR13_AVERAGE(BigDecimal r13_AVERAGE) {
			R13_AVERAGE = r13_AVERAGE;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public String getR14_CROSS_REFERENCE() {
			return R14_CROSS_REFERENCE;
		}

		public void setR14_CROSS_REFERENCE(String r14_CROSS_REFERENCE) {
			R14_CROSS_REFERENCE = r14_CROSS_REFERENCE;
		}

		public BigDecimal getR14_MONTH_END() {
			return R14_MONTH_END;
		}

		public void setR14_MONTH_END(BigDecimal r14_MONTH_END) {
			R14_MONTH_END = r14_MONTH_END;
		}

		public BigDecimal getR14_AVERAGE() {
			return R14_AVERAGE;
		}

		public void setR14_AVERAGE(BigDecimal r14_AVERAGE) {
			R14_AVERAGE = r14_AVERAGE;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public String getR15_CROSS_REFERENCE() {
			return R15_CROSS_REFERENCE;
		}

		public void setR15_CROSS_REFERENCE(String r15_CROSS_REFERENCE) {
			R15_CROSS_REFERENCE = r15_CROSS_REFERENCE;
		}

		public BigDecimal getR15_MONTH_END() {
			return R15_MONTH_END;
		}

		public void setR15_MONTH_END(BigDecimal r15_MONTH_END) {
			R15_MONTH_END = r15_MONTH_END;
		}

		public BigDecimal getR15_AVERAGE() {
			return R15_AVERAGE;
		}

		public void setR15_AVERAGE(BigDecimal r15_AVERAGE) {
			R15_AVERAGE = r15_AVERAGE;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public String getR16_CROSS_REFERENCE() {
			return R16_CROSS_REFERENCE;
		}

		public void setR16_CROSS_REFERENCE(String r16_CROSS_REFERENCE) {
			R16_CROSS_REFERENCE = r16_CROSS_REFERENCE;
		}

		public BigDecimal getR16_MONTH_END() {
			return R16_MONTH_END;
		}

		public void setR16_MONTH_END(BigDecimal r16_MONTH_END) {
			R16_MONTH_END = r16_MONTH_END;
		}

		public BigDecimal getR16_AVERAGE() {
			return R16_AVERAGE;
		}

		public void setR16_AVERAGE(BigDecimal r16_AVERAGE) {
			R16_AVERAGE = r16_AVERAGE;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public String getR17_CROSS_REFERENCE() {
			return R17_CROSS_REFERENCE;
		}

		public void setR17_CROSS_REFERENCE(String r17_CROSS_REFERENCE) {
			R17_CROSS_REFERENCE = r17_CROSS_REFERENCE;
		}

		public BigDecimal getR17_MONTH_END() {
			return R17_MONTH_END;
		}

		public void setR17_MONTH_END(BigDecimal r17_MONTH_END) {
			R17_MONTH_END = r17_MONTH_END;
		}

		public BigDecimal getR17_AVERAGE() {
			return R17_AVERAGE;
		}

		public void setR17_AVERAGE(BigDecimal r17_AVERAGE) {
			R17_AVERAGE = r17_AVERAGE;
		}

		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public String getR18_CROSS_REFERENCE() {
			return R18_CROSS_REFERENCE;
		}

		public void setR18_CROSS_REFERENCE(String r18_CROSS_REFERENCE) {
			R18_CROSS_REFERENCE = r18_CROSS_REFERENCE;
		}

		public BigDecimal getR18_MONTH_END() {
			return R18_MONTH_END;
		}

		public void setR18_MONTH_END(BigDecimal r18_MONTH_END) {
			R18_MONTH_END = r18_MONTH_END;
		}

		public BigDecimal getR18_AVERAGE() {
			return R18_AVERAGE;
		}

		public void setR18_AVERAGE(BigDecimal r18_AVERAGE) {
			R18_AVERAGE = r18_AVERAGE;
		}

		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public String getR19_CROSS_REFERENCE() {
			return R19_CROSS_REFERENCE;
		}

		public void setR19_CROSS_REFERENCE(String r19_CROSS_REFERENCE) {
			R19_CROSS_REFERENCE = r19_CROSS_REFERENCE;
		}

		public BigDecimal getR19_MONTH_END() {
			return R19_MONTH_END;
		}

		public void setR19_MONTH_END(BigDecimal r19_MONTH_END) {
			R19_MONTH_END = r19_MONTH_END;
		}

		public BigDecimal getR19_AVERAGE() {
			return R19_AVERAGE;
		}

		public void setR19_AVERAGE(BigDecimal r19_AVERAGE) {
			R19_AVERAGE = r19_AVERAGE;
		}

		public String getR20_PRODUCT() {
			return R20_PRODUCT;
		}

		public void setR20_PRODUCT(String r20_PRODUCT) {
			R20_PRODUCT = r20_PRODUCT;
		}

		public String getR20_CROSS_REFERENCE() {
			return R20_CROSS_REFERENCE;
		}

		public void setR20_CROSS_REFERENCE(String r20_CROSS_REFERENCE) {
			R20_CROSS_REFERENCE = r20_CROSS_REFERENCE;
		}

		public BigDecimal getR20_MONTH_END() {
			return R20_MONTH_END;
		}

		public void setR20_MONTH_END(BigDecimal r20_MONTH_END) {
			R20_MONTH_END = r20_MONTH_END;
		}

		public BigDecimal getR20_AVERAGE() {
			return R20_AVERAGE;
		}

		public void setR20_AVERAGE(BigDecimal r20_AVERAGE) {
			R20_AVERAGE = r20_AVERAGE;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			R21_PRODUCT = r21_PRODUCT;
		}

		public String getR21_CROSS_REFERENCE() {
			return R21_CROSS_REFERENCE;
		}

		public void setR21_CROSS_REFERENCE(String r21_CROSS_REFERENCE) {
			R21_CROSS_REFERENCE = r21_CROSS_REFERENCE;
		}

		public BigDecimal getR21_MONTH_END() {
			return R21_MONTH_END;
		}

		public void setR21_MONTH_END(BigDecimal r21_MONTH_END) {
			R21_MONTH_END = r21_MONTH_END;
		}

		public BigDecimal getR21_AVERAGE() {
			return R21_AVERAGE;
		}

		public void setR21_AVERAGE(BigDecimal r21_AVERAGE) {
			R21_AVERAGE = r21_AVERAGE;
		}

		public String getR22_PRODUCT() {
			return R22_PRODUCT;
		}

		public void setR22_PRODUCT(String r22_PRODUCT) {
			R22_PRODUCT = r22_PRODUCT;
		}

		public String getR22_CROSS_REFERENCE() {
			return R22_CROSS_REFERENCE;
		}

		public void setR22_CROSS_REFERENCE(String r22_CROSS_REFERENCE) {
			R22_CROSS_REFERENCE = r22_CROSS_REFERENCE;
		}

		public BigDecimal getR22_MONTH_END() {
			return R22_MONTH_END;
		}

		public void setR22_MONTH_END(BigDecimal r22_MONTH_END) {
			R22_MONTH_END = r22_MONTH_END;
		}

		public BigDecimal getR22_AVERAGE() {
			return R22_AVERAGE;
		}

		public void setR22_AVERAGE(BigDecimal r22_AVERAGE) {
			R22_AVERAGE = r22_AVERAGE;
		}

		public String getR23_PRODUCT() {
			return R23_PRODUCT;
		}

		public void setR23_PRODUCT(String r23_PRODUCT) {
			R23_PRODUCT = r23_PRODUCT;
		}

		public String getR23_CROSS_REFERENCE() {
			return R23_CROSS_REFERENCE;
		}

		public void setR23_CROSS_REFERENCE(String r23_CROSS_REFERENCE) {
			R23_CROSS_REFERENCE = r23_CROSS_REFERENCE;
		}

		public BigDecimal getR23_MONTH_END() {
			return R23_MONTH_END;
		}

		public void setR23_MONTH_END(BigDecimal r23_MONTH_END) {
			R23_MONTH_END = r23_MONTH_END;
		}

		public BigDecimal getR23_AVERAGE() {
			return R23_AVERAGE;
		}

		public void setR23_AVERAGE(BigDecimal r23_AVERAGE) {
			R23_AVERAGE = r23_AVERAGE;
		}

		public String getR24_PRODUCT() {
			return R24_PRODUCT;
		}

		public void setR24_PRODUCT(String r24_PRODUCT) {
			R24_PRODUCT = r24_PRODUCT;
		}

		public String getR24_CROSS_REFERENCE() {
			return R24_CROSS_REFERENCE;
		}

		public void setR24_CROSS_REFERENCE(String r24_CROSS_REFERENCE) {
			R24_CROSS_REFERENCE = r24_CROSS_REFERENCE;
		}

		public BigDecimal getR24_MONTH_END() {
			return R24_MONTH_END;
		}

		public void setR24_MONTH_END(BigDecimal r24_MONTH_END) {
			R24_MONTH_END = r24_MONTH_END;
		}

		public BigDecimal getR24_AVERAGE() {
			return R24_AVERAGE;
		}

		public void setR24_AVERAGE(BigDecimal r24_AVERAGE) {
			R24_AVERAGE = r24_AVERAGE;
		}

		public String getR25_PRODUCT() {
			return R25_PRODUCT;
		}

		public void setR25_PRODUCT(String r25_PRODUCT) {
			R25_PRODUCT = r25_PRODUCT;
		}

		public String getR25_CROSS_REFERENCE() {
			return R25_CROSS_REFERENCE;
		}

		public void setR25_CROSS_REFERENCE(String r25_CROSS_REFERENCE) {
			R25_CROSS_REFERENCE = r25_CROSS_REFERENCE;
		}

		public BigDecimal getR25_MONTH_END() {
			return R25_MONTH_END;
		}

		public void setR25_MONTH_END(BigDecimal r25_MONTH_END) {
			R25_MONTH_END = r25_MONTH_END;
		}

		public BigDecimal getR25_AVERAGE() {
			return R25_AVERAGE;
		}

		public void setR25_AVERAGE(BigDecimal r25_AVERAGE) {
			R25_AVERAGE = r25_AVERAGE;
		}

		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public String getR26_CROSS_REFERENCE() {
			return R26_CROSS_REFERENCE;
		}

		public void setR26_CROSS_REFERENCE(String r26_CROSS_REFERENCE) {
			R26_CROSS_REFERENCE = r26_CROSS_REFERENCE;
		}

		public BigDecimal getR26_MONTH_END() {
			return R26_MONTH_END;
		}

		public void setR26_MONTH_END(BigDecimal r26_MONTH_END) {
			R26_MONTH_END = r26_MONTH_END;
		}

		public BigDecimal getR26_AVERAGE() {
			return R26_AVERAGE;
		}

		public void setR26_AVERAGE(BigDecimal r26_AVERAGE) {
			R26_AVERAGE = r26_AVERAGE;
		}

		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public String getR27_CROSS_REFERENCE() {
			return R27_CROSS_REFERENCE;
		}

		public void setR27_CROSS_REFERENCE(String r27_CROSS_REFERENCE) {
			R27_CROSS_REFERENCE = r27_CROSS_REFERENCE;
		}

		public BigDecimal getR27_MONTH_END() {
			return R27_MONTH_END;
		}

		public void setR27_MONTH_END(BigDecimal r27_MONTH_END) {
			R27_MONTH_END = r27_MONTH_END;
		}

		public BigDecimal getR27_AVERAGE() {
			return R27_AVERAGE;
		}

		public void setR27_AVERAGE(BigDecimal r27_AVERAGE) {
			R27_AVERAGE = r27_AVERAGE;
		}

		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public String getR28_CROSS_REFERENCE() {
			return R28_CROSS_REFERENCE;
		}

		public void setR28_CROSS_REFERENCE(String r28_CROSS_REFERENCE) {
			R28_CROSS_REFERENCE = r28_CROSS_REFERENCE;
		}

		public BigDecimal getR28_MONTH_END() {
			return R28_MONTH_END;
		}

		public void setR28_MONTH_END(BigDecimal r28_MONTH_END) {
			R28_MONTH_END = r28_MONTH_END;
		}

		public BigDecimal getR28_AVERAGE() {
			return R28_AVERAGE;
		}

		public void setR28_AVERAGE(BigDecimal r28_AVERAGE) {
			R28_AVERAGE = r28_AVERAGE;
		}

		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public String getR29_CROSS_REFERENCE() {
			return R29_CROSS_REFERENCE;
		}

		public void setR29_CROSS_REFERENCE(String r29_CROSS_REFERENCE) {
			R29_CROSS_REFERENCE = r29_CROSS_REFERENCE;
		}

		public BigDecimal getR29_MONTH_END() {
			return R29_MONTH_END;
		}

		public void setR29_MONTH_END(BigDecimal r29_MONTH_END) {
			R29_MONTH_END = r29_MONTH_END;
		}

		public BigDecimal getR29_AVERAGE() {
			return R29_AVERAGE;
		}

		public void setR29_AVERAGE(BigDecimal r29_AVERAGE) {
			R29_AVERAGE = r29_AVERAGE;
		}

		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public String getR30_CROSS_REFERENCE() {
			return R30_CROSS_REFERENCE;
		}

		public void setR30_CROSS_REFERENCE(String r30_CROSS_REFERENCE) {
			R30_CROSS_REFERENCE = r30_CROSS_REFERENCE;
		}

		public BigDecimal getR30_MONTH_END() {
			return R30_MONTH_END;
		}

		public void setR30_MONTH_END(BigDecimal r30_MONTH_END) {
			R30_MONTH_END = r30_MONTH_END;
		}

		public BigDecimal getR30_AVERAGE() {
			return R30_AVERAGE;
		}

		public void setR30_AVERAGE(BigDecimal r30_AVERAGE) {
			R30_AVERAGE = r30_AVERAGE;
		}

		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public String getR31_CROSS_REFERENCE() {
			return R31_CROSS_REFERENCE;
		}

		public void setR31_CROSS_REFERENCE(String r31_CROSS_REFERENCE) {
			R31_CROSS_REFERENCE = r31_CROSS_REFERENCE;
		}

		public BigDecimal getR31_MONTH_END() {
			return R31_MONTH_END;
		}

		public void setR31_MONTH_END(BigDecimal r31_MONTH_END) {
			R31_MONTH_END = r31_MONTH_END;
		}

		public BigDecimal getR31_AVERAGE() {
			return R31_AVERAGE;
		}

		public void setR31_AVERAGE(BigDecimal r31_AVERAGE) {
			R31_AVERAGE = r31_AVERAGE;
		}

		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}

		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}

		public String getR32_CROSS_REFERENCE() {
			return R32_CROSS_REFERENCE;
		}

		public void setR32_CROSS_REFERENCE(String r32_CROSS_REFERENCE) {
			R32_CROSS_REFERENCE = r32_CROSS_REFERENCE;
		}

		public BigDecimal getR32_MONTH_END() {
			return R32_MONTH_END;
		}

		public void setR32_MONTH_END(BigDecimal r32_MONTH_END) {
			R32_MONTH_END = r32_MONTH_END;
		}

		public BigDecimal getR32_AVERAGE() {
			return R32_AVERAGE;
		}

		public void setR32_AVERAGE(BigDecimal r32_AVERAGE) {
			R32_AVERAGE = r32_AVERAGE;
		}

		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}

		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}

		public String getR33_CROSS_REFERENCE() {
			return R33_CROSS_REFERENCE;
		}

		public void setR33_CROSS_REFERENCE(String r33_CROSS_REFERENCE) {
			R33_CROSS_REFERENCE = r33_CROSS_REFERENCE;
		}

		public BigDecimal getR33_MONTH_END() {
			return R33_MONTH_END;
		}

		public void setR33_MONTH_END(BigDecimal r33_MONTH_END) {
			R33_MONTH_END = r33_MONTH_END;
		}

		public BigDecimal getR33_AVERAGE() {
			return R33_AVERAGE;
		}

		public void setR33_AVERAGE(BigDecimal r33_AVERAGE) {
			R33_AVERAGE = r33_AVERAGE;
		}

		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}

		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}

		public String getR34_CROSS_REFERENCE() {
			return R34_CROSS_REFERENCE;
		}

		public void setR34_CROSS_REFERENCE(String r34_CROSS_REFERENCE) {
			R34_CROSS_REFERENCE = r34_CROSS_REFERENCE;
		}

		public BigDecimal getR34_MONTH_END() {
			return R34_MONTH_END;
		}

		public void setR34_MONTH_END(BigDecimal r34_MONTH_END) {
			R34_MONTH_END = r34_MONTH_END;
		}

		public BigDecimal getR34_AVERAGE() {
			return R34_AVERAGE;
		}

		public void setR34_AVERAGE(BigDecimal r34_AVERAGE) {
			R34_AVERAGE = r34_AVERAGE;
		}

		public String getR35_PRODUCT() {
			return R35_PRODUCT;
		}

		public void setR35_PRODUCT(String r35_PRODUCT) {
			R35_PRODUCT = r35_PRODUCT;
		}

		public String getR35_CROSS_REFERENCE() {
			return R35_CROSS_REFERENCE;
		}

		public void setR35_CROSS_REFERENCE(String r35_CROSS_REFERENCE) {
			R35_CROSS_REFERENCE = r35_CROSS_REFERENCE;
		}

		public BigDecimal getR35_MONTH_END() {
			return R35_MONTH_END;
		}

		public void setR35_MONTH_END(BigDecimal r35_MONTH_END) {
			R35_MONTH_END = r35_MONTH_END;
		}

		public BigDecimal getR35_AVERAGE() {
			return R35_AVERAGE;
		}

		public void setR35_AVERAGE(BigDecimal r35_AVERAGE) {
			R35_AVERAGE = r35_AVERAGE;
		}

		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public String getR36_CROSS_REFERENCE() {
			return R36_CROSS_REFERENCE;
		}

		public void setR36_CROSS_REFERENCE(String r36_CROSS_REFERENCE) {
			R36_CROSS_REFERENCE = r36_CROSS_REFERENCE;
		}

		public BigDecimal getR36_MONTH_END() {
			return R36_MONTH_END;
		}

		public void setR36_MONTH_END(BigDecimal r36_MONTH_END) {
			R36_MONTH_END = r36_MONTH_END;
		}

		public BigDecimal getR36_AVERAGE() {
			return R36_AVERAGE;
		}

		public void setR36_AVERAGE(BigDecimal r36_AVERAGE) {
			R36_AVERAGE = r36_AVERAGE;
		}

		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public String getR37_CROSS_REFERENCE() {
			return R37_CROSS_REFERENCE;
		}

		public void setR37_CROSS_REFERENCE(String r37_CROSS_REFERENCE) {
			R37_CROSS_REFERENCE = r37_CROSS_REFERENCE;
		}

		public BigDecimal getR37_MONTH_END() {
			return R37_MONTH_END;
		}

		public void setR37_MONTH_END(BigDecimal r37_MONTH_END) {
			R37_MONTH_END = r37_MONTH_END;
		}

		public BigDecimal getR37_AVERAGE() {
			return R37_AVERAGE;
		}

		public void setR37_AVERAGE(BigDecimal r37_AVERAGE) {
			R37_AVERAGE = r37_AVERAGE;
		}

		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public String getR38_CROSS_REFERENCE() {
			return R38_CROSS_REFERENCE;
		}

		public void setR38_CROSS_REFERENCE(String r38_CROSS_REFERENCE) {
			R38_CROSS_REFERENCE = r38_CROSS_REFERENCE;
		}

		public BigDecimal getR38_MONTH_END() {
			return R38_MONTH_END;
		}

		public void setR38_MONTH_END(BigDecimal r38_MONTH_END) {
			R38_MONTH_END = r38_MONTH_END;
		}

		public BigDecimal getR38_AVERAGE() {
			return R38_AVERAGE;
		}

		public void setR38_AVERAGE(BigDecimal r38_AVERAGE) {
			R38_AVERAGE = r38_AVERAGE;
		}

		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public String getR39_CROSS_REFERENCE() {
			return R39_CROSS_REFERENCE;
		}

		public void setR39_CROSS_REFERENCE(String r39_CROSS_REFERENCE) {
			R39_CROSS_REFERENCE = r39_CROSS_REFERENCE;
		}

		public BigDecimal getR39_MONTH_END() {
			return R39_MONTH_END;
		}

		public void setR39_MONTH_END(BigDecimal r39_MONTH_END) {
			R39_MONTH_END = r39_MONTH_END;
		}

		public BigDecimal getR39_AVERAGE() {
			return R39_AVERAGE;
		}

		public void setR39_AVERAGE(BigDecimal r39_AVERAGE) {
			R39_AVERAGE = r39_AVERAGE;
		}

		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public String getR40_CROSS_REFERENCE() {
			return R40_CROSS_REFERENCE;
		}

		public void setR40_CROSS_REFERENCE(String r40_CROSS_REFERENCE) {
			R40_CROSS_REFERENCE = r40_CROSS_REFERENCE;
		}

		public BigDecimal getR40_MONTH_END() {
			return R40_MONTH_END;
		}

		public void setR40_MONTH_END(BigDecimal r40_MONTH_END) {
			R40_MONTH_END = r40_MONTH_END;
		}

		public BigDecimal getR40_AVERAGE() {
			return R40_AVERAGE;
		}

		public void setR40_AVERAGE(BigDecimal r40_AVERAGE) {
			R40_AVERAGE = r40_AVERAGE;
		}

		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public String getR41_CROSS_REFERENCE() {
			return R41_CROSS_REFERENCE;
		}

		public void setR41_CROSS_REFERENCE(String r41_CROSS_REFERENCE) {
			R41_CROSS_REFERENCE = r41_CROSS_REFERENCE;
		}

		public BigDecimal getR41_MONTH_END() {
			return R41_MONTH_END;
		}

		public void setR41_MONTH_END(BigDecimal r41_MONTH_END) {
			R41_MONTH_END = r41_MONTH_END;
		}

		public BigDecimal getR41_AVERAGE() {
			return R41_AVERAGE;
		}

		public void setR41_AVERAGE(BigDecimal r41_AVERAGE) {
			R41_AVERAGE = r41_AVERAGE;
		}

		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}

		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}

		public String getR42_CROSS_REFERENCE() {
			return R42_CROSS_REFERENCE;
		}

		public void setR42_CROSS_REFERENCE(String r42_CROSS_REFERENCE) {
			R42_CROSS_REFERENCE = r42_CROSS_REFERENCE;
		}

		public BigDecimal getR42_MONTH_END() {
			return R42_MONTH_END;
		}

		public void setR42_MONTH_END(BigDecimal r42_MONTH_END) {
			R42_MONTH_END = r42_MONTH_END;
		}

		public BigDecimal getR42_AVERAGE() {
			return R42_AVERAGE;
		}

		public void setR42_AVERAGE(BigDecimal r42_AVERAGE) {
			R42_AVERAGE = r42_AVERAGE;
		}

		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}

		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}

		public String getR43_CROSS_REFERENCE() {
			return R43_CROSS_REFERENCE;
		}

		public void setR43_CROSS_REFERENCE(String r43_CROSS_REFERENCE) {
			R43_CROSS_REFERENCE = r43_CROSS_REFERENCE;
		}

		public BigDecimal getR43_MONTH_END() {
			return R43_MONTH_END;
		}

		public void setR43_MONTH_END(BigDecimal r43_MONTH_END) {
			R43_MONTH_END = r43_MONTH_END;
		}

		public BigDecimal getR43_AVERAGE() {
			return R43_AVERAGE;
		}

		public void setR43_AVERAGE(BigDecimal r43_AVERAGE) {
			R43_AVERAGE = r43_AVERAGE;
		}

		public String getR44_PRODUCT() {
			return R44_PRODUCT;
		}

		public void setR44_PRODUCT(String r44_PRODUCT) {
			R44_PRODUCT = r44_PRODUCT;
		}

		public String getR44_CROSS_REFERENCE() {
			return R44_CROSS_REFERENCE;
		}

		public void setR44_CROSS_REFERENCE(String r44_CROSS_REFERENCE) {
			R44_CROSS_REFERENCE = r44_CROSS_REFERENCE;
		}

		public BigDecimal getR44_MONTH_END() {
			return R44_MONTH_END;
		}

		public void setR44_MONTH_END(BigDecimal r44_MONTH_END) {
			R44_MONTH_END = r44_MONTH_END;
		}

		public BigDecimal getR44_AVERAGE() {
			return R44_AVERAGE;
		}

		public void setR44_AVERAGE(BigDecimal r44_AVERAGE) {
			R44_AVERAGE = r44_AVERAGE;
		}

		public String getR45_PRODUCT() {
			return R45_PRODUCT;
		}

		public void setR45_PRODUCT(String r45_PRODUCT) {
			R45_PRODUCT = r45_PRODUCT;
		}

		public String getR45_CROSS_REFERENCE() {
			return R45_CROSS_REFERENCE;
		}

		public void setR45_CROSS_REFERENCE(String r45_CROSS_REFERENCE) {
			R45_CROSS_REFERENCE = r45_CROSS_REFERENCE;
		}

		public BigDecimal getR45_MONTH_END() {
			return R45_MONTH_END;
		}

		public void setR45_MONTH_END(BigDecimal r45_MONTH_END) {
			R45_MONTH_END = r45_MONTH_END;
		}

		public BigDecimal getR45_AVERAGE() {
			return R45_AVERAGE;
		}

		public void setR45_AVERAGE(BigDecimal r45_AVERAGE) {
			R45_AVERAGE = r45_AVERAGE;
		}

		public String getR46_PRODUCT() {
			return R46_PRODUCT;
		}

		public void setR46_PRODUCT(String r46_PRODUCT) {
			R46_PRODUCT = r46_PRODUCT;
		}

		public String getR46_CROSS_REFERENCE() {
			return R46_CROSS_REFERENCE;
		}

		public void setR46_CROSS_REFERENCE(String r46_CROSS_REFERENCE) {
			R46_CROSS_REFERENCE = r46_CROSS_REFERENCE;
		}

		public BigDecimal getR46_MONTH_END() {
			return R46_MONTH_END;
		}

		public void setR46_MONTH_END(BigDecimal r46_MONTH_END) {
			R46_MONTH_END = r46_MONTH_END;
		}

		public BigDecimal getR46_AVERAGE() {
			return R46_AVERAGE;
		}

		public void setR46_AVERAGE(BigDecimal r46_AVERAGE) {
			R46_AVERAGE = r46_AVERAGE;
		}

		public String getR47_PRODUCT() {
			return R47_PRODUCT;
		}

		public void setR47_PRODUCT(String r47_PRODUCT) {
			R47_PRODUCT = r47_PRODUCT;
		}

		public String getR47_CROSS_REFERENCE() {
			return R47_CROSS_REFERENCE;
		}

		public void setR47_CROSS_REFERENCE(String r47_CROSS_REFERENCE) {
			R47_CROSS_REFERENCE = r47_CROSS_REFERENCE;
		}

		public BigDecimal getR47_MONTH_END() {
			return R47_MONTH_END;
		}

		public void setR47_MONTH_END(BigDecimal r47_MONTH_END) {
			R47_MONTH_END = r47_MONTH_END;
		}

		public BigDecimal getR47_AVERAGE() {
			return R47_AVERAGE;
		}

		public void setR47_AVERAGE(BigDecimal r47_AVERAGE) {
			R47_AVERAGE = r47_AVERAGE;
		}

		public String getR48_PRODUCT() {
			return R48_PRODUCT;
		}

		public void setR48_PRODUCT(String r48_PRODUCT) {
			R48_PRODUCT = r48_PRODUCT;
		}

		public String getR48_CROSS_REFERENCE() {
			return R48_CROSS_REFERENCE;
		}

		public void setR48_CROSS_REFERENCE(String r48_CROSS_REFERENCE) {
			R48_CROSS_REFERENCE = r48_CROSS_REFERENCE;
		}

		public BigDecimal getR48_MONTH_END() {
			return R48_MONTH_END;
		}

		public void setR48_MONTH_END(BigDecimal r48_MONTH_END) {
			R48_MONTH_END = r48_MONTH_END;
		}

		public BigDecimal getR48_AVERAGE() {
			return R48_AVERAGE;
		}

		public void setR48_AVERAGE(BigDecimal r48_AVERAGE) {
			R48_AVERAGE = r48_AVERAGE;
		}

		public String getR49_PRODUCT() {
			return R49_PRODUCT;
		}

		public void setR49_PRODUCT(String r49_PRODUCT) {
			R49_PRODUCT = r49_PRODUCT;
		}

		public String getR49_CROSS_REFERENCE() {
			return R49_CROSS_REFERENCE;
		}

		public void setR49_CROSS_REFERENCE(String r49_CROSS_REFERENCE) {
			R49_CROSS_REFERENCE = r49_CROSS_REFERENCE;
		}

		public BigDecimal getR49_MONTH_END() {
			return R49_MONTH_END;
		}

		public void setR49_MONTH_END(BigDecimal r49_MONTH_END) {
			R49_MONTH_END = r49_MONTH_END;
		}

		public BigDecimal getR49_AVERAGE() {
			return R49_AVERAGE;
		}

		public void setR49_AVERAGE(BigDecimal r49_AVERAGE) {
			R49_AVERAGE = r49_AVERAGE;
		}

		public String getR50_PRODUCT() {
			return R50_PRODUCT;
		}

		public void setR50_PRODUCT(String r50_PRODUCT) {
			R50_PRODUCT = r50_PRODUCT;
		}

		public String getR50_CROSS_REFERENCE() {
			return R50_CROSS_REFERENCE;
		}

		public void setR50_CROSS_REFERENCE(String r50_CROSS_REFERENCE) {
			R50_CROSS_REFERENCE = r50_CROSS_REFERENCE;
		}

		public BigDecimal getR50_MONTH_END() {
			return R50_MONTH_END;
		}

		public void setR50_MONTH_END(BigDecimal r50_MONTH_END) {
			R50_MONTH_END = r50_MONTH_END;
		}

		public BigDecimal getR50_AVERAGE() {
			return R50_AVERAGE;
		}

		public void setR50_AVERAGE(BigDecimal r50_AVERAGE) {
			R50_AVERAGE = r50_AVERAGE;
		}

		public String getR51_PRODUCT() {
			return R51_PRODUCT;
		}

		public void setR51_PRODUCT(String r51_PRODUCT) {
			R51_PRODUCT = r51_PRODUCT;
		}

		public String getR51_CROSS_REFERENCE() {
			return R51_CROSS_REFERENCE;
		}

		public void setR51_CROSS_REFERENCE(String r51_CROSS_REFERENCE) {
			R51_CROSS_REFERENCE = r51_CROSS_REFERENCE;
		}

		public BigDecimal getR51_MONTH_END() {
			return R51_MONTH_END;
		}

		public void setR51_MONTH_END(BigDecimal r51_MONTH_END) {
			R51_MONTH_END = r51_MONTH_END;
		}

		public BigDecimal getR51_AVERAGE() {
			return R51_AVERAGE;
		}

		public void setR51_AVERAGE(BigDecimal r51_AVERAGE) {
			R51_AVERAGE = r51_AVERAGE;
		}

		public String getR52_PRODUCT() {
			return R52_PRODUCT;
		}

		public void setR52_PRODUCT(String r52_PRODUCT) {
			R52_PRODUCT = r52_PRODUCT;
		}

		public String getR52_CROSS_REFERENCE() {
			return R52_CROSS_REFERENCE;
		}

		public void setR52_CROSS_REFERENCE(String r52_CROSS_REFERENCE) {
			R52_CROSS_REFERENCE = r52_CROSS_REFERENCE;
		}

		public BigDecimal getR52_MONTH_END() {
			return R52_MONTH_END;
		}

		public void setR52_MONTH_END(BigDecimal r52_MONTH_END) {
			R52_MONTH_END = r52_MONTH_END;
		}

		public BigDecimal getR52_AVERAGE() {
			return R52_AVERAGE;
		}

		public void setR52_AVERAGE(BigDecimal r52_AVERAGE) {
			R52_AVERAGE = r52_AVERAGE;
		}

		public String getR53_PRODUCT() {
			return R53_PRODUCT;
		}

		public void setR53_PRODUCT(String r53_PRODUCT) {
			R53_PRODUCT = r53_PRODUCT;
		}

		public String getR53_CROSS_REFERENCE() {
			return R53_CROSS_REFERENCE;
		}

		public void setR53_CROSS_REFERENCE(String r53_CROSS_REFERENCE) {
			R53_CROSS_REFERENCE = r53_CROSS_REFERENCE;
		}

		public BigDecimal getR53_MONTH_END() {
			return R53_MONTH_END;
		}

		public void setR53_MONTH_END(BigDecimal r53_MONTH_END) {
			R53_MONTH_END = r53_MONTH_END;
		}

		public BigDecimal getR53_AVERAGE() {
			return R53_AVERAGE;
		}

		public void setR53_AVERAGE(BigDecimal r53_AVERAGE) {
			R53_AVERAGE = r53_AVERAGE;
		}

		public String getR54_PRODUCT() {
			return R54_PRODUCT;
		}

		public void setR54_PRODUCT(String r54_PRODUCT) {
			R54_PRODUCT = r54_PRODUCT;
		}

		public String getR54_CROSS_REFERENCE() {
			return R54_CROSS_REFERENCE;
		}

		public void setR54_CROSS_REFERENCE(String r54_CROSS_REFERENCE) {
			R54_CROSS_REFERENCE = r54_CROSS_REFERENCE;
		}

		public BigDecimal getR54_MONTH_END() {
			return R54_MONTH_END;
		}

		public void setR54_MONTH_END(BigDecimal r54_MONTH_END) {
			R54_MONTH_END = r54_MONTH_END;
		}

		public BigDecimal getR54_AVERAGE() {
			return R54_AVERAGE;
		}

		public void setR54_AVERAGE(BigDecimal r54_AVERAGE) {
			R54_AVERAGE = r54_AVERAGE;
		}

		public String getR55_PRODUCT() {
			return R55_PRODUCT;
		}

		public void setR55_PRODUCT(String r55_PRODUCT) {
			R55_PRODUCT = r55_PRODUCT;
		}

		public String getR55_CROSS_REFERENCE() {
			return R55_CROSS_REFERENCE;
		}

		public void setR55_CROSS_REFERENCE(String r55_CROSS_REFERENCE) {
			R55_CROSS_REFERENCE = r55_CROSS_REFERENCE;
		}

		public BigDecimal getR55_MONTH_END() {
			return R55_MONTH_END;
		}

		public void setR55_MONTH_END(BigDecimal r55_MONTH_END) {
			R55_MONTH_END = r55_MONTH_END;
		}

		public BigDecimal getR55_AVERAGE() {
			return R55_AVERAGE;
		}

		public void setR55_AVERAGE(BigDecimal r55_AVERAGE) {
			R55_AVERAGE = r55_AVERAGE;
		}

		public String getR56_PRODUCT() {
			return R56_PRODUCT;
		}

		public void setR56_PRODUCT(String r56_PRODUCT) {
			R56_PRODUCT = r56_PRODUCT;
		}

		public String getR56_CROSS_REFERENCE() {
			return R56_CROSS_REFERENCE;
		}

		public void setR56_CROSS_REFERENCE(String r56_CROSS_REFERENCE) {
			R56_CROSS_REFERENCE = r56_CROSS_REFERENCE;
		}

		public BigDecimal getR56_MONTH_END() {
			return R56_MONTH_END;
		}

		public void setR56_MONTH_END(BigDecimal r56_MONTH_END) {
			R56_MONTH_END = r56_MONTH_END;
		}

		public BigDecimal getR56_AVERAGE() {
			return R56_AVERAGE;
		}

		public void setR56_AVERAGE(BigDecimal r56_AVERAGE) {
			R56_AVERAGE = r56_AVERAGE;
		}

		public String getR57_PRODUCT() {
			return R57_PRODUCT;
		}

		public void setR57_PRODUCT(String r57_PRODUCT) {
			R57_PRODUCT = r57_PRODUCT;
		}

		public String getR57_CROSS_REFERENCE() {
			return R57_CROSS_REFERENCE;
		}

		public void setR57_CROSS_REFERENCE(String r57_CROSS_REFERENCE) {
			R57_CROSS_REFERENCE = r57_CROSS_REFERENCE;
		}

		public BigDecimal getR57_MONTH_END() {
			return R57_MONTH_END;
		}

		public void setR57_MONTH_END(BigDecimal r57_MONTH_END) {
			R57_MONTH_END = r57_MONTH_END;
		}

		public BigDecimal getR57_AVERAGE() {
			return R57_AVERAGE;
		}

		public void setR57_AVERAGE(BigDecimal r57_AVERAGE) {
			R57_AVERAGE = r57_AVERAGE;
		}

		public String getR58_PRODUCT() {
			return R58_PRODUCT;
		}

		public void setR58_PRODUCT(String r58_PRODUCT) {
			R58_PRODUCT = r58_PRODUCT;
		}

		public String getR58_CROSS_REFERENCE() {
			return R58_CROSS_REFERENCE;
		}

		public void setR58_CROSS_REFERENCE(String r58_CROSS_REFERENCE) {
			R58_CROSS_REFERENCE = r58_CROSS_REFERENCE;
		}

		public BigDecimal getR58_MONTH_END() {
			return R58_MONTH_END;
		}

		public void setR58_MONTH_END(BigDecimal r58_MONTH_END) {
			R58_MONTH_END = r58_MONTH_END;
		}

		public BigDecimal getR58_AVERAGE() {
			return R58_AVERAGE;
		}

		public void setR58_AVERAGE(BigDecimal r58_AVERAGE) {
			R58_AVERAGE = r58_AVERAGE;
		}

		public String getR59_PRODUCT() {
			return R59_PRODUCT;
		}

		public void setR59_PRODUCT(String r59_PRODUCT) {
			R59_PRODUCT = r59_PRODUCT;
		}

		public String getR59_CROSS_REFERENCE() {
			return R59_CROSS_REFERENCE;
		}

		public void setR59_CROSS_REFERENCE(String r59_CROSS_REFERENCE) {
			R59_CROSS_REFERENCE = r59_CROSS_REFERENCE;
		}

		public BigDecimal getR59_MONTH_END() {
			return R59_MONTH_END;
		}

		public void setR59_MONTH_END(BigDecimal r59_MONTH_END) {
			R59_MONTH_END = r59_MONTH_END;
		}

		public BigDecimal getR59_AVERAGE() {
			return R59_AVERAGE;
		}

		public void setR59_AVERAGE(BigDecimal r59_AVERAGE) {
			R59_AVERAGE = r59_AVERAGE;
		}

		public String getR60_PRODUCT() {
			return R60_PRODUCT;
		}

		public void setR60_PRODUCT(String r60_PRODUCT) {
			R60_PRODUCT = r60_PRODUCT;
		}

		public String getR60_CROSS_REFERENCE() {
			return R60_CROSS_REFERENCE;
		}

		public void setR60_CROSS_REFERENCE(String r60_CROSS_REFERENCE) {
			R60_CROSS_REFERENCE = r60_CROSS_REFERENCE;
		}

		public BigDecimal getR60_MONTH_END() {
			return R60_MONTH_END;
		}

		public void setR60_MONTH_END(BigDecimal r60_MONTH_END) {
			R60_MONTH_END = r60_MONTH_END;
		}

		public BigDecimal getR60_AVERAGE() {
			return R60_AVERAGE;
		}

		public void setR60_AVERAGE(BigDecimal r60_AVERAGE) {
			R60_AVERAGE = r60_AVERAGE;
		}

		public String getR61_PRODUCT() {
			return R61_PRODUCT;
		}

		public void setR61_PRODUCT(String r61_PRODUCT) {
			R61_PRODUCT = r61_PRODUCT;
		}

		public String getR61_CROSS_REFERENCE() {
			return R61_CROSS_REFERENCE;
		}

		public void setR61_CROSS_REFERENCE(String r61_CROSS_REFERENCE) {
			R61_CROSS_REFERENCE = r61_CROSS_REFERENCE;
		}

		public BigDecimal getR61_MONTH_END() {
			return R61_MONTH_END;
		}

		public void setR61_MONTH_END(BigDecimal r61_MONTH_END) {
			R61_MONTH_END = r61_MONTH_END;
		}

		public BigDecimal getR61_AVERAGE() {
			return R61_AVERAGE;
		}

		public void setR61_AVERAGE(BigDecimal r61_AVERAGE) {
			R61_AVERAGE = r61_AVERAGE;
		}

		public String getR62_PRODUCT() {
			return R62_PRODUCT;
		}

		public void setR62_PRODUCT(String r62_PRODUCT) {
			R62_PRODUCT = r62_PRODUCT;
		}

		public String getR62_CROSS_REFERENCE() {
			return R62_CROSS_REFERENCE;
		}

		public void setR62_CROSS_REFERENCE(String r62_CROSS_REFERENCE) {
			R62_CROSS_REFERENCE = r62_CROSS_REFERENCE;
		}

		public BigDecimal getR62_MONTH_END() {
			return R62_MONTH_END;
		}

		public void setR62_MONTH_END(BigDecimal r62_MONTH_END) {
			R62_MONTH_END = r62_MONTH_END;
		}

		public BigDecimal getR62_AVERAGE() {
			return R62_AVERAGE;
		}

		public void setR62_AVERAGE(BigDecimal r62_AVERAGE) {
			R62_AVERAGE = r62_AVERAGE;
		}

		public String getR63_PRODUCT() {
			return R63_PRODUCT;
		}

		public void setR63_PRODUCT(String r63_PRODUCT) {
			R63_PRODUCT = r63_PRODUCT;
		}

		public String getR63_CROSS_REFERENCE() {
			return R63_CROSS_REFERENCE;
		}

		public void setR63_CROSS_REFERENCE(String r63_CROSS_REFERENCE) {
			R63_CROSS_REFERENCE = r63_CROSS_REFERENCE;
		}

		public BigDecimal getR63_MONTH_END() {
			return R63_MONTH_END;
		}

		public void setR63_MONTH_END(BigDecimal r63_MONTH_END) {
			R63_MONTH_END = r63_MONTH_END;
		}

		public BigDecimal getR63_AVERAGE() {
			return R63_AVERAGE;
		}

		public void setR63_AVERAGE(BigDecimal r63_AVERAGE) {
			R63_AVERAGE = r63_AVERAGE;
		}

		public String getR64_PRODUCT() {
			return R64_PRODUCT;
		}

		public void setR64_PRODUCT(String r64_PRODUCT) {
			R64_PRODUCT = r64_PRODUCT;
		}

		public String getR64_CROSS_REFERENCE() {
			return R64_CROSS_REFERENCE;
		}

		public void setR64_CROSS_REFERENCE(String r64_CROSS_REFERENCE) {
			R64_CROSS_REFERENCE = r64_CROSS_REFERENCE;
		}

		public BigDecimal getR64_MONTH_END() {
			return R64_MONTH_END;
		}

		public void setR64_MONTH_END(BigDecimal r64_MONTH_END) {
			R64_MONTH_END = r64_MONTH_END;
		}

		public BigDecimal getR64_AVERAGE() {
			return R64_AVERAGE;
		}

		public void setR64_AVERAGE(BigDecimal r64_AVERAGE) {
			R64_AVERAGE = r64_AVERAGE;
		}

		public String getR65_PRODUCT() {
			return R65_PRODUCT;
		}

		public void setR65_PRODUCT(String r65_PRODUCT) {
			R65_PRODUCT = r65_PRODUCT;
		}

		public String getR65_CROSS_REFERENCE() {
			return R65_CROSS_REFERENCE;
		}

		public void setR65_CROSS_REFERENCE(String r65_CROSS_REFERENCE) {
			R65_CROSS_REFERENCE = r65_CROSS_REFERENCE;
		}

		public BigDecimal getR65_MONTH_END() {
			return R65_MONTH_END;
		}

		public void setR65_MONTH_END(BigDecimal r65_MONTH_END) {
			R65_MONTH_END = r65_MONTH_END;
		}

		public BigDecimal getR65_AVERAGE() {
			return R65_AVERAGE;
		}

		public void setR65_AVERAGE(BigDecimal r65_AVERAGE) {
			R65_AVERAGE = r65_AVERAGE;
		}

		public String getR66_PRODUCT() {
			return R66_PRODUCT;
		}

		public void setR66_PRODUCT(String r66_PRODUCT) {
			R66_PRODUCT = r66_PRODUCT;
		}

		public String getR66_CROSS_REFERENCE() {
			return R66_CROSS_REFERENCE;
		}

		public void setR66_CROSS_REFERENCE(String r66_CROSS_REFERENCE) {
			R66_CROSS_REFERENCE = r66_CROSS_REFERENCE;
		}

		public BigDecimal getR66_MONTH_END() {
			return R66_MONTH_END;
		}

		public void setR66_MONTH_END(BigDecimal r66_MONTH_END) {
			R66_MONTH_END = r66_MONTH_END;
		}

		public BigDecimal getR66_AVERAGE() {
			return R66_AVERAGE;
		}

		public void setR66_AVERAGE(BigDecimal r66_AVERAGE) {
			R66_AVERAGE = r66_AVERAGE;
		}

		public String getR67_PRODUCT() {
			return R67_PRODUCT;
		}

		public void setR67_PRODUCT(String r67_PRODUCT) {
			R67_PRODUCT = r67_PRODUCT;
		}

		public String getR67_CROSS_REFERENCE() {
			return R67_CROSS_REFERENCE;
		}

		public void setR67_CROSS_REFERENCE(String r67_CROSS_REFERENCE) {
			R67_CROSS_REFERENCE = r67_CROSS_REFERENCE;
		}

		public BigDecimal getR67_MONTH_END() {
			return R67_MONTH_END;
		}

		public void setR67_MONTH_END(BigDecimal r67_MONTH_END) {
			R67_MONTH_END = r67_MONTH_END;
		}

		public BigDecimal getR67_AVERAGE() {
			return R67_AVERAGE;
		}

		public void setR67_AVERAGE(BigDecimal r67_AVERAGE) {
			R67_AVERAGE = r67_AVERAGE;
		}

		public String getR68_PRODUCT() {
			return R68_PRODUCT;
		}

		public void setR68_PRODUCT(String r68_PRODUCT) {
			R68_PRODUCT = r68_PRODUCT;
		}

		public String getR68_CROSS_REFERENCE() {
			return R68_CROSS_REFERENCE;
		}

		public void setR68_CROSS_REFERENCE(String r68_CROSS_REFERENCE) {
			R68_CROSS_REFERENCE = r68_CROSS_REFERENCE;
		}

		public BigDecimal getR68_MONTH_END() {
			return R68_MONTH_END;
		}

		public void setR68_MONTH_END(BigDecimal r68_MONTH_END) {
			R68_MONTH_END = r68_MONTH_END;
		}

		public BigDecimal getR68_AVERAGE() {
			return R68_AVERAGE;
		}

		public void setR68_AVERAGE(BigDecimal r68_AVERAGE) {
			R68_AVERAGE = r68_AVERAGE;
		}

		public String getR69_PRODUCT() {
			return R69_PRODUCT;
		}

		public void setR69_PRODUCT(String r69_PRODUCT) {
			R69_PRODUCT = r69_PRODUCT;
		}

		public String getR69_CROSS_REFERENCE() {
			return R69_CROSS_REFERENCE;
		}

		public void setR69_CROSS_REFERENCE(String r69_CROSS_REFERENCE) {
			R69_CROSS_REFERENCE = r69_CROSS_REFERENCE;
		}

		public BigDecimal getR69_MONTH_END() {
			return R69_MONTH_END;
		}

		public void setR69_MONTH_END(BigDecimal r69_MONTH_END) {
			R69_MONTH_END = r69_MONTH_END;
		}

		public BigDecimal getR69_AVERAGE() {
			return R69_AVERAGE;
		}

		public void setR69_AVERAGE(BigDecimal r69_AVERAGE) {
			R69_AVERAGE = r69_AVERAGE;
		}

		public String getR70_PRODUCT() {
			return R70_PRODUCT;
		}

		public void setR70_PRODUCT(String r70_PRODUCT) {
			R70_PRODUCT = r70_PRODUCT;
		}

		public String getR70_CROSS_REFERENCE() {
			return R70_CROSS_REFERENCE;
		}

		public void setR70_CROSS_REFERENCE(String r70_CROSS_REFERENCE) {
			R70_CROSS_REFERENCE = r70_CROSS_REFERENCE;
		}

		public BigDecimal getR70_MONTH_END() {
			return R70_MONTH_END;
		}

		public void setR70_MONTH_END(BigDecimal r70_MONTH_END) {
			R70_MONTH_END = r70_MONTH_END;
		}

		public BigDecimal getR70_AVERAGE() {
			return R70_AVERAGE;
		}

		public void setR70_AVERAGE(BigDecimal r70_AVERAGE) {
			R70_AVERAGE = r70_AVERAGE;
		}

		public String getR71_PRODUCT() {
			return R71_PRODUCT;
		}

		public void setR71_PRODUCT(String r71_PRODUCT) {
			R71_PRODUCT = r71_PRODUCT;
		}

		public String getR71_CROSS_REFERENCE() {
			return R71_CROSS_REFERENCE;
		}

		public void setR71_CROSS_REFERENCE(String r71_CROSS_REFERENCE) {
			R71_CROSS_REFERENCE = r71_CROSS_REFERENCE;
		}

		public BigDecimal getR71_MONTH_END() {
			return R71_MONTH_END;
		}

		public void setR71_MONTH_END(BigDecimal r71_MONTH_END) {
			R71_MONTH_END = r71_MONTH_END;
		}

		public BigDecimal getR71_AVERAGE() {
			return R71_AVERAGE;
		}

		public void setR71_AVERAGE(BigDecimal r71_AVERAGE) {
			R71_AVERAGE = r71_AVERAGE;
		}

		public String getR72_PRODUCT() {
			return R72_PRODUCT;
		}

		public void setR72_PRODUCT(String r72_PRODUCT) {
			R72_PRODUCT = r72_PRODUCT;
		}

		public String getR72_CROSS_REFERENCE() {
			return R72_CROSS_REFERENCE;
		}

		public void setR72_CROSS_REFERENCE(String r72_CROSS_REFERENCE) {
			R72_CROSS_REFERENCE = r72_CROSS_REFERENCE;
		}

		public BigDecimal getR72_MONTH_END() {
			return R72_MONTH_END;
		}

		public void setR72_MONTH_END(BigDecimal r72_MONTH_END) {
			R72_MONTH_END = r72_MONTH_END;
		}

		public BigDecimal getR72_AVERAGE() {
			return R72_AVERAGE;
		}

		public void setR72_AVERAGE(BigDecimal r72_AVERAGE) {
			R72_AVERAGE = r72_AVERAGE;
		}

		public String getR73_PRODUCT() {
			return R73_PRODUCT;
		}

		public void setR73_PRODUCT(String r73_PRODUCT) {
			R73_PRODUCT = r73_PRODUCT;
		}

		public String getR73_CROSS_REFERENCE() {
			return R73_CROSS_REFERENCE;
		}

		public void setR73_CROSS_REFERENCE(String r73_CROSS_REFERENCE) {
			R73_CROSS_REFERENCE = r73_CROSS_REFERENCE;
		}

		public BigDecimal getR73_MONTH_END() {
			return R73_MONTH_END;
		}

		public void setR73_MONTH_END(BigDecimal r73_MONTH_END) {
			R73_MONTH_END = r73_MONTH_END;
		}

		public BigDecimal getR73_AVERAGE() {
			return R73_AVERAGE;
		}

		public void setR73_AVERAGE(BigDecimal r73_AVERAGE) {
			R73_AVERAGE = r73_AVERAGE;
		}

		public String getR74_PRODUCT() {
			return R74_PRODUCT;
		}

		public void setR74_PRODUCT(String r74_PRODUCT) {
			R74_PRODUCT = r74_PRODUCT;
		}

		public String getR74_CROSS_REFERENCE() {
			return R74_CROSS_REFERENCE;
		}

		public void setR74_CROSS_REFERENCE(String r74_CROSS_REFERENCE) {
			R74_CROSS_REFERENCE = r74_CROSS_REFERENCE;
		}

		public BigDecimal getR74_MONTH_END() {
			return R74_MONTH_END;
		}

		public void setR74_MONTH_END(BigDecimal r74_MONTH_END) {
			R74_MONTH_END = r74_MONTH_END;
		}

		public BigDecimal getR74_AVERAGE() {
			return R74_AVERAGE;
		}

		public void setR74_AVERAGE(BigDecimal r74_AVERAGE) {
			R74_AVERAGE = r74_AVERAGE;
		}

		public String getR75_PRODUCT() {
			return R75_PRODUCT;
		}

		public void setR75_PRODUCT(String r75_PRODUCT) {
			R75_PRODUCT = r75_PRODUCT;
		}

		public String getR75_CROSS_REFERENCE() {
			return R75_CROSS_REFERENCE;
		}

		public void setR75_CROSS_REFERENCE(String r75_CROSS_REFERENCE) {
			R75_CROSS_REFERENCE = r75_CROSS_REFERENCE;
		}

		public BigDecimal getR75_MONTH_END() {
			return R75_MONTH_END;
		}

		public void setR75_MONTH_END(BigDecimal r75_MONTH_END) {
			R75_MONTH_END = r75_MONTH_END;
		}

		public BigDecimal getR75_AVERAGE() {
			return R75_AVERAGE;
		}

		public void setR75_AVERAGE(BigDecimal r75_AVERAGE) {
			R75_AVERAGE = r75_AVERAGE;
		}

		public String getR76_PRODUCT() {
			return R76_PRODUCT;
		}

		public void setR76_PRODUCT(String r76_PRODUCT) {
			R76_PRODUCT = r76_PRODUCT;
		}

		public String getR76_CROSS_REFERENCE() {
			return R76_CROSS_REFERENCE;
		}

		public void setR76_CROSS_REFERENCE(String r76_CROSS_REFERENCE) {
			R76_CROSS_REFERENCE = r76_CROSS_REFERENCE;
		}

		public BigDecimal getR76_MONTH_END() {
			return R76_MONTH_END;
		}

		public void setR76_MONTH_END(BigDecimal r76_MONTH_END) {
			R76_MONTH_END = r76_MONTH_END;
		}

		public BigDecimal getR76_AVERAGE() {
			return R76_AVERAGE;
		}

		public void setR76_AVERAGE(BigDecimal r76_AVERAGE) {
			R76_AVERAGE = r76_AVERAGE;
		}

		public String getR77_PRODUCT() {
			return R77_PRODUCT;
		}

		public void setR77_PRODUCT(String r77_PRODUCT) {
			R77_PRODUCT = r77_PRODUCT;
		}

		public String getR77_CROSS_REFERENCE() {
			return R77_CROSS_REFERENCE;
		}

		public void setR77_CROSS_REFERENCE(String r77_CROSS_REFERENCE) {
			R77_CROSS_REFERENCE = r77_CROSS_REFERENCE;
		}

		public BigDecimal getR77_MONTH_END() {
			return R77_MONTH_END;
		}

		public void setR77_MONTH_END(BigDecimal r77_MONTH_END) {
			R77_MONTH_END = r77_MONTH_END;
		}

		public BigDecimal getR77_AVERAGE() {
			return R77_AVERAGE;
		}

		public void setR77_AVERAGE(BigDecimal r77_AVERAGE) {
			R77_AVERAGE = r77_AVERAGE;
		}

		public String getR78_PRODUCT() {
			return R78_PRODUCT;
		}

		public void setR78_PRODUCT(String r78_PRODUCT) {
			R78_PRODUCT = r78_PRODUCT;
		}

		public String getR78_CROSS_REFERENCE() {
			return R78_CROSS_REFERENCE;
		}

		public void setR78_CROSS_REFERENCE(String r78_CROSS_REFERENCE) {
			R78_CROSS_REFERENCE = r78_CROSS_REFERENCE;
		}

		public BigDecimal getR78_MONTH_END() {
			return R78_MONTH_END;
		}

		public void setR78_MONTH_END(BigDecimal r78_MONTH_END) {
			R78_MONTH_END = r78_MONTH_END;
		}

		public BigDecimal getR78_AVERAGE() {
			return R78_AVERAGE;
		}

		public void setR78_AVERAGE(BigDecimal r78_AVERAGE) {
			R78_AVERAGE = r78_AVERAGE;
		}

		public String getR79_PRODUCT() {
			return R79_PRODUCT;
		}

		public void setR79_PRODUCT(String r79_PRODUCT) {
			R79_PRODUCT = r79_PRODUCT;
		}

		public String getR79_CROSS_REFERENCE() {
			return R79_CROSS_REFERENCE;
		}

		public void setR79_CROSS_REFERENCE(String r79_CROSS_REFERENCE) {
			R79_CROSS_REFERENCE = r79_CROSS_REFERENCE;
		}

		public BigDecimal getR79_MONTH_END() {
			return R79_MONTH_END;
		}

		public void setR79_MONTH_END(BigDecimal r79_MONTH_END) {
			R79_MONTH_END = r79_MONTH_END;
		}

		public BigDecimal getR79_AVERAGE() {
			return R79_AVERAGE;
		}

		public void setR79_AVERAGE(BigDecimal r79_AVERAGE) {
			R79_AVERAGE = r79_AVERAGE;
		}

		public String getR80_PRODUCT() {
			return R80_PRODUCT;
		}

		public void setR80_PRODUCT(String r80_PRODUCT) {
			R80_PRODUCT = r80_PRODUCT;
		}

		public String getR80_CROSS_REFERENCE() {
			return R80_CROSS_REFERENCE;
		}

		public void setR80_CROSS_REFERENCE(String r80_CROSS_REFERENCE) {
			R80_CROSS_REFERENCE = r80_CROSS_REFERENCE;
		}

		public BigDecimal getR80_MONTH_END() {
			return R80_MONTH_END;
		}

		public void setR80_MONTH_END(BigDecimal r80_MONTH_END) {
			R80_MONTH_END = r80_MONTH_END;
		}

		public BigDecimal getR80_AVERAGE() {
			return R80_AVERAGE;
		}

		public void setR80_AVERAGE(BigDecimal r80_AVERAGE) {
			R80_AVERAGE = r80_AVERAGE;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date rEPORT_DATE) {
			REPORT_DATE = rEPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
			REPORT_VERSION = rEPORT_VERSION;
		}

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}

		public M_SFINP2_Archival_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

// ------------------------------
// M_SFINP2_Archival_Detail_Entity 
// ------------------------------
	public static class M_SFINP2_Archival_Detail_Entity {

		@Column(name = "CUST_ID")
		private String custId;
		@Id
		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "COLUMN_ID")
		private String columnId;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria_1;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "REPORT_VERSION")
		private String reportVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
		private BigDecimal acctBalanceInpula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;
		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private char entityFlg;

		@Column(name = "MODIFY_FLG")
		private char modifyFlg;

		@Column(name = "DEL_FLG")
		private char delFlg;

		@Column(name = "AVERAGE", precision = 24, scale = 3)
		private BigDecimal average;

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getColumnId() {
			return columnId;
		}

		public void setColumnId(String columnId) {
			this.columnId = columnId;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria_1() {
			return reportAddlCriteria_1;
		}

		public void setReportAddlCriteria_1(String reportAddlCriteria_1) {
			this.reportAddlCriteria_1 = reportAddlCriteria_1;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public String getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(String reportVersion) {
			this.reportVersion = reportVersion;
		}

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public char getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(char entityFlg) {
			this.entityFlg = entityFlg;
		}

		public char getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(char modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public char getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(char delFlg) {
			this.delFlg = delFlg;
		}

		public BigDecimal getAverage() {
			return average;
		}

		public void setAverage(BigDecimal average) {
			this.average = average;
		}

		public M_SFINP2_Archival_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

// ------------------------------
// M_SFINP2_RESUB_Summary_Entity 
// ------------------------------
	public static class M_SFINP2_RESUB_Summary_Entity {
		public String R10_PRODUCT;
		public String R10_CROSS_REFERENCE;
		public BigDecimal R10_MONTH_END;
		public BigDecimal R10_AVERAGE;
		public String R11_PRODUCT;
		public String R11_CROSS_REFERENCE;
		public BigDecimal R11_MONTH_END;
		public BigDecimal R11_AVERAGE;
		public String R12_PRODUCT;
		public String R12_CROSS_REFERENCE;
		public BigDecimal R12_MONTH_END;
		public BigDecimal R12_AVERAGE;
		public String R13_PRODUCT;
		public String R13_CROSS_REFERENCE;
		public BigDecimal R13_MONTH_END;
		public BigDecimal R13_AVERAGE;
		public String R14_PRODUCT;
		public String R14_CROSS_REFERENCE;
		public BigDecimal R14_MONTH_END;
		public BigDecimal R14_AVERAGE;
		public String R15_PRODUCT;
		public String R15_CROSS_REFERENCE;
		public BigDecimal R15_MONTH_END;
		public BigDecimal R15_AVERAGE;
		public String R16_PRODUCT;
		public String R16_CROSS_REFERENCE;
		public BigDecimal R16_MONTH_END;
		public BigDecimal R16_AVERAGE;
		public String R17_PRODUCT;
		public String R17_CROSS_REFERENCE;
		public BigDecimal R17_MONTH_END;
		public BigDecimal R17_AVERAGE;
		public String R18_PRODUCT;
		public String R18_CROSS_REFERENCE;
		public BigDecimal R18_MONTH_END;
		public BigDecimal R18_AVERAGE;
		public String R19_PRODUCT;
		public String R19_CROSS_REFERENCE;
		public BigDecimal R19_MONTH_END;
		public BigDecimal R19_AVERAGE;
		public String R20_PRODUCT;
		public String R20_CROSS_REFERENCE;
		public BigDecimal R20_MONTH_END;
		public BigDecimal R20_AVERAGE;
		public String R21_PRODUCT;
		public String R21_CROSS_REFERENCE;
		public BigDecimal R21_MONTH_END;
		public BigDecimal R21_AVERAGE;
		public String R22_PRODUCT;
		public String R22_CROSS_REFERENCE;
		public BigDecimal R22_MONTH_END;
		public BigDecimal R22_AVERAGE;
		public String R23_PRODUCT;
		public String R23_CROSS_REFERENCE;
		public BigDecimal R23_MONTH_END;
		public BigDecimal R23_AVERAGE;
		public String R24_PRODUCT;
		public String R24_CROSS_REFERENCE;
		public BigDecimal R24_MONTH_END;
		public BigDecimal R24_AVERAGE;
		public String R25_PRODUCT;
		public String R25_CROSS_REFERENCE;
		public BigDecimal R25_MONTH_END;
		public BigDecimal R25_AVERAGE;
		public String R26_PRODUCT;
		public String R26_CROSS_REFERENCE;
		public BigDecimal R26_MONTH_END;
		public BigDecimal R26_AVERAGE;
		public String R27_PRODUCT;
		public String R27_CROSS_REFERENCE;
		public BigDecimal R27_MONTH_END;
		public BigDecimal R27_AVERAGE;
		public String R28_PRODUCT;
		public String R28_CROSS_REFERENCE;
		public BigDecimal R28_MONTH_END;
		public BigDecimal R28_AVERAGE;
		public String R29_PRODUCT;
		public String R29_CROSS_REFERENCE;
		public BigDecimal R29_MONTH_END;
		public BigDecimal R29_AVERAGE;
		public String R30_PRODUCT;
		public String R30_CROSS_REFERENCE;
		public BigDecimal R30_MONTH_END;
		public BigDecimal R30_AVERAGE;
		public String R31_PRODUCT;
		public String R31_CROSS_REFERENCE;
		public BigDecimal R31_MONTH_END;
		public BigDecimal R31_AVERAGE;
		public String R32_PRODUCT;
		public String R32_CROSS_REFERENCE;
		public BigDecimal R32_MONTH_END;
		public BigDecimal R32_AVERAGE;
		public String R33_PRODUCT;
		public String R33_CROSS_REFERENCE;
		public BigDecimal R33_MONTH_END;
		public BigDecimal R33_AVERAGE;
		public String R34_PRODUCT;
		public String R34_CROSS_REFERENCE;
		public BigDecimal R34_MONTH_END;
		public BigDecimal R34_AVERAGE;
		public String R35_PRODUCT;
		public String R35_CROSS_REFERENCE;
		public BigDecimal R35_MONTH_END;
		public BigDecimal R35_AVERAGE;
		public String R36_PRODUCT;
		public String R36_CROSS_REFERENCE;
		public BigDecimal R36_MONTH_END;
		public BigDecimal R36_AVERAGE;
		public String R37_PRODUCT;
		public String R37_CROSS_REFERENCE;
		public BigDecimal R37_MONTH_END;
		public BigDecimal R37_AVERAGE;
		public String R38_PRODUCT;
		public String R38_CROSS_REFERENCE;
		public BigDecimal R38_MONTH_END;
		public BigDecimal R38_AVERAGE;
		public String R39_PRODUCT;
		public String R39_CROSS_REFERENCE;
		public BigDecimal R39_MONTH_END;
		public BigDecimal R39_AVERAGE;
		public String R40_PRODUCT;
		public String R40_CROSS_REFERENCE;
		public BigDecimal R40_MONTH_END;
		public BigDecimal R40_AVERAGE;
		public String R41_PRODUCT;
		public String R41_CROSS_REFERENCE;
		public BigDecimal R41_MONTH_END;
		public BigDecimal R41_AVERAGE;
		public String R42_PRODUCT;
		public String R42_CROSS_REFERENCE;
		public BigDecimal R42_MONTH_END;
		public BigDecimal R42_AVERAGE;
		public String R43_PRODUCT;
		public String R43_CROSS_REFERENCE;
		public BigDecimal R43_MONTH_END;
		public BigDecimal R43_AVERAGE;
		public String R44_PRODUCT;
		public String R44_CROSS_REFERENCE;
		public BigDecimal R44_MONTH_END;
		public BigDecimal R44_AVERAGE;
		public String R45_PRODUCT;
		public String R45_CROSS_REFERENCE;
		public BigDecimal R45_MONTH_END;
		public BigDecimal R45_AVERAGE;
		public String R46_PRODUCT;
		public String R46_CROSS_REFERENCE;
		public BigDecimal R46_MONTH_END;
		public BigDecimal R46_AVERAGE;
		public String R47_PRODUCT;
		public String R47_CROSS_REFERENCE;
		public BigDecimal R47_MONTH_END;
		public BigDecimal R47_AVERAGE;
		public String R48_PRODUCT;
		public String R48_CROSS_REFERENCE;
		public BigDecimal R48_MONTH_END;
		public BigDecimal R48_AVERAGE;
		public String R49_PRODUCT;
		public String R49_CROSS_REFERENCE;
		public BigDecimal R49_MONTH_END;
		public BigDecimal R49_AVERAGE;
		public String R50_PRODUCT;
		public String R50_CROSS_REFERENCE;
		public BigDecimal R50_MONTH_END;
		public BigDecimal R50_AVERAGE;
		public String R51_PRODUCT;
		public String R51_CROSS_REFERENCE;
		public BigDecimal R51_MONTH_END;
		public BigDecimal R51_AVERAGE;
		public String R52_PRODUCT;
		public String R52_CROSS_REFERENCE;
		public BigDecimal R52_MONTH_END;
		public BigDecimal R52_AVERAGE;
		public String R53_PRODUCT;
		public String R53_CROSS_REFERENCE;
		public BigDecimal R53_MONTH_END;
		public BigDecimal R53_AVERAGE;
		public String R54_PRODUCT;
		public String R54_CROSS_REFERENCE;
		public BigDecimal R54_MONTH_END;
		public BigDecimal R54_AVERAGE;
		public String R55_PRODUCT;
		public String R55_CROSS_REFERENCE;
		public BigDecimal R55_MONTH_END;
		public BigDecimal R55_AVERAGE;
		public String R56_PRODUCT;
		public String R56_CROSS_REFERENCE;
		public BigDecimal R56_MONTH_END;
		public BigDecimal R56_AVERAGE;
		public String R57_PRODUCT;
		public String R57_CROSS_REFERENCE;
		public BigDecimal R57_MONTH_END;
		public BigDecimal R57_AVERAGE;
		public String R58_PRODUCT;
		public String R58_CROSS_REFERENCE;
		public BigDecimal R58_MONTH_END;
		public BigDecimal R58_AVERAGE;
		public String R59_PRODUCT;
		public String R59_CROSS_REFERENCE;
		public BigDecimal R59_MONTH_END;
		public BigDecimal R59_AVERAGE;
		public String R60_PRODUCT;
		public String R60_CROSS_REFERENCE;
		public BigDecimal R60_MONTH_END;
		public BigDecimal R60_AVERAGE;
		public String R61_PRODUCT;
		public String R61_CROSS_REFERENCE;
		public BigDecimal R61_MONTH_END;
		public BigDecimal R61_AVERAGE;
		public String R62_PRODUCT;
		public String R62_CROSS_REFERENCE;
		public BigDecimal R62_MONTH_END;
		public BigDecimal R62_AVERAGE;
		public String R63_PRODUCT;
		public String R63_CROSS_REFERENCE;
		public BigDecimal R63_MONTH_END;
		public BigDecimal R63_AVERAGE;
		public String R64_PRODUCT;
		public String R64_CROSS_REFERENCE;
		public BigDecimal R64_MONTH_END;
		public BigDecimal R64_AVERAGE;
		public String R65_PRODUCT;
		public String R65_CROSS_REFERENCE;
		public BigDecimal R65_MONTH_END;
		public BigDecimal R65_AVERAGE;
		public String R66_PRODUCT;
		public String R66_CROSS_REFERENCE;
		public BigDecimal R66_MONTH_END;
		public BigDecimal R66_AVERAGE;
		public String R67_PRODUCT;
		public String R67_CROSS_REFERENCE;
		public BigDecimal R67_MONTH_END;
		public BigDecimal R67_AVERAGE;
		public String R68_PRODUCT;
		public String R68_CROSS_REFERENCE;
		public BigDecimal R68_MONTH_END;
		public BigDecimal R68_AVERAGE;
		public String R69_PRODUCT;
		public String R69_CROSS_REFERENCE;
		public BigDecimal R69_MONTH_END;
		public BigDecimal R69_AVERAGE;
		public String R70_PRODUCT;
		public String R70_CROSS_REFERENCE;
		public BigDecimal R70_MONTH_END;
		public BigDecimal R70_AVERAGE;
		public String R71_PRODUCT;
		public String R71_CROSS_REFERENCE;
		public BigDecimal R71_MONTH_END;
		public BigDecimal R71_AVERAGE;
		public String R72_PRODUCT;
		public String R72_CROSS_REFERENCE;
		public BigDecimal R72_MONTH_END;
		public BigDecimal R72_AVERAGE;
		public String R73_PRODUCT;
		public String R73_CROSS_REFERENCE;
		public BigDecimal R73_MONTH_END;
		public BigDecimal R73_AVERAGE;
		public String R74_PRODUCT;
		public String R74_CROSS_REFERENCE;
		public BigDecimal R74_MONTH_END;
		public BigDecimal R74_AVERAGE;
		public String R75_PRODUCT;
		public String R75_CROSS_REFERENCE;
		public BigDecimal R75_MONTH_END;
		public BigDecimal R75_AVERAGE;
		public String R76_PRODUCT;
		public String R76_CROSS_REFERENCE;
		public BigDecimal R76_MONTH_END;
		public BigDecimal R76_AVERAGE;
		public String R77_PRODUCT;
		public String R77_CROSS_REFERENCE;
		public BigDecimal R77_MONTH_END;
		public BigDecimal R77_AVERAGE;
		public String R78_PRODUCT;
		public String R78_CROSS_REFERENCE;
		public BigDecimal R78_MONTH_END;
		public BigDecimal R78_AVERAGE;
		public String R79_PRODUCT;
		public String R79_CROSS_REFERENCE;
		public BigDecimal R79_MONTH_END;
		public BigDecimal R79_AVERAGE;
		public String R80_PRODUCT;
		public String R80_CROSS_REFERENCE;
		public BigDecimal R80_MONTH_END;
		public BigDecimal R80_AVERAGE;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		public Date REPORT_DATE;
		@Id
		public BigDecimal REPORT_VERSION;
		@Column(name = "REPORT_RESUBDATE")

		private Date reportResubDate;
		public String REPORT_FREQUENCY;
		public String REPORT_CODE;
		public String REPORT_DESC;
		public String ENTITY_FLG;
		public String MODIFY_FLG;
		public String DEL_FLG;

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public String getR10_CROSS_REFERENCE() {
			return R10_CROSS_REFERENCE;
		}

		public void setR10_CROSS_REFERENCE(String r10_CROSS_REFERENCE) {
			R10_CROSS_REFERENCE = r10_CROSS_REFERENCE;
		}

		public BigDecimal getR10_MONTH_END() {
			return R10_MONTH_END;
		}

		public void setR10_MONTH_END(BigDecimal r10_MONTH_END) {
			R10_MONTH_END = r10_MONTH_END;
		}

		public BigDecimal getR10_AVERAGE() {
			return R10_AVERAGE;
		}

		public void setR10_AVERAGE(BigDecimal r10_AVERAGE) {
			R10_AVERAGE = r10_AVERAGE;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public String getR11_CROSS_REFERENCE() {
			return R11_CROSS_REFERENCE;
		}

		public void setR11_CROSS_REFERENCE(String r11_CROSS_REFERENCE) {
			R11_CROSS_REFERENCE = r11_CROSS_REFERENCE;
		}

		public BigDecimal getR11_MONTH_END() {
			return R11_MONTH_END;
		}

		public void setR11_MONTH_END(BigDecimal r11_MONTH_END) {
			R11_MONTH_END = r11_MONTH_END;
		}

		public BigDecimal getR11_AVERAGE() {
			return R11_AVERAGE;
		}

		public void setR11_AVERAGE(BigDecimal r11_AVERAGE) {
			R11_AVERAGE = r11_AVERAGE;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public String getR12_CROSS_REFERENCE() {
			return R12_CROSS_REFERENCE;
		}

		public void setR12_CROSS_REFERENCE(String r12_CROSS_REFERENCE) {
			R12_CROSS_REFERENCE = r12_CROSS_REFERENCE;
		}

		public BigDecimal getR12_MONTH_END() {
			return R12_MONTH_END;
		}

		public void setR12_MONTH_END(BigDecimal r12_MONTH_END) {
			R12_MONTH_END = r12_MONTH_END;
		}

		public BigDecimal getR12_AVERAGE() {
			return R12_AVERAGE;
		}

		public void setR12_AVERAGE(BigDecimal r12_AVERAGE) {
			R12_AVERAGE = r12_AVERAGE;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public String getR13_CROSS_REFERENCE() {
			return R13_CROSS_REFERENCE;
		}

		public void setR13_CROSS_REFERENCE(String r13_CROSS_REFERENCE) {
			R13_CROSS_REFERENCE = r13_CROSS_REFERENCE;
		}

		public BigDecimal getR13_MONTH_END() {
			return R13_MONTH_END;
		}

		public void setR13_MONTH_END(BigDecimal r13_MONTH_END) {
			R13_MONTH_END = r13_MONTH_END;
		}

		public BigDecimal getR13_AVERAGE() {
			return R13_AVERAGE;
		}

		public void setR13_AVERAGE(BigDecimal r13_AVERAGE) {
			R13_AVERAGE = r13_AVERAGE;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public String getR14_CROSS_REFERENCE() {
			return R14_CROSS_REFERENCE;
		}

		public void setR14_CROSS_REFERENCE(String r14_CROSS_REFERENCE) {
			R14_CROSS_REFERENCE = r14_CROSS_REFERENCE;
		}

		public BigDecimal getR14_MONTH_END() {
			return R14_MONTH_END;
		}

		public void setR14_MONTH_END(BigDecimal r14_MONTH_END) {
			R14_MONTH_END = r14_MONTH_END;
		}

		public BigDecimal getR14_AVERAGE() {
			return R14_AVERAGE;
		}

		public void setR14_AVERAGE(BigDecimal r14_AVERAGE) {
			R14_AVERAGE = r14_AVERAGE;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public String getR15_CROSS_REFERENCE() {
			return R15_CROSS_REFERENCE;
		}

		public void setR15_CROSS_REFERENCE(String r15_CROSS_REFERENCE) {
			R15_CROSS_REFERENCE = r15_CROSS_REFERENCE;
		}

		public BigDecimal getR15_MONTH_END() {
			return R15_MONTH_END;
		}

		public void setR15_MONTH_END(BigDecimal r15_MONTH_END) {
			R15_MONTH_END = r15_MONTH_END;
		}

		public BigDecimal getR15_AVERAGE() {
			return R15_AVERAGE;
		}

		public void setR15_AVERAGE(BigDecimal r15_AVERAGE) {
			R15_AVERAGE = r15_AVERAGE;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public String getR16_CROSS_REFERENCE() {
			return R16_CROSS_REFERENCE;
		}

		public void setR16_CROSS_REFERENCE(String r16_CROSS_REFERENCE) {
			R16_CROSS_REFERENCE = r16_CROSS_REFERENCE;
		}

		public BigDecimal getR16_MONTH_END() {
			return R16_MONTH_END;
		}

		public void setR16_MONTH_END(BigDecimal r16_MONTH_END) {
			R16_MONTH_END = r16_MONTH_END;
		}

		public BigDecimal getR16_AVERAGE() {
			return R16_AVERAGE;
		}

		public void setR16_AVERAGE(BigDecimal r16_AVERAGE) {
			R16_AVERAGE = r16_AVERAGE;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public String getR17_CROSS_REFERENCE() {
			return R17_CROSS_REFERENCE;
		}

		public void setR17_CROSS_REFERENCE(String r17_CROSS_REFERENCE) {
			R17_CROSS_REFERENCE = r17_CROSS_REFERENCE;
		}

		public BigDecimal getR17_MONTH_END() {
			return R17_MONTH_END;
		}

		public void setR17_MONTH_END(BigDecimal r17_MONTH_END) {
			R17_MONTH_END = r17_MONTH_END;
		}

		public BigDecimal getR17_AVERAGE() {
			return R17_AVERAGE;
		}

		public void setR17_AVERAGE(BigDecimal r17_AVERAGE) {
			R17_AVERAGE = r17_AVERAGE;
		}

		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public String getR18_CROSS_REFERENCE() {
			return R18_CROSS_REFERENCE;
		}

		public void setR18_CROSS_REFERENCE(String r18_CROSS_REFERENCE) {
			R18_CROSS_REFERENCE = r18_CROSS_REFERENCE;
		}

		public BigDecimal getR18_MONTH_END() {
			return R18_MONTH_END;
		}

		public void setR18_MONTH_END(BigDecimal r18_MONTH_END) {
			R18_MONTH_END = r18_MONTH_END;
		}

		public BigDecimal getR18_AVERAGE() {
			return R18_AVERAGE;
		}

		public void setR18_AVERAGE(BigDecimal r18_AVERAGE) {
			R18_AVERAGE = r18_AVERAGE;
		}

		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public String getR19_CROSS_REFERENCE() {
			return R19_CROSS_REFERENCE;
		}

		public void setR19_CROSS_REFERENCE(String r19_CROSS_REFERENCE) {
			R19_CROSS_REFERENCE = r19_CROSS_REFERENCE;
		}

		public BigDecimal getR19_MONTH_END() {
			return R19_MONTH_END;
		}

		public void setR19_MONTH_END(BigDecimal r19_MONTH_END) {
			R19_MONTH_END = r19_MONTH_END;
		}

		public BigDecimal getR19_AVERAGE() {
			return R19_AVERAGE;
		}

		public void setR19_AVERAGE(BigDecimal r19_AVERAGE) {
			R19_AVERAGE = r19_AVERAGE;
		}

		public String getR20_PRODUCT() {
			return R20_PRODUCT;
		}

		public void setR20_PRODUCT(String r20_PRODUCT) {
			R20_PRODUCT = r20_PRODUCT;
		}

		public String getR20_CROSS_REFERENCE() {
			return R20_CROSS_REFERENCE;
		}

		public void setR20_CROSS_REFERENCE(String r20_CROSS_REFERENCE) {
			R20_CROSS_REFERENCE = r20_CROSS_REFERENCE;
		}

		public BigDecimal getR20_MONTH_END() {
			return R20_MONTH_END;
		}

		public void setR20_MONTH_END(BigDecimal r20_MONTH_END) {
			R20_MONTH_END = r20_MONTH_END;
		}

		public BigDecimal getR20_AVERAGE() {
			return R20_AVERAGE;
		}

		public void setR20_AVERAGE(BigDecimal r20_AVERAGE) {
			R20_AVERAGE = r20_AVERAGE;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			R21_PRODUCT = r21_PRODUCT;
		}

		public String getR21_CROSS_REFERENCE() {
			return R21_CROSS_REFERENCE;
		}

		public void setR21_CROSS_REFERENCE(String r21_CROSS_REFERENCE) {
			R21_CROSS_REFERENCE = r21_CROSS_REFERENCE;
		}

		public BigDecimal getR21_MONTH_END() {
			return R21_MONTH_END;
		}

		public void setR21_MONTH_END(BigDecimal r21_MONTH_END) {
			R21_MONTH_END = r21_MONTH_END;
		}

		public BigDecimal getR21_AVERAGE() {
			return R21_AVERAGE;
		}

		public void setR21_AVERAGE(BigDecimal r21_AVERAGE) {
			R21_AVERAGE = r21_AVERAGE;
		}

		public String getR22_PRODUCT() {
			return R22_PRODUCT;
		}

		public void setR22_PRODUCT(String r22_PRODUCT) {
			R22_PRODUCT = r22_PRODUCT;
		}

		public String getR22_CROSS_REFERENCE() {
			return R22_CROSS_REFERENCE;
		}

		public void setR22_CROSS_REFERENCE(String r22_CROSS_REFERENCE) {
			R22_CROSS_REFERENCE = r22_CROSS_REFERENCE;
		}

		public BigDecimal getR22_MONTH_END() {
			return R22_MONTH_END;
		}

		public void setR22_MONTH_END(BigDecimal r22_MONTH_END) {
			R22_MONTH_END = r22_MONTH_END;
		}

		public BigDecimal getR22_AVERAGE() {
			return R22_AVERAGE;
		}

		public void setR22_AVERAGE(BigDecimal r22_AVERAGE) {
			R22_AVERAGE = r22_AVERAGE;
		}

		public String getR23_PRODUCT() {
			return R23_PRODUCT;
		}

		public void setR23_PRODUCT(String r23_PRODUCT) {
			R23_PRODUCT = r23_PRODUCT;
		}

		public String getR23_CROSS_REFERENCE() {
			return R23_CROSS_REFERENCE;
		}

		public void setR23_CROSS_REFERENCE(String r23_CROSS_REFERENCE) {
			R23_CROSS_REFERENCE = r23_CROSS_REFERENCE;
		}

		public BigDecimal getR23_MONTH_END() {
			return R23_MONTH_END;
		}

		public void setR23_MONTH_END(BigDecimal r23_MONTH_END) {
			R23_MONTH_END = r23_MONTH_END;
		}

		public BigDecimal getR23_AVERAGE() {
			return R23_AVERAGE;
		}

		public void setR23_AVERAGE(BigDecimal r23_AVERAGE) {
			R23_AVERAGE = r23_AVERAGE;
		}

		public String getR24_PRODUCT() {
			return R24_PRODUCT;
		}

		public void setR24_PRODUCT(String r24_PRODUCT) {
			R24_PRODUCT = r24_PRODUCT;
		}

		public String getR24_CROSS_REFERENCE() {
			return R24_CROSS_REFERENCE;
		}

		public void setR24_CROSS_REFERENCE(String r24_CROSS_REFERENCE) {
			R24_CROSS_REFERENCE = r24_CROSS_REFERENCE;
		}

		public BigDecimal getR24_MONTH_END() {
			return R24_MONTH_END;
		}

		public void setR24_MONTH_END(BigDecimal r24_MONTH_END) {
			R24_MONTH_END = r24_MONTH_END;
		}

		public BigDecimal getR24_AVERAGE() {
			return R24_AVERAGE;
		}

		public void setR24_AVERAGE(BigDecimal r24_AVERAGE) {
			R24_AVERAGE = r24_AVERAGE;
		}

		public String getR25_PRODUCT() {
			return R25_PRODUCT;
		}

		public void setR25_PRODUCT(String r25_PRODUCT) {
			R25_PRODUCT = r25_PRODUCT;
		}

		public String getR25_CROSS_REFERENCE() {
			return R25_CROSS_REFERENCE;
		}

		public void setR25_CROSS_REFERENCE(String r25_CROSS_REFERENCE) {
			R25_CROSS_REFERENCE = r25_CROSS_REFERENCE;
		}

		public BigDecimal getR25_MONTH_END() {
			return R25_MONTH_END;
		}

		public void setR25_MONTH_END(BigDecimal r25_MONTH_END) {
			R25_MONTH_END = r25_MONTH_END;
		}

		public BigDecimal getR25_AVERAGE() {
			return R25_AVERAGE;
		}

		public void setR25_AVERAGE(BigDecimal r25_AVERAGE) {
			R25_AVERAGE = r25_AVERAGE;
		}

		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public String getR26_CROSS_REFERENCE() {
			return R26_CROSS_REFERENCE;
		}

		public void setR26_CROSS_REFERENCE(String r26_CROSS_REFERENCE) {
			R26_CROSS_REFERENCE = r26_CROSS_REFERENCE;
		}

		public BigDecimal getR26_MONTH_END() {
			return R26_MONTH_END;
		}

		public void setR26_MONTH_END(BigDecimal r26_MONTH_END) {
			R26_MONTH_END = r26_MONTH_END;
		}

		public BigDecimal getR26_AVERAGE() {
			return R26_AVERAGE;
		}

		public void setR26_AVERAGE(BigDecimal r26_AVERAGE) {
			R26_AVERAGE = r26_AVERAGE;
		}

		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public String getR27_CROSS_REFERENCE() {
			return R27_CROSS_REFERENCE;
		}

		public void setR27_CROSS_REFERENCE(String r27_CROSS_REFERENCE) {
			R27_CROSS_REFERENCE = r27_CROSS_REFERENCE;
		}

		public BigDecimal getR27_MONTH_END() {
			return R27_MONTH_END;
		}

		public void setR27_MONTH_END(BigDecimal r27_MONTH_END) {
			R27_MONTH_END = r27_MONTH_END;
		}

		public BigDecimal getR27_AVERAGE() {
			return R27_AVERAGE;
		}

		public void setR27_AVERAGE(BigDecimal r27_AVERAGE) {
			R27_AVERAGE = r27_AVERAGE;
		}

		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public String getR28_CROSS_REFERENCE() {
			return R28_CROSS_REFERENCE;
		}

		public void setR28_CROSS_REFERENCE(String r28_CROSS_REFERENCE) {
			R28_CROSS_REFERENCE = r28_CROSS_REFERENCE;
		}

		public BigDecimal getR28_MONTH_END() {
			return R28_MONTH_END;
		}

		public void setR28_MONTH_END(BigDecimal r28_MONTH_END) {
			R28_MONTH_END = r28_MONTH_END;
		}

		public BigDecimal getR28_AVERAGE() {
			return R28_AVERAGE;
		}

		public void setR28_AVERAGE(BigDecimal r28_AVERAGE) {
			R28_AVERAGE = r28_AVERAGE;
		}

		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public String getR29_CROSS_REFERENCE() {
			return R29_CROSS_REFERENCE;
		}

		public void setR29_CROSS_REFERENCE(String r29_CROSS_REFERENCE) {
			R29_CROSS_REFERENCE = r29_CROSS_REFERENCE;
		}

		public BigDecimal getR29_MONTH_END() {
			return R29_MONTH_END;
		}

		public void setR29_MONTH_END(BigDecimal r29_MONTH_END) {
			R29_MONTH_END = r29_MONTH_END;
		}

		public BigDecimal getR29_AVERAGE() {
			return R29_AVERAGE;
		}

		public void setR29_AVERAGE(BigDecimal r29_AVERAGE) {
			R29_AVERAGE = r29_AVERAGE;
		}

		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public String getR30_CROSS_REFERENCE() {
			return R30_CROSS_REFERENCE;
		}

		public void setR30_CROSS_REFERENCE(String r30_CROSS_REFERENCE) {
			R30_CROSS_REFERENCE = r30_CROSS_REFERENCE;
		}

		public BigDecimal getR30_MONTH_END() {
			return R30_MONTH_END;
		}

		public void setR30_MONTH_END(BigDecimal r30_MONTH_END) {
			R30_MONTH_END = r30_MONTH_END;
		}

		public BigDecimal getR30_AVERAGE() {
			return R30_AVERAGE;
		}

		public void setR30_AVERAGE(BigDecimal r30_AVERAGE) {
			R30_AVERAGE = r30_AVERAGE;
		}

		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public String getR31_CROSS_REFERENCE() {
			return R31_CROSS_REFERENCE;
		}

		public void setR31_CROSS_REFERENCE(String r31_CROSS_REFERENCE) {
			R31_CROSS_REFERENCE = r31_CROSS_REFERENCE;
		}

		public BigDecimal getR31_MONTH_END() {
			return R31_MONTH_END;
		}

		public void setR31_MONTH_END(BigDecimal r31_MONTH_END) {
			R31_MONTH_END = r31_MONTH_END;
		}

		public BigDecimal getR31_AVERAGE() {
			return R31_AVERAGE;
		}

		public void setR31_AVERAGE(BigDecimal r31_AVERAGE) {
			R31_AVERAGE = r31_AVERAGE;
		}

		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}

		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}

		public String getR32_CROSS_REFERENCE() {
			return R32_CROSS_REFERENCE;
		}

		public void setR32_CROSS_REFERENCE(String r32_CROSS_REFERENCE) {
			R32_CROSS_REFERENCE = r32_CROSS_REFERENCE;
		}

		public BigDecimal getR32_MONTH_END() {
			return R32_MONTH_END;
		}

		public void setR32_MONTH_END(BigDecimal r32_MONTH_END) {
			R32_MONTH_END = r32_MONTH_END;
		}

		public BigDecimal getR32_AVERAGE() {
			return R32_AVERAGE;
		}

		public void setR32_AVERAGE(BigDecimal r32_AVERAGE) {
			R32_AVERAGE = r32_AVERAGE;
		}

		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}

		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}

		public String getR33_CROSS_REFERENCE() {
			return R33_CROSS_REFERENCE;
		}

		public void setR33_CROSS_REFERENCE(String r33_CROSS_REFERENCE) {
			R33_CROSS_REFERENCE = r33_CROSS_REFERENCE;
		}

		public BigDecimal getR33_MONTH_END() {
			return R33_MONTH_END;
		}

		public void setR33_MONTH_END(BigDecimal r33_MONTH_END) {
			R33_MONTH_END = r33_MONTH_END;
		}

		public BigDecimal getR33_AVERAGE() {
			return R33_AVERAGE;
		}

		public void setR33_AVERAGE(BigDecimal r33_AVERAGE) {
			R33_AVERAGE = r33_AVERAGE;
		}

		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}

		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}

		public String getR34_CROSS_REFERENCE() {
			return R34_CROSS_REFERENCE;
		}

		public void setR34_CROSS_REFERENCE(String r34_CROSS_REFERENCE) {
			R34_CROSS_REFERENCE = r34_CROSS_REFERENCE;
		}

		public BigDecimal getR34_MONTH_END() {
			return R34_MONTH_END;
		}

		public void setR34_MONTH_END(BigDecimal r34_MONTH_END) {
			R34_MONTH_END = r34_MONTH_END;
		}

		public BigDecimal getR34_AVERAGE() {
			return R34_AVERAGE;
		}

		public void setR34_AVERAGE(BigDecimal r34_AVERAGE) {
			R34_AVERAGE = r34_AVERAGE;
		}

		public String getR35_PRODUCT() {
			return R35_PRODUCT;
		}

		public void setR35_PRODUCT(String r35_PRODUCT) {
			R35_PRODUCT = r35_PRODUCT;
		}

		public String getR35_CROSS_REFERENCE() {
			return R35_CROSS_REFERENCE;
		}

		public void setR35_CROSS_REFERENCE(String r35_CROSS_REFERENCE) {
			R35_CROSS_REFERENCE = r35_CROSS_REFERENCE;
		}

		public BigDecimal getR35_MONTH_END() {
			return R35_MONTH_END;
		}

		public void setR35_MONTH_END(BigDecimal r35_MONTH_END) {
			R35_MONTH_END = r35_MONTH_END;
		}

		public BigDecimal getR35_AVERAGE() {
			return R35_AVERAGE;
		}

		public void setR35_AVERAGE(BigDecimal r35_AVERAGE) {
			R35_AVERAGE = r35_AVERAGE;
		}

		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public String getR36_CROSS_REFERENCE() {
			return R36_CROSS_REFERENCE;
		}

		public void setR36_CROSS_REFERENCE(String r36_CROSS_REFERENCE) {
			R36_CROSS_REFERENCE = r36_CROSS_REFERENCE;
		}

		public BigDecimal getR36_MONTH_END() {
			return R36_MONTH_END;
		}

		public void setR36_MONTH_END(BigDecimal r36_MONTH_END) {
			R36_MONTH_END = r36_MONTH_END;
		}

		public BigDecimal getR36_AVERAGE() {
			return R36_AVERAGE;
		}

		public void setR36_AVERAGE(BigDecimal r36_AVERAGE) {
			R36_AVERAGE = r36_AVERAGE;
		}

		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public String getR37_CROSS_REFERENCE() {
			return R37_CROSS_REFERENCE;
		}

		public void setR37_CROSS_REFERENCE(String r37_CROSS_REFERENCE) {
			R37_CROSS_REFERENCE = r37_CROSS_REFERENCE;
		}

		public BigDecimal getR37_MONTH_END() {
			return R37_MONTH_END;
		}

		public void setR37_MONTH_END(BigDecimal r37_MONTH_END) {
			R37_MONTH_END = r37_MONTH_END;
		}

		public BigDecimal getR37_AVERAGE() {
			return R37_AVERAGE;
		}

		public void setR37_AVERAGE(BigDecimal r37_AVERAGE) {
			R37_AVERAGE = r37_AVERAGE;
		}

		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public String getR38_CROSS_REFERENCE() {
			return R38_CROSS_REFERENCE;
		}

		public void setR38_CROSS_REFERENCE(String r38_CROSS_REFERENCE) {
			R38_CROSS_REFERENCE = r38_CROSS_REFERENCE;
		}

		public BigDecimal getR38_MONTH_END() {
			return R38_MONTH_END;
		}

		public void setR38_MONTH_END(BigDecimal r38_MONTH_END) {
			R38_MONTH_END = r38_MONTH_END;
		}

		public BigDecimal getR38_AVERAGE() {
			return R38_AVERAGE;
		}

		public void setR38_AVERAGE(BigDecimal r38_AVERAGE) {
			R38_AVERAGE = r38_AVERAGE;
		}

		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public String getR39_CROSS_REFERENCE() {
			return R39_CROSS_REFERENCE;
		}

		public void setR39_CROSS_REFERENCE(String r39_CROSS_REFERENCE) {
			R39_CROSS_REFERENCE = r39_CROSS_REFERENCE;
		}

		public BigDecimal getR39_MONTH_END() {
			return R39_MONTH_END;
		}

		public void setR39_MONTH_END(BigDecimal r39_MONTH_END) {
			R39_MONTH_END = r39_MONTH_END;
		}

		public BigDecimal getR39_AVERAGE() {
			return R39_AVERAGE;
		}

		public void setR39_AVERAGE(BigDecimal r39_AVERAGE) {
			R39_AVERAGE = r39_AVERAGE;
		}

		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public String getR40_CROSS_REFERENCE() {
			return R40_CROSS_REFERENCE;
		}

		public void setR40_CROSS_REFERENCE(String r40_CROSS_REFERENCE) {
			R40_CROSS_REFERENCE = r40_CROSS_REFERENCE;
		}

		public BigDecimal getR40_MONTH_END() {
			return R40_MONTH_END;
		}

		public void setR40_MONTH_END(BigDecimal r40_MONTH_END) {
			R40_MONTH_END = r40_MONTH_END;
		}

		public BigDecimal getR40_AVERAGE() {
			return R40_AVERAGE;
		}

		public void setR40_AVERAGE(BigDecimal r40_AVERAGE) {
			R40_AVERAGE = r40_AVERAGE;
		}

		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public String getR41_CROSS_REFERENCE() {
			return R41_CROSS_REFERENCE;
		}

		public void setR41_CROSS_REFERENCE(String r41_CROSS_REFERENCE) {
			R41_CROSS_REFERENCE = r41_CROSS_REFERENCE;
		}

		public BigDecimal getR41_MONTH_END() {
			return R41_MONTH_END;
		}

		public void setR41_MONTH_END(BigDecimal r41_MONTH_END) {
			R41_MONTH_END = r41_MONTH_END;
		}

		public BigDecimal getR41_AVERAGE() {
			return R41_AVERAGE;
		}

		public void setR41_AVERAGE(BigDecimal r41_AVERAGE) {
			R41_AVERAGE = r41_AVERAGE;
		}

		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}

		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}

		public String getR42_CROSS_REFERENCE() {
			return R42_CROSS_REFERENCE;
		}

		public void setR42_CROSS_REFERENCE(String r42_CROSS_REFERENCE) {
			R42_CROSS_REFERENCE = r42_CROSS_REFERENCE;
		}

		public BigDecimal getR42_MONTH_END() {
			return R42_MONTH_END;
		}

		public void setR42_MONTH_END(BigDecimal r42_MONTH_END) {
			R42_MONTH_END = r42_MONTH_END;
		}

		public BigDecimal getR42_AVERAGE() {
			return R42_AVERAGE;
		}

		public void setR42_AVERAGE(BigDecimal r42_AVERAGE) {
			R42_AVERAGE = r42_AVERAGE;
		}

		public String getR43_PRODUCT() {
			return R43_PRODUCT;
		}

		public void setR43_PRODUCT(String r43_PRODUCT) {
			R43_PRODUCT = r43_PRODUCT;
		}

		public String getR43_CROSS_REFERENCE() {
			return R43_CROSS_REFERENCE;
		}

		public void setR43_CROSS_REFERENCE(String r43_CROSS_REFERENCE) {
			R43_CROSS_REFERENCE = r43_CROSS_REFERENCE;
		}

		public BigDecimal getR43_MONTH_END() {
			return R43_MONTH_END;
		}

		public void setR43_MONTH_END(BigDecimal r43_MONTH_END) {
			R43_MONTH_END = r43_MONTH_END;
		}

		public BigDecimal getR43_AVERAGE() {
			return R43_AVERAGE;
		}

		public void setR43_AVERAGE(BigDecimal r43_AVERAGE) {
			R43_AVERAGE = r43_AVERAGE;
		}

		public String getR44_PRODUCT() {
			return R44_PRODUCT;
		}

		public void setR44_PRODUCT(String r44_PRODUCT) {
			R44_PRODUCT = r44_PRODUCT;
		}

		public String getR44_CROSS_REFERENCE() {
			return R44_CROSS_REFERENCE;
		}

		public void setR44_CROSS_REFERENCE(String r44_CROSS_REFERENCE) {
			R44_CROSS_REFERENCE = r44_CROSS_REFERENCE;
		}

		public BigDecimal getR44_MONTH_END() {
			return R44_MONTH_END;
		}

		public void setR44_MONTH_END(BigDecimal r44_MONTH_END) {
			R44_MONTH_END = r44_MONTH_END;
		}

		public BigDecimal getR44_AVERAGE() {
			return R44_AVERAGE;
		}

		public void setR44_AVERAGE(BigDecimal r44_AVERAGE) {
			R44_AVERAGE = r44_AVERAGE;
		}

		public String getR45_PRODUCT() {
			return R45_PRODUCT;
		}

		public void setR45_PRODUCT(String r45_PRODUCT) {
			R45_PRODUCT = r45_PRODUCT;
		}

		public String getR45_CROSS_REFERENCE() {
			return R45_CROSS_REFERENCE;
		}

		public void setR45_CROSS_REFERENCE(String r45_CROSS_REFERENCE) {
			R45_CROSS_REFERENCE = r45_CROSS_REFERENCE;
		}

		public BigDecimal getR45_MONTH_END() {
			return R45_MONTH_END;
		}

		public void setR45_MONTH_END(BigDecimal r45_MONTH_END) {
			R45_MONTH_END = r45_MONTH_END;
		}

		public BigDecimal getR45_AVERAGE() {
			return R45_AVERAGE;
		}

		public void setR45_AVERAGE(BigDecimal r45_AVERAGE) {
			R45_AVERAGE = r45_AVERAGE;
		}

		public String getR46_PRODUCT() {
			return R46_PRODUCT;
		}

		public void setR46_PRODUCT(String r46_PRODUCT) {
			R46_PRODUCT = r46_PRODUCT;
		}

		public String getR46_CROSS_REFERENCE() {
			return R46_CROSS_REFERENCE;
		}

		public void setR46_CROSS_REFERENCE(String r46_CROSS_REFERENCE) {
			R46_CROSS_REFERENCE = r46_CROSS_REFERENCE;
		}

		public BigDecimal getR46_MONTH_END() {
			return R46_MONTH_END;
		}

		public void setR46_MONTH_END(BigDecimal r46_MONTH_END) {
			R46_MONTH_END = r46_MONTH_END;
		}

		public BigDecimal getR46_AVERAGE() {
			return R46_AVERAGE;
		}

		public void setR46_AVERAGE(BigDecimal r46_AVERAGE) {
			R46_AVERAGE = r46_AVERAGE;
		}

		public String getR47_PRODUCT() {
			return R47_PRODUCT;
		}

		public void setR47_PRODUCT(String r47_PRODUCT) {
			R47_PRODUCT = r47_PRODUCT;
		}

		public String getR47_CROSS_REFERENCE() {
			return R47_CROSS_REFERENCE;
		}

		public void setR47_CROSS_REFERENCE(String r47_CROSS_REFERENCE) {
			R47_CROSS_REFERENCE = r47_CROSS_REFERENCE;
		}

		public BigDecimal getR47_MONTH_END() {
			return R47_MONTH_END;
		}

		public void setR47_MONTH_END(BigDecimal r47_MONTH_END) {
			R47_MONTH_END = r47_MONTH_END;
		}

		public BigDecimal getR47_AVERAGE() {
			return R47_AVERAGE;
		}

		public void setR47_AVERAGE(BigDecimal r47_AVERAGE) {
			R47_AVERAGE = r47_AVERAGE;
		}

		public String getR48_PRODUCT() {
			return R48_PRODUCT;
		}

		public void setR48_PRODUCT(String r48_PRODUCT) {
			R48_PRODUCT = r48_PRODUCT;
		}

		public String getR48_CROSS_REFERENCE() {
			return R48_CROSS_REFERENCE;
		}

		public void setR48_CROSS_REFERENCE(String r48_CROSS_REFERENCE) {
			R48_CROSS_REFERENCE = r48_CROSS_REFERENCE;
		}

		public BigDecimal getR48_MONTH_END() {
			return R48_MONTH_END;
		}

		public void setR48_MONTH_END(BigDecimal r48_MONTH_END) {
			R48_MONTH_END = r48_MONTH_END;
		}

		public BigDecimal getR48_AVERAGE() {
			return R48_AVERAGE;
		}

		public void setR48_AVERAGE(BigDecimal r48_AVERAGE) {
			R48_AVERAGE = r48_AVERAGE;
		}

		public String getR49_PRODUCT() {
			return R49_PRODUCT;
		}

		public void setR49_PRODUCT(String r49_PRODUCT) {
			R49_PRODUCT = r49_PRODUCT;
		}

		public String getR49_CROSS_REFERENCE() {
			return R49_CROSS_REFERENCE;
		}

		public void setR49_CROSS_REFERENCE(String r49_CROSS_REFERENCE) {
			R49_CROSS_REFERENCE = r49_CROSS_REFERENCE;
		}

		public BigDecimal getR49_MONTH_END() {
			return R49_MONTH_END;
		}

		public void setR49_MONTH_END(BigDecimal r49_MONTH_END) {
			R49_MONTH_END = r49_MONTH_END;
		}

		public BigDecimal getR49_AVERAGE() {
			return R49_AVERAGE;
		}

		public void setR49_AVERAGE(BigDecimal r49_AVERAGE) {
			R49_AVERAGE = r49_AVERAGE;
		}

		public String getR50_PRODUCT() {
			return R50_PRODUCT;
		}

		public void setR50_PRODUCT(String r50_PRODUCT) {
			R50_PRODUCT = r50_PRODUCT;
		}

		public String getR50_CROSS_REFERENCE() {
			return R50_CROSS_REFERENCE;
		}

		public void setR50_CROSS_REFERENCE(String r50_CROSS_REFERENCE) {
			R50_CROSS_REFERENCE = r50_CROSS_REFERENCE;
		}

		public BigDecimal getR50_MONTH_END() {
			return R50_MONTH_END;
		}

		public void setR50_MONTH_END(BigDecimal r50_MONTH_END) {
			R50_MONTH_END = r50_MONTH_END;
		}

		public BigDecimal getR50_AVERAGE() {
			return R50_AVERAGE;
		}

		public void setR50_AVERAGE(BigDecimal r50_AVERAGE) {
			R50_AVERAGE = r50_AVERAGE;
		}

		public String getR51_PRODUCT() {
			return R51_PRODUCT;
		}

		public void setR51_PRODUCT(String r51_PRODUCT) {
			R51_PRODUCT = r51_PRODUCT;
		}

		public String getR51_CROSS_REFERENCE() {
			return R51_CROSS_REFERENCE;
		}

		public void setR51_CROSS_REFERENCE(String r51_CROSS_REFERENCE) {
			R51_CROSS_REFERENCE = r51_CROSS_REFERENCE;
		}

		public BigDecimal getR51_MONTH_END() {
			return R51_MONTH_END;
		}

		public void setR51_MONTH_END(BigDecimal r51_MONTH_END) {
			R51_MONTH_END = r51_MONTH_END;
		}

		public BigDecimal getR51_AVERAGE() {
			return R51_AVERAGE;
		}

		public void setR51_AVERAGE(BigDecimal r51_AVERAGE) {
			R51_AVERAGE = r51_AVERAGE;
		}

		public String getR52_PRODUCT() {
			return R52_PRODUCT;
		}

		public void setR52_PRODUCT(String r52_PRODUCT) {
			R52_PRODUCT = r52_PRODUCT;
		}

		public String getR52_CROSS_REFERENCE() {
			return R52_CROSS_REFERENCE;
		}

		public void setR52_CROSS_REFERENCE(String r52_CROSS_REFERENCE) {
			R52_CROSS_REFERENCE = r52_CROSS_REFERENCE;
		}

		public BigDecimal getR52_MONTH_END() {
			return R52_MONTH_END;
		}

		public void setR52_MONTH_END(BigDecimal r52_MONTH_END) {
			R52_MONTH_END = r52_MONTH_END;
		}

		public BigDecimal getR52_AVERAGE() {
			return R52_AVERAGE;
		}

		public void setR52_AVERAGE(BigDecimal r52_AVERAGE) {
			R52_AVERAGE = r52_AVERAGE;
		}

		public String getR53_PRODUCT() {
			return R53_PRODUCT;
		}

		public void setR53_PRODUCT(String r53_PRODUCT) {
			R53_PRODUCT = r53_PRODUCT;
		}

		public String getR53_CROSS_REFERENCE() {
			return R53_CROSS_REFERENCE;
		}

		public void setR53_CROSS_REFERENCE(String r53_CROSS_REFERENCE) {
			R53_CROSS_REFERENCE = r53_CROSS_REFERENCE;
		}

		public BigDecimal getR53_MONTH_END() {
			return R53_MONTH_END;
		}

		public void setR53_MONTH_END(BigDecimal r53_MONTH_END) {
			R53_MONTH_END = r53_MONTH_END;
		}

		public BigDecimal getR53_AVERAGE() {
			return R53_AVERAGE;
		}

		public void setR53_AVERAGE(BigDecimal r53_AVERAGE) {
			R53_AVERAGE = r53_AVERAGE;
		}

		public String getR54_PRODUCT() {
			return R54_PRODUCT;
		}

		public void setR54_PRODUCT(String r54_PRODUCT) {
			R54_PRODUCT = r54_PRODUCT;
		}

		public String getR54_CROSS_REFERENCE() {
			return R54_CROSS_REFERENCE;
		}

		public void setR54_CROSS_REFERENCE(String r54_CROSS_REFERENCE) {
			R54_CROSS_REFERENCE = r54_CROSS_REFERENCE;
		}

		public BigDecimal getR54_MONTH_END() {
			return R54_MONTH_END;
		}

		public void setR54_MONTH_END(BigDecimal r54_MONTH_END) {
			R54_MONTH_END = r54_MONTH_END;
		}

		public BigDecimal getR54_AVERAGE() {
			return R54_AVERAGE;
		}

		public void setR54_AVERAGE(BigDecimal r54_AVERAGE) {
			R54_AVERAGE = r54_AVERAGE;
		}

		public String getR55_PRODUCT() {
			return R55_PRODUCT;
		}

		public void setR55_PRODUCT(String r55_PRODUCT) {
			R55_PRODUCT = r55_PRODUCT;
		}

		public String getR55_CROSS_REFERENCE() {
			return R55_CROSS_REFERENCE;
		}

		public void setR55_CROSS_REFERENCE(String r55_CROSS_REFERENCE) {
			R55_CROSS_REFERENCE = r55_CROSS_REFERENCE;
		}

		public BigDecimal getR55_MONTH_END() {
			return R55_MONTH_END;
		}

		public void setR55_MONTH_END(BigDecimal r55_MONTH_END) {
			R55_MONTH_END = r55_MONTH_END;
		}

		public BigDecimal getR55_AVERAGE() {
			return R55_AVERAGE;
		}

		public void setR55_AVERAGE(BigDecimal r55_AVERAGE) {
			R55_AVERAGE = r55_AVERAGE;
		}

		public String getR56_PRODUCT() {
			return R56_PRODUCT;
		}

		public void setR56_PRODUCT(String r56_PRODUCT) {
			R56_PRODUCT = r56_PRODUCT;
		}

		public String getR56_CROSS_REFERENCE() {
			return R56_CROSS_REFERENCE;
		}

		public void setR56_CROSS_REFERENCE(String r56_CROSS_REFERENCE) {
			R56_CROSS_REFERENCE = r56_CROSS_REFERENCE;
		}

		public BigDecimal getR56_MONTH_END() {
			return R56_MONTH_END;
		}

		public void setR56_MONTH_END(BigDecimal r56_MONTH_END) {
			R56_MONTH_END = r56_MONTH_END;
		}

		public BigDecimal getR56_AVERAGE() {
			return R56_AVERAGE;
		}

		public void setR56_AVERAGE(BigDecimal r56_AVERAGE) {
			R56_AVERAGE = r56_AVERAGE;
		}

		public String getR57_PRODUCT() {
			return R57_PRODUCT;
		}

		public void setR57_PRODUCT(String r57_PRODUCT) {
			R57_PRODUCT = r57_PRODUCT;
		}

		public String getR57_CROSS_REFERENCE() {
			return R57_CROSS_REFERENCE;
		}

		public void setR57_CROSS_REFERENCE(String r57_CROSS_REFERENCE) {
			R57_CROSS_REFERENCE = r57_CROSS_REFERENCE;
		}

		public BigDecimal getR57_MONTH_END() {
			return R57_MONTH_END;
		}

		public void setR57_MONTH_END(BigDecimal r57_MONTH_END) {
			R57_MONTH_END = r57_MONTH_END;
		}

		public BigDecimal getR57_AVERAGE() {
			return R57_AVERAGE;
		}

		public void setR57_AVERAGE(BigDecimal r57_AVERAGE) {
			R57_AVERAGE = r57_AVERAGE;
		}

		public String getR58_PRODUCT() {
			return R58_PRODUCT;
		}

		public void setR58_PRODUCT(String r58_PRODUCT) {
			R58_PRODUCT = r58_PRODUCT;
		}

		public String getR58_CROSS_REFERENCE() {
			return R58_CROSS_REFERENCE;
		}

		public void setR58_CROSS_REFERENCE(String r58_CROSS_REFERENCE) {
			R58_CROSS_REFERENCE = r58_CROSS_REFERENCE;
		}

		public BigDecimal getR58_MONTH_END() {
			return R58_MONTH_END;
		}

		public void setR58_MONTH_END(BigDecimal r58_MONTH_END) {
			R58_MONTH_END = r58_MONTH_END;
		}

		public BigDecimal getR58_AVERAGE() {
			return R58_AVERAGE;
		}

		public void setR58_AVERAGE(BigDecimal r58_AVERAGE) {
			R58_AVERAGE = r58_AVERAGE;
		}

		public String getR59_PRODUCT() {
			return R59_PRODUCT;
		}

		public void setR59_PRODUCT(String r59_PRODUCT) {
			R59_PRODUCT = r59_PRODUCT;
		}

		public String getR59_CROSS_REFERENCE() {
			return R59_CROSS_REFERENCE;
		}

		public void setR59_CROSS_REFERENCE(String r59_CROSS_REFERENCE) {
			R59_CROSS_REFERENCE = r59_CROSS_REFERENCE;
		}

		public BigDecimal getR59_MONTH_END() {
			return R59_MONTH_END;
		}

		public void setR59_MONTH_END(BigDecimal r59_MONTH_END) {
			R59_MONTH_END = r59_MONTH_END;
		}

		public BigDecimal getR59_AVERAGE() {
			return R59_AVERAGE;
		}

		public void setR59_AVERAGE(BigDecimal r59_AVERAGE) {
			R59_AVERAGE = r59_AVERAGE;
		}

		public String getR60_PRODUCT() {
			return R60_PRODUCT;
		}

		public void setR60_PRODUCT(String r60_PRODUCT) {
			R60_PRODUCT = r60_PRODUCT;
		}

		public String getR60_CROSS_REFERENCE() {
			return R60_CROSS_REFERENCE;
		}

		public void setR60_CROSS_REFERENCE(String r60_CROSS_REFERENCE) {
			R60_CROSS_REFERENCE = r60_CROSS_REFERENCE;
		}

		public BigDecimal getR60_MONTH_END() {
			return R60_MONTH_END;
		}

		public void setR60_MONTH_END(BigDecimal r60_MONTH_END) {
			R60_MONTH_END = r60_MONTH_END;
		}

		public BigDecimal getR60_AVERAGE() {
			return R60_AVERAGE;
		}

		public void setR60_AVERAGE(BigDecimal r60_AVERAGE) {
			R60_AVERAGE = r60_AVERAGE;
		}

		public String getR61_PRODUCT() {
			return R61_PRODUCT;
		}

		public void setR61_PRODUCT(String r61_PRODUCT) {
			R61_PRODUCT = r61_PRODUCT;
		}

		public String getR61_CROSS_REFERENCE() {
			return R61_CROSS_REFERENCE;
		}

		public void setR61_CROSS_REFERENCE(String r61_CROSS_REFERENCE) {
			R61_CROSS_REFERENCE = r61_CROSS_REFERENCE;
		}

		public BigDecimal getR61_MONTH_END() {
			return R61_MONTH_END;
		}

		public void setR61_MONTH_END(BigDecimal r61_MONTH_END) {
			R61_MONTH_END = r61_MONTH_END;
		}

		public BigDecimal getR61_AVERAGE() {
			return R61_AVERAGE;
		}

		public void setR61_AVERAGE(BigDecimal r61_AVERAGE) {
			R61_AVERAGE = r61_AVERAGE;
		}

		public String getR62_PRODUCT() {
			return R62_PRODUCT;
		}

		public void setR62_PRODUCT(String r62_PRODUCT) {
			R62_PRODUCT = r62_PRODUCT;
		}

		public String getR62_CROSS_REFERENCE() {
			return R62_CROSS_REFERENCE;
		}

		public void setR62_CROSS_REFERENCE(String r62_CROSS_REFERENCE) {
			R62_CROSS_REFERENCE = r62_CROSS_REFERENCE;
		}

		public BigDecimal getR62_MONTH_END() {
			return R62_MONTH_END;
		}

		public void setR62_MONTH_END(BigDecimal r62_MONTH_END) {
			R62_MONTH_END = r62_MONTH_END;
		}

		public BigDecimal getR62_AVERAGE() {
			return R62_AVERAGE;
		}

		public void setR62_AVERAGE(BigDecimal r62_AVERAGE) {
			R62_AVERAGE = r62_AVERAGE;
		}

		public String getR63_PRODUCT() {
			return R63_PRODUCT;
		}

		public void setR63_PRODUCT(String r63_PRODUCT) {
			R63_PRODUCT = r63_PRODUCT;
		}

		public String getR63_CROSS_REFERENCE() {
			return R63_CROSS_REFERENCE;
		}

		public void setR63_CROSS_REFERENCE(String r63_CROSS_REFERENCE) {
			R63_CROSS_REFERENCE = r63_CROSS_REFERENCE;
		}

		public BigDecimal getR63_MONTH_END() {
			return R63_MONTH_END;
		}

		public void setR63_MONTH_END(BigDecimal r63_MONTH_END) {
			R63_MONTH_END = r63_MONTH_END;
		}

		public BigDecimal getR63_AVERAGE() {
			return R63_AVERAGE;
		}

		public void setR63_AVERAGE(BigDecimal r63_AVERAGE) {
			R63_AVERAGE = r63_AVERAGE;
		}

		public String getR64_PRODUCT() {
			return R64_PRODUCT;
		}

		public void setR64_PRODUCT(String r64_PRODUCT) {
			R64_PRODUCT = r64_PRODUCT;
		}

		public String getR64_CROSS_REFERENCE() {
			return R64_CROSS_REFERENCE;
		}

		public void setR64_CROSS_REFERENCE(String r64_CROSS_REFERENCE) {
			R64_CROSS_REFERENCE = r64_CROSS_REFERENCE;
		}

		public BigDecimal getR64_MONTH_END() {
			return R64_MONTH_END;
		}

		public void setR64_MONTH_END(BigDecimal r64_MONTH_END) {
			R64_MONTH_END = r64_MONTH_END;
		}

		public BigDecimal getR64_AVERAGE() {
			return R64_AVERAGE;
		}

		public void setR64_AVERAGE(BigDecimal r64_AVERAGE) {
			R64_AVERAGE = r64_AVERAGE;
		}

		public String getR65_PRODUCT() {
			return R65_PRODUCT;
		}

		public void setR65_PRODUCT(String r65_PRODUCT) {
			R65_PRODUCT = r65_PRODUCT;
		}

		public String getR65_CROSS_REFERENCE() {
			return R65_CROSS_REFERENCE;
		}

		public void setR65_CROSS_REFERENCE(String r65_CROSS_REFERENCE) {
			R65_CROSS_REFERENCE = r65_CROSS_REFERENCE;
		}

		public BigDecimal getR65_MONTH_END() {
			return R65_MONTH_END;
		}

		public void setR65_MONTH_END(BigDecimal r65_MONTH_END) {
			R65_MONTH_END = r65_MONTH_END;
		}

		public BigDecimal getR65_AVERAGE() {
			return R65_AVERAGE;
		}

		public void setR65_AVERAGE(BigDecimal r65_AVERAGE) {
			R65_AVERAGE = r65_AVERAGE;
		}

		public String getR66_PRODUCT() {
			return R66_PRODUCT;
		}

		public void setR66_PRODUCT(String r66_PRODUCT) {
			R66_PRODUCT = r66_PRODUCT;
		}

		public String getR66_CROSS_REFERENCE() {
			return R66_CROSS_REFERENCE;
		}

		public void setR66_CROSS_REFERENCE(String r66_CROSS_REFERENCE) {
			R66_CROSS_REFERENCE = r66_CROSS_REFERENCE;
		}

		public BigDecimal getR66_MONTH_END() {
			return R66_MONTH_END;
		}

		public void setR66_MONTH_END(BigDecimal r66_MONTH_END) {
			R66_MONTH_END = r66_MONTH_END;
		}

		public BigDecimal getR66_AVERAGE() {
			return R66_AVERAGE;
		}

		public void setR66_AVERAGE(BigDecimal r66_AVERAGE) {
			R66_AVERAGE = r66_AVERAGE;
		}

		public String getR67_PRODUCT() {
			return R67_PRODUCT;
		}

		public void setR67_PRODUCT(String r67_PRODUCT) {
			R67_PRODUCT = r67_PRODUCT;
		}

		public String getR67_CROSS_REFERENCE() {
			return R67_CROSS_REFERENCE;
		}

		public void setR67_CROSS_REFERENCE(String r67_CROSS_REFERENCE) {
			R67_CROSS_REFERENCE = r67_CROSS_REFERENCE;
		}

		public BigDecimal getR67_MONTH_END() {
			return R67_MONTH_END;
		}

		public void setR67_MONTH_END(BigDecimal r67_MONTH_END) {
			R67_MONTH_END = r67_MONTH_END;
		}

		public BigDecimal getR67_AVERAGE() {
			return R67_AVERAGE;
		}

		public void setR67_AVERAGE(BigDecimal r67_AVERAGE) {
			R67_AVERAGE = r67_AVERAGE;
		}

		public String getR68_PRODUCT() {
			return R68_PRODUCT;
		}

		public void setR68_PRODUCT(String r68_PRODUCT) {
			R68_PRODUCT = r68_PRODUCT;
		}

		public String getR68_CROSS_REFERENCE() {
			return R68_CROSS_REFERENCE;
		}

		public void setR68_CROSS_REFERENCE(String r68_CROSS_REFERENCE) {
			R68_CROSS_REFERENCE = r68_CROSS_REFERENCE;
		}

		public BigDecimal getR68_MONTH_END() {
			return R68_MONTH_END;
		}

		public void setR68_MONTH_END(BigDecimal r68_MONTH_END) {
			R68_MONTH_END = r68_MONTH_END;
		}

		public BigDecimal getR68_AVERAGE() {
			return R68_AVERAGE;
		}

		public void setR68_AVERAGE(BigDecimal r68_AVERAGE) {
			R68_AVERAGE = r68_AVERAGE;
		}

		public String getR69_PRODUCT() {
			return R69_PRODUCT;
		}

		public void setR69_PRODUCT(String r69_PRODUCT) {
			R69_PRODUCT = r69_PRODUCT;
		}

		public String getR69_CROSS_REFERENCE() {
			return R69_CROSS_REFERENCE;
		}

		public void setR69_CROSS_REFERENCE(String r69_CROSS_REFERENCE) {
			R69_CROSS_REFERENCE = r69_CROSS_REFERENCE;
		}

		public BigDecimal getR69_MONTH_END() {
			return R69_MONTH_END;
		}

		public void setR69_MONTH_END(BigDecimal r69_MONTH_END) {
			R69_MONTH_END = r69_MONTH_END;
		}

		public BigDecimal getR69_AVERAGE() {
			return R69_AVERAGE;
		}

		public void setR69_AVERAGE(BigDecimal r69_AVERAGE) {
			R69_AVERAGE = r69_AVERAGE;
		}

		public String getR70_PRODUCT() {
			return R70_PRODUCT;
		}

		public void setR70_PRODUCT(String r70_PRODUCT) {
			R70_PRODUCT = r70_PRODUCT;
		}

		public String getR70_CROSS_REFERENCE() {
			return R70_CROSS_REFERENCE;
		}

		public void setR70_CROSS_REFERENCE(String r70_CROSS_REFERENCE) {
			R70_CROSS_REFERENCE = r70_CROSS_REFERENCE;
		}

		public BigDecimal getR70_MONTH_END() {
			return R70_MONTH_END;
		}

		public void setR70_MONTH_END(BigDecimal r70_MONTH_END) {
			R70_MONTH_END = r70_MONTH_END;
		}

		public BigDecimal getR70_AVERAGE() {
			return R70_AVERAGE;
		}

		public void setR70_AVERAGE(BigDecimal r70_AVERAGE) {
			R70_AVERAGE = r70_AVERAGE;
		}

		public String getR71_PRODUCT() {
			return R71_PRODUCT;
		}

		public void setR71_PRODUCT(String r71_PRODUCT) {
			R71_PRODUCT = r71_PRODUCT;
		}

		public String getR71_CROSS_REFERENCE() {
			return R71_CROSS_REFERENCE;
		}

		public void setR71_CROSS_REFERENCE(String r71_CROSS_REFERENCE) {
			R71_CROSS_REFERENCE = r71_CROSS_REFERENCE;
		}

		public BigDecimal getR71_MONTH_END() {
			return R71_MONTH_END;
		}

		public void setR71_MONTH_END(BigDecimal r71_MONTH_END) {
			R71_MONTH_END = r71_MONTH_END;
		}

		public BigDecimal getR71_AVERAGE() {
			return R71_AVERAGE;
		}

		public void setR71_AVERAGE(BigDecimal r71_AVERAGE) {
			R71_AVERAGE = r71_AVERAGE;
		}

		public String getR72_PRODUCT() {
			return R72_PRODUCT;
		}

		public void setR72_PRODUCT(String r72_PRODUCT) {
			R72_PRODUCT = r72_PRODUCT;
		}

		public String getR72_CROSS_REFERENCE() {
			return R72_CROSS_REFERENCE;
		}

		public void setR72_CROSS_REFERENCE(String r72_CROSS_REFERENCE) {
			R72_CROSS_REFERENCE = r72_CROSS_REFERENCE;
		}

		public BigDecimal getR72_MONTH_END() {
			return R72_MONTH_END;
		}

		public void setR72_MONTH_END(BigDecimal r72_MONTH_END) {
			R72_MONTH_END = r72_MONTH_END;
		}

		public BigDecimal getR72_AVERAGE() {
			return R72_AVERAGE;
		}

		public void setR72_AVERAGE(BigDecimal r72_AVERAGE) {
			R72_AVERAGE = r72_AVERAGE;
		}

		public String getR73_PRODUCT() {
			return R73_PRODUCT;
		}

		public void setR73_PRODUCT(String r73_PRODUCT) {
			R73_PRODUCT = r73_PRODUCT;
		}

		public String getR73_CROSS_REFERENCE() {
			return R73_CROSS_REFERENCE;
		}

		public void setR73_CROSS_REFERENCE(String r73_CROSS_REFERENCE) {
			R73_CROSS_REFERENCE = r73_CROSS_REFERENCE;
		}

		public BigDecimal getR73_MONTH_END() {
			return R73_MONTH_END;
		}

		public void setR73_MONTH_END(BigDecimal r73_MONTH_END) {
			R73_MONTH_END = r73_MONTH_END;
		}

		public BigDecimal getR73_AVERAGE() {
			return R73_AVERAGE;
		}

		public void setR73_AVERAGE(BigDecimal r73_AVERAGE) {
			R73_AVERAGE = r73_AVERAGE;
		}

		public String getR74_PRODUCT() {
			return R74_PRODUCT;
		}

		public void setR74_PRODUCT(String r74_PRODUCT) {
			R74_PRODUCT = r74_PRODUCT;
		}

		public String getR74_CROSS_REFERENCE() {
			return R74_CROSS_REFERENCE;
		}

		public void setR74_CROSS_REFERENCE(String r74_CROSS_REFERENCE) {
			R74_CROSS_REFERENCE = r74_CROSS_REFERENCE;
		}

		public BigDecimal getR74_MONTH_END() {
			return R74_MONTH_END;
		}

		public void setR74_MONTH_END(BigDecimal r74_MONTH_END) {
			R74_MONTH_END = r74_MONTH_END;
		}

		public BigDecimal getR74_AVERAGE() {
			return R74_AVERAGE;
		}

		public void setR74_AVERAGE(BigDecimal r74_AVERAGE) {
			R74_AVERAGE = r74_AVERAGE;
		}

		public String getR75_PRODUCT() {
			return R75_PRODUCT;
		}

		public void setR75_PRODUCT(String r75_PRODUCT) {
			R75_PRODUCT = r75_PRODUCT;
		}

		public String getR75_CROSS_REFERENCE() {
			return R75_CROSS_REFERENCE;
		}

		public void setR75_CROSS_REFERENCE(String r75_CROSS_REFERENCE) {
			R75_CROSS_REFERENCE = r75_CROSS_REFERENCE;
		}

		public BigDecimal getR75_MONTH_END() {
			return R75_MONTH_END;
		}

		public void setR75_MONTH_END(BigDecimal r75_MONTH_END) {
			R75_MONTH_END = r75_MONTH_END;
		}

		public BigDecimal getR75_AVERAGE() {
			return R75_AVERAGE;
		}

		public void setR75_AVERAGE(BigDecimal r75_AVERAGE) {
			R75_AVERAGE = r75_AVERAGE;
		}

		public String getR76_PRODUCT() {
			return R76_PRODUCT;
		}

		public void setR76_PRODUCT(String r76_PRODUCT) {
			R76_PRODUCT = r76_PRODUCT;
		}

		public String getR76_CROSS_REFERENCE() {
			return R76_CROSS_REFERENCE;
		}

		public void setR76_CROSS_REFERENCE(String r76_CROSS_REFERENCE) {
			R76_CROSS_REFERENCE = r76_CROSS_REFERENCE;
		}

		public BigDecimal getR76_MONTH_END() {
			return R76_MONTH_END;
		}

		public void setR76_MONTH_END(BigDecimal r76_MONTH_END) {
			R76_MONTH_END = r76_MONTH_END;
		}

		public BigDecimal getR76_AVERAGE() {
			return R76_AVERAGE;
		}

		public void setR76_AVERAGE(BigDecimal r76_AVERAGE) {
			R76_AVERAGE = r76_AVERAGE;
		}

		public String getR77_PRODUCT() {
			return R77_PRODUCT;
		}

		public void setR77_PRODUCT(String r77_PRODUCT) {
			R77_PRODUCT = r77_PRODUCT;
		}

		public String getR77_CROSS_REFERENCE() {
			return R77_CROSS_REFERENCE;
		}

		public void setR77_CROSS_REFERENCE(String r77_CROSS_REFERENCE) {
			R77_CROSS_REFERENCE = r77_CROSS_REFERENCE;
		}

		public BigDecimal getR77_MONTH_END() {
			return R77_MONTH_END;
		}

		public void setR77_MONTH_END(BigDecimal r77_MONTH_END) {
			R77_MONTH_END = r77_MONTH_END;
		}

		public BigDecimal getR77_AVERAGE() {
			return R77_AVERAGE;
		}

		public void setR77_AVERAGE(BigDecimal r77_AVERAGE) {
			R77_AVERAGE = r77_AVERAGE;
		}

		public String getR78_PRODUCT() {
			return R78_PRODUCT;
		}

		public void setR78_PRODUCT(String r78_PRODUCT) {
			R78_PRODUCT = r78_PRODUCT;
		}

		public String getR78_CROSS_REFERENCE() {
			return R78_CROSS_REFERENCE;
		}

		public void setR78_CROSS_REFERENCE(String r78_CROSS_REFERENCE) {
			R78_CROSS_REFERENCE = r78_CROSS_REFERENCE;
		}

		public BigDecimal getR78_MONTH_END() {
			return R78_MONTH_END;
		}

		public void setR78_MONTH_END(BigDecimal r78_MONTH_END) {
			R78_MONTH_END = r78_MONTH_END;
		}

		public BigDecimal getR78_AVERAGE() {
			return R78_AVERAGE;
		}

		public void setR78_AVERAGE(BigDecimal r78_AVERAGE) {
			R78_AVERAGE = r78_AVERAGE;
		}

		public String getR79_PRODUCT() {
			return R79_PRODUCT;
		}

		public void setR79_PRODUCT(String r79_PRODUCT) {
			R79_PRODUCT = r79_PRODUCT;
		}

		public String getR79_CROSS_REFERENCE() {
			return R79_CROSS_REFERENCE;
		}

		public void setR79_CROSS_REFERENCE(String r79_CROSS_REFERENCE) {
			R79_CROSS_REFERENCE = r79_CROSS_REFERENCE;
		}

		public BigDecimal getR79_MONTH_END() {
			return R79_MONTH_END;
		}

		public void setR79_MONTH_END(BigDecimal r79_MONTH_END) {
			R79_MONTH_END = r79_MONTH_END;
		}

		public BigDecimal getR79_AVERAGE() {
			return R79_AVERAGE;
		}

		public void setR79_AVERAGE(BigDecimal r79_AVERAGE) {
			R79_AVERAGE = r79_AVERAGE;
		}

		public String getR80_PRODUCT() {
			return R80_PRODUCT;
		}

		public void setR80_PRODUCT(String r80_PRODUCT) {
			R80_PRODUCT = r80_PRODUCT;
		}

		public String getR80_CROSS_REFERENCE() {
			return R80_CROSS_REFERENCE;
		}

		public void setR80_CROSS_REFERENCE(String r80_CROSS_REFERENCE) {
			R80_CROSS_REFERENCE = r80_CROSS_REFERENCE;
		}

		public BigDecimal getR80_MONTH_END() {
			return R80_MONTH_END;
		}

		public void setR80_MONTH_END(BigDecimal r80_MONTH_END) {
			R80_MONTH_END = r80_MONTH_END;
		}

		public BigDecimal getR80_AVERAGE() {
			return R80_AVERAGE;
		}

		public void setR80_AVERAGE(BigDecimal r80_AVERAGE) {
			R80_AVERAGE = r80_AVERAGE;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date rEPORT_DATE) {
			REPORT_DATE = rEPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
			REPORT_VERSION = rEPORT_VERSION;
		}

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}

		public String getREPORT_FREQUENCY() {
			return REPORT_FREQUENCY;
		}

		public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
			REPORT_FREQUENCY = rEPORT_FREQUENCY;
		}

		public String getREPORT_CODE() {
			return REPORT_CODE;
		}

		public void setREPORT_CODE(String rEPORT_CODE) {
			REPORT_CODE = rEPORT_CODE;
		}

		public String getREPORT_DESC() {
			return REPORT_DESC;
		}

		public void setREPORT_DESC(String rEPORT_DESC) {
			REPORT_DESC = rEPORT_DESC;
		}

		public String getENTITY_FLG() {
			return ENTITY_FLG;
		}

		public void setENTITY_FLG(String eNTITY_FLG) {
			ENTITY_FLG = eNTITY_FLG;
		}

		public String getMODIFY_FLG() {
			return MODIFY_FLG;
		}

		public void setMODIFY_FLG(String mODIFY_FLG) {
			MODIFY_FLG = mODIFY_FLG;
		}

		public String getDEL_FLG() {
			return DEL_FLG;
		}

		public void setDEL_FLG(String dEL_FLG) {
			DEL_FLG = dEL_FLG;
		}

		public M_SFINP2_RESUB_Summary_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

// ------------------------------
// M_SFINP2_RESUB_Detail_Entity
// ------------------------------
	public static class M_SFINP2_RESUB_Detail_Entity {

		@Id
		@Column(name = "CUST_ID")
		private String custId;

		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "COLUMN_ID")
		private String columnId;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria_1;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
		private BigDecimal acctBalanceInpula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;
		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private char entityFlg;

		@Column(name = "MODIFY_FLG")
		private char modifyFlg;

		@Column(name = "DEL_FLG")
		private char delFlg;

		@Column(name = "SANCTION_LIMIT")
		private String sanctionLimit;

		@Column(name = "AVERAGE", precision = 24, scale = 3)
		private BigDecimal average;

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getColumnId() {
			return columnId;
		}

		public void setColumnId(String columnId) {
			this.columnId = columnId;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria_1() {
			return reportAddlCriteria_1;
		}

		public void setReportAddlCriteria_1(String reportAddlCriteria_1) {
			this.reportAddlCriteria_1 = reportAddlCriteria_1;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public char getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(char entityFlg) {
			this.entityFlg = entityFlg;
		}

		public char getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(char modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public char getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(char delFlg) {
			this.delFlg = delFlg;
		}

		public String getSanctionLimit() {
			return sanctionLimit;
		}

		public void setSanctionLimit(String sanctionLimit) {
			this.sanctionLimit = sanctionLimit;
		}

		public BigDecimal getAverage() {
			return average;
		}

		public void setAverage(BigDecimal average) {
			this.average = average;
		}

		public M_SFINP2_RESUB_Detail_Entity() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

}
