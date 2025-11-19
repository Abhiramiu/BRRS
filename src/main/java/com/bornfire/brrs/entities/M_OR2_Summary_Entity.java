
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
@Table(name = "BRRS_M_OR2_SUMMARYTABLE")  
public class M_OR2_Summary_Entity {
	
	private String	r12_product; 
	private String	r12_business_line_month; 
	private BigDecimal	r12_corporate_finance;
	private BigDecimal	r12_trading_and_sales;
	private BigDecimal	r12_retail_banking;
	private BigDecimal	r12_commercial_banking;
	private BigDecimal	r12_payments_and_settlements;
	private BigDecimal	r12_agency_services;
	private BigDecimal	r12_asset_management;
	private BigDecimal	r12_retail_brokerage;
	private String	r13_business_line_month;
	private BigDecimal	r13_corporate_finance;
	private BigDecimal	r13_trading_and_sales;
	private BigDecimal	r13_retail_banking;
	private BigDecimal	r13_commercial_banking;
	private BigDecimal	r13_payments_and_settlements;
	private BigDecimal	r13_agency_services;
	private BigDecimal	r13_asset_management;
	private BigDecimal	r13_retail_brokerage;
	private String	r14_business_line_month;
	private BigDecimal	r14_corporate_finance;
	private BigDecimal	r14_trading_and_sales;
	private BigDecimal	r14_retail_banking;
	private BigDecimal	r14_commercial_banking;
	private BigDecimal	r14_payments_and_settlements;
	private BigDecimal	r14_agency_services;
	private BigDecimal	r14_asset_management;
	private BigDecimal	r14_retail_brokerage;
	private String	r15_business_line_month;
	private BigDecimal	r15_corporate_finance;
	private BigDecimal	r15_trading_and_sales;
	private BigDecimal	r15_retail_banking;
	private BigDecimal	r15_commercial_banking;
	private BigDecimal	r15_payments_and_settlements;
	private BigDecimal	r15_agency_services;
	private BigDecimal	r15_asset_management;
	private BigDecimal	r15_retail_brokerage;
	private String	r16_business_line_month;
	private BigDecimal	r16_corporate_finance;
	private BigDecimal	r16_trading_and_sales;
	private BigDecimal	r16_retail_banking;
	private BigDecimal	r16_commercial_banking;
	private BigDecimal	r16_payments_and_settlements;
	private BigDecimal	r16_agency_services;
	private BigDecimal	r16_asset_management;
	private BigDecimal	r16_retail_brokerage;
	private String	r17_business_line_month;
	private BigDecimal	r17_corporate_finance;
	private BigDecimal	r17_trading_and_sales;
	private BigDecimal	r17_retail_banking;
	private BigDecimal	r17_commercial_banking;
	private BigDecimal	r17_payments_and_settlements;
	private BigDecimal	r17_agency_services;
	private BigDecimal	r17_asset_management;
	private BigDecimal	r17_retail_brokerage;
	private String	r18_business_line_month;
	private BigDecimal	r18_corporate_finance;
	private BigDecimal	r18_trading_and_sales;
	private BigDecimal	r18_retail_banking;
	private BigDecimal	r18_commercial_banking;
	private BigDecimal	r18_payments_and_settlements;
	private BigDecimal	r18_agency_services;
	private BigDecimal	r18_asset_management;
	private BigDecimal	r18_retail_brokerage;
	private String	r19_business_line_month;
	private BigDecimal	r19_corporate_finance;
	private BigDecimal	r19_trading_and_sales;
	private BigDecimal	r19_retail_banking;
	private BigDecimal	r19_commercial_banking;
	private BigDecimal	r19_payments_and_settlements;
	private BigDecimal	r19_agency_services;
	private BigDecimal	r19_asset_management;
	private BigDecimal	r19_retail_brokerage;
	private String	r20_business_line_month;
	private BigDecimal	r20_corporate_finance;
	private BigDecimal	r20_trading_and_sales;
	private BigDecimal	r20_retail_banking;
	private BigDecimal	r20_commercial_banking;
	private BigDecimal	r20_payments_and_settlements;
	private BigDecimal	r20_agency_services;
	private BigDecimal	r20_asset_management;
	private BigDecimal	r20_retail_brokerage;
	private String	r21_business_line_month;
	private BigDecimal	r21_corporate_finance;
	private BigDecimal	r21_trading_and_sales;
	private BigDecimal	r21_retail_banking;
	private BigDecimal	r21_commercial_banking;
	private BigDecimal	r21_payments_and_settlements;
	private BigDecimal	r21_agency_services;
	private BigDecimal	r21_asset_management;
	private BigDecimal	r21_retail_brokerage;
	private String	r22_business_line_month;
	private BigDecimal	r22_corporate_finance;
	private BigDecimal	r22_trading_and_sales;
	private BigDecimal	r22_retail_banking;
	private BigDecimal	r22_commercial_banking;
	private BigDecimal	r22_payments_and_settlements;
	private BigDecimal	r22_agency_services;
	private BigDecimal	r22_asset_management;
	private BigDecimal	r22_retail_brokerage;
	private String	r23_business_line_month;
	private BigDecimal	r23_corporate_finance;
	private BigDecimal	r23_trading_and_sales;
	private BigDecimal	r23_retail_banking;
	private BigDecimal	r23_commercial_banking;
	private BigDecimal	r23_payments_and_settlements;
	private BigDecimal	r23_agency_services;
	private BigDecimal	r23_asset_management;
	private BigDecimal	r23_retail_brokerage;
	private String	r24_product;
	private BigDecimal	r24_corporate_finance;
	private BigDecimal	r24_trading_and_sales;
	private BigDecimal	r24_retail_banking;
	private BigDecimal	r24_commercial_banking;
	private BigDecimal	r24_payments_and_settlements;
	private BigDecimal	r24_agency_services;
	private BigDecimal	r24_asset_management;
	private BigDecimal	r24_retail_brokerage;
	private String	r25_product;
	private BigDecimal	r25_corporate_finance;
	private BigDecimal	r25_trading_and_sales;
	private BigDecimal	r25_retail_banking;
	private BigDecimal	r25_commercial_banking;
	private BigDecimal	r25_payments_and_settlements;
	private BigDecimal	r25_agency_services;
	private BigDecimal	r25_asset_management;
	private BigDecimal	r25_retail_brokerage;
	private String	r26_product;
	private BigDecimal	r26_corporate_finance;
	private BigDecimal	r26_trading_and_sales;
	private BigDecimal	r26_retail_banking;
	private BigDecimal	r26_commercial_banking;
	private BigDecimal	r26_payments_and_settlements;
	private BigDecimal	r26_agency_services;
	private BigDecimal	r26_asset_management;
	private BigDecimal	r26_retail_brokerage;
	private String	r27_product;
	private BigDecimal	r27_operational_risk_cap_charge;
	private String	r28_product;
	private String	r28_business_line_month;
	private BigDecimal	r28_corporate_finance;
	private BigDecimal	r28_trading_and_sales;
	private BigDecimal	r28_retail_banking;
	private BigDecimal	r28_commercial_banking;
	private BigDecimal	r28_payments_and_settlements;
	private BigDecimal	r28_agency_services;
	private BigDecimal	r28_asset_management;
	private BigDecimal	r28_retail_brokerage;
	private String	r29_business_line_month;
	private BigDecimal	r29_corporate_finance;
	private BigDecimal	r29_trading_and_sales;
	private BigDecimal	r29_retail_banking;
	private BigDecimal	r29_commercial_banking;
	private BigDecimal	r29_payments_and_settlements;
	private BigDecimal	r29_agency_services;
	private BigDecimal	r29_asset_management;
	private BigDecimal	r29_retail_brokerage;
	private String	r30_business_line_month;
	private BigDecimal	r30_corporate_finance;
	private BigDecimal	r30_trading_and_sales;
	private BigDecimal	r30_retail_banking;
	private BigDecimal	r30_commercial_banking;
	private BigDecimal	r30_payments_and_settlements;
	private BigDecimal	r30_agency_services;
	private BigDecimal	r30_asset_management;
	private BigDecimal	r30_retail_brokerage;
	private String	r31_business_line_month;
	private BigDecimal	r31_corporate_finance;
	private BigDecimal	r31_trading_and_sales;
	private BigDecimal	r31_retail_banking;
	private BigDecimal	r31_commercial_banking;
	private BigDecimal	r31_payments_and_settlements;
	private BigDecimal	r31_agency_services;
	private BigDecimal	r31_asset_management;
	private BigDecimal	r31_retail_brokerage;
	private String	r32_business_line_month;
	private BigDecimal	r32_corporate_finance;
	private BigDecimal	r32_trading_and_sales;
	private BigDecimal	r32_retail_banking;
	private BigDecimal	r32_commercial_banking;
	private BigDecimal	r32_payments_and_settlements;
	private BigDecimal	r32_agency_services;
	private BigDecimal	r32_asset_management;
	private BigDecimal	r32_retail_brokerage;
	private String	r33_business_line_month;
	private BigDecimal	r33_corporate_finance;
	private BigDecimal	r33_trading_and_sales;
	private BigDecimal	r33_retail_banking;
	private BigDecimal	r33_commercial_banking;
	private BigDecimal	r33_payments_and_settlements;
	private BigDecimal	r33_agency_services;
	private BigDecimal	r33_asset_management;
	private BigDecimal	r33_retail_brokerage;
	private String	r34_business_line_month;
	private BigDecimal	r34_corporate_finance;
	private BigDecimal	r34_trading_and_sales;
	private BigDecimal	r34_retail_banking;
	private BigDecimal	r34_commercial_banking;
	private BigDecimal	r34_payments_and_settlements;
	private BigDecimal	r34_agency_services;
	private BigDecimal	r34_asset_management;
	private BigDecimal	r34_retail_brokerage;
	private String	r35_business_line_month;
	private BigDecimal	r35_corporate_finance;
	private BigDecimal	r35_trading_and_sales;
	private BigDecimal	r35_retail_banking;
	private BigDecimal	r35_commercial_banking;
	private BigDecimal	r35_payments_and_settlements;
	private BigDecimal	r35_agency_services;
	private BigDecimal	r35_asset_management;
	private BigDecimal	r35_retail_brokerage;
	private String	r36_business_line_month;
	private BigDecimal	r36_corporate_finance;
	private BigDecimal	r36_trading_and_sales;
	private BigDecimal	r36_retail_banking;
	private BigDecimal	r36_commercial_banking;
	private BigDecimal	r36_payments_and_settlements;
	private BigDecimal	r36_agency_services;
	private BigDecimal	r36_asset_management;
	private BigDecimal	r36_retail_brokerage;
	private String	r37_business_line_month;
	private BigDecimal	r37_corporate_finance;
	private BigDecimal	r37_trading_and_sales;
	private BigDecimal	r37_retail_banking;
	private BigDecimal	r37_commercial_banking;
	private BigDecimal	r37_payments_and_settlements;
	private BigDecimal	r37_agency_services;
	private BigDecimal	r37_asset_management;
	private BigDecimal	r37_retail_brokerage;
	private String	r38_business_line_month;
	private BigDecimal	r38_corporate_finance;
	private BigDecimal	r38_trading_and_sales;
	private BigDecimal	r38_retail_banking;
	private BigDecimal	r38_commercial_banking;
	private BigDecimal	r38_payments_and_settlements;
	private BigDecimal	r38_agency_services;
	private BigDecimal	r38_asset_management;
	private BigDecimal	r38_retail_brokerage;
	private String	r39_business_line_month;
	private BigDecimal	r39_corporate_finance;
	private BigDecimal	r39_trading_and_sales;
	private BigDecimal	r39_retail_banking;
	private BigDecimal	r39_commercial_banking;
	private BigDecimal	r39_payments_and_settlements;
	private BigDecimal	r39_agency_services;
	private BigDecimal	r39_asset_management;
	private BigDecimal	r39_retail_brokerage;
	private String	r40_product;
	private BigDecimal	r40_corporate_finance;
	private BigDecimal	r40_trading_and_sales;
	private BigDecimal	r40_retail_banking;
	private BigDecimal	r40_commercial_banking;
	private BigDecimal	r40_payments_and_settlements;
	private BigDecimal	r40_agency_services;
	private BigDecimal	r40_asset_management;
	private BigDecimal	r40_retail_brokerage;
	private String	r41_product;
	private BigDecimal	r41_corporate_finance;
	private BigDecimal	r41_trading_and_sales;
	private BigDecimal	r41_retail_banking;
	private BigDecimal	r41_commercial_banking;
	private BigDecimal	r41_payments_and_settlements;
	private BigDecimal	r41_agency_services;
	private BigDecimal	r41_asset_management;
	private BigDecimal	r41_retail_brokerage;
	private String	r42_product;
	private BigDecimal	r42_corporate_finance;
	private BigDecimal	r42_trading_and_sales;
	private BigDecimal	r42_retail_banking;
	private BigDecimal	r42_commercial_banking;
	private BigDecimal	r42_payments_and_settlements;
	private BigDecimal	r42_agency_services;
	private BigDecimal	r42_asset_management;
	private BigDecimal	r42_retail_brokerage;
	private String	r43_product;
	private BigDecimal	r43_operational_risk_cap_charge;
	private String	r44_product;
	private String	r44_business_line_month;
	private BigDecimal	r44_corporate_finance;
	private BigDecimal	r44_trading_and_sales;
	private BigDecimal	r44_retail_banking;
	private BigDecimal	r44_commercial_banking;
	private BigDecimal	r44_payments_and_settlements;
	private BigDecimal	r44_agency_services;
	private BigDecimal	r44_asset_management;
	private BigDecimal	r44_retail_brokerage;
	private String	r45_business_line_month;
	private BigDecimal	r45_corporate_finance;
	private BigDecimal	r45_trading_and_sales;
	private BigDecimal	r45_retail_banking;
	private BigDecimal	r45_commercial_banking;
	private BigDecimal	r45_payments_and_settlements;
	private BigDecimal	r45_agency_services;
	private BigDecimal	r45_asset_management;
	private BigDecimal	r45_retail_brokerage;
	private String	r46_business_line_month;
	private BigDecimal	r46_corporate_finance;
	private BigDecimal	r46_trading_and_sales;
	private BigDecimal	r46_retail_banking;
	private BigDecimal	r46_commercial_banking;
	private BigDecimal	r46_payments_and_settlements;
	private BigDecimal	r46_agency_services;
	private BigDecimal	r46_asset_management;
	private BigDecimal	r46_retail_brokerage;
	private String	r47_business_line_month;
	private BigDecimal	r47_corporate_finance;
	private BigDecimal	r47_trading_and_sales;
	private BigDecimal	r47_retail_banking;
	private BigDecimal	r47_commercial_banking;
	private BigDecimal	r47_payments_and_settlements;
	private BigDecimal	r47_agency_services;
	private BigDecimal	r47_asset_management;
	private BigDecimal	r47_retail_brokerage;
	private String	r48_business_line_month;
	private BigDecimal	r48_corporate_finance;
	private BigDecimal	r48_trading_and_sales;
	private BigDecimal	r48_retail_banking;
	private BigDecimal	r48_commercial_banking;
	private BigDecimal	r48_payments_and_settlements;
	private BigDecimal	r48_agency_services;
	private BigDecimal	r48_asset_management;
	private BigDecimal	r48_retail_brokerage;
	private String	r49_business_line_month;
	private BigDecimal	r49_corporate_finance;
	private BigDecimal	r49_trading_and_sales;
	private BigDecimal	r49_retail_banking;
	private BigDecimal	r49_commercial_banking;
	private BigDecimal	r49_payments_and_settlements;
	private BigDecimal	r49_agency_services;
	private BigDecimal	r49_asset_management;
	private BigDecimal	r49_retail_brokerage;
	private String	r50_business_line_month;
	private BigDecimal	r50_corporate_finance;
	private BigDecimal	r50_trading_and_sales;
	private BigDecimal	r50_retail_banking;
	private BigDecimal	r50_commercial_banking;
	private BigDecimal	r50_payments_and_settlements;
	private BigDecimal	r50_agency_services;
	private BigDecimal	r50_asset_management;
	private BigDecimal	r50_retail_brokerage;
	private String	r51_business_line_month;
	private BigDecimal	r51_corporate_finance;
	private BigDecimal	r51_trading_and_sales;
	private BigDecimal	r51_retail_banking;
	private BigDecimal	r51_commercial_banking;
	private BigDecimal	r51_payments_and_settlements;
	private BigDecimal	r51_agency_services;
	private BigDecimal	r51_asset_management;
	private BigDecimal	r51_retail_brokerage;
	private String	r52_business_line_month;
	private BigDecimal	r52_corporate_finance;
	private BigDecimal	r52_trading_and_sales;
	private BigDecimal	r52_retail_banking;
	private BigDecimal	r52_commercial_banking;
	private BigDecimal	r52_payments_and_settlements;
	private BigDecimal	r52_agency_services;
	private BigDecimal	r52_asset_management;
	private BigDecimal	r52_retail_brokerage;
	private String	r53_business_line_month;
	private BigDecimal	r53_corporate_finance;
	private BigDecimal	r53_trading_and_sales;
	private BigDecimal	r53_retail_banking;
	private BigDecimal	r53_commercial_banking;
	private BigDecimal	r53_payments_and_settlements;
	private BigDecimal	r53_agency_services;
	private BigDecimal	r53_asset_management;
	private BigDecimal	r53_retail_brokerage;
	private String	r54_business_line_month;
	private BigDecimal	r54_corporate_finance;
	private BigDecimal	r54_trading_and_sales;
	private BigDecimal	r54_retail_banking;
	private BigDecimal	r54_commercial_banking;
	private BigDecimal	r54_payments_and_settlements;
	private BigDecimal	r54_agency_services;
	private BigDecimal	r54_asset_management;
	private BigDecimal	r54_retail_brokerage;
	private String	r55_business_line_month;
	private BigDecimal	r55_corporate_finance;
	private BigDecimal	r55_trading_and_sales;
	private BigDecimal	r55_retail_banking;
	private BigDecimal	r55_commercial_banking;
	private BigDecimal	r55_payments_and_settlements;
	private BigDecimal	r55_agency_services;
	private BigDecimal	r55_asset_management;
	private BigDecimal	r55_retail_brokerage;
	private String	r56_product;
	private BigDecimal	r56_corporate_finance;
	private BigDecimal	r56_trading_and_sales;
	private BigDecimal	r56_retail_banking;
	private BigDecimal	r56_commercial_banking;
	private BigDecimal	r56_payments_and_settlements;
	private BigDecimal	r56_agency_services;
	private BigDecimal	r56_asset_management;
	private BigDecimal	r56_retail_brokerage;
	private String	r57_product;
	private BigDecimal	r57_corporate_finance;
	private BigDecimal	r57_trading_and_sales;
	private BigDecimal	r57_retail_banking;
	private BigDecimal	r57_commercial_banking;
	private BigDecimal	r57_payments_and_settlements;
	private BigDecimal	r57_agency_services;
	private BigDecimal	r57_asset_management;
	private BigDecimal	r57_retail_brokerage;
	private String	r58_product;
	private BigDecimal	r58_corporate_finance;
	private BigDecimal	r58_trading_and_sales;
	private BigDecimal	r58_retail_banking;
	private BigDecimal	r58_commercial_banking;
	private BigDecimal	r58_payments_and_settlements;
	private BigDecimal	r58_agency_services;
	private BigDecimal	r58_asset_management;
	private BigDecimal	r58_retail_brokerage;
	private String	r59_product;
	private BigDecimal	r59_operational_risk_cap_charge;
	private String	r61_product;
	private BigDecimal	r61_operational_risk_cap_charge;
	private String	r62_product;
	private BigDecimal	r62_operational_risk_cap_charge;
	private String	r63_product;
	private BigDecimal	r63_operational_risk_cap_charge;
	private String	r64_product;
	private BigDecimal	r64_risk_weight_factor;
	private String	r65_product;
	private BigDecimal	r65_risk_weight_factor;
	
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
	
