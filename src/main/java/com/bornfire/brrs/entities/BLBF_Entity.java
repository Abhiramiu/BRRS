package com.bornfire.brrs.entities;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "BRRS_BLBF")
public class BLBF_Entity {

	
	private String	cust_id;
	private String	sol_id;
	
	@Id
	private String	account_no;
	private String	acct_name;
	private String	schm_code;
	private String	schm_desc;
    @Temporal(TemporalType.TIMESTAMP)
	private Date	acct_opn_date;
	private BigDecimal	approved_limit;
	private BigDecimal	sanction_limit;
	private BigDecimal	disbursed_amt;
	private BigDecimal	balance_as_on;
	private String	ccy;
	private BigDecimal	bal_equi_to_bwp;
	private BigDecimal	int_rate;
	private BigDecimal	hundred;
	private BigDecimal	accrued_int_amt;
	private BigDecimal	int_of_aug_25;
    @Temporal(TemporalType.TIMESTAMP)
	private Date	last_interest_debit_date;
	private String	acct_cls_flg;
    @Temporal(TemporalType.TIMESTAMP)
	private Date	close_date;
	private String	gender;
	private String	classification_code;
	private String	constitution_code;
    @Temporal(TemporalType.TIMESTAMP)
	private Date	maturity_date;
	private String	gl_sub_head_code;
	private String	gl_sub_head_desc;
	private BigDecimal	tenor_month;
	private BigDecimal	emi;
	private String	segment;
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
	private String	period;
	private BigDecimal	effective_interest_rate;
	private String	stage;
	private BigDecimal	ecl_provision;
	private String	branch_name;
	private String	branch_code;
    @Temporal(TemporalType.TIMESTAMP)
	private Date	report_date;
    @Temporal(TemporalType.TIMESTAMP)
	private Date	entry_date;
    @Temporal(TemporalType.TIMESTAMP)
	private Date	modify_date;
    @Temporal(TemporalType.TIMESTAMP)
	private Date	verify_date;
	private String	entry_user;
	private String	modify_user;
	private String	verify_user;
	private String	entry_flg;
	private String	modify_flg;
	private String	verify_flg;
	private String	del_flg;
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	public String getSol_id() {
		return sol_id;
	}
	public void setSol_id(String sol_id) {
		this.sol_id = sol_id;
	}
	public String getAccount_no() {
		return account_no;
	}
	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}
	public String getAcct_name() {
		return acct_name;
	}
	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
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
	public Date getAcct_opn_date() {
		return acct_opn_date;
	}
	public void setAcct_opn_date(Date acct_opn_date) {
		this.acct_opn_date = acct_opn_date;
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
	
	public BigDecimal getBalance_as_on() {
		return balance_as_on;
	}
	public void setBalance_as_on(BigDecimal balance_as_on) {
		this.balance_as_on = balance_as_on;
	}
	public String getCcy() {
		return ccy;
	}
	public void setCcy(String ccy) {
		this.ccy = ccy;
	}
	public BigDecimal getBal_equi_to_bwp() {
		return bal_equi_to_bwp;
	}
	public void setBal_equi_to_bwp(BigDecimal bal_equi_to_bwp) {
		this.bal_equi_to_bwp = bal_equi_to_bwp;
	}
	public BigDecimal getInt_rate() {
		return int_rate;
	}
	public void setInt_rate(BigDecimal int_rate) {
		this.int_rate = int_rate;
	}
	public BigDecimal getHundred() {
		return hundred;
	}
	public void setHundred(BigDecimal hundred) {
		this.hundred = hundred;
	}
	public BigDecimal getAccrued_int_amt() {
		return accrued_int_amt;
	}
	public void setAccrued_int_amt(BigDecimal accrued_int_amt) {
		this.accrued_int_amt = accrued_int_amt;
	}
	public BigDecimal getInt_of_aug_25() {
		return int_of_aug_25;
	}
	public void setInt_of_aug_25(BigDecimal int_of_aug_25) {
		this.int_of_aug_25 = int_of_aug_25;
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
	public Date getClose_date() {
		return close_date;
	}
	public void setClose_date(Date close_date) {
		this.close_date = close_date;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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
	public Date getMaturity_date() {
		return maturity_date;
	}
	public void setMaturity_date(Date maturity_date) {
		this.maturity_date = maturity_date;
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
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
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