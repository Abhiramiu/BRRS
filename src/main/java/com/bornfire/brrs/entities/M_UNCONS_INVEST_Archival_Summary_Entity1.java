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
@Table(name = "BRRS_M_UNCONS_INVEST_ARCHIVALTABLE_SUMMARY1")
public class M_UNCONS_INVEST_Archival_Summary_Entity1 {
	
	private String R11_PRODUCT;
	private BigDecimal R11_AMOUNT;
	private BigDecimal R11_PERCENT_OF_CET1_HOLDING;
	private BigDecimal R11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
	private BigDecimal R11_PERCENT_OF_TIER_2_HOLDING;

	private String R12_PRODUCT;
	private BigDecimal R12_AMOUNT;
	private BigDecimal R12_PERCENT_OF_CET1_HOLDING;
	private BigDecimal R12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
	private BigDecimal R12_PERCENT_OF_TIER_2_HOLDING;

	private String R13_PRODUCT;
	private BigDecimal R13_AMOUNT;
	private BigDecimal R13_PERCENT_OF_CET1_HOLDING;
	private BigDecimal R13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
	private BigDecimal R13_PERCENT_OF_TIER_2_HOLDING;

	private String R14_PRODUCT;
	private BigDecimal R14_AMOUNT;
	private BigDecimal R14_PERCENT_OF_CET1_HOLDING;
	private BigDecimal R14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
	private BigDecimal R14_PERCENT_OF_TIER_2_HOLDING;

	private String R15_PRODUCT;
	private BigDecimal R15_AMOUNT;
	private BigDecimal R15_PERCENT_OF_CET1_HOLDING;
	private BigDecimal R15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
	private BigDecimal R15_PERCENT_OF_TIER_2_HOLDING;

   @Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
   
   @Id
   private Date REPORT_DATE;

