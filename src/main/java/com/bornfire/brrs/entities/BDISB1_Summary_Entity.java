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
@Table(name = "BRRS_BDISB1_SUMMARYTABLE")
public class BDISB1_Summary_Entity {
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;
	
	//@Column(name = "REPORT_RESUBDATE")
	//@Temporal(TemporalType.TIMESTAMP)
	//private Date reportResubDate;
	
	public String report_frequency;
	public String report_code;
	public String report_desc;
	public String entity_flg;
	public String modify_flg;
	public String del_flg;
	

    // ===================== R5 =====================
    private String R5_RECORD_NUMBER;
    private String R5_TITLE;
    private String R5_FIRST_NAME;
    private String R5_MIDDLE_NAME;
    private String R5_SURNAME;
    private String R5_PREVIOUS_NAME;
    private String R5_GENDER;
    private String R5_IDENTIFICATION_TYPE;
    private String R5_PASSPORT_NUMBER;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R5_DATE_OF_BIRTH;
    private String R5_HOME_ADDRESS;
    private String R5_POSTAL_ADDRESS;
    private String R5_RESIDENCE;
    private String R5_EMAIL;
    private String R5_LANDLINE;
    private String R5_MOBILE_PHONE_NUMBER;
    private String R5_MOBILE_MONEY_NUMBER;
    private String R5_PRODUCT_TYPE;
    private String R5_ACCOUNT_BY_OWNERSHIP;
    private String R5_ACCOUNT_NUMBER;
    private BigDecimal R5_ACCOUNT_HOLDER_INDICATOR;
    private String R5_STATUS_OF_ACCOUNT;
    private String R5_NOT_FIT_FOR_STP;
    private String R5_BRANCH_CODE_AND_NAME;
    private BigDecimal R5_ACCOUNT_BALANCE_IN_PULA;
    private String R5_CURRENCY_OF_ACCOUNT;
    private BigDecimal R5_EXCHANGE_RATE;

    // ===================== R6 =====================
    private String R6_RECORD_NUMBER;
    private String R6_TITLE;
    private String R6_FIRST_NAME;
    private String R6_MIDDLE_NAME;
    private String R6_SURNAME;
    private String R6_PREVIOUS_NAME;
    private String R6_GENDER;
    private String R6_IDENTIFICATION_TYPE;
    private String R6_PASSPORT_NUMBER;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R6_DATE_OF_BIRTH;
    private String R6_HOME_ADDRESS;
    private String R6_POSTAL_ADDRESS;
    private String R6_RESIDENCE;
    private String R6_EMAIL;
    private String R6_LANDLINE;
    private String R6_MOBILE_PHONE_NUMBER;
    private String R6_MOBILE_MONEY_NUMBER;
    private String R6_PRODUCT_TYPE;
    private String R6_ACCOUNT_BY_OWNERSHIP;
    private String R6_ACCOUNT_NUMBER;
    private BigDecimal R6_ACCOUNT_HOLDER_INDICATOR;
    private String R6_STATUS_OF_ACCOUNT;
    private String R6_NOT_FIT_FOR_STP;
    private String R6_BRANCH_CODE_AND_NAME;
    private BigDecimal R6_ACCOUNT_BALANCE_IN_PULA;
    private String R6_CURRENCY_OF_ACCOUNT;
    private BigDecimal R6_EXCHANGE_RATE;

    // ===================== R7 =====================
    private String R7_RECORD_NUMBER;
    private String R7_TITLE;
    private String R7_FIRST_NAME;
    private String R7_MIDDLE_NAME;
    private String R7_SURNAME;
    private String R7_PREVIOUS_NAME;
    private String R7_GENDER;
    private String R7_IDENTIFICATION_TYPE;
    private String R7_PASSPORT_NUMBER;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R7_DATE_OF_BIRTH;
    private String R7_HOME_ADDRESS;
    private String R7_POSTAL_ADDRESS;
    private String R7_RESIDENCE;
    private String R7_EMAIL;
    private String R7_LANDLINE;
    private String R7_MOBILE_PHONE_NUMBER;
    private String R7_MOBILE_MONEY_NUMBER;
    private String R7_PRODUCT_TYPE;
    private String R7_ACCOUNT_BY_OWNERSHIP;
    private String R7_ACCOUNT_NUMBER;
    private BigDecimal R7_ACCOUNT_HOLDER_INDICATOR;
    private String R7_STATUS_OF_ACCOUNT;
    private String R7_NOT_FIT_FOR_STP;
    private String R7_BRANCH_CODE_AND_NAME;
    private BigDecimal R7_ACCOUNT_BALANCE_IN_PULA;
    private String R7_CURRENCY_OF_ACCOUNT;
    private BigDecimal R7_EXCHANGE_RATE;

    // ===================== R8 =====================
    private String R8_RECORD_NUMBER;
    private String R8_TITLE;
    private String R8_FIRST_NAME;
    private String R8_MIDDLE_NAME;
    private String R8_SURNAME;
    private String R8_PREVIOUS_NAME;
    private String R8_GENDER;
    private String R8_IDENTIFICATION_TYPE;
    private String R8_PASSPORT_NUMBER;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R8_DATE_OF_BIRTH;
    private String R8_HOME_ADDRESS;
    private String R8_POSTAL_ADDRESS;
    private String R8_RESIDENCE;
    private String R8_EMAIL;
    private String R8_LANDLINE;
    private String R8_MOBILE_PHONE_NUMBER;
    private String R8_MOBILE_MONEY_NUMBER;
    private String R8_PRODUCT_TYPE;
    private String R8_ACCOUNT_BY_OWNERSHIP;
    private String R8_ACCOUNT_NUMBER;
    private BigDecimal R8_ACCOUNT_HOLDER_INDICATOR;
    private String R8_STATUS_OF_ACCOUNT;
    private String R8_NOT_FIT_FOR_STP;
    private String R8_BRANCH_CODE_AND_NAME;
    private BigDecimal R8_ACCOUNT_BALANCE_IN_PULA;
    private String R8_CURRENCY_OF_ACCOUNT;
    private BigDecimal R8_EXCHANGE_RATE;

    // ===================== R9 =====================
    private String R9_RECORD_NUMBER;
    private String R9_TITLE;
    private String R9_FIRST_NAME;
    private String R9_MIDDLE_NAME;
    private String R9_SURNAME;
    private String R9_PREVIOUS_NAME;
    private String R9_GENDER;
    private String R9_IDENTIFICATION_TYPE;
    private String R9_PASSPORT_NUMBER;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R9_DATE_OF_BIRTH;
    private String R9_HOME_ADDRESS;
    private String R9_POSTAL_ADDRESS;
    private String R9_RESIDENCE;
    private String R9_EMAIL;
    private String R9_LANDLINE;
    private String R9_MOBILE_PHONE_NUMBER;
    private String R9_MOBILE_MONEY_NUMBER;
    private String R9_PRODUCT_TYPE;
    private String R9_ACCOUNT_BY_OWNERSHIP;
    private String R9_ACCOUNT_NUMBER;
    private BigDecimal R9_ACCOUNT_HOLDER_INDICATOR;
    private String R9_STATUS_OF_ACCOUNT;
    private String R9_NOT_FIT_FOR_STP;
    private String R9_BRANCH_CODE_AND_NAME;
    private BigDecimal R9_ACCOUNT_BALANCE_IN_PULA;
    private String R9_CURRENCY_OF_ACCOUNT;
    private BigDecimal R9_EXCHANGE_RATE;

