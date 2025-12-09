
package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;



@Entity
@Table(name = "BRRS_BDISB2_ARCHIVALTABLE_SUMMARY")
@IdClass(BDISB2_Archival_Summary_PK.class)

public class BDISB2_Archival_Summary_Entity{	



	@Id
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	
	@Id
	@Column(name = "REPORT_VERSION")
	private String reportVersion;
	
    @Column(name = "REPORT_RESUBDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportResubDate;	
	
	public String report_frequency;
	public String report_code;
	public String report_desc;
	public String entity_flg;
	public String modify_flg;
	public String del_flg;


	private BigDecimal r6_bank_spec_single_cust_rec_num;
	private String r6_company_name;
	private BigDecimal r6_company_reg_num;
	private String r6_businees_phy_address;
	private String r6_postal_address;
	private String r6_country_of_reg;
	private String r6_company_email;
	private String r6_company_landline;
	private String r6_company_mob_phone_num;
	private String r6_product_type;
	private BigDecimal r6_acct_num;
	private String r6_status_of_acct;
	private String r6_acct_status_fit_or_not_fit_for_straight_throu_payout;
	private String r6_acct_branch;
	private BigDecimal r6_acct_balance_pula;
	private String r6_currency_of_acct;
	private BigDecimal r6_exchange_rate;
	private BigDecimal r7_bank_spec_single_cust_rec_num;
	private String r7_company_name;
	private BigDecimal r7_company_reg_num;
	private String r7_businees_phy_address;
	private String r7_postal_address;
	private String r7_country_of_reg;
	private String r7_company_email;
	private String r7_company_landline;
	private String r7_company_mob_phone_num;
	private String r7_product_type;
	private BigDecimal r7_acct_num;
	private String r7_status_of_acct;
	private String r7_acct_status_fit_or_not_fit_for_straight_throu_payout;
	private String r7_acct_branch;
	private BigDecimal r7_acct_balance_pula;
	private String r7_currency_of_acct;
	private BigDecimal r7_exchange_rate;
	private BigDecimal r8_bank_spec_single_cust_rec_num;
	private String r8_company_name;
	private BigDecimal r8_company_reg_num;
	private String r8_businees_phy_address;
	private String r8_postal_address;
	private String r8_country_of_reg;
	private String r8_company_email;
	private String r8_company_landline;
	private String r8_company_mob_phone_num;
	private String r8_product_type;
	private BigDecimal r8_acct_num;
	private String r8_status_of_acct;
	private String r8_acct_status_fit_or_not_fit_for_straight_throu_payout;
	private String r8_acct_branch;
	private BigDecimal r8_acct_balance_pula;
	private String r8_currency_of_acct;
	private BigDecimal r8_exchange_rate;
	private BigDecimal r9_bank_spec_single_cust_rec_num;
	private String r9_company_name;
	private BigDecimal r9_company_reg_num;
	private String r9_businees_phy_address;
	private String r9_postal_address;
	private String r9_country_of_reg;
	private String r9_company_email;
	private String r9_company_landline;
	private String r9_company_mob_phone_num;
	private String r9_product_type;
	private BigDecimal r9_acct_num;
	private String r9_status_of_acct;
	private String r9_acct_status_fit_or_not_fit_for_straight_throu_payout;
	private String r9_acct_branch;
	private BigDecimal r9_acct_balance_pula;
	private String r9_currency_of_acct;
	private BigDecimal r9_exchange_rate;
	private BigDecimal r10_bank_spec_single_cust_rec_num;
	private String r10_company_name;
	private BigDecimal r10_company_reg_num;
	private String r10_businees_phy_address;
	private String r10_postal_address;
	private String r10_country_of_reg;
	private String r10_company_email;
	private String r10_company_landline;
	private String r10_company_mob_phone_num;
	private String r10_product_type;
	private BigDecimal r10_acct_num;
	private String r10_status_of_acct;
	private String r10_acct_status_fit_or_not_fit_for_straight_throu_payout;
	private String r10_acct_branch;
	private BigDecimal r10_acct_balance_pula;
	private String r10_currency_of_acct;
	private BigDecimal r10_exchange_rate;
	private BigDecimal r11_bank_spec_single_cust_rec_num;
	private String r11_company_name;
	private BigDecimal r11_company_reg_num;
	private String r11_businees_phy_address;
	private String r11_postal_address;
	private String r11_country_of_reg;
	private String r11_company_email;
	private String r11_company_landline;
	private String r11_company_mob_phone_num;
	private String r11_product_type;
	private BigDecimal r11_acct_num;
	private String r11_status_of_acct;
	private String r11_acct_status_fit_or_not_fit_for_straight_throu_payout;
	private String r11_acct_branch;
	private BigDecimal r11_acct_balance_pula;
	private String r11_currency_of_acct;
	private BigDecimal r11_exchange_rate;
	private BigDecimal r12_bank_spec_single_cust_rec_num;
	private String r12_company_name;
	private BigDecimal r12_company_reg_num;
	private String r12_businees_phy_address;
	private String r12_postal_address;
	private String r12_country_of_reg;
	private String r12_company_email;
	private String r12_company_landline;
	private String r12_company_mob_phone_num;
	private String r12_product_type;
	private BigDecimal r12_acct_num;
	private String r12_status_of_acct;
	private String r12_acct_status_fit_or_not_fit_for_straight_throu_payout;
	private String r12_acct_branch;
	private BigDecimal r12_acct_balance_pula;
	private String r12_currency_of_acct;
	private BigDecimal r12_exchange_rate;

	



	public Date getReportDate() {
		return reportDate;
	}





	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}