   private String REPORT_VERSION;
   private String REPORT_FREQUENCY;
   private String REPORT_CODE;
   private String REPORT_DESC;
   private String ENTITY_FLG;
   private String MODIFY_FLG;
   private String DEL_FLG;
   public String getR11_PRODUCT() {
	return R11_PRODUCT;
   }
   public void setR11_PRODUCT(String r11_PRODUCT) {
	R11_PRODUCT = r11_PRODUCT;
   }
   public BigDecimal getR11_AMOUNT() {
	return R11_AMOUNT;
   }
   public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
	R11_AMOUNT = r11_AMOUNT;
   }
   public BigDecimal getR11_PERCENT_OF_CET1_HOLDING() {
	return R11_PERCENT_OF_CET1_HOLDING;
   }
   public void setR11_PERCENT_OF_CET1_HOLDING(BigDecimal r11_PERCENT_OF_CET1_HOLDING) {
	R11_PERCENT_OF_CET1_HOLDING = r11_PERCENT_OF_CET1_HOLDING;
   }
   public BigDecimal getR11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() {
	return R11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
   }
   public void setR11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING(BigDecimal r11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING) {
	R11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING = r11_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
   }
   public BigDecimal getR11_PERCENT_OF_TIER_2_HOLDING() {
	return R11_PERCENT_OF_TIER_2_HOLDING;
   }
   public void setR11_PERCENT_OF_TIER_2_HOLDING(BigDecimal r11_PERCENT_OF_TIER_2_HOLDING) {
	R11_PERCENT_OF_TIER_2_HOLDING = r11_PERCENT_OF_TIER_2_HOLDING;
   }
   public String getR12_PRODUCT() {
	return R12_PRODUCT;
   }
   public void setR12_PRODUCT(String r12_PRODUCT) {
	R12_PRODUCT = r12_PRODUCT;
   }
   public BigDecimal getR12_AMOUNT() {
	return R12_AMOUNT;
   }
   public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
	R12_AMOUNT = r12_AMOUNT;
   }
   public BigDecimal getR12_PERCENT_OF_CET1_HOLDING() {
	return R12_PERCENT_OF_CET1_HOLDING;
   }
   public void setR12_PERCENT_OF_CET1_HOLDING(BigDecimal r12_PERCENT_OF_CET1_HOLDING) {
	R12_PERCENT_OF_CET1_HOLDING = r12_PERCENT_OF_CET1_HOLDING;
   }
   public BigDecimal getR12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() {
	return R12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
   }
   public void setR12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING(BigDecimal r12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING) {
	R12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING = r12_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
   }
   public BigDecimal getR12_PERCENT_OF_TIER_2_HOLDING() {
	return R12_PERCENT_OF_TIER_2_HOLDING;
   }
   public void setR12_PERCENT_OF_TIER_2_HOLDING(BigDecimal r12_PERCENT_OF_TIER_2_HOLDING) {
	R12_PERCENT_OF_TIER_2_HOLDING = r12_PERCENT_OF_TIER_2_HOLDING;
   }
   public String getR13_PRODUCT() {
	return R13_PRODUCT;
   }
   public void setR13_PRODUCT(String r13_PRODUCT) {
	R13_PRODUCT = r13_PRODUCT;
   }
   public BigDecimal getR13_AMOUNT() {
	return R13_AMOUNT;
   }
   public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
	R13_AMOUNT = r13_AMOUNT;
   }
   public BigDecimal getR13_PERCENT_OF_CET1_HOLDING() {
	return R13_PERCENT_OF_CET1_HOLDING;
   }
   public void setR13_PERCENT_OF_CET1_HOLDING(BigDecimal r13_PERCENT_OF_CET1_HOLDING) {
	R13_PERCENT_OF_CET1_HOLDING = r13_PERCENT_OF_CET1_HOLDING;
   }
   public BigDecimal getR13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() {
	return R13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
   }
   public void setR13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING(BigDecimal r13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING) {
	R13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING = r13_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
   }
   public BigDecimal getR13_PERCENT_OF_TIER_2_HOLDING() {
	return R13_PERCENT_OF_TIER_2_HOLDING;
   }
   public void setR13_PERCENT_OF_TIER_2_HOLDING(BigDecimal r13_PERCENT_OF_TIER_2_HOLDING) {
	R13_PERCENT_OF_TIER_2_HOLDING = r13_PERCENT_OF_TIER_2_HOLDING;
   }
   public String getR14_PRODUCT() {
	return R14_PRODUCT;
   }
   public void setR14_PRODUCT(String r14_PRODUCT) {
	R14_PRODUCT = r14_PRODUCT;
   }
   public BigDecimal getR14_AMOUNT() {
	return R14_AMOUNT;
   }
   public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
	R14_AMOUNT = r14_AMOUNT;
   }
   public BigDecimal getR14_PERCENT_OF_CET1_HOLDING() {
	return R14_PERCENT_OF_CET1_HOLDING;
   }
   public void setR14_PERCENT_OF_CET1_HOLDING(BigDecimal r14_PERCENT_OF_CET1_HOLDING) {
	R14_PERCENT_OF_CET1_HOLDING = r14_PERCENT_OF_CET1_HOLDING;
   }
   public BigDecimal getR14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() {
	return R14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
   }
   public void setR14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING(BigDecimal r14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING) {
	R14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING = r14_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
   }
   public BigDecimal getR14_PERCENT_OF_TIER_2_HOLDING() {
	return R14_PERCENT_OF_TIER_2_HOLDING;
   }
   public void setR14_PERCENT_OF_TIER_2_HOLDING(BigDecimal r14_PERCENT_OF_TIER_2_HOLDING) {
	R14_PERCENT_OF_TIER_2_HOLDING = r14_PERCENT_OF_TIER_2_HOLDING;
   }
   public String getR15_PRODUCT() {
	return R15_PRODUCT;
   }
   public void setR15_PRODUCT(String r15_PRODUCT) {
	R15_PRODUCT = r15_PRODUCT;
   }
   public BigDecimal getR15_AMOUNT() {
	return R15_AMOUNT;
   }
   public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
	R15_AMOUNT = r15_AMOUNT;
   }
   public BigDecimal getR15_PERCENT_OF_CET1_HOLDING() {
	return R15_PERCENT_OF_CET1_HOLDING;
   }
   public void setR15_PERCENT_OF_CET1_HOLDING(BigDecimal r15_PERCENT_OF_CET1_HOLDING) {
	R15_PERCENT_OF_CET1_HOLDING = r15_PERCENT_OF_CET1_HOLDING;
   }
   public BigDecimal getR15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING() {
	return R15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
   }
   public void setR15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING(BigDecimal r15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING) {
	R15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING = r15_PERCENT_OF_ADDITIONAL_TIER_1_HOLDING;
   }
   public BigDecimal getR15_PERCENT_OF_TIER_2_HOLDING() {
	return R15_PERCENT_OF_TIER_2_HOLDING;
   }
   public void setR15_PERCENT_OF_TIER_2_HOLDING(BigDecimal r15_PERCENT_OF_TIER_2_HOLDING) {
	R15_PERCENT_OF_TIER_2_HOLDING = r15_PERCENT_OF_TIER_2_HOLDING;
   }
   public Date getREPORT_DATE() {
	return REPORT_DATE;
   }
   public void setREPORT_DATE(Date rEPORT_DATE) {
	REPORT_DATE = rEPORT_DATE;
   }
   public String getREPORT_VERSION() {
	return REPORT_VERSION;
   }
   public void setREPORT_VERSION(String rEPORT_VERSION) {
	REPORT_VERSION = rEPORT_VERSION;
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
   public M_UNCONS_INVEST_Archival_Summary_Entity1() {
	super();
	// TODO Auto-generated constructor stub
   }
   
   
}