    // ===================== R10 =====================
    private String R10_RECORD_NUMBER;
    private String R10_TITLE;
    private String R10_FIRST_NAME;
    private String R10_MIDDLE_NAME;
    private String R10_SURNAME;
    private String R10_PREVIOUS_NAME;
    private String R10_GENDER;
    private String R10_IDENTIFICATION_TYPE;
    private String R10_PASSPORT_NUMBER;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R10_DATE_OF_BIRTH;
    private String R10_HOME_ADDRESS;
    private String R10_POSTAL_ADDRESS;
    private String R10_RESIDENCE;
    private String R10_EMAIL;
    private String R10_LANDLINE;
    private String R10_MOBILE_PHONE_NUMBER;
    private String R10_MOBILE_MONEY_NUMBER;
    private String R10_PRODUCT_TYPE;
    private String R10_ACCOUNT_BY_OWNERSHIP;
    private String R10_ACCOUNT_NUMBER;
    private BigDecimal R10_ACCOUNT_HOLDER_INDICATOR;
    private String R10_STATUS_OF_ACCOUNT;
    private String R10_NOT_FIT_FOR_STP;
    private String R10_BRANCH_CODE_AND_NAME;
    private BigDecimal R10_ACCOUNT_BALANCE_IN_PULA;
    private String R10_CURRENCY_OF_ACCOUNT;
    private BigDecimal R10_EXCHANGE_RATE;

