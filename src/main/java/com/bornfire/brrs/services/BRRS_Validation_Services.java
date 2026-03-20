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
	               if(count > 0) {
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
	               if(countP2 > 0) {
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
