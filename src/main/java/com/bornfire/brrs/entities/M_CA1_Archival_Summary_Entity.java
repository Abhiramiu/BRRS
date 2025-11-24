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
@Table(name = "BRRS_M_CA1_ARCHIVALTABLE_SUMMARY")

public class M_CA1_Archival_Summary_Entity {
    private String R9_REF_PARM;
    private String R9_PRODUCT;
    private BigDecimal R9_AMOUNT;

    private String R10_REF_PARM;
    private String R10_PRODUCT;
    private BigDecimal R10_AMOUNT;

    private String R11_REF_PARM;
    private String R11_PRODUCT;
    private BigDecimal R11_AMOUNT;

    private String R12_REF_PARM;
    private String R12_PRODUCT;
    private BigDecimal R12_AMOUNT;

    private String R13_REF_PARM;
    private String R13_PRODUCT;
    private BigDecimal R13_AMOUNT;

    private String R14_REF_PARM;
    private String R14_PRODUCT;
    private BigDecimal R14_AMOUNT;

    private String R17_REF_PARM;
    private String R17_PRODUCT;
    private BigDecimal R17_AMOUNT;

    private String R18_REF_PARM;
    private String R18_PRODUCT;
    private BigDecimal R18_AMOUNT;

    private String R19_REF_PARM;
    private String R19_PRODUCT;
    private BigDecimal R19_AMOUNT;

    private String R20_REF_PARM;
    private String R20_PRODUCT;
    private BigDecimal R20_AMOUNT;

    private String R21_REF_PARM;
    private String R21_PRODUCT;
    private BigDecimal R21_AMOUNT;

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
	public String getR9_REF_PARM() {
		return R9_REF_PARM;
	}
	public void setR9_REF_PARM(String r9_REF_PARM) {
		R9_REF_PARM = r9_REF_PARM;
	}
	public String getR9_PRODUCT() {
		return R9_PRODUCT;
	}
	public void setR9_PRODUCT(String r9_PRODUCT) {
		R9_PRODUCT = r9_PRODUCT;
	}
	public BigDecimal getR9_AMOUNT() {
		return R9_AMOUNT;
	}
	public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
		R9_AMOUNT = r9_AMOUNT;
	}
	public String getR10_REF_PARM() {
		return R10_REF_PARM;
	}
	public void setR10_REF_PARM(String r10_REF_PARM) {
		R10_REF_PARM = r10_REF_PARM;
	}
	public String getR10_PRODUCT() {
		return R10_PRODUCT;
	}
	public void setR10_PRODUCT(String r10_PRODUCT) {
		R10_PRODUCT = r10_PRODUCT;
	}
	public BigDecimal getR10_AMOUNT() {
		return R10_AMOUNT;
	}
	public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
		R10_AMOUNT = r10_AMOUNT;
	}
	public String getR11_REF_PARM() {
		return R11_REF_PARM;
	}
	public void setR11_REF_PARM(String r11_REF_PARM) {
		R11_REF_PARM = r11_REF_PARM;
	}
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
	public String getR12_REF_PARM() {
		return R12_REF_PARM;
	}
	public void setR12_REF_PARM(String r12_REF_PARM) {
		R12_REF_PARM = r12_REF_PARM;
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
	public String getR13_REF_PARM() {
		return R13_REF_PARM;
	}
	public void setR13_REF_PARM(String r13_REF_PARM) {
		R13_REF_PARM = r13_REF_PARM;
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
	public String getR14_REF_PARM() {
		return R14_REF_PARM;
	}
	public void setR14_REF_PARM(String r14_REF_PARM) {
		R14_REF_PARM = r14_REF_PARM;
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
	public String getR17_REF_PARM() {
		return R17_REF_PARM;
	}
	public void setR17_REF_PARM(String r17_REF_PARM) {
		R17_REF_PARM = r17_REF_PARM;
	}
	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}
	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}
	public BigDecimal getR17_AMOUNT() {
		return R17_AMOUNT;
	}
	public void setR17_AMOUNT(BigDecimal r17_AMOUNT) {
		R17_AMOUNT = r17_AMOUNT;
	}
	public String getR18_REF_PARM() {
		return R18_REF_PARM;
	}
	public void setR18_REF_PARM(String r18_REF_PARM) {
		R18_REF_PARM = r18_REF_PARM;
	}
	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}
	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}
	public BigDecimal getR18_AMOUNT() {
		return R18_AMOUNT;
	}
	public void setR18_AMOUNT(BigDecimal r18_AMOUNT) {
		R18_AMOUNT = r18_AMOUNT;
	}
	public String getR19_REF_PARM() {
		return R19_REF_PARM;
	}
	public void setR19_REF_PARM(String r19_REF_PARM) {
		R19_REF_PARM = r19_REF_PARM;
	}
	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}
	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}
	public BigDecimal getR19_AMOUNT() {
		return R19_AMOUNT;
	}
	public void setR19_AMOUNT(BigDecimal r19_AMOUNT) {
		R19_AMOUNT = r19_AMOUNT;
	}
	public String getR20_REF_PARM() {
		return R20_REF_PARM;
	}
	public void setR20_REF_PARM(String r20_REF_PARM) {
		R20_REF_PARM = r20_REF_PARM;
	}
	public String getR20_PRODUCT() {
		return R20_PRODUCT;
	}
	public void setR20_PRODUCT(String r20_PRODUCT) {
		R20_PRODUCT = r20_PRODUCT;
	}
	public BigDecimal getR20_AMOUNT() {
		return R20_AMOUNT;
	}
	public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
		R20_AMOUNT = r20_AMOUNT;
	}
	public String getR21_REF_PARM() {
		return R21_REF_PARM;
	}
	public void setR21_REF_PARM(String r21_REF_PARM) {
		R21_REF_PARM = r21_REF_PARM;
	}
	public String getR21_PRODUCT() {
		return R21_PRODUCT;
	}
	public void setR21_PRODUCT(String r21_PRODUCT) {
		R21_PRODUCT = r21_PRODUCT;
	}
	public BigDecimal getR21_AMOUNT() {
		return R21_AMOUNT;
	}
	public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
		R21_AMOUNT = r21_AMOUNT;
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
	public M_CA1_Archival_Summary_Entity() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
