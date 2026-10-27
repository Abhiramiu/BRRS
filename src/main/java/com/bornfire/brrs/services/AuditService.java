package com.bornfire.brrs.services;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
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

	public List<AuditServicesEntity> getModifyStatus() {
		List<AuditServicesEntity> auditList = auditServicesRep.getModifyStatus();

		System.out.println("Total MODIFY records: " + auditList.size());

		auditList.forEach(audit -> System.out.println("SNO: " + audit.getSno() + ", FUNC_CODE: " + audit.getFunc_code()
				+ ", TABLE: " + audit.getAudit_table()));

		return auditList;
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
			// audit.setAudit_ref_no(auditID.toString());
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
				GeneralMasterEntity existing = GeneralMasterRepos.getdataBybdgf(newData.getAccountNo(),
						newData.getReportDate());

				if (existing != null) {
					logger.info("MCBL Data Already Exist...");
					return "MCBL Data Already Exist, So can you Replace It";
				} else {
					GeneralMasterEntity newAcc = new GeneralMasterEntity();
					// newAcc.setId(sequence.generateRequestUUId());
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
			} else if ("edit".equalsIgnoreCase(formmode)) {
				GeneralMasterEntity newAcc = GeneralMasterRepos.getdataBybdgf(newData.getAccountNo(),
						newData.getReportDate());

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

		try {
			System.out
					.println("DEBUG: account_no=" + newData.getAccountNo() + " reportDate=" + newData.getReportDate());

			// Fetch existing records
			GeneralMasterEntity existingGM = GeneralMasterRepos.getdataBybfbl(newData.getAccountNo(),
					newData.getReportDate());
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
				if (existingGM != null)
					GeneralMasterRepos.delete(existingGM);
				if (existingBLBF != null)
					BLBF_Reps.delete(existingBLBF);
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
		// gm.setId(sequence.generateRequestUUId());
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
			System.out
					.println("DEBUG: account_no=" + newData.getAccountNo() + " reportDate=" + newData.getReportDate());

			// Fetch existing records
			GeneralMasterEntity existingGM = GeneralMasterRepos.getdataBybdgf(newData.getAccountNo(),
					newData.getReportDate());
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
				if (existingGM != null)
					GeneralMasterRepos.delete(existingGM);
				if (existingBDGF != null)
					BDGF_Reps.delete(existingBDGF);
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

		// gm.setId(sequence.generateRequestUUId());
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
			GeneralMasterEntity existingGM = GeneralMasterRepos.getdataBybdgf(newData.getAccountNo(),
					newData.getReportDate());
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
				if (existingGM != null)
					GeneralMasterRepos.delete(existingGM);
				if (existingBFDB != null)
					BFDB_Reps.delete(existingBFDB);
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

	@PersistenceContext
	EntityManager entityManager;

	public <T> void compareEntities(T oldEntity, T newEntity, String Screenname, String tableName) {
		System.out.println("Screen Name: " + Screenname);

		if (oldEntity == null || newEntity == null) {
			throw new IllegalArgumentException("Entities cannot be null");
		}

		// Check if both entities are of the same type
		if (!oldEntity.getClass().equals(newEntity.getClass())) {
			throw new IllegalArgumentException(
					"Entities must be of the same type. " + "Old entity type: " + oldEntity.getClass().getSimpleName()
							+ ", New entity type: " + newEntity.getClass().getSimpleName());
		}
		List<String> ignoreFields = Arrays.asList("createdAt", "updatedAt", "modifiedAt", "createdBy", "modifiedBy",
				"lastModifiedDate", "lastModifiedBy", "version");
		List<String> lowerCaseIgnoreFields = new ArrayList<String>();
		if (ignoreFields != null) {
			for (String field : ignoreFields) {
				lowerCaseIgnoreFields.add(field.toLowerCase());
			}
		}

		// Extract and print Primary Key
		EntityType<?> entityType = entityManager.getMetamodel().entity(newEntity.getClass());
		List<String> idNames = new ArrayList<String>();

		if (entityType.hasSingleIdAttribute()) {
			// Single @Id
			for (SingularAttribute<?, ?> attribute : entityType.getSingularAttributes()) {
				if (attribute.isId()) {
					idNames.add(attribute.getName());
					break;
				}
			}
		} else {
			// Composite @IdClass
			for (Object attrObj : entityType.getIdClassAttributes()) {
				SingularAttribute<?, ?> attribute = (SingularAttribute<?, ?>) attrObj;
				idNames.add(attribute.getName());
			}
		}

		// Get the ID value
		Object idValue = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(newEntity);

		System.out.println("--- Extracting Composite Key Values ---");
		String formattedIdString = "";

		if (idValue != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

			if (entityType.hasSingleIdAttribute()) {
				// Single Primary Key
				String singleIdName = idNames.get(0);
				String displayName = singleIdName.replace('_', ' ');
				displayName = displayName.substring(0, 1).toUpperCase() + displayName.substring(1);
				String displayValue = (idValue instanceof Date) ? sdf.format((Date) idValue) : idValue.toString();
				formattedIdString = displayName + " : " + displayValue;
			} else {
				// Composite Primary Key
				StringJoiner joiner = new StringJoiner(";");
				Field[] fields = idValue.getClass().getDeclaredFields();

				for (Field field : fields) {
					field.setAccessible(true);
					try {
						String fieldName = field.getName();
						Object fieldValue = field.get(idValue);

						if (!fieldName.equals("serialVersionUID")) {
							String displayName = fieldName.replace('_', ' ');
							displayName = displayName.substring(0, 1).toUpperCase() + displayName.substring(1);
							Object displayValue = (fieldValue instanceof Date) ? sdf.format((Date) fieldValue)
									: fieldValue;
							joiner.add(displayName + " : " + displayValue);
						}
					} catch (IllegalAccessException e) {
						System.err.println("Could not read field: " + field.getName());
					}
				}
				formattedIdString = joiner.toString();
			}
		}

		System.out.println("ID values : " + formattedIdString);

		// Compare entities and build changes string
		StringBuilder changes = new StringBuilder();
		Class<?> clazz = oldEntity.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			// Skip if field is in ignore list (case-insensitive)
			if (lowerCaseIgnoreFields.contains(field.getName().toLowerCase())) {
				continue;
			}

			field.setAccessible(true);
			try {
				Object oldValue = field.get(oldEntity);
				Object newValue = field.get(newEntity);

				if (!areEqual(oldValue, newValue)) {
					if (changes.length() > 0) {
						changes.append("|||");
					}
					changes.append(field.getName()).append(": OldValue: ").append(oldValue).append(", NewValue: ")
							.append(newValue);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Error accessing field: " + field.getName(), e);
			}
		}

		System.out.println("Changes in Audit : " + changes);

		System.out.println("Entered Dynamic  Modify Service Audit");
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
		// audit.setAudit_ref_no(auditID.toString());
		audit.setAudit_date(currentDate);
		audit.setEntry_time(currentDate);
		audit.setEntry_user(userId);
		audit.setEntry_user_name(username);
		audit.setFunc_code("MODIFY");
		audit.setAudit_table(tableName);
		audit.setAudit_screen(Screenname);
		audit.setEvent_id(userId);
		audit.setEvent_name(username);
		audit.setModi_details(changes.toString());
		audit.setChange_details(formattedIdString);

		auditServicesRep.save(audit);

	}

	public <T> void compareEntitiesmanual(T oldEntity, T newEntity, String id_values, String Screenname,
			String tableName) {
		System.out.println("Screen Name: " + Screenname);

		if (oldEntity == null || newEntity == null) {
			throw new IllegalArgumentException("Entities cannot be null");
		}

		// Check if both entities are of the same type
		if (!oldEntity.getClass().equals(newEntity.getClass())) {
			throw new IllegalArgumentException(
					"Entities must be of the same type. " + "Old entity type: " + oldEntity.getClass().getSimpleName()
							+ ", New entity type: " + newEntity.getClass().getSimpleName());
		}
		List<String> ignoreFields = Arrays.asList("createdAt", "updatedAt", "modifiedAt", "createdBy", "modifiedBy",
				"lastModifiedDate", "lastModifiedBy", "version");
		List<String> lowerCaseIgnoreFields = new ArrayList<String>();
		if (ignoreFields != null) {
			for (String field : ignoreFields) {
				lowerCaseIgnoreFields.add(field.toLowerCase());
			}
		}

		// Compare entities and build changes string
		StringBuilder changes = new StringBuilder();
		Class<?> clazz = oldEntity.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			// Skip if field is in ignore list (case-insensitive)
			if (lowerCaseIgnoreFields.contains(field.getName().toLowerCase())) {
				continue;
			}

			field.setAccessible(true);
			try {
				Object oldValue = field.get(oldEntity);
				Object newValue = field.get(newEntity);

				if (!areEqual(oldValue, newValue)) {
					if (changes.length() > 0) {
						changes.append("|||");
					}
					changes.append(field.getName()).append(": OldValue: ").append(oldValue).append(", NewValue: ")
							.append(newValue);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Error accessing field: " + field.getName(), e);
			}
		}

		System.out.println("Changes in Audit : " + changes);

		System.out.println("Entered Dynamic  Modify Service Audit");
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
		// audit.setAudit_ref_no(auditID.toString());
		audit.setAudit_date(currentDate);
		audit.setEntry_time(currentDate);
		audit.setEntry_user(userId);
		audit.setEntry_user_name(username);
		audit.setFunc_code("MODIFY");
		audit.setAudit_table(tableName);
		audit.setAudit_screen(Screenname);
		audit.setEvent_id(userId);
		audit.setEvent_name(username);
		audit.setModi_details(changes.toString());
		audit.setChange_details(id_values);

		auditServicesRep.save(audit);

	}

	private boolean areEqual(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null)
			return true;
		if (obj1 == null || obj2 == null)
			return false;
		return obj1.equals(obj2);
	}

	public <T> String getChanges(T oldEntity, T newEntity) {

		StringBuilder changes = new StringBuilder();

		Class<?> clazz = oldEntity.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {

			field.setAccessible(true);

			try {
				Object oldValue = field.get(oldEntity);
				Object newValue = field.get(newEntity);

				if (!areEqual(oldValue, newValue)) {

					if (changes.length() > 0) {
						changes.append("|||");
					}

					changes.append(field.getName()).append(": OldValue: ").append(oldValue).append(", NewValue: ")
							.append(newValue);
				}

			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		return changes.toString();
	}

//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//
//	public AuditServicesEntity findById(Long id) {
//		Optional<AuditServicesEntity> auditData = auditServicesRep.findById(id);
//		return auditData.orElse(null);
//	}
//
//	@Transactional
//	public void verifyAndTransferColumn(Long auditId, String columnName) {
//
//		AuditServicesEntity auditRecord = findById(auditId);
//
//		if (auditRecord == null) {
//			throw new IllegalArgumentException("Audit record not found for ID: " + auditId);
//		}
//
//		// Block if the whole audit record is already finalized/verified
//		if (auditRecord.getAuth_user() != null && !auditRecord.getAuth_user().trim().isEmpty()) {
//			throw new IllegalStateException("This audit record has already been fully verified.");
//		}
//
//		if (auditRecord.getFunc_code() == null || !"MODIFY".equalsIgnoreCase(auditRecord.getFunc_code())) {
//			throw new IllegalStateException(
//					"Only MODIFY records can be verified. Current Function: " + auditRecord.getFunc_code());
//		}
//
//		String auditTable = auditRecord.getAudit_table();
//		if (auditTable == null || auditTable.trim().isEmpty()) {
//			throw new IllegalStateException("Audit table name missing for ID: " + auditId);
//		}
//
//		String upperTable = auditTable.trim().toUpperCase();
//
//		// Transfer data for this specific column
//		if (upperTable.contains("SFINP2")) {
//			transferSfinp2ColumnData(auditRecord, columnName);
//		} else if (upperTable.contains("FAS")) {
//			transferFASColumnData(auditRecord, columnName);
//		} else {
//			throw new IllegalArgumentException("Verification not configured for table: " + auditTable);
//		}
//
//		// NOTE: We removed setting auth_user here so other columns can still be
//		// verified!
//		System.out.println("Column " + columnName + " for Audit Record ID " + auditId + " transferred successfully.");
//	}
//
//	@Transactional
//	public void finalizeAuditRecord(Long auditId) {
//		AuditServicesEntity auditRecord = findById(auditId);
//		if (auditRecord == null) {
//			throw new IllegalArgumentException("Audit record not found for ID: " + auditId);
//		}
//
//		// Update authentication details to lock the record
//		try {
//			String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
//			auditRecord.setAuth_user(currentPrincipalName);
//		} catch (Exception e) {
//			auditRecord.setAuth_user("SYSTEM_VERIFIER");
//		}
//
//		auditRecord.setAuth_time(new Date());
//		auditServicesRep.save(auditRecord);
//		System.out.println("Audit Record ID " + auditId + " fully finalized and verified.");
//	}
//
//	private void transferSfinp2ColumnData(AuditServicesEntity auditRecord, String columnName) {
//		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SFINP2_MOD_SUMMARYTABLE";
//		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
//
//		if (reportDate == null) {
//			throw new IllegalStateException("No modified SFINP2 record available for verification.");
//		}
//
//		// Validate column name against allowed list to prevent SQL injection
//		if (!isValidSfinp2Column(columnName)) {
//			throw new IllegalArgumentException("Invalid or unauthorized column name for SFINP2: " + columnName);
//		}
//
//		// Dynamic single-column update query
//		String updateSql = "UPDATE BRRS_M_SFINP2_SUMMARYTABLE M " + "SET " + columnName + " = (SELECT S." + columnName
//				+ " FROM BRRS_M_SFINP2_MOD_SUMMARYTABLE S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) "
//				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
//
//		int inserted = jdbcTemplate.update(updateSql, reportDate);
//
//		if (inserted == 0) {
//			throw new IllegalStateException("SFINP2 modification record not found for date : " + reportDate);
//		}
//
//		System.out.println("SFINP2 Column " + columnName + " Updated : " + inserted);
//	}
//
//	private void transferFASColumnData(AuditServicesEntity auditRecord, String columnName) {
//		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_FAS_MOD_SUMMARYTABLE";
//		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
//
//		if (reportDate == null) {
//			throw new IllegalStateException("No modified FAS record available for verification.");
//		}
//
//		// Validate column name against allowed list to prevent SQL injection
//		if (!isValidFasColumn(columnName)) {
//			throw new IllegalArgumentException("Invalid or unauthorized column name for FAS: " + columnName);
//		}
//
//		// Dynamic single-column update query
//		String updateSql = "UPDATE BRRS_M_FAS_SUMMARYTABLE M " + "SET " + columnName + " = (SELECT S." + columnName
//				+ " FROM BRRS_M_FAS_MOD_SUMMARYTABLE S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) "
//				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
//
//		int inserted = jdbcTemplate.update(updateSql, reportDate);
//
//		if (inserted == 0) {
//			throw new IllegalStateException("M_FAS modification record not found for date : " + reportDate);
//		}
//
//		System.out.println("M_FAS Column " + columnName + " Updated : " + inserted);
//
//		// Optionally execute stored procedure if needed after column update
//		String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);
//		jdbcTemplate.update("BEGIN BRRS_M_FAS_SUMMARY_PROCEDURE(?); END;", formattedDate);
//	}
//
//	// Security validation helpers to prevent SQL Injection on column names
//	private boolean isValidSfinp2Column(String col) {
//		List<String> allowedCols = Arrays.asList("R34_MONTH_END", "R35_MONTH_END", "R43_MONTH_END", "R47_MONTH_END",
//				"R48_MONTH_END", "R51_MONTH_END", "R52_MONTH_END", "R53_MONTH_END", "R54_MONTH_END", "R55_MONTH_END",
//				"R56_MONTH_END", "R57_MONTH_END", "R58_MONTH_END", "R61_MONTH_END", "R62_MONTH_END", "R78_MONTH_END",
//				"R79_MONTH_END", "ENTITY_FLG", "MODIFY_FLG", "DEL_FLG");
//		return allowedCols.contains(col.toUpperCase());
//	}
//
//	private boolean isValidFasColumn(String col) {
//		List<String> allowedCols = Arrays.asList("R12_FIX_ASS", "R12_COST", "R12_ADD", "R12_DISPOSALS",
//				"R12_DEPRECIATION", "R12_NET_BOOK_VALUE", "R23_INTANGIBLE_ASS", "R23_COST_REV", "R23_USEFUL_LIFE",
//				"R23_RES_VALUE", "R23_MONTH_AMORT", "R23_ACC_AMORT_AMT", "R23_CLOSE_BAL", "R24_INTANGIBLE_ASS",
//				"R24_COST_REV", "R24_USEFUL_LIFE", "R24_RES_VALUE", "R24_MONTH_AMORT", "R24_ACC_AMORT_AMT",
//				"R24_CLOSE_BAL", "R25_INTANGIBLE_ASS", "R25_COST_REV", "R25_USEFUL_LIFE", "R25_RES_VALUE",
//				"R25_MONTH_AMORT", "R25_ACC_AMORT_AMT", "R25_CLOSE_BAL", "R26_INTANGIBLE_ASS", "R26_COST_REV",
//				"R26_USEFUL_LIFE", "R26_RES_VALUE", "R26_MONTH_AMORT", "R26_ACC_AMORT_AMT", "R26_CLOSE_BAL",
//				"R27_INTANGIBLE_ASS", "R27_COST_REV", "R27_USEFUL_LIFE", "R27_RES_VALUE", "R27_MONTH_AMORT",
//				"R27_ACC_AMORT_AMT", "R27_CLOSE_BAL", "R28_INTANGIBLE_ASS", "R28_COST_REV", "R28_USEFUL_LIFE",
//				"R28_RES_VALUE", "R28_MONTH_AMORT", "R28_ACC_AMORT_AMT", "R28_CLOSE_BAL", "ENTITY_FLG", "MODIFY_FLG",
//				"DEL_FLG");
//		return allowedCols.contains(col.toUpperCase());
//	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public AuditServicesEntity findById(Long id) {
		Optional<AuditServicesEntity> auditData = auditServicesRep.findById(id);
		return auditData.orElse(null);
	}

	@Transactional
	public void verifyAndTransferReport(Long auditId) {

		AuditServicesEntity auditRecord = findById(auditId);

		if (auditRecord == null) {
			throw new IllegalArgumentException("Audit record not found for ID: " + auditId);
		}

		/* Only MODIFY records can be verified. */
		if (auditRecord.getFunc_code() == null || !"MODIFY".equalsIgnoreCase(auditRecord.getFunc_code())) {
			throw new IllegalStateException(
					"Only MODIFY records can be verified. Current Function: " + auditRecord.getFunc_code());
		}

		String auditTable = auditRecord.getAudit_table();

		if (auditTable == null || auditTable.trim().isEmpty()) {
			throw new IllegalStateException("Audit table name missing for ID: " + auditId);
		}

		String upperTable = auditTable.trim().toUpperCase();

		System.out.println("======================================");
		System.out.println("CHECKER VERIFICATION");
		System.out.println("Audit ID     : " + auditId);
		System.out.println("Audit Table  : " + upperTable);
		System.out.println("Function     : " + auditRecord.getFunc_code());
		System.out.println("Entry User   : " + auditRecord.getEntry_user());
		System.out.println("======================================");

		/* SFINP2 */
		if (upperTable.contains("SFINP2")) {
			transferSfinp2Data(auditRecord);
		}
		/* FAS */
		else if (upperTable.contains("FAS")) {
			transferFASData(auditRecord);
		}
		/* DEP3 */
		else if (upperTable.contains("DEP3")) {
			transferDEP3Data(auditRecord);
		}
		/* Q_STAFF */
		else if (upperTable.contains("Q_STAFF")) {
			transferQStaffData(auditRecord);
		}
		/* Q_BRANCHNET */
		else if (upperTable.contains("Q_BRANCHNET")) {
			transferQBranchnetData(auditRecord);
		}
		/* Q_SMME_DEP */
		else if (upperTable.contains("Q_SMME_DEP")) {
			transferQSmmeDepData(auditRecord);
		}
		
		/* M_CA4 (Maker-Checker) */
		else if (upperTable.contains("M_CA4")) {
		    transferCA4Data(auditRecord);
		}
		
		/* M_SECL (Maker-Checker) */
		else if (upperTable.contains("M_SECL")) {
		    transferSECLData(auditRecord);
		}
		
		else {
			throw new IllegalArgumentException("Verification not configured for table: " + auditTable);
		}

		/*
		 * UPDATE AUTHENTICATION / VERIFICATION DETAILS This ensures auth_user is
		 * populated so the frontend marks it as "Verified"
		 */
		try {
			// Get the currently logged-in username from Spring Security context
			String currentPrincipalName = org.springframework.security.core.context.SecurityContextHolder.getContext()
					.getAuthentication().getName();
			auditRecord.setAuth_user(currentPrincipalName);
		} catch (Exception e) {
			// Fallback if security context is unavailable
			auditRecord.setAuth_user("SYSTEM_VERIFIER");
		}

		auditRecord.setAuth_time(new Date());

		// Save the updated audit record to persist the auth details
		auditServicesRep.save(auditRecord);

		System.out.println("Audit Record ID " + auditId + " marked as Verified successfully.");
	}

	private void transferSfinp2Data(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SFINP2_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified SFINP2 record available for verification.");
		}

		System.out.println("SFINP2 Report Date : " + reportDate);

		String updateSql = "UPDATE BRRS_M_SFINP2_SUMMARYTABLE M " + "SET (" + "      R34_MONTH_END, R35_MONTH_END, "
				+ "      R43_MONTH_END, R47_MONTH_END, R48_MONTH_END, "
				+ "      R51_MONTH_END, R52_MONTH_END, R53_MONTH_END, "
				+ "      R54_MONTH_END, R55_MONTH_END, R56_MONTH_END, " + "      R57_MONTH_END, R58_MONTH_END, "
				+ "      R61_MONTH_END, R62_MONTH_END, " + "      R78_MONTH_END, R79_MONTH_END, "
				+ "      ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				+ "         S.R34_MONTH_END, S.R35_MONTH_END, "
				+ "         S.R43_MONTH_END, S.R47_MONTH_END, S.R48_MONTH_END, "
				+ "         S.R51_MONTH_END, S.R52_MONTH_END, S.R53_MONTH_END, "
				+ "         S.R54_MONTH_END, S.R55_MONTH_END, S.R56_MONTH_END, "
				+ "         S.R57_MONTH_END, S.R58_MONTH_END, " + "         S.R61_MONTH_END, S.R62_MONTH_END, "
				+ "         S.R78_MONTH_END, S.R79_MONTH_END, " + "         S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG "
				+ "      FROM BRRS_M_SFINP2_MOD_SUMMARYTABLE S "
				+ "      WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int inserted = jdbcTemplate.update(updateSql, reportDate);

		if (inserted == 0) {
			throw new IllegalStateException("SFINP2 modification record not found for date : " + reportDate);
		}

		System.out.println("SFINP2 Main Inserted : " + inserted);

		String updateModSql = "UPDATE BRRS_M_SFINP2_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int updated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("SFINP2 MOD Updated : " + updated);
		System.out.println("SFINP2 Verification Completed Successfully");
	}

	private void transferFASData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_FAS_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified FAS record available for verification.");
		}

		System.out.println("M_FAS Report Date : " + reportDate);

		String updateSql = "UPDATE BRRS_M_FAS_SUMMARYTABLE M " + "SET ("
				+ "      R12_FIX_ASS, R12_COST, R12_ADD, R12_DISPOSALS, R12_DEPRECIATION, R12_NET_BOOK_VALUE, "
				+ "      R23_INTANGIBLE_ASS, R23_COST_REV, R23_USEFUL_LIFE, R23_RES_VALUE, R23_MONTH_AMORT, R23_ACC_AMORT_AMT, R23_CLOSE_BAL, "
				+ "      R24_INTANGIBLE_ASS, R24_COST_REV, R24_USEFUL_LIFE, R24_RES_VALUE, R24_MONTH_AMORT, R24_ACC_AMORT_AMT, R24_CLOSE_BAL, "
				+ "      R25_INTANGIBLE_ASS, R25_COST_REV, R25_USEFUL_LIFE, R25_RES_VALUE, R25_MONTH_AMORT, R25_ACC_AMORT_AMT, R25_CLOSE_BAL, "
				+ "      R26_INTANGIBLE_ASS, R26_COST_REV, R26_USEFUL_LIFE, R26_RES_VALUE, R26_MONTH_AMORT, R26_ACC_AMORT_AMT, R26_CLOSE_BAL, "
				+ "      R27_INTANGIBLE_ASS, R27_COST_REV, R27_USEFUL_LIFE, R27_RES_VALUE, R27_MONTH_AMORT, R27_ACC_AMORT_AMT, R27_CLOSE_BAL, "
				+ "      R28_INTANGIBLE_ASS, R28_COST_REV, R28_USEFUL_LIFE, R28_RES_VALUE, R28_MONTH_AMORT, R28_ACC_AMORT_AMT, R28_CLOSE_BAL, "
				+ "      ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				+ "         S.R12_FIX_ASS, S.R12_COST, S.R12_ADD, S.R12_DISPOSALS, S.R12_DEPRECIATION, S.R12_NET_BOOK_VALUE, "
				+ "         S.R23_INTANGIBLE_ASS, S.R23_COST_REV, S.R23_USEFUL_LIFE, S.R23_RES_VALUE, S.R23_MONTH_AMORT, S.R23_ACC_AMORT_AMT, S.R23_CLOSE_BAL, "
				+ "         S.R24_INTANGIBLE_ASS, S.R24_COST_REV, S.R24_USEFUL_LIFE, S.R24_RES_VALUE, S.R24_MONTH_AMORT, S.R24_ACC_AMORT_AMT, S.R24_CLOSE_BAL, "
				+ "         S.R25_INTANGIBLE_ASS, S.R25_COST_REV, S.R25_USEFUL_LIFE, S.R25_RES_VALUE, S.R25_MONTH_AMORT, S.R25_ACC_AMORT_AMT, S.R25_CLOSE_BAL, "
				+ "         S.R26_INTANGIBLE_ASS, S.R26_COST_REV, S.R26_USEFUL_LIFE, S.R26_RES_VALUE, S.R26_MONTH_AMORT, S.R26_ACC_AMORT_AMT, S.R26_CLOSE_BAL, "
				+ "         S.R27_INTANGIBLE_ASS, S.R27_COST_REV, S.R27_USEFUL_LIFE, S.R27_RES_VALUE, S.R27_MONTH_AMORT, S.R27_ACC_AMORT_AMT, S.R27_CLOSE_BAL, "
				+ "         S.R28_INTANGIBLE_ASS, S.R28_COST_REV, S.R28_USEFUL_LIFE, S.R28_RES_VALUE, S.R28_MONTH_AMORT, S.R28_ACC_AMORT_AMT, S.R28_CLOSE_BAL, "
				+ "         S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "      FROM BRRS_M_FAS_MOD_SUMMARYTABLE S "
				+ "      WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int inserted = jdbcTemplate.update(updateSql, reportDate);

		if (inserted == 0) {
			throw new IllegalStateException("M_FAS modification record not found for date : " + reportDate);
		}

		System.out.println("M_FAS Main Updated : " + inserted);

		String updateModSql = "UPDATE BRRS_M_FAS_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int updated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_FAS MOD Updated : " + updated);

		// Execute stored procedure after verification/transfer
		String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);
		jdbcTemplate.update("BEGIN BRRS_M_FAS_SUMMARY_PROCEDURE(?); END;", formattedDate);

		System.out.println("M_FAS Verification and Procedure Execution Completed Successfully");
	}

	private void transferDEP3Data(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_DEP3_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_DEP3 record available for verification.");
		}

		System.out.println("M_DEP3 Report Date : " + reportDate);

		String updateSql = "UPDATE BRRS_M_DEP3_SUMMARYTABLE M " + "SET ("
		// Exchange Rates (11-16)
				+ "      R11_EX_RATE_BUY, R11_EX_RATE_MID, R11_EX_RATE_SELL, "
				+ "      R12_EX_RATE_BUY, R12_EX_RATE_MID, R12_EX_RATE_SELL, "
				+ "      R13_EX_RATE_BUY, R13_EX_RATE_MID, R13_EX_RATE_SELL, "
				+ "      R14_EX_RATE_BUY, R14_EX_RATE_MID, R14_EX_RATE_SELL, "
				+ "      R15_EX_RATE_BUY, R15_EX_RATE_MID, R15_EX_RATE_SELL, "
				+ "      R16_EX_RATE_BUY, R16_EX_RATE_MID, R16_EX_RATE_SELL, "
				// Notice & Deposits (11-16, 18)
				+ "      R11_NOTICE_0TO31, R11_NOTICE_32TO88, R11_CER_OF_DEPO, "
				+ "      R12_NOTICE_0TO31, R12_NOTICE_32TO88, R12_CER_OF_DEPO, "
				+ "      R13_NOTICE_0TO31, R13_NOTICE_32TO88, R13_CER_OF_DEPO, "
				+ "      R14_NOTICE_0TO31, R14_NOTICE_32TO88, R14_CER_OF_DEPO, "
				+ "      R15_NOTICE_0TO31, R15_NOTICE_32TO88, R15_CER_OF_DEPO, "
				+ "      R16_NOTICE_0TO31, R16_NOTICE_32TO88, R16_CER_OF_DEPO, "
				+ "      R18_NOTICE_0TO31, R18_NOTICE_32TO88, R18_CER_OF_DEPO, "
				// Table 2 fields (28-34)
				+ "      R28_IMPORT, R28_INVESTMENT, R28_OTHER, R28_RESIDENTS, R28_NON_RESIDENTS, "
				+ "      R29_IMPORT, R29_INVESTMENT, R29_OTHER, R29_RESIDENTS, R29_NON_RESIDENTS, "
				+ "      R30_IMPORT, R30_INVESTMENT, R30_OTHER, R30_RESIDENTS, R30_NON_RESIDENTS, "
				+ "      R31_IMPORT, R31_INVESTMENT, R31_OTHER, R31_RESIDENTS, R31_NON_RESIDENTS, "
				+ "      R32_IMPORT, R32_INVESTMENT, R32_OTHER, R32_RESIDENTS, R32_NON_RESIDENTS, "
				+ "      R33_IMPORT, R33_INVESTMENT, R33_OTHER, R33_RESIDENTS, R33_NON_RESIDENTS, "
				+ "      R34_IMPORT, R34_INVESTMENT, R34_OTHER, R34_RESIDENTS, R34_NON_RESIDENTS, "
				+ "      ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				// Exchange Rates (11-16)
				+ "         S.R11_EX_RATE_BUY, S.R11_EX_RATE_MID, S.R11_EX_RATE_SELL, "
				+ "         S.R12_EX_RATE_BUY, S.R12_EX_RATE_MID, S.R12_EX_RATE_SELL, "
				+ "         S.R13_EX_RATE_BUY, S.R13_EX_RATE_MID, S.R13_EX_RATE_SELL, "
				+ "         S.R14_EX_RATE_BUY, S.R14_EX_RATE_MID, S.R14_EX_RATE_SELL, "
				+ "         S.R15_EX_RATE_BUY, S.R15_EX_RATE_MID, S.R15_EX_RATE_SELL, "
				+ "         S.R16_EX_RATE_BUY, S.R16_EX_RATE_MID, S.R16_EX_RATE_SELL, "
				// Notice & Deposits (11-16, 18)
				+ "         S.R11_NOTICE_0TO31, S.R11_NOTICE_32TO88, S.R11_CER_OF_DEPO, "
				+ "         S.R12_NOTICE_0TO31, S.R12_NOTICE_32TO88, S.R12_CER_OF_DEPO, "
				+ "         S.R13_NOTICE_0TO31, S.R13_NOTICE_32TO88, S.R13_CER_OF_DEPO, "
				+ "         S.R14_NOTICE_0TO31, S.R14_NOTICE_32TO88, S.R14_CER_OF_DEPO, "
				+ "         S.R15_NOTICE_0TO31, S.R15_NOTICE_32TO88, S.R15_CER_OF_DEPO, "
				+ "         S.R16_NOTICE_0TO31, S.R16_NOTICE_32TO88, S.R16_CER_OF_DEPO, "
				+ "         S.R18_NOTICE_0TO31, S.R18_NOTICE_32TO88, S.R18_CER_OF_DEPO, "
				// Table 2 fields (28-34)
				+ "         S.R28_IMPORT, S.R28_INVESTMENT, S.R28_OTHER, S.R28_RESIDENTS, S.R28_NON_RESIDENTS, "
				+ "         S.R29_IMPORT, S.R29_INVESTMENT, S.R29_OTHER, S.R29_RESIDENTS, S.R29_NON_RESIDENTS, "
				+ "         S.R30_IMPORT, S.R30_INVESTMENT, S.R30_OTHER, S.R30_RESIDENTS, S.R30_NON_RESIDENTS, "
				+ "         S.R31_IMPORT, S.R31_INVESTMENT, S.R31_OTHER, S.R31_RESIDENTS, S.R31_NON_RESIDENTS, "
				+ "         S.R32_IMPORT, S.R32_INVESTMENT, S.R32_OTHER, S.R32_RESIDENTS, S.R32_NON_RESIDENTS, "
				+ "         S.R33_IMPORT, S.R33_INVESTMENT, S.R33_OTHER, S.R33_RESIDENTS, S.R33_NON_RESIDENTS, "
				+ "         S.R34_IMPORT, S.R34_INVESTMENT, S.R34_OTHER, S.R34_RESIDENTS, S.R34_NON_RESIDENTS, "
				+ "         S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "      FROM BRRS_M_DEP3_MOD_SUMMARYTABLE S "
				+ "      WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int inserted = jdbcTemplate.update(updateSql, reportDate);

		if (inserted == 0) {
			throw new IllegalStateException("M_DEP3 modification record not found for date : " + reportDate);
		}

		System.out.println("M_DEP3 Main Updated : " + inserted);

		String updateModSql = "UPDATE BRRS_M_DEP3_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int updated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_DEP3 MOD Updated : " + updated);

		// Execute stored procedure after verification/transfer
		String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);
		jdbcTemplate.update("BEGIN BRRS_M_DEP3_SUMMARY_PROCEDURE(?); END;", formattedDate);

		System.out.println("M_DEP3 Verification and Procedure Execution Completed Successfully");
	}

	private void transferQStaffData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_Q_STAFF_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified Q_STAFF record available for verification.");
		}

		System.out.println("Q_STAFF Report Date : " + reportDate);

		// 1. Update BRRS_Q_STAFF_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_Q_STAFF_SUMMARYTABLE M " + "SET ("
				+ "    R9_STAFF_COMPLEMENT, R9_LOCAL, R9_EXPARIATES, R9_TOTAL, "
				+ "    R10_STAFF_COMPLEMENT, R10_LOCAL, R10_EXPARIATES, R10_TOTAL, "
				+ "    R11_STAFF_COMPLEMENT, R11_LOCAL, R11_EXPARIATES, R11_TOTAL, "
				+ "    R12_STAFF_COMPLEMENT, R12_LOCAL, R12_EXPARIATES, R12_TOTAL, "
				+ "    R13_STAFF_COMPLEMENT, R13_LOCAL, R13_EXPARIATES, R13_TOTAL, "
				+ "    R14_STAFF_COMPLEMENT, R14_LOCAL, R14_EXPARIATES, R14_TOTAL, "
				+ "    R15_STAFF_COMPLEMENT, R15_LOCAL, R15_EXPARIATES, R15_TOTAL, "
				+ "    R21_SENIOR_MANAGEMENT_COMPENSATION, R21_LOCAL, R21_EXPARIATES, R21_TOTAL, "
				+ "    R22_SENIOR_MANAGEMENT_COMPENSATION, R22_LOCAL, R22_EXPARIATES, R22_TOTAL, "
				+ "    R23_SENIOR_MANAGEMENT_COMPENSATION, R23_LOCAL, R23_EXPARIATES, R23_TOTAL, "
				+ "    R24_SENIOR_MANAGEMENT_COMPENSATION, R24_LOCAL, R24_EXPARIATES, R24_TOTAL, "
				+ "    R25_SENIOR_MANAGEMENT_COMPENSATION, R25_LOCAL, R25_EXPARIATES, R25_TOTAL, "
				+ "    R26_SENIOR_MANAGEMENT_COMPENSATION, R26_LOCAL, R26_EXPARIATES, R26_TOTAL, "
				+ "    R27_SENIOR_MANAGEMENT_COMPENSATION, R27_LOCAL, R27_EXPARIATES, R27_TOTAL, "
				+ "    R28_SENIOR_MANAGEMENT_COMPENSATION, R28_LOCAL, R28_EXPARIATES, R28_TOTAL, "
				+ "    R33_STAFF_LOANS, R33_ORIGINAL_AMT, R33_BALANCE_OUTSTANDING, R33_NO_OF_ACS, R33_INTEREST_RATE, "
				+ "    R34_STAFF_LOANS, R34_ORIGINAL_AMT, R34_BALANCE_OUTSTANDING, R34_NO_OF_ACS, R34_INTEREST_RATE, "
				+ "    R35_STAFF_LOANS, R35_ORIGINAL_AMT, R35_BALANCE_OUTSTANDING, R35_NO_OF_ACS, R35_INTEREST_RATE, "
				+ "    R36_STAFF_LOANS, R36_ORIGINAL_AMT, R36_BALANCE_OUTSTANDING, R36_NO_OF_ACS, R36_INTEREST_RATE, "
				+ "    R37_STAFF_LOANS, R37_ORIGINAL_AMT, R37_BALANCE_OUTSTANDING, R37_NO_OF_ACS, R37_INTEREST_RATE, "
				+ "    R38_STAFF_LOANS, R38_ORIGINAL_AMT, R38_BALANCE_OUTSTANDING, R38_NO_OF_ACS, R38_INTEREST_RATE, "
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				+ "        S.R9_STAFF_COMPLEMENT, S.R9_LOCAL, S.R9_EXPARIATES, S.R9_TOTAL, "
				+ "        S.R10_STAFF_COMPLEMENT, S.R10_LOCAL, S.R10_EXPARIATES, S.R10_TOTAL, "
				+ "        S.R11_STAFF_COMPLEMENT, S.R11_LOCAL, S.R11_EXPARIATES, S.R11_TOTAL, "
				+ "        S.R12_STAFF_COMPLEMENT, S.R12_LOCAL, S.R12_EXPARIATES, S.R12_TOTAL, "
				+ "        S.R13_STAFF_COMPLEMENT, S.R13_LOCAL, S.R13_EXPARIATES, S.R13_TOTAL, "
				+ "        S.R14_STAFF_COMPLEMENT, S.R14_LOCAL, S.R14_EXPARIATES, S.R14_TOTAL, "
				+ "        S.R15_STAFF_COMPLEMENT, S.R15_LOCAL, S.R15_EXPARIATES, S.R15_TOTAL, "
				+ "        S.R21_SENIOR_MANAGEMENT_COMPENSATION, S.R21_LOCAL, S.R21_EXPARIATES, S.R21_TOTAL, "
				+ "        S.R22_SENIOR_MANAGEMENT_COMPENSATION, S.R22_LOCAL, S.R22_EXPARIATES, S.R22_TOTAL, "
				+ "        S.R23_SENIOR_MANAGEMENT_COMPENSATION, S.R23_LOCAL, S.R23_EXPARIATES, S.R23_TOTAL, "
				+ "        S.R24_SENIOR_MANAGEMENT_COMPENSATION, S.R24_LOCAL, S.R24_EXPARIATES, S.R24_TOTAL, "
				+ "        S.R25_SENIOR_MANAGEMENT_COMPENSATION, S.R25_LOCAL, S.R25_EXPARIATES, S.R25_TOTAL, "
				+ "        S.R26_SENIOR_MANAGEMENT_COMPENSATION, S.R26_LOCAL, S.R26_EXPARIATES, S.R26_TOTAL, "
				+ "        S.R27_SENIOR_MANAGEMENT_COMPENSATION, S.R27_LOCAL, S.R27_EXPARIATES, S.R27_TOTAL, "
				+ "        S.R28_SENIOR_MANAGEMENT_COMPENSATION, S.R28_LOCAL, S.R28_EXPARIATES, S.R28_TOTAL, "
				+ "        S.R33_STAFF_LOANS, S.R33_ORIGINAL_AMT, S.R33_BALANCE_OUTSTANDING, S.R33_NO_OF_ACS, S.R33_INTEREST_RATE, "
				+ "        S.R34_STAFF_LOANS, S.R34_ORIGINAL_AMT, S.R34_BALANCE_OUTSTANDING, S.R34_NO_OF_ACS, S.R34_INTEREST_RATE, "
				+ "        S.R35_STAFF_LOANS, S.R35_ORIGINAL_AMT, S.R35_BALANCE_OUTSTANDING, S.R35_NO_OF_ACS, S.R35_INTEREST_RATE, "
				+ "        S.R36_STAFF_LOANS, S.R36_ORIGINAL_AMT, S.R36_BALANCE_OUTSTANDING, S.R36_NO_OF_ACS, S.R36_INTEREST_RATE, "
				+ "        S.R37_STAFF_LOANS, S.R37_ORIGINAL_AMT, S.R37_BALANCE_OUTSTANDING, S.R37_NO_OF_ACS, S.R37_INTEREST_RATE, "
				+ "        S.R38_STAFF_LOANS, S.R38_ORIGINAL_AMT, S.R38_BALANCE_OUTSTANDING, S.R38_NO_OF_ACS, S.R38_INTEREST_RATE, "
				+ "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_Q_STAFF_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("Q_STAFF modification record not found for date : " + reportDate);
		}

		System.out.println("Q_STAFF Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_Q_STAFF_DETAILTABLE from MOD table (Same column mapping)
		String updateDetailSql = "UPDATE BRRS_Q_STAFF_DETAILTABLE M " + "SET ("
				+ "    R9_STAFF_COMPLEMENT, R9_LOCAL, R9_EXPARIATES, R9_TOTAL, "
				+ "    R10_STAFF_COMPLEMENT, R10_LOCAL, R10_EXPARIATES, R10_TOTAL, "
				+ "    R11_STAFF_COMPLEMENT, R11_LOCAL, R11_EXPARIATES, R11_TOTAL, "
				+ "    R12_STAFF_COMPLEMENT, R12_LOCAL, R12_EXPARIATES, R12_TOTAL, "
				+ "    R13_STAFF_COMPLEMENT, R13_LOCAL, R13_EXPARIATES, R13_TOTAL, "
				+ "    R14_STAFF_COMPLEMENT, R14_LOCAL, R14_EXPARIATES, R14_TOTAL, "
				+ "    R15_STAFF_COMPLEMENT, R15_LOCAL, R15_EXPARIATES, R15_TOTAL, "
				+ "    R21_SENIOR_MANAGEMENT_COMPENSATION, R21_LOCAL, R21_EXPARIATES, R21_TOTAL, "
				+ "    R22_SENIOR_MANAGEMENT_COMPENSATION, R22_LOCAL, R22_EXPARIATES, R22_TOTAL, "
				+ "    R23_SENIOR_MANAGEMENT_COMPENSATION, R23_LOCAL, R23_EXPARIATES, R23_TOTAL, "
				+ "    R24_SENIOR_MANAGEMENT_COMPENSATION, R24_LOCAL, R24_EXPARIATES, R24_TOTAL, "
				+ "    R25_SENIOR_MANAGEMENT_COMPENSATION, R25_LOCAL, R25_EXPARIATES, R25_TOTAL, "
				+ "    R26_SENIOR_MANAGEMENT_COMPENSATION, R26_LOCAL, R26_EXPARIATES, R26_TOTAL, "
				+ "    R27_SENIOR_MANAGEMENT_COMPENSATION, R27_LOCAL, R27_EXPARIATES, R27_TOTAL, "
				+ "    R28_SENIOR_MANAGEMENT_COMPENSATION, R28_LOCAL, R28_EXPARIATES, R28_TOTAL, "
				+ "    R33_STAFF_LOANS, R33_ORIGINAL_AMT, R33_BALANCE_OUTSTANDING, R33_NO_OF_ACS, R33_INTEREST_RATE, "
				+ "    R34_STAFF_LOANS, R34_ORIGINAL_AMT, R34_BALANCE_OUTSTANDING, R34_NO_OF_ACS, R34_INTEREST_RATE, "
				+ "    R35_STAFF_LOANS, R35_ORIGINAL_AMT, R35_BALANCE_OUTSTANDING, R35_NO_OF_ACS, R35_INTEREST_RATE, "
				+ "    R36_STAFF_LOANS, R36_ORIGINAL_AMT, R36_BALANCE_OUTSTANDING, R36_NO_OF_ACS, R36_INTEREST_RATE, "
				+ "    R37_STAFF_LOANS, R37_ORIGINAL_AMT, R37_BALANCE_OUTSTANDING, R37_NO_OF_ACS, R37_INTEREST_RATE, "
				+ "    R38_STAFF_LOANS, R38_ORIGINAL_AMT, R38_BALANCE_OUTSTANDING, R38_NO_OF_ACS, R38_INTEREST_RATE, "
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				+ "        S.R9_STAFF_COMPLEMENT, S.R9_LOCAL, S.R9_EXPARIATES, S.R9_TOTAL, "
				+ "        S.R10_STAFF_COMPLEMENT, S.R10_LOCAL, S.R10_EXPARIATES, S.R10_TOTAL, "
				+ "        S.R11_STAFF_COMPLEMENT, S.R11_LOCAL, S.R11_EXPARIATES, S.R11_TOTAL, "
				+ "        S.R12_STAFF_COMPLEMENT, S.R12_LOCAL, S.R12_EXPARIATES, S.R12_TOTAL, "
				+ "        S.R13_STAFF_COMPLEMENT, S.R13_LOCAL, S.R13_EXPARIATES, S.R13_TOTAL, "
				+ "        S.R14_STAFF_COMPLEMENT, S.R14_LOCAL, S.R14_EXPARIATES, S.R14_TOTAL, "
				+ "        S.R15_STAFF_COMPLEMENT, S.R15_LOCAL, S.R15_EXPARIATES, S.R15_TOTAL, "
				+ "        S.R21_SENIOR_MANAGEMENT_COMPENSATION, S.R21_LOCAL, S.R21_EXPARIATES, S.R21_TOTAL, "
				+ "        S.R22_SENIOR_MANAGEMENT_COMPENSATION, S.R22_LOCAL, S.R22_EXPARIATES, S.R22_TOTAL, "
				+ "        S.R23_SENIOR_MANAGEMENT_COMPENSATION, S.R23_LOCAL, S.R23_EXPARIATES, S.R23_TOTAL, "
				+ "        S.R24_SENIOR_MANAGEMENT_COMPENSATION, S.R24_LOCAL, S.R24_EXPARIATES, S.R24_TOTAL, "
				+ "        S.R25_SENIOR_MANAGEMENT_COMPENSATION, S.R25_LOCAL, S.R25_EXPARIATES, S.R25_TOTAL, "
				+ "        S.R26_SENIOR_MANAGEMENT_COMPENSATION, S.R26_LOCAL, S.R26_EXPARIATES, S.R26_TOTAL, "
				+ "        S.R27_SENIOR_MANAGEMENT_COMPENSATION, S.R27_LOCAL, S.R27_EXPARIATES, S.R27_TOTAL, "
				+ "        S.R28_SENIOR_MANAGEMENT_COMPENSATION, S.R28_LOCAL, S.R28_EXPARIATES, S.R28_TOTAL, "
				+ "        S.R33_STAFF_LOANS, S.R33_ORIGINAL_AMT, S.R33_BALANCE_OUTSTANDING, S.R33_NO_OF_ACS, S.R33_INTEREST_RATE, "
				+ "        S.R34_STAFF_LOANS, S.R34_ORIGINAL_AMT, S.R34_BALANCE_OUTSTANDING, S.R34_NO_OF_ACS, S.R34_INTEREST_RATE, "
				+ "        S.R35_STAFF_LOANS, S.R35_ORIGINAL_AMT, S.R35_BALANCE_OUTSTANDING, S.R35_NO_OF_ACS, S.R35_INTEREST_RATE, "
				+ "        S.R36_STAFF_LOANS, S.R36_ORIGINAL_AMT, S.R36_BALANCE_OUTSTANDING, S.R36_NO_OF_ACS, S.R36_INTEREST_RATE, "
				+ "        S.R37_STAFF_LOANS, S.R37_ORIGINAL_AMT, S.R37_BALANCE_OUTSTANDING, S.R37_NO_OF_ACS, S.R37_INTEREST_RATE, "
				+ "        S.R38_STAFF_LOANS, S.R38_ORIGINAL_AMT, S.R38_BALANCE_OUTSTANDING, S.R38_NO_OF_ACS, S.R38_INTEREST_RATE, "
				+ "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_Q_STAFF_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("Q_STAFF Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_Q_STAFF_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("Q_STAFF MOD Updated : " + modUpdated);
		System.out.println("Q_STAFF Verification and Transfer Completed Successfully");
	}

	private void transferQBranchnetData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_Q_BRANCHNET_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified Q_BRANCHNET record available for verification.");
		}

		System.out.println("Q_BRANCHNET Report Date : " + reportDate);

		// 1. Update BRRS_Q_BRANCHNET_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_Q_BRANCHNET_SUMMARYTABLE M " + "SET ("
		// Rows 10 to 20
				+ "    R10_BRAN_SUB_BRAN_DISTRICT, R10_NO1_OF_BRANCHES, R10_NO1_OF_SUB_BRANCHES, R10_NO1_OF_AGENCIES, "
				+ "    R11_BRAN_SUB_BRAN_DISTRICT, R11_NO1_OF_BRANCHES, R11_NO1_OF_SUB_BRANCHES, R11_NO1_OF_AGENCIES, "
				+ "    R12_BRAN_SUB_BRAN_DISTRICT, R12_NO1_OF_BRANCHES, R12_NO1_OF_SUB_BRANCHES, R12_NO1_OF_AGENCIES, "
				+ "    R13_BRAN_SUB_BRAN_DISTRICT, R13_NO1_OF_BRANCHES, R13_NO1_OF_SUB_BRANCHES, R13_NO1_OF_AGENCIES, "
				+ "    R14_BRAN_SUB_BRAN_DISTRICT, R14_NO1_OF_BRANCHES, R14_NO1_OF_SUB_BRANCHES, R14_NO1_OF_AGENCIES, "
				+ "    R15_BRAN_SUB_BRAN_DISTRICT, R15_NO1_OF_BRANCHES, R15_NO1_OF_SUB_BRANCHES, R15_NO1_OF_AGENCIES, "
				+ "    R16_BRAN_SUB_BRAN_DISTRICT, R16_NO1_OF_BRANCHES, R16_NO1_OF_SUB_BRANCHES, R16_NO1_OF_AGENCIES, "
				+ "    R17_BRAN_SUB_BRAN_DISTRICT, R17_NO1_OF_BRANCHES, R17_NO1_OF_SUB_BRANCHES, R17_NO1_OF_AGENCIES, "
				+ "    R18_BRAN_SUB_BRAN_DISTRICT, R18_NO1_OF_BRANCHES, R18_NO1_OF_SUB_BRANCHES, R18_NO1_OF_AGENCIES, "
				+ "    R19_BRAN_SUB_BRAN_DISTRICT, R19_NO1_OF_BRANCHES, R19_NO1_OF_SUB_BRANCHES, R19_NO1_OF_AGENCIES, "
				+ "    R20_BRAN_SUB_BRAN_DISTRICT, R20_NO1_OF_BRANCHES, R20_NO1_OF_SUB_BRANCHES, R20_NO1_OF_AGENCIES, "
				// Rows 25 to 35
				+ "    R25_ATM_MINI_ATM_DISTRICT, R25_NO_OF_ATMS, R25_NO_OF_MINI_ATMS, R25_ENCASHMENT_POINTS, "
				+ "    R26_ATM_MINI_ATM_DISTRICT, R26_NO_OF_ATMS, R26_NO_OF_MINI_ATMS, R26_ENCASHMENT_POINTS, "
				+ "    R27_ATM_MINI_ATM_DISTRICT, R27_NO_OF_ATMS, R27_NO_OF_MINI_ATMS, R27_ENCASHMENT_POINTS, "
				+ "    R28_ATM_MINI_ATM_DISTRICT, R28_NO_OF_ATMS, R28_NO_OF_MINI_ATMS, R28_ENCASHMENT_POINTS, "
				+ "    R29_ATM_MINI_ATM_DISTRICT, R29_NO_OF_ATMS, R29_NO_OF_MINI_ATMS, R29_ENCASHMENT_POINTS, "
				+ "    R30_ATM_MINI_ATM_DISTRICT, R30_NO_OF_ATMS, R30_NO_OF_MINI_ATMS, R30_ENCASHMENT_POINTS, "
				+ "    R31_ATM_MINI_ATM_DISTRICT, R31_NO_OF_ATMS, R31_NO_OF_MINI_ATMS, R31_ENCASHMENT_POINTS, "
				+ "    R32_ATM_MINI_ATM_DISTRICT, R32_NO_OF_ATMS, R32_NO_OF_MINI_ATMS, R32_ENCASHMENT_POINTS, "
				+ "    R33_ATM_MINI_ATM_DISTRICT, R33_NO_OF_ATMS, R33_NO_OF_MINI_ATMS, R33_ENCASHMENT_POINTS, "
				+ "    R34_ATM_MINI_ATM_DISTRICT, R34_NO_OF_ATMS, R34_NO_OF_MINI_ATMS, R34_ENCASHMENT_POINTS, "
				+ "    R35_ATM_MINI_ATM_DISTRICT, R35_NO_OF_ATMS, R35_NO_OF_MINI_ATMS, R35_ENCASHMENT_POINTS, "
				// Rows 40 to 50
				+ "    R40_DEBIT_DISTRICT, R40_OPENING_NO_OF_CARDS, R40_NO_OF_CARDS_ISSUED, R40_NO_CARDS_OF_CLOSED, R40_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R41_DEBIT_DISTRICT, R41_OPENING_NO_OF_CARDS, R41_NO_OF_CARDS_ISSUED, R41_NO_CARDS_OF_CLOSED, R41_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R42_DEBIT_DISTRICT, R42_OPENING_NO_OF_CARDS, R42_NO_OF_CARDS_ISSUED, R42_NO_CARDS_OF_CLOSED, R42_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R43_DEBIT_DISTRICT, R43_OPENING_NO_OF_CARDS, R43_NO_OF_CARDS_ISSUED, R43_NO_CARDS_OF_CLOSED, R43_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R44_DEBIT_DISTRICT, R44_OPENING_NO_OF_CARDS, R44_NO_OF_CARDS_ISSUED, R44_NO_CARDS_OF_CLOSED, R44_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R45_DEBIT_DISTRICT, R45_OPENING_NO_OF_CARDS, R45_NO_OF_CARDS_ISSUED, R45_NO_CARDS_OF_CLOSED, R45_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R46_DEBIT_DISTRICT, R46_OPENING_NO_OF_CARDS, R46_NO_OF_CARDS_ISSUED, R46_NO_CARDS_OF_CLOSED, R46_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R47_DEBIT_DISTRICT, R47_OPENING_NO_OF_CARDS, R47_NO_OF_CARDS_ISSUED, R47_NO_CARDS_OF_CLOSED, R47_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R48_DEBIT_DISTRICT, R48_OPENING_NO_OF_CARDS, R48_NO_OF_CARDS_ISSUED, R48_NO_CARDS_OF_CLOSED, R48_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R49_DEBIT_DISTRICT, R49_OPENING_NO_OF_CARDS, R49_NO_OF_CARDS_ISSUED, R49_NO_CARDS_OF_CLOSED, R49_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R50_DEBIT_DISTRICT, R50_OPENING_NO_OF_CARDS, R50_NO_OF_CARDS_ISSUED, R50_NO_CARDS_OF_CLOSED, R50_CLOSING_BAL_OF_ACTIVE_CARDS, "
				// Rows 55 to 65
				+ "    R55_CREDIT_DISTRICT, R55_OPENING_NO_OF_CARDS, R55_NO_OF_CARDS_ISSUED, R55_NO_CARDS_OF_CLOSED, R55_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R56_CREDIT_DISTRICT, R56_OPENING_NO_OF_CARDS, R56_NO_OF_CARDS_ISSUED, R56_NO_CARDS_OF_CLOSED, R56_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R57_CREDIT_DISTRICT, R57_OPENING_NO_OF_CARDS, R57_NO_OF_CARDS_ISSUED, R57_NO_CARDS_OF_CLOSED, R57_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R58_CREDIT_DISTRICT, R58_OPENING_NO_OF_CARDS, R58_NO_OF_CARDS_ISSUED, R58_NO_CARDS_OF_CLOSED, R58_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R59_CREDIT_DISTRICT, R59_OPENING_NO_OF_CARDS, R59_NO_OF_CARDS_ISSUED, R59_NO_CARDS_OF_CLOSED, R59_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R60_CREDIT_DISTRICT, R60_OPENING_NO_OF_CARDS, R60_NO_OF_CARDS_ISSUED, R60_NO_CARDS_OF_CLOSED, R60_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R61_CREDIT_DISTRICT, R61_OPENING_NO_OF_CARDS, R61_NO_OF_CARDS_ISSUED, R61_NO_CARDS_OF_CLOSED, R61_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R62_CREDIT_DISTRICT, R62_OPENING_NO_OF_CARDS, R62_NO_OF_CARDS_ISSUED, R62_NO_CARDS_OF_CLOSED, R62_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R63_CREDIT_DISTRICT, R63_OPENING_NO_OF_CARDS, R63_NO_OF_CARDS_ISSUED, R63_NO_CARDS_OF_CLOSED, R63_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R64_CREDIT_DISTRICT, R64_OPENING_NO_OF_CARDS, R64_NO_OF_CARDS_ISSUED, R64_NO_CARDS_OF_CLOSED, R64_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    R65_CREDIT_DISTRICT, R65_OPENING_NO_OF_CARDS, R65_NO_OF_CARDS_ISSUED, R65_NO_CARDS_OF_CLOSED, R65_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				// Rows 10 to 20
				+ "        S.R10_BRAN_SUB_BRAN_DISTRICT, S.R10_NO1_OF_BRANCHES, S.R10_NO1_OF_SUB_BRANCHES, S.R10_NO1_OF_AGENCIES, "
				+ "        S.R11_BRAN_SUB_BRAN_DISTRICT, S.R11_NO1_OF_BRANCHES, S.R11_NO1_OF_SUB_BRANCHES, S.R11_NO1_OF_AGENCIES, "
				+ "        S.R12_BRAN_SUB_BRAN_DISTRICT, S.R12_NO1_OF_BRANCHES, S.R12_NO1_OF_SUB_BRANCHES, S.R12_NO1_OF_AGENCIES, "
				+ "        S.R13_BRAN_SUB_BRAN_DISTRICT, S.R13_NO1_OF_BRANCHES, S.R13_NO1_OF_SUB_BRANCHES, S.R13_NO1_OF_AGENCIES, "
				+ "        S.R14_BRAN_SUB_BRAN_DISTRICT, S.R14_NO1_OF_BRANCHES, S.R14_NO1_OF_SUB_BRANCHES, S.R14_NO1_OF_AGENCIES, "
				+ "        S.R15_BRAN_SUB_BRAN_DISTRICT, S.R15_NO1_OF_BRANCHES, S.R15_NO1_OF_SUB_BRANCHES, S.R15_NO1_OF_AGENCIES, "
				+ "        S.R16_BRAN_SUB_BRAN_DISTRICT, S.R16_NO1_OF_BRANCHES, S.R16_NO1_OF_SUB_BRANCHES, S.R16_NO1_OF_AGENCIES, "
				+ "        S.R17_BRAN_SUB_BRAN_DISTRICT, S.R17_NO1_OF_BRANCHES, S.R17_NO1_OF_SUB_BRANCHES, S.R17_NO1_OF_AGENCIES, "
				+ "        S.R18_BRAN_SUB_BRAN_DISTRICT, S.R18_NO1_OF_BRANCHES, S.R18_NO1_OF_SUB_BRANCHES, S.R18_NO1_OF_AGENCIES, "
				+ "        S.R19_BRAN_SUB_BRAN_DISTRICT, S.R19_NO1_OF_BRANCHES, S.R19_NO1_OF_SUB_BRANCHES, S.R19_NO1_OF_AGENCIES, "
				+ "        S.R20_BRAN_SUB_BRAN_DISTRICT, S.R20_NO1_OF_BRANCHES, S.R20_NO1_OF_SUB_BRANCHES, S.R20_NO1_OF_AGENCIES, "
				// Rows 25 to 35
				+ "        S.R25_ATM_MINI_ATM_DISTRICT, S.R25_NO_OF_ATMS, S.R25_NO_OF_MINI_ATMS, S.R25_ENCASHMENT_POINTS, "
				+ "        S.R26_ATM_MINI_ATM_DISTRICT, S.R26_NO_OF_ATMS, S.R26_NO_OF_MINI_ATMS, S.R26_ENCASHMENT_POINTS, "
				+ "        S.R27_ATM_MINI_ATM_DISTRICT, S.R27_NO_OF_ATMS, S.R27_NO_OF_MINI_ATMS, S.R27_ENCASHMENT_POINTS, "
				+ "        S.R28_ATM_MINI_ATM_DISTRICT, S.R28_NO_OF_ATMS, S.R28_NO_OF_MINI_ATMS, S.R28_ENCASHMENT_POINTS, "
				+ "        S.R29_ATM_MINI_ATM_DISTRICT, S.R29_NO_OF_ATMS, S.R29_NO_OF_MINI_ATMS, S.R29_ENCASHMENT_POINTS, "
				+ "        S.R30_ATM_MINI_ATM_DISTRICT, S.R30_NO_OF_ATMS, S.R30_NO_OF_MINI_ATMS, S.R30_ENCASHMENT_POINTS, "
				+ "        S.R31_ATM_MINI_ATM_DISTRICT, S.R31_NO_OF_ATMS, S.R31_NO_OF_MINI_ATMS, S.R31_ENCASHMENT_POINTS, "
				+ "        S.R32_ATM_MINI_ATM_DISTRICT, S.R32_NO_OF_ATMS, S.R32_NO_OF_MINI_ATMS, S.R32_ENCASHMENT_POINTS, "
				+ "        S.R33_ATM_MINI_ATM_DISTRICT, S.R33_NO_OF_ATMS, S.R33_NO_OF_MINI_ATMS, S.R33_ENCASHMENT_POINTS, "
				+ "        S.R34_ATM_MINI_ATM_DISTRICT, S.R34_NO_OF_ATMS, S.R34_NO_OF_MINI_ATMS, S.R34_ENCASHMENT_POINTS, "
				+ "        S.R35_ATM_MINI_ATM_DISTRICT, S.R35_NO_OF_ATMS, S.R35_NO_OF_MINI_ATMS, S.R35_ENCASHMENT_POINTS, "
				// Rows 40 to 50
				+ "        S.R40_DEBIT_DISTRICT, S.R40_OPENING_NO_OF_CARDS, S.R40_NO_OF_CARDS_ISSUED, S.R40_NO_CARDS_OF_CLOSED, S.R40_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R41_DEBIT_DISTRICT, S.R41_OPENING_NO_OF_CARDS, S.R41_NO_OF_CARDS_ISSUED, S.R41_NO_CARDS_OF_CLOSED, S.R41_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R42_DEBIT_DISTRICT, S.R42_OPENING_NO_OF_CARDS, S.R42_NO_OF_CARDS_ISSUED, S.R42_NO_CARDS_OF_CLOSED, S.R42_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R43_DEBIT_DISTRICT, S.R43_OPENING_NO_OF_CARDS, S.R43_NO_OF_CARDS_ISSUED, S.R43_NO_CARDS_OF_CLOSED, S.R43_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R44_DEBIT_DISTRICT, S.R44_OPENING_NO_OF_CARDS, S.R44_NO_OF_CARDS_ISSUED, S.R44_NO_CARDS_OF_CLOSED, S.R44_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R45_DEBIT_DISTRICT, S.R45_OPENING_NO_OF_CARDS, S.R45_NO_OF_CARDS_ISSUED, S.R45_NO_CARDS_OF_CLOSED, S.R45_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R46_DEBIT_DISTRICT, S.R46_OPENING_NO_OF_CARDS, S.R46_NO_OF_CARDS_ISSUED, S.R46_NO_CARDS_OF_CLOSED, S.R46_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R47_DEBIT_DISTRICT, S.R47_OPENING_NO_OF_CARDS, S.R47_NO_OF_CARDS_ISSUED, S.R47_NO_CARDS_OF_CLOSED, S.R47_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R48_DEBIT_DISTRICT, S.R48_OPENING_NO_OF_CARDS, S.R48_NO_OF_CARDS_ISSUED, S.R48_NO_CARDS_OF_CLOSED, S.R48_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R49_DEBIT_DISTRICT, S.R49_OPENING_NO_OF_CARDS, S.R49_NO_OF_CARDS_ISSUED, S.R49_NO_CARDS_OF_CLOSED, S.R49_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R50_DEBIT_DISTRICT, S.R50_OPENING_NO_OF_CARDS, S.R50_NO_OF_CARDS_ISSUED, S.R50_NO_CARDS_OF_CLOSED, S.R50_CLOSING_BAL_OF_ACTIVE_CARDS, "
				// Rows 55 to 65
				+ "        S.R55_CREDIT_DISTRICT, S.R55_OPENING_NO_OF_CARDS, S.R55_NO_OF_CARDS_ISSUED, S.R55_NO_CARDS_OF_CLOSED, S.R55_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R56_CREDIT_DISTRICT, S.R56_OPENING_NO_OF_CARDS, S.R56_NO_OF_CARDS_ISSUED, S.R56_NO_CARDS_OF_CLOSED, S.R56_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R57_CREDIT_DISTRICT, S.R57_OPENING_NO_OF_CARDS, S.R57_NO_OF_CARDS_ISSUED, S.R57_NO_CARDS_OF_CLOSED, S.R57_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R58_CREDIT_DISTRICT, S.R58_OPENING_NO_OF_CARDS, S.R58_NO_OF_CARDS_ISSUED, S.R58_NO_CARDS_OF_CLOSED, S.R58_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R59_CREDIT_DISTRICT, S.R59_OPENING_NO_OF_CARDS, S.R59_NO_OF_CARDS_ISSUED, S.R59_NO_CARDS_OF_CLOSED, S.R59_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R60_CREDIT_DISTRICT, S.R60_OPENING_NO_OF_CARDS, S.R60_NO_OF_CARDS_ISSUED, S.R60_NO_CARDS_OF_CLOSED, S.R60_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R61_CREDIT_DISTRICT, S.R61_OPENING_NO_OF_CARDS, S.R61_NO_OF_CARDS_ISSUED, S.R61_NO_CARDS_OF_CLOSED, S.R61_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R62_CREDIT_DISTRICT, S.R62_OPENING_NO_OF_CARDS, S.R62_NO_OF_CARDS_ISSUED, S.R62_NO_CARDS_OF_CLOSED, S.R62_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R63_CREDIT_DISTRICT, S.R63_OPENING_NO_OF_CARDS, S.R63_NO_OF_CARDS_ISSUED, S.R63_NO_CARDS_OF_CLOSED, S.R63_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R64_CREDIT_DISTRICT, S.R64_OPENING_NO_OF_CARDS, S.R64_NO_OF_CARDS_ISSUED, S.R64_NO_CARDS_OF_CLOSED, S.R64_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.R65_CREDIT_DISTRICT, S.R65_OPENING_NO_OF_CARDS, S.R65_NO_OF_CARDS_ISSUED, S.R65_NO_CARDS_OF_CLOSED, S.R65_CLOSING_BAL_OF_ACTIVE_CARDS, "
				+ "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_Q_BRANCHNET_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("Q_BRANCHNET modification record not found for date : " + reportDate);
		}

		System.out.println("Q_BRANCHNET Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_Q_BRANCHNET_DETAILTABLE from MOD table (Using the same full
		// column mapping)
		String updateDetailSql = updateSummarySql.replace("BRRS_Q_BRANCHNET_SUMMARYTABLE",
				"BRRS_Q_BRANCHNET_DETAILTABLE");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		System.out.println("Q_BRANCHNET Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_Q_BRANCHNET_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("Q_BRANCHNET MOD Updated : " + modUpdated);
		System.out.println("Q_BRANCHNET Verification and Transfer Completed Successfully");
	}

	private void transferQSmmeDepData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_Q_SMME_DEP_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified Q_SMME_DEP record available for verification.");
		}

		System.out.println("Q_SMME_DEP Report Date : " + reportDate);

		// 1. Update BRRS_Q_SMME_DEP_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_Q_SMME_DEP_SUMMARYTABLE M " + "SET ("
		// Rows 11 to 26 & 32 to 48 (Skipping 27 to 31)
				+ "    R11_PRODUCT, R11_CURRENT, R11_CALL, R11_SAVINGS, R11_0_31D_NOTICE, R11_32_88D_NOTICE, R11_91D_DEPOSIT, R11_1_2M_FD, R11_4_6M_FD, R11_7_12M_FD, R11_13_18M_FD, R11_19_24M_FD, R11_OVER24_FD, R11_TOTAL, R11_NOACC, "
				+ "    R12_PRODUCT, R12_CURRENT, R12_CALL, R12_SAVINGS, R12_0_31D_NOTICE, R12_32_88D_NOTICE, R12_91D_DEPOSIT, R12_1_2M_FD, R12_4_6M_FD, R12_7_12M_FD, R12_13_18M_FD, R12_19_24M_FD, R12_OVER24_FD, R12_TOTAL, R12_NOACC, "
				+ "    R13_PRODUCT, R13_CURRENT, R13_CALL, R13_SAVINGS, R13_0_31D_NOTICE, R13_32_88D_NOTICE, R13_91D_DEPOSIT, R13_1_2M_FD, R13_4_6M_FD, R13_7_12M_FD, R13_13_18M_FD, R13_19_24M_FD, R13_OVER24_FD, R13_TOTAL, R13_NOACC, "
				+ "    R14_PRODUCT, R14_CURRENT, R14_CALL, R14_SAVINGS, R14_0_31D_NOTICE, R14_32_88D_NOTICE, R14_91D_DEPOSIT, R14_1_2M_FD, R14_4_6M_FD, R14_7_12M_FD, R14_13_18M_FD, R14_19_24M_FD, R14_OVER24_FD, R14_TOTAL, R14_NOACC, "
				+ "    R15_PRODUCT, R15_CURRENT, R15_CALL, R15_SAVINGS, R15_0_31D_NOTICE, R15_32_88D_NOTICE, R15_91D_DEPOSIT, R15_1_2M_FD, R15_4_6M_FD, R15_7_12M_FD, R15_13_18M_FD, R15_19_24M_FD, R15_OVER24_FD, R15_TOTAL, R15_NOACC, "
				+ "    R16_PRODUCT, R16_CURRENT, R16_CALL, R16_SAVINGS, R16_0_31D_NOTICE, R16_32_88D_NOTICE, R16_91D_DEPOSIT, R16_1_2M_FD, R16_4_6M_FD, R16_7_12M_FD, R16_13_18M_FD, R16_19_24M_FD, R16_OVER24_FD, R16_TOTAL, R16_NOACC, "
				+ "    R17_PRODUCT, R17_CURRENT, R17_CALL, R17_SAVINGS, R17_0_31D_NOTICE, R17_32_88D_NOTICE, R17_91D_DEPOSIT, R17_1_2M_FD, R17_4_6M_FD, R17_7_12M_FD, R17_13_18M_FD, R17_19_24M_FD, R17_OVER24_FD, R17_TOTAL, R17_NOACC, "
				+ "    R18_PRODUCT, R18_CURRENT, R18_CALL, R18_SAVINGS, R18_0_31D_NOTICE, R18_32_88D_NOTICE, R18_91D_DEPOSIT, R18_1_2M_FD, R18_4_6M_FD, R18_7_12M_FD, R18_13_18M_FD, R18_19_24M_FD, R18_OVER24_FD, R18_TOTAL, R18_NOACC, "
				+ "    R19_PRODUCT, R19_CURRENT, R19_CALL, R19_SAVINGS, R19_0_31D_NOTICE, R19_32_88D_NOTICE, R19_91D_DEPOSIT, R19_1_2M_FD, R19_4_6M_FD, R19_7_12M_FD, R19_13_18M_FD, R19_19_24M_FD, R19_OVER24_FD, R19_TOTAL, R19_NOACC, "
				+ "    R20_PRODUCT, R20_CURRENT, R20_CALL, R20_SAVINGS, R20_0_31D_NOTICE, R20_32_88D_NOTICE, R20_91D_DEPOSIT, R20_1_2M_FD, R20_4_6M_FD, R20_7_12M_FD, R20_13_18M_FD, R20_19_24M_FD, R20_OVER24_FD, R20_TOTAL, R20_NOACC, "
				+ "    R21_PRODUCT, R21_CURRENT, R21_CALL, R21_SAVINGS, R21_0_31D_NOTICE, R21_32_88D_NOTICE, R21_91D_DEPOSIT, R21_1_2M_FD, R21_4_6M_FD, R21_7_12M_FD, R21_13_18M_FD, R21_19_24M_FD, R21_OVER24_FD, R21_TOTAL, R21_NOACC, "
				+ "    R22_PRODUCT, R22_CURRENT, R22_CALL, R22_SAVINGS, R22_0_31D_NOTICE, R22_32_88D_NOTICE, R22_91D_DEPOSIT, R22_1_2M_FD, R22_4_6M_FD, R22_7_12M_FD, R22_13_18M_FD, R22_19_24M_FD, R22_OVER24_FD, R22_TOTAL, R22_NOACC, "
				+ "    R23_PRODUCT, R23_CURRENT, R23_CALL, R23_SAVINGS, R23_0_31D_NOTICE, R23_32_88D_NOTICE, R23_91D_DEPOSIT, R23_1_2M_FD, R23_4_6M_FD, R23_7_12M_FD, R23_13_18M_FD, R23_19_24M_FD, R23_OVER24_FD, R23_TOTAL, R23_NOACC, "
				+ "    R24_PRODUCT, R24_CURRENT, R24_CALL, R24_SAVINGS, R24_0_31D_NOTICE, R24_32_88D_NOTICE, R24_91D_DEPOSIT, R24_1_2M_FD, R24_4_6M_FD, R24_7_12M_FD, R24_13_18M_FD, R24_19_24M_FD, R24_OVER24_FD, R24_TOTAL, R24_NOACC, "
				+ "    R25_PRODUCT, R25_CURRENT, R25_CALL, R25_SAVINGS, R25_0_31D_NOTICE, R25_32_88D_NOTICE, R25_91D_DEPOSIT, R25_1_2M_FD, R25_4_6M_FD, R25_7_12M_FD, R25_13_18M_FD, R25_19_24M_FD, R25_OVER24_FD, R25_TOTAL, R25_NOACC, "
				+ "    R26_PRODUCT, R26_CURRENT, R26_CALL, R26_SAVINGS, R26_0_31D_NOTICE, R26_32_88D_NOTICE, R26_91D_DEPOSIT, R26_1_2M_FD, R26_4_6M_FD, R26_7_12M_FD, R26_13_18M_FD, R26_19_24M_FD, R26_OVER24_FD, R26_TOTAL, R26_NOACC, "
				+ "    R32_PRODUCT, R32_CURRENT, R32_CALL, R32_SAVINGS, R32_0_31D_NOTICE, R32_32_88D_NOTICE, R32_91D_DEPOSIT, R32_1_2M_FD, R32_4_6M_FD, R32_7_12M_FD, R32_13_18M_FD, R32_19_24M_FD, R32_OVER24_FD, R32_TOTAL, R32_NOACC, "
				+ "    R33_PRODUCT, R33_CURRENT, R33_CALL, R33_SAVINGS, R33_0_31D_NOTICE, R33_32_88D_NOTICE, R33_91D_DEPOSIT, R33_1_2M_FD, R33_4_6M_FD, R33_7_12M_FD, R33_13_18M_FD, R33_19_24M_FD, R33_OVER24_FD, R33_TOTAL, R33_NOACC, "
				+ "    R34_PRODUCT, R34_CURRENT, R34_CALL, R34_SAVINGS, R34_0_31D_NOTICE, R34_32_88D_NOTICE, R34_91D_DEPOSIT, R34_1_2M_FD, R34_4_6M_FD, R34_7_12M_FD, R34_13_18M_FD, R34_19_24M_FD, R34_OVER24_FD, R34_TOTAL, R34_NOACC, "
				+ "    R35_PRODUCT, R35_CURRENT, R35_CALL, R35_SAVINGS, R35_0_31D_NOTICE, R35_32_88D_NOTICE, R35_91D_DEPOSIT, R35_1_2M_FD, R35_4_6M_FD, R35_7_12M_FD, R35_13_18M_FD, R35_19_24M_FD, R35_OVER24_FD, R35_TOTAL, R35_NOACC, "
				+ "    R36_PRODUCT, R36_CURRENT, R36_CALL, R36_SAVINGS, R36_0_31D_NOTICE, R36_32_88D_NOTICE, R36_91D_DEPOSIT, R36_1_2M_FD, R36_4_6M_FD, R36_7_12M_FD, R36_13_18M_FD, R36_19_24M_FD, R36_OVER24_FD, R36_TOTAL, R36_NOACC, "
				+ "    R37_PRODUCT, R37_CURRENT, R37_CALL, R37_SAVINGS, R37_0_31D_NOTICE, R37_32_88D_NOTICE, R37_91D_DEPOSIT, R37_1_2M_FD, R37_4_6M_FD, R37_7_12M_FD, R37_13_18M_FD, R37_19_24M_FD, R37_OVER24_FD, R37_TOTAL, R37_NOACC, "
				+ "    R38_PRODUCT, R38_CURRENT, R38_CALL, R38_SAVINGS, R38_0_31D_NOTICE, R38_32_88D_NOTICE, R38_91D_DEPOSIT, R38_1_2M_FD, R38_4_6M_FD, R38_7_12M_FD, R38_13_18M_FD, R38_19_24M_FD, R38_OVER24_FD, R38_TOTAL, R38_NOACC, "
				+ "    R39_PRODUCT, R39_CURRENT, R39_CALL, R39_SAVINGS, R39_0_31D_NOTICE, R39_32_88D_NOTICE, R39_91D_DEPOSIT, R39_1_2M_FD, R39_4_6M_FD, R39_7_12M_FD, R39_13_18M_FD, R39_19_24M_FD, R39_OVER24_FD, R39_TOTAL, R39_NOACC, "
				+ "    R40_PRODUCT, R40_CURRENT, R40_CALL, R40_SAVINGS, R40_0_31D_NOTICE, R40_32_88D_NOTICE, R40_91D_DEPOSIT, R40_1_2M_FD, R40_4_6M_FD, R40_7_12M_FD, R40_13_18M_FD, R40_19_24M_FD, R40_OVER24_FD, R40_TOTAL, R40_NOACC, "
				+ "    R41_PRODUCT, R41_CURRENT, R41_CALL, R41_SAVINGS, R41_0_31D_NOTICE, R41_32_88D_NOTICE, R41_91D_DEPOSIT, R41_1_2M_FD, R41_4_6M_FD, R41_7_12M_FD, R41_13_18M_FD, R41_19_24M_FD, R41_OVER24_FD, R41_TOTAL, R41_NOACC, "
				+ "    R42_PRODUCT, R42_CURRENT, R42_CALL, R42_SAVINGS, R42_0_31D_NOTICE, R42_32_88D_NOTICE, R42_91D_DEPOSIT, R42_1_2M_FD, R42_4_6M_FD, R42_7_12M_FD, R42_13_18M_FD, R42_19_24M_FD, R42_OVER24_FD, R42_TOTAL, R42_NOACC, "
				+ "    R43_PRODUCT, R43_CURRENT, R43_CALL, R43_SAVINGS, R43_0_31D_NOTICE, R43_32_88D_NOTICE, R43_91D_DEPOSIT, R43_1_2M_FD, R43_4_6M_FD, R43_7_12M_FD, R43_13_18M_FD, R43_19_24M_FD, R43_OVER24_FD, R43_TOTAL, R43_NOACC, "
				+ "    R44_PRODUCT, R44_CURRENT, R44_CALL, R44_SAVINGS, R44_0_31D_NOTICE, R44_32_88D_NOTICE, R44_91D_DEPOSIT, R44_1_2M_FD, R44_4_6M_FD, R44_7_12M_FD, R44_13_18M_FD, R44_19_24M_FD, R44_OVER24_FD, R44_TOTAL, R44_NOACC, "
				+ "    R45_PRODUCT, R45_CURRENT, R45_CALL, R45_SAVINGS, R45_0_31D_NOTICE, R45_32_88D_NOTICE, R45_91D_DEPOSIT, R45_1_2M_FD, R45_4_6M_FD, R45_7_12M_FD, R45_13_18M_FD, R45_19_24M_FD, R45_OVER24_FD, R45_TOTAL, R45_NOACC, "
				+ "    R46_PRODUCT, R46_CURRENT, R46_CALL, R46_SAVINGS, R46_0_31D_NOTICE, R46_32_88D_NOTICE, R46_91D_DEPOSIT, R46_1_2M_FD, R46_4_6M_FD, R46_7_12M_FD, R46_13_18M_FD, R46_19_24M_FD, R46_OVER24_FD, R46_TOTAL, R46_NOACC, "
				+ "    R47_PRODUCT, R47_CURRENT, R47_CALL, R47_SAVINGS, R47_0_31D_NOTICE, R47_32_88D_NOTICE, R47_91D_DEPOSIT, R47_1_2M_FD, R47_4_6M_FD, R47_7_12M_FD, R47_13_18M_FD, R47_19_24M_FD, R47_OVER24_FD, R47_TOTAL, R47_NOACC, "
				+ "    R48_PRODUCT, R48_CURRENT, R48_CALL, R48_SAVINGS, R48_0_31D_NOTICE, R48_32_88D_NOTICE, R48_91D_DEPOSIT, R48_1_2M_FD, R48_4_6M_FD, R48_7_12M_FD, R48_13_18M_FD, R48_19_24M_FD, R48_OVER24_FD, R48_TOTAL, R48_NOACC, "
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				// Select corresponding columns from MOD table
				+ "    S.R11_PRODUCT, S.R11_CURRENT, S.R11_CALL, S.R11_SAVINGS, S.R11_0_31D_NOTICE, S.R11_32_88D_NOTICE, S.R11_91D_DEPOSIT, S.R11_1_2M_FD, S.R11_4_6M_FD, S.R11_7_12M_FD, S.R11_13_18M_FD, S.R11_19_24M_FD, S.R11_OVER24_FD, S.R11_TOTAL, S.R11_NOACC, "
				+ "    S.R12_PRODUCT, S.R12_CURRENT, S.R12_CALL, S.R12_SAVINGS, S.R12_0_31D_NOTICE, S.R12_32_88D_NOTICE, S.R12_91D_DEPOSIT, S.R12_1_2M_FD, S.R12_4_6M_FD, S.R12_7_12M_FD, S.R12_13_18M_FD, S.R12_19_24M_FD, S.R12_OVER24_FD, S.R12_TOTAL, S.R12_NOACC, "
				+ "    S.R13_PRODUCT, S.R13_CURRENT, S.R13_CALL, S.R13_SAVINGS, S.R13_0_31D_NOTICE, S.R13_32_88D_NOTICE, S.R13_91D_DEPOSIT, S.R13_1_2M_FD, S.R13_4_6M_FD, S.R13_7_12M_FD, S.R13_13_18M_FD, S.R13_19_24M_FD, S.R13_OVER24_FD, S.R13_TOTAL, S.R13_NOACC, "
				+ "    S.R14_PRODUCT, S.R14_CURRENT, S.R14_CALL, S.R14_SAVINGS, S.R14_0_31D_NOTICE, S.R14_32_88D_NOTICE, S.R14_91D_DEPOSIT, S.R14_1_2M_FD, S.R14_4_6M_FD, S.R14_7_12M_FD, S.R14_13_18M_FD, S.R14_19_24M_FD, S.R14_OVER24_FD, S.R14_TOTAL, S.R14_NOACC, "
				+ "    S.R15_PRODUCT, S.R15_CURRENT, S.R15_CALL, S.R15_SAVINGS, S.R15_0_31D_NOTICE, S.R15_32_88D_NOTICE, S.R15_91D_DEPOSIT, S.R15_1_2M_FD, S.R15_4_6M_FD, S.R15_7_12M_FD, S.R15_13_18M_FD, S.R15_19_24M_FD, S.R15_OVER24_FD, S.R15_TOTAL, S.R15_NOACC, "
				+ "    S.R16_PRODUCT, S.R16_CURRENT, S.R16_CALL, S.R16_SAVINGS, S.R16_0_31D_NOTICE, S.R16_32_88D_NOTICE, S.R16_91D_DEPOSIT, S.R16_1_2M_FD, S.R16_4_6M_FD, S.R16_7_12M_FD, S.R16_13_18M_FD, S.R16_19_24M_FD, S.R16_OVER24_FD, S.R16_TOTAL, S.R16_NOACC, "
				+ "    S.R17_PRODUCT, S.R17_CURRENT, S.R17_CALL, S.R17_SAVINGS, S.R17_0_31D_NOTICE, S.R17_32_88D_NOTICE, S.R17_91D_DEPOSIT, S.R17_1_2M_FD, S.R17_4_6M_FD, S.R17_7_12M_FD, S.R17_13_18M_FD, S.R17_19_24M_FD, S.R17_OVER24_FD, S.R17_TOTAL, S.R17_NOACC, "
				+ "    S.R18_PRODUCT, S.R18_CURRENT, S.R18_CALL, S.R18_SAVINGS, S.R18_0_31D_NOTICE, S.R18_32_88D_NOTICE, S.R18_91D_DEPOSIT, S.R18_1_2M_FD, S.R18_4_6M_FD, S.R18_7_12M_FD, S.R18_13_18M_FD, S.R18_19_24M_FD, S.R18_OVER24_FD, S.R18_TOTAL, S.R18_NOACC, "
				+ "    S.R19_PRODUCT, S.R19_CURRENT, S.R19_CALL, S.R19_SAVINGS, S.R19_0_31D_NOTICE, S.R19_32_88D_NOTICE, S.R19_91D_DEPOSIT, S.R19_1_2M_FD, S.R19_4_6M_FD, S.R19_7_12M_FD, S.R19_13_18M_FD, S.R19_19_24M_FD, S.R19_OVER24_FD, S.R19_TOTAL, S.R19_NOACC, "
				+ "    S.R20_PRODUCT, S.R20_CURRENT, S.R20_CALL, S.R20_SAVINGS, S.R20_0_31D_NOTICE, S.R20_32_88D_NOTICE, S.R20_91D_DEPOSIT, S.R20_1_2M_FD, S.R20_4_6M_FD, S.R20_7_12M_FD, S.R20_13_18M_FD, S.R20_19_24M_FD, S.R20_OVER24_FD, S.R20_TOTAL, S.R20_NOACC, "
				+ "    S.R21_PRODUCT, S.R21_CURRENT, S.R21_CALL, S.R21_SAVINGS, S.R21_0_31D_NOTICE, S.R21_32_88D_NOTICE, S.R21_91D_DEPOSIT, S.R21_1_2M_FD, S.R21_4_6M_FD, S.R21_7_12M_FD, S.R21_13_18M_FD, S.R21_19_24M_FD, S.R21_OVER24_FD, S.R21_TOTAL, S.R21_NOACC, "
				+ "    S.R22_PRODUCT, S.R22_CURRENT, S.R22_CALL, S.R22_SAVINGS, S.R22_0_31D_NOTICE, S.R22_32_88D_NOTICE, S.R22_91D_DEPOSIT, S.R22_1_2M_FD, S.R22_4_6M_FD, S.R22_7_12M_FD, S.R22_13_18M_FD, S.R22_19_24M_FD, S.R22_OVER24_FD, S.R22_TOTAL, S.R22_NOACC, "
				+ "    S.R23_PRODUCT, S.R23_CURRENT, S.R23_CALL, S.R23_SAVINGS, S.R23_0_31D_NOTICE, S.R23_32_88D_NOTICE, S.R23_91D_DEPOSIT, S.R23_1_2M_FD, S.R23_4_6M_FD, S.R23_7_12M_FD, S.R23_13_18M_FD, S.R23_19_24M_FD, S.R23_OVER24_FD, S.R23_TOTAL, S.R23_NOACC, "
				+ "    S.R24_PRODUCT, S.R24_CURRENT, S.R24_CALL, S.R24_SAVINGS, S.R24_0_31D_NOTICE, S.R24_32_88D_NOTICE, S.R24_91D_DEPOSIT, S.R24_1_2M_FD, S.R24_4_6M_FD, S.R24_7_12M_FD, S.R24_13_18M_FD, S.R24_19_24M_FD, S.R24_OVER24_FD, S.R24_TOTAL, S.R24_NOACC, "
				+ "    S.R25_PRODUCT, S.R25_CURRENT, S.R25_CALL, S.R25_SAVINGS, S.R25_0_31D_NOTICE, S.R25_32_88D_NOTICE, S.R25_91D_DEPOSIT, S.R25_1_2M_FD, S.R25_4_6M_FD, S.R25_7_12M_FD, S.R25_13_18M_FD, S.R25_19_24M_FD, S.R25_OVER24_FD, S.R25_TOTAL, S.R25_NOACC, "
				+ "    S.R26_PRODUCT, S.R26_CURRENT, S.R26_CALL, S.R26_SAVINGS, S.R26_0_31D_NOTICE, S.R26_32_88D_NOTICE, S.R26_91D_DEPOSIT, S.R26_1_2M_FD, S.R26_4_6M_FD, S.R26_7_12M_FD, S.R26_13_18M_FD, S.R26_19_24M_FD, S.R26_OVER24_FD, S.R26_TOTAL, S.R26_NOACC, "
				+ "    S.R32_PRODUCT, S.R32_CURRENT, S.R32_CALL, S.R32_SAVINGS, S.R32_0_31D_NOTICE, S.R32_32_88D_NOTICE, S.R32_91D_DEPOSIT, S.R32_1_2M_FD, S.R32_4_6M_FD, S.R32_7_12M_FD, S.R32_13_18M_FD, S.R32_19_24M_FD, S.R32_OVER24_FD, S.R32_TOTAL, S.R32_NOACC, "
				+ "    S.R33_PRODUCT, S.R33_CURRENT, S.R33_CALL, S.R33_SAVINGS, S.R33_0_31D_NOTICE, S.R33_32_88D_NOTICE, S.R33_91D_DEPOSIT, S.R33_1_2M_FD, S.R33_4_6M_FD, S.R33_7_12M_FD, S.R33_13_18M_FD, S.R33_19_24M_FD, S.R33_OVER24_FD, S.R33_TOTAL, S.R33_NOACC, "
				+ "    S.R34_PRODUCT, S.R34_CURRENT, S.R34_CALL, S.R34_SAVINGS, S.R34_0_31D_NOTICE, S.R34_32_88D_NOTICE, S.R34_91D_DEPOSIT, S.R34_1_2M_FD, S.R34_4_6M_FD, S.R34_7_12M_FD, S.R34_13_18M_FD, S.R34_19_24M_FD, S.R34_OVER24_FD, S.R34_TOTAL, S.R34_NOACC, "
				+ "    S.R35_PRODUCT, S.R35_CURRENT, S.R35_CALL, S.R35_SAVINGS, S.R35_0_31D_NOTICE, S.R35_32_88D_NOTICE, S.R35_91D_DEPOSIT, S.R35_1_2M_FD, S.R35_4_6M_FD, S.R35_7_12M_FD, S.R35_13_18M_FD, S.R35_19_24M_FD, S.R35_OVER24_FD, S.R35_TOTAL, S.R35_NOACC, "
				+ "    S.R36_PRODUCT, S.R36_CURRENT, S.R36_CALL, S.R36_SAVINGS, S.R36_0_31D_NOTICE, S.R36_32_88D_NOTICE, S.R36_91D_DEPOSIT, S.R36_1_2M_FD, S.R36_4_6M_FD, S.R36_7_12M_FD, S.R36_13_18M_FD, S.R36_19_24M_FD, S.R36_OVER24_FD, S.R36_TOTAL, S.R36_NOACC, "
				+ "    S.R37_PRODUCT, S.R37_CURRENT, S.R37_CALL, S.R37_SAVINGS, S.R37_0_31D_NOTICE, S.R37_32_88D_NOTICE, S.R37_91D_DEPOSIT, S.R37_1_2M_FD, S.R37_4_6M_FD, S.R37_7_12M_FD, S.R37_13_18M_FD, S.R37_19_24M_FD, S.R37_OVER24_FD, S.R37_TOTAL, S.R37_NOACC, "
				+ "    S.R38_PRODUCT, S.R38_CURRENT, S.R38_CALL, S.R38_SAVINGS, S.R38_0_31D_NOTICE, S.R38_32_88D_NOTICE, S.R38_91D_DEPOSIT, S.R38_1_2M_FD, S.R38_4_6M_FD, S.R38_7_12M_FD, S.R38_13_18M_FD, S.R38_19_24M_FD, S.R38_OVER24_FD, S.R38_TOTAL, S.R38_NOACC, "
				+ "    S.R39_PRODUCT, S.R39_CURRENT, S.R39_CALL, S.R39_SAVINGS, S.R39_0_31D_NOTICE, S.R39_32_88D_NOTICE, S.R39_91D_DEPOSIT, S.R39_1_2M_FD, S.R39_4_6M_FD, S.R39_7_12M_FD, S.R39_13_18M_FD, S.R39_19_24M_FD, S.R39_OVER24_FD, S.R39_TOTAL, S.R39_NOACC, "
				+ "    S.R40_PRODUCT, S.R40_CURRENT, S.R40_CALL, S.R40_SAVINGS, S.R40_0_31D_NOTICE, S.R40_32_88D_NOTICE, S.R40_91D_DEPOSIT, S.R40_1_2M_FD, S.R40_4_6M_FD, S.R40_7_12M_FD, S.R40_13_18M_FD, S.R40_19_24M_FD, S.R40_OVER24_FD, S.R40_TOTAL, S.R40_NOACC, "
				+ "    S.R41_PRODUCT, S.R41_CURRENT, S.R41_CALL, S.R41_SAVINGS, S.R41_0_31D_NOTICE, S.R41_32_88D_NOTICE, S.R41_91D_DEPOSIT, S.R41_1_2M_FD, S.R41_4_6M_FD, S.R41_7_12M_FD, S.R41_13_18M_FD, S.R41_19_24M_FD, S.R41_OVER24_FD, S.R41_TOTAL, S.R41_NOACC, "
				+ "    S.R42_PRODUCT, S.R42_CURRENT, S.R42_CALL, S.R42_SAVINGS, S.R42_0_31D_NOTICE, S.R42_32_88D_NOTICE, S.R42_91D_DEPOSIT, S.R42_1_2M_FD, S.R42_4_6M_FD, S.R42_7_12M_FD, S.R42_13_18M_FD, S.R42_19_24M_FD, S.R42_OVER24_FD, S.R42_TOTAL, S.R42_NOACC, "
				+ "    S.R43_PRODUCT, S.R43_CURRENT, S.R43_CALL, S.R43_SAVINGS, S.R43_0_31D_NOTICE, S.R43_32_88D_NOTICE, S.R43_91D_DEPOSIT, S.R43_1_2M_FD, S.R43_4_6M_FD, S.R43_7_12M_FD, S.R43_13_18M_FD, S.R43_19_24M_FD, S.R43_OVER24_FD, S.R43_TOTAL, S.R43_NOACC, "
				+ "    S.R44_PRODUCT, S.R44_CURRENT, S.R44_CALL, S.R44_SAVINGS, S.R44_0_31D_NOTICE, S.R44_32_88D_NOTICE, S.R44_91D_DEPOSIT, S.R44_1_2M_FD, S.R44_4_6M_FD, S.R44_7_12M_FD, S.R44_13_18M_FD, S.R44_19_24M_FD, S.R44_OVER24_FD, S.R44_TOTAL, S.R44_NOACC, "
				+ "    S.R45_PRODUCT, S.R45_CURRENT, S.R45_CALL, S.R45_SAVINGS, S.R45_0_31D_NOTICE, S.R45_32_88D_NOTICE, S.R45_91D_DEPOSIT, S.R45_1_2M_FD, S.R45_4_6M_FD, S.R45_7_12M_FD, S.R45_13_18M_FD, S.R45_19_24M_FD, S.R45_OVER24_FD, S.R45_TOTAL, S.R45_NOACC, "
				+ "    S.R46_PRODUCT, S.R46_CURRENT, S.R46_CALL, S.R46_SAVINGS, S.R46_0_31D_NOTICE, S.R46_32_88D_NOTICE, S.R46_91D_DEPOSIT, S.R46_1_2M_FD, S.R46_4_6M_FD, S.R46_7_12M_FD, S.R46_13_18M_FD, S.R46_19_24M_FD, S.R46_OVER24_FD, S.R46_TOTAL, S.R46_NOACC, "
				+ "    S.R47_PRODUCT, S.R47_CURRENT, S.R47_CALL, S.R47_SAVINGS, S.R47_0_31D_NOTICE, S.R47_32_88D_NOTICE, S.R47_91D_DEPOSIT, S.R47_1_2M_FD, S.R47_4_6M_FD, S.R47_7_12M_FD, S.R47_13_18M_FD, S.R47_19_24M_FD, S.R47_OVER24_FD, S.R47_TOTAL, S.R47_NOACC, "
				+ "    S.R48_PRODUCT, S.R48_CURRENT, S.R48_CALL, S.R48_SAVINGS, S.R48_0_31D_NOTICE, S.R48_32_88D_NOTICE, S.R48_91D_DEPOSIT, S.R48_1_2M_FD, S.R48_4_6M_FD, S.R48_7_12M_FD, S.R48_13_18M_FD, S.R48_19_24M_FD, S.R48_OVER24_FD, S.R48_TOTAL, S.R48_NOACC, "
				+ "    S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_Q_SMME_DEP_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("Q_SMME_DEP modification record not found for date : " + reportDate);
		}

		System.out.println("Q_SMME_DEP Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_Q_SMME_DEP_DETAILTABLE from MOD table
		String updateDetailSql = updateSummarySql.replace("BRRS_Q_SMME_DEP_SUMMARYTABLE",
				"BRRS_Q_SMME_DEP_DETAILTABLE");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		System.out.println("Q_SMME_DEP Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_Q_SMME_DEP_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("Q_SMME_DEP MOD Updated : " + modUpdated);
		System.out.println("Q_SMME_DEP Verification and Transfer Completed Successfully");
	}
	
	
	private void transferCA4Data(AuditServicesEntity auditRecord) {
	    String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_CA4_MOD_SUMMARYTABLE";
	    java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

	    if (reportDate == null) {
	        throw new IllegalStateException("No modified M_CA4 record available for verification.");
	    }

	    System.out.println("M_CA4 Report Date : " + reportDate);

	    // 1. Update BRRS_M_CA4_SUMMARYTABLE from MOD
	    String updateSummarySql = "UPDATE BRRS_M_CA4_SUMMARYTABLE M " + "SET ("
	        + "    R10_ITEM, R10_AMT_NAME_OF_SUB1, R10_AMT_NAME_OF_SUB2, R10_AMT_NAME_OF_SUB3, R10_AMT_NAME_OF_SUB4, R10_AMT_NAME_OF_SUB5, R10_TOT_AMT, "
	        + "    R11_ITEM, R11_AMT_NAME_OF_SUB1, R11_AMT_NAME_OF_SUB2, R11_AMT_NAME_OF_SUB3, R11_AMT_NAME_OF_SUB4, R11_AMT_NAME_OF_SUB5, R11_TOT_AMT, "
	        + "    R12_ITEM, R12_AMT_NAME_OF_SUB1, R12_AMT_NAME_OF_SUB2, R12_AMT_NAME_OF_SUB3, R12_AMT_NAME_OF_SUB4, R12_AMT_NAME_OF_SUB5, R12_TOT_AMT, "
	        + "    R13_ITEM, R13_AMT_NAME_OF_SUB1, R13_AMT_NAME_OF_SUB2, R13_AMT_NAME_OF_SUB3, R13_AMT_NAME_OF_SUB4, R13_AMT_NAME_OF_SUB5, R13_TOT_AMT, "
	        + "    R14_ITEM, R14_AMT_NAME_OF_SUB1, R14_AMT_NAME_OF_SUB2, R14_AMT_NAME_OF_SUB3, R14_AMT_NAME_OF_SUB4, R14_AMT_NAME_OF_SUB5, R14_TOT_AMT, "
	        + "    R17_ITEM, R17_AMT_NAME_OF_SUB1, R17_AMT_NAME_OF_SUB2, R17_AMT_NAME_OF_SUB3, R17_AMT_NAME_OF_SUB4, R17_AMT_NAME_OF_SUB5, R17_TOT_AMT, "
	        + "    R18_ITEM, R18_AMT_NAME_OF_SUB1, R18_AMT_NAME_OF_SUB2, R18_AMT_NAME_OF_SUB3, R18_AMT_NAME_OF_SUB4, R18_AMT_NAME_OF_SUB5, R18_TOT_AMT, "
	        + "    R19_ITEM, R19_AMT_NAME_OF_SUB1, R19_AMT_NAME_OF_SUB2, R19_AMT_NAME_OF_SUB3, R19_AMT_NAME_OF_SUB4, R19_AMT_NAME_OF_SUB5, R19_TOT_AMT, "
	        + "    R22_ITEM, R22_AMT_NAME_OF_SUB1, R22_AMT_NAME_OF_SUB2, R22_AMT_NAME_OF_SUB3, R22_AMT_NAME_OF_SUB4, R22_AMT_NAME_OF_SUB5, R22_TOT_AMT, "
	        + "    R23_ITEM, R23_AMT_NAME_OF_SUB1, R23_AMT_NAME_OF_SUB2, R23_AMT_NAME_OF_SUB3, R23_AMT_NAME_OF_SUB4, R23_AMT_NAME_OF_SUB5, R23_TOT_AMT, "
	        + "    R24_ITEM, R24_AMT_NAME_OF_SUB1, R24_AMT_NAME_OF_SUB2, R24_AMT_NAME_OF_SUB3, R24_AMT_NAME_OF_SUB4, R24_AMT_NAME_OF_SUB5, R24_TOT_AMT, "
	        + "    R27_ITEM, R27_AMT_NAME_OF_SUB1, R27_AMT_NAME_OF_SUB2, R27_AMT_NAME_OF_SUB3, R27_AMT_NAME_OF_SUB4, R27_AMT_NAME_OF_SUB5, R27_TOT_AMT, "
	        + "    R28_ITEM, R28_AMT_NAME_OF_SUB1, R28_AMT_NAME_OF_SUB2, R28_AMT_NAME_OF_SUB3, R28_AMT_NAME_OF_SUB4, R28_AMT_NAME_OF_SUB5, R28_TOT_AMT, "
	        + "    R29_ITEM, R29_AMT_NAME_OF_SUB1, R29_AMT_NAME_OF_SUB2, R29_AMT_NAME_OF_SUB3, R29_AMT_NAME_OF_SUB4, R29_AMT_NAME_OF_SUB5, R29_TOT_AMT, "
	        + "    R32_ITEM, R32_AMT_NAME_OF_SUB1, R32_AMT_NAME_OF_SUB2, R32_AMT_NAME_OF_SUB3, R32_AMT_NAME_OF_SUB4, R32_AMT_NAME_OF_SUB5, R32_TOT_AMT, "
	        + "    R33_ITEM, R33_AMT_NAME_OF_SUB1, R33_AMT_NAME_OF_SUB2, R33_AMT_NAME_OF_SUB3, R33_AMT_NAME_OF_SUB4, R33_AMT_NAME_OF_SUB5, R33_TOT_AMT, "
	        + "    R34_ITEM, R34_AMT_NAME_OF_SUB1, R34_AMT_NAME_OF_SUB2, R34_AMT_NAME_OF_SUB3, R34_AMT_NAME_OF_SUB4, R34_AMT_NAME_OF_SUB5, R34_TOT_AMT, "
	        + "    R36_ITEM, R36_AMT_NAME_OF_SUB1, R36_AMT_NAME_OF_SUB2, R36_AMT_NAME_OF_SUB3, R36_AMT_NAME_OF_SUB4, R36_AMT_NAME_OF_SUB5, R36_TOT_AMT, "
	        + "    R37_ITEM, R37_AMT_NAME_OF_SUB1, R37_AMT_NAME_OF_SUB2, R37_AMT_NAME_OF_SUB3, R37_AMT_NAME_OF_SUB4, R37_AMT_NAME_OF_SUB5, R37_TOT_AMT, "
	        + "    R38_ITEM, R38_AMT_NAME_OF_SUB1, R38_AMT_NAME_OF_SUB2, R38_AMT_NAME_OF_SUB3, R38_AMT_NAME_OF_SUB4, R38_AMT_NAME_OF_SUB5, R38_TOT_AMT, "
	        + "    R39_ITEM, R39_AMT_NAME_OF_SUB1, R39_AMT_NAME_OF_SUB2, R39_AMT_NAME_OF_SUB3, R39_AMT_NAME_OF_SUB4, R39_AMT_NAME_OF_SUB5, R39_TOT_AMT, "
	        + "    R40_ITEM, R40_AMT_NAME_OF_SUB1, R40_AMT_NAME_OF_SUB2, R40_AMT_NAME_OF_SUB3, R40_AMT_NAME_OF_SUB4, R40_AMT_NAME_OF_SUB5, R40_TOT_AMT, "
	        + "    R41_ITEM, R41_AMT_NAME_OF_SUB1, R41_AMT_NAME_OF_SUB2, R41_AMT_NAME_OF_SUB3, R41_AMT_NAME_OF_SUB4, R41_AMT_NAME_OF_SUB5, R41_TOT_AMT, "
	        + "    R42_ITEM, R42_AMT_NAME_OF_SUB1, R42_AMT_NAME_OF_SUB2, R42_AMT_NAME_OF_SUB3, R42_AMT_NAME_OF_SUB4, R42_AMT_NAME_OF_SUB5, R42_TOT_AMT, "
	        + "    R43_ITEM, R43_AMT_NAME_OF_SUB1, R43_AMT_NAME_OF_SUB2, R43_AMT_NAME_OF_SUB3, R43_AMT_NAME_OF_SUB4, R43_AMT_NAME_OF_SUB5, R43_TOT_AMT, "
	        + "    R44_ITEM, R44_AMT_NAME_OF_SUB1, R44_AMT_NAME_OF_SUB2, R44_AMT_NAME_OF_SUB3, R44_AMT_NAME_OF_SUB4, R44_AMT_NAME_OF_SUB5, R44_TOT_AMT, "
	        + "    R45_ITEM, R45_AMT_NAME_OF_SUB1, R45_AMT_NAME_OF_SUB2, R45_AMT_NAME_OF_SUB3, R45_AMT_NAME_OF_SUB4, R45_AMT_NAME_OF_SUB5, R45_TOT_AMT, "
	        + "    R46_ITEM, R46_AMT_NAME_OF_SUB1, R46_AMT_NAME_OF_SUB2, R46_AMT_NAME_OF_SUB3, R46_AMT_NAME_OF_SUB4, R46_AMT_NAME_OF_SUB5, R46_TOT_AMT, "
	        + "    R47_ITEM, R47_AMT_NAME_OF_SUB1, R47_AMT_NAME_OF_SUB2, R47_AMT_NAME_OF_SUB3, R47_AMT_NAME_OF_SUB4, R47_AMT_NAME_OF_SUB5, R47_TOT_AMT, "
	        + "    R48_ITEM, R48_AMT_NAME_OF_SUB1, R48_AMT_NAME_OF_SUB2, R48_AMT_NAME_OF_SUB3, R48_AMT_NAME_OF_SUB4, R48_AMT_NAME_OF_SUB5, R48_TOT_AMT, "
	        + "    R49_ITEM, R49_AMT_NAME_OF_SUB1, R49_AMT_NAME_OF_SUB2, R49_AMT_NAME_OF_SUB3, R49_AMT_NAME_OF_SUB4, R49_AMT_NAME_OF_SUB5, R49_TOT_AMT, "
	        + "    R50_ITEM, R50_AMT_NAME_OF_SUB1, R50_AMT_NAME_OF_SUB2, R50_AMT_NAME_OF_SUB3, R50_AMT_NAME_OF_SUB4, R50_AMT_NAME_OF_SUB5, R50_TOT_AMT, "
	        + "    R51_ITEM, R51_AMT_NAME_OF_SUB1, R51_AMT_NAME_OF_SUB2, R51_AMT_NAME_OF_SUB3, R51_AMT_NAME_OF_SUB4, R51_AMT_NAME_OF_SUB5, R51_TOT_AMT, "
	        + "    R52_ITEM, R52_AMT_NAME_OF_SUB1, R52_AMT_NAME_OF_SUB2, R52_AMT_NAME_OF_SUB3, R52_AMT_NAME_OF_SUB4, R52_AMT_NAME_OF_SUB5, R52_TOT_AMT, "
	        + "    R53_ITEM, R53_AMT_NAME_OF_SUB1, R53_AMT_NAME_OF_SUB2, R53_AMT_NAME_OF_SUB3, R53_AMT_NAME_OF_SUB4, R53_AMT_NAME_OF_SUB5, R53_TOT_AMT, "
	        + "    R54_ITEM, R54_AMT_NAME_OF_SUB1, R54_AMT_NAME_OF_SUB2, R54_AMT_NAME_OF_SUB3, R54_AMT_NAME_OF_SUB4, R54_AMT_NAME_OF_SUB5, R54_TOT_AMT, "
	        + "    R55_ITEM, R55_AMT_NAME_OF_SUB1, R55_AMT_NAME_OF_SUB2, R55_AMT_NAME_OF_SUB3, R55_AMT_NAME_OF_SUB4, R55_AMT_NAME_OF_SUB5, R55_TOT_AMT, "
	        + "    R56_ITEM, R56_AMT_NAME_OF_SUB1, R56_AMT_NAME_OF_SUB2, R56_AMT_NAME_OF_SUB3, R56_AMT_NAME_OF_SUB4, R56_AMT_NAME_OF_SUB5, R56_TOT_AMT, "
	        + "    R57_ITEM, R57_AMT_NAME_OF_SUB1, R57_AMT_NAME_OF_SUB2, R57_AMT_NAME_OF_SUB3, R57_AMT_NAME_OF_SUB4, R57_AMT_NAME_OF_SUB5, R57_TOT_AMT, "
	        + "    R58_ITEM, R58_AMT_NAME_OF_SUB1, R58_AMT_NAME_OF_SUB2, R58_AMT_NAME_OF_SUB3, R58_AMT_NAME_OF_SUB4, R58_AMT_NAME_OF_SUB5, R58_TOT_AMT, "
	        + "    ENTITY_FLG, MODIFY_FLG, DEL_FLG"
	        + ") = ("
	        + "    SELECT "
	        + "    S.R10_ITEM, S.R10_AMT_NAME_OF_SUB1, S.R10_AMT_NAME_OF_SUB2, S.R10_AMT_NAME_OF_SUB3, S.R10_AMT_NAME_OF_SUB4, S.R10_AMT_NAME_OF_SUB5, S.R10_TOT_AMT, "
	        + "    S.R11_ITEM, S.R11_AMT_NAME_OF_SUB1, S.R11_AMT_NAME_OF_SUB2, S.R11_AMT_NAME_OF_SUB3, S.R11_AMT_NAME_OF_SUB4, S.R11_AMT_NAME_OF_SUB5, S.R11_TOT_AMT, "
	        + "    S.R12_ITEM, S.R12_AMT_NAME_OF_SUB1, S.R12_AMT_NAME_OF_SUB2, S.R12_AMT_NAME_OF_SUB3, S.R12_AMT_NAME_OF_SUB4, S.R12_AMT_NAME_OF_SUB5, S.R12_TOT_AMT, "
	        + "    S.R13_ITEM, S.R13_AMT_NAME_OF_SUB1, S.R13_AMT_NAME_OF_SUB2, S.R13_AMT_NAME_OF_SUB3, S.R13_AMT_NAME_OF_SUB4, S.R13_AMT_NAME_OF_SUB5, S.R13_TOT_AMT, "
	        + "    S.R14_ITEM, S.R14_AMT_NAME_OF_SUB1, S.R14_AMT_NAME_OF_SUB2, S.R14_AMT_NAME_OF_SUB3, S.R14_AMT_NAME_OF_SUB4, S.R14_AMT_NAME_OF_SUB5, S.R14_TOT_AMT, "
	        + "    S.R17_ITEM, S.R17_AMT_NAME_OF_SUB1, S.R17_AMT_NAME_OF_SUB2, S.R17_AMT_NAME_OF_SUB3, S.R17_AMT_NAME_OF_SUB4, S.R17_AMT_NAME_OF_SUB5, S.R17_TOT_AMT, "
	        + "    S.R18_ITEM, S.R18_AMT_NAME_OF_SUB1, S.R18_AMT_NAME_OF_SUB2, S.R18_AMT_NAME_OF_SUB3, S.R18_AMT_NAME_OF_SUB4, S.R18_AMT_NAME_OF_SUB5, S.R18_TOT_AMT, "
	        + "    S.R19_ITEM, S.R19_AMT_NAME_OF_SUB1, S.R19_AMT_NAME_OF_SUB2, S.R19_AMT_NAME_OF_SUB3, S.R19_AMT_NAME_OF_SUB4, S.R19_AMT_NAME_OF_SUB5, S.R19_TOT_AMT, "
	        + "    S.R22_ITEM, S.R22_AMT_NAME_OF_SUB1, S.R22_AMT_NAME_OF_SUB2, S.R22_AMT_NAME_OF_SUB3, S.R22_AMT_NAME_OF_SUB4, S.R22_AMT_NAME_OF_SUB5, S.R22_TOT_AMT, "
	        + "    S.R23_ITEM, S.R23_AMT_NAME_OF_SUB1, S.R23_AMT_NAME_OF_SUB2, S.R23_AMT_NAME_OF_SUB3, S.R23_AMT_NAME_OF_SUB4, S.R23_AMT_NAME_OF_SUB5, S.R23_TOT_AMT, "
	        + "    S.R24_ITEM, S.R24_AMT_NAME_OF_SUB1, S.R24_AMT_NAME_OF_SUB2, S.R24_AMT_NAME_OF_SUB3, S.R24_AMT_NAME_OF_SUB4, S.R24_AMT_NAME_OF_SUB5, S.R24_TOT_AMT, "
	        + "    S.R27_ITEM, S.R27_AMT_NAME_OF_SUB1, S.R27_AMT_NAME_OF_SUB2, S.R27_AMT_NAME_OF_SUB3, S.R27_AMT_NAME_OF_SUB4, S.R27_AMT_NAME_OF_SUB5, S.R27_TOT_AMT, "
	        + "    S.R28_ITEM, S.R28_AMT_NAME_OF_SUB1, S.R28_AMT_NAME_OF_SUB2, S.R28_AMT_NAME_OF_SUB3, S.R28_AMT_NAME_OF_SUB4, S.R28_AMT_NAME_OF_SUB5, S.R28_TOT_AMT, "
	        + "    S.R29_ITEM, S.R29_AMT_NAME_OF_SUB1, S.R29_AMT_NAME_OF_SUB2, S.R29_AMT_NAME_OF_SUB3, S.R29_AMT_NAME_OF_SUB4, S.R29_AMT_NAME_OF_SUB5, S.R29_TOT_AMT, "
	        + "    S.R32_ITEM, S.R32_AMT_NAME_OF_SUB1, S.R32_AMT_NAME_OF_SUB2, S.R32_AMT_NAME_OF_SUB3, S.R32_AMT_NAME_OF_SUB4, S.R32_AMT_NAME_OF_SUB5, S.R32_TOT_AMT, "
	        + "    S.R33_ITEM, S.R33_AMT_NAME_OF_SUB1, S.R33_AMT_NAME_OF_SUB2, S.R33_AMT_NAME_OF_SUB3, S.R33_AMT_NAME_OF_SUB4, S.R33_AMT_NAME_OF_SUB5, S.R33_TOT_AMT, "
	        + "    S.R34_ITEM, S.R34_AMT_NAME_OF_SUB1, S.R34_AMT_NAME_OF_SUB2, S.R34_AMT_NAME_OF_SUB3, S.R34_AMT_NAME_OF_SUB4, S.R34_AMT_NAME_OF_SUB5, S.R34_TOT_AMT, "
	        + "    S.R36_ITEM, S.R36_AMT_NAME_OF_SUB1, S.R36_AMT_NAME_OF_SUB2, S.R36_AMT_NAME_OF_SUB3, S.R36_AMT_NAME_OF_SUB4, S.R36_AMT_NAME_OF_SUB5, S.R36_TOT_AMT, "
	        + "    S.R37_ITEM, S.R37_AMT_NAME_OF_SUB1, S.R37_AMT_NAME_OF_SUB2, S.R37_AMT_NAME_OF_SUB3, S.R37_AMT_NAME_OF_SUB4, S.R37_AMT_NAME_OF_SUB5, S.R37_TOT_AMT, "
	        + "    S.R38_ITEM, S.R38_AMT_NAME_OF_SUB1, S.R38_AMT_NAME_OF_SUB2, S.R38_AMT_NAME_OF_SUB3, S.R38_AMT_NAME_OF_SUB4, S.R38_AMT_NAME_OF_SUB5, S.R38_TOT_AMT, "
	        + "    S.R39_ITEM, S.R39_AMT_NAME_OF_SUB1, S.R39_AMT_NAME_OF_SUB2, S.R39_AMT_NAME_OF_SUB3, S.R39_AMT_NAME_OF_SUB4, S.R39_AMT_NAME_OF_SUB5, S.R39_TOT_AMT, "
	        + "    S.R40_ITEM, S.R40_AMT_NAME_OF_SUB1, S.R40_AMT_NAME_OF_SUB2, S.R40_AMT_NAME_OF_SUB3, S.R40_AMT_NAME_OF_SUB4, S.R40_AMT_NAME_OF_SUB5, S.R40_TOT_AMT, "
	        + "    S.R41_ITEM, S.R41_AMT_NAME_OF_SUB1, S.R41_AMT_NAME_OF_SUB2, S.R41_AMT_NAME_OF_SUB3, S.R41_AMT_NAME_OF_SUB4, S.R41_AMT_NAME_OF_SUB5, S.R41_TOT_AMT, "
	        + "    S.R42_ITEM, S.R42_AMT_NAME_OF_SUB1, S.R42_AMT_NAME_OF_SUB2, S.R42_AMT_NAME_OF_SUB3, S.R42_AMT_NAME_OF_SUB4, S.R42_AMT_NAME_OF_SUB5, S.R42_TOT_AMT, "
	        + "    S.R43_ITEM, S.R43_AMT_NAME_OF_SUB1, S.R43_AMT_NAME_OF_SUB2, S.R43_AMT_NAME_OF_SUB3, S.R43_AMT_NAME_OF_SUB4, S.R43_AMT_NAME_OF_SUB5, S.R43_TOT_AMT, "
	        + "    S.R44_ITEM, S.R44_AMT_NAME_OF_SUB1, S.R44_AMT_NAME_OF_SUB2, S.R44_AMT_NAME_OF_SUB3, S.R44_AMT_NAME_OF_SUB4, S.R44_AMT_NAME_OF_SUB5, S.R44_TOT_AMT, "
	        + "    S.R45_ITEM, S.R45_AMT_NAME_OF_SUB1, S.R45_AMT_NAME_OF_SUB2, S.R45_AMT_NAME_OF_SUB3, S.R45_AMT_NAME_OF_SUB4, S.R45_AMT_NAME_OF_SUB5, S.R45_TOT_AMT, "
	        + "    S.R46_ITEM, S.R46_AMT_NAME_OF_SUB1, S.R46_AMT_NAME_OF_SUB2, S.R46_AMT_NAME_OF_SUB3, S.R46_AMT_NAME_OF_SUB4, S.R46_AMT_NAME_OF_SUB5, S.R46_TOT_AMT, "
	        + "    S.R47_ITEM, S.R47_AMT_NAME_OF_SUB1, S.R47_AMT_NAME_OF_SUB2, S.R47_AMT_NAME_OF_SUB3, S.R47_AMT_NAME_OF_SUB4, S.R47_AMT_NAME_OF_SUB5, S.R47_TOT_AMT, "
	        + "    S.R48_ITEM, S.R48_AMT_NAME_OF_SUB1, S.R48_AMT_NAME_OF_SUB2, S.R48_AMT_NAME_OF_SUB3, S.R48_AMT_NAME_OF_SUB4, S.R48_AMT_NAME_OF_SUB5, S.R48_TOT_AMT, "
	        + "    S.R49_ITEM, S.R49_AMT_NAME_OF_SUB1, S.R49_AMT_NAME_OF_SUB2, S.R49_AMT_NAME_OF_SUB3, S.R49_AMT_NAME_OF_SUB4, S.R49_AMT_NAME_OF_SUB5, S.R49_TOT_AMT, "
	        + "    S.R50_ITEM, S.R50_AMT_NAME_OF_SUB1, S.R50_AMT_NAME_OF_SUB2, S.R50_AMT_NAME_OF_SUB3, S.R50_AMT_NAME_OF_SUB4, S.R50_AMT_NAME_OF_SUB5, S.R50_TOT_AMT, "
	        + "    S.R51_ITEM, S.R51_AMT_NAME_OF_SUB1, S.R51_AMT_NAME_OF_SUB2, S.R51_AMT_NAME_OF_SUB3, S.R51_AMT_NAME_OF_SUB4, S.R51_AMT_NAME_OF_SUB5, S.R51_TOT_AMT, "
	        + "    S.R52_ITEM, S.R52_AMT_NAME_OF_SUB1, S.R52_AMT_NAME_OF_SUB2, S.R52_AMT_NAME_OF_SUB3, S.R52_AMT_NAME_OF_SUB4, S.R52_AMT_NAME_OF_SUB5, S.R52_TOT_AMT, "
	        + "    S.R53_ITEM, S.R53_AMT_NAME_OF_SUB1, S.R53_AMT_NAME_OF_SUB2, S.R53_AMT_NAME_OF_SUB3, S.R53_AMT_NAME_OF_SUB4, S.R53_AMT_NAME_OF_SUB5, S.R53_TOT_AMT, "
	        + "    S.R54_ITEM, S.R54_AMT_NAME_OF_SUB1, S.R54_AMT_NAME_OF_SUB2, S.R54_AMT_NAME_OF_SUB3, S.R54_AMT_NAME_OF_SUB4, S.R54_AMT_NAME_OF_SUB5, S.R54_TOT_AMT, "
	        + "    S.R55_ITEM, S.R55_AMT_NAME_OF_SUB1, S.R55_AMT_NAME_OF_SUB2, S.R55_AMT_NAME_OF_SUB3, S.R55_AMT_NAME_OF_SUB4, S.R55_AMT_NAME_OF_SUB5, S.R55_TOT_AMT, "
	        + "    S.R56_ITEM, S.R56_AMT_NAME_OF_SUB1, S.R56_AMT_NAME_OF_SUB2, S.R56_AMT_NAME_OF_SUB3, S.R56_AMT_NAME_OF_SUB4, S.R56_AMT_NAME_OF_SUB5, S.R56_TOT_AMT, "
	        + "    S.R57_ITEM, S.R57_AMT_NAME_OF_SUB1, S.R57_AMT_NAME_OF_SUB2, S.R57_AMT_NAME_OF_SUB3, S.R57_AMT_NAME_OF_SUB4, S.R57_AMT_NAME_OF_SUB5, S.R57_TOT_AMT, "
	        + "    S.R58_ITEM, S.R58_AMT_NAME_OF_SUB1, S.R58_AMT_NAME_OF_SUB2, S.R58_AMT_NAME_OF_SUB3, S.R58_AMT_NAME_OF_SUB4, S.R58_AMT_NAME_OF_SUB5, S.R58_TOT_AMT, "
	        + "    S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG "
	        + "    FROM BRRS_M_CA4_MOD_SUMMARYTABLE S "
	        + "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
	        + ") "
	        + "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

	    int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

	    if (summaryUpdated == 0) {
	        throw new IllegalStateException("M_CA4 modification record not found for date : " + reportDate);
	    }
	    System.out.println("M_CA4 Summary Table Updated : " + summaryUpdated);

	    // 2. Update BRRS_M_CA4_DETAILTABLE from MOD (explicit, mirrors Q_STAFF style)
	    String updateDetailSql = "UPDATE BRRS_M_CA4_DETAILTABLE M " + "SET ("
	        + "    R10_ITEM, R10_AMT_NAME_OF_SUB1, R10_AMT_NAME_OF_SUB2, R10_AMT_NAME_OF_SUB3, R10_AMT_NAME_OF_SUB4, R10_AMT_NAME_OF_SUB5, R10_TOT_AMT, "
	        + "    R11_ITEM, R11_AMT_NAME_OF_SUB1, R11_AMT_NAME_OF_SUB2, R11_AMT_NAME_OF_SUB3, R11_AMT_NAME_OF_SUB4, R11_AMT_NAME_OF_SUB5, R11_TOT_AMT, "
	        + "    R12_ITEM, R12_AMT_NAME_OF_SUB1, R12_AMT_NAME_OF_SUB2, R12_AMT_NAME_OF_SUB3, R12_AMT_NAME_OF_SUB4, R12_AMT_NAME_OF_SUB5, R12_TOT_AMT, "
	        + "    R13_ITEM, R13_AMT_NAME_OF_SUB1, R13_AMT_NAME_OF_SUB2, R13_AMT_NAME_OF_SUB3, R13_AMT_NAME_OF_SUB4, R13_AMT_NAME_OF_SUB5, R13_TOT_AMT, "
	        + "    R14_ITEM, R14_AMT_NAME_OF_SUB1, R14_AMT_NAME_OF_SUB2, R14_AMT_NAME_OF_SUB3, R14_AMT_NAME_OF_SUB4, R14_AMT_NAME_OF_SUB5, R14_TOT_AMT, "
	        + "    R17_ITEM, R17_AMT_NAME_OF_SUB1, R17_AMT_NAME_OF_SUB2, R17_AMT_NAME_OF_SUB3, R17_AMT_NAME_OF_SUB4, R17_AMT_NAME_OF_SUB5, R17_TOT_AMT, "
	        + "    R18_ITEM, R18_AMT_NAME_OF_SUB1, R18_AMT_NAME_OF_SUB2, R18_AMT_NAME_OF_SUB3, R18_AMT_NAME_OF_SUB4, R18_AMT_NAME_OF_SUB5, R18_TOT_AMT, "
	        + "    R19_ITEM, R19_AMT_NAME_OF_SUB1, R19_AMT_NAME_OF_SUB2, R19_AMT_NAME_OF_SUB3, R19_AMT_NAME_OF_SUB4, R19_AMT_NAME_OF_SUB5, R19_TOT_AMT, "
	        + "    R22_ITEM, R22_AMT_NAME_OF_SUB1, R22_AMT_NAME_OF_SUB2, R22_AMT_NAME_OF_SUB3, R22_AMT_NAME_OF_SUB4, R22_AMT_NAME_OF_SUB5, R22_TOT_AMT, "
	        + "    R23_ITEM, R23_AMT_NAME_OF_SUB1, R23_AMT_NAME_OF_SUB2, R23_AMT_NAME_OF_SUB3, R23_AMT_NAME_OF_SUB4, R23_AMT_NAME_OF_SUB5, R23_TOT_AMT, "
	        + "    R24_ITEM, R24_AMT_NAME_OF_SUB1, R24_AMT_NAME_OF_SUB2, R24_AMT_NAME_OF_SUB3, R24_AMT_NAME_OF_SUB4, R24_AMT_NAME_OF_SUB5, R24_TOT_AMT, "
	        + "    R27_ITEM, R27_AMT_NAME_OF_SUB1, R27_AMT_NAME_OF_SUB2, R27_AMT_NAME_OF_SUB3, R27_AMT_NAME_OF_SUB4, R27_AMT_NAME_OF_SUB5, R27_TOT_AMT, "
	        + "    R28_ITEM, R28_AMT_NAME_OF_SUB1, R28_AMT_NAME_OF_SUB2, R28_AMT_NAME_OF_SUB3, R28_AMT_NAME_OF_SUB4, R28_AMT_NAME_OF_SUB5, R28_TOT_AMT, "
	        + "    R29_ITEM, R29_AMT_NAME_OF_SUB1, R29_AMT_NAME_OF_SUB2, R29_AMT_NAME_OF_SUB3, R29_AMT_NAME_OF_SUB4, R29_AMT_NAME_OF_SUB5, R29_TOT_AMT, "
	        + "    R32_ITEM, R32_AMT_NAME_OF_SUB1, R32_AMT_NAME_OF_SUB2, R32_AMT_NAME_OF_SUB3, R32_AMT_NAME_OF_SUB4, R32_AMT_NAME_OF_SUB5, R32_TOT_AMT, "
	        + "    R33_ITEM, R33_AMT_NAME_OF_SUB1, R33_AMT_NAME_OF_SUB2, R33_AMT_NAME_OF_SUB3, R33_AMT_NAME_OF_SUB4, R33_AMT_NAME_OF_SUB5, R33_TOT_AMT, "
	        + "    R34_ITEM, R34_AMT_NAME_OF_SUB1, R34_AMT_NAME_OF_SUB2, R34_AMT_NAME_OF_SUB3, R34_AMT_NAME_OF_SUB4, R34_AMT_NAME_OF_SUB5, R34_TOT_AMT, "
	        + "    R36_ITEM, R36_AMT_NAME_OF_SUB1, R36_AMT_NAME_OF_SUB2, R36_AMT_NAME_OF_SUB3, R36_AMT_NAME_OF_SUB4, R36_AMT_NAME_OF_SUB5, R36_TOT_AMT, "
	        + "    R37_ITEM, R37_AMT_NAME_OF_SUB1, R37_AMT_NAME_OF_SUB2, R37_AMT_NAME_OF_SUB3, R37_AMT_NAME_OF_SUB4, R37_AMT_NAME_OF_SUB5, R37_TOT_AMT, "
	        + "    R38_ITEM, R38_AMT_NAME_OF_SUB1, R38_AMT_NAME_OF_SUB2, R38_AMT_NAME_OF_SUB3, R38_AMT_NAME_OF_SUB4, R38_AMT_NAME_OF_SUB5, R38_TOT_AMT, "
	        + "    R39_ITEM, R39_AMT_NAME_OF_SUB1, R39_AMT_NAME_OF_SUB2, R39_AMT_NAME_OF_SUB3, R39_AMT_NAME_OF_SUB4, R39_AMT_NAME_OF_SUB5, R39_TOT_AMT, "
	        + "    R40_ITEM, R40_AMT_NAME_OF_SUB1, R40_AMT_NAME_OF_SUB2, R40_AMT_NAME_OF_SUB3, R40_AMT_NAME_OF_SUB4, R40_AMT_NAME_OF_SUB5, R40_TOT_AMT, "
	        + "    R41_ITEM, R41_AMT_NAME_OF_SUB1, R41_AMT_NAME_OF_SUB2, R41_AMT_NAME_OF_SUB3, R41_AMT_NAME_OF_SUB4, R41_AMT_NAME_OF_SUB5, R41_TOT_AMT, "
	        + "    R42_ITEM, R42_AMT_NAME_OF_SUB1, R42_AMT_NAME_OF_SUB2, R42_AMT_NAME_OF_SUB3, R42_AMT_NAME_OF_SUB4, R42_AMT_NAME_OF_SUB5, R42_TOT_AMT, "
	        + "    R43_ITEM, R43_AMT_NAME_OF_SUB1, R43_AMT_NAME_OF_SUB2, R43_AMT_NAME_OF_SUB3, R43_AMT_NAME_OF_SUB4, R43_AMT_NAME_OF_SUB5, R43_TOT_AMT, "
	        + "    R44_ITEM, R44_AMT_NAME_OF_SUB1, R44_AMT_NAME_OF_SUB2, R44_AMT_NAME_OF_SUB3, R44_AMT_NAME_OF_SUB4, R44_AMT_NAME_OF_SUB5, R44_TOT_AMT, "
	        + "    R45_ITEM, R45_AMT_NAME_OF_SUB1, R45_AMT_NAME_OF_SUB2, R45_AMT_NAME_OF_SUB3, R45_AMT_NAME_OF_SUB4, R45_AMT_NAME_OF_SUB5, R45_TOT_AMT, "
	        + "    R46_ITEM, R46_AMT_NAME_OF_SUB1, R46_AMT_NAME_OF_SUB2, R46_AMT_NAME_OF_SUB3, R46_AMT_NAME_OF_SUB4, R46_AMT_NAME_OF_SUB5, R46_TOT_AMT, "
	        + "    R47_ITEM, R47_AMT_NAME_OF_SUB1, R47_AMT_NAME_OF_SUB2, R47_AMT_NAME_OF_SUB3, R47_AMT_NAME_OF_SUB4, R47_AMT_NAME_OF_SUB5, R47_TOT_AMT, "
	        + "    R48_ITEM, R48_AMT_NAME_OF_SUB1, R48_AMT_NAME_OF_SUB2, R48_AMT_NAME_OF_SUB3, R48_AMT_NAME_OF_SUB4, R48_AMT_NAME_OF_SUB5, R48_TOT_AMT, "
	        + "    R49_ITEM, R49_AMT_NAME_OF_SUB1, R49_AMT_NAME_OF_SUB2, R49_AMT_NAME_OF_SUB3, R49_AMT_NAME_OF_SUB4, R49_AMT_NAME_OF_SUB5, R49_TOT_AMT, "
	        + "    R50_ITEM, R50_AMT_NAME_OF_SUB1, R50_AMT_NAME_OF_SUB2, R50_AMT_NAME_OF_SUB3, R50_AMT_NAME_OF_SUB4, R50_AMT_NAME_OF_SUB5, R50_TOT_AMT, "
	        + "    R51_ITEM, R51_AMT_NAME_OF_SUB1, R51_AMT_NAME_OF_SUB2, R51_AMT_NAME_OF_SUB3, R51_AMT_NAME_OF_SUB4, R51_AMT_NAME_OF_SUB5, R51_TOT_AMT, "
	        + "    R52_ITEM, R52_AMT_NAME_OF_SUB1, R52_AMT_NAME_OF_SUB2, R52_AMT_NAME_OF_SUB3, R52_AMT_NAME_OF_SUB4, R52_AMT_NAME_OF_SUB5, R52_TOT_AMT, "
	        + "    R53_ITEM, R53_AMT_NAME_OF_SUB1, R53_AMT_NAME_OF_SUB2, R53_AMT_NAME_OF_SUB3, R53_AMT_NAME_OF_SUB4, R53_AMT_NAME_OF_SUB5, R53_TOT_AMT, "
	        + "    R54_ITEM, R54_AMT_NAME_OF_SUB1, R54_AMT_NAME_OF_SUB2, R54_AMT_NAME_OF_SUB3, R54_AMT_NAME_OF_SUB4, R54_AMT_NAME_OF_SUB5, R54_TOT_AMT, "
	        + "    R55_ITEM, R55_AMT_NAME_OF_SUB1, R55_AMT_NAME_OF_SUB2, R55_AMT_NAME_OF_SUB3, R55_AMT_NAME_OF_SUB4, R55_AMT_NAME_OF_SUB5, R55_TOT_AMT, "
	        + "    R56_ITEM, R56_AMT_NAME_OF_SUB1, R56_AMT_NAME_OF_SUB2, R56_AMT_NAME_OF_SUB3, R56_AMT_NAME_OF_SUB4, R56_AMT_NAME_OF_SUB5, R56_TOT_AMT, "
	        + "    R57_ITEM, R57_AMT_NAME_OF_SUB1, R57_AMT_NAME_OF_SUB2, R57_AMT_NAME_OF_SUB3, R57_AMT_NAME_OF_SUB4, R57_AMT_NAME_OF_SUB5, R57_TOT_AMT, "
	        + "    R58_ITEM, R58_AMT_NAME_OF_SUB1, R58_AMT_NAME_OF_SUB2, R58_AMT_NAME_OF_SUB3, R58_AMT_NAME_OF_SUB4, R58_AMT_NAME_OF_SUB5, R58_TOT_AMT, "
	        + "    ENTITY_FLG, MODIFY_FLG, DEL_FLG"
	        + ") = ("
	        + "    SELECT "
	        + "    S.R10_ITEM, S.R10_AMT_NAME_OF_SUB1, S.R10_AMT_NAME_OF_SUB2, S.R10_AMT_NAME_OF_SUB3, S.R10_AMT_NAME_OF_SUB4, S.R10_AMT_NAME_OF_SUB5, S.R10_TOT_AMT, "
	        + "    S.R11_ITEM, S.R11_AMT_NAME_OF_SUB1, S.R11_AMT_NAME_OF_SUB2, S.R11_AMT_NAME_OF_SUB3, S.R11_AMT_NAME_OF_SUB4, S.R11_AMT_NAME_OF_SUB5, S.R11_TOT_AMT, "
	        + "    S.R12_ITEM, S.R12_AMT_NAME_OF_SUB1, S.R12_AMT_NAME_OF_SUB2, S.R12_AMT_NAME_OF_SUB3, S.R12_AMT_NAME_OF_SUB4, S.R12_AMT_NAME_OF_SUB5, S.R12_TOT_AMT, "
	        + "    S.R13_ITEM, S.R13_AMT_NAME_OF_SUB1, S.R13_AMT_NAME_OF_SUB2, S.R13_AMT_NAME_OF_SUB3, S.R13_AMT_NAME_OF_SUB4, S.R13_AMT_NAME_OF_SUB5, S.R13_TOT_AMT, "
	        + "    S.R14_ITEM, S.R14_AMT_NAME_OF_SUB1, S.R14_AMT_NAME_OF_SUB2, S.R14_AMT_NAME_OF_SUB3, S.R14_AMT_NAME_OF_SUB4, S.R14_AMT_NAME_OF_SUB5, S.R14_TOT_AMT, "
	        + "    S.R17_ITEM, S.R17_AMT_NAME_OF_SUB1, S.R17_AMT_NAME_OF_SUB2, S.R17_AMT_NAME_OF_SUB3, S.R17_AMT_NAME_OF_SUB4, S.R17_AMT_NAME_OF_SUB5, S.R17_TOT_AMT, "
	        + "    S.R18_ITEM, S.R18_AMT_NAME_OF_SUB1, S.R18_AMT_NAME_OF_SUB2, S.R18_AMT_NAME_OF_SUB3, S.R18_AMT_NAME_OF_SUB4, S.R18_AMT_NAME_OF_SUB5, S.R18_TOT_AMT, "
	        + "    S.R19_ITEM, S.R19_AMT_NAME_OF_SUB1, S.R19_AMT_NAME_OF_SUB2, S.R19_AMT_NAME_OF_SUB3, S.R19_AMT_NAME_OF_SUB4, S.R19_AMT_NAME_OF_SUB5, S.R19_TOT_AMT, "
	        + "    S.R22_ITEM, S.R22_AMT_NAME_OF_SUB1, S.R22_AMT_NAME_OF_SUB2, S.R22_AMT_NAME_OF_SUB3, S.R22_AMT_NAME_OF_SUB4, S.R22_AMT_NAME_OF_SUB5, S.R22_TOT_AMT, "
	        + "    S.R23_ITEM, S.R23_AMT_NAME_OF_SUB1, S.R23_AMT_NAME_OF_SUB2, S.R23_AMT_NAME_OF_SUB3, S.R23_AMT_NAME_OF_SUB4, S.R23_AMT_NAME_OF_SUB5, S.R23_TOT_AMT, "
	        + "    S.R24_ITEM, S.R24_AMT_NAME_OF_SUB1, S.R24_AMT_NAME_OF_SUB2, S.R24_AMT_NAME_OF_SUB3, S.R24_AMT_NAME_OF_SUB4, S.R24_AMT_NAME_OF_SUB5, S.R24_TOT_AMT, "
	        + "    S.R27_ITEM, S.R27_AMT_NAME_OF_SUB1, S.R27_AMT_NAME_OF_SUB2, S.R27_AMT_NAME_OF_SUB3, S.R27_AMT_NAME_OF_SUB4, S.R27_AMT_NAME_OF_SUB5, S.R27_TOT_AMT, "
	        + "    S.R28_ITEM, S.R28_AMT_NAME_OF_SUB1, S.R28_AMT_NAME_OF_SUB2, S.R28_AMT_NAME_OF_SUB3, S.R28_AMT_NAME_OF_SUB4, S.R28_AMT_NAME_OF_SUB5, S.R28_TOT_AMT, "
	        + "    S.R29_ITEM, S.R29_AMT_NAME_OF_SUB1, S.R29_AMT_NAME_OF_SUB2, S.R29_AMT_NAME_OF_SUB3, S.R29_AMT_NAME_OF_SUB4, S.R29_AMT_NAME_OF_SUB5, S.R29_TOT_AMT, "
	        + "    S.R32_ITEM, S.R32_AMT_NAME_OF_SUB1, S.R32_AMT_NAME_OF_SUB2, S.R32_AMT_NAME_OF_SUB3, S.R32_AMT_NAME_OF_SUB4, S.R32_AMT_NAME_OF_SUB5, S.R32_TOT_AMT, "
	        + "    S.R33_ITEM, S.R33_AMT_NAME_OF_SUB1, S.R33_AMT_NAME_OF_SUB2, S.R33_AMT_NAME_OF_SUB3, S.R33_AMT_NAME_OF_SUB4, S.R33_AMT_NAME_OF_SUB5, S.R33_TOT_AMT, "
	        + "    S.R34_ITEM, S.R34_AMT_NAME_OF_SUB1, S.R34_AMT_NAME_OF_SUB2, S.R34_AMT_NAME_OF_SUB3, S.R34_AMT_NAME_OF_SUB4, S.R34_AMT_NAME_OF_SUB5, S.R34_TOT_AMT, "
	        + "    S.R36_ITEM, S.R36_AMT_NAME_OF_SUB1, S.R36_AMT_NAME_OF_SUB2, S.R36_AMT_NAME_OF_SUB3, S.R36_AMT_NAME_OF_SUB4, S.R36_AMT_NAME_OF_SUB5, S.R36_TOT_AMT, "
	        + "    S.R37_ITEM, S.R37_AMT_NAME_OF_SUB1, S.R37_AMT_NAME_OF_SUB2, S.R37_AMT_NAME_OF_SUB3, S.R37_AMT_NAME_OF_SUB4, S.R37_AMT_NAME_OF_SUB5, S.R37_TOT_AMT, "
	        + "    S.R38_ITEM, S.R38_AMT_NAME_OF_SUB1, S.R38_AMT_NAME_OF_SUB2, S.R38_AMT_NAME_OF_SUB3, S.R38_AMT_NAME_OF_SUB4, S.R38_AMT_NAME_OF_SUB5, S.R38_TOT_AMT, "
	        + "    S.R39_ITEM, S.R39_AMT_NAME_OF_SUB1, S.R39_AMT_NAME_OF_SUB2, S.R39_AMT_NAME_OF_SUB3, S.R39_AMT_NAME_OF_SUB4, S.R39_AMT_NAME_OF_SUB5, S.R39_TOT_AMT, "
	        + "    S.R40_ITEM, S.R40_AMT_NAME_OF_SUB1, S.R40_AMT_NAME_OF_SUB2, S.R40_AMT_NAME_OF_SUB3, S.R40_AMT_NAME_OF_SUB4, S.R40_AMT_NAME_OF_SUB5, S.R40_TOT_AMT, "
	        + "    S.R41_ITEM, S.R41_AMT_NAME_OF_SUB1, S.R41_AMT_NAME_OF_SUB2, S.R41_AMT_NAME_OF_SUB3, S.R41_AMT_NAME_OF_SUB4, S.R41_AMT_NAME_OF_SUB5, S.R41_TOT_AMT, "
	        + "    S.R42_ITEM, S.R42_AMT_NAME_OF_SUB1, S.R42_AMT_NAME_OF_SUB2, S.R42_AMT_NAME_OF_SUB3, S.R42_AMT_NAME_OF_SUB4, S.R42_AMT_NAME_OF_SUB5, S.R42_TOT_AMT, "
	        + "    S.R43_ITEM, S.R43_AMT_NAME_OF_SUB1, S.R43_AMT_NAME_OF_SUB2, S.R43_AMT_NAME_OF_SUB3, S.R43_AMT_NAME_OF_SUB4, S.R43_AMT_NAME_OF_SUB5, S.R43_TOT_AMT, "
	        + "    S.R44_ITEM, S.R44_AMT_NAME_OF_SUB1, S.R44_AMT_NAME_OF_SUB2, S.R44_AMT_NAME_OF_SUB3, S.R44_AMT_NAME_OF_SUB4, S.R44_AMT_NAME_OF_SUB5, S.R44_TOT_AMT, "
	        + "    S.R45_ITEM, S.R45_AMT_NAME_OF_SUB1, S.R45_AMT_NAME_OF_SUB2, S.R45_AMT_NAME_OF_SUB3, S.R45_AMT_NAME_OF_SUB4, S.R45_AMT_NAME_OF_SUB5, S.R45_TOT_AMT, "
	        + "    S.R46_ITEM, S.R46_AMT_NAME_OF_SUB1, S.R46_AMT_NAME_OF_SUB2, S.R46_AMT_NAME_OF_SUB3, S.R46_AMT_NAME_OF_SUB4, S.R46_AMT_NAME_OF_SUB5, S.R46_TOT_AMT, "
	        + "    S.R47_ITEM, S.R47_AMT_NAME_OF_SUB1, S.R47_AMT_NAME_OF_SUB2, S.R47_AMT_NAME_OF_SUB3, S.R47_AMT_NAME_OF_SUB4, S.R47_AMT_NAME_OF_SUB5, S.R47_TOT_AMT, "
	        + "    S.R48_ITEM, S.R48_AMT_NAME_OF_SUB1, S.R48_AMT_NAME_OF_SUB2, S.R48_AMT_NAME_OF_SUB3, S.R48_AMT_NAME_OF_SUB4, S.R48_AMT_NAME_OF_SUB5, S.R48_TOT_AMT, "
	        + "    S.R49_ITEM, S.R49_AMT_NAME_OF_SUB1, S.R49_AMT_NAME_OF_SUB2, S.R49_AMT_NAME_OF_SUB3, S.R49_AMT_NAME_OF_SUB4, S.R49_AMT_NAME_OF_SUB5, S.R49_TOT_AMT, "
	        + "    S.R50_ITEM, S.R50_AMT_NAME_OF_SUB1, S.R50_AMT_NAME_OF_SUB2, S.R50_AMT_NAME_OF_SUB3, S.R50_AMT_NAME_OF_SUB4, S.R50_AMT_NAME_OF_SUB5, S.R50_TOT_AMT, "
	        + "    S.R51_ITEM, S.R51_AMT_NAME_OF_SUB1, S.R51_AMT_NAME_OF_SUB2, S.R51_AMT_NAME_OF_SUB3, S.R51_AMT_NAME_OF_SUB4, S.R51_AMT_NAME_OF_SUB5, S.R51_TOT_AMT, "
	        + "    S.R52_ITEM, S.R52_AMT_NAME_OF_SUB1, S.R52_AMT_NAME_OF_SUB2, S.R52_AMT_NAME_OF_SUB3, S.R52_AMT_NAME_OF_SUB4, S.R52_AMT_NAME_OF_SUB5, S.R52_TOT_AMT, "
	        + "    S.R53_ITEM, S.R53_AMT_NAME_OF_SUB1, S.R53_AMT_NAME_OF_SUB2, S.R53_AMT_NAME_OF_SUB3, S.R53_AMT_NAME_OF_SUB4, S.R53_AMT_NAME_OF_SUB5, S.R53_TOT_AMT, "
	        + "    S.R54_ITEM, S.R54_AMT_NAME_OF_SUB1, S.R54_AMT_NAME_OF_SUB2, S.R54_AMT_NAME_OF_SUB3, S.R54_AMT_NAME_OF_SUB4, S.R54_AMT_NAME_OF_SUB5, S.R54_TOT_AMT, "
	        + "    S.R55_ITEM, S.R55_AMT_NAME_OF_SUB1, S.R55_AMT_NAME_OF_SUB2, S.R55_AMT_NAME_OF_SUB3, S.R55_AMT_NAME_OF_SUB4, S.R55_AMT_NAME_OF_SUB5, S.R55_TOT_AMT, "
	        + "    S.R56_ITEM, S.R56_AMT_NAME_OF_SUB1, S.R56_AMT_NAME_OF_SUB2, S.R56_AMT_NAME_OF_SUB3, S.R56_AMT_NAME_OF_SUB4, S.R56_AMT_NAME_OF_SUB5, S.R56_TOT_AMT, "
	        + "    S.R57_ITEM, S.R57_AMT_NAME_OF_SUB1, S.R57_AMT_NAME_OF_SUB2, S.R57_AMT_NAME_OF_SUB3, S.R57_AMT_NAME_OF_SUB4, S.R57_AMT_NAME_OF_SUB5, S.R57_TOT_AMT, "
	        + "    S.R58_ITEM, S.R58_AMT_NAME_OF_SUB1, S.R58_AMT_NAME_OF_SUB2, S.R58_AMT_NAME_OF_SUB3, S.R58_AMT_NAME_OF_SUB4, S.R58_AMT_NAME_OF_SUB5, S.R58_TOT_AMT, "
	        + "    S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG "
	        + "    FROM BRRS_M_CA4_MOD_SUMMARYTABLE S "
	        + "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
	        + ") "
	        + "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

	    int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

	    if (detailUpdated == 0) {
	        throw new IllegalStateException("M_CA4 detail record not found for date : " + reportDate);
	    }
	    System.out.println("M_CA4 Detail Table Updated : " + detailUpdated);

	    // 3. Mark MOD row approved
	    String updateModSql =
	        "UPDATE BRRS_M_CA4_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' " +
	        "WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
	    int modUpdated = jdbcTemplate.update(updateModSql, reportDate);
	    System.out.println("M_CA4 MOD Updated : " + modUpdated);

	    System.out.println("M_CA4 Verification and Transfer Completed Successfully");
	}
	
	// =========================================================
	// M_SECL — Checker transfer (MOD → LIVE Summary + Detail)
	// =========================================================
	private void transferSECLData(AuditServicesEntity auditRecord) {
	    String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SECL_MOD_SUMMARYTABLE";
	    java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

	    if (reportDate == null) {
	        throw new IllegalStateException("No modified M_SECL record available for verification.");
	    }

	    System.out.println("M_SECL Report Date : " + reportDate);

	    // Build the (cols) = (SELECT cols FROM MOD) parts once — same for Summary + Detail
	    String columnList = buildSecLColumnList();     // "R12_PRODUCT, R12_EQUITY, …, R57_TOTAL, ENTITY_FLG, MODIFY_FLG, DEL_FLG"
	    String selectList = buildSecLSelectList();     // "S.R12_PRODUCT, S.R12_EQUITY, …, S.R57_TOTAL, S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG"

	    // 1. Update BRRS_M_SECL_SUMMARYTABLE from MOD
	    String updateSummarySql =
	            "UPDATE BRRS_M_SECL_SUMMARYTABLE M " +
	            "SET (" + columnList + ") = (" +
	            "    SELECT " + selectList +
	            "    FROM BRRS_M_SECL_MOD_SUMMARYTABLE S " +
	            "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" +
	            ") " +
	            "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

	    int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

	    if (summaryUpdated == 0) {
	        throw new IllegalStateException("M_SECL modification record not found for date : " + reportDate);
	    }
	    System.out.println("M_SECL Summary Table Updated : " + summaryUpdated);

	    // 2. Update BRRS_M_SECL_DETAILTABLE from MOD (identical column/select lists)
	    String updateDetailSql =
	            "UPDATE BRRS_M_SECL_DETAILTABLE M " +
	            "SET (" + columnList + ") = (" +
	            "    SELECT " + selectList +
	            "    FROM BRRS_M_SECL_MOD_SUMMARYTABLE S " +
	            "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" +
	            ") " +
	            "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

	    int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

	    if (detailUpdated == 0) {
	        throw new IllegalStateException("M_SECL detail record not found for date : " + reportDate);
	    }
	    System.out.println("M_SECL Detail Table Updated : " + detailUpdated);

	    // 3. Mark MOD row as approved
	    String updateModSql =
	            "UPDATE BRRS_M_SECL_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' " +
	            "WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
	    int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

	    System.out.println("M_SECL MOD Updated : " + modUpdated);
	    System.out.println("M_SECL Verification and Transfer Completed Successfully");
	}
	
	
	private static String buildSecLColumnList() {
	    StringBuilder sb = new StringBuilder();

	    for (int row = 12; row <= 57; row++) {
	        String p = "R" + row + "_";
	        sb.append(p).append("PRODUCT, ")
	          .append(p).append("EQUITY, ")
	          .append(p).append("BONDS, ")
	          .append(p).append("BOBCS, ")
	          .append(p).append("TREASURY_BILLS, ")
	          .append(p).append("REPURCHASE_AGREEMENTS, ")
	          .append(p).append("COMMERCIAL_PAPER, ")
	          .append(p).append("CERTIFICATES_OF_DEPOSITS, ")
	          .append(p).append("PLEDGED_ASSETS, ")
	          .append(p).append("OTHER_SPECIFY, ")
	          .append(p).append("TOTAL, ");
	    }
	    sb.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
	    return sb.toString();
	}
	
	
	
	private static String buildSecLSelectList() {
	    StringBuilder sb = new StringBuilder();

	    for (int row = 12; row <= 57; row++) {
	        String p = "R" + row + "_";
	        sb.append("S.").append(p).append("PRODUCT, ")
	          .append("S.").append(p).append("EQUITY, ")
	          .append("S.").append(p).append("BONDS, ")
	          .append("S.").append(p).append("BOBCS, ")
	          .append("S.").append(p).append("TREASURY_BILLS, ")
	          .append("S.").append(p).append("REPURCHASE_AGREEMENTS, ")
	          .append("S.").append(p).append("COMMERCIAL_PAPER, ")
	          .append("S.").append(p).append("CERTIFICATES_OF_DEPOSITS, ")
	          .append("S.").append(p).append("PLEDGED_ASSETS, ")
	          .append("S.").append(p).append("OTHER_SPECIFY, ")
	          .append("S.").append(p).append("TOTAL, ");
	    }
	    sb.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
	    return sb.toString();
	}
	
	
	
}
