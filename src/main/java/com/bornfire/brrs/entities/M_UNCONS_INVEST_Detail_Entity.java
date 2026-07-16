package com.bornfire.brrs.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BRRS_M_UNCONS_INVEST_DETAILTABLE")
public class M_UNCONS_INVEST_Detail_Entity {

	// ---------------- R11 ----------------
	@Column(name = "R11_PRODUCT")
	private String r11_product;

	@Column(name = "R11_AMOUNT")
	private BigDecimal r11_amount;

	@Column(name = "R11_PERCENT_OF_CET1_HOLDING")
	private BigDecimal r11_percent_of_cet1_holding;

	@Column(name = "R11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING")
	private BigDecimal r11_percent_of_additional_tier_1_holding;

	@Column(name = "R11_PERCENT_OF_TIER_2_HOLDING")
	private BigDecimal r11_percent_of_tier_2_holding;

	// ---------------- R12 ----------------
	@Column(name = "R12_PRODUCT")
	private String r12_product;

	@Column(name = "R12_AMOUNT")
	private BigDecimal r12_amount;

	@Column(name = "R12_PERCENT_OF_CET1_HOLDING")
	private BigDecimal r12_percent_of_cet1_holding;

	@Column(name = "R12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING")
	private BigDecimal r12_percent_of_additional_tier_1_holding;

	@Column(name = "R12_PERCENT_OF_TIER_2_HOLDING")
	private BigDecimal r12_percent_of_tier_2_holding;

	// ---------------- R13 ----------------
	@Column(name = "R13_PRODUCT")
	private String r13_product;

	@Column(name = "R13_AMOUNT")
	private BigDecimal r13_amount;

	@Column(name = "R13_PERCENT_OF_CET1_HOLDING")
	private BigDecimal r13_percent_of_cet1_holding;

	@Column(name = "R13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING")
	private BigDecimal r13_percent_of_additional_tier_1_holding;

	@Column(name = "R13_PERCENT_OF_TIER_2_HOLDING")
	private BigDecimal r13_percent_of_tier_2_holding;

	// ---------------- R14 ----------------
	@Column(name = "R14_PRODUCT")
	private String r14_product;

	@Column(name = "R14_AMOUNT")
	private BigDecimal r14_amount;

	@Column(name = "R14_PERCENT_OF_CET1_HOLDING")
	private BigDecimal r14_percent_of_cet1_holding;

	@Column(name = "R14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING")
	private BigDecimal r14_percent_of_additional_tier_1_holding;

	@Column(name = "R14_PERCENT_OF_TIER_2_HOLDING")
	private BigDecimal r14_percent_of_tier_2_holding;

	// ---------------- R15 ----------------
	@Column(name = "R15_PRODUCT")
	private String r15_product;

	@Column(name = "R15_AMOUNT")
	private BigDecimal r15_amount;

	@Column(name = "R15_PERCENT_OF_CET1_HOLDING")
	private BigDecimal r15_percent_of_cet1_holding;

	@Column(name = "R15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING")
	private BigDecimal r15_percent_of_additional_tier_1_holding;

	@Column(name = "R15_PERCENT_OF_TIER_2_HOLDING")
	private BigDecimal r15_percent_of_tier_2_holding;

	// ---------------- R22 ----------------
	@Column(name = "R22_PRODUCT")
	private String r22_product;

	@Column(name = "R22_ACCUULATED_EQUITY_INTEREST_5")
	private BigDecimal r22_accuulated_equity_interest_5;

	@Column(name = "R22_ASSETS")
	private BigDecimal r22_assets;

	@Column(name = "R22_LIABILITIES")
	private BigDecimal r22_liabilities;

	@Column(name = "R22_REVENUE")
	private BigDecimal r22_revenue;

	@Column(name = "R22_PROFIT_OR_LOSS")
	private BigDecimal r22_profit_or_loss;

