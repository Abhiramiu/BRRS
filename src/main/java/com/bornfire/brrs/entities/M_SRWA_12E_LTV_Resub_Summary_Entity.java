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

@Entity
@Table(name = "BRRS_M_SRWA_12E_RESUB_SUMMARYTABLE")
@IdClass(BRRS_M_SRWA_12E_LTV_PK.class)
public class M_SRWA_12E_LTV_Resub_Summary_Entity {

	private String R13_PRODUCT;
	private BigDecimal R13_PERFORMING_EXPOSURE;
	private BigDecimal R13_NON_PERFORMING;
	private BigDecimal R13_SPECIFIC_PROV;
	private BigDecimal R13_UNSECURED_PORTION_NPL;
	private String R14_PRODUCT;
	private BigDecimal R14_PERFORMING_EXPOSURE;
	private BigDecimal R14_NON_PERFORMING;
	private BigDecimal R14_SPECIFIC_PROV;
	private BigDecimal R14_UNSECURED_PORTION_NPL;
	private String R15_PRODUCT;
	private BigDecimal R15_PERFORMING_EXPOSURE;
	private BigDecimal R15_NON_PERFORMING;
	private BigDecimal R15_SPECIFIC_PROV;
	private BigDecimal R15_UNSECURED_PORTION_NPL;
	private String R16_PRODUCT;
	private BigDecimal R16_PERFORMING_EXPOSURE;
	private BigDecimal R16_NON_PERFORMING;
	private BigDecimal R16_SPECIFIC_PROV;
	private BigDecimal R16_UNSECURED_PORTION_NPL;
	private String R17_PRODUCT;
	private BigDecimal R17_PERFORMING_EXPOSURE;
	private BigDecimal R17_NON_PERFORMING;
	private BigDecimal R17_SPECIFIC_PROV;
	private BigDecimal R17_UNSECURED_PORTION_NPL;
	private String R18_PRODUCT;
	private BigDecimal R18_PERFORMING_EXPOSURE;
	private BigDecimal R18_NON_PERFORMING;
	private BigDecimal R18_SPECIFIC_PROV;
	private BigDecimal R18_UNSECURED_PORTION_NPL;
	private String R19_PRODUCT;
	private BigDecimal R19_PERFORMING_EXPOSURE;
	private BigDecimal R19_NON_PERFORMING;
	private BigDecimal R19_SPECIFIC_PROV;
	private BigDecimal R19_UNSECURED_PORTION_NPL;

    private BigDecimal R13_TOTAL;
    private BigDecimal R14_TOTAL;
    private BigDecimal R15_TOTAL;
    private BigDecimal R16_TOTAL;
    private BigDecimal R17_TOTAL;
    private BigDecimal R18_TOTAL;
    private BigDecimal R19_TOTAL;
    
	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	
	@Id
	@Column(name = "REPORT_VERSION")
	private BigDecimal reportVersion;

