package com.bornfire.brrs.services;

import java.util.Date;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bornfire.brrs.config.SequenceGenerator;
import com.bornfire.brrs.entities.AuditServicesEntity;
import com.bornfire.brrs.entities.AuditServicesRep;
import com.bornfire.brrs.entities.BDGF_Entity;
import com.bornfire.brrs.entities.BDGF_Rep;
import com.bornfire.brrs.entities.BFDB_Entity;
import com.bornfire.brrs.entities.BFDB_Rep;
import com.bornfire.brrs.entities.BLBF_Entity;
import com.bornfire.brrs.entities.BLBF_Rep;
import com.bornfire.brrs.entities.BrrsGeneralMasterEntity;
import com.bornfire.brrs.entities.BrrsGeneralMasterRepo;
import com.bornfire.brrs.entities.GeneralMasterEntity;
import com.bornfire.brrs.entities.GeneralMasterRepo;
import com.bornfire.brrs.entities.MCBL_Entity;
import com.bornfire.brrs.entities.MCBL_Main_Rep;
import com.bornfire.brrs.entities.MCBL_Rep;

@Service
public class AuditService {
	private static final Logger logger = LoggerFactory.getLogger(AuditService.class);
	@Autowired
	BFDB_Rep BFDB_Reps;
    @Autowired
    private BDGF_Rep BDGF_Reps;
	@Autowired
	SequenceGenerator sequence;
	@Autowired
	GeneralMasterRepo GeneralMasterRepos;
	@Autowired
	private AuditServicesRep auditServicesRep;
	@Autowired
	MCBL_Main_Rep MCBL_Main_Reps;
	@Autowired
	MCBL_Rep MCBL_Reps;
	@Autowired
	BLBF_Rep BLBF_Reps;

	public List<AuditServicesEntity> getUserServices() {
		System.out.println(auditServicesRep.getUserAudit());
		return auditServicesRep.getUserAudit();
	}

	public List<AuditServicesEntity> getAuditServices() {
		System.out.println(auditServicesRep.getServiceAudit());
		return auditServicesRep.getServiceAudit();	
	}

	public void createBusinessAudit(final String customerId, final String functionCode, final String screenName,
			final Map<String, String> changeDetails, final String tableName) {
		try {
			final UUID auditID = UUID.randomUUID();
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			String userId = null;
			String username = null;
			if (attr != null) {
				HttpServletRequest request = attr.getRequest();
				userId = (String) request.getSession().getAttribute("USERID");
				username = (String) request.getSession().getAttribute("USERNAME");
			}
			final Date currentDate = new Date();

			AuditServicesEntity audit = new AuditServicesEntity();
			audit.setAudit_ref_no(auditID.toString());
			audit.setAudit_date(currentDate);
			audit.setEntry_time(currentDate);
			audit.setEntry_user(userId);
			audit.setEntry_user_name(username);
			audit.setFunc_code(functionCode);
			audit.setAudit_table(tableName);
			audit.setAudit_screen(screenName);
			audit.setEvent_id(userId);
			audit.setEvent_name(username);

			if (changeDetails != null && !changeDetails.isEmpty()) {
				StringBuilder changes = new StringBuilder();
				changeDetails
						.forEach((field, value) -> changes.append(field).append(": ").append(value).append("||| "));
			
	            audit.setChange_details(changes.toString()); 
			}

			if ("VERIFY".equalsIgnoreCase(functionCode)) {
				audit.setAuth_user(userId);
				audit.setAuth_user_name(username);
				audit.setAuth_time(currentDate);
			}

			audit.setReport_id(customerId);

			System.out.println(audit);
			auditServicesRep.save(audit);

		} catch (Exception e) {
			System.err.println("Error creating business audit: " + e.getMessage());
			e.printStackTrace();
		}
	}
	

