
package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_BDISB2_SUMMARYTABLE")

public class BDISB2_Summary_Entity{	




	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	
	@Column(name = "REPORT_VERSION")
	private String reportVersion;
	
	//@Column(name = "REPORT_RESUBDATE")
	//@Temporal(TemporalType.TIMESTAMP)
	//private Date reportResubDate;
	
	public String REPORT_FREQUENCY;
	public String REPORT_CODE;
	public String REPORT_DESC;
	public String ENTITY_FLG;
	public String MODIFY_FLG;
	public String DEL_FLG;

	private BigDecimal R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
	private String R6_COMPANY_NAME;
	private BigDecimal R6_COMPANY_REG_NUM;
	private String R6_BUSINEES_PHY_ADDRESS;
	private String R6_POSTAL_ADDRESS;
	private String R6_COUNTRY_OF_REG;
	private String R6_COMPANY_EMAIL;
	private String R6_COMPANY_LANDLINE;
	private String R6_COMPANY_MOB_PHONE_NUM;
	private String R6_PRODUCT_TYPE;
	private BigDecimal R6_ACCT_NUM;
	private String R6_STATUS_OF_ACCT;
	private String R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	private String R6_ACCT_BRANCH;
	private BigDecimal R6_ACCT_BALANCE_PULA;
	private String R6_CURRENCY_OF_ACCT;
	private BigDecimal R6_EXCHANGE_RATE;

	private BigDecimal R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
	private String R7_COMPANY_NAME;
	private BigDecimal R7_COMPANY_REG_NUM;
	private String R7_BUSINEES_PHY_ADDRESS;
	private String R7_POSTAL_ADDRESS;
	private String R7_COUNTRY_OF_REG;
	private String R7_COMPANY_EMAIL;
	private String R7_COMPANY_LANDLINE;
	private String R7_COMPANY_MOB_PHONE_NUM;
	private String R7_PRODUCT_TYPE;
	private BigDecimal R7_ACCT_NUM;
	private String R7_STATUS_OF_ACCT;
	private String R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	private String R7_ACCT_BRANCH;
	private BigDecimal R7_ACCT_BALANCE_PULA;
	private String R7_CURRENCY_OF_ACCT;
	private BigDecimal R7_EXCHANGE_RATE;

	private BigDecimal R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
	private String R8_COMPANY_NAME;
	private BigDecimal R8_COMPANY_REG_NUM;
	private String R8_BUSINEES_PHY_ADDRESS;
	private String R8_POSTAL_ADDRESS;
	private String R8_COUNTRY_OF_REG;
	private String R8_COMPANY_EMAIL;
	private String R8_COMPANY_LANDLINE;
	private String R8_COMPANY_MOB_PHONE_NUM;
	private String R8_PRODUCT_TYPE;
	private BigDecimal R8_ACCT_NUM;
	private String R8_STATUS_OF_ACCT;
	private String R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	private String R8_ACCT_BRANCH;
	private BigDecimal R8_ACCT_BALANCE_PULA;
	private String R8_CURRENCY_OF_ACCT;
	private BigDecimal R8_EXCHANGE_RATE;

	private BigDecimal R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
	private String R9_COMPANY_NAME;
	private BigDecimal R9_COMPANY_REG_NUM;
	private String R9_BUSINEES_PHY_ADDRESS;
	private String R9_POSTAL_ADDRESS;
	private String R9_COUNTRY_OF_REG;
	private String R9_COMPANY_EMAIL;
	private String R9_COMPANY_LANDLINE;
	private String R9_COMPANY_MOB_PHONE_NUM;
	private String R9_PRODUCT_TYPE;
	private BigDecimal R9_ACCT_NUM;
	private String R9_STATUS_OF_ACCT;
	private String R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	private String R9_ACCT_BRANCH;
	private BigDecimal R9_ACCT_BALANCE_PULA;
	private String R9_CURRENCY_OF_ACCT;
	private BigDecimal R9_EXCHANGE_RATE;

	private BigDecimal R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
	private String R10_COMPANY_NAME;
	private BigDecimal R10_COMPANY_REG_NUM;
	private String R10_BUSINEES_PHY_ADDRESS;
	private String R10_POSTAL_ADDRESS;
	private String R10_COUNTRY_OF_REG;
	private String R10_COMPANY_EMAIL;
	private String R10_COMPANY_LANDLINE;
	private String R10_COMPANY_MOB_PHONE_NUM;
	private String R10_PRODUCT_TYPE;
	private BigDecimal R10_ACCT_NUM;
	private String R10_STATUS_OF_ACCT;
	private String R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	private String R10_ACCT_BRANCH;
	private BigDecimal R10_ACCT_BALANCE_PULA;
	private String R10_CURRENCY_OF_ACCT;
	private BigDecimal R10_EXCHANGE_RATE;