	@Column(name = "REPORT_RESUBDATE")
	private Date reportResubDate;
    @Column(name = "REPORT_FREQUENCY")
    private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;

	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}

	public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
		REPORT_FREQUENCY = rEPORT_FREQUENCY;
	}

	public String getR13_PRODUCT() {
		return R13_PRODUCT;
	}

	public void setR13_PRODUCT(String r13_PRODUCT) {
		R13_PRODUCT = r13_PRODUCT;
	}

	public BigDecimal getR13_PERFORMING_EXPOSURE() {
		return R13_PERFORMING_EXPOSURE;
	}

	public void setR13_PERFORMING_EXPOSURE(BigDecimal r13_PERFORMING_EXPOSURE) {
		R13_PERFORMING_EXPOSURE = r13_PERFORMING_EXPOSURE;
	}

	public BigDecimal getR13_NON_PERFORMING() {
		return R13_NON_PERFORMING;
	}

	public void setR13_NON_PERFORMING(BigDecimal r13_NON_PERFORMING) {
		R13_NON_PERFORMING = r13_NON_PERFORMING;
	}

	public BigDecimal getR13_SPECIFIC_PROV() {
		return R13_SPECIFIC_PROV;
	}

	public void setR13_SPECIFIC_PROV(BigDecimal r13_SPECIFIC_PROV) {
		R13_SPECIFIC_PROV = r13_SPECIFIC_PROV;
	}

	public BigDecimal getR13_UNSECURED_PORTION_NPL() {
		return R13_UNSECURED_PORTION_NPL;
	}

	public void setR13_UNSECURED_PORTION_NPL(BigDecimal r13_UNSECURED_PORTION_NPL) {
		R13_UNSECURED_PORTION_NPL = r13_UNSECURED_PORTION_NPL;
	}

	public String getR14_PRODUCT() {
		return R14_PRODUCT;
	}

	public void setR14_PRODUCT(String r14_PRODUCT) {
		R14_PRODUCT = r14_PRODUCT;
	}

	public BigDecimal getR14_PERFORMING_EXPOSURE() {
		return R14_PERFORMING_EXPOSURE;
	}

	public void setR14_PERFORMING_EXPOSURE(BigDecimal r14_PERFORMING_EXPOSURE) {
		R14_PERFORMING_EXPOSURE = r14_PERFORMING_EXPOSURE;
	}

	public BigDecimal getR14_NON_PERFORMING() {
		return R14_NON_PERFORMING;
	}

	public void setR14_NON_PERFORMING(BigDecimal r14_NON_PERFORMING) {
		R14_NON_PERFORMING = r14_NON_PERFORMING;
	}

	public BigDecimal getR14_SPECIFIC_PROV() {
		return R14_SPECIFIC_PROV;
	}

	public void setR14_SPECIFIC_PROV(BigDecimal r14_SPECIFIC_PROV) {
		R14_SPECIFIC_PROV = r14_SPECIFIC_PROV;
	}

	public BigDecimal getR14_UNSECURED_PORTION_NPL() {
		return R14_UNSECURED_PORTION_NPL;
	}

	public void setR14_UNSECURED_PORTION_NPL(BigDecimal r14_UNSECURED_PORTION_NPL) {
		R14_UNSECURED_PORTION_NPL = r14_UNSECURED_PORTION_NPL;
	}

	public String getR15_PRODUCT() {
		return R15_PRODUCT;
	}

	public void setR15_PRODUCT(String r15_PRODUCT) {
		R15_PRODUCT = r15_PRODUCT;
	}

	public BigDecimal getR15_PERFORMING_EXPOSURE() {
		return R15_PERFORMING_EXPOSURE;
	}

	public void setR15_PERFORMING_EXPOSURE(BigDecimal r15_PERFORMING_EXPOSURE) {
		R15_PERFORMING_EXPOSURE = r15_PERFORMING_EXPOSURE;
	}

	public BigDecimal getR15_NON_PERFORMING() {
		return R15_NON_PERFORMING;
	}

	public void setR15_NON_PERFORMING(BigDecimal r15_NON_PERFORMING) {
		R15_NON_PERFORMING = r15_NON_PERFORMING;
	}

	public BigDecimal getR15_SPECIFIC_PROV() {
		return R15_SPECIFIC_PROV;
	}

	public void setR15_SPECIFIC_PROV(BigDecimal r15_SPECIFIC_PROV) {
		R15_SPECIFIC_PROV = r15_SPECIFIC_PROV;
	}

	public BigDecimal getR15_UNSECURED_PORTION_NPL() {
		return R15_UNSECURED_PORTION_NPL;
	}

	public void setR15_UNSECURED_PORTION_NPL(BigDecimal r15_UNSECURED_PORTION_NPL) {
		R15_UNSECURED_PORTION_NPL = r15_UNSECURED_PORTION_NPL;
	}

	public String getR16_PRODUCT() {
		return R16_PRODUCT;
	}

	public void setR16_PRODUCT(String r16_PRODUCT) {
		R16_PRODUCT = r16_PRODUCT;
	}

	public BigDecimal getR16_PERFORMING_EXPOSURE() {
		return R16_PERFORMING_EXPOSURE;
	}

	public void setR16_PERFORMING_EXPOSURE(BigDecimal r16_PERFORMING_EXPOSURE) {
		R16_PERFORMING_EXPOSURE = r16_PERFORMING_EXPOSURE;
	}

	public BigDecimal getR16_NON_PERFORMING() {
		return R16_NON_PERFORMING;
	}

	public void setR16_NON_PERFORMING(BigDecimal r16_NON_PERFORMING) {
		R16_NON_PERFORMING = r16_NON_PERFORMING;
	}

	public BigDecimal getR16_SPECIFIC_PROV() {
		return R16_SPECIFIC_PROV;
	}

	public void setR16_SPECIFIC_PROV(BigDecimal r16_SPECIFIC_PROV) {
		R16_SPECIFIC_PROV = r16_SPECIFIC_PROV;
	}

	public BigDecimal getR16_UNSECURED_PORTION_NPL() {
		return R16_UNSECURED_PORTION_NPL;
	}

	public void setR16_UNSECURED_PORTION_NPL(BigDecimal r16_UNSECURED_PORTION_NPL) {
		R16_UNSECURED_PORTION_NPL = r16_UNSECURED_PORTION_NPL;
	}

	public String getR17_PRODUCT() {
		return R17_PRODUCT;
	}

	public void setR17_PRODUCT(String r17_PRODUCT) {
		R17_PRODUCT = r17_PRODUCT;
	}

	public BigDecimal getR17_PERFORMING_EXPOSURE() {
		return R17_PERFORMING_EXPOSURE;
	}

	public void setR17_PERFORMING_EXPOSURE(BigDecimal r17_PERFORMING_EXPOSURE) {
		R17_PERFORMING_EXPOSURE = r17_PERFORMING_EXPOSURE;
	}

	public BigDecimal getR17_NON_PERFORMING() {
		return R17_NON_PERFORMING;
	}

	public void setR17_NON_PERFORMING(BigDecimal r17_NON_PERFORMING) {
		R17_NON_PERFORMING = r17_NON_PERFORMING;
	}

	public BigDecimal getR17_SPECIFIC_PROV() {
		return R17_SPECIFIC_PROV;
	}

	public void setR17_SPECIFIC_PROV(BigDecimal r17_SPECIFIC_PROV) {
		R17_SPECIFIC_PROV = r17_SPECIFIC_PROV;
	}

	public BigDecimal getR17_UNSECURED_PORTION_NPL() {
		return R17_UNSECURED_PORTION_NPL;
	}

	public void setR17_UNSECURED_PORTION_NPL(BigDecimal r17_UNSECURED_PORTION_NPL) {
		R17_UNSECURED_PORTION_NPL = r17_UNSECURED_PORTION_NPL;
	}

	public String getR18_PRODUCT() {
		return R18_PRODUCT;
	}

	public void setR18_PRODUCT(String r18_PRODUCT) {
		R18_PRODUCT = r18_PRODUCT;
	}

	public BigDecimal getR18_PERFORMING_EXPOSURE() {
		return R18_PERFORMING_EXPOSURE;
	}

	public void setR18_PERFORMING_EXPOSURE(BigDecimal r18_PERFORMING_EXPOSURE) {
		R18_PERFORMING_EXPOSURE = r18_PERFORMING_EXPOSURE;
	}

	public BigDecimal getR18_NON_PERFORMING() {
		return R18_NON_PERFORMING;
	}

	public void setR18_NON_PERFORMING(BigDecimal r18_NON_PERFORMING) {
		R18_NON_PERFORMING = r18_NON_PERFORMING;
	}

	public BigDecimal getR18_SPECIFIC_PROV() {
		return R18_SPECIFIC_PROV;
	}

	public void setR18_SPECIFIC_PROV(BigDecimal r18_SPECIFIC_PROV) {
		R18_SPECIFIC_PROV = r18_SPECIFIC_PROV;
	}

	public BigDecimal getR18_UNSECURED_PORTION_NPL() {
		return R18_UNSECURED_PORTION_NPL;
	}

	public void setR18_UNSECURED_PORTION_NPL(BigDecimal r18_UNSECURED_PORTION_NPL) {
		R18_UNSECURED_PORTION_NPL = r18_UNSECURED_PORTION_NPL;
	}

	public String getR19_PRODUCT() {
		return R19_PRODUCT;
	}

	public void setR19_PRODUCT(String r19_PRODUCT) {
		R19_PRODUCT = r19_PRODUCT;
	}

	public BigDecimal getR19_PERFORMING_EXPOSURE() {
		return R19_PERFORMING_EXPOSURE;
	}

	public void setR19_PERFORMING_EXPOSURE(BigDecimal r19_PERFORMING_EXPOSURE) {
		R19_PERFORMING_EXPOSURE = r19_PERFORMING_EXPOSURE;
	}

	public BigDecimal getR19_NON_PERFORMING() {
		return R19_NON_PERFORMING;
	}

	public void setR19_NON_PERFORMING(BigDecimal r19_NON_PERFORMING) {
		R19_NON_PERFORMING = r19_NON_PERFORMING;
	}

	public BigDecimal getR19_SPECIFIC_PROV() {
		return R19_SPECIFIC_PROV;
	}

	public void setR19_SPECIFIC_PROV(BigDecimal r19_SPECIFIC_PROV) {
		R19_SPECIFIC_PROV = r19_SPECIFIC_PROV;
	}

	public BigDecimal getR19_UNSECURED_PORTION_NPL() {
		return R19_UNSECURED_PORTION_NPL;
	}

	public void setR19_UNSECURED_PORTION_NPL(BigDecimal r19_UNSECURED_PORTION_NPL) {
		R19_UNSECURED_PORTION_NPL = r19_UNSECURED_PORTION_NPL;
	}

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

	public Date getReportResubDate() {
		return reportResubDate;
	}

	public void setReportResubDate(Date reportResubDate) {
		this.reportResubDate = reportResubDate;
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

	public M_SRWA_12E_LTV_Resub_Summary_Entity() {
		super();
	}

	public BigDecimal getR13_TOTAL() {
		return R13_TOTAL;
	}

	public void setR13_TOTAL(BigDecimal r13_TOTAL) {
		R13_TOTAL = r13_TOTAL;
	}

	public BigDecimal getR14_TOTAL() {
		return R14_TOTAL;
	}

	public void setR14_TOTAL(BigDecimal r14_TOTAL) {
		R14_TOTAL = r14_TOTAL;
	}

	public BigDecimal getR15_TOTAL() {
		return R15_TOTAL;
	}

	public void setR15_TOTAL(BigDecimal r15_TOTAL) {
		R15_TOTAL = r15_TOTAL;
	}

	public BigDecimal getR16_TOTAL() {
		return R16_TOTAL;
	}

	public void setR16_TOTAL(BigDecimal r16_TOTAL) {
		R16_TOTAL = r16_TOTAL;
	}

	public BigDecimal getR17_TOTAL() {
		return R17_TOTAL;
	}

	public void setR17_TOTAL(BigDecimal r17_TOTAL) {
		R17_TOTAL = r17_TOTAL;
	}

	public BigDecimal getR18_TOTAL() {
		return R18_TOTAL;
	}

	public void setR18_TOTAL(BigDecimal r18_TOTAL) {
		R18_TOTAL = r18_TOTAL;
	}

	public BigDecimal getR19_TOTAL() {
		return R19_TOTAL;
	}

	public void setR19_TOTAL(BigDecimal r19_TOTAL) {
		R19_TOTAL = r19_TOTAL;
	}

}