	public String fetchChanges(@RequestParam(required = false) String audit_ref_no) {

	return auditServicesRep.getchanges(audit_ref_no); 
	}
	
	
	public String createAccount_MCBL(String formmode, GeneralMasterEntity newData, String userid) {
	    String msg = null;
        logger.info("Enter MCBL Account method...");

	    try {
	            
	            if ("add".equalsIgnoreCase(formmode)) {
	                GeneralMasterEntity existing = GeneralMasterRepos.getdataByAcc(newData.getMcbl_head_acc_no(), newData.getReport_date());
	                
	                if (existing != null) {
	                    logger.info("MCBL Data Already Exist...");
	                    return "MCBL Data Already Exist, So can you Replace It";
	                } else {
	                    GeneralMasterEntity newAcc = new GeneralMasterEntity();
	                    newAcc.setId(sequence.generateRequestUUId());
	                    newAcc.setMcbl_gl_code(newData.getMcbl_gl_code());
	                    newAcc.setMcbl_gl_sub_code(newData.getMcbl_gl_sub_code());
	                    newAcc.setMcbl_head_acc_no(newData.getMcbl_head_acc_no());
	                    newAcc.setMcbl_currency(newData.getMcbl_currency());
	                    newAcc.setMcbl_description(newData.getMcbl_description());
	                    newAcc.setMcbl_debit_balance(newData.getMcbl_debit_balance());
	                    newAcc.setMcbl_credit_balance(newData.getMcbl_credit_balance());
	                    newAcc.setMcbl_debit_equivalent(newData.getMcbl_debit_equivalent());
	                    newAcc.setMcbl_credit_equivalent(newData.getMcbl_credit_equivalent());
	                    newAcc.setReport_date(newData.getReport_date());
	                    newAcc.setEntry_user(userid);
	                    newAcc.setEntry_date(new Date());
	                    newAcc.setModify_flg("Y");
	                    newAcc.setDel_flg("N");
	                    newAcc.setMcbl_flg("Y");

	                    GeneralMasterRepos.save(newAcc);

	                    MCBL_Entity mcblNew = new MCBL_Entity();
	                    mcblNew.setId(sequence.generateRequestUUId());
	                    mcblNew.setMcbl_gl_code(newData.getMcbl_gl_code());
	                    mcblNew.setMcbl_gl_sub_code(newData.getMcbl_gl_sub_code());
	                    mcblNew.setMcbl_head_acc_no(newData.getMcbl_head_acc_no());
	                    mcblNew.setMcbl_currency(newData.getMcbl_currency());
	                    mcblNew.setMcbl_description(newData.getMcbl_description());
	                    mcblNew.setMcbl_debit_balance(newData.getMcbl_debit_balance());
	                    mcblNew.setMcbl_credit_balance(newData.getMcbl_credit_balance());
	                    mcblNew.setMcbl_debit_equivalent(newData.getMcbl_debit_equivalent());
	                    mcblNew.setMcbl_credit_equivalent(newData.getMcbl_credit_equivalent());
	                    mcblNew.setReport_date(newData.getReport_date());
	                    mcblNew.setEntry_user(userid);
	                    mcblNew.setEntry_date(new Date());
	                    mcblNew.setModify_flg("Y");
	                    mcblNew.setDelete_flg("N");

	                    MCBL_Reps.save(mcblNew);

	                    msg = "MCBL Added successfully";
	                }
	            }else if ("edit".equalsIgnoreCase(formmode)) {
	                GeneralMasterEntity newAcc = GeneralMasterRepos.getById(newData.getId());
	                
	                if (newAcc != null) {
	                    newAcc.setMcbl_gl_code(newData.getMcbl_gl_code());
	                    newAcc.setMcbl_gl_sub_code(newData.getMcbl_gl_sub_code());
	                    newAcc.setMcbl_currency(newData.getMcbl_currency());
	                    newAcc.setMcbl_description(newData.getMcbl_description());
	                    newAcc.setMcbl_debit_balance(newData.getMcbl_debit_balance());
	                    newAcc.setMcbl_credit_balance(newData.getMcbl_credit_balance());
	                    newAcc.setMcbl_debit_equivalent(newData.getMcbl_debit_equivalent());
	                    newAcc.setMcbl_credit_equivalent(newData.getMcbl_credit_equivalent());
	                    newAcc.setModify_user(userid);
	                    newAcc.setModify_flg("Y");
	                    newAcc.setDel_flg("N");

	                    GeneralMasterRepos.save(newAcc);

	                    MCBL_Entity mcblNew = MCBL_Reps.getdataByAcc(newData.getMcbl_head_acc_no(), newData.getReport_date());
	                    if (mcblNew != null) {
	                        mcblNew.setMcbl_gl_code(newData.getMcbl_gl_code());
	                        mcblNew.setMcbl_gl_sub_code(newData.getMcbl_gl_sub_code());
	                        mcblNew.setMcbl_currency(newData.getMcbl_currency());
	                        mcblNew.setMcbl_description(newData.getMcbl_description());
	                        mcblNew.setMcbl_debit_balance(newData.getMcbl_debit_balance());
	                        mcblNew.setMcbl_credit_balance(newData.getMcbl_credit_balance());
	                        mcblNew.setMcbl_debit_equivalent(newData.getMcbl_debit_equivalent());
	                        mcblNew.setMcbl_credit_equivalent(newData.getMcbl_credit_equivalent());
	                        mcblNew.setModify_flg("Y");
	                        mcblNew.setDelete_flg("N");

	                        MCBL_Reps.save(mcblNew);
	                    }

	                    msg = "MCBL Data Edited Successfully";
	                } else {
	                    msg = "Error: MCBL not found for Account " + newData.getMcbl_head_acc_no();
	                }
	            }else if ("delete".equalsIgnoreCase(formmode)) {
	                GeneralMasterEntity existingOpt = GeneralMasterRepos.getdataByAcc(newData.getMcbl_head_acc_no(), newData.getReport_date());
	                if (existingOpt != null) {
	                    GeneralMasterRepos.delete(existingOpt);
	                }

	                MCBL_Entity existingOpt1 = MCBL_Reps.getdataByAcc(newData.getMcbl_head_acc_no(), newData.getReport_date());
	                if (existingOpt1 != null) {
	                    MCBL_Reps.delete(existingOpt1);
	                }

	                msg = "MCBL deleted successfully";
	            }
	        
	    } catch (Exception e) {
	        msg = "Error: " + e.getMessage();
	    }

	    return msg;
	}
	
