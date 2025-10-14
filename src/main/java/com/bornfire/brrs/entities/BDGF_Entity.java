package com.bornfire.brrs.entities;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "BRRS_BDGF")
public class BDGF_Entity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bdgf_seq_gen")
	@SequenceGenerator(name = "bdgf_seq_gen", sequenceName = "BDGF_SEQ", allocationSize = 1)
	private BigDecimal	s_no;
	private String	sol_id;
	private String	acc_no;
	private String	customer_id;
	private String	customer_name;
    @Temporal(TemporalType.TIMESTAMP)
	private Date	open_date;
	private BigDecimal	amount_deposited;
	private String	currency;
	private String	period;
	private BigDecimal	rate_of_interest;
	private BigDecimal	hundred;
	private BigDecimal	bal_equi_to_bwp;
	private BigDecimal	outstanding_balance;
	private BigDecimal	oustndng_bal_ugx;
    @Temporal(TemporalType.TIMESTAMP)
	private Date	maturity_date;
	private BigDecimal	maturity_amount;
	private String	scheme;
	private BigDecimal	cr_pref_int_rate;
	private String	segment;
    @Temporal(TemporalType.TIMESTAMP)
	private Date	reference_date;
	private BigDecimal	difference;
	private BigDecimal	days;
	private BigDecimal	period_days;
	private BigDecimal	effective_int_rate;
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
	   private BigDecimal residual_tenure;
	   private String sls_bucket;
	    
		public String getSls_bucket() {
		return sls_bucket;
	}
	   public void setSls_bucket(String sls_bucket) {
		   this.sls_bucket = sls_bucket;
	   }
		public BigDecimal getResidual_tenure() {
			return residual_tenure;
		}
		public void setResidual_tenure(BigDecimal residual_tenure) {
			this.residual_tenure = residual_tenure;
		}
	public BigDecimal getS_no() {
		return s_no;
	}
	public void setS_no(BigDecimal s_no) {
		this.s_no = s_no;
	}
	public String getSol_id() {
		return sol_id;
	}
	public void setSol_id(String sol_id) {
		this.sol_id = sol_id;
	}
	public String getAcc_no() {
		return acc_no;
	}
	public void setAcc_no(String acc_no) {
		this.acc_no = acc_no;
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
	public Date getOpen_date() {
		return open_date;
	}
	public void setOpen_date(Date open_date) {
		this.open_date = open_date;
	}
	public BigDecimal getAmount_deposited() {
		return amount_deposited;
	}
	public void setAmount_deposited(BigDecimal amount_deposited) {
		this.amount_deposited = amount_deposited;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
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
	public BigDecimal getBal_equi_to_bwp() {
		return bal_equi_to_bwp;
	}
	public void setBal_equi_to_bwp(BigDecimal bal_equi_to_bwp) {
		this.bal_equi_to_bwp = bal_equi_to_bwp;
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
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
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
	public BigDecimal getPeriod_days() {
		return period_days;
	}
	public void setPeriod_days(BigDecimal period_days) {
		this.period_days = period_days;
	}
	public BigDecimal getEffective_int_rate() {
		return effective_int_rate;
	}
	public void setEffective_int_rate(BigDecimal effective_int_rate) {
		this.effective_int_rate = effective_int_rate;
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
