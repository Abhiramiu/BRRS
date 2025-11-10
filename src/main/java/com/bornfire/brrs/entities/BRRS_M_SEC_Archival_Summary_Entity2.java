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
@Table(name = "BRRS_M_SEC_ARCHIVALTABLE_SUMMARY2") 
public class BRRS_M_SEC_Archival_Summary_Entity2 {
	private String R11_PRODUCT;
	private BigDecimal R11_TCA2;
	private String R12_PRODUCT;
	private BigDecimal R12_TCA2;
	private String R13_PRODUCT;
	private BigDecimal R13_TCA2;
	private String R14_PRODUCT;
	private BigDecimal R14_TCA2;
	private String R15_PRODUCT;
	private BigDecimal R15_TCA2;
	private String R16_PRODUCT;
	private BigDecimal R16_TCA2;
	
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
	public BigDecimal getR11_TCA2() {
		return R11_TCA2;
	}
	public void setR11_TCA2(BigDecimal r11_TCA2) {
		R11_TCA2 = r11_TCA2;
	}
	public String getR12_PRODUCT() {
		return R12_PRODUCT;
	}
	public void setR12_PRODUCT(String r12_PRODUCT) {
		R12_PRODUCT = r12_PRODUCT;
	}
	public BigDecimal getR12_TCA2() {
		return R12_TCA2;
	}
	public void setR12_TCA2(BigDecimal r12_TCA2) {
		R12_TCA2 = r12_TCA2;
	}
	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}
	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}
	public BigDecimal getR13_TCA2() {
		return R13_TCA2;
	}
	public void setR13_TCA2(BigDecimal r13_TCA2) {
		R13_TCA2 = r13_TCA2;
	}
	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}
	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}
	public BigDecimal getR14_TCA2() {
		return R14_TCA2;
	}
	public void setR14_TCA2(BigDecimal r14_TCA2) {
		R14_TCA2 = r14_TCA2;
	}
	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}
	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}
	public BigDecimal getR15_TCA2() {
		return R15_TCA2;
	}
	public void setR15_TCA2(BigDecimal r15_TCA2) {
		R15_TCA2 = r15_TCA2;
	}
	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}
	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}
	public BigDecimal getR16_TCA2() {
		return R16_TCA2;
	}
	public void setR16_TCA2(BigDecimal r16_TCA2) {
		R16_TCA2 = r16_TCA2;
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
	public BRRS_M_SEC_Archival_Summary_Entity2() {
		super();
		// TODO Auto-generated constructor stub
	}
	

   
    

}