	public String createAccount_BLBF(String formmode, GeneralMasterEntity newData, String userid) {
	    String msg = null;
	    logger.info("Enter BLBF Account method for {} mode...", formmode);

	    try {
	        // Fetch existing records
	        GeneralMasterEntity existingGM = GeneralMasterRepos.getdataByAcc(newData.getAccount_no(), newData.getReport_date());
	        BLBF_Entity existingBLBF = BLBF_Reps.findById(newData.getAccount_no()).orElse(null);

	        switch (formmode.toLowerCase()) {
	            case "add":
	                if (existingGM != null) {
	                    return "BLBF Data Already Exist, so can you Replace It";
	                }

	                // Create new GeneralMasterEntity
	                GeneralMasterEntity gm = copyBLBFDataToGeneralMaster(newData, userid);
	                GeneralMasterRepos.save(gm);

	                // Create new BLBF_Entity
	                BLBF_Entity blbf = copyBLBFDataToBLBF(newData, userid);
	                BLBF_Reps.save(blbf);

	                msg = "BLBF Added successfully";
	                break;

	            case "edit":
	                if (existingGM != null) {
	                    updateBLBFData(existingGM, newData, userid);
	                    GeneralMasterRepos.save(existingGM);
	                } else {
	                    return "Error: BLBF not found for Account " + newData.getAccount_no();
	                }

	                if (existingBLBF != null) {
	                    updateBLBFData(existingBLBF, newData);
	                    BLBF_Reps.save(existingBLBF);
	                }

	                msg = "BLBF Data Edited Successfully";
	                break;

	            case "delete":
	                if (existingGM != null) GeneralMasterRepos.delete(existingGM);
	                if (existingBLBF != null) BLBF_Reps.delete(existingBLBF);
	                msg = "BLBF deleted successfully";
	                break;

	            default:
	                msg = "Unknown form mode: " + formmode;
	        }

	    } catch (Exception e) {
	        logger.error("Error while processing BLBF: {}", e.getMessage(), e);
	        msg = "Error: " + e.getMessage();
	    }

	    return msg;
	}

	// Helper methods
	private GeneralMasterEntity copyBLBFDataToGeneralMaster(GeneralMasterEntity data, String userid) {
	    GeneralMasterEntity gm = new GeneralMasterEntity();
	    gm.setId(sequence.generateRequestUUId());
	    gm.setAccount_no(data.getAccount_no());
	    gm.setCustomer_id(data.getCustomer_id());
	    gm.setSol_id(data.getSol_id());
	    gm.setCustomer_name(data.getCustomer_name());
	    gm.setSchm_code(data.getSchm_code());
	    gm.setSchm_desc(data.getSchm_desc());
	    gm.setAcct_open_date(data.getAcct_open_date());
	    gm.setAcct_close_date(data.getAcct_close_date());
	    gm.setApproved_limit(data.getApproved_limit());
	    gm.setSanction_limit(data.getSanction_limit());
	    gm.setDisbursed_amt(data.getDisbursed_amt());
	    gm.setBalance_as_on(data.getBalance_as_on());
	    
	    gm.setHundred(data.getHundred());
	    gm.setPeriod(data.getPeriod());
	    gm.setEffective_interest_rate(data.getEffective_interest_rate());
	    gm.setMat_bucket(data.getMat_bucket());
	    
	    gm.setCurrency(data.getCurrency());
	    gm.setBal_equi_to_bwp(data.getBal_equi_to_bwp());
	    gm.setRate_of_interest(data.getRate_of_interest());
	    gm.setAccrued_int_amt(data.getAccrued_int_amt());
	    gm.setMonthly_interest(data.getMonthly_interest());
	    gm.setLast_interest_debit_date(data.getLast_interest_debit_date());
	    gm.setAcct_cls_flg(data.getAcct_cls_flg());
	    gm.setGender(data.getGender());
	    gm.setClassification_code(data.getClassification_code());
	    gm.setConstitution_code(data.getConstitution_code());
	    gm.setMaturity_date(data.getMaturity_date());
	    gm.setGl_sub_head_code(data.getGl_sub_head_code());
	    gm.setGl_sub_head_desc(data.getGl_sub_head_desc());
	    gm.setTenor_month(data.getTenor_month());
	    gm.setEmi(data.getEmi());
	    gm.setSegment(data.getSegment());
	    gm.setFacility(data.getFacility());
	    gm.setPast_due(data.getPast_due());
	    gm.setPast_due_days(data.getPast_due_days());
	    gm.setAsset(data.getAsset());
	    gm.setProvision(data.getProvision());
	    gm.setUnsecured(data.getUnsecured());
	    gm.setInt_bucket(data.getInt_bucket());
	    gm.setStaff(data.getStaff());
	    gm.setSmme(data.getSmme());
	    gm.setLabod(data.getLabod());
	    gm.setNew_ac(data.getNew_ac());
	    gm.setUndrawn(data.getUndrawn());
	    gm.setSector(data.getSector());
	    gm.setStage(data.getStage());
	    gm.setEcl_provision(data.getEcl_provision());
	    gm.setBranch_name(data.getBranch_name());
	    gm.setBranch_code(data.getBranch_code());
	    gm.setReport_date(data.getReport_date());
	    gm.setEntry_date(new Date());
	    gm.setEntry_user(userid);
	    gm.setModify_flg("Y");
	    gm.setBlbf_flg("Y");
	    gm.setDel_flg("N");
	    return gm;
	}