	public String report_frequency;
	public String report_code;
	public String report_desc;
	public String entity_flg;
	public String modify_flg;
	public String del_flg;
	public String getR12_product() {
		return r12_product;
	}
	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}
	public String getR12_business_line_month() {
		return r12_business_line_month;
	}
	public void setR12_business_line_month(String r12_business_line_month) {
		this.r12_business_line_month = r12_business_line_month;
	}
	public BigDecimal getR12_corporate_finance() {
		return r12_corporate_finance;
	}
	public void setR12_corporate_finance(BigDecimal r12_corporate_finance) {
		this.r12_corporate_finance = r12_corporate_finance;
	}
	public BigDecimal getR12_trading_and_sales() {
		return r12_trading_and_sales;
	}
	public void setR12_trading_and_sales(BigDecimal r12_trading_and_sales) {
		this.r12_trading_and_sales = r12_trading_and_sales;
	}
	public BigDecimal getR12_retail_banking() {
		return r12_retail_banking;
	}
	public void setR12_retail_banking(BigDecimal r12_retail_banking) {
		this.r12_retail_banking = r12_retail_banking;
	}
	public BigDecimal getR12_commercial_banking() {
		return r12_commercial_banking;
	}
	public void setR12_commercial_banking(BigDecimal r12_commercial_banking) {
		this.r12_commercial_banking = r12_commercial_banking;
	}
	public BigDecimal getR12_payments_and_settlements() {
		return r12_payments_and_settlements;
	}
	public void setR12_payments_and_settlements(BigDecimal r12_payments_and_settlements) {
		this.r12_payments_and_settlements = r12_payments_and_settlements;
	}
	public BigDecimal getR12_agency_services() {
		return r12_agency_services;
	}
	public void setR12_agency_services(BigDecimal r12_agency_services) {
		this.r12_agency_services = r12_agency_services;
	}
	public BigDecimal getR12_asset_management() {
		return r12_asset_management;
	}
	public void setR12_asset_management(BigDecimal r12_asset_management) {
		this.r12_asset_management = r12_asset_management;
	}
	public BigDecimal getR12_retail_brokerage() {
		return r12_retail_brokerage;
	}
	public void setR12_retail_brokerage(BigDecimal r12_retail_brokerage) {
		this.r12_retail_brokerage = r12_retail_brokerage;
	}
	public String getR13_business_line_month() {
		return r13_business_line_month;
	}
	public void setR13_business_line_month(String r13_business_line_month) {
		this.r13_business_line_month = r13_business_line_month;
	}
	public BigDecimal getR13_corporate_finance() {
		return r13_corporate_finance;
	}
	public void setR13_corporate_finance(BigDecimal r13_corporate_finance) {
		this.r13_corporate_finance = r13_corporate_finance;
	}
	public BigDecimal getR13_trading_and_sales() {
		return r13_trading_and_sales;
	}
	public void setR13_trading_and_sales(BigDecimal r13_trading_and_sales) {
		this.r13_trading_and_sales = r13_trading_and_sales;
	}
	public BigDecimal getR13_retail_banking() {
		return r13_retail_banking;
	}
	public void setR13_retail_banking(BigDecimal r13_retail_banking) {
		this.r13_retail_banking = r13_retail_banking;
	}
	public BigDecimal getR13_commercial_banking() {
		return r13_commercial_banking;
	}
	public void setR13_commercial_banking(BigDecimal r13_commercial_banking) {
		this.r13_commercial_banking = r13_commercial_banking;
	}
	public BigDecimal getR13_payments_and_settlements() {
		return r13_payments_and_settlements;
	}
	public void setR13_payments_and_settlements(BigDecimal r13_payments_and_settlements) {
		this.r13_payments_and_settlements = r13_payments_and_settlements;
	}
	public BigDecimal getR13_agency_services() {
		return r13_agency_services;
	}
	public void setR13_agency_services(BigDecimal r13_agency_services) {
		this.r13_agency_services = r13_agency_services;
	}
	public BigDecimal getR13_asset_management() {
		return r13_asset_management;
	}
	public void setR13_asset_management(BigDecimal r13_asset_management) {
		this.r13_asset_management = r13_asset_management;
	}
	public BigDecimal getR13_retail_brokerage() {
		return r13_retail_brokerage;
	}
	public void setR13_retail_brokerage(BigDecimal r13_retail_brokerage) {
		this.r13_retail_brokerage = r13_retail_brokerage;
	}
	public String getR14_business_line_month() {
		return r14_business_line_month;
	}
	public void setR14_business_line_month(String r14_business_line_month) {
		this.r14_business_line_month = r14_business_line_month;
	}
	public BigDecimal getR14_corporate_finance() {
		return r14_corporate_finance;
	}
	public void setR14_corporate_finance(BigDecimal r14_corporate_finance) {
		this.r14_corporate_finance = r14_corporate_finance;
	}
	public BigDecimal getR14_trading_and_sales() {
		return r14_trading_and_sales;
	}
	public void setR14_trading_and_sales(BigDecimal r14_trading_and_sales) {
		this.r14_trading_and_sales = r14_trading_and_sales;
	}
	public BigDecimal getR14_retail_banking() {
		return r14_retail_banking;
	}
	public void setR14_retail_banking(BigDecimal r14_retail_banking) {
		this.r14_retail_banking = r14_retail_banking;
	}
	public BigDecimal getR14_commercial_banking() {
		return r14_commercial_banking;
	}
	public void setR14_commercial_banking(BigDecimal r14_commercial_banking) {
		this.r14_commercial_banking = r14_commercial_banking;
	}
	public BigDecimal getR14_payments_and_settlements() {
		return r14_payments_and_settlements;
	}
	public void setR14_payments_and_settlements(BigDecimal r14_payments_and_settlements) {
		this.r14_payments_and_settlements = r14_payments_and_settlements;
	}
	public BigDecimal getR14_agency_services() {
		return r14_agency_services;
	}
	public void setR14_agency_services(BigDecimal r14_agency_services) {
		this.r14_agency_services = r14_agency_services;
	}
	public BigDecimal getR14_asset_management() {
		return r14_asset_management;
	}
	public void setR14_asset_management(BigDecimal r14_asset_management) {
		this.r14_asset_management = r14_asset_management;
	}
	public BigDecimal getR14_retail_brokerage() {
		return r14_retail_brokerage;
	}
	public void setR14_retail_brokerage(BigDecimal r14_retail_brokerage) {
		this.r14_retail_brokerage = r14_retail_brokerage;
	}
	public String getR15_business_line_month() {
		return r15_business_line_month;
	}
	public void setR15_business_line_month(String r15_business_line_month) {
		this.r15_business_line_month = r15_business_line_month;
	}
	public BigDecimal getR15_corporate_finance() {
		return r15_corporate_finance;
	}
	public void setR15_corporate_finance(BigDecimal r15_corporate_finance) {
		this.r15_corporate_finance = r15_corporate_finance;
	}
	public BigDecimal getR15_trading_and_sales() {
		return r15_trading_and_sales;
	}
	public void setR15_trading_and_sales(BigDecimal r15_trading_and_sales) {
		this.r15_trading_and_sales = r15_trading_and_sales;
	}
	public BigDecimal getR15_retail_banking() {
		return r15_retail_banking;
	}
	public void setR15_retail_banking(BigDecimal r15_retail_banking) {
		this.r15_retail_banking = r15_retail_banking;
	}
	public BigDecimal getR15_commercial_banking() {
		return r15_commercial_banking;
	}
	public void setR15_commercial_banking(BigDecimal r15_commercial_banking) {
		this.r15_commercial_banking = r15_commercial_banking;
	}
	public BigDecimal getR15_payments_and_settlements() {
		return r15_payments_and_settlements;
	}
	public void setR15_payments_and_settlements(BigDecimal r15_payments_and_settlements) {
		this.r15_payments_and_settlements = r15_payments_and_settlements;
	}
	public BigDecimal getR15_agency_services() {
		return r15_agency_services;
	}
	public void setR15_agency_services(BigDecimal r15_agency_services) {
		this.r15_agency_services = r15_agency_services;
	}
	public BigDecimal getR15_asset_management() {
		return r15_asset_management;
	}
	public void setR15_asset_management(BigDecimal r15_asset_management) {
		this.r15_asset_management = r15_asset_management;
	}
	public BigDecimal getR15_retail_brokerage() {
		return r15_retail_brokerage;
	}
	public void setR15_retail_brokerage(BigDecimal r15_retail_brokerage) {
		this.r15_retail_brokerage = r15_retail_brokerage;
	}
	public String getR16_business_line_month() {
		return r16_business_line_month;
	}
	public void setR16_business_line_month(String r16_business_line_month) {
		this.r16_business_line_month = r16_business_line_month;
	}
	public BigDecimal getR16_corporate_finance() {
		return r16_corporate_finance;
	}
	public void setR16_corporate_finance(BigDecimal r16_corporate_finance) {
		this.r16_corporate_finance = r16_corporate_finance;
	}
	public BigDecimal getR16_trading_and_sales() {
		return r16_trading_and_sales;
	}
	public void setR16_trading_and_sales(BigDecimal r16_trading_and_sales) {
		this.r16_trading_and_sales = r16_trading_and_sales;
	}
	public BigDecimal getR16_retail_banking() {
		return r16_retail_banking;
	}
	public void setR16_retail_banking(BigDecimal r16_retail_banking) {
		this.r16_retail_banking = r16_retail_banking;
	}
	public BigDecimal getR16_commercial_banking() {
		return r16_commercial_banking;
	}
	public void setR16_commercial_banking(BigDecimal r16_commercial_banking) {
		this.r16_commercial_banking = r16_commercial_banking;
	}
	public BigDecimal getR16_payments_and_settlements() {
		return r16_payments_and_settlements;
	}
	public void setR16_payments_and_settlements(BigDecimal r16_payments_and_settlements) {
		this.r16_payments_and_settlements = r16_payments_and_settlements;
	}
	public BigDecimal getR16_agency_services() {
		return r16_agency_services;
	}
	public void setR16_agency_services(BigDecimal r16_agency_services) {
		this.r16_agency_services = r16_agency_services;
	}
	public BigDecimal getR16_asset_management() {
		return r16_asset_management;
	}
	public void setR16_asset_management(BigDecimal r16_asset_management) {
		this.r16_asset_management = r16_asset_management;
	}
	public BigDecimal getR16_retail_brokerage() {
		return r16_retail_brokerage;
	}
	public void setR16_retail_brokerage(BigDecimal r16_retail_brokerage) {
		this.r16_retail_brokerage = r16_retail_brokerage;
	}
	public String getR17_business_line_month() {
		return r17_business_line_month;
	}
	public void setR17_business_line_month(String r17_business_line_month) {
		this.r17_business_line_month = r17_business_line_month;
	}
	public BigDecimal getR17_corporate_finance() {
		return r17_corporate_finance;
	}
	public void setR17_corporate_finance(BigDecimal r17_corporate_finance) {
		this.r17_corporate_finance = r17_corporate_finance;
	}
	public BigDecimal getR17_trading_and_sales() {
		return r17_trading_and_sales;
	}
	public void setR17_trading_and_sales(BigDecimal r17_trading_and_sales) {
		this.r17_trading_and_sales = r17_trading_and_sales;
	}
	public BigDecimal getR17_retail_banking() {
		return r17_retail_banking;
	}
	public void setR17_retail_banking(BigDecimal r17_retail_banking) {
		this.r17_retail_banking = r17_retail_banking;
	}
	public BigDecimal getR17_commercial_banking() {
		return r17_commercial_banking;
	}
	public void setR17_commercial_banking(BigDecimal r17_commercial_banking) {
		this.r17_commercial_banking = r17_commercial_banking;
	}
	public BigDecimal getR17_payments_and_settlements() {
		return r17_payments_and_settlements;
	}
	public void setR17_payments_and_settlements(BigDecimal r17_payments_and_settlements) {
		this.r17_payments_and_settlements = r17_payments_and_settlements;
	}
	public BigDecimal getR17_agency_services() {
		return r17_agency_services;
	}
	public void setR17_agency_services(BigDecimal r17_agency_services) {
		this.r17_agency_services = r17_agency_services;
	}
	public BigDecimal getR17_asset_management() {
		return r17_asset_management;
	}
	public void setR17_asset_management(BigDecimal r17_asset_management) {
		this.r17_asset_management = r17_asset_management;
	}
	public BigDecimal getR17_retail_brokerage() {
		return r17_retail_brokerage;
	}
	public void setR17_retail_brokerage(BigDecimal r17_retail_brokerage) {
		this.r17_retail_brokerage = r17_retail_brokerage;
	}
	public String getR18_business_line_month() {
		return r18_business_line_month;
	}
	public void setR18_business_line_month(String r18_business_line_month) {
		this.r18_business_line_month = r18_business_line_month;
	}
	public BigDecimal getR18_corporate_finance() {
		return r18_corporate_finance;
	}
	public void setR18_corporate_finance(BigDecimal r18_corporate_finance) {
		this.r18_corporate_finance = r18_corporate_finance;
	}
	public BigDecimal getR18_trading_and_sales() {
		return r18_trading_and_sales;
	}
	public void setR18_trading_and_sales(BigDecimal r18_trading_and_sales) {
		this.r18_trading_and_sales = r18_trading_and_sales;
	}
	public BigDecimal getR18_retail_banking() {
		return r18_retail_banking;
	}
	public void setR18_retail_banking(BigDecimal r18_retail_banking) {
		this.r18_retail_banking = r18_retail_banking;
	}
	public BigDecimal getR18_commercial_banking() {
		return r18_commercial_banking;
	}
	public void setR18_commercial_banking(BigDecimal r18_commercial_banking) {
		this.r18_commercial_banking = r18_commercial_banking;
	}
	public BigDecimal getR18_payments_and_settlements() {
		return r18_payments_and_settlements;
	}
	public void setR18_payments_and_settlements(BigDecimal r18_payments_and_settlements) {
		this.r18_payments_and_settlements = r18_payments_and_settlements;
	}
	public BigDecimal getR18_agency_services() {
		return r18_agency_services;
	}
	public void setR18_agency_services(BigDecimal r18_agency_services) {
		this.r18_agency_services = r18_agency_services;
	}
	public BigDecimal getR18_asset_management() {
		return r18_asset_management;
	}
	public void setR18_asset_management(BigDecimal r18_asset_management) {
		this.r18_asset_management = r18_asset_management;
	}
	public BigDecimal getR18_retail_brokerage() {
		return r18_retail_brokerage;
	}
	public void setR18_retail_brokerage(BigDecimal r18_retail_brokerage) {
		this.r18_retail_brokerage = r18_retail_brokerage;
	}
	public String getR19_business_line_month() {
		return r19_business_line_month;
	}
	public void setR19_business_line_month(String r19_business_line_month) {
		this.r19_business_line_month = r19_business_line_month;
	}
	public BigDecimal getR19_corporate_finance() {
		return r19_corporate_finance;
	}
	public void setR19_corporate_finance(BigDecimal r19_corporate_finance) {
		this.r19_corporate_finance = r19_corporate_finance;
	}
	public BigDecimal getR19_trading_and_sales() {
		return r19_trading_and_sales;
	}
	public void setR19_trading_and_sales(BigDecimal r19_trading_and_sales) {
		this.r19_trading_and_sales = r19_trading_and_sales;
	}
	public BigDecimal getR19_retail_banking() {
		return r19_retail_banking;
	}
	public void setR19_retail_banking(BigDecimal r19_retail_banking) {
		this.r19_retail_banking = r19_retail_banking;
	}
	public BigDecimal getR19_commercial_banking() {
		return r19_commercial_banking;
	}
	public void setR19_commercial_banking(BigDecimal r19_commercial_banking) {
		this.r19_commercial_banking = r19_commercial_banking;
	}
	public BigDecimal getR19_payments_and_settlements() {
		return r19_payments_and_settlements;
	}
	public void setR19_payments_and_settlements(BigDecimal r19_payments_and_settlements) {
		this.r19_payments_and_settlements = r19_payments_and_settlements;
	}
	public BigDecimal getR19_agency_services() {
		return r19_agency_services;
	}
	public void setR19_agency_services(BigDecimal r19_agency_services) {
		this.r19_agency_services = r19_agency_services;
	}
	public BigDecimal getR19_asset_management() {
		return r19_asset_management;
	}
	public void setR19_asset_management(BigDecimal r19_asset_management) {
		this.r19_asset_management = r19_asset_management;
	}
	public BigDecimal getR19_retail_brokerage() {
		return r19_retail_brokerage;
	}
	public void setR19_retail_brokerage(BigDecimal r19_retail_brokerage) {
		this.r19_retail_brokerage = r19_retail_brokerage;
	}
	public String getR20_business_line_month() {
		return r20_business_line_month;
	}
	public void setR20_business_line_month(String r20_business_line_month) {
		this.r20_business_line_month = r20_business_line_month;
	}
	public BigDecimal getR20_corporate_finance() {
		return r20_corporate_finance;
	}
	public void setR20_corporate_finance(BigDecimal r20_corporate_finance) {
		this.r20_corporate_finance = r20_corporate_finance;
	}
	public BigDecimal getR20_trading_and_sales() {
		return r20_trading_and_sales;
	}
	public void setR20_trading_and_sales(BigDecimal r20_trading_and_sales) {
		this.r20_trading_and_sales = r20_trading_and_sales;
	}
	public BigDecimal getR20_retail_banking() {
		return r20_retail_banking;
	}
	public void setR20_retail_banking(BigDecimal r20_retail_banking) {
		this.r20_retail_banking = r20_retail_banking;
	}
	public BigDecimal getR20_commercial_banking() {
		return r20_commercial_banking;
	}
	public void setR20_commercial_banking(BigDecimal r20_commercial_banking) {
		this.r20_commercial_banking = r20_commercial_banking;
	}
	public BigDecimal getR20_payments_and_settlements() {
		return r20_payments_and_settlements;
	}
	public void setR20_payments_and_settlements(BigDecimal r20_payments_and_settlements) {
		this.r20_payments_and_settlements = r20_payments_and_settlements;
	}
	public BigDecimal getR20_agency_services() {
		return r20_agency_services;
	}
	public void setR20_agency_services(BigDecimal r20_agency_services) {
		this.r20_agency_services = r20_agency_services;
	}
	public BigDecimal getR20_asset_management() {
		return r20_asset_management;
	}
	public void setR20_asset_management(BigDecimal r20_asset_management) {
		this.r20_asset_management = r20_asset_management;
	}
	public BigDecimal getR20_retail_brokerage() {
		return r20_retail_brokerage;
	}
	public void setR20_retail_brokerage(BigDecimal r20_retail_brokerage) {
		this.r20_retail_brokerage = r20_retail_brokerage;
	}
	public String getR21_business_line_month() {
		return r21_business_line_month;
	}
	public void setR21_business_line_month(String r21_business_line_month) {
		this.r21_business_line_month = r21_business_line_month;
	}
	public BigDecimal getR21_corporate_finance() {
		return r21_corporate_finance;
	}
	public void setR21_corporate_finance(BigDecimal r21_corporate_finance) {
		this.r21_corporate_finance = r21_corporate_finance;
	}
	public BigDecimal getR21_trading_and_sales() {
		return r21_trading_and_sales;
	}
	public void setR21_trading_and_sales(BigDecimal r21_trading_and_sales) {
		this.r21_trading_and_sales = r21_trading_and_sales;
	}
	public BigDecimal getR21_retail_banking() {
		return r21_retail_banking;
	}
	public void setR21_retail_banking(BigDecimal r21_retail_banking) {
		this.r21_retail_banking = r21_retail_banking;
	}
	public BigDecimal getR21_commercial_banking() {
		return r21_commercial_banking;
	}
	public void setR21_commercial_banking(BigDecimal r21_commercial_banking) {
		this.r21_commercial_banking = r21_commercial_banking;
	}
	public BigDecimal getR21_payments_and_settlements() {
		return r21_payments_and_settlements;
	}
	public void setR21_payments_and_settlements(BigDecimal r21_payments_and_settlements) {
		this.r21_payments_and_settlements = r21_payments_and_settlements;
	}
	public BigDecimal getR21_agency_services() {
		return r21_agency_services;
	}
	public void setR21_agency_services(BigDecimal r21_agency_services) {
		this.r21_agency_services = r21_agency_services;
	}
	public BigDecimal getR21_asset_management() {
		return r21_asset_management;
	}
	public void setR21_asset_management(BigDecimal r21_asset_management) {
		this.r21_asset_management = r21_asset_management;
	}
	public BigDecimal getR21_retail_brokerage() {
		return r21_retail_brokerage;
	}
	public void setR21_retail_brokerage(BigDecimal r21_retail_brokerage) {
		this.r21_retail_brokerage = r21_retail_brokerage;
	}
	public String getR22_business_line_month() {
		return r22_business_line_month;
	}
	public void setR22_business_line_month(String r22_business_line_month) {
		this.r22_business_line_month = r22_business_line_month;
	}
	public BigDecimal getR22_corporate_finance() {
		return r22_corporate_finance;
	}
	public void setR22_corporate_finance(BigDecimal r22_corporate_finance) {
		this.r22_corporate_finance = r22_corporate_finance;
	}
	public BigDecimal getR22_trading_and_sales() {
		return r22_trading_and_sales;
	}
	public void setR22_trading_and_sales(BigDecimal r22_trading_and_sales) {
		this.r22_trading_and_sales = r22_trading_and_sales;
	}
	public BigDecimal getR22_retail_banking() {
		return r22_retail_banking;
	}
	public void setR22_retail_banking(BigDecimal r22_retail_banking) {
		this.r22_retail_banking = r22_retail_banking;
	}
	public BigDecimal getR22_commercial_banking() {
		return r22_commercial_banking;
	}
	public void setR22_commercial_banking(BigDecimal r22_commercial_banking) {
		this.r22_commercial_banking = r22_commercial_banking;
	}
	public BigDecimal getR22_payments_and_settlements() {
		return r22_payments_and_settlements;
	}
	public void setR22_payments_and_settlements(BigDecimal r22_payments_and_settlements) {
		this.r22_payments_and_settlements = r22_payments_and_settlements;
	}
	public BigDecimal getR22_agency_services() {
		return r22_agency_services;
	}
	public void setR22_agency_services(BigDecimal r22_agency_services) {
		this.r22_agency_services = r22_agency_services;
	}
	public BigDecimal getR22_asset_management() {
		return r22_asset_management;
	}
	public void setR22_asset_management(BigDecimal r22_asset_management) {
		this.r22_asset_management = r22_asset_management;
	}
	public BigDecimal getR22_retail_brokerage() {
		return r22_retail_brokerage;
	}
	public void setR22_retail_brokerage(BigDecimal r22_retail_brokerage) {
		this.r22_retail_brokerage = r22_retail_brokerage;
	}
	public String getR23_business_line_month() {
		return r23_business_line_month;
	}
	public void setR23_business_line_month(String r23_business_line_month) {
		this.r23_business_line_month = r23_business_line_month;
	}
	public BigDecimal getR23_corporate_finance() {
		return r23_corporate_finance;
	}
	public void setR23_corporate_finance(BigDecimal r23_corporate_finance) {
		this.r23_corporate_finance = r23_corporate_finance;
	}
	public BigDecimal getR23_trading_and_sales() {
		return r23_trading_and_sales;
	}
	public void setR23_trading_and_sales(BigDecimal r23_trading_and_sales) {
		this.r23_trading_and_sales = r23_trading_and_sales;
	}
	public BigDecimal getR23_retail_banking() {
		return r23_retail_banking;
	}
	public void setR23_retail_banking(BigDecimal r23_retail_banking) {
		this.r23_retail_banking = r23_retail_banking;
	}
	public BigDecimal getR23_commercial_banking() {
		return r23_commercial_banking;
	}
	public void setR23_commercial_banking(BigDecimal r23_commercial_banking) {
		this.r23_commercial_banking = r23_commercial_banking;
	}
	public BigDecimal getR23_payments_and_settlements() {
		return r23_payments_and_settlements;
	}
	public void setR23_payments_and_settlements(BigDecimal r23_payments_and_settlements) {
		this.r23_payments_and_settlements = r23_payments_and_settlements;
	}
	public BigDecimal getR23_agency_services() {
		return r23_agency_services;
	}
	public void setR23_agency_services(BigDecimal r23_agency_services) {
		this.r23_agency_services = r23_agency_services;
	}
	public BigDecimal getR23_asset_management() {
		return r23_asset_management;
	}
	public void setR23_asset_management(BigDecimal r23_asset_management) {
		this.r23_asset_management = r23_asset_management;
	}
	public BigDecimal getR23_retail_brokerage() {
		return r23_retail_brokerage;
	}
	public void setR23_retail_brokerage(BigDecimal r23_retail_brokerage) {
		this.r23_retail_brokerage = r23_retail_brokerage;
	}
	public String getR24_product() {
		return r24_product;
	}
	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}
	public BigDecimal getR24_corporate_finance() {
		return r24_corporate_finance;
	}
	public void setR24_corporate_finance(BigDecimal r24_corporate_finance) {
		this.r24_corporate_finance = r24_corporate_finance;
	}
	public BigDecimal getR24_trading_and_sales() {
		return r24_trading_and_sales;
	}
	public void setR24_trading_and_sales(BigDecimal r24_trading_and_sales) {
		this.r24_trading_and_sales = r24_trading_and_sales;
	}
	public BigDecimal getR24_retail_banking() {
		return r24_retail_banking;
	}
	public void setR24_retail_banking(BigDecimal r24_retail_banking) {
		this.r24_retail_banking = r24_retail_banking;
	}
	public BigDecimal getR24_commercial_banking() {
		return r24_commercial_banking;
	}
	public void setR24_commercial_banking(BigDecimal r24_commercial_banking) {
		this.r24_commercial_banking = r24_commercial_banking;
	}
	public BigDecimal getR24_payments_and_settlements() {
		return r24_payments_and_settlements;
	}
	public void setR24_payments_and_settlements(BigDecimal r24_payments_and_settlements) {
		this.r24_payments_and_settlements = r24_payments_and_settlements;
	}
	public BigDecimal getR24_agency_services() {
		return r24_agency_services;
	}
	public void setR24_agency_services(BigDecimal r24_agency_services) {
		this.r24_agency_services = r24_agency_services;
	}
	public BigDecimal getR24_asset_management() {
		return r24_asset_management;
	}
	public void setR24_asset_management(BigDecimal r24_asset_management) {
		this.r24_asset_management = r24_asset_management;
	}
	public BigDecimal getR24_retail_brokerage() {
		return r24_retail_brokerage;
	}
	public void setR24_retail_brokerage(BigDecimal r24_retail_brokerage) {
		this.r24_retail_brokerage = r24_retail_brokerage;
	}
	public String getR25_product() {
		return r25_product;
	}
	public void setR25_product(String r25_product) {
		this.r25_product = r25_product;
	}
	public BigDecimal getR25_corporate_finance() {
		return r25_corporate_finance;
	}
	public void setR25_corporate_finance(BigDecimal r25_corporate_finance) {
		this.r25_corporate_finance = r25_corporate_finance;
	}
	public BigDecimal getR25_trading_and_sales() {
		return r25_trading_and_sales;
	}
	public void setR25_trading_and_sales(BigDecimal r25_trading_and_sales) {
		this.r25_trading_and_sales = r25_trading_and_sales;
	}
	public BigDecimal getR25_retail_banking() {
		return r25_retail_banking;
	}
	public void setR25_retail_banking(BigDecimal r25_retail_banking) {
		this.r25_retail_banking = r25_retail_banking;
	}
	public BigDecimal getR25_commercial_banking() {
		return r25_commercial_banking;
	}
	public void setR25_commercial_banking(BigDecimal r25_commercial_banking) {
		this.r25_commercial_banking = r25_commercial_banking;
	}
	public BigDecimal getR25_payments_and_settlements() {
		return r25_payments_and_settlements;
	}
	public void setR25_payments_and_settlements(BigDecimal r25_payments_and_settlements) {
		this.r25_payments_and_settlements = r25_payments_and_settlements;
	}
	public BigDecimal getR25_agency_services() {
		return r25_agency_services;
	}
	public void setR25_agency_services(BigDecimal r25_agency_services) {
		this.r25_agency_services = r25_agency_services;
	}
	public BigDecimal getR25_asset_management() {
		return r25_asset_management;
	}
	public void setR25_asset_management(BigDecimal r25_asset_management) {
		this.r25_asset_management = r25_asset_management;
	}
	public BigDecimal getR25_retail_brokerage() {
		return r25_retail_brokerage;
	}
	public void setR25_retail_brokerage(BigDecimal r25_retail_brokerage) {
		this.r25_retail_brokerage = r25_retail_brokerage;
	}
	public String getR26_product() {
		return r26_product;
	}
	public void setR26_product(String r26_product) {
		this.r26_product = r26_product;
	}
	public BigDecimal getR26_corporate_finance() {
		return r26_corporate_finance;
	}
	public void setR26_corporate_finance(BigDecimal r26_corporate_finance) {
		this.r26_corporate_finance = r26_corporate_finance;
	}
	public BigDecimal getR26_trading_and_sales() {
		return r26_trading_and_sales;
	}
	public void setR26_trading_and_sales(BigDecimal r26_trading_and_sales) {
		this.r26_trading_and_sales = r26_trading_and_sales;
	}
	public BigDecimal getR26_retail_banking() {
		return r26_retail_banking;
	}
	public void setR26_retail_banking(BigDecimal r26_retail_banking) {
		this.r26_retail_banking = r26_retail_banking;
	}
	public BigDecimal getR26_commercial_banking() {
		return r26_commercial_banking;
	}
	public void setR26_commercial_banking(BigDecimal r26_commercial_banking) {
		this.r26_commercial_banking = r26_commercial_banking;
	}
	public BigDecimal getR26_payments_and_settlements() {
		return r26_payments_and_settlements;
	}
	public void setR26_payments_and_settlements(BigDecimal r26_payments_and_settlements) {
		this.r26_payments_and_settlements = r26_payments_and_settlements;
	}
	public BigDecimal getR26_agency_services() {
		return r26_agency_services;
	}
	public void setR26_agency_services(BigDecimal r26_agency_services) {
		this.r26_agency_services = r26_agency_services;
	}
	public BigDecimal getR26_asset_management() {
		return r26_asset_management;
	}
	public void setR26_asset_management(BigDecimal r26_asset_management) {
		this.r26_asset_management = r26_asset_management;
	}
	public BigDecimal getR26_retail_brokerage() {
		return r26_retail_brokerage;
	}
	public void setR26_retail_brokerage(BigDecimal r26_retail_brokerage) {
		this.r26_retail_brokerage = r26_retail_brokerage;
	}
	public String getR27_product() {
		return r27_product;
	}
	public void setR27_product(String r27_product) {
		this.r27_product = r27_product;
	}
	public BigDecimal getR27_operational_risk_cap_charge() {
		return r27_operational_risk_cap_charge;
	}
	public void setR27_operational_risk_cap_charge(BigDecimal r27_operational_risk_cap_charge) {
		this.r27_operational_risk_cap_charge = r27_operational_risk_cap_charge;
	}
	public String getR28_product() {
		return r28_product;
	}
	public void setR28_product(String r28_product) {
		this.r28_product = r28_product;
	}
	public String getR28_business_line_month() {
		return r28_business_line_month;
	}
	public void setR28_business_line_month(String r28_business_line_month) {
		this.r28_business_line_month = r28_business_line_month;
	}
	public BigDecimal getR28_corporate_finance() {
		return r28_corporate_finance;
	}
	public void setR28_corporate_finance(BigDecimal r28_corporate_finance) {
		this.r28_corporate_finance = r28_corporate_finance;
	}
	public BigDecimal getR28_trading_and_sales() {
		return r28_trading_and_sales;
	}
	public void setR28_trading_and_sales(BigDecimal r28_trading_and_sales) {
		this.r28_trading_and_sales = r28_trading_and_sales;
	}
	public BigDecimal getR28_retail_banking() {
		return r28_retail_banking;
	}
	public void setR28_retail_banking(BigDecimal r28_retail_banking) {
		this.r28_retail_banking = r28_retail_banking;
	}
	public BigDecimal getR28_commercial_banking() {
		return r28_commercial_banking;
	}
	public void setR28_commercial_banking(BigDecimal r28_commercial_banking) {
		this.r28_commercial_banking = r28_commercial_banking;
	}
	public BigDecimal getR28_payments_and_settlements() {
		return r28_payments_and_settlements;
	}
	public void setR28_payments_and_settlements(BigDecimal r28_payments_and_settlements) {
		this.r28_payments_and_settlements = r28_payments_and_settlements;
	}
	public BigDecimal getR28_agency_services() {
		return r28_agency_services;
	}
	public void setR28_agency_services(BigDecimal r28_agency_services) {
		this.r28_agency_services = r28_agency_services;
	}
	public BigDecimal getR28_asset_management() {
		return r28_asset_management;
	}
	public void setR28_asset_management(BigDecimal r28_asset_management) {
		this.r28_asset_management = r28_asset_management;
	}
	public BigDecimal getR28_retail_brokerage() {
		return r28_retail_brokerage;
	}
	public void setR28_retail_brokerage(BigDecimal r28_retail_brokerage) {
		this.r28_retail_brokerage = r28_retail_brokerage;
	}
	public String getR29_business_line_month() {
		return r29_business_line_month;
	}
	public void setR29_business_line_month(String r29_business_line_month) {
		this.r29_business_line_month = r29_business_line_month;
	}
	public BigDecimal getR29_corporate_finance() {
		return r29_corporate_finance;
	}
	public void setR29_corporate_finance(BigDecimal r29_corporate_finance) {
		this.r29_corporate_finance = r29_corporate_finance;
	}
	public BigDecimal getR29_trading_and_sales() {
		return r29_trading_and_sales;
	}
	public void setR29_trading_and_sales(BigDecimal r29_trading_and_sales) {
		this.r29_trading_and_sales = r29_trading_and_sales;
	}
	public BigDecimal getR29_retail_banking() {
		return r29_retail_banking;
	}
	public void setR29_retail_banking(BigDecimal r29_retail_banking) {
		this.r29_retail_banking = r29_retail_banking;
	}
	public BigDecimal getR29_commercial_banking() {
		return r29_commercial_banking;
	}
	public void setR29_commercial_banking(BigDecimal r29_commercial_banking) {
		this.r29_commercial_banking = r29_commercial_banking;
	}
	public BigDecimal getR29_payments_and_settlements() {
		return r29_payments_and_settlements;
	}
	public void setR29_payments_and_settlements(BigDecimal r29_payments_and_settlements) {
		this.r29_payments_and_settlements = r29_payments_and_settlements;
	}
	public BigDecimal getR29_agency_services() {
		return r29_agency_services;
	}
	public void setR29_agency_services(BigDecimal r29_agency_services) {
		this.r29_agency_services = r29_agency_services;
	}
	public BigDecimal getR29_asset_management() {
		return r29_asset_management;
	}
	public void setR29_asset_management(BigDecimal r29_asset_management) {
		this.r29_asset_management = r29_asset_management;
	}
	public BigDecimal getR29_retail_brokerage() {
		return r29_retail_brokerage;
	}
	public void setR29_retail_brokerage(BigDecimal r29_retail_brokerage) {
		this.r29_retail_brokerage = r29_retail_brokerage;
	}
	public String getR30_business_line_month() {
		return r30_business_line_month;
	}
	public void setR30_business_line_month(String r30_business_line_month) {
		this.r30_business_line_month = r30_business_line_month;
	}
	public BigDecimal getR30_corporate_finance() {
		return r30_corporate_finance;
	}
	public void setR30_corporate_finance(BigDecimal r30_corporate_finance) {
		this.r30_corporate_finance = r30_corporate_finance;
	}
	public BigDecimal getR30_trading_and_sales() {
		return r30_trading_and_sales;
	}
	public void setR30_trading_and_sales(BigDecimal r30_trading_and_sales) {
		this.r30_trading_and_sales = r30_trading_and_sales;
	}
	public BigDecimal getR30_retail_banking() {
		return r30_retail_banking;
	}
	public void setR30_retail_banking(BigDecimal r30_retail_banking) {
		this.r30_retail_banking = r30_retail_banking;
	}
	public BigDecimal getR30_commercial_banking() {
		return r30_commercial_banking;
	}
	public void setR30_commercial_banking(BigDecimal r30_commercial_banking) {
		this.r30_commercial_banking = r30_commercial_banking;
	}
	public BigDecimal getR30_payments_and_settlements() {
		return r30_payments_and_settlements;
	}
	public void setR30_payments_and_settlements(BigDecimal r30_payments_and_settlements) {
		this.r30_payments_and_settlements = r30_payments_and_settlements;
	}
	public BigDecimal getR30_agency_services() {
		return r30_agency_services;
	}
	public void setR30_agency_services(BigDecimal r30_agency_services) {
		this.r30_agency_services = r30_agency_services;
	}
	public BigDecimal getR30_asset_management() {
		return r30_asset_management;
	}
	public void setR30_asset_management(BigDecimal r30_asset_management) {
		this.r30_asset_management = r30_asset_management;
	}
	public BigDecimal getR30_retail_brokerage() {
		return r30_retail_brokerage;
	}
	public void setR30_retail_brokerage(BigDecimal r30_retail_brokerage) {
		this.r30_retail_brokerage = r30_retail_brokerage;
	}
	public String getR31_business_line_month() {
		return r31_business_line_month;
	}
	public void setR31_business_line_month(String r31_business_line_month) {
		this.r31_business_line_month = r31_business_line_month;
	}
	public BigDecimal getR31_corporate_finance() {
		return r31_corporate_finance;
	}
	public void setR31_corporate_finance(BigDecimal r31_corporate_finance) {
		this.r31_corporate_finance = r31_corporate_finance;
	}
	public BigDecimal getR31_trading_and_sales() {
		return r31_trading_and_sales;
	}
	public void setR31_trading_and_sales(BigDecimal r31_trading_and_sales) {
		this.r31_trading_and_sales = r31_trading_and_sales;
	}
	public BigDecimal getR31_retail_banking() {
		return r31_retail_banking;
	}
	public void setR31_retail_banking(BigDecimal r31_retail_banking) {
		this.r31_retail_banking = r31_retail_banking;
	}
	public BigDecimal getR31_commercial_banking() {
		return r31_commercial_banking;
	}
	public void setR31_commercial_banking(BigDecimal r31_commercial_banking) {
		this.r31_commercial_banking = r31_commercial_banking;
	}
	public BigDecimal getR31_payments_and_settlements() {
		return r31_payments_and_settlements;
	}
	public void setR31_payments_and_settlements(BigDecimal r31_payments_and_settlements) {
		this.r31_payments_and_settlements = r31_payments_and_settlements;
	}
	public BigDecimal getR31_agency_services() {
		return r31_agency_services;
	}
	public void setR31_agency_services(BigDecimal r31_agency_services) {
		this.r31_agency_services = r31_agency_services;
	}
	public BigDecimal getR31_asset_management() {
		return r31_asset_management;
	}
	public void setR31_asset_management(BigDecimal r31_asset_management) {
		this.r31_asset_management = r31_asset_management;
	}
	public BigDecimal getR31_retail_brokerage() {
		return r31_retail_brokerage;
	}
	public void setR31_retail_brokerage(BigDecimal r31_retail_brokerage) {
		this.r31_retail_brokerage = r31_retail_brokerage;
	}
	public String getR32_business_line_month() {
		return r32_business_line_month;
	}
	public void setR32_business_line_month(String r32_business_line_month) {
		this.r32_business_line_month = r32_business_line_month;
	}
	public BigDecimal getR32_corporate_finance() {
		return r32_corporate_finance;
	}
	public void setR32_corporate_finance(BigDecimal r32_corporate_finance) {
		this.r32_corporate_finance = r32_corporate_finance;
	}
	public BigDecimal getR32_trading_and_sales() {
		return r32_trading_and_sales;
	}
	public void setR32_trading_and_sales(BigDecimal r32_trading_and_sales) {
		this.r32_trading_and_sales = r32_trading_and_sales;
	}
	public BigDecimal getR32_retail_banking() {
		return r32_retail_banking;
	}
	public void setR32_retail_banking(BigDecimal r32_retail_banking) {
		this.r32_retail_banking = r32_retail_banking;
	}
	public BigDecimal getR32_commercial_banking() {
		return r32_commercial_banking;
	}
	public void setR32_commercial_banking(BigDecimal r32_commercial_banking) {
		this.r32_commercial_banking = r32_commercial_banking;
	}
	public BigDecimal getR32_payments_and_settlements() {
		return r32_payments_and_settlements;
	}
	public void setR32_payments_and_settlements(BigDecimal r32_payments_and_settlements) {
		this.r32_payments_and_settlements = r32_payments_and_settlements;
	}
	public BigDecimal getR32_agency_services() {
		return r32_agency_services;
	}
	public void setR32_agency_services(BigDecimal r32_agency_services) {
		this.r32_agency_services = r32_agency_services;
	}
	public BigDecimal getR32_asset_management() {
		return r32_asset_management;
	}
	public void setR32_asset_management(BigDecimal r32_asset_management) {
		this.r32_asset_management = r32_asset_management;
	}
	public BigDecimal getR32_retail_brokerage() {
		return r32_retail_brokerage;
	}
	public void setR32_retail_brokerage(BigDecimal r32_retail_brokerage) {
		this.r32_retail_brokerage = r32_retail_brokerage;
	}
	public String getR33_business_line_month() {
		return r33_business_line_month;
	}
	public void setR33_business_line_month(String r33_business_line_month) {
		this.r33_business_line_month = r33_business_line_month;
	}
	public BigDecimal getR33_corporate_finance() {
		return r33_corporate_finance;
	}
	public void setR33_corporate_finance(BigDecimal r33_corporate_finance) {
		this.r33_corporate_finance = r33_corporate_finance;
	}
	public BigDecimal getR33_trading_and_sales() {
		return r33_trading_and_sales;
	}
	public void setR33_trading_and_sales(BigDecimal r33_trading_and_sales) {
		this.r33_trading_and_sales = r33_trading_and_sales;
	}
	public BigDecimal getR33_retail_banking() {
		return r33_retail_banking;
	}
	public void setR33_retail_banking(BigDecimal r33_retail_banking) {
		this.r33_retail_banking = r33_retail_banking;
	}
	public BigDecimal getR33_commercial_banking() {
		return r33_commercial_banking;
	}
	public void setR33_commercial_banking(BigDecimal r33_commercial_banking) {
		this.r33_commercial_banking = r33_commercial_banking;
	}
	public BigDecimal getR33_payments_and_settlements() {
		return r33_payments_and_settlements;
	}
	public void setR33_payments_and_settlements(BigDecimal r33_payments_and_settlements) {
		this.r33_payments_and_settlements = r33_payments_and_settlements;
	}
	public BigDecimal getR33_agency_services() {
		return r33_agency_services;
	}
	public void setR33_agency_services(BigDecimal r33_agency_services) {
		this.r33_agency_services = r33_agency_services;
	}
	public BigDecimal getR33_asset_management() {
		return r33_asset_management;
	}
	public void setR33_asset_management(BigDecimal r33_asset_management) {
		this.r33_asset_management = r33_asset_management;
	}
	public BigDecimal getR33_retail_brokerage() {
		return r33_retail_brokerage;
	}
	public void setR33_retail_brokerage(BigDecimal r33_retail_brokerage) {
		this.r33_retail_brokerage = r33_retail_brokerage;
	}
	public String getR34_business_line_month() {
		return r34_business_line_month;
	}
	public void setR34_business_line_month(String r34_business_line_month) {
		this.r34_business_line_month = r34_business_line_month;
	}
	public BigDecimal getR34_corporate_finance() {
		return r34_corporate_finance;
	}
	public void setR34_corporate_finance(BigDecimal r34_corporate_finance) {
		this.r34_corporate_finance = r34_corporate_finance;
	}
	public BigDecimal getR34_trading_and_sales() {
		return r34_trading_and_sales;
	}
	public void setR34_trading_and_sales(BigDecimal r34_trading_and_sales) {
		this.r34_trading_and_sales = r34_trading_and_sales;
	}
	public BigDecimal getR34_retail_banking() {
		return r34_retail_banking;
	}
	public void setR34_retail_banking(BigDecimal r34_retail_banking) {
		this.r34_retail_banking = r34_retail_banking;
	}
	public BigDecimal getR34_commercial_banking() {
		return r34_commercial_banking;
	}
	public void setR34_commercial_banking(BigDecimal r34_commercial_banking) {
		this.r34_commercial_banking = r34_commercial_banking;
	}
	public BigDecimal getR34_payments_and_settlements() {
		return r34_payments_and_settlements;
	}
	public void setR34_payments_and_settlements(BigDecimal r34_payments_and_settlements) {
		this.r34_payments_and_settlements = r34_payments_and_settlements;
	}
	public BigDecimal getR34_agency_services() {
		return r34_agency_services;
	}
	public void setR34_agency_services(BigDecimal r34_agency_services) {
		this.r34_agency_services = r34_agency_services;
	}
	public BigDecimal getR34_asset_management() {
		return r34_asset_management;
	}
	public void setR34_asset_management(BigDecimal r34_asset_management) {
		this.r34_asset_management = r34_asset_management;
	}
	public BigDecimal getR34_retail_brokerage() {
		return r34_retail_brokerage;
	}
	public void setR34_retail_brokerage(BigDecimal r34_retail_brokerage) {
		this.r34_retail_brokerage = r34_retail_brokerage;
	}
	public String getR35_business_line_month() {
		return r35_business_line_month;
	}
	public void setR35_business_line_month(String r35_business_line_month) {
		this.r35_business_line_month = r35_business_line_month;
	}
	public BigDecimal getR35_corporate_finance() {
		return r35_corporate_finance;
	}
	public void setR35_corporate_finance(BigDecimal r35_corporate_finance) {
		this.r35_corporate_finance = r35_corporate_finance;
	}
	public BigDecimal getR35_trading_and_sales() {
		return r35_trading_and_sales;
	}
	public void setR35_trading_and_sales(BigDecimal r35_trading_and_sales) {
		this.r35_trading_and_sales = r35_trading_and_sales;
	}
	public BigDecimal getR35_retail_banking() {
		return r35_retail_banking;
	}
	public void setR35_retail_banking(BigDecimal r35_retail_banking) {
		this.r35_retail_banking = r35_retail_banking;
	}
	public BigDecimal getR35_commercial_banking() {
		return r35_commercial_banking;
	}
	public void setR35_commercial_banking(BigDecimal r35_commercial_banking) {
		this.r35_commercial_banking = r35_commercial_banking;
	}
	public BigDecimal getR35_payments_and_settlements() {
		return r35_payments_and_settlements;
	}
	public void setR35_payments_and_settlements(BigDecimal r35_payments_and_settlements) {
		this.r35_payments_and_settlements = r35_payments_and_settlements;
	}
	public BigDecimal getR35_agency_services() {
		return r35_agency_services;
	}
	public void setR35_agency_services(BigDecimal r35_agency_services) {
		this.r35_agency_services = r35_agency_services;
	}
	public BigDecimal getR35_asset_management() {
		return r35_asset_management;
	}
	public void setR35_asset_management(BigDecimal r35_asset_management) {
		this.r35_asset_management = r35_asset_management;
	}
	public BigDecimal getR35_retail_brokerage() {
		return r35_retail_brokerage;
	}
	public void setR35_retail_brokerage(BigDecimal r35_retail_brokerage) {
		this.r35_retail_brokerage = r35_retail_brokerage;
	}
	public String getR36_business_line_month() {
		return r36_business_line_month;
	}
	public void setR36_business_line_month(String r36_business_line_month) {
		this.r36_business_line_month = r36_business_line_month;
	}
	public BigDecimal getR36_corporate_finance() {
		return r36_corporate_finance;
	}
	public void setR36_corporate_finance(BigDecimal r36_corporate_finance) {
		this.r36_corporate_finance = r36_corporate_finance;
	}
	public BigDecimal getR36_trading_and_sales() {
		return r36_trading_and_sales;
	}
	public void setR36_trading_and_sales(BigDecimal r36_trading_and_sales) {
		this.r36_trading_and_sales = r36_trading_and_sales;
	}
	public BigDecimal getR36_retail_banking() {
		return r36_retail_banking;
	}
	public void setR36_retail_banking(BigDecimal r36_retail_banking) {
		this.r36_retail_banking = r36_retail_banking;
	}
	public BigDecimal getR36_commercial_banking() {
		return r36_commercial_banking;
	}
	public void setR36_commercial_banking(BigDecimal r36_commercial_banking) {
		this.r36_commercial_banking = r36_commercial_banking;
	}
	public BigDecimal getR36_payments_and_settlements() {
		return r36_payments_and_settlements;
	}
	public void setR36_payments_and_settlements(BigDecimal r36_payments_and_settlements) {
		this.r36_payments_and_settlements = r36_payments_and_settlements;
	}
	public BigDecimal getR36_agency_services() {
		return r36_agency_services;
	}
	public void setR36_agency_services(BigDecimal r36_agency_services) {
		this.r36_agency_services = r36_agency_services;
	}
	public BigDecimal getR36_asset_management() {
		return r36_asset_management;
	}
	public void setR36_asset_management(BigDecimal r36_asset_management) {
		this.r36_asset_management = r36_asset_management;
	}
	public BigDecimal getR36_retail_brokerage() {
		return r36_retail_brokerage;
	}
	public void setR36_retail_brokerage(BigDecimal r36_retail_brokerage) {
		this.r36_retail_brokerage = r36_retail_brokerage;
	}
	public String getR37_business_line_month() {
		return r37_business_line_month;
	}
	public void setR37_business_line_month(String r37_business_line_month) {
		this.r37_business_line_month = r37_business_line_month;
	}
	public BigDecimal getR37_corporate_finance() {
		return r37_corporate_finance;
	}
	public void setR37_corporate_finance(BigDecimal r37_corporate_finance) {
		this.r37_corporate_finance = r37_corporate_finance;
	}
	public BigDecimal getR37_trading_and_sales() {
		return r37_trading_and_sales;
	}
	public void setR37_trading_and_sales(BigDecimal r37_trading_and_sales) {
		this.r37_trading_and_sales = r37_trading_and_sales;
	}
	public BigDecimal getR37_retail_banking() {
		return r37_retail_banking;
	}
	public void setR37_retail_banking(BigDecimal r37_retail_banking) {
		this.r37_retail_banking = r37_retail_banking;
	}
	public BigDecimal getR37_commercial_banking() {
		return r37_commercial_banking;
	}
	public void setR37_commercial_banking(BigDecimal r37_commercial_banking) {
		this.r37_commercial_banking = r37_commercial_banking;
	}
	public BigDecimal getR37_payments_and_settlements() {
		return r37_payments_and_settlements;
	}
	public void setR37_payments_and_settlements(BigDecimal r37_payments_and_settlements) {
		this.r37_payments_and_settlements = r37_payments_and_settlements;
	}
	public BigDecimal getR37_agency_services() {
		return r37_agency_services;
	}
	public void setR37_agency_services(BigDecimal r37_agency_services) {
		this.r37_agency_services = r37_agency_services;
	}
	public BigDecimal getR37_asset_management() {
		return r37_asset_management;
	}
	public void setR37_asset_management(BigDecimal r37_asset_management) {
		this.r37_asset_management = r37_asset_management;
	}
	public BigDecimal getR37_retail_brokerage() {
		return r37_retail_brokerage;
	}
	public void setR37_retail_brokerage(BigDecimal r37_retail_brokerage) {
		this.r37_retail_brokerage = r37_retail_brokerage;
	}
	public String getR38_business_line_month() {
		return r38_business_line_month;
	}
	public void setR38_business_line_month(String r38_business_line_month) {
		this.r38_business_line_month = r38_business_line_month;
	}
	public BigDecimal getR38_corporate_finance() {
		return r38_corporate_finance;
	}
	public void setR38_corporate_finance(BigDecimal r38_corporate_finance) {
		this.r38_corporate_finance = r38_corporate_finance;
	}
	public BigDecimal getR38_trading_and_sales() {
		return r38_trading_and_sales;
	}
	public void setR38_trading_and_sales(BigDecimal r38_trading_and_sales) {
		this.r38_trading_and_sales = r38_trading_and_sales;
	}
	public BigDecimal getR38_retail_banking() {
		return r38_retail_banking;
	}
	public void setR38_retail_banking(BigDecimal r38_retail_banking) {
		this.r38_retail_banking = r38_retail_banking;
	}
	public BigDecimal getR38_commercial_banking() {
		return r38_commercial_banking;
	}
	public void setR38_commercial_banking(BigDecimal r38_commercial_banking) {
		this.r38_commercial_banking = r38_commercial_banking;
	}
	public BigDecimal getR38_payments_and_settlements() {
		return r38_payments_and_settlements;
	}
	public void setR38_payments_and_settlements(BigDecimal r38_payments_and_settlements) {
		this.r38_payments_and_settlements = r38_payments_and_settlements;
	}
	public BigDecimal getR38_agency_services() {
		return r38_agency_services;
	}
	public void setR38_agency_services(BigDecimal r38_agency_services) {
		this.r38_agency_services = r38_agency_services;
	}
	public BigDecimal getR38_asset_management() {
		return r38_asset_management;
	}
	public void setR38_asset_management(BigDecimal r38_asset_management) {
		this.r38_asset_management = r38_asset_management;
	}
	public BigDecimal getR38_retail_brokerage() {
		return r38_retail_brokerage;
	}
	public void setR38_retail_brokerage(BigDecimal r38_retail_brokerage) {
		this.r38_retail_brokerage = r38_retail_brokerage;
	}
	public String getR39_business_line_month() {
		return r39_business_line_month;
	}
	public void setR39_business_line_month(String r39_business_line_month) {
		this.r39_business_line_month = r39_business_line_month;
	}
	public BigDecimal getR39_corporate_finance() {
		return r39_corporate_finance;
	}
	public void setR39_corporate_finance(BigDecimal r39_corporate_finance) {
		this.r39_corporate_finance = r39_corporate_finance;
	}
	public BigDecimal getR39_trading_and_sales() {
		return r39_trading_and_sales;
	}
	public void setR39_trading_and_sales(BigDecimal r39_trading_and_sales) {
		this.r39_trading_and_sales = r39_trading_and_sales;
	}
	public BigDecimal getR39_retail_banking() {
		return r39_retail_banking;
	}
	public void setR39_retail_banking(BigDecimal r39_retail_banking) {
		this.r39_retail_banking = r39_retail_banking;
	}
	public BigDecimal getR39_commercial_banking() {
		return r39_commercial_banking;
	}
	public void setR39_commercial_banking(BigDecimal r39_commercial_banking) {
		this.r39_commercial_banking = r39_commercial_banking;
	}
	public BigDecimal getR39_payments_and_settlements() {
		return r39_payments_and_settlements;
	}
	public void setR39_payments_and_settlements(BigDecimal r39_payments_and_settlements) {
		this.r39_payments_and_settlements = r39_payments_and_settlements;
	}
	public BigDecimal getR39_agency_services() {
		return r39_agency_services;
	}
	public void setR39_agency_services(BigDecimal r39_agency_services) {
		this.r39_agency_services = r39_agency_services;
	}
	public BigDecimal getR39_asset_management() {
		return r39_asset_management;
	}
	public void setR39_asset_management(BigDecimal r39_asset_management) {
		this.r39_asset_management = r39_asset_management;
	}
	public BigDecimal getR39_retail_brokerage() {
		return r39_retail_brokerage;
	}
	public void setR39_retail_brokerage(BigDecimal r39_retail_brokerage) {
		this.r39_retail_brokerage = r39_retail_brokerage;
	}
	public String getR40_product() {
		return r40_product;
	}
	public void setR40_product(String r40_product) {
		this.r40_product = r40_product;
	}
	public BigDecimal getR40_corporate_finance() {
		return r40_corporate_finance;
	}
	public void setR40_corporate_finance(BigDecimal r40_corporate_finance) {
		this.r40_corporate_finance = r40_corporate_finance;
	}
	public BigDecimal getR40_trading_and_sales() {
		return r40_trading_and_sales;
	}
	public void setR40_trading_and_sales(BigDecimal r40_trading_and_sales) {
		this.r40_trading_and_sales = r40_trading_and_sales;
	}
	public BigDecimal getR40_retail_banking() {
		return r40_retail_banking;
	}
	public void setR40_retail_banking(BigDecimal r40_retail_banking) {
		this.r40_retail_banking = r40_retail_banking;
	}
	public BigDecimal getR40_commercial_banking() {
		return r40_commercial_banking;
	}
	public void setR40_commercial_banking(BigDecimal r40_commercial_banking) {
		this.r40_commercial_banking = r40_commercial_banking;
	}
	public BigDecimal getR40_payments_and_settlements() {
		return r40_payments_and_settlements;
	}
	public void setR40_payments_and_settlements(BigDecimal r40_payments_and_settlements) {
		this.r40_payments_and_settlements = r40_payments_and_settlements;
	}
	public BigDecimal getR40_agency_services() {
		return r40_agency_services;
	}
	public void setR40_agency_services(BigDecimal r40_agency_services) {
		this.r40_agency_services = r40_agency_services;
	}
	public BigDecimal getR40_asset_management() {
		return r40_asset_management;
	}
	public void setR40_asset_management(BigDecimal r40_asset_management) {
		this.r40_asset_management = r40_asset_management;
	}
	public BigDecimal getR40_retail_brokerage() {
		return r40_retail_brokerage;
	}
	public void setR40_retail_brokerage(BigDecimal r40_retail_brokerage) {
		this.r40_retail_brokerage = r40_retail_brokerage;
	}
	public String getR41_product() {
		return r41_product;
	}
	public void setR41_product(String r41_product) {
		this.r41_product = r41_product;
	}
	public BigDecimal getR41_corporate_finance() {
		return r41_corporate_finance;
	}
	public void setR41_corporate_finance(BigDecimal r41_corporate_finance) {
		this.r41_corporate_finance = r41_corporate_finance;
	}
	public BigDecimal getR41_trading_and_sales() {
		return r41_trading_and_sales;
	}
	public void setR41_trading_and_sales(BigDecimal r41_trading_and_sales) {
		this.r41_trading_and_sales = r41_trading_and_sales;
	}
	public BigDecimal getR41_retail_banking() {
		return r41_retail_banking;
	}
	public void setR41_retail_banking(BigDecimal r41_retail_banking) {
		this.r41_retail_banking = r41_retail_banking;
	}
	public BigDecimal getR41_commercial_banking() {
		return r41_commercial_banking;
	}
	public void setR41_commercial_banking(BigDecimal r41_commercial_banking) {
		this.r41_commercial_banking = r41_commercial_banking;
	}
	public BigDecimal getR41_payments_and_settlements() {
		return r41_payments_and_settlements;
	}
	public void setR41_payments_and_settlements(BigDecimal r41_payments_and_settlements) {
		this.r41_payments_and_settlements = r41_payments_and_settlements;
	}
	public BigDecimal getR41_agency_services() {
		return r41_agency_services;
	}
	public void setR41_agency_services(BigDecimal r41_agency_services) {
		this.r41_agency_services = r41_agency_services;
	}
	public BigDecimal getR41_asset_management() {
		return r41_asset_management;
	}
	public void setR41_asset_management(BigDecimal r41_asset_management) {
		this.r41_asset_management = r41_asset_management;
	}
	public BigDecimal getR41_retail_brokerage() {
		return r41_retail_brokerage;
	}
	public void setR41_retail_brokerage(BigDecimal r41_retail_brokerage) {
		this.r41_retail_brokerage = r41_retail_brokerage;
	}
	public String getR42_product() {
		return r42_product;
	}
	public void setR42_product(String r42_product) {
		this.r42_product = r42_product;
	}
	public BigDecimal getR42_corporate_finance() {
		return r42_corporate_finance;
	}
	public void setR42_corporate_finance(BigDecimal r42_corporate_finance) {
		this.r42_corporate_finance = r42_corporate_finance;
	}
	public BigDecimal getR42_trading_and_sales() {
		return r42_trading_and_sales;
	}
	public void setR42_trading_and_sales(BigDecimal r42_trading_and_sales) {
		this.r42_trading_and_sales = r42_trading_and_sales;
	}
	public BigDecimal getR42_retail_banking() {
		return r42_retail_banking;
	}
	public void setR42_retail_banking(BigDecimal r42_retail_banking) {
		this.r42_retail_banking = r42_retail_banking;
	}
	public BigDecimal getR42_commercial_banking() {
		return r42_commercial_banking;
	}
	public void setR42_commercial_banking(BigDecimal r42_commercial_banking) {
		this.r42_commercial_banking = r42_commercial_banking;
	}
	public BigDecimal getR42_payments_and_settlements() {
		return r42_payments_and_settlements;
	}
	public void setR42_payments_and_settlements(BigDecimal r42_payments_and_settlements) {
		this.r42_payments_and_settlements = r42_payments_and_settlements;
	}
	public BigDecimal getR42_agency_services() {
		return r42_agency_services;
	}
	public void setR42_agency_services(BigDecimal r42_agency_services) {
		this.r42_agency_services = r42_agency_services;
	}
	public BigDecimal getR42_asset_management() {
		return r42_asset_management;
	}
	public void setR42_asset_management(BigDecimal r42_asset_management) {
		this.r42_asset_management = r42_asset_management;
	}
	public BigDecimal getR42_retail_brokerage() {
		return r42_retail_brokerage;
	}
	public void setR42_retail_brokerage(BigDecimal r42_retail_brokerage) {
		this.r42_retail_brokerage = r42_retail_brokerage;
	}
	public String getR43_product() {
		return r43_product;
	}
	public void setR43_product(String r43_product) {
		this.r43_product = r43_product;
	}
	public BigDecimal getR43_operational_risk_cap_charge() {
		return r43_operational_risk_cap_charge;
	}
	public void setR43_operational_risk_cap_charge(BigDecimal r43_operational_risk_cap_charge) {
		this.r43_operational_risk_cap_charge = r43_operational_risk_cap_charge;
	}
	public String getR44_product() {
		return r44_product;
	}
	public void setR44_product(String r44_product) {
		this.r44_product = r44_product;
	}
	public String getR44_business_line_month() {
		return r44_business_line_month;
	}
	public void setR44_business_line_month(String r44_business_line_month) {
		this.r44_business_line_month = r44_business_line_month;
	}
	public BigDecimal getR44_corporate_finance() {
		return r44_corporate_finance;
	}
	public void setR44_corporate_finance(BigDecimal r44_corporate_finance) {
		this.r44_corporate_finance = r44_corporate_finance;
	}
	public BigDecimal getR44_trading_and_sales() {
		return r44_trading_and_sales;
	}
	public void setR44_trading_and_sales(BigDecimal r44_trading_and_sales) {
		this.r44_trading_and_sales = r44_trading_and_sales;
	}
	public BigDecimal getR44_retail_banking() {
		return r44_retail_banking;
	}
	public void setR44_retail_banking(BigDecimal r44_retail_banking) {
		this.r44_retail_banking = r44_retail_banking;
	}
	public BigDecimal getR44_commercial_banking() {
		return r44_commercial_banking;
	}
	public void setR44_commercial_banking(BigDecimal r44_commercial_banking) {
		this.r44_commercial_banking = r44_commercial_banking;
	}
	public BigDecimal getR44_payments_and_settlements() {
		return r44_payments_and_settlements;
	}
	public void setR44_payments_and_settlements(BigDecimal r44_payments_and_settlements) {
		this.r44_payments_and_settlements = r44_payments_and_settlements;
	}
	public BigDecimal getR44_agency_services() {
		return r44_agency_services;
	}
	public void setR44_agency_services(BigDecimal r44_agency_services) {
		this.r44_agency_services = r44_agency_services;
	}
	public BigDecimal getR44_asset_management() {
		return r44_asset_management;
	}
	public void setR44_asset_management(BigDecimal r44_asset_management) {
		this.r44_asset_management = r44_asset_management;
	}
	public BigDecimal getR44_retail_brokerage() {
		return r44_retail_brokerage;
	}
	public void setR44_retail_brokerage(BigDecimal r44_retail_brokerage) {
		this.r44_retail_brokerage = r44_retail_brokerage;
	}
	public String getR45_business_line_month() {
		return r45_business_line_month;
	}
	public void setR45_business_line_month(String r45_business_line_month) {
		this.r45_business_line_month = r45_business_line_month;
	}
	public BigDecimal getR45_corporate_finance() {
		return r45_corporate_finance;
	}
	public void setR45_corporate_finance(BigDecimal r45_corporate_finance) {
		this.r45_corporate_finance = r45_corporate_finance;
	}
	public BigDecimal getR45_trading_and_sales() {
		return r45_trading_and_sales;
	}
	public void setR45_trading_and_sales(BigDecimal r45_trading_and_sales) {
		this.r45_trading_and_sales = r45_trading_and_sales;
	}
	public BigDecimal getR45_retail_banking() {
		return r45_retail_banking;
	}
	public void setR45_retail_banking(BigDecimal r45_retail_banking) {
		this.r45_retail_banking = r45_retail_banking;
	}
	public BigDecimal getR45_commercial_banking() {
		return r45_commercial_banking;
	}
	public void setR45_commercial_banking(BigDecimal r45_commercial_banking) {
		this.r45_commercial_banking = r45_commercial_banking;
	}
	public BigDecimal getR45_payments_and_settlements() {
		return r45_payments_and_settlements;
	}
	public void setR45_payments_and_settlements(BigDecimal r45_payments_and_settlements) {
		this.r45_payments_and_settlements = r45_payments_and_settlements;
	}
	public BigDecimal getR45_agency_services() {
		return r45_agency_services;
	}
	public void setR45_agency_services(BigDecimal r45_agency_services) {
		this.r45_agency_services = r45_agency_services;
	}
	public BigDecimal getR45_asset_management() {
		return r45_asset_management;
	}
	public void setR45_asset_management(BigDecimal r45_asset_management) {
		this.r45_asset_management = r45_asset_management;
	}
	public BigDecimal getR45_retail_brokerage() {
		return r45_retail_brokerage;
	}
	public void setR45_retail_brokerage(BigDecimal r45_retail_brokerage) {
		this.r45_retail_brokerage = r45_retail_brokerage;
	}
	public String getR46_business_line_month() {
		return r46_business_line_month;
	}
	public void setR46_business_line_month(String r46_business_line_month) {
		this.r46_business_line_month = r46_business_line_month;
	}
	public BigDecimal getR46_corporate_finance() {
		return r46_corporate_finance;
	}
	public void setR46_corporate_finance(BigDecimal r46_corporate_finance) {
		this.r46_corporate_finance = r46_corporate_finance;
	}
	public BigDecimal getR46_trading_and_sales() {
		return r46_trading_and_sales;
	}
	public void setR46_trading_and_sales(BigDecimal r46_trading_and_sales) {
		this.r46_trading_and_sales = r46_trading_and_sales;
	}
	public BigDecimal getR46_retail_banking() {
		return r46_retail_banking;
	}
	public void setR46_retail_banking(BigDecimal r46_retail_banking) {
		this.r46_retail_banking = r46_retail_banking;
	}
	public BigDecimal getR46_commercial_banking() {
		return r46_commercial_banking;
	}
	public void setR46_commercial_banking(BigDecimal r46_commercial_banking) {
		this.r46_commercial_banking = r46_commercial_banking;
	}
	public BigDecimal getR46_payments_and_settlements() {
		return r46_payments_and_settlements;
	}
	public void setR46_payments_and_settlements(BigDecimal r46_payments_and_settlements) {
		this.r46_payments_and_settlements = r46_payments_and_settlements;
	}
	public BigDecimal getR46_agency_services() {
		return r46_agency_services;
	}
	public void setR46_agency_services(BigDecimal r46_agency_services) {
		this.r46_agency_services = r46_agency_services;
	}
	public BigDecimal getR46_asset_management() {
		return r46_asset_management;
	}
	public void setR46_asset_management(BigDecimal r46_asset_management) {
		this.r46_asset_management = r46_asset_management;
	}
	public BigDecimal getR46_retail_brokerage() {
		return r46_retail_brokerage;
	}
	public void setR46_retail_brokerage(BigDecimal r46_retail_brokerage) {
		this.r46_retail_brokerage = r46_retail_brokerage;
	}
	public String getR47_business_line_month() {
		return r47_business_line_month;
	}
	public void setR47_business_line_month(String r47_business_line_month) {
		this.r47_business_line_month = r47_business_line_month;
	}
	public BigDecimal getR47_corporate_finance() {
		return r47_corporate_finance;
	}
	public void setR47_corporate_finance(BigDecimal r47_corporate_finance) {
		this.r47_corporate_finance = r47_corporate_finance;
	}
	public BigDecimal getR47_trading_and_sales() {
		return r47_trading_and_sales;
	}
	public void setR47_trading_and_sales(BigDecimal r47_trading_and_sales) {
		this.r47_trading_and_sales = r47_trading_and_sales;
	}
	public BigDecimal getR47_retail_banking() {
		return r47_retail_banking;
	}
	public void setR47_retail_banking(BigDecimal r47_retail_banking) {
		this.r47_retail_banking = r47_retail_banking;
	}
	public BigDecimal getR47_commercial_banking() {
		return r47_commercial_banking;
	}
	public void setR47_commercial_banking(BigDecimal r47_commercial_banking) {
		this.r47_commercial_banking = r47_commercial_banking;
	}
	public BigDecimal getR47_payments_and_settlements() {
		return r47_payments_and_settlements;
	}
	public void setR47_payments_and_settlements(BigDecimal r47_payments_and_settlements) {
		this.r47_payments_and_settlements = r47_payments_and_settlements;
	}
	public BigDecimal getR47_agency_services() {
		return r47_agency_services;
	}
	public void setR47_agency_services(BigDecimal r47_agency_services) {
		this.r47_agency_services = r47_agency_services;
	}
	public BigDecimal getR47_asset_management() {
		return r47_asset_management;
	}
	public void setR47_asset_management(BigDecimal r47_asset_management) {
		this.r47_asset_management = r47_asset_management;
	}
	public BigDecimal getR47_retail_brokerage() {
		return r47_retail_brokerage;
	}
	public void setR47_retail_brokerage(BigDecimal r47_retail_brokerage) {
		this.r47_retail_brokerage = r47_retail_brokerage;
	}
	public String getR48_business_line_month() {
		return r48_business_line_month;
	}
	public void setR48_business_line_month(String r48_business_line_month) {
		this.r48_business_line_month = r48_business_line_month;
	}
	public BigDecimal getR48_corporate_finance() {
		return r48_corporate_finance;
	}
	public void setR48_corporate_finance(BigDecimal r48_corporate_finance) {
		this.r48_corporate_finance = r48_corporate_finance;
	}
	public BigDecimal getR48_trading_and_sales() {
		return r48_trading_and_sales;
	}
	public void setR48_trading_and_sales(BigDecimal r48_trading_and_sales) {
		this.r48_trading_and_sales = r48_trading_and_sales;
	}
	public BigDecimal getR48_retail_banking() {
		return r48_retail_banking;
	}
	public void setR48_retail_banking(BigDecimal r48_retail_banking) {
		this.r48_retail_banking = r48_retail_banking;
	}
	public BigDecimal getR48_commercial_banking() {
		return r48_commercial_banking;
	}
	public void setR48_commercial_banking(BigDecimal r48_commercial_banking) {
		this.r48_commercial_banking = r48_commercial_banking;
	}
	public BigDecimal getR48_payments_and_settlements() {
		return r48_payments_and_settlements;
	}
	public void setR48_payments_and_settlements(BigDecimal r48_payments_and_settlements) {
		this.r48_payments_and_settlements = r48_payments_and_settlements;
	}
	public BigDecimal getR48_agency_services() {
		return r48_agency_services;
	}
	public void setR48_agency_services(BigDecimal r48_agency_services) {
		this.r48_agency_services = r48_agency_services;
	}
	public BigDecimal getR48_asset_management() {
		return r48_asset_management;
	}
	public void setR48_asset_management(BigDecimal r48_asset_management) {
		this.r48_asset_management = r48_asset_management;
	}
	public BigDecimal getR48_retail_brokerage() {
		return r48_retail_brokerage;
	}
	public void setR48_retail_brokerage(BigDecimal r48_retail_brokerage) {
		this.r48_retail_brokerage = r48_retail_brokerage;
	}
	public String getR49_business_line_month() {
		return r49_business_line_month;
	}
	public void setR49_business_line_month(String r49_business_line_month) {
		this.r49_business_line_month = r49_business_line_month;
	}
	public BigDecimal getR49_corporate_finance() {
		return r49_corporate_finance;
	}
	public void setR49_corporate_finance(BigDecimal r49_corporate_finance) {
		this.r49_corporate_finance = r49_corporate_finance;
	}
	public BigDecimal getR49_trading_and_sales() {
		return r49_trading_and_sales;
	}
	public void setR49_trading_and_sales(BigDecimal r49_trading_and_sales) {
		this.r49_trading_and_sales = r49_trading_and_sales;
	}
	public BigDecimal getR49_retail_banking() {
		return r49_retail_banking;
	}
	public void setR49_retail_banking(BigDecimal r49_retail_banking) {
		this.r49_retail_banking = r49_retail_banking;
	}
	public BigDecimal getR49_commercial_banking() {
		return r49_commercial_banking;
	}
	public void setR49_commercial_banking(BigDecimal r49_commercial_banking) {
		this.r49_commercial_banking = r49_commercial_banking;
	}
	public BigDecimal getR49_payments_and_settlements() {
		return r49_payments_and_settlements;
	}
	public void setR49_payments_and_settlements(BigDecimal r49_payments_and_settlements) {
		this.r49_payments_and_settlements = r49_payments_and_settlements;
	}
	public BigDecimal getR49_agency_services() {
		return r49_agency_services;
	}
	public void setR49_agency_services(BigDecimal r49_agency_services) {
		this.r49_agency_services = r49_agency_services;
	}
	public BigDecimal getR49_asset_management() {
		return r49_asset_management;
	}
	public void setR49_asset_management(BigDecimal r49_asset_management) {
		this.r49_asset_management = r49_asset_management;
	}
	public BigDecimal getR49_retail_brokerage() {
		return r49_retail_brokerage;
	}
	public void setR49_retail_brokerage(BigDecimal r49_retail_brokerage) {
		this.r49_retail_brokerage = r49_retail_brokerage;
	}
	public String getR50_business_line_month() {
		return r50_business_line_month;
	}
	public void setR50_business_line_month(String r50_business_line_month) {
		this.r50_business_line_month = r50_business_line_month;
	}
	public BigDecimal getR50_corporate_finance() {
		return r50_corporate_finance;
	}
	public void setR50_corporate_finance(BigDecimal r50_corporate_finance) {
		this.r50_corporate_finance = r50_corporate_finance;
	}
	public BigDecimal getR50_trading_and_sales() {
		return r50_trading_and_sales;
	}
	public void setR50_trading_and_sales(BigDecimal r50_trading_and_sales) {
		this.r50_trading_and_sales = r50_trading_and_sales;
	}
	public BigDecimal getR50_retail_banking() {
		return r50_retail_banking;
	}
	public void setR50_retail_banking(BigDecimal r50_retail_banking) {
		this.r50_retail_banking = r50_retail_banking;
	}
	public BigDecimal getR50_commercial_banking() {
		return r50_commercial_banking;
	}
	public void setR50_commercial_banking(BigDecimal r50_commercial_banking) {
		this.r50_commercial_banking = r50_commercial_banking;
	}
	public BigDecimal getR50_payments_and_settlements() {
		return r50_payments_and_settlements;
	}
	public void setR50_payments_and_settlements(BigDecimal r50_payments_and_settlements) {
		this.r50_payments_and_settlements = r50_payments_and_settlements;
	}
	public BigDecimal getR50_agency_services() {
		return r50_agency_services;
	}
	public void setR50_agency_services(BigDecimal r50_agency_services) {
		this.r50_agency_services = r50_agency_services;
	}
	public BigDecimal getR50_asset_management() {
		return r50_asset_management;
	}
	public void setR50_asset_management(BigDecimal r50_asset_management) {
		this.r50_asset_management = r50_asset_management;
	}
	public BigDecimal getR50_retail_brokerage() {
		return r50_retail_brokerage;
	}
	public void setR50_retail_brokerage(BigDecimal r50_retail_brokerage) {
		this.r50_retail_brokerage = r50_retail_brokerage;
	}
	public String getR51_business_line_month() {
		return r51_business_line_month;
	}
	public void setR51_business_line_month(String r51_business_line_month) {
		this.r51_business_line_month = r51_business_line_month;
	}
	public BigDecimal getR51_corporate_finance() {
		return r51_corporate_finance;
	}
	public void setR51_corporate_finance(BigDecimal r51_corporate_finance) {
		this.r51_corporate_finance = r51_corporate_finance;
	}
	public BigDecimal getR51_trading_and_sales() {
		return r51_trading_and_sales;
	}
	public void setR51_trading_and_sales(BigDecimal r51_trading_and_sales) {
		this.r51_trading_and_sales = r51_trading_and_sales;
	}
	public BigDecimal getR51_retail_banking() {
		return r51_retail_banking;
	}
	public void setR51_retail_banking(BigDecimal r51_retail_banking) {
		this.r51_retail_banking = r51_retail_banking;
	}
	public BigDecimal getR51_commercial_banking() {
		return r51_commercial_banking;
	}
	public void setR51_commercial_banking(BigDecimal r51_commercial_banking) {
		this.r51_commercial_banking = r51_commercial_banking;
	}
	public BigDecimal getR51_payments_and_settlements() {
		return r51_payments_and_settlements;
	}
	public void setR51_payments_and_settlements(BigDecimal r51_payments_and_settlements) {
		this.r51_payments_and_settlements = r51_payments_and_settlements;
	}
	public BigDecimal getR51_agency_services() {
		return r51_agency_services;
	}
	public void setR51_agency_services(BigDecimal r51_agency_services) {
		this.r51_agency_services = r51_agency_services;
	}
	public BigDecimal getR51_asset_management() {
		return r51_asset_management;
	}
	public void setR51_asset_management(BigDecimal r51_asset_management) {
		this.r51_asset_management = r51_asset_management;
	}
	public BigDecimal getR51_retail_brokerage() {
		return r51_retail_brokerage;
	}
	public void setR51_retail_brokerage(BigDecimal r51_retail_brokerage) {
		this.r51_retail_brokerage = r51_retail_brokerage;
	}
	public String getR52_business_line_month() {
		return r52_business_line_month;
	}
	public void setR52_business_line_month(String r52_business_line_month) {
		this.r52_business_line_month = r52_business_line_month;
	}
	public BigDecimal getR52_corporate_finance() {
		return r52_corporate_finance;
	}
	public void setR52_corporate_finance(BigDecimal r52_corporate_finance) {
		this.r52_corporate_finance = r52_corporate_finance;
	}
	public BigDecimal getR52_trading_and_sales() {
		return r52_trading_and_sales;
	}
	public void setR52_trading_and_sales(BigDecimal r52_trading_and_sales) {
		this.r52_trading_and_sales = r52_trading_and_sales;
	}
	public BigDecimal getR52_retail_banking() {
		return r52_retail_banking;
	}
	public void setR52_retail_banking(BigDecimal r52_retail_banking) {
		this.r52_retail_banking = r52_retail_banking;
	}
	public BigDecimal getR52_commercial_banking() {
		return r52_commercial_banking;
	}
	public void setR52_commercial_banking(BigDecimal r52_commercial_banking) {
		this.r52_commercial_banking = r52_commercial_banking;
	}
	public BigDecimal getR52_payments_and_settlements() {
		return r52_payments_and_settlements;
	}
	public void setR52_payments_and_settlements(BigDecimal r52_payments_and_settlements) {
		this.r52_payments_and_settlements = r52_payments_and_settlements;
	}
	public BigDecimal getR52_agency_services() {
		return r52_agency_services;
	}
	public void setR52_agency_services(BigDecimal r52_agency_services) {
		this.r52_agency_services = r52_agency_services;
	}
	public BigDecimal getR52_asset_management() {
		return r52_asset_management;
	}
	public void setR52_asset_management(BigDecimal r52_asset_management) {
		this.r52_asset_management = r52_asset_management;
	}
	public BigDecimal getR52_retail_brokerage() {
		return r52_retail_brokerage;
	}
	public void setR52_retail_brokerage(BigDecimal r52_retail_brokerage) {
		this.r52_retail_brokerage = r52_retail_brokerage;
	}
	public String getR53_business_line_month() {
		return r53_business_line_month;
	}
	public void setR53_business_line_month(String r53_business_line_month) {
		this.r53_business_line_month = r53_business_line_month;
	}
	public BigDecimal getR53_corporate_finance() {
		return r53_corporate_finance;
	}
	public void setR53_corporate_finance(BigDecimal r53_corporate_finance) {
		this.r53_corporate_finance = r53_corporate_finance;
	}
	public BigDecimal getR53_trading_and_sales() {
		return r53_trading_and_sales;
	}
	public void setR53_trading_and_sales(BigDecimal r53_trading_and_sales) {
		this.r53_trading_and_sales = r53_trading_and_sales;
	}
	public BigDecimal getR53_retail_banking() {
		return r53_retail_banking;
	}
	public void setR53_retail_banking(BigDecimal r53_retail_banking) {
		this.r53_retail_banking = r53_retail_banking;
	}
	public BigDecimal getR53_commercial_banking() {
		return r53_commercial_banking;
	}
	public void setR53_commercial_banking(BigDecimal r53_commercial_banking) {
		this.r53_commercial_banking = r53_commercial_banking;
	}
	public BigDecimal getR53_payments_and_settlements() {
		return r53_payments_and_settlements;
	}
	public void setR53_payments_and_settlements(BigDecimal r53_payments_and_settlements) {
		this.r53_payments_and_settlements = r53_payments_and_settlements;
	}
	public BigDecimal getR53_agency_services() {
		return r53_agency_services;
	}
	public void setR53_agency_services(BigDecimal r53_agency_services) {
		this.r53_agency_services = r53_agency_services;
	}
	public BigDecimal getR53_asset_management() {
		return r53_asset_management;
	}
	public void setR53_asset_management(BigDecimal r53_asset_management) {
		this.r53_asset_management = r53_asset_management;
	}
	public BigDecimal getR53_retail_brokerage() {
		return r53_retail_brokerage;
	}
	public void setR53_retail_brokerage(BigDecimal r53_retail_brokerage) {
		this.r53_retail_brokerage = r53_retail_brokerage;
	}
	public String getR54_business_line_month() {
		return r54_business_line_month;
	}
	public void setR54_business_line_month(String r54_business_line_month) {
		this.r54_business_line_month = r54_business_line_month;
	}
	public BigDecimal getR54_corporate_finance() {
		return r54_corporate_finance;
	}
	public void setR54_corporate_finance(BigDecimal r54_corporate_finance) {
		this.r54_corporate_finance = r54_corporate_finance;
	}
	public BigDecimal getR54_trading_and_sales() {
		return r54_trading_and_sales;
	}
	public void setR54_trading_and_sales(BigDecimal r54_trading_and_sales) {
		this.r54_trading_and_sales = r54_trading_and_sales;
	}
	public BigDecimal getR54_retail_banking() {
		return r54_retail_banking;
	}
	public void setR54_retail_banking(BigDecimal r54_retail_banking) {
		this.r54_retail_banking = r54_retail_banking;
	}
	public BigDecimal getR54_commercial_banking() {
		return r54_commercial_banking;
	}
	public void setR54_commercial_banking(BigDecimal r54_commercial_banking) {
		this.r54_commercial_banking = r54_commercial_banking;
	}
	public BigDecimal getR54_payments_and_settlements() {
		return r54_payments_and_settlements;
	}
	public void setR54_payments_and_settlements(BigDecimal r54_payments_and_settlements) {
		this.r54_payments_and_settlements = r54_payments_and_settlements;
	}
	public BigDecimal getR54_agency_services() {
		return r54_agency_services;
	}
	public void setR54_agency_services(BigDecimal r54_agency_services) {
		this.r54_agency_services = r54_agency_services;
	}
	public BigDecimal getR54_asset_management() {
		return r54_asset_management;
	}
	public void setR54_asset_management(BigDecimal r54_asset_management) {
		this.r54_asset_management = r54_asset_management;
	}
	public BigDecimal getR54_retail_brokerage() {
		return r54_retail_brokerage;
	}
	public void setR54_retail_brokerage(BigDecimal r54_retail_brokerage) {
		this.r54_retail_brokerage = r54_retail_brokerage;
	}
	public String getR55_business_line_month() {
		return r55_business_line_month;
	}
	public void setR55_business_line_month(String r55_business_line_month) {
		this.r55_business_line_month = r55_business_line_month;
	}
	public BigDecimal getR55_corporate_finance() {
		return r55_corporate_finance;
	}
	public void setR55_corporate_finance(BigDecimal r55_corporate_finance) {
		this.r55_corporate_finance = r55_corporate_finance;
	}
	public BigDecimal getR55_trading_and_sales() {
		return r55_trading_and_sales;
	}
	public void setR55_trading_and_sales(BigDecimal r55_trading_and_sales) {
		this.r55_trading_and_sales = r55_trading_and_sales;
	}
	public BigDecimal getR55_retail_banking() {
		return r55_retail_banking;
	}
	public void setR55_retail_banking(BigDecimal r55_retail_banking) {
		this.r55_retail_banking = r55_retail_banking;
	}
	public BigDecimal getR55_commercial_banking() {
		return r55_commercial_banking;
	}
	public void setR55_commercial_banking(BigDecimal r55_commercial_banking) {
		this.r55_commercial_banking = r55_commercial_banking;
	}
	public BigDecimal getR55_payments_and_settlements() {
		return r55_payments_and_settlements;
	}
	public void setR55_payments_and_settlements(BigDecimal r55_payments_and_settlements) {
		this.r55_payments_and_settlements = r55_payments_and_settlements;
	}
	public BigDecimal getR55_agency_services() {
		return r55_agency_services;
	}
	public void setR55_agency_services(BigDecimal r55_agency_services) {
		this.r55_agency_services = r55_agency_services;
	}
	public BigDecimal getR55_asset_management() {
		return r55_asset_management;
	}
	public void setR55_asset_management(BigDecimal r55_asset_management) {
		this.r55_asset_management = r55_asset_management;
	}
	public BigDecimal getR55_retail_brokerage() {
		return r55_retail_brokerage;
	}
	public void setR55_retail_brokerage(BigDecimal r55_retail_brokerage) {
		this.r55_retail_brokerage = r55_retail_brokerage;
	}
	public String getR56_product() {
		return r56_product;
	}
	public void setR56_product(String r56_product) {
		this.r56_product = r56_product;
	}
	public BigDecimal getR56_corporate_finance() {
		return r56_corporate_finance;
	}
	public void setR56_corporate_finance(BigDecimal r56_corporate_finance) {
		this.r56_corporate_finance = r56_corporate_finance;
	}
	public BigDecimal getR56_trading_and_sales() {
		return r56_trading_and_sales;
	}
	public void setR56_trading_and_sales(BigDecimal r56_trading_and_sales) {
		this.r56_trading_and_sales = r56_trading_and_sales;
	}
	public BigDecimal getR56_retail_banking() {
		return r56_retail_banking;
	}
	public void setR56_retail_banking(BigDecimal r56_retail_banking) {
		this.r56_retail_banking = r56_retail_banking;
	}
	public BigDecimal getR56_commercial_banking() {
		return r56_commercial_banking;
	}
	public void setR56_commercial_banking(BigDecimal r56_commercial_banking) {
		this.r56_commercial_banking = r56_commercial_banking;
	}
	public BigDecimal getR56_payments_and_settlements() {
		return r56_payments_and_settlements;
	}
	public void setR56_payments_and_settlements(BigDecimal r56_payments_and_settlements) {
		this.r56_payments_and_settlements = r56_payments_and_settlements;
	}
	public BigDecimal getR56_agency_services() {
		return r56_agency_services;
	}
	public void setR56_agency_services(BigDecimal r56_agency_services) {
		this.r56_agency_services = r56_agency_services;
	}
	public BigDecimal getR56_asset_management() {
		return r56_asset_management;
	}
	public void setR56_asset_management(BigDecimal r56_asset_management) {
		this.r56_asset_management = r56_asset_management;
	}
	public BigDecimal getR56_retail_brokerage() {
		return r56_retail_brokerage;
	}
	public void setR56_retail_brokerage(BigDecimal r56_retail_brokerage) {
		this.r56_retail_brokerage = r56_retail_brokerage;
	}
	public String getR57_product() {
		return r57_product;
	}
	public void setR57_product(String r57_product) {
		this.r57_product = r57_product;
	}
	public BigDecimal getR57_corporate_finance() {
		return r57_corporate_finance;
	}
	public void setR57_corporate_finance(BigDecimal r57_corporate_finance) {
		this.r57_corporate_finance = r57_corporate_finance;
	}
	public BigDecimal getR57_trading_and_sales() {
		return r57_trading_and_sales;
	}
	public void setR57_trading_and_sales(BigDecimal r57_trading_and_sales) {
		this.r57_trading_and_sales = r57_trading_and_sales;
	}
	public BigDecimal getR57_retail_banking() {
		return r57_retail_banking;
	}
	public void setR57_retail_banking(BigDecimal r57_retail_banking) {
		this.r57_retail_banking = r57_retail_banking;
	}
	public BigDecimal getR57_commercial_banking() {
		return r57_commercial_banking;
	}
	public void setR57_commercial_banking(BigDecimal r57_commercial_banking) {
		this.r57_commercial_banking = r57_commercial_banking;
	}
	public BigDecimal getR57_payments_and_settlements() {
		return r57_payments_and_settlements;
	}
	public void setR57_payments_and_settlements(BigDecimal r57_payments_and_settlements) {
		this.r57_payments_and_settlements = r57_payments_and_settlements;
	}
	public BigDecimal getR57_agency_services() {
		return r57_agency_services;
	}
	public void setR57_agency_services(BigDecimal r57_agency_services) {
		this.r57_agency_services = r57_agency_services;
	}
	public BigDecimal getR57_asset_management() {
		return r57_asset_management;
	}
	public void setR57_asset_management(BigDecimal r57_asset_management) {
		this.r57_asset_management = r57_asset_management;
	}
	public BigDecimal getR57_retail_brokerage() {
		return r57_retail_brokerage;
	}
	public void setR57_retail_brokerage(BigDecimal r57_retail_brokerage) {
		this.r57_retail_brokerage = r57_retail_brokerage;
	}
	public String getR58_product() {
		return r58_product;
	}
	public void setR58_product(String r58_product) {
		this.r58_product = r58_product;
	}
	public BigDecimal getR58_corporate_finance() {
		return r58_corporate_finance;
	}
	public void setR58_corporate_finance(BigDecimal r58_corporate_finance) {
		this.r58_corporate_finance = r58_corporate_finance;
	}
	public BigDecimal getR58_trading_and_sales() {
		return r58_trading_and_sales;
	}
	public void setR58_trading_and_sales(BigDecimal r58_trading_and_sales) {
		this.r58_trading_and_sales = r58_trading_and_sales;
	}
	public BigDecimal getR58_retail_banking() {
		return r58_retail_banking;
	}
	public void setR58_retail_banking(BigDecimal r58_retail_banking) {
		this.r58_retail_banking = r58_retail_banking;
	}
	public BigDecimal getR58_commercial_banking() {
		return r58_commercial_banking;
	}
	public void setR58_commercial_banking(BigDecimal r58_commercial_banking) {
		this.r58_commercial_banking = r58_commercial_banking;
	}
	public BigDecimal getR58_payments_and_settlements() {
		return r58_payments_and_settlements;
	}
	public void setR58_payments_and_settlements(BigDecimal r58_payments_and_settlements) {
		this.r58_payments_and_settlements = r58_payments_and_settlements;
	}
	public BigDecimal getR58_agency_services() {
		return r58_agency_services;
	}
	public void setR58_agency_services(BigDecimal r58_agency_services) {
		this.r58_agency_services = r58_agency_services;
	}
	public BigDecimal getR58_asset_management() {
		return r58_asset_management;
	}
	public void setR58_asset_management(BigDecimal r58_asset_management) {
		this.r58_asset_management = r58_asset_management;
	}
	public BigDecimal getR58_retail_brokerage() {
		return r58_retail_brokerage;
	}
	public void setR58_retail_brokerage(BigDecimal r58_retail_brokerage) {
		this.r58_retail_brokerage = r58_retail_brokerage;
	}
	public String getR59_product() {
		return r59_product;
	}
	public void setR59_product(String r59_product) {
		this.r59_product = r59_product;
	}
	public BigDecimal getR59_operational_risk_cap_charge() {
		return r59_operational_risk_cap_charge;
	}
	public void setR59_operational_risk_cap_charge(BigDecimal r59_operational_risk_cap_charge) {
		this.r59_operational_risk_cap_charge = r59_operational_risk_cap_charge;
	}
	public String getR61_product() {
		return r61_product;
	}
	public void setR61_product(String r61_product) {
		this.r61_product = r61_product;
	}
	public BigDecimal getR61_operational_risk_cap_charge() {
		return r61_operational_risk_cap_charge;
	}
	public void setR61_operational_risk_cap_charge(BigDecimal r61_operational_risk_cap_charge) {
		this.r61_operational_risk_cap_charge = r61_operational_risk_cap_charge;
	}
	public String getR62_product() {
		return r62_product;
	}
	public void setR62_product(String r62_product) {
		this.r62_product = r62_product;
	}
	public BigDecimal getR62_operational_risk_cap_charge() {
		return r62_operational_risk_cap_charge;
	}
	public void setR62_operational_risk_cap_charge(BigDecimal r62_operational_risk_cap_charge) {
		this.r62_operational_risk_cap_charge = r62_operational_risk_cap_charge;
	}
	public String getR63_product() {
		return r63_product;
	}
	public void setR63_product(String r63_product) {
		this.r63_product = r63_product;
	}
	public BigDecimal getR63_operational_risk_cap_charge() {
		return r63_operational_risk_cap_charge;
	}
	public void setR63_operational_risk_cap_charge(BigDecimal r63_operational_risk_cap_charge) {
		this.r63_operational_risk_cap_charge = r63_operational_risk_cap_charge;
	}
	public String getR64_product() {
		return r64_product;
	}
	public void setR64_product(String r64_product) {
		this.r64_product = r64_product;
	}
	public BigDecimal getR64_risk_weight_factor() {
		return r64_risk_weight_factor;
	}
	public void setR64_risk_weight_factor(BigDecimal r64_risk_weight_factor) {
		this.r64_risk_weight_factor = r64_risk_weight_factor;
	}
	public String getR65_product() {
		return r65_product;
	}
	public void setR65_product(String r65_product) {
		this.r65_product = r65_product;
	}
	public BigDecimal getR65_risk_weight_factor() {
		return r65_risk_weight_factor;
	}
	public void setR65_risk_weight_factor(BigDecimal r65_risk_weight_factor) {
		this.r65_risk_weight_factor = r65_risk_weight_factor;
	}
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
	public M_OR2_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}
