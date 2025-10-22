package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "GENERAL_MASTER_TABLE")
public class GeneralMasterEntity {
	@Id
	private String	id;
	private String	mcbl_gl_code;
	private String	mcbl_gl_sub_code;
    @Column(name="MCBL_HEAD_ACC_NO")
	private String	mcbl_head_acc_no;
	private String	mcbl_description;
	private String	mcbl_currency;
	private BigDecimal	mcbl_debit_balance;
	private BigDecimal	mcbl_credit_balance;
	private BigDecimal	mcbl_debit_equivalent;
	private BigDecimal	mcbl_credit_equivalent;
	private String	sol_id;	
	private String	customer_id; 
	private String	customer_name; 
    @Column(name="ACCOUNT_NO")
	private String	account_no; 
	private String	gender; 
	private String	schm_code;
	private String	schm_desc;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date	acct_open_date;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date	acct_close_date;
	private BigDecimal	balance_as_on;
	private String	currency;
	private BigDecimal	bal_equi_to_bwp;
	private BigDecimal	rate_of_interest;
	private BigDecimal	hundred;
	private String	status;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date	maturity_date;
	private BigDecimal	maturity_amount;
	private String	gl_sub_head_code;
	private String	gl_sub_head_desc;
	private String	type_of_accounts;
	private String	segment;
	private String	period;
	private BigDecimal	effective_interest_rate;
	private BigDecimal	amount_deposited;
	private BigDecimal	outstanding_balance;
	private BigDecimal	oustndng_bal_ugx;
	private String	scheme;
	private BigDecimal	cr_pref_int_rate;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date	reference_date;
	private BigDecimal	difference;
	private BigDecimal	days;
	private BigDecimal	approved_limit;
	private BigDecimal	sanction_limit;
	private BigDecimal	disbursed_amt;
	private BigDecimal	accrued_int_amt;
	private BigDecimal	monthly_interest;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date	last_interest_debit_date;
	private String	acct_cls_flg;
	private String	classification_code;
	private String	constitution_code;
	private BigDecimal	tenor_month;
	private BigDecimal	emi;
	private String	facility;
	private String	past_due;
	private BigDecimal	past_due_days;
	private String	asset;
	private BigDecimal	provision;
	private String	unsecured;
	private String	int_bucket;
	private String	staff;
	private String	smme;
	private String	labod;
	private String	new_ac;
	private BigDecimal	undrawn;
	private String	sector;
	private String	stage;
	private BigDecimal	ecl_provision;
	private BigDecimal	period_days;
	private BigDecimal	mat_bucket;
	private String	branch_name;
	private String	branch_code;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="REPORT_DATE")
	private Date	report_date;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date	entry_date;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date	modify_date;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date	verify_date;
	private String	entry_user;
	private String	modify_user;
	private String	verify_user;
	private String	entry_flg;
	private String	modify_flg;
	private String	verify_flg;
	private String	del_flg;
	private String	mcbl_flg;
	private String	blbf_flg;
	private String	bdgf_flg;
	private String	bfdb_flg;
	

	public String getMcbl_flg() {
		return mcbl_flg;
	}
	public void setMcbl_flg(String mcbl_flg) {
		this.mcbl_flg = mcbl_flg;
	}
	public String getBlbf_flg() {
		return blbf_flg;
	}
	public void setBlbf_flg(String blbf_flg) {
		this.blbf_flg = blbf_flg;
	}
	public String getBdgf_flg() {
		return bdgf_flg;
	}
	public void setBdgf_flg(String bdgf_flg) {
		this.bdgf_flg = bdgf_flg;
	}
	public String getBfdb_flg() {
		return bfdb_flg;
	}
	public void setBfdb_flg(String bfdb_flg) {
		this.bfdb_flg = bfdb_flg;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMcbl_gl_code() {
		return mcbl_gl_code;
	}
	public void setMcbl_gl_code(String mcbl_gl_code) {
		this.mcbl_gl_code = mcbl_gl_code;
	}
	public String getMcbl_gl_sub_code() {
		return mcbl_gl_sub_code;
	}
	public void setMcbl_gl_sub_code(String mcbl_gl_sub_code) {
		this.mcbl_gl_sub_code = mcbl_gl_sub_code;
	}
	public String getMcbl_head_acc_no() {
		return mcbl_head_acc_no;
	}
	public void setMcbl_head_acc_no(String mcbl_head_acc_no) {
		this.mcbl_head_acc_no = mcbl_head_acc_no;
	}
	public String getMcbl_description() {
		return mcbl_description;
	}
	public void setMcbl_description(String mcbl_description) {
		this.mcbl_description = mcbl_description;
	}
	public String getMcbl_currency() {
		return mcbl_currency;
	}
	public void setMcbl_currency(String mcbl_currency) {
		this.mcbl_currency = mcbl_currency;
	}
	public BigDecimal getMcbl_debit_balance() {
		return mcbl_debit_balance;
	}
	public void setMcbl_debit_balance(BigDecimal mcbl_debit_balance) {
		this.mcbl_debit_balance = mcbl_debit_balance;
	}
	public BigDecimal getMcbl_credit_balance() {
		return mcbl_credit_balance;
	}
	public void setMcbl_credit_balance(BigDecimal mcbl_credit_balance) {
		this.mcbl_credit_balance = mcbl_credit_balance;
	}
	public BigDecimal getMcbl_debit_equivalent() {
		return mcbl_debit_equivalent;
	}
	public void setMcbl_debit_equivalent(BigDecimal mcbl_debit_equivalent) {
		this.mcbl_debit_equivalent = mcbl_debit_equivalent;
	}
	public BigDecimal getMcbl_credit_equivalent() {
		return mcbl_credit_equivalent;
	}
	public void setMcbl_credit_equivalent(BigDecimal mcbl_credit_equivalent) {
		this.mcbl_credit_equivalent = mcbl_credit_equivalent;
	}
	public String getSol_id() {
		return sol_id;
	}
	public void setSol_id(String sol_id) {
		this.sol_id = sol_id;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getAccount_no() {
		return account_no;
	}
	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSchm_code() {
		return schm_code;
	}
	public void setSchm_code(String schm_code) {
		this.schm_code = schm_code;
	}
	public String getSchm_desc() {
		return schm_desc;
	}
	public void setSchm_desc(String schm_desc) {
		this.schm_desc = schm_desc;
	}
	public Date getAcct_open_date() {
		return acct_open_date;
	}
	public void setAcct_open_date(Date acct_open_date) {
		this.acct_open_date = acct_open_date;
	}
	public Date getAcct_close_date() {
		return acct_close_date;
	}
	public void setAcct_close_date(Date acct_close_date) {
		this.acct_close_date = acct_close_date;
	}
	public BigDecimal getBalance_as_on() {
		return balance_as_on;
	}
	public void setBalance_as_on(BigDecimal balance_as_on) {
		this.balance_as_on = balance_as_on;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getBal_equi_to_bwp() {
		return bal_equi_to_bwp;
	}
	public void setBal_equi_to_bwp(BigDecimal bal_equi_to_bwp) {
		this.bal_equi_to_bwp = bal_equi_to_bwp;
	}
	public BigDecimal getRate_of_interest() {
		return rate_of_interest;
	}
	public void setRate_of_interest(BigDecimal rate_of_interest) {
		this.rate_of_interest = rate_of_interest;
	}
	public BigDecimal getHundred() {
		return hundred;
	}
	public void setHundred(BigDecimal hundred) {
		this.hundred = hundred;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getMaturity_date() {
		return maturity_date;
	}
	public void setMaturity_date(Date maturity_date) {
		this.maturity_date = maturity_date;
	}
	public BigDecimal getMaturity_amount() {
		return maturity_amount;
	}
	public void setMaturity_amount(BigDecimal maturity_amount) {
		this.maturity_amount = maturity_amount;
	}
	public String getGl_sub_head_code() {
		return gl_sub_head_code;
	}
	public void setGl_sub_head_code(String gl_sub_head_code) {
		this.gl_sub_head_code = gl_sub_head_code;
	}
	public String getGl_sub_head_desc() {
		return gl_sub_head_desc;
	}
	public void setGl_sub_head_desc(String gl_sub_head_desc) {
		this.gl_sub_head_desc = gl_sub_head_desc;
	}
	public String getType_of_accounts() {
		return type_of_accounts;
	}
	public void setType_of_accounts(String type_of_accounts) {
		this.type_of_accounts = type_of_accounts;
	}
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public BigDecimal getEffective_interest_rate() {
		return effective_interest_rate;
	}
	public void setEffective_interest_rate(BigDecimal effective_interest_rate) {
		this.effective_interest_rate = effective_interest_rate;
	}
	public BigDecimal getAmount_deposited() {
		return amount_deposited;
	}
	public void setAmount_deposited(BigDecimal amount_deposited) {
		this.amount_deposited = amount_deposited;
	}
	public BigDecimal getOutstanding_balance() {
		return outstanding_balance;
	}
	public void setOutstanding_balance(BigDecimal outstanding_balance) {
		this.outstanding_balance = outstanding_balance;
	}
	public BigDecimal getOustndng_bal_ugx() {
		return oustndng_bal_ugx;
	}
	public void setOustndng_bal_ugx(BigDecimal oustndng_bal_ugx) {
		this.oustndng_bal_ugx = oustndng_bal_ugx;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public BigDecimal getCr_pref_int_rate() {
		return cr_pref_int_rate;
	}
	public void setCr_pref_int_rate(BigDecimal cr_pref_int_rate) {
		this.cr_pref_int_rate = cr_pref_int_rate;
	}
	public Date getReference_date() {
		return reference_date;
	}
	public void setReference_date(Date reference_date) {
		this.reference_date = reference_date;
	}
	public BigDecimal getDifference() {
		return difference;
	}
	public void setDifference(BigDecimal difference) {
		this.difference = difference;
	}
	public BigDecimal getDays() {
		return days;
	}
	public void setDays(BigDecimal days) {
		this.days = days;
	}
	public BigDecimal getApproved_limit() {
		return approved_limit;
	}
	public void setApproved_limit(BigDecimal approved_limit) {
		this.approved_limit = approved_limit;
	}
	public BigDecimal getSanction_limit() {
		return sanction_limit;
	}
	public void setSanction_limit(BigDecimal sanction_limit) {
		this.sanction_limit = sanction_limit;
	}
	public BigDecimal getDisbursed_amt() {
		return disbursed_amt;
	}
	public void setDisbursed_amt(BigDecimal disbursed_amt) {
		this.disbursed_amt = disbursed_amt;
	}
	public BigDecimal getAccrued_int_amt() {
		return accrued_int_amt;
	}
	public void setAccrued_int_amt(BigDecimal accrued_int_amt) {
		this.accrued_int_amt = accrued_int_amt;
	}
	public BigDecimal getMonthly_interest() {
		return monthly_interest;
	}
	public void setMonthly_interest(BigDecimal monthly_interest) {
		this.monthly_interest = monthly_interest;
	}
	public Date getLast_interest_debit_date() {
		return last_interest_debit_date;
	}
	public void setLast_interest_debit_date(Date last_interest_debit_date) {
		this.last_interest_debit_date = last_interest_debit_date;
	}
	public String getAcct_cls_flg() {
		return acct_cls_flg;
	}
	public void setAcct_cls_flg(String acct_cls_flg) {
		this.acct_cls_flg = acct_cls_flg;
	}
	public String getClassification_code() {
		return classification_code;
	}
	public void setClassification_code(String classification_code) {
		this.classification_code = classification_code;
	}
	public String getConstitution_code() {
		return constitution_code;
	}
	public void setConstitution_code(String constitution_code) {
		this.constitution_code = constitution_code;
	}
	public BigDecimal getTenor_month() {
		return tenor_month;
	}
	public void setTenor_month(BigDecimal tenor_month) {
		this.tenor_month = tenor_month;
	}
	public BigDecimal getEmi() {
		return emi;
	}
	public void setEmi(BigDecimal emi) {
		this.emi = emi;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public String getPast_due() {
		return past_due;
	}
	public void setPast_due(String past_due) {
		this.past_due = past_due;
	}
	public BigDecimal getPast_due_days() {
		return past_due_days;
	}
	public void setPast_due_days(BigDecimal past_due_days) {
		this.past_due_days = past_due_days;
	}
	public String getAsset() {
		return asset;
	}
	public void setAsset(String asset) {
		this.asset = asset;
	}
	public BigDecimal getProvision() {
		return provision;
	}
	public void setProvision(BigDecimal provision) {
		this.provision = provision;
	}
	public String getUnsecured() {
		return unsecured;
	}
	public void setUnsecured(String unsecured) {
		this.unsecured = unsecured;
	}
	public String getInt_bucket() {
		return int_bucket;
	}
	public void setInt_bucket(String int_bucket) {
		this.int_bucket = int_bucket;
	}
	public String getStaff() {
		return staff;
	}
	public void setStaff(String staff) {
		this.staff = staff;
	}
	public String getSmme() {
		return smme;
	}
	public void setSmme(String smme) {
		this.smme = smme;
	}
	public String getLabod() {
		return labod;
	}
	public void setLabod(String labod) {
		this.labod = labod;
	}
	public String getNew_ac() {
		return new_ac;
	}
	public void setNew_ac(String new_ac) {
		this.new_ac = new_ac;
	}
	public BigDecimal getUndrawn() {
		return undrawn;
	}
	public void setUndrawn(BigDecimal undrawn) {
		this.undrawn = undrawn;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public BigDecimal getEcl_provision() {
		return ecl_provision;
	}
	public void setEcl_provision(BigDecimal ecl_provision) {
		this.ecl_provision = ecl_provision;
	}
	public BigDecimal getPeriod_days() {
		return period_days;
	}
	public void setPeriod_days(BigDecimal period_days) {
		this.period_days = period_days;
	}
	public BigDecimal getMat_bucket() {
		return mat_bucket;
	}
	public void setMat_bucket(BigDecimal mat_bucket) {
		this.mat_bucket = mat_bucket;
	}
	public String getBranch_name() {
		return branch_name;
	}
	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}
	public String getBranch_code() {
		return branch_code;
	}
	public void setBranch_code(String branch_code) {
		this.branch_code = branch_code;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public Date getEntry_date() {
		return entry_date;
	}
	public void setEntry_date(Date entry_date) {
		this.entry_date = entry_date;
	}
	public Date getModify_date() {
		return modify_date;
	}
	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}
	public Date getVerify_date() {
		return verify_date;
	}
	public void setVerify_date(Date verify_date) {
		this.verify_date = verify_date;
	}
	public String getEntry_user() {
		return entry_user;
	}
	public void setEntry_user(String entry_user) {
		this.entry_user = entry_user;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public String getVerify_user() {
		return verify_user;
	}
	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}
	public String getEntry_flg() {
		return entry_flg;
	}
	public void setEntry_flg(String entry_flg) {
		this.entry_flg = entry_flg;
	}
	public String getModify_flg() {
		return modify_flg;
	}
	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
	}
	public String getVerify_flg() {
		return verify_flg;
	}
	public void setVerify_flg(String verify_flg) {
		this.verify_flg = verify_flg;
	}
	public String getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}

}