	private BLBF_Entity copyBLBFDataToBLBF(GeneralMasterEntity data, String userid) {
	    BLBF_Entity blbf = new BLBF_Entity();
	    blbf.setAccount_no(data.getAccount_no());
	    blbf.setCustomer_id(data.getCustomer_id());
	    blbf.setSol_id(data.getSol_id());
	    blbf.setCustomer_name(data.getCustomer_name());
	    blbf.setSchm_code(data.getSchm_code());
	    blbf.setSchm_desc(data.getSchm_desc());
	    blbf.setAcct_open_date(data.getAcct_open_date());
	    blbf.setAcct_close_date(data.getAcct_close_date());
	    blbf.setApproved_limit(data.getApproved_limit());
	    blbf.setSanction_limit(data.getSanction_limit());
	    blbf.setDisbursed_amt(data.getDisbursed_amt());
	    blbf.setBalance_as_on(data.getBalance_as_on());
	    blbf.setCurrency(data.getCurrency());

	    blbf.setHundred(data.getHundred());
	    blbf.setPeriod(data.getPeriod());
	    blbf.setEffective_interest_rate(data.getEffective_interest_rate());
	    blbf.setMat_bucket(data.getMat_bucket());
	    
	    blbf.setBal_equi_to_bwp(data.getBal_equi_to_bwp());
	    blbf.setRate_of_interest(data.getRate_of_interest());
	    blbf.setAccrued_int_amt(data.getAccrued_int_amt());
	    blbf.setMonthly_interest(data.getMonthly_interest());
	    blbf.setLast_interest_debit_date(data.getLast_interest_debit_date());
	    blbf.setAcct_cls_flg(data.getAcct_cls_flg());
	    blbf.setGender(data.getGender());
	    blbf.setClassification_code(data.getClassification_code());
	    blbf.setConstitution_code(data.getConstitution_code());
	    blbf.setMaturity_date(data.getMaturity_date());
	    blbf.setGl_sub_head_code(data.getGl_sub_head_code());
	    blbf.setGl_sub_head_desc(data.getGl_sub_head_desc());
	    blbf.setTenor_month(data.getTenor_month());
	    blbf.setEmi(data.getEmi());
	    blbf.setSegment(data.getSegment());
	    blbf.setFacility(data.getFacility());
	    blbf.setPast_due(data.getPast_due());
	    blbf.setPast_due_days(data.getPast_due_days());
	    blbf.setAsset(data.getAsset());
	    blbf.setProvision(data.getProvision());
	    blbf.setUnsecured(data.getUnsecured());
	    blbf.setInt_bucket(data.getInt_bucket());
	    blbf.setStaff(data.getStaff());
	    blbf.setSmme(data.getSmme());
	    blbf.setLabod(data.getLabod());
	    blbf.setNew_ac(data.getNew_ac());
	    blbf.setUndrawn(data.getUndrawn());
	    blbf.setSector(data.getSector());
	    blbf.setStage(data.getStage());
	    blbf.setEcl_provision(data.getEcl_provision());
	    blbf.setBranch_name(data.getBranch_name());
	    blbf.setBranch_code(data.getBranch_code());
	    blbf.setReport_date(data.getReport_date());
	    blbf.setEntry_date(new Date());
	    blbf.setEntry_user(userid);
	    return blbf;
	}

	private void updateBLBFData(GeneralMasterEntity existing, GeneralMasterEntity newData, String userid) {
	    existing.setCustomer_id(newData.getCustomer_id());
	    existing.setSol_id(newData.getSol_id());
	    existing.setCustomer_name(newData.getCustomer_name());
	    existing.setSchm_code(newData.getSchm_code());
	    existing.setSchm_desc(newData.getSchm_desc());
	    existing.setAcct_open_date(newData.getAcct_open_date());
	    existing.setAcct_close_date(newData.getAcct_close_date());
	    existing.setApproved_limit(newData.getApproved_limit());
	    existing.setSanction_limit(newData.getSanction_limit());
	    existing.setDisbursed_amt(newData.getDisbursed_amt());
	    existing.setBalance_as_on(newData.getBalance_as_on());
	    existing.setCurrency(newData.getCurrency());
	    existing.setBal_equi_to_bwp(newData.getBal_equi_to_bwp());
	    existing.setRate_of_interest(newData.getRate_of_interest());
	    existing.setAccrued_int_amt(newData.getAccrued_int_amt());
	    existing.setMonthly_interest(newData.getMonthly_interest());
	    existing.setLast_interest_debit_date(newData.getLast_interest_debit_date());
	    existing.setAcct_cls_flg(newData.getAcct_cls_flg());
	    existing.setGender(newData.getGender());
	    existing.setClassification_code(newData.getClassification_code());
	    existing.setConstitution_code(newData.getConstitution_code());
	    existing.setMaturity_date(newData.getMaturity_date());
	    existing.setGl_sub_head_code(newData.getGl_sub_head_code());
	    existing.setGl_sub_head_desc(newData.getGl_sub_head_desc());
	    existing.setTenor_month(newData.getTenor_month());
	    existing.setEmi(newData.getEmi());
	    existing.setSegment(newData.getSegment());
	    existing.setFacility(newData.getFacility());
	    existing.setPast_due(newData.getPast_due());
	    existing.setPast_due_days(newData.getPast_due_days());
	    existing.setAsset(newData.getAsset());
	    existing.setProvision(newData.getProvision());
	    existing.setUnsecured(newData.getUnsecured());
	    existing.setInt_bucket(newData.getInt_bucket());
	    existing.setStaff(newData.getStaff());
	    existing.setSmme(newData.getSmme());
	    existing.setLabod(newData.getLabod());
	    existing.setNew_ac(newData.getNew_ac());
	    existing.setUndrawn(newData.getUndrawn());
	    existing.setSector(newData.getSector());
	    existing.setStage(newData.getStage());
	    existing.setEcl_provision(newData.getEcl_provision());
	    existing.setBranch_name(newData.getBranch_name());
	    existing.setBranch_code(newData.getBranch_code());
	    existing.setModify_user(userid);
	    existing.setModify_flg("Y");
	    existing.setDel_flg("N");
	}

