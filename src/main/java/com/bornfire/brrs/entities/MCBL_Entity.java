package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BRRS_MCBL")
public class MCBL_Entity {

	// In @Entity
	@Id
	private String id;



    @Column(name = "gl_code")
    private String mcbl_gl_code;

    @Column(name = "gl_sub_code")
    private String mcbl_gl_sub_code;

    @Column(name = "head_acc_no")
    private String	mcbl_head_acc_no;
    
    @Column(name = "description")
	private String	mcbl_description;
    
    @Column(name = "currency")
	private String	mcbl_currency;
    
    @Column(name = "debit_balance")
	private BigDecimal	mcbl_debit_balance;
    
    @Column(name = "credit_balance")
	private BigDecimal	mcbl_credit_balance;
    
    @Column(name = "debit_equivalent")
	private BigDecimal	mcbl_debit_equivalent;
    
    @Column(name = "credit_equivalent")
	private BigDecimal	mcbl_credit_equivalent;
	
    
  
    @Column(name = "entry_user")
    private String entry_user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "entry_date")
    private Date entry_date;
    
    @Column(name = "modify_user")
    private String modify_user;

    @Column(name = "delete_user")
    private String delete_user;

    @Column(name = "entry_flg")
    private String entry_flg;
    
    @Column(name = "modify_flg")
    private String modify_flg;
    
    @Column(name = "delete_flg")
    private String delete_flg;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "report_date")
    private Date report_date;

    public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
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
	public String getEntry_user() {
		return entry_user;
	}
	public void setEntry_user(String entry_user) {
		this.entry_user = entry_user;
	}
	public Date getEntry_date() {
		return entry_date;
	}
	public void setEntry_date(Date entry_date) {
		this.entry_date = entry_date;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public String getDelete_user() {
		return delete_user;
	}
	public void setDelete_user(String delete_user) {
		this.delete_user = delete_user;
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
	public String getDelete_flg() {
		return delete_flg;
	}
	public void setDelete_flg(String delete_flg) {
		this.delete_flg = delete_flg;
	}
	
	
	
}