	@Column(name = "R22_UNREG_SHARE_OF_LOSS")
	private BigDecimal r22_unreg_share_of_loss;

	@Column(name = "R22_CUMULATIVE_UNREG_SHARE_OF_LOSS")
	private BigDecimal r22_cumulative_unreg_share_of_loss;

	// ---------------- R23 ----------------
	@Column(name = "R23_PRODUCT")
	private String r23_product;

	@Column(name = "R23_ACCUULATED_EQUITY_INTEREST_5")
	private BigDecimal r23_accuulated_equity_interest_5;

	@Column(name = "R23_ASSETS")
	private BigDecimal r23_assets;

	@Column(name = "R23_LIABILITIES")
	private BigDecimal r23_liabilities;

	@Column(name = "R23_REVENUE")
	private BigDecimal r23_revenue;

	@Column(name = "R23_PROFIT_OR_LOSS")
	private BigDecimal r23_profit_or_loss;

	@Column(name = "R23_UNREG_SHARE_OF_LOSS")
	private BigDecimal r23_unreg_share_of_loss;

	@Column(name = "R23_CUMULATIVE_UNREG_SHARE_OF_LOSS")
	private BigDecimal r23_cumulative_unreg_share_of_loss;

	// ---------------- R24 ----------------
	@Column(name = "R24_PRODUCT")
	private String r24_product;

	@Column(name = "R24_ACCUULATED_EQUITY_INTEREST_5")
	private BigDecimal r24_accuulated_equity_interest_5;

	@Column(name = "R24_ASSETS")
	private BigDecimal r24_assets;

	@Column(name = "R24_LIABILITIES")
	private BigDecimal r24_liabilities;

	@Column(name = "R24_REVENUE")
	private BigDecimal r24_revenue;

	@Column(name = "R24_PROFIT_OR_LOSS")
	private BigDecimal r24_profit_or_loss;

	@Column(name = "R24_UNREG_SHARE_OF_LOSS")
	private BigDecimal r24_unreg_share_of_loss;

	@Column(name = "R24_CUMULATIVE_UNREG_SHARE_OF_LOSS")
	private BigDecimal r24_cumulative_unreg_share_of_loss;

	// ---------------- R29 ----------------
	@Column(name = "R29_PRODUCT")
	private String r29_product;

	@Column(name = "R29_FAIR_VALUE")
	private BigDecimal r29_fair_value;

	// ---------------- R35 ----------------
	@Column(name = "R35_PRODUCT")
	private String r35_product;

	@Column(name = "R35_COMPANY")
	private BigDecimal r35_company;

	@Column(name = "R35_JURISDICTION_OF_INCORP_1")
	private BigDecimal r35_jurisdiction_of_incorp_1;

	@Column(name = "R35_JURISDICTION_OF_INCORP_2")
	private BigDecimal r35_jurisdiction_of_incorp_2;

	@Column(name = "R35_LINE_OF_BUSINESS")
	private BigDecimal r35_line_of_business;

	@Column(name = "R35_CURRENCY")
	private BigDecimal r35_currency;

	@Column(name = "R35_SHARE_CAPITAL")
	private BigDecimal r35_share_capital;

	@Column(name = "R35_ACCUMULATED_EQUITY_INTEREST")
	private BigDecimal r35_accumulated_equity_interest;

	// ---------------- R36 ----------------
	@Column(name = "R36_PRODUCT")
	private String r36_product;

	@Column(name = "R36_COMPANY")
	private BigDecimal r36_company;

	@Column(name = "R36_JURISDICTION_OF_INCORP_1")
	private BigDecimal r36_jurisdiction_of_incorp_1;

	@Column(name = "R36_JURISDICTION_OF_INCORP_2")
	private BigDecimal r36_jurisdiction_of_incorp_2;

	@Column(name = "R36_LINE_OF_BUSINESS")
	private BigDecimal r36_line_of_business;

	@Column(name = "R36_CURRENCY")
	private BigDecimal r36_currency;

