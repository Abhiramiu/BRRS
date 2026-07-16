
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
@Table(name = "BRRS_M_INT_RATES_FCA_RESUB_SUMMARYTABLE")
@IdClass(M_INT_RATES_FCA_PK.class)


public class M_INT_RATES_FCA_RESUB_Summary_Entity {
	
	private String R10_CURRENCY;
	private String R10_CURRENT;
	private String R10_CALL;
	private String R10_SAVINGS;
	private String R10_NOTICE_0_31_DAYS;
	private String R10_NOTICE_32_88_DAYS;
	private String R10_91_DEPOSIT_DAY;
	private String R10_FD_1_6_MONTHS;
	private String R10_FD_7_12_MONTHS;
	private String R10_FD_13_18_MONTHS;
	private String R10_FD_19_24_MONTHS;
	private String R10_FD_OVER_24_MONTHS;
	private String R10_TOTAL;

	private String R11_CURRENCY;
	private String R11_CURRENT;
	private String R11_CALL;
	private String R11_SAVINGS;
	private String R11_NOTICE_0_31_DAYS;
	private String R11_NOTICE_32_88_DAYS;
	private String R11_91_DEPOSIT_DAY;
	private String R11_FD_1_6_MONTHS;
	private String R11_FD_7_12_MONTHS;
	private String R11_FD_13_18_MONTHS;
	private String R11_FD_19_24_MONTHS;
	private String R11_FD_OVER_24_MONTHS;
	private String R11_TOTAL;

	private String R12_CURRENCY;
	private String R12_CURRENT;
	private String R12_CALL;
	private String R12_SAVINGS;
	private String R12_NOTICE_0_31_DAYS;
	private String R12_NOTICE_32_88_DAYS;
	private String R12_91_DEPOSIT_DAY;
	private String R12_FD_1_6_MONTHS;
	private String R12_FD_7_12_MONTHS;
	private String R12_FD_13_18_MONTHS;
	private String R12_FD_19_24_MONTHS;
	private String R12_FD_OVER_24_MONTHS;
	private String R12_TOTAL;

	private String R13_CURRENCY;
	private String R13_CURRENT;
	private String R13_CALL;
	private String R13_SAVINGS;
	private String R13_NOTICE_0_31_DAYS;
	private String R13_NOTICE_32_88_DAYS;
	private String R13_91_DEPOSIT_DAY;
	private String R13_FD_1_6_MONTHS;
	private String R13_FD_7_12_MONTHS;
	private String R13_FD_13_18_MONTHS;
	private String R13_FD_19_24_MONTHS;
	private String R13_FD_OVER_24_MONTHS;
	private String R13_TOTAL;

	private String R14_CURRENCY;
	private String R14_CURRENT;
	private String R14_CALL;
	private String R14_SAVINGS;
	private String R14_NOTICE_0_31_DAYS;
	private String R14_NOTICE_32_88_DAYS;
	private String R14_91_DEPOSIT_DAY;
	private String R14_FD_1_6_MONTHS;
	private String R14_FD_7_12_MONTHS;
	private String R14_FD_13_18_MONTHS;
	private String R14_FD_19_24_MONTHS;
	private String R14_FD_OVER_24_MONTHS;
	private String R14_TOTAL;