    // ===================== R11 =====================
    private String R11_RECORD_NUMBER;
    private String R11_TITLE;
    private String R11_FIRST_NAME;
    private String R11_MIDDLE_NAME;
    private String R11_SURNAME;
    private String R11_PREVIOUS_NAME;
    private String R11_GENDER;
    private String R11_IDENTIFICATION_TYPE;
    private String R11_PASSPORT_NUMBER;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date R11_DATE_OF_BIRTH;
    private String R11_HOME_ADDRESS;
    private String R11_POSTAL_ADDRESS;
    private String R11_RESIDENCE;
    private String R11_EMAIL;
    private String R11_LANDLINE;
    private String R11_MOBILE_PHONE_NUMBER;
    private String R11_MOBILE_MONEY_NUMBER;
    private String R11_PRODUCT_TYPE;
    private String R11_ACCOUNT_BY_OWNERSHIP;
    private String R11_ACCOUNT_NUMBER;
    private BigDecimal R11_ACCOUNT_HOLDER_INDICATOR;
    private String R11_STATUS_OF_ACCOUNT;
    private String R11_NOT_FIT_FOR_STP;
    private String R11_BRANCH_CODE_AND_NAME;
    private BigDecimal R11_ACCOUNT_BALANCE_IN_PULA;
    private String R11_CURRENCY_OF_ACCOUNT;
    private BigDecimal R11_EXCHANGE_RATE;
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public BigDecimal getReportVersion() {
		return reportVersion;
	}
	public void setReportVersion(BigDecimal reportVersion) {
		this.reportVersion = reportVersion;
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
	public String getR5_RECORD_NUMBER() {
		return R5_RECORD_NUMBER;
	}
	public void setR5_RECORD_NUMBER(String r5_RECORD_NUMBER) {
		R5_RECORD_NUMBER = r5_RECORD_NUMBER;
	}
	public String getR5_TITLE() {
		return R5_TITLE;
	}
	public void setR5_TITLE(String r5_TITLE) {
		R5_TITLE = r5_TITLE;
	}
	public String getR5_FIRST_NAME() {
		return R5_FIRST_NAME;
	}
	public void setR5_FIRST_NAME(String r5_FIRST_NAME) {
		R5_FIRST_NAME = r5_FIRST_NAME;
	}
	public String getR5_MIDDLE_NAME() {
		return R5_MIDDLE_NAME;
	}
	public void setR5_MIDDLE_NAME(String r5_MIDDLE_NAME) {
		R5_MIDDLE_NAME = r5_MIDDLE_NAME;
	}
	public String getR5_SURNAME() {
		return R5_SURNAME;
	}
	public void setR5_SURNAME(String r5_SURNAME) {
		R5_SURNAME = r5_SURNAME;
	}
	public String getR5_PREVIOUS_NAME() {
		return R5_PREVIOUS_NAME;
	}
	public void setR5_PREVIOUS_NAME(String r5_PREVIOUS_NAME) {
		R5_PREVIOUS_NAME = r5_PREVIOUS_NAME;
	}
	public String getR5_GENDER() {
		return R5_GENDER;
	}
	public void setR5_GENDER(String r5_GENDER) {
		R5_GENDER = r5_GENDER;
	}
	public String getR5_IDENTIFICATION_TYPE() {
		return R5_IDENTIFICATION_TYPE;
	}
	public void setR5_IDENTIFICATION_TYPE(String r5_IDENTIFICATION_TYPE) {
		R5_IDENTIFICATION_TYPE = r5_IDENTIFICATION_TYPE;
	}
	public String getR5_PASSPORT_NUMBER() {
		return R5_PASSPORT_NUMBER;
	}
	public void setR5_PASSPORT_NUMBER(String r5_PASSPORT_NUMBER) {
		R5_PASSPORT_NUMBER = r5_PASSPORT_NUMBER;
	}
	public Date getR5_DATE_OF_BIRTH() {
		return R5_DATE_OF_BIRTH;
	}
	public void setR5_DATE_OF_BIRTH(Date r5_DATE_OF_BIRTH) {
		R5_DATE_OF_BIRTH = r5_DATE_OF_BIRTH;
	}
	public String getR5_HOME_ADDRESS() {
		return R5_HOME_ADDRESS;
	}
	public void setR5_HOME_ADDRESS(String r5_HOME_ADDRESS) {
		R5_HOME_ADDRESS = r5_HOME_ADDRESS;
	}
	public String getR5_POSTAL_ADDRESS() {
		return R5_POSTAL_ADDRESS;
	}
	public void setR5_POSTAL_ADDRESS(String r5_POSTAL_ADDRESS) {
		R5_POSTAL_ADDRESS = r5_POSTAL_ADDRESS;
	}
	public String getR5_RESIDENCE() {
		return R5_RESIDENCE;
	}
	public void setR5_RESIDENCE(String r5_RESIDENCE) {
		R5_RESIDENCE = r5_RESIDENCE;
	}
	public String getR5_EMAIL() {
		return R5_EMAIL;
	}
	public void setR5_EMAIL(String r5_EMAIL) {
		R5_EMAIL = r5_EMAIL;
	}
	public String getR5_LANDLINE() {
		return R5_LANDLINE;
	}
	public void setR5_LANDLINE(String r5_LANDLINE) {
		R5_LANDLINE = r5_LANDLINE;
	}
	public String getR5_MOBILE_PHONE_NUMBER() {
		return R5_MOBILE_PHONE_NUMBER;
	}
	public void setR5_MOBILE_PHONE_NUMBER(String r5_MOBILE_PHONE_NUMBER) {
		R5_MOBILE_PHONE_NUMBER = r5_MOBILE_PHONE_NUMBER;
	}
	public String getR5_MOBILE_MONEY_NUMBER() {
		return R5_MOBILE_MONEY_NUMBER;
	}
	public void setR5_MOBILE_MONEY_NUMBER(String r5_MOBILE_MONEY_NUMBER) {
		R5_MOBILE_MONEY_NUMBER = r5_MOBILE_MONEY_NUMBER;
	}
	public String getR5_PRODUCT_TYPE() {
		return R5_PRODUCT_TYPE;
	}
	public void setR5_PRODUCT_TYPE(String r5_PRODUCT_TYPE) {
		R5_PRODUCT_TYPE = r5_PRODUCT_TYPE;
	}
	public String getR5_ACCOUNT_BY_OWNERSHIP() {
		return R5_ACCOUNT_BY_OWNERSHIP;
	}
	public void setR5_ACCOUNT_BY_OWNERSHIP(String r5_ACCOUNT_BY_OWNERSHIP) {
		R5_ACCOUNT_BY_OWNERSHIP = r5_ACCOUNT_BY_OWNERSHIP;
	}
	public String getR5_ACCOUNT_NUMBER() {
		return R5_ACCOUNT_NUMBER;
	}
	public void setR5_ACCOUNT_NUMBER(String r5_ACCOUNT_NUMBER) {
		R5_ACCOUNT_NUMBER = r5_ACCOUNT_NUMBER;
	}
	public BigDecimal getR5_ACCOUNT_HOLDER_INDICATOR() {
		return R5_ACCOUNT_HOLDER_INDICATOR;
	}
	public void setR5_ACCOUNT_HOLDER_INDICATOR(BigDecimal r5_ACCOUNT_HOLDER_INDICATOR) {
		R5_ACCOUNT_HOLDER_INDICATOR = r5_ACCOUNT_HOLDER_INDICATOR;
	}
	public String getR5_STATUS_OF_ACCOUNT() {
		return R5_STATUS_OF_ACCOUNT;
	}
	public void setR5_STATUS_OF_ACCOUNT(String r5_STATUS_OF_ACCOUNT) {
		R5_STATUS_OF_ACCOUNT = r5_STATUS_OF_ACCOUNT;
	}
	public String getR5_NOT_FIT_FOR_STP() {
		return R5_NOT_FIT_FOR_STP;
	}
	public void setR5_NOT_FIT_FOR_STP(String r5_NOT_FIT_FOR_STP) {
		R5_NOT_FIT_FOR_STP = r5_NOT_FIT_FOR_STP;
	}
	public String getR5_BRANCH_CODE_AND_NAME() {
		return R5_BRANCH_CODE_AND_NAME;
	}
	public void setR5_BRANCH_CODE_AND_NAME(String r5_BRANCH_CODE_AND_NAME) {
		R5_BRANCH_CODE_AND_NAME = r5_BRANCH_CODE_AND_NAME;
	}
	public BigDecimal getR5_ACCOUNT_BALANCE_IN_PULA() {
		return R5_ACCOUNT_BALANCE_IN_PULA;
	}
	public void setR5_ACCOUNT_BALANCE_IN_PULA(BigDecimal r5_ACCOUNT_BALANCE_IN_PULA) {
		R5_ACCOUNT_BALANCE_IN_PULA = r5_ACCOUNT_BALANCE_IN_PULA;
	}
	public String getR5_CURRENCY_OF_ACCOUNT() {
		return R5_CURRENCY_OF_ACCOUNT;
	}
	public void setR5_CURRENCY_OF_ACCOUNT(String r5_CURRENCY_OF_ACCOUNT) {
		R5_CURRENCY_OF_ACCOUNT = r5_CURRENCY_OF_ACCOUNT;
	}
	public BigDecimal getR5_EXCHANGE_RATE() {
		return R5_EXCHANGE_RATE;
	}
	public void setR5_EXCHANGE_RATE(BigDecimal r5_EXCHANGE_RATE) {
		R5_EXCHANGE_RATE = r5_EXCHANGE_RATE;
	}
	public String getR6_RECORD_NUMBER() {
		return R6_RECORD_NUMBER;
	}
	public void setR6_RECORD_NUMBER(String r6_RECORD_NUMBER) {
		R6_RECORD_NUMBER = r6_RECORD_NUMBER;
	}
	public String getR6_TITLE() {
		return R6_TITLE;
	}
	public void setR6_TITLE(String r6_TITLE) {
		R6_TITLE = r6_TITLE;
	}
	public String getR6_FIRST_NAME() {
		return R6_FIRST_NAME;
	}
	public void setR6_FIRST_NAME(String r6_FIRST_NAME) {
		R6_FIRST_NAME = r6_FIRST_NAME;
	}
	public String getR6_MIDDLE_NAME() {
		return R6_MIDDLE_NAME;
	}
	public void setR6_MIDDLE_NAME(String r6_MIDDLE_NAME) {
		R6_MIDDLE_NAME = r6_MIDDLE_NAME;
	}
	public String getR6_SURNAME() {
		return R6_SURNAME;
	}
	public void setR6_SURNAME(String r6_SURNAME) {
		R6_SURNAME = r6_SURNAME;
	}
	public String getR6_PREVIOUS_NAME() {
		return R6_PREVIOUS_NAME;
	}
	public void setR6_PREVIOUS_NAME(String r6_PREVIOUS_NAME) {
		R6_PREVIOUS_NAME = r6_PREVIOUS_NAME;
	}
	public String getR6_GENDER() {
		return R6_GENDER;
	}
	public void setR6_GENDER(String r6_GENDER) {
		R6_GENDER = r6_GENDER;
	}
	public String getR6_IDENTIFICATION_TYPE() {
		return R6_IDENTIFICATION_TYPE;
	}
	public void setR6_IDENTIFICATION_TYPE(String r6_IDENTIFICATION_TYPE) {
		R6_IDENTIFICATION_TYPE = r6_IDENTIFICATION_TYPE;
	}
	public String getR6_PASSPORT_NUMBER() {
		return R6_PASSPORT_NUMBER;
	}
	public void setR6_PASSPORT_NUMBER(String r6_PASSPORT_NUMBER) {
		R6_PASSPORT_NUMBER = r6_PASSPORT_NUMBER;
	}
	public Date getR6_DATE_OF_BIRTH() {
		return R6_DATE_OF_BIRTH;
	}
	public void setR6_DATE_OF_BIRTH(Date r6_DATE_OF_BIRTH) {
		R6_DATE_OF_BIRTH = r6_DATE_OF_BIRTH;
	}
	public String getR6_HOME_ADDRESS() {
		return R6_HOME_ADDRESS;
	}
	public void setR6_HOME_ADDRESS(String r6_HOME_ADDRESS) {
		R6_HOME_ADDRESS = r6_HOME_ADDRESS;
	}
	public String getR6_POSTAL_ADDRESS() {
		return R6_POSTAL_ADDRESS;
	}
	public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
		R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
	}
	public String getR6_RESIDENCE() {
		return R6_RESIDENCE;
	}
	public void setR6_RESIDENCE(String r6_RESIDENCE) {
		R6_RESIDENCE = r6_RESIDENCE;
	}
	public String getR6_EMAIL() {
		return R6_EMAIL;
	}
	public void setR6_EMAIL(String r6_EMAIL) {
		R6_EMAIL = r6_EMAIL;
	}
	public String getR6_LANDLINE() {
		return R6_LANDLINE;
	}
	public void setR6_LANDLINE(String r6_LANDLINE) {
		R6_LANDLINE = r6_LANDLINE;
	}
	public String getR6_MOBILE_PHONE_NUMBER() {
		return R6_MOBILE_PHONE_NUMBER;
	}
	public void setR6_MOBILE_PHONE_NUMBER(String r6_MOBILE_PHONE_NUMBER) {
		R6_MOBILE_PHONE_NUMBER = r6_MOBILE_PHONE_NUMBER;
	}
	public String getR6_MOBILE_MONEY_NUMBER() {
		return R6_MOBILE_MONEY_NUMBER;
	}
	public void setR6_MOBILE_MONEY_NUMBER(String r6_MOBILE_MONEY_NUMBER) {
		R6_MOBILE_MONEY_NUMBER = r6_MOBILE_MONEY_NUMBER;
	}
	public String getR6_PRODUCT_TYPE() {
		return R6_PRODUCT_TYPE;
	}
	public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
		R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
	}
	public String getR6_ACCOUNT_BY_OWNERSHIP() {
		return R6_ACCOUNT_BY_OWNERSHIP;
	}
	public void setR6_ACCOUNT_BY_OWNERSHIP(String r6_ACCOUNT_BY_OWNERSHIP) {
		R6_ACCOUNT_BY_OWNERSHIP = r6_ACCOUNT_BY_OWNERSHIP;
	}
	public String getR6_ACCOUNT_NUMBER() {
		return R6_ACCOUNT_NUMBER;
	}
	public void setR6_ACCOUNT_NUMBER(String r6_ACCOUNT_NUMBER) {
		R6_ACCOUNT_NUMBER = r6_ACCOUNT_NUMBER;
	}
	public BigDecimal getR6_ACCOUNT_HOLDER_INDICATOR() {
		return R6_ACCOUNT_HOLDER_INDICATOR;
	}
	public void setR6_ACCOUNT_HOLDER_INDICATOR(BigDecimal r6_ACCOUNT_HOLDER_INDICATOR) {
		R6_ACCOUNT_HOLDER_INDICATOR = r6_ACCOUNT_HOLDER_INDICATOR;
	}
	public String getR6_STATUS_OF_ACCOUNT() {
		return R6_STATUS_OF_ACCOUNT;
	}
	public void setR6_STATUS_OF_ACCOUNT(String r6_STATUS_OF_ACCOUNT) {
		R6_STATUS_OF_ACCOUNT = r6_STATUS_OF_ACCOUNT;
	}
	public String getR6_NOT_FIT_FOR_STP() {
		return R6_NOT_FIT_FOR_STP;
	}
	public void setR6_NOT_FIT_FOR_STP(String r6_NOT_FIT_FOR_STP) {
		R6_NOT_FIT_FOR_STP = r6_NOT_FIT_FOR_STP;
	}
	public String getR6_BRANCH_CODE_AND_NAME() {
		return R6_BRANCH_CODE_AND_NAME;
	}
	public void setR6_BRANCH_CODE_AND_NAME(String r6_BRANCH_CODE_AND_NAME) {
		R6_BRANCH_CODE_AND_NAME = r6_BRANCH_CODE_AND_NAME;
	}
	public BigDecimal getR6_ACCOUNT_BALANCE_IN_PULA() {
		return R6_ACCOUNT_BALANCE_IN_PULA;
	}
	public void setR6_ACCOUNT_BALANCE_IN_PULA(BigDecimal r6_ACCOUNT_BALANCE_IN_PULA) {
		R6_ACCOUNT_BALANCE_IN_PULA = r6_ACCOUNT_BALANCE_IN_PULA;
	}
	public String getR6_CURRENCY_OF_ACCOUNT() {
		return R6_CURRENCY_OF_ACCOUNT;
	}
	public void setR6_CURRENCY_OF_ACCOUNT(String r6_CURRENCY_OF_ACCOUNT) {
		R6_CURRENCY_OF_ACCOUNT = r6_CURRENCY_OF_ACCOUNT;
	}
	public BigDecimal getR6_EXCHANGE_RATE() {
		return R6_EXCHANGE_RATE;
	}
	public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
		R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
	}
	public String getR7_RECORD_NUMBER() {
		return R7_RECORD_NUMBER;
	}
	public void setR7_RECORD_NUMBER(String r7_RECORD_NUMBER) {
		R7_RECORD_NUMBER = r7_RECORD_NUMBER;
	}
	public String getR7_TITLE() {
		return R7_TITLE;
	}
	public void setR7_TITLE(String r7_TITLE) {
		R7_TITLE = r7_TITLE;
	}
	public String getR7_FIRST_NAME() {
		return R7_FIRST_NAME;
	}
	public void setR7_FIRST_NAME(String r7_FIRST_NAME) {
		R7_FIRST_NAME = r7_FIRST_NAME;
	}
	public String getR7_MIDDLE_NAME() {
		return R7_MIDDLE_NAME;
	}
	public void setR7_MIDDLE_NAME(String r7_MIDDLE_NAME) {
		R7_MIDDLE_NAME = r7_MIDDLE_NAME;
	}
	public String getR7_SURNAME() {
		return R7_SURNAME;
	}
	public void setR7_SURNAME(String r7_SURNAME) {
		R7_SURNAME = r7_SURNAME;
	}
	public String getR7_PREVIOUS_NAME() {
		return R7_PREVIOUS_NAME;
	}
	public void setR7_PREVIOUS_NAME(String r7_PREVIOUS_NAME) {
		R7_PREVIOUS_NAME = r7_PREVIOUS_NAME;
	}
	public String getR7_GENDER() {
		return R7_GENDER;
	}
	public void setR7_GENDER(String r7_GENDER) {
		R7_GENDER = r7_GENDER;
	}
	public String getR7_IDENTIFICATION_TYPE() {
		return R7_IDENTIFICATION_TYPE;
	}
	public void setR7_IDENTIFICATION_TYPE(String r7_IDENTIFICATION_TYPE) {
		R7_IDENTIFICATION_TYPE = r7_IDENTIFICATION_TYPE;
	}
	public String getR7_PASSPORT_NUMBER() {
		return R7_PASSPORT_NUMBER;
	}
	public void setR7_PASSPORT_NUMBER(String r7_PASSPORT_NUMBER) {
		R7_PASSPORT_NUMBER = r7_PASSPORT_NUMBER;
	}
	public Date getR7_DATE_OF_BIRTH() {
		return R7_DATE_OF_BIRTH;
	}
	public void setR7_DATE_OF_BIRTH(Date r7_DATE_OF_BIRTH) {
		R7_DATE_OF_BIRTH = r7_DATE_OF_BIRTH;
	}
	public String getR7_HOME_ADDRESS() {
		return R7_HOME_ADDRESS;
	}
	public void setR7_HOME_ADDRESS(String r7_HOME_ADDRESS) {
		R7_HOME_ADDRESS = r7_HOME_ADDRESS;
	}
	public String getR7_POSTAL_ADDRESS() {
		return R7_POSTAL_ADDRESS;
	}
	public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
		R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
	}
	public String getR7_RESIDENCE() {
		return R7_RESIDENCE;
	}
	public void setR7_RESIDENCE(String r7_RESIDENCE) {
		R7_RESIDENCE = r7_RESIDENCE;
	}
	public String getR7_EMAIL() {
		return R7_EMAIL;
	}
	public void setR7_EMAIL(String r7_EMAIL) {
		R7_EMAIL = r7_EMAIL;
	}
	public String getR7_LANDLINE() {
		return R7_LANDLINE;
	}
	public void setR7_LANDLINE(String r7_LANDLINE) {
		R7_LANDLINE = r7_LANDLINE;
	}
	public String getR7_MOBILE_PHONE_NUMBER() {
		return R7_MOBILE_PHONE_NUMBER;
	}
	public void setR7_MOBILE_PHONE_NUMBER(String r7_MOBILE_PHONE_NUMBER) {
		R7_MOBILE_PHONE_NUMBER = r7_MOBILE_PHONE_NUMBER;
	}
	public String getR7_MOBILE_MONEY_NUMBER() {
		return R7_MOBILE_MONEY_NUMBER;
	}
	public void setR7_MOBILE_MONEY_NUMBER(String r7_MOBILE_MONEY_NUMBER) {
		R7_MOBILE_MONEY_NUMBER = r7_MOBILE_MONEY_NUMBER;
	}
	public String getR7_PRODUCT_TYPE() {
		return R7_PRODUCT_TYPE;
	}
	public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
		R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
	}
	public String getR7_ACCOUNT_BY_OWNERSHIP() {
		return R7_ACCOUNT_BY_OWNERSHIP;
	}
	public void setR7_ACCOUNT_BY_OWNERSHIP(String r7_ACCOUNT_BY_OWNERSHIP) {
		R7_ACCOUNT_BY_OWNERSHIP = r7_ACCOUNT_BY_OWNERSHIP;
	}
	public String getR7_ACCOUNT_NUMBER() {
		return R7_ACCOUNT_NUMBER;
	}
	public void setR7_ACCOUNT_NUMBER(String r7_ACCOUNT_NUMBER) {
		R7_ACCOUNT_NUMBER = r7_ACCOUNT_NUMBER;
	}
	public BigDecimal getR7_ACCOUNT_HOLDER_INDICATOR() {
		return R7_ACCOUNT_HOLDER_INDICATOR;
	}
	public void setR7_ACCOUNT_HOLDER_INDICATOR(BigDecimal r7_ACCOUNT_HOLDER_INDICATOR) {
		R7_ACCOUNT_HOLDER_INDICATOR = r7_ACCOUNT_HOLDER_INDICATOR;
	}
	public String getR7_STATUS_OF_ACCOUNT() {
		return R7_STATUS_OF_ACCOUNT;
	}
	public void setR7_STATUS_OF_ACCOUNT(String r7_STATUS_OF_ACCOUNT) {
		R7_STATUS_OF_ACCOUNT = r7_STATUS_OF_ACCOUNT;
	}
	public String getR7_NOT_FIT_FOR_STP() {
		return R7_NOT_FIT_FOR_STP;
	}
	public void setR7_NOT_FIT_FOR_STP(String r7_NOT_FIT_FOR_STP) {
		R7_NOT_FIT_FOR_STP = r7_NOT_FIT_FOR_STP;
	}
	public String getR7_BRANCH_CODE_AND_NAME() {
		return R7_BRANCH_CODE_AND_NAME;
	}
	public void setR7_BRANCH_CODE_AND_NAME(String r7_BRANCH_CODE_AND_NAME) {
		R7_BRANCH_CODE_AND_NAME = r7_BRANCH_CODE_AND_NAME;
	}
	public BigDecimal getR7_ACCOUNT_BALANCE_IN_PULA() {
		return R7_ACCOUNT_BALANCE_IN_PULA;
	}
	public void setR7_ACCOUNT_BALANCE_IN_PULA(BigDecimal r7_ACCOUNT_BALANCE_IN_PULA) {
		R7_ACCOUNT_BALANCE_IN_PULA = r7_ACCOUNT_BALANCE_IN_PULA;
	}
	public String getR7_CURRENCY_OF_ACCOUNT() {
		return R7_CURRENCY_OF_ACCOUNT;
	}
	public void setR7_CURRENCY_OF_ACCOUNT(String r7_CURRENCY_OF_ACCOUNT) {
		R7_CURRENCY_OF_ACCOUNT = r7_CURRENCY_OF_ACCOUNT;
	}
	public BigDecimal getR7_EXCHANGE_RATE() {
		return R7_EXCHANGE_RATE;
	}
	public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
		R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
	}
	public String getR8_RECORD_NUMBER() {
		return R8_RECORD_NUMBER;
	}
	public void setR8_RECORD_NUMBER(String r8_RECORD_NUMBER) {
		R8_RECORD_NUMBER = r8_RECORD_NUMBER;
	}
	public String getR8_TITLE() {
		return R8_TITLE;
	}
	public void setR8_TITLE(String r8_TITLE) {
		R8_TITLE = r8_TITLE;
	}
	public String getR8_FIRST_NAME() {
		return R8_FIRST_NAME;
	}
	public void setR8_FIRST_NAME(String r8_FIRST_NAME) {
		R8_FIRST_NAME = r8_FIRST_NAME;
	}
	public String getR8_MIDDLE_NAME() {
		return R8_MIDDLE_NAME;
	}
	public void setR8_MIDDLE_NAME(String r8_MIDDLE_NAME) {
		R8_MIDDLE_NAME = r8_MIDDLE_NAME;
	}
	public String getR8_SURNAME() {
		return R8_SURNAME;
	}
	public void setR8_SURNAME(String r8_SURNAME) {
		R8_SURNAME = r8_SURNAME;
	}
	public String getR8_PREVIOUS_NAME() {
		return R8_PREVIOUS_NAME;
	}
	public void setR8_PREVIOUS_NAME(String r8_PREVIOUS_NAME) {
		R8_PREVIOUS_NAME = r8_PREVIOUS_NAME;
	}
	public String getR8_GENDER() {
		return R8_GENDER;
	}
	public void setR8_GENDER(String r8_GENDER) {
		R8_GENDER = r8_GENDER;
	}
	public String getR8_IDENTIFICATION_TYPE() {
		return R8_IDENTIFICATION_TYPE;
	}
	public void setR8_IDENTIFICATION_TYPE(String r8_IDENTIFICATION_TYPE) {
		R8_IDENTIFICATION_TYPE = r8_IDENTIFICATION_TYPE;
	}
	public String getR8_PASSPORT_NUMBER() {
		return R8_PASSPORT_NUMBER;
	}
	public void setR8_PASSPORT_NUMBER(String r8_PASSPORT_NUMBER) {
		R8_PASSPORT_NUMBER = r8_PASSPORT_NUMBER;
	}
	public Date getR8_DATE_OF_BIRTH() {
		return R8_DATE_OF_BIRTH;
	}
	public void setR8_DATE_OF_BIRTH(Date r8_DATE_OF_BIRTH) {
		R8_DATE_OF_BIRTH = r8_DATE_OF_BIRTH;
	}
	public String getR8_HOME_ADDRESS() {
		return R8_HOME_ADDRESS;
	}
	public void setR8_HOME_ADDRESS(String r8_HOME_ADDRESS) {
		R8_HOME_ADDRESS = r8_HOME_ADDRESS;
	}
	public String getR8_POSTAL_ADDRESS() {
		return R8_POSTAL_ADDRESS;
	}
	public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
		R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
	}
	public String getR8_RESIDENCE() {
		return R8_RESIDENCE;
	}
	public void setR8_RESIDENCE(String r8_RESIDENCE) {
		R8_RESIDENCE = r8_RESIDENCE;
	}
	public String getR8_EMAIL() {
		return R8_EMAIL;
	}
	public void setR8_EMAIL(String r8_EMAIL) {
		R8_EMAIL = r8_EMAIL;
	}
	public String getR8_LANDLINE() {
		return R8_LANDLINE;
	}
	public void setR8_LANDLINE(String r8_LANDLINE) {
		R8_LANDLINE = r8_LANDLINE;
	}
	public String getR8_MOBILE_PHONE_NUMBER() {
		return R8_MOBILE_PHONE_NUMBER;
	}
	public void setR8_MOBILE_PHONE_NUMBER(String r8_MOBILE_PHONE_NUMBER) {
		R8_MOBILE_PHONE_NUMBER = r8_MOBILE_PHONE_NUMBER;
	}
	public String getR8_MOBILE_MONEY_NUMBER() {
		return R8_MOBILE_MONEY_NUMBER;
	}
	public void setR8_MOBILE_MONEY_NUMBER(String r8_MOBILE_MONEY_NUMBER) {
		R8_MOBILE_MONEY_NUMBER = r8_MOBILE_MONEY_NUMBER;
	}
	public String getR8_PRODUCT_TYPE() {
		return R8_PRODUCT_TYPE;
	}
	public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
		R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
	}
	public String getR8_ACCOUNT_BY_OWNERSHIP() {
		return R8_ACCOUNT_BY_OWNERSHIP;
	}
	public void setR8_ACCOUNT_BY_OWNERSHIP(String r8_ACCOUNT_BY_OWNERSHIP) {
		R8_ACCOUNT_BY_OWNERSHIP = r8_ACCOUNT_BY_OWNERSHIP;
	}
	public String getR8_ACCOUNT_NUMBER() {
		return R8_ACCOUNT_NUMBER;
	}
	public void setR8_ACCOUNT_NUMBER(String r8_ACCOUNT_NUMBER) {
		R8_ACCOUNT_NUMBER = r8_ACCOUNT_NUMBER;
	}
	public BigDecimal getR8_ACCOUNT_HOLDER_INDICATOR() {
		return R8_ACCOUNT_HOLDER_INDICATOR;
	}
	public void setR8_ACCOUNT_HOLDER_INDICATOR(BigDecimal r8_ACCOUNT_HOLDER_INDICATOR) {
		R8_ACCOUNT_HOLDER_INDICATOR = r8_ACCOUNT_HOLDER_INDICATOR;
	}
	public String getR8_STATUS_OF_ACCOUNT() {
		return R8_STATUS_OF_ACCOUNT;
	}
	public void setR8_STATUS_OF_ACCOUNT(String r8_STATUS_OF_ACCOUNT) {
		R8_STATUS_OF_ACCOUNT = r8_STATUS_OF_ACCOUNT;
	}
	public String getR8_NOT_FIT_FOR_STP() {
		return R8_NOT_FIT_FOR_STP;
	}
	public void setR8_NOT_FIT_FOR_STP(String r8_NOT_FIT_FOR_STP) {
		R8_NOT_FIT_FOR_STP = r8_NOT_FIT_FOR_STP;
	}
	public String getR8_BRANCH_CODE_AND_NAME() {
		return R8_BRANCH_CODE_AND_NAME;
	}
	public void setR8_BRANCH_CODE_AND_NAME(String r8_BRANCH_CODE_AND_NAME) {
		R8_BRANCH_CODE_AND_NAME = r8_BRANCH_CODE_AND_NAME;
	}
	public BigDecimal getR8_ACCOUNT_BALANCE_IN_PULA() {
		return R8_ACCOUNT_BALANCE_IN_PULA;
	}
	public void setR8_ACCOUNT_BALANCE_IN_PULA(BigDecimal r8_ACCOUNT_BALANCE_IN_PULA) {
		R8_ACCOUNT_BALANCE_IN_PULA = r8_ACCOUNT_BALANCE_IN_PULA;
	}
	public String getR8_CURRENCY_OF_ACCOUNT() {
		return R8_CURRENCY_OF_ACCOUNT;
	}
	public void setR8_CURRENCY_OF_ACCOUNT(String r8_CURRENCY_OF_ACCOUNT) {
		R8_CURRENCY_OF_ACCOUNT = r8_CURRENCY_OF_ACCOUNT;
	}
	public BigDecimal getR8_EXCHANGE_RATE() {
		return R8_EXCHANGE_RATE;
	}
	public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
		R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
	}
	public String getR9_RECORD_NUMBER() {
		return R9_RECORD_NUMBER;
	}
	public void setR9_RECORD_NUMBER(String r9_RECORD_NUMBER) {
		R9_RECORD_NUMBER = r9_RECORD_NUMBER;
	}
	public String getR9_TITLE() {
		return R9_TITLE;
	}
	public void setR9_TITLE(String r9_TITLE) {
		R9_TITLE = r9_TITLE;
	}
	public String getR9_FIRST_NAME() {
		return R9_FIRST_NAME;
	}
	public void setR9_FIRST_NAME(String r9_FIRST_NAME) {
		R9_FIRST_NAME = r9_FIRST_NAME;
	}
	public String getR9_MIDDLE_NAME() {
		return R9_MIDDLE_NAME;
	}
	public void setR9_MIDDLE_NAME(String r9_MIDDLE_NAME) {
		R9_MIDDLE_NAME = r9_MIDDLE_NAME;
	}
	public String getR9_SURNAME() {
		return R9_SURNAME;
	}
	public void setR9_SURNAME(String r9_SURNAME) {
		R9_SURNAME = r9_SURNAME;
	}
	public String getR9_PREVIOUS_NAME() {
		return R9_PREVIOUS_NAME;
	}
	public void setR9_PREVIOUS_NAME(String r9_PREVIOUS_NAME) {
		R9_PREVIOUS_NAME = r9_PREVIOUS_NAME;
	}
	public String getR9_GENDER() {
		return R9_GENDER;
	}
	public void setR9_GENDER(String r9_GENDER) {
		R9_GENDER = r9_GENDER;
	}
	public String getR9_IDENTIFICATION_TYPE() {
		return R9_IDENTIFICATION_TYPE;
	}
	public void setR9_IDENTIFICATION_TYPE(String r9_IDENTIFICATION_TYPE) {
		R9_IDENTIFICATION_TYPE = r9_IDENTIFICATION_TYPE;
	}
	public String getR9_PASSPORT_NUMBER() {
		return R9_PASSPORT_NUMBER;
	}
	public void setR9_PASSPORT_NUMBER(String r9_PASSPORT_NUMBER) {
		R9_PASSPORT_NUMBER = r9_PASSPORT_NUMBER;
	}
	public Date getR9_DATE_OF_BIRTH() {
		return R9_DATE_OF_BIRTH;
	}
	public void setR9_DATE_OF_BIRTH(Date r9_DATE_OF_BIRTH) {
		R9_DATE_OF_BIRTH = r9_DATE_OF_BIRTH;
	}
	public String getR9_HOME_ADDRESS() {
		return R9_HOME_ADDRESS;
	}
	public void setR9_HOME_ADDRESS(String r9_HOME_ADDRESS) {
		R9_HOME_ADDRESS = r9_HOME_ADDRESS;
	}
	public String getR9_POSTAL_ADDRESS() {
		return R9_POSTAL_ADDRESS;
	}
	public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
		R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
	}
	public String getR9_RESIDENCE() {
		return R9_RESIDENCE;
	}
	public void setR9_RESIDENCE(String r9_RESIDENCE) {
		R9_RESIDENCE = r9_RESIDENCE;
	}
	public String getR9_EMAIL() {
		return R9_EMAIL;
	}
	public void setR9_EMAIL(String r9_EMAIL) {
		R9_EMAIL = r9_EMAIL;
	}
	public String getR9_LANDLINE() {
		return R9_LANDLINE;
	}
	public void setR9_LANDLINE(String r9_LANDLINE) {
		R9_LANDLINE = r9_LANDLINE;
	}
	public String getR9_MOBILE_PHONE_NUMBER() {
		return R9_MOBILE_PHONE_NUMBER;
	}
	public void setR9_MOBILE_PHONE_NUMBER(String r9_MOBILE_PHONE_NUMBER) {
		R9_MOBILE_PHONE_NUMBER = r9_MOBILE_PHONE_NUMBER;
	}
	public String getR9_MOBILE_MONEY_NUMBER() {
		return R9_MOBILE_MONEY_NUMBER;
	}
	public void setR9_MOBILE_MONEY_NUMBER(String r9_MOBILE_MONEY_NUMBER) {
		R9_MOBILE_MONEY_NUMBER = r9_MOBILE_MONEY_NUMBER;
	}
	public String getR9_PRODUCT_TYPE() {
		return R9_PRODUCT_TYPE;
	}
	public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
		R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
	}
	public String getR9_ACCOUNT_BY_OWNERSHIP() {
		return R9_ACCOUNT_BY_OWNERSHIP;
	}
	public void setR9_ACCOUNT_BY_OWNERSHIP(String r9_ACCOUNT_BY_OWNERSHIP) {
		R9_ACCOUNT_BY_OWNERSHIP = r9_ACCOUNT_BY_OWNERSHIP;
	}
	public String getR9_ACCOUNT_NUMBER() {
		return R9_ACCOUNT_NUMBER;
	}
	public void setR9_ACCOUNT_NUMBER(String r9_ACCOUNT_NUMBER) {
		R9_ACCOUNT_NUMBER = r9_ACCOUNT_NUMBER;
	}
	public BigDecimal getR9_ACCOUNT_HOLDER_INDICATOR() {
		return R9_ACCOUNT_HOLDER_INDICATOR;
	}
	public void setR9_ACCOUNT_HOLDER_INDICATOR(BigDecimal r9_ACCOUNT_HOLDER_INDICATOR) {
		R9_ACCOUNT_HOLDER_INDICATOR = r9_ACCOUNT_HOLDER_INDICATOR;
	}
	public String getR9_STATUS_OF_ACCOUNT() {
		return R9_STATUS_OF_ACCOUNT;
	}
	public void setR9_STATUS_OF_ACCOUNT(String r9_STATUS_OF_ACCOUNT) {
		R9_STATUS_OF_ACCOUNT = r9_STATUS_OF_ACCOUNT;
	}
	public String getR9_NOT_FIT_FOR_STP() {
		return R9_NOT_FIT_FOR_STP;
	}
	public void setR9_NOT_FIT_FOR_STP(String r9_NOT_FIT_FOR_STP) {
		R9_NOT_FIT_FOR_STP = r9_NOT_FIT_FOR_STP;
	}
	public String getR9_BRANCH_CODE_AND_NAME() {
		return R9_BRANCH_CODE_AND_NAME;
	}
	public void setR9_BRANCH_CODE_AND_NAME(String r9_BRANCH_CODE_AND_NAME) {
		R9_BRANCH_CODE_AND_NAME = r9_BRANCH_CODE_AND_NAME;
	}
	public BigDecimal getR9_ACCOUNT_BALANCE_IN_PULA() {
		return R9_ACCOUNT_BALANCE_IN_PULA;
	}
	public void setR9_ACCOUNT_BALANCE_IN_PULA(BigDecimal r9_ACCOUNT_BALANCE_IN_PULA) {
		R9_ACCOUNT_BALANCE_IN_PULA = r9_ACCOUNT_BALANCE_IN_PULA;
	}
	public String getR9_CURRENCY_OF_ACCOUNT() {
		return R9_CURRENCY_OF_ACCOUNT;
	}
	public void setR9_CURRENCY_OF_ACCOUNT(String r9_CURRENCY_OF_ACCOUNT) {
		R9_CURRENCY_OF_ACCOUNT = r9_CURRENCY_OF_ACCOUNT;
	}
	public BigDecimal getR9_EXCHANGE_RATE() {
		return R9_EXCHANGE_RATE;
	}
	public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
		R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
	}
	public String getR10_RECORD_NUMBER() {
		return R10_RECORD_NUMBER;
	}
	public void setR10_RECORD_NUMBER(String r10_RECORD_NUMBER) {
		R10_RECORD_NUMBER = r10_RECORD_NUMBER;
	}
	public String getR10_TITLE() {
		return R10_TITLE;
	}
	public void setR10_TITLE(String r10_TITLE) {
		R10_TITLE = r10_TITLE;
	}
	public String getR10_FIRST_NAME() {
		return R10_FIRST_NAME;
	}
	public void setR10_FIRST_NAME(String r10_FIRST_NAME) {
		R10_FIRST_NAME = r10_FIRST_NAME;
	}
	public String getR10_MIDDLE_NAME() {
		return R10_MIDDLE_NAME;
	}
	public void setR10_MIDDLE_NAME(String r10_MIDDLE_NAME) {
		R10_MIDDLE_NAME = r10_MIDDLE_NAME;
	}
	public String getR10_SURNAME() {
		return R10_SURNAME;
	}
	public void setR10_SURNAME(String r10_SURNAME) {
		R10_SURNAME = r10_SURNAME;
	}
	public String getR10_PREVIOUS_NAME() {
		return R10_PREVIOUS_NAME;
	}
	public void setR10_PREVIOUS_NAME(String r10_PREVIOUS_NAME) {
		R10_PREVIOUS_NAME = r10_PREVIOUS_NAME;
	}
	public String getR10_GENDER() {
		return R10_GENDER;
	}
	public void setR10_GENDER(String r10_GENDER) {
		R10_GENDER = r10_GENDER;
	}
	public String getR10_IDENTIFICATION_TYPE() {
		return R10_IDENTIFICATION_TYPE;
	}
	public void setR10_IDENTIFICATION_TYPE(String r10_IDENTIFICATION_TYPE) {
		R10_IDENTIFICATION_TYPE = r10_IDENTIFICATION_TYPE;
	}
	public String getR10_PASSPORT_NUMBER() {
		return R10_PASSPORT_NUMBER;
	}
	public void setR10_PASSPORT_NUMBER(String r10_PASSPORT_NUMBER) {
		R10_PASSPORT_NUMBER = r10_PASSPORT_NUMBER;
	}
	public Date getR10_DATE_OF_BIRTH() {
		return R10_DATE_OF_BIRTH;
	}
	public void setR10_DATE_OF_BIRTH(Date r10_DATE_OF_BIRTH) {
		R10_DATE_OF_BIRTH = r10_DATE_OF_BIRTH;
	}
	public String getR10_HOME_ADDRESS() {
		return R10_HOME_ADDRESS;
	}
	public void setR10_HOME_ADDRESS(String r10_HOME_ADDRESS) {
		R10_HOME_ADDRESS = r10_HOME_ADDRESS;
	}
	public String getR10_POSTAL_ADDRESS() {
		return R10_POSTAL_ADDRESS;
	}
	public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
		R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
	}
	public String getR10_RESIDENCE() {
		return R10_RESIDENCE;
	}
	public void setR10_RESIDENCE(String r10_RESIDENCE) {
		R10_RESIDENCE = r10_RESIDENCE;
	}
	public String getR10_EMAIL() {
		return R10_EMAIL;
	}
	public void setR10_EMAIL(String r10_EMAIL) {
		R10_EMAIL = r10_EMAIL;
	}
	public String getR10_LANDLINE() {
		return R10_LANDLINE;
	}
	public void setR10_LANDLINE(String r10_LANDLINE) {
		R10_LANDLINE = r10_LANDLINE;
	}
	public String getR10_MOBILE_PHONE_NUMBER() {
		return R10_MOBILE_PHONE_NUMBER;
	}
	public void setR10_MOBILE_PHONE_NUMBER(String r10_MOBILE_PHONE_NUMBER) {
		R10_MOBILE_PHONE_NUMBER = r10_MOBILE_PHONE_NUMBER;
	}
	public String getR10_MOBILE_MONEY_NUMBER() {
		return R10_MOBILE_MONEY_NUMBER;
	}
	public void setR10_MOBILE_MONEY_NUMBER(String r10_MOBILE_MONEY_NUMBER) {
		R10_MOBILE_MONEY_NUMBER = r10_MOBILE_MONEY_NUMBER;
	}
	public String getR10_PRODUCT_TYPE() {
		return R10_PRODUCT_TYPE;
	}
	public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
		R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
	}
	public String getR10_ACCOUNT_BY_OWNERSHIP() {
		return R10_ACCOUNT_BY_OWNERSHIP;
	}
	public void setR10_ACCOUNT_BY_OWNERSHIP(String r10_ACCOUNT_BY_OWNERSHIP) {
		R10_ACCOUNT_BY_OWNERSHIP = r10_ACCOUNT_BY_OWNERSHIP;
	}
	public String getR10_ACCOUNT_NUMBER() {
		return R10_ACCOUNT_NUMBER;
	}
	public void setR10_ACCOUNT_NUMBER(String r10_ACCOUNT_NUMBER) {
		R10_ACCOUNT_NUMBER = r10_ACCOUNT_NUMBER;
	}
	public BigDecimal getR10_ACCOUNT_HOLDER_INDICATOR() {
		return R10_ACCOUNT_HOLDER_INDICATOR;
	}
	public void setR10_ACCOUNT_HOLDER_INDICATOR(BigDecimal r10_ACCOUNT_HOLDER_INDICATOR) {
		R10_ACCOUNT_HOLDER_INDICATOR = r10_ACCOUNT_HOLDER_INDICATOR;
	}
	public String getR10_STATUS_OF_ACCOUNT() {
		return R10_STATUS_OF_ACCOUNT;
	}
	public void setR10_STATUS_OF_ACCOUNT(String r10_STATUS_OF_ACCOUNT) {
		R10_STATUS_OF_ACCOUNT = r10_STATUS_OF_ACCOUNT;
	}
	public String getR10_NOT_FIT_FOR_STP() {
		return R10_NOT_FIT_FOR_STP;
	}
	public void setR10_NOT_FIT_FOR_STP(String r10_NOT_FIT_FOR_STP) {
		R10_NOT_FIT_FOR_STP = r10_NOT_FIT_FOR_STP;
	}
	public String getR10_BRANCH_CODE_AND_NAME() {
		return R10_BRANCH_CODE_AND_NAME;
	}
	public void setR10_BRANCH_CODE_AND_NAME(String r10_BRANCH_CODE_AND_NAME) {
		R10_BRANCH_CODE_AND_NAME = r10_BRANCH_CODE_AND_NAME;
	}
	public BigDecimal getR10_ACCOUNT_BALANCE_IN_PULA() {
		return R10_ACCOUNT_BALANCE_IN_PULA;
	}
	public void setR10_ACCOUNT_BALANCE_IN_PULA(BigDecimal r10_ACCOUNT_BALANCE_IN_PULA) {
		R10_ACCOUNT_BALANCE_IN_PULA = r10_ACCOUNT_BALANCE_IN_PULA;
	}
	public String getR10_CURRENCY_OF_ACCOUNT() {
		return R10_CURRENCY_OF_ACCOUNT;
	}
	public void setR10_CURRENCY_OF_ACCOUNT(String r10_CURRENCY_OF_ACCOUNT) {
		R10_CURRENCY_OF_ACCOUNT = r10_CURRENCY_OF_ACCOUNT;
	}
	public BigDecimal getR10_EXCHANGE_RATE() {
		return R10_EXCHANGE_RATE;
	}
	public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
		R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
	}
	public String getR11_RECORD_NUMBER() {
		return R11_RECORD_NUMBER;
	}
	public void setR11_RECORD_NUMBER(String r11_RECORD_NUMBER) {
		R11_RECORD_NUMBER = r11_RECORD_NUMBER;
	}
	public String getR11_TITLE() {
		return R11_TITLE;
	}
	public void setR11_TITLE(String r11_TITLE) {
		R11_TITLE = r11_TITLE;
	}
	public String getR11_FIRST_NAME() {
		return R11_FIRST_NAME;
	}
	public void setR11_FIRST_NAME(String r11_FIRST_NAME) {
		R11_FIRST_NAME = r11_FIRST_NAME;
	}
	public String getR11_MIDDLE_NAME() {
		return R11_MIDDLE_NAME;
	}
	public void setR11_MIDDLE_NAME(String r11_MIDDLE_NAME) {
		R11_MIDDLE_NAME = r11_MIDDLE_NAME;
	}
	public String getR11_SURNAME() {
		return R11_SURNAME;
	}
	public void setR11_SURNAME(String r11_SURNAME) {
		R11_SURNAME = r11_SURNAME;
	}
	public String getR11_PREVIOUS_NAME() {
		return R11_PREVIOUS_NAME;
	}
	public void setR11_PREVIOUS_NAME(String r11_PREVIOUS_NAME) {
		R11_PREVIOUS_NAME = r11_PREVIOUS_NAME;
	}
	public String getR11_GENDER() {
		return R11_GENDER;
	}
	public void setR11_GENDER(String r11_GENDER) {
		R11_GENDER = r11_GENDER;
	}
	public String getR11_IDENTIFICATION_TYPE() {
		return R11_IDENTIFICATION_TYPE;
	}
	public void setR11_IDENTIFICATION_TYPE(String r11_IDENTIFICATION_TYPE) {
		R11_IDENTIFICATION_TYPE = r11_IDENTIFICATION_TYPE;
	}
	public String getR11_PASSPORT_NUMBER() {
		return R11_PASSPORT_NUMBER;
	}
	public void setR11_PASSPORT_NUMBER(String r11_PASSPORT_NUMBER) {
		R11_PASSPORT_NUMBER = r11_PASSPORT_NUMBER;
	}
	public Date getR11_DATE_OF_BIRTH() {
		return R11_DATE_OF_BIRTH;
	}
	public void setR11_DATE_OF_BIRTH(Date r11_DATE_OF_BIRTH) {
		R11_DATE_OF_BIRTH = r11_DATE_OF_BIRTH;
	}
	public String getR11_HOME_ADDRESS() {
		return R11_HOME_ADDRESS;
	}
	public void setR11_HOME_ADDRESS(String r11_HOME_ADDRESS) {
		R11_HOME_ADDRESS = r11_HOME_ADDRESS;
	}
	public String getR11_POSTAL_ADDRESS() {
		return R11_POSTAL_ADDRESS;
	}
	public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
		R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
	}
	public String getR11_RESIDENCE() {
		return R11_RESIDENCE;
	}
	public void setR11_RESIDENCE(String r11_RESIDENCE) {
		R11_RESIDENCE = r11_RESIDENCE;
	}
	public String getR11_EMAIL() {
		return R11_EMAIL;
	}
	public void setR11_EMAIL(String r11_EMAIL) {
		R11_EMAIL = r11_EMAIL;
	}
	public String getR11_LANDLINE() {
		return R11_LANDLINE;
	}
	public void setR11_LANDLINE(String r11_LANDLINE) {
		R11_LANDLINE = r11_LANDLINE;
	}
	public String getR11_MOBILE_PHONE_NUMBER() {
		return R11_MOBILE_PHONE_NUMBER;
	}
	public void setR11_MOBILE_PHONE_NUMBER(String r11_MOBILE_PHONE_NUMBER) {
		R11_MOBILE_PHONE_NUMBER = r11_MOBILE_PHONE_NUMBER;
	}
	public String getR11_MOBILE_MONEY_NUMBER() {
		return R11_MOBILE_MONEY_NUMBER;
	}
	public void setR11_MOBILE_MONEY_NUMBER(String r11_MOBILE_MONEY_NUMBER) {
		R11_MOBILE_MONEY_NUMBER = r11_MOBILE_MONEY_NUMBER;
	}
	public String getR11_PRODUCT_TYPE() {
		return R11_PRODUCT_TYPE;
	}
	public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
		R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
	}
	public String getR11_ACCOUNT_BY_OWNERSHIP() {
		return R11_ACCOUNT_BY_OWNERSHIP;
	}
	public void setR11_ACCOUNT_BY_OWNERSHIP(String r11_ACCOUNT_BY_OWNERSHIP) {
		R11_ACCOUNT_BY_OWNERSHIP = r11_ACCOUNT_BY_OWNERSHIP;
	}
	public String getR11_ACCOUNT_NUMBER() {
		return R11_ACCOUNT_NUMBER;
	}
	public void setR11_ACCOUNT_NUMBER(String r11_ACCOUNT_NUMBER) {
		R11_ACCOUNT_NUMBER = r11_ACCOUNT_NUMBER;
	}
	public BigDecimal getR11_ACCOUNT_HOLDER_INDICATOR() {
		return R11_ACCOUNT_HOLDER_INDICATOR;
	}
	public void setR11_ACCOUNT_HOLDER_INDICATOR(BigDecimal r11_ACCOUNT_HOLDER_INDICATOR) {
		R11_ACCOUNT_HOLDER_INDICATOR = r11_ACCOUNT_HOLDER_INDICATOR;
	}
	public String getR11_STATUS_OF_ACCOUNT() {
		return R11_STATUS_OF_ACCOUNT;
	}
	public void setR11_STATUS_OF_ACCOUNT(String r11_STATUS_OF_ACCOUNT) {
		R11_STATUS_OF_ACCOUNT = r11_STATUS_OF_ACCOUNT;
	}
	public String getR11_NOT_FIT_FOR_STP() {
		return R11_NOT_FIT_FOR_STP;
	}
	public void setR11_NOT_FIT_FOR_STP(String r11_NOT_FIT_FOR_STP) {
		R11_NOT_FIT_FOR_STP = r11_NOT_FIT_FOR_STP;
	}
	public String getR11_BRANCH_CODE_AND_NAME() {
		return R11_BRANCH_CODE_AND_NAME;
	}
	public void setR11_BRANCH_CODE_AND_NAME(String r11_BRANCH_CODE_AND_NAME) {
		R11_BRANCH_CODE_AND_NAME = r11_BRANCH_CODE_AND_NAME;
	}
	public BigDecimal getR11_ACCOUNT_BALANCE_IN_PULA() {
		return R11_ACCOUNT_BALANCE_IN_PULA;
	}
	public void setR11_ACCOUNT_BALANCE_IN_PULA(BigDecimal r11_ACCOUNT_BALANCE_IN_PULA) {
		R11_ACCOUNT_BALANCE_IN_PULA = r11_ACCOUNT_BALANCE_IN_PULA;
	}
	public String getR11_CURRENCY_OF_ACCOUNT() {
		return R11_CURRENCY_OF_ACCOUNT;
	}
	public void setR11_CURRENCY_OF_ACCOUNT(String r11_CURRENCY_OF_ACCOUNT) {
		R11_CURRENCY_OF_ACCOUNT = r11_CURRENCY_OF_ACCOUNT;
	}
	public BigDecimal getR11_EXCHANGE_RATE() {
		return R11_EXCHANGE_RATE;
	}
	public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
		R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
	}
	public BDISB1_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
    
    
    

}