	@Column(name = "R36_SHARE_CAPITAL")
	private BigDecimal r36_share_capital;

	@Column(name = "R36_ACCUMULATED_EQUITY_INTEREST")
	private BigDecimal r36_accumulated_equity_interest;

	// ---------------- R37 ----------------
	@Column(name = "R37_PRODUCT")
	private String r37_product;

	@Column(name = "R37_COMPANY")
	private BigDecimal r37_company;

	@Column(name = "R37_JURISDICTION_OF_INCORP_1")
	private BigDecimal r37_jurisdiction_of_incorp_1;

	@Column(name = "R37_JURISDICTION_OF_INCORP_2")
	private BigDecimal r37_jurisdiction_of_incorp_2;

	@Column(name = "R37_LINE_OF_BUSINESS")
	private BigDecimal r37_line_of_business;

	@Column(name = "R37_CURRENCY")
	private BigDecimal r37_currency;

	@Column(name = "R37_SHARE_CAPITAL")
	private BigDecimal r37_share_capital;

	@Column(name = "R37_ACCUMULATED_EQUITY_INTEREST")
	private BigDecimal r37_accumulated_equity_interest;

	// ---------------- R38 ----------------
	@Column(name = "R38_PRODUCT")
	private String r38_product;

	@Column(name = "R38_COMPANY")
	private BigDecimal r38_company;

	@Column(name = "R38_JURISDICTION_OF_INCORP_1")
	private BigDecimal r38_jurisdiction_of_incorp_1;

	@Column(name = "R38_JURISDICTION_OF_INCORP_2")
	private BigDecimal r38_jurisdiction_of_incorp_2;

	@Column(name = "R38_LINE_OF_BUSINESS")
	private BigDecimal r38_line_of_business;

	@Column(name = "R38_CURRENCY")
	private BigDecimal r38_currency;

	@Column(name = "R38_SHARE_CAPITAL")
	private BigDecimal r38_share_capital;

	@Column(name = "R38_ACCUMULATED_EQUITY_INTEREST")
	private BigDecimal r38_accumulated_equity_interest;

	// ---------------- COMMON ----------------

	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date report_date;

	@Column(name = "REPORT_VERSION")
	private BigDecimal report_version;

	@Column(name = "REPORT_FREQUENCY")
	private String report_frequency;

	@Column(name = "REPORT_CODE")
	private String report_code;

	@Column(name = "REPORT_DESC")
	private String report_desc;

	@Column(name = "ENTITY_FLG")
	private String entity_flg;

	@Column(name = "MODIFY_FLG")
	private String modify_flg;

	@Column(name = "DEL_FLG")
	private String del_flg;

	public String getR11_product() {
		return r11_product;
	}

	public void setR11_product(String r11_product) {
		this.r11_product = r11_product;
	}

	public BigDecimal getR11_amount() {
		return r11_amount;
	}

	public void setR11_amount(BigDecimal r11_amount) {
		this.r11_amount = r11_amount;
	}

	public BigDecimal getR11_percent_of_cet1_holding() {
		return r11_percent_of_cet1_holding;
	}

	public void setR11_percent_of_cet1_holding(BigDecimal r11_percent_of_cet1_holding) {
		this.r11_percent_of_cet1_holding = r11_percent_of_cet1_holding;
	}

	public BigDecimal getR11_percent_of_additional_tier_1_holding() {
		return r11_percent_of_additional_tier_1_holding;
	}

	public void setR11_percent_of_additional_tier_1_holding(BigDecimal r11_percent_of_additional_tier_1_holding) {
		this.r11_percent_of_additional_tier_1_holding = r11_percent_of_additional_tier_1_holding;
	}

	public BigDecimal getR11_percent_of_tier_2_holding() {
		return r11_percent_of_tier_2_holding;
	}

	public void setR11_percent_of_tier_2_holding(BigDecimal r11_percent_of_tier_2_holding) {
		this.r11_percent_of_tier_2_holding = r11_percent_of_tier_2_holding;
	}

