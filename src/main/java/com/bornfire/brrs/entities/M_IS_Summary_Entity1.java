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
@Table(name = "BRRS_M_IS_SUMMARYTABLE1")

public class M_IS_Summary_Entity1 {
	public String R10_PRODUCT;
	public BigDecimal R10_FAIR_VALUE_PROFIT_AND_LOSS;
	public BigDecimal R10_HELD_TO_MATURITY;
	public BigDecimal R10_AVAILABLE_FOR_SALE;
	public BigDecimal R10_TOTAL;
	public String R11_PRODUCT;
	public BigDecimal R11_FAIR_VALUE_PROFIT_AND_LOSS;
	public BigDecimal R11_HELD_TO_MATURITY;
	public BigDecimal R11_AVAILABLE_FOR_SALE;
	public BigDecimal R11_TOTAL;
	public String R12_PRODUCT;
	public BigDecimal R12_FAIR_VALUE_PROFIT_AND_LOSS;
	public BigDecimal R12_HELD_TO_MATURITY;
	public BigDecimal R12_AVAILABLE_FOR_SALE;
	public BigDecimal R12_TOTAL;
	public String R13_PRODUCT;
	public BigDecimal R13_FAIR_VALUE_PROFIT_AND_LOSS;
	public BigDecimal R13_HELD_TO_MATURITY;
	public BigDecimal R13_AVAILABLE_FOR_SALE;
	public BigDecimal R13_TOTAL;
	public String R14_PRODUCT;
	public BigDecimal R14_FAIR_VALUE_PROFIT_AND_LOSS;
	public BigDecimal R14_HELD_TO_MATURITY;
	public BigDecimal R14_AVAILABLE_FOR_SALE;
	public BigDecimal R14_TOTAL;
	public String R15_PRODUCT;
	public BigDecimal R15_FAIR_VALUE_PROFIT_AND_LOSS;
	public BigDecimal R15_HELD_TO_MATURITY;
	public BigDecimal R15_AVAILABLE_FOR_SALE;
	public BigDecimal R15_TOTAL;
	public String R16_PRODUCT;
	public BigDecimal R16_FAIR_VALUE_PROFIT_AND_LOSS;
	public BigDecimal R16_HELD_TO_MATURITY;
	public BigDecimal R16_AVAILABLE_FOR_SALE;
	public BigDecimal R16_TOTAL;
	

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Id
	@Column(name = "REPORT_DATE")
	private Date reportDate;
	@Column(name = "REPORT_VERSION")
	private String reportVersion;
   public String REPORT_FREQUENCY;
   public String REPORT_CODE;
   public String REPORT_DESC;
   public String ENTITY_FLG;
   public String MODIFY_FLG;
   public String DEL_FLG;
	public String getR10_PRODUCT() {
		return R10_PRODUCT;
	}
	public void setR10_PRODUCT(String r10_PRODUCT) {
		R10_PRODUCT = r10_PRODUCT;
	}
	public BigDecimal getR10_FAIR_VALUE_PROFIT_AND_LOSS() {
		return R10_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public void setR10_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r10_FAIR_VALUE_PROFIT_AND_LOSS) {
		R10_FAIR_VALUE_PROFIT_AND_LOSS = r10_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public BigDecimal getR10_HELD_TO_MATURITY() {
		return R10_HELD_TO_MATURITY;
	}
	public void setR10_HELD_TO_MATURITY(BigDecimal r10_HELD_TO_MATURITY) {
		R10_HELD_TO_MATURITY = r10_HELD_TO_MATURITY;
	}
	public BigDecimal getR10_AVAILABLE_FOR_SALE() {
		return R10_AVAILABLE_FOR_SALE;
	}
	public void setR10_AVAILABLE_FOR_SALE(BigDecimal r10_AVAILABLE_FOR_SALE) {
		R10_AVAILABLE_FOR_SALE = r10_AVAILABLE_FOR_SALE;
	}
	public BigDecimal getR10_TOTAL() {
		return R10_TOTAL;
	}
	public void setR10_TOTAL(BigDecimal r10_TOTAL) {
		R10_TOTAL = r10_TOTAL;
	}
	public String getR11_PRODUCT() {
		return R11_PRODUCT;
	}
	public void setR11_PRODUCT(String r11_PRODUCT) {
		R11_PRODUCT = r11_PRODUCT;
	}
	public BigDecimal getR11_FAIR_VALUE_PROFIT_AND_LOSS() {
		return R11_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public void setR11_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r11_FAIR_VALUE_PROFIT_AND_LOSS) {
		R11_FAIR_VALUE_PROFIT_AND_LOSS = r11_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public BigDecimal getR11_HELD_TO_MATURITY() {
		return R11_HELD_TO_MATURITY;
	}
	public void setR11_HELD_TO_MATURITY(BigDecimal r11_HELD_TO_MATURITY) {
		R11_HELD_TO_MATURITY = r11_HELD_TO_MATURITY;
	}
	public BigDecimal getR11_AVAILABLE_FOR_SALE() {
		return R11_AVAILABLE_FOR_SALE;
	}
	public void setR11_AVAILABLE_FOR_SALE(BigDecimal r11_AVAILABLE_FOR_SALE) {
		R11_AVAILABLE_FOR_SALE = r11_AVAILABLE_FOR_SALE;
	}
	public BigDecimal getR11_TOTAL() {
		return R11_TOTAL;
	}
	public void setR11_TOTAL(BigDecimal r11_TOTAL) {
		R11_TOTAL = r11_TOTAL;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_FAIR_VALUE_PROFIT_AND_LOSS() {
		return R12_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public void setR12_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r12_FAIR_VALUE_PROFIT_AND_LOSS) {
		R12_FAIR_VALUE_PROFIT_AND_LOSS = r12_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public BigDecimal getR12_HELD_TO_MATURITY() {
		return R12_HELD_TO_MATURITY;
	}
	public void setR12_HELD_TO_MATURITY(BigDecimal r12_HELD_TO_MATURITY) {
		R12_HELD_TO_MATURITY = r12_HELD_TO_MATURITY;
	}
	public BigDecimal getR12_AVAILABLE_FOR_SALE() {
		return R12_AVAILABLE_FOR_SALE;
	}
	public void setR12_AVAILABLE_FOR_SALE(BigDecimal r12_AVAILABLE_FOR_SALE) {
		R12_AVAILABLE_FOR_SALE = r12_AVAILABLE_FOR_SALE;
	}
	public BigDecimal getR12_TOTAL() {
		return R12_TOTAL;
	}
	public void setR12_TOTAL(BigDecimal r12_TOTAL) {
		R12_TOTAL = r12_TOTAL;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_FAIR_VALUE_PROFIT_AND_LOSS() {
		return R13_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public void setR13_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r13_FAIR_VALUE_PROFIT_AND_LOSS) {
		R13_FAIR_VALUE_PROFIT_AND_LOSS = r13_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public BigDecimal getR13_HELD_TO_MATURITY() {
		return R13_HELD_TO_MATURITY;
	}
	public void setR13_HELD_TO_MATURITY(BigDecimal r13_HELD_TO_MATURITY) {
		R13_HELD_TO_MATURITY = r13_HELD_TO_MATURITY;
	}
	public BigDecimal getR13_AVAILABLE_FOR_SALE() {
		return R13_AVAILABLE_FOR_SALE;
	}
	public void setR13_AVAILABLE_FOR_SALE(BigDecimal r13_AVAILABLE_FOR_SALE) {
		R13_AVAILABLE_FOR_SALE = r13_AVAILABLE_FOR_SALE;
	}
	public BigDecimal getR13_TOTAL() {
		return R13_TOTAL;
	}
	public void setR13_TOTAL(BigDecimal r13_TOTAL) {
		R13_TOTAL = r13_TOTAL;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_FAIR_VALUE_PROFIT_AND_LOSS() {
		return R14_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public void setR14_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r14_FAIR_VALUE_PROFIT_AND_LOSS) {
		R14_FAIR_VALUE_PROFIT_AND_LOSS = r14_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public BigDecimal getR14_HELD_TO_MATURITY() {
		return R14_HELD_TO_MATURITY;
	}
	public void setR14_HELD_TO_MATURITY(BigDecimal r14_HELD_TO_MATURITY) {
		R14_HELD_TO_MATURITY = r14_HELD_TO_MATURITY;
	}
	public BigDecimal getR14_AVAILABLE_FOR_SALE() {
		return R14_AVAILABLE_FOR_SALE;
	}
	public void setR14_AVAILABLE_FOR_SALE(BigDecimal r14_AVAILABLE_FOR_SALE) {
		R14_AVAILABLE_FOR_SALE = r14_AVAILABLE_FOR_SALE;
	}
	public BigDecimal getR14_TOTAL() {
		return R14_TOTAL;
	}
	public void setR14_TOTAL(BigDecimal r14_TOTAL) {
		R14_TOTAL = r14_TOTAL;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_FAIR_VALUE_PROFIT_AND_LOSS() {
		return R15_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public void setR15_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r15_FAIR_VALUE_PROFIT_AND_LOSS) {
		R15_FAIR_VALUE_PROFIT_AND_LOSS = r15_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public BigDecimal getR15_HELD_TO_MATURITY() {
		return R15_HELD_TO_MATURITY;
	}
	public void setR15_HELD_TO_MATURITY(BigDecimal r15_HELD_TO_MATURITY) {
		R15_HELD_TO_MATURITY = r15_HELD_TO_MATURITY;
	}
	public BigDecimal getR15_AVAILABLE_FOR_SALE() {
		return R15_AVAILABLE_FOR_SALE;
	}
	public void setR15_AVAILABLE_FOR_SALE(BigDecimal r15_AVAILABLE_FOR_SALE) {
		R15_AVAILABLE_FOR_SALE = r15_AVAILABLE_FOR_SALE;
	}
	public BigDecimal getR15_TOTAL() {
		return R15_TOTAL;
	}
	public void setR15_TOTAL(BigDecimal r15_TOTAL) {
		R15_TOTAL = r15_TOTAL;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_FAIR_VALUE_PROFIT_AND_LOSS() {
		return R16_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public void setR16_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r16_FAIR_VALUE_PROFIT_AND_LOSS) {
		R16_FAIR_VALUE_PROFIT_AND_LOSS = r16_FAIR_VALUE_PROFIT_AND_LOSS;
	}
	public BigDecimal getR16_HELD_TO_MATURITY() {
		return R16_HELD_TO_MATURITY;
	}
	public void setR16_HELD_TO_MATURITY(BigDecimal r16_HELD_TO_MATURITY) {
		R16_HELD_TO_MATURITY = r16_HELD_TO_MATURITY;
	}
	public BigDecimal getR16_AVAILABLE_FOR_SALE() {
		return R16_AVAILABLE_FOR_SALE;
	}
	public void setR16_AVAILABLE_FOR_SALE(BigDecimal r16_AVAILABLE_FOR_SALE) {
		R16_AVAILABLE_FOR_SALE = r16_AVAILABLE_FOR_SALE;
	}
	public BigDecimal getR16_TOTAL() {
		return R16_TOTAL;
	}
	public void setR16_TOTAL(BigDecimal r16_TOTAL) {
		R16_TOTAL = r16_TOTAL;
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
	public M_IS_Summary_Entity1() {
		super();
		// TODO Auto-generated constructor stub
	}
  
  
  
}	
	
	
	
	
	
	
	
	
	
	
	
