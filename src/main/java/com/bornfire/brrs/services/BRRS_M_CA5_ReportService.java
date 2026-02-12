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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.BRRS_M_CA5_Archival_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA5_Archival_Summary_Repo2;
import com.bornfire.brrs.entities.BRRS_M_CA5_Summary_Repo1;
import com.bornfire.brrs.entities.BRRS_M_CA5_Summary_Repo2;
import com.bornfire.brrs.entities.M_CA5_Archival_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA5_Archival_Summary_Entity2;
import com.bornfire.brrs.entities.M_CA5_Summary_Entity1;
import com.bornfire.brrs.entities.M_CA5_Summary_Entity2;

@Component
@Service

public class BRRS_M_CA5_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_CA5_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRS_M_CA5_Summary_Repo1 BRRS_M_CA5_Summary_Repo1;

	@Autowired
	BRRS_M_CA5_Summary_Repo2 BRRS_M_CA5_Summary_Repo2;

	/*
	 * @Autowired BRRS_M_CA5_Detail_Repo BRRS_M_CA5_Detail_Repo;
	 * 
	 * @Autowired BRRS_M_CA5_Archival_Detail_Repo M_CA5_Archival_Detail_Repo;
	 */

	@Autowired
	BRRS_M_CA5_Archival_Summary_Repo1 M_CA5_Archival_Summary_Repo1;

	@Autowired
	BRRS_M_CA5_Archival_Summary_Repo2 M_CA5_Archival_Summary_Repo2;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_CA5View(String reportId, String fromdate, String todate, String currency, String dtltype,
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

				List<M_CA5_Archival_Summary_Entity1> T1Master = M_CA5_Archival_Summary_Repo1
						.getdatabydateListarchival(d1, version);
				List<M_CA5_Archival_Summary_Entity2> T2Master = M_CA5_Archival_Summary_Repo2
						.getdatabydateListarchival(d1, version);

				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);

			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_CA5_Archival_Summary_Entity1> T1Master = M_CA5_Archival_Summary_Repo1
						.getdatabydateListarchival(d1, version);
				List<M_CA5_Archival_Summary_Entity2> T2Master = M_CA5_Archival_Summary_Repo2
						.getdatabydateListarchival(d1, version);

				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);

			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<M_CA5_Summary_Entity1> T1Master = BRRS_M_CA5_Summary_Repo1.getdatabydateList(d1);
				List<M_CA5_Summary_Entity2> T2Master = BRRS_M_CA5_Summary_Repo2.getdatabydateList(d1);

				System.out.println("T1Master Size: " + T1Master.size());
				System.out.println("T2Master Size: " + T2Master.size());

				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);

			}

			mv.setViewName("BRRS/M_CA5");
			mv.addObject("displaymode", "summary");
			System.out.println("✅ View set: " + mv.getViewName());

		} catch (ParseException e) {
			e.printStackTrace();
			mv.addObject("error", "Invalid date format for: " + todate);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("error", "An error occurred while fetching M_LARADV data.");
		}

		return mv;
	}

	public void updateReport(M_CA5_Summary_Entity1 updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_CA5_Summary_Entity1 existing = BRRS_M_CA5_Summary_Repo1.findById(updatedEntity.getReportDate()).orElseThrow(
				() -> new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		try {
			// 1️⃣ Loop through R14 to R100
			for (int i = 14; i <= 100; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "line_no", "note_holders", "name_of_instrument_programe",
						"issuing_entity_if_issued_throughan_spv", "issuance_date", "contractual_maturity_date",
						"effective_maturity_date_if_date", "amount", "amount_derecognised_p000" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA5_Summary_Entity1.class.getMethod(getterName);
						Method setter = M_CA5_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R100 total fields using same structure
			String prefix = "R100_";
			String[] totalFields = { "line_no", "note_holders", "name_of_instrument_programe",
					"issuing_entity_if_issued_throughan_spv", "issuance_date", "contractual_maturity_date",
					"effective_maturity_date_if_date", "amount", "amount_derecognised_p000", "total_p000" };

			for (String field : totalFields) {
				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					Method getter = M_CA5_Summary_Entity1.class.getMethod(getterName);
					Method setter = M_CA5_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());

					Object newValue = getter.invoke(updatedEntity);
					setter.invoke(existing, newValue);

				} catch (NoSuchMethodException e) {
					// Skip missing total fields
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		try {
			existing.setR14_issuance_date(updatedEntity.getR14_issuance_date());
			existing.setR14_contractual_maturity_date(updatedEntity.getR14_contractual_maturity_date());
			existing.setR14_effective_maturity_date_if_date(updatedEntity.getR14_effective_maturity_date_if_date());

			existing.setR15_issuance_date(updatedEntity.getR15_issuance_date());
			existing.setR15_contractual_maturity_date(updatedEntity.getR15_contractual_maturity_date());
			existing.setR15_effective_maturity_date_if_date(updatedEntity.getR15_effective_maturity_date_if_date());

			existing.setR16_issuance_date(updatedEntity.getR16_issuance_date());
			existing.setR16_contractual_maturity_date(updatedEntity.getR16_contractual_maturity_date());
			existing.setR16_effective_maturity_date_if_date(updatedEntity.getR16_effective_maturity_date_if_date());

			existing.setR17_issuance_date(updatedEntity.getR17_issuance_date());
			existing.setR17_contractual_maturity_date(updatedEntity.getR17_contractual_maturity_date());
			existing.setR17_effective_maturity_date_if_date(updatedEntity.getR17_effective_maturity_date_if_date());

			existing.setR18_issuance_date(updatedEntity.getR18_issuance_date());
			existing.setR18_contractual_maturity_date(updatedEntity.getR18_contractual_maturity_date());
			existing.setR18_effective_maturity_date_if_date(updatedEntity.getR18_effective_maturity_date_if_date());

			existing.setR19_issuance_date(updatedEntity.getR19_issuance_date());
			existing.setR19_contractual_maturity_date(updatedEntity.getR19_contractual_maturity_date());
			existing.setR19_effective_maturity_date_if_date(updatedEntity.getR19_effective_maturity_date_if_date());

			existing.setR20_issuance_date(updatedEntity.getR20_issuance_date());
			existing.setR20_contractual_maturity_date(updatedEntity.getR20_contractual_maturity_date());
			existing.setR20_effective_maturity_date_if_date(updatedEntity.getR20_effective_maturity_date_if_date());

			existing.setR21_issuance_date(updatedEntity.getR21_issuance_date());
			existing.setR21_contractual_maturity_date(updatedEntity.getR21_contractual_maturity_date());
			existing.setR21_effective_maturity_date_if_date(updatedEntity.getR21_effective_maturity_date_if_date());

			existing.setR22_issuance_date(updatedEntity.getR22_issuance_date());
			existing.setR22_contractual_maturity_date(updatedEntity.getR22_contractual_maturity_date());
			existing.setR22_effective_maturity_date_if_date(updatedEntity.getR22_effective_maturity_date_if_date());

			existing.setR23_issuance_date(updatedEntity.getR23_issuance_date());
			existing.setR23_contractual_maturity_date(updatedEntity.getR23_contractual_maturity_date());
			existing.setR23_effective_maturity_date_if_date(updatedEntity.getR23_effective_maturity_date_if_date());

			existing.setR24_issuance_date(updatedEntity.getR24_issuance_date());
			existing.setR24_contractual_maturity_date(updatedEntity.getR24_contractual_maturity_date());
			existing.setR24_effective_maturity_date_if_date(updatedEntity.getR24_effective_maturity_date_if_date());

			existing.setR25_issuance_date(updatedEntity.getR25_issuance_date());
			existing.setR25_contractual_maturity_date(updatedEntity.getR25_contractual_maturity_date());
			existing.setR25_effective_maturity_date_if_date(updatedEntity.getR25_effective_maturity_date_if_date());

			existing.setR26_issuance_date(updatedEntity.getR26_issuance_date());
			existing.setR26_contractual_maturity_date(updatedEntity.getR26_contractual_maturity_date());
			existing.setR26_effective_maturity_date_if_date(updatedEntity.getR26_effective_maturity_date_if_date());

			existing.setR27_issuance_date(updatedEntity.getR27_issuance_date());
			existing.setR27_contractual_maturity_date(updatedEntity.getR27_contractual_maturity_date());
			existing.setR27_effective_maturity_date_if_date(updatedEntity.getR27_effective_maturity_date_if_date());

			existing.setR28_issuance_date(updatedEntity.getR28_issuance_date());
			existing.setR28_contractual_maturity_date(updatedEntity.getR28_contractual_maturity_date());
			existing.setR28_effective_maturity_date_if_date(updatedEntity.getR28_effective_maturity_date_if_date());

			existing.setR35_issuance_date(updatedEntity.getR35_issuance_date());
			existing.setR35_contractual_maturity_date(updatedEntity.getR35_contractual_maturity_date());
			existing.setR35_effective_maturity_date_if_date(updatedEntity.getR35_effective_maturity_date_if_date());

			existing.setR36_issuance_date(updatedEntity.getR36_issuance_date());
			existing.setR36_contractual_maturity_date(updatedEntity.getR36_contractual_maturity_date());
			existing.setR36_effective_maturity_date_if_date(updatedEntity.getR36_effective_maturity_date_if_date());

			existing.setR37_issuance_date(updatedEntity.getR37_issuance_date());
			existing.setR37_contractual_maturity_date(updatedEntity.getR37_contractual_maturity_date());
			existing.setR37_effective_maturity_date_if_date(updatedEntity.getR37_effective_maturity_date_if_date());

			existing.setR38_issuance_date(updatedEntity.getR38_issuance_date());
			existing.setR38_contractual_maturity_date(updatedEntity.getR38_contractual_maturity_date());
			existing.setR38_effective_maturity_date_if_date(updatedEntity.getR38_effective_maturity_date_if_date());

			existing.setR39_issuance_date(updatedEntity.getR39_issuance_date());
			existing.setR39_contractual_maturity_date(updatedEntity.getR39_contractual_maturity_date());
			existing.setR39_effective_maturity_date_if_date(updatedEntity.getR39_effective_maturity_date_if_date());

			existing.setR40_issuance_date(updatedEntity.getR40_issuance_date());
			existing.setR40_contractual_maturity_date(updatedEntity.getR40_contractual_maturity_date());
			existing.setR40_effective_maturity_date_if_date(updatedEntity.getR40_effective_maturity_date_if_date());

			existing.setR41_issuance_date(updatedEntity.getR41_issuance_date());
			existing.setR41_contractual_maturity_date(updatedEntity.getR41_contractual_maturity_date());
			existing.setR41_effective_maturity_date_if_date(updatedEntity.getR41_effective_maturity_date_if_date());

			existing.setR42_issuance_date(updatedEntity.getR42_issuance_date());
			existing.setR42_contractual_maturity_date(updatedEntity.getR42_contractual_maturity_date());
			existing.setR42_effective_maturity_date_if_date(updatedEntity.getR42_effective_maturity_date_if_date());

			existing.setR43_issuance_date(updatedEntity.getR43_issuance_date());
			existing.setR43_contractual_maturity_date(updatedEntity.getR43_contractual_maturity_date());
			existing.setR43_effective_maturity_date_if_date(updatedEntity.getR43_effective_maturity_date_if_date());

			existing.setR44_issuance_date(updatedEntity.getR44_issuance_date());
			existing.setR44_contractual_maturity_date(updatedEntity.getR44_contractual_maturity_date());
			existing.setR44_effective_maturity_date_if_date(updatedEntity.getR44_effective_maturity_date_if_date());

			existing.setR45_issuance_date(updatedEntity.getR45_issuance_date());
			existing.setR45_contractual_maturity_date(updatedEntity.getR45_contractual_maturity_date());
			existing.setR45_effective_maturity_date_if_date(updatedEntity.getR45_effective_maturity_date_if_date());

			existing.setR46_issuance_date(updatedEntity.getR46_issuance_date());
			existing.setR46_contractual_maturity_date(updatedEntity.getR46_contractual_maturity_date());
			existing.setR46_effective_maturity_date_if_date(updatedEntity.getR46_effective_maturity_date_if_date());

			existing.setR47_issuance_date(updatedEntity.getR47_issuance_date());
			existing.setR47_contractual_maturity_date(updatedEntity.getR47_contractual_maturity_date());
			existing.setR47_effective_maturity_date_if_date(updatedEntity.getR47_effective_maturity_date_if_date());

			existing.setR48_issuance_date(updatedEntity.getR48_issuance_date());
			existing.setR48_contractual_maturity_date(updatedEntity.getR48_contractual_maturity_date());
			existing.setR48_effective_maturity_date_if_date(updatedEntity.getR48_effective_maturity_date_if_date());

			existing.setR49_issuance_date(updatedEntity.getR49_issuance_date());
			existing.setR49_contractual_maturity_date(updatedEntity.getR49_contractual_maturity_date());
			existing.setR49_effective_maturity_date_if_date(updatedEntity.getR49_effective_maturity_date_if_date());

			existing.setR50_issuance_date(updatedEntity.getR50_issuance_date());
			existing.setR50_contractual_maturity_date(updatedEntity.getR50_contractual_maturity_date());
			existing.setR50_effective_maturity_date_if_date(updatedEntity.getR50_effective_maturity_date_if_date());

			existing.setR51_issuance_date(updatedEntity.getR51_issuance_date());
			existing.setR51_contractual_maturity_date(updatedEntity.getR51_contractual_maturity_date());
			existing.setR51_effective_maturity_date_if_date(updatedEntity.getR51_effective_maturity_date_if_date());

			existing.setR52_issuance_date(updatedEntity.getR52_issuance_date());
			existing.setR52_contractual_maturity_date(updatedEntity.getR52_contractual_maturity_date());
			existing.setR52_effective_maturity_date_if_date(updatedEntity.getR52_effective_maturity_date_if_date());

			existing.setR53_issuance_date(updatedEntity.getR53_issuance_date());
			existing.setR53_contractual_maturity_date(updatedEntity.getR53_contractual_maturity_date());
			existing.setR53_effective_maturity_date_if_date(updatedEntity.getR53_effective_maturity_date_if_date());

			existing.setR54_issuance_date(updatedEntity.getR54_issuance_date());
			existing.setR54_contractual_maturity_date(updatedEntity.getR54_contractual_maturity_date());
			existing.setR54_effective_maturity_date_if_date(updatedEntity.getR54_effective_maturity_date_if_date());

			existing.setR55_issuance_date(updatedEntity.getR55_issuance_date());
			existing.setR55_contractual_maturity_date(updatedEntity.getR55_contractual_maturity_date());
			existing.setR55_effective_maturity_date_if_date(updatedEntity.getR55_effective_maturity_date_if_date());

			existing.setR56_issuance_date(updatedEntity.getR56_issuance_date());
			existing.setR56_contractual_maturity_date(updatedEntity.getR56_contractual_maturity_date());
			existing.setR56_effective_maturity_date_if_date(updatedEntity.getR56_effective_maturity_date_if_date());

			existing.setR57_issuance_date(updatedEntity.getR57_issuance_date());
			existing.setR57_contractual_maturity_date(updatedEntity.getR57_contractual_maturity_date());
			existing.setR57_effective_maturity_date_if_date(updatedEntity.getR57_effective_maturity_date_if_date());

			existing.setR58_issuance_date(updatedEntity.getR58_issuance_date());
			existing.setR58_contractual_maturity_date(updatedEntity.getR58_contractual_maturity_date());
			existing.setR58_effective_maturity_date_if_date(updatedEntity.getR58_effective_maturity_date_if_date());

			existing.setR59_issuance_date(updatedEntity.getR59_issuance_date());
			existing.setR59_contractual_maturity_date(updatedEntity.getR59_contractual_maturity_date());
			existing.setR59_effective_maturity_date_if_date(updatedEntity.getR59_effective_maturity_date_if_date());

			existing.setR60_issuance_date(updatedEntity.getR60_issuance_date());
			existing.setR60_contractual_maturity_date(updatedEntity.getR60_contractual_maturity_date());
			existing.setR60_effective_maturity_date_if_date(updatedEntity.getR60_effective_maturity_date_if_date());

			existing.setR61_issuance_date(updatedEntity.getR61_issuance_date());
			existing.setR61_contractual_maturity_date(updatedEntity.getR61_contractual_maturity_date());
			existing.setR61_effective_maturity_date_if_date(updatedEntity.getR61_effective_maturity_date_if_date());

			existing.setR62_issuance_date(updatedEntity.getR62_issuance_date());
			existing.setR62_contractual_maturity_date(updatedEntity.getR62_contractual_maturity_date());
			existing.setR62_effective_maturity_date_if_date(updatedEntity.getR62_effective_maturity_date_if_date());

			existing.setR63_issuance_date(updatedEntity.getR63_issuance_date());
			existing.setR63_contractual_maturity_date(updatedEntity.getR63_contractual_maturity_date());
			existing.setR63_effective_maturity_date_if_date(updatedEntity.getR63_effective_maturity_date_if_date());

			existing.setR64_issuance_date(updatedEntity.getR64_issuance_date());
			existing.setR64_contractual_maturity_date(updatedEntity.getR64_contractual_maturity_date());
			existing.setR64_effective_maturity_date_if_date(updatedEntity.getR64_effective_maturity_date_if_date());

			existing.setR65_issuance_date(updatedEntity.getR65_issuance_date());
			existing.setR65_contractual_maturity_date(updatedEntity.getR65_contractual_maturity_date());
			existing.setR65_effective_maturity_date_if_date(updatedEntity.getR65_effective_maturity_date_if_date());

			existing.setR66_issuance_date(updatedEntity.getR66_issuance_date());
			existing.setR66_contractual_maturity_date(updatedEntity.getR66_contractual_maturity_date());
			existing.setR66_effective_maturity_date_if_date(updatedEntity.getR66_effective_maturity_date_if_date());

			existing.setR67_issuance_date(updatedEntity.getR67_issuance_date());
			existing.setR67_contractual_maturity_date(updatedEntity.getR67_contractual_maturity_date());
			existing.setR67_effective_maturity_date_if_date(updatedEntity.getR67_effective_maturity_date_if_date());

			existing.setR68_issuance_date(updatedEntity.getR68_issuance_date());
			existing.setR68_contractual_maturity_date(updatedEntity.getR68_contractual_maturity_date());
			existing.setR68_effective_maturity_date_if_date(updatedEntity.getR68_effective_maturity_date_if_date());

			existing.setR69_issuance_date(updatedEntity.getR69_issuance_date());
			existing.setR69_contractual_maturity_date(updatedEntity.getR69_contractual_maturity_date());
			existing.setR69_effective_maturity_date_if_date(updatedEntity.getR69_effective_maturity_date_if_date());

			existing.setR70_issuance_date(updatedEntity.getR70_issuance_date());
			existing.setR70_contractual_maturity_date(updatedEntity.getR70_contractual_maturity_date());
			existing.setR70_effective_maturity_date_if_date(updatedEntity.getR70_effective_maturity_date_if_date());

			existing.setR71_issuance_date(updatedEntity.getR71_issuance_date());
			existing.setR71_contractual_maturity_date(updatedEntity.getR71_contractual_maturity_date());
			existing.setR71_effective_maturity_date_if_date(updatedEntity.getR71_effective_maturity_date_if_date());

			existing.setR72_issuance_date(updatedEntity.getR72_issuance_date());
			existing.setR72_contractual_maturity_date(updatedEntity.getR72_contractual_maturity_date());
			existing.setR72_effective_maturity_date_if_date(updatedEntity.getR72_effective_maturity_date_if_date());

			existing.setR73_issuance_date(updatedEntity.getR73_issuance_date());
			existing.setR73_contractual_maturity_date(updatedEntity.getR73_contractual_maturity_date());
			existing.setR73_effective_maturity_date_if_date(updatedEntity.getR73_effective_maturity_date_if_date());

			existing.setR74_issuance_date(updatedEntity.getR74_issuance_date());
			existing.setR74_contractual_maturity_date(updatedEntity.getR74_contractual_maturity_date());
			existing.setR74_effective_maturity_date_if_date(updatedEntity.getR74_effective_maturity_date_if_date());

			existing.setR75_issuance_date(updatedEntity.getR75_issuance_date());
			existing.setR75_contractual_maturity_date(updatedEntity.getR75_contractual_maturity_date());
			existing.setR75_effective_maturity_date_if_date(updatedEntity.getR75_effective_maturity_date_if_date());

			existing.setR76_issuance_date(updatedEntity.getR76_issuance_date());
			existing.setR76_contractual_maturity_date(updatedEntity.getR76_contractual_maturity_date());
			existing.setR76_effective_maturity_date_if_date(updatedEntity.getR76_effective_maturity_date_if_date());

			existing.setR77_issuance_date(updatedEntity.getR77_issuance_date());
			existing.setR77_contractual_maturity_date(updatedEntity.getR77_contractual_maturity_date());
			existing.setR77_effective_maturity_date_if_date(updatedEntity.getR77_effective_maturity_date_if_date());

			existing.setR78_issuance_date(updatedEntity.getR78_issuance_date());
			existing.setR78_contractual_maturity_date(updatedEntity.getR78_contractual_maturity_date());
			existing.setR78_effective_maturity_date_if_date(updatedEntity.getR78_effective_maturity_date_if_date());

			existing.setR79_issuance_date(updatedEntity.getR79_issuance_date());
			existing.setR79_contractual_maturity_date(updatedEntity.getR79_contractual_maturity_date());
			existing.setR79_effective_maturity_date_if_date(updatedEntity.getR79_effective_maturity_date_if_date());

			existing.setR80_issuance_date(updatedEntity.getR80_issuance_date());
			existing.setR80_contractual_maturity_date(updatedEntity.getR80_contractual_maturity_date());
			existing.setR80_effective_maturity_date_if_date(updatedEntity.getR80_effective_maturity_date_if_date());

			existing.setR81_issuance_date(updatedEntity.getR81_issuance_date());
			existing.setR81_contractual_maturity_date(updatedEntity.getR81_contractual_maturity_date());
			existing.setR81_effective_maturity_date_if_date(updatedEntity.getR81_effective_maturity_date_if_date());

			existing.setR82_issuance_date(updatedEntity.getR82_issuance_date());
			existing.setR82_contractual_maturity_date(updatedEntity.getR82_contractual_maturity_date());
			existing.setR82_effective_maturity_date_if_date(updatedEntity.getR82_effective_maturity_date_if_date());

			existing.setR83_issuance_date(updatedEntity.getR83_issuance_date());
			existing.setR83_contractual_maturity_date(updatedEntity.getR83_contractual_maturity_date());
			existing.setR83_effective_maturity_date_if_date(updatedEntity.getR83_effective_maturity_date_if_date());

			existing.setR84_issuance_date(updatedEntity.getR84_issuance_date());
			existing.setR84_contractual_maturity_date(updatedEntity.getR84_contractual_maturity_date());
			existing.setR84_effective_maturity_date_if_date(updatedEntity.getR84_effective_maturity_date_if_date());

			existing.setR85_issuance_date(updatedEntity.getR85_issuance_date());
			existing.setR85_contractual_maturity_date(updatedEntity.getR85_contractual_maturity_date());
			existing.setR85_effective_maturity_date_if_date(updatedEntity.getR85_effective_maturity_date_if_date());

			existing.setR86_issuance_date(updatedEntity.getR86_issuance_date());
			existing.setR86_contractual_maturity_date(updatedEntity.getR86_contractual_maturity_date());
			existing.setR86_effective_maturity_date_if_date(updatedEntity.getR86_effective_maturity_date_if_date());

			existing.setR87_issuance_date(updatedEntity.getR87_issuance_date());
			existing.setR87_contractual_maturity_date(updatedEntity.getR87_contractual_maturity_date());
			existing.setR87_effective_maturity_date_if_date(updatedEntity.getR87_effective_maturity_date_if_date());

			existing.setR88_issuance_date(updatedEntity.getR88_issuance_date());
			existing.setR88_contractual_maturity_date(updatedEntity.getR88_contractual_maturity_date());
			existing.setR88_effective_maturity_date_if_date(updatedEntity.getR88_effective_maturity_date_if_date());

			existing.setR89_issuance_date(updatedEntity.getR89_issuance_date());
			existing.setR89_contractual_maturity_date(updatedEntity.getR89_contractual_maturity_date());
			existing.setR89_effective_maturity_date_if_date(updatedEntity.getR89_effective_maturity_date_if_date());

			existing.setR90_issuance_date(updatedEntity.getR90_issuance_date());
			existing.setR90_contractual_maturity_date(updatedEntity.getR90_contractual_maturity_date());
			existing.setR90_effective_maturity_date_if_date(updatedEntity.getR90_effective_maturity_date_if_date());

			existing.setR91_issuance_date(updatedEntity.getR91_issuance_date());
			existing.setR91_contractual_maturity_date(updatedEntity.getR91_contractual_maturity_date());
			existing.setR91_effective_maturity_date_if_date(updatedEntity.getR91_effective_maturity_date_if_date());

			existing.setR92_issuance_date(updatedEntity.getR92_issuance_date());
			existing.setR92_contractual_maturity_date(updatedEntity.getR92_contractual_maturity_date());
			existing.setR92_effective_maturity_date_if_date(updatedEntity.getR92_effective_maturity_date_if_date());

			existing.setR93_issuance_date(updatedEntity.getR93_issuance_date());
			existing.setR93_contractual_maturity_date(updatedEntity.getR93_contractual_maturity_date());
			existing.setR93_effective_maturity_date_if_date(updatedEntity.getR93_effective_maturity_date_if_date());

			existing.setR94_issuance_date(updatedEntity.getR94_issuance_date());
			existing.setR94_contractual_maturity_date(updatedEntity.getR94_contractual_maturity_date());
			existing.setR94_effective_maturity_date_if_date(updatedEntity.getR94_effective_maturity_date_if_date());

			existing.setR95_issuance_date(updatedEntity.getR95_issuance_date());
			existing.setR95_contractual_maturity_date(updatedEntity.getR95_contractual_maturity_date());
			existing.setR95_effective_maturity_date_if_date(updatedEntity.getR95_effective_maturity_date_if_date());

			existing.setR96_issuance_date(updatedEntity.getR96_issuance_date());
			existing.setR96_contractual_maturity_date(updatedEntity.getR96_contractual_maturity_date());
			existing.setR96_effective_maturity_date_if_date(updatedEntity.getR96_effective_maturity_date_if_date());

			existing.setR97_issuance_date(updatedEntity.getR97_issuance_date());
			existing.setR97_contractual_maturity_date(updatedEntity.getR97_contractual_maturity_date());
			existing.setR97_effective_maturity_date_if_date(updatedEntity.getR97_effective_maturity_date_if_date());

			existing.setR98_issuance_date(updatedEntity.getR98_issuance_date());
			existing.setR98_contractual_maturity_date(updatedEntity.getR98_contractual_maturity_date());
			existing.setR98_effective_maturity_date_if_date(updatedEntity.getR98_effective_maturity_date_if_date());

			existing.setR99_issuance_date(updatedEntity.getR99_issuance_date());
			existing.setR99_contractual_maturity_date(updatedEntity.getR99_contractual_maturity_date());
			existing.setR99_effective_maturity_date_if_date(updatedEntity.getR99_effective_maturity_date_if_date());

			existing.setR100_issuance_date(updatedEntity.getR100_issuance_date());
			existing.setR100_contractual_maturity_date(updatedEntity.getR100_contractual_maturity_date());
			existing.setR100_effective_maturity_date_if_date(updatedEntity.getR100_effective_maturity_date_if_date());

		} catch (Exception e) {
			throw new RuntimeException("Error while updating date fields", e);
		}

		// 3️⃣ Save updated entity
		BRRS_M_CA5_Summary_Repo1.save(existing);
	}

	public void updateReport2(M_CA5_Summary_Entity2 updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReportDate());

		M_CA5_Summary_Entity2 existing = BRRS_M_CA5_Summary_Repo2.findById(updatedEntity.getReportDate()).orElseThrow(
				() -> new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReportDate()));

		try {
			// 1️⃣ Loop from R101 to R149 and copy fields
			for (int i = 101; i <= 149; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "line_no", "note_holders", "name_of_instrument_programe",
						"issuing_entity_if_issued_throughan_spv", "issuance_date", "contractual_maturity_date",
						"effective_maturity_date_if_date", "amount", "amount_derecognised_p000" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_CA5_Summary_Entity2.class.getMethod(getterName);
						Method setter = M_CA5_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

			// 2️⃣ Handle R150 total fields using the same pattern
			String prefix = "R150_";
			String[] totalFields = { "line_no", "note_holders", "name_of_instrument_programe",
					"issuing_entity_if_issued_throughan_spv", "issuance_date", "contractual_maturity_date",
					"effective_maturity_date_if_date", "amount", "amount_derecognised_p000", "total_p000" };

			for (String field : totalFields) {
				String getterName = "get" + prefix + field;
				String setterName = "set" + prefix + field;

				try {
					Method getter = M_CA5_Summary_Entity2.class.getMethod(getterName);
					Method setter = M_CA5_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());

					Object newValue = getter.invoke(updatedEntity);
					setter.invoke(existing, newValue);

				} catch (NoSuchMethodException e) {
					// Skip missing total fields
					continue;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		try {
			existing.setR101_issuance_date(updatedEntity.getR101_issuance_date());
			existing.setR101_contractual_maturity_date(updatedEntity.getR101_contractual_maturity_date());
			existing.setR101_effective_maturity_date_if_date(updatedEntity.getR101_effective_maturity_date_if_date());

			existing.setR102_issuance_date(updatedEntity.getR102_issuance_date());
			existing.setR102_contractual_maturity_date(updatedEntity.getR102_contractual_maturity_date());
			existing.setR102_effective_maturity_date_if_date(updatedEntity.getR102_effective_maturity_date_if_date());

			existing.setR103_issuance_date(updatedEntity.getR103_issuance_date());
			existing.setR103_contractual_maturity_date(updatedEntity.getR103_contractual_maturity_date());
			existing.setR103_effective_maturity_date_if_date(updatedEntity.getR103_effective_maturity_date_if_date());

			existing.setR104_issuance_date(updatedEntity.getR104_issuance_date());
			existing.setR104_contractual_maturity_date(updatedEntity.getR104_contractual_maturity_date());
			existing.setR104_effective_maturity_date_if_date(updatedEntity.getR104_effective_maturity_date_if_date());

			existing.setR105_issuance_date(updatedEntity.getR105_issuance_date());
			existing.setR105_contractual_maturity_date(updatedEntity.getR105_contractual_maturity_date());
			existing.setR105_effective_maturity_date_if_date(updatedEntity.getR105_effective_maturity_date_if_date());

			existing.setR106_issuance_date(updatedEntity.getR106_issuance_date());
			existing.setR106_contractual_maturity_date(updatedEntity.getR106_contractual_maturity_date());
			existing.setR106_effective_maturity_date_if_date(updatedEntity.getR106_effective_maturity_date_if_date());

			existing.setR107_issuance_date(updatedEntity.getR107_issuance_date());
			existing.setR107_contractual_maturity_date(updatedEntity.getR107_contractual_maturity_date());
			existing.setR107_effective_maturity_date_if_date(updatedEntity.getR107_effective_maturity_date_if_date());

			existing.setR108_issuance_date(updatedEntity.getR108_issuance_date());
			existing.setR108_contractual_maturity_date(updatedEntity.getR108_contractual_maturity_date());
			existing.setR108_effective_maturity_date_if_date(updatedEntity.getR108_effective_maturity_date_if_date());

			existing.setR109_issuance_date(updatedEntity.getR109_issuance_date());
			existing.setR109_contractual_maturity_date(updatedEntity.getR109_contractual_maturity_date());
			existing.setR109_effective_maturity_date_if_date(updatedEntity.getR109_effective_maturity_date_if_date());

			existing.setR110_issuance_date(updatedEntity.getR110_issuance_date());
			existing.setR110_contractual_maturity_date(updatedEntity.getR110_contractual_maturity_date());
			existing.setR110_effective_maturity_date_if_date(updatedEntity.getR110_effective_maturity_date_if_date());

			existing.setR111_issuance_date(updatedEntity.getR111_issuance_date());
			existing.setR111_contractual_maturity_date(updatedEntity.getR111_contractual_maturity_date());
			existing.setR111_effective_maturity_date_if_date(updatedEntity.getR111_effective_maturity_date_if_date());

			existing.setR112_issuance_date(updatedEntity.getR112_issuance_date());
			existing.setR112_contractual_maturity_date(updatedEntity.getR112_contractual_maturity_date());
			existing.setR112_effective_maturity_date_if_date(updatedEntity.getR112_effective_maturity_date_if_date());

			existing.setR113_issuance_date(updatedEntity.getR113_issuance_date());
			existing.setR113_contractual_maturity_date(updatedEntity.getR113_contractual_maturity_date());
			existing.setR113_effective_maturity_date_if_date(updatedEntity.getR113_effective_maturity_date_if_date());

			existing.setR114_issuance_date(updatedEntity.getR114_issuance_date());
			existing.setR114_contractual_maturity_date(updatedEntity.getR114_contractual_maturity_date());
			existing.setR114_effective_maturity_date_if_date(updatedEntity.getR114_effective_maturity_date_if_date());

			existing.setR115_issuance_date(updatedEntity.getR115_issuance_date());
			existing.setR115_contractual_maturity_date(updatedEntity.getR115_contractual_maturity_date());
			existing.setR115_effective_maturity_date_if_date(updatedEntity.getR115_effective_maturity_date_if_date());

			existing.setR116_issuance_date(updatedEntity.getR116_issuance_date());
			existing.setR116_contractual_maturity_date(updatedEntity.getR116_contractual_maturity_date());
			existing.setR116_effective_maturity_date_if_date(updatedEntity.getR116_effective_maturity_date_if_date());

			existing.setR117_issuance_date(updatedEntity.getR117_issuance_date());
			existing.setR117_contractual_maturity_date(updatedEntity.getR117_contractual_maturity_date());
			existing.setR117_effective_maturity_date_if_date(updatedEntity.getR117_effective_maturity_date_if_date());

			existing.setR118_issuance_date(updatedEntity.getR118_issuance_date());
			existing.setR118_contractual_maturity_date(updatedEntity.getR118_contractual_maturity_date());
			existing.setR118_effective_maturity_date_if_date(updatedEntity.getR118_effective_maturity_date_if_date());

			existing.setR119_issuance_date(updatedEntity.getR119_issuance_date());
			existing.setR119_contractual_maturity_date(updatedEntity.getR119_contractual_maturity_date());
			existing.setR119_effective_maturity_date_if_date(updatedEntity.getR119_effective_maturity_date_if_date());

			existing.setR120_issuance_date(updatedEntity.getR120_issuance_date());
			existing.setR120_contractual_maturity_date(updatedEntity.getR120_contractual_maturity_date());
			existing.setR120_effective_maturity_date_if_date(updatedEntity.getR120_effective_maturity_date_if_date());

			existing.setR121_issuance_date(updatedEntity.getR121_issuance_date());
			existing.setR121_contractual_maturity_date(updatedEntity.getR121_contractual_maturity_date());
			existing.setR121_effective_maturity_date_if_date(updatedEntity.getR121_effective_maturity_date_if_date());

			existing.setR122_issuance_date(updatedEntity.getR122_issuance_date());
			existing.setR122_contractual_maturity_date(updatedEntity.getR122_contractual_maturity_date());
			existing.setR122_effective_maturity_date_if_date(updatedEntity.getR122_effective_maturity_date_if_date());

			existing.setR123_issuance_date(updatedEntity.getR123_issuance_date());
			existing.setR123_contractual_maturity_date(updatedEntity.getR123_contractual_maturity_date());
			existing.setR123_effective_maturity_date_if_date(updatedEntity.getR123_effective_maturity_date_if_date());

			existing.setR124_issuance_date(updatedEntity.getR124_issuance_date());
			existing.setR124_contractual_maturity_date(updatedEntity.getR124_contractual_maturity_date());
			existing.setR124_effective_maturity_date_if_date(updatedEntity.getR124_effective_maturity_date_if_date());

			existing.setR125_issuance_date(updatedEntity.getR125_issuance_date());
			existing.setR125_contractual_maturity_date(updatedEntity.getR125_contractual_maturity_date());
			existing.setR125_effective_maturity_date_if_date(updatedEntity.getR125_effective_maturity_date_if_date());

			existing.setR126_issuance_date(updatedEntity.getR126_issuance_date());
			existing.setR126_contractual_maturity_date(updatedEntity.getR126_contractual_maturity_date());
			existing.setR126_effective_maturity_date_if_date(updatedEntity.getR126_effective_maturity_date_if_date());

			existing.setR127_issuance_date(updatedEntity.getR127_issuance_date());
			existing.setR127_contractual_maturity_date(updatedEntity.getR127_contractual_maturity_date());
			existing.setR127_effective_maturity_date_if_date(updatedEntity.getR127_effective_maturity_date_if_date());

			existing.setR128_issuance_date(updatedEntity.getR128_issuance_date());
			existing.setR128_contractual_maturity_date(updatedEntity.getR128_contractual_maturity_date());
			existing.setR128_effective_maturity_date_if_date(updatedEntity.getR128_effective_maturity_date_if_date());

			existing.setR129_issuance_date(updatedEntity.getR129_issuance_date());
			existing.setR129_contractual_maturity_date(updatedEntity.getR129_contractual_maturity_date());
			existing.setR129_effective_maturity_date_if_date(updatedEntity.getR129_effective_maturity_date_if_date());

			existing.setR130_issuance_date(updatedEntity.getR130_issuance_date());
			existing.setR130_contractual_maturity_date(updatedEntity.getR130_contractual_maturity_date());
			existing.setR130_effective_maturity_date_if_date(updatedEntity.getR130_effective_maturity_date_if_date());

			existing.setR131_issuance_date(updatedEntity.getR131_issuance_date());
			existing.setR131_contractual_maturity_date(updatedEntity.getR131_contractual_maturity_date());
			existing.setR131_effective_maturity_date_if_date(updatedEntity.getR131_effective_maturity_date_if_date());

			existing.setR132_issuance_date(updatedEntity.getR132_issuance_date());
			existing.setR132_contractual_maturity_date(updatedEntity.getR132_contractual_maturity_date());
			existing.setR132_effective_maturity_date_if_date(updatedEntity.getR132_effective_maturity_date_if_date());

			existing.setR133_issuance_date(updatedEntity.getR133_issuance_date());
			existing.setR133_contractual_maturity_date(updatedEntity.getR133_contractual_maturity_date());
			existing.setR133_effective_maturity_date_if_date(updatedEntity.getR133_effective_maturity_date_if_date());

			existing.setR134_issuance_date(updatedEntity.getR134_issuance_date());
			existing.setR134_contractual_maturity_date(updatedEntity.getR134_contractual_maturity_date());
			existing.setR134_effective_maturity_date_if_date(updatedEntity.getR134_effective_maturity_date_if_date());

			existing.setR135_issuance_date(updatedEntity.getR135_issuance_date());
			existing.setR135_contractual_maturity_date(updatedEntity.getR135_contractual_maturity_date());
			existing.setR135_effective_maturity_date_if_date(updatedEntity.getR135_effective_maturity_date_if_date());

			existing.setR136_issuance_date(updatedEntity.getR136_issuance_date());
			existing.setR136_contractual_maturity_date(updatedEntity.getR136_contractual_maturity_date());
			existing.setR136_effective_maturity_date_if_date(updatedEntity.getR136_effective_maturity_date_if_date());

			existing.setR137_issuance_date(updatedEntity.getR137_issuance_date());
			existing.setR137_contractual_maturity_date(updatedEntity.getR137_contractual_maturity_date());
			existing.setR137_effective_maturity_date_if_date(updatedEntity.getR137_effective_maturity_date_if_date());

			existing.setR138_issuance_date(updatedEntity.getR138_issuance_date());
			existing.setR138_contractual_maturity_date(updatedEntity.getR138_contractual_maturity_date());
			existing.setR138_effective_maturity_date_if_date(updatedEntity.getR138_effective_maturity_date_if_date());

			existing.setR139_issuance_date(updatedEntity.getR139_issuance_date());
			existing.setR139_contractual_maturity_date(updatedEntity.getR139_contractual_maturity_date());
			existing.setR139_effective_maturity_date_if_date(updatedEntity.getR139_effective_maturity_date_if_date());

			existing.setR140_issuance_date(updatedEntity.getR140_issuance_date());
			existing.setR140_contractual_maturity_date(updatedEntity.getR140_contractual_maturity_date());
			existing.setR140_effective_maturity_date_if_date(updatedEntity.getR140_effective_maturity_date_if_date());

			existing.setR141_issuance_date(updatedEntity.getR141_issuance_date());
			existing.setR141_contractual_maturity_date(updatedEntity.getR141_contractual_maturity_date());
			existing.setR141_effective_maturity_date_if_date(updatedEntity.getR141_effective_maturity_date_if_date());

			existing.setR142_issuance_date(updatedEntity.getR142_issuance_date());
			existing.setR142_contractual_maturity_date(updatedEntity.getR142_contractual_maturity_date());
			existing.setR142_effective_maturity_date_if_date(updatedEntity.getR142_effective_maturity_date_if_date());

			existing.setR143_issuance_date(updatedEntity.getR143_issuance_date());
			existing.setR143_contractual_maturity_date(updatedEntity.getR143_contractual_maturity_date());
			existing.setR143_effective_maturity_date_if_date(updatedEntity.getR143_effective_maturity_date_if_date());

			existing.setR144_issuance_date(updatedEntity.getR144_issuance_date());
			existing.setR144_contractual_maturity_date(updatedEntity.getR144_contractual_maturity_date());
			existing.setR144_effective_maturity_date_if_date(updatedEntity.getR144_effective_maturity_date_if_date());

			existing.setR145_issuance_date(updatedEntity.getR145_issuance_date());
			existing.setR145_contractual_maturity_date(updatedEntity.getR145_contractual_maturity_date());
			existing.setR145_effective_maturity_date_if_date(updatedEntity.getR145_effective_maturity_date_if_date());

			existing.setR146_issuance_date(updatedEntity.getR146_issuance_date());
			existing.setR146_contractual_maturity_date(updatedEntity.getR146_contractual_maturity_date());
			existing.setR146_effective_maturity_date_if_date(updatedEntity.getR146_effective_maturity_date_if_date());

			existing.setR147_issuance_date(updatedEntity.getR147_issuance_date());
			existing.setR147_contractual_maturity_date(updatedEntity.getR147_contractual_maturity_date());
			existing.setR147_effective_maturity_date_if_date(updatedEntity.getR147_effective_maturity_date_if_date());

			existing.setR148_issuance_date(updatedEntity.getR148_issuance_date());
			existing.setR148_contractual_maturity_date(updatedEntity.getR148_contractual_maturity_date());
			existing.setR148_effective_maturity_date_if_date(updatedEntity.getR148_effective_maturity_date_if_date());

			existing.setR149_issuance_date(updatedEntity.getR149_issuance_date());
			existing.setR149_contractual_maturity_date(updatedEntity.getR149_contractual_maturity_date());
			existing.setR149_effective_maturity_date_if_date(updatedEntity.getR149_effective_maturity_date_if_date());

		} catch (Exception e) {
			throw new RuntimeException("Error while updating date fields", e);
		}

		// 3️⃣ Save updated entity
		BRRS_M_CA5_Summary_Repo2.save(existing);

	}

	public byte[] getM_CA5Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equals(type) && version != null) {
			byte[] ARCHIVALreport = getExcelM_CA5ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
			return ARCHIVALreport;
		}

		List<M_CA5_Summary_Entity1> dataList1 = BRRS_M_CA5_Summary_Repo1.getdatabydateList(dateformat.parse(todate));
		List<M_CA5_Summary_Entity2> dataList2 = BRRS_M_CA5_Summary_Repo2.getdatabydateList(dateformat.parse(todate));

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA5 report. Returning empty result.");
			return new byte[0];
		}
		if (dataList2.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_CA5 report. Returning empty result.");
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
//			 --- End of Style Definitions ---

			if (!dataList1.isEmpty()) {
				populateEntity1Data(sheet, dataList1.get(0), textStyle, numberStyle, dateStyle); // only 2 styles
			}

			if (!dataList2.isEmpty()) {
				populateEntity2Data(sheet, dataList2.get(0), textStyle, numberStyle, dateStyle); // only 2 styles
			}

			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}
	// cell code should be for only select query written cells not for text and also
	// not for formula cells
	// here first Cell cell1 is declaring variable
	// createCell(2)<--this 2 is the index value of the excel cell
	// i.e, A column is cell(0) B ->1, C->2....
	// record is declared above to fetch from which entity

	private void populateEntity1Data(Sheet sheet, M_CA5_Summary_Entity1 record, CellStyle textStyle,
			CellStyle numberStyle, CellStyle dateStyle) {
		Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
		CellStyle originalStyle;

		// Column C
		Row row = sheet.getRow(13);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR14_note_holders() != null) {
			cell1.setCellValue(record.getR14_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR14_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR14_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR14_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR14_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR14_issuance_date() != null) {
			cell6.setCellValue(record.getR14_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G

		cell7 = row.createCell(7);
		if (record.getR14_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR14_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR14_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR14_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR14_amount() != null) {
			cell9.setCellValue(record.getR14_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		row = sheet.getRow(14);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR15_note_holders() != null) {
			cell1.setCellValue(record.getR15_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR15_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR15_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR15_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR15_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR15_issuance_date() != null) {
			cell6.setCellValue(record.getR15_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR15_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR15_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR15_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR15_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR15_amount() != null) {
			cell9.setCellValue(record.getR15_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R16 =================
		row = sheet.getRow(15);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR16_note_holders() != null) {
			cell1.setCellValue(record.getR16_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR16_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR16_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR16_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR16_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR16_issuance_date() != null) {
			cell6.setCellValue(record.getR16_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR16_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR16_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR16_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR16_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR16_amount() != null) {
			cell9.setCellValue(record.getR16_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R17 =================
		row = sheet.getRow(16);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR17_note_holders() != null) {
			cell1.setCellValue(record.getR17_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR17_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR17_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR17_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR17_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR17_issuance_date() != null) {
			cell6.setCellValue(record.getR17_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR17_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR17_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR17_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR17_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR17_amount() != null) {
			cell9.setCellValue(record.getR17_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R18 =================
		row = sheet.getRow(17);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR18_note_holders() != null) {
			cell1.setCellValue(record.getR18_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR18_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR18_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR18_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR18_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR18_issuance_date() != null) {
			cell6.setCellValue(record.getR18_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR18_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR18_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR18_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR18_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR18_amount() != null) {
			cell9.setCellValue(record.getR18_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R19 =================
		row = sheet.getRow(18);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR19_note_holders() != null) {
			cell1.setCellValue(record.getR19_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR19_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR19_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR19_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR19_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR19_issuance_date() != null) {
			cell6.setCellValue(record.getR19_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR19_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR19_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR19_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR19_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR19_amount() != null) {
			cell9.setCellValue(record.getR19_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R20 =================
		row = sheet.getRow(19);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR20_note_holders() != null) {
			cell1.setCellValue(record.getR20_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR20_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR20_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR20_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR20_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR20_issuance_date() != null) {
			cell6.setCellValue(record.getR20_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR20_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR20_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR20_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR20_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR20_amount() != null) {
			cell9.setCellValue(record.getR20_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R21 =================
		row = sheet.getRow(20);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR21_note_holders() != null) {
			cell1.setCellValue(record.getR21_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR21_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR21_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR21_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR21_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR21_issuance_date() != null) {
			cell6.setCellValue(record.getR21_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR21_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR21_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR21_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR21_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR21_amount() != null) {
			cell9.setCellValue(record.getR21_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R22 =================
		row = sheet.getRow(21);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR22_note_holders() != null) {
			cell1.setCellValue(record.getR22_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR22_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR22_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR22_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR22_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR22_issuance_date() != null) {
			cell6.setCellValue(record.getR22_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR22_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR22_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR22_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR22_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR22_amount() != null) {
			cell9.setCellValue(record.getR22_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R23 =================
		row = sheet.getRow(22);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR23_note_holders() != null) {
			cell1.setCellValue(record.getR23_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR23_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR23_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR23_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR23_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR23_issuance_date() != null) {
			cell6.setCellValue(record.getR23_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR23_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR23_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR23_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR23_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR23_amount() != null) {
			cell9.setCellValue(record.getR23_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R24 =================
		row = sheet.getRow(23);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR24_note_holders() != null) {
			cell1.setCellValue(record.getR24_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR24_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR24_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR24_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR24_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR24_issuance_date() != null) {
			cell6.setCellValue(record.getR24_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR24_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR24_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR24_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR24_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR24_amount() != null) {
			cell9.setCellValue(record.getR24_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R25 =================
		row = sheet.getRow(24);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR25_note_holders() != null) {
			cell1.setCellValue(record.getR25_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR25_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR25_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR25_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR25_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR25_issuance_date() != null) {
			cell6.setCellValue(record.getR25_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR25_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR25_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR25_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR25_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR25_amount() != null) {
			cell9.setCellValue(record.getR25_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R26 =================
		row = sheet.getRow(25);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR26_note_holders() != null) {
			cell1.setCellValue(record.getR26_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR26_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR26_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR26_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR26_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR26_issuance_date() != null) {
			cell6.setCellValue(record.getR26_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR26_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR26_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR26_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR26_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR26_amount() != null) {
			cell9.setCellValue(record.getR26_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R27 =================
		row = sheet.getRow(26);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR27_note_holders() != null) {
			cell1.setCellValue(record.getR27_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR27_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR27_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR27_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR27_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR27_issuance_date() != null) {
			cell6.setCellValue(record.getR27_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR27_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR27_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR27_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR27_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR27_amount() != null) {
			cell9.setCellValue(record.getR27_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R28 =================
		row = sheet.getRow(27);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR28_note_holders() != null) {
			cell1.setCellValue(record.getR28_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR28_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR28_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR28_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR28_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR28_issuance_date() != null) {
			cell6.setCellValue(record.getR28_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR28_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR28_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR28_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR28_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// ===== Row R35 =====
		row = sheet.getRow(34);

		cell1 = row.createCell(1);
		if (record.getR35_note_holders() != null) {
			cell1.setCellValue(record.getR35_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR35_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR35_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR35_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR35_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR35_issuance_date() != null) {
			cell6.setCellValue(record.getR35_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR35_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR35_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR35_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR35_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR35_amount() != null) {
			cell9.setCellValue(record.getR35_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR35_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR35_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R36 =====
		row = sheet.getRow(35);

		cell1 = row.createCell(1);
		if (record.getR36_note_holders() != null) {
			cell1.setCellValue(record.getR36_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR36_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR36_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR36_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR36_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR36_issuance_date() != null) {
			cell6.setCellValue(record.getR36_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR36_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR36_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR36_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR36_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR36_amount() != null) {
			cell9.setCellValue(record.getR36_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR36_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR36_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R37 =====
		row = sheet.getRow(36);

		cell1 = row.createCell(1);
		if (record.getR37_note_holders() != null) {
			cell1.setCellValue(record.getR37_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR37_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR37_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR37_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR37_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR37_issuance_date() != null) {
			cell6.setCellValue(record.getR37_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR37_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR37_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR37_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR37_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR37_amount() != null) {
			cell9.setCellValue(record.getR37_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR37_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR37_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R38 =====
		row = sheet.getRow(37);

		cell1 = row.createCell(1);
		if (record.getR38_note_holders() != null) {
			cell1.setCellValue(record.getR38_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR38_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR38_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR38_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR38_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR38_issuance_date() != null) {
			cell6.setCellValue(record.getR38_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR38_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR38_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR38_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR38_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR38_amount() != null) {
			cell9.setCellValue(record.getR38_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR38_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR38_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R39 =====
		row = sheet.getRow(38);

		cell1 = row.createCell(1);
		if (record.getR39_note_holders() != null) {
			cell1.setCellValue(record.getR39_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR39_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR39_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR39_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR39_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR39_issuance_date() != null) {
			cell6.setCellValue(record.getR39_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR39_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR39_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR39_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR39_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR39_amount() != null) {
			cell9.setCellValue(record.getR39_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR39_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR39_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R40 =====
		row = sheet.getRow(39);

		cell1 = row.createCell(1);
		if (record.getR40_note_holders() != null) {
			cell1.setCellValue(record.getR40_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR40_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR40_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR40_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR40_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR40_issuance_date() != null) {
			cell6.setCellValue(record.getR40_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR40_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR40_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR40_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR40_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR40_amount() != null) {
			cell9.setCellValue(record.getR40_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR40_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR40_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R41 =====
		row = sheet.getRow(40);

		cell1 = row.createCell(1);
		if (record.getR41_note_holders() != null) {
			cell1.setCellValue(record.getR41_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR41_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR41_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR41_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR41_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR41_issuance_date() != null) {
			cell6.setCellValue(record.getR41_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR41_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR41_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR41_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR41_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR41_amount() != null) {
			cell9.setCellValue(record.getR41_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR41_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR41_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R42 =====
		row = sheet.getRow(41);

		cell1 = row.createCell(1);
		if (record.getR42_note_holders() != null) {
			cell1.setCellValue(record.getR42_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR42_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR42_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR42_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR42_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR42_issuance_date() != null) {
			cell6.setCellValue(record.getR42_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR42_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR42_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR42_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR42_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR42_amount() != null) {
			cell9.setCellValue(record.getR42_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR42_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR42_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R43 =====
		row = sheet.getRow(42);

		cell1 = row.createCell(1);
		if (record.getR43_note_holders() != null) {
			cell1.setCellValue(record.getR43_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR43_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR43_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR43_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR43_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR43_issuance_date() != null) {
			cell6.setCellValue(record.getR43_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR43_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR43_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR43_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR43_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR43_amount() != null) {
			cell9.setCellValue(record.getR43_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR43_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR43_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R44 =====
		row = sheet.getRow(43);

		cell1 = row.createCell(1);
		if (record.getR44_note_holders() != null) {
			cell1.setCellValue(record.getR44_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR44_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR44_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR44_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR44_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR44_issuance_date() != null) {
			cell6.setCellValue(record.getR44_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR44_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR44_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR44_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR44_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR44_amount() != null) {
			cell9.setCellValue(record.getR44_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR44_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR44_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R45 =====
		row = sheet.getRow(44);

		cell1 = row.createCell(1);
		if (record.getR45_note_holders() != null) {
			cell1.setCellValue(record.getR45_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR45_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR45_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR45_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR45_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR45_issuance_date() != null) {
			cell6.setCellValue(record.getR45_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR45_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR45_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR45_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR45_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR45_amount() != null) {
			cell9.setCellValue(record.getR45_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR45_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR45_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R46 =====
		row = sheet.getRow(45);

		cell1 = row.createCell(1);
		if (record.getR46_note_holders() != null) {
			cell1.setCellValue(record.getR46_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR46_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR46_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR46_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR46_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR46_issuance_date() != null) {
			cell6.setCellValue(record.getR46_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR46_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR46_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR46_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR46_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR46_amount() != null) {
			cell9.setCellValue(record.getR46_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR46_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR46_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R47 =====
		row = sheet.getRow(46);

		cell1 = row.createCell(1);
		if (record.getR47_note_holders() != null) {
			cell1.setCellValue(record.getR47_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR47_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR47_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR47_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR47_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR47_issuance_date() != null) {
			cell6.setCellValue(record.getR47_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR47_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR47_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR47_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR47_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR47_amount() != null) {
			cell9.setCellValue(record.getR47_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR47_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR47_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R48 =====
		row = sheet.getRow(47);

		cell1 = row.createCell(1);
		if (record.getR48_note_holders() != null) {
			cell1.setCellValue(record.getR48_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR48_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR48_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR48_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR48_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR48_issuance_date() != null) {
			cell6.setCellValue(record.getR48_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR48_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR48_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR48_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR48_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR48_amount() != null) {
			cell9.setCellValue(record.getR48_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR48_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR48_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R49 =====
		row = sheet.getRow(48);

		cell1 = row.createCell(1);
		if (record.getR49_note_holders() != null) {
			cell1.setCellValue(record.getR49_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR49_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR49_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR49_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR49_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR49_issuance_date() != null) {
			cell6.setCellValue(record.getR49_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR49_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR49_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR49_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR49_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR49_amount() != null) {
			cell9.setCellValue(record.getR49_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR49_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR49_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R50 =====
		row = sheet.getRow(49);

		cell1 = row.createCell(1);
		if (record.getR50_note_holders() != null) {
			cell1.setCellValue(record.getR50_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR50_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR50_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR50_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR50_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR50_issuance_date() != null) {
			cell6.setCellValue(record.getR50_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR50_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR50_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR50_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR50_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR50_amount() != null) {
			cell9.setCellValue(record.getR50_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR50_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR50_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R51 =====
		row = sheet.getRow(50);

		cell1 = row.createCell(1);
		if (record.getR51_note_holders() != null) {
			cell1.setCellValue(record.getR51_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR51_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR51_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR51_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR51_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR51_issuance_date() != null) {
			cell6.setCellValue(record.getR51_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR51_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR51_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR51_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR51_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR51_amount() != null) {
			cell9.setCellValue(record.getR51_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR51_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR51_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R52 =====
		row = sheet.getRow(51);

		cell1 = row.createCell(1);
		if (record.getR52_note_holders() != null) {
			cell1.setCellValue(record.getR52_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR52_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR52_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR52_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR52_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR52_issuance_date() != null) {
			cell6.setCellValue(record.getR52_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR52_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR52_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR52_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR52_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR52_amount() != null) {
			cell9.setCellValue(record.getR52_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR52_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR52_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R53 =====
		row = sheet.getRow(52);

		cell1 = row.createCell(1);
		if (record.getR53_note_holders() != null) {
			cell1.setCellValue(record.getR53_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR53_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR53_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR53_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR53_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR53_issuance_date() != null) {
			cell6.setCellValue(record.getR53_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR53_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR53_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR53_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR53_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR53_amount() != null) {
			cell9.setCellValue(record.getR53_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR53_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR53_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R54 =====
		row = sheet.getRow(53);

		cell1 = row.createCell(1);
		if (record.getR54_note_holders() != null) {
			cell1.setCellValue(record.getR54_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR54_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR54_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR54_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR54_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR54_issuance_date() != null) {
			cell6.setCellValue(record.getR54_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR54_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR54_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR54_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR54_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR54_amount() != null) {
			cell9.setCellValue(record.getR54_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR54_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR54_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R55 =====
		row = sheet.getRow(54);

		cell1 = row.createCell(1);
		if (record.getR55_note_holders() != null) {
			cell1.setCellValue(record.getR55_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR55_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR55_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR55_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR55_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR55_issuance_date() != null) {
			cell6.setCellValue(record.getR55_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR55_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR55_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR55_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR55_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR55_amount() != null) {
			cell9.setCellValue(record.getR55_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR55_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR55_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R56 =====
		row = sheet.getRow(55);

		cell1 = row.createCell(1);
		if (record.getR56_note_holders() != null) {
			cell1.setCellValue(record.getR56_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR56_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR56_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR56_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR56_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR56_issuance_date() != null) {
			cell6.setCellValue(record.getR56_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR56_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR56_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR56_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR56_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR56_amount() != null) {
			cell9.setCellValue(record.getR56_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR56_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR56_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R57 =====
		row = sheet.getRow(56);

		cell1 = row.createCell(1);
		if (record.getR57_note_holders() != null) {
			cell1.setCellValue(record.getR57_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR57_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR57_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR57_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR57_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR57_issuance_date() != null) {
			cell6.setCellValue(record.getR57_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR57_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR57_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR57_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR57_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR57_amount() != null) {
			cell9.setCellValue(record.getR57_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR57_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR57_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R58 =====
		row = sheet.getRow(57);

		cell1 = row.createCell(1);
		if (record.getR58_note_holders() != null) {
			cell1.setCellValue(record.getR58_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR58_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR58_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR58_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR58_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR58_issuance_date() != null) {
			cell6.setCellValue(record.getR58_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR58_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR58_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR58_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR58_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR58_amount() != null) {
			cell9.setCellValue(record.getR58_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR58_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR58_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R59 =====
		row = sheet.getRow(58);

		cell1 = row.createCell(1);
		if (record.getR59_note_holders() != null) {
			cell1.setCellValue(record.getR59_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR59_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR59_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR59_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR59_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR59_issuance_date() != null) {
			cell6.setCellValue(record.getR59_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR59_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR59_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR59_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR59_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR59_amount() != null) {
			cell9.setCellValue(record.getR59_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR59_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR59_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R60 =====
		row = sheet.getRow(59);

		cell1 = row.createCell(1);
		if (record.getR60_note_holders() != null) {
			cell1.setCellValue(record.getR60_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR60_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR60_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR60_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR60_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR60_issuance_date() != null) {
			cell6.setCellValue(record.getR60_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR60_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR60_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR60_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR60_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR60_amount() != null) {
			cell9.setCellValue(record.getR60_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR60_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR60_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R61 =====
		row = sheet.getRow(60);

		cell1 = row.createCell(1);
		if (record.getR61_note_holders() != null) {
			cell1.setCellValue(record.getR61_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR61_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR61_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR61_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR61_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR61_issuance_date() != null) {
			cell6.setCellValue(record.getR61_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR61_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR61_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR61_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR61_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR61_amount() != null) {
			cell9.setCellValue(record.getR61_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR61_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR61_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R62 =====
		row = sheet.getRow(61);

		cell1 = row.createCell(1);
		if (record.getR62_note_holders() != null) {
			cell1.setCellValue(record.getR62_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR62_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR62_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR62_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR62_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR62_issuance_date() != null) {
			cell6.setCellValue(record.getR62_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR62_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR62_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR62_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR62_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR62_amount() != null) {
			cell9.setCellValue(record.getR62_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR62_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR62_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R63 =====
		row = sheet.getRow(62);

		cell1 = row.createCell(1);
		if (record.getR63_note_holders() != null) {
			cell1.setCellValue(record.getR63_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR63_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR63_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR63_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR63_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR63_issuance_date() != null) {
			cell6.setCellValue(record.getR63_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR63_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR63_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR63_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR63_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR63_amount() != null) {
			cell9.setCellValue(record.getR63_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR63_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR63_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R64 =====
		row = sheet.getRow(63);

		cell1 = row.createCell(1);
		if (record.getR64_note_holders() != null) {
			cell1.setCellValue(record.getR64_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR64_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR64_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR64_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR64_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR64_issuance_date() != null) {
			cell6.setCellValue(record.getR64_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR64_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR64_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR64_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR64_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR64_amount() != null) {
			cell9.setCellValue(record.getR64_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR64_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR64_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R65 =====
		row = sheet.getRow(64);

		cell1 = row.createCell(1);
		if (record.getR65_note_holders() != null) {
			cell1.setCellValue(record.getR65_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR65_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR65_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR65_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR65_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR65_issuance_date() != null) {
			cell6.setCellValue(record.getR65_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR65_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR65_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR65_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR65_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR65_amount() != null) {
			cell9.setCellValue(record.getR65_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR65_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR65_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R66 =====
		row = sheet.getRow(65);

		cell1 = row.createCell(1);
		if (record.getR66_note_holders() != null) {
			cell1.setCellValue(record.getR66_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR66_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR66_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR66_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR66_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR66_issuance_date() != null) {
			cell6.setCellValue(record.getR66_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR66_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR66_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR66_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR66_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR66_amount() != null) {
			cell9.setCellValue(record.getR66_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR66_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR66_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R67 =====
		row = sheet.getRow(66);

		cell1 = row.createCell(1);
		if (record.getR67_note_holders() != null) {
			cell1.setCellValue(record.getR67_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR67_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR67_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR67_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR67_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR67_issuance_date() != null) {
			cell6.setCellValue(record.getR67_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR67_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR67_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR67_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR67_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR67_amount() != null) {
			cell9.setCellValue(record.getR67_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR67_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR67_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R68 =====
		row = sheet.getRow(67);

		cell1 = row.createCell(1);
		if (record.getR68_note_holders() != null) {
			cell1.setCellValue(record.getR68_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR68_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR68_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR68_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR68_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR68_issuance_date() != null) {
			cell6.setCellValue(record.getR68_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR68_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR68_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR68_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR68_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR68_amount() != null) {
			cell9.setCellValue(record.getR68_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR68_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR68_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R69 =====
		row = sheet.getRow(68);

		cell1 = row.createCell(1);
		if (record.getR69_note_holders() != null) {
			cell1.setCellValue(record.getR69_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR69_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR69_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR69_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR69_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR69_issuance_date() != null) {
			cell6.setCellValue(record.getR69_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR69_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR69_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR69_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR69_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR69_amount() != null) {
			cell9.setCellValue(record.getR69_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR69_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR69_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R70 =====
		row = sheet.getRow(69);

		cell1 = row.createCell(1);
		if (record.getR70_note_holders() != null) {
			cell1.setCellValue(record.getR70_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR70_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR70_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR70_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR70_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR70_issuance_date() != null) {
			cell6.setCellValue(record.getR70_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR70_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR70_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR70_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR70_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR70_amount() != null) {
			cell9.setCellValue(record.getR70_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR70_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR70_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R71 =====
		row = sheet.getRow(70);

		cell1 = row.createCell(1);
		if (record.getR71_note_holders() != null) {
			cell1.setCellValue(record.getR71_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR71_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR71_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR71_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR71_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR71_issuance_date() != null) {
			cell6.setCellValue(record.getR71_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR71_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR71_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR71_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR71_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR71_amount() != null) {
			cell9.setCellValue(record.getR71_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR71_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR71_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R72 =====
		row = sheet.getRow(71);

		cell1 = row.createCell(1);
		if (record.getR72_note_holders() != null) {
			cell1.setCellValue(record.getR72_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR72_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR72_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR72_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR72_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR72_issuance_date() != null) {
			cell6.setCellValue(record.getR72_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR72_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR72_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR72_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR72_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR72_amount() != null) {
			cell9.setCellValue(record.getR72_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR72_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR72_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R73 =====
		row = sheet.getRow(72);

		cell1 = row.createCell(1);
		if (record.getR73_note_holders() != null) {
			cell1.setCellValue(record.getR73_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR73_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR73_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR73_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR73_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR73_issuance_date() != null) {
			cell6.setCellValue(record.getR73_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR73_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR73_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR73_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR73_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR73_amount() != null) {
			cell9.setCellValue(record.getR73_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR73_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR73_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R74 =====
		row = sheet.getRow(73);

		cell1 = row.createCell(1);
		if (record.getR74_note_holders() != null) {
			cell1.setCellValue(record.getR74_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR74_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR74_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR74_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR74_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR74_issuance_date() != null) {
			cell6.setCellValue(record.getR74_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR74_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR74_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR74_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR74_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR74_amount() != null) {
			cell9.setCellValue(record.getR74_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR74_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR74_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R75 =====
		row = sheet.getRow(74);

		cell1 = row.createCell(1);
		if (record.getR75_note_holders() != null) {
			cell1.setCellValue(record.getR75_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR75_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR75_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR75_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR75_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR75_issuance_date() != null) {
			cell6.setCellValue(record.getR75_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR75_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR75_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR75_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR75_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR75_amount() != null) {
			cell9.setCellValue(record.getR75_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR75_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR75_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R76 =====
		row = sheet.getRow(75);

		cell1 = row.createCell(1);
		if (record.getR76_note_holders() != null) {
			cell1.setCellValue(record.getR76_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR76_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR76_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR76_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR76_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR76_issuance_date() != null) {
			cell6.setCellValue(record.getR76_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR76_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR76_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR76_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR76_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR76_amount() != null) {
			cell9.setCellValue(record.getR76_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR76_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR76_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R77 =====
		row = sheet.getRow(76);

		cell1 = row.createCell(1);
		if (record.getR77_note_holders() != null) {
			cell1.setCellValue(record.getR77_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR77_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR77_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR77_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR77_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR77_issuance_date() != null) {
			cell6.setCellValue(record.getR77_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR77_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR77_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR77_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR77_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR77_amount() != null) {
			cell9.setCellValue(record.getR77_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR77_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR77_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R78 =====
		row = sheet.getRow(77);

		cell1 = row.createCell(1);
		if (record.getR78_note_holders() != null) {
			cell1.setCellValue(record.getR78_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR78_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR78_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR78_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR78_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR78_issuance_date() != null) {
			cell6.setCellValue(record.getR78_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR78_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR78_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR78_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR78_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR78_amount() != null) {
			cell9.setCellValue(record.getR78_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR78_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR78_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R79 =====
		row = sheet.getRow(78);

		cell1 = row.createCell(1);
		if (record.getR79_note_holders() != null) {
			cell1.setCellValue(record.getR79_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR79_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR79_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR79_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR79_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR79_issuance_date() != null) {
			cell6.setCellValue(record.getR79_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR79_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR79_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR79_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR79_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR79_amount() != null) {
			cell9.setCellValue(record.getR79_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR79_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR79_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R80 =====
		row = sheet.getRow(79);

		cell1 = row.createCell(1);
		if (record.getR80_note_holders() != null) {
			cell1.setCellValue(record.getR80_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR80_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR80_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR80_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR80_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR80_issuance_date() != null) {
			cell6.setCellValue(record.getR80_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR80_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR80_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR80_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR80_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR80_amount() != null) {
			cell9.setCellValue(record.getR80_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR80_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR80_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R81 =====
		row = sheet.getRow(80);

		cell1 = row.createCell(1);
		if (record.getR81_note_holders() != null) {
			cell1.setCellValue(record.getR81_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR81_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR81_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR81_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR81_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR81_issuance_date() != null) {
			cell6.setCellValue(record.getR81_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR81_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR81_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR81_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR81_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR81_amount() != null) {
			cell9.setCellValue(record.getR81_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR81_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR81_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R82 =====
		row = sheet.getRow(81);

		cell1 = row.createCell(1);
		if (record.getR82_note_holders() != null) {
			cell1.setCellValue(record.getR82_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR82_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR82_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR82_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR82_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR82_issuance_date() != null) {
			cell6.setCellValue(record.getR82_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR82_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR82_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR82_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR82_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR82_amount() != null) {
			cell9.setCellValue(record.getR82_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR82_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR82_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R83 =====
		row = sheet.getRow(82);

		cell1 = row.createCell(1);
		if (record.getR83_note_holders() != null) {
			cell1.setCellValue(record.getR83_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR83_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR83_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR83_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR83_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR83_issuance_date() != null) {
			cell6.setCellValue(record.getR83_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR83_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR83_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR83_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR83_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR83_amount() != null) {
			cell9.setCellValue(record.getR83_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR83_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR83_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R84 =====
		row = sheet.getRow(83);

		cell1 = row.createCell(1);
		if (record.getR84_note_holders() != null) {
			cell1.setCellValue(record.getR84_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR84_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR84_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR84_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR84_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR84_issuance_date() != null) {
			cell6.setCellValue(record.getR84_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR84_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR84_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR84_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR84_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR84_amount() != null) {
			cell9.setCellValue(record.getR84_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR84_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR84_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R85 =====
		row = sheet.getRow(84);

		cell1 = row.createCell(1);
		if (record.getR85_note_holders() != null) {
			cell1.setCellValue(record.getR85_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR85_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR85_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR85_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR85_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR85_issuance_date() != null) {
			cell6.setCellValue(record.getR85_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR85_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR85_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR85_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR85_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR85_amount() != null) {
			cell9.setCellValue(record.getR85_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR85_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR85_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R86 =====
		row = sheet.getRow(85);

		cell1 = row.createCell(1);
		if (record.getR86_note_holders() != null) {
			cell1.setCellValue(record.getR86_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR86_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR86_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR86_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR86_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR86_issuance_date() != null) {
			cell6.setCellValue(record.getR86_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR86_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR86_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR86_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR86_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR86_amount() != null) {
			cell9.setCellValue(record.getR86_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR86_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR86_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R87 =====
		row = sheet.getRow(86);

		cell1 = row.createCell(1);
		if (record.getR87_note_holders() != null) {
			cell1.setCellValue(record.getR87_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR87_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR87_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR87_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR87_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR87_issuance_date() != null) {
			cell6.setCellValue(record.getR87_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR87_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR87_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR87_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR87_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR87_amount() != null) {
			cell9.setCellValue(record.getR87_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR87_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR87_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R88 =====
		row = sheet.getRow(87);

		cell1 = row.createCell(1);
		if (record.getR88_note_holders() != null) {
			cell1.setCellValue(record.getR88_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR88_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR88_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR88_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR88_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR88_issuance_date() != null) {
			cell6.setCellValue(record.getR88_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR88_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR88_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR88_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR88_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR88_amount() != null) {
			cell9.setCellValue(record.getR88_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR88_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR88_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R89 =====
		row = sheet.getRow(88);

		cell1 = row.createCell(1);
		if (record.getR89_note_holders() != null) {
			cell1.setCellValue(record.getR89_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR89_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR89_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR89_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR89_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR89_issuance_date() != null) {
			cell6.setCellValue(record.getR89_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR89_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR89_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR89_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR89_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR89_amount() != null) {
			cell9.setCellValue(record.getR89_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR89_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR89_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R90 =====
		row = sheet.getRow(89);

		cell1 = row.createCell(1);
		if (record.getR90_note_holders() != null) {
			cell1.setCellValue(record.getR90_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR90_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR90_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR90_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR90_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR90_issuance_date() != null) {
			cell6.setCellValue(record.getR90_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR90_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR90_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR90_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR90_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR90_amount() != null) {
			cell9.setCellValue(record.getR90_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR90_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR90_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R91 =====
		row = sheet.getRow(90);

		cell1 = row.createCell(1);
		if (record.getR91_note_holders() != null) {
			cell1.setCellValue(record.getR91_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR91_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR91_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR91_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR91_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR91_issuance_date() != null) {
			cell6.setCellValue(record.getR91_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR91_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR91_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR91_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR91_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR91_amount() != null) {
			cell9.setCellValue(record.getR91_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR91_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR91_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R92 =====
		row = sheet.getRow(91);

		cell1 = row.createCell(1);
		if (record.getR92_note_holders() != null) {
			cell1.setCellValue(record.getR92_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR92_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR92_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR92_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR92_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR92_issuance_date() != null) {
			cell6.setCellValue(record.getR92_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR92_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR92_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR92_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR92_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR92_amount() != null) {
			cell9.setCellValue(record.getR92_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR92_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR92_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R93 =====
		row = sheet.getRow(92);

		cell1 = row.createCell(1);
		if (record.getR93_note_holders() != null) {
			cell1.setCellValue(record.getR93_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR93_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR93_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR93_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR93_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR93_issuance_date() != null) {
			cell6.setCellValue(record.getR93_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR93_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR93_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR93_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR93_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR93_amount() != null) {
			cell9.setCellValue(record.getR93_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR93_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR93_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R94 =====
		row = sheet.getRow(93);

		cell1 = row.createCell(1);
		if (record.getR94_note_holders() != null) {
			cell1.setCellValue(record.getR94_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR94_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR94_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR94_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR94_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR94_issuance_date() != null) {
			cell6.setCellValue(record.getR94_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR94_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR94_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR94_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR94_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR94_amount() != null) {
			cell9.setCellValue(record.getR94_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR94_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR94_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R95 =====
		row = sheet.getRow(94);

		cell1 = row.createCell(1);
		if (record.getR95_note_holders() != null) {
			cell1.setCellValue(record.getR95_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR95_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR95_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR95_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR95_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR95_issuance_date() != null) {
			cell6.setCellValue(record.getR95_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR95_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR95_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR95_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR95_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR95_amount() != null) {
			cell9.setCellValue(record.getR95_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR95_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR95_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R96 =====
		row = sheet.getRow(95);

		cell1 = row.createCell(1);
		if (record.getR96_note_holders() != null) {
			cell1.setCellValue(record.getR96_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR96_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR96_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR96_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR96_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR96_issuance_date() != null) {
			cell6.setCellValue(record.getR96_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR96_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR96_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR96_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR96_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR96_amount() != null) {
			cell9.setCellValue(record.getR96_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR96_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR96_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R97 =====
		row = sheet.getRow(96);

		cell1 = row.createCell(1);
		if (record.getR97_note_holders() != null) {
			cell1.setCellValue(record.getR97_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR97_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR97_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR97_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR97_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR97_issuance_date() != null) {
			cell6.setCellValue(record.getR97_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR97_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR97_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR97_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR97_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR97_amount() != null) {
			cell9.setCellValue(record.getR97_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR97_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR97_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R98 =====
		row = sheet.getRow(97);

		cell1 = row.createCell(1);
		if (record.getR98_note_holders() != null) {
			cell1.setCellValue(record.getR98_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR98_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR98_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR98_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR98_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR98_issuance_date() != null) {
			cell6.setCellValue(record.getR98_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR98_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR98_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR98_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR98_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR98_amount() != null) {
			cell9.setCellValue(record.getR98_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR98_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR98_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R99 =====
		row = sheet.getRow(98);

		cell1 = row.createCell(1);
		if (record.getR99_note_holders() != null) {
			cell1.setCellValue(record.getR99_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR99_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR99_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR99_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR99_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR99_issuance_date() != null) {
			cell6.setCellValue(record.getR99_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR99_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR99_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR99_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR99_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR99_amount() != null) {
			cell9.setCellValue(record.getR99_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR99_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR99_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R100 =====
		row = sheet.getRow(99);

		cell1 = row.createCell(1);
		if (record.getR100_note_holders() != null) {
			cell1.setCellValue(record.getR100_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR100_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR100_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR100_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR100_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR100_issuance_date() != null) {
			cell6.setCellValue(record.getR100_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR100_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR100_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR100_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR100_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR100_amount() != null) {
			cell9.setCellValue(record.getR100_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR100_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR100_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

	}

	private void populateEntity2Data(Sheet sheet, M_CA5_Summary_Entity2 record1, CellStyle textStyle,
			CellStyle numberStyle, CellStyle dateStyle) {
		Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
		CellStyle originalStyle;

		Row row = sheet.getRow(100);
		// --- R101 ---
		// Column C
		cell1 = row.createCell(1);
		if (record1.getR101_note_holders() != null) {
			cell1.setCellValue(record1.getR101_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR101_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR101_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR101_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR101_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR101_issuance_date() != null) {
			cell6.setCellValue(record1.getR101_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR101_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR101_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR101_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR101_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR101_amount() != null) {
			cell9.setCellValue(record1.getR101_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR101_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR101_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R102 =====
		row = sheet.getRow(101);

		cell1 = row.createCell(1);
		if (record1.getR102_note_holders() != null) {
			cell1.setCellValue(record1.getR102_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR102_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR102_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR102_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR102_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR102_issuance_date() != null) {
			cell6.setCellValue(record1.getR102_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR102_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR102_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR102_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR102_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR102_amount() != null) {
			cell9.setCellValue(record1.getR102_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR102_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR102_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R103 =====
		row = sheet.getRow(102);

		cell1 = row.createCell(1);
		if (record1.getR103_note_holders() != null) {
			cell1.setCellValue(record1.getR103_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR103_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR103_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR103_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR103_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR103_issuance_date() != null) {
			cell6.setCellValue(record1.getR103_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR103_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR103_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR103_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR103_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR103_amount() != null) {
			cell9.setCellValue(record1.getR103_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR103_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR103_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R104 =====
		row = sheet.getRow(103);

		cell1 = row.createCell(1);
		if (record1.getR104_note_holders() != null) {
			cell1.setCellValue(record1.getR104_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR104_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR104_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR104_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR104_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR104_issuance_date() != null) {
			cell6.setCellValue(record1.getR104_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR104_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR104_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR104_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR104_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR104_amount() != null) {
			cell9.setCellValue(record1.getR104_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR104_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR104_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R105 =====
		row = sheet.getRow(104);

		cell1 = row.createCell(1);
		if (record1.getR105_note_holders() != null) {
			cell1.setCellValue(record1.getR105_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR105_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR105_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR105_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR105_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR105_issuance_date() != null) {
			cell6.setCellValue(record1.getR105_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR105_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR105_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR105_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR105_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR105_amount() != null) {
			cell9.setCellValue(record1.getR105_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR105_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR105_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R106 =====
		row = sheet.getRow(105);

		cell1 = row.createCell(1);
		if (record1.getR106_note_holders() != null) {
			cell1.setCellValue(record1.getR106_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR106_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR106_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR106_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR106_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR106_issuance_date() != null) {
			cell6.setCellValue(record1.getR106_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR106_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR106_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR106_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR106_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR106_amount() != null) {
			cell9.setCellValue(record1.getR106_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR106_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR106_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R107 =====
		row = sheet.getRow(106);

		cell1 = row.createCell(1);
		if (record1.getR107_note_holders() != null) {
			cell1.setCellValue(record1.getR107_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR107_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR107_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR107_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR107_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR107_issuance_date() != null) {
			cell6.setCellValue(record1.getR107_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR107_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR107_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR107_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR107_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR107_amount() != null) {
			cell9.setCellValue(record1.getR107_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR107_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR107_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R108 =====
		row = sheet.getRow(107);

		cell1 = row.createCell(1);
		if (record1.getR108_note_holders() != null) {
			cell1.setCellValue(record1.getR108_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR108_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR108_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR108_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR108_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR108_issuance_date() != null) {
			cell6.setCellValue(record1.getR108_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR108_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR108_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR108_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR108_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR108_amount() != null) {
			cell9.setCellValue(record1.getR108_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR108_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR108_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R109 =====
		row = sheet.getRow(108);

		cell1 = row.createCell(1);
		if (record1.getR109_note_holders() != null) {
			cell1.setCellValue(record1.getR109_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR109_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR109_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR109_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR109_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR109_issuance_date() != null) {
			cell6.setCellValue(record1.getR109_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR109_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR109_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR109_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR109_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR109_amount() != null) {
			cell9.setCellValue(record1.getR109_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR109_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR109_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R110 =====
		row = sheet.getRow(109);

		cell1 = row.createCell(1);
		if (record1.getR110_note_holders() != null) {
			cell1.setCellValue(record1.getR110_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR110_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR110_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR110_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR110_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR110_issuance_date() != null) {
			cell6.setCellValue(record1.getR110_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR110_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR110_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR110_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR110_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR110_amount() != null) {
			cell9.setCellValue(record1.getR110_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR110_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR110_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R111 =====
		row = sheet.getRow(110);

		cell1 = row.createCell(1);
		if (record1.getR111_note_holders() != null) {
			cell1.setCellValue(record1.getR111_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR111_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR111_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR111_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR111_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR111_issuance_date() != null) {
			cell6.setCellValue(record1.getR111_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR111_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR111_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR111_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR111_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR111_amount() != null) {
			cell9.setCellValue(record1.getR111_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR111_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR111_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R112 =====
		row = sheet.getRow(111);

		cell1 = row.createCell(1);
		if (record1.getR112_note_holders() != null) {
			cell1.setCellValue(record1.getR112_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR112_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR112_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR112_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR112_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR112_issuance_date() != null) {
			cell6.setCellValue(record1.getR112_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR112_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR112_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR112_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR112_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR112_amount() != null) {
			cell9.setCellValue(record1.getR112_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR112_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR112_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R113 =====
		row = sheet.getRow(112);

		cell1 = row.createCell(1);
		if (record1.getR113_note_holders() != null) {
			cell1.setCellValue(record1.getR113_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR113_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR113_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR113_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR113_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR113_issuance_date() != null) {
			cell6.setCellValue(record1.getR113_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR113_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR113_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR113_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR113_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR113_amount() != null) {
			cell9.setCellValue(record1.getR113_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR113_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR113_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R114 =====
		row = sheet.getRow(113);

		cell1 = row.createCell(1);
		if (record1.getR114_note_holders() != null) {
			cell1.setCellValue(record1.getR114_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR114_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR114_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR114_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR114_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR114_issuance_date() != null) {
			cell6.setCellValue(record1.getR114_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR114_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR114_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR114_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR114_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR114_amount() != null) {
			cell9.setCellValue(record1.getR114_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR114_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR114_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R115 =====
		row = sheet.getRow(114);

		cell1 = row.createCell(1);
		if (record1.getR115_note_holders() != null) {
			cell1.setCellValue(record1.getR115_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR115_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR115_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR115_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR115_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR115_issuance_date() != null) {
			cell6.setCellValue(record1.getR115_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR115_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR115_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR115_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR115_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR115_amount() != null) {
			cell9.setCellValue(record1.getR115_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR115_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR115_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R116 =====
		row = sheet.getRow(115);

		cell1 = row.createCell(1);
		if (record1.getR116_note_holders() != null) {
			cell1.setCellValue(record1.getR116_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR116_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR116_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR116_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR116_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR116_issuance_date() != null) {
			cell6.setCellValue(record1.getR116_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR116_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR116_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR116_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR116_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR116_amount() != null) {
			cell9.setCellValue(record1.getR116_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR116_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR116_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R117 =====
		row = sheet.getRow(116);

		cell1 = row.createCell(1);
		if (record1.getR117_note_holders() != null) {
			cell1.setCellValue(record1.getR117_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR117_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR117_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR117_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR117_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR117_issuance_date() != null) {
			cell6.setCellValue(record1.getR117_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR117_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR117_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR117_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR117_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR117_amount() != null) {
			cell9.setCellValue(record1.getR117_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR117_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR117_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R118 =====
		row = sheet.getRow(117);

		cell1 = row.createCell(1);
		if (record1.getR118_note_holders() != null) {
			cell1.setCellValue(record1.getR118_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR118_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR118_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR118_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR118_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR118_issuance_date() != null) {
			cell6.setCellValue(record1.getR118_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR118_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR118_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR118_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR118_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR118_amount() != null) {
			cell9.setCellValue(record1.getR118_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR118_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR118_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R119 =====
		row = sheet.getRow(118);

		cell1 = row.createCell(1);
		if (record1.getR119_note_holders() != null) {
			cell1.setCellValue(record1.getR119_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR119_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR119_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR119_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR119_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR119_issuance_date() != null) {
			cell6.setCellValue(record1.getR119_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR119_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR119_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR119_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR119_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR119_amount() != null) {
			cell9.setCellValue(record1.getR119_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR119_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR119_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R120 =====
		row = sheet.getRow(119);

		cell1 = row.createCell(1);
		if (record1.getR120_note_holders() != null) {
			cell1.setCellValue(record1.getR120_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR120_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR120_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR120_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR120_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR120_issuance_date() != null) {
			cell6.setCellValue(record1.getR120_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR120_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR120_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR120_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR120_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR120_amount() != null) {
			cell9.setCellValue(record1.getR120_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR120_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR120_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R121 =====
		row = sheet.getRow(120);

		cell1 = row.createCell(1);
		if (record1.getR121_note_holders() != null) {
			cell1.setCellValue(record1.getR121_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR121_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR121_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR121_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR121_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR121_issuance_date() != null) {
			cell6.setCellValue(record1.getR121_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR121_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR121_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR121_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR121_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR121_amount() != null) {
			cell9.setCellValue(record1.getR121_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR121_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR121_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R122 =====
		row = sheet.getRow(121);

		cell1 = row.createCell(1);
		if (record1.getR122_note_holders() != null) {
			cell1.setCellValue(record1.getR122_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR122_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR122_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR122_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR122_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR122_issuance_date() != null) {
			cell6.setCellValue(record1.getR122_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR122_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR122_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR122_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR122_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR122_amount() != null) {
			cell9.setCellValue(record1.getR122_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR122_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR122_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R123 =====
		row = sheet.getRow(122);

		cell1 = row.createCell(1);
		if (record1.getR123_note_holders() != null) {
			cell1.setCellValue(record1.getR123_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR123_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR123_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR123_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR123_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR123_issuance_date() != null) {
			cell6.setCellValue(record1.getR123_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR123_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR123_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR123_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR123_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR123_amount() != null) {
			cell9.setCellValue(record1.getR123_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR123_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR123_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R124 =====
		row = sheet.getRow(123);

		cell1 = row.createCell(1);
		if (record1.getR124_note_holders() != null) {
			cell1.setCellValue(record1.getR124_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR124_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR124_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR124_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR124_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR124_issuance_date() != null) {
			cell6.setCellValue(record1.getR124_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR124_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR124_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR124_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR124_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR124_amount() != null) {
			cell9.setCellValue(record1.getR124_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR124_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR124_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R125 =====
		row = sheet.getRow(124);

		cell1 = row.createCell(1);
		if (record1.getR125_note_holders() != null) {
			cell1.setCellValue(record1.getR125_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR125_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR125_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR125_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR125_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR125_issuance_date() != null) {
			cell6.setCellValue(record1.getR125_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR125_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR125_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR125_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR125_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR125_amount() != null) {
			cell9.setCellValue(record1.getR125_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR125_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR125_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R126 =====
		row = sheet.getRow(125);

		cell1 = row.createCell(1);
		if (record1.getR126_note_holders() != null) {
			cell1.setCellValue(record1.getR126_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR126_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR126_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR126_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR126_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR126_issuance_date() != null) {
			cell6.setCellValue(record1.getR126_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR126_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR126_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR126_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR126_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR126_amount() != null) {
			cell9.setCellValue(record1.getR126_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR126_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR126_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R127 =====
		row = sheet.getRow(126);

		cell1 = row.createCell(1);
		if (record1.getR127_note_holders() != null) {
			cell1.setCellValue(record1.getR127_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR127_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR127_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR127_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR127_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR127_issuance_date() != null) {
			cell6.setCellValue(record1.getR127_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR127_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR127_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR127_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR127_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR127_amount() != null) {
			cell9.setCellValue(record1.getR127_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR127_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR127_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R128 =====
		row = sheet.getRow(127);

		cell1 = row.createCell(1);
		if (record1.getR128_note_holders() != null) {
			cell1.setCellValue(record1.getR128_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR128_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR128_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR128_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR128_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR128_issuance_date() != null) {
			cell6.setCellValue(record1.getR128_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR128_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR128_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR128_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR128_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR128_amount() != null) {
			cell9.setCellValue(record1.getR128_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR128_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR128_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R129 =====
		row = sheet.getRow(128);

		cell1 = row.createCell(1);
		if (record1.getR129_note_holders() != null) {
			cell1.setCellValue(record1.getR129_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR129_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR129_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR129_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR129_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR129_issuance_date() != null) {
			cell6.setCellValue(record1.getR129_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR129_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR129_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR129_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR129_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR129_amount() != null) {
			cell9.setCellValue(record1.getR129_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR129_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR129_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R130 =====
		row = sheet.getRow(129);

		cell1 = row.createCell(1);
		if (record1.getR130_note_holders() != null) {
			cell1.setCellValue(record1.getR130_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR130_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR130_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR130_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR130_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR130_issuance_date() != null) {
			cell6.setCellValue(record1.getR130_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR130_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR130_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR130_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR130_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR130_amount() != null) {
			cell9.setCellValue(record1.getR130_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR130_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR130_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R131 =====
		row = sheet.getRow(130);

		cell1 = row.createCell(1);
		if (record1.getR131_note_holders() != null) {
			cell1.setCellValue(record1.getR131_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR131_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR131_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR131_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR131_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR131_issuance_date() != null) {
			cell6.setCellValue(record1.getR131_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR131_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR131_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR131_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR131_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR131_amount() != null) {
			cell9.setCellValue(record1.getR131_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR131_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR131_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R132 =====
		row = sheet.getRow(131);

		cell1 = row.createCell(1);
		if (record1.getR132_note_holders() != null) {
			cell1.setCellValue(record1.getR132_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR132_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR132_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR132_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR132_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR132_issuance_date() != null) {
			cell6.setCellValue(record1.getR132_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR132_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR132_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR132_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR132_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR132_amount() != null) {
			cell9.setCellValue(record1.getR132_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR132_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR132_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R133 =====
		row = sheet.getRow(132);

		cell1 = row.createCell(1);
		if (record1.getR133_note_holders() != null) {
			cell1.setCellValue(record1.getR133_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR133_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR133_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR133_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR133_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR133_issuance_date() != null) {
			cell6.setCellValue(record1.getR133_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR133_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR133_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR133_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR133_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR133_amount() != null) {
			cell9.setCellValue(record1.getR133_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR133_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR133_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R134 =====
		row = sheet.getRow(133);

		cell1 = row.createCell(1);
		if (record1.getR134_note_holders() != null) {
			cell1.setCellValue(record1.getR134_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR134_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR134_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR134_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR134_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR134_issuance_date() != null) {
			cell6.setCellValue(record1.getR134_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR134_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR134_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR134_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR134_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR134_amount() != null) {
			cell9.setCellValue(record1.getR134_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR134_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR134_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R135 =====
		row = sheet.getRow(134);

		cell1 = row.createCell(1);
		if (record1.getR135_note_holders() != null) {
			cell1.setCellValue(record1.getR135_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR135_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR135_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR135_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR135_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR135_issuance_date() != null) {
			cell6.setCellValue(record1.getR135_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR135_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR135_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR135_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR135_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR135_amount() != null) {
			cell9.setCellValue(record1.getR135_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR135_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR135_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R136 =====
		row = sheet.getRow(135);

		cell1 = row.createCell(1);
		if (record1.getR136_note_holders() != null) {
			cell1.setCellValue(record1.getR136_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR136_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR136_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR136_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR136_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR136_issuance_date() != null) {
			cell6.setCellValue(record1.getR136_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR136_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR136_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR136_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR136_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR136_amount() != null) {
			cell9.setCellValue(record1.getR136_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR136_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR136_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R137 =====
		row = sheet.getRow(136);

		cell1 = row.createCell(1);
		if (record1.getR137_note_holders() != null) {
			cell1.setCellValue(record1.getR137_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR137_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR137_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR137_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR137_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR137_issuance_date() != null) {
			cell6.setCellValue(record1.getR137_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR137_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR137_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR137_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR137_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR137_amount() != null) {
			cell9.setCellValue(record1.getR137_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR137_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR137_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R138 =====
		row = sheet.getRow(137);

		cell1 = row.createCell(1);
		if (record1.getR138_note_holders() != null) {
			cell1.setCellValue(record1.getR138_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR138_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR138_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR138_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR138_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR138_issuance_date() != null) {
			cell6.setCellValue(record1.getR138_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR138_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR138_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR138_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR138_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR138_amount() != null) {
			cell9.setCellValue(record1.getR138_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR138_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR138_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R139 =====
		row = sheet.getRow(138);

		cell1 = row.createCell(1);
		if (record1.getR139_note_holders() != null) {
			cell1.setCellValue(record1.getR139_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR139_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR139_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR139_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR139_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR139_issuance_date() != null) {
			cell6.setCellValue(record1.getR139_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR139_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR139_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR139_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR139_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR139_amount() != null) {
			cell9.setCellValue(record1.getR139_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR139_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR139_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R140 =====
		row = sheet.getRow(139);

		cell1 = row.createCell(1);
		if (record1.getR140_note_holders() != null) {
			cell1.setCellValue(record1.getR140_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR140_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR140_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR140_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR140_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR140_issuance_date() != null) {
			cell6.setCellValue(record1.getR140_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR140_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR140_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR140_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR140_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR140_amount() != null) {
			cell9.setCellValue(record1.getR140_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR140_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR140_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R141 =====
		row = sheet.getRow(140);

		cell1 = row.createCell(1);
		if (record1.getR141_note_holders() != null) {
			cell1.setCellValue(record1.getR141_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR141_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR141_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR141_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR141_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR141_issuance_date() != null) {
			cell6.setCellValue(record1.getR141_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR141_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR141_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR141_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR141_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR141_amount() != null) {
			cell9.setCellValue(record1.getR141_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR141_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR141_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R142 =====
		row = sheet.getRow(141);

		cell1 = row.createCell(1);
		if (record1.getR142_note_holders() != null) {
			cell1.setCellValue(record1.getR142_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR142_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR142_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR142_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR142_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR142_issuance_date() != null) {
			cell6.setCellValue(record1.getR142_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR142_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR142_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR142_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR142_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR142_amount() != null) {
			cell9.setCellValue(record1.getR142_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR142_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR142_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R143 =====
		row = sheet.getRow(142);

		cell1 = row.createCell(1);
		if (record1.getR143_note_holders() != null) {
			cell1.setCellValue(record1.getR143_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR143_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR143_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR143_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR143_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR143_issuance_date() != null) {
			cell6.setCellValue(record1.getR143_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR143_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR143_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR143_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR143_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR143_amount() != null) {
			cell9.setCellValue(record1.getR143_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR143_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR143_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R144 =====
		row = sheet.getRow(143);

		cell1 = row.createCell(1);
		if (record1.getR144_note_holders() != null) {
			cell1.setCellValue(record1.getR144_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR144_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR144_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR144_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR144_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR144_issuance_date() != null) {
			cell6.setCellValue(record1.getR144_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR144_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR144_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR144_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR144_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR144_amount() != null) {
			cell9.setCellValue(record1.getR144_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR144_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR144_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R145 =====
		row = sheet.getRow(144);

		cell1 = row.createCell(1);
		if (record1.getR145_note_holders() != null) {
			cell1.setCellValue(record1.getR145_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR145_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR145_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR145_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR145_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR145_issuance_date() != null) {
			cell6.setCellValue(record1.getR145_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR145_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR145_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR145_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR145_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR145_amount() != null) {
			cell9.setCellValue(record1.getR145_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR145_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR145_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R146 =====
		row = sheet.getRow(145);

		cell1 = row.createCell(1);
		if (record1.getR146_note_holders() != null) {
			cell1.setCellValue(record1.getR146_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR146_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR146_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR146_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR146_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR146_issuance_date() != null) {
			cell6.setCellValue(record1.getR146_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR146_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR146_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR146_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR146_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR146_amount() != null) {
			cell9.setCellValue(record1.getR146_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR146_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR146_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R147 =====
		row = sheet.getRow(146);

		cell1 = row.createCell(1);
		if (record1.getR147_note_holders() != null) {
			cell1.setCellValue(record1.getR147_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR147_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR147_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR147_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR147_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR147_issuance_date() != null) {
			cell6.setCellValue(record1.getR147_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR147_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR147_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR147_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR147_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR147_amount() != null) {
			cell9.setCellValue(record1.getR147_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR147_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR147_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R148 =====
		row = sheet.getRow(147);

		cell1 = row.createCell(1);
		if (record1.getR148_note_holders() != null) {
			cell1.setCellValue(record1.getR148_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR148_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR148_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR148_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR148_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR148_issuance_date() != null) {
			cell6.setCellValue(record1.getR148_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR148_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR148_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR148_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR148_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR148_amount() != null) {
			cell9.setCellValue(record1.getR148_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR148_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR148_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R149 =====
		row = sheet.getRow(148);

		cell1 = row.createCell(1);
		if (record1.getR149_note_holders() != null) {
			cell1.setCellValue(record1.getR149_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR149_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR149_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR149_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR149_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR149_issuance_date() != null) {
			cell6.setCellValue(record1.getR149_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR149_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR149_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR149_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR149_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR149_amount() != null) {
			cell9.setCellValue(record1.getR149_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR149_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR149_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

	}

	/*
	 * public byte[] getM_CA5DetailExcel(String filename, String fromdate, String
	 * todate) { try { logger.info("Generating Excel for M_CA5 Details...");
	 * System.out.println("came to Detail download service");
	 * 
	 * XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("M_CA5Detail");
	 * 
	 * // Common border style BorderStyle border = BorderStyle.THIN;
	 * 
	 * // Header style (left aligned) CellStyle headerStyle =
	 * workbook.createCellStyle(); Font headerFont = workbook.createFont();
	 * headerFont.setBold(true); headerFont.setFontHeightInPoints((short) 10);
	 * headerStyle.setFont(headerFont);
	 * headerStyle.setAlignment(HorizontalAlignment.LEFT);
	 * headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	 * headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	 * headerStyle.setBorderTop(border); headerStyle.setBorderBottom(border);
	 * headerStyle.setBorderLeft(border); headerStyle.setBorderRight(border);
	 * 
	 * // Right-aligned header style for ACCT BALANCE CellStyle
	 * rightAlignedHeaderStyle = workbook.createCellStyle();
	 * rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
	 * rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * 
	 * // Default data style (left aligned) CellStyle dataStyle =
	 * workbook.createCellStyle(); dataStyle.setAlignment(HorizontalAlignment.LEFT);
	 * dataStyle.setBorderTop(border); dataStyle.setBorderBottom(border);
	 * dataStyle.setBorderLeft(border); dataStyle.setBorderRight(border);
	 * 
	 * // ACCT BALANCE style (right aligned with 3 decimals) CellStyle balanceStyle
	 * = workbook.createCellStyle();
	 * balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
	 * balanceStyle.setBorderTop(border); balanceStyle.setBorderBottom(border);
	 * balanceStyle.setBorderLeft(border); balanceStyle.setBorderRight(border);
	 * 
	 * // Header row String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME",
	 * "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE" };
	 * 
	 * XSSFRow headerRow = sheet.createRow(0); for (int i = 0; i < headers.length;
	 * i++) { Cell cell = headerRow.createCell(i); cell.setCellValue(headers[i]);
	 * 
	 * if (i == 3) { // ACCT BALANCE cell.setCellStyle(rightAlignedHeaderStyle); }
	 * else { cell.setCellStyle(headerStyle); }
	 * 
	 * sheet.setColumnWidth(i, 5000); }
	 * 
	 * // Get data Date parsedToDate = new
	 * SimpleDateFormat("dd/MM/yyyy").parse(todate); List<M_CA5_Detail_Entity>
	 * reportData = BRRS_M_CA5_Detail_Repo.getdatabydateList(parsedToDate);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_CA5_Detail_Entity item : reportData) { XSSFRow row =
	 * sheet.createRow(rowIndex++);
	 * 
	 * row.createCell(0).setCellValue(item.getCustId());
	 * row.createCell(1).setCellValue(item.getAcctNumber());
	 * row.createCell(2).setCellValue(item.getAcctName());
	 * 
	 * // ACCT BALANCE (right aligned, 3 decimal places) Cell balanceCell =
	 * row.createCell(3); if (item.getAcctBalanceInPula() != null) {
	 * balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue()); } else {
	 * balanceCell.setCellValue(0.000); } balanceCell.setCellStyle(balanceStyle);
	 * 
	 * row.createCell(4).setCellValue(item.getRowId());
	 * row.createCell(5).setCellValue(item.getColumnId());
	 * row.createCell(6).setCellValue( item.getReportDate() != null ? new
	 * SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : "" );
	 * 
	 * // Apply data style for all other cells for (int j = 0; j < 7; j++) { if (j
	 * != 3) { row.getCell(j).setCellStyle(dataStyle); } } } } else {
	 * logger.info("No data found for M_CA5 — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating M_CA5 Excel", e);
	 * return new byte[0]; } }
	 */

	// public List<Object> getM_CA5Archival() {
	// List<Object> M_CA5Archivallist = new ArrayList<>();
	// List<Object> M_CA5Archivallist1 = new ArrayList<>();
	// try {
	// M_CA5Archivallist = M_CA5_Archival_Summary_Repo1.getM_CA5archival();
	// M_CA5Archivallist1 = M_CA5_Archival_Summary_Repo2.getM_CA5archival();
	// System.out.println("countser" + M_CA5Archivallist.size());
	// System.out.println("countser" + M_CA5Archivallist1.size());
	// } catch (Exception e) {
	// // Log the exception
	// System.err.println("Error fetching M_CA5 Archival data: " + e.getMessage());
	// e.printStackTrace();

	// Optionally, you can rethrow it or return empty list
	// throw new RuntimeException("Failed to fetch data", e);
	// }
	// return M_CA5Archivallist;
	// }

	public byte[] getExcelM_CA5ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") && version != null) {
			// Additional logic can go here if needed
		}

		// --- Fetch Data ---
		List<M_CA5_Archival_Summary_Entity1> dataList = M_CA5_Archival_Summary_Repo1
				.getdatabydateListarchival(dateformat.parse(todate), version);

		List<M_CA5_Archival_Summary_Entity2> dataList1 = M_CA5_Archival_Summary_Repo2
				.getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty() && dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_CA5 archival report. Returning empty result.");
			return new byte[0];
		}

		// --- Load Excel Template ---
		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		Path templatePath = Paths.get(templateDir, templateFileName);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		// --- Create Workbook ---
		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);
			CreationHelper createHelper = workbook.getCreationHelper();

			// --- Style Definitions ---
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

			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8);
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);

			// --- Populate Data ---
			if (!dataList.isEmpty()) {
				archivalpopulateEntity1Data(sheet, dataList.get(0), textStyle, numberStyle, numberStyle);
			}

			if (!dataList1.isEmpty()) {
				archivalpopulateEntity2Data(sheet, dataList1.get(0), textStyle, numberStyle, numberStyle);
			}

			// --- Finalize Workbook ---
			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();

		} catch (Exception e) {
			logger.error("Service: Error occurred while generating Excel file: {}", e.getMessage(), e);
			throw e;
		}
	}

	// cell code should be for only select query written cells not for text and also
	// not for formula cells
	// here first Cell cell1 is declaring variable
	// createCell(2)<--this 2 is the index value of the excel cell
	// i.e, A column is cell(0) B ->1, C->2....
	// record is declared above to fetch from which entity

	private void archivalpopulateEntity1Data(Sheet sheet, M_CA5_Archival_Summary_Entity1 record, CellStyle textStyle,
			CellStyle numberStyle, CellStyle dateStyle) {
		Cell cell1, cell2, cell5, cell6, cell7, cell8, cell9, cell10;
		CellStyle originalStyle;

		// Column C
		Row row = sheet.getRow(13);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR14_note_holders() != null) {
			cell1.setCellValue(record.getR14_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR14_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR14_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR14_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR14_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR14_issuance_date() != null) {
			cell6.setCellValue(record.getR14_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G

		cell7 = row.createCell(7);
		if (record.getR14_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR14_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR14_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR14_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR14_amount() != null) {
			cell9.setCellValue(record.getR14_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		row = sheet.getRow(14);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR15_note_holders() != null) {
			cell1.setCellValue(record.getR15_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR15_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR15_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR15_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR15_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR15_issuance_date() != null) {
			cell6.setCellValue(record.getR15_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR15_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR15_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR15_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR15_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR15_amount() != null) {
			cell9.setCellValue(record.getR15_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R16 =================
		row = sheet.getRow(15);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR16_note_holders() != null) {
			cell1.setCellValue(record.getR16_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR16_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR16_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR16_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR16_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR16_issuance_date() != null) {
			cell6.setCellValue(record.getR16_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR16_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR16_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR16_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR16_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR16_amount() != null) {
			cell9.setCellValue(record.getR16_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R17 =================
		row = sheet.getRow(16);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR17_note_holders() != null) {
			cell1.setCellValue(record.getR17_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR17_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR17_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR17_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR17_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR17_issuance_date() != null) {
			cell6.setCellValue(record.getR17_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR17_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR17_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR17_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR17_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR17_amount() != null) {
			cell9.setCellValue(record.getR17_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R18 =================
		row = sheet.getRow(17);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR18_note_holders() != null) {
			cell1.setCellValue(record.getR18_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR18_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR18_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR18_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR18_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR18_issuance_date() != null) {
			cell6.setCellValue(record.getR18_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR18_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR18_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR18_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR18_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR18_amount() != null) {
			cell9.setCellValue(record.getR18_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R19 =================
		row = sheet.getRow(18);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR19_note_holders() != null) {
			cell1.setCellValue(record.getR19_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR19_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR19_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR19_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR19_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR19_issuance_date() != null) {
			cell6.setCellValue(record.getR19_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR19_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR19_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR19_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR19_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR19_amount() != null) {
			cell9.setCellValue(record.getR19_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R20 =================
		row = sheet.getRow(19);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR20_note_holders() != null) {
			cell1.setCellValue(record.getR20_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR20_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR20_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR20_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR20_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR20_issuance_date() != null) {
			cell6.setCellValue(record.getR20_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR20_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR20_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR20_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR20_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR20_amount() != null) {
			cell9.setCellValue(record.getR20_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R21 =================
		row = sheet.getRow(20);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR21_note_holders() != null) {
			cell1.setCellValue(record.getR21_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR21_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR21_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR21_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR21_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR21_issuance_date() != null) {
			cell6.setCellValue(record.getR21_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR21_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR21_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR21_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR21_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR21_amount() != null) {
			cell9.setCellValue(record.getR21_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R22 =================
		row = sheet.getRow(21);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR22_note_holders() != null) {
			cell1.setCellValue(record.getR22_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR22_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR22_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR22_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR22_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR22_issuance_date() != null) {
			cell6.setCellValue(record.getR22_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR22_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR22_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR22_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR22_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR22_amount() != null) {
			cell9.setCellValue(record.getR22_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R23 =================
		row = sheet.getRow(22);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR23_note_holders() != null) {
			cell1.setCellValue(record.getR23_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR23_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR23_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR23_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR23_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR23_issuance_date() != null) {
			cell6.setCellValue(record.getR23_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR23_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR23_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR23_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR23_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR23_amount() != null) {
			cell9.setCellValue(record.getR23_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R24 =================
		row = sheet.getRow(23);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR24_note_holders() != null) {
			cell1.setCellValue(record.getR24_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR24_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR24_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR24_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR24_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR24_issuance_date() != null) {
			cell6.setCellValue(record.getR24_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR24_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR24_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR24_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR24_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR24_amount() != null) {
			cell9.setCellValue(record.getR24_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R25 =================
		row = sheet.getRow(24);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR25_note_holders() != null) {
			cell1.setCellValue(record.getR25_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR25_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR25_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR25_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR25_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR25_issuance_date() != null) {
			cell6.setCellValue(record.getR25_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR25_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR25_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR25_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR25_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR25_amount() != null) {
			cell9.setCellValue(record.getR25_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R26 =================
		row = sheet.getRow(25);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR26_note_holders() != null) {
			cell1.setCellValue(record.getR26_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR26_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR26_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR26_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR26_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR26_issuance_date() != null) {
			cell6.setCellValue(record.getR26_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR26_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR26_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR26_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR26_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR26_amount() != null) {
			cell9.setCellValue(record.getR26_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R27 =================
		row = sheet.getRow(26);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR27_note_holders() != null) {
			cell1.setCellValue(record.getR27_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR27_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR27_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR27_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR27_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR27_issuance_date() != null) {
			cell6.setCellValue(record.getR27_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR27_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR27_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR27_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR27_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// Column I
		cell9 = row.createCell(9);
		if (record.getR27_amount() != null) {
			cell9.setCellValue(record.getR27_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		// ================= R28 =================
		row = sheet.getRow(27);
		// Column C
		cell1 = row.createCell(1);
		if (record.getR28_note_holders() != null) {
			cell1.setCellValue(record.getR28_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		// Column D
		cell2 = row.createCell(2);
		if (record.getR28_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR28_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		// Column E
		cell5 = row.createCell(5);
		if (record.getR28_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR28_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		// Column F
		cell6 = row.createCell(6);
		if (record.getR28_issuance_date() != null) {
			cell6.setCellValue(record.getR28_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		// Column G
		cell7 = row.createCell(7);
		if (record.getR28_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR28_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		// Column H
		cell8 = row.createCell(8);
		if (record.getR28_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR28_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		// ===== Row R35 =====
		row = sheet.getRow(34);

		cell1 = row.createCell(1);
		if (record.getR35_note_holders() != null) {
			cell1.setCellValue(record.getR35_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR35_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR35_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR35_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR35_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR35_issuance_date() != null) {
			cell6.setCellValue(record.getR35_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR35_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR35_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR35_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR35_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR35_amount() != null) {
			cell9.setCellValue(record.getR35_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR35_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR35_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R36 =====
		row = sheet.getRow(35);

		cell1 = row.createCell(1);
		if (record.getR36_note_holders() != null) {
			cell1.setCellValue(record.getR36_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR36_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR36_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR36_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR36_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR36_issuance_date() != null) {
			cell6.setCellValue(record.getR36_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR36_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR36_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR36_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR36_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR36_amount() != null) {
			cell9.setCellValue(record.getR36_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR36_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR36_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R37 =====
		row = sheet.getRow(36);

		cell1 = row.createCell(1);
		if (record.getR37_note_holders() != null) {
			cell1.setCellValue(record.getR37_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR37_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR37_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR37_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR37_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR37_issuance_date() != null) {
			cell6.setCellValue(record.getR37_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR37_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR37_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR37_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR37_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR37_amount() != null) {
			cell9.setCellValue(record.getR37_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR37_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR37_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R38 =====
		row = sheet.getRow(37);

		cell1 = row.createCell(1);
		if (record.getR38_note_holders() != null) {
			cell1.setCellValue(record.getR38_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR38_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR38_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR38_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR38_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR38_issuance_date() != null) {
			cell6.setCellValue(record.getR38_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR38_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR38_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR38_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR38_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR38_amount() != null) {
			cell9.setCellValue(record.getR38_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR38_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR38_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R39 =====
		row = sheet.getRow(38);

		cell1 = row.createCell(1);
		if (record.getR39_note_holders() != null) {
			cell1.setCellValue(record.getR39_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR39_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR39_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR39_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR39_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR39_issuance_date() != null) {
			cell6.setCellValue(record.getR39_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR39_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR39_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR39_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR39_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR39_amount() != null) {
			cell9.setCellValue(record.getR39_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR39_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR39_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R40 =====
		row = sheet.getRow(39);

		cell1 = row.createCell(1);
		if (record.getR40_note_holders() != null) {
			cell1.setCellValue(record.getR40_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR40_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR40_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR40_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR40_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR40_issuance_date() != null) {
			cell6.setCellValue(record.getR40_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR40_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR40_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR40_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR40_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR40_amount() != null) {
			cell9.setCellValue(record.getR40_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR40_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR40_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R41 =====
		row = sheet.getRow(40);

		cell1 = row.createCell(1);
		if (record.getR41_note_holders() != null) {
			cell1.setCellValue(record.getR41_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR41_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR41_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR41_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR41_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR41_issuance_date() != null) {
			cell6.setCellValue(record.getR41_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR41_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR41_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR41_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR41_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR41_amount() != null) {
			cell9.setCellValue(record.getR41_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR41_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR41_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R42 =====
		row = sheet.getRow(41);

		cell1 = row.createCell(1);
		if (record.getR42_note_holders() != null) {
			cell1.setCellValue(record.getR42_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR42_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR42_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR42_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR42_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR42_issuance_date() != null) {
			cell6.setCellValue(record.getR42_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR42_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR42_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR42_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR42_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR42_amount() != null) {
			cell9.setCellValue(record.getR42_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR42_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR42_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R43 =====
		row = sheet.getRow(42);

		cell1 = row.createCell(1);
		if (record.getR43_note_holders() != null) {
			cell1.setCellValue(record.getR43_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR43_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR43_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR43_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR43_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR43_issuance_date() != null) {
			cell6.setCellValue(record.getR43_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR43_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR43_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR43_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR43_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR43_amount() != null) {
			cell9.setCellValue(record.getR43_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR43_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR43_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R44 =====
		row = sheet.getRow(43);

		cell1 = row.createCell(1);
		if (record.getR44_note_holders() != null) {
			cell1.setCellValue(record.getR44_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR44_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR44_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR44_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR44_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR44_issuance_date() != null) {
			cell6.setCellValue(record.getR44_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR44_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR44_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR44_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR44_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR44_amount() != null) {
			cell9.setCellValue(record.getR44_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR44_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR44_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R45 =====
		row = sheet.getRow(44);

		cell1 = row.createCell(1);
		if (record.getR45_note_holders() != null) {
			cell1.setCellValue(record.getR45_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR45_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR45_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR45_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR45_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR45_issuance_date() != null) {
			cell6.setCellValue(record.getR45_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR45_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR45_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR45_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR45_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR45_amount() != null) {
			cell9.setCellValue(record.getR45_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR45_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR45_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R46 =====
		row = sheet.getRow(45);

		cell1 = row.createCell(1);
		if (record.getR46_note_holders() != null) {
			cell1.setCellValue(record.getR46_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR46_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR46_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR46_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR46_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR46_issuance_date() != null) {
			cell6.setCellValue(record.getR46_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR46_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR46_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR46_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR46_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR46_amount() != null) {
			cell9.setCellValue(record.getR46_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR46_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR46_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R47 =====
		row = sheet.getRow(46);

		cell1 = row.createCell(1);
		if (record.getR47_note_holders() != null) {
			cell1.setCellValue(record.getR47_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR47_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR47_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR47_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR47_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR47_issuance_date() != null) {
			cell6.setCellValue(record.getR47_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR47_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR47_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR47_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR47_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR47_amount() != null) {
			cell9.setCellValue(record.getR47_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR47_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR47_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R48 =====
		row = sheet.getRow(47);

		cell1 = row.createCell(1);
		if (record.getR48_note_holders() != null) {
			cell1.setCellValue(record.getR48_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR48_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR48_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR48_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR48_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR48_issuance_date() != null) {
			cell6.setCellValue(record.getR48_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR48_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR48_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR48_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR48_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR48_amount() != null) {
			cell9.setCellValue(record.getR48_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR48_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR48_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R49 =====
		row = sheet.getRow(48);

		cell1 = row.createCell(1);
		if (record.getR49_note_holders() != null) {
			cell1.setCellValue(record.getR49_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR49_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR49_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR49_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR49_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR49_issuance_date() != null) {
			cell6.setCellValue(record.getR49_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR49_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR49_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR49_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR49_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR49_amount() != null) {
			cell9.setCellValue(record.getR49_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR49_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR49_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R50 =====
		row = sheet.getRow(49);

		cell1 = row.createCell(1);
		if (record.getR50_note_holders() != null) {
			cell1.setCellValue(record.getR50_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR50_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR50_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR50_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR50_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR50_issuance_date() != null) {
			cell6.setCellValue(record.getR50_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR50_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR50_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR50_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR50_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR50_amount() != null) {
			cell9.setCellValue(record.getR50_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR50_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR50_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R51 =====
		row = sheet.getRow(50);

		cell1 = row.createCell(1);
		if (record.getR51_note_holders() != null) {
			cell1.setCellValue(record.getR51_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR51_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR51_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR51_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR51_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR51_issuance_date() != null) {
			cell6.setCellValue(record.getR51_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR51_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR51_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR51_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR51_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR51_amount() != null) {
			cell9.setCellValue(record.getR51_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR51_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR51_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R52 =====
		row = sheet.getRow(51);

		cell1 = row.createCell(1);
		if (record.getR52_note_holders() != null) {
			cell1.setCellValue(record.getR52_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR52_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR52_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR52_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR52_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR52_issuance_date() != null) {
			cell6.setCellValue(record.getR52_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR52_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR52_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR52_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR52_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR52_amount() != null) {
			cell9.setCellValue(record.getR52_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR52_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR52_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R53 =====
		row = sheet.getRow(52);

		cell1 = row.createCell(1);
		if (record.getR53_note_holders() != null) {
			cell1.setCellValue(record.getR53_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR53_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR53_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR53_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR53_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR53_issuance_date() != null) {
			cell6.setCellValue(record.getR53_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR53_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR53_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR53_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR53_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR53_amount() != null) {
			cell9.setCellValue(record.getR53_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR53_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR53_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R54 =====
		row = sheet.getRow(53);

		cell1 = row.createCell(1);
		if (record.getR54_note_holders() != null) {
			cell1.setCellValue(record.getR54_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR54_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR54_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR54_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR54_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR54_issuance_date() != null) {
			cell6.setCellValue(record.getR54_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR54_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR54_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR54_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR54_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR54_amount() != null) {
			cell9.setCellValue(record.getR54_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR54_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR54_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R55 =====
		row = sheet.getRow(54);

		cell1 = row.createCell(1);
		if (record.getR55_note_holders() != null) {
			cell1.setCellValue(record.getR55_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR55_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR55_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR55_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR55_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR55_issuance_date() != null) {
			cell6.setCellValue(record.getR55_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR55_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR55_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR55_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR55_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR55_amount() != null) {
			cell9.setCellValue(record.getR55_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR55_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR55_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R56 =====
		row = sheet.getRow(55);

		cell1 = row.createCell(1);
		if (record.getR56_note_holders() != null) {
			cell1.setCellValue(record.getR56_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR56_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR56_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR56_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR56_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR56_issuance_date() != null) {
			cell6.setCellValue(record.getR56_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR56_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR56_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR56_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR56_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR56_amount() != null) {
			cell9.setCellValue(record.getR56_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR56_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR56_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R57 =====
		row = sheet.getRow(56);

		cell1 = row.createCell(1);
		if (record.getR57_note_holders() != null) {
			cell1.setCellValue(record.getR57_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR57_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR57_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR57_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR57_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR57_issuance_date() != null) {
			cell6.setCellValue(record.getR57_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR57_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR57_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR57_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR57_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR57_amount() != null) {
			cell9.setCellValue(record.getR57_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR57_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR57_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R58 =====
		row = sheet.getRow(57);

		cell1 = row.createCell(1);
		if (record.getR58_note_holders() != null) {
			cell1.setCellValue(record.getR58_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR58_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR58_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR58_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR58_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR58_issuance_date() != null) {
			cell6.setCellValue(record.getR58_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR58_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR58_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR58_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR58_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR58_amount() != null) {
			cell9.setCellValue(record.getR58_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR58_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR58_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R59 =====
		row = sheet.getRow(58);

		cell1 = row.createCell(1);
		if (record.getR59_note_holders() != null) {
			cell1.setCellValue(record.getR59_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR59_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR59_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR59_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR59_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR59_issuance_date() != null) {
			cell6.setCellValue(record.getR59_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR59_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR59_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR59_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR59_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR59_amount() != null) {
			cell9.setCellValue(record.getR59_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR59_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR59_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R60 =====
		row = sheet.getRow(59);

		cell1 = row.createCell(1);
		if (record.getR60_note_holders() != null) {
			cell1.setCellValue(record.getR60_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR60_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR60_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR60_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR60_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR60_issuance_date() != null) {
			cell6.setCellValue(record.getR60_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR60_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR60_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR60_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR60_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR60_amount() != null) {
			cell9.setCellValue(record.getR60_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR60_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR60_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R61 =====
		row = sheet.getRow(60);

		cell1 = row.createCell(1);
		if (record.getR61_note_holders() != null) {
			cell1.setCellValue(record.getR61_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR61_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR61_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR61_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR61_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR61_issuance_date() != null) {
			cell6.setCellValue(record.getR61_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR61_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR61_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR61_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR61_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR61_amount() != null) {
			cell9.setCellValue(record.getR61_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR61_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR61_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R62 =====
		row = sheet.getRow(61);

		cell1 = row.createCell(1);
		if (record.getR62_note_holders() != null) {
			cell1.setCellValue(record.getR62_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR62_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR62_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR62_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR62_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR62_issuance_date() != null) {
			cell6.setCellValue(record.getR62_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR62_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR62_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR62_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR62_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR62_amount() != null) {
			cell9.setCellValue(record.getR62_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR62_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR62_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R63 =====
		row = sheet.getRow(62);

		cell1 = row.createCell(1);
		if (record.getR63_note_holders() != null) {
			cell1.setCellValue(record.getR63_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR63_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR63_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR63_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR63_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR63_issuance_date() != null) {
			cell6.setCellValue(record.getR63_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR63_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR63_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR63_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR63_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR63_amount() != null) {
			cell9.setCellValue(record.getR63_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR63_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR63_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R64 =====
		row = sheet.getRow(63);

		cell1 = row.createCell(1);
		if (record.getR64_note_holders() != null) {
			cell1.setCellValue(record.getR64_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR64_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR64_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR64_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR64_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR64_issuance_date() != null) {
			cell6.setCellValue(record.getR64_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR64_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR64_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR64_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR64_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR64_amount() != null) {
			cell9.setCellValue(record.getR64_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR64_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR64_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R65 =====
		row = sheet.getRow(64);

		cell1 = row.createCell(1);
		if (record.getR65_note_holders() != null) {
			cell1.setCellValue(record.getR65_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR65_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR65_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR65_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR65_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR65_issuance_date() != null) {
			cell6.setCellValue(record.getR65_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR65_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR65_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR65_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR65_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR65_amount() != null) {
			cell9.setCellValue(record.getR65_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR65_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR65_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R66 =====
		row = sheet.getRow(65);

		cell1 = row.createCell(1);
		if (record.getR66_note_holders() != null) {
			cell1.setCellValue(record.getR66_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR66_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR66_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR66_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR66_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR66_issuance_date() != null) {
			cell6.setCellValue(record.getR66_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR66_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR66_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR66_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR66_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR66_amount() != null) {
			cell9.setCellValue(record.getR66_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR66_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR66_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R67 =====
		row = sheet.getRow(66);

		cell1 = row.createCell(1);
		if (record.getR67_note_holders() != null) {
			cell1.setCellValue(record.getR67_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR67_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR67_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR67_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR67_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR67_issuance_date() != null) {
			cell6.setCellValue(record.getR67_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR67_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR67_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR67_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR67_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR67_amount() != null) {
			cell9.setCellValue(record.getR67_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR67_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR67_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R68 =====
		row = sheet.getRow(67);

		cell1 = row.createCell(1);
		if (record.getR68_note_holders() != null) {
			cell1.setCellValue(record.getR68_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR68_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR68_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR68_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR68_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR68_issuance_date() != null) {
			cell6.setCellValue(record.getR68_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR68_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR68_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR68_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR68_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR68_amount() != null) {
			cell9.setCellValue(record.getR68_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR68_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR68_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R69 =====
		row = sheet.getRow(68);

		cell1 = row.createCell(1);
		if (record.getR69_note_holders() != null) {
			cell1.setCellValue(record.getR69_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR69_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR69_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR69_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR69_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR69_issuance_date() != null) {
			cell6.setCellValue(record.getR69_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR69_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR69_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR69_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR69_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR69_amount() != null) {
			cell9.setCellValue(record.getR69_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR69_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR69_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R70 =====
		row = sheet.getRow(69);

		cell1 = row.createCell(1);
		if (record.getR70_note_holders() != null) {
			cell1.setCellValue(record.getR70_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR70_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR70_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR70_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR70_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR70_issuance_date() != null) {
			cell6.setCellValue(record.getR70_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR70_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR70_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR70_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR70_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR70_amount() != null) {
			cell9.setCellValue(record.getR70_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR70_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR70_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R71 =====
		row = sheet.getRow(70);

		cell1 = row.createCell(1);
		if (record.getR71_note_holders() != null) {
			cell1.setCellValue(record.getR71_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR71_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR71_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR71_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR71_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR71_issuance_date() != null) {
			cell6.setCellValue(record.getR71_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR71_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR71_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR71_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR71_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR71_amount() != null) {
			cell9.setCellValue(record.getR71_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR71_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR71_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R72 =====
		row = sheet.getRow(71);

		cell1 = row.createCell(1);
		if (record.getR72_note_holders() != null) {
			cell1.setCellValue(record.getR72_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR72_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR72_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR72_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR72_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR72_issuance_date() != null) {
			cell6.setCellValue(record.getR72_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR72_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR72_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR72_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR72_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR72_amount() != null) {
			cell9.setCellValue(record.getR72_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR72_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR72_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R73 =====
		row = sheet.getRow(72);

		cell1 = row.createCell(1);
		if (record.getR73_note_holders() != null) {
			cell1.setCellValue(record.getR73_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR73_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR73_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR73_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR73_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR73_issuance_date() != null) {
			cell6.setCellValue(record.getR73_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR73_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR73_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR73_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR73_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR73_amount() != null) {
			cell9.setCellValue(record.getR73_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR73_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR73_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R74 =====
		row = sheet.getRow(73);

		cell1 = row.createCell(1);
		if (record.getR74_note_holders() != null) {
			cell1.setCellValue(record.getR74_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR74_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR74_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR74_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR74_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR74_issuance_date() != null) {
			cell6.setCellValue(record.getR74_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR74_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR74_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR74_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR74_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR74_amount() != null) {
			cell9.setCellValue(record.getR74_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR74_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR74_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R75 =====
		row = sheet.getRow(74);

		cell1 = row.createCell(1);
		if (record.getR75_note_holders() != null) {
			cell1.setCellValue(record.getR75_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR75_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR75_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR75_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR75_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR75_issuance_date() != null) {
			cell6.setCellValue(record.getR75_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR75_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR75_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR75_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR75_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR75_amount() != null) {
			cell9.setCellValue(record.getR75_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR75_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR75_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R76 =====
		row = sheet.getRow(75);

		cell1 = row.createCell(1);
		if (record.getR76_note_holders() != null) {
			cell1.setCellValue(record.getR76_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR76_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR76_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR76_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR76_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR76_issuance_date() != null) {
			cell6.setCellValue(record.getR76_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR76_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR76_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR76_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR76_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR76_amount() != null) {
			cell9.setCellValue(record.getR76_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR76_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR76_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R77 =====
		row = sheet.getRow(76);

		cell1 = row.createCell(1);
		if (record.getR77_note_holders() != null) {
			cell1.setCellValue(record.getR77_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR77_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR77_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR77_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR77_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR77_issuance_date() != null) {
			cell6.setCellValue(record.getR77_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR77_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR77_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR77_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR77_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR77_amount() != null) {
			cell9.setCellValue(record.getR77_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR77_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR77_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R78 =====
		row = sheet.getRow(77);

		cell1 = row.createCell(1);
		if (record.getR78_note_holders() != null) {
			cell1.setCellValue(record.getR78_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR78_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR78_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR78_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR78_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR78_issuance_date() != null) {
			cell6.setCellValue(record.getR78_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR78_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR78_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR78_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR78_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR78_amount() != null) {
			cell9.setCellValue(record.getR78_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR78_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR78_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R79 =====
		row = sheet.getRow(78);

		cell1 = row.createCell(1);
		if (record.getR79_note_holders() != null) {
			cell1.setCellValue(record.getR79_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR79_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR79_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR79_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR79_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR79_issuance_date() != null) {
			cell6.setCellValue(record.getR79_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR79_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR79_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR79_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR79_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR79_amount() != null) {
			cell9.setCellValue(record.getR79_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR79_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR79_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R80 =====
		row = sheet.getRow(79);

		cell1 = row.createCell(1);
		if (record.getR80_note_holders() != null) {
			cell1.setCellValue(record.getR80_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR80_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR80_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR80_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR80_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR80_issuance_date() != null) {
			cell6.setCellValue(record.getR80_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR80_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR80_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR80_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR80_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR80_amount() != null) {
			cell9.setCellValue(record.getR80_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR80_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR80_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R81 =====
		row = sheet.getRow(80);

		cell1 = row.createCell(1);
		if (record.getR81_note_holders() != null) {
			cell1.setCellValue(record.getR81_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR81_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR81_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR81_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR81_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR81_issuance_date() != null) {
			cell6.setCellValue(record.getR81_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR81_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR81_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR81_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR81_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR81_amount() != null) {
			cell9.setCellValue(record.getR81_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR81_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR81_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R82 =====
		row = sheet.getRow(81);

		cell1 = row.createCell(1);
		if (record.getR82_note_holders() != null) {
			cell1.setCellValue(record.getR82_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR82_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR82_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR82_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR82_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR82_issuance_date() != null) {
			cell6.setCellValue(record.getR82_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR82_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR82_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR82_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR82_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR82_amount() != null) {
			cell9.setCellValue(record.getR82_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR82_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR82_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R83 =====
		row = sheet.getRow(82);

		cell1 = row.createCell(1);
		if (record.getR83_note_holders() != null) {
			cell1.setCellValue(record.getR83_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR83_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR83_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR83_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR83_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR83_issuance_date() != null) {
			cell6.setCellValue(record.getR83_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR83_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR83_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR83_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR83_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR83_amount() != null) {
			cell9.setCellValue(record.getR83_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR83_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR83_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R84 =====
		row = sheet.getRow(83);

		cell1 = row.createCell(1);
		if (record.getR84_note_holders() != null) {
			cell1.setCellValue(record.getR84_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR84_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR84_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR84_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR84_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR84_issuance_date() != null) {
			cell6.setCellValue(record.getR84_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR84_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR84_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR84_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR84_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR84_amount() != null) {
			cell9.setCellValue(record.getR84_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR84_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR84_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R85 =====
		row = sheet.getRow(84);

		cell1 = row.createCell(1);
		if (record.getR85_note_holders() != null) {
			cell1.setCellValue(record.getR85_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR85_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR85_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR85_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR85_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR85_issuance_date() != null) {
			cell6.setCellValue(record.getR85_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR85_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR85_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR85_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR85_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR85_amount() != null) {
			cell9.setCellValue(record.getR85_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR85_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR85_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R86 =====
		row = sheet.getRow(85);

		cell1 = row.createCell(1);
		if (record.getR86_note_holders() != null) {
			cell1.setCellValue(record.getR86_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR86_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR86_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR86_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR86_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR86_issuance_date() != null) {
			cell6.setCellValue(record.getR86_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR86_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR86_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR86_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR86_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR86_amount() != null) {
			cell9.setCellValue(record.getR86_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR86_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR86_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R87 =====
		row = sheet.getRow(86);

		cell1 = row.createCell(1);
		if (record.getR87_note_holders() != null) {
			cell1.setCellValue(record.getR87_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR87_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR87_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR87_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR87_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR87_issuance_date() != null) {
			cell6.setCellValue(record.getR87_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR87_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR87_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR87_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR87_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR87_amount() != null) {
			cell9.setCellValue(record.getR87_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR87_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR87_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R88 =====
		row = sheet.getRow(87);

		cell1 = row.createCell(1);
		if (record.getR88_note_holders() != null) {
			cell1.setCellValue(record.getR88_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR88_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR88_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR88_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR88_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR88_issuance_date() != null) {
			cell6.setCellValue(record.getR88_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR88_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR88_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR88_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR88_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR88_amount() != null) {
			cell9.setCellValue(record.getR88_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR88_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR88_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R89 =====
		row = sheet.getRow(88);

		cell1 = row.createCell(1);
		if (record.getR89_note_holders() != null) {
			cell1.setCellValue(record.getR89_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR89_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR89_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR89_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR89_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR89_issuance_date() != null) {
			cell6.setCellValue(record.getR89_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR89_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR89_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR89_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR89_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR89_amount() != null) {
			cell9.setCellValue(record.getR89_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR89_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR89_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R90 =====
		row = sheet.getRow(89);

		cell1 = row.createCell(1);
		if (record.getR90_note_holders() != null) {
			cell1.setCellValue(record.getR90_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR90_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR90_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR90_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR90_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR90_issuance_date() != null) {
			cell6.setCellValue(record.getR90_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR90_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR90_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR90_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR90_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR90_amount() != null) {
			cell9.setCellValue(record.getR90_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR90_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR90_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R91 =====
		row = sheet.getRow(90);

		cell1 = row.createCell(1);
		if (record.getR91_note_holders() != null) {
			cell1.setCellValue(record.getR91_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR91_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR91_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR91_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR91_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR91_issuance_date() != null) {
			cell6.setCellValue(record.getR91_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR91_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR91_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR91_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR91_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR91_amount() != null) {
			cell9.setCellValue(record.getR91_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR91_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR91_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R92 =====
		row = sheet.getRow(91);

		cell1 = row.createCell(1);
		if (record.getR92_note_holders() != null) {
			cell1.setCellValue(record.getR92_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR92_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR92_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR92_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR92_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR92_issuance_date() != null) {
			cell6.setCellValue(record.getR92_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR92_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR92_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR92_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR92_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR92_amount() != null) {
			cell9.setCellValue(record.getR92_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR92_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR92_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R93 =====
		row = sheet.getRow(92);

		cell1 = row.createCell(1);
		if (record.getR93_note_holders() != null) {
			cell1.setCellValue(record.getR93_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR93_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR93_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR93_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR93_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR93_issuance_date() != null) {
			cell6.setCellValue(record.getR93_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR93_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR93_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR93_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR93_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR93_amount() != null) {
			cell9.setCellValue(record.getR93_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR93_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR93_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R94 =====
		row = sheet.getRow(93);

		cell1 = row.createCell(1);
		if (record.getR94_note_holders() != null) {
			cell1.setCellValue(record.getR94_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR94_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR94_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR94_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR94_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR94_issuance_date() != null) {
			cell6.setCellValue(record.getR94_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR94_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR94_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR94_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR94_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR94_amount() != null) {
			cell9.setCellValue(record.getR94_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR94_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR94_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R95 =====
		row = sheet.getRow(94);

		cell1 = row.createCell(1);
		if (record.getR95_note_holders() != null) {
			cell1.setCellValue(record.getR95_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR95_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR95_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR95_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR95_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR95_issuance_date() != null) {
			cell6.setCellValue(record.getR95_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR95_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR95_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR95_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR95_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR95_amount() != null) {
			cell9.setCellValue(record.getR95_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR95_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR95_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R96 =====
		row = sheet.getRow(95);

		cell1 = row.createCell(1);
		if (record.getR96_note_holders() != null) {
			cell1.setCellValue(record.getR96_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR96_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR96_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR96_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR96_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR96_issuance_date() != null) {
			cell6.setCellValue(record.getR96_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR96_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR96_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR96_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR96_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR96_amount() != null) {
			cell9.setCellValue(record.getR96_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR96_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR96_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R97 =====
		row = sheet.getRow(96);

		cell1 = row.createCell(1);
		if (record.getR97_note_holders() != null) {
			cell1.setCellValue(record.getR97_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR97_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR97_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR97_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR97_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR97_issuance_date() != null) {
			cell6.setCellValue(record.getR97_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR97_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR97_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR97_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR97_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR97_amount() != null) {
			cell9.setCellValue(record.getR97_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR97_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR97_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R98 =====
		row = sheet.getRow(97);

		cell1 = row.createCell(1);
		if (record.getR98_note_holders() != null) {
			cell1.setCellValue(record.getR98_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR98_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR98_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR98_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR98_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR98_issuance_date() != null) {
			cell6.setCellValue(record.getR98_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR98_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR98_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR98_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR98_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR98_amount() != null) {
			cell9.setCellValue(record.getR98_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR98_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR98_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R99 =====
		row = sheet.getRow(98);

		cell1 = row.createCell(1);
		if (record.getR99_note_holders() != null) {
			cell1.setCellValue(record.getR99_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR99_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR99_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR99_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR99_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR99_issuance_date() != null) {
			cell6.setCellValue(record.getR99_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR99_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR99_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR99_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR99_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR99_amount() != null) {
			cell9.setCellValue(record.getR99_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR99_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR99_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R100 =====
		row = sheet.getRow(99);

		cell1 = row.createCell(1);
		if (record.getR100_note_holders() != null) {
			cell1.setCellValue(record.getR100_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record.getR100_name_of_instrument_programe() != null) {
			cell2.setCellValue(record.getR100_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record.getR100_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record.getR100_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record.getR100_issuance_date() != null) {
			cell6.setCellValue(record.getR100_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record.getR100_contractual_maturity_date() != null) {
			cell7.setCellValue(record.getR100_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record.getR100_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record.getR100_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record.getR100_amount() != null) {
			cell9.setCellValue(record.getR100_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record.getR100_amount_derecognised_p000() != null) {
			cell10.setCellValue(record.getR100_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

	}

	private void archivalpopulateEntity2Data(Sheet sheet, M_CA5_Archival_Summary_Entity2 record1, CellStyle textStyle,
			CellStyle numberStyle, CellStyle dateStyle) {
		Cell cell1, cell2, cell3, cell4, cell5, cell6, cell7, cell8, cell9, cell10;
		CellStyle originalStyle;

		Row row = sheet.getRow(100);
		// --- R101 ---
		// Column C
		cell1 = row.createCell(1);
		if (record1.getR101_note_holders() != null) {
			cell1.setCellValue(record1.getR101_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR101_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR101_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR101_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR101_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR101_issuance_date() != null) {
			cell6.setCellValue(record1.getR101_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR101_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR101_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR101_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR101_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR101_amount() != null) {
			cell9.setCellValue(record1.getR101_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR101_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR101_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R102 =====
		row = sheet.getRow(101);

		cell1 = row.createCell(1);
		if (record1.getR102_note_holders() != null) {
			cell1.setCellValue(record1.getR102_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR102_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR102_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR102_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR102_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR102_issuance_date() != null) {
			cell6.setCellValue(record1.getR102_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR102_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR102_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR102_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR102_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR102_amount() != null) {
			cell9.setCellValue(record1.getR102_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR102_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR102_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R103 =====
		row = sheet.getRow(102);

		cell1 = row.createCell(1);
		if (record1.getR103_note_holders() != null) {
			cell1.setCellValue(record1.getR103_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR103_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR103_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR103_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR103_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR103_issuance_date() != null) {
			cell6.setCellValue(record1.getR103_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR103_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR103_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR103_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR103_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR103_amount() != null) {
			cell9.setCellValue(record1.getR103_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR103_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR103_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R104 =====
		row = sheet.getRow(103);

		cell1 = row.createCell(1);
		if (record1.getR104_note_holders() != null) {
			cell1.setCellValue(record1.getR104_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR104_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR104_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR104_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR104_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR104_issuance_date() != null) {
			cell6.setCellValue(record1.getR104_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR104_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR104_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR104_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR104_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR104_amount() != null) {
			cell9.setCellValue(record1.getR104_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR104_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR104_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R105 =====
		row = sheet.getRow(104);

		cell1 = row.createCell(1);
		if (record1.getR105_note_holders() != null) {
			cell1.setCellValue(record1.getR105_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR105_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR105_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR105_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR105_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR105_issuance_date() != null) {
			cell6.setCellValue(record1.getR105_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR105_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR105_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR105_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR105_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR105_amount() != null) {
			cell9.setCellValue(record1.getR105_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR105_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR105_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R106 =====
		row = sheet.getRow(105);

		cell1 = row.createCell(1);
		if (record1.getR106_note_holders() != null) {
			cell1.setCellValue(record1.getR106_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR106_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR106_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR106_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR106_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR106_issuance_date() != null) {
			cell6.setCellValue(record1.getR106_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR106_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR106_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR106_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR106_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR106_amount() != null) {
			cell9.setCellValue(record1.getR106_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR106_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR106_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R107 =====
		row = sheet.getRow(106);

		cell1 = row.createCell(1);
		if (record1.getR107_note_holders() != null) {
			cell1.setCellValue(record1.getR107_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR107_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR107_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR107_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR107_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR107_issuance_date() != null) {
			cell6.setCellValue(record1.getR107_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR107_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR107_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR107_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR107_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR107_amount() != null) {
			cell9.setCellValue(record1.getR107_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR107_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR107_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R108 =====
		row = sheet.getRow(107);

		cell1 = row.createCell(1);
		if (record1.getR108_note_holders() != null) {
			cell1.setCellValue(record1.getR108_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR108_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR108_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR108_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR108_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR108_issuance_date() != null) {
			cell6.setCellValue(record1.getR108_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR108_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR108_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR108_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR108_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR108_amount() != null) {
			cell9.setCellValue(record1.getR108_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR108_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR108_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R109 =====
		row = sheet.getRow(108);

		cell1 = row.createCell(1);
		if (record1.getR109_note_holders() != null) {
			cell1.setCellValue(record1.getR109_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR109_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR109_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR109_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR109_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR109_issuance_date() != null) {
			cell6.setCellValue(record1.getR109_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR109_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR109_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR109_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR109_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR109_amount() != null) {
			cell9.setCellValue(record1.getR109_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR109_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR109_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R110 =====
		row = sheet.getRow(109);

		cell1 = row.createCell(1);
		if (record1.getR110_note_holders() != null) {
			cell1.setCellValue(record1.getR110_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR110_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR110_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR110_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR110_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR110_issuance_date() != null) {
			cell6.setCellValue(record1.getR110_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR110_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR110_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR110_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR110_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR110_amount() != null) {
			cell9.setCellValue(record1.getR110_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR110_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR110_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R111 =====
		row = sheet.getRow(110);

		cell1 = row.createCell(1);
		if (record1.getR111_note_holders() != null) {
			cell1.setCellValue(record1.getR111_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR111_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR111_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR111_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR111_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR111_issuance_date() != null) {
			cell6.setCellValue(record1.getR111_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR111_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR111_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR111_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR111_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR111_amount() != null) {
			cell9.setCellValue(record1.getR111_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR111_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR111_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R112 =====
		row = sheet.getRow(111);

		cell1 = row.createCell(1);
		if (record1.getR112_note_holders() != null) {
			cell1.setCellValue(record1.getR112_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR112_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR112_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR112_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR112_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR112_issuance_date() != null) {
			cell6.setCellValue(record1.getR112_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR112_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR112_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR112_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR112_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR112_amount() != null) {
			cell9.setCellValue(record1.getR112_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR112_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR112_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R113 =====
		row = sheet.getRow(112);

		cell1 = row.createCell(1);
		if (record1.getR113_note_holders() != null) {
			cell1.setCellValue(record1.getR113_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR113_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR113_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR113_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR113_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR113_issuance_date() != null) {
			cell6.setCellValue(record1.getR113_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR113_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR113_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR113_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR113_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR113_amount() != null) {
			cell9.setCellValue(record1.getR113_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR113_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR113_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R114 =====
		row = sheet.getRow(113);

		cell1 = row.createCell(1);
		if (record1.getR114_note_holders() != null) {
			cell1.setCellValue(record1.getR114_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR114_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR114_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR114_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR114_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR114_issuance_date() != null) {
			cell6.setCellValue(record1.getR114_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR114_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR114_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR114_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR114_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR114_amount() != null) {
			cell9.setCellValue(record1.getR114_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR114_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR114_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R115 =====
		row = sheet.getRow(114);

		cell1 = row.createCell(1);
		if (record1.getR115_note_holders() != null) {
			cell1.setCellValue(record1.getR115_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR115_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR115_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR115_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR115_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR115_issuance_date() != null) {
			cell6.setCellValue(record1.getR115_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR115_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR115_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR115_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR115_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR115_amount() != null) {
			cell9.setCellValue(record1.getR115_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR115_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR115_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R116 =====
		row = sheet.getRow(115);

		cell1 = row.createCell(1);
		if (record1.getR116_note_holders() != null) {
			cell1.setCellValue(record1.getR116_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR116_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR116_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR116_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR116_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR116_issuance_date() != null) {
			cell6.setCellValue(record1.getR116_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR116_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR116_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR116_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR116_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR116_amount() != null) {
			cell9.setCellValue(record1.getR116_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR116_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR116_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R117 =====
		row = sheet.getRow(116);

		cell1 = row.createCell(1);
		if (record1.getR117_note_holders() != null) {
			cell1.setCellValue(record1.getR117_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR117_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR117_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR117_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR117_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR117_issuance_date() != null) {
			cell6.setCellValue(record1.getR117_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR117_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR117_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR117_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR117_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR117_amount() != null) {
			cell9.setCellValue(record1.getR117_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR117_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR117_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R118 =====
		row = sheet.getRow(117);

		cell1 = row.createCell(1);
		if (record1.getR118_note_holders() != null) {
			cell1.setCellValue(record1.getR118_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR118_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR118_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR118_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR118_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR118_issuance_date() != null) {
			cell6.setCellValue(record1.getR118_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR118_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR118_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR118_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR118_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR118_amount() != null) {
			cell9.setCellValue(record1.getR118_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR118_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR118_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R119 =====
		row = sheet.getRow(118);

		cell1 = row.createCell(1);
		if (record1.getR119_note_holders() != null) {
			cell1.setCellValue(record1.getR119_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR119_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR119_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR119_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR119_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR119_issuance_date() != null) {
			cell6.setCellValue(record1.getR119_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR119_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR119_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR119_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR119_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR119_amount() != null) {
			cell9.setCellValue(record1.getR119_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR119_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR119_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R120 =====
		row = sheet.getRow(119);

		cell1 = row.createCell(1);
		if (record1.getR120_note_holders() != null) {
			cell1.setCellValue(record1.getR120_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR120_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR120_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR120_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR120_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR120_issuance_date() != null) {
			cell6.setCellValue(record1.getR120_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR120_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR120_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR120_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR120_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR120_amount() != null) {
			cell9.setCellValue(record1.getR120_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR120_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR120_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R121 =====
		row = sheet.getRow(120);

		cell1 = row.createCell(1);
		if (record1.getR121_note_holders() != null) {
			cell1.setCellValue(record1.getR121_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR121_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR121_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR121_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR121_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR121_issuance_date() != null) {
			cell6.setCellValue(record1.getR121_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR121_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR121_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR121_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR121_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR121_amount() != null) {
			cell9.setCellValue(record1.getR121_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR121_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR121_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R122 =====
		row = sheet.getRow(121);

		cell1 = row.createCell(1);
		if (record1.getR122_note_holders() != null) {
			cell1.setCellValue(record1.getR122_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR122_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR122_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR122_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR122_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR122_issuance_date() != null) {
			cell6.setCellValue(record1.getR122_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR122_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR122_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR122_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR122_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR122_amount() != null) {
			cell9.setCellValue(record1.getR122_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR122_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR122_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R123 =====
		row = sheet.getRow(122);

		cell1 = row.createCell(1);
		if (record1.getR123_note_holders() != null) {
			cell1.setCellValue(record1.getR123_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR123_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR123_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR123_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR123_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR123_issuance_date() != null) {
			cell6.setCellValue(record1.getR123_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR123_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR123_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR123_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR123_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR123_amount() != null) {
			cell9.setCellValue(record1.getR123_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR123_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR123_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R124 =====
		row = sheet.getRow(123);

		cell1 = row.createCell(1);
		if (record1.getR124_note_holders() != null) {
			cell1.setCellValue(record1.getR124_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR124_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR124_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR124_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR124_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR124_issuance_date() != null) {
			cell6.setCellValue(record1.getR124_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR124_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR124_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR124_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR124_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR124_amount() != null) {
			cell9.setCellValue(record1.getR124_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR124_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR124_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R125 =====
		row = sheet.getRow(124);

		cell1 = row.createCell(1);
		if (record1.getR125_note_holders() != null) {
			cell1.setCellValue(record1.getR125_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR125_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR125_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR125_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR125_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR125_issuance_date() != null) {
			cell6.setCellValue(record1.getR125_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR125_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR125_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR125_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR125_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR125_amount() != null) {
			cell9.setCellValue(record1.getR125_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR125_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR125_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R126 =====
		row = sheet.getRow(125);

		cell1 = row.createCell(1);
		if (record1.getR126_note_holders() != null) {
			cell1.setCellValue(record1.getR126_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR126_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR126_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR126_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR126_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR126_issuance_date() != null) {
			cell6.setCellValue(record1.getR126_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR126_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR126_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR126_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR126_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR126_amount() != null) {
			cell9.setCellValue(record1.getR126_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR126_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR126_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R127 =====
		row = sheet.getRow(126);

		cell1 = row.createCell(1);
		if (record1.getR127_note_holders() != null) {
			cell1.setCellValue(record1.getR127_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR127_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR127_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR127_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR127_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR127_issuance_date() != null) {
			cell6.setCellValue(record1.getR127_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR127_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR127_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR127_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR127_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR127_amount() != null) {
			cell9.setCellValue(record1.getR127_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR127_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR127_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R128 =====
		row = sheet.getRow(127);

		cell1 = row.createCell(1);
		if (record1.getR128_note_holders() != null) {
			cell1.setCellValue(record1.getR128_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR128_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR128_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR128_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR128_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR128_issuance_date() != null) {
			cell6.setCellValue(record1.getR128_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR128_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR128_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR128_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR128_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR128_amount() != null) {
			cell9.setCellValue(record1.getR128_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR128_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR128_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R129 =====
		row = sheet.getRow(128);

		cell1 = row.createCell(1);
		if (record1.getR129_note_holders() != null) {
			cell1.setCellValue(record1.getR129_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR129_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR129_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR129_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR129_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR129_issuance_date() != null) {
			cell6.setCellValue(record1.getR129_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR129_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR129_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR129_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR129_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR129_amount() != null) {
			cell9.setCellValue(record1.getR129_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR129_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR129_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R130 =====
		row = sheet.getRow(129);

		cell1 = row.createCell(1);
		if (record1.getR130_note_holders() != null) {
			cell1.setCellValue(record1.getR130_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR130_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR130_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR130_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR130_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR130_issuance_date() != null) {
			cell6.setCellValue(record1.getR130_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR130_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR130_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR130_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR130_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR130_amount() != null) {
			cell9.setCellValue(record1.getR130_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR130_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR130_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R131 =====
		row = sheet.getRow(130);

		cell1 = row.createCell(1);
		if (record1.getR131_note_holders() != null) {
			cell1.setCellValue(record1.getR131_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR131_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR131_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR131_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR131_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR131_issuance_date() != null) {
			cell6.setCellValue(record1.getR131_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR131_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR131_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR131_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR131_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR131_amount() != null) {
			cell9.setCellValue(record1.getR131_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR131_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR131_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R132 =====
		row = sheet.getRow(131);

		cell1 = row.createCell(1);
		if (record1.getR132_note_holders() != null) {
			cell1.setCellValue(record1.getR132_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR132_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR132_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR132_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR132_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR132_issuance_date() != null) {
			cell6.setCellValue(record1.getR132_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR132_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR132_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR132_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR132_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR132_amount() != null) {
			cell9.setCellValue(record1.getR132_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR132_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR132_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R133 =====
		row = sheet.getRow(132);

		cell1 = row.createCell(1);
		if (record1.getR133_note_holders() != null) {
			cell1.setCellValue(record1.getR133_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR133_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR133_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR133_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR133_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR133_issuance_date() != null) {
			cell6.setCellValue(record1.getR133_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR133_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR133_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR133_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR133_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR133_amount() != null) {
			cell9.setCellValue(record1.getR133_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR133_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR133_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R134 =====
		row = sheet.getRow(133);

		cell1 = row.createCell(1);
		if (record1.getR134_note_holders() != null) {
			cell1.setCellValue(record1.getR134_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR134_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR134_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR134_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR134_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR134_issuance_date() != null) {
			cell6.setCellValue(record1.getR134_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR134_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR134_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR134_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR134_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR134_amount() != null) {
			cell9.setCellValue(record1.getR134_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR134_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR134_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R135 =====
		row = sheet.getRow(134);

		cell1 = row.createCell(1);
		if (record1.getR135_note_holders() != null) {
			cell1.setCellValue(record1.getR135_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR135_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR135_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR135_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR135_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR135_issuance_date() != null) {
			cell6.setCellValue(record1.getR135_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR135_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR135_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR135_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR135_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR135_amount() != null) {
			cell9.setCellValue(record1.getR135_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR135_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR135_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R136 =====
		row = sheet.getRow(135);

		cell1 = row.createCell(1);
		if (record1.getR136_note_holders() != null) {
			cell1.setCellValue(record1.getR136_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR136_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR136_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR136_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR136_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR136_issuance_date() != null) {
			cell6.setCellValue(record1.getR136_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR136_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR136_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR136_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR136_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR136_amount() != null) {
			cell9.setCellValue(record1.getR136_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR136_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR136_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R137 =====
		row = sheet.getRow(136);

		cell1 = row.createCell(1);
		if (record1.getR137_note_holders() != null) {
			cell1.setCellValue(record1.getR137_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR137_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR137_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR137_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR137_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR137_issuance_date() != null) {
			cell6.setCellValue(record1.getR137_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR137_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR137_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR137_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR137_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR137_amount() != null) {
			cell9.setCellValue(record1.getR137_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR137_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR137_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R138 =====
		row = sheet.getRow(137);

		cell1 = row.createCell(1);
		if (record1.getR138_note_holders() != null) {
			cell1.setCellValue(record1.getR138_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR138_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR138_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR138_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR138_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR138_issuance_date() != null) {
			cell6.setCellValue(record1.getR138_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR138_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR138_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR138_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR138_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR138_amount() != null) {
			cell9.setCellValue(record1.getR138_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR138_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR138_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R139 =====
		row = sheet.getRow(138);

		cell1 = row.createCell(1);
		if (record1.getR139_note_holders() != null) {
			cell1.setCellValue(record1.getR139_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR139_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR139_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR139_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR139_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR139_issuance_date() != null) {
			cell6.setCellValue(record1.getR139_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR139_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR139_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR139_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR139_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR139_amount() != null) {
			cell9.setCellValue(record1.getR139_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR139_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR139_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R140 =====
		row = sheet.getRow(139);

		cell1 = row.createCell(1);
		if (record1.getR140_note_holders() != null) {
			cell1.setCellValue(record1.getR140_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR140_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR140_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR140_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR140_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR140_issuance_date() != null) {
			cell6.setCellValue(record1.getR140_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR140_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR140_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR140_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR140_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR140_amount() != null) {
			cell9.setCellValue(record1.getR140_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR140_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR140_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R141 =====
		row = sheet.getRow(140);

		cell1 = row.createCell(1);
		if (record1.getR141_note_holders() != null) {
			cell1.setCellValue(record1.getR141_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR141_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR141_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR141_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR141_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR141_issuance_date() != null) {
			cell6.setCellValue(record1.getR141_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR141_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR141_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR141_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR141_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR141_amount() != null) {
			cell9.setCellValue(record1.getR141_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR141_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR141_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R142 =====
		row = sheet.getRow(141);

		cell1 = row.createCell(1);
		if (record1.getR142_note_holders() != null) {
			cell1.setCellValue(record1.getR142_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR142_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR142_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR142_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR142_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR142_issuance_date() != null) {
			cell6.setCellValue(record1.getR142_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR142_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR142_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR142_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR142_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR142_amount() != null) {
			cell9.setCellValue(record1.getR142_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR142_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR142_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R143 =====
		row = sheet.getRow(142);

		cell1 = row.createCell(1);
		if (record1.getR143_note_holders() != null) {
			cell1.setCellValue(record1.getR143_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR143_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR143_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR143_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR143_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR143_issuance_date() != null) {
			cell6.setCellValue(record1.getR143_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR143_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR143_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR143_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR143_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR143_amount() != null) {
			cell9.setCellValue(record1.getR143_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR143_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR143_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R144 =====
		row = sheet.getRow(143);

		cell1 = row.createCell(1);
		if (record1.getR144_note_holders() != null) {
			cell1.setCellValue(record1.getR144_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR144_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR144_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR144_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR144_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR144_issuance_date() != null) {
			cell6.setCellValue(record1.getR144_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR144_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR144_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR144_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR144_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR144_amount() != null) {
			cell9.setCellValue(record1.getR144_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR144_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR144_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R145 =====
		row = sheet.getRow(144);

		cell1 = row.createCell(1);
		if (record1.getR145_note_holders() != null) {
			cell1.setCellValue(record1.getR145_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR145_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR145_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR145_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR145_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR145_issuance_date() != null) {
			cell6.setCellValue(record1.getR145_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR145_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR145_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR145_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR145_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR145_amount() != null) {
			cell9.setCellValue(record1.getR145_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR145_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR145_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R146 =====
		row = sheet.getRow(145);

		cell1 = row.createCell(1);
		if (record1.getR146_note_holders() != null) {
			cell1.setCellValue(record1.getR146_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR146_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR146_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR146_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR146_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR146_issuance_date() != null) {
			cell6.setCellValue(record1.getR146_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR146_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR146_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR146_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR146_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR146_amount() != null) {
			cell9.setCellValue(record1.getR146_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR146_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR146_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R147 =====
		row = sheet.getRow(146);

		cell1 = row.createCell(1);
		if (record1.getR147_note_holders() != null) {
			cell1.setCellValue(record1.getR147_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR147_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR147_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR147_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR147_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR147_issuance_date() != null) {
			cell6.setCellValue(record1.getR147_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR147_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR147_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR147_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR147_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR147_amount() != null) {
			cell9.setCellValue(record1.getR147_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR147_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR147_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R148 =====
		row = sheet.getRow(147);

		cell1 = row.createCell(1);
		if (record1.getR148_note_holders() != null) {
			cell1.setCellValue(record1.getR148_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR148_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR148_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR148_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR148_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR148_issuance_date() != null) {
			cell6.setCellValue(record1.getR148_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR148_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR148_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR148_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR148_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR148_amount() != null) {
			cell9.setCellValue(record1.getR148_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR148_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR148_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

		// ===== Row R149 =====
		row = sheet.getRow(148);

		cell1 = row.createCell(1);
		if (record1.getR149_note_holders() != null) {
			cell1.setCellValue(record1.getR149_note_holders());
			cell1.setCellStyle(numberStyle);
		} else {
			cell1.setCellValue("");
			cell1.setCellStyle(textStyle);
		}

		cell2 = row.createCell(2);
		if (record1.getR149_name_of_instrument_programe() != null) {
			cell2.setCellValue(record1.getR149_name_of_instrument_programe());
			cell2.setCellStyle(textStyle);
		} else {
			cell2.setCellValue("");
			cell2.setCellStyle(textStyle);
		}

		cell5 = row.createCell(5);
		if (record1.getR149_issuing_entity_if_issued_throughan_spv() != null) {
			cell5.setCellValue(record1.getR149_issuing_entity_if_issued_throughan_spv());
			cell5.setCellStyle(textStyle);
		} else {
			cell5.setCellValue("");
			cell5.setCellStyle(textStyle);
		}

		cell6 = row.createCell(6);
		if (record1.getR149_issuance_date() != null) {
			cell6.setCellValue(record1.getR149_issuance_date());
			cell6.setCellStyle(dateStyle);
		} else {
			cell6.setCellValue("");
			cell6.setCellStyle(textStyle);
		}

		cell7 = row.createCell(7);
		if (record1.getR149_contractual_maturity_date() != null) {
			cell7.setCellValue(record1.getR149_contractual_maturity_date());
			cell7.setCellStyle(dateStyle);
		} else {
			cell7.setCellValue("");
			cell7.setCellStyle(textStyle);
		}

		cell8 = row.createCell(8);
		if (record1.getR149_effective_maturity_date_if_date() != null) {
			cell8.setCellValue(record1.getR149_effective_maturity_date_if_date());
			cell8.setCellStyle(dateStyle);
		} else {
			cell8.setCellValue("");
			cell8.setCellStyle(textStyle);
		}

		cell9 = row.createCell(9);
		if (record1.getR149_amount() != null) {
			cell9.setCellValue(record1.getR149_amount().doubleValue());
			cell9.setCellStyle(numberStyle);
		} else {
			cell9.setCellValue("");
			cell9.setCellStyle(textStyle);
		}

		cell10 = row.createCell(10);
		if (record1.getR149_amount_derecognised_p000() != null) {
			cell10.setCellValue(record1.getR149_amount_derecognised_p000().doubleValue());
			cell10.setCellStyle(numberStyle);
		} else {
			cell10.setCellValue("");
			cell10.setCellStyle(textStyle);
		}

	}

	////////////////////////////////////////// RESUBMISSION///////////////////////////////////////////////////////////////////
/// Report Date | Report Version | Domain
/// RESUB VIEW

	public List<Object[]> getM_CA5Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_CA5_Archival_Summary_Entity1> latestArchivalList = M_CA5_Archival_Summary_Repo1
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA5_Archival_Summary_Entity1 entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReportDate(), entity.getReportVersion() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetchingM_LARADV Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_CA5Archival() {
		List<Object[]> archivalList = new ArrayList<>();
		try {
			List<M_CA5_Archival_Summary_Entity1> latestArchivalList = M_CA5_Archival_Summary_Repo1
					.getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_CA5_Archival_Summary_Entity1 entity : latestArchivalList) {
					archivalList.add(new Object[] { entity.getReportDate(), entity.getReportVersion() });
				}
				System.out.println("Fetched " + archivalList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetchingM_LARADV Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return archivalList;
	}

	public void updateReportReSub(M_CA5_Summary_Entity1 updatedEntity1, M_CA5_Summary_Entity2 updatedEntity2) {

		System.out.println("Came t M_CA5 Resub Service");
		System.out.println("Report Date: " + updatedEntity1.getReportDate());

		Date reportDate = updatedEntity1.getReportDate();
		int newVersion = 1;

		try {
			// 🔹 Fetch the latest archival version for this report date from Entity1
			Optional<M_CA5_Archival_Summary_Entity1> latestArchivalOpt1 = M_CA5_Archival_Summary_Repo1
					.getLatestArchivalVersionByDate(reportDate);

			if (latestArchivalOpt1.isPresent()) {
				M_CA5_Archival_Summary_Entity1 latestArchival = latestArchivalOpt1.get();
				try {
					newVersion = Integer.parseInt(latestArchival.getReportVersion()) + 1;
				} catch (NumberFormatException e) {
					System.err.println("Invalid version format. Defaulting to version 1");
					newVersion = 1;
				}
			} else {
				System.out.println("No previous archival found for date: " + reportDate);
			}

			// 🔹 Prevent duplicate version number in Repo1
			boolean exists = M_CA5_Archival_Summary_Repo1
					.findByReportDateAndReportVersion(reportDate, String.valueOf(newVersion)).isPresent();

			if (exists) {
				throw new RuntimeException("⚠ Version " + newVersion + " already exists for report date " + reportDate);
			}

			// Copy data from summary to archival entities for all 3 entities
			M_CA5_Archival_Summary_Entity1 archivalEntity1 = new M_CA5_Archival_Summary_Entity1();
			M_CA5_Archival_Summary_Entity2 archivalEntity2 = new M_CA5_Archival_Summary_Entity2();

			org.springframework.beans.BeanUtils.copyProperties(updatedEntity1, archivalEntity1);
			org.springframework.beans.BeanUtils.copyProperties(updatedEntity2, archivalEntity2);

			// Set common fields
			Date now = new Date();
			archivalEntity1.setReportDate(reportDate);
			archivalEntity2.setReportDate(reportDate);

			archivalEntity1.setReportVersion(String.valueOf(newVersion));
			archivalEntity2.setReportVersion(String.valueOf(newVersion));

			archivalEntity1.setReportResubDate(now);
			archivalEntity2.setReportResubDate(now);

			System.out.println("Saving new archival version: " + newVersion);

			// Save to all three archival repositories
			M_CA5_Archival_Summary_Repo1.save(archivalEntity1);
			M_CA5_Archival_Summary_Repo2.save(archivalEntity2);

			System.out.println("Saved archival version successfully: " + newVersion);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while creatingM_LARADV archival resubmission record", e);
		}
	}

	/*
	 * public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String
	 * todate, String currency, String dtltype, String type, String version) { try {
	 * logger.info("Generating Excel for BRRS_M_CA5 ARCHIVAL Details...");
	 * System.out.println("came to Detail download service"); if
	 * (type.equals("ARCHIVAL") & version != null) {
	 * 
	 * } XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet sheet =
	 * workbook.createSheet("MLA4Detail");
	 * 
	 * // Common border style BorderStyle border = BorderStyle.THIN;
	 * 
	 * // Header style (left aligned) CellStyle headerStyle =
	 * workbook.createCellStyle(); Font headerFont = workbook.createFont();
	 * headerFont.setBold(true); headerFont.setFontHeightInPoints((short) 10);
	 * headerStyle.setFont(headerFont);
	 * headerStyle.setAlignment(HorizontalAlignment.LEFT);
	 * headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	 * headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	 * headerStyle.setBorderTop(border); headerStyle.setBorderBottom(border);
	 * headerStyle.setBorderLeft(border); headerStyle.setBorderRight(border);
	 * 
	 * // Right-aligned header style for ACCT BALANCE CellStyle
	 * rightAlignedHeaderStyle = workbook.createCellStyle();
	 * rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
	 * rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * 
	 * // Default data style (left aligned) CellStyle dataStyle =
	 * workbook.createCellStyle(); dataStyle.setAlignment(HorizontalAlignment.LEFT);
	 * dataStyle.setBorderTop(border); dataStyle.setBorderBottom(border);
	 * dataStyle.setBorderLeft(border); dataStyle.setBorderRight(border);
	 * 
	 * // ACCT BALANCE style (right aligned with 3 decimals) CellStyle balanceStyle
	 * = workbook.createCellStyle();
	 * balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
	 * balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
	 * balanceStyle.setBorderTop(border); balanceStyle.setBorderBottom(border);
	 * balanceStyle.setBorderLeft(border); balanceStyle.setBorderRight(border);
	 * 
	 * // Header row String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME",
	 * "ACCT BALANCE", "ROWID", "COLUMNID", "REPORT_DATE" };
	 * 
	 * XSSFRow headerRow = sheet.createRow(0); for (int i = 0; i < headers.length;
	 * i++) { Cell cell = headerRow.createCell(i); cell.setCellValue(headers[i]);
	 * 
	 * if (i == 3) { // ACCT BALANCE cell.setCellStyle(rightAlignedHeaderStyle); }
	 * else { cell.setCellStyle(headerStyle); }
	 * 
	 * sheet.setColumnWidth(i, 5000); }
	 * 
	 * // Get data Date parsedToDate = new
	 * SimpleDateFormat("dd/MM/yyyy").parse(todate);
	 * List<M_CA5_Archival_Detail_Entity> reportData = M_CA5_Archival_Detail_Repo
	 * .getdatabydateList(parsedToDate, version);
	 * 
	 * if (reportData != null && !reportData.isEmpty()) { int rowIndex = 1; for
	 * (M_CA5_Archival_Detail_Entity item : reportData) { XSSFRow row =
	 * sheet.createRow(rowIndex++);
	 * 
	 * row.createCell(0).setCellValue(item.getCustId());
	 * row.createCell(1).setCellValue(item.getAcctNumber());
	 * row.createCell(2).setCellValue(item.getAcctName());
	 * 
	 * // ACCT BALANCE (right aligned, 3 decimal places) Cell balanceCell =
	 * row.createCell(3); if (item.getAcctBalanceInPula() != null) {
	 * balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue()); } else {
	 * balanceCell.setCellValue(0.000); } balanceCell.setCellStyle(balanceStyle);
	 * 
	 * row.createCell(4).setCellValue(item.getRowId());
	 * row.createCell(5).setCellValue(item.getColumnId()); row.createCell(6)
	 * .setCellValue(item.getReportDate() != null ? new
	 * SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : "");
	 * 
	 * // Apply data style for all other cells for (int j = 0; j < 7; j++) { if (j
	 * != 3) { row.getCell(j).setCellStyle(dataStyle); } } } } else {
	 * logger.info("No data found for BRRS_M_CA5 — only header will be written."); }
	 * 
	 * // Write to byte[] ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); workbook.close();
	 * 
	 * logger.info("Excel generation completed with {} row(s).", reportData != null
	 * ? reportData.size() : 0); return bos.toByteArray();
	 * 
	 * } catch (Exception e) { logger.error("Error generating BRRS_M_CA5Excel", e);
	 * return new byte[0]; } }
	 */

}