	public String getR12_product() {
		return r12_product;
	}

	public void setR12_product(String r12_product) {
		this.r12_product = r12_product;
	}

	public BigDecimal getR12_amount() {
		return r12_amount;
	}

	public void setR12_amount(BigDecimal r12_amount) {
		this.r12_amount = r12_amount;
	}

	public BigDecimal getR12_percent_of_cet1_holding() {
		return r12_percent_of_cet1_holding;
	}

	public void setR12_percent_of_cet1_holding(BigDecimal r12_percent_of_cet1_holding) {
		this.r12_percent_of_cet1_holding = r12_percent_of_cet1_holding;
	}

	public BigDecimal getR12_percent_of_additional_tier_1_holding() {
		return r12_percent_of_additional_tier_1_holding;
	}

	public void setR12_percent_of_additional_tier_1_holding(BigDecimal r12_percent_of_additional_tier_1_holding) {
		this.r12_percent_of_additional_tier_1_holding = r12_percent_of_additional_tier_1_holding;
	}

	public BigDecimal getR12_percent_of_tier_2_holding() {
		return r12_percent_of_tier_2_holding;
	}

	public void setR12_percent_of_tier_2_holding(BigDecimal r12_percent_of_tier_2_holding) {
		this.r12_percent_of_tier_2_holding = r12_percent_of_tier_2_holding;
	}

	public String getR13_product() {
		return r13_product;
	}

	public void setR13_product(String r13_product) {
		this.r13_product = r13_product;
	}

	public BigDecimal getR13_amount() {
		return r13_amount;
	}

	public void setR13_amount(BigDecimal r13_amount) {
		this.r13_amount = r13_amount;
	}

	public BigDecimal getR13_percent_of_cet1_holding() {
		return r13_percent_of_cet1_holding;
	}

	public void setR13_percent_of_cet1_holding(BigDecimal r13_percent_of_cet1_holding) {
		this.r13_percent_of_cet1_holding = r13_percent_of_cet1_holding;
	}

	public BigDecimal getR13_percent_of_additional_tier_1_holding() {
		return r13_percent_of_additional_tier_1_holding;
	}

	public void setR13_percent_of_additional_tier_1_holding(BigDecimal r13_percent_of_additional_tier_1_holding) {
		this.r13_percent_of_additional_tier_1_holding = r13_percent_of_additional_tier_1_holding;
	}

	public BigDecimal getR13_percent_of_tier_2_holding() {
		return r13_percent_of_tier_2_holding;
	}

	public void setR13_percent_of_tier_2_holding(BigDecimal r13_percent_of_tier_2_holding) {
		this.r13_percent_of_tier_2_holding = r13_percent_of_tier_2_holding;
	}

	public String getR14_product() {
		return r14_product;
	}

	public void setR14_product(String r14_product) {
		this.r14_product = r14_product;
	}

	public BigDecimal getR14_amount() {
		return r14_amount;
	}

	public void setR14_amount(BigDecimal r14_amount) {
		this.r14_amount = r14_amount;
	}

	public BigDecimal getR14_percent_of_cet1_holding() {
		return r14_percent_of_cet1_holding;
	}

	public void setR14_percent_of_cet1_holding(BigDecimal r14_percent_of_cet1_holding) {
		this.r14_percent_of_cet1_holding = r14_percent_of_cet1_holding;
	}

	public BigDecimal getR14_percent_of_additional_tier_1_holding() {
		return r14_percent_of_additional_tier_1_holding;
	}

	public void setR14_percent_of_additional_tier_1_holding(BigDecimal r14_percent_of_additional_tier_1_holding) {
		this.r14_percent_of_additional_tier_1_holding = r14_percent_of_additional_tier_1_holding;
	}

	public BigDecimal getR14_percent_of_tier_2_holding() {
		return r14_percent_of_tier_2_holding;
	}

