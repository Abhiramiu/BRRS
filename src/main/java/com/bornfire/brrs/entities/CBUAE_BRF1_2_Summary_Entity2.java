package com.bornfire.brrs.entities;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;



@Entity
@Table(name = "CBUAE_BRF1_2_SUMMARYTABLE2")

public class CBUAE_BRF1_2_Summary_Entity2 {
	private String	r0910_product;
	private BigDecimal	r0910_no_acct_aed_resident;
	private BigDecimal	r0910_amount_aed_resident;
	private BigDecimal	r0910_no_acct_fcy_resident;
	private BigDecimal	r0910_amount_fcy_resident;
	private BigDecimal	r0910_no_acct_aed_non_resident;
	private BigDecimal	r0910_amount_aed_non_resident;
	private BigDecimal	r0910_no_acct_fcy_non_resident;
	private BigDecimal	r0910_amount_fcy_non_resident;
	private BigDecimal	r0910_total_amount;
	private String	r0920_product;
	private BigDecimal	r0920_no_acct_aed_resident;
	private BigDecimal	r0920_amount_aed_resident;
	private BigDecimal	r0920_no_acct_fcy_resident;
	private BigDecimal	r0920_amount_fcy_resident;
	private BigDecimal	r0920_no_acct_aed_non_resident;
	private BigDecimal	r0920_amount_aed_non_resident;
	private BigDecimal	r0920_no_acct_fcy_non_resident;
	private BigDecimal	r0920_amount_fcy_non_resident;
	private BigDecimal	r0920_total_amount;
	private String	r0930_product;
	private BigDecimal	r0930_no_acct_aed_resident;
	private BigDecimal	r0930_amount_aed_resident;
	private BigDecimal	r0930_no_acct_fcy_resident;
	private BigDecimal	r0930_amount_fcy_resident;
	private BigDecimal	r0930_no_acct_aed_non_resident;
	private BigDecimal	r0930_amount_aed_non_resident;
	private BigDecimal	r0930_no_acct_fcy_non_resident;
	private BigDecimal	r0930_amount_fcy_non_resident;
	private BigDecimal	r0930_total_amount;
	private String	r0940_product;
	private BigDecimal	r0940_stage1_loans;
	private BigDecimal	r0940_stage1_investments;
	private BigDecimal	r0940_stage1_all_others;
	private BigDecimal	r0940_stage2_loans;
	private BigDecimal	r0940_stage2_investments;
	private BigDecimal	r0940_stage2_all_others;
	private BigDecimal	r0940_stage3_loans;
	private BigDecimal	r0940_stage3_investments;
	private BigDecimal	r0940_stage3_all_others;
	private BigDecimal	r0940_legacy_interest;
	private BigDecimal	r0940_provision;
	private BigDecimal	r0940_total;
	private String	r0950_product;
	private BigDecimal	r0950_stage1_loans;
	private BigDecimal	r0950_stage1_investments;
	private BigDecimal	r0950_stage1_all_others;
	private BigDecimal	r0950_stage2_loans;
	private BigDecimal	r0950_stage2_investments;
	private BigDecimal	r0950_stage2_all_others;
	private BigDecimal	r0950_stage3_loans;
	private BigDecimal	r0950_stage3_investments;
	private BigDecimal	r0950_stage3_all_others;
	private BigDecimal	r0950_legacy_interest;
	private BigDecimal	r0950_provision;
	private BigDecimal	r0950_total;
	private String	r0960_product;
	private BigDecimal	r0960_stage1_loans;
	private BigDecimal	r0960_stage1_investments;
	private BigDecimal	r0960_stage1_all_others;
	private BigDecimal	r0960_stage2_loans;
	private BigDecimal	r0960_stage2_investments;
	private BigDecimal	r0960_stage2_all_others;
	private BigDecimal	r0960_stage3_loans;
	private BigDecimal	r0960_stage3_investments;
	private BigDecimal	r0960_stage3_all_others;
	private BigDecimal	r0960_legacy_interest;
	private BigDecimal	r0960_provision;
	private BigDecimal	r0960_total;
	private String	r0970_product;
	private BigDecimal	r0970_stage1_loans;
	private BigDecimal	r0970_stage1_investments;
	private BigDecimal	r0970_stage1_all_others;
	private BigDecimal	r0970_stage2_loans;
	private BigDecimal	r0970_stage2_investments;
	private BigDecimal	r0970_stage2_all_others;
	private BigDecimal	r0970_stage3_loans;
	private BigDecimal	r0970_stage3_investments;
	private BigDecimal	r0970_stage3_all_others;
	private BigDecimal	r0970_legacy_interest;
	private BigDecimal	r0970_provision;
	private BigDecimal	r0970_total;
	private String	r0980_product;
	private BigDecimal	r0980_stage1_loans;
	private BigDecimal	r0980_stage1_investments;
	private BigDecimal	r0980_stage1_all_others;
	private BigDecimal	r0980_stage2_loans;
	private BigDecimal	r0980_stage2_investments;
	private BigDecimal	r0980_stage2_all_others;
	private BigDecimal	r0980_stage3_loans;
	private BigDecimal	r0980_stage3_investments;
	private BigDecimal	r0980_stage3_all_others;
	private BigDecimal	r0980_legacy_interest;
	private BigDecimal	r0980_provision;
	private BigDecimal	r0980_total;
	private String	r0990_product;
	private BigDecimal	r0990_stage1_loans;
	private BigDecimal	r0990_stage1_investments;
	private BigDecimal	r0990_stage1_all_others;
	private BigDecimal	r0990_stage2_loans;
	private BigDecimal	r0990_stage2_investments;
	private BigDecimal	r0990_stage2_all_others;
	private BigDecimal	r0990_stage3_loans;
	private BigDecimal	r0990_stage3_investments;
	private BigDecimal	r0990_stage3_all_others;
	private BigDecimal	r0990_legacy_interest;
	private BigDecimal	r0990_provision;
	private BigDecimal	r0990_total;
	private String	r1000_product;
	private BigDecimal	r1000_stage1_loans;
	private BigDecimal	r1000_stage1_investments;
	private BigDecimal	r1000_stage1_all_others;
	private BigDecimal	r1000_stage2_loans;
	private BigDecimal	r1000_stage2_investments;
	private BigDecimal	r1000_stage2_all_others;
	private BigDecimal	r1000_stage3_loans;
	private BigDecimal	r1000_stage3_investments;
	private BigDecimal	r1000_stage3_all_others;
	private BigDecimal	r1000_legacy_interest;
	private BigDecimal	r1000_provision;
	private BigDecimal	r1000_total;
	private String	r1010_product;
	private BigDecimal	r1010_stage1_loans;
	private BigDecimal	r1010_stage1_investments;
	private BigDecimal	r1010_stage1_all_others;
	private BigDecimal	r1010_stage2_loans;
	private BigDecimal	r1010_stage2_investments;
	private BigDecimal	r1010_stage2_all_others;
	private BigDecimal	r1010_stage3_loans;
	private BigDecimal	r1010_stage3_investments;
	private BigDecimal	r1010_stage3_all_others;
	private BigDecimal	r1010_legacy_interest;
	private BigDecimal	r1010_provision;
	private BigDecimal	r1010_total;
	private String	r1020_product;
	private BigDecimal	r1020_stage1_loans;
	private BigDecimal	r1020_stage1_investments;
	private BigDecimal	r1020_stage1_all_others;
	private BigDecimal	r1020_stage2_loans;
	private BigDecimal	r1020_stage2_investments;
	private BigDecimal	r1020_stage2_all_others;
	private BigDecimal	r1020_stage3_loans;
	private BigDecimal	r1020_stage3_investments;
	private BigDecimal	r1020_stage3_all_others;
	private BigDecimal	r1020_legacy_interest;
	private BigDecimal	r1020_provision;
	private BigDecimal	r1020_total;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	private Date	report_date;
	private String	report_version;
	private String	report_frequency;
	private String	report_code;
	private String	report_desc;
	private String	entity_flg;
	private String	modify_flg;
	private String	del_flg;
	public String getR0910_product() {
		return r0910_product;
	}
	public void setR0910_product(String r0910_product) {
		this.r0910_product = r0910_product;
	}
	public BigDecimal getR0910_no_acct_aed_resident() {
		return r0910_no_acct_aed_resident;
	}
	public void setR0910_no_acct_aed_resident(BigDecimal r0910_no_acct_aed_resident) {
		this.r0910_no_acct_aed_resident = r0910_no_acct_aed_resident;
	}
	public BigDecimal getR0910_amount_aed_resident() {
		return r0910_amount_aed_resident;
	}
	public void setR0910_amount_aed_resident(BigDecimal r0910_amount_aed_resident) {
		this.r0910_amount_aed_resident = r0910_amount_aed_resident;
	}
	public BigDecimal getR0910_no_acct_fcy_resident() {
		return r0910_no_acct_fcy_resident;
	}
	public void setR0910_no_acct_fcy_resident(BigDecimal r0910_no_acct_fcy_resident) {
		this.r0910_no_acct_fcy_resident = r0910_no_acct_fcy_resident;
	}
	public BigDecimal getR0910_amount_fcy_resident() {
		return r0910_amount_fcy_resident;
	}
	public void setR0910_amount_fcy_resident(BigDecimal r0910_amount_fcy_resident) {
		this.r0910_amount_fcy_resident = r0910_amount_fcy_resident;
	}
	public BigDecimal getR0910_no_acct_aed_non_resident() {
		return r0910_no_acct_aed_non_resident;
	}
	public void setR0910_no_acct_aed_non_resident(BigDecimal r0910_no_acct_aed_non_resident) {
		this.r0910_no_acct_aed_non_resident = r0910_no_acct_aed_non_resident;
	}
	public BigDecimal getR0910_amount_aed_non_resident() {
		return r0910_amount_aed_non_resident;
	}
	public void setR0910_amount_aed_non_resident(BigDecimal r0910_amount_aed_non_resident) {
		this.r0910_amount_aed_non_resident = r0910_amount_aed_non_resident;
	}
	public BigDecimal getR0910_no_acct_fcy_non_resident() {
		return r0910_no_acct_fcy_non_resident;
	}
	public void setR0910_no_acct_fcy_non_resident(BigDecimal r0910_no_acct_fcy_non_resident) {
		this.r0910_no_acct_fcy_non_resident = r0910_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0910_amount_fcy_non_resident() {
		return r0910_amount_fcy_non_resident;
	}
	public void setR0910_amount_fcy_non_resident(BigDecimal r0910_amount_fcy_non_resident) {
		this.r0910_amount_fcy_non_resident = r0910_amount_fcy_non_resident;
	}
	public BigDecimal getR0910_total_amount() {
		return r0910_total_amount;
	}
	public void setR0910_total_amount(BigDecimal r0910_total_amount) {
		this.r0910_total_amount = r0910_total_amount;
	}
	public String getR0920_product() {
		return r0920_product;
	}
	public void setR0920_product(String r0920_product) {
		this.r0920_product = r0920_product;
	}
	public BigDecimal getR0920_no_acct_aed_resident() {
		return r0920_no_acct_aed_resident;
	}
	public void setR0920_no_acct_aed_resident(BigDecimal r0920_no_acct_aed_resident) {
		this.r0920_no_acct_aed_resident = r0920_no_acct_aed_resident;
	}
	public BigDecimal getR0920_amount_aed_resident() {
		return r0920_amount_aed_resident;
	}
	public void setR0920_amount_aed_resident(BigDecimal r0920_amount_aed_resident) {
		this.r0920_amount_aed_resident = r0920_amount_aed_resident;
	}
	public BigDecimal getR0920_no_acct_fcy_resident() {
		return r0920_no_acct_fcy_resident;
	}
	public void setR0920_no_acct_fcy_resident(BigDecimal r0920_no_acct_fcy_resident) {
		this.r0920_no_acct_fcy_resident = r0920_no_acct_fcy_resident;
	}
	public BigDecimal getR0920_amount_fcy_resident() {
		return r0920_amount_fcy_resident;
	}
	public void setR0920_amount_fcy_resident(BigDecimal r0920_amount_fcy_resident) {
		this.r0920_amount_fcy_resident = r0920_amount_fcy_resident;
	}
	public BigDecimal getR0920_no_acct_aed_non_resident() {
		return r0920_no_acct_aed_non_resident;
	}
	public void setR0920_no_acct_aed_non_resident(BigDecimal r0920_no_acct_aed_non_resident) {
		this.r0920_no_acct_aed_non_resident = r0920_no_acct_aed_non_resident;
	}
	public BigDecimal getR0920_amount_aed_non_resident() {
		return r0920_amount_aed_non_resident;
	}
	public void setR0920_amount_aed_non_resident(BigDecimal r0920_amount_aed_non_resident) {
		this.r0920_amount_aed_non_resident = r0920_amount_aed_non_resident;
	}
	public BigDecimal getR0920_no_acct_fcy_non_resident() {
		return r0920_no_acct_fcy_non_resident;
	}
	public void setR0920_no_acct_fcy_non_resident(BigDecimal r0920_no_acct_fcy_non_resident) {
		this.r0920_no_acct_fcy_non_resident = r0920_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0920_amount_fcy_non_resident() {
		return r0920_amount_fcy_non_resident;
	}
	public void setR0920_amount_fcy_non_resident(BigDecimal r0920_amount_fcy_non_resident) {
		this.r0920_amount_fcy_non_resident = r0920_amount_fcy_non_resident;
	}
	public BigDecimal getR0920_total_amount() {
		return r0920_total_amount;
	}
	public void setR0920_total_amount(BigDecimal r0920_total_amount) {
		this.r0920_total_amount = r0920_total_amount;
	}
	public String getR0930_product() {
		return r0930_product;
	}
	public void setR0930_product(String r0930_product) {
		this.r0930_product = r0930_product;
	}
	public BigDecimal getR0930_no_acct_aed_resident() {
		return r0930_no_acct_aed_resident;
	}
	public void setR0930_no_acct_aed_resident(BigDecimal r0930_no_acct_aed_resident) {
		this.r0930_no_acct_aed_resident = r0930_no_acct_aed_resident;
	}
	public BigDecimal getR0930_amount_aed_resident() {
		return r0930_amount_aed_resident;
	}
	public void setR0930_amount_aed_resident(BigDecimal r0930_amount_aed_resident) {
		this.r0930_amount_aed_resident = r0930_amount_aed_resident;
	}
	public BigDecimal getR0930_no_acct_fcy_resident() {
		return r0930_no_acct_fcy_resident;
	}
	public void setR0930_no_acct_fcy_resident(BigDecimal r0930_no_acct_fcy_resident) {
		this.r0930_no_acct_fcy_resident = r0930_no_acct_fcy_resident;
	}
	public BigDecimal getR0930_amount_fcy_resident() {
		return r0930_amount_fcy_resident;
	}
	public void setR0930_amount_fcy_resident(BigDecimal r0930_amount_fcy_resident) {
		this.r0930_amount_fcy_resident = r0930_amount_fcy_resident;
	}
	public BigDecimal getR0930_no_acct_aed_non_resident() {
		return r0930_no_acct_aed_non_resident;
	}
	public void setR0930_no_acct_aed_non_resident(BigDecimal r0930_no_acct_aed_non_resident) {
		this.r0930_no_acct_aed_non_resident = r0930_no_acct_aed_non_resident;
	}
	public BigDecimal getR0930_amount_aed_non_resident() {
		return r0930_amount_aed_non_resident;
	}
	public void setR0930_amount_aed_non_resident(BigDecimal r0930_amount_aed_non_resident) {
		this.r0930_amount_aed_non_resident = r0930_amount_aed_non_resident;
	}
	public BigDecimal getR0930_no_acct_fcy_non_resident() {
		return r0930_no_acct_fcy_non_resident;
	}
	public void setR0930_no_acct_fcy_non_resident(BigDecimal r0930_no_acct_fcy_non_resident) {
		this.r0930_no_acct_fcy_non_resident = r0930_no_acct_fcy_non_resident;
	}
	public BigDecimal getR0930_amount_fcy_non_resident() {
		return r0930_amount_fcy_non_resident;
	}
	public void setR0930_amount_fcy_non_resident(BigDecimal r0930_amount_fcy_non_resident) {
		this.r0930_amount_fcy_non_resident = r0930_amount_fcy_non_resident;
	}
	public BigDecimal getR0930_total_amount() {
		return r0930_total_amount;
	}
	public void setR0930_total_amount(BigDecimal r0930_total_amount) {
		this.r0930_total_amount = r0930_total_amount;
	}
	public String getR0940_product() {
		return r0940_product;
	}
	public void setR0940_product(String r0940_product) {
		this.r0940_product = r0940_product;
	}
	public BigDecimal getR0940_stage1_loans() {
		return r0940_stage1_loans;
	}
	public void setR0940_stage1_loans(BigDecimal r0940_stage1_loans) {
		this.r0940_stage1_loans = r0940_stage1_loans;
	}
	public BigDecimal getR0940_stage1_investments() {
		return r0940_stage1_investments;
	}
	public void setR0940_stage1_investments(BigDecimal r0940_stage1_investments) {
		this.r0940_stage1_investments = r0940_stage1_investments;
	}
	public BigDecimal getR0940_stage1_all_others() {
		return r0940_stage1_all_others;
	}
	public void setR0940_stage1_all_others(BigDecimal r0940_stage1_all_others) {
		this.r0940_stage1_all_others = r0940_stage1_all_others;
	}
	public BigDecimal getR0940_stage2_loans() {
		return r0940_stage2_loans;
	}
	public void setR0940_stage2_loans(BigDecimal r0940_stage2_loans) {
		this.r0940_stage2_loans = r0940_stage2_loans;
	}
	public BigDecimal getR0940_stage2_investments() {
		return r0940_stage2_investments;
	}
	public void setR0940_stage2_investments(BigDecimal r0940_stage2_investments) {
		this.r0940_stage2_investments = r0940_stage2_investments;
	}
	public BigDecimal getR0940_stage2_all_others() {
		return r0940_stage2_all_others;
	}
	public void setR0940_stage2_all_others(BigDecimal r0940_stage2_all_others) {
		this.r0940_stage2_all_others = r0940_stage2_all_others;
	}
	public BigDecimal getR0940_stage3_loans() {
		return r0940_stage3_loans;
	}
	public void setR0940_stage3_loans(BigDecimal r0940_stage3_loans) {
		this.r0940_stage3_loans = r0940_stage3_loans;
	}
	public BigDecimal getR0940_stage3_investments() {
		return r0940_stage3_investments;
	}
	public void setR0940_stage3_investments(BigDecimal r0940_stage3_investments) {
		this.r0940_stage3_investments = r0940_stage3_investments;
	}
	public BigDecimal getR0940_stage3_all_others() {
		return r0940_stage3_all_others;
	}
	public void setR0940_stage3_all_others(BigDecimal r0940_stage3_all_others) {
		this.r0940_stage3_all_others = r0940_stage3_all_others;
	}
	public BigDecimal getR0940_legacy_interest() {
		return r0940_legacy_interest;
	}
	public void setR0940_legacy_interest(BigDecimal r0940_legacy_interest) {
		this.r0940_legacy_interest = r0940_legacy_interest;
	}
	public BigDecimal getR0940_provision() {
		return r0940_provision;
	}
	public void setR0940_provision(BigDecimal r0940_provision) {
		this.r0940_provision = r0940_provision;
	}
	public BigDecimal getR0940_total() {
		return r0940_total;
	}
	public void setR0940_total(BigDecimal r0940_total) {
		this.r0940_total = r0940_total;
	}
	public String getR0950_product() {
		return r0950_product;
	}
	public void setR0950_product(String r0950_product) {
		this.r0950_product = r0950_product;
	}
	public BigDecimal getR0950_stage1_loans() {
		return r0950_stage1_loans;
	}
	public void setR0950_stage1_loans(BigDecimal r0950_stage1_loans) {
		this.r0950_stage1_loans = r0950_stage1_loans;
	}
	public BigDecimal getR0950_stage1_investments() {
		return r0950_stage1_investments;
	}
	public void setR0950_stage1_investments(BigDecimal r0950_stage1_investments) {
		this.r0950_stage1_investments = r0950_stage1_investments;
	}
	public BigDecimal getR0950_stage1_all_others() {
		return r0950_stage1_all_others;
	}
	public void setR0950_stage1_all_others(BigDecimal r0950_stage1_all_others) {
		this.r0950_stage1_all_others = r0950_stage1_all_others;
	}
	public BigDecimal getR0950_stage2_loans() {
		return r0950_stage2_loans;
	}
	public void setR0950_stage2_loans(BigDecimal r0950_stage2_loans) {
		this.r0950_stage2_loans = r0950_stage2_loans;
	}
	public BigDecimal getR0950_stage2_investments() {
		return r0950_stage2_investments;
	}
	public void setR0950_stage2_investments(BigDecimal r0950_stage2_investments) {
		this.r0950_stage2_investments = r0950_stage2_investments;
	}
	public BigDecimal getR0950_stage2_all_others() {
		return r0950_stage2_all_others;
	}
	public void setR0950_stage2_all_others(BigDecimal r0950_stage2_all_others) {
		this.r0950_stage2_all_others = r0950_stage2_all_others;
	}
	public BigDecimal getR0950_stage3_loans() {
		return r0950_stage3_loans;
	}
	public void setR0950_stage3_loans(BigDecimal r0950_stage3_loans) {
		this.r0950_stage3_loans = r0950_stage3_loans;
	}
	public BigDecimal getR0950_stage3_investments() {
		return r0950_stage3_investments;
	}
	public void setR0950_stage3_investments(BigDecimal r0950_stage3_investments) {
		this.r0950_stage3_investments = r0950_stage3_investments;
	}
	public BigDecimal getR0950_stage3_all_others() {
		return r0950_stage3_all_others;
	}
	public void setR0950_stage3_all_others(BigDecimal r0950_stage3_all_others) {
		this.r0950_stage3_all_others = r0950_stage3_all_others;
	}
	public BigDecimal getR0950_legacy_interest() {
		return r0950_legacy_interest;
	}
	public void setR0950_legacy_interest(BigDecimal r0950_legacy_interest) {
		this.r0950_legacy_interest = r0950_legacy_interest;
	}
	public BigDecimal getR0950_provision() {
		return r0950_provision;
	}
	public void setR0950_provision(BigDecimal r0950_provision) {
		this.r0950_provision = r0950_provision;
	}
	public BigDecimal getR0950_total() {
		return r0950_total;
	}
	public void setR0950_total(BigDecimal r0950_total) {
		this.r0950_total = r0950_total;
	}
	public String getR0960_product() {
		return r0960_product;
	}
	public void setR0960_product(String r0960_product) {
		this.r0960_product = r0960_product;
	}
	public BigDecimal getR0960_stage1_loans() {
		return r0960_stage1_loans;
	}
	public void setR0960_stage1_loans(BigDecimal r0960_stage1_loans) {
		this.r0960_stage1_loans = r0960_stage1_loans;
	}
	public BigDecimal getR0960_stage1_investments() {
		return r0960_stage1_investments;
	}
	public void setR0960_stage1_investments(BigDecimal r0960_stage1_investments) {
		this.r0960_stage1_investments = r0960_stage1_investments;
	}
	public BigDecimal getR0960_stage1_all_others() {
		return r0960_stage1_all_others;
	}
	public void setR0960_stage1_all_others(BigDecimal r0960_stage1_all_others) {
		this.r0960_stage1_all_others = r0960_stage1_all_others;
	}
	public BigDecimal getR0960_stage2_loans() {
		return r0960_stage2_loans;
	}
	public void setR0960_stage2_loans(BigDecimal r0960_stage2_loans) {
		this.r0960_stage2_loans = r0960_stage2_loans;
	}
	public BigDecimal getR0960_stage2_investments() {
		return r0960_stage2_investments;
	}
	public void setR0960_stage2_investments(BigDecimal r0960_stage2_investments) {
		this.r0960_stage2_investments = r0960_stage2_investments;
	}
	public BigDecimal getR0960_stage2_all_others() {
		return r0960_stage2_all_others;
	}
	public void setR0960_stage2_all_others(BigDecimal r0960_stage2_all_others) {
		this.r0960_stage2_all_others = r0960_stage2_all_others;
	}
	public BigDecimal getR0960_stage3_loans() {
		return r0960_stage3_loans;
	}
	public void setR0960_stage3_loans(BigDecimal r0960_stage3_loans) {
		this.r0960_stage3_loans = r0960_stage3_loans;
	}
	public BigDecimal getR0960_stage3_investments() {
		return r0960_stage3_investments;
	}
	public void setR0960_stage3_investments(BigDecimal r0960_stage3_investments) {
		this.r0960_stage3_investments = r0960_stage3_investments;
	}
	public BigDecimal getR0960_stage3_all_others() {
		return r0960_stage3_all_others;
	}
	public void setR0960_stage3_all_others(BigDecimal r0960_stage3_all_others) {
		this.r0960_stage3_all_others = r0960_stage3_all_others;
	}
	public BigDecimal getR0960_legacy_interest() {
		return r0960_legacy_interest;
	}
	public void setR0960_legacy_interest(BigDecimal r0960_legacy_interest) {
		this.r0960_legacy_interest = r0960_legacy_interest;
	}
	public BigDecimal getR0960_provision() {
		return r0960_provision;
	}
	public void setR0960_provision(BigDecimal r0960_provision) {
		this.r0960_provision = r0960_provision;
	}
	public BigDecimal getR0960_total() {
		return r0960_total;
	}
	public void setR0960_total(BigDecimal r0960_total) {
		this.r0960_total = r0960_total;
	}
	public String getR0970_product() {
		return r0970_product;
	}
	public void setR0970_product(String r0970_product) {
		this.r0970_product = r0970_product;
	}
	public BigDecimal getR0970_stage1_loans() {
		return r0970_stage1_loans;
	}
	public void setR0970_stage1_loans(BigDecimal r0970_stage1_loans) {
		this.r0970_stage1_loans = r0970_stage1_loans;
	}
	public BigDecimal getR0970_stage1_investments() {
		return r0970_stage1_investments;
	}
	public void setR0970_stage1_investments(BigDecimal r0970_stage1_investments) {
		this.r0970_stage1_investments = r0970_stage1_investments;
	}
	public BigDecimal getR0970_stage1_all_others() {
		return r0970_stage1_all_others;
	}
	public void setR0970_stage1_all_others(BigDecimal r0970_stage1_all_others) {
		this.r0970_stage1_all_others = r0970_stage1_all_others;
	}
	public BigDecimal getR0970_stage2_loans() {
		return r0970_stage2_loans;
	}
	public void setR0970_stage2_loans(BigDecimal r0970_stage2_loans) {
		this.r0970_stage2_loans = r0970_stage2_loans;
	}
	public BigDecimal getR0970_stage2_investments() {
		return r0970_stage2_investments;
	}
	public void setR0970_stage2_investments(BigDecimal r0970_stage2_investments) {
		this.r0970_stage2_investments = r0970_stage2_investments;
	}
	public BigDecimal getR0970_stage2_all_others() {
		return r0970_stage2_all_others;
	}
	public void setR0970_stage2_all_others(BigDecimal r0970_stage2_all_others) {
		this.r0970_stage2_all_others = r0970_stage2_all_others;
	}
	public BigDecimal getR0970_stage3_loans() {
		return r0970_stage3_loans;
	}
	public void setR0970_stage3_loans(BigDecimal r0970_stage3_loans) {
		this.r0970_stage3_loans = r0970_stage3_loans;
	}
	public BigDecimal getR0970_stage3_investments() {
		return r0970_stage3_investments;
	}
	public void setR0970_stage3_investments(BigDecimal r0970_stage3_investments) {
		this.r0970_stage3_investments = r0970_stage3_investments;
	}
	public BigDecimal getR0970_stage3_all_others() {
		return r0970_stage3_all_others;
	}
	public void setR0970_stage3_all_others(BigDecimal r0970_stage3_all_others) {
		this.r0970_stage3_all_others = r0970_stage3_all_others;
	}
	public BigDecimal getR0970_legacy_interest() {
		return r0970_legacy_interest;
	}
	public void setR0970_legacy_interest(BigDecimal r0970_legacy_interest) {
		this.r0970_legacy_interest = r0970_legacy_interest;
	}
	public BigDecimal getR0970_provision() {
		return r0970_provision;
	}
	public void setR0970_provision(BigDecimal r0970_provision) {
		this.r0970_provision = r0970_provision;
	}
	public BigDecimal getR0970_total() {
		return r0970_total;
	}
	public void setR0970_total(BigDecimal r0970_total) {
		this.r0970_total = r0970_total;
	}
	public String getR0980_product() {
		return r0980_product;
	}
	public void setR0980_product(String r0980_product) {
		this.r0980_product = r0980_product;
	}
	public BigDecimal getR0980_stage1_loans() {
		return r0980_stage1_loans;
	}
	public void setR0980_stage1_loans(BigDecimal r0980_stage1_loans) {
		this.r0980_stage1_loans = r0980_stage1_loans;
	}
	public BigDecimal getR0980_stage1_investments() {
		return r0980_stage1_investments;
	}
	public void setR0980_stage1_investments(BigDecimal r0980_stage1_investments) {
		this.r0980_stage1_investments = r0980_stage1_investments;
	}
	public BigDecimal getR0980_stage1_all_others() {
		return r0980_stage1_all_others;
	}
	public void setR0980_stage1_all_others(BigDecimal r0980_stage1_all_others) {
		this.r0980_stage1_all_others = r0980_stage1_all_others;
	}
	public BigDecimal getR0980_stage2_loans() {
		return r0980_stage2_loans;
	}
	public void setR0980_stage2_loans(BigDecimal r0980_stage2_loans) {
		this.r0980_stage2_loans = r0980_stage2_loans;
	}
	public BigDecimal getR0980_stage2_investments() {
		return r0980_stage2_investments;
	}
	public void setR0980_stage2_investments(BigDecimal r0980_stage2_investments) {
		this.r0980_stage2_investments = r0980_stage2_investments;
	}
	public BigDecimal getR0980_stage2_all_others() {
		return r0980_stage2_all_others;
	}
	public void setR0980_stage2_all_others(BigDecimal r0980_stage2_all_others) {
		this.r0980_stage2_all_others = r0980_stage2_all_others;
	}
	public BigDecimal getR0980_stage3_loans() {
		return r0980_stage3_loans;
	}
	public void setR0980_stage3_loans(BigDecimal r0980_stage3_loans) {
		this.r0980_stage3_loans = r0980_stage3_loans;
	}
	public BigDecimal getR0980_stage3_investments() {
		return r0980_stage3_investments;
	}
	public void setR0980_stage3_investments(BigDecimal r0980_stage3_investments) {
		this.r0980_stage3_investments = r0980_stage3_investments;
	}
	public BigDecimal getR0980_stage3_all_others() {
		return r0980_stage3_all_others;
	}
	public void setR0980_stage3_all_others(BigDecimal r0980_stage3_all_others) {
		this.r0980_stage3_all_others = r0980_stage3_all_others;
	}
	public BigDecimal getR0980_legacy_interest() {
		return r0980_legacy_interest;
	}
	public void setR0980_legacy_interest(BigDecimal r0980_legacy_interest) {
		this.r0980_legacy_interest = r0980_legacy_interest;
	}
	public BigDecimal getR0980_provision() {
		return r0980_provision;
	}
	public void setR0980_provision(BigDecimal r0980_provision) {
		this.r0980_provision = r0980_provision;
	}
	public BigDecimal getR0980_total() {
		return r0980_total;
	}
	public void setR0980_total(BigDecimal r0980_total) {
		this.r0980_total = r0980_total;
	}
	public String getR0990_product() {
		return r0990_product;
	}
	public void setR0990_product(String r0990_product) {
		this.r0990_product = r0990_product;
	}
	public BigDecimal getR0990_stage1_loans() {
		return r0990_stage1_loans;
	}
	public void setR0990_stage1_loans(BigDecimal r0990_stage1_loans) {
		this.r0990_stage1_loans = r0990_stage1_loans;
	}
	public BigDecimal getR0990_stage1_investments() {
		return r0990_stage1_investments;
	}
	public void setR0990_stage1_investments(BigDecimal r0990_stage1_investments) {
		this.r0990_stage1_investments = r0990_stage1_investments;
	}
	public BigDecimal getR0990_stage1_all_others() {
		return r0990_stage1_all_others;
	}
	public void setR0990_stage1_all_others(BigDecimal r0990_stage1_all_others) {
		this.r0990_stage1_all_others = r0990_stage1_all_others;
	}
	public BigDecimal getR0990_stage2_loans() {
		return r0990_stage2_loans;
	}
	public void setR0990_stage2_loans(BigDecimal r0990_stage2_loans) {
		this.r0990_stage2_loans = r0990_stage2_loans;
	}
	public BigDecimal getR0990_stage2_investments() {
		return r0990_stage2_investments;
	}
	public void setR0990_stage2_investments(BigDecimal r0990_stage2_investments) {
		this.r0990_stage2_investments = r0990_stage2_investments;
	}
	public BigDecimal getR0990_stage2_all_others() {
		return r0990_stage2_all_others;
	}
	public void setR0990_stage2_all_others(BigDecimal r0990_stage2_all_others) {
		this.r0990_stage2_all_others = r0990_stage2_all_others;
	}
	public BigDecimal getR0990_stage3_loans() {
		return r0990_stage3_loans;
	}
	public void setR0990_stage3_loans(BigDecimal r0990_stage3_loans) {
		this.r0990_stage3_loans = r0990_stage3_loans;
	}
	public BigDecimal getR0990_stage3_investments() {
		return r0990_stage3_investments;
	}
	public void setR0990_stage3_investments(BigDecimal r0990_stage3_investments) {
		this.r0990_stage3_investments = r0990_stage3_investments;
	}
	public BigDecimal getR0990_stage3_all_others() {
		return r0990_stage3_all_others;
	}
	public void setR0990_stage3_all_others(BigDecimal r0990_stage3_all_others) {
		this.r0990_stage3_all_others = r0990_stage3_all_others;
	}
	public BigDecimal getR0990_legacy_interest() {
		return r0990_legacy_interest;
	}
	public void setR0990_legacy_interest(BigDecimal r0990_legacy_interest) {
		this.r0990_legacy_interest = r0990_legacy_interest;
	}
	public BigDecimal getR0990_provision() {
		return r0990_provision;
	}
	public void setR0990_provision(BigDecimal r0990_provision) {
		this.r0990_provision = r0990_provision;
	}
	public BigDecimal getR0990_total() {
		return r0990_total;
	}
	public void setR0990_total(BigDecimal r0990_total) {
		this.r0990_total = r0990_total;
	}
	public String getR1000_product() {
		return r1000_product;
	}
	public void setR1000_product(String r1000_product) {
		this.r1000_product = r1000_product;
	}
	public BigDecimal getR1000_stage1_loans() {
		return r1000_stage1_loans;
	}
	public void setR1000_stage1_loans(BigDecimal r1000_stage1_loans) {
		this.r1000_stage1_loans = r1000_stage1_loans;
	}
	public BigDecimal getR1000_stage1_investments() {
		return r1000_stage1_investments;
	}
	public void setR1000_stage1_investments(BigDecimal r1000_stage1_investments) {
		this.r1000_stage1_investments = r1000_stage1_investments;
	}
	public BigDecimal getR1000_stage1_all_others() {
		return r1000_stage1_all_others;
	}
	public void setR1000_stage1_all_others(BigDecimal r1000_stage1_all_others) {
		this.r1000_stage1_all_others = r1000_stage1_all_others;
	}
	public BigDecimal getR1000_stage2_loans() {
		return r1000_stage2_loans;
	}
	public void setR1000_stage2_loans(BigDecimal r1000_stage2_loans) {
		this.r1000_stage2_loans = r1000_stage2_loans;
	}
	public BigDecimal getR1000_stage2_investments() {
		return r1000_stage2_investments;
	}
	public void setR1000_stage2_investments(BigDecimal r1000_stage2_investments) {
		this.r1000_stage2_investments = r1000_stage2_investments;
	}
	public BigDecimal getR1000_stage2_all_others() {
		return r1000_stage2_all_others;
	}
	public void setR1000_stage2_all_others(BigDecimal r1000_stage2_all_others) {
		this.r1000_stage2_all_others = r1000_stage2_all_others;
	}
	public BigDecimal getR1000_stage3_loans() {
		return r1000_stage3_loans;
	}
	public void setR1000_stage3_loans(BigDecimal r1000_stage3_loans) {
		this.r1000_stage3_loans = r1000_stage3_loans;
	}
	public BigDecimal getR1000_stage3_investments() {
		return r1000_stage3_investments;
	}
	public void setR1000_stage3_investments(BigDecimal r1000_stage3_investments) {
		this.r1000_stage3_investments = r1000_stage3_investments;
	}
	public BigDecimal getR1000_stage3_all_others() {
		return r1000_stage3_all_others;
	}
	public void setR1000_stage3_all_others(BigDecimal r1000_stage3_all_others) {
		this.r1000_stage3_all_others = r1000_stage3_all_others;
	}
	public BigDecimal getR1000_legacy_interest() {
		return r1000_legacy_interest;
	}
	public void setR1000_legacy_interest(BigDecimal r1000_legacy_interest) {
		this.r1000_legacy_interest = r1000_legacy_interest;
	}
	public BigDecimal getR1000_provision() {
		return r1000_provision;
	}
	public void setR1000_provision(BigDecimal r1000_provision) {
		this.r1000_provision = r1000_provision;
	}
	public BigDecimal getR1000_total() {
		return r1000_total;
	}
	public void setR1000_total(BigDecimal r1000_total) {
		this.r1000_total = r1000_total;
	}
	public String getR1010_product() {
		return r1010_product;
	}
	public void setR1010_product(String r1010_product) {
		this.r1010_product = r1010_product;
	}
	public BigDecimal getR1010_stage1_loans() {
		return r1010_stage1_loans;
	}
	public void setR1010_stage1_loans(BigDecimal r1010_stage1_loans) {
		this.r1010_stage1_loans = r1010_stage1_loans;
	}
	public BigDecimal getR1010_stage1_investments() {
		return r1010_stage1_investments;
	}
	public void setR1010_stage1_investments(BigDecimal r1010_stage1_investments) {
		this.r1010_stage1_investments = r1010_stage1_investments;
	}
	public BigDecimal getR1010_stage1_all_others() {
		return r1010_stage1_all_others;
	}
	public void setR1010_stage1_all_others(BigDecimal r1010_stage1_all_others) {
		this.r1010_stage1_all_others = r1010_stage1_all_others;
	}
	public BigDecimal getR1010_stage2_loans() {
		return r1010_stage2_loans;
	}
	public void setR1010_stage2_loans(BigDecimal r1010_stage2_loans) {
		this.r1010_stage2_loans = r1010_stage2_loans;
	}
	public BigDecimal getR1010_stage2_investments() {
		return r1010_stage2_investments;
	}
	public void setR1010_stage2_investments(BigDecimal r1010_stage2_investments) {
		this.r1010_stage2_investments = r1010_stage2_investments;
	}
	public BigDecimal getR1010_stage2_all_others() {
		return r1010_stage2_all_others;
	}
	public void setR1010_stage2_all_others(BigDecimal r1010_stage2_all_others) {
		this.r1010_stage2_all_others = r1010_stage2_all_others;
	}
	public BigDecimal getR1010_stage3_loans() {
		return r1010_stage3_loans;
	}
	public void setR1010_stage3_loans(BigDecimal r1010_stage3_loans) {
		this.r1010_stage3_loans = r1010_stage3_loans;
	}
	public BigDecimal getR1010_stage3_investments() {
		return r1010_stage3_investments;
	}
	public void setR1010_stage3_investments(BigDecimal r1010_stage3_investments) {
		this.r1010_stage3_investments = r1010_stage3_investments;
	}
	public BigDecimal getR1010_stage3_all_others() {
		return r1010_stage3_all_others;
	}
	public void setR1010_stage3_all_others(BigDecimal r1010_stage3_all_others) {
		this.r1010_stage3_all_others = r1010_stage3_all_others;
	}
	public BigDecimal getR1010_legacy_interest() {
		return r1010_legacy_interest;
	}
	public void setR1010_legacy_interest(BigDecimal r1010_legacy_interest) {
		this.r1010_legacy_interest = r1010_legacy_interest;
	}
	public BigDecimal getR1010_provision() {
		return r1010_provision;
	}
	public void setR1010_provision(BigDecimal r1010_provision) {
		this.r1010_provision = r1010_provision;
	}
	public BigDecimal getR1010_total() {
		return r1010_total;
	}
	public void setR1010_total(BigDecimal r1010_total) {
		this.r1010_total = r1010_total;
	}
	public String getR1020_product() {
		return r1020_product;
	}
	public void setR1020_product(String r1020_product) {
		this.r1020_product = r1020_product;
	}
	public BigDecimal getR1020_stage1_loans() {
		return r1020_stage1_loans;
	}
	public void setR1020_stage1_loans(BigDecimal r1020_stage1_loans) {
		this.r1020_stage1_loans = r1020_stage1_loans;
	}
	public BigDecimal getR1020_stage1_investments() {
		return r1020_stage1_investments;
	}
	public void setR1020_stage1_investments(BigDecimal r1020_stage1_investments) {
		this.r1020_stage1_investments = r1020_stage1_investments;
	}
	public BigDecimal getR1020_stage1_all_others() {
		return r1020_stage1_all_others;
	}
	public void setR1020_stage1_all_others(BigDecimal r1020_stage1_all_others) {
		this.r1020_stage1_all_others = r1020_stage1_all_others;
	}
	public BigDecimal getR1020_stage2_loans() {
		return r1020_stage2_loans;
	}
	public void setR1020_stage2_loans(BigDecimal r1020_stage2_loans) {
		this.r1020_stage2_loans = r1020_stage2_loans;
	}
	public BigDecimal getR1020_stage2_investments() {
		return r1020_stage2_investments;
	}
	public void setR1020_stage2_investments(BigDecimal r1020_stage2_investments) {
		this.r1020_stage2_investments = r1020_stage2_investments;
	}
	public BigDecimal getR1020_stage2_all_others() {
		return r1020_stage2_all_others;
	}
	public void setR1020_stage2_all_others(BigDecimal r1020_stage2_all_others) {
		this.r1020_stage2_all_others = r1020_stage2_all_others;
	}
	public BigDecimal getR1020_stage3_loans() {
		return r1020_stage3_loans;
	}
	public void setR1020_stage3_loans(BigDecimal r1020_stage3_loans) {
		this.r1020_stage3_loans = r1020_stage3_loans;
	}
	public BigDecimal getR1020_stage3_investments() {
		return r1020_stage3_investments;
	}
	public void setR1020_stage3_investments(BigDecimal r1020_stage3_investments) {
		this.r1020_stage3_investments = r1020_stage3_investments;
	}
	public BigDecimal getR1020_stage3_all_others() {
		return r1020_stage3_all_others;
	}
	public void setR1020_stage3_all_others(BigDecimal r1020_stage3_all_others) {
		this.r1020_stage3_all_others = r1020_stage3_all_others;
	}
	public BigDecimal getR1020_legacy_interest() {
		return r1020_legacy_interest;
	}
	public void setR1020_legacy_interest(BigDecimal r1020_legacy_interest) {
		this.r1020_legacy_interest = r1020_legacy_interest;
	}
	public BigDecimal getR1020_provision() {
		return r1020_provision;
	}
	public void setR1020_provision(BigDecimal r1020_provision) {
		this.r1020_provision = r1020_provision;
	}
	public BigDecimal getR1020_total() {
		return r1020_total;
	}
	public void setR1020_total(BigDecimal r1020_total) {
		this.r1020_total = r1020_total;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public String getReport_version() {
		return report_version;
	}
	public void setReport_version(String report_version) {
		this.report_version = report_version;
	}
	public String getReport_frequency() {
		return report_frequency;
	}
	public void setReport_frequency(String report_frequency) {
		this.report_frequency = report_frequency;
	}
	public String getReport_code() {
		return report_code;
	}
	public void setReport_code(String report_code) {
		this.report_code = report_code;
	}
	public String getReport_desc() {
		return report_desc;
	}
	public void setReport_desc(String report_desc) {
		this.report_desc = report_desc;
	}
	public String getEntity_flg() {
		return entity_flg;
	}
	public void setEntity_flg(String entity_flg) {
		this.entity_flg = entity_flg;
	}
	public String getModify_flg() {
		return modify_flg;
	}
	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
	}
	public String getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}
	public CBUAE_BRF1_2_Summary_Entity2() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