	public String getReportVersion() {
		return reportVersion;
	}





	public void setReportVersion(String reportVersion) {
		this.reportVersion = reportVersion;
	}





	public Date getReportResubDate() {
		return reportResubDate;
	}





	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
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





	public BigDecimal getR6_bank_spec_single_cust_rec_num() {
		return r6_bank_spec_single_cust_rec_num;
	}





	public void setR6_bank_spec_single_cust_rec_num(BigDecimal r6_bank_spec_single_cust_rec_num) {
		this.r6_bank_spec_single_cust_rec_num = r6_bank_spec_single_cust_rec_num;
	}





	public String getR6_company_name() {
		return r6_company_name;
	}





	public void setR6_company_name(String r6_company_name) {
		this.r6_company_name = r6_company_name;
	}





	public BigDecimal getR6_company_reg_num() {
		return r6_company_reg_num;
	}





	public void setR6_company_reg_num(BigDecimal r6_company_reg_num) {
		this.r6_company_reg_num = r6_company_reg_num;
	}





	public String getR6_businees_phy_address() {
		return r6_businees_phy_address;
	}





	public void setR6_businees_phy_address(String r6_businees_phy_address) {
		this.r6_businees_phy_address = r6_businees_phy_address;
	}





	public String getR6_postal_address() {
		return r6_postal_address;
	}





	public void setR6_postal_address(String r6_postal_address) {
		this.r6_postal_address = r6_postal_address;
	}





	public String getR6_country_of_reg() {
		return r6_country_of_reg;
	}





	public void setR6_country_of_reg(String r6_country_of_reg) {
		this.r6_country_of_reg = r6_country_of_reg;
	}





	public String getR6_company_email() {
		return r6_company_email;
	}





	public void setR6_company_email(String r6_company_email) {
		this.r6_company_email = r6_company_email;
	}





	public String getR6_company_landline() {
		return r6_company_landline;
	}





	public void setR6_company_landline(String r6_company_landline) {
		this.r6_company_landline = r6_company_landline;
	}





	public String getR6_company_mob_phone_num() {
		return r6_company_mob_phone_num;
	}





	public void setR6_company_mob_phone_num(String r6_company_mob_phone_num) {
		this.r6_company_mob_phone_num = r6_company_mob_phone_num;
	}





	public String getR6_product_type() {
		return r6_product_type;
	}





	public void setR6_product_type(String r6_product_type) {
		this.r6_product_type = r6_product_type;
	}





	public BigDecimal getR6_acct_num() {
		return r6_acct_num;
	}





	public void setR6_acct_num(BigDecimal r6_acct_num) {
		this.r6_acct_num = r6_acct_num;
	}





	public String getR6_status_of_acct() {
		return r6_status_of_acct;
	}