	public void setR14_percent_of_tier_2_holding(BigDecimal r14_percent_of_tier_2_holding) {
		this.r14_percent_of_tier_2_holding = r14_percent_of_tier_2_holding;
	}

	public String getR15_product() {
		return r15_product;
	}

	public void setR15_product(String r15_product) {
		this.r15_product = r15_product;
	}

	public BigDecimal getR15_amount() {
		return r15_amount;
	}

	public void setR15_amount(BigDecimal r15_amount) {
		this.r15_amount = r15_amount;
	}

	public BigDecimal getR15_percent_of_cet1_holding() {
		return r15_percent_of_cet1_holding;
	}

	public void setR15_percent_of_cet1_holding(BigDecimal r15_percent_of_cet1_holding) {
		this.r15_percent_of_cet1_holding = r15_percent_of_cet1_holding;
	}

	public BigDecimal getR15_percent_of_additional_tier_1_holding() {
		return r15_percent_of_additional_tier_1_holding;
	}

	public void setR15_percent_of_additional_tier_1_holding(BigDecimal r15_percent_of_additional_tier_1_holding) {
		this.r15_percent_of_additional_tier_1_holding = r15_percent_of_additional_tier_1_holding;
	}

	public BigDecimal getR15_percent_of_tier_2_holding() {
		return r15_percent_of_tier_2_holding;
	}

	public void setR15_percent_of_tier_2_holding(BigDecimal r15_percent_of_tier_2_holding) {
		this.r15_percent_of_tier_2_holding = r15_percent_of_tier_2_holding;
	}

	public String getR22_product() {
		return r22_product;
	}

	public void setR22_product(String r22_product) {
		this.r22_product = r22_product;
	}

	public BigDecimal getR22_accuulated_equity_interest_5() {
		return r22_accuulated_equity_interest_5;
	}

	public void setR22_accuulated_equity_interest_5(BigDecimal r22_accuulated_equity_interest_5) {
		this.r22_accuulated_equity_interest_5 = r22_accuulated_equity_interest_5;
	}

	public BigDecimal getR22_assets() {
		return r22_assets;
	}

	public void setR22_assets(BigDecimal r22_assets) {
		this.r22_assets = r22_assets;
	}

	public BigDecimal getR22_liabilities() {
		return r22_liabilities;
	}

	public void setR22_liabilities(BigDecimal r22_liabilities) {
		this.r22_liabilities = r22_liabilities;
	}

	public BigDecimal getR22_revenue() {
		return r22_revenue;
	}

	public void setR22_revenue(BigDecimal r22_revenue) {
		this.r22_revenue = r22_revenue;
	}

	public BigDecimal getR22_profit_or_loss() {
		return r22_profit_or_loss;
	}

	public void setR22_profit_or_loss(BigDecimal r22_profit_or_loss) {
		this.r22_profit_or_loss = r22_profit_or_loss;
	}

	public BigDecimal getR22_unreg_share_of_loss() {
		return r22_unreg_share_of_loss;
	}

	public void setR22_unreg_share_of_loss(BigDecimal r22_unreg_share_of_loss) {
		this.r22_unreg_share_of_loss = r22_unreg_share_of_loss;
	}

	public BigDecimal getR22_cumulative_unreg_share_of_loss() {
		return r22_cumulative_unreg_share_of_loss;
	}

	public void setR22_cumulative_unreg_share_of_loss(BigDecimal r22_cumulative_unreg_share_of_loss) {
		this.r22_cumulative_unreg_share_of_loss = r22_cumulative_unreg_share_of_loss;
	}

	public String getR23_product() {
		return r23_product;
	}

	public void setR23_product(String r23_product) {
		this.r23_product = r23_product;
	}

	public BigDecimal getR23_accuulated_equity_interest_5() {
		return r23_accuulated_equity_interest_5;
	}

	public void setR23_accuulated_equity_interest_5(BigDecimal r23_accuulated_equity_interest_5) {
		this.r23_accuulated_equity_interest_5 = r23_accuulated_equity_interest_5;
	}

