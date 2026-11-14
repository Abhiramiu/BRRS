package com.bornfire.brrs.services;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Locale;

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
import com.bornfire.brrs.entities.UserProfileRep;
import com.bornfire.brrs.services.BRRS_M_GALOR_ReportService.M_GALOR_Manual_Summary_Entity;
import com.bornfire.brrs.services.BRRS_M_IS_ReportService.M_IS_Summary_Entity1;
import com.bornfire.brrs.services.BRRS_M_IS_ReportService.M_IS_Summary_Entity2;
import com.bornfire.brrs.services.BRRS_M_I_S_CA_ReportService.M_I_S_CA_Summary_Entity;
import com.bornfire.brrs.services.BRRS_M_PD_ReportService.M_PD_Manual_Summary_Entity;

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
	@Autowired
	private UserProfileRep userProfileRep;

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

		StringBuilder changes = new StringBuilder();

		Class<?> clazz = oldEntity.getClass();
		List<Field> fields = getAllFields(clazz);

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

		StringBuilder changes = new StringBuilder();
		Class<?> clazz = oldEntity.getClass();
		List<Field> fields = getAllFields(clazz);

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

	private List<Field> getAllFields(Class<?> clazz) {
		List<Field> result = new ArrayList<>();
		for (Class<?> c = clazz; c != null && c != Object.class; c = c.getSuperclass()) {
			for (Field f : c.getDeclaredFields()) {
				if (java.lang.reflect.Modifier.isStatic(f.getModifiers()) || f.isSynthetic()) {
					continue;
				}
				result.add(f);
			}
		}
		return result;
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
		List<Field> fields = getAllFields(clazz);

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

		if (upperTable.contains("SFINP2")) {
			transferSfinp2Data(auditRecord);
		} else if (upperTable.contains("M_SFINP1")) {
			transferSFINP1Data(auditRecord);
		} else if (upperTable.contains("FAS")) {
			transferFASData(auditRecord);
		} else if (upperTable.contains("DEP3")) {
			transferDEP3Data(auditRecord);
		} else if (upperTable.contains("Q_STAFF")) {
			transferQStaffData(auditRecord);
		} else if (upperTable.contains("BDISB1")) {
			transferBDISB1Data(auditRecord);
		} else if (upperTable.contains("AIDP") || upperTable.contains("M_AIDP")) {
			transferAIDPData(auditRecord);
		} else if (upperTable.contains("Q_BRANCHNET")) {
			transferQBranchnetData(auditRecord);
		} else if (upperTable.contains("Q_SMME_DEP")) {
			transferQSmmeDepData(auditRecord);
		} else if (upperTable.contains("M_CA4")) {
			transferCA4Data(auditRecord);
		} else if (upperTable.contains("M_CA6")) {
			transferMCA6Data(auditRecord);
		} else if (upperTable.contains("M_CA3")) {
			transferCA3Data(auditRecord);
		} else if (upperTable.contains("M_SECL")) {
			transferSECLData(auditRecord);
		} else if (upperTable.contains("M_BOP")) {
			transferMBopData(auditRecord);
		} else if (upperTable.contains("M_OB")) {
			transferMObData(auditRecord);
		} else if (upperTable.contains("M_SECA")) {
			transferSECAData(auditRecord);
		} else if (upperTable.contains("M_OR1")) {
			transferMOR1Data(auditRecord);
		} else if (upperTable.contains("M_OR2")) {
			transferMOR2Data(auditRecord);
		} else if (upperTable.contains("M_LA2")) {
			transferMLA2Data(auditRecord);
		} else if (upperTable.contains("M_SRWA_12F")) {
			transferMSRWA12FData(auditRecord);
		} else if (upperTable.contains("M_SRWA_12G")) {
			transferMSRWA12GData(auditRecord);
		} else if (upperTable.contains("M_SRWA_12H")) {
			transferMSRWA12HData(auditRecord);
		} else if (upperTable.contains("INT_RATES_FCA")) {
			transferIntRatesFcaData(auditRecord);
		} else if (upperTable.contains("Q_RLFA1")) {
			transferQRlfa1Data(auditRecord);
		} else if (upperTable.contains("Q_RLFA2")) {
			transferQRlfa2Data(auditRecord);
		} else if (upperTable.contains("M_GP")) {
			transferMGpData(auditRecord);
		} else if (upperTable.contains("M_TBS")) {
			transferMTBSData(auditRecord);
		} else if (upperTable.contains("M_INT_RATES")) {
			transferMINTRATESData(auditRecord);
		} else if (upperTable.contains("M_FXR")) {
			transferMFXRData(auditRecord);
		} else if (upperTable.contains("M_EPR")) {
			transferM_EPRData(auditRecord);
		} else if (upperTable.contains("M_CA5_1") || upperTable.contains("M_CA5")) {
			transferM_CA5_Data1(auditRecord);
		} else if (upperTable.contains("M_CA5_2")) {
			transferM_CA5_Data2(auditRecord);
		} else if (upperTable.contains("M_CA7")) {
			transferM_CA7_Data(auditRecord);
		} else if (upperTable.contains("M_CR")) {
			transferM_CR_Data(auditRecord);
		} else if (upperTable.contains("M_MRC")) {
			transferM_MRCData(auditRecord);
		} else if (upperTable.contains("M_OPTR")) {
			transferM_OPTR_Data(auditRecord);
		} else if (upperTable.contains("M_UNCONS_INVEST")) {
			transferM_UNCONS_INVEST_Data(auditRecord);
		} else if (upperTable.contains("M_LA4")) {
			transferLA4Data(auditRecord);
		} else if (upperTable.contains("M_LA3")) {
			transferLA3Data(auditRecord);
		} else if (upperTable.contains("M_IS")) {
			transferISData(auditRecord);
		} else if (upperTable.contains("M_PD")) {
			transferPDData(auditRecord);
		} else if (upperTable.contains("M_CA2")) {
			transferCA2Data(auditRecord);
		} else if (upperTable.contains("M_I_S_CA")) {
			transferISCAData(auditRecord);
		} else if (upperTable.contains("M_SCI_E")) {
			transferSCIEData(auditRecord);
		} else if (upperTable.contains("M_LIQGAP")) {
			transferLIQGAPData(auditRecord);
		} else if (upperTable.contains("M_LIQ")) {
			transferLIQData(auditRecord);
		} else if (upperTable.contains("M_GALOR")) {
			transferGALORData(auditRecord);
		} else if (upperTable.contains("M_SIR")) {
			transferMSirData(auditRecord);
		} else if (upperTable.contains("M_SRWA_12C") || upperTable.contains("SRWA_12C")) {
			transferM_SRWA_12CData(auditRecord);
		} else if (upperTable.contains("M_SRWA_12D") || upperTable.contains("SRWA_12D")) {
			transferM_SRWA_12DData(auditRecord);
		} else if (upperTable.contains("M_GMIRT")) {
			transferGMIRTData(auditRecord);
		} else if (upperTable.contains("M_IRB")) {
			transferMIrbData(auditRecord);
		} else if (upperTable.contains("M_SRWA_12E") || upperTable.contains("SRWA_12E")) {
			transferMSRWA12EData(auditRecord);
		} else if (upperTable.contains("M_SRWA_12B") || upperTable.contains("SRWA_12B")) {
			transferMSRWA12BData(auditRecord);
		} else if (upperTable.contains("M_SEC_MOD_SUMMARYTABLE") || upperTable.startsWith("BRRS_M_SEC")) {
			transferMSecData(auditRecord);
		} else if (upperTable.contains("UFCE_CALCULATION_MOD_SUMMARYTABLE_MANUAL") || upperTable.startsWith("BRRS_UFCE_CALCULATION")) {
			transferUFCEManualData(auditRecord);
		} else {
			throw new IllegalArgumentException("Verification not configured for table: " + auditTable);
		}
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

	private void transferM_SRWA_12CData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12C_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_SRWA_12C record available for verification.");
		}

		System.out.println("M_SRWA_12C Report Date : " + reportDate);

		// 1. Update BRRS_M_SRWA_12C_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_SRWA_12C_SUMMARYTABLE M SET ("
				+ "    R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, "
				+ "    R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R12_NUMBER_OF_FAILED_TRADES, R12_POSITIVE_CURRENT_EXPOSURE, R12_RISK_MULTIPLIER, R12_RWA, "
				+ "    R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R13_NUMBER_OF_FAILED_TRADES, R13_POSITIVE_CURRENT_EXPOSURE, R13_RISK_MULTIPLIER, R13_RWA, "
				+ "    R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R14_NUMBER_OF_FAILED_TRADES, R14_POSITIVE_CURRENT_EXPOSURE, R14_RISK_MULTIPLIER, R14_RWA, "
				+ "    R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R15_NUMBER_OF_FAILED_TRADES, R15_POSITIVE_CURRENT_EXPOSURE, R15_RISK_MULTIPLIER, R15_RWA, "
				+ "    R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R16_NUMBER_OF_FAILED_TRADES, R16_POSITIVE_CURRENT_EXPOSURE, R16_RWA, "
				+ "    R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, "
				+ "    R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R20_NUMBER_OF_FAILED_TRADES, R20_POSITIVE_CURRENT_EXPOSURE, R20_RISK_MULTIPLIER, R20_RWA, "
				+ "    R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R21_NUMBER_OF_FAILED_TRADES, R21_POSITIVE_CURRENT_EXPOSURE, R21_RISK_MULTIPLIER, R21_RWA, "
				+ "    R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R22_NUMBER_OF_FAILED_TRADES, R22_POSITIVE_CURRENT_EXPOSURE, R22_RISK_MULTIPLIER, R22_RWA, "
				+ "    R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R23_NUMBER_OF_FAILED_TRADES, R23_POSITIVE_CURRENT_EXPOSURE, R23_RISK_MULTIPLIER, R23_RWA, "
				+ "    R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R24_NUMBER_OF_FAILED_TRADES, R24_POSITIVE_CURRENT_EXPOSURE, R24_RISK_MULTIPLIER, R24_RWA, "
				+ "    R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R25_NUMBER_OF_FAILED_TRADES, R25_POSITIVE_CURRENT_EXPOSURE, R25_RISK_MULTIPLIER, R25_RWA, "
				+ "    R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R26_NUMBER_OF_FAILED_TRADES, R26_POSITIVE_CURRENT_EXPOSURE, R26_RISK_MULTIPLIER, R26_RWA, "
				+ "    R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R27_NUMBER_OF_FAILED_TRADES, R27_POSITIVE_CURRENT_EXPOSURE, R27_RWA, "
				+ "    R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R28_NUMBER_OF_FAILED_TRADES, R28_POSITIVE_CURRENT_EXPOSURE, R28_RWA, "
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				+ "        S.R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, "
				+ "        S.R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R12_NUMBER_OF_FAILED_TRADES, S.R12_POSITIVE_CURRENT_EXPOSURE, S.R12_RISK_MULTIPLIER, S.R12_RWA, "
				+ "        S.R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R13_NUMBER_OF_FAILED_TRADES, S.R13_POSITIVE_CURRENT_EXPOSURE, S.R13_RISK_MULTIPLIER, S.R13_RWA, "
				+ "        S.R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R14_NUMBER_OF_FAILED_TRADES, S.R14_POSITIVE_CURRENT_EXPOSURE, S.R14_RISK_MULTIPLIER, S.R14_RWA, "
				+ "        S.R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R15_NUMBER_OF_FAILED_TRADES, S.R15_POSITIVE_CURRENT_EXPOSURE, S.R15_RISK_MULTIPLIER, S.R15_RWA, "
				+ "        S.R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R16_NUMBER_OF_FAILED_TRADES, S.R16_POSITIVE_CURRENT_EXPOSURE, S.R16_RWA, "
				+ "        S.R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, "
				+ "        S.R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R20_NUMBER_OF_FAILED_TRADES, S.R20_POSITIVE_CURRENT_EXPOSURE, S.R20_RISK_MULTIPLIER, S.R20_RWA, "
				+ "        S.R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R21_NUMBER_OF_FAILED_TRADES, S.R21_POSITIVE_CURRENT_EXPOSURE, S.R21_RISK_MULTIPLIER, S.R21_RWA, "
				+ "        S.R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R22_NUMBER_OF_FAILED_TRADES, S.R22_POSITIVE_CURRENT_EXPOSURE, S.R22_RISK_MULTIPLIER, S.R22_RWA, "
				+ "        S.R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R23_NUMBER_OF_FAILED_TRADES, S.R23_POSITIVE_CURRENT_EXPOSURE, S.R23_RISK_MULTIPLIER, S.R23_RWA, "
				+ "        S.R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R24_NUMBER_OF_FAILED_TRADES, S.R24_POSITIVE_CURRENT_EXPOSURE, S.R24_RISK_MULTIPLIER, S.R24_RWA, "
				+ "        S.R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R25_NUMBER_OF_FAILED_TRADES, S.R25_POSITIVE_CURRENT_EXPOSURE, S.R25_RISK_MULTIPLIER, S.R25_RWA, "
				+ "        S.R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R26_NUMBER_OF_FAILED_TRADES, S.R26_POSITIVE_CURRENT_EXPOSURE, S.R26_RISK_MULTIPLIER, S.R26_RWA, "
				+ "        S.R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R27_NUMBER_OF_FAILED_TRADES, S.R27_POSITIVE_CURRENT_EXPOSURE, S.R27_RWA, "
				+ "        S.R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R28_NUMBER_OF_FAILED_TRADES, S.R28_POSITIVE_CURRENT_EXPOSURE, S.R28_RWA, "
				+ "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_SRWA_12C_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_SRWA_12C modification record not found for date : " + reportDate);
		}

		System.out.println("M_SRWA_12C Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_SRWA_12C_DETAILTABLE from MOD table (Same column mapping)
		String updateDetailSql = "UPDATE BRRS_M_SRWA_12C_DETAILTABLE M SET ("
				+ "    R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, "
				+ "    R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R12_NUMBER_OF_FAILED_TRADES, R12_POSITIVE_CURRENT_EXPOSURE, R12_RISK_MULTIPLIER, R12_RWA, "
				+ "    R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R13_NUMBER_OF_FAILED_TRADES, R13_POSITIVE_CURRENT_EXPOSURE, R13_RISK_MULTIPLIER, R13_RWA, "
				+ "    R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R14_NUMBER_OF_FAILED_TRADES, R14_POSITIVE_CURRENT_EXPOSURE, R14_RISK_MULTIPLIER, R14_RWA, "
				+ "    R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R15_NUMBER_OF_FAILED_TRADES, R15_POSITIVE_CURRENT_EXPOSURE, R15_RISK_MULTIPLIER, R15_RWA, "
				+ "    R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R16_NUMBER_OF_FAILED_TRADES, R16_POSITIVE_CURRENT_EXPOSURE, R16_RWA, "
				+ "    R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, "
				+ "    R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R20_NUMBER_OF_FAILED_TRADES, R20_POSITIVE_CURRENT_EXPOSURE, R20_RISK_MULTIPLIER, R20_RWA, "
				+ "    R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R21_NUMBER_OF_FAILED_TRADES, R21_POSITIVE_CURRENT_EXPOSURE, R21_RISK_MULTIPLIER, R21_RWA, "
				+ "    R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R22_NUMBER_OF_FAILED_TRADES, R22_POSITIVE_CURRENT_EXPOSURE, R22_RISK_MULTIPLIER, R22_RWA, "
				+ "    R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R23_NUMBER_OF_FAILED_TRADES, R23_POSITIVE_CURRENT_EXPOSURE, R23_RISK_MULTIPLIER, R23_RWA, "
				+ "    R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R24_NUMBER_OF_FAILED_TRADES, R24_POSITIVE_CURRENT_EXPOSURE, R24_RISK_MULTIPLIER, R24_RWA, "
				+ "    R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R25_NUMBER_OF_FAILED_TRADES, R25_POSITIVE_CURRENT_EXPOSURE, R25_RISK_MULTIPLIER, R25_RWA, "
				+ "    R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R26_NUMBER_OF_FAILED_TRADES, R26_POSITIVE_CURRENT_EXPOSURE, R26_RISK_MULTIPLIER, R26_RWA, "
				+ "    R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R27_NUMBER_OF_FAILED_TRADES, R27_POSITIVE_CURRENT_EXPOSURE, R27_RWA, "
				+ "    R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, R28_NUMBER_OF_FAILED_TRADES, R28_POSITIVE_CURRENT_EXPOSURE, R28_RWA, "
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				+ "        S.R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, "
				+ "        S.R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R12_NUMBER_OF_FAILED_TRADES, S.R12_POSITIVE_CURRENT_EXPOSURE, S.R12_RISK_MULTIPLIER, S.R12_RWA, "
				+ "        S.R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R13_NUMBER_OF_FAILED_TRADES, S.R13_POSITIVE_CURRENT_EXPOSURE, S.R13_RISK_MULTIPLIER, S.R13_RWA, "
				+ "        S.R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R14_NUMBER_OF_FAILED_TRADES, S.R14_POSITIVE_CURRENT_EXPOSURE, S.R14_RISK_MULTIPLIER, S.R14_RWA, "
				+ "        S.R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R15_NUMBER_OF_FAILED_TRADES, S.R15_POSITIVE_CURRENT_EXPOSURE, S.R15_RISK_MULTIPLIER, S.R15_RWA, "
				+ "        S.R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R16_NUMBER_OF_FAILED_TRADES, S.R16_POSITIVE_CURRENT_EXPOSURE, S.R16_RWA, "
				+ "        S.R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, "
				+ "        S.R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R20_NUMBER_OF_FAILED_TRADES, S.R20_POSITIVE_CURRENT_EXPOSURE, S.R20_RISK_MULTIPLIER, S.R20_RWA, "
				+ "        S.R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R21_NUMBER_OF_FAILED_TRADES, S.R21_POSITIVE_CURRENT_EXPOSURE, S.R21_RISK_MULTIPLIER, S.R21_RWA, "
				+ "        S.R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R22_NUMBER_OF_FAILED_TRADES, S.R22_POSITIVE_CURRENT_EXPOSURE, S.R22_RISK_MULTIPLIER, S.R22_RWA, "
				+ "        S.R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R23_NUMBER_OF_FAILED_TRADES, S.R23_POSITIVE_CURRENT_EXPOSURE, S.R23_RISK_MULTIPLIER, S.R23_RWA, "
				+ "        S.R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R24_NUMBER_OF_FAILED_TRADES, S.R24_POSITIVE_CURRENT_EXPOSURE, S.R24_RISK_MULTIPLIER, S.R24_RWA, "
				+ "        S.R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R25_NUMBER_OF_FAILED_TRADES, S.R25_POSITIVE_CURRENT_EXPOSURE, S.R25_RISK_MULTIPLIER, S.R25_RWA, "
				+ "        S.R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R26_NUMBER_OF_FAILED_TRADES, S.R26_POSITIVE_CURRENT_EXPOSURE, S.R26_RISK_MULTIPLIER, S.R26_RWA, "
				+ "        S.R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R27_NUMBER_OF_FAILED_TRADES, S.R27_POSITIVE_CURRENT_EXPOSURE, S.R27_RWA, "
				+ "        S.R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE, S.R28_NUMBER_OF_FAILED_TRADES, S.R28_POSITIVE_CURRENT_EXPOSURE, S.R28_RWA, "
				+ "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_SRWA_12C_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_SRWA_12C Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_SRWA_12C_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_SRWA_12C MOD Updated : " + modUpdated);
		System.out.println("M_SRWA_12C Verification and Transfer Completed Successfully");
	}

	// =========================================================
	// M_SRWA_12D — Checker transfer (MOD → LIVE Summary + Detail)
	// =========================================================
	private void transferM_SRWA_12DData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12D_MOD_SUMMARYTABLE WHERE MODIFY_FLG = 'Y'";
		java.sql.Date reportDate = null;
		try {
			reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
		} catch (Exception e) {
			// fallback below
		}
		if (reportDate == null) {
			reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12D_MOD_SUMMARYTABLE",
					java.sql.Date.class);
		}

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_SRWA_12D record available for verification.");
		}

		System.out.println("M_SRWA_12D Report Date : " + reportDate);

		String columns = getSRWA12DColumns();

		// 1. Update BRRS_M_SRWA_12D_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_SRWA_12D_SUMMARYTABLE M SET (" + columns + ") = (" + "    SELECT "
				+ getSRWA12DPrefixedColumns("S.") + "    FROM BRRS_M_SRWA_12D_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_SRWA_12D modification record not found for date : " + reportDate);
		}

		System.out.println("M_SRWA_12D Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_SRWA_12D_DETAILTABLE from MOD table (Same column mapping)
		String updateDetailSql = "UPDATE BRRS_M_SRWA_12D_DETAILTABLE M SET (" + columns + ") = (" + "    SELECT "
				+ getSRWA12DPrefixedColumns("S.") + "    FROM BRRS_M_SRWA_12D_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_SRWA_12D Detail Table Updated : " + detailUpdated);

		// 3. Mark live tables as approved
		jdbcTemplate.update(
				"UPDATE BRRS_M_SRWA_12D_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);
		jdbcTemplate.update(
				"UPDATE BRRS_M_SRWA_12D_DETAILTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);

		// 4. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_SRWA_12D_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_SRWA_12D MOD Updated : " + modUpdated);
		System.out.println("M_SRWA_12D Verification and Transfer Completed Successfully");
	}

	private String getSRWA12DColumns() {
		StringBuilder sb = new StringBuilder();
		// R12-R15 Exchange and Interest
		String[] exchCols = { "LINE_NO_EXCHANGE_CONTRACTS", "RESIDUAL_MATURITY_EXCHANGE_CONTRACTS",
				"PRINCIPAL_AMOUNT_EXCHANGE_CONTRACTS", "TOTAL_CURRENT_EXCHANGE_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXCHANGE_CONTRACTS", "POTENTIAL_FUTURE_CREDIT_EXPOSURE_EXCHANGE_CONTRACTS",
				"CREDIT_EQUIVALENT_EXCHANGE_CONTRACTS", "APPLICABLE_COUNTERPARTY_EXCHANGE_CONTRACTS",
				"RISK_WEIGHTED_ASSET_EXCHANGE_CONTRACTS", "LINE_NO_INTEREST_CONTRACTS",
				"RESIDUAL_MATURITY_INTEREST_CONTRACTS", "PRINCIPAL_AMOUNT_INTEREST_CONTRACTS",
				"TOTAL_CURRENT_INTEREST_CONTRACTS", "POTENTIAL_FUTURE_CREDIT_INTEREST_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_INTEREST_CONTRACTS", "CREDIT_EQUIVALENT_INTEREST_CONTRACTS",
				"APPLICABLE_COUNTERPARTY_INTEREST_CONTRACTS", "RISK_WEIGHTED_ASSET_INTEREST_CONTRACTS" };
		for (int r = 12; r <= 15; r++) {
			for (String col : exchCols) {
				sb.append("R").append(r).append("_").append(col).append(", ");
			}
		}

		// R21-R24 Equity and Precious
		String[] eqCols = { "LINE_NO_EQUITY_CONTRACTS", "RESIDUAL_MATURITY_EQUITY_CONTRACTS",
				"PRINCIPAL_AMOUNT_EQUITY_CONTRACTS", "TOTAL_CURRENT_EQUITY_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EQUITY_CONTRACTS", "POTENTIAL_FUTURE_CREDIT_EXPOSURE_EQUITY_CONTRACTS",
				"CREDIT_EQUIVALENT_EQUITY_CONTRACTS", "APPLICABLE_COUNTERPARTY_EQUITY_CONTRACTS",
				"RISK_WEIGHTED_ASSET_EQUITY_CONTRACTS", "LINE_NO_PRECIOUS_CONTRACTS",
				"RESIDUAL_MATURITY_PRECIOUS_CONTRACTS", "PRINCIPAL_AMOUNT_PRECIOUS_CONTRACTS",
				"TOTAL_CURRENT_PRECIOUS_CONTRACTS", "POTENTIAL_FUTURE_CREDIT_PRECIOUS_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_EXPOSURE_PRECIOUS_CONTRACTS", "CREDIT_EQUIVALENT_PRECIOUS_CONTRACTS",
				"APPLICABLE_COUNTERPARTY_PRECIOUS_CONTRACTS", "RISK_WEIGHTED_ASSET_PRECIOUS_CONTRACTS" };
		for (int r = 21; r <= 24; r++) {
			for (String col : eqCols) {
				sb.append("R").append(r).append("_").append(col).append(", ");
			}
		}

		// R30-R33 Debt and Credit
		String[] debtCols = { "LINE_NO_DEBT_CONTRACTS", "RESIDUAL_MATURITY_DEBT_CONTRACTS",
				"PRINCIPAL_AMOUNT_DEBT_CONTRACTS", "TOTAL_CURRENT_DEBT_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_DEBT_CONTRACTS", "POTENTIAL_FUTURE_CREDIT_EXPOSURE_DEBT_CONTRACTS",
				"CREDIT_EQUIVALENT_DEBT_CONTRACTS", "APPLICABLE_COUNTERPARTY_DEBT_CONTRACTS",
				"RISK_WEIGHTED_ASSET_DEBT_CONTRACTS", "LINE_NO_CREDIT_CONTRACTS", "RESIDUAL_MATURITY_CREDIT_CONTRACTS",
				"PRINCIPAL_AMOUNT_CREDIT_CONTRACTS", "TOTAL_CURRENT_CREDIT_CONTRACTS",
				"POTENTIAL_FUTURE_CREDIT_CREDIT_CONTRACTS", "POTENTIAL_FUTURE_CREDIT_EXPOSURE_CREDIT_CONTRACTS",
				"CREDIT_EQUIVALENT_CREDIT_CONTRACTS", "APPLICABLE_COUNTERPARTY_CREDIT_CONTRACTS",
				"RISK_WEIGHTED_ASSET_CREDIT_CONTRACTS" };
		for (int r = 30; r <= 33; r++) {
			for (String col : debtCols) {
				sb.append("R").append(r).append("_").append(col).append(", ");
			}
		}

		// R43 Derivative
		sb.append("R43_LINE_NO_DERIVATIVE_CONTRACTS, R43_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS, ").append(
				"R43_POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS, R43_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS, ")
				.append("R43_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS, R43_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS, ")
				.append("R43_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS, ");

		// R44 Derivative
		sb.append("R44_LINE_NO_DERIVATIVE_CONTRACTS, R44_NETTED_EXPOSURES_DERIVATIVE_CONTRACTS, ").append(
				"R44_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS, R44_POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS, ")
				.append("R44_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS, R44_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS, ")
				.append("R44_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS, R44_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS, ");

		// R45 Derivative
		sb.append("R45_LINE_NO_DERIVATIVE_CONTRACTS, R45_PRINCIPAL_AMOUNT_DERIVATIVE_CONTRACTS, ").append(
				"R45_POSITIVE_NET_REPLACEMENT_DERIVATIVE_CONTRACTS, R45_ADDON_FOR_NETTED_DERIVATIVE_CONTRACTS, ")
				.append("R45_CREDIT_EQUIVALENT_DERIVATIVE_CONTRACTS, R45_APPLICABLE_COUNTERPARTY_DERIVATIVE_CONTRACTS, ")
				.append("R45_RISK_WEIGHTED_ASSET_DERIVATIVE_CONTRACTS, ");

		sb.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		return sb.toString();
	}

	private String getSRWA12DPrefixedColumns(String prefix) {
		String[] cols = getSRWA12DColumns().split(", ");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cols.length; i++) {
			if (i > 0)
				sb.append(", ");
			sb.append(prefix).append(cols[i].trim());
		}
		return sb.toString();
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

	private BigDecimal sfinp1AbsValue(Object value) {
		return value == null ? BigDecimal.ZERO : new BigDecimal(value.toString()).abs();
	}

	private void transferSFINP1Data(AuditServicesEntity auditRecord) {

		final String liveTable = "BRRS_M_SFINP1_SUMMARYTABLE_MANUAL";
		final String modTable = "BRRS_M_SFINP1_MOD_SUMMARYTABLE_MANUAL";
		final String summaryTable = "BRRS_M_SFINP1_SUMMARYTABLE";
		final String archivalTable = "BRRS_M_SFINP1_ARCHIVALTABLE_SUMMARY_MANUAL";

		final String[] manualRows = { "R34", "R37", "R39", "R43", "R50", "R51", "R52", "R57", "R59" };

		final String[] averageRows = { "R34", "R37", "R43", "R42", "R50", "R51", "R52", "R57", "R59" };

		java.sql.Date reportDate = resolveSFINP1ReportDate(auditRecord, modTable);

		if (reportDate == null) {
			throw new IllegalStateException("No pending M_SFINP1 modification available for verification.");
		}
		System.out.println("M_SFINP1 Report Date : " + reportDate);

		StringBuilder colList = new StringBuilder();
		StringBuilder srcList = new StringBuilder();
		for (String r : manualRows) {
			colList.append(r).append("_MONTH_END, ");
			srcList.append("S.").append(r).append("_MONTH_END, ");
		}
		colList.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		srcList.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		String updateSql = "UPDATE " + liveTable + " M SET (" + colList + ") = (SELECT " + srcList + " FROM " + modTable
				+ " S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) " + "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int updatedLive = jdbcTemplate.update(updateSql, reportDate);

		if (updatedLive == 0) {
			throw new IllegalStateException("M_SFINP1 live manual record not found for date : " + reportDate);
		}
		System.out.println("M_SFINP1 Main Updated : " + updatedLive);

		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(reportDate);
		cal.add(java.util.Calendar.MONTH, -1);
		java.util.Date previousMonthDate = cal.getTime();

		StringBuilder avgCols = new StringBuilder();
		for (String r : averageRows) {
			if (avgCols.length() > 0) {
				avgCols.append(", ");
			}
			avgCols.append(r).append("_MONTH_END");
		}

		List<java.util.Map<String, Object>> currRows = jdbcTemplate.queryForList(
				"SELECT " + avgCols + " FROM " + liveTable + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)", reportDate);
		List<java.util.Map<String, Object>> prevRows = jdbcTemplate.queryForList(
				"SELECT " + avgCols + " FROM " + archivalTable + " WHERE REPORT_DATE = ?", previousMonthDate);

		java.util.Map<String, Object> curr = currRows.isEmpty() ? null : currRows.get(0);
		java.util.Map<String, Object> prev = prevRows.isEmpty() ? null : prevRows.get(0);

		StringBuilder setClause = new StringBuilder();
		List<Object> params = new java.util.ArrayList<Object>();
		for (String r : averageRows) {
			String col = r + "_MONTH_END";
			BigDecimal c = sfinp1AbsValue(curr == null ? null : curr.get(col));
			BigDecimal p = sfinp1AbsValue(prev == null ? null : prev.get(col));
			BigDecimal avg = c.add(p).divide(BigDecimal.valueOf(2), 0, java.math.RoundingMode.HALF_UP);

			if (setClause.length() > 0) {
				setClause.append(", ");
			}
			setClause.append(r).append("_AVERAGE = ?");
			params.add(avg);
		}
		params.add(reportDate);

		int updatedSummary = jdbcTemplate.update(
				"UPDATE " + summaryTable + " SET " + setClause + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				params.toArray());
		if (updatedSummary == 0) {
			throw new IllegalStateException("M_SFINP1 summary record not found for date : " + reportDate);
		}
		System.out.println("M_SFINP1 Averages Updated : " + updatedSummary);

		java.util.Map<String, Object> sumRow = jdbcTemplate
				.queryForList("SELECT * FROM " + summaryTable + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)", reportDate)
				.get(0);

		BigDecimal[] me = new BigDecimal[62];
		BigDecimal[] av = new BigDecimal[62];
		for (int r = 10; r <= 61; r++) {
			me[r] = sfinp1Num(sumRow.get("R" + r + "_MONTH_END"));
			av[r] = sfinp1Num(sumRow.get("R" + r + "_AVERAGE"));
		}

		for (String r : manualRows) {
			if (!"R39".equals(r)) {
				me[Integer.parseInt(r.substring(1))] = sfinp1Num(curr == null ? null : curr.get(r + "_MONTH_END"));
			}
		}

		for (BigDecimal[] a : new BigDecimal[][] { me, av }) {
			a[35] = a[36].add(a[37]);
			a[40] = a[41].add(a[42]).add(a[43]);
			a[38] = a[39].subtract(a[40]);
			a[49] = a[50].add(a[51]).add(a[52]);
			a[56] = a[57].add(a[58]).add(a[59]).add(a[60]);
			BigDecimal total = BigDecimal.ZERO;
			for (int r : new int[] { 10, 11, 12, 17, 18, 19, 20, 27, 31, 34, 35, 38, 44, 49, 53, 54, 55, 56 }) {
				total = total.add(a[r]);
			}
			a[61] = total;
		}

		StringBuilder subSet = new StringBuilder();
		List<Object> subParams = new java.util.ArrayList<Object>();
		for (int r : new int[] { 35, 38, 40, 49, 56, 61 }) {
			if (subSet.length() > 0) {
				subSet.append(", ");
			}
			subSet.append("R").append(r).append("_MONTH_END = ?, R").append(r).append("_AVERAGE = ?");
			subParams.add(me[r]);
			subParams.add(av[r]);
		}
		subParams.add(reportDate);
		jdbcTemplate.update("UPDATE " + summaryTable + " SET " + subSet + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				subParams.toArray());
		System.out.println("M_SFINP1 Subtotals Updated");

		int updatedMod = jdbcTemplate.update(
				"UPDATE " + modTable + " SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);
		System.out.println("M_SFINP1 MOD Updated : " + updatedMod);
	}

	private BigDecimal sfinp1Num(Object value) {
		return value == null ? BigDecimal.ZERO : new BigDecimal(value.toString());
	}

	private java.sql.Date resolveSFINP1ReportDate(AuditServicesEntity auditRecord, String modTable) {
		java.util.Date parsed = null;
		String raw = auditRecord.getChange_details();
		if (raw != null && !raw.trim().isEmpty()) {
			String[] patterns = { "EEE MMM dd HH:mm:ss zzz yyyy", "yyyy-MM-dd HH:mm:ss.S", "yyyy-MM-dd HH:mm:ss",
					"yyyy-MM-dd", "dd-MM-yyyy", "dd/MM/yyyy", "dd-MMM-yyyy" };
			for (String pattern : patterns) {
				try {
					parsed = new SimpleDateFormat(pattern, Locale.ENGLISH).parse(raw.trim());
					break;
				} catch (Exception ignored) {
				}
			}
		}
		if (parsed != null) {
			java.sql.Date d = new java.sql.Date(parsed.getTime());
			Integer pending = jdbcTemplate.queryForObject(
					"SELECT COUNT(*) FROM " + modTable + " WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND MODIFY_FLG = 'Y'",
					Integer.class, d);
			if (pending == null || pending == 0) {
				throw new IllegalStateException(
						"No pending M_SFINP1 modification for " + raw.trim() + " (already verified?).");
			}
			return d;
		}
		return jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM " + modTable + " WHERE MODIFY_FLG = 'Y'",
				java.sql.Date.class);
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

	private void transferBDISB1Data(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_BDISB1_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified BDISB1 record available for verification.");
		}

		System.out.println("BDISB1 Report Date : " + reportDate);

		String[] fields = {
			"RECORD_NUMBER", "TITLE", "FIRST_NAME", "MIDDLE_NAME", "SURNAME",
			"PREVIOUS_NAME", "GENDER", "IDENTIFICATION_TYPE", "PASSPORT_NUMBER",
			"DATE_OF_BIRTH", "HOME_ADDRESS", "POSTAL_ADDRESS", "RESIDENCE",
			"EMAIL", "LANDLINE", "MOBILE_PHONE_NUMBER", "MOBILE_MONEY_NUMBER",
			"PRODUCT_TYPE", "ACCOUNT_BY_OWNERSHIP", "ACCOUNT_NUMBER",
			"ACCOUNT_HOLDER_INDICATOR", "STATUS_OF_ACCOUNT", "NOT_FIT_FOR_STP",
			"BRANCH_CODE_AND_NAME", "ACCOUNT_BALANCE_IN_PULA", "CURRENCY_OF_ACCOUNT",
			"EXCHANGE_RATE"
		};

		StringBuilder colList = new StringBuilder();
		StringBuilder selectList = new StringBuilder();

		for (int i = 5; i <= 11; i++) {
			for (String f : fields) {
				colList.append("R").append(i).append("_").append(f).append(", ");
				selectList.append("S.R").append(i).append("_").append(f).append(", ");
			}
		}
		colList.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		selectList.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		// 1. Update BRRS_BDISB1_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_BDISB1_SUMMARYTABLE M SET ("
				+ colList.toString() + ") = (SELECT "
				+ selectList.toString() + " FROM BRRS_BDISB1_MOD_SUMMARYTABLE S "
				+ " WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("BDISB1 modification record not found for date : " + reportDate);
		}

		System.out.println("BDISB1 Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_BDISB1_DETAILTABLE from MOD table (Same column mapping)
		String updateDetailSql = "UPDATE BRRS_BDISB1_DETAILTABLE M SET ("
				+ colList.toString() + ") = (SELECT "
				+ selectList.toString() + " FROM BRRS_BDISB1_MOD_SUMMARYTABLE S "
				+ " WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("BDISB1 Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_BDISB1_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("BDISB1 MOD Updated : " + modUpdated);
		System.out.println("BDISB1 Verification and Transfer Completed Successfully");
	}

	// =========================================================
	// M_SRWA_12F — Checker transfer (MOD → LIVE Summary + Detail)
	// =========================================================
	private void transferMSRWA12FData(AuditServicesEntity auditRecord) {
		// 1. Find the pending (MODIFY_FLG='Y') report date; fall back to MAX if none
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12F_MOD_SUMMARYTABLE WHERE MODIFY_FLG = 'Y'";
		java.sql.Date reportDate = null;
		try {
			reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
		} catch (Exception e) {
			// fallback below
		}
		if (reportDate == null) {
			reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12F_MOD_SUMMARYTABLE",
					java.sql.Date.class);
		}
		if (reportDate == null) {
			throw new IllegalStateException("No modified M_SRWA_12F record available for verification.");
		}

		System.out.println("M_SRWA_12F Report Date : " + reportDate);

		// 2. Build the (cols) = (SELECT cols FROM MOD) pieces once
		String columnList = buildMSRWA12FColumnList();
		String selectList = buildMSRWA12FSelectList();

		// 3. Update BRRS_M_SRWA_12F_SUMMARYTABLE from MOD
		String updateSummarySql = "UPDATE BRRS_M_SRWA_12F_SUMMARYTABLE M " + "SET (" + columnList + ") = ("
				+ "    SELECT " + selectList + "    FROM BRRS_M_SRWA_12F_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);
		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_SRWA_12F modification record not found for date : " + reportDate);
		}
		System.out.println("M_SRWA_12F Summary Table Updated : " + summaryUpdated);

		// 4. Update BRRS_M_SRWA_12F_DETAILTABLE from MOD (identical column/select
		// lists)
		String updateDetailSql = "UPDATE BRRS_M_SRWA_12F_DETAILTABLE M " + "SET (" + columnList + ") = ("
				+ "    SELECT " + selectList + "    FROM BRRS_M_SRWA_12F_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_SRWA_12F Detail Table Updated : " + detailUpdated);

		// 5. Mark MOD record as approved
		String updateModSql = "UPDATE BRRS_M_SRWA_12F_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' "
				+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_SRWA_12F MOD Updated : " + modUpdated);
		System.out.println("M_SRWA_12F Verification and Transfer Completed Successfully");
	}

	// =========================================================
	// M_SRWA_12G — Checker transfer (MOD → LIVE Summary + Detail)
	// =========================================================
	private void transferMSRWA12GData(AuditServicesEntity auditRecord) {

		// 1. Locate the pending MOD row (MODIFY_FLG = 'Y'); fall back to MAX date if
		// none pending
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12G_MOD_SUMMARYTABLE WHERE MODIFY_FLG = 'Y'";
		java.sql.Date reportDate = null;
		try {
			reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
		} catch (Exception ignored) {
			// fall through to MAX fallback
		}
		if (reportDate == null) {
			reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12G_MOD_SUMMARYTABLE",
					java.sql.Date.class);
		}
		if (reportDate == null) {
			throw new IllegalStateException("No modified M_SRWA_12G record available for verification.");
		}

		System.out.println("M_SRWA_12G Report Date : " + reportDate);

		// 2. Build column / select fragments once (identical for summary + detail)
		String columnList = buildMSRWA12GColumnList();
		String selectList = buildMSRWA12GSelectList();

		// 3. Transfer MOD → LIVE Summary
		String sqlSummary = "UPDATE BRRS_M_SRWA_12G_SUMMARYTABLE M " + "SET (" + columnList + ") = (" + "    SELECT "
				+ selectList + "    FROM BRRS_M_SRWA_12G_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(sqlSummary, reportDate);
		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_SRWA_12G modification record not found for date : " + reportDate);
		}
		System.out.println("M_SRWA_12G Summary Table Updated : " + summaryUpdated);

		// 4. Transfer MOD → LIVE Detail (only if the detail table exists for this
		// report)
		try {
			String sqlDetail = "UPDATE BRRS_M_SRWA_12G_DETAILTABLE M " + "SET (" + columnList + ") = (" + "    SELECT "
					+ selectList + "    FROM BRRS_M_SRWA_12G_MOD_SUMMARYTABLE S "
					+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
					+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

			int detailUpdated = jdbcTemplate.update(sqlDetail, reportDate);
			System.out.println("M_SRWA_12G Detail Table Updated : " + detailUpdated);
		} catch (org.springframework.jdbc.BadSqlGrammarException ex) {
			System.out.println("M_SRWA_12G DETAILTABLE not present — summary-only report. Skipping detail transfer.");
		}

		// 5. Flip the MOD row to approved
		int modUpdated = jdbcTemplate
				.update("UPDATE BRRS_M_SRWA_12G_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' "
						+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?)", reportDate);

		System.out.println("M_SRWA_12G MOD Updated : " + modUpdated);
		System.out.println("M_SRWA_12G Verification and Transfer Completed Successfully");
	}

	// ─────────────────────────────────────────────────────────
	// M_SRWA_12G — Column builders
	// R11–R60: 6 fields per row
	// R61 : 2 fields only (EXPOSURE_AMOUNT, RISK_WEIGHTED_AMOUNT)
	// ─────────────────────────────────────────────────────────
	private static String buildMSRWA12GColumnList() {
		StringBuilder sb = new StringBuilder();

		for (int row = 11; row <= 60; row++) {
			String p = "R" + row + "_";
			sb.append(p).append("SECURITY_FIRM, ").append(p).append("CREDIT_RATING, ").append(p)
					.append("RATING_AGENCY, ").append(p).append("EXPOSURE_AMOUNT, ").append(p).append("RISK_WEIGHT, ")
					.append(p).append("RISK_WEIGHTED_AMOUNT, ");
		}

		// Row 61 — only two numeric columns; no SECURITY_FIRM / CREDIT_RATING /
		// RATING_AGENCY / RISK_WEIGHT exist in the 12G schema
		sb.append("R61_EXPOSURE_AMOUNT, R61_RISK_WEIGHTED_AMOUNT, ");

		sb.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		return sb.toString();
	}

	private static String buildMSRWA12GSelectList() {
		StringBuilder sb = new StringBuilder();

		for (int row = 11; row <= 60; row++) {
			String p = "S.R" + row + "_";
			sb.append(p).append("SECURITY_FIRM, ").append(p).append("CREDIT_RATING, ").append(p)
					.append("RATING_AGENCY, ").append(p).append("EXPOSURE_AMOUNT, ").append(p).append("RISK_WEIGHT, ")
					.append(p).append("RISK_WEIGHTED_AMOUNT, ");
		}

		sb.append("S.R61_EXPOSURE_AMOUNT, S.R61_RISK_WEIGHTED_AMOUNT, ");

		sb.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		return sb.toString();
	}

	// =========================================================
	// M_SRWA_12H — Checker transfer (MOD → LIVE Summary + Detail)
	// =========================================================
	private void transferMSRWA12HData(AuditServicesEntity auditRecord) {

		// 1. Prefer the pending MOD row (MODIFY_FLG='Y'); fall back to MAX date
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12H_MOD_SUMMARYTABLE WHERE MODIFY_FLG = 'Y'";
		java.sql.Date reportDate = null;
		try {
			reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
		} catch (Exception ignored) {
			// fall through to MAX fallback
		}
		if (reportDate == null) {
			reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12H_MOD_SUMMARYTABLE",
					java.sql.Date.class);
		}
		if (reportDate == null) {
			throw new IllegalStateException("No modified M_SRWA_12H record available for verification.");
		}

		System.out.println("M_SRWA_12H Report Date : " + reportDate);

		// 2. Build column/select fragments once (identical for summary + detail)
		String columnList = buildMSRWA12HColumnList();
		String selectList = buildMSRWA12HSelectList();

		// 3. Transfer MOD → LIVE Summary
		String sqlSummary = "UPDATE BRRS_M_SRWA_12H_SUMMARYTABLE M " + "SET (" + columnList + ") = (" + "    SELECT "
				+ selectList + "    FROM BRRS_M_SRWA_12H_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(sqlSummary, reportDate);
		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_SRWA_12H modification record not found for date : " + reportDate);
		}
		System.out.println("M_SRWA_12H Summary Table Updated : " + summaryUpdated);

		// 4. Transfer MOD → LIVE Detail (guarded — 12H detail table may not exist)
		try {
			String sqlDetail = "UPDATE BRRS_M_SRWA_12H_DETAILTABLE M " + "SET (" + columnList + ") = (" + "    SELECT "
					+ selectList + "    FROM BRRS_M_SRWA_12H_MOD_SUMMARYTABLE S "
					+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
					+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

			int detailUpdated = jdbcTemplate.update(sqlDetail, reportDate);
			System.out.println("M_SRWA_12H Detail Table Updated : " + detailUpdated);
		} catch (org.springframework.jdbc.BadSqlGrammarException ex) {
			System.out.println("M_SRWA_12H DETAILTABLE not present — summary-only report. Skipping detail transfer.");
		}

		// 5. Flip the MOD row to approved
		int modUpdated = jdbcTemplate
				.update("UPDATE BRRS_M_SRWA_12H_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' "
						+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?)", reportDate);

		System.out.println("M_SRWA_12H MOD Updated : " + modUpdated);
		System.out.println("M_SRWA_12H Verification and Transfer Completed Successfully");
	}

	// ─────────────────────────────────────────────────────────
	// M_SRWA_12H — Column builders
	// R11..R81: 9 uniform fields per row (no sparse row like 12G's R61)
	// ─────────────────────────────────────────────────────────
	private static String buildMSRWA12HColumnList() {
		StringBuilder sb = new StringBuilder();
		String[] fields = { "PRODUCT", "ISSUER", "ISSUES_RATING", "1YR_VAL_OF_CRM", "1YR_5YR_VAL_OF_CRM",
				"5YR_VAL_OF_CRM", "OTHER", "STD_SUPERVISORY_HAIRCUT", "APPLICABLE_RISK_WEIGHT" };

		for (int row = 11; row <= 81; row++) {
			for (String f : fields) {
				sb.append("R").append(row).append("_").append(f).append(", ");
			}
		}
		sb.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		return sb.toString();
	}

	private static String buildMSRWA12HSelectList() {
		StringBuilder sb = new StringBuilder();
		String[] fields = { "PRODUCT", "ISSUER", "ISSUES_RATING", "1YR_VAL_OF_CRM", "1YR_5YR_VAL_OF_CRM",
				"5YR_VAL_OF_CRM", "OTHER", "STD_SUPERVISORY_HAIRCUT", "APPLICABLE_RISK_WEIGHT" };

		for (int row = 11; row <= 81; row++) {
			for (String f : fields) {
				sb.append("S.R").append(row).append("_").append(f).append(", ");
			}
		}
		sb.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		return sb.toString();
	}

	private void transferAIDPData(AuditServicesEntity auditRecord) {
		String upperTable = auditRecord.getAudit_table() != null ? auditRecord.getAudit_table().trim().toUpperCase()
				: "";

		if (upperTable.contains("TABLE1") || upperTable.endsWith("1")) {
			transferAIDPTable1Data();
		} else if (upperTable.contains("TABLE2") || upperTable.endsWith("2")) {
			transferAIDPTable2Data();
		} else if (upperTable.contains("TABLE3") || upperTable.endsWith("3")) {
			transferAIDPTable3Data();
		} else if (upperTable.contains("TABLE4") || upperTable.endsWith("4")) {
			transferAIDPTable4Data();
		} else {
			transferAIDPTable1Data();
			transferAIDPTable2Data();
			transferAIDPTable3Data();
			transferAIDPTable4Data();
		}
	}

	private void transferAIDPTable1Data() {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_AIDP_MOD_SUMMARYTABLE1 WHERE MODIFY_FLG = 'Y'";
		List<java.sql.Date> dates = jdbcTemplate.queryForList(dateSql, java.sql.Date.class);
		java.sql.Date reportDate = (!dates.isEmpty() && dates.get(0) != null) ? dates.get(0)
				: jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM BRRS_M_AIDP_MOD_SUMMARYTABLE1",
						java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_AIDP Table 1 record available for verification.");
		}

		System.out.println("M_AIDP Table 1 Report Date : " + reportDate);

		String cols = buildAIDPTable1Columns(false);
		String selectCols = buildAIDPTable1Columns(true);

		String updateSummarySql = "UPDATE BRRS_M_AIDP_SUMMARYTABLE1 M SET (" + cols + ") = (SELECT " + selectCols
				+ " FROM BRRS_M_AIDP_MOD_SUMMARYTABLE1 S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);
		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_AIDP Table 1 modification record not found for date : " + reportDate);
		}
		System.out.println("M_AIDP Summary Table 1 Updated : " + summaryUpdated);

		String updateDetailSql = "UPDATE BRRS_M_AIDP_DETAILTABLE1 M SET (" + cols + ") = (SELECT " + selectCols
				+ " FROM BRRS_M_AIDP_MOD_SUMMARYTABLE1 S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_AIDP Detail Table 1 Updated : " + detailUpdated);

		String updateModSql = "UPDATE BRRS_M_AIDP_MOD_SUMMARYTABLE1 SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);
		System.out.println("M_AIDP MOD Table 1 Updated : " + modUpdated);

		jdbcTemplate.update(
				"UPDATE BRRS_M_AIDP_SUMMARYTABLE1 SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N', DEL_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				reportDate);
		jdbcTemplate.update(
				"UPDATE BRRS_M_AIDP_DETAILTABLE1 SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N', DEL_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				reportDate);
		System.out.println("M_AIDP Table 1 Verification and Transfer Completed Successfully");
	}

	private void transferAIDPTable2Data() {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_AIDP_MOD_SUMMARYTABLE2 WHERE MODIFY_FLG = 'Y'";
		List<java.sql.Date> dates = jdbcTemplate.queryForList(dateSql, java.sql.Date.class);
		java.sql.Date reportDate = (!dates.isEmpty() && dates.get(0) != null) ? dates.get(0)
				: jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM BRRS_M_AIDP_MOD_SUMMARYTABLE2",
						java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_AIDP Table 2 record available for verification.");
		}

		System.out.println("M_AIDP Table 2 Report Date : " + reportDate);

		String cols = buildAIDPTable2Columns(false);
		String selectCols = buildAIDPTable2Columns(true);

		String updateSummarySql = "UPDATE BRRS_M_AIDP_SUMMARYTABLE2 M SET (" + cols + ") = (SELECT " + selectCols
				+ " FROM BRRS_M_AIDP_MOD_SUMMARYTABLE2 S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);
		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_AIDP Table 2 modification record not found for date : " + reportDate);
		}
		System.out.println("M_AIDP Summary Table 2 Updated : " + summaryUpdated);

		String updateDetailSql = "UPDATE BRRS_M_AIDP_DETAILTABLE2 M SET (" + cols + ") = (SELECT " + selectCols
				+ " FROM BRRS_M_AIDP_MOD_SUMMARYTABLE2 S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_AIDP Detail Table 2 Updated : " + detailUpdated);

		String updateModSql = "UPDATE BRRS_M_AIDP_MOD_SUMMARYTABLE2 SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);
		System.out.println("M_AIDP MOD Table 2 Updated : " + modUpdated);

		jdbcTemplate.update(
				"UPDATE BRRS_M_AIDP_SUMMARYTABLE2 SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N', DEL_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				reportDate);
		jdbcTemplate.update(
				"UPDATE BRRS_M_AIDP_DETAILTABLE2 SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N', DEL_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				reportDate);
		System.out.println("M_AIDP Table 2 Verification and Transfer Completed Successfully");
	}

	private void transferAIDPTable3Data() {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_AIDP_MOD_SUMMARYTABLE3 WHERE MODIFY_FLG = 'Y'";
		List<java.sql.Date> dates = jdbcTemplate.queryForList(dateSql, java.sql.Date.class);
		java.sql.Date reportDate = (!dates.isEmpty() && dates.get(0) != null) ? dates.get(0)
				: jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM BRRS_M_AIDP_MOD_SUMMARYTABLE3",
						java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_AIDP Table 3 record available for verification.");
		}

		System.out.println("M_AIDP Table 3 Report Date : " + reportDate);

		String cols = buildAIDPTable3Columns(false);
		String selectCols = buildAIDPTable3Columns(true);

		String updateSummarySql = "UPDATE BRRS_M_AIDP_SUMMARYTABLE3 M SET (" + cols + ") = (SELECT " + selectCols
				+ " FROM BRRS_M_AIDP_MOD_SUMMARYTABLE3 S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);
		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_AIDP Table 3 modification record not found for date : " + reportDate);
		}
		System.out.println("M_AIDP Summary Table 3 Updated : " + summaryUpdated);

		String updateDetailSql = "UPDATE BRRS_M_AIDP_DETAILTABLE3 M SET (" + cols + ") = (SELECT " + selectCols
				+ " FROM BRRS_M_AIDP_MOD_SUMMARYTABLE3 S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_AIDP Detail Table 3 Updated : " + detailUpdated);

		String updateModSql = "UPDATE BRRS_M_AIDP_MOD_SUMMARYTABLE3 SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);
		System.out.println("M_AIDP MOD Table 3 Updated : " + modUpdated);

		jdbcTemplate.update(
				"UPDATE BRRS_M_AIDP_SUMMARYTABLE3 SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N', DEL_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				reportDate);
		jdbcTemplate.update(
				"UPDATE BRRS_M_AIDP_DETAILTABLE3 SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N', DEL_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				reportDate);
		System.out.println("M_AIDP Table 3 Verification and Transfer Completed Successfully");
	}

	private void transferAIDPTable4Data() {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_AIDP_MOD_SUMMARYTABLE4 WHERE MODIFY_FLG = 'Y'";
		List<java.sql.Date> dates = jdbcTemplate.queryForList(dateSql, java.sql.Date.class);
		java.sql.Date reportDate = (!dates.isEmpty() && dates.get(0) != null) ? dates.get(0)
				: jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM BRRS_M_AIDP_MOD_SUMMARYTABLE4",
						java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_AIDP Table 4 record available for verification.");
		}

		System.out.println("M_AIDP Table 4 Report Date : " + reportDate);

		String cols = buildAIDPTable4Columns(false);
		String selectCols = buildAIDPTable4Columns(true);

		String updateSummarySql = "UPDATE BRRS_M_AIDP_SUMMARYTABLE4 M SET (" + cols + ") = (SELECT " + selectCols
				+ " FROM BRRS_M_AIDP_MOD_SUMMARYTABLE4 S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);
		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_AIDP Table 4 modification record not found for date : " + reportDate);
		}
		System.out.println("M_AIDP Summary Table 4 Updated : " + summaryUpdated);

		String updateDetailSql = "UPDATE BRRS_M_AIDP_DETAILTABLE4 M SET (" + cols + ") = (SELECT " + selectCols
				+ " FROM BRRS_M_AIDP_MOD_SUMMARYTABLE4 S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_AIDP Detail Table 4 Updated : " + detailUpdated);

		String updateModSql = "UPDATE BRRS_M_AIDP_MOD_SUMMARYTABLE4 SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);
		System.out.println("M_AIDP MOD Table 4 Updated : " + modUpdated);

		jdbcTemplate.update(
				"UPDATE BRRS_M_AIDP_SUMMARYTABLE4 SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N', DEL_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				reportDate);
		jdbcTemplate.update(
				"UPDATE BRRS_M_AIDP_DETAILTABLE4 SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N', DEL_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				reportDate);
		System.out.println("M_AIDP Table 4 Verification and Transfer Completed Successfully");
	}

	private String buildAIDPTable1Columns(boolean withPrefix) {
		StringBuilder sb = new StringBuilder();
		String[] fields = { "NAME_OF_BANK", "TYPE_OF_ACC", "PURPOSE", "CURRENCY", "BANK_RATE", "AMT_LESS_184_DAYS",
				"AMT_MORE_184_DAYS" };
		for (int i = 11; i <= 50; i++) {
			for (String f : fields) {
				if (withPrefix)
					sb.append("S.");
				sb.append("R").append(i).append("_").append(f).append(", ");
			}
		}
		if (withPrefix) {
			sb.append(
					"S.R51_TOT_AMT_LESS_184_DAYS, S.R51_TOT_AMT_MORE_184_DAYS, S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		} else {
			sb.append("R51_TOT_AMT_LESS_184_DAYS, R51_TOT_AMT_MORE_184_DAYS, ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		}
		return sb.toString();
	}

	private String buildAIDPTable2Columns(boolean withPrefix) {
		StringBuilder sb = new StringBuilder();
		String[] fields = { "NAME_OF_BANK", "TYPE_OF_ACC", "PURPOSE", "CURRENCY", "BANK_RATE", "AMT_LESS_184_DAYS",
				"AMT_MORE_184_DAYS" };
		for (int i = 56; i <= 95; i++) {
			for (String f : fields) {
				if (withPrefix)
					sb.append("S.");
				sb.append("R").append(i).append("_").append(f).append(", ");
			}
		}
		if (withPrefix) {
			sb.append(
					"S.R96_TOT_AMT_LESS_184_DAYS, S.R96_TOT_AMT_MORE_184_DAYS, S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		} else {
			sb.append("R96_TOT_AMT_LESS_184_DAYS, R96_TOT_AMT_MORE_184_DAYS, ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		}
		return sb.toString();
	}

	private String buildAIDPTable3Columns(boolean withPrefix) {
		StringBuilder sb = new StringBuilder();
		String[] fields = { "NAME_OF_BANK", "TYPE_OF_ACC", "PURPOSE", "CURRENCY", "BANK_RATE", "AMT_DEMAND",
				"AMT_TIME" };
		for (int i = 101; i <= 141; i++) {
			for (String f : fields) {
				if (withPrefix)
					sb.append("S.");
				sb.append("R").append(i).append("_").append(f).append(", ");
			}
		}
		if (withPrefix) {
			sb.append("S.R142_TOT_AMT_DEMAND, S.R142_TOT_AMT_TIME, S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		} else {
			sb.append("R142_TOT_AMT_DEMAND, R142_TOT_AMT_TIME, ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		}
		return sb.toString();
	}

	private String buildAIDPTable4Columns(boolean withPrefix) {
		StringBuilder sb = new StringBuilder();
		String[] fields = { "NAME_OF_BANK", "COUNTRY", "TYPE_OF_ACC", "PURPOSE", "CURRENCY", "BANK_RATE", "AMT_DEMAND",
				"AMT_TIME" };
		for (int i = 147; i <= 193; i++) {
			for (String f : fields) {
				if (withPrefix)
					sb.append("S.");
				sb.append("R").append(i).append("_").append(f).append(", ");
			}
		}
		if (withPrefix) {
			sb.append("S.R194_TOT_AMT_DEMAND, S.R194_TOT_AMT_TIME, S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		} else {
			sb.append("R194_TOT_AMT_DEMAND, R194_TOT_AMT_TIME, ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		}
		return sb.toString();
	}

	private void transferIntRatesFcaData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_INT_RATES_FCA_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_INT_RATES_FCA record available for verification.");
		}

		System.out.println("M_INT_RATES_FCA Report Date : " + reportDate);

		String[] rows = { "R10", "R11", "R12", "R13", "R14", "R15" };
		String[] fields = { "CURRENCY", "CURRENT", "CALL", "SAVINGS", "NOTICE_0_31_DAYS", "NOTICE_32_88_DAYS",
				"91_DEPOSIT_DAY", "FD_1_6_MONTHS", "FD_7_12_MONTHS", "FD_13_18_MONTHS", "FD_19_24_MONTHS",
				"FD_OVER_24_MONTHS", "TOTAL" };

		StringBuilder colsBuilder = new StringBuilder();
		StringBuilder selBuilder = new StringBuilder();

		for (String r : rows) {
			for (String f : fields) {
				String col = r + "_" + f;
				colsBuilder.append(col).append(", ");
				selBuilder.append("S.").append(col).append(", ");
			}
		}
		colsBuilder.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		selBuilder.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		String cols = colsBuilder.toString();
		String sel = selBuilder.toString();

		// 1. Update BRRS_M_INT_RATES_FCA_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_INT_RATES_FCA_SUMMARYTABLE M SET (" + cols + ") = (" + "    SELECT "
				+ sel + " FROM BRRS_M_INT_RATES_FCA_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_INT_RATES_FCA modification record not found for date : " + reportDate);
		}

		System.out.println("M_INT_RATES_FCA Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_INT_RATES_FCA_DETAILTABLE from MOD table (Same column
		// mapping)
		String updateDetailSql = "UPDATE BRRS_M_INT_RATES_FCA_DETAILTABLE M SET (" + cols + ") = (" + "    SELECT "
				+ sel + " FROM BRRS_M_INT_RATES_FCA_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_INT_RATES_FCA Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_INT_RATES_FCA_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_INT_RATES_FCA MOD Updated : " + modUpdated);
		System.out.println("M_INT_RATES_FCA Verification and Transfer Completed Successfully");
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
				+ "    R27_PRODUCT, R27_CURRENT, R27_CALL, R27_SAVINGS, R27_0_31D_NOTICE, R27_32_88D_NOTICE, R27_91D_DEPOSIT, R27_1_2M_FD, R27_4_6M_FD, R27_7_12M_FD, R27_13_18M_FD, R27_19_24M_FD, R27_OVER24_FD, R27_TOTAL, R27_NOACC, "
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
				+ "    S.R27_PRODUCT, S.R27_CURRENT, S.R27_CALL, S.R27_SAVINGS, S.R27_0_31D_NOTICE, S.R27_32_88D_NOTICE, S.R27_91D_DEPOSIT, S.R27_1_2M_FD, S.R27_4_6M_FD, S.R27_7_12M_FD, S.R27_13_18M_FD, S.R27_19_24M_FD, S.R27_OVER24_FD, S.R27_TOTAL, S.R27_NOACC, "
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
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
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
				+ "    S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_CA4_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
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
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
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
				+ "    S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_CA4_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		if (detailUpdated == 0) {
			throw new IllegalStateException("M_CA4 detail record not found for date : " + reportDate);
		}
		System.out.println("M_CA4 Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD row approved
		String updateModSql = "UPDATE BRRS_M_CA4_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' "
				+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);
		System.out.println("M_CA4 MOD Updated : " + modUpdated);

		System.out.println("M_CA4 Verification and Transfer Completed Successfully");
	}

	// =========================================================
	// M_CA6 — Checker transfer (MOD → LIVE Summary + Detail)
	// =========================================================
	private void transferMCA6Data(AuditServicesEntity auditRecord) {
		String upperTable = auditRecord.getAudit_table() != null ? auditRecord.getAudit_table().toUpperCase() : "";
		boolean doTable1 = upperTable.contains("TABLE1") || !upperTable.contains("TABLE2");
		boolean doTable2 = upperTable.contains("TABLE2") || !upperTable.contains("TABLE1");

		if (doTable1) {
			transferMCA6Table1Data();
		}
		if (doTable2) {
			transferMCA6Table2Data();
		}
	}

	private void transferMCA6Table1Data() {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_CA6_MOD_SUMMARYTABLE1 WHERE MODIFY_FLG = 'Y'";
		java.sql.Date reportDate = null;
		try {
			reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
		} catch (Exception ignored) {
		}
		if (reportDate == null) {
			dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_CA6_MOD_SUMMARYTABLE1";
			try {
				reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
			} catch (Exception ignored) {
			}
		}
		if (reportDate == null) {
			System.out.println("No modified M_CA6 Table 1 record available for verification.");
			return;
		}
		System.out.println("M_CA6 Table 1 Report Date : " + reportDate);

		String cols = buildMCA6Table1ColumnList();
		String sel = buildMCA6Table1SelectList();

		String updateSummarySql = "UPDATE BRRS_M_CA6_SUMMARYTABLE1 M SET (" + cols + ") = (" + "    SELECT " + sel
				+ " FROM BRRS_M_CA6_MOD_SUMMARYTABLE1 S " + "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
				+ ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);
		System.out.println("M_CA6 Summary Table 1 Updated : " + summaryUpdated);

		String updateDetailSql = updateSummarySql.replace("BRRS_M_CA6_SUMMARYTABLE1", "BRRS_M_CA6_DETAILTABLE1");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_CA6 Detail Table 1 Updated : " + detailUpdated);

		String updateModSql = "UPDATE BRRS_M_CA6_MOD_SUMMARYTABLE1 SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);
		System.out.println("M_CA6 MOD Table 1 Updated : " + modUpdated);
		System.out.println("M_CA6 Table 1 Verification and Transfer Completed Successfully");
	}

	private void transferMCA6Table2Data() {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_CA6_MOD_SUMMARYTABLE2 WHERE MODIFY_FLG = 'Y'";
		java.sql.Date reportDate = null;
		try {
			reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
		} catch (Exception ignored) {
		}
		if (reportDate == null) {
			dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_CA6_MOD_SUMMARYTABLE2";
			try {
				reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
			} catch (Exception ignored) {
			}
		}
		if (reportDate == null) {
			System.out.println("No modified M_CA6 Table 2 record available for verification.");
			return;
		}
		System.out.println("M_CA6 Table 2 Report Date : " + reportDate);

		String cols = buildMCA6Table2ColumnList();
		String sel = buildMCA6Table2SelectList();

		String updateSummarySql = "UPDATE BRRS_M_CA6_SUMMARYTABLE2 M SET (" + cols + ") = (" + "    SELECT " + sel
				+ " FROM BRRS_M_CA6_MOD_SUMMARYTABLE2 S " + "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
				+ ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);
		System.out.println("M_CA6 Summary Table 2 Updated : " + summaryUpdated);

		String updateDetailSql = updateSummarySql.replace("BRRS_M_CA6_SUMMARYTABLE2", "BRRS_M_CA6_DETAILTABLE2");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_CA6 Detail Table 2 Updated : " + detailUpdated);

		String updateModSql = "UPDATE BRRS_M_CA6_MOD_SUMMARYTABLE2 SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);
		System.out.println("M_CA6 MOD Table 2 Updated : " + modUpdated);
		System.out.println("M_CA6 Table 2 Verification and Transfer Completed Successfully");
	}

	private static String buildMCA6Table1ColumnList() {
		StringBuilder sb = new StringBuilder();
		int[] rows = { 12, 13, 14, 15, 16, 20, 21, 22, 23, 24 };
		for (int r : rows) {
			sb.append("R").append(r).append("_CALENDAR_YEAR, ");
			sb.append("R").append(r).append("_CAP, ");
			sb.append("R").append(r).append("_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT, ");
			sb.append("R").append(r).append("_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT, ");
		}
		sb.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		return sb.toString();
	}

	private static String buildMCA6Table1SelectList() {
		StringBuilder sb = new StringBuilder();
		int[] rows = { 12, 13, 14, 15, 16, 20, 21, 22, 23, 24 };
		for (int r : rows) {
			sb.append("S.R").append(r).append("_CALENDAR_YEAR, ");
			sb.append("S.R").append(r).append("_CAP, ");
			sb.append("S.R").append(r).append("_CAP_ON_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT, ");
			sb.append("S.R").append(r).append("_AMT_ELIGIBLE_FOR_PHASEOUT_TREATMENT, ");
		}
		sb.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		return sb.toString();
	}

	private static String buildMCA6Table2ColumnList() {
		StringBuilder sb = new StringBuilder();
		int[] rows1 = { 28, 29, 30, 31, 32, 33, 34 };
		for (int r : rows1) {
			sb.append("R").append(r).append("_PRODUCT, ");
			sb.append("R").append(r).append("_ISSUANCE_DATE, ");
			sb.append("R").append(r).append("_CONTRACTUAL_MATURITY_DATE, ");
			sb.append("R").append(r).append("_EFFECTIVE_MATURITY_DATE, ");
			sb.append("R").append(r).append("_AMOUNT, ");
		}
		sb.append("R35_AMOUNT, ");
		int[] rows2 = { 40, 41, 42, 43, 44, 45, 46 };
		for (int r : rows2) {
			sb.append("R").append(r).append("_PRODUCT, ");
			sb.append("R").append(r).append("_ISSUANCE_DATE, ");
			sb.append("R").append(r).append("_CONTRACTUAL_MATURITY_DATE, ");
			sb.append("R").append(r).append("_EFFECTIVE_MATURITY_DATE, ");
			sb.append("R").append(r).append("_AMOUNT, ");
		}
		sb.append("R47_AMOUNT, ");
		sb.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		return sb.toString();
	}

	private static String buildMCA6Table2SelectList() {
		StringBuilder sb = new StringBuilder();
		int[] rows1 = { 28, 29, 30, 31, 32, 33, 34 };
		for (int r : rows1) {
			sb.append("S.R").append(r).append("_PRODUCT, ");
			sb.append("S.R").append(r).append("_ISSUANCE_DATE, ");
			sb.append("S.R").append(r).append("_CONTRACTUAL_MATURITY_DATE, ");
			sb.append("S.R").append(r).append("_EFFECTIVE_MATURITY_DATE, ");
			sb.append("S.R").append(r).append("_AMOUNT, ");
		}
		sb.append("S.R35_AMOUNT, ");
		int[] rows2 = { 40, 41, 42, 43, 44, 45, 46 };
		for (int r : rows2) {
			sb.append("S.R").append(r).append("_PRODUCT, ");
			sb.append("S.R").append(r).append("_ISSUANCE_DATE, ");
			sb.append("S.R").append(r).append("_CONTRACTUAL_MATURITY_DATE, ");
			sb.append("S.R").append(r).append("_EFFECTIVE_MATURITY_DATE, ");
			sb.append("S.R").append(r).append("_AMOUNT, ");
		}
		sb.append("S.R47_AMOUNT, ");
		sb.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		return sb.toString();
	}

// =========================================================
	// M_CA3 — Checker transfer (MOD → LIVE Summary + Detail)
	// =========================================================
	private void transferCA3Data(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_CA3_MOD_SUMMARYTABLE WHERE MODIFY_FLG = 'Y'";
		java.sql.Date reportDate = null;
		try {
			reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
		} catch (Exception e) {
			// fallback
		}
		if (reportDate == null) {
			reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM BRRS_M_CA3_MOD_SUMMARYTABLE",
					java.sql.Date.class);
		}

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_CA3 record available for verification.");
		}

		System.out.println("M_CA3 Report Date : " + reportDate);

		String columns = "R10_PRODUCT, R10_AMOUNT, R11_PRODUCT, R11_AMOUNT, R12_PRODUCT, R12_AMOUNT, "
				+ "R13_PRODUCT, R13_AMOUNT, R14_PRODUCT, R14_AMOUNT, R15_PRODUCT, R15_AMOUNT, "
				+ "R16_PRODUCT, R16_AMOUNT, R17_PRODUCT, R17_AMOUNT, R18_PRODUCT, R18_AMOUNT, "
				+ "R19_PRODUCT, R19_AMOUNT, R20_PRODUCT, R20_AMOUNT, "
				+ "R24_PRODUCT, R24_AMOUNT, R25_PRODUCT, R25_AMOUNT, R26_PRODUCT, R26_AMOUNT, "
				+ "R27_PRODUCT, R27_AMOUNT, R28_PRODUCT, R28_AMOUNT, R29_PRODUCT, R29_AMOUNT, "
				+ "R36_PRODUCT, R36_AMOUNT, R37_PRODUCT, R37_AMOUNT, R38_PRODUCT, R38_AMOUNT, "
				+ "R39_PRODUCT, R39_AMOUNT, R40_PRODUCT, R40_AMOUNT, R41_PRODUCT, R41_AMOUNT, "
				+ "R44_PRODUCT, R44_AMOUNT, R45_PRODUCT, R45_AMOUNT, R46_PRODUCT, R46_AMOUNT, "
				+ "R50_PRODUCT, R50_AMOUNT, R51_PRODUCT, R51_AMOUNT, R52_PRODUCT, R52_AMOUNT, "
				+ "R53_PRODUCT, R53_AMOUNT, R54_PRODUCT, R54_AMOUNT, R55_PRODUCT, R55_AMOUNT, "
				+ "R58_PRODUCT, R58_AMOUNT, R59_PRODUCT, R59_AMOUNT, R60_PRODUCT, R60_AMOUNT, "
				+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG";

		String selectColumns = "S.R10_PRODUCT, S.R10_AMOUNT, S.R11_PRODUCT, S.R11_AMOUNT, S.R12_PRODUCT, S.R12_AMOUNT, "
				+ "S.R13_PRODUCT, S.R13_AMOUNT, S.R14_PRODUCT, S.R14_AMOUNT, S.R15_PRODUCT, S.R15_AMOUNT, "
				+ "S.R16_PRODUCT, S.R16_AMOUNT, S.R17_PRODUCT, S.R17_AMOUNT, S.R18_PRODUCT, S.R18_AMOUNT, "
				+ "S.R19_PRODUCT, S.R19_AMOUNT, S.R20_PRODUCT, S.R20_AMOUNT, "
				+ "S.R24_PRODUCT, S.R24_AMOUNT, S.R25_PRODUCT, S.R25_AMOUNT, S.R26_PRODUCT, S.R26_AMOUNT, "
				+ "S.R27_PRODUCT, S.R27_AMOUNT, S.R28_PRODUCT, S.R28_AMOUNT, S.R29_PRODUCT, S.R29_AMOUNT, "
				+ "S.R36_PRODUCT, S.R36_AMOUNT, S.R37_PRODUCT, S.R37_AMOUNT, S.R38_PRODUCT, S.R38_AMOUNT, "
				+ "S.R39_PRODUCT, S.R39_AMOUNT, S.R40_PRODUCT, S.R40_AMOUNT, S.R41_PRODUCT, S.R41_AMOUNT, "
				+ "S.R44_PRODUCT, S.R44_AMOUNT, S.R45_PRODUCT, S.R45_AMOUNT, S.R46_PRODUCT, S.R46_AMOUNT, "
				+ "S.R50_PRODUCT, S.R50_AMOUNT, S.R51_PRODUCT, S.R51_AMOUNT, S.R52_PRODUCT, S.R52_AMOUNT, "
				+ "S.R53_PRODUCT, S.R53_AMOUNT, S.R54_PRODUCT, S.R54_AMOUNT, S.R55_PRODUCT, S.R55_AMOUNT, "
				+ "S.R58_PRODUCT, S.R58_AMOUNT, S.R59_PRODUCT, S.R59_AMOUNT, S.R60_PRODUCT, S.R60_AMOUNT, "
				+ "S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG";

		// 1. Update BRRS_M_CA3_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_CA3_SUMMARYTABLE M SET (" + columns + ") = (" + "    SELECT "
				+ selectColumns + " FROM BRRS_M_CA3_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);
		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_CA3 modification record not found for date : " + reportDate);
		}
		System.out.println("M_CA3 Summary Table Updated : " + summaryUpdated);

		// Ensure detail record exists for this date
		Integer detailCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM BRRS_M_CA3_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				new Object[] { reportDate }, Integer.class);
		if (detailCount == null || detailCount == 0) {
			jdbcTemplate.update(
					"INSERT INTO BRRS_M_CA3_DETAILTABLE (REPORT_DATE, ENTITY_FLG, MODIFY_FLG, DEL_FLG) VALUES (?, 'Y', 'N', 'N')",
					reportDate);
		}

		// 2. Update BRRS_M_CA3_DETAILTABLE from MOD table
		String updateDetailSql = "UPDATE BRRS_M_CA3_DETAILTABLE M SET (" + columns + ") = (" + "    SELECT "
				+ selectColumns + " FROM BRRS_M_CA3_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_CA3 Detail Table Updated : " + detailUpdated);

		// Update flags on Live Summary & Detail tables to approved status
		jdbcTemplate.update(
				"UPDATE BRRS_M_CA3_SUMMARYTABLE SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N', DEL_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				reportDate);
		jdbcTemplate.update(
				"UPDATE BRRS_M_CA3_DETAILTABLE SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N', DEL_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				reportDate);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_CA3_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_CA3 MOD Updated : " + modUpdated);
		System.out.println("M_CA3 Verification and Transfer Completed Successfully");
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

		// Build the (cols) = (SELECT cols FROM MOD) parts once — same for Summary +
		// Detail
		String columnList = buildSecLColumnList(); // "R12_PRODUCT, R12_EQUITY, …, R57_TOTAL, ENTITY_FLG, MODIFY_FLG,
													// DEL_FLG"
		String selectList = buildSecLSelectList(); // "S.R12_PRODUCT, S.R12_EQUITY, …, S.R57_TOTAL, S.ENTITY_FLG,
													// S.MODIFY_FLG, S.DEL_FLG"

		// 1. Update BRRS_M_SECL_SUMMARYTABLE from MOD
		String updateSummarySql = "UPDATE BRRS_M_SECL_SUMMARYTABLE M " + "SET (" + columnList + ") = (" + "    SELECT "
				+ selectList + "    FROM BRRS_M_SECL_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_SECL modification record not found for date : " + reportDate);
		}
		System.out.println("M_SECL Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_SECL_DETAILTABLE from MOD (identical column/select lists)
		String updateDetailSql = "UPDATE BRRS_M_SECL_DETAILTABLE M " + "SET (" + columnList + ") = (" + "    SELECT "
				+ selectList + "    FROM BRRS_M_SECL_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		if (detailUpdated == 0) {
			throw new IllegalStateException("M_SECL detail record not found for date : " + reportDate);
		}
		System.out.println("M_SECL Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD row as approved
		String updateModSql = "UPDATE BRRS_M_SECL_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' "
				+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_SECL MOD Updated : " + modUpdated);
		System.out.println("M_SECL Verification and Transfer Completed Successfully");
	}

	// =========================================================
	// M_SECA — Checker transfer (MOD → LIVE Summary + Detail)
	// =========================================================
	private void transferSECAData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SECA_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_SECA record available for verification.");
		}

		System.out.println("M_SECA Report Date : " + reportDate);

		// Build the (cols) = (SELECT cols FROM MOD) parts once — reused for Summary +
		// Detail
		String columnList = buildSecaColumnList();
		String selectList = buildSecaSelectList();

		// 1. Update BRRS_M_SECA_SUMMARYTABLE from MOD
		String updateSummarySql = "UPDATE BRRS_M_SECA_SUMMARYTABLE M " + "SET (" + columnList + ") = (" + "    SELECT "
				+ selectList + "    FROM BRRS_M_SECA_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_SECA modification record not found for date : " + reportDate);
		}
		System.out.println("M_SECA Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_SECA_DETAILTABLE from MOD (identical column/select lists)
		String updateDetailSql = "UPDATE BRRS_M_SECA_DETAILTABLE M " + "SET (" + columnList + ") = (" + "    SELECT "
				+ selectList + "    FROM BRRS_M_SECA_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		if (detailUpdated == 0) {
			throw new IllegalStateException("M_SECA detail record not found for date : " + reportDate);
		}
		System.out.println("M_SECA Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD row as approved
		String updateModSql = "UPDATE BRRS_M_SECA_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' "
				+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_SECA MOD Updated : " + modUpdated);
		System.out.println("M_SECA Verification and Transfer Completed Successfully");
	}

	// =========================================================
	// M_OR1 — Checker transfer (MOD → LIVE Summary + Detail)
	// =========================================================
	private void transferMOR1Data(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_OR1_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_OR1 record available for verification.");
		}

		System.out.println("M_OR1 Report Date : " + reportDate);

		String columnList = buildMOR1ColumnList();
		String selectList = buildMOR1SelectList();

		// 1. Update BRRS_M_OR1_SUMMARYTABLE from MOD
		String updateSummarySql = "UPDATE BRRS_M_OR1_SUMMARYTABLE M " + "SET (" + columnList + ") = (" + "    SELECT "
				+ selectList + "    FROM BRRS_M_OR1_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);
		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_OR1 modification record not found for date : " + reportDate);
		}
		System.out.println("M_OR1 Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_OR1_DETAILTABLE from MOD (if detail table exists)
		String updateDetailSql = "UPDATE BRRS_M_OR1_DETAILTABLE M " + "SET (" + columnList + ") = (" + "    SELECT "
				+ selectList + "    FROM BRRS_M_OR1_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_OR1 Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD row as approved
		String updateModSql = "UPDATE BRRS_M_OR1_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' "
				+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);
		System.out.println("M_OR1 MOD Updated : " + modUpdated);
		System.out.println("M_OR1 Verification and Transfer Completed Successfully");
	}

	// =========================================================
	// M_OR2 — Checker transfer (MOD → LIVE Summary only)
	// =========================================================
	private void transferMOR2Data(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_OR2_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_OR2 record available for verification.");
		}
		System.out.println("M_OR2 Report Date : " + reportDate);

		String cols = buildMOR2ColumnList();
		String sel = buildMOR2SelectList();

		String updateSummarySql = "UPDATE BRRS_M_OR2_SUMMARYTABLE M " + "SET (" + cols + ") = (" + "    SELECT " + sel
				+ "    FROM BRRS_M_OR2_MOD_SUMMARYTABLE S " + "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
				+ ") " + "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);
		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_OR2 modification record not found for date : " + reportDate);
		}
		System.out.println("M_OR2 Summary Table Updated : " + summaryUpdated);

		/*
		 * NOTE: No BRRS_M_OR2_DETAILTABLE exists for this report — summary-only,
		 * matching the OR2 data model. Detail-table update block intentionally removed.
		 */

		String updateModSql = "UPDATE BRRS_M_OR2_MOD_SUMMARYTABLE " + "SET ENTITY_FLG='Y', MODIFY_FLG='N' "
				+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);
		System.out.println("M_OR2 MOD Updated : " + modUpdated);
		System.out.println("M_OR2 Verification and Transfer Completed Successfully");
	}

	private static String buildMOR2ColumnList() {
		StringBuilder sb = new StringBuilder();
		// Block A — BUSINESS_LINE_MONTH rows only:
		appendMOR2BlockA(sb, false, 12, 23); // R12–R23
		appendMOR2BlockA(sb, false, 28, 39); // R28–R39 ← stop before R40
		appendMOR2BlockA(sb, false, 44, 55); // R44–R55 ← start after R43
		// Block B — PRODUCT + 8 value cols:
		appendMOR2BlockB(sb, false); // 24,25,26, 40,41,42, 56,57,58
		// Block C — PRODUCT + OPERATIONAL_RISK_CAP_CHARGE:
		appendMOR2BlockC(sb, false); // 27, 43, 59, 61, 62, 63
		// Block D — PRODUCT + RISK_WEIGHT_FACTOR:
		appendMOR2BlockD(sb, false); // 64, 65
		sb.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		return sb.toString();
	}

	private static String buildMOR2SelectList() {
		StringBuilder sb = new StringBuilder();
		appendMOR2BlockA(sb, true, 12, 23);
		appendMOR2BlockA(sb, true, 28, 39);
		appendMOR2BlockA(sb, true, 44, 55);
		appendMOR2BlockB(sb, true);
		appendMOR2BlockC(sb, true);
		appendMOR2BlockD(sb, true);
		sb.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		return sb.toString();
	}

	private static void appendMOR2BlockA(StringBuilder sb, boolean withS, int from, int to) {
		for (int i = from; i <= to; i++) {
			String p = (withS ? "S." : "") + "R" + i + "_";
			sb.append(p).append("BUSINESS_LINE_MONTH, ").append(p).append("CORPORATE_FINANCE, ").append(p)
					.append("TRADING_AND_SALES, ").append(p).append("RETAIL_BANKING, ").append(p)
					.append("COMMERCIAL_BANKING, ").append(p).append("PAYMENTS_AND_SETTLEMENTS, ").append(p)
					.append("AGENCY_SERVICES, ").append(p).append("ASSET_MANAGEMENT, ").append(p)
					.append("RETAIL_BROKERAGE, ");
		}
	}

	private static void appendMOR2BlockB(StringBuilder sb, boolean withS) {
		int[] rows = { 24, 25, 26, 40, 41, 42, 56, 57, 58 };
		for (int i : rows) {
			String p = (withS ? "S." : "") + "R" + i + "_";
			sb.append(p).append("PRODUCT, ").append(p).append("CORPORATE_FINANCE, ").append(p)
					.append("TRADING_AND_SALES, ").append(p).append("RETAIL_BANKING, ").append(p)
					.append("COMMERCIAL_BANKING, ").append(p).append("PAYMENTS_AND_SETTLEMENTS, ").append(p)
					.append("AGENCY_SERVICES, ").append(p).append("ASSET_MANAGEMENT, ").append(p)
					.append("RETAIL_BROKERAGE, ");
		}
	}

	private static void appendMOR2BlockC(StringBuilder sb, boolean withS) {
		int[] rows = { 27, 43, 59, 61, 62, 63 };
		for (int i : rows) {
			String p = (withS ? "S." : "") + "R" + i + "_";
			sb.append(p).append("PRODUCT, ").append(p).append("OPERATIONAL_RISK_CAP_CHARGE, ");
		}
	}

	private static void appendMOR2BlockD(StringBuilder sb, boolean withS) {
		int[] rows = { 64, 65 };
		for (int i : rows) {
			String p = (withS ? "S." : "") + "R" + i + "_";
			sb.append(p).append("PRODUCT, ").append(p).append("RISK_WEIGHT_FACTOR, ");
		}
	}

	private static String buildSecLColumnList() {
		StringBuilder sb = new StringBuilder();

		for (int row = 12; row <= 57; row++) {
			String p = "R" + row + "_";
			sb.append(p).append("PRODUCT, ").append(p).append("EQUITY, ").append(p).append("BONDS, ").append(p)
					.append("BOBCS, ").append(p).append("TREASURY_BILLS, ").append(p).append("REPURCHASE_AGREEMENTS, ")
					.append(p).append("COMMERCIAL_PAPER, ").append(p).append("CERTIFICATES_OF_DEPOSITS, ").append(p)
					.append("PLEDGED_ASSETS, ").append(p).append("OTHER_SPECIFY, ").append(p).append("TOTAL, ");
		}
		sb.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		return sb.toString();
	}

	private static String buildSecLSelectList() {
		StringBuilder sb = new StringBuilder();

		for (int row = 12; row <= 57; row++) {
			String p = "R" + row + "_";
			sb.append("S.").append(p).append("PRODUCT, ").append("S.").append(p).append("EQUITY, ").append("S.")
					.append(p).append("BONDS, ").append("S.").append(p).append("BOBCS, ").append("S.").append(p)
					.append("TREASURY_BILLS, ").append("S.").append(p).append("REPURCHASE_AGREEMENTS, ").append("S.")
					.append(p).append("COMMERCIAL_PAPER, ").append("S.").append(p).append("CERTIFICATES_OF_DEPOSITS, ")
					.append("S.").append(p).append("PLEDGED_ASSETS, ").append("S.").append(p).append("OTHER_SPECIFY, ")
					.append("S.").append(p).append("TOTAL, ");
		}
		sb.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		return sb.toString();
	}

	private static String buildSecaColumnList() {
		StringBuilder sb = new StringBuilder();

		for (int row = 13; row <= 59; row++) {
			String p = "R" + row + "_";
			sb.append(p).append("PRODUCT, ").append(p).append("EQUITY, ").append(p).append("BONDS, ").append(p)
					.append("BOBC, ").append(p).append("TRES_BILLS, ").append(p).append("REPURCHASE_AGREE, ").append(p)
					.append("COM_PAPER, ").append(p).append("CERT_OF_DEP, ").append(p).append("PLEDGED_ASSET, ")
					.append(p).append("OTHER, ").append(p).append("TOTAL, ");
		}
		sb.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		return sb.toString();
	}

	private static String buildSecaSelectList() {
		StringBuilder sb = new StringBuilder();

		for (int row = 13; row <= 59; row++) {
			String p = "R" + row + "_";
			sb.append("S.").append(p).append("PRODUCT, ").append("S.").append(p).append("EQUITY, ").append("S.")
					.append(p).append("BONDS, ").append("S.").append(p).append("BOBC, ").append("S.").append(p)
					.append("TRES_BILLS, ").append("S.").append(p).append("REPURCHASE_AGREE, ").append("S.").append(p)
					.append("COM_PAPER, ").append("S.").append(p).append("CERT_OF_DEP, ").append("S.").append(p)
					.append("PLEDGED_ASSET, ").append("S.").append(p).append("OTHER, ").append("S.").append(p)
					.append("TOTAL, ");
		}
		sb.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		return sb.toString();
	}

	private void transferMBopData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_BOP_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_BOP record available for verification.");
		}

		System.out.println("M_BOP Report Date : " + reportDate);

		// 1. Update BRRS_M_BOP_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_BOP_SUMMARYTABLE M " + "SET ("
				+ "    R13_PRODUCT, R13_OPEN_POSITION, R13_CPDM_DT_INC, R13_CPDM_DT_DEC, R13_NET, R13_CPDM_DT_DER, R13_CPDM_DT_DTO, R13_CP, "
				+ "    R14_PRODUCT, R14_OPEN_POSITION, R14_CPDM_DT_INC, R14_CPDM_DT_DEC, R14_NET, R14_CPDM_DT_DER, R14_CPDM_DT_DTO, R14_CP, "
				+ "    R15_PRODUCT, R15_OPEN_POSITION, R15_CPDM_DT_INC, R15_CPDM_DT_DEC, R15_NET, R15_CPDM_DT_DER, R15_CPDM_DT_DTO, R15_CP, "
				+ "    R16_PRODUCT, R16_OPEN_POSITION, R16_CPDM_DT_INC, R16_CPDM_DT_DEC, R16_NET, R16_CPDM_DT_DER, R16_CPDM_DT_DTO, R16_CP, "
				+ "    R17_PRODUCT, R17_OPEN_POSITION, R17_CPDM_DT_INC, R17_CPDM_DT_DEC, R17_NET, R17_CPDM_DT_DER, R17_CPDM_DT_DTO, R17_CP, "
				+ "    R18_PRODUCT, R18_OPEN_POSITION, R18_CPDM_DT_INC, R18_CPDM_DT_DEC, R18_NET, R18_CPDM_DT_DER, R18_CPDM_DT_DTO, R18_CP, "
				+ "    R19_PRODUCT, R19_OPEN_POSITION, R19_CPDM_DT_INC, R19_CPDM_DT_DEC, R19_NET, R19_CPDM_DT_DER, R19_CPDM_DT_DTO, R19_CP, "
				+ "    R20_PRODUCT, R20_OPEN_POSITION, R20_CPDM_DT_INC, R20_CPDM_DT_DEC, R20_NET, R20_CPDM_DT_DER, R20_CPDM_DT_DTO, R20_CP, "
				+ "    R21_PRODUCT, R21_OPEN_POSITION, R21_CPDM_DT_INC, R21_CPDM_DT_DEC, R21_NET, R21_CPDM_DT_DER, R21_CPDM_DT_DTO, R21_CP, "
				+ "    R22_PRODUCT, R22_OPEN_POSITION, R22_CPDM_DT_INC, R22_CPDM_DT_DEC, R22_NET, R22_CPDM_DT_DER, R22_CPDM_DT_DTO, R22_CP, "
				+ "    R23_PRODUCT, R23_OPEN_POSITION, R23_CPDM_DT_INC, R23_CPDM_DT_DEC, R23_NET, R23_CPDM_DT_DER, R23_CPDM_DT_DTO, R23_CP, "
				+ "    R24_PRODUCT, R24_OPEN_POSITION, R24_CPDM_DT_INC, R24_CPDM_DT_DEC, R24_NET, R24_CPDM_DT_DER, R24_CPDM_DT_DTO, R24_CP, "
				+ "    R25_PRODUCT, R25_OPEN_POSITION, R25_CPDM_DT_INC, R25_CPDM_DT_DEC, R25_NET, R25_CPDM_DT_DER, R25_CPDM_DT_DTO, R25_CP, "
				+ "    R26_PRODUCT, R26_OPEN_POSITION, R26_CPDM_DT_INC, R26_CPDM_DT_DEC, R26_NET, R26_CPDM_DT_DER, R26_CPDM_DT_DTO, R26_CP, "
				+ "    R27_PRODUCT, R27_OPEN_POSITION, R27_CPDM_DT_INC, R27_CPDM_DT_DEC, R27_NET, R27_CPDM_DT_DER, R27_CPDM_DT_DTO, R27_CP, "
				+ "    R28_PRODUCT, R28_OPEN_POSITION, R28_CPDM_DT_INC, R28_CPDM_DT_DEC, R28_NET, R28_CPDM_DT_DER, R28_CPDM_DT_DTO, R28_CP, "
				+ "    R29_PRODUCT, R29_OPEN_POSITION, R29_CPDM_DT_INC, R29_CPDM_DT_DEC, R29_NET, R29_CPDM_DT_DER, R29_CPDM_DT_DTO, R29_CP, "
				+ "    R30_PRODUCT, R30_OPEN_POSITION, R30_CPDM_DT_INC, R30_CPDM_DT_DEC, R30_NET, R30_CPDM_DT_DER, R30_CPDM_DT_DTO, R30_CP, "
				+ "    R31_PRODUCT, R31_OPEN_POSITION, R31_CPDM_DT_INC, R31_CPDM_DT_DEC, R31_NET, R31_CPDM_DT_DER, R31_CPDM_DT_DTO, R31_CP, "
				+ "    R32_PRODUCT, R32_OPEN_POSITION, R32_CPDM_DT_INC, R32_CPDM_DT_DEC, R32_NET, R32_CPDM_DT_DER, R32_CPDM_DT_DTO, R32_CP, "
				+ "    R33_PRODUCT, R33_OPEN_POSITION, R33_CPDM_DT_INC, R33_CPDM_DT_DEC, R33_NET, R33_CPDM_DT_DER, R33_CPDM_DT_DTO, R33_CP, "
				+ "    R34_PRODUCT, R34_OPEN_POSITION, R34_CPDM_DT_INC, R34_CPDM_DT_DEC, R34_NET, R34_CPDM_DT_DER, R34_CPDM_DT_DTO, R34_CP, "
				+ "    R35_PRODUCT, R35_OPEN_POSITION, R35_CPDM_DT_INC, R35_CPDM_DT_DEC, R35_NET, R35_CPDM_DT_DER, R35_CPDM_DT_DTO, R35_CP, "
				+ "    R36_PRODUCT, R36_OPEN_POSITION, R36_CPDM_DT_INC, R36_CPDM_DT_DEC, R36_NET, R36_CPDM_DT_DER, R36_CPDM_DT_DTO, R36_CP, "
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				+ "        S.R13_PRODUCT, S.R13_OPEN_POSITION, S.R13_CPDM_DT_INC, S.R13_CPDM_DT_DEC, S.R13_NET, S.R13_CPDM_DT_DER, S.R13_CPDM_DT_DTO, S.R13_CP, "
				+ "        S.R14_PRODUCT, S.R14_OPEN_POSITION, S.R14_CPDM_DT_INC, S.R14_CPDM_DT_DEC, S.R14_NET, S.R14_CPDM_DT_DER, S.R14_CPDM_DT_DTO, S.R14_CP, "
				+ "        S.R15_PRODUCT, S.R15_OPEN_POSITION, S.R15_CPDM_DT_INC, S.R15_CPDM_DT_DEC, S.R15_NET, S.R15_CPDM_DT_DER, S.R15_CPDM_DT_DTO, S.R15_CP, "
				+ "        S.R16_PRODUCT, S.R16_OPEN_POSITION, S.R16_CPDM_DT_INC, S.R16_CPDM_DT_DEC, S.R16_NET, S.R16_CPDM_DT_DER, S.R16_CPDM_DT_DTO, S.R16_CP, "
				+ "        S.R17_PRODUCT, S.R17_OPEN_POSITION, S.R17_CPDM_DT_INC, S.R17_CPDM_DT_DEC, S.R17_NET, S.R17_CPDM_DT_DER, S.R17_CPDM_DT_DTO, S.R17_CP, "
				+ "        S.R18_PRODUCT, S.R18_OPEN_POSITION, S.R18_CPDM_DT_INC, S.R18_CPDM_DT_DEC, S.R18_NET, S.R18_CPDM_DT_DER, S.R18_CPDM_DT_DTO, S.R18_CP, "
				+ "        S.R19_PRODUCT, S.R19_OPEN_POSITION, S.R19_CPDM_DT_INC, S.R19_CPDM_DT_DEC, S.R19_NET, S.R19_CPDM_DT_DER, S.R19_CPDM_DT_DTO, S.R19_CP, "
				+ "        S.R20_PRODUCT, S.R20_OPEN_POSITION, S.R20_CPDM_DT_INC, S.R20_CPDM_DT_DEC, S.R20_NET, S.R20_CPDM_DT_DER, S.R20_CPDM_DT_DTO, S.R20_CP, "
				+ "        S.R21_PRODUCT, S.R21_OPEN_POSITION, S.R21_CPDM_DT_INC, S.R21_CPDM_DT_DEC, S.R21_NET, S.R21_CPDM_DT_DER, S.R21_CPDM_DT_DTO, S.R21_CP, "
				+ "        S.R22_PRODUCT, S.R22_OPEN_POSITION, S.R22_CPDM_DT_INC, S.R22_CPDM_DT_DEC, S.R22_NET, S.R22_CPDM_DT_DER, S.R22_CPDM_DT_DTO, S.R22_CP, "
				+ "        S.R23_PRODUCT, S.R23_OPEN_POSITION, S.R23_CPDM_DT_INC, S.R23_CPDM_DT_DEC, S.R23_NET, S.R23_CPDM_DT_DER, S.R23_CPDM_DT_DTO, S.R23_CP, "
				+ "        S.R24_PRODUCT, S.R24_OPEN_POSITION, S.R24_CPDM_DT_INC, S.R24_CPDM_DT_DEC, S.R24_NET, S.R24_CPDM_DT_DER, S.R24_CPDM_DT_DTO, S.R24_CP, "
				+ "        S.R25_PRODUCT, S.R25_OPEN_POSITION, S.R25_CPDM_DT_INC, S.R25_CPDM_DT_DEC, S.R25_NET, S.R25_CPDM_DT_DER, S.R25_CPDM_DT_DTO, S.R25_CP, "
				+ "        S.R26_PRODUCT, S.R26_OPEN_POSITION, S.R26_CPDM_DT_INC, S.R26_CPDM_DT_DEC, S.R26_NET, S.R26_CPDM_DT_DER, S.R26_CPDM_DT_DTO, S.R26_CP, "
				+ "        S.R27_PRODUCT, S.R27_OPEN_POSITION, S.R27_CPDM_DT_INC, S.R27_CPDM_DT_DEC, S.R27_NET, S.R27_CPDM_DT_DER, S.R27_CPDM_DT_DTO, S.R27_CP, "
				+ "        S.R28_PRODUCT, S.R28_OPEN_POSITION, S.R28_CPDM_DT_INC, S.R28_CPDM_DT_DEC, S.R28_NET, S.R28_CPDM_DT_DER, S.R28_CPDM_DT_DTO, S.R28_CP, "
				+ "        S.R29_PRODUCT, S.R29_OPEN_POSITION, S.R29_CPDM_DT_INC, S.R29_CPDM_DT_DEC, S.R29_NET, S.R29_CPDM_DT_DER, S.R29_CPDM_DT_DTO, S.R29_CP, "
				+ "        S.R30_PRODUCT, S.R30_OPEN_POSITION, S.R30_CPDM_DT_INC, S.R30_CPDM_DT_DEC, S.R30_NET, S.R30_CPDM_DT_DER, S.R30_CPDM_DT_DTO, S.R30_CP, "
				+ "        S.R31_PRODUCT, S.R31_OPEN_POSITION, S.R31_CPDM_DT_INC, S.R31_CPDM_DT_DEC, S.R31_NET, S.R31_CPDM_DT_DER, S.R31_CPDM_DT_DTO, S.R31_CP, "
				+ "        S.R32_PRODUCT, S.R32_OPEN_POSITION, S.R32_CPDM_DT_INC, S.R32_CPDM_DT_DEC, S.R32_NET, S.R32_CPDM_DT_DER, S.R32_CPDM_DT_DTO, S.R32_CP, "
				+ "        S.R33_PRODUCT, S.R33_OPEN_POSITION, S.R33_CPDM_DT_INC, S.R33_CPDM_DT_DEC, S.R33_NET, S.R33_CPDM_DT_DER, S.R33_CPDM_DT_DTO, S.R33_CP, "
				+ "        S.R34_PRODUCT, S.R34_OPEN_POSITION, S.R34_CPDM_DT_INC, S.R34_CPDM_DT_DEC, S.R34_NET, S.R34_CPDM_DT_DER, S.R34_CPDM_DT_DTO, S.R34_CP, "
				+ "        S.R35_PRODUCT, S.R35_OPEN_POSITION, S.R35_CPDM_DT_INC, S.R35_CPDM_DT_DEC, S.R35_NET, S.R35_CPDM_DT_DER, S.R35_CPDM_DT_DTO, S.R35_CP, "
				+ "        S.R36_PRODUCT, S.R36_OPEN_POSITION, S.R36_CPDM_DT_INC, S.R36_CPDM_DT_DEC, S.R36_NET, S.R36_CPDM_DT_DER, S.R36_CPDM_DT_DTO, S.R36_CP, "
				+ "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_BOP_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_BOP modification record not found for date : " + reportDate);
		}

		System.out.println("M_BOP Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_BOP_DETAILTABLE from MOD table
		String updateDetailSql = updateSummarySql.replace("BRRS_M_BOP_SUMMARYTABLE", "BRRS_M_BOP_DETAILTABLE");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		System.out.println("M_BOP Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_BOP_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_BOP MOD Updated : " + modUpdated);
		System.out.println("M_BOP Verification and Transfer Completed Successfully");
	}

	private void transferMObData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_OB_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_OB record available for verification.");
		}

		System.out.println("M_OB Report Date : " + reportDate);

		// 1. Update BRRS_M_OB_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_OB_SUMMARYTABLE M " + "SET ("
				+ "    R11_OTHER_BORROW, R12_OTHER_BORROW, R13_OTHER_BORROW, R14_OTHER_BORROW, R15_OTHER_BORROW, "
				+ "    R16_OTHER_BORROW, R17_OTHER_BORROW, R18_OTHER_BORROW, R19_OTHER_BORROW, R20_OTHER_BORROW, "
				+ "    R21_OTHER_BORROW, R22_OTHER_BORROW, R23_OTHER_BORROW, R24_OTHER_BORROW, R25_OTHER_BORROW, "
				+ "    R26_OTHER_BORROW, R27_OTHER_BORROW, R28_OTHER_BORROW, R29_OTHER_BORROW, R30_OTHER_BORROW, "
				+ "    R31_OTHER_BORROW, R32_OTHER_BORROW, R33_OTHER_BORROW, R34_OTHER_BORROW, R35_OTHER_BORROW, "
				+ "    R36_OTHER_BORROW, R37_OTHER_BORROW, R38_OTHER_BORROW, R39_OTHER_BORROW, R40_OTHER_BORROW, "
				+ "    R41_OTHER_BORROW, R42_OTHER_BORROW, R43_OTHER_BORROW, R44_OTHER_BORROW, R45_OTHER_BORROW, "
				+ "    R46_OTHER_BORROW, R47_OTHER_BORROW, R48_OTHER_BORROW, R49_OTHER_BORROW, R50_OTHER_BORROW, "
				+ "    R51_OTHER_BORROW, R52_OTHER_BORROW, R53_OTHER_BORROW, R54_OTHER_BORROW, R55_OTHER_BORROW, "
				+ "    R56_OTHER_BORROW, R57_OTHER_BORROW, R58_OTHER_BORROW, R59_OTHER_BORROW, R60_OTHER_BORROW, "
				+ "    R61_OTHER_BORROW, R62_OTHER_BORROW, R63_OTHER_BORROW, R64_OTHER_BORROW, "
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				+ "        S.R11_OTHER_BORROW, S.R12_OTHER_BORROW, S.R13_OTHER_BORROW, S.R14_OTHER_BORROW, S.R15_OTHER_BORROW, "
				+ "        S.R16_OTHER_BORROW, S.R17_OTHER_BORROW, S.R18_OTHER_BORROW, S.R19_OTHER_BORROW, S.R20_OTHER_BORROW, "
				+ "        S.R21_OTHER_BORROW, S.R22_OTHER_BORROW, S.R23_OTHER_BORROW, S.R24_OTHER_BORROW, S.R25_OTHER_BORROW, "
				+ "        S.R26_OTHER_BORROW, S.R27_OTHER_BORROW, S.R28_OTHER_BORROW, S.R29_OTHER_BORROW, S.R30_OTHER_BORROW, "
				+ "        S.R31_OTHER_BORROW, S.R32_OTHER_BORROW, S.R33_OTHER_BORROW, S.R34_OTHER_BORROW, S.R35_OTHER_BORROW, "
				+ "        S.R36_OTHER_BORROW, S.R37_OTHER_BORROW, S.R38_OTHER_BORROW, S.R39_OTHER_BORROW, S.R40_OTHER_BORROW, "
				+ "        S.R41_OTHER_BORROW, S.R42_OTHER_BORROW, S.R43_OTHER_BORROW, S.R44_OTHER_BORROW, S.R45_OTHER_BORROW, "
				+ "        S.R46_OTHER_BORROW, S.R47_OTHER_BORROW, S.R48_OTHER_BORROW, S.R49_OTHER_BORROW, S.R50_OTHER_BORROW, "
				+ "        S.R51_OTHER_BORROW, S.R52_OTHER_BORROW, S.R53_OTHER_BORROW, S.R54_OTHER_BORROW, S.R55_OTHER_BORROW, "
				+ "        S.R56_OTHER_BORROW, S.R57_OTHER_BORROW, S.R58_OTHER_BORROW, S.R59_OTHER_BORROW, S.R60_OTHER_BORROW, "
				+ "        S.R61_OTHER_BORROW, S.R62_OTHER_BORROW, S.R63_OTHER_BORROW, S.R64_OTHER_BORROW, "
				+ "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_OB_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_OB modification record not found for date : " + reportDate);
		}

		System.out.println("M_OB Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_OB_DETAILTABLE from MOD table
		String updateDetailSql = updateSummarySql.replace("BRRS_M_OB_SUMMARYTABLE", "BRRS_M_OB_DETAILTABLE");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		System.out.println("M_OB Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_OB_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_OB MOD Updated : " + modUpdated);
		System.out.println("M_OB Verification and Transfer Completed Successfully");
	}

	private static String buildMOR1ColumnList() {
		StringBuilder sb = new StringBuilder();
		for (int row = 10; row <= 56; row++) {
			String p = "R" + row + "_";
			sb.append(p).append("PRODUCT, ").append(p).append("MONTH, ").append(p).append("GROSS_INCOME, ").append(p)
					.append("AGGREGATE_GROSS_INCOME, ").append(p).append("RISK_WEIGHT_FACTOR, ");
		}
		sb.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		return sb.toString();
	}

	private static String buildMOR1SelectList() {
		StringBuilder sb = new StringBuilder();
		for (int row = 10; row <= 56; row++) {
			String p = "R" + row + "_";
			sb.append("S.").append(p).append("PRODUCT, ").append("S.").append(p).append("MONTH, ").append("S.")
					.append(p).append("GROSS_INCOME, ").append("S.").append(p).append("AGGREGATE_GROSS_INCOME, ")
					.append("S.").append(p).append("RISK_WEIGHT_FACTOR, ");
		}
		sb.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		return sb.toString();
	}

	private static String buildMSRWA12FColumnList() {
		StringBuilder sb = new StringBuilder();
		for (int row = 11; row <= 37; row++) {
			String p = "R" + row + "_";
			sb.append(p).append("NAME_OF_CORPORATE, ").append(p).append("CREDIT_RATING, ").append(p)
					.append("RATING_AGENCY, ").append(p).append("EXPOSURE_AMT, ").append(p).append("RISK_WEIGHT, ")
					.append(p).append("RISK_WEIGHTED_AMT, ");
		}
		sb.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		return sb.toString();
	}

	private static String buildMSRWA12FSelectList() {
		StringBuilder sb = new StringBuilder();
		for (int row = 11; row <= 37; row++) {
			String p = "R" + row + "_";
			sb.append("S.").append(p).append("NAME_OF_CORPORATE, ").append("S.").append(p).append("CREDIT_RATING, ")
					.append("S.").append(p).append("RATING_AGENCY, ").append("S.").append(p).append("EXPOSURE_AMT, ")
					.append("S.").append(p).append("RISK_WEIGHT, ").append("S.").append(p)
					.append("RISK_WEIGHTED_AMT, ");
		}
		sb.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");
		return sb.toString();
	}

	private void transferMLA2Data(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_LA2_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_LA2 record available for verification.");
		}

		System.out.println("M_LA2 Report Date : " + reportDate);

		// 1. Update BRRS_M_LA2_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_LA2_SUMMARYTABLE M " + "SET (" + "     R12_INDUSTRY, R12_TOTAL,"
				+ "     R13_INDUSTRY, R13_TOTAL," + "     R14_INDUSTRY, R14_TOTAL," + "     R15_INDUSTRY, R15_TOTAL,"
				+ "     R16_INDUSTRY, R16_TOTAL," + "     R17_INDUSTRY, R17_TOTAL," + "     R18_INDUSTRY, R18_TOTAL,"
				+ "     R19_INDUSTRY, R19_TOTAL," + "    R20_INDUSTRY, R20_TOTAL," + "    R21_INDUSTRY, R21_TOTAL,"
				+ "    R22_INDUSTRY, R22_TOTAL," + "    R23_INDUSTRY, R23_TOTAL," + "    R24_INDUSTRY, R24_TOTAL,"
				+ "    R25_INDUSTRY, R25_TOTAL," + "    R26_INDUSTRY, R26_TOTAL,"
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT " + "     S.R12_INDUSTRY, S.R12_TOTAL,"
				+ "     S.R13_INDUSTRY, S.R13_TOTAL," + "     S.R14_INDUSTRY, S.R14_TOTAL,"
				+ "     S.R15_INDUSTRY, S.R15_TOTAL," + "     S.R16_INDUSTRY, S.R16_TOTAL,"
				+ "     S.R17_INDUSTRY, S.R17_TOTAL," + "     S.R18_INDUSTRY, S.R18_TOTAL,"
				+ "     S.R19_INDUSTRY, S.R19_TOTAL," + "    S.R20_INDUSTRY, S.R20_TOTAL,"
				+ "    S.R21_INDUSTRY, S.R21_TOTAL," + "    S.R22_INDUSTRY, S.R22_TOTAL,"
				+ "    S.R23_INDUSTRY, S.R23_TOTAL," + "    S.R24_INDUSTRY, S.R24_TOTAL,"
				+ "    S.R25_INDUSTRY, S.R25_TOTAL," + "    S.R26_INDUSTRY, S.R26_TOTAL,"
				+ "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_LA2_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_LA2 modification record not found for date : " + reportDate);
		}

		System.out.println("M_LA2 Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_LA2_DETAILTABLE from MOD table (Same column mapping)
		String updateDetailSql = "UPDATE BRRS_M_LA2_DETAILTABLE M " + "SET (" + "     R12_INDUSTRY, R12_TOTAL,"
				+ "     R13_INDUSTRY, R13_TOTAL," + "     R14_INDUSTRY, R14_TOTAL," + "     R15_INDUSTRY, R15_TOTAL,"
				+ "     R16_INDUSTRY, R16_TOTAL," + "     R17_INDUSTRY, R17_TOTAL," + "     R18_INDUSTRY, R18_TOTAL,"
				+ "     R19_INDUSTRY, R19_TOTAL," + "    R20_INDUSTRY, R20_TOTAL," + "    R21_INDUSTRY, R21_TOTAL,"
				+ "    R22_INDUSTRY, R22_TOTAL," + "    R23_INDUSTRY, R23_TOTAL," + "    R24_INDUSTRY, R24_TOTAL,"
				+ "    R25_INDUSTRY, R25_TOTAL," + "    R26_INDUSTRY, R26_TOTAL,"
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT " + "     S.R12_INDUSTRY, S.R12_TOTAL,"
				+ "     S.R13_INDUSTRY, S.R13_TOTAL," + "     S.R14_INDUSTRY, S.R14_TOTAL,"
				+ "     S.R15_INDUSTRY, S.R15_TOTAL," + "     S.R16_INDUSTRY, S.R16_TOTAL,"
				+ "     S.R17_INDUSTRY, S.R17_TOTAL," + "     S.R18_INDUSTRY, S.R18_TOTAL,"
				+ "     S.R19_INDUSTRY, S.R19_TOTAL," + "    S.R20_INDUSTRY, S.R20_TOTAL,"
				+ "    S.R21_INDUSTRY, S.R21_TOTAL," + "    S.R22_INDUSTRY, S.R22_TOTAL,"
				+ "    S.R23_INDUSTRY, S.R23_TOTAL," + "    S.R24_INDUSTRY, S.R24_TOTAL,"
				+ "    S.R25_INDUSTRY, S.R25_TOTAL," + "    S.R26_INDUSTRY, S.R26_TOTAL,"
				+ "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_LA2_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_LA2 Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_LA2_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_LA2 MOD Updated : " + modUpdated);
		System.out.println("M_LA2 Verification and Transfer Completed Successfully");
	}

	private void transferQRlfa1Data(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_Q_RLFA1_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified Q_RLFA1 record available for verification.");
		}

		System.out.println("Q_RLFA1 Report Date : " + reportDate);

		// Build column list dynamically or explicitly for rows 10 to 63
		StringBuilder setColumns = new StringBuilder();
		StringBuilder selectColumns = new StringBuilder();
		String[] fields = { "rene_loans", "collateral_amount", "carrying_amount", "no_of_accts" };

		for (int i = 10; i <= 63; i++) {
			for (String field : fields) {
				String col = "R" + i + "_" + field;
				setColumns.append(col).append(", ");
				selectColumns.append("S.").append(col).append(", ");
			}
		}
		setColumns.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		selectColumns.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		// 1. Update BRRS_Q_RLFA1_SUMMARY_TABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_Q_RLFA1_SUMMARY_TABLE M SET (" + setColumns.toString() + ") = ("
				+ "    SELECT " + selectColumns.toString() + " FROM BRRS_Q_RLFA1_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("Q_RLFA1 modification record not found for date : " + reportDate);
		}

		System.out.println("Q_RLFA1 Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_Q_RLFA1_DETAIL_TABLE from MOD table
		String updateDetailSql = updateSummarySql.replace("BRRS_Q_RLFA1_SUMMARY_TABLE", "BRRS_Q_RLFA1_DETAIL_TABLE");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		System.out.println("Q_RLFA1 Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_Q_RLFA1_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("Q_RLFA1 MOD Updated : " + modUpdated);
		System.out.println("Q_RLFA1 Verification and Transfer Completed Successfully");
	}

	private void transferQRlfa2Data(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_Q_RLFA2_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified Q_RLFA2 record available for verification.");
		}

		System.out.println("Q_RLFA2 Report Date : " + reportDate);

		// Build column lists dynamically for rows 10 to 64
		StringBuilder setColumns = new StringBuilder();
		StringBuilder selectColumns = new StringBuilder();
		String[] fields = { "sche_fore_ass", "orig_amt", "fore_amt", "no_of_acc" };

		for (int i = 10; i <= 64; i++) {
			for (String field : fields) {
				String col = "R" + i + "_" + field;
				setColumns.append(col).append(", ");
				selectColumns.append("S.").append(col).append(", ");
			}
		}
		setColumns.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		selectColumns.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		// 1. Update BRRS_Q_RLFA2_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_Q_RLFA2_SUMMARYTABLE M SET (" + setColumns.toString() + ") = ("
				+ "    SELECT " + selectColumns.toString() + " FROM BRRS_Q_RLFA2_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("Q_RLFA2 modification record not found for date : " + reportDate);
		}

		System.out.println("Q_RLFA2 Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_Q_RLFA2_DETAILTABLE from MOD table
		String updateDetailSql = updateSummarySql.replace("BRRS_Q_RLFA2_SUMMARYTABLE", "BRRS_Q_RLFA2_DETAILTABLE");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		System.out.println("Q_RLFA2 Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_Q_RLFA2_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("Q_RLFA2 MOD Updated : " + modUpdated);
		System.out.println("Q_RLFA2 Verification and Transfer Completed Successfully");
	}

	private void transferMGpData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_GP_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_GP record available for verification.");
		}

		System.out.println("M_GP Report Date : " + reportDate);

		// Build column lists dynamically for rows 11 to 64
		StringBuilder setColumns = new StringBuilder();
		StringBuilder selectColumns = new StringBuilder();
		String[] fields = { "PRODUCT", "STAGE1_PROVISIONS", "QUALIFY_STAGE2_PROVISIONS", "TOTAL_GENERAL_PROVISIONS" };

		for (int i = 11; i <= 64; i++) {
			for (String field : fields) {
				String col = "R" + i + "_" + field;
				setColumns.append(col).append(", ");
				selectColumns.append("S.").append(col).append(", ");
			}
		}
		setColumns.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		selectColumns.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		// 1. Update BRRS_M_GP_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_GP_SUMMARYTABLE M SET (" + setColumns.toString() + ") = ("
				+ "    SELECT " + selectColumns.toString() + " FROM BRRS_M_GP_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_GP modification record not found for date : " + reportDate);
		}

		System.out.println("M_GP Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_GP_DETAILTABLE from MOD table
		String updateDetailSql = updateSummarySql.replace("BRRS_M_GP_SUMMARYTABLE", "BRRS_M_GP_DETAILTABLE");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		System.out.println("M_GP Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_GP_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_GP MOD Updated : " + modUpdated);
		System.out.println("M_GP Verification and Transfer Completed Successfully");
	}

	private void transferMTBSData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_TBS_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_TBS record available for verification.");
		}

		System.out.println("M_TBS Report Date : " + reportDate);

		// Build column lists dynamically for rows 11 to 55
		StringBuilder setColumns = new StringBuilder();
		StringBuilder selectColumns = new StringBuilder();
		String[] fields = { "NV_LONG", "NV_SHORT", "FV_LONG", "FV_SHORT", "QFHA" };
		int[] targetRows = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33,
				34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55 };

		for (int i : targetRows) {
			for (String field : fields) {
				String col = "R" + i + "_" + field;
				setColumns.append(col).append(", ");
				selectColumns.append("S.").append(col).append(", ");
			}
		}
		setColumns.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		selectColumns.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		// 1. Update BRRS_M_TBS_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_TBS_SUMMARYTABLE M SET (" + setColumns.toString() + ") = ("
				+ "    SELECT " + selectColumns.toString() + " FROM BRRS_M_TBS_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_TBS modification record not found for date : " + reportDate);
		}

		System.out.println("M_TBS Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_TBS_DETAILTABLE from MOD table
		String updateDetailSql = updateSummarySql.replace("BRRS_M_TBS_SUMMARYTABLE", "BRRS_M_TBS_DETAILTABLE");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		System.out.println("M_TBS Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_TBS_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_TBS MOD Updated : " + modUpdated);
		System.out.println("M_TBS Verification and Transfer Completed Successfully");
	}

	private void transferMINTRATESData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_INT_RATES_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_INT_RATES record available for verification.");
		}

		System.out.println("M_INT_RATES Report Date : " + reportDate);

		// Build column lists dynamically for rows 11 to 42
		StringBuilder setColumns = new StringBuilder();
		StringBuilder selectColumns = new StringBuilder();
		String[] fields = { "LENDING", "NOMINAL_INTEREST_RATE", "AVG_EFFECTIVE_RATE", "VOLUME" };

		for (int i = 11; i <= 42; i++) {
			for (String field : fields) {
				String col = "R" + i + "_" + field;
				setColumns.append(col).append(", ");
				selectColumns.append("S.").append(col).append(", ");
			}
		}
		setColumns.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		selectColumns.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		// 1. Update BRRS_M_INT_RATES_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_INT_RATES_SUMMARYTABLE M SET (" + setColumns.toString() + ") = ("
				+ "    SELECT " + selectColumns.toString() + " FROM BRRS_M_INT_RATES_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_INT_RATES modification record not found for date : " + reportDate);
		}

		System.out.println("M_INT_RATES Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_INT_RATES_DETAILTABLE from MOD table
		String updateDetailSql = updateSummarySql.replace("BRRS_M_INT_RATES_SUMMARYTABLE",
				"BRRS_M_INT_RATES_DETAILTABLE");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		System.out.println("M_INT_RATES Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_INT_RATES_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_INT_RATES MOD Updated : " + modUpdated);
		System.out.println("M_INT_RATES Verification and Transfer Completed Successfully");
	}

	private void transferMFXRData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_FXR_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_FXR record available for verification.");
		}

		System.out.println("M_FXR Report Date : " + reportDate);

		// 1. Update BRRS_M_FXR_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_FXR_SUMMARYTABLE M " + "SET ("
				+ "    R11_NET_SPOT_POSITION, R11_NET_FORWARD_POSITION, R11_GUARANTEES, R11_NET_FUTURE_INC_OR_EXP, R11_NET_DELTA_WEI_FX_OPT_POSI, R11_OTHER_ITEMS, R11_NET_LONG_POSITION, R11_OR, R11_NET_SHORT_POSITION, "
				+ "    R12_NET_SPOT_POSITION, R12_NET_FORWARD_POSITION, R12_GUARANTEES, R12_NET_FUTURE_INC_OR_EXP, R12_NET_DELTA_WEI_FX_OPT_POSI, R12_OTHER_ITEMS, R12_NET_LONG_POSITION, R12_OR, R12_NET_SHORT_POSITION, "
				+ "    R13_NET_SPOT_POSITION, R13_NET_FORWARD_POSITION, R13_GUARANTEES, R13_NET_FUTURE_INC_OR_EXP, R13_NET_DELTA_WEI_FX_OPT_POSI, R13_OTHER_ITEMS, R13_NET_LONG_POSITION, R13_OR, R13_NET_SHORT_POSITION, "
				+ "    R14_NET_SPOT_POSITION, R14_NET_FORWARD_POSITION, R14_GUARANTEES, R14_NET_FUTURE_INC_OR_EXP, R14_NET_DELTA_WEI_FX_OPT_POSI, R14_OTHER_ITEMS, R14_NET_LONG_POSITION, R14_OR, R14_NET_SHORT_POSITION, "
				+ "    R15_NET_SPOT_POSITION, R15_NET_FORWARD_POSITION, R15_GUARANTEES, R15_NET_FUTURE_INC_OR_EXP, R15_NET_DELTA_WEI_FX_OPT_POSI, R15_OTHER_ITEMS, R15_NET_LONG_POSITION, R15_OR, R15_NET_SHORT_POSITION, "
				+ "    R16_NET_SPOT_POSITION, R16_NET_FORWARD_POSITION, R16_GUARANTEES, R16_NET_FUTURE_INC_OR_EXP, R16_NET_DELTA_WEI_FX_OPT_POSI, R16_OTHER_ITEMS, R16_NET_LONG_POSITION, R16_OR, R16_NET_SHORT_POSITION, "
				+ "    R17_NET_LONG_POSITION, R17_NET_SHORT_POSITION, "
				+ "    R21_LONG, R21_SHORT, R21_TOTAL_GROSS_LONG_SHORT, R21_NET_POSITION, "
				+ "    R22_LONG, R22_SHORT, R22_TOTAL_GROSS_LONG_SHORT, R22_NET_POSITION, " + "    R23_NET_POSITION, "
				+ "    R29_GREATER_NET_LONG_OR_SHORT, R29_ABS_VALUE_NET_GOLD_POSI, R29_CAPITAL_CHARGE, "
				+ "    R30_CAPITAL_REQUIRE, " + "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				+ "        S.R11_NET_SPOT_POSITION, S.R11_NET_FORWARD_POSITION, S.R11_GUARANTEES, S.R11_NET_FUTURE_INC_OR_EXP, S.R11_NET_DELTA_WEI_FX_OPT_POSI, S.R11_OTHER_ITEMS, S.R11_NET_LONG_POSITION, S.R11_OR, S.R11_NET_SHORT_POSITION, "
				+ "        S.R12_NET_SPOT_POSITION, S.R12_NET_FORWARD_POSITION, S.R12_GUARANTEES, S.R12_NET_FUTURE_INC_OR_EXP, S.R12_NET_DELTA_WEI_FX_OPT_POSI, S.R12_OTHER_ITEMS, S.R12_NET_LONG_POSITION, S.R12_OR, S.R12_NET_SHORT_POSITION, "
				+ "        S.R13_NET_SPOT_POSITION, S.R13_NET_FORWARD_POSITION, S.R13_GUARANTEES, S.R13_NET_FUTURE_INC_OR_EXP, S.R13_NET_DELTA_WEI_FX_OPT_POSI, S.R13_OTHER_ITEMS, S.R13_NET_LONG_POSITION, S.R13_OR, S.R13_NET_SHORT_POSITION, "
				+ "        S.R14_NET_SPOT_POSITION, S.R14_NET_FORWARD_POSITION, S.R14_GUARANTEES, S.R14_NET_FUTURE_INC_OR_EXP, S.R14_NET_DELTA_WEI_FX_OPT_POSI, S.R14_OTHER_ITEMS, S.R14_NET_LONG_POSITION, S.R14_OR, S.R14_NET_SHORT_POSITION, "
				+ "        S.R15_NET_SPOT_POSITION, S.R15_NET_FORWARD_POSITION, S.R15_GUARANTEES, S.R15_NET_FUTURE_INC_OR_EXP, S.R15_NET_DELTA_WEI_FX_OPT_POSI, S.R15_OTHER_ITEMS, S.R15_NET_LONG_POSITION, S.R15_OR, S.R15_NET_SHORT_POSITION, "
				+ "        S.R16_NET_SPOT_POSITION, S.R16_NET_FORWARD_POSITION, S.R16_GUARANTEES, S.R16_NET_FUTURE_INC_OR_EXP, S.R16_NET_DELTA_WEI_FX_OPT_POSI, S.R16_OTHER_ITEMS, S.R16_NET_LONG_POSITION, S.R16_OR, S.R16_NET_SHORT_POSITION, "
				+ "        S.R17_NET_LONG_POSITION, S.R17_NET_SHORT_POSITION, "
				+ "        S.R21_LONG, S.R21_SHORT, S.R21_TOTAL_GROSS_LONG_SHORT, S.R21_NET_POSITION, "
				+ "        S.R22_LONG, S.R22_SHORT, S.R22_TOTAL_GROSS_LONG_SHORT, S.R22_NET_POSITION, "
				+ "        S.R23_NET_POSITION, "
				+ "        S.R29_GREATER_NET_LONG_OR_SHORT, S.R29_ABS_VALUE_NET_GOLD_POSI, S.R29_CAPITAL_CHARGE, "
				+ "        S.R30_CAPITAL_REQUIRE, " + "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG "
				+ "    FROM BRRS_M_FXR_MOD_SUMMARYTABLE S " + "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
				+ ") " + "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_FXR modification record not found for date : " + reportDate);
		}

		System.out.println("M_FXR Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_FXR_DETAILTABLE from MOD table (Same column mapping)
		String updateDetailSql = "UPDATE BRRS_M_FXR_DETAILTABLE M " + "SET ("
				+ "    R11_NET_SPOT_POSITION, R11_NET_FORWARD_POSITION, R11_GUARANTEES, R11_NET_FUTURE_INC_OR_EXP, R11_NET_DELTA_WEI_FX_OPT_POSI, R11_OTHER_ITEMS, R11_NET_LONG_POSITION, R11_OR, R11_NET_SHORT_POSITION, "
				+ "    R12_NET_SPOT_POSITION, R12_NET_FORWARD_POSITION, R12_GUARANTEES, R12_NET_FUTURE_INC_OR_EXP, R12_NET_DELTA_WEI_FX_OPT_POSI, R12_OTHER_ITEMS, R12_NET_LONG_POSITION, R12_OR, R12_NET_SHORT_POSITION, "
				+ "    R13_NET_SPOT_POSITION, R13_NET_FORWARD_POSITION, R13_GUARANTEES, R13_NET_FUTURE_INC_OR_EXP, R13_NET_DELTA_WEI_FX_OPT_POSI, R13_OTHER_ITEMS, R13_NET_LONG_POSITION, R13_OR, R13_NET_SHORT_POSITION, "
				+ "    R14_NET_SPOT_POSITION, R14_NET_FORWARD_POSITION, R14_GUARANTEES, R14_NET_FUTURE_INC_OR_EXP, R14_NET_DELTA_WEI_FX_OPT_POSI, R14_OTHER_ITEMS, R14_NET_LONG_POSITION, R14_OR, R14_NET_SHORT_POSITION, "
				+ "    R15_NET_SPOT_POSITION, R15_NET_FORWARD_POSITION, R15_GUARANTEES, R15_NET_FUTURE_INC_OR_EXP, R15_NET_DELTA_WEI_FX_OPT_POSI, R15_OTHER_ITEMS, R15_NET_LONG_POSITION, R15_OR, R15_NET_SHORT_POSITION, "
				+ "    R16_NET_SPOT_POSITION, R16_NET_FORWARD_POSITION, R16_GUARANTEES, R16_NET_FUTURE_INC_OR_EXP, R16_NET_DELTA_WEI_FX_OPT_POSI, R16_OTHER_ITEMS, R16_NET_LONG_POSITION, R16_OR, R16_NET_SHORT_POSITION, "
				+ "    R17_NET_LONG_POSITION, R17_NET_SHORT_POSITION, "
				+ "    R21_LONG, R21_SHORT, R21_TOTAL_GROSS_LONG_SHORT, R21_NET_POSITION, "
				+ "    R22_LONG, R22_SHORT, R22_TOTAL_GROSS_LONG_SHORT, R22_NET_POSITION, " + "    R23_NET_POSITION, "
				+ "    R29_GREATER_NET_LONG_OR_SHORT, R29_ABS_VALUE_NET_GOLD_POSI, R29_CAPITAL_CHARGE, "
				+ "    R30_CAPITAL_REQUIRE, " + "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				+ "        S.R11_NET_SPOT_POSITION, S.R11_NET_FORWARD_POSITION, S.R11_GUARANTEES, S.R11_NET_FUTURE_INC_OR_EXP, S.R11_NET_DELTA_WEI_FX_OPT_POSI, S.R11_OTHER_ITEMS, S.R11_NET_LONG_POSITION, S.R11_OR, S.R11_NET_SHORT_POSITION, "
				+ "        S.R12_NET_SPOT_POSITION, S.R12_NET_FORWARD_POSITION, S.R12_GUARANTEES, S.R12_NET_FUTURE_INC_OR_EXP, S.R12_NET_DELTA_WEI_FX_OPT_POSI, S.R12_OTHER_ITEMS, S.R12_NET_LONG_POSITION, S.R12_OR, S.R12_NET_SHORT_POSITION, "
				+ "        S.R13_NET_SPOT_POSITION, S.R13_NET_FORWARD_POSITION, S.R13_GUARANTEES, S.R13_NET_FUTURE_INC_OR_EXP, S.R13_NET_DELTA_WEI_FX_OPT_POSI, S.R13_OTHER_ITEMS, S.R13_NET_LONG_POSITION, S.R13_OR, S.R13_NET_SHORT_POSITION, "
				+ "        S.R14_NET_SPOT_POSITION, S.R14_NET_FORWARD_POSITION, S.R14_GUARANTEES, S.R14_NET_FUTURE_INC_OR_EXP, S.R14_NET_DELTA_WEI_FX_OPT_POSI, S.R14_OTHER_ITEMS, S.R14_NET_LONG_POSITION, S.R14_OR, S.R14_NET_SHORT_POSITION, "
				+ "        S.R15_NET_SPOT_POSITION, S.R15_NET_FORWARD_POSITION, S.R15_GUARANTEES, S.R15_NET_FUTURE_INC_OR_EXP, S.R15_NET_DELTA_WEI_FX_OPT_POSI, S.R15_OTHER_ITEMS, S.R15_NET_LONG_POSITION, S.R15_OR, S.R15_NET_SHORT_POSITION, "
				+ "        S.R16_NET_SPOT_POSITION, S.R16_NET_FORWARD_POSITION, S.R16_GUARANTEES, S.R16_NET_FUTURE_INC_OR_EXP, S.R16_NET_DELTA_WEI_FX_OPT_POSI, S.R16_OTHER_ITEMS, S.R16_NET_LONG_POSITION, S.R16_OR, S.R16_NET_SHORT_POSITION, "
				+ "        S.R17_NET_LONG_POSITION, S.R17_NET_SHORT_POSITION, "
				+ "        S.R21_LONG, S.R21_SHORT, S.R21_TOTAL_GROSS_LONG_SHORT, S.R21_NET_POSITION, "
				+ "        S.R22_LONG, S.R22_SHORT, S.R22_TOTAL_GROSS_LONG_SHORT, S.R22_NET_POSITION, "
				+ "        S.R23_NET_POSITION, "
				+ "        S.R29_GREATER_NET_LONG_OR_SHORT, S.R29_ABS_VALUE_NET_GOLD_POSI, S.R29_CAPITAL_CHARGE, "
				+ "        S.R30_CAPITAL_REQUIRE, " + "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG "
				+ "    FROM BRRS_M_FXR_MOD_SUMMARYTABLE S " + "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
				+ ") " + "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_FXR Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_FXR_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_FXR MOD Updated : " + modUpdated);
		System.out.println("M_FXR Verification and Transfer Completed Successfully");
	}

	private void transferM_EPRData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_EPR_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_EPR record available for verification.");
		}

		System.out.println("M_EPR Report Date : " + reportDate);

		// Build column lists dynamically for rows 11 to 23
		StringBuilder setColumns = new StringBuilder();
		StringBuilder selectColumns = new StringBuilder();
		String[] fields = { "market", "gpfsr_nom_amt", "gpfsr_pos_att8_per_spe_ris", "gpfsr_chrg", "gpfsr_nom_amt1",
				"gpfsr_pos_att4_per_spe_ris", "gpfsr_chrg1", "gpfsr_nom_amt2", "gpfsr_pos_att2_per_spe_ris",
				"gpfsr_chrg2", "tot_spe_ris_chrg", "net_pos_gen_mar_ris", "gen_mar_ris_chrg_8per",
				"2per_gen_mar_ris_chrg_div_port", "tot_gen_mar_risk_chrg", "tot_mar_ris_chrg" };

		for (int i = 11; i <= 23; i++) {
			for (String field : fields) {
				String col = "R" + i + "_" + field;
				setColumns.append(col).append(", ");
				selectColumns.append("S.").append(col).append(", ");
			}
		}
		setColumns.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		selectColumns.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		// 1. Update BRRS_M_EPR_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_EPR_SUMMARYTABLE M SET (" + setColumns.toString() + ") = ("
				+ "    SELECT " + selectColumns.toString() + " FROM BRRS_M_EPR_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_EPR modification record not found for date : " + reportDate);
		}

		System.out.println("M_EPR Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_EPR_DETAILTABLE from MOD table
		String updateDetailSql = updateSummarySql.replace("BRRS_M_EPR_SUMMARYTABLE", "BRRS_M_EPR_DETAILTABLE");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		System.out.println("M_EPR Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_EPR_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_EPR MOD Updated : " + modUpdated);
		System.out.println("M_EPR Verification and Transfer Completed Successfully");
	}

	private static final String TBL_SUMMARY1 = "BRRS_M_CA5_SUMMARYTABLE1";

	private static final String TBL_DETAIL1 = "BRRS_M_CA5_DETAILTABLE1";

	private static final String TBL_SUMMARY2 = "BRRS_M_CA5_SUMMARYTABLE2";

	private static final String TBL_DETAIL2 = "BRRS_M_CA5_DETAILTABLE2";

	private void transferM_CA5_Data1(AuditServicesEntity auditRecord) {

		transferM_CA5("BRRS_M_CA5_MOD_SUMMARYTABLE1", TBL_SUMMARY1, TBL_DETAIL1, 14, 100, true);

		System.out.println("M_CA5 Part 1 Verification and Transfer Completed Successfully");
	}

	private void transferM_CA5_Data2(AuditServicesEntity auditRecord) {

		transferM_CA5("BRRS_M_CA5_MOD_SUMMARYTABLE2", TBL_SUMMARY2, TBL_DETAIL2, 101, 150, false);

		System.out.println("M_CA5 Part 2 Verification and Transfer Completed Successfully");
	}

