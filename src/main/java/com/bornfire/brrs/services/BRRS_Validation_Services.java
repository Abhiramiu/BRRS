package com.bornfire.brrs.services;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.bornfire.brrs.entities.BRRSValidations;
import com.bornfire.brrs.entities.BRRSValidationsRepo;
import com.bornfire.brrs.entities.ValidationResponse;

@Service
@Transactional
@ConfigurationProperties("output")
public class BRRS_Validation_Services {

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	BRRSValidationsRepo brrsValidationsRepo;

	private static final Logger logger = LoggerFactory.getLogger(BRRS_Validation_Services.class);

	public ValidationResponse chkBRFValidations(BRRSValidations brrsValidations, String srl_no, String report_date)
			throws ParseException {
		logger.info("Entered chkBRFValidations method");
		logger.info("srl_no: {}", srl_no);
		logger.info("report_date: {}", report_date);

		ValidationResponse msg = new ValidationResponse();
		Date dateFormat = new SimpleDateFormat("dd/MM/yyyy").parse(report_date);
		String convertDate = new SimpleDateFormat("dd-MMM-yyyy").format(dateFormat);

		try {
			Optional<BRRSValidations> brfValidationOpt = brrsValidationsRepo.findById(srl_no);
			if (!brfValidationOpt.isPresent()) {
				logger.error("No BRRSValidation found for srl_no: {}", srl_no);
				msg.setGenID("0");
				msg.setStatus("Validation record not found");
				return msg;
			}

			BRRSValidations brfValidation = brfValidationOpt.get();
			String status = "";
			switch (srl_no) {
			case "1":
				int count = brrsValidationsRepo.getCheckSrlNo1(convertDate);
				if (count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SFINP1 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SFINP1 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "2":
				List<Object[]> resultMonthEnd = brrsValidationsRepo.getCheckSrlNo2(convertDate);
				if (!resultMonthEnd.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("MONTH END TOTAL IN ASSETS AND LIABILITIES ARE EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("MONTH END TOTAL IN ASSETS AND LIABILITIES ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "3":
				List<Object[]> resultAverage = brrsValidationsRepo.getCheckSrlNo3(convertDate);
				if (!resultAverage.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("AVERAGE TOTAL IN ASSETS AND LIABILITIES ARE EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("AVERAGE TOTAL IN ASSETS AND LIABILITIES ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "4":
				int countP2 = brrsValidationsRepo.getCheckSrlNo4(convertDate);
				if (countP2 > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SFINP1 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SFINP1 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "5":
				List<Object[]> resultMonthEndP2 = brrsValidationsRepo.getCheckSrlNo5(convertDate);
				if (!resultMonthEndP2.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("MONTH END TOTAL IN ASSETS AND LIABILITIES ARE EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("MONTH END TOTAL IN ASSETS AND LIABILITIES ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "6":
				List<Object[]> resultAverageP2 = brrsValidationsRepo.getCheckSrlNo6(convertDate);
				if (!resultAverageP2.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("AVERAGE TOTAL IN ASSETS AND LIABILITIES ARE EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("AVERAGE TOTAL IN ASSETS AND LIABILITIES ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "7":
				int LIQcount = brrsValidationsRepo.getCheckSrlNo7(convertDate);
				if (LIQcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_LIQ SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_LIQ SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "8":
				int SCIEcount = brrsValidationsRepo.getCheckSrlNo8(convertDate);
				if (SCIEcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SCI_E SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SCI_E SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "9":
				int IScount = brrsValidationsRepo.getCheckSrlNo9(convertDate);
				if (IScount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_IS SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_IS SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "10":
				int CA1count = brrsValidationsRepo.getCheckSrlNo10(convertDate);
				if (CA1count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_CA1 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_CA1 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "11":
				int CA2count = brrsValidationsRepo.getCheckSrlNo11(convertDate);
				if (CA2count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_CA2 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_CA2 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "12":
				int CA3count = brrsValidationsRepo.getCheckSrlNo12(convertDate);
				if (CA3count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_CA3 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_CA3 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "13":
				int CA4count = brrsValidationsRepo.getCheckSrlNo13(convertDate);
				if (CA4count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_CA4 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_CA4 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "14":
				int CA5count = brrsValidationsRepo.getCheckSrlNo14(convertDate);
				if (CA5count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_CA5 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_CA5 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "15":
				int CA6count = brrsValidationsRepo.getCheckSrlNo15(convertDate);
				if (CA6count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_CA6 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_CA6 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "16":
				int CA7count = brrsValidationsRepo.getCheckSrlNo16(convertDate);
				if (CA7count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_CA7 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_CA7 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "17":
				int M12Acount = brrsValidationsRepo.getCheckSrlNo17(convertDate);
				if (M12Acount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SRWA_12A SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SRWA_12A SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "18":
				int M12Bcount = brrsValidationsRepo.getCheckSrlNo18(convertDate);
				if (M12Bcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SRWA_12B SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SRWA_12B SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "19":
				int M12Ccount = brrsValidationsRepo.getCheckSrlNo19(convertDate);
				if (M12Ccount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SRWA_12C SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SRWA_12C SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "20":
				int M12Dcount = brrsValidationsRepo.getCheckSrlNo20(convertDate);
				if (M12Dcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SRWA_12D SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SRWA_12D SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "21":
				int M12Ecount = brrsValidationsRepo.getCheckSrlNo21(convertDate);
				if (M12Ecount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SRWA_12E SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SRWA_12E SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "22":
				int M12Fcount = brrsValidationsRepo.getCheckSrlNo22(convertDate);
				if (M12Fcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SRWA_12F SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SRWA_12F SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "23":
				int M12Gcount = brrsValidationsRepo.getCheckSrlNo23(convertDate);
				if (M12Gcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SRWA_12G SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SRWA_12G SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "24":
				int M12Hcount = brrsValidationsRepo.getCheckSrlNo24(convertDate);
				if (M12Hcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SRWA_12H SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SRWA_12H SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "25":
				int MOR1count = brrsValidationsRepo.getCheckSrlNo25(convertDate);
				if (MOR1count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_OR1 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_OR1 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "26":
				int MOR2count = brrsValidationsRepo.getCheckSrlNo26(convertDate);
				if (MOR2count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_OR2 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_OR2 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "27":
				int M_MRCcount = brrsValidationsRepo.getCheckSrlNo27(convertDate);
				if (M_MRCcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_MRC SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_MRC SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "28":
				int M_SIRcount = brrsValidationsRepo.getCheckSrlNo28(convertDate);
				if (M_SIRcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SIR SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SIR SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "29":
				int M_GMIRTcount = brrsValidationsRepo.getCheckSrlNo29(convertDate);
				if (M_GMIRTcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_GMIRT SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_GMIRT SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "30":
				int M_IRBcount = brrsValidationsRepo.getCheckSrlNo30(convertDate);
				if (M_IRBcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_IRB SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_IRB SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "31":
				int M_EPRcount = brrsValidationsRepo.getCheckSrlNo31(convertDate);
				if (M_EPRcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_EPR SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_EPR SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "32":
				int M_FXRcount = brrsValidationsRepo.getCheckSrlNo32(convertDate);
				if (M_FXRcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_FXR SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_FXR SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "33":
				int M_CRcount = brrsValidationsRepo.getCheckSrlNo33(convertDate);
				if (M_CRcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_CR SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_CR SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "34":
				int M_OPTRcount = brrsValidationsRepo.getCheckSrlNo34(convertDate);
				if (M_OPTRcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_OPTR SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_OPTR SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "35":
				int M_GALORcount = brrsValidationsRepo.getCheckSrlNo35(convertDate);
				if (M_GALORcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_GALOR SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_GALOR SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "36":
				int M_CALOCcount = brrsValidationsRepo.getCheckSrlNo36(convertDate);
				if (M_CALOCcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_CALOC SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_CALOC SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "37":
				int M_LA1count = brrsValidationsRepo.getCheckSrlNo37(convertDate);
				if (M_LA1count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_LA1 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_LA1 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "38":
				int M_LA2count = brrsValidationsRepo.getCheckSrlNo38(convertDate);
				if (M_LA2count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_LA2 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_LA2 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;
			case "39":
				int M_LA3count = brrsValidationsRepo.getCheckSrlNo39(convertDate);
				if (M_LA3count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_LA3 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_LA3 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "40":
				int M_LA4count = brrsValidationsRepo.getCheckSrlNo40(convertDate);
				if (M_LA4count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_LA4 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_LA4 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "41":
				int M_LA5count = brrsValidationsRepo.getCheckSrlNo41(convertDate);
				if (M_LA5count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_LA5 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_LA5 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "42":
				int M_PLLcount = brrsValidationsRepo.getCheckSrlNo42(convertDate);
				if (M_PLLcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_PLL SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_PLL SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "43":
				int M_PDcount = brrsValidationsRepo.getCheckSrlNo43(convertDate);
				if (M_PDcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_PD SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_PD SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "44":
				int M_I_S_CAcount = brrsValidationsRepo.getCheckSrlNo44(convertDate);
				if (M_I_S_CAcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_I_S_CA SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_I_S_CA SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "45":
				int M_SPcount = brrsValidationsRepo.getCheckSrlNo45(convertDate);
				if (M_SPcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SP SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SP SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "46":
				int M_GPcount = brrsValidationsRepo.getCheckSrlNo46(convertDate);
				if (M_GPcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_GP SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_GP SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "47":
				int M_TBScount = brrsValidationsRepo.getCheckSrlNo47(convertDate);
				if (M_TBScount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_TBS SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_TBS SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "48":
				int M_LIQGAPcount = brrsValidationsRepo.getCheckSrlNo48(convertDate);
				if (M_LIQGAPcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_LIQGAP SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_LIQGAP SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "49":
				int M_NOSVOScount = brrsValidationsRepo.getCheckSrlNo49(convertDate);
				if (M_NOSVOScount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_NOSVOS SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_NOSVOS SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;
			case "50":
				int M_AIDPcount = brrsValidationsRepo.getCheckSrlNo50(convertDate);
				if (M_AIDPcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_AIDP SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_AIDP SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;
			case "51":
				int M_DEP1count = brrsValidationsRepo.getCheckSrlNo51(convertDate);
				if (M_DEP1count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_DEP1 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_DEP1 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "52":
				int M_DEP2count = brrsValidationsRepo.getCheckSrlNo52(convertDate);
				if (M_DEP2count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_DEP2 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_DEP2 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "53":
				int M_DEP3count = brrsValidationsRepo.getCheckSrlNo53(convertDate);
				if (M_DEP3count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_DEP3 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_DEP3 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "54":
				int M_DEP4count = brrsValidationsRepo.getCheckSrlNo54(convertDate);
				if (M_DEP4count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_DEP4 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_DEP4 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "55":
				int M_OBcount = brrsValidationsRepo.getCheckSrlNo55(convertDate);
				if (M_OBcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_OB SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_OB SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "56":
				int M_BOPcount = brrsValidationsRepo.getCheckSrlNo56(convertDate);
				if (M_BOPcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_BOP SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_BOP SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "57":
				int M_INT_RATEScount = brrsValidationsRepo.getCheckSrlNo57(convertDate);
				if (M_INT_RATEScount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_INT_RATES SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_INT_RATES SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "58":
				int M_INT_RATES_FCAcount = brrsValidationsRepo.getCheckSrlNo58(convertDate);
				if (M_INT_RATES_FCAcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_INT_RATES_FCA SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_INT_RATES_FCA SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "59":
				int M_SECAcount = brrsValidationsRepo.getCheckSrlNo59(convertDate);
				if (M_SECAcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SECA SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SECA SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "60":
				int M_SECLcount = brrsValidationsRepo.getCheckSrlNo60(convertDate);
				if (M_SECLcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SECL SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SECL SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "61":
				int M_RPDcount = brrsValidationsRepo.getCheckSrlNo61(convertDate);
				if (M_RPDcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_RPD SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_RPD SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "62":
				int M_FAScount = brrsValidationsRepo.getCheckSrlNo62(convertDate);
				if (M_FAScount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_FAS SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_FAS SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "63":
				int M_SECcount = brrsValidationsRepo.getCheckSrlNo63(convertDate);
				if (M_SECcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_SEC SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_SEC SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "64":
				int UNCONS_INVESTcount = brrsValidationsRepo.getCheckSrlNo64(convertDate);
				if (UNCONS_INVESTcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("UNCONS_INVEST SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("UNCONS_INVEST SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "65":
				int Q_ATFcount = brrsValidationsRepo.getCheckSrlNo65(convertDate);
				if (Q_ATFcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("Q_ATF SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("Q_ATF SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "66":
				int Q_RLFA1count = brrsValidationsRepo.getCheckSrlNo66(convertDate);
				if (Q_RLFA1count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("Q_RLFA1 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("Q_RLFA1 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "67":
				int Q_RLFA2count = brrsValidationsRepo.getCheckSrlNo67(convertDate);
				if (Q_RLFA2count > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("Q_RLFA2 SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("Q_RLFA2 SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "68":
				int Q_SMME_LOANS_ADVANCEScount = brrsValidationsRepo.getCheckSrlNo68(convertDate);
				if (Q_SMME_LOANS_ADVANCEScount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("Q_SMME_LOANS_ADVANCES SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("Q_SMME_LOANS_ADVANCES SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "69":
				int Q_SMME_INTREST_INCOMEcount = brrsValidationsRepo.getCheckSrlNo69(convertDate);
				if (Q_SMME_INTREST_INCOMEcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("Q_SMME_INTREST_INCOME SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("Q_SMME_INTREST_INCOME SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "70":
				int Q_SMME_DEPcount = brrsValidationsRepo.getCheckSrlNo70(convertDate);
				if (Q_SMME_DEPcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("Q_SMME_DEP SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("Q_SMME_DEP SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "71":
				int Q_STAFFcount = brrsValidationsRepo.getCheckSrlNo71(convertDate);
				if (Q_STAFFcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("Q_STAFF SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("Q_STAFF SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "72":
				int Q_LARADVcount = brrsValidationsRepo.getCheckSrlNo72(convertDate);
				if (Q_LARADVcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("Q_LARADV SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("Q_LARADV SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "73":
				int Q_BRANCHNETcount = brrsValidationsRepo.getCheckSrlNo73(convertDate);
				if (Q_BRANCHNETcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("Q_BRANCHNET SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("Q_BRANCHNET SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "74":
				int M_PIcount = brrsValidationsRepo.getCheckSrlNo74(convertDate);
				if (M_PIcount > 0) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("M_PI SUMMARY TABLE HAVE VALUES");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("M_PI SUMMARY TABLE DOES NOT HAVE VALUES");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "75":
				List<Object[]> TotalASSETS = brrsValidationsRepo.getCheckSrlNo75(convertDate);
				if (!TotalASSETS.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2(
							"TOTAL ON-BALANCE SHEET AMOUNT IN EXPOSURE CLASS AND  MONTH END TOTAL IN ASSETS ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2(
							"TOTAL ON-BALANCE SHEET AMOUNT IN EXPOSURE CLASS AND  MONTH END TOTAL IN ASSETS ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "76":
				List<Object[]> TotOnBal = brrsValidationsRepo.getCheckSrlNo76(convertDate);
				if (!TotOnBal.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2(
							"TOTAL ON-BALANCE SHEET AMOUNT IN EXPOSURE CLASS AND  MONTH END TOTAL IN ASSETS ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2(
							"TOTAL ON-BALANCE SHEET AMOUNT IN EXPOSURE CLASS AND  MONTH END TOTAL IN ASSETS ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "77":
				List<Object[]> OFFBALANCE = brrsValidationsRepo.getCheckSrlNo77(convertDate);
				if (!OFFBALANCE.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2(
							"TOTAL OFF-BALANCE SHEET AMOUNT  IN PART B : RISK WEIGHTED AMOUNTS (OFF BALANCESHEET EXPOSURES) AND  BOTSWANA TOTAL IN M_GALOR-III  ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2(
							"TOTAL OFF-BALANCE SHEET AMOUNT  IN PART B : RISK WEIGHTED AMOUNTS (OFF BALANCESHEET EXPOSURES) AND  BOTSWANA TOTAL IN M_GALOR-III  ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "78":
				List<Object[]> TOTALOFFBALANCE = brrsValidationsRepo.getCheckSrlNo78(convertDate);
				if (!TOTALOFFBALANCE.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2(
							"TOTAL OFF-BALANCE SHEET AMOUNT  IN PART B : RISK WEIGHTED AMOUNTS (OFF BALANCESHEET EXPOSURES) AND  BOTSWANA TOTAL IN M_GALOR-III  ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2(
							"TOTAL OFF-BALANCE SHEET AMOUNT  IN PART B : RISK WEIGHTED AMOUNTS (OFF BALANCESHEET EXPOSURES) AND  BOTSWANA TOTAL IN M_GALOR-III  ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;
			case "79":
				List<Object[]> TOTASSGALOR = brrsValidationsRepo.getCheckSrlNo79(convertDate);
				if (!TOTASSGALOR.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("TOTAL ASSETS  IN M-GALOR AND  ASSETS ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("TOTAL ASSETS  IN M-GALOR AND  ASSETS ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "80":
				List<Object[]> TOTASSP1 = brrsValidationsRepo.getCheckSrlNo80(convertDate);
				if (!TOTASSP1.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("TOTAL ASSETS  IN M-GALOR AND  ASSETS ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("TOTAL ASSETS  IN M-GALOR AND  ASSETS ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;
			case "81":
				List<Object[]> TOTASSCALOC = brrsValidationsRepo.getCheckSrlNo81(convertDate);
				if (!TOTASSCALOC.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("  TOTAL ASSETS  IN M-CALOC AND  ASSETS ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("  TOTAL ASSETS  IN M-CALOC AND  ASSETS ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "82":
				List<Object[]> TOTSFIP1 = brrsValidationsRepo.getCheckSrlNo82(convertDate);
				if (!TOTSFIP1.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("  TOTAL ASSETS  IN M-CALOC AND  ASSETS ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("  TOTAL ASSETS  IN M-CALOC AND  ASSETS ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "83":
				List<Object[]> TOTOCTLA1 = brrsValidationsRepo.getCheckSrlNo83(convertDate);
				if (!TOTOCTLA1.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2(
							"  TOTAL OUTSTANDING BALANCE IN M-LA1 AND  GROSS LOAN AND ADVANCE IN M-SFinP1 ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2(
							"  TOTAL OUTSTANDING BALANCE IN M-LA1 AND  GROSS LOAN AND ADVANCE IN M-SFinP1 ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "84":
				List<Object[]> GROSSLOAN = brrsValidationsRepo.getCheckSrlNo84(convertDate);
				if (!GROSSLOAN.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2(
							"  TOTAL OUTSTANDING BALANCE IN M-LA1 AND  GROSS LOAN AND ADVANCE IN M-SFinP1 ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2(
							"  TOTAL OUTSTANDING BALANCE IN M-LA1 AND  GROSS LOAN AND ADVANCE IN M-SFinP1 ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "85":
				List<Object[]> TOTLIQGAP = brrsValidationsRepo.getCheckSrlNo85(convertDate);
				if (!TOTLIQGAP.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("	TOTAL ASSETS IN M-LIQGAP  AND  ASSETS ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("	TOTAL ASSETS IN M-LIQGAP  AND  ASSETS ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "86":
				List<Object[]> TOTSFIP1ASS = brrsValidationsRepo.getCheckSrlNo86(convertDate);
				if (!TOTSFIP1ASS.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("	TOTAL ASSETS IN M-LIQGAP  AND  ASSETS ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("	TOTAL ASSETS IN M-LIQGAP  AND  ASSETS ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "87":
				List<Object[]> LIATOT = brrsValidationsRepo.getCheckSrlNo87(convertDate);
				if (!LIATOT.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2(" TOTAL LIABILITIES IN M-LIQGAP  AND  LIABILITIES ARE NOT EQUALL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2(" TOTAL LIABILITIES IN M-LIQGAP  AND  LIABILITIES ARE NOT EQUALL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "88":
				List<Object[]> LIATOTAL = brrsValidationsRepo.getCheckSrlNo88(convertDate);
				if (!LIATOTAL.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2(" TOTAL LIABILITIES IN M-LIQGAP  AND  LIABILITIES ARE NOT EQUALL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("TOTAL LIABILITIES IN M-LIQGAP  AND  LIABILITIES ARE NOT EQUALL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "89":
				List<Object[]> LA3TOT = brrsValidationsRepo.getCheckSrlNo89(convertDate);
				if (!LA3TOT.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation
							.setRemarks2("TOTAL APPROVED_LIMIT IN M-LIQGAP  AND  TOTAL APPROVED_LIMIT ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation
							.setRemarks2("TOTAL APPROVED_LIMIT IN M-LIQGAP  AND  TOTAL APPROVED_LIMIT ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "90":
				List<Object[]> LA1TOT = brrsValidationsRepo.getCheckSrlNo90(convertDate);
				if (!LA1TOT.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation
							.setRemarks2("TOTAL APPROVED_LIMIT IN M-LIQGAP  AND  TOTAL APPROVED_LIMIT ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation
							.setRemarks2("TOTAL APPROVED_LIMIT IN M-LIQGAP  AND  TOTAL APPROVED_LIMIT ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "91":
				List<Object[]> MPLLTOT = brrsValidationsRepo.getCheckSrlNo91(convertDate);
				if (!MPLLTOT.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation
							.setRemarks2("  TOTAL PROVISION FOR LOANSS IN M-PLL  AND  TOTAL IN M-PD ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation
							.setRemarks2("  TOTAL PROVISION FOR LOANSS IN M-PLL  AND  TOTAL IN M-PD ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "92":
				List<Object[]> MPDTOT = brrsValidationsRepo.getCheckSrlNo92(convertDate);
				if (!MPDTOT.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation
							.setRemarks2("  TOTAL PROVISION FOR LOANSS IN M-PLL  AND  TOTAL IN M-PD ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation
							.setRemarks2("  TOTAL PROVISION FOR LOANSS IN M-PLL  AND  TOTAL IN M-PD ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "93":
				List<Object[]> MIRBTOT = brrsValidationsRepo.getCheckSrlNo93(convertDate);
				if (!MIRBTOT.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2(
							"TOTAL FOR STATIC REPRICING GAP IN M-IRB  AND  MONTH END TOTAL ASSETS ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2(
							"TOTAL FOR STATIC REPRICING GAP IN M-IRB  AND  MONTH END TOTAL ASSETS ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "94":
				List<Object[]> SFIP1TOT = brrsValidationsRepo.getCheckSrlNo94(convertDate);
				if (!SFIP1TOT.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2(
							"TOTAL FOR STATIC REPRICING GAP IN M-IRB  AND  MONTH END TOTAL ASSETS ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2(
							"TOTAL FOR STATIC REPRICING GAP IN M-IRB  AND  MONTH END TOTAL ASSETS ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "95":
				List<Object[]> MGPTOT = brrsValidationsRepo.getCheckSrlNo95(convertDate);
				if (!MGPTOT.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("TOTAL IN M-GP AND GENERAL RESERVE ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("TOTAL IN M-GP AND GENERAL RESERVE ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;

			case "96":
				List<Object[]> SFIP2TOT = brrsValidationsRepo.getCheckSrlNo96(convertDate);
				if (!SFIP2TOT.isEmpty()) {
					brfValidation.setCur_status("Y");
					brfValidation.setRemarks2("TOTAL IN M-GP AND GENERAL RESERVE ARE NOT EQUAL");
					status = "0";
				} else {
					brfValidation.setCur_status("N");
					brfValidation.setRemarks2("TOTAL IN M-GP AND GENERAL RESERVE ARE NOT EQUAL");
					status = "2";
				}
				brrsValidationsRepo.save(brfValidation);
				break;
			case "100":
				 List<Object[]> TOTDEP3  = brrsValidationsRepo.getCheckSrlNo100(convertDate);
	                if (!TOTDEP3.isEmpty()) {
	                    brfValidation.setCur_status("Y");
	                    brfValidation.setRemarks2( "	TOTAL OF PULA EQUIVALENT  IN M-DEP3  AND TOTAL OF INSTITUTIONAL FOREIGN CURRENCY DEPOSITS  ARE NOT EQUAL");
	                    status = "0";
	                } else {
	                    brfValidation.setCur_status("N");
	                    brfValidation.setRemarks2( "	TOTAL OF PULA EQUIVALENT  IN M-DEP3  AND TOTAL OF INSTITUTIONAL FOREIGN CURRENCY DEPOSITS  ARE NOT EQUAL");
	                    status = "2";
	                }
	                brrsValidationsRepo.save(brfValidation);
	                break;
	                
			case "101":
				 List<Object[]> TOTDEP2 = brrsValidationsRepo.getCheckSrlNo101(convertDate);
	                if (!TOTDEP2.isEmpty()) {
	                    brfValidation.setCur_status("Y");
	                    brfValidation.setRemarks2( "	TOTAL OF PULA EQUIVALENT  IN M-DEP3  AND TOTAL OF INSTITUTIONAL FOREIGN CURRENCY DEPOSITS  ARE NOT EQUAL");
	                    status = "0";
	                } else {
	                    brfValidation.setCur_status("N");
	                    brfValidation.setRemarks2( "	TOTAL OF PULA EQUIVALENT  IN M-DEP3  AND TOTAL OF INSTITUTIONAL FOREIGN CURRENCY DEPOSITS  ARE NOT EQUAL");
	                    status = "2";
	                }
	                brrsValidationsRepo.save(brfValidation);
	                break;
	                
			case "102":
				 List<Object[]> TOTLA4  = brrsValidationsRepo.getCheckSrlNo102(convertDate);
	                if (!TOTLA4.isEmpty()) {
	                    brfValidation.setCur_status("Y");
	                    brfValidation.setRemarks2( "	TOTAL OF ADVANCES BY INSTITUTIONAL SECTOR IN M-LA4  AND GROSS LOAN AND ADVANCE IN M-SFinP1  ARE NOT EQUAL");
	                    status = "0";
	                } else {
	                    brfValidation.setCur_status("N");
	                    brfValidation.setRemarks2( "	TOTAL OF ADVANCES BY INSTITUTIONAL SECTOR IN M-LA4  AND GROSS LOAN AND ADVANCE IN M-SFinP1  ARE NOT EQUAL");
	                    status = "2";
	                }
	                brrsValidationsRepo.save(brfValidation);
	                break;
	                
			case "103":
				 List<Object[]> TOTSF1LA4 = brrsValidationsRepo.getCheckSrlNo103(convertDate);
	                if (!TOTSF1LA4.isEmpty()) {
	                    brfValidation.setCur_status("Y");
	                    brfValidation.setRemarks2( "	TOTAL OF ADVANCES BY INSTITUTIONAL SECTOR IN M-LA4  AND GROSS LOAN AND ADVANCE IN M-SFinP1  ARE NOT EQUAL");
	                    status = "0";
	                } else {
	                    brfValidation.setCur_status("N");
	                    brfValidation.setRemarks2( "	TOTAL OF ADVANCES BY INSTITUTIONAL SECTOR IN M-LA4  AND GROSS LOAN AND ADVANCE IN M-SFinP1  ARE NOT EQUAL");
	                    status = "2";
	                }
	                brrsValidationsRepo.save(brfValidation);
	                break;
			default:
				logger.warn("Unhandled srl_no: {}", srl_no);
				msg.setGenID("0");
				msg.setStatus("Unhandled validation type");
				return msg;
			}

			if (status.equals("0")) {
				msg.setGenID("1");
				msg.setStatus("Validation Success");
			} else if (status.equals("2")) {
				msg.setGenID("1");
				msg.setStatus("Validation Failed");
			}
		} catch (Exception e) {
			logger.error("Exception occurred: {}", e.getMessage(), e);
			msg.setGenID("0");
			msg.setStatus("Validation error");
		}

		return msg;
	}

};