	public BigDecimal getR23_assets() {
		return r23_assets;
	}

	public void setR23_assets(BigDecimal r23_assets) {
		this.r23_assets = r23_assets;
	}

	public BigDecimal getR23_liabilities() {
		return r23_liabilities;
	}

	public void setR23_liabilities(BigDecimal r23_liabilities) {
		this.r23_liabilities = r23_liabilities;
	}

	public BigDecimal getR23_revenue() {
		return r23_revenue;
	}

	public void setR23_revenue(BigDecimal r23_revenue) {
		this.r23_revenue = r23_revenue;
	}

	public BigDecimal getR23_profit_or_loss() {
		return r23_profit_or_loss;
	}

	public void setR23_profit_or_loss(BigDecimal r23_profit_or_loss) {
		this.r23_profit_or_loss = r23_profit_or_loss;
	}

	public BigDecimal getR23_unreg_share_of_loss() {
		return r23_unreg_share_of_loss;
	}

	public void setR23_unreg_share_of_loss(BigDecimal r23_unreg_share_of_loss) {
		this.r23_unreg_share_of_loss = r23_unreg_share_of_loss;
	}

	public BigDecimal getR23_cumulative_unreg_share_of_loss() {
		return r23_cumulative_unreg_share_of_loss;
	}

	public void setR23_cumulative_unreg_share_of_loss(BigDecimal r23_cumulative_unreg_share_of_loss) {
		this.r23_cumulative_unreg_share_of_loss = r23_cumulative_unreg_share_of_loss;
	}

	public String getR24_product() {
		return r24_product;
	}

	public void setR24_product(String r24_product) {
		this.r24_product = r24_product;
	}

	public BigDecimal getR24_accuulated_equity_interest_5() {
		return r24_accuulated_equity_interest_5;
	}

	public void setR24_accuulated_equity_interest_5(BigDecimal r24_accuulated_equity_interest_5) {
		this.r24_accuulated_equity_interest_5 = r24_accuulated_equity_interest_5;
	}

	public BigDecimal getR24_assets() {
		return r24_assets;
	}

	public void setR24_assets(BigDecimal r24_assets) {
		this.r24_assets = r24_assets;
	}

	public BigDecimal getR24_liabilities() {
		return r24_liabilities;
	}

	public void setR24_liabilities(BigDecimal r24_liabilities) {
		this.r24_liabilities = r24_liabilities;
	}

	public BigDecimal getR24_revenue() {
		return r24_revenue;
	}

	public void setR24_revenue(BigDecimal r24_revenue) {
		this.r24_revenue = r24_revenue;
	}

	public BigDecimal getR24_profit_or_loss() {
		return r24_profit_or_loss;
	}

	public void setR24_profit_or_loss(BigDecimal r24_profit_or_loss) {
		this.r24_profit_or_loss = r24_profit_or_loss;
	}

	public BigDecimal getR24_unreg_share_of_loss() {
		return r24_unreg_share_of_loss;
	}

	public void setR24_unreg_share_of_loss(BigDecimal r24_unreg_share_of_loss) {
		this.r24_unreg_share_of_loss = r24_unreg_share_of_loss;
	}

	public BigDecimal getR24_cumulative_unreg_share_of_loss() {
		return r24_cumulative_unreg_share_of_loss;
	}

	public void setR24_cumulative_unreg_share_of_loss(BigDecimal r24_cumulative_unreg_share_of_loss) {
		this.r24_cumulative_unreg_share_of_loss = r24_cumulative_unreg_share_of_loss;
	}

	public String getR29_product() {
		return r29_product;
	}

	public void setR29_product(String r29_product) {
		this.r29_product = r29_product;
	}

	public BigDecimal getR29_fair_value() {
		return r29_fair_value;
	}

	public void setR29_fair_value(BigDecimal r29_fair_value) {
		this.r29_fair_value = r29_fair_value;
	}

