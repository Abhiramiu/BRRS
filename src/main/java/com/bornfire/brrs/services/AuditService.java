package com.bornfire.brrs.services;

import java.util.Date;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
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
	                GeneralMasterEntity existing = GeneralMasterRepos.getdataBybdgf(newData.getAccountNo(), newData.getReportDate());
	                
	                if (existing != null) {
	                    logger.info("MCBL Data Already Exist...");
	                    return "MCBL Data Already Exist, So can you Replace It";
	                } else {
	                    GeneralMasterEntity newAcc = new GeneralMasterEntity();
	                    //newAcc.setId(sequence.generateRequestUUId());
	                    newAcc.setMcblGlCode(newData.getMcblGlCode());
	                    newAcc.setGlSubHeadCode(newData.getGlSubHeadCode());
	                    newAcc.setCurrency(newData.getCurrency());
	                    newAcc.setAccountNo(newData.getAccountNo());
	                    newAcc.setMcblDescription(newData.getMcblDescription());
	                    newAcc.setMcblDebitBalance(newData.getMcblDebitBalance());
	                    newAcc.setMcblCreditBalance(newData.getMcblCreditBalance());
	                    newAcc.setMcblDebitEquivalent(newData.getMcblDebitEquivalent());
	                    newAcc.setMcblCreditEquivalent(newData.getMcblCreditEquivalent());
	                    newAcc.setReportDate(newData.getReportDate());
	                    newAcc.setEntryUser(userid);
	                    newAcc.setEntryTime(new Date());
	                    newAcc.setMcblFlg("Y");
	                    newAcc.setDelFlg("N");
	                    newAcc.setMcblFlg("Y");

	                    GeneralMasterRepos.save(newAcc);

	                    MCBL_Entity mcblNew = new MCBL_Entity();
	                    mcblNew.setId(sequence.generateRequestUUId());
	                    mcblNew.setMcbl_gl_code(newData.getMcblGlCode());
	                    mcblNew.setMcbl_gl_sub_code(newData.getGlSubHeadCode());
	                    mcblNew.setMcbl_head_acc_no(newData.getAccountNo());
	                    mcblNew.setMcbl_currency(newData.getCurrency());
	                    mcblNew.setMcbl_description(newData.getMcblDescription());
	                    mcblNew.setMcbl_debit_balance(newData.getMcblDebitBalance());
	                    mcblNew.setMcbl_credit_balance(newData.getMcblCreditBalance());
	                    mcblNew.setMcbl_debit_equivalent(newData.getMcblDebitEquivalent());
	                    mcblNew.setMcbl_credit_equivalent(newData.getMcblCreditEquivalent());
	                    mcblNew.setReport_date(newData.getReportDate());
	                    mcblNew.setEntry_user(userid);
	                    mcblNew.setEntry_date(new Date());
	                    mcblNew.setModify_flg("Y");
	                    mcblNew.setDelete_flg("N");

	                    MCBL_Reps.save(mcblNew);

	                    msg = "MCBL Data Added successfully";
	                }
	            }else if ("edit".equalsIgnoreCase(formmode)) {
	                GeneralMasterEntity newAcc = GeneralMasterRepos.getdataBybdgf(newData.getAccountNo(), newData.getReportDate());
	                
	                if (newAcc != null) {
	                    newAcc.setMcblGlCode(newData.getMcblGlCode());
	                    newAcc.setGlSubHeadCode(newData.getGlSubHeadCode());
	                    newAcc.setCurrency(newData.getCurrency());
	                    newAcc.setMcblDescription(newData.getMcblDescription());
	                    newAcc.setMcblDebitBalance(newData.getMcblDebitBalance());
	                    newAcc.setMcblCreditBalance(newData.getMcblCreditBalance());
	                    newAcc.setMcblDebitEquivalent(newData.getMcblDebitEquivalent());
	                    newAcc.setMcblCreditEquivalent(newData.getMcblCreditEquivalent());
	                    newAcc.setModifyUser(userid);
	                    newAcc.setMcblFlg("Y");
	                    newAcc.setDelFlg("N");
	                    newAcc.setMcblFlg("Y");

	                    GeneralMasterRepos.save(newAcc);

	                    MCBL_Entity mcblNew = MCBL_Reps.getdataByAcc(newData.getAccountNo(), newData.getReportDate());
	                    if (mcblNew != null) {
	                        mcblNew.setMcbl_gl_code(newData.getMcblGlCode());
		                    mcblNew.setMcbl_gl_sub_code(newData.getGlSubHeadCode());
		                    mcblNew.setMcbl_currency(newData.getCurrency());
		                    mcblNew.setMcbl_head_acc_no(newData.getAccountNo());
	                        mcblNew.setMcbl_description(newData.getMcblDescription());
	                        mcblNew.setMcbl_debit_balance(newData.getMcblDebitBalance());
	                        mcblNew.setMcbl_credit_balance(newData.getMcblCreditBalance());
	                        mcblNew.setMcbl_debit_equivalent(newData.getMcblDebitEquivalent());
	                        mcblNew.setMcbl_credit_equivalent(newData.getMcblCreditEquivalent());
	                        mcblNew.setModify_flg("Y");
	                        mcblNew.setDelete_flg("N");

	                        MCBL_Reps.save(mcblNew);
	                    }

	                    msg = "MCBL Data Edited Successfully";
	                } else {
	                    msg = "Error: MCBL Data not found for Account " + newData.getAccountNo();
	                }
				} else if ("delete".equalsIgnoreCase(formmode)) {
					GeneralMasterRepos.deleteById(newData.getSNO());
					MCBL_Entity data = MCBL_Reps.getdataByAcc(newData.getAccountNo(), newData.getReportDate());
					if (data != null) {
						MCBL_Reps.delete(data);
						msg = "Deleted successfully";
					} else {
						msg = "No record found for given account_no and date in MCBL Table";
					}

					msg = "MCBL Data deleted successfully";
				}

	    } catch (Exception e) {
	        msg = "Error: " + e.getMessage();
	    }

	    return msg;
	}
	
	public String createAccount_BLBF(String formmode, GeneralMasterEntity newData, String userid) {
	    String msg = null;
	    logger.info("Enter BLBF Account method for {} mode...", formmode);

	    try {System.out.println("DEBUG: account_no=" + newData.getAccountNo() + 
                " reportDate=" + newData.getReportDate());

	        // Fetch existing records
	        GeneralMasterEntity existingGM = GeneralMasterRepos.getdataBybfbl(newData.getAccountNo(), newData.getReportDate());
	        BLBF_Entity existingBLBF = BLBF_Reps.GetAll(newData.getAccountNo(), newData.getReportDate());

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

	                msg = "BLBF Data Added successfully";
	                break;

	            case "edit":
	                if (existingGM != null) {
	                    updateBLBFData(existingGM, newData, userid);
	                    GeneralMasterRepos.save(existingGM);
	                } else {
	                    return "Error: BLBF Data not found for Account " + newData.getAccountNo();
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
	                msg = "BLBF Data deleted successfully";
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
	    //gm.setId(sequence.generateRequestUUId());
	    gm.setAccountNo(data.getAccountNo());
	    gm.setCustomerId(data.getCustomerId());
	    gm.setSolId(data.getSolId());
	    gm.setCustomerName(data.getCustomerName());
	    gm.setSchmCode(data.getSchmCode());
	    gm.setSchmDesc(data.getSchmDesc());
	    gm.setAcctOpenDate(data.getAcctOpenDate());
	    gm.setAcctCloseDate(data.getAcctCloseDate());
	    gm.setApprovedLimit(data.getApprovedLimit());
	    gm.setSanctionLimit(data.getSanctionLimit());
	    gm.setDisbursedAmt(data.getDisbursedAmt());
	    gm.setBalanceAsOn(data.getBalanceAsOn());
	    
	    gm.setHundred(data.getHundred());
	    gm.setPeriod(data.getPeriod());
	    gm.setEffectiveInterestRate(data.getEffectiveInterestRate());
	    gm.setMatBucket(data.getMatBucket());
	    
	    gm.setCurrency(data.getCurrency());
	    gm.setBalEquiToBwp(data.getBalEquiToBwp());
	    gm.setRateOfInterest(data.getRateOfInterest());
	    gm.setAccruedIntAmt(data.getAccruedIntAmt());
	    gm.setMonthlyInterest(data.getMonthlyInterest());
	    gm.setLastInterestDebitDate(data.getLastInterestDebitDate());
	    gm.setAcctClsFlg(data.getAcctClsFlg());
	    gm.setGender(data.getGender());
	    gm.setClassificationCode(data.getClassificationCode());
	    gm.setConstitutionCode(data.getConstitutionCode());
	    gm.setMaturityDate(data.getMaturityDate());
	    gm.setGlSubHeadCode(data.getGlSubHeadCode());
	    gm.setGlSubHeadDesc(data.getGlSubHeadDesc());
	    gm.setTenorMonth(data.getTenorMonth());
	    gm.setEmi(data.getEmi());
	    gm.setSegment(data.getSegment());
	    gm.setFacility(data.getFacility());
	    gm.setPastDue(data.getPastDue());
	    gm.setPastDueDays(data.getPastDueDays());
	    gm.setAsset(data.getAsset());
	    gm.setProvision(data.getProvision());
	    gm.setUnsecured(data.getUnsecured());
	    gm.setIntBucket(data.getIntBucket());
	    gm.setStaff(data.getStaff());
	    gm.setSmme(data.getSmme());
	    gm.setLabod(data.getLabod());
	    gm.setNewAc(data.getNewAc());
	    gm.setUndrawn(data.getUndrawn());
	    gm.setSector(data.getSector());
	    gm.setStage(data.getStage());
	    gm.setEclProvision(data.getEclProvision());
	    gm.setBranchName(data.getBranchName());
	    gm.setBranchCode(data.getBranchCode());
	    gm.setReportDate(data.getReportDate());

	    gm.setEntryTime(new Date());
	    gm.setEntryUser(userid);

	    gm.setMcblFlg("Y");
	    gm.setBlbfFlg("Y");
	    gm.setDelFlg("N");

	    return gm;
	}

	private BLBF_Entity copyBLBFDataToBLBF(GeneralMasterEntity data, String userid) {
		BLBF_Entity blbf = new BLBF_Entity();

		blbf.setAccount_no(data.getAccountNo());
		blbf.setCustomer_id(data.getCustomerId());
		blbf.setSol_id(data.getSolId());
		blbf.setCustomer_name(data.getCustomerName());
		blbf.setSchm_code(data.getSchmCode());
		blbf.setSchm_desc(data.getSchmDesc());
		blbf.setAcct_open_date(data.getAcctOpenDate());
		blbf.setAcct_close_date(data.getAcctCloseDate());
		blbf.setApproved_limit(data.getApprovedLimit());
		blbf.setSanction_limit(data.getSanctionLimit());
		blbf.setDisbursed_amt(data.getDisbursedAmt());
		blbf.setBalance_as_on(data.getBalanceAsOn());
		blbf.setCurrency(data.getCurrency());

		blbf.setHundred(data.getHundred());
		blbf.setPeriod(data.getPeriod());
		blbf.setEffective_interest_rate(data.getEffectiveInterestRate());
		blbf.setMat_bucket(data.getMatBucket());

		blbf.setBal_equi_to_bwp(data.getBalEquiToBwp());
		blbf.setRate_of_interest(data.getRateOfInterest());
		blbf.setAccrued_int_amt(data.getAccruedIntAmt());
		blbf.setMonthly_interest(data.getMonthlyInterest());
		blbf.setLast_interest_debit_date(data.getLastInterestDebitDate());
		blbf.setAcct_cls_flg(data.getAcctClsFlg());
		blbf.setGender(data.getGender());
		blbf.setClassification_code(data.getClassificationCode());
		blbf.setConstitution_code(data.getConstitutionCode());
		blbf.setMaturity_date(data.getMaturityDate());
		blbf.setGl_sub_head_code(data.getGlSubHeadCode());
		blbf.setGl_sub_head_desc(data.getGlSubHeadDesc());
		blbf.setTenor_month(data.getTenorMonth());
		blbf.setEmi(data.getEmi());
		blbf.setSegment(data.getSegment());
		blbf.setFacility(data.getFacility());
		blbf.setPast_due(data.getPastDue());
		blbf.setPast_due_days(data.getPastDueDays());
		blbf.setAsset(data.getAsset());
		blbf.setProvision(data.getProvision());
		blbf.setUnsecured(data.getUnsecured());
		blbf.setInt_bucket(data.getIntBucket());
		blbf.setStaff(data.getStaff());
		blbf.setSmme(data.getSmme());
		blbf.setLabod(data.getLabod());
		blbf.setNew_ac(data.getNewAc());
		blbf.setUndrawn(data.getUndrawn());
		blbf.setSector(data.getSector());
		blbf.setStage(data.getStage());
		blbf.setEcl_provision(data.getEclProvision());
		blbf.setBranch_name(data.getBranchName());
		blbf.setBranch_code(data.getBranchCode());
		blbf.setReport_date(data.getReportDate());

		blbf.setEntry_date(new Date());
		blbf.setEntry_user(userid);

		return blbf;

	}

	private void updateBLBFData(GeneralMasterEntity existing, GeneralMasterEntity newData, String userid) {

	    existing.setCustomerId(newData.getCustomerId());
	    existing.setSolId(newData.getSolId());
	    existing.setCustomerName(newData.getCustomerName());
	    existing.setSchmCode(newData.getSchmCode());
	    existing.setSchmDesc(newData.getSchmDesc());
	    existing.setAcctOpenDate(newData.getAcctOpenDate());
	    existing.setAcctCloseDate(newData.getAcctCloseDate());
	    existing.setApprovedLimit(newData.getApprovedLimit());
	    existing.setSanctionLimit(newData.getSanctionLimit());
	    existing.setDisbursedAmt(newData.getDisbursedAmt());
	    existing.setBalanceAsOn(newData.getBalanceAsOn());
	    existing.setCurrency(newData.getCurrency());

	    existing.setHundred(newData.getHundred());
	    existing.setPeriod(newData.getPeriod());
	    existing.setEffectiveInterestRate(newData.getEffectiveInterestRate());
	    existing.setMatBucket(newData.getMatBucket());

	    existing.setBalEquiToBwp(newData.getBalEquiToBwp());
	    existing.setRateOfInterest(newData.getRateOfInterest());
	    existing.setAccruedIntAmt(newData.getAccruedIntAmt());
	    existing.setMonthlyInterest(newData.getMonthlyInterest());
	    existing.setLastInterestDebitDate(newData.getLastInterestDebitDate());
	    existing.setAcctClsFlg(newData.getAcctClsFlg());
	    existing.setGender(newData.getGender());
	    existing.setClassificationCode(newData.getClassificationCode());
	    existing.setConstitutionCode(newData.getConstitutionCode());
	    existing.setMaturityDate(newData.getMaturityDate());
	    existing.setGlSubHeadCode(newData.getGlSubHeadCode());
	    existing.setGlSubHeadDesc(newData.getGlSubHeadDesc());
	    existing.setTenorMonth(newData.getTenorMonth());
	    existing.setEmi(newData.getEmi());
	    existing.setSegment(newData.getSegment());
	    existing.setFacility(newData.getFacility());
	    existing.setPastDue(newData.getPastDue());
	    existing.setPastDueDays(newData.getPastDueDays());
	    existing.setAsset(newData.getAsset());
	    existing.setProvision(newData.getProvision());
	    existing.setUnsecured(newData.getUnsecured());
	    existing.setIntBucket(newData.getIntBucket());
	    existing.setStaff(newData.getStaff());
	    existing.setSmme(newData.getSmme());
	    existing.setLabod(newData.getLabod());
	    existing.setNewAc(newData.getNewAc());
	    existing.setUndrawn(newData.getUndrawn());
	    existing.setSector(newData.getSector());
	    existing.setStage(newData.getStage());
	    existing.setEclProvision(newData.getEclProvision());
	    existing.setBranchName(newData.getBranchName());
	    existing.setBranchCode(newData.getBranchCode());

	    existing.setModifyUser(userid);
	    existing.setModifyFlg("Y");
	    existing.setDelFlg("N");
	    existing.setBlbfFlg("Y");
	}


	private void updateBLBFData(BLBF_Entity existing, GeneralMasterEntity newData) {

	    existing.setCustomer_id(newData.getCustomerId());
	    existing.setSol_id(newData.getSolId());
	    existing.setCustomer_name(newData.getCustomerName());
	    existing.setSchm_code(newData.getSchmCode());
	    existing.setSchm_desc(newData.getSchmDesc());
	    existing.setAcct_open_date(newData.getAcctOpenDate());
	    existing.setAcct_close_date(newData.getAcctCloseDate());
	    existing.setApproved_limit(newData.getApprovedLimit());
	    existing.setSanction_limit(newData.getSanctionLimit());
	    existing.setDisbursed_amt(newData.getDisbursedAmt());
	    existing.setBalance_as_on(newData.getBalanceAsOn());
	    existing.setCurrency(newData.getCurrency());

	    existing.setHundred(newData.getHundred());
	    existing.setPeriod(newData.getPeriod());
	    existing.setEffective_interest_rate(newData.getEffectiveInterestRate());
	    existing.setMat_bucket(newData.getMatBucket());

	    existing.setBal_equi_to_bwp(newData.getBalEquiToBwp());
	    existing.setRate_of_interest(newData.getRateOfInterest());
	    existing.setAccrued_int_amt(newData.getAccruedIntAmt());
	    existing.setMonthly_interest(newData.getMonthlyInterest());
	    existing.setLast_interest_debit_date(newData.getLastInterestDebitDate());
	    existing.setAcct_cls_flg(newData.getAcctClsFlg());
	    existing.setGender(newData.getGender());
	    existing.setClassification_code(newData.getClassificationCode());
	    existing.setConstitution_code(newData.getConstitutionCode());
	    existing.setMaturity_date(newData.getMaturityDate());
	    existing.setGl_sub_head_code(newData.getGlSubHeadCode());
	    existing.setGl_sub_head_desc(newData.getGlSubHeadDesc());
	    existing.setTenor_month(newData.getTenorMonth());
	    existing.setEmi(newData.getEmi());
	    existing.setSegment(newData.getSegment());
	    existing.setFacility(newData.getFacility());
	    existing.setPast_due(newData.getPastDue());
	    existing.setPast_due_days(newData.getPastDueDays());
	    existing.setAsset(newData.getAsset());
	    existing.setProvision(newData.getProvision());
	    existing.setUnsecured(newData.getUnsecured());
	    existing.setInt_bucket(newData.getIntBucket());
	    existing.setStaff(newData.getStaff());
	    existing.setSmme(newData.getSmme());
	    existing.setLabod(newData.getLabod());
	    existing.setNew_ac(newData.getNewAc());
	    existing.setUndrawn(newData.getUndrawn());
	    existing.setSector(newData.getSector());
	    existing.setStage(newData.getStage());
	    existing.setEcl_provision(newData.getEclProvision());
	}



	public String createAccount_BDGF(String formmode, GeneralMasterEntity newData, String userid) {
	    String msg = null;
	    logger.info("Enter BDGF Account method for {} mode...", formmode);

	    try {
	        System.out.println("DEBUG: account_no=" + newData.getAccountNo() + 
	                " reportDate=" + newData.getReportDate());

	        // Fetch existing records
	        GeneralMasterEntity existingGM = GeneralMasterRepos.getdataBybdgf(newData.getAccountNo(), newData.getReportDate());
	        BDGF_Entity existingBDGF = BDGF_Reps.getdataBybdgf(newData.getAccountNo(), newData.getReportDate());

	        switch (formmode.toLowerCase()) {
	            case "add":
	                if (existingGM != null) {
	                    return "BDGF Data Already Exist, so can you Replace It";
	                }

	                // Create new GeneralMasterEntity
	                GeneralMasterEntity gm = copyBDGFDataToGeneralMaster(newData, userid);
	                GeneralMasterRepos.save(gm);

	                // Create new BDGF_Entity
	                BDGF_Entity bdgf = copyBDGFDataToBDGF(newData, userid);
	                BDGF_Reps.save(bdgf);

	                msg = "BDGF Data Added Successfully";
	                break;

	            case "edit":
	                if (existingGM != null) {
	                    updateBDGFData(existingGM, newData, userid);
	                    GeneralMasterRepos.save(existingGM);
	                } else {
	                    return "Error: BDGF Data not found for Account " + newData.getAccountNo();
	                }

	                if (existingBDGF != null) {
	                    updateBDGFData(existingBDGF, newData, userid);
	                    BDGF_Reps.save(existingBDGF);
	                }

	                msg = "BDGF Data Edited Successfully";
	                break;

	            case "delete":
	                if (existingGM != null) GeneralMasterRepos.delete(existingGM);
	                if (existingBDGF != null) BDGF_Reps.delete(existingBDGF);
	                msg = "BDGF Data deleted successfully";
	                break;

	            default:
	                msg = "Unknown form mode: " + formmode;
	        }

	    } catch (Exception e) {
	        logger.error("Error while processing BDGF: {}", e.getMessage(), e);
	        msg = "Error: " + e.getMessage();
	    }

	    return msg;
	}

	// Helper Methods
	private GeneralMasterEntity copyBDGFDataToGeneralMaster(GeneralMasterEntity data, String userid) {
	    GeneralMasterEntity gm = new GeneralMasterEntity();

	    //gm.setId(sequence.generateRequestUUId());
	    gm.setSolId(data.getSolId());
	    gm.setAccountNo(data.getAccountNo());
	    gm.setCustomerId(data.getCustomerId());
	    gm.setCustomerName(data.getCustomerName());
	    gm.setAcctOpenDate(data.getAcctOpenDate());
	    gm.setAmountDeposited(data.getAmountDeposited());
	    gm.setCurrency(data.getCurrency());
	    gm.setPeriod(data.getPeriod());
	    gm.setRateOfInterest(data.getRateOfInterest());
	    gm.setHundred(data.getHundred());
	    gm.setBalEquiToBwp(data.getBalEquiToBwp());
	    gm.setOutstandingBalance(data.getOutstandingBalance());
	    gm.setOustndngBalUgx(data.getOustndngBalUgx());
	    gm.setMaturityDate(data.getMaturityDate());
	    gm.setMaturityAmount(data.getMaturityAmount());
	    gm.setScheme(data.getScheme());
	    gm.setCrPrefIntRate(data.getCrPrefIntRate());
	    gm.setSegment(data.getSegment());
	    gm.setReferenceDate(data.getReferenceDate());
	    gm.setDifference(data.getDifference());
	    gm.setDays(data.getDays());
	    gm.setPeriodDays(data.getPeriodDays());
	    gm.setEffectiveInterestRate(data.getEffectiveInterestRate());
	    gm.setBranchName(data.getBranchName());
	    gm.setBranchCode(data.getBranchCode());
	    gm.setReportDate(data.getReportDate());
	    gm.setEntryTime(new Date());
	    gm.setEntryUser(userid);

	    gm.setModifyFlg("Y");
	    gm.setDelFlg("N");
	    gm.setBdgfFlg("Y");

	    return gm;
	}


	private BDGF_Entity copyBDGFDataToBDGF(GeneralMasterEntity data, String userid) {
	    BDGF_Entity bdgf = new BDGF_Entity();

	    bdgf.setSol_id(data.getSolId());
	    bdgf.setAccount_no(data.getAccountNo());
	    bdgf.setCustomer_id(data.getCustomerId());
	    bdgf.setCustomer_name(data.getCustomerName());
	    bdgf.setAcct_open_date(data.getAcctOpenDate());
	    bdgf.setAmount_deposited(data.getAmountDeposited());
	    bdgf.setCurrency(data.getCurrency());
	    bdgf.setPeriod(data.getPeriod());
	    bdgf.setRate_of_interest(data.getRateOfInterest());
	    bdgf.setHundred(data.getHundred());
	    bdgf.setBal_equi_to_bwp(data.getBalEquiToBwp());
	    bdgf.setOutstanding_balance(data.getOutstandingBalance());
	    bdgf.setOustndng_bal_ugx(data.getOustndngBalUgx());
	    bdgf.setMaturity_date(data.getMaturityDate());
	    bdgf.setMaturity_amount(data.getMaturityAmount());
	    bdgf.setScheme(data.getScheme());
	    bdgf.setCr_pref_int_rate(data.getCrPrefIntRate());
	    bdgf.setSegment(data.getSegment());
	    bdgf.setReference_date(data.getReferenceDate());
	    bdgf.setDifference(data.getDifference());
	    bdgf.setDays(data.getDays());
	    bdgf.setPeriod_days(data.getPeriodDays());
	    bdgf.setEffective_interest_rate(data.getEffectiveInterestRate());
	    bdgf.setBranch_name(data.getBranchName());
	    bdgf.setBranch_code(data.getBranchCode());
	    bdgf.setReport_date(data.getReportDate());

	    bdgf.setEntry_date(new Date());
	    bdgf.setEntry_user(userid);
	    bdgf.setModify_flg("Y");
	    bdgf.setDel_flg("N");

	    return bdgf;
	}


	private void updateBDGFData(GeneralMasterEntity existing, GeneralMasterEntity newData, String userid) {

	    existing.setSolId(newData.getSolId());
	    existing.setCustomerId(newData.getCustomerId());
	    existing.setCustomerName(newData.getCustomerName());
	    existing.setAcctOpenDate(newData.getAcctOpenDate());
	    existing.setAmountDeposited(newData.getAmountDeposited());
	    existing.setCurrency(newData.getCurrency());
	    existing.setPeriod(newData.getPeriod());
	    existing.setRateOfInterest(newData.getRateOfInterest());
	    existing.setHundred(newData.getHundred());
	    existing.setBalEquiToBwp(newData.getBalEquiToBwp());
	    existing.setOutstandingBalance(newData.getOutstandingBalance());
	    existing.setOustndngBalUgx(newData.getOustndngBalUgx());
	    existing.setMaturityDate(newData.getMaturityDate());
	    existing.setMaturityAmount(newData.getMaturityAmount());
	    existing.setScheme(newData.getScheme());
	    existing.setCrPrefIntRate(newData.getCrPrefIntRate());
	    existing.setSegment(newData.getSegment());
	    existing.setReferenceDate(newData.getReferenceDate());
	    existing.setDifference(newData.getDifference());
	    existing.setDays(newData.getDays());
	    existing.setPeriodDays(newData.getPeriodDays());
	    existing.setEffectiveInterestRate(newData.getEffectiveInterestRate());
	    existing.setBranchName(newData.getBranchName());
	    existing.setBranchCode(newData.getBranchCode());

	    existing.setModifyUser(userid);
	    existing.setModifyFlg("Y");
	    existing.setDelFlg("N");
	    existing.setBdgfFlg("Y");
	}


	private void updateBDGFData(BDGF_Entity existing, GeneralMasterEntity newData, String userid) {

	    existing.setSol_id(newData.getSolId());
	    existing.setCustomer_id(newData.getCustomerId());
	    existing.setCustomer_name(newData.getCustomerName());
	    existing.setAcct_open_date(newData.getAcctOpenDate());
	    existing.setAmount_deposited(newData.getAmountDeposited());
	    existing.setCurrency(newData.getCurrency());
	    existing.setPeriod(newData.getPeriod());
	    existing.setRate_of_interest(newData.getRateOfInterest());
	    existing.setHundred(newData.getHundred());
	    existing.setBal_equi_to_bwp(newData.getBalEquiToBwp());
	    existing.setOutstanding_balance(newData.getOutstandingBalance());
	    existing.setOustndng_bal_ugx(newData.getOustndngBalUgx());
	    existing.setMaturity_date(newData.getMaturityDate());
	    existing.setMaturity_amount(newData.getMaturityAmount());
	    existing.setScheme(newData.getScheme());
	    existing.setCr_pref_int_rate(newData.getCrPrefIntRate());
	    existing.setSegment(newData.getSegment());
	    existing.setReference_date(newData.getReferenceDate());
	    existing.setDifference(newData.getDifference());
	    existing.setDays(newData.getDays());
	    existing.setPeriod_days(newData.getPeriodDays());
	    existing.setEffective_interest_rate(newData.getEffectiveInterestRate());
	    existing.setBranch_name(newData.getBranchName());
	    existing.setBranch_code(newData.getBranchCode());
	    existing.setModify_user(userid);
	    existing.setModify_flg("Y");
	    existing.setDel_flg("N");
	}



	public String createAccount_BFDB(String formmode, GeneralMasterEntity newData, String userid) {
	    String msg = null;
	    logger.info("Enter BFDB Account method for {} mode...", formmode);

	    try {
	        // Fetch existing records
	        GeneralMasterEntity existingGM = GeneralMasterRepos.getdataBybdgf(newData.getAccountNo(), newData.getReportDate());
	        BFDB_Entity existingBFDB = BFDB_Reps.getdataByAcc(newData.getAccountNo(), newData.getReportDate());

	        switch (formmode.toLowerCase()) {
	            case "add":
	                if (existingGM != null) {
	                    return "BFDB Data Already Exist, Do you want to replace it?";
	                }

	                // Create new GeneralMasterEntity
	                GeneralMasterEntity gm = copyBFDBDataToGeneralMaster(newData, userid);
	                GeneralMasterRepos.save(gm);

	                // Create new BFDB_Entity
	                BFDB_Entity bfdb = copyBFDBDataToBFDB(newData, userid);
	                BFDB_Reps.save(bfdb);

	                msg = "BFDB Data Added Successfully";
	                break;

	            case "edit":
	                if (existingGM != null) {
	                    updateBFDBData(existingGM, newData, userid);
	                    GeneralMasterRepos.save(existingGM);
	                } else {
	                    return "Error: BFDB Data not found for Account " + newData.getAccountNo();
	                }

	                if (existingBFDB != null) {
	                    updateBFDBData(existingBFDB, newData, userid);
	                    BFDB_Reps.save(existingBFDB);
	                }

	                msg = "BFDB Data Edited Successfully";
	                break;

	            case "delete":
	                if (existingGM != null) GeneralMasterRepos.delete(existingGM);
	                if (existingBFDB != null) BFDB_Reps.delete(existingBFDB);
	                msg = "BFDB Data Deleted Successfully";
	                break;

	            default:
	                msg = "Unknown form mode: " + formmode;
	        }

	    } catch (Exception e) {
	        logger.error("Error while processing BFDB: {}", e.getMessage(), e);
	        msg = "Error: " + e.getMessage();
	    }

	    return msg;
	}
	private GeneralMasterEntity copyBFDBDataToGeneralMaster(GeneralMasterEntity data, String userid) {
	    GeneralMasterEntity gm = new GeneralMasterEntity();
	   // gm.setId(sequence.generateRequestUUId());
	    gm.setSolId(data.getSolId());
	    gm.setGender(data.getGender());
	    gm.setAccountNo(data.getAccountNo());
	    gm.setCustomerId(data.getCustomerId());
	    gm.setCustomerName(data.getCustomerName());
	    gm.setSchmCode(data.getSchmCode());
	    gm.setSchmDesc(data.getSchmDesc());
	    gm.setAcctOpenDate(data.getAcctOpenDate());
	    gm.setAcctCloseDate(data.getAcctCloseDate());
	    gm.setBalanceAsOn(data.getBalanceAsOn());
	    gm.setCurrency(data.getCurrency());
	    gm.setBalEquiToBwp(data.getBalEquiToBwp());
	    gm.setRateOfInterest(data.getRateOfInterest());
	    gm.setHundred(data.getHundred());
	    gm.setStatus(data.getStatus());
	    gm.setMaturityDate(data.getMaturityDate());
	    gm.setGlSubHeadCode(data.getGlSubHeadCode());
	    gm.setGlSubHeadDesc(data.getGlSubHeadDesc());
	    gm.setTypeOfAccounts(data.getTypeOfAccounts());
	    gm.setSegment(data.getSegment());
	    gm.setPeriod(data.getPeriod());
	    gm.setEffectiveInterestRate(data.getEffectiveInterestRate());
	    gm.setBranchName(data.getBranchName());
	    gm.setBranchCode(data.getBranchCode());
	    gm.setReportDate(data.getReportDate());
	    gm.setEntryUser(userid);
	    gm.setEntryTime(new Date());
	    gm.setModifyFlg("Y");
	    gm.setDelFlg("N");
	    gm.setBfdbFlg("Y");
	    return gm;
	}

	private BFDB_Entity copyBFDBDataToBFDB(GeneralMasterEntity data, String userid) {
	    BFDB_Entity bfdb = new BFDB_Entity();
	    bfdb.setSol_id(data.getSolId());
	    bfdb.setGender(data.getGender());
	    bfdb.setAccount_no(data.getAccountNo());
	    bfdb.setCustomer_id(data.getCustomerId());
	    bfdb.setCustomer_name(data.getCustomerName());
	    bfdb.setSchm_code(data.getSchmCode());
	    bfdb.setSchm_desc(data.getSchmDesc());
	    bfdb.setAcct_open_date(data.getAcctOpenDate());
	    bfdb.setAcct_close_date(data.getAcctCloseDate());
	    bfdb.setBalance_as_on(data.getBalanceAsOn());
	    bfdb.setCurrency(data.getCurrency());
	    bfdb.setBal_equi_to_bwp(data.getBalEquiToBwp());
	    bfdb.setRate_of_interest(data.getRateOfInterest());
	    bfdb.setHundred(data.getHundred());
	    bfdb.setStatus(data.getStatus());
	    bfdb.setMaturity_date(data.getMaturityDate());
	    bfdb.setGl_sub_head_code(data.getGlSubHeadCode());
	    bfdb.setGl_sub_head_desc(data.getGlSubHeadDesc());
	    bfdb.setType_of_accounts(data.getTypeOfAccounts());
	    bfdb.setSegment(data.getSegment());
	    bfdb.setPeriod(data.getPeriod());
	    bfdb.setEffective_interest_rate(data.getEffectiveInterestRate());
	    bfdb.setBranch_name(data.getBranchName());
	    bfdb.setBranch_code(data.getBranchCode());
	    bfdb.setReport_date(data.getReportDate());
	    bfdb.setEntry_user(userid);
	    bfdb.setEntry_date(new Date());
	    bfdb.setModify_flg("Y");
	    bfdb.setDel_flg("N");
	    return bfdb;
	}

	private void updateBFDBData(GeneralMasterEntity existing, GeneralMasterEntity newData, String userid) {
	    existing.setSolId(newData.getSolId());
	    existing.setGender(newData.getGender());
	    existing.setCustomerId(newData.getCustomerId());
	    existing.setCustomerName(newData.getCustomerName());
	    existing.setSchmCode(newData.getSchmCode());
	    existing.setSchmDesc(newData.getSchmDesc());
	    existing.setAcctOpenDate(newData.getAcctOpenDate());
	    existing.setAcctCloseDate(newData.getAcctCloseDate());
	    existing.setBalanceAsOn(newData.getBalanceAsOn());
	    existing.setCurrency(newData.getCurrency());
	    existing.setBalEquiToBwp(newData.getBalEquiToBwp());
	    existing.setRateOfInterest(newData.getRateOfInterest());
	    existing.setHundred(newData.getHundred());
	    existing.setStatus(newData.getStatus());
	    existing.setMaturityDate(newData.getMaturityDate());
	    existing.setGlSubHeadCode(newData.getGlSubHeadCode());
	    existing.setGlSubHeadDesc(newData.getGlSubHeadDesc());
	    existing.setTypeOfAccounts(newData.getTypeOfAccounts());
	    existing.setSegment(newData.getSegment());
	    existing.setPeriod(newData.getPeriod());
	    existing.setEffectiveInterestRate(newData.getEffectiveInterestRate());
	    existing.setBranchName(newData.getBranchName());
	    existing.setBranchCode(newData.getBranchCode());
	    existing.setModifyUser(userid);
	    existing.setModifyTime(new Date());
	    existing.setModifyFlg("Y");
	    existing.setDelFlg("N");
	    existing.setBfdbFlg("Y");
	}


	private void updateBFDBData(BFDB_Entity existing, GeneralMasterEntity newData, String userid) {
	    existing.setSol_id(newData.getSolId());
	    existing.setGender(newData.getGender());
	    existing.setCustomer_name(newData.getCustomerName());
	    existing.setSchm_code(newData.getSchmCode());
	    existing.setSchm_desc(newData.getSchmDesc());
	    existing.setAcct_open_date(newData.getAcctOpenDate());
	    existing.setAcct_close_date(newData.getAcctCloseDate());
	    existing.setBalance_as_on(newData.getBalanceAsOn());
	    existing.setCurrency(newData.getCurrency());
	    existing.setBal_equi_to_bwp(newData.getBalEquiToBwp());
	    existing.setRate_of_interest(newData.getRateOfInterest());
	    existing.setHundred(newData.getHundred());
	    existing.setStatus(newData.getStatus());
	    existing.setMaturity_date(newData.getMaturityDate());
	    existing.setGl_sub_head_code(newData.getGlSubHeadCode());
	    existing.setGl_sub_head_desc(newData.getGlSubHeadDesc());
	    existing.setType_of_accounts(newData.getTypeOfAccounts());
	    existing.setSegment(newData.getSegment());
	    existing.setPeriod(newData.getPeriod());
	    existing.setEffective_interest_rate(newData.getEffectiveInterestRate());
	    existing.setBranch_name(newData.getBranchName());
	    existing.setBranch_code(newData.getBranchCode());
	    existing.setModify_user(userid);
	    existing.setModify_date(new Date());
	    existing.setModify_flg("Y");
	    existing.setDel_flg("N");
	}

}