// ============================================================
// COMMON M_CA5 TRANSFER METHOD
// ============================================================

	private void transferM_CA5(String modSummaryTable, String liveSummaryTable, String liveDetailTable, int startRow,
			int endRow, boolean skipRows) {

		// --------------------------------------------------------
		// 1. GET REPORT DATE FROM MOD SUMMARY TABLE
		// --------------------------------------------------------

		String dateSql = "SELECT MAX(REPORT_DATE) FROM " + modSummaryTable;

		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_CA5 record found in " + modSummaryTable);
		}

		System.out.println("M_CA5 Report Date: " + reportDate);

		// --------------------------------------------------------
		// 2. BUILD COLUMN LIST
		// --------------------------------------------------------

		StringBuilder setColumns = new StringBuilder();
		StringBuilder selectColumns = new StringBuilder();

		String[] commonFields = { "line_no", "note_holders", "name_of_instrument_programe",
				"issuing_entity_if_issued_throughan_spv", "issuance_date", "contractual_maturity_date",
				"effective_maturity_date_if_date", "amount" };

		String[] additionalFields = { "amount_derecognised_p000", "total_p000" };

		for (int i = startRow; i <= endRow; i++) {

			// Skip R29 through R34 for Part 1
			if (skipRows && i >= 29 && i <= 34) {
				continue;
			}

			// Common fields
			for (String field : commonFields) {

				String column = "R" + i + "_" + field;

				setColumns.append(column).append(", ");

				selectColumns.append("S.").append(column).append(", ");
			}

			// Additional fields:
			// Part 1: R35 to R100
			// Part 2: R101 to R150
			if (!skipRows || i >= 35) {

				for (String field : additionalFields) {

					String column = "R" + i + "_" + field;

					setColumns.append(column).append(", ");

					selectColumns.append("S.").append(column).append(", ");
				}
			}
		}

		// Add status columns
		setColumns.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");

		selectColumns.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		// --------------------------------------------------------
		// 3. UPDATE LIVE SUMMARY TABLE
		// --------------------------------------------------------

		String updateSummarySql = "UPDATE " + liveSummaryTable + " M " + "SET (" + setColumns + ") = (" + " SELECT "
				+ selectColumns + " FROM " + modSummaryTable + " S "
				+ " WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + " ) "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("No matching live summary record found for " + reportDate);
		}

		System.out.println("M_CA5 Summary Updated: " + summaryUpdated);

		// --------------------------------------------------------
		// 4. UPDATE LIVE DETAIL TABLE
		// MOD_DETAIL TABLE IS NOT USED
		// --------------------------------------------------------

		String updateDetailSql = "UPDATE " + liveDetailTable + " M " + "SET (" + setColumns + ") = (" + " SELECT "
				+ selectColumns + " FROM " + modSummaryTable + " S "
				+ " WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + " ) "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);

		System.out.println("M_CA5 Detail Updated: " + detailUpdated);

		// --------------------------------------------------------
		// 5. UPDATE MOD SUMMARY FLAGS
		// --------------------------------------------------------

		String updateModSummarySql = "UPDATE " + modSummaryTable + " " + "SET ENTITY_FLG = 'Y', "
				+ "    MODIFY_FLG = 'N' " + "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";

		int modSummaryUpdated = jdbcTemplate.update(updateModSummarySql, reportDate);

		System.out.println("M_CA5 MOD Summary Flags Updated: " + modSummaryUpdated);
	}

	private void transferM_CA7_Data(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_CA7_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_CA7 record available for transfer.");
		}

		System.out.println("M_CA7 Report Date : " + reportDate);

		String updateSummarySql = "UPDATE BRRS_M_CA7_SUMMARYTABLE M SET ("
				+ "R12_PRE_IFRS_PRO, R12_POST_IFRS9_PRO, R12_TRANS_AMT, "
				+ "R19_PRODUCT, R19_CAP_YEAR1, R19_AMT_ADD_YEAR1, " + "R20_PRODUCT, R20_CAP_YEAR2, R20_AMT_ADD_YEAR2, "
				+ "R21_PRODUCT, R21_CAP_YEAR3, R21_AMT_ADD_YEAR3, " + "R22_PRODUCT, R22_CAP_YEAR4, R22_AMT_ADD_YEAR4, "
				+ "REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, "
				+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG) = ("
				+ "    SELECT S.R12_PRE_IFRS_PRO, S.R12_POST_IFRS9_PRO, S.R12_TRANS_AMT, "
				+ "    S.R19_PRODUCT, S.R19_CAP_YEAR1, S.R19_AMT_ADD_YEAR1, "
				+ "    S.R20_PRODUCT, S.R20_CAP_YEAR2, S.R20_AMT_ADD_YEAR2, "
				+ "    S.R21_PRODUCT, S.R21_CAP_YEAR3, S.R21_AMT_ADD_YEAR3, "
				+ "    S.R22_PRODUCT, S.R22_CAP_YEAR4, S.R22_AMT_ADD_YEAR4, "
				+ "    S.REPORT_VERSION, S.REPORT_FREQUENCY, S.REPORT_CODE, S.REPORT_DESC, "
				+ "    S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_CA7_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		// 1. Update live Summary table
		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_CA7 modification record not found for date : " + reportDate);
		}
		System.out.println("M_CA7 Summary Table Updated : " + summaryUpdated);

		// 2. Update live Detail table (mirrors summary table update logic)
		String updateDetailSql = updateSummarySql.replace("BRRS_M_CA7_SUMMARYTABLE", "BRRS_M_CA7_DETAILTABLE");
		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_CA7 Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved
		String updateModSql = "UPDATE BRRS_M_CA7_MOD_SUMMARYTABLE SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_CA7 MOD Updated : " + modUpdated);
		System.out.println("✔ M_CA7 Verification and Transfer Completed Successfully (Summary & Detail updated)");
	}

	private void transferM_CR_Data(AuditServicesEntity auditRecord) {
		final String summaryTable = "BRRS_M_CR_SUMMARYTABLE";
		final String detailTable = "BRRS_M_CR_DETAILTABLE";
		final String modTable = "BRRS_M_CR_MOD_SUMMARYTABLE";
		final String[] fields = { "PRODUCT", "TOTAL_LONG_POS", "TOTAL_SHORT_POS", "GROSS_OPEN_POS", "CHARGE_BASIS_RISK",
				"CAPITAL_CHARGE_BASIS_RISK", "NET_OPEN_POS", "CHARGE_DIR_RISK", "CAPITAL_CHARGE_DIR_RISK",
				"TOTAL_CAPITAL_CHARGE" };

		// 1. Latest modified report date
		java.sql.Date reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM " + modTable,
				java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_CR record available for transfer.");
		}
		System.out.println("M_CR Report Date : " + reportDate);

		// 2. Column list: R10..R17 x fields, plus flags
		List<String> cols = new ArrayList<>();
		for (int i = 10; i <= 17; i++) {
			for (String field : fields) {
				cols.add("R" + i + "_" + field);
			}
		}
		cols.add("ENTITY_FLG");
		cols.add("MODIFY_FLG");
		cols.add("DEL_FLG");

		String targetCols = String.join(", ", cols);
		String sourceCols = cols.stream().map(c -> "S." + c).collect(java.util.stream.Collectors.joining(", "));

		String updateTemplate = " M SET (" + targetCols + ") = (SELECT " + sourceCols + " FROM " + modTable
				+ " S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		// 3. Live summary table: update, else insert from the MOD row
		int summaryUpdated = jdbcTemplate.update("UPDATE " + summaryTable + updateTemplate, reportDate);

		if (summaryUpdated == 0) {
			String insertSql = "INSERT INTO " + summaryTable + " (REPORT_DATE, " + targetCols + ")"
					+ " SELECT REPORT_DATE, " + targetCols + " FROM " + modTable
					+ " WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			summaryUpdated = jdbcTemplate.update(insertSql, reportDate);

			if (summaryUpdated == 0) {
				throw new IllegalStateException(
						"M_CR modification record not found in live summary table for date : " + reportDate);
			}
		}
		System.out.println("M_CR Summary Table Updated (" + summaryTable + ") : " + summaryUpdated);

		// 4. Live detail table: update only
		int detailUpdated = jdbcTemplate.update("UPDATE " + detailTable + updateTemplate, reportDate);
		System.out.println("M_CR Detail Table Updated (" + detailTable + ") : " + detailUpdated);

		// 5. Mark the MOD row as verified
		int modUpdated = jdbcTemplate.update(
				"UPDATE " + modTable + " SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				reportDate);
		System.out.println("M_CR MOD Record Updated : " + modUpdated);
		System.out.println("M_CR Verification and Transfer Completed Successfully across Summary & Detail tables!");
	}

	private void transferM_MRCData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_MRC_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_MRC record available for verification.");
		}

		System.out.println("M_MRC Report Date : " + reportDate);

		// Build column lists dynamically for M_MRC rows/fields (R33, R34 totals +
		// metadata)
		StringBuilder setColumns = new StringBuilder();
		StringBuilder selectColumns = new StringBuilder();

		// Rows 33 and 34 total fields
		int[] rowCodes = { 33, 34 };
		String[] fields = { "TOTAL" };

		for (int code : rowCodes) {
			for (String field : fields) {
				String col = "R" + code + "_" + field;
				setColumns.append(col).append(", ");
				selectColumns.append("S.").append(col).append(", ");
			}
		}

		// Metadata fields
		setColumns.append("REPORT_VERSION, REPORT_CODE, REPORT_DESC, ");
		selectColumns.append("S.REPORT_VERSION, S.REPORT_CODE, S.REPORT_DESC, ");

		// Common Audit/Status flags
		setColumns.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		selectColumns.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		// 1. Update BRRS_M_MRC_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_MRC_SUMMARYTABLE M SET (" + setColumns.toString() + ") = ("
				+ "    SELECT " + selectColumns.toString() + " FROM BRRS_M_MRC_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_MRC modification record not found for date : " + reportDate);
		}

		System.out.println("M_MRC Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_MRC_DETAILTABLE from MOD table
		String updateDetailSql = updateSummarySql.replace("BRRS_M_MRC_SUMMARYTABLE", "BRRS_M_MRC_DETAILTABLE");
		try {
			int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
			System.out.println("M_MRC Detail Table Updated : " + detailUpdated);
		} catch (Exception e) {
			System.out.println("ℹ Detail table update skipped or not applicable for M_MRC.");
		}

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_MRC_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_MRC MOD Updated : " + modUpdated);
		System.out.println("M_MRC Verification and Transfer Completed Successfully");
	}

	private void transferM_OPTR_Data(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_OPTR_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_OPTR record available for transfer.");
		}

		System.out.println("M_OPTR Report Date : " + reportDate);

		StringBuilder setColumns = new StringBuilder();
		StringBuilder selectColumns = new StringBuilder();
		String[] categories = { "INTEREST_RATES", "EQUITIES", "FOREIGN_EXC_GOLD", "COMMODITIES", "TOTAL" };

		for (int i = 10; i <= 14; i++) {
			for (String cat : categories) {
				String col = "R" + i + "_" + cat.toUpperCase();
				setColumns.append(col).append(", ");
				selectColumns.append("S.").append(col).append(", ");
			}
		}
		setColumns.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		selectColumns.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		// Base query template referencing MOD table to Live tables
		String selectSubquery = " SELECT " + selectColumns.toString() + " FROM BRRS_M_OPTR_MOD_SUMMARYTABLE S "
				+ " WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)";

		// ==========================================
		// 1. UPDATE LIVE SUMMARY TABLE
		// ==========================================
		String updateSummarySql = "UPDATE BRRS_M_OPTR_SUMMARYTABLE M SET (" + setColumns.toString() + ") = ("
				+ selectSubquery + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException(
					"M_OPTR modification record not found in live summary table for date : " + reportDate);
		}
		System.out.println("M_OPTR Summary Table Updated (BRRS_M_OPTR_SUMMARYTABLE) : " + summaryUpdated);

		// ==========================================
		// 2. UPDATE LIVE DETAIL TABLE
		// ==========================================
		String updateDetailSql = "UPDATE BRRS_M_OPTR_DETAILTABLE M SET (" + setColumns.toString() + ") = ("
				+ selectSubquery + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_OPTR Detail Table Updated (BRRS_M_OPTR_DETAILTABLE) : " + detailUpdated);

		// ==========================================
		// 3. MARK MOD RECORD AS APPROVED (MODIFY_FLG = 'N')
		// ==========================================
		String updateModSql = "UPDATE BRRS_M_OPTR_MOD_SUMMARYTABLE SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_OPTR MOD Record Updated : " + modUpdated);
		System.out.println("✔ M_OPTR Verification and Transfer Completed Successfully across Summary & Detail tables!");
	}

	private void transferM_UNCONS_INVEST_Data(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_UNCONS_INVEST_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_UNCONS_INVEST record available for transfer.");
		}

		StringBuilder setColumns = new StringBuilder();
		StringBuilder selectColumns = new StringBuilder();

		int[] ranges = { 11, 12, 13, 14, 15, 22, 23, 24, 29, 35, 36, 37, 38 };
		for (int i : ranges) {
			String prefix = "R" + i + "_";
			String[] fields;
			if (i >= 11 && i <= 15) {
				fields = new String[] { "product", "amount", "percent_of_cet1_holding",
						"percent_of_additional_tier_1_holding", "percent_of_tier_2_holding" };
			} else if (i >= 22 && i <= 24) {
				fields = new String[] { "product", "accuulated_equity_interest_5", "assets", "liabilities", "revenue",
						"profit_or_loss", "unreg_share_of_loss", "cumulative_unreg_share_of_loss" };
			} else if (i == 29) {
				fields = new String[] { "product", "fair_value" };
			} else {
				fields = new String[] { "product", "company", "jurisdiction_of_incorp_1", "jurisdiction_of_incorp_2",
						"line_of_business", "currency", "share_capital", "accumulated_equity_interest" };
			}

			for (String field : fields) {
				String col = (prefix + field).toUpperCase();
				setColumns.append(col).append(", ");
				selectColumns.append("S.").append(col).append(", ");
			}
		}
		setColumns.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		selectColumns.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		String selectSubquery = " SELECT " + selectColumns.toString() + " FROM BRRS_M_UNCONS_INVEST_MOD_SUMMARYTABLE S "
				+ " WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)";

		// 1. Update live Summary table
		String updateSummarySql = "UPDATE BRRS_M_UNCONS_INVEST_SUMMARYTABLE M SET (" + setColumns.toString() + ") = ("
				+ selectSubquery + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);
		if (summaryUpdated == 0) {
			throw new IllegalStateException(
					"M_UNCONS_INVEST modification record not found in live summary table for date : " + reportDate);
		}

		// 2. Update live Detail table
		String updateDetailSql = "UPDATE BRRS_M_UNCONS_INVEST_DETAILTABLE M SET (" + setColumns.toString() + ") = ("
				+ selectSubquery + ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		jdbcTemplate.update(updateDetailSql, reportDate);

		// 3. Mark MOD record status as approved
		String updateModSql = "UPDATE BRRS_M_UNCONS_INVEST_MOD_SUMMARYTABLE SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		jdbcTemplate.update(updateModSql, reportDate);

		System.out.println(
				"✔ M_UNCONS_INVEST Verification and Transfer Completed Successfully across Summary & Detail tables!");
	}

	private void transferLA4Data(AuditServicesEntity auditRecord) {
		final String liveTable = "BRRS_M_LA4_SUMMARYTABLE2";
		final String modTable = "BRRS_M_LA4_MOD_SUMMARYTABLE2";

		String dateSql = "SELECT MAX(REPORT_DATE) FROM " + modTable;
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_LA4 record available for verification.");
		}

		System.out.println("M_LA4 Report Date : " + reportDate);

		// Build column lists for rows 11 to 64 (FACTORING_DEBTORS, LEASING)
		StringBuilder targetCols = new StringBuilder();
		StringBuilder sourceCols = new StringBuilder();

		for (int i = 11; i <= 64; i++) {
			targetCols.append("R").append(i).append("_FACTORING_DEBTORS, ").append("R").append(i).append("_LEASING, ");
			sourceCols.append("S.R").append(i).append("_FACTORING_DEBTORS, ").append("S.R").append(i)
					.append("_LEASING, ");
		}

		// Flags
		targetCols.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		sourceCols.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		String updateSql = "UPDATE " + liveTable + " M SET (" + targetCols + ") = (" + "SELECT " + sourceCols + " FROM "
				+ modTable + " S" + " WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
				+ ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int inserted = jdbcTemplate.update(updateSql, reportDate);

		if (inserted == 0) {
			throw new IllegalStateException("M_LA4 modification record not found for date : " + reportDate);
		}

		System.out.println("M_LA4 Main Updated : " + inserted);

		String updateModSql = "UPDATE " + modTable
				+ " SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int updated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_LA4 MOD Updated : " + updated);

		// Execute stored procedure after verification/transfer
		String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);
		jdbcTemplate.update("BEGIN BRRS_M_LA4_SUMMARY_PROCEDURE(?); END;", formattedDate);

		System.out.println("M_LA4 Verification and Procedure Execution Completed Successfully");
	}

	private void transferLA3Data(AuditServicesEntity auditRecord) {
		final String liveTable = "BRRS_M_LA3_SUMMARYTABLE2";
		final String modTable = "BRRS_M_LA3_MOD_SUMMARYTABLE2";

		String dateSql = "SELECT MAX(REPORT_DATE) FROM " + modTable;
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_LA3 record available for verification.");
		}

		System.out.println("M_LA3 Report Date : " + reportDate);

		// Build column lists for rows 36 to 42
		StringBuilder targetCols = new StringBuilder();
		StringBuilder sourceCols = new StringBuilder();

		for (int i = 36; i <= 42; i++) {
			targetCols.append("R").append(i).append("_NO_OF_AC, ").append("R").append(i).append("_CREDIT_LIMIT, ")
					.append("R").append(i).append("_AMOUNT_OUTSTANDING, ");
			sourceCols.append("S.R").append(i).append("_NO_OF_AC, ").append("S.R").append(i).append("_CREDIT_LIMIT, ")
					.append("S.R").append(i).append("_AMOUNT_OUTSTANDING, ");
		}

		// Flags
		targetCols.append("ENTITY_FLG, MODIFY_FLG, DEL_FLG");
		sourceCols.append("S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG");

		String updateSql = "UPDATE " + liveTable + " M SET (" + targetCols + ") = (" + "SELECT " + sourceCols + " FROM "
				+ modTable + " S" + " WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
				+ ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int inserted = jdbcTemplate.update(updateSql, reportDate);

		if (inserted == 0) {
			throw new IllegalStateException("M_LA3 modification record not found for date : " + reportDate);
		}

		System.out.println("M_LA3 Main Updated : " + inserted);

		String updateModSql = "UPDATE " + modTable
				+ " SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int updated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_LA3 MOD Updated : " + updated);

		// Execute stored procedure after verification/transfer
		String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);
		jdbcTemplate.update("BEGIN BRRS_M_LA3_SUMMARY_PROCEDURE(?); END;", formattedDate);

		System.out.println("M_LA3 Verification and Procedure Execution Completed Successfully");
	}

	private void transferISData(AuditServicesEntity auditRecord) {
		final String[] liveTables = { "BRRS_M_IS_SUMMARYTABLE1", "BRRS_M_IS_SUMMARYTABLE2" };
		final String[] modTables = { "BRRS_M_IS_MOD_SUMMARYTABLE1", "BRRS_M_IS_MOD_SUMMARYTABLE2" };
		final Class<?>[] entities = { M_IS_Summary_Entity1.class, M_IS_Summary_Entity2.class };
		final int[] fromRow = { 10, 21 };
		final int[] toRow = { 16, 35 };
		final String[][] fields = {
				{ "PRODUCT", "FAIR_VALUE_PROFIT_AND_LOSS", "HELD_TO_MATURITY", "AVAILABLE_FOR_SALE", "TOTAL" },
				{ "PRODUCT", "HELD_FOR_TRADING", "AMORTISED_COST", "AVAILABLE_FOR_SALE",
						"FAIR_VALUE_THROUGH_PROFIT_AND_LOSS", "QUALIFYING_FOR_HEDGE_ACCOUNTING", "TOTAL" } };

		// 1. Latest modified report date
		java.sql.Date reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM " + modTables[0],
				java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_IS record available for verification.");
		}
		System.out.println("M_IS Report Date : " + reportDate);

		for (int t = 0; t < 2; t++) {
			// 2. Column list: only columns that have a getter on the entity.
			// Table 2 skips R28_HELD_FOR_TRADING.
			List<String> cols = new ArrayList<>();
			for (int i = fromRow[t]; i <= toRow[t]; i++) {
				for (String field : fields[t]) {
					if (t == 1 && i == 28 && "HELD_FOR_TRADING".equals(field)) {
						continue;
					}
					try {
						entities[t].getMethod("getR" + i + "_" + field);
						cols.add("R" + i + "_" + field);
					} catch (NoSuchMethodException ignored) {
					}
				}
			}
			cols.add("ENTITY_FLG");
			cols.add("MODIFY_FLG");
			cols.add("DEL_FLG");

			String targetCols = String.join(", ", cols);
			String sourceCols = cols.stream().map(c -> "S." + c).collect(java.util.stream.Collectors.joining(", "));

			// 3. Update the live row from the MOD row
			String updateSql = "UPDATE " + liveTables[t] + " M SET (" + targetCols + ") = (SELECT " + sourceCols
					+ " FROM " + modTables[t] + " S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE))"
					+ " WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
			int updatedLive = jdbcTemplate.update(updateSql, reportDate);

			// 4. No live row yet for this date: insert it from the MOD row
			if (updatedLive == 0) {
				String insertSql = "INSERT INTO " + liveTables[t] + " (REPORT_DATE, " + targetCols + ")"
						+ " SELECT REPORT_DATE, " + targetCols + " FROM " + modTables[t]
						+ " WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				updatedLive = jdbcTemplate.update(insertSql, reportDate);

				if (updatedLive == 0) {
					throw new IllegalStateException(
							"M_IS table " + (t + 1) + " modification record not found for date : " + reportDate);
				}
			}
			System.out.println("M_IS Main Table " + (t + 1) + " Updated : " + updatedLive);
		}

		// 5. Mark both MOD rows as verified
		String flagSql = " SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int m1 = jdbcTemplate.update("UPDATE " + modTables[0] + flagSql, reportDate);
		int m2 = jdbcTemplate.update("UPDATE " + modTables[1] + flagSql, reportDate);
		System.out.println("M_IS MOD Updated : " + m1 + " / " + m2);

		// 6. Run the stored procedure after verification/transfer
		String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);
		jdbcTemplate.update("BEGIN BRRS_M_IS_SUMMARY_PROCEDURE(?); END;", formattedDate);

		System.out.println("M_IS Verification and Procedure Execution Completed Successfully");
	}

	private void transferPDData(AuditServicesEntity auditRecord) {
		final String liveTable = "BRRS_M_PD_MANUAL_SUMMARYTABLE";
		final String modTable = "BRRS_M_PD_MANUAL_MOD_SUMMARYTABLE";

		// 1. Latest modified report date
		java.sql.Date reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM " + modTable,
				java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_PD record available for verification.");
		}
		System.out.println("M_PD Report Date : " + reportDate);

		// 2. Column list from the entity getters (REPORT_DATE excluded)
		List<String> cols = new ArrayList<>();
		for (Method m : M_PD_Manual_Summary_Entity.class.getMethods()) {
			String name = m.getName();
			if (!name.startsWith("get") || m.getParameterCount() != 0 || name.equals("getClass")) {
				continue;
			}
			String col = name.substring(3).toUpperCase();
			if (col.equals("REPORT_DATE")) {
				continue;
			}
			cols.add(col);
		}
		java.util.Collections.sort(cols);

		String targetCols = String.join(", ", cols);
		String sourceCols = cols.stream().map(c -> "S." + c).collect(java.util.stream.Collectors.joining(", "));

		// 3. Update the live row from the MOD row
		String updateSql = "UPDATE " + liveTable + " M SET (" + targetCols + ") = (SELECT " + sourceCols + " FROM "
				+ modTable + " S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE))"
				+ " WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int updatedLive = jdbcTemplate.update(updateSql, reportDate);

		// 4. No live row for this date yet: insert it straight from the MOD row
		if (updatedLive == 0) {
			String insertSql = "INSERT INTO " + liveTable + " (REPORT_DATE, " + targetCols + ")"
					+ " SELECT REPORT_DATE, " + targetCols + " FROM " + modTable
					+ " WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			updatedLive = jdbcTemplate.update(insertSql, reportDate);
		}
		System.out.println("M_PD Main Updated : " + updatedLive);

		// 5. Mark the MOD row as verified
		int updatedMod = jdbcTemplate.update(
				"UPDATE " + modTable + " SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);
		System.out.println("M_PD MOD Updated : " + updatedMod);
		System.out.println("M_PD Verification Completed Successfully");
	}

	private void transferCA2Data(AuditServicesEntity auditRecord) {
		final String liveTable = "BRRS_M_CA2_SUMMARYTABLE";
		final String modTable = "BRRS_M_CA2_MOD_SUMMARYTABLE";

		// 1. Latest modified report date
		java.sql.Date reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM " + modTable,
				java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_CA2 record available for verification.");
		}
		System.out.println("M_CA2 Report Date : " + reportDate);

		String cols = "R11_AMOUNT_2, R32_AMOUNT_2, R42_AMOUNT_2, R44_AMOUNT_2, R43_AMOUNT_2, R46_AMOUNT_2, "
				+ "R14_AMOUNT_1, R15_AMOUNT_1, R16_AMOUNT_1, R18_AMOUNT_1, R19_AMOUNT_1, R21_AMOUNT_1, "
				+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG";
		String srcCols = java.util.Arrays.stream(cols.split(",\\s*")).map(c -> "S." + c)
				.collect(java.util.stream.Collectors.joining(", "));

		// 2. Update the live row from the MOD row
		String updateSql = "UPDATE " + liveTable + " M SET (" + cols + ") = (SELECT " + srcCols + " FROM " + modTable
				+ " S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE))" + " WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int updatedLive = jdbcTemplate.update(updateSql, reportDate);

		// 3. No live row yet for this date: insert it from the MOD row
		if (updatedLive == 0) {
			String insertSql = "INSERT INTO " + liveTable + " (REPORT_DATE, " + cols + ")" + " SELECT REPORT_DATE, "
					+ cols + " FROM " + modTable + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			updatedLive = jdbcTemplate.update(insertSql, reportDate);

			if (updatedLive == 0) {
				throw new IllegalStateException("M_CA2 modification record not found for date : " + reportDate);
			}
		}
		System.out.println("M_CA2 Main Updated : " + updatedLive);

		// 4. Mark the MOD row as verified
		int updatedMod = jdbcTemplate.update(
				"UPDATE " + modTable + " SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);
		System.out.println("M_CA2 MOD Updated : " + updatedMod);

		// 5. Run the stored procedure after verification/transfer
		String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);
		jdbcTemplate.update("BEGIN BRRS_M_CA2_SUMMARY_PROCEDURE(?); END;", formattedDate);

		System.out.println("M_CA2 Verification and Procedure Execution Completed Successfully");
	}

	private void transferISCAData(AuditServicesEntity auditRecord) {
		final String liveTable = "BRRS_M_I_S_CA_SUMMARYTABLE";
		final String modTable = "BRRS_M_I_S_CA_MOD_SUMMARYTABLE";
		final int[] extraTotalRows = { 66, 67, 68 };

		// 1. Latest modified report date
		java.sql.Date reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM " + modTable,
				java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_I_S_CA record available for verification.");
		}
		System.out.println("M_I_S_CA Report Date : " + reportDate);

		// 2. Column list: only columns that have a getter on the entity
		List<String> cols = new ArrayList<>();
		for (int i = 11; i <= 64; i++) {
			try {
				M_I_S_CA_Summary_Entity.class.getMethod("getR" + i + "_write_offs");
				cols.add("R" + i + "_WRITE_OFFS");
			} catch (NoSuchMethodException ignored) {
			}
		}
		for (int i : extraTotalRows) {
			try {
				M_I_S_CA_Summary_Entity.class.getMethod("getR" + i + "_total");
				cols.add("R" + i + "_TOTAL");
			} catch (NoSuchMethodException ignored) {
			}
		}
		cols.add("ENTITY_FLG");
		cols.add("MODIFY_FLG");
		cols.add("DEL_FLG");

		String targetCols = String.join(", ", cols);
		String sourceCols = cols.stream().map(c -> "S." + c).collect(java.util.stream.Collectors.joining(", "));

		// 3. Update the live row from the MOD row
		String updateSql = "UPDATE " + liveTable + " M SET (" + targetCols + ") = (SELECT " + sourceCols + " FROM "
				+ modTable + " S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE))"
				+ " WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int updatedLive = jdbcTemplate.update(updateSql, reportDate);

		// 4. No live row yet for this date: insert it from the MOD row
		if (updatedLive == 0) {
			String insertSql = "INSERT INTO " + liveTable + " (REPORT_DATE, " + targetCols + ")"
					+ " SELECT REPORT_DATE, " + targetCols + " FROM " + modTable
					+ " WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			updatedLive = jdbcTemplate.update(insertSql, reportDate);

			if (updatedLive == 0) {
				throw new IllegalStateException("M_I_S_CA modification record not found for date : " + reportDate);
			}
		}
		System.out.println("M_I_S_CA Main Updated : " + updatedLive);

		// 5. Mark the MOD row as verified
		int updatedMod = jdbcTemplate.update(
				"UPDATE " + modTable + " SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);
		System.out.println("M_I_S_CA MOD Updated : " + updatedMod);
		System.out.println("M_I_S_CA Verification Completed Successfully");
	}

	private void transferSCIEData(AuditServicesEntity auditRecord) {
		final String liveTable = "BRRS_M_SCI_E_SUMMARYTABLE";
		final String modTable = "BRRS_M_SCI_E_MOD_SUMMARYTABLE";
		final int[] rows = { 45, 46, 54, 58, 59, 60, 66, 67, 68, 74, 85 };

		// 1. Latest modified report date
		java.sql.Date reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM " + modTable,
				java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_SCI_E record available for verification.");
		}
		System.out.println("M_SCI_E Report Date : " + reportDate);

		// 2. Column list
		List<String> cols = new ArrayList<>();
		for (int i : rows) {
			cols.add("R" + i + "_MONTH");
		}
		cols.add("ENTITY_FLG");
		cols.add("MODIFY_FLG");
		cols.add("DEL_FLG");

		String targetCols = String.join(", ", cols);
		String sourceCols = cols.stream().map(c -> "S." + c).collect(java.util.stream.Collectors.joining(", "));

		// 3. Update the live row from the MOD row
		String updateSql = "UPDATE " + liveTable + " M SET (" + targetCols + ") = (SELECT " + sourceCols + " FROM "
				+ modTable + " S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE))"
				+ " WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int updatedLive = jdbcTemplate.update(updateSql, reportDate);

		// 4. No live row yet for this date: insert it from the MOD row
		if (updatedLive == 0) {
			String insertSql = "INSERT INTO " + liveTable + " (REPORT_DATE, " + targetCols + ")"
					+ " SELECT REPORT_DATE, " + targetCols + " FROM " + modTable
					+ " WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			updatedLive = jdbcTemplate.update(insertSql, reportDate);

			if (updatedLive == 0) {
				throw new IllegalStateException("M_SCI_E modification record not found for date : " + reportDate);
			}
		}
		System.out.println("M_SCI_E Main Updated : " + updatedLive);

		// 5. Mark the MOD row as verified
		int updatedMod = jdbcTemplate.update(
				"UPDATE " + modTable + " SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);
		System.out.println("M_SCI_E MOD Updated : " + updatedMod);
		System.out.println("M_SCI_E Verification Completed Successfully");
	}

	private void transferLIQData(AuditServicesEntity auditRecord) {
		final String liveTable = "BRRS_M_LIQ_MANUAL_SUMMARYTABLE";
		final String modTable = "BRRS_M_LIQ_MANUAL_MOD_SUMMARYTABLE";
		final int[] rows = { 16, 19, 20, 22, 23, 26 };
		final String[] fields = { "PRODUCT", "TOTAL" };

		// 1. Latest modified report date
		java.sql.Date reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM " + modTable,
				java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_LIQ record available for verification.");
		}
		System.out.println("M_LIQ Report Date : " + reportDate);

		// 2. Column list: R16_PRODUCT, R16_TOTAL, R19_PRODUCT, ...
		List<String> cols = new ArrayList<>();
		for (int r : rows) {
			for (String f : fields) {
				cols.add("R" + r + "_" + f);
			}
		}
		String targetCols = String.join(", ", cols);
		String sourceCols = cols.stream().map(c -> "S." + c).collect(java.util.stream.Collectors.joining(", "));

		// 3. Update the live row from the MOD row
		String updateSql = "UPDATE " + liveTable + " M SET (" + targetCols + ") = (SELECT " + sourceCols + " FROM "
				+ modTable + " S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE))"
				+ " WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int updatedLive = jdbcTemplate.update(updateSql, reportDate);

		// 4. No live row yet for this date: insert it from the MOD row
		if (updatedLive == 0) {
			String insertSql = "INSERT INTO " + liveTable + " (REPORT_DATE, " + targetCols + ")"
					+ " SELECT REPORT_DATE, " + targetCols + " FROM " + modTable
					+ " WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			updatedLive = jdbcTemplate.update(insertSql, reportDate);

			if (updatedLive == 0) {
				throw new IllegalStateException("M_LIQ modification record not found for date : " + reportDate);
			}
		}
		System.out.println("M_LIQ Main Updated : " + updatedLive);

		// 5. Mark the MOD row as verified
		int updatedMod = jdbcTemplate.update(
				"UPDATE " + modTable + " SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);
		System.out.println("M_LIQ MOD Updated : " + updatedMod);
		System.out.println("M_LIQ Verification Completed Successfully");
	}

	private void transferLIQGAPData(AuditServicesEntity auditRecord) {
		final String liveTable = "BRRS_M_LIQGAP_MANUAL_SUMMARYTABLE";
		final String modTable = "BRRS_M_LIQGAP_MOD_MANUAL_SUMMARYTABLE";
		final String cols = "R21_NON_INTEREST_BEARING, R32_NON_INTEREST_BEARING, "
				+ "R33_THIRD_MONTH, R33_LAST_MONTH, R33_FIRST_YEAR, R33_FIFTH_YEAR, R33_NON_INTEREST_BEARING, "
				+ "R34_FIRST_MONTH, ENTITY_FLG, MODIFY_FLG, DEL_FLG";
		final String srcCols = "S." + cols.replace(", ", ", S.");

		java.sql.Date reportDate = jdbcTemplate.queryForObject(
				"SELECT MAX(REPORT_DATE) FROM " + modTable + " WHERE MODIFY_FLG = 'Y'", java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No pending M_LIQGAP modification available for verification.");
		}
		System.out.println("M_LIQGAP Report Date : " + reportDate);

		String updateSql = "UPDATE " + liveTable + " M SET (" + cols + ") = (SELECT " + srcCols + " FROM " + modTable
				+ " S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE))" + " WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int updatedLive = jdbcTemplate.update(updateSql, reportDate);

		if (updatedLive == 0) {
			String insertSql = "INSERT INTO " + liveTable
					+ " (REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, " + cols
					+ ") SELECT REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, " + cols
					+ " FROM " + modTable + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			updatedLive = jdbcTemplate.update(insertSql, reportDate);

			if (updatedLive == 0) {
				throw new IllegalStateException("M_LIQGAP modification record not found for date : " + reportDate);
			}
		}
		System.out.println("M_LIQGAP Main Updated : " + updatedLive);

		int updatedMod = jdbcTemplate.update(
				"UPDATE " + modTable + " SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);
		System.out.println("M_LIQGAP MOD Updated : " + updatedMod);
	}

	private void transferGALORData(AuditServicesEntity auditRecord) {
		final String liveTable = "BRRS_M_GALOR_MANUAL_SUMMARYTABLE";
		final String modTable = "BRRS_M_GALOR_MANUAL_MOD_SUMMARYTABLE";
		final int[] rows = { 22, 23, 57, 58, 60, 61, 64, 65, 67, 68, 111, 112, 113, 114 };
		final String[] fullFields = { "product", "botswana", "south_africa", "sadc", "usa", "uk", "europe", "india",
				"sydney", "uganda", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "total" };

		// 1. Latest modified report date
		java.sql.Date reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM " + modTable,
				java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_GALOR record available for verification.");
		}
		System.out.println("M_GALOR Report Date : " + reportDate);

		// 2. Column list: only {row, field} pairs that have a getter on the entity.
		// Rows 111 to 114 only use "botswana".
		List<String> cols = new ArrayList<>();
		for (int row : rows) {
			String[] fields = (row >= 111 && row <= 114) ? new String[] { "botswana" } : fullFields;
			for (String field : fields) {
				try {
					M_GALOR_Manual_Summary_Entity.class.getMethod("getR" + row + "_" + field);
					cols.add("R" + row + "_" + field.toUpperCase());
				} catch (NoSuchMethodException ignored) {
				}
			}
		}
		cols.add("ENTITY_FLG");
		cols.add("MODIFY_FLG");
		cols.add("DEL_FLG");

		String targetCols = String.join(", ", cols);
		String sourceCols = cols.stream().map(c -> "S." + c).collect(java.util.stream.Collectors.joining(", "));

		// 3. Update the live row from the MOD row
		String updateSql = "UPDATE " + liveTable + " M SET (" + targetCols + ") = (SELECT " + sourceCols + " FROM "
				+ modTable + " S WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE))"
				+ " WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
		int updatedLive = jdbcTemplate.update(updateSql, reportDate);

		// 4. No live row yet for this date: insert it from the MOD row
		if (updatedLive == 0) {
			String insertSql = "INSERT INTO " + liveTable + " (REPORT_DATE, " + targetCols + ")"
					+ " SELECT REPORT_DATE, " + targetCols + " FROM " + modTable
					+ " WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			updatedLive = jdbcTemplate.update(insertSql, reportDate);

			if (updatedLive == 0) {
				throw new IllegalStateException("M_GALOR modification record not found for date : " + reportDate);
			}
		}
		System.out.println("M_GALOR Main Updated : " + updatedLive);

		// 5. Mark the MOD row as verified
		int updatedMod = jdbcTemplate.update(
				"UPDATE " + modTable + " SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);
		System.out.println("M_GALOR MOD Updated : " + updatedMod);
		System.out.println("M_GALOR Verification Completed Successfully");
	}

	private void transferMSirData(AuditServicesEntity auditRecord) {

		// ── 1. Find the pending MOD row (prefer MODIFY_FLG='Y', fallback to MAX) ──
		java.sql.Date reportDate = null;
		try {
			reportDate = jdbcTemplate.queryForObject(
					"SELECT MAX(REPORT_DATE) FROM BRRS_M_SIR_MOD_SUMMARYTABLE WHERE MODIFY_FLG = 'Y'",
					java.sql.Date.class);
		} catch (Exception ignored) {
			// fall through to MAX(REPORT_DATE)
		}
		if (reportDate == null) {
			reportDate = jdbcTemplate.queryForObject("SELECT MAX(REPORT_DATE) FROM BRRS_M_SIR_MOD_SUMMARYTABLE",
					java.sql.Date.class);
		}
		if (reportDate == null) {
			throw new IllegalStateException("No modified M_SIR record available for verification.");
		}

		System.out.println("M_SIR Report Date : " + reportDate);

		// ── 2. Load MOD row ──
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
				"SELECT * FROM BRRS_M_SIR_MOD_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)", reportDate);
		if (rows.isEmpty()) {
			throw new IllegalStateException("M_SIR MOD row not found for date : " + reportDate);
		}
		Map<String, Object> mod = rows.get(0);

		// ── 3. Guard: refuse to transfer unless MODIFY_FLG='Y' ──
		Object modifyFlag = mod.get("MODIFY_FLG");
		if (modifyFlag == null || !"Y".equalsIgnoreCase(modifyFlag.toString())) {
			throw new IllegalStateException(
					"M_SIR MOD row is not pending (MODIFY_FLG != 'Y') for date : " + reportDate);
		}

		// ── 4. Build SET clause — exclude identifier and metadata columns ──
		StringBuilder setClause = new StringBuilder();
		List<Object> params = new ArrayList<>();
		for (Map.Entry<String, Object> entry : mod.entrySet()) {
			String col = entry.getKey();

			// Skip identifier + metadata + resub-only columns
			if (col.equalsIgnoreCase("REPORT_DATE") || col.equalsIgnoreCase("REPORT_VERSION")
					|| col.equalsIgnoreCase("REPORT_FREQUENCY") || col.equalsIgnoreCase("REPORT_CODE")
					|| col.equalsIgnoreCase("REPORT_DESC") || col.equalsIgnoreCase("REPORT_RESUBDATE") // ← IMPORTANT:
																										// added
					|| col.equalsIgnoreCase("ENTITY_FLG") || col.equalsIgnoreCase("MODIFY_FLG")
					|| col.equalsIgnoreCase("DEL_FLG")) {
				continue;
			}
			setClause.append(", ").append(col).append(" = ?");
			params.add(entry.getValue());
		}
		if (setClause.length() == 0) {
			throw new IllegalStateException("M_SIR MOD row has no transferable columns for date : " + reportDate);
		}
		String body = setClause.substring(2); // strip leading ", "
		Object[] args = appendArgs(params.toArray(), reportDate);

		// ── 5. Transfer MOD → LIVE Summary ──
		int summaryUpdated = jdbcTemplate
				.update("UPDATE BRRS_M_SIR_SUMMARYTABLE SET " + body + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)", args);
		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_SIR live summary row not found for date : " + reportDate);
		}
		System.out.println("M_SIR Summary Table Updated : " + summaryUpdated);

		// ── 6. Transfer MOD → LIVE Detail (guarded — warn if 0 rows but don't fail) ──
		int detailUpdated = jdbcTemplate
				.update("UPDATE BRRS_M_SIR_DETAILTABLE SET " + body + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)", args);
		if (detailUpdated == 0) {
			System.out.println("M_SIR Detail Table has no row for date : " + reportDate + " — summary-only transfer");
		} else {
			System.out.println("M_SIR Detail Table Updated : " + detailUpdated);
		}

		// ── 7. Flip MOD flag to approved ──
		int modUpdated = jdbcTemplate.update("UPDATE BRRS_M_SIR_MOD_SUMMARYTABLE "
				+ "SET ENTITY_FLG='Y', MODIFY_FLG='N' " + "WHERE TRUNC(REPORT_DATE) = TRUNC(?)", reportDate);

		System.out.println("M_SIR MOD Updated : " + modUpdated);
		System.out.println("M_SIR Verification and Transfer Completed Successfully");
	}

	private void transferGMIRTData(AuditServicesEntity auditRecord) {

		System.out.println("================ M_GMIRT TRANSFER START ================");
		System.out.println("Audit ID     : " + auditRecord.getSno());
		System.out.println("Audit Table  : " + auditRecord.getAudit_table());

		try {
			java.sql.Date reportDate = jdbcTemplate.queryForObject(
					"SELECT MAX(REPORT_DATE) FROM BRRS_M_GMIRT_MOD_SUMMARYTABLE WHERE MODIFY_FLG='Y'",
					java.sql.Date.class);

			if (reportDate == null) {
				throw new IllegalStateException("No modified M_GMIRT record available for verification.");
			}

			BigDecimal version = jdbcTemplate
					.queryForObject(
							"SELECT MAX(REPORT_VERSION) FROM BRRS_M_GMIRT_MOD_SUMMARYTABLE "
									+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?) AND MODIFY_FLG='Y'",
							BigDecimal.class, reportDate);

			if (version == null) {
				throw new IllegalStateException("No pending MOD version for M_GMIRT date " + reportDate);
			}

			System.out.println("M_GMIRT Verify — date=" + reportDate + ", version=" + version);

			String updateLive = "UPDATE BRRS_M_GMIRT_SUMMARYTABLE M SET ("
					+ "  R9_PULA, R9_USD, R9_ZAR, R9_GBP, R9_EURO, R9_JPY, R9_RUPEE, R9_RENMINBI, R9_OTHER, "
					+ "  R10_PULA, R10_USD, R10_ZAR, R10_GBP, R10_EURO, R10_JPY, R10_RUPEE, R10_RENMINBI, R10_OTHER, R10_TOT_CAP_REQ, "
					+ "  R11_PULA, R11_USD, R11_ZAR, R11_GBP, R11_EURO, R11_JPY, R11_RUPEE, R11_RENMINBI, R11_OTHER, R11_TOT_CAP_REQ, "
					+ "  ENTITY_FLG, MODIFY_FLG, DEL_FLG) = (SELECT "
					+ "  S.R9_PULA, S.R9_USD, S.R9_ZAR, S.R9_GBP, S.R9_EURO, S.R9_JPY, S.R9_RUPEE, S.R9_RENMINBI, S.R9_OTHER, "
					+ "  S.R10_PULA, S.R10_USD, S.R10_ZAR, S.R10_GBP, S.R10_EURO, S.R10_JPY, S.R10_RUPEE, S.R10_RENMINBI, S.R10_OTHER, S.R10_TOT_CAP_REQ, "
					+ "  S.R11_PULA, S.R11_USD, S.R11_ZAR, S.R11_GBP, S.R11_EURO, S.R11_JPY, S.R11_RUPEE, S.R11_RENMINBI, S.R11_OTHER, S.R11_TOT_CAP_REQ, "
					+ "  S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "  FROM BRRS_M_GMIRT_MOD_SUMMARYTABLE S "
					+ "  WHERE TRUNC(S.REPORT_DATE)=TRUNC(M.REPORT_DATE) AND S.REPORT_VERSION=?) "
					+ "WHERE TRUNC(M.REPORT_DATE)=TRUNC(?)";

			System.out.println("M_GMIRT about to execute UPDATE SUMMARYTABLE...");
			int updated = jdbcTemplate.update(updateLive, version, reportDate);
			if (updated == 0) {
				throw new IllegalStateException("M_GMIRT live row (SUMMARYTABLE) missing for date " + reportDate);
			}
			System.out.println("M_GMIRT Live Summary Updated : " + updated);

			int modUpdated = jdbcTemplate
					.update("UPDATE BRRS_M_GMIRT_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' "
							+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?) AND REPORT_VERSION=?", reportDate, version);
			System.out.println("M_GMIRT MOD Updated : " + modUpdated);

			try {
				String formattedDate = new SimpleDateFormat("dd-MMM-yyyy").format(reportDate);
				jdbcTemplate.update("BEGIN BRRS_M_GMIRT_SUMMARY_PROCEDURE(?); END;", formattedDate);
				System.out.println("M_GMIRT procedure executed for " + formattedDate);
			} catch (org.springframework.jdbc.BadSqlGrammarException ex) {
				System.out.println("M_GMIRT procedure not found — falling back to inline recompute");
				recomputeGMIRTComputedFields(reportDate);
			}

			System.out.println("M_GMIRT Verify + Transfer completed for " + reportDate);

		} catch (Exception ex) {
			System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.err.println("M_GMIRT transfer FAILED");
			System.err.println("Exception class : " + ex.getClass().getName());
			System.err.println("Exception msg   : " + ex.getMessage());
			if (ex.getCause() != null) {
				System.err.println(
						"Cause           : " + ex.getCause().getClass().getName() + " — " + ex.getCause().getMessage());
			}
			System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			ex.printStackTrace();
			throw ex;
		}

		System.out.println("================ M_GMIRT TRANSFER END ==================");
	}

	private void recomputeGMIRTComputedFields(java.sql.Date reportDate) {
		try {

			// R9_TOT_CAP_REQ comes from M_FXR (MCBL — sum of all R29_CAPITAL_CHARGE for the
			// date)
			BigDecimal r9Tot = jdbcTemplate
					.queryForObject("SELECT NVL(SUM(R29_CAPITAL_CHARGE),0) FROM BRRS_M_FXR_SUMMARYTABLE "
							+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?)", BigDecimal.class, reportDate);

			// Recompute:
			// - R12_<CCY> = R9_<CCY> + R10_<CCY> + R11_<CCY> (per currency)
			// - R12_TOT_CAP_REQ = R9_TOT_CAP_REQ + R10_TOT_CAP_REQ + R11_TOT_CAP_REQ
			// (R9_TOT is the FXR-derived value above; R10/R11 come from the row)
			jdbcTemplate.update("UPDATE BRRS_M_GMIRT_SUMMARYTABLE SET " + "  R9_TOT_CAP_REQ  = ?, "
					+ "  R12_PULA        = NVL(R9_PULA,0)     + NVL(R10_PULA,0)     + NVL(R11_PULA,0), "
					+ "  R12_USD         = NVL(R9_USD,0)      + NVL(R10_USD,0)      + NVL(R11_USD,0), "
					+ "  R12_ZAR         = NVL(R9_ZAR,0)      + NVL(R10_ZAR,0)      + NVL(R11_ZAR,0), "
					+ "  R12_GBP         = NVL(R9_GBP,0)      + NVL(R10_GBP,0)      + NVL(R11_GBP,0), "
					+ "  R12_EURO        = NVL(R9_EURO,0)     + NVL(R10_EURO,0)     + NVL(R11_EURO,0), "
					+ "  R12_JPY         = NVL(R9_JPY,0)      + NVL(R10_JPY,0)      + NVL(R11_JPY,0), "
					+ "  R12_RUPEE       = NVL(R9_RUPEE,0)    + NVL(R10_RUPEE,0)    + NVL(R11_RUPEE,0), "
					+ "  R12_RENMINBI    = NVL(R9_RENMINBI,0) + NVL(R10_RENMINBI,0) + NVL(R11_RENMINBI,0), "
					+ "  R12_OTHER       = NVL(R9_OTHER,0)    + NVL(R10_OTHER,0)    + NVL(R11_OTHER,0), "
					+ "  R12_TOT_CAP_REQ = ? + NVL(R10_TOT_CAP_REQ,0) + NVL(R11_TOT_CAP_REQ,0) "
					+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?)", r9Tot, r9Tot, reportDate);

			System.out.println("M_GMIRT inline recompute — R9_TOT=" + r9Tot);

		} catch (Exception e) {
			// Loud failure — this is a data-integrity concern, not an optional feature.
			System.err.println("M_GMIRT inline recompute FAILED for " + reportDate + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	// =========================================================
	// M_SRWA_12E – Checker transfer (MOD -> LIVE Summary + Detail)
	// =========================================================
	private void transferMSRWA12EData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12E_MOD_SUMMARYTABLE";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_SRWA_12E record available for verification.");
		}

		System.out.println("M_SRWA_12E Report Date : " + reportDate);

		// 1. Update BRRS_M_SRWA_12E_SUMMARYTABLE from MOD table
		String updateSummarySql = "UPDATE BRRS_M_SRWA_12E_SUMMARYTABLE M " + "SET ("
				+ "    R13_PRODUCT, R13_PERFORMING_EXPOSURE, R13_NON_PERFORMING, R13_SPECIFIC_PROV, R13_UNSECURED_PORTION_NPL, R13_TOTAL, "
				+ "    R14_PRODUCT, R14_PERFORMING_EXPOSURE, R14_NON_PERFORMING, R14_SPECIFIC_PROV, R14_UNSECURED_PORTION_NPL, R14_TOTAL, "
				+ "    R15_PRODUCT, R15_PERFORMING_EXPOSURE, R15_NON_PERFORMING, R15_SPECIFIC_PROV, R15_UNSECURED_PORTION_NPL, R15_TOTAL, "
				+ "    R16_PRODUCT, R16_PERFORMING_EXPOSURE, R16_NON_PERFORMING, R16_SPECIFIC_PROV, R16_UNSECURED_PORTION_NPL, R16_TOTAL, "
				+ "    R17_PRODUCT, R17_PERFORMING_EXPOSURE, R17_NON_PERFORMING, R17_SPECIFIC_PROV, R17_UNSECURED_PORTION_NPL, R17_TOTAL, "
				+ "    R18_PRODUCT, R18_PERFORMING_EXPOSURE, R18_NON_PERFORMING, R18_SPECIFIC_PROV, R18_UNSECURED_PORTION_NPL, R18_TOTAL, "
				+ "    R19_PRODUCT, R19_PERFORMING_EXPOSURE, R19_NON_PERFORMING, R19_SPECIFIC_PROV, R19_UNSECURED_PORTION_NPL, R19_TOTAL, "
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				+ "        S.R13_PRODUCT, S.R13_PERFORMING_EXPOSURE, S.R13_NON_PERFORMING, S.R13_SPECIFIC_PROV, S.R13_UNSECURED_PORTION_NPL, S.R13_TOTAL, "
				+ "        S.R14_PRODUCT, S.R14_PERFORMING_EXPOSURE, S.R14_NON_PERFORMING, S.R14_SPECIFIC_PROV, S.R14_UNSECURED_PORTION_NPL, S.R14_TOTAL, "
				+ "        S.R15_PRODUCT, S.R15_PERFORMING_EXPOSURE, S.R15_NON_PERFORMING, S.R15_SPECIFIC_PROV, S.R15_UNSECURED_PORTION_NPL, S.R15_TOTAL, "
				+ "        S.R16_PRODUCT, S.R16_PERFORMING_EXPOSURE, S.R16_NON_PERFORMING, S.R16_SPECIFIC_PROV, S.R16_UNSECURED_PORTION_NPL, S.R16_TOTAL, "
				+ "        S.R17_PRODUCT, S.R17_PERFORMING_EXPOSURE, S.R17_NON_PERFORMING, S.R17_SPECIFIC_PROV, S.R17_UNSECURED_PORTION_NPL, S.R17_TOTAL, "
				+ "        S.R18_PRODUCT, S.R18_PERFORMING_EXPOSURE, S.R18_NON_PERFORMING, S.R18_SPECIFIC_PROV, S.R18_UNSECURED_PORTION_NPL, S.R18_TOTAL, "
				+ "        S.R19_PRODUCT, S.R19_PERFORMING_EXPOSURE, S.R19_NON_PERFORMING, S.R19_SPECIFIC_PROV, S.R19_UNSECURED_PORTION_NPL, S.R19_TOTAL, "
				+ "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_SRWA_12E_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);

		if (summaryUpdated == 0) {
			throw new IllegalStateException("M_SRWA_12E modification record not found for date : " + reportDate);
		}

		System.out.println("M_SRWA_12E Summary Table Updated : " + summaryUpdated);

		// 2. Update BRRS_M_SRWA_12E_DETAILTABLE from MOD table
		String updateDetailSql = "UPDATE BRRS_M_SRWA_12E_DETAILTABLE M " + "SET ("
				+ "    R13_PRODUCT, R13_PERFORMING_EXPOSURE, R13_NON_PERFORMING, R13_SPECIFIC_PROV, R13_UNSECURED_PORTION_NPL, R13_TOTAL, "
				+ "    R14_PRODUCT, R14_PERFORMING_EXPOSURE, R14_NON_PERFORMING, R14_SPECIFIC_PROV, R14_UNSECURED_PORTION_NPL, R14_TOTAL, "
				+ "    R15_PRODUCT, R15_PERFORMING_EXPOSURE, R15_NON_PERFORMING, R15_SPECIFIC_PROV, R15_UNSECURED_PORTION_NPL, R15_TOTAL, "
				+ "    R16_PRODUCT, R16_PERFORMING_EXPOSURE, R16_NON_PERFORMING, R16_SPECIFIC_PROV, R16_UNSECURED_PORTION_NPL, R16_TOTAL, "
				+ "    R17_PRODUCT, R17_PERFORMING_EXPOSURE, R17_NON_PERFORMING, R17_SPECIFIC_PROV, R17_UNSECURED_PORTION_NPL, R17_TOTAL, "
				+ "    R18_PRODUCT, R18_PERFORMING_EXPOSURE, R18_NON_PERFORMING, R18_SPECIFIC_PROV, R18_UNSECURED_PORTION_NPL, R18_TOTAL, "
				+ "    R19_PRODUCT, R19_PERFORMING_EXPOSURE, R19_NON_PERFORMING, R19_SPECIFIC_PROV, R19_UNSECURED_PORTION_NPL, R19_TOTAL, "
				+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
				+ "        S.R13_PRODUCT, S.R13_PERFORMING_EXPOSURE, S.R13_NON_PERFORMING, S.R13_SPECIFIC_PROV, S.R13_UNSECURED_PORTION_NPL, S.R13_TOTAL, "
				+ "        S.R14_PRODUCT, S.R14_PERFORMING_EXPOSURE, S.R14_NON_PERFORMING, S.R14_SPECIFIC_PROV, S.R14_UNSECURED_PORTION_NPL, S.R14_TOTAL, "
				+ "        S.R15_PRODUCT, S.R15_PERFORMING_EXPOSURE, S.R15_NON_PERFORMING, S.R15_SPECIFIC_PROV, S.R15_UNSECURED_PORTION_NPL, S.R15_TOTAL, "
				+ "        S.R16_PRODUCT, S.R16_PERFORMING_EXPOSURE, S.R16_NON_PERFORMING, S.R16_SPECIFIC_PROV, S.R16_UNSECURED_PORTION_NPL, S.R16_TOTAL, "
				+ "        S.R17_PRODUCT, S.R17_PERFORMING_EXPOSURE, S.R17_NON_PERFORMING, S.R17_SPECIFIC_PROV, S.R17_UNSECURED_PORTION_NPL, S.R17_TOTAL, "
				+ "        S.R18_PRODUCT, S.R18_PERFORMING_EXPOSURE, S.R18_NON_PERFORMING, S.R18_SPECIFIC_PROV, S.R18_UNSECURED_PORTION_NPL, S.R18_TOTAL, "
				+ "        S.R19_PRODUCT, S.R19_PERFORMING_EXPOSURE, S.R19_NON_PERFORMING, S.R19_SPECIFIC_PROV, S.R19_UNSECURED_PORTION_NPL, S.R19_TOTAL, "
				+ "        S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_SRWA_12E_MOD_SUMMARYTABLE S "
				+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)" + ") "
				+ "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
		System.out.println("M_SRWA_12E Detail Table Updated : " + detailUpdated);

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE BRRS_M_SRWA_12E_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_SRWA_12E MOD Updated : " + modUpdated);
		System.out.println("M_SRWA_12E Verification and Transfer Completed Successfully");
	}

	// =========================================================
	// M_SRWA_12B – Checker transfer (MOD -> LIVE Summary + Detail)
	// =========================================================
	private void transferMSRWA12BData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM ("
				+ "    SELECT REPORT_DATE FROM BRRS_M_SRWA_12B_MOD_SUMMARYTABLE1 WHERE MODIFY_FLG = 'Y' "
				+ "    UNION ALL "
				+ "    SELECT REPORT_DATE FROM BRRS_M_SRWA_12B_MOD_SUMMARYTABLE2 WHERE MODIFY_FLG = 'Y' "
				+ "    UNION ALL "
				+ "    SELECT REPORT_DATE FROM BRRS_M_SRWA_12B_MOD_SUMMARYTABLE3 WHERE MODIFY_FLG = 'Y' "
				+ "    UNION ALL "
				+ "    SELECT REPORT_DATE FROM BRRS_M_SRWA_12B_MOD_SUMMARYTABLE4 WHERE MODIFY_FLG = 'Y' "
				+ "    UNION ALL "
				+ "    SELECT REPORT_DATE FROM BRRS_M_SRWA_12B_MOD_SUMMARYTABLE5 WHERE MODIFY_FLG = 'Y' "
				+ "    UNION ALL "
				+ "    SELECT REPORT_DATE FROM BRRS_M_SRWA_12B_MOD_SUMMARYTABLE6 WHERE MODIFY_FLG = 'Y' "
				+ "    UNION ALL "
				+ "    SELECT REPORT_DATE FROM BRRS_M_SRWA_12B_MOD_SUMMARYTABLE7 WHERE MODIFY_FLG = 'Y' "
				+ "    UNION ALL "
				+ "    SELECT REPORT_DATE FROM BRRS_M_SRWA_12B_MOD_SUMMARYTABLE8 WHERE MODIFY_FLG = 'Y' " + ")";
		java.sql.Date reportDate = null;
		try {
			reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
		} catch (Exception e) {
			// fallback
		}
		if (reportDate == null) {
			String fallbackSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12B_MOD_SUMMARYTABLE1";
			reportDate = jdbcTemplate.queryForObject(fallbackSql, java.sql.Date.class);
		}

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_SRWA_12B record available for verification.");
		}

		System.out.println("M_SRWA_12B Report Date : " + reportDate);

		transferMSRWA12BShard(1, BRRS_M_SRWA_12B_ReportService.R_COLS_1, reportDate);
		transferMSRWA12BShard(2, BRRS_M_SRWA_12B_ReportService.R_COLS_2, reportDate);
		transferMSRWA12BShard(3, BRRS_M_SRWA_12B_ReportService.R_COLS_3, reportDate);
		transferMSRWA12BShard(4, BRRS_M_SRWA_12B_ReportService.R_COLS_4, reportDate);
		transferMSRWA12BShard(5, BRRS_M_SRWA_12B_ReportService.R_COLS_5, reportDate);
		transferMSRWA12BShard(6, BRRS_M_SRWA_12B_ReportService.R_COLS_6, reportDate);
		transferMSRWA12BShard(7, BRRS_M_SRWA_12B_ReportService.R_COLS_7, reportDate);
		transferMSRWA12BShard(8, BRRS_M_SRWA_12B_ReportService.R_COLS_8, reportDate);

		System.out.println("M_SRWA_12B Verification and Transfer Completed Successfully for all 8 shards.");
	}

	private void transferMSRWA12BShard(int shard, String cols, java.sql.Date reportDate) {
		String modTable = "BRRS_M_SRWA_12B_MOD_SUMMARYTABLE" + shard;
		String sumTable = "BRRS_M_SRWA_12B_SUMMARYTABLE" + shard;
		String detTable = "BRRS_M_SRWA_12B_DETAILTABLE" + shard;

		Integer modCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM " + modTable + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)", Integer.class, reportDate);
		if (modCount == null || modCount == 0) {
			System.out.println("No MOD record found in " + modTable + " for date: " + reportDate);
			return;
		}

		String[] colArr = cols.split(",");
		StringBuilder sCols = new StringBuilder();
		for (int i = 0; i < colArr.length; i++) {
			if (i > 0)
				sCols.append(", ");
			sCols.append("S.").append(colArr[i].trim());
		}

		// 1. Update Live Summary Table
		String updateSummarySql = "UPDATE " + sumTable + " M SET (" + cols + ", ENTITY_FLG, MODIFY_FLG, DEL_FLG) = ("
				+ " SELECT " + sCols.toString() + ", 'Y', 'N', 'N' FROM " + modTable + " S"
				+ " WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE))" + " WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int summaryUpdated = jdbcTemplate.update(updateSummarySql, reportDate);
		System.out.println("M_SRWA_12B Shard " + shard + " Summary Table Updated : " + summaryUpdated);

		// 2. Update Live Detail Table (or insert if not present)
		Integer detailCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM " + detTable + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)", Integer.class, reportDate);

		if (detailCount != null && detailCount > 0) {
			String updateDetailSql = "UPDATE " + detTable + " M SET (" + cols + ", ENTITY_FLG, MODIFY_FLG, DEL_FLG) = ("
					+ " SELECT " + sCols.toString() + ", 'Y', 'N', 'N' FROM " + modTable + " S"
					+ " WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE))" + " WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
			int detailUpdated = jdbcTemplate.update(updateDetailSql, reportDate);
			System.out.println("M_SRWA_12B Shard " + shard + " Detail Table Updated : " + detailUpdated);
		} else {
			String insertDetailSql = "INSERT INTO " + detTable + " (REPORT_DATE, " + cols
					+ ", REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG)"
					+ " SELECT S.REPORT_DATE, " + sCols.toString()
					+ ", S.REPORT_VERSION, S.REPORT_FREQUENCY, S.REPORT_CODE, S.REPORT_DESC, 'Y', 'N', 'N'" + " FROM "
					+ modTable + " S WHERE TRUNC(S.REPORT_DATE) = TRUNC(?)";
			int detailInserted = jdbcTemplate.update(insertDetailSql, reportDate);
			System.out.println("M_SRWA_12B Shard " + shard + " Detail Table Inserted : " + detailInserted);
		}

		// 3. Mark MOD record status as approved (MODIFY_FLG = 'N')
		String updateModSql = "UPDATE " + modTable
				+ " SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		jdbcTemplate.update(updateModSql, reportDate);
		System.out.println("M_SRWA_12B Shard " + shard + " MOD Record Marked Approved (MODIFY_FLG='N')");
	}

	// =========================================================
	// M_SEC — Checker transfer (MOD -> LIVE Summary + Detail)
	// =========================================================
	private void transferMSecData(AuditServicesEntity auditRecord) {
		// 1. Find the pending report date from any of the M_SEC MOD tables
		String dateSql = "SELECT MAX(REPORT_DATE) FROM ("
				+ "    SELECT REPORT_DATE FROM BRRS_M_SEC_MOD_SUMMARYTABLE1 WHERE MODIFY_FLG = 'Y' " + "    UNION ALL "
				+ "    SELECT REPORT_DATE FROM BRRS_M_SEC_MOD_SUMMARYTABLE2 WHERE MODIFY_FLG = 'Y' " + "    UNION ALL "
				+ "    SELECT REPORT_DATE FROM BRRS_M_SEC_MOD_SUMMARYTABLE3 WHERE MODIFY_FLG = 'Y' " + "    UNION ALL "
				+ "    SELECT REPORT_DATE FROM BRRS_M_SEC_MOD_SUMMARYTABLE4 WHERE MODIFY_FLG = 'Y' " + ")";
		java.sql.Date reportDate = null;
		try {
			reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);
		} catch (Exception e) {
			// fallback
		}

		if (reportDate == null) {
			String fallbackSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SEC_MOD_SUMMARYTABLE1";
			try {
				reportDate = jdbcTemplate.queryForObject(fallbackSql, java.sql.Date.class);
			} catch (Exception e) {
				// ignore
			}
		}

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_SEC record available for verification.");
		}

		System.out.println("M_SEC Report Date : " + reportDate);

		// 1. Transfer Table 1: Summary & Detail
		try {
			String updateSummary1 = "UPDATE BRRS_M_SEC_SUMMARYTABLE1 M SET ("
					+ "    R11_PRODUCT, R11_TCA, R12_PRODUCT, R12_TCA, R13_PRODUCT, R13_TCA, "
					+ "    R14_PRODUCT, R14_TCA, R15_PRODUCT, R15_TCA, R16_PRODUCT, R16_TCA, "
					+ "    R17_PRODUCT, R17_TCA, R18_PRODUCT, R18_TCA, R19_PRODUCT, R19_TCA, "
					+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = ("
					+ "    SELECT S.R11_PRODUCT, S.R11_TCA, S.R12_PRODUCT, S.R12_TCA, S.R13_PRODUCT, S.R13_TCA, "
					+ "           S.R14_PRODUCT, S.R14_TCA, S.R15_PRODUCT, S.R15_TCA, S.R16_PRODUCT, S.R16_TCA, "
					+ "           S.R17_PRODUCT, S.R17_TCA, S.R18_PRODUCT, S.R18_TCA, S.R19_PRODUCT, S.R19_TCA, "
					+ "           S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_SEC_MOD_SUMMARYTABLE1 S "
					+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
					+ ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
			int s1 = jdbcTemplate.update(updateSummary1, reportDate);
			System.out.println("M_SEC Summary1 Updated : " + s1);

			String updateDetail1 = "UPDATE BRRS_M_SEC_DETAILTABLE1 M SET ("
					+ "    R11_PRODUCT, R11_TCA, R12_PRODUCT, R12_TCA, R13_PRODUCT, R13_TCA, "
					+ "    R14_PRODUCT, R14_TCA, R15_PRODUCT, R15_TCA, R16_PRODUCT, R16_TCA, "
					+ "    R17_PRODUCT, R17_TCA, R18_PRODUCT, R18_TCA, R19_PRODUCT, R19_TCA, "
					+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = ("
					+ "    SELECT S.R11_PRODUCT, S.R11_TCA, S.R12_PRODUCT, S.R12_TCA, S.R13_PRODUCT, S.R13_TCA, "
					+ "           S.R14_PRODUCT, S.R14_TCA, S.R15_PRODUCT, S.R15_TCA, S.R16_PRODUCT, S.R16_TCA, "
					+ "           S.R17_PRODUCT, S.R17_TCA, S.R18_PRODUCT, S.R18_TCA, S.R19_PRODUCT, S.R19_TCA, "
					+ "           S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_SEC_MOD_SUMMARYTABLE1 S "
					+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
					+ ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
			int d1 = jdbcTemplate.update(updateDetail1, reportDate);
			System.out.println("M_SEC Detail1 Updated : " + d1);
		} catch (Exception e) {
			System.err.println("Note on M_SEC Table 1 transfer: " + e.getMessage());
		}

		// 2. Transfer Table 2: Summary & Detail
		try {
			String updateSummary2 = "UPDATE BRRS_M_SEC_SUMMARYTABLE2 M SET ("
					+ "    R11_PRODUCT, R11_TCA2, R12_PRODUCT, R12_TCA2, R13_PRODUCT, R13_TCA2, "
					+ "    R14_PRODUCT, R14_TCA2, R15_PRODUCT, R15_TCA2, R16_PRODUCT, R16_TCA2, "
					+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = ("
					+ "    SELECT S.R11_PRODUCT, S.R11_TCA2, S.R12_PRODUCT, S.R12_TCA2, S.R13_PRODUCT, S.R13_TCA2, "
					+ "           S.R14_PRODUCT, S.R14_TCA2, S.R15_PRODUCT, S.R15_TCA2, S.R16_PRODUCT, S.R16_TCA2, "
					+ "           S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_SEC_MOD_SUMMARYTABLE2 S "
					+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
					+ ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
			int s2 = jdbcTemplate.update(updateSummary2, reportDate);
			System.out.println("M_SEC Summary2 Updated : " + s2);

			String updateDetail2 = "UPDATE BRRS_M_SEC_DETAILTABLE2 M SET ("
					+ "    R11_PRODUCT, R11_TCA2, R12_PRODUCT, R12_TCA2, R13_PRODUCT, R13_TCA2, "
					+ "    R14_PRODUCT, R14_TCA2, R15_PRODUCT, R15_TCA2, R16_PRODUCT, R16_TCA2, "
					+ "    ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = ("
					+ "    SELECT S.R11_PRODUCT, S.R11_TCA2, S.R12_PRODUCT, S.R12_TCA2, S.R13_PRODUCT, S.R13_TCA2, "
					+ "           S.R14_PRODUCT, S.R14_TCA2, S.R15_PRODUCT, S.R15_TCA2, S.R16_PRODUCT, S.R16_TCA2, "
					+ "           S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "    FROM BRRS_M_SEC_MOD_SUMMARYTABLE2 S "
					+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
					+ ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
			int d2 = jdbcTemplate.update(updateDetail2, reportDate);
			System.out.println("M_SEC Detail2 Updated : " + d2);
		} catch (Exception e) {
			System.err.println("Note on M_SEC Table 2 transfer: " + e.getMessage());
		}

		// Helper string for Table 3 columns (R26 to R31)
		String cols3 = "R26_PRODUCT, R26_0_1Y_FT, R26_0_1Y_HTM, R26_0_1Y_TOTAL, R26_1_5Y_FT, R26_1_5Y_HTM, R26_1_5Y_TOTAL, R26_O5Y_FT, R26_O5Y_HTM, R26_O5Y_TOTAL, R26_T_FT, R26_T_HTM, R26_T_TOTAL, "
				+ "R27_PRODUCT, R27_0_1Y_FT, R27_0_1Y_HTM, R27_0_1Y_TOTAL, R27_1_5Y_FT, R27_1_5Y_HTM, R27_1_5Y_TOTAL, R27_O5Y_FT, R27_O5Y_HTM, R27_O5Y_TOTAL, R27_T_FT, R27_T_HTM, R27_T_TOTAL, "
				+ "R28_PRODUCT, R28_0_1Y_FT, R28_0_1Y_HTM, R28_0_1Y_TOTAL, R28_1_5Y_FT, R28_1_5Y_HTM, R28_1_5Y_TOTAL, R28_O5Y_FT, R28_O5Y_HTM, R28_O5Y_TOTAL, R28_T_FT, R28_T_HTM, R28_T_TOTAL, "
				+ "R29_PRODUCT, R29_0_1Y_FT, R29_0_1Y_HTM, R29_0_1Y_TOTAL, R29_1_5Y_FT, R29_1_5Y_HTM, R29_1_5Y_TOTAL, R29_O5Y_FT, R29_O5Y_HTM, R29_O5Y_TOTAL, R29_T_FT, R29_T_HTM, R29_T_TOTAL, "
				+ "R30_PRODUCT, R30_0_1Y_FT, R30_0_1Y_HTM, R30_0_1Y_TOTAL, R30_1_5Y_FT, R30_1_5Y_HTM, R30_1_5Y_TOTAL, R30_O5Y_FT, R30_O5Y_HTM, R30_O5Y_TOTAL, R30_T_FT, R30_T_HTM, R30_T_TOTAL, "
				+ "R31_PRODUCT, R31_0_1Y_FT, R31_0_1Y_HTM, R31_0_1Y_TOTAL, R31_1_5Y_FT, R31_1_5Y_HTM, R31_1_5Y_TOTAL, R31_O5Y_FT, R31_O5Y_HTM, R31_O5Y_TOTAL, R31_T_FT, R31_T_HTM, R31_T_TOTAL";

		// 3. Transfer Table 3: Summary & Detail
		try {
			String updateSummary3 = "UPDATE BRRS_M_SEC_SUMMARYTABLE3 M SET (" + cols3
					+ ", ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
					+ cols3.replaceAll("(R\\w+)", "S.$1") + ", S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG "
					+ "    FROM BRRS_M_SEC_MOD_SUMMARYTABLE3 S "
					+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
					+ ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
			int s3 = jdbcTemplate.update(updateSummary3, reportDate);
			System.out.println("M_SEC Summary3 Updated : " + s3);

			String updateDetail3 = "UPDATE BRRS_M_SEC_DETAILTABLE3 M SET (" + cols3
					+ ", ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
					+ cols3.replaceAll("(R\\w+)", "S.$1") + ", S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG "
					+ "    FROM BRRS_M_SEC_MOD_SUMMARYTABLE3 S "
					+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
					+ ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
			int d3 = jdbcTemplate.update(updateDetail3, reportDate);
			System.out.println("M_SEC Detail3 Updated : " + d3);
		} catch (Exception e) {
			System.err.println("Note on M_SEC Table 3 transfer: " + e.getMessage());
		}

		// Helper string for Table 4 columns (R36 to R43)
		String cols4 = "R36_PRODUCT, R36_0_1Y_FT, R36_0_1Y_HTM, R36_0_1Y_TOTAL, R36_1_5Y_FT, R36_1_5Y_HTM, R36_1_5Y_TOTAL, R36_O5Y_FT, R36_O5Y_HTM, R36_O5Y_TOTAL, R36_T_FT, R36_T_HTM, R36_T_TOTAL, "
				+ "R37_PRODUCT, R37_0_1Y_FT, R37_0_1Y_HTM, R37_0_1Y_TOTAL, R37_1_5Y_FT, R37_1_5Y_HTM, R37_1_5Y_TOTAL, R37_O5Y_FT, R37_O5Y_HTM, R37_O5Y_TOTAL, R37_T_FT, R37_T_HTM, R37_T_TOTAL, "
				+ "R38_PRODUCT, R38_0_1Y_FT, R38_0_1Y_HTM, R38_0_1Y_TOTAL, R38_1_5Y_FT, R38_1_5Y_HTM, R38_1_5Y_TOTAL, R38_O5Y_FT, R38_O5Y_HTM, R38_O5Y_TOTAL, R38_T_FT, R38_T_HTM, R38_T_TOTAL, "
				+ "R39_PRODUCT, R39_0_1Y_FT, R39_0_1Y_HTM, R39_0_1Y_TOTAL, R39_1_5Y_FT, R39_1_5Y_HTM, R39_1_5Y_TOTAL, R39_O5Y_FT, R39_O5Y_HTM, R39_O5Y_TOTAL, R39_T_FT, R39_T_HTM, R39_T_TOTAL, "
				+ "R40_PRODUCT, R40_0_1Y_FT, R40_0_1Y_HTM, R40_0_1Y_TOTAL, R40_1_5Y_FT, R40_1_5Y_HTM, R40_1_5Y_TOTAL, R40_O5Y_FT, R40_O5Y_HTM, R40_O5Y_TOTAL, R40_T_FT, R40_T_HTM, R40_T_TOTAL, "
				+ "R41_PRODUCT, R41_0_1Y_FT, R41_0_1Y_HTM, R41_0_1Y_TOTAL, R41_1_5Y_FT, R41_1_5Y_HTM, R41_1_5Y_TOTAL, R41_O5Y_FT, R41_O5Y_HTM, R41_O5Y_TOTAL, R41_T_FT, R41_T_HTM, R41_T_TOTAL, "
				+ "R42_PRODUCT, R42_0_1Y_FT, R42_0_1Y_HTM, R42_0_1Y_TOTAL, R42_1_5Y_FT, R42_1_5Y_HTM, R42_1_5Y_TOTAL, R42_O5Y_FT, R42_O5Y_HTM, R42_O5Y_TOTAL, R42_T_FT, R42_T_HTM, R42_T_TOTAL, "
				+ "R43_PRODUCT, R43_0_1Y_FT, R43_0_1Y_HTM, R43_0_1Y_TOTAL, R43_1_5Y_FT, R43_1_5Y_HTM, R43_1_5Y_TOTAL, R43_O5Y_FT, R43_O5Y_HTM, R43_O5Y_TOTAL, R43_T_FT, R43_T_HTM, R43_T_TOTAL";

		// 4. Transfer Table 4: Summary & Detail
		try {
			String updateSummary4 = "UPDATE BRRS_M_SEC_SUMMARYTABLE4 M SET (" + cols4
					+ ", ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
					+ cols4.replaceAll("(R\\w+)", "S.$1") + ", S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG "
					+ "    FROM BRRS_M_SEC_MOD_SUMMARYTABLE4 S "
					+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
					+ ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
			int s4 = jdbcTemplate.update(updateSummary4, reportDate);
			System.out.println("M_SEC Summary4 Updated : " + s4);

			String updateDetail4 = "UPDATE BRRS_M_SEC_DETAILTABLE4 M SET (" + cols4
					+ ", ENTITY_FLG, MODIFY_FLG, DEL_FLG" + ") = (" + "    SELECT "
					+ cols4.replaceAll("(R\\w+)", "S.$1") + ", S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG "
					+ "    FROM BRRS_M_SEC_MOD_SUMMARYTABLE4 S "
					+ "    WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)"
					+ ") WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";
			int d4 = jdbcTemplate.update(updateDetail4, reportDate);
			System.out.println("M_SEC Detail4 Updated : " + d4);
		} catch (Exception e) {
			System.err.println("Note on M_SEC Table 4 transfer: " + e.getMessage());
		}

		// 5. Update MOD tables flags to approved (MODIFY_FLG = 'N')
		jdbcTemplate.update(
				"UPDATE BRRS_M_SEC_MOD_SUMMARYTABLE1 SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);
		jdbcTemplate.update(
				"UPDATE BRRS_M_SEC_MOD_SUMMARYTABLE2 SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);
		jdbcTemplate.update(
				"UPDATE BRRS_M_SEC_MOD_SUMMARYTABLE3 SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);
		jdbcTemplate.update(
				"UPDATE BRRS_M_SEC_MOD_SUMMARYTABLE4 SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
				reportDate);

		System.out.println("M_SEC Verification and Transfer Completed Successfully");
	}
	
	// =====================================================================
	// UFCE CALCULATION (MANUAL ENTRY)  -  CHECKER: MOD -> LIVE      << NEW >>
	// =====================================================================

	private static final String UFCE_LIVE_MANUAL = "BRRS_UFCE_CALCULATION_SUMMARYTABLE_MANUAL";
	private static final String UFCE_MOD_MANUAL = "BRRS_UFCE_CALCULATION_MOD_SUMMARYTABLE_MANUAL";

	/** Business columns promoted from MOD to LIVE (must match the Maker's MERGE). */
	private List<String> ufceManualColumns() {
		List<String> cols = new ArrayList<>();
		for (int i = 10; i <= 21; i++) {
			cols.add("R" + i + "_SERIAL_NO");
			cols.add("R" + i + "_SUBSIDIARY");
			cols.add("R" + i + "_TOTAL_ADV");
			cols.add("R" + i + "_PROVISION");
		}
		cols.add("R22_TOTAL_ADV");
		cols.add("R22_PROVISION");
		return cols;
	}

	/** Server-side Checker gate: CHECKER / C / MAN / MANAGER / B / ADMIN / A. */
	private void assertCheckerRole(String reportName) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attr == null)
			return; // internal (non-HTTP) call
		String userid = (String) attr.getRequest().getSession().getAttribute("USERID");
		String role = userid != null ? userProfileRep.getUserRole(userid) : null;
		String r = role == null ? "" : role.trim().toUpperCase();
		boolean ok = r.equals("CHECKER") || r.equals("C") || r.equals("MAN") || r.equals("MANAGER") || r.equals("B")
				|| r.equals("ADMIN") || r.equals("A");
		if (!ok) {
			throw new SecurityException("Only Checker (or MAN/ADMIN) can verify " + reportName
					+ " modifications. Current role: " + role);
		}
	}

	private void transferUFCEManualData(AuditServicesEntity auditRecord) {

		assertCheckerRole("UFCE");

		// 1) Which record? The Maker stores "yyyy-MM-dd|version" in change_details.
		java.sql.Date reportDate = null;
		BigDecimal reportVersion = null;
		String key = auditRecord.getChange_details();
		if (key != null && key.contains("|")) {
			try {
				String[] parts = key.split("\\|");
				reportDate = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(parts[0].trim()).getTime());
				reportVersion = new BigDecimal(parts[1].trim());
			} catch (Exception e) {
				logger.warn("UFCE verify: could not parse change_details '{}': {}", key, e.getMessage());
				reportDate = null;
				reportVersion = null;
			}
		}

		// Fallback (old audit rows): accept only if exactly ONE pending record exists
		if (reportDate == null || reportVersion == null) {
			List<Object[]> pending = jdbcTemplate.query(
					"SELECT REPORT_DATE, REPORT_VERSION FROM " + UFCE_MOD_MANUAL + " WHERE MODIFY_FLG = 'Y'",
					(rs, i) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
			if (pending.size() != 1) {
				throw new IllegalStateException("UFCE verify: expected exactly one pending modification but found "
						+ pending.size() + ". Audit change_details='" + key + "'");
			}
			reportDate = (java.sql.Date) pending.get(0)[0];
			reportVersion = (BigDecimal) pending.get(0)[1];
		}

		System.out.println("UFCE Verify -> date=" + reportDate + ", version=" + reportVersion);

		// 2) Must still be pending (also blocks double verification)
		Integer pendingCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + UFCE_MOD_MANUAL
				+ " WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ? AND MODIFY_FLG = 'Y'", Integer.class,
				reportDate, reportVersion);
		if (pendingCount == null || pendingCount == 0) {
			throw new IllegalStateException("No pending UFCE modification for date " + reportDate + " version "
					+ reportVersion + " (already verified?)");
		}

		// 3) MOD -> LIVE (update the version if it exists, otherwise create it)
		List<String> cols = ufceManualColumns();
		StringBuilder upd = new StringBuilder();
		StringBuilder insCols = new StringBuilder();
		StringBuilder insVals = new StringBuilder();
		for (String c : cols) {
			upd.append("d.").append(c).append(" = s.").append(c).append(", ");
			insCols.append(c).append(", ");
			insVals.append("s.").append(c).append(", ");
		}

		String mergeSql = "MERGE INTO " + UFCE_LIVE_MANUAL + " d " + "USING (SELECT * FROM " + UFCE_MOD_MANUAL
				+ " WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?) s "
				+ "ON (TRUNC(d.REPORT_DATE) = TRUNC(s.REPORT_DATE) AND d.REPORT_VERSION = s.REPORT_VERSION) "
				+ "WHEN MATCHED THEN UPDATE SET " + upd
				+ "d.REPORT_FREQUENCY = s.REPORT_FREQUENCY, d.REPORT_CODE = s.REPORT_CODE, d.REPORT_DESC = s.REPORT_DESC, "
				+ "d.ENTITY_FLG = 'Y', d.MODIFY_FLG = 'Y', d.DEL_FLG = 'N' "
				+ "WHEN NOT MATCHED THEN INSERT (REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, "
				+ insCols + "ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
				+ "VALUES (s.REPORT_DATE, s.REPORT_VERSION, s.REPORT_FREQUENCY, s.REPORT_CODE, s.REPORT_DESC, "
				+ insVals + "'Y', 'Y', 'N')";

		int merged = jdbcTemplate.update(mergeSql, reportDate, reportVersion);
		if (merged == 0) {
			throw new IllegalStateException(
					"UFCE modification record not found for date " + reportDate + " version " + reportVersion);
		}
		System.out.println("UFCE LIVE manual table updated: " + merged);

		// 4) Close the pending item
		int updated = jdbcTemplate.update("UPDATE " + UFCE_MOD_MANUAL
				+ " SET ENTITY_FLG = 'Y', MODIFY_FLG = 'N' WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?",
				reportDate, reportVersion);
		System.out.println("UFCE MOD marked verified: " + updated);
		System.out.println("UFCE Verification Completed Successfully");
	}

	private void transferMIrbData(AuditServicesEntity auditRecord) {
		String dateSql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_IRB_MOD_SUMMARYTABLE WHERE MODIFY_FLG = 'Y'";
		java.sql.Date reportDate = jdbcTemplate.queryForObject(dateSql, java.sql.Date.class);

		if (reportDate == null) {
			throw new IllegalStateException("No modified M_IRB record available for verification.");
		}

		System.out.println("M_IRB Report Date : " + reportDate);

		int[] rows = { 96, 100, 101, 102, 103, 104 };
		String[] suffix = { "PRODUCT", "UP_TO_1_MONTH", "MORE_THAN_1_MONTH_TO_3_MONTHS",
				"MORE_THAN_3_MONTH_TO_6_MONTHS", "MORE_THAN_6_MONTH_TO_12_MONTHS", "MORE_THAN_12_MONTH_TO_3_YEARS",
				"MORE_THAN_3_YEARS_TO_5_YEARS", "MORE_THAN_5_YEARS_TO_10_YEARS", "MORE_THAN_10_YEARS",
				"NON_RATIO_SENSATIVE_ITEMS", "TOTAL" };

		StringBuilder setCols = new StringBuilder();
		StringBuilder selCols = new StringBuilder();
		for (int row : rows) {
			for (String s : suffix) {
				setCols.append("R").append(row).append("_").append(s).append(", ");
				selCols.append("S.R").append(row).append("_").append(s).append(", ");
			}
		}

		String updateSql = "UPDATE BRRS_M_IRB_SUMMARYTABLE M SET (" + setCols + "ENTITY_FLG, MODIFY_FLG, DEL_FLG) = ("
				+ "SELECT " + selCols + "S.ENTITY_FLG, S.MODIFY_FLG, S.DEL_FLG " + "FROM BRRS_M_IRB_MOD_SUMMARYTABLE S "
				+ "WHERE TRUNC(S.REPORT_DATE) = TRUNC(M.REPORT_DATE)) " + "WHERE TRUNC(M.REPORT_DATE) = TRUNC(?)";

		int updated = jdbcTemplate.update(updateSql, reportDate);

		if (updated == 0) {
			throw new IllegalStateException("M_IRB modification record not found for date : " + reportDate);
		}

		System.out.println("M_IRB Main Updated : " + updated);

		String updateModSql = "UPDATE BRRS_M_IRB_MOD_SUMMARYTABLE SET ENTITY_FLG='Y', MODIFY_FLG='N' WHERE TRUNC(REPORT_DATE)=TRUNC(?)";
		int modUpdated = jdbcTemplate.update(updateModSql, reportDate);

		System.out.println("M_IRB MOD Updated : " + modUpdated);
	}

	private static Object[] appendArgs(Object[] base, Object extra) {
		Object[] out = java.util.Arrays.copyOf(base, base.length + 1);
		out[base.length] = extra;
		return out;
	}

}