	public String getR35_product() {
		return r35_product;
	}

	public void setR35_product(String r35_product) {
		this.r35_product = r35_product;
	}

	public BigDecimal getR35_company() {
		return r35_company;
	}

	public void setR35_company(BigDecimal r35_company) {
		this.r35_company = r35_company;
	}

	public BigDecimal getR35_jurisdiction_of_incorp_1() {
		return r35_jurisdiction_of_incorp_1;
	}

	public void setR35_jurisdiction_of_incorp_1(BigDecimal r35_jurisdiction_of_incorp_1) {
		this.r35_jurisdiction_of_incorp_1 = r35_jurisdiction_of_incorp_1;
	}

	public BigDecimal getR35_jurisdiction_of_incorp_2() {
		return r35_jurisdiction_of_incorp_2;
	}

	public void setR35_jurisdiction_of_incorp_2(BigDecimal r35_jurisdiction_of_incorp_2) {
		this.r35_jurisdiction_of_incorp_2 = r35_jurisdiction_of_incorp_2;
	}

	public BigDecimal getR35_line_of_business() {
		return r35_line_of_business;
	}

	public void setR35_line_of_business(BigDecimal r35_line_of_business) {
		this.r35_line_of_business = r35_line_of_business;
	}

	public BigDecimal getR35_currency() {
		return r35_currency;
	}

	public void setR35_currency(BigDecimal r35_currency) {
		this.r35_currency = r35_currency;
	}

	public BigDecimal getR35_share_capital() {
		return r35_share_capital;
	}

	public void setR35_share_capital(BigDecimal r35_share_capital) {
		this.r35_share_capital = r35_share_capital;
	}

	public BigDecimal getR35_accumulated_equity_interest() {
		return r35_accumulated_equity_interest;
	}

	public void setR35_accumulated_equity_interest(BigDecimal r35_accumulated_equity_interest) {
		this.r35_accumulated_equity_interest = r35_accumulated_equity_interest;
	}

	public String getR36_product() {
		return r36_product;
	}

	public void setR36_product(String r36_product) {
		this.r36_product = r36_product;
	}

	public BigDecimal getR36_company() {
		return r36_company;
	}

	public void setR36_company(BigDecimal r36_company) {
		this.r36_company = r36_company;
	}

	public BigDecimal getR36_jurisdiction_of_incorp_1() {
		return r36_jurisdiction_of_incorp_1;
	}

	public void setR36_jurisdiction_of_incorp_1(BigDecimal r36_jurisdiction_of_incorp_1) {
		this.r36_jurisdiction_of_incorp_1 = r36_jurisdiction_of_incorp_1;
	}

	public BigDecimal getR36_jurisdiction_of_incorp_2() {
		return r36_jurisdiction_of_incorp_2;
	}

	public void setR36_jurisdiction_of_incorp_2(BigDecimal r36_jurisdiction_of_incorp_2) {
		this.r36_jurisdiction_of_incorp_2 = r36_jurisdiction_of_incorp_2;
	}

	public BigDecimal getR36_line_of_business() {
		return r36_line_of_business;
	}

	public void setR36_line_of_business(BigDecimal r36_line_of_business) {
		this.r36_line_of_business = r36_line_of_business;
	}

	public BigDecimal getR36_currency() {
		return r36_currency;
	}

	public void setR36_currency(BigDecimal r36_currency) {
		this.r36_currency = r36_currency;
	}

	public BigDecimal getR36_share_capital() {
		return r36_share_capital;
	}

	public void setR36_share_capital(BigDecimal r36_share_capital) {
		this.r36_share_capital = r36_share_capital;
	}

	public BigDecimal getR36_accumulated_equity_interest() {
		return r36_accumulated_equity_interest;
	}

	public void setR36_accumulated_equity_interest(BigDecimal r36_accumulated_equity_interest) {
		this.r36_accumulated_equity_interest = r36_accumulated_equity_interest;
	}

	public String getR37_product() {
		return r37_product;
	}