	public void setR6_status_of_acct(String r6_status_of_acct) {
		this.r6_status_of_acct = r6_status_of_acct;
	}





	public String getR6_acct_status_fit_or_not_fit_for_straight_throu_payout() {
		return r6_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public void setR6_acct_status_fit_or_not_fit_for_straight_throu_payout(
			String r6_acct_status_fit_or_not_fit_for_straight_throu_payout) {
		this.r6_acct_status_fit_or_not_fit_for_straight_throu_payout = r6_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public String getR6_acct_branch() {
		return r6_acct_branch;
	}





	public void setR6_acct_branch(String r6_acct_branch) {
		this.r6_acct_branch = r6_acct_branch;
	}





	public BigDecimal getR6_acct_balance_pula() {
		return r6_acct_balance_pula;
	}





	public void setR6_acct_balance_pula(BigDecimal r6_acct_balance_pula) {
		this.r6_acct_balance_pula = r6_acct_balance_pula;
	}





	public String getR6_currency_of_acct() {
		return r6_currency_of_acct;
	}





	public void setR6_currency_of_acct(String r6_currency_of_acct) {
		this.r6_currency_of_acct = r6_currency_of_acct;
	}





	public BigDecimal getR6_exchange_rate() {
		return r6_exchange_rate;
	}





	public void setR6_exchange_rate(BigDecimal r6_exchange_rate) {
		this.r6_exchange_rate = r6_exchange_rate;
	}





	public BigDecimal getR7_bank_spec_single_cust_rec_num() {
		return r7_bank_spec_single_cust_rec_num;
	}





	public void setR7_bank_spec_single_cust_rec_num(BigDecimal r7_bank_spec_single_cust_rec_num) {
		this.r7_bank_spec_single_cust_rec_num = r7_bank_spec_single_cust_rec_num;
	}





	public String getR7_company_name() {
		return r7_company_name;
	}





	public void setR7_company_name(String r7_company_name) {
		this.r7_company_name = r7_company_name;
	}





	public BigDecimal getR7_company_reg_num() {
		return r7_company_reg_num;
	}





	public void setR7_company_reg_num(BigDecimal r7_company_reg_num) {
		this.r7_company_reg_num = r7_company_reg_num;
	}





	public String getR7_businees_phy_address() {
		return r7_businees_phy_address;
	}





	public void setR7_businees_phy_address(String r7_businees_phy_address) {
		this.r7_businees_phy_address = r7_businees_phy_address;
	}





	public String getR7_postal_address() {
		return r7_postal_address;
	}





	public void setR7_postal_address(String r7_postal_address) {
		this.r7_postal_address = r7_postal_address;
	}





	public String getR7_country_of_reg() {
		return r7_country_of_reg;
	}





	public void setR7_country_of_reg(String r7_country_of_reg) {
		this.r7_country_of_reg = r7_country_of_reg;
	}





	public String getR7_company_email() {
		return r7_company_email;
	}





	public void setR7_company_email(String r7_company_email) {
		this.r7_company_email = r7_company_email;
	}





	public String getR7_company_landline() {
		return r7_company_landline;
	}





	public void setR7_company_landline(String r7_company_landline) {
		this.r7_company_landline = r7_company_landline;
	}





	public String getR7_company_mob_phone_num() {
		return r7_company_mob_phone_num;
	}





	public void setR7_company_mob_phone_num(String r7_company_mob_phone_num) {
		this.r7_company_mob_phone_num = r7_company_mob_phone_num;
	}





	public String getR7_product_type() {
		return r7_product_type;
	}





	public void setR7_product_type(String r7_product_type) {
		this.r7_product_type = r7_product_type;
	}





	public BigDecimal getR7_acct_num() {
		return r7_acct_num;
	}





	public void setR7_acct_num(BigDecimal r7_acct_num) {
		this.r7_acct_num = r7_acct_num;
	}





	public String getR7_status_of_acct() {
		return r7_status_of_acct;
	}





	public void setR7_status_of_acct(String r7_status_of_acct) {
		this.r7_status_of_acct = r7_status_of_acct;
	}





	public String getR7_acct_status_fit_or_not_fit_for_straight_throu_payout() {
		return r7_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public void setR7_acct_status_fit_or_not_fit_for_straight_throu_payout(
			String r7_acct_status_fit_or_not_fit_for_straight_throu_payout) {
		this.r7_acct_status_fit_or_not_fit_for_straight_throu_payout = r7_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public String getR7_acct_branch() {
		return r7_acct_branch;
	}





	public void setR7_acct_branch(String r7_acct_branch) {
		this.r7_acct_branch = r7_acct_branch;
	}





	public BigDecimal getR7_acct_balance_pula() {
		return r7_acct_balance_pula;
	}





	public void setR7_acct_balance_pula(BigDecimal r7_acct_balance_pula) {
		this.r7_acct_balance_pula = r7_acct_balance_pula;
	}





	public String getR7_currency_of_acct() {
		return r7_currency_of_acct;
	}





	public void setR7_currency_of_acct(String r7_currency_of_acct) {
		this.r7_currency_of_acct = r7_currency_of_acct;
	}





	public BigDecimal getR7_exchange_rate() {
		return r7_exchange_rate;
	}





	public void setR7_exchange_rate(BigDecimal r7_exchange_rate) {
		this.r7_exchange_rate = r7_exchange_rate;
	}





	public BigDecimal getR8_bank_spec_single_cust_rec_num() {
		return r8_bank_spec_single_cust_rec_num;
	}





	public void setR8_bank_spec_single_cust_rec_num(BigDecimal r8_bank_spec_single_cust_rec_num) {
		this.r8_bank_spec_single_cust_rec_num = r8_bank_spec_single_cust_rec_num;
	}





	public String getR8_company_name() {
		return r8_company_name;
	}





	public void setR8_company_name(String r8_company_name) {
		this.r8_company_name = r8_company_name;
	}





	public BigDecimal getR8_company_reg_num() {
		return r8_company_reg_num;
	}





	public void setR8_company_reg_num(BigDecimal r8_company_reg_num) {
		this.r8_company_reg_num = r8_company_reg_num;
	}





	public String getR8_businees_phy_address() {
		return r8_businees_phy_address;
	}





	public void setR8_businees_phy_address(String r8_businees_phy_address) {
		this.r8_businees_phy_address = r8_businees_phy_address;
	}





	public String getR8_postal_address() {
		return r8_postal_address;
	}





	public void setR8_postal_address(String r8_postal_address) {
		this.r8_postal_address = r8_postal_address;
	}





	public String getR8_country_of_reg() {
		return r8_country_of_reg;
	}





	public void setR8_country_of_reg(String r8_country_of_reg) {
		this.r8_country_of_reg = r8_country_of_reg;
	}





	public String getR8_company_email() {
		return r8_company_email;
	}





	public void setR8_company_email(String r8_company_email) {
		this.r8_company_email = r8_company_email;
	}





	public String getR8_company_landline() {
		return r8_company_landline;
	}





	public void setR8_company_landline(String r8_company_landline) {
		this.r8_company_landline = r8_company_landline;
	}





	public String getR8_company_mob_phone_num() {
		return r8_company_mob_phone_num;
	}





	public void setR8_company_mob_phone_num(String r8_company_mob_phone_num) {
		this.r8_company_mob_phone_num = r8_company_mob_phone_num;
	}





	public String getR8_product_type() {
		return r8_product_type;
	}





	public void setR8_product_type(String r8_product_type) {
		this.r8_product_type = r8_product_type;
	}





	public BigDecimal getR8_acct_num() {
		return r8_acct_num;
	}





	public void setR8_acct_num(BigDecimal r8_acct_num) {
		this.r8_acct_num = r8_acct_num;
	}





	public String getR8_status_of_acct() {
		return r8_status_of_acct;
	}





	public void setR8_status_of_acct(String r8_status_of_acct) {
		this.r8_status_of_acct = r8_status_of_acct;
	}





	public String getR8_acct_status_fit_or_not_fit_for_straight_throu_payout() {
		return r8_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public void setR8_acct_status_fit_or_not_fit_for_straight_throu_payout(
			String r8_acct_status_fit_or_not_fit_for_straight_throu_payout) {
		this.r8_acct_status_fit_or_not_fit_for_straight_throu_payout = r8_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public String getR8_acct_branch() {
		return r8_acct_branch;
	}





	public void setR8_acct_branch(String r8_acct_branch) {
		this.r8_acct_branch = r8_acct_branch;
	}





	public BigDecimal getR8_acct_balance_pula() {
		return r8_acct_balance_pula;
	}





	public void setR8_acct_balance_pula(BigDecimal r8_acct_balance_pula) {
		this.r8_acct_balance_pula = r8_acct_balance_pula;
	}





	public String getR8_currency_of_acct() {
		return r8_currency_of_acct;
	}





	public void setR8_currency_of_acct(String r8_currency_of_acct) {
		this.r8_currency_of_acct = r8_currency_of_acct;
	}





	public BigDecimal getR8_exchange_rate() {
		return r8_exchange_rate;
	}





	public void setR8_exchange_rate(BigDecimal r8_exchange_rate) {
		this.r8_exchange_rate = r8_exchange_rate;
	}





	public BigDecimal getR9_bank_spec_single_cust_rec_num() {
		return r9_bank_spec_single_cust_rec_num;
	}





	public void setR9_bank_spec_single_cust_rec_num(BigDecimal r9_bank_spec_single_cust_rec_num) {
		this.r9_bank_spec_single_cust_rec_num = r9_bank_spec_single_cust_rec_num;
	}





	public String getR9_company_name() {
		return r9_company_name;
	}





	public void setR9_company_name(String r9_company_name) {
		this.r9_company_name = r9_company_name;
	}





	public BigDecimal getR9_company_reg_num() {
		return r9_company_reg_num;
	}





	public void setR9_company_reg_num(BigDecimal r9_company_reg_num) {
		this.r9_company_reg_num = r9_company_reg_num;
	}





	public String getR9_businees_phy_address() {
		return r9_businees_phy_address;
	}





	public void setR9_businees_phy_address(String r9_businees_phy_address) {
		this.r9_businees_phy_address = r9_businees_phy_address;
	}





	public String getR9_postal_address() {
		return r9_postal_address;
	}





	public void setR9_postal_address(String r9_postal_address) {
		this.r9_postal_address = r9_postal_address;
	}





	public String getR9_country_of_reg() {
		return r9_country_of_reg;
	}





	public void setR9_country_of_reg(String r9_country_of_reg) {
		this.r9_country_of_reg = r9_country_of_reg;
	}





	public String getR9_company_email() {
		return r9_company_email;
	}





	public void setR9_company_email(String r9_company_email) {
		this.r9_company_email = r9_company_email;
	}





	public String getR9_company_landline() {
		return r9_company_landline;
	}





	public void setR9_company_landline(String r9_company_landline) {
		this.r9_company_landline = r9_company_landline;
	}





	public String getR9_company_mob_phone_num() {
		return r9_company_mob_phone_num;
	}





	public void setR9_company_mob_phone_num(String r9_company_mob_phone_num) {
		this.r9_company_mob_phone_num = r9_company_mob_phone_num;
	}





	public String getR9_product_type() {
		return r9_product_type;
	}





	public void setR9_product_type(String r9_product_type) {
		this.r9_product_type = r9_product_type;
	}





	public BigDecimal getR9_acct_num() {
		return r9_acct_num;
	}





	public void setR9_acct_num(BigDecimal r9_acct_num) {
		this.r9_acct_num = r9_acct_num;
	}





	public String getR9_status_of_acct() {
		return r9_status_of_acct;
	}





	public void setR9_status_of_acct(String r9_status_of_acct) {
		this.r9_status_of_acct = r9_status_of_acct;
	}





	public String getR9_acct_status_fit_or_not_fit_for_straight_throu_payout() {
		return r9_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public void setR9_acct_status_fit_or_not_fit_for_straight_throu_payout(
			String r9_acct_status_fit_or_not_fit_for_straight_throu_payout) {
		this.r9_acct_status_fit_or_not_fit_for_straight_throu_payout = r9_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public String getR9_acct_branch() {
		return r9_acct_branch;
	}





	public void setR9_acct_branch(String r9_acct_branch) {
		this.r9_acct_branch = r9_acct_branch;
	}





	public BigDecimal getR9_acct_balance_pula() {
		return r9_acct_balance_pula;
	}





	public void setR9_acct_balance_pula(BigDecimal r9_acct_balance_pula) {
		this.r9_acct_balance_pula = r9_acct_balance_pula;
	}





	public String getR9_currency_of_acct() {
		return r9_currency_of_acct;
	}





	public void setR9_currency_of_acct(String r9_currency_of_acct) {
		this.r9_currency_of_acct = r9_currency_of_acct;
	}





	public BigDecimal getR9_exchange_rate() {
		return r9_exchange_rate;
	}





	public void setR9_exchange_rate(BigDecimal r9_exchange_rate) {
		this.r9_exchange_rate = r9_exchange_rate;
	}





	public BigDecimal getR10_bank_spec_single_cust_rec_num() {
		return r10_bank_spec_single_cust_rec_num;
	}





	public void setR10_bank_spec_single_cust_rec_num(BigDecimal r10_bank_spec_single_cust_rec_num) {
		this.r10_bank_spec_single_cust_rec_num = r10_bank_spec_single_cust_rec_num;
	}





	public String getR10_company_name() {
		return r10_company_name;
	}





	public void setR10_company_name(String r10_company_name) {
		this.r10_company_name = r10_company_name;
	}





	public BigDecimal getR10_company_reg_num() {
		return r10_company_reg_num;
	}





	public void setR10_company_reg_num(BigDecimal r10_company_reg_num) {
		this.r10_company_reg_num = r10_company_reg_num;
	}





	public String getR10_businees_phy_address() {
		return r10_businees_phy_address;
	}





	public void setR10_businees_phy_address(String r10_businees_phy_address) {
		this.r10_businees_phy_address = r10_businees_phy_address;
	}





	public String getR10_postal_address() {
		return r10_postal_address;
	}





	public void setR10_postal_address(String r10_postal_address) {
		this.r10_postal_address = r10_postal_address;
	}





	public String getR10_country_of_reg() {
		return r10_country_of_reg;
	}





	public void setR10_country_of_reg(String r10_country_of_reg) {
		this.r10_country_of_reg = r10_country_of_reg;
	}





	public String getR10_company_email() {
		return r10_company_email;
	}





	public void setR10_company_email(String r10_company_email) {
		this.r10_company_email = r10_company_email;
	}





	public String getR10_company_landline() {
		return r10_company_landline;
	}





	public void setR10_company_landline(String r10_company_landline) {
		this.r10_company_landline = r10_company_landline;
	}





	public String getR10_company_mob_phone_num() {
		return r10_company_mob_phone_num;
	}





	public void setR10_company_mob_phone_num(String r10_company_mob_phone_num) {
		this.r10_company_mob_phone_num = r10_company_mob_phone_num;
	}





	public String getR10_product_type() {
		return r10_product_type;
	}





	public void setR10_product_type(String r10_product_type) {
		this.r10_product_type = r10_product_type;
	}





	public BigDecimal getR10_acct_num() {
		return r10_acct_num;
	}





	public void setR10_acct_num(BigDecimal r10_acct_num) {
		this.r10_acct_num = r10_acct_num;
	}





	public String getR10_status_of_acct() {
		return r10_status_of_acct;
	}





	public void setR10_status_of_acct(String r10_status_of_acct) {
		this.r10_status_of_acct = r10_status_of_acct;
	}





	public String getR10_acct_status_fit_or_not_fit_for_straight_throu_payout() {
		return r10_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public void setR10_acct_status_fit_or_not_fit_for_straight_throu_payout(
			String r10_acct_status_fit_or_not_fit_for_straight_throu_payout) {
		this.r10_acct_status_fit_or_not_fit_for_straight_throu_payout = r10_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public String getR10_acct_branch() {
		return r10_acct_branch;
	}





	public void setR10_acct_branch(String r10_acct_branch) {
		this.r10_acct_branch = r10_acct_branch;
	}





	public BigDecimal getR10_acct_balance_pula() {
		return r10_acct_balance_pula;
	}





	public void setR10_acct_balance_pula(BigDecimal r10_acct_balance_pula) {
		this.r10_acct_balance_pula = r10_acct_balance_pula;
	}





	public String getR10_currency_of_acct() {
		return r10_currency_of_acct;
	}





	public void setR10_currency_of_acct(String r10_currency_of_acct) {
		this.r10_currency_of_acct = r10_currency_of_acct;
	}





	public BigDecimal getR10_exchange_rate() {
		return r10_exchange_rate;
	}





	public void setR10_exchange_rate(BigDecimal r10_exchange_rate) {
		this.r10_exchange_rate = r10_exchange_rate;
	}





	public BigDecimal getR11_bank_spec_single_cust_rec_num() {
		return r11_bank_spec_single_cust_rec_num;
	}





	public void setR11_bank_spec_single_cust_rec_num(BigDecimal r11_bank_spec_single_cust_rec_num) {
		this.r11_bank_spec_single_cust_rec_num = r11_bank_spec_single_cust_rec_num;
	}





	public String getR11_company_name() {
		return r11_company_name;
	}





	public void setR11_company_name(String r11_company_name) {
		this.r11_company_name = r11_company_name;
	}





	public BigDecimal getR11_company_reg_num() {
		return r11_company_reg_num;
	}





	public void setR11_company_reg_num(BigDecimal r11_company_reg_num) {
		this.r11_company_reg_num = r11_company_reg_num;
	}





	public String getR11_businees_phy_address() {
		return r11_businees_phy_address;
	}





	public void setR11_businees_phy_address(String r11_businees_phy_address) {
		this.r11_businees_phy_address = r11_businees_phy_address;
	}





	public String getR11_postal_address() {
		return r11_postal_address;
	}





	public void setR11_postal_address(String r11_postal_address) {
		this.r11_postal_address = r11_postal_address;
	}





	public String getR11_country_of_reg() {
		return r11_country_of_reg;
	}





	public void setR11_country_of_reg(String r11_country_of_reg) {
		this.r11_country_of_reg = r11_country_of_reg;
	}





	public String getR11_company_email() {
		return r11_company_email;
	}





	public void setR11_company_email(String r11_company_email) {
		this.r11_company_email = r11_company_email;
	}





	public String getR11_company_landline() {
		return r11_company_landline;
	}





	public void setR11_company_landline(String r11_company_landline) {
		this.r11_company_landline = r11_company_landline;
	}





	public String getR11_company_mob_phone_num() {
		return r11_company_mob_phone_num;
	}





	public void setR11_company_mob_phone_num(String r11_company_mob_phone_num) {
		this.r11_company_mob_phone_num = r11_company_mob_phone_num;
	}





	public String getR11_product_type() {
		return r11_product_type;
	}





	public void setR11_product_type(String r11_product_type) {
		this.r11_product_type = r11_product_type;
	}





	public BigDecimal getR11_acct_num() {
		return r11_acct_num;
	}





	public void setR11_acct_num(BigDecimal r11_acct_num) {
		this.r11_acct_num = r11_acct_num;
	}





	public String getR11_status_of_acct() {
		return r11_status_of_acct;
	}





	public void setR11_status_of_acct(String r11_status_of_acct) {
		this.r11_status_of_acct = r11_status_of_acct;
	}





	public String getR11_acct_status_fit_or_not_fit_for_straight_throu_payout() {
		return r11_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public void setR11_acct_status_fit_or_not_fit_for_straight_throu_payout(
			String r11_acct_status_fit_or_not_fit_for_straight_throu_payout) {
		this.r11_acct_status_fit_or_not_fit_for_straight_throu_payout = r11_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public String getR11_acct_branch() {
		return r11_acct_branch;
	}





	public void setR11_acct_branch(String r11_acct_branch) {
		this.r11_acct_branch = r11_acct_branch;
	}





	public BigDecimal getR11_acct_balance_pula() {
		return r11_acct_balance_pula;
	}





	public void setR11_acct_balance_pula(BigDecimal r11_acct_balance_pula) {
		this.r11_acct_balance_pula = r11_acct_balance_pula;
	}





	public String getR11_currency_of_acct() {
		return r11_currency_of_acct;
	}





	public void setR11_currency_of_acct(String r11_currency_of_acct) {
		this.r11_currency_of_acct = r11_currency_of_acct;
	}





	public BigDecimal getR11_exchange_rate() {
		return r11_exchange_rate;
	}





	public void setR11_exchange_rate(BigDecimal r11_exchange_rate) {
		this.r11_exchange_rate = r11_exchange_rate;
	}





	public BigDecimal getR12_bank_spec_single_cust_rec_num() {
		return r12_bank_spec_single_cust_rec_num;
	}





	public void setR12_bank_spec_single_cust_rec_num(BigDecimal r12_bank_spec_single_cust_rec_num) {
		this.r12_bank_spec_single_cust_rec_num = r12_bank_spec_single_cust_rec_num;
	}





	public String getR12_company_name() {
		return r12_company_name;
	}





	public void setR12_company_name(String r12_company_name) {
		this.r12_company_name = r12_company_name;
	}





	public BigDecimal getR12_company_reg_num() {
		return r12_company_reg_num;
	}





	public void setR12_company_reg_num(BigDecimal r12_company_reg_num) {
		this.r12_company_reg_num = r12_company_reg_num;
	}





	public String getR12_businees_phy_address() {
		return r12_businees_phy_address;
	}





	public void setR12_businees_phy_address(String r12_businees_phy_address) {
		this.r12_businees_phy_address = r12_businees_phy_address;
	}





	public String getR12_postal_address() {
		return r12_postal_address;
	}





	public void setR12_postal_address(String r12_postal_address) {
		this.r12_postal_address = r12_postal_address;
	}





	public String getR12_country_of_reg() {
		return r12_country_of_reg;
	}





	public void setR12_country_of_reg(String r12_country_of_reg) {
		this.r12_country_of_reg = r12_country_of_reg;
	}





	public String getR12_company_email() {
		return r12_company_email;
	}





	public void setR12_company_email(String r12_company_email) {
		this.r12_company_email = r12_company_email;
	}





	public String getR12_company_landline() {
		return r12_company_landline;
	}





	public void setR12_company_landline(String r12_company_landline) {
		this.r12_company_landline = r12_company_landline;
	}





	public String getR12_company_mob_phone_num() {
		return r12_company_mob_phone_num;
	}





	public void setR12_company_mob_phone_num(String r12_company_mob_phone_num) {
		this.r12_company_mob_phone_num = r12_company_mob_phone_num;
	}





	public String getR12_product_type() {
		return r12_product_type;
	}





	public void setR12_product_type(String r12_product_type) {
		this.r12_product_type = r12_product_type;
	}





	public BigDecimal getR12_acct_num() {
		return r12_acct_num;
	}





	public void setR12_acct_num(BigDecimal r12_acct_num) {
		this.r12_acct_num = r12_acct_num;
	}





	public String getR12_status_of_acct() {
		return r12_status_of_acct;
	}





	public void setR12_status_of_acct(String r12_status_of_acct) {
		this.r12_status_of_acct = r12_status_of_acct;
	}





	public String getR12_acct_status_fit_or_not_fit_for_straight_throu_payout() {
		return r12_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public void setR12_acct_status_fit_or_not_fit_for_straight_throu_payout(
			String r12_acct_status_fit_or_not_fit_for_straight_throu_payout) {
		this.r12_acct_status_fit_or_not_fit_for_straight_throu_payout = r12_acct_status_fit_or_not_fit_for_straight_throu_payout;
	}





	public String getR12_acct_branch() {
		return r12_acct_branch;
	}





	public void setR12_acct_branch(String r12_acct_branch) {
		this.r12_acct_branch = r12_acct_branch;
	}





	public BigDecimal getR12_acct_balance_pula() {
		return r12_acct_balance_pula;
	}





	public void setR12_acct_balance_pula(BigDecimal r12_acct_balance_pula) {
		this.r12_acct_balance_pula = r12_acct_balance_pula;
	}





	public String getR12_currency_of_acct() {
		return r12_currency_of_acct;
	}





	public void setR12_currency_of_acct(String r12_currency_of_acct) {
		this.r12_currency_of_acct = r12_currency_of_acct;
	}





	public BigDecimal getR12_exchange_rate() {
		return r12_exchange_rate;
	}





	public void setR12_exchange_rate(BigDecimal r12_exchange_rate) {
		this.r12_exchange_rate = r12_exchange_rate;
	}





	public BDISB2_Archival_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}

	
}