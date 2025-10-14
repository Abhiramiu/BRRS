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
    private String gl_code;

    @Column(name = "gl_sub_code")
    private String gl_sub_code;

    @Column(name = "head_acc_no")
    private String head_acc_no;

    @Column(name = "description")
    private String description;

    @Column(name = "currency")
    private String currency;

    @Column(name = "debit_balance")
    private BigDecimal debit_balance;

    @Column(name = "credit_balance")
    private BigDecimal credit_balance;

    @Column(name = "debit_equivalent")
    private BigDecimal debit_equivalent;

    @Column(name = "credit_equivalent")
    private BigDecimal credit_equivalent;

    @Column(name = "entry_user")
    private String entry_user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "entry_date")
    private Date entry_date;
    

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "report_date")
    private Date report_date;

    public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	// 🔹 Getters & Setters
   

    public String getGl_code() {
        return gl_code;
    }
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setGl_code(String gl_code) {
        this.gl_code = gl_code;
    }

    public String getGl_sub_code() {
        return gl_sub_code;
    }
    public void setGl_sub_code(String gl_sub_code) {
        this.gl_sub_code = gl_sub_code;
    }

    public String getHead_acc_no() {
        return head_acc_no;
    }
    public void setHead_acc_no(String head_acc_no) {
        this.head_acc_no = head_acc_no;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getDebit_balance() {
        return debit_balance;
    }
    public void setDebit_balance(BigDecimal debit_balance) {
        this.debit_balance = debit_balance;
    }

    public BigDecimal getCredit_balance() {
        return credit_balance;
    }
    public void setCredit_balance(BigDecimal credit_balance) {
        this.credit_balance = credit_balance;
    }

    public BigDecimal getDebit_equivalent() {
        return debit_equivalent;
    }
    public void setDebit_equivalent(BigDecimal debit_equivalent) {
        this.debit_equivalent = debit_equivalent;
    }

    public BigDecimal getCredit_equivalent() {
        return credit_equivalent;
    }
    public void setCredit_equivalent(BigDecimal credit_equivalent) {
        this.credit_equivalent = credit_equivalent;
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
}