	public void setR37_product(String r37_product) {
		this.r37_product = r37_product;
	}

	public BigDecimal getR37_company() {
		return r37_company;
	}

	public void setR37_company(BigDecimal r37_company) {
		this.r37_company = r37_company;
	}

	public BigDecimal getR37_jurisdiction_of_incorp_1() {
		return r37_jurisdiction_of_incorp_1;
	}

	public void setR37_jurisdiction_of_incorp_1(BigDecimal r37_jurisdiction_of_incorp_1) {
		this.r37_jurisdiction_of_incorp_1 = r37_jurisdiction_of_incorp_1;
	}

	public BigDecimal getR37_jurisdiction_of_incorp_2() {
		return r37_jurisdiction_of_incorp_2;
	}

	public void setR37_jurisdiction_of_incorp_2(BigDecimal r37_jurisdiction_of_incorp_2) {
		this.r37_jurisdiction_of_incorp_2 = r37_jurisdiction_of_incorp_2;
	}

	public BigDecimal getR37_line_of_business() {
		return r37_line_of_business;
	}

	public void setR37_line_of_business(BigDecimal r37_line_of_business) {
		this.r37_line_of_business = r37_line_of_business;
	}

	public BigDecimal getR37_currency() {
		return r37_currency;
	}

	public void setR37_currency(BigDecimal r37_currency) {
		this.r37_currency = r37_currency;
	}

	public BigDecimal getR37_share_capital() {
		return r37_share_capital;
	}

	public void setR37_share_capital(BigDecimal r37_share_capital) {
		this.r37_share_capital = r37_share_capital;
	}

	public BigDecimal getR37_accumulated_equity_interest() {
		return r37_accumulated_equity_interest;
	}

	public void setR37_accumulated_equity_interest(BigDecimal r37_accumulated_equity_interest) {
		this.r37_accumulated_equity_interest = r37_accumulated_equity_interest;
	}

	public String getR38_product() {
		return r38_product;
	}

	public void setR38_product(String r38_product) {
		this.r38_product = r38_product;
	}

	public BigDecimal getR38_company() {
		return r38_company;
	}

	public void setR38_company(BigDecimal r38_company) {
		this.r38_company = r38_company;
	}

	public BigDecimal getR38_jurisdiction_of_incorp_1() {
		return r38_jurisdiction_of_incorp_1;
	}

	public void setR38_jurisdiction_of_incorp_1(BigDecimal r38_jurisdiction_of_incorp_1) {
		this.r38_jurisdiction_of_incorp_1 = r38_jurisdiction_of_incorp_1;
	}

	public BigDecimal getR38_jurisdiction_of_incorp_2() {
		return r38_jurisdiction_of_incorp_2;
	}

	public void setR38_jurisdiction_of_incorp_2(BigDecimal r38_jurisdiction_of_incorp_2) {
		this.r38_jurisdiction_of_incorp_2 = r38_jurisdiction_of_incorp_2;
	}

	public BigDecimal getR38_line_of_business() {
		return r38_line_of_business;
	}

	public void setR38_line_of_business(BigDecimal r38_line_of_business) {
		this.r38_line_of_business = r38_line_of_business;
	}

	public BigDecimal getR38_currency() {
		return r38_currency;
	}

	public void setR38_currency(BigDecimal r38_currency) {
		this.r38_currency = r38_currency;
	}

	public BigDecimal getR38_share_capital() {
		return r38_share_capital;
	}

	public void setR38_share_capital(BigDecimal r38_share_capital) {
		this.r38_share_capital = r38_share_capital;
	}

	public BigDecimal getR38_accumulated_equity_interest() {
		return r38_accumulated_equity_interest;
	}

	public void setR38_accumulated_equity_interest(BigDecimal r38_accumulated_equity_interest) {
		this.r38_accumulated_equity_interest = r38_accumulated_equity_interest;
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

	public M_UNCONS_INVEST_Detail_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}

}