	private void updateBLBFData(BLBF_Entity existing, GeneralMasterEntity newData) {
	    existing.setCustomer_id(newData.getCustomer_id());
	    existing.setSol_id(newData.getSol_id());
	    existing.setCustomer_name(newData.getCustomer_name());
	    existing.setSchm_code(newData.getSchm_code());
	    existing.setSchm_desc(newData.getSchm_desc());
	    existing.setAcct_open_date(newData.getAcct_open_date());
	    existing.setAcct_close_date(newData.getAcct_close_date());
	    existing.setApproved_limit(newData.getApproved_limit());
	    existing.setSanction_limit(newData.getSanction_limit());
	    existing.setDisbursed_amt(newData.getDisbursed_amt());
	    existing.setBalance_as_on(newData.getBalance_as_on());
	    existing.setCurrency(newData.getCurrency());
	    existing.setBal_equi_to_bwp(newData.getBal_equi_to_bwp());
	    existing.setRate_of_interest(newData.getRate_of_interest());
	    existing.setAccrued_int_amt(newData.getAccrued_int_amt());
	    existing.setMonthly_interest(newData.getMonthly_interest());
	    existing.setLast_interest_debit_date(newData.getLast_interest_debit_date());
	    existing.setAcct_cls_flg(newData.getAcct_cls_flg());
	    existing.setGender(newData.getGender());
	    existing.setClassification_code(newData.getClassification_code());
	    existing.setConstitution_code(newData.getConstitution_code());
	    existing.setMaturity_date(newData.getMaturity_date());
	    existing.setGl_sub_head_code(newData.getGl_sub_head_code());
	    existing.setGl_sub_head_desc(newData.getGl_sub_head_desc());
	    existing.setTenor_month(newData.getTenor_month());
	    existing.setEmi(newData.getEmi());
	    existing.setSegment(newData.getSegment());
	    existing.setFacility(newData.getFacility());
	    existing.setPast_due(newData.getPast_due());
	    existing.setPast_due_days(newData.getPast_due_days());
	    existing.setAsset(newData.getAsset());
	    existing.setProvision(newData.getProvision());
	    existing.setUnsecured(newData.getUnsecured());
	    existing.setInt_bucket(newData.getInt_bucket());
	    existing.setStaff(newData.getStaff());
	    existing.setSmme(newData.getSmme());
	    existing.setLabod(newData.getLabod());
	    existing.setNew_ac(newData.getNew_ac());
	    existing.setUndrawn(newData.getUndrawn());
	    existing.setSector(newData.getSector());
	    existing.setStage(newData.getStage());
	    existing.setEcl_provision(newData.getEcl_provision());
	}


	
	public String createAccount_BDGF(String formmode, GeneralMasterEntity newData, String userid) {
	    String msg = null;
	    logger.info("Enter BDGF Account method...");

	    try {
	        if ("add".equalsIgnoreCase(formmode)) {
	            // Check if record exists
	            GeneralMasterEntity existing = GeneralMasterRepos.getdataByAcc(newData.getAccount_no(), newData.getReport_date());
	            if (existing != null) {
	                logger.info("BDGF Data Already Exist...");
	                return "BDGF Data Already Exist, So can you Replace It";
	            }

	            // 1️⃣ Insert into GENERAL_MASTER_TABLE
	            GeneralMasterEntity gen = new GeneralMasterEntity();
	            gen.setId(sequence.generateRequestUUId());
	            gen.setSol_id(newData.getSol_id());
	            gen.setAccount_no(newData.getAccount_no());
	            gen.setCustomer_id(newData.getCustomer_id());
	            gen.setCustomer_name(newData.getCustomer_name());
	            gen.setAcct_open_date(newData.getAcct_open_date());
	            gen.setAmount_deposited(newData.getAmount_deposited());
	            gen.setCurrency(newData.getCurrency());
	            gen.setPeriod(newData.getPeriod());
	            gen.setRate_of_interest(newData.getRate_of_interest());
	            gen.setHundred(newData.getHundred());
	            gen.setBal_equi_to_bwp(newData.getBal_equi_to_bwp());
	            gen.setOutstanding_balance(newData.getOutstanding_balance());
	            gen.setOustndng_bal_ugx(newData.getOustndng_bal_ugx());
	            gen.setMaturity_date(newData.getMaturity_date());
	            gen.setMaturity_amount(newData.getMaturity_amount());
	            gen.setScheme(newData.getScheme());
	            gen.setCr_pref_int_rate(newData.getCr_pref_int_rate());
	            gen.setSegment(newData.getSegment());
	            gen.setReference_date(newData.getReference_date());
	            gen.setDifference(newData.getDifference());
	            gen.setDays(newData.getDays());
	            gen.setPeriod_days(newData.getPeriod_days());
	            gen.setEffective_interest_rate(newData.getEffective_interest_rate());
	            gen.setBranch_name(newData.getBranch_name());
	            gen.setBranch_code(newData.getBranch_code());
	            //gen.setResidual_tenure(newData.getResidual_tenure());
	            //gen.setSls_bucket(newData.getSls_bucket());
	            gen.setReport_date(newData.getReport_date());
	            gen.setEntry_date(new Date());
	            gen.setEntry_user(userid);
	            gen.setModify_flg("Y");
	            gen.setDel_flg("N");
	            gen.setBdgf_flg("Y");

	            GeneralMasterRepos.save(gen);

	            // 2️⃣ Insert into BRRS_BDGF
	            BDGF_Entity bdgf = new BDGF_Entity();
	            bdgf.setSol_id(newData.getSol_id());
	            bdgf.setAccount_no(newData.getAccount_no());
	            bdgf.setCustomer_id(newData.getCustomer_id());
	            bdgf.setCustomer_name(newData.getCustomer_name());
	            bdgf.setAcct_open_date(newData.getAcct_open_date());
	            bdgf.setAmount_deposited(newData.getAmount_deposited());
	            bdgf.setCurrency(newData.getCurrency());
	            bdgf.setPeriod(newData.getPeriod());
	            bdgf.setRate_of_interest(newData.getRate_of_interest());
	            bdgf.setHundred(newData.getHundred());
	            bdgf.setBal_equi_to_bwp(newData.getBal_equi_to_bwp());
	            bdgf.setOutstanding_balance(newData.getOutstanding_balance());
	            bdgf.setOustndng_bal_ugx(newData.getOustndng_bal_ugx());
	            bdgf.setMaturity_date(newData.getMaturity_date());
	            bdgf.setMaturity_amount(newData.getMaturity_amount());
	            bdgf.setScheme(newData.getScheme());
	            bdgf.setCr_pref_int_rate(newData.getCr_pref_int_rate());
	            bdgf.setSegment(newData.getSegment());
	            bdgf.setReference_date(newData.getReference_date());
	            bdgf.setDifference(newData.getDifference());
	            bdgf.setDays(newData.getDays());
	            bdgf.setPeriod_days(newData.getPeriod_days());
	            bdgf.setEffective_interest_rate(newData.getEffective_interest_rate());
	            bdgf.setBranch_name(newData.getBranch_name());
	            bdgf.setBranch_code(newData.getBranch_code());
	            //bdgf.setResidual_tenure(newData.getResidual_tenure());
	            //bdgf.setSls_bucket(newData.getSls_bucket());
	            bdgf.setReport_date(newData.getReport_date());
	            bdgf.setEntry_date(new Date());
	            bdgf.setEntry_user(userid);
	            bdgf.setModify_flg("Y");
	            bdgf.setDel_flg("N");

	            BDGF_Reps.save(bdgf);

	            msg = "BDGF Added Successfully";

	        } else if ("edit".equalsIgnoreCase(formmode)) {
	            // 1️⃣ Update General Master
	            GeneralMasterEntity gen = GeneralMasterRepos.getdataByAcc(newData.getAccount_no(), newData.getReport_date());
	            if (gen != null) {
	                gen.setSol_id(newData.getSol_id());
	                gen.setCustomer_id(newData.getCustomer_id());
	                gen.setCustomer_name(newData.getCustomer_name());
	                gen.setAcct_open_date(newData.getAcct_open_date());
	                gen.setAmount_deposited(newData.getAmount_deposited());
	                gen.setCurrency(newData.getCurrency());
	                gen.setPeriod(newData.getPeriod());
	                gen.setRate_of_interest(newData.getRate_of_interest());
	                gen.setHundred(newData.getHundred());
	                gen.setBal_equi_to_bwp(newData.getBal_equi_to_bwp());
	                gen.setOutstanding_balance(newData.getOutstanding_balance());
	                gen.setOustndng_bal_ugx(newData.getOustndng_bal_ugx());
	                gen.setMaturity_date(newData.getMaturity_date());
	                gen.setMaturity_amount(newData.getMaturity_amount());
	                gen.setScheme(newData.getScheme());
	                gen.setCr_pref_int_rate(newData.getCr_pref_int_rate());
	                gen.setSegment(newData.getSegment());
	                gen.setReference_date(newData.getReference_date());
	                gen.setDifference(newData.getDifference());
	                gen.setDays(newData.getDays());
	                gen.setPeriod_days(newData.getPeriod_days());
	                gen.setEffective_interest_rate(newData.getEffective_interest_rate());
	                gen.setBranch_name(newData.getBranch_name());
	                gen.setBranch_code(newData.getBranch_code());
	                //gen.setResidual_tenure(newData.getResidual_tenure());
	                //gen.setSls_bucket(newData.getSls_bucket());
	                gen.setModify_user(userid);
	                gen.setModify_date(new Date());
	                gen.setModify_flg("Y");
	                gen.setDel_flg("N");

	                GeneralMasterRepos.save(gen);

	                // 2️⃣ Update BDGF
	                BDGF_Entity bdgf = BDGF_Reps.getdataByAcc(newData.getAccount_no(), newData.getReport_date());
	                if (bdgf != null) {
	                    bdgf.setSol_id(newData.getSol_id());
	                    bdgf.setCustomer_id(newData.getCustomer_id());
	                    bdgf.setCustomer_name(newData.getCustomer_name());
	                    bdgf.setAcct_open_date(newData.getAcct_open_date());
	                    bdgf.setAmount_deposited(newData.getAmount_deposited());
	                    bdgf.setCurrency(newData.getCurrency());
	                    bdgf.setPeriod(newData.getPeriod());
	                    bdgf.setRate_of_interest(newData.getRate_of_interest());
	                    bdgf.setHundred(newData.getHundred());
	                    bdgf.setBal_equi_to_bwp(newData.getBal_equi_to_bwp());
	                    bdgf.setOutstanding_balance(newData.getOutstanding_balance());
	                    bdgf.setOustndng_bal_ugx(newData.getOustndng_bal_ugx());
	                    bdgf.setMaturity_date(newData.getMaturity_date());
	                    bdgf.setMaturity_amount(newData.getMaturity_amount());
	                    bdgf.setScheme(newData.getScheme());
	                    bdgf.setCr_pref_int_rate(newData.getCr_pref_int_rate());
	                    bdgf.setSegment(newData.getSegment());
	                    bdgf.setReference_date(newData.getReference_date());
	                    bdgf.setDifference(newData.getDifference());
	                    bdgf.setDays(newData.getDays());
	                    bdgf.setPeriod_days(newData.getPeriod_days());
	                    bdgf.setEffective_interest_rate(newData.getEffective_interest_rate());
	                    bdgf.setBranch_name(newData.getBranch_name());
	                    bdgf.setBranch_code(newData.getBranch_code());
	                    //bdgf.setResidual_tenure(newData.getResidual_tenure());
	                    //bdgf.setSls_bucket(newData.getSls_bucket());
	                    bdgf.setModify_user(userid);
	                    bdgf.setModify_date(new Date());
	                    bdgf.setModify_flg("Y");
	                    bdgf.setDel_flg("N");

	                    BDGF_Reps.save(bdgf);
	                }

	                msg = "BDGF Data Edited Successfully";

	            } else {
	                msg = "Error: BDGF not found for Account " + newData.getAccount_no();
	            }

	        } else if ("delete".equalsIgnoreCase(formmode)) {
	            GeneralMasterEntity gen = GeneralMasterRepos.getdataByAcc(newData.getAccount_no(), newData.getReport_date());
	            if (gen != null) {
	                GeneralMasterRepos.delete(gen);
	            }

	            BDGF_Entity bdgf = BDGF_Reps.getdataByAcc(newData.getAccount_no(), newData.getReport_date());
	            if (bdgf != null) {
	                BDGF_Reps.delete(bdgf);
	            }

	            msg = "BDGF deleted successfully";
	        }

	    } catch (Exception e) {
	        msg = "Error: " + e.getMessage();
	        logger.error("Exception in createAccount_BDGF: ", e);
	    }

	    return msg;
	}
	public String createAccount_BFDB(String formmode, GeneralMasterEntity newData, String userid) {
	    String msg = null;
	    logger.info("Enter BFDB Account method...");

	    try {
	        if ("add".equalsIgnoreCase(formmode)) {

	            // Check existing data
	            GeneralMasterEntity existing = GeneralMasterRepos.getdataByAcc(newData.getAccount_no(), newData.getReport_date());
	            if (existing != null) {
	                logger.info("BFDB Data Already Exist...");
	                return "BFDB Data Already Exist, Do you want to replace it?";
	            }

	            // 1️⃣ Insert into GENERAL_MASTER_TABLE
	            GeneralMasterEntity gen = new GeneralMasterEntity();
	            gen.setId(sequence.generateRequestUUId());
	            gen.setSol_id(newData.getSol_id());
	            gen.setGender(newData.getGender());
	            gen.setAccount_no(newData.getAccount_no());
	            gen.setCustomer_id(newData.getCustomer_id());
	            gen.setCustomer_name(newData.getCustomer_name());
	            gen.setSchm_code(newData.getSchm_code());
	            gen.setSchm_desc(newData.getSchm_desc());
	            gen.setAcct_open_date(newData.getAcct_open_date());
	            gen.setAcct_close_date(newData.getAcct_close_date());
	            gen.setBalance_as_on(newData.getBalance_as_on());
	            gen.setCurrency(newData.getCurrency());
	            gen.setBal_equi_to_bwp(newData.getBal_equi_to_bwp());
	            gen.setRate_of_interest(newData.getRate_of_interest());
	            gen.setHundred(newData.getHundred());
	            gen.setStatus(newData.getStatus());
	            gen.setMaturity_date(newData.getMaturity_date());
	            gen.setGl_sub_head_code(newData.getGl_sub_head_code());
	            gen.setGl_sub_head_desc(newData.getGl_sub_head_desc());
	            gen.setType_of_accounts(newData.getType_of_accounts());
	            gen.setSegment(newData.getSegment());
	            gen.setPeriod(newData.getPeriod());
	            gen.setEffective_interest_rate(newData.getEffective_interest_rate());
	            gen.setBranch_name(newData.getBranch_name());
	            gen.setBranch_code(newData.getBranch_code());
	            gen.setReport_date(newData.getReport_date());
	            gen.setEntry_user(userid);
	            gen.setEntry_date(new Date());
	            gen.setModify_flg("Y");
	            gen.setDel_flg("N");
	            gen.setBfdb_flg("Y");

	            GeneralMasterRepos.save(gen);

	            // 2️⃣ Insert into BRRS_BFDB
	            BFDB_Entity bfdb = new BFDB_Entity();
	            bfdb.setSol_id(newData.getSol_id());
	            bfdb.setGender(newData.getGender());
	            bfdb.setAccount_no(newData.getAccount_no());
	            bfdb.setCustomer_id(newData.getCustomer_id());
	            bfdb.setCustomer_name(newData.getCustomer_name());
	            bfdb.setSchm_code(newData.getSchm_code());
	            bfdb.setSchm_desc(newData.getSchm_desc());
	            bfdb.setAcct_open_date(newData.getAcct_open_date());
	            bfdb.setAcct_close_date(newData.getAcct_close_date());
	            bfdb.setBalance_as_on(newData.getBalance_as_on());
	            bfdb.setCurrency(newData.getCurrency());
	            bfdb.setBal_equi_to_bwp(newData.getBal_equi_to_bwp());
	            bfdb.setRate_of_interest(newData.getRate_of_interest());
	            bfdb.setHundred(newData.getHundred());
	            bfdb.setStatus(newData.getStatus());
	            bfdb.setMaturity_date(newData.getMaturity_date());
	            bfdb.setGl_sub_head_code(newData.getGl_sub_head_code());
	            bfdb.setGl_sub_head_desc(newData.getGl_sub_head_desc());
	            bfdb.setType_of_accounts(newData.getType_of_accounts());
	            bfdb.setSegment(newData.getSegment());
	            bfdb.setPeriod(newData.getPeriod());
	            bfdb.setEffective_interest_rate(newData.getEffective_interest_rate());
	            bfdb.setBranch_name(newData.getBranch_name());
	            bfdb.setBranch_code(newData.getBranch_code());
	            bfdb.setReport_date(newData.getReport_date());
	            bfdb.setEntry_user(userid);
	            bfdb.setEntry_date(new Date());
	            bfdb.setModify_flg("Y");
	            bfdb.setDel_flg("N");

	            BFDB_Reps.save(bfdb);

	            msg = "BFDB Data Added Successfully";

	        } else if ("edit".equalsIgnoreCase(formmode)) {

	            // 1️⃣ Update General Master
	            GeneralMasterEntity gen = GeneralMasterRepos.getdataByAcc(newData.getAccount_no(), newData.getReport_date());
	            if (gen != null) {
	                gen.setSol_id(newData.getSol_id());
	                gen.setGender(newData.getGender());
	                gen.setCustomer_id(newData.getCustomer_id());
	                gen.setCustomer_name(newData.getCustomer_name());
	                gen.setSchm_code(newData.getSchm_code());
	                gen.setSchm_desc(newData.getSchm_desc());
	                gen.setAcct_open_date(newData.getAcct_open_date());
	                gen.setAcct_close_date(newData.getAcct_close_date());
	                gen.setBalance_as_on(newData.getBalance_as_on());
	                gen.setCurrency(newData.getCurrency());
	                gen.setBal_equi_to_bwp(newData.getBal_equi_to_bwp());
	                gen.setRate_of_interest(newData.getRate_of_interest());
	                gen.setHundred(newData.getHundred());
	                gen.setStatus(newData.getStatus());
	                gen.setMaturity_date(newData.getMaturity_date());
	                gen.setGl_sub_head_code(newData.getGl_sub_head_code());
	                gen.setGl_sub_head_desc(newData.getGl_sub_head_desc());
	                gen.setType_of_accounts(newData.getType_of_accounts());
	                gen.setSegment(newData.getSegment());
	                gen.setPeriod(newData.getPeriod());
	                gen.setEffective_interest_rate(newData.getEffective_interest_rate());
	                gen.setBranch_name(newData.getBranch_name());
	                gen.setBranch_code(newData.getBranch_code());
	                gen.setModify_user(userid);
	                gen.setModify_date(new Date());
	                gen.setModify_flg("Y");

	                GeneralMasterRepos.save(gen);
	            }

	            // 2️⃣ Update BFDB
	            BFDB_Entity bfdb = BFDB_Reps.getdataByAcc(newData.getAccount_no(), newData.getReport_date());
	            if (bfdb != null) {
	                bfdb.setSol_id(newData.getSol_id());
	                bfdb.setGender(newData.getGender());
	                bfdb.setCustomer_name(newData.getCustomer_name());
	                bfdb.setSchm_code(newData.getSchm_code());
	                bfdb.setSchm_desc(newData.getSchm_desc());
	                bfdb.setAcct_open_date(newData.getAcct_open_date());
	                bfdb.setAcct_close_date(newData.getAcct_close_date());
	                bfdb.setBalance_as_on(newData.getBalance_as_on());
	                bfdb.setCurrency(newData.getCurrency());
	                bfdb.setBal_equi_to_bwp(newData.getBal_equi_to_bwp());
	                bfdb.setRate_of_interest(newData.getRate_of_interest());
	                bfdb.setHundred(newData.getHundred());
	                bfdb.setStatus(newData.getStatus());
	                bfdb.setMaturity_date(newData.getMaturity_date());
	                bfdb.setGl_sub_head_code(newData.getGl_sub_head_code());
	                bfdb.setGl_sub_head_desc(newData.getGl_sub_head_desc());
	                bfdb.setType_of_accounts(newData.getType_of_accounts());
	                bfdb.setSegment(newData.getSegment());
	                bfdb.setPeriod(newData.getPeriod());
	                bfdb.setEffective_interest_rate(newData.getEffective_interest_rate());
	                bfdb.setBranch_name(newData.getBranch_name());
	                bfdb.setBranch_code(newData.getBranch_code());
	                bfdb.setModify_user(userid);
	                bfdb.setModify_date(new Date());
	                bfdb.setModify_flg("Y");

	                BFDB_Reps.save(bfdb);
	            }

	            msg = "BFDB Data Edited Successfully";

	        } else if ("delete".equalsIgnoreCase(formmode)) {
	            GeneralMasterEntity gen = GeneralMasterRepos.getdataByAcc(newData.getAccount_no(), newData.getReport_date());
	            if (gen != null)
	                GeneralMasterRepos.delete(gen);

	            BFDB_Entity bfdb = BFDB_Reps.getdataByAcc(newData.getAccount_no(), newData.getReport_date());
	            if (bfdb != null)
	            	BFDB_Reps.delete(bfdb);

	            msg = "BFDB Data Deleted Successfully";
	        }

	    } catch (Exception e) {
	        msg = "Error: " + e.getMessage();
	        logger.error("Exception in createAccount_BFDB: ", e);
	    }

	    return msg;
	}


}