	private BigDecimal R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
	private String R11_COMPANY_NAME;
	private BigDecimal R11_COMPANY_REG_NUM;
	private String R11_BUSINEES_PHY_ADDRESS;
	private String R11_POSTAL_ADDRESS;
	private String R11_COUNTRY_OF_REG;
	private String R11_COMPANY_EMAIL;
	private String R11_COMPANY_LANDLINE;
	private String R11_COMPANY_MOB_PHONE_NUM;
	private String R11_PRODUCT_TYPE;
	private BigDecimal R11_ACCT_NUM;
	private String R11_STATUS_OF_ACCT;
	private String R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	private String R11_ACCT_BRANCH;
	private BigDecimal R11_ACCT_BALANCE_PULA;
	private String R11_CURRENCY_OF_ACCT;
	private BigDecimal R11_EXCHANGE_RATE;

	private BigDecimal R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
	private String R12_COMPANY_NAME;
	private BigDecimal R12_COMPANY_REG_NUM;
	private String R12_BUSINEES_PHY_ADDRESS;
	private String R12_POSTAL_ADDRESS;
	private String R12_COUNTRY_OF_REG;
	private String R12_COMPANY_EMAIL;
	private String R12_COMPANY_LANDLINE;
	private String R12_COMPANY_MOB_PHONE_NUM;
	private String R12_PRODUCT_TYPE;
	private BigDecimal R12_ACCT_NUM;
	private String R12_STATUS_OF_ACCT;
	private String R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	private String R12_ACCT_BRANCH;
	private BigDecimal R12_ACCT_BALANCE_PULA;
	private String R12_CURRENCY_OF_ACCT;
	private BigDecimal R12_EXCHANGE_RATE;

	


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





	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}




	public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
		REPORT_FREQUENCY = rEPORT_FREQUENCY;
	}




	public String getREPORT_CODE() {
		return REPORT_CODE;
	}




	public void setREPORT_CODE(String rEPORT_CODE) {
		REPORT_CODE = rEPORT_CODE;
	}




	public String getREPORT_DESC() {
		return REPORT_DESC;
	}




	public void setREPORT_DESC(String rEPORT_DESC) {
		REPORT_DESC = rEPORT_DESC;
	}




	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}




	public void setENTITY_FLG(String eNTITY_FLG) {
		ENTITY_FLG = eNTITY_FLG;
	}




	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}




	public void setMODIFY_FLG(String mODIFY_FLG) {
		MODIFY_FLG = mODIFY_FLG;
	}




	public String getDEL_FLG() {
		return DEL_FLG;
	}




	public void setDEL_FLG(String dEL_FLG) {
		DEL_FLG = dEL_FLG;
	}




	public BigDecimal getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() {
		return R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public void setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(BigDecimal r6_BANK_SPEC_SINGLE_CUST_REC_NUM) {
		R6_BANK_SPEC_SINGLE_CUST_REC_NUM = r6_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public String getR6_COMPANY_NAME() {
		return R6_COMPANY_NAME;
	}




	public void setR6_COMPANY_NAME(String r6_COMPANY_NAME) {
		R6_COMPANY_NAME = r6_COMPANY_NAME;
	}




	public BigDecimal getR6_COMPANY_REG_NUM() {
		return R6_COMPANY_REG_NUM;
	}




	public void setR6_COMPANY_REG_NUM(BigDecimal r6_COMPANY_REG_NUM) {
		R6_COMPANY_REG_NUM = r6_COMPANY_REG_NUM;
	}




	public String getR6_BUSINEES_PHY_ADDRESS() {
		return R6_BUSINEES_PHY_ADDRESS;
	}




	public void setR6_BUSINEES_PHY_ADDRESS(String r6_BUSINEES_PHY_ADDRESS) {
		R6_BUSINEES_PHY_ADDRESS = r6_BUSINEES_PHY_ADDRESS;
	}




	public String getR6_POSTAL_ADDRESS() {
		return R6_POSTAL_ADDRESS;
	}




	public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
		R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
	}




	public String getR6_COUNTRY_OF_REG() {
		return R6_COUNTRY_OF_REG;
	}




	public void setR6_COUNTRY_OF_REG(String r6_COUNTRY_OF_REG) {
		R6_COUNTRY_OF_REG = r6_COUNTRY_OF_REG;
	}




	public String getR6_COMPANY_EMAIL() {
		return R6_COMPANY_EMAIL;
	}




	public void setR6_COMPANY_EMAIL(String r6_COMPANY_EMAIL) {
		R6_COMPANY_EMAIL = r6_COMPANY_EMAIL;
	}




	public String getR6_COMPANY_LANDLINE() {
		return R6_COMPANY_LANDLINE;
	}




	public void setR6_COMPANY_LANDLINE(String r6_COMPANY_LANDLINE) {
		R6_COMPANY_LANDLINE = r6_COMPANY_LANDLINE;
	}




	public String getR6_COMPANY_MOB_PHONE_NUM() {
		return R6_COMPANY_MOB_PHONE_NUM;
	}




	public void setR6_COMPANY_MOB_PHONE_NUM(String r6_COMPANY_MOB_PHONE_NUM) {
		R6_COMPANY_MOB_PHONE_NUM = r6_COMPANY_MOB_PHONE_NUM;
	}




	public String getR6_PRODUCT_TYPE() {
		return R6_PRODUCT_TYPE;
	}




	public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
		R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
	}




	public BigDecimal getR6_ACCT_NUM() {
		return R6_ACCT_NUM;
	}




	public void setR6_ACCT_NUM(BigDecimal r6_ACCT_NUM) {
		R6_ACCT_NUM = r6_ACCT_NUM;
	}




	public String getR6_STATUS_OF_ACCT() {
		return R6_STATUS_OF_ACCT;
	}




	public void setR6_STATUS_OF_ACCT(String r6_STATUS_OF_ACCT) {
		R6_STATUS_OF_ACCT = r6_STATUS_OF_ACCT;
	}




	public String getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
		return R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public void setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
			String r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
		R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public String getR6_ACCT_BRANCH() {
		return R6_ACCT_BRANCH;
	}




	public void setR6_ACCT_BRANCH(String r6_ACCT_BRANCH) {
		R6_ACCT_BRANCH = r6_ACCT_BRANCH;
	}




	public BigDecimal getR6_ACCT_BALANCE_PULA() {
		return R6_ACCT_BALANCE_PULA;
	}




	public void setR6_ACCT_BALANCE_PULA(BigDecimal r6_ACCT_BALANCE_PULA) {
		R6_ACCT_BALANCE_PULA = r6_ACCT_BALANCE_PULA;
	}




	public String getR6_CURRENCY_OF_ACCT() {
		return R6_CURRENCY_OF_ACCT;
	}




	public void setR6_CURRENCY_OF_ACCT(String r6_CURRENCY_OF_ACCT) {
		R6_CURRENCY_OF_ACCT = r6_CURRENCY_OF_ACCT;
	}




	public BigDecimal getR6_EXCHANGE_RATE() {
		return R6_EXCHANGE_RATE;
	}




	public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
		R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
	}




	public BigDecimal getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() {
		return R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public void setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(BigDecimal r7_BANK_SPEC_SINGLE_CUST_REC_NUM) {
		R7_BANK_SPEC_SINGLE_CUST_REC_NUM = r7_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public String getR7_COMPANY_NAME() {
		return R7_COMPANY_NAME;
	}




	public void setR7_COMPANY_NAME(String r7_COMPANY_NAME) {
		R7_COMPANY_NAME = r7_COMPANY_NAME;
	}




	public BigDecimal getR7_COMPANY_REG_NUM() {
		return R7_COMPANY_REG_NUM;
	}




	public void setR7_COMPANY_REG_NUM(BigDecimal r7_COMPANY_REG_NUM) {
		R7_COMPANY_REG_NUM = r7_COMPANY_REG_NUM;
	}




	public String getR7_BUSINEES_PHY_ADDRESS() {
		return R7_BUSINEES_PHY_ADDRESS;
	}




	public void setR7_BUSINEES_PHY_ADDRESS(String r7_BUSINEES_PHY_ADDRESS) {
		R7_BUSINEES_PHY_ADDRESS = r7_BUSINEES_PHY_ADDRESS;
	}




	public String getR7_POSTAL_ADDRESS() {
		return R7_POSTAL_ADDRESS;
	}




	public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
		R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
	}




	public String getR7_COUNTRY_OF_REG() {
		return R7_COUNTRY_OF_REG;
	}




	public void setR7_COUNTRY_OF_REG(String r7_COUNTRY_OF_REG) {
		R7_COUNTRY_OF_REG = r7_COUNTRY_OF_REG;
	}




	public String getR7_COMPANY_EMAIL() {
		return R7_COMPANY_EMAIL;
	}




	public void setR7_COMPANY_EMAIL(String r7_COMPANY_EMAIL) {
		R7_COMPANY_EMAIL = r7_COMPANY_EMAIL;
	}




	public String getR7_COMPANY_LANDLINE() {
		return R7_COMPANY_LANDLINE;
	}




	public void setR7_COMPANY_LANDLINE(String r7_COMPANY_LANDLINE) {
		R7_COMPANY_LANDLINE = r7_COMPANY_LANDLINE;
	}




	public String getR7_COMPANY_MOB_PHONE_NUM() {
		return R7_COMPANY_MOB_PHONE_NUM;
	}




	public void setR7_COMPANY_MOB_PHONE_NUM(String r7_COMPANY_MOB_PHONE_NUM) {
		R7_COMPANY_MOB_PHONE_NUM = r7_COMPANY_MOB_PHONE_NUM;
	}




	public String getR7_PRODUCT_TYPE() {
		return R7_PRODUCT_TYPE;
	}




	public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
		R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
	}




	public BigDecimal getR7_ACCT_NUM() {
		return R7_ACCT_NUM;
	}




	public void setR7_ACCT_NUM(BigDecimal r7_ACCT_NUM) {
		R7_ACCT_NUM = r7_ACCT_NUM;
	}




	public String getR7_STATUS_OF_ACCT() {
		return R7_STATUS_OF_ACCT;
	}




	public void setR7_STATUS_OF_ACCT(String r7_STATUS_OF_ACCT) {
		R7_STATUS_OF_ACCT = r7_STATUS_OF_ACCT;
	}




	public String getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
		return R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public void setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
			String r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
		R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public String getR7_ACCT_BRANCH() {
		return R7_ACCT_BRANCH;
	}




	public void setR7_ACCT_BRANCH(String r7_ACCT_BRANCH) {
		R7_ACCT_BRANCH = r7_ACCT_BRANCH;
	}




	public BigDecimal getR7_ACCT_BALANCE_PULA() {
		return R7_ACCT_BALANCE_PULA;
	}




	public void setR7_ACCT_BALANCE_PULA(BigDecimal r7_ACCT_BALANCE_PULA) {
		R7_ACCT_BALANCE_PULA = r7_ACCT_BALANCE_PULA;
	}




	public String getR7_CURRENCY_OF_ACCT() {
		return R7_CURRENCY_OF_ACCT;
	}




	public void setR7_CURRENCY_OF_ACCT(String r7_CURRENCY_OF_ACCT) {
		R7_CURRENCY_OF_ACCT = r7_CURRENCY_OF_ACCT;
	}




	public BigDecimal getR7_EXCHANGE_RATE() {
		return R7_EXCHANGE_RATE;
	}




	public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
		R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
	}




	public BigDecimal getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() {
		return R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public void setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(BigDecimal r8_BANK_SPEC_SINGLE_CUST_REC_NUM) {
		R8_BANK_SPEC_SINGLE_CUST_REC_NUM = r8_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public String getR8_COMPANY_NAME() {
		return R8_COMPANY_NAME;
	}




	public void setR8_COMPANY_NAME(String r8_COMPANY_NAME) {
		R8_COMPANY_NAME = r8_COMPANY_NAME;
	}




	public BigDecimal getR8_COMPANY_REG_NUM() {
		return R8_COMPANY_REG_NUM;
	}




	public void setR8_COMPANY_REG_NUM(BigDecimal r8_COMPANY_REG_NUM) {
		R8_COMPANY_REG_NUM = r8_COMPANY_REG_NUM;
	}




	public String getR8_BUSINEES_PHY_ADDRESS() {
		return R8_BUSINEES_PHY_ADDRESS;
	}




	public void setR8_BUSINEES_PHY_ADDRESS(String r8_BUSINEES_PHY_ADDRESS) {
		R8_BUSINEES_PHY_ADDRESS = r8_BUSINEES_PHY_ADDRESS;
	}




	public String getR8_POSTAL_ADDRESS() {
		return R8_POSTAL_ADDRESS;
	}




	public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
		R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
	}




	public String getR8_COUNTRY_OF_REG() {
		return R8_COUNTRY_OF_REG;
	}




	public void setR8_COUNTRY_OF_REG(String r8_COUNTRY_OF_REG) {
		R8_COUNTRY_OF_REG = r8_COUNTRY_OF_REG;
	}




	public String getR8_COMPANY_EMAIL() {
		return R8_COMPANY_EMAIL;
	}




	public void setR8_COMPANY_EMAIL(String r8_COMPANY_EMAIL) {
		R8_COMPANY_EMAIL = r8_COMPANY_EMAIL;
	}




	public String getR8_COMPANY_LANDLINE() {
		return R8_COMPANY_LANDLINE;
	}




	public void setR8_COMPANY_LANDLINE(String r8_COMPANY_LANDLINE) {
		R8_COMPANY_LANDLINE = r8_COMPANY_LANDLINE;
	}




	public String getR8_COMPANY_MOB_PHONE_NUM() {
		return R8_COMPANY_MOB_PHONE_NUM;
	}




	public void setR8_COMPANY_MOB_PHONE_NUM(String r8_COMPANY_MOB_PHONE_NUM) {
		R8_COMPANY_MOB_PHONE_NUM = r8_COMPANY_MOB_PHONE_NUM;
	}




	public String getR8_PRODUCT_TYPE() {
		return R8_PRODUCT_TYPE;
	}




	public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
		R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
	}




	public BigDecimal getR8_ACCT_NUM() {
		return R8_ACCT_NUM;
	}




	public void setR8_ACCT_NUM(BigDecimal r8_ACCT_NUM) {
		R8_ACCT_NUM = r8_ACCT_NUM;
	}




	public String getR8_STATUS_OF_ACCT() {
		return R8_STATUS_OF_ACCT;
	}




	public void setR8_STATUS_OF_ACCT(String r8_STATUS_OF_ACCT) {
		R8_STATUS_OF_ACCT = r8_STATUS_OF_ACCT;
	}




	public String getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
		return R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public void setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
			String r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
		R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public String getR8_ACCT_BRANCH() {
		return R8_ACCT_BRANCH;
	}




	public void setR8_ACCT_BRANCH(String r8_ACCT_BRANCH) {
		R8_ACCT_BRANCH = r8_ACCT_BRANCH;
	}




	public BigDecimal getR8_ACCT_BALANCE_PULA() {
		return R8_ACCT_BALANCE_PULA;
	}




	public void setR8_ACCT_BALANCE_PULA(BigDecimal r8_ACCT_BALANCE_PULA) {
		R8_ACCT_BALANCE_PULA = r8_ACCT_BALANCE_PULA;
	}




	public String getR8_CURRENCY_OF_ACCT() {
		return R8_CURRENCY_OF_ACCT;
	}




	public void setR8_CURRENCY_OF_ACCT(String r8_CURRENCY_OF_ACCT) {
		R8_CURRENCY_OF_ACCT = r8_CURRENCY_OF_ACCT;
	}




	public BigDecimal getR8_EXCHANGE_RATE() {
		return R8_EXCHANGE_RATE;
	}




	public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
		R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
	}




	public BigDecimal getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() {
		return R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public void setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(BigDecimal r9_BANK_SPEC_SINGLE_CUST_REC_NUM) {
		R9_BANK_SPEC_SINGLE_CUST_REC_NUM = r9_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public String getR9_COMPANY_NAME() {
		return R9_COMPANY_NAME;
	}




	public void setR9_COMPANY_NAME(String r9_COMPANY_NAME) {
		R9_COMPANY_NAME = r9_COMPANY_NAME;
	}




	public BigDecimal getR9_COMPANY_REG_NUM() {
		return R9_COMPANY_REG_NUM;
	}




	public void setR9_COMPANY_REG_NUM(BigDecimal r9_COMPANY_REG_NUM) {
		R9_COMPANY_REG_NUM = r9_COMPANY_REG_NUM;
	}




	public String getR9_BUSINEES_PHY_ADDRESS() {
		return R9_BUSINEES_PHY_ADDRESS;
	}




	public void setR9_BUSINEES_PHY_ADDRESS(String r9_BUSINEES_PHY_ADDRESS) {
		R9_BUSINEES_PHY_ADDRESS = r9_BUSINEES_PHY_ADDRESS;
	}




	public String getR9_POSTAL_ADDRESS() {
		return R9_POSTAL_ADDRESS;
	}




	public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
		R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
	}




	public String getR9_COUNTRY_OF_REG() {
		return R9_COUNTRY_OF_REG;
	}




	public void setR9_COUNTRY_OF_REG(String r9_COUNTRY_OF_REG) {
		R9_COUNTRY_OF_REG = r9_COUNTRY_OF_REG;
	}




	public String getR9_COMPANY_EMAIL() {
		return R9_COMPANY_EMAIL;
	}




	public void setR9_COMPANY_EMAIL(String r9_COMPANY_EMAIL) {
		R9_COMPANY_EMAIL = r9_COMPANY_EMAIL;
	}




	public String getR9_COMPANY_LANDLINE() {
		return R9_COMPANY_LANDLINE;
	}




	public void setR9_COMPANY_LANDLINE(String r9_COMPANY_LANDLINE) {
		R9_COMPANY_LANDLINE = r9_COMPANY_LANDLINE;
	}




	public String getR9_COMPANY_MOB_PHONE_NUM() {
		return R9_COMPANY_MOB_PHONE_NUM;
	}




	public void setR9_COMPANY_MOB_PHONE_NUM(String r9_COMPANY_MOB_PHONE_NUM) {
		R9_COMPANY_MOB_PHONE_NUM = r9_COMPANY_MOB_PHONE_NUM;
	}




	public String getR9_PRODUCT_TYPE() {
		return R9_PRODUCT_TYPE;
	}




	public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
		R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
	}




	public BigDecimal getR9_ACCT_NUM() {
		return R9_ACCT_NUM;
	}




	public void setR9_ACCT_NUM(BigDecimal r9_ACCT_NUM) {
		R9_ACCT_NUM = r9_ACCT_NUM;
	}




	public String getR9_STATUS_OF_ACCT() {
		return R9_STATUS_OF_ACCT;
	}




	public void setR9_STATUS_OF_ACCT(String r9_STATUS_OF_ACCT) {
		R9_STATUS_OF_ACCT = r9_STATUS_OF_ACCT;
	}




	public String getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
		return R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public void setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
			String r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
		R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public String getR9_ACCT_BRANCH() {
		return R9_ACCT_BRANCH;
	}




	public void setR9_ACCT_BRANCH(String r9_ACCT_BRANCH) {
		R9_ACCT_BRANCH = r9_ACCT_BRANCH;
	}




	public BigDecimal getR9_ACCT_BALANCE_PULA() {
		return R9_ACCT_BALANCE_PULA;
	}




	public void setR9_ACCT_BALANCE_PULA(BigDecimal r9_ACCT_BALANCE_PULA) {
		R9_ACCT_BALANCE_PULA = r9_ACCT_BALANCE_PULA;
	}




	public String getR9_CURRENCY_OF_ACCT() {
		return R9_CURRENCY_OF_ACCT;
	}




	public void setR9_CURRENCY_OF_ACCT(String r9_CURRENCY_OF_ACCT) {
		R9_CURRENCY_OF_ACCT = r9_CURRENCY_OF_ACCT;
	}




	public BigDecimal getR9_EXCHANGE_RATE() {
		return R9_EXCHANGE_RATE;
	}




	public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
		R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
	}




	public BigDecimal getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() {
		return R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public void setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(BigDecimal r10_BANK_SPEC_SINGLE_CUST_REC_NUM) {
		R10_BANK_SPEC_SINGLE_CUST_REC_NUM = r10_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public String getR10_COMPANY_NAME() {
		return R10_COMPANY_NAME;
	}




	public void setR10_COMPANY_NAME(String r10_COMPANY_NAME) {
		R10_COMPANY_NAME = r10_COMPANY_NAME;
	}




	public BigDecimal getR10_COMPANY_REG_NUM() {
		return R10_COMPANY_REG_NUM;
	}




	public void setR10_COMPANY_REG_NUM(BigDecimal r10_COMPANY_REG_NUM) {
		R10_COMPANY_REG_NUM = r10_COMPANY_REG_NUM;
	}




	public String getR10_BUSINEES_PHY_ADDRESS() {
		return R10_BUSINEES_PHY_ADDRESS;
	}




	public void setR10_BUSINEES_PHY_ADDRESS(String r10_BUSINEES_PHY_ADDRESS) {
		R10_BUSINEES_PHY_ADDRESS = r10_BUSINEES_PHY_ADDRESS;
	}




	public String getR10_POSTAL_ADDRESS() {
		return R10_POSTAL_ADDRESS;
	}




	public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
		R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
	}




	public String getR10_COUNTRY_OF_REG() {
		return R10_COUNTRY_OF_REG;
	}




	public void setR10_COUNTRY_OF_REG(String r10_COUNTRY_OF_REG) {
		R10_COUNTRY_OF_REG = r10_COUNTRY_OF_REG;
	}




	public String getR10_COMPANY_EMAIL() {
		return R10_COMPANY_EMAIL;
	}




	public void setR10_COMPANY_EMAIL(String r10_COMPANY_EMAIL) {
		R10_COMPANY_EMAIL = r10_COMPANY_EMAIL;
	}




	public String getR10_COMPANY_LANDLINE() {
		return R10_COMPANY_LANDLINE;
	}




	public void setR10_COMPANY_LANDLINE(String r10_COMPANY_LANDLINE) {
		R10_COMPANY_LANDLINE = r10_COMPANY_LANDLINE;
	}




	public String getR10_COMPANY_MOB_PHONE_NUM() {
		return R10_COMPANY_MOB_PHONE_NUM;
	}




	public void setR10_COMPANY_MOB_PHONE_NUM(String r10_COMPANY_MOB_PHONE_NUM) {
		R10_COMPANY_MOB_PHONE_NUM = r10_COMPANY_MOB_PHONE_NUM;
	}




	public String getR10_PRODUCT_TYPE() {
		return R10_PRODUCT_TYPE;
	}




	public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
		R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
	}




	public BigDecimal getR10_ACCT_NUM() {
		return R10_ACCT_NUM;
	}




	public void setR10_ACCT_NUM(BigDecimal r10_ACCT_NUM) {
		R10_ACCT_NUM = r10_ACCT_NUM;
	}




	public String getR10_STATUS_OF_ACCT() {
		return R10_STATUS_OF_ACCT;
	}




	public void setR10_STATUS_OF_ACCT(String r10_STATUS_OF_ACCT) {
		R10_STATUS_OF_ACCT = r10_STATUS_OF_ACCT;
	}




	public String getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
		return R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public void setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
			String r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
		R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public String getR10_ACCT_BRANCH() {
		return R10_ACCT_BRANCH;
	}




	public void setR10_ACCT_BRANCH(String r10_ACCT_BRANCH) {
		R10_ACCT_BRANCH = r10_ACCT_BRANCH;
	}




	public BigDecimal getR10_ACCT_BALANCE_PULA() {
		return R10_ACCT_BALANCE_PULA;
	}




	public void setR10_ACCT_BALANCE_PULA(BigDecimal r10_ACCT_BALANCE_PULA) {
		R10_ACCT_BALANCE_PULA = r10_ACCT_BALANCE_PULA;
	}




	public String getR10_CURRENCY_OF_ACCT() {
		return R10_CURRENCY_OF_ACCT;
	}




	public void setR10_CURRENCY_OF_ACCT(String r10_CURRENCY_OF_ACCT) {
		R10_CURRENCY_OF_ACCT = r10_CURRENCY_OF_ACCT;
	}




	public BigDecimal getR10_EXCHANGE_RATE() {
		return R10_EXCHANGE_RATE;
	}




	public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
		R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
	}




	public BigDecimal getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() {
		return R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public void setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(BigDecimal r11_BANK_SPEC_SINGLE_CUST_REC_NUM) {
		R11_BANK_SPEC_SINGLE_CUST_REC_NUM = r11_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public String getR11_COMPANY_NAME() {
		return R11_COMPANY_NAME;
	}




	public void setR11_COMPANY_NAME(String r11_COMPANY_NAME) {
		R11_COMPANY_NAME = r11_COMPANY_NAME;
	}




	public BigDecimal getR11_COMPANY_REG_NUM() {
		return R11_COMPANY_REG_NUM;
	}




	public void setR11_COMPANY_REG_NUM(BigDecimal r11_COMPANY_REG_NUM) {
		R11_COMPANY_REG_NUM = r11_COMPANY_REG_NUM;
	}




	public String getR11_BUSINEES_PHY_ADDRESS() {
		return R11_BUSINEES_PHY_ADDRESS;
	}




	public void setR11_BUSINEES_PHY_ADDRESS(String r11_BUSINEES_PHY_ADDRESS) {
		R11_BUSINEES_PHY_ADDRESS = r11_BUSINEES_PHY_ADDRESS;
	}




	public String getR11_POSTAL_ADDRESS() {
		return R11_POSTAL_ADDRESS;
	}




	public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
		R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
	}




	public String getR11_COUNTRY_OF_REG() {
		return R11_COUNTRY_OF_REG;
	}




	public void setR11_COUNTRY_OF_REG(String r11_COUNTRY_OF_REG) {
		R11_COUNTRY_OF_REG = r11_COUNTRY_OF_REG;
	}




	public String getR11_COMPANY_EMAIL() {
		return R11_COMPANY_EMAIL;
	}




	public void setR11_COMPANY_EMAIL(String r11_COMPANY_EMAIL) {
		R11_COMPANY_EMAIL = r11_COMPANY_EMAIL;
	}




	public String getR11_COMPANY_LANDLINE() {
		return R11_COMPANY_LANDLINE;
	}




	public void setR11_COMPANY_LANDLINE(String r11_COMPANY_LANDLINE) {
		R11_COMPANY_LANDLINE = r11_COMPANY_LANDLINE;
	}




	public String getR11_COMPANY_MOB_PHONE_NUM() {
		return R11_COMPANY_MOB_PHONE_NUM;
	}




	public void setR11_COMPANY_MOB_PHONE_NUM(String r11_COMPANY_MOB_PHONE_NUM) {
		R11_COMPANY_MOB_PHONE_NUM = r11_COMPANY_MOB_PHONE_NUM;
	}




	public String getR11_PRODUCT_TYPE() {
		return R11_PRODUCT_TYPE;
	}




	public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
		R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
	}




	public BigDecimal getR11_ACCT_NUM() {
		return R11_ACCT_NUM;
	}




	public void setR11_ACCT_NUM(BigDecimal r11_ACCT_NUM) {
		R11_ACCT_NUM = r11_ACCT_NUM;
	}




	public String getR11_STATUS_OF_ACCT() {
		return R11_STATUS_OF_ACCT;
	}




	public void setR11_STATUS_OF_ACCT(String r11_STATUS_OF_ACCT) {
		R11_STATUS_OF_ACCT = r11_STATUS_OF_ACCT;
	}




	public String getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
		return R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public void setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
			String r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
		R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public String getR11_ACCT_BRANCH() {
		return R11_ACCT_BRANCH;
	}




	public void setR11_ACCT_BRANCH(String r11_ACCT_BRANCH) {
		R11_ACCT_BRANCH = r11_ACCT_BRANCH;
	}




	public BigDecimal getR11_ACCT_BALANCE_PULA() {
		return R11_ACCT_BALANCE_PULA;
	}




	public void setR11_ACCT_BALANCE_PULA(BigDecimal r11_ACCT_BALANCE_PULA) {
		R11_ACCT_BALANCE_PULA = r11_ACCT_BALANCE_PULA;
	}




	public String getR11_CURRENCY_OF_ACCT() {
		return R11_CURRENCY_OF_ACCT;
	}




	public void setR11_CURRENCY_OF_ACCT(String r11_CURRENCY_OF_ACCT) {
		R11_CURRENCY_OF_ACCT = r11_CURRENCY_OF_ACCT;
	}




	public BigDecimal getR11_EXCHANGE_RATE() {
		return R11_EXCHANGE_RATE;
	}




	public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
		R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
	}




	public BigDecimal getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() {
		return R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public void setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(BigDecimal r12_BANK_SPEC_SINGLE_CUST_REC_NUM) {
		R12_BANK_SPEC_SINGLE_CUST_REC_NUM = r12_BANK_SPEC_SINGLE_CUST_REC_NUM;
	}




	public String getR12_COMPANY_NAME() {
		return R12_COMPANY_NAME;
	}




	public void setR12_COMPANY_NAME(String r12_COMPANY_NAME) {
		R12_COMPANY_NAME = r12_COMPANY_NAME;
	}




	public BigDecimal getR12_COMPANY_REG_NUM() {
		return R12_COMPANY_REG_NUM;
	}




	public void setR12_COMPANY_REG_NUM(BigDecimal r12_COMPANY_REG_NUM) {
		R12_COMPANY_REG_NUM = r12_COMPANY_REG_NUM;
	}




	public String getR12_BUSINEES_PHY_ADDRESS() {
		return R12_BUSINEES_PHY_ADDRESS;
	}




	public void setR12_BUSINEES_PHY_ADDRESS(String r12_BUSINEES_PHY_ADDRESS) {
		R12_BUSINEES_PHY_ADDRESS = r12_BUSINEES_PHY_ADDRESS;
	}




	public String getR12_POSTAL_ADDRESS() {
		return R12_POSTAL_ADDRESS;
	}




	public void setR12_POSTAL_ADDRESS(String r12_POSTAL_ADDRESS) {
		R12_POSTAL_ADDRESS = r12_POSTAL_ADDRESS;
	}




	public String getR12_COUNTRY_OF_REG() {
		return R12_COUNTRY_OF_REG;
	}




	public void setR12_COUNTRY_OF_REG(String r12_COUNTRY_OF_REG) {
		R12_COUNTRY_OF_REG = r12_COUNTRY_OF_REG;
	}




	public String getR12_COMPANY_EMAIL() {
		return R12_COMPANY_EMAIL;
	}




	public void setR12_COMPANY_EMAIL(String r12_COMPANY_EMAIL) {
		R12_COMPANY_EMAIL = r12_COMPANY_EMAIL;
	}




	public String getR12_COMPANY_LANDLINE() {
		return R12_COMPANY_LANDLINE;
	}




	public void setR12_COMPANY_LANDLINE(String r12_COMPANY_LANDLINE) {
		R12_COMPANY_LANDLINE = r12_COMPANY_LANDLINE;
	}




	public String getR12_COMPANY_MOB_PHONE_NUM() {
		return R12_COMPANY_MOB_PHONE_NUM;
	}




	public void setR12_COMPANY_MOB_PHONE_NUM(String r12_COMPANY_MOB_PHONE_NUM) {
		R12_COMPANY_MOB_PHONE_NUM = r12_COMPANY_MOB_PHONE_NUM;
	}




	public String getR12_PRODUCT_TYPE() {
		return R12_PRODUCT_TYPE;
	}




	public void setR12_PRODUCT_TYPE(String r12_PRODUCT_TYPE) {
		R12_PRODUCT_TYPE = r12_PRODUCT_TYPE;
	}




	public BigDecimal getR12_ACCT_NUM() {
		return R12_ACCT_NUM;
	}




	public void setR12_ACCT_NUM(BigDecimal r12_ACCT_NUM) {
		R12_ACCT_NUM = r12_ACCT_NUM;
	}




	public String getR12_STATUS_OF_ACCT() {
		return R12_STATUS_OF_ACCT;
	}




	public void setR12_STATUS_OF_ACCT(String r12_STATUS_OF_ACCT) {
		R12_STATUS_OF_ACCT = r12_STATUS_OF_ACCT;
	}




	public String getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
		return R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public void setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
			String r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
		R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
	}




	public String getR12_ACCT_BRANCH() {
		return R12_ACCT_BRANCH;
	}




	public void setR12_ACCT_BRANCH(String r12_ACCT_BRANCH) {
		R12_ACCT_BRANCH = r12_ACCT_BRANCH;
	}




	public BigDecimal getR12_ACCT_BALANCE_PULA() {
		return R12_ACCT_BALANCE_PULA;
	}




	public void setR12_ACCT_BALANCE_PULA(BigDecimal r12_ACCT_BALANCE_PULA) {
		R12_ACCT_BALANCE_PULA = r12_ACCT_BALANCE_PULA;
	}




	public String getR12_CURRENCY_OF_ACCT() {
		return R12_CURRENCY_OF_ACCT;
	}




	public void setR12_CURRENCY_OF_ACCT(String r12_CURRENCY_OF_ACCT) {
		R12_CURRENCY_OF_ACCT = r12_CURRENCY_OF_ACCT;
	}




	public BigDecimal getR12_EXCHANGE_RATE() {
		return R12_EXCHANGE_RATE;
	}




	public void setR12_EXCHANGE_RATE(BigDecimal r12_EXCHANGE_RATE) {
		R12_EXCHANGE_RATE = r12_EXCHANGE_RATE;
	}




	public BDISB2_Summary_Entity() {
	super();
	// TODO Auto-generated constructor stub
}

	
}