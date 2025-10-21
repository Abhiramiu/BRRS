package com.bornfire.brrs.entities;



import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "BRRS_BFDB")
public class BFDB_Entity {

    @Id
    private String customer_id;

    private String sol_id;
    private String gender;
    private String account_no;

    private String customer_name; 
    private String schm_code;
    private String schm_desc;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date acct_open_date; 

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date acct_close_date; 

    private BigDecimal balance_as_on;
    private String currency; 
    private BigDecimal bal_equi_to_bwp;
    private BigDecimal rate_of_interest; 
    private BigDecimal hundred;
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date maturity_date;

    private String gl_sub_head_code;
    private String gl_sub_head_desc;
    private String type_of_accounts;
    private String segment;
    private String period;
    private BigDecimal effective_interest_rate; 
    private String branch_name;
    private String branch_code;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date report_date;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date entry_date;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modify_date;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date verify_date;

    private String entry_user;
    private String modify_user;
    private String verify_user;
    private String entry_flg;
    private String modify_flg;
    private String verify_flg;
    private String del_flg;
	
	public String getSol_id() {
		return sol_id;
	}
	public void setSol_id(String sol_id) {
		this.sol_id = sol_id;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAccount_no() {
		return account_no;
	}
	public void setAccount_no(String account_no) {
		this.account_no = account_no;
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

	

	