	private String R15_CURRENCY;
	private String R15_CURRENT;
	private String R15_CALL;
	private String R15_SAVINGS;
	private String R15_NOTICE_0_31_DAYS;
	private String R15_NOTICE_32_88_DAYS;
	private String R15_91_DEPOSIT_DAY;
	private String R15_FD_1_6_MONTHS;
	private String R15_FD_7_12_MONTHS;
	private String R15_FD_13_18_MONTHS;
	private String R15_FD_19_24_MONTHS;
	private String R15_FD_OVER_24_MONTHS;
	private String R15_TOTAL;
	
	
	@Id
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "REPORT_DATE")
    private Date report_date;
	
	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal report_version;
	
    @Column(name = "REPORT_RESUBDATE")
    
    private Date reportResubDate;
    
    
	private String report_frequency;
	private String report_code;
	private String report_desc;
	private String entity_flg;
	private String modify_flg;
	private String del_flg;
	public String getR10_CURRENCY() {
		return R10_CURRENCY;
	}
	public void setR10_CURRENCY(String r10_CURRENCY) {
		R10_CURRENCY = r10_CURRENCY;
	}
	public String getR10_CURRENT() {
		return R10_CURRENT;
	}
	public void setR10_CURRENT(String r10_CURRENT) {
		R10_CURRENT = r10_CURRENT;
	}
	public String getR10_CALL() {
		return R10_CALL;
	}
	public void setR10_CALL(String r10_CALL) {
		R10_CALL = r10_CALL;
	}
	public String getR10_SAVINGS() {
		return R10_SAVINGS;
	}
	public void setR10_SAVINGS(String r10_SAVINGS) {
		R10_SAVINGS = r10_SAVINGS;
	}
	public String getR10_NOTICE_0_31_DAYS() {
		return R10_NOTICE_0_31_DAYS;
	}
	public void setR10_NOTICE_0_31_DAYS(String r10_NOTICE_0_31_DAYS) {
		R10_NOTICE_0_31_DAYS = r10_NOTICE_0_31_DAYS;
	}
	public String getR10_NOTICE_32_88_DAYS() {
		return R10_NOTICE_32_88_DAYS;
	}
	public void setR10_NOTICE_32_88_DAYS(String r10_NOTICE_32_88_DAYS) {
		R10_NOTICE_32_88_DAYS = r10_NOTICE_32_88_DAYS;
	}
	public String getR10_91_DEPOSIT_DAY() {
		return R10_91_DEPOSIT_DAY;
	}
	public void setR10_91_DEPOSIT_DAY(String r10_91_DEPOSIT_DAY) {
		R10_91_DEPOSIT_DAY = r10_91_DEPOSIT_DAY;
	}
	public String getR10_FD_1_6_MONTHS() {
		return R10_FD_1_6_MONTHS;
	}
	public void setR10_FD_1_6_MONTHS(String r10_FD_1_6_MONTHS) {
		R10_FD_1_6_MONTHS = r10_FD_1_6_MONTHS;
	}
	public String getR10_FD_7_12_MONTHS() {
		return R10_FD_7_12_MONTHS;
	}
	public void setR10_FD_7_12_MONTHS(String r10_FD_7_12_MONTHS) {
		R10_FD_7_12_MONTHS = r10_FD_7_12_MONTHS;
	}
	public String getR10_FD_13_18_MONTHS() {
		return R10_FD_13_18_MONTHS;
	}
	public void setR10_FD_13_18_MONTHS(String r10_FD_13_18_MONTHS) {
		R10_FD_13_18_MONTHS = r10_FD_13_18_MONTHS;
	}
	public String getR10_FD_19_24_MONTHS() {
		return R10_FD_19_24_MONTHS;
	}
	public void setR10_FD_19_24_MONTHS(String r10_FD_19_24_MONTHS) {
		R10_FD_19_24_MONTHS = r10_FD_19_24_MONTHS;
	}
	public String getR10_FD_OVER_24_MONTHS() {
		return R10_FD_OVER_24_MONTHS;
	}
	public void setR10_FD_OVER_24_MONTHS(String r10_FD_OVER_24_MONTHS) {
		R10_FD_OVER_24_MONTHS = r10_FD_OVER_24_MONTHS;
	}
	public String getR10_TOTAL() {
		return R10_TOTAL;
	}
	public void setR10_TOTAL(String r10_TOTAL) {
		R10_TOTAL = r10_TOTAL;
	}
	public String getR11_CURRENCY() {
		return R11_CURRENCY;
	}
	public void setR11_CURRENCY(String r11_CURRENCY) {
		R11_CURRENCY = r11_CURRENCY;
	}
	public String getR11_CURRENT() {
		return R11_CURRENT;
	}
	public void setR11_CURRENT(String r11_CURRENT) {
		R11_CURRENT = r11_CURRENT;
	}
	public String getR11_CALL() {
		return R11_CALL;
	}
	public void setR11_CALL(String r11_CALL) {
		R11_CALL = r11_CALL;
	}
	public String getR11_SAVINGS() {
		return R11_SAVINGS;
	}
	public void setR11_SAVINGS(String r11_SAVINGS) {
		R11_SAVINGS = r11_SAVINGS;
	}
	public String getR11_NOTICE_0_31_DAYS() {
		return R11_NOTICE_0_31_DAYS;
	}
	public void setR11_NOTICE_0_31_DAYS(String r11_NOTICE_0_31_DAYS) {
		R11_NOTICE_0_31_DAYS = r11_NOTICE_0_31_DAYS;
	}
	public String getR11_NOTICE_32_88_DAYS() {
		return R11_NOTICE_32_88_DAYS;
	}
	public void setR11_NOTICE_32_88_DAYS(String r11_NOTICE_32_88_DAYS) {
		R11_NOTICE_32_88_DAYS = r11_NOTICE_32_88_DAYS;
	}
	public String getR11_91_DEPOSIT_DAY() {
		return R11_91_DEPOSIT_DAY;
	}
	public void setR11_91_DEPOSIT_DAY(String r11_91_DEPOSIT_DAY) {
		R11_91_DEPOSIT_DAY = r11_91_DEPOSIT_DAY;
	}
	public String getR11_FD_1_6_MONTHS() {
		return R11_FD_1_6_MONTHS;
	}
	public void setR11_FD_1_6_MONTHS(String r11_FD_1_6_MONTHS) {
		R11_FD_1_6_MONTHS = r11_FD_1_6_MONTHS;
	}
	public String getR11_FD_7_12_MONTHS() {
		return R11_FD_7_12_MONTHS;
	}
	public void setR11_FD_7_12_MONTHS(String r11_FD_7_12_MONTHS) {
		R11_FD_7_12_MONTHS = r11_FD_7_12_MONTHS;
	}
	public String getR11_FD_13_18_MONTHS() {
		return R11_FD_13_18_MONTHS;
	}
	public void setR11_FD_13_18_MONTHS(String r11_FD_13_18_MONTHS) {
		R11_FD_13_18_MONTHS = r11_FD_13_18_MONTHS;
	}
	public String getR11_FD_19_24_MONTHS() {
		return R11_FD_19_24_MONTHS;
	}
	public void setR11_FD_19_24_MONTHS(String r11_FD_19_24_MONTHS) {
		R11_FD_19_24_MONTHS = r11_FD_19_24_MONTHS;
	}
	public String getR11_FD_OVER_24_MONTHS() {
		return R11_FD_OVER_24_MONTHS;
	}
	public void setR11_FD_OVER_24_MONTHS(String r11_FD_OVER_24_MONTHS) {
		R11_FD_OVER_24_MONTHS = r11_FD_OVER_24_MONTHS;
	}
	public String getR11_TOTAL() {
		return R11_TOTAL;
	}
	public void setR11_TOTAL(String r11_TOTAL) {
		R11_TOTAL = r11_TOTAL;
	}
	public String getR12_CURRENCY() {
		return R12_CURRENCY;
	}
	public void setR12_CURRENCY(String r12_CURRENCY) {
		R12_CURRENCY = r12_CURRENCY;
	}
	public String getR12_CURRENT() {
		return R12_CURRENT;
	}
	public void setR12_CURRENT(String r12_CURRENT) {
		R12_CURRENT = r12_CURRENT;
	}
	public String getR12_CALL() {
		return R12_CALL;
	}
	public void setR12_CALL(String r12_CALL) {
		R12_CALL = r12_CALL;
	}
	public String getR12_SAVINGS() {
		return R12_SAVINGS;
	}
	public void setR12_SAVINGS(String r12_SAVINGS) {
		R12_SAVINGS = r12_SAVINGS;
	}
	public String getR12_NOTICE_0_31_DAYS() {
		return R12_NOTICE_0_31_DAYS;
	}
	public void setR12_NOTICE_0_31_DAYS(String r12_NOTICE_0_31_DAYS) {
		R12_NOTICE_0_31_DAYS = r12_NOTICE_0_31_DAYS;
	}
	public String getR12_NOTICE_32_88_DAYS() {
		return R12_NOTICE_32_88_DAYS;
	}
	public void setR12_NOTICE_32_88_DAYS(String r12_NOTICE_32_88_DAYS) {
		R12_NOTICE_32_88_DAYS = r12_NOTICE_32_88_DAYS;
	}
	public String getR12_91_DEPOSIT_DAY() {
		return R12_91_DEPOSIT_DAY;
	}
	public void setR12_91_DEPOSIT_DAY(String r12_91_DEPOSIT_DAY) {
		R12_91_DEPOSIT_DAY = r12_91_DEPOSIT_DAY;
	}
	public String getR12_FD_1_6_MONTHS() {
		return R12_FD_1_6_MONTHS;
	}
	public void setR12_FD_1_6_MONTHS(String r12_FD_1_6_MONTHS) {
		R12_FD_1_6_MONTHS = r12_FD_1_6_MONTHS;
	}
	public String getR12_FD_7_12_MONTHS() {
		return R12_FD_7_12_MONTHS;
	}
	public void setR12_FD_7_12_MONTHS(String r12_FD_7_12_MONTHS) {
		R12_FD_7_12_MONTHS = r12_FD_7_12_MONTHS;
	}
	public String getR12_FD_13_18_MONTHS() {
		return R12_FD_13_18_MONTHS;
	}
	public void setR12_FD_13_18_MONTHS(String r12_FD_13_18_MONTHS) {
		R12_FD_13_18_MONTHS = r12_FD_13_18_MONTHS;
	}
	public String getR12_FD_19_24_MONTHS() {
		return R12_FD_19_24_MONTHS;
	}
	public void setR12_FD_19_24_MONTHS(String r12_FD_19_24_MONTHS) {
		R12_FD_19_24_MONTHS = r12_FD_19_24_MONTHS;
	}
	public String getR12_FD_OVER_24_MONTHS() {
		return R12_FD_OVER_24_MONTHS;
	}
	public void setR12_FD_OVER_24_MONTHS(String r12_FD_OVER_24_MONTHS) {
		R12_FD_OVER_24_MONTHS = r12_FD_OVER_24_MONTHS;
	}
	public String getR12_TOTAL() {
		return R12_TOTAL;
	}
	public void setR12_TOTAL(String r12_TOTAL) {
		R12_TOTAL = r12_TOTAL;
	}
	public String getR13_CURRENCY() {
		return R13_CURRENCY;
	}
	public void setR13_CURRENCY(String r13_CURRENCY) {
		R13_CURRENCY = r13_CURRENCY;
	}
	public String getR13_CURRENT() {
		return R13_CURRENT;
	}
	public void setR13_CURRENT(String r13_CURRENT) {
		R13_CURRENT = r13_CURRENT;
	}
	public String getR13_CALL() {
		return R13_CALL;
	}
	public void setR13_CALL(String r13_CALL) {
		R13_CALL = r13_CALL;
	}
	public String getR13_SAVINGS() {
		return R13_SAVINGS;
	}
	public void setR13_SAVINGS(String r13_SAVINGS) {
		R13_SAVINGS = r13_SAVINGS;
	}
	public String getR13_NOTICE_0_31_DAYS() {
		return R13_NOTICE_0_31_DAYS;
	}
	public void setR13_NOTICE_0_31_DAYS(String r13_NOTICE_0_31_DAYS) {
		R13_NOTICE_0_31_DAYS = r13_NOTICE_0_31_DAYS;
	}
	public String getR13_NOTICE_32_88_DAYS() {
		return R13_NOTICE_32_88_DAYS;
	}
	public void setR13_NOTICE_32_88_DAYS(String r13_NOTICE_32_88_DAYS) {
		R13_NOTICE_32_88_DAYS = r13_NOTICE_32_88_DAYS;
	}
	public String getR13_91_DEPOSIT_DAY() {
		return R13_91_DEPOSIT_DAY;
	}
	public void setR13_91_DEPOSIT_DAY(String r13_91_DEPOSIT_DAY) {
		R13_91_DEPOSIT_DAY = r13_91_DEPOSIT_DAY;
	}
	public String getR13_FD_1_6_MONTHS() {
		return R13_FD_1_6_MONTHS;
	}
	public void setR13_FD_1_6_MONTHS(String r13_FD_1_6_MONTHS) {
		R13_FD_1_6_MONTHS = r13_FD_1_6_MONTHS;
	}
	public String getR13_FD_7_12_MONTHS() {
		return R13_FD_7_12_MONTHS;
	}
	public void setR13_FD_7_12_MONTHS(String r13_FD_7_12_MONTHS) {
		R13_FD_7_12_MONTHS = r13_FD_7_12_MONTHS;
	}
	public String getR13_FD_13_18_MONTHS() {
		return R13_FD_13_18_MONTHS;
	}
	public void setR13_FD_13_18_MONTHS(String r13_FD_13_18_MONTHS) {
		R13_FD_13_18_MONTHS = r13_FD_13_18_MONTHS;
	}
	public String getR13_FD_19_24_MONTHS() {
		return R13_FD_19_24_MONTHS;
	}
	public void setR13_FD_19_24_MONTHS(String r13_FD_19_24_MONTHS) {
		R13_FD_19_24_MONTHS = r13_FD_19_24_MONTHS;
	}
	public String getR13_FD_OVER_24_MONTHS() {
		return R13_FD_OVER_24_MONTHS;
	}
	public void setR13_FD_OVER_24_MONTHS(String r13_FD_OVER_24_MONTHS) {
		R13_FD_OVER_24_MONTHS = r13_FD_OVER_24_MONTHS;
	}
	public String getR13_TOTAL() {
		return R13_TOTAL;
	}
	public void setR13_TOTAL(String r13_TOTAL) {
		R13_TOTAL = r13_TOTAL;
	}
	public String getR14_CURRENCY() {
		return R14_CURRENCY;
	}
	public void setR14_CURRENCY(String r14_CURRENCY) {
		R14_CURRENCY = r14_CURRENCY;
	}
	public String getR14_CURRENT() {
		return R14_CURRENT;
	}
	public void setR14_CURRENT(String r14_CURRENT) {
		R14_CURRENT = r14_CURRENT;
	}
	public String getR14_CALL() {
		return R14_CALL;
	}
	public void setR14_CALL(String r14_CALL) {
		R14_CALL = r14_CALL;
	}
	public String getR14_SAVINGS() {
		return R14_SAVINGS;
	}
	public void setR14_SAVINGS(String r14_SAVINGS) {
		R14_SAVINGS = r14_SAVINGS;
	}
	public String getR14_NOTICE_0_31_DAYS() {
		return R14_NOTICE_0_31_DAYS;
	}
	public void setR14_NOTICE_0_31_DAYS(String r14_NOTICE_0_31_DAYS) {
		R14_NOTICE_0_31_DAYS = r14_NOTICE_0_31_DAYS;
	}
	public String getR14_NOTICE_32_88_DAYS() {
		return R14_NOTICE_32_88_DAYS;
	}
	public void setR14_NOTICE_32_88_DAYS(String r14_NOTICE_32_88_DAYS) {
		R14_NOTICE_32_88_DAYS = r14_NOTICE_32_88_DAYS;
	}
	public String getR14_91_DEPOSIT_DAY() {
		return R14_91_DEPOSIT_DAY;
	}
	public void setR14_91_DEPOSIT_DAY(String r14_91_DEPOSIT_DAY) {
		R14_91_DEPOSIT_DAY = r14_91_DEPOSIT_DAY;
	}
	public String getR14_FD_1_6_MONTHS() {
		return R14_FD_1_6_MONTHS;
	}
	public void setR14_FD_1_6_MONTHS(String r14_FD_1_6_MONTHS) {
		R14_FD_1_6_MONTHS = r14_FD_1_6_MONTHS;
	}
	public String getR14_FD_7_12_MONTHS() {
		return R14_FD_7_12_MONTHS;
	}
	public void setR14_FD_7_12_MONTHS(String r14_FD_7_12_MONTHS) {
		R14_FD_7_12_MONTHS = r14_FD_7_12_MONTHS;
	}
	public String getR14_FD_13_18_MONTHS() {
		return R14_FD_13_18_MONTHS;
	}
	public void setR14_FD_13_18_MONTHS(String r14_FD_13_18_MONTHS) {
		R14_FD_13_18_MONTHS = r14_FD_13_18_MONTHS;
	}
	public String getR14_FD_19_24_MONTHS() {
		return R14_FD_19_24_MONTHS;
	}
	public void setR14_FD_19_24_MONTHS(String r14_FD_19_24_MONTHS) {
		R14_FD_19_24_MONTHS = r14_FD_19_24_MONTHS;
	}
	public String getR14_FD_OVER_24_MONTHS() {
		return R14_FD_OVER_24_MONTHS;
	}
	public void setR14_FD_OVER_24_MONTHS(String r14_FD_OVER_24_MONTHS) {
		R14_FD_OVER_24_MONTHS = r14_FD_OVER_24_MONTHS;
	}
	public String getR14_TOTAL() {
		return R14_TOTAL;
	}
	public void setR14_TOTAL(String r14_TOTAL) {
		R14_TOTAL = r14_TOTAL;
	}
	public String getR15_CURRENCY() {
		return R15_CURRENCY;
	}
	public void setR15_CURRENCY(String r15_CURRENCY) {
		R15_CURRENCY = r15_CURRENCY;
	}
	public String getR15_CURRENT() {
		return R15_CURRENT;
	}
	public void setR15_CURRENT(String r15_CURRENT) {
		R15_CURRENT = r15_CURRENT;
	}
	public String getR15_CALL() {
		return R15_CALL;
	}
	public void setR15_CALL(String r15_CALL) {
		R15_CALL = r15_CALL;
	}
	public String getR15_SAVINGS() {
		return R15_SAVINGS;
	}
	public void setR15_SAVINGS(String r15_SAVINGS) {
		R15_SAVINGS = r15_SAVINGS;
	}
	public String getR15_NOTICE_0_31_DAYS() {
		return R15_NOTICE_0_31_DAYS;
	}
	public void setR15_NOTICE_0_31_DAYS(String r15_NOTICE_0_31_DAYS) {
		R15_NOTICE_0_31_DAYS = r15_NOTICE_0_31_DAYS;
	}
	public String getR15_NOTICE_32_88_DAYS() {
		return R15_NOTICE_32_88_DAYS;
	}
	public void setR15_NOTICE_32_88_DAYS(String r15_NOTICE_32_88_DAYS) {
		R15_NOTICE_32_88_DAYS = r15_NOTICE_32_88_DAYS;
	}
	public String getR15_91_DEPOSIT_DAY() {
		return R15_91_DEPOSIT_DAY;
	}
	public void setR15_91_DEPOSIT_DAY(String r15_91_DEPOSIT_DAY) {
		R15_91_DEPOSIT_DAY = r15_91_DEPOSIT_DAY;
	}
	public String getR15_FD_1_6_MONTHS() {
		return R15_FD_1_6_MONTHS;
	}
	public void setR15_FD_1_6_MONTHS(String r15_FD_1_6_MONTHS) {
		R15_FD_1_6_MONTHS = r15_FD_1_6_MONTHS;
	}
	public String getR15_FD_7_12_MONTHS() {
		return R15_FD_7_12_MONTHS;
	}
	public void setR15_FD_7_12_MONTHS(String r15_FD_7_12_MONTHS) {
		R15_FD_7_12_MONTHS = r15_FD_7_12_MONTHS;
	}
	public String getR15_FD_13_18_MONTHS() {
		return R15_FD_13_18_MONTHS;
	}
	public void setR15_FD_13_18_MONTHS(String r15_FD_13_18_MONTHS) {
		R15_FD_13_18_MONTHS = r15_FD_13_18_MONTHS;
	}
	public String getR15_FD_19_24_MONTHS() {
		return R15_FD_19_24_MONTHS;
	}
	public void setR15_FD_19_24_MONTHS(String r15_FD_19_24_MONTHS) {
		R15_FD_19_24_MONTHS = r15_FD_19_24_MONTHS;
	}
	public String getR15_FD_OVER_24_MONTHS() {
		return R15_FD_OVER_24_MONTHS;
	}
	public void setR15_FD_OVER_24_MONTHS(String r15_FD_OVER_24_MONTHS) {
		R15_FD_OVER_24_MONTHS = r15_FD_OVER_24_MONTHS;
	}
	public String getR15_TOTAL() {
		return R15_TOTAL;
	}
	public void setR15_TOTAL(String r15_TOTAL) {
		R15_TOTAL = r15_TOTAL;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public BigDecimal getReport_version() {
		return report_version;
	}
	public void setReport_version(BigDecimal report_version) {
		this.report_version = report_version;
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
	public M_INT_RATES_FCA_RESUB_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
